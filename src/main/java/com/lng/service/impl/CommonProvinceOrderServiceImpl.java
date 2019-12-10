package com.lng.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.CommonProvinceOrderDao;
import com.lng.pojo.CommonProvinceOrder;
import com.lng.service.CommonProvinceOrderService;

@Service
public class CommonProvinceOrderServiceImpl implements CommonProvinceOrderService{

	@Autowired
	private CommonProvinceOrderDao cpDao;
	
	@Override
	public List<CommonProvinceOrder> listAllInfo(String orderEng) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "orderNo");//降序排列
		if(orderEng.isEmpty() || orderEng.equalsIgnoreCase("asc")) {
			sort = Sort.by(Sort.Direction.ASC, "orderNo");//升序排列
		}
		return cpDao.findAll(sort);
	}

	@Override
	public CommonProvinceOrder getEntityByOpt(Integer id, String province) {
		// TODO Auto-generated method stub
		List<CommonProvinceOrder> cpList = new ArrayList<CommonProvinceOrder>();
		if(id > 0) {
			return cpDao.findById(id).get();
		}
		if(!province.isEmpty()) {
			Specification<CommonProvinceOrder> spec = new Specification<CommonProvinceOrder>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<CommonProvinceOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					Predicate pre = cb.conjunction();
					if(!province.isEmpty()) {
						pre.getExpressions().add(cb.equal(root.get("province"), province));
					}
					return pre;
			}};
			cpList = cpDao.findAll(spec);
			if(cpList.size() > 0) {
				return cpList.get(0);
			}
			
		}
		return null;
	}

	@Override
	public Integer saveOrUpdate(CommonProvinceOrder pro) {
		// TODO Auto-generated method stub
		return cpDao.save(pro).getId();
	}

}
