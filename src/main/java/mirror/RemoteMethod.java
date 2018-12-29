package mirror;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteMethod<T> extends RemoteAccessible {

    protected Object that;
    protected Method method;

    public RemoteMethod(Object that, Class<?> cls, Field field) throws NoSuchMethodException {
        this.that = that;
        initRemote(cls, field);
        if (field.isAnnotationPresent(MethodParams.class)) {
            Class<?>[] types = field.getAnnotation(MethodParams.class).value();
            for (int i = 0; i < types.length; i++) {
                Class<?> clazz = types[i];
                if (clazz.getClassLoader() == getClass().getClassLoader()) {
                    try {
                        Class.forName(clazz.getName());
                        Class<?> realClass = (Class<?>) clazz.getField("TYPE").get(null);
                        types[i] = realClass;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.method = deepFindMethod(cls, field.getName(), types, false);
        } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
            boolean arrayset = false;
            String[] typeNames = field.getAnnotation(MethodReflectParams.class).value();
            Class<?>[] types = new Class<?>[typeNames.length];
            Class<?>[] types2 = new Class<?>[typeNames.length];
            for (int i = 0; i < typeNames.length; i++) {
                Class<?> type = getProtoType(typeNames[i]);
                if (type == null) {
                    try {
                        type = Class.forName(typeNames[i]);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                types[i] = type;
                if ("java.util.HashSet".equals(typeNames[i])) {
                    arrayset = true;
                    Class<?> type2 = type;
                    try {
                        type2 = Class.forName("android.util.ArraySet");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (type2 != null) {
                        types2[i] = type2;
                    } else {
                        types2[i] = type;
                    }
                } else {
                    types2[i] = type;
                }
            }
            try {
                this.method = deepFindMethod(cls, field.getName(), types, false);
            } catch (Exception e) {
                e.printStackTrace();
                if (arrayset) {
                    this.method = cls.getDeclaredMethod(field.getName(), types2);
                }
            }
            this.method.setAccessible(true);
        } else {
            this.method = deepFindMethod(cls, field.getName(), null, true);
        }
        if (this.method == null) {
            throw new NoSuchMethodException(field.getName() + " in class [" + cls.getName() + "]");
        } else {
            this.method.setAccessible(true);
        }
    }

    Method deepFindMethod(Class cls, String methodName, Class<?>[] types, boolean allowDegrade) throws NoSuchMethodException {
        Class originClass = cls;
        while (cls != null) {
            try {
                return cls.getDeclaredMethod(methodName, types);
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }

        if (allowDegrade) {
            //降级再查找一遍，忽略参数影响
            cls = originClass;
            while (cls != null) {
                try {
                    Method[] methods = cls.getDeclaredMethods();
                    if (methods == null
                            || methodName.length() == 0) {
                        throw new NoSuchMethodException();
                    }
                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].getName().equals(methodName)) {
                            return methods[i];
                        }
                    }
                    throw new NoSuchMethodException();
                } catch (NoSuchMethodException e) {
                    cls = cls.getSuperclass();
                }
            }
        }

        String args = "";
        if (types == null
                || types.length == 0) {
            args = "Void.class";
        } else {
            for (int i = 0; i < types.length; i++) {
                Class type = types[i];
                if (type == null) {
                    args = TextUtils.isEmpty(args) ? "NULL" : args + ",NULL";
                } else {
                    args = TextUtils.isEmpty(args) ? type.getName() : args + "," + type.getName();
                }
            }
        }
        throw new NoSuchMethodException("class[" + originClass.getName() + "." + methodName + "(" + args + ")]");
    }

    public T call(Object... args) {
        try {
            return callWithException(args);
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            } else {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public T callWithException(Object... args) throws Throwable {
        try {
            boolean isRemoteCaller = remoteCaller();
            if (isRemoteCaller) {
                //处理参数，有的参数是伪类型
                if (args != null
                        && args.length > 0) {

                    for (int i = 0; i < args.length; i++) {
                        Object obj = args[i];
                        if (obj instanceof IRemoteObject) {
                            //伪类型，找到真正的类型
                            args[i] = realObject((IRemoteObject) obj);
                        }
                    }

                }
            }
            Object ret = this.method.invoke(that, args);
            if (isRemoteCaller) {
                ret = callRemote(ret);
            }
            return (T) ret;
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }

    static Object realObject(IRemoteObject remoteObject) {
        //step.1: remoteObject不包含真正的object，因为为了使用简单，object隐藏到了各个field中，所以先找到第一个有效的field.
        return RefCreater.findRealObject(remoteObject);
    }

    static Class<?> getProtoType(String typeName) {
        if (typeName.equals("int")) {
            return Integer.TYPE;
        }
        if (typeName.equals("long")) {
            return Long.TYPE;
        }
        if (typeName.equals("boolean")) {
            return Boolean.TYPE;
        }
        if (typeName.equals("byte")) {
            return Byte.TYPE;
        }
        if (typeName.equals("short")) {
            return Short.TYPE;
        }
        if (typeName.equals("char")) {
            return Character.TYPE;
        }
        if (typeName.equals("float")) {
            return Float.TYPE;
        }
        if (typeName.equals("double")) {
            return Double.TYPE;
        }
        if (typeName.equals("void")) {
            return Void.TYPE;
        }
        return null;
    }

    public Class<?>[] paramList() {
        return method.getParameterTypes();
    }
}
