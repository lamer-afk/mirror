package mirror;


import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class MemberObject<T> extends RemoteObject<T> implements IRemoteMember {

    public MemberObject(Object that, Class<?> cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}