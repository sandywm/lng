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

import com.lng.dao.ModuleActDao;
import com.lng.pojo.ModuleAct;
import com.lng.service.ModuleActService;
@Service
public class ModuleActServiceImpl implements ModuleActService{

	@Autowired
	private ModuleActDao maDao;
	
	@Override
	public List<ModuleAct> listInfoByModId(String modId) {
		// TODO Auto-generated method stub
		Specification<ModuleAct> spec = new Specification<ModuleAct>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ModuleAct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!modId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("module").get("id"), modId));
				}
				return pre;
		}};
		Sort sort = Sort.by(Sort.Direction.ASC, "actOrder");//升序排列
		return maDao.findAll(spec, sort);
	}

	@Override
	public List<ModuleAct> listInfoByOpt(String modId,String actNameChi,String actNameEng) {
		// TODO Auto-generated method stub
		Specification<ModuleAct> spec = new Specification<ModuleAct>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ModuleAct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!modId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("module").get("id"), modId));
				}
				if(!actNameChi.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("actNameChi"), actNameChi));
				}
				if(!actNameEng.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("actNameEng"), actNameEng));
				}
				return pre;
		}};
		return maDao.findAll(spec);
	}

	@Override
	public String addOrUpModuleAct(ModuleAct ma) {
		// TODO Auto-generated method stub
		return maDao.save(ma).getId();
	}

	@Override
	public ModuleAct getEntityById(String maId) {
		// TODO Auto-generated method stub
		Specification<ModuleAct> spec = new Specification<ModuleAct>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ModuleAct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!maId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("id"), maId));
				}
				return pre;
		}};
		List<ModuleAct> maList = maDao.findAll(spec);
		if(maList.size() > 0) {
			return maList.get(0);
		}
		return null;
	}

}
