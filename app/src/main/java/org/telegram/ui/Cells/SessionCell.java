package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_webAuthorization;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.DotDividerSpan;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class SessionCell extends FrameLayout {
    private AvatarDrawable avatarDrawable;
    private TextView detailExTextView;
    private TextView detailTextView;
    FlickerLoadingView globalGradient;
    private BackupImageView imageView;
    LinearLayout linearLayout;
    private TextView nameTextView;
    private boolean needDivider;
    private TextView onlineTextView;
    private BackupImageView placeholderImageView;
    private boolean showStub;
    private AnimatedFloat showStubValue = new AnimatedFloat(this);
    private int currentAccount = UserConfig.selectedAccount;

    /* JADX WARN: Removed duplicated region for block: B:85:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0294  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SessionCell(Context context, int i) {
        super(context);
        int i2;
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout = linearLayout;
        int i3 = 0;
        linearLayout.setOrientation(0);
        this.linearLayout.setWeightSum(1.0f);
        int i4 = 15;
        int i5 = 21;
        int i6 = 5;
        if (i == 1) {
            LinearLayout linearLayout2 = this.linearLayout;
            boolean z = LocaleController.isRTL;
            addView(linearLayout2, LayoutHelper.createFrame(-1, 30.0f, (z ? 5 : 3) | 48, z ? 15 : 49, 11.0f, z ? 49 : i4, 0.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            this.avatarDrawable = avatarDrawable;
            avatarDrawable.setTextSize(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView2 = this.imageView;
            boolean z2 = LocaleController.isRTL;
            addView(backupImageView2, LayoutHelper.createFrame(20, 20.0f, (z2 ? 5 : 3) | 48, z2 ? 0 : 21, 13.0f, z2 ? 21 : i3, 0.0f));
        } else {
            BackupImageView backupImageView3 = new BackupImageView(context);
            this.placeholderImageView = backupImageView3;
            backupImageView3.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView4 = this.placeholderImageView;
            boolean z3 = LocaleController.isRTL;
            addView(backupImageView4, LayoutHelper.createFrame(42, 42.0f, (z3 ? 5 : 3) | 48, z3 ? 0 : 16, 13.0f, z3 ? 16 : 0, 0.0f));
            BackupImageView backupImageView5 = new BackupImageView(context);
            this.imageView = backupImageView5;
            backupImageView5.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView6 = this.imageView;
            boolean z4 = LocaleController.isRTL;
            addView(backupImageView6, LayoutHelper.createFrame(42, 42.0f, (z4 ? 5 : 3) | 48, z4 ? 0 : 16, 13.0f, z4 ? 16 : i3, 0.0f));
            LinearLayout linearLayout3 = this.linearLayout;
            boolean z5 = LocaleController.isRTL;
            addView(linearLayout3, LayoutHelper.createFrame(-1, 30.0f, (z5 ? 5 : 3) | 48, z5 ? 15 : 72, 11.0f, z5 ? 72 : i4, 0.0f));
        }
        TextView textView = new TextView(context);
        this.nameTextView = textView;
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 16.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        TextView textView2 = new TextView(context);
        this.onlineTextView = textView2;
        textView2.setTextSize(1, 14.0f);
        this.onlineTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
        if (LocaleController.isRTL) {
            this.linearLayout.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 51, 0, 2, 0, 0));
            this.linearLayout.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0f, 53, 10, 0, 0, 0));
        } else {
            this.linearLayout.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0f, 51, 0, 0, 10, 0));
            this.linearLayout.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 53, 0, 2, 0, 0));
        }
        if (!LocaleController.isRTL) {
            i5 = i == 0 ? 72 : 21;
        } else if (i == 0) {
            i2 = 72;
            TextView textView3 = new TextView(context);
            this.detailTextView = textView3;
            textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.detailTextView.setTextSize(1, 14.0f);
            this.detailTextView.setLines(1);
            this.detailTextView.setMaxLines(1);
            this.detailTextView.setSingleLine(true);
            this.detailTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.detailTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
            float f = i5;
            float f2 = i2;
            addView(this.detailTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 5 : 3) | 48, f, 36.0f, f2, 0.0f));
            TextView textView4 = new TextView(context);
            this.detailExTextView = textView4;
            textView4.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
            this.detailExTextView.setTextSize(1, 14.0f);
            this.detailExTextView.setLines(1);
            this.detailExTextView.setMaxLines(1);
            this.detailExTextView.setSingleLine(true);
            this.detailExTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.detailExTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
            addView(this.detailExTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 3 : i6) | 48, f, 59.0f, f2, 0.0f));
        }
        i2 = 21;
        TextView textView32 = new TextView(context);
        this.detailTextView = textView32;
        textView32.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.detailTextView.setTextSize(1, 14.0f);
        this.detailTextView.setLines(1);
        this.detailTextView.setMaxLines(1);
        this.detailTextView.setSingleLine(true);
        this.detailTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.detailTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        float f3 = i5;
        float f22 = i2;
        addView(this.detailTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 5 : 3) | 48, f3, 36.0f, f22, 0.0f));
        TextView textView42 = new TextView(context);
        this.detailExTextView = textView42;
        textView42.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.detailExTextView.setTextSize(1, 14.0f);
        this.detailExTextView.setLines(1);
        this.detailExTextView.setMaxLines(1);
        this.detailExTextView.setSingleLine(true);
        this.detailExTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.detailExTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.detailExTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 3 : i6) | 48, f3, 59.0f, f22, 0.0f));
    }

    private void setContentAlpha(float f) {
        TextView textView = this.detailExTextView;
        if (textView != null) {
            textView.setAlpha(f);
        }
        TextView textView2 = this.detailTextView;
        if (textView2 != null) {
            textView2.setAlpha(f);
        }
        TextView textView3 = this.nameTextView;
        if (textView3 != null) {
            textView3.setAlpha(f);
        }
        TextView textView4 = this.onlineTextView;
        if (textView4 != null) {
            textView4.setAlpha(f);
        }
        BackupImageView backupImageView = this.imageView;
        if (backupImageView != null) {
            backupImageView.setAlpha(f);
        }
        BackupImageView backupImageView2 = this.placeholderImageView;
        if (backupImageView2 != null) {
            backupImageView2.setAlpha(1.0f - f);
        }
        LinearLayout linearLayout = this.linearLayout;
        if (linearLayout != null) {
            linearLayout.setAlpha(f);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(90.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    public void setSession(TLObject tLObject, boolean z) {
        String str;
        String str2;
        this.needDivider = z;
        if (tLObject instanceof TLRPC$TL_authorization) {
            TLRPC$TL_authorization tLRPC$TL_authorization = (TLRPC$TL_authorization) tLObject;
            this.imageView.setImageDrawable(createDrawable(tLRPC$TL_authorization));
            StringBuilder sb = new StringBuilder();
            if (tLRPC$TL_authorization.device_model.length() != 0) {
                sb.append(tLRPC$TL_authorization.device_model);
            }
            if (sb.length() == 0) {
                if (tLRPC$TL_authorization.platform.length() != 0) {
                    sb.append(tLRPC$TL_authorization.platform);
                }
                if (tLRPC$TL_authorization.system_version.length() != 0) {
                    if (tLRPC$TL_authorization.platform.length() != 0) {
                        sb.append(" ");
                    }
                    sb.append(tLRPC$TL_authorization.system_version);
                }
            }
            this.nameTextView.setText(sb);
            if ((tLRPC$TL_authorization.flags & 1) != 0) {
                setTag("windowBackgroundWhiteValueText");
                str2 = LocaleController.getString("Online", 2131627132);
            } else {
                setTag("windowBackgroundWhiteGrayText3");
                str2 = LocaleController.stringForMessageListDate(tLRPC$TL_authorization.date_active);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            if (tLRPC$TL_authorization.country.length() != 0) {
                spannableStringBuilder.append((CharSequence) tLRPC$TL_authorization.country);
            }
            if (spannableStringBuilder.length() != 0) {
                DotDividerSpan dotDividerSpan = new DotDividerSpan();
                dotDividerSpan.setTopPadding(AndroidUtilities.dp(1.5f));
                spannableStringBuilder.append((CharSequence) " . ").setSpan(dotDividerSpan, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 0);
            }
            spannableStringBuilder.append((CharSequence) str2);
            this.detailExTextView.setText(spannableStringBuilder);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(tLRPC$TL_authorization.app_name);
            sb2.append(" ");
            sb2.append(tLRPC$TL_authorization.app_version);
            this.detailTextView.setText(sb2);
        } else if (tLObject instanceof TLRPC$TL_webAuthorization) {
            TLRPC$TL_webAuthorization tLRPC$TL_webAuthorization = (TLRPC$TL_webAuthorization) tLObject;
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_webAuthorization.bot_id));
            this.nameTextView.setText(tLRPC$TL_webAuthorization.domain);
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                str = UserObject.getFirstName(user);
                this.imageView.setForUserOrChat(user, this.avatarDrawable);
            } else {
                str = "";
            }
            setTag("windowBackgroundWhiteGrayText3");
            this.onlineTextView.setText(LocaleController.stringForMessageListDate(tLRPC$TL_webAuthorization.date_active));
            this.onlineTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
            StringBuilder sb3 = new StringBuilder();
            if (tLRPC$TL_webAuthorization.ip.length() != 0) {
                sb3.append(tLRPC$TL_webAuthorization.ip);
            }
            if (tLRPC$TL_webAuthorization.region.length() != 0) {
                if (sb3.length() != 0) {
                    sb3.append(" ");
                }
                sb3.append("— ");
                sb3.append(tLRPC$TL_webAuthorization.region);
            }
            this.detailExTextView.setText(sb3);
            StringBuilder sb4 = new StringBuilder();
            if (!TextUtils.isEmpty(str)) {
                sb4.append(str);
            }
            if (tLRPC$TL_webAuthorization.browser.length() != 0) {
                if (sb4.length() != 0) {
                    sb4.append(", ");
                }
                sb4.append(tLRPC$TL_webAuthorization.browser);
            }
            if (tLRPC$TL_webAuthorization.platform.length() != 0) {
                if (sb4.length() != 0) {
                    sb4.append(", ");
                }
                sb4.append(tLRPC$TL_webAuthorization.platform);
            }
            this.detailTextView.setText(sb4);
        }
        if (this.showStub) {
            this.showStub = false;
            invalidate();
        }
    }

    public static Drawable createDrawable(TLRPC$TL_authorization tLRPC$TL_authorization) {
        String lowerCase = tLRPC$TL_authorization.platform.toLowerCase();
        if (lowerCase.isEmpty()) {
            lowerCase = tLRPC$TL_authorization.system_version.toLowerCase();
        }
        String lowerCase2 = tLRPC$TL_authorization.device_model.toLowerCase();
        int i = 2131165384;
        String str = "avatar_backgroundCyan";
        if (lowerCase2.contains("safari")) {
            i = 2131165385;
        } else if (lowerCase2.contains("edge")) {
            i = 2131165381;
        } else if (lowerCase2.contains("chrome")) {
            i = 2131165380;
        } else if (lowerCase2.contains("opera")) {
            i = 2131165383;
        } else if (lowerCase2.contains("firefox")) {
            i = 2131165382;
        } else if (!lowerCase2.contains("vivaldi")) {
            if (lowerCase.contains("ios")) {
                i = lowerCase2.contains("ipad") ? 2131165379 : 2131165376;
                str = "avatar_backgroundBlue";
            } else if (lowerCase.contains("windows")) {
                i = 2131165374;
            } else if (lowerCase.contains("macos")) {
                i = 2131165372;
            } else if (lowerCase.contains("android")) {
                i = lowerCase2.contains("tab") ? 2131165378 : 2131165375;
                str = "avatar_backgroundGreen";
            } else if (tLRPC$TL_authorization.app_name.toLowerCase().contains("desktop")) {
                i = 2131165373;
            }
            Drawable mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("avatar_text"), PorterDuff.Mode.SRC_IN));
            return new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)), mutate);
        }
        str = "avatar_backgroundPink";
        Drawable mutate2 = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("avatar_text"), PorterDuff.Mode.SRC_IN));
        return new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)), mutate2);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f = this.showStubValue.set(this.showStub ? 1.0f : 0.0f);
        setContentAlpha(1.0f - f);
        if (f > 0.0f && this.globalGradient != null) {
            if (f < 1.0f) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.saveLayerAlpha(rectF, (int) (255.0f * f), 31);
            }
            this.globalGradient.updateColors();
            this.globalGradient.updateGradient();
            if (getParent() != null) {
                View view = (View) getParent();
                this.globalGradient.setParentSize(view.getMeasuredWidth(), view.getMeasuredHeight(), -getX());
            }
            float top = this.linearLayout.getTop() + this.nameTextView.getTop() + AndroidUtilities.dp(12.0f);
            float x = this.linearLayout.getX();
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(x, top - AndroidUtilities.dp(4.0f), (getMeasuredWidth() * 0.2f) + x, top + AndroidUtilities.dp(4.0f));
            canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.globalGradient.getPaint());
            float top2 = (this.linearLayout.getTop() + this.detailTextView.getTop()) - AndroidUtilities.dp(1.0f);
            float x2 = this.linearLayout.getX();
            rectF2.set(x2, top2 - AndroidUtilities.dp(4.0f), (getMeasuredWidth() * 0.4f) + x2, top2 + AndroidUtilities.dp(4.0f));
            canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.globalGradient.getPaint());
            float top3 = (this.linearLayout.getTop() + this.detailExTextView.getTop()) - AndroidUtilities.dp(1.0f);
            float x3 = this.linearLayout.getX();
            rectF2.set(x3, top3 - AndroidUtilities.dp(4.0f), (getMeasuredWidth() * 0.3f) + x3, top3 + AndroidUtilities.dp(4.0f));
            canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.globalGradient.getPaint());
            invalidate();
            if (f < 1.0f) {
                canvas.restore();
            }
        }
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    public void showStub(FlickerLoadingView flickerLoadingView) {
        this.globalGradient = flickerLoadingView;
        this.showStub = true;
        Drawable mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, AndroidUtilities.isTablet() ? 2131165378 : 2131165375).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("avatar_text"), PorterDuff.Mode.SRC_IN));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor("avatar_backgroundGreen")), mutate);
        BackupImageView backupImageView = this.placeholderImageView;
        if (backupImageView != null) {
            backupImageView.setImageDrawable(combinedDrawable);
        } else {
            this.imageView.setImageDrawable(combinedDrawable);
        }
        invalidate();
    }
}
