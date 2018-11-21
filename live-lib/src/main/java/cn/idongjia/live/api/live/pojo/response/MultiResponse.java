package cn.idongjia.live.api.live.pojo.response;

import lombok.ToString;

import java.util.Collection;

/**
 * 多数据响应
 *
 * @param <T>
 */
@ToString(callSuper = true)
public class MultiResponse<T> extends Response {

    /**
     * 总数据量
     */
    private int total;

    /**
     * 查询结果
     */
    private Collection<T> data;

    public static <T> MultiResponse<T> of(Collection<T> data, int total) {
        MultiResponse<T> multiResponse = new MultiResponse<>();
        multiResponse.setSuccess(true);
        multiResponse.setData(data);
        multiResponse.setTotal(total);
        return multiResponse;
    }

    public static <T> MultiResponse<T> ofWithoutTotal(Collection<T> data) {
        return of(data, 0);
    }


    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }


    public Collection<T> getData() {
        return data;
    }


    public void setData(Collection<T> data) {
        this.data = data;
    }

    public static MultiResponse buildFailure(int errCode, String errMessage) {
        MultiResponse response = new MultiResponse();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static MultiResponse buildSuccess() {
        MultiResponse response = new MultiResponse();
        response.setSuccess(true);
        return response;
    }

}
