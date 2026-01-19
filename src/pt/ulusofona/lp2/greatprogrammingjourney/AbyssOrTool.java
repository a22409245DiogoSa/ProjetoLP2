package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class AbyssOrTool {
    protected int id;
    protected String name;
    protected int position;

    public AbyssOrTool(int id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPosition() { return position; }

    // Força as subclasses a identificar o seu tipo ("Abyss" ou "Tool")
    public abstract String getType();

    /**
     * Define o impacto do objeto no jogador.
     */
    public abstract String apply(Player p, GameManager gm);
}