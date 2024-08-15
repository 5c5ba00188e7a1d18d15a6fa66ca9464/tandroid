package org.telegram.ui.Stories.recorder;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Size;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class PreviewView$$ExternalSyntheticApiModelOutline0 {
    public static /* bridge */ /* synthetic */ Bitmap m(ContentResolver contentResolver, Uri uri, Size size, CancellationSignal cancellationSignal) {
        return contentResolver.loadThumbnail(uri, size, cancellationSignal);
    }
}
