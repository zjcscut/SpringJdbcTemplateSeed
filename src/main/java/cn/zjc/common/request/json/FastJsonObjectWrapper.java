package cn.zjc.common.request.json;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhangjinci
 * @version 2016/11/2 12:29
 * @function
 */
public class FastJsonObjectWrapper {

    private JSONObject jsonObject;

    public FastJsonObjectWrapper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
