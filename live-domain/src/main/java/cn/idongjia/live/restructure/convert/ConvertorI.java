package cn.idongjia.live.restructure.convert;

/**
 * @author lc
 * @create at 2018/7/7.
 */
public interface ConvertorI<C, E, D> {

    default public C entityToClient(E entityObject){return null;}

    default public C dataToClient(D dataObject){return null;}

    default public E clientToEntity(C clientObject){return null;}

    default public E dataToEntity(D dataObject){return null;}

    default public D entityToData(E entityObject){return null;}

}
