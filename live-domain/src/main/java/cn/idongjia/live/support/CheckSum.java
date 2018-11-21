package cn.idongjia.live.support;

import java.security.MessageDigest;

/**
 * 获取md5编码
 */
public final class CheckSum {

    private CheckSum(){}

    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final int DIGITAL_OFFSET = 4;
    private static final byte CONJUNCTION = 0x0f;

    /**
     * 根据所选择的加密算法和加密内容获取加密后字符串
     * @param algorithm 算法名称
     * @param value 加密内容
     * @return 加密后字符串
     */
    public static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取十六进制的字符串
     * @param bytes 字节数据
     * @return 十六进制字符串
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> DIGITAL_OFFSET & CONJUNCTION)]);
            buf.append(HEX_DIGITS[aByte & CONJUNCTION]);
        }
        return buf.toString();
    }
}
