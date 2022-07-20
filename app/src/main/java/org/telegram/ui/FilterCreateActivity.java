package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$TL_dialogFilter;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC$TL_inputPeerChat;
import org.telegram.tgnet.TLRPC$TL_inputPeerUser;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFilter;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class FilterCreateActivity extends BaseFragment {
    private ListAdapter adapter;
    private boolean creatingNew;
    private ActionBarMenuItem doneItem;
    private int excludeAddRow;
    private int excludeArchivedRow;
    private int excludeEndRow;
    private boolean excludeExpanded;
    private int excludeHeaderRow;
    private int excludeMutedRow;
    private int excludeReadRow;
    private int excludeSectionRow;
    private int excludeShowMoreRow;
    private int excludeStartRow;
    private MessagesController.DialogFilter filter;
    private boolean hasUserChanged;
    private int imageRow;
    private int includeAddRow;
    private int includeBotsRow;
    private int includeChannelsRow;
    private int includeContactsRow;
    private int includeEndRow;
    private boolean includeExpanded;
    private int includeGroupsRow;
    private int includeHeaderRow;
    private int includeNonContactsRow;
    private int includeSectionRow;
    private int includeShowMoreRow;
    private int includeStartRow;
    private RecyclerListView listView;
    private boolean nameChangedManually;
    private int namePreSectionRow;
    private int nameRow;
    private int nameSectionRow;
    private ArrayList<Long> newAlwaysShow;
    private int newFilterFlags;
    private String newFilterName;
    private ArrayList<Long> newNeverShow;
    private LongSparseIntArray newPinned;
    private int removeRow;
    private int removeSectionRow;
    private int rowCount;

    /* loaded from: classes3.dex */
    public static class HintInnerCell extends FrameLayout {
        private RLottieImageView imageView;

        public HintInnerCell(Context context) {
            super(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setAnimation(2131558446, 100, 100);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.playAnimation();
            addView(this.imageView, LayoutHelper.createFrame(100, 100.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
            this.imageView.setOnClickListener(new FilterCreateActivity$HintInnerCell$$ExternalSyntheticLambda0(this));
        }

        public /* synthetic */ void lambda$new$0(View view) {
            if (!this.imageView.isPlaying()) {
                this.imageView.setProgress(0.0f);
                this.imageView.playAnimation();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(156.0f), 1073741824));
        }
    }

    public FilterCreateActivity() {
        this(null, null);
    }

    public FilterCreateActivity(MessagesController.DialogFilter dialogFilter) {
        this(dialogFilter, null);
    }

    public FilterCreateActivity(MessagesController.DialogFilter dialogFilter, ArrayList<Long> arrayList) {
        this.rowCount = 0;
        this.filter = dialogFilter;
        if (dialogFilter == null) {
            MessagesController.DialogFilter dialogFilter2 = new MessagesController.DialogFilter();
            this.filter = dialogFilter2;
            dialogFilter2.id = 2;
            while (getMessagesController().dialogFiltersById.get(this.filter.id) != null) {
                this.filter.id++;
            }
            this.filter.name = "";
            this.creatingNew = true;
        }
        MessagesController.DialogFilter dialogFilter3 = this.filter;
        this.newFilterName = dialogFilter3.name;
        this.newFilterFlags = dialogFilter3.flags;
        ArrayList<Long> arrayList2 = new ArrayList<>(this.filter.alwaysShow);
        this.newAlwaysShow = arrayList2;
        if (arrayList != null) {
            arrayList2.addAll(arrayList);
        }
        this.newNeverShow = new ArrayList<>(this.filter.neverShow);
        this.newPinned = this.filter.pinnedDialogs.clone();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        updateRows();
        return super.onFragmentCreate();
    }

    private void updateRows() {
        this.rowCount = 0;
        if (this.creatingNew) {
            this.rowCount = 0 + 1;
            this.imageRow = 0;
            this.namePreSectionRow = -1;
        } else {
            this.imageRow = -1;
            this.rowCount = 0 + 1;
            this.namePreSectionRow = 0;
        }
        int i = this.rowCount;
        int i2 = i + 1;
        this.rowCount = i2;
        this.nameRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.nameSectionRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.includeHeaderRow = i3;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.includeAddRow = i4;
        int i6 = this.newFilterFlags;
        if ((MessagesController.DIALOG_FILTER_FLAG_CONTACTS & i6) != 0) {
            this.rowCount = i5 + 1;
            this.includeContactsRow = i5;
        } else {
            this.includeContactsRow = -1;
        }
        if ((MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS & i6) != 0) {
            int i7 = this.rowCount;
            this.rowCount = i7 + 1;
            this.includeNonContactsRow = i7;
        } else {
            this.includeNonContactsRow = -1;
        }
        if ((MessagesController.DIALOG_FILTER_FLAG_GROUPS & i6) != 0) {
            int i8 = this.rowCount;
            this.rowCount = i8 + 1;
            this.includeGroupsRow = i8;
        } else {
            this.includeGroupsRow = -1;
        }
        if ((MessagesController.DIALOG_FILTER_FLAG_CHANNELS & i6) != 0) {
            int i9 = this.rowCount;
            this.rowCount = i9 + 1;
            this.includeChannelsRow = i9;
        } else {
            this.includeChannelsRow = -1;
        }
        if ((MessagesController.DIALOG_FILTER_FLAG_BOTS & i6) != 0) {
            int i10 = this.rowCount;
            this.rowCount = i10 + 1;
            this.includeBotsRow = i10;
        } else {
            this.includeBotsRow = -1;
        }
        if (!this.newAlwaysShow.isEmpty()) {
            this.includeStartRow = this.rowCount;
            int size = (this.includeExpanded || this.newAlwaysShow.size() < 8) ? this.newAlwaysShow.size() : Math.min(5, this.newAlwaysShow.size());
            int i11 = this.rowCount + size;
            this.rowCount = i11;
            this.includeEndRow = i11;
            if (size != this.newAlwaysShow.size()) {
                int i12 = this.rowCount;
                this.rowCount = i12 + 1;
                this.includeShowMoreRow = i12;
            } else {
                this.includeShowMoreRow = -1;
            }
        } else {
            this.includeStartRow = -1;
            this.includeEndRow = -1;
            this.includeShowMoreRow = -1;
        }
        int i13 = this.rowCount;
        int i14 = i13 + 1;
        this.rowCount = i14;
        this.includeSectionRow = i13;
        int i15 = i14 + 1;
        this.rowCount = i15;
        this.excludeHeaderRow = i14;
        int i16 = i15 + 1;
        this.rowCount = i16;
        this.excludeAddRow = i15;
        int i17 = this.newFilterFlags;
        if ((MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED & i17) != 0) {
            this.rowCount = i16 + 1;
            this.excludeMutedRow = i16;
        } else {
            this.excludeMutedRow = -1;
        }
        if ((MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ & i17) != 0) {
            int i18 = this.rowCount;
            this.rowCount = i18 + 1;
            this.excludeReadRow = i18;
        } else {
            this.excludeReadRow = -1;
        }
        if ((i17 & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED) != 0) {
            int i19 = this.rowCount;
            this.rowCount = i19 + 1;
            this.excludeArchivedRow = i19;
        } else {
            this.excludeArchivedRow = -1;
        }
        if (!this.newNeverShow.isEmpty()) {
            this.excludeStartRow = this.rowCount;
            int size2 = (this.excludeExpanded || this.newNeverShow.size() < 8) ? this.newNeverShow.size() : Math.min(5, this.newNeverShow.size());
            int i20 = this.rowCount + size2;
            this.rowCount = i20;
            this.excludeEndRow = i20;
            if (size2 != this.newNeverShow.size()) {
                int i21 = this.rowCount;
                this.rowCount = i21 + 1;
                this.excludeShowMoreRow = i21;
            } else {
                this.excludeShowMoreRow = -1;
            }
        } else {
            this.excludeStartRow = -1;
            this.excludeEndRow = -1;
            this.excludeShowMoreRow = -1;
        }
        int i22 = this.rowCount;
        int i23 = i22 + 1;
        this.rowCount = i23;
        this.excludeSectionRow = i22;
        if (!this.creatingNew) {
            int i24 = i23 + 1;
            this.rowCount = i24;
            this.removeRow = i23;
            this.rowCount = i24 + 1;
            this.removeSectionRow = i24;
        } else {
            this.removeRow = -1;
            this.removeSectionRow = -1;
        }
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (this.creatingNew) {
            this.actionBar.setTitle(LocaleController.getString("FilterNew", 2131625911));
        } else {
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTextSize(AndroidUtilities.dp(20.0f));
            this.actionBar.setTitle(Emoji.replaceEmoji(this.filter.name, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.doneItem = createMenu.addItem(1, LocaleController.getString("Save", 2131628122).toUpperCase());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.listView = anonymousClass2;
        anonymousClass2.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new FilterCreateActivity$$ExternalSyntheticLambda12(this));
        this.listView.setOnItemLongClickListener(new FilterCreateActivity$$ExternalSyntheticLambda13(this));
        checkDoneButton(false);
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.FilterCreateActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            FilterCreateActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!FilterCreateActivity.this.checkDiscard()) {
                    return;
                }
                FilterCreateActivity.this.finishFragment();
            } else if (i != 1) {
            } else {
                FilterCreateActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.FilterCreateActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerListView {
        @Override // android.view.ViewGroup, android.view.View
        public boolean requestFocus(int i, Rect rect) {
            return false;
        }

        AnonymousClass2(FilterCreateActivity filterCreateActivity, Context context) {
            super(context);
        }
    }

    public /* synthetic */ void lambda$createView$4(View view, int i) {
        if (getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (i == this.includeShowMoreRow) {
            this.includeExpanded = true;
            updateRows();
        } else if (i == this.excludeShowMoreRow) {
            this.excludeExpanded = true;
            updateRows();
        } else {
            int i2 = this.includeAddRow;
            if (i == i2 || i == this.excludeAddRow) {
                ArrayList<Long> arrayList = i == this.excludeAddRow ? this.newNeverShow : this.newAlwaysShow;
                if (i != i2) {
                    z = false;
                }
                FilterUsersActivity filterUsersActivity = new FilterUsersActivity(z, arrayList, this.newFilterFlags);
                filterUsersActivity.setDelegate(new FilterCreateActivity$$ExternalSyntheticLambda14(this, i));
                presentFragment(filterUsersActivity);
            } else if (i == this.removeRow) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("FilterDelete", 2131625887));
                builder.setMessage(LocaleController.getString("FilterDeleteAlert", 2131625888));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new FilterCreateActivity$$ExternalSyntheticLambda2(this));
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            } else if (i == this.nameRow) {
                PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
                pollEditTextCell.getTextView().requestFocus();
                AndroidUtilities.showKeyboard(pollEditTextCell.getTextView());
            } else if (!(view instanceof UserCell)) {
            } else {
                UserCell userCell = (UserCell) view;
                CharSequence name = userCell.getName();
                Object currentObject = userCell.getCurrentObject();
                if (i >= this.includeSectionRow) {
                    z = false;
                }
                showRemoveAlert(i, name, currentObject, z);
            }
        }
    }

    public /* synthetic */ void lambda$createView$0(int i, ArrayList arrayList, int i2) {
        this.newFilterFlags = i2;
        if (i == this.excludeAddRow) {
            this.newNeverShow = arrayList;
            for (int i3 = 0; i3 < this.newNeverShow.size(); i3++) {
                Long l = this.newNeverShow.get(i3);
                this.newAlwaysShow.remove(l);
                this.newPinned.delete(l.longValue());
            }
        } else {
            this.newAlwaysShow = arrayList;
            for (int i4 = 0; i4 < this.newAlwaysShow.size(); i4++) {
                this.newNeverShow.remove(this.newAlwaysShow.get(i4));
            }
            ArrayList arrayList2 = new ArrayList();
            int size = this.newPinned.size();
            for (int i5 = 0; i5 < size; i5++) {
                Long valueOf = Long.valueOf(this.newPinned.keyAt(i5));
                if (!DialogObject.isEncryptedDialog(valueOf.longValue()) && !this.newAlwaysShow.contains(valueOf)) {
                    arrayList2.add(valueOf);
                }
            }
            int size2 = arrayList2.size();
            for (int i6 = 0; i6 < size2; i6++) {
                this.newPinned.delete(((Long) arrayList2.get(i6)).longValue());
            }
        }
        fillFilterName();
        checkDoneButton(false);
        updateRows();
    }

    public /* synthetic */ void lambda$createView$3(DialogInterface dialogInterface, int i) {
        AlertDialog alertDialog;
        if (getParentActivity() != null) {
            alertDialog = new AlertDialog(getParentActivity(), 3);
            alertDialog.setCanCancel(false);
            alertDialog.show();
        } else {
            alertDialog = null;
        }
        TLRPC$TL_messages_updateDialogFilter tLRPC$TL_messages_updateDialogFilter = new TLRPC$TL_messages_updateDialogFilter();
        tLRPC$TL_messages_updateDialogFilter.id = this.filter.id;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFilter, new FilterCreateActivity$$ExternalSyntheticLambda9(this, alertDialog));
    }

    public /* synthetic */ void lambda$createView$2(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new FilterCreateActivity$$ExternalSyntheticLambda6(this, alertDialog));
    }

    public /* synthetic */ void lambda$createView$1(AlertDialog alertDialog) {
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        getMessagesController().removeFilter(this.filter);
        getMessagesStorage().deleteDialogFilter(this.filter);
        finishFragment();
    }

    public /* synthetic */ boolean lambda$createView$5(View view, int i) {
        boolean z = false;
        if (view instanceof UserCell) {
            UserCell userCell = (UserCell) view;
            CharSequence name = userCell.getName();
            Object currentObject = userCell.getCurrentObject();
            if (i < this.includeSectionRow) {
                z = true;
            }
            showRemoveAlert(i, name, currentObject, z);
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return checkDiscard();
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void fillFilterName() {
        String str;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        if (this.creatingNew) {
            if (!TextUtils.isEmpty(this.newFilterName) && this.nameChangedManually) {
                return;
            }
            int i = this.newFilterFlags;
            int i2 = MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS;
            int i3 = i & i2;
            String str2 = "";
            if ((i3 & i2) == i2) {
                if ((MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ & i) != 0) {
                    str = LocaleController.getString("FilterNameUnread", 2131625909);
                } else {
                    if ((i & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED) != 0) {
                        str = LocaleController.getString("FilterNameNonMuted", 2131625908);
                    }
                    str = str2;
                }
                if (str != null || str.length() <= 12) {
                    str2 = str;
                }
                this.newFilterName = str2;
                findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                if (findViewHolderForAdapterPosition == null) {
                    return;
                }
                this.adapter.onViewAttachedToWindow(findViewHolderForAdapterPosition);
                return;
            }
            int i4 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
            if ((i3 & i4) == 0) {
                int i5 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
                if ((i3 & i5) == 0) {
                    int i6 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
                    if ((i3 & i6) == 0) {
                        int i7 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
                        if ((i3 & i7) == 0) {
                            int i8 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
                            if ((i3 & i8) != 0 && ((i8 ^ (-1)) & i3) == 0) {
                                str = LocaleController.getString("FilterChannels", 2131625877);
                                if (str != null) {
                                }
                                str2 = str;
                                this.newFilterName = str2;
                                findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                                if (findViewHolderForAdapterPosition == null) {
                                }
                            }
                            str = str2;
                            if (str != null) {
                            }
                            str2 = str;
                            this.newFilterName = str2;
                            findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                            if (findViewHolderForAdapterPosition == null) {
                            }
                        } else {
                            if (((i7 ^ (-1)) & i3) == 0) {
                                str = LocaleController.getString("FilterBots", 2131625876);
                                if (str != null) {
                                }
                                str2 = str;
                                this.newFilterName = str2;
                                findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                                if (findViewHolderForAdapterPosition == null) {
                                }
                            }
                            str = str2;
                            if (str != null) {
                            }
                            str2 = str;
                            this.newFilterName = str2;
                            findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                            if (findViewHolderForAdapterPosition == null) {
                            }
                        }
                    } else {
                        if (((i6 ^ (-1)) & i3) == 0) {
                            str = LocaleController.getString("FilterGroups", 2131625903);
                            if (str != null) {
                            }
                            str2 = str;
                            this.newFilterName = str2;
                            findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                            if (findViewHolderForAdapterPosition == null) {
                            }
                        }
                        str = str2;
                        if (str != null) {
                        }
                        str2 = str;
                        this.newFilterName = str2;
                        findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                        if (findViewHolderForAdapterPosition == null) {
                        }
                    }
                } else {
                    if (((i5 ^ (-1)) & i3) == 0) {
                        str = LocaleController.getString("FilterNonContacts", 2131625916);
                        if (str != null) {
                        }
                        str2 = str;
                        this.newFilterName = str2;
                        findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                        if (findViewHolderForAdapterPosition == null) {
                        }
                    }
                    str = str2;
                    if (str != null) {
                    }
                    str2 = str;
                    this.newFilterName = str2;
                    findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                    if (findViewHolderForAdapterPosition == null) {
                    }
                }
            } else {
                if (((i4 ^ (-1)) & i3) == 0) {
                    str = LocaleController.getString("FilterContacts", 2131625886);
                    if (str != null) {
                    }
                    str2 = str;
                    this.newFilterName = str2;
                    findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                    if (findViewHolderForAdapterPosition == null) {
                    }
                }
                str = str2;
                if (str != null) {
                }
                str2 = str;
                this.newFilterName = str2;
                findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.nameRow);
                if (findViewHolderForAdapterPosition == null) {
                }
            }
        }
    }

    public boolean checkDiscard() {
        if (this.doneItem.getAlpha() == 1.0f) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            if (this.creatingNew) {
                builder.setTitle(LocaleController.getString("FilterDiscardNewTitle", 2131625893));
                builder.setMessage(LocaleController.getString("FilterDiscardNewAlert", 2131625891));
                builder.setPositiveButton(LocaleController.getString("FilterDiscardNewSave", 2131625892), new FilterCreateActivity$$ExternalSyntheticLambda3(this));
            } else {
                builder.setTitle(LocaleController.getString("FilterDiscardTitle", 2131625894));
                builder.setMessage(LocaleController.getString("FilterDiscardAlert", 2131625890));
                builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131624396), new FilterCreateActivity$$ExternalSyntheticLambda0(this));
            }
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131627272), new FilterCreateActivity$$ExternalSyntheticLambda1(this));
            showDialog(builder.create());
            return false;
        }
        return true;
    }

    public /* synthetic */ void lambda$checkDiscard$6(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$7(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$8(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    private void showRemoveAlert(int i, CharSequence charSequence, Object obj, boolean z) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        if (z) {
            builder.setTitle(LocaleController.getString("FilterRemoveInclusionTitle", 2131625927));
            if (obj instanceof String) {
                builder.setMessage(LocaleController.formatString("FilterRemoveInclusionText", 2131625926, charSequence));
            } else if (obj instanceof TLRPC$User) {
                builder.setMessage(LocaleController.formatString("FilterRemoveInclusionUserText", 2131625928, charSequence));
            } else {
                builder.setMessage(LocaleController.formatString("FilterRemoveInclusionChatText", 2131625925, charSequence));
            }
        } else {
            builder.setTitle(LocaleController.getString("FilterRemoveExclusionTitle", 2131625922));
            if (obj instanceof String) {
                builder.setMessage(LocaleController.formatString("FilterRemoveExclusionText", 2131625921, charSequence));
            } else if (obj instanceof TLRPC$User) {
                builder.setMessage(LocaleController.formatString("FilterRemoveExclusionUserText", 2131625923, charSequence));
            } else {
                builder.setMessage(LocaleController.formatString("FilterRemoveExclusionChatText", 2131625920, charSequence));
            }
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setPositiveButton(LocaleController.getString("StickersRemove", 2131628514), new FilterCreateActivity$$ExternalSyntheticLambda4(this, i, z));
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }
    }

    public /* synthetic */ void lambda$showRemoveAlert$9(int i, boolean z, DialogInterface dialogInterface, int i2) {
        if (i == this.includeContactsRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_CONTACTS ^ (-1);
        } else if (i == this.includeNonContactsRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS ^ (-1);
        } else if (i == this.includeGroupsRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_GROUPS ^ (-1);
        } else if (i == this.includeChannelsRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_CHANNELS ^ (-1);
        } else if (i == this.includeBotsRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_BOTS ^ (-1);
        } else if (i == this.excludeArchivedRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED ^ (-1);
        } else if (i == this.excludeMutedRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED ^ (-1);
        } else if (i == this.excludeReadRow) {
            this.newFilterFlags &= MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ ^ (-1);
        } else if (z) {
            this.newAlwaysShow.remove(i - this.includeStartRow);
        } else {
            this.newNeverShow.remove(i - this.excludeStartRow);
        }
        fillFilterName();
        updateRows();
        checkDoneButton(true);
    }

    public void processDone() {
        saveFilterToServer(this.filter, this.newFilterFlags, this.newFilterName, this.newAlwaysShow, this.newNeverShow, this.newPinned, this.creatingNew, false, this.hasUserChanged, true, true, this, new FilterCreateActivity$$ExternalSyntheticLambda5(this));
    }

    public /* synthetic */ void lambda$processDone$10() {
        getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated, new Object[0]);
        finishFragment();
    }

    private static void processAddFilter(MessagesController.DialogFilter dialogFilter, int i, String str, ArrayList<Long> arrayList, ArrayList<Long> arrayList2, boolean z, boolean z2, boolean z3, boolean z4, BaseFragment baseFragment, Runnable runnable) {
        if (dialogFilter.flags != i || z3) {
            dialogFilter.pendingUnreadCount = -1;
            if (z4) {
                dialogFilter.unreadCount = -1;
            }
        }
        dialogFilter.flags = i;
        dialogFilter.name = str;
        dialogFilter.neverShow = arrayList2;
        dialogFilter.alwaysShow = arrayList;
        if (z) {
            baseFragment.getMessagesController().addFilter(dialogFilter, z2);
        } else {
            baseFragment.getMessagesController().onFilterUpdate(dialogFilter);
        }
        baseFragment.getMessagesStorage().saveDialogFilter(dialogFilter, z2, true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void saveFilterToServer(MessagesController.DialogFilter dialogFilter, int i, String str, ArrayList<Long> arrayList, ArrayList<Long> arrayList2, LongSparseIntArray longSparseIntArray, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, BaseFragment baseFragment, Runnable runnable) {
        AlertDialog alertDialog;
        ArrayList<Long> arrayList3;
        ArrayList<TLRPC$InputPeer> arrayList4;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        int i2 = 3;
        if (z5) {
            alertDialog = new AlertDialog(baseFragment.getParentActivity(), 3);
            alertDialog.setCanCancel(false);
            alertDialog.show();
        } else {
            alertDialog = null;
        }
        TLRPC$TL_messages_updateDialogFilter tLRPC$TL_messages_updateDialogFilter = new TLRPC$TL_messages_updateDialogFilter();
        tLRPC$TL_messages_updateDialogFilter.id = dialogFilter.id;
        int i3 = 1;
        tLRPC$TL_messages_updateDialogFilter.flags |= 1;
        TLRPC$TL_dialogFilter tLRPC$TL_dialogFilter = new TLRPC$TL_dialogFilter();
        tLRPC$TL_messages_updateDialogFilter.filter = tLRPC$TL_dialogFilter;
        tLRPC$TL_dialogFilter.contacts = (i & MessagesController.DIALOG_FILTER_FLAG_CONTACTS) != 0;
        tLRPC$TL_dialogFilter.non_contacts = (i & MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS) != 0;
        tLRPC$TL_dialogFilter.groups = (i & MessagesController.DIALOG_FILTER_FLAG_GROUPS) != 0;
        tLRPC$TL_dialogFilter.broadcasts = (i & MessagesController.DIALOG_FILTER_FLAG_CHANNELS) != 0;
        tLRPC$TL_dialogFilter.bots = (i & MessagesController.DIALOG_FILTER_FLAG_BOTS) != 0;
        tLRPC$TL_dialogFilter.exclude_muted = (i & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED) != 0;
        tLRPC$TL_dialogFilter.exclude_read = (i & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) != 0;
        tLRPC$TL_dialogFilter.exclude_archived = (i & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED) != 0;
        tLRPC$TL_dialogFilter.id = dialogFilter.id;
        tLRPC$TL_dialogFilter.title = str;
        MessagesController messagesController = baseFragment.getMessagesController();
        ArrayList<Long> arrayList5 = new ArrayList<>();
        if (longSparseIntArray.size() != 0) {
            int size = longSparseIntArray.size();
            for (int i4 = 0; i4 < size; i4++) {
                long keyAt = longSparseIntArray.keyAt(i4);
                if (!DialogObject.isEncryptedDialog(keyAt)) {
                    arrayList5.add(Long.valueOf(keyAt));
                }
            }
            Collections.sort(arrayList5, new FilterCreateActivity$$ExternalSyntheticLambda8(longSparseIntArray));
        }
        int i5 = 0;
        while (i5 < i2) {
            if (i5 == 0) {
                arrayList4 = tLRPC$TL_messages_updateDialogFilter.filter.include_peers;
                arrayList3 = arrayList;
            } else if (i5 == i3) {
                arrayList4 = tLRPC$TL_messages_updateDialogFilter.filter.exclude_peers;
                arrayList3 = arrayList2;
            } else {
                arrayList4 = tLRPC$TL_messages_updateDialogFilter.filter.pinned_peers;
                arrayList3 = arrayList5;
            }
            int size2 = arrayList3.size();
            for (int i6 = 0; i6 < size2; i6++) {
                long longValue = arrayList3.get(i6).longValue();
                if ((i5 != 0 || longSparseIntArray.indexOfKey(longValue) < 0) && !DialogObject.isEncryptedDialog(longValue)) {
                    if (longValue > 0) {
                        TLRPC$User user = messagesController.getUser(Long.valueOf(longValue));
                        if (user != null) {
                            TLRPC$TL_inputPeerUser tLRPC$TL_inputPeerUser = new TLRPC$TL_inputPeerUser();
                            tLRPC$TL_inputPeerUser.user_id = longValue;
                            tLRPC$TL_inputPeerUser.access_hash = user.access_hash;
                            arrayList4.add(tLRPC$TL_inputPeerUser);
                        }
                    } else {
                        long j = -longValue;
                        TLRPC$Chat chat = messagesController.getChat(Long.valueOf(j));
                        if (chat != null) {
                            if (ChatObject.isChannel(chat)) {
                                TLRPC$TL_inputPeerChannel tLRPC$TL_inputPeerChannel = new TLRPC$TL_inputPeerChannel();
                                tLRPC$TL_inputPeerChannel.channel_id = j;
                                tLRPC$TL_inputPeerChannel.access_hash = chat.access_hash;
                                arrayList4.add(tLRPC$TL_inputPeerChannel);
                            } else {
                                TLRPC$TL_inputPeerChat tLRPC$TL_inputPeerChat = new TLRPC$TL_inputPeerChat();
                                tLRPC$TL_inputPeerChat.chat_id = j;
                                arrayList4.add(tLRPC$TL_inputPeerChat);
                            }
                        }
                    }
                }
            }
            i5++;
            i2 = 3;
            i3 = 1;
        }
        baseFragment.getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFilter, new FilterCreateActivity$$ExternalSyntheticLambda10(z5, alertDialog, dialogFilter, i, str, arrayList, arrayList2, z, z2, z3, z4, baseFragment, runnable));
        if (z5) {
            return;
        }
        processAddFilter(dialogFilter, i, str, arrayList, arrayList2, z, z2, z3, z4, baseFragment, runnable);
    }

    public static /* synthetic */ int lambda$saveFilterToServer$11(LongSparseIntArray longSparseIntArray, Long l, Long l2) {
        int i = longSparseIntArray.get(l.longValue());
        int i2 = longSparseIntArray.get(l2.longValue());
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public static /* synthetic */ void lambda$saveFilterToServer$13(boolean z, AlertDialog alertDialog, MessagesController.DialogFilter dialogFilter, int i, String str, ArrayList arrayList, ArrayList arrayList2, boolean z2, boolean z3, boolean z4, boolean z5, BaseFragment baseFragment, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new FilterCreateActivity$$ExternalSyntheticLambda7(z, alertDialog, dialogFilter, i, str, arrayList, arrayList2, z2, z3, z4, z5, baseFragment, runnable));
    }

    public static /* synthetic */ void lambda$saveFilterToServer$12(boolean z, AlertDialog alertDialog, MessagesController.DialogFilter dialogFilter, int i, String str, ArrayList arrayList, ArrayList arrayList2, boolean z2, boolean z3, boolean z4, boolean z5, BaseFragment baseFragment, Runnable runnable) {
        if (z) {
            if (alertDialog != null) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            processAddFilter(dialogFilter, i, str, arrayList, arrayList2, z2, z3, z4, z5, baseFragment, runnable);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        return checkDiscard();
    }

    private boolean hasChanges() {
        this.hasUserChanged = false;
        if (this.filter.alwaysShow.size() != this.newAlwaysShow.size()) {
            this.hasUserChanged = true;
        }
        if (this.filter.neverShow.size() != this.newNeverShow.size()) {
            this.hasUserChanged = true;
        }
        if (!this.hasUserChanged) {
            Collections.sort(this.filter.alwaysShow);
            Collections.sort(this.newAlwaysShow);
            if (!this.filter.alwaysShow.equals(this.newAlwaysShow)) {
                this.hasUserChanged = true;
            }
            Collections.sort(this.filter.neverShow);
            Collections.sort(this.newNeverShow);
            if (!this.filter.neverShow.equals(this.newNeverShow)) {
                this.hasUserChanged = true;
            }
        }
        if (TextUtils.equals(this.filter.name, this.newFilterName) && this.filter.flags == this.newFilterFlags) {
            return this.hasUserChanged;
        }
        return true;
    }

    public void checkDoneButton(boolean z) {
        boolean z2 = true;
        boolean z3 = !TextUtils.isEmpty(this.newFilterName) && this.newFilterName.length() <= 12;
        if (z3) {
            if ((this.newFilterFlags & MessagesController.DIALOG_FILTER_FLAG_ALL_CHATS) == 0 && this.newAlwaysShow.isEmpty()) {
                z2 = false;
            }
            z3 = (!z2 || this.creatingNew) ? z2 : hasChanges();
        }
        if (this.doneItem.isEnabled() == z3) {
            return;
        }
        this.doneItem.setEnabled(z3);
        float f = 1.0f;
        if (z) {
            ViewPropertyAnimator scaleX = this.doneItem.animate().alpha(z3 ? 1.0f : 0.0f).scaleX(z3 ? 1.0f : 0.0f);
            if (!z3) {
                f = 0.0f;
            }
            scaleX.scaleY(f).setDuration(180L).start();
            return;
        }
        this.doneItem.setAlpha(z3 ? 1.0f : 0.0f);
        this.doneItem.setScaleX(z3 ? 1.0f : 0.0f);
        ActionBarMenuItem actionBarMenuItem = this.doneItem;
        if (!z3) {
            f = 0.0f;
        }
        actionBarMenuItem.setScaleY(f);
    }

    public void setTextLeft(View view) {
        if (view instanceof PollEditTextCell) {
            PollEditTextCell pollEditTextCell = (PollEditTextCell) view;
            String str = this.newFilterName;
            int length = 12 - (str != null ? str.length() : 0);
            if (length <= 3.6000004f) {
                pollEditTextCell.setText2(String.format("%d", Integer.valueOf(length)));
                SimpleTextView textView2 = pollEditTextCell.getTextView2();
                String str2 = length < 0 ? "windowBackgroundWhiteRedText5" : "windowBackgroundWhiteGrayText3";
                textView2.setTextColor(Theme.getColor(str2));
                textView2.setTag(str2);
                textView2.setAlpha((pollEditTextCell.getTextView().isFocused() || length < 0) ? 1.0f : 0.0f);
                return;
            }
            pollEditTextCell.setText2("");
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            FilterCreateActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 3 || itemViewType == 0 || itemViewType == 2 || itemViewType == 5) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return FilterCreateActivity.this.rowCount;
        }

        /* renamed from: org.telegram.ui.FilterCreateActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements TextWatcher {
            final /* synthetic */ PollEditTextCell val$cell;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass1(PollEditTextCell pollEditTextCell) {
                ListAdapter.this = r1;
                this.val$cell = pollEditTextCell;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.val$cell.getTag() != null) {
                    return;
                }
                String obj = editable.toString();
                if (!TextUtils.equals(obj, FilterCreateActivity.this.newFilterName)) {
                    FilterCreateActivity.this.nameChangedManually = !TextUtils.isEmpty(obj);
                    FilterCreateActivity.this.newFilterName = obj;
                }
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = FilterCreateActivity.this.listView.findViewHolderForAdapterPosition(FilterCreateActivity.this.nameRow);
                if (findViewHolderForAdapterPosition != null) {
                    FilterCreateActivity.this.setTextLeft(findViewHolderForAdapterPosition.itemView);
                }
                FilterCreateActivity.this.checkDoneButton(true);
            }
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(PollEditTextCell pollEditTextCell, View view, boolean z) {
            pollEditTextCell.getTextView2().setAlpha((z || FilterCreateActivity.this.newFilterName.length() > 12) ? 1.0f : 0.0f);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            TextCell textCell;
            if (i == 0) {
                HeaderCell headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCell = headerCell;
            } else if (i == 1) {
                UserCell userCell = new UserCell(this.mContext, 6, 0, false);
                userCell.setSelfAsSavedMessages(true);
                userCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCell = userCell;
            } else if (i == 2) {
                PollEditTextCell pollEditTextCell = new PollEditTextCell(this.mContext, null);
                pollEditTextCell.createErrorTextView();
                pollEditTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                pollEditTextCell.addTextWatcher(new AnonymousClass1(pollEditTextCell));
                EditTextBoldCursor textView = pollEditTextCell.getTextView();
                pollEditTextCell.setShowNextButton(true);
                textView.setOnFocusChangeListener(new FilterCreateActivity$ListAdapter$$ExternalSyntheticLambda0(this, pollEditTextCell));
                textView.setImeOptions(268435462);
                textCell = pollEditTextCell;
            } else {
                if (i == 3) {
                    view = new ShadowSectionCell(this.mContext);
                } else if (i == 4) {
                    TextCell textCell2 = new TextCell(this.mContext);
                    textCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    textCell = textCell2;
                } else if (i == 5) {
                    view = new HintInnerCell(this.mContext);
                } else {
                    view = new TextInfoPrivacyCell(this.mContext);
                }
                return new RecyclerListView.Holder(view);
            }
            view = textCell;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 2) {
                FilterCreateActivity.this.setTextLeft(viewHolder.itemView);
                PollEditTextCell pollEditTextCell = (PollEditTextCell) viewHolder.itemView;
                pollEditTextCell.setTag(1);
                pollEditTextCell.setTextAndHint(FilterCreateActivity.this.newFilterName != null ? FilterCreateActivity.this.newFilterName : "", LocaleController.getString("FilterNameHint", 2131625907), false);
                pollEditTextCell.setTag(null);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 2) {
                EditTextBoldCursor textView = ((PollEditTextCell) viewHolder.itemView).getTextView();
                if (!textView.isFocused()) {
                    return;
                }
                textView.clearFocus();
                AndroidUtilities.hideKeyboard(textView);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:56:0x0185, code lost:
            if (r12 == (org.telegram.ui.FilterCreateActivity.this.includeEndRow - 1)) goto L57;
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x0188, code lost:
            r3 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x01be, code lost:
            if (r12 == (org.telegram.ui.FilterCreateActivity.this.excludeEndRow - 1)) goto L57;
         */
        /* JADX WARN: Removed duplicated region for block: B:69:0x01cb  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0205  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            String str;
            Long l;
            String string2;
            String string3;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != FilterCreateActivity.this.includeHeaderRow) {
                    if (i != FilterCreateActivity.this.excludeHeaderRow) {
                        return;
                    }
                    headerCell.setText(LocaleController.getString("FilterExclude", 2131625898));
                    return;
                }
                headerCell.setText(LocaleController.getString("FilterInclude", 2131625904));
                return;
            }
            boolean z = false;
            boolean z2 = true;
            if (itemViewType != 1) {
                if (itemViewType == 3) {
                    if (i == FilterCreateActivity.this.removeSectionRow) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                } else if (itemViewType != 4) {
                    if (itemViewType != 6) {
                        return;
                    }
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != FilterCreateActivity.this.includeSectionRow) {
                        if (i == FilterCreateActivity.this.excludeSectionRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString("FilterExcludeInfo", 2131625900));
                        }
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.getString("FilterIncludeInfo", 2131625905));
                    }
                    if (i == FilterCreateActivity.this.excludeSectionRow && FilterCreateActivity.this.removeSectionRow == -1) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                } else {
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i != FilterCreateActivity.this.removeRow) {
                        if (i != FilterCreateActivity.this.includeShowMoreRow) {
                            if (i != FilterCreateActivity.this.excludeShowMoreRow) {
                                if (i != FilterCreateActivity.this.includeAddRow) {
                                    if (i != FilterCreateActivity.this.excludeAddRow) {
                                        return;
                                    }
                                    textCell.setColors("switchTrackChecked", "windowBackgroundWhiteBlueText4");
                                    String string4 = LocaleController.getString("FilterRemoveChats", 2131625919);
                                    if (i + 1 != FilterCreateActivity.this.excludeSectionRow) {
                                        z = true;
                                    }
                                    textCell.setTextAndIcon(string4, 2131165677, z);
                                    return;
                                }
                                textCell.setColors("switchTrackChecked", "windowBackgroundWhiteBlueText4");
                                String string5 = LocaleController.getString("FilterAddChats", 2131625863);
                                if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                                    z = true;
                                }
                                textCell.setTextAndIcon(string5, 2131165677, z);
                                return;
                            }
                            textCell.setColors("switchTrackChecked", "windowBackgroundWhiteBlueText4");
                            textCell.setTextAndIcon(LocaleController.formatPluralString("FilterShowMoreChats", FilterCreateActivity.this.newNeverShow.size() - 5, new Object[0]), 2131165259, false);
                            return;
                        }
                        textCell.setColors("switchTrackChecked", "windowBackgroundWhiteBlueText4");
                        textCell.setTextAndIcon(LocaleController.formatPluralString("FilterShowMoreChats", FilterCreateActivity.this.newAlwaysShow.size() - 5, new Object[0]), 2131165259, false);
                        return;
                    }
                    textCell.setColors(null, "windowBackgroundWhiteRedText5");
                    textCell.setText(LocaleController.getString("FilterDelete", 2131625887), false);
                    return;
                }
            }
            UserCell userCell = (UserCell) viewHolder.itemView;
            if (i < FilterCreateActivity.this.includeStartRow || i >= FilterCreateActivity.this.includeEndRow) {
                if (i < FilterCreateActivity.this.excludeStartRow || i >= FilterCreateActivity.this.excludeEndRow) {
                    if (i != FilterCreateActivity.this.includeContactsRow) {
                        if (i != FilterCreateActivity.this.includeNonContactsRow) {
                            if (i != FilterCreateActivity.this.includeGroupsRow) {
                                if (i != FilterCreateActivity.this.includeChannelsRow) {
                                    if (i != FilterCreateActivity.this.includeBotsRow) {
                                        if (i != FilterCreateActivity.this.excludeMutedRow) {
                                            if (i == FilterCreateActivity.this.excludeReadRow) {
                                                string = LocaleController.getString("FilterRead", 2131625917);
                                                if (i + 1 != FilterCreateActivity.this.excludeSectionRow) {
                                                    z = true;
                                                }
                                                str = "read";
                                            } else {
                                                string = LocaleController.getString("FilterArchived", 2131625873);
                                                if (i + 1 != FilterCreateActivity.this.excludeSectionRow) {
                                                    z = true;
                                                }
                                                str = "archived";
                                            }
                                        } else {
                                            string = LocaleController.getString("FilterMuted", 2131625906);
                                            if (i + 1 != FilterCreateActivity.this.excludeSectionRow) {
                                                z = true;
                                            }
                                            str = "muted";
                                        }
                                    } else {
                                        string = LocaleController.getString("FilterBots", 2131625876);
                                        if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                                            z = true;
                                        }
                                        str = "bots";
                                    }
                                } else {
                                    string = LocaleController.getString("FilterChannels", 2131625877);
                                    if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                                        z = true;
                                    }
                                    str = "channels";
                                }
                            } else {
                                string = LocaleController.getString("FilterGroups", 2131625903);
                                if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                                    z = true;
                                }
                                str = "groups";
                            }
                        } else {
                            string = LocaleController.getString("FilterNonContacts", 2131625916);
                            if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                                z = true;
                            }
                            str = "non_contacts";
                        }
                    } else {
                        string = LocaleController.getString("FilterContacts", 2131625886);
                        if (i + 1 != FilterCreateActivity.this.includeSectionRow) {
                            z = true;
                        }
                        str = "contacts";
                    }
                    userCell.setData(str, string, null, 0, z);
                    return;
                }
                l = (Long) FilterCreateActivity.this.newNeverShow.get(i - FilterCreateActivity.this.excludeStartRow);
                if (FilterCreateActivity.this.excludeShowMoreRow == -1) {
                }
                boolean z3 = z2;
                if (l.longValue() <= 0) {
                    TLRPC$User user = FilterCreateActivity.this.getMessagesController().getUser(l);
                    if (user == null) {
                        return;
                    }
                    if (user.bot) {
                        string3 = LocaleController.getString("Bot", 2131624715);
                    } else if (user.contact) {
                        string3 = LocaleController.getString("FilterContact", 2131625885);
                    } else {
                        string3 = LocaleController.getString("FilterNonContact", 2131625915);
                    }
                    userCell.setData(user, null, string3, 0, z3);
                    return;
                }
                TLRPC$Chat chat = FilterCreateActivity.this.getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (chat == null) {
                    return;
                }
                int i2 = chat.participants_count;
                if (i2 != 0) {
                    string2 = LocaleController.formatPluralString("Members", i2, new Object[0]);
                } else if (TextUtils.isEmpty(chat.username)) {
                    if (ChatObject.isChannel(chat) && !chat.megagroup) {
                        string2 = LocaleController.getString("ChannelPrivate", 2131624959);
                    } else {
                        string2 = LocaleController.getString("MegaPrivate", 2131626633);
                    }
                } else if (ChatObject.isChannel(chat) && !chat.megagroup) {
                    string2 = LocaleController.getString("ChannelPublic", 2131624962);
                } else {
                    string2 = LocaleController.getString("MegaPublic", 2131626636);
                }
                userCell.setData(chat, null, string2, 0, z3);
                return;
            }
            l = (Long) FilterCreateActivity.this.newAlwaysShow.get(i - FilterCreateActivity.this.includeStartRow);
            if (FilterCreateActivity.this.includeShowMoreRow == -1) {
            }
            boolean z32 = z2;
            if (l.longValue() <= 0) {
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == FilterCreateActivity.this.includeHeaderRow || i == FilterCreateActivity.this.excludeHeaderRow) {
                return 0;
            }
            if (i >= FilterCreateActivity.this.includeStartRow && i < FilterCreateActivity.this.includeEndRow) {
                return 1;
            }
            if ((i >= FilterCreateActivity.this.excludeStartRow && i < FilterCreateActivity.this.excludeEndRow) || i == FilterCreateActivity.this.includeContactsRow || i == FilterCreateActivity.this.includeNonContactsRow || i == FilterCreateActivity.this.includeGroupsRow || i == FilterCreateActivity.this.includeChannelsRow || i == FilterCreateActivity.this.includeBotsRow || i == FilterCreateActivity.this.excludeReadRow || i == FilterCreateActivity.this.excludeArchivedRow || i == FilterCreateActivity.this.excludeMutedRow) {
                return 1;
            }
            if (i == FilterCreateActivity.this.nameRow) {
                return 2;
            }
            if (i == FilterCreateActivity.this.nameSectionRow || i == FilterCreateActivity.this.namePreSectionRow || i == FilterCreateActivity.this.removeSectionRow) {
                return 3;
            }
            if (i == FilterCreateActivity.this.imageRow) {
                return 5;
            }
            return (i == FilterCreateActivity.this.includeSectionRow || i == FilterCreateActivity.this.excludeSectionRow) ? 6 : 4;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        FilterCreateActivity$$ExternalSyntheticLambda11 filterCreateActivity$$ExternalSyntheticLambda11 = new FilterCreateActivity$$ExternalSyntheticLambda11(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCell.class, PollEditTextCell.class, UserCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"ImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{UserCell.class}, new String[]{"adminTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "profile_creatorIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, filterCreateActivity$$ExternalSyntheticLambda11, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, filterCreateActivity$$ExternalSyntheticLambda11, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, filterCreateActivity$$ExternalSyntheticLambda11, "avatar_backgroundPink"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$14() {
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
