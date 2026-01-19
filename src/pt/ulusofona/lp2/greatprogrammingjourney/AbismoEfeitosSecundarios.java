package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoEfeitosSecundarios extends AbismoPai {

    public AbismoEfeitosSecundarios(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int novaPosicao = jogador.getPenultimaPosicao();
        gestorJogo.setPlayerPosition(jogador, novaPosicao);

        return "Caiu em " + nome;
    }
}