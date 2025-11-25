package Model;

import java.time.LocalDateTime;

public class Post {
	
	private String id;
	private String rawContent;
	private LocalDateTime timestamp;
	private PostSource source;
	private String url;
	
	private String cleanContent;
	
	private String sentimentPoint;
	private String damageType;
	
	public Post(String id, String rawContent, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
	}

	public Post(String id, String rawContent, LocalDateTime timestamp, PostSource source) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.source = source;
	}

	public Post(String id, String rawContent, LocalDateTime timestamp, PostSource source, String url) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.source = source;
		this.url = url;
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

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
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

	public String getUrl() {
		return url;
	}
}