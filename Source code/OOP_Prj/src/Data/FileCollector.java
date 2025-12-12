package Data;

import Model.Post;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileCollector implements IDataCollector {

    private String filePath;
    // Regex này tách dấu phẩy NHƯNG bỏ qua dấu phẩy nằm trong ngoặc kép
    private static final String CSV_SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    public FileCollector(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void initialize(Map<String, String> configParams) {
        System.out.println("Reading file (No Lib): " + filePath);
    }

    @Override
    public List<Post> collect(List<String> keywords, Date startDate, Date endDate) {
        List<Post> results = new ArrayList<>();
        SimpleDateFormat csvDateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;

                // 1. Tách dòng bằng Regex
                // -1 để giữ cả các trường rỗng ở cuối dòng
                String[] row = line.split(CSV_SPLIT_REGEX, -1);
                
                if (row.length < 5) continue;

                // 2. Làm sạch dữ liệu (Xóa ngoặc kép bao quanh, un-escape "")
                String id = cleanCsvCell(row[0]);
                String content = cleanCsvCell(row[1]);
                String dateStr = cleanCsvCell(row[2]);
                
                int likeCount = 0;
                int cmtCount = 0;
                try {
                    likeCount = Integer.parseInt(cleanCsvCell(row[3]));
                    cmtCount = Integer.parseInt(cleanCsvCell(row[4]));
                } catch (Exception e) {}

                Date postDate = null;
                try {
                    postDate = csvDateFmt.parse(dateStr);
                } catch (Exception e) {}

                // --- LOGIC LỌC (FILTERING) ---
                if (postDate != null) {
                    if (startDate != null && postDate.before(startDate)) continue;
                    if (endDate != null && postDate.after(endDate)) continue;
                }

                boolean match = true;
                if (keywords != null && !keywords.isEmpty()) {
                    match = false;
                    for (String kw : keywords) {
                        if (content.toLowerCase().contains(kw.toLowerCase())) {
                            match = true;
                            break;
                        }
                    }
                }

                if (match) {
                    results.add(new Post(id, content, postDate, likeCount, cmtCount));
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc file: " + e.getMessage());
        }
        return results;
    }

    // Hàm phụ trợ: Xóa ngoặc kép bao quanh và sửa "" thành "
    private String cleanCsvCell(String raw) {
        if (raw == null) return "";
        String clean = raw.trim();
        if (clean.startsWith("\"") && clean.endsWith("\"")) {
            clean = clean.substring(1, clean.length() - 1);
        }
        return clean.replace("\"\"", "\"");
    }
}