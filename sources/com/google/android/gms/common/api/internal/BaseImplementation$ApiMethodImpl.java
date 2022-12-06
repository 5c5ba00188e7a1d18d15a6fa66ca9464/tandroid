package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public abstract class BaseImplementation$ApiMethodImpl<R extends Result, A extends Api.AnyClient> extends BasePendingResult<R> implements BaseImplementation$ResultHolder<R> {
    private final Api<?> mApi;
    private final Api.AnyClientKey<A> mClientKey;

    private void setFailedResult(RemoteException remoteException) {
        setFailedResult(new Status(8, remoteException.getLocalizedMessage(), (PendingIntent) null));
    }

    protected abstract void doExecute(A a) throws RemoteException;

    public final Api<?> getApi() {
        return this.mApi;
    }

    public final Api.AnyClientKey<A> getClientKey() {
        return this.mClientKey;
    }

    protected void onSetFailedResult(R r) {
    }

    public final void run(A a) throws DeadObjectException {
        try {
            doExecute(a);
        } catch (DeadObjectException e) {
            setFailedResult(e);
            throw e;
        } catch (RemoteException e2) {
            setFailedResult(e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((BaseImplementation$ApiMethodImpl<R, A>) ((Result) obj));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseImplementation$ApiMethodImpl(Api<?> api, GoogleApiClient googleApiClient) {
        super((GoogleApiClient) Preconditions.checkNotNull(googleApiClient, "GoogleApiClient must not be null"));
        Preconditions.checkNotNull(api, "Api must not be null");
        this.mClientKey = api.zab();
        this.mApi = api;
    }

    public final void setFailedResult(Status status) {
        Preconditions.checkArgument(!status.isSuccess(), "Failed result must not be success");
        R createFailedResult = createFailedResult(status);
        setResult((BaseImplementation$ApiMethodImpl<R, A>) createFailedResult);
        onSetFailedResult(createFailedResult);
    }
}
