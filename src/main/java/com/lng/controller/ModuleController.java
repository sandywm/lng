package com.lng.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.ActSuper;
import com.lng.pojo.Module;
import com.lng.pojo.ModuleAct;
import com.lng.pojo.SuperDep;
import com.lng.service.ActSuperService;
import com.lng.service.ModuleActService;
import com.lng.service.ModuleService;
import com.lng.service.SuperDepService;
import com.lng.tools.CommonTools;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 模块、权限接口
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月5日 上午8:50:30
 */
@RestController
@Api(tags = "模块管理")
@RequestMapping(value = "/mod")
public class ModuleController {

	@Autowired
	private ModuleService ms;
	@Autowired
	private ModuleActService mas;
	@Autowired
	private SuperDepService sds;
	@Autowired
	private ActSuperService ass;
	
	/**
	 * 按照模块升序排序
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 上午8:33:39
	 */
	private class SortClass implements Comparator<Object> {
		@Override
		@SuppressWarnings("unchecked")
	    public int compare(Object obj0, Object obj1) {
		Map<String, Integer> map0 = (Map<String, Integer>) obj0;
	      Map<String, Integer> map1 = (Map<String, Integer>) obj1;
	      if(map0.get("modOrder") != null && map1.get("modOrder") != null) {
	    	  int flag = map0.get("modOrder").compareTo(map1.get("modOrder"));
		      return flag; // 不取反，则按正序排列
	      }else {
	    	  return 1;
	      }
	    }
	 }
	
