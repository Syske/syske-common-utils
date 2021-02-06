package io.github.syske.common.util;

import com.alibaba.fastjson.JSONObject;
import io.github.syske.commont.utils.http.HttpClientUtil;
import org.junit.Test;

import java.util.Map;

public class HttpUtilsTest {
    @Test
    public static void postTest() {
        /*Map<String, String> map = new HashMap<>();
        JSONObject param = new JSONObject();
        param.put("activity_id","60:video:comets:10011#2");
        param.put("state",3);
        param.put("attr_tags","");
        param.put("msg","");
        map.put("param",param.toJSONString());
        String form = form("http://yp5ntd.natappfree.cc/CnInteraction/services/commentForHd/auditCallBackNew", map);
        System.out.println(form);*/


        JSONObject params = new JSONObject();
        params.put("video_id","60_8be004799c424688949704814ea0d16d");
        params.put("state",3+"");
        params.put("attr_tags","");
        params.put("msg","");
        HttpClientUtil httpUtil = HttpClientUtil.init();
        httpUtil.setParam("param",params.toJSONString());
        String URL ="http://abcd.com/v1/ShenHeInfo/shenheNotify?platform=AUDIT&token=94cead75c";
        Map<String, String> post = httpUtil.post(URL);
        System.out.println(post.get("result"));
    }
}
