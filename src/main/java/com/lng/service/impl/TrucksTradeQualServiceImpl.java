package com.lng.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.TrucksTradeQualificationDao;
import com.lng.pojo.TrucksTradeQualification;
import com.lng.service.TrucksTradeQualService;

@Service
public class TrucksTradeQualServiceImpl implements TrucksTradeQualService {
	@Autowired	
	private TrucksTradeQualificationDao ttQualDao;

	@Override
	public String addOrUpdate(TrucksTradeQualification TTQual) {
		
		return ttQualDao.save(TTQual).getId();
	}

	@Override
	public TrucksTradeQualification getEntityById(String id) {
		
		Optional<TrucksTradeQualification> ttQual  = ttQualDao.findById(id);
		if(ttQual.isPresent()) {
			return ttQual.get();
		}
		return null;
	}

	@Override
	public void delete(String id) {
		ttQualDao.deleteById(id);
	}

	@SuppressWarnings("serial")
	@Override
	public List<TrucksTradeQualification> getTrucksTradeQualList(String ttId) {
		
		Specification<TrucksTradeQualification> spec  = new Specification<TrucksTradeQualification>() {
			@Override
			public Predicate toPredicate(Root<TrucksTradeQualification> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(!ttId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("trucksTrade").get("id"), ttId));
				}
				return pre;
			}
		};
	
		return  ttQualDao.findAll(spec);
	}

	@Override
	public void addOrUpdateBatch(List<TrucksTradeQualification> TTQual) {
		ttQualDao.saveAll(TTQual);
	}

	@Override
	public void deleteBatch(List<TrucksTradeQualification> TTQual) {
		ttQualDao.deleteAll(TTQual);
		
	}

}
