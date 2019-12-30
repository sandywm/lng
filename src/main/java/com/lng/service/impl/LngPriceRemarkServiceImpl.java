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

import com.lng.dao.LngPriceRemarkDao;
import com.lng.pojo.LngPriceRemark;
import com.lng.service.LngPriceRemarkService;

@Service
public class LngPriceRemarkServiceImpl implements LngPriceRemarkService{

	@Autowired
	private LngPriceRemarkDao lprDao;
	
	@Override
	public List<LngPriceRemark> listInfoByGfId(String gfId) {
		// TODO Auto-generated method stub
		@SuppressWarnings("serial")
		Specification<LngPriceRemark> spec = new Specification<LngPriceRemark>() {

			@Override
			public Predicate toPredicate(Root<LngPriceRemark> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gf").get("id"), gfId));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");
		return lprDao.findAll(spec, sort);
	}

	@Override
	public String saveOrUpdate(LngPriceRemark lpr) {
		// TODO Auto-generated method stub
		return lprDao.save(lpr).getId();
	}

}
