package pt.ulusofona.lp2.greatprogrammingjourney;

public class Abyss extends AbyssOrTool {
    public Abyss(int id, String name, int position) {
        super(id, name, position);
    }

    @Override
    public String getType() { return "Abyss"; }

    @Override
    public String apply(Player p, GameManager gm) {
        int ultimoDado = gm.getLastDiceRoll();
        String ferramentaLLM = "Ajuda Do Professor";
        int novaPosicao = p.getPosicao();

        switch (id) {
            case 20: // ABISMO LLM
                if (p.getMovimentosRealizados() >= 4) {
                    int destino = p.getPosicao() + ultimoDado;
                    // Lógica de ressalto manual
                    if (destino > gm.getBoardSize()) {
                        int excesso = destino - gm.getBoardSize();
                        destino = gm.getBoardSize() - excesso;
                    }
                    gm.setPlayerPosition(p, destino);
                    return "LLM: Avançou " + ultimoDado + " casas";
                }

                if (p.getFerramentas().contains(ferramentaLLM)) {
                    p.getFerramentas().remove(ferramentaLLM);
                    return name + " anulado parcialmente por " + ferramentaLLM;
                } else {
                    novaPosicao = p.getLastPosition();
                    gm.setPlayerPosition(p, novaPosicao);
                    return "Caiu em " + name + ": Recuou";
                }

            case 7: // BSOD
                p.setMotivoParagem("Blue Screen of Death");
                gm.eliminatePlayer(p);
                return "Caiu em " + name;

            case 8: // Ciclo Infinito
                p.setMotivoParagem("Ciclo Infinito");
                gm.skipTurns(p, 3);
                return "Caiu em " + name;

            default:
                p.setMotivoParagem(this.name);
                if (id == 0) novaPosicao = Math.max(1, p.getPosicao() - 1);
                else if (id == 1) novaPosicao = Math.max(1, p.getPosicao() - (ultimoDado / 2));
                else if (id == 2 || id == 3) novaPosicao = Math.max(1, p.getPosicao() - 2);
                else if (id == 4) novaPosicao = 1;
                else if (id == 5) novaPosicao = p.getLastPosition();
                else if (id == 6) novaPosicao = p.getSecondLastPosition();
                else if (id == 9) novaPosicao = Math.max(1, p.getPosicao() - 3);

                if (novaPosicao != p.getPosicao()) {
                    gm.setPlayerPosition(p, novaPosicao);
                }
                break;
        }
        return "Caiu em " + name;
    }
}