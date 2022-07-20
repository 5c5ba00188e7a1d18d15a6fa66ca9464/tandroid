package org.telegram.ui.Components;

import org.telegram.ui.Components.LinkActionView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PermanentLinkBottomSheet$$ExternalSyntheticLambda5 implements LinkActionView.Delegate {
    public final /* synthetic */ PermanentLinkBottomSheet f$0;

    public /* synthetic */ PermanentLinkBottomSheet$$ExternalSyntheticLambda5(PermanentLinkBottomSheet permanentLinkBottomSheet) {
        this.f$0 = permanentLinkBottomSheet;
    }

    @Override // org.telegram.ui.Components.LinkActionView.Delegate
    public /* synthetic */ void editLink() {
        LinkActionView.Delegate.CC.$default$editLink(this);
    }

    @Override // org.telegram.ui.Components.LinkActionView.Delegate
    public /* synthetic */ void removeLink() {
        LinkActionView.Delegate.CC.$default$removeLink(this);
    }

    @Override // org.telegram.ui.Components.LinkActionView.Delegate
    public final void revokeLink() {
        this.f$0.lambda$new$0();
    }

    @Override // org.telegram.ui.Components.LinkActionView.Delegate
    public /* synthetic */ void showUsersForPermanentLink() {
        LinkActionView.Delegate.CC.$default$showUsersForPermanentLink(this);
    }
}
