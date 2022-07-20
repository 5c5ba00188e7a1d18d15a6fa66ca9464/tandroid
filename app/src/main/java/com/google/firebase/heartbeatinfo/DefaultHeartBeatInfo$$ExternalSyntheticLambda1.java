package com.google.firebase.heartbeatinfo;

import android.content.Context;
import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultHeartBeatInfo$$ExternalSyntheticLambda1 implements Provider {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ DefaultHeartBeatInfo$$ExternalSyntheticLambda1(Context context) {
        this.f$0 = context;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        HeartBeatInfoStorage heartBeatInfoStorage;
        heartBeatInfoStorage = HeartBeatInfoStorage.getInstance(this.f$0);
        return heartBeatInfoStorage;
    }
}
