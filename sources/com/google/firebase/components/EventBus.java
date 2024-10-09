package com.google.firebase.components;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class EventBus implements Subscriber, Publisher {
    private final Executor defaultExecutor;
    private final Map handlerMap = new HashMap();
    private Queue pendingEvents = new ArrayDeque();

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventBus(Executor executor) {
        this.defaultExecutor = executor;
    }

    private synchronized Set getHandlers(Event event) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$publish$0(Map.Entry entry, Event event) {
        ((EventHandler) entry.getKey()).handle(event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enablePublishingAndFlushPending() {
        Queue queue;
        synchronized (this) {
            try {
                queue = this.pendingEvents;
                if (queue != null) {
                    this.pendingEvents = null;
                } else {
                    queue = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (queue != null) {
            Iterator it = queue.iterator();
            while (it.hasNext()) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                publish(null);
            }
        }
    }

    public void publish(final Event event) {
        Preconditions.checkNotNull(event);
        synchronized (this) {
            try {
                Queue queue = this.pendingEvents;
                if (queue != null) {
                    queue.add(event);
                    return;
                }
                for (final Map.Entry entry : getHandlers(event)) {
                    ((Executor) entry.getValue()).execute(new Runnable(entry, event) { // from class: com.google.firebase.components.EventBus$$ExternalSyntheticLambda0
                        public final /* synthetic */ Map.Entry f$0;

                        @Override // java.lang.Runnable
                        public final void run() {
                            EventBus.lambda$publish$0(this.f$0, null);
                        }
                    });
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.google.firebase.events.Subscriber
    public void subscribe(Class cls, EventHandler eventHandler) {
        subscribe(cls, this.defaultExecutor, eventHandler);
    }

    public synchronized void subscribe(Class cls, Executor executor, EventHandler eventHandler) {
        try {
            Preconditions.checkNotNull(cls);
            Preconditions.checkNotNull(eventHandler);
            Preconditions.checkNotNull(executor);
            if (!this.handlerMap.containsKey(cls)) {
                this.handlerMap.put(cls, new ConcurrentHashMap());
            }
            ((ConcurrentHashMap) this.handlerMap.get(cls)).put(eventHandler, executor);
        } catch (Throwable th) {
            throw th;
        }
    }
}
