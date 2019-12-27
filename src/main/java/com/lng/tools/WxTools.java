package com.lng.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lng.util.Constants;

public class WxTools {

	/**
	 * @description 自定义发送https请求
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月27日 上午9:11:52
	 * @param path
	 * @param method
	 * @param body
	 * @return
	 */
	public static String httpsRequestToString(String path, String method, String body) {
		if (path == null || method == null) {
			return null;
		}
		String response = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpsURLConnection conn = null;
		try {
			// 创建SSLConrext对象，并使用我们指定的信任管理器初始化
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			TrustManager[] tm = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
 
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
 
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
 
			} };
			sslContext.init(null, tm, new java.security.SecureRandom());
 
			// 从上面对象中得到SSLSocketFactory
			SSLSocketFactory ssf = sslContext.getSocketFactory();
 
			URL url = new URL(path);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
 
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
 
			// 设置请求方式（get|post）
			conn.setRequestMethod(method);
 
			// 有数据提交时
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}
 
			// 将返回的输入流转换成字符串
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
 
			response = buffer.toString();
		} catch (Exception e) {
 
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			try {
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
			} catch (IOException execption) {
 
			}
		}
		return response;
	}
	
	
	/**
	 * @description 微信小程序解密用户数据
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月27日 上午9:36:26
	 * @param encryptedData
	 * @param sessionKey
	 * @param iv
	 * @return
	 */
	public static JSONObject decryptionUserInfo(String encryptedData, String sessionKey, String iv) {
		// 被加密的数据
		Decoder  decoder = Base64.getDecoder();
		byte[] dataByte = decoder.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = decoder.decode(sessionKey);
		// 偏移量
		byte[] ivByte = decoder.decode(iv);
 
		try {
			// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return JSONObject.parseObject(result);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @description 发送请求用code换取sessionKey和相关信息
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月27日 上午9:37:54
	 * @param code
	 * @return
	 */
	public static JSONObject code2sessionKey(String code) {
		String stringToken = Constants.WX_URL+"?appid="+Constants.APP_ID
				+"&secret="+Constants.SECRET_KEY
				+"&js_code="+code+"&grant_type="+Constants.GRANT_TYPE;
		String response = WxTools.httpsRequestToString(stringToken, "GET", null);
		return JSON.parseObject(response);
	}
}
