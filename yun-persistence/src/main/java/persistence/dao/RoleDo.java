package persistence.dao;

import common.MyMapper;
import common.query.RoleQuery;
import domain.Permission;
import domain.Role;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * Created by zhangshengchen on 2017/10/19.
 */
public interface RoleDo extends MyMapper<Role> {
    /**
     * 根据条件查询角色组信息
     * @param roleQuery
     */
    List<Role> GetRoleList(RoleQuery roleQuery);
    /**
     * 添加角色信息
     * @param roleQuery
     */
    void addRole(RoleQuery roleQuery);

    /**
     * 删除角色信息
     * @param roleId
     */
    void deleteRole(@Param("roleId") Long roleId);

    /**
     * 修改角色信息
     * @param roleQuery
     */
    void updateRole(RoleQuery roleQuery);

    /**
     * 根据角色所在资源组信息
     * @param role
     */
    List<Permission> GetPermissionList(Role role);

    /**
     * 根据id查询角色
     * @param roleId
     */
    Role GetRole(@Param("roleId") Long roleId);

}
