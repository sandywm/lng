package com.lng.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lng.pojo.ModuleAct;

/**
 * 模块下的子动作接口
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月5日 上午8:48:16
 */
public interface ModuleActDao extends JpaRepository<ModuleAct, Object>,JpaSpecificationExecutor<ModuleAct>{

}
