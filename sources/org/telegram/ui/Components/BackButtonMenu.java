package org.telegram.ui.Components;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BackButtonMenu;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.TopicsFragment;
/* loaded from: classes3.dex */
public class BackButtonMenu {

    /* loaded from: classes3.dex */
    public static class PulledDialog<T> {
        Class<T> activity;
        TLRPC$Chat chat;
        long dialogId;
        int filterId;
        int folderId;
        int stackIndex;
        TLRPC$TL_forumTopic topic;
        TLRPC$User user;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:74:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x027a A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v4, types: [android.graphics.drawable.BitmapDrawable] */
    /* JADX WARN: Type inference failed for: r1v13, types: [android.widget.FrameLayout, android.view.View] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ActionBarPopupWindow show(final BaseFragment baseFragment, View view, long j, long j2, Theme.ResourcesProvider resourcesProvider) {
        ArrayList<PulledDialog> stackedHistoryDialogs;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout;
        long j3;
        PulledDialog pulledDialog;
        int i;
        boolean z;
        Drawable drawable;
        String str;
        ?? r12;
        if (baseFragment == null) {
            return null;
        }
        final INavigationLayout parentLayout = baseFragment.getParentLayout();
        Activity parentActivity = baseFragment.getParentActivity();
        View fragmentView = baseFragment.getFragmentView();
        if (parentLayout == null || parentActivity == null || fragmentView == null) {
            return null;
        }
        if (j2 != 0) {
            stackedHistoryDialogs = getStackedHistoryForTopic(baseFragment, j, j2);
        } else {
            stackedHistoryDialogs = getStackedHistoryDialogs(baseFragment, j);
        }
        ArrayList<PulledDialog> arrayList = stackedHistoryDialogs;
        if (arrayList.size() <= 0) {
            return null;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(parentActivity, resourcesProvider);
        android.graphics.Rect rect = new android.graphics.Rect();
        baseFragment.getParentActivity().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate().getPadding(rect);
        actionBarPopupWindowLayout2.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider));
        AtomicReference atomicReference = new AtomicReference();
        int i2 = 0;
        while (i2 < arrayList.size()) {
            PulledDialog pulledDialog2 = arrayList.get(i2);
            TLRPC$Chat tLRPC$Chat = pulledDialog2.chat;
            TLRPC$User tLRPC$User = pulledDialog2.user;
            final TLRPC$TL_forumTopic tLRPC$TL_forumTopic = pulledDialog2.topic;
            ?? frameLayout = new FrameLayout(parentActivity);
            frameLayout.setMinimumWidth(AndroidUtilities.dp(200.0f));
            BackupImageView backupImageView = new BackupImageView(parentActivity);
            int i3 = i2;
            if (tLRPC$Chat == null && tLRPC$User == null) {
                backupImageView.setRoundRadius(0);
            } else {
                backupImageView.setRoundRadius((tLRPC$Chat == null || !tLRPC$Chat.forum) ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f));
            }
            frameLayout.addView(backupImageView, LayoutHelper.createFrameRelatively(32.0f, 32.0f, 8388627, 13.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(parentActivity);
            textView.setLines(1);
            ArrayList<PulledDialog> arrayList2 = arrayList;
            textView.setTextSize(1, 16.0f);
            textView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem, resourcesProvider));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            frameLayout.addView(textView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388627, 59.0f, 0.0f, 12.0f, 0.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setScaleSize(0.8f);
            if (tLRPC$TL_forumTopic != null) {
                if (tLRPC$TL_forumTopic.id == 1) {
                    backupImageView.setImageDrawable(ForumUtilities.createGeneralTopicDrawable(fragmentView.getContext(), 1.0f, Theme.getColor(Theme.key_chat_inMenu, resourcesProvider), false));
                    actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                    j3 = 0;
                } else {
                    j3 = 0;
                    if (tLRPC$TL_forumTopic.icon_emoji_id != 0) {
                        actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                        backupImageView.setAnimatedEmojiDrawable(new AnimatedEmojiDrawable(10, baseFragment.getCurrentAccount(), tLRPC$TL_forumTopic.icon_emoji_id));
                    } else {
                        actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                        backupImageView.setImageDrawable(ForumUtilities.createTopicDrawable(tLRPC$TL_forumTopic, false));
                    }
                }
                textView.setText(tLRPC$TL_forumTopic.title);
            } else {
                actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                j3 = 0;
                if (tLRPC$Chat != null) {
                    avatarDrawable.setInfo(baseFragment.getCurrentAccount(), tLRPC$Chat);
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
                    if (tLRPC$ChatPhoto != null && (r12 = tLRPC$ChatPhoto.strippedBitmap) != 0) {
                        avatarDrawable = r12;
                    }
                    backupImageView.setImage(ImageLocation.getForChat(tLRPC$Chat, 1), "50_50", avatarDrawable, tLRPC$Chat);
                    textView.setText(tLRPC$Chat.title);
                } else if (tLRPC$User != null) {
                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
                    if (tLRPC$UserProfilePhoto == null || (drawable = tLRPC$UserProfilePhoto.strippedBitmap) == null) {
                        drawable = avatarDrawable;
                    }
                    pulledDialog = pulledDialog2;
                    if (pulledDialog2.activity == ChatActivity.class && UserObject.isUserSelf(tLRPC$User)) {
                        str = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                        avatarDrawable.setAvatarType(1);
                        backupImageView.setImageDrawable(avatarDrawable);
                    } else if (UserObject.isReplyUser(tLRPC$User)) {
                        str = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                        avatarDrawable.setAvatarType(12);
                        backupImageView.setImageDrawable(avatarDrawable);
                    } else {
                        if (UserObject.isDeleted(tLRPC$User)) {
                            str = LocaleController.getString("HiddenName", R.string.HiddenName);
                            avatarDrawable.setInfo(baseFragment.getCurrentAccount(), tLRPC$User);
                            i = 1;
                            backupImageView.setImage(ImageLocation.getForUser(tLRPC$User, 1), "50_50", avatarDrawable, tLRPC$User);
                        } else {
                            i = 1;
                            String userName = UserObject.getUserName(tLRPC$User);
                            avatarDrawable.setInfo(baseFragment.getCurrentAccount(), tLRPC$User);
                            backupImageView.setImage(ImageLocation.getForUser(tLRPC$User, 1), "50_50", drawable, tLRPC$User);
                            str = userName;
                        }
                        textView.setText(str);
                        z = false;
                        frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
                        final PulledDialog pulledDialog3 = pulledDialog;
                        final AtomicReference atomicReference2 = atomicReference;
                        AtomicReference atomicReference3 = atomicReference;
                        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                BackButtonMenu.lambda$show$0(atomicReference2, pulledDialog3, parentLayout, tLRPC$TL_forumTopic, baseFragment, view2);
                            }
                        });
                        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout3 = actionBarPopupWindowLayout;
                        actionBarPopupWindowLayout3.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
                        if (!z) {
                            FrameLayout frameLayout2 = new FrameLayout(parentActivity);
                            frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuSeparator, resourcesProvider));
                            frameLayout2.setTag(R.id.fit_width_tag, Integer.valueOf(i));
                            actionBarPopupWindowLayout3.addView((View) frameLayout2, LayoutHelper.createLinear(-1, 8));
                        }
                        i2 = i3 + 1;
                        actionBarPopupWindowLayout2 = actionBarPopupWindowLayout3;
                        atomicReference = atomicReference3;
                        arrayList = arrayList2;
                    }
                    i = 1;
                    textView.setText(str);
                    z = false;
                    frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
                    final PulledDialog pulledDialog32 = pulledDialog;
                    final AtomicReference atomicReference22 = atomicReference;
                    AtomicReference atomicReference32 = atomicReference;
                    frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            BackButtonMenu.lambda$show$0(atomicReference22, pulledDialog32, parentLayout, tLRPC$TL_forumTopic, baseFragment, view2);
                        }
                    });
                    ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout32 = actionBarPopupWindowLayout;
                    actionBarPopupWindowLayout32.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
                    if (!z) {
                    }
                    i2 = i3 + 1;
                    actionBarPopupWindowLayout2 = actionBarPopupWindowLayout32;
                    atomicReference = atomicReference32;
                    arrayList = arrayList2;
                } else {
                    pulledDialog = pulledDialog2;
                    i = 1;
                    backupImageView.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.msg_viewchats).mutate());
                    backupImageView.setSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                    backupImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon, resourcesProvider), PorterDuff.Mode.MULTIPLY));
                    textView.setText(LocaleController.getString("AllChats", R.string.AllChats));
                    z = true;
                    frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
                    final PulledDialog pulledDialog322 = pulledDialog;
                    final AtomicReference atomicReference222 = atomicReference;
                    AtomicReference atomicReference322 = atomicReference;
                    frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            BackButtonMenu.lambda$show$0(atomicReference222, pulledDialog322, parentLayout, tLRPC$TL_forumTopic, baseFragment, view2);
                        }
                    });
                    ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout322 = actionBarPopupWindowLayout;
                    actionBarPopupWindowLayout322.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
                    if (!z) {
                    }
                    i2 = i3 + 1;
                    actionBarPopupWindowLayout2 = actionBarPopupWindowLayout322;
                    atomicReference = atomicReference322;
                    arrayList = arrayList2;
                }
            }
            pulledDialog = pulledDialog2;
            i = 1;
            z = false;
            frameLayout.setBackground(Theme.getSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), false));
            final PulledDialog pulledDialog3222 = pulledDialog;
            final AtomicReference atomicReference2222 = atomicReference;
            AtomicReference atomicReference3222 = atomicReference;
            frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    BackButtonMenu.lambda$show$0(atomicReference2222, pulledDialog3222, parentLayout, tLRPC$TL_forumTopic, baseFragment, view2);
                }
            });
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout3222 = actionBarPopupWindowLayout;
            actionBarPopupWindowLayout3222.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
            if (!z) {
            }
            i2 = i3 + 1;
            actionBarPopupWindowLayout2 = actionBarPopupWindowLayout3222;
            atomicReference = atomicReference3222;
            arrayList = arrayList2;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout4 = actionBarPopupWindowLayout2;
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout4, -2, -2);
        atomicReference.set(actionBarPopupWindow);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(220);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayout4.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.setSoftInputMode(0);
        actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
        actionBarPopupWindowLayout4.setFitItems(true);
        int dp = AndroidUtilities.dp(8.0f) - rect.left;
        if (AndroidUtilities.isTablet()) {
            int[] iArr = new int[2];
            fragmentView.getLocationInWindow(iArr);
            dp += iArr[0];
        }
        actionBarPopupWindow.showAtLocation(fragmentView, 51, dp, (view.getBottom() - rect.top) - AndroidUtilities.dp(8.0f));
        return actionBarPopupWindow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ae A[LOOP:1: B:35:0x00aa->B:37:0x00ae, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ba A[EDGE_INSN: B:45:0x00ba->B:38:0x00ba ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$show$0(AtomicReference atomicReference, PulledDialog pulledDialog, INavigationLayout iNavigationLayout, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, BaseFragment baseFragment, View view) {
        Long l;
        ArrayList arrayList;
        int size;
        int i;
        Long l2 = null;
        if (atomicReference.get() != null) {
            ((ActionBarPopupWindow) atomicReference.getAndSet(null)).dismiss();
        }
        if (pulledDialog.stackIndex >= 0) {
            if (iNavigationLayout != null && iNavigationLayout.getFragmentStack() != null && pulledDialog.stackIndex < iNavigationLayout.getFragmentStack().size()) {
                BaseFragment baseFragment2 = iNavigationLayout.getFragmentStack().get(pulledDialog.stackIndex);
                if (baseFragment2 instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment2;
                    l2 = Long.valueOf(chatActivity.getDialogId());
                    l = Long.valueOf(chatActivity.getTopicId());
                } else if (baseFragment2 instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment2;
                    l2 = Long.valueOf(profileActivity.getDialogId());
                    l = Long.valueOf(profileActivity.getTopicId());
                }
                if ((l2 == null && l2.longValue() != pulledDialog.dialogId) || !(tLRPC$TL_forumTopic == null || l == null || tLRPC$TL_forumTopic.id == l.longValue())) {
                    for (int size2 = iNavigationLayout.getFragmentStack().size() - 2; size2 > pulledDialog.stackIndex; size2--) {
                        iNavigationLayout.removeFragmentFromStack(size2);
                    }
                } else if (iNavigationLayout != null && iNavigationLayout.getFragmentStack() != null) {
                    arrayList = new ArrayList(iNavigationLayout.getFragmentStack());
                    size = arrayList.size() - 2;
                    while (true) {
                        i = pulledDialog.stackIndex;
                        if (size > i) {
                            break;
                        }
                        ((BaseFragment) arrayList.get(size)).removeSelfFromStack();
                        size--;
                    }
                    if (i < iNavigationLayout.getFragmentStack().size()) {
                        iNavigationLayout.closeLastFragment(true);
                        return;
                    }
                }
            }
            l = null;
            if (l2 == null) {
            }
            if (iNavigationLayout != null) {
                arrayList = new ArrayList(iNavigationLayout.getFragmentStack());
                size = arrayList.size() - 2;
                while (true) {
                    i = pulledDialog.stackIndex;
                    if (size > i) {
                    }
                    ((BaseFragment) arrayList.get(size)).removeSelfFromStack();
                    size--;
                }
                if (i < iNavigationLayout.getFragmentStack().size()) {
                }
            }
        }
        goToPulledDialog(baseFragment, pulledDialog);
    }

    private static ArrayList<PulledDialog> getStackedHistoryForTopic(BaseFragment baseFragment, long j, long j2) {
        INavigationLayout parentLayout;
        int i;
        ArrayList<PulledDialog> arrayList = new ArrayList<>();
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return arrayList;
        }
        List<PulledDialog> pulledDialogs = parentLayout.getPulledDialogs();
        if (pulledDialogs != null) {
            i = -1;
            for (int i2 = 0; i2 < pulledDialogs.size(); i2++) {
                PulledDialog pulledDialog = pulledDialogs.get(i2);
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = pulledDialog.topic;
                if (tLRPC$TL_forumTopic != null && tLRPC$TL_forumTopic.id != j2) {
                    int i3 = pulledDialog.stackIndex;
                    if (i3 >= i) {
                        i = i3;
                    }
                    arrayList.add(pulledDialog);
                }
            }
        } else {
            i = -1;
        }
        if (parentLayout.getFragmentStack().size() > 1 && (parentLayout.getFragmentStack().get(parentLayout.getFragmentStack().size() - 2) instanceof TopicsFragment)) {
            PulledDialog pulledDialog2 = new PulledDialog();
            arrayList.add(pulledDialog2);
            pulledDialog2.stackIndex = i + 1;
            pulledDialog2.activity = DialogsActivity.class;
            PulledDialog pulledDialog3 = new PulledDialog();
            arrayList.add(pulledDialog3);
            pulledDialog3.stackIndex = -1;
            pulledDialog3.activity = TopicsFragment.class;
            pulledDialog3.chat = MessagesController.getInstance(baseFragment.getCurrentAccount()).getChat(Long.valueOf(-j));
        } else {
            PulledDialog pulledDialog4 = new PulledDialog();
            arrayList.add(pulledDialog4);
            pulledDialog4.stackIndex = -1;
            pulledDialog4.activity = TopicsFragment.class;
            pulledDialog4.chat = MessagesController.getInstance(baseFragment.getCurrentAccount()).getChat(Long.valueOf(-j));
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getStackedHistoryForTopic$1;
                lambda$getStackedHistoryForTopic$1 = BackButtonMenu.lambda$getStackedHistoryForTopic$1((BackButtonMenu.PulledDialog) obj, (BackButtonMenu.PulledDialog) obj2);
                return lambda$getStackedHistoryForTopic$1;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getStackedHistoryForTopic$1(PulledDialog pulledDialog, PulledDialog pulledDialog2) {
        return pulledDialog2.stackIndex - pulledDialog.stackIndex;
    }

    public static void goToPulledDialog(BaseFragment baseFragment, PulledDialog pulledDialog) {
        if (pulledDialog == null) {
            return;
        }
        GenericDeclaration genericDeclaration = pulledDialog.activity;
        if (genericDeclaration == ChatActivity.class) {
            Bundle bundle = new Bundle();
            TLRPC$Chat tLRPC$Chat = pulledDialog.chat;
            if (tLRPC$Chat != null) {
                bundle.putLong("chat_id", tLRPC$Chat.id);
            } else {
                TLRPC$User tLRPC$User = pulledDialog.user;
                if (tLRPC$User != null) {
                    bundle.putLong("user_id", tLRPC$User.id);
                }
            }
            bundle.putInt("dialog_folder_id", pulledDialog.folderId);
            bundle.putInt("dialog_filter_id", pulledDialog.filterId);
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic = pulledDialog.topic;
            if (tLRPC$TL_forumTopic != null) {
                baseFragment.presentFragment(ForumUtilities.getChatActivityForTopic(baseFragment, pulledDialog.chat.id, tLRPC$TL_forumTopic, 0, bundle), true);
            } else {
                baseFragment.presentFragment(new ChatActivity(bundle), true);
            }
        } else if (genericDeclaration == ProfileActivity.class) {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("dialog_id", pulledDialog.dialogId);
            baseFragment.presentFragment(new ProfileActivity(bundle2), true);
        }
        if (pulledDialog.activity == TopicsFragment.class) {
            Bundle bundle3 = new Bundle();
            bundle3.putLong("chat_id", pulledDialog.chat.id);
            baseFragment.presentFragment(new TopicsFragment(bundle3), true);
        }
        if (pulledDialog.activity == DialogsActivity.class) {
            baseFragment.presentFragment(new DialogsActivity(null), true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00bf A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x009d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<PulledDialog> getStackedHistoryDialogs(BaseFragment baseFragment, long j) {
        INavigationLayout parentLayout;
        boolean z;
        long dialogId;
        int i;
        int i2;
        TLRPC$User tLRPC$User;
        Class<T> cls;
        TLRPC$Chat tLRPC$Chat;
        int i3;
        int i4;
        boolean z2;
        ArrayList<PulledDialog> arrayList = new ArrayList<>();
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return arrayList;
        }
        List<BaseFragment> fragmentStack = parentLayout.getFragmentStack();
        List<PulledDialog> pulledDialogs = parentLayout.getPulledDialogs();
        if (fragmentStack != null) {
            int size = fragmentStack.size();
            int i5 = 0;
            while (i5 < size) {
                BaseFragment baseFragment2 = fragmentStack.get(i5);
                TLRPC$User tLRPC$User2 = null;
                if (baseFragment2 instanceof ChatActivity) {
                    Class<ChatActivity> cls2 = ChatActivity.class;
                    ChatActivity chatActivity = (ChatActivity) baseFragment2;
                    if (chatActivity.getChatMode() == 0 && !chatActivity.isReport()) {
                        tLRPC$Chat = chatActivity.getCurrentChat();
                        tLRPC$User = chatActivity.getCurrentUser();
                        dialogId = chatActivity.getDialogId();
                        i2 = chatActivity.getDialogFolderId();
                        i = chatActivity.getDialogFilterId();
                        cls = cls2;
                        if (dialogId != j && (j != 0 || !UserObject.isUserSelf(tLRPC$User))) {
                            i4 = 0;
                            while (true) {
                                if (i4 < arrayList.size()) {
                                    i3 = size;
                                    z2 = false;
                                    break;
                                }
                                i3 = size;
                                if (arrayList.get(i4).dialogId == dialogId) {
                                    z2 = true;
                                    break;
                                }
                                i4++;
                                size = i3;
                            }
                            if (z2) {
                                PulledDialog pulledDialog = new PulledDialog();
                                pulledDialog.activity = cls;
                                pulledDialog.stackIndex = i5;
                                pulledDialog.chat = tLRPC$Chat;
                                pulledDialog.user = tLRPC$User;
                                pulledDialog.dialogId = dialogId;
                                pulledDialog.folderId = i2;
                                pulledDialog.filterId = i;
                                if (tLRPC$Chat != null || tLRPC$User != null) {
                                    arrayList.add(pulledDialog);
                                }
                            }
                        }
                    }
                    i3 = size;
                } else {
                    if (baseFragment2 instanceof ProfileActivity) {
                        ProfileActivity profileActivity = (ProfileActivity) baseFragment2;
                        TLRPC$Chat currentChat = profileActivity.getCurrentChat();
                        try {
                            tLRPC$User2 = profileActivity.getUserInfo().user;
                        } catch (Exception unused) {
                        }
                        dialogId = profileActivity.getDialogId();
                        i = 0;
                        i2 = 0;
                        tLRPC$User = tLRPC$User2;
                        cls = ProfileActivity.class;
                        tLRPC$Chat = currentChat;
                        if (dialogId != j) {
                            i4 = 0;
                            while (true) {
                                if (i4 < arrayList.size()) {
                                }
                                i4++;
                                size = i3;
                            }
                            if (z2) {
                            }
                        }
                    }
                    i3 = size;
                }
                i5++;
                size = i3;
            }
        }
        if (pulledDialogs != null) {
            for (int size2 = pulledDialogs.size() - 1; size2 >= 0; size2--) {
                PulledDialog pulledDialog2 = pulledDialogs.get(size2);
                if (pulledDialog2.dialogId != j) {
                    int i6 = 0;
                    while (true) {
                        if (i6 >= arrayList.size()) {
                            z = false;
                            break;
                        } else if (arrayList.get(i6).dialogId == pulledDialog2.dialogId) {
                            z = true;
                            break;
                        } else {
                            i6++;
                        }
                    }
                    if (!z) {
                        arrayList.add(pulledDialog2);
                    }
                }
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Components.BackButtonMenu$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getStackedHistoryDialogs$2;
                lambda$getStackedHistoryDialogs$2 = BackButtonMenu.lambda$getStackedHistoryDialogs$2((BackButtonMenu.PulledDialog) obj, (BackButtonMenu.PulledDialog) obj2);
                return lambda$getStackedHistoryDialogs$2;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getStackedHistoryDialogs$2(PulledDialog pulledDialog, PulledDialog pulledDialog2) {
        return pulledDialog2.stackIndex - pulledDialog.stackIndex;
    }

    public static void addToPulledDialogs(BaseFragment baseFragment, int i, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, long j, int i2, int i3) {
        INavigationLayout parentLayout;
        if ((tLRPC$Chat == null && tLRPC$User == null) || baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null) {
            return;
        }
        if (parentLayout.getPulledDialogs() == null) {
            parentLayout.setPulledDialogs(new ArrayList());
        }
        boolean z = false;
        for (PulledDialog pulledDialog : parentLayout.getPulledDialogs()) {
            if ((tLRPC$TL_forumTopic == null && pulledDialog.dialogId == j) || (tLRPC$TL_forumTopic != null && pulledDialog.topic.id == tLRPC$TL_forumTopic.id)) {
                z = true;
                break;
            }
        }
        if (z) {
            return;
        }
        PulledDialog pulledDialog2 = new PulledDialog();
        pulledDialog2.activity = ChatActivity.class;
        pulledDialog2.stackIndex = i;
        pulledDialog2.dialogId = j;
        pulledDialog2.filterId = i3;
        pulledDialog2.folderId = i2;
        pulledDialog2.chat = tLRPC$Chat;
        pulledDialog2.user = tLRPC$User;
        pulledDialog2.topic = tLRPC$TL_forumTopic;
        parentLayout.getPulledDialogs().add(pulledDialog2);
    }

    public static void clearPulledDialogs(BaseFragment baseFragment, int i) {
        INavigationLayout parentLayout;
        if (baseFragment == null || (parentLayout = baseFragment.getParentLayout()) == null || parentLayout.getPulledDialogs() == null) {
            return;
        }
        int i2 = 0;
        while (i2 < parentLayout.getPulledDialogs().size()) {
            if (parentLayout.getPulledDialogs().get(i2).stackIndex > i) {
                parentLayout.getPulledDialogs().remove(i2);
                i2--;
            }
            i2++;
        }
    }
}
