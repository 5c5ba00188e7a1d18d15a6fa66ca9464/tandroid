package org.telegram.messenger;

import android.content.SharedPreferences;
import android.util.LongSparseArray;
import org.telegram.messenger.NotificationBadge;
/* loaded from: classes.dex */
public class SaveToGallerySettingsHelper {
    public static String CHANNELS_PREF_NAME = "channels_save_gallery_exceptions";
    public static final long DEFAULT_VIDEO_LIMIT = 104857600;
    public static String GROUPS_PREF_NAME = "groups_save_gallery_exceptions";
    public static final long MAX_VIDEO_LIMIT = 4194304000L;
    public static String USERS_PREF_NAME = "users_save_gallery_exceptions";
    public static SharedSettings channels;
    public static SharedSettings groups;
    public static SharedSettings user;

    public static void load(SharedPreferences sharedPreferences) {
        int i = (sharedPreferences.getBoolean("save_gallery", false) && BuildVars.NO_SCOPED_STORAGE) ? 7 : sharedPreferences.getInt("save_gallery_flags", -1);
        if (i != -1) {
            sharedPreferences.edit().remove("save_gallery").remove("save_gallery_flags").apply();
            SharedSettings sharedSettings = new SharedSettings();
            user = sharedSettings;
            boolean z = (i & 1) != 0;
            sharedSettings.saveVideo = z;
            sharedSettings.savePhoto = z;
            sharedSettings.limitVideo = DEFAULT_VIDEO_LIMIT;
            sharedSettings.save("user", sharedPreferences);
            SharedSettings sharedSettings2 = new SharedSettings();
            groups = sharedSettings2;
            SharedSettings sharedSettings3 = user;
            boolean z2 = (i & 2) != 0;
            sharedSettings3.saveVideo = z2;
            sharedSettings2.savePhoto = z2;
            sharedSettings2.limitVideo = DEFAULT_VIDEO_LIMIT;
            sharedSettings2.save("groups", sharedPreferences);
            SharedSettings sharedSettings4 = new SharedSettings();
            channels = sharedSettings4;
            boolean z3 = (i & 4) != 0;
            sharedSettings4.saveVideo = z3;
            sharedSettings4.savePhoto = z3;
            sharedSettings4.limitVideo = DEFAULT_VIDEO_LIMIT;
            sharedSettings4.save("channels", sharedPreferences);
        } else {
            user = SharedSettings.read("user", sharedPreferences);
            groups = SharedSettings.read("groups", sharedPreferences);
            channels = SharedSettings.read("channels", sharedPreferences);
        }
        user.type = 1;
        groups.type = 2;
        channels.type = 4;
    }

    public static boolean needSave(int i, MessageObject messageObject, int i2) {
        SharedSettings sharedSettings;
        if (i == 1) {
            sharedSettings = user;
        } else if (i == 4) {
            sharedSettings = channels;
        } else if (i != 2) {
            return false;
        } else {
            sharedSettings = groups;
        }
        return sharedSettings.needSave(messageObject, i2);
    }

    public static LongSparseArray<DialogException> loadExceptions(SharedPreferences sharedPreferences) {
        LongSparseArray<DialogException> longSparseArray = new LongSparseArray<>();
        int i = sharedPreferences.getInt(NotificationBadge.NewHtcHomeBadger.COUNT, 0);
        for (int i2 = 0; i2 < i; i2++) {
            DialogException dialogException = new DialogException();
            dialogException.dialogId = sharedPreferences.getLong(i2 + "_dialog_id", 0L);
            dialogException.savePhoto = sharedPreferences.getBoolean(i2 + "_photo", false);
            dialogException.saveVideo = sharedPreferences.getBoolean(i2 + "_video", false);
            dialogException.limitVideo = sharedPreferences.getLong(i2 + "_limitVideo", DEFAULT_VIDEO_LIMIT);
            long j = dialogException.dialogId;
            if (j != 0) {
                longSparseArray.put(j, dialogException);
            }
        }
        return longSparseArray;
    }

    public static void saveExceptions(SharedPreferences sharedPreferences, LongSparseArray<DialogException> longSparseArray) {
        sharedPreferences.edit().clear().apply();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(NotificationBadge.NewHtcHomeBadger.COUNT, longSparseArray.size());
        for (int i = 0; i < longSparseArray.size(); i++) {
            DialogException valueAt = longSparseArray.valueAt(i);
            edit.putLong(i + "_dialog_id", valueAt.dialogId);
            edit.putBoolean(i + "_photo", valueAt.savePhoto);
            edit.putBoolean(i + "_video", valueAt.saveVideo);
            edit.putLong(i + "_limitVideo", valueAt.limitVideo);
        }
        edit.apply();
    }

    public static Settings getSettings(int i) {
        if (i == 1) {
            return user;
        }
        if (i == 2) {
            return groups;
        }
        if (i == 4) {
            return channels;
        }
        return null;
    }

