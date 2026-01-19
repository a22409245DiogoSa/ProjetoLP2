package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoCrash extends AbismoPai {

    public AbismoCrash(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int novaPosicao = 1;
        gestorJogo.setPlayerPosition(jogador, novaPosicao);

        return "Caiu em " + nome;
    }
}