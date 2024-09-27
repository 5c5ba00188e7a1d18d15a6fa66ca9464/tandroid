package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
/* loaded from: classes4.dex */
public class DialogMeUrlCell extends BaseCell {
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private int avatarTop;
    private int currentAccount;
    private boolean drawNameLock;
    private boolean drawVerified;
    private boolean isSelected;
    private StaticLayout messageLayout;
    private int messageLeft;
    private int messageTop;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private TLRPC.RecentMeUrl recentMeUrl;
    public boolean useSeparator;

    public DialogMeUrlCell(Context context) {
        super(context);
        this.avatarImage = new ImageReceiver(this);
        this.avatarDrawable = new AvatarDrawable();
        this.messageTop = AndroidUtilities.dp(40.0f);
        this.avatarTop = AndroidUtilities.dp(10.0f);
        this.currentAccount = UserConfig.selectedAccount;
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
    }

    /* JADX WARN: Can't wrap try/catch for region: R(24:1|(4:3|(1:5)(1:104)|6|7)(2:105|(5:107|(1:109)(1:121)|110|(3:112|(3:114|(1:116)(1:118)|117)|119)|120)(23:122|(4:124|(1:126)(1:129)|127|128)(2:130|(8:132|(1:134)(1:145)|135|136|(1:138)(1:144)|139|(1:141)(1:143)|142)(2:146|(4:148|(1:150)(1:153)|151|152)(1:154)))|9|(1:11)|12|(1:14)(1:103)|15|16|(1:18)|19|(2:21|(1:23))|24|25|26|27|(3:29|(1:31)(1:94)|32)(3:95|(1:97)(1:99)|98)|33|34|35|36|(4:38|(4:42|(1:44)|45|(2:47|(1:49)))|50|(1:63)(2:56|(1:58)(1:62)))(4:66|(4:70|(2:72|(1:74))|75|(1:77))|78|(1:88)(2:84|(1:86)(1:87)))|59|60))|8|9|(0)|12|(0)(0)|15|16|(0)|19|(0)|24|25|26|27|(0)(0)|33|34|35|36|(0)(0)|59|60) */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x029b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x029c, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0322, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0323, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        String str;
        int dp;
        int dp2;
        int measuredWidth;
        int measuredWidth2;
        double d;
        TLRPC.Chat chat;
        int dp3;
        TextPaint textPaint = Theme.dialogs_namePaint[0];
        TextPaint textPaint2 = Theme.dialogs_messagePaint[0];
        this.drawNameLock = false;
        this.drawVerified = false;
        TLRPC.RecentMeUrl recentMeUrl = this.recentMeUrl;
        float f = 14.0f;
        if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChat) {
            TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.recentMeUrl.chat_id));
            this.drawVerified = chat2.verified;
            if (LocaleController.isRTL) {
                this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                dp3 = AndroidUtilities.dp(14.0f);
            } else {
                this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                dp3 = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
            }
            this.nameLeft = dp3;
            str = chat2.title;
            this.avatarDrawable.setInfo(this.currentAccount, chat2);
            chat = chat2;
        } else if (!(recentMeUrl instanceof TLRPC.TL_recentMeUrlUser)) {
            if (recentMeUrl instanceof TLRPC.TL_recentMeUrlStickerSet) {
                this.nameLeft = !LocaleController.isRTL ? AndroidUtilities.dp(AndroidUtilities.leftBaseline) : AndroidUtilities.dp(14.0f);
                str = this.recentMeUrl.set.set.title;
                this.avatarDrawable.setInfo(5L, str, null);
                this.avatarImage.setImage(ImageLocation.getForDocument(this.recentMeUrl.set.cover), null, this.avatarDrawable, null, this.recentMeUrl, 0);
            } else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChatInvite) {
                this.nameLeft = !LocaleController.isRTL ? AndroidUtilities.dp(AndroidUtilities.leftBaseline) : AndroidUtilities.dp(14.0f);
                TLRPC.ChatInvite chatInvite = this.recentMeUrl.chat_invite;
                TLRPC.Chat chat3 = chatInvite.chat;
                if (chat3 != null) {
                    this.avatarDrawable.setInfo(this.currentAccount, chat3);
                    TLRPC.RecentMeUrl recentMeUrl2 = this.recentMeUrl;
                    TLRPC.Chat chat4 = recentMeUrl2.chat_invite.chat;
                    String str2 = chat4.title;
                    this.drawVerified = chat4.verified;
                    this.avatarImage.setForUserOrChat(chat4, this.avatarDrawable, recentMeUrl2);
                    str = str2;
                } else {
                    String str3 = chatInvite.title;
                    this.avatarDrawable.setInfo(5L, str3, null);
                    this.avatarImage.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(this.recentMeUrl.chat_invite.photo.sizes, 50), this.recentMeUrl.chat_invite.photo), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
                    str = str3;
                }
                if (LocaleController.isRTL) {
                    this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                    dp = AndroidUtilities.dp(14.0f);
                } else {
                    this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                    dp = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
                }
                this.nameLeft = dp;
            } else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlUnknown) {
                this.nameLeft = !LocaleController.isRTL ? AndroidUtilities.dp(AndroidUtilities.leftBaseline) : AndroidUtilities.dp(14.0f);
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.recentMeUrl, 0);
                str = "Url";
            } else {
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, recentMeUrl, 0);
                str = "";
            }
            String str4 = MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + this.recentMeUrl.url;
            if (TextUtils.isEmpty(str)) {
                str = LocaleController.getString(R.string.HiddenName);
            }
            if (LocaleController.isRTL) {
                measuredWidth = getMeasuredWidth() - this.nameLeft;
            } else {
                measuredWidth = getMeasuredWidth() - this.nameLeft;
                f = AndroidUtilities.leftBaseline;
            }
            int dp4 = measuredWidth - AndroidUtilities.dp(f);
            if (this.drawNameLock) {
                dp4 -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
            }
            if (this.drawVerified) {
                int dp5 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                dp4 -= dp5;
                if (LocaleController.isRTL) {
                    this.nameLeft += dp5;
                }
            }
            int max = Math.max(AndroidUtilities.dp(12.0f), dp4);
            this.nameLayout = new StaticLayout(TextUtils.ellipsize(str.replace('\n', ' '), textPaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), textPaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline + 16);
            if (LocaleController.isRTL) {
                this.messageLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                measuredWidth2 = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 13.0f : 9.0f);
            } else {
                this.messageLeft = AndroidUtilities.dp(16.0f);
                measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.isTablet() ? 65.0f : 61.0f);
            }
            this.avatarImage.setImageCoords(measuredWidth2, this.avatarTop, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
            int max2 = Math.max(AndroidUtilities.dp(12.0f), measuredWidth3);
            this.messageLayout = new StaticLayout(TextUtils.ellipsize(str4, textPaint2, max2 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), textPaint2, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (LocaleController.isRTL) {
                StaticLayout staticLayout = this.nameLayout;
                if (staticLayout != null && staticLayout.getLineCount() > 0) {
                    float lineRight = this.nameLayout.getLineRight(0);
                    if (lineRight == max) {
                        double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                        double d2 = max;
                        if (ceil < d2) {
                            double d3 = this.nameLeft;
                            Double.isNaN(d2);
                            Double.isNaN(d3);
                            this.nameLeft = (int) (d3 - (d2 - ceil));
                        }
                    }
                    if (this.drawVerified) {
                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                    }
                }
                StaticLayout staticLayout2 = this.messageLayout;
                if (staticLayout2 == null || staticLayout2.getLineCount() <= 0 || this.messageLayout.getLineRight(0) != max2) {
                    return;
                }
                double ceil2 = Math.ceil(this.messageLayout.getLineWidth(0));
                double d4 = max2;
                if (ceil2 >= d4) {
                    return;
                }
                double d5 = this.messageLeft;
                Double.isNaN(d4);
                Double.isNaN(d5);
                d = d5 - (d4 - ceil2);
            } else {
                StaticLayout staticLayout3 = this.nameLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil3 = Math.ceil(this.nameLayout.getLineWidth(0));
                    if (this.drawVerified) {
                        double d6 = this.nameLeft;
                        double d7 = max;
                        Double.isNaN(d7);
                        Double.isNaN(d6);
                        double d8 = d6 + (d7 - ceil3);
                        double dp6 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp6);
                        double d9 = d8 - dp6;
                        double intrinsicWidth = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth);
                        this.nameMuteLeft = (int) (d9 - intrinsicWidth);
                    }
                    if (lineLeft == 0.0f) {
                        double d10 = max;
                        if (ceil3 < d10) {
                            double d11 = this.nameLeft;
                            Double.isNaN(d10);
                            Double.isNaN(d11);
                            this.nameLeft = (int) (d11 + (d10 - ceil3));
                        }
                    }
                }
                StaticLayout staticLayout4 = this.messageLayout;
                if (staticLayout4 == null || staticLayout4.getLineCount() <= 0 || this.messageLayout.getLineLeft(0) != 0.0f) {
                    return;
                }
                double ceil4 = Math.ceil(this.messageLayout.getLineWidth(0));
                double d12 = max2;
                if (ceil4 >= d12) {
                    return;
                }
                double d13 = this.messageLeft;
                Double.isNaN(d12);
                Double.isNaN(d13);
                d = d13 + (d12 - ceil4);
            }
            this.messageLeft = (int) d;
        } else {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.recentMeUrl.user_id));
            this.nameLeft = !LocaleController.isRTL ? AndroidUtilities.dp(AndroidUtilities.leftBaseline) : AndroidUtilities.dp(14.0f);
            if (user != null) {
                if (user.bot) {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (LocaleController.isRTL) {
                        this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                        dp2 = AndroidUtilities.dp(14.0f);
                    } else {
                        this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                        dp2 = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
                    }
                    this.nameLeft = dp2;
                }
                this.drawVerified = user.verified;
            }
            str = UserObject.getUserName(user);
            this.avatarDrawable.setInfo(this.currentAccount, user);
            chat = user;
        }
        this.avatarImage.setForUserOrChat(chat, this.avatarDrawable, this.recentMeUrl);
        String str42 = MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + this.recentMeUrl.url;
        if (TextUtils.isEmpty(str)) {
        }
        if (LocaleController.isRTL) {
        }
        int dp42 = measuredWidth - AndroidUtilities.dp(f);
        if (this.drawNameLock) {
        }
        if (this.drawVerified) {
        }
        int max3 = Math.max(AndroidUtilities.dp(12.0f), dp42);
        this.nameLayout = new StaticLayout(TextUtils.ellipsize(str.replace('\n', ' '), textPaint, max3 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), textPaint, max3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int measuredWidth32 = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline + 16);
        if (LocaleController.isRTL) {
        }
        this.avatarImage.setImageCoords(measuredWidth2, this.avatarTop, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
        int max22 = Math.max(AndroidUtilities.dp(12.0f), measuredWidth32);
        this.messageLayout = new StaticLayout(TextUtils.ellipsize(str42, textPaint2, max22 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), textPaint2, max22, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        if (LocaleController.isRTL) {
        }
        this.messageLeft = (int) d;
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float dp;
        float measuredHeight;
        float measuredWidth;
        float measuredHeight2;
        Paint paint;
        if (this.isSelected) {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
        }
        if (this.drawNameLock) {
            BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_lockDrawable.draw(canvas);
        }
        if (this.nameLayout != null) {
            canvas.save();
            canvas.translate(this.nameLeft, AndroidUtilities.dp(13.0f));
            this.nameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.messageLayout != null) {
            canvas.save();
            canvas.translate(this.messageLeft, this.messageTop);
            try {
                this.messageLayout.draw(canvas);
            } catch (Exception e) {
                FileLog.e(e);
            }
            canvas.restore();
        }
        if (this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        }
        if (this.useSeparator) {
            if (LocaleController.isRTL) {
                measuredHeight = getMeasuredHeight() - 1;
                measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                measuredHeight2 = getMeasuredHeight() - 1;
                paint = Theme.dividerPaint;
                dp = 0.0f;
            } else {
                dp = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                measuredHeight = getMeasuredHeight() - 1;
                measuredWidth = getMeasuredWidth();
                measuredHeight2 = getMeasuredHeight() - 1;
                paint = Theme.dividerPaint;
            }
            canvas.drawLine(dp, measuredHeight, measuredWidth, measuredHeight2, paint);
        }
        this.avatarImage.draw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (z) {
            buildLayout();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(72.0f) + (this.useSeparator ? 1 : 0));
    }

    public void setDialogSelected(boolean z) {
        if (this.isSelected != z) {
            invalidate();
        }
        this.isSelected = z;
    }

    public void setRecentMeUrl(TLRPC.RecentMeUrl recentMeUrl) {
        this.recentMeUrl = recentMeUrl;
        requestLayout();
    }
}
