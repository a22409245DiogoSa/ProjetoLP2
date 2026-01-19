package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoFileNotFoundException extends AbismoPai {

    public AbismoFileNotFoundException(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int novaPosicao = Math.max(1, jogador.getPosicao() - 3);
        if (novaPosicao != jogador.getPosicao()) {
            gestorJogo.setPlayerPosition(jogador, novaPosicao);
        }

        return "Caiu em " + nome;
    }
}