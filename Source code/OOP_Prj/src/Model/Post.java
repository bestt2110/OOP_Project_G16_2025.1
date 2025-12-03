package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Model.PostSource;

public class Post {
	
	private String id;
	private String rawContent;
	private LocalDateTime timestamp;
	private int cmt;
	private PostSource source;
	private final List<Comment> comments;
	
	private String cleanContent;
	private String sentimentLabel; // Renamed for consistency with APIClient (was sentimentPoint)
	private String damageTypeLabel; // Renamed for consistency with APIClient (was damageType)
    private String reliefTypeLabel; // NEW FIELD: Required for Problem 3 (Relief Item Classification)

	public Post(String id, String rawContent, LocalDateTime timestamp, int cmt) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.cmt = cmt;
		this.comments = new ArrayList<>();
	}

	public Post(String id, String rawContent, LocalDateTime timestamp, int cmt, PostSource source) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.cmt = cmt;
		this.comments = new ArrayList<>();
		this.setSource(source);
	}

	public String getCleanContent() {
		return cleanContent;
	}

	public void setCleanContent(String cleanContent) {
		this.cleanContent = cleanContent;
	}


	public String getSentimentLabel() { 
		return sentimentLabel;
	}

	public void setSentimentLabel(String sentimentLabel) { 
		this.sentimentLabel = sentimentLabel;
	}

	public String getDamageTypeLabel() { 
		return damageTypeLabel;
	}

	public void setDamageTypeLabel(String damageTypeLabel) { 
		this.damageTypeLabel = damageTypeLabel;
	}
    
    public String getReliefTypeLabel() {
        return reliefTypeLabel;
    }

    public void setReliefTypeLabel(String reliefTypeLabel) {
        this.reliefTypeLabel = reliefTypeLabel;
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

	public PostSource getSource() {
		return source;
	}

	public void setSource(PostSource source) {
		this.source = source;
	}

	public int getCmt() {
		return cmt;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
}	