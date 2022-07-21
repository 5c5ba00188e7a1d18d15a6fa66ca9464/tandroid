package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_phone_getGroupCallJoinAs;
import org.telegram.tgnet.TLRPC$TL_phone_joinAsPeers;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class JoinCallAlert extends BottomSheet {
    private static ArrayList<TLRPC$Peer> cachedChats;
    private static long lastCacheDid;
    private static long lastCacheTime;
    private static int lastCachedAccount;
    private boolean animationInProgress;
    private ArrayList<TLRPC$Peer> chats;
    private TLRPC$Peer currentPeer;
    private int currentType;
    private JoinCallAlertDelegate delegate;
    private BottomSheetCell doneButton;
    private boolean ignoreLayout;
    private RecyclerListView listView;
    private TextView messageTextView;
    private boolean schedule;
    private int scrollOffsetY;
    private TLRPC$InputPeer selectAfterDismiss;
    private TLRPC$Peer selectedPeer;
    private Drawable shadowDrawable;
    private TextView textView;

    /* loaded from: classes3.dex */
    public interface JoinCallAlertDelegate {
        void didSelectChat(TLRPC$InputPeer tLRPC$InputPeer, boolean z, boolean z2);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    public static void resetCache() {
        cachedChats = null;
    }

    public static void processDeletedChat(int i, long j) {
        ArrayList<TLRPC$Peer> arrayList;
        if (lastCachedAccount != i || (arrayList = cachedChats) == null || j > 0) {
            return;
        }
        int i2 = 0;
        int size = arrayList.size();
        while (true) {
            if (i2 >= size) {
                break;
            } else if (MessageObject.getPeerId(cachedChats.get(i2)) == j) {
                cachedChats.remove(i2);
                break;
            } else {
                i2++;
            }
        }
        if (!cachedChats.isEmpty()) {
            return;
        }
        cachedChats = null;
    }

    /* loaded from: classes3.dex */
    public class BottomSheetCell extends FrameLayout {
        private View background;
        private boolean hasBackground;
        private CharSequence text;
        private TextView[] textView = new TextView[2];

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BottomSheetCell(Context context, boolean z) {
            super(context);
            JoinCallAlert.this = r13;
            this.hasBackground = !z;
            setBackground(null);
            View view = new View(context);
            this.background = view;
            if (this.hasBackground) {
                view.setBackground(Theme.AdaptiveRipple.filledRect("featuredStickers_addButton", 4.0f));
            }
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, z ? 0.0f : 16.0f, 16.0f, 16.0f));
            for (int i = 0; i < 2; i++) {
                this.textView[i] = new TextView(context);
                this.textView[i].setFocusable(false);
                this.textView[i].setLines(1);
                this.textView[i].setSingleLine(true);
                this.textView[i].setGravity(1);
                this.textView[i].setEllipsize(TextUtils.TruncateAt.END);
                this.textView[i].setGravity(17);
                if (this.hasBackground) {
                    this.textView[i].setTextColor(Theme.getColor("featuredStickers_buttonText"));
                    this.textView[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                } else {
                    this.textView[i].setTextColor(Theme.getColor("featuredStickers_addButton"));
                }
                this.textView[i].setImportantForAccessibility(2);
                this.textView[i].setTextSize(1, 14.0f);
                this.textView[i].setPadding(0, 0, 0, this.hasBackground ? 0 : AndroidUtilities.dp(13.0f));
                addView(this.textView[i], LayoutHelper.createFrame(-2, -2.0f, 17, 24.0f, 0.0f, 24.0f, 0.0f));
                if (i == 1) {
                    this.textView[i].setAlpha(0.0f);
                }
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.hasBackground ? 80.0f : 50.0f), 1073741824));
        }

        public void setText(CharSequence charSequence, boolean z) {
            this.text = charSequence;
            if (!z) {
                this.textView[0].setText(charSequence);
                return;
            }
            this.textView[1].setText(charSequence);
            JoinCallAlert.this.animationInProgress = true;
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(180L);
            animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.textView[0], View.ALPHA, 1.0f, 0.0f), ObjectAnimator.ofFloat(this.textView[0], View.TRANSLATION_Y, 0.0f, -AndroidUtilities.dp(10.0f)), ObjectAnimator.ofFloat(this.textView[1], View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.textView[1], View.TRANSLATION_Y, AndroidUtilities.dp(10.0f), 0.0f));
            animatorSet.addListener(new AnonymousClass1());
            animatorSet.start();
        }

        /* renamed from: org.telegram.ui.Components.JoinCallAlert$BottomSheetCell$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                BottomSheetCell.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                JoinCallAlert.this.animationInProgress = false;
                TextView textView = BottomSheetCell.this.textView[0];
                BottomSheetCell.this.textView[0] = BottomSheetCell.this.textView[1];
                BottomSheetCell.this.textView[1] = textView;
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("android.widget.Button");
            accessibilityNodeInfo.setClickable(true);
        }
    }

    public static void checkFewUsers(Context context, long j, AccountInstance accountInstance, MessagesStorage.BooleanCallback booleanCallback) {
        if (lastCachedAccount == accountInstance.getCurrentAccount() && lastCacheDid == j && cachedChats != null && SystemClock.elapsedRealtime() - lastCacheTime < 240000) {
            boolean z = true;
            if (cachedChats.size() != 1) {
                z = false;
            }
            booleanCallback.run(z);
            return;
        }
        AlertDialog alertDialog = new AlertDialog(context, 3);
        TLRPC$TL_phone_getGroupCallJoinAs tLRPC$TL_phone_getGroupCallJoinAs = new TLRPC$TL_phone_getGroupCallJoinAs();
        tLRPC$TL_phone_getGroupCallJoinAs.peer = accountInstance.getMessagesController().getInputPeer(j);
        alertDialog.setOnCancelListener(new JoinCallAlert$$ExternalSyntheticLambda1(accountInstance, accountInstance.getConnectionsManager().sendRequest(tLRPC$TL_phone_getGroupCallJoinAs, new JoinCallAlert$$ExternalSyntheticLambda6(alertDialog, j, accountInstance, booleanCallback))));
        try {
            alertDialog.showDelayed(500L);
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$checkFewUsers$1(AlertDialog alertDialog, long j, AccountInstance accountInstance, MessagesStorage.BooleanCallback booleanCallback, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new JoinCallAlert$$ExternalSyntheticLambda4(alertDialog, tLObject, j, accountInstance, booleanCallback));
    }

    public static /* synthetic */ void lambda$checkFewUsers$0(AlertDialog alertDialog, TLObject tLObject, long j, AccountInstance accountInstance, MessagesStorage.BooleanCallback booleanCallback) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            TLRPC$TL_phone_joinAsPeers tLRPC$TL_phone_joinAsPeers = (TLRPC$TL_phone_joinAsPeers) tLObject;
            cachedChats = tLRPC$TL_phone_joinAsPeers.peers;
            lastCacheDid = j;
            lastCacheTime = SystemClock.elapsedRealtime();
            lastCachedAccount = accountInstance.getCurrentAccount();
            boolean z = false;
            accountInstance.getMessagesController().putChats(tLRPC$TL_phone_joinAsPeers.chats, false);
            accountInstance.getMessagesController().putUsers(tLRPC$TL_phone_joinAsPeers.users, false);
            if (tLRPC$TL_phone_joinAsPeers.peers.size() == 1) {
                z = true;
            }
            booleanCallback.run(z);
        }
    }

    public static /* synthetic */ void lambda$checkFewUsers$2(AccountInstance accountInstance, int i, DialogInterface dialogInterface) {
        accountInstance.getConnectionsManager().cancelRequest(i, true);
    }

    public static void open(Context context, long j, AccountInstance accountInstance, BaseFragment baseFragment, int i, TLRPC$Peer tLRPC$Peer, JoinCallAlertDelegate joinCallAlertDelegate) {
        if (context == null || joinCallAlertDelegate == null) {
            return;
        }
        if (lastCachedAccount == accountInstance.getCurrentAccount() && lastCacheDid == j && cachedChats != null && SystemClock.elapsedRealtime() - lastCacheTime < 300000) {
            if (cachedChats.size() == 1 && i != 0) {
                joinCallAlertDelegate.didSelectChat(accountInstance.getMessagesController().getInputPeer(MessageObject.getPeerId(cachedChats.get(0))), false, false);
                return;
            } else {
                showAlert(context, j, cachedChats, baseFragment, i, tLRPC$Peer, joinCallAlertDelegate);
                return;
            }
        }
        AlertDialog alertDialog = new AlertDialog(context, 3);
        TLRPC$TL_phone_getGroupCallJoinAs tLRPC$TL_phone_getGroupCallJoinAs = new TLRPC$TL_phone_getGroupCallJoinAs();
        tLRPC$TL_phone_getGroupCallJoinAs.peer = accountInstance.getMessagesController().getInputPeer(j);
        alertDialog.setOnCancelListener(new JoinCallAlert$$ExternalSyntheticLambda0(accountInstance, accountInstance.getConnectionsManager().sendRequest(tLRPC$TL_phone_getGroupCallJoinAs, new JoinCallAlert$$ExternalSyntheticLambda7(alertDialog, accountInstance, joinCallAlertDelegate, j, context, baseFragment, i, tLRPC$Peer))));
        try {
            alertDialog.showDelayed(500L);
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$open$4(AlertDialog alertDialog, AccountInstance accountInstance, JoinCallAlertDelegate joinCallAlertDelegate, long j, Context context, BaseFragment baseFragment, int i, TLRPC$Peer tLRPC$Peer, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new JoinCallAlert$$ExternalSyntheticLambda5(alertDialog, tLObject, accountInstance, joinCallAlertDelegate, j, context, baseFragment, i, tLRPC$Peer));
    }

    public static /* synthetic */ void lambda$open$3(AlertDialog alertDialog, TLObject tLObject, AccountInstance accountInstance, JoinCallAlertDelegate joinCallAlertDelegate, long j, Context context, BaseFragment baseFragment, int i, TLRPC$Peer tLRPC$Peer) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            TLRPC$TL_phone_joinAsPeers tLRPC$TL_phone_joinAsPeers = (TLRPC$TL_phone_joinAsPeers) tLObject;
            if (tLRPC$TL_phone_joinAsPeers.peers.size() == 1) {
                joinCallAlertDelegate.didSelectChat(accountInstance.getMessagesController().getInputPeer(MessageObject.getPeerId(tLRPC$TL_phone_joinAsPeers.peers.get(0))), false, false);
                return;
            }
            cachedChats = tLRPC$TL_phone_joinAsPeers.peers;
            lastCacheDid = j;
            lastCacheTime = SystemClock.elapsedRealtime();
            lastCachedAccount = accountInstance.getCurrentAccount();
            accountInstance.getMessagesController().putChats(tLRPC$TL_phone_joinAsPeers.chats, false);
            accountInstance.getMessagesController().putUsers(tLRPC$TL_phone_joinAsPeers.users, false);
            showAlert(context, j, tLRPC$TL_phone_joinAsPeers.peers, baseFragment, i, tLRPC$Peer, joinCallAlertDelegate);
        }
    }

    public static /* synthetic */ void lambda$open$5(AccountInstance accountInstance, int i, DialogInterface dialogInterface) {
        accountInstance.getConnectionsManager().cancelRequest(i, true);
    }

    private static void showAlert(Context context, long j, ArrayList<TLRPC$Peer> arrayList, BaseFragment baseFragment, int i, TLRPC$Peer tLRPC$Peer, JoinCallAlertDelegate joinCallAlertDelegate) {
        JoinCallAlert joinCallAlert = new JoinCallAlert(context, j, arrayList, i, tLRPC$Peer, joinCallAlertDelegate);
        if (baseFragment != null) {
            if (baseFragment.getParentActivity() == null) {
                return;
            }
            baseFragment.showDialog(joinCallAlert);
            return;
        }
        joinCallAlert.show();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r19v0, types: [org.telegram.ui.Components.JoinCallAlert, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog] */
    /* JADX WARN: Type inference failed for: r5v7, types: [android.widget.LinearLayout, org.telegram.ui.Components.JoinCallAlert$1, android.view.View] */
    /* JADX WARN: Type inference failed for: r8v5, types: [androidx.core.widget.NestedScrollView, android.view.View] */
    private JoinCallAlert(Context context, long j, ArrayList<TLRPC$Peer> arrayList, int i, TLRPC$Peer tLRPC$Peer, JoinCallAlertDelegate joinCallAlertDelegate) {
        super(context, false);
        int i2;
        AnonymousClass2 anonymousClass2;
        boolean z;
        setApplyBottomPadding(false);
        this.chats = new ArrayList<>(arrayList);
        this.delegate = joinCallAlertDelegate;
        this.currentType = i;
        Drawable mutate = context.getResources().getDrawable(2131166143).mutate();
        this.shadowDrawable = mutate;
        if (i == 2) {
            if (VoIPService.getSharedInstance() != null) {
                long selfId = VoIPService.getSharedInstance().getSelfId();
                int size = this.chats.size();
                int i3 = 0;
                while (true) {
                    if (i3 >= size) {
                        break;
                    }
                    TLRPC$Peer tLRPC$Peer2 = this.chats.get(i3);
                    if (MessageObject.getPeerId(tLRPC$Peer2) == selfId) {
                        this.currentPeer = tLRPC$Peer2;
                        this.selectedPeer = tLRPC$Peer2;
                        break;
                    }
                    i3++;
                }
            } else if (tLRPC$Peer != null) {
                long peerId = MessageObject.getPeerId(tLRPC$Peer);
                int size2 = this.chats.size();
                int i4 = 0;
                while (true) {
                    if (i4 >= size2) {
                        break;
                    }
                    TLRPC$Peer tLRPC$Peer3 = this.chats.get(i4);
                    if (MessageObject.getPeerId(tLRPC$Peer3) == peerId) {
                        this.currentPeer = tLRPC$Peer3;
                        this.selectedPeer = tLRPC$Peer3;
                        break;
                    }
                    i4++;
                }
            } else {
                this.selectedPeer = this.chats.get(0);
            }
            Drawable drawable = this.shadowDrawable;
            i2 = Theme.getColor("voipgroup_inviteMembersBackground");
            drawable.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        } else {
            i2 = Theme.getColor("dialogBackground");
            mutate.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
            this.selectedPeer = this.chats.get(0);
        }
        fixNavigationBar(i2);
        if (this.currentType == 0) {
            ?? anonymousClass1 = new AnonymousClass1(context);
            anonymousClass1.setOrientation(1);
            ?? nestedScrollView = new NestedScrollView(context);
            nestedScrollView.addView(anonymousClass1);
            setCustomView(nestedScrollView);
            anonymousClass2 = anonymousClass1;
        } else {
            AnonymousClass2 anonymousClass22 = new AnonymousClass2(context);
            this.containerView = anonymousClass22;
            anonymousClass22.setWillNotDraw(false);
            ViewGroup viewGroup = this.containerView;
            int i5 = this.backgroundPaddingLeft;
            viewGroup.setPadding(i5, 0, i5, 0);
            anonymousClass2 = anonymousClass22;
        }
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.listView = anonymousClass3;
        anonymousClass3.setLayoutManager(new LinearLayoutManager(getContext(), this.currentType == 0 ? 0 : 1, false));
        this.listView.setAdapter(new ListAdapter(context));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setEnabled(true);
        this.listView.setSelectorDrawableColor(0);
        this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.listView.setOnScrollListener(new AnonymousClass4());
        this.listView.setOnItemClickListener(new JoinCallAlert$$ExternalSyntheticLambda8(this, chat));
        if (i != 0) {
            anonymousClass2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 100.0f, 0.0f, 80.0f));
        } else {
            this.listView.setSelectorDrawableColor(0);
            this.listView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
        }
        if (i == 0) {
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            rLottieImageView.setAutoRepeat(true);
            rLottieImageView.setAnimation(2131558596, 120, 120);
            rLottieImageView.playAnimation();
            anonymousClass2.addView(rLottieImageView, LayoutHelper.createLinear(160, 160, 49, 17, 8, 17, 0));
        }
        TextView textView = new TextView(context);
        this.textView = textView;
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setTextSize(1, 20.0f);
        if (i == 2) {
            this.textView.setTextColor(Theme.getColor("voipgroup_nameText"));
        } else {
            this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
        }
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils.TruncateAt.END);
        if (i == 0) {
            if (ChatObject.isChannelOrGiga(chat)) {
                this.textView.setText(LocaleController.getString("StartVoipChannelTitle", 2131628489));
            } else {
                this.textView.setText(LocaleController.getString("StartVoipChatTitle", 2131628493));
            }
            anonymousClass2.addView(this.textView, LayoutHelper.createLinear(-2, -2, 49, 23, 16, 23, 0));
        } else {
            if (i == 2) {
                this.textView.setText(LocaleController.getString("VoipGroupDisplayAs", 2131629121));
            } else if (ChatObject.isChannelOrGiga(chat)) {
                this.textView.setText(LocaleController.getString("VoipChannelJoinAs", 2131629050));
            } else {
                this.textView.setText(LocaleController.getString("VoipGroupJoinAs", 2131629139));
            }
            anonymousClass2.addView(this.textView, LayoutHelper.createFrame(-2, -2.0f, 51, 23.0f, 8.0f, 23.0f, 0.0f));
        }
        TextView textView2 = new TextView(getContext());
        this.messageTextView = textView2;
        if (i == 2) {
            textView2.setTextColor(Theme.getColor("voipgroup_lastSeenText"));
        } else {
            textView2.setTextColor(Theme.getColor("dialogTextGray3"));
        }
        this.messageTextView.setTextSize(1, 14.0f);
        int size3 = this.chats.size();
        for (int i6 = 0; i6 < size3; i6++) {
            long peerId2 = MessageObject.getPeerId(this.chats.get(i6));
            if (peerId2 < 0) {
                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId2));
                if (!ChatObject.isChannel(chat2) || chat2.megagroup) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        this.messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        this.messageTextView.setLinkTextColor(Theme.getColor("dialogTextLink"));
        if (i == 0) {
            StringBuilder sb = new StringBuilder();
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                sb.append(LocaleController.getString("VoipChannelStart2", 2131629064));
            } else {
                sb.append(LocaleController.getString("VoipGroupStart2", 2131629178));
            }
            if (this.chats.size() > 1) {
                sb.append("\n\n");
                sb.append(LocaleController.getString("VoipChatDisplayedAs", 2131629078));
            } else {
                this.listView.setVisibility(8);
            }
            this.messageTextView.setText(sb);
            this.messageTextView.setGravity(49);
            anonymousClass2.addView(this.messageTextView, LayoutHelper.createLinear(-2, -2, 49, 23, 0, 23, 5));
        } else {
            if (z) {
                this.messageTextView.setText(LocaleController.getString("VoipGroupStartAsInfoGroup", 2131629181));
            } else {
                this.messageTextView.setText(LocaleController.getString("VoipGroupStartAsInfo", 2131629180));
            }
            this.messageTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            anonymousClass2.addView(this.messageTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 23.0f, 0.0f, 23.0f, 5.0f));
        }
        if (i == 0) {
            anonymousClass2.addView(this.listView, LayoutHelper.createLinear(this.chats.size() < 5 ? -2 : -1, 95, 49, 0, 6, 0, 0));
        }
        BottomSheetCell bottomSheetCell = new BottomSheetCell(context, false);
        this.doneButton = bottomSheetCell;
        bottomSheetCell.background.setOnClickListener(new JoinCallAlert$$ExternalSyntheticLambda3(this, joinCallAlertDelegate));
        if (this.currentType == 0) {
            anonymousClass2.addView(this.doneButton, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
            BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context, true);
            if (ChatObject.isChannelOrGiga(chat)) {
                bottomSheetCell2.setText(LocaleController.getString("VoipChannelScheduleVoiceChat", 2131629060), false);
            } else {
                bottomSheetCell2.setText(LocaleController.getString("VoipGroupScheduleVoiceChat", 2131629170), false);
            }
            bottomSheetCell2.background.setOnClickListener(new JoinCallAlert$$ExternalSyntheticLambda2(this));
            anonymousClass2.addView(bottomSheetCell2, LayoutHelper.createLinear(-1, 50, 51, 0, 0, 0, 0));
        } else {
            anonymousClass2.addView(this.doneButton, LayoutHelper.createFrame(-1, 50.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        updateDoneButton(false, chat);
    }

    /* renamed from: org.telegram.ui.Components.JoinCallAlert$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayout {
        boolean sorted;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            JoinCallAlert.this = r1;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            if (JoinCallAlert.this.currentType == 0) {
                int size = View.MeasureSpec.getSize(i);
                int size2 = JoinCallAlert.this.chats.size() * AndroidUtilities.dp(95.0f);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) JoinCallAlert.this.listView.getLayoutParams();
                if (size2 > size) {
                    layoutParams.width = -1;
                    layoutParams.gravity = 51;
                    if (!this.sorted) {
                        if (JoinCallAlert.this.selectedPeer != null) {
                            JoinCallAlert.this.chats.remove(JoinCallAlert.this.selectedPeer);
                            JoinCallAlert.this.chats.add(0, JoinCallAlert.this.selectedPeer);
                        }
                        this.sorted = true;
                    }
                } else {
                    layoutParams.width = -2;
                    layoutParams.gravity = 49;
                    if (!this.sorted) {
                        if (JoinCallAlert.this.selectedPeer != null) {
                            int max = JoinCallAlert.this.chats.size() % 2 == 0 ? Math.max(0, (JoinCallAlert.this.chats.size() / 2) - 1) : JoinCallAlert.this.chats.size() / 2;
                            JoinCallAlert.this.chats.remove(JoinCallAlert.this.selectedPeer);
                            JoinCallAlert.this.chats.add(max, JoinCallAlert.this.selectedPeer);
                        }
                        this.sorted = true;
                    }
                }
            }
            super.onMeasure(i, i2);
        }
    }

    /* renamed from: org.telegram.ui.Components.JoinCallAlert$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            JoinCallAlert.this = r1;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && JoinCallAlert.this.scrollOffsetY != 0 && motionEvent.getY() < JoinCallAlert.this.scrollOffsetY) {
                JoinCallAlert.this.dismiss();
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !JoinCallAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21) {
                size -= AndroidUtilities.statusBarHeight;
            }
            measureChildWithMargins(JoinCallAlert.this.messageTextView, i, 0, i2, 0);
            int measuredHeight = JoinCallAlert.this.messageTextView.getMeasuredHeight();
            ((FrameLayout.LayoutParams) JoinCallAlert.this.listView.getLayoutParams()).topMargin = AndroidUtilities.dp(65.0f) + measuredHeight;
            getMeasuredWidth();
            int dp = AndroidUtilities.dp(80.0f) + (JoinCallAlert.this.chats.size() * AndroidUtilities.dp(58.0f)) + ((BottomSheet) JoinCallAlert.this).backgroundPaddingTop + AndroidUtilities.dp(55.0f) + measuredHeight;
            int i3 = size / 5;
            int i4 = dp < i3 * 3 ? size - dp : i3 * 2;
            if (JoinCallAlert.this.listView.getPaddingTop() != i4) {
                JoinCallAlert.this.ignoreLayout = true;
                JoinCallAlert.this.listView.setPadding(0, i4, 0, 0);
                JoinCallAlert.this.ignoreLayout = false;
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            JoinCallAlert.this.updateLayout();
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (JoinCallAlert.this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            JoinCallAlert.this.shadowDrawable.setBounds(0, JoinCallAlert.this.scrollOffsetY - ((BottomSheet) JoinCallAlert.this).backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
            JoinCallAlert.this.shadowDrawable.draw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.Components.JoinCallAlert$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            JoinCallAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (JoinCallAlert.this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.JoinCallAlert$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends RecyclerView.OnScrollListener {
        AnonymousClass4() {
            JoinCallAlert.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            JoinCallAlert.this.updateLayout();
        }
    }

    public /* synthetic */ void lambda$new$6(TLRPC$Chat tLRPC$Chat, View view, int i) {
        if (this.animationInProgress || this.chats.get(i) == this.selectedPeer) {
            return;
        }
        this.selectedPeer = this.chats.get(i);
        boolean z = view instanceof GroupCreateUserCell;
        if (z) {
            ((GroupCreateUserCell) view).setChecked(true, true);
        } else if (view instanceof ShareDialogCell) {
            ((ShareDialogCell) view).setChecked(true, true);
            view.invalidate();
        }
        int childCount = this.listView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt != view) {
                if (z) {
                    ((GroupCreateUserCell) childAt).setChecked(false, true);
                } else if (view instanceof ShareDialogCell) {
                    ((ShareDialogCell) childAt).setChecked(false, true);
                }
            }
        }
        if (this.currentType == 0) {
            return;
        }
        updateDoneButton(true, tLRPC$Chat);
    }

    public /* synthetic */ void lambda$new$7(JoinCallAlertDelegate joinCallAlertDelegate, View view) {
        TLRPC$InputPeer inputPeer = MessagesController.getInstance(this.currentAccount).getInputPeer(MessageObject.getPeerId(this.selectedPeer));
        if (this.currentType == 2) {
            if (this.selectedPeer != this.currentPeer) {
                boolean z = true;
                if (this.chats.size() <= 1) {
                    z = false;
                }
                joinCallAlertDelegate.didSelectChat(inputPeer, z, false);
            }
        } else {
            this.selectAfterDismiss = inputPeer;
        }
        dismiss();
    }

    public /* synthetic */ void lambda$new$8(View view) {
        this.selectAfterDismiss = MessagesController.getInstance(this.currentAccount).getInputPeer(MessageObject.getPeerId(this.selectedPeer));
        this.schedule = true;
        dismiss();
    }

    private void updateDoneButton(boolean z, TLRPC$Chat tLRPC$Chat) {
        if (this.currentType == 0) {
            if (ChatObject.isChannelOrGiga(tLRPC$Chat)) {
                this.doneButton.setText(LocaleController.formatString("VoipChannelStartVoiceChat", 2131629067, new Object[0]), z);
                return;
            } else {
                this.doneButton.setText(LocaleController.formatString("VoipGroupStartVoiceChat", 2131629188, new Object[0]), z);
                return;
            }
        }
        long peerId = MessageObject.getPeerId(this.selectedPeer);
        if (DialogObject.isUserDialog(peerId)) {
            this.doneButton.setText(LocaleController.formatString("VoipGroupContinueAs", 2131629116, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId)))), z);
            return;
        }
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
        BottomSheetCell bottomSheetCell = this.doneButton;
        Object[] objArr = new Object[1];
        objArr[0] = chat != null ? chat.title : "";
        bottomSheetCell.setText(LocaleController.formatString("VoipGroupContinueAs", 2131629116, objArr), z);
    }

    public void updateLayout() {
        if (this.currentType == 0) {
            return;
        }
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        int i = 0;
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(9.0f);
        if (top > 0 && holder != null && holder.getAdapterPosition() == 0) {
            i = top;
        }
        if (this.scrollOffsetY == i) {
            return;
        }
        this.textView.setTranslationY(AndroidUtilities.dp(19.0f) + top);
        this.messageTextView.setTranslationY(top + AndroidUtilities.dp(56.0f));
        RecyclerListView recyclerListView2 = this.listView;
        this.scrollOffsetY = i;
        recyclerListView2.setTopGlowOffset(i);
        this.containerView.invalidate();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        TLRPC$InputPeer tLRPC$InputPeer = this.selectAfterDismiss;
        if (tLRPC$InputPeer != null) {
            JoinCallAlertDelegate joinCallAlertDelegate = this.delegate;
            boolean z = true;
            if (this.chats.size() <= 1) {
                z = false;
            }
            joinCallAlertDelegate.didSelectChat(tLRPC$InputPeer, z, this.schedule);
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ListAdapter(Context context) {
            JoinCallAlert.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return JoinCallAlert.this.chats.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (JoinCallAlert.this.currentType == 0) {
                view = new ShareDialogCell(this.context, 2, null);
                view.setLayoutParams(new RecyclerView.LayoutParams(AndroidUtilities.dp(80.0f), AndroidUtilities.dp(100.0f)));
            } else {
                view = new GroupCreateUserCell(this.context, 2, 0, false, JoinCallAlert.this.currentType == 2);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            viewHolder.getAdapterPosition();
            long peerId = MessageObject.getPeerId(JoinCallAlert.this.selectedPeer);
            View view = viewHolder.itemView;
            boolean z = true;
            if (view instanceof GroupCreateUserCell) {
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) view;
                Object object = groupCreateUserCell.getObject();
                long j = 0;
                if (object != null) {
                    if (object instanceof TLRPC$Chat) {
                        j = -((TLRPC$Chat) object).id;
                    } else {
                        j = ((TLRPC$User) object).id;
                    }
                }
                if (peerId != j) {
                    z = false;
                }
                groupCreateUserCell.setChecked(z, false);
                return;
            }
            ShareDialogCell shareDialogCell = (ShareDialogCell) view;
            if (peerId != shareDialogCell.getCurrentDialog()) {
                z = false;
            }
            shareDialogCell.setChecked(z, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            TLObject tLObject;
            long peerId = MessageObject.getPeerId((TLRPC$Peer) JoinCallAlert.this.chats.get(i));
            if (peerId > 0) {
                tLObject = MessagesController.getInstance(((BottomSheet) JoinCallAlert.this).currentAccount).getUser(Long.valueOf(peerId));
                str = LocaleController.getString("VoipGroupPersonalAccount", 2131629162);
            } else {
                tLObject = MessagesController.getInstance(((BottomSheet) JoinCallAlert.this).currentAccount).getChat(Long.valueOf(-peerId));
                str = null;
            }
            boolean z = false;
            if (JoinCallAlert.this.currentType == 0) {
                ShareDialogCell shareDialogCell = (ShareDialogCell) viewHolder.itemView;
                if (peerId == MessageObject.getPeerId(JoinCallAlert.this.selectedPeer)) {
                    z = true;
                }
                shareDialogCell.setDialog(peerId, z, null);
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            if (i != getItemCount() - 1) {
                z = true;
            }
            groupCreateUserCell.setObject(tLObject, null, str, z);
        }
    }
}
