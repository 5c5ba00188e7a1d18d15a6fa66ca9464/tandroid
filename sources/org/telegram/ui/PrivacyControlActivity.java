package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.PrivacyControlActivity;

/* loaded from: classes4.dex */
public class PrivacyControlActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ImageUpdater.ImageUpdaterDelegate {
    private int alwaysShareRow;
    private TLRPC.PhotoSize avatarForRest;
    private TLRPC.Photo avatarForRestPhoto;
    private RLottieDrawable cameraDrawable;
    private ArrayList currentMinus;
    private int currentPhotoForRestRow;
    private ArrayList currentPlus;
    private final boolean[] currentPlusPremium;
    private boolean currentReadValue;
    private int currentSubType;
    private int currentType;
    private int detailRow;
    private View doneButton;
    private int everybodyRow;
    ImageUpdater imageUpdater;
    private ArrayList initialMinus;
    private ArrayList initialPlus;
    private final boolean[] initialPlusPremium;
    private int initialRulesSubType;
    private int initialRulesType;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private MessageCell messageCell;
    private int messageRow;
    private int myContactsRow;
    private int neverShareRow;
    private int nobodyRow;
    private BackupImageView oldAvatarView;
    private TextCell oldPhotoCell;
    private int p2pDetailRow;
    private int p2pRow;
    private int p2pSectionRow;
    private int phoneContactsRow;
    private int phoneDetailRow;
    private int phoneEverybodyRow;
    private int phoneSectionRow;
    private int photoForRestDescriptionRow;
    private int photoForRestRow;
    private boolean prevSubtypeContacts;
    private int readDetailRow;
    private int readPremiumDetailRow;
    private int readPremiumRow;
    private int readRow;
    private int rowCount;
    private int rulesType;
    private int sectionRow;
    private boolean selectedReadValue;
    private TextCell setAvatarCell;
    private int setBirthdayRow;
    private int shakeDp;
    private int shareDetailRow;
    private int shareSectionRow;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class DiffCallback extends DiffUtil.Callback {
        SparseIntArray newPositionToItem;
        SparseIntArray oldPositionToItem;
        int oldRowCount;

        private DiffCallback() {
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return areItemsTheSame(i, i2);
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            int i3 = this.oldPositionToItem.get(i, -1);
            return i3 == this.newPositionToItem.get(i2, -1) && i3 >= 0;
        }

        public void fillPositions(SparseIntArray sparseIntArray) {
            sparseIntArray.clear();
            put(1, PrivacyControlActivity.this.messageRow, sparseIntArray);
            put(2, PrivacyControlActivity.this.sectionRow, sparseIntArray);
            put(3, PrivacyControlActivity.this.everybodyRow, sparseIntArray);
            put(4, PrivacyControlActivity.this.myContactsRow, sparseIntArray);
            put(5, PrivacyControlActivity.this.nobodyRow, sparseIntArray);
            put(6, PrivacyControlActivity.this.detailRow, sparseIntArray);
            put(7, PrivacyControlActivity.this.shareSectionRow, sparseIntArray);
            put(8, PrivacyControlActivity.this.alwaysShareRow, sparseIntArray);
            put(9, PrivacyControlActivity.this.neverShareRow, sparseIntArray);
            put(10, PrivacyControlActivity.this.shareDetailRow, sparseIntArray);
            put(11, PrivacyControlActivity.this.phoneSectionRow, sparseIntArray);
            put(12, PrivacyControlActivity.this.phoneEverybodyRow, sparseIntArray);
            put(13, PrivacyControlActivity.this.phoneContactsRow, sparseIntArray);
            put(14, PrivacyControlActivity.this.phoneDetailRow, sparseIntArray);
            put(15, PrivacyControlActivity.this.photoForRestRow, sparseIntArray);
            put(16, PrivacyControlActivity.this.currentPhotoForRestRow, sparseIntArray);
            put(17, PrivacyControlActivity.this.photoForRestDescriptionRow, sparseIntArray);
            put(18, PrivacyControlActivity.this.p2pSectionRow, sparseIntArray);
            put(19, PrivacyControlActivity.this.p2pRow, sparseIntArray);
            put(20, PrivacyControlActivity.this.p2pDetailRow, sparseIntArray);
            put(21, PrivacyControlActivity.this.readRow, sparseIntArray);
            put(22, PrivacyControlActivity.this.readDetailRow, sparseIntArray);
            put(23, PrivacyControlActivity.this.readPremiumRow, sparseIntArray);
            put(24, PrivacyControlActivity.this.readPremiumDetailRow, sparseIntArray);
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return PrivacyControlActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.oldRowCount;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        private int getUsersCount(ArrayList arrayList) {
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                long longValue = ((Long) arrayList.get(i2)).longValue();
                if (longValue > 0) {
                    i++;
                } else {
                    TLRPC.Chat chat = PrivacyControlActivity.this.getMessagesController().getChat(Long.valueOf(-longValue));
                    if (chat != null) {
                        i += chat.participants_count;
                    }
                }
            }
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0() {
            PrivacyControlActivity.this.presentFragment(new PremiumPreviewFragment("noncontacts"));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(TLObject tLObject, TLRPC.UserFull userFull, TLRPC.TL_birthday tL_birthday, TLRPC.TL_error tL_error) {
            Bulletin createSimpleBulletin;
            String str;
            if (tLObject instanceof TLRPC.TL_boolTrue) {
                createSimpleBulletin = BulletinFactory.of(PrivacyControlActivity.this).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.PrivacyBirthdaySetDone)).setDuration(5000);
            } else {
                if (userFull != null) {
                    int i = userFull.flags2;
                    userFull.flags2 = tL_birthday == null ? i & (-33) : i | 32;
                    userFull.birthday = tL_birthday;
                    PrivacyControlActivity.this.getMessagesStorage().updateUserInfo(userFull, false);
                }
                if (tL_error != null && (str = tL_error.text) != null && str.startsWith("FLOOD_WAIT_")) {
                    if (PrivacyControlActivity.this.getContext() != null) {
                        PrivacyControlActivity privacyControlActivity = PrivacyControlActivity.this;
                        privacyControlActivity.showDialog(new AlertDialog.Builder(privacyControlActivity.getContext(), ((BaseFragment) PrivacyControlActivity.this).resourceProvider).setTitle(LocaleController.getString(R.string.PrivacyBirthdayTooOftenTitle)).setMessage(LocaleController.getString(R.string.PrivacyBirthdayTooOftenMessage)).setPositiveButton(LocaleController.getString(R.string.OK), null).create());
                        return;
                    }
                    return;
                }
                createSimpleBulletin = BulletinFactory.of(PrivacyControlActivity.this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UnknownError));
            }
            createSimpleBulletin.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$2(final TLRPC.UserFull userFull, final TLRPC.TL_birthday tL_birthday, final TLObject tLObject, final TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$ListAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyControlActivity.ListAdapter.this.lambda$onBindViewHolder$1(tLObject, userFull, tL_birthday, tL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$3(TLRPC.TL_birthday tL_birthday) {
            TLRPC.TL_account_updateBirthday tL_account_updateBirthday = new TLRPC.TL_account_updateBirthday();
            tL_account_updateBirthday.flags |= 1;
            tL_account_updateBirthday.birthday = tL_birthday;
            final TLRPC.UserFull userFull = PrivacyControlActivity.this.getMessagesController().getUserFull(PrivacyControlActivity.this.getUserConfig().getClientUserId());
            final TLRPC.TL_birthday tL_birthday2 = userFull != null ? userFull.birthday : null;
            if (userFull != null) {
                userFull.flags2 |= 32;
                userFull.birthday = tL_birthday;
                PrivacyControlActivity.this.getMessagesStorage().updateUserInfo(userFull, false);
            }
            PrivacyControlActivity.this.getMessagesController().invalidateContentSettings();
            PrivacyControlActivity.this.getConnectionsManager().sendRequest(tL_account_updateBirthday, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$ListAdapter$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PrivacyControlActivity.ListAdapter.this.lambda$onBindViewHolder$2(userFull, tL_birthday2, tLObject, tL_error);
                }
            }, 1024);
            MessagesController.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).removeSuggestion(0L, "BIRTHDAY_SETUP");
            NotificationCenter.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.premiumPromoUpdated, new Object[0]);
            PrivacyControlActivity.this.updateRows(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$4() {
            PrivacyControlActivity privacyControlActivity = PrivacyControlActivity.this;
            privacyControlActivity.showDialog(AlertsCreator.createBirthdayPickerDialog(privacyControlActivity.getContext(), LocaleController.getString(R.string.EditProfileBirthdayTitle), LocaleController.getString(R.string.EditProfileBirthdayButton), null, new Utilities.Callback() { // from class: org.telegram.ui.PrivacyControlActivity$ListAdapter$$ExternalSyntheticLambda2
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PrivacyControlActivity.ListAdapter.this.lambda$onBindViewHolder$3((TLRPC.TL_birthday) obj);
                }
            }, null, PrivacyControlActivity.this.getResourceProvider()).create());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PrivacyControlActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PrivacyControlActivity.this.alwaysShareRow || i == PrivacyControlActivity.this.neverShareRow || i == PrivacyControlActivity.this.p2pRow || i == PrivacyControlActivity.this.readPremiumRow) {
                return 0;
            }
            if (i == PrivacyControlActivity.this.shareDetailRow || i == PrivacyControlActivity.this.detailRow || i == PrivacyControlActivity.this.p2pDetailRow || i == PrivacyControlActivity.this.photoForRestDescriptionRow || i == PrivacyControlActivity.this.readDetailRow || i == PrivacyControlActivity.this.readPremiumDetailRow || i == PrivacyControlActivity.this.setBirthdayRow) {
                return 1;
            }
            if (i == PrivacyControlActivity.this.sectionRow || i == PrivacyControlActivity.this.shareSectionRow || i == PrivacyControlActivity.this.p2pSectionRow || i == PrivacyControlActivity.this.phoneSectionRow) {
                return 2;
            }
            if (i == PrivacyControlActivity.this.everybodyRow || i == PrivacyControlActivity.this.myContactsRow || i == PrivacyControlActivity.this.nobodyRow || i == PrivacyControlActivity.this.phoneEverybodyRow || i == PrivacyControlActivity.this.phoneContactsRow) {
                return 3;
            }
            if (i == PrivacyControlActivity.this.messageRow) {
                return 4;
            }
            if (i == PrivacyControlActivity.this.phoneDetailRow) {
                return 5;
            }
            if (i == PrivacyControlActivity.this.photoForRestRow) {
                return 6;
            }
            if (i == PrivacyControlActivity.this.currentPhotoForRestRow) {
                return 7;
            }
            return i == PrivacyControlActivity.this.readRow ? 8 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PrivacyControlActivity.this.nobodyRow || adapterPosition == PrivacyControlActivity.this.everybodyRow || adapterPosition == PrivacyControlActivity.this.myContactsRow || adapterPosition == PrivacyControlActivity.this.neverShareRow || adapterPosition == PrivacyControlActivity.this.alwaysShareRow || (adapterPosition == PrivacyControlActivity.this.p2pRow && !ContactsController.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).getLoadingPrivacyInfo(3)) || adapterPosition == PrivacyControlActivity.this.currentPhotoForRestRow || adapterPosition == PrivacyControlActivity.this.photoForRestDescriptionRow || adapterPosition == PrivacyControlActivity.this.photoForRestRow || adapterPosition == PrivacyControlActivity.this.readRow || adapterPosition == PrivacyControlActivity.this.readPremiumRow;
        }

        /* JADX WARN: Code restructure failed: missing block: B:156:0x02c4, code lost:
        
            if (r13.this$0.getUserConfig().isPremium() != false) goto L160;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0055, code lost:
        
            if (r13.this$0.currentSubType == 1) goto L99;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x01c7, code lost:
        
            r0 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:208:0x0467, code lost:
        
            if (r13.this$0.rulesType != 0) goto L149;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x01c8, code lost:
        
            r14.setText(r15, r0, false);
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:249:0x049e, code lost:
        
            if (r13.this$0.readPremiumDetailRow == (-1)) goto L149;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x006d, code lost:
        
            if (r13.this$0.currentSubType == 0) goto L37;
         */
        /* JADX WARN: Code restructure failed: missing block: B:280:0x057f, code lost:
        
            if (r13.this$0.neverShareRow != (-1)) goto L294;
         */
        /* JADX WARN: Code restructure failed: missing block: B:281:0x0591, code lost:
        
            r0 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:282:0x0592, code lost:
        
            r14.setTextAndValue(r1, r15, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:283:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:285:0x058e, code lost:
        
            if (r13.this$0.neverShareRow != (-1)) goto L294;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x009d, code lost:
        
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x008c, code lost:
        
            if (r13.this$0.currentType == 0) goto L37;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x009b, code lost:
        
            if (r13.this$0.currentType == 0) goto L37;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0113, code lost:
        
            if (r13.this$0.nobodyRow != (-1)) goto L78;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x0152, code lost:
        
            r0 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x0153, code lost:
        
            r14.setText(r15, r1, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x0135, code lost:
        
            if (r13.this$0.nobodyRow != (-1)) goto L78;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x014f, code lost:
        
            if (r13.this$0.nobodyRow != (-1)) goto L78;
         */
        /* JADX WARN: Code restructure failed: missing block: B:85:0x01b5, code lost:
        
            if (r13.this$0.currentType == 1) goto L99;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x01c4, code lost:
        
            if (r13.this$0.currentType == 1) goto L99;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:148:0x04c2  */
        /* JADX WARN: Removed duplicated region for block: B:150:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:207:0x0461  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            int i2;
            String string2;
            CharSequence string3;
            int i3;
            int i4;
            CharSequence replaceTags;
            int i5;
            String str;
            int i6;
            String string4;
            String string5;
            boolean z;
            String string6;
            boolean z2 = true;
            int itemViewType = viewHolder.getItemViewType();
            int i7 = 0;
            r2 = false;
            r2 = false;
            boolean z3 = false;
            i7 = 0;
            if (itemViewType == 8) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == PrivacyControlActivity.this.readRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.HideReadTime), PrivacyControlActivity.this.selectedReadValue, false);
                    return;
                }
                return;
            }
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                textSettingsCell.setTextColor(PrivacyControlActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                if (i != PrivacyControlActivity.this.alwaysShareRow) {
                    if (i == PrivacyControlActivity.this.neverShareRow) {
                        string = PrivacyControlActivity.this.currentMinus.size() != 0 ? LocaleController.formatPluralString("Users", getUsersCount(PrivacyControlActivity.this.currentMinus), new Object[0]) : LocaleController.getString(R.string.EmpryUsersPlaceholder);
                        i2 = (PrivacyControlActivity.this.rulesType == 0 || PrivacyControlActivity.this.rulesType == 4 || PrivacyControlActivity.this.rulesType == 9) ? R.string.NeverShareWith : R.string.NeverAllow;
                    } else {
                        if (i != PrivacyControlActivity.this.p2pRow) {
                            if (i == PrivacyControlActivity.this.readPremiumRow) {
                                textSettingsCell.setText(LocaleController.getString(PrivacyControlActivity.this.getUserConfig().isPremium() ? R.string.PrivacyLastSeenPremiumForPremium : R.string.PrivacyLastSeenPremium), false);
                                textSettingsCell.setTextColor(PrivacyControlActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText));
                                return;
                            }
                            return;
                        }
                        string = ContactsController.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).getLoadingPrivacyInfo(3) ? LocaleController.getString(R.string.Loading) : PrivacySettingsActivity.formatRulesString(PrivacyControlActivity.this.getAccountInstance(), 3);
                        i2 = R.string.PrivacyP2P2;
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString(i2), string, false);
                    return;
                }
                String formatPluralString = PrivacyControlActivity.this.currentPlus.size() != 0 ? LocaleController.formatPluralString("Users", getUsersCount(PrivacyControlActivity.this.currentPlus), new Object[0]) : LocaleController.getString(R.string.EmpryUsersPlaceholder);
                if (PrivacyControlActivity.this.currentPlusPremium[PrivacyControlActivity.this.currentType == 2 ? (char) 0 : (char) 1]) {
                    formatPluralString = (PrivacyControlActivity.this.currentPlus == null || PrivacyControlActivity.this.currentPlus.isEmpty()) ? LocaleController.formatString(R.string.PrivacyPremium, new Object[0]) : LocaleController.formatString(R.string.PrivacyPremiumAnd, formatPluralString);
                }
                if (PrivacyControlActivity.this.rulesType == 0 || PrivacyControlActivity.this.rulesType == 4 || PrivacyControlActivity.this.rulesType == 9) {
                    string2 = LocaleController.getString(R.string.AlwaysShareWith);
                } else {
                    string2 = LocaleController.getString(R.string.AlwaysAllow);
                }
            } else {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                        if (i == PrivacyControlActivity.this.sectionRow) {
                            i6 = PrivacyControlActivity.this.rulesType == 6 ? R.string.PrivacyPhoneTitle : PrivacyControlActivity.this.rulesType == 5 ? R.string.PrivacyForwardsTitle : PrivacyControlActivity.this.rulesType == 4 ? R.string.PrivacyProfilePhotoTitle : PrivacyControlActivity.this.rulesType == 9 ? R.string.PrivacyBioTitle : PrivacyControlActivity.this.rulesType == 3 ? R.string.P2PEnabledWith : PrivacyControlActivity.this.rulesType == 2 ? R.string.WhoCanCallMe : PrivacyControlActivity.this.rulesType == 1 ? R.string.WhoCanAddMe : PrivacyControlActivity.this.rulesType == 8 ? R.string.PrivacyVoiceMessagesTitle : PrivacyControlActivity.this.rulesType == 10 ? R.string.PrivacyMessagesTitle : PrivacyControlActivity.this.rulesType == 11 ? R.string.PrivacyBirthdayTitle : R.string.LastSeenTitle;
                        } else if (i == PrivacyControlActivity.this.shareSectionRow) {
                            i6 = R.string.AddExceptions;
                        } else if (i == PrivacyControlActivity.this.p2pSectionRow) {
                            i6 = R.string.PrivacyP2PHeader;
                        } else if (i != PrivacyControlActivity.this.phoneSectionRow) {
                            return;
                        } else {
                            i6 = R.string.PrivacyPhoneTitle2;
                        }
                        headerCell.setText(LocaleController.getString(i6));
                        return;
                    }
                    if (itemViewType != 3) {
                        return;
                    }
                    RadioCell radioCell = (RadioCell) viewHolder.itemView;
                    radioCell.setRadioIcon(null);
                    if (i == PrivacyControlActivity.this.everybodyRow || i == PrivacyControlActivity.this.myContactsRow || i == PrivacyControlActivity.this.nobodyRow) {
                        if (i == PrivacyControlActivity.this.everybodyRow) {
                            if (PrivacyControlActivity.this.rulesType == 3) {
                                string6 = LocaleController.getString(R.string.P2PEverybody);
                            } else {
                                string6 = LocaleController.getString(R.string.LastSeenEverybody);
                            }
                        } else if (i == PrivacyControlActivity.this.myContactsRow) {
                            if ((PrivacyControlActivity.this.rulesType == 8 && !PrivacyControlActivity.this.getUserConfig().isPremium()) || (PrivacyControlActivity.this.rulesType == 10 && !PrivacyControlActivity.this.getMessagesController().newNoncontactPeersRequirePremiumWithoutOwnpremium && !PrivacyControlActivity.this.getUserConfig().isPremium())) {
                                radioCell.setRadioIcon(PrivacyControlActivity.this.getContext().getResources().getDrawable(R.drawable.mini_switch_lock).mutate());
                            }
                            if (PrivacyControlActivity.this.rulesType == 3) {
                                string5 = LocaleController.getString(R.string.P2PContacts);
                                z = PrivacyControlActivity.this.currentType == 2;
                            } else if (PrivacyControlActivity.this.rulesType == 10) {
                                string5 = LocaleController.getString(R.string.PrivacyMessagesContactsAndPremium);
                                z = PrivacyControlActivity.this.currentType == 2;
                            } else {
                                string5 = LocaleController.getString(R.string.LastSeenContacts);
                                z = PrivacyControlActivity.this.currentType == 2;
                            }
                        } else {
                            if ((PrivacyControlActivity.this.rulesType == 8 && !PrivacyControlActivity.this.getUserConfig().isPremium()) || (PrivacyControlActivity.this.rulesType == 10 && !PrivacyControlActivity.this.getMessagesController().newNoncontactPeersRequirePremiumWithoutOwnpremium && !PrivacyControlActivity.this.getUserConfig().isPremium())) {
                                radioCell.setRadioIcon(PrivacyControlActivity.this.getContext().getResources().getDrawable(R.drawable.mini_switch_lock).mutate());
                            }
                            if (PrivacyControlActivity.this.rulesType == 3) {
                                string4 = LocaleController.getString(R.string.P2PNobody);
                            } else {
                                string4 = LocaleController.getString(R.string.LastSeenNobody);
                            }
                        }
                    } else if (i == PrivacyControlActivity.this.phoneContactsRow) {
                        string4 = LocaleController.getString(R.string.LastSeenContacts);
                    } else if (i != PrivacyControlActivity.this.phoneEverybodyRow) {
                        return;
                    } else {
                        string6 = LocaleController.getString(R.string.LastSeenEverybody);
                    }
                    radioCell.setText(string6, z3, true);
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != PrivacyControlActivity.this.detailRow || PrivacyControlActivity.this.rulesType != 10) {
                    if (i == PrivacyControlActivity.this.detailRow && PrivacyControlActivity.this.rulesType == 8) {
                        textInfoPrivacyCell.setText(LocaleController.getString(R.string.PrivacyVoiceMessagesInfo));
                    } else {
                        if (i == PrivacyControlActivity.this.setBirthdayRow) {
                            textInfoPrivacyCell.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.PrivacyBirthdaySet), new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$ListAdapter$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    PrivacyControlActivity.ListAdapter.this.lambda$onBindViewHolder$4();
                                }
                            }), true));
                        } else if (i == PrivacyControlActivity.this.detailRow) {
                            if (PrivacyControlActivity.this.rulesType == 6) {
                                PrivacyControlActivity privacyControlActivity = PrivacyControlActivity.this;
                                if (privacyControlActivity.prevSubtypeContacts = privacyControlActivity.currentType == 1 && PrivacyControlActivity.this.currentSubType == 1) {
                                    i5 = R.string.PrivacyPhoneInfo3;
                                } else {
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                    final String format = String.format(Locale.ENGLISH, "https://t.me/+%s", PrivacyControlActivity.this.getUserConfig().getClientPhone());
                                    SpannableString spannableString = new SpannableString(format);
                                    spannableString.setSpan(new ClickableSpan() { // from class: org.telegram.ui.PrivacyControlActivity.ListAdapter.2
                                        @Override // android.text.style.ClickableSpan
                                        public void onClick(View view) {
                                            ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", format));
                                            BulletinFactory.of(PrivacyControlActivity.this).createCopyLinkBulletin(LocaleController.getString(R.string.LinkCopied), PrivacyControlActivity.this.getResourceProvider()).show();
                                        }
                                    }, 0, format.length(), 33);
                                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.PrivacyPhoneInfo)).append((CharSequence) "\n\n").append((CharSequence) LocaleController.getString(R.string.PrivacyPhoneInfo4)).append((CharSequence) "\n").append((CharSequence) spannableString);
                                    str = spannableStringBuilder;
                                    textInfoPrivacyCell.setText(str);
                                }
                            } else {
                                i5 = PrivacyControlActivity.this.rulesType == 5 ? R.string.PrivacyForwardsInfo : PrivacyControlActivity.this.rulesType == 4 ? R.string.PrivacyProfilePhotoInfo : PrivacyControlActivity.this.rulesType == 9 ? R.string.PrivacyBioInfo3 : PrivacyControlActivity.this.rulesType == 11 ? R.string.PrivacyBirthdayInfo : PrivacyControlActivity.this.rulesType == 3 ? R.string.PrivacyCallsP2PHelp : PrivacyControlActivity.this.rulesType == 2 ? R.string.WhoCanCallMeInfo : PrivacyControlActivity.this.rulesType == 1 ? R.string.WhoCanAddMeInfo : R.string.CustomHelp;
                            }
                            str = LocaleController.getString(i5);
                            textInfoPrivacyCell.setText(str);
                        } else if (i == PrivacyControlActivity.this.shareDetailRow) {
                            if (PrivacyControlActivity.this.rulesType == 6) {
                                i3 = R.string.PrivacyPhoneInfo2;
                            } else if (PrivacyControlActivity.this.rulesType == 5) {
                                i3 = R.string.PrivacyForwardsInfo2;
                            } else if (PrivacyControlActivity.this.rulesType == 4) {
                                if (PrivacyControlActivity.this.currentType == 2) {
                                    i4 = R.string.PrivacyProfilePhotoInfo5;
                                } else if (PrivacyControlActivity.this.currentType == 0) {
                                    i4 = R.string.PrivacyProfilePhotoInfo3;
                                } else {
                                    i3 = R.string.PrivacyProfilePhotoInfo4;
                                }
                                replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(i4));
                                textInfoPrivacyCell.setText(replaceTags);
                                if (PrivacyControlActivity.this.rulesType != 2) {
                                }
                            } else {
                                i3 = PrivacyControlActivity.this.rulesType == 3 ? R.string.CustomP2PInfo : PrivacyControlActivity.this.rulesType == 9 ? R.string.PrivacyBioInfo : PrivacyControlActivity.this.rulesType == 11 ? R.string.PrivacyBirthdayInfo3 : PrivacyControlActivity.this.rulesType == 2 ? R.string.CustomCallInfo : PrivacyControlActivity.this.rulesType == 1 ? R.string.CustomShareInfo : PrivacyControlActivity.this.rulesType == 8 ? R.string.PrivacyVoiceMessagesInfo2 : R.string.CustomShareSettingsHelp;
                            }
                            replaceTags = LocaleController.getString(i3);
                            textInfoPrivacyCell.setText(replaceTags);
                            if (PrivacyControlActivity.this.rulesType != 2) {
                            }
                        } else {
                            if (i != PrivacyControlActivity.this.p2pDetailRow) {
                                if (i == PrivacyControlActivity.this.photoForRestDescriptionRow) {
                                    textInfoPrivacyCell.setText(LocaleController.getString(R.string.PhotoForRestDescription));
                                } else if (i == PrivacyControlActivity.this.readDetailRow) {
                                    textInfoPrivacyCell.setText(LocaleController.getString(R.string.HideReadTimeInfo));
                                } else if (i == PrivacyControlActivity.this.readPremiumDetailRow) {
                                    string3 = LocaleController.getString(PrivacyControlActivity.this.getUserConfig().isPremium() ? R.string.PrivacyLastSeenPremiumInfoForPremium : R.string.PrivacyLastSeenPremiumInfo);
                                }
                            }
                            i7 = R.drawable.greydivider_bottom;
                        }
                        i7 = R.drawable.greydivider;
                    }
                    if (i7 == 0) {
                        CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, i7, Theme.key_windowBackgroundGrayShadow));
                        combinedDrawable.setFullsize(true);
                        textInfoPrivacyCell.setBackgroundDrawable(combinedDrawable);
                        return;
                    }
                    return;
                }
                string3 = AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.PrivacyMessagesInfo), new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PrivacyControlActivity.ListAdapter.this.lambda$onBindViewHolder$0();
                    }
                });
                textInfoPrivacyCell.setText(string3);
                i7 = R.drawable.greydivider_bottom;
                if (i7 == 0) {
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            BackupImageView backupImageView;
            ImageLocation forLocal;
            int i2;
            switch (i) {
                case 0:
                    textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    textSettingsCell = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 2:
                    textSettingsCell = new HeaderCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    textSettingsCell = new RadioCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    textSettingsCell = PrivacyControlActivity.this.messageCell;
                    break;
                case 5:
                default:
                    textSettingsCell = new ShadowSectionCell(this.mContext);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable.setFullsize(true);
                    textSettingsCell.setBackgroundDrawable(combinedDrawable);
                    break;
                case 6:
                    PrivacyControlActivity.this.setAvatarCell = new TextCell(PrivacyControlActivity.this.getContext());
                    if (PrivacyControlActivity.this.avatarForRest == null) {
                        PrivacyControlActivity.this.setAvatarCell.setTextAndIcon((CharSequence) LocaleController.formatString("SetPhotoForRest", R.string.SetPhotoForRest, new Object[0]), R.drawable.msg_addphoto, false);
                    } else {
                        PrivacyControlActivity.this.setAvatarCell.setTextAndIcon((CharSequence) LocaleController.formatString("UpdatePhotoForRest", R.string.UpdatePhotoForRest, new Object[0]), R.drawable.msg_addphoto, true);
                    }
                    PrivacyControlActivity.this.setAvatarCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    PrivacyControlActivity.this.setAvatarCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                    PrivacyControlActivity privacyControlActivity = PrivacyControlActivity.this;
                    int i3 = R.raw.camera_outline;
                    privacyControlActivity.cameraDrawable = new RLottieDrawable(i3, "" + i3, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f), false, null);
                    PrivacyControlActivity.this.setAvatarCell.imageView.setTranslationX((float) (-AndroidUtilities.dp(8.0f)));
                    PrivacyControlActivity.this.setAvatarCell.imageView.setAnimation(PrivacyControlActivity.this.cameraDrawable);
                    PrivacyControlActivity.this.setAvatarCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    textSettingsCell = PrivacyControlActivity.this.setAvatarCell;
                    break;
                case 7:
                    PrivacyControlActivity.this.oldAvatarView = new BackupImageView(PrivacyControlActivity.this.getContext());
                    PrivacyControlActivity.this.oldPhotoCell = new TextCell(PrivacyControlActivity.this.getContext()) { // from class: org.telegram.ui.PrivacyControlActivity.ListAdapter.1
                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                        public void onLayout(boolean z, int i4, int i5, int i6, int i7) {
                            super.onLayout(z, i4, i5, i6, i7);
                            int dp = AndroidUtilities.dp(21.0f);
                            int measuredHeight = (getMeasuredHeight() - PrivacyControlActivity.this.oldAvatarView.getMeasuredHeight()) / 2;
                            PrivacyControlActivity.this.oldAvatarView.layout(dp, measuredHeight, PrivacyControlActivity.this.oldAvatarView.getMeasuredWidth() + dp, PrivacyControlActivity.this.oldAvatarView.getMeasuredHeight() + measuredHeight);
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.View
                        public void onMeasure(int i4, int i5) {
                            super.onMeasure(i4, i5);
                            PrivacyControlActivity.this.oldAvatarView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
                            PrivacyControlActivity.this.oldAvatarView.setRoundRadius(AndroidUtilities.dp(30.0f));
                        }
                    };
                    if (PrivacyControlActivity.this.avatarForRest != null) {
                        if (PrivacyControlActivity.this.avatarForRestPhoto != null) {
                            backupImageView = PrivacyControlActivity.this.oldAvatarView;
                            forLocal = ImageLocation.getForPhoto(PrivacyControlActivity.this.avatarForRest, PrivacyControlActivity.this.avatarForRestPhoto);
                            i2 = ((BaseFragment) PrivacyControlActivity.this).currentAccount;
                        } else {
                            backupImageView = PrivacyControlActivity.this.oldAvatarView;
                            forLocal = ImageLocation.getForLocal(PrivacyControlActivity.this.avatarForRest.location);
                            i2 = ((BaseFragment) PrivacyControlActivity.this).currentAccount;
                        }
                        backupImageView.setImage(forLocal, "50_50", (Drawable) null, UserConfig.getInstance(i2).getCurrentUser());
                    }
                    PrivacyControlActivity.this.oldPhotoCell.addView(PrivacyControlActivity.this.oldAvatarView, LayoutHelper.createFrame(30, 30.0f, 16, 21.0f, 0.0f, 21.0f, 0.0f));
                    PrivacyControlActivity.this.oldPhotoCell.setText(LocaleController.getString(R.string.RemovePublicPhoto), false);
                    PrivacyControlActivity.this.oldPhotoCell.getImageView().setVisibility(0);
                    PrivacyControlActivity.this.oldPhotoCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    TextCell textCell = PrivacyControlActivity.this.oldPhotoCell;
                    int i4 = Theme.key_text_RedRegular;
                    textCell.setColors(i4, i4);
                    PrivacyControlActivity.this.oldPhotoCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    textSettingsCell = PrivacyControlActivity.this.oldPhotoCell;
                    break;
                case 8:
                    textSettingsCell = new TextCheckCell(this.mContext, ((BaseFragment) PrivacyControlActivity.this).resourceProvider);
                    textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new RecyclerListView.Holder(textSettingsCell);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class MessageCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
        private ChatMessageCell cell;
        private HintView hintView;
        private final Runnable invalidateRunnable;
        private MessageObject messageObject;
        private Drawable shadowDrawable;

        public MessageCell(Context context) {
            super(context);
            this.invalidateRunnable = new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$MessageCell$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyControlActivity.MessageCell.this.invalidate();
                }
            };
            setWillNotDraw(false);
            setClipToPadding(false);
            this.shadowDrawable = Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
            setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            TLRPC.User user = MessagesController.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).getUser(Long.valueOf(UserConfig.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).getClientUserId()));
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.message = LocaleController.getString(R.string.PrivacyForwardsMessageLine);
            tL_message.date = currentTimeMillis - 3540;
            tL_message.dialog_id = 1L;
            tL_message.flags = NotificationCenter.webRtcSpeakerAmplitudeEvent;
            tL_message.from_id = new TLRPC.TL_peerUser();
            tL_message.id = 1;
            TLRPC.TL_messageFwdHeader tL_messageFwdHeader = new TLRPC.TL_messageFwdHeader();
            tL_message.fwd_from = tL_messageFwdHeader;
            tL_messageFwdHeader.from_name = ContactsController.formatName(user.first_name, user.last_name);
            tL_message.media = new TLRPC.TL_messageMediaEmpty();
            tL_message.out = false;
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_message.peer_id = tL_peerUser;
            tL_peerUser.user_id = UserConfig.getInstance(((BaseFragment) PrivacyControlActivity.this).currentAccount).getClientUserId();
            MessageObject messageObject = new MessageObject(((BaseFragment) PrivacyControlActivity.this).currentAccount, tL_message, true, false);
            this.messageObject = messageObject;
            messageObject.eventId = 1L;
            messageObject.resetLayout();
            ChatMessageCell chatMessageCell = new ChatMessageCell(context, ((BaseFragment) PrivacyControlActivity.this).currentAccount);
            this.cell = chatMessageCell;
            chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() { // from class: org.telegram.ui.PrivacyControlActivity.MessageCell.1
                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean canDrawOutboundsContent() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean canPerformActions() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canPerformActions(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell2, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell2, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell2, keyboardButton);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, int i, float f, float f2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell2, chat, i, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC.User user2, float f, float f2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell2, user2, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressAboutRevenueSharingAds() {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAboutRevenueSharingAds(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBoostCounter(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell2, keyboardButton);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, int i, float f, float f2, boolean z) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, chat, i, f, f2, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, boolean z) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell2, chat, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressCodeCopy(ChatMessageCell chatMessageCell2, MessageObject.TextLayoutBlock textLayoutBlock) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCodeCopy(this, chatMessageCell2, textLayoutBlock);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressDialogButton(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressDialogButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressEffect(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, keyboardButton);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheck(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell2, int i, int i2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheckWhat(this, chatMessageCell2, i, i2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell2, ImageReceiver imageReceiver, TLRPC.MessageExtendedMedia messageExtendedMedia, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGroupImage(this, chatMessageCell2, imageReceiver, messageExtendedMedia, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHiddenForward(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell2, int i) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHint(this, chatMessageCell2, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell2, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressImage(this, chatMessageCell2, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell2, int i) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressInstantButton(this, chatMessageCell2, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell2, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell2, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, reactionCount, z, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressRevealSensitiveContent(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell2, float f, float f2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredInfo(this, chatMessageCell2, f, f2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTime(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressTopicButton(ChatMessageCell chatMessageCell2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTopicButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressUrl(ChatMessageCell chatMessageCell2, CharacterStyle characterStyle, boolean z) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUrl(this, chatMessageCell2, characterStyle, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC.User user2, float f, float f2, boolean z) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, user2, f, f2, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell2, TLRPC.User user2, TLRPC.Document document) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell2, user2, document);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell2, String str) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBot(this, chatMessageCell2, str);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell2, long j) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBotNotInline(this, chatMessageCell2, j);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell2, ArrayList arrayList, int i, int i2, int i3) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressVoteButtons(this, chatMessageCell2, arrayList, i, i2, i3);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell2, TLRPC.WebPage webPage, String str, boolean z) {
                    Browser.openUrl(chatMessageCell2.getContext(), str);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void didStartVideoStream(MessageObject messageObject2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean doNotShowLoadingReply(MessageObject messageObject2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$doNotShowLoadingReply(this, messageObject2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void forceUpdate(ChatMessageCell chatMessageCell2, boolean z) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$forceUpdate(this, chatMessageCell2, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ String getAdminRank(long j) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getAdminRank(this, j);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getPinchToZoomHelper(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingLink(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getTextSelectionHelper(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean hasSelectedMessages() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$hasSelectedMessages(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void invalidateBlur() {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$invalidateBlur(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean isLandscape() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isLandscape(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean isProgressLoading(ChatMessageCell chatMessageCell2, int i) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isProgressLoading(this, chatMessageCell2, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean isReplyOrSelf() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isReplyOrSelf(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean keyboardIsOpened() {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$keyboardIsOpened(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void needOpenWebView(MessageObject messageObject2, String str, String str2, String str3, String str4, int i, int i2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needOpenWebView(this, messageObject2, str, str2, str3, str4, i, i2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean needPlayMessage(ChatMessageCell chatMessageCell2, MessageObject messageObject2, boolean z) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, chatMessageCell2, messageObject2, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void needReloadPolls() {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needReloadPolls(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void needShowPremiumBulletin(int i) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needShowPremiumBulletin(this, i);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean onAccessibilityAction(int i, Bundle bundle) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onAccessibilityAction(this, i, bundle);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void onDiceFinished() {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onDiceFinished(this);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject2) {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$setShouldNotRepeatSticker(this, messageObject2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2, boolean z) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2, z);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean shouldShowDialogButton(ChatMessageCell chatMessageCell2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowDialogButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ boolean shouldShowTopicButton(ChatMessageCell chatMessageCell2) {
                    return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this, chatMessageCell2);
                }

                @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                public /* synthetic */ void videoTimerReached() {
                    ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                }
            });
            ChatMessageCell chatMessageCell2 = this.cell;
            chatMessageCell2.isChat = false;
            chatMessageCell2.setFullyDraw(true);
            this.cell.setMessageObject(this.messageObject, null, false, false);
            addView(this.cell, LayoutHelper.createLinear(-1, -2));
            HintView hintView = new HintView(context, 1, true);
            this.hintView = hintView;
            addView(hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            this.hintView.showForMessageCell(this.cell, false);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchSetPressed(boolean z) {
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.cell.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.backgroundGradientDisposable = null;
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != null && this.backgroundDrawable != cachedWallpaperNonBlocking) {
                BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                if (disposable != null) {
                    disposable.dispose();
                    this.backgroundGradientDisposable = null;
                }
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            Drawable drawable = this.backgroundDrawable;
            if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                Drawable drawable2 = this.backgroundDrawable;
                if (drawable2 instanceof BackgroundGradientDrawable) {
                    this.backgroundGradientDisposable = ((BackgroundGradientDrawable) drawable2).drawExactBoundsSize(canvas, this);
                } else {
                    drawable2.draw(canvas);
                }
            } else if (drawable instanceof BitmapDrawable) {
                if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                    canvas.save();
                    float f = 2.0f / AndroidUtilities.density;
                    canvas.scale(f, f);
                    this.backgroundDrawable.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f), (int) Math.ceil(getMeasuredHeight() / f));
                } else {
                    int measuredHeight = getMeasuredHeight();
                    float max = Math.max(getMeasuredWidth() / this.backgroundDrawable.getIntrinsicWidth(), measuredHeight / this.backgroundDrawable.getIntrinsicHeight());
                    int ceil = (int) Math.ceil(this.backgroundDrawable.getIntrinsicWidth() * max);
                    int ceil2 = (int) Math.ceil(this.backgroundDrawable.getIntrinsicHeight() * max);
                    int measuredWidth = (getMeasuredWidth() - ceil) / 2;
                    int i = (measuredHeight - ceil2) / 2;
                    canvas.save();
                    canvas.clipRect(0, 0, ceil, getMeasuredHeight());
                    this.backgroundDrawable.setBounds(measuredWidth, i, ceil + measuredWidth, ceil2 + i);
                }
                this.backgroundDrawable.draw(canvas);
                canvas.restore();
            } else {
                super.onDraw(canvas);
            }
            this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }
    }

    public PrivacyControlActivity(int i) {
        this(i, false);
    }

    public PrivacyControlActivity(int i, boolean z) {
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        this.initialPlus = new ArrayList();
        this.initialMinus = new ArrayList();
        this.initialPlusPremium = new boolean[2];
        this.currentPlusPremium = new boolean[2];
        this.shakeDp = 4;
        this.rulesType = i;
        if (z) {
            ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
        }
        if (this.rulesType == 4) {
            ImageUpdater imageUpdater = new ImageUpdater(false, 0, true);
            this.imageUpdater = imageUpdater;
            imageUpdater.parentFragment = this;
            imageUpdater.setDelegate(this);
            TLRPC.UserFull userFull = getMessagesController().getUserFull(getUserConfig().clientUserId);
            if (!UserObject.hasFallbackPhoto(userFull) || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(userFull.fallback_photo.sizes, 1000)) == null) {
                return;
            }
            this.avatarForRest = closestPhotoSizeWithSize;
            this.avatarForRestPhoto = userFull.fallback_photo;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01f7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyCurrentPrivacySettings() {
        ArrayList<TLRPC.InputPrivacyRule> arrayList;
        TLRPC.InputPrivacyRule tL_inputPrivacyValueAllowContacts;
        int i;
        final AlertDialog alertDialog;
        TLRPC.InputUser inputUser;
        TLRPC.InputUser inputUser2;
        ArrayList<TLRPC.InputPrivacyRule> arrayList2;
        TLRPC.InputPrivacyRule tL_inputPrivacyValueAllowContacts2;
        if (this.rulesType == 10) {
            final TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings = new TLRPC.TL_account_setGlobalPrivacySettings();
            tL_account_setGlobalPrivacySettings.settings = new TLRPC.TL_globalPrivacySettings();
            final TLRPC.TL_globalPrivacySettings globalPrivacySettings = getContactsController().getGlobalPrivacySettings();
            if (globalPrivacySettings != null) {
                TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings = tL_account_setGlobalPrivacySettings.settings;
                tL_globalPrivacySettings.archive_and_mute_new_noncontact_peers = globalPrivacySettings.archive_and_mute_new_noncontact_peers;
                tL_globalPrivacySettings.keep_archived_folders = globalPrivacySettings.keep_archived_folders;
                tL_globalPrivacySettings.keep_archived_unmuted = globalPrivacySettings.keep_archived_unmuted;
                tL_globalPrivacySettings.hide_read_marks = globalPrivacySettings.hide_read_marks;
            }
            tL_account_setGlobalPrivacySettings.settings.new_noncontact_peers_require_premium = this.currentType == 2;
            getConnectionsManager().sendRequest(tL_account_setGlobalPrivacySettings, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda13
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$11(globalPrivacySettings, tL_account_setGlobalPrivacySettings, tLObject, tL_error);
                }
            });
            return;
        }
        TLRPC.TL_account_setPrivacy tL_account_setPrivacy = new TLRPC.TL_account_setPrivacy();
        int i2 = this.rulesType;
        if (i2 == 6) {
            tL_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
            if (this.currentType == 1) {
                TLRPC.TL_account_setPrivacy tL_account_setPrivacy2 = new TLRPC.TL_account_setPrivacy();
                tL_account_setPrivacy2.key = new TLRPC.TL_inputPrivacyKeyAddedByPhone();
                if (this.currentSubType == 0) {
                    arrayList2 = tL_account_setPrivacy2.rules;
                    tL_inputPrivacyValueAllowContacts2 = new TLRPC.TL_inputPrivacyValueAllowAll();
                } else {
                    arrayList2 = tL_account_setPrivacy2.rules;
                    tL_inputPrivacyValueAllowContacts2 = new TLRPC.TL_inputPrivacyValueAllowContacts();
                }
                arrayList2.add(tL_inputPrivacyValueAllowContacts2);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_setPrivacy2, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda14
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$13(tLObject, tL_error);
                    }
                }, 2);
            }
        } else {
            tL_account_setPrivacy.key = i2 == 5 ? new TLRPC.TL_inputPrivacyKeyForwards() : i2 == 4 ? new TLRPC.TL_inputPrivacyKeyProfilePhoto() : i2 == 9 ? new TLRPC.TL_inputPrivacyKeyAbout() : i2 == 3 ? new TLRPC.TL_inputPrivacyKeyPhoneP2P() : i2 == 2 ? new TLRPC.TL_inputPrivacyKeyPhoneCall() : i2 == 1 ? new TLRPC.TL_inputPrivacyKeyChatInvite() : i2 == 8 ? new TLRPC.TL_inputPrivacyKeyVoiceMessages() : i2 == 11 ? new TLRPC.TL_inputPrivacyKeyBirthday() : new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
        }
        if (this.currentType != 0 && this.currentPlus.size() > 0) {
            TLRPC.TL_inputPrivacyValueAllowUsers tL_inputPrivacyValueAllowUsers = new TLRPC.TL_inputPrivacyValueAllowUsers();
            TLRPC.TL_inputPrivacyValueAllowChatParticipants tL_inputPrivacyValueAllowChatParticipants = new TLRPC.TL_inputPrivacyValueAllowChatParticipants();
            for (int i3 = 0; i3 < this.currentPlus.size(); i3++) {
                Long l = (Long) this.currentPlus.get(i3);
                long longValue = l.longValue();
                if (DialogObject.isUserDialog(longValue)) {
                    TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(l);
                    if (user != null && (inputUser2 = MessagesController.getInstance(this.currentAccount).getInputUser(user)) != null) {
                        tL_inputPrivacyValueAllowUsers.users.add(inputUser2);
                    }
                } else {
                    tL_inputPrivacyValueAllowChatParticipants.chats.add(Long.valueOf(-longValue));
                }
            }
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueAllowUsers);
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueAllowChatParticipants);
        }
        if (this.currentType != 1 && this.currentMinus.size() > 0) {
            TLRPC.TL_inputPrivacyValueDisallowUsers tL_inputPrivacyValueDisallowUsers = new TLRPC.TL_inputPrivacyValueDisallowUsers();
            TLRPC.TL_inputPrivacyValueDisallowChatParticipants tL_inputPrivacyValueDisallowChatParticipants = new TLRPC.TL_inputPrivacyValueDisallowChatParticipants();
            for (int i4 = 0; i4 < this.currentMinus.size(); i4++) {
                Long l2 = (Long) this.currentMinus.get(i4);
                long longValue2 = l2.longValue();
                if (DialogObject.isUserDialog(longValue2)) {
                    TLRPC.User user2 = getMessagesController().getUser(l2);
                    if (user2 != null && (inputUser = getMessagesController().getInputUser(user2)) != null) {
                        tL_inputPrivacyValueDisallowUsers.users.add(inputUser);
                    }
                } else {
                    tL_inputPrivacyValueDisallowChatParticipants.chats.add(Long.valueOf(-longValue2));
                }
            }
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueDisallowUsers);
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueDisallowChatParticipants);
        }
        int i5 = this.currentType;
        if (i5 == 0) {
            arrayList = tL_account_setPrivacy.rules;
            tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueAllowAll();
        } else {
            if (i5 != 1) {
                if (i5 == 2) {
                    arrayList = tL_account_setPrivacy.rules;
                    tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueAllowContacts();
                }
                i = this.currentType;
                if (i != 0) {
                    if (this.currentPlusPremium[i == 2 ? (char) 0 : (char) 1]) {
                        tL_account_setPrivacy.rules.add(new TLRPC.TL_inputPrivacyValueAllowPremium());
                    }
                }
                if (getParentActivity() == null) {
                    alertDialog = new AlertDialog(getParentActivity(), 3);
                    alertDialog.setCanCancel(false);
                    alertDialog.show();
                } else {
                    alertDialog = null;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_setPrivacy, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda15
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$15(alertDialog, tLObject, tL_error);
                    }
                }, 2);
                if (this.rulesType == 0 || this.selectedReadValue == this.currentReadValue) {
                }
                final TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings2 = new TLRPC.TL_account_setGlobalPrivacySettings();
                tL_account_setGlobalPrivacySettings2.settings = new TLRPC.TL_globalPrivacySettings();
                final TLRPC.TL_globalPrivacySettings globalPrivacySettings2 = getContactsController().getGlobalPrivacySettings();
                TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings2 = tL_account_setGlobalPrivacySettings2.settings;
                tL_globalPrivacySettings2.archive_and_mute_new_noncontact_peers = globalPrivacySettings2.archive_and_mute_new_noncontact_peers;
                tL_globalPrivacySettings2.keep_archived_folders = globalPrivacySettings2.keep_archived_folders;
                tL_globalPrivacySettings2.keep_archived_unmuted = globalPrivacySettings2.keep_archived_unmuted;
                tL_globalPrivacySettings2.new_noncontact_peers_require_premium = globalPrivacySettings2.new_noncontact_peers_require_premium;
                tL_globalPrivacySettings2.hide_read_marks = this.selectedReadValue;
                getConnectionsManager().sendRequest(tL_account_setGlobalPrivacySettings2, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda16
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$17(globalPrivacySettings2, tL_account_setGlobalPrivacySettings2, tLObject, tL_error);
                    }
                });
                return;
            }
            arrayList = tL_account_setPrivacy.rules;
            tL_inputPrivacyValueAllowContacts = new TLRPC.TL_inputPrivacyValueDisallowAll();
        }
        arrayList.add(tL_inputPrivacyValueAllowContacts);
        i = this.currentType;
        if (i != 0) {
        }
        if (getParentActivity() == null) {
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_setPrivacy, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$15(alertDialog, tLObject, tL_error);
            }
        }, 2);
        if (this.rulesType == 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        if (this.doneButton.getAlpha() != 1.0f) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.UserRestrictionsApplyChanges));
        builder.setMessage(LocaleController.getString(R.string.PrivacySettingsChangedAlert));
        builder.setPositiveButton(LocaleController.getString(R.string.ApplyTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PrivacyControlActivity.this.lambda$checkDiscard$19(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PrivacyControlActivity.this.lambda$checkDiscard$20(dialogInterface, i);
            }
        });
        showDialog(builder.create());
        return false;
    }

    private void checkPrivacy() {
        ArrayList arrayList;
        ArrayList<Long> arrayList2;
        int i = this.rulesType;
        if (i == 10) {
            TLRPC.TL_globalPrivacySettings globalPrivacySettings = ContactsController.getInstance(this.currentAccount).getGlobalPrivacySettings();
            int i2 = (globalPrivacySettings == null || !globalPrivacySettings.new_noncontact_peers_require_premium) ? 0 : 2;
            this.currentType = i2;
            this.initialRulesType = i2;
            this.currentMinus = new ArrayList();
            this.currentPlus = new ArrayList();
            return;
        }
        boolean[] zArr = this.currentPlusPremium;
        boolean[] zArr2 = this.initialPlusPremium;
        boolean z = i == 1;
        zArr2[0] = z;
        zArr[0] = z;
        zArr2[1] = false;
        zArr[1] = false;
        this.currentPlus = new ArrayList();
        this.currentMinus = new ArrayList();
        ArrayList<TLRPC.PrivacyRule> privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(this.rulesType);
        if (privacyRules == null || privacyRules.size() == 0) {
            this.currentType = 1;
        } else {
            char c = 65535;
            boolean z2 = false;
            boolean z3 = false;
            for (int i3 = 0; i3 < privacyRules.size(); i3++) {
                TLRPC.PrivacyRule privacyRule = privacyRules.get(i3);
                if (privacyRule instanceof TLRPC.TL_privacyValueAllowChatParticipants) {
                    TLRPC.TL_privacyValueAllowChatParticipants tL_privacyValueAllowChatParticipants = (TLRPC.TL_privacyValueAllowChatParticipants) privacyRule;
                    int size = tL_privacyValueAllowChatParticipants.chats.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        this.currentPlus.add(Long.valueOf(-tL_privacyValueAllowChatParticipants.chats.get(i4).longValue()));
                    }
                } else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowChatParticipants) {
                    TLRPC.TL_privacyValueDisallowChatParticipants tL_privacyValueDisallowChatParticipants = (TLRPC.TL_privacyValueDisallowChatParticipants) privacyRule;
                    int size2 = tL_privacyValueDisallowChatParticipants.chats.size();
                    for (int i5 = 0; i5 < size2; i5++) {
                        this.currentMinus.add(Long.valueOf(-tL_privacyValueDisallowChatParticipants.chats.get(i5).longValue()));
                    }
                } else {
                    if (privacyRule instanceof TLRPC.TL_privacyValueAllowUsers) {
                        arrayList = this.currentPlus;
                        arrayList2 = ((TLRPC.TL_privacyValueAllowUsers) privacyRule).users;
                    } else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowUsers) {
                        arrayList = this.currentMinus;
                        arrayList2 = ((TLRPC.TL_privacyValueDisallowUsers) privacyRule).users;
                    } else if (privacyRule instanceof TLRPC.TL_privacyValueAllowPremium) {
                        z2 = true;
                    } else {
                        boolean z4 = privacyRule instanceof TLRPC.TL_privacyValueAllowAll;
                        if (!z4) {
                            boolean z5 = privacyRule instanceof TLRPC.TL_privacyValueDisallowAll;
                            if (!z5 || z3) {
                                if (privacyRule instanceof TLRPC.TL_privacyValueAllowContacts) {
                                    c = 2;
                                    z3 = true;
                                } else if (c == 65535) {
                                    if (!z4) {
                                        if (!z5 || z3) {
                                            c = 2;
                                        }
                                    }
                                }
                            }
                            c = 1;
                        }
                        c = 0;
                    }
                    arrayList.addAll(arrayList2);
                }
            }
            if (c == 0 || (c == 65535 && this.currentMinus.size() > 0)) {
                this.currentType = 0;
            } else if (c == 2 || (c == 65535 && this.currentMinus.size() > 0 && this.currentPlus.size() > 0)) {
                this.currentType = 2;
            } else if (c == 1 || (c == 65535 && this.currentPlus.size() > 0)) {
                this.currentType = 1;
            }
            char c2 = this.currentType == 2 ? (char) 0 : (char) 1;
            boolean[] zArr3 = this.currentPlusPremium;
            this.initialPlusPremium[c2] = z2;
            zArr3[c2] = z2;
            View view = this.doneButton;
            if (view != null) {
                view.setAlpha(0.0f);
                this.doneButton.setScaleX(0.0f);
                this.doneButton.setScaleY(0.0f);
                this.doneButton.setEnabled(false);
            }
        }
        this.initialPlus.clear();
        this.initialMinus.clear();
        this.initialRulesType = this.currentType;
        this.initialPlus.addAll(this.currentPlus);
        this.initialMinus.addAll(this.currentMinus);
        if (this.rulesType == 6) {
            ArrayList<TLRPC.PrivacyRule> privacyRules2 = ContactsController.getInstance(this.currentAccount).getPrivacyRules(7);
            if (privacyRules2 != null && privacyRules2.size() != 0) {
                int i6 = 0;
                while (true) {
                    if (i6 >= privacyRules2.size()) {
                        break;
                    }
                    TLRPC.PrivacyRule privacyRule2 = privacyRules2.get(i6);
                    if (privacyRule2 instanceof TLRPC.TL_privacyValueAllowAll) {
                        break;
                    }
                    if (privacyRule2 instanceof TLRPC.TL_privacyValueDisallowAll) {
                        this.currentSubType = 2;
                        break;
                    } else {
                        if (privacyRule2 instanceof TLRPC.TL_privacyValueAllowContacts) {
                            this.currentSubType = 1;
                            break;
                        }
                        i6++;
                    }
                }
                this.initialRulesSubType = this.currentSubType;
            }
            this.currentSubType = 0;
            this.initialRulesSubType = this.currentSubType;
        }
        if (this.rulesType == 0) {
            TLRPC.TL_globalPrivacySettings globalPrivacySettings2 = getContactsController().getGlobalPrivacySettings();
            boolean z6 = globalPrivacySettings2 != null && globalPrivacySettings2.hide_read_marks;
            this.currentReadValue = z6;
            this.selectedReadValue = z6;
        }
        updateRows(false);
    }

    private boolean hasChanges() {
        ArrayList arrayList;
        if (this.rulesType == 0 && ((this.currentType != 0 || ((arrayList = this.currentMinus) != null && !arrayList.isEmpty())) && this.currentReadValue != this.selectedReadValue)) {
            return true;
        }
        int i = this.initialRulesType;
        int i2 = this.currentType;
        if (i != i2) {
            return true;
        }
        if (this.rulesType == 6 && i2 == 1 && this.initialRulesSubType != this.currentSubType) {
            return true;
        }
        if (i2 != 0) {
            if (this.initialPlusPremium[i2 == 2 ? (char) 0 : (char) 1] != this.currentPlusPremium[i2 == 2 ? (char) 0 : (char) 1]) {
                return true;
            }
        }
        if (this.initialMinus.size() != this.currentMinus.size() || this.initialPlus.size() != this.currentPlus.size()) {
            return true;
        }
        Collections.sort(this.initialPlus);
        Collections.sort(this.currentPlus);
        if (!this.initialPlus.equals(this.currentPlus)) {
            return true;
        }
        Collections.sort(this.initialMinus);
        Collections.sort(this.currentMinus);
        return !this.initialMinus.equals(this.currentMinus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$10(TLRPC.TL_error tL_error, TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings, TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings) {
        if (tL_error != null) {
            showErrorAlert();
            return;
        }
        if (tL_globalPrivacySettings != null) {
            tL_globalPrivacySettings.new_noncontact_peers_require_premium = tL_account_setGlobalPrivacySettings.settings.new_noncontact_peers_require_premium;
        }
        lambda$onBackPressed$300();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$11(final TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings, final TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings, TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$10(tL_error, tL_globalPrivacySettings, tL_account_setGlobalPrivacySettings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$12(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            ContactsController.getInstance(this.currentAccount).setPrivacyRules(((TLRPC.TL_account_privacyRules) tLObject).rules, 7);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$13(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$12(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$14(AlertDialog alertDialog, TLRPC.TL_error tL_error, TLObject tLObject) {
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        if (tL_error != null) {
            showErrorAlert();
            return;
        }
        TLRPC.TL_account_privacyRules tL_account_privacyRules = (TLRPC.TL_account_privacyRules) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tL_account_privacyRules.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tL_account_privacyRules.chats, false);
        ContactsController.getInstance(this.currentAccount).setPrivacyRules(tL_account_privacyRules.rules, this.rulesType);
        lambda$onBackPressed$300();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$15(final AlertDialog alertDialog, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$14(alertDialog, tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$16(TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings, TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings) {
        boolean z = tL_account_setGlobalPrivacySettings.settings.hide_read_marks;
        this.currentReadValue = z;
        tL_globalPrivacySettings.hide_read_marks = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentPrivacySettings$17(final TLRPC.TL_globalPrivacySettings tL_globalPrivacySettings, final TLRPC.TL_account_setGlobalPrivacySettings tL_account_setGlobalPrivacySettings, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$applyCurrentPrivacySettings$16(tL_globalPrivacySettings, tL_account_setGlobalPrivacySettings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$19(DialogInterface dialogInterface, int i) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$20(DialogInterface dialogInterface, int i) {
        lambda$onBackPressed$300();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3() {
        TLRPC.Photo photo;
        this.avatarForRest = null;
        this.avatarForRestPhoto = null;
        TLRPC.UserFull userFull = getMessagesController().getUserFull(getUserConfig().clientUserId);
        if (userFull == null || (photo = userFull.fallback_photo) == null) {
            return;
        }
        userFull.flags &= -4194305;
        userFull.fallback_photo = null;
        getMessagesStorage().updateUserInfo(userFull, true);
        updateAvatarForRestInfo();
        updateRows(true);
        TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
        tL_inputPhoto.id = photo.id;
        tL_inputPhoto.access_hash = photo.access_hash;
        byte[] bArr = photo.file_reference;
        tL_inputPhoto.file_reference = bArr;
        if (bArr == null) {
            tL_inputPhoto.file_reference = new byte[0];
        }
        MessagesController.getInstance(this.currentAccount).deleteUserPhoto(tL_inputPhoto);
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadDialogPhotos, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(DialogInterface dialogInterface) {
        if (this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCurrentFrame(0, false);
        } else {
            this.cameraDrawable.setCustomEndFrame(86);
            this.setAvatarCell.imageView.playAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        presentFragment(new PremiumPreviewFragment("noncontacts"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7() {
        presentFragment(new PremiumPreviewFragment("settings"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(int i, boolean z, ArrayList arrayList) {
        int i2 = 0;
        if (i == this.neverShareRow) {
            this.currentMinus = arrayList;
            while (i2 < this.currentMinus.size()) {
                this.currentPlus.remove(this.currentMinus.get(i2));
                i2++;
            }
        } else {
            this.currentPlusPremium[this.currentType == 2 ? (char) 0 : (char) 1] = z;
            this.currentPlus = arrayList;
            while (i2 < this.currentPlus.size()) {
                this.currentMinus.remove(this.currentPlus.get(i2));
                i2++;
            }
        }
        updateDoneButton();
        this.listAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view, final int i) {
        BaseFragment premiumPreviewFragment;
        BulletinFactory of;
        int i2;
        String string;
        SpannableStringBuilder replaceTags;
        String string2;
        Runnable runnable;
        if (i == this.currentPhotoForRestRow) {
            AlertDialog create = AlertsCreator.createSimpleAlert(getContext(), LocaleController.getString(R.string.RemovePublicPhoto), LocaleController.getString(R.string.RemovePhotoForRestDescription), LocaleController.getString(R.string.Remove), new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyControlActivity.this.lambda$createView$3();
                }
            }, null).create();
            create.show();
            create.redPositive();
            return;
        }
        int i3 = 0;
        r1 = false;
        boolean z = false;
        if (i == this.photoForRestRow) {
            ImageUpdater imageUpdater = this.imageUpdater;
            if (imageUpdater != null) {
                imageUpdater.openMenu(false, new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        PrivacyControlActivity.lambda$createView$4();
                    }
                }, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda7
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        PrivacyControlActivity.this.lambda$createView$5(dialogInterface);
                    }
                }, 0);
                this.cameraDrawable.setCurrentFrame(0);
                this.cameraDrawable.setCustomEndFrame(43);
                this.setAvatarCell.imageView.playAnimation();
                return;
            }
            return;
        }
        if (this.rulesType == 10 && i == this.myContactsRow && !getMessagesController().newNoncontactPeersRequirePremiumWithoutOwnpremium && !getUserConfig().isPremium()) {
            of = BulletinFactory.of(this);
            i2 = R.raw.star_premium_2;
            string = LocaleController.getString(R.string.OptionPremiumRequiredTitle);
            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(R.string.OptionPremiumRequiredMessage));
            string2 = LocaleController.getString(R.string.OptionPremiumRequiredButton);
            runnable = new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyControlActivity.this.lambda$createView$6();
                }
            };
        } else {
            if (this.rulesType != 8 || (!(i == this.myContactsRow || i == this.nobodyRow) || getUserConfig().isPremium())) {
                int i4 = this.nobodyRow;
                if (i == i4 || i == this.everybodyRow || i == this.myContactsRow) {
                    if (i == i4) {
                        i3 = 1;
                    } else if (i != this.everybodyRow) {
                        i3 = 2;
                    }
                    if (i3 == this.currentType) {
                        return;
                    }
                    this.currentType = i3;
                    Bulletin.hideVisible();
                } else {
                    if (i != this.phoneContactsRow && i != this.phoneEverybodyRow) {
                        int i5 = this.neverShareRow;
                        if (i != i5 && i != this.alwaysShareRow) {
                            if (i == this.p2pRow) {
                                premiumPreviewFragment = new PrivacyControlActivity(3);
                            } else if (i == this.readRow) {
                                this.selectedReadValue = !this.selectedReadValue;
                                updateDoneButton();
                                ((TextCheckCell) view).setChecked(this.selectedReadValue);
                                return;
                            } else if (i != this.readPremiumRow) {
                                return;
                            } else {
                                premiumPreviewFragment = new PremiumPreviewFragment("lastseen");
                            }
                            presentFragment(premiumPreviewFragment);
                            return;
                        }
                        ArrayList arrayList = i == i5 ? this.currentMinus : this.currentPlus;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(i == this.neverShareRow ? "isNeverShare" : "isAlwaysShare", true);
                        bundle.putInt("chatAddType", this.rulesType != 0 ? 1 : 0);
                        if (i == this.alwaysShareRow && this.rulesType == 1) {
                            bundle.putBoolean("allowPremium", true);
                        }
                        GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle);
                        if (i == this.alwaysShareRow) {
                            if (this.currentPlusPremium[this.currentType == 2 ? (char) 0 : (char) 1]) {
                                z = true;
                            }
                        }
                        groupCreateActivity.select(arrayList, z);
                        groupCreateActivity.setDelegate(new GroupCreateActivity.GroupCreateActivityDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda10
                            @Override // org.telegram.ui.GroupCreateActivity.GroupCreateActivityDelegate
                            public final void didSelectUsers(boolean z2, ArrayList arrayList2) {
                                PrivacyControlActivity.this.lambda$createView$8(i, z2, arrayList2);
                            }
                        });
                        presentFragment(groupCreateActivity);
                        return;
                    }
                    int i6 = i != this.phoneEverybodyRow ? 1 : 0;
                    if (i6 == this.currentSubType) {
                        return;
                    } else {
                        this.currentSubType = i6;
                    }
                }
                updateDoneButton();
                updateRows(true);
                return;
            }
            of = BulletinFactory.of(this);
            i2 = R.raw.star_premium_2;
            string = LocaleController.getString(R.string.OptionPremiumRequiredTitle);
            replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(R.string.OptionPremiumRequiredMessage));
            string2 = LocaleController.getString(R.string.OptionPremiumRequiredButton);
            runnable = new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyControlActivity.this.lambda$createView$7();
                }
            };
        }
        of.createSimpleBulletin(i2, string, replaceTags, string2, runnable).show();
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        int i7 = -this.shakeDp;
        this.shakeDp = i7;
        AndroidUtilities.shakeViewSpring(view, i7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$0(TLObject tLObject) {
        if (tLObject != null) {
            TLRPC.TL_photos_photo tL_photos_photo = (TLRPC.TL_photos_photo) tLObject;
            TLRPC.UserFull userFull = getMessagesController().getUserFull(getUserConfig().clientUserId);
            userFull.flags |= 4194304;
            userFull.fallback_photo = tL_photos_photo.photo;
            getMessagesStorage().updateUserInfo(userFull, true);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadDialogPhotos, new Object[0]);
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photos_photo.photo.sizes, 100);
            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tL_photos_photo.photo.sizes, 1000);
            if (closestPhotoSizeWithSize != null && this.avatarForRest != null) {
                FileLoader.getInstance(this.currentAccount).getPathToAttach(this.avatarForRest, true).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true));
                ImageLoader.getInstance().replaceImageInCache(this.avatarForRest.location.volume_id + "_" + this.avatarForRest.location.local_id + "@50_50", closestPhotoSizeWithSize.location.volume_id + "_" + closestPhotoSizeWithSize.location.local_id + "@50_50", ImageLocation.getForLocal(closestPhotoSizeWithSize.location), false);
            }
            if (closestPhotoSizeWithSize2 == null || this.avatarForRest == null) {
                return;
            }
            FileLoader.getInstance(this.currentAccount).getPathToAttach(this.avatarForRest.location, true).renameTo(FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize2, true));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$1(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$didUploadPhoto$0(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didUploadPhoto$2(TLRPC.PhotoSize photoSize, TLRPC.InputFile inputFile, TLRPC.InputFile inputFile2, double d, TLRPC.VideoSize videoSize, TLRPC.PhotoSize photoSize2) {
        this.avatarForRest = photoSize;
        this.avatarForRestPhoto = null;
        updateAvatarForRestInfo();
        if (inputFile != null || inputFile2 != null) {
            TLRPC.TL_photos_uploadProfilePhoto tL_photos_uploadProfilePhoto = new TLRPC.TL_photos_uploadProfilePhoto();
            if (inputFile != null) {
                tL_photos_uploadProfilePhoto.file = inputFile;
                tL_photos_uploadProfilePhoto.flags |= 1;
            }
            if (inputFile2 != null) {
                tL_photos_uploadProfilePhoto.video = inputFile2;
                int i = tL_photos_uploadProfilePhoto.flags;
                tL_photos_uploadProfilePhoto.video_start_ts = d;
                tL_photos_uploadProfilePhoto.flags = i | 6;
            }
            if (videoSize != null) {
                tL_photos_uploadProfilePhoto.video_emoji_markup = videoSize;
                tL_photos_uploadProfilePhoto.flags |= 16;
            }
            tL_photos_uploadProfilePhoto.fallback = true;
            tL_photos_uploadProfilePhoto.flags |= 8;
            getConnectionsManager().sendRequest(tL_photos_uploadProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda4
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PrivacyControlActivity.this.lambda$didUploadPhoto$1(tLObject, tL_error);
                }
            });
            TLRPC.TL_user tL_user = new TLRPC.TL_user();
            TLRPC.TL_userProfilePhoto tL_userProfilePhoto = new TLRPC.TL_userProfilePhoto();
            tL_user.photo = tL_userProfilePhoto;
            tL_userProfilePhoto.photo_small = photoSize.location;
            tL_userProfilePhoto.photo_big = photoSize2.location;
            tL_user.first_name = getUserConfig().getCurrentUser().first_name;
            tL_user.last_name = getUserConfig().getCurrentUser().last_name;
            tL_user.access_hash = getUserConfig().getCurrentUser().access_hash;
            BulletinFactory.of(this).createUsersBulletin(Collections.singletonList(tL_user), LocaleController.getString(R.string.PhotoForRestTooltip)).show();
        }
        updateRows(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$18(SharedPreferences sharedPreferences, DialogInterface dialogInterface, int i) {
        applyCurrentPrivacySettings();
        sharedPreferences.edit().putBoolean("privacyAlertShowed", true).commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDone() {
        if (getParentActivity() == null) {
            return;
        }
        if (this.currentType != 0 && this.rulesType == 0 && !getUserConfig().isPremium()) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (!globalMainSettings.getBoolean("privacyAlertShowed", false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setMessage(LocaleController.getString(this.rulesType == 1 ? R.string.WhoCanAddMeInfo : R.string.CustomHelp));
                builder.setTitle(LocaleController.getString(R.string.AppName));
                builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda11
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        PrivacyControlActivity.this.lambda$processDone$18(globalMainSettings, dialogInterface, i);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                showDialog(builder.create());
                return;
            }
        }
        applyCurrentPrivacySettings();
    }

    private void setMessageText() {
        HintView hintView;
        int i;
        TLRPC.Peer peer;
        MessageCell messageCell = this.messageCell;
        if (messageCell != null) {
            messageCell.messageObject.messageOwner.fwd_from.from_id = new TLRPC.TL_peerUser();
            int i2 = this.currentType;
            long j = 1;
            if (i2 == 0) {
                hintView = this.messageCell.hintView;
                i = R.string.PrivacyForwardsEverybody;
            } else {
                if (i2 == 1) {
                    this.messageCell.hintView.setOverrideText(LocaleController.getString(R.string.PrivacyForwardsNobody));
                    peer = this.messageCell.messageObject.messageOwner.fwd_from.from_id;
                    j = 0;
                    peer.user_id = j;
                    this.messageCell.cell.forceResetMessageObject();
                }
                hintView = this.messageCell.hintView;
                i = R.string.PrivacyForwardsContacts;
            }
            hintView.setOverrideText(LocaleController.getString(i));
            peer = this.messageCell.messageObject.messageOwner.fwd_from.from_id;
            peer.user_id = j;
            this.messageCell.cell.forceResetMessageObject();
        }
    }

    private void showErrorAlert() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(LocaleController.getString(R.string.PrivacyFloodControlError));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        showDialog(builder.create());
    }

    private void updateAvatarForRestInfo() {
        TLRPC.PhotoSize photoSize;
        TextCell textCell = this.setAvatarCell;
        if (textCell != null) {
            TLRPC.PhotoSize photoSize2 = this.avatarForRest;
            SimpleTextView textView = textCell.getTextView();
            if (photoSize2 == null) {
                textView.setText(LocaleController.formatString("SetPhotoForRest", R.string.SetPhotoForRest, new Object[0]));
                this.setAvatarCell.setNeedDivider(false);
            } else {
                textView.setText(LocaleController.formatString("UpdatePhotoForRest", R.string.UpdatePhotoForRest, new Object[0]));
                this.setAvatarCell.setNeedDivider(true);
            }
        }
        BackupImageView backupImageView = this.oldAvatarView;
        if (backupImageView == null || (photoSize = this.avatarForRest) == null) {
            return;
        }
        TLRPC.Photo photo = this.avatarForRestPhoto;
        backupImageView.setImage(photo != null ? ImageLocation.getForPhoto(photoSize, photo) : ImageLocation.getForLocal(photoSize.location), "50_50", (Drawable) null, UserConfig.getInstance(this.currentAccount).getCurrentUser());
    }

    private void updateDoneButton() {
        boolean hasChanges = hasChanges();
        this.doneButton.setEnabled(hasChanges);
        this.doneButton.animate().alpha(hasChanges ? 1.0f : 0.0f).scaleX(hasChanges ? 1.0f : 0.0f).scaleY(hasChanges ? 1.0f : 0.0f).setDuration(180L).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01ad, code lost:
    
        if (r13.currentSubType == (r4 == r13.phoneContactsRow ? 1 : 0)) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01c1, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01bf, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01bd, code lost:
    
        if (r13.currentType == (r4 == r5 ? 0 : r4 == r13.myContactsRow ? 2 : 1)) goto L105;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRows(boolean z) {
        RecyclerView.ViewHolder findContainingViewHolder;
        boolean z2;
        ArrayList arrayList;
        int i;
        TLRPC.UserFull userFull;
        DiffCallback diffCallback = null;
        Object[] objArr = 0;
        if (z) {
            DiffCallback diffCallback2 = new DiffCallback();
            diffCallback2.fillPositions(diffCallback2.oldPositionToItem);
            diffCallback2.oldRowCount = this.rowCount;
            diffCallback = diffCallback2;
        }
        this.photoForRestRow = -1;
        this.currentPhotoForRestRow = -1;
        this.photoForRestDescriptionRow = -1;
        this.messageRow = -1;
        this.setBirthdayRow = -1;
        this.phoneDetailRow = -1;
        this.phoneSectionRow = -1;
        this.phoneEverybodyRow = -1;
        this.phoneContactsRow = -1;
        this.alwaysShareRow = -1;
        this.neverShareRow = -1;
        this.p2pSectionRow = -1;
        this.p2pRow = -1;
        this.p2pDetailRow = -1;
        this.readDetailRow = -1;
        this.readRow = -1;
        this.nobodyRow = -1;
        this.shareSectionRow = -1;
        this.shareDetailRow = -1;
        this.readPremiumDetailRow = -1;
        this.readPremiumRow = -1;
        this.rowCount = 0;
        if (this.rulesType == 11 && (userFull = getMessagesController().getUserFull(getUserConfig().getClientUserId())) != null && userFull.birthday == null) {
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.setBirthdayRow = i2;
        }
        int i3 = this.rulesType;
        if (i3 == 5) {
            int i4 = this.rowCount;
            this.rowCount = i4 + 1;
            this.messageRow = i4;
        }
        int i5 = this.rowCount;
        this.sectionRow = i5;
        this.everybodyRow = i5 + 1;
        int i6 = i5 + 3;
        this.rowCount = i6;
        this.myContactsRow = i5 + 2;
        if (i3 == 4 || i3 == 9 || i3 == 0 || i3 == 2 || i3 == 3 || i3 == 5 || i3 == 6 || i3 == 8 || i3 == 1 || i3 == 11) {
            this.rowCount = i5 + 4;
            this.nobodyRow = i6;
        }
        if (i3 == 6 && this.currentType == 1) {
            int i7 = this.rowCount;
            this.phoneDetailRow = i7;
            this.phoneSectionRow = i7 + 1;
            this.phoneEverybodyRow = i7 + 2;
            this.rowCount = i7 + 4;
            this.phoneContactsRow = i7 + 3;
        }
        int i8 = this.rowCount;
        this.rowCount = i8 + 1;
        this.detailRow = i8;
        if (i3 != 10 && (i3 != 8 || getUserConfig().isPremium())) {
            int i9 = this.rowCount;
            int i10 = i9 + 1;
            this.rowCount = i10;
            this.shareSectionRow = i9;
            int i11 = this.currentType;
            if (i11 == 1 || i11 == 2) {
                this.rowCount = i9 + 2;
                this.alwaysShareRow = i10;
            }
            if (i11 == 0 || i11 == 2) {
                int i12 = this.rowCount;
                this.rowCount = i12 + 1;
                this.neverShareRow = i12;
            }
            int i13 = this.rowCount;
            int i14 = i13 + 1;
            this.rowCount = i14;
            this.shareDetailRow = i13;
            int i15 = this.rulesType;
            if (i15 == 2) {
                this.p2pSectionRow = i14;
                this.p2pRow = i13 + 2;
                this.rowCount = i13 + 4;
                this.p2pDetailRow = i13 + 3;
            }
            if (i15 == 4 && (this.currentMinus.size() > 0 || (i = this.currentType) == 2 || i == 1)) {
                int i16 = this.rowCount;
                int i17 = i16 + 1;
                this.rowCount = i17;
                this.photoForRestRow = i16;
                if (this.avatarForRest != null) {
                    this.rowCount = i16 + 2;
                    this.currentPhotoForRestRow = i17;
                }
                int i18 = this.rowCount;
                this.rowCount = i18 + 1;
                this.photoForRestDescriptionRow = i18;
            }
            if (this.rulesType == 0 && (this.currentType != 0 || ((arrayList = this.currentMinus) != null && !arrayList.isEmpty()))) {
                int i19 = this.rowCount;
                this.readRow = i19;
                this.rowCount = i19 + 2;
                this.readDetailRow = i19 + 1;
            }
            if (this.rulesType == 0 && !getMessagesController().premiumFeaturesBlocked()) {
                int i20 = this.rowCount;
                this.readPremiumRow = i20;
                this.rowCount = i20 + 2;
                this.readPremiumDetailRow = i20 + 1;
            }
        }
        setMessageText();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            if (!z) {
                listAdapter.notifyDataSetChanged();
                return;
            }
            int childCount = this.listView.getChildCount();
            for (int i21 = 0; i21 < childCount; i21++) {
                View childAt = this.listView.getChildAt(i21);
                if ((childAt instanceof RadioCell) && (findContainingViewHolder = this.listView.findContainingViewHolder(childAt)) != null) {
                    int adapterPosition = findContainingViewHolder.getAdapterPosition();
                    RadioCell radioCell = (RadioCell) childAt;
                    int i22 = this.everybodyRow;
                    if (adapterPosition == i22 || adapterPosition == this.myContactsRow || adapterPosition == this.nobodyRow) {
                    }
                    radioCell.setChecked(z2, true);
                }
            }
            diffCallback.fillPositions(diffCallback.newPositionToItem);
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this.listAdapter);
            AndroidUtilities.updateVisibleRows(this.listView);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        return checkDiscard();
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ boolean canFinishFragment() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00ad  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        ActionBar actionBar;
        int i;
        if (this.rulesType == 5) {
            this.messageCell = new MessageCell(context);
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.rulesType;
        if (i2 == 6) {
            actionBar = this.actionBar;
            i = R.string.PrivacyPhone;
        } else if (i2 == 5) {
            actionBar = this.actionBar;
            i = R.string.PrivacyForwards;
        } else if (i2 == 4) {
            actionBar = this.actionBar;
            i = R.string.PrivacyProfilePhoto;
        } else if (i2 == 9) {
            actionBar = this.actionBar;
            i = R.string.PrivacyBio;
        } else if (i2 == 3) {
            actionBar = this.actionBar;
            i = R.string.PrivacyP2P;
        } else if (i2 == 2) {
            actionBar = this.actionBar;
            i = R.string.Calls;
        } else if (i2 == 1) {
            actionBar = this.actionBar;
            i = R.string.PrivacyInvites;
        } else if (i2 == 8) {
            actionBar = this.actionBar;
            i = R.string.PrivacyVoiceMessages;
        } else if (i2 == 0) {
            actionBar = this.actionBar;
            i = R.string.PrivacyLastSeen;
        } else {
            if (i2 != 10) {
                if (i2 == 11) {
                    actionBar = this.actionBar;
                    i = R.string.PrivacyBirthday;
                }
                this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PrivacyControlActivity.1
                    @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                    public void onItemClick(int i3) {
                        if (i3 == -1) {
                            if (PrivacyControlActivity.this.checkDiscard()) {
                                PrivacyControlActivity.this.lambda$onBackPressed$300();
                            }
                        } else if (i3 == 1) {
                            PrivacyControlActivity.this.processDone();
                        }
                    }
                });
                this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
                boolean hasChanges = hasChanges();
                this.doneButton.setAlpha(!hasChanges ? 1.0f : 0.0f);
                this.doneButton.setScaleX(!hasChanges ? 1.0f : 0.0f);
                this.doneButton.setScaleY(hasChanges ? 1.0f : 0.0f);
                this.doneButton.setEnabled(hasChanges);
                this.listAdapter = new ListAdapter(context);
                FrameLayout frameLayout = new FrameLayout(context);
                this.fragmentView = frameLayout;
                frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
                RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.PrivacyControlActivity.2
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                    public void dispatchDraw(Canvas canvas) {
                        drawSectionBackground(canvas, PrivacyControlActivity.this.shareSectionRow, PrivacyControlActivity.this.shareDetailRow - 1, getThemedColor(Theme.key_windowBackgroundWhite));
                        super.dispatchDraw(canvas);
                    }
                };
                this.listView = recyclerListView;
                recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
                this.listView.setVerticalScrollBarEnabled(false);
                ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
                frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
                this.listView.setAdapter(this.listAdapter);
                this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i3) {
                        PrivacyControlActivity.this.lambda$createView$9(view, i3);
                    }
                });
                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.PrivacyControlActivity.3
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                    public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                        super.onMoveAnimationUpdate(viewHolder);
                        PrivacyControlActivity.this.listView.invalidate();
                    }
                };
                defaultItemAnimator.setDurations(350L);
                defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                defaultItemAnimator.setDelayAnimations(false);
                this.listView.setItemAnimator(defaultItemAnimator);
                setMessageText();
                return this.fragmentView;
            }
            actionBar = this.actionBar;
            i = R.string.PrivacyMessages;
        }
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PrivacyControlActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    if (PrivacyControlActivity.this.checkDiscard()) {
                        PrivacyControlActivity.this.lambda$onBackPressed$300();
                    }
                } else if (i3 == 1) {
                    PrivacyControlActivity.this.processDone();
                }
            }
        });
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
        boolean hasChanges2 = hasChanges();
        this.doneButton.setAlpha(!hasChanges2 ? 1.0f : 0.0f);
        this.doneButton.setScaleX(!hasChanges2 ? 1.0f : 0.0f);
        this.doneButton.setScaleY(hasChanges2 ? 1.0f : 0.0f);
        this.doneButton.setEnabled(hasChanges2);
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.fragmentView = frameLayout2;
        frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView2 = new RecyclerListView(context) { // from class: org.telegram.ui.PrivacyControlActivity.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                drawSectionBackground(canvas, PrivacyControlActivity.this.shareSectionRow, PrivacyControlActivity.this.shareDetailRow - 1, getThemedColor(Theme.key_windowBackgroundWhite));
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView2;
        recyclerListView2.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                PrivacyControlActivity.this.lambda$createView$9(view, i3);
            }
        });
        DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.PrivacyControlActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                super.onMoveAnimationUpdate(viewHolder);
                PrivacyControlActivity.this.listView.invalidate();
            }
        };
        defaultItemAnimator2.setDurations(350L);
        defaultItemAnimator2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator2.setDelayAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator2);
        setMessageText();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageCell messageCell;
        if (i == NotificationCenter.privacyRulesUpdated) {
            checkPrivacy();
            return;
        }
        if (i == NotificationCenter.emojiLoaded) {
            this.listView.invalidateViews();
        } else {
            if (i != NotificationCenter.didSetNewWallpapper || (messageCell = this.messageCell) == null) {
                return;
            }
            messageCell.invalidate();
        }
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didStartUpload(boolean z) {
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ void didUploadFailed() {
        ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.InputFile inputFile2, final double d, String str, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2, boolean z, final TLRPC.VideoSize videoSize) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PrivacyControlActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyControlActivity.this.lambda$didUploadPhoto$2(photoSize2, inputFile, inputFile2, d, videoSize, photoSize);
            }
        });
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ String getInitialSearchString() {
        return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getInitialSearchString(this);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, RadioCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i5 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i5));
        int i6 = Theme.key_windowBackgroundGray;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected));
        RecyclerListView recyclerListView = this.listView;
        Drawable[] shadowDrawables = Theme.chat_msgInDrawable.getShadowDrawables();
        int i7 = Theme.key_chat_inBubbleShadow;
        arrayList.add(new ThemeDescription(recyclerListView, 0, null, null, shadowDrawables, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, Theme.chat_msgInMediaDrawable.getShadowDrawables(), null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient1));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient2));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient3));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected));
        RecyclerListView recyclerListView2 = this.listView;
        Drawable[] shadowDrawables2 = Theme.chat_msgOutDrawable.getShadowDrawables();
        int i8 = Theme.key_chat_outBubbleShadow;
        arrayList.add(new ThemeDescription(recyclerListView2, 0, null, null, shadowDrawables2, null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, Theme.chat_msgOutMediaDrawable.getShadowDrawables(), null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextIn));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextOut));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckDrawable}, null, Theme.key_chat_outSentCheck));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckReadDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheckRead));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckReadSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckReadSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyLine));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyLine));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyNameText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyNameText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMessageText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMessageText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeSelectedText));
        arrayList.add(new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeSelectedText));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return checkDiscard();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        checkPrivacy();
        updateRows(false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        updateRows(false);
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onResume();
        }
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public /* synthetic */ void onUploadProgressChanged(float f) {
        ImageUpdater.ImageUpdaterDelegate.-CC.$default$onUploadProgressChanged(this, f);
    }
}
