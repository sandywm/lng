package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.QualificationDao;
import com.lng.pojo.Qualification;
import com.lng.service.QualificationService;

@Service
public class QualificationServiceImpl implements QualificationService {

	@Autowired
	private QualificationDao qualificationDao;

	@Override
	public String  save(Qualification qualification) {
		return qualificationDao.save(qualification).getId();

	}

	@Override
	public void edit(Qualification qualification) {
		qualificationDao.save(qualification);

	}

	@Override
	public void delete(String id) {
		if(!id.isEmpty()) {
			qualificationDao.deleteById(id);
		}
		
	}

	@Override
	public Qualification findById(String id) {
		if(!id.isEmpty()) {
			return qualificationDao.findById(id).get();
		}else {
			return null;
		}
		
	}

	@Override
	public List<Qualification> getQualificationList() {
		return qualificationDao.findAll();
	}

	@SuppressWarnings("serial")
	@Override
	public List<Qualification> getQualByNameList(String name) {
		if(!name.isEmpty()) {
			Specification<Qualification> spec = new Specification<Qualification>() {
				@Override
				public Predicate toPredicate(Root<Qualification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
			}};
			return qualificationDao.findAll(spec);
		}
		return null;
	}

}
