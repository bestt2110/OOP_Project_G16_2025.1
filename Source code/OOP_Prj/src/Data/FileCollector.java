package Data;

import Model.Post;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileCollector implements IDataCollector {

    private String filePath;

    public FileCollector(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void initialize(Map<String, String> configParams) {
        // Không cần làm gì
    }

    @Override
    public List<Post> collect(List<String> keywords, Date startDate, Date endDate) {
        List<Post> posts = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        File file = new File(this.filePath);
        if (!file.exists()) {
            System.err.println("❌ Lỗi: File không tồn tại: " + this.filePath);
            return posts;
        }

        System.out.println("-> Đang đọc file và lọc từ ngày: " + startDate + " đến " + endDate);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            
            String line = br.readLine(); // Đọc header
            if (line != null && line.startsWith("\ufeff")) line = line.substring(1);

            while ((line = br.readLine()) != null) {
                Post p = parseLineQuickly(line, fmt);
                
                if (p != null) {
                    Date pDate = p.getTimestamp();
                    
                    // [LOGIC LỌC THỜI GIAN]
                    if (pDate != null) {
                        // Nếu bài viết sớm hơn ngày bắt đầu -> Bỏ qua
                        if (startDate != null && pDate.before(startDate)) {
                            continue; 
                        }
                        // Nếu bài viết muộn hơn ngày kết thúc -> Bỏ qua
                        if (endDate != null && pDate.after(endDate)) {
                            continue;
                        }
                    }
                    
                    // Nếu ngày null hoặc thỏa mãn điều kiện thì mới thêm vào list
                    posts.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("-> Kết quả sau lọc: " + posts.size() + " bài viết.");
        return posts;
    }

    private Post parseLineQuickly(String line, SimpleDateFormat fmt) {
        try {
            int firstSep = line.indexOf("\",\"");
            int secondSep = line.indexOf("\",\"", firstSep + 3);
            int lastQuote = line.lastIndexOf("\",");

            if (firstSep == -1 || secondSep == -1 || lastQuote == -1) return null;

            String id = line.substring(1, firstSep);
            String dateStr = line.substring(firstSep + 3, secondSep);
            String content = line.substring(secondSep + 3, lastQuote);
            
            String statsPart = line.substring(lastQuote + 2); 
            String[] stats = statsPart.split(",");
            
            int likes = 0;
            int comments = 0;
            if (stats.length >= 2) {
                likes = Integer.parseInt(stats[0].trim());
                comments = Integer.parseInt(stats[1].trim());
            }

            Date date = null;
            try {
                if (!dateStr.equals("null") && !dateStr.isEmpty()) {
                    date = fmt.parse(dateStr);
                }
            } catch (Exception e) {}

            return new Post(id, content, date, likes, comments);

        } catch (Exception e) {
            return null;
        }
    }
}