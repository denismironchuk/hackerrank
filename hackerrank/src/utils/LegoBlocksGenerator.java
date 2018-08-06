package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LegoBlocksGenerator {
    public static void main(String[] args) {
        int len = 6;
        int height = 4;

        List<List<Integer>>[] variants = new List[len + 1];
        variants[0] = new ArrayList<>();
        variants[0].add(new ArrayList<>());

        for (int i = 1; i <= len; i++) {
            variants[i] = new ArrayList<>();
            for (int blocLen = 4; blocLen > 0; blocLen--) {
                if (i - blocLen < 0) {
                    continue;
                }

                for (List<Integer> variant : variants[i - blocLen]) {
                    List<Integer> newVariant = new ArrayList<>();
                    for (Integer v : variant) {
                        newVariant.add(v);
                    }
                    newVariant.add(blocLen);
                    variants[i].add(newVariant);
                }
            }
        }

        List<List<Integer>> neededVariants = variants[len];

        List<BlockWall>[] walls = new List[height + 1];

        walls[1] = neededVariants.stream().map(variant -> {
            List<List<Integer>> blocks = new ArrayList<>();
            blocks.add(variant);
            return new BlockWall(len, 1, blocks);
        }).collect(Collectors.toList());

        for (int i = 2; i <= height; i++) {
            walls[i] = new ArrayList<>();
            for (BlockWall wall : walls[i - 1]) {
                for (List<Integer> neededVariant : neededVariants) {
                    walls[i].add(wall.clone().addRow(neededVariant));
                }
            }
        }

        long solidWallsCnt = 0;
        long notSolidWallsCnt = 0;

        for (BlockWall wall : walls[height]) {
            if (wall.isSolid()) {
                solidWallsCnt++;
            } else {
                notSolidWallsCnt++;
            }
        }

        System.out.println("All = " + walls[height].size());
        System.out.println("Solid walls = " + solidWallsCnt);
        System.out.println("Not solid walls = " + notSolidWallsCnt);
    }
}
