package com.huawei.hms.opendevice;

import android.content.Context;
import com.huawei.hms.common.internal.AbstractClientBuilder;
import com.huawei.hms.common.internal.BaseHmsClient;
import com.huawei.hms.common.internal.ClientSettings;
/* loaded from: classes.dex */
public class OpenDeviceHmsClientBuilder extends AbstractClientBuilder<OpenDeviceHmsClient, OpenDeviceOptions> {
    @Override // com.huawei.hms.common.internal.AbstractClientBuilder
    /* renamed from: buildClient  reason: collision with other method in class */
    public OpenDeviceHmsClient mo230buildClient(Context context, ClientSettings clientSettings, BaseHmsClient.OnConnectionFailedListener onConnectionFailedListener, BaseHmsClient.ConnectionCallbacks connectionCallbacks) {
        return new OpenDeviceHmsClient(context, clientSettings, onConnectionFailedListener, connectionCallbacks);
    }
}
