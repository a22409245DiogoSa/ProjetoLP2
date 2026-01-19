package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String id;
    private String nome;
    private String linguagens;
    private String cor;
    private int posicao = 1;
    private boolean alive = true;
    private int movimentosRealizados = 0;
    private String motivoParagem = "Em Jogo";
    private List<String> ferramentas = new ArrayList<>();
    private int lastPosition = 1;
    private int secondLastPosition = 1;

    public Player(String id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor;
    }

    public void setPosicao(int pos) {
        this.secondLastPosition = this.lastPosition;
        this.lastPosition = this.posicao;
        this.posicao = pos;
    }

    public int getPosicao() { return posicao; }
    public String getNome() { return nome; }
    public String getId() { return id; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public void incMovimentos() { this.movimentosRealizados++; }
    public int getMovimentosRealizados() { return movimentosRealizados; }
    public String getMotivoParagem() { return motivoParagem; }
    public void setMotivoParagem(String motivo) { this.motivoParagem = motivo; }
    public int getLastPosition() { return lastPosition; }
    public int getSecondLastPosition() { return secondLastPosition; }
    public List<String> getFerramentas() { return ferramentas; }
    public String getLinguagens() { return linguagens; }

    public void setFerramentas(List<String> f) { this.ferramentas = f; }
    public void setPosicaoForLoad(int pos) { this.posicao = pos; }
    public void setLastPosition(int lp) { this.lastPosition = lp; }
    public void setSecondLastPosition(int slp) { this.secondLastPosition = slp; }
    public String getCor() { return cor; }

    public String[] toArray() {
        String toolsStr = ferramentas.isEmpty() ? "No tools" : String.join(";", ferramentas);
        return new String[]{id, nome, linguagens, cor, String.valueOf(posicao), toolsStr, alive ? "Em Jogo" : "Derrotado"};
    }

    public String getLinguagensOrdenadas() {
        return linguagens; // Simplificado para o exemplo
    }
}