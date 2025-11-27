// File: DataCollector.java (trong gói Data)

package Data;

import Model.Post;
import java.util.List;

public interface DataCollector {
    
    /**
     * Thu thập dữ liệu từ một nguồn cụ thể và trả về List<Post> thô.
     * @param sourcePath Đường dẫn đến nguồn dữ liệu (ví dụ: đường dẫn file, API endpoint).
     * @return Danh sách các Post thô (chưa được xử lý).
     */
    List<Post> collect(String sourcePath);
}