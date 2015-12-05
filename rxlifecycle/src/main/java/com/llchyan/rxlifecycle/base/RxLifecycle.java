/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.llchyan.rxlifecycle.base;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewAttachEvent;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

public class RxLifecycle
{

    private RxLifecycle()
    {
        throw new AssertionError("No instances");
    }

    /**
     * 译 → 将给定 source 绑定到一个 Fragment 的生命周期。
     * <p/>
     * 译 → 当生命周期事件发生时, the source 将停止发出任何通知。
     * <p/>
     * Use with {@link Observable#compose(Observable.Transformer)}:  compose(组成)
     * {@code source.compose(RxLifecycle.bindUntilEvent(lifecycle, FragmentEvent.STOP)).subscribe()}
     *
     * @param lifecycle 译 → Fragment的生命周期序列
     * @param event     译 → 从 the source 应该得出结论通知的事件
     * @return a reusable{@link Observable.Transformer} that unsubscribes the source at the specified event
     * <p/>
     * 译 → 返回一个可以在指定的事件中取消订阅 the source、可重用的 Observable.Transformer
     */
    public static <T> Observable.Transformer<? super T, ? extends T> bindUntilFragmentEvent(
            final Observable<FragmentEvent> lifecycle, final FragmentEvent event)
    {
        return bindUntilEvent(lifecycle, event);
    }

    /**
     * 译 → 将给定 source 绑定到一个 Activity 的生命周期。
     * <p/>
     * 译 → 当生命周期事件发生时, the source 将停止发出任何通知。
     * <p/>
     * Use with {@link Observable#compose(Observable.Transformer)}:
     * {@code source.compose(RxLifecycle.bindUntilEvent(lifecycle, ActivityEvent.STOP)).subscribe()}
     *
     * @param lifecycle 译 → Activity 的生命周期序列
     * @param event     译 → 从 the source 应该得出结论通知的事件
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source at the specified event
     * <p/>
     * 译 → 返回一个可以在指定的事件中取消订阅 the source、可重用的 Observable.Transformer
     */
    public static <T> Observable.Transformer<? super T, ? extends T> bindUntilActivityEvent(
            final Observable<ActivityEvent> lifecycle, final ActivityEvent event)
    {
        return bindUntilEvent(lifecycle, event);
    }

    private static <T, R> Observable.Transformer<? super T, ? extends T> bindUntilEvent(final Observable<R> lifecycle,
                                                                                        final R event)
    {
        if (lifecycle == null)
        {
            throw new IllegalArgumentException("Lifecycle must be given");
        } else if (event == null)
        {
            throw new IllegalArgumentException("Event must be given");
        }

        return new Observable.Transformer<T, T>()
        {
            @Override
            public Observable<T> call(Observable<T> source)
            {
                return source.takeUntil(
                        lifecycle.takeFirst(new Func1<R, Boolean>()
                        {
                            @Override
                            public Boolean call(R lifecycleEvent)
                            {
                                return lifecycleEvent == event;
                            }
                        })
                );
            }
        };
    }

    /**
     * 译 → 将给定 source 绑定到一个 Activity 的生命周期。
     * <p/>
     * Use with {@link Observable#compose(Observable.Transformer)}:
     * {@code source.compose(RxLifecycle.bindActivity(lifecycle)).subscribe()}
     * <p/>
     * <p/>
     * 译 → 当 the source 应该停止发出 items 时，由这个助手自动决定(基于生命周期序列本身)。在生命周期的序列中创建阶段(CREATE、START)的 case 中，
     * 它将选择等效销毁阶段(DESTROY、STOP等)。如果使用在销毁阶段,将停止下一个事件的通知; 例如,如果在周期 PAUSE 使用,它将周期 STOP 退订。
     * <p/>
     * 译 → 由于 Activity 和 Fragment 生命周期之间的差异,这个方法应该只用于一个 Activity 的生命周期。
     *
     * @param lifecycle 译 → Activity 的生命周期序列
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source during the Activity lifecycle
     */
    public static <T> Observable.Transformer<? super T, ? extends T> bindActivity(Observable<ActivityEvent> lifecycle)
    {
        return bind(lifecycle, ACTIVITY_LIFECYCLE);
    }

