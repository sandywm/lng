package com.lng.service.impl;

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

import com.lng.dao.RqDevTradeDao;
import com.lng.pojo.RqDevTrade;
import com.lng.service.RqDevTradeService;

@Service
public class RqDevTradeServiceImpl implements RqDevTradeService {
	@Autowired
	private RqDevTradeDao rqDevTradeDao;

	@Override
	public String saveOrUpdate(RqDevTrade rqDevTrade) {
		return rqDevTradeDao.save(rqDevTrade).getId();
	}

	@Override
	public RqDevTrade getEntityById(String id) {
		if (!id.isEmpty()) {
			return rqDevTradeDao.findById(id).get();
		} else {
			return null;
		}

	}

	@SuppressWarnings("serial")
	@Override
	public Page<RqDevTrade> getRqDevTradeList(String compId, String lmId, String zlId, Integer checkSta,
			Integer showSta, String addUserId, Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");//降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<RqDevTrade> spec = new Specification<RqDevTrade>() {

			@Override
			public Predicate toPredicate(Root<RqDevTrade> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				if (!lmId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("rqDevType").get("id"), lmId));
				}
				if (!zlId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("rqDevType1").get("id"), zlId));
				}
				if (checkSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showSta));
				}
				if (!addUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId").get("id"), addUserId));
				}
				return pre;
			}
		};
		return rqDevTradeDao.findAll(spec, pageable);
	}

}
