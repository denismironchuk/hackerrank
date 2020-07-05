package codejam.year2020.round2.emacs.dataStructures;

public class ParenthesisDistTime {
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
