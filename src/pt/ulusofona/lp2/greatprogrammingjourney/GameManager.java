package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

public class GameManager {
    String[][] playerInfo;
    int boardSize;
    public String currentPlayer;
    EstadoJogo gameState;
    ArrayList<String>[] board;
    HashMap<String, Integer> positions;
    int turnCount = 1;
    private HashMap<String, Integer> skippedTurns = new HashMap<>();
    private int lastDiceRoll = 0;

    List<Player> players = new ArrayList<>();
    Board gameBoard;

    public GameManager() {
    }

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
        this.skippedTurns.clear();


        for (String[] jogador : playerInfo) {
            if (jogador == null || jogador.length != 4) {
                return false;
            }
            for (String campo : jogador) {
                if (campo == null) {
                    return false;
                }
            }

            Player p = new Player(jogador[0], jogador[1], jogador[2], jogador[3]);
            p.setAlive(true);
            players.add(p);
            gameBoard.addPlayer(p);
        }


        players.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        currentPlayer = players.get(0).getId();
        gameState = EstadoJogo.EM_ANDAMENTO;
        turnCount = 1;


        if (abyssesAndTools != null) {
            for (String[] item : abyssesAndTools) {
                if (item == null || item.length != 3) {
                    return false;
                }

                try {
                    int type = Integer.parseInt(item[0]);
                    int idSubtype = Integer.parseInt(item[1]);
                    int position = Integer.parseInt(item[2]);


                    if (position < 1 || position > worldSize) {
                        return false;
                    }
                    if (gameBoard.getObjectAt(position) != null) {
                        return false; // Apenas 1 objeto por casa
                    }


                    if (type == 0) {

                        String name = getAbyssName(idSubtype);
                        if (name == null) {
                            return false;
                        }
                        Abyss abyss = new Abyss(idSubtype, name, position);
                        gameBoard.placeObject(abyss);
                    } else if (type == 1) {

                        String name = getToolName(idSubtype);
                        if (name == null) {
                            return false;
                        }
                        Tool tool = new Tool(idSubtype, name, position);
                        gameBoard.placeObject(tool);
                    } else {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canMove(Player p) {
        if (!p.isAlive()) return false;
        if (skippedTurns.containsKey(p.getId()) && skippedTurns.get(p.getId()) > 0) return false;

        String firstLang = p.getLinguagens().contains(";") ? p.getLinguagens().split(";")[0].trim() : p.getLinguagens().trim();
        for (int dado = 1; dado <= 6; dado++) {
            if (firstLang.equalsIgnoreCase("C") && dado >= 4) continue;
            if (firstLang.equalsIgnoreCase("Assembly") && dado >= 3) continue;
            return true;
        }
        return false;
    }

    private void checkBlockedGame() {
        boolean algumPodeMover = false;
        for (Player p : players) {
            if (canMove(p)) {
                algumPodeMover = true;
                break;
            }
        }
        if (!algumPodeMover) gameState = EstadoJogo.TERMINADO;
    }

    private String getAbyssName(int id) {
        switch (id) {
            case 0:
                return "Erro de sintaxe";
            case 1:
                return "Erro de lógica";
            case 2:
                return "Exception";
            case 3:
                return "FileNotFoundException";
            case 4:
                return "Crash";
            case 5:
                return "Código duplicado";
            case 6:
                return "Efeitos secundários";
            case 7:
                return "Blue Screen of Death";
            case 8:
                return "Ciclo infinito";
            case 9:
                return "Segmentation fault";
            case 20:
                return "LLM";
            default:
                return null;
        }
    }

    private String getToolName(int id) {
        switch (id) {
            case 0:
                return "Herança";
            case 1:
                return "Programação Funcional";
            case 2:
                return "Testes Unitários";
            case 3:
                return "Tratamento de Excepções";
            case 4:
                return "IDE";
            case 5:
                return "Ajuda Do Professor";
            default:
                return null;
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
        if (p == null) {
            return null;
        }

        int pos = p.getPosicao();
        AbyssOrTool obj = gameBoard.getObjectAt(pos);

        String resultado = null;

        if (obj == null) {
            advanceToNextPlayer();
            return null;
        }

        resultado = obj.apply(p, this);

        if (gameState != EstadoJogo.TERMINADO && p.isAlive()) {
            advanceToNextPlayer();
        }

        checkBlockedGame();
        return resultado;
    }

    ;

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (gameState == EstadoJogo.TERMINADO || gameBoard == null) {
            return false;
        }

        if (currentPlayer == null) {
            return false;
        }

        Player atual = getPlayerById(currentPlayer);


        if (atual == null || !atual.isAlive()) {
            advanceToNextPlayer();
            return false;
        }


        Integer skips = skippedTurns.getOrDefault(atual.getId(), 0);
        if (skips > 0) {

            skippedTurns.put(atual.getId(), skips - 1);

            if (skippedTurns.get(atual.getId()) == 0) {
                skippedTurns.remove(atual.getId());
            }

            turnCount++;
            advanceToNextPlayer();
            return false;
        }


        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }


        String linguagem = atual.getLinguagens(); // Ex: "C;Java" ou "Assembly;C#"

        if (linguagem != null && !linguagem.isEmpty()) {
            String firstLang = linguagem;
            if (linguagem.contains(";")) {
                firstLang = linguagem.split(";")[0];
            }
            String trimmedFirstLang = firstLang.trim();


            if (trimmedFirstLang.equalsIgnoreCase("C") && nrSpaces >= 4) {
                return false;
            }


            if (trimmedFirstLang.equalsIgnoreCase("Assembly") && nrSpaces >= 3) {
                return false;
            }
        }



        gameBoard.movePlayer(atual, nrSpaces);
        this.lastDiceRoll = nrSpaces;
        turnCount++;


        if (gameBoard.isAtEnd(atual)) {
            gameState = EstadoJogo.TERMINADO;
            return true;
        }

        checkBlockedGame();
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
            if (p.isAlive()) {
                String nome = p.getNome();
                List<String> tools = p.getFerramentas();

                String toolsStr;
                if (tools.isEmpty()) {
                    toolsStr = "No tools";
                } else {
                    toolsStr = String.join(";", tools);
                }

                infoParts.add(nome + " : " + toolsStr);
            }
        }

        return String.join(" | ", infoParts);
    }


    public String[] getProgrammerInfo(int id) {
        for (Player p : players) {
            if (Integer.parseInt(p.getId()) == id) {
                return p.toArray();
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {
        Player p = getPlayerById(String.valueOf(id));
        if (p == null) {
            return null;
        }

        String tools = p.getFerramentas().isEmpty() ? "No tools"
                : String.join("; ", p.getFerramentas());

        String estado;
        if (!p.isAlive()) {
            estado = "Derrotado";
        } else if (skippedTurns.containsKey(p.getId()) && skippedTurns.get(p.getId()) > 0) {
            estado = "Preso";
        } else {
            estado = "Em Jogo";
        }


        return p.getId() + " | " + p.getNome() + " | " + p.getPosicao() + " | " + tools +
                " | " + p.getLinguagensOrdenadas() + " | " + estado;
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

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }

    public Player getPlayerById(String id) {
        for (Player p : players) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void skipTurns(Player p, int n) {
        if (p == null || n <= 0) {
            return;
        }
        skippedTurns.put(p.getId(), skippedTurns.getOrDefault(p.getId(), 0) + n);
    }

    public void eliminatePlayer(Player p) {
        if (p == null) {
            return;
        }

        p.setAlive(false);


        int aliveCount = 0;
        Player lastAlivePlayer = null;


        for (Player player : players) {
            if (player.isAlive()) {
                aliveCount++;
                lastAlivePlayer = player;
            }
        }

        if (aliveCount <= 1) {
            gameState = EstadoJogo.TERMINADO;
            if (aliveCount == 1) {
                currentPlayer = lastAlivePlayer.getId();
            } else {
                currentPlayer = null; // Ninguém sobreviveu
            }
            return;
        }

        if (currentPlayer.equals(p.getId())) {
            advanceToNextPlayer();
        }
    }

    private void advanceToNextPlayer() {
        if (players.isEmpty()) {
            currentPlayer = null;
            return;
        }

        int startIndex = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(currentPlayer)) {
                startIndex = i;
                break;
            }
        }

        for (int i = 1; i <= players.size(); i++) {
            int nextIndex = (startIndex + i) % players.size();
            Player nextPlayer = players.get(nextIndex);

            if (nextPlayer.isAlive()) {
                currentPlayer = nextPlayer.getId();
                return;
            }
        }
        currentPlayer = null;
        gameState = EstadoJogo.TERMINADO;
    }

    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {
        players.clear();
        skippedTurns.clear();
        gameBoard = null;
        currentPlayer = null;
        gameState = EstadoJogo.EM_ANDAMENTO;
        turnCount = 1;
        lastDiceRoll = 0;
        boardSize = 0;


        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Ficheiro não encontrado: " + file.getName());
        }

        if (lines.isEmpty() || !lines.get(0).equals("GPJ_SAVE_FILE")) {
            throw new InvalidFileException("Cabeçalho de ficheiro inválido.");
        }

        List<String[]> playerData = new ArrayList<>();
        List<String[]> objectData = new ArrayList<>();


        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("GPJ_SAVE_FILE")) {
                continue;
            }

            String[] parts = line.split("\\|", -1);
            String type = parts[0];

            try {
                switch (type) {
                    case "GS":
                        // GS|<boardSize>|<turnCount>|<currentPlayer>|<gameState>|<lastDiceRoll>
                        if (parts.length != 6) {
                            throw new InvalidFileException("Linha GS corrompida.");
                        }
                        boardSize = Integer.parseInt(parts[1]);
                        turnCount = Integer.parseInt(parts[2]);
                        currentPlayer = parts[3];
                        gameState = EstadoJogo.valueOf(parts[4]);
                        lastDiceRoll = Integer.parseInt(parts[5]);
                        gameBoard = new Board(boardSize);
                        break;
                    case "ST":
                        // ST|<PlayerID>|<TurnsLeft>
                        if (parts.length != 3) {
                            throw new InvalidFileException("Linha ST corrompida.");
                        }
                        skippedTurns.put(parts[1], Integer.parseInt(parts[2]));
                        break;
                    case "P":
                        // P|<ID>|<Nome>|<Linguagens>|<Cor>|<Posicao>|<Alive>|<lastPosition>|<secondLastPosition>|<Tool1;Tool2;...>
                        if (parts.length != 10) {
                            throw new InvalidFileException("Linha P corrompida.");
                        }
                        playerData.add(parts);
                        break;
                    case "O":
                        // O|<Type:A/T>|<SubtypeID>|<Position>
                        if (parts.length != 4) {
                            throw new InvalidFileException("Linha O corrompida.");
                        }
                        objectData.add(parts);
                        break;
                    default:

                }
            } catch (Exception e) {
                throw new InvalidFileException("Corrupção de dados na linha: " + line);
            }
        }

        if (boardSize == 0) {
            throw new InvalidFileException("Tamanho do tabuleiro não definido.");
        }

        restorePlayers(playerData);

        restoreGameObjects(objectData);


        boolean currentPlayerIsValid = false;
        Player firstActive = null;
        for (Player p : players) {
            if (p.getId().equals(currentPlayer) && p.isAlive()) {
                currentPlayerIsValid = true;
            }

            if (p.isAlive() && firstActive == null) {
                firstActive = p;
            }
        }

        if (!currentPlayerIsValid) {
            currentPlayer = (firstActive != null) ? firstActive.getId() : null;
        }
    }

    public boolean saveGame(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("GPJ_SAVE_FILE\n");


            String gameStateStr = gameState.name();
            String globalStateLine = "GS|" + boardSize + "|" + turnCount + "|" + currentPlayer + "|" + gameStateStr + "|" + lastDiceRoll + "\n";
            writer.write(globalStateLine);

            // 3. Turnos Saltados
            for (Map.Entry<String, Integer> entry : skippedTurns.entrySet()) {
                // ST|<PlayerID>|<TurnsLeft>
                writer.write("ST|" + entry.getKey() + "|" + entry.getValue() + "\n");
            }


            for (Player p : players) {
                String toolsStr = String.join(";", p.getFerramentas());
                String playerLine = "P|" + p.getId() + "|" + p.getNome() + "|" + p.getLinguagens() + "|" + p.getCor() + "|" +
                        p.getPosicao() + "|" + p.isAlive() + "|" + p.getLastPosition() + "|" + p.getSecondLastPosition() + "|" + toolsStr + "\n";
                writer.write(playerLine);
            }

            if (gameBoard != null) {
                for (int i = 1; i <= boardSize; i++) {
                    AbyssOrTool obj = gameBoard.getObjectAt(i);
                    if (obj != null) {
                        String typeChar = "Abyss".equals(obj.getType()) ? "A" : "T";
                        writer.write("O|" + typeChar + "|" + obj.getId() + "|" + obj.getPosition() + "\n");
                    }
                }
            }

            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public void setPlayerPosition(Player p, int newPos) {
        if (p == null || gameBoard == null) {
            return;
        }

        int oldPos = p.getPosicao();

        gameBoard.updatePlayerPosition(p, oldPos, newPos);

        p.setPosicao(newPos);
    }

    public List<String> getPlayersInSlot(int pos) {
        return gameBoard.getPlayersInSlot(pos);
    }

    public int getLastDiceRoll() {
        return lastDiceRoll;
    }

    private void restoreGameObjects(List<String[]> objectData) throws InvalidFileException {
        for (String[] data : objectData) {
            String typeChar = data[1];
            int idSubtype = Integer.parseInt(data[2]);
            int position = Integer.parseInt(data[3]);

            AbyssOrTool obj = null;
            if (typeChar.equals("A")) {
                String name = getAbyssName(idSubtype);
                if (name == null) {
                    throw new InvalidFileException("ID de Abismo inválido no ficheiro.");
                }
                obj = new Abyss(idSubtype, name, position);
            } else if (typeChar.equals("T")) {
                String name = getToolName(idSubtype);
                if (name == null) {
                    throw new InvalidFileException("ID de Ferramenta inválido no ficheiro.");
                }
                obj = new Tool(idSubtype, name, position);
            }

            if (obj != null) {
                gameBoard.placeObject(obj);
            }
        }
    }

    private void restorePlayers(List<String[]> playerData) throws InvalidFileException {
        for (String[] data : playerData) {
            String id = data[1];
            String nome = data[2];
            String linguagens = data[3];
            String cor = data[4];
            int posicao = Integer.parseInt(data[5]);
            boolean alive = Boolean.parseBoolean(data[6]);
            int lastPos = Integer.parseInt(data[7]);
            int secondLastPos = Integer.parseInt(data[8]);
            String toolsStr = data[9];

            Player p = new Player(id, nome, linguagens, cor);
            p.setAlive(alive);

            p.setPosicaoForLoad(posicao);
            p.setLastPosition(lastPos);
            p.setSecondLastPosition(secondLastPos);

            if (!toolsStr.isEmpty()) {
                p.setFerramentas(new ArrayList<>(Arrays.asList(toolsStr.split(";"))));
            }

            players.add(p);
            gameBoard.addPlayerForLoad(p, posicao);
        }
        players.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
    }
}


