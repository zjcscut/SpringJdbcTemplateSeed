package cn.zjc.eventbus.listerner;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;


/**
 * @author zhangjinci
 * @version 2016/10/21 16:47
 * @function
 */
@Component("strMessageListerner")
public class StrMessageListerner {

    @Subscribe
    public void commit(String msg) {
        System.out.println("接收到的消息为==>" + msg);
    }
}
