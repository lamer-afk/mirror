package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteFloat /* no need extends RemoteAccessible*/ {

    protected Object that;
    protected Field field;

    public RemoteFloat(Object that, Class cls, Field field) throws NoSuchFieldException {
        this.that = that;
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public float get() {
        try {
            return this.field.getFloat(that);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(float value) {
        try {
            this.field.setFloat(that, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}
