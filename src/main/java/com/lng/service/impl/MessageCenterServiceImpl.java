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

import com.lng.dao.MessageCenterDao;
import com.lng.pojo.MessageCenter;
import com.lng.service.MessageCenterService;

@Service
public class MessageCenterServiceImpl implements MessageCenterService {
	@Autowired
	private MessageCenterDao mcDao;

	@Override
	public String saveOrUpdate(MessageCenter mc) {
		return mcDao.save(mc).getId();
	}

	@Override
	public MessageCenter getEntityById(String id) {
		if(!id.equals("")) {
			Optional<MessageCenter> mc =  mcDao.findById(id);
			if(mc.isPresent()) {
				return mc.get();
			}
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public Page<MessageCenter> getMessageCenterByOption(Integer msgTypeId,String toUserId,Integer showStatus,Integer readSta,
			 Integer pageNo,Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<MessageCenter> spec = new Specification<MessageCenter>() {

			@Override
			public Predicate toPredicate(Root<MessageCenter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(msgTypeId > 0) {
					pre.getExpressions().add(cb.equal(root.get("messageType"), msgTypeId));
				}
				if (!toUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("toUserId"), toUserId));
				}
				if (readSta >= 0) {
					pre.getExpressions().add(cb.equal(root.get("readStatus"), readSta));
				}
				if (showStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		return mcDao.findAll(spec, pageable);
	}

	@SuppressWarnings("serial")
	@Override
	public List<MessageCenter> listMsgByOpt(Integer msgTypeId, String sTime, String eTime) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Specification<MessageCenter> spec = new Specification<MessageCenter>() {

			@Override
			public Predicate toPredicate(Root<MessageCenter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if(msgTypeId > 0) {
					pre.getExpressions().add(cb.equal(root.get("messageType"), msgTypeId));
				}
				if (!sTime.isEmpty() && !eTime.isEmpty()) {
					pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sTime + " 00:00:01"));
					pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eTime + " 23:59:59"));
				}
				return pre;
			}
		};
		return mcDao.findAll(spec, sort);
	}

}
