package PreProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StopWordsRemover implements PreProcessor {

    private Set<String> stopWords;

    public StopWordsRemover(String filePath) {
        this.stopWords = new HashSet<>();
        loadStopWords(filePath);
    }

    private void loadStopWords(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopWords.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Can't identify" + filePath + "as a stop word file");
        }
    }

    @Override
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        String[] words = content.split("\\s+");

        String result = Arrays.stream(words)
                .filter(word -> !stopWords.contains(word.toLowerCase())) 
                .collect(Collectors.joining(" "));  

        return result;
    }
}