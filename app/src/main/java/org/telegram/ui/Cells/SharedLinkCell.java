package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
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
import androidx.core.net.MailTo;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
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
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes4.dex */
public class SharedLinkCell extends FrameLayout {
    private static final int SPOILER_TYPE_DESCRIPTION = 1;
    private static final int SPOILER_TYPE_DESCRIPTION2 = 2;
    private static final int SPOILER_TYPE_LINK = 0;
    public static final int VIEW_TYPE_DEFAULT = 0;
    public static final int VIEW_TYPE_GLOBAL_SEARCH = 1;
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
    private int fromInfoLayoutY;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList<StaticLayout> linkLayout;
    private boolean linkPreviewPressed;
    private SparseArray<List<SpoilerEffect>> linkSpoilers;
    private int linkY;
    ArrayList<CharSequence> links;
    private MessageObject message;
    private boolean needDivider;
    private AtomicReference<Layout> patchedDescriptionLayout;
    private AtomicReference<Layout> patchedDescriptionLayout2;
    private Path path;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    private int pressedLink;
    private Theme.ResourcesProvider resourcesProvider;
    private SpoilerEffect spoilerPressed;
    private int spoilerTypePressed;
    private Stack<SpoilerEffect> spoilersPool;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint;
    private int titleY;
    private Paint urlPaint;
    private LinkPath urlPath;
    private int viewType;

    /* loaded from: classes4.dex */
    public interface SharedLinkCellDelegate {
        boolean canPerformActions();

        void needOpenWebView(TLRPC.WebPage webPage, MessageObject messageObject);

        void onLinkPress(String str, boolean z);
    }

    static /* synthetic */ int access$104(SharedLinkCell x0) {
        int i = x0.pressCount + 1;
        x0.pressCount = i;
        return i;
    }

    /* loaded from: classes4.dex */
    public final class CheckForTap implements Runnable {
        private CheckForTap() {
            SharedLinkCell.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.pendingCheckForLongPress == null) {
                SharedLinkCell.this.pendingCheckForLongPress = new CheckForLongPress();
            }
            SharedLinkCell.this.pendingCheckForLongPress.currentPressCount = SharedLinkCell.access$104(SharedLinkCell.this);
            SharedLinkCell sharedLinkCell = SharedLinkCell.this;
            sharedLinkCell.postDelayed(sharedLinkCell.pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
        }
    }

    /* loaded from: classes4.dex */
    public class CheckForLongPress implements Runnable {
        public int currentPressCount;

