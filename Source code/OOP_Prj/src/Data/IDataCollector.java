package Data;
import Model.Post;

import java.time.LocalDateTime;
import java.util.*;
public interface IDataCollector {
	void initialize(Map<String, String> configParams);
	
    List<Post> collect(List<String> keywords, Date startDate, Date endDate);

}