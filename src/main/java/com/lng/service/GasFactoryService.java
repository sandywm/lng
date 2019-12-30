package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.GasFactory;

public interface GasFactoryService {

	/**
	 * @description 增加或者修改液厂信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 上午10:34:53
	 * @param gs
	 * @return
	 */
	String addOrUpGasFactory(GasFactory gs);
	
	/**
	 * @description 根据主键获取液厂信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午3:55:23
	 * @param gfId 液厂主键
	 * @return
	 */
	GasFactory getEntityById(String gfId);
	
	/**
	 * @description 根据条件获取液厂信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午3:38:43
	 * @param name 液厂名称(""表示全部)
	 * @param namePy 液厂名称拼音码(""表示全部)
	 * @param gasTypeId 液质类型编号(""表示全部)
	 * @param province 省(""表示全部)
	 * @param provincePy 市(""表示全部)
	 * @param checkStatus 审核状态（-1:全部,0:未审核,1:审核通过,2:审核未通过）
	 * @return
	 */
	List<GasFactory> listInfoByOpt(String name,String namePy,String gasTypeId,String province,
			String provincePy,Integer checkStatus);
	
	/**
	 * @description 根据条件分页获取液厂信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午3:38:57
	 * @param name 液厂名称(""表示全部)
	 * @param namePy 液厂名称拼音码(""表示全部)
	 * @param gasTypeId 液质类型编号(""表示全部)
	 * @param province 省(""表示全部)
	 * @param provincePy 市(""表示全部)
	 * @param checkStatus 审核状态（-1:全部,0:未审核,1:审核通过,2:审核未通过）
	 * @param owerUserId 液厂所属人编号
	 * @opt 使用范围（0:后台，1：前台）
	 * @param pageIndex 页码
	 * @param pageSize 每页记录条数
	 * @return
	 */
	Page<GasFactory> listPageInfoByOpt(String name,String namePy,String gasTypeId,String province,
			String provincePy,Integer checkStatus,String owerUserId,Integer opt,Integer pageIndex,Integer pageSize);
	
	/**
	 * @description 根据条件获取所有液厂信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月13日 上午10:49:15
	 * @param provPy 省份拼音(""不查询)
	 * @param gsId 液厂编号(""不查询)
	 * @param gsNamePy 液厂拼音(""不查询)
	 * @param checkStatus 审核状态（-1:全部,0:未审核,1:审核通过,2:审核未通过）
	 * @return
	 */
	List<GasFactory> listInfoByOpt(String provPy,String gsId,String gasTypeId,String gsNamePy,Integer checkStatus);
	
	/**
	 * @description 根据条件分页获取所有液厂信息(前台显示用)--按照hot降序排列
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月14日 上午10:49:15
	 * @param provPy 省份拼音(""不查询)
	 * @param gtId 液资编号(""不查询)
	 * @param gsNamePy 液厂拼音(""不查询)
	 * @param pageIndex 页码
	 * @param pageSize 每页记录条数
	 * @return
	 */
	Page<GasFactory> listInfoByOpt(String provPy,String gtId,String gsNamePy,Integer pageIndex,Integer pageSize);
	
	/**
	 * @description 按照全国省份统计液厂数量（审核通过）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月28日 下午2:47:13
	 * @return
	 */
	List<Object> getTjInfo();
}
