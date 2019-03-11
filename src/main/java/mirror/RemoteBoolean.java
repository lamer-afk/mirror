package mirror;

import java.lang.reflect.Field;

/**
 * Created by chaos on 2018/12/12 19:32
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteBoolean /* no need extends RemoteAccessible*/ {

    protected Object that;
    protected Field field;

    public RemoteBoolean(Object that, Class<?> cls, Field field) throws NoSuchFieldException {
        this.that = that;
        this.field = cls.getDeclaredField(RemoteCaller.getFeildName(field));
        this.field.setAccessible(true);
    }

    public boolean get() {
        try {
            return this.field.getBoolean(that);
        } catch (Exception e) {
            return false;
        }
    }

    public void set(boolean value) {
        try {
            this.field.setBoolean(that, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}
