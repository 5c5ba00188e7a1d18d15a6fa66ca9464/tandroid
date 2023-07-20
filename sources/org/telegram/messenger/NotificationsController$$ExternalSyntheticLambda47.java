package org.telegram.messenger;

import j$.util.function.ToLongFunction;
import org.telegram.messenger.NotificationsController;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationsController$$ExternalSyntheticLambda47 implements ToLongFunction {
    public static final /* synthetic */ NotificationsController$$ExternalSyntheticLambda47 INSTANCE = new NotificationsController$$ExternalSyntheticLambda47();

    private /* synthetic */ NotificationsController$$ExternalSyntheticLambda47() {
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        long j;
        j = ((NotificationsController.StoryNotification) obj).date;
        return j;
    }
}
