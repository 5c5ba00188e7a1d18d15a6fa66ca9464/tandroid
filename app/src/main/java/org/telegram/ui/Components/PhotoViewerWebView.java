package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.Components.PhotoViewerWebView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class PhotoViewerWebView extends FrameLayout {
    private float bufferedPosition;
    private int currentPosition;
    private TLRPC$WebPage currentWebpage;
    private boolean isPlaying;
    private boolean isTouchDisabled;
    private boolean isYouTube;
    private PhotoViewer photoViewer;
    private View pipItem;
    private float playbackSpeed;
    private RadialProgressView progressBar;
    private View progressBarBlackBackground;
    private boolean setPlaybackSpeed;
    private int videoDuration;
    private WebView webView;
    private int currentAccount = UserConfig.selectedAccount;
    private Runnable progressRunnable = new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            PhotoViewerWebView.this.lambda$new$0();
        }
    };

    protected void drawBlackBackground(Canvas canvas, int i, int i2) {
    }

    public void hideControls() {
    }

    protected void processTouch(MotionEvent motionEvent) {
    }

    public void showControls() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.isYouTube) {
            runJsCode("pollPosition();");
        }
        if (this.isPlaying) {
            AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class YoutubeProxy {
        private YoutubeProxy() {
        }

        @JavascriptInterface
        public void onPlayerLoaded() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerLoaded$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerLoaded$0() {
            PhotoViewerWebView.this.progressBar.setVisibility(4);
            PhotoViewerWebView.this.progressBarBlackBackground.setVisibility(4);
            if (PhotoViewerWebView.this.setPlaybackSpeed) {
                PhotoViewerWebView.this.setPlaybackSpeed = false;
                PhotoViewerWebView photoViewerWebView = PhotoViewerWebView.this;
                photoViewerWebView.setPlaybackSpeed(photoViewerWebView.playbackSpeed);
            }
            PhotoViewerWebView.this.pipItem.setEnabled(true);
            PhotoViewerWebView.this.pipItem.setAlpha(1.0f);
        }

        @JavascriptInterface
        public void onPlayerStateChange(String str) {
            int parseInt = Integer.parseInt(str);
            boolean z = PhotoViewerWebView.this.isPlaying;
            final boolean z2 = false;
            final int i = 3;
            PhotoViewerWebView.this.isPlaying = parseInt == 1 || parseInt == 3;
            PhotoViewerWebView.this.checkPlayingPoll(z);
            if (parseInt == 0) {
                i = 4;
            } else if (parseInt == 1) {
                z2 = true;
            } else if (parseInt != 2) {
                if (parseInt != 3) {
                    i = 1;
                } else {
                    z2 = true;
                    i = 2;
                }
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerStateChange$1(z2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerStateChange$1(boolean z, int i) {
            PhotoViewerWebView.this.photoViewer.updateWebPlayerState(z, i);
        }

        @JavascriptInterface
        public void onPlayerNotifyDuration(int i) {
            PhotoViewerWebView.this.videoDuration = i * 1000;
        }

        @JavascriptInterface
        public void onPlayerNotifyCurrentPosition(int i) {
            PhotoViewerWebView.this.currentPosition = i * 1000;
        }

        @JavascriptInterface
        public void onPlayerNotifyBufferedPosition(float f) {
            PhotoViewerWebView.this.bufferedPosition = f;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public PhotoViewerWebView(PhotoViewer photoViewer, Context context, View view) {
        super(context);
        this.photoViewer = photoViewer;
        this.pipItem = view;
        WebView webView = new WebView(context) { // from class: org.telegram.ui.Components.PhotoViewerWebView.1
            @Override // android.webkit.WebView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                PhotoViewerWebView.this.processTouch(motionEvent);
                return super.onTouchEvent(motionEvent);
            }
        };
        this.webView = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        int i = Build.VERSION.SDK_INT;
        if (i >= 17) {
            this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (i >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
        }
        this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.Components.PhotoViewerWebView.2
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView2, String str) {
                super.onPageFinished(webView2, str);
                if (!PhotoViewerWebView.this.isYouTube || Build.VERSION.SDK_INT < 17) {
                    PhotoViewerWebView.this.progressBar.setVisibility(4);
                    PhotoViewerWebView.this.progressBarBlackBackground.setVisibility(4);
                    PhotoViewerWebView.this.pipItem.setEnabled(true);
                    PhotoViewerWebView.this.pipItem.setAlpha(1.0f);
                }
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView2, String str) {
                if (PhotoViewerWebView.this.isYouTube) {
                    Browser.openUrl(webView2.getContext(), str);
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView2, str);
            }
        });
        addView(this.webView, LayoutHelper.createFrame(-1, -1, 51));
        View view2 = new View(context) { // from class: org.telegram.ui.Components.PhotoViewerWebView.3
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                PhotoViewerWebView.this.drawBlackBackground(canvas, getMeasuredWidth(), getMeasuredHeight());
            }
        };
        this.progressBarBlackBackground = view2;
        view2.setBackgroundColor(-16777216);
        this.progressBarBlackBackground.setVisibility(4);
        addView(this.progressBarBlackBackground, LayoutHelper.createFrame(-1, -1.0f));
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressBar = radialProgressView;
        radialProgressView.setVisibility(4);
        addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
    }

    public void setTouchDisabled(boolean z) {
        this.isTouchDisabled = z;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.isTouchDisabled) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public WebView getWebView() {
        return this.webView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPlayingPoll(boolean z) {
        if (!z && this.isPlaying) {
            AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
        } else if (!z || this.isPlaying) {
        } else {
            AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
        }
    }

    public void seekTo(long j) {
        seekTo(j, true);
    }

    public void seekTo(final long j, final boolean z) {
        boolean z2 = this.isPlaying;
        this.currentPosition = (int) j;
        if (z2) {
            pauseVideo();
        }
        if (z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.this.lambda$seekTo$1(j, z);
                }
            }, 100L);
            return;
        }
        runJsCode("seekTo(" + Math.round(((float) j) / 1000.0f) + ", " + z + ");");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$seekTo$1(long j, boolean z) {
        runJsCode("seekTo(" + Math.round(((float) j) / 1000.0f) + ", " + z + ");");
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoViewerWebView.this.playVideo();
            }
        }, 100L);
    }

    public int getVideoDuration() {
        return this.videoDuration;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public float getBufferedPosition() {
        return this.bufferedPosition;
    }

    private void runJsCode(String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.webView.evaluateJavascript(str, null);
            return;
        }
        try {
            WebView webView = this.webView;
            webView.loadUrl("javascript:" + str);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.webView.getParent() == this) {
            TLRPC$WebPage tLRPC$WebPage = this.currentWebpage;
            int i3 = tLRPC$WebPage.embed_width;
            int i4 = 100;
            if (i3 == 0) {
                i3 = 100;
            }
            int i5 = tLRPC$WebPage.embed_height;
            if (i5 != 0) {
                i4 = i5;
            }
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            float f = i3;
            float f2 = i4;
            float min = Math.min(size / f, size2 / f2);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.webView.getLayoutParams();
            int i6 = (int) (f * min);
            layoutParams.width = i6;
            int i7 = (int) (f2 * min);
            layoutParams.height = i7;
            layoutParams.topMargin = (size2 - i7) / 2;
            layoutParams.leftMargin = (size - i6) / 2;
        }
        super.onMeasure(i, i2);
    }

    public boolean isLoaded() {
        return this.progressBar.getVisibility() != 0;
    }

    public boolean isInAppOnly() {
        return this.isYouTube && "inapp".equals(MessagesController.getInstance(this.currentAccount).youtubePipType);
    }

    public boolean openInPip() {
        boolean isInAppOnly = isInAppOnly();
        if ((isInAppOnly || checkInlinePermissions()) && this.progressBar.getVisibility() != 0) {
            if (PipVideoOverlay.isVisible()) {
                PipVideoOverlay.dismiss();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoViewerWebView.this.openInPip();
                    }
                }, 300L);
                return true;
            }
            WebView webView = this.webView;
            TLRPC$WebPage tLRPC$WebPage = this.currentWebpage;
            if (PipVideoOverlay.show(isInAppOnly, (Activity) getContext(), this, webView, tLRPC$WebPage.embed_width, tLRPC$WebPage.embed_height, false)) {
                PipVideoOverlay.setPhotoViewer(PhotoViewer.getInstance());
            }
            return true;
        }
        return false;
    }

    public boolean isControllable() {
        return this.isYouTube;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void playVideo() {
        if (this.isPlaying || !isControllable()) {
            return;
        }
        runJsCode("playVideo();");
        this.isPlaying = true;
        checkPlayingPoll(false);
    }

    public void pauseVideo() {
        if (!this.isPlaying || !isControllable()) {
            return;
        }
        runJsCode("pauseVideo();");
        this.isPlaying = false;
        checkPlayingPoll(true);
    }

    public void setPlaybackSpeed(float f) {
        this.playbackSpeed = f;
        if (this.progressBar.getVisibility() != 0) {
            if (!this.isYouTube) {
                return;
            }
            runJsCode("setPlaybackSpeed(" + f + ");");
            return;
        }
        this.setPlaybackSpeed = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x00a6 A[Catch: Exception -> 0x00e9, LOOP:0: B:10:0x009f->B:12:0x00a6, LOOP_END, TryCatch #0 {Exception -> 0x00e9, blocks: (B:4:0x0013, B:6:0x0021, B:9:0x0088, B:10:0x009f, B:12:0x00a6, B:14:0x00aa, B:41:0x0084, B:42:0x00d5, B:28:0x002f, B:30:0x0035, B:32:0x0048, B:34:0x0050, B:36:0x0058, B:38:0x005e, B:39:0x007a), top: B:2:0x0011, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00aa A[EDGE_INSN: B:13:0x00aa->B:14:0x00aa ?: BREAK  , SYNTHETIC] */
    @SuppressLint({"AddJavascriptInterface"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void init(int i, TLRPC$WebPage tLRPC$WebPage) {
        int intValue;
        InputStream open;
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] bArr;
        int read;
        this.currentWebpage = tLRPC$WebPage;
        String youTubeVideoId = WebPlayerView.getYouTubeVideoId(tLRPC$WebPage.embed_url);
        String str = tLRPC$WebPage.url;
        requestLayout();
        try {
            if (youTubeVideoId != null) {
                this.progressBarBlackBackground.setVisibility(0);
                this.isYouTube = true;
                String str2 = null;
                if (Build.VERSION.SDK_INT >= 17) {
                    this.webView.addJavascriptInterface(new YoutubeProxy(), "YoutubeProxy");
                }
                if (str != null) {
                    try {
                        Uri parse = Uri.parse(str);
                        if (i > 0) {
                            str2 = "" + i;
                        }
                        if (str2 == null && (str2 = parse.getQueryParameter("t")) == null) {
                            str2 = parse.getQueryParameter("time_continue");
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (str2 != null) {
                        if (str2.contains("m")) {
                            String[] split = str2.split("m");
                            intValue = (Utilities.parseInt((CharSequence) split[0]).intValue() * 60) + Utilities.parseInt((CharSequence) split[1]).intValue();
                        } else {
                            intValue = Utilities.parseInt((CharSequence) str2).intValue();
                        }
                        open = getContext().getAssets().open("youtube_embed.html");
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        bArr = new byte[10240];
                        while (true) {
                            read = open.read(bArr);
                            if (read != -1) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, read);
                        }
                        byteArrayOutputStream.close();
                        open.close();
                        this.webView.loadDataWithBaseURL("https://messenger.telegram.org/", String.format(Locale.US, byteArrayOutputStream.toString("UTF-8"), youTubeVideoId, Integer.valueOf(intValue)), "text/html", "UTF-8", "https://youtube.com");
                    }
                }
                intValue = 0;
                open = getContext().getAssets().open("youtube_embed.html");
                byteArrayOutputStream = new ByteArrayOutputStream();
                bArr = new byte[10240];
                while (true) {
                    read = open.read(bArr);
                    if (read != -1) {
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byteArrayOutputStream.close();
                open.close();
                this.webView.loadDataWithBaseURL("https://messenger.telegram.org/", String.format(Locale.US, byteArrayOutputStream.toString("UTF-8"), youTubeVideoId, Integer.valueOf(intValue)), "text/html", "UTF-8", "https://youtube.com");
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("Referer", "messenger.telegram.org");
                this.webView.loadUrl(tLRPC$WebPage.embed_url, hashMap);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.pipItem.setEnabled(false);
        this.pipItem.setAlpha(0.5f);
        this.progressBar.setVisibility(0);
        if (youTubeVideoId != null) {
            this.progressBarBlackBackground.setVisibility(0);
        }
        this.webView.setVisibility(0);
        this.webView.setKeepScreenOn(true);
        if (youTubeVideoId == null || !"disabled".equals(MessagesController.getInstance(this.currentAccount).youtubePipType)) {
            return;
        }
        this.pipItem.setVisibility(8);
    }

    public boolean checkInlinePermissions() {
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(getContext())) {
            return true;
        }
        AlertsCreator.createDrawOverlayPermissionDialog((Activity) getContext(), null);
        return false;
    }

    public void exitFromPip() {
        if (this.webView == null) {
            return;
        }
        if (ApplicationLoader.mainInterfacePaused) {
            try {
                getContext().startService(new Intent(ApplicationLoader.applicationContext, BringAppForegroundService.class));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        ViewGroup viewGroup = (ViewGroup) this.webView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this.webView);
        }
        addView(this.webView, 0, LayoutHelper.createFrame(-1, -1, 51));
        PipVideoOverlay.dismiss();
    }

    public void release() {
        this.webView.stopLoading();
        this.webView.loadUrl("about:blank");
        this.webView.destroy();
        AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
    }
}
