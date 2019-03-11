package mirror;

import java.lang.reflect.Field;

/**
 * 是Static描述，构造器不存在非static情况，这不符合常理.
 *
 * @param <T>
 */
public class RefConstructor<T> extends RemoteConstructor<T> {

    public RefConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        super(cls, field);
    }
}