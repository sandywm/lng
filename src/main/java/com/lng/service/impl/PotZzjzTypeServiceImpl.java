package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.PotZzjzTypeDao;
import com.lng.pojo.PotZzjzType;
import com.lng.service.PotZzjzTypeService;

@Service
public class PotZzjzTypeServiceImpl implements PotZzjzTypeService {
	
	@Autowired
	private PotZzjzTypeDao potZzjzTypeDao;
	

	@Override
	public String saveOrUpdate(PotZzjzType potZzJzType) {
		
		return potZzjzTypeDao.save(potZzJzType).getId();
	}

	@Override
	public PotZzjzType findById(String id) {
		if(!id.isEmpty()) {
			Optional<PotZzjzType>  pt = potZzjzTypeDao.findById(id);
			if(pt.isPresent()) {
				return pt.get();
			}
			return null;
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("serial")
	@Override
	public List<PotZzjzType> getPotZzjzTypeByNameList(String name) {
		if(!name.isEmpty()) {
			Specification<PotZzjzType> spec = new Specification<PotZzjzType>() {
				@Override
				public Predicate toPredicate(Root<PotZzjzType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
			}};
			return potZzjzTypeDao.findAll(spec);
		}
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 同一天价格时间降序排列
		return potZzjzTypeDao.findAll(sort);
	}

}
