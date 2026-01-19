package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    String id;
    String nome;
    String linguagens;
    String cor;
    int posicao = 1;
    private boolean alive;

    List<String> ferramentas = new ArrayList<>();

    private int lastPosition = 1;
    private int secondLastPosition = 1;

    public Player(String id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor.substring(0, 1).toUpperCase() + cor.substring(1).toLowerCase();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCor() { return cor; }
    public int getPosicao() { return posicao; }
    public void setPosicao(int pos) {
        this.secondLastPosition = this.lastPosition;
        this.lastPosition = this.posicao; // A posição atual antes de mudar
        this.posicao = pos;
    }

    public void addFerramenta(String f) {
        if (f != null && !ferramentas.contains(f)) {
            ferramentas.add(f);
        }
    }

    public List<String> getFerramentas() {
        return ferramentas;
    }

    public String getLinguagensOrdenadas() {
        String[] arr = linguagens.split(";");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim();
        }
        Arrays.sort(arr);
        return String.join("; ", arr);
    }

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
                this.cor = firstLetterMaiuscula(cor),
                String.valueOf(this.posicao),
                toolsStr,
                estado
        };
    }

    public String getInfoAsStr(int boardSize) {
        String tools = ferramentas.isEmpty() ? "No tools"
                : String.join("; ", ferramentas);

        return id + " | " + nome + " | " + posicao + " | " + tools +
                " | " + getLinguagensOrdenadas() + " | Em Jogo";
    }

    public String getLinguagens() {
        return linguagens;
    }

    public int getLastPosition() { return lastPosition; }
    public int getSecondLastPosition() { return secondLastPosition; }

    public void setPosicaoForLoad(int pos) {
        this.posicao = pos;
    }

    public void setLastPosition(int lastPosition) { this.lastPosition = lastPosition; }
    public void setSecondLastPosition(int secondLastPosition) { this.secondLastPosition = secondLastPosition; }

    public void setFerramentas(List<String> ferramentas) {
        this.ferramentas = ferramentas;
    }

    private String firstLetterMaiuscula(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


}
