package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ChatAttachAlertContactsLayout extends ChatAttachAlert.AttachAlertLayout implements NotificationCenter.NotificationCenterDelegate {
    private PhonebookShareAlertDelegate delegate;
    private EmptyTextProgressView emptyView;
    private FrameLayout frameLayout;
    private boolean ignoreLayout;
    private FillLastLinearLayoutManager layoutManager;
    private ShareAdapter listAdapter;
    private RecyclerListView listView;
    private ShareSearchAdapter searchAdapter;
    private SearchField searchField;
    private View shadow;
    private AnimatorSet shadowAnimation;

    /* loaded from: classes3.dex */
    public interface PhonebookShareAlertDelegate {
        void didSelectContact(TLRPC$User tLRPC$User, boolean z, int i);
    }

    /* loaded from: classes3.dex */
    public static class UserCell extends FrameLayout {
        private AvatarDrawable avatarDrawable;
        private BackupImageView avatarImageView;
        private int currentId;
        private CharSequence currentName;
        private CharSequence currentStatus;
        private TLRPC$User currentUser;
        private CharSequence formattedPhoneNumber;
        private TLRPC$User formattedPhoneNumberUser;
        private TLRPC$FileLocation lastAvatar;
        private String lastName;
        private int lastStatus;
        private SimpleTextView nameTextView;
        private boolean needDivider;
        private final Theme.ResourcesProvider resourcesProvider;
        private SimpleTextView statusTextView;

        /* loaded from: classes3.dex */
        public interface CharSequenceCallback {
            CharSequence run();
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        public UserCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            int i = UserConfig.selectedAccount;
            this.resourcesProvider = resourcesProvider;
            this.avatarDrawable = new AvatarDrawable(resourcesProvider);
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(23.0f));
            BackupImageView backupImageView2 = this.avatarImageView;
            boolean z = LocaleController.isRTL;
            int i2 = 5;
            addView(backupImageView2, LayoutHelper.createFrame(46, 46.0f, (z ? 5 : 3) | 48, z ? 0.0f : 14.0f, 9.0f, z ? 14.0f : 0.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.nameTextView = simpleTextView;
            simpleTextView.setTextColor(getThemedColor("dialogTextBlack"));
            this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.nameTextView.setTextSize(16);
            this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            SimpleTextView simpleTextView2 = this.nameTextView;
            boolean z2 = LocaleController.isRTL;
            addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, (z2 ? 5 : 3) | 48, z2 ? 28.0f : 72.0f, 12.0f, z2 ? 72.0f : 28.0f, 0.0f));
            SimpleTextView simpleTextView3 = new SimpleTextView(context);
            this.statusTextView = simpleTextView3;
            simpleTextView3.setTextSize(13);
            this.statusTextView.setTextColor(getThemedColor("dialogTextGray2"));
            this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            SimpleTextView simpleTextView4 = this.statusTextView;
            boolean z3 = LocaleController.isRTL;
            addView(simpleTextView4, LayoutHelper.createFrame(-1, 20.0f, (!z3 ? 3 : i2) | 48, z3 ? 28.0f : 72.0f, 36.0f, z3 ? 72.0f : 28.0f, 0.0f));
        }

        public void setCurrentId(int i) {
            this.currentId = i;
        }

        public void setData(TLRPC$User tLRPC$User, CharSequence charSequence, CharSequence charSequence2, boolean z) {
            if (tLRPC$User == null && charSequence == null && charSequence2 == null) {
                this.currentStatus = null;
                this.currentName = null;
                this.nameTextView.setText("");
                this.statusTextView.setText("");
                this.avatarImageView.setImageDrawable(null);
                return;
            }
            this.currentStatus = charSequence2;
            this.currentName = charSequence;
            this.currentUser = tLRPC$User;
            this.needDivider = z;
            setWillNotDraw(!z);
            update(0);
        }

        public void setData(TLRPC$User tLRPC$User, CharSequence charSequence, CharSequenceCallback charSequenceCallback, boolean z) {
            setData(tLRPC$User, charSequence, (CharSequence) null, z);
            Utilities.globalQueue.postRunnable(new ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda3(this, charSequenceCallback));
        }

        public /* synthetic */ void lambda$setData$1(CharSequenceCallback charSequenceCallback) {
            AndroidUtilities.runOnUIThread(new ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda2(this, charSequenceCallback.run()));
        }

        /* renamed from: setStatus */
        public void lambda$setData$0(CharSequence charSequence) {
            CharSequence charSequence2;
            this.currentStatus = charSequence;
            if (charSequence != null) {
                this.statusTextView.setText(charSequence);
                return;
            }
            TLRPC$User tLRPC$User = this.currentUser;
            if (tLRPC$User == null) {
                return;
            }
            if (TextUtils.isEmpty(tLRPC$User.phone)) {
                this.statusTextView.setText(LocaleController.getString("NumberUnknown", 2131627126));
            } else if (this.formattedPhoneNumberUser != this.currentUser && (charSequence2 = this.formattedPhoneNumber) != null) {
                this.statusTextView.setText(charSequence2);
            } else {
                this.statusTextView.setText("");
                Utilities.globalQueue.postRunnable(new ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda0(this));
            }
        }

        public /* synthetic */ void lambda$setStatus$3() {
            if (this.currentUser != null) {
                PhoneFormat phoneFormat = PhoneFormat.getInstance();
                this.formattedPhoneNumber = phoneFormat.format("+" + this.currentUser.phone);
                this.formattedPhoneNumberUser = this.currentUser;
                AndroidUtilities.runOnUIThread(new ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda1(this));
            }
        }

        public /* synthetic */ void lambda$setStatus$2() {
            this.statusTextView.setText(this.formattedPhoneNumber);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        /* JADX WARN: Code restructure failed: missing block: B:47:0x0068, code lost:
            if (r12.equals(r11.lastName) == false) goto L50;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void update(int i) {
            String str;
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$User tLRPC$User = this.currentUser;
            TLRPC$FileLocation tLRPC$FileLocation2 = (tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null) ? null : tLRPC$UserProfilePhoto.photo_small;
            if (i != 0) {
                boolean z = true;
                boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation = this.lastAvatar) != null && tLRPC$FileLocation2 == null) || ((tLRPC$FileLocation == null && tLRPC$FileLocation2 != null) || !(tLRPC$FileLocation == null || tLRPC$FileLocation2 == null || (tLRPC$FileLocation.volume_id == tLRPC$FileLocation2.volume_id && tLRPC$FileLocation.local_id == tLRPC$FileLocation2.local_id))));
                if (tLRPC$User != null && !z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                    TLRPC$UserStatus tLRPC$UserStatus = tLRPC$User.status;
                    if ((tLRPC$UserStatus != null ? tLRPC$UserStatus.expires : 0) != this.lastStatus) {
                        z2 = true;
                    }
                }
                if (z2 || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str = null;
                } else {
                    str = tLRPC$User != null ? UserObject.getUserName(tLRPC$User) : null;
                }
                z = z2;
                if (!z) {
                    return;
                }
            } else {
                str = null;
            }
            TLRPC$User tLRPC$User2 = this.currentUser;
            if (tLRPC$User2 != null) {
                this.avatarDrawable.setInfo(tLRPC$User2);
                TLRPC$UserStatus tLRPC$UserStatus2 = this.currentUser.status;
                if (tLRPC$UserStatus2 != null) {
                    this.lastStatus = tLRPC$UserStatus2.expires;
                } else {
                    this.lastStatus = 0;
                }
            } else {
                CharSequence charSequence = this.currentName;
                if (charSequence != null) {
                    this.avatarDrawable.setInfo(this.currentId, charSequence.toString(), null);
                } else {
                    this.avatarDrawable.setInfo(this.currentId, "#", null);
                }
            }
            CharSequence charSequence2 = this.currentName;
            if (charSequence2 != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence2);
            } else {
                TLRPC$User tLRPC$User3 = this.currentUser;
                if (tLRPC$User3 != null) {
                    if (str == null) {
                        str = UserObject.getUserName(tLRPC$User3);
                    }
                    this.lastName = str;
                } else {
                    this.lastName = "";
                }
                this.nameTextView.setText(this.lastName);
            }
            lambda$setData$0(this.currentStatus);
            this.lastAvatar = tLRPC$FileLocation2;
            TLRPC$User tLRPC$User4 = this.currentUser;
            if (tLRPC$User4 != null) {
                this.avatarImageView.setForUserOrChat(tLRPC$User4, this.avatarDrawable);
            } else {
                this.avatarImageView.setImageDrawable(this.avatarDrawable);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(70.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(70.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }
    }

    public ChatAttachAlertContactsLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.searchAdapter = new ShareSearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor("dialogBackground"));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, false, resourcesProvider);
        this.searchField = anonymousClass1;
        anonymousClass1.setHint(LocaleController.getString("SearchFriends", 2131628171));
        this.frameLayout.addView(this.searchField, LayoutHelper.createFrame(-1, -1, 51));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context, null, resourcesProvider);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.showTextView();
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131626867));
        addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, resourcesProvider);
        this.listView = anonymousClass2;
        anonymousClass2.setClipToPadding(false);
        RecyclerListView recyclerListView = this.listView;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(getContext(), 1, false, AndroidUtilities.dp(9.0f), this.listView);
        this.layoutManager = anonymousClass3;
        recyclerListView.setLayoutManager(anonymousClass3);
        this.layoutManager.setBind(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ShareAdapter shareAdapter = new ShareAdapter(context);
        this.listAdapter = shareAdapter;
        recyclerListView2.setAdapter(shareAdapter);
        this.listView.setGlowColor(getThemedColor("dialogScrollGlow"));
        this.listView.setOnItemClickListener(new ChatAttachAlertContactsLayout$$ExternalSyntheticLambda2(this, resourcesProvider));
        this.listView.setOnScrollListener(new AnonymousClass4());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp(58.0f);
        View view = new View(context);
        this.shadow = view;
        view.setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.shadow.setAlpha(0.0f);
        this.shadow.setTag(1);
        addView(this.shadow, layoutParams);
        addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
        NotificationCenter.getInstance(this.parentAlert.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        updateEmptyView();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends SearchField {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, z, resourcesProvider);
            ChatAttachAlertContactsLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.SearchField
        public void onTextChange(String str) {
            if (str.length() != 0) {
                if (ChatAttachAlertContactsLayout.this.emptyView != null) {
                    ChatAttachAlertContactsLayout.this.emptyView.setText(LocaleController.getString("NoResult", 2131626910));
                }
            } else if (ChatAttachAlertContactsLayout.this.listView.getAdapter() != ChatAttachAlertContactsLayout.this.listAdapter) {
                int currentTop = ChatAttachAlertContactsLayout.this.getCurrentTop();
                ChatAttachAlertContactsLayout.this.emptyView.setText(LocaleController.getString("NoContacts", 2131626867));
                ChatAttachAlertContactsLayout.this.emptyView.showTextView();
                ChatAttachAlertContactsLayout.this.listView.setAdapter(ChatAttachAlertContactsLayout.this.listAdapter);
                ChatAttachAlertContactsLayout.this.listAdapter.notifyDataSetChanged();
                if (currentTop > 0) {
                    ChatAttachAlertContactsLayout.this.layoutManager.scrollToPositionWithOffset(0, -currentTop);
                }
            }
            if (ChatAttachAlertContactsLayout.this.searchAdapter != null) {
                ChatAttachAlertContactsLayout.this.searchAdapter.search(str);
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            ChatAttachAlertContactsLayout.this.parentAlert.makeFocusable(getSearchEditText(), true);
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.SearchField
        public void processTouchEvent(MotionEvent motionEvent) {
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setLocation(obtain.getRawX(), (obtain.getRawY() - ChatAttachAlertContactsLayout.this.parentAlert.getSheetContainer().getTranslationY()) - AndroidUtilities.dp(58.0f));
            ChatAttachAlertContactsLayout.this.listView.dispatchTouchEvent(obtain);
            obtain.recycle();
        }

        @Override // org.telegram.ui.Components.SearchField
        protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
            ChatAttachAlertContactsLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            ChatAttachAlertContactsLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return f2 >= ((float) ((ChatAttachAlertContactsLayout.this.parentAlert.scrollOffsetY[0] + AndroidUtilities.dp(30.0f)) + ((Build.VERSION.SDK_INT < 21 || ChatAttachAlertContactsLayout.this.parentAlert.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight)));
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends FillLastLinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, int i, boolean z, int i2, RecyclerView recyclerView) {
            super(context, i, z, i2, recyclerView);
            ChatAttachAlertContactsLayout.this = r7;
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends LinearSmoothScroller {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                AnonymousClass3.this = r1;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateDyToMakeVisible(View view, int i) {
                return super.calculateDyToMakeVisible(view, i) - (ChatAttachAlertContactsLayout.this.listView.getPaddingTop() - AndroidUtilities.dp(8.0f));
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateTimeForDeceleration(int i) {
                return super.calculateTimeForDeceleration(i) * 2;
            }
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(recyclerView.getContext());
            anonymousClass1.setTargetPosition(i);
            startSmoothScroll(anonymousClass1);
        }
    }

    public /* synthetic */ void lambda$new$1(Theme.ResourcesProvider resourcesProvider, View view, int i) {
        Object obj;
        String str;
        String str2;
        ContactsController.Contact contact;
        String str3;
        String str4;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        ShareSearchAdapter shareSearchAdapter = this.searchAdapter;
        if (adapter == shareSearchAdapter) {
            obj = shareSearchAdapter.getItem(i);
        } else {
            int sectionForPosition = this.listAdapter.getSectionForPosition(i);
            int positionInSectionForPosition = this.listAdapter.getPositionInSectionForPosition(i);
            if (positionInSectionForPosition < 0 || sectionForPosition < 0) {
                return;
            }
            obj = this.listAdapter.getItem(sectionForPosition, positionInSectionForPosition);
        }
        if (obj != null) {
            if (obj instanceof ContactsController.Contact) {
                ContactsController.Contact contact2 = (ContactsController.Contact) obj;
                TLRPC$User tLRPC$User = contact2.user;
                if (tLRPC$User != null) {
                    str3 = tLRPC$User.first_name;
                    str4 = tLRPC$User.last_name;
                } else {
                    str3 = contact2.first_name;
                    str4 = contact2.last_name;
                }
                contact = contact2;
                str = str4;
                str2 = str3;
            } else {
                TLRPC$User tLRPC$User2 = (TLRPC$User) obj;
                ContactsController.Contact contact3 = new ContactsController.Contact();
                String str5 = tLRPC$User2.first_name;
                contact3.first_name = str5;
                String str6 = tLRPC$User2.last_name;
                contact3.last_name = str6;
                contact3.phones.add(tLRPC$User2.phone);
                contact3.user = tLRPC$User2;
                contact = contact3;
                str2 = str5;
                str = str6;
            }
            PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(this.parentAlert.baseFragment, contact, null, null, null, str2, str, resourcesProvider);
            phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout$$ExternalSyntheticLambda1(this));
            phonebookShareAlert.show();
        }
    }

    public /* synthetic */ void lambda$new$0(TLRPC$User tLRPC$User, boolean z, int i) {
        this.parentAlert.dismiss(true);
        this.delegate.didSelectContact(tLRPC$User, z, i);
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends RecyclerView.OnScrollListener {
        AnonymousClass4() {
            ChatAttachAlertContactsLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            ChatAttachAlertContactsLayout chatAttachAlertContactsLayout = ChatAttachAlertContactsLayout.this;
            chatAttachAlertContactsLayout.parentAlert.updateLayout(chatAttachAlertContactsLayout, true, i2);
            ChatAttachAlertContactsLayout.this.updateEmptyViewPosition();
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void scrollToTop() {
        this.listView.smoothScrollToPosition(0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            return Integer.MAX_VALUE;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(8.0f);
        int i = (top <= 0 || holder == null || holder.getAdapterPosition() != 0) ? 0 : top;
        if (top >= 0 && holder != null && holder.getAdapterPosition() == 0) {
            runShadowAnimation(false);
        } else {
            runShadowAnimation(true);
            top = i;
        }
        this.frameLayout.setTranslationY(top);
        return top + AndroidUtilities.dp(12.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(4.0f);
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onPreMeasure(int i, int i2) {
        int i3;
        if (this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            i3 = AndroidUtilities.dp(8.0f);
            this.parentAlert.setAllowNestedScroll(false);
        } else {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i3 = (int) (i2 / 3.5f);
                    this.parentAlert.setAllowNestedScroll(true);
                }
            }
            i3 = (i2 / 5) * 2;
            this.parentAlert.setAllowNestedScroll(true);
        }
        if (this.listView.getPaddingTop() != i3) {
            this.ignoreLayout = true;
            this.listView.setPadding(0, i3, 0, 0);
            this.ignoreLayout = false;
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    private void runShadowAnimation(boolean z) {
        if ((!z || this.shadow.getTag() == null) && (z || this.shadow.getTag() != null)) {
            return;
        }
        this.shadow.setTag(z ? null : 1);
        if (z) {
            this.shadow.setVisibility(0);
        }
        AnimatorSet animatorSet = this.shadowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.shadowAnimation = animatorSet2;
        Animator[] animatorArr = new Animator[1];
        View view = this.shadow;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
        animatorSet2.playTogether(animatorArr);
        this.shadowAnimation.setDuration(150L);
        this.shadowAnimation.addListener(new AnonymousClass5(z));
        this.shadowAnimation.start();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertContactsLayout$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass5(boolean z) {
            ChatAttachAlertContactsLayout.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChatAttachAlertContactsLayout.this.shadowAnimation == null || !ChatAttachAlertContactsLayout.this.shadowAnimation.equals(animator)) {
                return;
            }
            if (!this.val$show) {
                ChatAttachAlertContactsLayout.this.shadow.setVisibility(4);
            }
            ChatAttachAlertContactsLayout.this.shadowAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (ChatAttachAlertContactsLayout.this.shadowAnimation == null || !ChatAttachAlertContactsLayout.this.shadowAnimation.equals(animator)) {
                return;
            }
            ChatAttachAlertContactsLayout.this.shadowAnimation = null;
        }
    }

    public int getCurrentTop() {
        if (this.listView.getChildCount() != 0) {
            int i = 0;
            View childAt = this.listView.getChildAt(0);
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
            if (holder == null) {
                return -1000;
            }
            int paddingTop = this.listView.getPaddingTop();
            if (holder.getAdapterPosition() == 0 && childAt.getTop() >= 0) {
                i = childAt.getTop();
            }
            return paddingTop - i;
        }
        return -1000;
    }

    public void setDelegate(PhonebookShareAlertDelegate phonebookShareAlertDelegate) {
        this.delegate = phonebookShareAlertDelegate;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ShareAdapter shareAdapter;
        if (i != NotificationCenter.contactsDidLoad || (shareAdapter = this.listAdapter) == null) {
            return;
        }
        shareAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onDestroy() {
        NotificationCenter.getInstance(this.parentAlert.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateEmptyViewPosition();
    }

    public void updateEmptyViewPosition() {
        View childAt;
        if (this.emptyView.getVisibility() == 0 && (childAt = this.listView.getChildAt(0)) != null) {
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            emptyTextProgressView.setTranslationY(((emptyTextProgressView.getMeasuredHeight() - getMeasuredHeight()) + childAt.getTop()) / 2);
        }
    }

    public void updateEmptyView() {
        int i = 0;
        boolean z = this.listView.getAdapter().getItemCount() == 2;
        EmptyTextProgressView emptyTextProgressView = this.emptyView;
        if (!z) {
            i = 8;
        }
        emptyTextProgressView.setVisibility(i);
        updateEmptyViewPosition();
    }

    /* loaded from: classes3.dex */
    public class ShareAdapter extends RecyclerListView.SectionsAdapter {
        private int currentAccount = UserConfig.selectedAccount;
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public View getSectionHeaderView(int i, View view) {
            return null;
        }

        public ShareAdapter(Context context) {
            ChatAttachAlertContactsLayout.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public Object getItem(int i, int i2) {
            if (i == 0) {
                return null;
            }
            int i3 = i - 1;
            HashMap<String, ArrayList<Object>> hashMap = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
            ArrayList<String> arrayList = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
            if (i3 < arrayList.size()) {
                ArrayList<Object> arrayList2 = hashMap.get(arrayList.get(i3));
                if (i2 < arrayList2.size()) {
                    return arrayList2.get(i2);
                }
            }
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder, int i, int i2) {
            if (i == 0 || i == getSectionCount() - 1) {
                return false;
            }
            return i2 < ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(i + (-1))).size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getSectionCount() {
            return ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.size() + 2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getCountForSection(int i) {
            if (i == 0 || i == getSectionCount() - 1) {
                return 1;
            }
            int i2 = i - 1;
            HashMap<String, ArrayList<Object>> hashMap = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
            ArrayList<String> arrayList = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
            if (i2 >= arrayList.size()) {
                return 0;
            }
            return hashMap.get(arrayList.get(i2)).size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new UserCell(this.mContext, ChatAttachAlertContactsLayout.this.resourcesProvider);
            } else if (i == 1) {
                view = new View(this.mContext);
                view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            } else {
                view = new View(this.mContext);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public void onBindViewHolder(int i, int i2, RecyclerView.ViewHolder viewHolder) {
            TLRPC$User tLRPC$User;
            if (viewHolder.getItemViewType() == 0) {
                UserCell userCell = (UserCell) viewHolder.itemView;
                Object item = getItem(i, i2);
                boolean z = true;
                if (i == getSectionCount() - 2 && i2 == getCountForSection(i) - 1) {
                    z = false;
                }
                if (item instanceof ContactsController.Contact) {
                    ContactsController.Contact contact = (ContactsController.Contact) item;
                    tLRPC$User = contact.user;
                    if (tLRPC$User == null) {
                        userCell.setCurrentId(contact.contact_id);
                        userCell.setData((TLRPC$User) null, ContactsController.formatName(contact.first_name, contact.last_name), new ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda0(contact), z);
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$User = (TLRPC$User) item;
                }
                if (tLRPC$User == null) {
                    return;
                }
                userCell.setData(tLRPC$User, (CharSequence) null, new ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda1(tLRPC$User), z);
            }
        }

        public static /* synthetic */ CharSequence lambda$onBindViewHolder$0(ContactsController.Contact contact) {
            return contact.phones.isEmpty() ? "" : PhoneFormat.getInstance().format(contact.phones.get(0));
        }

        public static /* synthetic */ CharSequence lambda$onBindViewHolder$1(TLRPC$User tLRPC$User) {
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            return phoneFormat.format("+" + tLRPC$User.phone);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getItemViewType(int i, int i2) {
            if (i == 0) {
                return 1;
            }
            return i == getSectionCount() - 1 ? 2 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = 0;
            iArr[1] = 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            ChatAttachAlertContactsLayout.this.updateEmptyView();
        }
    }

    /* loaded from: classes3.dex */
    public class ShareSearchAdapter extends RecyclerListView.SelectionAdapter {
        private int lastSearchId;
        private Context mContext;
        private ArrayList<Object> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Runnable searchRunnable;

        public ShareSearchAdapter(Context context) {
            ChatAttachAlertContactsLayout.this = r1;
            this.mContext = context;
        }

        public void search(String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            int i = this.lastSearchId + 1;
            this.lastSearchId = i;
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1 chatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1 = new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1(this, str, i);
            this.searchRunnable = chatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1;
            dispatchQueue.postRunnable(chatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1, 300L);
        }

        /* renamed from: processSearch */
        public void lambda$search$0(String str, int i) {
            AndroidUtilities.runOnUIThread(new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda2(this, str, i));
        }

        public /* synthetic */ void lambda$processSearch$2(String str, int i) {
            int i2 = UserConfig.selectedAccount;
            Utilities.searchQueue.postRunnable(new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda3(this, str, new ArrayList(ContactsController.getInstance(i2).contactsBook.values()), new ArrayList(ContactsController.getInstance(i2).contacts), i2, i));
        }

        /* JADX WARN: Code restructure failed: missing block: B:35:0x00c4, code lost:
            if (r5.contains(" " + r0) == false) goto L36;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00e1, code lost:
            if (r6.contains(" " + r0) != false) goto L41;
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x012b, code lost:
            if (r12.contains(" " + r0) != false) goto L59;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0222, code lost:
            if (r6.contains(" " + r12) != false) goto L99;
         */
        /* JADX WARN: Removed duplicated region for block: B:106:0x0270 A[LOOP:3: B:84:0x01e8->B:106:0x0270, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0134 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:118:0x0234 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:72:0x018c A[LOOP:1: B:29:0x00a5->B:72:0x018c, LOOP_END] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$1(String str, ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
            String str2;
            String str3;
            char c;
            String str4;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                this.lastSearchId = -1;
                updateSearchResults(str, new ArrayList<>(), new ArrayList<>(), this.lastSearchId);
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i3 = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i3];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<Object> arrayList3 = new ArrayList<>();
            ArrayList<CharSequence> arrayList4 = new ArrayList<>();
            LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                ContactsController.Contact contact = (ContactsController.Contact) arrayList.get(i4);
                String lowerCase2 = ContactsController.formatName(contact.first_name, contact.last_name).toLowerCase();
                String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                TLRPC$User tLRPC$User = contact.user;
                if (tLRPC$User != null) {
                    str3 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).toLowerCase();
                    str2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                } else {
                    str3 = null;
                    str2 = null;
                }
                if (lowerCase2.equals(translitString2)) {
                    translitString2 = null;
                }
                int i5 = 0;
                char c2 = 0;
                while (true) {
                    if (i5 < i3) {
                        String str5 = strArr[i5];
                        if (str3 != null) {
                            if (!str3.startsWith(str5)) {
                            }
                            c = 1;
                            if (c != 0) {
                                if (c == 3) {
                                    arrayList4.add(AndroidUtilities.generateSearchName(contact.first_name, contact.last_name, str5));
                                } else if (c == 1) {
                                    TLRPC$User tLRPC$User2 = contact.user;
                                    arrayList4.add(AndroidUtilities.generateSearchName(tLRPC$User2.first_name, tLRPC$User2.last_name, str5));
                                } else {
                                    arrayList4.add(AndroidUtilities.generateSearchName("@" + contact.user.username, null, "@" + str5));
                                }
                                TLRPC$User tLRPC$User3 = contact.user;
                                if (tLRPC$User3 != null) {
                                    longSparseIntArray.put(tLRPC$User3.id, 1);
                                }
                                arrayList3.add(contact);
                            } else {
                                i5++;
                                c2 = c;
                            }
                        }
                        if (str2 != null) {
                            if (!str2.startsWith(str5)) {
                            }
                            c = 1;
                            if (c != 0) {
                            }
                        }
                        TLRPC$User tLRPC$User4 = contact.user;
                        if (tLRPC$User4 == null || (str4 = tLRPC$User4.username) == null || !str4.startsWith(str5)) {
                            if (!lowerCase2.startsWith(str5)) {
                                if (!lowerCase2.contains(" " + str5)) {
                                    if (translitString2 != null) {
                                        if (!translitString2.startsWith(str5)) {
                                        }
                                    }
                                    c = c2;
                                }
                            }
                            c = 3;
                        } else {
                            c = 2;
                        }
                        if (c != 0) {
                        }
                    }
                }
            }
            for (int i6 = 0; i6 < arrayList2.size(); i6++) {
                TLRPC$TL_contact tLRPC$TL_contact = (TLRPC$TL_contact) arrayList2.get(i6);
                if (longSparseIntArray.indexOfKey(tLRPC$TL_contact.user_id) < 0) {
                    TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_contact.user_id));
                    String lowerCase3 = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                    String translitString3 = LocaleController.getInstance().getTranslitString(lowerCase3);
                    if (lowerCase3.equals(translitString3)) {
                        translitString3 = null;
                    }
                    char c3 = 0;
                    for (int i7 = 0; i7 < i3; i7++) {
                        String str6 = strArr[i7];
                        if (!lowerCase3.startsWith(str6)) {
                            if (!lowerCase3.contains(" " + str6)) {
                                if (translitString3 != null) {
                                    if (!translitString3.startsWith(str6)) {
                                    }
                                }
                                String str7 = user.username;
                                if (str7 != null && str7.startsWith(str6)) {
                                    c3 = 2;
                                }
                                if (c3 == 0) {
                                    if (c3 == 1) {
                                        arrayList4.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, str6));
                                    } else {
                                        arrayList4.add(AndroidUtilities.generateSearchName("@" + user.username, null, "@" + str6));
                                    }
                                    arrayList3.add(user);
                                }
                            }
                        }
                        c3 = 1;
                        if (c3 == 0) {
                        }
                    }
                }
            }
            updateSearchResults(str, arrayList3, arrayList4, i2);
        }

        private void updateSearchResults(String str, ArrayList<Object> arrayList, ArrayList<CharSequence> arrayList2, int i) {
            AndroidUtilities.runOnUIThread(new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda0(this, i, arrayList, arrayList2));
        }

        public /* synthetic */ void lambda$updateSearchResults$3(int i, ArrayList arrayList, ArrayList arrayList2) {
            if (i != this.lastSearchId) {
                return;
            }
            if (i != -1 && ChatAttachAlertContactsLayout.this.listView.getAdapter() != ChatAttachAlertContactsLayout.this.searchAdapter) {
                ChatAttachAlertContactsLayout.this.listView.setAdapter(ChatAttachAlertContactsLayout.this.searchAdapter);
            }
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.searchResult.size() + 2;
        }

        public Object getItem(int i) {
            int i2 = i - 1;
            if (i2 < 0 || i2 >= this.searchResult.size()) {
                return null;
            }
            return this.searchResult.get(i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new UserCell(this.mContext, ChatAttachAlertContactsLayout.this.resourcesProvider);
            } else if (i == 1) {
                view = new View(this.mContext);
                view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            } else {
                view = new View(this.mContext);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$User tLRPC$User;
            if (viewHolder.getItemViewType() == 0) {
                UserCell userCell = (UserCell) viewHolder.itemView;
                boolean z = i != getItemCount() + (-2);
                Object item = getItem(i);
                if (item instanceof ContactsController.Contact) {
                    ContactsController.Contact contact = (ContactsController.Contact) item;
                    tLRPC$User = contact.user;
                    if (tLRPC$User == null) {
                        userCell.setCurrentId(contact.contact_id);
                        userCell.setData((TLRPC$User) null, this.searchResultNames.get(i - 1), new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda4(contact), z);
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$User = (TLRPC$User) item;
                }
                if (tLRPC$User == null) {
                    return;
                }
                userCell.setData(tLRPC$User, this.searchResultNames.get(i - 1), new ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda5(tLRPC$User), z);
            }
        }

        public static /* synthetic */ CharSequence lambda$onBindViewHolder$4(ContactsController.Contact contact) {
            return contact.phones.isEmpty() ? "" : PhoneFormat.getInstance().format(contact.phones.get(0));
        }

        public static /* synthetic */ CharSequence lambda$onBindViewHolder$5(TLRPC$User tLRPC$User) {
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            return phoneFormat.format("+" + tLRPC$User.phone);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 1;
            }
            return i == getItemCount() - 1 ? 2 : 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            ChatAttachAlertContactsLayout.this.updateEmptyView();
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ChatAttachAlertContactsLayout$$ExternalSyntheticLambda0 chatAttachAlertContactsLayout$$ExternalSyntheticLambda0 = new ChatAttachAlertContactsLayout$$ExternalSyntheticLambda0(this);
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.frameLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.shadow, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogShadowLine"));
        arrayList.add(new ThemeDescription(this.searchField.getSearchBackground(), ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "dialogSearchBackground"));
        arrayList.add(new ThemeDescription(this.searchField, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SearchField.class}, new String[]{"searchIconImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogSearchIcon"));
        arrayList.add(new ThemeDescription(this.searchField, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SearchField.class}, new String[]{"clearSearchImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogSearchIcon"));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogSearchText"));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "dialogSearchHint"));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "featuredStickers_addedIcon"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "dialogScrollGlow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogTextGray2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "dialogTextGray2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertContactsLayout$$ExternalSyntheticLambda0, "avatar_backgroundPink"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$2() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }
}
