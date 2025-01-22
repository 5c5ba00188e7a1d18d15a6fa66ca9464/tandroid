package org.telegram.ui.Components;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public abstract class JoinCallByUrlAlert extends BottomSheet {
    private boolean joinAfterDismiss;

    public static class BottomSheetCell extends FrameLayout {
        private View background;
        private TextView textView;

        public BottomSheetCell(Context context) {
            super(context);
            View view = new View(context);
            this.background = view;
            view.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 4.0f));
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(17);
            this.textView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.bold());
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public JoinCallByUrlAlert(Context context, TLRPC.Chat chat) {
        super(context, true);
        String str;
        setApplyBottomPadding(false);
        setApplyTopPadding(false);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        setCustomView(linearLayout);
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(45.0f));
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(90, 90, 49, 0, 29, 0, 0));
        backupImageView.setForUserOrChat(chat, new AvatarDrawable(chat));
        TextView textView = new TextView(context);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setGravity(1);
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 49, 17, 24, 17, 0));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray3));
        textView2.setGravity(1);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 49, 30, 8, 30, 0));
        ChatObject.Call groupCall = AccountInstance.getInstance(this.currentAccount).getMessagesController().getGroupCall(chat.id, false);
        if (groupCall != null) {
            textView.setText(TextUtils.isEmpty(groupCall.call.title) ? chat.title : groupCall.call.title);
            int i = groupCall.call.participants_count;
            if (i != 0) {
                str = LocaleController.formatPluralString("Participants", i, new Object[0]);
                textView2.setText(str);
                BottomSheetCell bottomSheetCell = new BottomSheetCell(context);
                bottomSheetCell.setBackground(null);
                bottomSheetCell.setText(LocaleController.getString(!ChatObject.isChannelOrGiga(chat) ? R.string.VoipChannelJoinVoiceChatUrl : R.string.VoipGroupJoinVoiceChatUrl));
                bottomSheetCell.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinCallByUrlAlert$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        JoinCallByUrlAlert.this.lambda$new$0(view);
                    }
                });
                linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50, 51, 0, 30, 0, 0));
            }
        } else {
            textView.setText(chat.title);
        }
        str = LocaleController.getString(R.string.NoOneJoinedYet);
        textView2.setText(str);
        BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context);
        bottomSheetCell2.setBackground(null);
        bottomSheetCell2.setText(LocaleController.getString(!ChatObject.isChannelOrGiga(chat) ? R.string.VoipChannelJoinVoiceChatUrl : R.string.VoipGroupJoinVoiceChatUrl));
        bottomSheetCell2.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinCallByUrlAlert$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                JoinCallByUrlAlert.this.lambda$new$0(view);
            }
        });
        linearLayout.addView(bottomSheetCell2, LayoutHelper.createLinear(-1, 50, 51, 0, 30, 0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        this.joinAfterDismiss = true;
        lambda$new$0();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        if (this.joinAfterDismiss) {
            onJoin();
        }
    }

    protected abstract void onJoin();
}
