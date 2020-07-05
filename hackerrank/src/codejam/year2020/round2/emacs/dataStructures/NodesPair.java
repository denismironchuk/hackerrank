package codejam.year2020.round2.emacs.dataStructures;

public class NodesPair {
    public Parenthesis par1;
    public boolean par1Open = false;
    public Parenthesis par2;
    public boolean par2Open = false;
    public Parenthesis lca = null;

    public NodesPair(Parenthesis par1, Parenthesis par2) {
        this.par1 = par1;
        this.par2 = par2;
    }

    public NodesPair(Parenthesis par1, boolean par1Open, Parenthesis par2, boolean par2Open) {
        this.par1 = par1;
        this.par1Open = par1Open;
        this.par2 = par2;
        this.par2Open = par2Open;
    }
}
