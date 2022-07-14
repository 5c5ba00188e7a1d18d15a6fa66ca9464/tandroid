package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes4.dex */
public class ChatActionCell extends BaseCell implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private int TAG;
    private SpannableStringBuilder accessibilityText;
    private AvatarDrawable avatarDrawable;
    private int backgroundHeight;
    private Path backgroundPath;
    private boolean canDrawInParent;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private ImageLocation currentVideoLocation;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private boolean invalidatePath;
    private float lastTouchX;
    private float lastTouchY;
    private ArrayList<Integer> lineHeights;
    private ArrayList<Integer> lineWidths;
    private String overrideBackground;
    private Paint overrideBackgroundPaint;
    private int overrideColor;
    private String overrideText;
    private TextPaint overrideTextPaint;
    private URLSpan pressedLink;
    private int previousWidth;
    private RectF rect;
    public List<SpoilerEffect> spoilers;
    private Stack<SpoilerEffect> spoilersPool;
    private int textHeight;
    private StaticLayout textLayout;
    TextPaint textPaint;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private ThemeDelegate themeDelegate;
    private float viewTop;
    private boolean visiblePartSet;
    private boolean wasLayout;

    /* loaded from: classes4.dex */
    public interface ThemeDelegate extends Theme.ResourcesProvider {
        int getCurrentColor();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
        } else if (id == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
        }
    }

    public void setSpoilersSuppressed(boolean s) {
        for (SpoilerEffect eff : this.spoilers) {
            eff.setSuppressUpdates(s);
        }
    }

    /* loaded from: classes4.dex */
    public interface ChatActionCellDelegate {
        void didClickImage(ChatActionCell chatActionCell);

        boolean didLongPress(ChatActionCell chatActionCell, float f, float f2);

        void didPressBotButton(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton);

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported);

        void needOpenUserProfile(long j);

        /* renamed from: org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$-CC */
        /* loaded from: classes4.dex */
        public final /* synthetic */ class CC {
            public static void $default$didClickImage(ChatActionCellDelegate _this, ChatActionCell cell) {
            }

            public static boolean $default$didLongPress(ChatActionCellDelegate _this, ChatActionCell cell, float x, float y) {
                return false;
            }

            public static void $default$needOpenUserProfile(ChatActionCellDelegate _this, long uid) {
            }

            public static void $default$didPressBotButton(ChatActionCellDelegate _this, MessageObject messageObject, TLRPC.KeyboardButton button) {
            }

            public static void $default$didPressReplyMessage(ChatActionCellDelegate _this, ChatActionCell cell, int id) {
            }

            public static void $default$needOpenInviteLink(ChatActionCellDelegate _this, TLRPC.TL_chatInviteExported invite) {
            }
        }
    }

    public ChatActionCell(Context context) {
        this(context, false, null);
    }

    public ChatActionCell(Context context, boolean canDrawInParent, ThemeDelegate themeDelegate) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.spoilers = new ArrayList();
        this.spoilersPool = new Stack<>();
        this.lineWidths = new ArrayList<>();
        this.lineHeights = new ArrayList<>();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.canDrawInParent = canDrawInParent;
        this.themeDelegate = themeDelegate;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        this.avatarDrawable = new AvatarDrawable();
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
    }

    public void setDelegate(ChatActionCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void setCustomDate(int date, boolean scheduled, boolean inLayout) {
        CharSequence newText;
        int i = this.customDate;
        if (i == date || i / 3600 == date / 3600) {
            return;
        }
        if (scheduled) {
            if (date == 2147483646) {
                newText = LocaleController.getString("MessageScheduledUntilOnline", R.string.MessageScheduledUntilOnline);
            } else {
                newText = LocaleController.formatString("MessageScheduledOn", R.string.MessageScheduledOn, LocaleController.formatDateChat(date));
            }
        } else {
            newText = LocaleController.formatDateChat(date);
        }
        this.customDate = date;
        CharSequence charSequence = this.customText;
        if (charSequence != null && TextUtils.equals(newText, charSequence)) {
            return;
        }
        this.customText = newText;
        this.accessibilityText = null;
        updateTextInternal(inLayout);
    }

    private void updateTextInternal(boolean inLayout) {
        if (getMeasuredWidth() != 0) {
            createLayout(this.customText, getMeasuredWidth());
            invalidate();
        }
        if (!this.wasLayout) {
            if (inLayout) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActionCell.this.requestLayout();
                    }
                });
                return;
            } else {
                requestLayout();
                return;
            }
        }
        buildLayout();
    }

    public void setCustomText(CharSequence text) {
        this.customText = text;
        if (text != null) {
            updateTextInternal(false);
        }
    }

    public void setOverrideColor(String background, String text) {
        this.overrideBackground = background;
        this.overrideText = text;
    }

    public void setMessageObject(MessageObject messageObject) {
        StaticLayout staticLayout;
        if (this.currentMessageObject != messageObject || (((staticLayout = this.textLayout) != null && !TextUtils.equals(staticLayout.getText(), messageObject.messageText)) || (!this.hasReplyMessage && messageObject.replyMessageObject != null))) {
            this.accessibilityText = null;
            this.currentMessageObject = messageObject;
            if (messageObject != null && messageObject.viewRef != null && (this.currentMessageObject.viewRef.get() == null || this.currentMessageObject.viewRef.get().get() != this)) {
                this.currentMessageObject.viewRef.set(new WeakReference<>(this));
            }
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
            if (this.currentMessageObject.type != 11) {
                this.imageReceiver.setImageBitmap((Bitmap) null);
            } else {
                long id = messageObject.getDialogId();
                this.avatarDrawable.setInfo(id, null, null);
                if (this.currentMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                    this.imageReceiver.setImage(null, null, this.avatarDrawable, null, this.currentMessageObject, 0);
                } else {
                    TLRPC.PhotoSize strippedPhotoSize = null;
                    int a = 0;
                    int N = this.currentMessageObject.photoThumbs.size();
                    while (true) {
                        if (a >= N) {
                            break;
                        }
                        TLRPC.PhotoSize photoSize = this.currentMessageObject.photoThumbs.get(a);
                        if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                            a++;
                        } else {
                            strippedPhotoSize = photoSize;
                            break;
                        }
                    }
                    TLRPC.PhotoSize photoSize2 = FileLoader.getClosestPhotoSizeWithSize(this.currentMessageObject.photoThumbs, 640);
                    if (photoSize2 == null) {
                        this.imageReceiver.setImageBitmap(this.avatarDrawable);
                    } else {
                        TLRPC.Photo photo = messageObject.messageOwner.action.photo;
                        TLRPC.VideoSize videoSize = null;
                        if (!photo.video_sizes.isEmpty() && SharedConfig.autoplayGifs) {
                            videoSize = photo.video_sizes.get(0);
                            if (!messageObject.mediaExists && !DownloadController.getInstance(this.currentAccount).canDownloadMedia(4, videoSize.size)) {
                                this.currentVideoLocation = ImageLocation.getForPhoto(videoSize, photo);
                                String fileName = FileLoader.getAttachFileName(videoSize);
                                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                                videoSize = null;
                            }
                        }
                        if (videoSize != null) {
                            this.imageReceiver.setImage(ImageLocation.getForPhoto(videoSize, photo), ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(strippedPhotoSize, this.currentMessageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, this.currentMessageObject, 1);
                        } else {
                            this.imageReceiver.setImage(ImageLocation.getForObject(photoSize2, this.currentMessageObject.photoThumbsObject), "150_150", ImageLocation.getForObject(strippedPhotoSize, this.currentMessageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, this.currentMessageObject, 1);
                        }
                    }
                }
                this.imageReceiver.setVisible(!PhotoViewer.isShowingImage(this.currentMessageObject), false);
            }
            requestLayout();
        }
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    public void setVisiblePart(float visibleTop, int parentH) {
        this.visiblePartSet = true;
        this.backgroundHeight = parentH;
        this.viewTop = visibleTop;
    }

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean onLongPress() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            return chatActionCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.imageReceiver.onDetachedFromWindow();
        this.wasLayout = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        this.lastTouchX = x;
        float y = event.getY();
        this.lastTouchY = y;
        boolean result = false;
        if (event.getAction() == 0) {
            if (this.delegate != null) {
                if (this.currentMessageObject.type == 11 && this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = true;
                    result = true;
                }
                if (result) {
                    startCheckLongPress();
                }
            }
        } else {
            if (event.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.imagePressed) {
                if (event.getAction() == 1) {
                    this.imagePressed = false;
                    ChatActionCellDelegate chatActionCellDelegate = this.delegate;
                    if (chatActionCellDelegate != null) {
                        chatActionCellDelegate.didClickImage(this);
                        playSoundEffect(0);
                    }
                } else if (event.getAction() == 3) {
                    this.imagePressed = false;
                } else if (event.getAction() == 2 && !this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = false;
                }
            }
        }
        if (!result && (event.getAction() == 0 || (this.pressedLink != null && event.getAction() == 1))) {
            int i = this.textX;
            if (x >= i) {
                int i2 = this.textY;
                if (y >= i2 && x <= i + this.textWidth && y <= this.textHeight + i2) {
                    float x2 = x - this.textXLeft;
                    int line = this.textLayout.getLineForVertical((int) (y - i2));
                    int off = this.textLayout.getOffsetForHorizontal(line, x2);
                    float left = this.textLayout.getLineLeft(line);
                    if (left <= x2 && this.textLayout.getLineWidth(line) + left >= x2 && (this.currentMessageObject.messageText instanceof Spannable)) {
                        Spannable buffer = (Spannable) this.currentMessageObject.messageText;
                        URLSpan[] link = (URLSpan[]) buffer.getSpans(off, off, URLSpan.class);
                        if (link.length != 0) {
                            if (event.getAction() == 0) {
                                this.pressedLink = link[0];
                                result = true;
                            } else {
                                URLSpan uRLSpan = link[0];
                                URLSpan uRLSpan2 = this.pressedLink;
                                if (uRLSpan == uRLSpan2) {
                                    openLink(uRLSpan2);
                                    result = true;
                                }
                            }
                        } else {
                            this.pressedLink = null;
                        }
                    } else {
                        this.pressedLink = null;
                    }
                }
            }
            this.pressedLink = null;
        }
        if (!result) {
            return super.onTouchEvent(event);
        }
        return result;
    }

    public void openLink(CharacterStyle link) {
        if (this.delegate != null && (link instanceof URLSpan)) {
            String url = ((URLSpan) link).getURL();
            if (url.startsWith("invite")) {
                URLSpan uRLSpan = this.pressedLink;
                if (uRLSpan instanceof URLSpanNoUnderline) {
                    URLSpanNoUnderline spanNoUnderline = (URLSpanNoUnderline) uRLSpan;
                    TLObject object = spanNoUnderline.getObject();
                    if (object instanceof TLRPC.TL_chatInviteExported) {
                        TLRPC.TL_chatInviteExported invite = (TLRPC.TL_chatInviteExported) object;
                        this.delegate.needOpenInviteLink(invite);
                        return;
                    }
                    return;
                }
            }
            if (url.startsWith("game")) {
                this.delegate.didPressReplyMessage(this, this.currentMessageObject.getReplyMsgId());
            } else if (url.startsWith("http")) {
                Browser.openUrl(getContext(), url);
            } else {
                this.delegate.needOpenUserProfile(Long.parseLong(url));
            }
        }
    }

    private void createLayout(CharSequence text, int width) {
        int maxWidth = width - AndroidUtilities.dp(30.0f);
        this.invalidatePath = true;
        this.textLayout = new StaticLayout(text, (TextPaint) getThemedPaint(Theme.key_paint_chatActionText), maxWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        if (text instanceof Spannable) {
            SpoilerEffect.addSpoilers(this, this.textLayout, (Spannable) text, this.spoilersPool, this.spoilers);
        }
        this.textHeight = 0;
        this.textWidth = 0;
        try {
            int linesCount = this.textLayout.getLineCount();
            for (int a = 0; a < linesCount; a++) {
                try {
                    float lineWidth = this.textLayout.getLineWidth(a);
                    if (lineWidth > maxWidth) {
                        lineWidth = maxWidth;
                    }
                    this.textHeight = (int) Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(a)));
                    this.textWidth = (int) Math.max(this.textWidth, Math.ceil(lineWidth));
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.textX = (width - this.textWidth) / 2;
        this.textY = AndroidUtilities.dp(7.0f);
        this.textXLeft = (width - this.textLayout.getWidth()) / 2;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.currentMessageObject == null && this.customText == null) {
            setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        int width = Math.max(AndroidUtilities.dp(30.0f), View.MeasureSpec.getSize(widthMeasureSpec));
        if (this.previousWidth != width) {
            this.wasLayout = true;
            this.previousWidth = width;
            buildLayout();
        }
        int i = this.textHeight;
        MessageObject messageObject = this.currentMessageObject;
        setMeasuredDimension(width, i + ((messageObject == null || messageObject.type != 11) ? 0 : AndroidUtilities.roundMessageSize + AndroidUtilities.dp(10.0f)) + AndroidUtilities.dp(14.0f));
    }

    private void buildLayout() {
        CharSequence text;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            if (messageObject.messageOwner != null && this.currentMessageObject.messageOwner.media != null && this.currentMessageObject.messageOwner.media.ttl_seconds != 0) {
                if (this.currentMessageObject.messageOwner.media.photo instanceof TLRPC.TL_photoEmpty) {
                    text = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                } else if (this.currentMessageObject.messageOwner.media.document instanceof TLRPC.TL_documentEmpty) {
                    text = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                } else {
                    text = this.currentMessageObject.messageText;
                }
            } else {
                text = this.currentMessageObject.messageText;
            }
        } else {
            text = this.customText;
        }
        createLayout(text, this.previousWidth);
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 != null && messageObject2.type == 11) {
            this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2, this.textHeight + AndroidUtilities.dp(19.0f), AndroidUtilities.roundMessageSize, AndroidUtilities.roundMessageSize);
        }
    }

    public int getCustomDate() {
        return this.customDate;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 11) {
            this.imageReceiver.draw(canvas);
        }
        if (this.textLayout == null) {
            return;
        }
        drawBackground(canvas, false);
        if (this.textPaint != null) {
            canvas.save();
            canvas.translate(this.textXLeft, this.textY);
            if (this.textLayout.getPaint() != this.textPaint) {
                buildLayout();
            }
            canvas.save();
            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
            this.textLayout.draw(canvas);
            canvas.restore();
            for (SpoilerEffect eff : this.spoilers) {
                eff.setColor(this.textLayout.getPaint().getColor());
                eff.draw(canvas);
            }
            canvas.restore();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x036f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x024b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawBackground(Canvas canvas, boolean fromParent) {
        Paint backgroundPaint;
        Paint backgroundPaint2;
        int innerCornerRad;
        Paint backgroundPaint3;
        int nextLineWidth;
        int innerCornerRad2;
        int nextLineWidth2;
        int prevLineWidth;
        int x;
        int cornerInSmall;
        int cornerIn;
        int nextLineWidth3;
        int diff;
        if (this.canDrawInParent) {
            if (hasGradientService() && !fromParent) {
                return;
            }
            if (!hasGradientService() && fromParent) {
                return;
            }
        }
        Paint backgroundPaint4 = getThemedPaint(Theme.key_paint_chatActionBackground);
        this.textPaint = (TextPaint) getThemedPaint(Theme.key_paint_chatActionText);
        String str = this.overrideBackground;
        if (str != null) {
            int color = getThemedColor(str);
            if (this.overrideBackgroundPaint == null) {
                Paint paint = new Paint(1);
                this.overrideBackgroundPaint = paint;
                paint.setColor(color);
                TextPaint textPaint = new TextPaint(1);
                this.overrideTextPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.overrideTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                this.overrideTextPaint.setColor(getThemedColor(this.overrideText));
            }
            backgroundPaint4 = this.overrideBackgroundPaint;
            this.textPaint = this.overrideTextPaint;
        }
        if (this.invalidatePath) {
            this.invalidatePath = false;
            this.lineWidths.clear();
            int count = this.textLayout.getLineCount();
            int corner = AndroidUtilities.dp(11.0f);
            int cornerIn2 = AndroidUtilities.dp(8.0f);
            int prevLineWidth2 = 0;
            for (int a = 0; a < count; a++) {
                int lineWidth = (int) Math.ceil(this.textLayout.getLineWidth(a));
                if (a != 0 && (diff = prevLineWidth2 - lineWidth) > 0 && diff <= corner + cornerIn2) {
                    lineWidth = prevLineWidth2;
                }
                this.lineWidths.add(Integer.valueOf(lineWidth));
                prevLineWidth2 = lineWidth;
            }
            for (int a2 = count - 2; a2 >= 0; a2--) {
                int lineWidth2 = this.lineWidths.get(a2).intValue();
                int diff2 = prevLineWidth2 - lineWidth2;
                if (diff2 > 0 && diff2 <= corner + cornerIn2) {
                    lineWidth2 = prevLineWidth2;
                }
                this.lineWidths.set(a2, Integer.valueOf(lineWidth2));
                prevLineWidth2 = lineWidth2;
            }
            int y = AndroidUtilities.dp(4.0f);
            int x2 = getMeasuredWidth() / 2;
            int previousLineBottom = 0;
            int cornerOffset = AndroidUtilities.dp(3.0f);
            int cornerInSmall2 = AndroidUtilities.dp(6.0f);
            int cornerRest = corner - cornerOffset;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            this.backgroundPath.moveTo(x2, y);
            int a3 = 0;
            while (a3 < count) {
                int lineWidth3 = this.lineWidths.get(a3).intValue();
                int lineBottom = this.textLayout.getLineBottom(a3);
                if (a3 < count - 1) {
                    backgroundPaint3 = backgroundPaint4;
                    nextLineWidth = this.lineWidths.get(a3 + 1).intValue();
                } else {
                    backgroundPaint3 = backgroundPaint4;
                    nextLineWidth = 0;
                }
                int height = lineBottom - previousLineBottom;
                if (a3 == 0 || lineWidth3 > prevLineWidth2) {
                    height += AndroidUtilities.dp(3.0f);
                }
                int previousLineBottom2 = count - 1;
                if (a3 == previousLineBottom2 || lineWidth3 > nextLineWidth) {
                    height += AndroidUtilities.dp(3.0f);
                }
                float startX = x2 + (lineWidth3 / 2.0f);
                if (a3 != count - 1 && lineWidth3 < nextLineWidth && a3 != 0 && lineWidth3 < prevLineWidth2) {
                    innerCornerRad2 = cornerInSmall2;
                } else {
                    innerCornerRad2 = cornerIn2;
                }
                if (a3 == 0) {
                    nextLineWidth2 = nextLineWidth;
                    cornerIn = cornerIn2;
                    prevLineWidth = prevLineWidth2;
                    x = x2;
                    cornerInSmall = cornerInSmall2;
                } else if (lineWidth3 > prevLineWidth2) {
                    nextLineWidth2 = nextLineWidth;
                    cornerIn = cornerIn2;
                    prevLineWidth = prevLineWidth2;
                    x = x2;
                    cornerInSmall = cornerInSmall2;
                } else {
                    if (lineWidth3 >= prevLineWidth2) {
                        nextLineWidth2 = nextLineWidth;
                        cornerIn = cornerIn2;
                        prevLineWidth = prevLineWidth2;
                        x = x2;
                        cornerInSmall = cornerInSmall2;
                    } else {
                        cornerIn = cornerIn2;
                        cornerInSmall = cornerInSmall2;
                        x = x2;
                        prevLineWidth = prevLineWidth2;
                        nextLineWidth2 = nextLineWidth;
                        this.rect.set(cornerRest + startX, y, cornerRest + startX + (innerCornerRad2 * 2), (innerCornerRad2 * 2) + y);
                        this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                    }
                    y += height;
                    if (a3 == count - 1) {
                        nextLineWidth3 = nextLineWidth2;
                        if (lineWidth3 < nextLineWidth3) {
                            y -= AndroidUtilities.dp(3.0f);
                            height -= AndroidUtilities.dp(3.0f);
                        }
                    } else {
                        nextLineWidth3 = nextLineWidth2;
                    }
                    if (a3 != 0 && lineWidth3 < prevLineWidth) {
                        y -= AndroidUtilities.dp(3.0f);
                        height -= AndroidUtilities.dp(3.0f);
                    }
                    this.lineHeights.add(Integer.valueOf(height));
                    if (a3 != count - 1 && lineWidth3 <= nextLineWidth3) {
                        if (lineWidth3 >= nextLineWidth3) {
                            int yOffset = y - (innerCornerRad2 * 2);
                            this.rect.set(cornerRest + startX, yOffset, cornerRest + startX + (innerCornerRad2 * 2), y);
                            this.backgroundPath.arcTo(this.rect, 180.0f, -90.0f);
                        }
                        prevLineWidth2 = lineWidth3;
                        a3++;
                        backgroundPaint4 = backgroundPaint3;
                        previousLineBottom = lineBottom;
                        cornerIn2 = cornerIn;
                        cornerInSmall2 = cornerInSmall;
                        x2 = x;
                    }
                    this.rect.set((startX - cornerOffset) - corner, y - (corner * 2), cornerRest + startX, y);
                    this.backgroundPath.arcTo(this.rect, 0.0f, 90.0f);
                    prevLineWidth2 = lineWidth3;
                    a3++;
                    backgroundPaint4 = backgroundPaint3;
                    previousLineBottom = lineBottom;
                    cornerIn2 = cornerIn;
                    cornerInSmall2 = cornerInSmall;
                    x2 = x;
                }
                this.rect.set((startX - cornerOffset) - corner, y, cornerRest + startX, (corner * 2) + y);
                this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                y += height;
                if (a3 == count - 1) {
                }
                if (a3 != 0) {
                    y -= AndroidUtilities.dp(3.0f);
                    height -= AndroidUtilities.dp(3.0f);
                }
                this.lineHeights.add(Integer.valueOf(height));
                if (a3 != count - 1) {
                    if (lineWidth3 >= nextLineWidth3) {
                    }
                    prevLineWidth2 = lineWidth3;
                    a3++;
                    backgroundPaint4 = backgroundPaint3;
                    previousLineBottom = lineBottom;
                    cornerIn2 = cornerIn;
                    cornerInSmall2 = cornerInSmall;
                    x2 = x;
                }
                this.rect.set((startX - cornerOffset) - corner, y - (corner * 2), cornerRest + startX, y);
                this.backgroundPath.arcTo(this.rect, 0.0f, 90.0f);
                prevLineWidth2 = lineWidth3;
                a3++;
                backgroundPaint4 = backgroundPaint3;
                previousLineBottom = lineBottom;
                cornerIn2 = cornerIn;
                cornerInSmall2 = cornerInSmall;
                x2 = x;
            }
            backgroundPaint = backgroundPaint4;
            int cornerIn3 = cornerIn2;
            int x3 = x2;
            int cornerInSmall3 = cornerInSmall2;
            int a4 = count - 1;
            while (a4 >= 0) {
                int prevLineWidth3 = a4 != 0 ? this.lineWidths.get(a4 - 1).intValue() : 0;
                int lineWidth4 = this.lineWidths.get(a4).intValue();
                int nextLineWidth4 = a4 != count + (-1) ? this.lineWidths.get(a4 + 1).intValue() : 0;
                this.textLayout.getLineBottom(a4);
                float startX2 = x3 - (lineWidth4 / 2);
                if (a4 != count - 1 && lineWidth4 < nextLineWidth4 && a4 != 0 && lineWidth4 < prevLineWidth3) {
                    innerCornerRad = cornerInSmall3;
                } else {
                    innerCornerRad = cornerIn3;
                }
                if (a4 != count - 1 && lineWidth4 <= nextLineWidth4) {
                    if (lineWidth4 < nextLineWidth4) {
                        this.rect.set((startX2 - cornerRest) - (innerCornerRad * 2), y - (innerCornerRad * 2), startX2 - cornerRest, y);
                        this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                    }
                    y -= this.lineHeights.get(a4).intValue();
                    if (a4 != 0 && lineWidth4 <= prevLineWidth3) {
                        if (lineWidth4 >= prevLineWidth3) {
                            this.rect.set((startX2 - cornerRest) - (innerCornerRad * 2), y, startX2 - cornerRest, (innerCornerRad * 2) + y);
                            this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                        }
                        a4--;
                    }
                    this.rect.set(startX2 - cornerRest, y, cornerOffset + startX2 + corner, (corner * 2) + y);
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                    a4--;
                }
                this.rect.set(startX2 - cornerRest, y - (corner * 2), cornerOffset + startX2 + corner, y);
                this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                y -= this.lineHeights.get(a4).intValue();
                if (a4 != 0) {
                    if (lineWidth4 >= prevLineWidth3) {
                    }
                    a4--;
                }
                this.rect.set(startX2 - cornerRest, y, cornerOffset + startX2 + corner, (corner * 2) + y);
                this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                a4--;
            }
            this.backgroundPath.close();
        } else {
            backgroundPaint = backgroundPaint4;
        }
        if (!this.visiblePartSet) {
            ViewGroup parent = (ViewGroup) getParent();
            this.backgroundHeight = parent.getMeasuredHeight();
        }
        ThemeDelegate themeDelegate = this.themeDelegate;
        if (themeDelegate != null) {
            themeDelegate.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, 0.0f, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, 0.0f, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        int oldAlpha = -1;
        int oldAlpha2 = -1;
        if (!fromParent || getAlpha() == 1.0f) {
            backgroundPaint2 = backgroundPaint;
        } else {
            oldAlpha = backgroundPaint.getAlpha();
            oldAlpha2 = Theme.chat_actionBackgroundGradientDarkenPaint.getAlpha();
            backgroundPaint2 = backgroundPaint;
            backgroundPaint2.setAlpha((int) (oldAlpha * getAlpha()));
            Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha((int) (oldAlpha2 * getAlpha()));
        }
        canvas.drawPath(this.backgroundPath, backgroundPaint2);
        if (hasGradientService()) {
            canvas.drawPath(this.backgroundPath, Theme.chat_actionBackgroundGradientDarkenPaint);
        }
        if (oldAlpha >= 0) {
            backgroundPaint2.setAlpha(oldAlpha);
            Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha(oldAlpha2);
        }
    }

    public boolean hasGradientService() {
        ThemeDelegate themeDelegate;
        return this.overrideBackgroundPaint == null && ((themeDelegate = this.themeDelegate) == null ? Theme.hasGradientService() : themeDelegate.hasGradientService());
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String fileName, boolean canceled) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String fileName) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 11) {
            TLRPC.PhotoSize strippedPhotoSize = null;
            int a = 0;
            int N = this.currentMessageObject.photoThumbs.size();
            while (true) {
                if (a >= N) {
                    break;
                }
                TLRPC.PhotoSize photoSize = this.currentMessageObject.photoThumbs.get(a);
                if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                    a++;
                } else {
                    strippedPhotoSize = photoSize;
                    break;
                }
            }
            this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(strippedPhotoSize, this.currentMessageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, this.currentMessageObject, 1);
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String fileName, long downloadSize, long totalSize) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String fileName, long downloadSize, long totalSize, boolean isEncrypted) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (TextUtils.isEmpty(this.customText) && this.currentMessageObject == null) {
            return;
        }
        if (this.accessibilityText == null) {
            CharSequence text = !TextUtils.isEmpty(this.customText) ? this.customText : this.currentMessageObject.messageText;
            SpannableStringBuilder sb = new SpannableStringBuilder(text);
            CharacterStyle[] links = (CharacterStyle[]) sb.getSpans(0, sb.length(), ClickableSpan.class);
            for (final CharacterStyle link : links) {
                int start = sb.getSpanStart(link);
                int end = sb.getSpanEnd(link);
                sb.removeSpan(link);
                ClickableSpan underlineSpan = new ClickableSpan() { // from class: org.telegram.ui.Cells.ChatActionCell.1
                    {
                        ChatActionCell.this = this;
                    }

                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        if (ChatActionCell.this.delegate != null) {
                            ChatActionCell.this.openLink(link);
                        }
                    }
                };
                sb.setSpan(underlineSpan, start, end, 33);
            }
            this.accessibilityText = sb;
        }
        if (Build.VERSION.SDK_INT < 24) {
            info.setContentDescription(this.accessibilityText.toString());
        } else {
            info.setText(this.accessibilityText);
        }
        info.setEnabled(true);
    }

    public void setInvalidateColors(boolean invalidate) {
        if (this.invalidateColors == invalidate) {
            return;
        }
        this.invalidateColors = invalidate;
        invalidate();
    }

    private int getThemedColor(String key) {
        ThemeDelegate themeDelegate = this.themeDelegate;
        Integer color = themeDelegate != null ? themeDelegate.getColor(key) : null;
        return color != null ? color.intValue() : Theme.getColor(key);
    }

    private Paint getThemedPaint(String paintKey) {
        ThemeDelegate themeDelegate = this.themeDelegate;
        Paint paint = themeDelegate != null ? themeDelegate.getPaint(paintKey) : null;
        return paint != null ? paint : Theme.getThemePaint(paintKey);
    }
}
