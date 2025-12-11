package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class GameManager {
    String[][] playerInfo;
    int boardSize;
    String currentPlayer;
    EstadoJogo gameState;
    HashMap<String, Integer> positions;
    int turnCount = 1;

    List<Player> players = new ArrayList<>();
    Board gameBoard;

    HashMap<String, Integer> skippedTurns = new HashMap<>();

    public GameManager() {}

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {

        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4)
            return false;

        if (worldSize < playerInfo.length * 2)
            return false;

        this.playerInfo = playerInfo;
        this.boardSize = worldSize;
        this.players.clear();
        this.positions = new HashMap<>();
        this.gameBoard = new Board(worldSize);

        // Criar jogadores
        for (String[] j : playerInfo) {
            if (j == null || j.length != 4) return false;
            for (String campo : j) if (campo == null) return false;

            Player p = new Player(j[0], j[1], j[2], j[3]);
            players.add(p);
            gameBoard.addPlayer(p);
        }

        // Ordenar jogadores por ID
        players.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        currentPlayer = players.get(0).getId();
        gameState = EstadoJogo.EM_ANDAMENTO;
        turnCount = 1;

        // Ainda sem parse de Abysses/Tools (vamos fazer depois)
        return true;
    }

    public boolean createInitialBoard(String[][] p, int w) {
        return createInitialBoard(p, w, null);
    }

    public int getCurrentPlayerID() {
        return Integer.parseInt(currentPlayer);
    }

    // Para quando implementarmos efeitos
    public String reactToAbyssOrTool() {
        return null;
    }

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO || gameBoard == null) return false;

        Player atual = getPlayerById(currentPlayer);

        // skip turn
        int skips = skippedTurns.getOrDefault(atual.getId(), 0);
        if (skips > 0) {
            skippedTurns.put(atual.getId(), skips - 1);
            if (skippedTurns.get(atual.getId()) == 0) skippedTurns.remove(atual.getId());
            turnCount++;
            advanceToNextPlayer();
            return true;
        }

        if (nrSpaces < 1 || nrSpaces > 6) return false;

        gameBoard.movePlayer(atual, nrSpaces);
        turnCount++;

        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }

        advanceToNextPlayer();
        return true;
    }

    public void skipTurns(Player p, int n) {
        if (p == null || n <= 0) return;
        skippedTurns.put(p.getId(), skippedTurns.getOrDefault(p.getId(), 0) + n);
    }

    public void eliminatePlayer(Player p) {
        if (p == null) return;

        players.remove(p);

        if (players.size() <= 1) {
            gameState = EstadoJogo.TERMINADO;
            currentPlayer = players.isEmpty() ? null : players.get(0).getId();
            return;
        }

        if (currentPlayer.equals(p.getId())) {
            currentPlayer = players.get(0).getId();
        }
    }

    private void advanceToNextPlayer() {
        int idx = 0;
        for (int i = 0; i < players.size(); i++)
            if (players.get(i).getId().equals(currentPlayer)) idx = i;

        currentPlayer = players.get((idx + 1) % players.size()).getId();
    }

    public Player getPlayerById(String id) {
        for (Player p : players)
            if (p.getId().equals(id)) return p;
        return null;
    }

    public String getProgrammersInfo() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            if (i > 0) sb.append(" | ");

            if (p.getFerramentas().isEmpty()) {
                sb.append(p.getNome()).append(" : No tools");
            } else {
                sb.append(p.getNome())
                        .append(" : ")
                        .append(String.join("; ", p.getFerramentas()));
            }
        }
        return sb.toString();
    }

    public void loadGame(File file)
            throws InvalidFileException, FileNotFoundException {
        // para implementar depois
    }

    public boolean saveGame(File file) {
        // para implementar depois
        return false;
    }
}
