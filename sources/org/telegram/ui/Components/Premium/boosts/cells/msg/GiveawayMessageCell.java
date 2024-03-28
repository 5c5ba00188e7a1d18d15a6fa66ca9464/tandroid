package org.telegram.ui.Components.Premium.boosts.cells.msg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.StateSet;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.StaticLayoutEx;
/* loaded from: classes3.dex */
public class GiveawayMessageCell {
    private static final Map<Integer, String> monthsToEmoticon;
    private int additionPrizeHeight;
    private StaticLayout additionPrizeLayout;
    private AvatarDrawable[] avatarDrawables;
    private ImageReceiver[] avatarImageReceivers;
    private boolean[] avatarVisible;
    private int bottomHeight;
    private StaticLayout bottomLayout;
    private Paint chatBgPaint;
    private RectF chatRect;
    private TextPaint chatTextPaint;
    private float[] chatTitleWidths;
    private CharSequence[] chatTitles;
    private TLRPC$Chat[] chats;
    private Rect[] clickRect;
    private Paint clipRectPaint;
    private Rect containerRect;
    private RectF countRect;
    private Paint counterBgPaint;
    private String counterStr;
    private Rect counterTextBounds;
    private TextPaint counterTextPaint;
    private int countriesHeight;
    private StaticLayout countriesLayout;
    private TextPaint countriesTextPaint;
    private int diffTextWidth;
    private ImageReceiver giftReceiver;
    private Paint lineDividerPaint;
    private MessageObject messageObject;
    private boolean[] needNewRow;
    private final ChatMessageCell parentView;
    private int[] pressedState;
    private Paint saveLayerPaint;
    private int selectorColor;
    private Drawable selectorDrawable;
    private String textDivider;
    private TextPaint textDividerPaint;
    private float textDividerWidth;
    private TextPaint textPaint;
    private int titleHeight;
    private StaticLayout titleLayout;
    private int topHeight;
    private StaticLayout topLayout;
    private int measuredHeight = 0;
    private int measuredWidth = 0;
    private int pressedPos = -1;
    private boolean isButtonPressed = false;
    private boolean isContainerPressed = false;

    static {
        HashMap hashMap = new HashMap();
        monthsToEmoticon = hashMap;
        hashMap.put(1, "1⃣");
        hashMap.put(3, "2⃣");
        hashMap.put(6, "3⃣");
        hashMap.put(12, "4⃣");
        hashMap.put(24, "5⃣");
    }

    public GiveawayMessageCell(ChatMessageCell chatMessageCell) {
        this.parentView = chatMessageCell;
    }

    private void init() {
        if (this.counterTextPaint != null) {
            return;
        }
        this.counterTextPaint = new TextPaint(1);
        this.chatTextPaint = new TextPaint(1);
        this.textPaint = new TextPaint(1);
        this.textDividerPaint = new TextPaint(1);
        this.lineDividerPaint = new Paint(1);
        this.countriesTextPaint = new TextPaint(1);
        this.counterBgPaint = new Paint(1);
        this.chatBgPaint = new Paint(1);
        this.saveLayerPaint = new Paint();
        this.clipRectPaint = new Paint();
        this.countRect = new RectF();
        this.chatRect = new RectF();
        this.counterTextBounds = new Rect();
        this.containerRect = new Rect();
        this.pressedState = new int[]{16842910, 16842919};
        this.chatTitles = new CharSequence[10];
        this.chats = new TLRPC$Chat[10];
        this.chatTitleWidths = new float[10];
        this.needNewRow = new boolean[10];
        this.clickRect = new Rect[10];
        ImageReceiver imageReceiver = new ImageReceiver(this.parentView);
        this.giftReceiver = imageReceiver;
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        this.clipRectPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.counterTextPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.counterTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.counterTextPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.counterTextPaint.setTextAlign(Paint.Align.CENTER);
        this.chatTextPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.chatTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.countriesTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.textDividerPaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.textDividerPaint.setTextAlign(Paint.Align.CENTER);
    }

