package org.telegram.ui;

import java.io.File;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda70 implements FileLoader.FileResolver {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda70(PhotoViewer photoViewer, TLObject tLObject) {
        this.f$0 = photoViewer;
        this.f$1 = tLObject;
    }

    @Override // org.telegram.messenger.FileLoader.FileResolver
    public final File getFile() {
        File lambda$checkProgress$67;
        lambda$checkProgress$67 = this.f$0.lambda$checkProgress$67(this.f$1);
        return lambda$checkProgress$67;
    }
}
