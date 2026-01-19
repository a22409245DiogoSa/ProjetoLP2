package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbismoCicloInfinito extends AbismoPai {

    public AbismoCicloInfinito(int id, String nome, int posicao) {
        super(id, nome, posicao);
    }

    @Override
    public String aplicaJogador(Jogador jogador, GameManager gestorJogo) {

        // Se tem ferramenta: não fica preso e não liberta quem já estava preso
        if (anularComFerramenta(jogador)) {
            return this.nome + " anulado por " + anulacoes.get(id);
        }

        int pos = this.getPosicao();

        String presoAtual = gestorJogo.getPresoDoCiclo(pos);

        // ✅ Só liberta se estava lá OUTRO jogador preso
        if (presoAtual != null && !presoAtual.equals(jogador.getId())) {
            gestorJogo.libertarPresoDoCiclo(pos);
        }

        // ✅ Agora este jogador fica preso
        gestorJogo.prenderNoCiclo(pos, jogador.getId());

        return "Caiu em " + nome;
    }


}
