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

import com.lng.dao.UserCompanyDao;
import com.lng.pojo.UserCompany;
import com.lng.service.UserCompanyService;

@Service
public class UserCompanyServiceImpl implements UserCompanyService {
	@Autowired
	private UserCompanyDao ucDao;

	@Override
	public String addOrUpdate(UserCompany uc) {
		return ucDao.save(uc).getId();
	}

	@Override
	public UserCompany getEntityId(String id) {
		Optional<UserCompany> ucs = ucDao.findById(id);
		if(ucs.isPresent()) {
			return ucs.get();
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<UserCompany> getUserCompanyList(String compId, String userId) {
		Specification<UserCompany> spec = new Specification<UserCompany>() {
			@Override
			public Predicate toPredicate(Root<UserCompany> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!compId.isEmpty()) {
					pre.getExpressions().add((cb.equal(root.get("company").get("id"), compId)));
				}
				if(!userId.isEmpty()) {
					pre.getExpressions().add((cb.equal(root.get("user").get("id"), userId)));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		return ucDao.findAll(spec,sort);
	}


}
