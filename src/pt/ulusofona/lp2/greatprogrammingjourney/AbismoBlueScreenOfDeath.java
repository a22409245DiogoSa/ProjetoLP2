package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoBlueScreenOfDeath extends AbismoPai {

    public AbismoBlueScreenOfDeath(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        gestorJogo.eliminatePlayer(jogador);
        return "Caiu em " + nome;
    }
}