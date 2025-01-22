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
import org.telegram.tgnet.TLRPC;
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

    public static class CircleGradientDrawable extends Drawable {
        private int colorBottom;
        private int colorTop;
        private Paint paint;
        private int size;

        public CircleGradientDrawable(int i, int i2, int i3) {
            this.size = i;
            this.colorTop = i2;
            this.colorBottom = i3;
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, i, new int[]{i2, i3}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), Math.min(getBounds().width(), getBounds().height()) / 2.0f, this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.size;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.size;
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.paint.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0213, code lost:
    
        if (r25 == 0) goto L92;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SessionCell(Context context, int i) {
        super(context);
        LinearLayout linearLayout;
        TextView textView;
        LinearLayout.LayoutParams createLinear;
        this.showStubValue = new AnimatedFloat(this);
        this.currentAccount = UserConfig.selectedAccount;
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.linearLayout = linearLayout2;
        linearLayout2.setOrientation(0);
        this.linearLayout.setWeightSum(1.0f);
        this.currentType = i;
        int i2 = 21;
        if (i == 1) {
            LinearLayout linearLayout3 = this.linearLayout;
            boolean z = LocaleController.isRTL;
            addView(linearLayout3, LayoutHelper.createFrame(-1, 30.0f, (z ? 5 : 3) | 48, z ? 15 : 49, 11.0f, z ? 49 : 15, 0.0f));
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
            LinearLayout linearLayout4 = this.linearLayout;
            boolean z5 = LocaleController.isRTL;
            addView(linearLayout4, LayoutHelper.createFrame(-1, 30.0f, (z5 ? 5 : 3) | 48, z5 ? 15 : 72, 6.333f, z5 ? 72 : 15, 0.0f));
        }
        TextView textView2 = new TextView(context);
        this.nameTextView = textView2;
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        textView2.setTextColor(Theme.getColor(i3));
        this.nameTextView.setTextSize(1, i == 0 ? 15.0f : 16.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setTypeface(AndroidUtilities.bold());
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        TextView textView3 = this.nameTextView;
        TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
        textView3.setEllipsize(truncateAt);
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        TextView textView4 = new TextView(context);
        this.onlineTextView = textView4;
        textView4.setTextSize(1, i == 0 ? 12.0f : 13.0f);
        this.onlineTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
        if (LocaleController.isRTL) {
            this.linearLayout.addView(this.onlineTextView, LayoutHelper.createLinear(-2, -1, 51, 0, 2, 0, 0));
            linearLayout = this.linearLayout;
            textView = this.nameTextView;
            createLinear = LayoutHelper.createLinear(0, -1, 1.0f, 53, 10, 0, 0, 0);
        } else {
            this.linearLayout.addView(this.nameTextView, LayoutHelper.createLinear(0, -1, 1.0f, 51, 0, 0, 10, 0));
            linearLayout = this.linearLayout;
            textView = this.onlineTextView;
            createLinear = LayoutHelper.createLinear(-2, -1, 53, 0, 2, 0, 0);
        }
        linearLayout.addView(textView, createLinear);
        if (!LocaleController.isRTL) {
            i2 = i != 0 ? 21 : 72;
        }
        r3 = 21;
        TextView textView5 = new TextView(context);
        this.detailTextView = textView5;
        textView5.setTextColor(Theme.getColor(i3));
        this.detailTextView.setTextSize(1, i == 0 ? 13.0f : 14.0f);
        this.detailTextView.setLines(1);
        this.detailTextView.setMaxLines(1);
        this.detailTextView.setSingleLine(true);
        this.detailTextView.setEllipsize(truncateAt);
        this.detailTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        float f = i2;
        float f2 = r3;
        addView(this.detailTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, f, i == 0 ? 28.0f : 36.0f, f2, 0.0f));
        TextView textView6 = new TextView(context);
        this.detailExTextView = textView6;
        textView6.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.detailExTextView.setTextSize(1, i != 0 ? 14.0f : 13.0f);
        this.detailExTextView.setLines(1);
        this.detailExTextView.setMaxLines(1);
        this.detailExTextView.setSingleLine(true);
        this.detailExTextView.setEllipsize(truncateAt);
        this.detailExTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.detailExTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, f, i == 0 ? 46.0f : 59.0f, f2, 0.0f));
    }

    public static CombinedDrawable createDrawable(int i, String str) {
        TLRPC.TL_authorization tL_authorization = new TLRPC.TL_authorization();
        tL_authorization.device_model = str;
        tL_authorization.platform = str;
        tL_authorization.app_name = str;
        return createDrawable(i, tL_authorization);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static CombinedDrawable createDrawable(int i, TLRPC.TL_authorization tL_authorization) {
        int i2;
        int i3;
        int i4;
        Drawable mutate;
        float f;
        CombinedDrawable combinedDrawable;
        String lowerCase = tL_authorization.platform.toLowerCase();
        if (lowerCase.isEmpty()) {
            lowerCase = tL_authorization.system_version.toLowerCase();
        }
        String lowerCase2 = tL_authorization.device_model.toLowerCase();
        if (lowerCase2.contains("safari")) {
            i2 = R.drawable.device_web_safari;
        } else if (lowerCase2.contains("edge")) {
            i2 = R.drawable.device_web_edge;
        } else if (lowerCase2.contains("chrome")) {
            i2 = R.drawable.device_web_chrome;
        } else if (lowerCase2.contains("opera")) {
            i2 = R.drawable.device_web_opera;
        } else if (lowerCase2.contains("firefox")) {
            i2 = R.drawable.device_web_firefox;
        } else {
            if (!lowerCase2.contains("vivaldi")) {
                if (!lowerCase.contains("ios")) {
                    if (lowerCase.contains("windows")) {
                        i2 = R.drawable.device_desktop_win;
                    } else {
                        if (!lowerCase.contains("macos")) {
                            if (!lowerCase.contains("android")) {
                                if (lowerCase.contains("fragment")) {
                                    i2 = R.drawable.fragment;
                                } else if (lowerCase.contains("anonymous")) {
                                    i2 = R.drawable.large_hidden;
                                } else {
                                    if (lowerCase.contains("premiumbot")) {
                                        i2 = R.drawable.filled_star_plus;
                                        i3 = Theme.key_color_yellow;
                                        i4 = Theme.key_color_orange;
                                        mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
                                        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
                                        f = i;
                                        combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
                                        if (lowerCase != null && lowerCase.contains("fragment")) {
                                            combinedDrawable.setIconSize((int) ((mutate.getIntrinsicWidth() / 44.0f) * f), (int) ((mutate.getIntrinsicHeight() / 44.0f) * f));
                                        }
                                        return combinedDrawable;
                                    }
                                    if (lowerCase.contains("ads")) {
                                        i2 = R.drawable.msg_channel;
                                    } else if (lowerCase.contains("api")) {
                                        i2 = R.drawable.filled_paid_broadcast;
                                    } else if (lowerCase.equals("?")) {
                                        i2 = R.drawable.msg_emoji_question;
                                    } else if (tL_authorization.app_name.toLowerCase().contains("desktop")) {
                                        i2 = R.drawable.device_desktop_other;
                                    }
                                }
                                i3 = -1;
                                i4 = -1;
                                mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
                                mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
                                f = i;
                                combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
                                if (lowerCase != null) {
                                    combinedDrawable.setIconSize((int) ((mutate.getIntrinsicWidth() / 44.0f) * f), (int) ((mutate.getIntrinsicHeight() / 44.0f) * f));
                                }
                                return combinedDrawable;
                            }
                            i2 = lowerCase2.contains("tab") ? R.drawable.device_tablet_android : R.drawable.device_phone_android;
                            i3 = Theme.key_avatar_backgroundGreen;
                            i4 = Theme.key_avatar_background2Green;
                            mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
                            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
                            f = i;
                            combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
                            if (lowerCase != null) {
                            }
                            return combinedDrawable;
                        }
                        i2 = R.drawable.device_desktop_osx;
                    }
                    i3 = Theme.key_avatar_backgroundCyan;
                    i4 = Theme.key_avatar_background2Cyan;
                    mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
                    f = i;
                    combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
                    if (lowerCase != null) {
                    }
                    return combinedDrawable;
                }
                i2 = lowerCase2.contains("ipad") ? R.drawable.device_tablet_ios : R.drawable.device_phone_ios;
                i3 = Theme.key_avatar_backgroundBlue;
                i4 = Theme.key_avatar_background2Blue;
                mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
                f = i;
                combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
                if (lowerCase != null) {
                }
                return combinedDrawable;
            }
            i2 = R.drawable.device_web_other;
        }
        i3 = Theme.key_avatar_backgroundPink;
        i4 = Theme.key_avatar_background2Pink;
        mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, i2).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
        f = i;
        combinedDrawable = new CombinedDrawable(new CircleGradientDrawable(AndroidUtilities.dp(f), i3 != -1 ? -16777216 : Theme.getColor(i3), i4 != -1 ? Theme.getColor(i4) : -16777216), mutate);
        if (lowerCase != null) {
        }
        return combinedDrawable;
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

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.currentType == 0 ? 70.0f : 90.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSession(TLObject tLObject, boolean z) {
        String str;
        StringBuilder sb;
        String str2;
        String stringForMessageListDate;
        this.needDivider = z;
        if (!(tLObject instanceof TLRPC.TL_authorization)) {
            if (tLObject instanceof TLRPC.TL_webAuthorization) {
                TLRPC.TL_webAuthorization tL_webAuthorization = (TLRPC.TL_webAuthorization) tLObject;
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tL_webAuthorization.bot_id));
                this.nameTextView.setText(tL_webAuthorization.domain);
                if (user != null) {
                    this.avatarDrawable.setInfo(this.currentAccount, user);
                    str = UserObject.getFirstName(user);
                    this.imageView.setForUserOrChat(user, this.avatarDrawable);
                } else {
                    str = "";
                }
                int i = Theme.key_windowBackgroundWhiteGrayText3;
                setTag(Integer.valueOf(i));
                this.onlineTextView.setText(LocaleController.stringForMessageListDate(tL_webAuthorization.date_active));
                this.onlineTextView.setTextColor(Theme.getColor(i));
                StringBuilder sb2 = new StringBuilder();
                if (tL_webAuthorization.ip.length() != 0) {
                    sb2.append(tL_webAuthorization.ip);
                }
                if (tL_webAuthorization.region.length() != 0) {
                    if (sb2.length() != 0) {
                        sb2.append(" ");
                    }
                    sb2.append("— ");
                    sb2.append(tL_webAuthorization.region);
                }
                this.detailExTextView.setText(sb2);
                sb = new StringBuilder();
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str);
                }
                if (tL_webAuthorization.browser.length() != 0) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(tL_webAuthorization.browser);
                }
                if (tL_webAuthorization.platform.length() != 0) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    str2 = tL_webAuthorization.platform;
                }
                this.detailTextView.setText(sb);
            }
            if (this.showStub) {
                return;
            }
            this.showStub = false;
            invalidate();
            return;
        }
        TLRPC.TL_authorization tL_authorization = (TLRPC.TL_authorization) tLObject;
        this.imageView.setImageDrawable(createDrawable(42, tL_authorization));
        StringBuilder sb3 = new StringBuilder();
        if (tL_authorization.device_model.length() != 0) {
            sb3.append(tL_authorization.device_model);
        }
        if (sb3.length() == 0) {
            if (tL_authorization.platform.length() != 0) {
                sb3.append(tL_authorization.platform);
            }
            if (tL_authorization.system_version.length() != 0) {
                if (tL_authorization.platform.length() != 0) {
                    sb3.append(" ");
                }
                sb3.append(tL_authorization.system_version);
            }
        }
        this.nameTextView.setText(sb3);
        if ((tL_authorization.flags & 1) != 0) {
            setTag(Integer.valueOf(Theme.key_windowBackgroundWhiteValueText));
            stringForMessageListDate = LocaleController.getString(R.string.Online);
        } else {
            setTag(Integer.valueOf(Theme.key_windowBackgroundWhiteGrayText3));
            stringForMessageListDate = LocaleController.stringForMessageListDate(tL_authorization.date_active);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (tL_authorization.country.length() != 0) {
            spannableStringBuilder.append((CharSequence) tL_authorization.country);
        }
        if (spannableStringBuilder.length() != 0) {
            DotDividerSpan dotDividerSpan = new DotDividerSpan();
            dotDividerSpan.setTopPadding(AndroidUtilities.dp(1.5f));
            spannableStringBuilder.append((CharSequence) " . ").setSpan(dotDividerSpan, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 0);
        }
        spannableStringBuilder.append((CharSequence) stringForMessageListDate);
        this.detailExTextView.setText(spannableStringBuilder);
        sb = new StringBuilder();
        sb.append(tL_authorization.app_name);
        sb.append(" ");
        str2 = tL_authorization.app_version;
        sb.append(str2);
        this.detailTextView.setText(sb);
        if (this.showStub) {
        }
    }

    public void showStub(FlickerLoadingView flickerLoadingView) {
        this.globalGradient = flickerLoadingView;
        this.showStub = true;
        Drawable mutate = ContextCompat.getDrawable(ApplicationLoader.applicationContext, AndroidUtilities.isTablet() ? R.drawable.device_tablet_android : R.drawable.device_phone_android).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_avatar_text), PorterDuff.Mode.SRC_IN));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(Theme.key_avatar_backgroundGreen)), mutate);
        BackupImageView backupImageView = this.placeholderImageView;
        if (backupImageView == null) {
            backupImageView = this.imageView;
        }
        backupImageView.setImageDrawable(combinedDrawable);
        invalidate();
    }
}
