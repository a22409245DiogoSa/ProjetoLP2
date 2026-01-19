package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Jogador {
    private String id;
    private String corAvatar;
    private String nome;
    private String linguagensFavoritas;
    private int posicao = 1;
    private int ultimaPosicao = 1;
    private int penultimaPosicao = 1;
    private boolean vivo = true;
    private ArrayList<String> ferramentas = new ArrayList<>();
    private boolean preso = false;
    private String estado = "Em Jogo";

    public Jogador(String id, String nome, String linguagensFavoritas, String corAvatar) {
        this.id = id;
        this.nome = nome;
        this.linguagensFavoritas = linguagensFavoritas;
        this.corAvatar = corAvatar;
    }

    public String getId() {
        return id;
    }

    public String getCorAvatar() {
        return corAvatar;
    }

    public String getNome() {
        return nome;
    }

    public String getLinguagensFavoritas() {
        return linguagensFavoritas;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
        if (!vivo) {
            this.estado = "Derrotado";
        }
    }

    public int getPosicao() {
        return posicao;
    }

    public int getUltimaPosicao() {
        return ultimaPosicao;
    }

    public int getPenultimaPosicao() {
        return penultimaPosicao;
    }

    public ArrayList<String> getFerramentas() {
        ArrayList<String> ferramentasOrg = new ArrayList<>(ferramentas);
        ferramentasOrg.sort(String::compareTo);
        return ferramentasOrg;
    }

    public void setFerramentas(ArrayList<String> ferramentas) {
        this.ferramentas = ferramentas;
    }

    public String getEstado() {
        return estado;
    }

    public boolean estaPreso() {
        return preso;
    }

    public void setPreso(boolean preso) {
        this.preso = preso;
        if (preso) {
            this.estado = "Preso";
        }
    }

    public void adicionarFerramenta(String ferramenta) {
        if (ferramenta != null && !ferramentas.contains(ferramenta)) {
            ferramentas.add(ferramenta);
        }
    }

    public void removerFerramenta(String ferramenta) {
        ferramentas.remove(ferramenta);
    }

    public void setPosicao(int novaPosicao) {
        this.penultimaPosicao = this.ultimaPosicao;
        this.ultimaPosicao = this.posicao;
        this.posicao = novaPosicao;
    }

    public void setUltimaPosicao(int ultimaPosicao) {
        this.ultimaPosicao = ultimaPosicao;
    }

    public void setPenultimaPosicao(int penultimaPosicao) {
        this.penultimaPosicao = penultimaPosicao;
    }

    public String getLinguagensOrdenadas() {
        if (linguagensFavoritas == null || linguagensFavoritas.isEmpty()) {
            return "";
        }
        String[] linguagens = linguagensFavoritas.split(";");
        for (int i = 0; i < linguagens.length; i++) {
            linguagens[i] = linguagens[i].trim();
        }
        Arrays.sort(linguagens);
        return String.join("; ", linguagens);
    }

    public String[] toArray() {
        String ferramentasStr;
        if (ferramentas.isEmpty()) {
            ferramentasStr = "No tools";
        } else {
            ferramentasStr = String.join(";", ferramentas);
        }

        return new String[] {
                id,
                nome,
                getLinguagensOrdenadas(),
                corAvatar,
                String.valueOf(posicao),
                ferramentasStr,
                estado
        };
    }
}