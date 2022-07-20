package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC$BotInlineMessage;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$GeoPoint;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_botInlineMessageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_botInlineMessageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_webDocument;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class ContextLinkCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
    public final Property<ContextLinkCell, Float> IMAGE_SCALE;
    private int TAG;
    private AnimatorSet animator;
    private Paint backgroundPaint;
    private boolean buttonPressed;
    private int buttonState;
    File cacheFile;
    private boolean canPreviewGif;
    private CheckBox2 checkBox;
    private int currentAccount;
    private int currentDate;
    private MessageObject currentMessageObject;
    private TLRPC$PhotoSize currentPhotoObject;
    private ContextLinkCellDelegate delegate;
    private StaticLayout descriptionLayout;
    private int descriptionY;
    private TLRPC$Document documentAttach;
    private int documentAttachType;
    private boolean drawLinkImageView;
    boolean fileExist;
    String fileName;
    private float imageScale;
    private TLRPC$User inlineBot;
    private TLRPC$BotInlineResult inlineResult;
    private boolean isForceGif;
    private long lastUpdateTime;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private StaticLayout linkLayout;
    private int linkY;
    private boolean mediaWebpage;
    private boolean needDivider;
    private boolean needShadow;
    private Object parentObject;
    private TLRPC$Photo photoAttach;
    private RadialProgress2 radialProgress;
    int resolveFileNameId;
    boolean resolvingFileName;
    private Theme.ResourcesProvider resourcesProvider;
    private float scale;
    private boolean scaled;
    private StaticLayout titleLayout;
    private int titleY;

    /* loaded from: classes3.dex */
    public interface ContextLinkCellDelegate {
        void didPressedImage(ContextLinkCell contextLinkCell);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    static {
        new AccelerateInterpolator(0.5f);
    }

    public ContextLinkCell(Context context) {
        this(context, false, null);
    }

    public ContextLinkCell(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.titleY = AndroidUtilities.dp(7.0f);
        this.descriptionY = AndroidUtilities.dp(27.0f);
        this.imageScale = 1.0f;
        this.IMAGE_SCALE = new AnonymousClass2("animationValue");
        this.resourcesProvider = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setLayerNum(1);
        this.linkImageView.setUseSharedAnimationQueue(true);
        this.letterDrawable = new LetterDrawable(resourcesProvider);
        this.radialProgress = new RadialProgress2(this);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        setFocusable(true);
        if (z) {
            Paint paint = new Paint();
            this.backgroundPaint = paint;
            paint.setColor(Theme.getColor("sharedMedia_photoPlaceholder", resourcesProvider));
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setVisibility(4);
            this.checkBox.setColor(null, "sharedMedia_photoPlaceholder", "checkboxCheck");
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(1);
            addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 1.0f, 1.0f, 0.0f));
        }
        setWillNotDraw(false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x01e2, code lost:
        if (r0 == r40.currentPhotoObject) goto L74;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:0x026f  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02ae  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x02e1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x02e7  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0306 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0311  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x031d  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0378  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0388  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0390  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0430  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x057c  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0649  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0130 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:240:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0240  */
    /* JADX WARN: Type inference failed for: r3v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v39 */
    /* JADX WARN: Type inference failed for: r3v40 */
    @Override // android.widget.FrameLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        ArrayList arrayList;
        float f;
        ?? r3;
        ArrayList arrayList2;
        TLRPC$Document tLRPC$Document;
        String str;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        TLRPC$BotInlineResult tLRPC$BotInlineResult;
        String str2;
        WebFile webFile;
        int i3;
        int i4;
        CheckBox2 checkBox2;
        float f2;
        int i5;
        String str3;
        String str4;
        TLRPC$PhotoSize tLRPC$PhotoSize2;
        TLRPC$TL_webDocument tLRPC$TL_webDocument;
        WebFile webFile2;
        TLRPC$BotInlineMessage tLRPC$BotInlineMessage;
        String str5;
        boolean z;
        TLRPC$BotInlineResult tLRPC$BotInlineResult2;
        String str6;
        char c;
        boolean z2;
        char c2;
        Exception e;
        boolean z3;
        String str7;
        Exception e2;
        CharSequence ellipsize;
        ArrayList arrayList3;
        int i6 = 0;
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
        int dp = (size - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        if (this.documentAttach != null) {
            arrayList3 = new ArrayList(this.documentAttach.thumbs);
        } else {
            TLRPC$BotInlineResult tLRPC$BotInlineResult3 = this.inlineResult;
            if (tLRPC$BotInlineResult3 != null && tLRPC$BotInlineResult3.photo != null) {
                arrayList3 = new ArrayList(this.inlineResult.photo.sizes);
            } else {
                arrayList = null;
                if (!this.mediaWebpage || (tLRPC$BotInlineResult2 = this.inlineResult) == null) {
                    arrayList2 = arrayList;
                    z = true;
                } else {
                    if (tLRPC$BotInlineResult2.title != null) {
                        try {
                            this.titleLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(this.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), Theme.chat_contextResult_titleTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_titleTextPaint.measureText(str6)), dp), TextUtils.TruncateAt.END), Theme.chat_contextResult_titleTextPaint, dp + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                        this.letterDrawable.setTitle(this.inlineResult.title);
                    }
                    String str8 = this.inlineResult.description;
                    if (str8 != null) {
                        try {
                            c2 = '\n';
                            c = ' ';
                            z2 = true;
                            z3 = true;
                            z3 = true;
                        } catch (Exception e4) {
                            e = e4;
                            c2 = '\n';
                            z2 = true;
                            c = ' ';
                        }
                        try {
                            StaticLayout generateStaticLayout = ChatMessageCell.generateStaticLayout(Emoji.replaceEmoji(str8, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), AndroidUtilities.dp(13.0f), false), Theme.chat_contextResult_descriptionTextPaint, dp, dp, 0, 3);
                            this.descriptionLayout = generateStaticLayout;
                            if (generateStaticLayout.getLineCount() > 0) {
                                int i7 = this.descriptionY;
                                StaticLayout staticLayout = this.descriptionLayout;
                                this.linkY = i7 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(1.0f);
                            }
                        } catch (Exception e5) {
                            e = e5;
                            FileLog.e(e);
                            z3 = z2;
                            if (this.inlineResult.url == null) {
                            }
                        }
                    } else {
                        c2 = '\n';
                        z3 = true;
                        c = ' ';
                    }
                    if (this.inlineResult.url == null) {
                        try {
                            ellipsize = TextUtils.ellipsize(this.inlineResult.url.replace(c2, c), Theme.chat_contextResult_descriptionTextPaint, Math.min((int) Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText(str7)), dp), TextUtils.TruncateAt.MIDDLE);
                            f = 1.0f;
                            arrayList2 = arrayList;
                        } catch (Exception e6) {
                            e2 = e6;
                            arrayList2 = arrayList;
                            f = 1.0f;
                        }
                        try {
                            this.linkLayout = new StaticLayout(ellipsize, Theme.chat_contextResult_descriptionTextPaint, dp, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            r3 = z3;
                        } catch (Exception e7) {
                            e2 = e7;
                            FileLog.e(e2);
                            r3 = z3;
                            tLRPC$Document = this.documentAttach;
                            if (tLRPC$Document == null) {
                            }
                            tLRPC$PhotoSize = null;
                            str = null;
                            tLRPC$BotInlineResult = this.inlineResult;
                            if (tLRPC$BotInlineResult != null) {
                            }
                            if (this.documentAttach != null) {
                            }
                            i4 = 0;
                            i3 = 0;
                            if (i4 != 0) {
                            }
                            tLRPC$PhotoSize2 = this.currentPhotoObject;
                            if (tLRPC$PhotoSize2 != null) {
                            }
                            if (i4 != 0) {
                            }
                            i4 = AndroidUtilities.dp(80.0f);
                            i3 = i4;
                            if (this.documentAttach == null) {
                            }
                            if (this.mediaWebpage) {
                            }
                            this.linkImageView.setAspectFit(this.documentAttachType == 6);
                            if (this.documentAttachType == 2) {
                            }
                            this.drawLinkImageView = r3;
                            if (this.mediaWebpage) {
                            }
                            checkBox2 = this.checkBox;
                            if (checkBox2 == null) {
                            }
                        }
                        tLRPC$Document = this.documentAttach;
                        if (tLRPC$Document == null) {
                            if (this.isForceGif || MessageObject.isGifDocument(tLRPC$Document)) {
                                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                            } else if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, r3)) {
                                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                                str = "webp";
                                tLRPC$PhotoSize = null;
                                tLRPC$BotInlineResult = this.inlineResult;
                                if (tLRPC$BotInlineResult != null) {
                                    if ((tLRPC$BotInlineResult.content instanceof TLRPC$TL_webDocument) && (str5 = tLRPC$BotInlineResult.type) != null) {
                                        if (str5.startsWith("gif")) {
                                            TLRPC$WebDocument tLRPC$WebDocument = this.inlineResult.thumb;
                                            if ((tLRPC$WebDocument instanceof TLRPC$TL_webDocument) && "video/mp4".equals(tLRPC$WebDocument.mime_type)) {
                                                tLRPC$TL_webDocument = (TLRPC$TL_webDocument) this.inlineResult.thumb;
                                            } else {
                                                tLRPC$TL_webDocument = (TLRPC$TL_webDocument) this.inlineResult.content;
                                            }
                                            this.documentAttachType = 2;
                                        } else if (this.inlineResult.type.equals("photo")) {
                                            TLRPC$BotInlineResult tLRPC$BotInlineResult4 = this.inlineResult;
                                            TLRPC$WebDocument tLRPC$WebDocument2 = tLRPC$BotInlineResult4.thumb;
                                            if (tLRPC$WebDocument2 instanceof TLRPC$TL_webDocument) {
                                                tLRPC$TL_webDocument = (TLRPC$TL_webDocument) tLRPC$WebDocument2;
                                            } else {
                                                tLRPC$TL_webDocument = (TLRPC$TL_webDocument) tLRPC$BotInlineResult4.content;
                                            }
                                        }
                                        if (tLRPC$TL_webDocument == null) {
                                            TLRPC$WebDocument tLRPC$WebDocument3 = this.inlineResult.thumb;
                                            if (tLRPC$WebDocument3 instanceof TLRPC$TL_webDocument) {
                                                tLRPC$TL_webDocument = (TLRPC$TL_webDocument) tLRPC$WebDocument3;
                                            }
                                        }
                                        if (tLRPC$TL_webDocument == null && this.currentPhotoObject == null && tLRPC$PhotoSize == null) {
                                            tLRPC$BotInlineMessage = this.inlineResult.send_message;
                                            if (!(tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaVenue) || (tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaGeo)) {
                                                TLRPC$GeoPoint tLRPC$GeoPoint = tLRPC$BotInlineMessage.geo;
                                                double d = tLRPC$GeoPoint.lat;
                                                double d2 = tLRPC$GeoPoint._long;
                                                if (MessagesController.getInstance(this.currentAccount).mapProvider != 2) {
                                                    webFile2 = WebFile.createWithGeoPoint(this.inlineResult.send_message.geo, 72, 72, 15, Math.min(2, (int) Math.ceil(AndroidUtilities.density)));
                                                    str2 = null;
                                                    webFile = tLRPC$TL_webDocument == null ? WebFile.createWithWebDocument(tLRPC$TL_webDocument) : webFile2;
                                                } else {
                                                    str2 = AndroidUtilities.formapMapUrl(this.currentAccount, d, d2, 72, 72, true, 15, -1);
                                                    webFile2 = null;
                                                    if (tLRPC$TL_webDocument == null) {
                                                    }
                                                }
                                            }
                                        }
                                        str2 = null;
                                        webFile2 = null;
                                        if (tLRPC$TL_webDocument == null) {
                                        }
                                    }
                                    tLRPC$TL_webDocument = null;
                                    if (tLRPC$TL_webDocument == null) {
                                    }
                                    if (tLRPC$TL_webDocument == null) {
                                        tLRPC$BotInlineMessage = this.inlineResult.send_message;
                                        if (!(tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaVenue)) {
                                        }
                                        TLRPC$GeoPoint tLRPC$GeoPoint2 = tLRPC$BotInlineMessage.geo;
                                        double d3 = tLRPC$GeoPoint2.lat;
                                        double d22 = tLRPC$GeoPoint2._long;
                                        if (MessagesController.getInstance(this.currentAccount).mapProvider != 2) {
                                        }
                                    }
                                    str2 = null;
                                    webFile2 = null;
                                    if (tLRPC$TL_webDocument == null) {
                                    }
                                } else {
                                    webFile = null;
                                    str2 = null;
                                }
                                if (this.documentAttach != null) {
                                    for (int i8 = 0; i8 < this.documentAttach.attributes.size(); i8++) {
                                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = this.documentAttach.attributes.get(i8);
                                        if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                                            i4 = tLRPC$DocumentAttribute.w;
                                            i3 = tLRPC$DocumentAttribute.h;
                                            break;
                                        }
                                    }
                                }
                                i4 = 0;
                                i3 = 0;
                                if (i4 != 0 || i3 == 0) {
                                    tLRPC$PhotoSize2 = this.currentPhotoObject;
                                    if (tLRPC$PhotoSize2 != null) {
                                        if (tLRPC$PhotoSize != null) {
                                            tLRPC$PhotoSize.size = -1;
                                        }
                                        i4 = tLRPC$PhotoSize2.w;
                                        i3 = tLRPC$PhotoSize2.h;
                                    } else {
                                        TLRPC$BotInlineResult tLRPC$BotInlineResult5 = this.inlineResult;
                                        if (tLRPC$BotInlineResult5 != null) {
                                            int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(tLRPC$BotInlineResult5);
                                            int i9 = inlineResultWidthAndHeight[0];
                                            i3 = inlineResultWidthAndHeight[r3];
                                            i4 = i9;
                                        }
                                    }
                                }
                                if (i4 != 0 || i3 == 0) {
                                    i4 = AndroidUtilities.dp(80.0f);
                                    i3 = i4;
                                }
                                if (this.documentAttach == null || this.currentPhotoObject != null || webFile != null || str2 != null) {
                                    if (this.mediaWebpage) {
                                        int dp2 = (int) (i4 / (i3 / AndroidUtilities.dp(80.0f)));
                                        if (this.documentAttachType == 2) {
                                            Locale locale = Locale.US;
                                            Object[] objArr = new Object[2];
                                            objArr[0] = Integer.valueOf((int) (dp2 / AndroidUtilities.density));
                                            objArr[r3] = 80;
                                            str4 = String.format(locale, "%d_%d_b", objArr);
                                            str3 = str4;
                                        } else {
                                            Locale locale2 = Locale.US;
                                            Object[] objArr2 = new Object[2];
                                            objArr2[0] = Integer.valueOf((int) (dp2 / AndroidUtilities.density));
                                            char c3 = r3 == true ? 1 : 0;
                                            char c4 = r3 == true ? 1 : 0;
                                            char c5 = r3 == true ? 1 : 0;
                                            char c6 = r3 == true ? 1 : 0;
                                            char c7 = r3 == true ? 1 : 0;
                                            objArr2[c3] = 80;
                                            str4 = String.format(locale2, "%d_%d", objArr2);
                                            str3 = str4 + "_b";
                                        }
                                    } else {
                                        str3 = "52_52_b";
                                        str4 = "52_52";
                                    }
                                    this.linkImageView.setAspectFit(this.documentAttachType == 6);
                                    if (this.documentAttachType == 2) {
                                        TLRPC$Document tLRPC$Document2 = this.documentAttach;
                                        if (tLRPC$Document2 != null) {
                                            TLRPC$VideoSize documentVideoThumb = MessageObject.getDocumentVideoThumb(tLRPC$Document2);
                                            if (documentVideoThumb != null) {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(documentVideoThumb, this.documentAttach), "100_100", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str4, -1L, str, this.parentObject, 1);
                                            } else {
                                                ImageLocation forDocument = ImageLocation.getForDocument(this.documentAttach);
                                                if (this.isForceGif) {
                                                    forDocument.imageType = 2;
                                                }
                                                this.linkImageView.setImage(forDocument, "100_100", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str4, this.documentAttach.size, str, this.parentObject, 0);
                                            }
                                        } else if (webFile != null) {
                                            this.linkImageView.setImage(ImageLocation.getForWebFile(webFile), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str4, -1L, str, this.parentObject, 1);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForPath(str2), "100_100", ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str4, -1L, str, this.parentObject, 1);
                                        }
                                    } else if (this.currentPhotoObject != null) {
                                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(this.documentAttach, "windowBackgroundGray", f);
                                        if (!MessageObject.canAutoplayAnimatedSticker(this.documentAttach)) {
                                            TLRPC$Document tLRPC$Document3 = this.documentAttach;
                                            if (tLRPC$Document3 == null) {
                                                this.linkImageView.setImage(ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), str4, ImageLocation.getForPhoto(tLRPC$PhotoSize, this.photoAttach), str3, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else if (svgThumb != null) {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, tLRPC$Document3), str4, svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            } else {
                                                this.linkImageView.setImage(ImageLocation.getForDocument(this.currentPhotoObject, tLRPC$Document3), str4, ImageLocation.getForPhoto(tLRPC$PhotoSize, this.photoAttach), str3, this.currentPhotoObject.size, str, this.parentObject, 0);
                                            }
                                        } else if (svgThumb != null) {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", svgThumb, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        } else {
                                            this.linkImageView.setImage(ImageLocation.getForDocument(this.documentAttach), "80_80", ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), str3, this.currentPhotoObject.size, str, this.parentObject, 0);
                                        }
                                    } else if (webFile != null) {
                                        this.linkImageView.setImage(ImageLocation.getForWebFile(webFile), str4, ImageLocation.getForPhoto(tLRPC$PhotoSize, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    } else {
                                        this.linkImageView.setImage(ImageLocation.getForPath(str2), str4, ImageLocation.getForPhoto(tLRPC$PhotoSize, this.photoAttach), str3, -1L, str, this.parentObject, 1);
                                    }
                                    this.drawLinkImageView = r3;
                                }
                                if (this.mediaWebpage) {
                                    int size2 = View.MeasureSpec.getSize(i2);
                                    if (size2 == 0) {
                                        size2 = AndroidUtilities.dp(100.0f);
                                    }
                                    setMeasuredDimension(size, size2);
                                    int dp3 = (size - AndroidUtilities.dp(24.0f)) / 2;
                                    int dp4 = (size2 - AndroidUtilities.dp(24.0f)) / 2;
                                    this.radialProgress.setProgressRect(dp3, dp4, AndroidUtilities.dp(24.0f) + dp3, AndroidUtilities.dp(24.0f) + dp4);
                                    this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
                                    this.linkImageView.setImageCoords(0.0f, 0.0f, size, size2);
                                } else {
                                    StaticLayout staticLayout2 = this.titleLayout;
                                    if (staticLayout2 != null && staticLayout2.getLineCount() != 0) {
                                        StaticLayout staticLayout3 = this.titleLayout;
                                        i6 = 0 + staticLayout3.getLineBottom(staticLayout3.getLineCount() - r3);
                                    }
                                    StaticLayout staticLayout4 = this.descriptionLayout;
                                    if (staticLayout4 != null && staticLayout4.getLineCount() != 0) {
                                        StaticLayout staticLayout5 = this.descriptionLayout;
                                        i6 += staticLayout5.getLineBottom(staticLayout5.getLineCount() - r3);
                                    }
                                    StaticLayout staticLayout6 = this.linkLayout;
                                    if (staticLayout6 != null && staticLayout6.getLineCount() > 0) {
                                        StaticLayout staticLayout7 = this.linkLayout;
                                        int lineCount = staticLayout7.getLineCount();
                                        int i10 = r3 == true ? 1 : 0;
                                        int i11 = r3 == true ? 1 : 0;
                                        int i12 = r3 == true ? 1 : 0;
                                        int i13 = r3 == true ? 1 : 0;
                                        int i14 = r3 == true ? 1 : 0;
                                        i6 += staticLayout7.getLineBottom(lineCount - i10);
                                    }
                                    setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(68.0f), Math.max(AndroidUtilities.dp(52.0f), i6) + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
                                    int dp5 = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        f2 = 8.0f;
                                        i5 = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(8.0f)) - dp5;
                                    } else {
                                        f2 = 8.0f;
                                        i5 = AndroidUtilities.dp(8.0f);
                                    }
                                    this.letterDrawable.setBounds(i5, AndroidUtilities.dp(f2), i5 + dp5, AndroidUtilities.dp(60.0f));
                                    float f3 = dp5;
                                    this.linkImageView.setImageCoords(i5, AndroidUtilities.dp(f2), f3, f3);
                                    int i15 = this.documentAttachType;
                                    if (i15 == 3 || i15 == 5) {
                                        this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                                        this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + i5, AndroidUtilities.dp(12.0f), i5 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
                                    }
                                }
                                checkBox2 = this.checkBox;
                                if (checkBox2 == null) {
                                    return;
                                }
                                measureChildWithMargins(checkBox2, i, 0, i2, 0);
                                return;
                            } else {
                                int i16 = this.documentAttachType;
                                if (i16 != 5 && i16 != 3) {
                                    this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                                }
                            }
                        } else {
                            TLRPC$BotInlineResult tLRPC$BotInlineResult6 = this.inlineResult;
                            if (tLRPC$BotInlineResult6 != null && tLRPC$BotInlineResult6.photo != null) {
                                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(arrayList2, AndroidUtilities.getPhotoSize(), r3);
                                tLRPC$PhotoSize = FileLoader.getClosestPhotoSizeWithSize(arrayList2, 80);
                            }
                        }
                        tLRPC$PhotoSize = null;
                        str = null;
                        tLRPC$BotInlineResult = this.inlineResult;
                        if (tLRPC$BotInlineResult != null) {
                        }
                        if (this.documentAttach != null) {
                        }
                        i4 = 0;
                        i3 = 0;
                        if (i4 != 0) {
                        }
                        tLRPC$PhotoSize2 = this.currentPhotoObject;
                        if (tLRPC$PhotoSize2 != null) {
                        }
                        if (i4 != 0) {
                        }
                        i4 = AndroidUtilities.dp(80.0f);
                        i3 = i4;
                        if (this.documentAttach == null) {
                        }
                        if (this.mediaWebpage) {
                        }
                        this.linkImageView.setAspectFit(this.documentAttachType == 6);
                        if (this.documentAttachType == 2) {
                        }
                        this.drawLinkImageView = r3;
                        if (this.mediaWebpage) {
                        }
                        checkBox2 = this.checkBox;
                        if (checkBox2 == null) {
                        }
                    } else {
                        arrayList2 = arrayList;
                        z = z3;
                    }
                }
                f = 1.0f;
                r3 = z;
                tLRPC$Document = this.documentAttach;
                if (tLRPC$Document == null) {
                }
                tLRPC$PhotoSize = null;
                str = null;
                tLRPC$BotInlineResult = this.inlineResult;
                if (tLRPC$BotInlineResult != null) {
                }
                if (this.documentAttach != null) {
                }
                i4 = 0;
                i3 = 0;
                if (i4 != 0) {
                }
                tLRPC$PhotoSize2 = this.currentPhotoObject;
                if (tLRPC$PhotoSize2 != null) {
                }
                if (i4 != 0) {
                }
                i4 = AndroidUtilities.dp(80.0f);
                i3 = i4;
                if (this.documentAttach == null) {
                }
                if (this.mediaWebpage) {
                }
                this.linkImageView.setAspectFit(this.documentAttachType == 6);
                if (this.documentAttachType == 2) {
                }
                this.drawLinkImageView = r3;
                if (this.mediaWebpage) {
                }
                checkBox2 = this.checkBox;
                if (checkBox2 == null) {
                }
            }
        }
        arrayList = arrayList3;
        if (!this.mediaWebpage) {
        }
        arrayList2 = arrayList;
        z = true;
        f = 1.0f;
        r3 = z;
        tLRPC$Document = this.documentAttach;
        if (tLRPC$Document == null) {
        }
        tLRPC$PhotoSize = null;
        str = null;
        tLRPC$BotInlineResult = this.inlineResult;
        if (tLRPC$BotInlineResult != null) {
        }
        if (this.documentAttach != null) {
        }
        i4 = 0;
        i3 = 0;
        if (i4 != 0) {
        }
        tLRPC$PhotoSize2 = this.currentPhotoObject;
        if (tLRPC$PhotoSize2 != null) {
        }
        if (i4 != 0) {
        }
        i4 = AndroidUtilities.dp(80.0f);
        i3 = i4;
        if (this.documentAttach == null) {
        }
        if (this.mediaWebpage) {
        }
        this.linkImageView.setAspectFit(this.documentAttachType == 6);
        if (this.documentAttachType == 2) {
        }
        this.drawLinkImageView = r3;
        if (this.mediaWebpage) {
        }
        checkBox2 = this.checkBox;
        if (checkBox2 == null) {
        }
    }

    private void setAttachType() {
        this.currentMessageObject = null;
        this.documentAttachType = 0;
        TLRPC$Document tLRPC$Document = this.documentAttach;
        if (tLRPC$Document != null) {
            if (MessageObject.isGifDocument(tLRPC$Document)) {
                this.documentAttachType = 2;
            } else if (MessageObject.isStickerDocument(this.documentAttach) || MessageObject.isAnimatedStickerDocument(this.documentAttach, true)) {
                this.documentAttachType = 6;
            } else if (MessageObject.isMusicDocument(this.documentAttach)) {
                this.documentAttachType = 5;
            } else if (MessageObject.isVoiceDocument(this.documentAttach)) {
                this.documentAttachType = 3;
            }
        } else {
            TLRPC$BotInlineResult tLRPC$BotInlineResult = this.inlineResult;
            if (tLRPC$BotInlineResult != null) {
                if (tLRPC$BotInlineResult.photo != null) {
                    this.documentAttachType = 7;
                } else if (tLRPC$BotInlineResult.type.equals("audio")) {
                    this.documentAttachType = 5;
                } else if (this.inlineResult.type.equals("voice")) {
                    this.documentAttachType = 3;
                }
            }
        }
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.out = true;
            tLRPC$TL_message.id = -Utilities.random.nextInt();
            tLRPC$TL_message.peer_id = new TLRPC$TL_peerUser();
            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
            TLRPC$Peer tLRPC$Peer = tLRPC$TL_message.peer_id;
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            tLRPC$TL_peerUser.user_id = clientUserId;
            tLRPC$Peer.user_id = clientUserId;
            tLRPC$TL_message.date = (int) (System.currentTimeMillis() / 1000);
            String str = "";
            tLRPC$TL_message.message = str;
            TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
            tLRPC$TL_message.media = tLRPC$TL_messageMediaDocument;
            tLRPC$TL_messageMediaDocument.flags |= 3;
            tLRPC$TL_messageMediaDocument.document = new TLRPC$TL_document();
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$TL_message.media;
            tLRPC$MessageMedia.document.file_reference = new byte[0];
            tLRPC$TL_message.flags |= 768;
            TLRPC$Document tLRPC$Document2 = this.documentAttach;
            if (tLRPC$Document2 != null) {
                tLRPC$MessageMedia.document = tLRPC$Document2;
                tLRPC$TL_message.attachPath = str;
            } else {
                String str2 = "mp3";
                String httpUrlExtension = ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? str2 : "ogg");
                TLRPC$Document tLRPC$Document3 = tLRPC$TL_message.media.document;
                tLRPC$Document3.id = 0L;
                tLRPC$Document3.access_hash = 0L;
                tLRPC$Document3.date = tLRPC$TL_message.date;
                tLRPC$Document3.mime_type = "audio/" + httpUrlExtension;
                TLRPC$Document tLRPC$Document4 = tLRPC$TL_message.media.document;
                tLRPC$Document4.size = 0L;
                tLRPC$Document4.dc_id = 0;
                TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                tLRPC$TL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(this.inlineResult);
                TLRPC$BotInlineResult tLRPC$BotInlineResult2 = this.inlineResult;
                String str3 = tLRPC$BotInlineResult2.title;
                if (str3 == null) {
                    str3 = str;
                }
                tLRPC$TL_documentAttributeAudio.title = str3;
                String str4 = tLRPC$BotInlineResult2.description;
                if (str4 != null) {
                    str = str4;
                }
                tLRPC$TL_documentAttributeAudio.performer = str;
                tLRPC$TL_documentAttributeAudio.flags |= 3;
                if (this.documentAttachType == 3) {
                    tLRPC$TL_documentAttributeAudio.voice = true;
                }
                tLRPC$TL_message.media.document.attributes.add(tLRPC$TL_documentAttributeAudio);
                TLRPC$TL_documentAttributeFilename tLRPC$TL_documentAttributeFilename = new TLRPC$TL_documentAttributeFilename();
                StringBuilder sb = new StringBuilder();
                sb.append(Utilities.MD5(this.inlineResult.content.url));
                sb.append(".");
                sb.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? str2 : "ogg"));
                tLRPC$TL_documentAttributeFilename.file_name = sb.toString();
                tLRPC$TL_message.media.document.attributes.add(tLRPC$TL_documentAttributeFilename);
                File directory = FileLoader.getDirectory(4);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Utilities.MD5(this.inlineResult.content.url));
                sb2.append(".");
                String str5 = this.inlineResult.content.url;
                if (this.documentAttachType != 5) {
                    str2 = "ogg";
                }
                sb2.append(ImageLoader.getHttpUrlExtension(str5, str2));
                tLRPC$TL_message.attachPath = new File(directory, sb2.toString()).getAbsolutePath();
            }
            this.currentMessageObject = new MessageObject(this.currentAccount, tLRPC$TL_message, false, true);
        }
    }

    public void setLink(TLRPC$BotInlineResult tLRPC$BotInlineResult, TLRPC$User tLRPC$User, boolean z, boolean z2, boolean z3, boolean z4) {
        this.needDivider = z2;
        this.needShadow = z3;
        this.inlineBot = tLRPC$User;
        this.inlineResult = tLRPC$BotInlineResult;
        this.parentObject = tLRPC$BotInlineResult;
        if (tLRPC$BotInlineResult != null) {
            this.documentAttach = tLRPC$BotInlineResult.document;
            this.photoAttach = tLRPC$BotInlineResult.photo;
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
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    public TLRPC$User getInlineBot() {
        return this.inlineBot;
    }

    public Object getParentObject() {
        return this.parentObject;
    }

    public void setGif(TLRPC$Document tLRPC$Document, boolean z) {
        setGif(tLRPC$Document, "gif" + tLRPC$Document, 0, z);
    }

    public void setGif(TLRPC$Document tLRPC$Document, Object obj, int i, boolean z) {
        this.needDivider = z;
        this.needShadow = false;
        this.currentDate = i;
        this.inlineResult = null;
        this.parentObject = obj;
        this.documentAttach = tLRPC$Document;
        this.photoAttach = null;
        this.mediaWebpage = true;
        this.isForceGif = true;
        setAttachType();
        this.documentAttachType = 2;
        requestLayout();
        this.fileName = null;
        this.fileExist = false;
        this.resolvingFileName = false;
        updateButtonState(false, false);
    }

    public boolean isSticker() {
        return this.documentAttachType == 6;
    }

    public boolean isGif() {
        return this.documentAttachType == 2 && this.canPreviewGif;
    }

    public boolean showingBitmap() {
        return this.linkImageView.getBitmap() != null;
    }

    public int getDate() {
        return this.currentDate;
    }

    public TLRPC$Document getDocument() {
        return this.documentAttach;
    }

    public TLRPC$BotInlineResult getBotInlineResult() {
        return this.inlineResult;
    }

    public ImageReceiver getPhotoImage() {
        return this.linkImageView;
    }

    public void setScaled(boolean z) {
        this.scaled = z;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void setCanPreviewGif(boolean z) {
        this.canPreviewGif = z;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.linkImageView.onDetachedFromWindow();
        this.radialProgress.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.linkImageView.onAttachedToWindow()) {
            updateButtonState(false, false);
        }
        this.radialProgress.onAttachedToWindow();
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        TLRPC$WebDocument tLRPC$WebDocument;
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
                    return z ? super.onTouchEvent(motionEvent) : z;
                }
            } else if (this.buttonPressed) {
                if (motionEvent.getAction() == 1) {
                    this.buttonPressed = false;
                    playSoundEffect(0);
                    didPressedButton();
                    invalidate();
                } else if (motionEvent.getAction() == 3) {
                    this.buttonPressed = false;
                    invalidate();
                } else if (motionEvent.getAction() == 2 && !contains) {
                    this.buttonPressed = false;
                    invalidate();
                }
                this.radialProgress.setPressed(this.buttonPressed, false);
            }
            z = false;
            if (z) {
            }
        } else {
            TLRPC$BotInlineResult tLRPC$BotInlineResult = this.inlineResult;
            if (tLRPC$BotInlineResult != null && (tLRPC$WebDocument = tLRPC$BotInlineResult.content) != null && !TextUtils.isEmpty(tLRPC$WebDocument.url)) {
                if (motionEvent.getAction() == 0) {
                    if (this.letterDrawable.getBounds().contains(x, y)) {
                        this.buttonPressed = true;
                        if (z) {
                        }
                    }
                } else if (this.buttonPressed) {
                    if (motionEvent.getAction() == 1) {
                        this.buttonPressed = false;
                        playSoundEffect(0);
                        this.delegate.didPressedImage(this);
                    } else if (motionEvent.getAction() == 3) {
                        this.buttonPressed = false;
                    } else if (motionEvent.getAction() == 2 && !this.letterDrawable.getBounds().contains(x, y)) {
                        this.buttonPressed = false;
                    }
                }
            }
            z = false;
            if (z) {
            }
        }
    }

    private void didPressedButton() {
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            int i2 = this.buttonState;
            if (i2 == 0) {
                if (!MediaController.getInstance().playMessage(this.currentMessageObject)) {
                    return;
                }
                this.buttonState = 1;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            } else if (i2 == 1) {
                if (!MediaController.getInstance().lambda$startAudioAgain$7(this.currentMessageObject)) {
                    return;
                }
                this.buttonState = 0;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            } else if (i2 == 2) {
                this.radialProgress.setProgress(0.0f, false);
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.inlineResult, 1, 0);
                } else if (this.inlineResult.content instanceof TLRPC$TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).loadFile(WebFile.createWithWebDocument(this.inlineResult.content), 1, 1);
                }
                this.buttonState = 4;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            } else if (i2 != 4) {
            } else {
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                } else if (this.inlineResult.content instanceof TLRPC$TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(WebFile.createWithWebDocument(this.inlineResult.content));
                }
                this.buttonState = 2;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        TLRPC$BotInlineResult tLRPC$BotInlineResult;
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null && (checkBox2.isChecked() || !this.linkImageView.hasBitmapImage() || this.linkImageView.getCurrentAlpha() != 1.0f || PhotoViewer.isShowingImage((MessageObject) this.parentObject))) {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
        }
        float f = 8.0f;
        if (this.titleLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText2", this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.linkLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteLinkText", this.resourcesProvider));
            canvas.save();
            if (!LocaleController.isRTL) {
                f = AndroidUtilities.leftBaseline;
            }
            canvas.translate(AndroidUtilities.dp(f), this.linkY);
            this.linkLayout.draw(canvas);
            canvas.restore();
        }
        if (!this.mediaWebpage) {
            if (this.drawLinkImageView && !PhotoViewer.isShowingImage(this.inlineResult)) {
                this.letterDrawable.setAlpha((int) ((1.0f - this.linkImageView.getCurrentAlpha()) * 255.0f));
            } else {
                this.letterDrawable.setAlpha(255);
            }
            int i2 = this.documentAttachType;
            if (i2 == 3 || i2 == 5) {
                this.radialProgress.setProgressColor(Theme.getColor(this.buttonPressed ? "chat_inAudioSelectedProgress" : "chat_inAudioProgress", this.resourcesProvider));
                this.radialProgress.draw(canvas);
            } else {
                TLRPC$BotInlineResult tLRPC$BotInlineResult2 = this.inlineResult;
                if (tLRPC$BotInlineResult2 != null && tLRPC$BotInlineResult2.type.equals("file")) {
                    int intrinsicWidth = Theme.chat_inlineResultFile.getIntrinsicWidth();
                    int intrinsicHeight = Theme.chat_inlineResultFile.getIntrinsicHeight();
                    int imageX = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth) / 2));
                    int imageY = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight) / 2));
                    canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                    Theme.chat_inlineResultFile.setBounds(imageX, imageY, intrinsicWidth + imageX, intrinsicHeight + imageY);
                    Theme.chat_inlineResultFile.draw(canvas);
                } else {
                    TLRPC$BotInlineResult tLRPC$BotInlineResult3 = this.inlineResult;
                    if (tLRPC$BotInlineResult3 != null && (tLRPC$BotInlineResult3.type.equals("audio") || this.inlineResult.type.equals("voice"))) {
                        int intrinsicWidth2 = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                        int intrinsicHeight2 = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                        int imageX2 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth2) / 2));
                        int imageY2 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight2) / 2));
                        canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                        Theme.chat_inlineResultAudio.setBounds(imageX2, imageY2, intrinsicWidth2 + imageX2, intrinsicHeight2 + imageY2);
                        Theme.chat_inlineResultAudio.draw(canvas);
                    } else {
                        TLRPC$BotInlineResult tLRPC$BotInlineResult4 = this.inlineResult;
                        if (tLRPC$BotInlineResult4 != null && (tLRPC$BotInlineResult4.type.equals("venue") || this.inlineResult.type.equals("geo"))) {
                            int intrinsicWidth3 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                            int intrinsicHeight3 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                            int imageX3 = (int) (this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - intrinsicWidth3) / 2));
                            int imageY3 = (int) (this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - intrinsicHeight3) / 2));
                            canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f), this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f), LetterDrawable.paint);
                            Theme.chat_inlineResultLocation.setBounds(imageX3, imageY3, intrinsicWidth3 + imageX3, intrinsicHeight3 + imageY3);
                            Theme.chat_inlineResultLocation.draw(canvas);
                        } else {
                            this.letterDrawable.draw(canvas);
                        }
                    }
                }
            }
        } else {
            TLRPC$BotInlineResult tLRPC$BotInlineResult5 = this.inlineResult;
            if (tLRPC$BotInlineResult5 != null) {
                TLRPC$BotInlineMessage tLRPC$BotInlineMessage = tLRPC$BotInlineResult5.send_message;
                if ((tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaGeo) || (tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaVenue)) {
                    int intrinsicWidth4 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                    int intrinsicHeight4 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                    int imageX4 = (int) (this.linkImageView.getImageX() + ((this.linkImageView.getImageWidth() - intrinsicWidth4) / 2.0f));
                    int imageY4 = (int) (this.linkImageView.getImageY() + ((this.linkImageView.getImageHeight() - intrinsicHeight4) / 2.0f));
                    canvas.drawRect(this.linkImageView.getImageX(), this.linkImageView.getImageY(), this.linkImageView.getImageX() + this.linkImageView.getImageWidth(), this.linkImageView.getImageY() + this.linkImageView.getImageHeight(), LetterDrawable.paint);
                    Theme.chat_inlineResultLocation.setBounds(imageX4, imageY4, intrinsicWidth4 + imageX4, intrinsicHeight4 + imageY4);
                    Theme.chat_inlineResultLocation.draw(canvas);
                }
            }
        }
        if (this.drawLinkImageView) {
            if (this.inlineResult != null) {
                this.linkImageView.setVisible(!PhotoViewer.isShowingImage(tLRPC$BotInlineResult), false);
            }
            canvas.save();
            boolean z = this.scaled;
            if ((z && this.scale != 0.8f) || (!z && this.scale != 1.0f)) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = currentTimeMillis - this.lastUpdateTime;
                this.lastUpdateTime = currentTimeMillis;
                if (this.scaled) {
                    float f2 = this.scale;
                    if (f2 != 0.8f) {
                        float f3 = f2 - (((float) j) / 400.0f);
                        this.scale = f3;
                        if (f3 < 0.8f) {
                            this.scale = 0.8f;
                        }
                        invalidate();
                    }
                }
                float f4 = this.scale + (((float) j) / 400.0f);
                this.scale = f4;
                if (f4 > 1.0f) {
                    this.scale = 1.0f;
                }
                invalidate();
            }
            float f5 = this.scale;
            float f6 = this.imageScale;
            canvas.scale(f5 * f6, f5 * f6, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            this.linkImageView.draw(canvas);
            canvas.restore();
        }
        if (this.mediaWebpage && ((i = this.documentAttachType) == 7 || i == 2)) {
            this.radialProgress.draw(canvas);
        }
        if (this.needDivider && !this.mediaWebpage) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas.drawLine(AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
        if (this.needShadow) {
            Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(3.0f));
            Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(canvas);
        }
    }

    private int getIconForCurrentState() {
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
            int i2 = this.buttonState;
            if (i2 == 1) {
                return 1;
            }
            if (i2 == 2) {
                return 2;
            }
            return i2 == 4 ? 3 : 0;
        }
        this.radialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
        return this.buttonState == 1 ? 10 : 4;
    }

    public void updateButtonState(boolean z, boolean z2) {
        boolean z3;
        String str = this.fileName;
        if (str == null && !this.resolvingFileName) {
            this.resolvingFileName = true;
            int i = this.resolveFileNameId;
            this.resolveFileNameId = i + 1;
            this.resolveFileNameId = i;
            Utilities.searchQueue.postRunnable(new AnonymousClass1(i, z));
            this.radialProgress.setIcon(4, z, false);
        } else if (TextUtils.isEmpty(str)) {
            this.buttonState = -1;
            this.radialProgress.setIcon(4, z, false);
        } else {
            if (this.documentAttach != null) {
                z3 = FileLoader.getInstance(this.currentAccount).isLoadingFile(this.fileName);
            } else {
                z3 = ImageLoader.getInstance().isLoadingHttpFile(this.fileName);
            }
            if (z3 || !this.fileExist) {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(this.fileName, this);
                int i2 = this.documentAttachType;
                float f = 0.0f;
                if (i2 != 5 && i2 != 3) {
                    this.buttonState = 1;
                    Float fileProgress = ImageLoader.getInstance().getFileProgress(this.fileName);
                    if (fileProgress != null) {
                        f = fileProgress.floatValue();
                    }
                    this.radialProgress.setProgress(f, false);
                } else if (!z3) {
                    this.buttonState = 2;
                } else {
                    this.buttonState = 4;
                    Float fileProgress2 = ImageLoader.getInstance().getFileProgress(this.fileName);
                    if (fileProgress2 != null) {
                        this.radialProgress.setProgress(fileProgress2.floatValue(), z2);
                    } else {
                        this.radialProgress.setProgress(0.0f, z2);
                    }
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

    /* renamed from: org.telegram.ui.Cells.ContextLinkCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean val$ifSame;
        final /* synthetic */ int val$localId;

        AnonymousClass1(int i, boolean z) {
            ContextLinkCell.this = r1;
            this.val$localId = i;
            this.val$ifSame = z;
        }

        /* JADX WARN: Removed duplicated region for block: B:35:0x019c  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            File file;
            String str;
            File file2;
            File file3;
            String str2;
            File file4;
            String str3 = null;
            if (ContextLinkCell.this.documentAttachType == 5 || ContextLinkCell.this.documentAttachType == 3) {
                if (ContextLinkCell.this.documentAttach != null) {
                    str3 = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                    file2 = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.documentAttach);
                } else {
                    if (ContextLinkCell.this.inlineResult.content instanceof TLRPC$TL_webDocument) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(Utilities.MD5(ContextLinkCell.this.inlineResult.content.url));
                        sb.append(".");
                        sb.append(ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, ContextLinkCell.this.documentAttachType == 5 ? "mp3" : "ogg"));
                        str3 = sb.toString();
                        file2 = new File(FileLoader.getDirectory(4), str3);
                    }
                    str = null;
                    file = null;
                }
                file = file2;
                str = str3;
            } else {
                if (ContextLinkCell.this.mediaWebpage) {
                    if (ContextLinkCell.this.inlineResult != null) {
                        if (ContextLinkCell.this.inlineResult.document instanceof TLRPC$TL_document) {
                            str2 = FileLoader.getAttachFileName(ContextLinkCell.this.inlineResult.document);
                            file3 = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.inlineResult.document);
                        } else if (!(ContextLinkCell.this.inlineResult.photo instanceof TLRPC$TL_photo)) {
                            if (!(ContextLinkCell.this.inlineResult.content instanceof TLRPC$TL_webDocument)) {
                                if (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC$TL_webDocument) {
                                    str2 = Utilities.MD5(ContextLinkCell.this.inlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.thumb.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.thumb.mime_type));
                                    file4 = new File(FileLoader.getDirectory(4), str2);
                                }
                                str2 = null;
                                file3 = null;
                            } else {
                                str2 = Utilities.MD5(ContextLinkCell.this.inlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(ContextLinkCell.this.inlineResult.content.url, FileLoader.getMimeTypePart(ContextLinkCell.this.inlineResult.content.mime_type));
                                file4 = new File(FileLoader.getDirectory(4), str2);
                                if (ContextLinkCell.this.documentAttachType == 2 && (ContextLinkCell.this.inlineResult.thumb instanceof TLRPC$TL_webDocument) && "video/mp4".equals(ContextLinkCell.this.inlineResult.thumb.mime_type)) {
                                    file3 = file4;
                                    str2 = null;
                                }
                            }
                            file3 = file4;
                        } else {
                            ContextLinkCell contextLinkCell = ContextLinkCell.this;
                            contextLinkCell.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(contextLinkCell.inlineResult.photo.sizes, AndroidUtilities.getPhotoSize(), true);
                            str2 = FileLoader.getAttachFileName(ContextLinkCell.this.currentPhotoObject);
                            file3 = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.currentPhotoObject);
                        }
                        if (ContextLinkCell.this.documentAttach == null && ContextLinkCell.this.documentAttachType == 2 && MessageObject.getDocumentVideoThumb(ContextLinkCell.this.documentAttach) != null) {
                            file = file3;
                            str = str3;
                        } else {
                            str = str2;
                            file = file3;
                        }
                    } else {
                        if (ContextLinkCell.this.documentAttach != null) {
                            str2 = FileLoader.getAttachFileName(ContextLinkCell.this.documentAttach);
                            file3 = FileLoader.getInstance(ContextLinkCell.this.currentAccount).getPathToAttach(ContextLinkCell.this.documentAttach);
                            if (ContextLinkCell.this.documentAttach == null) {
                            }
                            str = str2;
                            file = file3;
                        }
                        str2 = null;
                        file3 = null;
                        if (ContextLinkCell.this.documentAttach == null) {
                        }
                        str = str2;
                        file = file3;
                    }
                }
                str = null;
                file = null;
            }
            AndroidUtilities.runOnUIThread(new ContextLinkCell$1$$ExternalSyntheticLambda0(this, this.val$localId, str, file, !TextUtils.isEmpty(str) && file.exists(), this.val$ifSame));
        }

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
    }

    public void setDelegate(ContextLinkCellDelegate contextLinkCellDelegate) {
        this.delegate = contextLinkCellDelegate;
    }

    public TLRPC$BotInlineResult getResult() {
        return this.inlineResult;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        this.fileExist = true;
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
        int i = this.documentAttachType;
        if (i == 3 || i == 5) {
            if (this.buttonState == 4) {
                return;
            }
            updateButtonState(false, true);
        } else if (this.buttonState == 1) {
        } else {
            updateButtonState(false, true);
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        switch (this.documentAttachType) {
            case 1:
                sb.append(LocaleController.getString("AttachDocument", 2131624492));
                break;
            case 2:
                sb.append(LocaleController.getString("AttachGif", 2131624494));
                break;
            case 3:
                sb.append(LocaleController.getString("AttachAudio", 2131624487));
                break;
            case 4:
                sb.append(LocaleController.getString("AttachVideo", 2131624519));
                break;
            case 5:
                sb.append(LocaleController.getString("AttachMusic", 2131624512));
                break;
            case 6:
                sb.append(LocaleController.getString("AttachSticker", 2131624516));
                break;
            case 7:
                sb.append(LocaleController.getString("AttachPhoto", 2131624513));
                break;
            case 8:
                sb.append(LocaleController.getString("AttachLocation", 2131624503));
                break;
        }
        StaticLayout staticLayout = this.titleLayout;
        boolean z = staticLayout != null && !TextUtils.isEmpty(staticLayout.getText());
        StaticLayout staticLayout2 = this.descriptionLayout;
        boolean z2 = staticLayout2 != null && !TextUtils.isEmpty(staticLayout2.getText());
        if (this.documentAttachType != 5 || !z || !z2) {
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
        } else {
            sb.append(", ");
            sb.append(LocaleController.formatString("AccDescrMusicInfo", 2131624010, this.descriptionLayout.getText(), this.titleLayout.getText()));
        }
        accessibilityNodeInfo.setText(sb);
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null || !checkBox2.isChecked()) {
            return;
        }
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(true);
    }

    /* renamed from: org.telegram.ui.Cells.ContextLinkCell$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimationProperties.FloatProperty<ContextLinkCell> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str) {
            super(str);
            ContextLinkCell.this = r1;
        }

        public void setValue(ContextLinkCell contextLinkCell, float f) {
            ContextLinkCell.this.imageScale = f;
            ContextLinkCell.this.invalidate();
        }

        public Float get(ContextLinkCell contextLinkCell) {
            return Float.valueOf(ContextLinkCell.this.imageScale);
        }
    }

    public void setChecked(boolean z, boolean z2) {
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
        float f = 1.0f;
        if (z2) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animator = animatorSet2;
            Animator[] animatorArr = new Animator[1];
            Property<ContextLinkCell, Float> property = this.IMAGE_SCALE;
            float[] fArr = new float[1];
            if (z) {
                f = 0.81f;
            }
            fArr[0] = f;
            animatorArr[0] = ObjectAnimator.ofFloat(this, property, fArr);
            animatorSet2.playTogether(animatorArr);
            this.animator.setDuration(200L);
            this.animator.addListener(new AnonymousClass3(z));
            this.animator.start();
            return;
        }
        if (z) {
            f = 0.85f;
        }
        this.imageScale = f;
        invalidate();
    }

    /* renamed from: org.telegram.ui.Cells.ContextLinkCell$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$checked;

        AnonymousClass3(boolean z) {
            ContextLinkCell.this = r1;
            this.val$checked = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                return;
            }
            ContextLinkCell.this.animator = null;
            if (this.val$checked) {
                return;
            }
            ContextLinkCell.this.setBackgroundColor(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (ContextLinkCell.this.animator == null || !ContextLinkCell.this.animator.equals(animator)) {
                return;
            }
            ContextLinkCell.this.animator = null;
        }
    }
}
