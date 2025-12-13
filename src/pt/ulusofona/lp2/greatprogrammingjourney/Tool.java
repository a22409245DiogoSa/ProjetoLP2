package pt.ulusofona.lp2.greatprogrammingjourney;

public class Tool extends AbyssOrTool {

    public Tool(int id, String name, int position) {
        super(id, name, position);
    }

    @Override
    public String getType() {
        return "Tool";
    }

    @Override
    public String apply(Player p, GameManager gm) {
        if (p.getFerramentas().contains(this.name)) {
            // Se já tiver a ferramenta, deve retornar uma mensagem, mas não apanha.
            // A Ferramenta permanece no tabuleiro (regra 1).
            return "Já possui " + this.name + ", não apanhou novamente.";
        }

        // Se não tiver, apanha.
        p.addFerramenta(this.name);
        return "Apanhou Ferramenta: " + this.name;
    }
}
