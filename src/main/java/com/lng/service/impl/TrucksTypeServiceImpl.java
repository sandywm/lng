package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.TrucksTypeDao;
import com.lng.pojo.TrucksType;
import com.lng.service.TrucksTypeService;

@Service
public class TrucksTypeServiceImpl implements TrucksTypeService {
	
	@Autowired
	private TrucksTypeDao trucksTypeDao;

	@Override
	public String saveOrUpdate(TrucksType trucksType) {
		return trucksTypeDao.save(trucksType).getId();
	}

	@Override
	public TrucksType findById(String id) {
		return trucksTypeDao.findById(id).get();
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksType> getTrucksTypeByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<TrucksType> spec = new Specification<TrucksType>() {

				@Override
				public Predicate toPredicate(Root<TrucksType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return trucksTypeDao.findAll(spec);
		}
		return trucksTypeDao.findAll();
	}

}
