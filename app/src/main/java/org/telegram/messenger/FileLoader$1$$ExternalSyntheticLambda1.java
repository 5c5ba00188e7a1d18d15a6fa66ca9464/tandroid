package org.telegram.messenger;

import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ FileLoader.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ TLRPC$InputFile f$4;
    public final /* synthetic */ TLRPC$InputEncryptedFile f$5;
    public final /* synthetic */ byte[] f$6;
    public final /* synthetic */ byte[] f$7;
    public final /* synthetic */ FileUploadOperation f$8;

    public /* synthetic */ FileLoader$1$$ExternalSyntheticLambda1(FileLoader.AnonymousClass1 anonymousClass1, boolean z, String str, boolean z2, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, FileUploadOperation fileUploadOperation) {
        this.f$0 = anonymousClass1;
        this.f$1 = z;
        this.f$2 = str;
        this.f$3 = z2;
        this.f$4 = tLRPC$InputFile;
        this.f$5 = tLRPC$InputEncryptedFile;
        this.f$6 = bArr;
        this.f$7 = bArr2;
        this.f$8 = fileUploadOperation;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didFinishUploadingFile$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
    }
}
