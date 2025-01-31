package com.google.android.recaptcha.internal;

import android.content.Context;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes.dex */
public final class zzeo implements zzen {
    private final Context zzb;
    private final Map zzc;

    public zzeo(Context context) {
        Map mapOf;
        this.zzb = context;
        mapOf = MapsKt__MapsKt.mapOf(TuplesKt.to(2, "activity"), TuplesKt.to(3, "phone"), TuplesKt.to(4, "input_method"), TuplesKt.to(5, MediaStreamTrack.AUDIO_TRACK_KIND));
        this.zzc = mapOf;
    }

    @Override // com.google.android.recaptcha.internal.zzen
    public final /* synthetic */ Object cs(Object[] objArr) {
        return zzel.zza(this, objArr);
    }

    @Override // com.google.android.recaptcha.internal.zzen
    public final Object zza(Object... objArr) {
        Object obj = objArr[0];
        if (true != (obj instanceof Integer)) {
            obj = null;
        }
        Integer num = (Integer) obj;
        if (num == null) {
            throw new zzae(4, 5, null);
        }
        Object obj2 = this.zzc.get(Integer.valueOf(num.intValue()));
        if (obj2 != null) {
            return this.zzb.getSystemService((String) obj2);
        }
        throw new zzae(4, 4, null);
    }
}
