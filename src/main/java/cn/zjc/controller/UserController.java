package cn.zjc.controller;

import cn.zjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(ModelMap modelMap) {
        modelMap.put("users", userService.selectAll());
        return "index";
    }


}
