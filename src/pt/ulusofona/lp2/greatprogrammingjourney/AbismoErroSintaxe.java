package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoErroSintaxe extends AbismoPai {

    public AbismoErroSintaxe(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return nome + " anulado por " + anulacoes.get(id);
        }

        int novaPosicao = Math.max(1, jogador.getPosicao() - 1);
        if (novaPosicao != jogador.getPosicao()) {
            gestorJogo.setPlayerPosition(jogador, novaPosicao);
        }

        return "Caiu em " + nome;
    }
}