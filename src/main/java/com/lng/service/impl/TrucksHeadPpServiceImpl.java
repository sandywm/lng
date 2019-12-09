package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.TrucksHeadPpDao;
import com.lng.pojo.TrucksHeadPp;
import com.lng.service.TrucksHeadPpService;

@Service
public class TrucksHeadPpServiceImpl implements TrucksHeadPpService {
	@Autowired
	private TrucksHeadPpDao trucksHeadPpDao;

	@Override
	public String saveOrUpdate(TrucksHeadPp trucksHeadPp) {

		return trucksHeadPpDao.save(trucksHeadPp).getId();
	}

	@Override
	public TrucksHeadPp findById(String id) {

		return trucksHeadPpDao.findById(id).get();
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksHeadPp> getTrucksHeadPpByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<TrucksHeadPp> spec = new Specification<TrucksHeadPp>() {
				@Override
				public Predicate toPredicate(Root<TrucksHeadPp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return trucksHeadPpDao.findAll(spec);
		}
		return trucksHeadPpDao.findAll();
	}

}
