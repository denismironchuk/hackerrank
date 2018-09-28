package utils.disjointSet;

public class DisjointSet {
    private int[] parents;
    private int[] rank;

    public DisjointSet(int n) {
        parents = new int[n];
        rank = new int[n];
    }

    public void makeSet(int x) {
        parents[x] = x;
    }

    public int find(int x) {
        if (parents[x] == x) {
            return x;
        } else {
            parents[x] = find(parents[x]);
            return parents[x];
        }
    }

    public void unite(int x, int y) {
        int px = find(x);
        int py = find(y);

        if (rank[px] > rank[py]) {
            parents[py] = px;
        } else {
            parents[px] = py;
            if (rank[px] == rank[py]) {
                rank[py]++;
            }
        }
    }
}
