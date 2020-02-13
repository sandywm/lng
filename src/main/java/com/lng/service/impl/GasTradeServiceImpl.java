package com.lng.service.impl;

import java.util.ArrayList;
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

import com.lng.dao.GasTradeDao;
import com.lng.dao.GasTradeImgDao;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeImg;
import com.lng.service.GasTradeService;

@Service
public class GasTradeServiceImpl implements GasTradeService{

	@Autowired
	private GasTradeDao gtDao;
	@Autowired
	private GasTradeImgDao gtiDao;
	
	@Override
	public String saveOrUpdate(GasTrade gt) {
		// TODO Auto-generated method stub
		return gtDao.save(gt).getId();
	}

	@Override
	public GasTrade getEntityById(String id) {
		// TODO Auto-generated method stub
		if(!id.isEmpty()) {
			Optional<GasTrade> gt = gtDao.findById(id);
			if(gt.isPresent()) {
				return gt.get();
			}
		}
		return null;
	}

	@Override
	public Page<GasTrade> listPageInfoByOpt(String cpyId, String addUserId,String gtId, String gfId, Integer checkStatus,
			Integer showStatus, Integer sPrice, Integer ePrice, String psArea, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub

		Specification<GasTrade> spec = new Specification<GasTrade>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!cpyId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), cpyId));
				}
				if(!addUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), addUserId));
				}
				if(!gtId.isEmpty()) {
					String[] gtIdArr = gtId.split(",");
					if(gtIdArr.length == 1) {
						pre.getExpressions().add(cb.equal(root.get("gasType").get("id"), gtId));
					}else {
						List<Predicate> predicateList = new ArrayList<Predicate>();
						Predicate [] p = new Predicate[gtIdArr.length];
						for(int i = 0 ; i < gtIdArr.length ; i++) {
							predicateList.add(cb.equal(root.get("gasType").get("id"), gtIdArr[i]));
						}
						predicateList.toArray(p);
						pre.getExpressions().add(cb.or(p));
					}
					
				}
				if(!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("id"), gfId));
				}
				if(checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if(showStatus.equals(0) || showStatus.equals(1)) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				if(sPrice >= 0 && ePrice > 0 && sPrice <= ePrice) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("gasPrice"), sPrice.doubleValue()));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("gasPrice"), ePrice.doubleValue()));
				}
				if(!psArea.equals("") && !psArea.equals("全国")) {
					String[] psAreaArr = psArea.split(",");
					List<Predicate> predicateList = new ArrayList<Predicate>();
					Predicate [] p = new Predicate[psAreaArr.length];
					for(int i = 0 ; i < psAreaArr.length ; i++) {
						predicateList.add(cb.like(root.get("psArea"), "%"+psAreaArr[i]+"%"));
					}
					predicateList.toArray(p);
					pre.getExpressions().add(cb.or(p));
				}
				return pre;
		}};
		Sort.Order sort1 = new Sort.Order(Sort.Direction.DESC, "addTime");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.DESC, "hot");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return gtDao.findAll(spec, pageable);
	}

	@Override
	public String saveOrUpdate(GasTradeImg gti) {
		// TODO Auto-generated method stub
		return gtiDao.save(gti).getId();
	}

	@Override
	public List<GasTradeImg> listInfoByGtId(String gtId) {
		// TODO Auto-generated method stub
		Specification<GasTradeImg> spec = new Specification<GasTradeImg>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTradeImg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				return pre;
		}};
		return gtiDao.findAll(spec);
	}

	@Override
	public GasTradeImg getEntityById_1(String id) {
		// TODO Auto-generated method stub
		Optional<GasTradeImg> gt = gtiDao.findById(id);
		if(gt.isPresent()) {
			return gt.get();
		}
		return null;
	}

	@Override
	public void delBatchByGtId(List<GasTradeImg> gtiList) {
		// TODO Auto-generated method stub
		gtiDao.deleteAll(gtiList);
	}

	@Override
	public void addBatchInfo(List<GasTradeImg> gtiList) {
		// TODO Auto-generated method stub
		gtiDao.saveAll(gtiList);
	}

	@Override
	public Page<GasTrade> listInfoByOpt(String sDate, String eDate,Integer checkStatus,
			Integer showStatus, Integer pageNo,Integer pageSize) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<GasTrade> spec = new Specification<GasTrade>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if(checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if(showStatus.equals(0) || showStatus.equals(1)) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
		}};
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return gtDao.findAll(spec, pageable);
	}
	@SuppressWarnings("serial")
	@Override
	public Page<GasTrade> gasTradeOnPublish(String userId, Integer showStatus, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Specification<GasTrade> spec = new Specification<GasTrade>() {
			@Override
			public Predicate toPredicate(Root<GasTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), userId));
				}
				if(showStatus != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
		}};
		return gtDao.findAll(spec, pageable);
	}

	@Override
	public List<GasTrade> listTradingInfoByOpt(String sDate, String eDate, String pubUserId) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<GasTrade> spec = new Specification<GasTrade>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				pre.getExpressions().add(cb.equal(root.get("checkStatus"), 1));
				pre.getExpressions().add(cb.equal(root.get("showStatus"), 0));
				if (!pubUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), pubUserId));
				}
				return pre;
		}};
		return gtDao.findAll(spec, sort);
	}

}
