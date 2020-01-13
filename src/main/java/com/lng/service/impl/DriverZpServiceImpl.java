package com.lng.service.impl;

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
			if (zp.isPresent()) {
				return zp.get();
			}
			return null;
		} else {
			return null;
		}

	}

	@SuppressWarnings("serial")
	@Override
	public Page<DriverZp> getDriverQzByOption(String compId, String jzType, String jzYear,Integer checkSta, Integer showSta,
			String wage, Integer pageNo, Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
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
				if (!jzYear.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("jlYearRange"), jzYear));
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
					Integer wage2 = null;
					if (wages.length == 1) {
						wage2 = wage1;
					} else {
						wage2 = Integer.parseInt(wages[1]);
					}
					pre.getExpressions().add(cb.between(root.get("wage"), wage1, wage2));
				}
				return pre;
			}
		};
		return driverZpDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<DriverZp> getDriverZpList(String cpyId) {
		if (!cpyId.isEmpty()) {
			Specification<DriverZp> spec = new Specification<DriverZp>() {
				@Override
				public Predicate toPredicate(Root<DriverZp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Predicate pre = cb.conjunction();
					pre.getExpressions().add(cb.equal(root.get("company").get("id"), cpyId));
					return pre;
				}
			};
			return driverZpDao.findAll(spec);
		}
		return driverZpDao.findAll();
	}

	@SuppressWarnings("serial")
	@Override
	public List<DriverZp> listDriverZpByOpt(String sDate, String eDate, Integer checkSta, Integer showSta) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<DriverZp> spec = new Specification<DriverZp>() {

			@Override
			public Predicate toPredicate(Root<DriverZp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!sDate.isEmpty() && !eDate.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
				}
				if (checkSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showSta));
				}
				return pre;
			}
		};
		return driverZpDao.findAll(spec, sort);
	}

	@SuppressWarnings("serial")
	@Override
	public List<DriverZp> listDriverZpByOpt(String addUserId, Integer checkSta, Integer showSta) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<DriverZp> spec = new Specification<DriverZp>() {

			@Override
			public Predicate toPredicate(Root<DriverZp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!addUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("addUserId"), addUserId));
				}
				if (checkSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkSta));
				}
				if (showSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showSta));
				}
				return pre;
			}
		};
		return driverZpDao.findAll(spec, sort);
	}
}
