package com.lng.service.impl;

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

import com.lng.dao.CompanyZzDao;
import com.lng.pojo.CompanyZz;
import com.lng.service.CompanyZzService;

@Service
public class CompanyZzServiceImpl implements CompanyZzService {
	
	@Autowired	
	private CompanyZzDao companyZzDao;

	@Override
	public String saveOrUpdate(CompanyZz compZz) {
		return companyZzDao.save(compZz).getId();
	}

	@Override
	public CompanyZz getEntityById(String id) {
		if(!id.isEmpty()) {
			return companyZzDao.findById(id).get();
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public Page<CompanyZz> getCompanyZzList(String compId, Integer pageNo, Integer pageSize) {
		Pageable pageable  = PageRequest.of(pageNo, pageSize);
		Specification<CompanyZz> spec = new Specification<CompanyZz>() {
			@Override
			public Predicate toPredicate(Root<CompanyZz> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}
				return pre;
			}
		};
		
		return companyZzDao.findAll(spec, pageable);
	}
}