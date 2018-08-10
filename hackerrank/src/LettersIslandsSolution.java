import java.io.*;
import java.util.*;

public class LettersIslandsSolution {

    static String text;
    static char[] chars;
    static int text_length;
    static int cou_isl_tgt;
    static long ret;
    static Ptrn ROOT;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream("D://letterIslands31.txt"));
        String text = sc.next();
        int cou_isl_tgt = sc.nextInt();
        Date start = new Date();
        System.out.println(count_patterns(text, cou_isl_tgt));
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
        sc.close();
    }

    private static final void init(String text) {
        LettersIslandsSolution.text = text;
        chars = text.toCharArray();
        text_length = chars.length;
        ROOT = new Ptrn();
        ret = 0;
    }

    private static final long count_patterns(String text, int count_islands_target) {
        if (count_islands_target == 1) {
            init(text);
            cou_isl_tgt = 1;
            char c = chars[0];
            int count_same_from_beg = 1;
            for (int i = 1; i < text_length; i++) {
                if (chars[i] == c) {
                    count_same_from_beg++;
                } else {
                    break;
                }
            }
            if (count_same_from_beg > 1000) {
                text = text.substring(count_same_from_beg);
                int length = text.length();
                StringBuilder b = new StringBuilder(length);
                for (int i = 0; i < length; i++) {
                    b.append(c);
                    count_same_from_beg--;
                }
                b.append(text);
                text = b.toString();
                init(text);
                long res_0 = _count_patterns();
                text = c + text;
                init(text);
                long res_1 = _count_patterns();
                long n = res_1 - res_0;
                long to_add = n * count_same_from_beg;
                ret = res_0 + to_add;
                return ret;
            }
        }
        init(text);
        cou_isl_tgt = count_islands_target;
        return _count_patterns();
    }

    private static final void increment_count_of_islands(Ptrn p, int increment) {
        int cou_isl_curr_old = p.count_of_islands[0];
        p.count_of_islands[0] += increment;
        int cou_isl_curr_new = p.count_of_islands[0];
        if (cou_isl_curr_old == cou_isl_tgt)
            ret--;
        else if (cou_isl_curr_new == cou_isl_tgt)
            ret++;
    }

    private static final long _count_patterns() {
        Ptrn[] char_cache = new Ptrn[26];
        for (int offset = 0; offset < text_length; offset++) {
            Ptrn curr = char_cache[chars[offset] - 'a'];
            if (curr == null) {
                curr = new Ptrn(offset, 1);
                char_cache[chars[offset] - 'a'] = curr;
            } else {
                new Ptrn(curr, offset, 1);
            }
        }
        int len_ptrn_max = 0;
        if (cou_isl_tgt == 1) {
            len_ptrn_max = lrs(new Index(0, chars.length)).l;
            if(len_ptrn_max < 2) {
                len_ptrn_max = text_length;
            } else {
                long cou_ = text_length - len_ptrn_max;
                ret += ((cou_ + 1) * cou_) / 2;
            }
        } else {
            if(cou_isl_tgt > chars.length / 100) {
                len_ptrn_max = ((chars.length + 1) / (cou_isl_tgt + 1));
            } else {
                len_ptrn_max = lrs(new Index(0, chars.length)).l;
            }
        }
        for (int len_ptrn = 2; len_ptrn <= len_ptrn_max; ++len_ptrn) {
            Ptrn next_iter = ROOT.next_iter;
            while (next_iter != null) {
                if (next_iter.ptrn_len != len_ptrn) {
                    ++next_iter.ix_of_last_c;
                    ++next_iter.ptrn_len;
                    if (next_iter.ix_of_last_c == text_length) {
                        next_iter = disconnect_iter(next_iter);
                        continue;
                    } else if (next_iter.count_of_islands[0] == cou_isl_tgt) {
                        ret++;
                    }
                }
                int ix_of_last_c = next_iter.ix_of_last_c;
                char c_ptrn = chars[ix_of_last_c];
                Ptrn next_ptrn = next_iter.next_ptrn;
                while (next_ptrn != null) {
                    boolean added = false;
                    if (next_ptrn.ptrn_len != len_ptrn) {
                        added = true;
                        next_ptrn.ix_of_last_c++;
                        next_ptrn.ptrn_len++;
                        if (next_ptrn.ix_of_last_c == text_length) {
                            next_ptrn = disconnect_ptrn(next_iter, next_ptrn);
                            continue;
                        }
                    }
                    char c_ptrn_next = chars[next_ptrn.ix_of_last_c];
                    if (c_ptrn != c_ptrn_next) {
                        Ptrn last_ptrn = next_ptrn.prev_ptrn;
                        moveToIterNext(next_iter, next_ptrn);
                        next_ptrn = last_ptrn;
                    } else {
                        decrement_if_needed(next_ptrn, added);
                    }
                    next_ptrn = next_ptrn.next_ptrn;
                }
                next_iter = next_iter.next_iter;
            }
        }
        return ret;
    }

    private static final class Index {
        final int b, l;

        public Index(int b, int e) {
            this.b = b;
            l = e - b;
        }
    }

    private static final Index lcp(Index s, Index t) {
        int n = Math.min(s.l, t.l);
        for (int i = 0; i < n; i++) {
            if (chars[i + s.b] != chars[i + t.b])
                return new Index(0, i);
        }
        return new Index(0, n);
    }

    private static final Comparator<Index> comp = new Comparator<Index>() {
        @Override
        public int compare(Index o1, Index o2) {
            int min = Math.min(o1.l, o2.l);
            for (int z = 0; z < min; ++z) {
                if (chars[o1.b + z] != chars[o2.b + z])
                    return chars[o1.b + z] - chars[o2.b + z];
            }
            return o1.l - o2.l;
        }
    };

    private static final Index lrs(Index s) {
        int N = s.l;
        Index[] suffixes = new Index[N];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new Index(i, N);
        }
        Arrays.parallelSort(suffixes, comp);
        Index lrs = new Index(0, 0);
        for (int i = 0; i < N - 1; i++) {
            Index x = lcp(suffixes[i], suffixes[i + 1]);
            if (x.l > lrs.l)
                lrs = x;
        }
        return lrs;
    }

    private static final Ptrn disconnect_iter(final Ptrn p) {
        boolean p_is_last = ROOT.last_ptrn == p;
        Ptrn p_prev_iter = p.prev_iter;
        Ptrn p_next_iter = p.next_iter;
        if (p_prev_iter != null)
            p_prev_iter.next_iter = p_next_iter;
        if (p_next_iter != null)
            p_next_iter.prev_iter = p_prev_iter;
        if (p_is_last)
            ROOT.last_ptrn = null;
        p.prev_iter = null;
        p.next_iter = null;
        return p_next_iter;
    }

    private static final void insert_iter(final Ptrn prev, final Ptrn p) {
        Ptrn prev_next_iter = prev.next_iter;
        if (prev_next_iter != null) {
            prev_next_iter.prev_iter = p;
            p.next_iter = prev_next_iter;
        } else {
            ROOT.last_iter = p;
        }
        prev.next_iter = p;
        p.prev_iter = prev;
        p.count_of_islands = new int[] { 0 };
        increment_count_of_islands(p, 1);
    }

    private static final void append_ptrn(final Ptrn p_head, final Ptrn p) {
        Ptrn to_append_to = p_head.last_ptrn;
        if (to_append_to == null) {
            to_append_to = p_head;
        }
        boolean overlap = !(to_append_to.ix_of_last_c < p.ix_of_last_c - p.ptrn_len);
        to_append_to.next_ptrn = p;
        p.prev_ptrn = to_append_to;
        p_head.last_ptrn = p;
        p.count_of_islands = p_head.count_of_islands;
        if (!overlap) {
            increment_count_of_islands(p_head, 1);
        }
    }

    private static final void decrement_if_needed(final Ptrn p, final boolean added) {
        final Ptrn p_prev_ptrn = p.prev_ptrn;
        boolean old_overlap = false;
        boolean new_overlap = false;
        if (p_prev_ptrn != null) {
            if (added)
                old_overlap = !(p_prev_ptrn.ix_of_last_c < p.ix_of_last_c - (p.ptrn_len - 1));
            else
                old_overlap = !(p_prev_ptrn.ix_of_last_c + 1 < p.ix_of_last_c - (p.ptrn_len - 1));
        }
        if (p_prev_ptrn != null) {
            new_overlap |= !(p_prev_ptrn.ix_of_last_c < p.ix_of_last_c - p.ptrn_len);
        }
        if (!old_overlap && new_overlap) {
            increment_count_of_islands(p, -1);
        }
    }

    private static final Ptrn disconnect_ptrn(final Ptrn p_head, final Ptrn p) {

        boolean p_is_last = p_head.last_ptrn == p;
        Ptrn p_prev_ptrn = p.prev_ptrn;
        Ptrn p_next_ptrn = p.next_ptrn;

        boolean old_overlap_prev = false;
        boolean old_overlap_next = false;
        boolean new_overlap = false;

        if (p_prev_ptrn != null) {
            old_overlap_prev = !(p_prev_ptrn.ix_of_last_c < p.ix_of_last_c - (p.ptrn_len - 1));
            p_prev_ptrn.next_ptrn = p_next_ptrn;
        }
        if (p_next_ptrn != null) {
            old_overlap_next = !(p.ix_of_last_c < p_next_ptrn.ix_of_last_c - (p_next_ptrn.ptrn_len - 1));
            p_next_ptrn.prev_ptrn = p_prev_ptrn;
        }
        if (p_prev_ptrn != null && p_next_ptrn != null) {
            new_overlap |= !(p_prev_ptrn.ix_of_last_c < p_next_ptrn.ix_of_last_c - p_next_ptrn.ptrn_len);
        }
        if (p_is_last) {
            p_head.last_ptrn = null;
        }
        p.prev_ptrn = null;
        p.next_ptrn = null;

        if (!new_overlap) {
            if (old_overlap_prev && old_overlap_next)
                increment_count_of_islands(p_head, 1);
            else if (!old_overlap_prev && !old_overlap_next)
                increment_count_of_islands(p_head, -1);
        }
        return p_next_ptrn;
    }

    private static final void moveToIterNext(final Ptrn ptrn_head, final Ptrn p_to_move) {
        char c_last = chars[p_to_move.ix_of_last_c];
        Ptrn p_append_to = ptrn_head;
        boolean is_last_iter = false;
        while (true) {
            if (p_append_to.next_iter == null || p_append_to.next_iter.ptrn_len != ptrn_head.ptrn_len) {
                is_last_iter = true;
                break;
            }
            if (c_last == chars[p_append_to.next_iter.ix_of_last_c]) {
                p_append_to = p_append_to.next_iter;
                break;
            }
            p_append_to = p_append_to.next_iter;
        }
        disconnect_ptrn(ptrn_head, p_to_move);
        if (is_last_iter)
            insert_iter(p_append_to, p_to_move);
        else
            append_ptrn(p_append_to, p_to_move);
    }

    final static class Ptrn {

        private int[] count_of_islands;
        private int ix_of_last_c;
        private int ptrn_len;
        private Ptrn prev_ptrn, next_ptrn, last_ptrn;
        private Ptrn prev_iter, next_iter, last_iter;

        private Ptrn() {
        }

        private Ptrn(int ix_of_last_c, int ptrn_len) {
            this.ix_of_last_c = ix_of_last_c;
            this.ptrn_len = ptrn_len;
            count_of_islands = new int[] { 0 };
            increment_count_of_islands(this, 1);
            if (ROOT.last_iter == null) {
                ROOT.next_iter = this;
                ROOT.last_iter = this;
                this.prev_iter = ROOT;
            } else {
                ROOT.last_iter.next_iter = this;
                this.prev_iter = ROOT.last_iter;
                ROOT.last_iter = this;
            }
        }

        private Ptrn(Ptrn root_ptrn, int ix_of_last_c, int ptrn_len) {
            this.ix_of_last_c = ix_of_last_c;
            this.ptrn_len = ptrn_len;
            count_of_islands = root_ptrn.count_of_islands;
            if (root_ptrn.last_ptrn == null) {
                root_ptrn.next_ptrn = this;
                root_ptrn.last_ptrn = this;
                this.prev_ptrn = root_ptrn;
            } else {
                root_ptrn.last_ptrn.next_ptrn = this;
                this.prev_ptrn = root_ptrn.last_ptrn;
                root_ptrn.last_ptrn = this;
            }
            if (prev_ptrn.ix_of_last_c < ix_of_last_c - ptrn_len) {
                increment_count_of_islands(this, 1);
            }
        }

    }

}
