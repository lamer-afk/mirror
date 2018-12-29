package mirror;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
class RemoteInterfaceConstructor {

    protected Class mRealClass;

    public RemoteInterfaceConstructor(Class cls, Field field) {
        mRealClass = cls;
        checkForThrows();
    }

    private void checkForThrows() {
        if (!mRealClass.isInterface()) {
            throw new UnsupportedOperationException("Only support interface... class " + mRealClass.getName() + " is NOT interface.");
        }
    }

    public Object newInstance(IRemoteProxy proxy) {
        return java.lang.reflect.Proxy.newProxyInstance(mRealClass.getClassLoader(), new Class[]{mRealClass}, new Proxy(proxy));
    }

    private static class Proxy implements InvocationHandler {

        private IRemoteProxy userProxy;
        private List<MirrorMethod> methods;

        public Proxy(IRemoteProxy proxy) {
            this.userProxy = proxy;
            methods = new ArrayList<>();

            Method[] originMethods = proxy.getClass().getMethods();
            if (originMethods != null
                    && originMethods.length > 0) {
                for (Method method : originMethods) {
                    methods.add(new MirrorMethod(MirrorMethod.TYPE_LOCAL_METHOD, method));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            int index = methods.indexOf(new MirrorMethod(MirrorMethod.TYPE_REMOTE_METHOD, method));
            if (index >= 0) {
                return methods.get(index).invoke(proxy, args);
            } else {
                return userProxy.missingMethod(proxy, method, args);
            }
        }
    }
}
