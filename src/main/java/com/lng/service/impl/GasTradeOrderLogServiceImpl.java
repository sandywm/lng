package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.GasTradeOrderLogDao;
import com.lng.pojo.GasTradeOrderLog;
import com.lng.service.GasTradeOrderLogService;

@Service
public class GasTradeOrderLogServiceImpl implements GasTradeOrderLogService {
	@Autowired
	private GasTradeOrderLogDao gtolDao;

	@Override
	public String addOrUpdate(GasTradeOrderLog gtoLog) {

		return gtolDao.save(gtoLog).getId();
	}

	@Override
	public GasTradeOrderLog getEntityById(String id) {
		Optional<GasTradeOrderLog> gtol = gtolDao.findById(id);
		if (gtol.isPresent()) {
			return gtol.get();
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrderLog> getGtLogList(String gtoId) {
		Specification<GasTradeOrderLog> spec = new Specification<GasTradeOrderLog>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrderLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gtoId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTradeOrder").get("id"), gtoId));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.ASC, "addTime");// 时间升序排列
		return gtolDao.findAll(spec,sort);
	}

	@Override
	public void delBatchLog(List<GasTradeOrderLog> logList) {
		// TODO Auto-generated method stub
		gtolDao.deleteInBatch(logList);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrderLog> getGtLogList(String gtoId, Integer orderStatus) {
		// TODO Auto-generated method stub
		Specification<GasTradeOrderLog> spec = new Specification<GasTradeOrderLog>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrderLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gtoId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTradeOrder").get("id"), gtoId));
				}
				pre.getExpressions().add(cb.equal(root.get("orderStatus"), orderStatus));
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 时间降序排列
		return gtolDao.findAll(spec,sort);
	}

}
