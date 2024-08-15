package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes4.dex */
public class SharedLinkCell extends FrameLayout {
    private StaticLayout captionLayout;
    private TextPaint captionTextPaint;
    private int captionY;
    private CheckBox2 checkBox;
    private boolean checkingForLongPress;
    private StaticLayout dateLayout;
    private int dateLayoutX;
    private SharedLinkCellDelegate delegate;
    private TextPaint description2TextPaint;
    private int description2Y;
    private StaticLayout descriptionLayout;
    private StaticLayout descriptionLayout2;
    private List<SpoilerEffect> descriptionLayout2Spoilers;
    private List<SpoilerEffect> descriptionLayoutSpoilers;
    private TextPaint descriptionTextPaint;
    private int descriptionY;
    private boolean drawLinkImageView;
    private StaticLayout fromInfoLayout;
    private AnimatedEmojiSpan.EmojiGroupedSpans fromInfoLayoutEmojis;
    private int fromInfoLayoutY;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList<StaticLayout> linkLayout;
    private boolean linkPreviewPressed;
    private SparseArray<List<SpoilerEffect>> linkSpoilers;
    private int linkY;
    ArrayList<CharSequence> links;
    private LinkSpanDrawable.LinkCollector linksCollector;
    private MessageObject message;
    private boolean needDivider;
    private AtomicReference<Layout> patchedDescriptionLayout;
    private AtomicReference<Layout> patchedDescriptionLayout2;
    private Path path;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    private LinkSpanDrawable pressedLink;
    private int pressedLinkIndex;
    private Theme.ResourcesProvider resourcesProvider;
    private SpoilerEffect spoilerPressed;
    private int spoilerTypePressed;
    private Stack<SpoilerEffect> spoilersPool;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint;
    private int titleY;
    private int viewType;

    /* loaded from: classes4.dex */
    public interface SharedLinkCellDelegate {
        boolean canPerformActions();

        void needOpenWebView(TLRPC$WebPage tLRPC$WebPage, MessageObject messageObject);

        void onLinkPress(String str, boolean z);
    }

    static /* synthetic */ int access$104(SharedLinkCell sharedLinkCell) {
        int i = sharedLinkCell.pressCount + 1;
        sharedLinkCell.pressCount = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class CheckForTap implements Runnable {
        private CheckForTap() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.pendingCheckForLongPress == null) {
                SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                sharedLinkCell.pendingCheckForLongPress = new CheckForLongPress();
            }
            SharedLinkCell.this.pendingCheckForLongPress.currentPressCount = SharedLinkCell.access$104(SharedLinkCell.this);
            SharedLinkCell sharedLinkCell2 = SharedLinkCell.this;
            sharedLinkCell2.postDelayed(sharedLinkCell2.pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class CheckForLongPress implements Runnable {
        public int currentPressCount;

        CheckForLongPress() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
                SharedLinkCell.this.checkingForLongPress = false;
                SharedLinkCell.this.performHapticFeedback(0);
                if (SharedLinkCell.this.pressedLinkIndex >= 0) {
                    SharedLinkCellDelegate sharedLinkCellDelegate = SharedLinkCell.this.delegate;
                    SharedLinkCell sharedLinkCell = SharedLinkCell.this;
                    sharedLinkCellDelegate.onLinkPress(sharedLinkCell.links.get(sharedLinkCell.pressedLinkIndex).toString(), true);
                }
                MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                SharedLinkCell.this.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
    }

    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        postDelayed(this.pendingCheckForTap, ViewConfiguration.getTapTimeout());
    }

    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        CheckForLongPress checkForLongPress = this.pendingCheckForLongPress;
        if (checkForLongPress != null) {
            removeCallbacks(checkForLongPress);
        }
        CheckForTap checkForTap = this.pendingCheckForTap;
        if (checkForTap != null) {
            removeCallbacks(checkForTap);
        }
    }

    public SharedLinkCell(Context context, int i) {
        this(context, i, null);
    }

