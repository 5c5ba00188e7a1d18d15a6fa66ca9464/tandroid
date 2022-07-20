package org.telegram.ui.Components;

import android.webkit.ValueCallback;
import org.telegram.ui.Components.WebPlayerView;
/* loaded from: classes3.dex */
public final /* synthetic */ class WebPlayerView$YoutubeVideoTask$$ExternalSyntheticLambda0 implements ValueCallback {
    public final /* synthetic */ WebPlayerView.YoutubeVideoTask f$0;

    public /* synthetic */ WebPlayerView$YoutubeVideoTask$$ExternalSyntheticLambda0(WebPlayerView.YoutubeVideoTask youtubeVideoTask) {
        this.f$0 = youtubeVideoTask;
    }

    @Override // android.webkit.ValueCallback
    public final void onReceiveValue(Object obj) {
        this.f$0.lambda$doInBackground$0((String) obj);
    }
}
