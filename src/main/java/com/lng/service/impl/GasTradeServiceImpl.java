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

import com.lng.dao.GasTradeDao;
import com.lng.dao.GasTradeImgDao;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeImg;
import com.lng.service.GasTradeService;

@Service
public class GasTradeServiceImpl implements GasTradeService{

	@Autowired
	private GasTradeDao gtDao;
	@Autowired
	private GasTradeImgDao gtiDao;
	
	@Override
	public String saveOrUpdate(GasTrade gt) {
		// TODO Auto-generated method stub
		return gtDao.save(gt).getId();
	}

	@Override
	public GasTrade getEntityById(String id) {
		// TODO Auto-generated method stub
		Optional<GasTrade> gt = gtDao.findById(id);
		if(gt.isPresent()) {
			return gt.get();
		}
		return null;
	}

	@Override
	public Page<GasTrade> listPageInfoByOpt(String cpyId, String addUserId,String gtId, String gfId, Integer checkStatus,
			Integer showStatus, Integer sPrice, Integer ePrice, String psArea, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Specification<GasTrade> spec = new Specification<GasTrade>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTrade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!cpyId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), cpyId));
				}
				if(!addUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), addUserId));
				}
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasType"), gtId));
				}
				if(!gfId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("id"), gfId));
				}
				if(checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if(showStatus.equals(0) || showStatus.equals(1)) {
					pre.getExpressions().add(cb.equal(root.get("provincePy"), showStatus));
				}
				if(sPrice >= 0 && ePrice > 0 && sPrice <= ePrice) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("gasPrice"), sPrice.doubleValue()));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("gasPrice"), ePrice.doubleValue()));
				}
				if(!psArea.isEmpty()) {
//					String[] psAreaArr = psArea.split(",");
//					List predicateList = new ArrayList();
//					Predicate [] p = new Predicate[bottomLevelDataPermission.size()];
					pre.getExpressions().add(cb.like(root.get("psArea"), "%"+psArea+"%"));
				}
				return pre;
		}};
		Sort.Order sort2 = new Sort.Order(Sort.Direction.DESC, "hot");//升序排列
		Sort.Order sort1 = new Sort.Order(Sort.Direction.DESC, "addTime");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return gtDao.findAll(spec, pageable);
	}

	@Override
	public String saveOrUpdate(GasTradeImg gti) {
		// TODO Auto-generated method stub
		return gtiDao.save(gti).getId();
	}

	@Override
	public List<GasTradeImg> listInfoByGtId(String gtId) {
		// TODO Auto-generated method stub
		Specification<GasTradeImg> spec = new Specification<GasTradeImg>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<GasTradeImg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!gtId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasTrade").get("id"), gtId));
				}
				return pre;
		}};
		return gtiDao.findAll(spec);
	}

	@Override
	public GasTradeImg getEntityById_1(String id) {
		// TODO Auto-generated method stub
		Optional<GasTradeImg> gt = gtiDao.findById(id);
		if(gt.isPresent()) {
			return gt.get();
		}
		return null;
	}

	@Override
	public void delBatchByGtId(List<GasTradeImg> gtiList) {
		// TODO Auto-generated method stub
		gtiDao.deleteAll(gtiList);
	}

	@Override
	public void addBatchInfo(List<GasTradeImg> gtiList) {
		// TODO Auto-generated method stub
		gtiDao.saveAll(gtiList);
	}

	@Override
	public Page<GasTrade> listPageInfoByOpt(String cpyId, String addUserId, String gtId, String gfId,
			Integer checkStatus, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		return gtDao.findPageInfoByOpt(cpyId, addUserId, gtId, gfId, checkStatus, pageable);
	}

}
