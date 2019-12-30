package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.LngPriceSubDetailDao;
import com.lng.pojo.LngPriceSubDetail;
import com.lng.service.LngPriceSubDetailService;

@Service
public class LngPriceSubDetailServiceImpl implements LngPriceSubDetailService{

	@Autowired
	private LngPriceSubDetailDao lpsdDao;
	
	@Override
	public List<LngPriceSubDetail> listInfoByLpdId(String lpdId,String sDate,String eDate) {
		// TODO Auto-generated method stub
		@SuppressWarnings("serial")
		Specification<LngPriceSubDetail> spec = new Specification<LngPriceSubDetail>() {

			@Override
			public Predicate toPredicate(Root<LngPriceSubDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!lpdId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("lpd").get("id"), lpdId));
				}
				if(!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("priceTime"), sDate + " 00:00:00"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("priceTime"), eDate + " 23:59:59"));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");
		return lpsdDao.findAll(spec, sort);
	}

	@Override
	public String saveOrUpdate(LngPriceSubDetail lpsd) {
		// TODO Auto-generated method stub
		return lpsdDao.save(lpsd).getId();
	}

}
