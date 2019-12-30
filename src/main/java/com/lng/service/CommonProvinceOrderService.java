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
	
	/**
	 * @description 根据省份模糊查询（批量导入时使用）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月29日 下午4:10:24
	 * @param province
	 * @return
	 */
	CommonProvinceOrder getEntityByProv(String province);
}
