package org.telegram.messenger.utils;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photos_photo;
import org.telegram.tgnet.TLRPC$TL_photos_uploadProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_userProfilePhoto;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes.dex */
public class PhotoUtilities {
    public static void applyPhotoToUser(TLRPC$Photo tLRPC$Photo, TLRPC$User tLRPC$User, boolean z) {
        ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$Photo.sizes;
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 100);
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, 1000);
        tLRPC$User.flags |= 32;
        TLRPC$TL_userProfilePhoto tLRPC$TL_userProfilePhoto = new TLRPC$TL_userProfilePhoto();
        tLRPC$User.photo = tLRPC$TL_userProfilePhoto;
        tLRPC$TL_userProfilePhoto.personal = z;
        tLRPC$TL_userProfilePhoto.photo_id = tLRPC$Photo.id;
        ArrayList<TLRPC$VideoSize> arrayList2 = tLRPC$Photo.video_sizes;
        tLRPC$TL_userProfilePhoto.has_video = arrayList2 != null && arrayList2.size() > 0;
        if (closestPhotoSizeWithSize != null) {
            tLRPC$User.photo.photo_small = closestPhotoSizeWithSize.location;
        }
        if (closestPhotoSizeWithSize2 != null) {
            tLRPC$User.photo.photo_big = closestPhotoSizeWithSize2.location;
        }
    }

    public static void setImageAsAvatar(MediaController.PhotoEntry photoEntry, BaseFragment baseFragment, final Runnable runnable) {
        final INavigationLayout parentLayout = baseFragment.getParentLayout();
        final int currentAccount = baseFragment.getCurrentAccount();
        final ImageUpdater imageUpdater = new ImageUpdater(true);
        imageUpdater.parentFragment = baseFragment;
        imageUpdater.processEntry(photoEntry);
        imageUpdater.setDelegate(new ImageUpdater.ImageUpdaterDelegate() { // from class: org.telegram.messenger.utils.PhotoUtilities$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public /* synthetic */ boolean canFinishFragment() {
                return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
            }

            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public /* synthetic */ void didStartUpload(boolean z) {
                ImageUpdater.ImageUpdaterDelegate.-CC.$default$didStartUpload(this, z);
            }

            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public /* synthetic */ void didUploadFailed() {
                ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
            }

            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public final void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2, boolean z) {
                PhotoUtilities.lambda$setImageAsAvatar$4(currentAccount, runnable, parentLayout, imageUpdater, tLRPC$InputFile, tLRPC$InputFile2, d, str, tLRPC$PhotoSize, tLRPC$PhotoSize2, z);
            }

            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public /* synthetic */ String getInitialSearchString() {
                return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getInitialSearchString(this);
            }

            @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
            public /* synthetic */ void onUploadProgressChanged(float f) {
                ImageUpdater.ImageUpdaterDelegate.-CC.$default$onUploadProgressChanged(this, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setImageAsAvatar$4(final int i, final Runnable runnable, final INavigationLayout iNavigationLayout, final ImageUpdater imageUpdater, final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputFile tLRPC$InputFile2, final double d, String str, final TLRPC$PhotoSize tLRPC$PhotoSize, final TLRPC$PhotoSize tLRPC$PhotoSize2, boolean z) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.utils.PhotoUtilities$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PhotoUtilities.lambda$setImageAsAvatar$3(TLRPC$InputFile.this, tLRPC$InputFile2, d, i, tLRPC$PhotoSize2, tLRPC$PhotoSize, runnable, iNavigationLayout, imageUpdater);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setImageAsAvatar$3(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, final int i, final TLRPC$PhotoSize tLRPC$PhotoSize, final TLRPC$PhotoSize tLRPC$PhotoSize2, final Runnable runnable, final INavigationLayout iNavigationLayout, ImageUpdater imageUpdater) {
        TLRPC$TL_photos_uploadProfilePhoto tLRPC$TL_photos_uploadProfilePhoto = new TLRPC$TL_photos_uploadProfilePhoto();
        if (tLRPC$InputFile != null) {
            tLRPC$TL_photos_uploadProfilePhoto.file = tLRPC$InputFile;
            tLRPC$TL_photos_uploadProfilePhoto.flags |= 1;
        }
        if (tLRPC$InputFile2 != null) {
            tLRPC$TL_photos_uploadProfilePhoto.video = tLRPC$InputFile2;
            int i2 = tLRPC$TL_photos_uploadProfilePhoto.flags | 2;
            tLRPC$TL_photos_uploadProfilePhoto.flags = i2;
            tLRPC$TL_photos_uploadProfilePhoto.video_start_ts = d;
            tLRPC$TL_photos_uploadProfilePhoto.flags = i2 | 4;
        }
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_photos_uploadProfilePhoto, new RequestDelegate() { // from class: org.telegram.messenger.utils.PhotoUtilities$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PhotoUtilities.lambda$setImageAsAvatar$2(i, tLRPC$PhotoSize, tLRPC$PhotoSize2, runnable, iNavigationLayout, tLObject, tLRPC$TL_error);
            }
        });
        imageUpdater.onPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setImageAsAvatar$2(final int i, final TLRPC$PhotoSize tLRPC$PhotoSize, final TLRPC$PhotoSize tLRPC$PhotoSize2, final Runnable runnable, final INavigationLayout iNavigationLayout, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.utils.PhotoUtilities$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoUtilities.lambda$setImageAsAvatar$1(TLObject.this, i, tLRPC$PhotoSize, tLRPC$PhotoSize2, runnable, iNavigationLayout);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setImageAsAvatar$1(TLObject tLObject, final int i, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2, Runnable runnable, final INavigationLayout iNavigationLayout) {
        if (tLObject instanceof TLRPC$TL_photos_photo) {
            TLRPC$TL_photos_photo tLRPC$TL_photos_photo = (TLRPC$TL_photos_photo) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_photos_photo.users, false);
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(UserConfig.getInstance(i).clientUserId));
            TLRPC$Photo tLRPC$Photo = tLRPC$TL_photos_photo.photo;
            if (!(tLRPC$Photo instanceof TLRPC$TL_photo) || user == null) {
                return;
            }
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 100);
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_photos_photo.photo.sizes, 1000);
            if (closestPhotoSizeWithSize != null && tLRPC$PhotoSize != null && tLRPC$PhotoSize.location != null) {
                FileLoader.getInstance(i).getPathToAttach(tLRPC$PhotoSize.location, true).renameTo(FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize, true));
                ImageLoader.getInstance().replaceImageInCache(tLRPC$PhotoSize.location.volume_id + "_" + tLRPC$PhotoSize.location.local_id + "@50_50", closestPhotoSizeWithSize.location.volume_id + "_" + closestPhotoSizeWithSize.location.local_id + "@50_50", ImageLocation.getForUser(user, 1), false);
            }
            if (closestPhotoSizeWithSize2 != null && tLRPC$PhotoSize2 != null && tLRPC$PhotoSize2.location != null) {
                FileLoader.getInstance(i).getPathToAttach(tLRPC$PhotoSize2.location, true).renameTo(FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize2, true));
            }
            applyPhotoToUser(tLRPC$TL_photos_photo.photo, user, false);
            UserConfig.getInstance(i).setCurrentUser(user);
            UserConfig.getInstance(i).saveConfig(true);
            if (runnable != null) {
                runnable.run();
            }
            BulletinFactory.of(iNavigationLayout.getLastFragment()).createUsersBulletin(Collections.singletonList(user), AndroidUtilities.replaceTags(LocaleController.getString("ApplyAvatarHint", R.string.ApplyAvatarHintTitle)), AndroidUtilities.replaceSingleTag(LocaleController.getString("ApplyAvatarHint", R.string.ApplyAvatarHint), new Runnable() { // from class: org.telegram.messenger.utils.PhotoUtilities$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoUtilities.lambda$setImageAsAvatar$0(i, iNavigationLayout);
                }
            })).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setImageAsAvatar$0(int i, INavigationLayout iNavigationLayout) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).clientUserId);
        iNavigationLayout.getLastFragment().presentFragment(new ProfileActivity(bundle));
    }

    public static void replacePhotoImagesInCache(int i, TLRPC$Photo tLRPC$Photo, TLRPC$Photo tLRPC$Photo2) {
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 100);
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 1000);
        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, 100);
        TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, 1000);
        if (closestPhotoSizeWithSize3 != null && closestPhotoSizeWithSize != null) {
            FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize, true).renameTo(FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize3, true));
            ImageLoader.getInstance().replaceImageInCache(closestPhotoSizeWithSize.location.volume_id + "_" + closestPhotoSizeWithSize.location.local_id + "@50_50", closestPhotoSizeWithSize3.location.volume_id + "_" + closestPhotoSizeWithSize3.location.local_id + "@50_50", ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), false);
        }
        if (closestPhotoSizeWithSize4 == null || closestPhotoSizeWithSize2 == null) {
            return;
        }
        FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize2, true).renameTo(FileLoader.getInstance(i).getPathToAttach(closestPhotoSizeWithSize4, true));
        ImageLoader.getInstance().replaceImageInCache(closestPhotoSizeWithSize2.location.volume_id + "_" + closestPhotoSizeWithSize2.location.local_id + "@150_150", closestPhotoSizeWithSize4.location.volume_id + "_" + closestPhotoSizeWithSize4.location.local_id + "@150_150", ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tLRPC$Photo), false);
    }

    public static void applyPhotoToUser(TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2, boolean z, TLRPC$User tLRPC$User, boolean z2) {
        tLRPC$User.flags |= 32;
        TLRPC$TL_userProfilePhoto tLRPC$TL_userProfilePhoto = new TLRPC$TL_userProfilePhoto();
        tLRPC$User.photo = tLRPC$TL_userProfilePhoto;
        tLRPC$TL_userProfilePhoto.personal = z2;
        tLRPC$TL_userProfilePhoto.photo_id = 0L;
        tLRPC$TL_userProfilePhoto.has_video = z;
        if (tLRPC$PhotoSize != null) {
            tLRPC$TL_userProfilePhoto.photo_small = tLRPC$PhotoSize.location;
        }
        if (tLRPC$PhotoSize2 != null) {
            tLRPC$TL_userProfilePhoto.photo_big = tLRPC$PhotoSize2.location;
        }
    }
}
