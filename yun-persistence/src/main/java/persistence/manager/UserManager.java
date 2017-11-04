package persistence.manager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import common.ErrConstatns;
import common.exception.PlatformRequestRuntimeException;
import common.query.XinxirenUserQuery;
import domain.bean.XinxirenUserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.UserDo;

import java.util.List;

/**
 * Created by zhangshengchen on 2017/10/20.
 */
@Service
public class UserManager {

    @Autowired
    private UserDo userDo;

    public Object GetUserList(XinxirenUserQuery xinxirenUserQuery) {
        PageHelper.startPage(xinxirenUserQuery.getPageNo(),xinxirenUserQuery.getRowCount());
        List<XinxirenUserBean> list = userDo.GetUserList(xinxirenUserQuery);
        PageInfo page = new PageInfo(list);
        return page;
    }

    public void addUser(XinxirenUserQuery xinxirenUserQuery) {
        XinxirenUserQuery query = new XinxirenUserQuery();
        query.setAccount(xinxirenUserQuery.getAccount());
        XinxirenUserBean user  =userDo.SelectOne(query);
        if(user!=null){
            throw new PlatformRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.E_ACCOUNT_ALREADY_EXIST), ErrConstatns.E_ACCOUNT_ALREADY_EXIST, HttpStatus.OK);
        }
        userDo.AddUser(xinxirenUserQuery);
    }


    public void deleteUser(@Param("userId") Long userId) {
        userDo.deleteUser(userId);
    }


    @Transactional
    public void updateUser(XinxirenUserQuery xinxirenUserQuery) {
        if(xinxirenUserQuery.getRid() == null || xinxirenUserQuery.getId() == null){
            throw new PlatformRequestRuntimeException(ErrConstatns.getCodeMessage(ErrConstatns.API3_PARAMETER_ERROR), ErrConstatns.API3_PARAMETER_ERROR, HttpStatus.OK);
        }
        userDo.updateUser(xinxirenUserQuery);
    }

    public XinxirenUserBean GetUser(XinxirenUserQuery xinxirenUserQuery) {
        XinxirenUserBean user  = userDo.SelectOne(xinxirenUserQuery);
        return user;
    }
}
