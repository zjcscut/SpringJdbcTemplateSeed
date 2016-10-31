package cn.zjc.common.converter;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Date;

/**
 * @author zhangjinci
 * @version 2016/10/31 15:50
 * @function 字符串转换为java.util.Date转换器
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final Logger log = LoggerFactory.getLogger(StringToDateConverter.class);

    private final static String[] dateFormats = {
            "EEE, d MMM yyyy HH:mm:ss z",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss"};

    @Override
    public Date convert(String s) {
        Assert.hasText(s, "Null or emtpy date string");
        Date date = null;
        try {
            date = DateUtils.parseDate(s, dateFormats);
        } catch (ParseException e) {
            String errMsg = String.format("Failed to convert [%s] to [%s] for value '%s'", String.class.toString(), Date.class.toString(), s);
            log.debug(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        return date;
    }
}
