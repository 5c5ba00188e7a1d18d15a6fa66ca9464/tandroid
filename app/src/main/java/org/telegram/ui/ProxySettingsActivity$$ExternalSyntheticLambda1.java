package org.telegram.ui;

import android.content.ClipboardManager;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProxySettingsActivity$$ExternalSyntheticLambda1 implements ClipboardManager.OnPrimaryClipChangedListener {
    public final /* synthetic */ ProxySettingsActivity f$0;

    public /* synthetic */ ProxySettingsActivity$$ExternalSyntheticLambda1(ProxySettingsActivity proxySettingsActivity) {
        this.f$0 = proxySettingsActivity;
    }

    @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
    public final void onPrimaryClipChanged() {
        this.f$0.updatePasteCell();
    }
}