    /**
     * 译 → 将给定 source 绑定到一个 Fragment 的生命周期。
     * Use with {@link Observable#compose(Observable.Transformer)}:
     * {@code source.compose(RxLifecycle.bindFragment(lifecycle)).subscribe()}
     * <p/>
     * 译 → 当 the source 应该停止发出 items 时，由这个助手自动决定(基于生命周期序列本身)。在生命周期的序列中创建阶段(CREATE、START)的 case 中，
     * 它将选择等效销毁阶段(DESTROY、STOP等)。如果使用在销毁阶段,将停止下一个事件的通知; 例如,如果在周期 PAUSE 使用,它将周期 STOP 退订。
     * <p/>
     * 译 → 由于 Activity 和 Fragment 生命周期之间的差异,这个方法应该只用于一个 Fragment 的生命周期。
     *
     * @param lifecycle the lifecycle sequence of a Fragment
     *                  译 → Fragment 的生命周期序列
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source during the Fragment lifecycle
     */
    public static <T> Observable.Transformer<? super T, ? extends T> bindFragment(Observable<FragmentEvent> lifecycle)
    {
        return bind(lifecycle, FRAGMENT_LIFECYCLE);
    }

    /**
     * 译 → 将给定 source 绑定到一个 View 的生命周期。
     * <p/>
     * Use with {@link Observable#compose(Observable.Transformer)}:
     * {@code source.compose(RxLifecycle.bindView(lifecycle)).subscribe()}
     * <p/>
     * 译 → 当 the source 应该停止发射 items 时，由这个助手自动决定(基于 View 本身生命周期序列)。
     * 对于 views,这实际上意味着发生分离事件那一刻，将取消订阅序列。
     * <p/>
     * 译 → 注意,这将在第一个{@link ViewAttachEvent.Kind#DETACH}事件接收后取消订阅,以及如果 the view  re-attached 后也不会恢复订阅。
     *
     * @param view the view to bind the source sequence to
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source during the View lifecycle
     */
    public static <T> Observable.Transformer<? super T, ? extends T> bindView(final View view)
    {
        if (view == null)
        {
            throw new IllegalArgumentException("View must be given");
        }

        return bindView(RxView.detaches(view));
    }

    /**
     * Binds the given source a View lifecycle.
     * 译 → 将给定 source 绑定到一个 View 的生命周期。
     * <p/>
     * Use with {@link Observable#compose(Observable.Transformer)}:
     * {@code source.compose(RxLifecycle.bindView(lifecycle)).subscribe()}
     * <p/>
     * This helper automatically determines (based on the lifecycle sequence itself) when the source
     * should stop emitting items. For views, this effectively means watching for a detach event and
     * unsubscribing the sequence when one occurs. Note that this assumes <em>any</em> event
     * emitted by the given lifecycle indicates a detach event.
     * 译 → 当 the source 应该停止发射 items 时，由这个助手自动决定(基于 View 本身生命周期序列)。
     * 注意,这个假设 <em>any</em> event 发生，通过给定的生命周期表明分离事件。
     *
     * @param lifecycle the lifecycle sequence of a View
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source during the View lifecycle
     */
    public static <T, E> Observable.Transformer<? super T, ? extends T> bindView(final Observable<? extends E> lifecycle)
    {
        if (lifecycle == null)
        {
            throw new IllegalArgumentException("Lifecycle must be given");
        }

        return new Observable.Transformer<T, T>()
        {
            @Override
            public Observable<T> call(Observable<T> source)
            {
                return source.takeUntil(lifecycle);
            }
        };
    }

