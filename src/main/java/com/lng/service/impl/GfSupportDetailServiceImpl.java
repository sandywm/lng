package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.GfSupportDetailDao;
import com.lng.pojo.GfSupportDetail;
import com.lng.service.GfSupportDetailService;

@Service
public class GfSupportDetailServiceImpl implements GfSupportDetailService{

	@Autowired
	private GfSupportDetailDao gfsdDao;
	
	@Override
	public String saveOrUpdate(GfSupportDetail gfsd) {
		// TODO Auto-generated method stub
		return gfsdDao.save(gfsd).getId();
	}

	@SuppressWarnings("serial")
	@Override
	public GfSupportDetail getEntityByOpt(String userId, String gfId) {
		// TODO Auto-generated method stub
		Specification<GfSupportDetail> spec = new Specification<GfSupportDetail>() {
			@Override
			public Predicate toPredicate(Root<GfSupportDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!gfId.equals("")) {
					pre.getExpressions().add(cb.equal(root.get("gf").get("id"), gfId));
				}
				if(!userId.equals("")) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				return pre;
			}
		};
		List<GfSupportDetail> gfsdList = gfsdDao.findAll(spec);
		if(gfsdList.size() > 0) {
			return gfsdList.get(0);
		}
		return null;
	}

}
