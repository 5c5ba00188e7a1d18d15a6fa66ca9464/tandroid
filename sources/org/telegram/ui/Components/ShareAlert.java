package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.arch.core.util.Function;
import androidx.collection.LongSparseArray;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.Cells.ShareTopicCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageStatisticActivity;
import org.telegram.ui.PremiumPreviewFragment;

/* loaded from: classes3.dex */
public class ShareAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private AnimatorSet animatorSet;
    private FrameLayout bulletinContainer;
    public FrameLayout bulletinContainer2;
    private float captionEditTextTopOffset;
    private float chatActivityEnterViewAnimateFromTop;
    private EditTextEmoji commentTextView;
    private int containerViewTop;
    private boolean copyLinkOnEnd;
    private float currentPanTranslationY;
    private boolean darkTheme;
    private ShareAlertDelegate delegate;
    private TLRPC.TL_exportedMessageLink exportedMessageLink;
    public boolean forceDarkThemeForHint;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private boolean fullyShown;
    private RecyclerListView gridView;
    private int hasPoll;
    private boolean includeStory;
    public boolean includeStoryFromMessage;
    private boolean isChannel;
    int lastOffset;
    private GridLayoutManager layoutManager;
    private String[] linkToCopy;
    private ShareDialogsAdapter listAdapter;
    private boolean loadingLink;
    private Paint paint;
    private boolean panTranslationMoveLayout;
    private Activity parentActivity;
    private ChatActivity parentFragment;
    private TextView pickerBottomLayout;
    private int previousScrollOffsetY;
    private ArrayList recentSearchObjects;
    private LongSparseArray recentSearchObjectsById;
    private RectF rect;
    RecyclerItemsEnterAnimator recyclerItemsEnterAnimator;
    private final Theme.ResourcesProvider resourcesProvider;
    private int scrollOffsetY;
    private ShareSearchAdapter searchAdapter;
    private StickerEmptyView searchEmptyView;
    private RecyclerListView searchGridView;
    private boolean searchIsVisible;
    private FillLastGridLayoutManager searchLayoutManager;
    SearchField searchView;
    private boolean searchWasVisibleBeforeTopics;
    private View selectedCountView;
    protected Map selectedDialogTopics;
    protected LongSparseArray selectedDialogs;
    private TLRPC.Dialog selectedTopicDialog;
    private ActionBarPopupWindow sendPopupWindow;
    protected ArrayList sendingMessageObjects;
    private String[] sendingText;
    private View[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private ShareTopicsAdapter shareTopicsAdapter;
    private LinearLayout sharesCountLayout;
    private int shiftDp;
    private boolean showSendersName;
    private SizeNotifierFrameLayout sizeNotifierFrameLayout;
    TL_stories.StoryItem storyItem;
    private SwitchView switchView;
    private TextPaint textPaint;
    private ValueAnimator topBackgroundAnimator;
    private int topBeforeSwitch;
    private SpringAnimation topicsAnimation;
    ActionBar topicsBackActionBar;
    private RecyclerListView topicsGridView;
    private GridLayoutManager topicsLayoutManager;
    private boolean updateSearchAdapter;
    private FrameLayout writeButtonContainer;

    class 16 extends FrameLayout {
        private final Paint p;

        16(Context context) {
            super(context);
            this.p = new Paint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDraw$0(ValueAnimator valueAnimator) {
            ShareAlert.this.captionEditTextTopOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ShareAlert.this.frameLayout2.invalidate();
            invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            canvas.save();
            canvas.clipRect(0.0f, ShareAlert.this.captionEditTextTopOffset, getMeasuredWidth(), getMeasuredHeight());
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (ShareAlert.this.chatActivityEnterViewAnimateFromTop != 0.0f && ShareAlert.this.chatActivityEnterViewAnimateFromTop != ShareAlert.this.frameLayout2.getTop() + ShareAlert.this.chatActivityEnterViewAnimateFromTop) {
                if (ShareAlert.this.topBackgroundAnimator != null) {
                    ShareAlert.this.topBackgroundAnimator.cancel();
                }
                ShareAlert shareAlert = ShareAlert.this;
                shareAlert.captionEditTextTopOffset = shareAlert.chatActivityEnterViewAnimateFromTop - (ShareAlert.this.frameLayout2.getTop() + ShareAlert.this.captionEditTextTopOffset);
                ShareAlert shareAlert2 = ShareAlert.this;
                shareAlert2.topBackgroundAnimator = ValueAnimator.ofFloat(shareAlert2.captionEditTextTopOffset, 0.0f);
                ShareAlert.this.topBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$16$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ShareAlert.16.this.lambda$onDraw$0(valueAnimator);
                    }
                });
                ShareAlert.this.topBackgroundAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ShareAlert.this.topBackgroundAnimator.setDuration(200L);
                ShareAlert.this.topBackgroundAnimator.start();
                ShareAlert.this.chatActivityEnterViewAnimateFromTop = 0.0f;
            }
            ShareAlert.this.shadow[1].setTranslationY((-(ShareAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(48.0f))) + ShareAlert.this.captionEditTextTopOffset + ShareAlert.this.currentPanTranslationY + ((ShareAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(48.0f)) * (1.0f - getAlpha())));
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            invalidate();
        }

        @Override // android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            if (i != 0) {
                ShareAlert.this.shadow[1].setTranslationY(0.0f);
            }
        }
    }

    class 17 extends EditTextEmoji {
        private ValueAnimator messageEditTextAnimator;
        private int messageEditTextPredrawHeigth;
        private int messageEditTextPredrawScrollY;
        private boolean shouldAnimateEditTextWithBounds;

        17(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout, BaseFragment baseFragment, int i, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, sizeNotifierFrameLayout, baseFragment, i, z, resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$dispatchDraw$0(EditTextCaption editTextCaption, ValueAnimator valueAnimator) {
            editTextCaption.setOffsetY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$hidePopup$2(int i) {
            ShareAlert shareAlert = ShareAlert.this;
            shareAlert.setOverlayNavBarColor(((BottomSheet) shareAlert).navBarColor = i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showPopup$1(int i) {
            ShareAlert shareAlert = ShareAlert.this;
            shareAlert.setOverlayNavBarColor(((BottomSheet) shareAlert).navBarColor = i);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.shouldAnimateEditTextWithBounds) {
                final EditTextCaption editText = ShareAlert.this.commentTextView.getEditText();
                editText.setOffsetY(editText.getOffsetY() - ((this.messageEditTextPredrawHeigth - editText.getMeasuredHeight()) + (this.messageEditTextPredrawScrollY - editText.getScrollY())));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(editText.getOffsetY(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$17$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ShareAlert.17.lambda$dispatchDraw$0(EditTextCaption.this, valueAnimator);
                    }
                });
                ValueAnimator valueAnimator = this.messageEditTextAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                this.messageEditTextAnimator = ofFloat;
                ofFloat.setDuration(200L);
                ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.start();
                this.shouldAnimateEditTextWithBounds = false;
            }
            super.dispatchDraw(canvas);
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        public void hidePopup(boolean z) {
            super.hidePopup(z);
            if (ShareAlert.this.darkTheme) {
                ((BottomSheet) ShareAlert.this).navBarColorKey = -1;
                AndroidUtilities.setNavigationBarColor(ShareAlert.this.getWindow(), ShareAlert.this.getThemedColor(Theme.key_voipgroup_inviteMembersBackground), true, new AndroidUtilities.IntColorCallback() { // from class: org.telegram.ui.Components.ShareAlert$17$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.AndroidUtilities.IntColorCallback
                    public final void run(int i) {
                        ShareAlert.17.this.lambda$hidePopup$2(i);
                    }
                });
            }
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void onLineCountChanged(int i, int i2) {
            if (TextUtils.isEmpty(getEditText().getText())) {
                getEditText().animate().cancel();
                getEditText().setOffsetY(0.0f);
                this.shouldAnimateEditTextWithBounds = false;
            } else {
                this.shouldAnimateEditTextWithBounds = true;
                this.messageEditTextPredrawHeigth = getEditText().getMeasuredHeight();
                this.messageEditTextPredrawScrollY = getEditText().getScrollY();
                invalidate();
            }
            ShareAlert.this.chatActivityEnterViewAnimateFromTop = r2.frameLayout2.getTop() + ShareAlert.this.captionEditTextTopOffset;
            ShareAlert.this.frameLayout2.invalidate();
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void showPopup(int i) {
            super.showPopup(i);
            if (ShareAlert.this.darkTheme) {
                ((BottomSheet) ShareAlert.this).navBarColorKey = -1;
                AndroidUtilities.setNavigationBarColor(ShareAlert.this.getWindow(), ShareAlert.this.getThemedColor(Theme.key_windowBackgroundGray), true, new AndroidUtilities.IntColorCallback() { // from class: org.telegram.ui.Components.ShareAlert$17$$ExternalSyntheticLambda0
                    @Override // org.telegram.messenger.AndroidUtilities.IntColorCallback
                    public final void run(int i2) {
                        ShareAlert.17.this.lambda$showPopup$1(i2);
                    }
                });
            }
        }
    }

    class 22 implements NotificationCenter.NotificationCenterDelegate {
        final /* synthetic */ View val$cell;
        final /* synthetic */ TLRPC.Dialog val$dialog;
        final /* synthetic */ AtomicReference val$timeoutRef;

        22(TLRPC.Dialog dialog, AtomicReference atomicReference, View view) {
            this.val$dialog = dialog;
            this.val$timeoutRef = atomicReference;
            this.val$cell = view;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$0(View view, int[] iArr, DynamicAnimation dynamicAnimation, float f, float f2) {
            ShareAlert.this.invalidateTopicsAnimation(view, iArr, f / 1000.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$1(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            ShareAlert.this.gridView.setVisibility(8);
            ShareAlert.this.searchGridView.setVisibility(8);
            ShareAlert.this.searchView.setVisibility(8);
            ShareAlert.this.topicsAnimation = null;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (((Long) objArr[0]).longValue() == (-this.val$dialog.id)) {
                boolean z = (ShareAlert.this.shareTopicsAdapter.topics == null && MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getTopicsController().getTopics(-this.val$dialog.id) != null) || this.val$timeoutRef.get() == null;
                ShareAlert.this.shareTopicsAdapter.topics = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getTopicsController().getTopics(-this.val$dialog.id);
                if (z) {
                    ShareAlert.this.shareTopicsAdapter.notifyDataSetChanged();
                }
                if (ShareAlert.this.shareTopicsAdapter.topics != null) {
                    NotificationCenter.getInstance(((BottomSheet) ShareAlert.this).currentAccount).removeObserver(this, NotificationCenter.topicsDidLoaded);
                }
                if (z) {
                    ShareAlert.this.topicsGridView.setVisibility(0);
                    ShareAlert.this.topicsGridView.setAlpha(0.0f);
                    ShareAlert.this.topicsBackActionBar.setVisibility(0);
                    ShareAlert.this.topicsBackActionBar.setAlpha(0.0f);
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.topicsBackActionBar.setTitle(MessagesController.getInstance(((BottomSheet) shareAlert).currentAccount).getChat(Long.valueOf(-this.val$dialog.id)).title);
                    ShareAlert.this.topicsBackActionBar.setSubtitle(LocaleController.getString(R.string.SelectTopic));
                    ShareAlert shareAlert2 = ShareAlert.this;
                    shareAlert2.searchWasVisibleBeforeTopics = shareAlert2.searchIsVisible;
                    if (ShareAlert.this.topicsAnimation != null) {
                        ShareAlert.this.topicsAnimation.cancel();
                    }
                    final int[] iArr = new int[2];
                    ShareAlert.this.topicsAnimation = new SpringAnimation(new FloatValueHolder(0.0f)).setSpring(new SpringForce(1000.0f).setStiffness((ShareAlert.this.parentFragment == null || !ShareAlert.this.parentFragment.shareAlertDebugTopicsSlowMotion) ? 800.0f : 10.0f).setDampingRatio(1.0f));
                    SpringAnimation springAnimation = ShareAlert.this.topicsAnimation;
                    final View view = this.val$cell;
                    springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$22$$ExternalSyntheticLambda0
                        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                        public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                            ShareAlert.22.this.lambda$didReceivedNotification$0(view, iArr, dynamicAnimation, f, f2);
                        }
                    });
                    ShareAlert.this.topicsAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ShareAlert$22$$ExternalSyntheticLambda1
                        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                        public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f, float f2) {
                            ShareAlert.22.this.lambda$didReceivedNotification$1(dynamicAnimation, z2, f, f2);
                        }
                    });
                    ShareAlert.this.topicsAnimation.start();
                    if (this.val$timeoutRef.get() != null) {
                        AndroidUtilities.cancelRunOnUIThread((Runnable) this.val$timeoutRef.get());
                        this.val$timeoutRef.set(null);
                    }
                }
            }
        }
    }

    public static class DialogSearchResult {
        public int date;
        public TLRPC.Dialog dialog = new TLRPC.TL_dialog();
        public CharSequence name;
        public TLObject object;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SearchField extends FrameLayout {
        private ImageView clearSearchImageView;
        private CloseProgressDrawable2 progressDrawable;
        private View searchBackground;
        private EditTextBoldCursor searchEditText;
        private ImageView searchIconImageView;

        public SearchField(Context context) {
            super(context);
            View view = new View(context);
            this.searchBackground = view;
            view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_searchBackground : Theme.key_dialogSearchBackground)));
            addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            this.searchIconImageView = imageView;
            ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
            imageView.setScaleType(scaleType);
            this.searchIconImageView.setImageResource(R.drawable.smiles_inputsearch);
            this.searchIconImageView.setColorFilter(new PorterDuffColorFilter(ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_mutedIcon : Theme.key_dialogSearchIcon), PorterDuff.Mode.MULTIPLY));
            addView(this.searchIconImageView, LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.clearSearchImageView = imageView2;
            imageView2.setScaleType(scaleType);
            ImageView imageView3 = this.clearSearchImageView;
            CloseProgressDrawable2 closeProgressDrawable2 = new CloseProgressDrawable2() { // from class: org.telegram.ui.Components.ShareAlert.SearchField.1
                @Override // org.telegram.ui.Components.CloseProgressDrawable2
                protected int getCurrentColor() {
                    ShareAlert shareAlert = ShareAlert.this;
                    return shareAlert.getThemedColor(shareAlert.darkTheme ? Theme.key_voipgroup_searchPlaceholder : Theme.key_dialogSearchIcon);
                }
            };
            this.progressDrawable = closeProgressDrawable2;
            imageView3.setImageDrawable(closeProgressDrawable2);
            this.progressDrawable.setSide(AndroidUtilities.dp(7.0f));
            this.clearSearchImageView.setScaleX(0.1f);
            this.clearSearchImageView.setScaleY(0.1f);
            this.clearSearchImageView.setAlpha(0.0f);
            addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 11.0f, 14.0f, 0.0f));
            this.clearSearchImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$SearchField$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.SearchField.this.lambda$new$0(view2);
                }
            });
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.searchEditText = editTextBoldCursor;
            editTextBoldCursor.setTextSize(1, 16.0f);
            this.searchEditText.setHintTextColor(ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_searchPlaceholder : Theme.key_dialogSearchHint));
            this.searchEditText.setTextColor(ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_searchText : Theme.key_dialogSearchText));
            this.searchEditText.setBackgroundDrawable(null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            this.searchEditText.setHint(LocaleController.getString(R.string.ShareSendTo));
            this.searchEditText.setCursorColor(ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_searchText : Theme.key_featuredStickers_addedIcon));
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Components.ShareAlert.SearchField.2
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    boolean z = SearchField.this.searchEditText.length() > 0;
                    if (z != (SearchField.this.clearSearchImageView.getAlpha() != 0.0f)) {
                        SearchField.this.clearSearchImageView.animate().alpha(z ? 1.0f : 0.0f).setDuration(150L).scaleX(z ? 1.0f : 0.1f).scaleY(z ? 1.0f : 0.1f).start();
                    }
                    if (!TextUtils.isEmpty(SearchField.this.searchEditText.getText())) {
                        ShareAlert.this.checkCurrentList(false);
                    }
                    if (ShareAlert.this.updateSearchAdapter) {
                        String obj = SearchField.this.searchEditText.getText().toString();
                        if (obj.length() != 0) {
                            if (ShareAlert.this.searchEmptyView != null) {
                                ShareAlert.this.searchEmptyView.title.setText(LocaleController.getString(R.string.NoResult));
                            }
                        } else if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.listAdapter) {
                            int currentTop = ShareAlert.this.getCurrentTop();
                            ShareAlert.this.searchEmptyView.title.setText(LocaleController.getString(R.string.NoResult));
                            ShareAlert.this.searchEmptyView.showProgress(false, true);
                            ShareAlert.this.checkCurrentList(false);
                            ShareAlert.this.listAdapter.notifyDataSetChanged();
                            if (currentTop > 0) {
                                ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -currentTop);
                            }
                        }
                        if (ShareAlert.this.searchAdapter != null) {
                            ShareAlert.this.searchAdapter.searchDialogs(obj);
                        }
                    }
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }
            });
            this.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ShareAlert$SearchField$$ExternalSyntheticLambda1
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    boolean lambda$new$1;
                    lambda$new$1 = ShareAlert.SearchField.this.lambda$new$1(textView, i, keyEvent);
                    return lambda$new$1;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            ShareAlert.this.updateSearchAdapter = true;
            this.searchEditText.setText("");
            AndroidUtilities.showKeyboard(this.searchEditText);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (keyEvent == null) {
                return false;
            }
            if ((keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 84) && (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) {
                return false;
            }
            AndroidUtilities.hideKeyboard(this.searchEditText);
            return false;
        }

        public void hideKeyboard() {
            AndroidUtilities.hideKeyboard(this.searchEditText);
        }
    }

    public interface ShareAlertDelegate {

        public abstract /* synthetic */ class -CC {
            public static void $default$didShare(ShareAlertDelegate shareAlertDelegate) {
            }
        }

        boolean didCopy();

        void didShare();
    }

    private class ShareDialogsAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private ArrayList dialogs = new ArrayList();
        private LongSparseArray dialogsMap = new LongSparseArray();

        private class MyStoryDialog extends TLRPC.Dialog {
            private MyStoryDialog() {
                this.id = Long.MAX_VALUE;
            }
        }

        public ShareDialogsAdapter(Context context) {
            this.context = context;
            fetchDialogs();
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x009f, code lost:
        
            if (r6.folder_id == 1) goto L44;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x00e7, code lost:
        
            r11.dialogs.add(r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x00e3, code lost:
        
            r2.add(r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00e1, code lost:
        
            if (r6.folder_id == 1) goto L44;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void fetchDialogs() {
            TLRPC.TL_chatAdminRights tL_chatAdminRights;
            this.dialogs.clear();
            this.dialogsMap.clear();
            long j = UserConfig.getInstance(((BottomSheet) ShareAlert.this).currentAccount).clientUserId;
            if (ShareAlert.this.includeStory) {
                MyStoryDialog myStoryDialog = new MyStoryDialog();
                this.dialogs.add(myStoryDialog);
                this.dialogsMap.put(myStoryDialog.id, myStoryDialog);
            }
            if (!MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).dialogsForward.isEmpty()) {
                TLRPC.Dialog dialog = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).dialogsForward.get(0);
                this.dialogs.add(dialog);
                this.dialogsMap.put(dialog.id, dialog);
            }
            ArrayList arrayList = new ArrayList();
            ArrayList<TLRPC.Dialog> allDialogs = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getAllDialogs();
            for (int i = 0; i < allDialogs.size(); i++) {
                TLRPC.Dialog dialog2 = allDialogs.get(i);
                if (dialog2 instanceof TLRPC.TL_dialog) {
                    long j2 = dialog2.id;
                    if (j2 != j && !DialogObject.isEncryptedDialog(j2)) {
                        if (!DialogObject.isUserDialog(dialog2.id)) {
                            TLRPC.Chat chat = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getChat(Long.valueOf(-dialog2.id));
                            if (chat != null && !ChatObject.isNotInChat(chat) && ((!chat.gigagroup || ChatObject.hasAdminRights(chat)) && (!ChatObject.isChannel(chat) || chat.creator || (((tL_chatAdminRights = chat.admin_rights) != null && tL_chatAdminRights.post_messages) || chat.megagroup)))) {
                            }
                        }
                        this.dialogsMap.put(dialog2.id, dialog2);
                    }
                }
            }
            this.dialogs.addAll(arrayList);
            if (ShareAlert.this.parentFragment != null) {
                int i2 = ShareAlert.this.parentFragment.shareAlertDebugMode;
                if (i2 == 1) {
                    ArrayList arrayList2 = this.dialogs;
                    ArrayList arrayList3 = new ArrayList(arrayList2.subList(0, Math.min(4, arrayList2.size())));
                    this.dialogs.clear();
                    this.dialogs.addAll(arrayList3);
                } else if (i2 == 2) {
                    while (!this.dialogs.isEmpty() && this.dialogs.size() < 80) {
                        ArrayList arrayList4 = this.dialogs;
                        arrayList4.add((TLRPC.Dialog) arrayList4.get(arrayList4.size() - 1));
                    }
                }
            }
            notifyDataSetChanged();
        }

        public TLRPC.Dialog getItem(int i) {
            int i2 = i - 1;
            if (i2 < 0 || i2 >= this.dialogs.size()) {
                return null;
            }
            return (TLRPC.Dialog) this.dialogs.get(i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.dialogs.size();
            return size != 0 ? size + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == 0 ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ShareDialogCell shareDialogCell = (ShareDialogCell) viewHolder.itemView;
                TLRPC.Dialog item = getItem(i);
                if (item == null) {
                    return;
                }
                shareDialogCell.setTopic((TLRPC.TL_forumTopic) ShareAlert.this.selectedDialogTopics.get(item), false);
                long j = item.id;
                shareDialogCell.setDialog(j, ShareAlert.this.selectedDialogs.indexOfKey(j) >= 0, null);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            RecyclerView.LayoutParams layoutParams;
            if (i != 0) {
                view = new View(this.context);
                layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 56.0f : 109.0f));
            } else {
                view = new ShareDialogCell(this.context, ShareAlert.this.darkTheme ? 1 : 0, ShareAlert.this.resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.ShareDialogsAdapter.1
                    @Override // org.telegram.ui.Cells.ShareDialogCell
                    protected String repostToCustomName() {
                        return ShareAlert.this.includeStoryFromMessage ? LocaleController.getString(R.string.RepostToStory) : super.repostToCustomName();
                    }
                };
                layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f));
            }
            view.setLayoutParams(layoutParams);
            return new RecyclerListView.Holder(view);
        }
    }

    public class ShareSearchAdapter extends RecyclerListView.SelectionAdapter {
        DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapter;
        RecyclerView categoryListView;
        private Context context;
        int itemsCount;
        private int lastGlobalSearchId;
        int lastItemCont;
        private int lastLocalSearchId;
        private int lastSearchId;
        private String lastSearchText;
        private SearchAdapterHelper searchAdapterHelper;
        private Runnable searchRunnable;
        private Runnable searchRunnable2;
        private ArrayList searchResult = new ArrayList();
        int hintsCell = -1;
        int resentTitleCell = -1;
        int firstEmptyViewCell = -1;
        int recentDialogsStartRow = -1;
        int searchResultsStartRow = -1;
        int lastFilledItem = -1;
        boolean internalDialogsIsSearching = false;

        public ShareSearchAdapter(Context context) {
            this.context = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.1
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper
                protected boolean filter(TLObject tLObject) {
                    return !(tLObject instanceof TLRPC.Chat) || ChatObject.canWriteToChat((TLRPC.Chat) tLObject);
                }
            };
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.2
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public boolean canApplySearchResults(int i) {
                    return i == ShareSearchAdapter.this.lastSearchId;
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public void onDataSetChanged(int i) {
                    ShareSearchAdapter.this.lastGlobalSearchId = i;
                    if (ShareSearchAdapter.this.lastLocalSearchId != i) {
                        ShareSearchAdapter.this.searchResult.clear();
                    }
                    ShareSearchAdapter shareSearchAdapter = ShareSearchAdapter.this;
                    int i2 = shareSearchAdapter.lastItemCont;
                    if (shareSearchAdapter.getItemCount() == 0 && !ShareSearchAdapter.this.searchAdapterHelper.isSearchInProgress()) {
                        ShareSearchAdapter shareSearchAdapter2 = ShareSearchAdapter.this;
                        if (!shareSearchAdapter2.internalDialogsIsSearching) {
                            ShareAlert.this.searchEmptyView.showProgress(false, true);
                            ShareSearchAdapter.this.notifyDataSetChanged();
                            ShareAlert.this.checkCurrentList(true);
                        }
                    }
                    ShareAlert.this.recyclerItemsEnterAnimator.showItemsAnimated(i2);
                    ShareSearchAdapter.this.notifyDataSetChanged();
                    ShareAlert.this.checkCurrentList(true);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$5(View view, int i) {
            HintDialogCell hintDialogCell = (HintDialogCell) view;
            TLRPC.TL_topPeer tL_topPeer = MediaDataController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).hints.get(i);
            TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
            TLRPC.Peer peer = tL_topPeer.peer;
            long j = peer.user_id;
            if (j == 0) {
                long j2 = peer.channel_id;
                if (j2 == 0) {
                    j2 = peer.chat_id;
                    if (j2 == 0) {
                        j = 0;
                    }
                }
                j = -j2;
            }
            if (hintDialogCell.isBlocked()) {
                ShareAlert.this.showPremiumBlockedToast(hintDialogCell, j);
                return;
            }
            tL_dialog.id = j;
            ShareAlert.this.selectDialog(null, tL_dialog);
            hintDialogCell.setChecked(ShareAlert.this.selectedDialogs.indexOfKey(j) >= 0, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$3(int i, String str) {
            this.searchRunnable2 = null;
            if (i != this.lastSearchId) {
                return;
            }
            this.searchAdapterHelper.queryServerSearch(str, true, true, true, true, false, 0L, false, 0, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$4(final String str, final int i) {
            this.searchRunnable = null;
            searchDialogsInternal(str, i);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$searchDialogs$3(i, str);
                }
            };
            this.searchRunnable2 = runnable;
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$searchDialogsInternal$0(Object obj, Object obj2) {
            int i = ((DialogSearchResult) obj).date;
            int i2 = ((DialogSearchResult) obj2).date;
            if (i < i2) {
                return 1;
            }
            return i > i2 ? -1 : 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:130:0x02aa, code lost:
        
            if (r2.post_messages != false) goto L122;
         */
        /* JADX WARN: Code restructure failed: missing block: B:195:0x040d, code lost:
        
            r19 = r4;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:190:0x0411 A[Catch: Exception -> 0x0020, LOOP:7: B:174:0x0353->B:190:0x0411, LOOP_END, TryCatch #0 {Exception -> 0x0020, blocks: (B:3:0x0004, B:5:0x0013, B:8:0x0023, B:10:0x0031, B:14:0x003d, B:16:0x0044, B:17:0x0046, B:18:0x006b, B:20:0x0071, B:36:0x0089, B:39:0x0093, B:30:0x00af, B:23:0x0099, B:26:0x009f, B:29:0x00aa, B:42:0x00b3, B:45:0x00c4, B:46:0x00e9, B:48:0x00ef, B:51:0x0103, B:53:0x010a, B:56:0x0115, B:58:0x011f, B:61:0x0138, B:63:0x013e, B:67:0x0156, B:73:0x0166, B:75:0x016d, B:77:0x0184, B:79:0x0193, B:80:0x01c3, B:83:0x019c, B:71:0x01da, B:95:0x01ec, B:96:0x01f9, B:98:0x01ff, B:99:0x0225, B:101:0x022b, B:106:0x0242, B:108:0x024a, B:111:0x0261, B:113:0x0267, B:140:0x027e, B:117:0x0281, B:119:0x0287, B:121:0x0294, B:123:0x029a, B:125:0x02a0, B:127:0x02a4, B:129:0x02a8, B:131:0x02ac, B:134:0x02b4, B:147:0x02d7, B:148:0x02df, B:149:0x02e5, B:151:0x02eb, B:153:0x02f5, B:155:0x02f9, B:157:0x02fc, B:161:0x02ff, B:162:0x0316, B:164:0x031c, B:167:0x0328, B:170:0x0341, B:172:0x0349, B:175:0x0355, B:177:0x035d, B:180:0x0374, B:182:0x037a, B:186:0x0392, B:192:0x03a0, B:199:0x03a7, B:201:0x03bb, B:202:0x03c2, B:204:0x03d0, B:205:0x0401, B:207:0x03da, B:190:0x0411, B:217:0x041f), top: B:2:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:191:0x03a0 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:71:0x01da A[Catch: Exception -> 0x0020, LOOP:2: B:55:0x0113->B:71:0x01da, LOOP_END, TryCatch #0 {Exception -> 0x0020, blocks: (B:3:0x0004, B:5:0x0013, B:8:0x0023, B:10:0x0031, B:14:0x003d, B:16:0x0044, B:17:0x0046, B:18:0x006b, B:20:0x0071, B:36:0x0089, B:39:0x0093, B:30:0x00af, B:23:0x0099, B:26:0x009f, B:29:0x00aa, B:42:0x00b3, B:45:0x00c4, B:46:0x00e9, B:48:0x00ef, B:51:0x0103, B:53:0x010a, B:56:0x0115, B:58:0x011f, B:61:0x0138, B:63:0x013e, B:67:0x0156, B:73:0x0166, B:75:0x016d, B:77:0x0184, B:79:0x0193, B:80:0x01c3, B:83:0x019c, B:71:0x01da, B:95:0x01ec, B:96:0x01f9, B:98:0x01ff, B:99:0x0225, B:101:0x022b, B:106:0x0242, B:108:0x024a, B:111:0x0261, B:113:0x0267, B:140:0x027e, B:117:0x0281, B:119:0x0287, B:121:0x0294, B:123:0x029a, B:125:0x02a0, B:127:0x02a4, B:129:0x02a8, B:131:0x02ac, B:134:0x02b4, B:147:0x02d7, B:148:0x02df, B:149:0x02e5, B:151:0x02eb, B:153:0x02f5, B:155:0x02f9, B:157:0x02fc, B:161:0x02ff, B:162:0x0316, B:164:0x031c, B:167:0x0328, B:170:0x0341, B:172:0x0349, B:175:0x0355, B:177:0x035d, B:180:0x0374, B:182:0x037a, B:186:0x0392, B:192:0x03a0, B:199:0x03a7, B:201:0x03bb, B:202:0x03c2, B:204:0x03d0, B:205:0x0401, B:207:0x03da, B:190:0x0411, B:217:0x041f), top: B:2:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0166 A[SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r12v10 */
        /* JADX WARN: Type inference failed for: r12v12 */
        /* JADX WARN: Type inference failed for: r12v20 */
        /* JADX WARN: Type inference failed for: r12v21 */
        /* JADX WARN: Type inference failed for: r12v26 */
        /* JADX WARN: Type inference failed for: r12v28 */
        /* JADX WARN: Type inference failed for: r12v4 */
        /* JADX WARN: Type inference failed for: r12v5 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$searchDialogsInternal$1(String str, int i) {
            LongSparseArray longSparseArray;
            String str2;
            int i2;
            LongSparseArray longSparseArray2;
            String str3;
            ?? r12;
            LongSparseArray longSparseArray3;
            LongSparseArray longSparseArray4;
            String str4;
            ?? r122;
            boolean z;
            Long valueOf;
            ArrayList arrayList;
            int i3 = 0;
            try {
                String lowerCase = str.trim().toLowerCase();
                if (lowerCase.length() == 0) {
                    this.lastSearchId = -1;
                    updateSearchResults(new ArrayList(), this.lastSearchId);
                    return;
                }
                String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                if (lowerCase.equals(translitString) || translitString.length() == 0) {
                    translitString = null;
                }
                int i4 = (translitString != null ? 1 : 0) + 1;
                String[] strArr = new String[i4];
                strArr[0] = lowerCase;
                if (translitString != null) {
                    strArr[1] = translitString;
                }
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                LongSparseArray longSparseArray5 = new LongSparseArray();
                SQLiteCursor queryFinalized = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized("SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 400", new Object[0]);
                while (queryFinalized.next()) {
                    long longValue = queryFinalized.longValue(0);
                    DialogSearchResult dialogSearchResult = new DialogSearchResult();
                    dialogSearchResult.date = queryFinalized.intValue(1);
                    longSparseArray5.put(longValue, dialogSearchResult);
                    if (DialogObject.isUserDialog(longValue)) {
                        if (!arrayList2.contains(Long.valueOf(longValue))) {
                            valueOf = Long.valueOf(longValue);
                            arrayList = arrayList2;
                            arrayList.add(valueOf);
                        }
                    } else if (DialogObject.isChatDialog(longValue)) {
                        long j = -longValue;
                        if (!arrayList3.contains(Long.valueOf(j))) {
                            valueOf = Long.valueOf(j);
                            arrayList = arrayList3;
                            arrayList.add(valueOf);
                        }
                    }
                }
                queryFinalized.dispose();
                String str5 = ";;;";
                String str6 = " ";
                if (arrayList2.isEmpty()) {
                    longSparseArray = longSparseArray5;
                    str2 = ";;;";
                    i2 = 0;
                } else {
                    SQLiteCursor queryFinalized2 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, status, name FROM users WHERE uid IN(%s)", TextUtils.join(",", arrayList2)), new Object[0]);
                    i2 = 0;
                    while (queryFinalized2.next()) {
                        String stringValue = queryFinalized2.stringValue(2);
                        String translitString2 = LocaleController.getInstance().getTranslitString(stringValue);
                        if (stringValue.equals(translitString2)) {
                            translitString2 = null;
                        }
                        int lastIndexOf = stringValue.lastIndexOf(str5);
                        String substring = lastIndexOf != -1 ? stringValue.substring(lastIndexOf + 3) : null;
                        boolean z2 = false;
                        while (true) {
                            if (i3 >= i4) {
                                longSparseArray4 = longSparseArray5;
                                str4 = str5;
                                break;
                            }
                            boolean z3 = z2;
                            String str7 = strArr[i3];
                            if (stringValue.startsWith(str7)) {
                                str4 = str5;
                            } else {
                                str4 = str5;
                                if (!stringValue.contains(" " + str7)) {
                                    if (translitString2 != null) {
                                        if (!translitString2.startsWith(str7)) {
                                            if (translitString2.contains(" " + str7)) {
                                            }
                                        }
                                    }
                                    r122 = (substring == null || !substring.startsWith(str7)) ? z3 : 2;
                                    if (r122 == 0) {
                                        NativeByteBuffer byteBufferValue = queryFinalized2.byteBufferValue(0);
                                        if (byteBufferValue != null) {
                                            TLRPC.User TLdeserialize = TLRPC.User.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                            byteBufferValue.reuse();
                                            DialogSearchResult dialogSearchResult2 = (DialogSearchResult) longSparseArray5.get(TLdeserialize.id);
                                            TLRPC.UserStatus userStatus = TLdeserialize.status;
                                            if (userStatus != null) {
                                                longSparseArray4 = longSparseArray5;
                                                z = true;
                                                userStatus.expires = queryFinalized2.intValue(1);
                                            } else {
                                                longSparseArray4 = longSparseArray5;
                                                z = true;
                                            }
                                            dialogSearchResult2.name = r122 == z ? AndroidUtilities.generateSearchName(TLdeserialize.first_name, TLdeserialize.last_name, str7) : AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(TLdeserialize), null, "@" + str7);
                                            dialogSearchResult2.object = TLdeserialize;
                                            dialogSearchResult2.dialog.id = TLdeserialize.id;
                                            i2++;
                                        } else {
                                            longSparseArray4 = longSparseArray5;
                                        }
                                    } else {
                                        i3++;
                                        z2 = r122;
                                        substring = substring;
                                        str5 = str4;
                                    }
                                }
                            }
                            r122 = 1;
                            if (r122 == 0) {
                            }
                        }
                        str5 = str4;
                        longSparseArray5 = longSparseArray4;
                        i3 = 0;
                    }
                    longSparseArray = longSparseArray5;
                    str2 = str5;
                    queryFinalized2.dispose();
                }
                if (arrayList3.isEmpty()) {
                    longSparseArray2 = longSparseArray;
                } else {
                    SQLiteCursor queryFinalized3 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, name FROM chats WHERE uid IN(%s)", TextUtils.join(",", arrayList3)), new Object[0]);
                    while (queryFinalized3.next()) {
                        String stringValue2 = queryFinalized3.stringValue(1);
                        String translitString3 = LocaleController.getInstance().getTranslitString(stringValue2);
                        if (stringValue2.equals(translitString3)) {
                            translitString3 = null;
                        }
                        for (int i5 = 0; i5 < i4; i5++) {
                            String str8 = strArr[i5];
                            if (!stringValue2.startsWith(str8)) {
                                if (!stringValue2.contains(" " + str8)) {
                                    if (translitString3 != null) {
                                        if (!translitString3.startsWith(str8)) {
                                            if (translitString3.contains(" " + str8)) {
                                            }
                                        }
                                    }
                                }
                            }
                            NativeByteBuffer byteBufferValue2 = queryFinalized3.byteBufferValue(0);
                            if (byteBufferValue2 != null) {
                                TLRPC.Chat TLdeserialize2 = TLRPC.Chat.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                                byteBufferValue2.reuse();
                                if (TLdeserialize2 != null) {
                                    if (!ChatObject.isNotInChat(TLdeserialize2)) {
                                        if (ChatObject.isChannel(TLdeserialize2)) {
                                            if (!TLdeserialize2.creator) {
                                                TLRPC.TL_chatAdminRights tL_chatAdminRights = TLdeserialize2.admin_rights;
                                                if (tL_chatAdminRights != null) {
                                                }
                                                if (TLdeserialize2.megagroup) {
                                                }
                                            }
                                        }
                                        longSparseArray3 = longSparseArray;
                                        DialogSearchResult dialogSearchResult3 = (DialogSearchResult) longSparseArray3.get(-TLdeserialize2.id);
                                        dialogSearchResult3.name = AndroidUtilities.generateSearchName(TLdeserialize2.title, null, str8);
                                        dialogSearchResult3.object = TLdeserialize2;
                                        dialogSearchResult3.dialog.id = -TLdeserialize2.id;
                                        i2++;
                                        longSparseArray = longSparseArray3;
                                    }
                                }
                            }
                            longSparseArray3 = longSparseArray;
                            longSparseArray = longSparseArray3;
                        }
                        longSparseArray3 = longSparseArray;
                        longSparseArray = longSparseArray3;
                    }
                    longSparseArray2 = longSparseArray;
                    queryFinalized3.dispose();
                }
                ArrayList arrayList4 = new ArrayList(i2);
                for (int i6 = 0; i6 < longSparseArray2.size(); i6++) {
                    DialogSearchResult dialogSearchResult4 = (DialogSearchResult) longSparseArray2.valueAt(i6);
                    if (dialogSearchResult4.object != null && dialogSearchResult4.name != null) {
                        arrayList4.add(dialogSearchResult4);
                    }
                }
                SQLiteCursor queryFinalized4 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized("SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid", new Object[0]);
                while (queryFinalized4.next()) {
                    if (longSparseArray2.indexOfKey(queryFinalized4.longValue(3)) < 0) {
                        String stringValue3 = queryFinalized4.stringValue(2);
                        String translitString4 = LocaleController.getInstance().getTranslitString(stringValue3);
                        if (stringValue3.equals(translitString4)) {
                            str3 = str2;
                            translitString4 = null;
                        } else {
                            str3 = str2;
                        }
                        int lastIndexOf2 = stringValue3.lastIndexOf(str3);
                        String substring2 = lastIndexOf2 != -1 ? stringValue3.substring(lastIndexOf2 + 3) : null;
                        int i7 = 0;
                        boolean z4 = false;
                        while (true) {
                            if (i7 >= i4) {
                                break;
                            }
                            String str9 = strArr[i7];
                            if (!stringValue3.startsWith(str9)) {
                                if (!stringValue3.contains(str6 + str9)) {
                                    if (translitString4 != null) {
                                        if (!translitString4.startsWith(str9)) {
                                            if (translitString4.contains(str6 + str9)) {
                                            }
                                        }
                                    }
                                    r12 = (substring2 == null || !substring2.startsWith(str9)) ? z4 : 2;
                                    if (r12 == 0) {
                                        NativeByteBuffer byteBufferValue3 = queryFinalized4.byteBufferValue(0);
                                        if (byteBufferValue3 != null) {
                                            TLRPC.User TLdeserialize3 = TLRPC.User.TLdeserialize(byteBufferValue3, byteBufferValue3.readInt32(false), false);
                                            byteBufferValue3.reuse();
                                            DialogSearchResult dialogSearchResult5 = new DialogSearchResult();
                                            TLRPC.UserStatus userStatus2 = TLdeserialize3.status;
                                            if (userStatus2 != null) {
                                                userStatus2.expires = queryFinalized4.intValue(1);
                                            }
                                            int i8 = i4;
                                            String str10 = str6;
                                            dialogSearchResult5.dialog.id = TLdeserialize3.id;
                                            dialogSearchResult5.object = TLdeserialize3;
                                            dialogSearchResult5.name = r12 == 1 ? AndroidUtilities.generateSearchName(TLdeserialize3.first_name, TLdeserialize3.last_name, str9) : AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(TLdeserialize3), null, "@" + str9);
                                            arrayList4.add(dialogSearchResult5);
                                            str2 = str3;
                                            i4 = i8;
                                            str6 = str10;
                                        }
                                    } else {
                                        i7++;
                                        z4 = r12;
                                        i4 = i4;
                                    }
                                }
                            }
                            r12 = 1;
                            if (r12 == 0) {
                            }
                        }
                    }
                }
                queryFinalized4.dispose();
                Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda4
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$searchDialogsInternal$0;
                        lambda$searchDialogsInternal$0 = ShareAlert.ShareSearchAdapter.lambda$searchDialogsInternal$0(obj, obj2);
                        return lambda$searchDialogsInternal$0;
                    }
                });
                updateSearchResults(arrayList4, i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$2(int i, ArrayList arrayList) {
            if (i != this.lastSearchId) {
                return;
            }
            getItemCount();
            this.internalDialogsIsSearching = false;
            this.lastLocalSearchId = i;
            if (this.lastGlobalSearchId != i) {
                this.searchAdapterHelper.clear();
            }
            if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.searchAdapter) {
                ShareAlert shareAlert = ShareAlert.this;
                shareAlert.topBeforeSwitch = shareAlert.getCurrentTop();
                ShareAlert.this.searchAdapter.notifyDataSetChanged();
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLObject tLObject = ((DialogSearchResult) arrayList.get(i2)).object;
                if (tLObject instanceof TLRPC.User) {
                    MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putUser((TLRPC.User) tLObject, true);
                } else if (tLObject instanceof TLRPC.Chat) {
                    MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putChat((TLRPC.Chat) tLObject, true);
                }
            }
            boolean z = !this.searchResult.isEmpty() && arrayList.isEmpty();
            if (this.searchResult.isEmpty()) {
                arrayList.isEmpty();
            }
            if (z) {
                ShareAlert shareAlert2 = ShareAlert.this;
                shareAlert2.topBeforeSwitch = shareAlert2.getCurrentTop();
            }
            this.searchResult = arrayList;
            this.searchAdapterHelper.mergeResults(arrayList, null);
            int i3 = this.lastItemCont;
            if (getItemCount() != 0 || this.searchAdapterHelper.isSearchInProgress() || this.internalDialogsIsSearching) {
                ShareAlert.this.recyclerItemsEnterAnimator.showItemsAnimated(i3);
            } else {
                ShareAlert.this.searchEmptyView.showProgress(false, true);
            }
            notifyDataSetChanged();
            ShareAlert.this.checkCurrentList(true);
        }

        private void searchDialogsInternal(final String str, final int i) {
            MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$searchDialogsInternal$1(str, i);
                }
            });
        }

        private void updateSearchResults(final ArrayList arrayList, final int i) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$updateSearchResults$2(i, arrayList);
                }
            });
        }

        public TLRPC.Dialog getItem(int i) {
            long j;
            long j2;
            int i2 = this.recentDialogsStartRow;
            if (i >= i2 && i2 >= 0) {
                int i3 = i - i2;
                if (i3 >= 0 && i3 < ShareAlert.this.recentSearchObjects.size()) {
                    TLObject tLObject = ((DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i3)).object;
                    TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
                    if (tLObject instanceof TLRPC.User) {
                        j2 = ((TLRPC.User) tLObject).id;
                    } else if (tLObject instanceof TLRPC.Chat) {
                        j2 = -((TLRPC.Chat) tLObject).id;
                    }
                    tL_dialog.id = j2;
                    return tL_dialog;
                }
                return null;
            }
            int i4 = i - 1;
            if (i4 < 0) {
                return null;
            }
            if (i4 < this.searchResult.size()) {
                return ((DialogSearchResult) this.searchResult.get(i4)).dialog;
            }
            int size = i4 - this.searchResult.size();
            ArrayList localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            if (size < localServerSearch.size()) {
                TLObject tLObject2 = (TLObject) localServerSearch.get(size);
                TLRPC.TL_dialog tL_dialog2 = new TLRPC.TL_dialog();
                if (tLObject2 instanceof TLRPC.User) {
                    j = ((TLRPC.User) tLObject2).id;
                } else if (tLObject2 instanceof TLRPC.Chat) {
                    j = -((TLRPC.Chat) tLObject2).id;
                }
                tL_dialog2.id = j;
                return tL_dialog2;
            }
            return null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = 0;
            this.itemsCount = 0;
            this.hintsCell = -1;
            this.resentTitleCell = -1;
            this.recentDialogsStartRow = -1;
            this.searchResultsStartRow = -1;
            this.lastFilledItem = -1;
            if (!TextUtils.isEmpty(this.lastSearchText)) {
                int i2 = this.itemsCount;
                int i3 = i2 + 1;
                this.itemsCount = i3;
                this.firstEmptyViewCell = i2;
                this.searchResultsStartRow = i3;
                int size = i3 + this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size();
                this.itemsCount = size;
                if (size == 1) {
                    this.firstEmptyViewCell = -1;
                    this.itemsCount = 0;
                } else {
                    i = size + 1;
                    this.itemsCount = i;
                    this.lastFilledItem = size;
                }
                this.lastItemCont = i;
                return i;
            }
            int i4 = this.itemsCount;
            this.firstEmptyViewCell = i4;
            this.itemsCount = i4 + 2;
            this.hintsCell = i4 + 1;
            if (ShareAlert.this.recentSearchObjects.size() > 0) {
                int i5 = this.itemsCount;
                int i6 = i5 + 1;
                this.itemsCount = i6;
                this.resentTitleCell = i5;
                this.recentDialogsStartRow = i6;
                this.itemsCount = i6 + ShareAlert.this.recentSearchObjects.size();
            }
            int i7 = this.itemsCount;
            int i8 = i7 + 1;
            this.itemsCount = i8;
            this.lastFilledItem = i7;
            this.lastItemCont = i8;
            return i8;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == this.lastFilledItem) {
                return 4;
            }
            if (i == this.firstEmptyViewCell) {
                return 1;
            }
            if (i == this.hintsCell) {
                return 2;
            }
            if (i == this.resentTitleCell) {
                return 3;
            }
            return TextUtils.isEmpty(this.lastSearchText) ? 0 : 5;
        }

        public int getSpanSize(int i, int i2) {
            if (i2 == this.hintsCell || i2 == this.resentTitleCell || i2 == this.firstEmptyViewCell || i2 == this.lastFilledItem || getItemViewType(i2) == 0) {
                return i;
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return (viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 4) ? false : true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v27, types: [java.lang.CharSequence] */
        /* JADX WARN: Type inference failed for: r7v2, types: [android.text.SpannableStringBuilder] */
        /* JADX WARN: Type inference failed for: r7v5, types: [android.text.SpannableStringBuilder] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            long j;
            String str;
            String str2;
            long j2;
            int indexOfIgnoreCase;
            TLObject tLObject;
            int indexOfIgnoreCase2;
            if (viewHolder.getItemViewType() != 0 && viewHolder.getItemViewType() != 5) {
                if (viewHolder.getItemViewType() == 2) {
                    ((RecyclerListView) viewHolder.itemView).getAdapter().notifyDataSetChanged();
                    return;
                }
                return;
            }
            TLObject tLObject2 = null;
            r6 = null;
            r6 = null;
            String str3 = null;
            TLRPC.TL_encryptedChat tL_encryptedChat = null;
            if (!TextUtils.isEmpty(this.lastSearchText)) {
                int i2 = i - 1;
                if (i2 < this.searchResult.size()) {
                    DialogSearchResult dialogSearchResult = (DialogSearchResult) this.searchResult.get(i2);
                    j2 = dialogSearchResult.dialog.id;
                    str2 = dialogSearchResult.name;
                } else {
                    i2 -= this.searchResult.size();
                    tLObject2 = (TLObject) this.searchAdapterHelper.getLocalServerSearch().get(i2);
                    if (tLObject2 instanceof TLRPC.User) {
                        TLRPC.User user = (TLRPC.User) tLObject2;
                        j = user.id;
                        str = ContactsController.formatName(user.first_name, user.last_name);
                    } else {
                        TLRPC.Chat chat = (TLRPC.Chat) tLObject2;
                        j = -chat.id;
                        str = chat.title;
                    }
                    String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                    if (TextUtils.isEmpty(lastFoundUsername) || str == null || (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str.toString(), lastFoundUsername)) == -1) {
                        str2 = str;
                    } else {
                        ?? spannableStringBuilder = new SpannableStringBuilder(str);
                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_windowBackgroundWhiteBlueText4, ShareAlert.this.resourcesProvider), indexOfIgnoreCase, lastFoundUsername.length() + indexOfIgnoreCase, 33);
                        str2 = spannableStringBuilder;
                    }
                    j2 = j;
                }
                TLObject tLObject3 = tLObject2;
                View view = viewHolder.itemView;
                if (view instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) view).setData(tLObject3, null, str2, null, false, false);
                    ((ProfileSearchCell) viewHolder.itemView).useSeparator = i2 < getItemCount() - 2;
                    return;
                } else {
                    if (view instanceof ShareDialogCell) {
                        ((ShareDialogCell) view).setDialog(j2, ShareAlert.this.selectedDialogs.indexOfKey(j2) >= 0, str2);
                        return;
                    }
                    return;
                }
            }
            int i3 = this.recentDialogsStartRow;
            long j3 = 0;
            if (i3 < 0 || i < i3) {
                tLObject = null;
            } else {
                TLObject tLObject4 = ((DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i - i3)).object;
                if (tLObject4 instanceof TLRPC.User) {
                    TLRPC.User user2 = (TLRPC.User) tLObject4;
                    j3 = user2.id;
                    str3 = ContactsController.formatName(user2.first_name, user2.last_name);
                } else if (tLObject4 instanceof TLRPC.Chat) {
                    TLRPC.Chat chat2 = (TLRPC.Chat) tLObject4;
                    j3 = -chat2.id;
                    str3 = chat2.title;
                } else if (tLObject4 instanceof TLRPC.TL_encryptedChat) {
                    tL_encryptedChat = (TLRPC.TL_encryptedChat) tLObject4;
                    TLRPC.User user3 = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getUser(Long.valueOf(tL_encryptedChat.user_id));
                    if (user3 != null) {
                        j3 = user3.id;
                        str3 = ContactsController.formatName(user3.first_name, user3.last_name);
                    }
                }
                String lastFoundUsername2 = this.searchAdapterHelper.getLastFoundUsername();
                if (TextUtils.isEmpty(lastFoundUsername2) || str3 == null || (indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str3.toString(), lastFoundUsername2)) == -1) {
                    tLObject = tLObject4;
                } else {
                    ?? spannableStringBuilder2 = new SpannableStringBuilder(str3);
                    spannableStringBuilder2.setSpan(new ForegroundColorSpanThemable(Theme.key_windowBackgroundWhiteBlueText4, ShareAlert.this.resourcesProvider), indexOfIgnoreCase2, lastFoundUsername2.length() + indexOfIgnoreCase2, 33);
                    tLObject = tLObject4;
                    str3 = spannableStringBuilder2;
                }
            }
            TLRPC.TL_encryptedChat tL_encryptedChat2 = tL_encryptedChat;
            View view2 = viewHolder.itemView;
            if (view2 instanceof ProfileSearchCell) {
                ((ProfileSearchCell) view2).setData(tLObject, tL_encryptedChat2, str3, null, false, false);
                ((ProfileSearchCell) viewHolder.itemView).useSeparator = i < getItemCount() - 2;
            } else if (view2 instanceof ShareDialogCell) {
                ((ShareDialogCell) view2).setDialog(j3, ShareAlert.this.selectedDialogs.indexOfKey(j3) >= 0, str3);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new ProfileSearchCell(this.context, ShareAlert.this.resourcesProvider).useCustomPaints().showPremiumBlock(true);
            } else if (i == 2) {
                RecyclerListView recyclerListView = new RecyclerListView(this.context, ShareAlert.this.resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.3
                    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                        if (getParent() != null && getParent().getParent() != null) {
                            ViewParent parent = getParent().getParent();
                            boolean z = true;
                            if (!canScrollHorizontally(-1) && !canScrollHorizontally(1)) {
                                z = false;
                            }
                            parent.requestDisallowInterceptTouchEvent(z);
                        }
                        return super.onInterceptTouchEvent(motionEvent);
                    }
                };
                this.categoryListView = recyclerListView;
                recyclerListView.setItemAnimator(null);
                recyclerListView.setLayoutAnimation(null);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.4
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(0);
                recyclerListView.setLayoutManager(linearLayoutManager);
                DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapterRecycler = new DialogsSearchAdapter.CategoryAdapterRecycler(this.context, ((BottomSheet) ShareAlert.this).currentAccount, true, true, ShareAlert.this.resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.5
                    @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.CategoryAdapterRecycler, androidx.recyclerview.widget.RecyclerView.Adapter
                    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
                        TLRPC.Chat chat;
                        MessagesController messagesController;
                        long j;
                        HintDialogCell hintDialogCell = (HintDialogCell) viewHolder.itemView;
                        if (ShareAlert.this.darkTheme || ShareAlert.this.forceDarkThemeForHint) {
                            hintDialogCell.setColors(Theme.key_voipgroup_nameText, Theme.key_voipgroup_inviteMembersBackground);
                        }
                        TLRPC.TL_topPeer tL_topPeer = MediaDataController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).hints.get(i2);
                        TLRPC.Peer peer = tL_topPeer.peer;
                        long j2 = peer.user_id;
                        TLRPC.User user = null;
                        if (j2 != 0) {
                            user = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getUser(Long.valueOf(tL_topPeer.peer.user_id));
                            chat = null;
                        } else {
                            long j3 = peer.channel_id;
                            if (j3 != 0) {
                                j2 = -j3;
                                messagesController = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount);
                                j = tL_topPeer.peer.channel_id;
                            } else {
                                long j4 = peer.chat_id;
                                if (j4 != 0) {
                                    j2 = -j4;
                                    messagesController = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount);
                                    j = tL_topPeer.peer.chat_id;
                                } else {
                                    chat = null;
                                    j2 = 0;
                                }
                            }
                            chat = messagesController.getChat(Long.valueOf(j));
                        }
                        boolean z = j2 == hintDialogCell.getDialogId();
                        hintDialogCell.setTag(Long.valueOf(j2));
                        hintDialogCell.setDialog(j2, true, user != null ? UserObject.getFirstName(user) : chat != null ? chat.title : "");
                        hintDialogCell.setChecked(ShareAlert.this.selectedDialogs.indexOfKey(j2) >= 0, z);
                    }
                };
                this.categoryAdapter = categoryAdapterRecycler;
                recyclerListView.setAdapter(categoryAdapterRecycler);
                recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view2, int i2) {
                        ShareAlert.ShareSearchAdapter.this.lambda$onCreateViewHolder$5(view2, i2);
                    }
                });
                view = recyclerListView;
            } else if (i == 3) {
                GraySectionCell graySectionCell = new GraySectionCell(this.context, ShareAlert.this.resourcesProvider);
                graySectionCell.setTextColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_nameText : Theme.key_graySectionText);
                ShareAlert shareAlert = ShareAlert.this;
                graySectionCell.setBackgroundColor(shareAlert.getThemedColor(shareAlert.darkTheme ? Theme.key_voipgroup_searchBackground : Theme.key_graySection));
                graySectionCell.setText(LocaleController.getString(R.string.Recent));
                view = graySectionCell;
            } else if (i == 4) {
                view = new View(this.context) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.6
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(ShareAlert.this.searchLayoutManager.lastItemHeight, 1073741824));
                    }
                };
            } else if (i != 5) {
                View view2 = new View(this.context);
                view2.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 56.0f : 109.0f)));
                view = view2;
            } else {
                View shareDialogCell = new ShareDialogCell(this.context, ShareAlert.this.darkTheme ? 1 : 0, ShareAlert.this.resourcesProvider);
                shareDialogCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f)));
                view = shareDialogCell;
            }
            return new RecyclerListView.Holder(view);
        }

        public void searchDialogs(final String str) {
            if (str == null || !str.equals(this.lastSearchText)) {
                this.lastSearchText = str;
                if (this.searchRunnable != null) {
                    Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                    this.searchRunnable = null;
                }
                Runnable runnable = this.searchRunnable2;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.searchRunnable2 = null;
                }
                this.searchResult.clear();
                this.searchAdapterHelper.mergeResults(null);
                this.searchAdapterHelper.queryServerSearch(null, true, true, true, true, false, 0L, false, 0, 0);
                notifyDataSetChanged();
                ShareAlert.this.checkCurrentList(true);
                if (TextUtils.isEmpty(str)) {
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.topBeforeSwitch = shareAlert.getCurrentTop();
                    this.lastSearchId = -1;
                    this.internalDialogsIsSearching = false;
                } else {
                    this.internalDialogsIsSearching = true;
                    final int i = this.lastSearchId + 1;
                    this.lastSearchId = i;
                    ShareAlert.this.searchEmptyView.showProgress(true, true);
                    DispatchQueue dispatchQueue = Utilities.searchQueue;
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ShareAlert.ShareSearchAdapter.this.lambda$searchDialogs$4(str, i);
                        }
                    };
                    this.searchRunnable = runnable2;
                    dispatchQueue.postRunnable(runnable2, 300L);
                }
                ShareAlert.this.checkCurrentList(false);
            }
        }
    }

    private class ShareTopicsAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private List topics;

        public ShareTopicsAdapter(Context context) {
            this.context = context;
        }

        public TLRPC.TL_forumTopic getItem(int i) {
            int i2 = i - 1;
            List list = this.topics;
            if (list == null || i2 < 0 || i2 >= list.size()) {
                return null;
            }
            return (TLRPC.TL_forumTopic) this.topics.get(i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List list = this.topics;
            if (list == null) {
                return 0;
            }
            return list.size() + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == 0 ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ShareTopicCell shareTopicCell = (ShareTopicCell) viewHolder.itemView;
                TLRPC.TL_forumTopic item = getItem(i);
                shareTopicCell.setTopic(ShareAlert.this.selectedTopicDialog, item, ShareAlert.this.selectedDialogs.indexOfKey((long) item.id) >= 0, null);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shareTopicCell;
            RecyclerView.LayoutParams layoutParams;
            if (i != 0) {
                shareTopicCell = new View(this.context);
                layoutParams = new RecyclerView.LayoutParams(-1, ActionBar.getCurrentActionBarHeight());
            } else {
                shareTopicCell = new ShareTopicCell(this.context, ShareAlert.this.resourcesProvider);
                layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f));
            }
            shareTopicCell.setLayoutParams(layoutParams);
            return new RecyclerListView.Holder(shareTopicCell);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SwitchView extends FrameLayout {
        private AnimatorSet animator;
        private int currentTab;
        private int lastColor;
        private SimpleTextView leftTab;
        private LinearGradient linearGradient;
        private Paint paint;
        private RectF rect;
        private SimpleTextView rightTab;
        private View searchBackground;
        private View slidingView;

        public SwitchView(Context context) {
            super(context);
            this.paint = new Paint(1);
            this.rect = new RectF();
            View view = new View(context);
            this.searchBackground = view;
            view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_searchBackground : Theme.key_dialogSearchBackground)));
            addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 0.0f, 14.0f, 0.0f));
            View view2 = new View(context) { // from class: org.telegram.ui.Components.ShareAlert.SwitchView.1
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    int offsetColor = AndroidUtilities.getOffsetColor(-9057429, -10513163, getTranslationX() / getMeasuredWidth(), 1.0f);
                    int offsetColor2 = AndroidUtilities.getOffsetColor(-11554882, -4629871, getTranslationX() / getMeasuredWidth(), 1.0f);
                    if (offsetColor != SwitchView.this.lastColor) {
                        SwitchView.this.linearGradient = new LinearGradient(0.0f, 0.0f, getMeasuredWidth(), 0.0f, new int[]{offsetColor, offsetColor2}, (float[]) null, Shader.TileMode.CLAMP);
                        SwitchView.this.paint.setShader(SwitchView.this.linearGradient);
                    }
                    SwitchView.this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    canvas.drawRoundRect(SwitchView.this.rect, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f), SwitchView.this.paint);
                }

                @Override // android.view.View
                public void setTranslationX(float f) {
                    super.setTranslationX(f);
                    invalidate();
                }
            };
            this.slidingView = view2;
            addView(view2, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 0.0f, 14.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.leftTab = simpleTextView;
            int i = Theme.key_voipgroup_nameText;
            simpleTextView.setTextColor(ShareAlert.this.getThemedColor(i));
            this.leftTab.setTextSize(13);
            this.leftTab.setLeftDrawable(R.drawable.msg_tabs_mic1);
            this.leftTab.setText(LocaleController.getString(R.string.VoipGroupInviteCanSpeak));
            this.leftTab.setGravity(17);
            addView(this.leftTab, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 0.0f, 0.0f, 0.0f));
            this.leftTab.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$SwitchView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ShareAlert.SwitchView.this.lambda$new$0(view3);
                }
            });
            SimpleTextView simpleTextView2 = new SimpleTextView(context);
            this.rightTab = simpleTextView2;
            simpleTextView2.setTextColor(ShareAlert.this.getThemedColor(i));
            this.rightTab.setTextSize(13);
            this.rightTab.setLeftDrawable(R.drawable.msg_tabs_mic2);
            this.rightTab.setText(LocaleController.getString(R.string.VoipGroupInviteListenOnly));
            this.rightTab.setGravity(17);
            addView(this.rightTab, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 14.0f, 0.0f));
            this.rightTab.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$SwitchView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ShareAlert.SwitchView.this.lambda$new$1(view3);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            switchToTab(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            switchToTab(1);
        }

        private void switchToTab(int i) {
            if (this.currentTab == i) {
                return;
            }
            this.currentTab = i;
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animator = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.slidingView, (Property<View, Float>) View.TRANSLATION_X, this.currentTab == 0 ? 0.0f : r2.getMeasuredWidth()));
            this.animator.setDuration(180L);
            this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.SwitchView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SwitchView.this.animator = null;
                }
            });
            this.animator.start();
            onTabSwitch(this.currentTab);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(28.0f)) / 2;
            ((FrameLayout.LayoutParams) this.leftTab.getLayoutParams()).width = size;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.rightTab.getLayoutParams();
            layoutParams.width = size;
            layoutParams.leftMargin = AndroidUtilities.dp(14.0f) + size;
            ((FrameLayout.LayoutParams) this.slidingView.getLayoutParams()).width = size;
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.slidingView.setTranslationX(this.currentTab == 0 ? 0.0f : r1.width);
            super.onMeasure(i, i2);
        }

        protected abstract void onTabSwitch(int i);
    }

    public ShareAlert(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2) {
        this(context, arrayList, str, z, str2, z2, null);
    }

    public ShareAlert(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        this(context, null, arrayList, str, null, z, str2, null, z2, false, false, resourcesProvider);
    }

    public ShareAlert(Context context, ChatActivity chatActivity, ArrayList arrayList, String str, String str2, boolean z, String str3, String str4, boolean z2, boolean z3) {
        this(context, chatActivity, arrayList, str, str2, z, str3, str4, z2, z3, false, null);
    }

    public ShareAlert(final Context context, ChatActivity chatActivity, ArrayList arrayList, String str, String str2, boolean z, String str3, String str4, boolean z2, boolean z3, boolean z4, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        TextView textView;
        int i;
        this.sendingText = new String[2];
        this.shadow = new View[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.selectedDialogs = new LongSparseArray();
        this.selectedDialogTopics = new HashMap();
        this.containerViewTop = -1;
        this.fullyShown = false;
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.linkToCopy = new String[2];
        this.recentSearchObjects = new ArrayList();
        this.recentSearchObjectsById = new LongSparseArray();
        this.shiftDp = 4;
        this.showSendersName = true;
        this.lastOffset = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.resourcesProvider = resourcesProvider;
        this.includeStory = z4;
        this.parentActivity = AndroidUtilities.findActivity(context);
        this.darkTheme = z3;
        this.parentFragment = chatActivity;
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        int i2 = this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground;
        this.behindKeyboardColorKey = i2;
        int themedColor = getThemedColor(i2);
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.MULTIPLY));
        fixNavigationBar(themedColor);
        this.isFullscreen = z2;
        String[] strArr = this.linkToCopy;
        strArr[0] = str3;
        strArr[1] = str4;
        this.sendingMessageObjects = arrayList;
        this.searchAdapter = new ShareSearchAdapter(context);
        this.isChannel = z;
        String[] strArr2 = this.sendingText;
        strArr2[0] = str;
        strArr2[1] = str2;
        this.useSmoothKeyboard = true;
        super.setDelegate(new BottomSheet.BottomSheetDelegate() { // from class: org.telegram.ui.Components.ShareAlert.1
            @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegate, org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
            public void onOpenAnimationEnd() {
                ShareAlert.this.fullyShown = true;
            }
        });
        ArrayList arrayList2 = this.sendingMessageObjects;
        if (arrayList2 != null) {
            int size = arrayList2.size();
            for (int i3 = 0; i3 < size; i3++) {
                MessageObject messageObject = (MessageObject) this.sendingMessageObjects.get(i3);
                if (messageObject.isPoll()) {
                    int i4 = messageObject.isPublicPoll() ? 2 : 1;
                    this.hasPoll = i4;
                    if (i4 == 2) {
                        break;
                    }
                }
            }
        }
        if (z) {
            this.loadingLink = true;
            TLRPC.TL_channels_exportMessageLink tL_channels_exportMessageLink = new TLRPC.TL_channels_exportMessageLink();
            tL_channels_exportMessageLink.id = ((MessageObject) arrayList.get(0)).getId();
            tL_channels_exportMessageLink.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(((MessageObject) arrayList.get(0)).messageOwner.peer_id.channel_id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_exportMessageLink, new RequestDelegate() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ShareAlert.this.lambda$new$1(context, tLObject, tL_error);
                }
            });
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.Components.ShareAlert.2
            private int fromOffsetTop;
            private int fromScrollY;
            private boolean fullHeight;
            private boolean lightStatusBar;
            private final AnimatedFloat pinnedToTop;
            private int previousTopOffset;
            private int toOffsetTop;
            private int toScrollY;
            private int topOffset;
            private boolean ignoreLayout = false;
            private RectF rect1 = new RectF();

            {
                this.adjustPanLayoutHelper = new AdjustPanLayoutHelper(this) { // from class: org.telegram.ui.Components.ShareAlert.2.1
                    @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                    protected boolean heightAnimationEnabled() {
                        if (ShareAlert.this.isDismissed() || !ShareAlert.this.fullyShown) {
                            return false;
                        }
                        return !ShareAlert.this.commentTextView.isPopupVisible();
                    }

                    @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                    protected void onPanTranslationUpdate(float f, float f2, boolean z5) {
                        super.onPanTranslationUpdate(f, f2, z5);
                        for (int i5 = 0; i5 < ((BottomSheet) ShareAlert.this).containerView.getChildCount(); i5++) {
                            if (((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.pickerBottomLayout && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.bulletinContainer && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.shadow[1] && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.sharesCountLayout && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.frameLayout2 && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.writeButtonContainer && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5) != ShareAlert.this.selectedCountView) {
                                ((BottomSheet) ShareAlert.this).containerView.getChildAt(i5).setTranslationY(f);
                            }
                        }
                        ShareAlert.this.currentPanTranslationY = f;
                        if (2.this.fromScrollY != -1) {
                            if (!z5) {
                                f2 = 1.0f - f2;
                            }
                            float f3 = 1.0f - f2;
                            ShareAlert.this.scrollOffsetY = (int) ((r5.fromScrollY * f3) + (2.this.toScrollY * f2));
                            float f4 = ShareAlert.this.currentPanTranslationY + ((2.this.fromScrollY - 2.this.toScrollY) * f3);
                            ShareAlert.this.gridView.setTranslationY(f4);
                            RecyclerListView recyclerListView = ShareAlert.this.searchGridView;
                            if (!z5) {
                                f4 += ShareAlert.this.gridView.getPaddingTop();
                            }
                            recyclerListView.setTranslationY(f4);
                        } else if (2.this.fromOffsetTop != -1) {
                            float f5 = 1.0f - f2;
                            ShareAlert.this.scrollOffsetY = (int) ((r5.fromOffsetTop * f5) + (2.this.toOffsetTop * f2));
                            if (!z5) {
                                f5 = f2;
                            }
                            RecyclerListView recyclerListView2 = ShareAlert.this.gridView;
                            if (z5) {
                                recyclerListView2.setTranslationY(ShareAlert.this.currentPanTranslationY - ((2.this.fromOffsetTop - 2.this.toOffsetTop) * f2));
                            } else {
                                recyclerListView2.setTranslationY(ShareAlert.this.currentPanTranslationY + ((2.this.toOffsetTop - 2.this.fromOffsetTop) * f5));
                            }
                        }
                        ShareAlert.this.gridView.setTopGlowOffset((int) (ShareAlert.this.scrollOffsetY + ShareAlert.this.currentPanTranslationY));
                        ShareAlert.this.frameLayout.setTranslationY(ShareAlert.this.scrollOffsetY + ShareAlert.this.currentPanTranslationY);
                        ShareAlert.this.searchEmptyView.setTranslationY(ShareAlert.this.scrollOffsetY + ShareAlert.this.currentPanTranslationY);
                        ShareAlert.this.frameLayout2.invalidate();
                        ShareAlert shareAlert = ShareAlert.this;
                        shareAlert.setCurrentPanTranslationY(shareAlert.currentPanTranslationY);
                        invalidate();
                    }

                    @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                    protected void onTransitionEnd() {
                        super.onTransitionEnd();
                        ShareAlert.this.panTranslationMoveLayout = false;
                        ShareAlert shareAlert = ShareAlert.this;
                        shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                        ShareAlert.this.gridView.setTopGlowOffset(ShareAlert.this.scrollOffsetY);
                        ShareAlert.this.frameLayout.setTranslationY(ShareAlert.this.scrollOffsetY);
                        ShareAlert.this.searchEmptyView.setTranslationY(ShareAlert.this.scrollOffsetY);
                        ShareAlert.this.gridView.setTranslationY(0.0f);
                        ShareAlert.this.searchGridView.setTranslationY(0.0f);
                    }

                    @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                    protected void onTransitionStart(boolean z5, int i5) {
                        super.onTransitionStart(z5, i5);
                        if (ShareAlert.this.previousScrollOffsetY != ShareAlert.this.scrollOffsetY) {
                            2 r5 = 2.this;
                            r5.fromScrollY = ShareAlert.this.previousScrollOffsetY;
                            2 r52 = 2.this;
                            r52.toScrollY = ShareAlert.this.scrollOffsetY;
                            ShareAlert.this.panTranslationMoveLayout = true;
                            2 r53 = 2.this;
                            ShareAlert.this.scrollOffsetY = r53.fromScrollY;
                        } else {
                            2.this.fromScrollY = -1;
                        }
                        if (2.this.topOffset != 2.this.previousTopOffset) {
                            2.this.fromOffsetTop = 0;
                            2.this.toOffsetTop = 0;
                            ShareAlert.this.panTranslationMoveLayout = true;
                            2 r54 = 2.this;
                            int i6 = r54.topOffset - 2.this.previousTopOffset;
                            if (z5) {
                                2.access$3412(r54, i6);
                            } else {
                                2.access$3420(r54, i6);
                            }
                            2 r55 = 2.this;
                            ShareAlert.this.scrollOffsetY = z5 ? r55.fromScrollY : r55.toScrollY;
                        } else {
                            2.this.fromOffsetTop = -1;
                        }
                        ShareAlert.this.gridView.setTopGlowOffset((int) (ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY));
                        ShareAlert.this.frameLayout.setTranslationY(ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY);
                        ShareAlert.this.searchEmptyView.setTranslationY(ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY);
                        invalidate();
                    }
                };
                this.lightStatusBar = AndroidUtilities.computePerceivedBrightness(ShareAlert.this.getThemedColor(ShareAlert.this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground)) > 0.721f;
                this.pinnedToTop = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            }

            static /* synthetic */ int access$3412(2 r1, int i5) {
                int i6 = r1.toOffsetTop + i5;
                r1.toOffsetTop = i6;
                return i6;
            }

            static /* synthetic */ int access$3420(2 r1, int i5) {
                int i6 = r1.toOffsetTop - i5;
                r1.toOffsetTop = i6;
                return i6;
            }

            private void onMeasureInternal(int i5, int i6) {
                int makeMeasureSpec;
                int paddingTop;
                int size2 = View.MeasureSpec.getSize(i5);
                int size3 = View.MeasureSpec.getSize(i6);
                int i7 = size2 - (((BottomSheet) ShareAlert.this).backgroundPaddingLeft * 2);
                if (!ShareAlert.this.commentTextView.isWaitingForKeyboardOpen() && AndroidUtilities.dp(20.0f) >= 0 && !ShareAlert.this.commentTextView.isPopupShowing() && !ShareAlert.this.commentTextView.isAnimatePopupClosing()) {
                    this.ignoreLayout = true;
                    ShareAlert.this.commentTextView.hideEmojiView();
                    this.ignoreLayout = false;
                }
                this.ignoreLayout = true;
                if (AndroidUtilities.dp(20.0f) >= 0) {
                    if (!AndroidUtilities.isInMultiwindow) {
                        size3 -= ((BottomSheet) ShareAlert.this).keyboardVisible ? 0 : ShareAlert.this.commentTextView.getEmojiPadding();
                        i6 = View.MeasureSpec.makeMeasureSpec(size3, 1073741824);
                    }
                    int i8 = ShareAlert.this.commentTextView.isPopupShowing() ? 8 : 0;
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(i8);
                        if (ShareAlert.this.sharesCountLayout != null) {
                            ShareAlert.this.sharesCountLayout.setVisibility(i8);
                        }
                    }
                } else {
                    ShareAlert.this.commentTextView.hideEmojiView();
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(8);
                        if (ShareAlert.this.sharesCountLayout != null) {
                            ShareAlert.this.sharesCountLayout.setVisibility(8);
                        }
                    }
                }
                this.ignoreLayout = false;
                int childCount = getChildCount();
                for (int i9 = 0; i9 < childCount; i9++) {
                    View childAt = getChildAt(i9);
                    if (childAt != null && childAt.getVisibility() != 8) {
                        if (ShareAlert.this.commentTextView == null || !ShareAlert.this.commentTextView.isPopupView(childAt)) {
                            measureChildWithMargins(childAt, i5, 0, i6, 0);
                        } else {
                            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
                                paddingTop = childAt.getLayoutParams().height;
                            } else if (AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
                                paddingTop = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (size3 - AndroidUtilities.statusBarHeight) + getPaddingTop());
                            } else {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i7, 1073741824);
                                paddingTop = (size3 - AndroidUtilities.statusBarHeight) + getPaddingTop();
                            }
                            childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                        }
                    }
                }
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0.0f, getPaddingTop() + ShareAlert.this.currentPanTranslationY, getMeasuredWidth(), getMeasuredHeight() + ShareAlert.this.currentPanTranslationY + AndroidUtilities.dp(50.0f));
                super.dispatchDraw(canvas);
                canvas.restore();
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                this.adjustPanLayoutHelper.setResizableView(this);
                this.adjustPanLayoutHelper.onAttach();
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                this.adjustPanLayoutHelper.onDetach();
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float f;
                canvas.save();
                canvas.translate(0.0f, ShareAlert.this.currentPanTranslationY);
                int dp = (ShareAlert.this.scrollOffsetY - ((BottomSheet) ShareAlert.this).backgroundPaddingTop) + AndroidUtilities.dp(6.0f) + this.topOffset;
                ShareAlert shareAlert = ShareAlert.this;
                int i5 = shareAlert.containerViewTop = ((shareAlert.scrollOffsetY - ((BottomSheet) ShareAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f)) + this.topOffset;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(60.0f) + ((BottomSheet) ShareAlert.this).backgroundPaddingTop;
                if (((BottomSheet) ShareAlert.this).isFullscreen || Build.VERSION.SDK_INT < 21) {
                    f = 0.0f;
                } else {
                    dp += AndroidUtilities.statusBarHeight;
                    boolean z5 = this.fullHeight && ((BottomSheet) ShareAlert.this).backgroundPaddingTop + i5 < AndroidUtilities.statusBarHeight;
                    int i6 = i5 + AndroidUtilities.statusBarHeight;
                    int i7 = -((BottomSheet) ShareAlert.this).backgroundPaddingTop;
                    f = this.pinnedToTop.set(z5);
                    i5 = AndroidUtilities.lerp(i6, i7, f);
                }
                ShareAlert.this.shadowDrawable.setBounds(0, i5, getMeasuredWidth(), measuredHeight);
                ShareAlert.this.shadowDrawable.draw(canvas);
                FrameLayout frameLayout = ShareAlert.this.bulletinContainer2;
                if (frameLayout != null) {
                    if (i5 > AndroidUtilities.statusBarHeight || frameLayout.getChildCount() <= 0) {
                        ShareAlert.this.bulletinContainer2.setTranslationY(Math.max(0, ((i5 + ((BottomSheet) r1).backgroundPaddingTop) - ShareAlert.this.bulletinContainer2.getTop()) - ShareAlert.this.bulletinContainer2.getMeasuredHeight()));
                    } else {
                        ShareAlert.this.bulletinContainer2.setTranslationY(0.0f);
                        Bulletin visibleBulletin = Bulletin.getVisibleBulletin();
                        if (visibleBulletin != null) {
                            if (visibleBulletin.getLayout() != null) {
                                visibleBulletin.getLayout().setTop(true);
                            }
                            visibleBulletin.hide();
                        }
                    }
                }
                if (f < 1.0f) {
                    int dp2 = AndroidUtilities.dp(36.0f);
                    this.rect1.set((getMeasuredWidth() - dp2) / 2, dp, (getMeasuredWidth() + dp2) / 2, dp + AndroidUtilities.dp(4.0f));
                    Paint paint = Theme.dialogs_onlineCirclePaint;
                    ShareAlert shareAlert2 = ShareAlert.this;
                    paint.setColor(shareAlert2.getThemedColor(shareAlert2.darkTheme ? Theme.key_voipgroup_scrollUp : Theme.key_sheet_scrollUp));
                    Theme.dialogs_onlineCirclePaint.setAlpha((int) (r0.getAlpha() * (1.0f - f)));
                    canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    int systemUiVisibility = getSystemUiVisibility();
                    boolean z6 = this.lightStatusBar && ((float) 0) > ((float) AndroidUtilities.statusBarHeight) * 0.5f;
                    if (z6 != ((systemUiVisibility & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) > 0)) {
                        setSystemUiVisibility(z6 ? systemUiVisibility | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : systemUiVisibility & (-8193));
                    }
                }
                canvas.restore();
                this.previousTopOffset = this.topOffset;
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (this.fullHeight ? motionEvent.getAction() != 0 || ShareAlert.this.scrollOffsetY == 0 || motionEvent.getY() >= ShareAlert.this.scrollOffsetY - AndroidUtilities.dp(30.0f) : motionEvent.getAction() != 0 || motionEvent.getY() >= this.topOffset - AndroidUtilities.dp(30.0f)) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                ShareAlert.this.dismiss();
                return true;
            }

            /* JADX WARN: Removed duplicated region for block: B:24:0x008c  */
            /* JADX WARN: Removed duplicated region for block: B:31:0x00c0  */
            /* JADX WARN: Removed duplicated region for block: B:35:0x00d2  */
            /* JADX WARN: Removed duplicated region for block: B:37:0x00dc  */
            /* JADX WARN: Removed duplicated region for block: B:44:0x00ab  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onLayout(boolean z5, int i5, int i6, int i7, int i8) {
                int i9;
                int i10;
                int i11;
                int i12;
                int i13;
                int childCount = getChildCount();
                int measureKeyboardHeight = measureKeyboardHeight();
                int emojiPadding = (((BottomSheet) ShareAlert.this).keyboardVisible || measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : ShareAlert.this.commentTextView.getEmojiPadding();
                setBottomClip(emojiPadding);
                for (int i14 = 0; i14 < childCount; i14++) {
                    View childAt = getChildAt(i14);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i15 = layoutParams.gravity;
                        if (i15 == -1) {
                            i15 = 51;
                        }
                        int i16 = i15 & 112;
                        int i17 = i15 & 7;
                        if (i17 == 1) {
                            i9 = (((i7 - i5) - measuredWidth) / 2) + layoutParams.leftMargin;
                            i10 = layoutParams.rightMargin;
                        } else if (i17 != 5) {
                            i11 = layoutParams.leftMargin + getPaddingLeft();
                            if (i16 == 16) {
                                if (i16 == 48) {
                                    i13 = layoutParams.topMargin + getPaddingTop() + this.topOffset;
                                } else if (i16 != 80) {
                                    i13 = layoutParams.topMargin;
                                } else {
                                    i12 = ((i8 - emojiPadding) - i6) - measuredHeight;
                                }
                                if (ShareAlert.this.commentTextView != null && ShareAlert.this.commentTextView.isPopupView(childAt)) {
                                    i13 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                                }
                                childAt.layout(i11, i13, measuredWidth + i11, measuredHeight + i13);
                            } else {
                                i12 = ((((i8 - emojiPadding) - (this.topOffset + i6)) - measuredHeight) / 2) + layoutParams.topMargin;
                            }
                            i13 = i12 - layoutParams.bottomMargin;
                            if (ShareAlert.this.commentTextView != null) {
                                i13 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                            }
                            childAt.layout(i11, i13, measuredWidth + i11, measuredHeight + i13);
                        } else {
                            i9 = (((i7 - i5) - measuredWidth) - layoutParams.rightMargin) - getPaddingRight();
                            i10 = ((BottomSheet) ShareAlert.this).backgroundPaddingLeft;
                        }
                        i11 = i9 - i10;
                        if (i16 == 16) {
                        }
                        i13 = i12 - layoutParams.bottomMargin;
                        if (ShareAlert.this.commentTextView != null) {
                        }
                        childAt.layout(i11, i13, measuredWidth + i11, measuredHeight + i13);
                    }
                }
                notifyHeightChanged();
                ShareAlert.this.updateLayout();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i5, int i6) {
                int dp;
                int size2 = getLayoutParams().height > 0 ? getLayoutParams().height : View.MeasureSpec.getSize(i6);
                ShareAlert.this.layoutManager.setNeedFixGap(getLayoutParams().height <= 0);
                ShareAlert.this.searchLayoutManager.setNeedFixGap(getLayoutParams().height <= 0);
                if (Build.VERSION.SDK_INT >= 21 && !((BottomSheet) ShareAlert.this).isFullscreen) {
                    this.ignoreLayout = true;
                    setPadding(((BottomSheet) ShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) ShareAlert.this).backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
                int paddingTop = size2 - getPaddingTop();
                int dp2 = AndroidUtilities.dp(103.0f) + AndroidUtilities.dp(48.0f) + (Math.max(2, (int) Math.ceil(Math.max(ShareAlert.this.searchAdapter.getItemCount(), ShareAlert.this.listAdapter.getItemCount() - 1) / 4.0f)) * AndroidUtilities.dp(103.0f)) + ((BottomSheet) ShareAlert.this).backgroundPaddingTop;
                if (ShareAlert.this.topicsGridView.getVisibility() != 8 && (dp = AndroidUtilities.dp(103.0f) + AndroidUtilities.dp(48.0f) + (Math.max(2, (int) Math.ceil((ShareAlert.this.shareTopicsAdapter.getItemCount() - 1) / 4.0f)) * AndroidUtilities.dp(103.0f)) + ((BottomSheet) ShareAlert.this).backgroundPaddingTop) > dp2) {
                    dp2 = AndroidUtilities.lerp(dp2, dp, ShareAlert.this.topicsGridView.getAlpha());
                }
                int dp3 = (dp2 < paddingTop ? 0 : paddingTop - ((paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f);
                if (ShareAlert.this.gridView.getPaddingTop() != dp3) {
                    this.ignoreLayout = true;
                    ShareAlert.this.gridView.setPadding(0, dp3, 0, AndroidUtilities.dp(48.0f));
                    ShareAlert.this.topicsGridView.setPadding(0, dp3, 0, AndroidUtilities.dp(48.0f));
                    this.ignoreLayout = false;
                }
                if (((BottomSheet) ShareAlert.this).keyboardVisible && getLayoutParams().height <= 0 && ShareAlert.this.searchGridView.getPaddingTop() != dp3) {
                    this.ignoreLayout = true;
                    ShareAlert.this.searchGridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
                    this.ignoreLayout = false;
                }
                boolean z5 = dp2 >= size2;
                this.fullHeight = z5;
                this.topOffset = z5 ? 0 : size2 - dp2;
                this.ignoreLayout = true;
                ShareAlert.this.checkCurrentList(false);
                this.ignoreLayout = false;
                setMeasuredDimension(View.MeasureSpec.getSize(i5), size2);
                onMeasureInternal(i5, View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !ShareAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.sizeNotifierFrameLayout = sizeNotifierFrameLayout;
        this.containerView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setWillNotDraw(false);
        this.containerView.setClipChildren(false);
        ViewGroup viewGroup = this.containerView;
        int i5 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i5, 0, i5, 0);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground));
        if (this.darkTheme && this.linkToCopy[1] != null) {
            SwitchView switchView = new SwitchView(context) { // from class: org.telegram.ui.Components.ShareAlert.3
                @Override // org.telegram.ui.Components.ShareAlert.SwitchView
                protected void onTabSwitch(int i6) {
                    TextView textView2;
                    int i7;
                    if (ShareAlert.this.pickerBottomLayout == null) {
                        return;
                    }
                    if (i6 == 0) {
                        textView2 = ShareAlert.this.pickerBottomLayout;
                        i7 = R.string.VoipGroupCopySpeakerLink;
                    } else {
                        textView2 = ShareAlert.this.pickerBottomLayout;
                        i7 = R.string.VoipGroupCopyListenLink;
                    }
                    textView2.setText(LocaleController.getString(i7).toUpperCase());
                }
            };
            this.switchView = switchView;
            this.frameLayout.addView(switchView, LayoutHelper.createFrame(-1, 36.0f, 51, 0.0f, 11.0f, 0.0f, 0.0f));
        }
        SearchField searchField = new SearchField(context);
        this.searchView = searchField;
        this.frameLayout.addView(searchField, LayoutHelper.createFrame(-1, 58, 83));
        ActionBar actionBar = new ActionBar(context);
        this.topicsBackActionBar = actionBar;
        actionBar.setOccupyStatusBar(false);
        this.topicsBackActionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.topicsBackActionBar.setTitleColor(getThemedColor(Theme.key_dialogTextBlack));
        this.topicsBackActionBar.setSubtitleColor(getThemedColor(Theme.key_dialogTextGray2));
        this.topicsBackActionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2), false);
        this.topicsBackActionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
        this.topicsBackActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.ShareAlert.4
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i6) {
                ShareAlert.this.onBackPressed();
            }
        });
        this.topicsBackActionBar.setVisibility(8);
        this.frameLayout.addView(this.topicsBackActionBar, LayoutHelper.createFrame(-1, 58, 83));
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider);
        this.topicsGridView = recyclerListView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        this.topicsLayoutManager = gridLayoutManager;
        recyclerListView.setLayoutManager(gridLayoutManager);
        this.topicsLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ShareAlert.5
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i6) {
                if (i6 == 0) {
                    return ShareAlert.this.topicsLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.topicsGridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ShareAlert.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i6, int i7) {
                if (i7 != 0) {
                    ShareAlert.this.updateLayout();
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                }
            }
        });
        RecyclerListView recyclerListView2 = this.topicsGridView;
        ShareTopicsAdapter shareTopicsAdapter = new ShareTopicsAdapter(context);
        this.shareTopicsAdapter = shareTopicsAdapter;
        recyclerListView2.setAdapter(shareTopicsAdapter);
        this.topicsGridView.setGlowColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogScrollGlow));
        this.topicsGridView.setVerticalScrollBarEnabled(false);
        this.topicsGridView.setHorizontalScrollBarEnabled(false);
        this.topicsGridView.setOverScrollMode(2);
        this.topicsGridView.setSelectorDrawableColor(0);
        this.topicsGridView.setItemSelectorColorProvider(new GenericProvider() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda5
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Integer lambda$new$2;
                lambda$new$2 = ShareAlert.lambda$new$2((Integer) obj);
                return lambda$new$2;
            }
        });
        this.topicsGridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.topicsGridView.setClipToPadding(false);
        this.topicsGridView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.ShareAlert.7
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerView.getChildViewHolder(view);
                if (holder == null) {
                    rect.left = AndroidUtilities.dp(4.0f);
                    rect.right = AndroidUtilities.dp(4.0f);
                } else {
                    int adapterPosition = holder.getAdapterPosition() % 4;
                    rect.left = adapterPosition == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    rect.right = adapterPosition != 3 ? AndroidUtilities.dp(4.0f) : 0;
                }
            }
        });
        this.topicsGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i6) {
                ShareAlert.this.lambda$new$3(view, i6);
            }
        });
        this.topicsGridView.setVisibility(8);
        this.containerView.addView(this.topicsGridView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView3 = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.8
            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) (AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
            public void draw(Canvas canvas) {
                if (ShareAlert.this.topicsGridView.getVisibility() != 8) {
                    canvas.save();
                    canvas.clipRect(0, ShareAlert.this.scrollOffsetY + AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f), getWidth(), getHeight());
                }
                super.draw(canvas);
                if (ShareAlert.this.topicsGridView.getVisibility() != 8) {
                    canvas.restore();
                }
            }
        };
        this.gridView = recyclerListView3;
        recyclerListView3.setSelectorDrawableColor(0);
        this.gridView.setItemSelectorColorProvider(new GenericProvider() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda7
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Integer lambda$new$4;
                lambda$new$4 = ShareAlert.lambda$new$4((Integer) obj);
                return lambda$new$4;
            }
        });
        this.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.gridView.setClipToPadding(false);
        RecyclerListView recyclerListView4 = this.gridView;
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 4);
        this.layoutManager = gridLayoutManager2;
        recyclerListView4.setLayoutManager(gridLayoutManager2);
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ShareAlert.9
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i6) {
                if (i6 == 0) {
                    return ShareAlert.this.layoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.gridView.setHorizontalScrollBarEnabled(false);
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.setOverScrollMode(2);
        this.gridView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.ShareAlert.10
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerView.getChildViewHolder(view);
                if (holder == null) {
                    rect.left = AndroidUtilities.dp(4.0f);
                    rect.right = AndroidUtilities.dp(4.0f);
                } else {
                    int adapterPosition = holder.getAdapterPosition() % 4;
                    rect.left = adapterPosition == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    rect.right = adapterPosition != 3 ? AndroidUtilities.dp(4.0f) : 0;
                }
            }
        });
        this.containerView.addView(this.gridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView5 = this.gridView;
        ShareDialogsAdapter shareDialogsAdapter = new ShareDialogsAdapter(context);
        this.listAdapter = shareDialogsAdapter;
        recyclerListView5.setAdapter(shareDialogsAdapter);
        this.gridView.setGlowColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogScrollGlow));
        this.gridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i6) {
                ShareAlert.this.lambda$new$5(view, i6);
            }
        });
        this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ShareAlert.11
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i6, int i7) {
                if (i7 != 0) {
                    ShareAlert.this.updateLayout();
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                }
                if (Bulletin.getVisibleBulletin() == null || Bulletin.getVisibleBulletin().getLayout() == null || !(Bulletin.getVisibleBulletin().getLayout().getParent() instanceof View) || ((View) Bulletin.getVisibleBulletin().getLayout().getParent()).getParent() != ShareAlert.this.bulletinContainer2) {
                    return;
                }
                Bulletin.hideVisible();
            }
        });
        RecyclerListView recyclerListView6 = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.12
            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) (AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
            public void draw(Canvas canvas) {
                if (ShareAlert.this.topicsGridView.getVisibility() != 8) {
                    canvas.save();
                    canvas.clipRect(0, ShareAlert.this.scrollOffsetY + AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f), getWidth(), getHeight());
                }
                super.draw(canvas);
                if (ShareAlert.this.topicsGridView.getVisibility() != 8) {
                    canvas.restore();
                }
            }
        };
        this.searchGridView = recyclerListView6;
        recyclerListView6.setItemSelectorColorProvider(new GenericProvider() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda9
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Integer lambda$new$6;
                lambda$new$6 = ShareAlert.lambda$new$6((Integer) obj);
                return lambda$new$6;
            }
        });
        this.searchGridView.setSelectorDrawableColor(0);
        this.searchGridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.searchGridView.setClipToPadding(false);
        RecyclerListView recyclerListView7 = this.searchGridView;
        FillLastGridLayoutManager fillLastGridLayoutManager = new FillLastGridLayoutManager(getContext(), 4, 0, this.searchGridView);
        this.searchLayoutManager = fillLastGridLayoutManager;
        recyclerListView7.setLayoutManager(fillLastGridLayoutManager);
        this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ShareAlert.13
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i6) {
                return ShareAlert.this.searchAdapter.getSpanSize(4, i6);
            }
        });
        this.searchGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i6) {
                ShareAlert.this.lambda$new$7(view, i6);
            }
        });
        this.searchGridView.setHasFixedSize(true);
        this.searchGridView.setItemAnimator(null);
        this.searchGridView.setHorizontalScrollBarEnabled(false);
        this.searchGridView.setVerticalScrollBarEnabled(false);
        this.searchGridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ShareAlert.14
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i6, int i7) {
                if (i7 != 0) {
                    ShareAlert.this.updateLayout();
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                }
            }
        });
        this.searchGridView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.ShareAlert.15
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerView.getChildViewHolder(view);
                if (holder == null) {
                    rect.left = AndroidUtilities.dp(4.0f);
                    rect.right = AndroidUtilities.dp(4.0f);
                    return;
                }
                if (holder.getItemViewType() != 5) {
                    rect.right = 0;
                    rect.left = 0;
                } else {
                    int adapterPosition = holder.getAdapterPosition() % 4;
                    rect.left = adapterPosition == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    rect.right = adapterPosition != 3 ? AndroidUtilities.dp(4.0f) : 0;
                }
            }
        });
        this.searchGridView.setAdapter(this.searchAdapter);
        this.searchGridView.setGlowColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogScrollGlow));
        this.recyclerItemsEnterAnimator = new RecyclerItemsEnterAnimator(this.searchGridView, true);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context, resourcesProvider);
        flickerLoadingView.setViewType(12);
        if (this.darkTheme) {
            flickerLoadingView.setColors(Theme.key_voipgroup_inviteMembersBackground, Theme.key_voipgroup_searchBackground, -1);
        }
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, flickerLoadingView, 1, resourcesProvider);
        this.searchEmptyView = stickerEmptyView;
        stickerEmptyView.addView(flickerLoadingView, 0);
        this.searchEmptyView.setAnimateLayoutChange(true);
        this.searchEmptyView.showProgress(false, false);
        if (this.darkTheme) {
            this.searchEmptyView.title.setTextColor(getThemedColor(Theme.key_voipgroup_nameText));
        }
        this.searchEmptyView.title.setText(LocaleController.getString(R.string.NoResult));
        this.searchGridView.setEmptyView(this.searchEmptyView);
        this.searchGridView.setHideIfEmpty(false);
        this.searchGridView.setAnimateEmptyView(true, 0);
        this.containerView.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
        this.containerView.addView(this.searchGridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp((!this.darkTheme || this.linkToCopy[1] == null) ? 58.0f : 111.0f);
        this.shadow[0] = new View(context);
        View view = this.shadow[0];
        int i6 = Theme.key_dialogShadowLine;
        view.setBackgroundColor(getThemedColor(i6));
        this.shadow[0].setAlpha(0.0f);
        this.shadow[0].setTag(1);
        this.containerView.addView(this.shadow[0], layoutParams);
        this.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, (!this.darkTheme || this.linkToCopy[1] == null) ? 58 : 111, 51));
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
        layoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
        this.shadow[1] = new View(context);
        this.shadow[1].setBackgroundColor(getThemedColor(i6));
        this.containerView.addView(this.shadow[1], layoutParams2);
        if (this.isChannel || this.linkToCopy[0] != null) {
            TextView textView2 = new TextView(context);
            this.pickerBottomLayout = textView2;
            textView2.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(getThemedColor(this.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground), getThemedColor(this.darkTheme ? Theme.key_voipgroup_listSelector : Theme.key_listSelector)));
            this.pickerBottomLayout.setTextColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listeningText : Theme.key_dialogTextBlue2));
            this.pickerBottomLayout.setTextSize(1, 14.0f);
            this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.pickerBottomLayout.setTypeface(AndroidUtilities.bold());
            this.pickerBottomLayout.setGravity(17);
            if (!this.darkTheme || this.linkToCopy[1] == null) {
                textView = this.pickerBottomLayout;
                i = R.string.CopyLink;
            } else {
                textView = this.pickerBottomLayout;
                i = R.string.VoipGroupCopySpeakerLink;
            }
            textView.setText(LocaleController.getString(i).toUpperCase());
            this.pickerBottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda11
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.this.lambda$new$8(view2);
                }
            });
            this.containerView.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && ChatObject.hasAdminRights(chatActivity2.getCurrentChat()) && this.sendingMessageObjects.size() > 0 && ((MessageObject) this.sendingMessageObjects.get(0)).messageOwner.forwards > 0) {
                final MessageObject messageObject2 = (MessageObject) this.sendingMessageObjects.get(0);
                if (!messageObject2.isForwarded()) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    this.sharesCountLayout = linearLayout;
                    linearLayout.setOrientation(0);
                    this.sharesCountLayout.setGravity(16);
                    this.sharesCountLayout.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listSelector : Theme.key_listSelector), 2));
                    this.containerView.addView(this.sharesCountLayout, LayoutHelper.createFrame(-2, 48.0f, 85, 6.0f, 0.0f, -6.0f, 0.0f));
                    this.sharesCountLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda12
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ShareAlert.this.lambda$new$9(messageObject2, view2);
                        }
                    });
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.share_arrow);
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listeningText : Theme.key_dialogTextBlue2), PorterDuff.Mode.MULTIPLY));
                    this.sharesCountLayout.addView(imageView, LayoutHelper.createLinear(-2, -1, 16, 20, 0, 0, 0));
                    TextView textView3 = new TextView(context);
                    textView3.setText(String.format("%d", Integer.valueOf(messageObject2.messageOwner.forwards)));
                    textView3.setTextSize(1, 14.0f);
                    textView3.setTextColor(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listeningText : Theme.key_dialogTextBlue2));
                    textView3.setGravity(16);
                    textView3.setTypeface(AndroidUtilities.bold());
                    this.sharesCountLayout.addView(textView3, LayoutHelper.createLinear(-2, -1, 16, 8, 0, 20, 0));
                }
            }
        } else {
            this.shadow[1].setAlpha(0.0f);
        }
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.bulletinContainer = frameLayout2;
        this.containerView.addView(frameLayout2, LayoutHelper.createFrame(-1, 100.0f, 87, 0.0f, 0.0f, 0.0f, this.pickerBottomLayout != null ? 48.0f : 0.0f));
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.bulletinContainer2 = frameLayout3;
        this.containerView.addView(frameLayout3, LayoutHelper.createFrame(-1, -2.0f, 55, 0.0f, 0.0f, 0.0f, 0.0f));
        16 r2 = new 16(context);
        this.frameLayout2 = r2;
        r2.setWillNotDraw(false);
        this.frameLayout2.setAlpha(0.0f);
        this.frameLayout2.setVisibility(4);
        this.containerView.addView(this.frameLayout2, LayoutHelper.createFrame(-1, -2, 83));
        this.frameLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda13
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean lambda$new$10;
                lambda$new$10 = ShareAlert.lambda$new$10(view2, motionEvent);
                return lambda$new$10;
            }
        });
        17 r22 = new 17(context, this.sizeNotifierFrameLayout, null, 1, true, resourcesProvider);
        this.commentTextView = r22;
        if (this.darkTheme) {
            EditTextCaption editText = r22.getEditText();
            int i7 = Theme.key_voipgroup_nameText;
            editText.setTextColor(getThemedColor(i7));
            this.commentTextView.getEditText().setCursorColor(getThemedColor(i7));
        }
        this.commentTextView.setBackgroundColor(themedColor);
        this.commentTextView.setHint(LocaleController.getString(R.string.ShareComment));
        this.commentTextView.onResume();
        this.commentTextView.setPadding(0, 0, AndroidUtilities.dp(84.0f), 0);
        this.frameLayout2.addView(this.commentTextView, LayoutHelper.createFrame(-1, -2, 51));
        this.frameLayout2.setClipChildren(false);
        this.frameLayout2.setClipToPadding(false);
        this.commentTextView.setClipChildren(false);
        FrameLayout frameLayout4 = new FrameLayout(context) { // from class: org.telegram.ui.Components.ShareAlert.18
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setText(LocaleController.formatPluralString("AccDescrShareInChats", ShareAlert.this.selectedDialogs.size(), new Object[0]));
                accessibilityNodeInfo.setClassName(Button.class.getName());
                accessibilityNodeInfo.setLongClickable(true);
                accessibilityNodeInfo.setClickable(true);
            }
        };
        this.writeButtonContainer = frameLayout4;
        frameLayout4.setFocusable(true);
        this.writeButtonContainer.setFocusableInTouchMode(true);
        this.writeButtonContainer.setVisibility(4);
        this.writeButtonContainer.setScaleX(0.2f);
        this.writeButtonContainer.setScaleY(0.2f);
        this.writeButtonContainer.setAlpha(0.0f);
        this.containerView.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
        final ImageView imageView2 = new ImageView(context);
        int dp = AndroidUtilities.dp(56.0f);
        int i8 = Theme.key_dialogFloatingButton;
        int themedColor2 = getThemedColor(i8);
        int i9 = Build.VERSION.SDK_INT;
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, themedColor2, getThemedColor(i9 >= 21 ? Theme.key_dialogFloatingButtonPressed : i8));
        if (i9 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        imageView2.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        imageView2.setImageResource(R.drawable.attach_send);
        imageView2.setImportantForAccessibility(2);
        imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogFloatingIcon), PorterDuff.Mode.MULTIPLY));
        imageView2.setScaleType(ImageView.ScaleType.CENTER);
        if (i9 >= 21) {
            imageView2.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.ShareAlert.19
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view2, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        this.writeButtonContainer.addView(imageView2, LayoutHelper.createFrame(i9 >= 21 ? 56 : 60, i9 < 21 ? 60.0f : 56.0f, 51, i9 >= 21 ? 2.0f : 0.0f, 0.0f, 0.0f, 0.0f));
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlert.this.lambda$new$11(view2);
            }
        });
        imageView2.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda4
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view2) {
                boolean lambda$new$12;
                lambda$new$12 = ShareAlert.this.lambda$new$12(imageView2, view2);
                return lambda$new$12;
            }
        });
        this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.bold());
        View view2 = new View(context) { // from class: org.telegram.ui.Components.ShareAlert.20
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                String format = String.format("%d", Integer.valueOf(Math.max(1, ShareAlert.this.selectedDialogs.size())));
                int max = Math.max(AndroidUtilities.dp(16.0f) + ((int) Math.ceil(ShareAlert.this.textPaint.measureText(format))), AndroidUtilities.dp(24.0f));
                int measuredWidth = getMeasuredWidth() / 2;
                getMeasuredHeight();
                ShareAlert.this.textPaint.setColor(ShareAlert.this.getThemedColor(Theme.key_dialogRoundCheckBoxCheck));
                Paint paint = ShareAlert.this.paint;
                ShareAlert shareAlert = ShareAlert.this;
                paint.setColor(shareAlert.getThemedColor(shareAlert.darkTheme ? Theme.key_voipgroup_inviteMembersBackground : Theme.key_dialogBackground));
                int i10 = max / 2;
                ShareAlert.this.rect.set(measuredWidth - i10, 0.0f, i10 + measuredWidth, getMeasuredHeight());
                canvas.drawRoundRect(ShareAlert.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), ShareAlert.this.paint);
                ShareAlert.this.paint.setColor(ShareAlert.this.getThemedColor(Theme.key_dialogFloatingButton));
                ShareAlert.this.rect.set(r5 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), r2 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
                canvas.drawRoundRect(ShareAlert.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), ShareAlert.this.paint);
                canvas.drawText(format, measuredWidth - (r1 / 2), AndroidUtilities.dp(16.2f), ShareAlert.this.textPaint);
            }
        };
        this.selectedCountView = view2;
        view2.setAlpha(0.0f);
        this.selectedCountView.setScaleX(0.2f);
        this.selectedCountView.setScaleY(0.2f);
        this.containerView.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -8.0f, 9.0f));
        updateSelectedCount(0);
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        if (this.listAdapter.dialogs.isEmpty()) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
        }
        DialogsSearchAdapter.loadRecentSearch(this.currentAccount, 0, new DialogsSearchAdapter.OnRecentSearchLoaded() { // from class: org.telegram.ui.Components.ShareAlert.21
            @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.OnRecentSearchLoaded
            public void setRecentSearch(ArrayList arrayList3, LongSparseArray longSparseArray) {
                if (arrayList3 != null) {
                    int i10 = 0;
                    while (i10 < arrayList3.size()) {
                        TLObject tLObject = ((DialogsSearchAdapter.RecentSearchObject) arrayList3.get(i10)).object;
                        if ((tLObject instanceof TLRPC.Chat) && !ChatObject.canWriteToChat((TLRPC.Chat) tLObject)) {
                            arrayList3.remove(i10);
                            i10--;
                        }
                        i10++;
                    }
                }
                ShareAlert.this.recentSearchObjects = arrayList3;
                ShareAlert.this.recentSearchObjectsById = longSparseArray;
                for (int i11 = 0; i11 < ShareAlert.this.recentSearchObjects.size(); i11++) {
                    DialogsSearchAdapter.RecentSearchObject recentSearchObject = (DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i11);
                    TLObject tLObject2 = recentSearchObject.object;
                    if (tLObject2 instanceof TLRPC.User) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putUser((TLRPC.User) recentSearchObject.object, true);
                    } else if (tLObject2 instanceof TLRPC.Chat) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putChat((TLRPC.Chat) recentSearchObject.object, true);
                    } else if (tLObject2 instanceof TLRPC.EncryptedChat) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putEncryptedChat((TLRPC.EncryptedChat) recentSearchObject.object, true);
                    }
                }
                ShareAlert.this.searchAdapter.notifyDataSetChanged();
            }
        });
        MediaDataController.getInstance(this.currentAccount).loadHints(true);
        AndroidUtilities.updateViewVisibilityAnimated(this.gridView, true, 1.0f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, false, 1.0f, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCurrentList(boolean z) {
        LinearLayoutManager linearLayoutManager;
        int paddingTop;
        boolean z2 = true;
        if (!TextUtils.isEmpty(this.searchView.searchEditText.getText()) || ((this.keyboardVisible && this.searchView.searchEditText.hasFocus()) || this.searchWasVisibleBeforeTopics)) {
            this.updateSearchAdapter = true;
            if (this.selectedTopicDialog == null) {
                AndroidUtilities.updateViewVisibilityAnimated(this.gridView, false, 0.98f, true);
                AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, true);
            }
        } else {
            if (this.selectedTopicDialog == null) {
                AndroidUtilities.updateViewVisibilityAnimated(this.gridView, true, 0.98f, true);
                AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, false);
            }
            z2 = false;
        }
        if (this.searchIsVisible != z2 || z) {
            this.searchIsVisible = z2;
            this.searchAdapter.notifyDataSetChanged();
            this.listAdapter.notifyDataSetChanged();
            if (!this.searchIsVisible) {
                this.layoutManager.scrollToPositionWithOffset(0, 0);
                return;
            }
            if (this.lastOffset == Integer.MAX_VALUE) {
                linearLayoutManager = (LinearLayoutManager) this.searchGridView.getLayoutManager();
                paddingTop = -this.searchGridView.getPaddingTop();
            } else {
                linearLayoutManager = (LinearLayoutManager) this.searchGridView.getLayoutManager();
                paddingTop = this.lastOffset - this.searchGridView.getPaddingTop();
            }
            linearLayoutManager.scrollToPositionWithOffset(0, paddingTop);
            this.searchAdapter.searchDialogs(this.searchView.searchEditText.getText().toString());
        }
    }

    private void collapseTopics() {
        TLRPC.Dialog dialog = this.selectedTopicDialog;
        if (dialog == null) {
            return;
        }
        final View view = null;
        this.selectedTopicDialog = null;
        for (int i = 0; i < getMainGridView().getChildCount(); i++) {
            View childAt = getMainGridView().getChildAt(i);
            if ((childAt instanceof ShareDialogCell) && ((ShareDialogCell) childAt).getCurrentDialog() == dialog.id) {
                view = childAt;
            }
        }
        if (view == null) {
            return;
        }
        SpringAnimation springAnimation = this.topicsAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
        }
        getMainGridView().setVisibility(0);
        this.searchView.setVisibility(0);
        if (this.searchIsVisible || this.searchWasVisibleBeforeTopics) {
            this.sizeNotifierFrameLayout.adjustPanLayoutHelper.ignoreOnce();
            this.searchView.searchEditText.requestFocus();
            AndroidUtilities.showKeyboard(this.searchView.searchEditText);
        }
        final int[] iArr = new int[2];
        SpringAnimation springAnimation2 = new SpringAnimation(new FloatValueHolder(1000.0f));
        SpringForce springForce = new SpringForce(0.0f);
        ChatActivity chatActivity = this.parentFragment;
        SpringAnimation spring = springAnimation2.setSpring(springForce.setStiffness((chatActivity == null || !chatActivity.shareAlertDebugTopicsSlowMotion) ? 800.0f : 10.0f).setDampingRatio(1.0f));
        this.topicsAnimation = spring;
        spring.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda0
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                ShareAlert.this.lambda$collapseTopics$16(view, iArr, dynamicAnimation, f, f2);
            }
        });
        this.topicsAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda1
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                ShareAlert.this.lambda$collapseTopics$17(dynamicAnimation, z, f, f2);
            }
        });
        this.topicsAnimation.start();
    }

    private void copyLink(Context context) {
        final boolean z = false;
        if (this.exportedMessageLink == null && this.linkToCopy[0] == null) {
            return;
        }
        try {
            SwitchView switchView = this.switchView;
            String str = switchView != null ? this.linkToCopy[switchView.currentTab] : this.linkToCopy[0];
            ClipboardManager clipboardManager = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
            if (str == null) {
                str = this.exportedMessageLink.link;
            }
            clipboardManager.setPrimaryClip(ClipData.newPlainText("label", str));
            ShareAlertDelegate shareAlertDelegate = this.delegate;
            if ((shareAlertDelegate == null || !shareAlertDelegate.didCopy()) && (this.parentActivity instanceof LaunchActivity)) {
                TLRPC.TL_exportedMessageLink tL_exportedMessageLink = this.exportedMessageLink;
                if (tL_exportedMessageLink != null && tL_exportedMessageLink.link.contains("/c/")) {
                    z = true;
                }
                ((LaunchActivity) this.parentActivity).showBulletin(new Function() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda14
                    @Override // androidx.arch.core.util.Function
                    public final Object apply(Object obj) {
                        Bulletin lambda$copyLink$24;
                        lambda$copyLink$24 = ShareAlert.this.lambda$copyLink$24(z, (BulletinFactory) obj);
                        return lambda$copyLink$24;
                    }
                });
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static ShareAlert createShareAlert(Context context, MessageObject messageObject, String str, boolean z, String str2, boolean z2) {
        ArrayList arrayList;
        if (messageObject != null) {
            arrayList = new ArrayList();
            arrayList.add(messageObject);
        } else {
            arrayList = null;
        }
        return new ShareAlert(context, null, arrayList, str, null, z, str2, null, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentTop() {
        if (this.gridView.getChildCount() == 0) {
            return -1000;
        }
        int i = 0;
        View childAt = this.gridView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.gridView.findContainingViewHolder(childAt);
        if (holder == null) {
            return -1000;
        }
        int paddingTop = this.gridView.getPaddingTop();
        if (holder.getLayoutPosition() == 0 && childAt.getTop() >= 0) {
            i = childAt.getTop();
        }
        return paddingTop - i;
    }

    private RecyclerListView getMainGridView() {
        return (this.searchIsVisible || this.searchWasVisibleBeforeTopics) ? this.searchGridView : this.gridView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateTopicsAnimation(View view, int[] iArr, float f) {
        this.topicsGridView.setPivotX(view.getX() + (view.getWidth() / 2.0f));
        this.topicsGridView.setPivotY(view.getY() + (view.getHeight() / 2.0f));
        float f2 = 0.25f * f;
        float f3 = 0.75f + f2;
        this.topicsGridView.setScaleX(f3);
        this.topicsGridView.setScaleY(f3);
        this.topicsGridView.setAlpha(f);
        RecyclerListView mainGridView = getMainGridView();
        mainGridView.setPivotX(view.getX() + (view.getWidth() / 2.0f));
        mainGridView.setPivotY(view.getY() + (view.getHeight() / 2.0f));
        float f4 = f2 + 1.0f;
        mainGridView.setScaleX(f4);
        mainGridView.setScaleY(f4);
        float f5 = 1.0f - f;
        mainGridView.setAlpha(f5);
        this.searchView.setPivotX(r4.getWidth() / 2.0f);
        this.searchView.setPivotY(0.0f);
        float f6 = (0.1f * f5) + 0.9f;
        this.searchView.setScaleX(f6);
        this.searchView.setScaleY(f6);
        this.searchView.setAlpha(f5);
        this.topicsBackActionBar.getBackButton().setTranslationX((-AndroidUtilities.dp(16.0f)) * f5);
        this.topicsBackActionBar.getTitleTextView().setTranslationY(AndroidUtilities.dp(16.0f) * f5);
        this.topicsBackActionBar.getSubtitleTextView().setTranslationY(AndroidUtilities.dp(16.0f) * f5);
        this.topicsBackActionBar.setAlpha(f);
        this.topicsGridView.getLocationInWindow(iArr);
        float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f);
        for (int i = 0; i < mainGridView.getChildCount(); i++) {
            View childAt = mainGridView.getChildAt(i);
            if (childAt instanceof ShareDialogCell) {
                childAt.setTranslationX((childAt.getX() - view.getX()) * 0.5f * interpolation);
                childAt.setTranslationY((childAt.getY() - view.getY()) * 0.5f * interpolation);
                if (childAt != view) {
                    childAt.setAlpha(1.0f - (Math.min(f, 0.5f) / 0.5f));
                } else {
                    childAt.setAlpha(f5);
                }
            }
        }
        for (int i2 = 0; i2 < this.topicsGridView.getChildCount(); i2++) {
            View childAt2 = this.topicsGridView.getChildAt(i2);
            if (childAt2 instanceof ShareTopicCell) {
                double d = -(childAt2.getX() - view.getX());
                double d2 = 1.0f - interpolation;
                double pow = Math.pow(d2, 2.0d);
                Double.isNaN(d);
                childAt2.setTranslationX((float) (d * pow));
                double d3 = -((childAt2.getY() + this.topicsGridView.getTranslationY()) - view.getY());
                double pow2 = Math.pow(d2, 2.0d);
                Double.isNaN(d3);
                childAt2.setTranslationY((float) (d3 * pow2));
            }
        }
        this.containerView.requestLayout();
        mainGridView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collapseTopics$16(View view, int[] iArr, DynamicAnimation dynamicAnimation, float f, float f2) {
        invalidateTopicsAnimation(view, iArr, f / 1000.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collapseTopics$17(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.topicsGridView.setVisibility(8);
        this.topicsBackActionBar.setVisibility(8);
        this.shareTopicsAdapter.topics = null;
        this.shareTopicsAdapter.notifyDataSetChanged();
        this.topicsAnimation = null;
        this.searchWasVisibleBeforeTopics = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Bulletin lambda$copyLink$24(boolean z, BulletinFactory bulletinFactory) {
        return bulletinFactory.createCopyLinkBulletin(z, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLObject tLObject, Context context) {
        if (tLObject != null) {
            this.exportedMessageLink = (TLRPC.TL_exportedMessageLink) tLObject;
            if (this.copyLinkOnEnd) {
                copyLink(context);
            }
        }
        this.loadingLink = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final Context context, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                ShareAlert.this.lambda$new$0(tLObject, context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$10(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(View view) {
        sendInternal(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$12(ImageView imageView, View view) {
        return onSendLongClick(imageView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$2(Integer num) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view, int i) {
        TLRPC.Dialog dialog;
        TLRPC.TL_forumTopic item = this.shareTopicsAdapter.getItem(i);
        if (item == null || (dialog = this.selectedTopicDialog) == null) {
            return;
        }
        this.selectedDialogs.put(dialog.id, dialog);
        this.selectedDialogTopics.put(dialog, item);
        updateSelectedCount(2);
        if (this.searchIsVisible || this.searchWasVisibleBeforeTopics) {
            if (((TLRPC.Dialog) this.listAdapter.dialogsMap.get(dialog.id)) == null) {
                this.listAdapter.dialogsMap.put(dialog.id, dialog);
                this.listAdapter.dialogs.add(!this.listAdapter.dialogs.isEmpty() ? 1 : 0, dialog);
            }
            this.listAdapter.notifyDataSetChanged();
            this.updateSearchAdapter = false;
            this.searchView.searchEditText.setText("");
            checkCurrentList(false);
        }
        for (int i2 = 0; i2 < getMainGridView().getChildCount(); i2++) {
            View childAt = getMainGridView().getChildAt(i2);
            if (childAt instanceof ShareDialogCell) {
                ShareDialogCell shareDialogCell = (ShareDialogCell) childAt;
                if (shareDialogCell.getCurrentDialog() == this.selectedTopicDialog.id) {
                    shareDialogCell.setTopic(item, true);
                    shareDialogCell.setChecked(true, true);
                }
            }
        }
        collapseTopics();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$4(Integer num) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view, int i) {
        TLRPC.Dialog item;
        if (i >= 0 && (item = this.listAdapter.getItem(i)) != null) {
            selectDialog(view, item);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$new$6(Integer num) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view, int i) {
        TLRPC.Dialog item;
        if (i >= 0 && (item = this.searchAdapter.getItem(i)) != null) {
            selectDialog(view, item);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(View view) {
        if (this.selectedDialogs.size() == 0) {
            if (this.isChannel || this.linkToCopy[0] != null) {
                dismiss();
                if (this.linkToCopy[0] != null || !this.loadingLink) {
                    copyLink(getContext());
                } else {
                    this.copyLinkOnEnd = true;
                    Toast.makeText(getContext(), LocaleController.getString(R.string.Loading), 0).show();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(MessageObject messageObject, View view) {
        this.parentFragment.presentFragment(new MessageStatisticActivity(messageObject));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$18(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$19(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, View view) {
        this.showSendersName = true;
        actionBarMenuSubItem.setChecked(true);
        actionBarMenuSubItem2.setChecked(!this.showSendersName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$20(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, View view) {
        this.showSendersName = false;
        actionBarMenuSubItem.setChecked(false);
        actionBarMenuSubItem2.setChecked(!this.showSendersName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$21(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$22(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendInternal(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$23(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendInternal(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$selectDialog$15(AtomicReference atomicReference, NotificationCenter.NotificationCenterDelegate notificationCenterDelegate, TLRPC.Dialog dialog) {
        atomicReference.set(null);
        notificationCenterDelegate.didReceivedNotification(NotificationCenter.topicsDidLoaded, this.currentAccount, Long.valueOf(-dialog.id));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showPremiumBlockedToast$13() {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment != null) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new PremiumPreviewFragment("noncontacts"), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$14() {
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                ShareAlert.lambda$showPremiumBlockedToast$13();
            }
        };
        if (!isKeyboardVisible()) {
            runnable.run();
            return;
        }
        SearchField searchField = this.searchView;
        if (searchField != null) {
            AndroidUtilities.hideKeyboard(searchField.searchEditText);
        }
        AndroidUtilities.runOnUIThread(runnable, 300L);
    }

    private boolean onSendLongClick(View view) {
        ChatActivity chatActivity;
        if (this.parentActivity == null) {
            return false;
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        if (this.sendingMessageObjects != null) {
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarPopupWindowLayout.setBackgroundColor(getThemedColor(Theme.key_voipgroup_inviteMembersBackground));
            }
            actionBarPopupWindowLayout.setAnimationEnabled(false);
            actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ShareAlert.23
                private android.graphics.Rect popupRect = new android.graphics.Rect();

                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view2, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() != 0 || ShareAlert.this.sendPopupWindow == null || !ShareAlert.this.sendPopupWindow.isShowing()) {
                        return false;
                    }
                    view2.getHitRect(this.popupRect);
                    if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        return false;
                    }
                    ShareAlert.this.sendPopupWindow.dismiss();
                    return false;
                }
            });
            actionBarPopupWindowLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda16
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
                public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                    ShareAlert.this.lambda$onSendLongClick$18(keyEvent);
                }
            });
            actionBarPopupWindowLayout.setShownFromBottom(false);
            final ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, true, false, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarMenuSubItem.setTextColor(getThemedColor(Theme.key_voipgroup_nameText));
            }
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.ShowSendersName), 0);
            this.showSendersName = true;
            actionBarMenuSubItem.setChecked(true);
            final ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), true, false, true, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarMenuSubItem2.setTextColor(getThemedColor(Theme.key_voipgroup_nameText));
            }
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
            actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.HideSendersName), 0);
            actionBarMenuSubItem2.setChecked(!this.showSendersName);
            actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.this.lambda$onSendLongClick$19(actionBarMenuSubItem, actionBarMenuSubItem2, view2);
                }
            });
            actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.this.lambda$onSendLongClick$20(actionBarMenuSubItem, actionBarMenuSubItem2, view2);
                }
            });
            actionBarPopupWindowLayout.setupRadialSelectors(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listSelector : Theme.key_dialogButtonSelector));
            linearLayout.addView(actionBarPopupWindowLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, -8.0f));
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarPopupWindowLayout2.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_inviteMembersBackground));
        }
        actionBarPopupWindowLayout2.setAnimationEnabled(false);
        actionBarPopupWindowLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ShareAlert.24
            private android.graphics.Rect popupRect = new android.graphics.Rect();

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() != 0 || ShareAlert.this.sendPopupWindow == null || !ShareAlert.this.sendPopupWindow.isShowing()) {
                    return false;
                }
                view2.getHitRect(this.popupRect);
                if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    return false;
                }
                ShareAlert.this.sendPopupWindow.dismiss();
                return false;
            }
        });
        actionBarPopupWindowLayout2.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
            public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                ShareAlert.this.lambda$onSendLongClick$21(keyEvent);
            }
        });
        actionBarPopupWindowLayout2.setShownFromBottom(false);
        ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getContext(), true, true, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarMenuSubItem3.setTextColor(getThemedColor(Theme.key_voipgroup_nameText));
            actionBarMenuSubItem3.setIconColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
        }
        actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString(R.string.SendWithoutSound), R.drawable.input_notify_off);
        actionBarMenuSubItem3.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout2.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlert.this.lambda$onSendLongClick$22(view2);
            }
        });
        ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(getContext(), true, true, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarMenuSubItem4.setTextColor(getThemedColor(Theme.key_voipgroup_nameText));
            actionBarMenuSubItem4.setIconColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
        }
        actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString(R.string.SendMessage), R.drawable.msg_send);
        actionBarMenuSubItem4.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout2.addView((View) actionBarMenuSubItem4, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlert.this.lambda$onSendLongClick$23(view2);
            }
        });
        actionBarPopupWindowLayout2.setupRadialSelectors(getThemedColor(this.darkTheme ? Theme.key_voipgroup_listSelector : Theme.key_dialogButtonSelector));
        linearLayout.addView(actionBarPopupWindowLayout2, LayoutHelper.createLinear(-1, -2));
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(linearLayout, -2, -2);
        this.sendPopupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setAnimationEnabled(false);
        this.sendPopupWindow.setAnimationStyle(R.style.PopupContextAnimation2);
        this.sendPopupWindow.setOutsideTouchable(true);
        this.sendPopupWindow.setClippingEnabled(true);
        this.sendPopupWindow.setInputMethodMode(2);
        this.sendPopupWindow.setSoftInputMode(0);
        this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
        SharedConfig.removeScheduledOrNoSoundHint();
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.sendPopupWindow.setFocusable(true);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        this.sendPopupWindow.showAtLocation(view, 51, ((iArr[0] + view.getMeasuredWidth()) - linearLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), (!this.keyboardVisible || (chatActivity = this.parentFragment) == null || chatActivity.contentView.getMeasuredHeight() <= AndroidUtilities.dp(58.0f)) ? (iArr[1] - linearLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f) : iArr[1] + view.getMeasuredHeight());
        this.sendPopupWindow.dimBehind();
        try {
            view.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        return true;
    }

    private void runShadowAnimation(final int i, final boolean z) {
        if ((!z || this.shadow[i].getTag() == null) && (z || this.shadow[i].getTag() != null)) {
            return;
        }
        this.shadow[i].setTag(z ? null : 1);
        if (z) {
            this.shadow[i].setVisibility(0);
        }
        AnimatorSet animatorSet = this.shadowAnimation[i];
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.shadowAnimation[i] = new AnimatorSet();
        this.shadowAnimation[i].playTogether(ObjectAnimator.ofFloat(this.shadow[i], (Property<View, Float>) View.ALPHA, z ? 1.0f : 0.0f));
        this.shadowAnimation[i].setDuration(150L);
        this.shadowAnimation[i].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.25
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (ShareAlert.this.shadowAnimation[i] == null || !ShareAlert.this.shadowAnimation[i].equals(animator)) {
                    return;
                }
                ShareAlert.this.shadowAnimation[i] = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ShareAlert.this.shadowAnimation[i] == null || !ShareAlert.this.shadowAnimation[i].equals(animator)) {
                    return;
                }
                if (!z) {
                    ShareAlert.this.shadow[i].setVisibility(4);
                }
                ShareAlert.this.shadowAnimation[i] = null;
            }
        });
        this.shadowAnimation[i].start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectDialog(View view, final TLRPC.Dialog dialog) {
        DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapterRecycler;
        if (dialog instanceof ShareDialogsAdapter.MyStoryDialog) {
            onShareStory(view);
            return;
        }
        if (dialog != null && (((view instanceof ShareDialogCell) && ((ShareDialogCell) view).isBlocked()) || ((view instanceof ProfileSearchCell) && ((ProfileSearchCell) view).isBlocked()))) {
            showPremiumBlockedToast(view, dialog.id);
            return;
        }
        if (this.topicsGridView.getVisibility() != 8 || this.parentActivity == null) {
            return;
        }
        if (DialogObject.isChatDialog(dialog.id)) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog.id));
            if (ChatObject.isChannel(chat) && !chat.megagroup && (!ChatObject.isCanWriteToChannel(-dialog.id, this.currentAccount) || this.hasPoll == 2)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                builder.setTitle(LocaleController.getString(R.string.SendMessageTitle));
                builder.setMessage(LocaleController.getString(this.hasPoll == 2 ? this.isChannel ? R.string.PublicPollCantForward : ChatObject.isActionBannedByDefault(chat, 10) ? R.string.ErrorSendRestrictedPollsAll : R.string.ErrorSendRestrictedPolls : R.string.ChannelCantSendMessage));
                builder.setNegativeButton(LocaleController.getString(R.string.OK), null);
                builder.show();
                return;
            }
        } else if (DialogObject.isEncryptedDialog(dialog.id) && this.hasPoll != 0) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this.parentActivity);
            builder2.setTitle(LocaleController.getString(R.string.SendMessageTitle));
            builder2.setMessage(LocaleController.getString(this.hasPoll != 0 ? R.string.PollCantForwardSecretChat : R.string.InvoiceCantForwardSecretChat));
            builder2.setNegativeButton(LocaleController.getString(R.string.OK), null);
            builder2.show();
            return;
        }
        if (this.selectedDialogs.indexOfKey(dialog.id) >= 0) {
            this.selectedDialogs.remove(dialog.id);
            this.selectedDialogTopics.remove(dialog);
            if (view instanceof ProfileSearchCell) {
                ((ProfileSearchCell) view).setChecked(false, true);
            } else if (view instanceof ShareDialogCell) {
                ((ShareDialogCell) view).setChecked(false, true);
            }
            updateSelectedCount(1);
        } else {
            if (DialogObject.isChatDialog(dialog.id) && MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog.id)) != null && MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog.id)).forum) {
                this.selectedTopicDialog = dialog;
                this.topicsLayoutManager.scrollToPositionWithOffset(0, this.scrollOffsetY - this.topicsGridView.getPaddingTop());
                final AtomicReference atomicReference = new AtomicReference();
                final 22 r3 = new 22(dialog, atomicReference, view);
                atomicReference.set(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShareAlert.this.lambda$selectDialog$15(atomicReference, r3, dialog);
                    }
                });
                NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
                int i = NotificationCenter.topicsDidLoaded;
                notificationCenter.addObserver(r3, i);
                if (MessagesController.getInstance(this.currentAccount).getTopicsController().getTopics(-dialog.id) != null) {
                    r3.didReceivedNotification(i, this.currentAccount, Long.valueOf(-dialog.id));
                    return;
                } else {
                    MessagesController.getInstance(this.currentAccount).getTopicsController().loadTopics(-dialog.id);
                    AndroidUtilities.runOnUIThread((Runnable) atomicReference.get(), 300L);
                    return;
                }
            }
            this.selectedDialogs.put(dialog.id, dialog);
            if (view instanceof ProfileSearchCell) {
                ((ProfileSearchCell) view).setChecked(true, true);
            } else if (view instanceof ShareDialogCell) {
                ((ShareDialogCell) view).setChecked(true, true);
            }
            updateSelectedCount(2);
            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
            if (this.searchIsVisible) {
                TLRPC.Dialog dialog2 = (TLRPC.Dialog) this.listAdapter.dialogsMap.get(dialog.id);
                if (dialog2 == null) {
                    this.listAdapter.dialogsMap.put(dialog.id, dialog);
                    this.listAdapter.dialogs.add(1 ^ (this.listAdapter.dialogs.isEmpty() ? 1 : 0), dialog);
                } else if (dialog2.id != j) {
                    this.listAdapter.dialogs.remove(dialog2);
                    this.listAdapter.dialogs.add(1 ^ (this.listAdapter.dialogs.isEmpty() ? 1 : 0), dialog2);
                }
                this.listAdapter.notifyDataSetChanged();
                this.updateSearchAdapter = false;
                this.searchView.searchEditText.setText("");
                checkCurrentList(false);
                this.searchView.hideKeyboard();
            }
        }
        ShareSearchAdapter shareSearchAdapter = this.searchAdapter;
        if (shareSearchAdapter == null || (categoryAdapterRecycler = shareSearchAdapter.categoryAdapter) == null) {
            return;
        }
        categoryAdapterRecycler.notifyItemRangeChanged(0, categoryAdapterRecycler.getItemCount());
    }

    private boolean showCommentTextView(final boolean z) {
        if (z == (this.frameLayout2.getTag() != null)) {
            return false;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.frameLayout2.setTag(z ? 1 : null);
        if (this.commentTextView.getEditText().isFocused()) {
            AndroidUtilities.hideKeyboard(this.commentTextView.getEditText());
        }
        this.commentTextView.hidePopup(true);
        if (z) {
            this.frameLayout2.setVisibility(0);
            this.writeButtonContainer.setVisibility(0);
        }
        TextView textView = this.pickerBottomLayout;
        if (textView != null) {
            ViewCompat.setImportantForAccessibility(textView, z ? 4 : 1);
        }
        LinearLayout linearLayout = this.sharesCountLayout;
        if (linearLayout != null) {
            ViewCompat.setImportantForAccessibility(linearLayout, z ? 4 : 1);
        }
        this.animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        FrameLayout frameLayout = this.frameLayout2;
        Property property = View.ALPHA;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout, (Property<FrameLayout, Float>) property, z ? 1.0f : 0.0f));
        FrameLayout frameLayout2 = this.writeButtonContainer;
        Property property2 = View.SCALE_X;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout2, (Property<FrameLayout, Float>) property2, z ? 1.0f : 0.2f));
        FrameLayout frameLayout3 = this.writeButtonContainer;
        Property property3 = View.SCALE_Y;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout3, (Property<FrameLayout, Float>) property3, z ? 1.0f : 0.2f));
        arrayList.add(ObjectAnimator.ofFloat(this.writeButtonContainer, (Property<FrameLayout, Float>) property, z ? 1.0f : 0.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.selectedCountView, (Property<View, Float>) property2, z ? 1.0f : 0.2f));
        arrayList.add(ObjectAnimator.ofFloat(this.selectedCountView, (Property<View, Float>) property3, z ? 1.0f : 0.2f));
        arrayList.add(ObjectAnimator.ofFloat(this.selectedCountView, (Property<View, Float>) property, z ? 1.0f : 0.0f));
        TextView textView2 = this.pickerBottomLayout;
        if (textView2 == null || textView2.getVisibility() != 0) {
            arrayList.add(ObjectAnimator.ofFloat(this.shadow[1], (Property<View, Float>) property, z ? 1.0f : 0.0f));
        }
        this.animatorSet.playTogether(arrayList);
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.setDuration(180L);
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.26
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (animator.equals(ShareAlert.this.animatorSet)) {
                    ShareAlert.this.animatorSet = null;
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(ShareAlert.this.animatorSet)) {
                    if (!z) {
                        ShareAlert.this.frameLayout2.setVisibility(4);
                        ShareAlert.this.writeButtonContainer.setVisibility(4);
                    }
                    ShareAlert.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPremiumBlockedToast(View view, long j) {
        int i = -this.shiftDp;
        this.shiftDp = i;
        AndroidUtilities.shakeViewSpring(view, i);
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        String userName = j >= 0 ? UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j))) : "";
        (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() ? BulletinFactory.of(this.bulletinContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName))) : BulletinFactory.of(this.bulletinContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                ShareAlert.this.lambda$showPremiumBlockedToast$14();
            }
        })).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout() {
        if (this.panTranslationMoveLayout) {
            return;
        }
        RecyclerListView recyclerListView = this.searchIsVisible ? this.searchGridView : this.gridView;
        if (recyclerListView.getChildCount() <= 0) {
            return;
        }
        View childAt = recyclerListView.getChildAt(0);
        for (int i = 0; i < recyclerListView.getChildCount(); i++) {
            if (recyclerListView.getChildAt(i).getTop() < childAt.getTop()) {
                childAt = recyclerListView.getChildAt(i);
            }
        }
        RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerListView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(8.0f);
        int i2 = (top <= 0 || holder == null || holder.getAdapterPosition() != 0) ? 0 : top;
        if (top < 0 || holder == null || holder.getAdapterPosition() != 0) {
            this.lastOffset = ConnectionsManager.DEFAULT_DATACENTER_ID;
            runShadowAnimation(0, true);
            top = i2;
        } else {
            this.lastOffset = childAt.getTop();
            runShadowAnimation(0, false);
        }
        if (this.topicsGridView.getVisibility() == 0) {
            RecyclerListView recyclerListView2 = this.topicsGridView;
            if (recyclerListView2.getChildCount() <= 0) {
                return;
            }
            View childAt2 = recyclerListView2.getChildAt(0);
            for (int i3 = 0; i3 < recyclerListView2.getChildCount(); i3++) {
                if (recyclerListView2.getChildAt(i3).getTop() < childAt2.getTop()) {
                    childAt2 = recyclerListView2.getChildAt(i3);
                }
            }
            RecyclerListView.Holder holder2 = (RecyclerListView.Holder) recyclerListView2.findContainingViewHolder(childAt2);
            int top2 = childAt2.getTop() - AndroidUtilities.dp(8.0f);
            int i4 = (top2 <= 0 || holder2 == null || holder2.getAdapterPosition() != 0) ? 0 : top2;
            if (top2 < 0 || holder2 == null || holder2.getAdapterPosition() != 0) {
                this.lastOffset = ConnectionsManager.DEFAULT_DATACENTER_ID;
                runShadowAnimation(0, true);
                top2 = i4;
            } else {
                this.lastOffset = childAt2.getTop();
                runShadowAnimation(0, false);
            }
            top = AndroidUtilities.lerp(top, top2, this.topicsGridView.getAlpha());
        }
        int i5 = this.scrollOffsetY;
        if (i5 != top) {
            this.previousScrollOffsetY = i5;
            RecyclerListView recyclerListView3 = this.gridView;
            float f = top;
            int i6 = (int) (this.currentPanTranslationY + f);
            this.scrollOffsetY = i6;
            recyclerListView3.setTopGlowOffset(i6);
            RecyclerListView recyclerListView4 = this.searchGridView;
            int i7 = (int) (this.currentPanTranslationY + f);
            this.scrollOffsetY = i7;
            recyclerListView4.setTopGlowOffset(i7);
            RecyclerListView recyclerListView5 = this.topicsGridView;
            int i8 = (int) (f + this.currentPanTranslationY);
            this.scrollOffsetY = i8;
            recyclerListView5.setTopGlowOffset(i8);
            this.frameLayout.setTranslationY(this.scrollOffsetY + this.currentPanTranslationY);
            this.searchEmptyView.setTranslationY(this.scrollOffsetY + this.currentPanTranslationY);
            this.containerView.invalidate();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3 = NotificationCenter.dialogsNeedReload;
        if (i == i3) {
            ShareDialogsAdapter shareDialogsAdapter = this.listAdapter;
            if (shareDialogsAdapter != null) {
                shareDialogsAdapter.fetchDialogs();
            }
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, i3);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            AndroidUtilities.hideKeyboard(editTextEmoji.getEditText());
        }
        this.fullyShown = false;
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public int getContainerViewHeight() {
        return this.containerView.getMeasuredHeight() - this.containerViewTop;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (this.selectedTopicDialog != null) {
            collapseTopics();
            return;
        }
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji == null || !editTextEmoji.isPopupShowing()) {
            super.onBackPressed();
        } else {
            this.commentTextView.hidePopup(true);
        }
    }

    protected void onSend(LongSparseArray longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic) {
    }

    protected void onShareStory(View view) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r10v7, types: [org.telegram.messenger.MessageObject] */
    /* JADX WARN: Type inference failed for: r10v9 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v6, types: [boolean] */
    /* JADX WARN: Type inference failed for: r5v46, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v48 */
    /* JADX WARN: Type inference failed for: r5v72, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v74 */
    /* JADX WARN: Type inference failed for: r5v76 */
    /* JADX WARN: Type inference failed for: r5v77 */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v2, types: [org.telegram.messenger.MessageObject] */
    /* JADX WARN: Type inference failed for: r9v4 */
    protected void sendInternal(boolean z) {
        char c;
        MessageObject messageObject;
        long j;
        int i;
        char c2;
        MessageObject messageObject2;
        int i2;
        TLRPC.TL_forumTopic tL_forumTopic;
        SendMessagesHelper.SendMessageParams of;
        ArrayList arrayList;
        MessageObject messageObject3;
        long j2;
        int i3;
        ArrayList arrayList2;
        int i4 = 1;
        ?? r2 = 0;
        for (int i5 = 0; i5 < this.selectedDialogs.size(); i5++) {
            if (AlertsCreator.checkSlowMode(getContext(), this.currentAccount, this.selectedDialogs.keyAt(i5), this.frameLayout2.getTag() != null && this.commentTextView.length() > 0)) {
                return;
            }
        }
        CharSequence[] charSequenceArr = {this.commentTextView.getText()};
        ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true);
        TLRPC.TL_forumTopic tL_forumTopic2 = null;
        if (this.sendingMessageObjects != null) {
            ArrayList arrayList3 = new ArrayList();
            int i6 = 0;
            while (true) {
                if (i6 >= this.selectedDialogs.size()) {
                    arrayList = arrayList3;
                    break;
                }
                long keyAt = this.selectedDialogs.keyAt(i6);
                TLRPC.TL_forumTopic tL_forumTopic3 = (TLRPC.TL_forumTopic) this.selectedDialogTopics.get(this.selectedDialogs.get(keyAt));
                MessageObject messageObject4 = tL_forumTopic3 != null ? new MessageObject(this.currentAccount, tL_forumTopic3.topicStartMessage, r2, r2) : tL_forumTopic2;
                if (messageObject4 != 0) {
                    messageObject4.isTopicMainMessage = true;
                }
                if (this.frameLayout2.getTag() == null || this.commentTextView.length() <= 0) {
                    messageObject3 = messageObject4;
                    j2 = keyAt;
                    i3 = i6;
                    arrayList2 = arrayList3;
                } else {
                    SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(this.currentAccount);
                    CharSequence charSequence = charSequenceArr[r2];
                    messageObject3 = messageObject4;
                    j2 = keyAt;
                    i3 = i6;
                    arrayList2 = arrayList3;
                    sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of(charSequence == null ? tL_forumTopic2 : charSequence.toString(), keyAt, messageObject4, messageObject4, null, true, entities, null, null, z, 0, null, false));
                }
                int sendMessage = SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.sendingMessageObjects, j2, !this.showSendersName, false, z, 0, messageObject3);
                if (sendMessage != 0) {
                    arrayList = arrayList2;
                    arrayList.add(Long.valueOf(j2));
                } else {
                    arrayList = arrayList2;
                }
                if (this.selectedDialogs.size() == 1) {
                    tL_forumTopic2 = null;
                    AlertsCreator.showSendMediaAlert(sendMessage, this.parentFragment, null);
                    if (sendMessage != 0) {
                        break;
                    }
                } else {
                    tL_forumTopic2 = null;
                }
                i6 = i3 + 1;
                arrayList3 = arrayList;
                r2 = 0;
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                long longValue = ((Long) it.next()).longValue();
                TLRPC.Dialog dialog = (TLRPC.Dialog) this.selectedDialogs.get(longValue);
                this.selectedDialogs.remove(longValue);
                if (dialog != null) {
                    this.selectedDialogTopics.remove(dialog);
                }
            }
            if (!this.selectedDialogs.isEmpty()) {
                LongSparseArray longSparseArray = this.selectedDialogs;
                int size = this.sendingMessageObjects.size();
                if (this.selectedDialogs.size() == 1) {
                    tL_forumTopic2 = (TLRPC.TL_forumTopic) this.selectedDialogTopics.get(this.selectedDialogs.valueAt(0));
                }
                onSend(longSparseArray, size, tL_forumTopic2);
            }
        } else {
            SwitchView switchView = this.switchView;
            int i7 = switchView != null ? switchView.currentTab : 0;
            if (this.storyItem != null) {
                int i8 = 0;
                while (i8 < this.selectedDialogs.size()) {
                    long keyAt2 = this.selectedDialogs.keyAt(i8);
                    TLRPC.TL_forumTopic tL_forumTopic4 = (TLRPC.TL_forumTopic) this.selectedDialogTopics.get(this.selectedDialogs.get(keyAt2));
                    if (tL_forumTopic4 != null) {
                        c2 = 0;
                        messageObject2 = new MessageObject(this.currentAccount, tL_forumTopic4.topicStartMessage, false, false);
                    } else {
                        c2 = 0;
                        messageObject2 = tL_forumTopic2;
                    }
                    if (this.storyItem != null) {
                        i2 = i8;
                        tL_forumTopic = tL_forumTopic2;
                        if (this.frameLayout2.getTag() != null && this.commentTextView.length() > 0 && charSequenceArr[0] != null) {
                            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(charSequenceArr[0].toString(), keyAt2, null, messageObject2, null, true, null, null, null, z, 0, null, false));
                        }
                        of = SendMessagesHelper.SendMessageParams.of(null, keyAt2, messageObject2, messageObject2, null, true, null, null, null, z, 0, null, false);
                        of.sendingStory = this.storyItem;
                    } else if (this.frameLayout2.getTag() == null || this.commentTextView.length() <= 0) {
                        i2 = i8;
                        tL_forumTopic = tL_forumTopic2;
                        of = SendMessagesHelper.SendMessageParams.of(this.sendingText[i7], keyAt2, messageObject2, messageObject2, null, true, null, null, null, z, 0, null, false);
                    } else {
                        CharSequence charSequence2 = charSequenceArr[c2];
                        i2 = i8;
                        tL_forumTopic = tL_forumTopic2;
                        of = SendMessagesHelper.SendMessageParams.of(charSequence2 == null ? tL_forumTopic2 : charSequence2.toString(), keyAt2, messageObject2, messageObject2, null, true, entities, null, null, z, 0, null, false);
                    }
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
                    i8 = i2 + 1;
                    tL_forumTopic2 = tL_forumTopic;
                }
            } else if (this.sendingText[i7] != null) {
                int i9 = 0;
                while (i9 < this.selectedDialogs.size()) {
                    long keyAt3 = this.selectedDialogs.keyAt(i9);
                    TLRPC.TL_forumTopic tL_forumTopic5 = (TLRPC.TL_forumTopic) this.selectedDialogTopics.get(this.selectedDialogs.get(keyAt3));
                    if (tL_forumTopic5 != null) {
                        c = 0;
                        messageObject = new MessageObject(this.currentAccount, tL_forumTopic5.topicStartMessage, false, false);
                    } else {
                        c = 0;
                        messageObject = null;
                    }
                    if (this.frameLayout2.getTag() == null || this.commentTextView.length() <= 0) {
                        j = keyAt3;
                        i = i9;
                    } else {
                        SendMessagesHelper sendMessagesHelper2 = SendMessagesHelper.getInstance(this.currentAccount);
                        CharSequence charSequence3 = charSequenceArr[c];
                        j = keyAt3;
                        i = i9;
                        sendMessagesHelper2.sendMessage(SendMessagesHelper.SendMessageParams.of(charSequence3 == null ? null : charSequence3.toString(), keyAt3, messageObject, messageObject, null, true, entities, null, null, z, 0, null, false));
                    }
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(this.sendingText[i7], j, messageObject, messageObject, null, true, null, null, null, z, 0, null, false));
                    i4 = 1;
                    i9 = i + 1;
                }
            }
            LongSparseArray longSparseArray2 = this.selectedDialogs;
            onSend(longSparseArray2, i4, (TLRPC.TL_forumTopic) this.selectedDialogTopics.get(longSparseArray2.valueAt(0)));
        }
        ShareAlertDelegate shareAlertDelegate = this.delegate;
        if (shareAlertDelegate != null) {
            shareAlertDelegate.didShare();
        }
        dismiss();
    }

    public void setDelegate(ShareAlertDelegate shareAlertDelegate) {
        this.delegate = shareAlertDelegate;
    }

    public void setStoryToShare(TL_stories.StoryItem storyItem) {
        this.storyItem = storyItem;
    }

    public void updateSelectedCount(int i) {
        if (this.selectedDialogs.size() == 0) {
            this.selectedCountView.setPivotX(0.0f);
            this.selectedCountView.setPivotY(0.0f);
            showCommentTextView(false);
            return;
        }
        this.selectedCountView.invalidate();
        if (showCommentTextView(true) || i == 0) {
            this.selectedCountView.setPivotX(0.0f);
            this.selectedCountView.setPivotY(0.0f);
            return;
        }
        this.selectedCountView.setPivotX(AndroidUtilities.dp(21.0f));
        this.selectedCountView.setPivotY(AndroidUtilities.dp(12.0f));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.selectedCountView, (Property<View, Float>) View.SCALE_X, i == 1 ? 1.1f : 0.9f, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, (Property<View, Float>) View.SCALE_Y, i == 1 ? 1.1f : 0.9f, 1.0f));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setDuration(180L);
        animatorSet.start();
    }
}