    private static <T, R> Observable.Transformer<? super T, ? extends T> bind(Observable<R> lifecycle,
                                                                              final Func1<R, R> correspondingEvents)
    {
        if (lifecycle == null)
        {
            throw new IllegalArgumentException("Lifecycle must be given");
        }

        //确保我们是真正地比较 a single stream to itself
        final Observable<R> sharedLifecycle = lifecycle.share();

        // Keep emitting from source until the corresponding event occurs in the lifecycle
        //在生命周期里发生相应的事件之前，从 source  保持发射状态。
        return new Observable.Transformer<T, T>()
        {
            @Override
            public Observable<T> call(Observable<T> source)
            {
                return source.takeUntil(
                        Observable.combineLatest(
                                sharedLifecycle.take(1).map(correspondingEvents),
                                sharedLifecycle.skip(1),
                                new Func2<R, R, Boolean>()
                                {
                                    @Override
                                    public Boolean call(R bindUntilEvent, R lifecycleEvent)
                                    {
                                        return lifecycleEvent == bindUntilEvent;
                                    }
                                })
                                .onErrorReturn(RESUME_FUNCTION)
                                .takeFirst(SHOULD_COMPLETE)
                );
            }
        };
    }

    private static final Func1<Throwable, Boolean> RESUME_FUNCTION = new Func1<Throwable, Boolean>()
    {
        @Override
        public Boolean call(Throwable throwable)
        {
            if (throwable instanceof OutsideLifecycleException)
            {
                return true;
            }

            Exceptions.propagate(throwable);
            return false;
        }
    };

    private static final Func1<Boolean, Boolean> SHOULD_COMPLETE = new Func1<Boolean, Boolean>()
    {
        @Override
        public Boolean call(Boolean shouldComplete)
        {
            return shouldComplete;
        }
    };

    //从 Activities 中找出下一个 相应的用来取消订阅的生命周期事件
    private static final Func1<ActivityEvent, ActivityEvent> ACTIVITY_LIFECYCLE =
            new Func1<ActivityEvent, ActivityEvent>()
            {
                @Override
                public ActivityEvent call(ActivityEvent lastEvent)
                {
                    switch (lastEvent)
                    {
                        case CREATE:
                            return ActivityEvent.DESTROY;
                        case START:
                            return ActivityEvent.STOP;
                        case RESUME:
                            return ActivityEvent.PAUSE;
                        case PAUSE:
                            return ActivityEvent.STOP;
                        case STOP:
                            return ActivityEvent.DESTROY;
                        case DESTROY:
                            throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };

    //从 Fragments 中找出下一个 相应的用来取消订阅的生命周期事件
    private static final Func1<FragmentEvent, FragmentEvent> FRAGMENT_LIFECYCLE =
            new Func1<FragmentEvent, FragmentEvent>()
            {
                @Override
                public FragmentEvent call(FragmentEvent lastEvent)
                {
                    switch (lastEvent)
                    {
                        case ATTACH:
                            return FragmentEvent.DETACH;
                        case CREATE:
                            return FragmentEvent.DESTROY;
                        case CREATE_VIEW:
                            return FragmentEvent.DESTROY_VIEW;
                        case START:
                            return FragmentEvent.STOP;
                        case RESUME:
                            return FragmentEvent.PAUSE;
                        case PAUSE:
                            return FragmentEvent.STOP;
                        case STOP:
                            return FragmentEvent.DESTROY_VIEW;
                        case DESTROY_VIEW:
                            return FragmentEvent.DESTROY;
                        case DESTROY:
                            return FragmentEvent.DETACH;
                        case DETACH:
                            throw new OutsideLifecycleException("Cannot bind to Fragment lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };

    private static class OutsideLifecycleException extends IllegalStateException
    {
        public OutsideLifecycleException(String detailMessage)
        {
            super(detailMessage);
        }
    }

}
