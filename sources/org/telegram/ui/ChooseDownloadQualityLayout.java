package org.telegram.ui;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.PopupSwipeBackLayout;
import org.telegram.ui.Components.VideoPlayer;

/* loaded from: classes4.dex */
public class ChooseDownloadQualityLayout {
    public final LinearLayout buttonsLayout;
    private final Callback callback;
    public final ActionBarPopupWindow.ActionBarPopupWindowLayout layout;

    public interface Callback {
        void onQualitySelected(MessageObject messageObject, VideoPlayer.Quality quality);
    }

    public ChooseDownloadQualityLayout(Context context, final PopupSwipeBackLayout popupSwipeBackLayout, Callback callback) {
        this.callback = callback;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, 0, null);
        this.layout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_arrow_back, LocaleController.getString(R.string.Back), false, null);
        addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChooseDownloadQualityLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PopupSwipeBackLayout.this.closeForeground();
            }
        });
        addItem.setColors(-328966, -328966);
        addItem.setSelectorColor(268435455);
        View view = new FrameLayout(context) { // from class: org.telegram.ui.ChooseDownloadQualityLayout.1
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
            }
        };
        view.setMinimumWidth(AndroidUtilities.dp(196.0f));
        view.setBackgroundColor(-15198184);
        actionBarPopupWindowLayout.addView(view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(8.0f);
        view.setLayoutParams(layoutParams);
        LinearLayout linearLayout = new LinearLayout(context);
        this.buttonsLayout = linearLayout;
        linearLayout.setOrientation(1);
        actionBarPopupWindowLayout.addView(linearLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(MessageObject messageObject, VideoPlayer.Quality quality, View view) {
        this.callback.onQualitySelected(messageObject, quality);
    }

    public boolean update(final MessageObject messageObject) {
        TLRPC.Message message;
        CharSequence formatFileSize;
        if (messageObject == null || (message = messageObject.messageOwner) == null || message.media == null || !messageObject.hasVideoQualities()) {
            return false;
        }
        int i = messageObject.currentAccount;
        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
        ArrayList qualities = VideoPlayer.getQualities(i, messageMedia.document, messageMedia.alt_documents, 0, false);
        this.buttonsLayout.removeAllViews();
        for (int i2 = 0; i2 < qualities.size(); i2++) {
            final VideoPlayer.Quality quality = (VideoPlayer.Quality) qualities.get(i2);
            VideoPlayer.VideoUri downloadUri = quality.getDownloadUri();
            StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.formatString(R.string.QualitySaveIn, Integer.valueOf(quality.p())));
            sb.append(quality.original ? " (" + LocaleController.getString(R.string.QualitySource) + ")" : "");
            String sb2 = sb.toString();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            if (downloadUri.isCached()) {
                spannableStringBuilder.append(AndroidUtilities.formatFileSize(downloadUri.document.size));
                formatFileSize = LocaleController.getString(R.string.QualityCached);
            } else {
                SpannableString spannableString = new SpannableString("s ");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_mini_arrow_mediabold);
                coloredImageSpan.rotate(90.0f);
                coloredImageSpan.translate(0.0f, AndroidUtilities.dp(1.0f));
                coloredImageSpan.spaceScaleX = 0.85f;
                spannableString.setSpan(coloredImageSpan, 0, 1, 33);
                spannableStringBuilder.append((CharSequence) spannableString);
                formatFileSize = AndroidUtilities.formatFileSize(downloadUri.document.size);
            }
            spannableStringBuilder.append(formatFileSize);
            ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(this.buttonsLayout, 0, sb2, false, null);
            addItem.setSubtext(spannableStringBuilder);
            addItem.setColors(-328966, -328966);
            addItem.subtextView.setPadding(0, 0, 0, 0);
            addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChooseDownloadQualityLayout$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChooseDownloadQualityLayout.this.lambda$update$1(messageObject, quality, view);
                }
            });
            addItem.setSelectorColor(268435455);
        }
        return true;
    }
}
