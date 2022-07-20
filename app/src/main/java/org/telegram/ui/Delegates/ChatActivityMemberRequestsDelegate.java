package org.telegram.ui.Delegates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MemberRequestsBottomSheet;
/* loaded from: classes3.dex */
public class ChatActivityMemberRequestsDelegate {
    private AvatarsImageView avatarsView;
    private MemberRequestsBottomSheet bottomSheet;
    private final Callback callback;
    private TLRPC$ChatFull chatInfo;
    private int closePendingRequestsCount = -1;
    private ImageView closeView;
    private final int currentAccount;
    private final TLRPC$Chat currentChat;
    private final BaseFragment fragment;
    private ValueAnimator pendingRequestsAnimator;
    private int pendingRequestsCount;
    private float pendingRequestsEnterOffset;
    private TextView requestsCountTextView;
    private FrameLayout root;

    /* loaded from: classes3.dex */
    public interface Callback {
        void onEnterOffsetChanged();
    }

    public ChatActivityMemberRequestsDelegate(BaseFragment baseFragment, TLRPC$Chat tLRPC$Chat, Callback callback) {
        this.fragment = baseFragment;
        this.currentChat = tLRPC$Chat;
        this.currentAccount = baseFragment.getCurrentAccount();
        this.callback = callback;
    }

