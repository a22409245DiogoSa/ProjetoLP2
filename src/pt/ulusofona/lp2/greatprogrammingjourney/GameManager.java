package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
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

    public GameManager() {}

public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
    if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
        return false;
    }

    if (worldSize < (2 * playerInfo.length)) {
        return false;
    }

    // Guardar as informações no objeto
    this.playerInfo = playerInfo;
    this.boardSize = worldSize;
    this.positions = new HashMap<>();
    this.board = new ArrayList[worldSize];

    for (int i = 0; i < worldSize; i++) {
        board[i] = new ArrayList<>();
    }

    // Colocar todos os jogadores na posição inicial (1)
    for (String[] jogador : playerInfo) {
        if (jogador == null || jogador.length != 4) {
            return false;
        }

        String id = jogador[0];
        String nome = jogador[1];
        String linguagens = jogador[2];
        String cor = jogador[3];

        if (id == null || nome == null || linguagens == null || cor == null) {
            return false;
        }

        board[0].add(id);
        positions.put(id, 1);
    }

    String menorID = playerInfo[0][0];
    for (String[] jogador : playerInfo) {
        if (Integer.parseInt(jogador[0]) < Integer.parseInt(menorID)) {
            menorID = jogador[0];
        }
    }
    currentPlayer = menorID;
    gameState = EstadoJogo.EM_ANDAMENTO;

    return true;
}

    public int getCurrentPlayerID() {
        return Integer.parseInt(currentPlayer);
    }

    public boolean moveCurrentPlayer(int nrSpaces) {

        if (positions == null) {
            return false;
        }

        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        int posAtual = positions.get(currentPlayer);
        int posDestino = posAtual + nrSpaces;
        if (posDestino > boardSize) {
            int excesso = posDestino - boardSize;
            posDestino = boardSize - excesso;
        }

        board[posAtual - 1].remove(currentPlayer);
        board[posDestino - 1].add(currentPlayer);
        positions.put(currentPlayer, posDestino);

        int idxAtual = -1;
        for (int i = 0; i < playerInfo.length; i++) {
            if (playerInfo[i][0].equals(currentPlayer)) {
                idxAtual = i;
                break;
            }
        }

        int proximoIdx = (idxAtual + 1) % playerInfo.length;
        currentPlayer = playerInfo[proximoIdx][0];

        turnCount++;
        return true;
    }

    public boolean gameIsOver() {
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            if (entry.getValue() == boardSize) {
                return true;
            }
        }
        return false;
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
        results.add(vencedor != null ? vencedor : "Desconhecido");
        results.add("");

        results.add("RESTANTES");

        List<String[]> restantes = new ArrayList<>();
        for (String[] jogador : playerInfo) {
            if (!jogador[1].equals(vencedor)) {
                int pos = positions.get(jogador[0]);
                restantes.add(new String[]{jogador[1], String.valueOf(pos)});
            }
        }

        restantes.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
        for (String[] r : restantes) {
            results.add(r[0] + " " + r[1]);
        }

        return results;
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }
        if (nrSquare == boardSize) {
            return "final.png";
        }
        return "imagem" + nrSquare + ".png";
    }

    public String[] getProgrammerInfo(int id) {
        String idStr = String.valueOf(id);

        if (positions == null) {
            return null;
        }

        for (String[] jogador : playerInfo) {
            if (jogador[0].equals(idStr)) {
                Integer pos = positions.get(idStr);
                if (pos == null) {
                    return null;
                }

                String[] linguagens = jogador[2].split(";");
                for (int i = 0; i < linguagens.length; i++) {
                    linguagens[i] = linguagens[i].trim();
                }
                Arrays.sort(linguagens);
                String linguagensOrdenadas = String.join("; ", linguagens);



                return new String[]{
                        jogador[0],
                        jogador[1],
                        linguagensOrdenadas,
                        jogador[3],
                        String.valueOf(pos)
                };
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {

        String idStr = String.valueOf(id);

        if (positions == null) {
            return null;
        }

        for (String[] jogador : playerInfo) {
            if (jogador[0].equals(idStr)) {
                Integer pos = positions.get(idStr);
                if (pos == null) {
                    return null;
                }

                String[] linguagens = jogador[2].split(";");
                for (int i = 0; i < linguagens.length; i++) {
                    linguagens[i] = linguagens[i].trim();
                }
                Arrays.sort(linguagens);
                String linguagensOrdenadas = String.join("; ", linguagens);

                String estado = (pos >= boardSize) ? "Em Jogo" : "Em Jogo";

                String[] info = new String[]{
                        jogador[0],
                        jogador[1],
                        String.valueOf(pos),
                        linguagensOrdenadas,
                        estado
                };
                if (info == null) {
                    return null;
                }
                return String.join(" | ", info);
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return null;
        }

        List<String> ids = board[position - 1];
        if (ids.isEmpty()) {
            return new String[]{""};
        }

        String idsStr = String.join(",", ids);
        return new String[]{idsStr};
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }

    public String getWinnerName() {
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            if (entry.getValue() == boardSize) {
                String idVencedor = entry.getKey();
                for (String[] jogador : playerInfo) {
                    if (jogador[0].equals(idVencedor)) {
                        return jogador[1];
                    }
                }
            }
        }
        return null;
    }

}
