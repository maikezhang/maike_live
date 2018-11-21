package cn.idongjia.live.restructure.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端对象
 *
 * @author lc
 * @create at 2018/7/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ClientObject implements Serializable {

    /**
     * This is for extended values
     */
    protected Map<String, Object> extValues;

    public Object getExtField(String key) {
        if (extValues != null) {
            return extValues.get(key);
        }
        return null;
    }

    public void putExtField(String fieldName, Object value) {
        if (extValues == null) {
            extValues = new HashMap<>();
        }
        this.extValues.put(fieldName, value);
    }

    public Map<String, Object> getExtValues() {
        return extValues;
    }

    public void setExtValues(Map<String, Object> extValues) {
        this.extValues = extValues;
    }
}
