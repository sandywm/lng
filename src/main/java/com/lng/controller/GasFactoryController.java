package com.lng.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.CommonProvinceOrder;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasFactoryCompany;
import com.lng.pojo.GasType;
import com.lng.pojo.HqProvinceOrder;
import com.lng.pojo.LngPriceDetail;
import com.lng.pojo.LngPriceRemark;
import com.lng.pojo.LngPriceSubDetail;
import com.lng.pojo.MessageCenter;
import com.lng.service.CommonProvinceOrderService;
import com.lng.service.GasFactoryCompanyService;
import com.lng.service.GasFactoryService;
import com.lng.service.GasTypeService;
import com.lng.service.HqProvinceOrderService;
import com.lng.service.LngPriceDetailService;
import com.lng.service.LngPriceRemarkService;
import com.lng.service.LngPriceSubDetailService;
import com.lng.service.MessageCenterService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.PageResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

@RestController
@Api(tags = "液厂管理")
@RequestMapping(value = "/gsf")
public class GasFactoryController {

	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasFactoryCompanyService gfcs;
	@Autowired
	private GasTypeService gts;
	@Autowired
	private CommonProvinceOrderService cpos;
	@Autowired
	private HqProvinceOrderService hpos;
	@Autowired
	private MessageCenterService mcs;
	@Autowired
	private LngPriceDetailService lpds;
	@Autowired
	private LngPriceSubDetailService lpsds;
	@Autowired
	private LngPriceRemarkService lprs;
	
