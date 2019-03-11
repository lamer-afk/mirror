package mirror;

import java.lang.reflect.Field;

/**
 * Created by chaos on 2018/12/13 17:47
 * <p>
 * mail: 157688302@qq.com
 */
public class RefStaticConstructor<T> extends RemoteConstructor<T> {

    public RefStaticConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        super(cls, field);
    }
}
