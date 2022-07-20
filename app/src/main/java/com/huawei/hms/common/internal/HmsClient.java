package com.huawei.hms.common.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;
import com.huawei.hms.adapter.BaseAdapter;
import com.huawei.hms.common.internal.AnyClient;
import com.huawei.hms.common.internal.BaseHmsClient;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.log.HMSLog;
import java.lang.ref.WeakReference;
import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class HmsClient extends BaseHmsClient implements AnyClient {
    private static final String TAG = "HmsClient";

    public HmsClient(Context context, ClientSettings clientSettings, BaseHmsClient.OnConnectionFailedListener onConnectionFailedListener, BaseHmsClient.ConnectionCallbacks connectionCallbacks) {
        super(context, clientSettings, onConnectionFailedListener, connectionCallbacks);
    }

    @Override // com.huawei.hms.common.internal.AnyClient
    public void post(IMessageEntity iMessageEntity, String str, AnyClient.CallBack callBack) {
        if (callBack == null) {
            HMSLog.e("HmsClient", "callback is invalid, discard.");
            return;
        }
        boolean z = true;
        if ((iMessageEntity instanceof RequestHeader) && str != null) {
            if (!isConnected()) {
                HMSLog.i("HmsClient", "No connection now, the connection status:" + getConnectionStatus());
                if (getConnectionStatus() != 6) {
                    HMSLog.e("HmsClient", "post failed for not connected.");
                    callBack.onCallback(new ResponseHeader(1, 907135001, "Not Connected"), new JSONObject().toString());
                    return;
                }
                HMSLog.i("HmsClient", "in timeout-disconnect status, need to bind again.");
                bindCoreService();
            }
            RequestHeader requestHeader = (RequestHeader) iMessageEntity;
            HMSLog.i("HmsClient", "post msg " + requestHeader);
            Activity cpActivity = getClientSettings().getCpActivity();
            if (cpActivity != null) {
                z = false;
            }
            if (z) {
                HMSLog.i("HmsClient", "Activity is null for " + getClientSettings().getAppID());
            }
            (z ? new BaseAdapter(this) : new BaseAdapter(this, cpActivity)).baseRequest(requestHeader.toJson(), str, requestHeader.getParcelable(), new BaseAdapterCallBack(this, callBack));
            return;
        }
        HMSLog.e("HmsClient", "arguments is invalid.");
        callBack.onCallback(new ResponseHeader(1, 907135000, "Args is invalid"), new JSONObject().toString());
    }

    public void updateSessionId(String str) {
        if (TextUtils.isEmpty(this.sessionId)) {
            this.sessionId = str;
        }
    }

    /* loaded from: classes.dex */
    private static class BaseAdapterCallBack implements BaseAdapter.BaseCallBack {
        private final AnyClient.CallBack callback;
        private final WeakReference<HmsClient> hmsClient;

        BaseAdapterCallBack(HmsClient hmsClient, AnyClient.CallBack callBack) {
            this.callback = callBack;
            this.hmsClient = new WeakReference<>(hmsClient);
        }

        private void doCallback(String str, String str2) {
            ResponseHeader responseHeader = new ResponseHeader();
            if (responseHeader.fromJson(str)) {
                HMSLog.i("HmsClient", "receive msg " + responseHeader);
                updateSessionId(responseHeader.getSessionId());
                this.callback.onCallback(responseHeader, str2);
                return;
            }
            this.callback.onCallback(new ResponseHeader(1, 907135000, "response header json error"), new JSONObject().toString());
        }

        private void updateSessionId(String str) {
            HmsClient hmsClient = this.hmsClient.get();
            if (hmsClient != null) {
                hmsClient.updateSessionId(str);
            }
        }

        @Override // com.huawei.hms.adapter.BaseAdapter.BaseCallBack
        public void onComplete(String str, String str2, Parcelable parcelable) {
            if (parcelable == null) {
                doCallback(str, str2);
            } else {
                doCallback(str, str2, parcelable);
            }
        }

        @Override // com.huawei.hms.adapter.BaseAdapter.BaseCallBack
        public void onError(String str) {
            ResponseWrap responseWrap = new ResponseWrap(new ResponseHeader());
            if (responseWrap.fromJson(str)) {
                HMSLog.i("HmsClient", "receive msg " + responseWrap);
                ResponseHeader responseHeader = responseWrap.getResponseHeader();
                updateSessionId(responseHeader.getSessionId());
                this.callback.onCallback(responseHeader, responseWrap.getBody());
                return;
            }
            this.callback.onCallback(new ResponseHeader(1, 907135000, "response header json error"), new JSONObject().toString());
        }

        private void doCallback(String str, String str2, Parcelable parcelable) {
            ResponseHeader responseHeader = new ResponseHeader();
            if (responseHeader.fromJson(str)) {
                responseHeader.setParcelable(parcelable);
                HMSLog.i("HmsClient", "receive msg " + responseHeader);
                updateSessionId(responseHeader.getSessionId());
                this.callback.onCallback(responseHeader, str2);
                return;
            }
            this.callback.onCallback(new ResponseHeader(1, 907135000, "response header json error"), new JSONObject().toString());
        }
    }
}
