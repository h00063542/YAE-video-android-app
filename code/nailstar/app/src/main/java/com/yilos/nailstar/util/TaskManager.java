package com.yilos.nailstar.util;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdan on 15/10/23.
 */
public class TaskManager {

    private Object lastResult = null;

    private List<Task> taskList = new ArrayList<Task>(8);

    public TaskManager next(Task task){
        taskList.add(task);

        return this;
    }

    public void start() {
        executeTask(0);
    }

    public void executeTask(final int index){
        if(index >= taskList.size()) {
            return;
        }

        Task<Object> task = taskList.get(index);
        if(task instanceof BackgroundTask) {
            executeBackgroundTask(task, new UITask() {
                @Override
                public Object doWork(Object data) {
                    executeTask(index + 1);
                    return null;
                }
            });
        }
        else {
            lastResult = task.doWork(lastResult);
        }
    }

    public void executeBackgroundTask(final Task backgroundTask, final Task uiTask){
        final Handler handler = new Handler(){
            public void handleMessage(Message msg){
                uiTask.doWork(msg.obj);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                lastResult = backgroundTask.doWork(lastResult);

                Message msg = handler.obtainMessage(1, lastResult);
                msg.sendToTarget();
            }
        }).start();
    }

    public interface Task<T> {
        Object doWork(T data);
    }

    public interface BackgroundTask<T> extends Task<T>{
    }

    public interface UITask<T> extends Task<T>{
    }
}
