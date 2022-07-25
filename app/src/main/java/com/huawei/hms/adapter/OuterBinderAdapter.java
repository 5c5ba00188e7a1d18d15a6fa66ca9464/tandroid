package com.huawei.hms.adapter;

import android.content.Context;
import com.huawei.hms.common.internal.Objects;
import com.huawei.hms.support.log.HMSLog;
/* loaded from: classes.dex */
public class OuterBinderAdapter extends BinderAdapter {
    private static final Object LOCK_OBJECT_INIT = new Object();
    private static final int MSG_CONN_TIMEOUT = 1001;
    private static final int MSG_DELAY_DISCONNECT = 1002;
    private static final String TAG = "OuterBinderAdapter";
    private static BinderAdapter adapter;
    private static String sActionName;
    private static String sServiceName;

    private OuterBinderAdapter(Context context, String str, String str2) {
        super(context, str, str2);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0030 A[Catch: all -> 0x004b, TryCatch #0 {, blocks: (B:4:0x000a, B:6:0x000e, B:7:0x0047, B:8:0x0049, B:12:0x001a, B:14:0x0022, B:19:0x0030), top: B:3:0x000a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BinderAdapter getInstance(Context context, String str, String str2) {
        boolean z;
        BinderAdapter binderAdapter;
        HMSLog.i(TAG, "OuterBinderAdapter getInstance.");
        synchronized (LOCK_OBJECT_INIT) {
            if (adapter == null) {
                sActionName = str;
                sServiceName = str2;
                adapter = new OuterBinderAdapter(context, str, str2);
            } else {
                if (Objects.equal(sActionName, str) && Objects.equal(sServiceName, str2)) {
                    z = false;
                    if (z) {
                        HMSLog.i(TAG, "OuterBinderAdapter getInstance refresh adapter");
                        sActionName = str;
                        sServiceName = str2;
                        adapter.unBind();
                        adapter = new OuterBinderAdapter(context, str, str2);
                    }
                }
                z = true;
                if (z) {
                }
            }
            binderAdapter = adapter;
        }
        return binderAdapter;
    }

    @Override // com.huawei.hms.adapter.BinderAdapter
    protected int getConnTimeOut() {
        return 1001;
    }

    @Override // com.huawei.hms.adapter.BinderAdapter
    protected int getMsgDelayDisconnect() {
        return 1002;
    }
}
