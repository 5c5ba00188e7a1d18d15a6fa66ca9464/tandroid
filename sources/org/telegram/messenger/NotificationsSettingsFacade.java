package org.telegram.messenger;

import android.content.SharedPreferences;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.NotificationsSoundActivity;

/* loaded from: classes3.dex */
public class NotificationsSettingsFacade {
    public static final String PROPERTY_CONTENT_PREVIEW = "content_preview_";
    public static final String PROPERTY_CUSTOM = "custom_";
    public static final String PROPERTY_NOTIFY = "notify2_";
    public static final String PROPERTY_NOTIFY_UNTIL = "notifyuntil_";
    public static final String PROPERTY_SILENT = "silent_";
    public static final String PROPERTY_STORIES_NOTIFY = "stories_";
    private final int currentAccount;

    public NotificationsSettingsFacade(int i) {
        this.currentAccount = i;
    }

    private SharedPreferences getPreferences() {
        return MessagesController.getNotificationsSettings(this.currentAccount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyDialogNotificationsSettings$0() {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01c1, code lost:
    
        if (r22 == 0) goto L68;
     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0198  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$applyDialogNotificationsSettings$1(long j, long j2, TLRPC.PeerNotifySettings peerNotifySettings) {
        MessagesStorage messagesStorage;
        boolean z;
        long j3;
        int i;
        int i2;
        long j4;
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2, true);
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance(this.currentAccount);
        MessagesStorage messagesStorage2 = MessagesStorage.getInstance(this.currentAccount);
        NotificationsController notificationsController = NotificationsController.getInstance(this.currentAccount);
        int i3 = getPreferences().getInt(PROPERTY_NOTIFY + sharedPrefKey, -1);
        int i4 = getPreferences().getInt(PROPERTY_NOTIFY_UNTIL + sharedPrefKey, 0);
        SharedPreferences.Editor edit = getPreferences().edit();
        if ((peerNotifySettings.flags & 2) != 0) {
            edit.putBoolean(PROPERTY_SILENT + sharedPrefKey, peerNotifySettings.silent);
        } else {
            edit.remove(PROPERTY_SILENT + sharedPrefKey);
        }
        if ((peerNotifySettings.flags & 64) != 0) {
            edit.putBoolean(PROPERTY_STORIES_NOTIFY + sharedPrefKey, !peerNotifySettings.stories_muted);
        } else {
            edit.remove(PROPERTY_STORIES_NOTIFY + sharedPrefKey);
        }
        TLRPC.Dialog dialog = j2 == 0 ? (TLRPC.Dialog) messagesController.dialogs_dict.get(j) : null;
        if (dialog != null) {
            dialog.notify_settings = peerNotifySettings;
        }
        if ((peerNotifySettings.flags & 4) == 0) {
            messagesStorage = messagesStorage2;
            z = true;
            if (i3 != -1) {
                if (dialog != null) {
                    dialog.notify_settings.mute_until = 0;
                }
                edit.remove(PROPERTY_NOTIFY + sharedPrefKey);
            } else {
                z = false;
            }
            j3 = 0;
        } else if (peerNotifySettings.mute_until > connectionsManager.getCurrentTime()) {
            if (peerNotifySettings.mute_until > connectionsManager.getCurrentTime() + 31536000) {
                if (i3 != 2) {
                    edit.putInt(PROPERTY_NOTIFY + sharedPrefKey, 2);
                    if (dialog != null) {
                        dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    }
                    z = true;
                } else {
                    z = false;
                }
                j4 = 0;
                i2 = 0;
            } else {
                if (i3 == 3 && i4 == peerNotifySettings.mute_until) {
                    z = false;
                } else {
                    edit.putInt(PROPERTY_NOTIFY + sharedPrefKey, 3);
                    edit.putInt(PROPERTY_NOTIFY_UNTIL + sharedPrefKey, peerNotifySettings.mute_until);
                    if (dialog != null) {
                        dialog.notify_settings.mute_until = 0;
                    }
                    z = true;
                }
                i2 = peerNotifySettings.mute_until;
                j4 = 0;
            }
            if (j2 == j4) {
                messagesStorage2.setDialogFlags(j, (i2 << 32) | 1);
                notificationsController.removeNotificationsForDialog(j);
            }
        } else {
            messagesStorage = messagesStorage2;
            if (i3 != 0) {
                z = true;
                if (i3 != 1) {
                    if (dialog != null) {
                        i = 0;
                        dialog.notify_settings.mute_until = 0;
                    } else {
                        i = 0;
                    }
                    edit.putInt(PROPERTY_NOTIFY + sharedPrefKey, i);
                    if (j2 == 0) {
                        j3 = 0;
                        messagesStorage.setDialogFlags(j, j3);
                    }
                }
            }
            z = false;
            if (j2 == 0) {
            }
        }
        boolean z2 = z;
        applySoundSettings(peerNotifySettings.android_sound, edit, j, j2, 0, false);
        edit.apply();
        if (z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsSettingsFacade$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsSettingsFacade.this.lambda$applyDialogNotificationsSettings$0();
                }
            });
        }
    }

    public void applyDialogNotificationsSettings(final long j, final long j2, final TLRPC.PeerNotifySettings peerNotifySettings) {
        if (peerNotifySettings == null) {
            return;
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsSettingsFacade$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsSettingsFacade.this.lambda$applyDialogNotificationsSettings$1(j, j2, peerNotifySettings);
            }
        });
    }

    public void applySoundSettings(TLRPC.NotificationSound notificationSound, SharedPreferences.Editor editor, long j, long j2, int i, boolean z) {
        String str;
        String str2;
        String str3;
        if (notificationSound == null) {
            return;
        }
        if (j != 0) {
            String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2, true);
            str = "sound_" + sharedPrefKey;
            str3 = "sound_path_" + sharedPrefKey;
            str2 = "sound_document_id_" + sharedPrefKey;
        } else if (i == 0) {
            str = "GroupSound";
            str2 = "GroupSoundDocId";
            str3 = "GroupSoundPath";
        } else if (i == 3) {
            str = "StoriesSound";
            str2 = "StoriesSoundDocId";
            str3 = "StoriesSoundPath";
        } else if (i == 1) {
            str = "GlobalSound";
            str2 = "GlobalSoundDocId";
            str3 = "GlobalSoundPath";
        } else if (i == 4 || i == 5) {
            str = "ReactionSound";
            str2 = "ReactionSoundDocId";
            str3 = "ReactionSoundPath";
        } else {
            str = "ChannelSound";
            str2 = "ChannelSoundDocId";
            str3 = "ChannelSoundPath";
        }
        if (notificationSound instanceof TLRPC.TL_notificationSoundLocal) {
            TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal = (TLRPC.TL_notificationSoundLocal) notificationSound;
            if ("Default".equalsIgnoreCase(tL_notificationSoundLocal.data)) {
                notificationSound = new TLRPC.TL_notificationSoundDefault();
            } else if ("NoSound".equalsIgnoreCase(tL_notificationSoundLocal.data)) {
                notificationSound = new TLRPC.TL_notificationSoundNone();
            } else {
                String findRingtonePathByName = NotificationsSoundActivity.findRingtonePathByName(tL_notificationSoundLocal.title);
                if (findRingtonePathByName == null) {
                    return;
                } else {
                    tL_notificationSoundLocal.data = findRingtonePathByName;
                }
            }
        }
        if (notificationSound instanceof TLRPC.TL_notificationSoundDefault) {
            editor.putString(str, "Default");
            editor.putString(str3, "Default");
        } else if (notificationSound instanceof TLRPC.TL_notificationSoundNone) {
            editor.putString(str, "NoSound");
            editor.putString(str3, "NoSound");
        } else {
            if (!(notificationSound instanceof TLRPC.TL_notificationSoundLocal)) {
                if (notificationSound instanceof TLRPC.TL_notificationSoundRingtone) {
                    TLRPC.TL_notificationSoundRingtone tL_notificationSoundRingtone = (TLRPC.TL_notificationSoundRingtone) notificationSound;
                    editor.putLong(str2, tL_notificationSoundRingtone.id);
                    MediaDataController.getInstance(this.currentAccount).checkRingtones(true);
                    if (z && j != 0) {
                        editor.putBoolean(PROPERTY_CUSTOM + j, true);
                    }
                    MediaDataController.getInstance(this.currentAccount).ringtoneDataStore.getDocument(tL_notificationSoundRingtone.id);
                    return;
                }
                return;
            }
            TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal2 = (TLRPC.TL_notificationSoundLocal) notificationSound;
            editor.putString(str, tL_notificationSoundLocal2.title);
            editor.putString(str3, tL_notificationSoundLocal2.data);
        }
        editor.remove(str2);
    }

    public void clearPreference(long j, long j2) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2, true);
        getPreferences().edit().remove(PROPERTY_NOTIFY + sharedPrefKey).remove(PROPERTY_CUSTOM + sharedPrefKey).remove(PROPERTY_NOTIFY_UNTIL + sharedPrefKey).remove(PROPERTY_CONTENT_PREVIEW + sharedPrefKey).remove(PROPERTY_SILENT + sharedPrefKey).remove(PROPERTY_STORIES_NOTIFY + sharedPrefKey).apply();
    }

    public int getProperty(String str, long j, long j2, int i) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2, true);
        if (getPreferences().contains(str + sharedPrefKey)) {
            return getPreferences().getInt(str + sharedPrefKey, i);
        }
        String sharedPrefKey2 = NotificationsController.getSharedPrefKey(j, 0L, true);
        return getPreferences().getInt(str + sharedPrefKey2, i);
    }

    public long getProperty(String str, long j, long j2, long j3) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2, true);
        if (getPreferences().contains(str + sharedPrefKey)) {
            return getPreferences().getLong(str + sharedPrefKey, j3);
        }
        String sharedPrefKey2 = NotificationsController.getSharedPrefKey(j, 0L, true);
        return getPreferences().getLong(str + sharedPrefKey2, j3);
    }

    public boolean getProperty(String str, long j, long j2, boolean z) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2);
        if (getPreferences().contains(str + sharedPrefKey)) {
            return getPreferences().getBoolean(str + sharedPrefKey, z);
        }
        String sharedPrefKey2 = NotificationsController.getSharedPrefKey(j, 0L);
        return getPreferences().getBoolean(str + sharedPrefKey2, z);
    }

    public String getPropertyString(String str, long j, long j2, String str2) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2);
        if (getPreferences().contains(str + sharedPrefKey)) {
            return getPreferences().getString(str + sharedPrefKey, str2);
        }
        String sharedPrefKey2 = NotificationsController.getSharedPrefKey(j, 0L);
        return getPreferences().getString(str + sharedPrefKey2, str2);
    }

    public boolean isDefault(long j, long j2) {
        NotificationsController.getSharedPrefKey(j, j2, true);
        return false;
    }

    public void removeProperty(String str, long j, long j2) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2);
        getPreferences().edit().remove(str + sharedPrefKey).apply();
    }

    public void setSettingsForDialog(SharedPreferences.Editor editor, TLRPC.Dialog dialog, TLRPC.PeerNotifySettings peerNotifySettings) {
        long peerId = MessageObject.getPeerId(dialog.peer);
        if ((dialog.notify_settings.flags & 2) != 0) {
            editor.putBoolean(PROPERTY_SILENT + peerId, dialog.notify_settings.silent);
        } else {
            editor.remove(PROPERTY_SILENT + peerId);
        }
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance(this.currentAccount);
        TLRPC.PeerNotifySettings peerNotifySettings2 = dialog.notify_settings;
        if ((peerNotifySettings2.flags & 4) == 0) {
            editor.remove(PROPERTY_NOTIFY + peerId);
            return;
        }
        if (peerNotifySettings2.mute_until <= connectionsManager.getCurrentTime()) {
            editor.putInt(PROPERTY_NOTIFY + peerId, 0);
            return;
        }
        if (dialog.notify_settings.mute_until > connectionsManager.getCurrentTime() + 31536000) {
            editor.putInt(PROPERTY_NOTIFY + peerId, 2);
            dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
            return;
        }
        editor.putInt(PROPERTY_NOTIFY + peerId, 3);
        editor.putInt(PROPERTY_NOTIFY_UNTIL + peerId, dialog.notify_settings.mute_until);
    }
}
