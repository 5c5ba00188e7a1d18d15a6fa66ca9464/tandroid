package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import org.telegram.messenger.support.customtabs.ICustomTabsService;
/* loaded from: classes.dex */
public abstract class CustomTabsServiceConnection implements ServiceConnection {
    public abstract void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient);

    /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsServiceConnection$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 extends CustomTabsClient {
        AnonymousClass1(CustomTabsServiceConnection customTabsServiceConnection, ICustomTabsService iCustomTabsService, ComponentName componentName) {
            super(iCustomTabsService, componentName);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        onCustomTabsServiceConnected(componentName, new AnonymousClass1(this, ICustomTabsService.Stub.asInterface(iBinder), componentName));
    }
}
