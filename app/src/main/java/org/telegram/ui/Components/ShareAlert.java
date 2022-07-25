package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.view.ViewPropertyAnimator;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_channels_exportMessageLink;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialog;
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_exportedMessageLink;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
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
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageStatisticActivity;
/* loaded from: classes3.dex */
public class ShareAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private AnimatorSet animatorSet;
    private float captionEditTextTopOffset;
    private float chatActivityEnterViewAnimateFromTop;
    private EditTextEmoji commentTextView;
    private int containerViewTop;
    private boolean copyLinkOnEnd;
    private float currentPanTranslationY;
    private boolean darkTheme;
    private ShareAlertDelegate delegate;
    private TLRPC$TL_exportedMessageLink exportedMessageLink;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private boolean fullyShown;
    private RecyclerListView gridView;
    private int hasPoll;
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
    private ArrayList<DialogsSearchAdapter.RecentSearchObject> recentSearchObjects;
    private LongSparseArray<DialogsSearchAdapter.RecentSearchObject> recentSearchObjectsById;
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
    private View selectedCountView;
    protected LongSparseArray<TLRPC$Dialog> selectedDialogs;
    private ActionBarPopupWindow sendPopupWindow;
    protected ArrayList<MessageObject> sendingMessageObjects;
    private String[] sendingText;
    private View[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private LinearLayout sharesCountLayout;
    private boolean showSendersName;
    private SwitchView switchView;
    private TextPaint textPaint;
    private ValueAnimator topBackgroundAnimator;
    private int topBeforeSwitch;
    private boolean updateSearchAdapter;
    private FrameLayout writeButtonContainer;

    /* loaded from: classes3.dex */
    public static class DialogSearchResult {
        public int date;
        public TLRPC$Dialog dialog = new TLRPC$TL_dialog();
        public CharSequence name;
        public TLObject object;
    }

    /* loaded from: classes3.dex */
    public interface ShareAlertDelegate {

        /* renamed from: org.telegram.ui.Components.ShareAlert$ShareAlertDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$didShare(ShareAlertDelegate shareAlertDelegate) {
            }
        }

        boolean didCopy();

        void didShare();
    }

    public static /* synthetic */ boolean lambda$new$6(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    protected void onSend(LongSparseArray<TLRPC$Dialog> longSparseArray, int i) {
    }

    /* loaded from: classes3.dex */
    public class SwitchView extends FrameLayout {
        private AnimatorSet animator;
        private int currentTab;
        private int lastColor;
        private SimpleTextView leftTab;
        private LinearGradient linearGradient;
        private Paint paint = new Paint(1);
        private RectF rect = new RectF();
        private SimpleTextView rightTab;
        private View searchBackground;
        private View slidingView;

        protected void onTabSwitch(int i) {
            throw null;
        }

        public SwitchView(ShareAlert shareAlert, Context context) {
            super(context);
            View view = new View(context);
            this.searchBackground = view;
            view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), shareAlert.getThemedColor(shareAlert.darkTheme ? "voipgroup_searchBackground" : "dialogSearchBackground")));
            addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 0.0f, 14.0f, 0.0f));
            View view2 = new View(context, shareAlert) { // from class: org.telegram.ui.Components.ShareAlert.SwitchView.1
                {
                    SwitchView.this = this;
                }

                @Override // android.view.View
                public void setTranslationX(float f) {
                    super.setTranslationX(f);
                    invalidate();
                }

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
            };
            this.slidingView = view2;
            addView(view2, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 0.0f, 14.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.leftTab = simpleTextView;
            simpleTextView.setTextColor(shareAlert.getThemedColor("voipgroup_nameText"));
            this.leftTab.setTextSize(13);
            this.leftTab.setLeftDrawable(R.drawable.msg_tabs_mic1);
            this.leftTab.setText(LocaleController.getString("VoipGroupInviteCanSpeak", R.string.VoipGroupInviteCanSpeak));
            this.leftTab.setGravity(17);
            addView(this.leftTab, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 0.0f, 0.0f, 0.0f));
            this.leftTab.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$SwitchView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ShareAlert.SwitchView.this.lambda$new$0(view3);
                }
            });
            SimpleTextView simpleTextView2 = new SimpleTextView(context);
            this.rightTab = simpleTextView2;
            simpleTextView2.setTextColor(shareAlert.getThemedColor("voipgroup_nameText"));
            this.rightTab.setTextSize(13);
            this.rightTab.setLeftDrawable(R.drawable.msg_tabs_mic2);
            this.rightTab.setText(LocaleController.getString("VoipGroupInviteListenOnly", R.string.VoipGroupInviteListenOnly));
            this.rightTab.setGravity(17);
            addView(this.rightTab, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 14.0f, 0.0f));
            this.rightTab.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$SwitchView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ShareAlert.SwitchView.this.lambda$new$1(view3);
                }
            });
        }

        public /* synthetic */ void lambda$new$0(View view) {
            switchToTab(0);
        }

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
            Animator[] animatorArr = new Animator[1];
            View view = this.slidingView;
            Property property = View.TRANSLATION_X;
            float[] fArr = new float[1];
            fArr[0] = this.currentTab == 0 ? 0.0f : view.getMeasuredWidth();
            animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
            animatorSet2.playTogether(animatorArr);
            this.animator.setDuration(180L);
            this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.SwitchView.2
                {
                    SwitchView.this = this;
                }

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
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.slidingView.getLayoutParams();
            layoutParams2.width = size;
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.slidingView.setTranslationX(this.currentTab == 0 ? 0.0f : layoutParams2.width);
            super.onMeasure(i, i2);
        }
    }

    /* loaded from: classes3.dex */
    public class SearchField extends FrameLayout {
        private ImageView clearSearchImageView;
        private CloseProgressDrawable2 progressDrawable;
        private View searchBackground;
        private EditTextBoldCursor searchEditText;
        private ImageView searchIconImageView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SearchField(Context context) {
            super(context);
            ShareAlert.this = r9;
            View view = new View(context);
            this.searchBackground = view;
            view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), r9.getThemedColor(r9.darkTheme ? "voipgroup_searchBackground" : "dialogSearchBackground")));
            addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            this.searchIconImageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.searchIconImageView.setImageResource(R.drawable.smiles_inputsearch);
            this.searchIconImageView.setColorFilter(new PorterDuffColorFilter(r9.getThemedColor(r9.darkTheme ? "voipgroup_mutedIcon" : "dialogSearchIcon"), PorterDuff.Mode.MULTIPLY));
            addView(this.searchIconImageView, LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.clearSearchImageView = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            ImageView imageView3 = this.clearSearchImageView;
            CloseProgressDrawable2 closeProgressDrawable2 = new CloseProgressDrawable2(r9) { // from class: org.telegram.ui.Components.ShareAlert.SearchField.1
                {
                    SearchField.this = this;
                }

                @Override // org.telegram.ui.Components.CloseProgressDrawable2
                protected int getCurrentColor() {
                    ShareAlert shareAlert = ShareAlert.this;
                    return shareAlert.getThemedColor(shareAlert.darkTheme ? "voipgroup_searchPlaceholder" : "dialogSearchIcon");
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
            this.searchEditText.setHintTextColor(r9.getThemedColor(r9.darkTheme ? "voipgroup_searchPlaceholder" : "dialogSearchHint"));
            String str = "voipgroup_searchText";
            this.searchEditText.setTextColor(r9.getThemedColor(r9.darkTheme ? str : "dialogSearchText"));
            this.searchEditText.setBackgroundDrawable(null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            this.searchEditText.setHint(LocaleController.getString("ShareSendTo", R.string.ShareSendTo));
            this.searchEditText.setCursorColor(r9.getThemedColor(!r9.darkTheme ? "featuredStickers_addedIcon" : str));
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener(new TextWatcher(r9) { // from class: org.telegram.ui.Components.ShareAlert.SearchField.2
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                {
                    SearchField.this = this;
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    boolean z = SearchField.this.searchEditText.length() > 0;
                    float f = 0.0f;
                    if (z != (SearchField.this.clearSearchImageView.getAlpha() != 0.0f)) {
                        ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                        float f2 = 1.0f;
                        if (z) {
                            f = 1.0f;
                        }
                        ViewPropertyAnimator scaleX = animate.alpha(f).setDuration(150L).scaleX(z ? 1.0f : 0.1f);
                        if (!z) {
                            f2 = 0.1f;
                        }
                        scaleX.scaleY(f2).start();
                    }
                    if (!TextUtils.isEmpty(SearchField.this.searchEditText.getText())) {
                        ShareAlert.this.checkCurrentList(false);
                    }
                    if (!ShareAlert.this.updateSearchAdapter) {
                        return;
                    }
                    String obj = SearchField.this.searchEditText.getText().toString();
                    if (obj.length() != 0) {
                        if (ShareAlert.this.searchEmptyView != null) {
                            ShareAlert.this.searchEmptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
                        }
                    } else if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.listAdapter) {
                        int currentTop = ShareAlert.this.getCurrentTop();
                        ShareAlert.this.searchEmptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
                        ShareAlert.this.searchEmptyView.showProgress(false, true);
                        ShareAlert.this.checkCurrentList(false);
                        ShareAlert.this.listAdapter.notifyDataSetChanged();
                        if (currentTop > 0) {
                            ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -currentTop);
                        }
                    }
                    if (ShareAlert.this.searchAdapter == null) {
                        return;
                    }
                    ShareAlert.this.searchAdapter.searchDialogs(obj);
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

        public /* synthetic */ void lambda$new$0(View view) {
            ShareAlert.this.updateSearchAdapter = true;
            this.searchEditText.setText("");
            AndroidUtilities.showKeyboard(this.searchEditText);
        }

        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (keyEvent != null) {
                if ((keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 84) && (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) {
                    return false;
                }
                AndroidUtilities.hideKeyboard(this.searchEditText);
                return false;
            }
            return false;
        }

        public void hideKeyboard() {
            AndroidUtilities.hideKeyboard(this.searchEditText);
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

    public ShareAlert(Context context, ArrayList<MessageObject> arrayList, String str, boolean z, String str2, boolean z2) {
        this(context, arrayList, str, z, str2, z2, null);
    }

    public ShareAlert(Context context, ArrayList<MessageObject> arrayList, String str, boolean z, String str2, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        this(context, null, arrayList, str, null, z, str2, null, z2, false, resourcesProvider);
    }

    public ShareAlert(Context context, ChatActivity chatActivity, ArrayList<MessageObject> arrayList, String str, String str2, boolean z, String str3, String str4, boolean z2, boolean z3) {
        this(context, chatActivity, arrayList, str, str2, z, str3, str4, z2, z3, null);
    }

    public ShareAlert(final Context context, ChatActivity chatActivity, ArrayList<MessageObject> arrayList, String str, String str2, boolean z, String str3, String str4, boolean z2, boolean z3, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        this.sendingText = new String[2];
        this.shadow = new View[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.selectedDialogs = new LongSparseArray<>();
        this.containerViewTop = -1;
        this.fullyShown = false;
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.linkToCopy = new String[2];
        this.recentSearchObjects = new ArrayList<>();
        new LongSparseArray();
        this.showSendersName = true;
        this.lastOffset = Integer.MAX_VALUE;
        this.resourcesProvider = resourcesProvider;
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        this.darkTheme = z3;
        this.parentFragment = chatActivity;
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        String str5 = "dialogBackground";
        String str6 = this.darkTheme ? "voipgroup_inviteMembersBackground" : str5;
        this.behindKeyboardColorKey = str6;
        int themedColor = getThemedColor(str6);
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
            {
                ShareAlert.this = this;
            }

            @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegate, org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
            public void onOpenAnimationEnd() {
                ShareAlert.this.fullyShown = true;
            }
        });
        ArrayList<MessageObject> arrayList2 = this.sendingMessageObjects;
        if (arrayList2 != null) {
            int size = arrayList2.size();
            for (int i = 0; i < size; i++) {
                MessageObject messageObject = this.sendingMessageObjects.get(i);
                if (messageObject.isPoll()) {
                    int i2 = messageObject.isPublicPoll() ? 2 : 1;
                    this.hasPoll = i2;
                    if (i2 == 2) {
                        break;
                    }
                }
            }
        }
        if (z) {
            this.loadingLink = true;
            TLRPC$TL_channels_exportMessageLink tLRPC$TL_channels_exportMessageLink = new TLRPC$TL_channels_exportMessageLink();
            tLRPC$TL_channels_exportMessageLink.id = arrayList.get(0).getId();
            tLRPC$TL_channels_exportMessageLink.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(arrayList.get(0).messageOwner.peer_id.channel_id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_exportMessageLink, new RequestDelegate() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda11
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ShareAlert.this.lambda$new$1(context, tLObject, tLRPC$TL_error);
                }
            });
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.Components.ShareAlert.2
            private int fromOffsetTop;
            private int fromScrollY;
            private boolean fullHeight;
            private boolean lightStatusBar;
            private int previousTopOffset;
            private int toOffsetTop;
            private int toScrollY;
            private int topOffset;
            private boolean ignoreLayout = false;
            private RectF rect1 = new RectF();
            AdjustPanLayoutHelper adjustPanLayoutHelper = new AdjustPanLayoutHelper(this) { // from class: org.telegram.ui.Components.ShareAlert.2.1
                {
                    AnonymousClass2.this = this;
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onTransitionStart(boolean z4, int i3) {
                    super.onTransitionStart(z4, i3);
                    if (ShareAlert.this.previousScrollOffsetY == ShareAlert.this.scrollOffsetY) {
                        AnonymousClass2.this.fromScrollY = -1;
                    } else {
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        anonymousClass2.fromScrollY = ShareAlert.this.previousScrollOffsetY;
                        AnonymousClass2 anonymousClass22 = AnonymousClass2.this;
                        anonymousClass22.toScrollY = ShareAlert.this.scrollOffsetY;
                        ShareAlert.this.panTranslationMoveLayout = true;
                        AnonymousClass2 anonymousClass23 = AnonymousClass2.this;
                        ShareAlert.this.scrollOffsetY = anonymousClass23.fromScrollY;
                    }
                    if (AnonymousClass2.this.topOffset != AnonymousClass2.this.previousTopOffset) {
                        AnonymousClass2.this.fromOffsetTop = 0;
                        AnonymousClass2.this.toOffsetTop = 0;
                        ShareAlert.this.panTranslationMoveLayout = true;
                        if (!z4) {
                            AnonymousClass2 anonymousClass24 = AnonymousClass2.this;
                            AnonymousClass2.access$3420(anonymousClass24, anonymousClass24.topOffset - AnonymousClass2.this.previousTopOffset);
                        } else {
                            AnonymousClass2 anonymousClass25 = AnonymousClass2.this;
                            AnonymousClass2.access$3412(anonymousClass25, anonymousClass25.topOffset - AnonymousClass2.this.previousTopOffset);
                        }
                        AnonymousClass2 anonymousClass26 = AnonymousClass2.this;
                        ShareAlert.this.scrollOffsetY = z4 ? anonymousClass26.fromScrollY : anonymousClass26.toScrollY;
                    } else {
                        AnonymousClass2.this.fromOffsetTop = -1;
                    }
                    ShareAlert.this.gridView.setTopGlowOffset((int) (ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY));
                    ShareAlert.this.frameLayout.setTranslationY(ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY);
                    ShareAlert.this.searchEmptyView.setTranslationY(ShareAlert.this.currentPanTranslationY + ShareAlert.this.scrollOffsetY);
                    invalidate();
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onTransitionEnd() {
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
                public void onPanTranslationUpdate(float f, float f2, boolean z4) {
                    super.onPanTranslationUpdate(f, f2, z4);
                    for (int i3 = 0; i3 < ((BottomSheet) ShareAlert.this).containerView.getChildCount(); i3++) {
                        if (((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.pickerBottomLayout && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.shadow[1] && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.sharesCountLayout && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.frameLayout2 && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.writeButtonContainer && ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3) != ShareAlert.this.selectedCountView) {
                            ((BottomSheet) ShareAlert.this).containerView.getChildAt(i3).setTranslationY(f);
                        }
                    }
                    ShareAlert.this.currentPanTranslationY = f;
                    if (AnonymousClass2.this.fromScrollY == -1) {
                        if (AnonymousClass2.this.fromOffsetTop != -1) {
                            AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                            float f3 = 1.0f - f2;
                            ShareAlert.this.scrollOffsetY = (int) ((anonymousClass2.fromOffsetTop * f3) + (AnonymousClass2.this.toOffsetTop * f2));
                            if (!z4) {
                                f3 = f2;
                            }
                            if (z4) {
                                ShareAlert.this.gridView.setTranslationY(ShareAlert.this.currentPanTranslationY - ((AnonymousClass2.this.fromOffsetTop - AnonymousClass2.this.toOffsetTop) * f2));
                            } else {
                                ShareAlert.this.gridView.setTranslationY(ShareAlert.this.currentPanTranslationY + ((AnonymousClass2.this.toOffsetTop - AnonymousClass2.this.fromOffsetTop) * f3));
                            }
                        }
                    } else {
                        if (!z4) {
                            f2 = 1.0f - f2;
                        }
                        AnonymousClass2 anonymousClass22 = AnonymousClass2.this;
                        float f4 = 1.0f - f2;
                        ShareAlert.this.scrollOffsetY = (int) ((anonymousClass22.fromScrollY * f4) + (AnonymousClass2.this.toScrollY * f2));
                        float f5 = ShareAlert.this.currentPanTranslationY + ((AnonymousClass2.this.fromScrollY - AnonymousClass2.this.toScrollY) * f4);
                        ShareAlert.this.gridView.setTranslationY(f5);
                        if (z4) {
                            ShareAlert.this.searchGridView.setTranslationY(f5);
                        } else {
                            ShareAlert.this.searchGridView.setTranslationY(f5 + ShareAlert.this.gridView.getPaddingTop());
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
                protected boolean heightAnimationEnabled() {
                    if (ShareAlert.this.isDismissed() || !ShareAlert.this.fullyShown) {
                        return false;
                    }
                    return !ShareAlert.this.commentTextView.isPopupVisible();
                }
            };

            {
                ShareAlert.this = this;
                boolean z4 = false;
                this.lightStatusBar = AndroidUtilities.computePerceivedBrightness(this.getThemedColor(this.darkTheme ? "voipgroup_inviteMembersBackground" : "dialogBackground")) > 0.721f ? true : z4;
            }

            static /* synthetic */ int access$3412(AnonymousClass2 anonymousClass2, int i3) {
                int i4 = anonymousClass2.toOffsetTop + i3;
                anonymousClass2.toOffsetTop = i4;
                return i4;
            }

            static /* synthetic */ int access$3420(AnonymousClass2 anonymousClass2, int i3) {
                int i4 = anonymousClass2.toOffsetTop - i3;
                anonymousClass2.toOffsetTop = i4;
                return i4;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
                this.adjustPanLayoutHelper.setResizableView(this);
                this.adjustPanLayoutHelper.onAttach();
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                this.adjustPanLayoutHelper.onDetach();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i3, int i4) {
                int size2;
                if (getLayoutParams().height > 0) {
                    size2 = getLayoutParams().height;
                } else {
                    size2 = View.MeasureSpec.getSize(i4);
                }
                ShareAlert.this.layoutManager.setNeedFixGap(getLayoutParams().height <= 0);
                ShareAlert.this.searchLayoutManager.setNeedFixGap(getLayoutParams().height <= 0);
                if (Build.VERSION.SDK_INT >= 21 && !((BottomSheet) ShareAlert.this).isFullscreen) {
                    this.ignoreLayout = true;
                    setPadding(((BottomSheet) ShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) ShareAlert.this).backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
                int paddingTop = size2 - getPaddingTop();
                int dp = AndroidUtilities.dp(103.0f) + AndroidUtilities.dp(48.0f) + (Math.max(2, (int) Math.ceil(Math.max(ShareAlert.this.searchAdapter.getItemCount(), ShareAlert.this.listAdapter.getItemCount() - 1) / 4.0f)) * AndroidUtilities.dp(103.0f)) + ((BottomSheet) ShareAlert.this).backgroundPaddingTop;
                int dp2 = (dp < paddingTop ? 0 : paddingTop - ((paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f);
                if (ShareAlert.this.gridView.getPaddingTop() != dp2) {
                    this.ignoreLayout = true;
                    ShareAlert.this.gridView.setPadding(0, dp2, 0, AndroidUtilities.dp(48.0f));
                    this.ignoreLayout = false;
                }
                if (((BottomSheet) ShareAlert.this).keyboardVisible && getLayoutParams().height <= 0 && ShareAlert.this.searchGridView.getPaddingTop() != dp2) {
                    this.ignoreLayout = true;
                    ShareAlert.this.searchGridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
                    this.ignoreLayout = false;
                }
                boolean z4 = dp >= size2;
                this.fullHeight = z4;
                this.topOffset = (z4 || !SharedConfig.smoothKeyboard) ? 0 : size2 - dp;
                this.ignoreLayout = true;
                ShareAlert.this.checkCurrentList(false);
                this.ignoreLayout = false;
                setMeasuredDimension(View.MeasureSpec.getSize(i3), size2);
                onMeasureInternal(i3, View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }

            private void onMeasureInternal(int i3, int i4) {
                int size2 = View.MeasureSpec.getSize(i3);
                int size3 = View.MeasureSpec.getSize(i4);
                int i5 = size2 - (((BottomSheet) ShareAlert.this).backgroundPaddingLeft * 2);
                int measureKeyboardHeight = SharedConfig.smoothKeyboard ? 0 : measureKeyboardHeight();
                if (!ShareAlert.this.commentTextView.isWaitingForKeyboardOpen() && measureKeyboardHeight <= AndroidUtilities.dp(20.0f) && !ShareAlert.this.commentTextView.isPopupShowing() && !ShareAlert.this.commentTextView.isAnimatePopupClosing()) {
                    this.ignoreLayout = true;
                    ShareAlert.this.commentTextView.hideEmojiView();
                    this.ignoreLayout = false;
                }
                this.ignoreLayout = true;
                if (measureKeyboardHeight > AndroidUtilities.dp(20.0f)) {
                    ShareAlert.this.commentTextView.hideEmojiView();
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(8);
                        if (ShareAlert.this.sharesCountLayout != null) {
                            ShareAlert.this.sharesCountLayout.setVisibility(8);
                        }
                    }
                } else {
                    if (!AndroidUtilities.isInMultiwindow) {
                        size3 -= (!SharedConfig.smoothKeyboard || !((BottomSheet) ShareAlert.this).keyboardVisible) ? ShareAlert.this.commentTextView.getEmojiPadding() : 0;
                        i4 = View.MeasureSpec.makeMeasureSpec(size3, 1073741824);
                    }
                    int i6 = ShareAlert.this.commentTextView.isPopupShowing() ? 8 : 0;
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(i6);
                        if (ShareAlert.this.sharesCountLayout != null) {
                            ShareAlert.this.sharesCountLayout.setVisibility(i6);
                        }
                    }
                }
                this.ignoreLayout = false;
                int childCount = getChildCount();
                for (int i7 = 0; i7 < childCount; i7++) {
                    View childAt = getChildAt(i7);
                    if (childAt != null && childAt.getVisibility() != 8) {
                        if (ShareAlert.this.commentTextView != null && ShareAlert.this.commentTextView.isPopupView(childAt)) {
                            if (AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) {
                                if (AndroidUtilities.isTablet()) {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (size3 - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                                } else {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec((size3 - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                                }
                            } else {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                            }
                        } else {
                            measureChildWithMargins(childAt, i3, 0, i4, 0);
                        }
                    }
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:20:0x0090  */
            /* JADX WARN: Removed duplicated region for block: B:27:0x00c5  */
            /* JADX WARN: Removed duplicated region for block: B:31:0x00d7  */
            /* JADX WARN: Removed duplicated region for block: B:33:0x00e0  */
            /* JADX WARN: Removed duplicated region for block: B:40:0x00ad  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onLayout(boolean z4, int i3, int i4, int i5, int i6) {
                int i7;
                int i8;
                int i9;
                int i10;
                int i11;
                int i12;
                int measuredHeight;
                int measuredHeight2;
                int childCount = getChildCount();
                int measureKeyboardHeight = measureKeyboardHeight();
                int emojiPadding = ((!SharedConfig.smoothKeyboard || !((BottomSheet) ShareAlert.this).keyboardVisible) && measureKeyboardHeight <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) ? ShareAlert.this.commentTextView.getEmojiPadding() : 0;
                setBottomClip(emojiPadding);
                for (int i13 = 0; i13 < childCount; i13++) {
                    View childAt = getChildAt(i13);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight3 = childAt.getMeasuredHeight();
                        int i14 = layoutParams.gravity;
                        if (i14 == -1) {
                            i14 = 51;
                        }
                        int i15 = i14 & 7;
                        int i16 = i14 & 112;
                        int i17 = i15 & 7;
                        if (i17 == 1) {
                            i7 = (((i5 - i3) - measuredWidth) / 2) + layoutParams.leftMargin;
                            i8 = layoutParams.rightMargin;
                        } else if (i17 == 5) {
                            i7 = (((i5 - i3) - measuredWidth) - layoutParams.rightMargin) - getPaddingRight();
                            i8 = ((BottomSheet) ShareAlert.this).backgroundPaddingLeft;
                        } else {
                            i9 = layoutParams.leftMargin + getPaddingLeft();
                            if (i16 == 16) {
                                if (i16 == 48) {
                                    i12 = layoutParams.topMargin + getPaddingTop() + this.topOffset;
                                } else if (i16 == 80) {
                                    i10 = ((i6 - emojiPadding) - i4) - measuredHeight3;
                                    i11 = layoutParams.bottomMargin;
                                } else {
                                    i12 = layoutParams.topMargin;
                                }
                                if (ShareAlert.this.commentTextView != null && ShareAlert.this.commentTextView.isPopupView(childAt)) {
                                    if (!AndroidUtilities.isTablet()) {
                                        measuredHeight = getMeasuredHeight();
                                        measuredHeight2 = childAt.getMeasuredHeight();
                                    } else {
                                        measuredHeight = getMeasuredHeight() + measureKeyboardHeight;
                                        measuredHeight2 = childAt.getMeasuredHeight();
                                    }
                                    i12 = measuredHeight - measuredHeight2;
                                }
                                childAt.layout(i9, i12, measuredWidth + i9, measuredHeight3 + i12);
                            } else {
                                i10 = ((((i6 - emojiPadding) - (this.topOffset + i4)) - measuredHeight3) / 2) + layoutParams.topMargin;
                                i11 = layoutParams.bottomMargin;
                            }
                            i12 = i10 - i11;
                            if (ShareAlert.this.commentTextView != null) {
                                if (!AndroidUtilities.isTablet()) {
                                }
                                i12 = measuredHeight - measuredHeight2;
                            }
                            childAt.layout(i9, i12, measuredWidth + i9, measuredHeight3 + i12);
                        }
                        i9 = i7 - i8;
                        if (i16 == 16) {
                        }
                        i12 = i10 - i11;
                        if (ShareAlert.this.commentTextView != null) {
                        }
                        childAt.layout(i9, i12, measuredWidth + i9, measuredHeight3 + i12);
                    }
                }
                notifyHeightChanged();
                ShareAlert.this.updateLayout();
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (!this.fullHeight) {
                    if (motionEvent.getAction() == 0 && motionEvent.getY() < this.topOffset - AndroidUtilities.dp(30.0f)) {
                        ShareAlert.this.dismiss();
                        return true;
                    }
                } else if (motionEvent.getAction() == 0 && ShareAlert.this.scrollOffsetY != 0 && motionEvent.getY() < ShareAlert.this.scrollOffsetY - AndroidUtilities.dp(30.0f)) {
                    ShareAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
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

            /* JADX WARN: Removed duplicated region for block: B:15:0x00ce  */
            /* JADX WARN: Removed duplicated region for block: B:22:0x0158  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x017d  */
            /* JADX WARN: Removed duplicated region for block: B:42:0x015b  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onDraw(Canvas canvas) {
                float f;
                int i3;
                canvas.save();
                canvas.translate(0.0f, ShareAlert.this.currentPanTranslationY);
                int dp = (ShareAlert.this.scrollOffsetY - ((BottomSheet) ShareAlert.this).backgroundPaddingTop) + AndroidUtilities.dp(6.0f) + this.topOffset;
                ShareAlert shareAlert = ShareAlert.this;
                int i4 = shareAlert.containerViewTop = ((shareAlert.scrollOffsetY - ((BottomSheet) ShareAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f)) + this.topOffset;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(60.0f) + ((BottomSheet) ShareAlert.this).backgroundPaddingTop;
                boolean z4 = false;
                if (!((BottomSheet) ShareAlert.this).isFullscreen && Build.VERSION.SDK_INT >= 21) {
                    int i5 = AndroidUtilities.statusBarHeight;
                    i4 += i5;
                    dp += i5;
                    measuredHeight -= i5;
                    if (this.fullHeight) {
                        int i6 = ((BottomSheet) ShareAlert.this).backgroundPaddingTop + i4;
                        int i7 = AndroidUtilities.statusBarHeight;
                        if (i6 < i7 * 2) {
                            int min = Math.min(i7, ((i7 * 2) - i4) - ((BottomSheet) ShareAlert.this).backgroundPaddingTop);
                            i4 -= min;
                            measuredHeight += min;
                            f = 1.0f - Math.min(1.0f, (min * 2) / AndroidUtilities.statusBarHeight);
                        } else {
                            f = 1.0f;
                        }
                        int i8 = ((BottomSheet) ShareAlert.this).backgroundPaddingTop + i4;
                        int i9 = AndroidUtilities.statusBarHeight;
                        if (i8 < i9) {
                            i3 = Math.min(i9, (i9 - i4) - ((BottomSheet) ShareAlert.this).backgroundPaddingTop);
                            ShareAlert.this.shadowDrawable.setBounds(0, i4, getMeasuredWidth(), measuredHeight);
                            ShareAlert.this.shadowDrawable.draw(canvas);
                            if (f != 1.0f) {
                                Paint paint = Theme.dialogs_onlineCirclePaint;
                                ShareAlert shareAlert2 = ShareAlert.this;
                                paint.setColor(shareAlert2.getThemedColor(shareAlert2.darkTheme ? "voipgroup_inviteMembersBackground" : "dialogBackground"));
                                this.rect1.set(((BottomSheet) ShareAlert.this).backgroundPaddingLeft, ((BottomSheet) ShareAlert.this).backgroundPaddingTop + i4, getMeasuredWidth() - ((BottomSheet) ShareAlert.this).backgroundPaddingLeft, ((BottomSheet) ShareAlert.this).backgroundPaddingTop + i4 + AndroidUtilities.dp(24.0f));
                                canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                            }
                            int dp2 = AndroidUtilities.dp(36.0f);
                            this.rect1.set((getMeasuredWidth() - dp2) / 2, dp, (getMeasuredWidth() + dp2) / 2, dp + AndroidUtilities.dp(4.0f));
                            Paint paint2 = Theme.dialogs_onlineCirclePaint;
                            ShareAlert shareAlert3 = ShareAlert.this;
                            paint2.setColor(shareAlert3.getThemedColor(shareAlert3.darkTheme ? "voipgroup_scrollUp" : "key_sheet_scrollUp"));
                            canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                            if (Build.VERSION.SDK_INT >= 23) {
                                int systemUiVisibility = getSystemUiVisibility();
                                boolean z5 = this.lightStatusBar && ((float) i3) > ((float) AndroidUtilities.statusBarHeight) * 0.5f;
                                if ((systemUiVisibility & 8192) > 0) {
                                    z4 = true;
                                }
                                if (z5 != z4) {
                                    setSystemUiVisibility(z5 ? systemUiVisibility | 8192 : systemUiVisibility & (-8193));
                                }
                            }
                            canvas.restore();
                            this.previousTopOffset = this.topOffset;
                        }
                        i3 = 0;
                        ShareAlert.this.shadowDrawable.setBounds(0, i4, getMeasuredWidth(), measuredHeight);
                        ShareAlert.this.shadowDrawable.draw(canvas);
                        if (f != 1.0f) {
                        }
                        int dp22 = AndroidUtilities.dp(36.0f);
                        this.rect1.set((getMeasuredWidth() - dp22) / 2, dp, (getMeasuredWidth() + dp22) / 2, dp + AndroidUtilities.dp(4.0f));
                        Paint paint22 = Theme.dialogs_onlineCirclePaint;
                        ShareAlert shareAlert32 = ShareAlert.this;
                        paint22.setColor(shareAlert32.getThemedColor(shareAlert32.darkTheme ? "voipgroup_scrollUp" : "key_sheet_scrollUp"));
                        canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                        if (Build.VERSION.SDK_INT >= 23) {
                        }
                        canvas.restore();
                        this.previousTopOffset = this.topOffset;
                    }
                }
                f = 1.0f;
                i3 = 0;
                ShareAlert.this.shadowDrawable.setBounds(0, i4, getMeasuredWidth(), measuredHeight);
                ShareAlert.this.shadowDrawable.draw(canvas);
                if (f != 1.0f) {
                }
                int dp222 = AndroidUtilities.dp(36.0f);
                this.rect1.set((getMeasuredWidth() - dp222) / 2, dp, (getMeasuredWidth() + dp222) / 2, dp + AndroidUtilities.dp(4.0f));
                Paint paint222 = Theme.dialogs_onlineCirclePaint;
                ShareAlert shareAlert322 = ShareAlert.this;
                paint222.setColor(shareAlert322.getThemedColor(shareAlert322.darkTheme ? "voipgroup_scrollUp" : "key_sheet_scrollUp"));
                canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                if (Build.VERSION.SDK_INT >= 23) {
                }
                canvas.restore();
                this.previousTopOffset = this.topOffset;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0.0f, getPaddingTop() + ShareAlert.this.currentPanTranslationY, getMeasuredWidth(), getMeasuredHeight() + ShareAlert.this.currentPanTranslationY + AndroidUtilities.dp(50.0f));
                super.dispatchDraw(canvas);
                canvas.restore();
            }
        };
        this.containerView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setWillNotDraw(false);
        this.containerView.setClipChildren(false);
        ViewGroup viewGroup = this.containerView;
        int i3 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i3, 0, i3, 0);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor(this.darkTheme ? "voipgroup_inviteMembersBackground" : str5));
        if (this.darkTheme && this.linkToCopy[1] != null) {
            SwitchView switchView = new SwitchView(context) { // from class: org.telegram.ui.Components.ShareAlert.3
                {
                    ShareAlert.this = this;
                }

                @Override // org.telegram.ui.Components.ShareAlert.SwitchView
                protected void onTabSwitch(int i4) {
                    if (ShareAlert.this.pickerBottomLayout == null) {
                        return;
                    }
                    if (i4 == 0) {
                        ShareAlert.this.pickerBottomLayout.setText(LocaleController.getString("VoipGroupCopySpeakerLink", R.string.VoipGroupCopySpeakerLink).toUpperCase());
                    } else {
                        ShareAlert.this.pickerBottomLayout.setText(LocaleController.getString("VoipGroupCopyListenLink", R.string.VoipGroupCopyListenLink).toUpperCase());
                    }
                }
            };
            this.switchView = switchView;
            this.frameLayout.addView(switchView, LayoutHelper.createFrame(-1, 36.0f, 51, 0.0f, 11.0f, 0.0f, 0.0f));
        }
        SearchField searchField = new SearchField(context);
        this.searchView = searchField;
        this.frameLayout.addView(searchField, LayoutHelper.createFrame(-1, 58, 83));
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.4
            {
                ShareAlert.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) (AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
            }
        };
        this.gridView = recyclerListView;
        recyclerListView.setSelectorDrawableColor(0);
        this.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.gridView.setClipToPadding(false);
        RecyclerListView recyclerListView2 = this.gridView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        this.layoutManager = gridLayoutManager;
        recyclerListView2.setLayoutManager(gridLayoutManager);
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ShareAlert.5
            {
                ShareAlert.this = this;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i4) {
                if (i4 == 0) {
                    return ShareAlert.this.layoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.gridView.setHorizontalScrollBarEnabled(false);
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.addItemDecoration(new RecyclerView.ItemDecoration(this) { // from class: org.telegram.ui.Components.ShareAlert.6
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerView.getChildViewHolder(view);
                if (holder != null) {
                    int adapterPosition = holder.getAdapterPosition() % 4;
                    int i4 = 0;
                    rect.left = adapterPosition == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    if (adapterPosition != 3) {
                        i4 = AndroidUtilities.dp(4.0f);
                    }
                    rect.right = i4;
                    return;
                }
                rect.left = AndroidUtilities.dp(4.0f);
                rect.right = AndroidUtilities.dp(4.0f);
            }
        });
        this.containerView.addView(this.gridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView3 = this.gridView;
        ShareDialogsAdapter shareDialogsAdapter = new ShareDialogsAdapter(context);
        this.listAdapter = shareDialogsAdapter;
        recyclerListView3.setAdapter(shareDialogsAdapter);
        String str7 = "dialogScrollGlow";
        this.gridView.setGlowColor(getThemedColor(this.darkTheme ? "voipgroup_inviteMembersBackground" : str7));
        this.gridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda14
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i4) {
                ShareAlert.this.lambda$new$2(view, i4);
            }
        });
        this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ShareAlert.7
            {
                ShareAlert.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i4, int i5) {
                if (i5 != 0) {
                    ShareAlert.this.updateLayout();
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                }
            }
        });
        RecyclerListView recyclerListView4 = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.8
            {
                ShareAlert.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) (AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 58.0f : 111.0f) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
            }
        };
        this.searchGridView = recyclerListView4;
        recyclerListView4.setSelectorDrawableColor(0);
        this.searchGridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.searchGridView.setClipToPadding(false);
        RecyclerListView recyclerListView5 = this.searchGridView;
        FillLastGridLayoutManager fillLastGridLayoutManager = new FillLastGridLayoutManager(getContext(), 4, 0, this.searchGridView);
        this.searchLayoutManager = fillLastGridLayoutManager;
        recyclerListView5.setLayoutManager(fillLastGridLayoutManager);
        this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ShareAlert.9
            {
                ShareAlert.this = this;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i4) {
                return ShareAlert.this.searchAdapter.getSpanSize(4, i4);
            }
        });
        this.searchGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i4) {
                ShareAlert.this.lambda$new$3(view, i4);
            }
        });
        this.searchGridView.setHasFixedSize(true);
        this.searchGridView.setItemAnimator(null);
        this.searchGridView.setHorizontalScrollBarEnabled(false);
        this.searchGridView.setVerticalScrollBarEnabled(false);
        this.searchGridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ShareAlert.10
            {
                ShareAlert.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i4, int i5) {
                if (i5 != 0) {
                    ShareAlert.this.updateLayout();
                    ShareAlert shareAlert = ShareAlert.this;
                    shareAlert.previousScrollOffsetY = shareAlert.scrollOffsetY;
                }
            }
        });
        this.searchGridView.addItemDecoration(new RecyclerView.ItemDecoration(this) { // from class: org.telegram.ui.Components.ShareAlert.11
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                RecyclerListView.Holder holder = (RecyclerListView.Holder) recyclerView.getChildViewHolder(view);
                if (holder != null) {
                    int adapterPosition = holder.getAdapterPosition() % 4;
                    int i4 = 0;
                    rect.left = adapterPosition == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    if (adapterPosition != 3) {
                        i4 = AndroidUtilities.dp(4.0f);
                    }
                    rect.right = i4;
                    return;
                }
                rect.left = AndroidUtilities.dp(4.0f);
                rect.right = AndroidUtilities.dp(4.0f);
            }
        });
        this.searchGridView.setAdapter(this.searchAdapter);
        this.searchGridView.setGlowColor(getThemedColor(this.darkTheme ? "voipgroup_inviteMembersBackground" : str7));
        this.recyclerItemsEnterAnimator = new RecyclerItemsEnterAnimator(this.searchGridView, true);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context, resourcesProvider);
        flickerLoadingView.setViewType(12);
        if (this.darkTheme) {
            flickerLoadingView.setColors("voipgroup_inviteMembersBackground", "voipgroup_searchBackground", null);
        }
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, flickerLoadingView, 1, resourcesProvider);
        this.searchEmptyView = stickerEmptyView;
        stickerEmptyView.addView(flickerLoadingView, 0);
        this.searchEmptyView.setAnimateLayoutChange(true);
        this.searchEmptyView.showProgress(false, false);
        if (this.darkTheme) {
            this.searchEmptyView.title.setTextColor(getThemedColor("voipgroup_nameText"));
        }
        this.searchEmptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
        this.searchGridView.setEmptyView(this.searchEmptyView);
        this.searchGridView.setHideIfEmpty(false);
        this.searchGridView.setAnimateEmptyView(true, 0);
        this.containerView.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
        this.containerView.addView(this.searchGridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp((!this.darkTheme || this.linkToCopy[1] == null) ? 58.0f : 111.0f);
        this.shadow[0] = new View(context);
        this.shadow[0].setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.shadow[0].setAlpha(0.0f);
        this.shadow[0].setTag(1);
        this.containerView.addView(this.shadow[0], layoutParams);
        this.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, (!this.darkTheme || this.linkToCopy[1] == null) ? 58 : 111, 51));
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
        layoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
        this.shadow[1] = new View(context);
        this.shadow[1].setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.containerView.addView(this.shadow[1], layoutParams2);
        if (this.isChannel || this.linkToCopy[0] != null) {
            TextView textView = new TextView(context);
            this.pickerBottomLayout = textView;
            textView.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(getThemedColor(this.darkTheme ? "voipgroup_inviteMembersBackground" : str5), getThemedColor(this.darkTheme ? "voipgroup_listSelector" : "listSelectorSDK21")));
            String str8 = "voipgroup_listeningText";
            this.pickerBottomLayout.setTextColor(getThemedColor(this.darkTheme ? str8 : "dialogTextBlue2"));
            this.pickerBottomLayout.setTextSize(1, 14.0f);
            this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.pickerBottomLayout.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.pickerBottomLayout.setGravity(17);
            if (this.darkTheme && this.linkToCopy[1] != null) {
                this.pickerBottomLayout.setText(LocaleController.getString("VoipGroupCopySpeakerLink", R.string.VoipGroupCopySpeakerLink).toUpperCase());
            } else {
                this.pickerBottomLayout.setText(LocaleController.getString("CopyLink", R.string.CopyLink).toUpperCase());
            }
            this.pickerBottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ShareAlert.this.lambda$new$4(view);
                }
            });
            this.containerView.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && ChatObject.hasAdminRights(chatActivity2.getCurrentChat()) && this.sendingMessageObjects.size() > 0 && this.sendingMessageObjects.get(0).messageOwner.forwards > 0) {
                final MessageObject messageObject2 = this.sendingMessageObjects.get(0);
                if (!messageObject2.isForwarded()) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    this.sharesCountLayout = linearLayout;
                    linearLayout.setOrientation(0);
                    this.sharesCountLayout.setGravity(16);
                    this.sharesCountLayout.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(this.darkTheme ? "voipgroup_listSelector" : "listSelectorSDK21"), 2));
                    this.containerView.addView(this.sharesCountLayout, LayoutHelper.createFrame(-2, 48.0f, 85, 6.0f, 0.0f, -6.0f, 0.0f));
                    this.sharesCountLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ShareAlert.this.lambda$new$5(messageObject2, view);
                        }
                    });
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.share_arrow);
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(this.darkTheme ? str8 : "dialogTextBlue2"), PorterDuff.Mode.MULTIPLY));
                    this.sharesCountLayout.addView(imageView, LayoutHelper.createLinear(-2, -1, 16, 20, 0, 0, 0));
                    TextView textView2 = new TextView(context);
                    textView2.setText(String.format("%d", Integer.valueOf(messageObject2.messageOwner.forwards)));
                    textView2.setTextSize(1, 14.0f);
                    textView2.setTextColor(getThemedColor(!this.darkTheme ? "dialogTextBlue2" : str8));
                    textView2.setGravity(16);
                    textView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.sharesCountLayout.addView(textView2, LayoutHelper.createLinear(-2, -1, 16, 8, 0, 20, 0));
                }
            }
        } else {
            this.shadow[1].setAlpha(0.0f);
        }
        AnonymousClass12 anonymousClass12 = new AnonymousClass12(context);
        this.frameLayout2 = anonymousClass12;
        anonymousClass12.setWillNotDraw(false);
        this.frameLayout2.setAlpha(0.0f);
        this.frameLayout2.setVisibility(4);
        this.containerView.addView(this.frameLayout2, LayoutHelper.createFrame(-1, -2, 83));
        this.frameLayout2.setOnTouchListener(ShareAlert$$ExternalSyntheticLambda8.INSTANCE);
        AnonymousClass13 anonymousClass13 = new AnonymousClass13(context, sizeNotifierFrameLayout, null, 1, true, resourcesProvider);
        this.commentTextView = anonymousClass13;
        if (this.darkTheme) {
            anonymousClass13.getEditText().setTextColor(getThemedColor("voipgroup_nameText"));
            this.commentTextView.getEditText().setCursorColor(getThemedColor("voipgroup_nameText"));
        }
        this.commentTextView.setBackgroundColor(themedColor);
        this.commentTextView.setHint(LocaleController.getString("ShareComment", R.string.ShareComment));
        this.commentTextView.onResume();
        this.commentTextView.setPadding(0, 0, AndroidUtilities.dp(84.0f), 0);
        this.frameLayout2.addView(this.commentTextView, LayoutHelper.createFrame(-1, -2, 51));
        this.frameLayout2.setClipChildren(false);
        this.frameLayout2.setClipToPadding(false);
        this.commentTextView.setClipChildren(false);
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Components.ShareAlert.14
            {
                ShareAlert.this = this;
            }

            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setText(LocaleController.formatPluralString("AccDescrShareInChats", ShareAlert.this.selectedDialogs.size(), new Object[0]));
                accessibilityNodeInfo.setClassName(Button.class.getName());
                accessibilityNodeInfo.setLongClickable(true);
                accessibilityNodeInfo.setClickable(true);
            }
        };
        this.writeButtonContainer = frameLayout2;
        frameLayout2.setFocusable(true);
        this.writeButtonContainer.setFocusableInTouchMode(true);
        this.writeButtonContainer.setVisibility(4);
        this.writeButtonContainer.setScaleX(0.2f);
        this.writeButtonContainer.setScaleY(0.2f);
        this.writeButtonContainer.setAlpha(0.0f);
        this.containerView.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
        final ImageView imageView2 = new ImageView(context);
        float f = 56.0f;
        int dp = AndroidUtilities.dp(56.0f);
        int themedColor2 = getThemedColor("dialogFloatingButton");
        int i4 = Build.VERSION.SDK_INT;
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, themedColor2, getThemedColor(i4 >= 21 ? "dialogFloatingButtonPressed" : "dialogFloatingButton"));
        if (i4 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        imageView2.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        imageView2.setImageResource(R.drawable.attach_send);
        imageView2.setImportantForAccessibility(2);
        imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogFloatingIcon"), PorterDuff.Mode.MULTIPLY));
        imageView2.setScaleType(ImageView.ScaleType.CENTER);
        if (i4 >= 21) {
            imageView2.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.Components.ShareAlert.15
                @Override // android.view.ViewOutlineProvider
                @SuppressLint({"NewApi"})
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        this.writeButtonContainer.addView(imageView2, LayoutHelper.createFrame(i4 >= 21 ? 56 : 60, i4 < 21 ? 60.0f : f, 51, i4 >= 21 ? 2.0f : 0.0f, 0.0f, 0.0f, 0.0f));
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ShareAlert.this.lambda$new$7(view);
            }
        });
        imageView2.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda7
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$new$8;
                lambda$new$8 = ShareAlert.this.lambda$new$8(imageView2, view);
                return lambda$new$8;
            }
        });
        this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        View view = new View(context) { // from class: org.telegram.ui.Components.ShareAlert.16
            {
                ShareAlert.this = this;
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                String format = String.format("%d", Integer.valueOf(Math.max(1, ShareAlert.this.selectedDialogs.size())));
                int ceil = (int) Math.ceil(ShareAlert.this.textPaint.measureText(format));
                int max = Math.max(AndroidUtilities.dp(16.0f) + ceil, AndroidUtilities.dp(24.0f));
                int measuredWidth = getMeasuredWidth() / 2;
                int measuredHeight = getMeasuredHeight() / 2;
                ShareAlert.this.textPaint.setColor(ShareAlert.this.getThemedColor("dialogRoundCheckBoxCheck"));
                Paint paint = ShareAlert.this.paint;
                ShareAlert shareAlert = ShareAlert.this;
                paint.setColor(shareAlert.getThemedColor(shareAlert.darkTheme ? "voipgroup_inviteMembersBackground" : "dialogBackground"));
                int i5 = max / 2;
                int i6 = measuredWidth - i5;
                int i7 = i5 + measuredWidth;
                ShareAlert.this.rect.set(i6, 0.0f, i7, getMeasuredHeight());
                canvas.drawRoundRect(ShareAlert.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), ShareAlert.this.paint);
                ShareAlert.this.paint.setColor(ShareAlert.this.getThemedColor("dialogRoundCheckBox"));
                ShareAlert.this.rect.set(i6 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), i7 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
                canvas.drawRoundRect(ShareAlert.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), ShareAlert.this.paint);
                canvas.drawText(format, measuredWidth - (ceil / 2), AndroidUtilities.dp(16.2f), ShareAlert.this.textPaint);
            }
        };
        this.selectedCountView = view;
        view.setAlpha(0.0f);
        this.selectedCountView.setScaleX(0.2f);
        this.selectedCountView.setScaleY(0.2f);
        this.containerView.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -8.0f, 9.0f));
        updateSelectedCount(0);
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        if (this.listAdapter.dialogs.isEmpty()) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
        }
        DialogsSearchAdapter.loadRecentSearch(this.currentAccount, 0, new DialogsSearchAdapter.OnRecentSearchLoaded() { // from class: org.telegram.ui.Components.ShareAlert.17
            {
                ShareAlert.this = this;
            }

            @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.OnRecentSearchLoaded
            public void setRecentSearch(ArrayList<DialogsSearchAdapter.RecentSearchObject> arrayList3, LongSparseArray<DialogsSearchAdapter.RecentSearchObject> longSparseArray) {
                ShareAlert.this.recentSearchObjects = arrayList3;
                ShareAlert.this.recentSearchObjectsById = longSparseArray;
                for (int i5 = 0; i5 < ShareAlert.this.recentSearchObjects.size(); i5++) {
                    DialogsSearchAdapter.RecentSearchObject recentSearchObject = (DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i5);
                    TLObject tLObject = recentSearchObject.object;
                    if (tLObject instanceof TLRPC$User) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putUser((TLRPC$User) recentSearchObject.object, true);
                    } else if (tLObject instanceof TLRPC$Chat) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putChat((TLRPC$Chat) recentSearchObject.object, true);
                    } else if (tLObject instanceof TLRPC$EncryptedChat) {
                        MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putEncryptedChat((TLRPC$EncryptedChat) recentSearchObject.object, true);
                    }
                }
                ShareAlert.this.searchAdapter.notifyDataSetChanged();
            }
        });
        MediaDataController.getInstance(this.currentAccount).loadHints(true);
        AndroidUtilities.updateViewVisibilityAnimated(this.gridView, true, 1.0f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, false, 1.0f, false);
    }

    public /* synthetic */ void lambda$new$1(final Context context, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ShareAlert.this.lambda$new$0(tLObject, context);
            }
        });
    }

    public /* synthetic */ void lambda$new$0(TLObject tLObject, Context context) {
        if (tLObject != null) {
            this.exportedMessageLink = (TLRPC$TL_exportedMessageLink) tLObject;
            if (this.copyLinkOnEnd) {
                copyLink(context);
            }
        }
        this.loadingLink = false;
    }

    public /* synthetic */ void lambda$new$2(View view, int i) {
        TLRPC$Dialog item;
        if (i >= 0 && (item = this.listAdapter.getItem(i)) != null) {
            selectDialog((ShareDialogCell) view, item);
        }
    }

    public /* synthetic */ void lambda$new$3(View view, int i) {
        TLRPC$Dialog item;
        if (i >= 0 && (item = this.searchAdapter.getItem(i)) != null) {
            selectDialog((ShareDialogCell) view, item);
        }
    }

    public /* synthetic */ void lambda$new$4(View view) {
        if (this.selectedDialogs.size() == 0) {
            if (!this.isChannel && this.linkToCopy[0] == null) {
                return;
            }
            dismiss();
            if (this.linkToCopy[0] == null && this.loadingLink) {
                this.copyLinkOnEnd = true;
                Toast.makeText(getContext(), LocaleController.getString("Loading", R.string.Loading), 0).show();
                return;
            }
            copyLink(getContext());
        }
    }

    public /* synthetic */ void lambda$new$5(MessageObject messageObject, View view) {
        this.parentFragment.presentFragment(new MessageStatisticActivity(messageObject));
    }

    /* renamed from: org.telegram.ui.Components.ShareAlert$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context) {
            super(context);
            ShareAlert.this = r1;
            new Paint();
        }

        @Override // android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            if (i != 0) {
                ShareAlert.this.shadow[1].setTranslationY(0.0f);
            }
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            invalidate();
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
                ShareAlert.this.topBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$12$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ShareAlert.AnonymousClass12.this.lambda$onDraw$0(valueAnimator);
                    }
                });
                ShareAlert.this.topBackgroundAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ShareAlert.this.topBackgroundAnimator.setDuration(200L);
                ShareAlert.this.topBackgroundAnimator.start();
                ShareAlert.this.chatActivityEnterViewAnimateFromTop = 0.0f;
            }
            ShareAlert.this.shadow[1].setTranslationY((-(ShareAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(48.0f))) + ShareAlert.this.captionEditTextTopOffset + ShareAlert.this.currentPanTranslationY + ((ShareAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(48.0f)) * (1.0f - getAlpha())));
        }

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
    }

    /* renamed from: org.telegram.ui.Components.ShareAlert$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends EditTextEmoji {
        private ValueAnimator messageEditTextAnimator;
        private int messageEditTextPredrawHeigth;
        private int messageEditTextPredrawScrollY;
        private boolean shouldAnimateEditTextWithBounds;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout, BaseFragment baseFragment, int i, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, sizeNotifierFrameLayout, baseFragment, i, z, resourcesProvider);
            ShareAlert.this = r8;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.shouldAnimateEditTextWithBounds) {
                final EditTextCaption editText = ShareAlert.this.commentTextView.getEditText();
                editText.setOffsetY(editText.getOffsetY() - ((this.messageEditTextPredrawHeigth - editText.getMeasuredHeight()) + (this.messageEditTextPredrawScrollY - editText.getScrollY())));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(editText.getOffsetY(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ShareAlert$13$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ShareAlert.AnonymousClass13.lambda$dispatchDraw$0(EditTextCaption.this, valueAnimator);
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

        public static /* synthetic */ void lambda$dispatchDraw$0(EditTextCaption editTextCaption, ValueAnimator valueAnimator) {
            editTextCaption.setOffsetY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void onLineCountChanged(int i, int i2) {
            if (!TextUtils.isEmpty(getEditText().getText())) {
                this.shouldAnimateEditTextWithBounds = true;
                this.messageEditTextPredrawHeigth = getEditText().getMeasuredHeight();
                this.messageEditTextPredrawScrollY = getEditText().getScrollY();
                invalidate();
            } else {
                getEditText().animate().cancel();
                getEditText().setOffsetY(0.0f);
                this.shouldAnimateEditTextWithBounds = false;
            }
            ShareAlert shareAlert = ShareAlert.this;
            shareAlert.chatActivityEnterViewAnimateFromTop = shareAlert.frameLayout2.getTop() + ShareAlert.this.captionEditTextTopOffset;
            ShareAlert.this.frameLayout2.invalidate();
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        public void showPopup(int i) {
            super.showPopup(i);
            if (ShareAlert.this.darkTheme) {
                ((BottomSheet) ShareAlert.this).navBarColorKey = null;
                AndroidUtilities.setNavigationBarColor(ShareAlert.this.getWindow(), ShareAlert.this.getThemedColor("windowBackgroundGray"), true, new AndroidUtilities.IntColorCallback() { // from class: org.telegram.ui.Components.ShareAlert$13$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.AndroidUtilities.IntColorCallback
                    public final void run(int i2) {
                        ShareAlert.AnonymousClass13.this.lambda$showPopup$1(i2);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$showPopup$1(int i) {
            ShareAlert shareAlert = ShareAlert.this;
            shareAlert.setOverlayNavBarColor(((BottomSheet) shareAlert).navBarColor = i);
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        public void hidePopup(boolean z) {
            super.hidePopup(z);
            if (ShareAlert.this.darkTheme) {
                ((BottomSheet) ShareAlert.this).navBarColorKey = null;
                AndroidUtilities.setNavigationBarColor(ShareAlert.this.getWindow(), ShareAlert.this.getThemedColor("voipgroup_inviteMembersBackground"), true, new AndroidUtilities.IntColorCallback() { // from class: org.telegram.ui.Components.ShareAlert$13$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.AndroidUtilities.IntColorCallback
                    public final void run(int i) {
                        ShareAlert.AnonymousClass13.this.lambda$hidePopup$2(i);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$hidePopup$2(int i) {
            ShareAlert shareAlert = ShareAlert.this;
            shareAlert.setOverlayNavBarColor(((BottomSheet) shareAlert).navBarColor = i);
        }
    }

    public /* synthetic */ void lambda$new$7(View view) {
        sendInternal(true);
    }

    public /* synthetic */ boolean lambda$new$8(ImageView imageView, View view) {
        return onSendLongClick(imageView);
    }

    public void selectDialog(ShareDialogCell shareDialogCell, TLRPC$Dialog tLRPC$Dialog) {
        DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapterRecycler;
        if (DialogObject.isChatDialog(tLRPC$Dialog.id)) {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog.id));
            if (ChatObject.isChannel(chat) && !chat.megagroup && (!ChatObject.isCanWriteToChannel(-tLRPC$Dialog.id, this.currentAccount) || this.hasPoll == 2)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                builder.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
                if (this.hasPoll == 2) {
                    if (this.isChannel) {
                        builder.setMessage(LocaleController.getString("PublicPollCantForward", R.string.PublicPollCantForward));
                    } else if (ChatObject.isActionBannedByDefault(chat, 10)) {
                        builder.setMessage(LocaleController.getString("ErrorSendRestrictedPollsAll", R.string.ErrorSendRestrictedPollsAll));
                    } else {
                        builder.setMessage(LocaleController.getString("ErrorSendRestrictedPolls", R.string.ErrorSendRestrictedPolls));
                    }
                } else {
                    builder.setMessage(LocaleController.getString("ChannelCantSendMessage", R.string.ChannelCantSendMessage));
                }
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                builder.show();
                return;
            }
        } else if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id) && this.hasPoll != 0) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this.parentActivity);
            builder2.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
            if (this.hasPoll != 0) {
                builder2.setMessage(LocaleController.getString("PollCantForwardSecretChat", R.string.PollCantForwardSecretChat));
            } else {
                builder2.setMessage(LocaleController.getString("InvoiceCantForwardSecretChat", R.string.InvoiceCantForwardSecretChat));
            }
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
            builder2.show();
            return;
        }
        if (this.selectedDialogs.indexOfKey(tLRPC$Dialog.id) >= 0) {
            this.selectedDialogs.remove(tLRPC$Dialog.id);
            if (shareDialogCell != null) {
                shareDialogCell.setChecked(false, true);
            }
            updateSelectedCount(1);
        } else {
            this.selectedDialogs.put(tLRPC$Dialog.id, tLRPC$Dialog);
            if (shareDialogCell != null) {
                shareDialogCell.setChecked(true, true);
            }
            updateSelectedCount(2);
            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
            if (this.searchIsVisible) {
                TLRPC$Dialog tLRPC$Dialog2 = (TLRPC$Dialog) this.listAdapter.dialogsMap.get(tLRPC$Dialog.id);
                if (tLRPC$Dialog2 != null) {
                    if (tLRPC$Dialog2.id != j) {
                        this.listAdapter.dialogs.remove(tLRPC$Dialog2);
                        this.listAdapter.dialogs.add(!this.listAdapter.dialogs.isEmpty(), tLRPC$Dialog2);
                    }
                } else {
                    this.listAdapter.dialogsMap.put(tLRPC$Dialog.id, tLRPC$Dialog);
                    this.listAdapter.dialogs.add(!this.listAdapter.dialogs.isEmpty(), tLRPC$Dialog);
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

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public int getContainerViewHeight() {
        return this.containerView.getMeasuredHeight() - this.containerViewTop;
    }

    private boolean onSendLongClick(View view) {
        int measuredHeight;
        ChatActivity chatActivity;
        if (this.parentActivity == null) {
            return false;
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        String str = "voipgroup_listSelector";
        if (this.sendingMessageObjects != null) {
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarPopupWindowLayout.setBackgroundColor(getThemedColor("voipgroup_inviteMembersBackground"));
            }
            actionBarPopupWindowLayout.setAnimationEnabled(false);
            actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ShareAlert.18
                private android.graphics.Rect popupRect = new android.graphics.Rect();

                {
                    ShareAlert.this = this;
                }

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
            actionBarPopupWindowLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda12
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
                public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                    ShareAlert.this.lambda$onSendLongClick$9(keyEvent);
                }
            });
            actionBarPopupWindowLayout.setShownFromBottom(false);
            final ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, true, false, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarMenuSubItem.setTextColor(getThemedColor("voipgroup_nameText"));
            }
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("ShowSendersName", R.string.ShowSendersName), 0);
            this.showSendersName = true;
            actionBarMenuSubItem.setChecked(true);
            final ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), true, false, true, this.resourcesProvider);
            if (this.darkTheme) {
                actionBarMenuSubItem2.setTextColor(getThemedColor("voipgroup_nameText"));
            }
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
            actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("HideSendersName", R.string.HideSendersName), 0);
            actionBarMenuSubItem2.setChecked(!this.showSendersName);
            actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.this.lambda$onSendLongClick$10(actionBarMenuSubItem, actionBarMenuSubItem2, view2);
                }
            });
            actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ShareAlert.this.lambda$onSendLongClick$11(actionBarMenuSubItem, actionBarMenuSubItem2, view2);
                }
            });
            actionBarPopupWindowLayout.setupRadialSelectors(getThemedColor(this.darkTheme ? str : "dialogButtonSelector"));
            linearLayout.addView(actionBarPopupWindowLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, -8.0f));
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarPopupWindowLayout2.setBackgroundColor(Theme.getColor("voipgroup_inviteMembersBackground"));
        }
        actionBarPopupWindowLayout2.setAnimationEnabled(false);
        actionBarPopupWindowLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ShareAlert.19
            private android.graphics.Rect popupRect = new android.graphics.Rect();

            {
                ShareAlert.this = this;
            }

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
        actionBarPopupWindowLayout2.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda13
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
            public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                ShareAlert.this.lambda$onSendLongClick$12(keyEvent);
            }
        });
        actionBarPopupWindowLayout2.setShownFromBottom(false);
        ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getContext(), true, true, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarMenuSubItem3.setTextColor(getThemedColor("voipgroup_nameText"));
            actionBarMenuSubItem3.setIconColor(getThemedColor("windowBackgroundWhiteHintText"));
        }
        actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("SendWithoutSound", R.string.SendWithoutSound), R.drawable.input_notify_off);
        actionBarMenuSubItem3.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout2.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlert.this.lambda$onSendLongClick$13(view2);
            }
        });
        ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(getContext(), true, true, this.resourcesProvider);
        if (this.darkTheme) {
            actionBarMenuSubItem4.setTextColor(getThemedColor("voipgroup_nameText"));
            actionBarMenuSubItem4.setIconColor(getThemedColor("windowBackgroundWhiteHintText"));
        }
        actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString("SendMessage", R.string.SendMessage), R.drawable.msg_send);
        actionBarMenuSubItem4.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout2.addView((View) actionBarMenuSubItem4, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlert.this.lambda$onSendLongClick$14(view2);
            }
        });
        if (!this.darkTheme) {
            str = "dialogButtonSelector";
        }
        actionBarPopupWindowLayout2.setupRadialSelectors(getThemedColor(str));
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
        if (this.keyboardVisible && (chatActivity = this.parentFragment) != null && chatActivity.contentView.getMeasuredHeight() > AndroidUtilities.dp(58.0f)) {
            measuredHeight = iArr[1] + view.getMeasuredHeight();
        } else {
            measuredHeight = (iArr[1] - linearLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f);
        }
        this.sendPopupWindow.showAtLocation(view, 51, ((iArr[0] + view.getMeasuredWidth()) - linearLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), measuredHeight);
        this.sendPopupWindow.dimBehind();
        view.performHapticFeedback(3, 2);
        return true;
    }

    public /* synthetic */ void lambda$onSendLongClick$9(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() != 4 || keyEvent.getRepeatCount() != 0 || (actionBarPopupWindow = this.sendPopupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.sendPopupWindow.dismiss();
    }

    public /* synthetic */ void lambda$onSendLongClick$10(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, View view) {
        this.showSendersName = true;
        actionBarMenuSubItem.setChecked(true);
        actionBarMenuSubItem2.setChecked(!this.showSendersName);
    }

    public /* synthetic */ void lambda$onSendLongClick$11(ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2, View view) {
        this.showSendersName = false;
        actionBarMenuSubItem.setChecked(false);
        actionBarMenuSubItem2.setChecked(!this.showSendersName);
    }

    public /* synthetic */ void lambda$onSendLongClick$12(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() != 4 || keyEvent.getRepeatCount() != 0 || (actionBarPopupWindow = this.sendPopupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.sendPopupWindow.dismiss();
    }

    public /* synthetic */ void lambda$onSendLongClick$13(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendInternal(false);
    }

    public /* synthetic */ void lambda$onSendLongClick$14(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendInternal(true);
    }

    protected void sendInternal(boolean z) {
        int i = 0;
        int i2 = 0;
        while (true) {
            boolean z2 = true;
            if (i2 < this.selectedDialogs.size()) {
                long keyAt = this.selectedDialogs.keyAt(i2);
                Context context = getContext();
                int i3 = this.currentAccount;
                if (this.frameLayout2.getTag() == null || this.commentTextView.length() <= 0) {
                    z2 = false;
                }
                if (AlertsCreator.checkSlowMode(context, i3, keyAt, z2)) {
                    return;
                }
                i2++;
            } else {
                if (this.sendingMessageObjects != null) {
                    ArrayList<Long> arrayList = new ArrayList();
                    while (i < this.selectedDialogs.size()) {
                        long keyAt2 = this.selectedDialogs.keyAt(i);
                        if (this.frameLayout2.getTag() != null && this.commentTextView.length() > 0) {
                            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.commentTextView.getText().toString(), keyAt2, null, null, null, true, null, null, null, z, 0, null);
                        }
                        int sendMessage = SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.sendingMessageObjects, keyAt2, !this.showSendersName, false, z, 0);
                        if (sendMessage != 0) {
                            arrayList.add(Long.valueOf(keyAt2));
                        }
                        if (this.selectedDialogs.size() == 1) {
                            AlertsCreator.showSendMediaAlert(sendMessage, this.parentFragment, null);
                            if (sendMessage != 0) {
                                break;
                            }
                        }
                        i++;
                    }
                    for (Long l : arrayList) {
                        this.selectedDialogs.remove(l.longValue());
                    }
                    if (!this.selectedDialogs.isEmpty()) {
                        onSend(this.selectedDialogs, this.sendingMessageObjects.size());
                    }
                } else {
                    SwitchView switchView = this.switchView;
                    int i4 = switchView != null ? switchView.currentTab : 0;
                    if (this.sendingText[i4] != null) {
                        while (i < this.selectedDialogs.size()) {
                            long keyAt3 = this.selectedDialogs.keyAt(i);
                            if (this.frameLayout2.getTag() != null && this.commentTextView.length() > 0) {
                                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.commentTextView.getText().toString(), keyAt3, null, null, null, true, null, null, null, z, 0, null);
                            }
                            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.sendingText[i4], keyAt3, null, null, null, true, null, null, null, z, 0, null);
                            i++;
                        }
                    }
                    onSend(this.selectedDialogs, 1);
                }
                ShareAlertDelegate shareAlertDelegate = this.delegate;
                if (shareAlertDelegate != null) {
                    shareAlertDelegate.didShare();
                }
                dismiss();
                return;
            }
        }
    }

    public int getCurrentTop() {
        if (this.gridView.getChildCount() != 0) {
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
        return -1000;
    }

    public void setDelegate(ShareAlertDelegate shareAlertDelegate) {
        this.delegate = shareAlertDelegate;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null && editTextEmoji.isPopupShowing()) {
            this.commentTextView.hidePopup(true);
        } else {
            super.onBackPressed();
        }
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

    @SuppressLint({"NewApi"})
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
        if (top >= 0 && holder != null && holder.getAdapterPosition() == 0) {
            this.lastOffset = childAt.getTop();
            runShadowAnimation(0, false);
        } else {
            this.lastOffset = Integer.MAX_VALUE;
            runShadowAnimation(0, true);
            top = i2;
        }
        int i3 = this.scrollOffsetY;
        if (i3 == top) {
            return;
        }
        this.previousScrollOffsetY = i3;
        RecyclerListView recyclerListView2 = this.gridView;
        float f = top;
        int i4 = (int) (this.currentPanTranslationY + f);
        this.scrollOffsetY = i4;
        recyclerListView2.setTopGlowOffset(i4);
        RecyclerListView recyclerListView3 = this.searchGridView;
        int i5 = (int) (f + this.currentPanTranslationY);
        this.scrollOffsetY = i5;
        recyclerListView3.setTopGlowOffset(i5);
        this.frameLayout.setTranslationY(this.scrollOffsetY + this.currentPanTranslationY);
        this.searchEmptyView.setTranslationY(this.scrollOffsetY + this.currentPanTranslationY);
        this.containerView.invalidate();
    }

    private void runShadowAnimation(final int i, final boolean z) {
        if ((!z || this.shadow[i].getTag() == null) && (z || this.shadow[i].getTag() != null)) {
            return;
        }
        this.shadow[i].setTag(z ? null : 1);
        if (z) {
            this.shadow[i].setVisibility(0);
        }
        AnimatorSet[] animatorSetArr = this.shadowAnimation;
        if (animatorSetArr[i] != null) {
            animatorSetArr[i].cancel();
        }
        this.shadowAnimation[i] = new AnimatorSet();
        AnimatorSet animatorSet = this.shadowAnimation[i];
        Animator[] animatorArr = new Animator[1];
        View view = this.shadow[i];
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
        animatorSet.playTogether(animatorArr);
        this.shadowAnimation[i].setDuration(150L);
        this.shadowAnimation[i].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.20
            {
                ShareAlert.this = this;
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

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (ShareAlert.this.shadowAnimation[i] == null || !ShareAlert.this.shadowAnimation[i].equals(animator)) {
                    return;
                }
                ShareAlert.this.shadowAnimation[i] = null;
            }
        });
        this.shadowAnimation[i].start();
    }

    private void copyLink(Context context) {
        String str;
        final boolean z = false;
        if (this.exportedMessageLink == null && this.linkToCopy[0] == null) {
            return;
        }
        try {
            SwitchView switchView = this.switchView;
            if (switchView != null) {
                str = this.linkToCopy[switchView.currentTab];
            } else {
                str = this.linkToCopy[0];
            }
            ClipboardManager clipboardManager = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
            if (str == null) {
                str = this.exportedMessageLink.link;
            }
            clipboardManager.setPrimaryClip(ClipData.newPlainText("label", str));
            ShareAlertDelegate shareAlertDelegate = this.delegate;
            if ((shareAlertDelegate != null && shareAlertDelegate.didCopy()) || !(this.parentActivity instanceof LaunchActivity)) {
                return;
            }
            TLRPC$TL_exportedMessageLink tLRPC$TL_exportedMessageLink = this.exportedMessageLink;
            if (tLRPC$TL_exportedMessageLink != null && tLRPC$TL_exportedMessageLink.link.contains("/c/")) {
                z = true;
            }
            ((LaunchActivity) this.parentActivity).showBulletin(new Function() { // from class: org.telegram.ui.Components.ShareAlert$$ExternalSyntheticLambda9
                @Override // androidx.arch.core.util.Function
                public final Object apply(Object obj) {
                    Bulletin lambda$copyLink$15;
                    lambda$copyLink$15 = ShareAlert.this.lambda$copyLink$15(z, (BulletinFactory) obj);
                    return lambda$copyLink$15;
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ Bulletin lambda$copyLink$15(boolean z, BulletinFactory bulletinFactory) {
        return bulletinFactory.createCopyLinkBulletin(z, this.resourcesProvider);
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
        int i = 4;
        if (textView != null) {
            ViewCompat.setImportantForAccessibility(textView, z ? 4 : 1);
        }
        LinearLayout linearLayout = this.sharesCountLayout;
        if (linearLayout != null) {
            if (!z) {
                i = 1;
            }
            ViewCompat.setImportantForAccessibility(linearLayout, i);
        }
        this.animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        FrameLayout frameLayout = this.frameLayout2;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        float f = 0.0f;
        fArr[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout, property, fArr));
        FrameLayout frameLayout2 = this.writeButtonContainer;
        Property property2 = View.SCALE_X;
        float[] fArr2 = new float[1];
        float f2 = 0.2f;
        fArr2[0] = z ? 1.0f : 0.2f;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout2, property2, fArr2));
        FrameLayout frameLayout3 = this.writeButtonContainer;
        Property property3 = View.SCALE_Y;
        float[] fArr3 = new float[1];
        fArr3[0] = z ? 1.0f : 0.2f;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout3, property3, fArr3));
        FrameLayout frameLayout4 = this.writeButtonContainer;
        Property property4 = View.ALPHA;
        float[] fArr4 = new float[1];
        fArr4[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout4, property4, fArr4));
        View view = this.selectedCountView;
        Property property5 = View.SCALE_X;
        float[] fArr5 = new float[1];
        fArr5[0] = z ? 1.0f : 0.2f;
        arrayList.add(ObjectAnimator.ofFloat(view, property5, fArr5));
        View view2 = this.selectedCountView;
        Property property6 = View.SCALE_Y;
        float[] fArr6 = new float[1];
        if (z) {
            f2 = 1.0f;
        }
        fArr6[0] = f2;
        arrayList.add(ObjectAnimator.ofFloat(view2, property6, fArr6));
        View view3 = this.selectedCountView;
        Property property7 = View.ALPHA;
        float[] fArr7 = new float[1];
        fArr7[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(view3, property7, fArr7));
        TextView textView2 = this.pickerBottomLayout;
        if (textView2 == null || textView2.getVisibility() != 0) {
            View view4 = this.shadow[1];
            Property property8 = View.ALPHA;
            float[] fArr8 = new float[1];
            if (z) {
                f = 1.0f;
            }
            fArr8[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(view4, property8, fArr8));
        }
        this.animatorSet.playTogether(arrayList);
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.setDuration(180L);
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ShareAlert.21
            {
                ShareAlert.this = this;
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

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (animator.equals(ShareAlert.this.animatorSet)) {
                    ShareAlert.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
        return true;
    }

    public void updateSelectedCount(int i) {
        if (this.selectedDialogs.size() == 0) {
            this.selectedCountView.setPivotX(0.0f);
            this.selectedCountView.setPivotY(0.0f);
            showCommentTextView(false);
            return;
        }
        this.selectedCountView.invalidate();
        if (!showCommentTextView(true) && i != 0) {
            this.selectedCountView.setPivotX(AndroidUtilities.dp(21.0f));
            this.selectedCountView.setPivotY(AndroidUtilities.dp(12.0f));
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            View view = this.selectedCountView;
            Property property = View.SCALE_X;
            float[] fArr = new float[2];
            float f = 1.1f;
            fArr[0] = i == 1 ? 1.1f : 0.9f;
            fArr[1] = 1.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
            View view2 = this.selectedCountView;
            Property property2 = View.SCALE_Y;
            float[] fArr2 = new float[2];
            if (i != 1) {
                f = 0.9f;
            }
            fArr2[0] = f;
            fArr2[1] = 1.0f;
            animatorArr[1] = ObjectAnimator.ofFloat(view2, property2, fArr2);
            animatorSet.playTogether(animatorArr);
            animatorSet.setInterpolator(new OvershootInterpolator());
            animatorSet.setDuration(180L);
            animatorSet.start();
            return;
        }
        this.selectedCountView.setPivotX(0.0f);
        this.selectedCountView.setPivotY(0.0f);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            AndroidUtilities.hideKeyboard(editTextEmoji.getEditText());
        }
        this.fullyShown = false;
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
    }

    /* loaded from: classes3.dex */
    public class ShareDialogsAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private ArrayList<TLRPC$Dialog> dialogs = new ArrayList<>();
        private LongSparseArray<TLRPC$Dialog> dialogsMap = new LongSparseArray<>();

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == 0 ? 1 : 0;
        }

        public ShareDialogsAdapter(Context context) {
            ShareAlert.this = r1;
            this.context = context;
            fetchDialogs();
        }

        public void fetchDialogs() {
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
            this.dialogs.clear();
            this.dialogsMap.clear();
            long j = UserConfig.getInstance(((BottomSheet) ShareAlert.this).currentAccount).clientUserId;
            if (!MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).dialogsForward.isEmpty()) {
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).dialogsForward.get(0);
                this.dialogs.add(tLRPC$Dialog);
                this.dialogsMap.put(tLRPC$Dialog.id, tLRPC$Dialog);
            }
            ArrayList arrayList = new ArrayList();
            ArrayList<TLRPC$Dialog> allDialogs = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getAllDialogs();
            for (int i = 0; i < allDialogs.size(); i++) {
                TLRPC$Dialog tLRPC$Dialog2 = allDialogs.get(i);
                if (tLRPC$Dialog2 instanceof TLRPC$TL_dialog) {
                    long j2 = tLRPC$Dialog2.id;
                    if (j2 != j && !DialogObject.isEncryptedDialog(j2)) {
                        if (!DialogObject.isUserDialog(tLRPC$Dialog2.id)) {
                            TLRPC$Chat chat = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
                            if (chat != null && !ChatObject.isNotInChat(chat) && ((!chat.gigagroup || ChatObject.hasAdminRights(chat)) && (!ChatObject.isChannel(chat) || chat.creator || (((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.post_messages) || chat.megagroup)))) {
                                if (tLRPC$Dialog2.folder_id == 1) {
                                    arrayList.add(tLRPC$Dialog2);
                                } else {
                                    this.dialogs.add(tLRPC$Dialog2);
                                }
                                this.dialogsMap.put(tLRPC$Dialog2.id, tLRPC$Dialog2);
                            }
                        } else {
                            if (tLRPC$Dialog2.folder_id == 1) {
                                arrayList.add(tLRPC$Dialog2);
                            } else {
                                this.dialogs.add(tLRPC$Dialog2);
                            }
                            this.dialogsMap.put(tLRPC$Dialog2.id, tLRPC$Dialog2);
                        }
                    }
                }
            }
            this.dialogs.addAll(arrayList);
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.dialogs.size();
            return size != 0 ? size + 1 : size;
        }

        public TLRPC$Dialog getItem(int i) {
            int i2 = i - 1;
            if (i2 < 0 || i2 >= this.dialogs.size()) {
                return null;
            }
            return this.dialogs.get(i2);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo1758onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shareDialogCell;
            if (i == 0) {
                shareDialogCell = new ShareDialogCell(this.context, ShareAlert.this.darkTheme ? 1 : 0, ShareAlert.this.resourcesProvider);
                shareDialogCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f)));
            } else {
                shareDialogCell = new View(this.context);
                shareDialogCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 56.0f : 109.0f)));
            }
            return new RecyclerListView.Holder(shareDialogCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ShareDialogCell shareDialogCell = (ShareDialogCell) viewHolder.itemView;
                long j = getItem(i).id;
                shareDialogCell.setDialog(j, ShareAlert.this.selectedDialogs.indexOfKey(j) >= 0, null);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ShareSearchAdapter extends RecyclerListView.SelectionAdapter {
        DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapter;
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
        private ArrayList<Object> searchResult = new ArrayList<>();
        int hintsCell = -1;
        int resentTitleCell = -1;
        int firstEmptyViewCell = -1;
        int recentDialogsStartRow = -1;
        int lastFilledItem = -1;
        boolean internalDialogsIsSearching = false;

        public ShareSearchAdapter(Context context) {
            ShareAlert.this = r2;
            this.context = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate(r2) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.1
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$onSetHashtags(this, arrayList, hashMap);
                }

                {
                    ShareSearchAdapter.this = this;
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
                public boolean canApplySearchResults(int i) {
                    return i == ShareSearchAdapter.this.lastSearchId;
                }
            });
        }

        private void searchDialogsInternal(final String str, final int i) {
            MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$searchDialogsInternal$1(str, i);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:189:0x03f8 A[Catch: Exception -> 0x0414, LOOP:7: B:173:0x0346->B:189:0x03f8, LOOP_END, TryCatch #0 {Exception -> 0x0414, blocks: (B:3:0x0002, B:5:0x0011, B:8:0x001e, B:10:0x002c, B:15:0x003a, B:17:0x0041, B:18:0x0043, B:19:0x0068, B:21:0x006e, B:36:0x0086, B:39:0x0090, B:24:0x0098, B:27:0x009e, B:30:0x00a9, B:43:0x00b1, B:46:0x00c2, B:47:0x00e7, B:49:0x00ed, B:52:0x0101, B:54:0x0108, B:57:0x0113, B:59:0x011d, B:62:0x0136, B:64:0x013c, B:68:0x0154, B:74:0x0164, B:76:0x016b, B:78:0x0185, B:80:0x018d, B:81:0x01bf, B:84:0x0198, B:72:0x01cd, B:95:0x01e4, B:96:0x01f1, B:98:0x01f7, B:99:0x021d, B:101:0x0223, B:106:0x023a, B:108:0x0242, B:111:0x0259, B:113:0x025f, B:139:0x0275, B:117:0x0278, B:119:0x027f, B:121:0x028c, B:123:0x0292, B:125:0x0298, B:127:0x029c, B:129:0x02a0, B:131:0x02a4, B:133:0x02a8, B:146:0x02ce, B:147:0x02d6, B:148:0x02dc, B:150:0x02e2, B:152:0x02ec, B:154:0x02f0, B:156:0x02f3, B:160:0x02f6, B:161:0x030d, B:163:0x0313, B:166:0x031f, B:169:0x0333, B:171:0x033c, B:174:0x0348, B:176:0x0350, B:179:0x0367, B:181:0x036d, B:185:0x0385, B:191:0x0390, B:193:0x0397, B:195:0x03ab, B:196:0x03b2, B:198:0x03bd, B:199:0x03f0, B:203:0x03c9, B:189:0x03f8, B:213:0x0406), top: B:2:0x0002 }] */
        /* JADX WARN: Removed duplicated region for block: B:190:0x0390 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01cd A[Catch: Exception -> 0x0414, LOOP:2: B:56:0x0111->B:72:0x01cd, LOOP_END, TryCatch #0 {Exception -> 0x0414, blocks: (B:3:0x0002, B:5:0x0011, B:8:0x001e, B:10:0x002c, B:15:0x003a, B:17:0x0041, B:18:0x0043, B:19:0x0068, B:21:0x006e, B:36:0x0086, B:39:0x0090, B:24:0x0098, B:27:0x009e, B:30:0x00a9, B:43:0x00b1, B:46:0x00c2, B:47:0x00e7, B:49:0x00ed, B:52:0x0101, B:54:0x0108, B:57:0x0113, B:59:0x011d, B:62:0x0136, B:64:0x013c, B:68:0x0154, B:74:0x0164, B:76:0x016b, B:78:0x0185, B:80:0x018d, B:81:0x01bf, B:84:0x0198, B:72:0x01cd, B:95:0x01e4, B:96:0x01f1, B:98:0x01f7, B:99:0x021d, B:101:0x0223, B:106:0x023a, B:108:0x0242, B:111:0x0259, B:113:0x025f, B:139:0x0275, B:117:0x0278, B:119:0x027f, B:121:0x028c, B:123:0x0292, B:125:0x0298, B:127:0x029c, B:129:0x02a0, B:131:0x02a4, B:133:0x02a8, B:146:0x02ce, B:147:0x02d6, B:148:0x02dc, B:150:0x02e2, B:152:0x02ec, B:154:0x02f0, B:156:0x02f3, B:160:0x02f6, B:161:0x030d, B:163:0x0313, B:166:0x031f, B:169:0x0333, B:171:0x033c, B:174:0x0348, B:176:0x0350, B:179:0x0367, B:181:0x036d, B:185:0x0385, B:191:0x0390, B:193:0x0397, B:195:0x03ab, B:196:0x03b2, B:198:0x03bd, B:199:0x03f0, B:203:0x03c9, B:189:0x03f8, B:213:0x0406), top: B:2:0x0002 }] */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0164 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$searchDialogsInternal$1(String str, int i) {
            LongSparseArray longSparseArray;
            String str2;
            int i2;
            LongSparseArray longSparseArray2;
            Object obj;
            LongSparseArray longSparseArray3;
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
            LongSparseArray longSparseArray4;
            String str3;
            char c;
            try {
                String lowerCase = str.trim().toLowerCase();
                if (lowerCase.length() == 0) {
                    this.lastSearchId = -1;
                    updateSearchResults(new ArrayList<>(), this.lastSearchId);
                    return;
                }
                String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                if (lowerCase.equals(translitString) || translitString.length() == 0) {
                    translitString = null;
                }
                int i3 = 0;
                int i4 = (translitString != null ? 1 : 0) + 1;
                String[] strArr = new String[i4];
                strArr[0] = lowerCase;
                if (translitString != null) {
                    strArr[1] = translitString;
                }
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                LongSparseArray longSparseArray5 = new LongSparseArray();
                SQLiteCursor queryFinalized = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized("SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 400", new Object[0]);
                while (queryFinalized.next()) {
                    long longValue = queryFinalized.longValue(0);
                    DialogSearchResult dialogSearchResult = new DialogSearchResult();
                    dialogSearchResult.date = queryFinalized.intValue(1);
                    longSparseArray5.put(longValue, dialogSearchResult);
                    if (DialogObject.isUserDialog(longValue)) {
                        if (!arrayList.contains(Long.valueOf(longValue))) {
                            arrayList.add(Long.valueOf(longValue));
                        }
                    } else if (DialogObject.isChatDialog(longValue)) {
                        long j = -longValue;
                        if (!arrayList2.contains(Long.valueOf(j))) {
                            arrayList2.add(Long.valueOf(j));
                        }
                    }
                }
                queryFinalized.dispose();
                String str4 = ";;;";
                if (!arrayList.isEmpty()) {
                    SQLiteCursor queryFinalized2 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, status, name FROM users WHERE uid IN(%s)", TextUtils.join(",", arrayList)), new Object[0]);
                    i2 = 0;
                    while (queryFinalized2.next()) {
                        String stringValue = queryFinalized2.stringValue(2);
                        String translitString2 = LocaleController.getInstance().getTranslitString(stringValue);
                        if (stringValue.equals(translitString2)) {
                            translitString2 = null;
                        }
                        int lastIndexOf = stringValue.lastIndexOf(str4);
                        String substring = lastIndexOf != -1 ? stringValue.substring(lastIndexOf + 3) : null;
                        char c2 = 0;
                        while (true) {
                            if (i3 >= i4) {
                                longSparseArray4 = longSparseArray5;
                                str3 = str4;
                                break;
                            }
                            char c3 = c2;
                            String str5 = strArr[i3];
                            if (!stringValue.startsWith(str5)) {
                                str3 = str4;
                                if (!stringValue.contains(" " + str5)) {
                                    if (translitString2 != null) {
                                        if (!translitString2.startsWith(str5)) {
                                            if (translitString2.contains(" " + str5)) {
                                            }
                                        }
                                    }
                                    c = (substring == null || !substring.startsWith(str5)) ? c3 : (char) 2;
                                    if (c == 0) {
                                        NativeByteBuffer byteBufferValue = queryFinalized2.byteBufferValue(0);
                                        if (byteBufferValue != null) {
                                            TLRPC$User TLdeserialize = TLRPC$User.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                            byteBufferValue.reuse();
                                            DialogSearchResult dialogSearchResult2 = (DialogSearchResult) longSparseArray5.get(TLdeserialize.id);
                                            TLRPC$UserStatus tLRPC$UserStatus = TLdeserialize.status;
                                            longSparseArray4 = longSparseArray5;
                                            if (tLRPC$UserStatus != null) {
                                                tLRPC$UserStatus.expires = queryFinalized2.intValue(1);
                                            }
                                            if (c == 1) {
                                                dialogSearchResult2.name = AndroidUtilities.generateSearchName(TLdeserialize.first_name, TLdeserialize.last_name, str5);
                                            } else {
                                                dialogSearchResult2.name = AndroidUtilities.generateSearchName("@" + TLdeserialize.username, null, "@" + str5);
                                            }
                                            dialogSearchResult2.object = TLdeserialize;
                                            dialogSearchResult2.dialog.id = TLdeserialize.id;
                                            i2++;
                                        } else {
                                            longSparseArray4 = longSparseArray5;
                                        }
                                    } else {
                                        i3++;
                                        c2 = c;
                                        str4 = str3;
                                    }
                                }
                            } else {
                                str3 = str4;
                            }
                            c = 1;
                            if (c == 0) {
                            }
                        }
                        str4 = str3;
                        longSparseArray5 = longSparseArray4;
                        i3 = 0;
                    }
                    longSparseArray = longSparseArray5;
                    str2 = str4;
                    queryFinalized2.dispose();
                } else {
                    longSparseArray = longSparseArray5;
                    str2 = str4;
                    i2 = 0;
                }
                if (!arrayList2.isEmpty()) {
                    SQLiteCursor queryFinalized3 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, name FROM chats WHERE uid IN(%s)", TextUtils.join(",", arrayList2)), new Object[0]);
                    while (queryFinalized3.next()) {
                        String stringValue2 = queryFinalized3.stringValue(1);
                        String translitString3 = LocaleController.getInstance().getTranslitString(stringValue2);
                        if (stringValue2.equals(translitString3)) {
                            translitString3 = null;
                        }
                        for (int i5 = 0; i5 < i4; i5++) {
                            String str6 = strArr[i5];
                            if (!stringValue2.startsWith(str6)) {
                                if (!stringValue2.contains(" " + str6)) {
                                    if (translitString3 != null) {
                                        if (!translitString3.startsWith(str6)) {
                                            if (translitString3.contains(" " + str6)) {
                                            }
                                        }
                                    }
                                }
                            }
                            NativeByteBuffer byteBufferValue2 = queryFinalized3.byteBufferValue(0);
                            if (byteBufferValue2 != null) {
                                TLRPC$Chat TLdeserialize2 = TLRPC$Chat.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                                byteBufferValue2.reuse();
                                if (TLdeserialize2 != null && !ChatObject.isNotInChat(TLdeserialize2) && (!ChatObject.isChannel(TLdeserialize2) || TLdeserialize2.creator || (((tLRPC$TL_chatAdminRights = TLdeserialize2.admin_rights) != null && tLRPC$TL_chatAdminRights.post_messages) || TLdeserialize2.megagroup))) {
                                    longSparseArray3 = longSparseArray;
                                    DialogSearchResult dialogSearchResult3 = (DialogSearchResult) longSparseArray3.get(-TLdeserialize2.id);
                                    dialogSearchResult3.name = AndroidUtilities.generateSearchName(TLdeserialize2.title, null, str6);
                                    dialogSearchResult3.object = TLdeserialize2;
                                    dialogSearchResult3.dialog.id = -TLdeserialize2.id;
                                    i2++;
                                    longSparseArray = longSparseArray3;
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
                } else {
                    longSparseArray2 = longSparseArray;
                }
                ArrayList<Object> arrayList3 = new ArrayList<>(i2);
                for (int i6 = 0; i6 < longSparseArray2.size(); i6++) {
                    DialogSearchResult dialogSearchResult4 = (DialogSearchResult) longSparseArray2.valueAt(i6);
                    if (dialogSearchResult4.object != null && dialogSearchResult4.name != null) {
                        arrayList3.add(dialogSearchResult4);
                    }
                }
                SQLiteCursor queryFinalized4 = MessagesStorage.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getDatabase().queryFinalized("SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid", new Object[0]);
                while (queryFinalized4.next()) {
                    if (longSparseArray2.indexOfKey(queryFinalized4.longValue(3)) < 0) {
                        String stringValue3 = queryFinalized4.stringValue(2);
                        String translitString4 = LocaleController.getInstance().getTranslitString(stringValue3);
                        if (stringValue3.equals(translitString4)) {
                            translitString4 = null;
                        }
                        String str7 = str2;
                        int lastIndexOf2 = stringValue3.lastIndexOf(str7);
                        String substring2 = lastIndexOf2 != -1 ? stringValue3.substring(lastIndexOf2 + 3) : null;
                        int i7 = 0;
                        char c4 = 0;
                        while (true) {
                            if (i7 >= i4) {
                                break;
                            }
                            String str8 = strArr[i7];
                            if (!stringValue3.startsWith(str8)) {
                                if (!stringValue3.contains(" " + str8)) {
                                    if (translitString4 != null) {
                                        if (!translitString4.startsWith(str8)) {
                                            if (translitString4.contains(" " + str8)) {
                                            }
                                        }
                                    }
                                    if (substring2 != null && substring2.startsWith(str8)) {
                                        c4 = 2;
                                    }
                                    if (c4 == 0) {
                                        NativeByteBuffer byteBufferValue3 = queryFinalized4.byteBufferValue(0);
                                        if (byteBufferValue3 != null) {
                                            TLRPC$User TLdeserialize3 = TLRPC$User.TLdeserialize(byteBufferValue3, byteBufferValue3.readInt32(false), false);
                                            byteBufferValue3.reuse();
                                            DialogSearchResult dialogSearchResult5 = new DialogSearchResult();
                                            TLRPC$UserStatus tLRPC$UserStatus2 = TLdeserialize3.status;
                                            if (tLRPC$UserStatus2 != null) {
                                                tLRPC$UserStatus2.expires = queryFinalized4.intValue(1);
                                            }
                                            dialogSearchResult5.dialog.id = TLdeserialize3.id;
                                            dialogSearchResult5.object = TLdeserialize3;
                                            if (c4 == 1) {
                                                dialogSearchResult5.name = AndroidUtilities.generateSearchName(TLdeserialize3.first_name, TLdeserialize3.last_name, str8);
                                                obj = null;
                                            } else {
                                                obj = null;
                                                dialogSearchResult5.name = AndroidUtilities.generateSearchName("@" + TLdeserialize3.username, null, "@" + str8);
                                            }
                                            arrayList3.add(dialogSearchResult5);
                                        }
                                    } else {
                                        i7++;
                                    }
                                }
                            }
                            c4 = 1;
                            if (c4 == 0) {
                            }
                        }
                        str2 = str7;
                    }
                }
                queryFinalized4.dispose();
                Collections.sort(arrayList3, ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda4.INSTANCE);
                updateSearchResults(arrayList3, i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        public static /* synthetic */ int lambda$searchDialogsInternal$0(Object obj, Object obj2) {
            int i = ((DialogSearchResult) obj).date;
            int i2 = ((DialogSearchResult) obj2).date;
            if (i < i2) {
                return 1;
            }
            return i > i2 ? -1 : 0;
        }

        private void updateSearchResults(final ArrayList<Object> arrayList, final int i) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$updateSearchResults$2(i, arrayList);
                }
            });
        }

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
                if (tLObject instanceof TLRPC$User) {
                    MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putUser((TLRPC$User) tLObject, true);
                } else if (tLObject instanceof TLRPC$Chat) {
                    MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).putChat((TLRPC$Chat) tLObject, true);
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
            if (getItemCount() == 0 && !this.searchAdapterHelper.isSearchInProgress() && !this.internalDialogsIsSearching) {
                ShareAlert.this.searchEmptyView.showProgress(false, true);
            } else {
                ShareAlert.this.recyclerItemsEnterAnimator.showItemsAnimated(i3);
            }
            notifyDataSetChanged();
            ShareAlert.this.checkCurrentList(true);
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
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda3
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

        public /* synthetic */ void lambda$searchDialogs$4(final String str, final int i) {
            this.searchRunnable = null;
            searchDialogsInternal(str, i);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlert.ShareSearchAdapter.this.lambda$searchDialogs$3(i, str);
                }
            };
            this.searchRunnable2 = runnable;
            AndroidUtilities.runOnUIThread(runnable);
        }

        public /* synthetic */ void lambda$searchDialogs$3(int i, String str) {
            this.searchRunnable2 = null;
            if (i != this.lastSearchId) {
                return;
            }
            this.searchAdapterHelper.queryServerSearch(str, true, true, true, true, false, 0L, false, 0, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            this.itemsCount = 0;
            this.hintsCell = -1;
            this.resentTitleCell = -1;
            this.recentDialogsStartRow = -1;
            this.lastFilledItem = -1;
            if (TextUtils.isEmpty(this.lastSearchText)) {
                int i = this.itemsCount;
                int i2 = i + 1;
                this.itemsCount = i2;
                this.firstEmptyViewCell = i;
                this.itemsCount = i2 + 1;
                this.hintsCell = i2;
                if (ShareAlert.this.recentSearchObjects.size() > 0) {
                    int i3 = this.itemsCount;
                    int i4 = i3 + 1;
                    this.itemsCount = i4;
                    this.resentTitleCell = i3;
                    this.recentDialogsStartRow = i4;
                    this.itemsCount = i4 + ShareAlert.this.recentSearchObjects.size();
                }
                int i5 = this.itemsCount;
                int i6 = i5 + 1;
                this.itemsCount = i6;
                this.lastFilledItem = i5;
                this.lastItemCont = i6;
                return i6;
            }
            int i7 = this.itemsCount;
            int i8 = i7 + 1;
            this.itemsCount = i8;
            this.firstEmptyViewCell = i7;
            int size = i8 + this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size();
            this.itemsCount = size;
            if (size == 1) {
                this.firstEmptyViewCell = -1;
                this.itemsCount = 0;
                this.lastItemCont = 0;
                return 0;
            }
            int i9 = size + 1;
            this.itemsCount = i9;
            this.lastFilledItem = size;
            this.lastItemCont = i9;
            return i9;
        }

        public TLRPC$Dialog getItem(int i) {
            int i2 = this.recentDialogsStartRow;
            if (i >= i2 && i2 >= 0) {
                int i3 = i - i2;
                if (i3 < 0 || i3 >= ShareAlert.this.recentSearchObjects.size()) {
                    return null;
                }
                TLObject tLObject = ((DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i3)).object;
                TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
                if (tLObject instanceof TLRPC$User) {
                    tLRPC$TL_dialog.id = ((TLRPC$User) tLObject).id;
                } else {
                    tLRPC$TL_dialog.id = -((TLRPC$Chat) tLObject).id;
                }
                return tLRPC$TL_dialog;
            }
            int i4 = i - 1;
            if (i4 < 0) {
                return null;
            }
            if (i4 < this.searchResult.size()) {
                return ((DialogSearchResult) this.searchResult.get(i4)).dialog;
            }
            int size = i4 - this.searchResult.size();
            ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            if (size >= localServerSearch.size()) {
                return null;
            }
            TLObject tLObject2 = localServerSearch.get(size);
            TLRPC$TL_dialog tLRPC$TL_dialog2 = new TLRPC$TL_dialog();
            if (tLObject2 instanceof TLRPC$User) {
                tLRPC$TL_dialog2.id = ((TLRPC$User) tLObject2).id;
            } else {
                tLRPC$TL_dialog2.id = -((TLRPC$Chat) tLObject2).id;
            }
            return tLRPC$TL_dialog2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return (viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 4) ? false : true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r5v3, types: [org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$2, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, org.telegram.ui.Components.RecyclerListView] */
        /* JADX WARN: Type inference failed for: r5v4, types: [org.telegram.ui.Cells.GraySectionCell, android.widget.FrameLayout] */
        /* JADX WARN: Type inference failed for: r5v5, types: [org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$5] */
        /* JADX WARN: Type inference failed for: r5v6, types: [android.view.View] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo1758onCreateViewHolder(ViewGroup viewGroup, int i) {
            ShareDialogCell shareDialogCell;
            if (i == 0) {
                ShareDialogCell shareDialogCell2 = new ShareDialogCell(this.context, ShareAlert.this.darkTheme ? 1 : 0, ShareAlert.this.resourcesProvider);
                shareDialogCell2.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f)));
                shareDialogCell = shareDialogCell2;
            } else if (i == 2) {
                ?? r5 = new RecyclerListView(this, this.context, ShareAlert.this.resourcesProvider) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.2
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
                r5.setItemAnimator(null);
                r5.setLayoutAnimation(null);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, this.context) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.3
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(0);
                r5.setLayoutManager(linearLayoutManager);
                DialogsSearchAdapter.CategoryAdapterRecycler categoryAdapterRecycler = new DialogsSearchAdapter.CategoryAdapterRecycler(this.context, ((BottomSheet) ShareAlert.this).currentAccount, true) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.4
                    {
                        ShareSearchAdapter.this = this;
                    }

                    @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.CategoryAdapterRecycler, androidx.recyclerview.widget.RecyclerView.Adapter
                    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
                        TLRPC$Chat tLRPC$Chat;
                        String str;
                        HintDialogCell hintDialogCell = (HintDialogCell) viewHolder.itemView;
                        if (ShareAlert.this.darkTheme) {
                            hintDialogCell.setColors("voipgroup_nameText", "voipgroup_inviteMembersBackground");
                        }
                        TLRPC$TL_topPeer tLRPC$TL_topPeer = MediaDataController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).hints.get(i2);
                        TLRPC$Peer tLRPC$Peer = tLRPC$TL_topPeer.peer;
                        long j = tLRPC$Peer.user_id;
                        TLRPC$User tLRPC$User = null;
                        if (j != 0) {
                            tLRPC$User = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getUser(Long.valueOf(tLRPC$TL_topPeer.peer.user_id));
                            tLRPC$Chat = null;
                        } else {
                            long j2 = tLRPC$Peer.channel_id;
                            if (j2 != 0) {
                                j = -j2;
                                tLRPC$Chat = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getChat(Long.valueOf(tLRPC$TL_topPeer.peer.channel_id));
                            } else {
                                long j3 = tLRPC$Peer.chat_id;
                                if (j3 != 0) {
                                    j = -j3;
                                    tLRPC$Chat = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getChat(Long.valueOf(tLRPC$TL_topPeer.peer.chat_id));
                                } else {
                                    tLRPC$Chat = null;
                                    j = 0;
                                }
                            }
                        }
                        boolean z = false;
                        boolean z2 = j == hintDialogCell.getDialogId();
                        hintDialogCell.setTag(Long.valueOf(j));
                        if (tLRPC$User != null) {
                            str = UserObject.getFirstName(tLRPC$User);
                        } else {
                            str = tLRPC$Chat != null ? tLRPC$Chat.title : "";
                        }
                        hintDialogCell.setDialog(j, true, str);
                        if (ShareAlert.this.selectedDialogs.indexOfKey(j) >= 0) {
                            z = true;
                        }
                        hintDialogCell.setChecked(z, z2);
                    }
                };
                this.categoryAdapter = categoryAdapterRecycler;
                r5.setAdapter(categoryAdapterRecycler);
                r5.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i2) {
                        ShareAlert.ShareSearchAdapter.this.lambda$onCreateViewHolder$5(view, i2);
                    }
                });
                shareDialogCell = r5;
            } else if (i == 3) {
                ?? graySectionCell = new GraySectionCell(this.context, ShareAlert.this.resourcesProvider);
                graySectionCell.setTextColor(ShareAlert.this.darkTheme ? "voipgroup_nameText" : "key_graySectionText");
                ShareAlert shareAlert = ShareAlert.this;
                graySectionCell.setBackgroundColor(shareAlert.getThemedColor(shareAlert.darkTheme ? "voipgroup_searchBackground" : "graySection"));
                graySectionCell.setText(LocaleController.getString("Recent", R.string.Recent));
                shareDialogCell = graySectionCell;
            } else if (i != 4) {
                ?? view = new View(this.context);
                view.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp((!ShareAlert.this.darkTheme || ShareAlert.this.linkToCopy[1] == null) ? 56.0f : 109.0f)));
                shareDialogCell = view;
            } else {
                shareDialogCell = new View(this.context) { // from class: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.5
                    {
                        ShareSearchAdapter.this = this;
                    }

                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(ShareAlert.this.searchLayoutManager.lastItemHeight, 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder(shareDialogCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$5(View view, int i) {
            TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
            TLRPC$Peer tLRPC$Peer = MediaDataController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).hints.get(i).peer;
            long j = tLRPC$Peer.user_id;
            if (j == 0) {
                long j2 = tLRPC$Peer.channel_id;
                if (j2 == 0) {
                    j2 = tLRPC$Peer.chat_id;
                    if (j2 == 0) {
                        j = 0;
                    }
                }
                j = -j2;
            }
            tLRPC$TL_dialog.id = j;
            ShareAlert.this.selectDialog(null, tLRPC$TL_dialog);
            ((HintDialogCell) view).setChecked(ShareAlert.this.selectedDialogs.indexOfKey(j) >= 0, true);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            long j;
            CharSequence charSequence;
            int indexOfIgnoreCase;
            TLRPC$User user;
            int indexOfIgnoreCase2;
            if (viewHolder.getItemViewType() == 0) {
                ShareDialogCell shareDialogCell = (ShareDialogCell) viewHolder.itemView;
                String str = null;
                long j2 = 0;
                boolean z = true;
                if (TextUtils.isEmpty(this.lastSearchText)) {
                    int i2 = this.recentDialogsStartRow;
                    if (i2 >= 0 && i >= i2) {
                        TLObject tLObject = ((DialogsSearchAdapter.RecentSearchObject) ShareAlert.this.recentSearchObjects.get(i - i2)).object;
                        if (tLObject instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                            j2 = tLRPC$User.id;
                            str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                        } else if (tLObject instanceof TLRPC$Chat) {
                            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                            j2 = -tLRPC$Chat.id;
                            str = tLRPC$Chat.title;
                        } else if ((tLObject instanceof TLRPC$TL_encryptedChat) && (user = MessagesController.getInstance(((BottomSheet) ShareAlert.this).currentAccount).getUser(Long.valueOf(((TLRPC$TL_encryptedChat) tLObject).user_id))) != null) {
                            j2 = user.id;
                            str = ContactsController.formatName(user.first_name, user.last_name);
                        }
                        String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                        if (!TextUtils.isEmpty(lastFoundUsername) && str != null && (indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str.toString(), lastFoundUsername)) != -1) {
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("windowBackgroundWhiteBlueText4", ShareAlert.this.resourcesProvider), indexOfIgnoreCase2, lastFoundUsername.length() + indexOfIgnoreCase2, 33);
                            str = spannableStringBuilder;
                        }
                    }
                    long j3 = (int) j2;
                    if (ShareAlert.this.selectedDialogs.indexOfKey(j2) < 0) {
                        z = false;
                    }
                    shareDialogCell.setDialog(j3, z, str);
                    return;
                }
                int i3 = i - 1;
                if (i3 < this.searchResult.size()) {
                    DialogSearchResult dialogSearchResult = (DialogSearchResult) this.searchResult.get(i3);
                    j = dialogSearchResult.dialog.id;
                    charSequence = dialogSearchResult.name;
                } else {
                    TLObject tLObject2 = this.searchAdapterHelper.getLocalServerSearch().get(i3 - this.searchResult.size());
                    if (tLObject2 instanceof TLRPC$User) {
                        TLRPC$User tLRPC$User2 = (TLRPC$User) tLObject2;
                        j = tLRPC$User2.id;
                        charSequence = ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name);
                    } else {
                        TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) tLObject2;
                        j = -tLRPC$Chat2.id;
                        charSequence = tLRPC$Chat2.title;
                    }
                    String lastFoundUsername2 = this.searchAdapterHelper.getLastFoundUsername();
                    if (!TextUtils.isEmpty(lastFoundUsername2) && charSequence != null && (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(charSequence.toString(), lastFoundUsername2)) != -1) {
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(charSequence);
                        spannableStringBuilder2.setSpan(new ForegroundColorSpanThemable("windowBackgroundWhiteBlueText4", ShareAlert.this.resourcesProvider), indexOfIgnoreCase, lastFoundUsername2.length() + indexOfIgnoreCase, 33);
                        charSequence = spannableStringBuilder2;
                    }
                }
                if (ShareAlert.this.selectedDialogs.indexOfKey(j) < 0) {
                    z = false;
                }
                shareDialogCell.setDialog(j, z, charSequence);
            } else if (viewHolder.getItemViewType() != 2) {
            } else {
                ((RecyclerListView) viewHolder.itemView).getAdapter().notifyDataSetChanged();
            }
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
            return i == this.resentTitleCell ? 3 : 0;
        }

        public int getSpanSize(int i, int i2) {
            if (i2 == this.hintsCell || i2 == this.resentTitleCell || i2 == this.firstEmptyViewCell || i2 == this.lastFilledItem) {
                return i;
            }
            return 1;
        }
    }

    public void checkCurrentList(boolean z) {
        boolean z2 = true;
        if (!TextUtils.isEmpty(this.searchView.searchEditText.getText()) || (this.keyboardVisible && this.searchView.searchEditText.hasFocus())) {
            this.updateSearchAdapter = true;
            AndroidUtilities.updateViewVisibilityAnimated(this.gridView, false, 0.98f, true);
            AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, true);
        } else {
            AndroidUtilities.updateViewVisibilityAnimated(this.gridView, true, 0.98f, true);
            AndroidUtilities.updateViewVisibilityAnimated(this.searchGridView, false);
            z2 = false;
        }
        if (this.searchIsVisible != z2 || z) {
            this.searchIsVisible = z2;
            this.searchAdapter.notifyDataSetChanged();
            this.listAdapter.notifyDataSetChanged();
            if (this.searchIsVisible) {
                if (this.lastOffset == Integer.MAX_VALUE) {
                    ((LinearLayoutManager) this.searchGridView.getLayoutManager()).scrollToPositionWithOffset(0, -this.searchGridView.getPaddingTop());
                } else {
                    ((LinearLayoutManager) this.searchGridView.getLayoutManager()).scrollToPositionWithOffset(0, this.lastOffset - this.searchGridView.getPaddingTop());
                }
                this.searchAdapter.searchDialogs(this.searchView.searchEditText.getText().toString());
            } else if (this.lastOffset == Integer.MAX_VALUE) {
                this.layoutManager.scrollToPositionWithOffset(0, 0);
            } else {
                this.layoutManager.scrollToPositionWithOffset(0, 0);
            }
        }
    }
}
