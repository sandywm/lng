package com.lng.service;

import java.util.List;

import com.lng.pojo.CommonProvinceOrder;

public interface CommonProvinceOrderService {

	/**
	 * @description 排序获取省份排序规则
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月9日 下午5:16:25
	 * @param orderEng 排序规则（desc,asc）
	 * @return
	 */
	List<CommonProvinceOrder> listAllInfo(String orderEng);
	
	/**
	 * @description 根据条件获取实体信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月9日 下午5:17:51
	 * @param id 主键（0时不查询）
	 * @param province （""时不查询）
	 * @return
	 */
	CommonProvinceOrder getEntityByOpt(Integer id,String province);
	
	/**
	 * @description 修改或者保存省份排序
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月10日 上午8:55:21
	 * @param pro
	 * @return
	 */
	Integer saveOrUpdate(CommonProvinceOrder pro);
}
