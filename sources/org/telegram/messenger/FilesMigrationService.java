package org.telegram.messenger;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import j$.util.function.Consumer;
import j$.util.stream.Stream;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import org.telegram.messenger.FilesMigrationService;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.StickerImageView;

/* loaded from: classes3.dex */
public class FilesMigrationService extends Service {
    public static FilesMigrationBottomSheet filesMigrationBottomSheet = null;
    public static boolean hasOldFolder = false;
    public static boolean isRunning = false;
    private static boolean wasShown = false;
    long lastUpdateTime;
    private int movedFilesCount;
    private int totalFilesCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends Thread {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            FilesMigrationService.isRunning = false;
            FilesMigrationService.this.stopForeground(true);
            FilesMigrationService.this.stopSelf();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            FilesMigrationService.this.migrateOldFolder();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FilesMigrationService$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FilesMigrationService.1.this.lambda$run$0();
                }
            });
        }
    }

    /* loaded from: classes3.dex */
    public static class FilesMigrationBottomSheet extends BottomSheet {
        BaseFragment fragment;

        public FilesMigrationBottomSheet(BaseFragment baseFragment) {
            super(baseFragment.getParentActivity(), false);
            this.fragment = baseFragment;
            setCanceledOnTouchOutside(false);
            Activity parentActivity = baseFragment.getParentActivity();
            LinearLayout linearLayout = new LinearLayout(parentActivity);
            linearLayout.setOrientation(1);
            StickerImageView stickerImageView = new StickerImageView(parentActivity, this.currentAccount);
            stickerImageView.setStickerNum(7);
            stickerImageView.getImageReceiver().setAutoRepeat(1);
            linearLayout.addView(stickerImageView, LayoutHelper.createLinear(NotificationCenter.messagePlayingProgressDidChanged, NotificationCenter.messagePlayingProgressDidChanged, 1, 0, 16, 0, 0));
            TextView textView = new TextView(parentActivity);
            textView.setGravity(8388611);
            int i = Theme.key_dialogTextBlack;
            textView.setTextColor(Theme.getColor(i));
            textView.setTextSize(1, 20.0f);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setText(LocaleController.getString(R.string.MigrateOldFolderTitle));
            linearLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 30.0f, 21.0f, 0.0f));
            TextView textView2 = new TextView(parentActivity);
            textView2.setGravity(8388611);
            textView2.setTextSize(1, 15.0f);
            textView2.setTextColor(Theme.getColor(i));
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.MigrateOldFolderDescription)));
            linearLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 15.0f, 21.0f, 16.0f));
            TextView textView3 = new TextView(parentActivity);
            textView3.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
            textView3.setGravity(17);
            textView3.setTextSize(1, 14.0f);
            textView3.setTypeface(AndroidUtilities.bold());
            textView3.setText(LocaleController.getString(R.string.MigrateOldFolderButton));
            textView3.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            textView3.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 6.0f));
            linearLayout.addView(textView3, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 15.0f, 16.0f, 16.0f));
            textView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.messenger.FilesMigrationService$FilesMigrationBottomSheet$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    FilesMigrationService.FilesMigrationBottomSheet.this.lambda$new$0(view);
                }
            });
            ScrollView scrollView = new ScrollView(parentActivity);
            scrollView.addView(linearLayout);
            setCustomView(scrollView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            migrateOldFolder();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.ActionBar.BottomSheet
        public boolean canDismissWithSwipe() {
            return false;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        protected boolean canDismissWithTouchOutside() {
            return false;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
        public void dismiss() {
            super.dismiss();
            FilesMigrationService.filesMigrationBottomSheet = null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0031, code lost:
        
            if (r11 != 0) goto L14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x003b, code lost:
        
            r3 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0039, code lost:
        
            if (r11 == 0) goto L17;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void migrateOldFolder() {
            int checkSelfPermission;
            int checkSelfPermission2;
            int checkSelfPermission3;
            int checkSelfPermission4;
            int checkSelfPermission5;
            Activity parentActivity = this.fragment.getParentActivity();
            checkSelfPermission = parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            boolean z = false;
            boolean z2 = checkSelfPermission == 0;
            int i = Build.VERSION.SDK_INT;
            if (i >= 33) {
                checkSelfPermission3 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_IMAGES");
                if (checkSelfPermission3 == 0) {
                    checkSelfPermission4 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_VIDEO");
                    if (checkSelfPermission4 == 0) {
                        checkSelfPermission5 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_AUDIO");
                    }
                }
            }
            if (i < 33) {
                checkSelfPermission2 = parentActivity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
            }
            if (z && z2) {
                FilesMigrationService.start();
                dismiss();
                return;
            }
            ArrayList arrayList = new ArrayList();
            if (!z) {
                if (i >= 33) {
                    arrayList.add("android.permission.READ_MEDIA_IMAGES");
                    arrayList.add("android.permission.READ_MEDIA_VIDEO");
                    arrayList.add("android.permission.READ_MEDIA_AUDIO");
                } else {
                    arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
                }
            }
            if (!z2) {
                arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
            parentActivity.requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 4);
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
        public /* bridge */ /* synthetic */ void setLastVisible(boolean z) {
            BaseFragment.AttachedSheet.-CC.$default$setLastVisible(this, z);
        }
    }

    public static void checkBottomSheet(BaseFragment baseFragment) {
        boolean isExternalStorageLegacy;
        SharedPreferences.Editor putBoolean;
        ArrayList<File> rootDirs;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0);
        isExternalStorageLegacy = Environment.isExternalStorageLegacy();
        if (!isExternalStorageLegacy || sharedPreferences.getBoolean("migration_to_scoped_storage_finished", false) || sharedPreferences.getInt("migration_to_scoped_storage_count", 0) >= 5 || wasShown || filesMigrationBottomSheet != null || isRunning) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (!TextUtils.isEmpty(SharedConfig.storageCacheDir) && (rootDirs = AndroidUtilities.getRootDirs()) != null) {
                int size = rootDirs.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    File file = rootDirs.get(i);
                    if (file.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                        externalStorageDirectory = file;
                        break;
                    }
                    i++;
                }
            }
            hasOldFolder = new File(externalStorageDirectory, "Telegram").exists();
        }
        if (hasOldFolder) {
            FilesMigrationBottomSheet filesMigrationBottomSheet2 = new FilesMigrationBottomSheet(baseFragment);
            filesMigrationBottomSheet = filesMigrationBottomSheet2;
            filesMigrationBottomSheet2.show();
            wasShown = true;
            putBoolean = sharedPreferences.edit().putInt("migration_to_scoped_storage_count", sharedPreferences.getInt("migration_to_scoped_storage_count", 0) + 1);
        } else {
            putBoolean = sharedPreferences.edit().putBoolean("migration_to_scoped_storage_finished", true);
        }
        putBoolean.apply();
    }

    private int getFilesCount(File file) {
        File[] listFiles;
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < listFiles.length; i2++) {
            i = listFiles[i2].isDirectory() ? i + getFilesCount(listFiles[i2]) : i + 1;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$moveDirectory$0(File file, Path path) {
        Path fileName;
        String path2;
        boolean isDirectory;
        File file2;
        Path path3;
        File file3;
        fileName = path.getFileName();
        path2 = fileName.toString();
        File file4 = new File(file, path2);
        isDirectory = Files.isDirectory(path, new LinkOption[0]);
        if (isDirectory) {
            file3 = path.toFile();
            moveDirectory(file3, file4);
            return;
        }
        try {
            path3 = file4.toPath();
            Files.move(path, path3, new CopyOption[0]);
        } catch (Exception e) {
            FileLog.e((Throwable) e, false);
            try {
                file2 = path.toFile();
                file2.delete();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        this.movedFilesCount++;
        updateProgress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateProgress$1(int i) {
        ((NotificationManager) getSystemService("notification")).notify(NotificationCenter.storiesReadUpdated, new Notification.Builder(this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setContentTitle(getText(R.string.MigratingFiles)).setContentText(String.format("%s/%s", Integer.valueOf(i), Integer.valueOf(this.totalFilesCount))).setSmallIcon(R.drawable.notification).setAutoCancel(false).setProgress(this.totalFilesCount, i, false).build());
    }

    private void moveDirectory(File file, final File file2) {
        Path path;
        Stream convert;
        if (file.exists()) {
            if (file2.exists() || file2.mkdir()) {
                try {
                    path = file.toPath();
                    convert = Stream.VivifiedWrapper.convert(Files.list(path));
                    try {
                        convert.forEach(new Consumer() { // from class: org.telegram.messenger.FilesMigrationService$$ExternalSyntheticLambda8
                            @Override // j$.util.function.Consumer
                            /* renamed from: accept */
                            public final void r(Object obj) {
                                FilesMigrationService.this.lambda$moveDirectory$0(file2, (Path) obj);
                            }

                            @Override // j$.util.function.Consumer
                            public /* synthetic */ Consumer andThen(Consumer consumer) {
                                return Consumer.-CC.$default$andThen(this, consumer);
                            }
                        });
                        convert.close();
                    } finally {
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                try {
                    file.delete();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    public static void start() {
        ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) FilesMigrationService.class));
    }

    private void updateProgress() {
        if (System.currentTimeMillis() - this.lastUpdateTime > 20 || this.movedFilesCount >= this.totalFilesCount - 1) {
            final int i = this.movedFilesCount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FilesMigrationService$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    FilesMigrationService.this.lambda$updateProgress$1(i);
                }
            });
        }
    }

    public void migrateOldFolder() {
        ArrayList<File> rootDirs;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir) && (rootDirs = AndroidUtilities.getRootDirs()) != null) {
            int size = rootDirs.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                File file = rootDirs.get(i);
                if (file.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                    externalStorageDirectory = file;
                    break;
                }
                i++;
            }
        }
        File file2 = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
        File file3 = new File(externalStorageDirectory, "Telegram");
        this.totalFilesCount = getFilesCount(file3);
        long currentTimeMillis = System.currentTimeMillis();
        if (file3.canRead() && file3.canWrite()) {
            moveDirectory(file3, file2);
        }
        FileLog.d("move time = " + (System.currentTimeMillis() - currentTimeMillis));
        ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit().putBoolean("migration_to_scoped_storage_finished", true).apply();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        NotificationsController.checkOtherNotificationsChannel();
        Notification build = new Notification.Builder(this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setContentTitle(getText(R.string.MigratingFiles)).setAutoCancel(false).setSmallIcon(R.drawable.notification).build();
        isRunning = true;
        new 1().start();
        startForeground(NotificationCenter.storiesReadUpdated, build);
        return super.onStartCommand(intent, i, i2);
    }
}
