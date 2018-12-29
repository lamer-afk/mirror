package mirror;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public class RemoteCaller {

    static boolean isRemoteCaller(AccessibleObject accessible) {
        if (accessible instanceof Field) {
            Type type = ((Field) accessible).getGenericType();
            return isRemoteCaller(type);
        } else if (accessible instanceof Method) {
            Type type = ((Method) accessible).getGenericReturnType();
            return isRemoteCaller(type);
        } else {
            throw RefCreater.createNotSupportException("accessible should be Field or Method..." + accessible);
        }
    }

    static boolean isRemoteCaller(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypes != null &&
                    actualTypes.length > 0 &&
                    actualTypes[0] instanceof Class) {
                Class actualType = (Class) actualTypes[0];
                return IRemoteObject.class.isAssignableFrom(actualType);
            }
        }
        return false;
    }

    static Class getReturnActualType(AccessibleObject accessible) {
        Type type;
        if (accessible instanceof Field) {
            type = ((Field) accessible).getGenericType();
        } else if (accessible instanceof Method) {
            type = ((Method) accessible).getGenericReturnType();
        } else {
            throw RefCreater.createNotSupportException("accessible should be Field or Method..." + accessible);
        }
        if (type instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypes != null &&
                    actualTypes.length > 0 &&
                    actualTypes[0] instanceof Class) {
                Class actualType = (Class) actualTypes[0];
                return actualType;
            }
        }
        throw RefCreater.createNotSupportException("getReturnActualType => not found IRemoteObject class " + accessible);
    }

    public static <T extends IRemoteObject> T convertToRemote(Object that, Class<T> remoteObjectClass) {
        Class returnClass = remoteObjectClass;

        try {
            Constructor constructor = returnClass.getConstructor();
            IRemoteObject remoteObject = (IRemoteObject) constructor.newInstance();
            RefCreater.relocationMember(remoteObjectClass, remoteObject, that.getClass(), that);
            return (T) remoteObject;
        } catch (Throwable e) {
            throw new RuntimeException("convertToRemote => that(" + that + "),remoteObjectClass(" + remoteObjectClass.getName() + ")", e);
        }
    }

    @SuppressWarnings("unchecked")
    static IRemoteObject convertToRemote(AccessibleObject accessible, Class realClass, Object realObject) {
        Class returnClass = getReturnActualType(accessible);

        try {
            Constructor constructor = returnClass.getConstructor();
            IRemoteObject remoteObject = (IRemoteObject) constructor.newInstance();

            RefCreater.relocationMember(returnClass, remoteObject, realClass, realObject);

            return remoteObject;
        } catch (Throwable e) {
            throw new RuntimeException("convertToRemote => realClass(" + realClass + ") failure", e);
        }
    }

    public static Object getAttachObject(IRemoteObject object) {
        if (object == null) {
            return null;
        }
        return RefCreater.findRealObject(object);
    }
}
