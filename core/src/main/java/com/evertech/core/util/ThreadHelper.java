package com.evertech.core.util;


import android.annotation.SuppressLint;
import android.os.Handler;

import com.evertech.core.definition.JAction;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.reactivestreams.Publisher;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThreadHelper {
    public static final Handler HANDLER = new Handler();

    public static final FlowableTransformer APPLY_SCHEDULERS = new FlowableTransformer() {
        @Override
        public Publisher apply(Flowable upstream) {
            return upstream.subscribeOn(
                    Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
    };

    private static final Consumer<Throwable> sOnError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
        }
    };

    public static final int MIN_DELAY_MILLIS = 10;

    /**
     * 使用场景：仅仅需要异步处理，而没有与主线程的交互.
     */
    @SuppressLint("CheckResult")
    public static void run(final JAction action) {
        if (action == null) return;
        Flowable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                action.run();
                return 1;
            }
        }).compose(APPLY_SCHEDULERS).subscribe(new Consumer() {
            @Override
            public void accept(Object i) throws Exception {
            }
        }, sOnError);
    }

    /**
     * 使用场景：仅仅需要异步处理，而没有与主线程的交互，但是有错误回调.
     */
    @SuppressLint("CheckResult")
    public static void run(final JAction action, Consumer<Throwable> error) {
        if (action == null) return;
        Flowable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                action.run();
                return 1;
            }
        }).compose(APPLY_SCHEDULERS).subscribe(new Consumer() {
            @Override
            public void accept(Object i) throws Exception {
            }
        }, error);
    }

    /**
     * 使用场景：需要异步处理，处理完毕后给主线程回调，但是没有返回值.
     */
    @SuppressLint("CheckResult")
    public static void run(final JAction runAction, final JAction resultAction) {
        if (runAction == null || resultAction == null) return;
        Flowable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                runAction.run();
                return 1;
            }
        }).compose(APPLY_SCHEDULERS).subscribe(new Consumer() {
            @Override
            public void accept(Object i) throws Exception {
                resultAction.run();
            }
        }, sOnError);
    }

    /**
     * 使用场景：仅仅需要run在主线程上.
     */
    public static void runOnMainThread(final JAction action) {
        if (action == null) return;
        Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) throws Exception {
                        action.run();
                    }
                }, sOnError);
    }

    /**
     * 使用场景：delay操作.
     */
    public static void runDelayed(final JAction action, long delayMillis) {
        if (action == null || delayMillis < MIN_DELAY_MILLIS) return;
        Completable.timer(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        action.run();
                    }
                }, sOnError);
    }

    /**
     * 使用场景：delay操作，但需要绑定对应生命周期.
     *
     * @param transformer
     */
    public static void runDelayed(final JAction action, long delayMillis, LifecycleTransformer transformer) {
        if (action == null || delayMillis < MIN_DELAY_MILLIS) return;
        Completable.timer(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        action.run();
                    }
                }, sOnError);
    }

    /**
     * Handler的{@link Handler#post(Runnable)}，但是共享同一个Handler.
     */
    public static void post(Runnable r) {
        if (r == null) return;
        HANDLER.post(r);
    }

}
