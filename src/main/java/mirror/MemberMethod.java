package mirror;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class MemberMethod<T> extends RemoteMethod<T> implements IRemoteMember {

    public MemberMethod(Object that, Class<?> cls, Field field) throws NoSuchMethodException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}