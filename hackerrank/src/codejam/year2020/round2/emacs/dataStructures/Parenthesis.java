package codejam.year2020.round2.emacs.dataStructures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Parenthesis {
    public Parenthesis parent = null;
    public List<Parenthesis> children = new ArrayList<>();
    public int positionInParentChildList = -1;

    public int openAbsPosition = -1;
    public int closeAbsPosition = -1;

    public MoveTime openTime = new MoveTime();
    public MoveTime closeTime = new MoveTime();

    public long fromOpenToCloseTiming = 0;
    public long fromCloseToOpenTiming = 0;

    public Time fromParentOpening = new Time();
    public Time fromParentClosing = new Time();

    public Time toParentOpening = new Time();
    public Time toParentClosing = new Time();

    public static Parenthesis generateRandomParenthesis(int pairs) {
        Parenthesis root = new Parenthesis(null);
        root.openTime.teleport = Long.MAX_VALUE;
        root.closeTime.teleport = Long.MAX_VALUE;

        Queue<Parenthesis> q = new LinkedList<>();
        q.add(root);
        boolean isFirst = true;

        while (pairs > 0) {
            Parenthesis parent = q.peek();
            if (isFirst || Math.random() > 0.4) {
                Parenthesis child = new Parenthesis(parent);
                q.add(child);
                pairs--;
                isFirst = false;
            } else {
                q.poll();
                isFirst = true;
            }
        }

        return root;
    }

    public Parenthesis(String p, Parenthesis[] tree) {
        this.openTime.teleport = Long.MAX_VALUE;
        this.closeTime.teleport = Long.MAX_VALUE;

        Stack<Parenthesis> stack = new Stack<>();
        stack.push(this);

        for (int i = 0; i < p.length(); i++) {
            Parenthesis current = stack.peek();

            if (p.charAt(i) == '(') {
                tree[i] = new Parenthesis(current);
                tree[i].openAbsPosition = i;
                stack.push(tree[i]);
            } else {
                tree[i] = current;
                tree[i].closeAbsPosition = i;
                stack.pop();
            }
        }
    }

    public Parenthesis[] buildParenthesisArray() {
        List<Parenthesis> parArr = new ArrayList<>();
        for (Parenthesis child : children) {
            child.fillParenthesisArray(parArr);
        }
        return parArr.toArray(new Parenthesis[0]);
    }

    private void fillParenthesisArray(List<Parenthesis> parArr) {
        this.openAbsPosition = parArr.size();
        parArr.add(this);
        for (Parenthesis child : children) {
            child.fillParenthesisArray(parArr);
        }
        this.closeAbsPosition = parArr.size();
        parArr.add(this);
    }

    private Parenthesis(Parenthesis parent) {
        this.parent = parent;
        if (null != this.parent) {
            this.parent.addChild(this);
        }
    }

    public void setPositionInParentChildList(int positionInParentChildList) {
        this.positionInParentChildList = positionInParentChildList;
    }

    public void setParent(Parenthesis parent) {
        this.parent = parent;
    }

    public void addChild(Parenthesis childParenthesis) {
        childParenthesis.setPositionInParentChildList(children.size());
        childParenthesis.setParent(this);
        children.add(childParenthesis);
    }

    public void calculateTiming() {
        countFromOpenToCloseTiming();
        countFromCloseToOpenTiming();

        initTimeFromOpeningToInnerNodesOpenings();
        initTimeFromClosingToInnerNodesClosings();

        initTimeFromInnerNodesOpeningsToOpening();
        initTimeFromInnerNodesClosingsToClosing();

        finalTeleportCalculation();
    }

    /**
     * Calculate distance between two children of the same parent on the same level.
     * From source opening to dist opening and closing
     */
    public Time countTimeFromInnerOpening(int srcIndex, int destIndex) {
        Time res = new Time();

        Parenthesis src = children.get(srcIndex);
        Parenthesis dest = children.get(destIndex);

        if (srcIndex < destIndex) {
            res.opening = Math.min(dest.fromParentOpening.opening - src.fromParentOpening.opening,
                    src.toParentOpening.opening + fromOpenToCloseTiming + dest.fromParentClosing.opening);
            res.closing = Math.min(dest.fromParentOpening.closing - src.fromParentOpening.opening,
                    src.toParentOpening.opening + fromOpenToCloseTiming + dest.fromParentClosing.closing);
        } else {
            res.opening = Math.min(dest.fromParentClosing.opening - src.fromParentClosing.opening,
                    src.toParentClosing.opening + fromCloseToOpenTiming + dest.fromParentOpening.opening);
            res.closing = Math.min(dest.fromParentClosing.closing - src.fromParentClosing.opening,
                    src.toParentClosing.opening + fromCloseToOpenTiming + dest.fromParentOpening.closing);
        }

        return res;
    }

    /**
     * Calculate distance between two children of the same parent on the same level.
     * From source closing to dist opening and closing
     */
    public Time countTimeFromInnerClosing(int srcIndex, int destIndex) {
        Time res = new Time();

        Parenthesis src = children.get(srcIndex);
        Parenthesis dest = children.get(destIndex);

        if (srcIndex < destIndex) {
            res.opening = Math.min(dest.fromParentOpening.opening - src.fromParentOpening.closing,
                    src.toParentOpening.closing + fromOpenToCloseTiming + dest.fromParentClosing.opening);
            res.closing = Math.min(dest.fromParentOpening.closing - src.fromParentOpening.closing,
                    src.toParentOpening.closing + fromOpenToCloseTiming + dest.fromParentClosing.closing);
        } else {
            res.opening = Math.min(dest.fromParentClosing.opening - src.fromParentClosing.closing,
                    src.toParentClosing.closing + fromCloseToOpenTiming + dest.fromParentOpening.opening);
            res.closing = Math.min(dest.fromParentClosing.closing - src.fromParentClosing.closing,
                    src.toParentClosing.closing + fromCloseToOpenTiming + dest.fromParentOpening.closing);
        }

        return res;
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |------------------------------------->|
     */
    private long countFromOpenToCloseTiming() {
        fromOpenToCloseTiming = openTime.toRight;
        for (Parenthesis child : children) {
            fromOpenToCloseTiming += child.countFromOpenToCloseTiming();
            fromOpenToCloseTiming += child.closeTime.toRight;
        }
        fromOpenToCloseTiming = Math.min(fromOpenToCloseTiming, openTime.teleport);

        return fromOpenToCloseTiming;
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |<-------------------------------------|
     */
    private long countFromCloseToOpenTiming() {
        fromCloseToOpenTiming = closeTime.toLeft;
        for (Parenthesis child : children) {
            fromCloseToOpenTiming += child.countFromCloseToOpenTiming();
            fromCloseToOpenTiming += child.openTime.toLeft;
        }
        fromCloseToOpenTiming = Math.min(fromCloseToOpenTiming, closeTime.teleport);

        return fromCloseToOpenTiming;
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |->|  |  |           |  |        |
     * |---->|  |           |  |        |
     * |------->|           |  |        |
     * |------------------->|  |        |
     * |---------------------->|        |
     * |------------------------------->|
     */
    private void initTimeFromOpeningToInnerNodesOpenings() {
        long time = openTime.toRight;
        for (int index = 0; index < children.size(); index++) {
            Parenthesis child = children.get(index);
            child.fromParentOpening.opening = time;
            time += child.fromOpenToCloseTiming;
            child.fromParentOpening.closing = time;
            time += child.closeTime.toRight;

            child.initTimeFromOpeningToInnerNodesOpenings();
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     *             |  |  |        |  |     |<-|
     *             |  |  |        |  |<-------|
     *             |  |  |        |<----------|
     *             |  |  |<-------------------|
     *             |  |<----------------------|
     *             |<-------------------------|
     */
    private void initTimeFromClosingToInnerNodesClosings() {
        long time = closeTime.toLeft;
        for (int index = children.size() - 1; index >= 0; index--) {
            Parenthesis child = children.get(index);
            child.fromParentClosing.closing = time;
            time += child.fromCloseToOpenTiming;
            child.fromParentClosing.opening = time;
            time += child.openTime.toLeft;

            child.initTimeFromClosingToInnerNodesClosings();
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |<-|  |  |           |  |        |
     * |<----|  |           |  |        |
     * |<-------|           |  |        |
     * |<-------------------|  |        |
     * |<----------------------|        |
     * |<-------------------------------|
     */
    private void initTimeFromInnerNodesOpeningsToOpening() {
        long time = 0;
        for (int index = 0; index < children.size(); index++) {
            Parenthesis child = children.get(index);
            time += child.openTime.toLeft;
            child.toParentOpening.opening = time;
            time += child.fromCloseToOpenTiming;
            child.toParentOpening.closing = time;

            child.initTimeFromInnerNodesOpeningsToOpening();
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     *             |  |  |        |  |     |->|
     *             |  |  |        |  |------->|
     *             |  |  |        |---------->|
     *             |  |  |------------------->|
     *             |  |---------------------->|
     *             |------------------------->|
     */
    private void initTimeFromInnerNodesClosingsToClosing() {
        long time = 0;
        for (int index = children.size() - 1; index >= 0; index--) {
            Parenthesis child = children.get(index);
            time += child.closeTime.toRight;
            child.toParentClosing.closing = time;
            time += child.fromOpenToCloseTiming;
            child.toParentClosing.opening = time;

            child.initTimeFromInnerNodesClosingsToClosing();
        }
    }

    /**
     * >-------------------------------------------->
     * |                                            |
     * |                                            |
     * <--(  (  (  (  )  )  )  (  (  )  )  (  )  )<--
     *
     * <--------------------------------------------<
     * |                                            |
     * |                                            |
     * -->(  (  (  (  )  )  )  (  (  )  )  (  )  )-->
     */
    private void finalTeleportCalculation() {
        if (parent != null) {
            long candidate1 = toParentOpening.opening + parent.fromOpenToCloseTiming + fromParentClosing.closing;
            fromOpenToCloseTiming = Math.min(fromOpenToCloseTiming, candidate1);
            long candidate2 = toParentClosing.closing + parent.fromCloseToOpenTiming + fromParentOpening.opening;
            fromCloseToOpenTiming = Math.min(fromCloseToOpenTiming, candidate2);
        }

        for (Parenthesis child : children) {
            child.finalTeleportCalculation();
        }
    }

    @Override
    public String toString() {
        return "(" + children.stream().map(Parenthesis::toString).collect(Collectors.joining()) + ")";
    }
}
