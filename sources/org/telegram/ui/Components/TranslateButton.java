package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.TranslateController;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.RestrictedLanguagesSelectActivity;
/* loaded from: classes3.dex */
public class TranslateButton extends FrameLayout {
    private final int currentAccount;
    private final long dialogId;
    private final BaseFragment fragment;
    private ImageView menuView;
    private Theme.ResourcesProvider resourcesProvider;
    private AnimatedTextView textView;
    public final SpannableString translateIcon;

    protected void onButtonClick() {
        throw null;
    }

    public TranslateButton(Context context, ChatActivity chatActivity, Theme.ResourcesProvider resourcesProvider) {
        this(context, chatActivity.getCurrentAccount(), chatActivity.getDialogId(), chatActivity, resourcesProvider);
    }

    public TranslateButton(Context context, int i, long j, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = i;
        this.dialogId = j;
        this.fragment = baseFragment;
        this.resourcesProvider = resourcesProvider;
        AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, false);
        this.textView = animatedTextView;
        animatedTextView.setAnimationProperties(0.3f, 0L, 450L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.textView.setTextColor(Theme.getColor("chat_addContact", resourcesProvider));
        this.textView.setTextSize(AndroidUtilities.dp(15.0f));
        this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.textView.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        this.textView.setGravity(1);
        this.textView.setIgnoreRTL(!LocaleController.isRTL);
        AnimatedTextView animatedTextView2 = this.textView;
        animatedTextView2.adaptWidth = false;
        animatedTextView2.setBackground(Theme.createSelectorDrawable(Theme.getColor("chat_addContact", resourcesProvider) & 436207615, 3));
        this.textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranslateButton.this.lambda$new$0(view);
            }
        });
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f));
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.msg_translate).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_addContact", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        mutate.setBounds(0, AndroidUtilities.dp(-8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(12.0f));
        SpannableString spannableString = new SpannableString("x");
        this.translateIcon = spannableString;
        spannableString.setSpan(new ImageSpan(mutate, 0), 0, 1, 33);
        ImageView imageView = new ImageView(context);
        this.menuView = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        this.menuView.setImageResource(R.drawable.msg_mini_customize);
        this.menuView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_addContact", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        this.menuView.setBackground(Theme.createSelectorDrawable(Theme.getColor("chat_addContact", resourcesProvider) & 436207615, 7));
        this.menuView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranslateButton.this.lambda$new$1(view);
            }
        });
        addView(this.menuView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 8.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        onButtonClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        onMenuClick();
    }

    protected void onMenuClick() {
        final TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getContext(), R.drawable.popup_fixed_alert2, this.resourcesProvider, 1);
        final ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        actionBarPopupWindowLayout.setBackgroundColor(Theme.getColor("actionBarDefaultSubmenuBackground", this.resourcesProvider));
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout linearLayout2 = new LinearLayout(getContext());
        scrollView.addView(linearLayout2);
        linearLayout2.setOrientation(1);
        actionBarPopupWindowLayout.swipeBackGravityRight = true;
        final int addViewToSwipeBack = actionBarPopupWindowLayout.addViewToSwipeBack(linearLayout);
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, false, this.resourcesProvider);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("TranslateTo", R.string.TranslateTo), R.drawable.msg_translate);
        actionBarMenuSubItem.setSubtext(TranslateAlert2.languageName(translateController.getDialogTranslateTo(this.dialogId)));
        actionBarMenuSubItem.setItemHeight(56);
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranslateButton.lambda$onMenuClick$2(ActionBarPopupWindow.ActionBarPopupWindowLayout.this, addViewToSwipeBack, view);
            }
        });
        actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), true, false, this.resourcesProvider);
        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("Back", R.string.Back), R.drawable.ic_ab_back);
        actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranslateButton.lambda$onMenuClick$3(ActionBarPopupWindow.ActionBarPopupWindowLayout.this, view);
            }
        });
        linearLayout.addView(actionBarMenuSubItem2);
        linearLayout.addView(new ActionBarPopupWindow.GapView(getContext(), this.resourcesProvider), LayoutHelper.createLinear(-1, 8));
        linearLayout.addView(scrollView, LayoutHelper.createLinear(-1, 420));
        final String dialogDetectedLanguage = translateController.getDialogDetectedLanguage(this.dialogId);
        final String languageName = TranslateAlert2.languageName(dialogDetectedLanguage);
        String dialogTranslateTo = translateController.getDialogTranslateTo(this.dialogId);
        ArrayList<LocaleController.LocaleInfo> locales = TranslateController.getLocales();
        int i = 0;
        while (i < locales.size()) {
            final LocaleController.LocaleInfo localeInfo = locales.get(i);
            if (!TextUtils.equals(dialogDetectedLanguage, localeInfo.pluralLangCode) && !"en_raw".equals(localeInfo.shortName) && "remote".equals(localeInfo.pathToFile)) {
                ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getContext(), 2, i == 0, i == locales.size() - 1, this.resourcesProvider);
                actionBarMenuSubItem3.setChecked(dialogTranslateTo != null && dialogTranslateTo.equals(localeInfo.pluralLangCode));
                actionBarMenuSubItem3.setText(TranslateAlert2.languageName(localeInfo.pluralLangCode));
                actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        TranslateButton.this.lambda$onMenuClick$4(translateController, localeInfo, actionBarPopupWindow, view);
                    }
                });
                linearLayout2.addView(actionBarMenuSubItem3);
            }
            i++;
        }
        actionBarPopupWindowLayout.addView(new ActionBarPopupWindow.GapView(getContext(), this.resourcesProvider), LayoutHelper.createLinear(-1, 8));
        if (languageName != null) {
            ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(getContext(), true, false, this.resourcesProvider);
            actionBarMenuSubItem4.setTextAndIcon(LocaleController.formatString("DoNotTranslateLanguage", R.string.DoNotTranslateLanguage, languageName), R.drawable.msg_block2);
            actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TranslateButton.this.lambda$onMenuClick$6(dialogDetectedLanguage, translateController, languageName, actionBarPopupWindow, view);
                }
            });
            actionBarPopupWindowLayout.addView(actionBarMenuSubItem4);
        }
        ActionBarMenuSubItem actionBarMenuSubItem5 = new ActionBarMenuSubItem(getContext(), true, false, this.resourcesProvider);
        actionBarMenuSubItem5.setTextAndIcon(LocaleController.getString("Hide", R.string.Hide), R.drawable.msg_cancel);
        actionBarMenuSubItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TranslateButton.this.lambda$onMenuClick$8(translateController, actionBarPopupWindow, view);
            }
        });
        actionBarPopupWindowLayout.addView(actionBarMenuSubItem5);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(220);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.setSoftInputMode(0);
        ImageView imageView = this.menuView;
        actionBarPopupWindow.showAsDropDown(imageView, 0, (-imageView.getMeasuredHeight()) - AndroidUtilities.dp(8.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onMenuClick$2(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i, View view) {
        actionBarPopupWindowLayout.getSwipeBack().openForeground(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onMenuClick$3(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, View view) {
        actionBarPopupWindowLayout.getSwipeBack().closeForeground();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuClick$4(TranslateController translateController, LocaleController.LocaleInfo localeInfo, ActionBarPopupWindow actionBarPopupWindow, View view) {
        translateController.setDialogTranslateTo(this.dialogId, localeInfo.pluralLangCode);
        actionBarPopupWindow.dismiss();
        updateText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuClick$6(String str, TranslateController translateController, String str2, ActionBarPopupWindow actionBarPopupWindow, View view) {
        RestrictedLanguagesSelectActivity.toggleLanguage(str, true);
        translateController.checkRestrictedLanguagesUpdate();
        translateController.setHideTranslateDialog(this.dialogId, true);
        BulletinFactory.of(this.fragment).createSimpleBulletin(R.raw.msg_translate, AndroidUtilities.replaceTags(LocaleController.formatString("AddedToDoNotTranslate", R.string.AddedToDoNotTranslate, str2)), LocaleController.getString("Settings", R.string.Settings), new Runnable() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                TranslateButton.this.lambda$onMenuClick$5();
            }
        }).show();
        actionBarPopupWindow.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuClick$5() {
        this.fragment.presentFragment(new RestrictedLanguagesSelectActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuClick$8(final TranslateController translateController, ActionBarPopupWindow actionBarPopupWindow, View view) {
        String string;
        boolean z = true;
        translateController.setHideTranslateDialog(this.dialogId, true);
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
        if ((chat == null || !ChatObject.isChannelAndNotMegaGroup(chat)) ? false : false) {
            string = LocaleController.getString("TranslationBarHiddenForChannel", R.string.TranslationBarHiddenForChannel);
        } else if (chat != null) {
            string = LocaleController.getString("TranslationBarHiddenForGroup", R.string.TranslationBarHiddenForGroup);
        } else {
            string = LocaleController.getString("TranslationBarHiddenForChat", R.string.TranslationBarHiddenForChat);
        }
        BulletinFactory.of(this.fragment).createSimpleBulletin(R.raw.msg_translate, AndroidUtilities.replaceTags(string), LocaleController.getString("Undo", R.string.Undo), new Runnable() { // from class: org.telegram.ui.Components.TranslateButton$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                TranslateButton.this.lambda$onMenuClick$7(translateController);
            }
        }).show();
        actionBarPopupWindow.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuClick$7(TranslateController translateController) {
        translateController.setHideTranslateDialog(this.dialogId, false);
    }

    public void updateText() {
        String formatString;
        TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        AnimatedTextView animatedTextView = this.textView;
        CharSequence[] charSequenceArr = new CharSequence[3];
        charSequenceArr[0] = this.translateIcon;
        charSequenceArr[1] = " ";
        if (translateController.isTranslatingDialog(this.dialogId)) {
            formatString = LocaleController.getString("ShowOriginalButton", R.string.ShowOriginalButton);
        } else {
            formatString = LocaleController.formatString("TranslateToButton", R.string.TranslateToButton, TranslateAlert2.languageName(translateController.getDialogTranslateTo(this.dialogId)));
        }
        charSequenceArr[2] = formatString;
        animatedTextView.setText(TextUtils.concat(charSequenceArr));
    }
}
