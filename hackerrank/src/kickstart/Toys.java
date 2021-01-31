package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Toys {

    private static class Toy implements Comparable<Toy> {

        private int position;
        private long value;

        public Toy(int position, long value) {
            this.position = position;
            this.value = value;
        }

        @Override
        public int compareTo(Toy o) {
            int valueCompare = Long.compare(value, o.value);
            if (valueCompare == 0) {
                int positionCompare = Integer.compare(position, o.position);
                return positionCompare;
            } else {
                return valueCompare;
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "\"position\":" + position +
                    ", \"value\":" + value +
                    '}';
        }
    }

    private static class Treap {

        private Toy toy;
        private long rank;

        private Treap left;
        private Treap right;

        private Toy minPositionToy;

        private Treap parent;

        public Treap(Toy toy) {
            this.toy = toy;
            this.rank = (long)(100 * Math.random());
            this.minPositionToy = toy;
        }

        public Treap add(Toy toy) {
            Treap newTreap = new Treap(toy);
            Treap[] splitted = split(toy);
            Treap merge1 = merge(splitted[0], newTreap);
            Treap merge2 = merge(merge1, splitted[1]);
            return merge2;
        }

        public void updateMinPositionToyToTop() {
            updateMinPositionToy();
            if (parent != null) {
                parent.updateMinPositionToyToTop();
            }
        }

        public void updateMinPositionToy() {
            this.minPositionToy = this.toy;
            if (this.right != null) {
                if (this.minPositionToy.position > this.right.minPositionToy.position) {
                    this.minPositionToy = this.right.minPositionToy;
                }
            }

            if (this.left != null) {
                if (this.minPositionToy.position > this.left.minPositionToy.position) {
                    this.minPositionToy = this.left.minPositionToy;
                }
            }
        }

        public void setLeft(Treap left) {
            this.left = left;
            if (this.left != null) {
                this.left.parent = this;
            }
        }

        public void setRight(Treap right) {
            this.right = right;
            if (this.right != null) {
                this.right.parent = this;
            }
        }

        public Treap[] split(Toy splitToy) {
            if (toy.compareTo(splitToy) == -1) {
                if (this.right != null) {
                    Treap[] splitRes = this.right.split(splitToy);
                    this.setRight(splitRes[0]);
                    splitRes[0] = this;
                    this.updateMinPositionToy();
                    if (splitRes[1] != null) {
                        splitRes[1].updateMinPositionToy();
                    }
                    return splitRes;
                } else {
                    return new Treap[] {this, null};
                }
            } else {
                if (this.left != null) {
                    Treap[] splitRes = this.left.split(splitToy);
                    this.setLeft(splitRes[1]);
                    splitRes[1] = this;
                    this.updateMinPositionToy();
                    if (splitRes[0] != null) {
                        splitRes[0].updateMinPositionToy();
                    }
                    return splitRes;
                } else {
                    return new Treap[] {null, this};
                }
            }
        }

        public static Treap merge(Treap smaller, Treap greater) {
            if (smaller == null) {
                return greater;
            }

            if (greater == null) {
                return smaller;
            }

            if (smaller.rank > greater.rank) {
                smaller.setRight(merge(smaller.right, greater));
                smaller.updateMinPositionToy();
                return smaller;
            } else {
                greater.setLeft(merge(smaller, greater.left));
                greater.updateMinPositionToy();
                return greater;
            }
        }

        public Treap find(Toy toy) {
            int compRes = toy.compareTo(this.toy);
            if (compRes == 0) {
                return this;
            } else if (compRes == -1) {
                if (this.left == null) {
                    return null;
                } else {
                    return this.left.find(toy);
                }
            } else {
                if (this.right == null) {
                    return null;
                } else {
                    return this.right.find(toy);
                }
            }
        }

        public void delete(Toy toy) {
            Treap searchNode = find(toy);
            if (searchNode.left != null || searchNode.right != null) {
                Treap nodeReplace = merge(searchNode.left, searchNode.right);
                searchNode.toy = nodeReplace.toy;
                searchNode.setRight(nodeReplace.right);
                searchNode.setLeft(nodeReplace.left);
                searchNode.minPositionToy = nodeReplace.minPositionToy;
                searchNode.validateMinPositionToy();
            } else {
                if (searchNode.parent.left == searchNode) {
                    searchNode.parent.left = null;
                } else {
                    searchNode.parent.right = null;
                }
            }

            searchNode.updateMinPositionToyToTop();
        }

        public int getSize() {
            int size = 1;

            if (left != null) {
                size += left.getSize();
            }

            if (right != null) {
                size += right.getSize();
            }

            return size;
        }

        public void validateMinPositionToy() {
            List<Toy> minPosToys = new ArrayList<>();
            minPosToys.add(this.toy);
            if (this.left != null) {
                this.left.validateMinPositionToy();
                minPosToys.add(this.left.minPositionToy);
            }
            if (this.right != null) {
                this.right.validateMinPositionToy();
                minPosToys.add(this.right.minPositionToy);
            }
            minPosToys.sort(Comparator.comparingInt(toy -> toy.position));
            if (minPosToys.get(0) != this.minPositionToy) {
                throw new RuntimeException();
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "\"toy\":" + toy +
                    ", \"rank\":" + rank +
                    ", \"left\":" + left +
                    ", \"right\":" + right +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                long[][] toyDescr = new long[n][2];
                long roundTime = 0;
                long[] playTime = new long[n];
                for (int j = 0; j < n; j++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    toyDescr[j][0] = Long.parseLong(tkn.nextToken());
                    toyDescr[j][1] = Long.parseLong(tkn.nextToken());
                    roundTime += toyDescr[j][0];
                    playTime[j] = toyDescr[j][0];
                }

                calculateOptimal(t, n, toyDescr, roundTime, playTime);
            }
        }
    }

    private static void calculateTrivial(int t, int n, long[][] toyDescr, long roundTime, long[] playTime) {
        int[] processed = new int[n];
        int breakPos = 0;
        for (;breakPos < n; breakPos++) {
            if (roundTime - toyDescr[breakPos][0] < toyDescr[breakPos][1]) {
                break;
            }
        }
    }

    private static void calculateOptimal(int t, int n, long[][] toyDescr, long roundTime, long[] playTime) {
        Treap toys = new Treap(new Toy(0, roundTime - toyDescr[0][0] - toyDescr[0][1]));
        for (int i = 1; i < n; i++) {
            toys = toys.add(new Toy(i, roundTime - toyDescr[i][0] - toyDescr[i][1]));
        }

        long limit = 0;
        List<Toy> deleteOrder = new ArrayList<>();
        boolean infinityFound = false;
        for (int i = 0; i < n - 1; i++) {
            Treap[] splitRes = toys.split(new Toy(Integer.MIN_VALUE, limit));
            if (splitRes[0] == null) {
                infinityFound = true;
                break;
            } else {
                Treap minPart = splitRes[0];
                Toy toyToDelete = minPart.minPositionToy;
                deleteOrder.add(toyToDelete);
                limit += playTime[toyToDelete.position];
                if (minPart.left == null && minPart.right == null) {
                    toys = splitRes[1];
                } else {
                    minPart.delete(toyToDelete);
                    toys = Treap.merge(minPart, splitRes[1]);
                }
            }
        }

        if (infinityFound) {
            System.out.printf("Case #%s: %s INDEFINITELY\n", t, deleteOrder.size());
        } else {
            deleteOrder.add(toys.toy);
            long maxTime = -1;
            int maxTimeToys = -1;

            for (int i = 0; i < n; i++) {
                Toy toyToDelete = deleteOrder.get(i);
                long maxTimeCandidate = roundTime;
                for (int j = 0; j < toyToDelete.position; j++) {
                    maxTimeCandidate += playTime[j];
                }

                if (maxTimeCandidate > maxTime) {
                    maxTime = maxTimeCandidate;
                    maxTimeToys = i;
                }

                roundTime -= playTime[toyToDelete.position];
                playTime[toyToDelete.position] = 0;
            }

            System.out.printf("Case #%s: %s %s\n", t, maxTimeToys, maxTime);
        }
    }

    private static void testTreap() {
        int toysCnt = 10;
        int maxValue = 5;
        while (true) {
            List<Toy> toys = new ArrayList<>();
            for (int i = 0; i < toysCnt; i++) {
                toys.add(new Toy(i, (long) (maxValue * Math.random())));
            }

            Treap treap = new Treap(toys.get(0));
            treap.validateMinPositionToy();
            for (int i = 1; i < toysCnt; i++) {
                int initialSize = treap.getSize();
                treap = treap.add(toys.get(i));
                treap.validateMinPositionToy();
                int sizeAfterInsert = treap.getSize();

                if (null == treap.find(toys.get(i))) {
                    throw new RuntimeException();
                }

                if (initialSize + 1 != sizeAfterInsert) {
                    throw new RuntimeException();
                }
            }

            System.out.println(treap);

            while (treap.getSize() > 1) {
                int initialSize = treap.getSize();
                int indexToDelete = (int)(toys.size() * Math.random());
                Toy toyToDelete = toys.get(indexToDelete);

                toys.remove(indexToDelete);
                System.out.println("================");
                System.out.println(toyToDelete);

                if (null == treap.find(toyToDelete)) {
                    throw new RuntimeException();
                }

                treap.delete(toyToDelete);
                treap.validateMinPositionToy();
                System.out.println(treap);
                int sizeAfterDelete = treap.getSize();

                if (null != treap.find(toyToDelete)) {
                    throw new RuntimeException();
                }

                if (initialSize - 1 != sizeAfterDelete) {
                    throw new RuntimeException();
                }
            }
        }
    }
}
