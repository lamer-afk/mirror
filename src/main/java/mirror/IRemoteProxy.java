package mirror;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by chaos on 2018/12/14 09:52
 * <p>
 * mail: 157688302@qq.com
 */
public interface IRemoteProxy extends InvocationHandler {

    Object missingMethod(Object proxy, Method method, Object[] args)
            throws Throwable;
}
