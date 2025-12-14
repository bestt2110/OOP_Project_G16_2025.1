package Model;

import java.util.Date;

public abstract class SocialItem {
    
    protected String id;
    protected String rawContent;
    protected String cleanContent; 
    protected Date timestamp;
    protected int likeCount;

    public SocialItem(String id, String rawContent, Date timestamp, int likeCount) {
        this.id = id;
        this.rawContent = rawContent;
        this.timestamp = timestamp;
        this.likeCount = likeCount;
    }

    
    public String getId() {
        return id;
    }

    public String getRawContent() {
        return rawContent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getLikeCount() {
        return likeCount;
    }
   
    public int getLike() {
        return likeCount;
    }

    public String getCleanContent() {
        return cleanContent;
    }

    public void setCleanContent(String cleanContent) {
        this.cleanContent = cleanContent;
    }
}