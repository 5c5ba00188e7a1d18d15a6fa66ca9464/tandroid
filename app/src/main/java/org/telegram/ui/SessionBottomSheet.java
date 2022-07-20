package org.telegram.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_changeAuthorizationSettings;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.Switch;
/* loaded from: classes3.dex */
public class SessionBottomSheet extends BottomSheet {
    RLottieImageView imageView;
    BaseFragment parentFragment;
    TLRPC$TL_authorization session;

    /* loaded from: classes3.dex */
    public interface Callback {
        void onSessionTerminated(TLRPC$TL_authorization tLRPC$TL_authorization);
    }

    public static /* synthetic */ void lambda$uploadSessionSettings$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public SessionBottomSheet(BaseFragment baseFragment, TLRPC$TL_authorization tLRPC$TL_authorization, boolean z, Callback callback) {
        super(baseFragment.getParentActivity(), false);
        String str;
        setOpenNoDelay(true);
        Activity parentActivity = baseFragment.getParentActivity();
        this.session = tLRPC$TL_authorization;
        this.parentFragment = baseFragment;
        fixNavigationBar();
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        RLottieImageView rLottieImageView = new RLottieImageView(parentActivity);
        this.imageView = rLottieImageView;
        rLottieImageView.setOnClickListener(new AnonymousClass1());
        this.imageView.setScaleType(ImageView.ScaleType.CENTER);
        linearLayout.addView(this.imageView, LayoutHelper.createLinear(70, 70, 1, 0, 16, 0, 0));
        TextView textView = new TextView(parentActivity);
        textView.setTextSize(2, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        textView.setGravity(17);
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 1, 21, 12, 21, 0));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        textView2.setTextSize(2, 13.0f);
        textView2.setGravity(17);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 1, 21, 4, 21, 21));
        if ((tLRPC$TL_authorization.flags & 1) != 0) {
            str = LocaleController.getString("Online", 2131627132);
        } else {
            str = LocaleController.formatDateTime(tLRPC$TL_authorization.date_active);
        }
        textView2.setText(str);
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
        textView.setText(sb);
        setAnimation(tLRPC$TL_authorization, this.imageView);
        ItemView itemView = new ItemView(parentActivity, false);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(tLRPC$TL_authorization.app_name);
        sb2.append(" ");
        sb2.append(tLRPC$TL_authorization.app_version);
        itemView.valueText.setText(sb2);
        Drawable mutate = ContextCompat.getDrawable(parentActivity, 2131165613).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.SRC_IN));
        itemView.iconView.setImageDrawable(mutate);
        itemView.descriptionText.setText(LocaleController.getString("Application", 2131624394));
        linearLayout.addView(itemView);
        if (tLRPC$TL_authorization.country.length() != 0) {
            ItemView itemView2 = new ItemView(parentActivity, false);
            itemView2.valueText.setText(tLRPC$TL_authorization.country);
            Drawable mutate2 = ContextCompat.getDrawable(parentActivity, 2131165789).mutate();
            mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.SRC_IN));
            itemView2.iconView.setImageDrawable(mutate2);
            itemView2.descriptionText.setText(LocaleController.getString("Location", 2131626537));
            itemView2.setOnClickListener(new AnonymousClass2(tLRPC$TL_authorization));
            itemView2.setOnLongClickListener(new AnonymousClass3(tLRPC$TL_authorization));
            itemView2.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 2));
            linearLayout.addView(itemView2);
            itemView.needDivider = true;
            itemView = itemView2;
        }
        if (tLRPC$TL_authorization.ip.length() != 0) {
            ItemView itemView3 = new ItemView(parentActivity, false);
            itemView3.valueText.setText(tLRPC$TL_authorization.ip);
            Drawable mutate3 = ContextCompat.getDrawable(parentActivity, 2131165774).mutate();
            mutate3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.SRC_IN));
            itemView3.iconView.setImageDrawable(mutate3);
            itemView3.descriptionText.setText(LocaleController.getString("IpAddress", 2131626342));
            itemView3.setOnClickListener(new AnonymousClass4(tLRPC$TL_authorization));
            itemView3.setOnLongClickListener(new AnonymousClass5(tLRPC$TL_authorization));
            itemView3.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 2));
            linearLayout.addView(itemView3);
            itemView.needDivider = true;
            itemView = itemView3;
        }
        if (secretChatsEnabled(tLRPC$TL_authorization)) {
            ItemView itemView4 = new ItemView(parentActivity, true);
            itemView4.valueText.setText(LocaleController.getString("AcceptSecretChats", 2131624124));
            Drawable mutate4 = ContextCompat.getDrawable(parentActivity, 2131165925).mutate();
            mutate4.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.SRC_IN));
            itemView4.iconView.setImageDrawable(mutate4);
            itemView4.switchView.setChecked(!tLRPC$TL_authorization.encrypted_requests_disabled, false);
            itemView4.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 7));
            itemView4.setOnClickListener(new AnonymousClass6(itemView4, tLRPC$TL_authorization));
            itemView.needDivider = true;
            itemView4.descriptionText.setText(LocaleController.getString("AcceptSecretChatsDescription", 2131624125));
            linearLayout.addView(itemView4);
            itemView = itemView4;
        }
        ItemView itemView5 = new ItemView(parentActivity, true);
        itemView5.valueText.setText(LocaleController.getString("AcceptCalls", 2131624122));
        Drawable mutate5 = ContextCompat.getDrawable(parentActivity, 2131165664).mutate();
        mutate5.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.SRC_IN));
        itemView5.iconView.setImageDrawable(mutate5);
        itemView5.switchView.setChecked(!tLRPC$TL_authorization.call_requests_disabled, false);
        itemView5.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 7));
        itemView5.setOnClickListener(new AnonymousClass7(itemView5, tLRPC$TL_authorization));
        itemView.needDivider = true;
        itemView5.descriptionText.setText(LocaleController.getString("AcceptCallsChatsDescription", 2131624123));
        linearLayout.addView(itemView5);
        if (!z) {
            TextView textView3 = new TextView(parentActivity);
            textView3.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
            textView3.setGravity(17);
            textView3.setTextSize(1, 14.0f);
            textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView3.setText(LocaleController.getString("TerminateSession", 2131628651));
            textView3.setTextColor(Theme.getColor("featuredStickers_buttonText"));
            textView3.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("chat_attachAudioBackground"), ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhite"), 120)));
            linearLayout.addView(textView3, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 15.0f, 16.0f, 16.0f));
            textView3.setOnClickListener(new AnonymousClass8(callback, tLRPC$TL_authorization, baseFragment));
        }
        ScrollView scrollView = new ScrollView(parentActivity);
        scrollView.addView(linearLayout);
        setCustomView(scrollView);
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
            SessionBottomSheet.this = r1;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SessionBottomSheet.this.imageView.isPlaying() || SessionBottomSheet.this.imageView.getAnimatedDrawable() == null) {
                return;
            }
            SessionBottomSheet.this.imageView.getAnimatedDrawable().setCurrentFrame(40);
            SessionBottomSheet.this.imageView.playAnimation();
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass2(TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SessionBottomSheet.this.copyText(this.val$session.country);
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements View.OnLongClickListener {
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass3(TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SessionBottomSheet.this.copyText(this.val$session.country);
            return true;
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass4(TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SessionBottomSheet.this.copyText(this.val$session.ip);
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements View.OnLongClickListener {
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass5(TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            SessionBottomSheet.this.copyText(this.val$session.country);
            return true;
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        final /* synthetic */ ItemView val$acceptSecretChats;
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass6(ItemView itemView, TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$acceptSecretChats = itemView;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Switch r3 = this.val$acceptSecretChats.switchView;
            r3.setChecked(!r3.isChecked(), true);
            this.val$session.encrypted_requests_disabled = !this.val$acceptSecretChats.switchView.isChecked();
            SessionBottomSheet.this.uploadSessionSettings();
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        final /* synthetic */ ItemView val$acceptCalls;
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass7(ItemView itemView, TLRPC$TL_authorization tLRPC$TL_authorization) {
            SessionBottomSheet.this = r1;
            this.val$acceptCalls = itemView;
            this.val$session = tLRPC$TL_authorization;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Switch r3 = this.val$acceptCalls.switchView;
            r3.setChecked(!r3.isChecked(), true);
            this.val$session.call_requests_disabled = !this.val$acceptCalls.switchView.isChecked();
            SessionBottomSheet.this.uploadSessionSettings();
        }
    }

    /* renamed from: org.telegram.ui.SessionBottomSheet$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        final /* synthetic */ Callback val$callback;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ TLRPC$TL_authorization val$session;

        AnonymousClass8(Callback callback, TLRPC$TL_authorization tLRPC$TL_authorization, BaseFragment baseFragment) {
            SessionBottomSheet.this = r1;
            this.val$callback = callback;
            this.val$session = tLRPC$TL_authorization;
            this.val$fragment = baseFragment;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SessionBottomSheet.this.parentFragment.getParentActivity());
            builder.setMessage(LocaleController.getString("TerminateSessionText", 2131628654));
            builder.setTitle(LocaleController.getString("AreYouSureSessionTitle", 2131624474));
            builder.setPositiveButton(LocaleController.getString("Terminate", 2131628647), new SessionBottomSheet$8$$ExternalSyntheticLambda0(this, this.val$callback, this.val$session));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            AlertDialog create = builder.create();
            this.val$fragment.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        public /* synthetic */ void lambda$onClick$0(Callback callback, TLRPC$TL_authorization tLRPC$TL_authorization, DialogInterface dialogInterface, int i) {
            callback.onSessionTerminated(tLRPC$TL_authorization);
            SessionBottomSheet.this.dismiss();
        }
    }

    private boolean secretChatsEnabled(TLRPC$TL_authorization tLRPC$TL_authorization) {
        int i = tLRPC$TL_authorization.api_id;
        return (i == 2040 || i == 2496) ? false : true;
    }

    public void uploadSessionSettings() {
        TLRPC$TL_account_changeAuthorizationSettings tLRPC$TL_account_changeAuthorizationSettings = new TLRPC$TL_account_changeAuthorizationSettings();
        TLRPC$TL_authorization tLRPC$TL_authorization = this.session;
        tLRPC$TL_account_changeAuthorizationSettings.encrypted_requests_disabled = tLRPC$TL_authorization.encrypted_requests_disabled;
        tLRPC$TL_account_changeAuthorizationSettings.call_requests_disabled = tLRPC$TL_authorization.call_requests_disabled;
        tLRPC$TL_account_changeAuthorizationSettings.flags = 3;
        tLRPC$TL_account_changeAuthorizationSettings.hash = tLRPC$TL_authorization.hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_changeAuthorizationSettings, SessionBottomSheet$$ExternalSyntheticLambda1.INSTANCE);
    }

    public void copyText(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new CharSequence[]{LocaleController.getString("Copy", 2131625272)}, new SessionBottomSheet$$ExternalSyntheticLambda0(this, str));
        builder.show();
    }

    public /* synthetic */ void lambda$copyText$1(String str, DialogInterface dialogInterface, int i) {
        ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", str));
        BulletinFactory.of(getContainer(), null).createCopyBulletin(LocaleController.getString("TextCopied", 2131628662)).show();
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c4, code lost:
        if (r13.app_name.toLowerCase().contains("desktop") != false) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x010d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setAnimation(TLRPC$TL_authorization tLRPC$TL_authorization, RLottieImageView rLottieImageView) {
        boolean z;
        String lowerCase = tLRPC$TL_authorization.platform.toLowerCase();
        if (lowerCase.isEmpty()) {
            lowerCase = tLRPC$TL_authorization.system_version.toLowerCase();
        }
        String lowerCase2 = tLRPC$TL_authorization.device_model.toLowerCase();
        int i = 2131558625;
        String str = "avatar_backgroundBlue";
        if (lowerCase2.contains("safari")) {
            i = 2131558540;
        } else if (lowerCase2.contains("edge")) {
            i = 2131558444;
        } else {
            if (!lowerCase2.contains("chrome")) {
                if (lowerCase2.contains("opera") || lowerCase2.contains("firefox") || lowerCase2.contains("vivaldi")) {
                    if (lowerCase2.contains("opera")) {
                        i = 2131165383;
                    } else {
                        i = lowerCase2.contains("firefox") ? 2131165382 : 2131165384;
                    }
                    str = "avatar_backgroundPink";
                    z = false;
                    rLottieImageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)));
                    if (!z) {
                        rLottieImageView.setAnimation(i, 50, 50, new int[]{0, Theme.getColor(str)});
                        return;
                    } else {
                        rLottieImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), i));
                        return;
                    }
                }
                if (lowerCase.contains("ubuntu")) {
                    i = 2131558587;
                } else if (lowerCase.contains("ios")) {
                    i = lowerCase2.contains("ipad") ? 2131558480 : 2131558481;
                } else {
                    if (!lowerCase.contains("windows")) {
                        if (lowerCase.contains("macos")) {
                            i = 2131558487;
                        } else if (lowerCase.contains("android")) {
                            i = 2131558401;
                            str = "avatar_backgroundGreen";
                        }
                    }
                    str = "avatar_backgroundCyan";
                }
                z = true;
                rLottieImageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)));
                if (!z) {
                }
            }
            str = "avatar_backgroundPink";
            z = true;
            i = 2131558428;
            rLottieImageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)));
            if (!z) {
            }
        }
        str = "avatar_backgroundPink";
        z = true;
        rLottieImageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(str)));
        if (!z) {
        }
    }

    /* loaded from: classes3.dex */
    public static class ItemView extends FrameLayout {
        TextView descriptionText;
        ImageView iconView;
        boolean needDivider = false;
        Switch switchView;
        TextView valueText;

        public ItemView(Context context, boolean z) {
            super(context);
            ImageView imageView = new ImageView(context);
            this.iconView = imageView;
            addView(imageView, LayoutHelper.createFrame(28, 28.0f, 0, 16.0f, 8.0f, 0.0f, 0.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 0, 64.0f, 4.0f, 0.0f, 4.0f));
            TextView textView = new TextView(context);
            this.valueText = textView;
            textView.setTextSize(2, 16.0f);
            this.valueText.setGravity(3);
            this.valueText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            linearLayout.addView(this.valueText, LayoutHelper.createLinear(-1, -2, 0, 0, 0, z ? 46 : 0, 0));
            TextView textView2 = new TextView(context);
            this.descriptionText = textView2;
            textView2.setTextSize(2, 13.0f);
            this.descriptionText.setGravity(3);
            this.descriptionText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            linearLayout.addView(this.descriptionText, LayoutHelper.createLinear(-1, -2, 0, 0, 4, z ? 46 : 0, 0));
            setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            if (z) {
                Switch r2 = new Switch(context);
                this.switchView = r2;
                r2.setDrawIconType(1);
                addView(this.switchView, LayoutHelper.createFrame(37, 40.0f, 21, 21.0f, 0.0f, 21.0f, 0.0f));
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (this.needDivider) {
                canvas.drawRect(AndroidUtilities.dp(64.0f), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight(), Theme.dividerPaint);
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            String str;
            int i;
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (this.switchView != null) {
                accessibilityNodeInfo.setClassName("android.widget.Switch");
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(this.switchView.isChecked());
                StringBuilder sb = new StringBuilder();
                sb.append((Object) this.valueText.getText());
                sb.append("\n");
                sb.append((Object) this.descriptionText.getText());
                sb.append("\n");
                if (this.switchView.isChecked()) {
                    i = 2131627103;
                    str = "NotificationsOn";
                } else {
                    i = 2131627101;
                    str = "NotificationsOff";
                }
                sb.append(LocaleController.getString(str, i));
                accessibilityNodeInfo.setText(sb.toString());
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        this.imageView.playAnimation();
    }
}
