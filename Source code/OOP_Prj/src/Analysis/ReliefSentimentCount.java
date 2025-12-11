package Analysis;

public class ReliefSentimentCount {
    private long positive;
    private long negative;

    public ReliefSentimentCount(long pos, long neg) {
        this.positive = pos;
        this.negative = neg;
    }

    public long getPositive() { return positive; }
    public long getNegative() { return negative; }
    public void incrementPositive() { positive++; }
    public void incrementNegative() { negative++; }

    @Override
    public String toString() {
        return "POS=" + positive + ", NEG=" + negative;
    }
}
