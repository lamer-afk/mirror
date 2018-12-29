package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteConstructor<T> extends RemoteAccessible {

    protected Constructor<?> ctor;

    public RemoteConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        initRemote(cls, field);
        if (field.isAnnotationPresent(MethodParams.class)) {
            Class<?>[] types = field.getAnnotation(MethodParams.class).value();
            ctor = cls.getDeclaredConstructor(types);
        } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
            String[] values = field.getAnnotation(MethodReflectParams.class).value();
            Class[] parameterTypes = new Class[values.length];
            int N = 0;
            while (N < values.length) {
                try {
                    parameterTypes[N] = Class.forName(values[N]);
                    N++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ctor = cls.getDeclaredConstructor(parameterTypes);
        } else {
            ctor = cls.getDeclaredConstructor();
        }
        if (ctor != null && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public T newInstance() {
        try {
            Object instance = ctor.newInstance();
            if (remoteCaller()) {
                instance = callRemote(instance);
            }
            return (T) instance;
        } catch (Exception e) {
            return null;
        }
    }

    public T newInstance(Object... params) {
        try {
            Object instance = ctor.newInstance(params);
            if (remoteCaller()) {
                instance = callRemote(instance);
            }
            return (T) instance;
        } catch (Exception e) {
            return null;
        }
    }
}
