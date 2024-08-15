package org.telegram.ui;

import org.telegram.messenger.Utilities;
import org.telegram.ui.web.BrowserHistory;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda54 implements Utilities.Callback {
    public final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda54(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    @Override // org.telegram.messenger.Utilities.Callback
    public final void run(Object obj) {
        this.f$0.openHistoryEntry((BrowserHistory.Entry) obj);
    }
}
