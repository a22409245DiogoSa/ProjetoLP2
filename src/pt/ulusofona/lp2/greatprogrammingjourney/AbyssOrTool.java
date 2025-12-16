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

    public abstract String getType();


    public abstract String apply(Player p, GameManager gm);
}