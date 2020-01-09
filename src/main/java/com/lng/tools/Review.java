package com.lng.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.contentcensor.AipContentCensor;

/**
   *   敏感词屏蔽
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2020年1月9日 上午10:17:27
 */
public class Review {
	// 设置APPID/AK/SK
	public static final String APP_ID = "18127380";
	public static final String API_KEY = "OB1WrmtGAbxtqkwd8k3gtYeL";
	public static final String SECRET_KEY = "s3smniuM8FaYAvKPAFiihmjqa7lQrrrq";

	public static String textReview(String content) {
		AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
		String result = "";
		JSONObject outJson = client.antiSpam(content, null);
		JSONArray rejectJson = outJson.getJSONObject("result").getJSONArray("reject");
		JSONArray reviewJson = outJson.getJSONObject("result").getJSONArray("review");
		if (rejectJson.length() != 0) {
			for (int i = 0; i < rejectJson.length(); i++) {
				JSONObject rejects = rejectJson.getJSONObject(i);
				JSONArray hits = (JSONArray) rejects.get("hit");
				if (hits.length() != 0) {
					for (int j = 0; j < hits.length(); j++) {
						result += hits.get(j) + ",";
					}
				}
			}
		}
		if(reviewJson.length() !=0) {
			for (int i = 0; i < reviewJson.length(); i++) {
				JSONObject review = reviewJson.getJSONObject(i);
				JSONArray hits = (JSONArray) review.get("hit");
				if (hits.length() != 0) {
					for (int j = 0; j < hits.length(); j++) {
						result += hits.get(j) + ",";
					}
				}
			}
		}
		if (result.length() > 0) {
			String[] resArr = result.substring(0, result.length() - 1).split(",");
			for (int i = 0; i < resArr.length; i++) {
				String res = resArr[i];
				content = content.replace(res, "***");
			}
		}
		return content;
	}
	
	public static void main(String[] args) {
		String content="你是不是练法轮功的 你那里有毒品吗,你他妈的,你好吗,***";
		System.out.println(textReview(content));
	}

}
