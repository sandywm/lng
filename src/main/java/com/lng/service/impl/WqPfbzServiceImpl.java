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

import com.lng.dao.WqPfbzDao;
import com.lng.pojo.WqPfbz;
import com.lng.service.WqPfbzService;

@Service
public class WqPfbzServiceImpl implements WqPfbzService {
	@Autowired	
	private WqPfbzDao wqPfbzDao;

	@Override
	public String saveOrUpdate(WqPfbz wqPfbz) {
		return wqPfbzDao.save(wqPfbz).getId();
	}

	@Override
	public WqPfbz findById(String id) {
		if(!id.isEmpty()) {
			Optional<WqPfbz> wqpf = wqPfbzDao.findById(id);
			if(wqpf.isPresent()) {
				return wqpf.get();
			}
			return null;
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public List<WqPfbz> getWqPfbzByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<WqPfbz> spec = new Specification<WqPfbz>() {

				@Override
				public Predicate toPredicate(Root<WqPfbz> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return wqPfbzDao.findAll(spec);
		}
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 同一天价格时间降序排列
		return wqPfbzDao.findAll(sort);
	}

}