	@PostMapping("addGasFactory")
	@ApiOperation(value = "增加液厂--后台人员",notes = "后台人员增加液厂信息时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空"),
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称", required = true),
		@ApiImplicitParam(name = "facImage", value = "液厂图片"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号", required = true),
		@ApiImplicitParam(name = "province", value = "省", required = true),
		@ApiImplicitParam(name = "city", value = "市"),
		@ApiImplicitParam(name = "county", value = "县"),
		@ApiImplicitParam(name = "address", value = "详细地址"),
		@ApiImplicitParam(name = "lxName", value = "联系人"),
		@ApiImplicitParam(name = "lxTel", value = "联系电话"),
		@ApiImplicitParam(name = "yzbgImg", value = "液质报告图")
	})
	public GenericResponse addGasFactory(HttpServletRequest request) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			String name = CommonTools.getFinalStr("name", request);
			String province = CommonTools.getFinalStr("province", request);
			String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
			String facImage = CommonTools.getFinalStr("facImage", request);
			String city = CommonTools.getFinalStr("city", request);
			String county = CommonTools.getFinalStr("county", request);
			String address = CommonTools.getFinalStr("address", request);
			String lxName = CommonTools.getFinalStr("lxName", request);
			String lxTel = CommonTools.getFinalStr("lxTel", request);
			String yzbgImg = CommonTools.getFinalStr("yzbgImg", request);
			if(!name.equals("") && !province.equals("") && !gasTypeId.equals("")) {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request), Constants.ADD_YC)) {
					if(gfs.listInfoByOpt(name, "", "", "", "", -1).size() == 0) {
						String namePy = CommonTools.getFirstSpell(name);
						String provincePy = CommonTools.getFirstSpell(province);
						List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", province, "", -1);//指定省份下的液厂
						//获取指定省份的排序
						GasType gt = gts.findById(gasTypeId);
						if(gt != null) {
							Integer orderNo = 0;
							if(gt.getName().equals("海气")) {
								HqProvinceOrder prov = hpos.getEntityByOpt(0, province);
								if(prov != null) {
									orderNo = prov.getOrderNo();
								}
							}else {
								CommonProvinceOrder prov = cpos.getEntityByOpt(0, province);
								if(prov != null) {
									orderNo = prov.getOrderNo();
								}
							}
							Integer orderSubNo = gfList.size() + 1;
							GasFactory gf = new GasFactory(gts.findById(gasTypeId), name, namePy, CommonTools.dealUploadDetail(userId,"", facImage), province, provincePy,city,
									county, address, lxName, lxTel, CurrentTime.getCurrentTime(), CommonTools.dealUploadDetail(userId,"", yzbgImg), 1,
									CurrentTime.getCurrentTime(),userId,orderNo,orderSubNo,0);
							uId = gfs.addOrUpGasFactory(gf);
						}else {
							status = 50001;
						}
					}else {
						status = 50003;
					}
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, uId);
	}
	
	@PostMapping("addBatchGasFactory")
	@ApiOperation(value = "批量增加液厂--初始导入时使用",notes = "批量增加液厂--初始导入时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "filePath", value = "excel路径", required = true)
	})
	public GenericResponse addBatchGasFactory(HttpServletRequest request) {
		Integer status = 200;
		String userId = CommonTools.getLoginUserId(request);
		String filePath = CommonTools.getFinalStr("filePath", request);
		try {
			if(!filePath.equals("") ) {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request), Constants.ADD_YC)) {
					int i;  
			        Sheet sheet;  
			        Workbook book;  
			        Cell cell1;
			        HSSFWorkbook wb = new HSSFWorkbook();  
			        WorkbookSettings wbs = new WorkbookSettings();
			        wbs.setEncoding("GBK"); // 解决中文乱码
			        wbs.setSuppressWarnings(true); 
			        book= Workbook.getWorkbook(new File(filePath),wbs);
					//获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)  
					sheet=book.getSheet(0);   
		            i = 1;
		            Integer maxRow = sheet.getRows();
		         // 第一步，创建一个webbook，对应一个Excel文件  
			        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			        HSSFSheet sheet1 = wb.createSheet("初始导入液厂");  
			        //设置横向打印
			        sheet1.getPrintSetup().setLandscape(true);
			        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			        HSSFRow row = sheet1.createRow(0);  
			        // 第四步，创建单元格，并设置值表头 设置表头居中  
			        HSSFCellStyle style = wb.createCellStyle();  
			        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式  
		            style.setVerticalAlignment(VerticalAlignment.CENTER);  
		            
		            row = sheet1.createRow(0);
		            CommonTools.addCellData("id", "液厂名称", "液质类型", "液质类型编号", "省", "市", "县", "",  "", row, style);
		            
		            while(i < maxRow){
		            	String uId = "";
		            	//获取每一行的单元格   
		                cell1=sheet.getCell(0,i);//（列，行）  
		                if("".equals(cell1.getContents())==true)    //如果读取的数据为空  
		                    break;   
//		                String id = sheet.getCell(0,i).getContents().replace(" ", "").replace("\t", "");//编号
		                String gfName = sheet.getCell(1,i).getContents().replace(" ", "").replace("\t", "");//液厂名称
		                String prov = sheet.getCell(2,i).getContents().replace(" ", "").replace("\t", "");//省份
		                String gasType = sheet.getCell(3,i).getContents().replace(" ", "").replace("\t", "");//液质类型
		                String city = "";
		                String county = "";
		                String address = "";
		                String lxName = "";
		                String lxTel = "";
		                row = sheet1.createRow(i);
		                
		                String gasTypeId = "";
		                String namePy = CommonTools.getFirstSpell(gfName);
		                String provincePy = "";
		                if(gasType.equals("")) {
		                	gasType = "没类型";
		                }
		                List<GasType> gtList = gts.getGasTypeByNameList(gasType);
	                	if(gtList.size() > 0) {
	                		gasTypeId = gtList.get(0).getId();
							//获取指定省份的排序
							GasType gt = gts.findById(gasTypeId);
							if(gt != null) {
								Integer orderNo = 0;
								if(gt.getName().equals("海气")) {
									HqProvinceOrder provOrder = hpos.getEntityByOpt(prov);
									if(prov != null) {
										orderNo = provOrder.getOrderNo();
										prov = provOrder.getProvince();
										provincePy = provOrder.getProvincePy();
									}
								}else {
									CommonProvinceOrder provOrder = cpos.getEntityByProv(prov);
									if(prov != null) {
										orderNo = provOrder.getOrderNo();
										prov = provOrder.getProvince();
										provincePy = provOrder.getProvincePy();
									}
								}
								Integer orderSubNo = i;
								GasFactory gf = new GasFactory(gts.findById(gasTypeId), gfName, namePy, "", prov, provincePy,city,
										county, address, lxName, lxTel, CurrentTime.getCurrentTime(), "", 1,
										CurrentTime.getCurrentTime(),userId,orderNo,orderSubNo,0);
								uId = gfs.addOrUpGasFactory(gf);
								if(!uId.equals("")) {
									CommonTools.addCellData(uId, gfName, gasType, gasTypeId, prov, city, county, "", "", row, style);
								}
							}else {
								status = 50001;
							}
	                	}
	                	i++;
		            }
		            String absoFilePath = "d:\\批量液厂_"+CurrentTime.getStringTime1()+".xls";
		            FileOutputStream fout = new FileOutputStream(absoFilePath);  
		            wb.write(fout);  
		            fout.close();  
		            wb.close();
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	
	@PostMapping("addBatchGfPrice")
	@ApiOperation(value = "批量增加液厂价格明细--初始导入时使用",notes = "批量增加液厂价格明细--初始导入时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "filePath", value = "excel路径", required = true)
	})
	public GenericResponse addBatchGfPrice(HttpServletRequest request) {
		Integer status = 200;
		String userId = CommonTools.getLoginUserId(request);
		String filePath = CommonTools.getFinalStr("filePath", request);
		try {
			if(!filePath.equals("") ) {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request), Constants.ADD_YC)) {
					int i;  
			        Sheet sheet;  
			        Workbook book;  
			        Cell cell1;
			        HSSFWorkbook wb = new HSSFWorkbook();  
			        WorkbookSettings wbs = new WorkbookSettings();
			        wbs.setEncoding("GBK"); // 解决中文乱码
			        wbs.setSuppressWarnings(true); 
			        book= Workbook.getWorkbook(new File(filePath),wbs);
					//获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)  
					sheet=book.getSheet(0);   
		            i = 1;
		            Integer maxRow = sheet.getRows();
		         // 第一步，创建一个webbook，对应一个Excel文件  
			        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			        HSSFSheet sheet1 = wb.createSheet("初始导入液厂价格");  
			        //设置横向打印
			        sheet1.getPrintSetup().setLandscape(true);
			        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			        HSSFRow row = sheet1.createRow(0);  
			        // 第四步，创建单元格，并设置值表头 设置表头居中  
			        HSSFCellStyle style = wb.createCellStyle();  
			        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式  
		            style.setVerticalAlignment(VerticalAlignment.CENTER);  
		            
		            row = sheet1.createRow(0);
		            CommonTools.addCellData("id", "液厂名称", "价格时间", "价格", "", "", "", "",  "", row, style);
		            
		            while(i < maxRow){
		            	//获取每一行的单元格   
		                cell1=sheet.getCell(0,i);//（列，行）  
		                if("".equals(cell1.getContents())==true)    //如果读取的数据为空  
		                    break;   
		                String gfName = sheet.getCell(6,i).getContents().replace(" ", "").replace("\t", "");//液厂名称
		                String year = sheet.getCell(1,i).getContents().replace(" ", "").replace("\t", "");//年
		                Integer month = Integer.parseInt(sheet.getCell(2,i).getContents().replace(" ", "").replace("\t", ""));//月
		                Integer day = Integer.parseInt(sheet.getCell(3,i).getContents().replace(" ", "").replace("\t", ""));//日
		                Integer price = Integer.parseInt(sheet.getCell(4,i).getContents().replace(" ", "").replace("\t", ""));//价格
		                String monthStr = String.valueOf(month);
		                String dayStr = String.valueOf(day);
		                if(month < 10) {
		                	monthStr = "0"+month;
		                }
		                if(day < 10) {
		                	dayStr = "0"+day;
		                }
		                String priceTime = year + "-"+monthStr + "-"+dayStr + " 08:08:08";
		                row = sheet1.createRow(i);
		                
		                List<GasFactory> gfList = gfs.listInfoByOpt(gfName, "", "", "", "", 1);
	                	if(gfList.size() > 0) {
	                		//原始数据库一个液厂一天就一条记录
	                		String lpdId = lpds.addOrUpdate(new LngPriceDetail(gfList.get(0), price, priceTime, "", CurrentTime.getCurrentTime()));
	                		if(!lpdId.equals("")) {
	                			//增加价格明细子表
	                			lpsds.saveOrUpdate(new LngPriceSubDetail(lpds.getEntityById(lpdId), price, priceTime, "",
	                					CurrentTime.getCurrentTime()));
	                			CommonTools.addCellData(lpdId, gfName, priceTime, String.valueOf(price), "", "", "", "", "", row, style);
	                		}
	                	}else {
	                		CommonTools.addCellData("找不到该液厂", gfName, priceTime, String.valueOf(price), "", "", "", "", "", row, style);
	                	}
	                	i++;
		            }
		            String absoFilePath = "d:\\批量液厂价格_"+CurrentTime.getStringTime1()+".xls";
		            FileOutputStream fout = new FileOutputStream(absoFilePath);  
		            wb.write(fout);  
		            fout.close();  
		            wb.close();
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PostMapping("addBatchGfRemark")
	@ApiOperation(value = "批量增加液厂备注--初始导入时使用",notes = "批量增加液厂价格备注--初始导入时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "filePath", value = "excel路径", required = true)
	})
	public GenericResponse addBatchGfRemark(HttpServletRequest request) {
		Integer status = 200;
		String userId = CommonTools.getLoginUserId(request);
		String filePath = CommonTools.getFinalStr("filePath", request);
		try {
			if(!filePath.equals("") ) {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request), Constants.ADD_YC)) {
					int i;  
			        Sheet sheet;  
			        Workbook book;  
			        Cell cell1;
			        HSSFWorkbook wb = new HSSFWorkbook();  
			        WorkbookSettings wbs = new WorkbookSettings();
			        wbs.setEncoding("GBK"); // 解决中文乱码
			        wbs.setSuppressWarnings(true); 
			        book= Workbook.getWorkbook(new File(filePath),wbs);
					//获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)  
					sheet=book.getSheet(0);   
		            i = 1;
		            Integer maxRow = sheet.getRows();
		         // 第一步，创建一个webbook，对应一个Excel文件  
			        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			        HSSFSheet sheet1 = wb.createSheet("初始导入液厂价格备注");  
			        //设置横向打印
			        sheet1.getPrintSetup().setLandscape(true);
			        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			        HSSFRow row = sheet1.createRow(0);  
			        // 第四步，创建单元格，并设置值表头 设置表头居中  
			        HSSFCellStyle style = wb.createCellStyle();  
			        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式  
		            style.setVerticalAlignment(VerticalAlignment.CENTER);  
		            
		            row = sheet1.createRow(0);
		            CommonTools.addCellData("id", "液厂名称", "价格时间", "价格", "备注", "", "", "",  "", row, style);
		            
		            while(i < maxRow){
		            	//获取每一行的单元格   
		                cell1=sheet.getCell(0,i);//（列，行）  
		                if("".equals(cell1.getContents())==true)    //如果读取的数据为空  
		                    break;   
		                String gfName = sheet.getCell(4,i).getContents().replace(" ", "").replace("\t", "");//液厂名称
		                String date = sheet.getCell(2,i).getContents().replace(" ", "").replace("\t", "");//时间
		                String remark = sheet.getCell(3,i).getContents().replace(" ", "").replace("\t", "");//备注
		                row = sheet1.createRow(i);
		                List<GasFactory> gfList = gfs.listInfoByOpt(gfName, "", "", "", "", 1);
	                	if(gfList.size() > 0) {
	                		String gfId = gfList.get(0).getId();
	                		List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gfId, 0, date);
	                		if(lpdList.size() > 0) {
	                			LngPriceDetail lpd = lpdList.get(0);
	                			lpd.setRemark(remark);
	                			String lpdId = lpds.addOrUpdate(lpd);
	                			lprs.saveOrUpdate(new LngPriceRemark(lpd.getGf(), remark,date+" 08:08:08"));
	                			CommonTools.addCellData(lpdId, gfName, lpd.getPriceTime(), remark, "", "", "", "", "", row, style);
	                		}
	                	}else {
	                		CommonTools.addCellData("找不到该液厂", gfName, "", remark, "", "", "", "", "", row, style);
	                	}
	                	i++;
		            }
		            String absoFilePath = "d:\\批量液厂价格备注_"+CurrentTime.getStringTime1()+".xls";
		            FileOutputStream fout = new FileOutputStream(absoFilePath);  
		            wb.write(fout);  
		            fout.close();  
		            wb.close();
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PutMapping("upGasFactory")
	@ApiOperation(value = "修改液厂--后台人员",notes = "后台人员修改液厂信息时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "name", value = "液厂名称", required = true),
		@ApiImplicitParam(name = "facImage", value = "液厂图片", required = true),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号", required = true),
		@ApiImplicitParam(name = "province", value = "省", required = true),
		@ApiImplicitParam(name = "city", value = "市"),
		@ApiImplicitParam(name = "county", value = "县"),
		@ApiImplicitParam(name = "address", value = "详细地址", required = true),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "yzbgImg", value = "液质报告图")
	})
	public GenericResponse upGasFactory(HttpServletRequest request) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			String gfId = CommonTools.getFinalStr("gfId", request);
			String name = CommonTools.getFinalStr("name", request);
			String province = CommonTools.getFinalStr("province", request);
			String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
			String facImage = CommonTools.getFinalStr("facImage", request);
			String city = CommonTools.getFinalStr("city", request);
			String county = CommonTools.getFinalStr("county", request);
			String address = CommonTools.getFinalStr("address", request);
			String lxName = CommonTools.getFinalStr("lxName", request);
			String lxTel = CommonTools.getFinalStr("lxTel", request);
			String yzbgImg = CommonTools.getFinalStr("yzbgImg", request);
			if(!name.equals("") && !province.equals("") && !gasTypeId.equals("")) {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request),Constants.UP_YC)) {
					GasFactory gf = gfs.getEntityById(gfId);
					if(gf != null) {
						if(!name.equals(gf.getName())) {
							//需要检查是否重复
							if(gfs.listInfoByOpt(name, "", "", "", "", -1).size() == 0) {
								gf.setName(name);
								gf.setNamePy(CommonTools.getFirstSpell(name));
							}else {
								status = 50003;
							}
						}else {
							gf.setName(name);
							gf.setNamePy(CommonTools.getFirstSpell(name));
						}
						if(status.equals(200)) {
							gf.setFacImage(CommonTools.dealUploadDetail(userId, gf.getFacImage(),facImage));
							gf.setGasType(gts.findById(gasTypeId));
							if(!gasTypeId.equals(gf.getGasType().getId())) {//液质类型变更
								//需要修改液厂的排序
								//获取指定省份的排序
								GasType gt = gts.findById(gasTypeId);
								if(gt != null) {
									Integer orderNo = 0;
									Integer orderSubNo = gfs.listInfoByOpt("", "", gasTypeId, province, "", -1).size() + 1;
									if(gt.getName().equals("海气")) {//变更为海气时需要修改
										HqProvinceOrder prov = hpos.getEntityByOpt(0, province);
										if(prov != null) {
											orderNo = prov.getOrderNo();
											gf.setOrderNo(orderNo);
											gf.setOrderSubNo(orderSubNo);
										}
									}
								}
							}
							gf.setProvince(province);
							gf.setCity(city);
							gf.setCounty(county);
							gf.setAddress(address);
							gf.setLxName(lxName);
							gf.setLxTel(lxTel);
							gf.setYzbgImg(CommonTools.dealUploadDetail(userId, gf.getYzbgImg(),yzbgImg));
							uId = gfs.addOrUpGasFactory(gf);
						}
					}else {
						status = 50001;
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
		return ResponseFormat.retParam(status, uId);
	}
	
	@PutMapping("checkGf")
	@ApiOperation(value = "审核液厂",notes = "后台人员审核液厂信息时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态（1：审核通过,2：审核未通过）")
	})
	public GenericResponse checkGs(HttpServletRequest request) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		String gfId = CommonTools.getFinalStr("gfId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		try {
			if(gfId.equals("") || checkStatus.equals(0)) {
				status = 10002;
			}else {
				if(CommonTools.checkAuthorization(userId,CommonTools.getLoginRoleName(request), Constants.CHECK_YC)) {
					GasFactory gf = gfs.getEntityById(gfId);
					if(gf != null) {
						gf.setCheckStatus(checkStatus);
						uId = gfs.addOrUpGasFactory(gf);
					}else {
						status = 50001;
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
		return ResponseFormat.retParam(status, uId);
	}
	
	@GetMapping("getGasFactoryDetail")
	@ApiOperation(value = "获取指定主键的液厂详细信息",notes = "获取指定主键的液厂详细信息")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = false)
	})
	public GenericResponse getGasFactoryDetail(HttpServletRequest request,String gfId) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			gfId = CommonTools.getFinalStr(gfId);
			if(gfId.equals("")) {
				status = 50001;
			}else {
				GasFactory gf = gfs.getEntityById(gfId);
				if(gf != null) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("gfId", gf.getId());
					map.put("name", gf.getName());
					map.put("namePy", gf.getNamePy());
					map.put("facImage", gf.getFacImage());
					map.put("gasTypeId", gf.getGasType().getId());
					map.put("gasTypeName", gf.getGasType().getName());
					map.put("province", gf.getProvince());
					map.put("city", gf.getCity());
					map.put("county", gf.getCounty());
					map.put("address", gf.getAddress());
					map.put("lxName", gf.getLxName());
					map.put("lxTel", gf.getLxTel());
					map.put("addTime", gf.getAddTime());
					map.put("yzbg", gf.getYzbgImg());
					map.put("checkStatus", gf.getCheckStatus());
					//获取该液厂所有的贸易商列表
//					1122
					list.add(map);
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("getGasFactoryList")
	@ApiOperation(value = "获取指定贸易商公司的液厂列表",notes = "获取指定贸易商公司的液厂列表")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cpyId", value = "液厂编号",required = true)
	})
	public GenericResponse getGasFactoryList(HttpServletRequest request) {
		Integer status = 200;
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if(cpyId.equals("")) {
				status = 10002;
			}else {
				List<GasFactoryCompany> gfcList = gfcs.listCompanyByGfId("", cpyId, 1);
				if(gfcList.size() > 0) {
					for(GasFactoryCompany gfc:gfcList) {
						Map<String,Object> map = new HashMap<String,Object>();
						GasFactory gf = gfc.getGasFactory();
						GasType gt = gf.getGasType();
						map.put("id", gfc.getId());
						map.put("gfName", gf.getName());
						map.put("gfId", gf.getId());
						map.put("gasTypeId", gt.getId());
						map.put("gasTypeName", gt.getName());
						map.put("headImg", gt.getYzImg());
						list.add(map);
					}
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("getGasFactoryData")
	@ApiOperation(value = "获取液厂信息列表",notes = "为空时不查询，审核状态为-1时不查询")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称"),
		@ApiImplicitParam(name = "namePy", value = "液厂名称首字母"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号"),
		@ApiImplicitParam(name = "province", value = "省"),
		@ApiImplicitParam(name = "provincePy", value = "省首字母"),
		@ApiImplicitParam(name = "county", value = "县"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)"),
		@ApiImplicitParam(name = "owerUserId", value = "液厂拥有人")
	})
	public GenericResponse getGasFactoryData(HttpServletRequest request,String name,String namePy,String gasTypeId,
			String province,String provincePy,String county,Integer checkStatus,String owerUserId) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			if(checkStatus == null) {
				checkStatus = -1;
			}
			name = CommonTools.getFinalStr(name);
			province = CommonTools.getFinalStr(province);
			gasTypeId = CommonTools.getFinalStr(gasTypeId);
			county = CommonTools.getFinalStr(county);
			List<GasFactory> gfList = gfs.listInfoByOpt(name, namePy, gasTypeId, province, provincePy, checkStatus);
			if(gfList.size() == 0) {
				status = 50001;
			}else {
				for(GasFactory gf : gfList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("gfId", gf.getId());
					map.put("name", gf.getName());
					map.put("namePy", gf.getNamePy());
					map.put("gasTypeName", gf.getGasType().getName());
					map.put("province", gf.getProvince());
					map.put("yzbg", gf.getYzbgImg());
					list.add(map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("getPageGasFactoryData")
	@ApiOperation(value = "分页获取液厂信息列表",notes = "为空时不查询，审核状态为-1时不查询")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称"),
		@ApiImplicitParam(name = "namePy", value = "液厂名称首字母"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号"),
		@ApiImplicitParam(name = "province", value = "省"),
		@ApiImplicitParam(name = "provincePy", value = "省首字母"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)"),
		@ApiImplicitParam(name = "owerUserId", value = "液厂拥有人"),
//		@ApiImplicitParam(name = "opt", value = "使用范围（0:后台，1：前台）"),
		@ApiImplicitParam(name = "page", value = "页码",dataType = "integer"),
		@ApiImplicitParam(name = "limit", value = "每页记录条数",dataType = "integer")
	})
	public PageResponse getPageGasFactoryData(HttpServletRequest request ) {
		String name = CommonTools.getFinalStr("name", request);
		String namePy = CommonTools.getFinalStr("namePy", request);
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
		String province = CommonTools.getFinalStr("province", request);
		String provincePy = CommonTools.getFinalStr("provincePy", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		String owerUserId = CommonTools.getFinalStr("owerUserId", request);
		Integer pageIndex =  CommonTools.getFinalInteger("page", request);
		Integer pageSize =  CommonTools.getFinalInteger("limit", request);
		Integer status = 200;
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			if(pageIndex.equals(0)) {
				pageIndex = 1;
			}
			if(pageSize.equals(0)) {
				pageSize = 10;
			}
			name = CommonTools.getFinalStr(name);
			province = CommonTools.getFinalStr(province);
			gasTypeId = CommonTools.getFinalStr(gasTypeId);
			provincePy = CommonTools.getFinalStr(provincePy);
			Page<GasFactory> page = gfs.listPageInfoByOpt(name, namePy, gasTypeId, province, provincePy,
					checkStatus, owerUserId, 0,pageIndex, pageSize);
			count = page.getTotalElements();
			if(count == 0) {
				status = 50001;
			}else {
				for(GasFactory gf : page) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("gfId", gf.getId());
					map.put("name", gf.getName());
					map.put("namePy", gf.getNamePy());
					map.put("facImage", gf.getFacImage());
					map.put("gasTypeName", gf.getGasType().getName());
					map.put("province", gf.getProvince());
					map.put("city", gf.getCity());
					map.put("county", gf.getCounty());
					map.put("address", gf.getAddress());
					map.put("lxName", gf.getLxName());
					map.put("addTime", gf.getAddTime());
					map.put("yzbg", gf.getYzbgImg());
					map.put("checkStatus", gf.getCheckStatus());
					list.add(map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize,pageIndex,count,status, list);
	}
	
	@PutMapping("checkGasCompany")
	@ApiOperation(value = "审核申请贸易商",notes = "贸易商申请加入液厂时的申请")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfcId", value = "液厂贸易商编号", required = true),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)")
	})
	public GenericResponse checkGasCompany(HttpServletRequest request) {
		String gfcId = CommonTools.getFinalStr("gfcId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(!userId.equals("") && !gfcId.equals("")) {
				if(CommonTools.checkAuthorization(userId, CommonTools.getLoginRoleName(request),Constants.CHECK_GAS_FCY_CPY_APPLY)) {
					GasFactoryCompany gfc = gfcs.getEntityById(gfcId);
					if(gfc == null) {
						status = 50001;
					}else {
						gfc.setCheckStatus(checkStatus);
						gfc.setCheckTime(CurrentTime.getCurrentTime());
						uId = gfcs.saveOrUpdate(gfc);
						String result = "未审核通过";
						if(checkStatus.equals(1)) {
							result = "审核通过";
						}
						MessageCenter mc = new MessageCenter("","您提交的加入"+gfc.getGasFactory().getName()+"贸易商的申请"+result, "您提交的加入"+gfc.getGasFactory().getName()+"贸易商的申请"+result, 0, CurrentTime.getCurrentTime(), 2,
								gfcId, "joinGf", "", gfc.getAddUserId(), 0);
						mcs.saveOrUpdate(mc);
					}
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, uId);
	}
}
