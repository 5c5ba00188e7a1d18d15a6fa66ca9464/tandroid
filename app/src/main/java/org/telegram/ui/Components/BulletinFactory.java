package org.telegram.ui.Components;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
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
        PHOTO("PhotoSavedHint", 2131627557, r7),
        PHOTOS("PhotosSavedHint", r7),
        VIDEO("VideoSavedHint", 2131628964, r7),
        VIDEOS("VideosSavedHint", r7),
        MEDIA("MediaSavedHint", r7),
        PHOTO_TO_DOWNLOADS("PhotoSavedToDownloadsHint", 2131627558, r5),
        VIDEO_TO_DOWNLOADS("VideoSavedToDownloadsHint", 2131628965, r5),
        GIF("GifSavedHint", 2131626100, Icon.SAVED_TO_GIFS),
        GIF_TO_DOWNLOADS("GifSavedToDownloadsHint", 2131626101, r5),
        AUDIO("AudioSavedHint", 2131624522, r11),
        AUDIOS("AudiosSavedHint", r11),
        UNKNOWN("FileSavedHint", 2131625847, r5),
        UNKNOWNS("FilesSavedHint", r5);
        
        private final Icon icon;
        private final String localeKey;
        private final int localeRes;
        private final boolean plural;

        static {
            Icon icon = Icon.SAVED_TO_GALLERY;
            Icon icon2 = Icon.SAVED_TO_DOWNLOADS;
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

        public String getText(int i) {
            if (this.plural) {
                return LocaleController.formatPluralString(this.localeKey, i, new Object[0]);
            }
            return LocaleController.getString(this.localeKey, this.localeRes);
        }

        /* loaded from: classes3.dex */
        public enum Icon {
            SAVED_TO_DOWNLOADS(2131558465, 2, "Box", "Arrow"),
            SAVED_TO_GALLERY(2131558468, 0, "Box", "Arrow", "Mask", "Arrow 2", "Splash"),
            SAVED_TO_MUSIC(2131558470, 2, "Box", "Arrow"),
            SAVED_TO_GIFS(2131558469, 0, "gif");
            
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

    public Bulletin createSimpleBulletin(int i, String str) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.setAnimation(i, 36, 36, new String[0]);
        lottieLayout.textView.setText(str);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 1500);
    }

    public Bulletin createEmojiBulletin(TLRPC$Document tLRPC$Document, CharSequence charSequence, CharSequence charSequence2, Runnable runnable) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), this.resourcesProvider);
        lottieLayout.setAnimation(tLRPC$Document, 36, 36, new String[0]);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setTextSize(1, 14.0f);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
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
        lottieLayout.setAnimation(2131558424, new String[0]);
        lottieLayout.textView.setText(LocaleController.getString("ReportChatSent", 2131628000));
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
        lottieLayout.setAnimation(2131558424, new String[0]);
        lottieLayout.textView.setText(charSequence);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 1500);
    }

    public Bulletin createRestrictVoiceMessagesPremiumBulletin() {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), null);
        lottieLayout.setAnimation(2131558614, new String[0]);
        String string = LocaleController.getString(2131627798);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        int indexOf = string.indexOf(42);
        int lastIndexOf = string.lastIndexOf(42);
        spannableStringBuilder.replace(indexOf, lastIndexOf + 1, (CharSequence) string.substring(indexOf + 1, lastIndexOf));
        spannableStringBuilder.setSpan(new AnonymousClass1(), indexOf - 1, lastIndexOf - 1, 33);
        lottieLayout.textView.setText(spannableStringBuilder);
        lottieLayout.textView.setSingleLine(false);
        lottieLayout.textView.setMaxLines(2);
        return create(lottieLayout, 2750);
    }

    /* renamed from: org.telegram.ui.Components.BulletinFactory$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ClickableSpan {
        AnonymousClass1() {
            BulletinFactory.this = r1;
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            BulletinFactory.this.fragment.presentFragment(new PremiumPreviewFragment("settings"));
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    public Bulletin createErrorBulletinSubtitle(CharSequence charSequence, CharSequence charSequence2, Theme.ResourcesProvider resourcesProvider) {
        Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(getContext(), resourcesProvider);
        twoLineLottieLayout.setAnimation(2131558424, new String[0]);
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
        lottieLayout.setAnimation(2131558433, 36, 36, "NULL ROTATION", "Back", "Front");
        lottieLayout.textView.setText(str);
        return create(lottieLayout, 1500);
    }

    public Bulletin createCopyLinkBulletin(boolean z, Theme.ResourcesProvider resourcesProvider) {
        if (!AndroidUtilities.shouldShowClipboardToast()) {
            return new Bulletin.EmptyBulletin();
        }
        if (z) {
            Bulletin.TwoLineLottieLayout twoLineLottieLayout = new Bulletin.TwoLineLottieLayout(getContext(), resourcesProvider);
            twoLineLottieLayout.setAnimation(2131558613, 36, 36, "Wibe", "Circle");
            twoLineLottieLayout.titleTextView.setText(LocaleController.getString("LinkCopied", 2131626480));
            twoLineLottieLayout.subtitleTextView.setText(LocaleController.getString("LinkCopiedPrivateInfo", 2131626482));
            return create(twoLineLottieLayout, 2750);
        }
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(2131558613, 36, 36, "Wibe", "Circle");
        lottieLayout.textView.setText(LocaleController.getString("LinkCopied", 2131626480));
        return create(lottieLayout, 1500);
    }

    public Bulletin createCopyLinkBulletin(String str, Theme.ResourcesProvider resourcesProvider) {
        if (!AndroidUtilities.shouldShowClipboardToast()) {
            return new Bulletin.EmptyBulletin();
        }
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(getContext(), resourcesProvider);
        lottieLayout.setAnimation(2131558613, 36, 36, "Wibe", "Circle");
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

    /* JADX WARN: Removed duplicated region for block: B:20:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0089  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createMuteBulletin(BaseFragment baseFragment, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        boolean z;
        boolean z2;
        String str;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
        if (i == 0) {
            str = LocaleController.formatString("NotificationsMutedForHint", 2131627098, LocaleController.formatPluralString("Hours", 1, new Object[0]));
        } else if (i == 1) {
            str = LocaleController.formatString("NotificationsMutedForHint", 2131627098, LocaleController.formatPluralString("Hours", 8, new Object[0]));
        } else if (i == 2) {
            str = LocaleController.formatString("NotificationsMutedForHint", 2131627098, LocaleController.formatPluralString("Days", 2, new Object[0]));
        } else if (i != 3) {
            if (i == 4) {
                str = LocaleController.getString("NotificationsUnmutedHint", 2131627123);
                z2 = false;
                z = false;
                if (z2) {
                }
                lottieLayout.textView.setText(str);
                return Bulletin.make(baseFragment, lottieLayout, 1500);
            } else if (i != 5) {
                throw new IllegalArgumentException();
            } else {
                str = LocaleController.formatString("NotificationsMutedForHint", 2131627098, LocaleController.formatTTLString(i2));
                z2 = true;
                z = true;
                if (z2) {
                    lottieLayout.setAnimation(2131558498, new String[0]);
                } else if (z) {
                    lottieLayout.setAnimation(2131558466, "Body Main", "Body Top", "Line", "Curve Big", "Curve Small");
                } else {
                    lottieLayout.setAnimation(2131558472, "BODY", "Wibe Big", "Wibe Big 3", "Wibe Small");
                }
                lottieLayout.textView.setText(str);
                return Bulletin.make(baseFragment, lottieLayout, 1500);
            }
        } else {
            str = LocaleController.getString("NotificationsMutedHint", 2131627099);
        }
        z2 = false;
        z = true;
        if (z2) {
        }
        lottieLayout.textView.setText(str);
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
            twoLineLottieLayout.setAnimation(2131558473, 28, 28, "Pin", "Line");
            twoLineLottieLayout.titleTextView.setText(LocaleController.getString("PinnedMessagesHidden", 2131627607));
            twoLineLottieLayout.subtitleTextView.setText(LocaleController.getString("PinnedMessagesHiddenInfo", 2131627608));
            lottieLayout = twoLineLottieLayout;
        } else {
            Bulletin.LottieLayout lottieLayout2 = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
            lottieLayout2.setAnimation(2131558473, 28, 28, "Pin", "Line");
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

    public static Bulletin createPromoteToAdminBulletin(BaseFragment baseFragment, String str) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(2131558462, "Shield");
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserSetAsAdminHint", 2131628913, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createAddedAsAdminBulletin(BaseFragment baseFragment, String str) {
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(2131558462, "Shield");
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserAddedAsAdminHint", 2131628873, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00ae  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createInviteSentBulletin(Context context, FrameLayout frameLayout, int i, long j, int i2, int i3, int i4) {
        int i5;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(context, null, i3, i4);
        if (i <= 1) {
            if (j == UserConfig.getInstance(UserConfig.selectedAccount).clientUserId) {
                spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("InvLinkToSavedMessages", 2131626291));
                lottieLayout.setAnimation(2131558542, 30, 30, new String[0]);
                i5 = -1;
                lottieLayout.textView.setText(spannableStringBuilder);
                if (i5 > 0) {
                    lottieLayout.postDelayed(new BulletinFactory$$ExternalSyntheticLambda1(lottieLayout), i5);
                }
                return Bulletin.make(frameLayout, lottieLayout, 1500);
            }
            spannableStringBuilder2 = DialogObject.isChatDialog(j) ? AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToGroup", 2131626290, MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-j)).title)) : AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToUser", 2131626292, UserObject.getFirstName(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j)))));
            lottieLayout.setAnimation(2131558454, 30, 30, new String[0]);
        } else {
            spannableStringBuilder2 = AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToChats", 2131626289, LocaleController.formatPluralString("Chats", i, new Object[0])));
            lottieLayout.setAnimation(2131558454, 30, 30, new String[0]);
        }
        spannableStringBuilder = spannableStringBuilder2;
        i5 = 300;
        lottieLayout.textView.setText(spannableStringBuilder);
        if (i5 > 0) {
        }
        return Bulletin.make(frameLayout, lottieLayout, 1500);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0107  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bulletin createForwardedBulletin(Context context, FrameLayout frameLayout, int i, long j, int i2, int i3, int i4) {
        int i5;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(context, null, i3, i4);
        if (i <= 1) {
            if (j == UserConfig.getInstance(UserConfig.selectedAccount).clientUserId) {
                if (i2 <= 1) {
                    spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("FwdMessageToSavedMessages", 2131626080));
                } else {
                    spannableStringBuilder = AndroidUtilities.replaceTags(LocaleController.getString("FwdMessagesToSavedMessages", 2131626084));
                }
                lottieLayout.setAnimation(2131558542, 30, 30, new String[0]);
                i5 = -1;
                lottieLayout.textView.setText(spannableStringBuilder);
                if (i5 > 0) {
                    lottieLayout.postDelayed(new BulletinFactory$$ExternalSyntheticLambda0(lottieLayout), i5);
                }
                return Bulletin.make(frameLayout, lottieLayout, 1500);
            }
            if (DialogObject.isChatDialog(j)) {
                TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-j));
                spannableStringBuilder2 = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToGroup", 2131626079, chat.title)) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToGroup", 2131626083, chat.title));
            } else {
                TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j));
                spannableStringBuilder2 = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToUser", 2131626081, UserObject.getFirstName(user))) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToUser", 2131626085, UserObject.getFirstName(user)));
            }
            lottieLayout.setAnimation(2131558454, 30, 30, new String[0]);
        } else {
            spannableStringBuilder2 = i2 <= 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToChats", 2131626078, LocaleController.formatPluralString("Chats", i, new Object[0]))) : AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToChats", 2131626082, LocaleController.formatPluralString("Chats", i, new Object[0])));
            lottieLayout.setAnimation(2131558454, 30, 30, new String[0]);
        }
        spannableStringBuilder = spannableStringBuilder2;
        i5 = 300;
        lottieLayout.textView.setText(spannableStringBuilder);
        if (i5 > 0) {
        }
        return Bulletin.make(frameLayout, lottieLayout, 1500);
    }

    public static Bulletin createRemoveFromChatBulletin(BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        String str2;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        lottieLayout.setAnimation(2131558463, "Hand");
        if (tLRPC$User.deleted) {
            str2 = LocaleController.formatString("HiddenName", 2131626178, new Object[0]);
        } else {
            str2 = tLRPC$User.first_name;
        }
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("UserRemovedFromChatHint", 2131628881, str2, str)));
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }

    public static Bulletin createBanBulletin(BaseFragment baseFragment, boolean z) {
        String str;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        if (z) {
            lottieLayout.setAnimation(2131558463, "Hand");
            str = LocaleController.getString("UserBlocked", 2131628878);
        } else {
            lottieLayout.setAnimation(2131558471, "Main", "Finger 1", "Finger 2", "Finger 3", "Finger 4");
            str = LocaleController.getString("UserUnblocked", 2131628914);
        }
        lottieLayout.textView.setText(AndroidUtilities.replaceTags(str));
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
        lottieLayout.setAnimation(z ? 2131558467 : 2131558473, 28, 28, "Pin", "Line");
        lottieLayout.textView.setText(LocaleController.getString(z ? "MessagePinnedHint" : "MessageUnpinnedHint", z ? 2131626685 : 2131626710));
        if (!z) {
            lottieLayout.setButton(new Bulletin.UndoButton(baseFragment.getParentActivity(), true, resourcesProvider).setUndoAction(runnable).setDelayedAction(runnable2));
        }
        return Bulletin.make(baseFragment, lottieLayout, z ? 1500 : 5000);
    }

    public static Bulletin createSoundEnabledBulletin(BaseFragment baseFragment, int i, Theme.ResourcesProvider resourcesProvider) {
        String str;
        Bulletin.LottieLayout lottieLayout = new Bulletin.LottieLayout(baseFragment.getParentActivity(), resourcesProvider);
        boolean z = true;
        if (i == 0) {
            str = LocaleController.getString("SoundOnHint", 2131628456);
        } else if (i == 1) {
            str = LocaleController.getString("SoundOffHint", 2131628454);
            z = false;
        } else {
            throw new IllegalArgumentException();
        }
        if (z) {
            lottieLayout.setAnimation(2131558553, new String[0]);
        } else {
            lottieLayout.setAnimation(2131558552, new String[0]);
        }
        lottieLayout.textView.setText(str);
        return Bulletin.make(baseFragment, lottieLayout, 1500);
    }
}
