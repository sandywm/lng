package com.lng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lng.dao.SysConfigDao;
import com.lng.pojo.SystemInfo;
import com.lng.service.SysConfigService;

@Service
public class SysConfigServiceImpl implements SysConfigService{

	@Autowired
	private SysConfigDao scDao;
	
	@Override
	public List<SystemInfo> findInfo() {
		// TODO Auto-generated method stub
		return scDao.findAll();
	}

	@Override
	public String addOrUpdate(SystemInfo sys) {
		// TODO Auto-generated method stub
		return scDao.save(sys).getId();
	}

}
