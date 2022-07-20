package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.InviteTextCell;
import org.telegram.ui.Cells.InviteUserCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class InviteContactsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, View.OnClickListener {
    private InviteAdapter adapter;
    private int containerHeight;
    private TextView counterTextView;
    private FrameLayout counterView;
    private GroupCreateSpan currentDeletingSpan;
    private GroupCreateDividerItemDecoration decoration;
    private EditTextBoldCursor editText;
    private EmptyTextProgressView emptyView;
    private int fieldY;
    private boolean ignoreScrollEvent;
    private TextView infoTextView;
    private RecyclerListView listView;
    private ArrayList<ContactsController.Contact> phoneBookContacts;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private SpansContainer spansContainer;
    private TextView textView;
    private HashMap<String, GroupCreateSpan> selectedContacts = new HashMap<>();
    private ArrayList<GroupCreateSpan> allSpans = new ArrayList<>();

    /* loaded from: classes3.dex */
    public class SpansContainer extends ViewGroup {
        private View addingSpan;
        private boolean animationStarted;
        private ArrayList<Animator> animators = new ArrayList<>();
        private AnimatorSet currentAnimation;
        private View removingSpan;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SpansContainer(Context context) {
            super(context);
            InviteContactsActivity.this = r1;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            boolean z;
            float f;
            float f2;
            char c;
            int i4;
            int childCount = getChildCount();
            int size = View.MeasureSpec.getSize(i);
            float f3 = 32.0f;
            int dp = size - AndroidUtilities.dp(32.0f);
            int dp2 = AndroidUtilities.dp(12.0f);
            int dp3 = AndroidUtilities.dp(12.0f);
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            while (i5 < childCount) {
                View childAt = getChildAt(i5);
                if (childAt instanceof GroupCreateSpan) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(f3), 1073741824));
                    if (childAt == this.removingSpan || childAt.getMeasuredWidth() + i6 <= dp) {
                        f = 12.0f;
                    } else {
                        f = 12.0f;
                        dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(12.0f);
                        i6 = 0;
                    }
                    if (childAt.getMeasuredWidth() + i7 > dp) {
                        dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(f);
                        f2 = 16.0f;
                        i7 = 0;
                    } else {
                        f2 = 16.0f;
                    }
                    int dp4 = AndroidUtilities.dp(f2) + i6;
                    if (!this.animationStarted) {
                        View view = this.removingSpan;
                        if (childAt == view) {
                            childAt.setTranslationX(AndroidUtilities.dp(f2) + i7);
                            childAt.setTranslationY(dp3);
                        } else if (view != null) {
                            float f4 = dp4;
                            if (childAt.getTranslationX() != f4) {
                                i4 = 1;
                                c = 0;
                                this.animators.add(ObjectAnimator.ofFloat(childAt, "translationX", f4));
                            } else {
                                i4 = 1;
                                c = 0;
                            }
                            float f5 = dp2;
                            if (childAt.getTranslationY() != f5) {
                                ArrayList<Animator> arrayList = this.animators;
                                float[] fArr = new float[i4];
                                fArr[c] = f5;
                                arrayList.add(ObjectAnimator.ofFloat(childAt, "translationY", fArr));
                            }
                        } else {
                            childAt.setTranslationX(dp4);
                            childAt.setTranslationY(dp2);
                        }
                    }
                    if (childAt != this.removingSpan) {
                        i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    }
                    i7 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                }
                i5++;
                f3 = 32.0f;
            }
            if (AndroidUtilities.isTablet()) {
                i3 = AndroidUtilities.dp(366.0f) / 3;
            } else {
                Point point = AndroidUtilities.displaySize;
                i3 = (Math.min(point.x, point.y) - AndroidUtilities.dp(164.0f)) / 3;
            }
            if (dp - i6 < i3) {
                dp2 += AndroidUtilities.dp(44.0f);
                i6 = 0;
            }
            if (dp - i7 < i3) {
                dp3 += AndroidUtilities.dp(44.0f);
            }
            InviteContactsActivity.this.editText.measure(View.MeasureSpec.makeMeasureSpec(dp - i6, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            if (!this.animationStarted) {
                int dp5 = dp3 + AndroidUtilities.dp(44.0f);
                int dp6 = i6 + AndroidUtilities.dp(16.0f);
                InviteContactsActivity.this.fieldY = dp2;
                if (this.currentAnimation == null) {
                    InviteContactsActivity.this.containerHeight = dp5;
                    InviteContactsActivity.this.editText.setTranslationX(dp6);
                    InviteContactsActivity.this.editText.setTranslationY(InviteContactsActivity.this.fieldY);
                } else {
                    int dp7 = dp2 + AndroidUtilities.dp(44.0f);
                    if (InviteContactsActivity.this.containerHeight != dp7) {
                        this.animators.add(ObjectAnimator.ofInt(InviteContactsActivity.this, "containerHeight", dp7));
                    }
                    float f6 = dp6;
                    if (InviteContactsActivity.this.editText.getTranslationX() != f6) {
                        this.animators.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationX", f6));
                    }
                    if (InviteContactsActivity.this.editText.getTranslationY() != InviteContactsActivity.this.fieldY) {
                        z = false;
                        this.animators.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationY", InviteContactsActivity.this.fieldY));
                    } else {
                        z = false;
                    }
                    InviteContactsActivity.this.editText.setAllowDrawCursor(z);
                    this.currentAnimation.playTogether(this.animators);
                    this.currentAnimation.start();
                    this.animationStarted = true;
                }
            } else if (this.currentAnimation != null && !InviteContactsActivity.this.ignoreScrollEvent && this.removingSpan == null) {
                InviteContactsActivity.this.editText.bringPointIntoView(InviteContactsActivity.this.editText.getSelectionStart());
            }
            setMeasuredDimension(size, InviteContactsActivity.this.containerHeight);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        public void addSpan(GroupCreateSpan groupCreateSpan) {
            InviteContactsActivity.this.allSpans.add(groupCreateSpan);
            InviteContactsActivity.this.selectedContacts.put(groupCreateSpan.getKey(), groupCreateSpan);
            InviteContactsActivity.this.editText.setHintVisible(false);
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.currentAnimation = animatorSet2;
            animatorSet2.addListener(new AnonymousClass1());
            this.currentAnimation.setDuration(150L);
            this.addingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleX", 0.01f, 1.0f));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleY", 0.01f, 1.0f));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "alpha", 0.0f, 1.0f));
            addView(groupCreateSpan);
        }

        /* renamed from: org.telegram.ui.InviteContactsActivity$SpansContainer$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                SpansContainer.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SpansContainer.this.addingSpan = null;
                SpansContainer.this.currentAnimation = null;
                SpansContainer.this.animationStarted = false;
                InviteContactsActivity.this.editText.setAllowDrawCursor(true);
            }
        }

        public void removeSpan(GroupCreateSpan groupCreateSpan) {
            InviteContactsActivity.this.ignoreScrollEvent = true;
            InviteContactsActivity.this.selectedContacts.remove(groupCreateSpan.getKey());
            InviteContactsActivity.this.allSpans.remove(groupCreateSpan);
            groupCreateSpan.setOnClickListener(null);
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.currentAnimation = animatorSet2;
            animatorSet2.addListener(new AnonymousClass2(groupCreateSpan));
            this.currentAnimation.setDuration(150L);
            this.removingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleX", 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleY", 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "alpha", 1.0f, 0.0f));
            requestLayout();
        }

        /* renamed from: org.telegram.ui.InviteContactsActivity$SpansContainer$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ GroupCreateSpan val$span;

            AnonymousClass2(GroupCreateSpan groupCreateSpan) {
                SpansContainer.this = r1;
                this.val$span = groupCreateSpan;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SpansContainer.this.removeView(this.val$span);
                SpansContainer.this.removingSpan = null;
                SpansContainer.this.currentAnimation = null;
                SpansContainer.this.animationStarted = false;
                InviteContactsActivity.this.editText.setAllowDrawCursor(true);
                if (InviteContactsActivity.this.allSpans.isEmpty()) {
                    InviteContactsActivity.this.editText.setHintVisible(true);
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsImported);
        fetchContacts();
        if (!UserConfig.getInstance(this.currentAccount).contactsReimported) {
            ContactsController.getInstance(this.currentAccount).forceImportContacts();
            UserConfig.getInstance(this.currentAccount).contactsReimported = true;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsImported);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
        if (groupCreateSpan.isDeleting()) {
            this.currentDeletingSpan = null;
            this.spansContainer.removeSpan(groupCreateSpan);
            updateHint();
            checkVisibleRows();
            return;
        }
        GroupCreateSpan groupCreateSpan2 = this.currentDeletingSpan;
        if (groupCreateSpan2 != null) {
            groupCreateSpan2.cancelDeleteAnimation();
        }
        this.currentDeletingSpan = groupCreateSpan;
        groupCreateSpan.startDeleteAnimation();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("InviteFriends", 2131626308));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.fragmentView = anonymousClass2;
        AnonymousClass2 anonymousClass22 = anonymousClass2;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.scrollView = anonymousClass3;
        anonymousClass3.setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("windowBackgroundWhite"));
        anonymousClass22.addView(this.scrollView);
        SpansContainer spansContainer = new SpansContainer(context);
        this.spansContainer = spansContainer;
        this.scrollView.addView(spansContainer, LayoutHelper.createFrame(-1, -2.0f));
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
        this.editText = anonymousClass4;
        anonymousClass4.setTextSize(1, 18.0f);
        this.editText.setHintColor(Theme.getColor("groupcreate_hintText"));
        this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.editText.setCursorColor(Theme.getColor("groupcreate_cursor"));
        this.editText.setCursorWidth(1.5f);
        this.editText.setInputType(655536);
        this.editText.setSingleLine(true);
        this.editText.setBackgroundDrawable(null);
        this.editText.setVerticalScrollBarEnabled(false);
        this.editText.setHorizontalScrollBarEnabled(false);
        this.editText.setTextIsSelectable(false);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setImeOptions(268435462);
        this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.spansContainer.addView(this.editText);
        this.editText.setHintText(LocaleController.getString("SearchFriends", 2131628171));
        this.editText.setCustomSelectionActionModeCallback(new AnonymousClass5(this));
        this.editText.setOnKeyListener(new AnonymousClass6());
        this.editText.addTextChangedListener(new AnonymousClass7());
        this.emptyView = new EmptyTextProgressView(context);
        if (ContactsController.getInstance(this.currentAccount).isLoadingContacts()) {
            this.emptyView.showProgress();
        } else {
            this.emptyView.showTextView();
        }
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131626867));
        anonymousClass22.addView(this.emptyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView2 = this.listView;
        InviteAdapter inviteAdapter = new InviteAdapter(context);
        this.adapter = inviteAdapter;
        recyclerListView2.setAdapter(inviteAdapter);
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(true);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        RecyclerListView recyclerListView3 = this.listView;
        GroupCreateDividerItemDecoration groupCreateDividerItemDecoration = new GroupCreateDividerItemDecoration();
        this.decoration = groupCreateDividerItemDecoration;
        recyclerListView3.addItemDecoration(groupCreateDividerItemDecoration);
        anonymousClass22.addView(this.listView);
        this.listView.setOnItemClickListener(new InviteContactsActivity$$ExternalSyntheticLambda3(this));
        this.listView.setOnScrollListener(new AnonymousClass8());
        TextView textView = new TextView(context);
        this.infoTextView = textView;
        textView.setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
        this.infoTextView.setTextColor(Theme.getColor("contacts_inviteText"));
        this.infoTextView.setGravity(17);
        this.infoTextView.setText(LocaleController.getString("InviteFriendsHelp", 2131626309));
        this.infoTextView.setTextSize(1, 13.0f);
        this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.infoTextView.setPadding(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f), AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f));
        anonymousClass22.addView(this.infoTextView, LayoutHelper.createFrame(-1, -2, 83));
        FrameLayout frameLayout = new FrameLayout(context);
        this.counterView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
        this.counterView.setVisibility(4);
        anonymousClass22.addView(this.counterView, LayoutHelper.createFrame(-1, 48, 83));
        this.counterView.setOnClickListener(new InviteContactsActivity$$ExternalSyntheticLambda0(this));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        this.counterView.addView(linearLayout, LayoutHelper.createFrame(-2, -1, 17));
        TextView textView2 = new TextView(context);
        this.counterTextView = textView2;
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.counterTextView.setTextSize(1, 14.0f);
        this.counterTextView.setTextColor(Theme.getColor("contacts_inviteBackground"));
        this.counterTextView.setGravity(17);
        this.counterTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), Theme.getColor("contacts_inviteText")));
        this.counterTextView.setMinWidth(AndroidUtilities.dp(20.0f));
        this.counterTextView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(1.0f));
        linearLayout.addView(this.counterTextView, LayoutHelper.createLinear(-2, 20, 16, 0, 0, 10, 0));
        TextView textView3 = new TextView(context);
        this.textView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.textView.setTextColor(Theme.getColor("contacts_inviteText"));
        this.textView.setGravity(17);
        this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.textView.setText(LocaleController.getString("InviteToTelegram", 2131626337).toUpperCase());
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView(this.textView, LayoutHelper.createLinear(-2, -2, 16));
        updateHint();
        this.adapter.notifyDataSetChanged();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            InviteContactsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                InviteContactsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ViewGroup {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            InviteContactsActivity.this = r1;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            if (AndroidUtilities.isTablet() || size2 > size) {
                i3 = AndroidUtilities.dp(144.0f);
            } else {
                i3 = AndroidUtilities.dp(56.0f);
            }
            InviteContactsActivity.this.infoTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
            InviteContactsActivity.this.counterView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
            int measuredHeight = InviteContactsActivity.this.infoTextView.getVisibility() == 0 ? InviteContactsActivity.this.infoTextView.getMeasuredHeight() : InviteContactsActivity.this.counterView.getMeasuredHeight();
            InviteContactsActivity.this.scrollView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
            InviteContactsActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((size2 - InviteContactsActivity.this.scrollView.getMeasuredHeight()) - measuredHeight, 1073741824));
            InviteContactsActivity.this.emptyView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((size2 - InviteContactsActivity.this.scrollView.getMeasuredHeight()) - AndroidUtilities.dp(72.0f), 1073741824));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            InviteContactsActivity.this.scrollView.layout(0, 0, InviteContactsActivity.this.scrollView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight());
            InviteContactsActivity.this.listView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight(), InviteContactsActivity.this.listView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.listView.getMeasuredHeight());
            InviteContactsActivity.this.emptyView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(72.0f), InviteContactsActivity.this.emptyView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.emptyView.getMeasuredHeight());
            int i5 = i4 - i2;
            int measuredHeight = i5 - InviteContactsActivity.this.infoTextView.getMeasuredHeight();
            InviteContactsActivity.this.infoTextView.layout(0, measuredHeight, InviteContactsActivity.this.infoTextView.getMeasuredWidth(), InviteContactsActivity.this.infoTextView.getMeasuredHeight() + measuredHeight);
            int measuredHeight2 = i5 - InviteContactsActivity.this.counterView.getMeasuredHeight();
            InviteContactsActivity.this.counterView.layout(0, measuredHeight2, InviteContactsActivity.this.counterView.getMeasuredWidth(), InviteContactsActivity.this.counterView.getMeasuredHeight() + measuredHeight2);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == InviteContactsActivity.this.listView || view == InviteContactsActivity.this.emptyView) {
                ((BaseFragment) InviteContactsActivity.this).parentLayout.drawHeaderShadow(canvas, InviteContactsActivity.this.scrollView.getMeasuredHeight());
            }
            return drawChild;
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends ScrollView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            InviteContactsActivity.this = r1;
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            if (InviteContactsActivity.this.ignoreScrollEvent) {
                InviteContactsActivity.this.ignoreScrollEvent = false;
                return false;
            }
            rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
            rect.top += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(20.0f);
            rect.bottom += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(50.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends EditTextBoldCursor {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            InviteContactsActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (InviteContactsActivity.this.currentDeletingSpan != null) {
                InviteContactsActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                InviteContactsActivity.this.currentDeletingSpan = null;
            }
            if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                clearFocus();
                requestFocus();
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 implements ActionMode.Callback {
        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        AnonymousClass5(InviteContactsActivity inviteContactsActivity) {
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements View.OnKeyListener {
        private boolean wasEmpty;

        AnonymousClass6() {
            InviteContactsActivity.this = r1;
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            boolean z = true;
            if (keyEvent.getAction() == 0) {
                if (InviteContactsActivity.this.editText.length() != 0) {
                    z = false;
                }
                this.wasEmpty = z;
            } else if (keyEvent.getAction() == 1 && this.wasEmpty && !InviteContactsActivity.this.allSpans.isEmpty()) {
                InviteContactsActivity.this.spansContainer.removeSpan((GroupCreateSpan) InviteContactsActivity.this.allSpans.get(InviteContactsActivity.this.allSpans.size() - 1));
                InviteContactsActivity.this.updateHint();
                InviteContactsActivity.this.checkVisibleRows();
                return true;
            }
            return false;
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass7() {
            InviteContactsActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (InviteContactsActivity.this.editText.length() != 0) {
                InviteContactsActivity.this.searching = true;
                InviteContactsActivity.this.searchWas = true;
                InviteContactsActivity.this.adapter.setSearching(true);
                InviteContactsActivity.this.adapter.searchDialogs(InviteContactsActivity.this.editText.getText().toString());
                InviteContactsActivity.this.listView.setFastScrollVisible(false);
                InviteContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                InviteContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131626910));
                return;
            }
            InviteContactsActivity.this.closeSearch();
        }
    }

    public /* synthetic */ void lambda$createView$0(View view, int i) {
        InviteUserCell inviteUserCell;
        ContactsController.Contact contact;
        if (i == 0 && !this.searching) {
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                String inviteText = ContactsController.getInstance(this.currentAccount).getInviteText(0);
                intent.putExtra("android.intent.extra.TEXT", inviteText);
                getParentActivity().startActivityForResult(Intent.createChooser(intent, inviteText), 500);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (!(view instanceof InviteUserCell) || (contact = (inviteUserCell = (InviteUserCell) view).getContact()) == null) {
        } else {
            boolean containsKey = this.selectedContacts.containsKey(contact.key);
            if (containsKey) {
                this.spansContainer.removeSpan(this.selectedContacts.get(contact.key));
            } else {
                GroupCreateSpan groupCreateSpan = new GroupCreateSpan(this.editText.getContext(), contact);
                this.spansContainer.addSpan(groupCreateSpan);
                groupCreateSpan.setOnClickListener(this);
            }
            updateHint();
            if (this.searching || this.searchWas) {
                AndroidUtilities.showKeyboard(this.editText);
            } else {
                inviteUserCell.setChecked(!containsKey, true);
            }
            if (this.editText.length() <= 0) {
                return;
            }
            this.editText.setText((CharSequence) null);
        }
    }

    /* renamed from: org.telegram.ui.InviteContactsActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends RecyclerView.OnScrollListener {
        AnonymousClass8() {
            InviteContactsActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(InviteContactsActivity.this.editText);
            }
        }
    }

    public /* synthetic */ void lambda$createView$1(View view) {
        try {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (int i2 = 0; i2 < this.allSpans.size(); i2++) {
                ContactsController.Contact contact = this.allSpans.get(i2).getContact();
                if (sb.length() != 0) {
                    sb.append(';');
                }
                sb.append(contact.phones.get(0));
                if (i2 == 0 && this.allSpans.size() == 1) {
                    i = contact.imported;
                }
            }
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + sb.toString()));
            intent.putExtra("sms_body", ContactsController.getInstance(this.currentAccount).getInviteText(i));
            getParentActivity().startActivityForResult(intent, 500);
        } catch (Exception e) {
            FileLog.e(e);
        }
        finishFragment();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        EditTextBoldCursor editTextBoldCursor = this.editText;
        if (editTextBoldCursor != null) {
            editTextBoldCursor.requestFocus();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.contactsImported) {
            fetchContacts();
        }
    }

    @Keep
    public void setContainerHeight(int i) {
        this.containerHeight = i;
        SpansContainer spansContainer = this.spansContainer;
        if (spansContainer != null) {
            spansContainer.requestLayout();
        }
    }

    @Keep
    public int getContainerHeight() {
        return this.containerHeight;
    }

    public void checkVisibleRows() {
        InviteUserCell inviteUserCell;
        ContactsController.Contact contact;
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if ((childAt instanceof InviteUserCell) && (contact = (inviteUserCell = (InviteUserCell) childAt).getContact()) != null) {
                inviteUserCell.setChecked(this.selectedContacts.containsKey(contact.key), true);
            }
        }
    }

    public void updateHint() {
        if (this.selectedContacts.isEmpty()) {
            this.infoTextView.setVisibility(0);
            this.counterView.setVisibility(4);
            return;
        }
        this.infoTextView.setVisibility(4);
        this.counterView.setVisibility(0);
        this.counterTextView.setText(String.format("%d", Integer.valueOf(this.selectedContacts.size())));
    }

    public void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131626867));
    }

    private void fetchContacts() {
        ArrayList<ContactsController.Contact> arrayList = new ArrayList<>(ContactsController.getInstance(this.currentAccount).phoneBookContacts);
        this.phoneBookContacts = arrayList;
        Collections.sort(arrayList, InviteContactsActivity$$ExternalSyntheticLambda1.INSTANCE);
        EmptyTextProgressView emptyTextProgressView = this.emptyView;
        if (emptyTextProgressView != null) {
            emptyTextProgressView.showTextView();
        }
        InviteAdapter inviteAdapter = this.adapter;
        if (inviteAdapter != null) {
            inviteAdapter.notifyDataSetChanged();
        }
    }

    public static /* synthetic */ int lambda$fetchContacts$2(ContactsController.Contact contact, ContactsController.Contact contact2) {
        int i = contact.imported;
        int i2 = contact2.imported;
        if (i > i2) {
            return -1;
        }
        return i < i2 ? 1 : 0;
    }

    /* loaded from: classes3.dex */
    public class InviteAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private ArrayList<ContactsController.Contact> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Timer searchTimer;
        private boolean searching;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public InviteAdapter(Context context) {
            InviteContactsActivity.this = r1;
            this.context = context;
        }

        public void setSearching(boolean z) {
            if (this.searching == z) {
                return;
            }
            this.searching = z;
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (!this.searching) {
                return InviteContactsActivity.this.phoneBookContacts.size() + 1;
            }
            return this.searchResult.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            InviteUserCell inviteUserCell;
            if (i == 1) {
                InviteTextCell inviteTextCell = new InviteTextCell(this.context);
                inviteTextCell.setTextAndIcon(LocaleController.getString("ShareTelegram", 2131628347), 2131166137);
                inviteUserCell = inviteTextCell;
            } else {
                inviteUserCell = new InviteUserCell(this.context, true);
            }
            return new RecyclerListView.Holder(inviteUserCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            CharSequence charSequence;
            ContactsController.Contact contact;
            if (viewHolder.getItemViewType() != 0) {
                return;
            }
            InviteUserCell inviteUserCell = (InviteUserCell) viewHolder.itemView;
            if (!this.searching) {
                contact = (ContactsController.Contact) InviteContactsActivity.this.phoneBookContacts.get(i - 1);
                charSequence = null;
            } else {
                contact = this.searchResult.get(i);
                charSequence = this.searchResultNames.get(i);
            }
            inviteUserCell.setUser(contact, charSequence);
            inviteUserCell.setChecked(InviteContactsActivity.this.selectedContacts.containsKey(contact.key), false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (this.searching || i != 0) ? 0 : 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof InviteUserCell) {
                ((InviteUserCell) view).recycle();
            }
        }

        public void searchDialogs(String str) {
            try {
                Timer timer = this.searchTimer;
                if (timer != null) {
                    timer.cancel();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (str == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            Timer timer2 = new Timer();
            this.searchTimer = timer2;
            timer2.schedule(new AnonymousClass1(str), 200L, 300L);
        }

        /* renamed from: org.telegram.ui.InviteContactsActivity$InviteAdapter$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends TimerTask {
            final /* synthetic */ String val$query;

            AnonymousClass1(String str) {
                InviteAdapter.this = r1;
                this.val$query = str;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    InviteAdapter.this.searchTimer.cancel();
                    InviteAdapter.this.searchTimer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
                AndroidUtilities.runOnUIThread(new InviteContactsActivity$InviteAdapter$1$$ExternalSyntheticLambda1(this, this.val$query));
            }

            public /* synthetic */ void lambda$run$1(String str) {
                Utilities.searchQueue.postRunnable(new InviteContactsActivity$InviteAdapter$1$$ExternalSyntheticLambda0(this, str));
            }

            /* JADX WARN: Code restructure failed: missing block: B:34:0x00c6, code lost:
                if (r11.contains(" " + r14) != false) goto L35;
             */
            /* JADX WARN: Removed duplicated region for block: B:38:0x00da A[LOOP:1: B:25:0x008a->B:38:0x00da, LOOP_END] */
            /* JADX WARN: Removed duplicated region for block: B:45:0x00cb A[SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public /* synthetic */ void lambda$run$0(String str) {
                String lowerCase = str.trim().toLowerCase();
                if (lowerCase.length() == 0) {
                    InviteAdapter.this.updateSearchResults(new ArrayList(), new ArrayList());
                    return;
                }
                String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                if (lowerCase.equals(translitString) || translitString.length() == 0) {
                    translitString = null;
                }
                int i = (translitString != null ? 1 : 0) + 1;
                String[] strArr = new String[i];
                strArr[0] = lowerCase;
                if (translitString != null) {
                    strArr[1] = translitString;
                }
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < InviteContactsActivity.this.phoneBookContacts.size(); i2++) {
                    ContactsController.Contact contact = (ContactsController.Contact) InviteContactsActivity.this.phoneBookContacts.get(i2);
                    String lowerCase2 = ContactsController.formatName(contact.first_name, contact.last_name).toLowerCase();
                    String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                    if (lowerCase2.equals(translitString2)) {
                        translitString2 = null;
                    }
                    int i3 = 0;
                    boolean z = false;
                    while (true) {
                        if (i3 < i) {
                            String str2 = strArr[i3];
                            if (!lowerCase2.startsWith(str2)) {
                                if (!lowerCase2.contains(" " + str2)) {
                                    if (translitString2 != null) {
                                        if (!translitString2.startsWith(str2)) {
                                        }
                                    }
                                    if (!z) {
                                        arrayList2.add(AndroidUtilities.generateSearchName(contact.first_name, contact.last_name, str2));
                                        arrayList.add(contact);
                                        break;
                                    }
                                    i3++;
                                }
                            }
                            z = true;
                            if (!z) {
                            }
                        }
                    }
                }
                InviteAdapter.this.updateSearchResults(arrayList, arrayList2);
            }
        }

        public void updateSearchResults(ArrayList<ContactsController.Contact> arrayList, ArrayList<CharSequence> arrayList2) {
            AndroidUtilities.runOnUIThread(new InviteContactsActivity$InviteAdapter$$ExternalSyntheticLambda0(this, arrayList, arrayList2));
        }

        public /* synthetic */ void lambda$updateSearchResults$0(ArrayList arrayList, ArrayList arrayList2) {
            if (!this.searching) {
                return;
            }
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            int itemCount = getItemCount();
            boolean z = false;
            InviteContactsActivity.this.emptyView.setVisibility(itemCount == 1 ? 0 : 4);
            GroupCreateDividerItemDecoration groupCreateDividerItemDecoration = InviteContactsActivity.this.decoration;
            if (itemCount == 1) {
                z = true;
            }
            groupCreateDividerItemDecoration.setSingle(z);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        InviteContactsActivity$$ExternalSyntheticLambda2 inviteContactsActivity$$ExternalSyntheticLambda2 = new InviteContactsActivity$$ExternalSyntheticLambda2(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "groupcreate_hintText"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "groupcreate_cursor"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GroupCreateSectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GroupCreateSectionCell.class}, new String[]{"drawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "groupcreate_sectionShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "groupcreate_sectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "groupcreate_sectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkbox"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{InviteUserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, inviteContactsActivity$$ExternalSyntheticLambda2, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, "groupcreate_spanBackground"));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, "groupcreate_spanText"));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, "groupcreate_spanDelete"));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteText"));
        arrayList.add(new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "contacts_inviteBackground"));
        arrayList.add(new ThemeDescription(this.counterView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "contacts_inviteBackground"));
        arrayList.add(new ThemeDescription(this.counterTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteBackground"));
        arrayList.add(new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteText"));
        arrayList.add(new ThemeDescription(this.counterTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "contacts_inviteText"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$3() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof InviteUserCell) {
                    ((InviteUserCell) childAt).update(0);
                }
            }
        }
    }
}
