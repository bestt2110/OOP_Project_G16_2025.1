package Analysis;

import Model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DamageCategoryAnalysis: Implements AnalysisTask for Problem 2.
 * Logic: Classifies Post content into predefined damage categories using a simple keyword matching algorithm, 
 * and aggregates the counts for each category.
 */
public class DamageCategoryAnalysis {

    // ==========================
    // 1. Danh sách từ khóa theo category
    // ==========================
    private static final List<String> AFFECTED_PEOPLE_WORDS = Arrays.asList(
            "người bị thương", "mất tích", "cần cứu", "tử vong", "người dân gặp nạn"
    );

    private static final List<String> DAMAGED_INFRASTRUCTURE_WORDS = Arrays.asList(
            "cầu sập", "đứt đường", "mất điện", "cột điện", "trạm y tế", "trạm xá"
    );

    private static final List<String> HOUSES_DAMAGED_WORDS = Arrays.asList(
            "nhà sập", "tường đổ", "thiệt hại nhà", "mất nhà", "lều", "mái nhà"
    );

    private static final List<String> LOSS_BELONGINGS_WORDS = Arrays.asList(
            "mất tài sản", "tư trang", "mất đồ", "quần áo", "đồ đạc bị trôi"
    );

    private static final List<String> DISRUPTION_PRODUCTION_WORDS = Arrays.asList(
            "mất mùa", "ao cá", "cây trồng", "thất thu", "ngừng sản xuất"
    );

    // ==========================
    // 2. Phân loại và đếm
    // ==========================
    public Map<String, Long> execute(List<Comment> comments) {
        Map<String, Long> result = new HashMap<>();

        for (Comment c : comments) {
            String category = classifyComment(c.getCleanContent());
            result.put(category, result.getOrDefault(category, 0L) + 1);
        }

        return result;
    }

    // ==========================
    // 3. Xác định category của một comment
    // ==========================
    private String classifyComment(String content) {
        if (content == null || content.isEmpty()) return "Other/Unclassified";

        String lower = content.toLowerCase();

        if (containsAny(lower, AFFECTED_PEOPLE_WORDS)) return "Affected People";
        if (containsAny(lower, DAMAGED_INFRASTRUCTURE_WORDS)) return "Damaged Infrastructure";
        if (containsAny(lower, HOUSES_DAMAGED_WORDS)) return "Houses or Buildings Damaged";
        if (containsAny(lower, LOSS_BELONGINGS_WORDS)) return "Loss of Personal Belongings";
        if (containsAny(lower, DISRUPTION_PRODUCTION_WORDS)) return "Disruption of Economic Production";

        return "Other/Unclassified";
    }

    // ==========================
    // 4. Helper kiểm tra chứa từ khóa
    // ==========================
    private boolean containsAny(String content, List<String> keywords) {
        for (String k : keywords) {
            if (content.contains(k)) return true;
        }
        return false;
    }
}