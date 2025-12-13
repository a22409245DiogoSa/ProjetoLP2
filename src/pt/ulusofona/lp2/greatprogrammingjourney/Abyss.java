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
            p.getFerramentas().remove(ferramentaNecessaria);
            return this.name + " anulado por " + ferramentaNecessaria;
        }

        // 3. Aplica Efeito do Abismo (REGRAS FINAIS)
        int novaPosicao = p.getPosicao();

        switch (id) {
            case 0: // Syntax Error → recua 1 casa
                novaPosicao = Math.max(1, p.getPosicao() - 1);
                break;

            case 1: // Logic Error → Recua N casas = floor(dado / 2) E perde 1 turno
                int lastRoll = gm.getLastDiceRoll();
                int recuo = (int) Math.floor(lastRoll / 2.0);

                novaPosicao = Math.max(1, p.getPosicao() - recuo);
                // REMOVIDO: gm.skipTurns(p, 1); // Não perdem turno
                break;

            case 2:
                novaPosicao = Math.max(1, p.getPosicao() - 2);
                break;

            case 3: // FileNotFound → Recua 3 casas (Mantida a interpretação)
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;

            case 4: // Crash → volta à primeira casa (posição 1). **NÃO PERDE TURNO**
                novaPosicao = 1;
                break;

            case 5: // Código Duplicado → recua para a posição anterior E perde 1 turno
                novaPosicao = p.getLastPosition();
                // REMOVIDO: gm.skipTurns(p, 1); // Não perdem turno
                break;

            case 6: // Efeitos Secundários → recua para a posição de 2 movimentos atrás E perde 1 turno
                novaPosicao = p.getSecondLastPosition();
                // REMOVIDO: gm.skipTurns(p, 1); // Não perdem turno
                break;

            case 7: // BSOD → perde imediatamente o jogo
                gm.eliminatePlayer(p);
                return "Caiu em " + name;

            case 8: // Ciclo Infinito → perde 3 turnos
                gm.skipTurns(p, 3);
                break;

            case 9: // Segmentation Fault → Se >= 2 jogadores, todos recuam 3 casas
                novaPosicao = Math.max(1, p.getPosicao() - 3);
                break;
        }

        // Se houve alteração de posição (e não foi eliminado/saltou turno), atualiza.
        if (novaPosicao != p.getPosicao()) {
            gm.setPlayerPosition(p, novaPosicao);
        }

        return "Caiu em " + name;
    }
    }
