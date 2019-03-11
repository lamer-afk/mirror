package mirror;

import java.lang.reflect.Field;

/**
 * Created by chaos on 2018/12/12 17:20
 * <p>
 * mail: 157688302@qq.com
 */
public class MemberBoolean extends RemoteBoolean implements IRemoteMember {

    public MemberBoolean(Object that, Class<?> cls, Field field) throws NoSuchFieldException {
        super(that, cls, field);
    }

    @Override
    public Object getAttachObject() {
        return that;
    }
}
