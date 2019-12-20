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
		Optional<MessageCenter> mc =  mcDao.findById(id);
		if(mc.isPresent()) {
			return mc.get();
		}
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	public Page<MessageCenter> getMessageCenterByOption(String toUserId, Integer readSta, Integer pageNo,
			Integer pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
		Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
		Specification<MessageCenter> spec = new Specification<MessageCenter>() {

			@Override
			public Predicate toPredicate(Root<MessageCenter> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate pre = cb.conjunction();
				if (!toUserId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("toUserId"), toUserId));
				}
				if (readSta != -1) {
					pre.getExpressions().add(cb.equal(root.get("readStatus"), readSta));
				}
				return pre;
			}
		};
		return mcDao.findAll(spec, pageable);
	}

}
