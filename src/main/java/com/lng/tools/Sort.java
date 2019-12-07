package com.lng.tools;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Sort implements Comparator{

	@Override
	@SuppressWarnings("unchecked")
    public int compare(Object obj0, Object obj1) {
	  Map<String, String> map0 = (Map) obj0;
	  Map<String, String> map1 = (Map) obj1;
	  int flag = map0.get("modOrder").toString().compareTo(map1.get("modOrder").toString());
	  return flag; // 不取反，则按正序排列
    }
}
