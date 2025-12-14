package PreProcessor;

import java.util.HashMap;
import java.util.Map;

public class VietnameseNormalizer implements PreProcessor {
    
    private static final Map<String, String> TEENCODE_MAP = new HashMap<>();

    static {
        TEENCODE_MAP.put(" k ", " không ");
        TEENCODE_MAP.put(" ko ", " không ");
        TEENCODE_MAP.put(" kh ", " không ");
        TEENCODE_MAP.put(" vs ", " với ");
        TEENCODE_MAP.put(" dc ", " được ");
        TEENCODE_MAP.put(" đc ", " được ");
        TEENCODE_MAP.put(" j ", " gì ");
        TEENCODE_MAP.put(" v ", " vậy ");
        TEENCODE_MAP.put(" z ", " vậy "); 
        TEENCODE_MAP.put(" mn ", " mọi người ");
        TEENCODE_MAP.put(" mng ", " mọi người ");
        TEENCODE_MAP.put(" ng ", " người ");
        TEENCODE_MAP.put(" qá ", " quá ");
        TEENCODE_MAP.put(" qs ", " quá ");
        TEENCODE_MAP.put(" lun ", " luôn ");
        TEENCODE_MAP.put(" r ", " rồi ");
        TEENCODE_MAP.put(" oy ", " rồi "); 
        TEENCODE_MAP.put(" hnay ", " hôm nay ");
        TEENCODE_MAP.put(" cs ", " có ");
        
        TEENCODE_MAP.put(" vn ", " việt nam ");
        TEENCODE_MAP.put(" tphcm ", " thành phố hồ chí minh ");
        TEENCODE_MAP.put(" tp ", " thành phố ");
        TEENCODE_MAP.put(" ubnd ", " ủy ban nhân dân ");
        TEENCODE_MAP.put(" bv ", " bệnh viện ");
        TEENCODE_MAP.put(" pccc ", " phòng cháy chữa cháy ");
        TEENCODE_MAP.put(" ca ", " công an ");
    }

    @Override
    public String process(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        
        String result = " " + content + " "; 
        for (Map.Entry<String, String> entry : TEENCODE_MAP.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        return result.trim();
    }
}