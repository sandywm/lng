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

import com.lng.dao.UserDao;
import com.lng.pojo.User;
import com.lng.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao uDao;
	
	@Override
	public String saveAndUpdate(User user) {
		// TODO Auto-generated method stub
		return uDao.save(user).getId();
	}

	@Override
	public User getEntityById(String userId) {
		// TODO Auto-generated method stub
		if(!userId.equals("")) {
			Optional<User> user = uDao.findById(userId);
			if(user.isPresent()) {
				return user.get();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public Page<User> listPageInfoByWxName(String wxName, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Specification<User> spec = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!wxName.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("wxName"), "%"+wxName+"%"));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "signDate");// 降序排列
		Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
		return uDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public User getEntityByWxOpenId(String wxOpenId) {
		// TODO Auto-generated method stub
		Specification<User> spec = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!wxOpenId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("account"), wxOpenId));
				}
				return pre;
			}
		};
		List<User> uList = uDao.findAll(spec);
		if(uList.size() > 0) {
			return uList.get(0);
		}
		return null;
	}

}
