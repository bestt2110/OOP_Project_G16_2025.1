package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Kế thừa từ SocialItem
public class Post extends SocialItem {
    
    private int cmtCount;
    private final List<Comment> comments;

    public Post(String id, String rawContent, Date publishedAt, int likeCount, int cmtCount) {
        super(id, rawContent, publishedAt, likeCount);
        
        this.cmtCount = cmtCount;
        this.comments = new ArrayList<>();
    }


    public int getCmt() {
        return cmtCount;
    }

    public List<Comment> getComments() {
        return comments;
    }
    
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}