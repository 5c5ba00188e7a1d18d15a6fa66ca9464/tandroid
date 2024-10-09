package com.google.android.datatransport.cct.internal;

import com.google.firebase.encoders.DataEncoder;
import com.google.firebase.encoders.json.JsonDataEncoderBuilder;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BatchedLogRequest {
    public static BatchedLogRequest create(List list) {
        return new AutoValue_BatchedLogRequest(list);
    }

    public static DataEncoder createDataEncoder() {
        return new JsonDataEncoderBuilder().configureWith(AutoBatchedLogRequestEncoder.CONFIG).ignoreNullValues(true).build();
    }

    public abstract List getLogRequests();
}
