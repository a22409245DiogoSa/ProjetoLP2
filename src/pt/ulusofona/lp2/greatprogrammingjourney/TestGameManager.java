package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class TestGameManager {

    @Test
    public void testCreateInitialBoard() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Alice", "Java;C++", "Azul"},
                {"2", "Bob", "Python;Go", "Verde"}
        };

        boolean result = gm.createInitialBoard(players, 10);
        assertTrue(result, "O tabuleiro inicial deve ser criado com sucesso.");

        // Confirma que todos começam na posição 1
        String[] slotInfo = gm.getSlotInfo(1);
        assertTrue(slotInfo[0].contains("1") && slotInfo[0].contains("2"),
                "Ambos os jogadores devem começar na posição 1.");
    }

    @Test
    public void testMoveCurrentPlayerAndTurnRotation() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Alice", "Java;C++", "Azul"},
                {"2", "Bob", "Python;Go", "Verde"}
        };
        gm.createInitialBoard(players, 10);

        int current = gm.getCurrentPlayerID();
        assertEquals(1, current);

        boolean moved = gm.moveCurrentPlayer(3);
        assertTrue(moved);

        // Após mover, o jogador atual deve mudar
        int nextPlayer = gm.getCurrentPlayerID();
        assertEquals(2, nextPlayer, "Após Alice jogar, deve ser a vez do Bob.");
    }

    @Test
    public void testGameIsOverWhenPlayerReachesEnd() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Alice", "Java;C++", "Azul"}
        };
        gm.createInitialBoard(players, 5);

        // Movimentos até a posição final
        gm.moveCurrentPlayer(4);

        assertTrue(gm.gameIsOver(), "O jogo deve terminar quando o jogador chega ao fim.");
        ArrayList<String> results = gm.getGameResults();
        assertFalse(results.isEmpty(), "Os resultados devem estar disponíveis após o fim do jogo.");
    }

    @Test
    public void testAPIMethodsReturnValidValues() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Alice", "Java;C++", "Azul"}
        };
        gm.createInitialBoard(players, 6);

        // Testa funções obrigatórias da API
        assertNotNull(gm.getImagePng(1));
        assertNotNull(gm.getProgrammerInfo(1));
        assertNotNull(gm.getProgrammerInfoAsStr(1));
        assertNotNull(gm.getSlotInfo(1));
        assertNotNull(gm.getAuthorsPanel());
        assertNotNull(gm.customizeBoard());
    }
}
