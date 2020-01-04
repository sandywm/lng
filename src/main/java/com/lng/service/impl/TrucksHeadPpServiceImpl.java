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

import com.lng.dao.TrucksHeadPpDao;
import com.lng.pojo.TrucksHeadPp;
import com.lng.service.TrucksHeadPpService;

@Service
public class TrucksHeadPpServiceImpl implements TrucksHeadPpService {
	@Autowired
	private TrucksHeadPpDao trucksHeadPpDao;

	@Override
	public String saveOrUpdate(TrucksHeadPp trucksHeadPp) {

		return trucksHeadPpDao.save(trucksHeadPp).getId();
	}

	@Override
	public TrucksHeadPp findById(String id) {
		if(!id.isEmpty()) {
			Optional<TrucksHeadPp> thp =trucksHeadPpDao.findById(id);
			if(thp.isPresent()) {
				return thp.get();
			}
			return null;
		}else {
			return null;
		}
		
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksHeadPp> getTrucksHeadPpByNameList(String name) {
		if (!name.isEmpty()) {
			Specification<TrucksHeadPp> spec = new Specification<TrucksHeadPp>() {
				@Override
				public Predicate toPredicate(Root<TrucksHeadPp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("name"), name));
					return pre;
				}
			};
			return trucksHeadPpDao.findAll(spec);
		}
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");//同一天价格时间降序排列
		return trucksHeadPpDao.findAll(sort);
	}

}
