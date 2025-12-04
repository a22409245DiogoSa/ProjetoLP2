package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    ArrayList<String>[] board;
    HashMap<String, Integer> positions = new HashMap<>();
    int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.board = new ArrayList[boardSize];
        for (int i = 0; i < boardSize; i++) {
            board[i] = new ArrayList<>();
        }
    }

    public void addPlayer(Player p) {
        board[0].add(p.getId());
        positions.put(p.getId(), 1);
    }

    public void movePlayer(Player p, int nrSpaces) {
        int posAtual = positions.get(p.getId());
        int posDestino = posAtual + nrSpaces;
        if (posDestino > boardSize) {
            int excesso = posDestino - boardSize;
            posDestino = boardSize - excesso;
        }

        board[posAtual - 1].remove(p.getId());
        board[posDestino - 1].add(p.getId());
        positions.put(p.getId(), posDestino);
        p.setPosicao(posDestino);
    }

    public boolean isAtEnd(Player p) {
        return positions.get(p.getId()) == boardSize;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return null;
        }
        List<String> ids = board[position - 1];
        if (ids.isEmpty()) {
            return new String[]{""};
        }
        return new String[]{String.join(",", ids)};
    }

    public HashMap<String, Integer> getPositions() {
        return positions;
    }

}
