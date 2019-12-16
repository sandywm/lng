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

import com.lng.dao.GasTypeDao;
import com.lng.pojo.GasType;
import com.lng.service.GasTypeService;

@Service
public class GasTypeServiceImpl implements GasTypeService {

	@Autowired
	private GasTypeDao gTypeDao;

	@Override
	public String saveOrUpdate(GasType gtype) {

		return gTypeDao.save(gtype).getId();
	}

	@Override
	public GasType findById(String id) {
		if (!id.isEmpty()) {
			Optional<GasType> gt = gTypeDao.findById(id);
			if (gt.isPresent()) {
				return gt.get();
			}
			return null;
		} else {
			return null;
		}

	}

	@Override
	public List<GasType> getGasTypeList() {

		return gTypeDao.findAll();
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasType> getGasTypeByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<GasType> spec = new Specification<GasType>() {
				@Override
				public Predicate toPredicate(Root<GasType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return gTypeDao.findAll(spec);
		}
		return null;
	}
}