    public static void saveSettings(int i) {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        if (i == 1) {
            user.save("user", sharedPreferences);
        } else if (i == 2) {
            groups.save("group", sharedPreferences);
        } else if (i == 4) {
            channels.save("channels", sharedPreferences);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Settings {
        public long limitVideo = SaveToGallerySettingsHelper.DEFAULT_VIDEO_LIMIT;
        public boolean savePhoto;
        public boolean saveVideo;

        public abstract CharSequence createDescription(int i);

        public boolean enabled() {
            return this.savePhoto || this.saveVideo;
        }

        public void toggle() {
            if (enabled()) {
                this.saveVideo = false;
                this.savePhoto = false;
                return;
            }
            this.savePhoto = true;
            this.saveVideo = true;
        }
    }

    /* loaded from: classes.dex */
    public static class SharedSettings extends Settings {
        private int type;

        /* JADX INFO: Access modifiers changed from: private */
        public void save(String str, SharedPreferences sharedPreferences) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            SharedPreferences.Editor putBoolean = edit.putBoolean(str + "_save_gallery_photo", this.savePhoto);
            SharedPreferences.Editor putBoolean2 = putBoolean.putBoolean(str + "_save_gallery_video", this.saveVideo);
            putBoolean2.putLong(str + "_save_gallery_limitVideo", this.limitVideo).apply();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SharedSettings read(String str, SharedPreferences sharedPreferences) {
            SharedSettings sharedSettings = new SharedSettings();
            sharedSettings.savePhoto = sharedPreferences.getBoolean(str + "_save_gallery_photo", false);
            sharedSettings.saveVideo = sharedPreferences.getBoolean(str + "_save_gallery_video", false);
            sharedSettings.limitVideo = sharedPreferences.getLong(str + "_save_gallery_limitVideo", SaveToGallerySettingsHelper.DEFAULT_VIDEO_LIMIT);
            return sharedSettings;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean needSave(MessageObject messageObject, int i) {
            DialogException dialogException = UserConfig.getInstance(i).getSaveGalleryExceptions(this.type).get(messageObject.getDialogId());
            if (messageObject.isOutOwner()) {
                return false;
            }
            if (dialogException != null) {
                if (messageObject.isVideo()) {
                    if (dialogException.saveVideo && (dialogException.limitVideo == -1 || messageObject.getSize() < dialogException.limitVideo)) {
                        return true;
                    }
                } else if (dialogException.savePhoto) {
                    return true;
                }
            } else if (messageObject.isVideo()) {
                if (this.saveVideo && (this.limitVideo == -1 || messageObject.getSize() < this.limitVideo)) {
                    return true;
                }
            } else if (this.savePhoto) {
                return true;
            }
            return false;
        }

        @Override // org.telegram.messenger.SaveToGallerySettingsHelper.Settings
        public CharSequence createDescription(int i) {
            StringBuilder sb = new StringBuilder();
            if (enabled()) {
                if (this.savePhoto) {
                    sb.append(LocaleController.getString("SaveToGalleryPhotos", R.string.SaveToGalleryPhotos));
                }
                if (this.saveVideo) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(LocaleController.getString("SaveToGalleryVideos", R.string.SaveToGalleryVideos));
                    long j = this.limitVideo;
                    if (j > 0 && j < 4194304000L) {
                        sb.append(" (");
                        sb.append(AndroidUtilities.formatFileSize(this.limitVideo, true));
                        sb.append(")");
                    }
                }
            } else {
                sb.append(LocaleController.getString("SaveToGalleryOff", R.string.SaveToGalleryOff));
            }
            LongSparseArray<DialogException> saveGalleryExceptions = UserConfig.getInstance(i).getSaveGalleryExceptions(this.type);
            if (saveGalleryExceptions.size() != 0) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(LocaleController.formatPluralString("Exception", saveGalleryExceptions.size(), Integer.valueOf(saveGalleryExceptions.size())));
            }
            return sb;
        }
    }

    /* loaded from: classes.dex */
    public static class DialogException extends Settings {
        public long dialogId;

        @Override // org.telegram.messenger.SaveToGallerySettingsHelper.Settings
        public CharSequence createDescription(int i) {
            StringBuilder sb = new StringBuilder();
            if (enabled()) {
                if (this.savePhoto) {
                    sb.append(LocaleController.getString("SaveToGalleryPhotos", R.string.SaveToGalleryPhotos));
                }
                if (this.saveVideo) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    long j = this.limitVideo;
                    if (j <= 0 || j >= 4194304000L) {
                        sb.append(LocaleController.formatString("SaveToGalleryVideos", R.string.SaveToGalleryVideos, new Object[0]));
                    } else {
                        sb.append(LocaleController.formatString("SaveToGalleryVideosUpTo", R.string.SaveToGalleryVideosUpTo, AndroidUtilities.formatFileSize(j, true)));
                    }
                }
            } else {
                sb.append(LocaleController.getString("SaveToGalleryOff", R.string.SaveToGalleryOff));
            }
            return sb;
        }
    }
}
