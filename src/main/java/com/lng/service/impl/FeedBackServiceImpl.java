package com.lng.service.impl;

import com.lng.dao.FeedBackDao;
import com.lng.pojo.FeedBack;
import com.lng.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private FeedBackDao feedBackDao;

    @Override
    public Integer saveOrUpdate(FeedBack feedBack) {
        return feedBackDao.save(feedBack).getId();
    }

    @Override
    public FeedBack getEntityById(Integer id) {
        if (!id.equals(0)) {
            Optional<FeedBack> fb = feedBackDao.findById(id);
            if (fb.isPresent()) {
                return fb.get();
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Page<FeedBack> getFeedBackByOption(Integer readStatus, String sDate, String eDate, Integer pageNo, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "addTime");// 降序排列
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<FeedBack> spec  = new Specification<FeedBack>() {
            @Override
            public Predicate toPredicate(Root<FeedBack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate pre = cb.conjunction();
                if (readStatus != -1){
                    pre.getExpressions().add(cb.equal(root.get("readStatus"),readStatus));
                }
                if (!sDate.isEmpty() && !eDate.isEmpty()) {
                    pre.getExpressions().add(cb.greaterThanOrEqualTo(root.get("addTime"), sDate + " 00:00:01"));
                    pre.getExpressions().add(cb.lessThanOrEqualTo(root.get("addTime"), eDate + " 23:59:59"));
                }
                return pre;
            }
        };
        return feedBackDao.findAll(spec, pageable);
    }
}