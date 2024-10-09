package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;

/* loaded from: classes3.dex */
public class JoinGroupAlert extends BottomSheet {
    private TLRPC.ChatInvite chatInvite;
    private TLRPC.Chat currentChat;
    private final BaseFragment fragment;
    private final String hash;
    private RadialProgressView requestProgressView;
    private TextView requestTextView;

    public JoinGroupAlert(Context context, TLObject tLObject, String str, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        this(context, tLObject, str, baseFragment, resourcesProvider, -1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0213  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x047d  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0480  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x020e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public JoinGroupAlert(Context context, TLObject tLObject, String str, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, final int i) {
        super(context, false, resourcesProvider);
        String str2;
        boolean z;
        boolean z2;
        boolean z3;
        int i2;
        Drawable scamDrawable;
        final boolean z4;
        boolean z5;
        TLRPC.ChatInvite chatInvite;
        boolean z6;
        CharSequence format;
        setApplyBottomPadding(false);
        setApplyTopPadding(false);
        fixNavigationBar(getThemedColor(Theme.key_windowBackgroundWhite));
        this.fragment = baseFragment;
        if (tLObject instanceof TLRPC.ChatInvite) {
            this.chatInvite = (TLRPC.ChatInvite) tLObject;
        } else if (tLObject instanceof TLRPC.Chat) {
            this.currentChat = (TLRPC.Chat) tLObject;
        }
        this.hash = str;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setClickable(true);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(linearLayout);
        NestedScrollView nestedScrollView = new NestedScrollView(context);
        nestedScrollView.addView(frameLayout);
        setCustomView(nestedScrollView);
        ImageView imageView = new ImageView(context);
        imageView.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        imageView.setColorFilter(getThemedColor(Theme.key_sheet_other));
        imageView.setImageResource(R.drawable.ic_layer_close);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                JoinGroupAlert.this.lambda$new$0(view);
            }
        });
        int dp = AndroidUtilities.dp(8.0f);
        imageView.setPadding(dp, dp, dp, dp);
        frameLayout.addView(imageView, LayoutHelper.createFrame(36, 36.0f, 8388661, 6.0f, 8.0f, 8.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(45.0f));
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(90, 90, 49, 0, 27, 0, 0));
        TLRPC.ChatInvite chatInvite2 = this.chatInvite;
        if (chatInvite2 != null) {
            if (chatInvite2.chat != null) {
                AvatarDrawable avatarDrawable = new AvatarDrawable(this.chatInvite.chat);
                TLRPC.ChatInvite chatInvite3 = this.chatInvite;
                TLRPC.Chat chat = chatInvite3.chat;
                String str3 = chat.title;
                i2 = chat.participants_count;
                backupImageView.setForUserOrChat(chat, avatarDrawable, chatInvite3);
                r12 = str3;
            } else {
                AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                avatarDrawable2.setInfo(0L, this.chatInvite.title, null);
                TLRPC.ChatInvite chatInvite4 = this.chatInvite;
                String str4 = chatInvite4.title;
                i2 = chatInvite4.participants_count;
                backupImageView.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(chatInvite4.photo.sizes, 50), this.chatInvite.photo), "50_50", avatarDrawable2, this.chatInvite);
                r12 = str4;
            }
            TLRPC.ChatInvite chatInvite5 = this.chatInvite;
            str2 = chatInvite5.about;
            z2 = chatInvite5.verified;
            z3 = chatInvite5.fake;
            z = chatInvite5.scam;
        } else if (this.currentChat != null) {
            AvatarDrawable avatarDrawable3 = new AvatarDrawable(this.currentChat);
            String str5 = this.currentChat.title;
            TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.currentChat.id);
            r12 = chatFull != null ? chatFull.about : null;
            i2 = Math.max(this.currentChat.participants_count, chatFull != null ? chatFull.participants_count : 0);
            TLRPC.Chat chat2 = this.currentChat;
            backupImageView.setForUserOrChat(chat2, avatarDrawable3, chat2);
            TLRPC.Chat chat3 = this.currentChat;
            boolean z7 = chat3.verified;
            z3 = chat3.fake;
            z2 = z7;
            str2 = r12;
            r12 = str5;
            z = chat3.scam;
        } else {
            str2 = null;
            z = 0;
            z2 = false;
            z3 = false;
            i2 = 0;
        }
        SimpleTextView simpleTextView = new SimpleTextView(context);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setTextSize(20);
        int i3 = Theme.key_dialogTextBlack;
        simpleTextView.setTextColor(getThemedColor(i3));
        simpleTextView.setText(r12);
        simpleTextView.setGravity(17);
        linearLayout.addView(simpleTextView, LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, i2 > 0 ? 0 : 20));
        if (z == 0 && !z3) {
            scamDrawable = z2 ? getVerifiedCrossfadeDrawable() : scamDrawable;
            TLRPC.ChatInvite chatInvite6 = this.chatInvite;
            z4 = (chatInvite6 == null && ((chatInvite6.channel && !chatInvite6.megagroup) || ChatObject.isChannelAndNotMegaGroup(chatInvite6.chat))) || (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup);
            z5 = !TextUtils.isEmpty(str2);
            TextView textView = new TextView(context);
            textView.setTextSize(1, 13.0f);
            int i4 = Theme.key_dialogTextGray3;
            textView.setTextColor(getThemedColor(i4));
            textView.setSingleLine(true);
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView.setEllipsize(truncateAt);
            textView.setText(LocaleController.getString((this.chatInvite == null && i == 0) ? R.string.ChannelPublic : !z4 ? R.string.ChannelPrivate : R.string.MegaPrivate).toLowerCase());
            linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 49, 10, 0, 10, !z5 ? 0 : 20));
            if (z5) {
                TextView textView2 = new TextView(context);
                textView2.setGravity(17);
                textView2.setText(str2);
                textView2.setTextColor(getThemedColor(i3));
                textView2.setTextSize(1, 15.0f);
                linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 48, 24, 10, 24, 20));
            }
            chatInvite = this.chatInvite;
            if (chatInvite != null || chatInvite.request_needed) {
                FrameLayout frameLayout2 = new FrameLayout(getContext());
                linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2));
                RadialProgressView radialProgressView = new RadialProgressView(getContext(), resourcesProvider);
                this.requestProgressView = radialProgressView;
                int i5 = Theme.key_featuredStickers_addButton;
                radialProgressView.setProgressColor(getThemedColor(i5));
                this.requestProgressView.setSize(AndroidUtilities.dp(32.0f));
                this.requestProgressView.setVisibility(4);
                frameLayout2.addView(this.requestProgressView, LayoutHelper.createFrame(48, 48, 17));
                TextView textView3 = new TextView(getContext());
                this.requestTextView = textView3;
                textView3.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), getThemedColor(i5), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
                this.requestTextView.setEllipsize(truncateAt);
                this.requestTextView.setGravity(17);
                this.requestTextView.setSingleLine(true);
                this.requestTextView.setText(LocaleController.getString(!z4 ? R.string.RequestToJoinChannel : R.string.RequestToJoinGroup));
                this.requestTextView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
                this.requestTextView.setTextSize(1, 14.0f);
                this.requestTextView.setTypeface(AndroidUtilities.bold());
                this.requestTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        JoinGroupAlert.this.lambda$new$7(z4, view);
                    }
                });
                frameLayout2.addView(this.requestTextView, LayoutHelper.createLinear(-1, 48, 8388611, 14, 0, 14, 0));
                TextView textView4 = new TextView(getContext());
                textView4.setGravity(17);
                textView4.setTextSize(1, 14.0f);
                textView4.setText(LocaleController.getString(!z4 ? R.string.RequestToJoinChannelDescription : R.string.RequestToJoinGroupDescription));
                textView4.setTextColor(getThemedColor(i4));
                linearLayout.addView(textView4, LayoutHelper.createLinear(-1, -2, 48, 24, 17, 24, 15));
            }
            if (chatInvite.participants.isEmpty()) {
                z6 = false;
            } else {
                int min = Math.min(this.chatInvite.participants.size(), 3);
                AvatarsImageView avatarsImageView = new AvatarsImageView(context, false);
                avatarsImageView.setAvatarsTextSize(AndroidUtilities.dp(20.0f));
                float f = 38;
                avatarsImageView.setSize(AndroidUtilities.dp(f));
                avatarsImageView.setCount(min);
                avatarsImageView.setStepFactor(0.65f);
                for (int i6 = 0; i6 < min; i6++) {
                    avatarsImageView.setObject(i6, UserConfig.selectedAccount, this.chatInvite.participants.get(i6));
                }
                avatarsImageView.commitTransition(false);
                linearLayout.addView(avatarsImageView, LayoutHelper.createLinear((int) (f + ((min - 1) * ((0.65f * f) + 1.0f))), 44, 17, 0, 2, 0, 4));
                TextView textView5 = new TextView(context);
                textView5.setTextSize(1, 13.0f);
                textView5.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
                textView5.setGravity(17);
                if (min == 1) {
                    z6 = false;
                    format = ellipsize(textView5, this.chatInvite, 0).toString();
                } else {
                    z6 = false;
                    if (min == 2) {
                        format = LocaleController.formatString("RequestToJoinMembersTwo", R.string.RequestToJoinMembersTwo, ellipsize(textView5, this.chatInvite, 0), ellipsize(textView5, this.chatInvite, 1));
                    } else if (i2 == 3) {
                        format = LocaleController.formatString("RequestToJoinMembersThree", R.string.RequestToJoinMembersThree, ellipsize(textView5, this.chatInvite, 0), ellipsize(textView5, this.chatInvite, 1), ellipsize(textView5, this.chatInvite, 2));
                    } else {
                        int max = Math.max(i2 - min, 2);
                        format = String.format(LocaleController.getPluralString("RequestToJoinMembersAll", max), ellipsize(textView5, this.chatInvite, 0), ellipsize(textView5, this.chatInvite, 1), Integer.valueOf(max));
                    }
                }
                textView5.setText(format);
                linearLayout.addView(textView5, LayoutHelper.createLinear(-2, -2, 49, 10, 0, 10, 24));
            }
            TLRPC.ChatInvite chatInvite7 = this.chatInvite;
            if ((chatInvite7.channel && !chatInvite7.megagroup) || (ChatObject.isChannel(chatInvite7.chat) && !this.chatInvite.chat.megagroup)) {
                z6 = true;
            }
            TextView textView6 = new TextView(getContext());
            textView6.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), getThemedColor(Theme.key_featuredStickers_addButton), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
            textView6.setEllipsize(TextUtils.TruncateAt.END);
            textView6.setGravity(17);
            textView6.setSingleLine(true);
            textView6.setText(LocaleController.getString(z6 ? R.string.ProfileJoinChannel : R.string.ProfileJoinGroup));
            textView6.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
            textView6.setTextSize(1, 14.0f);
            textView6.setTypeface(AndroidUtilities.bold());
            linearLayout.addView(textView6, LayoutHelper.createLinear(-1, 48, 8388611, 14, 0, 14, 14));
            textView6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    JoinGroupAlert.this.lambda$new$10(i, view);
                }
            });
            return;
        }
        scamDrawable = getScamDrawable(!z);
        simpleTextView.setRightDrawable(scamDrawable);
        TLRPC.ChatInvite chatInvite62 = this.chatInvite;
        if (chatInvite62 == null) {
        }
        z5 = !TextUtils.isEmpty(str2);
        TextView textView7 = new TextView(context);
        textView7.setTextSize(1, 13.0f);
        int i42 = Theme.key_dialogTextGray3;
        textView7.setTextColor(getThemedColor(i42));
        textView7.setSingleLine(true);
        TextUtils.TruncateAt truncateAt2 = TextUtils.TruncateAt.END;
        textView7.setEllipsize(truncateAt2);
        textView7.setText(LocaleController.getString((this.chatInvite == null && i == 0) ? R.string.ChannelPublic : !z4 ? R.string.ChannelPrivate : R.string.MegaPrivate).toLowerCase());
        linearLayout.addView(textView7, LayoutHelper.createLinear(-2, -2, 49, 10, 0, 10, !z5 ? 0 : 20));
        if (z5) {
        }
        chatInvite = this.chatInvite;
        if (chatInvite != null) {
        }
        FrameLayout frameLayout22 = new FrameLayout(getContext());
        linearLayout.addView(frameLayout22, LayoutHelper.createLinear(-1, -2));
        RadialProgressView radialProgressView2 = new RadialProgressView(getContext(), resourcesProvider);
        this.requestProgressView = radialProgressView2;
        int i52 = Theme.key_featuredStickers_addButton;
        radialProgressView2.setProgressColor(getThemedColor(i52));
        this.requestProgressView.setSize(AndroidUtilities.dp(32.0f));
        this.requestProgressView.setVisibility(4);
        frameLayout22.addView(this.requestProgressView, LayoutHelper.createFrame(48, 48, 17));
        TextView textView32 = new TextView(getContext());
        this.requestTextView = textView32;
        textView32.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), getThemedColor(i52), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
        this.requestTextView.setEllipsize(truncateAt2);
        this.requestTextView.setGravity(17);
        this.requestTextView.setSingleLine(true);
        this.requestTextView.setText(LocaleController.getString(!z4 ? R.string.RequestToJoinChannel : R.string.RequestToJoinGroup));
        this.requestTextView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
        this.requestTextView.setTextSize(1, 14.0f);
        this.requestTextView.setTypeface(AndroidUtilities.bold());
        this.requestTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                JoinGroupAlert.this.lambda$new$7(z4, view);
            }
        });
        frameLayout22.addView(this.requestTextView, LayoutHelper.createLinear(-1, 48, 8388611, 14, 0, 14, 0));
        TextView textView42 = new TextView(getContext());
        textView42.setGravity(17);
        textView42.setTextSize(1, 14.0f);
        textView42.setText(LocaleController.getString(!z4 ? R.string.RequestToJoinChannelDescription : R.string.RequestToJoinGroupDescription));
        textView42.setTextColor(getThemedColor(i42));
        linearLayout.addView(textView42, LayoutHelper.createLinear(-1, -2, 48, 24, 17, 24, 15));
    }

    private CharSequence ellipsize(TextView textView, TLRPC.ChatInvite chatInvite, int i) {
        String str = chatInvite.participants.get(i).first_name;
        if (str == null) {
            str = "";
        }
        return TextUtils.ellipsize(str.trim(), textView.getPaint(), AndroidUtilities.dp(120.0f), TextUtils.TruncateAt.END);
    }

    private Drawable getScamDrawable(int i) {
        return i == 0 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable;
    }

    private Drawable getVerifiedCrossfadeDrawable() {
        return new CombinedDrawable(Theme.dialogs_verifiedDrawable, Theme.dialogs_verifiedCheckDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (isDismissed()) {
            return;
        }
        this.requestTextView.setVisibility(4);
        this.requestProgressView.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(final int i, View view) {
        dismiss();
        final TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite = new TLRPC.TL_messages_importChatInvite();
        tL_messages_importChatInvite.hash = this.hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                JoinGroupAlert.this.lambda$new$9(i, tL_messages_importChatInvite, tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(boolean z, DialogInterface dialogInterface) {
        showBulletin(getContext(), this.fragment, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3(final boolean z, TLRPC.TL_error tL_error) {
        if (tL_error != null && "INVITE_REQUEST_SENT".equals(tL_error.text)) {
            setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda9
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    JoinGroupAlert.this.lambda$new$2(z, dialogInterface);
                }
            });
        }
        dismiss();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(boolean z, DialogInterface dialogInterface) {
        showBulletin(getContext(), this.fragment, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(TLRPC.TL_error tL_error, final boolean z, TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite) {
        BaseFragment baseFragment = this.fragment;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (tL_error != null) {
            if ("INVITE_REQUEST_SENT".equals(tL_error.text)) {
                setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda11
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        JoinGroupAlert.this.lambda$new$4(z, dialogInterface);
                    }
                });
            } else {
                AlertsCreator.processError(this.currentAccount, tL_error, this.fragment, tL_messages_importChatInvite, new Object[0]);
            }
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(final boolean z, final TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite, TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                JoinGroupAlert.this.lambda$new$5(tL_error, z, tL_messages_importChatInvite);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(final boolean z, View view) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                JoinGroupAlert.this.lambda$new$1();
            }
        }, 400L);
        if (this.chatInvite == null && this.currentChat != null) {
            MessagesController.getInstance(this.currentAccount).addUserToChat(this.currentChat.id, UserConfig.getInstance(this.currentAccount).getCurrentUser(), 0, null, null, true, new Runnable() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    JoinGroupAlert.this.dismiss();
                }
            }, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC.TL_error tL_error) {
                    boolean lambda$new$3;
                    lambda$new$3 = JoinGroupAlert.this.lambda$new$3(z, tL_error);
                    return lambda$new$3;
                }
            });
            return;
        }
        final TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite = new TLRPC.TL_messages_importChatInvite();
        tL_messages_importChatInvite.hash = this.hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                JoinGroupAlert.this.lambda$new$6(z, tL_messages_importChatInvite, tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(TLRPC.TL_error tL_error, TLObject tLObject, int i, TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite) {
        TLRPC.ChatInvite chatInvite;
        TLRPC.Chat chat;
        long j;
        BaseFragment baseFragment = this.fragment;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (tL_error == null) {
            TLRPC.Updates updates = (TLRPC.Updates) tLObject;
            if (updates.chats.isEmpty()) {
                return;
            }
            TLRPC.Chat chat2 = updates.chats.get(0);
            chat2.left = false;
            chat2.kicked = false;
            MessagesController.getInstance(this.currentAccount).putUsers(updates.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(updates.chats, false);
            j = chat2.id;
        } else {
            if (!"USER_ALREADY_PARTICIPANT".equals(tL_error.text) || i != 0 || (chatInvite = this.chatInvite) == null || (chat = chatInvite.chat) == null) {
                AlertsCreator.processError(this.currentAccount, tL_error, this.fragment, tL_messages_importChatInvite, new Object[0]);
                return;
            }
            j = chat.id;
        }
        openChat(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(final int i, final TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.JoinGroupAlert$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                JoinGroupAlert.this.lambda$new$8(tL_error, tLObject, i, tL_messages_importChatInvite);
            }
        });
    }

    private void openChat(long j) {
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", j);
        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, this.fragment)) {
            ChatActivity chatActivity = new ChatActivity(bundle);
            BaseFragment baseFragment = this.fragment;
            baseFragment.presentFragment(chatActivity, baseFragment instanceof ChatActivity);
        }
    }

    public static void showBulletin(Context context, BaseFragment baseFragment, boolean z) {
        if (context == null) {
            if (baseFragment != null) {
                baseFragment.getContext();
            }
        } else {
            Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(context, baseFragment.getResourceProvider());
            twoLineLottieLayout.imageView.setAnimation(R.raw.timer_3, 28, 28);
            twoLineLottieLayout.titleTextView.setText(LocaleController.getString(R.string.RequestToJoinSent));
            twoLineLottieLayout.subtitleTextView.setText(LocaleController.getString(z ? R.string.RequestToJoinChannelSentDescription : R.string.RequestToJoinGroupSentDescription));
            Bulletin.make(baseFragment, twoLineLottieLayout, 2750).show();
        }
    }
}
