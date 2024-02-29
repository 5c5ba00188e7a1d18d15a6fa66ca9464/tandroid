package org.telegram.messenger;

import j$.util.function.ToLongFunction;
import org.telegram.messenger.NotificationsController;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsController$$ExternalSyntheticLambda46 implements ToLongFunction {
    public static final /* synthetic */ NotificationsController$$ExternalSyntheticLambda46 INSTANCE = new NotificationsController$$ExternalSyntheticLambda46();

    private /* synthetic */ NotificationsController$$ExternalSyntheticLambda46() {
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        long j;
        j = ((NotificationsController.StoryNotification) obj).date;
        return j;
    }
}
