package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tabuleiro {
    private int tamanhoTabuleiro;
    private ArrayList<String>[] slots;
    private HashMap<String, Integer> posicoes = new HashMap<>();
    private AbismoOuFerramenta[] objetos;

    public Tabuleiro(int tamanhoTabuleiro) {
        this.tamanhoTabuleiro = tamanhoTabuleiro;
        this.slots = new ArrayList[tamanhoTabuleiro];
        this.objetos = new AbismoOuFerramenta[tamanhoTabuleiro];

        for (int i = 0; i < tamanhoTabuleiro; i++) {
            slots[i] = new ArrayList<>();
        }
    }

    public void adicionarJogador(Jogador jogador) {
        slots[0].add(jogador.getId());
        posicoes.put(jogador.getId(), 1);
        jogador.setPosicao(1);
    }

    public void adicionarJogadorParaCarregamento(Jogador jogador, int posicao) {
        if (posicao < 1 || posicao > tamanhoTabuleiro) {
            posicao = 1;
        }
        slots[posicao - 1].add(jogador.getId());
        posicoes.put(jogador.getId(), posicao);
    }

    public AbismoOuFerramenta getAbismoOuFerramenta(int posicao) {
        if (posicao < 1 || posicao > tamanhoTabuleiro) {
            return null;
        }
        return objetos[posicao - 1];
    }

    public void posicionarObjeto(AbismoOuFerramenta objeto) {
        objetos[objeto.getPosicao() - 1] = objeto;
    }

    public String[] getSlotInfo(int posicao) {
        if (posicao < 1 || posicao > tamanhoTabuleiro) {
            return new String[]{"", "", ""};
        }

        List<String> ids = slots[posicao - 1];
        String jogadoresCSV = "";
        if (!ids.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                sb.append(ids.get(i));
                if (i < ids.size() - 1) {
                    sb.append(",");
                }
            }
            jogadoresCSV = sb.toString();
        }

        AbismoOuFerramenta objeto = objetos[posicao - 1];
        String nomeObjeto = "";
        String tipoId = "";

        if (objeto != null) {
            nomeObjeto = objeto.getNome();
            String idStr = String.valueOf(objeto.getId());

            if ("Abismo".equals(objeto.getTipo())) {
                tipoId = "A:" + idStr;
            } else if ("Tool".equals(objeto.getTipo())) {
                tipoId = "T:" + idStr;
            }
        }

        return new String[]{jogadoresCSV, nomeObjeto, tipoId};
    }

    public void moverJogador(Jogador jogador, int numeroEspacos) {
        int posicaoAtual = posicoes.get(jogador.getId());
        int novaPosicao = posicaoAtual + numeroEspacos;

        if (novaPosicao > tamanhoTabuleiro) {
            int excesso = novaPosicao - tamanhoTabuleiro;
            novaPosicao = tamanhoTabuleiro - excesso;
        }

        if (novaPosicao < 1) {
            novaPosicao = 1;
        }

        slots[posicaoAtual - 1].remove(jogador.getId());
        slots[novaPosicao - 1].add(jogador.getId());
        posicoes.put(jogador.getId(), novaPosicao);
        jogador.setPosicao(novaPosicao);
    }

    public boolean verificaFinal(Jogador jogador) {
        return posicoes.get(jogador.getId()) == tamanhoTabuleiro;
    }

    public void atualizarPosicaoJogador(Jogador jogador, int posicaoAntiga, int novaPosicao) {
        if (posicaoAntiga >= 1 && posicaoAntiga <= tamanhoTabuleiro) {
            slots[posicaoAntiga - 1].remove(jogador.getId());
        }

        if (novaPosicao >= 1 && novaPosicao <= tamanhoTabuleiro) {
            slots[novaPosicao - 1].add(jogador.getId());
        }

        posicoes.put(jogador.getId(), novaPosicao);
    }

    public ArrayList<String> getJogadoresNoSlot(int posicao) {
        if (posicao < 1 || posicao > tamanhoTabuleiro) {
            return new ArrayList<>();
        }
        return slots[posicao - 1];
    }

    public int getTamanho() {
        return tamanhoTabuleiro;
    }
}