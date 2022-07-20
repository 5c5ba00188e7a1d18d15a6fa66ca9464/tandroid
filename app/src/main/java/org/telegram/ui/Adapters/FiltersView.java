package org.telegram.ui.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$MessagesFilter;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterDocument;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterMusic;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotoVideo;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterRoundVoice;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterUrl;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class FiltersView extends RecyclerListView {
    LinearLayoutManager layoutManager;
    public static final MediaFilterData[] filters = {new MediaFilterData(2131166129, LocaleController.getString("SharedMediaTab2", 2131628366), new TLRPC$TL_inputMessagesFilterPhotoVideo(), 0), new MediaFilterData(2131166128, LocaleController.getString("SharedLinksTab2", 2131628363), new TLRPC$TL_inputMessagesFilterUrl(), 2), new MediaFilterData(2131166127, LocaleController.getString("SharedFilesTab2", 2131628359), new TLRPC$TL_inputMessagesFilterDocument(), 1), new MediaFilterData(2131166130, LocaleController.getString("SharedMusicTab2", 2131628368), new TLRPC$TL_inputMessagesFilterMusic(), 3), new MediaFilterData(2131166132, LocaleController.getString("SharedVoiceTab2", 2131628372), new TLRPC$TL_inputMessagesFilterRoundVoice(), 5)};
    private static final Pattern yearPatter = Pattern.compile("20[0-9]{1,2}");
    private static final Pattern monthYearOrDayPatter = Pattern.compile("(\\w{3,}) ([0-9]{0,4})");
    private static final Pattern yearOrDayAndMonthPatter = Pattern.compile("([0-9]{0,4}) (\\w{2,})");
    private static final Pattern shortDate = Pattern.compile("^([0-9]{1,4})(\\.| |/|\\-)([0-9]{1,4})$");
    private static final Pattern longDate = Pattern.compile("^([0-9]{1,2})(\\.| |/|\\-)([0-9]{1,2})(\\.| |/|\\-)([0-9]{1,4})$");
    private static final int[] numberOfDaysEachMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private ArrayList<MediaFilterData> usersFilters = new ArrayList<>();
    private ArrayList<MediaFilterData> oldItems = new ArrayList<>();
    DiffUtil.Callback diffUtilsCallback = new AnonymousClass4();

    /* renamed from: org.telegram.ui.Adapters.FiltersView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            FiltersView.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(recycler, state, accessibilityNodeInfoCompat);
            if (!FiltersView.this.isEnabled()) {
                accessibilityNodeInfoCompat.setVisibleToUser(false);
            }
        }
    }

    public FiltersView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
        this.layoutManager = anonymousClass1;
        anonymousClass1.setOrientation(0);
        setLayoutManager(this.layoutManager);
        setAdapter(new Adapter(this, null));
        addItemDecoration(new AnonymousClass2(this));
        setItemAnimator(new AnonymousClass3(this));
        setWillNotDraw(false);
        setHideIfEmpty(false);
        setSelectorRadius(AndroidUtilities.dp(28.0f));
        setSelectorDrawableColor(getThemedColor("listSelectorSDK21"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Adapters.FiltersView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerView.ItemDecoration {
        AnonymousClass2(FiltersView filtersView) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            rect.left = AndroidUtilities.dp(8.0f);
            if (childAdapterPosition == state.getItemCount() - 1) {
                rect.right = AndroidUtilities.dp(10.0f);
            }
            if (childAdapterPosition == 0) {
                rect.left = AndroidUtilities.dp(10.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Adapters.FiltersView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends DefaultItemAnimator {
        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getAddAnimationDelay(long j, long j2, long j3) {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getAddDuration() {
            return 220L;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getMoveAnimationDelay() {
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public long getMoveDuration() {
            return 220L;
        }

        AnonymousClass3(FiltersView filtersView) {
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
        public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
            boolean animateAdd = super.animateAdd(viewHolder);
            if (animateAdd) {
                viewHolder.itemView.setScaleX(0.0f);
                viewHolder.itemView.setScaleY(0.0f);
            }
            return animateAdd;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        public void animateAddImpl(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            ViewPropertyAnimator animate = view.animate();
            this.mAddAnimations.add(viewHolder);
            animate.alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(getAddDuration()).setListener(new AnonymousClass1(viewHolder, view, animate)).start();
        }

        /* renamed from: org.telegram.ui.Adapters.FiltersView$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ ViewPropertyAnimator val$animation;
            final /* synthetic */ RecyclerView.ViewHolder val$holder;
            final /* synthetic */ View val$view;

            AnonymousClass1(RecyclerView.ViewHolder viewHolder, View view, ViewPropertyAnimator viewPropertyAnimator) {
                AnonymousClass3.this = r1;
                this.val$holder = viewHolder;
                this.val$view = view;
                this.val$animation = viewPropertyAnimator;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                AnonymousClass3.this.dispatchAddStarting(this.val$holder);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.val$view.setAlpha(1.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.val$animation.setListener(null);
                AnonymousClass3.this.dispatchAddFinished(this.val$holder);
                ((DefaultItemAnimator) AnonymousClass3.this).mAddAnimations.remove(this.val$holder);
                AnonymousClass3.this.dispatchFinishedWhenDone();
            }
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected void animateRemoveImpl(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            ViewPropertyAnimator animate = view.animate();
            this.mRemoveAnimations.add(viewHolder);
            animate.setDuration(getRemoveDuration()).alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setListener(new AnonymousClass2(viewHolder, animate, view)).start();
        }

        /* renamed from: org.telegram.ui.Adapters.FiltersView$3$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ ViewPropertyAnimator val$animation;
            final /* synthetic */ RecyclerView.ViewHolder val$holder;
            final /* synthetic */ View val$view;

            AnonymousClass2(RecyclerView.ViewHolder viewHolder, ViewPropertyAnimator viewPropertyAnimator, View view) {
                AnonymousClass3.this = r1;
                this.val$holder = viewHolder;
                this.val$animation = viewPropertyAnimator;
                this.val$view = view;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                AnonymousClass3.this.dispatchRemoveStarting(this.val$holder);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.val$animation.setListener(null);
                this.val$view.setAlpha(1.0f);
                this.val$view.setTranslationX(0.0f);
                this.val$view.setTranslationY(0.0f);
                this.val$view.setScaleX(1.0f);
                this.val$view.setScaleY(1.0f);
                AnonymousClass3.this.dispatchRemoveFinished(this.val$holder);
                ((DefaultItemAnimator) AnonymousClass3.this).mRemoveAnimations.remove(this.val$holder);
                AnonymousClass3.this.dispatchFinishedWhenDone();
            }
        }
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
    }

    public MediaFilterData getFilterAt(int i) {
        if (this.usersFilters.isEmpty()) {
            return filters[i];
        }
        return this.usersFilters.get(i);
    }

    public void setUsersAndDates(ArrayList<Object> arrayList, ArrayList<DateData> arrayList2, boolean z) {
        String str;
        this.oldItems.clear();
        this.oldItems.addAll(this.usersFilters);
        this.usersFilters.clear();
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                Object obj = arrayList.get(i);
                if (obj instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) obj;
                    if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser().id == tLRPC$User.id) {
                        str = LocaleController.getString("SavedMessages", 2131628139);
                    } else {
                        str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name, 10);
                    }
                    MediaFilterData mediaFilterData = new MediaFilterData(2131166131, str, null, 4);
                    mediaFilterData.setUser(tLRPC$User);
                    this.usersFilters.add(mediaFilterData);
                } else if (obj instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
                    String str2 = tLRPC$Chat.title;
                    if (str2.length() > 12) {
                        str2 = String.format("%s...", str2.substring(0, 10));
                    }
                    MediaFilterData mediaFilterData2 = new MediaFilterData(2131166131, str2, null, 4);
                    mediaFilterData2.setUser(tLRPC$Chat);
                    this.usersFilters.add(mediaFilterData2);
                }
            }
        }
        if (arrayList2 != null) {
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                DateData dateData = arrayList2.get(i2);
                MediaFilterData mediaFilterData3 = new MediaFilterData(2131166126, dateData.title, null, 6);
                mediaFilterData3.setDate(dateData);
                this.usersFilters.add(mediaFilterData3);
            }
        }
        if (z) {
            this.usersFilters.add(new MediaFilterData(2131165331, LocaleController.getString("ArchiveSearchFilter", 2131624414), null, 7));
        }
        if (getAdapter() != null) {
            UpdateCallback updateCallback = new UpdateCallback(getAdapter(), null);
            DiffUtil.calculateDiff(this.diffUtilsCallback).dispatchUpdatesTo(updateCallback);
            if (this.usersFilters.isEmpty() || !updateCallback.changed) {
                return;
            }
            this.layoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    public static void fillTipDates(String str, ArrayList<DateData> arrayList) {
        arrayList.clear();
        if (str == null) {
            return;
        }
        String trim = str.trim();
        if (trim.length() < 3) {
            return;
        }
        if (LocaleController.getString("SearchTipToday", 2131628185).toLowerCase().startsWith(trim) || "today".startsWith(trim)) {
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(1);
            int i2 = calendar.get(2);
            int i3 = calendar.get(5);
            calendar.set(i, i2, i3, 0, 0, 0);
            long timeInMillis = calendar.getTimeInMillis();
            calendar.set(i, i2, i3 + 1, 0, 0, 0);
            arrayList.add(new DateData(LocaleController.getString("SearchTipToday", 2131628185), timeInMillis, calendar.getTimeInMillis() - 1, null));
        } else if (LocaleController.getString("SearchTipYesterday", 2131628186).toLowerCase().startsWith(trim) || "yesterday".startsWith(trim)) {
            Calendar calendar2 = Calendar.getInstance();
            int i4 = calendar2.get(1);
            int i5 = calendar2.get(2);
            int i6 = calendar2.get(5);
            calendar2.set(i4, i5, i6, 0, 0, 0);
            calendar2.set(i4, i5, i6 + 1, 0, 0, 0);
            arrayList.add(new DateData(LocaleController.getString("SearchTipYesterday", 2131628186), calendar2.getTimeInMillis() - 86400000, calendar2.getTimeInMillis() - 86400001, null));
        } else {
            int dayOfWeek = getDayOfWeek(trim);
            if (dayOfWeek >= 0) {
                Calendar calendar3 = Calendar.getInstance();
                long timeInMillis2 = calendar3.getTimeInMillis();
                calendar3.set(7, dayOfWeek);
                if (calendar3.getTimeInMillis() > timeInMillis2) {
                    calendar3.setTimeInMillis(calendar3.getTimeInMillis() - 604800000);
                }
                int i7 = calendar3.get(1);
                int i8 = calendar3.get(2);
                int i9 = calendar3.get(5);
                calendar3.set(i7, i8, i9, 0, 0, 0);
                long timeInMillis3 = calendar3.getTimeInMillis();
                calendar3.set(i7, i8, i9 + 1, 0, 0, 0);
                arrayList.add(new DateData(LocaleController.getInstance().formatterWeekLong.format(timeInMillis3), timeInMillis3, calendar3.getTimeInMillis() - 1, null));
                return;
            }
            Matcher matcher = shortDate.matcher(trim);
            if (matcher.matches()) {
                String group = matcher.group(1);
                String group2 = matcher.group(3);
                int parseInt = Integer.parseInt(group);
                int parseInt2 = Integer.parseInt(group2);
                if (parseInt <= 0 || parseInt > 31) {
                    if (parseInt < 2013 || parseInt2 > 12) {
                        return;
                    }
                    createForMonthYear(arrayList, parseInt2 - 1, parseInt);
                    return;
                } else if (parseInt2 >= 2013 && parseInt <= 12) {
                    createForMonthYear(arrayList, parseInt - 1, parseInt2);
                    return;
                } else if (parseInt2 > 12) {
                    return;
                } else {
                    createForDayMonth(arrayList, parseInt - 1, parseInt2 - 1);
                    return;
                }
            }
            Matcher matcher2 = longDate.matcher(trim);
            if (matcher2.matches()) {
                String group3 = matcher2.group(1);
                String group4 = matcher2.group(3);
                String group5 = matcher2.group(5);
                if (!matcher2.group(2).equals(matcher2.group(4))) {
                    return;
                }
                int parseInt3 = Integer.parseInt(group3);
                int parseInt4 = Integer.parseInt(group4) - 1;
                int parseInt5 = Integer.parseInt(group5);
                if (parseInt5 >= 10 && parseInt5 <= 99) {
                    parseInt5 += 2000;
                }
                int i10 = Calendar.getInstance().get(1);
                if (!validDateForMont(parseInt3 - 1, parseInt4) || parseInt5 < 2013 || parseInt5 > i10) {
                    return;
                }
                Calendar calendar4 = Calendar.getInstance();
                int i11 = parseInt5;
                calendar4.set(i11, parseInt4, parseInt3, 0, 0, 0);
                long timeInMillis4 = calendar4.getTimeInMillis();
                calendar4.set(i11, parseInt4, parseInt3 + 1, 0, 0, 0);
                arrayList.add(new DateData(LocaleController.getInstance().formatterYearMax.format(timeInMillis4), timeInMillis4, calendar4.getTimeInMillis() - 1, null));
            } else if (yearPatter.matcher(trim).matches()) {
                int intValue = Integer.valueOf(trim).intValue();
                int i12 = Calendar.getInstance().get(1);
                if (intValue < 2013) {
                    while (i12 >= 2013) {
                        Calendar calendar5 = Calendar.getInstance();
                        calendar5.set(i12, 0, 1, 0, 0, 0);
                        long timeInMillis5 = calendar5.getTimeInMillis();
                        calendar5.set(i12 + 1, 0, 1, 0, 0, 0);
                        arrayList.add(new DateData(Integer.toString(i12), timeInMillis5, calendar5.getTimeInMillis() - 1, null));
                        i12--;
                    }
                } else if (intValue <= i12) {
                    Calendar calendar6 = Calendar.getInstance();
                    calendar6.set(intValue, 0, 1, 0, 0, 0);
                    long timeInMillis6 = calendar6.getTimeInMillis();
                    calendar6.set(intValue + 1, 0, 1, 0, 0, 0);
                    arrayList.add(new DateData(Integer.toString(intValue), timeInMillis6, calendar6.getTimeInMillis() - 1, null));
                }
            } else {
                Matcher matcher3 = monthYearOrDayPatter.matcher(trim);
                if (matcher3.matches()) {
                    String group6 = matcher3.group(1);
                    String group7 = matcher3.group(2);
                    int month = getMonth(group6);
                    if (month >= 0) {
                        int intValue2 = Integer.valueOf(group7).intValue();
                        if (intValue2 > 0 && intValue2 <= 31) {
                            createForDayMonth(arrayList, intValue2 - 1, month);
                            return;
                        } else if (intValue2 >= 2013) {
                            createForMonthYear(arrayList, month, intValue2);
                            return;
                        }
                    }
                }
                Matcher matcher4 = yearOrDayAndMonthPatter.matcher(trim);
                if (matcher4.matches()) {
                    String group8 = matcher4.group(1);
                    int month2 = getMonth(matcher4.group(2));
                    if (month2 >= 0) {
                        int intValue3 = Integer.valueOf(group8).intValue();
                        if (intValue3 > 0 && intValue3 <= 31) {
                            createForDayMonth(arrayList, intValue3 - 1, month2);
                            return;
                        } else if (intValue3 >= 2013) {
                            createForMonthYear(arrayList, month2, intValue3);
                        }
                    }
                }
                if (TextUtils.isEmpty(trim) || trim.length() <= 2) {
                    return;
                }
                int month3 = getMonth(trim);
                long timeInMillis7 = Calendar.getInstance().getTimeInMillis();
                if (month3 < 0) {
                    return;
                }
                for (int i13 = Calendar.getInstance().get(1); i13 >= 2013; i13--) {
                    Calendar calendar7 = Calendar.getInstance();
                    calendar7.set(i13, month3, 1, 0, 0, 0);
                    long timeInMillis8 = calendar7.getTimeInMillis();
                    if (timeInMillis8 <= timeInMillis7) {
                        calendar7.add(2, 1);
                        arrayList.add(new DateData(LocaleController.getInstance().formatterMonthYear.format(timeInMillis8), timeInMillis8, calendar7.getTimeInMillis() - 1, null));
                    }
                }
            }
        }
    }

    private static void createForMonthYear(ArrayList<DateData> arrayList, int i, int i2) {
        int i3 = Calendar.getInstance().get(1);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (i2 < 2013 || i2 > i3) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(i2, i, 1, 0, 0, 0);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (timeInMillis2 > timeInMillis) {
            return;
        }
        calendar.add(2, 1);
        arrayList.add(new DateData(LocaleController.getInstance().formatterMonthYear.format(timeInMillis2), timeInMillis2, calendar.getTimeInMillis() - 1, null));
    }

    private static void createForDayMonth(ArrayList<DateData> arrayList, int i, int i2) {
        long j;
        if (validDateForMont(i, i2)) {
            int i3 = 1;
            int i4 = Calendar.getInstance().get(1);
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            int i5 = i4;
            while (i5 >= 2013) {
                if (i2 != i3 || i != 28 || gregorianCalendar.isLeapYear(i5)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i5, i2, i + 1, 0, 0, 0);
                    long timeInMillis2 = calendar.getTimeInMillis();
                    if (timeInMillis2 <= timeInMillis) {
                        j = timeInMillis;
                        calendar.set(i5, i2, i + 2, 0, 0, 0);
                        long timeInMillis3 = calendar.getTimeInMillis() - 1;
                        if (i5 == i4) {
                            arrayList.add(new DateData(LocaleController.getInstance().formatterDayMonth.format(timeInMillis2), timeInMillis2, timeInMillis3, null));
                        } else {
                            arrayList.add(new DateData(LocaleController.getInstance().formatterYearMax.format(timeInMillis2), timeInMillis2, timeInMillis3, null));
                        }
                        i5--;
                        timeInMillis = j;
                        i3 = 1;
                    }
                }
                j = timeInMillis;
                i5--;
                timeInMillis = j;
                i3 = 1;
            }
        }
    }

    private static boolean validDateForMont(int i, int i2) {
        return i2 >= 0 && i2 < 12 && i >= 0 && i < numberOfDaysEachMonth[i2];
    }

    public static int getDayOfWeek(String str) {
        Calendar calendar = Calendar.getInstance();
        if (str.length() <= 3) {
            return -1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        int i = 0;
        while (i < 7) {
            calendar.set(7, i);
            if (LocaleController.getInstance().formatterWeekLong.format(calendar.getTime()).toLowerCase().startsWith(str) || simpleDateFormat.format(calendar.getTime()).toLowerCase().startsWith(str)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int getMonth(String str) {
        String[] strArr = {LocaleController.getString("January", 2131626358).toLowerCase(), LocaleController.getString("February", 2131625846).toLowerCase(), LocaleController.getString("March", 2131626587).toLowerCase(), LocaleController.getString("April", 2131624400).toLowerCase(), LocaleController.getString("May", 2131626603).toLowerCase(), LocaleController.getString("June", 2131626383).toLowerCase(), LocaleController.getString("July", 2131626381).toLowerCase(), LocaleController.getString("August", 2131624534).toLowerCase(), LocaleController.getString("September", 2131628290).toLowerCase(), LocaleController.getString("October", 2131627128).toLowerCase(), LocaleController.getString("November", 2131627124).toLowerCase(), LocaleController.getString("December", 2131625378).toLowerCase()};
        String[] strArr2 = new String[12];
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= 12; i++) {
            calendar.set(0, 0, 0, 0, 0, 0);
            calendar.set(2, i);
            strArr2[i - 1] = calendar.getDisplayName(2, 2, Locale.ENGLISH).toLowerCase();
        }
        for (int i2 = 0; i2 < 12; i2++) {
            if (strArr2[i2].startsWith(str) || strArr[i2].startsWith(str)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0.0f, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight(), Theme.dividerPaint);
    }

    public void updateColors() {
        getRecycledViewPool().clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof FilterView) {
                ((FilterView) childAt).updateColors();
            }
        }
        for (int i2 = 0; i2 < getCachedChildCount(); i2++) {
            View cachedChildAt = getCachedChildAt(i2);
            if (cachedChildAt instanceof FilterView) {
                ((FilterView) cachedChildAt).updateColors();
            }
        }
        for (int i3 = 0; i3 < getAttachedScrapChildCount(); i3++) {
            View attachedScrapChildAt = getAttachedScrapChildAt(i3);
            if (attachedScrapChildAt instanceof FilterView) {
                ((FilterView) attachedScrapChildAt).updateColors();
            }
        }
        setSelectorDrawableColor(getThemedColor("listSelectorSDK21"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        private Adapter() {
            FiltersView.this = r1;
        }

        /* synthetic */ Adapter(FiltersView filtersView, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ViewHolder viewHolder = new ViewHolder(FiltersView.this, new FilterView(viewGroup.getContext(), ((RecyclerListView) FiltersView.this).resourcesProvider));
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-2, AndroidUtilities.dp(32.0f));
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = AndroidUtilities.dp(6.0f);
            viewHolder.itemView.setLayoutParams(layoutParams);
            return viewHolder;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((ViewHolder) viewHolder).filterView.setData((MediaFilterData) FiltersView.this.usersFilters.get(i));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return FiltersView.this.usersFilters.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Adapters.FiltersView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends DiffUtil.Callback {
        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return true;
        }

        AnonymousClass4() {
            FiltersView.this = r1;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return FiltersView.this.oldItems.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return FiltersView.this.usersFilters.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            MediaFilterData mediaFilterData = (MediaFilterData) FiltersView.this.oldItems.get(i);
            MediaFilterData mediaFilterData2 = (MediaFilterData) FiltersView.this.usersFilters.get(i2);
            if (mediaFilterData.isSameType(mediaFilterData2)) {
                int i3 = mediaFilterData.filterType;
                if (i3 == 4) {
                    TLObject tLObject = mediaFilterData.chat;
                    if (tLObject instanceof TLRPC$User) {
                        TLObject tLObject2 = mediaFilterData2.chat;
                        if (tLObject2 instanceof TLRPC$User) {
                            return ((TLRPC$User) tLObject).id == ((TLRPC$User) tLObject2).id;
                        }
                    }
                    if (tLObject instanceof TLRPC$Chat) {
                        TLObject tLObject3 = mediaFilterData2.chat;
                        return (tLObject3 instanceof TLRPC$Chat) && ((TLRPC$Chat) tLObject).id == ((TLRPC$Chat) tLObject3).id;
                    }
                } else if (i3 == 6) {
                    return mediaFilterData.title.equals(mediaFilterData2.title);
                } else {
                    if (i3 == 7) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public static class FilterView extends FrameLayout {
        BackupImageView avatarImageView;
        MediaFilterData data;
        private final Theme.ResourcesProvider resourcesProvider;
        CombinedDrawable thumbDrawable;
        TextView titleView;

        public FilterView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            addView(backupImageView, LayoutHelper.createFrame(32, 32.0f));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 14.0f);
            addView(this.titleView, LayoutHelper.createFrame(-2, -2.0f, 16, 38.0f, 0.0f, 16.0f, 0.0f));
            updateColors();
        }

        public void updateColors() {
            setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(28.0f), getThemedColor("groupcreate_spanBackground")));
            this.titleView.setTextColor(getThemedColor("windowBackgroundWhiteBlackText"));
            CombinedDrawable combinedDrawable = this.thumbDrawable;
            if (combinedDrawable != null) {
                if (this.data.filterType == 7) {
                    Theme.setCombinedDrawableColor(combinedDrawable, getThemedColor("avatar_backgroundArchived"), false);
                    Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
                    return;
                }
                Theme.setCombinedDrawableColor(combinedDrawable, getThemedColor("avatar_backgroundBlue"), false);
                Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
            }
        }

        public void setData(MediaFilterData mediaFilterData) {
            this.data = mediaFilterData;
            this.avatarImageView.getImageReceiver().clearImage();
            if (mediaFilterData.filterType == 7) {
                CombinedDrawable createCircleDrawableWithIcon = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), 2131165331);
                this.thumbDrawable = createCircleDrawableWithIcon;
                createCircleDrawableWithIcon.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_backgroundArchived"), false);
                Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
                this.avatarImageView.setImageDrawable(this.thumbDrawable);
                this.titleView.setText(mediaFilterData.title);
                return;
            }
            CombinedDrawable createCircleDrawableWithIcon2 = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), mediaFilterData.iconResFilled);
            this.thumbDrawable = createCircleDrawableWithIcon2;
            Theme.setCombinedDrawableColor(createCircleDrawableWithIcon2, getThemedColor("avatar_backgroundBlue"), false);
            Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
            if (mediaFilterData.filterType == 4) {
                TLObject tLObject = mediaFilterData.chat;
                if (tLObject instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                    if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser().id == tLRPC$User.id) {
                        CombinedDrawable createCircleDrawableWithIcon3 = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), 2131165340);
                        createCircleDrawableWithIcon3.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                        Theme.setCombinedDrawableColor(createCircleDrawableWithIcon3, getThemedColor("avatar_backgroundSaved"), false);
                        Theme.setCombinedDrawableColor(createCircleDrawableWithIcon3, getThemedColor("avatar_actionBarIconBlue"), true);
                        this.avatarImageView.setImageDrawable(createCircleDrawableWithIcon3);
                    } else {
                        this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(16.0f));
                        this.avatarImageView.getImageReceiver().setForUserOrChat(tLRPC$User, this.thumbDrawable);
                    }
                } else if (tLObject instanceof TLRPC$Chat) {
                    this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(16.0f));
                    this.avatarImageView.getImageReceiver().setForUserOrChat((TLRPC$Chat) tLObject, this.thumbDrawable);
                }
            } else {
                this.avatarImageView.setImageDrawable(this.thumbDrawable);
            }
            this.titleView.setText(mediaFilterData.title);
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        FilterView filterView;

        public ViewHolder(FiltersView filtersView, FilterView filterView) {
            super(filterView);
            this.filterView = filterView;
        }
    }

    /* loaded from: classes3.dex */
    public static class MediaFilterData {
        public TLObject chat;
        public DateData dateData;
        public final TLRPC$MessagesFilter filter;
        public final int filterType;
        public final int iconResFilled;
        public boolean removable = true;
        public final String title;

        public MediaFilterData(int i, String str, TLRPC$MessagesFilter tLRPC$MessagesFilter, int i2) {
            this.iconResFilled = i;
            this.title = str;
            this.filter = tLRPC$MessagesFilter;
            this.filterType = i2;
        }

        public void setUser(TLObject tLObject) {
            this.chat = tLObject;
        }

        public boolean isSameType(MediaFilterData mediaFilterData) {
            if (this.filterType == mediaFilterData.filterType) {
                return true;
            }
            return isMedia() && mediaFilterData.isMedia();
        }

        public boolean isMedia() {
            int i = this.filterType;
            return i == 0 || i == 1 || i == 2 || i == 3 || i == 5;
        }

        public void setDate(DateData dateData) {
            this.dateData = dateData;
        }
    }

    /* loaded from: classes3.dex */
    public static class DateData {
        public final long maxDate;
        public final long minDate;
        public final String title;

        /* synthetic */ DateData(String str, long j, long j2, AnonymousClass1 anonymousClass1) {
            this(str, j, j2);
        }

        private DateData(String str, long j, long j2) {
            this.title = str;
            this.minDate = j;
            this.maxDate = j2;
        }
    }

    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this, 0, null, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this, 0, null, null, null, null, "key_graySectionText"));
        return arrayList;
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* loaded from: classes3.dex */
    public static class UpdateCallback implements ListUpdateCallback {
        final RecyclerView.Adapter adapter;
        boolean changed;

        /* synthetic */ UpdateCallback(RecyclerView.Adapter adapter, AnonymousClass1 anonymousClass1) {
            this(adapter);
        }

        private UpdateCallback(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onInserted(int i, int i2) {
            this.changed = true;
            this.adapter.notifyItemRangeInserted(i, i2);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onRemoved(int i, int i2) {
            this.changed = true;
            this.adapter.notifyItemRangeRemoved(i, i2);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onMoved(int i, int i2) {
            this.changed = true;
            this.adapter.notifyItemMoved(i, i2);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onChanged(int i, int i2, Object obj) {
            this.adapter.notifyItemRangeChanged(i, i2, obj);
        }
    }
}
