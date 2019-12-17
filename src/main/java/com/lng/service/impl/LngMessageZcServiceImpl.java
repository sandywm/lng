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

import com.lng.dao.LngMessageZcDao;
import com.lng.pojo.LngMessageUserZc;
import com.lng.service.LngMessageZcService;

@Service
public class LngMessageZcServiceImpl implements LngMessageZcService {
	@Autowired	
	private LngMessageZcDao zcDao;

	@Override
	public String addLmZc(LngMessageUserZc lmZc) {
		return zcDao.save(lmZc).getId();
	}

	@SuppressWarnings("serial")
	@Override
	public List<LngMessageUserZc> getLmZc(String userId, String msgId) {
		Specification<LngMessageUserZc> spec = new Specification<LngMessageUserZc>() {
			@Override
			public Predicate toPredicate(Root<LngMessageUserZc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				if (!msgId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("lngMessage").get("id"), msgId));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		return zcDao.findAll(spec,sort);
	}

}
