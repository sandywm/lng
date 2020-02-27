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

import com.lng.dao.PotTradeDao;
import com.lng.pojo.PotTrade;
import com.lng.service.PotTradeService;

@Service
public class PotTradeServiceImpl implements PotTradeService {
	@Autowired
	private PotTradeDao potTradeDao;

	@Override
	public String saveOrUpdate(PotTrade potTrade) {
		return potTradeDao.save(potTrade).getId();
	}

	@Override
	public PotTrade getEntityById(String id) {
		if (!id.isEmpty()) {
			Optional<PotTrade> pt = potTradeDao.findById(id);
			if (pt.isPresent()) {
				return pt.get();
			}
			return null;
		} else {
			return null;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public Page<PotTrade> getPotTradeByOption(String potPpId, Integer potVol, String sxInfo, String zzjzTypeId,
			Integer checkSta,Integer showStatus,Integer tradeStatus, Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<PotTrade> spec = new Specification<PotTrade>() {
			@Override
			public Predicate toPredicate(Root<PotTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!potPpId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("trucksPotPp").get("id"), potPpId));
				}
				if (potVol != -1) {
					pre.getExpressions().add(cb.equal(root.get("potVolume"), potVol));
				}
				if (!sxInfo.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("sxInfo"), sxInfo));
				}
				if (!zzjzTypeId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("potZzjzType").get("id"), zzjzTypeId));
				}
				if (checkSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				if (tradeStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("tradeStatus"), tradeStatus));
				}
				return pre;
			}
		};
		return potTradeDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public Page<PotTrade> potTradeOnPublish(String userId, Integer showStatus, Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<PotTrade> spec = new Specification<PotTrade>() {
			@Override
			public Predicate toPredicate(Root<PotTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
		return potTradeDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<PotTrade> listPotTradeByOpt(String sDate, String eDate, Integer checkSta, Integer showStatus) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<PotTrade> spec = new Specification<PotTrade>() {
			@Override
			public Predicate toPredicate(Root<PotTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if (checkSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		return potTradeDao.findAll(spec, sort);
	}
}
