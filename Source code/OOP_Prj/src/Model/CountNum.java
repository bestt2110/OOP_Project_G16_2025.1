package Model;

public class CountNum extends AnalysisResult {
    private int count;

    public CountNum(int count) {
        super();
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }
}