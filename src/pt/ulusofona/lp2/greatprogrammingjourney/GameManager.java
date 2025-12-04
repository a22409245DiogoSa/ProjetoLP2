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
    ArrayList<String>[] board;
    HashMap<String, Integer> positions;
    int turnCount = 1;

    List<Player> players = new ArrayList<>();
    Board gameBoard;

    public GameManager() {}

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        if (worldSize < (2 * playerInfo.length)) {
            return false;
        }

        this.playerInfo = playerInfo;
        this.boardSize = worldSize;
        this.positions = new HashMap<>();
        this.gameBoard = new Board(worldSize);
        this.players.clear();

        for (String[] jogador : playerInfo) {
            if (jogador == null || jogador.length != 4){
                return false;
            }
            for (String campo : jogador) {
                if (campo == null){
                    return false;
                }
            }

            Player p = new Player(jogador[0], jogador[1], jogador[2], jogador[3]);
            players.add(p);
            gameBoard.addPlayer(p);
        }

        players.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        currentPlayer = players.get(0).getId();
        gameState = EstadoJogo.EM_ANDAMENTO;
        turnCount = 1;
        return true;
    }

    public int getCurrentPlayerID() {
        return Integer.parseInt(currentPlayer);
    }

    public String reactToAbyssOrTool() {
        return "";
    };

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO || gameBoard == null) {
            return false;
        }
        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        Player atual = getPlayerById(currentPlayer);
        gameBoard.movePlayer(atual, nrSpaces);
        turnCount++;

        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }

        int idxAtual = players.indexOf(atual);
        currentPlayer = players.get((idxAtual + 1) % players.size()).getId();
        return true;
    }

    public boolean gameIsOver() {
        return gameState == EstadoJogo.TERMINADO;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();

        if (!gameIsOver()) {
            return results;
        }

        results.add("THE GREAT PROGRAMMING JOURNEY");
        results.add("");
        results.add("NR. DE TURNOS");
        results.add(String.valueOf(turnCount));
        results.add("");
        results.add("VENCEDOR");

        String vencedor = getWinnerName();
        if (vencedor != null) {
            results.add(vencedor);
        } else {
            results.add("Desconhecido");
        }

        results.add("");
        results.add("RESTANTES");

        List<Player> restantes = new ArrayList<>();

        for (Player p : players) {
            if (!p.getNome().equals(vencedor)) {
                restantes.add(p);
            }
        }

        restantes.sort((a, b) -> b.getPosicao() - a.getPosicao());

        for (Player p : restantes) {
            results.add(p.getNome() + " " + p.getPosicao());
        }

        return results;
    }

    public String getWinnerName() {
        for (Player p : players) {
            if (gameBoard.isAtEnd(p)) {
                return p.getNome();
            }
        }
        return null;
    }

    public String getProgrammersInfo() {
        return "";
    };

    public String[] getProgrammerInfo(int id) {
        for (Player p : players) {
            if (Integer.parseInt(p.getId()) == id) {
                return p.toArray();
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {
        for (Player p : players) {
            if (Integer.parseInt(p.getId()) == id) {
                return p.getInfoAsStr(boardSize);
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        return gameBoard.getSlotInfo(position);
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }
        return nrSquare == boardSize ? "final.png" : "imagem" + nrSquare + ".png";
    }

    public JPanel getAuthorsPanel() { return null; }
    public HashMap<String, String> customizeBoard() { return new HashMap<>(); }

    public Player getPlayerById(String id) {
        for (Player p : players) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void loadGame(File file) throws
            InvalidFileException, FileNotFoundException {
    }

    public boolean saveGame(File file) {
        return true;
    }
}
