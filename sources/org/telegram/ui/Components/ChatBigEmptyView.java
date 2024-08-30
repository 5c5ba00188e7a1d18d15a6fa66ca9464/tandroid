package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ChatBigEmptyView extends LinearLayout {
    private ArrayList imageViews;
    private final Theme.ResourcesProvider resourcesProvider;
    private TextView statusTextView;
    private ArrayList textViews;

    /* JADX WARN: Removed duplicated region for block: B:11:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0206  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChatBigEmptyView(Context context, View view, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        TextView textView;
        int i2;
        int i3;
        int i4;
        float f;
        float f2;
        int i5;
        int i6;
        float f3;
        float f4;
        float f5;
        float f6;
        int i7;
        int i8;
        float f7;
        float f8;
        this.textViews = new ArrayList();
        this.imageViews = new ArrayList();
        this.resourcesProvider = resourcesProvider;
        setBackground(Theme.createServiceDrawable(AndroidUtilities.dp(18.0f), this, view, getThemedPaint("paintChatActionBackground")));
        setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        setOrientation(1);
        if (i == 0) {
            textView = new TextView(context);
        } else if (i != 1) {
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            rLottieImageView.setAutoRepeat(true);
            rLottieImageView.setAnimation(R.raw.utyan_saved_messages, 120, 120);
            rLottieImageView.playAnimation();
            addView(rLottieImageView, LayoutHelper.createLinear(-2, -2, 49, 0, 2, 0, 0));
            TextView textView2 = new TextView(context);
            if (i != 0) {
                i2 = R.string.EncryptedDescriptionTitle;
            } else if (i != 1) {
                textView2.setText(LocaleController.getString(R.string.ChatYourSelfTitle));
                textView2.setTextSize(1, 16.0f);
                textView2.setTypeface(AndroidUtilities.bold());
                textView2.setGravity(1);
                textView2.setTextColor(getThemedColor(Theme.key_chat_serviceText));
                this.textViews.add(textView2);
                textView2.setMaxWidth(AndroidUtilities.dp(260.0f));
                int i9 = 0;
                addView(textView2, LayoutHelper.createLinear(-2, -2, (i != 2 ? LocaleController.isRTL ? 5 : 3 : 1) | 48, 0, 8, 0, i != 2 ? 0 : 8));
                i3 = 0;
                while (i3 < 4) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(i9);
                    addView(linearLayout, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 0, 8, 0, 0));
                    ImageView imageView = new ImageView(context);
                    int i10 = Theme.key_chat_serviceText;
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i10), PorterDuff.Mode.MULTIPLY));
                    imageView.setImageResource(i == 0 ? R.drawable.ic_lock_white : i == 2 ? R.drawable.list_circle : R.drawable.groups_overview_check);
                    this.imageViews.add(imageView);
                    TextView textView3 = new TextView(context);
                    textView3.setTextSize(1, 15.0f);
                    textView3.setTextColor(getThemedColor(i10));
                    this.textViews.add(textView3);
                    textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                    textView3.setMaxWidth(AndroidUtilities.dp(260.0f));
                    if (i3 == 0) {
                        i4 = i == 0 ? R.string.EncryptedDescription1 : i == 2 ? R.string.ChatYourSelfDescription1 : R.string.GroupDescription1;
                    } else if (i3 == 1) {
                        i4 = i == 0 ? R.string.EncryptedDescription2 : i == 2 ? R.string.ChatYourSelfDescription2 : R.string.GroupDescription2;
                    } else if (i3 != 2) {
                        if (i3 == 3) {
                            i4 = i == 0 ? R.string.EncryptedDescription4 : i == 2 ? R.string.ChatYourSelfDescription4 : R.string.GroupDescription4;
                        }
                        if (LocaleController.isRTL) {
                            if (i != 0 && i == 2) {
                                f = 8.0f;
                                f2 = 0.0f;
                                i5 = -2;
                                i6 = -2;
                                f3 = 0.0f;
                                f4 = 8.0f;
                            } else {
                                f = 8.0f;
                                f2 = 0.0f;
                                i5 = -2;
                                i6 = -2;
                                f3 = 0.0f;
                                f4 = 4.0f;
                            }
                            linearLayout.addView(imageView, LayoutHelper.createLinear(i5, i6, f3, f4, f, f2));
                            linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2));
                        } else {
                            linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2));
                            if (i != 0 && i == 2) {
                                f5 = 0.0f;
                                f6 = 0.0f;
                                i7 = -2;
                                i8 = -2;
                                f7 = 8.0f;
                                f8 = 7.0f;
                            } else {
                                f5 = 0.0f;
                                f6 = 0.0f;
                                i7 = -2;
                                i8 = -2;
                                f7 = 8.0f;
                                f8 = 3.0f;
                            }
                            linearLayout.addView(imageView, LayoutHelper.createLinear(i7, i8, f7, f8, f5, f6));
                        }
                        i3++;
                        i9 = 0;
                    } else {
                        i4 = i == 0 ? R.string.EncryptedDescription3 : i == 2 ? R.string.ChatYourSelfDescription3 : R.string.GroupDescription3;
                    }
                    textView3.setText(LocaleController.getString(i4));
                    if (LocaleController.isRTL) {
                    }
                    i3++;
                    i9 = 0;
                }
            } else {
                i2 = R.string.GroupEmptyTitle2;
            }
            textView2.setText(LocaleController.getString(i2));
            textView2.setTextSize(1, 15.0f);
            textView2.setTextColor(getThemedColor(Theme.key_chat_serviceText));
            this.textViews.add(textView2);
            textView2.setMaxWidth(AndroidUtilities.dp(260.0f));
            int i92 = 0;
            addView(textView2, LayoutHelper.createLinear(-2, -2, (i != 2 ? LocaleController.isRTL ? 5 : 3 : 1) | 48, 0, 8, 0, i != 2 ? 0 : 8));
            i3 = 0;
            while (i3 < 4) {
            }
        } else {
            textView = new TextView(context);
        }
        this.statusTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.statusTextView.setTextColor(getThemedColor(Theme.key_chat_serviceText));
        this.statusTextView.setGravity(1);
        this.statusTextView.setMaxWidth(AndroidUtilities.dp(210.0f));
        this.textViews.add(this.statusTextView);
        addView(this.statusTextView, LayoutHelper.createLinear(-2, -2, 49));
        TextView textView22 = new TextView(context);
        if (i != 0) {
        }
        textView22.setText(LocaleController.getString(i2));
        textView22.setTextSize(1, 15.0f);
        textView22.setTextColor(getThemedColor(Theme.key_chat_serviceText));
        this.textViews.add(textView22);
        textView22.setMaxWidth(AndroidUtilities.dp(260.0f));
        int i922 = 0;
        addView(textView22, LayoutHelper.createLinear(-2, -2, (i != 2 ? LocaleController.isRTL ? 5 : 3 : 1) | 48, 0, 8, 0, i != 2 ? 0 : 8));
        i3 = 0;
        while (i3 < 4) {
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void setStatusText(CharSequence charSequence) {
        this.statusTextView.setText(charSequence);
    }

    public void setTextColor(int i) {
        for (int i2 = 0; i2 < this.textViews.size(); i2++) {
            ((TextView) this.textViews.get(i2)).setTextColor(i);
        }
        for (int i3 = 0; i3 < this.imageViews.size(); i3++) {
            ((ImageView) this.imageViews.get(i3)).setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
        }
    }
}
