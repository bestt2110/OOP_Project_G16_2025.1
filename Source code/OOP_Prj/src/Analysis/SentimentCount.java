package Analysis;

public class SentimentCount {
    public int positive;
    public int negative;

    public SentimentCount() {}
    public SentimentCount(int positive, int negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }

    @Override
    public String toString() {
        return "{positive=" + positive + ", negative=" + negative + "}";
    }

}

