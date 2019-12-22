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

import com.lng.dao.LngPriceDetailDao;
import com.lng.pojo.LngPriceDetail;
import com.lng.service.LngPriceDetailService;

@Service
public class LngPriceDetailServiceImpl implements LngPriceDetailService{

	@Autowired
	private LngPriceDetailDao lpdDao;
	
	@Override
	public String addOrUpdate(LngPriceDetail lpd) {
		// TODO Auto-generated method stub
		return lpdDao.save(lpd).getId();
	}

	@Override
	public void saveBatch(List<LngPriceDetail> lpdList) {
		// TODO Auto-generated method stub
		lpdDao.saveAll(lpdList);
	}

	@Override
	public List<LngPriceDetail> listInfoByOpt(String gfId, Integer price, String priceDate) {
		// TODO Auto-generated method stub
		Specification<LngPriceDetail> spec = new Specification<LngPriceDetail>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<LngPriceDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gf").get("id"), gfId));
				}
				if(price > 0) {
					pre.getExpressions().add(cb.equal(root.get("price"), price));
				}
				if(!priceDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("priceTime"), priceDate + " 00:00:00"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("priceTime"), priceDate + " 23:59:59"));
				}
				return pre;
		}};
		Sort sort = Sort.by(Sort.Direction.DESC, "priceTime");//同一天价格时间降序排列
		return lpdDao.findAll(spec,sort);
	}

	@Override
	public List<LngPriceDetail> listInfoByOpt(String provPy, String gsId, String gsNamePy, String sDate, String eDate,String orderStr) {
		// TODO Auto-generated method stub
		Specification<LngPriceDetail> spec = new Specification<LngPriceDetail>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<LngPriceDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!provPy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("gf").get("provincePy"), "%"+provPy+"%"));
				}else if(!gsId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gf").get("id"), gsId));
				}else if(!gsNamePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("gf").get("namePy"), "%"+gsNamePy+"%"));
				}
				if(!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("priceTime"), sDate + " 00:00:00"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("priceTime"), eDate + " 23:59:59"));
				}
				return pre;
		}};
		if(orderStr.isEmpty()) {
			return lpdDao.findAll(spec);
		}else {
			Sort sort = null;
			if(orderStr.equals("asc")) {
				sort = Sort.by(Sort.Direction.ASC, "priceTime");//同一天价格时间升序排列
			}else if(orderStr.equals("desc")) {
				sort = Sort.by(Sort.Direction.DESC, "priceTime");//同一天价格时间升序排列
			}
			return lpdDao.findAll(spec,sort);
		}
	}

	@Override
	public LngPriceDetail getEntityById(String lpdId) {
		// TODO Auto-generated method stub
		if(!lpdId.isEmpty()) {
			Optional<LngPriceDetail> cpy = lpdDao.findById(lpdId);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}
}
