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

        // Ferramentas que anulam cada abismo
        HashMap<Integer, String> anulacoes = new HashMap<>();
        anulacoes.put(0, "IDE");                       // Syntax Error
        anulacoes.put(1, "Testes Unitários");          // Logic Error
        anulacoes.put(2, "Tratamento de Excepções");   // Exception
        anulacoes.put(3, "Tratamento de Excepções");   // FileNotFound
        anulacoes.put(4, "Ajuda Do Professor");        // Crash
        anulacoes.put(5, "Herança");                   // Duplicated Code
        anulacoes.put(6, "Programação Funcional");     // Side Effects
        anulacoes.put(7, "Ajuda Do Professor");        // BSOD
        anulacoes.put(8, "Programação Funcional");     // Infinite Loop
        anulacoes.put(9, "IDE");                       // Segfault

        // Se o jogador tem a ferramenta correta → anula o abismo
        String ferramentaNecessaria = anulacoes.get(id);
        if (ferramentaNecessaria != null &&
                p.getFerramentas().contains(ferramentaNecessaria)) {

            p.getFerramentas().remove(ferramentaNecessaria);
            return "Avoided " + name + " using " + ferramentaNecessaria;
        }

        // Sem ferramenta → aplicar efeito correspondente
        switch (id) {

            case 0: // Syntax Error → recua 1 casa
                p.setPosicao(Math.max(1, p.getPosicao() - 1));
                return "Fell into " + name;

            case 1: // Logic Error → perde 1 turno
                gm.skipTurns(p, 1);
                return "Fell into " + name;

            case 2: // Exception → eliminado
                gm.eliminatePlayer(p);
                return "Fell into " + name;

            case 3: // FileNotFound → recua 2 casas
                p.setPosicao(Math.max(1, p.getPosicao() - 2));
                return "Fell into " + name;

            case 4: // Crash → perde 2 turnos
                gm.skipTurns(p, 2);
                return "Fell into " + name;

            case 5: // Duplicated Code → vai para meio do tabuleiro
                p.setPosicao(Math.max(1, gm.boardSize / 2));
                return "Fell into " + name;

            case 6: // Side Effects → volta ao início
                p.setPosicao(1);
                return "Fell into " + name;

            case 7: // BSOD → eliminado
                gm.eliminatePlayer(p);
                return "Fell into " + name;

            case 8: // Infinite Loop → perde 3 turnos
                gm.skipTurns(p, 3);
                return "Fell into " + name;

            case 9: // Segmentation Fault → eliminado
                gm.eliminatePlayer(p);
                return "Fell into " + name;
        }

        return "Fell into " + name;
    }
}