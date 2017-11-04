package common;

/**
 * Created by zhangshengchen on 2017/10/19.
 */
public class AuthorizationRequestDO {
    private String rid;
    private Long deadline;
    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    public Long getDeadline() {
        return deadline;
    }
    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

}
