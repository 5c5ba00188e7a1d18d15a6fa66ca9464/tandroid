package com.google.android.gms.common.internal;

import android.os.IInterface;
/* loaded from: classes.dex */
public interface IGmsServiceBroker extends IInterface {
    void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest);
}
