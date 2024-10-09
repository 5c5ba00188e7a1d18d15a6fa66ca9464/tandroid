package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public final class Api {
    private final AbstractClientBuilder zaa;
    private final ClientKey zab;
    private final String zac;

    /* loaded from: classes.dex */
    public static abstract class AbstractClientBuilder extends BaseClientBuilder {
        public Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return buildClient(context, looper, clientSettings, obj, (ConnectionCallbacks) connectionCallbacks, (OnConnectionFailedListener) onConnectionFailedListener);
        }

        public Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            throw new UnsupportedOperationException("buildClient must be implemented");
        }
    }

    /* loaded from: classes.dex */
    public interface AnyClient {
    }

    /* loaded from: classes.dex */
    public static class AnyClientKey {
    }

    /* loaded from: classes.dex */
    public interface ApiOptions {
        public static final NoOptions NO_OPTIONS = new NoOptions(null);

        /* loaded from: classes.dex */
        public interface HasAccountOptions extends ApiOptions {
            Account getAccount();
        }

        /* loaded from: classes.dex */
        public static final class NoOptions implements ApiOptions {
            /* synthetic */ NoOptions(zaa zaaVar) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class BaseClientBuilder {
        public List getImpliedScopes(Object obj) {
            return Collections.emptyList();
        }

        public int getPriority() {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
    }

    /* loaded from: classes.dex */
    public interface Client extends AnyClient {
        void connect(BaseGmsClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks);

        void disconnect();

        void disconnect(String str);

        void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

        Feature[] getAvailableFeatures();

        String getEndpointPackageName();

        String getLastDisconnectMessage();

        int getMinApkVersion();

        void getRemoteService(IAccountAccessor iAccountAccessor, Set set);

        Set getScopesForConnectionlessNonSignIn();

        Intent getSignInIntent();

        boolean isConnected();

        boolean isConnecting();

        void onUserSignOut(BaseGmsClient.SignOutCallbacks signOutCallbacks);

        boolean providesSignIn();

        boolean requiresGooglePlayServices();

        boolean requiresSignIn();
    }

    /* loaded from: classes.dex */
    public static final class ClientKey extends AnyClientKey {
    }

    public Api(String str, AbstractClientBuilder abstractClientBuilder, ClientKey clientKey) {
        Preconditions.checkNotNull(abstractClientBuilder, "Cannot construct an Api with a null ClientBuilder");
        Preconditions.checkNotNull(clientKey, "Cannot construct an Api with a null ClientKey");
        this.zac = str;
        this.zaa = abstractClientBuilder;
        this.zab = clientKey;
    }

    public final AbstractClientBuilder zaa() {
        return this.zaa;
    }

    public final AnyClientKey zab() {
        return this.zab;
    }

    public final BaseClientBuilder zac() {
        return this.zaa;
    }

    public final String zad() {
        return this.zac;
    }
}
