package mirror;

import java.lang.reflect.Field;

public class MemberLong extends RemoteLong implements IRemoteMember {

    public MemberLong(Object that, Class cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}