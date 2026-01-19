package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {
    @Test
    public void testCreateInitialBoard_TamanhoInvalido() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        assertFalse(gm.createInitialBoard(jogadores, 3, null));
    }

    @Test
    public void testCreateInitialBoard_NumeroJogadoresInvalido() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"}
        };

        assertFalse(gm.createInitialBoard(jogadores, 10, null));
    }

    @Test
    public void testMoveCurrentPlayer_MovimentoValido() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        assertTrue(gm.moveCurrentPlayer(3));
        assertEquals("1", gm.getJogadorAtual()); // Usar getter
    }

    @Test
    public void testMoveCurrentPlayer_MovimentoInvalido() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "C", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        assertFalse(gm.moveCurrentPlayer(4));
    }

    @Test
    public void testReactToAbyssOrTool_ComFerramenta() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        String[][] objetos = {
                {"1", "0", "3"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, objetos));
        gm.moveCurrentPlayer(2);
        String resultado = gm.reactToAbyssOrTool();
        assertNotNull(resultado);
        assertTrue(resultado.contains("Apanhou Ferramenta:"));
    }

    @Test
    public void testGetGameResults_JogoNaoTerminado() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        ArrayList<String> resultados = gm.getGameResults();
        assertTrue(resultados.isEmpty());
    }

    @Test
    public void testEliminatePlayer() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"},
                {"3", "Charlie", "C++", "Purple"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        Jogador bob = gm.getJogadorById("2");
        gm.eliminatePlayer(bob);
        assertFalse(bob.estaVivo());
        assertEquals("1", gm.getJogadorAtual()); // Usar getter
    }

    @Test
    public void testGetSlotInfo() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        String[][] objetos = {
                {"0", "2", "5"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, objetos));
        String[] slotInfo = gm.getSlotInfo(5);
        assertEquals(3, slotInfo.length);
        assertEquals("Exception", slotInfo[1]);
        assertEquals("A:2", slotInfo[2]);
    }

    @Test
    public void testGetProgrammerInfoAsStr() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java;Python", "Blue"},
                {"2", "Bob", "C;Assembly", "Green"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        String info = gm.getProgrammerInfoAsStr(1);
        assertNotNull(info);
        assertTrue(info.contains("Alice"));
        assertTrue(info.contains("Java; Python"));
        assertTrue(info.contains("Em Jogo"));
    }

    @Test
    public void testLoadAndSaveGame() {
        GameManager gm1 = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        String[][] objetos = {
                {"0", "3", "4"},
                {"1", "1", "6"}
        };

        assertTrue(gm1.createInitialBoard(jogadores, 10, objetos));
        gm1.moveCurrentPlayer(2);
        gm1.reactToAbyssOrTool();

        try {
            java.io.File tempFile = java.io.File.createTempFile("test_save", ".txt");
            assertTrue(gm1.saveGame(tempFile));

            GameManager gm2 = new GameManager();
            gm2.loadGame(tempFile);

            // Usar getters
            assertEquals(gm1.getTamanhoTabuleiro(), gm2.getTamanhoTabuleiro());
            assertEquals(gm1.getContadorTurnos(), gm2.getContadorTurnos());
            assertEquals(gm1.getJogadores().size(), gm2.getJogadores().size());

            tempFile.delete();
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testGetProgrammersInfo() {
        GameManager gm = new GameManager();
        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));
        String info = gm.getProgrammersInfo();
        assertNotNull(info);
        assertTrue(info.contains("Alice"));
        assertTrue(info.contains("Bob"));
        assertTrue(info.contains("No tools"));
    }

    @Test
    public void testAvancarIgnoraJogadorPreso_Debug() {

        GameManager gm = new GameManager();

        String[][] jogadores = {
                {"1", "Alice", "Java", "Blue"},
                {"2", "Bob", "Python", "Green"},
                {"3", "Charlie", "C", "Red"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, null));

        assertEquals("1", gm.getJogadorAtual());

        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();

        Jogador bob = gm.getJogadorById("2");
        bob.setPreso(true);
        gm.reactToAbyssOrTool();

        assertEquals("3", gm.getJogadorAtual());

        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();

        assertEquals("1", gm.getJogadorAtual());
    }

    @Test
    public void testDebug_turnosSaltados_quando_jogador_4_nao_devia_estar_preso() {

        GameManager gm = new GameManager();

        String[][] jogadores = {
                {"1", "A", "Java", "Blue"},
                {"2", "B", "Java", "Red"},
                {"3", "C", "Java", "Green"},
                {"4", "D", "Java", "Yellow"}
        };

        String[][] objetos = {
                {"0", "8", "3"},
                {"1", "1", "5"}
        };

        assertTrue(gm.createInitialBoard(jogadores, 10, objetos));

        gm.eliminatePlayer(gm.getJogadorById("1"));
        gm.eliminatePlayer(gm.getJogadorById("2"));

        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(4);
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(1);

        gm.moveCurrentPlayer(2);
        String r = gm.reactToAbyssOrTool();

        System.out.println("DEBUG turnosSaltados = " + gm.getTurnosSaltados());
    }



}