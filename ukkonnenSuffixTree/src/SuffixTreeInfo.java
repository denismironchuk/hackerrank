/**
 * Created by Denis_Mironchuk on 6/14/2018.
 */
public class SuffixTreeInfo {
    private String fullString;
    private int vertCnt = 0;
    private Node lastVisitedNode;
    private Node currentPhaseAddedInnerNode;
    private Node previousPhaseAddedInnerNode;
    private int phaseLastCharIndex;
    private Node root;
    private int suffixNum = 0;

    public SuffixTreeInfo(final String fullString) {
        this.fullString = fullString;
    }

    public String getFullString() {
        return fullString;
    }

    public void setFullString(final String fullString) {
        this.fullString = fullString;
    }

    public int getVertCnt() {
        int cntToReturn = vertCnt;
        vertCnt++;
        return cntToReturn;
    }

    public int getSuffixNum() {
        int suffixNumToReturn = suffixNum;
        suffixNum++;
        return suffixNumToReturn;
    }

    public Node getLastVisitedNode() {
        return lastVisitedNode;
    }

    public void setLastVisitedNode(final Node lastVisitedNode) {
        this.lastVisitedNode = lastVisitedNode;
    }

    public Node getCurrentPhaseAddedInnerNode() {
        return currentPhaseAddedInnerNode;
    }

    public void setCurrentPhaseAddedInnerNode(final Node currentPhaseAddedInnerNode) {
        this.currentPhaseAddedInnerNode = currentPhaseAddedInnerNode;
    }

    public Node getPreviousPhaseAddedInnerNode() {
        return previousPhaseAddedInnerNode;
    }

    public void setPreviousPhaseAddedInnerNode(final Node previousPhaseAddedInnerNode) {
        this.previousPhaseAddedInnerNode = previousPhaseAddedInnerNode;
    }

    public int getPhaseLastCharIndex() {
        return phaseLastCharIndex;
    }

    public void setPhaseLastCharIndex(final int phaseLastCharIndex) {
        this.phaseLastCharIndex = phaseLastCharIndex;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(final Node root) {
        this.root = root;
    }
}
