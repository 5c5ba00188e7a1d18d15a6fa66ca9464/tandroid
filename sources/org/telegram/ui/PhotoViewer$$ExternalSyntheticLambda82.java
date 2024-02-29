package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda82 implements Comparator {
    public static final /* synthetic */ PhotoViewer$$ExternalSyntheticLambda82 INSTANCE = new PhotoViewer$$ExternalSyntheticLambda82();

    private /* synthetic */ PhotoViewer$$ExternalSyntheticLambda82() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$onPhotoShow$74;
        lambda$onPhotoShow$74 = PhotoViewer.lambda$onPhotoShow$74((MessageObject) obj, (MessageObject) obj2);
        return lambda$onPhotoShow$74;
    }
}
