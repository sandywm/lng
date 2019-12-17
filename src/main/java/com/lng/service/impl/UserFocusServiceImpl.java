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

import com.lng.dao.UserFocusDao;
import com.lng.pojo.UserFocus;
import com.lng.service.UserFocusService;

@Service
public class UserFocusServiceImpl implements UserFocusService {
	@Autowired
	private UserFocusDao ufDao;

	@Override
	public String addOrUpdage(UserFocus uf) {
		return ufDao.save(uf).getId();
	}

	@Override
	public UserFocus getEntityId(String id) {
		Optional<UserFocus> ufs = ufDao.findById(id);
		if (ufs.isPresent()) {
			return ufs.get();
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<UserFocus> getUserFocusList(String userId, String focusType) {
		Specification<UserFocus> spec = new Specification<UserFocus>() {
			@Override
			public Predicate toPredicate(Root<UserFocus> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!userId.isEmpty()) {
					pre.getExpressions().add((cb.equal(root.get("user").get("id"), userId)));
				}
				if(!focusType.isEmpty()) {
					pre.getExpressions().add((cb.equal(root.get("focusType"), focusType)));
				}
				return pre;
			}
		};
		return ufDao.findAll(spec);
	}

	@Override
	public void delete(String id) {
		ufDao.deleteById(id);
	}
}
