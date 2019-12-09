package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.TrucksPotPpDao;
import com.lng.pojo.TrucksPotPp;
import com.lng.service.TrucksPotPpService;

@Service
public class TrucksPotPpServiceImpl implements TrucksPotPpService {
	@Autowired	
	private TrucksPotPpDao trucksPotPpDao;

	@Override
	public String saveOrUpdate(TrucksPotPp trucksPotPp) {
		return trucksPotPpDao.save(trucksPotPp).getId();
	}

	@Override
	public TrucksPotPp findById(String id) {
		return trucksPotPpDao.findById(id).get();
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksPotPp> getTrucksPotPpByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<TrucksPotPp> spec = new Specification<TrucksPotPp>() {

				@Override
				public Predicate toPredicate(Root<TrucksPotPp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return trucksPotPpDao.findAll(spec);
		}
		return trucksPotPpDao.findAll();
	}

}
