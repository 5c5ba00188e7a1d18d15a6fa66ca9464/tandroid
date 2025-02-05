package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.PhotoViewerWebView;
import org.telegram.ui.PhotoViewer;

/* loaded from: classes3.dex */
public abstract class PhotoViewerWebView extends FrameLayout {
    private float bufferedPosition;
    private int currentAccount;
    private int currentPosition;
    private TLRPC.WebPage currentWebpage;
    private String currentYoutubeId;
    private TextView errorButton;
    private LinearLayout errorLayout;
    private TextView errorMessage;
    private boolean isPlaying;
    private boolean isTouchDisabled;
    private boolean isYouTube;
    private PhotoViewer photoViewer;
    private View pipItem;
    private float playbackSpeed;
    private RadialProgressView progressBar;
    private View progressBarBlackBackground;
    private Runnable progressRunnable;
    private boolean setPlaybackSpeed;
    private int videoDuration;
    private WebView webView;
    private List youtubeStoryboards;
    private String youtubeStoryboardsSpecUrl;

    class 2 extends WebViewClient {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shouldInterceptRequest$0(String str, WebResourceRequest webResourceRequest) {
            Map requestHeaders;
            Map requestHeaders2;
            JSONObject optJSONObject;
            String optString;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setRequestMethod("POST");
                requestHeaders = webResourceRequest.getRequestHeaders();
                for (Map.Entry entry : requestHeaders.entrySet()) {
                    httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                JSONObject put = new JSONObject().put("userAgent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36,gzip(gfe)").put("clientName", "WEB");
                requestHeaders2 = webResourceRequest.getRequestHeaders();
                outputStream.write(jSONObject.put("context", jSONObject2.put("client", put.put("clientVersion", requestHeaders2.get("X-Youtube-Client-Version")).put("osName", "Windows").put("osVersion", "10.0").put("originalUrl", "https://www.youtube.com/watch?v=" + PhotoViewerWebView.this.currentYoutubeId).put("platform", "DESKTOP"))).put("videoId", PhotoViewerWebView.this.currentYoutubeId).toString().getBytes("UTF-8"));
                outputStream.close();
                InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
                byte[] bArr = new byte[10240];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    } else {
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                }
                byteArrayOutputStream.close();
                inputStream.close();
                JSONObject optJSONObject2 = new JSONObject(byteArrayOutputStream.toString("UTF-8")).optJSONObject("storyboards");
                if (optJSONObject2 == null || (optJSONObject = optJSONObject2.optJSONObject("playerStoryboardSpecRenderer")) == null || (optString = optJSONObject.optString("spec")) == null) {
                    return;
                }
                if (PhotoViewerWebView.this.videoDuration == 0) {
                    PhotoViewerWebView.this.youtubeStoryboardsSpecUrl = optString;
                } else {
                    PhotoViewerWebView.this.processYoutubeStoryboards(optString);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            if (PhotoViewerWebView.this.isYouTube) {
                return;
            }
            PhotoViewerWebView.this.progressBar.setVisibility(4);
            PhotoViewerWebView.this.progressBarBlackBackground.setVisibility(4);
            PhotoViewerWebView.this.pipItem.setEnabled(true);
            PhotoViewerWebView.this.pipItem.setAlpha(1.0f);
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, final WebResourceRequest webResourceRequest) {
            Uri url;
            url = webResourceRequest.getUrl();
            final String uri = url.toString();
            if (!PhotoViewerWebView.this.isYouTube || !uri.startsWith("https://www.youtube.com/youtubei/v1/player?key=")) {
                return null;
            }
            Utilities.externalNetworkQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.2.this.lambda$shouldInterceptRequest$0(uri, webResourceRequest);
                }
            });
            return null;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (!PhotoViewerWebView.this.isYouTube) {
                return super.shouldOverrideUrlLoading(webView, str);
            }
            Browser.openUrl(webView.getContext(), str);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class YoutubeProxy {
        private YoutubeProxy() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerError$1(View view) {
            view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(PhotoViewerWebView.this.currentWebpage.url)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerError$2(int i) {
            TextView textView;
            int i2;
            PhotoViewerWebView.this.errorButton.setVisibility(8);
            PhotoViewerWebView.this.webView.setVisibility(8);
            if (PhotoViewerWebView.this.errorLayout.getVisibility() == 8) {
                PhotoViewerWebView.this.errorLayout.setVisibility(0);
                PhotoViewerWebView.this.errorLayout.animate().cancel();
                PhotoViewerWebView.this.errorLayout.animate().alpha(1.0f).setDuration(150L).start();
            }
            if (PhotoViewerWebView.this.progressBar.getAlpha() == 1.0f) {
                PhotoViewerWebView.this.progressBar.animate().cancel();
                PhotoViewerWebView.this.progressBar.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PhotoViewerWebView.YoutubeProxy.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PhotoViewerWebView.this.progressBar.setVisibility(8);
                    }
                });
            }
            if (PhotoViewerWebView.this.progressBarBlackBackground.getAlpha() == 1.0f) {
                PhotoViewerWebView.this.progressBarBlackBackground.animate().cancel();
                PhotoViewerWebView.this.progressBarBlackBackground.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PhotoViewerWebView.YoutubeProxy.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PhotoViewerWebView.this.progressBarBlackBackground.setVisibility(8);
                    }
                });
            }
            if (i == 2) {
                textView = PhotoViewerWebView.this.errorMessage;
                i2 = R.string.YouTubeVideoErrorInvalid;
            } else {
                if (i != 5) {
                    if (i != 150) {
                        if (i == 100) {
                            textView = PhotoViewerWebView.this.errorMessage;
                            i2 = R.string.YouTubeVideoErrorNotFound;
                        } else if (i != 101) {
                            return;
                        }
                    }
                    PhotoViewerWebView.this.errorMessage.setText(LocaleController.getString(R.string.YouTubeVideoErrorNotAvailableInApp));
                    PhotoViewerWebView.this.errorButton.setText(LocaleController.getString(R.string.YouTubeVideoErrorOpenExternal));
                    PhotoViewerWebView.this.errorButton.setVisibility(0);
                    PhotoViewerWebView.this.errorButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerError$1(view);
                        }
                    });
                    return;
                }
                textView = PhotoViewerWebView.this.errorMessage;
                i2 = R.string.YouTubeVideoErrorHTML;
            }
            textView.setText(LocaleController.getString(i2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerLoaded$0() {
            PhotoViewerWebView.this.progressBar.setVisibility(4);
            if (PhotoViewerWebView.this.setPlaybackSpeed) {
                PhotoViewerWebView.this.setPlaybackSpeed = false;
                PhotoViewerWebView photoViewerWebView = PhotoViewerWebView.this;
                photoViewerWebView.setPlaybackSpeed(photoViewerWebView.playbackSpeed);
            }
            PhotoViewerWebView.this.pipItem.setEnabled(true);
            PhotoViewerWebView.this.pipItem.setAlpha(1.0f);
            if (PhotoViewerWebView.this.photoViewer != null) {
                PhotoViewerWebView.this.photoViewer.checkFullscreenButton();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerStateChange$3() {
            PhotoViewerWebView.this.progressBarBlackBackground.setVisibility(4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerStateChange$4(boolean z, int i) {
            PhotoViewerWebView.this.photoViewer.updateWebPlayerState(z, i);
        }

        @JavascriptInterface
        public void onPlayerError(String str) {
            final int parseInt = Integer.parseInt(str);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerError$2(parseInt);
                }
            });
        }

        @JavascriptInterface
        public void onPlayerLoaded() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerLoaded$0();
                }
            });
        }

        @JavascriptInterface
        public void onPlayerNotifyBufferedPosition(float f) {
            PhotoViewerWebView.this.bufferedPosition = f;
        }

        @JavascriptInterface
        public void onPlayerNotifyCurrentPosition(int i) {
            PhotoViewerWebView.this.currentPosition = i * MediaDataController.MAX_STYLE_RUNS_COUNT;
        }

        @JavascriptInterface
        public void onPlayerNotifyDuration(int i) {
            PhotoViewerWebView.this.videoDuration = i * MediaDataController.MAX_STYLE_RUNS_COUNT;
            if (PhotoViewerWebView.this.youtubeStoryboardsSpecUrl != null) {
                PhotoViewerWebView photoViewerWebView = PhotoViewerWebView.this;
                photoViewerWebView.processYoutubeStoryboards(photoViewerWebView.youtubeStoryboardsSpecUrl);
                PhotoViewerWebView.this.youtubeStoryboardsSpecUrl = null;
            }
        }

        @JavascriptInterface
        public void onPlayerStateChange(String str) {
            int parseInt = Integer.parseInt(str);
            boolean z = PhotoViewerWebView.this.isPlaying;
            final boolean z2 = false;
            final int i = 1;
            PhotoViewerWebView.this.isPlaying = parseInt == 1 || parseInt == 3;
            PhotoViewerWebView.this.checkPlayingPoll(z);
            if (parseInt != 0) {
                if (parseInt == 1) {
                    z2 = true;
                } else if (parseInt != 2) {
                    if (parseInt == 3) {
                        z2 = true;
                        i = 2;
                    }
                }
                i = 3;
            } else {
                i = 4;
            }
            if (i == 3 && PhotoViewerWebView.this.progressBarBlackBackground.getVisibility() != 4) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerStateChange$3();
                    }
                }, 300L);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$YoutubeProxy$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.YoutubeProxy.this.lambda$onPlayerStateChange$4(z2, i);
                }
            });
        }
    }

    public PhotoViewerWebView(PhotoViewer photoViewer, final Context context, View view) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.youtubeStoryboards = new ArrayList();
        this.progressRunnable = new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoViewerWebView.this.lambda$new$0();
            }
        };
        this.photoViewer = photoViewer;
        this.pipItem = view;
        WebView webView = new WebView(context) { // from class: org.telegram.ui.Components.PhotoViewerWebView.1
            @Override // android.view.View
            public void draw(Canvas canvas) {
                super.draw(canvas);
                if (PipVideoOverlay.getInnerView() == this && PhotoViewerWebView.this.progressBarBlackBackground.getVisibility() == 0) {
                    canvas.drawColor(-16777216);
                    PhotoViewerWebView.this.drawBlackBackground(canvas, getWidth(), getHeight());
                }
            }

            @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                AndroidUtilities.checkAndroidTheme(context, true);
                super.onAttachedToWindow();
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                AndroidUtilities.checkAndroidTheme(context, false);
                super.onDetachedFromWindow();
            }

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
        this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        if (i >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
        }
        this.webView.setWebViewClient(new 2());
        addView(this.webView, LayoutHelper.createFrame(-1, -1, 51));
        LinearLayout linearLayout = new LinearLayout(context);
        this.errorLayout = linearLayout;
        linearLayout.setOrientation(1);
        this.errorLayout.setGravity(17);
        this.errorLayout.setVisibility(8);
        addView(this.errorLayout, LayoutHelper.createFrame(-2, -2, 17));
        TextView textView = new TextView(context);
        this.errorMessage = textView;
        textView.setTextSize(1, 16.0f);
        this.errorMessage.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        this.errorMessage.setGravity(17);
        this.errorLayout.addView(this.errorMessage, LayoutHelper.createLinear(-2, -2, 1));
        TextView textView2 = new TextView(context);
        this.errorButton = textView2;
        textView2.setTextSize(1, 16.0f);
        TextView textView3 = this.errorButton;
        int i2 = Theme.key_windowBackgroundWhiteBlueText;
        textView3.setTextColor(Theme.getColor(i2));
        this.errorButton.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f));
        this.errorButton.setBackground(Theme.AdaptiveRipple.rectByKey(i2, 12.0f));
        this.errorButton.setVisibility(8);
        this.errorLayout.addView(this.errorButton, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
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

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPlayingPoll(boolean z) {
        if (!z && this.isPlaying) {
            AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
        } else {
            if (!z || this.isPlaying) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
        }
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
    public /* synthetic */ void lambda$seekTo$1(long j, boolean z) {
        runJsCode("seekTo(" + Math.round(j / 1000.0f) + ", " + z + ");");
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PhotoViewerWebView.this.playVideo();
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processYoutubeStoryboards(String str) {
        float f;
        float f2;
        float f3;
        int videoDuration = getVideoDuration() / MediaDataController.MAX_STYLE_RUNS_COUNT;
        this.youtubeStoryboards.clear();
        if (videoDuration <= 15) {
            return;
        }
        String[] split = str.split("\\|");
        String str2 = split[0].split("\\$")[0] + "2/";
        String str3 = split[0].split("\\$N")[1];
        String str4 = split.length == 3 ? split[2].split("M#")[1] : split.length == 2 ? split[1].split("t#")[1] : split[3].split("M#")[1];
        if (videoDuration <= 100) {
            f3 = videoDuration;
        } else {
            if (videoDuration <= 250) {
                f = videoDuration;
                f2 = 2.0f;
            } else if (videoDuration <= 500) {
                f = videoDuration;
                f2 = 4.0f;
            } else if (videoDuration <= 1000) {
                f = videoDuration;
                f2 = 5.0f;
            } else {
                f = videoDuration;
                f2 = 10.0f;
            }
            f3 = f / f2;
        }
        int ceil = (int) Math.ceil(f3 / 25.0f);
        for (int i = 0; i < ceil; i++) {
            this.youtubeStoryboards.add(String.format(Locale.ROOT, "%sM%d%s&sigh=%s", str2, Integer.valueOf(i), str3, str4));
        }
    }

    private void runJsCode(String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.webView.evaluateJavascript(str, null);
            return;
        }
        try {
            this.webView.loadUrl("javascript:" + str);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean checkInlinePermissions() {
        boolean canDrawOverlays;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        canDrawOverlays = Settings.canDrawOverlays(getContext());
        if (canDrawOverlays) {
            return true;
        }
        AlertsCreator.createDrawOverlayPermissionDialog((Activity) getContext(), null);
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.isTouchDisabled) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    protected abstract void drawBlackBackground(Canvas canvas, int i, int i2);

    public void exitFromPip() {
        if (this.webView == null) {
            return;
        }
        if (ApplicationLoader.mainInterfacePaused) {
            try {
                getContext().startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) BringAppForegroundService.class));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        this.progressBarBlackBackground.setVisibility(0);
        ViewGroup viewGroup = (ViewGroup) this.webView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this.webView);
        }
        addView(this.webView, 0, LayoutHelper.createFrame(-1, -1, 51));
        PipVideoOverlay.dismiss();
    }

    public float getBufferedPosition() {
        return this.bufferedPosition;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public int getVideoDuration() {
        return this.videoDuration;
    }

    public WebView getWebView() {
        return this.webView;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003f A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getYoutubeStoryboard(int i) {
        float f;
        float f2;
        float f3;
        int i2;
        int videoDuration = getVideoDuration() / MediaDataController.MAX_STYLE_RUNS_COUNT;
        if (videoDuration > 100) {
            if (videoDuration <= 250) {
                f = i;
                f3 = 2.0f;
            } else {
                f = i;
                if (videoDuration <= 500) {
                    f3 = 4.0f;
                } else if (videoDuration <= 1000) {
                    f3 = 5.0f;
                } else {
                    f2 = f / 10.0f;
                }
            }
            i2 = ((int) (f / f3)) / 25;
            if (i2 >= this.youtubeStoryboards.size()) {
                return (String) this.youtubeStoryboards.get(i2);
            }
            return null;
        }
        f2 = i;
        i2 = (int) (f2 / 25.0f);
        if (i2 >= this.youtubeStoryboards.size()) {
        }
    }

    public int getYoutubeStoryboardImageCount(int i) {
        float f;
        float f2;
        double d;
        int indexOf = this.youtubeStoryboards.indexOf(getYoutubeStoryboard(i));
        if (indexOf == -1) {
            return 0;
        }
        if (indexOf != this.youtubeStoryboards.size() - 1) {
            return 25;
        }
        int videoDuration = getVideoDuration() / MediaDataController.MAX_STYLE_RUNS_COUNT;
        if (videoDuration <= 100) {
            d = videoDuration;
        } else {
            if (videoDuration <= 250) {
                f = videoDuration;
                f2 = 2.0f;
            } else if (videoDuration <= 500) {
                f = videoDuration;
                f2 = 4.0f;
            } else if (videoDuration <= 1000) {
                f = videoDuration;
                f2 = 5.0f;
            } else {
                f = videoDuration;
                f2 = 10.0f;
            }
            d = f / f2;
        }
        return Math.min(25, (((int) Math.ceil(d)) - ((this.youtubeStoryboards.size() - 1) * 25)) + 1);
    }

    public int getYoutubeStoryboardImageIndex(int i) {
        float f;
        float f2;
        double d;
        int videoDuration = getVideoDuration() / MediaDataController.MAX_STYLE_RUNS_COUNT;
        if (videoDuration <= 100) {
            d = i;
        } else {
            if (videoDuration <= 250) {
                f = i;
                f2 = 2.0f;
            } else {
                f = i;
                f2 = videoDuration <= 500 ? 4.0f : videoDuration <= 1000 ? 5.0f : 10.0f;
            }
            d = f / f2;
        }
        return ((int) Math.ceil(d)) % 25;
    }

    public boolean hasYoutubeStoryboards() {
        return !this.youtubeStoryboards.isEmpty();
    }

    public void hideControls() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00ac A[Catch: Exception -> 0x008a, LOOP:0: B:9:0x00a5->B:11:0x00ac, LOOP_END, TryCatch #0 {Exception -> 0x008a, blocks: (B:3:0x0013, B:5:0x0017, B:8:0x008d, B:9:0x00a5, B:11:0x00ac, B:13:0x00b0, B:41:0x0086, B:42:0x00df, B:27:0x002d, B:29:0x0033, B:31:0x0049, B:33:0x0052, B:35:0x005b, B:37:0x0061, B:38:0x007d), top: B:2:0x0013, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x00b0 A[EDGE_INSN: B:12:0x00b0->B:13:0x00b0 BREAK  A[LOOP:0: B:9:0x00a5->B:11:0x00ac], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void init(int i, TLRPC.WebPage webPage) {
        int intValue;
        InputStream open;
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] bArr;
        int read;
        this.currentWebpage = webPage;
        this.currentYoutubeId = WebPlayerView.getYouTubeVideoId(webPage.embed_url);
        String str = webPage.url;
        requestLayout();
        try {
            if (this.currentYoutubeId != null) {
                this.progressBarBlackBackground.setVisibility(0);
                this.isYouTube = true;
                String str2 = null;
                this.webView.addJavascriptInterface(new YoutubeProxy(), "YoutubeProxy");
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
                            } else {
                                byteArrayOutputStream.write(bArr, 0, read);
                            }
                        }
                        byteArrayOutputStream.close();
                        open.close();
                        this.webView.loadDataWithBaseURL("https://messenger.telegram.org/", String.format(Locale.US, byteArrayOutputStream.toString("UTF-8"), this.currentYoutubeId, Integer.valueOf(intValue)), "text/html", "UTF-8", "https://youtube.com");
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
                this.webView.loadDataWithBaseURL("https://messenger.telegram.org/", String.format(Locale.US, byteArrayOutputStream.toString("UTF-8"), this.currentYoutubeId, Integer.valueOf(intValue)), "text/html", "UTF-8", "https://youtube.com");
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("Referer", "messenger.telegram.org");
                this.webView.loadUrl(webPage.embed_url, hashMap);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.pipItem.setEnabled(false);
        this.pipItem.setAlpha(0.5f);
        this.progressBar.setVisibility(0);
        if (this.currentYoutubeId != null) {
            this.progressBarBlackBackground.setVisibility(0);
        }
        this.webView.setVisibility(0);
        this.webView.setKeepScreenOn(true);
        if (this.currentYoutubeId == null || !"disabled".equals(MessagesController.getInstance(this.currentAccount).youtubePipType)) {
            return;
        }
        this.pipItem.setVisibility(8);
    }

    public boolean isControllable() {
        return isYouTube();
    }

    public boolean isInAppOnly() {
        return this.isYouTube && "inapp".equals(MessagesController.getInstance(this.currentAccount).youtubePipType);
    }

    public boolean isLoaded() {
        return this.progressBar.getVisibility() != 0;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public boolean isYouTube() {
        return this.isYouTube;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.webView.getParent() == this) {
            TLRPC.WebPage webPage = this.currentWebpage;
            int i3 = webPage.embed_width;
            if (i3 == 0) {
                i3 = 100;
            }
            int i4 = webPage.embed_height;
            int i5 = i4 != 0 ? i4 : 100;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            float f = i3;
            float f2 = i5;
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

    public boolean openInPip() {
        boolean isInAppOnly = isInAppOnly();
        if ((!isInAppOnly && !checkInlinePermissions()) || this.progressBar.getVisibility() == 0) {
            return false;
        }
        if (PipVideoOverlay.isVisible()) {
            PipVideoOverlay.dismiss();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.this.openInPip();
                }
            }, 300L);
            return true;
        }
        this.progressBarBlackBackground.setVisibility(0);
        Activity activity = (Activity) getContext();
        WebView webView = this.webView;
        TLRPC.WebPage webPage = this.currentWebpage;
        if (PipVideoOverlay.show(isInAppOnly, activity, this, webView, webPage.embed_width, webPage.embed_height, false)) {
            PipVideoOverlay.setPhotoViewer(PhotoViewer.getInstance());
        }
        return true;
    }

    public void pauseVideo() {
        if (this.isPlaying && isControllable()) {
            runJsCode("pauseVideo();");
            this.isPlaying = false;
            checkPlayingPoll(true);
        }
    }

    public void playVideo() {
        if (this.isPlaying || !isControllable()) {
            return;
        }
        runJsCode("playVideo();");
        this.isPlaying = true;
        checkPlayingPoll(false);
    }

    protected abstract void processTouch(MotionEvent motionEvent);

    public void release() {
        this.webView.stopLoading();
        this.webView.loadUrl("about:blank");
        this.webView.destroy();
        this.videoDuration = 0;
        this.currentPosition = 0;
        AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerWebView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerWebView.this.lambda$seekTo$1(j, z);
                }
            }, 100L);
            return;
        }
        runJsCode("seekTo(" + Math.round(j / 1000.0f) + ", " + z + ");");
    }

    public void setPlaybackSpeed(float f) {
        this.playbackSpeed = f;
        if (this.progressBar.getVisibility() == 0) {
            this.setPlaybackSpeed = true;
            return;
        }
        if (this.isYouTube) {
            runJsCode("setPlaybackSpeed(" + f + ");");
        }
    }

    public void setTouchDisabled(boolean z) {
        this.isTouchDisabled = z;
    }

    public void showControls() {
    }
}
