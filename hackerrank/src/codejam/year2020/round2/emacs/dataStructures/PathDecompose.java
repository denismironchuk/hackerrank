package codejam.year2020.round2.emacs.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class PathDecompose {
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
        Time fromClosingMerged = Time.mergeOpeningTimeReuse(fromClosing, timeNew.timeFromOpening, timeNew.timeFromClosing);
        return ParenthesisDistTime.getNewInstance(fromOpeningMerged, fromClosingMerged);
    }

    public ParenthesisDistTime calculateDownGoingTiming(int srcIndex, int destIndex, Time fromOpening, Time fromClosing) {
        //return calculateDownGoingTiming(nodes.get(srcIndex), nodes.get(destIndex), fromOpening, fromClosing);
        if (srcIndex == destIndex) {
            return ParenthesisDistTime.getNewInstance(fromOpening, fromClosing);
        }

        ParenthesisDistTime timeNew = calculateDownGoingTimingInner(1, 0, nodes.size() - 2, srcIndex, destIndex - 1);
        Time fromOpeningMerged = Time.mergeOpeningTimeReuse(timeNew.timeFromOpening, fromOpening, fromClosing);
        Time fromClosingMerged = Time.mergeOpeningTimeReuse(timeNew.timeFromClosing, fromOpening, fromClosing);
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
            Time fromClosingMerged = Time.mergeOpeningTimeReuse(time1.timeFromClosing, time2.timeFromOpening, time2.timeFromClosing);

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
            Time fromClosingMerged = Time.mergeOpeningTimeReuse(time2.timeFromClosing, time1.timeFromOpening, time1.timeFromClosing);

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
