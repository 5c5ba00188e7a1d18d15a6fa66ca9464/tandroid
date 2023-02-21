package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.LiteModeSettingsActivity;
/* loaded from: classes3.dex */
public class LiteModeSettingsActivity extends BaseFragment {
    Adapter adapter;
    FrameLayout contentView;
    private int lastCustomSettings;
    RecyclerListView listView;
    private String[] optionsArray;
    private String[] optionsArrayFull;
    private boolean[] expanded = new boolean[3];
    private ArrayList<Item> oldItems = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private int customIndex = -1;

    /* JADX INFO: Access modifiers changed from: private */
    public int getExpandedIndex(int i) {
        if (i == 3) {
            return 0;
        }
        if (i == 28) {
            return 1;
        }
        return i == 480 ? 2 : -1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("PowerUsage", R.string.PowerUsage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LiteModeSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    LiteModeSettingsActivity.this.finishFragment();
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.contentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView2 = this.listView;
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        recyclerListView2.setAdapter(adapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDurations(350L);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.contentView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LiteModeSettingsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                LiteModeSettingsActivity.this.lambda$createView$0(view, i, f, f2);
            }
        });
        this.fragmentView = this.contentView;
        updateItems();
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0084, code lost:
        r4 = getExpandedIndex(r5.flags);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x008b, code lost:
        if (r4 == (-1)) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x008d, code lost:
        r5 = r3.expanded;
        r5[r4] = !r5[r4];
        updateValues();
        updateItems();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x009a, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$0(View view, int i, float f, float f2) {
        if (view == null || i < 0 || i >= this.items.size()) {
            return;
        }
        Item item = this.items.get(i);
        int i2 = item.viewType;
        if (i2 == 3 || i2 == 4) {
            if (i2 == 3 && item.getFlagsCount() > 1) {
                if (LocaleController.isRTL) {
                }
            }
            LiteMode.toggleFlag(item.flags, !LiteMode.isEnabledSetting(item.flags));
            updateValues();
        } else if (i2 == 5) {
            int i3 = item.type;
            if (i3 != 0) {
                if (i3 == 1) {
                    LiteMode.setPowerSaverEnabled(!LiteMode.isPowerSaverEnabled());
                    ((TextCell) view).setChecked(LiteMode.isPowerSaverEnabled());
                    return;
                }
                return;
            }
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean z = globalMainSettings.getBoolean("view_animations", true);
            SharedPreferences.Editor edit = globalMainSettings.edit();
            edit.putBoolean("view_animations", !z);
            SharedConfig.setAnimationsEnabled(!z);
            edit.commit();
            ((TextCell) view).setChecked(!z);
        }
    }

