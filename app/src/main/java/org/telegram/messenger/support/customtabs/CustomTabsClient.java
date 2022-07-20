package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import org.telegram.messenger.support.customtabs.ICustomTabsCallback;
/* loaded from: classes.dex */
public class CustomTabsClient {
    private final ICustomTabsService mService;
    private final ComponentName mServiceComponentName;

    public CustomTabsClient(ICustomTabsService iCustomTabsService, ComponentName componentName) {
        this.mService = iCustomTabsService;
        this.mServiceComponentName = componentName;
    }

    public static boolean bindCustomTabsService(Context context, String str, CustomTabsServiceConnection customTabsServiceConnection) {
        Intent intent = new Intent("android.support.customtabs.action.CustomTabsService");
        if (!TextUtils.isEmpty(str)) {
            intent.setPackage(str);
        }
        return context.bindService(intent, customTabsServiceConnection, 33);
    }

    public boolean warmup(long j) {
        try {
            return this.mService.warmup(j);
        } catch (RemoteException unused) {
            return false;
        }
    }

    /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsClient$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends ICustomTabsCallback.Stub {
        private Handler mHandler = new Handler(Looper.getMainLooper());
        final /* synthetic */ CustomTabsCallback val$callback;

        AnonymousClass2(CustomTabsClient customTabsClient, CustomTabsCallback customTabsCallback) {
            this.val$callback = customTabsCallback;
        }

        /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsClient$2$1 */
        /* loaded from: classes.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ Bundle val$extras;
            final /* synthetic */ int val$navigationEvent;

            AnonymousClass1(int i, Bundle bundle) {
                AnonymousClass2.this = r1;
                this.val$navigationEvent = i;
                this.val$extras = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass2.this.val$callback.onNavigationEvent(this.val$navigationEvent, this.val$extras);
            }
        }

        @Override // org.telegram.messenger.support.customtabs.ICustomTabsCallback
        public void onNavigationEvent(int i, Bundle bundle) {
            if (this.val$callback == null) {
                return;
            }
            this.mHandler.post(new AnonymousClass1(i, bundle));
        }

        /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsClient$2$2 */
        /* loaded from: classes.dex */
        class RunnableC00102 implements Runnable {
            final /* synthetic */ Bundle val$args;
            final /* synthetic */ String val$callbackName;

            RunnableC00102(String str, Bundle bundle) {
                AnonymousClass2.this = r1;
                this.val$callbackName = str;
                this.val$args = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass2.this.val$callback.extraCallback(this.val$callbackName, this.val$args);
            }
        }

        @Override // org.telegram.messenger.support.customtabs.ICustomTabsCallback
        public void extraCallback(String str, Bundle bundle) throws RemoteException {
            if (this.val$callback == null) {
                return;
            }
            this.mHandler.post(new RunnableC00102(str, bundle));
        }

        /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsClient$2$3 */
        /* loaded from: classes.dex */
        class AnonymousClass3 implements Runnable {
            final /* synthetic */ Bundle val$extras;

            AnonymousClass3(Bundle bundle) {
                AnonymousClass2.this = r1;
                this.val$extras = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass2.this.val$callback.onMessageChannelReady(this.val$extras);
            }
        }

        @Override // org.telegram.messenger.support.customtabs.ICustomTabsCallback
        public void onMessageChannelReady(Bundle bundle) throws RemoteException {
            if (this.val$callback == null) {
                return;
            }
            this.mHandler.post(new AnonymousClass3(bundle));
        }

        /* renamed from: org.telegram.messenger.support.customtabs.CustomTabsClient$2$4 */
        /* loaded from: classes.dex */
        class AnonymousClass4 implements Runnable {
            final /* synthetic */ Bundle val$extras;
            final /* synthetic */ String val$message;

            AnonymousClass4(String str, Bundle bundle) {
                AnonymousClass2.this = r1;
                this.val$message = str;
                this.val$extras = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass2.this.val$callback.onPostMessage(this.val$message, this.val$extras);
            }
        }

        @Override // org.telegram.messenger.support.customtabs.ICustomTabsCallback
        public void onPostMessage(String str, Bundle bundle) throws RemoteException {
            if (this.val$callback == null) {
                return;
            }
            this.mHandler.post(new AnonymousClass4(str, bundle));
        }
    }

    public CustomTabsSession newSession(CustomTabsCallback customTabsCallback) {
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, customTabsCallback);
        try {
            if (this.mService.newSession(anonymousClass2)) {
                return new CustomTabsSession(this.mService, anonymousClass2, this.mServiceComponentName);
            }
            return null;
        } catch (RemoteException unused) {
            return null;
        }
    }
}
