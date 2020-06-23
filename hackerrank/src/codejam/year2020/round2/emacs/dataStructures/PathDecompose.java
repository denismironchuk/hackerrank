package codejam.year2020.round2.emacs.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class PathDecompose {
    public List<Parenthesis> nodes = new ArrayList<>();
    public Parenthesis parentPathNode = null;

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

    public ParenthesisDistTime calculateUpGoingTiming(Parenthesis src, Parenthesis dest, Time fromOpening, Time fromClosing) {
        if (src == dest) {
            return new ParenthesisDistTime(fromOpening, fromClosing);
        }

        Time fromOpeningNew = countTimeFromOpeningToAncestor(src.parent, src);
        Time fromClosingNew = countTimeFromClosingToAncestor(src.parent, src);

        Time fromOpeningMerged = Time.mergeOpeningTime(fromOpening, fromOpeningNew, fromClosingNew);
        Time fromClosingMerged = Time.mergeClosingTime(fromClosing, fromOpeningNew, fromClosingNew);

        return calculateUpGoingTiming(src.parent, dest, fromOpeningMerged, fromClosingMerged);
    }

    public ParenthesisDistTime calculateDownGoingTiming(Parenthesis src, Parenthesis dest, Time fromOpening, Time fromClosing) {
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
        return calculateUpGoingTiming(nodes.get(srcIndex), nodes.get(destIndex), fromOpening, fromClosing);
    }

    public ParenthesisDistTime calculateDownGoingTiming(int srcIndex, int destIndex, Time fromOpening, Time fromClosing) {
        return calculateDownGoingTiming(nodes.get(srcIndex), nodes.get(destIndex), fromOpening, fromClosing);
    }
}
