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

        // 2. Inicializar variáveis de estado
        this.playerInfo = playerInfo;
        this.boardSize = worldSize;
        this.positions = new HashMap<>(); // Nota: A classe Board já gere posições, mas se o GM precisar, mantém.
        this.gameBoard = new Board(worldSize);
        this.players.clear();
        this.skippedTurns.clear(); // Limpar turnos saltados de jogos anteriores

        // 3. Criar Jogadores
        for (String[] jogador : playerInfo) {
            if (jogador == null || jogador.length != 4) return false;
            for (String campo : jogador) {
                if (campo == null) return false;
            }

            Player p = new Player(jogador[0], jogador[1], jogador[2], jogador[3]);
            // IMPORTANTE: Garantir que o jogador começa "vivo"
            p.setAlive(true);
            players.add(p);
            gameBoard.addPlayer(p);
        }

        // Ordenar jogadores por ID para garantir a ordem dos turnos
        players.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        currentPlayer = players.get(0).getId();
        gameState = EstadoJogo.EM_ANDAMENTO;
        turnCount = 1;

        // 4. Criar Abismos e Ferramentas (NOVO)
        if (abyssesAndTools != null) {
            for (String[] item : abyssesAndTools) {
                if (item == null || item.length != 3) return false;

                try {
                    int type = Integer.parseInt(item[0]); // 0 = Abyss, 1 = Tool
                    int idSubtype = Integer.parseInt(item[1]);
                    int position = Integer.parseInt(item[2]);

                    // Validar posição
                    if (position < 1 || position > worldSize) return false;

                    if (type == 0) {
                        // É um Abismo
                        String name = getAbyssName(idSubtype);
                        Abyss abyss = new Abyss(idSubtype, name, position);
                        gameBoard.placeObject(abyss);
                    } else if (type == 1) {
                        // É uma Ferramenta
                        String name = getToolName(idSubtype);
                        Tool tool = new Tool(idSubtype, name, position);
                        gameBoard.placeObject(tool);
                    }
                } catch (NumberFormatException e) {
                    return false; // Se os dados não forem números válidos
                }
            }
        }

        return true;
    }

    private String getAbyssName(int id) {
        switch (id) {
            case 0: return "Erro de Sintaxe";
            case 1: return "Erro de Lógica";
            case 2: return "Exception";
            case 3: return "FileNotFoundException";
            case 4: return "Crash";
            case 5: return "Código Duplicado";
            case 6: return "Efeitos Secundários";
            case 7: return "Blue Screen of Death";
            case 8: return "Ciclo Infinito";
            case 9: return "Segmentation Fault";
            default: return "Unknown Abyss";
        }
    }

    private String getToolName(int id) {
        switch (id) {
            case 0: return "Herança";
            case 1: return "Programação Funcional";
            case 2: return "Testes Unitários";
            case 3: return "Tratamento de Excepções";
            case 4: return "IDE";
            case 5: return "Ajuda Do Professor";
            default: return "Unknown Tool";
        }
    }

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        return createInitialBoard(playerInfo, worldSize, null);
    }

    public int getCurrentPlayerID() {
        return Integer.parseInt(currentPlayer);
    }

    public String reactToAbyssOrTool() {
        Player p = getPlayerById(currentPlayer);
        if (p == null) return null;

        // 2. Verificar o que está na casa
        int pos = p.getPosicao();
        AbyssOrTool obj = gameBoard.getObjectAt(pos);

        String resultado = null;

        // 3. Lógica de Interação
        if (obj == null) {
            // Casa vazia -> Não faz nada, apenas retorna null e avança turno
            advanceToNextPlayer();
            return null;
        }

        // Aplica o efeito (polimorfismo: o método apply sabe se é Abyss ou Tool)
        resultado = obj.apply(p, this);

        // Se for Ferramenta, removemos do tabuleiro após apanhar
        if ("Tool".equals(obj.getType())) {
            gameBoard.removeObjectAt(pos);
        }

        // NOTA: Se o efeito do abismo for eliminar o jogador, o método 'eliminatePlayer'
        // pode alterar o currentPlayer. Devemos ter cuidado para não avançar duas vezes.
        // Mas, assumindo fluxo normal:

        if (gameState != EstadoJogo.TERMINADO) {
            advanceToNextPlayer();
        }

        return resultado;
    };

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO || gameBoard == null) {
            return false;
        }

        // Se currentPlayer é nulo (todos eliminados?) - falha
        if (currentPlayer == null) return false;

        // 1. Obter jogador atual e verificar se está vivo (isAlive deve ser usado, mas vamos manter
        // a verificação por ID para consistência com o que já tens)
        Player atual = getPlayerById(currentPlayer);
        if (atual == null) {
            // Se o jogador atual não existe (foi eliminado noutra fase do turno, por exemplo)
            // Tentamos avançar o turno e falhar o movimento.
            advanceToNextPlayer();
            return false;
        }

        // 2. Lógica de Turnos Saltados (Skip Turns)
        Integer skips = skippedTurns.getOrDefault(atual.getId(), 0);
        if (skips > 0) {
            // Decrementa e passa a vez sem mover
            skippedTurns.put(atual.getId(), skips - 1);

            // Remove do mapa se chegou a zero
            if (skippedTurns.get(atual.getId()) == 0) {
                skippedTurns.remove(atual.getId());
            }

            // Avança o turno porque não houve movimento a ser processado
            turnCount++;
            advanceToNextPlayer();
            return true;
        }

        // 3. Validação do número de casas a mover
        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        // 4. Movimento normal
        gameBoard.movePlayer(atual, nrSpaces);
        turnCount++;

        // 5. Verifica se chegou ao fim (Fim do Tabuleiro)
        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }

        // 6. Não avançar o turno!
        // O próximo passo esperado da GUI é chamar reactToAbyssOrTool()
        // que é onde o turno será avançado, garantindo que o Abismo/Ferramenta
        // na casa destino é processado pelo jogador atual.

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
        List<String> infoParts = new ArrayList<>();

        for (Player p : players) {
            // Apenas jogadores vivos
            if (p.isAlive()) {
                String nome = p.getNome();
                List<String> tools = p.getFerramentas();

                String toolsStr;
                if (tools.isEmpty()) {
                    toolsStr = "No tools";
                } else {
                    // Junta as ferramentas com ";"
                    toolsStr = String.join(";", tools);
                }

                infoParts.add(nome + " : " + toolsStr);
            }
        }

        // Junta os jogadores com " | "
        return String.join(" | ", infoParts);
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


