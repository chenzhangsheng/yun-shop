package domain;


import common.BaseDO;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by ChenZhangsheng on 2017/10/16.
 */
@Table(name ="permission")
public class Permission extends BaseDO implements Serializable {
    private String path;
    private String name;
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }




}
