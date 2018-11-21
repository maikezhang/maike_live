package cn.idongjia.live.restructure.domain.entity.tab;

import cn.idongjia.live.restructure.domain.entity.ValueObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 匠人类目
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
public class CraftmanCategoryV extends ValueObject {
    /**
     * 类目名称
     */
    private String name;
    /**
     * 类目id
     */
    private Long id;
}