    public SharedLinkCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.linksCollector = new LinkSpanDrawable.LinkCollector(this);
        this.links = new ArrayList<>();
        this.linkLayout = new ArrayList<>();
        this.linkSpoilers = new SparseArray<>();
        this.descriptionLayoutSpoilers = new ArrayList();
        this.descriptionLayout2Spoilers = new ArrayList();
        this.spoilersPool = new Stack<>();
        this.path = new Path();
        this.spoilerTypePressed = -1;
        this.titleY = AndroidUtilities.dp(10.0f);
        this.descriptionY = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout = new AtomicReference<>();
        this.description2Y = AndroidUtilities.dp(30.0f);
        this.patchedDescriptionLayout2 = new AtomicReference<>();
        this.captionY = AndroidUtilities.dp(30.0f);
        this.fromInfoLayoutY = AndroidUtilities.dp(30.0f);
        this.resourcesProvider = resourcesProvider;
        this.viewType = i;
        setFocusable(true);
        TextPaint textPaint = new TextPaint(1);
        this.titleTextPaint = textPaint;
        textPaint.setTypeface(AndroidUtilities.bold());
        this.titleTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.descriptionTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        setWillNotDraw(false);
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
        this.letterDrawable = new LetterDrawable(resourcesProvider, 0);
        CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setVisibility(4);
        this.checkBox.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        CheckBox2 checkBox22 = this.checkBox;
        boolean z = LocaleController.isRTL;
        addView(checkBox22, LayoutHelper.createFrame(24, 24.0f, (z ? 5 : 3) | 48, z ? 0.0f : 44.0f, 44.0f, z ? 44.0f : 0.0f, 0.0f));
        if (i == 1) {
            TextPaint textPaint2 = new TextPaint(1);
            this.description2TextPaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        }
        TextPaint textPaint3 = new TextPaint(1);
        this.captionTextPaint = textPaint3;
        textPaint3.setTextSize(AndroidUtilities.dp(13.0f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0305  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0363  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x036f  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0372  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x047b  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x049b  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x056a  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0575  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x058f  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x06b0  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x06df  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x030a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:283:0x01fd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0377 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:289:0x03c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r7v40, types: [android.text.SpannableStringBuilder, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r7v61, types: [android.text.SpannableStringBuilder, android.text.Spannable] */
    @Override // android.widget.FrameLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        String str;
        String str2;
        String str3;
        boolean z;
        MessageObject messageObject;
        SpannableStringBuilder spannableStringBuilder;
        int i3;
        String str4;
        MessageObject messageObject2;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        StaticLayout staticLayout;
        boolean z2;
        StaticLayout staticLayout2;
        int i4;
        int i5;
        StaticLayout staticLayout3;
        StaticLayout staticLayout4;
        StaticLayout staticLayout5;
        int i6;
        TLRPC$PhotoSize tLRPC$PhotoSize2;
        CharSequence charSequence;
        CharSequence ellipsize;
        int i7;
        String str5;
        String host;
        int lastIndexOf;
        String str6;
        int i8;
        String str7;
        String str8;
        int i9 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.descriptionLayout2 = null;
        this.captionLayout = null;
        this.linkLayout.clear();
        this.links.clear();
        int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        MessageObject messageObject3 = this.message;
        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject3.messageOwner.media;
        int i10 = 1;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia.webpage;
            if (tLRPC$WebPage instanceof TLRPC$TL_webPage) {
                if (messageObject3.photoThumbs == null && tLRPC$WebPage.photo != null) {
                    messageObject3.generateThumbs(true);
                }
                boolean z3 = (tLRPC$WebPage.photo == null || this.message.photoThumbs == null) ? false : true;
                str2 = tLRPC$WebPage.title;
                if (str2 == null) {
                    str2 = tLRPC$WebPage.site_name;
                }
                str3 = tLRPC$WebPage.description;
                str = tLRPC$WebPage.url;
                z = z3;
                messageObject = this.message;
                if (messageObject != null || messageObject.messageOwner.entities.isEmpty()) {
                    spannableStringBuilder = null;
                } else {
                    SpannableStringBuilder spannableStringBuilder2 = null;
                    int i11 = 0;
                    while (i11 < this.message.messageOwner.entities.size()) {
                        TLRPC$MessageEntity tLRPC$MessageEntity = this.message.messageOwner.entities.get(i11);
                        if (tLRPC$MessageEntity.length > 0 && (i7 = tLRPC$MessageEntity.offset) >= 0 && i7 < this.message.messageOwner.message.length()) {
                            if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > this.message.messageOwner.message.length()) {
                                tLRPC$MessageEntity.length = this.message.messageOwner.message.length() - tLRPC$MessageEntity.offset;
                            }
                            if (i11 == 0 && str != null && (tLRPC$MessageEntity.offset != 0 || tLRPC$MessageEntity.length != this.message.messageOwner.message.length())) {
                                if (this.message.messageOwner.entities.size() != i10) {
                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder2);
                                } else if (str3 == null) {
                                    spannableStringBuilder2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder2);
                                }
                            }
                            SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder2;
                            try {
                                if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl)) {
                                    if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityEmail) && (str2 == null || str2.length() == 0)) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("mailto:");
                                        String str9 = this.message.messageOwner.message;
                                        int i12 = tLRPC$MessageEntity.offset;
                                        sb.append(str9.substring(i12, tLRPC$MessageEntity.length + i12));
                                        str5 = sb.toString();
                                        String str10 = this.message.messageOwner.message;
                                        int i13 = tLRPC$MessageEntity.offset;
                                        host = str10.substring(i13, tLRPC$MessageEntity.length + i13);
                                        try {
                                            if (tLRPC$MessageEntity.offset != 0 || tLRPC$MessageEntity.length != this.message.messageOwner.message.length()) {
                                                ?? valueOf = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                                MediaDataController.addTextStyleRuns(this.message, valueOf);
                                                str6 = valueOf;
                                                str3 = str6;
                                            }
                                            str2 = host;
                                            if (str5 != null) {
                                            }
                                            spannableStringBuilder2 = spannableStringBuilder3;
                                            str2 = str7;
                                        } catch (Exception e) {
                                            e = e;
                                            str2 = host;
                                            FileLog.e(e);
                                            spannableStringBuilder2 = spannableStringBuilder3;
                                            i11++;
                                            i9 = 0;
                                            i10 = 1;
                                        }
                                    } else {
                                        str5 = null;
                                        if (str5 != null) {
                                            try {
                                                if (AndroidUtilities.charSequenceContains(str5, "://") || str5.toString().toLowerCase().indexOf("http") == 0 || str5.toString().toLowerCase().indexOf("mailto") == 0) {
                                                    i8 = 0;
                                                } else {
                                                    str5 = "http://" + ((Object) str5);
                                                    i8 = 7;
                                                }
                                                SpannableString valueOf2 = SpannableString.valueOf(str5);
                                                int i14 = tLRPC$MessageEntity.offset;
                                                int i15 = tLRPC$MessageEntity.length + i14;
                                                Iterator<TLRPC$MessageEntity> it = this.message.messageOwner.entities.iterator();
                                                while (it.hasNext()) {
                                                    TLRPC$MessageEntity next = it.next();
                                                    int i16 = next.offset;
                                                    int i17 = next.length + i16;
                                                    if (!(next instanceof TLRPC$TL_messageEntitySpoiler) || i14 > i17 || i15 < i16) {
                                                        str8 = str2;
                                                    } else {
                                                        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                                        str8 = str2;
                                                        try {
                                                            textStyleRun.flags |= LiteMode.FLAG_CHAT_BLUR;
                                                            valueOf2.setSpan(new TextStyleSpan(textStyleRun), Math.max(i14, i16), Math.min(i15, i17) + i8, 33);
                                                        } catch (Exception e2) {
                                                            e = e2;
                                                            str2 = str8;
                                                            FileLog.e(e);
                                                            spannableStringBuilder2 = spannableStringBuilder3;
                                                            i11++;
                                                            i9 = 0;
                                                            i10 = 1;
                                                        }
                                                    }
                                                    str2 = str8;
                                                }
                                                str7 = str2;
                                                this.links.add(valueOf2);
                                            } catch (Exception e3) {
                                                e = e3;
                                            }
                                        } else {
                                            str7 = str2;
                                        }
                                        spannableStringBuilder2 = spannableStringBuilder3;
                                        str2 = str7;
                                    }
                                }
                                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) {
                                    String str11 = this.message.messageOwner.message;
                                    int i18 = tLRPC$MessageEntity.offset;
                                    str5 = str11.substring(i18, tLRPC$MessageEntity.length + i18);
                                } else {
                                    str5 = tLRPC$MessageEntity.url;
                                }
                                if (str2 == null || str2.length() == 0) {
                                    host = Uri.parse(str5.toString()).getHost();
                                    if (host == null) {
                                        host = str5.toString();
                                    }
                                    if (host != null && (lastIndexOf = host.lastIndexOf(46)) >= 0) {
                                        String substring = host.substring(i9, lastIndexOf);
                                        int lastIndexOf2 = substring.lastIndexOf(46);
                                        if (lastIndexOf2 >= 0) {
                                            substring = substring.substring(lastIndexOf2 + 1);
                                        }
                                        host = substring.substring(i9, i10).toUpperCase() + substring.substring(i10);
                                    }
                                    if (tLRPC$MessageEntity.offset != 0 || tLRPC$MessageEntity.length != this.message.messageOwner.message.length()) {
                                        ?? valueOf3 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                        MediaDataController.addTextStyleRuns(this.message, valueOf3);
                                        str6 = valueOf3;
                                        str3 = str6;
                                    }
                                    str2 = host;
                                }
                                if (str5 != null) {
                                }
                                spannableStringBuilder2 = spannableStringBuilder3;
                                str2 = str7;
                            } catch (Exception e4) {
                                e = e4;
                            }
                        }
                        i11++;
                        i9 = 0;
                        i10 = 1;
                    }
                    spannableStringBuilder = spannableStringBuilder2;
                }
                if (str != null && this.links.isEmpty()) {
                    this.links.add(str);
                }
                if (this.viewType != 1) {
                    String stringForMessageListDate = LocaleController.stringForMessageListDate(this.message.messageOwner.date);
                    int ceil = (int) Math.ceil(this.description2TextPaint.measureText(stringForMessageListDate));
                    this.dateLayout = ChatMessageCell.generateStaticLayout(stringForMessageListDate, this.description2TextPaint, ceil, ceil, 0, 1);
                    this.dateLayoutX = (size - ceil) - AndroidUtilities.dp(8.0f);
                    i3 = ceil + AndroidUtilities.dp(12.0f);
                } else {
                    i3 = 0;
                }
                if (str2 != null) {
                    try {
                        ?? highlightText = AndroidUtilities.highlightText(str2, this.message.highlightedWords, (Theme.ResourcesProvider) null);
                        int i19 = size - i3;
                        StaticLayout generateStaticLayout = ChatMessageCell.generateStaticLayout(highlightText != 0 ? highlightText : str2, this.titleTextPaint, i19 - AndroidUtilities.dp(4.0f), i19 - AndroidUtilities.dp(4.0f), 0, 3);
                        this.titleLayout = generateStaticLayout;
                        if (generateStaticLayout.getLineCount() > 0) {
                            int i20 = this.titleY;
                            StaticLayout staticLayout6 = this.titleLayout;
                            this.descriptionY = i20 + staticLayout6.getLineBottom(staticLayout6.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                        }
                    } catch (Exception e5) {
                        FileLog.e(e5);
                    }
                    this.letterDrawable.setTitle(str2);
                }
                this.description2Y = this.descriptionY;
                StaticLayout staticLayout7 = this.titleLayout;
                int max = Math.max(1, 4 - (staticLayout7 == null ? staticLayout7.getLineCount() : 0));
                if (this.viewType != 1) {
                    spannableStringBuilder = null;
                    str4 = null;
                } else {
                    str4 = str3;
                }
                if (str4 != null) {
                    try {
                        StaticLayout generateStaticLayout2 = ChatMessageCell.generateStaticLayout(str4, this.descriptionTextPaint, size, size, 0, max);
                        this.descriptionLayout = generateStaticLayout2;
                        if (generateStaticLayout2.getLineCount() > 0) {
                            int i21 = this.descriptionY;
                            StaticLayout staticLayout8 = this.descriptionLayout;
                            this.description2Y = i21 + staticLayout8.getLineBottom(staticLayout8.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                        }
                        this.spoilersPool.addAll(this.descriptionLayoutSpoilers);
                        this.descriptionLayoutSpoilers.clear();
                        if (!this.message.isSpoilersRevealed) {
                            SpoilerEffect.addSpoilers(this, this.descriptionLayout, this.spoilersPool, this.descriptionLayoutSpoilers);
                        }
                    } catch (Exception e6) {
                        FileLog.e(e6);
                    }
                }
                if (spannableStringBuilder != null) {
                    try {
                        this.descriptionLayout2 = ChatMessageCell.generateStaticLayout(spannableStringBuilder, this.descriptionTextPaint, size, size, 0, max);
                        if (this.descriptionLayout != null) {
                            this.description2Y += AndroidUtilities.dp(10.0f);
                        }
                        this.spoilersPool.addAll(this.descriptionLayout2Spoilers);
                        this.descriptionLayout2Spoilers.clear();
                        if (!this.message.isSpoilersRevealed) {
                            SpoilerEffect.addSpoilers(this, this.descriptionLayout2, this.spoilersPool, this.descriptionLayout2Spoilers);
                        }
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                messageObject2 = this.message;
                if (messageObject2 != null || TextUtils.isEmpty(messageObject2.messageOwner.message)) {
                    tLRPC$PhotoSize = null;
                } else {
                    tLRPC$PhotoSize = null;
                    CharSequence highlightText2 = AndroidUtilities.highlightText(Emoji.replaceEmoji((CharSequence) this.message.messageOwner.message.replace("\n", " ").replaceAll(" +", " ").trim(), Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false), this.message.highlightedWords, (Theme.ResourcesProvider) null);
                    if (highlightText2 != null) {
                        this.captionLayout = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.ellipsizeCenterEnd(highlightText2, this.message.highlightedWords.get(0), size, this.captionTextPaint, 130), this.captionTextPaint, size, TextUtils.TruncateAt.END), this.captionTextPaint, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                }
                staticLayout = this.captionLayout;
                if (staticLayout != null) {
                    int i22 = this.descriptionY;
                    this.captionY = i22;
                    int lineBottom = i22 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    this.descriptionY = lineBottom;
                    this.description2Y = lineBottom;
                }
                if (!this.links.isEmpty()) {
                    for (int i23 = 0; i23 < this.linkSpoilers.size(); i23++) {
                        this.spoilersPool.addAll(this.linkSpoilers.get(i23));
                    }
                    this.linkSpoilers.clear();
                    int i24 = 0;
                    while (i24 < this.links.size()) {
                        try {
                            ellipsize = TextUtils.ellipsize(AndroidUtilities.replaceNewLines(SpannableStringBuilder.valueOf(this.links.get(i24))), this.descriptionTextPaint, Math.min((int) Math.ceil(this.descriptionTextPaint.measureText(charSequence, 0, charSequence.length())), size), TextUtils.TruncateAt.MIDDLE);
                            tLRPC$PhotoSize2 = tLRPC$PhotoSize;
                        } catch (Exception e8) {
                            e = e8;
                            tLRPC$PhotoSize2 = tLRPC$PhotoSize;
                        }
                        try {
                            StaticLayout staticLayout9 = new StaticLayout(ellipsize, this.descriptionTextPaint, size, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.linkY = this.description2Y;
                            StaticLayout staticLayout10 = this.descriptionLayout2;
                            if (staticLayout10 != null && staticLayout10.getLineCount() != 0) {
                                int i25 = this.linkY;
                                StaticLayout staticLayout11 = this.descriptionLayout2;
                                this.linkY = i25 + staticLayout11.getLineBottom(staticLayout11.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                            }
                            if (!this.message.isSpoilersRevealed) {
                                ArrayList arrayList = new ArrayList();
                                if (ellipsize instanceof Spannable) {
                                    SpoilerEffect.addSpoilers(this, staticLayout9, (Spannable) ellipsize, this.spoilersPool, arrayList);
                                }
                                this.linkSpoilers.put(i24, arrayList);
                            }
                            this.linkLayout.add(staticLayout9);
                        } catch (Exception e9) {
                            e = e9;
                            FileLog.e(e);
                            i24++;
                            tLRPC$PhotoSize = tLRPC$PhotoSize2;
                        }
                        i24++;
                        tLRPC$PhotoSize = tLRPC$PhotoSize2;
                    }
                }
                TLRPC$PhotoSize tLRPC$PhotoSize3 = tLRPC$PhotoSize;
                int dp = AndroidUtilities.dp(52.0f);
                int size2 = !LocaleController.isRTL ? (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(10.0f)) - dp : AndroidUtilities.dp(10.0f);
                this.letterDrawable.setBounds(size2, AndroidUtilities.dp(11.0f), size2 + dp, AndroidUtilities.dp(63.0f));
                if (z) {
                    z2 = true;
                } else {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, dp, true);
                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80);
                    if (closestPhotoSizeWithSize2 == closestPhotoSizeWithSize) {
                        closestPhotoSizeWithSize2 = tLRPC$PhotoSize3;
                    }
                    if (closestPhotoSizeWithSize != null) {
                        closestPhotoSizeWithSize.size = -1;
                    }
                    if (closestPhotoSizeWithSize2 != null) {
                        closestPhotoSizeWithSize2.size = -1;
                    }
                    float f = dp;
                    this.linkImageView.setImageCoords(size2, AndroidUtilities.dp(11.0f), f, f);
                    FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    Locale locale = Locale.US;
                    this.linkImageView.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), String.format(locale, "%d_%d", Integer.valueOf(dp), Integer.valueOf(dp)), ImageLocation.getForObject(closestPhotoSizeWithSize2, this.message.photoThumbsObject), String.format(locale, "%d_%d_b", Integer.valueOf(dp), Integer.valueOf(dp)), 0L, null, this.message, 0);
                    z2 = true;
                    this.drawLinkImageView = true;
                }
                if (this.viewType == z2) {
                    StaticLayout generateStaticLayout3 = ChatMessageCell.generateStaticLayout(FilteredSearchView.createFromInfoString(this.message, z2, 2, this.description2TextPaint), this.description2TextPaint, size, size, 0, max);
                    this.fromInfoLayout = generateStaticLayout3;
                    this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.fromInfoLayoutEmojis, generateStaticLayout3);
                }
                staticLayout2 = this.titleLayout;
                if (staticLayout2 != null || staticLayout2.getLineCount() == 0) {
                    i4 = 0;
                    i5 = 0;
                } else {
                    StaticLayout staticLayout12 = this.titleLayout;
                    i4 = 0;
                    i5 = staticLayout12.getLineBottom(staticLayout12.getLineCount() - 1) + AndroidUtilities.dp(4.0f) + 0;
                }
                staticLayout3 = this.captionLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() != 0) {
                    StaticLayout staticLayout13 = this.captionLayout;
                    i5 += staticLayout13.getLineBottom(staticLayout13.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                staticLayout4 = this.descriptionLayout;
                if (staticLayout4 != null && staticLayout4.getLineCount() != 0) {
                    StaticLayout staticLayout14 = this.descriptionLayout;
                    i5 += staticLayout14.getLineBottom(staticLayout14.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                staticLayout5 = this.descriptionLayout2;
                if (staticLayout5 != null && staticLayout5.getLineCount() != 0) {
                    StaticLayout staticLayout15 = this.descriptionLayout2;
                    i5 += staticLayout15.getLineBottom(staticLayout15.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    if (this.descriptionLayout != null) {
                        i5 += AndroidUtilities.dp(10.0f);
                    }
                }
                for (i6 = 0; i6 < this.linkLayout.size(); i6++) {
                    StaticLayout staticLayout16 = this.linkLayout.get(i6);
                    if (staticLayout16.getLineCount() > 0) {
                        i4 += staticLayout16.getLineBottom(staticLayout16.getLineCount() - 1);
                    }
                }
                int i26 = i5 + i4;
                if (this.fromInfoLayout != null) {
                    this.fromInfoLayoutY = this.linkY + i4 + AndroidUtilities.dp(5.0f);
                    StaticLayout staticLayout17 = this.fromInfoLayout;
                    i26 += staticLayout17.getLineBottom(staticLayout17.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
                setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(76.0f), i26 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
            }
        }
        str = null;
        str2 = null;
        str3 = null;
        z = false;
        messageObject = this.message;
        if (messageObject != null) {
        }
        spannableStringBuilder = null;
        if (str != null) {
            this.links.add(str);
        }
        if (this.viewType != 1) {
        }
        if (str2 != null) {
        }
        this.description2Y = this.descriptionY;
        StaticLayout staticLayout72 = this.titleLayout;
        int max2 = Math.max(1, 4 - (staticLayout72 == null ? staticLayout72.getLineCount() : 0));
        if (this.viewType != 1) {
        }
        if (str4 != null) {
        }
        if (spannableStringBuilder != null) {
        }
        messageObject2 = this.message;
        if (messageObject2 != null) {
        }
        tLRPC$PhotoSize = null;
        staticLayout = this.captionLayout;
        if (staticLayout != null) {
        }
        if (!this.links.isEmpty()) {
        }
        TLRPC$PhotoSize tLRPC$PhotoSize32 = tLRPC$PhotoSize;
        int dp2 = AndroidUtilities.dp(52.0f);
        if (!LocaleController.isRTL) {
        }
        this.letterDrawable.setBounds(size2, AndroidUtilities.dp(11.0f), size2 + dp2, AndroidUtilities.dp(63.0f));
        if (z) {
        }
        if (this.viewType == z2) {
        }
        staticLayout2 = this.titleLayout;
        if (staticLayout2 != null) {
        }
        i4 = 0;
        i5 = 0;
        staticLayout3 = this.captionLayout;
        if (staticLayout3 != null) {
            StaticLayout staticLayout132 = this.captionLayout;
            i5 += staticLayout132.getLineBottom(staticLayout132.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        staticLayout4 = this.descriptionLayout;
        if (staticLayout4 != null) {
            StaticLayout staticLayout142 = this.descriptionLayout;
            i5 += staticLayout142.getLineBottom(staticLayout142.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        staticLayout5 = this.descriptionLayout2;
        if (staticLayout5 != null) {
            StaticLayout staticLayout152 = this.descriptionLayout2;
            i5 += staticLayout152.getLineBottom(staticLayout152.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            if (this.descriptionLayout != null) {
            }
        }
        while (i6 < this.linkLayout.size()) {
        }
        int i262 = i5 + i4;
        if (this.fromInfoLayout != null) {
        }
        this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(76.0f), i262 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
    }

    public void setLink(MessageObject messageObject, boolean z) {
        this.needDivider = z;
        resetPressedLink();
        this.message = messageObject;
        requestLayout();
    }

    public ImageReceiver getLinkImageView() {
        return this.linkImageView;
    }

    public void setDelegate(SharedLinkCellDelegate sharedLinkCellDelegate) {
        this.delegate = sharedLinkCellDelegate;
    }

    public MessageObject getMessage() {
        return this.message;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
        AnimatedEmojiSpan.release(this, this.fromInfoLayoutEmojis);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
        this.fromInfoLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.fromInfoLayoutEmojis, this.fromInfoLayout);
    }

    /* JADX WARN: Removed duplicated region for block: B:133:0x0222  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        SharedLinkCellDelegate sharedLinkCellDelegate;
        boolean z2;
        int i;
        int i2;
        String str;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        if (this.message != null && !this.linkLayout.isEmpty() && (sharedLinkCellDelegate = this.delegate) != null && sharedLinkCellDelegate.canPerformActions()) {
            if (motionEvent.getAction() == 0 || ((this.linkPreviewPressed || this.spoilerPressed != null) && motionEvent.getAction() == 1)) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    if (i3 >= this.linkLayout.size()) {
                        z = false;
                        z2 = false;
                        break;
                    }
                    StaticLayout staticLayout = this.linkLayout.get(i3);
                    if (staticLayout.getLineCount() > 0) {
                        int lineBottom = staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                        int dp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                        float f = x;
                        float f2 = dp;
                        if (f >= staticLayout.getLineLeft(0) + f2 && f <= staticLayout.getLineWidth(0) + f2) {
                            int i5 = this.linkY;
                            if (y >= i5 + i4 && y <= i5 + i4 + lineBottom) {
                                TLRPC$WebPage tLRPC$WebPage = null;
                                if (motionEvent.getAction() == 0) {
                                    this.spoilerPressed = null;
                                    if (this.linkSpoilers.get(i3, null) != null) {
                                        Iterator<SpoilerEffect> it = this.linkSpoilers.get(i3).iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            SpoilerEffect next = it.next();
                                            if (next.getBounds().contains(x - dp, (y - this.linkY) - i4)) {
                                                resetPressedLink();
                                                this.spoilerPressed = next;
                                                this.spoilerTypePressed = 0;
                                                break;
                                            }
                                        }
                                    }
                                    if (this.spoilerPressed == null && (this.pressedLinkIndex != i3 || this.pressedLink == null || !this.linkPreviewPressed)) {
                                        resetPressedLink();
                                        this.pressedLinkIndex = i3;
                                        LinkSpanDrawable linkSpanDrawable = new LinkSpanDrawable(null, this.resourcesProvider, x - dp, (y - this.linkY) - i4);
                                        this.pressedLink = linkSpanDrawable;
                                        LinkPath obtainNewPath = linkSpanDrawable.obtainNewPath();
                                        this.linkPreviewPressed = true;
                                        this.linksCollector.addLink(this.pressedLink);
                                        startCheckLongPress();
                                        try {
                                            obtainNewPath.setCurrentLayout(staticLayout, 0, f2, this.linkY + i4);
                                            staticLayout.getSelectionPath(0, staticLayout.getText().length(), obtainNewPath);
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                } else if (this.linkPreviewPressed) {
                                    try {
                                        if (this.pressedLinkIndex == 0 && (tLRPC$MessageMedia = this.message.messageOwner.media) != null) {
                                            tLRPC$WebPage = tLRPC$MessageMedia.webpage;
                                        }
                                        if (tLRPC$WebPage != null && (str = tLRPC$WebPage.embed_url) != null && str.length() != 0) {
                                            this.delegate.needOpenWebView(tLRPC$WebPage, this.message);
                                        } else {
                                            this.delegate.onLinkPress(this.links.get(this.pressedLinkIndex).toString(), false);
                                        }
                                    } catch (Exception e2) {
                                        FileLog.e(e2);
                                    }
                                    resetPressedLink();
                                } else if (this.spoilerPressed != null) {
                                    startSpoilerRipples(x, y, i4);
                                } else {
                                    z = false;
                                    z2 = true;
                                }
                                z = true;
                                z2 = true;
                            }
                        }
                        i4 += lineBottom;
                    }
                    i3++;
                }
                if (motionEvent.getAction() == 0) {
                    int dp2 = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                    StaticLayout staticLayout2 = this.descriptionLayout;
                    if (staticLayout2 != null && x >= dp2 && x <= staticLayout2.getWidth() + dp2 && y >= (i2 = this.descriptionY) && y <= i2 + this.descriptionLayout.getHeight()) {
                        Iterator<SpoilerEffect> it2 = this.descriptionLayoutSpoilers.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            SpoilerEffect next2 = it2.next();
                            if (next2.getBounds().contains(x - dp2, y - this.descriptionY)) {
                                this.spoilerPressed = next2;
                                this.spoilerTypePressed = 1;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                    StaticLayout staticLayout3 = this.descriptionLayout2;
                    if (staticLayout3 != null && x >= dp2 && x <= staticLayout3.getWidth() + dp2 && y >= (i = this.description2Y) && y <= i + this.descriptionLayout2.getHeight()) {
                        for (SpoilerEffect spoilerEffect : this.descriptionLayout2Spoilers) {
                            if (spoilerEffect.getBounds().contains(x - dp2, y - this.description2Y)) {
                                this.spoilerPressed = spoilerEffect;
                                this.spoilerTypePressed = 2;
                                z = true;
                                z2 = true;
                                break;
                            }
                        }
                    }
                    if (!z2) {
                        resetPressedLink();
                    }
                } else {
                    if (motionEvent.getAction() == 1 && this.spoilerPressed != null) {
                        startSpoilerRipples(x, y, 0);
                        z = true;
                        z2 = true;
                        break;
                    }
                    if (!z2) {
                    }
                }
                return z || super.onTouchEvent(motionEvent);
            } else if (motionEvent.getAction() == 3) {
                resetPressedLink();
            }
        } else {
            resetPressedLink();
        }
        z = false;
        if (z) {
            return true;
        }
    }

    private void startSpoilerRipples(int i, int i2, int i3) {
        int dp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
        resetPressedLink();
        this.spoilerPressed.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.lambda$startSpoilerRipples$1();
            }
        });
        int i4 = i - dp;
        float sqrt = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        int i5 = this.spoilerTypePressed;
        if (i5 == 0) {
            float f = 0.0f;
            for (int i6 = 0; i6 < this.linkLayout.size(); i6++) {
                StaticLayout staticLayout = this.linkLayout.get(i6);
                f += staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                for (SpoilerEffect spoilerEffect : this.linkSpoilers.get(i6)) {
                    spoilerEffect.startRipple(i4, ((i2 - getYOffsetForType(0)) - i3) + f, sqrt);
                }
            }
        } else if (i5 == 1) {
            for (SpoilerEffect spoilerEffect2 : this.descriptionLayoutSpoilers) {
                spoilerEffect2.startRipple(i4, i2 - getYOffsetForType(1), sqrt);
            }
        } else if (i5 == 2) {
            for (SpoilerEffect spoilerEffect3 : this.descriptionLayout2Spoilers) {
                spoilerEffect3.startRipple(i4, i2 - getYOffsetForType(2), sqrt);
            }
        }
        for (int i7 = 0; i7 <= 2; i7++) {
            if (i7 != this.spoilerTypePressed) {
                if (i7 == 0) {
                    for (int i8 = 0; i8 < this.linkLayout.size(); i8++) {
                        StaticLayout staticLayout2 = this.linkLayout.get(i8);
                        staticLayout2.getLineBottom(staticLayout2.getLineCount() - 1);
                        for (SpoilerEffect spoilerEffect4 : this.linkSpoilers.get(i8)) {
                            spoilerEffect4.startRipple(spoilerEffect4.getBounds().centerX(), spoilerEffect4.getBounds().centerY(), sqrt);
                        }
                    }
                } else if (i7 == 1) {
                    for (SpoilerEffect spoilerEffect5 : this.descriptionLayoutSpoilers) {
                        spoilerEffect5.startRipple(spoilerEffect5.getBounds().centerX(), spoilerEffect5.getBounds().centerY(), sqrt);
                    }
                } else if (i7 == 2) {
                    for (SpoilerEffect spoilerEffect6 : this.descriptionLayout2Spoilers) {
                        spoilerEffect6.startRipple(spoilerEffect6.getBounds().centerX(), spoilerEffect6.getBounds().centerY(), sqrt);
                    }
                }
            }
        }
        this.spoilerTypePressed = -1;
        this.spoilerPressed = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$1() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.lambda$startSpoilerRipples$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSpoilerRipples$0() {
        this.message.isSpoilersRevealed = true;
        this.linkSpoilers.clear();
        this.descriptionLayoutSpoilers.clear();
        this.descriptionLayout2Spoilers.clear();
        invalidate();
    }

    private int getYOffsetForType(int i) {
        if (i != 1) {
            if (i != 2) {
                return this.linkY;
            }
            return this.description2Y;
        }
        return this.descriptionY;
    }

    public String getLink(int i) {
        if (i < 0 || i >= this.links.size()) {
            return null;
        }
        return this.links.get(i).toString();
    }

    protected void resetPressedLink() {
        this.linksCollector.clear(true);
        this.pressedLinkIndex = -1;
        this.pressedLink = null;
        this.linkPreviewPressed = false;
        cancelCheckLongPress();
        invalidate();
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        StaticLayout staticLayout;
        if (this.viewType == 1) {
            this.description2TextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3, this.resourcesProvider));
        }
        if (this.dateLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? 0 : this.dateLayoutX), this.titleY);
            this.dateLayout.draw(canvas);
            canvas.restore();
        }
        if (this.titleLayout != null) {
            canvas.save();
            float dp = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
            if (LocaleController.isRTL) {
                dp += this.dateLayout == null ? 0.0f : staticLayout.getWidth() + AndroidUtilities.dp(4.0f);
            }
            canvas.translate(dp, this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.captionLayout != null) {
            this.captionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.captionY);
            this.captionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.descriptionY);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout, this.descriptionLayout, this.descriptionLayoutSpoilers, canvas, false);
            canvas.restore();
        }
        if (this.descriptionLayout2 != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.description2Y);
            SpoilerEffect.renderWithRipple(this, false, this.descriptionTextPaint.getColor(), -AndroidUtilities.dp(2.0f), this.patchedDescriptionLayout2, this.descriptionLayout2, this.descriptionLayout2Spoilers, canvas, false);
            canvas.restore();
        }
        if (!this.linkLayout.isEmpty()) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, this.resourcesProvider));
            int i = 0;
            for (int i2 = 0; i2 < this.linkLayout.size(); i2++) {
                StaticLayout staticLayout2 = this.linkLayout.get(i2);
                List<SpoilerEffect> list = this.linkSpoilers.get(i2);
                if (staticLayout2.getLineCount() > 0) {
                    canvas.save();
                    canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY + i);
                    this.path.rewind();
                    if (list != null) {
                        for (SpoilerEffect spoilerEffect : list) {
                            Rect bounds = spoilerEffect.getBounds();
                            this.path.addRect(bounds.left, bounds.top, bounds.right, bounds.bottom, Path.Direction.CW);
                        }
                    }
                    canvas.save();
                    canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.clipPath(this.path);
                    this.path.rewind();
                    if (list != null && !list.isEmpty()) {
                        list.get(0).getRipplePath(this.path);
                    }
                    canvas.clipPath(this.path);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                    if (list != null) {
                        for (SpoilerEffect spoilerEffect2 : list) {
                            spoilerEffect2.draw(canvas);
                        }
                    }
                    canvas.restore();
                    i += staticLayout2.getLineBottom(staticLayout2.getLineCount() - 1);
                }
            }
            if (this.linksCollector.draw(canvas)) {
                invalidate();
            }
        }
        if (this.fromInfoLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.fromInfoLayoutY);
            this.fromInfoLayout.draw(canvas);
            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.fromInfoLayout, this.fromInfoLayoutEmojis, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            canvas.restore();
        }
        this.letterDrawable.draw(canvas);
        if (this.drawLinkImageView) {
            this.linkImageView.draw(canvas);
        }
        if (this.needDivider) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas.drawLine(AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        StaticLayout staticLayout = this.titleLayout;
        if (staticLayout != null) {
            sb.append(staticLayout.getText());
        }
        if (this.descriptionLayout != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout.getText());
        }
        if (this.descriptionLayout2 != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout2.getText());
        }
        accessibilityNodeInfo.setText(sb.toString());
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setChecked(true);
            accessibilityNodeInfo.setCheckable(true);
        }
    }
}
