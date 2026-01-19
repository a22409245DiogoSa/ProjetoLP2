package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class AbismoOuFerramenta {
    protected int id;
    protected String nome;
    protected int posicao;

    public AbismoOuFerramenta(int id, String nome, int posicao) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPosicao() {  // Corrigido: estava getPosicaon()
        return posicao;
    }

    public abstract String getTipo();
    public abstract String aplicaJogador(Jogador jogador, GameManager gameManager);
}