package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.AbyssOrTool;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

import java.util.HashMap;
import java.util.List;

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
        // Mapa de ferramentas que podem anular cada abismo
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

        // Verifica se o jogador tem a ferramenta que anula o abismo
        String ferramentaNecessaria = anulacoes.get(id);
        if (id != 20 && ferramentaNecessaria != null && p.getFerramentas().contains(ferramentaNecessaria)) {
            p.getFerramentas().remove(ferramentaNecessaria);
            return this.name + " anulado por " + ferramentaNecessaria;
        }

        int novaPosicao = p.getPosicao();

        // Lógica de cada abismo
        switch (id) {
            case 0: // Syntax Error
                novaPosicao = Math.max(1, p.getPosicao() - 1);
                break;

            case 1: // Logic Error
                int lastRoll = gm.getLastDiceRoll();
                int recuo = (int) Math.floor(lastRoll / 2.0);
                novaPosicao = Math.max(1, p.getPosicao() - recuo);
                break;

            case 2: // Exception
                novaPosicao = Math.max(1, p.getPosicao() - 2);
                break;

            case 3: // FileNotFoundException
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;

            case 4: // Crash
                novaPosicao = 1;
                break;

            case 5: // Código Duplicado
                novaPosicao = p.getLastPosition();
                break;

            case 6: // Efeitos Secundários
                novaPosicao = p.getSecondLastPosition();
                break;

            case 7: // Blue Screen of Death
                gm.eliminatePlayer(p);
                return "Caiu em " + name;

            case 8: // Ciclo Infinito
                gm.skipTurns(p, 3);
                break;

            case 9: // Segmentation Fault
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;

            case 20: { // LLM
                // Detecta se o jogador ainda está antes do 4º movimento
                boolean antesDoQuartoMovimento = p.getSecondLastPosition() == 1;

                if (antesDoQuartoMovimento) {
                    // Retrocede para a posição anterior
                    novaPosicao = p.getLastPosition();

                    // Se tiver a ferramenta "Ajuda Do Professor", anula parcialmente o abismo
                    if (p.getFerramentas().contains("Ajuda Do Professor")) {
                        p.getFerramentas().remove("Ajuda Do Professor");
                        return name + " anulado por Ajuda Do Professor";
                    }
                } else {
                    // A partir do 4º movimento → avança o número de casas do último dado
                    novaPosicao = p.getPosicao() + gm.getLastDiceRoll();
                    // Ignora a ferramenta "Ajuda Do Professor"
                }
                break;
            }

            default:
                // Nenhuma ação adicional
                break;
        }

        // Atualiza a posição do jogador se mudou
        if (novaPosicao != p.getPosicao()) {
            gm.setPlayerPosition(p, novaPosicao);
        }

        return "Caiu em " + name;
    }
}
