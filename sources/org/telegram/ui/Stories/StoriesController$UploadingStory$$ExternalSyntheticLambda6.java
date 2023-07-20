package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Stories.StoriesController;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$UploadingStory$$ExternalSyntheticLambda6 implements RequestDelegate {
    public static final /* synthetic */ StoriesController$UploadingStory$$ExternalSyntheticLambda6 INSTANCE = new StoriesController$UploadingStory$$ExternalSyntheticLambda6();

    private /* synthetic */ StoriesController$UploadingStory$$ExternalSyntheticLambda6() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StoriesController.UploadingStory.lambda$sendUploadedRequest$3(tLObject, tLRPC$TL_error);
    }
}
