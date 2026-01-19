package pt.ulusofona.lp2.greatprogrammingjourney;

public class CreateAbismo {

    public static AbismoPai criarAbismo(int id, int posicao) {
        String nome = getAbyssName(id);

        switch (id) {
            case 0:
                return new AbismoErroSintaxe(id, nome, posicao);
            case 1:
                return new AbismoErroLogico(id, nome, posicao);
            case 2:
                return new AbismoException(id, nome, posicao);
            case 3:
                return new AbismoFileNotFoundException(id, nome, posicao);
            case 4:
                return new AbismoCrash(id, nome, posicao);
            case 5:
                return new AbismoCodigoDuplicado(id, nome, posicao);
            case 6:
                return new AbismoEfeitosSecundarios(id, nome, posicao);
            case 7:
                return new AbismoBlueScreenOfDeath(id, nome, posicao);
            case 8:
                return new AbismoCicloInfinito(id, nome, posicao);
            case 9:
                return new AbismoSegmentationFault(id, nome, posicao);
            case 20:
                return new AbismoLLM(id, nome, posicao);
            default:
                return null;
        }
    }

    private static String getAbyssName(int id) {
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
}
