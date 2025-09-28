package com.magee.common.utils.encrypt;


import cn.hutool.crypto.digest.DigestUtil;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class MD5Util {
    /**
     * md5加密
     * */
    public static String md5Hash(String str){
        return DigestUtil.md5Hex(str);
    }
}
