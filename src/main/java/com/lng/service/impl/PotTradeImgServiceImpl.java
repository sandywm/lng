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

import com.lng.dao.PotTradeImgDao;
import com.lng.pojo.PotTradeImg;
import com.lng.service.PotTradeImgService;

@Service
public class PotTradeImgServiceImpl implements PotTradeImgService {
	@Autowired
	private PotTradeImgDao ptImgDao;

	@Override
	public String addOrUpdate(PotTradeImg ptImg) {

		return ptImgDao.save(ptImg).getId();
	}

	@Override
	public PotTradeImg getEntityById(String id) {
		Optional<PotTradeImg> ptImg = ptImgDao.findById(id);
		if (ptImg.isPresent()) {
			return ptImg.get();
		}
		return null;
	}

	@Override
	public void delete(String id) {
		ptImgDao.deleteById(id);
	}

	@SuppressWarnings("serial")
	@Override
	public List<PotTradeImg> getPotTradeImgByPtId(String PtId) {
		Specification<PotTradeImg> spec = new Specification<PotTradeImg>() {
			@Override
			public Predicate toPredicate(Root<PotTradeImg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!PtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("potTrade").get("id"), PtId));
				}
				return pre;
			}
		};

		return ptImgDao.findAll(spec);
	}

	@Override
	public void addOrUpdateBatch(List<PotTradeImg> ptImg) {
		ptImgDao.saveAll(ptImg);
	}

	@Override
	public void deleteBatch(List<PotTradeImg> ptImg) {
		ptImgDao.deleteAll(ptImg);
	}

}
