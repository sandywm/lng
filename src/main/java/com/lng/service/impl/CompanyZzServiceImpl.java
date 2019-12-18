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
			Optional<CompanyZz> cpy = companyZzDao.findById(id);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public List<CompanyZz> getCompanyZzList(String compId) {
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
		
		return companyZzDao.findAll(spec);
	}

	@Override
	public void saveOrUpdateBatch(List<CompanyZz> zzList) {
		companyZzDao.saveAll(zzList);
	}

	@Override
	public void deleteBatch(List<CompanyZz> zzList) {
		
		companyZzDao.deleteInBatch(zzList);
	}
}
