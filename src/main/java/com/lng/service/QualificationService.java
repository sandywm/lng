package com.lng.service;

import java.util.List;

import com.lng.pojo.Qualification;

public interface QualificationService {
	/**
	 * 
	 * @description 添加进港资质
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:21:01
	 * @param qualification  进港资质
	 */
	public String save(Qualification qualification);
	/**
	 * 
	 * @description 编辑进港资质
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:27:34
	 * @param qualification 进港资质
	 */
	public void edit(Qualification qualification);
	/**
	 * 
	 * @description 删除指定的进港资质信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:28:06
	 * @param id 进港资质编号
	 */
	public void delete(String id);
	/**
	 * 
	 * @description  查看指定的进港资质信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:28:53
	 * @param id  进港资质编号
	 * @return  进港资质
	 */
	Qualification findById(String id);
	/**
	 * 
	 * @description分页查看进港资质信息
	 * @author zdf
	 * @param validSta 有效状态
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:30:44
	 * @return 进港资质列表
	 */
	List<Qualification> getQualificationList(Integer validSta);
	/**
	 * 
	 * @description  根据进港资质名称查看信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月5日 下午2:55:17
	 * @param name 进港资质名称
	 * @return
	 */
	List<Qualification> getQualByNameList(String name);
}
