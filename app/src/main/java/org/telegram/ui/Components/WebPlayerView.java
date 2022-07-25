package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.huawei.hms.framework.common.ContainerUtils;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.WebPlayerView;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class WebPlayerView extends ViewGroup implements VideoPlayer.VideoPlayerDelegate, AudioManager.OnAudioFocusChangeListener {
    private static int lastContainerId = 4001;
    private boolean allowInlineAnimation;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private Paint backgroundPaint;
    private TextureView changedTextureView;
    private boolean changingTextureView;
    private ControlsView controlsView;
    private float currentAlpha;
    private Bitmap currentBitmap;
    private AsyncTask currentTask;
    private String currentYoutubeId;
    private WebPlayerViewDelegate delegate;
    private boolean drawImage;
    private boolean firstFrameRendered;
    private ImageView fullscreenButton;
    private boolean hasAudioFocus;
    private boolean inFullscreen;
    private boolean initied;
    private ImageView inlineButton;
    private String interfaceName;
    private boolean isAutoplay;
    private boolean isCompleted;
    private boolean isInline;
    private boolean isStream;
    private long lastUpdateTime;
    private String playAudioType;
    private String playAudioUrl;
    private ImageView playButton;
    private String playVideoType;
    private String playVideoUrl;
    private AnimatorSet progressAnimation;
    private Runnable progressRunnable;
    private RadialProgressView progressView;
    private boolean resumeAudioOnFocusGain;
    private int seekToTime;
    private ImageView shareButton;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private Runnable switchToInlineRunnable;
    private boolean switchingInlineMode;
    private ImageView textureImageView;
    private TextureView textureView;
    private ViewGroup textureViewContainer;
    private int videoHeight;
    private VideoPlayer videoPlayer;
    private int videoWidth;
    private int waitingForFirstTextureUpload;
    private WebView webView;
    private static final Pattern youtubeIdRegex = Pattern.compile("(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
    private static final Pattern vimeoIdRegex = Pattern.compile("https?://(?:(?:www|(player))\\.)?vimeo(pro)?\\.com/(?!(?:channels|album)/[^/?#]+/?(?:$|[?#])|[^/]+/review/|ondemand/)(?:.*?/)?(?:(?:play_redirect_hls|moogaloop\\.swf)\\?clip_id=)?(?:videos?/)?([0-9]+)(?:/[\\da-f]+)?/?(?:[?&].*)?(?:[#].*)?$");
    private static final Pattern coubIdRegex = Pattern.compile("(?:coub:|https?://(?:coub\\.com/(?:view|embed|coubs)/|c-cdn\\.coub\\.com/fb-player\\.swf\\?.*\\bcoub(?:ID|id)=))([\\da-z]+)");
    private static final Pattern aparatIdRegex = Pattern.compile("^https?://(?:www\\.)?aparat\\.com/(?:v/|video/video/embed/videohash/)([a-zA-Z0-9]+)");
    private static final Pattern twitchClipIdRegex = Pattern.compile("https?://clips\\.twitch\\.tv/(?:[^/]+/)*([^/?#&]+)");
    private static final Pattern twitchStreamIdRegex = Pattern.compile("https?://(?:(?:www\\.)?twitch\\.tv/|player\\.twitch\\.tv/\\?.*?\\bchannel=)([^/#?]+)");
    private static final Pattern aparatFileListPattern = Pattern.compile("fileList\\s*=\\s*JSON\\.parse\\('([^']+)'\\)");
    private static final Pattern twitchClipFilePattern = Pattern.compile("clipInfo\\s*=\\s*(\\{[^']+\\});");
    private static final Pattern stsPattern = Pattern.compile("\"sts\"\\s*:\\s*(\\d+)");
    private static final Pattern jsPattern = Pattern.compile("\"assets\":.+?\"js\":\\s*(\"[^\"]+\")");
    private static final Pattern sigPattern = Pattern.compile("\\.sig\\|\\|([a-zA-Z0-9$]+)\\(");
    private static final Pattern sigPattern2 = Pattern.compile("[\"']signature[\"']\\s*,\\s*([a-zA-Z0-9$]+)\\(");
    private static final Pattern stmtVarPattern = Pattern.compile("var\\s");
    private static final Pattern stmtReturnPattern = Pattern.compile("return(?:\\s+|$)");
    private static final Pattern exprParensPattern = Pattern.compile("[()]");
    private static final Pattern playerIdPattern = Pattern.compile(".*?-([a-zA-Z0-9_-]+)(?:/watch_as3|/html5player(?:-new)?|(?:/[a-z]{2}_[A-Z]{2})?/base)?\\.([a-z]+)$");

    /* loaded from: classes3.dex */
    public interface CallJavaResultInterface {
        void jsCallFinished(String str);
    }

    /* loaded from: classes3.dex */
    public interface WebPlayerViewDelegate {
        boolean checkInlinePermissions();

        ViewGroup getTextureViewContainer();

        void onInitFailed();

        void onInlineSurfaceTextureReady();

        void onPlayStateChanged(WebPlayerView webPlayerView, boolean z);

        void onSharePressed();

        TextureView onSwitchInlineMode(View view, boolean z, int i, int i2, int i3, boolean z2);

        TextureView onSwitchToFullscreen(View view, boolean z, float f, int i, boolean z2);

        void onVideoSizeChanged(float f, int i);

        void prepareToSwitchInlineMode(boolean z, Runnable runnable, float f, boolean z2);
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
        VideoPlayer.VideoPlayerDelegate.CC.$default$onRenderedFirstFrame(this, eventTime);
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
        VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekFinished(this, eventTime);
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
        VideoPlayer.VideoPlayerDelegate.CC.$default$onSeekStarted(this, eventTime);
    }

    static /* synthetic */ float access$4724(WebPlayerView webPlayerView, float f) {
        float f2 = webPlayerView.currentAlpha - f;
        webPlayerView.currentAlpha = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class JSExtractor {
        private String jsCode;
        ArrayList<String> codeLines = new ArrayList<>();
        private String[] operators = {HiAnalyticsConstant.REPORT_VAL_SEPARATOR, "^", ContainerUtils.FIELD_DELIMITER, ">>", "<<", "-", "+", "%", "/", "*"};
        private String[] assign_operators = {"|=", "^=", "&=", ">>=", "<<=", "-=", "+=", "%=", "/=", "*=", ContainerUtils.KEY_VALUE_DELIMITER};

        public JSExtractor(String str) {
            this.jsCode = str;
        }

        private void interpretExpression(String str, HashMap<String, String> hashMap, int i) throws Exception {
            String trim = str.trim();
            if (TextUtils.isEmpty(trim)) {
                return;
            }
            if (trim.charAt(0) == '(') {
                Matcher matcher = WebPlayerView.exprParensPattern.matcher(trim);
                int i2 = 0;
                while (true) {
                    if (!matcher.find()) {
                        break;
                    } else if (matcher.group(0).indexOf(48) == 40) {
                        i2++;
                    } else {
                        i2--;
                        if (i2 == 0) {
                            interpretExpression(trim.substring(1, matcher.start()), hashMap, i);
                            trim = trim.substring(matcher.end()).trim();
                            if (TextUtils.isEmpty(trim)) {
                                return;
                            }
                        }
                    }
                }
                if (i2 != 0) {
                    throw new Exception(String.format("Premature end of parens in %s", trim));
                }
            }
            int i3 = 0;
            while (true) {
                String[] strArr = this.assign_operators;
                if (i3 < strArr.length) {
                    Matcher matcher2 = Pattern.compile(String.format(Locale.US, "(?x)(%s)(?:\\[([^\\]]+?)\\])?\\s*%s(.*)$", "[a-zA-Z_$][a-zA-Z_$0-9]*", Pattern.quote(strArr[i3]))).matcher(trim);
                    if (matcher2.find()) {
                        interpretExpression(matcher2.group(3), hashMap, i - 1);
                        String group = matcher2.group(2);
                        if (!TextUtils.isEmpty(group)) {
                            interpretExpression(group, hashMap, i);
                            return;
                        } else {
                            hashMap.put(matcher2.group(1), "");
                            return;
                        }
                    }
                    i3++;
                } else {
                    try {
                        Integer.parseInt(trim);
                        return;
                    } catch (Exception unused) {
                        if (Pattern.compile(String.format(Locale.US, "(?!if|return|true|false)(%s)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(trim).find()) {
                            return;
                        }
                        if (trim.charAt(0) == '\"' && trim.charAt(trim.length() - 1) == '\"') {
                            return;
                        }
                        try {
                            new JSONObject(trim).toString();
                            return;
                        } catch (Exception unused2) {
                            Locale locale = Locale.US;
                            Matcher matcher3 = Pattern.compile(String.format(locale, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(trim);
                            if (matcher3.find()) {
                                matcher3.group(1);
                                interpretExpression(matcher3.group(2), hashMap, i - 1);
                                return;
                            }
                            Matcher matcher4 = Pattern.compile(String.format(locale, "(%s)(?:\\.([^(]+)|\\[([^]]+)\\])\\s*(?:\\(+([^()]*)\\))?$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(trim);
                            if (matcher4.find()) {
                                String group2 = matcher4.group(1);
                                String group3 = matcher4.group(2);
                                String group4 = matcher4.group(3);
                                if (TextUtils.isEmpty(group3)) {
                                    group3 = group4;
                                }
                                group3.replace("\"", "");
                                String group5 = matcher4.group(4);
                                if (hashMap.get(group2) == null) {
                                    extractObject(group2);
                                }
                                if (group5 == null) {
                                    return;
                                }
                                if (trim.charAt(trim.length() - 1) != ')') {
                                    throw new Exception("last char not ')'");
                                }
                                if (group5.length() == 0) {
                                    return;
                                }
                                for (String str2 : group5.split(",")) {
                                    interpretExpression(str2, hashMap, i);
                                }
                                return;
                            }
                            Matcher matcher5 = Pattern.compile(String.format(locale, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(trim);
                            if (matcher5.find()) {
                                hashMap.get(matcher5.group(1));
                                interpretExpression(matcher5.group(2), hashMap, i - 1);
                                return;
                            }
                            int i4 = 0;
                            while (true) {
                                String[] strArr2 = this.operators;
                                if (i4 < strArr2.length) {
                                    String str3 = strArr2[i4];
                                    Matcher matcher6 = Pattern.compile(String.format(Locale.US, "(.+?)%s(.+)", Pattern.quote(str3))).matcher(trim);
                                    if (matcher6.find()) {
                                        boolean[] zArr = new boolean[1];
                                        int i5 = i - 1;
                                        interpretStatement(matcher6.group(1), hashMap, zArr, i5);
                                        if (zArr[0]) {
                                            throw new Exception(String.format("Premature left-side return of %s in %s", str3, trim));
                                        }
                                        interpretStatement(matcher6.group(2), hashMap, zArr, i5);
                                        if (zArr[0]) {
                                            throw new Exception(String.format("Premature right-side return of %s in %s", str3, trim));
                                        }
                                    }
                                    i4++;
                                } else {
                                    Matcher matcher7 = Pattern.compile(String.format(Locale.US, "^(%s)\\(([a-zA-Z0-9_$,]*)\\)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(trim);
                                    if (matcher7.find()) {
                                        extractFunction(matcher7.group(1));
                                    }
                                    throw new Exception(String.format("Unsupported JS expression %s", trim));
                                }
                            }
                        }
                    }
                }
            }
        }

        private void interpretStatement(String str, HashMap<String, String> hashMap, boolean[] zArr, int i) throws Exception {
            if (i < 0) {
                throw new Exception("recursion limit reached");
            }
            zArr[0] = false;
            String trim = str.trim();
            Matcher matcher = WebPlayerView.stmtVarPattern.matcher(trim);
            if (!matcher.find()) {
                Matcher matcher2 = WebPlayerView.stmtReturnPattern.matcher(trim);
                if (matcher2.find()) {
                    trim = trim.substring(matcher2.group(0).length());
                    zArr[0] = true;
                }
            } else {
                trim = trim.substring(matcher.group(0).length());
            }
            interpretExpression(trim, hashMap, i);
        }

        private HashMap<String, Object> extractObject(String str) throws Exception {
            HashMap<String, Object> hashMap = new HashMap<>();
            Matcher matcher = Pattern.compile(String.format(Locale.US, "(?:var\\s+)?%s\\s*=\\s*\\{\\s*((%s\\s*:\\s*function\\(.*?\\)\\s*\\{.*?\\}(?:,\\s*)?)*)\\}\\s*;", Pattern.quote(str), "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(this.jsCode);
            String str2 = null;
            while (true) {
                if (!matcher.find()) {
                    break;
                }
                String group = matcher.group();
                String group2 = matcher.group(2);
                if (TextUtils.isEmpty(group2)) {
                    str2 = group2;
                } else {
                    if (!this.codeLines.contains(group)) {
                        this.codeLines.add(matcher.group());
                    }
                    str2 = group2;
                }
            }
            Matcher matcher2 = Pattern.compile(String.format("(%s)\\s*:\\s*function\\(([a-z,]+)\\)\\{([^}]+)\\}", "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(str2);
            while (matcher2.find()) {
                buildFunction(matcher2.group(2).split(","), matcher2.group(3));
            }
            return hashMap;
        }

        private void buildFunction(String[] strArr, String str) throws Exception {
            HashMap<String, String> hashMap = new HashMap<>();
            for (String str2 : strArr) {
                hashMap.put(str2, "");
            }
            String[] split = str.split(";");
            boolean[] zArr = new boolean[1];
            for (String str3 : split) {
                interpretStatement(str3, hashMap, zArr, 100);
                if (zArr[0]) {
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String extractFunction(String str) {
            try {
                String quote = Pattern.quote(str);
                Matcher matcher = Pattern.compile(String.format(Locale.US, "(?x)(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\(([^)]*)\\)\\s*\\{([^}]+)\\}", quote, quote, quote)).matcher(this.jsCode);
                if (matcher.find()) {
                    String group = matcher.group();
                    if (!this.codeLines.contains(group)) {
                        ArrayList<String> arrayList = this.codeLines;
                        arrayList.add(group + ";");
                    }
                    buildFunction(matcher.group(1).split(","), matcher.group(2));
                }
            } catch (Exception e) {
                this.codeLines.clear();
                FileLog.e(e);
            }
            return TextUtils.join("", this.codeLines);
        }
    }

    /* loaded from: classes3.dex */
    public static class JavaScriptInterface {
        private final CallJavaResultInterface callJavaResultInterface;

        public JavaScriptInterface(CallJavaResultInterface callJavaResultInterface) {
            this.callJavaResultInterface = callJavaResultInterface;
        }

        @JavascriptInterface
        public void returnResultToJava(String str) {
            this.callJavaResultInterface.jsCallFinished(str);
        }
    }

    protected String downloadUrlContent(AsyncTask asyncTask, String str) {
        return downloadUrlContent(asyncTask, str, null, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x0168, code lost:
        if (r3 == (-1)) goto L81;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x019a A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0129 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0186 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected String downloadUrlContent(AsyncTask asyncTask, String str, HashMap<String, String> hashMap, boolean z) {
        HttpURLConnection httpURLConnection;
        boolean z2;
        InputStream inputStream;
        StringBuilder sb;
        boolean z3;
        InputStream inputStream2;
        boolean z4 = true;
        try {
            URL url = new URL(str);
            httpURLConnection = url.openConnection();
            try {
                httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)");
                if (z) {
                    httpURLConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
                }
                httpURLConnection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
                httpURLConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                httpURLConnection.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
                if (hashMap != null) {
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                if (httpURLConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) httpURLConnection;
                    httpURLConnection2.setInstanceFollowRedirects(true);
                    int responseCode = httpURLConnection2.getResponseCode();
                    if (responseCode == 302 || responseCode == 301 || responseCode == 303) {
                        String headerField = httpURLConnection2.getHeaderField("Location");
                        String headerField2 = httpURLConnection2.getHeaderField("Set-Cookie");
                        url = new URL(headerField);
                        httpURLConnection = url.openConnection();
                        httpURLConnection.setRequestProperty("Cookie", headerField2);
                        httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)");
                        if (z) {
                            httpURLConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
                        }
                        httpURLConnection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
                        httpURLConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                        httpURLConnection.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
                        if (hashMap != null) {
                            for (Map.Entry<String, String> entry2 : hashMap.entrySet()) {
                                httpURLConnection.addRequestProperty(entry2.getKey(), entry2.getValue());
                            }
                        }
                    }
                }
                httpURLConnection.connect();
                if (z) {
                    try {
                        inputStream2 = new GZIPInputStream(httpURLConnection.getInputStream());
                    } catch (Exception unused) {
                        httpURLConnection = url.openConnection();
                        httpURLConnection.connect();
                        inputStream2 = httpURLConnection.getInputStream();
                    }
                } else {
                    inputStream2 = httpURLConnection.getInputStream();
                }
                inputStream = inputStream2;
                z2 = true;
            } catch (Throwable th) {
                th = th;
                boolean z5 = !(th instanceof SocketTimeoutException) ? !(th instanceof UnknownHostException) && (!(th instanceof SocketException) ? !(th instanceof FileNotFoundException) : th.getMessage() == null || !th.getMessage().contains("ECONNRESET")) : !ApplicationLoader.isNetworkOnline();
                FileLog.e(th);
                z2 = z5;
                inputStream = null;
                if (!z2) {
                }
                if (!z3) {
                }
            }
        } catch (Throwable th2) {
            th = th2;
            httpURLConnection = null;
        }
        if (!z2) {
            try {
                if (httpURLConnection instanceof HttpURLConnection) {
                    httpURLConnection.getResponseCode();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (inputStream != null) {
                try {
                    byte[] bArr = new byte[32768];
                    sb = null;
                    while (true) {
                        try {
                            if (asyncTask.isCancelled()) {
                                break;
                            }
                            try {
                                int read = inputStream.read(bArr);
                                if (read > 0) {
                                    if (sb == null) {
                                        sb = new StringBuilder();
                                    }
                                    try {
                                        try {
                                            sb.append(new String(bArr, 0, read, "UTF-8"));
                                        } catch (Throwable th3) {
                                            th = th3;
                                            FileLog.e(th);
                                            z3 = false;
                                            if (inputStream != null) {
                                            }
                                            if (!z3) {
                                            }
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        FileLog.e(e);
                                        z4 = false;
                                        z3 = z4;
                                        if (inputStream != null) {
                                        }
                                        if (!z3) {
                                        }
                                    }
                                }
                            } catch (Exception e3) {
                                e = e3;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                    z3 = z4;
                } catch (Throwable th5) {
                    th = th5;
                    sb = null;
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th6) {
                        FileLog.e(th6);
                    }
                }
            } else {
                sb = null;
            }
            z3 = false;
            if (inputStream != null) {
            }
        } else {
            sb = null;
            z3 = false;
        }
        if (!z3) {
            return sb.toString();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class YoutubeVideoTask extends AsyncTask<Void, Void, String[]> {
        private CountDownLatch countDownLatch = new CountDownLatch(1);
        private String[] result = new String[2];
        private String sig;
        private String videoId;

        public YoutubeVideoTask(String str) {
            this.videoId = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:144:0x02eb  */
        /* JADX WARN: Removed duplicated region for block: B:165:0x03e8  */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String[] doInBackground(Void... voidArr) {
            String[] strArr;
            boolean z;
            String str;
            Matcher matcher;
            String str2;
            String str3;
            String str4;
            final String str5;
            String str6;
            String str7;
            boolean z2;
            boolean z3;
            String str8;
            Matcher matcher2;
            String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, "https://www.youtube.com/embed/" + this.videoId);
            String[] strArr2 = null;
            if (isCancelled()) {
                return null;
            }
            String str9 = "video_id=" + this.videoId + "&ps=default&gl=US&hl=en";
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str9);
                sb.append("&eurl=");
                sb.append(URLEncoder.encode("https://youtube.googleapis.com/v/" + this.videoId, "UTF-8"));
                str9 = sb.toString();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (downloadUrlContent != null) {
                if (WebPlayerView.stsPattern.matcher(downloadUrlContent).find()) {
                    str9 = str9 + "&sts=" + downloadUrlContent.substring(matcher2.start() + 6, matcher2.end());
                } else {
                    str9 = str9 + "&sts=";
                }
            }
            char c = 1;
            this.result[1] = "dash";
            int i = 5;
            char c2 = 0;
            int i2 = 2;
            String[] strArr3 = {"", "&el=leanback", "&el=embedded", "&el=detailpage", "&el=vevo"};
            String str10 = null;
            boolean z4 = false;
            int i3 = 0;
            while (i3 < i) {
                String downloadUrlContent2 = WebPlayerView.this.downloadUrlContent(this, "https://www.youtube.com/get_video_info?" + str9 + strArr3[i3]);
                if (isCancelled()) {
                    return strArr2;
                }
                if (downloadUrlContent2 != null) {
                    String[] split = downloadUrlContent2.split(ContainerUtils.FIELD_DELIMITER);
                    boolean z5 = z4;
                    Object obj = strArr2;
                    int i4 = 0;
                    z2 = false;
                    z3 = false;
                    while (i4 < split.length) {
                        if (split[i4].startsWith("dashmpd")) {
                            String[] split2 = split[i4].split(ContainerUtils.KEY_VALUE_DELIMITER);
                            if (split2.length == i2) {
                                try {
                                    this.result[c2] = URLDecoder.decode(split2[c], "UTF-8");
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                            }
                            str8 = str9;
                            z3 = true;
                        } else if (split[i4].startsWith("url_encoded_fmt_stream_map")) {
                            String[] split3 = split[i4].split(ContainerUtils.KEY_VALUE_DELIMITER);
                            if (split3.length == i2) {
                                try {
                                    String[] split4 = URLDecoder.decode(split3[c], "UTF-8").split("[&,]");
                                    String str11 = null;
                                    int i5 = 0;
                                    boolean z6 = false;
                                    while (i5 < split4.length) {
                                        String[] split5 = split4[i5].split(ContainerUtils.KEY_VALUE_DELIMITER);
                                        String[] strArr4 = split4;
                                        str8 = str9;
                                        try {
                                            if (split5[0].startsWith("type")) {
                                                if (URLDecoder.decode(split5[1], "UTF-8").contains("video/mp4")) {
                                                    z6 = true;
                                                }
                                            } else if (split5[0].startsWith(RemoteMessageConst.Notification.URL)) {
                                                str11 = URLDecoder.decode(split5[1], "UTF-8");
                                            } else if (split5[0].startsWith("itag")) {
                                                str11 = null;
                                                z6 = false;
                                            }
                                            if (z6 && str11 != null) {
                                                str10 = str11;
                                                break;
                                            }
                                            i5++;
                                            split4 = strArr4;
                                            str9 = str8;
                                        } catch (Exception e3) {
                                            e = e3;
                                            FileLog.e(e);
                                            i4++;
                                            str9 = str8;
                                            c = 1;
                                            c2 = 0;
                                            i2 = 2;
                                        }
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                    str8 = str9;
                                }
                            }
                            str8 = str9;
                        } else {
                            str8 = str9;
                            if (split[i4].startsWith("use_cipher_signature")) {
                                String[] split6 = split[i4].split(ContainerUtils.KEY_VALUE_DELIMITER);
                                if (split6.length == 2 && split6[1].toLowerCase().equals("true")) {
                                    z5 = true;
                                }
                            } else if (split[i4].startsWith("hlsvp")) {
                                String[] split7 = split[i4].split(ContainerUtils.KEY_VALUE_DELIMITER);
                                if (split7.length == 2) {
                                    try {
                                        obj = URLDecoder.decode(split7[1], "UTF-8");
                                    } catch (Exception e5) {
                                        FileLog.e(e5);
                                    }
                                }
                            } else if (split[i4].startsWith("livestream")) {
                                String[] split8 = split[i4].split(ContainerUtils.KEY_VALUE_DELIMITER);
                                if (split8.length == 2 && split8[1].toLowerCase().equals("1")) {
                                    z2 = true;
                                }
                            }
                        }
                        i4++;
                        str9 = str8;
                        c = 1;
                        c2 = 0;
                        i2 = 2;
                    }
                    str6 = str9;
                    z4 = z5;
                    str7 = obj;
                } else {
                    str6 = str9;
                    str7 = 0;
                    z2 = false;
                    z3 = false;
                }
                if (z2) {
                    if (str7 == 0 || z4 || str7.contains("/s/")) {
                        return null;
                    }
                    String[] strArr5 = this.result;
                    strArr5[0] = str7;
                    strArr5[1] = "hls";
                }
                if (z3) {
                    break;
                }
                i3++;
                str9 = str6;
                strArr2 = null;
                i = 5;
                c = 1;
                c2 = 0;
                i2 = 2;
            }
            String[] strArr6 = this.result;
            if (strArr6[0] == null && str10 != null) {
                strArr6[0] = str10;
                strArr6[1] = "other";
            }
            if (strArr6[0] == null || ((!z4 && !strArr6[0].contains("/s/")) || downloadUrlContent == null)) {
                strArr = null;
                z = z4;
            } else {
                int indexOf = this.result[0].indexOf("/s/");
                int indexOf2 = this.result[0].indexOf(47, indexOf + 10);
                if (indexOf != -1) {
                    if (indexOf2 == -1) {
                        indexOf2 = this.result[0].length();
                    }
                    this.sig = this.result[0].substring(indexOf, indexOf2);
                    Matcher matcher3 = WebPlayerView.jsPattern.matcher(downloadUrlContent);
                    if (matcher3.find()) {
                        try {
                            Object nextValue = new JSONTokener(matcher3.group(1)).nextValue();
                            str = nextValue instanceof String ? (String) nextValue : null;
                        } catch (Exception e6) {
                            FileLog.e(e6);
                        }
                        if (str != null) {
                            if (WebPlayerView.playerIdPattern.matcher(str).find()) {
                                str2 = matcher.group(1) + matcher.group(2);
                            } else {
                                str2 = null;
                            }
                            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("youtubecode", 0);
                            if (str2 != null) {
                                str3 = sharedPreferences.getString(str2, null);
                                str4 = sharedPreferences.getString(str2 + "n", null);
                            } else {
                                str3 = null;
                                str4 = null;
                            }
                            if (str3 == null) {
                                if (str.startsWith("//")) {
                                    str = "https:" + str;
                                } else if (str.startsWith("/")) {
                                    str = "https://www.youtube.com" + str;
                                }
                                String downloadUrlContent3 = WebPlayerView.this.downloadUrlContent(this, str);
                                if (isCancelled()) {
                                    return null;
                                }
                                strArr = null;
                                if (downloadUrlContent3 != null) {
                                    Matcher matcher4 = WebPlayerView.sigPattern.matcher(downloadUrlContent3);
                                    if (!matcher4.find()) {
                                        Matcher matcher5 = WebPlayerView.sigPattern2.matcher(downloadUrlContent3);
                                        if (matcher5.find()) {
                                            str4 = matcher5.group(1);
                                        }
                                    } else {
                                        str4 = matcher4.group(1);
                                    }
                                    if (str4 != null) {
                                        try {
                                            str3 = new JSExtractor(downloadUrlContent3).extractFunction(str4);
                                            if (!TextUtils.isEmpty(str3) && str2 != null) {
                                                sharedPreferences.edit().putString(str2, str3).putString(str2 + "n", str4).commit();
                                            }
                                        } catch (Exception e7) {
                                            FileLog.e(e7);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(str3)) {
                                        if (Build.VERSION.SDK_INT >= 21) {
                                            str5 = str3 + str4 + "('" + this.sig.substring(3) + "');";
                                        } else {
                                            str5 = str3 + "window." + WebPlayerView.this.interfaceName + ".returnResultToJava(" + str4 + "('" + this.sig.substring(3) + "'));";
                                        }
                                        try {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView$YoutubeVideoTask$$ExternalSyntheticLambda1
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    WebPlayerView.YoutubeVideoTask.this.lambda$doInBackground$1(str5);
                                                }
                                            });
                                            this.countDownLatch.await();
                                            z = false;
                                        } catch (Exception e8) {
                                            FileLog.e(e8);
                                        }
                                    }
                                    z = true;
                                }
                            } else {
                                strArr = null;
                            }
                            if (!TextUtils.isEmpty(str3)) {
                            }
                            z = true;
                        }
                    }
                    str = null;
                    if (str != null) {
                    }
                }
                strArr = null;
                z = true;
            }
            return (isCancelled() || z) ? strArr : this.result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$doInBackground$1(String str) {
            if (Build.VERSION.SDK_INT >= 21) {
                WebPlayerView.this.webView.evaluateJavascript(str, new ValueCallback() { // from class: org.telegram.ui.Components.WebPlayerView$YoutubeVideoTask$$ExternalSyntheticLambda0
                    @Override // android.webkit.ValueCallback
                    public final void onReceiveValue(Object obj) {
                        WebPlayerView.YoutubeVideoTask.this.lambda$doInBackground$0((String) obj);
                    }
                });
                return;
            }
            try {
                String encodeToString = Base64.encodeToString(("<script>" + str + "</script>").getBytes("UTF-8"), 0);
                WebView webView = WebPlayerView.this.webView;
                webView.loadUrl("data:text/html;charset=utf-8;base64," + encodeToString);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$doInBackground$0(String str) {
            String[] strArr = this.result;
            String str2 = strArr[0];
            String str3 = this.sig;
            strArr[0] = str2.replace(str3, "/signature/" + str.substring(1, str.length() - 1));
            this.countDownLatch.countDown();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onInterfaceResult(String str) {
            String[] strArr = this.result;
            String str2 = strArr[0];
            String str3 = this.sig;
            strArr[0] = str2.replace(str3, "/signature/" + str);
            this.countDownLatch.countDown();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String[] strArr) {
            if (strArr[0] != null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("start play youtube video " + strArr[1] + " " + strArr[0]);
                }
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = strArr[0];
                WebPlayerView.this.playVideoType = strArr[1];
                if (WebPlayerView.this.playVideoType.equals("hls")) {
                    WebPlayerView.this.isStream = true;
                }
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class VimeoVideoTask extends AsyncTask<Void, Void, String> {
        private String[] results = new String[2];
        private String videoId;

        public VimeoVideoTask(String str) {
            this.videoId = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://player.vimeo.com/video/%s/config", this.videoId));
            if (isCancelled()) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(downloadUrlContent).getJSONObject("request").getJSONObject("files");
                if (jSONObject.has("hls")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("hls");
                    try {
                        this.results[0] = jSONObject2.getString(RemoteMessageConst.Notification.URL);
                    } catch (Exception unused) {
                        this.results[0] = jSONObject2.getJSONObject("cdns").getJSONObject(jSONObject2.getString("default_cdn")).getString(RemoteMessageConst.Notification.URL);
                    }
                    this.results[1] = "hls";
                } else if (jSONObject.has("progressive")) {
                    this.results[1] = "other";
                    this.results[0] = jSONObject.getJSONArray("progressive").getJSONObject(0).getString(RemoteMessageConst.Notification.URL);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                return this.results[0];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = str;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class AparatVideoTask extends AsyncTask<Void, Void, String> {
        private String[] results = new String[2];
        private String videoId;

        public AparatVideoTask(String str) {
            this.videoId = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "http://www.aparat.com/video/video/embed/vt/frame/showvideo/yes/videohash/%s", this.videoId));
            if (isCancelled()) {
                return null;
            }
            try {
                Matcher matcher = WebPlayerView.aparatFileListPattern.matcher(downloadUrlContent);
                if (matcher.find()) {
                    JSONArray jSONArray = new JSONArray(matcher.group(1));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONArray jSONArray2 = jSONArray.getJSONArray(i);
                        if (jSONArray2.length() != 0) {
                            JSONObject jSONObject = jSONArray2.getJSONObject(0);
                            if (jSONObject.has("file")) {
                                this.results[0] = jSONObject.getString("file");
                                this.results[1] = "other";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                return this.results[0];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = str;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TwitchClipVideoTask extends AsyncTask<Void, Void, String> {
        private String currentUrl;
        private String[] results = new String[2];

        public TwitchClipVideoTask(String str, String str2) {
            this.currentUrl = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, this.currentUrl, null, false);
            if (isCancelled()) {
                return null;
            }
            try {
                Matcher matcher = WebPlayerView.twitchClipFilePattern.matcher(downloadUrlContent);
                if (matcher.find()) {
                    this.results[0] = new JSONObject(matcher.group(1)).getJSONArray("quality_options").getJSONObject(0).getString("source");
                    this.results[1] = "other";
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                return this.results[0];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = str;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TwitchStreamVideoTask extends AsyncTask<Void, Void, String> {
        private String[] results = new String[2];
        private String videoId;

        public TwitchStreamVideoTask(String str, String str2) {
            this.videoId = str2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Client-ID", "jzkbprff40iqj646a697cyrvl0zt2m6");
            int indexOf = this.videoId.indexOf(38);
            if (indexOf > 0) {
                this.videoId = this.videoId.substring(0, indexOf);
            }
            WebPlayerView webPlayerView = WebPlayerView.this;
            Locale locale = Locale.US;
            String downloadUrlContent = webPlayerView.downloadUrlContent(this, String.format(locale, "https://api.twitch.tv/kraken/streams/%s?stream_type=all", this.videoId), hashMap, false);
            if (isCancelled()) {
                return null;
            }
            try {
                new JSONObject(downloadUrlContent).getJSONObject("stream");
                JSONObject jSONObject = new JSONObject(WebPlayerView.this.downloadUrlContent(this, String.format(locale, "https://api.twitch.tv/api/channels/%s/access_token", this.videoId), hashMap, false));
                String encode = URLEncoder.encode(jSONObject.getString("sig"), "UTF-8");
                String encode2 = URLEncoder.encode(jSONObject.getString("token"), "UTF-8");
                URLEncoder.encode("https://youtube.googleapis.com/v/" + this.videoId, "UTF-8");
                String format = String.format(locale, "https://usher.ttvnw.net/api/channel/hls/%s.m3u8?%s", this.videoId, "allow_source=true&allow_audio_only=true&allow_spectre=true&player=twitchweb&segment_preference=4&p=" + ((int) (Math.random() * 1.0E7d)) + "&sig=" + encode + "&token=" + encode2);
                String[] strArr = this.results;
                strArr[0] = format;
                strArr[1] = "hls";
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                return this.results[0];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = str;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CoubVideoTask extends AsyncTask<Void, Void, String> {
        private String[] results = new String[4];
        private String videoId;

        public CoubVideoTask(String str) {
            this.videoId = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://coub.com/api/v2/coubs/%s.json", this.videoId));
            if (isCancelled()) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(downloadUrlContent).getJSONObject("file_versions").getJSONObject("mobile");
                String string = jSONObject.getString(MediaStreamTrack.VIDEO_TRACK_KIND);
                String string2 = jSONObject.getJSONArray(MediaStreamTrack.AUDIO_TRACK_KIND).getString(0);
                if (string != null && string2 != null) {
                    String[] strArr = this.results;
                    strArr[0] = string;
                    strArr[1] = "other";
                    strArr[2] = string2;
                    strArr[3] = "other";
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                return this.results[0];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            if (str != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = str;
                WebPlayerView.this.playVideoType = this.results[1];
                WebPlayerView.this.playAudioUrl = this.results[2];
                WebPlayerView.this.playAudioType = this.results[3];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (isCancelled()) {
            } else {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ControlsView extends FrameLayout {
        private int bufferedPosition;
        private AnimatorSet currentAnimation;
        private int currentProgressX;
        private int duration;
        private StaticLayout durationLayout;
        private int durationWidth;
        private int lastProgressX;
        private int progress;
        private Paint progressBufferedPaint;
        private Paint progressInnerPaint;
        private StaticLayout progressLayout;
        private Paint progressPaint;
        private boolean progressPressed;
        private TextPaint textPaint;
        private boolean isVisible = true;
        private Runnable hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView$ControlsView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WebPlayerView.ControlsView.this.lambda$new$0();
            }
        };
        private ImageReceiver imageReceiver = new ImageReceiver(this);

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            show(false, true);
        }

        public ControlsView(Context context) {
            super(context);
            setWillNotDraw(false);
            TextPaint textPaint = new TextPaint(1);
            this.textPaint = textPaint;
            textPaint.setColor(-1);
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            Paint paint = new Paint(1);
            this.progressPaint = paint;
            paint.setColor(-15095832);
            Paint paint2 = new Paint();
            this.progressInnerPaint = paint2;
            paint2.setColor(-6975081);
            Paint paint3 = new Paint(1);
            this.progressBufferedPaint = paint3;
            paint3.setColor(-1);
        }

        public void setDuration(int i) {
            if (this.duration == i || i < 0 || WebPlayerView.this.isStream) {
                return;
            }
            this.duration = i;
            StaticLayout staticLayout = new StaticLayout(AndroidUtilities.formatShortDuration(this.duration), this.textPaint, AndroidUtilities.dp(1000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.durationLayout = staticLayout;
            if (staticLayout.getLineCount() > 0) {
                this.durationWidth = (int) Math.ceil(this.durationLayout.getLineWidth(0));
            }
            invalidate();
        }

        public void setBufferedProgress(int i) {
            this.bufferedPosition = i;
            invalidate();
        }

        public void setProgress(int i) {
            if (this.progressPressed || i < 0 || WebPlayerView.this.isStream) {
                return;
            }
            this.progress = i;
            this.progressLayout = new StaticLayout(AndroidUtilities.formatShortDuration(this.progress), this.textPaint, AndroidUtilities.dp(1000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            invalidate();
        }

        public void show(boolean z, boolean z2) {
            if (this.isVisible == z) {
                return;
            }
            this.isVisible = z;
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            if (this.isVisible) {
                if (z2) {
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.currentAnimation = animatorSet2;
                    animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f));
                    this.currentAnimation.setDuration(150L);
                    this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.WebPlayerView.ControlsView.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ControlsView.this.currentAnimation = null;
                        }
                    });
                    this.currentAnimation.start();
                } else {
                    setAlpha(1.0f);
                }
            } else if (z2) {
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.currentAnimation = animatorSet3;
                animatorSet3.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f));
                this.currentAnimation.setDuration(150L);
                this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.WebPlayerView.ControlsView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ControlsView.this.currentAnimation = null;
                    }
                });
                this.currentAnimation.start();
            } else {
                setAlpha(0.0f);
            }
            checkNeedHide();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkNeedHide() {
            AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            if (!this.isVisible || !WebPlayerView.this.videoPlayer.isPlaying()) {
                return;
            }
            AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (!this.isVisible) {
                    show(true, true);
                    return true;
                }
                onTouchEvent(motionEvent);
                return this.progressPressed;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            super.requestDisallowInterceptTouchEvent(z);
            checkNeedHide();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int measuredWidth;
            int measuredHeight;
            int i;
            if (WebPlayerView.this.inFullscreen) {
                i = AndroidUtilities.dp(36.0f) + this.durationWidth;
                measuredWidth = (getMeasuredWidth() - AndroidUtilities.dp(76.0f)) - this.durationWidth;
                measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(28.0f);
            } else {
                measuredWidth = getMeasuredWidth();
                measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                i = 0;
            }
            int i2 = this.duration;
            int i3 = (i2 != 0 ? (int) ((measuredWidth - i) * (this.progress / i2)) : 0) + i;
            if (motionEvent.getAction() == 0) {
                if (this.isVisible && !WebPlayerView.this.isInline && !WebPlayerView.this.isStream) {
                    if (this.duration != 0) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x >= i3 - AndroidUtilities.dp(10.0f) && x <= AndroidUtilities.dp(10.0f) + i3 && y >= measuredHeight - AndroidUtilities.dp(10.0f) && y <= measuredHeight + AndroidUtilities.dp(10.0f)) {
                            this.progressPressed = true;
                            this.lastProgressX = x;
                            this.currentProgressX = i3;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            invalidate();
                        }
                    }
                } else {
                    show(true, true);
                }
                AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (WebPlayerView.this.initied && WebPlayerView.this.videoPlayer.isPlaying()) {
                    AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
                }
                if (this.progressPressed) {
                    this.progressPressed = false;
                    if (WebPlayerView.this.initied) {
                        this.progress = (int) (this.duration * ((this.currentProgressX - i) / (measuredWidth - i)));
                        WebPlayerView.this.videoPlayer.seekTo(this.progress * 1000);
                    }
                }
            } else if (motionEvent.getAction() == 2 && this.progressPressed) {
                int x2 = (int) motionEvent.getX();
                int i4 = this.currentProgressX - (this.lastProgressX - x2);
                this.currentProgressX = i4;
                this.lastProgressX = x2;
                if (i4 < i) {
                    this.currentProgressX = i;
                } else if (i4 > measuredWidth) {
                    this.currentProgressX = measuredWidth;
                }
                setProgress((int) (this.duration * 1000 * ((this.currentProgressX - i) / (measuredWidth - i))));
                invalidate();
            }
            super.onTouchEvent(motionEvent);
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:39:0x014a  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x015e  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x0190  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0193  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x01b9  */
        /* JADX WARN: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0161  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int dp;
            int dp2;
            int dp3;
            int dp4;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            if (WebPlayerView.this.drawImage) {
                if (WebPlayerView.this.firstFrameRendered && WebPlayerView.this.currentAlpha != 0.0f) {
                    long currentTimeMillis = System.currentTimeMillis();
                    WebPlayerView.this.lastUpdateTime = currentTimeMillis;
                    WebPlayerView.access$4724(WebPlayerView.this, ((float) (currentTimeMillis - WebPlayerView.this.lastUpdateTime)) / 150.0f);
                    if (WebPlayerView.this.currentAlpha < 0.0f) {
                        WebPlayerView.this.currentAlpha = 0.0f;
                    }
                    invalidate();
                }
                this.imageReceiver.setAlpha(WebPlayerView.this.currentAlpha);
                this.imageReceiver.draw(canvas);
            }
            if (!WebPlayerView.this.videoPlayer.isPlayerPrepared() || WebPlayerView.this.isStream) {
                return;
            }
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            if (!WebPlayerView.this.isInline) {
                int i6 = 6;
                if (this.durationLayout != null) {
                    canvas.save();
                    canvas.translate((measuredWidth - AndroidUtilities.dp(58.0f)) - this.durationWidth, measuredHeight - AndroidUtilities.dp((WebPlayerView.this.inFullscreen ? 6 : 10) + 29));
                    this.durationLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.progressLayout != null) {
                    canvas.save();
                    float dp5 = AndroidUtilities.dp(18.0f);
                    if (!WebPlayerView.this.inFullscreen) {
                        i6 = 10;
                    }
                    canvas.translate(dp5, measuredHeight - AndroidUtilities.dp(i6 + 29));
                    this.progressLayout.draw(canvas);
                    canvas.restore();
                }
            }
            if (this.duration == 0) {
                return;
            }
            float f = 7.0f;
            if (!WebPlayerView.this.isInline) {
                if (WebPlayerView.this.inFullscreen) {
                    int dp6 = measuredHeight - AndroidUtilities.dp(29.0f);
                    int dp7 = AndroidUtilities.dp(36.0f) + this.durationWidth;
                    dp3 = (measuredWidth - AndroidUtilities.dp(76.0f)) - this.durationWidth;
                    dp4 = measuredHeight - AndroidUtilities.dp(28.0f);
                    i = dp6;
                    i2 = dp7;
                    if (WebPlayerView.this.inFullscreen) {
                        canvas.drawRect(i2, i, dp3, AndroidUtilities.dp(3.0f) + i, this.progressInnerPaint);
                    }
                    if (!this.progressPressed) {
                        i3 = this.currentProgressX;
                    } else {
                        i3 = ((int) ((dp3 - i2) * (this.progress / this.duration))) + i2;
                    }
                    int i7 = i3;
                    i4 = this.bufferedPosition;
                    if (i4 != 0 && (i5 = this.duration) != 0) {
                        float f2 = i2;
                        canvas.drawRect(f2, i, ((dp3 - i2) * (i4 / i5)) + f2, AndroidUtilities.dp(3.0f) + i, !WebPlayerView.this.inFullscreen ? this.progressBufferedPaint : this.progressInnerPaint);
                    }
                    float f3 = i7;
                    canvas.drawRect(i2, i, f3, i + AndroidUtilities.dp(3.0f), this.progressPaint);
                    if (!WebPlayerView.this.isInline) {
                        return;
                    }
                    float f4 = dp4;
                    if (!this.progressPressed) {
                        f = 5.0f;
                    }
                    canvas.drawCircle(f3, f4, AndroidUtilities.dp(f), this.progressPaint);
                    return;
                }
                dp = measuredHeight - AndroidUtilities.dp(13.0f);
                dp2 = AndroidUtilities.dp(12.0f);
            } else {
                dp = measuredHeight - AndroidUtilities.dp(3.0f);
                dp2 = AndroidUtilities.dp(7.0f);
            }
            dp3 = measuredWidth;
            dp4 = measuredHeight - dp2;
            i = dp;
            i2 = 0;
            if (WebPlayerView.this.inFullscreen) {
            }
            if (!this.progressPressed) {
            }
            int i72 = i3;
            i4 = this.bufferedPosition;
            if (i4 != 0) {
                float f22 = i2;
                canvas.drawRect(f22, i, ((dp3 - i2) * (i4 / i5)) + f22, AndroidUtilities.dp(3.0f) + i, !WebPlayerView.this.inFullscreen ? this.progressBufferedPaint : this.progressInnerPaint);
            }
            float f32 = i72;
            canvas.drawRect(i2, i, f32, i + AndroidUtilities.dp(3.0f), this.progressPaint);
            if (!WebPlayerView.this.isInline) {
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public WebPlayerView(Context context, boolean z, boolean z2, WebPlayerViewDelegate webPlayerViewDelegate) {
        super(context);
        lastContainerId++;
        this.allowInlineAnimation = Build.VERSION.SDK_INT >= 21;
        this.backgroundPaint = new Paint();
        this.progressRunnable = new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView.1
            @Override // java.lang.Runnable
            public void run() {
                if (WebPlayerView.this.videoPlayer == null || !WebPlayerView.this.videoPlayer.isPlaying()) {
                    return;
                }
                WebPlayerView.this.controlsView.setProgress((int) (WebPlayerView.this.videoPlayer.getCurrentPosition() / 1000));
                WebPlayerView.this.controlsView.setBufferedProgress((int) (WebPlayerView.this.videoPlayer.getBufferedPosition() / 1000));
                AndroidUtilities.runOnUIThread(WebPlayerView.this.progressRunnable, 1000L);
            }
        };
        this.surfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: org.telegram.ui.Components.WebPlayerView.2
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                if (WebPlayerView.this.changingTextureView) {
                    if (WebPlayerView.this.switchingInlineMode) {
                        WebPlayerView.this.waitingForFirstTextureUpload = 2;
                    }
                    WebPlayerView.this.textureView.setSurfaceTexture(surfaceTexture);
                    WebPlayerView.this.textureView.setVisibility(0);
                    WebPlayerView.this.changingTextureView = false;
                    return false;
                }
                return true;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: org.telegram.ui.Components.WebPlayerView$2$1  reason: invalid class name */
            /* loaded from: classes3.dex */
            public class AnonymousClass1 implements ViewTreeObserver.OnPreDrawListener {
                AnonymousClass1() {
                }

                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    WebPlayerView.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (WebPlayerView.this.textureImageView != null) {
                        WebPlayerView.this.textureImageView.setVisibility(4);
                        WebPlayerView.this.textureImageView.setImageDrawable(null);
                        if (WebPlayerView.this.currentBitmap != null) {
                            WebPlayerView.this.currentBitmap.recycle();
                            WebPlayerView.this.currentBitmap = null;
                        }
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView$2$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            WebPlayerView.AnonymousClass2.AnonymousClass1.this.lambda$onPreDraw$0();
                        }
                    });
                    WebPlayerView.this.waitingForFirstTextureUpload = 0;
                    return true;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$onPreDraw$0() {
                    WebPlayerView.this.delegate.onInlineSurfaceTextureReady();
                }
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                if (WebPlayerView.this.waitingForFirstTextureUpload == 1) {
                    WebPlayerView.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass1());
                    WebPlayerView.this.changedTextureView.invalidate();
                }
            }
        };
        this.switchToInlineRunnable = new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView.3
            @Override // java.lang.Runnable
            public void run() {
                WebPlayerView.this.switchingInlineMode = false;
                if (WebPlayerView.this.currentBitmap != null) {
                    WebPlayerView.this.currentBitmap.recycle();
                    WebPlayerView.this.currentBitmap = null;
                }
                WebPlayerView.this.changingTextureView = true;
                if (WebPlayerView.this.textureImageView != null) {
                    try {
                        WebPlayerView webPlayerView = WebPlayerView.this;
                        webPlayerView.currentBitmap = Bitmaps.createBitmap(webPlayerView.textureView.getWidth(), WebPlayerView.this.textureView.getHeight(), Bitmap.Config.ARGB_8888);
                        WebPlayerView.this.textureView.getBitmap(WebPlayerView.this.currentBitmap);
                    } catch (Throwable th) {
                        if (WebPlayerView.this.currentBitmap != null) {
                            WebPlayerView.this.currentBitmap.recycle();
                            WebPlayerView.this.currentBitmap = null;
                        }
                        FileLog.e(th);
                    }
                    if (WebPlayerView.this.currentBitmap != null) {
                        WebPlayerView.this.textureImageView.setVisibility(0);
                        WebPlayerView.this.textureImageView.setImageBitmap(WebPlayerView.this.currentBitmap);
                    } else {
                        WebPlayerView.this.textureImageView.setImageDrawable(null);
                    }
                }
                WebPlayerView.this.isInline = true;
                WebPlayerView.this.updatePlayButton();
                WebPlayerView.this.updateShareButton();
                WebPlayerView.this.updateFullscreenButton();
                WebPlayerView.this.updateInlineButton();
                ViewGroup viewGroup = (ViewGroup) WebPlayerView.this.controlsView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(WebPlayerView.this.controlsView);
                }
                WebPlayerView webPlayerView2 = WebPlayerView.this;
                webPlayerView2.changedTextureView = webPlayerView2.delegate.onSwitchInlineMode(WebPlayerView.this.controlsView, WebPlayerView.this.isInline, WebPlayerView.this.videoWidth, WebPlayerView.this.videoHeight, WebPlayerView.this.aspectRatioFrameLayout.getVideoRotation(), WebPlayerView.this.allowInlineAnimation);
                WebPlayerView.this.changedTextureView.setVisibility(4);
                ViewGroup viewGroup2 = (ViewGroup) WebPlayerView.this.textureView.getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView(WebPlayerView.this.textureView);
                }
                WebPlayerView.this.controlsView.show(false, false);
            }
        };
        setWillNotDraw(false);
        this.delegate = webPlayerViewDelegate;
        this.backgroundPaint.setColor(-16777216);
        AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout(context) { // from class: org.telegram.ui.Components.WebPlayerView.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.exoplayer2.ui.AspectRatioFrameLayout, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
                if (WebPlayerView.this.textureViewContainer != null) {
                    ViewGroup.LayoutParams layoutParams = WebPlayerView.this.textureView.getLayoutParams();
                    layoutParams.width = getMeasuredWidth();
                    layoutParams.height = getMeasuredHeight();
                    if (WebPlayerView.this.textureImageView == null) {
                        return;
                    }
                    ViewGroup.LayoutParams layoutParams2 = WebPlayerView.this.textureImageView.getLayoutParams();
                    layoutParams2.width = getMeasuredWidth();
                    layoutParams2.height = getMeasuredHeight();
                }
            }
        };
        this.aspectRatioFrameLayout = aspectRatioFrameLayout;
        addView(aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1, 17));
        this.interfaceName = "JavaScriptInterface";
        WebView webView = new WebView(context);
        this.webView = webView;
        webView.addJavascriptInterface(new JavaScriptInterface(new CallJavaResultInterface() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.WebPlayerView.CallJavaResultInterface
            public final void jsCallFinished(String str) {
                WebPlayerView.this.lambda$new$0(str);
            }
        }), this.interfaceName);
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        this.textureViewContainer = this.delegate.getTextureViewContainer();
        TextureView textureView = new TextureView(context);
        this.textureView = textureView;
        textureView.setPivotX(0.0f);
        this.textureView.setPivotY(0.0f);
        ViewGroup viewGroup = this.textureViewContainer;
        if (viewGroup != null) {
            viewGroup.addView(this.textureView);
        } else {
            this.aspectRatioFrameLayout.addView(this.textureView, LayoutHelper.createFrame(-1, -1, 17));
        }
        if (this.allowInlineAnimation && this.textureViewContainer != null) {
            ImageView imageView = new ImageView(context);
            this.textureImageView = imageView;
            imageView.setBackgroundColor(-65536);
            this.textureImageView.setPivotX(0.0f);
            this.textureImageView.setPivotY(0.0f);
            this.textureImageView.setVisibility(4);
            this.textureViewContainer.addView(this.textureImageView);
        }
        VideoPlayer videoPlayer = new VideoPlayer();
        this.videoPlayer = videoPlayer;
        videoPlayer.setDelegate(this);
        this.videoPlayer.setTextureView(this.textureView);
        ControlsView controlsView = new ControlsView(context);
        this.controlsView = controlsView;
        ViewGroup viewGroup2 = this.textureViewContainer;
        if (viewGroup2 != null) {
            viewGroup2.addView(controlsView);
        } else {
            addView(controlsView, LayoutHelper.createFrame(-1, -1.0f));
        }
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressView = radialProgressView;
        radialProgressView.setProgressColor(-1);
        addView(this.progressView, LayoutHelper.createFrame(48, 48, 17));
        ImageView imageView2 = new ImageView(context);
        this.fullscreenButton = imageView2;
        imageView2.setScaleType(ImageView.ScaleType.CENTER);
        this.controlsView.addView(this.fullscreenButton, LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
        this.fullscreenButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WebPlayerView.this.lambda$new$1(view);
            }
        });
        ImageView imageView3 = new ImageView(context);
        this.playButton = imageView3;
        imageView3.setScaleType(ImageView.ScaleType.CENTER);
        this.controlsView.addView(this.playButton, LayoutHelper.createFrame(48, 48, 17));
        this.playButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WebPlayerView.this.lambda$new$2(view);
            }
        });
        if (z) {
            ImageView imageView4 = new ImageView(context);
            this.inlineButton = imageView4;
            imageView4.setScaleType(ImageView.ScaleType.CENTER);
            this.controlsView.addView(this.inlineButton, LayoutHelper.createFrame(56, 48, 53));
            this.inlineButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    WebPlayerView.this.lambda$new$3(view);
                }
            });
        }
        if (z2) {
            ImageView imageView5 = new ImageView(context);
            this.shareButton = imageView5;
            imageView5.setScaleType(ImageView.ScaleType.CENTER);
            this.shareButton.setImageResource(R.drawable.ic_share_video);
            this.controlsView.addView(this.shareButton, LayoutHelper.createFrame(56, 48, 53));
            this.shareButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    WebPlayerView.this.lambda$new$4(view);
                }
            });
        }
        updatePlayButton();
        updateFullscreenButton();
        updateInlineButton();
        updateShareButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(String str) {
        AsyncTask asyncTask = this.currentTask;
        if (asyncTask == null || asyncTask.isCancelled()) {
            return;
        }
        AsyncTask asyncTask2 = this.currentTask;
        if (!(asyncTask2 instanceof YoutubeVideoTask)) {
            return;
        }
        ((YoutubeVideoTask) asyncTask2).onInterfaceResult(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        if (!this.initied || this.changingTextureView || this.switchingInlineMode || !this.firstFrameRendered) {
            return;
        }
        this.inFullscreen = !this.inFullscreen;
        updateFullscreenState(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        if (!this.initied || this.playVideoUrl == null) {
            return;
        }
        if (!this.videoPlayer.isPlayerPrepared()) {
            preparePlayer();
        }
        if (this.videoPlayer.isPlaying()) {
            this.videoPlayer.pause();
        } else {
            this.isCompleted = false;
            this.videoPlayer.play();
        }
        updatePlayButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        if (this.textureView == null || !this.delegate.checkInlinePermissions() || this.changingTextureView || this.switchingInlineMode || !this.firstFrameRendered) {
            return;
        }
        this.switchingInlineMode = true;
        if (!this.isInline) {
            this.inFullscreen = false;
            this.delegate.prepareToSwitchInlineMode(true, this.switchToInlineRunnable, this.aspectRatioFrameLayout.getAspectRatio(), this.allowInlineAnimation);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) this.aspectRatioFrameLayout.getParent();
        if (viewGroup != this) {
            if (viewGroup != null) {
                viewGroup.removeView(this.aspectRatioFrameLayout);
            }
            addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            this.aspectRatioFrameLayout.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - AndroidUtilities.dp(10.0f), 1073741824));
        }
        Bitmap bitmap = this.currentBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.currentBitmap = null;
        }
        this.changingTextureView = true;
        this.isInline = false;
        updatePlayButton();
        updateShareButton();
        updateFullscreenButton();
        updateInlineButton();
        this.textureView.setVisibility(4);
        ViewGroup viewGroup2 = this.textureViewContainer;
        if (viewGroup2 != null) {
            viewGroup2.addView(this.textureView);
        } else {
            this.aspectRatioFrameLayout.addView(this.textureView);
        }
        ViewGroup viewGroup3 = (ViewGroup) this.controlsView.getParent();
        if (viewGroup3 != this) {
            if (viewGroup3 != null) {
                viewGroup3.removeView(this.controlsView);
            }
            ViewGroup viewGroup4 = this.textureViewContainer;
            if (viewGroup4 != null) {
                viewGroup4.addView(this.controlsView);
            } else {
                addView(this.controlsView, 1);
            }
        }
        this.controlsView.show(false, false);
        this.delegate.prepareToSwitchInlineMode(false, null, this.aspectRatioFrameLayout.getAspectRatio(), this.allowInlineAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        WebPlayerViewDelegate webPlayerViewDelegate = this.delegate;
        if (webPlayerViewDelegate != null) {
            webPlayerViewDelegate.onSharePressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitFailed() {
        if (this.controlsView.getParent() != this) {
            this.controlsView.setVisibility(8);
        }
        this.delegate.onInitFailed();
    }

    public void updateTextureImageView() {
        if (this.textureImageView == null) {
            return;
        }
        try {
            Bitmap createBitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Bitmap.Config.ARGB_8888);
            this.currentBitmap = createBitmap;
            this.changedTextureView.getBitmap(createBitmap);
        } catch (Throwable th) {
            Bitmap bitmap = this.currentBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.currentBitmap = null;
            }
            FileLog.e(th);
        }
        if (this.currentBitmap != null) {
            this.textureImageView.setVisibility(0);
            this.textureImageView.setImageBitmap(this.currentBitmap);
            return;
        }
        this.textureImageView.setImageDrawable(null);
    }

    public String getYoutubeId() {
        return this.currentYoutubeId;
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public void onStateChanged(boolean z, int i) {
        if (i != 2) {
            if (this.videoPlayer.getDuration() != -9223372036854775807L) {
                this.controlsView.setDuration((int) (this.videoPlayer.getDuration() / 1000));
            } else {
                this.controlsView.setDuration(0);
            }
        }
        if (i != 4 && i != 1 && this.videoPlayer.isPlaying()) {
            this.delegate.onPlayStateChanged(this, true);
        } else {
            this.delegate.onPlayStateChanged(this, false);
        }
        if (this.videoPlayer.isPlaying() && i != 4) {
            updatePlayButton();
        } else if (i != 4) {
        } else {
            this.isCompleted = true;
            this.videoPlayer.pause();
            this.videoPlayer.seekTo(0L);
            updatePlayButton();
            this.controlsView.show(true, true);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(10.0f), this.backgroundPaint);
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public void onError(VideoPlayer videoPlayer, Exception exc) {
        FileLog.e(exc);
        onInitFailed();
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public void onVideoSizeChanged(int i, int i2, int i3, float f) {
        AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            if (i3 == 90 || i3 == 270) {
                i2 = i;
                i = i2;
            }
            float f2 = i * f;
            this.videoWidth = (int) f2;
            this.videoHeight = i2;
            float f3 = i2 == 0 ? 1.0f : f2 / i2;
            aspectRatioFrameLayout.setAspectRatio(f3, i3);
            if (!this.inFullscreen) {
                return;
            }
            this.delegate.onVideoSizeChanged(f3, i3);
        }
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public void onRenderedFirstFrame() {
        this.firstFrameRendered = true;
        this.lastUpdateTime = System.currentTimeMillis();
        this.controlsView.invalidate();
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        if (this.changingTextureView) {
            this.changingTextureView = false;
            if (this.inFullscreen || this.isInline) {
                if (this.isInline) {
                    this.waitingForFirstTextureUpload = 1;
                }
                this.changedTextureView.setSurfaceTexture(surfaceTexture);
                this.changedTextureView.setSurfaceTextureListener(this.surfaceTextureListener);
                this.changedTextureView.setVisibility(0);
                return true;
            }
        }
        return false;
    }

    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (this.waitingForFirstTextureUpload == 2) {
            ImageView imageView = this.textureImageView;
            if (imageView != null) {
                imageView.setVisibility(4);
                this.textureImageView.setImageDrawable(null);
                Bitmap bitmap = this.currentBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    this.currentBitmap = null;
                }
            }
            this.switchingInlineMode = false;
            this.delegate.onSwitchInlineMode(this.controlsView, false, this.videoWidth, this.videoHeight, this.aspectRatioFrameLayout.getVideoRotation(), this.allowInlineAnimation);
            this.waitingForFirstTextureUpload = 0;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int measuredWidth = (i5 - this.aspectRatioFrameLayout.getMeasuredWidth()) / 2;
        int i6 = i4 - i2;
        int dp = ((i6 - AndroidUtilities.dp(10.0f)) - this.aspectRatioFrameLayout.getMeasuredHeight()) / 2;
        AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        aspectRatioFrameLayout.layout(measuredWidth, dp, aspectRatioFrameLayout.getMeasuredWidth() + measuredWidth, this.aspectRatioFrameLayout.getMeasuredHeight() + dp);
        if (this.controlsView.getParent() == this) {
            ControlsView controlsView = this.controlsView;
            controlsView.layout(0, 0, controlsView.getMeasuredWidth(), this.controlsView.getMeasuredHeight());
        }
        int measuredWidth2 = (i5 - this.progressView.getMeasuredWidth()) / 2;
        int measuredHeight = (i6 - this.progressView.getMeasuredHeight()) / 2;
        RadialProgressView radialProgressView = this.progressView;
        radialProgressView.layout(measuredWidth2, measuredHeight, radialProgressView.getMeasuredWidth() + measuredWidth2, this.progressView.getMeasuredHeight() + measuredHeight);
        this.controlsView.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(10.0f));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        this.aspectRatioFrameLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - AndroidUtilities.dp(10.0f), 1073741824));
        if (this.controlsView.getParent() == this) {
            this.controlsView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
        }
        this.progressView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
        setMeasuredDimension(size, size2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayButton() {
        this.controlsView.checkNeedHide();
        AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
        if (!this.videoPlayer.isPlaying()) {
            if (this.isCompleted) {
                this.playButton.setImageResource(this.isInline ? R.drawable.ic_againinline : R.drawable.ic_again);
                return;
            } else {
                this.playButton.setImageResource(this.isInline ? R.drawable.ic_playinline : R.drawable.ic_play);
                return;
            }
        }
        this.playButton.setImageResource(this.isInline ? R.drawable.ic_pauseinline : R.drawable.ic_pause);
        AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
        checkAudioFocus();
    }

    private void checkAudioFocus() {
        if (!this.hasAudioFocus) {
            this.hasAudioFocus = true;
            ((AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).requestAudioFocus(this, 3, 1);
        }
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.WebPlayerView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                WebPlayerView.this.lambda$onAudioFocusChange$5(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioFocusChange$5(int i) {
        if (i == -1) {
            if (this.videoPlayer.isPlaying()) {
                this.videoPlayer.pause();
                updatePlayButton();
            }
            this.hasAudioFocus = false;
        } else if (i == 1) {
            if (!this.resumeAudioOnFocusGain) {
                return;
            }
            this.resumeAudioOnFocusGain = false;
            this.videoPlayer.play();
        } else if (i == -3 || i != -2 || !this.videoPlayer.isPlaying()) {
        } else {
            this.resumeAudioOnFocusGain = true;
            this.videoPlayer.pause();
            updatePlayButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFullscreenButton() {
        if (!this.videoPlayer.isPlayerPrepared() || this.isInline) {
            this.fullscreenButton.setVisibility(8);
            return;
        }
        this.fullscreenButton.setVisibility(0);
        if (!this.inFullscreen) {
            this.fullscreenButton.setImageResource(R.drawable.ic_gofullscreen);
            this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
            return;
        }
        this.fullscreenButton.setImageResource(R.drawable.ic_outfullscreen);
        this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 1.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShareButton() {
        ImageView imageView = this.shareButton;
        if (imageView == null) {
            return;
        }
        imageView.setVisibility((this.isInline || !this.videoPlayer.isPlayerPrepared()) ? 8 : 0);
    }

    private View getControlView() {
        return this.controlsView;
    }

    private View getProgressView() {
        return this.progressView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInlineButton() {
        ImageView imageView = this.inlineButton;
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(this.isInline ? R.drawable.ic_goinline : R.drawable.ic_outinline);
        this.inlineButton.setVisibility(this.videoPlayer.isPlayerPrepared() ? 0 : 8);
        if (this.isInline) {
            this.inlineButton.setLayoutParams(LayoutHelper.createFrame(40, 40, 53));
        } else {
            this.inlineButton.setLayoutParams(LayoutHelper.createFrame(56, 50, 53));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preparePlayer() {
        String str = this.playVideoUrl;
        if (str == null) {
            return;
        }
        if (str != null && this.playAudioUrl != null) {
            this.videoPlayer.preparePlayerLoop(Uri.parse(str), this.playVideoType, Uri.parse(this.playAudioUrl), this.playAudioType);
        } else {
            this.videoPlayer.preparePlayer(Uri.parse(str), this.playVideoType);
        }
        this.videoPlayer.setPlayWhenReady(this.isAutoplay);
        if (this.videoPlayer.getDuration() != -9223372036854775807L) {
            this.controlsView.setDuration((int) (this.videoPlayer.getDuration() / 1000));
        } else {
            this.controlsView.setDuration(0);
        }
        updateFullscreenButton();
        updateShareButton();
        updateInlineButton();
        this.controlsView.invalidate();
        int i = this.seekToTime;
        if (i == -1) {
            return;
        }
        this.videoPlayer.seekTo(i * 1000);
    }

    public void pause() {
        this.videoPlayer.pause();
        updatePlayButton();
        this.controlsView.show(true, true);
    }

    private void updateFullscreenState(boolean z) {
        ViewGroup viewGroup;
        if (this.textureView == null) {
            return;
        }
        updateFullscreenButton();
        ViewGroup viewGroup2 = this.textureViewContainer;
        if (viewGroup2 == null) {
            this.changingTextureView = true;
            if (!this.inFullscreen) {
                if (viewGroup2 != null) {
                    viewGroup2.addView(this.textureView);
                } else {
                    this.aspectRatioFrameLayout.addView(this.textureView);
                }
            }
            if (this.inFullscreen) {
                ViewGroup viewGroup3 = (ViewGroup) this.controlsView.getParent();
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.controlsView);
                }
            } else {
                ViewGroup viewGroup4 = (ViewGroup) this.controlsView.getParent();
                if (viewGroup4 != this) {
                    if (viewGroup4 != null) {
                        viewGroup4.removeView(this.controlsView);
                    }
                    ViewGroup viewGroup5 = this.textureViewContainer;
                    if (viewGroup5 != null) {
                        viewGroup5.addView(this.controlsView);
                    } else {
                        addView(this.controlsView, 1);
                    }
                }
            }
            TextureView onSwitchToFullscreen = this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), z);
            this.changedTextureView = onSwitchToFullscreen;
            onSwitchToFullscreen.setVisibility(4);
            if (this.inFullscreen && this.changedTextureView != null && (viewGroup = (ViewGroup) this.textureView.getParent()) != null) {
                viewGroup.removeView(this.textureView);
            }
            this.controlsView.checkNeedHide();
            return;
        }
        if (this.inFullscreen) {
            ViewGroup viewGroup6 = (ViewGroup) this.aspectRatioFrameLayout.getParent();
            if (viewGroup6 != null) {
                viewGroup6.removeView(this.aspectRatioFrameLayout);
            }
        } else {
            ViewGroup viewGroup7 = (ViewGroup) this.aspectRatioFrameLayout.getParent();
            if (viewGroup7 != this) {
                if (viewGroup7 != null) {
                    viewGroup7.removeView(this.aspectRatioFrameLayout);
                }
                addView(this.aspectRatioFrameLayout, 0);
            }
        }
        this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), z);
    }

    public void exitFullscreen() {
        if (!this.inFullscreen) {
            return;
        }
        this.inFullscreen = false;
        updateInlineButton();
        updateFullscreenState(false);
    }

    public boolean isInitied() {
        return this.initied;
    }

    public boolean isInline() {
        return this.isInline || this.switchingInlineMode;
    }

    public void enterFullscreen() {
        if (this.inFullscreen) {
            return;
        }
        this.inFullscreen = true;
        updateInlineButton();
        updateFullscreenState(false);
    }

    public boolean isInFullscreen() {
        return this.inFullscreen;
    }

    public static String getYouTubeVideoId(String str) {
        Matcher matcher = youtubeIdRegex.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public boolean canHandleUrl(String str) {
        if (str != null) {
            if (str.endsWith(".mp4")) {
                return true;
            }
            String str2 = null;
            try {
                Matcher matcher = youtubeIdRegex.matcher(str);
                if ((matcher.find() ? matcher.group(1) : null) != null) {
                    return true;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            try {
                Matcher matcher2 = vimeoIdRegex.matcher(str);
                if ((matcher2.find() ? matcher2.group(3) : null) != null) {
                    return true;
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            try {
                Matcher matcher3 = aparatIdRegex.matcher(str);
                if ((matcher3.find() ? matcher3.group(1) : null) != null) {
                    return true;
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            try {
                Matcher matcher4 = twitchClipIdRegex.matcher(str);
                if ((matcher4.find() ? matcher4.group(1) : null) != null) {
                    return true;
                }
            } catch (Exception e4) {
                FileLog.e(e4);
            }
            try {
                Matcher matcher5 = twitchStreamIdRegex.matcher(str);
                if ((matcher5.find() ? matcher5.group(1) : null) != null) {
                    return true;
                }
            } catch (Exception e5) {
                FileLog.e(e5);
            }
            try {
                Matcher matcher6 = coubIdRegex.matcher(str);
                if (matcher6.find()) {
                    str2 = matcher6.group(1);
                }
                return str2 != null;
            } catch (Exception e6) {
                FileLog.e(e6);
                return false;
            }
        }
        return false;
    }

    public void willHandle() {
        this.controlsView.setVisibility(4);
        this.controlsView.show(false, false);
        showProgress(true, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x024e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x010f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00f0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00d1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean loadVideo(String str, TLRPC$Photo tLRPC$Photo, Object obj, String str2, boolean z) {
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        AsyncTask asyncTask;
        AnimatorSet animatorSet;
        String coubId = getCoubId(str);
        if (coubId == null) {
            coubId = getCoubId(str2);
        }
        this.seekToTime = -1;
        if (coubId != null || str == null) {
            str3 = null;
            str4 = null;
        } else if (!str.endsWith(".mp4")) {
            try {
                if (str2 != null) {
                    try {
                        Uri parse = Uri.parse(str2);
                        String queryParameter = parse.getQueryParameter("t");
                        if (queryParameter == null) {
                            queryParameter = parse.getQueryParameter("time_continue");
                        }
                        if (queryParameter != null) {
                            if (queryParameter.contains("m")) {
                                String[] split = queryParameter.split("m");
                                this.seekToTime = (Utilities.parseInt((CharSequence) split[0]).intValue() * 60) + Utilities.parseInt((CharSequence) split[1]).intValue();
                            } else {
                                this.seekToTime = Utilities.parseInt((CharSequence) queryParameter).intValue();
                            }
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                Matcher matcher = youtubeIdRegex.matcher(str);
                String group = matcher.find() ? matcher.group(1) : null;
                if (group == null) {
                    group = null;
                }
                str4 = group;
            } catch (Exception e2) {
                FileLog.e(e2);
                str4 = null;
            }
            if (str4 == null) {
                try {
                    Matcher matcher2 = vimeoIdRegex.matcher(str);
                    String group2 = matcher2.find() ? matcher2.group(3) : null;
                    if (group2 == null) {
                        group2 = null;
                    }
                    str8 = group2;
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                if (str8 == null) {
                    try {
                        Matcher matcher3 = aparatIdRegex.matcher(str);
                        String group3 = matcher3.find() ? matcher3.group(1) : null;
                        if (group3 == null) {
                            group3 = null;
                        }
                        str7 = group3;
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    if (str7 == null) {
                        try {
                            Matcher matcher4 = twitchClipIdRegex.matcher(str);
                            String group4 = matcher4.find() ? matcher4.group(1) : null;
                            if (group4 == null) {
                                group4 = null;
                            }
                            str6 = group4;
                        } catch (Exception e5) {
                            FileLog.e(e5);
                        }
                        if (str6 == null) {
                            try {
                                Matcher matcher5 = twitchStreamIdRegex.matcher(str);
                                String group5 = matcher5.find() ? matcher5.group(1) : null;
                                if (group5 == null) {
                                    group5 = null;
                                }
                                str5 = group5;
                            } catch (Exception e6) {
                                FileLog.e(e6);
                            }
                            if (str5 == null) {
                                try {
                                    Matcher matcher6 = coubIdRegex.matcher(str);
                                    String group6 = matcher6.find() ? matcher6.group(1) : null;
                                    if (group6 != null) {
                                        coubId = group6;
                                    }
                                } catch (Exception e7) {
                                    FileLog.e(e7);
                                }
                            }
                            str3 = null;
                            this.initied = false;
                            this.isCompleted = false;
                            this.isAutoplay = z;
                            this.playVideoUrl = null;
                            this.playAudioUrl = null;
                            destroy();
                            this.firstFrameRendered = false;
                            this.currentAlpha = 1.0f;
                            asyncTask = this.currentTask;
                            if (asyncTask != null) {
                                asyncTask.cancel(true);
                                this.currentTask = null;
                            }
                            updateFullscreenButton();
                            updateShareButton();
                            updateInlineButton();
                            updatePlayButton();
                            if (tLRPC$Photo == null) {
                                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 80, true);
                                if (closestPhotoSizeWithSize != null) {
                                    this.controlsView.imageReceiver.setImage(null, null, ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), "80_80_b", 0L, null, obj, 1);
                                    this.drawImage = true;
                                }
                            } else {
                                this.drawImage = false;
                            }
                            animatorSet = this.progressAnimation;
                            if (animatorSet != null) {
                                animatorSet.cancel();
                                this.progressAnimation = null;
                            }
                            this.controlsView.setProgress(0);
                            if (str4 != null) {
                                this.currentYoutubeId = str4;
                                str4 = null;
                            }
                            if (str3 == null) {
                                this.initied = true;
                                this.playVideoUrl = str3;
                                this.playVideoType = "other";
                                if (this.isAutoplay) {
                                    preparePlayer();
                                }
                                showProgress(false, false);
                                this.controlsView.show(true, true);
                            } else {
                                if (str4 != null) {
                                    YoutubeVideoTask youtubeVideoTask = new YoutubeVideoTask(str4);
                                    youtubeVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = youtubeVideoTask;
                                } else if (str8 != null) {
                                    VimeoVideoTask vimeoVideoTask = new VimeoVideoTask(str8);
                                    vimeoVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = vimeoVideoTask;
                                } else if (coubId != null) {
                                    CoubVideoTask coubVideoTask = new CoubVideoTask(coubId);
                                    coubVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = coubVideoTask;
                                    this.isStream = true;
                                } else if (str7 != null) {
                                    AparatVideoTask aparatVideoTask = new AparatVideoTask(str7);
                                    aparatVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = aparatVideoTask;
                                } else if (str6 != null) {
                                    TwitchClipVideoTask twitchClipVideoTask = new TwitchClipVideoTask(str, str6);
                                    twitchClipVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = twitchClipVideoTask;
                                } else if (str5 != null) {
                                    TwitchStreamVideoTask twitchStreamVideoTask = new TwitchStreamVideoTask(str, str5);
                                    twitchStreamVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                                    this.currentTask = twitchStreamVideoTask;
                                    this.isStream = true;
                                }
                                this.controlsView.show(false, false);
                                showProgress(true, false);
                            }
                            if (str4 == null || str8 != null || coubId != null || str7 != null || str3 != null || str6 != null || str5 != null) {
                                this.controlsView.setVisibility(0);
                                return true;
                            }
                            this.controlsView.setVisibility(8);
                            return false;
                        }
                        str5 = null;
                        if (str5 == null) {
                        }
                        str3 = null;
                        this.initied = false;
                        this.isCompleted = false;
                        this.isAutoplay = z;
                        this.playVideoUrl = null;
                        this.playAudioUrl = null;
                        destroy();
                        this.firstFrameRendered = false;
                        this.currentAlpha = 1.0f;
                        asyncTask = this.currentTask;
                        if (asyncTask != null) {
                        }
                        updateFullscreenButton();
                        updateShareButton();
                        updateInlineButton();
                        updatePlayButton();
                        if (tLRPC$Photo == null) {
                        }
                        animatorSet = this.progressAnimation;
                        if (animatorSet != null) {
                        }
                        this.controlsView.setProgress(0);
                        if (str4 != null) {
                        }
                        if (str3 == null) {
                        }
                        if (str4 == null) {
                        }
                        this.controlsView.setVisibility(0);
                        return true;
                    }
                    str6 = null;
                    if (str6 == null) {
                    }
                    str5 = null;
                    if (str5 == null) {
                    }
                    str3 = null;
                    this.initied = false;
                    this.isCompleted = false;
                    this.isAutoplay = z;
                    this.playVideoUrl = null;
                    this.playAudioUrl = null;
                    destroy();
                    this.firstFrameRendered = false;
                    this.currentAlpha = 1.0f;
                    asyncTask = this.currentTask;
                    if (asyncTask != null) {
                    }
                    updateFullscreenButton();
                    updateShareButton();
                    updateInlineButton();
                    updatePlayButton();
                    if (tLRPC$Photo == null) {
                    }
                    animatorSet = this.progressAnimation;
                    if (animatorSet != null) {
                    }
                    this.controlsView.setProgress(0);
                    if (str4 != null) {
                    }
                    if (str3 == null) {
                    }
                    if (str4 == null) {
                    }
                    this.controlsView.setVisibility(0);
                    return true;
                }
                str7 = null;
                if (str7 == null) {
                }
                str6 = null;
                if (str6 == null) {
                }
                str5 = null;
                if (str5 == null) {
                }
                str3 = null;
                this.initied = false;
                this.isCompleted = false;
                this.isAutoplay = z;
                this.playVideoUrl = null;
                this.playAudioUrl = null;
                destroy();
                this.firstFrameRendered = false;
                this.currentAlpha = 1.0f;
                asyncTask = this.currentTask;
                if (asyncTask != null) {
                }
                updateFullscreenButton();
                updateShareButton();
                updateInlineButton();
                updatePlayButton();
                if (tLRPC$Photo == null) {
                }
                animatorSet = this.progressAnimation;
                if (animatorSet != null) {
                }
                this.controlsView.setProgress(0);
                if (str4 != null) {
                }
                if (str3 == null) {
                }
                if (str4 == null) {
                }
                this.controlsView.setVisibility(0);
                return true;
            }
            str8 = null;
            if (str8 == null) {
            }
            str7 = null;
            if (str7 == null) {
            }
            str6 = null;
            if (str6 == null) {
            }
            str5 = null;
            if (str5 == null) {
            }
            str3 = null;
            this.initied = false;
            this.isCompleted = false;
            this.isAutoplay = z;
            this.playVideoUrl = null;
            this.playAudioUrl = null;
            destroy();
            this.firstFrameRendered = false;
            this.currentAlpha = 1.0f;
            asyncTask = this.currentTask;
            if (asyncTask != null) {
            }
            updateFullscreenButton();
            updateShareButton();
            updateInlineButton();
            updatePlayButton();
            if (tLRPC$Photo == null) {
            }
            animatorSet = this.progressAnimation;
            if (animatorSet != null) {
            }
            this.controlsView.setProgress(0);
            if (str4 != null) {
            }
            if (str3 == null) {
            }
            if (str4 == null) {
            }
            this.controlsView.setVisibility(0);
            return true;
        } else {
            str3 = str;
            str4 = null;
        }
        str8 = str4;
        str7 = str8;
        str6 = str7;
        str5 = str6;
        this.initied = false;
        this.isCompleted = false;
        this.isAutoplay = z;
        this.playVideoUrl = null;
        this.playAudioUrl = null;
        destroy();
        this.firstFrameRendered = false;
        this.currentAlpha = 1.0f;
        asyncTask = this.currentTask;
        if (asyncTask != null) {
        }
        updateFullscreenButton();
        updateShareButton();
        updateInlineButton();
        updatePlayButton();
        if (tLRPC$Photo == null) {
        }
        animatorSet = this.progressAnimation;
        if (animatorSet != null) {
        }
        this.controlsView.setProgress(0);
        if (str4 != null) {
        }
        if (str3 == null) {
        }
        if (str4 == null) {
        }
        this.controlsView.setVisibility(0);
        return true;
    }

    public String getCoubId(String str) {
        String group;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Matcher matcher = coubIdRegex.matcher(str);
            group = matcher.find() ? matcher.group(1) : null;
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (group == null) {
            return null;
        }
        return group;
    }

    public View getAspectRatioView() {
        return this.aspectRatioFrameLayout;
    }

    public TextureView getTextureView() {
        return this.textureView;
    }

    public ImageView getTextureImageView() {
        return this.textureImageView;
    }

    public View getControlsView() {
        return this.controlsView;
    }

    public void destroy() {
        this.videoPlayer.releasePlayer(false);
        AsyncTask asyncTask = this.currentTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.currentTask = null;
        }
        this.webView.stopLoading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgress(boolean z, boolean z2) {
        float f = 1.0f;
        if (z2) {
            AnimatorSet animatorSet = this.progressAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.progressAnimation = animatorSet2;
            Animator[] animatorArr = new Animator[1];
            RadialProgressView radialProgressView = this.progressView;
            float[] fArr = new float[1];
            if (!z) {
                f = 0.0f;
            }
            fArr[0] = f;
            animatorArr[0] = ObjectAnimator.ofFloat(radialProgressView, "alpha", fArr);
            animatorSet2.playTogether(animatorArr);
            this.progressAnimation.setDuration(150L);
            this.progressAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.WebPlayerView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    WebPlayerView.this.progressAnimation = null;
                }
            });
            this.progressAnimation.start();
            return;
        }
        RadialProgressView radialProgressView2 = this.progressView;
        if (!z) {
            f = 0.0f;
        }
        radialProgressView2.setAlpha(f);
    }
}
