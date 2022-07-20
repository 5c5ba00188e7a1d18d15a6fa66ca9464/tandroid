package org.telegram.ui;

import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.FiltersListBottomSheet;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$30$$ExternalSyntheticLambda2 implements FiltersListBottomSheet.FiltersListBottomSheetDelegate {
    public final /* synthetic */ DialogsActivity.AnonymousClass30 f$0;

    public /* synthetic */ DialogsActivity$30$$ExternalSyntheticLambda2(DialogsActivity.AnonymousClass30 anonymousClass30) {
        this.f$0 = anonymousClass30;
    }

    @Override // org.telegram.ui.Components.FiltersListBottomSheet.FiltersListBottomSheetDelegate
    public final void didSelectFilter(MessagesController.DialogFilter dialogFilter) {
        this.f$0.lambda$onItemClick$2(dialogFilter);
    }
}
