package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.GasFactoryUserHotDao;
import com.lng.pojo.GasFactoryUserHot;
import com.lng.service.GasFactoryUserHotService;

@Service
public class GasFactoryUserHotServiceImpl implements GasFactoryUserHotService{

	@Autowired
	private GasFactoryUserHotDao gfuhDao;
	@Override
	public String addOrUpdate(GasFactoryUserHot gfuh) {
		// TODO Auto-generated method stub
		return gfuhDao.save(gfuh).getId();
	}

	@SuppressWarnings("serial")
	@Override
	public List<GasFactoryUserHot> listInfoByOpt(String userId, String addTime,String gfId) {
		// TODO Auto-generated method stub
		Specification<GasFactoryUserHot> spec = new Specification<GasFactoryUserHot>() {
			@Override
			public Predicate toPredicate(Root<GasFactoryUserHot> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!userId.equals("")) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				if(!addTime.equals("")) {
					pre.getExpressions().add(cb.equal(root.get("addTime"), addTime));
				}
				if(!gfId.equals("")) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("id"), gfId));
				}
				return pre;
			}
		};
		return gfuhDao.findAll(spec);
	}

}