    public boolean checkMotionEvent(MotionEvent motionEvent) {
        MessageObject messageObject = this.messageObject;
        if (messageObject != null && messageObject.isGiveaway()) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                int i = 0;
                while (true) {
                    Rect[] rectArr = this.clickRect;
                    if (i < rectArr.length) {
                        if (rectArr[i].contains(x, y)) {
                            this.pressedPos = i;
                            if (Build.VERSION.SDK_INT >= 21) {
                                this.selectorDrawable.setHotspot(x, y);
                            }
                            this.isButtonPressed = true;
                            setButtonPressed(true);
                            return true;
                        }
                        i++;
                    } else if (this.containerRect.contains(x, y)) {
                        this.isContainerPressed = true;
                        return true;
                    }
                }
            } else if (motionEvent.getAction() == 1) {
                if (this.isButtonPressed) {
                    if (this.parentView.getDelegate() != null) {
                        this.parentView.getDelegate().didPressGiveawayChatButton(this.parentView, this.pressedPos);
                    }
                    this.parentView.playSoundEffect(0);
                    setButtonPressed(false);
                    this.isButtonPressed = false;
                }
                if (this.isContainerPressed) {
                    this.isContainerPressed = false;
                    BoostDialogs.showBulletinAbout(this.messageObject);
                }
            } else if (motionEvent.getAction() != 2 && motionEvent.getAction() == 3) {
                if (this.isButtonPressed) {
                    setButtonPressed(false);
                }
                this.isButtonPressed = false;
                this.isContainerPressed = false;
            }
        }
        return false;
    }

    public void setButtonPressed(boolean z) {
        Drawable drawable;
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || !messageObject.isGiveaway() || (drawable = this.selectorDrawable) == null) {
            return;
        }
        if (z) {
            drawable.setCallback(new Drawable.Callback() { // from class: org.telegram.ui.Components.Premium.boosts.cells.msg.GiveawayMessageCell.1
                @Override // android.graphics.drawable.Drawable.Callback
                public void invalidateDrawable(Drawable drawable2) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void scheduleDrawable(Drawable drawable2, Runnable runnable, long j) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
                    GiveawayMessageCell.this.parentView.invalidate();
                }
            });
            this.selectorDrawable.setState(this.pressedState);
            this.parentView.invalidate();
            return;
        }
        drawable.setState(StateSet.NOTHING);
        this.parentView.invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x041d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x043b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageContent(MessageObject messageObject, int i, int i2) {
        int dp;
        String str;
        ArrayList arrayList;
        Iterator<Long> it;
        int i3;
        String str2;
        this.messageObject = null;
        this.titleLayout = null;
        this.additionPrizeLayout = null;
        this.topLayout = null;
        this.bottomLayout = null;
        this.countriesLayout = null;
        this.measuredHeight = 0;
        this.measuredWidth = 0;
        this.additionPrizeHeight = 0;
        this.textDividerWidth = 0.0f;
        if (messageObject.isGiveaway()) {
            this.messageObject = messageObject;
            init();
            createImages();
            setGiftImage(messageObject);
            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) messageObject.messageOwner.media;
            checkArraysLimits(tLRPC$TL_messageMediaGiveaway.channels.size());
            int dp2 = AndroidUtilities.dp(148.0f);
            if (AndroidUtilities.isTablet()) {
                dp = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(80.0f);
            } else {
                dp = i - AndroidUtilities.dp(80.0f);
            }
            MessagesController messagesController = MessagesController.getInstance(UserConfig.selectedAccount);
            boolean isForwarded = messageObject.isForwarded();
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(messagesController.getChat(Long.valueOf(-MessageObject.getPeerId(isForwarded ? tLRPC$Message.fwd_from.from_id : tLRPC$Message.peer_id))));
            SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("BoostingGiveawayPrizes", R.string.BoostingGiveawayPrizes));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(replaceTags);
            spannableStringBuilder.setSpan(new RelativeSizeSpan(1.05f), 0, replaceTags.length(), 33);
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("BoostingGiveawayMsgInfoPlural1", tLRPC$TL_messageMediaGiveaway.quantity)));
            spannableStringBuilder2.append((CharSequence) "\n");
            spannableStringBuilder2.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatPluralString("BoostingGiveawayMsgInfoPlural2", tLRPC$TL_messageMediaGiveaway.quantity, LocaleController.formatPluralString("BoldMonths", tLRPC$TL_messageMediaGiveaway.months, new Object[0]))));
            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
            spannableStringBuilder3.append((CharSequence) spannableStringBuilder2);
            spannableStringBuilder3.append((CharSequence) "\n\n");
            spannableStringBuilder3.setSpan(new RelativeSizeSpan(0.4f), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
            SpannableStringBuilder replaceTags2 = AndroidUtilities.replaceTags(LocaleController.getString("BoostingGiveawayMsgParticipants", R.string.BoostingGiveawayMsgParticipants));
            spannableStringBuilder3.append((CharSequence) replaceTags2);
            spannableStringBuilder3.setSpan(new RelativeSizeSpan(1.05f), spannableStringBuilder2.length() + 2, spannableStringBuilder2.length() + 2 + replaceTags2.length(), 33);
            spannableStringBuilder3.append((CharSequence) "\n");
            if (tLRPC$TL_messageMediaGiveaway.only_new_subscribers) {
                spannableStringBuilder3.append((CharSequence) LocaleController.formatPluralString(isChannelAndNotMegaGroup ? "BoostingGiveawayMsgNewSubsPlural" : "BoostingGiveawayMsgNewSubsGroupPlural", tLRPC$TL_messageMediaGiveaway.channels.size(), new Object[0]));
            } else {
                spannableStringBuilder3.append((CharSequence) LocaleController.formatPluralString(isChannelAndNotMegaGroup ? "BoostingGiveawayMsgAllSubsPlural" : "BoostingGiveawayMsgAllSubsGroupPlural", tLRPC$TL_messageMediaGiveaway.channels.size(), new Object[0]));
            }
            SpannableStringBuilder replaceTags3 = AndroidUtilities.replaceTags(LocaleController.getString("BoostingWinnersDate", R.string.BoostingWinnersDate));
            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(replaceTags3);
            spannableStringBuilder4.setSpan(new RelativeSizeSpan(1.05f), 0, replaceTags3.length(), 33);
            Date date = new Date(tLRPC$TL_messageMediaGiveaway.until_date * 1000);
            String format = LocaleController.getInstance().formatterGiveawayCard.format(date);
            String format2 = LocaleController.getInstance().formatterDay.format(date);
            spannableStringBuilder4.append((CharSequence) "\n");
            spannableStringBuilder4.append((CharSequence) LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, format, format2));
            int i4 = dp;
            int i5 = dp;
            this.titleLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder, this.textPaint, i4, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(2.0f), false, TextUtils.TruncateAt.END, i5, 10);
            this.topLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder3, this.textPaint, i4, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(2.0f), false, TextUtils.TruncateAt.END, i5, 10);
            this.bottomLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder4, this.textPaint, i4, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(3.0f), false, TextUtils.TruncateAt.END, i5, 10);
            int i6 = 0;
            for (int i7 = 0; i7 < this.titleLayout.getLineCount(); i7++) {
                i6 = (int) Math.max(i6, Math.ceil(this.titleLayout.getLineWidth(i7)));
            }
            for (int i8 = 0; i8 < this.topLayout.getLineCount(); i8++) {
                i6 = (int) Math.max(i6, Math.ceil(this.topLayout.getLineWidth(i8)));
            }
            for (int i9 = 0; i9 < this.bottomLayout.getLineCount(); i9++) {
                i6 = (int) Math.max(i6, Math.ceil(this.bottomLayout.getLineWidth(i9)));
            }
            if (i6 < AndroidUtilities.dp(180.0f)) {
                i6 = AndroidUtilities.dp(180.0f);
            }
            String str3 = tLRPC$TL_messageMediaGiveaway.prize_description;
            if (str3 != null && !str3.isEmpty()) {
                StaticLayout createStaticLayout = StaticLayoutEx.createStaticLayout(Emoji.replaceEmoji(AndroidUtilities.replaceTags(LocaleController.formatPluralString("BoostingGiveawayMsgPrizes", tLRPC$TL_messageMediaGiveaway.quantity, tLRPC$TL_messageMediaGiveaway.prize_description)), this.countriesTextPaint.getFontMetricsInt(), false), this.textPaint, i6, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(2.0f), false, TextUtils.TruncateAt.END, i6, 20);
                this.additionPrizeLayout = createStaticLayout;
                this.additionPrizeHeight = createStaticLayout.getLineBottom(createStaticLayout.getLineCount() - 1) + AndroidUtilities.dp(22.0f);
                String string = LocaleController.getString("BoostingGiveawayMsgWithDivider", R.string.BoostingGiveawayMsgWithDivider);
                this.textDivider = string;
                this.textDividerWidth = this.textDividerPaint.measureText(string, 0, string.length());
            }
            if (tLRPC$TL_messageMediaGiveaway.countries_iso2.size() > 0) {
                ArrayList arrayList2 = new ArrayList();
                Iterator<String> it2 = tLRPC$TL_messageMediaGiveaway.countries_iso2.iterator();
                while (it2.hasNext()) {
                    String next = it2.next();
                    String displayCountry = new Locale("", next).getDisplayCountry(Locale.getDefault());
                    String languageFlag = LocaleController.getLanguageFlag(next);
                    SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                    if (languageFlag != null) {
                        spannableStringBuilder5.append((CharSequence) languageFlag).append((CharSequence) " ");
                    }
                    spannableStringBuilder5.append((CharSequence) displayCountry);
                    arrayList2.add(spannableStringBuilder5);
                }
                if (!arrayList2.isEmpty()) {
                    str = "";
                    this.countriesLayout = StaticLayoutEx.createStaticLayout(Emoji.replaceEmoji(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingGiveAwayFromCountries", R.string.BoostingGiveAwayFromCountries, TextUtils.join(", ", arrayList2))), this.countriesTextPaint.getFontMetricsInt(), false), this.countriesTextPaint, i6, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i6, 10);
                    int max = Math.max(i2, Math.min(i6 + AndroidUtilities.dp(38.0f), dp));
                    this.diffTextWidth = max - dp;
                    float f = max;
                    float f2 = dp2;
                    float f3 = f2 / 2.0f;
                    this.giftReceiver.setImageCoords((f / 2.0f) - f3, AndroidUtilities.dp(42.0f) - f3, f2, f2);
                    StaticLayout staticLayout = this.titleLayout;
                    int lineBottom = staticLayout.getLineBottom(staticLayout.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    this.titleHeight = lineBottom;
                    int i10 = lineBottom + this.additionPrizeHeight;
                    StaticLayout staticLayout2 = this.topLayout;
                    this.topHeight = i10 + staticLayout2.getLineBottom(staticLayout2.getLineCount() - 1);
                    StaticLayout staticLayout3 = this.bottomLayout;
                    this.bottomHeight = staticLayout3.getLineBottom(staticLayout3.getLineCount() - 1);
                    StaticLayout staticLayout4 = this.countriesLayout;
                    int lineBottom2 = staticLayout4 == null ? staticLayout4.getLineBottom(staticLayout4.getLineCount() - 1) + AndroidUtilities.dp(12.0f) : 0;
                    this.countriesHeight = lineBottom2;
                    int i11 = this.measuredHeight + this.topHeight;
                    this.measuredHeight = i11;
                    int i12 = i11 + lineBottom2;
                    this.measuredHeight = i12;
                    int i13 = i12 + this.bottomHeight;
                    this.measuredHeight = i13;
                    this.measuredHeight = i13 + AndroidUtilities.dp(128.0f);
                    this.measuredWidth = max;
                    String str4 = "x" + tLRPC$TL_messageMediaGiveaway.quantity;
                    this.counterStr = str4;
                    this.counterTextPaint.getTextBounds(str4, 0, str4.length(), this.counterTextBounds);
                    Arrays.fill(this.avatarVisible, false);
                    this.measuredHeight += AndroidUtilities.dp(30.0f);
                    arrayList = new ArrayList(tLRPC$TL_messageMediaGiveaway.channels.size());
                    it = tLRPC$TL_messageMediaGiveaway.channels.iterator();
                    while (it.hasNext()) {
                        Long next2 = it.next();
                        if (MessagesController.getInstance(UserConfig.selectedAccount).getChat(next2) != null) {
                            arrayList.add(next2);
                        }
                    }
                    i3 = 0;
                    float f4 = 0.0f;
                    while (i3 < arrayList.size()) {
                        long longValue = ((Long) arrayList.get(i3)).longValue();
                        TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(longValue));
                        if (chat != null) {
                            this.avatarVisible[i3] = true;
                            this.chats[i3] = chat;
                            this.chatTitles[i3] = TextUtils.ellipsize(Emoji.replaceEmoji(chat.title, this.chatTextPaint.getFontMetricsInt(), false), this.chatTextPaint, 0.8f * f, TextUtils.TruncateAt.END);
                            float[] fArr = this.chatTitleWidths;
                            TextPaint textPaint = this.chatTextPaint;
                            CharSequence[] charSequenceArr = this.chatTitles;
                            fArr[i3] = textPaint.measureText(charSequenceArr[i3], 0, charSequenceArr[i3].length());
                            float dp3 = this.chatTitleWidths[i3] + AndroidUtilities.dp(40.0f);
                            f4 += dp3;
                            if (i3 > 0) {
                                boolean[] zArr = this.needNewRow;
                                zArr[i3] = f4 > 0.9f * f;
                                if (zArr[i3]) {
                                    this.measuredHeight += AndroidUtilities.dp(30.0f);
                                    f4 = dp3;
                                }
                            } else {
                                this.needNewRow[i3] = false;
                            }
                            this.avatarDrawables[i3].setInfo(chat);
                            this.avatarImageReceivers[i3].setForUserOrChat(chat, this.avatarDrawables[i3]);
                            this.avatarImageReceivers[i3].setImageCoords(0.0f, 0.0f, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                            str2 = str;
                        } else {
                            this.chats[i3] = null;
                            this.avatarVisible[i3] = false;
                            str2 = str;
                            this.chatTitles[i3] = str2;
                            this.needNewRow[i3] = false;
                            this.chatTitleWidths[i3] = AndroidUtilities.dp(20.0f);
                            this.avatarDrawables[i3].setInfo(longValue, str2, str2);
                        }
                        i3++;
                        str = str2;
                    }
                }
            }
            str = "";
            int max2 = Math.max(i2, Math.min(i6 + AndroidUtilities.dp(38.0f), dp));
            this.diffTextWidth = max2 - dp;
            float f5 = max2;
            float f22 = dp2;
            float f32 = f22 / 2.0f;
            this.giftReceiver.setImageCoords((f5 / 2.0f) - f32, AndroidUtilities.dp(42.0f) - f32, f22, f22);
            StaticLayout staticLayout5 = this.titleLayout;
            int lineBottom3 = staticLayout5.getLineBottom(staticLayout5.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
            this.titleHeight = lineBottom3;
            int i102 = lineBottom3 + this.additionPrizeHeight;
            StaticLayout staticLayout22 = this.topLayout;
            this.topHeight = i102 + staticLayout22.getLineBottom(staticLayout22.getLineCount() - 1);
            StaticLayout staticLayout32 = this.bottomLayout;
            this.bottomHeight = staticLayout32.getLineBottom(staticLayout32.getLineCount() - 1);
            StaticLayout staticLayout42 = this.countriesLayout;
            if (staticLayout42 == null) {
            }
            this.countriesHeight = lineBottom2;
            int i112 = this.measuredHeight + this.topHeight;
            this.measuredHeight = i112;
            int i122 = i112 + lineBottom2;
            this.measuredHeight = i122;
            int i132 = i122 + this.bottomHeight;
            this.measuredHeight = i132;
            this.measuredHeight = i132 + AndroidUtilities.dp(128.0f);
            this.measuredWidth = max2;
            String str42 = "x" + tLRPC$TL_messageMediaGiveaway.quantity;
            this.counterStr = str42;
            this.counterTextPaint.getTextBounds(str42, 0, str42.length(), this.counterTextBounds);
            Arrays.fill(this.avatarVisible, false);
            this.measuredHeight += AndroidUtilities.dp(30.0f);
            arrayList = new ArrayList(tLRPC$TL_messageMediaGiveaway.channels.size());
            it = tLRPC$TL_messageMediaGiveaway.channels.iterator();
            while (it.hasNext()) {
            }
            i3 = 0;
            float f42 = 0.0f;
            while (i3 < arrayList.size()) {
            }
        }
    }

    private int getChatColor(TLRPC$Chat tLRPC$Chat, Theme.ResourcesProvider resourcesProvider) {
        if (this.messageObject.isOutOwner()) {
            return Theme.getColor(Theme.key_chat_outPreviewInstantText, resourcesProvider);
        }
        int colorId = ChatObject.getColorId(tLRPC$Chat);
        if (colorId < 7) {
            return Theme.getColor(Theme.keys_avatar_nameInMessage[colorId], resourcesProvider);
        }
        MessagesController.PeerColors peerColors = MessagesController.getInstance(UserConfig.selectedAccount).peerColors;
        MessagesController.PeerColor color = peerColors == null ? null : peerColors.getColor(colorId);
        if (color != null) {
            return color.getColor(0, resourcesProvider);
        }
        return Theme.getColor(Theme.keys_avatar_nameInMessage[0], resourcesProvider);
    }

    public void draw(Canvas canvas, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        float f;
        boolean[] zArr;
        int i3;
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || !messageObject.isGiveaway()) {
            return;
        }
        if (this.selectorDrawable == null) {
            int color = Theme.getColor(Theme.key_listSelector);
            this.selectorColor = color;
            this.selectorDrawable = Theme.createRadSelectorDrawable(color, 12, 12);
        }
        this.textPaint.setColor(Theme.chat_msgTextPaint.getColor());
        this.textDividerPaint.setColor(Theme.multAlpha(Theme.chat_msgTextPaint.getColor(), 0.45f));
        this.lineDividerPaint.setColor(Theme.multAlpha(Theme.chat_msgTextPaint.getColor(), 0.15f));
        this.countriesTextPaint.setColor(Theme.chat_msgTextPaint.getColor());
        if (this.messageObject.isOutOwner()) {
            TextPaint textPaint = this.chatTextPaint;
            int i4 = Theme.key_chat_outPreviewInstantText;
            textPaint.setColor(Theme.getColor(i4, resourcesProvider));
            this.counterBgPaint.setColor(Theme.getColor(i4, resourcesProvider));
            this.chatBgPaint.setColor(Theme.getColor(Theme.key_chat_outReplyLine, resourcesProvider));
        } else {
            TextPaint textPaint2 = this.chatTextPaint;
            int i5 = Theme.key_chat_inPreviewInstantText;
            textPaint2.setColor(Theme.getColor(i5, resourcesProvider));
            this.counterBgPaint.setColor(Theme.getColor(i5, resourcesProvider));
            this.chatBgPaint.setColor(Theme.getColor(Theme.key_chat_inReplyLine, resourcesProvider));
        }
        canvas.save();
        int dp = i2 - AndroidUtilities.dp(4.0f);
        canvas.translate(dp, i);
        this.containerRect.set(dp, i, getMeasuredWidth() + dp, getMeasuredHeight() + i);
        canvas.saveLayer(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.saveLayerPaint, 31);
        this.giftReceiver.draw(canvas);
        float f2 = 2.0f;
        float measuredWidth = getMeasuredWidth() / 2.0f;
        float dp2 = AndroidUtilities.dp(106.0f);
        int width = this.counterTextBounds.width() + AndroidUtilities.dp(12.0f);
        int height = this.counterTextBounds.height() + AndroidUtilities.dp(10.0f);
        this.countRect.set(measuredWidth - ((AndroidUtilities.dp(2.0f) + width) / 2.0f), dp2 - ((AndroidUtilities.dp(2.0f) + height) / 2.0f), ((width + AndroidUtilities.dp(2.0f)) / 2.0f) + measuredWidth, ((height + AndroidUtilities.dp(2.0f)) / 2.0f) + dp2);
        canvas.drawRoundRect(this.countRect, AndroidUtilities.dp(11.0f), AndroidUtilities.dp(11.0f), this.clipRectPaint);
        float f3 = width / 2.0f;
        float f4 = height / 2.0f;
        this.countRect.set(measuredWidth - f3, dp2 - f4, f3 + measuredWidth, dp2 + f4);
        canvas.drawRoundRect(this.countRect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.counterBgPaint);
        canvas.drawText(this.counterStr, this.countRect.centerX(), this.countRect.centerY() + AndroidUtilities.dp(4.0f), this.counterTextPaint);
        canvas.restore();
        canvas.translate(0.0f, AndroidUtilities.dp(128.0f));
        int dp3 = i + AndroidUtilities.dp(128.0f);
        canvas.save();
        canvas.translate(this.diffTextWidth / 2.0f, 0.0f);
        this.titleLayout.draw(canvas);
        canvas.translate(0.0f, this.titleHeight);
        float f5 = 16.0f;
        if (this.additionPrizeLayout != null) {
            canvas.restore();
            canvas.save();
            float dp4 = (this.titleHeight + this.additionPrizeHeight) - AndroidUtilities.dp(6.0f);
            float f6 = this.measuredWidth / 2.0f;
            canvas.drawText(this.textDivider, f6, dp4, this.textDividerPaint);
            canvas.drawLine(AndroidUtilities.dp(17.0f), dp4 - AndroidUtilities.dp(4.0f), (f6 - (this.textDividerWidth / 2.0f)) - AndroidUtilities.dp(6.0f), dp4 - AndroidUtilities.dp(4.0f), this.lineDividerPaint);
            canvas.drawLine(f6 + (this.textDividerWidth / 2.0f) + AndroidUtilities.dp(6.0f), dp4 - AndroidUtilities.dp(4.0f), this.measuredWidth - AndroidUtilities.dp(16.0f), dp4 - AndroidUtilities.dp(4.0f), this.lineDividerPaint);
            canvas.translate((this.measuredWidth - this.additionPrizeLayout.getWidth()) / 2.0f, this.titleHeight);
            this.additionPrizeLayout.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(this.diffTextWidth / 2.0f, this.additionPrizeHeight + this.titleHeight);
        }
        this.topLayout.draw(canvas);
        canvas.restore();
        canvas.translate(0.0f, this.topHeight + AndroidUtilities.dp(6.0f));
        int i6 = 0;
        int dp5 = dp3 + this.topHeight + AndroidUtilities.dp(6.0f);
        int i7 = 0;
        while (true) {
            boolean[] zArr2 = this.avatarVisible;
            if (i6 >= zArr2.length) {
                break;
            }
            if (zArr2[i6]) {
                canvas.save();
                int i8 = i6;
                float f7 = 0.0f;
                do {
                    f7 += this.chatTitleWidths[i8] + AndroidUtilities.dp(40.0f);
                    i8++;
                    zArr = this.avatarVisible;
                    if (i8 >= zArr.length || this.needNewRow[i8]) {
                        break;
                    }
                } while (zArr[i8]);
                float f8 = measuredWidth - (f7 / f2);
                canvas.translate(f8, 0.0f);
                int i9 = i6;
                int i10 = ((int) f8) + dp;
                while (true) {
                    int chatColor = getChatColor(this.chats[i9], resourcesProvider);
                    int i11 = this.pressedPos;
                    i3 = (i11 < 0 || i11 != i9) ? i7 : chatColor;
                    this.chatTextPaint.setColor(chatColor);
                    this.chatBgPaint.setColor(chatColor);
                    this.chatBgPaint.setAlpha(25);
                    this.avatarImageReceivers[i9].draw(canvas);
                    CharSequence[] charSequenceArr = this.chatTitles;
                    int i12 = i10;
                    int i13 = i9;
                    canvas.drawText(charSequenceArr[i9], 0, charSequenceArr[i9].length(), AndroidUtilities.dp(30.0f), AndroidUtilities.dp(f5), this.chatTextPaint);
                    this.chatRect.set(0.0f, 0.0f, this.chatTitleWidths[i13] + AndroidUtilities.dp(40.0f), AndroidUtilities.dp(24.0f));
                    canvas.drawRoundRect(this.chatRect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.chatBgPaint);
                    float f9 = i12;
                    this.clickRect[i13].set(i12, dp5, (int) (this.chatRect.width() + f9), AndroidUtilities.dp(24.0f) + dp5);
                    canvas.translate(this.chatRect.width() + AndroidUtilities.dp(6.0f), 0.0f);
                    i10 = (int) (f9 + this.chatRect.width() + AndroidUtilities.dp(6.0f));
                    i9 = i13 + 1;
                    boolean[] zArr3 = this.avatarVisible;
                    if (i9 >= zArr3.length || this.needNewRow[i9] || !zArr3[i9]) {
                        break;
                    }
                    i7 = i3;
                    f5 = 16.0f;
                }
                canvas.restore();
                canvas.translate(0.0f, AndroidUtilities.dp(30.0f));
                dp5 += AndroidUtilities.dp(30.0f);
                i6 = i9;
                i7 = i3;
            } else {
                i6++;
            }
            f5 = 16.0f;
            f2 = 2.0f;
        }
        if (this.countriesLayout != null) {
            canvas.save();
            canvas.translate((this.measuredWidth - this.countriesLayout.getWidth()) / 2.0f, AndroidUtilities.dp(4.0f));
            this.countriesLayout.draw(canvas);
            canvas.restore();
            f = 0.0f;
            canvas.translate(0.0f, this.countriesHeight);
        } else {
            f = 0.0f;
        }
        canvas.translate(f, AndroidUtilities.dp(6.0f));
        canvas.save();
        canvas.translate(this.diffTextWidth / 2.0f, f);
        this.bottomLayout.draw(canvas);
        canvas.restore();
        canvas.restore();
        if (this.pressedPos >= 0) {
            int multAlpha = Theme.multAlpha(i7, Theme.isCurrentThemeDark() ? 0.12f : 0.1f);
            if (this.selectorColor != multAlpha) {
                Drawable drawable = this.selectorDrawable;
                this.selectorColor = multAlpha;
                Theme.setSelectorDrawableColor(drawable, multAlpha, true);
            }
            this.selectorDrawable.setBounds(this.clickRect[this.pressedPos]);
            this.selectorDrawable.draw(canvas);
        }
    }

    public void onDetachedFromWindow() {
        ImageReceiver imageReceiver = this.giftReceiver;
        if (imageReceiver != null) {
            imageReceiver.onDetachedFromWindow();
        }
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr != null) {
            for (ImageReceiver imageReceiver2 : imageReceiverArr) {
                imageReceiver2.onDetachedFromWindow();
            }
        }
    }

    public void onAttachedToWindow() {
        ImageReceiver imageReceiver = this.giftReceiver;
        if (imageReceiver != null) {
            imageReceiver.onAttachedToWindow();
        }
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr != null) {
            for (ImageReceiver imageReceiver2 : imageReceiverArr) {
                imageReceiver2.onAttachedToWindow();
            }
        }
    }

    public int getMeasuredHeight() {
        return this.measuredHeight;
    }

    public int getMeasuredWidth() {
        return this.measuredWidth;
    }

    private void createImages() {
        if (this.avatarImageReceivers != null) {
            return;
        }
        this.avatarImageReceivers = new ImageReceiver[10];
        this.avatarDrawables = new AvatarDrawable[10];
        this.avatarVisible = new boolean[10];
        int i = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
            if (i >= imageReceiverArr.length) {
                return;
            }
            imageReceiverArr[i] = new ImageReceiver(this.parentView);
            this.avatarImageReceivers[i].setAllowLoadingOnAttachedOnly(true);
            this.avatarImageReceivers[i].setRoundRadius(AndroidUtilities.dp(12.0f));
            this.avatarDrawables[i] = new AvatarDrawable();
            this.avatarDrawables[i].setTextSize(AndroidUtilities.dp(18.0f));
            this.clickRect[i] = new Rect();
            i++;
        }
    }

    private void checkArraysLimits(int i) {
        ImageReceiver[] imageReceiverArr = this.avatarImageReceivers;
        if (imageReceiverArr.length < i) {
            int length = imageReceiverArr.length;
            this.avatarImageReceivers = (ImageReceiver[]) Arrays.copyOf(imageReceiverArr, i);
            this.avatarDrawables = (AvatarDrawable[]) Arrays.copyOf(this.avatarDrawables, i);
            this.avatarVisible = Arrays.copyOf(this.avatarVisible, i);
            this.chatTitles = (CharSequence[]) Arrays.copyOf(this.chatTitles, i);
            this.chatTitleWidths = Arrays.copyOf(this.chatTitleWidths, i);
            this.needNewRow = Arrays.copyOf(this.needNewRow, i);
            this.clickRect = (Rect[]) Arrays.copyOf(this.clickRect, i);
            this.chats = (TLRPC$Chat[]) Arrays.copyOf(this.chats, i);
            for (int i2 = length - 1; i2 < i; i2++) {
                this.avatarImageReceivers[i2] = new ImageReceiver(this.parentView);
                this.avatarImageReceivers[i2].setAllowLoadingOnAttachedOnly(true);
                this.avatarImageReceivers[i2].setRoundRadius(AndroidUtilities.dp(12.0f));
                this.avatarDrawables[i2] = new AvatarDrawable();
                this.avatarDrawables[i2].setTextSize(AndroidUtilities.dp(18.0f));
                this.clickRect[i2] = new Rect();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x008e, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setGiftImage(MessageObject messageObject) {
        TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) messageObject.messageOwner.media;
        String str = UserConfig.getInstance(UserConfig.selectedAccount).premiumGiftsStickerPack;
        if (str == null) {
            MediaDataController.getInstance(UserConfig.selectedAccount).checkPremiumGiftStickers();
            return;
        }
        TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSetByName(str);
        if (stickerSetByName == null) {
            stickerSetByName = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSetByEmojiOrName(str);
        }
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSetByName;
        TLRPC$Document tLRPC$Document = null;
        if (tLRPC$TL_messages_stickerSet != null) {
            String str2 = monthsToEmoticon.get(Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
            Iterator<TLRPC$TL_stickerPack> it = tLRPC$TL_messages_stickerSet.packs.iterator();
            while (it.hasNext()) {
                TLRPC$TL_stickerPack next = it.next();
                if (Objects.equals(next.emoticon, str2)) {
                    Iterator<Long> it2 = next.documents.iterator();
                    while (it2.hasNext()) {
                        long longValue = it2.next().longValue();
                        Iterator<TLRPC$Document> it3 = tLRPC$TL_messages_stickerSet.documents.iterator();
                        while (true) {
                            if (it3.hasNext()) {
                                TLRPC$Document next2 = it3.next();
                                if (next2.id == longValue) {
                                    tLRPC$Document = next2;
                                    continue;
                                    break;
                                }
                            }
                        }
                        if (tLRPC$Document != null) {
                            break;
                        }
                    }
                    continue;
                }
                if (tLRPC$Document != null) {
                    break;
                }
            }
            if (tLRPC$Document == null && !tLRPC$TL_messages_stickerSet.documents.isEmpty()) {
                tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(0);
            }
        }
        if (tLRPC$Document != null) {
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document.thumbs, Theme.key_emptyListPlaceholder, 0.2f);
            if (svgThumb != null) {
                svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
            }
            this.giftReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), "160_160_firstframe", svgThumb, "tgs", tLRPC$TL_messages_stickerSet, 1);
            return;
        }
        MediaDataController.getInstance(UserConfig.selectedAccount).loadStickersByEmojiOrName(str, false, tLRPC$TL_messages_stickerSet == null);
    }
}
