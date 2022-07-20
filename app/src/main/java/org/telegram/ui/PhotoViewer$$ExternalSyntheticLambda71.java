package org.telegram.ui;

import java.io.File;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda71 implements FileLoader.FileResolver {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ TLRPC$Message f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda71(PhotoViewer photoViewer, TLRPC$Message tLRPC$Message) {
        this.f$0 = photoViewer;
        this.f$1 = tLRPC$Message;
    }

    @Override // org.telegram.messenger.FileLoader.FileResolver
    public final File getFile() {
        File lambda$checkProgress$68;
        lambda$checkProgress$68 = this.f$0.lambda$checkProgress$68(this.f$1);
        return lambda$checkProgress$68;
    }
}
