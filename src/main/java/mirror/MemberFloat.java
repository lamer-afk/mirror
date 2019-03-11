package mirror;

import java.lang.reflect.Field;

public class MemberFloat extends RemoteFloat implements IRemoteMember {

    public MemberFloat(Object that, Class cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}