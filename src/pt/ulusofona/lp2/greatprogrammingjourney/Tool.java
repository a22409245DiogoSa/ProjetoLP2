package pt.ulusofona.lp2.greatprogrammingjourney;

public class Tool extends AbyssOrTool {

    public Tool(int id, String name, int position) {
        super(id, name, position);
    }

    @Override
    public String getType() { return "Tool"; }

    @Override
    public String apply(Player p, GameManager gm) {
        // Regra: Não se pode apanhar uma ferramenta duplicada que já esteja no inventário
        if (p.getFerramentas().contains(this.name)) {
            return "Já possui " + this.name + ", não apanhou novamente.";
        }

        p.addFerramenta(this.name);
        return "Apanhou Ferramenta: " + this.name;
    }
}