package Analysis;

import Model.*;
import java.time.*;
import java.util.*;

public class DamageCategoryAnalysis implements AnalysisTask<B2Result> {
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
    @Override
    public void execute(Post p, HashMap<String, B2Result> result) {
    	if (!p.getComments().isEmpty()) {
    		List<Comment> comments  = p.getComments();
	        for (Comment c : comments) {
	            String category = classifyComment(c.getCleanContent());
	
	            // Lấy B2Result hiện tại hoặc tạo mới nếu chưa có
	            B2Result b2 = result.getOrDefault(category, new B2Result(0));
	
	            // Tăng count lên 1
	            b2.setCount(b2.getCount() + 1);
	
	            // Đưa lại vào map
	            result.put(category, b2);
	        }
    	}
    	String category = classifyComment(p.getCleanContent());
    	
        // Lấy B2Result hiện tại hoặc tạo mới nếu chưa có
        B2Result b2 = result.getOrDefault(category, new B2Result(0));

        // Tăng count lên 1
        b2.setCount(b2.getCount() + 1);

        // Đưa lại vào map
        result.put(category, b2);
    	
    private String classifyComment(String content) {
        if (content == null || content.isEmpty()) return null;

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
