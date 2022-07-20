package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ FileLoader f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ FileLoader$$ExternalSyntheticLambda4(FileLoader fileLoader, ArrayList arrayList) {
        this.f$0 = fileLoader;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkCurrentDownloadsFiles$13(this.f$1);
    }
}
