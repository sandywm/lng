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

import com.lng.dao.RqDevTradeImgDao;
import com.lng.pojo.RqDevTradeImg;
import com.lng.service.RqDevTradeImgService;

@Service
public class RqDevTradeImgServiceImpl implements RqDevTradeImgService {
	@Autowired
	private RqDevTradeImgDao rdtImgDao;

	@Override
	public String addOrUpdate(RqDevTradeImg rdtImg) {
		return rdtImgDao.save(rdtImg).getId();
	}

	@Override
	public RqDevTradeImg getEntityById(String id) {
		Optional<RqDevTradeImg> rdtImgs = rdtImgDao.findById(id);
		if (rdtImgs.isPresent()) {
			return rdtImgs.get();
		}
		return null;
	}

	@Override
	public void delete(String id) {
		rdtImgDao.deleteById(id);
	}

	@SuppressWarnings("serial")
	@Override
	public List<RqDevTradeImg> getRdtImgByRdtId(String rdtId) {
		Sort sort = Sort.by(Sort.Direction.ASC, "orderNum");// 升序排列
		Specification<RqDevTradeImg> spec = new Specification<RqDevTradeImg>() {
			@Override
			public Predicate toPredicate(Root<RqDevTradeImg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!rdtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("rqDevTrade").get("id"), rdtId));
				}
				return pre;
			}
		};

		return rdtImgDao.findAll(spec,sort);
	}

	@Override
	public void addOrUpdateBatch(List<RqDevTradeImg> rdtImg) {
		rdtImgDao.saveAll(rdtImg);
	}

	@Override
	public void deleteBatch(List<RqDevTradeImg> rdtImg) {
		rdtImgDao.deleteAll(rdtImg);
       	}

}
