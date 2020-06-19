package codejam.year2020.round2.emacs.dataStructures;

public class NodesPair {
    public Parenthesis par1;
    public Parenthesis par2;
    public Parenthesis lca = null;

    public NodesPair(Parenthesis par1, Parenthesis par2) {
        this.par1 = par1;
        this.par2 = par2;
    }
}
