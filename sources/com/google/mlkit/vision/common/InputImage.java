package com.google.mlkit.vision.common;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.os.SystemClock;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.mlkit_vision_common.zzms;
import com.google.android.gms.internal.mlkit_vision_common.zzmu;
import com.google.mlkit.common.sdkinternal.MLTaskInput;
import java.nio.ByteBuffer;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public class InputImage implements MLTaskInput {
    private volatile Bitmap zza;
    private volatile ByteBuffer zzb;
    private final int zzd;
    private final int zze;
    private final int zzf;
    private final int zzg;
    private final Matrix zzh;

    private InputImage(Bitmap bitmap, int i) {
        this.zza = (Bitmap) Preconditions.checkNotNull(bitmap);
        this.zzd = bitmap.getWidth();
        this.zze = bitmap.getHeight();
        zza(i);
        this.zzf = i;
        this.zzg = -1;
        this.zzh = null;
    }

    public static InputImage fromBitmap(Bitmap bitmap, int i) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        InputImage inputImage = new InputImage(bitmap, i);
        zzc(-1, 1, elapsedRealtime, bitmap.getHeight(), bitmap.getWidth(), bitmap.getAllocationByteCount(), i);
        return inputImage;
    }

    private static int zza(int i) {
        boolean z = true;
        if (i != 0 && i != 90 && i != 180) {
            if (i == 270) {
                i = NotificationCenter.onDatabaseOpened;
            } else {
                z = false;
            }
        }
        Preconditions.checkArgument(z, "Invalid rotation. Only 0, 90, 180, 270 are supported currently.");
        return i;
    }

    private static void zzc(int i, int i2, long j, int i3, int i4, int i5, int i6) {
        zzmu.zza(zzms.zzb("vision-common"), i, i2, j, i3, i4, i5, i6);
    }

    public Bitmap getBitmapInternal() {
        return this.zza;
    }

    public ByteBuffer getByteBuffer() {
        return this.zzb;
    }

    public int getFormat() {
        return this.zzg;
    }

    public int getHeight() {
        return this.zze;
    }

    public Image getMediaImage() {
        return null;
    }

    public Image.Plane[] getPlanes() {
        return null;
    }

    public int getRotationDegrees() {
        return this.zzf;
    }

    public int getWidth() {
        return this.zzd;
    }
}
