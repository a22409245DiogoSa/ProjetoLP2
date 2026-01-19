package pt.ulusofona.lp2.greatprogrammingjourney;

public class Ferramenta extends AbismoOuFerramenta {


    public Ferramenta(int id, String nome, int posicaon) {
        super(id, nome, posicaon);
    }


    @Override
    public String getTipo() {
        return "Tool";
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gameManager) {
        if (jogador.getFerramentas().contains(this.nome)) {
            return "Já possui " + this.nome + ", não apanhou novamente.";
        }
        jogador.adicionarFerramenta(this.nome);
        return "Apanhou Ferramenta: " + this.nome;
    }
}
