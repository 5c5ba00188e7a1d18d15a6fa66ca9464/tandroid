package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.PhotoViewer;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes4.dex */
public class ContextLinkCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
    private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5f);
    public final Property IMAGE_SCALE;
    private int TAG;
    private AnimatorSet animator;
    private Paint backgroundPaint;
    private ButtonBounce buttonBounce;
    private boolean buttonPressed;
    private int buttonState;
    File cacheFile;
    private boolean canPreviewGif;
    private CheckBox2 checkBox;
    private int currentAccount;
    private int currentDate;
    private MessageObject currentMessageObject;
    private TLRPC.PhotoSize currentPhotoObject;
    private ContextLinkCellDelegate delegate;
    private StaticLayout descriptionLayout;
    private int descriptionY;
    private TLRPC.Document documentAttach;
    private int documentAttachType;
    private boolean drawLinkImageView;
    boolean fileExist;
    String fileName;
    private float imageScale;
    private TLRPC.User inlineBot;
    private TLRPC.BotInlineResult inlineResult;
    private boolean isForceGif;
    private boolean isKeyboard;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private StaticLayout linkLayout;
    private int linkY;
    private boolean mediaWebpage;
    private boolean needDivider;
    private boolean needShadow;
    private Object parentObject;
    private TLRPC.Photo photoAttach;
    private RadialProgress2 radialProgress;
    int resolveFileNameId;
    boolean resolvingFileName;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean scaled;
    private StaticLayout titleLayout;
    private int titleY;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 1 implements Runnable {
        final /* synthetic */ boolean val$ifSame;
        final /* synthetic */ int val$localId;

        1(int i, boolean z) {
            this.val$localId = i;
            this.val$ifSame = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i, String str, File file, boolean z, boolean z2) {
            ContextLinkCell contextLinkCell = ContextLinkCell.this;
            contextLinkCell.resolvingFileName = false;
            if (contextLinkCell.resolveFileNameId == i) {
                contextLinkCell.fileName = str;
                if (str == null) {
                    contextLinkCell.fileName = "";
                }
                contextLinkCell.cacheFile = file;
                contextLinkCell.fileExist = z;
            }
            contextLinkCell.updateButtonState(z2, true);
        }

        @Override // java.lang.Runnable
        public void run() {
            File file;
            final File file2;
            final String str;
            String attachFileName;
            FileLoader fileLoader;
            TLObject tLObject;
            File file3;
            File file4;
            String str2 = null;
            if (ContextLinkCell.this.documentAttachType == 5 || ContextLinkCell.this.documentAttachType == 3) {
                if (ContextLinkCell.this.documentAttach != null) {
                    str2 = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                    file = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.documentAttach);
                } else {
                    if (ContextLinkCell.this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(Utilities.MD5(ContextLinkCell.this.inlineResult.content.url));
                        sb.append(".");
                        sb.append(ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, ContextLinkCell.this.documentAttachType == 5 ? "mp3" : "ogg"));
                        str2 = sb.toString();
                        file = new File(FileLoader.getDirectory(4), str2);
                    }
                    str = null;
                    file2 = null;
                }
                file2 = file;
                str = str2;
            } else {
                if (ContextLinkCell.this.mediaWebpage) {
                    if (ContextLinkCell.this.inlineResult != null) {
                        if (ContextLinkCell.this.inlineResult.document instanceof TLRPC.TL_document) {
                            attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.inlineResult.document);
                            fileLoader = FileLoader.getInstance(ContextLinkCell.this.currentAccount);
                            tLObject = ContextLinkCell.this.inlineResult.document;
                        } else if (ContextLinkCell.this.inlineResult.photo instanceof TLRPC.TL_photo) {
                            ContextLinkCell contextLinkCell = ContextLinkCell.this;
                            contextLinkCell.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(contextLinkCell.inlineResult.photo.sizes, AndroidUtilities.getPhotoSize(), true);
                            attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.currentPhotoObject);
                            fileLoader = FileLoader.getInstance(ContextLinkCell.this.currentAccount);
                            tLObject = ContextLinkCell.this.currentPhotoObject;
                        } else {
                            if (ContextLinkCell.this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                                attachFileName = Utilities.MD5(ContextLinkCell.this.inlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.content.mime_type));
                                file4 = new File(FileLoader.getDirectory(4), attachFileName);
                                if (ContextLinkCell.this.documentAttachType == 2 && (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC.TL_webDocument) && "video/mp4".equals(ContextLinkCell.this.inlineResult.thumb.mime_type)) {
                                    file3 = file4;
                                    attachFileName = null;
                                }
                            } else {
                                if (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC.TL_webDocument) {
                                    attachFileName = Utilities.MD5(ContextLinkCell.this.inlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.thumb.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.thumb.mime_type));
                                    file4 = new File(FileLoader.getDirectory(4), attachFileName);
                                }
                                attachFileName = null;
                                file3 = null;
                            }
                            file3 = file4;
                        }
                        file3 = fileLoader.getPathToAttach(tLObject);
                    } else {
                        if (ContextLinkCell.this.documentAttach != null) {
                            attachFileName = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                            fileLoader = FileLoader.getInstance(ContextLinkCell.this.currentAccount);
                            tLObject = ContextLinkCell.this.documentAttach;
                            file3 = fileLoader.getPathToAttach(tLObject);
                        }
                        attachFileName = null;
                        file3 = null;
                    }
                    if (ContextLinkCell.this.documentAttach == null || ContextLinkCell.this.documentAttachType != 2 || MessageObject.getDocumentVideoThumb(ContextLinkCell.this.documentAttach) == null) {
                        str = attachFileName;
                        file2 = file3;
                    } else {
                        file2 = file3;
                        str = str2;
                    }
                }
                str = null;
                file2 = null;
            }
            final boolean z = !TextUtils.isEmpty(str) && file2.exists();
            final int i = this.val$localId;
            final boolean z2 = this.val$ifSame;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ContextLinkCell$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContextLinkCell.1.this.lambda$run$0(i, str, file2, z, z2);
                }
            });
        }
    }

    /* loaded from: classes4.dex */
    public interface ContextLinkCellDelegate {
        void didPressedImage(ContextLinkCell contextLinkCell);
    }

    public ContextLinkCell(Context context) {
        this(context, false, null);
    }

    public ContextLinkCell(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.titleY = AndroidUtilities.dp(7.0f);
        this.descriptionY = AndroidUtilities.dp(27.0f);
        this.cacheFile = null;
        this.imageScale = 1.0f;
        this.IMAGE_SCALE = new AnimationProperties.FloatProperty("animationValue") { // from class: org.telegram.ui.Cells.ContextLinkCell.2
            @Override // android.util.Property
            public Float get(ContextLinkCell contextLinkCell) {
                return Float.valueOf(ContextLinkCell.this.imageScale);
            }

            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ContextLinkCell contextLinkCell, float f) {
                ContextLinkCell.this.imageScale = f;
                ContextLinkCell.this.invalidate();
            }
        };
        this.resourcesProvider = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        this.linkImageView.setLayerNum(1);
        this.linkImageView.setUseSharedAnimationQueue(true);
        this.letterDrawable = new LetterDrawable(resourcesProvider, 0);
        this.radialProgress = new RadialProgress2(this);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        setFocusable(true);
        if (z) {
            Paint paint = new Paint();
            this.backgroundPaint = paint;
            int i = Theme.key_sharedMedia_photoPlaceholder;
            paint.setColor(Theme.getColor(i, resourcesProvider));
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setVisibility(4);
            this.checkBox.setColor(-1, i, Theme.key_checkboxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(1);
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 1.0f, 1.0f, 0.0f));
        }
        setWillNotDraw(false);
    }

    private void didPressedButton() {
        int i;
        int i2 = this.documentAttachType;
        if (i2 == 3 || i2 == 5) {
            int i3 = this.buttonState;
            if (i3 == 0) {
                if (!MediaController.getInstance().playMessage(this.currentMessageObject)) {
                    return;
                } else {
                    i = 1;
                }
            } else if (i3 != 1) {
                i = 4;
                if (i3 == 2) {
                    this.radialProgress.setProgress(0.0f, false);
                    if (this.documentAttach != null) {
                        FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.inlineResult, 1, 0);
                    } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                        FileLoader.getInstance(this.currentAccount).loadFile(WebFile.createWithWebDocument(this.inlineResult.content), 3, 1);
                    }
                } else {
                    if (i3 != 4) {
                        return;
                    }
                    if (this.documentAttach != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                    } else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(WebFile.createWithWebDocument(this.inlineResult.content));
                    }
                    i = 2;
                }
            } else if (!MediaController.getInstance().lambda$startAudioAgain$7(this.currentMessageObject)) {
                return;
            } else {
                i = 0;
            }
            this.buttonState = i;
            this.radialProgress.setIcon(getIconForCurrentState(), false, true);
            invalidate();
        }
    }

    private int getIconForCurrentState() {
        int i = this.documentAttachType;
        if (i != 3 && i != 5) {
            this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
            return this.buttonState == 1 ? 10 : 4;
        }
        this.radialProgress.setColorKeys(Theme.key_chat_inLoader, Theme.key_chat_inLoaderSelected, Theme.key_chat_inMediaIcon, Theme.key_chat_inMediaIconSelected);
        int i2 = this.buttonState;
        if (i2 == 1) {
            return 1;
        }
        if (i2 == 2) {
            return 2;
        }
        return i2 == 4 ? 3 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0037, code lost:
    
        if (org.telegram.messenger.MessageObject.isVoiceDocument(r14.documentAttach) != false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x005f, code lost:
    
        r14.documentAttachType = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x005d, code lost:
    
        if (r14.inlineResult.type.equals("voice") != false) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setAttachType() {
        int i;
        this.currentMessageObject = null;
        this.documentAttachType = 0;
        TLRPC.Document document = this.documentAttach;
        if (document != null) {
            if (MessageObject.isGifDocument(document)) {
                i = 2;
            } else if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, true)) {
                i = 6;
            } else {
                if (!MessageObject.isMusicDocument(this.documentAttach)) {
                }
                this.documentAttachType = 5;
            }
            this.documentAttachType = i;
        } else {
            TLRPC.BotInlineResult botInlineResult = this.inlineResult;
            if (botInlineResult != null) {
                if (botInlineResult.photo != null) {
                    i = 7;
                    this.documentAttachType = i;
                } else {
                    if (!botInlineResult.type.equals(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                    }
                    this.documentAttachType = 5;
                }
            }
        }
        int i2 = this.documentAttachType;
        if (i2 == 3 || i2 == 5) {
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.out = true;
            tL_message.id = -Utilities.random.nextInt();
            tL_message.peer_id = new TLRPC.TL_peerUser();
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_message.from_id = tL_peerUser;
            TLRPC.Peer peer = tL_message.peer_id;
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            tL_peerUser.user_id = clientUserId;
            peer.user_id = clientUserId;
            tL_message.date = (int) (System.currentTimeMillis() / 1000);
            tL_message.message = "";
            TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
            tL_message.media = tL_messageMediaDocument;
            tL_messageMediaDocument.flags |= 3;
            tL_messageMediaDocument.document = new TLRPC.TL_document();
            TLRPC.MessageMedia messageMedia = tL_message.media;
            messageMedia.document.file_reference = new byte[0];
            tL_message.flags |= 768;
            TLRPC.Document document2 = this.documentAttach;
            if (document2 != null) {
                messageMedia.document = document2;
                tL_message.attachPath = "";
            } else {
                String httpUrlExtension = ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg");
                TLRPC.Document document3 = tL_message.media.document;
                document3.id = 0L;
                document3.access_hash = 0L;
                document3.date = tL_message.date;
                document3.mime_type = "audio/" + httpUrlExtension;
                TLRPC.Document document4 = tL_message.media.document;
                document4.size = 0L;
                document4.dc_id = 0;
                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                tL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(this.inlineResult);
                TLRPC.BotInlineResult botInlineResult2 = this.inlineResult;
                String str = botInlineResult2.title;
                if (str == null) {
                    str = "";
                }
                tL_documentAttributeAudio.title = str;
                String str2 = botInlineResult2.description;
                tL_documentAttributeAudio.performer = str2 != null ? str2 : "";
                tL_documentAttributeAudio.flags |= 3;
                if (this.documentAttachType == 3) {
                    tL_documentAttributeAudio.voice = true;
                }
                tL_message.media.document.attributes.add(tL_documentAttributeAudio);
                TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                StringBuilder sb = new StringBuilder();
                sb.append(Utilities.MD5(this.inlineResult.content.url));
                sb.append(".");
                sb.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                tL_documentAttributeFilename.file_name = sb.toString();
                tL_message.media.document.attributes.add(tL_documentAttributeFilename);
                File directory = FileLoader.getDirectory(4);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Utilities.MD5(this.inlineResult.content.url));
                sb2.append(".");
                sb2.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                tL_message.attachPath = new File(directory, sb2.toString()).getAbsolutePath();
            }
            this.currentMessageObject = new MessageObject(this.currentAccount, tL_message, false, true);
        }
    }

    public void allowButtonBounce(boolean z) {
        if (z != (this.buttonBounce != null)) {
            this.buttonBounce = z ? new ButtonBounce(this, 1.0f, 3.0f).setReleaseDelay(120L) : null;
        }
    }

    public TLRPC.BotInlineResult getBotInlineResult() {
        return this.inlineResult;
    }

    public int getDate() {
        return this.currentDate;
    }

    public TLRPC.Document getDocument() {
        return this.documentAttach;
    }

    public TLRPC.User getInlineBot() {
        return this.inlineBot;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    public Object getParentObject() {
        return this.parentObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.linkImageView;
    }

    public TLRPC.BotInlineResult getResult() {
        return this.inlineResult;
    }

    public boolean isGif() {
        return this.documentAttachType == 2 && this.canPreviewGif;
    }

    public boolean isSticker() {
        return this.documentAttachType == 6;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.linkImageView.onAttachedToWindow()) {
            updateButtonState(false, false);
        }
        this.radialProgress.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.linkImageView.onDetachedFromWindow();
        this.radialProgress.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x02f6  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0332  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0342  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x034a  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        Drawable drawable;
        float dp;
        float measuredHeight;
        float measuredWidth;
        float measuredHeight2;
        Paint paint;
        int i;
        LetterDrawable letterDrawable;
        int i2;
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null && (checkBox2.isChecked() || !this.linkImageView.hasBitmapImage() || this.linkImageView.getCurrentAlpha() != 1.0f || PhotoViewer.isShowingImage((MessageObject) this.parentObject))) {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
        }
        if (this.titleLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.linkLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY);
            this.linkLayout.draw(canvas);
            canvas.restore();
        }
        if (!this.mediaWebpage) {
            if (!this.drawLinkImageView || PhotoViewer.isShowingImage(this.inlineResult)) {
                letterDrawable = this.letterDrawable;
                i2 = NotificationCenter.notificationsCountUpdated;
            } else {
                letterDrawable = this.letterDrawable;
                i2 = (int) ((1.0f - this.linkImageView.getCurrentAlpha()) * 255.0f);
            }
            letterDrawable.setAlpha(i2);
            int i3 = this.documentAttachType;
            if (i3 == 3 || i3 == 5) {
                this.radialProgress.setProgressColor(Theme.getColor(this.buttonPressed ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress, this.resourcesProvider));
                this.radialProgress.draw(canvas);
            } else {
                TLRPC.BotInlineResult botInlineResult = this.inlineResult;
                if (botInlineResult == null || !botInlineResult.type.equals("file")) {
                    TLRPC.BotInlineResult botInlineResult2 = this.inlineResult;
                    if (botInlineResult2 == null || !(botInlineResult2.type.equals(MediaStreamTrack.AUDIO_TRACK_KIND) || this.inlineResult.type.equals("voice"))) {
                        TLRPC.BotInlineResult botInlineResult3 = this.inlineResult;
                        if (botInlineResult3 == null || !(botInlineResult3.type.equals("venue") || this.inlineResult.type.equals("geo"))) {
                            this.letterDrawable.draw(canvas);
                        } else {
                            int intrinsicWidth = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                            int intrinsicHeight = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                            int imageX = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth) / 2));
                            int imageY = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight) / 2));
                            canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                            Theme.chat_inlineResultLocation.setBounds(imageX, imageY, intrinsicWidth + imageX, intrinsicHeight + imageY);
                            drawable = Theme.chat_inlineResultLocation;
                        }
                    } else {
                        int intrinsicWidth2 = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                        int intrinsicHeight2 = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                        int imageX2 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth2) / 2));
                        int imageY2 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight2) / 2));
                        canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                        Theme.chat_inlineResultAudio.setBounds(imageX2, imageY2, intrinsicWidth2 + imageX2, intrinsicHeight2 + imageY2);
                        drawable = Theme.chat_inlineResultAudio;
                    }
                } else {
                    int intrinsicWidth3 = Theme.chat_inlineResultFile.getIntrinsicWidth();
                    int intrinsicHeight3 = Theme.chat_inlineResultFile.getIntrinsicHeight();
                    int imageX3 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth3) / 2));
                    int imageY3 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight3) / 2));
                    canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                    Theme.chat_inlineResultFile.setBounds(imageX3, imageY3, intrinsicWidth3 + imageX3, intrinsicHeight3 + imageY3);
                    drawable = Theme.chat_inlineResultFile;
                }
            }
            if (this.drawLinkImageView) {
                if (this.inlineResult != null) {
                    this.linkImageView.setVisible(!PhotoViewer.isShowingImage(r0), false);
                }
                canvas.save();
                float f = this.imageScale;
                ButtonBounce buttonBounce = this.buttonBounce;
                if (buttonBounce != null) {
                    f *= buttonBounce.getScale(0.1f);
                }
                canvas.scale(f, f, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                this.linkImageView.draw(canvas);
                canvas.restore();
            }
            if (this.mediaWebpage && ((i = this.documentAttachType) == 7 || i == 2)) {
                this.radialProgress.draw(canvas);
            }
            if (this.needDivider && !this.mediaWebpage) {
                if (LocaleController.isRTL) {
                    dp = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                    measuredHeight = getMeasuredHeight() - 1;
                    measuredWidth = getMeasuredWidth();
                    measuredHeight2 = getMeasuredHeight() - 1;
                    paint = Theme.dividerPaint;
                } else {
                    measuredHeight = getMeasuredHeight() - 1;
                    measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                    measuredHeight2 = getMeasuredHeight() - 1;
                    paint = Theme.dividerPaint;
                    dp = 0.0f;
                }
                canvas.drawLine(dp, measuredHeight, measuredWidth, measuredHeight2, paint);
            }
            if (this.needShadow) {
                Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(3.0f));
                Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(canvas);
                return;
            }
            return;
        }
        TLRPC.BotInlineResult botInlineResult4 = this.inlineResult;
        if (botInlineResult4 != null) {
            TLRPC.BotInlineMessage botInlineMessage = botInlineResult4.send_message;
            if ((botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaGeo) || (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue)) {
                int intrinsicWidth4 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                int intrinsicHeight4 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                int imageX4 = (int) (this.linkImageView.getImageX() + ((this.linkImageView.getImageWidth() - intrinsicWidth4) / 2.0f));
                int imageY4 = (int) (this.linkImageView.getImageY() + ((this.linkImageView.getImageHeight() - intrinsicHeight4) / 2.0f));
                canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + this.linkImageView.getImageWidth(), this.linkImageView.getImageY() + this.linkImageView.getImageHeight(), LetterDrawable.paint);
                Theme.chat_inlineResultLocation.setBounds(imageX4, imageY4, intrinsicWidth4 + imageX4, intrinsicHeight4 + imageY4);
                drawable = Theme.chat_inlineResultLocation;
            }
        }
        if (this.drawLinkImageView) {
        }
        if (this.mediaWebpage) {
            this.radialProgress.draw(canvas);
        }
        if (this.needDivider) {
            if (LocaleController.isRTL) {
            }
            canvas.drawLine(dp, measuredHeight, measuredWidth, measuredHeight2, paint);
        }
        if (this.needShadow) {
        }
        drawable.draw(canvas);
        if (this.drawLinkImageView) {
        }
        if (this.mediaWebpage) {
        }
        if (this.needDivider) {
        }
        if (this.needShadow) {
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        switch (this.documentAttachType) {
            case 1:
                i = R.string.AttachDocument;
                break;
            case 2:
                i = R.string.AttachGif;
                break;
            case 3:
                i = R.string.AttachAudio;
                break;
            case 4:
                i = R.string.AttachVideo;
                break;
            case 5:
                i = R.string.AttachMusic;
                break;
            case 6:
                i = R.string.AttachSticker;
                break;
            case 7:
                i = R.string.AttachPhoto;
                break;
            case 8:
                i = R.string.AttachLocation;
                break;
        }
        sb.append(LocaleController.getString(i));
        StaticLayout staticLayout = this.titleLayout;
        boolean z = (staticLayout == null || TextUtils.isEmpty(staticLayout.getText())) ? false : true;
        StaticLayout staticLayout2 = this.descriptionLayout;
        boolean z2 = (staticLayout2 == null || TextUtils.isEmpty(staticLayout2.getText())) ? false : true;
        if (this.documentAttachType == 5 && z && z2) {
            sb.append(", ");
            sb.append(LocaleController.formatString("AccDescrMusicInfo", R.string.AccDescrMusicInfo, this.descriptionLayout.getText(), this.titleLayout.getText()));
        } else {
            if (z) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(this.titleLayout.getText());
            }
            if (z2) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(this.descriptionLayout.getText());
            }
        }
        accessibilityNodeInfo.setText(sb);
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null || !checkBox2.isChecked()) {
            return;
        }
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:231:0x01e0, code lost:
    
        if (r0 == r43.currentPhotoObject) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x01aa, code lost:
    
        if (r0 != 3) goto L73;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0670  */
    /* JADX WARN: Removed duplicated region for block: B:104:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x05a2  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x031b  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x03ad  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0541  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x02e3  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x02ad  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0143 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x00e0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02b4  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0302 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0565  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        TLRPC.Document document;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        String str;
        TLRPC.BotInlineResult botInlineResult;
        String str2;
        WebFile webFile;
        int i3;
        int i4;
        TLRPC.PhotoSize photoSize;
        String str3;
        String str4;
        ImageLocation forWebFile;
        ImageLocation forPhoto;
        Object obj;
        long j;
        int i5;
        ImageReceiver imageReceiver;
        String str5;
        int i6;
        ImageReceiver imageReceiver2;
        String str6;
        long j2;
        String str7;
        Object obj2;
        Object obj3;
        String str8;
        float f;
        int dp;
        CheckBox2 checkBox2;
        TLRPC.TL_webDocument tL_webDocument;
        TLRPC.BotInlineMessage botInlineMessage;
        String str9;
        TLRPC.BotInlineResult botInlineResult2;
        ArrayList arrayList4;
        String str10;
        char c;
        char c2;
        int i7 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.linkLayout = null;
        this.currentPhotoObject = null;
        this.linkY = AndroidUtilities.dp(27.0f);
        if (this.inlineResult == null && this.documentAttach == null) {
            setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
            return;
        }
        int size = View.MeasureSpec.getSize(i);
        int dp2 = (size - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        if (this.documentAttach != null) {
            arrayList2 = new ArrayList(this.documentAttach.thumbs);
        } else {
            TLRPC.BotInlineResult botInlineResult3 = this.inlineResult;
            if (botInlineResult3 == null || botInlineResult3.photo == null) {
                arrayList = null;
                if (!this.mediaWebpage || (botInlineResult2 = this.inlineResult) == null) {
                    arrayList3 = arrayList;
                } else {
                    if (botInlineResult2.title != null) {
                        try {
                            arrayList4 = arrayList;
                        } catch (Exception e) {
                            e = e;
                            arrayList4 = arrayList;
                        }
                        try {
                            this.titleLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) this.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), Theme.chat_contextResult_titleTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_titleTextPaint.measureText(r0)), dp2), TextUtils.TruncateAt.END), Theme.chat_contextResult_titleTextPaint, dp2 + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        } catch (Exception e2) {
                            e = e2;
                            FileLog.e(e);
                            this.letterDrawable.setTitle(this.inlineResult.title);
                            str10 = this.inlineResult.description;
                            if (str10 == null) {
                            }
                            if (this.inlineResult.url != null) {
                            }
                            document = this.documentAttach;
                            if (document == null) {
                            }
                            closestPhotoSizeWithSize = null;
                            str = null;
                            botInlineResult = this.inlineResult;
                            if (botInlineResult != null) {
                            }
                            if (this.documentAttach != null) {
                            }
                            i3 = 0;
                            i4 = 0;
                            if (i3 != 0) {
                            }
                            photoSize = this.currentPhotoObject;
                            if (photoSize != null) {
                            }
                            if (i3 != 0) {
                            }
                            i3 = AndroidUtilities.dp(80.0f);
                            i4 = i3;
                            if (this.documentAttach == null) {
                            }
                            if (this.mediaWebpage) {
                            }
                            this.linkImageView.setAspectFit(this.documentAttachType == 6);
                            if (this.documentAttachType == 2) {
                            }
                            if (SharedConfig.isAutoplayGifs()) {
                            }
                            this.linkImageView.setAllowStartAnimation(true);
                            this.linkImageView.startAnimation();
                            this.drawLinkImageView = true;
                            if (this.mediaWebpage) {
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 != null) {
                            }
                        }
                        this.letterDrawable.setTitle(this.inlineResult.title);
                    } else {
                        arrayList4 = arrayList;
                    }
                    str10 = this.inlineResult.description;
                    if (str10 == null) {
                        try {
                            c = ' ';
                            c2 = '\n';
                            arrayList3 = arrayList4;
                        } catch (Exception e3) {
                            e = e3;
                            arrayList3 = arrayList4;
                            c = ' ';
                            c2 = '\n';
                        }
                        try {
                            StaticLayout generateStaticLayout = ChatMessageCell.generateStaticLayout(Emoji.replaceEmoji((CharSequence) str10, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), AndroidUtilities.dp(13.0f), false), Theme.chat_contextResult_descriptionTextPaint, dp2, dp2, 0, 3);
                            this.descriptionLayout = generateStaticLayout;
                            if (generateStaticLayout.getLineCount() > 0) {
                                int i8 = this.descriptionY;
                                StaticLayout staticLayout = this.descriptionLayout;
                                this.linkY = i8 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(1.0f);
                            }
                        } catch (Exception e4) {
                            e = e4;
                            FileLog.e(e);
                            if (this.inlineResult.url != null) {
                            }
                            document = this.documentAttach;
                            if (document == null) {
                            }
                            closestPhotoSizeWithSize = null;
                            str = null;
                            botInlineResult = this.inlineResult;
                            if (botInlineResult != null) {
                            }
                            if (this.documentAttach != null) {
                            }
                            i3 = 0;
                            i4 = 0;
                            if (i3 != 0) {
                            }
                            photoSize = this.currentPhotoObject;
                            if (photoSize != null) {
                            }
                            if (i3 != 0) {
                            }
                            i3 = AndroidUtilities.dp(80.0f);
                            i4 = i3;
                            if (this.documentAttach == null) {
                            }
                            if (this.mediaWebpage) {
                            }
                            this.linkImageView.setAspectFit(this.documentAttachType == 6);
                            if (this.documentAttachType == 2) {
                            }
                            if (SharedConfig.isAutoplayGifs()) {
                            }
                            this.linkImageView.setAllowStartAnimation(true);
                            this.linkImageView.startAnimation();
                            this.drawLinkImageView = true;
                            if (this.mediaWebpage) {
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 != null) {
                            }
                        }
                    } else {
                        arrayList3 = arrayList4;
                        c = ' ';
                        c2 = '\n';
                    }
                    if (this.inlineResult.url != null) {
                        try {
                            this.linkLayout = new StaticLayout(TextUtils.ellipsize(this.inlineResult.url.replace(c2, c), Theme.chat_contextResult_descriptionTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText(r0)), dp2), TextUtils.TruncateAt.MIDDLE), Theme.chat_contextResult_descriptionTextPaint, dp2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        } catch (Exception e5) {
                            FileLog.e(e5);
                        }
                    }
                }
                document = this.documentAttach;
                if (document == null) {
                    if (!this.isForceGif && !MessageObject.isGifDocument(document)) {
                        if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, true)) {
                            this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                            str = "webp";
                            closestPhotoSizeWithSize = null;
                            botInlineResult = this.inlineResult;
                            if (botInlineResult != null) {
                                if ((botInlineResult.content instanceof TLRPC.TL_webDocument) && (str9 = botInlineResult.type) != null) {
                                    if (str9.startsWith("gif")) {
                                        TLRPC.WebDocument webDocument = this.inlineResult.thumb;
                                        tL_webDocument = (TLRPC.TL_webDocument) (((webDocument instanceof TLRPC.TL_webDocument) && "video/mp4".equals(webDocument.mime_type)) ? this.inlineResult.thumb : this.inlineResult.content);
                                        this.documentAttachType = 2;
                                    } else if (this.inlineResult.type.equals("photo")) {
                                        TLRPC.BotInlineResult botInlineResult4 = this.inlineResult;
                                        TLRPC.WebDocument webDocument2 = botInlineResult4.thumb;
                                        tL_webDocument = webDocument2 instanceof TLRPC.TL_webDocument ? (TLRPC.TL_webDocument) webDocument2 : (TLRPC.TL_webDocument) botInlineResult4.content;
                                    }
                                    if (tL_webDocument == null) {
                                        TLRPC.WebDocument webDocument3 = this.inlineResult.thumb;
                                        if (webDocument3 instanceof TLRPC.TL_webDocument) {
                                            tL_webDocument = (TLRPC.TL_webDocument) webDocument3;
                                        }
                                    }
                                    if (tL_webDocument == null && this.currentPhotoObject == null && closestPhotoSizeWithSize == null) {
                                        botInlineMessage = this.inlineResult.send_message;
                                        if (!(botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue) || (botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaGeo)) {
                                            TLRPC.GeoPoint geoPoint = botInlineMessage.geo;
                                            double d = geoPoint.lat;
                                            double d2 = geoPoint._long;
                                            if (MessagesController.getInstance(this.currentAccount).mapProvider != 2) {
                                                webFile = WebFile.createWithGeoPoint(this.inlineResult.send_message.geo, 72, 72, 15, Math.min(2, (int) Math.ceil(AndroidUtilities.density)));
                                                str2 = null;
                                                if (tL_webDocument != null) {
                                                    webFile = WebFile.createWithWebDocument(tL_webDocument);
                                                }
                                            } else {
                                                str2 = AndroidUtilities.formapMapUrl(this.currentAccount, d, d2, 72, 72, true, 15, -1);
                                                webFile = null;
                                                if (tL_webDocument != null) {
                                                }
                                            }
                                        }
                                    }
                                    str2 = null;
                                    webFile = null;
                                    if (tL_webDocument != null) {
                                    }
                                }
                                tL_webDocument = null;
                                if (tL_webDocument == null) {
                                }
                                if (tL_webDocument == null) {
                                    botInlineMessage = this.inlineResult.send_message;
                                    if (!(botInlineMessage instanceof TLRPC.TL_botInlineMessageMediaVenue)) {
                                    }
                                    TLRPC.GeoPoint geoPoint2 = botInlineMessage.geo;
                                    double d3 = geoPoint2.lat;
                                    double d22 = geoPoint2._long;
                                    if (MessagesController.getInstance(this.currentAccount).mapProvider != 2) {
                                    }
                                }
                                str2 = null;
                                webFile = null;
                                if (tL_webDocument != null) {
                                }
                            } else {
                                str2 = null;
                                webFile = null;
                            }
                            if (this.documentAttach != null) {
                                for (int i9 = 0; i9 < this.documentAttach.attributes.size(); i9++) {
                                    TLRPC.DocumentAttribute documentAttribute = this.documentAttach.attributes.get(i9);
                                    if ((documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) || (documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                                        i3 = documentAttribute.w;
                                        i4 = documentAttribute.h;
                                        break;
                                    }
                                }
                            }
                            i3 = 0;
                            i4 = 0;
                            if (i3 != 0 || i4 == 0) {
                                photoSize = this.currentPhotoObject;
                                if (photoSize != null) {
                                    if (closestPhotoSizeWithSize != null) {
                                        closestPhotoSizeWithSize.size = -1;
                                    }
                                    i3 = photoSize.w;
                                    i4 = photoSize.h;
                                } else {
                                    TLRPC.BotInlineResult botInlineResult5 = this.inlineResult;
                                    if (botInlineResult5 != null) {
                                        int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(botInlineResult5);
                                        int i10 = inlineResultWidthAndHeight[0];
                                        i4 = inlineResultWidthAndHeight[1];
                                        i3 = i10;
                                    }
                                }
                            }
                            if (i3 != 0 || i4 == 0) {
                                i3 = AndroidUtilities.dp(80.0f);
                                i4 = i3;
                            }
                            if (this.documentAttach == null || this.currentPhotoObject != null || webFile != null || str2 != null) {
                                if (this.mediaWebpage) {
                                    int dp3 = (int) (i3 / (i4 / AndroidUtilities.dp(80.0f)));
                                    if (this.documentAttachType == 2) {
                                        str4 = String.format(Locale.US, "%d_%d_b", Integer.valueOf((int) (dp3 / AndroidUtilities.density)), 80);
                                        if (SharedConfig.isAutoplayGifs() || this.isKeyboard) {
                                            str3 = str4;
                                        } else {
                                            str8 = str4 + "_firstframe";
                                            str4 = str4 + "_firstframe";
                                        }
                                    } else {
                                        str4 = String.format(Locale.US, "%d_%d", Integer.valueOf((int) (dp3 / AndroidUtilities.density)), 80);
                                        str8 = str4 + "_b";
                                    }
                                    str3 = str8;
                                } else {
                                    str3 = "52_52_b";
                                    str4 = "52_52";
                                }
                                this.linkImageView.setAspectFit(this.documentAttachType == 6);
                                if (this.documentAttachType == 2) {
                                    TLRPC.Document document2 = this.documentAttach;
                                    if (document2 != null) {
                                        TLRPC.VideoSize documentVideoThumb = MessageObject.getDocumentVideoThumb(document2);
                                        if (documentVideoThumb != null) {
                                            ImageReceiver imageReceiver3 = this.linkImageView;
                                            forWebFile = ImageLocation.getForDocument(documentVideoThumb, this.documentAttach);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("100_100");
                                            sb.append((SharedConfig.isAutoplayGifs() || this.isKeyboard) ? "" : "_firstframe");
                                            str5 = sb.toString();
                                            forPhoto = ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach);
                                            obj = this.parentObject;
                                            j = -1;
                                            i5 = 1;
                                            imageReceiver = imageReceiver3;
                                            str3 = str4;
                                            obj3 = obj;
                                        } else {
                                            ImageLocation forDocument = ImageLocation.getForDocument(this.documentAttach);
                                            if (this.isForceGif) {
                                                forDocument.imageType = 2;
                                            }
                                            ImageReceiver imageReceiver4 = this.linkImageView;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("100_100");
                                            sb2.append((SharedConfig.isAutoplayGifs() || this.isKeyboard) ? "" : "_firstframe");
                                            str5 = sb2.toString();
                                            forPhoto = ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach);
                                            i5 = 0;
                                            imageReceiver = imageReceiver4;
                                            forWebFile = forDocument;
                                            str3 = str4;
                                            j = this.documentAttach.size;
                                            obj3 = this.parentObject;
                                        }
                                    } else {
                                        ImageReceiver imageReceiver5 = this.linkImageView;
                                        forWebFile = webFile != null ? ImageLocation.getForWebFile(webFile) : ImageLocation.getForPath(str2);
                                        forPhoto = ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach);
                                        j = -1;
                                        i5 = 1;
                                        str5 = "100_100";
                                        imageReceiver = imageReceiver5;
                                        str3 = str4;
                                        obj3 = this.parentObject;
                                    }
                                    imageReceiver.setImage(forWebFile, str5, forPhoto, str3, j, str, obj3, i5);
                                } else {
                                    if (this.currentPhotoObject != null) {
                                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(this.documentAttach, Theme.key_windowBackgroundGray, 1.0f);
                                        if (MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                            ImageReceiver imageReceiver6 = this.linkImageView;
                                            forWebFile = ImageLocation.getForDocument(this.documentAttach);
                                            TLRPC.PhotoSize photoSize2 = this.currentPhotoObject;
                                            if (svgThumb != null) {
                                                str6 = "80_80";
                                                i6 = 0;
                                                imageReceiver2 = imageReceiver6;
                                                j2 = photoSize2.size;
                                                str7 = str;
                                                obj2 = this.parentObject;
                                            } else {
                                                forPhoto = ImageLocation.getForDocument(photoSize2, this.documentAttach);
                                                str5 = "80_80";
                                                i5 = 0;
                                                imageReceiver = imageReceiver6;
                                                j = this.currentPhotoObject.size;
                                                obj3 = this.parentObject;
                                                imageReceiver.setImage(forWebFile, str5, forPhoto, str3, j, str, obj3, i5);
                                            }
                                        } else {
                                            TLRPC.Document document3 = this.documentAttach;
                                            if (document3 == null) {
                                                ImageReceiver imageReceiver7 = this.linkImageView;
                                                forWebFile = ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach);
                                                forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach);
                                                long j3 = this.currentPhotoObject.size;
                                                obj = this.parentObject;
                                                i5 = 0;
                                                imageReceiver = imageReceiver7;
                                                str5 = str4;
                                                j = j3;
                                            } else if (svgThumb != null) {
                                                ImageReceiver imageReceiver8 = this.linkImageView;
                                                forWebFile = ImageLocation.getForDocument(this.currentPhotoObject, document3);
                                                i6 = 0;
                                                imageReceiver2 = imageReceiver8;
                                                str6 = str4;
                                                j2 = this.currentPhotoObject.size;
                                                str7 = str;
                                                obj2 = this.parentObject;
                                            } else {
                                                ImageReceiver imageReceiver9 = this.linkImageView;
                                                forWebFile = ImageLocation.getForDocument(this.currentPhotoObject, document3);
                                                forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach);
                                                long j4 = this.currentPhotoObject.size;
                                                obj = this.parentObject;
                                                i5 = 0;
                                                imageReceiver = imageReceiver9;
                                                str5 = str4;
                                                j = j4;
                                            }
                                        }
                                        imageReceiver2.setImage(forWebFile, str6, svgThumb, j2, str7, obj2, i6);
                                    } else {
                                        ImageReceiver imageReceiver10 = this.linkImageView;
                                        forWebFile = webFile != null ? ImageLocation.getForWebFile(webFile) : ImageLocation.getForPath(str2);
                                        forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize, this.photoAttach);
                                        obj = this.parentObject;
                                        j = -1;
                                        i5 = 1;
                                        imageReceiver = imageReceiver10;
                                        str5 = str4;
                                    }
                                    obj3 = obj;
                                    imageReceiver.setImage(forWebFile, str5, forPhoto, str3, j, str, obj3, i5);
                                }
                                if (!SharedConfig.isAutoplayGifs() || this.isKeyboard) {
                                    this.linkImageView.setAllowStartAnimation(true);
                                    this.linkImageView.startAnimation();
                                } else {
                                    this.linkImageView.setAllowStartAnimation(false);
                                    this.linkImageView.stopAnimation();
                                }
                                this.drawLinkImageView = true;
                            }
                            if (this.mediaWebpage) {
                                int size2 = View.MeasureSpec.getSize(i2);
                                if (size2 == 0) {
                                    size2 = AndroidUtilities.dp(100.0f);
                                }
                                setMeasuredDimension(size, size2);
                                int dp4 = (size - AndroidUtilities.dp(24.0f)) / 2;
                                int dp5 = (size2 - AndroidUtilities.dp(24.0f)) / 2;
                                this.radialProgress.setProgressRect(dp4, dp5, AndroidUtilities.dp(24.0f) + dp4, AndroidUtilities.dp(24.0f) + dp5);
                                this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                                this.linkImageView.setImageCoords(0.0f, 0.0f, size, size2);
                            } else {
                                StaticLayout staticLayout2 = this.titleLayout;
                                if (staticLayout2 != null && staticLayout2.getLineCount() != 0) {
                                    StaticLayout staticLayout3 = this.titleLayout;
                                    i7 = staticLayout3.getLineBottom(staticLayout3.getLineCount() - 1);
                                }
                                StaticLayout staticLayout4 = this.descriptionLayout;
                                if (staticLayout4 != null && staticLayout4.getLineCount() != 0) {
                                    StaticLayout staticLayout5 = this.descriptionLayout;
                                    i7 += staticLayout5.getLineBottom(staticLayout5.getLineCount() - 1);
                                }
                                StaticLayout staticLayout6 = this.linkLayout;
                                if (staticLayout6 != null && staticLayout6.getLineCount() > 0) {
                                    StaticLayout staticLayout7 = this.linkLayout;
                                    i7 += staticLayout7.getLineBottom(staticLayout7.getLineCount() - 1);
                                }
                                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), i7) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                                int dp6 = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    f = 8.0f;
                                    dp = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(8.0f)) - dp6;
                                } else {
                                    f = 8.0f;
                                    dp = AndroidUtilities.dp(8.0f);
                                }
                                this.letterDrawable.setBounds(dp, AndroidUtilities.dp(f), dp + dp6, AndroidUtilities.dp(60.0f));
                                float f2 = dp6;
                                this.linkImageView.setImageCoords(dp, AndroidUtilities.dp(f), f2, f2);
                                int i11 = this.documentAttachType;
                                if (i11 == 3 || i11 == 5) {
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                    this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + dp, AndroidUtilities.dp(12.0f), dp + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                }
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 != null) {
                                measureChildWithMargins(checkBox2, i, 0, i2, 0);
                                return;
                            }
                            return;
                        }
                        int i12 = this.documentAttachType;
                        if (i12 != 5) {
                        }
                    }
                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                } else {
                    TLRPC.BotInlineResult botInlineResult6 = this.inlineResult;
                    if (botInlineResult6 != null && botInlineResult6.photo != null) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(arrayList3, AndroidUtilities.getPhotoSize(), true);
                        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList3, 80);
                    }
                }
                closestPhotoSizeWithSize = null;
                str = null;
                botInlineResult = this.inlineResult;
                if (botInlineResult != null) {
                }
                if (this.documentAttach != null) {
                }
                i3 = 0;
                i4 = 0;
                if (i3 != 0) {
                }
                photoSize = this.currentPhotoObject;
                if (photoSize != null) {
                }
                if (i3 != 0) {
                }
                i3 = AndroidUtilities.dp(80.0f);
                i4 = i3;
                if (this.documentAttach == null) {
                }
                if (this.mediaWebpage) {
                }
                this.linkImageView.setAspectFit(this.documentAttachType == 6);
                if (this.documentAttachType == 2) {
                }
                if (SharedConfig.isAutoplayGifs()) {
                }
                this.linkImageView.setAllowStartAnimation(true);
                this.linkImageView.startAnimation();
                this.drawLinkImageView = true;
                if (this.mediaWebpage) {
                }
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                }
            } else {
                arrayList2 = new ArrayList(this.inlineResult.photo.sizes);
            }
        }
        arrayList = arrayList2;
        if (this.mediaWebpage) {
        }
        arrayList3 = arrayList;
        document = this.documentAttach;
        if (document == null) {
        }
        closestPhotoSizeWithSize = null;
        str = null;
        botInlineResult = this.inlineResult;
        if (botInlineResult != null) {
        }
        if (this.documentAttach != null) {
        }
        i3 = 0;
        i4 = 0;
        if (i3 != 0) {
        }
        photoSize = this.currentPhotoObject;
        if (photoSize != null) {
        }
        if (i3 != 0) {
        }
        i3 = AndroidUtilities.dp(80.0f);
        i4 = i3;
        if (this.documentAttach == null) {
        }
        if (this.mediaWebpage) {
        }
        this.linkImageView.setAspectFit(this.documentAttachType == 6);
        if (this.documentAttachType == 2) {
        }
        if (SharedConfig.isAutoplayGifs()) {
        }
        this.linkImageView.setAllowStartAnimation(true);
        this.linkImageView.startAnimation();
        this.drawLinkImageView = true;
        if (this.mediaWebpage) {
        }
        checkBox2 = this.checkBox;
        if (checkBox2 != null) {
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            if (this.buttonState == 4) {
                return;
            }
        } else if (this.buttonState == 1) {
            return;
        }
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        this.fileExist = true;
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(false, true);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        TLRPC.WebDocument webDocument;
        if (this.mediaWebpage || this.delegate == null || this.inlineResult == null) {
            return super.onTouchEvent(motionEvent);
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        AndroidUtilities.dp(48.0f);
        int i = this.documentAttachType;
        boolean z = true;
        if (i == 3 || i == 5) {
            boolean contains = this.letterDrawable.getBounds().contains(x, y);
            if (motionEvent.getAction() == 0) {
                if (contains) {
                    this.buttonPressed = true;
                    this.radialProgress.setPressed(true, false);
                    invalidate();
                }
            } else if (this.buttonPressed) {
                if (motionEvent.getAction() == 1) {
                    this.buttonPressed = false;
                    playSoundEffect(0);
                    didPressedButton();
                } else {
                    if (motionEvent.getAction() == 3 || (motionEvent.getAction() == 2 && !contains)) {
                        this.buttonPressed = false;
                    }
                    this.radialProgress.setPressed(this.buttonPressed, false);
                }
                invalidate();
                this.radialProgress.setPressed(this.buttonPressed, false);
            }
            z = false;
        } else {
            TLRPC.BotInlineResult botInlineResult = this.inlineResult;
            if (botInlineResult != null && (webDocument = botInlineResult.content) != null && !TextUtils.isEmpty(webDocument.url)) {
                if (motionEvent.getAction() == 0) {
                    if (this.letterDrawable.getBounds().contains(x, y)) {
                        this.buttonPressed = true;
                    }
                } else if (this.buttonPressed) {
                    if (motionEvent.getAction() == 1) {
                        this.buttonPressed = false;
                        playSoundEffect(0);
                        this.delegate.didPressedImage(this);
                    } else if (motionEvent.getAction() == 3 || (motionEvent.getAction() == 2 && !this.letterDrawable.getBounds().contains(x, y))) {
                        this.buttonPressed = false;
                    }
                }
            }
            z = false;
        }
        return !z ? super.onTouchEvent(motionEvent) : z;
    }

    public void setCanPreviewGif(boolean z) {
        this.canPreviewGif = z;
    }

    public void setChecked(final boolean z, boolean z2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null) {
            return;
        }
        if (checkBox2.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
        AnimatorSet animatorSet = this.animator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animator = null;
        }
        if (!z2) {
            this.imageScale = z ? 0.85f : 1.0f;
            invalidate();
            return;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animator = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<ContextLinkCell, Float>) this.IMAGE_SCALE, z ? 0.81f : 1.0f));
        this.animator.setDuration(200L);
        this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.ContextLinkCell.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                    return;
                }
                ContextLinkCell.this.animator = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                    return;
                }
                ContextLinkCell.this.animator = null;
                if (z) {
                    return;
                }
                ContextLinkCell.this.setBackgroundColor(0);
            }
        });
        this.animator.start();
    }

    public void setDelegate(ContextLinkCellDelegate contextLinkCellDelegate) {
        this.delegate = contextLinkCellDelegate;
    }

    public void setGif(TLRPC.Document document, Object obj, int i, boolean z) {
        this.needDivider = z;
        this.needShadow = false;
        this.currentDate = i;
        this.inlineResult = null;
        this.parentObject = obj;
        this.documentAttach = document;
        this.photoAttach = null;
        this.mediaWebpage = true;
        this.isForceGif = true;
        setAttachType();
        this.documentAttachType = 2;
        requestLayout();
        this.fileName = null;
        this.cacheFile = null;
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    public void setGif(TLRPC.Document document, boolean z) {
        setGif(document, "gif" + document, 0, z);
    }

    public void setIsKeyboard(boolean z) {
        this.isKeyboard = z;
    }

    public void setLink(TLRPC.BotInlineResult botInlineResult, TLRPC.User user, boolean z, boolean z2, boolean z3, boolean z4) {
        this.needDivider = z2;
        this.needShadow = z3;
        this.inlineBot = user;
        this.inlineResult = botInlineResult;
        this.parentObject = botInlineResult;
        if (botInlineResult != null) {
            this.documentAttach = botInlineResult.document;
            this.photoAttach = botInlineResult.photo;
        } else {
            this.documentAttach = null;
            this.photoAttach = null;
        }
        this.mediaWebpage = z;
        this.isForceGif = z4;
        setAttachType();
        if (z4) {
            this.documentAttachType = 2;
        }
        requestLayout();
        this.fileName = null;
        this.cacheFile = null;
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
        ButtonBounce buttonBounce = this.buttonBounce;
        if (buttonBounce != null) {
            buttonBounce.setPressed(z || this.scaled);
        }
    }

    public void setScaled(boolean z) {
        this.scaled = z;
        ButtonBounce buttonBounce = this.buttonBounce;
        if (buttonBounce != null) {
            buttonBounce.setPressed(isPressed() || this.scaled);
        }
    }

    public boolean showingBitmap() {
        return this.linkImageView.getBitmap() != null;
    }

    public void updateButtonState(boolean z, boolean z2) {
        String str = this.fileName;
        if (str == null && !this.resolvingFileName) {
            this.resolvingFileName = true;
            int i = this.resolveFileNameId;
            this.resolveFileNameId = i;
            Utilities.searchQueue.postRunnable(new 1(i, z));
            this.radialProgress.setIcon(4, z, false);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            this.buttonState = -1;
            this.radialProgress.setIcon(4, z, false);
            return;
        }
        boolean isLoadingFile = this.documentAttach != null ? FileLoader.getInstance(this.currentAccount).isLoadingFile(this.fileName) : ImageLoader.getInstance().isLoadingHttpFile(this.fileName);
        if (isLoadingFile || !this.fileExist) {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(this.fileName, this);
            int i2 = this.documentAttachType;
            if (i2 != 5 && i2 != 3) {
                this.buttonState = 1;
                Float fileProgress = ImageLoader.getInstance().getFileProgress(this.fileName);
                this.radialProgress.setProgress(fileProgress != null ? fileProgress.floatValue() : 0.0f, false);
            } else if (isLoadingFile) {
                this.buttonState = 4;
                Float fileProgress2 = ImageLoader.getInstance().getFileProgress(this.fileName);
                if (fileProgress2 != null) {
                    this.radialProgress.setProgress(fileProgress2.floatValue(), z2);
                } else {
                    this.radialProgress.setProgress(0.0f, z2);
                }
            } else {
                this.buttonState = 2;
            }
        } else {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            int i3 = this.documentAttachType;
            if (i3 == 5 || i3 == 3) {
                boolean isPlayingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!isPlayingMessage || (isPlayingMessage && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setProgress(1.0f, z2);
            } else {
                this.buttonState = -1;
            }
        }
        this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
        invalidate();
    }
}
