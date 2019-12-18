package com.lng.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.GasTrade;

public interface GasTradeDao  extends BaseDao<GasTrade, Object> {

	@Query(value = "from GasTrade gt"
		+ " where (gt.company.id = ?1 and ?1 != '')"
		+ " where (gt.addUserId = ?2 and ?2 != '')"
		+ " where (gt.gasType.id = ?3 and ?3 != '')"
		+ " where (gt.gasFactory.id = ?4 and ?4 != '')"
		+ " where (gt.checkStatus = ?5 and ?5 > 0)"
		+ " order by gt.addTime desc",
		nativeQuery = true
	)
	Page<GasTrade> findPageInfoByOpt(String cpyId,String addUserId,String gasTypeId,String gfId,Integer checkStatus,Pageable pageable);
//	Page<GasTrade> findPageInfoByOpt(String cpyId,String addUserId,String gasTypeId,String gfId,Integer checkStatus,
//			Integer showStatus,Integer sPrice,Integer ePrice,String psArea,Pageable pageable);
}
