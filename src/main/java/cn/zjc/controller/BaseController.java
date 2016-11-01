package cn.zjc.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangjinci
 * @version 2016/11/1 15:00
 * @function
 */
public abstract class BaseController {

    private static final String LONG_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String LOCAL_LONG_DATETIME_PATTERN = "yyyy年MM月dd日 HH时mm分ss秒";
    private static final String LOCAL_DATE_PATTERN = "yyyy年MM月dd日";
    private static final String LOCAL_TIME_PATTERN = "HH时mm分ss秒";
    private static final boolean ALLOW_NULL = true;

    @InitBinder(value = "user" )
    public void initBinder(WebDataBinder binder) throws Exception {

//        binder.registerCustomEditor(Date.class, longDateTimeEditor());
//        binder.registerCustomEditor(Date.class, dateEditor());
//        binder.registerCustomEditor(Date.class, timeEditor());
        binder.registerCustomEditor(Date.class, localLongDateTimeEditor());
//        binder.registerCustomEditor(Date.class, localDateEditor());
//        binder.registerCustomEditor(Date.class, localTimeEditor());
    }

    private DateFormat buildDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }


    private CustomDateEditor longDateTimeEditor() {
        return new CustomDateEditor(buildDateFormat(LONG_DATETIME_PATTERN), ALLOW_NULL);
    }

    private CustomDateEditor dateEditor() {
        return new CustomDateEditor(buildDateFormat(DATE_PATTERN), ALLOW_NULL);
    }

    private CustomDateEditor timeEditor() {
        return new CustomDateEditor(buildDateFormat(TIME_PATTERN), ALLOW_NULL);
    }

    private CustomDateEditor localLongDateTimeEditor() {
        return new CustomDateEditor(buildDateFormat(LOCAL_LONG_DATETIME_PATTERN), ALLOW_NULL);
    }

    private CustomDateEditor localDateEditor() {
        return new CustomDateEditor(buildDateFormat(LOCAL_DATE_PATTERN), ALLOW_NULL);
    }

    private CustomDateEditor localTimeEditor() {
        return new CustomDateEditor(buildDateFormat(LOCAL_TIME_PATTERN), ALLOW_NULL);
    }


}
