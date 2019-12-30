package com.lng.dao;

import java.util.List;

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
}
