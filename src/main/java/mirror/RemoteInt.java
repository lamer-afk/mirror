package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteInt  /* no need extends RemoteAccessible */ {

    protected Object that;
    protected Field field;

    public RemoteInt(Object that, Class cls, Field field) throws NoSuchFieldException {
        this.that = that;
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public int get() {
        try {
            return this.field.getInt(that);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(int intValue) {
        try {
            this.field.setInt(that, intValue);
        } catch (Exception e) {
            //Ignore
        }
    }
}