    public View getView() {
        if (this.root == null) {
            FrameLayout frameLayout = new FrameLayout(this.fragment.getParentActivity());
            this.root = frameLayout;
            frameLayout.setBackgroundResource(2131165274);
            this.root.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(this.fragment.getThemedColor("chat_topPanelBackground"), PorterDuff.Mode.MULTIPLY));
            this.root.setVisibility(8);
            this.pendingRequestsEnterOffset = -getViewHeight();
            View view = new View(this.fragment.getParentActivity());
            view.setBackground(Theme.getSelectorDrawable(false));
            view.setOnClickListener(new ChatActivityMemberRequestsDelegate$$ExternalSyntheticLambda2(this));
            this.root.addView(view, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 2.0f));
            LinearLayout linearLayout = new LinearLayout(this.fragment.getParentActivity());
            linearLayout.setOrientation(0);
            this.root.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, 0.0f, 36.0f, 0.0f));
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, this.fragment.getParentActivity(), false);
            this.avatarsView = anonymousClass1;
            anonymousClass1.reset();
            linearLayout.addView(this.avatarsView, LayoutHelper.createFrame(-2, -1.0f, 48, 8.0f, 0.0f, 10.0f, 0.0f));
            TextView textView = new TextView(this.fragment.getParentActivity());
            this.requestsCountTextView = textView;
            textView.setEllipsize(TextUtils.TruncateAt.END);
            this.requestsCountTextView.setGravity(16);
            this.requestsCountTextView.setSingleLine();
            this.requestsCountTextView.setText((CharSequence) null);
            this.requestsCountTextView.setTextColor(this.fragment.getThemedColor("chat_topPanelTitle"));
            this.requestsCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(this.requestsCountTextView, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, 0.0f, 0.0f, 0.0f));
            ImageView imageView = new ImageView(this.fragment.getParentActivity());
            this.closeView = imageView;
            if (Build.VERSION.SDK_INT >= 21) {
                imageView.setBackground(Theme.createSelectorDrawable(this.fragment.getThemedColor("inappPlayerClose") & 436207615, 1, AndroidUtilities.dp(14.0f)));
            }
            this.closeView.setColorFilter(new PorterDuffColorFilter(this.fragment.getThemedColor("chat_topPanelClose"), PorterDuff.Mode.MULTIPLY));
            this.closeView.setContentDescription(LocaleController.getString("Close", 2131625183));
            this.closeView.setImageResource(2131165619);
            this.closeView.setScaleType(ImageView.ScaleType.CENTER);
            this.closeView.setOnClickListener(new ChatActivityMemberRequestsDelegate$$ExternalSyntheticLambda1(this));
            this.root.addView(this.closeView, LayoutHelper.createFrame(36, -1.0f, 53, 0.0f, 0.0f, 2.0f, 0.0f));
            TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
            if (tLRPC$ChatFull != null) {
                setPendingRequests(tLRPC$ChatFull.requests_pending, tLRPC$ChatFull.recent_requesters, false);
            }
        }
        return this.root;
    }

    public /* synthetic */ void lambda$getView$0(View view) {
        showBottomSheet();
    }

    /* renamed from: org.telegram.ui.Delegates.ChatActivityMemberRequestsDelegate$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AvatarsImageView {
        AnonymousClass1(ChatActivityMemberRequestsDelegate chatActivityMemberRequestsDelegate, Context context, boolean z) {
            super(context, z);
        }

        @Override // org.telegram.ui.Components.AvatarsImageView, android.view.View
        public void onMeasure(int i, int i2) {
            int i3 = this.avatarsDarawable.count;
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(i3 == 0 ? 0 : ((i3 - 1) * 20) + 24), 1073741824), i2);
        }
    }

    public /* synthetic */ void lambda$getView$1(View view) {
        this.fragment.getMessagesController().setChatPendingRequestsOnClose(this.currentChat.id, this.pendingRequestsCount);
        this.closePendingRequestsCount = this.pendingRequestsCount;
        animatePendingRequests(false, true);
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull, boolean z) {
        this.chatInfo = tLRPC$ChatFull;
        if (tLRPC$ChatFull != null) {
            setPendingRequests(tLRPC$ChatFull.requests_pending, tLRPC$ChatFull.recent_requesters, z);
        }
    }

    public int getViewHeight() {
        return AndroidUtilities.dp(40.0f);
    }

    public float getViewEnterOffset() {
        return this.pendingRequestsEnterOffset;
    }

    public void onBackToScreen() {
        MemberRequestsBottomSheet memberRequestsBottomSheet = this.bottomSheet;
        if (memberRequestsBottomSheet == null || !memberRequestsBottomSheet.isNeedRestoreDialog()) {
            return;
        }
        showBottomSheet();
    }

    /* renamed from: org.telegram.ui.Delegates.ChatActivityMemberRequestsDelegate$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends MemberRequestsBottomSheet {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(BaseFragment baseFragment, long j) {
            super(baseFragment, j);
            ChatActivityMemberRequestsDelegate.this = r1;
        }

        @Override // org.telegram.ui.Components.UsersAlertBase, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
        public void dismiss() {
            if (ChatActivityMemberRequestsDelegate.this.bottomSheet != null && !ChatActivityMemberRequestsDelegate.this.bottomSheet.isNeedRestoreDialog()) {
                ChatActivityMemberRequestsDelegate.this.bottomSheet = null;
            }
            super.dismiss();
        }
    }

    private void showBottomSheet() {
        if (this.bottomSheet == null) {
            this.bottomSheet = new AnonymousClass2(this.fragment, this.currentChat.id);
        }
        this.fragment.showDialog(this.bottomSheet);
    }

    private void setPendingRequests(int i, List<Long> list, boolean z) {
        if (this.root == null) {
            return;
        }
        if (i <= 0) {
            if (this.currentChat != null) {
                this.fragment.getMessagesController().setChatPendingRequestsOnClose(this.currentChat.id, 0);
                this.closePendingRequestsCount = 0;
            }
            animatePendingRequests(false, z);
            this.pendingRequestsCount = 0;
        } else if (this.pendingRequestsCount != i) {
            this.pendingRequestsCount = i;
            this.requestsCountTextView.setText(LocaleController.formatPluralString("JoinUsersRequests", i, new Object[0]));
            animatePendingRequests(true, z);
            if (list == null || list.isEmpty()) {
                return;
            }
            int min = Math.min(3, list.size());
            for (int i2 = 0; i2 < min; i2++) {
                TLRPC$User user = this.fragment.getMessagesController().getUser(list.get(i2));
                if (user != null) {
                    this.avatarsView.setObject(i2, this.currentAccount, user);
                }
            }
            this.avatarsView.setCount(min);
            this.avatarsView.commitTransition(true);
        }
    }

    private void animatePendingRequests(boolean z, boolean z2) {
        int i = 0;
        if (z == (this.root.getVisibility() == 0)) {
            return;
        }
        if (z) {
            if (this.closePendingRequestsCount == -1 && this.currentChat != null) {
                this.closePendingRequestsCount = this.fragment.getMessagesController().getChatPendingRequestsOnClosed(this.currentChat.id);
            }
            int i2 = this.pendingRequestsCount;
            int i3 = this.closePendingRequestsCount;
            if (i2 == i3) {
                return;
            }
            if (i3 != 0 && this.currentChat != null) {
                this.fragment.getMessagesController().setChatPendingRequestsOnClose(this.currentChat.id, 0);
            }
        }
        ValueAnimator valueAnimator = this.pendingRequestsAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float f = 0.0f;
        if (z2) {
            float[] fArr = new float[2];
            fArr[0] = z ? 0.0f : 1.0f;
            if (z) {
                f = 1.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.pendingRequestsAnimator = ofFloat;
            ofFloat.addUpdateListener(new ChatActivityMemberRequestsDelegate$$ExternalSyntheticLambda0(this));
            this.pendingRequestsAnimator.addListener(new AnonymousClass3(z));
            this.pendingRequestsAnimator.setDuration(200L);
            this.pendingRequestsAnimator.start();
            return;
        }
        FrameLayout frameLayout = this.root;
        if (!z) {
            i = 8;
        }
        frameLayout.setVisibility(i);
        if (!z) {
            f = -getViewHeight();
        }
        this.pendingRequestsEnterOffset = f;
        Callback callback = this.callback;
        if (callback == null) {
            return;
        }
        callback.onEnterOffsetChanged();
    }

    public /* synthetic */ void lambda$animatePendingRequests$2(ValueAnimator valueAnimator) {
        this.pendingRequestsEnterOffset = (-getViewHeight()) * (1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue());
        Callback callback = this.callback;
        if (callback != null) {
            callback.onEnterOffsetChanged();
        }
    }

    /* renamed from: org.telegram.ui.Delegates.ChatActivityMemberRequestsDelegate$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$appear;

        AnonymousClass3(boolean z) {
            ChatActivityMemberRequestsDelegate.this = r1;
            this.val$appear = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$appear) {
                ChatActivityMemberRequestsDelegate.this.root.setVisibility(0);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.val$appear) {
                ChatActivityMemberRequestsDelegate.this.root.setVisibility(8);
            }
        }
    }

    public void fillThemeDescriptions(List<ThemeDescription> list) {
        list.add(new ThemeDescription(this.root, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chat_topPanelBackground"));
        list.add(new ThemeDescription(this.requestsCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_topPanelTitle"));
        list.add(new ThemeDescription(this.closeView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chat_topPanelClose"));
    }
}
