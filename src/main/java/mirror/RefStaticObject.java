package mirror;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class RefStaticObject<T> extends RemoteObject<T> {

    public RefStaticObject(Class<?> cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }
}