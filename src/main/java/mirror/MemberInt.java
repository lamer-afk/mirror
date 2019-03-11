package mirror;

import java.lang.reflect.Field;

public class MemberInt extends RemoteInt implements IRemoteMember {

    public MemberInt(Object that, Class cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}