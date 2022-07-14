package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
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
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
/* loaded from: classes4.dex */
public class DialogMeUrlCell extends BaseCell {
    private boolean drawNameLock;
    private boolean drawVerified;
    private boolean isSelected;
    private StaticLayout messageLayout;
    private int messageLeft;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private TLRPC.RecentMeUrl recentMeUrl;
    public boolean useSeparator;
    private ImageReceiver avatarImage = new ImageReceiver(this);
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private int messageTop = AndroidUtilities.dp(40.0f);
    private int avatarTop = AndroidUtilities.dp(10.0f);
    private int currentAccount = UserConfig.selectedAccount;

    public DialogMeUrlCell(Context context) {
        super(context);
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
    }

    public void setRecentMeUrl(TLRPC.RecentMeUrl url) {
        this.recentMeUrl = url;
        requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(72.0f) + (this.useSeparator ? 1 : 0));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            buildLayout();
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(23:2|(3:4|(1:6)(1:7)|8)(2:9|(4:11|(1:13)(1:14)|(3:16|(2:18|(1:20)(1:21))|22)|23)(2:24|(3:26|(1:28)(1:29)|30)(2:31|(6:33|(1:35)(1:36)|37|(1:39)(1:40)|41|(1:43)(1:44))(2:45|(3:47|(1:49)(1:50)|51)(1:52)))))|53|(1:55)(1:56)|57|(1:59)(1:60)|61|(1:63)|64|(2:66|(1:68))|69|(3:150|70|71)|(2:152|72)|77|(3:79|(1:81)(1:82)|83)(3:84|(1:86)(1:87)|88)|89|146|90|91|148|92|97|(4:99|(1:112)(4:103|(1:105)(1:106)|107|(1:111))|113|(2:123|157)(2:117|(2:119|(2:121|155)(1:154))(2:122|156)))(4:124|(4:128|(2:130|(1:132))|133|(1:135))|136|(1:160)(2:142|(2:144|145)(1:161)))) */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x036a, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x036c, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x036d, code lost:
        r22 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x036f, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0418  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x02f2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0376  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        String nameString;
        int nameWidth;
        int avatarLeft;
        int nameWidth2;
        Exception e;
        CharSequence nameStringFinal;
        String nameString2 = "";
        TextPaint currentNamePaint = Theme.dialogs_namePaint[0];
        TextPaint currentMessagePaint = Theme.dialogs_messagePaint[0];
        this.drawNameLock = false;
        this.drawVerified = false;
        TLRPC.RecentMeUrl recentMeUrl = this.recentMeUrl;
        if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChat) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.recentMeUrl.chat_id));
            this.drawVerified = chat.verified;
            if (!LocaleController.isRTL) {
                this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
            } else {
                this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            nameString2 = chat.title;
            this.avatarDrawable.setInfo(chat);
            this.avatarImage.setForUserOrChat(chat, this.avatarDrawable, this.recentMeUrl);
        } else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlUser) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.recentMeUrl.user_id));
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
            } else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            if (user != null) {
                if (user.bot) {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                        this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
                    } else {
                        this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                        this.nameLeft = AndroidUtilities.dp(14.0f);
                    }
                }
                this.drawVerified = user.verified;
            }
            nameString2 = UserObject.getUserName(user);
            this.avatarDrawable.setInfo(user);
            this.avatarImage.setForUserOrChat(user, this.avatarDrawable, this.recentMeUrl);
        } else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlStickerSet) {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
            } else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            nameString2 = this.recentMeUrl.set.set.title;
            this.avatarDrawable.setInfo(5L, this.recentMeUrl.set.set.title, null);
            this.avatarImage.setImage(ImageLocation.getForDocument(this.recentMeUrl.set.cover), null, this.avatarDrawable, null, this.recentMeUrl, 0);
        } else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChatInvite) {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
            } else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            if (this.recentMeUrl.chat_invite.chat != null) {
                this.avatarDrawable.setInfo(this.recentMeUrl.chat_invite.chat);
                nameString2 = this.recentMeUrl.chat_invite.chat.title;
                this.drawVerified = this.recentMeUrl.chat_invite.chat.verified;
                this.avatarImage.setForUserOrChat(this.recentMeUrl.chat_invite.chat, this.avatarDrawable, this.recentMeUrl);
            } else {
                nameString2 = this.recentMeUrl.chat_invite.title;
                this.avatarDrawable.setInfo(5L, this.recentMeUrl.chat_invite.title, null);
                TLRPC.PhotoSize size = FileLoader.getClosestPhotoSizeWithSize(this.recentMeUrl.chat_invite.photo.sizes, 50);
                this.avatarImage.setImage(ImageLocation.getForPhoto(size, this.recentMeUrl.chat_invite.photo), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
            }
            if (!LocaleController.isRTL) {
                this.nameLockLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline + 4);
            } else {
                this.nameLockLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
        } else if (!(recentMeUrl instanceof TLRPC.TL_recentMeUrlUnknown)) {
            this.avatarImage.setImage(null, null, this.avatarDrawable, null, recentMeUrl, 0);
        } else {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
            } else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            nameString2 = "Url";
            this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.recentMeUrl, 0);
        }
        CharSequence messageString = MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + this.recentMeUrl.url;
        if (!TextUtils.isEmpty(nameString2)) {
            nameString = nameString2;
        } else {
            String nameString3 = LocaleController.getString("HiddenName", R.string.HiddenName);
            nameString = nameString3;
        }
        int nameWidth3 = !LocaleController.isRTL ? (getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(14.0f) : (getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(AndroidUtilities.leftBaseline);
        if (this.drawNameLock) {
            nameWidth3 -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (this.drawVerified) {
            int w = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            nameWidth3 -= w;
            if (LocaleController.isRTL) {
                this.nameLeft += w;
            }
        }
        int nameWidth4 = Math.max(AndroidUtilities.dp(12.0f), nameWidth3);
        try {
            nameStringFinal = TextUtils.ellipsize(nameString.replace('\n', ' '), currentNamePaint, nameWidth4 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
            nameWidth = nameWidth4;
        } catch (Exception e2) {
            e = e2;
            nameWidth = nameWidth4;
        }
        try {
            this.nameLayout = new StaticLayout(nameStringFinal, currentNamePaint, nameWidth4, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e3) {
            e = e3;
            FileLog.e(e);
            int messageWidth = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline + 16);
            if (LocaleController.isRTL) {
            }
            this.avatarImage.setImageCoords(avatarLeft, this.avatarTop, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
            int messageWidth2 = Math.max(AndroidUtilities.dp(12.0f), messageWidth);
            int messageWidth3 = AndroidUtilities.dp(12.0f);
            CharSequence messageStringFinal = TextUtils.ellipsize(messageString, currentMessagePaint, messageWidth2 - messageWidth3, TextUtils.TruncateAt.END);
            int messageWidth4 = messageWidth2;
            this.messageLayout = new StaticLayout(messageStringFinal, currentMessagePaint, messageWidth2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (LocaleController.isRTL) {
            }
        }
        int messageWidth5 = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline + 16);
        if (LocaleController.isRTL) {
            this.messageLeft = AndroidUtilities.dp(AndroidUtilities.leftBaseline);
            avatarLeft = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 13.0f : 9.0f);
        } else {
            this.messageLeft = AndroidUtilities.dp(16.0f);
            avatarLeft = getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.isTablet() ? 65.0f : 61.0f);
        }
        this.avatarImage.setImageCoords(avatarLeft, this.avatarTop, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
        int messageWidth22 = Math.max(AndroidUtilities.dp(12.0f), messageWidth5);
        int messageWidth32 = AndroidUtilities.dp(12.0f);
        CharSequence messageStringFinal2 = TextUtils.ellipsize(messageString, currentMessagePaint, messageWidth22 - messageWidth32, TextUtils.TruncateAt.END);
        int messageWidth42 = messageWidth22;
        this.messageLayout = new StaticLayout(messageStringFinal2, currentMessagePaint, messageWidth22, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        if (LocaleController.isRTL) {
            StaticLayout staticLayout = this.nameLayout;
            if (staticLayout != null && staticLayout.getLineCount() > 0) {
                float left = this.nameLayout.getLineLeft(0);
                double widthpx = Math.ceil(this.nameLayout.getLineWidth(0));
                if (!this.drawVerified) {
                    nameWidth2 = nameWidth;
                } else {
                    double d = this.nameLeft;
                    nameWidth2 = nameWidth;
                    double d2 = nameWidth2;
                    Double.isNaN(d2);
                    Double.isNaN(d);
                    double d3 = d + (d2 - widthpx);
                    double dp = AndroidUtilities.dp(6.0f);
                    Double.isNaN(dp);
                    double d4 = d3 - dp;
                    double intrinsicWidth = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    Double.isNaN(intrinsicWidth);
                    this.nameMuteLeft = (int) (d4 - intrinsicWidth);
                }
                if (left == 0.0f && widthpx < nameWidth2) {
                    double d5 = this.nameLeft;
                    double d6 = nameWidth2;
                    Double.isNaN(d6);
                    Double.isNaN(d5);
                    this.nameLeft = (int) (d5 + (d6 - widthpx));
                }
            }
            StaticLayout staticLayout2 = this.messageLayout;
            if (staticLayout2 != null && staticLayout2.getLineCount() > 0) {
                if (this.messageLayout.getLineLeft(0) == 0.0f) {
                    double widthpx2 = Math.ceil(this.messageLayout.getLineWidth(0));
                    int messageWidth6 = messageWidth42;
                    if (widthpx2 < messageWidth6) {
                        double d7 = this.messageLeft;
                        double d8 = messageWidth6;
                        Double.isNaN(d8);
                        Double.isNaN(d7);
                        this.messageLeft = (int) (d7 + (d8 - widthpx2));
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        int nameWidth5 = nameWidth;
        int messageWidth7 = messageWidth42;
        StaticLayout staticLayout3 = this.nameLayout;
        if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
            float left2 = this.nameLayout.getLineRight(0);
            if (left2 == nameWidth5) {
                double widthpx3 = Math.ceil(this.nameLayout.getLineWidth(0));
                if (widthpx3 < nameWidth5) {
                    double d9 = this.nameLeft;
                    double d10 = nameWidth5;
                    Double.isNaN(d10);
                    Double.isNaN(d9);
                    this.nameLeft = (int) (d9 - (d10 - widthpx3));
                }
            }
            if (this.drawVerified) {
                this.nameMuteLeft = (int) (this.nameLeft + left2 + AndroidUtilities.dp(6.0f));
            }
        }
        StaticLayout staticLayout4 = this.messageLayout;
        if (staticLayout4 != null && staticLayout4.getLineCount() > 0 && this.messageLayout.getLineRight(0) == messageWidth7) {
            double widthpx4 = Math.ceil(this.messageLayout.getLineWidth(0));
            if (widthpx4 < messageWidth7) {
                double d11 = this.messageLeft;
                double d12 = messageWidth7;
                Double.isNaN(d12);
                Double.isNaN(d11);
                this.messageLeft = (int) (d11 - (d12 - widthpx4));
            }
        }
    }

    public void setDialogSelected(boolean value) {
        if (this.isSelected != value) {
            invalidate();
        }
        this.isSelected = value;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.isSelected) {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
        }
        if (this.drawNameLock) {
            setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
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
            setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        }
        if (this.useSeparator) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas.drawLine(AndroidUtilities.dp(AndroidUtilities.leftBaseline), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
        this.avatarImage.draw(canvas);
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }
}
