package domain;


import common.BaseDO;

/**
 * Created by ChenZhangsheng on 2017/10/16.
 */
public class XinxirenUser extends BaseDO {
    private String account;
    private String password;
    private String phoneNumber;
    private String userName;
    private int authorize;
    private String remarks;
    private Long rid;
    public Long getRid() {return rid;}
    public void setRid(Long rid) {this.rid = rid;}
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getAuthorize() {
        return authorize;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAuthorize(int authorize) {
        this.authorize = authorize;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
