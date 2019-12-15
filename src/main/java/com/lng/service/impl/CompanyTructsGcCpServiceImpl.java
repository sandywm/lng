package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.CompanyTructsGcCpDao;
import com.lng.pojo.CompanyTructsGcCp;
import com.lng.service.CompanyTructsGcCpService;

@Service
public class CompanyTructsGcCpServiceImpl implements CompanyTructsGcCpService {
	@Autowired
	private CompanyTructsGcCpDao trucksGcCpDao;

	@Override
	public String saveOrUpdate(CompanyTructsGcCp tGcCp) {
		return trucksGcCpDao.save(tGcCp).getId();
	}

	@Override
	public CompanyTructsGcCp getEntityById(String id) {
		if(!id.isEmpty()) {
			return trucksGcCpDao.findById(id).get();
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyTructsGcCp> getTructsGcCpList(String compId) {

		Specification<CompanyTructsGcCp> spec = new Specification<CompanyTructsGcCp>() {

			@Override
			public Predicate toPredicate(Root<CompanyTructsGcCp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				return pre;
			}
		};
		return trucksGcCpDao.findAll(spec);
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyTructsGcCp> getGcCpByName(String cp) {
		if (!cp.isEmpty()) {
			Specification<CompanyTructsGcCp> spec = new Specification<CompanyTructsGcCp>() {
				@Override
				public Predicate toPredicate(Root<CompanyTructsGcCp> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("trucksGch"), cp));
					return pre;
				}
			};
			return trucksGcCpDao.findAll(spec);
		}
		return null;
	}

}
