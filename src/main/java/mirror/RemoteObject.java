package mirror;

import java.lang.reflect.Field;

/**
 * Created by chaos on 2018/12/12 19:28
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteObject<T> extends RemoteAccessible {
    protected Object that;
    protected Field field;

    public RemoteObject(Object that, Class<?> cls, Field field) throws NoSuchFieldException {
        initRemote(cls, field);
        this.that = that;
        this.field = cls.getDeclaredField(RemoteCaller.getFeildName(field));
        this.field.setAccessible(true);
    }

    public T get() {
        try {
            Object ret = this.field.get(that);
            if (remoteCaller()) {
                ret = callRemote(ret);
            }
            return (T) ret;
        } catch (Exception e) {
            return null;
        }
    }

    public void set(T value) {
        try {
            Object v;
            //处理一下remote类型
            if (value instanceof IRemoteObject) {
                v = realObject((IRemoteObject) value);
            }else{
                v = value;
            }
            this.field.set(that, v);
        } catch (Exception e) {
            //Ignore
        }
    }
}
