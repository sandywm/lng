package com.lng.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.GasFactory;

public interface GasFactoryDao extends BaseDao<GasFactory, Object> {

	/**
	 * @description 按照省份全国液厂数量（审核通过）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月28日 下午2:46:05
	 * @return
	 */
	@Query(value = "select gf.province,count(gf.id) from  GasFactory gf where gf.checkStatus = 1"
			+ " group by gf.province",
		nativeQuery = false
		)
	List<Object> findTjInfo();
	
	/**
	 * @description 分页获取当前用户（必须是创建贸易商的人）未加入液厂的液厂(审核通过)列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月28日 下午2:46:05
	 * @param userId 用户编号
	 * @return
	 */
	@Query(value = " from  GasFactory gf where gf.checkStatus = 1"
			+ " and not exists ( from GasFactoryCompany as gfc where gfc.addUserId = ?1"
			+ " and gfc.gasFactory.id = gf.id and gfc.checkStatus = 1)",
		nativeQuery = false
		)
	Page<GasFactory> findUnJoinGasFactoryList(String userId,Pageable pageable);
}
