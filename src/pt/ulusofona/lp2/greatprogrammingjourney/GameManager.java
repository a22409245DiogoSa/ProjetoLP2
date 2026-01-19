package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class GameManager {
    private String[][] informacaoJogadores;
    private String[][] abismosEFerramentas;
    private ArrayList<Jogador> jogadores = new ArrayList<>();
    private int contadorTurnos = 1;
    private int tamanhoTabuleiro;
    private Tabuleiro tabuleiro;
    private HashMap<String, Integer> turnosSaltados = new HashMap<>();
    private int ultimoLancamentoDado = 0;
    private String jogadorAtual;
    private EstadoJogo estadoJogo;
    private Map<String, Integer> turnosPorJogador = new HashMap<>();
    private HashMap<Integer, String> presoNoCicloPorPosicao = new HashMap<>();

    public GameManager() {
    }

    public boolean createInitialBoard(String[][] informacaoJogadores, int tamanhoMundo, String[][] abismosEFerramentas) {
        if (informacaoJogadores == null || informacaoJogadores.length < 2 || informacaoJogadores.length > 4) {
            return false;
        }
        if (tamanhoMundo < (2 * informacaoJogadores.length)) {
            return false;
        }
        this.abismosEFerramentas = abismosEFerramentas;
        this.informacaoJogadores = informacaoJogadores;
        this.tamanhoTabuleiro = tamanhoMundo;
        this.tabuleiro = new Tabuleiro(tamanhoMundo);
        this.jogadores.clear();
        this.turnosSaltados.clear();
        this.presoNoCicloPorPosicao.clear();
        this.turnosPorJogador.clear();

        for (String[] jogadorInfo : informacaoJogadores) {
            if (jogadorInfo == null || jogadorInfo.length != 4) {
                return false;
            }
            for (String campo : jogadorInfo) {
                if (campo == null || campo.trim().isEmpty()) {
                    return false;
                }
            }

            Jogador jogador = new Jogador(jogadorInfo[0], jogadorInfo[1], jogadorInfo[2], jogadorInfo[3]);
            jogador.setVivo(true);
            jogadores.add(jogador);
            tabuleiro.adicionarJogador(jogador);
        }

        jogadores.sort(Comparator.comparingInt(j -> Integer.parseInt(j.getId())));
        jogadorAtual = jogadores.get(0).getId();
        estadoJogo = EstadoJogo.EM_ANDAMENTO;
        contadorTurnos = 1;

        if (abismosEFerramentas != null) {
            for (String[] item : abismosEFerramentas) {
                if (item == null || item.length != 3) {
                    return false;
                }

                try {
                    int tipo = Integer.parseInt(item[0]);
                    int idSubtipo = Integer.parseInt(item[1]);
                    int posicao = Integer.parseInt(item[2]);

                    if (posicao < 1 || posicao > tamanhoMundo) {
                        return false;
                    }
                    if (tabuleiro.getAbismoOuFerramenta(posicao) != null) {
                        return false;
                    }

                    if (tipo == 0) {
                        AbismoPai abismo = CriacaoAbismo.criarAbismo(idSubtipo, posicao);
                        if (abismo == null) {
                            return false;
                        }
                        tabuleiro.posicionarObjeto(abismo);
                    } else if (tipo == 1) {
                        String nome = getNomeFerramenta(idSubtipo);
                        if (nome == null) {
                            return false;
                        }
                        Ferramenta ferramenta = new Ferramenta(idSubtipo, nome, posicao);
                        tabuleiro.posicionarObjeto(ferramenta);
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

    public boolean createInitialBoard(String[][] informacaoJogadores, int tamanhoMundo) {
        return createInitialBoard(informacaoJogadores, tamanhoMundo, null);
    }

    private String getNomeAbismo(int id) {
        switch (id) {
            case 0: return "Erro de sintaxe";
            case 1: return "Erro de lógica";
            case 2: return "Exception";
            case 3: return "FileNotFoundException";
            case 4: return "Crash";
            case 5: return "Código duplicado";
            case 6: return "Efeitos secundários";
            case 7: return "Blue Screen of Death";
            case 8: return "Ciclo Infinito";
            case 9: return "Segmentation fault";
            case 20: return "LLM";
            default: return null;
        }
    }

    private String getNomeFerramenta(int id) {
        switch (id) {
            case 0: return "Herança";
            case 1: return "Programação Funcional";
            case 2: return "Testes Unitários";
            case 3: return "Tratamento de Excepções";
            case 4: return "IDE";
            case 5: return "Ajuda Do Professor";
            default: return null;
        }
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > tamanhoTabuleiro || tabuleiro == null) {
            return null;
        }

        if (nrSquare == tamanhoTabuleiro) {
            return "glory.png";
        }

        if (this.abismosEFerramentas != null) {
            String posicaoProcurada = String.valueOf(nrSquare);

            // Percorre o array de configuração inicial
            for (String[] item : this.abismosEFerramentas) {

                // Verifica se a posição do item no array corresponde ao nrSquare
                if (item.length == 3 && item[2].equals(posicaoProcurada)) {

                    // Encontrou o item na posição nrSquare
                    int tipo = Integer.parseInt(item[0]);      // 0 ou 1
                    int idSubtipo = Integer.parseInt(item[1]); // ID

                    if (tipo == 0) { // Abismo
                    } else if (tipo == 1) { // Ferramenta
                    }
                }
            }
        }

        // Retorna null se não houver Abismo/Ferramenta nessa posição no array inicial
        return null;
    }

    public String[] getProgrammerInfo(int id) {
        for (Jogador jogador : jogadores) {
            if (Integer.parseInt(jogador.getId()) == id) {
                return jogador.toArray();
            }
        }
        return null;
    }

    public String getProgrammersInfo() {
        ArrayList<String> partesInfo = new ArrayList<>();

        for (Jogador jogador : jogadores) {
            if (jogador.estaVivo()) {
                String nome = jogador.getNome();
                ArrayList<String> ferramentas = jogador.getFerramentas();

                String stringFerramentas;
                if (ferramentas.isEmpty()) {
                    stringFerramentas = "No tools";
                } else {
                    stringFerramentas = String.join(";", ferramentas);
                }

                partesInfo.add(nome + " : " + stringFerramentas);
            }
        }

        return String.join(" | ", partesInfo);
    }

    public String getProgrammerInfoAsStr(int id) {
        Jogador jogador = getJogadorById(String.valueOf(id));
        if (jogador == null) {
            return null;
        }

        String ferramentas = jogador.getFerramentas().isEmpty() ? "No tools"
                : String.join("; ", jogador.getFerramentas());

        String estado;
        if (!jogador.estaVivo()) {
            estado = "Derrotado";
        } else if ((turnosSaltados.getOrDefault(jogador.getId(), 0) > 0) || estaPresoNoCiclo(jogador)) {
            estado = "Preso";
        } else {
            estado = "Em Jogo";
        }

        return jogador.getId() + " | " + jogador.getNome() + " | " + jogador.getPosicao() + " | " + ferramentas +
                " | " + jogador.getLinguagensOrdenadas() + " | " + estado;
    }

    public String[] getSlotInfo(int position) {
        return tabuleiro.getSlotInfo(position);
    }

    public int getCurrentPlayerID() {
        return Integer.parseInt(jogadorAtual);
    }

    public boolean moveCurrentPlayer(int numeroEspacos) {
        if (estadoJogo == EstadoJogo.TERMINADO || tabuleiro == null) {
            return false;
        }

        if (jogadorAtual == null) {
            return false;
        }

        Jogador atual = getJogadorById(jogadorAtual);

        if (atual == null || !atual.estaVivo()) {
            return false;
        }

        if (jogadorEstaPresoNoCiclo(atual.getId())) {
            contadorTurnos++;
            return false;
        }

        int saltos = turnosSaltados.getOrDefault(atual.getId(), 0);
        if (saltos > 0) {
            saltos--;
            if (saltos <= 0) {
                turnosSaltados.remove(atual.getId());
            } else {
                turnosSaltados.put(atual.getId(), saltos);
            }
            contadorTurnos++;
            return false;
        }

        boolean isEasterEgg = numeroEspacos < 0;
        int espacosEfetivos = isEasterEgg ? Math.abs(numeroEspacos) : numeroEspacos;

        if (!isEasterEgg) {
            if (numeroEspacos < 1 || numeroEspacos > 6) {
                return false;
            }

            String linguagem = atual.getLinguagensFavoritas();
            if (linguagem != null && !linguagem.isEmpty()) {
                String primeira = linguagem.contains(";") ? linguagem.split(";")[0] : linguagem;
                primeira = primeira.trim();

                if (primeira.equalsIgnoreCase("C") && numeroEspacos >= 4) {
                    return false;
                }

                if (primeira.equalsIgnoreCase("Assembly") && numeroEspacos >= 3) {
                    return false;
                }
            }
        }

        tabuleiro.moverJogador(atual, espacosEfetivos);

        turnosPorJogador.put(atual.getId(), turnosPorJogador.getOrDefault(atual.getId(), 0) + 1);

        ultimoLancamentoDado = espacosEfetivos;
        contadorTurnos++;

        if (tabuleiro.verificaFinal(atual)) {
            estadoJogo = EstadoJogo.TERMINADO;
            return true;
        }

        return true;
    }


    public Map<String, Integer> getTurnosPorJogador() {
        return turnosPorJogador;
    }

    public String reactToAbyssOrTool() {
        Jogador jogador = getJogadorById(jogadorAtual);
        if (jogador == null) {
            return null;
        }

        int pos = jogador.getPosicao();
        AbismoOuFerramenta objeto = tabuleiro.getAbismoOuFerramenta(pos);

        if (objeto == null) {
            if (estadoJogo != EstadoJogo.TERMINADO && jogador.estaVivo()) {
                avancarParaProximoJogador();
            }
            return null;
        }

        String resultado = objeto.aplicaJogador(jogador, this);

        if (estadoJogo != EstadoJogo.TERMINADO && jogador.estaVivo()) {
            avancarParaProximoJogador();
        }

        return resultado;
    }

    public boolean gameIsOver() {
        if (getVencedorNome() != null) {
            return true;
        }

        return !existeJogadorEmJogo();
    }

    public boolean existeJogadorEmJogo() {
        for (Jogador j : jogadores) {
            if (!j.estaVivo()) {
                continue;
            }

            int presoTurnos = turnosSaltados.getOrDefault(j.getId(), 0);
            boolean presoCiclo = estaPresoNoCiclo(j);

            if (presoTurnos <= 0 && !presoCiclo) {
                return true;
            }
        }
        return false;
    }


    public boolean estaPresoNoCiclo(Jogador jogador) {
        return jogador != null && jogadorEstaPresoNoCiclo(jogador.getId());
    }

    public void prenderNoCiclo(int posicao, String jogadorId) {
        if (jogadorId == null) {
            return;
        }
        presoNoCicloPorPosicao.put(posicao, jogadorId);
    }

    public void libertarPresoDoCiclo(int posicao) {
        presoNoCicloPorPosicao.remove(posicao);
    }

    public String getPresoDoCiclo(int posicao) {
        return presoNoCicloPorPosicao.get(posicao);
    }

    public boolean jogadorEstaPresoNoCiclo(String jogadorId) {
        if (jogadorId == null) {
            return false;
        }
        return presoNoCicloPorPosicao.containsValue(jogadorId);
    }

    public void limparTurnosSaltados(Jogador jogador) {
        if (jogador == null) {
            return;
        }
        turnosSaltados.remove(jogador.getId());
    }

    public HashMap<String, Integer> getTurnosSaltados() {
        return turnosSaltados;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> resultados = new ArrayList<>();

        if (!gameIsOver()) {
            return resultados;
        }

        if (!existeJogadorEmJogo()) {
            resultados.add("THE GREAT PROGRAMMING JOURNEY");
            resultados.add("");
            resultados.add("NR. DE TURNOS");
            resultados.add(String.valueOf(contadorTurnos));
            resultados.add("");
            resultados.add("O jogo terminou empatado.");
            resultados.add("");
            resultados.add("Participantes:");

            ArrayList<Jogador> participantes = new ArrayList<>();

            for (Jogador jogador : jogadores) {
                participantes.add(jogador);
            }

            participantes.sort((a, b) -> b.getPosicao() - a.getPosicao());

            for (Jogador jogador : participantes) {
                int ultimaPos = jogador.getPosicao();
                AbismoOuFerramenta slot = tabuleiro.getAbismoOuFerramenta(ultimaPos);

                String nomeUltimoAbismo = "";

                if (slot != null && slot.getTipo().equals("Abismo")) {
                    nomeUltimoAbismo = slot.getNome();
                }

                resultados.add(jogador.getNome() + " : " + jogador.getPosicao() + " : " + nomeUltimoAbismo);
            }
            return resultados;
        }

        resultados.add("THE GREAT PROGRAMMING JOURNEY");
        resultados.add("");
        resultados.add("NR. DE TURNOS");
        resultados.add(String.valueOf(contadorTurnos));
        resultados.add("");
        resultados.add("VENCEDOR");

        String vencedor = getVencedorNome();
        if (vencedor != null) {
            resultados.add(vencedor);
        } else {
            resultados.add("Desconhecido");
        }

        resultados.add("");
        resultados.add("RESTANTES");

        ArrayList<Jogador> restantes = new ArrayList<>();

        for (Jogador jogador : jogadores) {
            if (!jogador.getNome().equals(vencedor)) {
                restantes.add(jogador);
            }
        }

        restantes.sort((a, b) -> b.getPosicao() - a.getPosicao());

        for (Jogador jogador : restantes) {
            resultados.add(jogador.getNome() + " " + jogador.getPosicao());
        }

        return resultados;
    }

    public String getVencedorNome() {
        for (Jogador jogador : jogadores) {
            if (tabuleiro.verificaFinal(jogador)) {
                return jogador.getNome();
            }
        }
        return null;
    }

    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {
        jogadores.clear();
        turnosSaltados.clear();
        tabuleiro = null;
        jogadorAtual = null;
        estadoJogo = EstadoJogo.EM_ANDAMENTO;
        contadorTurnos = 1;
        ultimoLancamentoDado = 0;
        tamanhoTabuleiro = 0;

        ArrayList<String> linhas = new ArrayList<>();
        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            while (scanner.hasNextLine()) {
                linhas.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Ficheiro não encontrado: " + file.getName());
        }

        if (linhas.isEmpty() || !linhas.get(0).equals("GPJ_SAVE_FILE")) {
            throw new InvalidFileException("Cabeçalho de ficheiro inválido.");
        }

        ArrayList<String[]> dadosJogadores = new ArrayList<>();
        ArrayList<String[]> dadosObjetos = new ArrayList<>();

        for (String linha : linhas) {
            if (linha.isEmpty() || linha.startsWith("GPJ_SAVE_FILE")) {
                continue;
            }

            String[] partes = linha.split("\\|", -1);
            String tipo = partes[0];

            try {
                switch (tipo) {
                    case "GS":
                        if (partes.length != 6) {
                            throw new InvalidFileException("Linha GS corrompida.");
                        }
                        tamanhoTabuleiro = Integer.parseInt(partes[1]);
                        contadorTurnos = Integer.parseInt(partes[2]);
                        jogadorAtual = partes[3];
                        estadoJogo = EstadoJogo.valueOf(partes[4]);
                        ultimoLancamentoDado = Integer.parseInt(partes[5]);
                        tabuleiro = new Tabuleiro(tamanhoTabuleiro);
                        break;
                    case "ST":
                        if (partes.length != 3) {
                            throw new InvalidFileException("Linha ST corrompida.");
                        }
                        turnosSaltados.put(partes[1], Integer.parseInt(partes[2]));
                        break;
                    case "P":
                        if (partes.length != 10) {
                            throw new InvalidFileException("Linha P corrompida.");
                        }
                        dadosJogadores.add(partes);
                        break;
                    case "O":
                        if (partes.length != 4) {
                            throw new InvalidFileException("Linha O corrompida.");
                        }
                        dadosObjetos.add(partes);
                        break;
                    default:
                        // Ignorar outras linhas
                }
            } catch (Exception e) {
                throw new InvalidFileException("Corrupção de dados na linha: " + linha);
            }
        }

        if (tamanhoTabuleiro == 0) {
            throw new InvalidFileException("Tamanho do tabuleiro não definido.");
        }

        restaurarJogadores(dadosJogadores);
        restaurarObjetosJogo(dadosObjetos);

        boolean jogadorAtualValido = false;
        Jogador primeiroAtivo = null;
        for (Jogador jogador : jogadores) {
            if (jogador.getId().equals(jogadorAtual) && jogador.estaVivo()) {
                jogadorAtualValido = true;
            }

            if (jogador.estaVivo() && primeiroAtivo == null) {
                primeiroAtivo = jogador;
            }
        }

        if (!jogadorAtualValido) {
            jogadorAtual = (primeiroAtivo != null) ? primeiroAtivo.getId() : null;
        }
    }

    public boolean saveGame(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("GPJ_SAVE_FILE\n");

            String estadoJogoStr = estadoJogo.name();
            String linhaEstadoGlobal = "GS|" + tamanhoTabuleiro + "|" + contadorTurnos + "|" + jogadorAtual + "|" + estadoJogoStr + "|" + ultimoLancamentoDado + "\n";
            writer.write(linhaEstadoGlobal);

            for (Map.Entry<String, Integer> entry : turnosSaltados.entrySet()) {
                writer.write("ST|" + entry.getKey() + "|" + entry.getValue() + "\n");
            }

            for (Jogador jogador : jogadores) {
                String stringFerramentas = String.join(";", jogador.getFerramentas());
                String linhaJogador = "P|" + jogador.getId() + "|" + jogador.getNome() + "|" + jogador.getLinguagensFavoritas() + "|" + jogador.getCorAvatar() + "|" +
                        jogador.getPosicao() + "|" + jogador.estaVivo() + "|" + jogador.getUltimaPosicao() + "|" + jogador.getPenultimaPosicao() + "|" + stringFerramentas + "\n";
                writer.write(linhaJogador);
            }

            if (tabuleiro != null) {
                for (int i = 1; i <= tamanhoTabuleiro; i++) {
                    AbismoOuFerramenta objeto = tabuleiro.getAbismoOuFerramenta(i);
                    if (objeto != null) {
                        String tipoChar = "Abismo".equals(objeto.getTipo()) ? "A" : "T";
                        writer.write("O|" + tipoChar + "|" + objeto.getId() + "|" + objeto.getPosicao() + "\n");
                    }
                }
            }

            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 300));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("CRÉDITOS", SwingConstants.CENTER);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel autor1 = new JLabel("Rafael Magalhês - a22407372");
        JLabel autor2 = new JLabel("Diogo Alves - a22407372");

        autor1.setAlignmentX(Component.CENTER_ALIGNMENT);
        autor2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(autor1);
        panel.add(autor2);

        return panel;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }

    public int getLastDiceRoll() {
        return ultimoLancamentoDado;
    }

    public Jogador getJogadorById(String id) {
        for (Jogador jogador : jogadores) {
            if (jogador.getId().equals(id)) {
                return jogador;
            }
        }
        return null;
    }

    public void avancarParaProximoJogador() {
        if (jogadores.isEmpty()) {
            jogadorAtual = null;
            return;
        }

        int indiceInicial = 0;
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getId().equals(jogadorAtual)) {
                indiceInicial = i;
                break;
            }
        }

        for (int i = 1; i <= jogadores.size(); i++) {
            int proximoIndice = (indiceInicial + i) % jogadores.size();
            Jogador proximoJogador = jogadores.get(proximoIndice);

            if (proximoJogador.estaVivo()) {
                jogadorAtual = proximoJogador.getId();
                return;
            }
        }

        jogadorAtual = null;
        estadoJogo = EstadoJogo.TERMINADO;
    }



    public void skipTurns(Jogador jogador, int n) {
        if (jogador == null || n <= 0) {
            return;
        }
        turnosSaltados.put(jogador.getId(), turnosSaltados.getOrDefault(jogador.getId(), 0) + n);
    }

    public void eliminatePlayer(Jogador jogador) {
        if (jogador == null) {
            return;
        }

        jogador.setVivo(false);

        int contagemVivos = 0;
        Jogador ultimoJogadorVivo = null;

        for (Jogador j : jogadores) {
            if (j.estaVivo()) {
                contagemVivos++;
                ultimoJogadorVivo = j;
            }
        }

        if (contagemVivos <= 1) {
            estadoJogo = EstadoJogo.TERMINADO;
            if (contagemVivos == 1) {
                jogadorAtual = ultimoJogadorVivo.getId();
            } else {
                jogadorAtual = null;
            }
            return;
        }

        if (jogadorAtual.equals(jogador.getId())) {
            avancarParaProximoJogador();
        }
    }

    public void setPlayerPosition(Jogador jogador, int novaPosicao) {
        if (jogador == null || tabuleiro == null) {
            return;
        }

        int posicaoAntiga = jogador.getPosicao();
        tabuleiro.atualizarPosicaoJogador(jogador, posicaoAntiga, novaPosicao);
        jogador.setPosicao(novaPosicao);
    }

    public ArrayList<String> getPlayersInSlot(int pos) {
        return tabuleiro.getJogadoresNoSlot(pos);
    }

    private void restaurarObjetosJogo(ArrayList<String[]> dadosObjetos) throws InvalidFileException {
        for (String[] dados : dadosObjetos) {
            String tipoChar = dados[1];
            int idSubtipo = Integer.parseInt(dados[2]);
            int posicao = Integer.parseInt(dados[3]);

            AbismoOuFerramenta objeto = null;
            if (tipoChar.equals("A")) {
                objeto = CriacaoAbismo.criarAbismo(idSubtipo, posicao);
                if (objeto == null) {
                    throw new InvalidFileException("ID de Abismo inválido no ficheiro.");
                }
            } else if (tipoChar.equals("T")) {
                String nome = getNomeFerramenta(idSubtipo);
                if (nome == null) {
                    throw new InvalidFileException("ID de Ferramenta inválido no ficheiro.");
                }
                objeto = new Ferramenta(idSubtipo, nome, posicao);
            }

            if (objeto != null) {
                tabuleiro.posicionarObjeto(objeto);
            }
        }
    }

    private void restaurarJogadores(ArrayList<String[]> dadosJogadores) throws InvalidFileException {
        for (String[] dados : dadosJogadores) {
            String id = dados[1];
            String nome = dados[2];
            String linguagens = dados[3];
            String cor = dados[4];
            int posicao = Integer.parseInt(dados[5]);
            boolean vivo = Boolean.parseBoolean(dados[6]);
            int ultimaPos = Integer.parseInt(dados[7]);
            int penultimaPos = Integer.parseInt(dados[8]);
            String stringFerramentas = dados[9];

            Jogador jogador = new Jogador(id, nome, linguagens, cor);
            jogador.setVivo(vivo);

            jogador.setPosicao(posicao);
            jogador.setUltimaPosicao(ultimaPos);
            jogador.setPenultimaPosicao(penultimaPos);

            if (!stringFerramentas.isEmpty()) {
                ArrayList<String> ferramentasList = new ArrayList<>(Arrays.asList(stringFerramentas.split(";")));
                jogador.setFerramentas(ferramentasList);
            }

            jogadores.add(jogador);
            tabuleiro.adicionarJogadorParaCarregamento(jogador, posicao);
        }
        jogadores.sort(Comparator.comparingInt(j -> Integer.parseInt(j.getId())));
    }

    public String getJogadorAtual() {
        return jogadorAtual;
    }

    public int getTamanhoTabuleiro() {
        return tamanhoTabuleiro;
    }

    public int getContadorTurnos() {
        return contadorTurnos;
    }

    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    public void setEstadoJogo(EstadoJogo estadoJogo) {
        this.estadoJogo = estadoJogo;
    }
}