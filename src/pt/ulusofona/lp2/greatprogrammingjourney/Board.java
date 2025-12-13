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

        if (board == null || position < 1 || position > boardSize) {
            return new String[]{"", "", ""}; // Retorna array vazio em vez de null
        }

        // 1. Jogadores na Casa (playersCSV está correto)
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

        // 2. Abismo ou Ferramenta
        AbyssOrTool obj = objetos[position - 1];
        String objNameStr = ""; // O nome do objeto (Ex: "Erro de Sintaxe")
        String tipoIdStr = "";  // O Tipo:ID (Ex: "A:0")

        if (obj != null) {
            objNameStr = obj.getName();
            String objIdStr = String.valueOf(obj.getId());

            if ("Abyss".equals(obj.getType())) {
                tipoIdStr = "A:" + objIdStr;
            } else if ("Tool".equals(obj.getType())) {
                tipoIdStr = "T:" + objIdStr;
            }
        }

        // Retorna no formato CORRETO e esperado pelo teste:
        // [Jogadores na Casa, Nome do Objeto, Tipo:ID]
        return new String[]{playersCSV, objNameStr, tipoIdStr};
    }

    public void updatePlayerPosition(Player p, int oldPos, int newPos) {
        // As posições são 1-based, o array é 0-based.
        int oldIndex = oldPos - 1;
        int newIndex = newPos - 1;

        // 1. Remove da posição antiga
        if (oldIndex >= 0 && oldIndex < boardSize) {
            board[oldIndex].remove(p.getId());
        }

        // 2. Adiciona na nova posição
        if (newIndex >= 0 && newIndex < boardSize) {
            board[newIndex].add(p.getId());
        }

        // 3. Atualiza o mapa de posições
        positions.put(p.getId(), newPos);
    }

    public List<String> getPlayersInSlot(int pos) {
        if (pos < 1 || pos > boardSize) return new ArrayList<>();
        return board[pos - 1];
    }

    public void addPlayerForLoad(Player p, int pos) {
        if (pos < 1 || pos > boardSize) pos = 1; // Sanidade
        int index = pos - 1;

        if (board[index] == null) board[index] = new ArrayList<>();

        board[index].add(p.getId());
        positions.put(p.getId(), pos);
    }
}
