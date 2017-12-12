package com.vise.xsnow.manager;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;

import com.vise.log.ViseLog;
import com.vise.xsnow.ui.BaseActivity;

import java.util.Iterator;
import java.util.Stack;

/**
 * @Description: Activity堆栈管理
 */
public class AppManager {
    public static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }


    public boolean isActivityExist(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivityWithNoAnim(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity.overridePendingTransition(Animation.INFINITE, Animation.INFINITE);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }

//        Iterator<Activity> iterator = activityStack.iterator();
//        while (iterator.hasNext()){
//            Activity activity = iterator.next();
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
    }

    public void finishActivityWithNoAnim(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivityWithNoAnim(activity);
            }
        }
    }

    public void finishAllActivity() {
        if (activityStack == null) return;
        for (int i = 0, size = activityStack.size(); i<size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void finishExceptActivity(Class<?> cls) {
        while (!activityStack.isEmpty()) {
            Activity act = activityStack.pop();
            if (act != null && act.getClass() != cls) {
                act.finish();
            }
        }
    }

    public void finishExceptActivity(BaseActivity exceptAct) {
        while (!activityStack.isEmpty()) {
            Activity act = activityStack.pop();
            if (act != null && act != exceptAct) {
                act.finish();
            }
        }
    }

    boolean mAppExit = false;

    public boolean appHasExit(){
        return mAppExit;
    }

    public void appExit() {
        try {
            if (activityStack.size() == 0){
                android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                finishAllActivity();
                mAppExit = true;
            }
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e(e);
        }
    }

}
