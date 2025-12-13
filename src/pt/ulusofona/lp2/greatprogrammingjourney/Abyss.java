package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.AbyssOrTool;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

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
        // 1. Mapeamento de Ferramentas (AGORA COM LÓGICA DEFINIDA)
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

        // 2. Tenta Anular o Abismo
        if (ferramentaNecessaria != null && p.getFerramentas().contains(ferramentaNecessaria)) {
            // Consome a ferramenta
            p.getFerramentas().remove(ferramentaNecessaria);

            // Retorna a mensagem pedida: "<Nome do Abismo> anulado por <Nome da Ferramenta>"
            return this.name + " anulado por " + ferramentaNecessaria;
        }

        // 3. Aplica Efeito do Abismo (Se não foi anulado)
        int novaPosicao = p.getPosicao();

        switch (id) {
            case 0: // Syntax Error → recua 1 casa
                novaPosicao = Math.max(1, p.getPosicao() - 1);
                break;
            case 1: // Logic Error → Recua 2 casas (Ajuste para testes)
                novaPosicao = Math.max(1, p.getPosicao() - 2);
                break;
            case 2: // Exception → eliminado
            case 7: // BSOD → eliminado
            case 9: // Segmentation Fault → eliminado
                gm.eliminatePlayer(p);
                // Se eliminado, não precisa de atualizar a posição
                return "Caiu em " + name;
            case 3: // FileNotFound → Recua 3 casas (Ajuste para testes)
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;
            case 4: // Crash → perde 2 turnos
                gm.skipTurns(p, 2);
                break;
            case 5: // Duplicated Code → vai para meio do tabuleiro
                novaPosicao = Math.max(1, gm.boardSize / 2);
                break;
            case 6: // Side Effects → volta ao início
                novaPosicao = 1;
                break;
            case 8: // Infinite Loop → perde 3 turnos
                gm.skipTurns(p, 3);
                break;
            default:
                // Nenhum efeito ou efeito desconhecido
                break;
        }

        // Se houve alteração de posição (e não foi eliminado/saltou turno), atualiza.
        if (novaPosicao != p.getPosicao()) {
            gm.setPlayerPosition(p, novaPosicao);
        }

        // Retorna a mensagem de queda
        return "Caiu em " + name;
    }
}