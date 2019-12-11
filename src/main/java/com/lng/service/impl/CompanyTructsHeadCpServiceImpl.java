package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		if (!id.isEmpty()) {
			return tructsHeadCpDao.findById(id).get();
		} else {
			return null;
		}

	}

	@SuppressWarnings("serial")
	@Override
	public Page<CompanyTructsHeadCp> getTructsHeadCpList(String compId, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
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
		return tructsHeadCpDao.findAll(spec, pageable);
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
