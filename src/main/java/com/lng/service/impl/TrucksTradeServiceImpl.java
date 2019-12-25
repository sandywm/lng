package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
			Optional<TrucksTrade> tt = trucksTradeDao.findById(id);
			if(tt.isPresent()) {
				return tt.get();
			}
			return null;
		} else {
			return null;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public Page<TrucksTrade> getTrucksTradeByOption(Integer checkSta, String addUserId, Integer tradeType,Integer showStatus,
			Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
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
				if (showStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		return trucksTradeDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksTrade> listTrucksTradeByOpt(Integer checkSta, Integer showStatus, String sDate, String eDate) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<TrucksTrade> spec = new Specification<TrucksTrade>() {
			@Override
			public Predicate toPredicate(Root<TrucksTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (checkSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				return pre;
			}
		};
		return trucksTradeDao.findAll(spec, sort);
	}

}
