package collector;
import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
public class CSVWriterUtil {
    public static <T> void writeCsv(String filename, List<String[]> rows) {
        try (
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            CSVWriter writer = new CSVWriter(osw)
        ) {
            // Write BOM so Excel reads UTF-8 correctly
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            writer.writeAll(rows);
            System.out.println("Saved: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}