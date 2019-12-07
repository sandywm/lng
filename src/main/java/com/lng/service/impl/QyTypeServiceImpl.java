package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.QyTypeDao;
import com.lng.pojo.QyType;
import com.lng.service.QyTypeService;

@Service
public class QyTypeServiceImpl implements QyTypeService {

	@Autowired
	private QyTypeDao qyTypeDao;

	@Override
	public String saveOrUpdate(QyType qyType) {

		return qyTypeDao.save(qyType).getId();
	}

	@Override
	public QyType findById(String id) {
		return qyTypeDao.findById(id).get();
	}


	@SuppressWarnings("serial")
	@Override
	public List<QyType> getQyTypeByNameList(String name) {
		if(!name.isEmpty()) {
			Specification<QyType> spec = new Specification<QyType>() {
				@Override
				public Predicate toPredicate(Root<QyType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
			}};
			return qyTypeDao.findAll(spec);
		}
		return qyTypeDao.findAll();
	}

}
