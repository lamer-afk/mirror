package mirror;

import java.lang.reflect.Field;

public class RefDouble extends RemoteDouble {

    public RefDouble(Class cls, Field field) throws NoSuchFieldException {
        super(null, cls, field);
    }

    @Override
    public double get() {
        throw RefCreater.createNotSupportException();
    }

    @Override
    public void set(double value) {
        throw RefCreater.createNotSupportException();
    }

    public double get(Object object) {
        try {
            return this.field.getDouble(object);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(Object obj, double value) {
        try {
            this.field.setDouble(obj, value);
        } catch (Exception e) {
            //Ignore
        }
    }
}