package com.magee.common.sensitization;

import java.util.function.Function;

/**
 * 脱敏类型
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public enum SensitiveType {
    /**
     * 姓名，第2位星号替换
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /**
     * 密码，全部字符都用*代替
     */
    PASSWORD(SensitiveUtils::password),

    /**
     * 身份证，中间10位星号替换
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\d{3}[Xx]|\\d{4})", "$1** **** ****$2")),

    /**
     * 手机号，中间4位星号替换
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * 电子邮箱，仅显示第一个字母和@后面的地址显示，其他星号替换
     */
    EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

    /**
     * 银行卡号，保留最后4位，其他星号替换
     */
    BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1")),

    /**
     * 车牌号码，包含普通车辆、新能源车辆
     */
    CAR_LICENSE(SensitiveUtils::carLicense);

    private final Function<String, String> function;

    SensitiveType(Function<String, String> function)
    {
        this.function = function;
    }

    public Function<String, String>  sensitiveFunc()
    {
        return function;
    }
}
