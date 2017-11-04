package domain.bean;



import common.AccessTokenBean;
import domain.Permission;
import domain.Role;
import domain.XinxirenUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangshengchen on 2017/10/17.
 */
public class XinxirenUserBean extends XinxirenUser implements Serializable {

    private Role role;
    private List<Permission> permissionList;
    private AccessTokenBean accessTokenBean;

    public AccessTokenBean getAccessTokenBean() {return accessTokenBean;}

    public void setAccessTokenBean(AccessTokenBean accessTokenBean) {this.accessTokenBean = accessTokenBean;}

    public List<Permission> getPermissionList() {return permissionList;}

    public void setPermissionList(List<Permission> permissionList) {this.permissionList = permissionList;}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
