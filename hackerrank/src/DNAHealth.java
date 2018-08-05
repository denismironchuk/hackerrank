import java.io.*;
import java.util.*;

public class DNAHealth {
    class Gene {
        private String gene;
        private long health;
        private int index;

        public Gene(final String gene, final int index) {
            this.gene = gene;
            this.index = index;
        }

        public String getGene() {
            return gene;
        }

        public long getHealth() {
            return health;
        }

        public void setHealth(final long health) {
            this.health = health;
        }

        public int getIndex() {
            return index;
        }
    }

    class Node {
        private Node parent;
        private Node suffix;
        private List<Gene> genes = new ArrayList<>();

        private char value;
        private Node[] childrenMap = new Node[26];

        public Node(final Node parent, final char value) {
            this.value = value;
            setParent(parent);
        }

        public Node(final Node parent) {
            setParent(parent);
        }

        public void setParent(final Node parent) {
            this.parent = parent;
            if (null != this.parent) {
                this.parent.addChild(this);
            }
        }

        public Node getParent() {
            return parent;
        }

        public void addChild(Node child) {
            childrenMap[child.getValue() - 97] = child;
        }

        public void addGene(Gene g) {
            this.genes.add(g);
        }

        public void addGenes(Node n) {
            this.genes.addAll(n.getGenes());
            allGenes = true;
        }

        public Node getSuffix() {
            return suffix;
        }

        public void setSuffix(final Node suffix) {
            this.suffix = suffix;
        }

        public char getValue() {
            return value;
        }

        public List<Gene> getGenes() {
            return genes;
        }

        boolean allGenes = false;

    }

    private long process(String dna, Node root, int start, int end) {
        Node currentNode = root;
        long result = 0;

        for (int i = 0; i < dna.length(); i++) {
            char c = dna.charAt(i);

            Node child = currentNode.childrenMap[c - 97];
            if (null != child) {
                if (!child.allGenes) {
                    getSuffix(child);
                }

                //System.out.println(child.getGenes().size());

                for (Gene g : child.getGenes()) {
                    if (g.getIndex() >= start && g.getIndex() <= end) {
                        result += g.getHealth();
                    }
                }

                currentNode = child;
            } else if (currentNode.getSuffix() != null) {
                currentNode = null == currentNode.getSuffix() ? getSuffix(currentNode) : currentNode.getSuffix();
                i--;
            }
        }

        return result;
    }

    private Node buildTrie(Gene gene, Node root) {
        Node currentNode = root;

        for (int i = 0; i < gene.getGene().length(); i++) {
            char c = gene.getGene().charAt(i);
            Node child = currentNode.childrenMap[c - 97];
            if (child == null) {
                child = new Node(currentNode, c);
            }
            currentNode = child;
        }
        currentNode.addGene(gene);

        return currentNode;
    }

    private void run() throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader(new File("D:/dna2.txt")));
        int n = Integer.parseInt(br.readLine());
        String genesStr = br.readLine();
        StringTokenizer genesTkn = new StringTokenizer(genesStr, " ");

        List<Gene> genes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            genes.add(new Gene(genesTkn.nextToken(), i));
        }

        String healthStr = br.readLine();
        StringTokenizer healthTkn = new StringTokenizer(healthStr, " ");
        for (Gene gene : genes) {
            gene.setHealth(Integer.parseInt(healthTkn.nextToken()));
        }

        int s = Integer.parseInt(br.readLine());

        Node root = new Node(null);

        List<Node> leaves = new ArrayList<>();

        for (Gene gene : genes) {
            leaves.add(buildTrie(gene, root));
        }

        long[] results = new long[s];
        for (int i = 0; i < s; i++) {
            String strandStr = br.readLine();
            StringTokenizer strandTkn = new StringTokenizer(strandStr, " ");
            int start = Integer.parseInt(strandTkn.nextToken());
            int end = Integer.parseInt(strandTkn.nextToken());
            String strand = strandTkn.nextToken();
            results[i] = process(strand, root, start, end);
        }

        Arrays.sort(results);

        System.out.printf("%s %s\n", results[0], results[s-1]);
    }

    public static void main(String[] args) throws IOException {
        new DNAHealth().run();
    }

    private Node getSuffix(Node node) {
        if (null != node.getParent() && null == node.getSuffix()) {
            Node parentSuffix = getSuffix(node.getParent());
            Node suffix = null == parentSuffix ? node.getParent(): go(parentSuffix, node.getValue());
            node.setSuffix(suffix);
            if (null != getSuffix(suffix)) {
                node.addGenes(suffix);
            }
        }

        return node.getSuffix();
    }

    private Node go(Node node, char c) {
        if (null == node.childrenMap[c - 'a']) {
            Node suffix = getSuffix(node);
            node.childrenMap[c - 'a'] = null == suffix ? node : go(suffix, c);
        }
        return node.childrenMap[c - 'a'];
    }
}
