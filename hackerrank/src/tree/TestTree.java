package tree;

public class TestTree {
    private class Node {
        private int val;
        private Node left;
        private Node right;
        private Node parent;

        public Node(int val, Node parent) {
            this.val = val;
            this.parent = parent;
        }

        public void addChild(int newVal) {
            if (newVal <= val) {
                if (left == null) {
                    left = new Node(newVal, this);
                } else {
                    left.addChild(newVal);
                }
            } else {
                if (right == null) {
                    right = new Node(newVal, this);
                } else {
                    right.addChild(newVal);
                }
            }
        }

        public Node getLower(int searchVal) {
            if (val < searchVal) {
                if (right == null) {
                    return this;
                } else {
                    return right.getLower(searchVal);
                }
            } else {
                if (left == null) {
                    return getFirstRight();
                } else {
                    return left.getLower(searchVal);
                }
            }
        }

        /*public Node getUpper(int searchVal) {
            if (val > searchVal) {
                if (left == null) {

                }
            }
        }*/

        public Node getFirstRight() {
            if (parent == null) {
                return null;
            } else if (parent.right == this) {
                return parent;
            } else {
                return parent.getFirstRight();
            }
        }

        public Node getFirstLeft() {
            if (parent == null) {
                return null;
            } else if (parent.left == this) {
                return parent;
            } else {
                return parent.getFirstLeft();
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{\"val\":").append(val);
            if (left != null) {
                builder.append(",\"left\":").append(left);
            }
            if (right != null) {
                builder.append(",\"right\":").append(right);
            }
            builder.append("}");
            return builder.toString();
        }
    }

    private void run() {
        int n = 10;
        int max = 20;
        Node root = new Node((int)(max * Math.random()), null);
        for (int i = 1; i < n; i++) {
            root.addChild((int)(max * Math.random()));
        }

        System.out.println(root.toString());

        int searchVal = (int)(max * Math.random());
        Node lower = root.getLower(searchVal);
        System.out.println("Lower for " + searchVal + " is " + ((null == lower) ? "absent" : lower.val));
    }

    public static void main(String[] args) {
        new TestTree().run();
    }
}
