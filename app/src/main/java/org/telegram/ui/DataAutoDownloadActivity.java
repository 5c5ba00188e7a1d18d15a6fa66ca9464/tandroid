package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.MaxFileSizeCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
/* loaded from: classes3.dex */
public class DataAutoDownloadActivity extends BaseFragment {
    private boolean animateChecked;
    private int autoDownloadRow;
    private int autoDownloadSectionRow;
    private int currentPresetNum;
    private int currentType;
    private DownloadController.Preset defaultPreset;
    private int filesRow;
    private String key;
    private String key2;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int photosRow;
    private int rowCount;
    private int typeHeaderRow;
    private DownloadController.Preset typePreset;
    private int typeSectionRow;
    private int usageHeaderRow;
    private int usageProgressRow;
    private int usageSectionRow;
    private int videosRow;
    private boolean wereAnyChanges;
    private ArrayList<DownloadController.Preset> presets = new ArrayList<>();
    private int selectedPreset = 1;
    private DownloadController.Preset lowPreset = DownloadController.getInstance(this.currentAccount).lowPreset;
    private DownloadController.Preset mediumPreset = DownloadController.getInstance(this.currentAccount).mediumPreset;
    private DownloadController.Preset highPreset = DownloadController.getInstance(this.currentAccount).highPreset;

