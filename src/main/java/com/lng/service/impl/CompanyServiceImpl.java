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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.CompanyDao;
import com.lng.dao.GasFactoryCompanyDao;
import com.lng.pojo.Company;
import com.lng.pojo.GasFactoryCompany;
import com.lng.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private GasFactoryCompanyDao gfcDao;

	@Override
	public String saveOrUpdate(Company company) {

		return companyDao.save(company).getId();
	}

	@Override
	public Company getEntityById(String id) {
		if(!id.isEmpty()) {
			Optional<Company> cpy = companyDao.findById(id);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}else {
			return null;
		}
		
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
					pre.getExpressions().add(cb.equal(root.get("companyType").get("id"), typeId));
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

	@SuppressWarnings("serial")
	@Override
	public List<GasFactoryCompany> listCompanyByGfId(String gfId, Integer checkStatus) {
		// TODO Auto-generated method stub
		Specification<GasFactoryCompany> spec = new Specification<GasFactoryCompany>() {

			@Override
			public Predicate toPredicate(Root<GasFactoryCompany> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("id"), gfId));
				}
				if (checkStatus != null && checkStatus > 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				return pre;
			}
		};
		return gfcDao.findAll(spec);
	}

}
