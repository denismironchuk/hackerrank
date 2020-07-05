package codejam.year2020.round2.emacs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Emacs {
    private static class MoveTime {
        public long toLeft = Long.MAX_VALUE;
        public long toRight = Long.MAX_VALUE;
        public long teleport = Long.MAX_VALUE;
    }

    public static class Time {
        private static final int INST_CNT = 500;
        private static Time[] instances = new Time[INST_CNT];
        private static int currentInstanceIndex = 0;

        static {
            for (int i = 0; i < INST_CNT; i++) {
                instances[i] = new Time();
            }
        }

        public static Time getNewInstance() {
            return instances[currentInstanceIndex++];
        }

        public static Time getNewInstance(long opening, long closing) {
            Time inst = instances[currentInstanceIndex++];
            inst.opening = opening;
            inst.closing = closing;
            return inst;
        }

        public static void reset() {
            currentInstanceIndex = 0;
        }

        public long opening = 0;
        public long closing = 0;

        public Time() {
        }

        public Time(long opening, long closing) {
            this.opening = opening;
            this.closing = closing;
        }

        public static Time mergeOpeningTime(Time fromOpenining1, Time fromOpening2, Time fromClosing2) {
            Time fromOpeningMerged = new Time();

            fromOpeningMerged.opening = Math.min(fromOpenining1.opening + fromOpening2.opening,
                    fromOpenining1.closing + fromClosing2.opening);
            fromOpeningMerged.closing = Math.min(fromOpenining1.opening + fromOpening2.closing,
                    fromOpenining1.closing + fromClosing2.closing);

            return fromOpeningMerged;
        }

        public static Time mergeClosingTime(Time fromClosing1, Time fromOpening2,Time fromClosing2) {
            Time fromClosingMerged = new Time();

            fromClosingMerged.opening = Math.min(fromClosing1.opening + fromOpening2.opening,
                    fromClosing1.closing + fromClosing2.opening);
            fromClosingMerged.closing = Math.min(fromClosing1.opening + fromOpening2.closing,
                    fromClosing1.closing + fromClosing2.closing);

            return fromClosingMerged;
        }

        public static Time mergeOpeningTimeReuse(Time fromOpenining1, Time fromOpening2, Time fromClosing2) {
            Time fromOpeningMerged = Time.getNewInstance();

            fromOpeningMerged.opening = Math.min(fromOpenining1.opening + fromOpening2.opening,
                    fromOpenining1.closing + fromClosing2.opening);
            fromOpeningMerged.closing = Math.min(fromOpenining1.opening + fromOpening2.closing,
                    fromOpenining1.closing + fromClosing2.closing);

            return fromOpeningMerged;
        }

        public static Time mergeClosingTimeReuse(Time fromClosing1, Time fromOpening2, Time fromClosing2) {
            Time fromClosingMerged = Time.getNewInstance();

            fromClosingMerged.opening = Math.min(fromClosing1.opening + fromOpening2.opening,
                    fromClosing1.closing + fromClosing2.opening);
            fromClosingMerged.closing = Math.min(fromClosing1.opening + fromOpening2.closing,
                    fromClosing1.closing + fromClosing2.closing);

            return fromClosingMerged;
        }
    }

    private static class ParenthesisDistTime {

        private static final int INST_CNT = 500;
        private static ParenthesisDistTime[] instances = new ParenthesisDistTime[INST_CNT];
        private static int currentInstanceIndex = 0;

        static {
            for (int i = 0; i < INST_CNT; i++) {
                instances[i] = new ParenthesisDistTime();
            }
        }

        public static ParenthesisDistTime getNewInstance(Time timeFromOpening, Time timeFromClosing) {
            ParenthesisDistTime inst = instances[currentInstanceIndex++];
            inst.timeFromOpening = timeFromOpening;
            inst.timeFromClosing = timeFromClosing;
            return inst;
        }

        public static void reset() {
            currentInstanceIndex = 0;
        }

        public Time timeFromOpening;
        public Time timeFromClosing;

        public ParenthesisDistTime() {
        }

        public ParenthesisDistTime(Time timeFromOpening, Time timeFromClosing) {
            this.timeFromOpening = timeFromOpening;
            this.timeFromClosing = timeFromClosing;
        }

        public long getTime(boolean startOpening, boolean endOpening) {
            if (startOpening) {
                if (endOpening) {
                    return timeFromOpening.opening;
                } else {
                    return timeFromOpening.closing;
                }
            } else {
                if (endOpening) {
                    return timeFromClosing.opening;
                } else {
                    return timeFromClosing.closing;
                }
            }
        }
    }

    private static class Parenthesis implements Comparable<Parenthesis> {

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

        public Parenthesis(String p, Parenthesis[] tree) {
            this.openTime.teleport = Long.MAX_VALUE;
            this.closeTime.teleport = Long.MAX_VALUE;
            tree[0] = this;
            this.openAbsPosition = 0;

            Stack<Parenthesis> stack = new Stack<>();
            stack.push(this);

            for (int i = 0; i < p.length(); i++) {
                Parenthesis current = stack.peek();

                if (p.charAt(i) == '(') {
                    tree[i + 1] = new Parenthesis(current);
                    tree[i + 1].openAbsPosition = i + 1;
                    stack.push(tree[i + 1]);
                } else {
                    tree[i + 1] = current;
                    tree[i + 1].closeAbsPosition = i + 1;
                    stack.pop();
                }
            }
            tree[p.length() + 1] = this;
            this.closeAbsPosition = p.length() + 1;
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
                if (this.parent != null) {
                    path.nodes.add(this.parent);
                }
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
                path.initSegmentTrees();
            }
            return paths;
        }

        /**
         * Calculate distance between two children of the same parent on the same level.
         * From source opening to dist opening and closing
         */
        public Time countTimeFromInnerOpening(int srcIndex, int destIndex) {
            Time res = Time.getNewInstance();

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
            Time res = Time.getNewInstance();

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

        public ParenthesisDistTime calculateUpGoingTiming(Parenthesis dest, Time fromOpening, Time fromClosing) {
            PathDecompose currentPath = this.path;
            if (dest.path == currentPath) {
                return currentPath.calculateUpGoingTiming(this.indexInPath, dest.indexInPath, fromOpening, fromClosing);
            } else {
                ParenthesisDistTime time = currentPath.calculateUpGoingTiming(this.indexInPath, 0, fromOpening, fromClosing);
                return currentPath.parentPathNode.calculateUpGoingTiming(dest, time.timeFromOpening, time.timeFromClosing);
            }
        }

        public ParenthesisDistTime calculateDownGoingTiming(Parenthesis src, Time fromOpening, Time fromClosing) {
            PathDecompose currentPath = this.path;
            if (src.path == currentPath) {
                return currentPath.calculateDownGoingTiming(src.indexInPath, this.indexInPath, fromOpening, fromClosing);
            } else {
                ParenthesisDistTime time = currentPath.calculateDownGoingTiming(0, this.indexInPath, fromOpening, fromClosing);
                return currentPath.parentPathNode.calculateDownGoingTiming(src, time.timeFromOpening, time.timeFromClosing);
            }
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
            Time.reset();
            ParenthesisDistTime.reset();

            if (parent == dest.parent) {
                return ParenthesisDistTime.getNewInstance(parent.countTimeFromInnerOpening(this.positionInParentChildList, dest.positionInParentChildList),
                        parent.countTimeFromInnerClosing(this.positionInParentChildList, dest.positionInParentChildList));
            } else {
                if (lca.equals(this)) {
                    return dest.calculateDownGoingTiming(this, Time.getNewInstance(0, dest.fromOpenToCloseTiming),
                            Time.getNewInstance(dest.fromCloseToOpenTiming, 0));
                } else if (lca.equals(dest)) {
                    return calculateUpGoingTiming(dest, Time.getNewInstance(0, fromOpenToCloseTiming),
                            Time.getNewInstance(fromCloseToOpenTiming, 0));
                } else {
                    Parenthesis sameLevelStart = lca.childrenTree.floor(this);
                    ParenthesisDistTime time1 = calculateUpGoingTiming(sameLevelStart, Time.getNewInstance(0, fromOpenToCloseTiming),
                            Time.getNewInstance(fromCloseToOpenTiming, 0));

                    Parenthesis sameLevelEnd = lca.childrenTree.floor(dest);
                    ParenthesisDistTime time2 = ParenthesisDistTime.getNewInstance(lca.countTimeFromInnerOpening(sameLevelStart.positionInParentChildList, sameLevelEnd.positionInParentChildList),
                            lca.countTimeFromInnerClosing(sameLevelStart.positionInParentChildList, sameLevelEnd.positionInParentChildList));

                    ParenthesisDistTime time3 = ParenthesisDistTime.getNewInstance(
                            Time.mergeOpeningTimeReuse(time1.timeFromOpening, time2.timeFromOpening, time2.timeFromClosing),
                            Time.mergeOpeningTimeReuse(time1.timeFromClosing, time2.timeFromOpening, time2.timeFromClosing)
                    );

                    ParenthesisDistTime time4 = dest.calculateDownGoingTiming(sameLevelEnd, Time.getNewInstance(0, dest.fromOpenToCloseTiming),
                            Time.getNewInstance(dest.fromCloseToOpenTiming, 0));

                    return ParenthesisDistTime.getNewInstance(
                            Time.mergeOpeningTimeReuse(time3.timeFromOpening, time4.timeFromOpening, time4.timeFromClosing),
                            Time.mergeOpeningTimeReuse(time3.timeFromClosing, time4.timeFromOpening, time4.timeFromClosing)
                    );
                }
            }
        }

        @Override
        public int compareTo(Parenthesis o) {
            return Integer.compare(enterTime, o.enterTime);
        }
    }

    private static class PathDecompose {
        public List<Parenthesis> nodes = new ArrayList<>();
        public Parenthesis parentPathNode = null;
        private ParenthesisDistTime[] downGoingSegmentTree;
        private ParenthesisDistTime[] upGoingSegmentTree;

        private Time countTimeFromOpeningToDescendant(Parenthesis parent, Parenthesis child) {
            Time timeFromOpening = new Time();
            timeFromOpening.opening = Math.min(child.fromParentOpening.opening, parent.fromOpenToCloseTiming + child.fromParentClosing.opening);
            timeFromOpening.closing = Math.min(child.fromParentOpening.closing, parent.fromOpenToCloseTiming + child.fromParentClosing.closing);
            return timeFromOpening;
        }

        private Time countTimeFromClosingToDescendant(Parenthesis parent, Parenthesis child) {
            Time timeFromClosing = new Time();
            timeFromClosing.opening = Math.min(child.fromParentClosing.opening, parent.fromCloseToOpenTiming + child.fromParentOpening.opening);
            timeFromClosing.closing = Math.min(child.fromParentClosing.closing, parent.fromCloseToOpenTiming + child.fromParentOpening.closing);
            return timeFromClosing;
        }

        private Time countTimeFromOpeningToAncestor(Parenthesis parent, Parenthesis child) {
            Time timeFromOpening = new Time();
            timeFromOpening.opening = Math.min(child.toParentOpening.opening, child.toParentClosing.opening + parent.fromCloseToOpenTiming);
            timeFromOpening.closing = Math.min(child.toParentClosing.opening, child.toParentOpening.opening + parent.fromOpenToCloseTiming);
            return timeFromOpening;
        }

        private Time countTimeFromClosingToAncestor(Parenthesis parent, Parenthesis child) {
            Time timeFromClosing = new Time();
            timeFromClosing.opening = Math.min(child.toParentOpening.closing, child.toParentClosing.closing + parent.fromCloseToOpenTiming);
            timeFromClosing.closing = Math.min(child.toParentClosing.closing, child.toParentOpening.closing + parent.fromOpenToCloseTiming);
            return timeFromClosing;
        }

        private ParenthesisDistTime calculateUpGoingTiming(Parenthesis src, Parenthesis dest, Time fromOpening, Time fromClosing) {
            if (src == dest) {
                return new ParenthesisDistTime(fromOpening, fromClosing);
            }

            Time fromOpeningNew = countTimeFromOpeningToAncestor(src.parent, src);
            Time fromClosingNew = countTimeFromClosingToAncestor(src.parent, src);

            Time fromOpeningMerged = Time.mergeOpeningTime(fromOpening, fromOpeningNew, fromClosingNew);
            Time fromClosingMerged = Time.mergeClosingTime(fromClosing, fromOpeningNew, fromClosingNew);

            return calculateUpGoingTiming(src.parent, dest, fromOpeningMerged, fromClosingMerged);
        }

        private ParenthesisDistTime calculateDownGoingTiming(Parenthesis src, Parenthesis dest, Time fromOpening, Time fromClosing) {
            if (dest == src) {
                return new ParenthesisDistTime(fromOpening, fromClosing);
            }

            Time fromOpeningNew = countTimeFromOpeningToDescendant(dest.parent, dest);
            Time fromClosingNew = countTimeFromClosingToDescendant(dest.parent, dest);

            Time fromOpeningMerged = Time.mergeOpeningTime(fromOpeningNew, fromOpening, fromClosing);
            Time fromClosingMerged = Time.mergeClosingTime(fromClosingNew, fromOpening, fromClosing);

            return calculateDownGoingTiming(src, dest.parent, fromOpeningMerged, fromClosingMerged);
        }

        public ParenthesisDistTime calculateUpGoingTiming(int srcIndex, int destIndex, Time fromOpening, Time fromClosing) {
            //return calculateUpGoingTiming(nodes.get(srcIndex), nodes.get(destIndex), fromOpening, fromClosing);
            if (srcIndex == destIndex) {
                return ParenthesisDistTime.getNewInstance(fromOpening, fromClosing);
            }

            ParenthesisDistTime timeNew = calculateUpGoingTimingInner(1, 1, nodes.size() - 1, destIndex + 1, srcIndex);
            Time fromOpeningMerged = Time.mergeOpeningTimeReuse(fromOpening, timeNew.timeFromOpening, timeNew.timeFromClosing);
            Time fromClosingMerged = Time.mergeClosingTimeReuse(fromClosing, timeNew.timeFromOpening, timeNew.timeFromClosing);
            return ParenthesisDistTime.getNewInstance(fromOpeningMerged, fromClosingMerged);
        }

        public ParenthesisDistTime calculateDownGoingTiming(int srcIndex, int destIndex, Time fromOpening, Time fromClosing) {
            //return calculateDownGoingTiming(nodes.get(srcIndex), nodes.get(destIndex), fromOpening, fromClosing);
            if (srcIndex == destIndex) {
                return ParenthesisDistTime.getNewInstance(fromOpening, fromClosing);
            }

            ParenthesisDistTime timeNew = calculateDownGoingTimingInner(1, 0, nodes.size() - 2, srcIndex, destIndex - 1);
            Time fromOpeningMerged = Time.mergeOpeningTimeReuse(timeNew.timeFromOpening, fromOpening, fromClosing);
            Time fromClosingMerged = Time.mergeClosingTimeReuse(timeNew.timeFromClosing, fromOpening, fromClosing);
            return ParenthesisDistTime.getNewInstance(fromOpeningMerged, fromClosingMerged);
        }

        private ParenthesisDistTime calculateDownGoingTimingInner(int p, int intervalStart, int intervalEnd, int start, int end) {
            if (start > end) {
                return null;
            }

            if (start == intervalStart && end == intervalEnd) {
                return downGoingSegmentTree[p];
            }

            int middle = (intervalStart + intervalEnd) / 2;
            ParenthesisDistTime time1 = calculateDownGoingTimingInner(p * 2, intervalStart, middle, start, Math.min(middle, end));
            ParenthesisDistTime time2 = calculateDownGoingTimingInner(p * 2 + 1, middle + 1, intervalEnd, Math.max(middle + 1, start), end);

            if (time1 != null && time2 != null) {
                Time fromOpeningMerged = Time.mergeOpeningTimeReuse(time1.timeFromOpening, time2.timeFromOpening, time2.timeFromClosing);
                Time fromClosingMerged = Time.mergeClosingTimeReuse(time1.timeFromClosing, time2.timeFromOpening, time2.timeFromClosing);

                return ParenthesisDistTime.getNewInstance(fromOpeningMerged, fromClosingMerged);
            } else {
                return time1 != null ? time1 : time2;
            }
        }

        private ParenthesisDistTime calculateUpGoingTimingInner(int p, int intervalStart, int intervalEnd, int start, int end) {
            if (start > end) {
                return null;
            }

            if (start == intervalStart && end == intervalEnd) {
                return upGoingSegmentTree[p];
            }

            int middle = 1 + (intervalStart + intervalEnd) / 2;
            ParenthesisDistTime time1 = calculateUpGoingTimingInner(p * 2, intervalStart, middle - 1, start, Math.min(middle - 1, end));
            ParenthesisDistTime time2 = calculateUpGoingTimingInner(p * 2 + 1, middle, intervalEnd, Math.max(middle, start), end);

            if (time1 != null && time2 != null) {
                Time fromOpeningMerged = Time.mergeOpeningTimeReuse(time2.timeFromOpening, time1.timeFromOpening, time1.timeFromClosing);
                Time fromClosingMerged = Time.mergeClosingTimeReuse(time2.timeFromClosing, time1.timeFromOpening, time1.timeFromClosing);

                return ParenthesisDistTime.getNewInstance(fromOpeningMerged, fromClosingMerged);
            } else {
                return time1 != null ? time1 : time2;
            }
        }

        public void initSegmentTrees() {
            downGoingSegmentTree = new ParenthesisDistTime[5 * nodes.size()];
            upGoingSegmentTree = new ParenthesisDistTime[5 * nodes.size()];

            if (nodes.size() != 1) {
                buildDownGoingSegmentTree(1, 0, nodes.size() - 2);
                buildUpGoingSegmentTree(1, 1, nodes.size() - 1);
            }
        }

        private void buildDownGoingSegmentTree(int p, int start, int end) {
            if (start == end) {
                Parenthesis src = nodes.get(start);
                Parenthesis dest = nodes.get(start + 1);
                downGoingSegmentTree[p] = calculateDownGoingTiming(src, dest, new Time(0, dest.fromOpenToCloseTiming), new Time(dest.fromCloseToOpenTiming, 0));
                return;
            }

            int middle = (start + end) / 2;
            buildDownGoingSegmentTree(p * 2, start, middle);
            buildDownGoingSegmentTree(p * 2 + 1, middle + 1, end);

            ParenthesisDistTime time1 = downGoingSegmentTree[p * 2];
            ParenthesisDistTime time2 = downGoingSegmentTree[p * 2 + 1];

            Time fromOpeningMerged = Time.mergeOpeningTime(time1.timeFromOpening, time2.timeFromOpening, time2.timeFromClosing);
            Time fromClosingMerged = Time.mergeClosingTime(time1.timeFromClosing, time2.timeFromOpening, time2.timeFromClosing);

            downGoingSegmentTree[p] = new ParenthesisDistTime(fromOpeningMerged, fromClosingMerged);
        }

        private void buildUpGoingSegmentTree(int p, int start, int end) {
            if (start == end) {
                Parenthesis src = nodes.get(start);
                Parenthesis dest = nodes.get(start - 1);
                upGoingSegmentTree[p] = calculateUpGoingTiming(src, dest, new Time(0, src.fromOpenToCloseTiming), new Time(src.fromCloseToOpenTiming, 0));
                return;
            }

            int middle = 1 + (start + end) / 2;
            buildUpGoingSegmentTree(p * 2, start, middle - 1);
            buildUpGoingSegmentTree(p * 2 + 1, middle, end);

            ParenthesisDistTime time1 = upGoingSegmentTree[p * 2];
            ParenthesisDistTime time2 = upGoingSegmentTree[p * 2 + 1];

            Time fromOpeningMerged = Time.mergeOpeningTime(time2.timeFromOpening, time1.timeFromOpening, time1.timeFromClosing);
            Time fromClosingMerged = Time.mergeClosingTime(time2.timeFromClosing, time1.timeFromOpening, time1.timeFromClosing);

            upGoingSegmentTree[p] = new ParenthesisDistTime(fromOpeningMerged, fromClosingMerged);
        }
    }

    private static class NodesPair {
        public Parenthesis par1;
        public boolean par1Open = false;
        public Parenthesis par2;
        public boolean par2Open = false;
        public Parenthesis lca = null;

        public NodesPair(Parenthesis par1, boolean par1Open, Parenthesis par2, boolean par2Open) {
            this.par1 = par1;
            this.par1Open = par1Open;
            this.par2 = par2;
            this.par2Open = par2Open;
        }
    }

    public static void main(String[] aggs) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            Parenthesis[] tree = new Parenthesis[110000];

            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int k = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                String p = br.readLine();
                Parenthesis root = new Parenthesis(p, tree);

                StringTokenizer leftTkn = new StringTokenizer(br.readLine());
                StringTokenizer rightTkn = new StringTokenizer(br.readLine());
                StringTokenizer teleportTkn = new StringTokenizer(br.readLine());

                for (int i = 0; i < k; i++) {
                    MoveTime moveTime = p.charAt(i) == '(' ? tree[i + 1].openTime : tree[i + 1].closeTime;
                    moveTime.toLeft = Long.parseLong(leftTkn.nextToken());
                    moveTime.toRight = Long.parseLong(rightTkn.nextToken());
                    moveTime.teleport = Long.parseLong(teleportTkn.nextToken());
                }

                root.calculateTiming();
                List<Parenthesis> noHeavyChildNodes = new ArrayList();
                root.markHeavyEdges(noHeavyChildNodes);
                Parenthesis.buildHeavyLightDecomposition(noHeavyChildNodes);

                StringTokenizer sTkn = new StringTokenizer(br.readLine());
                StringTokenizer eTkn = new StringTokenizer(br.readLine());

                List<NodesPair> pairs = new ArrayList<>();

                for (int i = 0; i < q; i++) {
                    int s = Integer.parseInt(sTkn.nextToken());
                    int e = Integer.parseInt(eTkn.nextToken());

                    pairs.add(new NodesPair(tree[s], p.charAt(s - 1) == '(', tree[e], p.charAt(e - 1) == '('));
                }

                Map<Parenthesis, Set<NodesPair>> pairsGrouped = new HashMap<>();

                for (NodesPair pair : pairs) {
                    Set<NodesPair> list1 = pairsGrouped.get(pair.par1);
                    if (list1 == null) {
                        list1 = new HashSet<>();
                    }
                    list1.add(pair);
                    pairsGrouped.put(pair.par1, list1);

                    Set<NodesPair> list2 = pairsGrouped.get(pair.par2);
                    if (list2 == null) {
                        list2 = new HashSet<>();
                    }
                    list2.add(pair);
                    pairsGrouped.put(pair.par2, list2);
                }

                root.lca(new int[k + 2], pairsGrouped, new DisjointSet(k + 2), new int[k + 2], tree);
                long res = 0;

                for (NodesPair pair : pairs) {
                    Parenthesis par1 = pair.par1;
                    Parenthesis par2 = pair.par2;
                    Parenthesis lca = pair.lca;

                    if (par1 == par2) {
                        if (pair.par1Open) {
                            res += par1.fromOpenToCloseTiming;
                        } else {
                            res += par1.fromCloseToOpenTiming;
                        }
                    } else {
                        ParenthesisDistTime time = par1.calculateMinTime(par2, lca);
                        res += time.getTime(pair.par1Open, pair.par2Open);
                    }
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static class DisjointSet {
        private int[] parents;
        private int[] rank;

        public DisjointSet(int n) {
            parents = new int[n];
            rank = new int[n];
        }

        public void makeSet(int x) {
            parents[x] = x;
        }

        public int find(int x) {
            if (parents[x] == x) {
                return x;
            } else {
                parents[x] = find(parents[x]);
                return parents[x];
            }
        }

        public void unite(int x, int y) {
            int px = find(x);
            int py = find(y);

            if (rank[px] > rank[py]) {
                parents[py] = px;
            } else {
                parents[px] = py;
                if (rank[px] == rank[py]) {
                    rank[py]++;
                }
            }
        }
    }
}
