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

import com.lng.dao.GasFactoryCompanyDao;
import com.lng.pojo.CompanyPsr;
import com.lng.pojo.GasFactoryCompany;
import com.lng.service.GasFactoryCompanyService;

@Service
public class GasFactoryCompanyServiceImpl implements GasFactoryCompanyService{

	@Autowired
	private GasFactoryCompanyDao gfcDao;
	
	@SuppressWarnings("serial")
	@Override
	public List<GasFactoryCompany> listCompanyByGfId(String gfId, String cpyId, Integer checkStatus) {
		// TODO Auto-generated method stub
		Specification<GasFactoryCompany> spec = new Specification<GasFactoryCompany>() {

			@Override
			public Predicate toPredicate(Root<GasFactoryCompany> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("id"), gfId));
				}
				if (!cpyId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), cpyId));
				}
				if (checkStatus != null && checkStatus > 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				return pre;
			}
		};
		return gfcDao.findAll(spec);
	}

	@Override
	public String saveOrUpdate(GasFactoryCompany gfc) {
		// TODO Auto-generated method stub
		return gfcDao.save(gfc).getId();
	}

	@Override
	public GasFactoryCompany getEntityById(String id) {
		// TODO Auto-generated method stub
		if(!id.isEmpty()) {
			Optional<GasFactoryCompany> cpy = gfcDao.findById(id);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}
}
