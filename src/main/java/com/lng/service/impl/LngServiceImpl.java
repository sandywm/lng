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

import com.lng.dao.LngDao;
import com.lng.pojo.LngInfo;
import com.lng.service.LngService;
//import com.lng.tools.CurrentTime;

@Service
public class LngServiceImpl implements LngService{

	@Autowired
	private LngDao lDao;
	
	@Override
	public String addOrUpdate(LngInfo lng) {
		// TODO Auto-generated method stub
		return lDao.save(lng).getId();
	}

	@Override
	public List<LngInfo> listInfoByOpt(String province, String gsNamePy, String specDate) {
		// TODO Auto-generated method stub
		Specification<LngInfo> spec = new Specification<LngInfo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<LngInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!province.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("gasFactory").get("province"), province));
				}
				if(!gsNamePy.isEmpty()) {
					pre.getExpressions().add(cb.like(root.get("gasFactory").get("namePy"), "%"+gsNamePy+"%"));
				}
				if(!specDate.isEmpty()) {
//					String prevDate = CurrentTime.getFinalDate(-1);
//					String nextDate = CurrentTime.getFinalDate(1);
				}
				return pre;
		}};
		Sort.Order sort1 = new Sort.Order(Sort.Direction.ASC, "gasFactory.orderNo");//升序排列
		Sort.Order sort2 = new Sort.Order(Sort.Direction.ASC, "gasFactory.orderSubNo");//升序排列
		List<Sort.Order> list = new ArrayList<Sort.Order>();
		list.add(sort1);
		list.add(sort2);
		Sort sort = Sort.by(list);
		return lDao.findAll(spec,sort);
	}

}
