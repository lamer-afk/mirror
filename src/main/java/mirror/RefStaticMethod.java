package mirror;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
@SuppressWarnings("unchecked")
public class RefStaticMethod<T> extends RemoteMethod<T> {

    public RefStaticMethod(Class<?> cls, Field field) throws NoSuchMethodException {
        super(null, cls, field);
    }
}
