package cn.zjc.controller;

import cn.zjc.entity.User;
import cn.zjc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjinci
 * @version 2016/10/9 14:37
 * @function
 */
@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);



	@Autowired
	private UserService userService;

	@RequestMapping(value = "/index")
	public String index(@RequestParam("id") String id, ModelMap modelMap) {
		modelMap.put("users", userService.selectAll());
		String s = id;
		System.out.println("ssss==>" + s);
		return "index";
	}


	private static ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();


	@RequestMapping(value = "/user/save", method = RequestMethod.POST)
	@ResponseBody
	public String saveUser(User user) {
		if (userMap.containsKey("saveUser")) {
			logger.debug("saveUser ----------- 业务正在处理中请勿重复提交");
			return "业务正在处理中请勿重复提交";
		}
		userMap.put("saveUser", user.getNum());
		try {
			if (userService.saveUser(user)) {   //业务的处理方法
				logger.debug("saveUser ----------- 保存成功");
				return "保存成功";
			}
		} finally {
			userMap.remove("saveUser");
		}
		logger.debug("saveUser ----------- 已保存相同的用户");
		return "已保存相同的用户";
	}


}
