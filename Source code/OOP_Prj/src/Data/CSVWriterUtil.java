package Data;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVWriterUtil {

    public static void writeCsv(String filename, List<String[]> rows) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

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