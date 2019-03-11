package mirror;

import java.lang.reflect.Field;

public class RefLong extends RemoteLong {

    public RefLong(Class cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }

    @Override
    public long get() {
        throw RefCreater.createNotSupportException();
    }

    @Override
    public void set(long value) {
        throw RefCreater.createNotSupportException();
    }

    public long get(Object object) {
        try {
            return this.field.getLong(object);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(Object obj, long value) {
        try {
            this.field.setLong(obj, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}