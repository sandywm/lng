package com.lng.service.impl;

import java.util.ArrayList;
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
	public List<GasFactory> listInfoByOpt(String name, String namePy, String gasTypeId, String province, String provincePy,
			Integer checkStatus) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!name.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("name"), name));
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
				if(!provincePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("provincePy"), "%"+provincePy+"%"));
				}
				if(checkStatus > -1) {
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
			String provincePy, Integer checkStatus, String owerUserId,Integer opt,Integer pageIndex,Integer pageSize) {
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
					pre.getExpressions().add(cb.like(root.get("province"), "%"+province+"%"));
				}
				if(!provincePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("provincePy"), "%"+provincePy+"%"));
				}
				if(checkStatus > -1) {
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
			Optional<GasFactory> cpy = gfDao.findById(gfId);
			if(cpy.isPresent()) {
				return cpy.get();
			}
			return null;
		}
		return null;
	}

	@Override
	public List<GasFactory> listInfoByOpt(String provPy, String gsId, String gasTypeId,String gsNamePy,Integer checkStatus) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!provPy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("provincePy"), "%"+provPy+"%"));
				}
				if(!gsId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("id"), gsId));
				}
				if(!gasTypeId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasType").get("id"), gasTypeId));
				}
				if(!gsNamePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("namePy"), "%"+gsNamePy+"%"));
				}
				if(checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				return pre;
		}};
//		Sort sort = Sort.by(Sort.Direction.ASC, "orderNo");//液厂升序排列
		Sort.Order sort1 = new Sort.Order(Sort.Direction.ASC, "orderNo");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.ASC, "orderSubNo");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
		return gfDao.findAll(spec,sort);
	}

	@Override
	public Page<GasFactory> listInfoByOpt(String provName, String gtId, String gsNamePy,Integer pageIndex,Integer pageSize) {
		// TODO Auto-generated method stub
		Specification<GasFactory> spec = new Specification<GasFactory>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasFactory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!provName.isEmpty()) {
					String[] provPyArr = provName.split(",");
					if(provPyArr.length > 1) {
						List<Predicate> predicateList = new ArrayList<Predicate>();
						Predicate [] p = new Predicate[provPyArr.length];
						for(int i = 0 ; i < provPyArr.length ; i++) {
							predicateList.add(cb.equal(root.get("province"), provPyArr[i]));
						}
						predicateList.toArray(p);
						pre.getExpressions().add(cb.or(p));
					}else {
						pre.getExpressions().add(cb.equal(root.get("province"), provName));
					}
				}
				if(!gtId.isEmpty()) {
					String[] gtIdArr = gtId.split(",");
					if(gtIdArr.length > 1) {
						List<Predicate> predicateList = new ArrayList<Predicate>();
						Predicate [] p = new Predicate[gtIdArr.length];
						for(int i = 0 ; i < gtIdArr.length ; i++) {
							predicateList.add(cb.equal(root.get("gasType").get("id"), gtIdArr[i]));
						}
						predicateList.toArray(p);
						pre.getExpressions().add(cb.or(p));
					}else {
						pre.getExpressions().add(cb.equal(root.get("gasType").get("id"), gtId));
					}
				}
				if(!gsNamePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("namePy"), "%"+gsNamePy+"%"));
				}
				return pre;
		}};
		Sort.Order sort1 = new Sort.Order(Sort.Direction.ASC, "orderNo");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.ASC, "orderSubNo");//升序排列
		Sort.Order sort3 = new Sort.Order(Sort.Direction.DESC, "hot");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		list.add(sort3);
		Sort sort = Sort.by(list);
		Pageable pageable = PageRequest.of(pageIndex-1, pageSize, sort);
		return gfDao.findAll(spec, pageable);
	}

	@Override
	public List<Object> getTjInfo() {
		// TODO Auto-generated method stub
		return gfDao.findTjInfo();
	}

}
