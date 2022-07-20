package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$5$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ TLRPC$InputFile f$2;
    public final /* synthetic */ TLRPC$InputEncryptedFile f$3;
    public final /* synthetic */ byte[] f$4;
    public final /* synthetic */ byte[] f$5;
    public final /* synthetic */ long f$6;

    public /* synthetic */ ImageLoader$5$$ExternalSyntheticLambda2(int i, String str, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, long j) {
        this.f$0 = i;
        this.f$1 = str;
        this.f$2 = tLRPC$InputFile;
        this.f$3 = tLRPC$InputEncryptedFile;
        this.f$4 = bArr;
        this.f$5 = bArr2;
        this.f$6 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageLoader.AnonymousClass5.lambda$fileDidUploaded$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
