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

import com.lng.dao.GasTradeOrderDao;
import com.lng.pojo.GasTradeOrder;
import com.lng.service.GasTradeOrderService;

@Service
public class GasTradeOrderServiceImpl implements GasTradeOrderService {
	@Autowired
	private GasTradeOrderDao gtoDao;

	@Override
	public String addOrUpdate(GasTradeOrder gto) {
		return gtoDao.save(gto).getId();
	}

	@Override
	public GasTradeOrder getEntityById(String id) {
		if(id.equals("")) {
			return null;
		}else {
			Optional<GasTradeOrder> gto = gtoDao.findById(id);
			if (gto.isPresent()) {
				return gto.get();
			}
			return null;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public Page<GasTradeOrder> listPageInfoByOpt(String userId, String compId, String sDate,String eDate, Integer ordSta,
			Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if(ordSta.equals(-2)) {//未完成
					pre.getExpressions().add(cb.lessThan(root.get("orderStatus"), 7));
				}else if(ordSta.equals(-3)) {//全部
					
				}else {
					pre.getExpressions().add(cb.equal(root.get("orderStatus"), ordSta));
				}
				return pre;
			}
		};
		return gtoDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrder> getInfoBygtId(String gtId) {
		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				return pre;
			}
		};
		return gtoDao.findAll(spec);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrder> listComInfoByCpyId(String cpyId) {
		// TODO Auto-generated method stub
		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {
			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!cpyId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("company").get("id"), cpyId));
				}
				pre.getExpressions().add(cb.greaterThan(root.get("orderPjNumber"), 0));
				return pre;
			}
		};
		return gtoDao.findAll(spec);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrder> listComInfoByOpt(String userId, String gtId) {
		// TODO Auto-generated method stub
		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {
			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				if (!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				return pre;
			}
		};
		return gtoDao.findAll(spec);
	}

	@Override
	public void delBatchOrder(List<GasTradeOrder> gtoList) {
		// TODO Auto-generated method stub
		gtoDao.deleteInBatch(gtoList);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrder> listGtInfoByOpt(String gtId,String buyUserId,Integer ordSta,String sDate, String eDate) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "gasTrade.addTime");// 降序排列
		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				if (!buyUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), buyUserId));
				}
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if(ordSta < 1) {//已取消（-2），已拒绝（-1），未确认（0）
					pre.getExpressions().add(cb.lessThan(root.get("orderStatus"), 1));
				}else {
					pre.getExpressions().add(cb.equal(root.get("orderStatus"), ordSta));
				}
				return pre;
			}
		};
		return gtoDao.findAll(spec,sort);
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasTradeOrder> listGtInfoByOpt1(String gtId, String buyUserId, String ordStaStr, String sDate,
			String eDate) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "gasTrade.addTime");// 降序排列
		Specification<GasTradeOrder> spec = new Specification<GasTradeOrder>() {

			@Override
			public Predicate toPredicate(Root<GasTradeOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				if (!buyUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), buyUserId));
				}
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if(!ordStaStr.isEmpty()) {
					String[] orderStatusArr = ordStaStr.split(",");
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("orderStatus"), Integer.parseInt(orderStatusArr[0])));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("orderStatus"), Integer.parseInt(orderStatusArr[1])));
				}
				return pre;
			}
		};
		return gtoDao.findAll(spec,sort);
	}

}
