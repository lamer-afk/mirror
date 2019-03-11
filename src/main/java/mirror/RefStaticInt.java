package mirror;

import java.lang.reflect.Field;

public class RefStaticInt extends RemoteInt {

    public RefStaticInt(Class cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }
}