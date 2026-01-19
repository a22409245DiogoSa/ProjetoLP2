package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoErroLogico extends AbismoPai {

    public AbismoErroLogico(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int ultimoLancamento = gestorJogo.getLastDiceRoll();
        int recuo = (int) Math.floor(ultimoLancamento / 2.0);
        int novaPosicao = Math.max(1, jogador.getPosicao() - recuo);

        if (novaPosicao != jogador.getPosicao()) {
            gestorJogo.setPlayerPosition(jogador, novaPosicao);
        }

        return "Caiu em " + nome;
    }
}