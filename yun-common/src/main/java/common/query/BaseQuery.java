package common.query;

/**
 * Created by zhangshengchen on 2017/10/19.
 */
public class BaseQuery {
    private Long id;
    private int pageNo=1;
    private int rowCount=10;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
