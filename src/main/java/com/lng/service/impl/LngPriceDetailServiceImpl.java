package com.lng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lng.dao.LngPriceDetailDao;
import com.lng.pojo.LngPriceDetail;
import com.lng.service.LngPriceDetailService;

@Service
public class LngPriceDetailServiceImpl implements LngPriceDetailService{

	@Autowired
	private LngPriceDetailDao lpdDao;
	
	@Override
	public String addOrUpdate(LngPriceDetail lpd) {
		// TODO Auto-generated method stub
		return lpdDao.save(lpd).getId();
	}

	@Override
	public void saveBatch(List<LngPriceDetail> lpdList) {
		// TODO Auto-generated method stub
		lpdDao.saveAll(lpdList);
	}
}
