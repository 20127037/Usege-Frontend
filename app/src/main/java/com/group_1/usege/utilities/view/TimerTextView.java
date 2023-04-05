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
            int progress = msg.arg1;
            String progressText = DurationFormatUtils.formatDuration(progress * 1000L, "MM:SS", true);
            setText(progressText);
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

    private class DelayThread extends Thread
    {
        private final AtomicBoolean running = new AtomicBoolean(true);
        private final Runnable doneCallback;
        private final BiConsumer<Integer, DelayThread> progressCallback;
        private final int interval;
        private final int time;
        private int currentTime = 0;

        public DelayThread( int time, Runnable doneCallback, BiConsumer<Integer, DelayThread> progressCallback)
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
            while (running.get() && currentTime < time)
            {
                try
                {
                    Thread.sleep(interval * 1000L);
                    currentTime++;
                    progressCallback.accept(currentTime, this);
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
        currentWaiter = new DelayThread(timeInSeconds, this::doneDelaying, (progress, waiter) -> {
            Message msg = handler.obtainMessage(1, progress);
            handler.sendMessage(msg);
        });
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
