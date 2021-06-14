package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
            } else {
                if (searchNode.parent.left == searchNode) {
                    searchNode.parent.left = null;
                } else {
                    searchNode.parent.right = null;
                }
            }

            searchNode.updateMinPositionToyToTop();
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
            long[] playTimeTree = new long[4 * n];
            initSegmentTree(playTimeTree, playTime, 1, 0, n - 1);

            deleteOrder.add(toys.toy);
            long maxTime = -1;
            int maxTimeToys = -1;

            for (int i = 0; i < n; i++) {
                Toy toyToDelete = deleteOrder.get(i);
                long maxTimeCandidate = roundTime + getSum(playTimeTree, 1, 0, n - 1, 0, toyToDelete.position - 1);

                if (maxTimeCandidate > maxTime) {
                    maxTime = maxTimeCandidate;
                    maxTimeToys = i;
                }

                roundTime -= playTime[toyToDelete.position];
                playTime[toyToDelete.position] = 0;
                update(playTimeTree, 1, 0, n - 1, toyToDelete.position, 0);
            }

            System.out.printf("Case #%s: %s %s\n", t, maxTimeToys, maxTime);
        }
    }

    private static void initSegmentTree(long[] tree, long[] values, int p, int intervalStart, int intervalEnd) {
        if (intervalStart == intervalEnd) {
            tree[p] = values[intervalStart];
        } else {
            int middle = (intervalStart + intervalEnd) / 2;
            initSegmentTree(tree, values, 2 * p, intervalStart, middle);
            initSegmentTree(tree, values, 2 * p + 1, middle + 1, intervalEnd);
            tree[p] = tree[2 * p] + tree[2 * p + 1];
        }
    }

    private static long getSum(long[] tree, int p, int intervalStart, int intervalEnd, int searchStart, int searchEnd) {
        if (searchStart > searchEnd) {
            return 0;
        }

        if (intervalStart == searchStart && intervalEnd == searchEnd) {
            return tree[p];
        } else {
            int middle = (intervalStart + intervalEnd) / 2;
            return getSum(tree, 2 * p, intervalStart, middle, searchStart, Math.min(middle, searchEnd)) +
                    getSum(tree, 2 * p + 1, middle + 1, intervalEnd, Math.max(intervalStart, middle + 1), searchEnd);
        }
    }

    private static void update(long[] tree, int p, int intervalStart, int intervalEnd, int updatePos, int updateVal) {
        if (intervalStart == intervalEnd && intervalStart == updatePos) {
            tree[p] = updateVal;
        } else {
            int middle = (intervalStart + intervalEnd) / 2;
            if (updatePos >= intervalStart && updatePos <= middle) {
                update(tree, 2 * p, intervalStart, middle, updatePos, updateVal);
            }
            if (updatePos >= middle + 1 && updatePos <= intervalEnd) {
                update(tree, 2 * p + 1, middle + 1, intervalEnd, updatePos, updateVal);
            }
            tree[p] = tree[2 * p] + tree[2 * p + 1];
        }
    }
}
