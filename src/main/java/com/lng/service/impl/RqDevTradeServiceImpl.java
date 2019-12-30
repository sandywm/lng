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
			Optional<RqDevTrade> rt = rqDevTradeDao.findById(id);
			if(rt.isPresent()) {
				return rt.get();
			}
			return null;
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
					pre.getExpressions().add(cb.equal(root.get("addUserId"), addUserId));
				}
				return pre;
			}
		};
		return rqDevTradeDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public Page<RqDevTrade> rqDevTradeOnPublish(String userId, Integer showStatus, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Specification<RqDevTrade> spec = new Specification<RqDevTrade>() {
			@Override
			public Predicate toPredicate(Root<RqDevTrade> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), userId));
				}
				if (showStatus != -1) { 
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		return rqDevTradeDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<RqDevTrade> listInfoByOpt(String sDate, String eDate, Integer checkSta, Integer showSta) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");//降序排列
		Specification<RqDevTrade> spec = new Specification<RqDevTrade>() {

			@Override
			public Predicate toPredicate(Root<RqDevTrade> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if (checkSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showSta != -1) { 
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showSta));
				}
				return pre;
			}
		};
		return rqDevTradeDao.findAll(spec, sort);
	}

}