	@GetMapping("getModuleData")
	@ApiOperation(value = "获取平台模块--所有人",notes = "超管和后台管理人员通用，当时超管时获取全平台所有模块，当是其他管理人员时，只显示分配的权限模块")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),@ApiResponse(code = 50001, message = "数据未找到")})
	public GenericResponse getModuleData(HttpServletRequest request) {
		Integer status = 200;
		String superUserId = CommonTools.getLoginUserId(request);
		List<Object> list_d = new ArrayList<Object>();
		try {
			//获取用户所在部门
			List<SuperDep> sdList = sds.listSpecInfoByUserId(superUserId);
			if(sdList.size() > 0) {
				String roleName = CommonTools.getLoginRoleName(request);
				if(Constants.SUPER_DEP_ABILITY.contains(roleName)) {//超级管理员
					//获取所有模块
					List<Module> mList  = ms.listAllInfo();
					for(Module mod : mList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						map_d.put("modId", mod.getId());
						map_d.put("modName", mod.getModName());
						map_d.put("modUrl", mod.getModUrl());
						map_d.put("modOrder", mod.getModOrder());
						if(mod.getModOrder() == 0) {//系统配置
							List<Object> list_d1 = new ArrayList<Object>();
							List<ModuleAct> maList = mas.listInfoByModId(mod.getId());
							if(maList.size() > 0) {
								for(ModuleAct ma : maList) {
									Map<String,Object> map_d1 = new HashMap<String,Object>();
									map_d1.put("maId", ma.getId());
									map_d1.put("maChi", ma.getActNameChi());
									map_d1.put("maEng", ma.getActNameEng());
									list_d1.add(map_d1);
								}
								map_d.put("subModList", list_d1);
							}
						}
						list_d.add(map_d);
					}
					return ResponseFormat.retParam(status, list_d);
				}else {
					//其他部门的人员根据子模块获取主模块列表
					//系统配置模块所有人都能看
					List<Module> sysModList = ms.listSysMod();
					for(Module mod : sysModList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						map_d.put("modId", mod.getId());
						map_d.put("modName", mod.getModName());
						map_d.put("modUrl", mod.getModUrl());
						map_d.put("modOrder", mod.getModOrder());
						List<Object> list_d1 = new ArrayList<Object>();
						List<ActSuper> asList = ass.listSpecConfigInfoByOpt(superUserId, mod.getId());
						if(asList.size() > 0) {
							for(ActSuper as : asList) {
								Map<String,Object> map_d1 = new HashMap<String,Object>();
								map_d1.put("subModName", as.getModuleAct().getActNameChi());
								map_d1.put("subModEng", as.getModuleAct().getActNameEng());
								list_d1.add(map_d1);
							}
							map_d.put("subModList", list_d1);
							list_d.add(map_d);
						}
					}
					List<ActSuper> asList = ass.listSpecInfoByUserId(superUserId, "",0);//获取除系统配置以为的其他权限模块
					if(asList.size() > 0) {
						List<Module> list_m = new ArrayList<Module>();
						for(ActSuper as : asList) {
							Module mod = as.getModuleAct().getModule();
							Map<String,Object> map_d = new HashMap<String,Object>();
							if(list_d.size() == 0) {//首次不判断
								map_d.put("modId", mod.getId());
								map_d.put("modName", mod.getModName());
								map_d.put("modUrl", mod.getModUrl());
								map_d.put("modOrder", mod.getModOrder());
								map_d.put("subModList", new ArrayList<Object>());
								list_d.add(map_d);
								list_m.add(mod);
							}else {
								//判断list_m中有无相同的modId
								Integer exist_status = 1;
								for(Module mod_exist : list_m) {
									if(mod_exist.getId().equals(mod.getId())) {
										exist_status = 0;
										break;
									}else{
										exist_status = 1;//不存在
									}
								}
								if(exist_status.equals(1)){//不存在
									map_d.put("modId", mod.getId());
									map_d.put("modName", mod.getModName());
									map_d.put("modUrl", mod.getModUrl());
									map_d.put("modOrder", mod.getModOrder());
									map_d.put("subModList", new ArrayList<Object>());
									if(!map_d.isEmpty()) {
										list_d.add(map_d);
										list_m.add(mod);
									}
								}
							}
						}
					}
					if(list_d.size() == 0) {
						status = 50001;
					}else {
						SortClass sort = new SortClass();
						Collections.sort(list_d, sort);//list_d中modOrder升序排列
					}
				}
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list_d);
	}
	
	@GetMapping("getModuleDetailData")
	@ApiOperation(value = "获取平台模块详细信息",notes = "修改主模块时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modId", value = "主模块编号")
	})
	public GenericResponse getModuleDetailData(HttpServletRequest request,String modId) {
		Integer status = 200;
		List<Module> modList = new ArrayList<Module>();
		try {
			Module mod = ms.getEntityById(modId);
			if(mod != null) {
				modList.add(mod);
			}else {
				status = 50001;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, modList);
	}
	
	@GetMapping("getSubModuleDetailData")
	@ApiOperation(value = "获取平台指定子模块详细信息",notes = "修改子模块时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "maId", value = "子模块编号")
	})
	public GenericResponse getSubModuleDetailData(HttpServletRequest request,String maId) {
		Integer status = 200;
		List<ModuleAct> maList = new ArrayList<ModuleAct>();
		try {
			ModuleAct ma = mas.getEntityById(maId);
			if(ma != null) {
				maList.add(ma);
			}else {
				status = 50001;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, maList);
	}
	
	@GetMapping("getAllModuleData")
	@ApiOperation(value = "获取平台所有模块--超管",notes = "设置权限时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 70001, message = "无权限访问")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "specUserId", value = "后台人员编号")
	})
	public GenericResponse getAllModuleData(HttpServletRequest request,String specUserId) {
		Integer status = 200;
		List<Object> modList = new ArrayList<Object>();
		try {
			if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {
				//获取所有模块
				List<Module> mList = ms.listAllInfo();
				Integer i = 1;
				if(mList.size() > 0) {
					for(Module mod : mList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						map_d.put("modId", mod.getId());
						map_d.put("modName", mod.getModName());
						map_d.put("modUrl", mod.getModUrl());
						map_d.put("modOrder", i++);
						List<Object> list_d1 = new ArrayList<Object>();
						List<ModuleAct> maList = mas.listInfoByModId(mod.getId());
						Integer mainModCheckStatus = 1;
						if(maList.size() > 0) {
							for(ModuleAct ma : maList) {
								Map<String,Object> map_d1 = new HashMap<String,Object>();
								map_d1.put("maId", ma.getId());
								map_d1.put("maChi", ma.getActNameChi());
								map_d1.put("maEng", ma.getActNameEng());
								//获取指定人员的子模块
								if(specUserId == null || specUserId == "") {
									map_d1.put("selFlag", false);
									mainModCheckStatus *= 0;
									map_d1.put("disabledFlag", false);
								}else {
									if(ass.listSpecInfoByUserId(specUserId, ma.getId(),-1).size() > 0) {
										map_d1.put("selFlag", true);
										mainModCheckStatus *= 1;
										if(ma.getActNameEng().startsWith("list")) {
											if(ass.listSpecConfigInfoByOpt(specUserId, mod.getId()).size() >= 2) {//拥有两个或者两个以上的权限(包括list)
												map_d1.put("disabledFlag", true);
											}else {
												map_d1.put("disabledFlag", false);
											}
										}else {
											map_d1.put("disabledFlag", false);
										}
									}else {
										map_d1.put("selFlag", false);
										mainModCheckStatus *= 0;
										map_d1.put("disabledFlag", false);
									}
								}
								list_d1.add(map_d1);
							}
						}else {
							mainModCheckStatus = 0;
						}
						map_d.put("modSubList", list_d1);
						map_d.put("mainBindFlag", mainModCheckStatus);
						modList.add(map_d);
					}
				}else {
					status = 50001;
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, modList);
	}
	
	@PostMapping("addMod")
	@ApiOperation(value = "增加模块",notes = "增加模块(系统配置，审核管理时模块类型为1)")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modName", value = "模块名称",  required = true),
		@ApiImplicitParam(name = "modUrl", value = "模块URL",  required = true),
		@ApiImplicitParam(name = "modType", value = "模块类型(0:无子模块,1:有子模块)",  required = true)
	})
	public GenericResponse addMod(HttpServletRequest request) {
		Integer status = 200;
		String modId = "";
		String modName = CommonTools.getFinalStr("modName", request);
		String modUrl =  CommonTools.getFinalStr("modUrl", request);
		Integer modType = CommonTools.getFinalInteger("modType", request);
		try {
			if(!modName.equals("") && !modUrl.equals("")) {
				if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {
					//查询名字不能相同
					List<Module> mList = ms.listSpecInfoByName(modName);
					if(mList.size() == 0) {
						mList = ms.listAllInfo();
						Integer modOrder = 1;
						if(mList.size() > 0) {
							modOrder = mList.get(mList.size() - 1).getModOrder() + 1;
						}
						modId = ms.addOrUpMod(new Module(modName, modUrl, modOrder,modType));
						//默认
					}else {
						status = 50003;
					}
				}else {
					status = 70001;
				}
			}else {
				status = 1000;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, modId);
	}
	
	@PostMapping("upMod")
	@ApiOperation(value = "修改主模块",notes = "修改主模块(系统配置，审核管理时模块类型为1)")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modId", value = "模块编号",  required = true),
		@ApiImplicitParam(name = "modName", value = "模块名称",  required = true),
		@ApiImplicitParam(name = "modUrl", value = "模块URL",  required = true),
		@ApiImplicitParam(name = "modType", value = "模块类型(0:无子模块,1:有子模块)",  required = true)
	})
	public GenericResponse upMod(HttpServletRequest request) {
		Integer status = 200;
		String modId = CommonTools.getFinalStr("modId", request);
		String modName = CommonTools.getFinalStr("modName", request);
		String modUrl =  CommonTools.getFinalStr("modUrl", request);
		Integer modType = CommonTools.getFinalInteger("modType", request);
		try {
			if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {
				//查询名字不能相同
				Module mod = ms.getEntityById(modId);
				if(mod != null) {
					if(!mod.getModName().equals(modName)) {
						//需要检测是否重名
						if(ms.listSpecInfoByName(modName).size() > 0) {
							status = 50003;
						}else {
							mod.setModName(modName);
							mod.setModUrl(modUrl);
							mod.setModType(modType);
							ms.addOrUpMod(mod);
						}
					}else {
						mod.setModName(modName);
						mod.setModUrl(modUrl);
						mod.setModType(modType);
						ms.addOrUpMod(mod);
					}
				}else {
					status = 50001;
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PostMapping("addModAct")
	@ApiOperation(value = "增加子模块动作",notes = "增加子模块")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modId", value = "模块编号",  required = true),
		@ApiImplicitParam(name = "actNameChi", value = "动作名称中文",  required = true),
		@ApiImplicitParam(name = "actNameEng", value = "动作名称英文",  required = true)
	})
	public GenericResponse addModAct(HttpServletRequest request,String modId,String actNameChi, String actNameEng) {
		Integer status = 200;
		String superUserId = CommonTools.getLoginUserId(request);
		String maId = "";
		try {
			//获取用户所在部门
			List<SuperDep> sdList = sds.listSpecInfoByUserId(superUserId);
			if(sdList.size() > 0) {
				if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {
					//动作英文必须唯一，中文一个模块内部必须唯一
					List<ModuleAct> maList = mas.listInfoByOpt(modId,actNameChi,"");
					List<ModuleAct> maList_1 = mas.listInfoByOpt("","",actNameEng);
					if(maList.size() == 0 && maList_1.size() == 0) {
						maList = mas.listInfoByModId(modId);
						Integer actOrder = 1;
						if(maList.size() > 0) {
							actOrder = maList.get(maList.size() - 1).getActOrder() + 1;
						}
						maId = mas.addOrUpModuleAct(new ModuleAct(ms.getEntityById(modId) ,actNameChi, actNameEng,actOrder));
					}else {
						status = 50003;
					}
				}else {
					status = 70001;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, maId);
	}
	
	@PostMapping("upModAct")
	@ApiOperation(value = "修改子模块",notes = "修改子模块")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "maId", value = "模块动作编号",  required = true),
		@ApiImplicitParam(name = "actNameChi", value = "动作名称中文",  required = true),
		@ApiImplicitParam(name = "actNameEng", value = "动作名称英文",  required = true)
	})
	public GenericResponse upModAct(HttpServletRequest request,String maId,String actNameChi, String actNameEng) {
		Integer status = 200;
		try {
			if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {
				//查询名字不能相同
				ModuleAct mc = mas.getEntityById(maId);
				if(mc != null) {
					String actNameChi_db = mc.getActNameChi();
					String actNameEng_db = mc.getActNameEng();
					if(actNameChi_db.equals(actNameChi) && actNameEng_db.equals(actNameEng)) {//不变时不修改数据库
						
					}else if(!actNameChi_db.equals(actNameChi)){//模块名称不同时
						//需要判断模块是否重复
						String modId = mc.getModule().getId();
						//动作英文必须唯一，中文一个模块内部必须唯一
						if(mas.listInfoByOpt(modId,actNameChi, "").size() == 0) {
							mc.setActNameChi(actNameChi);
							mc.setActNameEng(actNameEng);
							mas.addOrUpModuleAct(mc);
						}else {
							status = 50003;
						}
					}else {//模块名称相同，动作不同时
						if(mas.listInfoByOpt("","", actNameEng).size() == 0) {
							mc.setActNameChi(actNameChi);
							mc.setActNameEng(actNameEng);
							mas.addOrUpModuleAct(mc);
						}else {
							status = 50003;
						}
					}
				}else {
					status = 50001;
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PostMapping("setAbility")
	@ApiOperation(value = "设置动作权限",notes = "给指定用户设置权限")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "specUserId", value = "后台人员编号",  required = true),
		@ApiImplicitParam(name = "selMaIdStr", value = "选择的模块动作，多个逗号隔开",  required = true)
	})
	public GenericResponse setAbility(HttpServletRequest request,String specUserId,String selMaIdStr) {
		Integer status = 200;
		String superUserId = CommonTools.getLoginUserId(request);
		try {
			if(selMaIdStr == null || selMaIdStr == "") {
				
			}else {
				//获取用户所在部门
				List<SuperDep> sdList = sds.listSpecInfoByUserId(superUserId);
				if(sdList.size() > 0) {
					if(Constants.SUPER_DEP_ABILITY.contains(CommonTools.getLoginRoleName(request))) {//拥有全部权限
						//查询当前人员所有模块动作
						List<ActSuper> asList = ass.listSpecInfoByUserId(specUserId, "",-1);
						if(asList.size() > 0) {
							//删除之前已绑定的用户权限动作
							ass.delBatchInfo(asList);
						}
						//批量增加当前已选择的用户权限动作
						ass.addBatchInfo(specUserId, selMaIdStr);
					}else {
						status = 70001;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
