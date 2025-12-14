package Model;

import java.util.Date;

// Kế thừa từ SocialItem
public class Comment extends SocialItem {
    
    private String postID;

    public Comment(String postID, String id, String rawComment, Date timestamp, int likeCount) {
        super(id, rawComment, timestamp, likeCount);       
        this.postID = postID;
    }

    public String getPostID() {
        return postID;
    }

}