package PreProcessor;

import Model.Post;

/**
 * Interface cho các bộ xử lý dữ liệu con.
 * Mỗi bộ xử lý nhận vào một String (nội dung đã sạch) và trả về String đã được xử lý tiếp.
 */
public interface PreProcessor {

    /**
     * Thực hiện một bước tiền xử lý trên nội dung văn bản.
     * @param content Nội dung văn bản đầu vào.
     * @return Nội dung văn bản đã được xử lý.
     */
    String process(String content);
}