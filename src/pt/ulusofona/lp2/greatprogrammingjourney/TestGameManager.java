package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class TestGameManager {

    @Test
    public void test_001_CriarTabuleiroInicial_FormatoCorretoCor() {
        String[][] jogadores = {
                {"1", "Joao", "Java", "Blue"},
                {"2", "Ana", "C", "red"}
        };
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20));

        assertEquals("Blue", gm.getPlayerById("1").getCor());
        assertEquals("Red", gm.getPlayerById("2").getCor());
    }

    @Test
    public void test_002_ObterInfoProgramador_SeteElementosEOrdem() {
        String[][] jogadores = {
                {"1", "P1", "Python", "Green"},
                {"2", "P2", "C;Java", "Red"}
        };
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20));

        String[] info = gm.getProgrammerInfo(1);

        assertNotNull(info);
        assertEquals(7, info.length);

        assertEquals("1", info[0]);
        assertEquals("P1", info[1]);
        assertEquals("Python", info[2]);
        assertEquals("Green", info[3]);
        assertEquals("1", info[4]);
    }


    @Test
    public void test_003_RestricoesMovimento_C_Assembly() {
        String[][] jogadores = {
                {"1", "P1", "C;Java", "Red"},
                {"2", "P2", "Assembly;Python", "Blue"}
        };
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20));


        assertFalse(gm.moveCurrentPlayer(4));
        assertTrue(gm.moveCurrentPlayer(3));
        assertEquals(4, gm.getPlayerById("1").getPosicao());
        gm.reactToAbyssOrTool();


        assertFalse(gm.moveCurrentPlayer(3));
        assertTrue(gm.moveCurrentPlayer(2));
        assertEquals(3, gm.getPlayerById("2").getPosicao());
    }



    @Test
    public void test_004_BSOD_EliminaEAvançaTurno_SemDuploAvanço() {
        String[][] jogadores = {
                {"1", "P1", "Java", "Red"},
                {"2", "P2", "Python", "Blue"},
                {"3", "P3", "C", "Green"}
        };
        String[][] objetos = {{"0", "7", "2"}};
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20, objetos));

        gm.moveCurrentPlayer(1);

        gm.reactToAbyssOrTool();

        assertFalse(gm.getPlayerById("1").isAlive());

        assertEquals("2", gm.currentPlayer);

        assertFalse(gm.getProgrammersInfo().contains("P1"));
    }

    @Test
    public void test_005_Abismo_CicloInfinito_DefineEstadoPreso() {
        String[][] jogadores = {{"1", "P1", "Java", "Red"}, {"2", "P2", "C", "Blue"}};
        String[][] objetos = {{"0", "8", "3"}};
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20, objetos));

        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();

        assertTrue(gm.getProgrammerInfoAsStr(1).contains("Preso"));

        assertEquals("2", gm.currentPlayer);
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();

        assertFalse(gm.moveCurrentPlayer(1));
        assertEquals("2", gm.currentPlayer);
    }



    @Test
    public void test_006_EliminaçãoTerminaJogo_SobreviventeDeclarado() {

        String[][] jogadores = {{"1", "P1", "Java", "Red"}, {"2", "P2", "Python", "Blue"}};
        String[][] objetos = {{"0", "7", "2"}};
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20, objetos));

        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();

        assertTrue(gm.gameIsOver(), "O jogo deve ter o estado TERMINADO após a eliminação do penúltimo jogador.");

        assertEquals("2", gm.currentPlayer, "O jogador atual deve ser P2 (o sobrevivente) no fim do jogo.");

        ArrayList<String> resultados = gm.getGameResults();

        boolean p2Encontrado = false;
        boolean p1Encontrado = false;

        for (String resultado : resultados) {
            if (resultado.contains("P2")) {
                p2Encontrado = true;
            }
            if (resultado.contains("P1 2")) {
                p1Encontrado = true;
            }
        }

        assertTrue(p2Encontrado, "P2 deve ser listado como VENCEDOR/SOBREVIVENTE nos resultados.");
        // Corrigido para P1 2, correspondente ao que foi procurado no loop
        assertTrue(p1Encontrado, "P1 deve estar na lista de RESTANTES (formato Nome + Posição final: P1 2).");
    }

    @Test
    public void test_007_pegaFerramenta_E_FormatoInfoProgramadores() {
        String[][] jogadores = {
                {"1", "P1", "Java", "Red"},
                {"2", "P2", "Python", "Blue"}
        };
        String[][] objetos = {{"1", "2", "3"}};
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 20, objetos));

        assertTrue(gm.moveCurrentPlayer(2));

        gm.reactToAbyssOrTool();

        assertEquals("2", gm.currentPlayer);

        String infoP1 = gm.getProgrammerInfoAsStr(1);
        assertTrue(infoP1.contains("Testes Unitários"), "P1 deve ter ganho a ferramenta.");

        String infoTodosJogadores = gm.getProgrammersInfo();

        String substringEsperada = "P1 : Testes Unitários | P2 : No tools";


        assertTrue(infoTodosJogadores.contains("P1 : Testes Unitários"), "P1 deve mostrar a ferramenta no resumo global.");
        assertTrue(infoTodosJogadores.contains("P2 : No tools"), "P2 deve mostrar 'No tools' no resumo global.");
        assertEquals(substringEsperada, infoTodosJogadores, "O formato e conteúdo de getProgrammersInfo está incorreto.");
    }

    @Test
    public void test_008_JogoTermina_VitoriaPorChegarAoFim() {
        String[][] jogadores = {{"1", "P1", "Java", "Red"}, {"2", "P2", "Python", "Blue"}};
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadores, 6));

        assertTrue(gm.moveCurrentPlayer(5));

        assertTrue(gm.gameIsOver(), "O jogo deve terminar quando o jogador chega à casa final.");

        ArrayList<String> resultados = gm.getGameResults();

        assertTrue(resultados.contains("P1"), "O vencedor P1 deve ser listado nos resultados.");
        assertTrue(resultados.contains("P2 1"), "O restante P2 deve estar na posição 1.");
    }

    @Test
    public void test_009_TabuleiroInicial_TamanhoInvalido() {
        String[][] jogadores = {
                {"1", "P1", "Java", "Red"},
                {"2", "P2", "Python", "Blue"},
                {"3", "P3", "C", "Green"},
                {"4", "P4", "Assembly", "Yellow"}
        };
        GameManager gm = new GameManager();
        assertFalse(gm.createInitialBoard(jogadores, 4), "Deve falhar se o tamanho for menor que 2 * NumJogadores.");
    }

}