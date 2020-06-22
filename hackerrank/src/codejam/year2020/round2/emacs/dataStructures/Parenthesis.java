package codejam.year2020.round2.emacs.dataStructures;

import utils.disjointSet.DisjointSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Parenthesis implements Comparable<Parenthesis> {

    public Parenthesis parent = null;
    public List<Parenthesis> children = new ArrayList<>();
    public TreeSet<Parenthesis> childrenTree = new TreeSet<>();
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

    public int enterTime = -1;
    public int subtreeCnt = 1;

    public boolean hasHeavyChild = false;
    public Parenthesis heavyParent = null;

    public PathDecompose path = null;
    public int indexInPath;

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

        this.openAbsPosition = parArr.size();
        parArr.add(this);

        for (Parenthesis child : children) {
            child.fillParenthesisArray(parArr);
        }

        this.closeAbsPosition = parArr.size();
        parArr.add(this);

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

    public int dfs(int time) {
        this.enterTime = time;
        time++;
        for (Parenthesis child : children) {
            time = child.dfs(time);
            this.subtreeCnt += child.subtreeCnt;
        }
        return time;
    }

    public void fillTreeSet() {
        childrenTree.addAll(children);
        for (Parenthesis child : children) {
            child.fillTreeSet();
        }
    }

    public void markHeavyEdges(List<Parenthesis> noHeavyChildNodes) {
        for (Parenthesis child : children) {
            if (2 * child.subtreeCnt >= this.subtreeCnt) {
                this.hasHeavyChild = true;
                child.heavyParent = this;
            }
            child.markHeavyEdges(noHeavyChildNodes);
        }

        if (!this.hasHeavyChild) {
            noHeavyChildNodes.add(this);
        }
    }

    public void buildHeavyLightDecomposition(PathDecompose path) {
        if (heavyParent == null) {
            path.parentPathNode = this.parent;
        } else {
            heavyParent.buildHeavyLightDecomposition(path);
        }

        this.path = path;
        this.indexInPath = path.nodes.size();
        path.nodes.add(this);
    }

    public static List<PathDecompose> buildHeavyLightDecomposition(List<Parenthesis> noHeavyChildNodes) {
        List<PathDecompose> paths = new ArrayList<>();
        for (Parenthesis node : noHeavyChildNodes) {
            PathDecompose path = new PathDecompose();
            paths.add(path);
            node.buildHeavyLightDecomposition(path);
        }
        return paths;
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

    private Time mergeOpeningTime(Time fromOpenining1, Time fromOpening2, Time fromClosing2) {
        Time fromOpeningMerged = new Time();

        fromOpeningMerged.opening = Math.min(fromOpenining1.opening + fromOpening2.opening,
                fromOpenining1.closing + fromClosing2.opening);
        fromOpeningMerged.closing = Math.min(fromOpenining1.opening + fromOpening2.closing,
                fromOpenining1.closing + fromClosing2.closing);

        return fromOpeningMerged;
    }

    private Time mergeClosingTime(Time fromClosing1, Time fromOpening2, Time fromClosing2) {
        Time fromClosingMerged = new Time();

        fromClosingMerged.opening = Math.min(fromClosing1.opening + fromOpening2.opening,
                fromClosing1.closing + fromClosing2.opening);
        fromClosingMerged.closing = Math.min(fromClosing1.opening + fromOpening2.closing,
                fromClosing1.closing + fromClosing2.closing);

        return fromClosingMerged;
    }

    public Time countTimeFromOpeningToDescendant(Parenthesis child) {
        Time timeFromOpening = new Time();
        timeFromOpening.opening = Math.min(child.fromParentOpening.opening, this.fromOpenToCloseTiming + child.fromParentClosing.opening);
        timeFromOpening.closing = Math.min(child.fromParentOpening.closing, this.fromOpenToCloseTiming + child.fromParentClosing.closing);
        return timeFromOpening;
    }

    public Time countTimeFromClosingToDescendant(Parenthesis child) {
        Time timeFromClosing = new Time();
        timeFromClosing.opening = Math.min(child.fromParentClosing.opening, this.fromCloseToOpenTiming + child.fromParentOpening.opening);
        timeFromClosing.closing = Math.min(child.fromParentClosing.closing, this.fromCloseToOpenTiming + child.fromParentOpening.closing);
        return timeFromClosing;
    }

    public Time countTimeFromOpeningToAncestor() {
        Time timeFromOpening = new Time();
        timeFromOpening.opening = Math.min(toParentOpening.opening, toParentClosing.opening + parent.fromCloseToOpenTiming);
        timeFromOpening.closing = Math.min(toParentClosing.opening, toParentOpening.opening + parent.fromOpenToCloseTiming);
        return timeFromOpening;
    }

    public Time countTimeFromClosingToAncestor() {
        Time timeFromClosing = new Time();
        timeFromClosing.opening = Math.min(toParentOpening.closing, toParentClosing.closing + parent.fromCloseToOpenTiming);
        timeFromClosing.closing = Math.min(toParentClosing.closing, toParentOpening.closing + parent.fromOpenToCloseTiming);
        return timeFromClosing;
    }

    public ParenthesisDistTime calculateUpGoingTiming(Parenthesis dest, Time fromOpening, Time fromClosing) {
        if (this == dest) {
            return new ParenthesisDistTime(fromOpening, fromClosing);
        }

        Time fromOpeningNew = countTimeFromOpeningToAncestor();
        Time fromClosingNew = countTimeFromClosingToAncestor();

        Time fromOpeningMerged = mergeOpeningTime(fromOpening, fromOpeningNew, fromClosingNew);
        Time fromClosingMerged = mergeClosingTime(fromClosing, fromOpeningNew, fromClosingNew);

        return parent.calculateUpGoingTiming(dest, fromOpeningMerged, fromClosingMerged);
    }

    public ParenthesisDistTime calculateDownGoingTiming(Parenthesis src, Time fromOpening, Time fromClosing) {
        if (this == src) {
            return new ParenthesisDistTime(fromOpening, fromClosing);
        }

        Time fromOpeningNew = parent.countTimeFromOpeningToDescendant(this);
        Time fromClosingNew = parent.countTimeFromClosingToDescendant(this);

        Time fromOpeningMerged = mergeOpeningTime(fromOpeningNew, fromOpening, fromClosing);
        Time fromClosingMerged = mergeClosingTime(fromClosingNew, fromOpening, fromClosing);

        return parent.calculateDownGoingTiming(src, fromOpeningMerged, fromClosingMerged);
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

    /**
     * LCA calculation block
     */
    public void lca(int[] black, Map<Parenthesis, Set<NodesPair>> pairs, DisjointSet dSet, int[] ancestors, Parenthesis[] treeArray) {
        int nodeNum = openAbsPosition;
        dSet.makeSet(nodeNum);
        ancestors[dSet.find(nodeNum)] = nodeNum;

        for (Parenthesis child : children) {
            int childNum = child.openAbsPosition;
            child.lca(black, pairs, dSet, ancestors, treeArray);
            dSet.unite(dSet.find(nodeNum), dSet.find(childNum));
            ancestors[dSet.find(nodeNum)] = nodeNum;
        }
        black[nodeNum] = 1;

        if (null == pairs.get(this)) {
            return;
        }

        for (NodesPair pair : pairs.get(this)) {
            if (black[pair.par1.openAbsPosition] == 1 && black[pair.par2.openAbsPosition] == 1) {
                if (pair.par1 == this) {
                    pair.lca = treeArray[ancestors[dSet.find(pair.par2.openAbsPosition)]];
                } else {
                    pair.lca = treeArray[ancestors[dSet.find(pair.par1.openAbsPosition)]];
                }
            }
        }
    }

    /**
     * Time calculation block
     */
    public ParenthesisDistTime calculateMinTime(Parenthesis dest, Parenthesis lca) {
        if (parent == dest.parent) {
            return new ParenthesisDistTime(parent.countTimeFromInnerOpening(this.positionInParentChildList, dest.positionInParentChildList),
                    parent.countTimeFromInnerClosing(this.positionInParentChildList, dest.positionInParentChildList));
        } else {
            if (lca.equals(this)) {
                return dest.calculateDownGoingTiming(this, new Time(0, dest.fromOpenToCloseTiming),
                        new Time(dest.fromCloseToOpenTiming, 0));
            } else if (lca.equals(dest)) {
                return calculateUpGoingTiming(dest, new Time(0, fromOpenToCloseTiming),
                        new Time(fromCloseToOpenTiming, 0));
            } else {
                Parenthesis sameLevelStart = lca.childrenTree.floor(this);
                ParenthesisDistTime time1 = calculateUpGoingTiming(sameLevelStart, new Time(0, fromOpenToCloseTiming),
                        new Time(fromCloseToOpenTiming, 0));

                Parenthesis sameLevelEnd = lca.childrenTree.floor(dest);
                ParenthesisDistTime time2 = new ParenthesisDistTime(lca.countTimeFromInnerOpening(sameLevelStart.positionInParentChildList, sameLevelEnd.positionInParentChildList),
                        lca.countTimeFromInnerClosing(sameLevelStart.positionInParentChildList, sameLevelEnd.positionInParentChildList));

                ParenthesisDistTime time3 = new ParenthesisDistTime(
                        mergeOpeningTime(time1.timeFromOpening, time2.timeFromOpening, time2.timeFromClosing),
                        mergeClosingTime(time1.timeFromClosing, time2.timeFromOpening, time2.timeFromClosing)
                );

                ParenthesisDistTime time4 = dest.calculateDownGoingTiming(sameLevelEnd, new Time(0, dest.fromOpenToCloseTiming),
                        new Time(dest.fromCloseToOpenTiming, 0));

                return new ParenthesisDistTime(
                        mergeOpeningTime(time3.timeFromOpening, time4.timeFromOpening, time4.timeFromClosing),
                        mergeClosingTime(time3.timeFromClosing, time4.timeFromOpening, time4.timeFromClosing)
                );
            }
        }
    }

    @Override
    public String toString() {
        return "(" + children.stream().map(Parenthesis::toString).collect(Collectors.joining()) + ")";
    }

    @Override
    public int compareTo(Parenthesis o) {
        return Integer.compare(enterTime, o.enterTime);
    }
}
