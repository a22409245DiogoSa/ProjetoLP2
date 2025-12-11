package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    ArrayList<String>[] board;
    HashMap<String, Integer> positions = new HashMap<>();
    AbyssOrTool[] objetos;
    int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;

        board = new ArrayList[boardSize];
        for (int i = 0; i < boardSize; i++) board[i] = new ArrayList<>();

        objetos = new AbyssOrTool[boardSize];
    }

    public void addPlayer(Player p) {
        board[0].add(p.getId());
        positions.put(p.getId(), 1);
    }

    public void movePlayer(Player p, int nrSpaces) {
        int atual = positions.get(p.getId());
        int destino = atual + nrSpaces;

        if (destino > boardSize) {
            int excesso = destino - boardSize;
            destino = boardSize - excesso;
        }

        board[atual - 1].remove(p.getId());
        board[destino - 1].add(p.getId());
        positions.put(p.getId(), destino);
        p.setPosicao(destino);
    }

    public boolean isAtEnd(Player p) {
        return positions.get(p.getId()) == boardSize;
    }

    public AbyssOrTool getObjectAt(int pos) {
        if (pos < 1 || pos > boardSize) return null;
        return objetos[pos - 1];
    }

    public void placeObject(AbyssOrTool obj) {
        objetos[obj.getPosition() - 1] = obj;
    }

    public void removeObjectAt(int pos) {
        objetos[pos - 1] = null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return null;
        }

        // jogadores na casa
        List<String> ids = board[position - 1];
        String playersCSV = "";
        if (!ids.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                sb.append(ids.get(i));
                if (i < ids.size() - 1) sb.append(",");
            }
            playersCSV = sb.toString();
        }

        // abismo ou ferramenta
        AbyssOrTool obj = objetos[position - 1];
        String tipoStr = "";
        String objIdStr = "";

        if (obj != null) {
            objIdStr = String.valueOf(obj.getId());
            if ("Abyss".equals(obj.getType())) {
                tipoStr = "A:" + objIdStr;
            } else if ("Tool".equals(obj.getType())) {
                tipoStr = "T:" + objIdStr;
            }
        }

        return new String[]{playersCSV, tipoStr, objIdStr};
    }
}
