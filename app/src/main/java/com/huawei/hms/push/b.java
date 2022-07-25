package com.huawei.hms.push;

import android.os.Parcel;
import android.os.Parcelable;
/* compiled from: RemoteMessage.java */
/* loaded from: classes.dex */
class b implements Parcelable.Creator<RemoteMessage> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    /* renamed from: createFromParcel */
    public RemoteMessage mo232createFromParcel(Parcel parcel) {
        return new RemoteMessage(parcel);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    /* renamed from: newArray */
    public RemoteMessage[] mo233newArray(int i) {
        return new RemoteMessage[i];
    }
}
