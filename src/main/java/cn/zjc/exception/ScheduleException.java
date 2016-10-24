package cn.zjc.exception;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:28
 * @function
 */
public class ScheduleException extends RuntimeException {

    public ScheduleException() {
    }

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
