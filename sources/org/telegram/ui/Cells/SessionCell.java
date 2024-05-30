package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
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
import org.telegram.messenger.R;
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
/* loaded from: classes4.dex */
public class SessionCell extends FrameLayout {
    private AvatarDrawable avatarDrawable;
    private int currentAccount;
    private int currentType;
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
    private AnimatedFloat showStubValue;

    /* JADX WARN: Removed duplicated region for block: B:101:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0271  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02ca  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02d4  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0255  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SessionCell(Context context, int i) {
        super(context);
        int i2;
        this.showStubValue = new AnimatedFloat(this);
        this.currentAccount = UserConfig.selectedAccount;
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout = linearLayout;
        linearLayout.setOrientation(0);
        this.linearLayout.setWeightSum(1.0f);
        this.currentType = i;
        int i3 = 21;
        if (i == 1) {
            LinearLayout linearLayout2 = this.linearLayout;
            boolean z = LocaleController.isRTL;
            addView(linearLayout2, LayoutHelper.createFrame(-1, 30.0f, (z ? 5 : 3) | 48, z ? 15 : 49, 11.0f, z ? 49 : 15, 0.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            this.avatarDrawable = avatarDrawable;
            avatarDrawable.setTextSize(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView2 = this.imageView;
            boolean z2 = LocaleController.isRTL;
            addView(backupImageView2, LayoutHelper.createFrame(20, 20.0f, (z2 ? 5 : 3) | 48, z2 ? 0 : 21, 13.0f, z2 ? 21 : 0, 0.0f));
        } else {
            BackupImageView backupImageView3 = new BackupImageView(context);
            this.placeholderImageView = backupImageView3;
            backupImageView3.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView4 = this.placeholderImageView;
            boolean z3 = LocaleController.isRTL;
            addView(backupImageView4, LayoutHelper.createFrame(42, 42.0f, (z3 ? 5 : 3) | 48, z3 ? 0 : 16, 9.0f, z3 ? 16 : 0, 0.0f));
            BackupImageView backupImageView5 = new BackupImageView(context);
            this.imageView = backupImageView5;
            backupImageView5.setRoundRadius(AndroidUtilities.dp(10.0f));
            BackupImageView backupImageView6 = this.imageView;
            boolean z4 = LocaleController.isRTL;
            addView(backupImageView6, LayoutHelper.createFrame(42, 42.0f, (z4 ? 5 : 3) | 48, z4 ? 0 : 16, 9.0f, z4 ? 16 : 0, 0.0f));
            LinearLayout linearLayout3 = this.linearLayout;
            boolean z5 = LocaleController.isRTL;
            addView(linearLayout3, LayoutHelper.createFrame(-1, 30.0f, (z5 ? 5 : 3) | 48, z5 ? 15 : 72, 6.333f, z5 ? 72 : 15, 0.0f));
        }
        TextView textView = new TextView(context);
        this.nameTextView = textView;
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(Theme.getColor(i4));
        this.nameTextView.setTextSize(1, i == 0 ? 15.0f : 16.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setTypeface(AndroidUtilities.bold());
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        TextView textView2 = new TextView(context);
        this.onlineTextView = textView2;
        textView2.setTextSize(1, i == 0 ? 12.0f : 13.0f);
        this.onlineTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
        if (LocaleController.isRTL) {
            this.linearLayout.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 51, 0, 2, 0, 0));
            this.linearLayout.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0f, 53, 10, 0, 0, 0));
        } else {
            this.linearLayout.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0f, 51, 0, 0, 10, 0));
            this.linearLayout.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 53, 0, 2, 0, 0));
        }
        if (!LocaleController.isRTL) {
            i3 = i == 0 ? 72 : 21;
        } else if (i == 0) {
            i2 = 72;
            TextView textView3 = new TextView(context);
            this.detailTextView = textView3;
            textView3.setTextColor(Theme.getColor(i4));
            this.detailTextView.setTextSize(1, i != 0 ? 13.0f : 14.0f);
            this.detailTextView.setLines(1);
            this.detailTextView.setMaxLines(1);
            this.detailTextView.setSingleLine(true);
            this.detailTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.detailTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
            float f = i3;
            float f2 = i2;
            addView(this.detailTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 5 : 3) | 48, f, i != 0 ? 28.0f : 36.0f, f2, 0.0f));
            TextView textView4 = new TextView(context);
            this.detailExTextView = textView4;
            textView4.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
            this.detailExTextView.setTextSize(1, i != 0 ? 14.0f : 13.0f);
            this.detailExTextView.setLines(1);
            this.detailExTextView.setMaxLines(1);
            this.detailExTextView.setSingleLine(true);
            this.detailExTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.detailExTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
            addView(this.detailExTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, f, i != 0 ? 46.0f : 59.0f, f2, 0.0f));
        }
        i2 = 21;
        TextView textView32 = new TextView(context);
        this.detailTextView = textView32;
        textView32.setTextColor(Theme.getColor(i4));
        this.detailTextView.setTextSize(1, i != 0 ? 13.0f : 14.0f);
        this.detailTextView.setLines(1);
        this.detailTextView.setMaxLines(1);
        this.detailTextView.setSingleLine(true);
        this.detailTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.detailTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        float f3 = i3;
        float f22 = i2;
        addView(this.detailTextView, LayoutHelper.createFrame(-1, -2.0f, (!LocaleController.isRTL ? 5 : 3) | 48, f3, i != 0 ? 28.0f : 36.0f, f22, 0.0f));
        TextView textView42 = new TextView(context);
        this.detailExTextView = textView42;
        textView42.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.detailExTextView.setTextSize(1, i != 0 ? 14.0f : 13.0f);
        this.detailExTextView.setLines(1);
        this.detailExTextView.setMaxLines(1);
        this.detailExTextView.setSingleLine(true);
        this.detailExTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.detailExTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.detailExTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, f3, i != 0 ? 46.0f : 59.0f, f22, 0.0f));
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
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.currentType == 0 ? 70.0f : 90.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    public void setSession(TLObject tLObject, boolean z) {
        String str;
        String stringForMessageListDate;
        this.needDivider = z;
        if (tLObject instanceof TLRPC$TL_authorization) {
            TLRPC$TL_authorization tLRPC$TL_authorization = (TLRPC$TL_authorization) tLObject;
            this.imageView.setImageDrawable(createDrawable(42, tLRPC$TL_authorization));
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
                setTag(Integer.valueOf(Theme.key_windowBackgroundWhiteValueText));
                stringForMessageListDate = LocaleController.getString("Online", R.string.Online);
            } else {
                setTag(Integer.valueOf(Theme.key_windowBackgroundWhiteGrayText3));
                stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$TL_authorization.date_active);
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
            spannableStringBuilder.append((CharSequence) stringForMessageListDate);
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
                this.avatarDrawable.setInfo(this.currentAccount, user);
                str = UserObject.getFirstName(user);
                this.imageView.setForUserOrChat(user, this.avatarDrawable);
            } else {
                str = "";
            }
            int i = Theme.key_windowBackgroundWhiteGrayText3;
            setTag(Integer.valueOf(i));
            this.onlineTextView.setText(LocaleController.stringForMessageListDate(tLRPC$TL_webAuthorization.date_active));
            this.onlineTextView.setTextColor(Theme.getColor(i));
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

    public static Drawable createDrawable(int i, String str) {
        TLRPC$TL_authorization tLRPC$TL_authorization = new TLRPC$TL_authorization();
        tLRPC$TL_authorization.device_model = str;
        tLRPC$TL_authorization.platform = str;
        tLRPC$TL_authorization.app_name = str;
        return createDrawable(i, tLRPC$TL_authorization);
    }

    public static Drawable createDrawable(int i, TLRPC$TL_authorization tLRPC$TL_authorization) {
        int i2;
        int i3;
        int i4;
        String lowerCase = tLRPC$TL_authorization.platform.toLowerCase();
        if (lowerCase.isEmpty()) {
            lowerCase = tLRPC$TL_authorization.system_version.toLowerCase();
        }
        String lowerCase2 = tLRPC$TL_authorization.device_model.toLowerCase();
        if (lowerCase2.contains("safari")) {
            i2 = R.drawable.device_web_safari;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase2.contains("edge")) {
            i2 = R.drawable.device_web_edge;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase2.contains("chrome")) {
            i2 = R.drawable.device_web_chrome;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase2.contains("opera")) {
            i2 = R.drawable.device_web_opera;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase2.contains("firefox")) {
            i2 = R.drawable.device_web_firefox;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase2.contains("vivaldi")) {
            i2 = R.drawable.device_web_other;
            i3 = Theme.key_avatar_backgroundPink;
            i4 = Theme.key_avatar_background2Pink;
        } else if (lowerCase.contains("ios")) {
            i2 = lowerCase2.contains("ipad") ? R.drawable.device_tablet_ios : R.drawable.device_phone_ios;
            i3 = Theme.key_avatar_backgroundBlue;
            i4 = Theme.key_avatar_background2Blue;
        } else if (lowerCase.contains("windows")) {
            i2 = R.drawable.device_desktop_win;
            i3 = Theme.key_avatar_backgroundCyan;
            i4 = Theme.key_avatar_background2Cyan;
        } else if (lowerCase.contains("macos")) {
            i2 = R.drawable.device_desktop_osx;
            i3 = Theme.key_avatar_backgroundCyan;
            i4 = Theme.key_avatar_background2Cyan;
        } else if (lowerCase.contains("android")) {
            i2 = lowerCase2.contains("tab") ? R.drawable.device_tablet_android : R.drawable.device_phone_android;
            i3 = Theme.key_avatar_backgroundGreen;
            i4 = Theme.key_avatar_background2Green;
        } else {
            if (lowerCase.contains("fragment")) {
                i2 = R.drawable.fragment;
            } else if (lowerCase.contains("premiumbot")) {
                i2 = R.drawable.filled_star_plus;
                i3 = Theme.key_color_yellow;
                i4 = Theme.key_color_orange;
            } else if (lowerCase.equals("?")) {
                i2 = R.drawable.msg_emoji_question;
            } else if (tLRPC$TL_authorization.app_name.toLowerCase().contains("desktop")) {
                i2 = R.drawable.device_desktop_other;
                i3 = Theme.key_avatar_backgroundCyan;
                i4 = Theme.key_avatar_background2Cyan;
            } else {
                i2 = R.drawable.device_web_other;
                i3 = Theme.key_avatar_backgroundPink;
                i4 = Theme.key_avatar_background2Pink;
            }
            i3 = -1;
            i4 = -1;
        }
        Drawable mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
        return new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(i), i3 == -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
    }

    /* loaded from: classes4.dex */
    public static class CircleGradientDrawable extends Drawable {
        private Paint paint;
        private int size;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public CircleGradientDrawable(int i, int i2, int i3) {
            this.size = i;
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, i, new int[]{i2, i3}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), Math.min(getBounds().width(), getBounds().height()) / 2.0f, this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.paint.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.size;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.size;
        }
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
            int i = this.currentType == 1 ? 49 : 72;
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(i), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(i) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    public void showStub(FlickerLoadingView flickerLoadingView) {
        this.globalGradient = flickerLoadingView;
        this.showStub = true;
        Drawable mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, AndroidUtilities.isTablet() ? R.drawable.device_tablet_android : R.drawable.device_phone_android).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(Theme.key_avatar_backgroundGreen)), mutate);
        BackupImageView backupImageView = this.placeholderImageView;
        if (backupImageView != null) {
            backupImageView.setImageDrawable(combinedDrawable);
        } else {
            this.imageView.setImageDrawable(combinedDrawable);
        }
        invalidate();
    }
}
