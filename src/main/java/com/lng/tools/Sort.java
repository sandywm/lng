package com.lng.tools;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
/**
 * 模块排序
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月15日 上午8:30:12
 */
public class Sort implements Comparator{

	@Override
	@SuppressWarnings("unchecked")
    public int compare(Object obj0, Object obj1) {
	  Map<String, Integer> map0 = (Map) obj0;
	  Map<String, Integer> map1 = (Map) obj1;
	  int flag = map0.get("modOrder").compareTo(map1.get("modOrder"));
	  return flag; // 不取反，则按正序排列
    }
}
