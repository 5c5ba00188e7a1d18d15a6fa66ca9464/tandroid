package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell2;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.NotificationsSettingsActivity;

/* loaded from: classes4.dex */
public class ProfileNotificationsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private boolean addingException;
    private AnimatorSet animatorSet;
    ChatAvatarContainer avatarContainer;
    private int avatarRow;
    private int avatarSectionRow;
    private int callsRow;
    private int callsVibrateRow;
    private int colorRow;
    private int customResetRow;
    private int customResetShadowRow;
    private ProfileNotificationsActivityDelegate delegate;
    private long dialogId;
    private int enableRow;
    private int generalRow;
    private boolean isInTop5Peers;
    private int ledInfoRow;
    private int ledRow;
    private RecyclerListView listView;
    private boolean needReset;
    private boolean notificationsEnabled;
    private int popupDisabledRow;
    private int popupEnabledRow;
    private int popupInfoRow;
    private int popupRow;
    private int previewRow;
    private int priorityInfoRow;
    private int priorityRow;
    private Theme.ResourcesProvider resourcesProvider;
    private int ringtoneInfoRow;
    private int ringtoneRow;
    private int rowCount;
    private int smartRow;
    private int soundRow;
    private int storiesRow;
    private long topicId;
    private int vibrateRow;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;

        public ListAdapter(Context context) {
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ProfileNotificationsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ProfileNotificationsActivity.this.generalRow || i == ProfileNotificationsActivity.this.popupRow || i == ProfileNotificationsActivity.this.ledRow || i == ProfileNotificationsActivity.this.callsRow) {
                return 0;
            }
            if (i == ProfileNotificationsActivity.this.soundRow || i == ProfileNotificationsActivity.this.vibrateRow || i == ProfileNotificationsActivity.this.priorityRow || i == ProfileNotificationsActivity.this.smartRow || i == ProfileNotificationsActivity.this.ringtoneRow || i == ProfileNotificationsActivity.this.callsVibrateRow || i == ProfileNotificationsActivity.this.customResetRow) {
                return 1;
            }
            if (i == ProfileNotificationsActivity.this.popupInfoRow || i == ProfileNotificationsActivity.this.ledInfoRow || i == ProfileNotificationsActivity.this.priorityInfoRow || i == ProfileNotificationsActivity.this.ringtoneInfoRow) {
                return 2;
            }
            if (i == ProfileNotificationsActivity.this.colorRow) {
                return 3;
            }
            if (i == ProfileNotificationsActivity.this.popupEnabledRow || i == ProfileNotificationsActivity.this.popupDisabledRow) {
                return 4;
            }
            if (i == ProfileNotificationsActivity.this.avatarRow) {
                return 5;
            }
            if (i == ProfileNotificationsActivity.this.avatarSectionRow || i == ProfileNotificationsActivity.this.customResetShadowRow) {
                return 6;
            }
            return (i == ProfileNotificationsActivity.this.enableRow || i == ProfileNotificationsActivity.this.previewRow || i == ProfileNotificationsActivity.this.storiesRow) ? 7 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() != ProfileNotificationsActivity.this.previewRow) {
                if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.customResetRow) {
                    return true;
                }
                switch (viewHolder.getItemViewType()) {
                    case 0:
                    case 2:
                    case 5:
                    case 6:
                        return false;
                    case 1:
                    case 3:
                    case 4:
                        break;
                    default:
                        return true;
                }
            }
            return ProfileNotificationsActivity.this.notificationsEnabled;
        }

        /* JADX WARN: Code restructure failed: missing block: B:140:0x03b9, code lost:
        
            if (r9.this$0.priorityRow == (-1)) goto L152;
         */
        /* JADX WARN: Code restructure failed: missing block: B:148:0x03d9, code lost:
        
            if (r9.this$0.priorityRow == (-1)) goto L152;
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x03f9, code lost:
        
            if (r9.this$0.priorityRow == (-1)) goto L152;
         */
        /* JADX WARN: Code restructure failed: missing block: B:158:0x0417, code lost:
        
            if (r9.this$0.priorityRow == (-1)) goto L152;
         */
        /* JADX WARN: Code restructure failed: missing block: B:181:0x04c3, code lost:
        
            if (r9.this$0.priorityRow != (-1)) goto L170;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String string;
            int i3;
            String string2;
            String string3;
            int i4;
            int i5;
            int i6;
            String string4;
            Context context;
            int i7;
            String str;
            int i8;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == ProfileNotificationsActivity.this.generalRow) {
                        i2 = R.string.General;
                    } else if (i == ProfileNotificationsActivity.this.popupRow) {
                        i2 = R.string.ProfilePopupNotification;
                    } else if (i == ProfileNotificationsActivity.this.ledRow) {
                        i2 = R.string.NotificationsLed;
                    } else if (i != ProfileNotificationsActivity.this.callsRow) {
                        return;
                    } else {
                        i2 = R.string.VoipNotificationSettings;
                    }
                    headerCell.setText(LocaleController.getString(i2));
                    return;
                case 1:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    String sharedPrefKey = NotificationsController.getSharedPrefKey(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId);
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    if (i == ProfileNotificationsActivity.this.customResetRow) {
                        textSettingsCell.setText(LocaleController.getString(R.string.ResetCustomNotifications), false);
                        textSettingsCell.setTextColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_text_RedBold));
                        return;
                    }
                    textSettingsCell.setTextColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (i == ProfileNotificationsActivity.this.soundRow) {
                        int i9 = R.string.SoundDefault;
                        String string5 = notificationsSettings.getString("sound_" + sharedPrefKey, LocaleController.getString(i9));
                        long j = notificationsSettings.getLong("sound_document_id_" + sharedPrefKey, 0L);
                        if (j != 0) {
                            TLRPC.Document document = ProfileNotificationsActivity.this.getMediaDataController().ringtoneDataStore.getDocument(j);
                            if (document != null) {
                                string5 = NotificationsSoundActivity.trimTitle(document, document.file_name_fixed);
                                textSettingsCell.setTextAndValue(LocaleController.getString(R.string.Sound), string5, true);
                                return;
                            } else {
                                i5 = R.string.CustomSound;
                                string5 = LocaleController.getString(i5);
                                textSettingsCell.setTextAndValue(LocaleController.getString(R.string.Sound), string5, true);
                                return;
                            }
                        }
                        if (string5.equals("NoSound")) {
                            i5 = R.string.NoSound;
                            string5 = LocaleController.getString(i5);
                            textSettingsCell.setTextAndValue(LocaleController.getString(R.string.Sound), string5, true);
                            return;
                        } else {
                            if (string5.equals("Default")) {
                                string5 = LocaleController.getString(i9);
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString(R.string.Sound), string5, true);
                            return;
                        }
                    }
                    if (i == ProfileNotificationsActivity.this.ringtoneRow) {
                        String string6 = notificationsSettings.getString("ringtone_" + sharedPrefKey, LocaleController.getString(R.string.DefaultRingtone));
                        if (string6.equals("NoSound")) {
                            string6 = LocaleController.getString(R.string.NoSound);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString(R.string.VoipSettingsRingtone), string6, false);
                        return;
                    }
                    if (i == ProfileNotificationsActivity.this.vibrateRow) {
                        int i10 = notificationsSettings.getInt("vibrate_" + sharedPrefKey, 0);
                        if (i10 == 0 || i10 == 4) {
                            string2 = LocaleController.getString(R.string.Vibrate);
                            string3 = LocaleController.getString(R.string.VibrationDefault);
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                break;
                            }
                            r3 = true;
                        } else if (i10 == 1) {
                            string2 = LocaleController.getString(R.string.Vibrate);
                            string3 = LocaleController.getString(R.string.Short);
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                break;
                            }
                            r3 = true;
                        } else if (i10 == 2) {
                            string2 = LocaleController.getString(R.string.Vibrate);
                            string3 = LocaleController.getString(R.string.VibrationDisabled);
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                break;
                            }
                            r3 = true;
                        } else {
                            if (i10 != 3) {
                                return;
                            }
                            string2 = LocaleController.getString(R.string.Vibrate);
                            string3 = LocaleController.getString(R.string.Long);
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                break;
                            }
                            r3 = true;
                        }
                    } else if (i == ProfileNotificationsActivity.this.priorityRow) {
                        int i11 = notificationsSettings.getInt("priority_" + sharedPrefKey, 3);
                        if (i11 == 0) {
                            string2 = LocaleController.getString(R.string.NotificationsImportance);
                            i4 = R.string.NotificationsPriorityHigh;
                        } else if (i11 == 1 || i11 == 2) {
                            string2 = LocaleController.getString(R.string.NotificationsImportance);
                            i4 = R.string.NotificationsPriorityUrgent;
                        } else if (i11 == 3) {
                            string2 = LocaleController.getString(R.string.NotificationsImportance);
                            i4 = R.string.NotificationsPrioritySettings;
                        } else if (i11 == 4) {
                            string2 = LocaleController.getString(R.string.NotificationsImportance);
                            i4 = R.string.NotificationsPriorityLow;
                        } else {
                            if (i11 != 5) {
                                return;
                            }
                            string2 = LocaleController.getString(R.string.NotificationsImportance);
                            i4 = R.string.NotificationsPriorityMedium;
                        }
                        string3 = LocaleController.getString(i4);
                    } else {
                        if (i != ProfileNotificationsActivity.this.smartRow) {
                            if (i == ProfileNotificationsActivity.this.callsVibrateRow) {
                                int i12 = notificationsSettings.getInt("calls_vibrate_" + sharedPrefKey, 0);
                                if (i12 == 0 || i12 == 4) {
                                    string = LocaleController.getString(R.string.Vibrate);
                                    i3 = R.string.VibrationDefault;
                                } else if (i12 == 1) {
                                    string = LocaleController.getString(R.string.Vibrate);
                                    i3 = R.string.Short;
                                } else if (i12 == 2) {
                                    string = LocaleController.getString(R.string.Vibrate);
                                    i3 = R.string.VibrationDisabled;
                                } else {
                                    if (i12 != 3) {
                                        return;
                                    }
                                    string = LocaleController.getString(R.string.Vibrate);
                                    i3 = R.string.Long;
                                }
                                textSettingsCell.setTextAndValue(string, LocaleController.getString(i3), true);
                                return;
                            }
                            return;
                        }
                        int i13 = notificationsSettings.getInt("smart_max_count_" + sharedPrefKey, 2);
                        int i14 = notificationsSettings.getInt("smart_delay_" + sharedPrefKey, NotificationCenter.updateBotMenuButton);
                        if (i13 != 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString(R.string.SmartNotifications), LocaleController.formatString("SmartNotificationsInfo", R.string.SmartNotificationsInfo, Integer.valueOf(i13), LocaleController.formatPluralString("Minutes", i14 / 60, new Object[0])), ProfileNotificationsActivity.this.priorityRow != -1);
                            return;
                        } else {
                            string2 = LocaleController.getString(R.string.SmartNotifications);
                            string3 = LocaleController.getString(R.string.SmartNotificationsDisabled);
                            break;
                        }
                    }
                    textSettingsCell.setTextAndValue(string2, string3, r3);
                    return;
                case 2:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    textInfoPrivacyCell.setFixedSize(0);
                    if (i == ProfileNotificationsActivity.this.popupInfoRow) {
                        i6 = R.string.ProfilePopupNotificationInfo;
                    } else {
                        if (i == ProfileNotificationsActivity.this.ledInfoRow) {
                            textInfoPrivacyCell.setText(LocaleController.getString(R.string.NotificationsLedInfo));
                            context = this.context;
                            i7 = R.drawable.greydivider_bottom;
                            textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, i7, Theme.key_windowBackgroundGrayShadow));
                            return;
                        }
                        if (i == ProfileNotificationsActivity.this.priorityInfoRow) {
                            string4 = ProfileNotificationsActivity.this.priorityRow == -1 ? "" : LocaleController.getString(R.string.PriorityInfo);
                            textInfoPrivacyCell.setText(string4);
                            context = this.context;
                            i7 = R.drawable.greydivider;
                            textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, i7, Theme.key_windowBackgroundGrayShadow));
                            return;
                        }
                        if (i != ProfileNotificationsActivity.this.ringtoneInfoRow) {
                            return;
                        } else {
                            i6 = R.string.VoipRingtoneInfo;
                        }
                    }
                    string4 = LocaleController.getString(i6);
                    textInfoPrivacyCell.setText(string4);
                    context = this.context;
                    i7 = R.drawable.greydivider;
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, i7, Theme.key_windowBackgroundGrayShadow));
                    return;
                case 3:
                    TextColorCell textColorCell = (TextColorCell) viewHolder.itemView;
                    String sharedPrefKey2 = NotificationsController.getSharedPrefKey(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId);
                    SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    if (notificationsSettings2.contains("color_" + sharedPrefKey2)) {
                        str = "color_" + sharedPrefKey2;
                    } else {
                        str = DialogObject.isChatDialog(ProfileNotificationsActivity.this.dialogId) ? "GroupLed" : "MessagesLed";
                    }
                    int i15 = notificationsSettings2.getInt(str, -16776961);
                    int i16 = 0;
                    while (true) {
                        if (i16 < 9) {
                            if (TextColorCell.colorsToSave[i16] == i15) {
                                i15 = TextColorCell.colors[i16];
                            } else {
                                i16++;
                            }
                        }
                    }
                    textColorCell.setTextAndColor(LocaleController.getString(R.string.NotificationsLedColor), i15, false);
                    return;
                case 4:
                    RadioCell radioCell = (RadioCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    int i17 = notificationsSettings3.getInt("popup_" + NotificationsController.getSharedPrefKey(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId), 0);
                    if (i17 == 0) {
                        i17 = notificationsSettings3.getInt(DialogObject.isChatDialog(ProfileNotificationsActivity.this.dialogId) ? "popupGroup" : "popupAll", 0) != 0 ? 1 : 2;
                    }
                    if (i == ProfileNotificationsActivity.this.popupEnabledRow) {
                        radioCell.setText(LocaleController.getString(R.string.PopupEnabled), i17 == 1, true);
                        i8 = 1;
                    } else {
                        if (i != ProfileNotificationsActivity.this.popupDisabledRow) {
                            return;
                        }
                        radioCell.setText(LocaleController.getString(R.string.PopupDisabled), i17 == 2, false);
                        i8 = 2;
                    }
                    radioCell.setTag(i8);
                    return;
                case 5:
                    ((UserCell2) viewHolder.itemView).setData(DialogObject.isUserDialog(ProfileNotificationsActivity.this.dialogId) ? MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).getUser(Long.valueOf(ProfileNotificationsActivity.this.dialogId)) : MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).getChat(Long.valueOf(-ProfileNotificationsActivity.this.dialogId)), null, null, 0);
                    return;
                case 6:
                    ((ShadowSectionCell) viewHolder.itemView).setTopBottom(i > 0, i < getItemCount() - 1);
                    return;
                case 7:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    if (i == ProfileNotificationsActivity.this.enableRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.Notifications), ProfileNotificationsActivity.this.notificationsEnabled, true);
                        return;
                    }
                    if (i == ProfileNotificationsActivity.this.previewRow) {
                        String sharedPrefKey3 = NotificationsController.getSharedPrefKey(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId);
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.MessagePreview), notificationsSettings4.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + sharedPrefKey3, true), true);
                        return;
                    }
                    if (i == ProfileNotificationsActivity.this.storiesRow) {
                        String str2 = NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + NotificationsController.getSharedPrefKey(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId);
                        if (ProfileNotificationsActivity.this.isInTop5Peers || (notificationsSettings4.contains("EnableAllStories") && notificationsSettings4.getBoolean("EnableAllStories", true))) {
                            r3 = true;
                        }
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.StoriesSoundEnabled), notificationsSettings4.getBoolean(str2, r3), true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            switch (i) {
                case 0:
                    headerCell = new HeaderCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    headerCell = new TextSettingsCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    headerCell = new TextInfoPrivacyCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    break;
                case 3:
                    headerCell = new TextColorCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    headerCell = new RadioCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    headerCell = new UserCell2(this.context, 4, 0, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 6:
                    headerCell = new ShadowSectionCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    break;
                default:
                    headerCell = new TextCheckCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    headerCell.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            headerCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(headerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((HeaderCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                return;
            }
            if (itemViewType == 1) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.customResetRow) {
                    textSettingsCell.setEnabled(true, null);
                    return;
                } else {
                    textSettingsCell.setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                    return;
                }
            }
            if (itemViewType == 2) {
                ((TextInfoPrivacyCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                return;
            }
            if (itemViewType == 3) {
                ((TextColorCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                return;
            }
            if (itemViewType == 4) {
                ((RadioCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                return;
            }
            if (itemViewType != 7) {
                return;
            }
            TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
            if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.previewRow || viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.storiesRow) {
                textCheckCell.setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
            } else {
                textCheckCell.setEnabled(true, null);
            }
        }
    }

    /* loaded from: classes4.dex */
    public interface ProfileNotificationsActivityDelegate {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$didRemoveException(ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate, long j) {
            }
        }

        void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException);

        void didRemoveException(long j);
    }

    public ProfileNotificationsActivity(Bundle bundle) {
        this(bundle, null);
    }

    public ProfileNotificationsActivity(Bundle bundle, Theme.ResourcesProvider resourcesProvider) {
        super(bundle);
        this.resourcesProvider = resourcesProvider;
        this.dialogId = bundle.getLong("dialog_id");
        this.topicId = bundle.getLong("topic_id");
        this.addingException = bundle.getBoolean("exception", false);
    }

    private void checkRowsEnabled() {
        int childCount = this.listView.getChildCount();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < childCount; i++) {
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.getChildViewHolder(this.listView.getChildAt(i));
            int itemViewType = holder.getItemViewType();
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != this.enableRow && adapterPosition != this.customResetRow) {
                if (itemViewType == 0) {
                    ((HeaderCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 1) {
                    ((TextSettingsCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 2) {
                    ((TextInfoPrivacyCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 3) {
                    ((TextColorCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 4) {
                    ((RadioCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 7 && adapterPosition == this.previewRow) {
                    ((TextCheckCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animatorSet = animatorSet2;
        animatorSet2.playTogether(arrayList);
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ProfileNotificationsActivity.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(ProfileNotificationsActivity.this.animatorSet)) {
                    ProfileNotificationsActivity.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.setDuration(150L);
        this.animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(String str, DialogInterface dialogInterface, int i) {
        this.needReset = true;
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + str, false).remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + str).apply();
        lambda$onBackPressed$321();
        ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate = this.delegate;
        if (profileNotificationsActivityDelegate != null) {
            profileNotificationsActivityDelegate.didRemoveException(this.dialogId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.vibrateRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.callsVibrateRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.priorityRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(String str, int i, int i2) {
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("smart_max_count_" + str, i).putInt("smart_delay_" + str, i2).apply();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.smartRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.colorRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(Context context, final String str, View view, int i) {
        View findViewWithTag;
        Dialog createColorSelectDialog;
        if (view.isEnabled()) {
            Parcelable parcelable = null;
            if (i == this.customResetRow) {
                AlertDialog create = new AlertDialog.Builder(context, this.resourcesProvider).setTitle(LocaleController.getString(R.string.ResetCustomNotificationsAlertTitle)).setMessage(LocaleController.getString(R.string.ResetCustomNotificationsAlert)).setPositiveButton(LocaleController.getString(R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ProfileNotificationsActivity.this.lambda$createView$0(str, dialogInterface, i2);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            }
            if (i == this.soundRow) {
                Bundle bundle = new Bundle();
                bundle.putLong("dialog_id", this.dialogId);
                bundle.putLong("topic_id", this.topicId);
                presentFragment(new NotificationsSoundActivity(bundle, this.resourcesProvider));
                return;
            }
            if (i == this.ringtoneRow) {
                try {
                    Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                    intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    Uri uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                    String path = uri != null ? uri.getPath() : null;
                    String string = notificationsSettings.getString("ringtone_path_" + str, path);
                    if (string != null && !string.equals("NoSound")) {
                        parcelable = string.equals(path) ? uri : Uri.parse(string);
                    }
                    intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", parcelable);
                    startActivityForResult(intent, 13);
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
            if (i == this.vibrateRow) {
                createColorSelectDialog = AlertsCreator.createVibrationSelectDialog(getParentActivity(), this.dialogId, this.topicId, false, false, new Runnable() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProfileNotificationsActivity.this.lambda$createView$1();
                    }
                }, this.resourcesProvider);
            } else {
                if (i == this.enableRow) {
                    TextCheckCell textCheckCell = (TextCheckCell) view;
                    boolean z = !textCheckCell.isChecked();
                    this.notificationsEnabled = z;
                    textCheckCell.setChecked(z);
                    checkRowsEnabled();
                    return;
                }
                if (i == this.previewRow) {
                    TextCheckCell textCheckCell2 = (TextCheckCell) view;
                    MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + str, !textCheckCell2.isChecked()).apply();
                    textCheckCell2.setChecked(textCheckCell2.isChecked() ^ true);
                    return;
                }
                if (i == this.callsVibrateRow) {
                    createColorSelectDialog = AlertsCreator.createVibrationSelectDialog(getParentActivity(), this.dialogId, this.topicId, "calls_vibrate_" + str, new Runnable() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileNotificationsActivity.this.lambda$createView$2();
                        }
                    }, this.resourcesProvider);
                } else if (i == this.priorityRow) {
                    createColorSelectDialog = AlertsCreator.createPrioritySelectDialog(getParentActivity(), this.dialogId, this.topicId, -1, new Runnable() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            ProfileNotificationsActivity.this.lambda$createView$3();
                        }
                    }, this.resourcesProvider);
                } else {
                    if (i == this.smartRow) {
                        if (getParentActivity() == null) {
                            return;
                        }
                        SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                        int i2 = notificationsSettings2.getInt("smart_max_count_" + str, 2);
                        AlertsCreator.createSoundFrequencyPickerDialog(getParentActivity(), i2 != 0 ? i2 : 2, notificationsSettings2.getInt("smart_delay_" + str, NotificationCenter.updateBotMenuButton), new AlertsCreator.SoundFrequencyDelegate() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda6
                            @Override // org.telegram.ui.Components.AlertsCreator.SoundFrequencyDelegate
                            public final void didSelectValues(int i3, int i4) {
                                ProfileNotificationsActivity.this.lambda$createView$4(str, i3, i4);
                            }
                        }, this.resourcesProvider);
                        return;
                    }
                    if (i != this.colorRow) {
                        if (i == this.popupEnabledRow) {
                            MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("popup_" + str, 1).apply();
                            ((RadioCell) view).setChecked(true, true);
                            findViewWithTag = this.listView.findViewWithTag(2);
                            if (findViewWithTag == null) {
                                return;
                            }
                        } else {
                            if (i != this.popupDisabledRow) {
                                if (i == this.storiesRow) {
                                    TextCheckCell textCheckCell3 = (TextCheckCell) view;
                                    boolean z2 = !textCheckCell3.isChecked();
                                    textCheckCell3.setChecked(z2);
                                    SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                                    if (this.isInTop5Peers && z2) {
                                        edit.remove(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str);
                                    } else {
                                        edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, z2);
                                    }
                                    edit.apply();
                                    getNotificationsController().updateServerNotificationsSettings(this.dialogId, this.topicId);
                                    return;
                                }
                                return;
                            }
                            MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("popup_" + str, 2).apply();
                            ((RadioCell) view).setChecked(true, true);
                            findViewWithTag = this.listView.findViewWithTag(1);
                            if (findViewWithTag == null) {
                                return;
                            }
                        }
                        ((RadioCell) findViewWithTag).setChecked(false, true);
                        return;
                    }
                    if (getParentActivity() == null) {
                        return;
                    } else {
                        createColorSelectDialog = AlertsCreator.createColorSelectDialog(getParentActivity(), this.dialogId, this.topicId, -1, new Runnable() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda7
                            @Override // java.lang.Runnable
                            public final void run() {
                                ProfileNotificationsActivity.this.lambda$createView$5();
                            }
                        }, this.resourcesProvider);
                    }
                }
            }
            showDialog(createColorSelectDialog);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$7() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell2) {
                    ((UserCell2) childAt).update(0);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00f9  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        ChatAvatarContainer chatAvatarContainer;
        String formatName;
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_avatar_actionBarSelectorBlue, this.resourcesProvider), false);
        this.actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarDefaultIcon, this.resourcesProvider), false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        final String sharedPrefKey = NotificationsController.getSharedPrefKey(this.dialogId, this.topicId);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ProfileNotificationsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    if (!ProfileNotificationsActivity.this.addingException && ProfileNotificationsActivity.this.notificationsEnabled) {
                        MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).edit().putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0).apply();
                    }
                } else if (i == 1) {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    SharedPreferences.Editor edit = notificationsSettings.edit();
                    edit.putBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey, true);
                    TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).dialogs_dict.get(ProfileNotificationsActivity.this.dialogId);
                    if (ProfileNotificationsActivity.this.notificationsEnabled) {
                        edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0);
                        if (ProfileNotificationsActivity.this.topicId == 0) {
                            MessagesStorage.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialogId, 0L);
                            if (dialog != null) {
                                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                            }
                        }
                    } else {
                        edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
                        if (ProfileNotificationsActivity.this.topicId == 0) {
                            NotificationsController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).removeNotificationsForDialog(ProfileNotificationsActivity.this.dialogId);
                            MessagesStorage.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialogId, 1L);
                            if (dialog != null) {
                                TLRPC.TL_peerNotifySettings tL_peerNotifySettings = new TLRPC.TL_peerNotifySettings();
                                dialog.notify_settings = tL_peerNotifySettings;
                                tL_peerNotifySettings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            }
                        }
                    }
                    edit.apply();
                    NotificationsController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).updateServerNotificationsSettings(ProfileNotificationsActivity.this.dialogId, ProfileNotificationsActivity.this.topicId);
                    if (ProfileNotificationsActivity.this.delegate != null) {
                        NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
                        notificationException.did = ProfileNotificationsActivity.this.dialogId;
                        notificationException.hasCustom = true;
                        int i2 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0);
                        notificationException.notify = i2;
                        if (i2 != 0) {
                            notificationException.muteUntil = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + sharedPrefKey, 0);
                        }
                        ProfileNotificationsActivity.this.delegate.didCreateNewException(notificationException);
                    }
                }
                ProfileNotificationsActivity.this.lambda$onBackPressed$321();
            }
        });
        ChatAvatarContainer chatAvatarContainer2 = new ChatAvatarContainer(context, null, false, this.resourcesProvider);
        this.avatarContainer = chatAvatarContainer2;
        chatAvatarContainer2.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, !this.inPreviewMode ? 56.0f : 0.0f, 0.0f, 40.0f, 0.0f));
        this.actionBar.setAllowOverlayTitle(false);
        if (this.dialogId >= 0) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(this.dialogId));
            if (user != null) {
                this.avatarContainer.setUserAvatar(user);
                chatAvatarContainer = this.avatarContainer;
                formatName = ContactsController.formatName(user.first_name, user.last_name);
            }
            if (this.addingException) {
                this.avatarContainer.setSubtitle(LocaleController.getString(R.string.CustomNotifications));
            } else {
                this.avatarContainer.setSubtitle(LocaleController.getString(R.string.NotificationsNewException));
                this.actionBar.createMenu().addItem(1, LocaleController.getString(R.string.Done).toUpperCase());
            }
            FrameLayout frameLayout = new FrameLayout(context);
            this.fragmentView = frameLayout;
            frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider));
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.listView = recyclerListView;
            frameLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
            RecyclerListView recyclerListView2 = this.listView;
            ListAdapter listAdapter = new ListAdapter(context);
            this.adapter = listAdapter;
            recyclerListView2.setAdapter(listAdapter);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            this.listView.setLayoutManager(new LinearLayoutManager(context) { // from class: org.telegram.ui.ProfileNotificationsActivity.2
                @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            });
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i) {
                    ProfileNotificationsActivity.this.lambda$createView$6(context, sharedPrefKey, view, i);
                }
            });
            return this.fragmentView;
        }
        if (this.topicId != 0) {
            TLRPC.TL_forumTopic findTopic = getMessagesController().getTopicsController().findTopic(-this.dialogId, this.topicId);
            ForumUtilities.setTopicIcon(this.avatarContainer.getAvatarImageView(), findTopic, false, true, this.resourcesProvider);
            chatAvatarContainer = this.avatarContainer;
            formatName = findTopic.title;
        } else {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
            this.avatarContainer.setChatAvatar(chat);
            chatAvatarContainer = this.avatarContainer;
            formatName = chat.title;
        }
        chatAvatarContainer.setTitle(formatName);
        if (this.addingException) {
        }
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.fragmentView = frameLayout2;
        frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider));
        RecyclerListView recyclerListView3 = new RecyclerListView(context);
        this.listView = recyclerListView3;
        frameLayout2.addView(recyclerListView3, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView22 = this.listView;
        ListAdapter listAdapter2 = new ListAdapter(context);
        this.adapter = listAdapter2;
        recyclerListView22.setAdapter(listAdapter2);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new LinearLayoutManager(context) { // from class: org.telegram.ui.ProfileNotificationsActivity.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                ProfileNotificationsActivity.this.lambda$createView$6(context, sharedPrefKey, view, i);
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            try {
                this.adapter.notifyDataSetChanged();
            } catch (Exception unused) {
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ProfileNotificationsActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ProfileNotificationsActivity.this.lambda$getThemeDescriptions$7();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextSettingsCell.class, TextColorCell.class, RadioCell.class, UserCell2.class, TextCheckCell.class, TextCheckBoxCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i3 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        String str;
        Ringtone ringtone;
        int i3;
        if (i2 != -1 || intent == null) {
            return;
        }
        Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
        if (uri == null || (ringtone = RingtoneManager.getRingtone(ApplicationLoader.applicationContext, uri)) == null) {
            str = null;
        } else {
            if (i == 13) {
                if (uri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
                    i3 = R.string.DefaultRingtone;
                    str = LocaleController.getString(i3);
                }
                str = ringtone.getTitle(getParentActivity());
            } else {
                if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    i3 = R.string.SoundDefault;
                    str = LocaleController.getString(i3);
                }
                str = ringtone.getTitle(getParentActivity());
            }
            ringtone.stop();
        }
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        String sharedPrefKey = NotificationsController.getSharedPrefKey(this.dialogId, this.topicId);
        if (i == 12) {
            if (str != null) {
                edit.putString("sound_" + sharedPrefKey, str);
                edit.putString("sound_path_" + sharedPrefKey, uri.toString());
            } else {
                edit.putString("sound_" + sharedPrefKey, "NoSound");
                edit.putString("sound_path_" + sharedPrefKey, "NoSound");
            }
            getNotificationsController().deleteNotificationChannel(this.dialogId, this.topicId);
        } else if (i == 13) {
            if (str != null) {
                edit.putString("ringtone_" + sharedPrefKey, str);
                edit.putString("ringtone_path_" + sharedPrefKey, uri.toString());
            } else {
                edit.putString("ringtone_" + sharedPrefKey, "NoSound");
                edit.putString("ringtone_path_" + sharedPrefKey, "NoSound");
            }
        }
        edit.apply();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i == 13 ? this.ringtoneRow : this.soundRow);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x012b  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        boolean z;
        boolean isGlobalNotificationsEnabled;
        if (DialogObject.isUserDialog(this.dialogId)) {
            ArrayList<TLRPC.TL_topPeer> arrayList = getMediaDataController().hints;
            int i = 0;
            while (true) {
                if (i >= arrayList.size()) {
                    break;
                }
                TLRPC.Peer peer = arrayList.get(i).peer;
                if ((peer instanceof TLRPC.TL_peerUser) && peer.user_id == this.dialogId) {
                    this.isInTop5Peers = i < 5;
                } else {
                    i++;
                }
            }
        }
        this.rowCount = 0;
        boolean z2 = this.addingException;
        int i2 = -1;
        if (z2) {
            this.avatarRow = 0;
            this.rowCount = 2;
            this.avatarSectionRow = 1;
        } else {
            this.avatarRow = -1;
            this.avatarSectionRow = -1;
        }
        int i3 = this.rowCount;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.generalRow = i3;
        if (z2 || this.topicId != 0) {
            this.rowCount = i3 + 2;
            this.enableRow = i4;
        } else {
            this.enableRow = -1;
        }
        this.storiesRow = -1;
        if (DialogObject.isEncryptedDialog(this.dialogId)) {
            this.previewRow = -1;
        } else {
            int i5 = this.rowCount;
            this.rowCount = i5 + 1;
            this.previewRow = i5;
            if (DialogObject.isUserDialog(this.dialogId)) {
                int i6 = this.rowCount;
                this.rowCount = i6 + 1;
                this.storiesRow = i6;
            }
        }
        int i7 = this.rowCount;
        this.soundRow = i7;
        this.rowCount = i7 + 2;
        this.vibrateRow = i7 + 1;
        if (DialogObject.isChatDialog(this.dialogId)) {
            int i8 = this.rowCount;
            this.rowCount = i8 + 1;
            this.smartRow = i8;
        } else {
            this.smartRow = -1;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            int i9 = this.rowCount;
            this.rowCount = i9 + 1;
            this.priorityRow = i9;
        } else {
            this.priorityRow = -1;
        }
        int i10 = this.rowCount;
        this.rowCount = i10 + 1;
        this.priorityInfoRow = i10;
        if (DialogObject.isChatDialog(this.dialogId)) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                z = true;
                if (!DialogObject.isEncryptedDialog(this.dialogId) || z) {
                    this.popupRow = -1;
                    this.popupEnabledRow = -1;
                    this.popupDisabledRow = -1;
                    this.popupInfoRow = -1;
                } else {
                    int i11 = this.rowCount;
                    this.popupRow = i11;
                    this.popupEnabledRow = i11 + 1;
                    this.popupDisabledRow = i11 + 2;
                    this.rowCount = i11 + 4;
                    this.popupInfoRow = i11 + 3;
                }
                if (DialogObject.isUserDialog(this.dialogId)) {
                    this.callsRow = -1;
                    this.callsVibrateRow = -1;
                    this.ringtoneRow = -1;
                    this.ringtoneInfoRow = -1;
                } else {
                    int i12 = this.rowCount;
                    this.callsRow = i12;
                    this.callsVibrateRow = i12 + 1;
                    this.ringtoneRow = i12 + 2;
                    this.rowCount = i12 + 4;
                    this.ringtoneInfoRow = i12 + 3;
                }
                int i13 = this.rowCount;
                this.ledRow = i13;
                this.colorRow = i13 + 1;
                int i14 = i13 + 3;
                this.rowCount = i14;
                this.ledInfoRow = i13 + 2;
                if (this.addingException) {
                    i2 = i13 + 4;
                    this.customResetRow = i14;
                    this.rowCount = i13 + 5;
                } else {
                    this.customResetRow = -1;
                }
                this.customResetShadowRow = i2;
                boolean isGlobalNotificationsEnabled2 = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId, false, false);
                if (this.addingException) {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    String sharedPrefKey = NotificationsController.getSharedPrefKey(this.dialogId, this.topicId);
                    boolean contains = notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                    int i15 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 0);
                    if (i15 == 0) {
                        if (!contains) {
                            isGlobalNotificationsEnabled = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId, false, false);
                        }
                    } else if (i15 != 1) {
                        this.notificationsEnabled = false;
                        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
                        return super.onFragmentCreate();
                    }
                    this.notificationsEnabled = true;
                    NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
                    return super.onFragmentCreate();
                }
                isGlobalNotificationsEnabled = !isGlobalNotificationsEnabled2;
                this.notificationsEnabled = isGlobalNotificationsEnabled;
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
                return super.onFragmentCreate();
            }
        }
        z = false;
        if (DialogObject.isEncryptedDialog(this.dialogId)) {
        }
        this.popupRow = -1;
        this.popupEnabledRow = -1;
        this.popupDisabledRow = -1;
        this.popupInfoRow = -1;
        if (DialogObject.isUserDialog(this.dialogId)) {
        }
        int i132 = this.rowCount;
        this.ledRow = i132;
        this.colorRow = i132 + 1;
        int i142 = i132 + 3;
        this.rowCount = i142;
        this.ledInfoRow = i132 + 2;
        if (this.addingException) {
        }
        this.customResetShadowRow = i2;
        boolean isGlobalNotificationsEnabled22 = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId, false, false);
        if (this.addingException) {
        }
        this.notificationsEnabled = isGlobalNotificationsEnabled;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (!this.needReset) {
            String sharedPrefKey = NotificationsController.getSharedPrefKey(this.dialogId, this.topicId);
            MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey, true).apply();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    public void setDelegate(ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate) {
        this.delegate = profileNotificationsActivityDelegate;
    }
}
