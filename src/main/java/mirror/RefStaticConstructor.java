package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public class RefStaticConstructor<T> extends RemoteConstructor<T> {

    public RefStaticConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        super(cls, field);
    }
}
