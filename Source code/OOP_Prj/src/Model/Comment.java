package Model;

import java.time.LocalDateTime;

public class Comment {
	
	private String postID;
	private String id;
	private String rawContent;
	private LocalDateTime timestamp;
	private String cleanContent;
	
	private String sentimentPoint;
	public Comment(String postID, String id, String rawComment, LocalDateTime timestamp) {
		super();
		this.postID = postID;
		this.id = id;
		this.rawContent = rawComment;
		this.timestamp = timestamp;
	}
	public String getCleanContent() {
		return cleanContent;
	}
	public void setCleanContent(String cleanContent) {
		this.cleanContent = cleanContent;
	}
	public String getSentimentPoint() {
		return sentimentPoint;
	}
	public void setSentimentPoint(String sentimentPoint) {
		this.sentimentPoint = sentimentPoint;
	}
	public String getPostID() {
		return postID;
	}
	public String getId() {
		return id;
	}
	public String getRawContent() {
		return rawContent;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

}