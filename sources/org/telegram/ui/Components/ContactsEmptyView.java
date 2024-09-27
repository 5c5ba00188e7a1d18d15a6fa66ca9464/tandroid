package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ContactsEmptyView extends LinearLayout {
    private int currentAccount;
    private LoadingStickerDrawable drawable;
    private ArrayList imageViews;
    private BackupImageView stickerView;
    private ArrayList textViews;
    private TextView titleTextView;

    /* JADX WARN: Removed duplicated region for block: B:27:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0162  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ContactsEmptyView(Context context) {
        super(context);
        int i;
        this.textViews = new ArrayList();
        this.imageViews = new ArrayList();
        this.currentAccount = UserConfig.selectedAccount;
        setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
        setOrientation(1);
        this.stickerView = new BackupImageView(context);
        LoadingStickerDrawable loadingStickerDrawable = new LoadingStickerDrawable(this.stickerView, "m418 282.6c13.4-21.1 20.2-44.9 20.2-70.8 0-88.3-79.8-175.3-178.9-175.3-100.1 0-178.9 88-178.9 175.3 0 46.6 16.9 73.1 29.1 86.1-19.3 23.4-30.9 52.3-34.6 86.1-2.5 22.7 3.2 41.4 17.4 57.3 14.3 16 51.7 35 148.1 35 41.2 0 119.9-5.3 156.7-18.3 49.5-17.4 59.2-41.1 59.2-76.2 0-41.5-12.9-74.8-38.3-99.2z", AndroidUtilities.dp(130.0f), AndroidUtilities.dp(130.0f));
        this.drawable = loadingStickerDrawable;
        this.stickerView.setImageDrawable(loadingStickerDrawable);
        if (!AndroidUtilities.isTablet()) {
            addView(this.stickerView, LayoutHelper.createLinear((int) NotificationCenter.walletSyncProgressChanged, (int) NotificationCenter.walletSyncProgressChanged, 49, 0, 2, 0, 0));
        }
        TextView textView = new TextView(context);
        this.titleTextView = textView;
        textView.setTextSize(1, 20.0f);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.titleTextView.setGravity(1);
        this.titleTextView.setText(LocaleController.getString(R.string.NoContactsYet));
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        this.titleTextView.setMaxWidth(AndroidUtilities.dp(260.0f));
        addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 14));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        addView(linearLayout, LayoutHelper.createLinear(-2, -2, 49));
        int i2 = 0;
        while (true) {
            if (i2 >= 3) {
                return;
            }
            if (i2 != 1) {
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(0);
                linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 0, 8, 0, 0));
                ImageView imageView = new ImageView(context);
                int i3 = Theme.key_windowBackgroundWhiteGrayText;
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i3), PorterDuff.Mode.MULTIPLY));
                imageView.setImageResource(R.drawable.list_circle);
                this.imageViews.add(imageView);
                TextView textView2 = new TextView(context);
                textView2.setTextSize(1, 15.0f);
                textView2.setTextColor(Theme.getColor(i3));
                textView2.setMaxWidth(AndroidUtilities.dp(260.0f));
                this.textViews.add(textView2);
                textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                if (i2 != 0) {
                    i = i2 == 2 ? R.string.NoContactsYetLine3 : i;
                    if (LocaleController.isRTL) {
                        linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 0.0f, 8.0f, 8.0f, 0.0f));
                        linearLayout2.addView(textView2, LayoutHelper.createLinear(-2, -2));
                    } else {
                        linearLayout2.addView(textView2, LayoutHelper.createLinear(-2, -2));
                        linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 8.0f, 7.0f, 0.0f, 0.0f));
                    }
                } else {
                    i = R.string.NoContactsYetLine1;
                }
                textView2.setText(LocaleController.getString(i));
                if (LocaleController.isRTL) {
                }
            }
            i2++;
        }
    }

    private void setSticker() {
        this.stickerView.setImageDrawable(new RLottieDrawable(R.raw.utyan_empty, "utyan_empty", AndroidUtilities.dp(130.0f), AndroidUtilities.dp(130.0f)));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSticker();
    }
}
