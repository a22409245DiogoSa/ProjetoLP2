package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.HashMap;
import java.util.List;

public abstract class AbismoPai extends AbismoOuFerramenta {
    protected HashMap<Integer, String> anulacoes = new HashMap<>();

    public AbismoPai(int id, String nome, int posicao) {
        super(id, nome, posicao);
        inicializarAnulacoes();
    }

    private void inicializarAnulacoes() {
        anulacoes.put(0, "IDE");
        anulacoes.put(1, "Testes Unitários");
        anulacoes.put(2, "Tratamento de Excepções");
        anulacoes.put(3, "Tratamento de Excepções");
        anulacoes.put(4, "Ajuda Do Professor");
        anulacoes.put(5, "Herança");
        anulacoes.put(6, "Programação Funcional");
        anulacoes.put(7, "Ajuda Do Professor");
        anulacoes.put(8, "Programação Funcional");
        anulacoes.put(9, "IDE");
        anulacoes.put(20, "Ajuda Do Professor");
    }

    @Override
    public String getTipo() {
        return "Abismo";
    }

    protected boolean anularComFerramenta(Jogador jogador) {
        String ferramentaNecessaria = anulacoes.get(id);
        if (ferramentaNecessaria != null) {
            if (jogador.getFerramentas().contains(ferramentaNecessaria)) {
                jogador.removerFerramenta(ferramentaNecessaria);
                return true;
            }
        }
        return false;
    }


    @Override
    public abstract String aplicaJogador(Jogador jogador, GameManager gestorJogo);
}
