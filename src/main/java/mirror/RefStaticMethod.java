package mirror;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class RefStaticMethod<T> extends RemoteMethod<T> {

    public RefStaticMethod(Class<?> cls, Field field) throws NoSuchMethodException {
        super(null, cls, field);
    }
}
