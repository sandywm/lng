package com.lng.service.impl;

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

import com.lng.dao.DriverZpDao;
import com.lng.pojo.DriverZp;
import com.lng.service.DriverZpService;

@Service
public class DriverZpServiceImpl implements DriverZpService {
	@Autowired
	private DriverZpDao driverZpDao;

	@Override
	public String saveOrUpdate(DriverZp driverZp) {
		return driverZpDao.save(driverZp).getId();
	}

	@Override
	public DriverZp getEntityById(String id) {
		if (!id.isEmpty()) {
			Optional<DriverZp> zp = driverZpDao.findById(id);
			if(zp.isPresent()) {
				return zp.get();
			}
			return null;
		} else {
			return null;
		}

	}

	@SuppressWarnings("serial")
	@Override
	public Page<DriverZp> getDriverQzByOption(String compId, String jzType, Integer checkSta, Integer showSta,
			String wage, Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<DriverZp> spec = new Specification<DriverZp>() {

			@Override
			public Predicate toPredicate(Root<DriverZp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!compId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), compId));
				}

				if (!jzType.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("jzType"), jzType));
				}
				if (checkSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showSta));
				}
				if (!wage.isEmpty()) {
					String[] wages = wage.split("-");
					Integer wage1 = Integer.parseInt(wages[0]);
					Integer wage2=null;
					if(wages.length==1) {
						wage2=wage1;
					}else {
						wage2 = Integer.parseInt(wages[1]);
					}
					pre.getExpressions().add(cb.between(root.get("wage"), wage1, wage2));
				}
				return null;
			}
		};
		return driverZpDao.findAll(spec, pageable);
	}

}