package com.lng.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 *	图片植入水印
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月6日 下午5:22:25
 */
public class JunitImage {
	/**
     *	 给图片添加水印文字、可设置水印文字的旋转角度
     * @param logoText 要写入的文字
     * @param srcImgPath 源图片路径
     * @param newImagePath 新图片路径
     * @param degree 旋转角度
     * @param color  字体颜色
     * @param formaName 图片后缀
     */
    public static void markImageByText(String logoText, String srcImgPath,String newImagePath,Integer degree,Color color,String formaName) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            java.awt.Image srcImg = ImageIO.read(new File(srcImgPath));
//            Integer lastIndex = srcImgPath.lastIndexOf(".");
//			String suffix = "."+srcImgPath.substring(lastIndex+1);//图片后缀名
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            Integer width = buffImg.getWidth();
            Integer height = buffImg.getHeight();
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),  width/2,height/2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            Integer fontSize = width / 10;
            if(width > height) {
            	fontSize = height / 10;
            }
            g.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD, fontSize));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.25f));
            
            
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText,  (width- getwaterMarkLength(logoText,g)) / 2, height / 2);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(newImagePath);
            ImageIO.write(buffImg, formaName, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
	 * 获取水印的坐标
	 * @param watermarkContent 		水印内容	
	 * @param g		2d图像
	 * @return		水印的长度
	 */
	public static int getwaterMarkLength(String watermarkContent,Graphics2D g) {
	
		return	g.getFontMetrics(g.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());
				
	}
    
    public static void main(String[] args) {
    	markImageByText("power by sandy","D:/1.jpg","D:/1.jpg",45,new Color(0,0,0),"JPG");
    }
    
}
