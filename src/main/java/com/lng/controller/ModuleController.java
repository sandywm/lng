package com.lng.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import com.lng.tools.Sort;
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
	
	@SuppressWarnings("unchecked")
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
				String roleName = sdList.get(0).getDepartment().getDepName();
				if(Constants.SUPER_DEP_ABILITY.contains(roleName)) {//拥有全部权限
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
								map_d.put("modSubList", list_d1);
							}
						}
						list_d.add(map_d);
					}
					return ResponseFormat.retParam(status, list_d);
				}else {
					//其他部门的人员根据子模块获取主模块列表
					//系统配置模块所有人都能看
					List<Module> sysModList = ms.listSysMod();
					if(sysModList.size() > 0) {
						Module mod = sysModList.get(0);
						Map<String,Object> map_d = new HashMap<String,Object>();
						map_d.put("modId", mod.getId());
						map_d.put("modName", mod.getModName());
						map_d.put("modUrl", mod.getModUrl());
						map_d.put("modOrder", mod.getModOrder());
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
							map_d.put("modSubList", list_d1);
						}
						list_d.add(map_d);
					}
					List<ActSuper> asList = ass.listSpecInfoByUserId(superUserId, "");
					if(asList.size() > 0) {
						List<Module> list_m = new ArrayList<Module>();
						for(ActSuper as : asList) {
							Module mod = as.getModuleAct().getModule();
							Map<String,Object> map_d = new HashMap<String,Object>();
							if(list_d.size() == 0) {//首次不判断
								if(mod.getModOrder() > 0) {//不加载系统配置模块
									map_d.put("modId", mod.getId());
									map_d.put("modName", mod.getModName());
									map_d.put("modUrl", mod.getModUrl());
									map_d.put("modOrder", mod.getModOrder());
									list_d.add(map_d);
									list_m.add(mod);
								}
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
									if(mod.getModOrder() > 0) {//不加载系统配置模块
										map_d.put("modId", mod.getId());
										map_d.put("modName", mod.getModName());
										map_d.put("modUrl", mod.getModUrl());
										map_d.put("modOrder", mod.getModOrder());
									}
									if(!map_d.isEmpty()) {
										list_d.add(map_d);
										list_m.add(mod);
									}
								}
							}
						}
						Sort sort = new Sort();
						Collections.sort(list_d, sort);//list_d升序排列
					}else {
						status = 50001;
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
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.SET_ABILITY)) {
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
									if(ass.listSpecInfoByUserId(specUserId, ma.getId()).size() > 0) {
										map_d1.put("selFlag", true);
										mainModCheckStatus *= 1;
										if(ma.getActNameEng().startsWith("list")) {
											if(ass.listSpecInfoByOpt1(specUserId, mod.getId()).size() >= 2) {//拥有两个或者两个以上的权限(包括list)
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
	@ApiOperation(value = "增加模块",notes = "增加模块")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modName", value = "模块名称",  required = true),
		@ApiImplicitParam(name = "modUrl", value = "模块URL",  required = true)
	})
	public GenericResponse addMod(HttpServletRequest request) {
		Integer status = 200;
		String modId = "";
		String modName = CommonTools.getFinalStr("modName", request);
		String modUrl =  CommonTools.getFinalStr("modUrl", request);
		try {
			if(!modName.equals("") && !modUrl.equals("")) {
				if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_MOD)) {
					//查询名字不能相同
					List<Module> mList = ms.listSpecInfoByName(modName);
					if(mList.size() == 0) {
						mList = ms.listAllInfo();
						Integer modOrder = 1;
						if(mList.size() > 0) {
							modOrder = mList.get(mList.size() - 1).getModOrder() + 1;
						}
						modId = ms.addOrUpMod(new Module(modName, modUrl, modOrder));
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
	@ApiOperation(value = "修改主模块",notes = "修改主模块")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "modId", value = "模块编号",  required = true),
		@ApiImplicitParam(name = "modName", value = "模块名称",  required = true),
		@ApiImplicitParam(name = "modUrl", value = "模块URL",  required = true)
	})
	public GenericResponse upMod(HttpServletRequest request,String modId,String modName, String modUrl) {
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_MOD)) {
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
							ms.addOrUpMod(mod);
						}
					}else {
						mod.setModName(modName);
						mod.setModUrl(modUrl);
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
				if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_MOD)) {
					//查询名字不能相同
					List<ModuleAct> maList = mas.listInfoByOpt(actNameChi,"");
					List<ModuleAct> maList_1 = mas.listInfoByOpt("",actNameEng);
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
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_MOD)) {
				//查询名字不能相同
				ModuleAct mc = mas.getEntityById(maId);
				if(mc != null) {
					String actNameChi_db = mc.getActNameChi();
					String actNameEng_db = mc.getActNameEng();
					if(actNameChi_db.equals(actNameChi) && actNameEng_db.equals(actNameEng)) {//不变时不修改数据库
						
					}else if(!actNameChi_db.equals(actNameChi)){//模块名称不同时
						//需要判断模块是否重复
						if(mas.listInfoByOpt(actNameChi, "").size() == 0) {
							mc.setActNameChi(actNameChi);
							mc.setActNameEng(actNameEng);
							mas.addOrUpModuleAct(mc);
						}else {
							status = 50003;
						}
					}else {//模块名称相同，动作不同时
						mc.setActNameChi(actNameChi);
						mc.setActNameEng(actNameEng);
						mas.addOrUpModuleAct(mc);
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
					if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.SET_ABILITY)) {
						//查询当前人员所有模块动作
						List<ActSuper> asList = ass.listSpecInfoByUserId(specUserId, "");
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
