package org.telegram.ui.Components;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public final class BulletinFactory {
    private final FrameLayout containerLayout;
    private final BaseFragment fragment;
    private final Theme.ResourcesProvider resourcesProvider;

    public static BulletinFactory of(BaseFragment baseFragment) {
        return new BulletinFactory(baseFragment);
    }

    public static BulletinFactory of(FrameLayout frameLayout, Theme.ResourcesProvider resourcesProvider) {
        return new BulletinFactory(frameLayout, resourcesProvider);
    }

    public static boolean canShowBulletin(BaseFragment baseFragment) {
        return (baseFragment == null || baseFragment.getParentActivity() == null || baseFragment.getLayoutContainer() == null) ? false : true;
    }

    /* JADX WARN: Init of enum AUDIO can be incorrect */
    /* JADX WARN: Init of enum AUDIOS can be incorrect */
    /* JADX WARN: Init of enum GIF_TO_DOWNLOADS can be incorrect */
    /* JADX WARN: Init of enum MEDIA can be incorrect */
    /* JADX WARN: Init of enum PHOTO can be incorrect */
    /* JADX WARN: Init of enum PHOTOS can be incorrect */
    /* JADX WARN: Init of enum PHOTO_TO_DOWNLOADS can be incorrect */
    /* JADX WARN: Init of enum UNKNOWN can be incorrect */
    /* JADX WARN: Init of enum UNKNOWNS can be incorrect */
    /* JADX WARN: Init of enum VIDEO can be incorrect */
    /* JADX WARN: Init of enum VIDEOS can be incorrect */
    /* JADX WARN: Init of enum VIDEO_TO_DOWNLOADS can be incorrect */
    /* loaded from: classes3.dex */
    public enum FileType {
        PHOTO("PhotoSavedHint", r4, r13),
        PHOTOS("PhotosSavedHint", r13),
        VIDEO("VideoSavedHint", R.string.VideoSavedHint, r13),
        VIDEOS("VideosSavedHint", r13),
        MEDIA("MediaSavedHint", r13),
        PHOTO_TO_DOWNLOADS("PhotoSavedToDownloadsHint", r18, r13),
        VIDEO_TO_DOWNLOADS("VideoSavedToDownloadsHint", R.string.VideoSavedToDownloadsHint, r13),
        GIF("GifSavedHint", R.string.GifSavedHint, Icon.SAVED_TO_GIFS),
        GIF_TO_DOWNLOADS("GifSavedToDownloadsHint", R.string.GifSavedToDownloadsHint, r13),
        AUDIO("AudioSavedHint", r28, r9),
        AUDIOS("AudiosSavedHint", r9),
        UNKNOWN("FileSavedHint", R.string.FileSavedHint, r13),
        UNKNOWNS("FilesSavedHint", r13);
        
        private final Icon icon;
        private final String localeKey;
        private final int localeRes;
        private final boolean plural;

        static {
            int i = R.string.PhotoSavedHint;
            Icon icon = Icon.SAVED_TO_GALLERY;
            int i2 = R.string.PhotoSavedToDownloadsHint;
            Icon icon2 = Icon.SAVED_TO_DOWNLOADS;
            int i3 = R.string.AudioSavedHint;
            Icon icon3 = Icon.SAVED_TO_MUSIC;
        }

        FileType(String str, int i, Icon icon) {
            this.localeKey = str;
            this.localeRes = i;
            this.icon = icon;
            this.plural = false;
        }

        FileType(String str, Icon icon) {
            this.localeKey = str;
            this.icon = icon;
            this.localeRes = 0;
            this.plural = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getText(int i) {
            if (this.plural) {
                return LocaleController.formatPluralString(this.localeKey, i, new Object[0]);
            }
            return LocaleController.getString(this.localeKey, this.localeRes);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public enum Icon {
            SAVED_TO_DOWNLOADS(R.raw.ic_download, 2, "Box", "Arrow"),
            SAVED_TO_GALLERY(R.raw.ic_save_to_gallery, 0, "Box", "Arrow", "Mask", "Arrow 2", "Splash"),
            SAVED_TO_MUSIC(R.raw.ic_save_to_music, 2, "Box", "Arrow"),
            SAVED_TO_GIFS(R.raw.ic_save_to_gifs, 0, "gif");
            
            private final String[] layers;
            private final int paddingBottom;
            private final int resId;

            Icon(int i, int i2, String... strArr) {
                this.resId = i;
                this.paddingBottom = i2;
                this.layers = strArr;
            }
        }
    }

    private BulletinFactory(BaseFragment baseFragment) {
        this.fragment = baseFragment;
        Theme.ResourcesProvider resourcesProvider = null;
        this.containerLayout = null;
        this.resourcesProvider = baseFragment != null ? baseFragment.getResourceProvider() : resourcesProvider;
    }

    private BulletinFactory(FrameLayout frameLayout, Theme.ResourcesProvider resourcesProvider) {
        this.containerLayout = frameLayout;
        this.fragment = null;
        this.resourcesProvider = resourcesProvider;
    }

    public Bulletin createSimpleBulletin(int i, CharSequence charSequence) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.setAnimation(i, 36, 36, new String[0]);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 1500);
    }

    public Bulletin createSimpleBulletin(int i, CharSequence charSequence, CharSequence charSequence2, Runnable runnable) {
        return createSimpleBulletin(i, charSequence, charSequence2, 1500, runnable);
    }

    public Bulletin createSimpleBulletin(int i, CharSequence charSequence, CharSequence charSequence2, int i2, Runnable runnable) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.setAnimation(i, 36, 36, new String[0]);
        lottieLayout.textView.setTextSize(1, 14.0f);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(3);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.setButton(new Bulletin.UndoButton(getContext(), true, this.resourcesProvider).setText(charSequence2).setUndoAction(runnable));
        return create(lottieLayout, i2);
    }

    public Bulletin createUndoBulletin(CharSequence charSequence, Runnable runnable, Runnable runnable2) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        lottieLayout.setTimer();
        lottieLayout.setButton(new Bulletin.UndoButton(getContext(), true, this.resourcesProvider).setText(LocaleController.getString("Undo", R.string.Undo)).setUndoAction(runnable).setDelayedAction(runnable2));
        return create(lottieLayout, 5000);
    }

    public Bulletin createUsersBulletin(ArrayList<TLRPC$User> arrayList, CharSequence charSequence) {
        Bulletin.UsersLayout usersLayout = new Bulletin.UsersLayout(getContext(), this.resourcesProvider);
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size() && i < 3; i2++) {
            TLRPC$User tLRPC$User = arrayList.get(i2);
            if (tLRPC$User != null) {
                i++;
                usersLayout.avatarsImageView.setCount(i);
                usersLayout.avatarsImageView.setObject(i - 1, UserConfig.selectedAccount, tLRPC$User);
            }
        }
        usersLayout.avatarsImageView.commitTransition(false);
        usersLayout.textView.setSingleLine(true);
        usersLayout.textView.setLines(1);
        usersLayout.textView.setText(charSequence);
        usersLayout.textView.setTranslationX((-(3 - i)) * AndroidUtilities.dp(12.0f));
        return create(usersLayout, 2750);
    }

    public Bulletin createUsersAddedBulletin(ArrayList<TLRPC$User> arrayList, TLRPC$Chat tLRPC$Chat) {
        SpannableStringBuilder spannableStringBuilder;
        if (arrayList == null || arrayList.size() == 0) {
            spannableStringBuilder = null;
        } else if (arrayList.size() == 1) {
            if (ChatObject.isForum(tLRPC$Chat)) {
                int i = R.string.HasBeenAddedToForum;
                spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HasBeenAddedToForum", i, "**" + UserObject.getUserName(arrayList.get(0)) + "**"));
            } else if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                int i2 = R.string.HasBeenAddedToChannel;
                spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HasBeenAddedToChannel", i2, "**" + UserObject.getUserName(arrayList.get(0)) + "**"));
            } else {
                int i3 = R.string.HasBeenAddedToGroup;
                spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HasBeenAddedToGroup", i3, "**" + UserObject.getUserName(arrayList.get(0)) + "**"));
            }
        } else if (ChatObject.isForum(tLRPC$Chat)) {
            String formatPluralString = LocaleController.formatPluralString("Members", arrayList.size(), new Object[0]);
            int i4 = R.string.HaveBeenAddedToForum;
            spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HaveBeenAddedToForum", i4, "**" + formatPluralString + "**"));
        } else if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
            String formatPluralString2 = LocaleController.formatPluralString("Subscribers", arrayList.size(), new Object[0]);
            int i5 = R.string.HaveBeenAddedToChannel;
            spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HaveBeenAddedToChannel", i5, "**" + formatPluralString2 + "**"));
        } else {
            String formatPluralString3 = LocaleController.formatPluralString("Members", arrayList.size(), new Object[0]);
            int i6 = R.string.HaveBeenAddedToGroup;
            spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.formatString("HaveBeenAddedToGroup", i6, "**" + formatPluralString3 + "**"));
        }
        return createUsersBulletin(arrayList, spannableStringBuilder);
    }

    public Bulletin createEmojiBulletin(TLRPC$Document tLRPC$Document, CharSequence charSequence, CharSequence charSequence2, Runnable runnable) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.setAnimation(tLRPC$Document, 36, 36, new String[0]);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setTextSize(1, 14.0f);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(3);
        lottieLayout.setButton(new Bulletin.UndoButton(getContext(), true, this.resourcesProvider).setText(charSequence2).setUndoAction(runnable));
        return create(lottieLayout, 2750);
    }

    public Bulletin createDownloadBulletin(FileType fileType) {
        return createDownloadBulletin(fileType, this.resourcesProvider);
    }

    public Bulletin createDownloadBulletin(FileType fileType, Theme.ResourcesProvider resourcesProvider) {
        return createDownloadBulletin(fileType, 1, resourcesProvider);
    }

    public Bulletin createDownloadBulletin(FileType fileType, int i, Theme.ResourcesProvider resourcesProvider) {
        return createDownloadBulletin(fileType, i, 0, 0, resourcesProvider);
    }

    public Bulletin createReportSent(Theme.ResourcesProvider resourcesProvider) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(R.raw.chats_infotip, new String[0]);
        lottieLayout.textView.setText(LocaleController.getString("ReportChatSent", R.string.ReportChatSent));
        return create(lottieLayout, 1500);
    }

    public Bulletin createDownloadBulletin(FileType fileType, int i, int i2, int i3) {
        return createDownloadBulletin(fileType, i, i2, i3, null);
    }

    public Bulletin createDownloadBulletin(FileType fileType, int i, int i2, int i3, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.LottieLayout lottieLayout;
        if (i2 != 0 && i3 != 0) {
            lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider, i2, i3);
        } else {
            lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        }
        lottieLayout.setAnimation(fileType.icon.resId, fileType.icon.layers);
        lottieLayout.textView.setText(fileType.getText(i));
        if (fileType.icon.paddingBottom != 0) {
            lottieLayout.setIconPaddingBottom(fileType.icon.paddingBottom);
        }
        return create(lottieLayout, 1500);
    }

    public Bulletin createErrorBulletin(CharSequence charSequence) {
        return createErrorBulletin(charSequence, null);
    }

    public Bulletin createErrorBulletin(CharSequence charSequence, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(R.raw.chats_infotip, new String[0]);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 1500);
    }

    public Bulletin createRestrictVoiceMessagesPremiumBulletin() {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), null);
        lottieLayout.setAnimation(R.raw.voip_muted, new String[0]);
        String string = LocaleController.getString(R.string.PrivacyVoiceMessagesPremiumOnly);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        int indexOf = string.indexOf(42);
        int lastIndexOf = string.lastIndexOf(42);
        spannableStringBuilder.replace(indexOf, lastIndexOf + 1, (CharSequence) string.substring(indexOf + 1, lastIndexOf));
        spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Components.BulletinFactory.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                BulletinFactory.this.fragment.presentFragment(new PremiumPreviewFragment("settings"));
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        }, indexOf, lastIndexOf - 1, 33);
        lottieLayout.textView.setText(spannableStringBuilder);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 2750);
    }

    public Bulletin createErrorBulletinSubtitle(CharSequence charSequence, CharSequence charSequence2, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(getContext(), resourcesProvider);
        twoLineLottieLayout.setAnimation(R.raw.chats_infotip, new String[0]);
        twoLineLottieLayout.titleTextView.setText(charSequence);
        twoLineLottieLayout.subtitleTextView.setText(charSequence2);
        return create(twoLineLottieLayout, 1500);
    }

    public Bulletin createCopyLinkBulletin() {
        return createCopyLinkBulletin(false, this.resourcesProvider);
    }

    public Bulletin createCopyBulletin(String str) {
        return createCopyBulletin(str, null);
    }

    public Bulletin createCopyBulletin(String str, Theme.ResourcesProvider resourcesProvider) {
        if (!AndroidUtilities.shouldShowClipboardToast()) {
            return new Bulletin.EmptyBulletin();
        }
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), null);
        lottieLayout.setAnimation(R.raw.copy, 36, 36, "NULL ROTATION", "Back", "Front");
        lottieLayout.textView.setText(str);
        return create(lottieLayout, 1500);
    }

    public Bulletin createCopyLinkBulletin(boolean z, Theme.ResourcesProvider resourcesProvider) {
        if (!AndroidUtilities.shouldShowClipboardToast()) {
            return new Bulletin.EmptyBulletin();
        }
        if (z) {
            Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(getContext(), resourcesProvider);
            twoLineLottieLayout.setAnimation(R.raw.voip_invite, 36, 36, "Wibe", "Circle");
            twoLineLottieLayout.titleTextView.setText(LocaleController.getString("LinkCopied", R.string.LinkCopied));
            twoLineLottieLayout.subtitleTextView.setText(LocaleController.getString("LinkCopiedPrivateInfo", R.string.LinkCopiedPrivateInfo));
            return create(twoLineLottieLayout, 2750);
        }
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(R.raw.voip_invite, 36, 36, "Wibe", "Circle");
        lottieLayout.textView.setText(LocaleController.getString("LinkCopied", R.string.LinkCopied));
        return create(lottieLayout, 1500);
    }

    public Bulletin createCopyLinkBulletin(String str, Theme.ResourcesProvider resourcesProvider) {
        if (!AndroidUtilities.shouldShowClipboardToast()) {
            return new Bulletin.EmptyBulletin();
        }
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(R.raw.voip_invite, 36, 36, "Wibe", "Circle");
        lottieLayout.textView.setText(str);
        return create(lottieLayout, 1500);
    }

    private Bulletin create(Bulletin.Layout layout, int i) {
        BaseFragment baseFragment = this.fragment;
        if (baseFragment != null) {
            return Bulletin.make(baseFragment, layout, i);
        }
        return Bulletin.make(this.containerLayout, layout, i);
    }

    private Context getContext() {
        BaseFragment baseFragment = this.fragment;
        return baseFragment != null ? baseFragment.getParentActivity() : this.containerLayout.getContext();
    }

    public static Bulletin createMuteBulletin(BaseFragment baseFragment, int i) {
        return createMuteBulletin(baseFragment, i, 0, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createMuteBulletin(BaseFragment baseFragment, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        String formatString;
        boolean z;
        boolean z2;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
        if (i == 0) {
            formatString = LocaleController.formatString("NotificationsMutedForHint", R.string.NotificationsMutedForHint, LocaleController.formatPluralString("Hours", 1, new Object[0]));
        } else if (i == 1) {
            formatString = LocaleController.formatString("NotificationsMutedForHint", R.string.NotificationsMutedForHint, LocaleController.formatPluralString("Hours", 8, new Object[0]));
        } else if (i == 2) {
            formatString = LocaleController.formatString("NotificationsMutedForHint", R.string.NotificationsMutedForHint, LocaleController.formatPluralString("Days", 2, new Object[0]));
        } else if (i != 3) {
            if (i == 4) {
                formatString = LocaleController.getString("NotificationsUnmutedHint", R.string.NotificationsUnmutedHint);
                z = false;
                z2 = false;
                if (z) {
                }
                lottieLayout.textView.setText(formatString);
                return Bulletin.make(baseFragment, lottieLayout, 1500);
            } else if (i == 5) {
                formatString = LocaleController.formatString("NotificationsMutedForHint", R.string.NotificationsMutedForHint, LocaleController.formatTTLString(i2));
                z = true;
                z2 = true;
                if (z) {
                    lottieLayout.setAnimation(R.raw.mute_for, new String[0]);
                } else if (z2) {
                    lottieLayout.setAnimation(R.raw.ic_mute, "Body Main", "Body Top", "Line", "Curve Big", "Curve Small");
                } else {
                    lottieLayout.setAnimation(R.raw.ic_unmute, "BODY", "Wibe Big", "Wibe Big 3", "Wibe Small");
                }
                lottieLayout.textView.setText(formatString);
                return Bulletin.make(baseFragment, lottieLayout, 1500);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            formatString = LocaleController.getString("NotificationsMutedHint", R.string.NotificationsMutedHint);
        }
        z = false;
        z2 = true;
        if (z) {
        }
        lottieLayout.textView.setText(formatString);
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createMuteBulletin(BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
        return createMuteBulletin(baseFragment, z ? 3 : 4, 0, resourcesProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Bulletin createUnpinAllMessagesBulletin(BaseFragment baseFragment, int i, boolean z, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.LottieLayout lottieLayout;
        if (baseFragment.getParentActivity() == null) {
            if (runnable2 == null) {
                return null;
            }
            runnable2.run();
            return null;
        }
        if (z) {
            Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(baseFragment.getParentActivity(), resourcesProvider);
            twoLineLottieLayout.setAnimation(R.raw.ic_unpin, 28, 28, "Pin", "Line");
            twoLineLottieLayout.titleTextView.setText(LocaleController.getString("PinnedMessagesHidden", R.string.PinnedMessagesHidden));
            twoLineLottieLayout.subtitleTextView.setText(LocaleController.getString("PinnedMessagesHiddenInfo", R.string.PinnedMessagesHiddenInfo));
            lottieLayout = twoLineLottieLayout;
        } else {
            Bulletin.LottieLayout lottieLayout2 = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
            lottieLayout2.setAnimation(R.raw.ic_unpin, 28, 28, "Pin", "Line");
            lottieLayout2.textView.setText(LocaleController.formatPluralString("MessagesUnpinned", i, new Object[0]));
            lottieLayout = lottieLayout2;
        }
        lottieLayout.setButton(new Bulletin.UndoButton(baseFragment.getParentActivity(), true, resourcesProvider).setUndoAction(runnable).setDelayedAction(runnable2));
        return Bulletin.make(baseFragment, lottieLayout, 5000);
    }

    public static Bulletin createSaveToGalleryBulletin(BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
        return of(baseFragment).createDownloadBulletin(z ? FileType.VIDEO : FileType.PHOTO, resourcesProvider);
    }

    public static Bulletin createSaveToGalleryBulletin(FrameLayout frameLayout, boolean z, int i, int i2) {
        return of(frameLayout, null).createDownloadBulletin(z ? FileType.VIDEO : FileType.PHOTO, 1, i, i2);
    }

    public static Bulletin createSaveToGalleryBulletin(FrameLayout frameLayout, int i, boolean z, int i2, int i3) {
        return of(frameLayout, null).createDownloadBulletin(z ? i > 1 ? FileType.VIDEOS : FileType.VIDEO : i > 1 ? FileType.PHOTOS : FileType.PHOTO, i, i2, i3);
    }

    public static Bulletin createPromoteToAdminBulletin(BaseFragment baseFragment, String str) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(R.raw.ic_admin, "Shield");
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserSetAsAdminHint", R.string.UserSetAsAdminHint, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createAddedAsAdminBulletin(BaseFragment baseFragment, String str) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(R.raw.ic_admin, "Shield");
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserAddedAsAdminHint", R.string.UserAddedAsAdminHint, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createInviteSentBulletin(Context context, FrameLayout frameLayout, int i, long j, int i2, int i3, int i4) {
        SpannableStringBuilder replaceTags;
        SpannableStringBuilder spannableStringBuilder;
        int i5;
        final Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(context, null, i3, i4);
        if (i <= 1) {
            if (j == UserConfig.getInstance(UserConfig.selectedAccount).clientUserId) {
                spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("InvLinkToSavedMessages", R.string.InvLinkToSavedMessages));
                lottieLayout.setAnimation(R.raw.saved_messages, 30, 30, new String[0]);
                i5 = -1;
                lottieLayout.textView.setText(spannableStringBuilder);
                if (i5 > 0) {
                    lottieLayout.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.BulletinFactory$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            Bulletin.LottieLayout.this.performHapticFeedback(3, 2);
                        }
                    }, i5);
                }
                return Bulletin.make(frameLayout, lottieLayout, 1500);
            }
            if (DialogObject.isChatDialog(j)) {
                replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToGroup", R.string.InvLinkToGroup, MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-j)).title));
            } else {
                replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToUser", R.string.InvLinkToUser, UserObject.getFirstName(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j)))));
            }
            lottieLayout.setAnimation(R.raw.forward, 30, 30, new String[0]);
        } else {
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToChats", R.string.InvLinkToChats, LocaleController.formatPluralString("Chats", i, new Object[0])));
            lottieLayout.setAnimation(R.raw.forward, 30, 30, new String[0]);
        }
        spannableStringBuilder = replaceTags;
        i5 = 300;
        lottieLayout.textView.setText(spannableStringBuilder);
        if (i5 > 0) {
        }
        return Bulletin.make(frameLayout, lottieLayout, 1500);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createForwardedBulletin(Context context, FrameLayout frameLayout, int i, long j, int i2, int i3, int i4) {
        SpannableStringBuilder replaceTags;
        SpannableStringBuilder spannableStringBuilder;
        int i5;
        final Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(context, null, i3, i4);
        if (i <= 1) {
            if (j == UserConfig.getInstance(UserConfig.selectedAccount).clientUserId) {
                if (i2 <= 1) {
                    spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("FwdMessageToSavedMessages", R.string.FwdMessageToSavedMessages));
                } else {
                    spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("FwdMessagesToSavedMessages", R.string.FwdMessagesToSavedMessages));
                }
                lottieLayout.setAnimation(R.raw.saved_messages, 30, 30, new String[0]);
                i5 = -1;
                lottieLayout.textView.setText(spannableStringBuilder);
                if (i5 > 0) {
                    lottieLayout.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.BulletinFactory$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            Bulletin.LottieLayout.this.performHapticFeedback(3, 2);
                        }
                    }, i5);
                }
                return Bulletin.make(frameLayout, lottieLayout, 1500);
            }
            if (DialogObject.isChatDialog(j)) {
                TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-j));
                replaceTags = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToGroup", R.string.FwdMessageToGroup, chat.title)) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToGroup", R.string.FwdMessagesToGroup, chat.title));
            } else {
                TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j));
                replaceTags = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToUser", R.string.FwdMessageToUser, UserObject.getFirstName(user))) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToUser", R.string.FwdMessagesToUser, UserObject.getFirstName(user)));
            }
            lottieLayout.setAnimation(R.raw.forward, 30, 30, new String[0]);
        } else {
            replaceTags = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToChats", R.string.FwdMessageToChats, LocaleController.formatPluralString("Chats", i, new Object[0]))) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToChats", R.string.FwdMessagesToChats, LocaleController.formatPluralString("Chats", i, new Object[0])));
            lottieLayout.setAnimation(R.raw.forward, 30, 30, new String[0]);
        }
        spannableStringBuilder = replaceTags;
        i5 = 300;
        lottieLayout.textView.setText(spannableStringBuilder);
        if (i5 > 0) {
        }
        return Bulletin.make(frameLayout, lottieLayout, 1500);
    }

    public static Bulletin createRemoveFromChatBulletin(BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        String str2;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(R.raw.ic_ban, "Hand");
        if (tLRPC$User.deleted) {
            str2 = LocaleController.formatString("HiddenName", R.string.HiddenName, new Object[0]);
        } else {
            str2 = tLRPC$User.first_name;
        }
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserRemovedFromChatHint", R.string.UserRemovedFromChatHint, str2, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createBanBulletin(BaseFragment baseFragment, boolean z) {
        String string;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        if (z) {
            lottieLayout.setAnimation(R.raw.ic_ban, "Hand");
            string = LocaleController.getString("UserBlocked", R.string.UserBlocked);
        } else {
            lottieLayout.setAnimation(R.raw.ic_unban, "Main", "Finger 1", "Finger 2", "Finger 3", "Finger 4");
            string = LocaleController.getString("UserUnblocked", R.string.UserUnblocked);
        }
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(string));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createCopyLinkBulletin(BaseFragment baseFragment) {
        return of(baseFragment).createCopyLinkBulletin();
    }

    public static Bulletin createCopyLinkBulletin(FrameLayout frameLayout) {
        return of(frameLayout, null).createCopyLinkBulletin();
    }

    public static Bulletin createPinMessageBulletin(BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        return createPinMessageBulletin(baseFragment, true, null, null, resourcesProvider);
    }

    public static Bulletin createUnpinMessageBulletin(BaseFragment baseFragment, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        return createPinMessageBulletin(baseFragment, false, runnable, runnable2, resourcesProvider);
    }

    private static Bulletin createPinMessageBulletin(BaseFragment baseFragment, boolean z, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
        lottieLayout.setAnimation(z ? R.raw.ic_pin : R.raw.ic_unpin, 28, 28, "Pin", "Line");
        lottieLayout.textView.setText(LocaleController.getString(z ? "MessagePinnedHint" : "MessageUnpinnedHint", z ? R.string.MessagePinnedHint : R.string.MessageUnpinnedHint));
        if (!z) {
            lottieLayout.setButton(new Bulletin.UndoButton(baseFragment.getParentActivity(), true, resourcesProvider).setUndoAction(runnable).setDelayedAction(runnable2));
        }
        return Bulletin.make(baseFragment, lottieLayout, z ? 1500 : 5000);
    }

    public static Bulletin createSoundEnabledBulletin(BaseFragment baseFragment, int i, Theme.ResourcesProvider resourcesProvider) {
        String string;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
        boolean z = true;
        if (i == 0) {
            string = LocaleController.getString("SoundOnHint", R.string.SoundOnHint);
        } else if (i == 1) {
            string = LocaleController.getString("SoundOffHint", R.string.SoundOffHint);
            z = false;
        } else {
            throw new IllegalArgumentException();
        }
        if (z) {
            lottieLayout.setAnimation(R.raw.sound_on, new String[0]);
        } else {
            lottieLayout.setAnimation(R.raw.sound_off, new String[0]);
        }
        lottieLayout.textView.setText(string);
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }
}
