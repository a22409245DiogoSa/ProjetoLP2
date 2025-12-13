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
        // 1. Adiciona a ferramenta ao inventário do jogador
        p.addFerramenta(this.name);

        // 2. Retorna a mensagem esperada (NÃO PODE SER NULL)
        return "Apanhou Ferramenta: " + this.name;
    }
}
