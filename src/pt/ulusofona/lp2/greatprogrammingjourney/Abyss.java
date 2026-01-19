package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.HashMap;

public class Abyss extends AbyssOrTool {

    public Abyss(int id, String name, int position) {
        super(id, name, position);
    }

    @Override
    public String getType() {
        return "Abyss";
    }

    @Override
    public String apply(Player p, GameManager gm) {
        // Mapeamento de quais ferramentas anulam quais abismos
        HashMap<Integer, String> anulacoes = new HashMap<>();
        anulacoes.put(0, "IDE");
        anulacoes.put(1, "Testes Unitários");
        anulacoes.put(2, "Tratamento de Excepções");
        anulacoes.put(3, "Tratamento de Excepções");
        anulacoes.put(4, "Ajuda Do Professor");
        anulacoes.put(5, "Herança");
        anulacoes.put(6, "Programação Funcional");
        anulacoes.put(7, "Ajuda Do Professor");
        anulacoes.put(8, "Programação Funcional");
        anulacoes.put(9, "IDE");

        String ferramentaNecessaria = anulacoes.get(id);

        // Lógica de Proteção: Se o jogador tiver a ferramenta, o abismo é ignorado e a ferramenta gasta
        if (ferramentaNecessaria != null && p.getFerramentas().contains(ferramentaNecessaria)) {
            p.getFerramentas().remove(ferramentaNecessaria);
            return this.name + " anulado por " + ferramentaNecessaria;
        }

        int novaPosicao = p.getPosicao();

        // Lógica de penalização específica para cada ID de abismo
        switch (id) {
            case 0:
                novaPosicao = Math.max(1, p.getPosicao() - 1);
                break;
            case 1: // Logic Error: Recua metade do dado
                int recuo = (int) Math.floor(gm.getLastDiceRoll() / 2.0);
                novaPosicao = Math.max(1, p.getPosicao() - recuo);
                break;
            case 2:
                novaPosicao = Math.max(1, p.getPosicao() - 2);
                break;
            case 3:
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;
            case 4:
                novaPosicao = 1;
                break; // Crash: Início do tabuleiro
            case 5:
                novaPosicao = p.getLastPosition();
                break; // Código Duplicado
            case 6:
                novaPosicao = p.getSecondLastPosition();
                break; // Efeitos Secundários
            case 7: // BSOD: Morte súbita
                gm.eliminatePlayer(p);
                return "Caiu em " + name;
            case 8:
                gm.skipTurns(p, 3);
                break; // Ciclo Infinito: 3 turnos preso
            case 9:
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break; // SegFault

            case 20: // LLM
                String ferramentaLLM = "Ajuda Do Professor";
                int ultimoDado = gm.getLastDiceRoll();
                // Regra: A partir do 4º movimento, avança com ressalto
                if (p.getMovimentosRealizados() >= 4) {
                    novaPosicao = p.getPosicao() + ultimoDado;
                    if (novaPosicao > gm.getBoardSize()) {
                        int excesso = novaPosicao - gm.getBoardSize();
                        novaPosicao = gm.getBoardSize() - excesso;
                    }
                    gm.setPlayerPosition(p, novaPosicao);
                    return "LLM: Avançou " + ultimoDado + " casas";
                }

                // Regra: Antes do 4º movimento, verifica ferramenta ou recua
                if (p.getFerramentas().contains(ferramentaLLM)) {
                    p.getFerramentas().remove(ferramentaLLM);
                    return name + " anulado por " + ferramentaLLM;
                } else {
                    novaPosicao = p.getLastPosition();
                    gm.setPlayerPosition(p, novaPosicao);
                    return "Caiu no " + name + ": Recuou para a posição onde estava antes ";
                }
            case 150: {
                    String linguagem = p.getLinguagens();
                    int count = 0;
                    String firstLang = "";
                if (linguagem.contains(";")) {
                    firstLang = linguagem.split(";")[0];
                }
                String trimmedFirstLang = firstLang.trim();
                if (firstLang.equals("Javascript")) {
                    novaPosicao = Math.max(1, p.getPosicao() - 5);
                    break;
                }

            }
        }
        if (novaPosicao != p.getPosicao()) gm.setPlayerPosition(p, novaPosicao);
        return "Caiu em " + name;
    }
}