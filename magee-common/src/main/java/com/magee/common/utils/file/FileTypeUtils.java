package com.magee.common.utils.file;

import com.magee.common.utils.StringUtils;

import java.io.File;
import java.util.Locale;

/**
 * 文件类型工具类
 * @author magee
 * @version 1.0
 */
public class FileTypeUtils {

    /**
     * 获取文件类型
     * 例如: a.txt, 返回: txt
     *
     * @param file 文件
     * @return 后缀(不包括.)
     * */
    public static String getFileType(File file){
        if(file == null){
            return StringUtils.EMPTY;
        }else{
            return getFileType(file.getName());
        }
    }

    /**
     * 获取文件类型
     * 例如: a.txt, 返回: txt
     *
     * @param fileMame 文件名
     * @return 后缀(不包括.)
     * */
    public static String getFileType(String fileMame){
        if(StringUtils.isEmpty(fileMame)){
            return StringUtils.EMPTY;
        }
        int index = fileMame.lastIndexOf(".");
        if(index < 0){
            return StringUtils.EMPTY;
        }
        return fileMame.substring(index+ 1).toLowerCase();
    }


    /**
     * 获取文件类型
     *
     * @param photoByte 文件字节码
     * @return 后缀（不含".")
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "JPG";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "GIF";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "JPG";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "BMP";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }
}
