package data.model;

public class PostRecord {

	public PostRecord() {
		// TODO Auto-generated constructor stub
	}
	public String postId;
    public String text; 
    public String publishedAt; 
    public String source;      
    public String authorId;   
    public int engagementCount; 

    public PostRecord(String postId, String text, String publishedAt, String source, String authorId, int engagementCount) {
        this.postId = postId;
        this.text = text;
        this.publishedAt = publishedAt;
        this.source = source;
        this.authorId = authorId;
        this.engagementCount = engagementCount;
    }

}
