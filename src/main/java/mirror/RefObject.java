package mirror;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class RefObject<T> extends RemoteObject<T> {

    public RefObject(Class<?> cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }

    @Override
    public T get() {
        throw RefCreater.createNotSupportException();
    }

    @Override
    public void set(T value) {
        throw RefCreater.createNotSupportException();
    }

    public T get(Object object) {
        try {
            Object ret = this.field.get(object);
            if (remoteCaller()) {
                ret = callRemote(ret);
            }
            return (T) ret;
        } catch (Exception e) {
            return null;
        }
    }

    public void set(Object obj, T value) {
        try {
            this.field.set(obj, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}