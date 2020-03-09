package com.lng.tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * 文件操作类
 * 文件的复制，删除，文件夹的删除
 * @author wm
 *
 */
public class FileOpration {
	
	//复制文件
	public static boolean copyFile(String oldPath,String newPath){
		 try {            
			 int bytesum = 0;            
			 int byteread = 0; 
			 File file = new File(oldPath);
			 if (file.exists()) { //文件存在时                
				 InputStream inStream = new FileInputStream(oldPath); //读入原文件              
				 FileOutputStream fs = new FileOutputStream(newPath);                
				 byte[] buffer = new byte[1444];                          
				 while ( (byteread = inStream.read(buffer)) != -1) {
					 bytesum += byteread; //字节数 文件大小                                  
					 fs.write(buffer, 0, byteread);               
				 }
				 inStream.close();  
				 fs.flush();
				 fs.close();
				 return true;
			 }
			 file = null;
		 }catch (Exception e) {            
			 System.out.println("复制单个文件操作出错");            
			 e.printStackTrace();       
		 }
		return false; 
	}
	//删除文件
	public static boolean deleteFile(String filePath){
		boolean flag = false;
		File file = new File(filePath);
		if(file.isFile() && file.exists()) {   
	        flag = file.delete();   
	    }
		if(!file.exists()){
			flag = true; 
		}
		file = null;
		return flag;
	}
	//删除文件夹里面的所有文件和文件夹
	public static boolean deleteAllFile(String path){
		File file = new File(path);
		if(!file.exists()){
			return false;
		}
		if(!file.isDirectory()){
			file.delete();
		}else if(file.isDirectory()){
			String[] tempList = file.list();
			for (int i = 0; i < tempList.length; i++) {
				File delfile = new File(path + "\\" + tempList[i]); 
				if(!delfile.isDirectory()){
					delfile.delete();  
				}else if(delfile.isDirectory()){
					deleteAllFile(path + "\\" + tempList[i]);  
				}
			}
			file.delete();  
		}
		file = null;
		return true;
	}
	
	/**
	 * 获取文件大小
	 * @description
	 * @author wm
	 * @date 2018-9-6 上午10:46:14
	 * @param filePath
	 * @return
	 */
	public static String getFileSize(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			return CommonTools.convertInputNumber((file.length() / 1024.0)) + "KB";
		}
		file = null;
		return "";
	}
	
	/**
	  * 等比缩放图片
	  * @description
	  * @author Administrator
	  * @date 2019-4-15 下午04:59:49
	  * @param url
	  * @param newWidth
	  * @param newHeight
	  * @param newUrl
	  * @param formatName 生成图片的格式
	  * @throws Exception
	  */
	 public static void makeImage(String url, Double rate, String newUrl, String formatName)throws Exception{
		 long startTime=System.currentTimeMillis();
		 //读取图片
		 File file_1 = new File(url);
	     BufferedImage bi = ImageIO.read(file_1);
	     Integer width_old = bi.getWidth();//原始尺寸
	     Integer height_old = bi.getHeight();
//	     //用Image里的方法对图片进行等比压缩,只要宽和高其一值为负,则以正的那个值为最大边进行等比压缩
//	     Image image = bi.getScaledInstance((int)(width_old * rate), -1,Image.SCALE_AREA_AVERAGING);
//	     int height = image.getHeight(null);
//	     int width = image.getWidth(null);
//	     //以新的高和宽构造一个新的缓存图片
//	     BufferedImage bi1 = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
//	     Graphics g = bi1.getGraphics();
//	     //在新的缓存图片中画图
//	     g.drawImage(image, 0, 0, null);
//	     //构造IO流输出到文件
//	     File file_2 = new File(newUrl);
//	     FileOutputStream fos = new FileOutputStream(file_2);
//	     ImageIO.write(bi1, formatName, fos);
//	     fos.flush();
//	     fos.close();
//	     file_1 = null;//file置为null是为了告诉gc此块内存可以回收
//	     file_2 = null;//file置为null是为了告诉gc此块内存可以回收
	     
	     //下列方法效率高
	     Integer newWidth = (int)(width_old * rate);
	     Integer newHeight = (int)(height_old * rate);
	     FileOutputStream out = null;
         try {
             BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
             tag.getGraphics().drawImage(bi, 0, 0, newWidth, newHeight, null);
             out = new FileOutputStream(newUrl);
             ImageIO.write(tag, formatName, out);
             out.flush();
             out.close();
             tag.flush();
             tag = null;
             bi.flush();
             bi = null;
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (out != null) {
                 out.close();
             }
             file_1 = null;//file置为null是为了告诉gc此块内存可以回收
             bi = null;
         }
	     long endTime=System.currentTimeMillis();
		 float excTime=(float)(endTime-startTime)/1000;
		 System.out.println("等比压缩耗费时间--"+excTime);
	 }
	 
	 /**
	  * @description 获取图片格式
	  * @author wm
	  * @Version : 版本
	  * @ModifiedBy : 修改人
	  * @date  2019年12月11日 下午1:45:50
	  * @param suffix
	  * @return
	  */
	 public static String getImageFormat(String suffix) {
		 String formatName = "";
		 if(suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")){
			formatName = "JPEG";
		 }else if(suffix.equalsIgnoreCase("png")){
			formatName = "PNG";
		 }else if(suffix.equalsIgnoreCase("bmp")){
			formatName = "BMP";
		 }else if(suffix.equalsIgnoreCase("gif")){
			formatName = "GIF";
		 }
	 	return  formatName;
	 }
	 
	 public static void main(String[] args) throws Exception{
		 for(int i = 1 ; i <= 9 ; i++){
//			 long systime = new Date().getTime();//当前系统时间
//			 FileOpration.makeImage("d:/12.jpg", i/10.0, "d:/12"+i+".jpg", "JPEG");
//			 long oldtime = new Date().getTime();//相比较的时间
//			 Long time = (oldtime - systime);//相差毫秒数
//			 System.out.println("按照"+(i*10)+"%等比压缩图片耗费时间："+time+"毫秒");
		 }
		 FileOpration.makeImage("d:/IMG_20200228_172347.jpg", 0.5, "d:/1a1a1aaaa.jpg", "JPEG");
	 }
}
