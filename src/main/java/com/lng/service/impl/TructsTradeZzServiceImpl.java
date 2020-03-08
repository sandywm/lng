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

import com.lng.dao.TructsTradeZzDao;
import com.lng.pojo.TructsTradeZz;
import com.lng.service.TructsTradeZzService;

@Service
public class TructsTradeZzServiceImpl implements TructsTradeZzService {
	@Autowired
	private TructsTradeZzDao trZzDao;

	@Override
	public String addOrUpdate(TructsTradeZz ttzz) {

		return trZzDao.save(ttzz).getId();
	}

	@Override
	public TructsTradeZz getEntityById(String id) {
		Optional<TructsTradeZz> ttzz = trZzDao.findById(id);
		if (ttzz.isPresent()) {
			return ttzz.get();
		}
		return null;
	}

	@Override
	public void delete(String id) {
		trZzDao.deleteById(id);

	}

	@SuppressWarnings("serial")
	@Override
	public List<TructsTradeZz> getTructsTradeZzByttId(String ttId) {
		Sort sort = Sort.by(Sort.Direction.ASC, "orderNum");// 升序排列
		Specification<TructsTradeZz> spec = new Specification<TructsTradeZz>() {
			@Override
			public Predicate toPredicate(Root<TructsTradeZz> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!ttId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("trucksTrade").get("id"), ttId));
				}
				return pre;
			}
		};

		return trZzDao.findAll(spec,sort);
	}

	@Override
	public void addOrUpdateBatch(List<TructsTradeZz> zz) {
		trZzDao.saveAll(zz);
	}

	@Override
	public void deleteBatch(List<TructsTradeZz> zz) {
		trZzDao.deleteAll(zz);
	}

}
