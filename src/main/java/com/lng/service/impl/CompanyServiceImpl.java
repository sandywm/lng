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

import com.lng.dao.CompanyDao;
import com.lng.pojo.Company;
import com.lng.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Override
	public String saveOrUpdate(Company company) {

		return companyDao.save(company).getId();
	}

	@Override
	public Company getEntityById(String id) {
		return companyDao.findById(id).get();
	}

	@SuppressWarnings("serial")
	@Override
	public Page<Company> getCompanyList(String name, String typeId, int checkSta, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Specification<Company> spec = new Specification<Company>() {

			@Override
			public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!name.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("name"), "%" + name + "%"));
				}
				if (!typeId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("CompanyType").get("id"), typeId));
				}
				if (checkSta != -1) { //checkSta  等于 -1  全部
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				return pre;
			}
		};
		return companyDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<Company> getCompanyByName(String name) {
		if (!name.isEmpty()) {
			Specification<Company> spec = new Specification<Company>() {

				@Override
				public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return companyDao.findAll(spec);
		}
		return null;
	}

}
