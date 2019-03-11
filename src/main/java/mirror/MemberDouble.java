package mirror;

import java.lang.reflect.Field;

public class MemberDouble extends RemoteDouble implements IRemoteMember{

    public MemberDouble(Object that, Class cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}