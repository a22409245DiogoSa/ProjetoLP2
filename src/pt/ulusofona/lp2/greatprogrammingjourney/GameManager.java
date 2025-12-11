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
    private HashMap<String, Integer> skippedTurns = new HashMap<>();

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

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        return createInitialBoard(playerInfo, worldSize, null);
    }

    public int getCurrentPlayerID() {
        return Integer.parseInt(currentPlayer);
    }

    public String reactToAbyssOrTool() {
        Player p = getPlayerById(currentPlayer);
        if (p == null) return "";

        int pos = p.getPosicao();
        AbyssOrTool obj = gameBoard.getObjectAt(pos);

        if (obj == null) {
            return ""; // não há abismo ou ferramenta na posição
        }

        String resultado = obj.apply(p, this);

        // Se for uma Tool, removemos do tabuleiro após pegar
        if ("Tool".equals(obj.getType())) {
            gameBoard.removeObjectAt(pos);
        }

        return resultado;
    };

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO || gameBoard == null) {
            return false;
        }

        // Se currentPlayer é nulo (todos eliminados?) - falha
        if (currentPlayer == null) return false;

        // Verifica se o jogador actual está eliminado (não deveria acontecer, mas por segurança)
        Player atual = getPlayerById(currentPlayer);
        if (atual == null) {
            // avança para o próximo
            advanceToNextPlayer();
            return false;
        }

        // Se o jogador tem turnos para saltar
        Integer skips = skippedTurns.getOrDefault(atual.getId(), 0);
        if (skips != null && skips > 0) {
            // decrementa e passa a vez sem mover
            skippedTurns.put(atual.getId(), skips - 1);
            // se chegou a zero, remove a entrada
            if (skippedTurns.get(atual.getId()) == 0) skippedTurns.remove(atual.getId());
            turnCount++;
            advanceToNextPlayer();
            return true;
        }

        // valida nrSpaces
        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        // movimento normal
        gameBoard.movePlayer(atual, nrSpaces);
        turnCount++;

        // verifica se chegou ao fim
        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }

        // depois do movimento, se o novo slot tem um Abyss/Tool,
        // o gameBoard.movePlayer idealmente já actualiza a posição do jogador
        // e depois a UI/tests chamam reactToAbyssOrTool()

        // passa a vez ao próximo jogador vivo
        advanceToNextPlayer();
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

    public void skipTurns(Player p, int n) {
        if (p == null || n <= 0) return;
        skippedTurns.put(p.getId(), skippedTurns.getOrDefault(p.getId(), 0) + n);
    }

    public void eliminatePlayer(Player p) {
        if (p == null) return;

        // remover posições do mapa interno
        if (positions != null) {
            positions.remove(p.getId());
        }

        // remover jogador da lista
        int idx = players.indexOf(p);
        if (idx == -1) return;

        boolean eraCurrent = currentPlayer.equals(p.getId());

        players.remove(idx);

        // se apenas 1 jogador resta → termina o jogo
        if (players.size() <= 1) {
            gameState = EstadoJogo.TERMINADO;
            if (players.size() == 1) {
                currentPlayer = players.get(0).getId();
            } else {
                currentPlayer = null;
            }
            return;
        }

        // avançar o turno se o jogador eliminado era o atual
        if (eraCurrent) {
            int next = idx % players.size();
            currentPlayer = players.get(next).getId();
        }
    }

    private void advanceToNextPlayer() {
        if (players.isEmpty()) {
            currentPlayer = null;
            return;
        }
        // encontra índice do currentPlayer (protege contra jogadores eliminados)
        int idx = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(currentPlayer)) {
                idx = i;
                break;
            }
        }
        currentPlayer = players.get((idx + 1) % players.size()).getId();
    }

    public void loadGame(File file) throws
            InvalidFileException, FileNotFoundException {
    }

    public boolean saveGame(File file) {
        return true;
    }
}
