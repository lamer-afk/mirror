package mirror;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public interface IRemoteProxy extends InvocationHandler {

    Object missingMethod(Object proxy, Method method, Object[] args)
            throws Throwable;
}
