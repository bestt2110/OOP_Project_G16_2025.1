// File: FileCollector.java (trong gói Data)

package Data;

import Model.Comment;
import Model.Post;
import Model.PostSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileCollector implements DataCollector {

    @Override
    public List<Post> collect(String filePath) {
        List<Post> posts = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Bỏ qua tiêu đề
            String line;
            
            while ((line = br.readLine()) != null) {
                // 1. DÙNG HÀM PARSE THÔNG MINH THAY VÌ SPLIT(",")
                List<String> values = parseCsvLine(line);
                
                // 2. LÀM SẠCH DỮ LIỆU (Trim + Xóa ngoặc kép bao quanh)
                for (int i = 0; i < values.size(); i++) {
                    String val = values.get(i).trim();
                    // Nếu chuỗi bắt đầu và kết thúc bằng ngoặc kép thì xóa chúng đi
                    if (val.startsWith("\"") && val.endsWith("\"")) {
                        val = val.substring(1, val.length() - 1);
                    }
                    // Xóa tiếp các ngoặc kép còn sót lại bên trong (nếu có, tuỳ nhu cầu)
                    val = val.replace("\"\"", "\""); 
                    values.set(i, val);
                }

                // --- BẮT ĐẦU XỬ LÝ ---
                try {
                    // Kiểm tra độ dài tối thiểu (4 cột bắt buộc)
                    if (values.size() < 4) {
                        continue; // Bỏ qua dòng lỗi
                    }

                    // --- CỘT BẮT BUỘC (0, 1, 2, 3) ---
                    String id = values.get(0);
                    String rawContent = values.get(1); // Bây giờ nội dung có dấu phẩy vẫn OK
                    
                    LocalDateTime timestamp = LocalDateTime.ofInstant(
                        Instant.parse(values.get(2)), ZoneOffset.UTC
                    );
                    
                    int cmt = Integer.parseInt(values.get(3));

                    // --- TẠO ĐỐI TƯỢNG POST ---
                    // (Tạo 1 lần duy nhất ở đây để tránh logic rườm rà)
                    Post post = new Post(id, rawContent, timestamp, cmt);
                    
                    // --- CỘT TÙY CHỌN (SOURCE - INDEX 4) ---
                    if (values.size() >= 5) {
                        try {
                            String sourceString = values.get(4);
                            if (!sourceString.isEmpty()) {
                                PostSource source = PostSource.valueOf(sourceString.toUpperCase());
                                post.setSource(source); // Gán Source nếu có
                            }
                        } catch (IllegalArgumentException e) {
                            System.err.println("Source không hợp lệ: " + values.get(4));
                        }
                    }

                    posts.add(post);

                } catch (Exception e) {
                    System.err.println("Lỗi xử lý dòng: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return posts;
    }

public void loadComments(String filePath, List<Post> posts) {
        
        // 1. TỐI ƯU HÓA TÌM KIẾM: Chuyển List<Post> thành Map<PostID, Post>
        // Để khi đọc 1 comment, ta tìm Post cha của nó trong O(1) thay vì phải loop
        Map<String, Post> postMap = new HashMap<>();
        for (Post p : posts) {
            postMap.put(p.getId(), p);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Bỏ qua header
            String line;

            while ((line = br.readLine()) != null) {
                // Dùng lại hàm parseCsvLine thông minh
                List<String> values = parseCsvLine(line);
                
                // Clean data
                for (int i = 0; i < values.size(); i++) {
                    String val = values.get(i).trim();
                    if (val.startsWith("\"") && val.endsWith("\"")) {
                        val = val.substring(1, val.length() - 1);
                    }
                    values.set(i, val.replace("\"\"", "\""));
                }

                if (values.size() < 4) continue; // Yêu cầu tối thiểu 4 cột

                try {
                    // Cấu trúc: 0:CommentID, 1:PostID, 2:Content, 3:Timestamp
                    String commentId = values.get(1);
                    String postId = values.get(0); // Khóa ngoại
                    String content = values.get(2);
                    LocalDateTime timestamp = LocalDateTime.ofInstant(
                        Instant.parse(values.get(3)), ZoneOffset.UTC
                    );

                    // Tạo đối tượng Comment
                    Comment comment = new Comment(postId, commentId, content, timestamp);

                    // 2. TÌM POST CHA VÀ GẮN COMMENT VÀO
                    Post parentPost = postMap.get(postId);
                    if (parentPost != null) {
                        parentPost.addComment(comment); // Hàm này phải có trong class Post
                    } else {
                        System.err.println("Cảnh báo: Không tìm thấy Post có ID " + postId + " cho Comment " + commentId);
                    }

                } catch (Exception e) {
                    System.err.println("Lỗi dòng comment: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * HÀM HỖ TRỢ QUAN TRỌNG:
     * Tách dòng CSV nhưng bỏ qua dấu phẩy nằm trong ngoặc kép.
     * Ví dụ: '1,"Hello, World",ABC' -> ["1", "Hello, World", "ABC"]
     */
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes; // Đảo trạng thái: Đang trong ngoặc kép hoặc không
                currentField.append(c); // Vẫn giữ ngoặc kép để xử lý sau
            } else if (c == ',' && !inQuotes) {
                // Nếu gặp dấu phẩy VÀ KHÔNG nằm trong ngoặc kép -> Kết thúc 1 trường
                result.add(currentField.toString());
                currentField.setLength(0); // Reset bộ đệm
            } else {
                currentField.append(c); // Thêm ký tự bình thường
            }
        }
        // Thêm trường cuối cùng
        result.add(currentField.toString());
        return result;
    }
}