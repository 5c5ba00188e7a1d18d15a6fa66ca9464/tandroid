package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda79 implements Comparator {
    public static final /* synthetic */ PhotoViewer$$ExternalSyntheticLambda79 INSTANCE = new PhotoViewer$$ExternalSyntheticLambda79();

    private /* synthetic */ PhotoViewer$$ExternalSyntheticLambda79() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$onPhotoShow$71;
        lambda$onPhotoShow$71 = PhotoViewer.lambda$onPhotoShow$71((MessageObject) obj, (MessageObject) obj2);
        return lambda$onPhotoShow$71;
    }
}
