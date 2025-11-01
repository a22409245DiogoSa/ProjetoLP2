package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.Arrays;

public class Player {
    String id;
    String nome;
    String linguagens;
    String cor;
    int posicao = 1;

    public Player(String id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCor() { return cor; }
    public int getPosicao() { return posicao; }
    public void setPosicao(int pos) { this.posicao = pos; }

    public String getLinguagensOrdenadas() {
        String[] linguas = linguagens.split(";");
        for (int i = 0; i < linguas.length; i++) {
            linguas[i] = linguas[i].trim();
        }
        Arrays.sort(linguas);
        return String.join("; ", linguas);
    }

    public String[] toArray() {
        return new String[]{id, nome, getLinguagensOrdenadas(), cor, String.valueOf(posicao)};
    }

    public String getInfoAsStr(int boardSize) {
        String estado = (posicao >= boardSize) ? "Em Jogo" : "Em Jogo";
        return id + " | " + nome + " | " + posicao + " | " + getLinguagensOrdenadas() + " | " + estado;
    }
}
