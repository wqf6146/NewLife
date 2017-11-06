package com.vise.xsnow.event;

/**
 * @Description: 事件总线接口
 */
public interface IBus {
    void register(Object object);

    void unregister(Object object);

    void post(IEvent event);

    void postSticky(IEvent event);
}