    public DataAutoDownloadActivity(int i) {
        this.currentType = i;
        int i2 = this.currentType;
        if (i2 == 0) {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentMobilePreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).mobilePreset;
            this.defaultPreset = this.mediumPreset;
            this.key = "mobilePreset";
            this.key2 = "currentMobilePreset";
        } else if (i2 == 1) {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentWifiPreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).wifiPreset;
            this.defaultPreset = this.highPreset;
            this.key = "wifiPreset";
            this.key2 = "currentWifiPreset";
        } else {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentRoamingPreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).roamingPreset;
            this.defaultPreset = this.lowPreset;
            this.key = "roamingPreset";
            this.key2 = "currentRoamingPreset";
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        fillPresets();
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        int i = this.currentType;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnMobileData", 2131624591));
        } else if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnWiFiData", 2131624596));
        } else if (i == 2) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnRoamingData", 2131624593));
        }
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        ((FrameLayout) this.fragmentView).addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new DataAutoDownloadActivity$$ExternalSyntheticLambda5(this));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.DataAutoDownloadActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            DataAutoDownloadActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                DataAutoDownloadActivity.this.finishFragment();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$4(View view, int i, float f, float f2) {
        int i2;
        DownloadController.Preset currentRoamingPreset;
        String str;
        String str2;
        boolean z;
        TextCheckCell[] textCheckCellArr;
        DownloadController.Preset preset;
        boolean z2;
        boolean z3;
        MaxFileSizeCell[] maxFileSizeCellArr;
        int i3;
        DownloadController.Preset preset2;
        int i4 = 4;
        int i5 = 0;
        boolean z4 = false;
        if (i == this.autoDownloadRow) {
            int i6 = this.currentPresetNum;
            if (i6 != 3) {
                if (i6 == 0) {
                    this.typePreset.set(this.lowPreset);
                } else if (i6 == 1) {
                    this.typePreset.set(this.mediumPreset);
                } else if (i6 == 2) {
                    this.typePreset.set(this.highPreset);
                }
            }
            TextCheckCell textCheckCell = (TextCheckCell) view;
            boolean isChecked = textCheckCell.isChecked();
            if (!isChecked) {
                DownloadController.Preset preset3 = this.typePreset;
                if (preset3.enabled) {
                    System.arraycopy(this.defaultPreset.mask, 0, preset3.mask, 0, 4);
                    String str3 = "windowBackgroundChecked";
                    view.setTag(!this.typePreset.enabled ? str3 : "windowBackgroundUnchecked");
                    boolean z5 = !isChecked;
                    if (!this.typePreset.enabled) {
                        str3 = "windowBackgroundUnchecked";
                    }
                    textCheckCell.setBackgroundColorAnimated(z5, Theme.getColor(str3));
                    updateRows();
                    if (!this.typePreset.enabled) {
                        this.listAdapter.notifyItemRangeInserted(this.autoDownloadSectionRow + 1, 8);
                    } else {
                        this.listAdapter.notifyItemRangeRemoved(this.autoDownloadSectionRow + 1, 8);
                    }
                    this.listAdapter.notifyItemChanged(this.autoDownloadSectionRow);
                    SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                    edit.putString(this.key, this.typePreset.toString());
                    String str4 = this.key2;
                    this.currentPresetNum = 3;
                    edit.putInt(str4, 3);
                    i3 = this.currentType;
                    if (i3 != 0) {
                        DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
                    } else if (i3 == 1) {
                        DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
                    } else {
                        DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
                    }
                    edit.commit();
                    textCheckCell.setChecked(!isChecked);
                    DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                    this.wereAnyChanges = true;
                }
            }
            this.typePreset.enabled = !preset2.enabled;
            String str32 = "windowBackgroundChecked";
            view.setTag(!this.typePreset.enabled ? str32 : "windowBackgroundUnchecked");
            boolean z52 = !isChecked;
            if (!this.typePreset.enabled) {
            }
            textCheckCell.setBackgroundColorAnimated(z52, Theme.getColor(str32));
            updateRows();
            if (!this.typePreset.enabled) {
            }
            this.listAdapter.notifyItemChanged(this.autoDownloadSectionRow);
            SharedPreferences.Editor edit2 = MessagesController.getMainSettings(this.currentAccount).edit();
            edit2.putString(this.key, this.typePreset.toString());
            String str42 = this.key2;
            this.currentPresetNum = 3;
            edit2.putInt(str42, 3);
            i3 = this.currentType;
            if (i3 != 0) {
            }
            edit2.commit();
            textCheckCell.setChecked(!isChecked);
            DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
            this.wereAnyChanges = true;
        } else if ((i != this.photosRow && i != this.videosRow && i != this.filesRow) || !view.isEnabled()) {
        } else {
            if (i == this.photosRow) {
                i2 = 1;
            } else {
                i2 = i == this.videosRow ? 4 : 8;
            }
            int typeToIndex = DownloadController.typeToIndex(i2);
            int i7 = this.currentType;
            if (i7 == 0) {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentMobilePreset();
                str = "mobilePreset";
                str2 = "currentMobilePreset";
            } else if (i7 == 1) {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentWiFiPreset();
                str = "wifiPreset";
                str2 = "currentWifiPreset";
            } else {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentRoamingPreset();
                str = "roamingPreset";
                str2 = "currentRoamingPreset";
            }
            DownloadController.Preset preset4 = currentRoamingPreset;
            String str5 = str;
            String str6 = str2;
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            boolean isChecked2 = notificationsCheckCell.isChecked();
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                int i8 = this.currentPresetNum;
                if (i8 != 3) {
                    if (i8 == 0) {
                        this.typePreset.set(this.lowPreset);
                    } else if (i8 == 1) {
                        this.typePreset.set(this.mediumPreset);
                    } else if (i8 == 2) {
                        this.typePreset.set(this.highPreset);
                    }
                }
                int i9 = 0;
                while (true) {
                    if (i9 >= this.typePreset.mask.length) {
                        z = false;
                        break;
                    } else if ((preset4.mask[i9] & i2) != 0) {
                        z = true;
                        break;
                    } else {
                        i9++;
                    }
                }
                while (true) {
                    int[] iArr = this.typePreset.mask;
                    if (i5 >= iArr.length) {
                        break;
                    }
                    if (isChecked2) {
                        iArr[i5] = iArr[i5] & (i2 ^ (-1));
                    } else if (!z) {
                        iArr[i5] = iArr[i5] | i2;
                    }
                    i5++;
                }
                SharedPreferences.Editor edit3 = MessagesController.getMainSettings(this.currentAccount).edit();
                edit3.putString(str5, this.typePreset.toString());
                this.currentPresetNum = 3;
                edit3.putInt(str6, 3);
                int i10 = this.currentType;
                if (i10 == 0) {
                    DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
                } else if (i10 == 1) {
                    DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
                } else {
                    DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
                }
                edit3.commit();
                notificationsCheckCell.setChecked(!isChecked2);
                RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
                if (findContainingViewHolder != null) {
                    this.listAdapter.onBindViewHolder(findContainingViewHolder, i);
                }
                DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                this.wereAnyChanges = true;
                fillPresets();
            } else if (getParentActivity() == null) {
            } else {
                BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
                builder.setApplyTopPadding(false);
                builder.setApplyBottomPadding(false);
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                builder.setCustomView(linearLayout);
                HeaderCell headerCell = new HeaderCell(getParentActivity(), "dialogTextBlue2", 21, 15, false);
                if (i == this.photosRow) {
                    headerCell.setText(LocaleController.getString("AutoDownloadPhotosTitle", 2131624600));
                } else if (i == this.videosRow) {
                    headerCell.setText(LocaleController.getString("AutoDownloadVideosTitle", 2131624610));
                } else {
                    headerCell.setText(LocaleController.getString("AutoDownloadFilesTitle", 2131624580));
                }
                linearLayout.addView(headerCell, LayoutHelper.createFrame(-1, -2.0f));
                MaxFileSizeCell[] maxFileSizeCellArr2 = new MaxFileSizeCell[1];
                TextCheckCell[] textCheckCellArr2 = new TextCheckCell[1];
                AnimatorSet[] animatorSetArr = new AnimatorSet[1];
                TextCheckBoxCell[] textCheckBoxCellArr = new TextCheckBoxCell[4];
                int i11 = 0;
                while (i11 < i4) {
                    TextCheckCell[] textCheckCellArr3 = textCheckCellArr2;
                    TextCheckBoxCell textCheckBoxCell = new TextCheckBoxCell(getParentActivity(), true, z4);
                    textCheckBoxCellArr[i11] = textCheckBoxCell;
                    if (i11 == 0) {
                        maxFileSizeCellArr = maxFileSizeCellArr2;
                        textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadContacts", 2131624632), (preset4.mask[0] & i2) != 0, true);
                    } else {
                        maxFileSizeCellArr = maxFileSizeCellArr2;
                        if (i11 == 1) {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadPrivateChats", 2131624634), (preset4.mask[1] & i2) != 0, true);
                        } else if (i11 == 2) {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadGroupChats", 2131624633), (preset4.mask[2] & i2) != 0, true);
                        } else {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadChannels", 2131624631), (preset4.mask[3] & i2) != 0, i != this.photosRow);
                        }
                    }
                    textCheckBoxCellArr[i11].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    MaxFileSizeCell[] maxFileSizeCellArr3 = maxFileSizeCellArr;
                    LinearLayout linearLayout2 = linearLayout;
                    textCheckBoxCellArr[i11].setOnClickListener(new DataAutoDownloadActivity$$ExternalSyntheticLambda1(this, textCheckBoxCell, textCheckBoxCellArr, i, maxFileSizeCellArr3, textCheckCellArr3, animatorSetArr));
                    linearLayout2.addView(textCheckBoxCellArr[i11], LayoutHelper.createFrame(-1, 50.0f));
                    i11++;
                    linearLayout = linearLayout2;
                    maxFileSizeCellArr2 = maxFileSizeCellArr3;
                    textCheckCellArr2 = textCheckCellArr3;
                    builder = builder;
                    str6 = str6;
                    str5 = str5;
                    preset4 = preset4;
                    i4 = 4;
                    z4 = false;
                }
                TextCheckCell[] textCheckCellArr4 = textCheckCellArr2;
                MaxFileSizeCell[] maxFileSizeCellArr4 = maxFileSizeCellArr2;
                BottomSheet.Builder builder2 = builder;
                String str7 = str6;
                String str8 = str5;
                DownloadController.Preset preset5 = preset4;
                LinearLayout linearLayout3 = linearLayout;
                if (i != this.photosRow) {
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(getParentActivity());
                    maxFileSizeCellArr4[0] = new AnonymousClass3(getParentActivity(), i, textInfoPrivacyCell, textCheckCellArr4, animatorSetArr);
                    preset = preset5;
                    maxFileSizeCellArr4[0].setSize(preset.sizes[typeToIndex]);
                    linearLayout3.addView(maxFileSizeCellArr4[0], LayoutHelper.createLinear(-1, 50));
                    textCheckCellArr = textCheckCellArr4;
                    textCheckCellArr[0] = new TextCheckCell(getParentActivity(), 21, true);
                    linearLayout3.addView(textCheckCellArr[0], LayoutHelper.createLinear(-1, 48));
                    textCheckCellArr[0].setOnClickListener(new DataAutoDownloadActivity$$ExternalSyntheticLambda3(textCheckCellArr));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), Theme.getThemedDrawable(getParentActivity(), 2131165435, "windowBackgroundGrayShadow"));
                    combinedDrawable.setFullsize(true);
                    textInfoPrivacyCell.setBackgroundDrawable(combinedDrawable);
                    linearLayout3.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
                    if (i == this.videosRow) {
                        maxFileSizeCellArr4[0].setText(LocaleController.getString("AutoDownloadMaxVideoSize", 2131624585));
                        textCheckCellArr[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadVideo", 2131624604), preset.preloadVideo, false);
                        textInfoPrivacyCell.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", 2131624605, AndroidUtilities.formatFileSize(preset.sizes[typeToIndex])));
                    } else {
                        maxFileSizeCellArr4[0].setText(LocaleController.getString("AutoDownloadMaxFileSize", 2131624584));
                        textCheckCellArr[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadMusic", 2131624602), preset.preloadMusic, false);
                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadPreloadMusicInfo", 2131624603));
                    }
                } else {
                    textCheckCellArr = textCheckCellArr4;
                    preset = preset5;
                    maxFileSizeCellArr4[0] = null;
                    textCheckCellArr[0] = null;
                    View view2 = new View(getParentActivity());
                    view2.setBackgroundColor(Theme.getColor("divider"));
                    linearLayout3.addView(view2, new LinearLayout.LayoutParams(-1, 1));
                }
                if (i == this.videosRow) {
                    int i12 = 0;
                    while (true) {
                        if (i12 >= 4) {
                            z2 = false;
                            break;
                        } else if (textCheckBoxCellArr[i12].isChecked()) {
                            z2 = true;
                            break;
                        } else {
                            i12++;
                        }
                    }
                    if (!z2) {
                        z3 = false;
                        maxFileSizeCellArr4[0].setEnabled(false, null);
                        textCheckCellArr[0].setEnabled(false, null);
                    } else {
                        z3 = false;
                    }
                    if (preset.sizes[typeToIndex] <= 2097152) {
                        char c = z3 ? 1 : 0;
                        char c2 = z3 ? 1 : 0;
                        textCheckCellArr[c].setEnabled(z3, null);
                    }
                }
                FrameLayout frameLayout = new FrameLayout(getParentActivity());
                frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                linearLayout3.addView(frameLayout, LayoutHelper.createLinear(-1, 52));
                TextView textView = new TextView(getParentActivity());
                textView.setTextSize(1, 14.0f);
                textView.setTextColor(Theme.getColor("dialogTextBlue2"));
                textView.setGravity(17);
                textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView.setText(LocaleController.getString("Cancel", 2131624832).toUpperCase());
                textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                frameLayout.addView(textView, LayoutHelper.createFrame(-2, 36, 51));
                textView.setOnClickListener(new DataAutoDownloadActivity$$ExternalSyntheticLambda0(builder2));
                TextView textView2 = new TextView(getParentActivity());
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(Theme.getColor("dialogTextBlue2"));
                textView2.setGravity(17);
                textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView2.setText(LocaleController.getString("Save", 2131628123).toUpperCase());
                textView2.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                frameLayout.addView(textView2, LayoutHelper.createFrame(-2, 36, 53));
                textView2.setOnClickListener(new DataAutoDownloadActivity$$ExternalSyntheticLambda2(this, textCheckBoxCellArr, i2, maxFileSizeCellArr4, typeToIndex, textCheckCellArr, i, str8, str7, builder2, view));
                showDialog(builder2.create());
            }
        }
    }

    public /* synthetic */ void lambda$createView$0(TextCheckBoxCell textCheckBoxCell, TextCheckBoxCell[] textCheckBoxCellArr, int i, MaxFileSizeCell[] maxFileSizeCellArr, TextCheckCell[] textCheckCellArr, AnimatorSet[] animatorSetArr, View view) {
        if (!view.isEnabled()) {
            return;
        }
        boolean z = true;
        textCheckBoxCell.setChecked(!textCheckBoxCell.isChecked());
        int i2 = 0;
        while (true) {
            if (i2 >= textCheckBoxCellArr.length) {
                z = false;
                break;
            } else if (textCheckBoxCellArr[i2].isChecked()) {
                break;
            } else {
                i2++;
            }
        }
        if (i != this.videosRow || maxFileSizeCellArr[0].isEnabled() == z) {
            return;
        }
        ArrayList<Animator> arrayList = new ArrayList<>();
        maxFileSizeCellArr[0].setEnabled(z, arrayList);
        if (maxFileSizeCellArr[0].getSize() > 2097152) {
            textCheckCellArr[0].setEnabled(z, arrayList);
        }
        if (animatorSetArr[0] != null) {
            animatorSetArr[0].cancel();
            animatorSetArr[0] = null;
        }
        animatorSetArr[0] = new AnimatorSet();
        animatorSetArr[0].playTogether(arrayList);
        animatorSetArr[0].addListener(new AnonymousClass2(this, animatorSetArr));
        animatorSetArr[0].setDuration(150L);
        animatorSetArr[0].start();
    }

    /* renamed from: org.telegram.ui.DataAutoDownloadActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ AnimatorSet[] val$animatorSet;

        AnonymousClass2(DataAutoDownloadActivity dataAutoDownloadActivity, AnimatorSet[] animatorSetArr) {
            this.val$animatorSet = animatorSetArr;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(this.val$animatorSet[0])) {
                this.val$animatorSet[0] = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.DataAutoDownloadActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends MaxFileSizeCell {
        final /* synthetic */ AnimatorSet[] val$animatorSet;
        final /* synthetic */ TextCheckCell[] val$checkCell;
        final /* synthetic */ TextInfoPrivacyCell val$infoCell;
        final /* synthetic */ int val$position;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, int i, TextInfoPrivacyCell textInfoPrivacyCell, TextCheckCell[] textCheckCellArr, AnimatorSet[] animatorSetArr) {
            super(context);
            DataAutoDownloadActivity.this = r1;
            this.val$position = i;
            this.val$infoCell = textInfoPrivacyCell;
            this.val$checkCell = textCheckCellArr;
            this.val$animatorSet = animatorSetArr;
        }

        @Override // org.telegram.ui.Cells.MaxFileSizeCell
        protected void didChangedSizeValue(int i) {
            if (this.val$position == DataAutoDownloadActivity.this.videosRow) {
                boolean z = true;
                this.val$infoCell.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", 2131624605, AndroidUtilities.formatFileSize(i)));
                if (i <= 2097152) {
                    z = false;
                }
                if (z == this.val$checkCell[0].isEnabled()) {
                    return;
                }
                ArrayList<Animator> arrayList = new ArrayList<>();
                this.val$checkCell[0].setEnabled(z, arrayList);
                AnimatorSet[] animatorSetArr = this.val$animatorSet;
                if (animatorSetArr[0] != null) {
                    animatorSetArr[0].cancel();
                    this.val$animatorSet[0] = null;
                }
                this.val$animatorSet[0] = new AnimatorSet();
                this.val$animatorSet[0].playTogether(arrayList);
                this.val$animatorSet[0].addListener(new AnonymousClass1());
                this.val$animatorSet[0].setDuration(150L);
                this.val$animatorSet[0].start();
            }
        }

        /* renamed from: org.telegram.ui.DataAutoDownloadActivity$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                AnonymousClass3.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(AnonymousClass3.this.val$animatorSet[0])) {
                    AnonymousClass3.this.val$animatorSet[0] = null;
                }
            }
        }
    }

    public static /* synthetic */ void lambda$createView$1(TextCheckCell[] textCheckCellArr, View view) {
        textCheckCellArr[0].setChecked(!textCheckCellArr[0].isChecked());
    }

    public static /* synthetic */ void lambda$createView$2(BottomSheet.Builder builder, View view) {
        builder.getDismissRunnable().run();
    }

    public /* synthetic */ void lambda$createView$3(TextCheckBoxCell[] textCheckBoxCellArr, int i, MaxFileSizeCell[] maxFileSizeCellArr, int i2, TextCheckCell[] textCheckCellArr, int i3, String str, String str2, BottomSheet.Builder builder, View view, View view2) {
        int i4 = this.currentPresetNum;
        if (i4 != 3) {
            if (i4 == 0) {
                this.typePreset.set(this.lowPreset);
            } else if (i4 == 1) {
                this.typePreset.set(this.mediumPreset);
            } else if (i4 == 2) {
                this.typePreset.set(this.highPreset);
            }
        }
        for (int i5 = 0; i5 < 4; i5++) {
            if (textCheckBoxCellArr[i5].isChecked()) {
                int[] iArr = this.typePreset.mask;
                iArr[i5] = iArr[i5] | i;
            } else {
                int[] iArr2 = this.typePreset.mask;
                iArr2[i5] = iArr2[i5] & (i ^ (-1));
            }
        }
        if (maxFileSizeCellArr[0] != null) {
            maxFileSizeCellArr[0].getSize();
            this.typePreset.sizes[i2] = (int) maxFileSizeCellArr[0].getSize();
        }
        if (textCheckCellArr[0] != null) {
            if (i3 == this.videosRow) {
                this.typePreset.preloadVideo = textCheckCellArr[0].isChecked();
            } else {
                this.typePreset.preloadMusic = textCheckCellArr[0].isChecked();
            }
        }
        SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        edit.putString(str, this.typePreset.toString());
        this.currentPresetNum = 3;
        edit.putInt(str2, 3);
        int i6 = this.currentType;
        if (i6 == 0) {
            DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
        } else if (i6 == 1) {
            DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
        } else {
            DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
        }
        edit.commit();
        builder.getDismissRunnable().run();
        RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
        if (findContainingViewHolder != null) {
            this.animateChecked = true;
            this.listAdapter.onBindViewHolder(findContainingViewHolder, i3);
            this.animateChecked = false;
        }
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
        this.wereAnyChanges = true;
        fillPresets();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.wereAnyChanges) {
            DownloadController.getInstance(this.currentAccount).savePresetToServer(this.currentType);
            this.wereAnyChanges = false;
        }
    }

    private void fillPresets() {
        this.presets.clear();
        this.presets.add(this.lowPreset);
        this.presets.add(this.mediumPreset);
        this.presets.add(this.highPreset);
        if (!this.typePreset.equals(this.lowPreset) && !this.typePreset.equals(this.mediumPreset) && !this.typePreset.equals(this.highPreset)) {
            this.presets.add(this.typePreset);
        }
        Collections.sort(this.presets, DataAutoDownloadActivity$$ExternalSyntheticLambda4.INSTANCE);
        int i = this.currentPresetNum;
        if (i == 0 || (i == 3 && this.typePreset.equals(this.lowPreset))) {
            this.selectedPreset = this.presets.indexOf(this.lowPreset);
        } else {
            int i2 = this.currentPresetNum;
            if (i2 == 1 || (i2 == 3 && this.typePreset.equals(this.mediumPreset))) {
                this.selectedPreset = this.presets.indexOf(this.mediumPreset);
            } else {
                int i3 = this.currentPresetNum;
                if (i3 == 2 || (i3 == 3 && this.typePreset.equals(this.highPreset))) {
                    this.selectedPreset = this.presets.indexOf(this.highPreset);
                } else {
                    this.selectedPreset = this.presets.indexOf(this.typePreset);
                }
            }
        }
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerListView.findViewHolderForAdapterPosition(this.usageProgressRow);
            if (findViewHolderForAdapterPosition != null) {
                View view = findViewHolderForAdapterPosition.itemView;
                if (view instanceof SlideChooseView) {
                    updatePresetChoseView((SlideChooseView) view);
                    return;
                }
            }
            this.listAdapter.notifyItemChanged(this.usageProgressRow);
        }
    }

    public static /* synthetic */ int lambda$fillPresets$5(DownloadController.Preset preset, DownloadController.Preset preset2) {
        int typeToIndex = DownloadController.typeToIndex(4);
        int typeToIndex2 = DownloadController.typeToIndex(8);
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (true) {
            int[] iArr = preset.mask;
            if (i < iArr.length) {
                if ((iArr[i] & 4) != 0) {
                    z = true;
                }
                if ((iArr[i] & 8) != 0) {
                    z2 = true;
                }
                if (z && z2) {
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        int i2 = 0;
        boolean z3 = false;
        boolean z4 = false;
        while (true) {
            int[] iArr2 = preset2.mask;
            if (i2 < iArr2.length) {
                if ((iArr2[i2] & 4) != 0) {
                    z3 = true;
                }
                if ((iArr2[i2] & 8) != 0) {
                    z4 = true;
                }
                if (z3 && z4) {
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        long j = 0;
        long j2 = (z ? preset.sizes[typeToIndex] : 0L) + (z2 ? preset.sizes[typeToIndex2] : 0L);
        long j3 = z3 ? preset2.sizes[typeToIndex] : 0L;
        if (z4) {
            j = preset2.sizes[typeToIndex2];
        }
        long j4 = j3 + j;
        if (j2 > j4) {
            return 1;
        }
        return j2 < j4 ? -1 : 0;
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.autoDownloadRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.autoDownloadSectionRow = i;
        if (this.typePreset.enabled) {
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.usageHeaderRow = i2;
            int i4 = i3 + 1;
            this.rowCount = i4;
            this.usageProgressRow = i3;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.usageSectionRow = i4;
            int i6 = i5 + 1;
            this.rowCount = i6;
            this.typeHeaderRow = i5;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.photosRow = i6;
            int i8 = i7 + 1;
            this.rowCount = i8;
            this.videosRow = i7;
            int i9 = i8 + 1;
            this.rowCount = i9;
            this.filesRow = i8;
            this.rowCount = i9 + 1;
            this.typeSectionRow = i9;
            return;
        }
        this.usageHeaderRow = -1;
        this.usageProgressRow = -1;
        this.usageSectionRow = -1;
        this.typeHeaderRow = -1;
        this.photosRow = -1;
        this.videosRow = -1;
        this.filesRow = -1;
        this.typeSectionRow = -1;
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            DataAutoDownloadActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DataAutoDownloadActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            int i2;
            StringBuilder sb;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.autoDownloadRow) {
                    return;
                }
                textCheckCell.setDrawCheckRipple(true);
                textCheckCell.setTextAndCheck(LocaleController.getString("AutoDownloadMedia", 2131624586), DataAutoDownloadActivity.this.typePreset.enabled, false);
                String str2 = "windowBackgroundChecked";
                textCheckCell.setTag(DataAutoDownloadActivity.this.typePreset.enabled ? str2 : "windowBackgroundUnchecked");
                if (!DataAutoDownloadActivity.this.typePreset.enabled) {
                    str2 = "windowBackgroundUnchecked";
                }
                textCheckCell.setBackgroundColor(Theme.getColor(str2));
            } else if (itemViewType == 2) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.usageHeaderRow) {
                    if (i != DataAutoDownloadActivity.this.typeHeaderRow) {
                        return;
                    }
                    headerCell.setText(LocaleController.getString("AutoDownloadTypes", 2131624606));
                    return;
                }
                headerCell.setText(LocaleController.getString("AutoDownloadDataUsage", 2131624577));
            } else if (itemViewType == 3) {
                DataAutoDownloadActivity.this.updatePresetChoseView((SlideChooseView) viewHolder.itemView);
            } else if (itemViewType != 4) {
                if (itemViewType != 5) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.typeSectionRow) {
                    if (i != DataAutoDownloadActivity.this.autoDownloadSectionRow) {
                        return;
                    }
                    if (DataAutoDownloadActivity.this.usageHeaderRow == -1) {
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        if (DataAutoDownloadActivity.this.currentType != 0) {
                            if (DataAutoDownloadActivity.this.currentType != 1) {
                                if (DataAutoDownloadActivity.this.currentType == 2) {
                                    textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnRoamingDataInfo", 2131624594));
                                }
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnWiFiDataInfo", 2131624597));
                            }
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnMobileDataInfo", 2131624592));
                        }
                        textInfoPrivacyCell.setImportantForAccessibility(1);
                        return;
                    }
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    textInfoPrivacyCell.setText(null);
                    textInfoPrivacyCell.setFixedSize(12);
                    if (Build.VERSION.SDK_INT >= 19) {
                        textInfoPrivacyCell.setImportantForAccessibility(4);
                        return;
                    } else {
                        textInfoPrivacyCell.setImportantForAccessibility(2);
                        return;
                    }
                }
                textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadAudioInfo", 2131624573));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                textInfoPrivacyCell.setFixedSize(0);
                textInfoPrivacyCell.setImportantForAccessibility(1);
            } else {
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.photosRow) {
                    if (i == DataAutoDownloadActivity.this.videosRow) {
                        str = LocaleController.getString("AutoDownloadVideos", 2131624608);
                        i2 = 4;
                    } else {
                        i2 = 8;
                        str = LocaleController.getString("AutoDownloadFiles", 2131624578);
                    }
                } else {
                    str = LocaleController.getString("AutoDownloadPhotos", 2131624598);
                    i2 = 1;
                }
                DownloadController.Preset currentMobilePreset = DataAutoDownloadActivity.this.currentType == 0 ? DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentMobilePreset() : DataAutoDownloadActivity.this.currentType == 1 ? DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentWiFiPreset() : DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentRoamingPreset();
                long j = currentMobilePreset.sizes[DownloadController.typeToIndex(i2)];
                StringBuilder sb2 = new StringBuilder();
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    int[] iArr = currentMobilePreset.mask;
                    if (i3 >= iArr.length) {
                        break;
                    }
                    if ((iArr[i3] & i2) != 0) {
                        if (sb2.length() != 0) {
                            sb2.append(", ");
                        }
                        if (i3 == 0) {
                            sb2.append(LocaleController.getString("AutoDownloadContacts", 2131624575));
                        } else if (i3 == 1) {
                            sb2.append(LocaleController.getString("AutoDownloadPm", 2131624601));
                        } else if (i3 == 2) {
                            sb2.append(LocaleController.getString("AutoDownloadGroups", 2131624581));
                        } else if (i3 == 3) {
                            sb2.append(LocaleController.getString("AutoDownloadChannels", 2131624574));
                        }
                        i4++;
                    }
                    i3++;
                }
                if (i4 == 4) {
                    sb2.setLength(0);
                    if (i == DataAutoDownloadActivity.this.photosRow) {
                        sb2.append(LocaleController.getString("AutoDownloadOnAllChats", 2131624589));
                    } else {
                        sb2.append(LocaleController.formatString("AutoDownloadUpToOnAllChats", 2131624607, AndroidUtilities.formatFileSize(j)));
                    }
                } else if (i4 != 0) {
                    if (i == DataAutoDownloadActivity.this.photosRow) {
                        sb = new StringBuilder(LocaleController.formatString("AutoDownloadOnFor", 2131624590, sb2.toString()));
                    } else {
                        sb = new StringBuilder(LocaleController.formatString("AutoDownloadOnUpToFor", 2131624595, AndroidUtilities.formatFileSize(j), sb2.toString()));
                    }
                    sb2 = sb;
                } else {
                    sb2.append(LocaleController.getString("AutoDownloadOff", 2131624588));
                }
                if (DataAutoDownloadActivity.this.animateChecked) {
                    notificationsCheckCell.setChecked(i4 != 0);
                }
                notificationsCheckCell.setTextAndValueAndCheck(str, sb2, i4 != 0, 0, true, i != DataAutoDownloadActivity.this.filesRow);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == DataAutoDownloadActivity.this.photosRow || adapterPosition == DataAutoDownloadActivity.this.videosRow || adapterPosition == DataAutoDownloadActivity.this.filesRow;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(int i) {
            DownloadController.Preset preset = (DownloadController.Preset) DataAutoDownloadActivity.this.presets.get(i);
            if (preset == DataAutoDownloadActivity.this.lowPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 0;
            } else if (preset == DataAutoDownloadActivity.this.mediumPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 1;
            } else if (preset == DataAutoDownloadActivity.this.highPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 2;
            } else {
                DataAutoDownloadActivity.this.currentPresetNum = 3;
            }
            if (DataAutoDownloadActivity.this.currentType == 0) {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentMobilePreset = DataAutoDownloadActivity.this.currentPresetNum;
            } else if (DataAutoDownloadActivity.this.currentType == 1) {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentWifiPreset = DataAutoDownloadActivity.this.currentPresetNum;
            } else {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentRoamingPreset = DataAutoDownloadActivity.this.currentPresetNum;
            }
            SharedPreferences.Editor edit = MessagesController.getMainSettings(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).edit();
            edit.putInt(DataAutoDownloadActivity.this.key2, DataAutoDownloadActivity.this.currentPresetNum);
            edit.commit();
            DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).checkAutodownloadSettings();
            for (int i2 = 0; i2 < 3; i2++) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = DataAutoDownloadActivity.this.listView.findViewHolderForAdapterPosition(DataAutoDownloadActivity.this.photosRow + i2);
                if (findViewHolderForAdapterPosition != null) {
                    DataAutoDownloadActivity.this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, DataAutoDownloadActivity.this.photosRow + i2);
                }
            }
            DataAutoDownloadActivity.this.wereAnyChanges = true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextInfoPrivacyCell textInfoPrivacyCell;
            if (i == 0) {
                TextCheckCell textCheckCell = new TextCheckCell(this.mContext);
                textCheckCell.setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
                textCheckCell.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textCheckCell.setHeight(56);
                textInfoPrivacyCell = textCheckCell;
            } else if (i == 1) {
                textInfoPrivacyCell = new ShadowSectionCell(this.mContext);
            } else if (i == 2) {
                HeaderCell headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textInfoPrivacyCell = headerCell;
            } else if (i == 3) {
                SlideChooseView slideChooseView = new SlideChooseView(this.mContext);
                slideChooseView.setCallback(new DataAutoDownloadActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                slideChooseView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textInfoPrivacyCell = slideChooseView;
            } else if (i == 4) {
                NotificationsCheckCell notificationsCheckCell = new NotificationsCheckCell(this.mContext);
                notificationsCheckCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textInfoPrivacyCell = notificationsCheckCell;
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                textInfoPrivacyCell = textInfoPrivacyCell2;
            }
            textInfoPrivacyCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(textInfoPrivacyCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == DataAutoDownloadActivity.this.autoDownloadRow) {
                return 0;
            }
            if (i == DataAutoDownloadActivity.this.usageSectionRow) {
                return 1;
            }
            if (i == DataAutoDownloadActivity.this.usageHeaderRow || i == DataAutoDownloadActivity.this.typeHeaderRow) {
                return 2;
            }
            if (i == DataAutoDownloadActivity.this.usageProgressRow) {
                return 3;
            }
            return (i == DataAutoDownloadActivity.this.photosRow || i == DataAutoDownloadActivity.this.videosRow || i == DataAutoDownloadActivity.this.filesRow) ? 4 : 5;
        }
    }

    public void updatePresetChoseView(SlideChooseView slideChooseView) {
        String[] strArr = new String[this.presets.size()];
        for (int i = 0; i < this.presets.size(); i++) {
            DownloadController.Preset preset = this.presets.get(i);
            if (preset == this.lowPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadLow", 2131624583);
            } else if (preset == this.mediumPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadMedium", 2131624587);
            } else if (preset == this.highPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadHigh", 2131624582);
            } else {
                strArr[i] = LocaleController.getString("AutoDownloadCustom", 2131624576);
            }
        }
        slideChooseView.setOptions(this.selectedPreset, strArr);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, NotificationsCheckCell.class, SlideChooseView.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, null, null, null, "windowBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, null, null, null, "windowBackgroundUnchecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundCheckText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlue"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlueChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlueThumb"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlueThumbChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlueSelector"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackBlueSelectorChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "windowBackgroundWhiteGrayText"));
        return arrayList;
    }
}
