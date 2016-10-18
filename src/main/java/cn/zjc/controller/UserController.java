package cn.zjc.controller;

import cn.zjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangjinci
 * @version 2016/10/9 14:37
 * @function
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index")
    public String index(@RequestParam("id") String id, ModelMap modelMap) {
        modelMap.put("users", userService.selectAll());
		String s = id;
		System.out.println("ssss==>" + s);
		return "index";
    }


}
