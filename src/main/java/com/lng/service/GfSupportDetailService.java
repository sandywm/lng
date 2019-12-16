package com.lng.service;

import com.lng.pojo.GfSupportDetail;

public interface GfSupportDetailService {

	/**
	 * @description 增加或者修改用户点击液厂明细
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午2:26:56
	 * @param gfsd
	 * @return
	 */
	String saveOrUpdate(GfSupportDetail gfsd);
	
	/**
	 * @description 根据用户编号、液厂编号获取用户点击液厂实体
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午2:28:01
	 * @param userId 用户编号
	 * @param gfId 液厂编号
	 * @return
	 */
	GfSupportDetail getEntityByOpt(String userId,String gfId);
}
