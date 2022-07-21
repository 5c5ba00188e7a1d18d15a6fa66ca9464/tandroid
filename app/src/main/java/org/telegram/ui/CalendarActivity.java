package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotoVideo;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotos;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterVideo;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messages_getSearchResultsCalendar;
import org.telegram.tgnet.TLRPC$TL_messages_searchResultsCalendar;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.HideViewAfterAnimation;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class CalendarActivity extends BaseFragment {
    CalendarAdapter adapter;
    BackDrawable backDrawable;
    private View blurredView;
    private FrameLayout bottomBar;
    private int calendarType;
    Callback callback;
    private boolean canClearHistory;
    private boolean checkEnterItems;
    FrameLayout contentView;
    private int dateSelectedEnd;
    private int dateSelectedStart;
    private long dialogId;
    boolean endReached;
    private boolean inSelectionMode;
    private boolean isOpened;
    int lastDaysSelected;
    int lastId;
    boolean lastInSelectionMode;
    LinearLayoutManager layoutManager;
    RecyclerListView listView;
    private boolean loading;
    private int minDate;
    int minMontYear;
    int monthCount;
    private int photosVideosTypeFilter;
    TextView removeDaysButton;
    TextView selectDaysButton;
    HintView selectDaysHint;
    int selectedMonth;
    int selectedYear;
    private ValueAnimator selectionAnimator;
    int startFromMonth;
    int startFromYear;
    TextPaint textPaint = new TextPaint(1);
    TextPaint activeTextPaint = new TextPaint(1);
    TextPaint textPaint2 = new TextPaint(1);
    private Paint selectOutlinePaint = new Paint(1);
    private Paint selectPaint = new Paint(1);
    Paint blackoutPaint = new Paint(1);
    SparseArray<SparseArray<PeriodDay>> messagesByYearMounth = new SparseArray<>();
    int startOffset = 0;

    /* loaded from: classes3.dex */
    public interface Callback {
        void onDateSelected(int i, int i2);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean needDelayOpenAnimation() {
        return true;
    }

    public CalendarActivity(Bundle bundle, int i, int i2) {
        super(bundle);
        this.photosVideosTypeFilter = i;
        if (i2 != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(i2 * 1000);
            this.selectedYear = calendar.get(1);
            this.selectedMonth = calendar.get(2);
        }
        this.selectOutlinePaint.setStyle(Paint.Style.STROKE);
        this.selectOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
        this.selectOutlinePaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.dialogId = getArguments().getLong("dialog_id");
        this.calendarType = getArguments().getInt("type");
        if (this.dialogId >= 0) {
            this.canClearHistory = true;
        } else {
            this.canClearHistory = false;
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.textPaint.setTextSize(AndroidUtilities.dp(16.0f));
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint2.setTextSize(AndroidUtilities.dp(11.0f));
        this.textPaint2.setTextAlign(Paint.Align.CENTER);
        this.textPaint2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.activeTextPaint.setTextSize(AndroidUtilities.dp(16.0f));
        this.activeTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.activeTextPaint.setTextAlign(Paint.Align.CENTER);
        this.contentView = new AnonymousClass1(context);
        createActionBar(context);
        this.contentView.addView(this.actionBar);
        this.actionBar.setTitle(LocaleController.getString("Calendar", 2131624784));
        this.actionBar.setCastShadows(false);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.listView = anonymousClass2;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.layoutManager = linearLayoutManager;
        anonymousClass2.setLayoutManager(linearLayoutManager);
        this.layoutManager.setReverseLayout(true);
        RecyclerListView recyclerListView = this.listView;
        CalendarAdapter calendarAdapter = new CalendarAdapter(this, null);
        this.adapter = calendarAdapter;
        recyclerListView.setAdapter(calendarAdapter);
        this.listView.addOnScrollListener(new AnonymousClass3());
        boolean z = this.calendarType == 0 && this.canClearHistory;
        this.contentView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 36.0f, 0.0f, z ? 48.0f : 0.0f));
        this.contentView.addView(new AnonymousClass4(context, new String[]{LocaleController.getString("CalendarWeekNameShortMonday", 2131624786), LocaleController.getString("CalendarWeekNameShortTuesday", 2131624790), LocaleController.getString("CalendarWeekNameShortWednesday", 2131624791), LocaleController.getString("CalendarWeekNameShortThursday", 2131624789), LocaleController.getString("CalendarWeekNameShortFriday", 2131624785), LocaleController.getString("CalendarWeekNameShortSaturday", 2131624787), LocaleController.getString("CalendarWeekNameShortSunday", 2131624788)}, ContextCompat.getDrawable(context, 2131165446).mutate()), LayoutHelper.createFrame(-1, 38.0f, 0, 0.0f, 0.0f, 0.0f, 0.0f));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass5());
        this.fragmentView = this.contentView;
        Calendar calendar = Calendar.getInstance();
        this.startFromYear = calendar.get(1);
        int i = calendar.get(2);
        this.startFromMonth = i;
        int i2 = this.selectedYear;
        if (i2 != 0) {
            int i3 = ((((this.startFromYear - i2) * 12) + i) - this.selectedMonth) + 1;
            this.monthCount = i3;
            this.layoutManager.scrollToPositionWithOffset(i3 - 1, AndroidUtilities.dp(120.0f));
        }
        if (this.monthCount < 3) {
            this.monthCount = 3;
        }
        BackDrawable backDrawable = new BackDrawable(false);
        this.backDrawable = backDrawable;
        this.actionBar.setBackButtonDrawable(backDrawable);
        this.backDrawable.setRotation(0.0f, false);
        loadNext();
        updateColors();
        this.activeTextPaint.setColor(-1);
        if (z) {
            AnonymousClass6 anonymousClass6 = new AnonymousClass6(this, context);
            this.bottomBar = anonymousClass6;
            anonymousClass6.setWillNotDraw(false);
            this.bottomBar.setPadding(0, AndroidUtilities.getShadowHeight(), 0, 0);
            this.bottomBar.setClipChildren(false);
            TextView textView = new TextView(context);
            this.selectDaysButton = textView;
            textView.setGravity(17);
            this.selectDaysButton.setTextSize(1, 15.0f);
            this.selectDaysButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.selectDaysButton.setOnClickListener(new CalendarActivity$$ExternalSyntheticLambda2(this));
            this.selectDaysButton.setText(LocaleController.getString("SelectDays", 2131628228));
            this.selectDaysButton.setAllCaps(true);
            this.bottomBar.addView(this.selectDaysButton, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 0.0f, 0.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.removeDaysButton = textView2;
            textView2.setGravity(17);
            this.removeDaysButton.setTextSize(1, 15.0f);
            this.removeDaysButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.removeDaysButton.setOnClickListener(new CalendarActivity$$ExternalSyntheticLambda1(this));
            this.removeDaysButton.setAllCaps(true);
            this.removeDaysButton.setVisibility(8);
            this.bottomBar.addView(this.removeDaysButton, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 0.0f, 0.0f, 0.0f));
            this.contentView.addView(this.bottomBar, LayoutHelper.createFrame(-1, 48.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
            this.selectDaysButton.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(Theme.getColor("chat_fieldOverlayText"), 51), 2));
            this.removeDaysButton.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(Theme.getColor("dialogTextRed"), 51), 2));
            this.selectDaysButton.setTextColor(Theme.getColor("chat_fieldOverlayText"));
            this.removeDaysButton.setTextColor(Theme.getColor("dialogTextRed"));
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.CalendarActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        int lastSize;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            CalendarActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int measuredHeight = (getMeasuredHeight() + getMeasuredWidth()) << 16;
            if (this.lastSize != measuredHeight) {
                this.lastSize = measuredHeight;
                CalendarActivity.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            CalendarActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            CalendarActivity.this.checkEnterItems = false;
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends RecyclerView.OnScrollListener {
        AnonymousClass3() {
            CalendarActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            CalendarActivity.this.checkLoadNext();
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends View {
        final /* synthetic */ String[] val$daysOfWeek;
        final /* synthetic */ Drawable val$headerShadowDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, String[] strArr, Drawable drawable) {
            super(context);
            CalendarActivity.this = r1;
            this.val$daysOfWeek = strArr;
            this.val$headerShadowDrawable = drawable;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float measuredWidth = getMeasuredWidth() / 7.0f;
            for (int i = 0; i < 7; i++) {
                canvas.drawText(this.val$daysOfWeek[i], (i * measuredWidth) + (measuredWidth / 2.0f), ((getMeasuredHeight() - AndroidUtilities.dp(2.0f)) / 2.0f) + AndroidUtilities.dp(5.0f), CalendarActivity.this.textPaint2);
            }
            this.val$headerShadowDrawable.setBounds(0, getMeasuredHeight() - AndroidUtilities.dp(3.0f), getMeasuredWidth(), getMeasuredHeight());
            this.val$headerShadowDrawable.draw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass5() {
            CalendarActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (CalendarActivity.this.dateSelectedStart != 0 || CalendarActivity.this.dateSelectedEnd != 0 || CalendarActivity.this.inSelectionMode) {
                    CalendarActivity.this.inSelectionMode = false;
                    CalendarActivity.this.dateSelectedStart = 0;
                    CalendarActivity.this.dateSelectedEnd = 0;
                    CalendarActivity.this.updateTitle();
                    CalendarActivity.this.animateSelection();
                    return;
                }
                CalendarActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends FrameLayout {
        AnonymousClass6(CalendarActivity calendarActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.getShadowHeight(), Theme.dividerPaint);
        }
    }

    public /* synthetic */ void lambda$createView$0(View view) {
        this.inSelectionMode = true;
        updateTitle();
    }

    public /* synthetic */ void lambda$createView$1(View view) {
        int i = this.lastDaysSelected;
        if (i == 0) {
            if (this.selectDaysHint == null) {
                HintView hintView = new HintView(this.contentView.getContext(), 8);
                this.selectDaysHint = hintView;
                hintView.setExtraTranslationY(AndroidUtilities.dp(24.0f));
                this.contentView.addView(this.selectDaysHint, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
                this.selectDaysHint.setText(LocaleController.getString("SelectDaysTooltip", 2131628229));
            }
            this.selectDaysHint.showForView(this.bottomBar, true);
            return;
        }
        AlertsCreator.createClearDaysDialogAlert(this, i, getMessagesController().getUser(Long.valueOf(this.dialogId)), null, false, new AnonymousClass7(), null);
    }

    /* renamed from: org.telegram.ui.CalendarActivity$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements MessagesStorage.BooleanCallback {
        AnonymousClass7() {
            CalendarActivity.this = r1;
        }

        @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
        public void run(boolean z) {
            CalendarActivity.this.finishFragment();
            if (((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() >= 2) {
                BaseFragment baseFragment = ((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() - 2);
                if (!(baseFragment instanceof ChatActivity)) {
                    return;
                }
                ((ChatActivity) baseFragment).deleteHistory(CalendarActivity.this.dateSelectedStart, CalendarActivity.this.dateSelectedEnd + 86400, z);
            }
        }
    }

    public void updateColors() {
        this.actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.activeTextPaint.setColor(-1);
        this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textPaint2.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.actionBar.setTitleColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.backDrawable.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteBlackText"), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor("listSelectorSDK21"), false);
    }

    private void loadNext() {
        if (this.loading || this.endReached) {
            return;
        }
        this.loading = true;
        TLRPC$TL_messages_getSearchResultsCalendar tLRPC$TL_messages_getSearchResultsCalendar = new TLRPC$TL_messages_getSearchResultsCalendar();
        int i = this.photosVideosTypeFilter;
        if (i == 1) {
            tLRPC$TL_messages_getSearchResultsCalendar.filter = new TLRPC$TL_inputMessagesFilterPhotos();
        } else if (i == 2) {
            tLRPC$TL_messages_getSearchResultsCalendar.filter = new TLRPC$TL_inputMessagesFilterVideo();
        } else {
            tLRPC$TL_messages_getSearchResultsCalendar.filter = new TLRPC$TL_inputMessagesFilterPhotoVideo();
        }
        tLRPC$TL_messages_getSearchResultsCalendar.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
        tLRPC$TL_messages_getSearchResultsCalendar.offset_id = this.lastId;
        Calendar calendar = Calendar.getInstance();
        this.listView.setItemAnimator(null);
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getSearchResultsCalendar, new CalendarActivity$$ExternalSyntheticLambda4(this, calendar));
    }

    public /* synthetic */ void lambda$loadNext$3(Calendar calendar, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new CalendarActivity$$ExternalSyntheticLambda3(this, tLRPC$TL_error, tLObject, calendar));
    }

    public /* synthetic */ void lambda$loadNext$2(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, Calendar calendar) {
        int i;
        int i2;
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_searchResultsCalendar tLRPC$TL_messages_searchResultsCalendar = (TLRPC$TL_messages_searchResultsCalendar) tLObject;
            int i3 = 0;
            while (true) {
                i = 5;
                i2 = 2;
                if (i3 >= tLRPC$TL_messages_searchResultsCalendar.periods.size()) {
                    break;
                }
                calendar.setTimeInMillis(tLRPC$TL_messages_searchResultsCalendar.periods.get(i3).date * 1000);
                int i4 = (calendar.get(1) * 100) + calendar.get(2);
                SparseArray<PeriodDay> sparseArray = this.messagesByYearMounth.get(i4);
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                    this.messagesByYearMounth.put(i4, sparseArray);
                }
                PeriodDay periodDay = new PeriodDay(this, null);
                periodDay.messageObject = new MessageObject(this.currentAccount, tLRPC$TL_messages_searchResultsCalendar.messages.get(i3), false, false);
                periodDay.date = (int) (calendar.getTimeInMillis() / 1000);
                int i5 = this.startOffset + tLRPC$TL_messages_searchResultsCalendar.periods.get(i3).count;
                this.startOffset = i5;
                periodDay.startOffset = i5;
                int i6 = calendar.get(5) - 1;
                if (sparseArray.get(i6, null) == null || !sparseArray.get(i6, null).hasImage) {
                    sparseArray.put(i6, periodDay);
                }
                int i7 = this.minMontYear;
                if (i4 < i7 || i7 == 0) {
                    this.minMontYear = i4;
                }
                i3++;
            }
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            int i8 = tLRPC$TL_messages_searchResultsCalendar.min_date;
            this.minDate = i8;
            while (i8 < currentTimeMillis) {
                calendar.setTimeInMillis(i8 * 1000);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                int i9 = (calendar.get(1) * 100) + calendar.get(i2);
                SparseArray<PeriodDay> sparseArray2 = this.messagesByYearMounth.get(i9);
                if (sparseArray2 == null) {
                    sparseArray2 = new SparseArray<>();
                    this.messagesByYearMounth.put(i9, sparseArray2);
                }
                int i10 = calendar.get(i) - 1;
                if (sparseArray2.get(i10, null) == null) {
                    PeriodDay periodDay2 = new PeriodDay(this, null);
                    periodDay2.hasImage = false;
                    periodDay2.date = (int) (calendar.getTimeInMillis() / 1000);
                    sparseArray2.put(i10, periodDay2);
                }
                i8 += 86400;
                i = 5;
                i2 = 2;
            }
            this.loading = false;
            if (!tLRPC$TL_messages_searchResultsCalendar.messages.isEmpty()) {
                ArrayList<TLRPC$Message> arrayList = tLRPC$TL_messages_searchResultsCalendar.messages;
                this.lastId = arrayList.get(arrayList.size() - 1).id;
                this.endReached = false;
                checkLoadNext();
            } else {
                this.endReached = true;
            }
            if (this.isOpened) {
                this.checkEnterItems = true;
            }
            this.listView.invalidate();
            int timeInMillis = ((int) (((calendar.getTimeInMillis() / 1000) - tLRPC$TL_messages_searchResultsCalendar.min_date) / 2629800)) + 1;
            this.adapter.notifyItemRangeChanged(0, this.monthCount);
            int i11 = this.monthCount;
            if (timeInMillis > i11) {
                this.adapter.notifyItemRangeInserted(i11 + 1, timeInMillis);
                this.monthCount = timeInMillis;
            }
            if (!this.endReached) {
                return;
            }
            resumeDelayedFragmentAnimation();
        }
    }

    public void checkLoadNext() {
        if (this.loading || this.endReached) {
            return;
        }
        int i = Integer.MAX_VALUE;
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof MonthView) {
                MonthView monthView = (MonthView) childAt;
                int i3 = (monthView.currentYear * 100) + monthView.currentMonthInYear;
                if (i3 < i) {
                    i = i3;
                }
            }
        }
        int i4 = this.minMontYear;
        if (((i4 / 100) * 12) + (i4 % 100) + 3 < ((i / 100) * 12) + (i % 100)) {
            return;
        }
        loadNext();
    }

    /* loaded from: classes3.dex */
    public class CalendarAdapter extends RecyclerView.Adapter {
        private CalendarAdapter() {
            CalendarActivity.this = r1;
        }

        /* synthetic */ CalendarAdapter(CalendarActivity calendarActivity, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new MonthView(viewGroup.getContext()));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            MonthView monthView = (MonthView) viewHolder.itemView;
            CalendarActivity calendarActivity = CalendarActivity.this;
            int i2 = calendarActivity.startFromYear - (i / 12);
            int i3 = calendarActivity.startFromMonth - (i % 12);
            if (i3 < 0) {
                i3 += 12;
                i2--;
            }
            monthView.setDate(i2, i3, calendarActivity.messagesByYearMounth.get((i2 * 100) + i3), monthView.currentYear == i2 && monthView.currentMonthInYear == i3);
            monthView.startSelectionAnimation(CalendarActivity.this.dateSelectedStart, CalendarActivity.this.dateSelectedEnd);
            monthView.setSelectionValue(1.0f);
            CalendarActivity.this.updateRowSelections(monthView, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            CalendarActivity calendarActivity = CalendarActivity.this;
            return ((calendarActivity.startFromYear - (i / 12)) * 100) + (calendarActivity.startFromMonth - (i % 12));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return CalendarActivity.this.monthCount;
        }
    }

    /* loaded from: classes3.dex */
    public class MonthView extends FrameLayout {
        int cellCount;
        int currentMonthInYear;
        int currentYear;
        int daysInMonth;
        GestureDetectorCompat gestureDetector;
        int startDayOfWeek;
        int startMonthTime;
        SimpleTextView titleView;
        SparseArray<PeriodDay> messagesByDays = new SparseArray<>();
        SparseArray<ImageReceiver> imagesByDays = new SparseArray<>();
        private SparseArray<ValueAnimator> rowAnimators = new SparseArray<>();
        private SparseArray<RowAnimationValue> rowSelectionPos = new SparseArray<>();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MonthView(Context context) {
            super(context);
            CalendarActivity.this = r10;
            boolean z = false;
            setWillNotDraw(false);
            this.titleView = new SimpleTextView(context);
            if (r10.calendarType == 0 && r10.canClearHistory) {
                this.titleView.setOnLongClickListener(new CalendarActivity$MonthView$$ExternalSyntheticLambda1(this));
                this.titleView.setOnClickListener(new AnonymousClass1(r10));
            }
            this.titleView.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 2));
            this.titleView.setTextSize(15);
            this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleView.setGravity(17);
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            addView(this.titleView, LayoutHelper.createFrame(-1, 28.0f, 0, 0.0f, 12.0f, 0.0f, 4.0f));
            GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(context, new AnonymousClass2(r10, context));
            this.gestureDetector = gestureDetectorCompat;
            gestureDetectorCompat.setIsLongpressEnabled(r10.calendarType == 0 ? true : z);
        }

        public /* synthetic */ boolean lambda$new$0(View view) {
            if (this.messagesByDays == null) {
                return false;
            }
            int i = -1;
            int i2 = -1;
            for (int i3 = 0; i3 < this.daysInMonth; i3++) {
                PeriodDay periodDay = this.messagesByDays.get(i3, null);
                if (periodDay != null) {
                    if (i == -1) {
                        i = periodDay.date;
                    }
                    i2 = periodDay.date;
                }
            }
            if (i >= 0 && i2 >= 0) {
                CalendarActivity.this.inSelectionMode = true;
                CalendarActivity.this.dateSelectedStart = i;
                CalendarActivity.this.dateSelectedEnd = i2;
                CalendarActivity.this.updateTitle();
                CalendarActivity.this.animateSelection();
            }
            return false;
        }

        /* renamed from: org.telegram.ui.CalendarActivity$MonthView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1(CalendarActivity calendarActivity) {
                MonthView.this = r1;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MonthView monthView;
                MonthView monthView2 = MonthView.this;
                if (monthView2.messagesByDays != null && CalendarActivity.this.inSelectionMode) {
                    int i = 0;
                    int i2 = -1;
                    int i3 = -1;
                    while (true) {
                        monthView = MonthView.this;
                        if (i >= monthView.daysInMonth) {
                            break;
                        }
                        PeriodDay periodDay = monthView.messagesByDays.get(i, null);
                        if (periodDay != null) {
                            if (i2 == -1) {
                                i2 = periodDay.date;
                            }
                            i3 = periodDay.date;
                        }
                        i++;
                    }
                    if (i2 < 0 || i3 < 0) {
                        return;
                    }
                    CalendarActivity.this.dateSelectedStart = i2;
                    CalendarActivity.this.dateSelectedEnd = i3;
                    CalendarActivity.this.updateTitle();
                    CalendarActivity.this.animateSelection();
                }
            }
        }

        /* renamed from: org.telegram.ui.CalendarActivity$MonthView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends GestureDetector.SimpleOnGestureListener {
            final /* synthetic */ Context val$context;

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            AnonymousClass2(CalendarActivity calendarActivity, Context context) {
                MonthView.this = r1;
                this.val$context = context;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            @SuppressLint({"NotifyDataSetChanged"})
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                PeriodDay dayAtCoord;
                MessageObject messageObject;
                Callback callback;
                if (CalendarActivity.this.calendarType == 1 && MonthView.this.messagesByDays != null && (dayAtCoord = getDayAtCoord(motionEvent.getX(), motionEvent.getY())) != null && (messageObject = dayAtCoord.messageObject) != null && (callback = CalendarActivity.this.callback) != null) {
                    callback.onDateSelected(messageObject.getId(), dayAtCoord.startOffset);
                    CalendarActivity.this.finishFragment();
                }
                MonthView monthView = MonthView.this;
                if (monthView.messagesByDays != null) {
                    if (CalendarActivity.this.inSelectionMode) {
                        PeriodDay dayAtCoord2 = getDayAtCoord(motionEvent.getX(), motionEvent.getY());
                        if (dayAtCoord2 != null) {
                            if (CalendarActivity.this.selectionAnimator != null) {
                                CalendarActivity.this.selectionAnimator.cancel();
                                CalendarActivity.this.selectionAnimator = null;
                            }
                            if (CalendarActivity.this.dateSelectedStart != 0 || CalendarActivity.this.dateSelectedEnd != 0) {
                                if (CalendarActivity.this.dateSelectedStart != dayAtCoord2.date || CalendarActivity.this.dateSelectedEnd != dayAtCoord2.date) {
                                    if (CalendarActivity.this.dateSelectedStart != dayAtCoord2.date) {
                                        if (CalendarActivity.this.dateSelectedEnd != dayAtCoord2.date) {
                                            if (CalendarActivity.this.dateSelectedStart == CalendarActivity.this.dateSelectedEnd) {
                                                if (dayAtCoord2.date > CalendarActivity.this.dateSelectedEnd) {
                                                    CalendarActivity.this.dateSelectedEnd = dayAtCoord2.date;
                                                } else {
                                                    CalendarActivity.this.dateSelectedStart = dayAtCoord2.date;
                                                }
                                            } else {
                                                CalendarActivity calendarActivity = CalendarActivity.this;
                                                calendarActivity.dateSelectedStart = calendarActivity.dateSelectedEnd = dayAtCoord2.date;
                                            }
                                        } else {
                                            CalendarActivity calendarActivity2 = CalendarActivity.this;
                                            calendarActivity2.dateSelectedEnd = calendarActivity2.dateSelectedStart;
                                        }
                                    } else {
                                        CalendarActivity calendarActivity3 = CalendarActivity.this;
                                        calendarActivity3.dateSelectedStart = calendarActivity3.dateSelectedEnd;
                                    }
                                } else {
                                    CalendarActivity calendarActivity4 = CalendarActivity.this;
                                    calendarActivity4.dateSelectedStart = calendarActivity4.dateSelectedEnd = 0;
                                }
                            } else {
                                CalendarActivity calendarActivity5 = CalendarActivity.this;
                                calendarActivity5.dateSelectedStart = calendarActivity5.dateSelectedEnd = dayAtCoord2.date;
                            }
                            CalendarActivity.this.updateTitle();
                            CalendarActivity.this.animateSelection();
                        }
                    } else {
                        PeriodDay dayAtCoord3 = getDayAtCoord(motionEvent.getX(), motionEvent.getY());
                        if (dayAtCoord3 != null && ((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() >= 2) {
                            BaseFragment baseFragment = ((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() - 2);
                            if (baseFragment instanceof ChatActivity) {
                                CalendarActivity.this.finishFragment();
                                ((ChatActivity) baseFragment).jumpToDate(dayAtCoord3.date);
                            }
                        }
                    }
                }
                return false;
            }

            private PeriodDay getDayAtCoord(float f, float f2) {
                PeriodDay periodDay;
                MonthView monthView = MonthView.this;
                if (monthView.messagesByDays == null) {
                    return null;
                }
                int i = monthView.startDayOfWeek;
                float measuredWidth = monthView.getMeasuredWidth() / 7.0f;
                float dp = AndroidUtilities.dp(52.0f);
                int dp2 = AndroidUtilities.dp(44.0f) / 2;
                int i2 = 0;
                for (int i3 = 0; i3 < MonthView.this.daysInMonth; i3++) {
                    float f3 = (i * measuredWidth) + (measuredWidth / 2.0f);
                    float dp3 = (i2 * dp) + (dp / 2.0f) + AndroidUtilities.dp(44.0f);
                    float f4 = dp2;
                    if (f >= f3 - f4 && f <= f3 + f4 && f2 >= dp3 - f4 && f2 <= dp3 + f4 && (periodDay = MonthView.this.messagesByDays.get(i3, null)) != null) {
                        return periodDay;
                    }
                    i++;
                    if (i >= 7) {
                        i2++;
                        i = 0;
                    }
                }
                return null;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                PeriodDay dayAtCoord;
                super.onLongPress(motionEvent);
                if (CalendarActivity.this.calendarType == 0 && (dayAtCoord = getDayAtCoord(motionEvent.getX(), motionEvent.getY())) != null) {
                    MonthView.this.performHapticFeedback(0);
                    Bundle bundle = new Bundle();
                    if (CalendarActivity.this.dialogId > 0) {
                        bundle.putLong("user_id", CalendarActivity.this.dialogId);
                    } else {
                        bundle.putLong("chat_id", -CalendarActivity.this.dialogId);
                    }
                    bundle.putInt("start_from_date", dayAtCoord.date);
                    bundle.putBoolean("need_remove_previous_same_chat_activity", false);
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(CalendarActivity.this.getParentActivity(), 2131166090, CalendarActivity.this.getResourceProvider());
                    actionBarPopupWindowLayout.setBackgroundColor(CalendarActivity.this.getThemedColor("actionBarDefaultSubmenuBackground"));
                    ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(CalendarActivity.this.getParentActivity(), true, false);
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("JumpToDate", 2131626382), 2131165801);
                    actionBarMenuSubItem.setMinimumWidth(160);
                    actionBarMenuSubItem.setOnClickListener(new CalendarActivity$MonthView$2$$ExternalSyntheticLambda2(this, dayAtCoord));
                    actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
                    if (CalendarActivity.this.canClearHistory) {
                        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(CalendarActivity.this.getParentActivity(), false, false);
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("SelectThisDay", 2131628235), 2131165933);
                        actionBarMenuSubItem2.setMinimumWidth(160);
                        actionBarMenuSubItem2.setOnClickListener(new CalendarActivity$MonthView$2$$ExternalSyntheticLambda3(this, dayAtCoord));
                        actionBarPopupWindowLayout.addView(actionBarMenuSubItem2);
                        ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(CalendarActivity.this.getParentActivity(), false, true);
                        actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("ClearHistory", 2131625154), 2131165702);
                        actionBarMenuSubItem3.setMinimumWidth(160);
                        actionBarMenuSubItem3.setOnClickListener(new CalendarActivity$MonthView$2$$ExternalSyntheticLambda0(this));
                        actionBarPopupWindowLayout.addView(actionBarMenuSubItem3);
                    }
                    actionBarPopupWindowLayout.setFitItems(true);
                    CalendarActivity.this.blurredView = new C00112(this.val$context);
                    CalendarActivity.this.blurredView.setOnClickListener(new CalendarActivity$MonthView$2$$ExternalSyntheticLambda1(this));
                    CalendarActivity.this.blurredView.setVisibility(8);
                    CalendarActivity.this.blurredView.setFitsSystemWindows(true);
                    ((BaseFragment) CalendarActivity.this).parentLayout.containerView.addView(CalendarActivity.this.blurredView, LayoutHelper.createFrame(-1, -1.0f));
                    CalendarActivity.this.prepareBlurBitmap();
                    CalendarActivity.this.presentFragmentAsPreviewWithMenu(chatActivity, actionBarPopupWindowLayout);
                }
            }

            public /* synthetic */ void lambda$onLongPress$1(PeriodDay periodDay, View view) {
                if (((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() >= 3) {
                    BaseFragment baseFragment = ((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() - 3);
                    if (baseFragment instanceof ChatActivity) {
                        AndroidUtilities.runOnUIThread(new CalendarActivity$MonthView$2$$ExternalSyntheticLambda4(this, baseFragment, periodDay), 300L);
                    }
                }
                CalendarActivity.this.finishPreviewFragment();
            }

            public /* synthetic */ void lambda$onLongPress$0(BaseFragment baseFragment, PeriodDay periodDay) {
                CalendarActivity.this.finishFragment();
                ((ChatActivity) baseFragment).jumpToDate(periodDay.date);
            }

            public /* synthetic */ void lambda$onLongPress$2(PeriodDay periodDay, View view) {
                CalendarActivity calendarActivity = CalendarActivity.this;
                calendarActivity.dateSelectedStart = calendarActivity.dateSelectedEnd = periodDay.date;
                CalendarActivity.this.inSelectionMode = true;
                CalendarActivity.this.updateTitle();
                CalendarActivity.this.animateSelection();
                CalendarActivity.this.finishPreviewFragment();
            }

            public /* synthetic */ void lambda$onLongPress$3(View view) {
                if (((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() >= 3) {
                    BaseFragment baseFragment = ((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.get(((BaseFragment) CalendarActivity.this).parentLayout.fragmentsStack.size() - 3);
                    if (baseFragment instanceof ChatActivity) {
                        CalendarActivity calendarActivity = CalendarActivity.this;
                        AlertsCreator.createClearDaysDialogAlert(calendarActivity, 1, calendarActivity.getMessagesController().getUser(Long.valueOf(CalendarActivity.this.dialogId)), null, false, new AnonymousClass1(baseFragment), null);
                    }
                }
                CalendarActivity.this.finishPreviewFragment();
            }

            /* renamed from: org.telegram.ui.CalendarActivity$MonthView$2$1 */
            /* loaded from: classes3.dex */
            public class AnonymousClass1 implements MessagesStorage.BooleanCallback {
                final /* synthetic */ BaseFragment val$fragment;

                AnonymousClass1(BaseFragment baseFragment) {
                    AnonymousClass2.this = r1;
                    this.val$fragment = baseFragment;
                }

                @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
                public void run(boolean z) {
                    CalendarActivity.this.finishFragment();
                    ((ChatActivity) this.val$fragment).deleteHistory(CalendarActivity.this.dateSelectedStart, CalendarActivity.this.dateSelectedEnd + 86400, z);
                }
            }

            /* renamed from: org.telegram.ui.CalendarActivity$MonthView$2$2 */
            /* loaded from: classes3.dex */
            class C00112 extends View {
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                C00112(Context context) {
                    super(context);
                    AnonymousClass2.this = r1;
                }

                @Override // android.view.View
                public void setAlpha(float f) {
                    super.setAlpha(f);
                    if (((BaseFragment) CalendarActivity.this).fragmentView != null) {
                        ((BaseFragment) CalendarActivity.this).fragmentView.invalidate();
                    }
                }
            }

            public /* synthetic */ void lambda$onLongPress$4(View view) {
                CalendarActivity.this.finishPreviewFragment();
            }
        }

        public void startSelectionAnimation(int i, int i2) {
            if (this.messagesByDays != null) {
                for (int i3 = 0; i3 < this.daysInMonth; i3++) {
                    PeriodDay periodDay = this.messagesByDays.get(i3, null);
                    if (periodDay != null) {
                        periodDay.fromSelProgress = periodDay.selectProgress;
                        int i4 = periodDay.date;
                        periodDay.toSelProgress = (i4 < i || i4 > i2) ? 0.0f : 1.0f;
                        periodDay.fromSelSEProgress = periodDay.selectStartEndProgress;
                        if (i4 == i || i4 == i2) {
                            periodDay.toSelSEProgress = 1.0f;
                        } else {
                            periodDay.toSelSEProgress = 0.0f;
                        }
                    }
                }
            }
        }

        public void setSelectionValue(float f) {
            if (this.messagesByDays != null) {
                for (int i = 0; i < this.daysInMonth; i++) {
                    PeriodDay periodDay = this.messagesByDays.get(i, null);
                    if (periodDay != null) {
                        float f2 = periodDay.fromSelProgress;
                        periodDay.selectProgress = f2 + ((periodDay.toSelProgress - f2) * f);
                        float f3 = periodDay.fromSelSEProgress;
                        periodDay.selectStartEndProgress = f3 + ((periodDay.toSelSEProgress - f3) * f);
                    }
                }
            }
            invalidate();
        }

        public void dismissRowAnimations(boolean z) {
            for (int i = 0; i < this.rowSelectionPos.size(); i++) {
                animateRow(this.rowSelectionPos.keyAt(i), 0, 0, false, z);
            }
        }

        public void animateRow(int i, int i2, int i3, boolean z, boolean z2) {
            float f;
            float f2;
            float f3;
            ValueAnimator valueAnimator = this.rowAnimators.get(i);
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float measuredWidth = getMeasuredWidth() / 7.0f;
            RowAnimationValue rowAnimationValue = this.rowSelectionPos.get(i);
            if (rowAnimationValue != null) {
                f3 = rowAnimationValue.startX;
                f2 = rowAnimationValue.endX;
                f = rowAnimationValue.alpha;
            } else {
                f3 = (i2 * measuredWidth) + (measuredWidth / 2.0f);
                f2 = f3;
                f = 0.0f;
            }
            float f4 = z ? (i2 * measuredWidth) + (measuredWidth / 2.0f) : f3;
            float f5 = z ? (i3 * measuredWidth) + (measuredWidth / 2.0f) : f2;
            float f6 = z ? 1.0f : 0.0f;
            RowAnimationValue rowAnimationValue2 = new RowAnimationValue(f3, f2);
            this.rowSelectionPos.put(i, rowAnimationValue2);
            if (z2) {
                ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                duration.setInterpolator(Easings.easeInOutQuad);
                duration.addUpdateListener(new CalendarActivity$MonthView$$ExternalSyntheticLambda0(this, rowAnimationValue2, f3, f4, f2, f5, f, f6));
                duration.addListener(new AnonymousClass3(rowAnimationValue2, f4, f5, f6, i, z));
                duration.start();
                this.rowAnimators.put(i, duration);
                return;
            }
            rowAnimationValue2.startX = f4;
            rowAnimationValue2.endX = f5;
            rowAnimationValue2.alpha = f6;
            invalidate();
        }

        public /* synthetic */ void lambda$animateRow$1(RowAnimationValue rowAnimationValue, float f, float f2, float f3, float f4, float f5, float f6, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            rowAnimationValue.startX = f + ((f2 - f) * floatValue);
            rowAnimationValue.endX = f3 + ((f4 - f3) * floatValue);
            rowAnimationValue.alpha = f5 + ((f6 - f5) * floatValue);
            invalidate();
        }

        /* renamed from: org.telegram.ui.CalendarActivity$MonthView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends AnimatorListenerAdapter {
            final /* synthetic */ boolean val$appear;
            final /* synthetic */ float val$cxTo1;
            final /* synthetic */ float val$cxTo2;
            final /* synthetic */ RowAnimationValue val$pr;
            final /* synthetic */ int val$row;
            final /* synthetic */ float val$toAlpha;

            AnonymousClass3(RowAnimationValue rowAnimationValue, float f, float f2, float f3, int i, boolean z) {
                MonthView.this = r1;
                this.val$pr = rowAnimationValue;
                this.val$cxTo1 = f;
                this.val$cxTo2 = f2;
                this.val$toAlpha = f3;
                this.val$row = i;
                this.val$appear = z;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                RowAnimationValue rowAnimationValue = this.val$pr;
                rowAnimationValue.startX = this.val$cxTo1;
                rowAnimationValue.endX = this.val$cxTo2;
                rowAnimationValue.alpha = this.val$toAlpha;
                MonthView.this.invalidate();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MonthView.this.rowAnimators.remove(this.val$row);
                if (!this.val$appear) {
                    MonthView.this.rowSelectionPos.remove(this.val$row);
                }
            }
        }

        @Override // android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.gestureDetector.onTouchEvent(motionEvent);
        }

        public void setDate(int i, int i2, SparseArray<PeriodDay> sparseArray, boolean z) {
            boolean z2 = false;
            boolean z3 = (i == this.currentYear && i2 == this.currentMonthInYear) ? false : true;
            this.currentYear = i;
            this.currentMonthInYear = i2;
            this.messagesByDays = sparseArray;
            if (z3 && this.imagesByDays != null) {
                for (int i3 = 0; i3 < this.imagesByDays.size(); i3++) {
                    this.imagesByDays.valueAt(i3).onDetachedFromWindow();
                    this.imagesByDays.valueAt(i3).setParentView(null);
                }
                this.imagesByDays = null;
            }
            if (sparseArray != null) {
                if (this.imagesByDays == null) {
                    this.imagesByDays = new SparseArray<>();
                }
                int i4 = 0;
                while (i4 < sparseArray.size()) {
                    int keyAt = sparseArray.keyAt(i4);
                    if (this.imagesByDays.get(keyAt, null) == null && sparseArray.get(keyAt).hasImage) {
                        ImageReceiver imageReceiver = new ImageReceiver();
                        imageReceiver.setParentView(this);
                        MessageObject messageObject = sparseArray.get(keyAt).messageObject;
                        if (messageObject != null) {
                            if (messageObject.isVideo()) {
                                TLRPC$Document document = messageObject.getDocument();
                                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                                TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320);
                                if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                                    closestPhotoSizeWithSize2 = null;
                                }
                                if (closestPhotoSizeWithSize != null) {
                                    if (messageObject.strippedThumb != null) {
                                        imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), "44_44", messageObject.strippedThumb, null, messageObject, 0);
                                    } else {
                                        imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), "44_44", ImageLocation.getForDocument(closestPhotoSizeWithSize, document), "b", (String) null, messageObject, 0);
                                    }
                                }
                            } else {
                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && tLRPC$MessageMedia.photo != null && !messageObject.photoThumbs.isEmpty()) {
                                    TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                                    TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 320, z2, closestPhotoSizeWithSize3, z2);
                                    if (messageObject.mediaExists || DownloadController.getInstance(((BaseFragment) CalendarActivity.this).currentAccount).canDownloadMedia(messageObject)) {
                                        if (closestPhotoSizeWithSize4 == closestPhotoSizeWithSize3) {
                                            closestPhotoSizeWithSize3 = null;
                                        }
                                        long j = 0;
                                        if (messageObject.strippedThumb != null) {
                                            ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject);
                                            BitmapDrawable bitmapDrawable = messageObject.strippedThumb;
                                            if (closestPhotoSizeWithSize4 != null) {
                                                j = closestPhotoSizeWithSize4.size;
                                            }
                                            imageReceiver.setImage(forObject, "44_44", null, null, bitmapDrawable, j, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                                        } else {
                                            imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject), "44_44", ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject), "b", closestPhotoSizeWithSize4 != null ? closestPhotoSizeWithSize4.size : 0L, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                                        }
                                    } else {
                                        BitmapDrawable bitmapDrawable2 = messageObject.strippedThumb;
                                        if (bitmapDrawable2 != null) {
                                            imageReceiver.setImage(null, null, bitmapDrawable2, null, messageObject, 0);
                                        } else {
                                            imageReceiver.setImage((ImageLocation) null, (String) null, ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject), "b", (String) null, messageObject, 0);
                                        }
                                    }
                                }
                            }
                            imageReceiver.setRoundRadius(AndroidUtilities.dp(22.0f));
                            this.imagesByDays.put(keyAt, imageReceiver);
                        }
                    }
                    i4++;
                    z2 = false;
                }
            }
            int i5 = i2 + 1;
            this.daysInMonth = YearMonth.of(i, i5).lengthOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, i2, 0);
            this.startDayOfWeek = (calendar.get(7) + 6) % 7;
            this.startMonthTime = (int) (calendar.getTimeInMillis() / 1000);
            int i6 = this.daysInMonth + this.startDayOfWeek;
            this.cellCount = ((int) (i6 / 7.0f)) + (i6 % 7 == 0 ? 0 : 1);
            calendar.set(i, i5, 0);
            this.titleView.setText(LocaleController.formatYearMont(calendar.getTimeInMillis() / 1000, true));
            CalendarActivity.this.updateRowSelections(this, false);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((this.cellCount * 52) + 44), 1073741824));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            float f2;
            int i;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            PeriodDay periodDay;
            super.onDraw(canvas);
            int i2 = this.startDayOfWeek;
            float measuredWidth = getMeasuredWidth() / 7.0f;
            float dp = AndroidUtilities.dp(52.0f);
            float f8 = 44.0f;
            int dp2 = AndroidUtilities.dp(44.0f);
            for (int i3 = 0; i3 < Math.ceil((this.startDayOfWeek + this.daysInMonth) / 7.0f); i3++) {
                float dp3 = (i3 * dp) + (dp / 2.0f) + AndroidUtilities.dp(44.0f);
                RowAnimationValue rowAnimationValue = this.rowSelectionPos.get(i3);
                if (rowAnimationValue != null) {
                    CalendarActivity.this.selectPaint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
                    CalendarActivity.this.selectPaint.setAlpha((int) (rowAnimationValue.alpha * 40.8f));
                    RectF rectF = AndroidUtilities.rectTmp;
                    float f9 = dp2 / 2.0f;
                    rectF.set(rowAnimationValue.startX - f9, dp3 - f9, rowAnimationValue.endX + f9, dp3 + f9);
                    float dp4 = AndroidUtilities.dp(32.0f);
                    canvas.drawRoundRect(rectF, dp4, dp4, CalendarActivity.this.selectPaint);
                }
            }
            int i4 = i2;
            int i5 = 0;
            int i6 = 0;
            while (i6 < this.daysInMonth) {
                float f10 = (i4 * measuredWidth) + (measuredWidth / 2.0f);
                float dp5 = (i5 * dp) + (dp / 2.0f) + AndroidUtilities.dp(f8);
                int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                SparseArray<PeriodDay> sparseArray = this.messagesByDays;
                PeriodDay periodDay2 = null;
                if (sparseArray != null) {
                    periodDay2 = sparseArray.get(i6, null);
                }
                int i7 = i6 + 1;
                if (currentTimeMillis < this.startMonthTime + (i7 * 86400) || (CalendarActivity.this.minDate > 0 && CalendarActivity.this.minDate > this.startMonthTime + ((i6 + 2) * 86400))) {
                    f = measuredWidth;
                    f2 = dp;
                    i = i5;
                    int alpha = CalendarActivity.this.textPaint.getAlpha();
                    CalendarActivity.this.textPaint.setAlpha((int) (alpha * 0.3f));
                    canvas.drawText(Integer.toString(i7), f10, AndroidUtilities.dp(5.0f) + dp5, CalendarActivity.this.textPaint);
                    CalendarActivity.this.textPaint.setAlpha(alpha);
                } else if (periodDay2 != null && periodDay2.hasImage) {
                    if (this.imagesByDays.get(i6) != null) {
                        if (CalendarActivity.this.checkEnterItems && !periodDay2.wasDrawn) {
                            periodDay2.enterAlpha = 0.0f;
                            periodDay2.startEnterDelay = Math.max(0.0f, ((getY() + dp5) / CalendarActivity.this.listView.getMeasuredHeight()) * 150.0f);
                        }
                        float f11 = periodDay2.startEnterDelay;
                        if (f11 > 0.0f) {
                            float f12 = f11 - 16.0f;
                            periodDay2.startEnterDelay = f12;
                            if (f12 < 0.0f) {
                                periodDay2.startEnterDelay = 0.0f;
                            } else {
                                invalidate();
                            }
                        }
                        if (periodDay2.startEnterDelay >= 0.0f) {
                            float f13 = periodDay2.enterAlpha;
                            if (f13 != 1.0f) {
                                float f14 = f13 + 0.07272727f;
                                periodDay2.enterAlpha = f14;
                                if (f14 > 1.0f) {
                                    periodDay2.enterAlpha = 1.0f;
                                } else {
                                    invalidate();
                                }
                            }
                        }
                        f4 = periodDay2.enterAlpha;
                        if (f4 != 1.0f) {
                            canvas.save();
                            float f15 = (0.2f * f4) + 0.8f;
                            canvas.scale(f15, f15, f10, dp5);
                        }
                        int dp6 = (int) (AndroidUtilities.dp(7.0f) * periodDay2.selectProgress);
                        if (periodDay2.selectStartEndProgress >= 0.01f) {
                            CalendarActivity.this.selectPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                            CalendarActivity.this.selectPaint.setAlpha((int) (periodDay2.selectStartEndProgress * 255.0f));
                            canvas.drawCircle(f10, dp5, AndroidUtilities.dp(44.0f) / 2.0f, CalendarActivity.this.selectPaint);
                            CalendarActivity.this.selectOutlinePaint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            f = measuredWidth;
                            rectF2.set(f10 - (AndroidUtilities.dp(44.0f) / 2.0f), dp5 - (AndroidUtilities.dp(44.0f) / 2.0f), (AndroidUtilities.dp(44.0f) / 2.0f) + f10, (AndroidUtilities.dp(44.0f) / 2.0f) + dp5);
                            periodDay = periodDay2;
                            f7 = dp5;
                            i = i5;
                            f2 = dp;
                            f3 = f10;
                            canvas.drawArc(rectF2, -90.0f, 360.0f * periodDay2.selectStartEndProgress, false, CalendarActivity.this.selectOutlinePaint);
                        } else {
                            periodDay = periodDay2;
                            f7 = dp5;
                            f = measuredWidth;
                            f2 = dp;
                            f3 = f10;
                            i = i5;
                        }
                        PeriodDay periodDay3 = periodDay;
                        this.imagesByDays.get(i6).setAlpha(periodDay3.enterAlpha);
                        f5 = f7;
                        this.imagesByDays.get(i6).setImageCoords(f3 - ((AndroidUtilities.dp(44.0f) - dp6) / 2.0f), f5 - ((AndroidUtilities.dp(44.0f) - dp6) / 2.0f), AndroidUtilities.dp(44.0f) - dp6, AndroidUtilities.dp(44.0f) - dp6);
                        this.imagesByDays.get(i6).draw(canvas);
                        CalendarActivity.this.blackoutPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (periodDay3.enterAlpha * 80.0f)));
                        canvas.drawCircle(f3, f5, (AndroidUtilities.dp(44.0f) - dp6) / 2.0f, CalendarActivity.this.blackoutPaint);
                        periodDay3.wasDrawn = true;
                        f6 = 1.0f;
                        if (f4 != 1.0f) {
                            canvas.restore();
                        }
                    } else {
                        f = measuredWidth;
                        f2 = dp;
                        f6 = 1.0f;
                        f3 = f10;
                        i = i5;
                        f5 = dp5;
                        f4 = 1.0f;
                    }
                    if (f4 != f6) {
                        int alpha2 = CalendarActivity.this.textPaint.getAlpha();
                        CalendarActivity.this.textPaint.setAlpha((int) (alpha2 * (f6 - f4)));
                        canvas.drawText(Integer.toString(i7), f3, f5 + AndroidUtilities.dp(5.0f), CalendarActivity.this.textPaint);
                        CalendarActivity.this.textPaint.setAlpha(alpha2);
                        int alpha3 = CalendarActivity.this.textPaint.getAlpha();
                        CalendarActivity.this.activeTextPaint.setAlpha((int) (alpha3 * f4));
                        canvas.drawText(Integer.toString(i7), f3, f5 + AndroidUtilities.dp(5.0f), CalendarActivity.this.activeTextPaint);
                        CalendarActivity.this.activeTextPaint.setAlpha(alpha3);
                    } else {
                        canvas.drawText(Integer.toString(i7), f3, f5 + AndroidUtilities.dp(5.0f), CalendarActivity.this.activeTextPaint);
                    }
                } else {
                    PeriodDay periodDay4 = periodDay2;
                    f = measuredWidth;
                    f2 = dp;
                    i = i5;
                    if (periodDay4 != null && periodDay4.selectStartEndProgress >= 0.01f) {
                        CalendarActivity.this.selectPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                        CalendarActivity.this.selectPaint.setAlpha((int) (periodDay4.selectStartEndProgress * 255.0f));
                        canvas.drawCircle(f10, dp5, AndroidUtilities.dp(44.0f) / 2.0f, CalendarActivity.this.selectPaint);
                        CalendarActivity.this.selectOutlinePaint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
                        RectF rectF3 = AndroidUtilities.rectTmp;
                        rectF3.set(f10 - (AndroidUtilities.dp(44.0f) / 2.0f), dp5 - (AndroidUtilities.dp(44.0f) / 2.0f), (AndroidUtilities.dp(44.0f) / 2.0f) + f10, (AndroidUtilities.dp(44.0f) / 2.0f) + dp5);
                        canvas.drawArc(rectF3, -90.0f, 360.0f * periodDay4.selectStartEndProgress, false, CalendarActivity.this.selectOutlinePaint);
                        CalendarActivity.this.selectPaint.setColor(Theme.getColor("chat_messagePanelVoiceBackground"));
                        CalendarActivity.this.selectPaint.setAlpha((int) (periodDay4.selectStartEndProgress * 255.0f));
                        canvas.drawCircle(f10, dp5, (AndroidUtilities.dp(44.0f) - ((int) (AndroidUtilities.dp(7.0f) * periodDay4.selectStartEndProgress))) / 2.0f, CalendarActivity.this.selectPaint);
                        float f16 = periodDay4.selectStartEndProgress;
                        if (f16 != 1.0f) {
                            int alpha4 = CalendarActivity.this.textPaint.getAlpha();
                            CalendarActivity.this.textPaint.setAlpha((int) (alpha4 * (1.0f - f16)));
                            canvas.drawText(Integer.toString(i7), f10, AndroidUtilities.dp(5.0f) + dp5, CalendarActivity.this.textPaint);
                            CalendarActivity.this.textPaint.setAlpha(alpha4);
                            int alpha5 = CalendarActivity.this.textPaint.getAlpha();
                            CalendarActivity.this.activeTextPaint.setAlpha((int) (alpha5 * f16));
                            canvas.drawText(Integer.toString(i7), f10, AndroidUtilities.dp(5.0f) + dp5, CalendarActivity.this.activeTextPaint);
                            CalendarActivity.this.activeTextPaint.setAlpha(alpha5);
                        } else {
                            canvas.drawText(Integer.toString(i7), f10, AndroidUtilities.dp(5.0f) + dp5, CalendarActivity.this.activeTextPaint);
                        }
                    } else {
                        canvas.drawText(Integer.toString(i7), f10, AndroidUtilities.dp(5.0f) + dp5, CalendarActivity.this.textPaint);
                    }
                }
                i4++;
                if (i4 >= 7) {
                    i5 = i + 1;
                    i4 = 0;
                } else {
                    i5 = i;
                }
                i6 = i7;
                dp = f2;
                measuredWidth = f;
                f8 = 44.0f;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.imagesByDays != null) {
                for (int i = 0; i < this.imagesByDays.size(); i++) {
                    this.imagesByDays.valueAt(i).onAttachedToWindow();
                }
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.imagesByDays != null) {
                for (int i = 0; i < this.imagesByDays.size(); i++) {
                    this.imagesByDays.valueAt(i).onDetachedFromWindow();
                }
            }
        }
    }

    public void updateTitle() {
        String str;
        HintView hintView;
        if (!this.canClearHistory) {
            this.actionBar.setTitle(LocaleController.getString("Calendar", 2131624784));
            this.backDrawable.setRotation(0.0f, true);
            return;
        }
        int i = this.dateSelectedStart;
        int i2 = this.dateSelectedEnd;
        int abs = (i == i2 && i == 0) ? 0 : (Math.abs(i - i2) / 86400) + 1;
        boolean z = this.lastInSelectionMode;
        int i3 = this.lastDaysSelected;
        if (abs == i3 && z == this.inSelectionMode) {
            return;
        }
        boolean z2 = i3 > abs;
        this.lastDaysSelected = abs;
        boolean z3 = this.inSelectionMode;
        this.lastInSelectionMode = z3;
        float f = 1.0f;
        if (abs > 0) {
            str = LocaleController.formatPluralString("Days", abs, new Object[0]);
            this.backDrawable.setRotation(1.0f, true);
        } else if (z3) {
            str = LocaleController.getString("SelectDays", 2131628228);
            this.backDrawable.setRotation(1.0f, true);
        } else {
            str = LocaleController.getString("Calendar", 2131624784);
            this.backDrawable.setRotation(0.0f, true);
        }
        if (abs > 1) {
            this.removeDaysButton.setText(LocaleController.formatString("ClearHistoryForTheseDays", 2131625156, new Object[0]));
        } else if (abs > 0 || this.inSelectionMode) {
            this.removeDaysButton.setText(LocaleController.formatString("ClearHistoryForThisDay", 2131625157, new Object[0]));
        }
        this.actionBar.setTitleAnimated(str, z2, 150L);
        if ((!this.inSelectionMode || abs > 0) && (hintView = this.selectDaysHint) != null) {
            hintView.hide();
        }
        if (abs > 0 || this.inSelectionMode) {
            if (this.removeDaysButton.getVisibility() == 8) {
                this.removeDaysButton.setAlpha(0.0f);
                this.removeDaysButton.setTranslationY(-AndroidUtilities.dp(20.0f));
            }
            this.removeDaysButton.setVisibility(0);
            this.selectDaysButton.animate().setListener(null).cancel();
            this.removeDaysButton.animate().setListener(null).cancel();
            this.selectDaysButton.animate().alpha(0.0f).translationY(AndroidUtilities.dp(20.0f)).setDuration(150L).setListener(new HideViewAfterAnimation(this.selectDaysButton)).start();
            ViewPropertyAnimator animate = this.removeDaysButton.animate();
            if (abs == 0) {
                f = 0.5f;
            }
            animate.alpha(f).translationY(0.0f).start();
            this.selectDaysButton.setEnabled(false);
            this.removeDaysButton.setEnabled(true);
            return;
        }
        if (this.selectDaysButton.getVisibility() == 8) {
            this.selectDaysButton.setAlpha(0.0f);
            this.selectDaysButton.setTranslationY(AndroidUtilities.dp(20.0f));
        }
        this.selectDaysButton.setVisibility(0);
        this.selectDaysButton.animate().setListener(null).cancel();
        this.removeDaysButton.animate().setListener(null).cancel();
        this.selectDaysButton.animate().alpha(1.0f).translationY(0.0f).start();
        this.removeDaysButton.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(20.0f)).setDuration(150L).setListener(new HideViewAfterAnimation(this.removeDaysButton)).start();
        this.selectDaysButton.setEnabled(true);
        this.removeDaysButton.setEnabled(false);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /* loaded from: classes3.dex */
    public class PeriodDay {
        int date;
        float enterAlpha;
        float fromSelProgress;
        float fromSelSEProgress;
        boolean hasImage;
        MessageObject messageObject;
        float selectProgress;
        float selectStartEndProgress;
        float startEnterDelay;
        int startOffset;
        float toSelProgress;
        float toSelSEProgress;
        boolean wasDrawn;

        private PeriodDay(CalendarActivity calendarActivity) {
            this.enterAlpha = 1.0f;
            this.startEnterDelay = 1.0f;
            this.hasImage = true;
        }

        /* synthetic */ PeriodDay(CalendarActivity calendarActivity, AnonymousClass1 anonymousClass1) {
            this(calendarActivity);
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 implements ThemeDescription.ThemeDescriptionDelegate {
        @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
        public /* synthetic */ void onAnimationProgress(float f) {
            ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
        }

        AnonymousClass8() {
            CalendarActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
        public void didSetColor() {
            CalendarActivity.this.updateColors();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        AnonymousClass8 anonymousClass8 = new AnonymousClass8();
        new ArrayList();
        new ThemeDescription(null, 0, null, null, null, anonymousClass8, "windowBackgroundWhite");
        new ThemeDescription(null, 0, null, null, null, anonymousClass8, "windowBackgroundWhiteBlackText");
        new ThemeDescription(null, 0, null, null, null, anonymousClass8, "listSelectorSDK21");
        return super.getThemeDescriptions();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationStart(boolean z, boolean z2) {
        super.onTransitionAnimationStart(z, z2);
        this.isOpened = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        super.onTransitionAnimationProgress(z, f);
        View view = this.blurredView;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        if (z) {
            this.blurredView.setAlpha(1.0f - f);
        } else {
            this.blurredView.setAlpha(f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        View view;
        if (!z || (view = this.blurredView) == null || view.getVisibility() != 0) {
            return;
        }
        this.blurredView.setVisibility(8);
        this.blurredView.setBackground(null);
    }

    public void animateSelection() {
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        duration.addUpdateListener(new CalendarActivity$$ExternalSyntheticLambda0(this));
        duration.addListener(new AnonymousClass9());
        duration.start();
        this.selectionAnimator = duration;
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            updateRowSelections((MonthView) this.listView.getChildAt(i), true);
        }
        for (int i2 = 0; i2 < this.listView.getCachedChildCount(); i2++) {
            MonthView monthView = (MonthView) this.listView.getCachedChildAt(i2);
            updateRowSelections(monthView, false);
            monthView.startSelectionAnimation(this.dateSelectedStart, this.dateSelectedEnd);
            monthView.setSelectionValue(1.0f);
        }
        for (int i3 = 0; i3 < this.listView.getHiddenChildCount(); i3++) {
            MonthView monthView2 = (MonthView) this.listView.getHiddenChildAt(i3);
            updateRowSelections(monthView2, false);
            monthView2.startSelectionAnimation(this.dateSelectedStart, this.dateSelectedEnd);
            monthView2.setSelectionValue(1.0f);
        }
        for (int i4 = 0; i4 < this.listView.getAttachedScrapChildCount(); i4++) {
            MonthView monthView3 = (MonthView) this.listView.getAttachedScrapChildAt(i4);
            updateRowSelections(monthView3, false);
            monthView3.startSelectionAnimation(this.dateSelectedStart, this.dateSelectedEnd);
            monthView3.setSelectionValue(1.0f);
        }
    }

    public /* synthetic */ void lambda$animateSelection$4(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            ((MonthView) this.listView.getChildAt(i)).setSelectionValue(floatValue);
        }
    }

    /* renamed from: org.telegram.ui.CalendarActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        AnonymousClass9() {
            CalendarActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            for (int i = 0; i < CalendarActivity.this.listView.getChildCount(); i++) {
                ((MonthView) CalendarActivity.this.listView.getChildAt(i)).startSelectionAnimation(CalendarActivity.this.dateSelectedStart, CalendarActivity.this.dateSelectedEnd);
            }
        }
    }

    public void updateRowSelections(MonthView monthView, boolean z) {
        int i;
        int i2;
        int i3;
        if (this.dateSelectedStart == 0 || this.dateSelectedEnd == 0) {
            monthView.dismissRowAnimations(z);
        } else if (monthView.messagesByDays != null) {
            if (!z) {
                monthView.dismissRowAnimations(false);
            }
            int i4 = monthView.startDayOfWeek;
            int i5 = 0;
            int i6 = -1;
            int i7 = -1;
            for (int i8 = 0; i8 < monthView.daysInMonth; i8++) {
                PeriodDay periodDay = monthView.messagesByDays.get(i8, null);
                if (periodDay == null || (i3 = periodDay.date) < this.dateSelectedStart || i3 > this.dateSelectedEnd) {
                    i2 = i6;
                    i = i7;
                } else {
                    if (i6 == -1) {
                        i6 = i4;
                    }
                    i = i4;
                    i2 = i6;
                }
                i4++;
                if (i4 >= 7) {
                    if (i2 != -1 && i != -1) {
                        monthView.animateRow(i5, i2, i, true, z);
                    } else {
                        monthView.animateRow(i5, 0, 0, false, z);
                    }
                    i5++;
                    i4 = 0;
                    i6 = -1;
                    i7 = -1;
                } else {
                    i6 = i2;
                    i7 = i;
                }
            }
            if (i6 != -1 && i7 != -1) {
                monthView.animateRow(i5, i6, i7, true, z);
            } else {
                monthView.animateRow(i5, 0, 0, false, z);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class RowAnimationValue {
        float alpha;
        float endX;
        float startX;

        RowAnimationValue(float f, float f2) {
            this.startX = f;
            this.endX = f2;
        }
    }

    public void prepareBlurBitmap() {
        if (this.blurredView == null) {
            return;
        }
        int measuredWidth = (int) (this.parentLayout.getMeasuredWidth() / 6.0f);
        int measuredHeight = (int) (this.parentLayout.getMeasuredHeight() / 6.0f);
        Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.16666667f, 0.16666667f);
        this.parentLayout.draw(canvas);
        Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / 180));
        this.blurredView.setBackground(new BitmapDrawable(createBitmap));
        this.blurredView.setAlpha(0.0f);
        this.blurredView.setVisibility(0);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.inSelectionMode) {
            this.inSelectionMode = false;
            this.dateSelectedEnd = 0;
            this.dateSelectedStart = 0;
            updateTitle();
            animateSelection();
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }
}
