package org.telegram.ui;

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
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.Comparator$-CC;
import j$.util.function.ToDoubleFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

/* loaded from: classes4.dex */
public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int accountsAllRow;
    private int accountsInfoRow;
    private int accountsSectionRow;
    private ListAdapter adapter;
    private int androidAutoAlertRow;
    private int badgeNumberMessagesRow;
    private int badgeNumberMutedRow;
    private int badgeNumberSection;
    private int badgeNumberSection2Row;
    private int badgeNumberShowRow;
    private int callsRingtoneRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int callsVibrateRow;
    private int channelsRow;
    private int contactJoinedRow;
    private int eventsSection2Row;
    private int eventsSectionRow;
    private int groupRow;
    private int inappPreviewRow;
    private int inappPriorityRow;
    private int inappSectionRow;
    private int inappSoundRow;
    private int inappVibrateRow;
    private int inchatSoundRow;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private int notificationsSection2Row;
    private int notificationsSectionRow;
    private int notificationsServiceConnectionRow;
    private int notificationsServiceRow;
    private int otherSection2Row;
    private int otherSectionRow;
    private int pinnedMessageRow;
    private int privateRow;
    private int reactionsRow;
    private int repeatRow;
    private int resetNotificationsRow;
    private int resetNotificationsSectionRow;
    private int resetSection2Row;
    private int resetSectionRow;
    private int storiesRow;
    private boolean updateRepeatNotifications;
    private boolean updateRingtone;
    private boolean updateVibrate;
    private boolean reseting = false;
    private ArrayList exceptionUsers = null;
    private ArrayList exceptionChats = null;
    private ArrayList exceptionChannels = null;
    private ArrayList exceptionStories = null;
    private ArrayList exceptionAutoStories = null;
    private int rowCount = 0;

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsSettingsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == NotificationsSettingsActivity.this.eventsSectionRow || i == NotificationsSettingsActivity.this.otherSectionRow || i == NotificationsSettingsActivity.this.resetSectionRow || i == NotificationsSettingsActivity.this.callsSectionRow || i == NotificationsSettingsActivity.this.badgeNumberSection || i == NotificationsSettingsActivity.this.inappSectionRow || i == NotificationsSettingsActivity.this.notificationsSectionRow || i == NotificationsSettingsActivity.this.accountsSectionRow) {
                return 0;
            }
            if (i == NotificationsSettingsActivity.this.inappSoundRow || i == NotificationsSettingsActivity.this.inappVibrateRow || i == NotificationsSettingsActivity.this.notificationsServiceConnectionRow || i == NotificationsSettingsActivity.this.inappPreviewRow || i == NotificationsSettingsActivity.this.contactJoinedRow || i == NotificationsSettingsActivity.this.pinnedMessageRow || i == NotificationsSettingsActivity.this.notificationsServiceRow || i == NotificationsSettingsActivity.this.badgeNumberMutedRow || i == NotificationsSettingsActivity.this.badgeNumberMessagesRow || i == NotificationsSettingsActivity.this.badgeNumberShowRow || i == NotificationsSettingsActivity.this.inappPriorityRow || i == NotificationsSettingsActivity.this.inchatSoundRow || i == NotificationsSettingsActivity.this.androidAutoAlertRow || i == NotificationsSettingsActivity.this.accountsAllRow) {
                return 1;
            }
            if (i == NotificationsSettingsActivity.this.resetNotificationsRow) {
                return 2;
            }
            if (i == NotificationsSettingsActivity.this.privateRow || i == NotificationsSettingsActivity.this.groupRow || i == NotificationsSettingsActivity.this.channelsRow || i == NotificationsSettingsActivity.this.storiesRow || i == NotificationsSettingsActivity.this.reactionsRow) {
                return 3;
            }
            if (i == NotificationsSettingsActivity.this.eventsSection2Row || i == NotificationsSettingsActivity.this.notificationsSection2Row || i == NotificationsSettingsActivity.this.otherSection2Row || i == NotificationsSettingsActivity.this.resetSection2Row || i == NotificationsSettingsActivity.this.callsSection2Row || i == NotificationsSettingsActivity.this.badgeNumberSection2Row || i == NotificationsSettingsActivity.this.resetNotificationsSectionRow) {
                return 4;
            }
            return i == NotificationsSettingsActivity.this.accountsInfoRow ? 6 : 5;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return (adapterPosition == NotificationsSettingsActivity.this.notificationsSectionRow || adapterPosition == NotificationsSettingsActivity.this.notificationsSection2Row || adapterPosition == NotificationsSettingsActivity.this.inappSectionRow || adapterPosition == NotificationsSettingsActivity.this.eventsSectionRow || adapterPosition == NotificationsSettingsActivity.this.otherSectionRow || adapterPosition == NotificationsSettingsActivity.this.resetSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection || adapterPosition == NotificationsSettingsActivity.this.otherSection2Row || adapterPosition == NotificationsSettingsActivity.this.resetSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection2Row || adapterPosition == NotificationsSettingsActivity.this.accountsSectionRow || adapterPosition == NotificationsSettingsActivity.this.accountsInfoRow || adapterPosition == NotificationsSettingsActivity.this.resetNotificationsSectionRow || adapterPosition == NotificationsSettingsActivity.this.eventsSection2Row) ? false : true;
        }

        /* JADX WARN: Removed duplicated region for block: B:122:0x020d  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x020a  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x0231  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x0324  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0326  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x026a  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x02b0  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String str;
            boolean z;
            boolean z2;
            String string;
            boolean z3;
            String string2;
            String str2;
            String string3;
            String string4;
            boolean z4;
            String str3;
            boolean z5;
            boolean z6;
            boolean z7;
            boolean z8;
            int i3;
            String string5;
            ArrayList arrayList;
            int i4;
            int i5;
            ArrayList arrayList2;
            String string6;
            boolean z9;
            String string7;
            int size;
            Context context;
            int i6;
            String string8;
            int i7;
            String str4;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == NotificationsSettingsActivity.this.notificationsSectionRow) {
                        i2 = R.string.NotificationsForChats;
                        str = "NotificationsForChats";
                    } else if (i == NotificationsSettingsActivity.this.inappSectionRow) {
                        i2 = R.string.InAppNotifications;
                        str = "InAppNotifications";
                    } else if (i == NotificationsSettingsActivity.this.eventsSectionRow) {
                        i2 = R.string.Events;
                        str = "Events";
                    } else if (i == NotificationsSettingsActivity.this.otherSectionRow) {
                        i2 = R.string.NotificationsOther;
                        str = "NotificationsOther";
                    } else if (i == NotificationsSettingsActivity.this.resetSectionRow) {
                        i2 = R.string.Reset;
                        str = "Reset";
                    } else if (i == NotificationsSettingsActivity.this.callsSectionRow) {
                        i2 = R.string.VoipNotificationSettings;
                        str = "VoipNotificationSettings";
                    } else if (i == NotificationsSettingsActivity.this.badgeNumberSection) {
                        i2 = R.string.BadgeNumber;
                        str = "BadgeNumber";
                    } else if (i == NotificationsSettingsActivity.this.accountsSectionRow) {
                        i2 = R.string.ShowNotificationsFor;
                        str = "ShowNotificationsFor";
                    }
                    headerCell.setText(LocaleController.getString(str, i2));
                    break;
                case 1:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    if (i == NotificationsSettingsActivity.this.inappSoundRow) {
                        string2 = LocaleController.getString(R.string.InAppSounds);
                        str2 = "EnableInAppSounds";
                    } else {
                        z = true;
                        if (i != NotificationsSettingsActivity.this.inappVibrateRow) {
                            if (i != NotificationsSettingsActivity.this.inappPreviewRow) {
                                if (i == NotificationsSettingsActivity.this.inappPriorityRow) {
                                    string3 = LocaleController.getString(R.string.InAppPopup);
                                    string4 = LocaleController.getString(R.string.InAppPopupInfo);
                                    z5 = notificationsSettings.getBoolean("EnableInAppPopup", true);
                                    z6 = true;
                                    z7 = false;
                                } else if (i == NotificationsSettingsActivity.this.contactJoinedRow) {
                                    string2 = LocaleController.getString("ContactJoined", R.string.ContactJoined);
                                    str2 = "EnableContactJoined";
                                } else {
                                    z = true;
                                    if (i == NotificationsSettingsActivity.this.pinnedMessageRow) {
                                        textCheckCell.setTextAndCheck(LocaleController.getString("PinnedMessages", R.string.PinnedMessages), notificationsSettings.getBoolean("PinnedMessages", true), false);
                                        break;
                                    } else if (i == NotificationsSettingsActivity.this.androidAutoAlertRow) {
                                        z8 = notificationsSettings.getBoolean("EnableAutoNotifications", false);
                                        string2 = "Android Auto";
                                        textCheckCell.setTextAndCheck(string2, z8, z);
                                        break;
                                    } else {
                                        if (i != NotificationsSettingsActivity.this.notificationsServiceRow) {
                                            if (i != NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                                                if (i == NotificationsSettingsActivity.this.badgeNumberShowRow) {
                                                    string = LocaleController.getString("BadgeNumberShow", R.string.BadgeNumberShow);
                                                    z3 = NotificationsSettingsActivity.this.getNotificationsController().showBadgeNumber;
                                                    z2 = true;
                                                } else {
                                                    z2 = true;
                                                    if (i == NotificationsSettingsActivity.this.badgeNumberMutedRow) {
                                                        string = LocaleController.getString("BadgeNumberMutedChats", R.string.BadgeNumberMutedChats);
                                                        z3 = NotificationsSettingsActivity.this.getNotificationsController().showBadgeMuted;
                                                    } else {
                                                        if (i == NotificationsSettingsActivity.this.badgeNumberMessagesRow) {
                                                            string = LocaleController.getString("BadgeNumberUnread", R.string.BadgeNumberUnread);
                                                            z3 = NotificationsSettingsActivity.this.getNotificationsController().showBadgeMessages;
                                                        } else if (i == NotificationsSettingsActivity.this.inchatSoundRow) {
                                                            string2 = LocaleController.getString("InChatSound", R.string.InChatSound);
                                                            str2 = "EnableInChatSound";
                                                        } else {
                                                            z = true;
                                                            if (i == NotificationsSettingsActivity.this.callsVibrateRow) {
                                                                string2 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                                                str2 = "EnableCallVibrate";
                                                            } else if (i == NotificationsSettingsActivity.this.accountsAllRow) {
                                                                string = LocaleController.getString("AllAccounts", R.string.AllAccounts);
                                                                z3 = MessagesController.getGlobalNotificationsSettings().getBoolean("AllAccounts", true);
                                                            }
                                                        }
                                                        z2 = false;
                                                    }
                                                }
                                                textCheckCell.setTextAndCheck(string, z3, z2);
                                                break;
                                            } else {
                                                string3 = LocaleController.getString("NotificationsServiceConnection", R.string.NotificationsServiceConnection);
                                                string4 = LocaleController.getString("NotificationsServiceConnectionInfo", R.string.NotificationsServiceConnectionInfo);
                                                z4 = NotificationsSettingsActivity.this.getMessagesController().backgroundConnection;
                                                str3 = "pushConnection";
                                            }
                                        } else {
                                            string3 = LocaleController.getString("NotificationsService", R.string.NotificationsService);
                                            string4 = LocaleController.getString("NotificationsServiceInfo", R.string.NotificationsServiceInfo);
                                            z4 = NotificationsSettingsActivity.this.getMessagesController().keepAliveService;
                                            str3 = "pushService";
                                        }
                                        z5 = notificationsSettings.getBoolean(str3, z4);
                                        z6 = true;
                                        z7 = true;
                                    }
                                }
                                textCheckCell.setTextAndValueAndCheck(string3, string4, z5, z6, z7);
                                break;
                            } else {
                                string2 = LocaleController.getString(R.string.InAppPreview);
                                str2 = "EnableInAppPreview";
                            }
                        } else {
                            string2 = LocaleController.getString(R.string.InAppVibrate);
                            str2 = "EnableInAppVibrate";
                        }
                        z8 = notificationsSettings.getBoolean(str2, z);
                        textCheckCell.setTextAndCheck(string2, z8, z);
                    }
                    z = true;
                    z8 = notificationsSettings.getBoolean(str2, z);
                    textCheckCell.setTextAndCheck(string2, z8, z);
                    break;
                case 2:
                    TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell) viewHolder.itemView;
                    textDetailSettingsCell.setMultilineDetail(true);
                    if (i == NotificationsSettingsActivity.this.resetNotificationsRow) {
                        textDetailSettingsCell.setTextAndValue(LocaleController.getString("ResetAllNotifications", R.string.ResetAllNotifications), LocaleController.getString("UndoAllCustom", R.string.UndoAllCustom), false);
                        break;
                    }
                    break;
                case 3:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    int currentTime = ConnectionsManager.getInstance(((BaseFragment) NotificationsSettingsActivity.this).currentAccount).getCurrentTime();
                    ArrayList arrayList3 = null;
                    if (i != NotificationsSettingsActivity.this.privateRow) {
                        if (i != NotificationsSettingsActivity.this.groupRow) {
                            int i8 = NotificationsSettingsActivity.this.storiesRow;
                            i3 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            if (i == i8) {
                                string5 = LocaleController.getString(R.string.NotificationStories);
                                arrayList3 = NotificationsSettingsActivity.this.exceptionStories;
                                arrayList2 = NotificationsSettingsActivity.this.exceptionAutoStories;
                                if (notificationsSettings2.getBoolean("EnableAllStories", false)) {
                                    i3 = 0;
                                }
                                i5 = R.drawable.msg_menu_stories;
                            } else if (i == NotificationsSettingsActivity.this.reactionsRow) {
                                string5 = LocaleController.getString(R.string.NotificationReactions);
                                int i9 = (notificationsSettings2.getBoolean("EnableReactionsMessages", true) || notificationsSettings2.getBoolean("EnableReactionsStories", true)) ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
                                i5 = R.drawable.msg_reactions;
                                i3 = i9;
                                arrayList2 = null;
                            } else {
                                string5 = LocaleController.getString(R.string.NotificationsChannels);
                                arrayList = NotificationsSettingsActivity.this.exceptionChannels;
                                i4 = notificationsSettings2.getInt("EnableChannel2", 0);
                                i5 = R.drawable.msg_channel;
                            }
                            boolean z10 = i3 >= currentTime;
                            int i10 = (!z10 && i3 - 31536000 < currentTime) ? 2 : 0;
                            StringBuilder sb = new StringBuilder();
                            int i11 = i5;
                            if (i != NotificationsSettingsActivity.this.reactionsRow) {
                                if (arrayList3 != null && !arrayList3.isEmpty()) {
                                    boolean z11 = i3 < currentTime;
                                    if (z11) {
                                        string7 = LocaleController.getString("NotificationsOn", R.string.NotificationsOn);
                                    } else if (i3 - 31536000 >= currentTime) {
                                        string7 = LocaleController.getString("NotificationsOff", R.string.NotificationsOff);
                                    } else {
                                        z10 = z11;
                                        sb.append(LocaleController.formatString("NotificationsOffUntil", R.string.NotificationsOffUntil, LocaleController.stringForMessageListDate(i3)));
                                        if (sb.length() != 0) {
                                            sb.append(", ");
                                        }
                                        size = arrayList3.size();
                                        if (i == NotificationsSettingsActivity.this.storiesRow && !notificationsSettings2.contains("EnableAllStories") && arrayList2 != null) {
                                            size += arrayList2.size();
                                        }
                                        string6 = LocaleController.formatPluralString("Exception", size, new Object[0]);
                                    }
                                    sb.append(string7);
                                    z10 = z11;
                                    if (sb.length() != 0) {
                                    }
                                    size = arrayList3.size();
                                    if (i == NotificationsSettingsActivity.this.storiesRow) {
                                        size += arrayList2.size();
                                    }
                                    string6 = LocaleController.formatPluralString("Exception", size, new Object[0]);
                                } else if (arrayList2 == null || arrayList2.isEmpty()) {
                                    string6 = LocaleController.getString("TapToChange", R.string.TapToChange);
                                } else {
                                    sb.append(i3 > 0 ? LocaleController.getString("NotificationsOff", R.string.NotificationsOff) : LocaleController.getString("NotificationsOn", R.string.NotificationsOn));
                                    if (!arrayList2.isEmpty() && !notificationsSettings2.contains("EnableAllStories")) {
                                        sb.append(", ");
                                        string6 = LocaleController.formatPluralString("AutoException", arrayList2.size(), new Object[0]);
                                    }
                                    z9 = z10;
                                }
                                sb.append(string6);
                                z9 = z10;
                            } else if (i3 > 0) {
                                sb.append(LocaleController.getString("NotificationsOff", R.string.NotificationsOff));
                                z9 = false;
                            } else {
                                if (notificationsSettings2.getBoolean("EnableReactionsMessages", true)) {
                                    sb.append(LocaleController.getString(R.string.NotificationReactionsMessages));
                                }
                                if (notificationsSettings2.getBoolean("EnableReactionsStories", true)) {
                                    if (sb.length() > 0) {
                                        sb.append(", ");
                                    }
                                    sb.append(LocaleController.getString(R.string.NotificationReactionsStories));
                                }
                                z9 = true;
                            }
                            notificationsCheckCell.setTextAndValueAndIconAndCheck(string5, sb, i11, z9, i10, false, i != NotificationsSettingsActivity.this.reactionsRow);
                            break;
                        } else {
                            string5 = LocaleController.getString(R.string.NotificationsGroups);
                            arrayList = NotificationsSettingsActivity.this.exceptionChats;
                            i4 = notificationsSettings2.getInt("EnableGroup2", 0);
                            i5 = R.drawable.msg_groups;
                        }
                    } else {
                        string5 = LocaleController.getString(R.string.NotificationsPrivateChats);
                        arrayList = NotificationsSettingsActivity.this.exceptionUsers;
                        i4 = notificationsSettings2.getInt("EnableAll2", 0);
                        i5 = R.drawable.msg_openprofile;
                    }
                    int i12 = i4;
                    arrayList2 = null;
                    arrayList3 = arrayList;
                    i3 = i12;
                    if (i3 >= currentTime) {
                    }
                    if (z10) {
                        StringBuilder sb2 = new StringBuilder();
                        int i112 = i5;
                        if (i != NotificationsSettingsActivity.this.reactionsRow) {
                        }
                        notificationsCheckCell.setTextAndValueAndIconAndCheck(string5, sb2, i112, z9, i10, false, i != NotificationsSettingsActivity.this.reactionsRow);
                    }
                    StringBuilder sb22 = new StringBuilder();
                    int i1122 = i5;
                    if (i != NotificationsSettingsActivity.this.reactionsRow) {
                    }
                    notificationsCheckCell.setTextAndValueAndIconAndCheck(string5, sb22, i1122, z9, i10, false, i != NotificationsSettingsActivity.this.reactionsRow);
                    break;
                case 4:
                    int i13 = NotificationsSettingsActivity.this.resetNotificationsSectionRow;
                    View view = viewHolder.itemView;
                    if (i == i13) {
                        context = this.mContext;
                        i6 = R.drawable.greydivider_bottom;
                    } else {
                        context = this.mContext;
                        i6 = R.drawable.greydivider;
                    }
                    view.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i6, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 5:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    if (i != NotificationsSettingsActivity.this.callsRingtoneRow) {
                        if (i != NotificationsSettingsActivity.this.callsVibrateRow) {
                            if (i == NotificationsSettingsActivity.this.repeatRow) {
                                int i14 = notificationsSettings3.getInt("repeat_messages", 60);
                                textSettingsCell.setTextAndValue(LocaleController.getString("RepeatNotifications", R.string.RepeatNotifications), i14 == 0 ? LocaleController.getString("RepeatNotificationsNever", R.string.RepeatNotificationsNever) : i14 < 60 ? LocaleController.formatPluralString("Minutes", i14, new Object[0]) : LocaleController.formatPluralString("Hours", i14 / 60, new Object[0]), NotificationsSettingsActivity.this.updateRepeatNotifications, false);
                                NotificationsSettingsActivity.this.updateRepeatNotifications = false;
                                break;
                            }
                        } else {
                            int i15 = notificationsSettings3.getInt("vibrate_calls", 0);
                            if (i15 != 0) {
                                if (i15 != 1) {
                                    if (i15 != 2) {
                                        if (i15 != 3) {
                                            if (i15 == 4) {
                                                string8 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                                i7 = R.string.OnlyIfSilent;
                                                str4 = "OnlyIfSilent";
                                            }
                                            NotificationsSettingsActivity.this.updateVibrate = false;
                                            break;
                                        } else {
                                            string8 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                            i7 = R.string.Long;
                                            str4 = "Long";
                                        }
                                    } else {
                                        string8 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                        i7 = R.string.VibrationDisabled;
                                        str4 = "VibrationDisabled";
                                    }
                                } else {
                                    string8 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                    i7 = R.string.Short;
                                    str4 = "Short";
                                }
                            } else {
                                string8 = LocaleController.getString("Vibrate", R.string.Vibrate);
                                i7 = R.string.VibrationDefault;
                                str4 = "VibrationDefault";
                            }
                            textSettingsCell.setTextAndValue(string8, LocaleController.getString(str4, i7), NotificationsSettingsActivity.this.updateVibrate, true);
                            NotificationsSettingsActivity.this.updateVibrate = false;
                        }
                    } else {
                        String string9 = notificationsSettings3.getString("CallsRingtone", LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone));
                        if (string9.equals("NoSound")) {
                            string9 = LocaleController.getString("NoSound", R.string.NoSound);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", R.string.VoipSettingsRingtone), string9, NotificationsSettingsActivity.this.updateRingtone, false);
                        NotificationsSettingsActivity.this.updateRingtone = false;
                        break;
                    }
                    break;
                case 6:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == NotificationsSettingsActivity.this.accountsInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ShowNotificationsForInfo", R.string.ShowNotificationsForInfo));
                        break;
                    }
                    break;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            if (i == 0) {
                headerCell = new HeaderCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 1) {
                headerCell = new TextCheckCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            } else if (i == 2) {
                headerCell = new TextDetailSettingsCell(this.mContext);
            } else {
                if (i != 3) {
                    if (i == 4) {
                        headerCell = new ShadowSectionCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
                    } else if (i != 5) {
                        headerCell = new TextInfoPrivacyCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
                        headerCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        headerCell = new TextSettingsCell(this.mContext, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
                    }
                    return new RecyclerListView.Holder(headerCell);
                }
                headerCell = new NotificationsCheckCell(this.mContext, 21, 64, true, ((BaseFragment) NotificationsSettingsActivity.this).resourceProvider);
            }
            headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return new RecyclerListView.Holder(headerCell);
        }
    }

    public static class NotificationException {
        public boolean auto;
        public long did;
        public boolean hasCustom;
        public int muteUntil;
        public int notify;
        public boolean story;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view, final int i, float f, float f2) {
        ArrayList arrayList;
        boolean isGlobalNotificationsEnabled;
        Dialog create;
        SharedPreferences.Editor edit;
        boolean z;
        String str;
        SharedPreferences.Editor edit2;
        String str2;
        boolean z2;
        SharedPreferences notificationsSettings;
        r3 = false;
        r3 = false;
        r3 = false;
        r3 = false;
        r3 = false;
        boolean z3 = false;
        final int i2 = 0;
        if (getParentActivity() == null) {
            return;
        }
        int i3 = this.privateRow;
        Parcelable parcelable = null;
        parcelable = null;
        if (i == i3 || i == this.groupRow || i == this.channelsRow || i == this.storiesRow || i == this.reactionsRow) {
            ArrayList arrayList2 = null;
            if (i == i3) {
                arrayList = this.exceptionUsers;
                isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(1);
                i2 = 1;
            } else if (i == this.groupRow) {
                arrayList = this.exceptionChats;
                isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(0);
            } else if (i == this.storiesRow) {
                ArrayList arrayList3 = this.exceptionStories;
                ArrayList arrayList4 = this.exceptionAutoStories;
                isGlobalNotificationsEnabled = getNotificationsSettings().getBoolean("EnableAllStories", false);
                i2 = 3;
                arrayList = arrayList3;
                arrayList2 = arrayList4;
            } else if (i == this.reactionsRow) {
                arrayList = null;
                isGlobalNotificationsEnabled = getNotificationsSettings().getBoolean("EnableReactionsMessages", true) || getNotificationsSettings().getBoolean("EnableReactionsStories", true);
                i2 = 4;
            } else {
                arrayList = this.exceptionChannels;
                isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(2);
                i2 = 2;
            }
            if (arrayList == null && i2 != 4) {
                return;
            }
            final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            if ((!LocaleController.isRTL || f > AndroidUtilities.dp(76.0f)) && (LocaleController.isRTL || f < view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                presentFragment(new NotificationsCustomSettingsActivity(i2, arrayList, arrayList2));
            } else {
                final boolean z4 = isGlobalNotificationsEnabled;
                showExceptionsAlert(i, new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationsSettingsActivity.this.lambda$createView$3(i2, z4, notificationsCheckCell, i);
                    }
                });
            }
            z3 = isGlobalNotificationsEnabled;
        } else if (i == this.callsRingtoneRow) {
            try {
                SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
                String path = uri != null ? uri.getPath() : null;
                String string = notificationsSettings2.getString("CallsRingtonePath", path);
                if (string != null && !string.equals("NoSound")) {
                    parcelable = string.equals(path) ? uri : Uri.parse(string);
                }
                intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", parcelable);
                startActivityForResult(intent, i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == this.resetNotificationsRow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("ResetNotificationsAlertTitle", R.string.ResetNotificationsAlertTitle));
            builder.setMessage(LocaleController.getString("ResetNotificationsAlert", R.string.ResetNotificationsAlert));
            builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i4) {
                    NotificationsSettingsActivity.this.lambda$createView$6(alertDialog, i4);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create2 = builder.create();
            showDialog(create2);
            TextView textView = (TextView) create2.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        } else {
            if (i == this.inappSoundRow) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings.edit();
                str2 = "EnableInAppSounds";
            } else if (i == this.inappVibrateRow) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings.edit();
                str2 = "EnableInAppVibrate";
            } else if (i == this.inappPreviewRow) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings.edit();
                str2 = "EnableInAppPreview";
            } else if (i == this.inchatSoundRow) {
                SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit3 = notificationsSettings3.edit();
                z3 = notificationsSettings3.getBoolean("EnableInChatSound", true);
                boolean z5 = !z3;
                edit3.putBoolean("EnableInChatSound", z5);
                edit3.commit();
                getNotificationsController().setInChatSoundEnabled(z5);
            } else if (i == this.inappPriorityRow) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings.edit();
                str2 = "EnableInAppPopup";
            } else if (i == this.contactJoinedRow) {
                SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit4 = notificationsSettings4.edit();
                z3 = notificationsSettings4.getBoolean("EnableContactJoined", true);
                boolean z6 = !z3;
                MessagesController.getInstance(this.currentAccount).enableJoined = z6;
                edit4.putBoolean("EnableContactJoined", z6);
                edit4.commit();
                TL_account.setContactSignUpNotification setcontactsignupnotification = new TL_account.setContactSignUpNotification();
                setcontactsignupnotification.silent = z3;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(setcontactsignupnotification, new RequestDelegate() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        NotificationsSettingsActivity.lambda$createView$7(tLObject, tL_error);
                    }
                });
            } else if (i == this.pinnedMessageRow) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings.edit();
                str2 = "PinnedMessages";
            } else if (i == this.androidAutoAlertRow) {
                SharedPreferences notificationsSettings5 = MessagesController.getNotificationsSettings(this.currentAccount);
                edit2 = notificationsSettings5.edit();
                str2 = "EnableAutoNotifications";
                z2 = notificationsSettings5.getBoolean("EnableAutoNotifications", false);
                z3 = z2;
                edit2.putBoolean(str2, !z3);
                edit2.commit();
            } else {
                if (i == this.badgeNumberShowRow) {
                    edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z3 = getNotificationsController().showBadgeNumber;
                    getNotificationsController().showBadgeNumber = !z3;
                    z = getNotificationsController().showBadgeNumber;
                    str = "badgeNumber";
                } else if (i == this.badgeNumberMutedRow) {
                    SharedPreferences.Editor edit5 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z3 = getNotificationsController().showBadgeMuted;
                    getNotificationsController().showBadgeMuted = !z3;
                    edit5.putBoolean("badgeNumberMuted", getNotificationsController().showBadgeMuted);
                    edit5.commit();
                    getNotificationsController().updateBadge();
                    getMessagesStorage().updateMutedDialogsFiltersCounters();
                } else if (i == this.badgeNumberMessagesRow) {
                    edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                    z3 = getNotificationsController().showBadgeMessages;
                    getNotificationsController().showBadgeMessages = !z3;
                    z = getNotificationsController().showBadgeMessages;
                    str = "badgeNumberMessages";
                } else if (i == this.notificationsServiceConnectionRow) {
                    SharedPreferences notificationsSettings6 = MessagesController.getNotificationsSettings(this.currentAccount);
                    boolean z7 = notificationsSettings6.getBoolean("pushConnection", getMessagesController().backgroundConnection);
                    SharedPreferences.Editor edit6 = notificationsSettings6.edit();
                    edit6.putBoolean("pushConnection", !z7);
                    edit6.commit();
                    ConnectionsManager connectionsManager = ConnectionsManager.getInstance(this.currentAccount);
                    if (z7) {
                        connectionsManager.setPushConnectionEnabled(false);
                    } else {
                        connectionsManager.setPushConnectionEnabled(true);
                    }
                    z3 = z7;
                } else if (i == this.accountsAllRow) {
                    SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
                    boolean z8 = globalNotificationsSettings.getBoolean("AllAccounts", true);
                    SharedPreferences.Editor edit7 = globalNotificationsSettings.edit();
                    boolean z9 = !z8;
                    edit7.putBoolean("AllAccounts", z9);
                    edit7.commit();
                    SharedConfig.showNotificationsForAllAccounts = z9;
                    for (int i4 = 0; i4 < 4; i4++) {
                        if (SharedConfig.showNotificationsForAllAccounts || i4 == this.currentAccount) {
                            NotificationsController.getInstance(i4).showNotifications();
                        } else {
                            NotificationsController.getInstance(i4).hideNotifications();
                        }
                    }
                    z3 = z8;
                } else if (i == this.notificationsServiceRow) {
                    SharedPreferences notificationsSettings7 = MessagesController.getNotificationsSettings(this.currentAccount);
                    z3 = notificationsSettings7.getBoolean("pushService", getMessagesController().keepAliveService);
                    SharedPreferences.Editor edit8 = notificationsSettings7.edit();
                    edit8.putBoolean("pushService", !z3);
                    edit8.commit();
                    ApplicationLoader.startPushService();
                } else {
                    if (i == this.callsVibrateRow) {
                        if (getParentActivity() == null) {
                            return;
                        } else {
                            create = AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, 0L, i == this.callsVibrateRow ? "vibrate_calls" : null, new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda4
                                @Override // java.lang.Runnable
                                public final void run() {
                                    NotificationsSettingsActivity.this.lambda$createView$8(i);
                                }
                            });
                        }
                    } else if (i == this.repeatRow) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                        builder2.setTitle(LocaleController.getString("RepeatNotifications", R.string.RepeatNotifications));
                        builder2.setItems(new CharSequence[]{LocaleController.getString("RepeatDisabled", R.string.RepeatDisabled), LocaleController.formatPluralString("Minutes", 5, new Object[0]), LocaleController.formatPluralString("Minutes", 10, new Object[0]), LocaleController.formatPluralString("Minutes", 30, new Object[0]), LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Hours", 2, new Object[0]), LocaleController.formatPluralString("Hours", 4, new Object[0])}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda5
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                NotificationsSettingsActivity.this.lambda$createView$9(i, dialogInterface, i5);
                            }
                        });
                        builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        create = builder2.create();
                    }
                    showDialog(create);
                }
                edit.putBoolean(str, z);
                edit.commit();
                getNotificationsController().updateBadge();
            }
            z2 = notificationsSettings.getBoolean(str2, true);
            z3 = z2;
            edit2.putBoolean(str2, !z3);
            edit2.commit();
        }
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(!z3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(int i, boolean z, NotificationsCheckCell notificationsCheckCell, int i2) {
        if (i == 3) {
            SharedPreferences.Editor edit = getNotificationsSettings().edit();
            if (z) {
                edit.remove("EnableAllStories");
            } else {
                edit.putBoolean("EnableAllStories", true);
            }
            edit.apply();
            getNotificationsController().updateServerNotificationsSettings(i);
        } else if (i == 4 || i == 5) {
            SharedPreferences.Editor edit2 = getNotificationsSettings().edit();
            if (z) {
                edit2.putBoolean("EnableReactionsMessages", false);
                edit2.putBoolean("EnableReactionsStories", false);
            } else {
                edit2.putBoolean("EnableReactionsMessages", true);
                edit2.putBoolean("EnableReactionsStories", true);
            }
            edit2.apply();
            getNotificationsController().updateServerNotificationsSettings(i);
            getNotificationsController().deleteNotificationChannelGlobal(i);
        } else {
            getNotificationsController().setGlobalNotificationsEnabled(i, !z ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID);
        }
        notificationsCheckCell.setChecked(!z, 0);
        this.adapter.notifyItemChanged(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        getMessagesController().enableJoined = true;
        this.reseting = false;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        edit.clear();
        edit.commit();
        this.exceptionChats.clear();
        this.exceptionUsers.clear();
        this.adapter.notifyDataSetChanged();
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), LocaleController.getString("ResetNotificationsText", R.string.ResetNotificationsText), 0).show();
        }
        getMessagesStorage().updateMutedDialogsFiltersCounters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsSettingsActivity.this.lambda$createView$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(AlertDialog alertDialog, int i) {
        if (this.reseting) {
            return;
        }
        this.reseting = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.resetNotifySettings(), new RequestDelegate() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NotificationsSettingsActivity.this.lambda$createView$5(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$7(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(int i) {
        this.updateVibrate = true;
        this.adapter.notifyItemChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(int i, DialogInterface dialogInterface, int i2) {
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("repeat_messages", i2 != 1 ? i2 == 2 ? 10 : i2 == 3 ? 30 : i2 == 4 ? 60 : i2 == 5 ? 120 : i2 == 6 ? NotificationCenter.didApplyNewTheme : 0 : 5).commit();
        this.updateRepeatNotifications = true;
        this.adapter.notifyItemChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadExceptions$1(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6, ArrayList arrayList7, ArrayList arrayList8) {
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, true);
        MessagesController.getInstance(this.currentAccount).putChats(arrayList2, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(arrayList3, true);
        this.exceptionUsers = arrayList4;
        this.exceptionChats = arrayList5;
        this.exceptionChannels = arrayList6;
        this.exceptionStories = arrayList7;
        this.exceptionAutoStories = arrayList8;
        this.adapter.notifyItemChanged(this.privateRow);
        this.adapter.notifyItemChanged(this.groupRow);
        this.adapter.notifyItemChanged(this.channelsRow);
        this.adapter.notifyItemChanged(this.storiesRow);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x02bb, code lost:
    
        if (r7.deleted != false) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x013d, code lost:
    
        if (r0.deleted != false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0166, code lost:
    
        if (r0.deleted != false) goto L30;
     */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0348  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x03ab A[LOOP:5: B:190:0x03a9->B:191:0x03ab, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x03c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadExceptions$2(ArrayList arrayList) {
        ArrayList<TLRPC.EncryptedChat> arrayList2;
        ArrayList<TLRPC.Chat> arrayList3;
        ArrayList<TLRPC.User> arrayList4;
        ArrayList<TLRPC.EncryptedChat> arrayList5;
        int size;
        int i;
        int size2;
        int i2;
        int size3;
        int i3;
        int size4;
        int i4;
        ArrayList arrayList6;
        ArrayList arrayList7;
        Long parseLong;
        long longValue;
        final ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        final ArrayList arrayList10 = new ArrayList();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList13 = new ArrayList<>();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList<TLRPC.User> arrayList16 = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList17 = new ArrayList<>();
        ArrayList<TLRPC.EncryptedChat> arrayList18 = new ArrayList<>();
        long j = UserConfig.getInstance(this.currentAccount).clientUserId;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList2 = arrayList18;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            ArrayList arrayList19 = arrayList12;
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY)) {
                String replace = key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY, "");
                if (!replace.contains("_")) {
                    Long parseLong2 = Utilities.parseLong(replace);
                    ArrayList arrayList20 = arrayList9;
                    ArrayList arrayList21 = arrayList10;
                    long longValue2 = parseLong2.longValue();
                    if (longValue2 == 0 || longValue2 == j) {
                        arrayList9 = arrayList20;
                        arrayList18 = arrayList2;
                        arrayList12 = arrayList19;
                        arrayList10 = arrayList21;
                    } else {
                        ArrayList arrayList22 = arrayList11;
                        NotificationException notificationException = new NotificationException();
                        notificationException.did = longValue2;
                        long j2 = j;
                        notificationException.hasCustom = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + longValue2, false);
                        int intValue = ((Integer) next.getValue()).intValue();
                        notificationException.notify = intValue;
                        if (intValue != 0) {
                            Integer num = (Integer) all.get(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + replace);
                            if (num != null) {
                                notificationException.muteUntil = num.intValue();
                            }
                        }
                        if (DialogObject.isEncryptedDialog(longValue2)) {
                            int encryptedChatId = DialogObject.getEncryptedChatId(longValue2);
                            TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(encryptedChatId));
                            if (encryptedChat == null) {
                                arrayList15.add(Integer.valueOf(encryptedChatId));
                            } else {
                                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                                if (user == null) {
                                    arrayList13.add(Long.valueOf(encryptedChat.user_id));
                                    longValue2 = encryptedChat.user_id;
                                }
                            }
                            longSparseArray.put(longValue2, notificationException);
                            arrayList8.add(notificationException);
                            arrayList9 = arrayList20;
                            arrayList18 = arrayList2;
                            arrayList12 = arrayList19;
                            arrayList10 = arrayList21;
                        } else {
                            if (DialogObject.isUserDialog(longValue2)) {
                                TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(parseLong2);
                                if (user2 == null) {
                                    arrayList13.add(parseLong2);
                                    longSparseArray.put(longValue2, notificationException);
                                    arrayList8.add(notificationException);
                                }
                            } else {
                                long j3 = -longValue2;
                                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j3));
                                if (chat == null) {
                                    arrayList14.add(Long.valueOf(j3));
                                    longSparseArray.put(longValue2, notificationException);
                                } else if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                                    if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                        arrayList10 = arrayList21;
                                        arrayList20.add(notificationException);
                                    } else {
                                        arrayList10 = arrayList21;
                                        arrayList10.add(notificationException);
                                    }
                                    arrayList9 = arrayList20;
                                    arrayList18 = arrayList2;
                                    arrayList12 = arrayList19;
                                }
                            }
                            arrayList9 = arrayList20;
                            arrayList18 = arrayList2;
                            arrayList12 = arrayList19;
                            arrayList10 = arrayList21;
                        }
                        arrayList11 = arrayList22;
                        j = j2;
                    }
                }
            }
            arrayList18 = arrayList2;
            arrayList12 = arrayList19;
        }
        final ArrayList arrayList23 = arrayList9;
        ArrayList arrayList24 = arrayList11;
        ArrayList arrayList25 = arrayList12;
        long j4 = j;
        HashSet hashSet = new HashSet();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String key2 = entry.getKey();
            if (key2.startsWith(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY)) {
                try {
                    parseLong = Utilities.parseLong(key2.substring(8));
                    longValue = parseLong.longValue();
                } catch (Exception unused) {
                    arrayList7 = arrayList24;
                }
                if (longValue != 0 && longValue != j4) {
                    NotificationException notificationException2 = new NotificationException();
                    notificationException2.did = longValue;
                    notificationException2.notify = ((Boolean) entry.getValue()).booleanValue() ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
                    notificationException2.story = true;
                    if (DialogObject.isUserDialog(longValue)) {
                        TLRPC.User user3 = getMessagesController().getUser(parseLong);
                        if (user3 == null) {
                            try {
                                arrayList13.add(parseLong);
                                longSparseArray.put(longValue, notificationException2);
                            } catch (Exception unused2) {
                                arrayList7 = arrayList24;
                            }
                        } else if (user3.deleted) {
                        }
                        arrayList7 = arrayList24;
                        try {
                            arrayList7.add(notificationException2);
                            hashSet.add(parseLong);
                        } catch (Exception unused3) {
                            arrayList24 = arrayList7;
                        }
                        arrayList24 = arrayList7;
                    }
                }
            }
        }
        ArrayList arrayList26 = arrayList;
        final ArrayList arrayList27 = arrayList24;
        if (arrayList26 != null) {
            Collections.sort(arrayList26, Comparator$-CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda7
                @Override // j$.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    double d;
                    d = ((TLRPC.TL_topPeer) obj).rating;
                    return d;
                }
            }));
            int max = Math.max(0, arrayList.size() - 5);
            while (max < arrayList.size()) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_topPeer) arrayList26.get(max)).peer);
                if (!hashSet.contains(Long.valueOf(peerDialogId))) {
                    NotificationException notificationException3 = new NotificationException();
                    notificationException3.did = peerDialogId;
                    notificationException3.notify = 0;
                    notificationException3.auto = true;
                    notificationException3.story = true;
                    if (DialogObject.isUserDialog(peerDialogId)) {
                        TLRPC.User user4 = getMessagesController().getUser(Long.valueOf(peerDialogId));
                        if (user4 == null) {
                            arrayList13.add(Long.valueOf(peerDialogId));
                            longSparseArray.put(peerDialogId, notificationException3);
                        }
                        arrayList6 = arrayList25;
                        arrayList6.add(0, notificationException3);
                        hashSet.add(Long.valueOf(peerDialogId));
                        max++;
                        arrayList26 = arrayList;
                        arrayList25 = arrayList6;
                    }
                }
                arrayList6 = arrayList25;
                max++;
                arrayList26 = arrayList;
                arrayList25 = arrayList6;
            }
        }
        final ArrayList arrayList28 = arrayList25;
        if (longSparseArray.size() != 0) {
            try {
                if (arrayList15.isEmpty()) {
                    arrayList5 = arrayList2;
                } else {
                    try {
                        arrayList5 = arrayList2;
                    } catch (Exception e) {
                        e = e;
                        arrayList5 = arrayList2;
                        arrayList3 = arrayList17;
                        arrayList4 = arrayList16;
                        FileLog.e(e);
                        size = arrayList3.size();
                        while (i < size) {
                        }
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList<TLRPC.User> arrayList29 = arrayList4;
                        final ArrayList<TLRPC.Chat> arrayList30 = arrayList3;
                        final ArrayList<TLRPC.EncryptedChat> arrayList31 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList29, arrayList30, arrayList31, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
                            }
                        });
                    }
                    try {
                        MessagesStorage.getInstance(this.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", arrayList15), arrayList5, arrayList13);
                    } catch (Exception e2) {
                        e = e2;
                        arrayList3 = arrayList17;
                        arrayList4 = arrayList16;
                        FileLog.e(e);
                        size = arrayList3.size();
                        while (i < size) {
                        }
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList292 = arrayList4;
                        final ArrayList arrayList302 = arrayList3;
                        final ArrayList arrayList312 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList292, arrayList302, arrayList312, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
                            }
                        });
                    }
                }
                if (arrayList13.isEmpty()) {
                    arrayList4 = arrayList16;
                } else {
                    try {
                        arrayList4 = arrayList16;
                    } catch (Exception e3) {
                        e = e3;
                        arrayList4 = arrayList16;
                        arrayList3 = arrayList17;
                        FileLog.e(e);
                        size = arrayList3.size();
                        while (i < size) {
                        }
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList2922 = arrayList4;
                        final ArrayList arrayList3022 = arrayList3;
                        final ArrayList arrayList3122 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList2922, arrayList3022, arrayList3122, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
                            }
                        });
                    }
                    try {
                        MessagesStorage.getInstance(this.currentAccount).getUsersInternal(arrayList13, arrayList4);
                    } catch (Exception e4) {
                        e = e4;
                        arrayList3 = arrayList17;
                        FileLog.e(e);
                        size = arrayList3.size();
                        while (i < size) {
                        }
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList29222 = arrayList4;
                        final ArrayList arrayList30222 = arrayList3;
                        final ArrayList arrayList31222 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList29222, arrayList30222, arrayList31222, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
                            }
                        });
                    }
                }
                if (arrayList14.isEmpty()) {
                    arrayList3 = arrayList17;
                } else {
                    arrayList3 = arrayList17;
                    try {
                        MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", arrayList14), arrayList3);
                    } catch (Exception e5) {
                        e = e5;
                        FileLog.e(e);
                        size = arrayList3.size();
                        while (i < size) {
                        }
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList292222 = arrayList4;
                        final ArrayList arrayList302222 = arrayList3;
                        final ArrayList arrayList312222 = arrayList5;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList292222, arrayList302222, arrayList312222, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
                            }
                        });
                    }
                }
            } catch (Exception e6) {
                e = e6;
                arrayList3 = arrayList17;
                arrayList4 = arrayList16;
                arrayList5 = arrayList2;
            }
            size = arrayList3.size();
            for (i = 0; i < size; i++) {
                TLRPC.Chat chat2 = arrayList3.get(i);
                if (!chat2.left && !chat2.kicked && chat2.migrated_to == null) {
                    ArrayList arrayList32 = arrayList10;
                    NotificationException notificationException4 = (NotificationException) longSparseArray.get(-chat2.id);
                    longSparseArray.remove(-chat2.id);
                    if (notificationException4 == null) {
                        arrayList10 = arrayList32;
                    } else if (!ChatObject.isChannel(chat2) || chat2.megagroup) {
                        arrayList10 = arrayList32;
                        arrayList23.add(notificationException4);
                    } else {
                        arrayList10 = arrayList32;
                        arrayList10.add(notificationException4);
                    }
                }
            }
            size2 = arrayList4.size();
            for (i2 = 0; i2 < size2; i2++) {
                TLRPC.User user5 = arrayList4.get(i2);
                if (!user5.deleted) {
                    longSparseArray.remove(user5.id);
                }
            }
            size3 = arrayList5.size();
            for (i3 = 0; i3 < size3; i3++) {
                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList5.get(i3).id));
            }
            size4 = longSparseArray.size();
            for (i4 = 0; i4 < size4; i4++) {
                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                    arrayList23.remove(longSparseArray.valueAt(i4));
                    arrayList10.remove(longSparseArray.valueAt(i4));
                } else {
                    arrayList8.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList3 = arrayList17;
            arrayList4 = arrayList16;
            arrayList5 = arrayList2;
        }
        final ArrayList arrayList2922222 = arrayList4;
        final ArrayList arrayList3022222 = arrayList3;
        final ArrayList arrayList3122222 = arrayList5;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsSettingsActivity.this.lambda$loadExceptions$1(arrayList2922222, arrayList3022222, arrayList3122222, arrayList8, arrayList23, arrayList10, arrayList27, arrayList28);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showExceptionsAlert$11(ArrayList arrayList, ArrayList arrayList2, AlertDialog alertDialog, int i) {
        presentFragment(new NotificationsCustomSettingsActivity(-1, arrayList, arrayList2));
    }

    private void loadExceptions() {
        MediaDataController.getInstance(this.currentAccount).loadHints(true);
        final ArrayList arrayList = new ArrayList(MediaDataController.getInstance(this.currentAccount).hints);
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsSettingsActivity.this.lambda$loadExceptions$2(arrayList);
            }
        });
    }

    private void showExceptionsAlert(int i, final Runnable runnable) {
        final ArrayList arrayList;
        String formatPluralString;
        final ArrayList arrayList2;
        String str = null;
        if (i == this.storiesRow) {
            arrayList = this.exceptionStories;
            arrayList2 = this.exceptionAutoStories;
            if (arrayList != null && !arrayList.isEmpty()) {
                str = LocaleController.formatPluralString("ChatsException", arrayList.size(), new Object[0]);
            }
        } else if (i == this.privateRow) {
            arrayList = this.exceptionUsers;
            if (arrayList != null && !arrayList.isEmpty()) {
                formatPluralString = LocaleController.formatPluralString("ChatsException", arrayList.size(), new Object[0]);
                str = formatPluralString;
                arrayList2 = null;
            }
            arrayList2 = null;
        } else if (i == this.groupRow) {
            arrayList = this.exceptionChats;
            if (arrayList != null && !arrayList.isEmpty()) {
                formatPluralString = LocaleController.formatPluralString("Groups", arrayList.size(), new Object[0]);
                str = formatPluralString;
                arrayList2 = null;
            }
            arrayList2 = null;
        } else {
            if (i == this.reactionsRow) {
                runnable.run();
                return;
            }
            arrayList = this.exceptionChannels;
            if (arrayList != null && !arrayList.isEmpty()) {
                formatPluralString = LocaleController.formatPluralString("Channels", arrayList.size(), new Object[0]);
                str = formatPluralString;
                arrayList2 = null;
            }
            arrayList2 = null;
        }
        if (str == null) {
            runnable.run();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(AndroidUtilities.replaceTags(arrayList.size() == 1 ? LocaleController.formatString("NotificationsExceptionsSingleAlert", R.string.NotificationsExceptionsSingleAlert, str) : LocaleController.formatString("NotificationsExceptionsAlert", R.string.NotificationsExceptionsAlert, str)));
        builder.setTitle(LocaleController.getString("NotificationsExceptions", R.string.NotificationsExceptions));
        builder.setNeutralButton(LocaleController.getString("ViewExceptions", R.string.ViewExceptions), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                NotificationsSettingsActivity.this.lambda$showExceptionsAlert$11(arrayList, arrayList2, alertDialog, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                runnable.run();
            }
        });
        showDialog(builder.create());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("NotificationsAndSounds", R.string.NotificationsAndSounds));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.NotificationsSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    NotificationsSettingsActivity.this.lambda$onBackPressed$323();
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.NotificationsSettingsActivity.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.NotificationsSettingsActivity$$ExternalSyntheticLambda0
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
                NotificationsSettingsActivity.this.lambda$createView$10(view, i, f, f2);
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextSettingsCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
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
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        int i7 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        String str;
        String str2;
        Ringtone ringtone;
        int i3;
        String str3;
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri == null || (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) == null) {
                str = null;
            } else {
                if (i == this.callsRingtoneRow) {
                    if (uri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
                        i3 = R.string.DefaultRingtone;
                        str3 = "DefaultRingtone";
                        str = LocaleController.getString(str3, i3);
                    }
                    str = ringtone.getTitle(getParentActivity());
                } else {
                    if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                        i3 = R.string.SoundDefault;
                        str3 = "SoundDefault";
                        str = LocaleController.getString(str3, i3);
                    }
                    str = ringtone.getTitle(getParentActivity());
                }
                ringtone.stop();
            }
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            if (i == this.callsRingtoneRow) {
                if (str == null || uri == null) {
                    str2 = "NoSound";
                    edit.putString("CallsRingtone", "NoSound");
                } else {
                    edit.putString("CallsRingtone", str);
                    str2 = uri.toString();
                }
                edit.putString("CallsRingtonePath", str2);
                this.updateRingtone = true;
            }
            edit.commit();
            this.adapter.notifyItemChanged(i);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        MessagesController.getInstance(this.currentAccount).loadSignUpNotificationsSettings();
        loadExceptions();
        if (UserConfig.getActivatedAccountsCount() > 1) {
            int i = this.rowCount;
            this.accountsSectionRow = i;
            this.accountsAllRow = i + 1;
            this.rowCount = i + 3;
            this.accountsInfoRow = i + 2;
        } else {
            this.accountsSectionRow = -1;
            this.accountsAllRow = -1;
            this.accountsInfoRow = -1;
        }
        int i2 = this.rowCount;
        this.notificationsSectionRow = i2;
        this.privateRow = i2 + 1;
        this.groupRow = i2 + 2;
        this.channelsRow = i2 + 3;
        this.storiesRow = i2 + 4;
        this.reactionsRow = i2 + 5;
        this.notificationsSection2Row = i2 + 6;
        this.callsSectionRow = i2 + 7;
        this.callsVibrateRow = i2 + 8;
        this.callsRingtoneRow = i2 + 9;
        this.eventsSection2Row = i2 + 10;
        this.badgeNumberSection = i2 + 11;
        this.badgeNumberShowRow = i2 + 12;
        this.badgeNumberMutedRow = i2 + 13;
        this.badgeNumberMessagesRow = i2 + 14;
        this.badgeNumberSection2Row = i2 + 15;
        this.inappSectionRow = i2 + 16;
        this.inappSoundRow = i2 + 17;
        this.inappVibrateRow = i2 + 18;
        this.inappPreviewRow = i2 + 19;
        int i3 = i2 + 21;
        this.rowCount = i3;
        this.inchatSoundRow = i2 + 20;
        if (Build.VERSION.SDK_INT >= 21) {
            this.rowCount = i2 + 22;
            this.inappPriorityRow = i3;
        } else {
            this.inappPriorityRow = -1;
        }
        int i4 = this.rowCount;
        this.callsSection2Row = i4;
        this.eventsSectionRow = i4 + 1;
        this.contactJoinedRow = i4 + 2;
        this.pinnedMessageRow = i4 + 3;
        this.otherSection2Row = i4 + 4;
        this.otherSectionRow = i4 + 5;
        this.notificationsServiceRow = i4 + 6;
        this.notificationsServiceConnectionRow = i4 + 7;
        this.androidAutoAlertRow = -1;
        this.repeatRow = i4 + 8;
        this.resetSection2Row = i4 + 9;
        this.resetSectionRow = i4 + 10;
        this.resetNotificationsRow = i4 + 11;
        this.rowCount = i4 + 13;
        this.resetNotificationsSectionRow = i4 + 12;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        getMessagesController().reloadReactionsNotifySettings();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
