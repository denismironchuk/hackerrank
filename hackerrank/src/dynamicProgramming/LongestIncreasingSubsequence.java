package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LongestIncreasingSubsequence {
    private static class TreapNode {
        private long listVal;
        private long y;
        private long maxSeqLen;
        private long subtreeMaxSeqLen;
        private TreapNode left = null;
        private TreapNode right = null;

        public TreapNode(long listVal, long maxSeqLen) {
            this.listVal = listVal;
            this.y = (long)(100000000 * Math.random());
            this.maxSeqLen = maxSeqLen;
            this.subtreeMaxSeqLen = maxSeqLen;
        }

        public void setLeft(TreapNode left) {
            this.left = left;
        }

        public void setRight(TreapNode right) {
            this.right = right;
        }

        public long getLeftSubtreeMaxSeqLen() {
            if (this.left != null) {
                return this.left.subtreeMaxSeqLen;
            }
            return 0;
        }

        public long getRightSubtreeMaxSeqLen() {
            if (this.right != null) {
                return this.right.subtreeMaxSeqLen;
            }
            return 0;
        }

        public void updateSubtreeMaxSeqLen() {
            this.subtreeMaxSeqLen = Math.max(getLeftSubtreeMaxSeqLen(), Math.max(getRightSubtreeMaxSeqLen(), maxSeqLen));
        }

        public TreapNode[] split(long val) {
            if (listVal < val) {
                if (right == null){
                    return new TreapNode[] {this, null};
                }

                TreapNode[] lr = right.split(val);
                right = lr[0];
                updateSubtreeMaxSeqLen();
                return new TreapNode[] {this, lr[1]};
            } else {
                if (left == null) {
                    return new TreapNode[] {null, this};
                }

                TreapNode[] lr = left.split(val);
                left = lr[1];
                updateSubtreeMaxSeqLen();
                return new TreapNode[] {lr[0],this};
            }
        }

        public TreapNode findNodeAndUpdate(long listVal, long maxSeqLen) {
            if (this.listVal == listVal) {
                this.maxSeqLen = Math.max(this.maxSeqLen, maxSeqLen);
                updateSubtreeMaxSeqLen();
                return this;
            } else if (this.listVal > listVal) {
                if (left != null) {
                    TreapNode foundNode = left.findNodeAndUpdate(listVal, maxSeqLen);
                    if (foundNode != null) {
                        updateSubtreeMaxSeqLen();
                    }
                    return foundNode;
                } else {
                    return null;
                }
            } else {
                if (right != null) {
                    TreapNode foundNode = right.findNodeAndUpdate(listVal, maxSeqLen);
                    if (foundNode != null) {
                        updateSubtreeMaxSeqLen();
                    }
                    return foundNode;
                } else {
                    return null;
                }
            }
        }

        public static TreapNode merge(TreapNode left, TreapNode right) {
            if (left == null) {
                return right;
            }


            if (right == null) {
                return left;
            }

            if (left.y > right.y) {
                left.setRight(merge(left.right, right));
                left.updateSubtreeMaxSeqLen();
                return left;
            } else {
                right.setLeft(merge(left, right.left));
                right.updateSubtreeMaxSeqLen();
                return right;
            }
        }
    }

    private static long[] countMaxSeqLenArrayOpt(long[] lst) {
        long[] maxSeqLen = new long[lst.length];

        TreapNode root = new TreapNode(lst[0], 1);

        for (int i = 1; i < lst.length; i++) {
            TreapNode[] lr = root.split(lst[i]);
            maxSeqLen[i] = lr[0] == null ? 1 : lr[0].subtreeMaxSeqLen + 1;
            if (lr[1] != null && lr[1].findNodeAndUpdate(lst[i], maxSeqLen[i]) != null) {
                root = TreapNode.merge(lr[0], lr[1]);
            } else {
                root = TreapNode.merge(lr[0], TreapNode.merge(new TreapNode(lst[i], maxSeqLen[i]), lr[1]));
            }
        }

        return maxSeqLen;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            long[] lst = new long[n];
            for (int i = 0; i < n; i++) {
                lst[i] = Long.parseLong(br.readLine());
            }

            long[] resArr = countMaxSeqLenArrayOpt(lst);
            long res = -1;
            for (int i = 0; i < n; i++) {
                if (resArr[i] > res) {
                    res = resArr[i];
                }
            }
            System.out.println(res);
        }
    }
}
