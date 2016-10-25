package cn.zjc.controller;

import cn.zjc.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zjc
 * @version 2016/10/25 23:38
 * @description 调度Controller
 */
@Controller
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	@RequestMapping(value = "/schedule.htm", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView schedule(ModelAndView mav) {
		mav.setViewName("schedule");
		mav.addObject("tasks", scheduleService.queryAll());
		return mav;
	}
}
