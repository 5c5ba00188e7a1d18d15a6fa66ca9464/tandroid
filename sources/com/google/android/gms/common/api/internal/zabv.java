package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApi;

/* loaded from: classes.dex */
public final class zabv extends zaag {
    private final GoogleApi zaa;

    public zabv(GoogleApi googleApi) {
        super("Method is not supported by connectionless client. APIs supporting connectionless client must not call this method.");
        this.zaa = googleApi;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final BaseImplementation$ApiMethodImpl enqueue(BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        return this.zaa.doRead(baseImplementation$ApiMethodImpl);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final BaseImplementation$ApiMethodImpl execute(BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        return this.zaa.doWrite(baseImplementation$ApiMethodImpl);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Looper getLooper() {
        return this.zaa.getLooper();
    }
}
