package org.telegram.ui.Components;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes4.dex */
public interface AttachableDrawable {
    void onAttachedToWindow(ImageReceiver imageReceiver);

    void onDetachedFromWindow(ImageReceiver imageReceiver);
}
