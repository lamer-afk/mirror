package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by chaos on 2018/12/12 16:46
 * <p>
 * mail: 157688302@qq.com
 */
public class RefCreater {

    private static HashMap<Class<?>, Constructor<?>> REF_TYPES = new HashMap<Class<?>, Constructor<?>>();
    private static HashMap<Class<?>, Constructor<?>> REF_STATIC_TYPES = new HashMap<Class<?>, Constructor<?>>();

    static {
        try {
            //not static method
            REF_TYPES.put(MemberObject.class, MemberObject.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberInt.class, MemberInt.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberLong.class, MemberLong.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberFloat.class, MemberFloat.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberDouble.class, MemberDouble.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberBoolean.class, MemberBoolean.class.getConstructor(Object.class, Class.class, Field.class));
            REF_TYPES.put(MemberMethod.class, MemberMethod.class.getConstructor(Object.class, Class.class, Field.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            //static method
            REF_STATIC_TYPES.put(RefObject.class, RefObject.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefMethod.class, RefMethod.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefInt.class, RefInt.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefLong.class, RefLong.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefFloat.class, RefFloat.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefDouble.class, RefDouble.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefBoolean.class, RefBoolean.class.getConstructor(Class.class, Field.class));

            REF_STATIC_TYPES.put(RefStaticObject.class, RefStaticObject.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticMethod.class, RefStaticMethod.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticInt.class, RefStaticInt.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticLong.class, RefStaticLong.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticFloat.class, RefStaticFloat.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticDouble.class, RefStaticDouble.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticBoolean.class, RefStaticBoolean.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticConstructor.class, RefStaticConstructor.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefConstructor.class, RefConstructor.class.getConstructor(Class.class, Field.class));
            REF_STATIC_TYPES.put(RefStaticInterfaceConstructor.class, RefStaticInterfaceConstructor.class.getConstructor(Class.class, Field.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Class relocationStatic(Class mappingClass, Class<?> realClass) {
        Field[] fields = mappingClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    Constructor<?> constructor = REF_STATIC_TYPES.get(field.getType());
                    if (constructor != null) {
                        field.set(null, constructor.newInstance(realClass, field));
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return realClass;
    }

    static Class relocationMember(Class mappingClass, Object mappingObject, Class<?> realClass, Object that) {
        Field[] fields = mappingClass.getFields();
        for (Field field : fields) {
            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    Constructor<?> constructor = REF_TYPES.get(field.getType());
                    if (constructor != null) {
                        try {
                            field.set(mappingObject, constructor.newInstance(that, realClass, field));
                        } catch (RuntimeException e) {
                            //TODO use strategy ? Only print error.
                            e.printStackTrace();
                        }
                    } else {
                        if ("that".equals(field.getName())) {
                            field.set(mappingObject, that);
                        }
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return realClass;
    }

    static Object findRealObject(IRemoteObject remoteObject) {
        Field[] fields = remoteObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if ("that".equals(field.getName())) {
                        return field.get(remoteObject);
                    }
                    if (!IRemoteMember.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    return ((IRemoteMember) field.get(remoteObject)).getAttachObject();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        String msg = String.format("Can not found MemberXXX from IRemoteObject , Type[%s] . pls check.", remoteObject.getClass().getName());
        throw new IllegalArgumentException(msg);
    }

    static Class findRealType(Class<? extends IRemoteObject> remoteClass) {
        try {
            return (Class) remoteClass.getField("TYPE").get(null);
        } catch (Exception e) {
            throw Mirror.createExceptionBy("mirror class must be contains TYPE field (Type is CLASS . eg. public static Class TYPE = RefClass.load(String,String)) ");
        }
    }

    static UnsupportedOperationException createNotSupportException() {
        return createNotSupportException("not support call static method by RefXXX ， pls use MemberXXXX replace...");
    }

    static UnsupportedOperationException createNotSupportException(String message) {
        return new UnsupportedOperationException(message);
    }

}
