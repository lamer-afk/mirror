package mirror;

import java.lang.reflect.Field;

public class RefFloat extends RemoteFloat {

    public RefFloat(Class cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }

    @Override
    public void set(float value) {
        throw RefCreater.createNotSupportException();
    }

    @Override
    public float get() {
        throw RefCreater.createNotSupportException();
    }

    public float get(Object object) {
        try {
            return this.field.getFloat(object);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(Object obj, float value) {
        try {
            this.field.setFloat(obj, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}