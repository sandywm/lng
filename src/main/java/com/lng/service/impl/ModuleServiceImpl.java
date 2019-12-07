package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.ModuleDao;
import com.lng.pojo.Module;
import com.lng.service.ModuleService;
@Service
public class ModuleServiceImpl implements ModuleService{

	@Autowired
	private ModuleDao mDao;
	
	@Override
	public List<Module> listAllInfo() {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.ASC, "modOrder");//升序排列
		return mDao.findAll(sort);
	}

	@Override
	public String addOrUpMod(Module mod) {
		// TODO Auto-generated method stub
		return mDao.save(mod).getId();
	}

	@Override
	public List<Module> listSpecInfoByName(String modName) {
		// TODO Auto-generated method stub
		Specification<Module> spec = new Specification<Module>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!modName.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("modName"), modName));
				}
				return pre;
		}};
		return mDao.findAll(spec);
	}

	@Override
	public Module getEntityById(String modId) {
		// TODO Auto-generated method stub
		Specification<Module> spec = new Specification<Module>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!modId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("id"), modId));
				}
				return pre;
		}};
		List<Module> mList = mDao.findAll(spec);
		if(mList.size() > 0) {
			return mList.get(0);
		}
		return null;
	}

}
