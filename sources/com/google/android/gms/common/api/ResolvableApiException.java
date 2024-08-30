package com.google.android.gms.common.api;

import android.app.Activity;
/* loaded from: classes.dex */
public class ResolvableApiException extends ApiException {
    public ResolvableApiException(Status status) {
        super(status);
    }

    public void startResolutionForResult(Activity activity, int i) {
        getStatus().startResolutionForResult(activity, i);
    }
}
