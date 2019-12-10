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

import com.lng.dao.HqProvinceOrderDao;
import com.lng.pojo.HqProvinceOrder;
import com.lng.service.HqProvinceOrderService;

@Service
public class HqProvinceOrderServiceImpl implements HqProvinceOrderService{

	@Autowired
	private HqProvinceOrderDao hpDao;
	
	@Override
	public List<HqProvinceOrder> listAllInfo(String orderEng) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "orderNo");//降序排列
		if(orderEng.isEmpty() || orderEng.equalsIgnoreCase("asc")) {
			sort = Sort.by(Sort.Direction.ASC, "orderNo");//升序排列
		}
		return hpDao.findAll(sort);
	}

	@Override
	public HqProvinceOrder getEntityByOpt(Integer id, String province) {
		// TODO Auto-generated method stub
		List<HqProvinceOrder> cpList = new ArrayList<HqProvinceOrder>();
		if(id > 0) {
			return hpDao.findById(id).get();
		}
		if(!province.isEmpty()) {
			Specification<HqProvinceOrder> spec = new Specification<HqProvinceOrder>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<HqProvinceOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					Predicate pre = cb.conjunction();
					if(!province.isEmpty()) {
						pre.getExpressions().add(cb.equal(root.get("province"), province));
					}
					return pre;
			}};
			cpList = hpDao.findAll(spec);
			if(cpList.size() > 0) {
				return cpList.get(0);
			}
			
		}
		return null;
	}

	@Override
	public Integer saveOrUpdate(HqProvinceOrder prov) {
		// TODO Auto-generated method stub
		return hpDao.save(prov).getId();
	}

}
