package PreProcessor;

public class SpecialSymbolRemover implements PreProcessor {

    @Override
    public String process(String content) {
        if (content == null) {
            return null;
        }
        
        // Regex giải thích:
        // [^ ... ] : Phủ định (Xóa tất cả những gì KHÔNG nằm trong ngoặc)
        // \p{L}    : Tất cả chữ cái Unicode (Bao gồm tiếng Việt có dấu, tiếng Anh...)
        // \p{N}    : Tất cả các số (0-9)
        // \p{P}    : Các dấu câu (phẩy, chấm, chấm than...)
        // \p{Z}    : Các khoảng trắng (dấu cách, tab, xuống dòng)
        
        return content.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");
    }
}