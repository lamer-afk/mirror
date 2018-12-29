package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public class RefBoolean extends RemoteBoolean {

    public RefBoolean(Class cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }

    @Override
    public boolean get() {
        throw RefCreater.createNotSupportException();
    }

    @Override
    public void set(boolean intValue) {
        throw RefCreater.createNotSupportException();
    }

    public boolean get(Object object) {
        try {
            return this.field.getBoolean(object);
        } catch (Exception e) {
            return false;
        }
    }

    public void set(Object obj, boolean value) {
        try {
            this.field.setBoolean(obj, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}