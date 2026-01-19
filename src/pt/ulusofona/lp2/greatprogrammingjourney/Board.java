package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe que gere o estado do tabuleiro, incluindo a localização dos jogadores
 * e a presença de objetos (Abismos ou Ferramentas) em cada casa.
 */
public class Board {
    // Array de listas para saber quais IDs de jogadores estão em cada casa (índice 0 é a casa 1)
    private ArrayList<String>[] board;
    // Mapa para consulta rápida da posição atual de cada jogador pelo seu ID
    private HashMap<String, Integer> positions = new HashMap<>();
    // Array que armazena objetos (AbyssOrTool) indexados pela posição no tabuleiro
    private AbyssOrTool[] objetos;
    private int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;

        // Inicializa o array de casas do tabuleiro
        board = new ArrayList[boardSize];
        for (int i = 0; i < boardSize; i++) {
            board[i] = new ArrayList<>();
        }
        // Inicializa o contentor de objetos com o tamanho do tabuleiro
        objetos = new AbyssOrTool[boardSize];
    }

    /**
     * Coloca um novo jogador na casa inicial (posição 1).
     */
    public void addPlayer(Player p) {
        board[0].add(p.getId());
        positions.put(p.getId(), 1);
    }

    /**
     * Move o jogador e gere a lógica de "ressalto" se ultrapassar o fim do tabuleiro.
     */
    public void movePlayer(Player p, int nrSpaces) {
        int atual = positions.get(p.getId());
        int destino = atual + nrSpaces;

        // Lógica de ressalto: se passar o limite, volta para trás o excesso
        if (destino > boardSize) {
            int excesso = destino - boardSize;
            destino = boardSize - excesso;
        }

        // Atualiza as listas do tabuleiro (remove da antiga, adiciona na nova)
        board[atual - 1].remove(p.getId());
        board[destino - 1].add(p.getId());

        // Sincroniza a posição no mapa e no objeto Player
        positions.put(p.getId(), destino);
        p.setPosicao(destino);
    }

    /**
     * Verifica se o jogador chegou exatamente à última casa.
     */
    public boolean isAtEnd(Player p) {
        return positions.get(p.getId()) == boardSize;
    }

    /**
     * Retorna o objeto (Abismo/Ferramenta) presente numa posição específica.
     */
    public AbyssOrTool getObjectAt(int pos) {
        if (pos < 1 || pos > boardSize) {
            return null;
        }
        return objetos[pos - 1];
    }

    /**
     * Coloca um objeto no tabuleiro usando a posição definida no próprio objeto.
     */
    public void placeObject(AbyssOrTool obj) {
        objetos[obj.getPosition() - 1] = obj;
    }

    /**
     * Remove um objeto de uma casa (ex: quando uma ferramenta é apanhada).
     */
    public void removeObjectAt(int pos) {
        objetos[pos - 1] = null;
    }

    /**
     * Devolve informação detalhada sobre uma casa para a interface gráfica.
     * Retorna um array com: [IDs dos jogadores, Nome do objeto, Tipo:ID do objeto]
     */
    public String[] getSlotInfo(int position) {
        if (board == null || position < 1 || position > boardSize) {
            return new String[]{"", "", ""};
        }

        // 1. Processa os IDs dos jogadores na casa em formato CSV
        List<String> ids = board[position - 1];
        String playersCSV = "";
        if (!ids.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                sb.append(ids.get(i));
                if (i < ids.size() - 1) {
                    sb.append(",");
                }
            }
            playersCSV = sb.toString();
        }

        // 2. Processa a informação do objeto na casa
        AbyssOrTool obj = objetos[position - 1];
        String objNameStr = "";
        String tipoIdStr = "";

        if (obj != null) {
            objNameStr = obj.getName();
            String objIdStr = String.valueOf(obj.getId());

            // Formata o prefixo de acordo com o tipo
            if ("Abyss".equals(obj.getType())) {
                tipoIdStr = "A:" + objIdStr;
            } else if ("Tool".equals(obj.getType())) {
                tipoIdStr = "T:" + objIdStr;
            }
        }

        return new String[]{playersCSV, objNameStr, tipoIdStr};
    }

    /**vfldnvfldkm
     * Move manualmente um jogador de uma posição para outra (útil para efeitos de abismos).
     */
    public void updatePlayerPosition(Player p, int oldPos, int newPos) {
        int oldIndex = oldPos - 1;
        int newIndex = newPos - 1;

        if (oldIndex >= 0 && oldIndex < boardSize) {
            board[oldIndex].remove(p.getId());
        }

        if (newIndex >= 0 && newIndex < boardSize) {
            board[newIndex].add(p.getId());
        }

        positions.put(p.getId(), newPos);
    }

    /**
     * Retorna a lista de IDs de jogadores numa casa específica.
     */
    public List<String> getPlayersInSlot(int pos) {
        if (pos < 1 || pos > boardSize) {
            return new ArrayList<>();
        }
        return board[pos - 1];
    }

    /**
     * Configura a posição de um jogador durante o carregamento de um jogo gravado.
     */
    public void addPlayerForLoad(Player p, int pos) {
        if (pos < 1 || pos > boardSize) {
            pos = 1;
        }
        int index = pos - 1;

        if (board[index] == null) {
            board[index] = new ArrayList<>();
        }

        board[index].add(p.getId());
        positions.put(p.getId(), pos);
    }


}
