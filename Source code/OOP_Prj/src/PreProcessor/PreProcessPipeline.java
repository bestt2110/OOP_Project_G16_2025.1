package PreProcessor;

import Model.Comment;
import Model.Post;
import java.util.ArrayList;
import java.util.List;

public class PreProcessPipeline {

    private final List<PreProcessor> processors;

    public PreProcessPipeline() {
        this.processors = new ArrayList<>();
    }

    public void addProcessor(PreProcessor processor) {
        this.processors.add(processor);
    }

    public void execute(Post post) {
        String currentContent = post.getRawContent();

        for (PreProcessor processor : processors) {
            currentContent = processor.process(currentContent);
        }

        post.setCleanContent(currentContent);
    }
    public void execute(Comment cmt) {
        String currentContent = cmt.getRawContent();

        for (PreProcessor processor : processors) {
            currentContent = processor.process(currentContent);
        }

        cmt.setCleanContent(currentContent);
    }
}