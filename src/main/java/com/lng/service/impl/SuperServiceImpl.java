package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.SuperDao;
import com.lng.pojo.SuperUser;
import com.lng.service.SuperService;
import com.lng.tools.MD5;

@Service
public class SuperServiceImpl implements SuperService{

	@Autowired
	private SuperDao sDao; 
	
	@Override
	public List<SuperUser> findInfoByOpt(String account, String password) {
		// TODO Auto-generated method stub
		Specification<SuperUser> spec = new Specification<SuperUser>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<SuperUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				pre.getExpressions().add(cb.equal(root.get("account"), account));
				if(!password.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("password"), new MD5().calcMD5(password)));
				}
				return pre;
			}};
		return sDao.findAll(spec);
	}

	@Override
	public SuperUser getEntityById(String superId) {
		// TODO Auto-generated method stub
		if(!superId.isEmpty()) {
			Optional<SuperUser> cpy = sDao.findById(superId);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}

	@Override
	public String addOrUpUser(SuperUser user) {
		// TODO Auto-generated method stub
		return sDao.save(user).getId();
	}

	@Override
	public List<SuperUser> findAllInfo() {
		// TODO Auto-generated method stub
		return sDao.findAll();
	}

	@Override
	public List<SuperUser> findInfoByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return sDao.findInfoByRoleId(roleId);
	}

}
