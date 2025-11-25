package PreProcessor;

public class LowerCaseProcessor implements PreProcessor {

    @Override
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        return content.toLowerCase();
    }
}