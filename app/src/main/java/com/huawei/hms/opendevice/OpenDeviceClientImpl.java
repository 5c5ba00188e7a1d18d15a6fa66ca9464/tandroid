package com.huawei.hms.opendevice;

import android.content.Context;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.api.Api;
import com.huawei.hms.common.HuaweiApi;
import com.huawei.hms.support.api.opendevice.OdidResult;
import com.huawei.hms.support.hianalytics.HiAnalyticsClient;
import com.huawei.hms.utils.JsonUtil;
/* loaded from: classes.dex */
public class OpenDeviceClientImpl extends HuaweiApi<OpenDeviceOptions> implements OpenDeviceClient {
    public static final OpenDeviceHmsClientBuilder a = new OpenDeviceHmsClientBuilder();
    public static final Api<OpenDeviceOptions> b = new Api<>("HuaweiOpenDevice.API");
    public static OpenDeviceOptions c = new OpenDeviceOptions();

    public OpenDeviceClientImpl(Context context) {
        super(context, b, c, a);
        super.setKitSdkVersion(60300305);
    }

    @Override // com.huawei.hms.opendevice.OpenDeviceClient
    public Task<OdidResult> getOdid() {
        return doWrite(new OpenDeviceTaskApiCall("opendevice.getodid", JsonUtil.createJsonString(null), HiAnalyticsClient.reportEntry(getContext(), "opendevice.getodid", 60300305)));
    }
}
