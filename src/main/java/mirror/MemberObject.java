package mirror;


import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
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