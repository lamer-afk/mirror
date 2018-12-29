package mirror;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by lamer on 2018/12/30 01:11
 * <p>
 * mail: 157688302@qq.com
 */
public final class RefClass {

    public static Class<?> load(Class<?> mappingClass, String className) {
        return load(mappingClass, className, null);
    }

    public static Class<?> load(Class<?> mappingClass, String className, String transferKey) {
        try {
            Class targetClass;
            if (TextUtils.isEmpty(transferKey)
                    || Mirror.get().getTransfer(transferKey) == null) {
                targetClass = Class.forName(className);
            } else {
                try {
                    targetClass = Mirror.get().getTransfer(transferKey).loadClass(className);
                } catch (ClassNotFoundException e) {
                    //use thread class loader
                    targetClass = Class.forName(className);
                }
            }
            return load(mappingClass, targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static Class load(Class mappingClass, Class<?> realClass) {
        return RefCreater.relocationStatic(mappingClass, realClass);
    }

}