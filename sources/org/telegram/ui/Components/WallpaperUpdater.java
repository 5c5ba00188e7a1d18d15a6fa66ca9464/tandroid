package org.telegram.ui.Components;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.PhotoAlbumPickerActivity;
/* loaded from: classes3.dex */
public class WallpaperUpdater {
    private String currentPicturePath;
    private File currentWallpaperPath;
    private WallpaperUpdaterDelegate delegate;
    private Activity parentActivity;
    private BaseFragment parentFragment;
    private File picturePath = null;

    /* loaded from: classes3.dex */
    public interface WallpaperUpdaterDelegate {
        void didSelectWallpaper(File file, Bitmap bitmap, boolean z);

        void needOpenColorPicker();
    }

    public void cleanup() {
    }

    public WallpaperUpdater(Activity activity, BaseFragment baseFragment, WallpaperUpdaterDelegate wallpaperUpdaterDelegate) {
        this.parentActivity = activity;
        this.parentFragment = baseFragment;
        this.delegate = wallpaperUpdaterDelegate;
    }

    public void showAlert(final boolean z) {
        CharSequence[] charSequenceArr;
        int[] iArr;
        BottomSheet.Builder builder = new BottomSheet.Builder(this.parentActivity);
        builder.setTitle(LocaleController.getString("ChoosePhoto", R.string.ChoosePhoto), true);
        if (z) {
            charSequenceArr = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", R.string.ChooseTakePhoto), LocaleController.getString("SelectFromGallery", R.string.SelectFromGallery), LocaleController.getString("SelectColor", R.string.SelectColor), LocaleController.getString("Default", R.string.Default)};
            iArr = null;
        } else {
            charSequenceArr = new CharSequence[]{LocaleController.getString("ChooseTakePhoto", R.string.ChooseTakePhoto), LocaleController.getString("SelectFromGallery", R.string.SelectFromGallery)};
            iArr = new int[]{R.drawable.msg_camera, R.drawable.msg_photos};
        }
        builder.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.WallpaperUpdater$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                WallpaperUpdater.this.lambda$showAlert$0(z, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlert$0(boolean z, DialogInterface dialogInterface, int i) {
        try {
            if (i != 0) {
                if (i == 1) {
                    openGallery();
                    return;
                } else if (z) {
                    if (i == 2) {
                        this.delegate.needOpenColorPicker();
                        return;
                    } else if (i == 3) {
                        this.delegate.didSelectWallpaper(null, null, false);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File generatePicturePath = AndroidUtilities.generatePicturePath();
                if (generatePicturePath != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        Activity activity = this.parentActivity;
                        intent.putExtra("output", FileProvider.getUriForFile(activity, ApplicationLoader.getApplicationId() + ".provider", generatePicturePath));
                        intent.addFlags(2);
                        intent.addFlags(1);
                    } else {
                        intent.putExtra("output", Uri.fromFile(generatePicturePath));
                    }
                    this.currentPicturePath = generatePicturePath.getAbsolutePath();
                }
                this.parentActivity.startActivityForResult(intent, 10);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public void openGallery() {
        int checkSelfPermission;
        int checkSelfPermission2;
        BaseFragment baseFragment = this.parentFragment;
        if (baseFragment != null) {
            Activity parentActivity = baseFragment.getParentActivity();
            if (parentActivity != null) {
                int i = Build.VERSION.SDK_INT;
                if (i >= 33) {
                    checkSelfPermission2 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_IMAGES");
                    if (checkSelfPermission2 != 0) {
                        parentActivity.requestPermissions(new String[]{"android.permission.READ_MEDIA_IMAGES"}, 4);
                        return;
                    }
                } else if (i >= 23) {
                    checkSelfPermission = parentActivity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
                    if (checkSelfPermission != 0) {
                        parentActivity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
                        return;
                    }
                }
            }
            PhotoAlbumPickerActivity photoAlbumPickerActivity = new PhotoAlbumPickerActivity(PhotoAlbumPickerActivity.SELECT_TYPE_WALLPAPER, false, false, null);
            photoAlbumPickerActivity.setAllowSearchImages(false);
            photoAlbumPickerActivity.setDelegate(new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() { // from class: org.telegram.ui.Components.WallpaperUpdater.1
                @Override // org.telegram.ui.PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate
                public void didSelectPhotos(ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList, boolean z, int i2) {
                    WallpaperUpdater.this.didSelectPhotos(arrayList);
                }

                @Override // org.telegram.ui.PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate
                public void startPhotoSelectActivity() {
                    try {
                        Intent intent = new Intent("android.intent.action.PICK");
                        intent.setType("image/*");
                        WallpaperUpdater.this.parentActivity.startActivityForResult(intent, 11);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            });
            this.parentFragment.presentFragment(photoAlbumPickerActivity);
            return;
        }
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        this.parentActivity.startActivityForResult(intent, 11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didSelectPhotos(ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList) {
        try {
            if (arrayList.isEmpty()) {
                return;
            }
            SendMessagesHelper.SendingMediaInfo sendingMediaInfo = arrayList.get(0);
            if (sendingMediaInfo.path != null) {
                File directory = FileLoader.getDirectory(4);
                this.currentWallpaperPath = new File(directory, Utilities.random.nextInt() + ".jpg");
                android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
                Bitmap loadBitmap = ImageLoader.loadBitmap(sendingMediaInfo.path, null, (float) realScreenSize.x, (float) realScreenSize.y, true);
                loadBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(this.currentWallpaperPath));
                this.delegate.didSelectWallpaper(this.currentWallpaperPath, loadBitmap, true);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public String getCurrentPicturePath() {
        return this.currentPicturePath;
    }

    public void setCurrentPicturePath(String str) {
        this.currentPicturePath = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0068, code lost:
        if (r9 == null) goto L12;
     */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onActivityResult(int i, int i2, Intent intent) {
        FileOutputStream fileOutputStream;
        if (i2 == -1) {
            FileOutputStream fileOutputStream2 = null;
            if (i == 10) {
                AndroidUtilities.addMediaToGallery(this.currentPicturePath);
                try {
                    this.currentWallpaperPath = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                    android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
                    Bitmap loadBitmap = ImageLoader.loadBitmap(this.currentPicturePath, null, (float) realScreenSize.x, (float) realScreenSize.y, true);
                    fileOutputStream = new FileOutputStream(this.currentWallpaperPath);
                    try {
                        loadBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                        this.delegate.didSelectWallpaper(this.currentWallpaperPath, loadBitmap, false);
                    } catch (Exception e) {
                        e = e;
                        try {
                            FileLog.e(e);
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream2 = fileOutputStream;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    fileOutputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
                try {
                    fileOutputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
                this.currentPicturePath = null;
            } else if (i != 11 || intent == null || intent.getData() == null) {
            } else {
                try {
                    this.currentWallpaperPath = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                    android.graphics.Point realScreenSize2 = AndroidUtilities.getRealScreenSize();
                    Bitmap loadBitmap2 = ImageLoader.loadBitmap(null, intent.getData(), (float) realScreenSize2.x, (float) realScreenSize2.y, true);
                    loadBitmap2.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(this.currentWallpaperPath));
                    this.delegate.didSelectWallpaper(this.currentWallpaperPath, loadBitmap2, false);
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
        }
    }
}
