package persistence.manager;

import common.ErrConstatns;
import common.exception.PlatformRequestRuntimeException;
import common.query.RoleQuery;
import common.query.XinxirenUserQuery;
import domain.Permission;
import domain.Role;
import domain.bean.XinxirenUserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import persistence.dao.RoleDo;
import persistence.dao.UserDo;


import java.util.List;

/**
 * Created by zhangshengchen on 2017/10/20.
 */
@Service
public class RoleManager {

    @Autowired
    private RoleDo roleDo;
    @Autowired
    private UserDo userDo;


    public List<Role> GetRoleList(RoleQuery roleQuery) {
        List<Role> list = roleDo.GetRoleList(roleQuery);
        return list;
    }

    public void addRole(RoleQuery roleQuery) {
        roleDo.addRole(roleQuery);
    }

    public void deleteRole(@Param("roleId") Long roleId) {
        XinxirenUserQuery xinxirenUserQuery = new XinxirenUserQuery();
        xinxirenUserQuery.setRid(roleId);
        List<XinxirenUserBean> list = userDo.GetUserList(xinxirenUserQuery);
        if(list.size()>0){
            throw new PlatformRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_ACCOUNT_EXIST), ErrConstatns.API3_ACCOUNT_EXIST, HttpStatus.OK);
        }
        roleDo.deleteRole(roleId);
    }

    public void updateRole(RoleQuery roleQuery) {
        roleDo.updateRole(roleQuery);
    }

    public List<Permission> GetPermissionList(Long rid){
        Role role = roleDo.GetRole(rid);
        return roleDo.GetPermissionList(role);
    }

}
