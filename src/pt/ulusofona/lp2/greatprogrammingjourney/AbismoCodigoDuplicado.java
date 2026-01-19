package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoCodigoDuplicado extends AbismoPai {

    public AbismoCodigoDuplicado(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int novaPosicao = jogador.getUltimaPosicao();
        gestorJogo.setPlayerPosition(jogador, novaPosicao);

        return "Caiu em " + nome;
    }
}
