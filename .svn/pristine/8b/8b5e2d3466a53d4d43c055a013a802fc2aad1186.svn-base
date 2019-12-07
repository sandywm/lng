
package com.lng.tools;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class Ability
{

    public Ability()
    {
    }
  //打印出没有权限对话框（带导向地址）
    public static void PrintAuthorizeScript(String url, String authorizeScript, HttpServletResponse response)
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String script = "";
        script += "<script language=\"Javascript\">\n";
        script += url + "\n";
        script += "alert('" + authorizeScript+ "')\n";
        script += "</script>\n";
        try
        {
            response.getWriter().print(script);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
}
