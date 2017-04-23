package com.zenglb.framework.rxhttp;

import org.reactivestreams.Subscription;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 统一线程处理
 * <p>
 * Created by zenglb on 2017/4/18.
 */
@Deprecated
public class RxSubscriberUtils {
    /**
     * 统一线程处理，所有的Http 的处理的模式都是一样的
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxNetThreadHelper() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull Subscription subscription) throws Exception {
                                //如果无网络连接，则直接取消了
//                                        if (!NetUtil.isConnected(context)) {
//                                            subscription.cancel();
//                                            MsgEvent msgEvent = new MsgEvent(SyncStateContract.Constants.NET_CODE_CONNECT, Constants.CONNECT_EXCEPTION);
//                                            EventBus.getDefault().post(msgEvent);
//                                        }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread()
                        );
            }
        };
    }


//    /**
//     * 带进度条的请求
//     */
//    public static <T> FlowableTransformer<T, T> io_main(final Context context, final ProgressDialog dialog) {
//        return new FlowableTransformer<T, T>() {
//            @Override public Publisher<T> apply(@NonNull Flowable<T> upstream) {
//                return upstream
//                        //为了让进度条保持一会儿做了个延时
//                        .delay(1, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .doOnSubscribe(new Consumer<Subscription>() {
//                            @Override
//                            public void accept(@NonNull final Subscription subscription) throws Exception {
//                                if (!NetUtil.isConnected(context)) {
//                                    subscription.cancel();
//                                    MsgEvent msgEvent = new MsgEvent(Constants.NET_CODE_CONNECT, Constants.CONNECT_EXCEPTION);
//                                    EventBus.getDefault().post(msgEvent);
//                                } else {
//                                    //启动进度显示，取消进度时会取消请求
//                                    if (dialog != null) {
//                                        dialog.setCanceledOnTouchOutside(false);
//                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                                            @Override public void onCancel(DialogInterface dialog) {
//                                                subscription.cancel();
//                                            }
//                                        });
//                                        dialog.show();
//                                    }
//                                }
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }


}
