package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public class RefConstructor<T> extends RemoteConstructor<T> {

    public RefConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        super(cls, field);
    }
}