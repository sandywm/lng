package com.lng.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.SuperUser;

public interface SuperDao  extends BaseDao<SuperUser,Object>{

	/**
	 * @description 根据角色查询用户列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月10日 上午11:53:46
	 * @param roleId
	 * @return
	 */
	@Query("from SuperUser su where EXISTS(select sd.id from SuperDep sd where sd.superUser.id = su.id and sd.department.id = ?1)")
	List<SuperUser> findInfoByRoleId(String roleId);
}
