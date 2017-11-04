package domain;


import common.BaseDO;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by ChenZhangsheng on 2017/10/16.
 */
@Table(name ="role")
public class Role extends BaseDO implements Serializable {
    private String name;
    private String remarks;
    private Long parentId;
    private int usable;
    private String permission;
    public String getPermission() {return permission;}
    public void setPermission(String permission) {this.permission = permission;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public int getUsable() {
        return usable;
    }
    public void setUsable(int usable) {
        this.usable = usable;
    }


}
