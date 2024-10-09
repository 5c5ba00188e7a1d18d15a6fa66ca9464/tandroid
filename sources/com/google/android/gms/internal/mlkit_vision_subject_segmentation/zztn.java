package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.TelemetryData;
import com.google.android.gms.common.internal.TelemetryLogging;
import com.google.android.gms.common.internal.TelemetryLoggingClient;
import com.google.android.gms.common.internal.TelemetryLoggingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public final class zztn {
    private final TelemetryLoggingClient zza;
    private final AtomicLong zzb = new AtomicLong(-1);

    zztn(Context context, String str) {
        this.zza = TelemetryLogging.getClient(context, TelemetryLoggingOptions.builder().setApi("mlkit:vision").build());
    }

    public static zztn zza(Context context) {
        return new zztn(context, "mlkit:vision");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzb(long j, Exception exc) {
        this.zzb.set(j);
    }

    public final synchronized void zzc(int i, int i2, long j, long j2) {
        AtomicLong atomicLong = this.zzb;
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        if (atomicLong.get() != -1 && elapsedRealtime - this.zzb.get() <= TimeUnit.MINUTES.toMillis(30L)) {
            return;
        }
        this.zza.log(new TelemetryData(0, Arrays.asList(new MethodInvocation(24336, i2, 0, j, j2, null, null, 0)))).addOnFailureListener(new OnFailureListener() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztm
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                zztn.this.zzb(elapsedRealtime, exc);
            }
        });
    }
}
