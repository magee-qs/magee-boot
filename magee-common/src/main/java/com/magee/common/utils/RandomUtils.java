package com.magee.common.utils;

import com.magee.common.exception.UtilException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 功能描述
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {
    public static final String BASE_NUMBER = "0123456789";
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    public static final String BASE_CHAR_NUMBER_LOWER = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String BASE_CHAR_NUMBER = "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 获取随机数生成器对象
     * */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    public static SecureRandom getSecureRandomStrong() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException e) {
            throw new UtilException(e);
        }
    }
    /**
     * 获得指定范围内的随机数 [0,limit)
     * */
    public static int randomInt(int limit) {
        return getRandom().nextInt(limit);
    }
    /**
     * 获得指定范围内的随机数
     * @param min 最小数(包含)
     * @param max 最大数(不包含)
     * */
    public static int randomInt(int min, int max) {
        return getRandom().nextInt(min, max);
    }

    /**
     * 获得一个随机的字符串（只包含数字和字符）
     * @param length 长度
     * @return 随机字符串
     * */
    public static String randomString(int length){
        return randomString(BASE_CHAR_NUMBER, length);
    }
    /**
     * 获得一个随机的字符串（只包含数字和字符）
     * @param baseStr 随机字符串样本
     * @param length 长度
     * @return 随机字符串
     * */
    public static String randomString(String baseStr, int length){
        if (StringUtils.isEmpty(baseStr)) {
            return StringUtils.EMPTY;
        }
        if (length < 1) {
            length = 1;
        }

        final StringBuilder sb = new StringBuilder(length);
        final int baseLength = baseStr.length();
        for (int i = 0; i < length; i++) {
            final int number = randomInt(baseLength);
            sb.append(baseStr.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 随机汉字
     * */
    public static char randomChinese() {
        return (char) randomInt('\u4E00', '\u9FFF');
    }
}
