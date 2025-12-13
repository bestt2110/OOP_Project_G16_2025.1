package Model;

public class B2Result extends AnalysisResult {
    private int count;

    public B2Result(int count) {
        super();
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    // [THÊM MỚI] Để in ra màn hình dễ đọc hơn
    @Override
    public String toString() {
        return String.valueOf(count);
    }
}