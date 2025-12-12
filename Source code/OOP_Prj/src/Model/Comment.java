package Model;

import java.time.LocalDateTime;
import java.util.Date;

public class Comment {
	
	private String postID;
	private String id;
	private String rawContent;
	private Date timestamp;
	private String cleanContent;
	private int likeCount;
	
	private String sentimentPoint;
	public Comment(String postID, String id, String rawComment, Date timestamp, int likeCount) {
		super();
		this.postID = postID;
		this.id = id;
		this.rawContent = rawComment;
		this.timestamp = timestamp;
		this.likeCount = likeCount;
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
	public Date getTimestamp() {
		return timestamp;
	}
	public int getLikeCount() {
		return likeCount;
	}

}