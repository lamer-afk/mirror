package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteDouble  /* no need extends RemoteAccessible*/ {

    protected Object that;
    protected Field field;

    public RemoteDouble(Object that, Class cls, Field field) throws NoSuchFieldException {
        this.that = that;
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public double get() {
        try {
            return this.field.getDouble(that);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(double value) {
        try {
            this.field.setDouble(that, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}
