package com.lng.tools;


import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AuthImg extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//设置生成图形验证码里面字母的字体和大小
	public AuthImg() {
		super();
	}
    //servlet的响应方法
    public void getRandcode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	//设置响应的文件头
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        //表明生成的响应是图片，而非JSP页面
        response.setContentType("image/jpeg");
      //验证码，由2个一位数的加减法构成
        Integer num1 = (int)(Math.random() * 10);
        Integer num2 = (int)(Math.random() * 10);
        String verifyCode = "";
        String funMethod = "加";
        Integer result = 0;
        Random random = new Random();
        Integer funNo = random.nextInt(3);//产生【0,1,2之间的随机整数】
        if(funNo.equals(2)){//乘法
        	result = num1 * num2;
        	funMethod = "乘";
        	verifyCode = num1 + funMethod + num2 + "等几";
        }else{
            if(num1 >= num2){
            	funMethod = "减";
            	result = num1 - num2;
            }else{
            	result = num1 + num2;
            }
        	verifyCode = num1 + funMethod + num2 + "等几";
        }
    	
        String finalResult = String.valueOf(result);
        //将随机产生的字符串放在session中
        HttpSession session = request.getSession(true);
        session.setAttribute("rand",finalResult);
      //将图片输出到servlet响应
        VerifyCodeUtils.outputImage(150, 40, response.getOutputStream(), verifyCode);
        
    }
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		super.init();
	}

}
