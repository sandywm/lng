package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.RqDevType1Dao;
import com.lng.pojo.RqDevType1;

@Service
public class RqDevType1Service implements com.lng.service.RqDevType1Service {
	@Autowired
	private RqDevType1Dao rqDevType1Dao;

	@Override
	public String saveOrUpdate(RqDevType1 rqDevType1) {

		return rqDevType1Dao.save(rqDevType1).getId();
	}

	@Override
	public RqDevType1 findById(String id) {
		if(!id.isEmpty()) {
			Optional<RqDevType1> rt1 =  rqDevType1Dao.findById(id);
			if(rt1.isPresent()) {
				return rt1.get();
			}
			return null;
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public List<RqDevType1> getRqDevType1ByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<RqDevType1> spec = new Specification<RqDevType1>() {

				@Override
				public Predicate toPredicate(Root<RqDevType1> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
		 return rqDevType1Dao.findAll(spec);
		}
		return rqDevType1Dao.findAll();
	}

}
