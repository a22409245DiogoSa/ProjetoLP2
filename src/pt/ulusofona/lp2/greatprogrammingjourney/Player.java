package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe que representa um Jogador no domínio do jogo "Great Programming Journey".
 * Gere informações como identificação, posição no tabuleiro, linguagens conhecidas e estado.
 */
public class Player {
    // Atributos privados para encapsulamento
    private String id;
    private String nome;
    private String linguagens; // String original com linguagens separadas por ";"
    private String cor;
    private int posicao = 1; // Posição atual, inicia na casa 1
    private boolean alive;   // Estado do jogador (ativo/derrotado)

    private List<String> ferramentas = new ArrayList<>(); // Lista de ferramentas adquiridas

    // Histórico de posições para suportar movimentos de recuo ou lógica de jogo
    private int lastPosition = 1;
    private int secondLastPosition = 1;


    private int movimentosRealizados = 0;
    private String motivoParagem = "Em Jogo";

    // Construtor: Inicializa os dados básicos e formata a cor
    public Player(String id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        // Normaliza a cor para Capitalized (ex: "VERMELHO" -> "Vermelho")
        this.cor = cor.substring(0, 1).toUpperCase() + cor.substring(1).toLowerCase();
    }

    public int getMovimentosRealizados() { return movimentosRealizados; }
    public void incMovimentos() { this.movimentosRealizados++; }

    public String getMotivoParagem() { return motivoParagem; }
    public void setMotivoParagem(String motivo) { this.motivoParagem = motivo; }


    // Getters e Setters para o estado de vida
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    // Getters básicos
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCor() { return cor; }
    public int getPosicao() { return posicao; }

    /**
     * Atualiza a posição e gere o histórico das duas últimas posições ocupadas.
     */
    public void setPosicao(int pos) {
        this.secondLastPosition = this.lastPosition;
        this.lastPosition = this.posicao; // A posição atual antes de mudar
        this.posicao = pos;
    }

    /**
     * Adiciona uma ferramenta à lista se esta não for nula e ainda não existir.
     */
    public void addFerramenta(String f) {
        if (f != null && !ferramentas.contains(f)) {
            ferramentas.add(f);
        }
    }

    public List<String> getFerramentas() {
        return ferramentas;
    }

    /**
     * Processa a string de linguagens, ordena-as alfabeticamente e devolve formatada.
     */
    public String getLinguagensOrdenadas() {
        String[] arr = linguagens.split(";");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim(); // Remove espaços extra
        }
        Arrays.sort(arr); // Ordenação alfabética
        return String.join("; ", arr);
    }

    /**
     * Converte o objeto Player num array de Strings para facilitar a exibição em tabelas/UI.
     */
    public String[] toArray() {
        String toolsStr = ferramentas.isEmpty() ? "No tools" : String.join(";", ferramentas);

        String estado;
        if (!isAlive()) {
            estado = "Derrotado";
        } else {
            estado = "Em Jogo";
        }

        return new String[]{
                this.id,
                this.nome,
                this.getLinguagensOrdenadas(),
                this.cor = firstLetterMaiuscula(cor), // Garante formatação da cor no retorno
                String.valueOf(this.posicao),
                toolsStr,
                estado
        };
    }

    /**
     * Devolve uma representação textual resumida do estado do jogador.
     */
    public String getInfoAsStr(int boardSize) {
        String tools = ferramentas.isEmpty() ? "No tools"
                : String.join("; ", ferramentas);

        return id + " | " + nome + " | " + posicao + " | " + tools +
                " | " + getLinguagensOrdenadas() + " | Em Jogo";
    }

    public String getLinguagens() {
        return linguagens;
    }

    // Métodos para gestão de histórico de posições
    public int getLastPosition() { return lastPosition; }
    public int getSecondLastPosition() { return secondLastPosition; }

    /**
     * Setter específico para carregamento de dados (load), sem afetar o histórico lastPosition.
     */
    public void setPosicaoForLoad(int pos) {
        this.posicao = pos;
    }

    public void setLastPosition(int lastPosition) { this.lastPosition = lastPosition; }
    public void setSecondLastPosition(int secondLastPosition) { this.secondLastPosition = secondLastPosition; }

    public void setFerramentas(List<String> ferramentas) {
        this.ferramentas = ferramentas;
    }

    /**
     * Método utilitário privado para capitalizar strings (Primeira letra maiúscula).
     */
    private String firstLetterMaiuscula(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}