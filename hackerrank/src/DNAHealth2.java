import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class DNAHealth2 {
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

        private Node[] childrenList = new Node[26];
        private int childrenListSize = 0;
        private Node[] childrenMap = new Node[26];
        //private Node[] go = new Node[26];
        private boolean hasChildren = false;

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
            childrenList[childrenListSize] = child;
            childrenListSize++;
            childrenMap[child.getValue() - 97] = child;
            hasChildren = true;
        }

        public void addGene(Gene g) {
            this.genes.add(g);
        }

        public void addGenes(Collection g) {
            this.genes.addAll(g);
        }

        public Node getSuffix() {
            return suffix;
        }

        public void setSuffix(final Node suffix) {
            this.suffix = suffix;
        }

        public void addAllChildrenToQueue(final Queue<Node> l) {
            for (int i = 0; i < childrenListSize; i++) {
                l.add(childrenList[i]);
            }
        }

        public char getValue() {
            return value;
        }

        public List<Gene> getGenes() {
            return genes;
        }

        public boolean isHasChildren() {
            return hasChildren;
        }
    }

    private long process(String dna, Node root, int start, int end) {
        Node currentNode = root;
        long result = 0;

        for (int i = 0; i < dna.length(); i++) {
            char c = dna.charAt(i);

            Node child = currentNode.childrenMap[c - 97];
            if (null != child) {
                for (Gene g : child.getGenes()) {
                    if (g.getIndex() >= start && g.getIndex() <= end) {
                        result += g.getHealth();
                    }
                }

                currentNode = child;
            } else if (currentNode.getSuffix() != null) {
                currentNode = currentNode.getSuffix();
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

    private Node getSuffix(Node node, char value) {
        Node currentNode = node;

        while (currentNode.getSuffix() != null) {
            currentNode = currentNode.getSuffix();

            Node child = currentNode.childrenMap[value - 97];
            if (null != child) {
                return child;
            }
        }

        return currentNode;
    }

    private void setSuffixes(Node root) {
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {
            Node nd = nodes.poll();
            if (nd.getParent() != null) {
                Node suffix = getSuffix(nd.getParent(), nd.getValue());
                nd.setSuffix(suffix);
                nd.addGenes(suffix.getGenes());
            }

            nd.addAllChildrenToQueue(nodes);
        }
    }

    private void run() throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader(new File("D:/dna.txt")));
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

        for (Node leaf : leaves) {
            if (leaf.isHasChildren()) {
                continue;
            }
            getSuffix(leaf);
        }

        //setSuffixes(root);

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
        new DNAHealth2().run();
    }

    private Node getSuffix(Node nd) {
        if (nd.getParent() != null && null == nd.getSuffix()) {
            Node parentSuffix = getSuffix(nd.getParent());
            Node suffix = null == parentSuffix ? nd.getParent() : go(parentSuffix, nd.getValue());
            nd.setSuffix(suffix);
            nd.addGenes(suffix.getGenes());
        }

        return nd.getSuffix();
    }

    private Node go(Node nd, char c) {
        if (nd.childrenMap[c - 'a'] == null) {
            Node suffix = getSuffix(nd);
            return null == suffix ? nd : go(suffix, c);
        } else {
            return nd.childrenMap[c - 'a'];
        }
    }
}