        CheckForLongPress() {
            SharedLinkCell.this = this$0;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
                SharedLinkCell.this.checkingForLongPress = false;
                SharedLinkCell.this.performHapticFeedback(0);
                if (SharedLinkCell.this.pressedLink >= 0) {
                    SharedLinkCell.this.delegate.onLinkPress(SharedLinkCell.this.links.get(SharedLinkCell.this.pressedLink).toString(), true);
                }
                MotionEvent event = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                SharedLinkCell.this.onTouchEvent(event);
                event.recycle();
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

    public SharedLinkCell(Context context) {
        this(context, 0, null);
    }

    public SharedLinkCell(Context context, int viewType) {
        this(context, viewType, null);
    }

    public SharedLinkCell(Context context, int viewType, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
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
        this.viewType = viewType;
        setFocusable(true);
        LinkPath linkPath = new LinkPath();
        this.urlPath = linkPath;
        linkPath.setUseRoundRect(true);
        TextPaint textPaint = new TextPaint(1);
        this.titleTextPaint = textPaint;
        textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.titleTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.descriptionTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        setWillNotDraw(false);
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.linkImageView = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
        this.letterDrawable = new LetterDrawable(resourcesProvider);
        CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setVisibility(4);
        this.checkBox.setColor(null, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 44.0f, 44.0f, LocaleController.isRTL ? 44.0f : 0.0f, 0.0f));
        if (viewType == 1) {
            TextPaint textPaint2 = new TextPaint(1);
            this.description2TextPaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        }
        TextPaint textPaint3 = new TextPaint(1);
        this.captionTextPaint = textPaint3;
        textPaint3.setTextSize(AndroidUtilities.dp(13.0f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01a1 A[Catch: Exception -> 0x0313, TryCatch #9 {Exception -> 0x0313, blocks: (B:51:0x012b, B:53:0x012f, B:56:0x0134, B:59:0x013a, B:61:0x0140, B:70:0x019d, B:72:0x01a1, B:73:0x01b4, B:75:0x01b9, B:77:0x01bf, B:99:0x023b, B:101:0x024b, B:103:0x025b), top: B:293:0x012b }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01b4 A[Catch: Exception -> 0x0313, TryCatch #9 {Exception -> 0x0313, blocks: (B:51:0x012b, B:53:0x012f, B:56:0x0134, B:59:0x013a, B:61:0x0140, B:70:0x019d, B:72:0x01a1, B:73:0x01b4, B:75:0x01b9, B:77:0x01bf, B:99:0x023b, B:101:0x024b, B:103:0x025b), top: B:293:0x012b }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ce A[Catch: Exception -> 0x0310, TryCatch #0 {Exception -> 0x0310, blocks: (B:62:0x0174, B:64:0x0178, B:68:0x018a, B:78:0x01c3, B:80:0x01ce, B:82:0x01d5, B:84:0x01de, B:86:0x01ea, B:87:0x01f1, B:88:0x020d, B:90:0x0211, B:92:0x021f), top: B:275:0x0174 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01ea A[Catch: Exception -> 0x0310, TryCatch #0 {Exception -> 0x0310, blocks: (B:62:0x0174, B:64:0x0178, B:68:0x018a, B:78:0x01c3, B:80:0x01ce, B:82:0x01d5, B:84:0x01de, B:86:0x01ea, B:87:0x01f1, B:88:0x020d, B:90:0x0211, B:92:0x021f), top: B:275:0x0174 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0232  */
    /* JADX WARN: Type inference failed for: r4v52, types: [android.text.SpannableStringBuilder, android.text.Spannable] */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean hasPhoto;
        String webPageLink;
        String title;
        int dateWidth;
        CharSequence description2;
        CharSequence description;
        int i;
        String title2;
        int a;
        Exception e;
        CharSequence linkFinal;
        SpannableStringBuilder st;
        CharSequence link;
        Exception e2;
        CharSequence description3;
        String title3;
        CharSequence lobj;
        CharSequence description4;
        String title4;
        int index;
        int index2;
        String title5;
        int i2 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.descriptionLayout2 = null;
        this.captionLayout = null;
        this.linkLayout.clear();
        this.links.clear();
        int maxWidth = (View.MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        String title6 = null;
        CharSequence description5 = null;
        CharSequence description6 = null;
        int i3 = 1;
        if ((this.message.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && (this.message.messageOwner.media.webpage instanceof TLRPC.TL_webPage)) {
            TLRPC.WebPage webPage = this.message.messageOwner.media.webpage;
            if (this.message.photoThumbs == null && webPage.photo != null) {
                this.message.generateThumbs(true);
            }
            boolean hasPhoto2 = (webPage.photo == null || this.message.photoThumbs == null) ? false : true;
            title6 = webPage.title;
            if (title6 == null) {
                title6 = webPage.site_name;
            }
            description5 = webPage.description;
            webPageLink = webPage.url;
            hasPhoto = hasPhoto2;
        } else {
            webPageLink = null;
            hasPhoto = false;
        }
        MessageObject messageObject = this.message;
        if (messageObject != null && !messageObject.messageOwner.entities.isEmpty()) {
            int a2 = 0;
            CharSequence charSequence = description5;
            String title7 = title6;
            CharSequence link2 = null;
            CharSequence description7 = charSequence;
            while (a2 < this.message.messageOwner.entities.size()) {
                TLRPC.MessageEntity entity = this.message.messageOwner.entities.get(a2);
                if (entity.length > 0 && entity.offset >= 0 && entity.offset < this.message.messageOwner.message.length()) {
                    if (entity.offset + entity.length > this.message.messageOwner.message.length()) {
                        entity.length = this.message.messageOwner.message.length() - entity.offset;
                    }
                    try {
                        if (a2 == 0 && webPageLink != null && (entity.offset != 0 || entity.length != this.message.messageOwner.message.length())) {
                            if (this.message.messageOwner.entities.size() == i3) {
                                if (description7 == null) {
                                    st = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, st);
                                }
                            } else {
                                st = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                MediaDataController.addTextStyleRuns(this.message, st);
                            }
                            link = null;
                            if (!(entity instanceof TLRPC.TL_messageEntityTextUrl) && !(entity instanceof TLRPC.TL_messageEntityUrl)) {
                                if ((entity instanceof TLRPC.TL_messageEntityEmail) && (title7 == null || title7.length() == 0)) {
                                    link = MailTo.MAILTO_SCHEME + this.message.messageOwner.message.substring(entity.offset, entity.offset + entity.length);
                                    title5 = this.message.messageOwner.message.substring(entity.offset, entity.offset + entity.length);
                                    try {
                                        if (entity.offset == 0 && entity.length == this.message.messageOwner.message.length()) {
                                            title7 = title5;
                                        }
                                        ?? valueOf = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                        MediaDataController.addTextStyleRuns(this.message, valueOf);
                                        description7 = valueOf;
                                        title7 = title5;
                                    } catch (Exception e3) {
                                        e2 = e3;
                                        title7 = title5;
                                        FileLog.e(e2);
                                        link2 = st;
                                        a2++;
                                        i2 = 0;
                                        i3 = 1;
                                    }
                                }
                                if (link == null) {
                                    int offset = 0;
                                    try {
                                        if (!AndroidUtilities.charSequenceContains(link, "://") && link.toString().toLowerCase().indexOf("http") != 0 && link.toString().toLowerCase().indexOf("mailto") != 0) {
                                            lobj = "http://" + ((Object) link);
                                            offset = 0 + "http://".length();
                                        } else {
                                            lobj = link;
                                        }
                                        SpannableString sb = SpannableString.valueOf(lobj);
                                        int start = entity.offset;
                                        int end = entity.offset + entity.length;
                                        Iterator<TLRPC.MessageEntity> it = this.message.messageOwner.entities.iterator();
                                        while (it.hasNext()) {
                                            TLRPC.MessageEntity e4 = it.next();
                                            String str = link;
                                            int ss = e4.offset;
                                            Iterator<TLRPC.MessageEntity> it2 = it;
                                            String title8 = title7;
                                            try {
                                                int se = e4.offset + e4.length;
                                                if (!(e4 instanceof TLRPC.TL_messageEntitySpoiler) || start > se || end < ss) {
                                                    description4 = description7;
                                                } else {
                                                    TextStyleSpan.TextStyleRun run = new TextStyleSpan.TextStyleRun();
                                                    description4 = description7;
                                                    try {
                                                        run.flags |= 256;
                                                        sb.setSpan(new TextStyleSpan(run), Math.max(start, ss), Math.min(end, se) + offset, 33);
                                                    } catch (Exception e5) {
                                                        e2 = e5;
                                                        title7 = title8;
                                                        description7 = description4;
                                                        FileLog.e(e2);
                                                        link2 = st;
                                                        a2++;
                                                        i2 = 0;
                                                        i3 = 1;
                                                    }
                                                }
                                                link = str;
                                                it = it2;
                                                title7 = title8;
                                                description7 = description4;
                                            } catch (Exception e6) {
                                                e2 = e6;
                                                title7 = title8;
                                            }
                                        }
                                        title3 = title7;
                                        description3 = description7;
                                        this.links.add(sb);
                                    } catch (Exception e7) {
                                        e2 = e7;
                                    }
                                } else {
                                    title3 = title7;
                                    description3 = description7;
                                }
                                link2 = st;
                                title7 = title3;
                                description7 = description3;
                            }
                            if (!(entity instanceof TLRPC.TL_messageEntityUrl)) {
                                link = this.message.messageOwner.message.substring(entity.offset, entity.offset + entity.length);
                            } else {
                                link = entity.url;
                            }
                            if (title7 != null || title7.length() == 0) {
                                String title9 = link.toString();
                                Uri uri = Uri.parse(title9);
                                title4 = uri.getHost();
                                if (title4 == null) {
                                    title4 = link.toString();
                                }
                                if (title4 != null && (index = title4.lastIndexOf(46)) >= 0) {
                                    String title10 = title4.substring(i2, index);
                                    index2 = title10.lastIndexOf(46);
                                    if (index2 >= 0) {
                                        title10 = title10.substring(index2 + 1);
                                    }
                                    title4 = title10.substring(i2, i3).toUpperCase() + title10.substring(i3);
                                }
                                if (entity.offset == 0 || entity.length != this.message.messageOwner.message.length()) {
                                    SpannableStringBuilder st2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                    MediaDataController.addTextStyleRuns(this.message, st2);
                                    description7 = st2;
                                }
                                title7 = title4;
                            }
                            if (link == null) {
                            }
                            link2 = st;
                            title7 = title3;
                            description7 = description3;
                        }
                        if (!(entity instanceof TLRPC.TL_messageEntityTextUrl)) {
                            if (entity instanceof TLRPC.TL_messageEntityEmail) {
                                link = MailTo.MAILTO_SCHEME + this.message.messageOwner.message.substring(entity.offset, entity.offset + entity.length);
                                title5 = this.message.messageOwner.message.substring(entity.offset, entity.offset + entity.length);
                                if (entity.offset == 0) {
                                    title7 = title5;
                                }
                                ?? valueOf2 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                                MediaDataController.addTextStyleRuns(this.message, valueOf2);
                                description7 = valueOf2;
                                title7 = title5;
                            }
                            if (link == null) {
                            }
                            link2 = st;
                            title7 = title3;
                            description7 = description3;
                        }
                        if (!(entity instanceof TLRPC.TL_messageEntityUrl)) {
                        }
                        if (title7 != null) {
                        }
                        String title92 = link.toString();
                        Uri uri2 = Uri.parse(title92);
                        title4 = uri2.getHost();
                        if (title4 == null) {
                        }
                        if (title4 != null) {
                            String title102 = title4.substring(i2, index);
                            index2 = title102.lastIndexOf(46);
                            if (index2 >= 0) {
                            }
                            title4 = title102.substring(i2, i3).toUpperCase() + title102.substring(i3);
                        }
                        if (entity.offset == 0) {
                        }
                        SpannableStringBuilder st22 = SpannableStringBuilder.valueOf(this.message.messageOwner.message);
                        MediaDataController.addTextStyleRuns(this.message, st22);
                        description7 = st22;
                        title7 = title4;
                        if (link == null) {
                        }
                        link2 = st;
                        title7 = title3;
                        description7 = description3;
                    } catch (Exception e8) {
                        e2 = e8;
                    }
                    st = link2;
                    link = null;
                }
                a2++;
                i2 = 0;
                i3 = 1;
            }
            title = title7;
            description5 = description7;
            description6 = link2;
        } else {
            title = title6;
        }
        if (webPageLink != null && this.links.isEmpty()) {
            this.links.add(webPageLink);
        }
        if (this.viewType != 1) {
            dateWidth = 0;
        } else {
            String str2 = LocaleController.stringForMessageListDate(this.message.messageOwner.date);
            int width = (int) Math.ceil(this.description2TextPaint.measureText(str2));
            this.dateLayout = ChatMessageCell.generateStaticLayout(str2, this.description2TextPaint, width, width, 0, 1);
            this.dateLayoutX = (maxWidth - width) - AndroidUtilities.dp(8.0f);
            dateWidth = width + AndroidUtilities.dp(12.0f);
        }
        if (title != null) {
            CharSequence titleFinal = title;
            try {
                CharSequence titleH = AndroidUtilities.highlightText(titleFinal, this.message.highlightedWords, (Theme.ResourcesProvider) null);
                if (titleH != null) {
                    titleFinal = titleH;
                }
                StaticLayout generateStaticLayout = ChatMessageCell.generateStaticLayout(titleFinal, this.titleTextPaint, (maxWidth - dateWidth) - AndroidUtilities.dp(4.0f), (maxWidth - dateWidth) - AndroidUtilities.dp(4.0f), 0, 3);
                this.titleLayout = generateStaticLayout;
                if (generateStaticLayout.getLineCount() > 0) {
                    int i4 = this.titleY;
                    StaticLayout staticLayout = this.titleLayout;
                    this.descriptionY = i4 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                }
            } catch (Exception e9) {
                FileLog.e(e9);
            }
            this.letterDrawable.setTitle(title);
        }
        this.description2Y = this.descriptionY;
        StaticLayout staticLayout2 = this.titleLayout;
        int desctiptionLines = Math.max(1, 4 - (staticLayout2 != null ? staticLayout2.getLineCount() : 0));
        if (this.viewType != 1) {
            description = description5;
            description2 = description6;
        } else {
            description = null;
            description2 = null;
        }
        if (description != null) {
            try {
                StaticLayout generateStaticLayout2 = ChatMessageCell.generateStaticLayout(description, this.descriptionTextPaint, maxWidth, maxWidth, 0, desctiptionLines);
                this.descriptionLayout = generateStaticLayout2;
                if (generateStaticLayout2.getLineCount() > 0) {
                    int i5 = this.descriptionY;
                    StaticLayout staticLayout3 = this.descriptionLayout;
                    this.description2Y = i5 + staticLayout3.getLineBottom(staticLayout3.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
                this.spoilersPool.addAll(this.descriptionLayoutSpoilers);
                this.descriptionLayoutSpoilers.clear();
                if (!this.message.isSpoilersRevealed) {
                    SpoilerEffect.addSpoilers(this, this.descriptionLayout, this.spoilersPool, this.descriptionLayoutSpoilers);
                }
            } catch (Exception e10) {
                FileLog.e(e10);
            }
        }
        if (description2 != null) {
            try {
                this.descriptionLayout2 = ChatMessageCell.generateStaticLayout(description2, this.descriptionTextPaint, maxWidth, maxWidth, 0, desctiptionLines);
                if (this.descriptionLayout != null) {
                    this.description2Y += AndroidUtilities.dp(10.0f);
                }
                this.spoilersPool.addAll(this.descriptionLayout2Spoilers);
                this.descriptionLayout2Spoilers.clear();
                if (!this.message.isSpoilersRevealed) {
                    SpoilerEffect.addSpoilers(this, this.descriptionLayout2, this.spoilersPool, this.descriptionLayout2Spoilers);
                }
            } catch (Exception e11) {
                FileLog.e(e11);
            }
        }
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null && !TextUtils.isEmpty(messageObject2.messageOwner.message)) {
            CharSequence caption = Emoji.replaceEmoji(this.message.messageOwner.message.replace("\n", " ").replaceAll(" +", " ").trim(), Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            CharSequence sequence = AndroidUtilities.highlightText(caption, this.message.highlightedWords, (Theme.ResourcesProvider) null);
            if (sequence != null) {
                this.captionLayout = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.ellipsizeCenterEnd(sequence, this.message.highlightedWords.get(0), maxWidth, this.captionTextPaint, TsExtractor.TS_STREAM_TYPE_HDMV_DTS), this.captionTextPaint, maxWidth, TextUtils.TruncateAt.END), this.captionTextPaint, maxWidth + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }
        StaticLayout staticLayout4 = this.captionLayout;
        if (staticLayout4 != null) {
            int i6 = this.descriptionY;
            this.captionY = i6;
            int lineBottom = i6 + staticLayout4.getLineBottom(staticLayout4.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            this.descriptionY = lineBottom;
            this.description2Y = lineBottom;
        }
        if (!this.links.isEmpty()) {
            for (int i7 = 0; i7 < this.linkSpoilers.size(); i7++) {
                this.spoilersPool.addAll(this.linkSpoilers.get(i7));
            }
            this.linkSpoilers.clear();
            int a3 = 0;
            while (a3 < this.links.size()) {
                try {
                    CharSequence link3 = this.links.get(a3);
                    linkFinal = TextUtils.ellipsize(AndroidUtilities.replaceNewLines(SpannableStringBuilder.valueOf(link3)), this.descriptionTextPaint, Math.min((int) Math.ceil(this.descriptionTextPaint.measureText(link3, 0, link3.length())), maxWidth), TextUtils.TruncateAt.MIDDLE);
                    title2 = title;
                    a = a3;
                } catch (Exception e12) {
                    e = e12;
                    title2 = title;
                    a = a3;
                }
                try {
                    StaticLayout layout = new StaticLayout(linkFinal, this.descriptionTextPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.linkY = this.description2Y;
                    StaticLayout staticLayout5 = this.descriptionLayout2;
                    if (staticLayout5 != null && staticLayout5.getLineCount() != 0) {
                        int i8 = this.linkY;
                        StaticLayout staticLayout6 = this.descriptionLayout2;
                        this.linkY = i8 + staticLayout6.getLineBottom(staticLayout6.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    }
                    if (!this.message.isSpoilersRevealed) {
                        List<SpoilerEffect> l = new ArrayList<>();
                        if (linkFinal instanceof Spannable) {
                            SpoilerEffect.addSpoilers(this, layout, (Spannable) linkFinal, this.spoilersPool, l);
                        }
                        this.linkSpoilers.put(a, l);
                    }
                    this.linkLayout.add(layout);
                } catch (Exception e13) {
                    e = e13;
                    FileLog.e(e);
                    a3 = a + 1;
                    title = title2;
                }
                a3 = a + 1;
                title = title2;
            }
        }
        int maxPhotoWidth = AndroidUtilities.dp(52.0f);
        int x = LocaleController.isRTL ? (View.MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(10.0f)) - maxPhotoWidth : AndroidUtilities.dp(10.0f);
        this.letterDrawable.setBounds(x, AndroidUtilities.dp(11.0f), x + maxPhotoWidth, AndroidUtilities.dp(63.0f));
        if (!hasPhoto) {
            i = 1;
        } else {
            TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, maxPhotoWidth, true);
            TLRPC.PhotoSize currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80);
            if (currentPhotoObjectThumb == currentPhotoObject) {
                currentPhotoObjectThumb = null;
            }
            currentPhotoObject.size = -1;
            if (currentPhotoObjectThumb != null) {
                currentPhotoObjectThumb.size = -1;
            }
            this.linkImageView.setImageCoords(x, AndroidUtilities.dp(11.0f), maxPhotoWidth, maxPhotoWidth);
            FileLoader.getAttachFileName(currentPhotoObject);
            String filter = String.format(Locale.US, "%d_%d", Integer.valueOf(maxPhotoWidth), Integer.valueOf(maxPhotoWidth));
            String thumbFilter = String.format(Locale.US, "%d_%d_b", Integer.valueOf(maxPhotoWidth), Integer.valueOf(maxPhotoWidth));
            this.linkImageView.setImage(ImageLocation.getForObject(currentPhotoObject, this.message.photoThumbsObject), filter, ImageLocation.getForObject(currentPhotoObjectThumb, this.message.photoThumbsObject), thumbFilter, 0L, null, this.message, 0);
            i = 1;
            this.drawLinkImageView = true;
        }
        if (this.viewType == i) {
            this.fromInfoLayout = ChatMessageCell.generateStaticLayout(FilteredSearchView.createFromInfoString(this.message), this.description2TextPaint, maxWidth, maxWidth, 0, desctiptionLines);
        }
        int height = 0;
        StaticLayout staticLayout7 = this.titleLayout;
        if (staticLayout7 != null && staticLayout7.getLineCount() != 0) {
            StaticLayout staticLayout8 = this.titleLayout;
            height = 0 + staticLayout8.getLineBottom(staticLayout8.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
        }
        StaticLayout staticLayout9 = this.captionLayout;
        if (staticLayout9 != null && staticLayout9.getLineCount() != 0) {
            StaticLayout staticLayout10 = this.captionLayout;
            height += staticLayout10.getLineBottom(staticLayout10.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        StaticLayout staticLayout11 = this.descriptionLayout;
        if (staticLayout11 != null && staticLayout11.getLineCount() != 0) {
            StaticLayout staticLayout12 = this.descriptionLayout;
            height += staticLayout12.getLineBottom(staticLayout12.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        StaticLayout staticLayout13 = this.descriptionLayout2;
        if (staticLayout13 != null && staticLayout13.getLineCount() != 0) {
            StaticLayout staticLayout14 = this.descriptionLayout2;
            height += staticLayout14.getLineBottom(staticLayout14.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            if (this.descriptionLayout != null) {
                height += AndroidUtilities.dp(10.0f);
            }
        }
        int linksHeight = 0;
        for (int a4 = 0; a4 < this.linkLayout.size(); a4++) {
            StaticLayout layout2 = this.linkLayout.get(a4);
            if (layout2.getLineCount() > 0) {
                linksHeight += layout2.getLineBottom(layout2.getLineCount() - 1);
            }
        }
        int height2 = height + linksHeight;
        if (this.fromInfoLayout != null) {
            this.fromInfoLayoutY = this.linkY + linksHeight + AndroidUtilities.dp(5.0f);
            StaticLayout staticLayout15 = this.fromInfoLayout;
            height2 += staticLayout15.getLineBottom(staticLayout15.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
        }
        this.checkBox.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), C.BUFFER_FLAG_ENCRYPTED));
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(76.0f), AndroidUtilities.dp(17.0f) + height2) + (this.needDivider ? 1 : 0));
    }

    public void setLink(MessageObject messageObject, boolean divider) {
        this.needDivider = divider;
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
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        SharedLinkCellDelegate sharedLinkCellDelegate;
        int i;
        int i2;
        boolean result = false;
        boolean z = true;
        z = true;
        z = true;
        int i3 = 1;
        if (this.message != null && !this.linkLayout.isEmpty() && (sharedLinkCellDelegate = this.delegate) != null && sharedLinkCellDelegate.canPerformActions()) {
            if (event.getAction() == 0 || ((this.linkPreviewPressed || this.spoilerPressed != null) && event.getAction() == 1)) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                boolean ok = false;
                int a = 0;
                int offset = 0;
                while (true) {
                    if (a >= this.linkLayout.size()) {
                        break;
                    }
                    StaticLayout layout = this.linkLayout.get(a);
                    if (layout.getLineCount() > 0) {
                        int height = layout.getLineBottom(layout.getLineCount() - i3);
                        int linkPosX = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                        if (x >= linkPosX + layout.getLineLeft(0) && x <= linkPosX + layout.getLineWidth(0)) {
                            int i4 = this.linkY;
                            if (y >= i4 + offset && y <= i4 + offset + height) {
                                ok = true;
                                TLRPC.WebPage webPage = null;
                                if (event.getAction() == 0) {
                                    resetPressedLink();
                                    this.spoilerPressed = null;
                                    if (this.linkSpoilers.get(a, null) != null) {
                                        Iterator<SpoilerEffect> it = this.linkSpoilers.get(a).iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            SpoilerEffect eff = it.next();
                                            if (eff.getBounds().contains(x - linkPosX, (y - this.linkY) - offset)) {
                                                this.spoilerPressed = eff;
                                                this.spoilerTypePressed = 0;
                                                break;
                                            }
                                        }
                                    }
                                    if (this.spoilerPressed != null) {
                                        result = true;
                                    } else {
                                        this.pressedLink = a;
                                        this.linkPreviewPressed = true;
                                        startCheckLongPress();
                                        try {
                                            this.urlPath.setCurrentLayout(layout, 0, 0.0f);
                                            layout.getSelectionPath(0, layout.getText().length(), this.urlPath);
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                        result = true;
                                    }
                                } else if (this.linkPreviewPressed) {
                                    try {
                                        if (this.pressedLink == 0 && this.message.messageOwner.media != null) {
                                            webPage = this.message.messageOwner.media.webpage;
                                        }
                                        TLRPC.WebPage webPage2 = webPage;
                                        if (webPage2 == null || webPage2.embed_url == null || webPage2.embed_url.length() == 0) {
                                            this.delegate.onLinkPress(this.links.get(this.pressedLink).toString(), false);
                                        } else {
                                            this.delegate.needOpenWebView(webPage2, this.message);
                                        }
                                    } catch (Exception e2) {
                                        FileLog.e(e2);
                                    }
                                    resetPressedLink();
                                    result = true;
                                } else if (this.spoilerPressed != null) {
                                    startSpoilerRipples(x, y, offset);
                                    result = true;
                                }
                            }
                        }
                        offset += height;
                    }
                    a++;
                    i3 = 1;
                }
                if (event.getAction() != 0) {
                    z = true;
                    z = true;
                    z = true;
                    if (event.getAction() == 1 && this.spoilerPressed != null) {
                        startSpoilerRipples(x, y, 0);
                        ok = true;
                        result = true;
                    }
                } else {
                    int offX = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
                    StaticLayout staticLayout = this.descriptionLayout;
                    if (staticLayout != null && x >= offX && x <= staticLayout.getWidth() + offX && y >= (i2 = this.descriptionY) && y <= i2 + this.descriptionLayout.getHeight()) {
                        Iterator<SpoilerEffect> it2 = this.descriptionLayoutSpoilers.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            SpoilerEffect eff2 = it2.next();
                            if (eff2.getBounds().contains(x - offX, y - this.descriptionY)) {
                                this.spoilerPressed = eff2;
                                this.spoilerTypePressed = 1;
                                ok = true;
                                result = true;
                                break;
                            }
                        }
                    }
                    StaticLayout staticLayout2 = this.descriptionLayout2;
                    if (staticLayout2 != null && x >= offX && x <= staticLayout2.getWidth() + offX && y >= (i = this.description2Y) && y <= i + this.descriptionLayout2.getHeight()) {
                        Iterator<SpoilerEffect> it3 = this.descriptionLayout2Spoilers.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                break;
                            }
                            SpoilerEffect eff3 = it3.next();
                            if (eff3.getBounds().contains(x - offX, y - this.description2Y)) {
                                this.spoilerPressed = eff3;
                                this.spoilerTypePressed = 2;
                                result = true;
                                ok = true;
                                break;
                            }
                        }
                    }
                    z = true;
                }
                if (!ok) {
                    resetPressedLink();
                }
            } else if (event.getAction() == 3) {
                resetPressedLink();
            }
        } else {
            resetPressedLink();
        }
        if (result || super.onTouchEvent(event)) {
            return z;
        }
        return false;
    }

    private void startSpoilerRipples(int x, int y, int offset) {
        int linkPosX = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
        resetPressedLink();
        SpoilerEffect eff = this.spoilerPressed;
        eff.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.m1669xd74f5b04();
            }
        });
        int nx = x - linkPosX;
        float rad = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        float offY = 0.0f;
        switch (this.spoilerTypePressed) {
            case 0:
                for (int i = 0; i < this.linkLayout.size(); i++) {
                    Layout lt = this.linkLayout.get(i);
                    offY += lt.getLineBottom(lt.getLineCount() - 1);
                    for (SpoilerEffect e : this.linkSpoilers.get(i)) {
                        e.startRipple(nx, ((y - getYOffsetForType(0)) - offset) + offY, rad);
                    }
                }
                break;
            case 1:
                for (SpoilerEffect sp : this.descriptionLayoutSpoilers) {
                    sp.startRipple(nx, y - getYOffsetForType(1), rad);
                }
                break;
            case 2:
                for (SpoilerEffect sp2 : this.descriptionLayout2Spoilers) {
                    sp2.startRipple(nx, y - getYOffsetForType(2), rad);
                }
                break;
        }
        for (int i2 = 0; i2 <= 2; i2++) {
            if (i2 != this.spoilerTypePressed) {
                switch (i2) {
                    case 0:
                        for (int j = 0; j < this.linkLayout.size(); j++) {
                            Layout lt2 = this.linkLayout.get(j);
                            offY += lt2.getLineBottom(lt2.getLineCount() - 1);
                            for (SpoilerEffect e2 : this.linkSpoilers.get(j)) {
                                e2.startRipple(e2.getBounds().centerX(), e2.getBounds().centerY(), rad);
                            }
                        }
                        continue;
                    case 1:
                        for (SpoilerEffect sp3 : this.descriptionLayoutSpoilers) {
                            sp3.startRipple(sp3.getBounds().centerX(), sp3.getBounds().centerY(), rad);
                        }
                        continue;
                    case 2:
                        for (SpoilerEffect sp4 : this.descriptionLayout2Spoilers) {
                            sp4.startRipple(sp4.getBounds().centerX(), sp4.getBounds().centerY(), rad);
                        }
                        continue;
                }
            }
        }
        this.spoilerTypePressed = -1;
        this.spoilerPressed = null;
    }

    /* renamed from: lambda$startSpoilerRipples$1$org-telegram-ui-Cells-SharedLinkCell */
    public /* synthetic */ void m1669xd74f5b04() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.SharedLinkCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SharedLinkCell.this.m1668x75fcbe65();
            }
        });
    }

    /* renamed from: lambda$startSpoilerRipples$0$org-telegram-ui-Cells-SharedLinkCell */
    public /* synthetic */ void m1668x75fcbe65() {
        this.message.isSpoilersRevealed = true;
        this.linkSpoilers.clear();
        this.descriptionLayoutSpoilers.clear();
        this.descriptionLayout2Spoilers.clear();
        invalidate();
    }

    private int getYOffsetForType(int type) {
        switch (type) {
            case 1:
                return this.descriptionY;
            case 2:
                return this.description2Y;
            default:
                return this.linkY;
        }
    }

    public String getLink(int num) {
        if (num < 0 || num >= this.links.size()) {
            return null;
        }
        return this.links.get(num).toString();
    }

    protected void resetPressedLink() {
        this.pressedLink = -1;
        this.linkPreviewPressed = false;
        cancelCheckLongPress();
        invalidate();
    }

    public void setChecked(boolean checked, boolean animated) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(checked, animated);
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
            float x = AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline);
            if (LocaleController.isRTL) {
                x += this.dateLayout == null ? 0.0f : staticLayout.getWidth() + AndroidUtilities.dp(4.0f);
            }
            canvas.translate(x, this.titleY);
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
            int offset = 0;
            for (int a = 0; a < this.linkLayout.size(); a++) {
                StaticLayout layout = this.linkLayout.get(a);
                List<SpoilerEffect> spoilers = this.linkSpoilers.get(a);
                if (layout.getLineCount() > 0) {
                    canvas.save();
                    canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.linkY + offset);
                    this.path.rewind();
                    if (spoilers != null) {
                        for (SpoilerEffect eff : spoilers) {
                            Rect b = eff.getBounds();
                            this.path.addRect(b.left, b.top, b.right, b.bottom, Path.Direction.CW);
                        }
                    }
                    if (this.urlPaint == null) {
                        Paint paint = new Paint(1);
                        this.urlPaint = paint;
                        paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(4.0f)));
                    }
                    this.urlPaint.setColor(Theme.getColor(Theme.key_chat_linkSelectBackground, this.resourcesProvider));
                    canvas.save();
                    canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                    if (this.pressedLink == a) {
                        canvas.drawPath(this.urlPath, this.urlPaint);
                    }
                    layout.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.clipPath(this.path);
                    this.path.rewind();
                    if (spoilers != null && !spoilers.isEmpty()) {
                        spoilers.get(0).getRipplePath(this.path);
                    }
                    canvas.clipPath(this.path);
                    if (this.pressedLink == a) {
                        canvas.drawPath(this.urlPath, this.urlPaint);
                    }
                    layout.draw(canvas);
                    canvas.restore();
                    if (spoilers != null) {
                        for (SpoilerEffect eff2 : spoilers) {
                            eff2.draw(canvas);
                        }
                    }
                    canvas.restore();
                    offset += layout.getLineBottom(layout.getLineCount() - 1);
                }
            }
        }
        if (this.fromInfoLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : AndroidUtilities.leftBaseline), this.fromInfoLayoutY);
            this.fromInfoLayout.draw(canvas);
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
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
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
        info.setText(sb.toString());
        if (this.checkBox.isChecked()) {
            info.setChecked(true);
            info.setCheckable(true);
        }
    }
}
