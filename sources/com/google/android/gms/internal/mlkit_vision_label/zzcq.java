package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.EncodingException;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.config.EncoderConfig;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzcq implements EncoderConfig {
    public static final /* synthetic */ int $r8$clinit = 0;
    private static final ObjectEncoder zzb = new ObjectEncoder() { // from class: com.google.android.gms.internal.mlkit_vision_label.zzcp
        @Override // com.google.firebase.encoders.ObjectEncoder
        public final void encode(Object obj, Object obj2) {
            int i = zzcq.$r8$clinit;
            throw new EncodingException("Couldn't find encoder for type ".concat(String.valueOf(obj.getClass().getCanonicalName())));
        }
    };
    private final Map zzc = new HashMap();
    private final Map zzd = new HashMap();
    private final ObjectEncoder zze = zzb;

    @Override // com.google.firebase.encoders.config.EncoderConfig
    public final /* bridge */ /* synthetic */ EncoderConfig registerEncoder(Class cls, ObjectEncoder objectEncoder) {
        this.zzc.put(cls, objectEncoder);
        this.zzd.remove(cls);
        return this;
    }

    public final zzcr zza() {
        return new zzcr(new HashMap(this.zzc), new HashMap(this.zzd), this.zze);
    }
}
