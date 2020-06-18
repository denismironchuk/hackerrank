package codejam.year2020.round2.emacs.dataStructures;

public class ParenthesisDistTime {
    public Time timeFromOpening;
    public Time timeFromClosing;

    public ParenthesisDistTime() {
    }

    public ParenthesisDistTime(Time timeFromOpening, Time timeFromClosing) {
        this.timeFromOpening = timeFromOpening;
        this.timeFromClosing = timeFromClosing;
    }
}
