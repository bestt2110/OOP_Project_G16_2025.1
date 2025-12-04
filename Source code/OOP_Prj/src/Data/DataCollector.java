// File: DataCollector.java (trong gói Data)

package Data;

import Model.Post;
import java.util.List;

public interface DataCollector {

    List<Post> collect(String sourcePath);
}
