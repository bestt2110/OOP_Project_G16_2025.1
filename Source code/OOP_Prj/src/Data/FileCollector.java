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
            // Dùng StringBuilder để ghép các dòng bị ngắt quãng (Multi-line support)
            StringBuilder currentRecord = new StringBuilder(); 
            boolean inQuotes = false; 

            while ((line = br.readLine()) != null) {
                
                // 1. Logic ghép dòng: Kiểm tra xem số lượng dấu ngoặc kép có chẵn/lẻ
                for (char c : line.toCharArray()) {
                    if (c == '\"') inQuotes = !inQuotes;
                }
                
                currentRecord.append(line);

                if (inQuotes) {
                    // Nếu vẫn đang trong ngoặc kép, thêm dấu xuống dòng và đọc tiếp dòng sau
                    currentRecord.append("\n");
                    continue; // Bỏ qua các bước dưới, quay lại while để đọc tiếp
                }

                // 2. Khi đã có bản ghi trọn vẹn (inQuotes = false)
                String fullRecord = currentRecord.toString();
                List<String> values = parseCsvLine(fullRecord);
                
                // Reset bộ đệm cho vòng lặp sau
                currentRecord.setLength(0); 

                // 3. LÀM SẠCH DỮ LIỆU
                for (int i = 0; i < values.size(); i++) {
                    String val = values.get(i).trim();
                    if (val.startsWith("\"") && val.endsWith("\"")) {
                        val = val.substring(1, val.length() - 1);
                    }
                    val = val.replace("\"\"", "\""); 
                    values.set(i, val);
                }

                // --- BẮT ĐẦU XỬ LÝ POST ---
                try {
                    if (values.size() < 4) continue; 

                    // CỘT BẮT BUỘC (0:ID, 1:Content, 2:Time, 3:CmtCount)
                    String id = values.get(0);
                    String rawContent = values.get(1);
                    
                    LocalDateTime timestamp = LocalDateTime.ofInstant(
                        Instant.parse(values.get(2)), ZoneOffset.UTC
                    );
                    
                    int cmt = Integer.parseInt(values.get(3));

                    Post post = new Post(id, rawContent, timestamp, cmt);
                    
                    // CỘT TÙY CHỌN (4: Source)
                    if (values.size() >= 5) {
                        try {
                            String sourceString = values.get(4);
                            if (!sourceString.isEmpty()) {
                                PostSource source = PostSource.valueOf(sourceString.toUpperCase());
                                post.setSource(source);
                            }
                        } catch (IllegalArgumentException e) {
                            // System.err.println("Source lỗi: " + values.get(4));
                        }
                    }
                    posts.add(post);

                } catch (Exception e) {
                    System.err.println("Lỗi dòng Post: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return posts;
    }

    // -------------------------------------------------------------
    // LOAD COMMENTS (ĐÃ SỬA LỖI ĐA DÒNG VÀ INDEX)
    // -------------------------------------------------------------
    public void loadComments(String filePath, List<Post> posts) {
        
        // Map để tìm Post cha nhanh chóng
        Map<String, Post> postMap = new HashMap<>();
        for (Post p : posts) {
            postMap.put(p.getId(), p);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Bỏ qua header
            
            String line;
            StringBuilder currentRecord = new StringBuilder();
            boolean inQuotes = false;

            while ((line = br.readLine()) != null) {
                
                // Logic ghép dòng tương tự như trên
                for (char c : line.toCharArray()) {
                    if (c == '\"') inQuotes = !inQuotes;
                }
                
                currentRecord.append(line);

                if (inQuotes) {
                    currentRecord.append("\n");
                    continue; 
                }

                String fullRecord = currentRecord.toString();
                List<String> values = parseCsvLine(fullRecord);
                currentRecord.setLength(0); // Reset

                // Làm sạch
                for (int i = 0; i < values.size(); i++) {
                    String val = values.get(i).trim();
                    if (val.startsWith("\"") && val.endsWith("\"")) {
                        val = val.substring(1, val.length() - 1);
                    }
                    values.set(i, val.replace("\"\"", "\""));
                }

                if (values.size() < 4) continue;

                try {
                    // Cấu trúc chuẩn: 0:CommentID, 1:PostID, 2:Content, 3:Timestamp
                    String commentId = values.get(1); 
                    String postId = values.get(0);   
                    String content = values.get(2);
                    
                    LocalDateTime timestamp = LocalDateTime.ofInstant(
                        Instant.parse(values.get(3)), ZoneOffset.UTC
                    );

                    // Tạo Comment (Constructor: ID, Content, Time)
                    Comment comment = new Comment(postId, commentId, content, timestamp);

                    // Gắn vào Post cha
                    Post parentPost = postMap.get(postId);
                    if (parentPost != null) {
                        parentPost.addComment(comment);
                    } 

                } catch (Exception e) {
                    System.err.println("Lỗi dòng Comment: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm tách CSV chuẩn (Giữ nguyên)
     */
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
                currentField.append(c);
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString());
        return result;
    }
}