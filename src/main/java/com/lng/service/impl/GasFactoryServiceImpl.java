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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.GasFactoryDao;
import com.lng.pojo.GasFactory;
import com.lng.service.GasFactoryService;

@Service
public class GasFactoryServiceImpl implements GasFactoryService{

	@Autowired
	private GasFactoryDao gfDao;
	
	@Override
	public String addOrUpGasFactory(GasFactory gs) {
		// TODO Auto-generated method stub
		return gfDao.save(gs).getId();
	}

	@Override
	public List<GasFactory> listInfoByOpt(String name, String namePy, String gasTypeId, String province, String city,
			String county, Integer checkStatus) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!name.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("name"), "%"+name+"%"));
				}
				if(!namePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("namePy"), "%"+namePy+"%"));
				}
				if(!gasTypeId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasType").get("id"), gasTypeId));
				}
				if(!province.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("province"), province));
				}
				if(!city.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("city"), city));
				}
				if(!county.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("county"), county));
				}
				if(checkStatus == null || checkStatus == -1) {
					
				}else {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				return pre;
		}};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");//降序排列
		return gfDao.findAll(spec, sort);
	}

	@Override
	public Page<GasFactory> listPageInfoByOpt(String name, String namePy, String gasTypeId, String province,
			String city, String county, Integer checkStatus, String owerUserId,Integer pageIndex,Integer pageSize) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!name.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("name"), "%"+name+"%"));
				}
				if(!namePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("namePy"), "%"+namePy+"%"));
				}
				if(!gasTypeId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasType").get("id"), gasTypeId));
				}
				if(!province.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("province"), province));
				}
				if(!city.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("city"), city));
				}
				if(!county.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("county"), county));
				}
				if(checkStatus == null || checkStatus == -1) {
					
				}else {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if(!owerUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("owerUserId"), owerUserId));
				}
				return pre;
		}};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");//降序排列
		Pageable pageable = PageRequest.of(pageIndex-1, pageSize, sort);
		return gfDao.findAll(spec, pageable);
	}

	@Override
	public GasFactory getEntityById(String gfId) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("id"), gfId));
				}
				return pre;
		}};
		List<GasFactory> gfList = gfDao.findAll(spec);
		if(gfList.size() > 0) {
			return gfList.get(0);
		}
		return null;
	}

}
