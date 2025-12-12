package Data;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVWriterUtil {

    public static void writeCsv(String filename, List<String[]> rows) {
        try {
            // 1. Tạo luồng ghi file
            FileOutputStream fos = new FileOutputStream(filename);
            
            // 2. Ghi BOM (Byte Order Mark) để Excel nhận diện Tiếng Việt UTF-8
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            // 3. Khởi tạo OpenCSV Writer
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            
            try (CSVWriter writer = new CSVWriter(osw)) {
                writer.writeAll(rows);
            }
            
            System.out.println("✅ Saved CSV successfully: " + filename);

        } catch (Exception e) {
            System.err.println("❌ Error writing CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}