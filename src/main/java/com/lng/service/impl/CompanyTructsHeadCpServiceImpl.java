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

import com.lng.dao.CompanyTructsHeadCpDao;
import com.lng.pojo.CompanyTructsHeadCp;
import com.lng.service.CompanyTructsHeadCpService;

@Service
public class CompanyTructsHeadCpServiceImpl implements CompanyTructsHeadCpService {

	@Autowired
	private CompanyTructsHeadCpDao tructsHeadCpDao;

	@Override
	public String saveOrUpdate(CompanyTructsHeadCp tHeadCp) {
		return tructsHeadCpDao.save(tHeadCp).getId();
	}

	@Override
	public CompanyTructsHeadCp getEntityById(String id) {
		if(!id.isEmpty()) {
			Optional<CompanyTructsHeadCp> cpy = tructsHeadCpDao.findById(id);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyTructsHeadCp> getTructsHeadCpList(String compId) {
		Specification<CompanyTructsHeadCp> spec = new Specification<CompanyTructsHeadCp>() {

			@Override
			public Predicate toPredicate(Root<CompanyTructsHeadCp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				return pre;
			}
		};
		return tructsHeadCpDao.findAll(spec);
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyTructsHeadCp> getHeadCpList(String cp) {
		if (!cp.isEmpty()) {
			Specification<CompanyTructsHeadCp> spec = new Specification<CompanyTructsHeadCp>() {
				@Override
				public Predicate toPredicate(Root<CompanyTructsHeadCp> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();

					pre.getExpressions().add(cb.equal(root.get("trucksCp"), cp));
					return pre;
				}
			};
			return tructsHeadCpDao.findAll(spec);
		}
		return null;
	}
}
