package mirror;

import java.lang.reflect.Field;

/**
 * Created by chaos on 2018/12/12 19:35
 * <p>
 * mail: 157688302@qq.com
 */
public class RefStaticBoolean extends RemoteBoolean {

    public RefStaticBoolean(Class<?> cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }
}
