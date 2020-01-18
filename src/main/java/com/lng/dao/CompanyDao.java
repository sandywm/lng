package com.lng.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.Company;

public interface CompanyDao extends BaseDao<Company, String> {

	/**
	 * @description 加入公司时获取的公司列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月17日 下午5:03:56
	 * @param cpyTypeId 公司类型编号
	 * @param userId 用户编号
	 * @return
	 */
	@Query(value = " from Company as c where"
			+ " c.companyType.id = ?1 and c.checkStatus = 1 and "
			+ "not exists( from UserCompany as uc where uc.user.id = ?2 and uc.checkStatus = 1 and c.id = uc.company.id)",
		nativeQuery = false
		)
	Page<Company> findPageCpyList(String cpyTypeId,String userId,Pageable pageable);
}
