package com.lng.service;

import java.util.List;

import com.lng.pojo.HqProvinceOrder;

public interface HqProvinceOrderService {

	/**
	 * @description 排序获取海气省份排序规则
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月9日 下午5:16:25
	 * @param orderEng 排序规则（desc,asc）
	 * @return
	 */
	List<HqProvinceOrder> listAllInfo(String orderEng);
	
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
	HqProvinceOrder getEntityByOpt(Integer id,String province);
	
	/**
	 * @description 增加或者修改海气省份排序规则
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月10日 下午1:37:02
	 * @param prov 省份
	 * @return
	 */
	Integer saveOrUpdate(HqProvinceOrder prov);
}