    private void updateItems() {
        this.oldItems.clear();
        this.oldItems.addAll(this.items);
        this.items.clear();
        this.items.add(Item.asHeader(LocaleController.getString("LitePresetTitle")));
        this.items.add(Item.asSlider());
        this.items.add(Item.asInfo(LocaleController.getString("LitePresetInfo")));
        this.items.add(Item.asHeader(LocaleController.getString("LiteOptionsTitle")));
        this.items.add(Item.asSwitch(R.drawable.msg2_sticker, LocaleController.getString("AnimatedStickers", R.string.AnimatedStickers), 3));
        if (this.expanded[0]) {
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsAutoplayKeyboard"), 1));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsAutoplayChat"), 2));
        }
        this.items.add(Item.asSwitch(R.drawable.msg2_smile_status, LocaleController.getString("PremiumPreviewEmoji", R.string.PremiumPreviewEmoji), 28));
        if (this.expanded[1]) {
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsAutoplayKeyboard"), 4));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsAutoplayReactions"), 8));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsAutoplayChat"), 16));
        }
        this.items.add(Item.asSwitch(R.drawable.msg2_ask_question, LocaleController.getString("LiteOptionsChat"), LiteMode.FLAGS_CHAT));
        if (this.expanded[2]) {
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsBackground"), 32));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsTopics"), 64));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsSpoiler"), 128));
            this.items.add(Item.asCheckbox(LocaleController.getString("LiteOptionsBlur"), LiteMode.FLAG_CHAT_BLUR));
        }
        this.items.add(Item.asSwitch(R.drawable.msg2_call_earpiece, LocaleController.getString("LiteOptionsCalls"), LiteMode.FLAG_CALLS_ANIMATIONS));
        this.items.add(Item.asSwitch(R.drawable.msg2_videocall, LocaleController.getString("LiteOptionsAutoplayVideo"), 1024));
        this.items.add(Item.asSwitch(R.drawable.msg2_gif, LocaleController.getString("LiteOptionsAutoplayGifs"), LiteMode.FLAG_AUTOPLAY_GIFS));
        this.items.add(Item.asInfo(""));
        this.items.add(Item.asSwitch(LocaleController.getString("LiteSmoothTransitions"), 0));
        this.items.add(Item.asInfo(LocaleController.getString("LiteSmoothTransitionsInfo")));
        if (Build.VERSION.SDK_INT >= 21) {
            this.items.add(Item.asSwitch(LocaleController.getString("LitePowerSaver"), 1));
            this.items.add(Item.asInfo(LocaleController.getString("LitePowerSaverInfo")));
        }
        this.adapter.setItems(this.oldItems, this.items);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateValues() {
        int childAdapterPosition;
        if (this.listView == null) {
            return;
        }
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt != null && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt)) >= 0 && childAdapterPosition < this.items.size()) {
                Item item = this.items.get(childAdapterPosition);
                int i2 = item.viewType;
                if (i2 == 3 || i2 == 4) {
                    ((SwitchCell) childAt).update(item);
                } else if (i2 == 1) {
                    updateSlider((SlideChooseView) childAt);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSlider(SlideChooseView slideChooseView) {
        int i;
        if (LiteMode.getValue() == 0) {
            i = 0;
        } else if (LiteMode.getValue() == 3666) {
            i = 1;
        } else {
            i = LiteMode.getValue() == 4095 ? 2 : -1;
        }
        if (i != -1) {
            this.customIndex = -1;
            if (this.optionsArray == null) {
                this.optionsArray = new String[]{LocaleController.getString("AutoDownloadLow", R.string.AutoDownloadLow), LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium), LocaleController.getString("AutoDownloadHigh", R.string.AutoDownloadHigh)};
            }
            slideChooseView.setOptions(i, this.optionsArray);
        } else if (Integer.bitCount(LiteMode.getValue()) <= Integer.bitCount(LiteMode.PRESET_MEDIUM)) {
            this.customIndex = 1;
            this.lastCustomSettings = LiteMode.getValue();
            String[] strArr = this.optionsArrayFull;
            if (strArr == null) {
                this.optionsArrayFull = new String[]{LocaleController.getString("AutoDownloadLow", R.string.AutoDownloadLow), LocaleController.getString("AutoDownloadCustom", R.string.AutoDownloadCustom), LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium), LocaleController.getString("AutoDownloadHigh", R.string.AutoDownloadHigh)};
            } else {
                strArr[1] = LocaleController.getString("AutoDownloadCustom", R.string.AutoDownloadCustom);
                this.optionsArrayFull[2] = LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium);
            }
            slideChooseView.setOptions(1, this.optionsArrayFull);
        } else {
            this.customIndex = 2;
            this.lastCustomSettings = LiteMode.getValue();
            String[] strArr2 = this.optionsArrayFull;
            if (strArr2 == null) {
                this.optionsArrayFull = new String[]{LocaleController.getString("AutoDownloadLow", R.string.AutoDownloadLow), LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium), LocaleController.getString("AutoDownloadCustom", R.string.AutoDownloadCustom), LocaleController.getString("AutoDownloadHigh", R.string.AutoDownloadHigh)};
            } else {
                strArr2[1] = LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium);
                this.optionsArrayFull[2] = LocaleController.getString("AutoDownloadCustom", R.string.AutoDownloadCustom);
            }
            slideChooseView.setOptions(2, this.optionsArrayFull);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends AdapterWithDiffUtils {
        private Adapter() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            Context context = viewGroup.getContext();
            if (i == 0) {
                FrameLayout headerCell = new HeaderCell(context);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                frameLayout = headerCell;
            } else if (i == 1) {
                SlideChooseView slideChooseView = new SlideChooseView(context);
                slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LiteModeSettingsActivity$Adapter$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i2) {
                        LiteModeSettingsActivity.Adapter.this.lambda$onCreateViewHolder$0(i2);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                slideChooseView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                frameLayout = slideChooseView;
            } else if (i == 2) {
                frameLayout = new TextInfoPrivacyCell(context);
            } else if (i == 3 || i == 4) {
                frameLayout = new SwitchCell(context);
            } else if (i == 5) {
                FrameLayout textCell = new TextCell(context, 23, false, true, null);
                textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                frameLayout = textCell;
            } else {
                frameLayout = null;
            }
            return new RecyclerListView.Holder(frameLayout);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(int i) {
            if (i == LiteModeSettingsActivity.this.customIndex) {
                LiteMode.setAllFlags(LiteModeSettingsActivity.this.lastCustomSettings);
                LiteModeSettingsActivity.this.updateValues();
            } else if (i != 0) {
                if (i != 1 || (LiteModeSettingsActivity.this.customIndex >= 0 && LiteModeSettingsActivity.this.customIndex <= 1)) {
                    if (i != 2 || LiteModeSettingsActivity.this.customIndex != 1) {
                        if (i == (LiteModeSettingsActivity.this.customIndex >= 0 ? 3 : 2)) {
                            LiteMode.setAllFlags(LiteMode.PRESET_HIGH);
                            LiteModeSettingsActivity.this.updateValues();
                            return;
                        }
                        return;
                    }
                }
                LiteMode.setAllFlags(LiteMode.PRESET_MEDIUM);
                LiteModeSettingsActivity.this.updateValues();
            } else {
                LiteMode.setAllFlags(0);
                LiteModeSettingsActivity.this.updateValues();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (i < 0 || i >= LiteModeSettingsActivity.this.items.size()) {
                return;
            }
            Item item = (Item) LiteModeSettingsActivity.this.items.get(i);
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((HeaderCell) viewHolder.itemView).setText(item.text);
                return;
            }
            boolean z = true;
            if (itemViewType == 1) {
                LiteModeSettingsActivity.this.updateSlider((SlideChooseView) viewHolder.itemView);
            } else if (itemViewType != 2) {
                if (itemViewType == 3 || itemViewType == 4) {
                    int i2 = i + 1;
                    ((SwitchCell) viewHolder.itemView).set(item, (i2 >= LiteModeSettingsActivity.this.items.size() || ((Item) LiteModeSettingsActivity.this.items.get(i2)).viewType == 2) ? false : false);
                } else if (itemViewType == 5) {
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    int i3 = item.type;
                    if (i3 == 0) {
                        textCell.setTextAndCheck(item.text, MessagesController.getGlobalMainSettings().getBoolean("view_animations", true), false);
                    } else if (i3 == 1) {
                        textCell.setTextAndCheck(item.text, LiteMode.isPowerSaverEnabled(), false);
                    }
                }
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (TextUtils.isEmpty(item.text)) {
                    textInfoPrivacyCell.setFixedSize(12);
                } else {
                    textInfoPrivacyCell.setFixedSize(0);
                }
                textInfoPrivacyCell.setText(item.text);
                boolean z2 = i > 0 && ((Item) LiteModeSettingsActivity.this.items.get(i + (-1))).viewType != 2;
                int i4 = i + 1;
                z = (i4 >= LiteModeSettingsActivity.this.items.size() || ((Item) LiteModeSettingsActivity.this.items.get(i4)).viewType == 2) ? false : false;
                if (z2 && z) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(LiteModeSettingsActivity.this.getContext(), R.drawable.greydivider, "windowBackgroundGrayShadow"));
                } else if (z2) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(LiteModeSettingsActivity.this.getContext(), R.drawable.greydivider_top, "windowBackgroundGrayShadow"));
                } else if (z) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(LiteModeSettingsActivity.this.getContext(), R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                } else {
                    textInfoPrivacyCell.setBackground(null);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < 0 || i >= LiteModeSettingsActivity.this.items.size()) {
                return 2;
            }
            return ((Item) LiteModeSettingsActivity.this.items.get(i)).viewType;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return LiteModeSettingsActivity.this.items.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SwitchCell extends FrameLayout {
        private ImageView arrowView;
        private CheckBox2 checkBoxView;
        private AnimatedTextView countTextView;
        private ImageView imageView;
        private boolean needDivider;
        private boolean needLine;
        private Switch switchView;
        private TextView textView;
        private LinearLayout textViewLayout;

        public SwitchCell(Context context) {
            super(context);
            setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.MULTIPLY));
            this.imageView.setVisibility(8);
            addView(this.imageView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 16, 20.0f, 0.0f, 20.0f, 0.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            AnimatedTextView animatedTextView = new AnimatedTextView(context, false, true, true);
            this.countTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.35f, 0L, 200L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.countTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.countTextView.setTextSize(AndroidUtilities.dp(14.0f));
            this.countTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            ImageView imageView2 = new ImageView(context);
            this.arrowView = imageView2;
            imageView2.setVisibility(8);
            this.arrowView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff.Mode.MULTIPLY));
            this.arrowView.setImageResource(R.drawable.arrow_more);
            LinearLayout linearLayout = new LinearLayout(context);
            this.textViewLayout = linearLayout;
            linearLayout.setOrientation(0);
            this.textViewLayout.addView(this.textView, LayoutHelper.createLinear(-2, -2, 16));
            this.textViewLayout.addView(this.countTextView, LayoutHelper.createLinear(-2, -2, 16, 6, 0, 0, 0));
            this.textViewLayout.addView(this.arrowView, LayoutHelper.createLinear(16, 16, 16, 2, 0, 0, 0));
            addView(this.textViewLayout, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 16, 64.0f, 0.0f, 64.0f, 0.0f));
            Switch r9 = new Switch(context);
            this.switchView = r9;
            r9.setVisibility(8);
            this.switchView.setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
            addView(this.switchView, LayoutHelper.createFrame(37, 50.0f, (LocaleController.isRTL ? 3 : 5) | 16, 19.0f, 0.0f, 19.0f, 0.0f));
            CheckBox2 checkBox2 = new CheckBox2(context, 21);
            this.checkBoxView = checkBox2;
            checkBox2.setColor("radioBackgroundChecked", "checkboxDisabled", "checkboxCheck");
            this.checkBoxView.setDrawUnchecked(true);
            this.checkBoxView.setChecked(true, false);
            this.checkBoxView.setDrawBackgroundAsArc(10);
            this.checkBoxView.setVisibility(8);
            CheckBox2 checkBox22 = this.checkBoxView;
            boolean z = LocaleController.isRTL;
            addView(checkBox22, LayoutHelper.createFrame(21, 21.0f, (z ? 5 : 3) | 16, z ? 0.0f : 64.0f, 0.0f, z ? 64.0f : 0.0f, 0.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        public void set(Item item, boolean z) {
            boolean z2 = true;
            if (item.viewType == 3) {
                this.checkBoxView.setVisibility(8);
                this.imageView.setVisibility(0);
                this.imageView.setImageResource(item.iconResId);
                this.textView.setText(item.text);
                if (item.getFlagsCount() > 1) {
                    this.countTextView.setText(String.format("%d/%d", Integer.valueOf(Integer.bitCount(LiteMode.getValue() & item.flags)), Integer.valueOf(item.getFlagsCount())), false);
                    this.countTextView.setVisibility(0);
                    this.arrowView.setVisibility(0);
                } else {
                    this.countTextView.setVisibility(8);
                    this.arrowView.setVisibility(8);
                }
                this.textView.setTranslationX(0.0f);
                this.switchView.setVisibility(0);
                this.switchView.setChecked(LiteMode.isEnabledSetting(item.flags), false);
                this.needLine = item.getFlagsCount() > 1;
            } else {
                this.checkBoxView.setVisibility(0);
                this.checkBoxView.setChecked(LiteMode.isEnabledSetting(item.flags), false);
                this.imageView.setVisibility(8);
                this.switchView.setVisibility(8);
                this.countTextView.setVisibility(8);
                this.arrowView.setVisibility(8);
                this.textView.setText(item.text);
                this.textView.setTranslationX(AndroidUtilities.dp(41.0f));
                this.needLine = false;
            }
            this.needDivider = z;
            setWillNotDraw((z || this.needLine) ? false : false);
        }

        public void update(Item item) {
            if (item.viewType == 3) {
                if (item.getFlagsCount() > 1) {
                    this.countTextView.setText(String.format("%d/%d", Integer.valueOf(Integer.bitCount(LiteMode.getValue() & item.flags)), Integer.valueOf(item.getFlagsCount())), true);
                    int expandedIndex = LiteModeSettingsActivity.this.getExpandedIndex(item.flags);
                    this.arrowView.clearAnimation();
                    this.arrowView.animate().rotation((expandedIndex < 0 || !LiteModeSettingsActivity.this.expanded[expandedIndex]) ? 0.0f : 180.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(240L).start();
                }
                this.switchView.setChecked(LiteMode.isEnabledSetting(item.flags), true);
                return;
            }
            this.checkBoxView.setChecked(LiteMode.isEnabledSetting(item.flags), true);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (LocaleController.isRTL) {
                return;
            }
            if (this.needLine) {
                float measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(75.0f);
                canvas.drawRect(measuredWidth - AndroidUtilities.dp(0.66f), (getMeasuredHeight() - AndroidUtilities.dp(20.0f)) / 2.0f, measuredWidth, (getMeasuredHeight() + AndroidUtilities.dp(20.0f)) / 2.0f, Theme.dividerPaint);
            }
            if (this.needDivider) {
                canvas.drawLine(AndroidUtilities.dp(64.0f), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Item extends AdapterWithDiffUtils.Item {
        public int flags;
        public int iconResId;
        public CharSequence text;
        public int type;

        private Item(int i, CharSequence charSequence, int i2, int i3, int i4) {
            super(i, false);
            this.text = charSequence;
            this.iconResId = i2;
            this.flags = i3;
            this.type = i4;
        }

        public static Item asHeader(CharSequence charSequence) {
            return new Item(0, charSequence, 0, 0, 0);
        }

        public static Item asSlider() {
            return new Item(1, null, 0, 0, 0);
        }

        public static Item asInfo(CharSequence charSequence) {
            return new Item(2, charSequence, 0, 0, 0);
        }

        public static Item asSwitch(int i, CharSequence charSequence, int i2) {
            return new Item(3, charSequence, i, i2, 0);
        }

        public static Item asCheckbox(CharSequence charSequence, int i) {
            return new Item(4, charSequence, 0, i, 0);
        }

        public static Item asSwitch(CharSequence charSequence, int i) {
            return new Item(5, charSequence, 0, 0, i);
        }

        public int getFlagsCount() {
            return Integer.bitCount(this.flags);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Item) {
                Item item = (Item) obj;
                int i = item.viewType;
                int i2 = this.viewType;
                if (i != i2) {
                    return false;
                }
                if (i2 != 3 || item.iconResId == this.iconResId) {
                    if (i2 != 5 || item.type == this.type) {
                        if ((i2 == 3 || i2 == 4) && item.flags != this.flags) {
                            return false;
                        }
                        return !(i2 == 0 || i2 == 2 || i2 == 3 || i2 == 4 || i2 == 5) || TextUtils.equals(item.text, this.text);
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        LiteMode.savePreference();
        AnimatedEmojiDrawable.updateAll();
        Theme.reloadWallpaper();
    }
}
