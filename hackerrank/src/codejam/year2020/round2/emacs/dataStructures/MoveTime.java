package codejam.year2020.round2.emacs.dataStructures;

public class MoveTime {
    public static long MAX_TIME = 1000000000;

    public long toLeft = 1 + (long)(Math.random() * MAX_TIME);
    public long toRight = 1 + (long)(Math.random() * MAX_TIME);
    public long teleport = 1 + (long)(Math.random() * MAX_TIME);
}
