package mirror;

import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by chaos on 2018/12/14 10:28
 * <p>
 * mail: 157688302@qq.com
 * <p>
 * 用来辨识两个method是否相同，条件如下：
 * 1.方法名字相同
 * 2.方法参数的类型相同，支持降级相同，比如参数A是参数B的子类，或者参数A是参数B的虚类型（Remote类型）
 * 3.返回值相同或者为其子类，或者是虚类型
 * <p>
 * Note：
 * 1. method分为远端方法（需要反射的方法）和本地方法（用于映射的方法）
 * 2. 对于本地方法，参数类型可能是虚类型（Remote类型），也可能是子类型。(子类型暂不支持)
 */
class MirrorMethod {

    static final int TYPE_LOCAL_METHOD = 1;
    static final int TYPE_REMOTE_METHOD = 2;

    private int methodType = -1;
    private Method method;
    private String methodName;
    private Class returnType;
    private Class[] parameterTypes;
    private Class[] originParameterTypes;
    private boolean isRemoteReturnType = false;
    private boolean[] isRemoteParameterTypes;

    public MirrorMethod(int type, Method method) {
        this.methodType = type;
        this.method = method;
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkForThrows(int a) {
        if (methodType != TYPE_LOCAL_METHOD
                && methodType != TYPE_REMOTE_METHOD) {
            throw new UnsupportedOperationException("不支持的method类型 " + methodType);
        }
    }

    private void init() throws Exception {
        this.methodName = method.getName();
        this.returnType = getRealType(method.getReturnType(), method.getAnnotation(Type.class));
        this.originParameterTypes = method.getParameterTypes();
        this.parameterTypes = getParameterTypes();
        this.isRemoteReturnType = IRemoteObject.class.isAssignableFrom(method.getReturnType());
        Class[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null
                && parameterTypes.length > 0) {
            isRemoteParameterTypes = new boolean[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                isRemoteParameterTypes[i] = IRemoteObject.class.isAssignableFrom(parameterTypes[i]);
            }
        }
    }

    private Class[] getParameterTypes() throws ClassNotFoundException {
        Class[] classTypes = method.getParameterTypes();
        if (classTypes == null
                || classTypes.length == 0) {
            return null;
        }
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < classTypes.length; i++) {
            Annotation[] annotationArrays = annotations[i];
            Type type = getTypeAnnotation(annotationArrays);
            classTypes[i] = getRealType(classTypes[i], type);
        }

        return classTypes;
    }

    private Type getTypeAnnotation(Annotation[] annotations) {
        if (annotations == null
                || annotations.length == 0) {
            return null;
        }
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i] instanceof Type) {
                return (Type) annotations[i];
            }
        }
        return null;
    }


    private Class getRealType(Class fakeClass, Type type) throws ClassNotFoundException {
        if (methodType == TYPE_REMOTE_METHOD) {
            //远端方法不做处理
            return fakeClass;
        } else if (methodType == TYPE_LOCAL_METHOD) {
            if (type != null) {
                //优先处理注解
                String realClassName = type.value();
                String transferKey = type.getTransferKey();
                if (TextUtils.isEmpty(transferKey)) {
                    return Class.forName(realClassName);
                } else {
                    return Mirror.get().getTransfer(transferKey).loadClass(realClassName);
                }
            } else {
                //查看是否为虚类型
                if (IRemoteObject.class.isAssignableFrom(fakeClass)) {
                    //虚类型转真实类型类型
                    return RefCreater.findRealType(fakeClass);
                } else {
                    return fakeClass;
                }
            }
        } else {
            throw new UnsupportedOperationException("不支持的method类型 " + methodType);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MirrorMethod)) return false;
        MirrorMethod that = (MirrorMethod) o;
        return objectEquals(methodName, that.methodName) &&
                objectEquals(returnType, that.returnType) &&
                Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode() {
        int result = objectHash(methodName, returnType);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    boolean objectEquals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    int objectHash(Object... values) {
        return Arrays.hashCode(values);
    }

    public Object invoke(Object proxy, Object[] args) throws InvocationTargetException, IllegalAccessException {
        //先进行参数转换
        if (isRemoteParameterTypes != null
                && isRemoteParameterTypes.length > 0) {
            for (int i = 0; i < isRemoteParameterTypes.length; i++) {
                if (isRemoteParameterTypes[i]) {
                    //需要转换
                    if (args[i] != null) {
                        args[i] = RemoteCaller.convertToRemote(args[i], this.originParameterTypes[i]);
                    }
                }
            }
        }
        Object ret = method.invoke(proxy, args);
        //检查返回类型是不是虚类型
        if (isRemoteReturnType) {
            ret = RemoteCaller.getAttachObject((IRemoteObject) ret);
        }
        return ret;
    }
}
