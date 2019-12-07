package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.CompanyTypeDao;
import com.lng.pojo.CompanyType;
import com.lng.service.CompanyTypeService;

@Service
public class CompanyTypeServiceImpl implements CompanyTypeService {
	@Autowired
	private CompanyTypeDao cTypeDao;

	@Override
	public String saveOrUpdate(CompanyType ctype) {
		return cTypeDao.save(ctype).getId();
	}

	@Override
	public CompanyType findById(String id) {
		
		return cTypeDao.findById(id).get();
	}

	@Override
	public List<CompanyType> getCompanyTypeList() {
		
		return cTypeDao.findAll();
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyType> getCompanyTypeByNameList(String name) {
		if(!name.isEmpty()) {
			Specification<CompanyType> spec = new Specification<CompanyType>() {

				@Override
				public Predicate toPredicate(Root<CompanyType> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}};
				return cTypeDao.findAll(spec); 
		}
		return null;
	}

}
