package com.google.android.gms.internal.firebase_messaging;

import com.google.firebase.encoders.EncodingException;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import com.google.firebase.encoders.ValueEncoder;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class zzab implements ObjectEncoderContext {
    private static final Charset zza = Charset.forName("UTF-8");
    private static final FieldDescriptor zzg;
    private static final FieldDescriptor zzh;
    private static final ObjectEncoder zzi;
    private OutputStream zzb;
    private final Map zzc;
    private final Map zzd;
    private final ObjectEncoder zze;
    private final zzaf zzf = new zzaf(this);

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("key");
        zzv zzvVar = new zzv();
        zzvVar.zza(1);
        zzg = builder.withProperty(zzvVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("value");
        zzv zzvVar2 = new zzv();
        zzvVar2.zza(2);
        zzh = builder2.withProperty(zzvVar2.zzb()).build();
        zzi = zzaa.zza;
    }

    zzab(OutputStream outputStream, Map map, Map map2, ObjectEncoder objectEncoder) {
        this.zzb = outputStream;
        this.zzc = map;
        this.zzd = map2;
        this.zze = objectEncoder;
    }

    static final /* synthetic */ void zzg(Map.Entry entry, ObjectEncoderContext objectEncoderContext) {
        objectEncoderContext.add(zzg, entry.getKey());
        objectEncoderContext.add(zzh, entry.getValue());
    }

    private final zzab zzh(ObjectEncoder objectEncoder, FieldDescriptor fieldDescriptor, Object obj, boolean z) {
        long zzi2 = zzi(objectEncoder, obj);
        if (z && zzi2 == 0) {
            return this;
        }
        zzn((zzl(fieldDescriptor) << 3) | 2);
        zzo(zzi2);
        objectEncoder.encode(obj, this);
        return this;
    }

    private final long zzi(ObjectEncoder objectEncoder, Object obj) {
        zzw zzwVar = new zzw();
        try {
            OutputStream outputStream = this.zzb;
            this.zzb = zzwVar;
            try {
                objectEncoder.encode(obj, this);
                this.zzb = outputStream;
                long zza2 = zzwVar.zza();
                zzwVar.close();
                return zza2;
            } catch (Throwable th) {
                this.zzb = outputStream;
                throw th;
            }
        } catch (Throwable th2) {
            try {
                zzwVar.close();
            } catch (Throwable th3) {
                zzt.zza(th2, th3);
            }
            throw th2;
        }
    }

    private final zzab zzj(ValueEncoder valueEncoder, FieldDescriptor fieldDescriptor, Object obj, boolean z) {
        this.zzf.zza(fieldDescriptor, z);
        valueEncoder.encode(obj, this.zzf);
        return this;
    }

    private static ByteBuffer zzk(int i) {
        return ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static int zzl(FieldDescriptor fieldDescriptor) {
        zzz zzzVar = (zzz) fieldDescriptor.getProperty(zzz.class);
        if (zzzVar != null) {
            return zzzVar.zza();
        }
        throw new EncodingException("Field has no @Protobuf config");
    }

    private static zzz zzm(FieldDescriptor fieldDescriptor) {
        zzz zzzVar = (zzz) fieldDescriptor.getProperty(zzz.class);
        if (zzzVar != null) {
            return zzzVar;
        }
        throw new EncodingException("Field has no @Protobuf config");
    }

    private final void zzn(int i) {
        while (true) {
            long j = i & (-128);
            OutputStream outputStream = this.zzb;
            if (j == 0) {
                outputStream.write(i & NotificationCenter.dialogTranslate);
                return;
            } else {
                outputStream.write((i & NotificationCenter.dialogTranslate) | 128);
                i >>>= 7;
            }
        }
    }

    private final void zzo(long j) {
        while (true) {
            long j2 = (-128) & j;
            OutputStream outputStream = this.zzb;
            if (j2 == 0) {
                outputStream.write(((int) j) & NotificationCenter.dialogTranslate);
                return;
            } else {
                outputStream.write((((int) j) & NotificationCenter.dialogTranslate) | 128);
                j >>>= 7;
            }
        }
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public final /* bridge */ /* synthetic */ ObjectEncoderContext add(FieldDescriptor fieldDescriptor, int i) {
        zzd(fieldDescriptor, i, true);
        return this;
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public final /* bridge */ /* synthetic */ ObjectEncoderContext add(FieldDescriptor fieldDescriptor, long j) {
        zze(fieldDescriptor, j, true);
        return this;
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public final ObjectEncoderContext add(FieldDescriptor fieldDescriptor, Object obj) {
        zza(fieldDescriptor, obj, true);
        return this;
    }

    final ObjectEncoderContext zza(FieldDescriptor fieldDescriptor, Object obj, boolean z) {
        if (obj == null) {
            return this;
        }
        if (obj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) obj;
            if (z && charSequence.length() == 0) {
                return this;
            }
            zzn((zzl(fieldDescriptor) << 3) | 2);
            byte[] bytes = charSequence.toString().getBytes(zza);
            zzn(bytes.length);
            this.zzb.write(bytes);
            return this;
        }
        if (obj instanceof Collection) {
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext()) {
                zza(fieldDescriptor, it.next(), false);
            }
            return this;
        }
        if (obj instanceof Map) {
            Iterator it2 = ((Map) obj).entrySet().iterator();
            while (it2.hasNext()) {
                zzh(zzi, fieldDescriptor, (Map.Entry) it2.next(), false);
            }
            return this;
        }
        if (obj instanceof Double) {
            zzb(fieldDescriptor, ((Double) obj).doubleValue(), z);
            return this;
        }
        if (obj instanceof Float) {
            zzc(fieldDescriptor, ((Float) obj).floatValue(), z);
            return this;
        }
        if (obj instanceof Number) {
            zze(fieldDescriptor, ((Number) obj).longValue(), z);
            return this;
        }
        if (obj instanceof Boolean) {
            zzd(fieldDescriptor, ((Boolean) obj).booleanValue() ? 1 : 0, z);
            return this;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            if (z && bArr.length == 0) {
                return this;
            }
            zzn((zzl(fieldDescriptor) << 3) | 2);
            zzn(bArr.length);
            this.zzb.write(bArr);
            return this;
        }
        ObjectEncoder objectEncoder = (ObjectEncoder) this.zzc.get(obj.getClass());
        if (objectEncoder != null) {
            zzh(objectEncoder, fieldDescriptor, obj, z);
            return this;
        }
        ValueEncoder valueEncoder = (ValueEncoder) this.zzd.get(obj.getClass());
        if (valueEncoder != null) {
            zzj(valueEncoder, fieldDescriptor, obj, z);
            return this;
        }
        if (obj instanceof zzx) {
            zzd(fieldDescriptor, ((zzx) obj).getNumber(), true);
            return this;
        }
        if (obj instanceof Enum) {
            zzd(fieldDescriptor, ((Enum) obj).ordinal(), true);
            return this;
        }
        zzh(this.zze, fieldDescriptor, obj, z);
        return this;
    }

    final ObjectEncoderContext zzb(FieldDescriptor fieldDescriptor, double d, boolean z) {
        if (z && d == 0.0d) {
            return this;
        }
        zzn((zzl(fieldDescriptor) << 3) | 1);
        this.zzb.write(zzk(8).putDouble(d).array());
        return this;
    }

    final ObjectEncoderContext zzc(FieldDescriptor fieldDescriptor, float f, boolean z) {
        if (z && f == 0.0f) {
            return this;
        }
        zzn((zzl(fieldDescriptor) << 3) | 5);
        this.zzb.write(zzk(4).putFloat(f).array());
        return this;
    }

    final zzab zzd(FieldDescriptor fieldDescriptor, int i, boolean z) {
        if (z && i == 0) {
            return this;
        }
        zzz zzm = zzm(fieldDescriptor);
        zzy zzyVar = zzy.zza;
        int ordinal = zzm.zzb().ordinal();
        if (ordinal == 0) {
            zzn(zzm.zza() << 3);
            zzn(i);
        } else if (ordinal == 1) {
            zzn(zzm.zza() << 3);
            zzn((i + i) ^ (i >> 31));
        } else if (ordinal == 2) {
            zzn((zzm.zza() << 3) | 5);
            this.zzb.write(zzk(4).putInt(i).array());
        }
        return this;
    }

    final zzab zze(FieldDescriptor fieldDescriptor, long j, boolean z) {
        if (z && j == 0) {
            return this;
        }
        zzz zzm = zzm(fieldDescriptor);
        zzy zzyVar = zzy.zza;
        int ordinal = zzm.zzb().ordinal();
        if (ordinal == 0) {
            zzn(zzm.zza() << 3);
            zzo(j);
        } else if (ordinal == 1) {
            zzn(zzm.zza() << 3);
            zzo((j >> 63) ^ (j + j));
        } else if (ordinal == 2) {
            zzn((zzm.zza() << 3) | 1);
            this.zzb.write(zzk(8).putLong(j).array());
        }
        return this;
    }

    final zzab zzf(Object obj) {
        if (obj == null) {
            return this;
        }
        ObjectEncoder objectEncoder = (ObjectEncoder) this.zzc.get(obj.getClass());
        if (objectEncoder != null) {
            objectEncoder.encode(obj, this);
            return this;
        }
        String valueOf = String.valueOf(obj.getClass());
        StringBuilder sb = new StringBuilder(valueOf.length() + 15);
        sb.append("No encoder for ");
        sb.append(valueOf);
        throw new EncodingException(sb.toString());
    }
}
