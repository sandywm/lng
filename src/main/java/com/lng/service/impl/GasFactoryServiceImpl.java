package com.lng.service.impl;

import java.util.ArrayList;
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
		Sort.Order sort1 = new Sort.Order(Sort.Direction.ASC, "orderNo");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.ASC, "orderSubNo");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
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
		Sort.Order sort1 = new Sort.Order(Sort.Direction.ASC, "orderNo");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.ASC, "orderSubNo");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
		Pageable pageable = PageRequest.of(pageIndex-1, pageSize, sort);
		return gfDao.findAll(spec, pageable);
	}

	@Override
	public GasFactory getEntityById(String gfId) {
		// TODO Auto-generated method stub
		if(!gfId.isEmpty()) {
			return gfDao.findById(gfId).get();
		}else {
			return null;
		}
	}

}
