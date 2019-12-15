package com.lng.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lng.dao.ActSuperDao;
import com.lng.pojo.ActSuper;
import com.lng.service.ActSuperService;
import com.lng.service.ModuleActService;
import com.lng.service.SuperService;
import com.lng.tools.CurrentTime;

@Service
public class ActSuperServiceImpl implements ActSuperService{

	@Autowired
	private ActSuperDao asDao;
	@Autowired
	private ModuleActService mas;
	@Autowired
	private SuperService ss;
	
	@Override
	public List<ActSuper> listSpecInfoByUserId(String userId, String maId,String opt) {
		// TODO Auto-generated method stub
		Specification<ActSuper> spec = new Specification<ActSuper>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ActSuper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("superUser").get("id"), userId));
				}
				if(!maId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("moduleAct").get("id"), maId));
				}
				if(opt.equals("other")) {
					pre.getExpressions().add(cb.notEqual(root.get("moduleAct").get("module").get("modOrder"), 0));
				}else if(opt.equals("sys")) {
					pre.getExpressions().add(cb.equal(root.get("moduleAct").get("module").get("modOrder"), 0));
				}
				return pre;
		}};
		return asDao.findAll(spec);
	}

	@Override
	public List<ActSuper> listSpecInfoByOpt(String userId, String actNameEng) {
		// TODO Auto-generated method stub
		Specification<ActSuper> spec = new Specification<ActSuper>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ActSuper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("superUser").get("id"), userId));
				}
				if(!actNameEng.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("moduleAct").get("actNameEng"), actNameEng));
				}
				return pre;
		}};
		return asDao.findAll(spec);
	}

	@Override
	public void delBatchInfo(List<ActSuper> asList) {
		// TODO Auto-generated method stub
		asDao.deleteInBatch(asList);
	}

	@Override
	public void addBatchInfo(String userId, String selMaIdStr) {
		// TODO Auto-generated method stub
		if(!selMaIdStr.isEmpty()) {
			String[] selMaIdArr = selMaIdStr.split(",");
			String currentTime = CurrentTime.getCurrentTime();
			for(int i = 0 ; i < selMaIdArr.length ; i++) {
				asDao.save(new ActSuper(mas.getEntityById(selMaIdArr[i]), ss.getEntityById(userId), currentTime));
			}
		}
	}

	@Override
	public List<ActSuper> listSpecInfoByOpt1(String userId, String modId) {
		// TODO Auto-generated method stub
		Specification<ActSuper> spec = new Specification<ActSuper>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ActSuper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if(!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("superUser").get("id"), userId));
				}
				if(!modId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("moduleAct").get("module").get("id"), modId));
				}
				return pre;
		}};
		return asDao.findAll(spec);
	}

}
