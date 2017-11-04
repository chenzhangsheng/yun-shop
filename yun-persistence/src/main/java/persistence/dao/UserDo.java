package persistence.dao;

import common.MyMapper;
import common.query.XinxirenUserQuery;
import domain.XinxirenUser;
import domain.bean.XinxirenUserBean;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * Created by ChenZhangsheng on 2017/10/17.
 */
public interface UserDo extends MyMapper<XinxirenUser> {

    /**
     * 根据条件查询用户信息
     * @param xinxirenUserQuery
     */
    List<XinxirenUserBean> GetUserList(XinxirenUserQuery xinxirenUserQuery);
    /**
     * 添加账户信息
     * @param xinxirenUserQuery
     */
    void AddUser(XinxirenUserQuery xinxirenUserQuery);

    /**
     * 根据条件查询单一账户信息
     * @param xinxirenUserQuery
     */
    XinxirenUserBean SelectOne(XinxirenUserQuery xinxirenUserQuery);

    /**
     * 删除用户信息
     * @param userId
     */
    void deleteUser(@Param("userId") Long userId);

    /**
     * 修改用户信息
     * @param xinxirenUserQuery
     */
    void updateUser(XinxirenUserQuery xinxirenUserQuery);


}
