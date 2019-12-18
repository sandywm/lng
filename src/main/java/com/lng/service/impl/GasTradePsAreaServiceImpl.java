package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.lng.dao.GasTradePsAreaDao;
import com.lng.pojo.GasTradePsArea;
import com.lng.service.GasTradePsAreaService;

public class GasTradePsAreaServiceImpl implements GasTradePsAreaService{

	@Autowired
	private GasTradePsAreaDao gtpaDao;
	
	@Override
	public String saveAndUpdate(GasTradePsArea gtpa) {
		// TODO Auto-generated method stub
		return gtpaDao.save(gtpa).getId();
	}

	@Override
	public List<GasTradePsArea> listInfoByGtId(String gtId) {
		// TODO Auto-generated method stub
		Specification<GasTradePsArea> spec = new Specification<GasTradePsArea>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTradePsArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				return pre;
		}};
		return gtpaDao.findAll(spec);
	}

	@Override
	public void addBatchInfo(List<GasTradePsArea> list) {
		// TODO Auto-generated method stub
		gtpaDao.saveAll(list);
	}

	@Override
	public void delBatchInfo(List<GasTradePsArea> list) {
		// TODO Auto-generated method stub
		gtpaDao.deleteInBatch(list);
	}

}
