package codejam.year2020.round2.emacs.dataStructures;

public class Time {
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

    public static Time mergeClosingTime(Time fromClosing1, Time fromOpening2, Time fromClosing2) {
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
