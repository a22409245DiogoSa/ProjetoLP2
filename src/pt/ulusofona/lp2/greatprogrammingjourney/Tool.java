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
        if (!p.getFerramentas().contains(name)) {
            p.addFerramenta(name);
        }
        return "Jogador agarrou " + name;
    }
}
