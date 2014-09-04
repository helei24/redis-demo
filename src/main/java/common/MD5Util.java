package common;

import java.security.MessageDigest;

public class MD5Util {

    public static String getMd5(String input) {

        try {
            byte[] by = input.getBytes("UTF-8");
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(by);
            byte[] mdByte = digest.digest();
            return parseByte2HexStr(mdByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Byte2HexStr
     * 
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * HexStr2Byte
     * 
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {

        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
