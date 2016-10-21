package cn.zjc.eventbus.listerner;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author zjc
 * @version 2016/10/21 0:39
 * @description
 */
@Component
public class DeadEventListerner {

	@Subscribe
	public void listern(DeadEvent deadEvent){

	}
}
