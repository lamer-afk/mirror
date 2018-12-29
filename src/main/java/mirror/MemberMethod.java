package mirror;

import java.lang.reflect.Field;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
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