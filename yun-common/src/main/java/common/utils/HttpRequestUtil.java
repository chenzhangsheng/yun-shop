package common.utils;

import common.exception.InvalidRequestRuntimeException;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

;

public class HttpRequestUtil {
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 获取Request的Referer域名
	 * @param request
	 * @return
	 */
	public static String getRefererDomain(HttpServletRequest request){
		String domain=request.getHeader("Referer");
		return subDomain(domain);
	}
	/**
	 * 截取域名
	 * @param url
	 * @return
	 */
	public static String subDomain(String url){
		if(url==null)
			return null;
		Pattern p = Pattern.compile("(?<=://)(([\\w-]+\\.)+[\\w-]+(?<=/?))|(?<=://)([\\w-]+(?<=/?))",Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        if(matcher.find())        
        	return matcher.group();
        return null;
	}
	/**
	 * 获取JSON对象
	 * @param request
	 * @return

	public static JSONObject getPostJSONObject(HttpServletRequest request) {
		String reqstring = getPostJSON(request);
		if(StringUtils.isEmpty(reqstring))
			return null;
    	JSONObject json;
		try {
			json = JSONObject.fromObject(URLDecoder.decode(reqstring,"UTF-8"));
			return json;
		} catch (UnsupportedEncodingException e) {
			throw new InvalidRequestRuntimeException("HttpServletRequest getPostJSONObject error",e);
		}
    } */
    /**
     * 获取JSONString
     * @param request
     * @return
     */
    public static String getPostJSON(HttpServletRequest request) {
    	if(!isContentTypeEqualsJson(request))
    		throw new InvalidRequestRuntimeException("HttpServletRequest getPostJSON error","ContentType must be 'application/json'");
    	String result=null;
    	try {
			 result=inputStreamToString(request.getInputStream());
		} catch (IOException e) {
			throw new InvalidRequestRuntimeException("HttpServletRequest getInputStream to String error",e);
		}
		return result;
    }
    /**
     * 获取String
     * @param in
     * @return
     * @throws IOException
     */
	public static String inputStreamToString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n,"utf-8"));
		}
		return out.toString();
	}
	
	private static boolean isContentTypeEqualsJson(HttpServletRequest request){
		return request.getContentType() != null && request.getContentType().indexOf("application/json") != -1;
	}
	
	public static Integer getParameterInteger(HttpServletRequest request,String param){
		String intt = request.getParameter(param);
		if(StringUtils.isNotEmpty(intt) && StringUtils.isNumeric(intt))
			return Integer.valueOf(intt);
		return null;
	}
	
	public static Long getParameterLong(HttpServletRequest request,String param){
		String intt = request.getParameter(param);
		if(StringUtils.isNotEmpty(intt) && StringUtils.isNumeric(intt))
			return Long.valueOf(intt);
		return null;
	}

	/**
	 * 提取reqest的请求参数，生成对应的Bean
	 * 支持x-www-form-urlencoded、application/json type的请求
	 * x-www-form-urlencoded 请求参数可在body和url中
	 * @param request
	 * @param cls
	 * @param <T>
	 * @return
	 */
	public static <T> T getRequestQuerryBean(HttpServletRequest request, Class<T> cls){
		if(isContentTypeEqualsJson(request)){
			T result = JsonUtil.getObjectFromJson(getPostJSON(request),cls);
			return result;
		}else {
			String querry = covertRequestToMap(request);
			T result = JsonUtil.getObjectFromJson(querry,cls);
			return result;
		}

	}
	/**
	 * 提取x-www-form-urlencoded type 的reuest参数（body和url中），按json格式输出
	 * @param request
	 * @return
	 */
	private static String covertRequestToMap(HttpServletRequest request){
		Enumeration<String> names = request.getParameterNames();
		Map<String,Object> result = new LinkedMap();
		while (names.hasMoreElements()){
			String key = names.nextElement();
			result.put(key,request.getParameter(key));
		}
		return JsonUtil.getJsonFromObject(result);
	}
}
