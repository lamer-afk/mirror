# Mirror
反射工具，用来更方便的访问系统/第三方。项目基于asLody的 [VirtualApp]: https://github.com/asLody/VirtualApp 中的部分代码进行扩展。


使用示例 (以ActivityThread为例)

------

第一步，创建镜像Class。（RefClass.load方法，第一个参数为当前镜像Class，第二个参数为目标Class）

```java
package mirror;

import android.app.Activity;
import android.app.Application;
import android.os.IBinder;
import android.os.Looper;

//一定要实现IRemoteObject这个标记接口
public class ActivityThread implements IRemoteObject{

    //命名必须为TYPE，返回类型是反射的类的Class对象
    public static final Class TYPE = RefClass.load(ActivityThread.class,"android.app.ActivityThread");

    //目标Class存在静态变量TAG，类型String.class
    public static RefStaticObject<String> TAG;

    //目标Class存在静态变量USER_LEAVING,DONT_REPORT，类型：int.class
    public static RefStaticInt USER_LEAVING;
    public static RefStaticInt DONT_REPORT;

    //目标Class存在成员变量Application，命名为mInitialApplication
    //成员变量请不要使用static修饰
    public MemberObject<Application> mInitialApplication;

    //目标Class存在静态变量ActivityThread（单例），命名为：mAppThread
    public static RefStaticObject<ActivityThread> mAppThread;

    //目标Class存在成员方法getLooper，参数为空，返回类型为Looper.class
    public MemberMethod<Looper> getLooper;

    //目标Class存在成员方法getActivity，参数为IBinder.class，返回类型为Activity.class
    @MethodParams(IBinder.class)
    public MemberMethod<Activity> getActivity;

```



第二步，使用自己创建的镜像Class。

```java
 //直接使用即可，不需要任何赋值操作
 ActivityThread.TAG.set("set by mirror");
 //反射获取report的值
 int report = ActivityThread.DONT_REPORT.get();
        
 //对于成员方法（非静态）
 //先获取对象
 ActivityThread activityThread = ActivityThread.mAppThread.get();
 //调用成员方法
 Looper looper = activityThread.getLooper.call();
 //获取-设置成员变量
 activityThread.mInitialApplication.set(null);
 activityThread.mInitialApplication.get();
```

