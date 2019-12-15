package com.lng.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.TrucksTradeDao;
import com.lng.pojo.TrucksTrade;
import com.lng.service.TrucksTradeService;

@Service
public class TrucksTradeServiceImpl implements TrucksTradeService {

	@Autowired
	private TrucksTradeDao trucksTradeDao;

	@Override
	public String saveOrUpdate(TrucksTrade trucksTrade) {
		return trucksTradeDao.save(trucksTrade).getId();
	}

	@Override
	public TrucksTrade getEntityById(String id) {
		if (!id.isEmpty()) {
			return trucksTradeDao.findById(id).get();
		} else {
			return null;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public Page<TrucksTrade> getTrucksTradeByOption(Integer checkSta, String addUserId, Integer tradeType,
			Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Specification<TrucksTrade> spec = new Specification<TrucksTrade>() {
			@Override
			public Predicate toPredicate(Root<TrucksTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (checkSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (!addUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), addUserId));
				}
				if (tradeType != -1) {
					pre.getExpressions().add(cb.equal(root.get("tradeType"), tradeType));
				}
				return pre;
			}
		};
		return trucksTradeDao.findAll(spec, pageable);
	}

}
