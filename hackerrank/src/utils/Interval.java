package utils;

/**
 * Created by Denis_Mironchuk on 2/28/2018.
 */
public class Interval {
    private int start;
    private int end;

    public Interval(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(final int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "[" + start + ", " + end+"]";
    }
}
