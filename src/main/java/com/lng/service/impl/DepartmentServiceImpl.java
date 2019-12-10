package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.DepartmentDao;
import com.lng.pojo.Department;
import com.lng.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	@Autowired
	private DepartmentDao dDao;
	
	@Override
	public Department getEntityById(String depId) {
		// TODO Auto-generated method stub
		if(!depId.isEmpty()) {
			return dDao.findById(depId).get();
		}
		return null;
	}

	@Override
	public String addOrUpDepartment(Department dep) {
		// TODO Auto-generated method stub
		return dDao.save(dep).getId();
	}

	@Override
	public List<Department> findSpecInfo(String depName) {
		// TODO Auto-generated method stub
		Specification<Department> spec = new Specification<Department>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!depName.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("depName"), depName));
				}
				pre.getExpressions().add(cb.equal(root.get("showStatus"), 0));
				return pre;
		}};
		return dDao.findAll(spec);
	}

	@Override
	public void delInfoById(String id) {
		// TODO Auto-generated method stub
		dDao.deleteById(id);
	}

}
