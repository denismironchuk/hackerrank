package kickstart;

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
            return position + ", " + value;
        }
    }

    private static class Treap {

        private Toy toy;
        private long rank;

        private Treap left;
        private Treap right;

        private Toy minPositionToy;

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

        public Treap[] split(Toy splitToy) {
            if (toy.compareTo(splitToy) == -1) {
                if (this.right != null) {
                    Treap[] splitRes = this.right.split(splitToy);
                    this.right = splitRes[0];
                    splitRes[0] = this;
                    return splitRes;
                } else {
                    return new Treap[] {this, null};
                }
            } else {
                if (this.left != null) {
                    Treap[] splitRes = this.left.split(splitToy);
                    this.left = splitRes[1];
                    splitRes[1] = this;
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
                smaller.right = merge(smaller.right, greater);
                return smaller;
            } else {
                greater.left = merge(smaller, greater.left);
                return greater;
            }
        }

        private int countNodes() {
            int res = 1;
            if (this.left != null) {
                res += this.left.countNodes();
            }

            if (this.right != null) {
                res += this.right.countNodes();
            }

            return res;
        }

        @Override
        public String toString() {
            return String.valueOf(this.countNodes());
        }
    }

    public static void main(String[] args) {
        Toy t1 = new Toy(1, (long)(20 * Math.random()));
        Toy t2 = new Toy(2, (long)(20 * Math.random()));
        Toy t3 = new Toy(3, (long)(20 * Math.random()));
        Toy t4 = new Toy(4, (long)(20 * Math.random()));
        Toy t5 = new Toy(5, (long)(20 * Math.random()));
        Toy t6 = new Toy(6, (long)(20 * Math.random()));
        Toy t7 = new Toy(7, (long)(20 * Math.random()));
        Toy t8 = new Toy(8, (long)(20 * Math.random()));

        Treap treap = new Treap(t1).add(t2).add(t3).add(t4).add(t5).add(t6).add(t7).add(t8);
        System.out.println();
    }
}
