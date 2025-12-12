package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
	
	private String id;
	private String rawContent;
	private Date publishedAt;
	private int likeCount;
	private int cmtCount;
	private PostSource source;
	private final List<Comment> comments;
	
	private String cleanContent;
	private String sentimentLabel; // Renamed for consistency with APIClient (was sentimentPoint)
	private String damageTypeLabel; // Renamed for consistency with APIClient (was damageType)
    private String reliefTypeLabel; // NEW FIELD: Required for Problem 3 (Relief Item Classification)

	public Post(String id, String rawContent, Date publishedAt, int likeCount, int cmtCount) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.publishedAt = publishedAt;
		this.likeCount = likeCount;
		this.cmtCount = cmtCount;
		this.comments = new ArrayList<>();
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

	public Date getTimestamp() {
		return publishedAt;
	}

	public PostSource getSource() {
		return source;
	}

	public void setSource(PostSource source) {
		this.source = source;
	}

	public int getLike() {
		return likeCount;
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