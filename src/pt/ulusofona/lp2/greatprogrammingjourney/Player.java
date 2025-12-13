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
        this.cor = cor;
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
        for (int i = 0; i < arr.length; i++) arr[i] = arr[i].trim();
        Arrays.sort(arr);
        return String.join("; ", arr);
    }

    public String[] toArray() {
        return new String[]{
                id,
                nome,
                getLinguagensOrdenadas(),
                cor,
                String.valueOf(posicao)
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
}
