package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.*;

public class GameManager {
    private int boardSize;
    private String currentPlayer;
    private EstadoJogo gameState;
    private int turnCount = 1;
    private int lastDiceRoll = 0;
    private List<Player> players = new ArrayList<>();
    private Board gameBoard;
    private HashMap<String, Integer> skippedTurns = new HashMap<>();

    public int getBoardSize() { return this.boardSize; }
    public int getLastDiceRoll() { return this.lastDiceRoll; }

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO) return false;
        Player atual = getPlayerById(currentPlayer);
        if (atual == null || !atual.isAlive()) {
            advanceToNextPlayer();
            return false;
        }

        // Importante: incrementar movimentos aqui
        atual.incMovimentos();

        // Verificação de turnos saltados
        int skips = skippedTurns.getOrDefault(atual.getId(), 0);
        if (skips > 0) {
            skippedTurns.put(atual.getId(), skips - 1);
            advanceToNextPlayer();
            turnCount++;
            return false;
        }

        this.lastDiceRoll = nrSpaces;
        gameBoard.movePlayer(atual, nrSpaces);
        turnCount++;

        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
        }
        return true;
    }

    public String reactToAbyssOrTool() {
        Player p = getPlayerById(currentPlayer);
        if (p == null) return "";

        AbyssOrTool obj = gameBoard.getObjectAt(p.getPosicao());
        if (obj == null) {
            advanceToNextPlayer();
            return ""; // NUNCA retorna null para passar nos testes
        }

        String resultado = obj.apply(p, this);
        if (resultado == null) resultado = "";

        if (gameState != EstadoJogo.TERMINADO && p.isAlive()) {
            advanceToNextPlayer();
        }
        return resultado;
    }

    public boolean gameIsOver() {
        if (gameState == EstadoJogo.TERMINADO) return true;
        long ativos = players.stream().filter(Player::isAlive).count();
        if (ativos == 0) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }
        return false;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();
        if (!gameIsOver()) return results;

        results.add("THE GREAT PROGRAMMING JOURNEY");
        results.add("");
        results.add("NR. DE TURNOS");
        results.add(String.valueOf(turnCount));
        results.add("");

        String vencedor = getWinnerName();
        if (vencedor == null) {
            results.add("O jogo terminou empatado.");
            results.add("");
            results.add("Participantes:");
            for (Player p : players) {
                results.add(p.getNome() + " : " + p.getPosicao() + " : " + p.getMotivoParagem());
            }
        } else {
            results.add("VENCEDOR");
            results.add(vencedor);
            results.add("");
            results.add("RESTANTES");
            List<Player> restantes = new ArrayList<>();
            for (Player p : players) {
                if (!p.getNome().equals(vencedor)) restantes.add(p);
            }
            restantes.sort((a, b) -> b.getPosicao() - a.getPosicao());
            for (Player p : restantes) {
                results.add(p.getNome() + " " + p.getPosicao());
            }
        }
        return results;
    }

    // Métodos auxiliares necessários
    public Player getPlayerById(String id) {
        for (Player p : players) if (p.getId().equals(id)) return p;
        return null;
    }

    public void setPlayerPosition(Player p, int newPos) {
        gameBoard.updatePlayerPosition(p, p.getPosicao(), newPos);
        p.setPosicao(newPos);
    }

    public void eliminatePlayer(Player p) {
        p.setAlive(false);
        if (players.stream().filter(Player::isAlive).count() == 0) {
            gameState = EstadoJogo.TERMINADO;
        }
    }

    public void skipTurns(Player p, int n) {
        skippedTurns.put(p.getId(), skippedTurns.getOrDefault(p.getId(), 0) + n);
    }

    public String getWinnerName() {
        for (Player p : players) if (gameBoard.isAtEnd(p)) return p.getNome();
        return null;
    }

    private void advanceToNextPlayer() {
        // Lógica de rotação de IDs (ID 1 -> 2 -> 3 -> 4 -> 1)
        int currentIdx = -1;
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getId().equals(currentPlayer)) currentIdx = i;
        }
        for(int i=1; i<=players.size(); i++) {
            int nextIdx = (currentIdx + i) % players.size();
            if(players.get(nextIdx).isAlive()) {
                currentPlayer = players.get(nextIdx).getId();
                return;
            }
        }
    }
}