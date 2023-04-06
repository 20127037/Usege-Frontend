package com.group_1.usege.utilities.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class TimerTextView extends androidx.appcompat.widget.AppCompatTextView {

    private final Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            setText((String)msg.obj);
        }
    };


    public TimerTextView(@NonNull Context context) {
        super(context);
    }

    public TimerTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static class DelayThread extends Thread
    {
        private final AtomicBoolean running = new AtomicBoolean(true);
        private final Runnable doneCallback;
        private final BiConsumer<Integer, DelayThread> progressCallback;
        private final int interval;
        private int time;

        public DelayThread(int time, Runnable doneCallback, BiConsumer<Integer, DelayThread> progressCallback)
        {
            this(time, 1, doneCallback, progressCallback);
        }

        public DelayThread(int time, int interval, Runnable doneCallback, BiConsumer<Integer, DelayThread> progressCallback)
        {
            this.doneCallback = doneCallback;
            this.progressCallback = progressCallback;
            this.time = time;
            this.interval = interval;
        }

        public void makeStop() {
            running.set(false);
        }

        public void run() {
            while (running.get() && time > 0)
            {
                try
                {
                    Thread.sleep(interval * 1000L);
                    time--;
                    progressCallback.accept(time, this);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.out.println(
                            "Thread was interrupted, Failed to complete operation");
                }
            }
            doneCallback.run();
        }
    }
    private DelayThread currentWaiter;
    private Runnable currentDoneCallback;

    public void startDelaying(int timeInSeconds, Runnable doneCallback)
    {
        if (currentWaiter != null)
            return;
        currentDoneCallback = doneCallback;
        currentWaiter = new DelayThread(timeInSeconds, () -> handler.post(this::doneDelaying), (progress, waiter) -> {
            String progressText = DurationFormatUtils.formatDuration(progress * 1000L, "m:s", true);
            Message msg = handler.obtainMessage(1, progressText);
            handler.sendMessage(msg);
        });
        currentWaiter.start();
    }
    public void forceStop()
    {
        if (currentWaiter == null)
            return;
        currentWaiter.makeStop();
    }
    private void doneDelaying()
    {
        if (currentDoneCallback != null)
            currentDoneCallback.run();
        currentDoneCallback = null;
        currentWaiter = null;
    }
}
