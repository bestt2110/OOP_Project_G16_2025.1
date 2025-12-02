package data.core;
import data.model.PostRecord;
import java.util.Date;
import java.util.List;
import java.util.Map;
public interface IDataCollector {
	void initialize(Map<String, String> configParams);
    List<PostRecord> collect(List<String> keywords, Date startDate, Date endDate);

}
