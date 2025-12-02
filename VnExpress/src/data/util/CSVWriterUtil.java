package data.util;
import data.model.PostRecord; 
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class CSVWriterUtil {
    
    private static final String DELIMITER = "\",\""; 
    private static final String QUOTE = "\"";       
    private static final String NEW_LINE = "\n";    

    public CSVWriterUtil() {
    }
    
    
	public static void writeCsv(String filename, List<String[]> rows) {
        try (
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw)
        ) {
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            for (String[] row : rows) {
                String line = QUOTE + String.join(DELIMITER, row) + QUOTE;
                writer.write(line);
                writer.write(NEW_LINE);
            }

            System.out.println("Saved: " + filename);
        } catch (Exception e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

}