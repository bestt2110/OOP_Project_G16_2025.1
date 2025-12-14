package Analysis;

import Model.*;
import java.util.*;

public class DamageCategoryAnalysis implements AnalysisTask<CountNum> {
    
    private static final List<String> AFFECTED_PEOPLE_WORDS = Arrays.asList("người bị thương", "mất tích", "cần cứu", "tử vong", "người dân gặp nạn");
    private static final List<String> DAMAGED_INFRASTRUCTURE_WORDS = Arrays.asList("cầu sập", "đứt đường", "mất điện", "cột điện", "trạm y tế", "trạm xá");
    private static final List<String> HOUSES_DAMAGED_WORDS = Arrays.asList("nhà sập", "tường đổ", "thiệt hại nhà", "mất nhà", "lều", "mái nhà", "tốc mái");
    private static final List<String> LOSS_BELONGINGS_WORDS = Arrays.asList("mất tài sản", "tư trang", "mất đồ", "quần áo", "đồ đạc bị trôi");
    private static final List<String> DISRUPTION_PRODUCTION_WORDS = Arrays.asList("mất mùa", "ao cá", "cây trồng", "thất thu", "ngừng sản xuất", "chết gà", "trôi bò");

    @Override
    public void execute(Post p, Map<String, CountNum> result) {
        String postCategory = classifyContent(p.getCleanContent());
        updateMap(result, postCategory);

        if (p.getComments() != null) {
            for (Comment c : p.getComments()) {
                String cmtCategory = classifyContent(c.getCleanContent());
                updateMap(result, cmtCategory);
            }
        }
    }
    
    private void updateMap(Map<String, CountNum> result, String category) {
        if (category == null) return;

        CountNum b2 = result.getOrDefault(category, new CountNum(0));
        b2.setCount(b2.getCount() + 1); 
        
        result.put(category, b2);
    }

    private String classifyContent(String content) {
        if (content == null || content.trim().isEmpty()) return null;
        String lower = content.toLowerCase();

        if (containsAny(lower, AFFECTED_PEOPLE_WORDS)) return "Con người (People)";
        if (containsAny(lower, DAMAGED_INFRASTRUCTURE_WORDS)) return "Hạ tầng (Infrastructure)";
        if (containsAny(lower, HOUSES_DAMAGED_WORDS)) return "Nhà cửa (Housing)";
        if (containsAny(lower, LOSS_BELONGINGS_WORDS)) return "Tài sản (Belongings)";
        if (containsAny(lower, DISRUPTION_PRODUCTION_WORDS)) return "Kinh tế (Economy)";

        return null;
    }

    private boolean containsAny(String content, List<String> keywords) {
        for (String k : keywords) if (content.contains(k)) return true;
        return false;
    }
}