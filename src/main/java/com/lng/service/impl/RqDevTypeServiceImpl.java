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

import com.lng.dao.RqDevTypeDao;
import com.lng.pojo.RqDevType;
import com.lng.service.RqDevTypeService;

@Service
public class RqDevTypeServiceImpl implements RqDevTypeService {

	@Autowired
	private RqDevTypeDao rqDevTypeDao;

	@Override
	public String saveOrUpdate(RqDevType rqDevType) {
		return rqDevTypeDao.save(rqDevType).getId();
	}

	@Override
	public RqDevType findById(String id) {
		if(!id.isEmpty()) {
			Optional<RqDevType> rt =  rqDevTypeDao.findById(id);
			if(rt.isPresent()) {
				return rt.get();
			}
			return null;
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public List<RqDevType> getRqDevTypeByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<RqDevType> spec = new Specification<RqDevType>() {

				@Override
				public Predicate toPredicate(Root<RqDevType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return rqDevTypeDao.findAll(spec);
		}
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 同一天价格时间降序排列
		return rqDevTypeDao.findAll(sort);
	}

}
