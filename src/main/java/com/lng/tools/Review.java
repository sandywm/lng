package com.lng.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.contentcensor.AipContentCensor;

public class Review {
	// 设置APPID/AK/SK
	public static final String APP_ID = "18127380";
	public static final String API_KEY = "OB1WrmtGAbxtqkwd8k3gtYeL";
	public static final String SECRET_KEY = "s3smniuM8FaYAvKPAFiihmjqa7lQrrrq";

	public static String textReview(String content) {
		AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
		String result = "";
		JSONObject outJson = client.antiSpam(content, null);
		JSONArray json = outJson.getJSONObject("result").getJSONArray("reject");
		if (json.length() != 0) {
			for (int i = 0; i < json.length(); i++) {
				JSONObject rejects = json.getJSONObject(i);
				JSONArray hits = (JSONArray) rejects.get("hit");
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

}
