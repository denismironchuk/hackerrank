package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockWall {
    int len;
    int height;
    List<List<Integer>> blocksRows;

    public BlockWall(final int len, final int height, final List<List<Integer>> blocksRows) {
        this.len = len;
        this.height = height;
        this.blocksRows = blocksRows;
    }

    public BlockWall clone() {
        List<List<Integer>> blocksRowClone = new ArrayList<>();
        blocksRowClone.addAll(blocksRows);
        return new BlockWall(len, height, blocksRowClone);
    }

    public BlockWall addRow(List<Integer> row) {
        height++;
        blocksRows.add(row);
        return this;
    }

    public boolean isSolid() {
       List<Set<Integer>> increasingSum = new ArrayList<>();

       for (List<Integer> row : blocksRows) {
           List<Integer> sum = new ArrayList<>();
           if (row.size() > 1) {
               sum.add(row.get(0));
               for (int i = 1; i < row.size() - 1; i++) {
                   sum.add(sum.get(i - 1) + row.get(i));
               }
           }
           increasingSum.add(new HashSet<>(sum));
       }

       Set<Integer> result = increasingSum.get(0);

       for (int i = 1; i < increasingSum.size(); i++) {
           result.retainAll(increasingSum.get(i));
       }

       return result.isEmpty();
    }
}
