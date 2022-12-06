package com.google.android.gms.common.internal;

import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.DoNotMock;
import com.google.errorprone.annotations.RestrictedInheritance;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
@DoNotMock("Use canonical fakes instead. go/cheezhead-testing-methodology")
@RestrictedInheritance(allowedOnPath = ".*java.*/com/google/android/gms.*", explanation = "Use canonical fakes instead.", link = "go/gmscore-restrictedinheritance")
/* loaded from: classes.dex */
public interface TelemetryLoggingClient {
    Task<Void> log(TelemetryData telemetryData);
}
