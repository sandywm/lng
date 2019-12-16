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

import com.lng.dao.CompanyPsrDao;
import com.lng.pojo.CompanyPsr;
import com.lng.service.CompanyPsrService;

@Service
public class CompanyPsrServiceImpl implements CompanyPsrService {

	@Autowired
	private CompanyPsrDao comPsrDao;

	@Override
	public String saveOrUpdate(CompanyPsr comPsr) {

		return comPsrDao.save(comPsr).getId();
	}

	@Override
	public CompanyPsr getEntityById(String id) {
		if(!id.isEmpty()) {
			Optional<CompanyPsr> cpy = comPsrDao.findById(id);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyPsr> getCompanyPsrList(String compId) {
		Specification<CompanyPsr> spec = new Specification<CompanyPsr>() {

			@Override
			public Predicate toPredicate(Root<CompanyPsr> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				return pre;
			}
		};

		return comPsrDao.findAll(spec);
	}

}
