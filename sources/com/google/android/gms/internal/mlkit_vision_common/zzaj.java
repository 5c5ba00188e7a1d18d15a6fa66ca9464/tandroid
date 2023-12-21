package com.google.android.gms.internal.mlkit_vision_common;

import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.util.Map;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzaj implements ObjectEncoder {
    public static final /* synthetic */ zzaj zza = new zzaj();

    private /* synthetic */ zzaj() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final void encode(Object obj, Object obj2) {
        zzak.zzg((Map.Entry) obj, (ObjectEncoderContext) obj2);
    }
}
