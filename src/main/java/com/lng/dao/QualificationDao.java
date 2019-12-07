package com.lng.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lng.pojo.Qualification;

public interface QualificationDao extends JpaRepository<Qualification, Object>,JpaSpecificationExecutor<Qualification>{

}
