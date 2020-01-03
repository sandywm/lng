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

import com.lng.dao.LngMessageDao;
import com.lng.dao.LngMessageReplyDao;
import com.lng.pojo.LngMessage;
import com.lng.pojo.LngMessageReply;
import com.lng.service.LngMessageService;

@Service
public class LngMessageServiceImpl implements LngMessageService {

	@Autowired
	private LngMessageDao lmDao;

	@Autowired
	private LngMessageReplyDao lmrDao;

	@Override
	public String addOrUpdateLngMsg(LngMessage lm) {
		// TODO Auto-generated method stub
		return lmDao.save(lm).getId();
	}

	@Override
	public Page<LngMessage> listPageMsgInfoByOpt(String userId, Integer checkStatus, Integer showStatus,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		Specification<LngMessage> spec = new Specification<LngMessage>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<LngMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if (!userId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("user").get("id"), userId));
				}
				if (checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if (showStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 同一天价格时间降序排列
		Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, sort);
		return lmDao.findAll(spec, pageable);
	}

	@Override
	public String addOrUpdateLngMsg(LngMessageReply lmr) {
		// TODO Auto-generated method stub
		return lmrDao.save(lmr).getId();
	}

	@Override
	public Page<LngMessageReply> listReplyMsgByMsdId(String msgId, Integer checkStatus, Integer showStatus,Integer page,Integer limit) {
		// TODO Auto-generated method stub
		Specification<LngMessageReply> spec = new Specification<LngMessageReply>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<LngMessageReply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate pre = cb.conjunction();
				if (!msgId.isEmpty()) {
					pre.getExpressions().add(cb.equal(root.get("lngMessage").get("id"), msgId));
				}
				if (checkStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("checkStatus"), checkStatus));
				}
				if (showStatus >= 0) {
					pre.getExpressions().add(cb.equal(root.get("showStatus"), showStatus));
				}
				return pre;
			}
		};
		Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 同一天价格时间降序排列
		Pageable pageable = PageRequest.of(page - 1, limit, sort);
		return lmrDao.findAll(spec, pageable);
	}

	@Override
	public LngMessage getEntityById(String id) {
		Optional<LngMessage> lm = lmDao.findById(id);
		if (lm.isPresent()) {
			return lm.get();
		}
		return null;
	}
}
