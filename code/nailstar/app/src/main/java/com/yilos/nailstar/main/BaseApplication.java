package com.yilos.nailstar.main;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;

/**
 * Created by yilos on 2015-10-20.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 播放器初始化，要在app启动前进行初始化，才能解压出相应的解码器
        VDApplication.getInstance().initPlayer(this);
        VDResolutionManager.getInstance(this).init(
                VDResolutionManager.RESOLUTION_SOLUTION_NONE);

//        CrashHandler.getInstance().init(this);
    }

    public void exit() {
        System.exit(0);
    }

//    class CrashHandler implements Thread.UncaughtExceptionHandler {
//        // 系统默认的UncaughtException处理类
//        private Thread.UncaughtExceptionHandler mDefaultHandler;
//        // CrashHandler实例
//        private CrashHandler INSTANCE;
//        // 程序的Context对象
//        private Context mContext;
//
//        /**
//         * 保证只有一个CrashHandler实例
//         */
//        private CrashHandler() {
//        }
//
//        /**
//         * 获取CrashHandler实例 ,单例模式
//         */
//        public CrashHandler getInstance() {
//            return null == INSTANCE ? new CrashHandler() : INSTANCE;
//        }
//
//        /**
//         * 初始化
//         *
//         * @param context
//         */
//        public void init(Context context) {
//            mContext = context;
//            // 获取系统默认的UncaughtException处理器
//            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//            // 设置该CrashHandler为程序的默认处理器
//            Thread.setDefaultUncaughtExceptionHandler(this);
//        }
//
//        /**
//         * 当UncaughtException发生时会转入该函数来处理
//         */
//        @Override
//        public void uncaughtException(Thread thread, Throwable ex) {
//            if (!handleException(ex) && mDefaultHandler != null) {
//                // 如果用户没有处理则让系统默认的异常处理器来处理
//                mDefaultHandler.uncaughtException(thread, ex);
//            } else {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                }
//                // 退出程序
//                BaseApplication.getInstance().exit();
//            }
//        }
//
//        /**
//         * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
//         *
//         * @param ex
//         * @return true:如果处理了该异常信息;否则返回false.
//         */
//        private boolean handleException(Throwable ex) {
//            if (ex == null) {
//                return false;
//            }
//            // 使用Toast来显示异常信息
//            new Thread() {
//                @Override
//                public void run() {
//                    Looper.prepare();
//                    Toast.makeText(mContext,
//                            mContext.getString(R.string.abnormal_exit_program),
//                            Toast.LENGTH_LONG).show();
//                    Looper.loop();
//                }
//            }.start();
//            // 记录日志
//            return true;
//        }
//    }
}
