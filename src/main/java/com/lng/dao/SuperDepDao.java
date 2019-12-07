package com.lng.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lng.pojo.SuperDep;

public interface SuperDepDao extends JpaRepository<SuperDep, Object>,JpaSpecificationExecutor<SuperDep> {

}
