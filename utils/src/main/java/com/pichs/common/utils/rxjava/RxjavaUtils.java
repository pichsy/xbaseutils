package com.pichs.common.utils.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: 吴波
 * @CreateDate: 2020/12/31 13:10
 * @UpdateUser: 吴波
 * @UpdateDate: 2020/12/31 13:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RxjavaUtils {

    /**
     * 子线程中执行，主线程中回调
     */
    public static <T, R> void io2main(T t, RxAction<T, R> ioAction, final RxResult<R> result) {
        Observable
                .just(t)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<T, ObservableSource<R>>() {
                    @Override
                    public ObservableSource<R> apply(@NonNull T t) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<R>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<R> emitter) throws Exception {
                                emitter.onNext(ioAction.run(t));
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<R>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull R r) {
                        result.onResult(r);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 子线程中执行，主线程中回调
     */
    public static <R> void io2main(RxAction<Integer, R> ioAction, final RxResult<R> result) {
        Observable
                .just(1)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Integer, ObservableSource<R>>) t ->
                        Observable.create((ObservableOnSubscribe<R>) emitter ->
                                emitter.onNext(ioAction.run(t))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<R>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull R r) {
                        result.onResult(r);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 主线程中执行，子线程中回调
     */
    public static <R> void main2io(RxAction<Integer, R> ioAction, final RxResult<R> result) {
        Observable
                .just(1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Integer, ObservableSource<R>>) t ->
                        Observable.create((ObservableOnSubscribe<R>) emitter ->
                                emitter.onNext(ioAction.run(t))))
                .observeOn(Schedulers.io())
                .subscribe(new Observer<R>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull R r) {
                        result.onResult(r);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 子线程计时
     * 主线程回调
     *
     * @param times  倒计时的总秒数
     * @param result 结果
     */
    public static Disposable cutDownTimer(long times, RxResult<Long> result) {
        return Observable.interval(1, TimeUnit.SECONDS)
                .take(times + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> result.onResult(times - time));
    }

    /**
     * 正计时，需要制定计时多少s
     * 主线程回调
     *
     * @param times  计时多少秒
     * @param result 回调
     */
    public static Disposable timer(long times, RxResult<Long> result) {
        return Observable.interval(1, TimeUnit.SECONDS)
                .take(times + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result::onResult);
    }

    /**
     * 延迟做一些操作
     *
     * @param timeMills 毫秒级别延迟
     * @param result    延迟结束，回调
     */
    public static void delay(long timeMills, RxResult<Long> result) {
        Observable.timer(timeMills, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long time) {
                        result.onResult(time);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
