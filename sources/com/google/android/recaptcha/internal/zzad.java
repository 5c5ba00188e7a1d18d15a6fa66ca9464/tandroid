package com.google.android.recaptcha.internal;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import kotlin.io.FilesKt__FileReadWriteKt;

/* loaded from: classes.dex */
public final class zzad {
    private final Context zza;

    public zzad(Context context) {
        this.zza = context;
    }

    public static final byte[] zza(File file) {
        byte[] readBytes;
        readBytes = FilesKt__FileReadWriteKt.readBytes(file);
        return readBytes;
    }

    public static final void zzb(File file, byte[] bArr) {
        if (file.exists() && !file.delete()) {
            throw new IOException("Unable to delete existing encrypted file");
        }
        FilesKt__FileReadWriteKt.writeBytes(file, bArr);
    }
}
