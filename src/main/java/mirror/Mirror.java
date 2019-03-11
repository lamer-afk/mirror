package mirror;

import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Created by chaos on 2018/11/14 19:24
 * <p>
 * mail: 157688302@qq.com
 */
public class Mirror {

    private static Mirror INSTANCE;

    private Mirror() {

    }

    public static Mirror get() {
        if (INSTANCE == null) {
            synchronized (Mirror.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Mirror();
                }
            }
        }
        return INSTANCE;
    }

    private HashMap<String, Transfer> mMappings = new HashMap<>();

    public void setClassLoader(String key, ClassLoader classLoader) {
        mMappings.put(key, className -> classLoader.loadClass(className));
    }

    public void setTransfer(String key, Transfer transfer) {
        mMappings.put(key, transfer);
    }

    protected Transfer getTransfer(String key) {
        return mMappings.get(key);
    }

    public interface Transfer {
        Class loadClass(String className) throws ClassNotFoundException;
    }


    static RuntimeException createExceptionBy(String message) {
        return new IllegalArgumentException(message);
    }

}
