package springboot.controller;

import common.AccessTokenBean;
import common.AuthorizationRequestDO;
import common.ErrConstatns;
import common.exception.InvalidRequestRuntimeException;
import common.exception.PlatformRequestRuntimeException;
import common.query.XinxirenUserQuery;
import common.utils.AESUtil;
import common.utils.HmacSHA1Util;
import common.utils.JsonUtil;
import domain.bean.XinxirenUserBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import persistence.manager.UserManager;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ChenZhangsheng on 2017/10/17.
 */
public class BaseAction {
    @Autowired
    private UserManager userManager;

    protected Logger log = Logger.getLogger(this.getClass());
    public static final String DEFAULT_CHARSET = "UTF-8";
    private final int ACCESS_TOKEN_EXPIRSIN = 7200;//两个小时
    private final String ACCESS_TOKEN_HEADER = "Auth ";
    public static final String ACCESS_TOKEN_AESKEY = "nbuxinxiren";

    public void sendJsonMsg(String msg,HttpServletResponse response){
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.write(msg);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("send json error", e);
        }
    }
    public ExceptionErrorMessage getExceptionErrorMessage(Exception ex) {
        int errCode = -1;
        HttpStatus httpStatus = HttpStatus.OK;
        String mString = "";
        if (ex instanceof InvalidRequestRuntimeException) {
            InvalidRequestRuntimeException exception = (InvalidRequestRuntimeException)ex;
            errCode = exception.getErr();
            mString =  exception.getMessage();
        } else if (ex instanceof PlatformRequestRuntimeException) {
            PlatformRequestRuntimeException exception = (PlatformRequestRuntimeException)ex;
            errCode = exception.getErr();
            mString =  exception.getMessage();
        }else{
            errCode = 500;
            mString =  ex.getMessage();
        }

        return new ExceptionErrorMessage(errCode, httpStatus, mString);
    }


    /**
     *  异常信息实体
     * @author chaogao
     */
    public class ExceptionErrorMessage {
        private int errCode = -1;
        private HttpStatus httpStatus = HttpStatus.OK;
        private String errMsg = "";

        public ExceptionErrorMessage(int errCode, HttpStatus httpStatus, String errMsg) {
            super();
            this.errCode = errCode;
            this.httpStatus = httpStatus;
            this.errMsg = errMsg;
        }
        public int getErrCode() {
            return errCode;
        }
        public void setErrCode(int errCode) {
            this.errCode = errCode;
        }
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }
        public void setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }
    }

    /**
     * 获取 access token
     * @param acconut
     * @param password
     * @return
     */
    public AccessTokenBean getAccessToken(String acconut, String password) {
        //check format
        if (StringUtil.isEmpty(acconut) || StringUtil.isEmpty(password)) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_PARAMETER_ERROR),ErrConstatns.API3_PARAMETER_ERROR,HttpStatus.OK);
        }
        //generate access token
        AccessTokenBean appAccessTokenResBean = new AccessTokenBean();
        Date expireDate = DateUtils.addSeconds(new Date(), ACCESS_TOKEN_EXPIRSIN);
        AuthorizationRequestDO authorizationRequestDO = new AuthorizationRequestDO();
        authorizationRequestDO.setRid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        authorizationRequestDO.setDeadline(expireDate.getTime()/1000);

        String json = JsonUtil.getJsonFromObject(authorizationRequestDO);

        String encodeJsonBase64 = null;
        try {
            encodeJsonBase64 = new String(Base64.encodeBase64(json.getBytes(DEFAULT_CHARSET), false, true));
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("system unsupport encoding %s", DEFAULT_CHARSET));
        }
        byte[] sign = null;
        try {
            sign = HmacSHA1Util.getSignatureBytes(encodeJsonBase64, acconut+password);
        } catch (Exception e) {
            log.error("sha1 sign error");
        }
        String encode_sign = new String(Base64.encodeBase64(sign, false, true));

        String accessToken = String.format("%s:%s:%s", acconut, encode_sign, encodeJsonBase64);
        accessToken = new String(Base64.encodeBase64(AESUtil.encrypt(accessToken, ACCESS_TOKEN_AESKEY), false, true));

        appAccessTokenResBean.setAccessToken(accessToken);
        appAccessTokenResBean.setExpiresIn(Long.valueOf(ACCESS_TOKEN_EXPIRSIN));

        return appAccessTokenResBean;
    }

    /**
     * 验证 accessToken
     * @param accessToken
     * @return
     */
    public void validAccessToken(String accessToken) {
        //check format
        if (StringUtil.isEmpty(accessToken)) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_PARAMETER_ERROR),ErrConstatns.API3_PARAMETER_ERROR,HttpStatus.OK);
        }
        byte[] accessTokenBytes = null;
        try {
            accessTokenBytes = Base64.decodeBase64(accessToken.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e1) {
            log.error(String.format("system unsupport encoding %s", DEFAULT_CHARSET));
        }
        byte[] decBytes = AESUtil.decrypt(accessTokenBytes, ACCESS_TOKEN_AESKEY);
        decBytes = decBytes == null ? new byte[0] : decBytes;
        accessToken = new String(decBytes);

        String[] tokenStr = StringUtils.split(accessToken, ":");
        if (tokenStr.length != 3) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_PARAMETER_ERROR),ErrConstatns.API3_PARAMETER_ERROR,HttpStatus.OK);
        }
        //check key
        XinxirenUserQuery xinxirenUserQuery = new XinxirenUserQuery();
        xinxirenUserQuery.setAccount(tokenStr[0]);
        XinxirenUserBean xinxirenUserBean = userManager.GetUser(xinxirenUserQuery);
        if (xinxirenUserBean == null
                || StringUtil.isEmpty(xinxirenUserBean.getAccount())
                || StringUtil.isEmpty(xinxirenUserBean.getPassword())) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_AUTH_FAIL),ErrConstatns.API3_AUTH_FAIL,HttpStatus.OK);
        }

        //check secrert
        String encode_json = tokenStr[2];
        String encode_sign_client = tokenStr[1];
        String encode_sign = null;
        try {
            byte[] sign = HmacSHA1Util.getSignatureBytes(encode_json, xinxirenUserBean.getAccount()+xinxirenUserBean.getPassword());
            encode_sign = new String(Base64.encodeBase64(sign, false, true));
        } catch (Exception e) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_AUTH_FAIL),ErrConstatns.API3_AUTH_FAIL,HttpStatus.OK);
        }
        if (encode_sign == null || !encode_sign.toLowerCase().equalsIgnoreCase(encode_sign_client.toLowerCase())) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_AUTH_FAIL),ErrConstatns.API3_AUTH_FAIL,HttpStatus.OK);
        }
        //check rid & time
        String json_urlcode = new String(Base64.decodeBase64(encode_json));
        String json = json_urlcode;
        try {
            if (json.indexOf("%")>-1) {
                json = URLDecoder.decode(json, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_AUTH_FAIL),ErrConstatns.API3_AUTH_FAIL,HttpStatus.OK);
        }
        AuthorizationRequestDO authorizationRequestDO = JsonUtil.getObjectFromJson(json, AuthorizationRequestDO.class);
        log.debug(String.format("==============================rid-%s, deadline-%s", authorizationRequestDO.getRid(), authorizationRequestDO.getDeadline()));
        if (authorizationRequestDO.getDeadline()<System.currentTimeMillis()/1000) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_AUTH_EXPIRATION_TIME_ERROR),ErrConstatns.API3_AUTH_EXPIRATION_TIME_ERROR,HttpStatus.OK);
        }

    }

    /**
     * 获取 access token
     * @param acconut
     * @param password
     * @return
     */
    public AccessTokenBean getToken(String acconut, String password) {
        //check format
        if (StringUtil.isEmpty(acconut) || StringUtil.isEmpty(password)) {
            throw new InvalidRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_PARAMETER_ERROR), ErrConstatns.API3_PARAMETER_ERROR, HttpStatus.OK);
        }
            //generate access token
            AccessTokenBean appAccessTokenResBean = new AccessTokenBean();
            String accessToken = String.format("%s:%s:%s", acconut, password, ACCESS_TOKEN_AESKEY);
            accessToken = new String(Base64.encodeBase64(AESUtil.encrypt(accessToken, ACCESS_TOKEN_AESKEY), false, true));
            appAccessTokenResBean.setAccessToken(accessToken);
            appAccessTokenResBean.setExpiresIn(Long.valueOf(ACCESS_TOKEN_EXPIRSIN));
        return appAccessTokenResBean;
    }

    public static void main(String args[]){
        BaseAction action = new BaseAction();
        AccessTokenBean accessTokenBean = action.getToken("1232323","adaddasdas");
        System.out.println(accessTokenBean.getAccessToken());
    }
}
