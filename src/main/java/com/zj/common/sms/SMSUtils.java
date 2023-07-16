package com.zj.common.sms;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 校验手机号是否合规的工具类
 */
public class SMSUtils {
	/**
	 * 校验手机是否合规 2020年最全的国内手机号格式
	 */
	private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

	/**
	 * 邮箱格式
	 */
	private static final String REGEX_EMAIL = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

	/**
     * 校验手机号
     *
     * @param phone 手机号
     * @return boolean true:是  false:否
     */
    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

	/**
	 * 校验邮箱
	 *
	 * @param email 需要校验的邮箱号
	 * @return boolean true:是  false:否
	 */
	public static boolean isEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 密码是否输入正确
	 * 密码不能为空, 同时长度必须大于等于 8
	 * @param password 输入的密码
	 * @return false 输入正确, true 输入错误
	 */
	public static boolean passwordIsError(String password) {
		// 是否正确
		boolean isRight = StringUtils.isNotBlank(password) && password.length() >= 8;
		// 是否错误 取反
		return !isRight;
	}


}

