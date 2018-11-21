package cn.idongjia.live.support;

import cn.idongjia.live.restructure.manager.ConfigManager;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Random;

public final class LiveUtil {

    public static final String[] WEEK_DAYS = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static final Gson GSON = new Gson();
    private static final String[] CH_NUM = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] CH_UNIT = new String[]{"", "十", "百", "千", "万", "十万", "千万", "亿"};
    private static final Random random = new Random();
    private static final ConfigManager configManager = SpringBeanLoader.getBean("configManager", ConfigManager.class);

    private LiveUtil() {
    }

    /**
     * 根据最大值获取0-max间随机数
     *
     * @param range 最大值
     * @return 随机数字符串
     */
    public static String getRandom(int range) {
        return String.valueOf(new Random().nextInt(range));
    }

    /**
     * 根据资源ID组装H5链接
     *
     * @param resId 资源ID
     * @return H5链接
     */
    public static String assembleH5Url(String resId) {
        return LiveUtil.configManager.getH5Prefix() + resId + LiveUtil.configManager.getH5Suffix();
    }

    /**
     * 根据数字转换成中文数字
     *
     * @param num 阿拉伯数字
     * @return 中文数字字符串
     */
    public static String convert2ChineseNum(int num) {
        int temp = 10;
        int unit = 0;
        int preNum = 0;
        StringBuilder result = new StringBuilder();
        while (num > 0) {
            int pos = num % temp;
            if (pos == 1 && unit == 1) {
                result.insert(0, LiveUtil.CH_UNIT[unit]);
            } else if (pos != 0) {
                result.insert(0, LiveUtil.CH_UNIT[unit]);
                result.insert(0, LiveUtil.CH_NUM[pos]);
            } else if (unit > 0 && preNum != 0) {
                result.insert(0, LiveUtil.CH_NUM[pos]);
            }
            unit++;
            preNum = pos;
            num = num / temp;
        }
        return result.toString();
    }
    public static String itemPrice2Int(String price){
        int priceInt= BigDecimal.valueOf(Double.valueOf(price)).multiply(new BigDecimal(100)).intValue();
        return priceInt%100==0?String.valueOf(priceInt/100):price;
    }


    public static int getCacheRandom(int baseSecond,int second){
        int s = LiveUtil.random.nextInt(second);
            return s+baseSecond;
    }

    public static Long timestampeToMills(Timestamp timestamp) {
        Long mills = null;
        if (timestamp != null) {
            mills = timestamp.getTime();
        }
        return mills;
    }
}
