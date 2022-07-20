package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_deleteAccount;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_acceptTermsOfService;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class TermsOfServiceView extends FrameLayout {
    private int currentAccount;
    private TLRPC$TL_help_termsOfService currentTos;
    private TermsOfServiceViewDelegate delegate;
    private ScrollView scrollView;
    private TextView textView;
    private TextView titleTextView;

    /* loaded from: classes3.dex */
    public interface TermsOfServiceViewDelegate {
        void onAcceptTerms(int i);
    }

    public static /* synthetic */ void lambda$accept$7(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public TermsOfServiceView(Context context) {
        super(context);
        setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        int i = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
        if (i > 0) {
            View view = new View(context);
            view.setBackgroundColor(-16777216);
            addView(view, new FrameLayout.LayoutParams(-1, i));
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165593);
        linearLayout.addView(imageView, LayoutHelper.createLinear(-2, -2, 3, 0, 28, 0, 0));
        TextView textView = new TextView(context);
        this.titleTextView = textView;
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleTextView.setTextSize(1, 17.0f);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setText(LocaleController.getString("PrivacyPolicyAndTerms", 2131627729));
        linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 3, 0, 20, 0, 0));
        TextView textView2 = new TextView(context);
        this.textView = textView2;
        textView2.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        this.textView.setGravity(51);
        this.textView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        linearLayout.addView(this.textView, LayoutHelper.createLinear(-1, -2, 3, 0, 15, 0, 15));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setVerticalScrollBarEnabled(false);
        this.scrollView.setOverScrollMode(2);
        this.scrollView.setPadding(AndroidUtilities.dp(24.0f), i, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(75.0f));
        this.scrollView.addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
        addView(this.scrollView, LayoutHelper.createLinear(-1, -2));
        TextView textView3 = new TextView(context);
        textView3.setText(LocaleController.getString("Decline", 2131625363).toUpperCase());
        textView3.setGravity(17);
        textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView3.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        textView3.setTextSize(1, 14.0f);
        textView3.setBackground(Theme.getRoundRectSelectorDrawable(Theme.getColor("windowBackgroundWhiteGrayText")));
        textView3.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f));
        addView(textView3, LayoutHelper.createFrame(-2, -2.0f, 83, 16.0f, 0.0f, 16.0f, 16.0f));
        textView3.setOnClickListener(new TermsOfServiceView$$ExternalSyntheticLambda4(this));
        TextView textView4 = new TextView(context);
        textView4.setText(LocaleController.getString("Accept", 2131624120));
        textView4.setGravity(17);
        textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView4.setTextColor(-1);
        textView4.setTextSize(1, 14.0f);
        textView4.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), -11491093, -12346402));
        textView4.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        addView(textView4, LayoutHelper.createFrame(-2, 42.0f, 85, 16.0f, 0.0f, 16.0f, 16.0f));
        textView4.setOnClickListener(new TermsOfServiceView$$ExternalSyntheticLambda3(this));
        View view2 = new View(context);
        view2.setBackgroundColor(Theme.getColor("divider"));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, 1);
        layoutParams.bottomMargin = AndroidUtilities.dp(75.0f);
        layoutParams.gravity = 80;
        addView(view2, layoutParams);
    }

    public /* synthetic */ void lambda$new$4(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(LocaleController.getString("TermsOfService", 2131628593));
        builder.setPositiveButton(LocaleController.getString("DeclineDeactivate", 2131625365), new TermsOfServiceView$$ExternalSyntheticLambda2(this));
        builder.setNegativeButton(LocaleController.getString("Back", 2131624636), null);
        builder.setMessage(LocaleController.getString("TosUpdateDecline", 2131628686));
        builder.show();
    }

    public /* synthetic */ void lambda$new$3(DialogInterface dialogInterface, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(LocaleController.getString("TosDeclineDeleteAccount", 2131628685));
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        builder.setPositiveButton(LocaleController.getString("Deactivate", 2131625334), new TermsOfServiceView$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        builder.show();
    }

    public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface, int i) {
        AlertDialog alertDialog = new AlertDialog(getContext(), 3);
        alertDialog.setCanCancel(false);
        TLRPC$TL_account_deleteAccount tLRPC$TL_account_deleteAccount = new TLRPC$TL_account_deleteAccount();
        tLRPC$TL_account_deleteAccount.reason = "Decline ToS update";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteAccount, new TermsOfServiceView$$ExternalSyntheticLambda6(this, alertDialog));
        alertDialog.show();
    }

    public /* synthetic */ void lambda$new$1(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TermsOfServiceView$$ExternalSyntheticLambda5(this, alertDialog, tLObject, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$new$0(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MessagesController.getInstance(this.currentAccount).performLogout(0);
        } else if (tLRPC$TL_error != null && tLRPC$TL_error.code == -1000) {
        } else {
            String string = LocaleController.getString("ErrorOccurred", 2131625657);
            if (tLRPC$TL_error != null) {
                string = string + "\n" + tLRPC$TL_error.text;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setMessage(string);
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            builder.show();
        }
    }

    public /* synthetic */ void lambda$new$6(View view) {
        if (this.currentTos.min_age_confirm != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(LocaleController.getString("TosAgeTitle", 2131628683));
            builder.setPositiveButton(LocaleController.getString("Agree", 2131624307), new TermsOfServiceView$$ExternalSyntheticLambda1(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            builder.setMessage(LocaleController.formatString("TosAgeText", 2131628682, LocaleController.formatPluralString("Years", this.currentTos.min_age_confirm, new Object[0])));
            builder.show();
            return;
        }
        accept();
    }

    public /* synthetic */ void lambda$new$5(DialogInterface dialogInterface, int i) {
        accept();
    }

    private void accept() {
        this.delegate.onAcceptTerms(this.currentAccount);
        TLRPC$TL_help_acceptTermsOfService tLRPC$TL_help_acceptTermsOfService = new TLRPC$TL_help_acceptTermsOfService();
        tLRPC$TL_help_acceptTermsOfService.id = this.currentTos.id;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_acceptTermsOfService, TermsOfServiceView$$ExternalSyntheticLambda7.INSTANCE);
    }

    public void show(int i, TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService) {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tLRPC$TL_help_termsOfService.text);
        MessageObject.addEntitiesToText(spannableStringBuilder, tLRPC$TL_help_termsOfService.entities, false, false, false, false);
        addBulletsToText(spannableStringBuilder, '-', AndroidUtilities.dp(10.0f), -11491093, AndroidUtilities.dp(4.0f));
        this.textView.setText(spannableStringBuilder);
        this.currentTos = tLRPC$TL_help_termsOfService;
        this.currentAccount = i;
    }

    public void setDelegate(TermsOfServiceViewDelegate termsOfServiceViewDelegate) {
        this.delegate = termsOfServiceViewDelegate;
    }

    private static void addBulletsToText(SpannableStringBuilder spannableStringBuilder, char c, int i, int i2, int i3) {
        int length = spannableStringBuilder.length() - 2;
        for (int i4 = 0; i4 < length; i4++) {
            if (spannableStringBuilder.charAt(i4) == '\n') {
                int i5 = i4 + 1;
                if (spannableStringBuilder.charAt(i5) == c) {
                    int i6 = i4 + 2;
                    if (spannableStringBuilder.charAt(i6) == ' ') {
                        BulletSpan bulletSpan = new BulletSpan(i, i2, i3);
                        spannableStringBuilder.replace(i5, i4 + 3, "\u0000\u0000");
                        spannableStringBuilder.setSpan(bulletSpan, i5, i6, 33);
                    }
                }
            }
        }
    }
}
