package cn.zjc.utils;

import cn.zjc.entity.RabbitmqConfig;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zjc
 * @version 2016/11/22 0:04
 * @description
 */
public class JsonFileResolver {


	public static List<RabbitmqConfig> getFromJsonFile(String localtion) throws Exception {
		Resource resource = ResourceUtils.getResourcesByLocation(localtion);
		String value  = Streams.asString(resource.getInputStream());
		return JacksonMapper.buildNonDefaultMapper().parseList(value, RabbitmqConfig.class);
	}

	public static void main(String[] args) throws Exception{
		List<RabbitmqConfig> list = getFromJsonFile("classpath:ss.json");
		System.out.println(JacksonMapper.buildNonDefaultMapper().toJson(list));



	}
}
