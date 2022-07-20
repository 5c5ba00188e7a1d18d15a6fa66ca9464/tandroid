package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Components.CircularViewPager;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public class ProfileGalleryView extends CircularViewPager implements NotificationCenter.NotificationCenterDelegate {
    private ViewPagerAdapter adapter;
    private final Callback callback;
    private TLRPC$ChatFull chatInfo;
    private boolean createThumbFromParent;
    ImageLocation curreantUploadingThumbLocation;
    private int currentAccount;
    ImageLocation currentUploadingImageLocation;
    private long dialogId;
    private final PointF downPoint;
    private boolean forceResetPosition;
    private boolean hasActiveVideo;
    private int imagesLayerNum;
    private ArrayList<ImageLocation> imagesLocations;
    private ArrayList<Integer> imagesLocationsSizes;
    private ArrayList<Float> imagesUploadProgress;
    private boolean invalidateWithParent;
    private boolean isDownReleased;
    private final boolean isProfileFragment;
    private boolean isScrollingListView;
    private boolean isSwipingViewPager;
    private final ActionBar parentActionBar;
    private final int parentClassGuid;
    private final RecyclerListView parentListView;
    Path path;
    private ArrayList<TLRPC$Photo> photos;
    PinchToZoomHelper pinchToZoomHelper;
    private ImageLocation prevImageLocation;
    private final SparseArray<RadialProgress2> radialProgresses;
    float[] radii;
    RectF rect;
    private int roundBottomRadius;
    private int roundTopRadius;
    private boolean scrolledByUser;
    private int settingMainPhoto;
    private ArrayList<String> thumbsFileNames;
    private ArrayList<ImageLocation> thumbsLocations;
    private final int touchSlop;
    private ImageLocation uploadingImageLocation;
    private ArrayList<String> videoFileNames;
    private ArrayList<ImageLocation> videoLocations;

    /* loaded from: classes3.dex */
    public interface Callback {
        void onDown(boolean z);

        void onPhotosLoaded();

        void onRelease();

        void onVideoSet();
    }

    public void setHasActiveVideo(boolean z) {
        this.hasActiveVideo = z;
    }

    public View findVideoActiveView() {
        if (!this.hasActiveVideo) {
            return null;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof TextureStubView) {
                return childAt;
            }
        }
        return null;
    }

    /* loaded from: classes3.dex */
    public static class Item {
        private AvatarImageView imageView;
        boolean isActiveVideo;
        private View textureViewStubView;

        private Item() {
        }

        /* synthetic */ Item(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public ProfileGalleryView(Context context, ActionBar actionBar, RecyclerListView recyclerListView, Callback callback) {
        super(context);
        this.downPoint = new PointF();
        this.isScrollingListView = true;
        this.isSwipingViewPager = true;
        this.currentAccount = UserConfig.selectedAccount;
        this.path = new Path();
        this.rect = new RectF();
        this.radii = new float[8];
        this.videoFileNames = new ArrayList<>();
        this.thumbsFileNames = new ArrayList<>();
        this.photos = new ArrayList<>();
        this.videoLocations = new ArrayList<>();
        this.imagesLocations = new ArrayList<>();
        this.thumbsLocations = new ArrayList<>();
        this.imagesLocationsSizes = new ArrayList<>();
        this.imagesUploadProgress = new ArrayList<>();
        this.radialProgresses = new SparseArray<>();
        this.createThumbFromParent = true;
        setOffscreenPageLimit(2);
        this.isProfileFragment = false;
        this.parentListView = recyclerListView;
        this.parentClassGuid = ConnectionsManager.generateClassGuid();
        this.parentActionBar = actionBar;
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.callback = callback;
        addOnPageChangeListener(new AnonymousClass1());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), null, actionBar);
        this.adapter = viewPagerAdapter;
        setAdapter((CircularViewPager.Adapter) viewPagerAdapter);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogPhotosLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.reloadDialogPhotos);
    }

    /* renamed from: org.telegram.ui.Components.ProfileGalleryView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements ViewPager.OnPageChangeListener {
        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
        }

        AnonymousClass1() {
            ProfileGalleryView.this = r1;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            ImageLocation imageLocation;
            if (i2 == 0) {
                int realPosition = ProfileGalleryView.this.adapter.getRealPosition(i);
                if (ProfileGalleryView.this.hasActiveVideo) {
                    realPosition--;
                }
                ProfileGalleryView.this.getCurrentItemView();
                int childCount = ProfileGalleryView.this.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt = ProfileGalleryView.this.getChildAt(i3);
                    if (childAt instanceof BackupImageView) {
                        int realPosition2 = ProfileGalleryView.this.adapter.getRealPosition(ProfileGalleryView.this.adapter.imageViews.indexOf(childAt));
                        if (ProfileGalleryView.this.hasActiveVideo) {
                            realPosition2--;
                        }
                        ImageReceiver imageReceiver = ((BackupImageView) childAt).getImageReceiver();
                        boolean allowStartAnimation = imageReceiver.getAllowStartAnimation();
                        if (realPosition2 == realPosition) {
                            if (!allowStartAnimation) {
                                imageReceiver.setAllowStartAnimation(true);
                                imageReceiver.startAnimation();
                            }
                            ImageLocation imageLocation2 = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition2);
                            if (imageLocation2 != null) {
                                FileLoader.getInstance(ProfileGalleryView.this.currentAccount).setForceStreamLoadingFile(imageLocation2.location, "mp4");
                            }
                        } else if (allowStartAnimation) {
                            AnimatedFileDrawable animation = imageReceiver.getAnimation();
                            if (animation != null && (imageLocation = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition2)) != null) {
                                animation.seekTo(imageLocation.videoSeekTo, false, true);
                            }
                            imageReceiver.setAllowStartAnimation(false);
                            imageReceiver.stopAnimation();
                        }
                    }
                }
            }
        }
    }

    public void setImagesLayerNum(int i) {
        this.imagesLayerNum = i;
    }

    public ProfileGalleryView(Context context, long j, ActionBar actionBar, RecyclerListView recyclerListView, ProfileActivity.AvatarImageView avatarImageView, int i, Callback callback) {
        super(context);
        this.downPoint = new PointF();
        this.isScrollingListView = true;
        this.isSwipingViewPager = true;
        this.currentAccount = UserConfig.selectedAccount;
        this.path = new Path();
        this.rect = new RectF();
        this.radii = new float[8];
        this.videoFileNames = new ArrayList<>();
        this.thumbsFileNames = new ArrayList<>();
        this.photos = new ArrayList<>();
        this.videoLocations = new ArrayList<>();
        this.imagesLocations = new ArrayList<>();
        this.thumbsLocations = new ArrayList<>();
        this.imagesLocationsSizes = new ArrayList<>();
        this.imagesUploadProgress = new ArrayList<>();
        this.radialProgresses = new SparseArray<>();
        this.createThumbFromParent = true;
        setVisibility(8);
        setOverScrollMode(2);
        setOffscreenPageLimit(2);
        this.isProfileFragment = true;
        this.dialogId = j;
        this.parentListView = recyclerListView;
        this.parentClassGuid = i;
        this.parentActionBar = actionBar;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), avatarImageView, actionBar);
        this.adapter = viewPagerAdapter;
        setAdapter((CircularViewPager.Adapter) viewPagerAdapter);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.callback = callback;
        addOnPageChangeListener(new AnonymousClass2());
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogPhotosLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.reloadDialogPhotos);
        MessagesController.getInstance(this.currentAccount).loadDialogPhotos(j, 80, 0, true, i);
    }

    /* renamed from: org.telegram.ui.Components.ProfileGalleryView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements ViewPager.OnPageChangeListener {
        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
        }

        AnonymousClass2() {
            ProfileGalleryView.this = r1;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            ImageLocation imageLocation;
            if (i2 == 0) {
                int realPosition = ProfileGalleryView.this.adapter.getRealPosition(i);
                ProfileGalleryView.this.getCurrentItemView();
                int childCount = ProfileGalleryView.this.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    View childAt = ProfileGalleryView.this.getChildAt(i3);
                    if (childAt instanceof BackupImageView) {
                        int realPosition2 = ProfileGalleryView.this.adapter.getRealPosition(ProfileGalleryView.this.adapter.imageViews.indexOf(childAt));
                        ImageReceiver imageReceiver = ((BackupImageView) childAt).getImageReceiver();
                        boolean allowStartAnimation = imageReceiver.getAllowStartAnimation();
                        if (realPosition2 == realPosition) {
                            if (!allowStartAnimation) {
                                imageReceiver.setAllowStartAnimation(true);
                                imageReceiver.startAnimation();
                            }
                            ImageLocation imageLocation2 = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition2);
                            if (imageLocation2 != null) {
                                FileLoader.getInstance(ProfileGalleryView.this.currentAccount).setForceStreamLoadingFile(imageLocation2.location, "mp4");
                            }
                        } else if (allowStartAnimation) {
                            AnimatedFileDrawable animation = imageReceiver.getAnimation();
                            if (animation != null && (imageLocation = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition2)) != null) {
                                animation.seekTo(imageLocation.videoSeekTo, false, true);
                            }
                            imageReceiver.setAllowStartAnimation(false);
                            imageReceiver.stopAnimation();
                        }
                    }
                }
            }
        }
    }

    public void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogPhotosLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.reloadDialogPhotos);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof BackupImageView) {
                BackupImageView backupImageView = (BackupImageView) childAt;
                if (backupImageView.getImageReceiver().hasStaticThumb()) {
                    Drawable drawable = backupImageView.getImageReceiver().getDrawable();
                    if (drawable instanceof AnimatedFileDrawable) {
                        ((AnimatedFileDrawable) drawable).removeSecondParentView(backupImageView);
                    }
                }
            }
        }
    }

    public void setAnimatedFileMaybe(AnimatedFileDrawable animatedFileDrawable) {
        if (animatedFileDrawable == null || this.adapter == null) {
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof BackupImageView) {
                ViewPagerAdapter viewPagerAdapter = this.adapter;
                if (viewPagerAdapter.getRealPosition(viewPagerAdapter.imageViews.indexOf(childAt)) == 0) {
                    BackupImageView backupImageView = (BackupImageView) childAt;
                    AnimatedFileDrawable animation = backupImageView.getImageReceiver().getAnimation();
                    if (animation != animatedFileDrawable) {
                        if (animation != null) {
                            animation.removeSecondParentView(backupImageView);
                        }
                        backupImageView.setImageDrawable(animatedFileDrawable);
                        animatedFileDrawable.addSecondParentView(this);
                        animatedFileDrawable.setInvalidateParentViewWithSecond(true);
                    }
                }
            }
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        if (this.adapter == null) {
            return false;
        }
        if (this.parentListView.getScrollState() != 0 && !this.isScrollingListView && this.isSwipingViewPager) {
            this.isSwipingViewPager = false;
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(3);
            super.onTouchEvent(obtain);
            obtain.recycle();
            return false;
        }
        int action = motionEvent.getAction();
        if (this.pinchToZoomHelper != null && getCurrentItemView() != null) {
            if (action != 0 && this.isDownReleased && !this.pinchToZoomHelper.isInOverlayMode()) {
                this.pinchToZoomHelper.checkPinchToZoom(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0), this, getCurrentItemView().getImageReceiver(), null);
            } else if (this.pinchToZoomHelper.checkPinchToZoom(motionEvent, this, getCurrentItemView().getImageReceiver(), null)) {
                if (!this.isDownReleased) {
                    this.isDownReleased = true;
                    this.callback.onRelease();
                }
                return true;
            }
        }
        if (action == 0) {
            this.isScrollingListView = true;
            this.isSwipingViewPager = true;
            this.scrolledByUser = true;
            this.downPoint.set(motionEvent.getX(), motionEvent.getY());
            if (this.adapter.getCount() > 1) {
                this.callback.onDown(motionEvent.getX() < ((float) getWidth()) / 3.0f);
            }
            this.isDownReleased = false;
        } else if (action == 1) {
            if (!this.isDownReleased) {
                int count = this.adapter.getCount();
                int currentItem = getCurrentItem();
                if (count > 1) {
                    if (motionEvent.getX() > getWidth() / 3.0f) {
                        i = this.adapter.getExtraCount();
                        int i2 = currentItem + 1;
                        if (i2 < count - i) {
                            i = i2;
                        }
                    } else {
                        int extraCount = this.adapter.getExtraCount();
                        int i3 = (-1) + currentItem;
                        i = i3 < extraCount ? (count - extraCount) - 1 : i3;
                    }
                    this.callback.onRelease();
                    setCurrentItem(i, false);
                }
            }
        } else if (action == 2) {
            float x = motionEvent.getX() - this.downPoint.x;
            float y = motionEvent.getY() - this.downPoint.y;
            boolean z = Math.abs(y) >= ((float) this.touchSlop) || Math.abs(x) >= ((float) this.touchSlop);
            if (z) {
                this.isDownReleased = true;
                this.callback.onRelease();
            }
            boolean z2 = this.isSwipingViewPager;
            if (!z2 || !this.isScrollingListView) {
                if (z2 && !canScrollHorizontally(-1) && x > this.touchSlop) {
                    return false;
                }
            } else if (z) {
                if (Math.abs(y) > Math.abs(x)) {
                    this.isSwipingViewPager = false;
                    MotionEvent obtain2 = MotionEvent.obtain(motionEvent);
                    obtain2.setAction(3);
                    super.onTouchEvent(obtain2);
                    obtain2.recycle();
                } else {
                    this.isScrollingListView = false;
                    MotionEvent obtain3 = MotionEvent.obtain(motionEvent);
                    obtain3.setAction(3);
                    this.parentListView.onTouchEvent(obtain3);
                    obtain3.recycle();
                }
            }
        }
        boolean onTouchEvent = this.isScrollingListView ? this.parentListView.onTouchEvent(motionEvent) : false;
        if (this.isSwipingViewPager) {
            onTouchEvent |= super.onTouchEvent(motionEvent);
        }
        if (action == 1 || action == 3) {
            this.isScrollingListView = false;
            this.isSwipingViewPager = false;
        }
        return onTouchEvent;
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.chatInfo = tLRPC$ChatFull;
        if (this.photos.isEmpty() || this.photos.get(0) != null || this.chatInfo == null || !FileLoader.isSamePhoto((TLRPC$FileLocation) this.imagesLocations.get(0).location, this.chatInfo.chat_photo)) {
            return;
        }
        this.photos.set(0, this.chatInfo.chat_photo);
        if (!this.chatInfo.chat_photo.video_sizes.isEmpty()) {
            TLRPC$VideoSize tLRPC$VideoSize = this.chatInfo.chat_photo.video_sizes.get(0);
            this.videoLocations.set(0, ImageLocation.getForPhoto(tLRPC$VideoSize, this.chatInfo.chat_photo));
            this.videoFileNames.set(0, FileLoader.getAttachFileName(tLRPC$VideoSize));
            this.callback.onPhotosLoaded();
        } else {
            this.videoLocations.set(0, null);
            this.videoFileNames.add(0, null);
        }
        this.imagesUploadProgress.set(0, null);
        this.adapter.notifyDataSetChanged();
    }

    public boolean initIfEmpty(ImageLocation imageLocation, ImageLocation imageLocation2, boolean z) {
        if (imageLocation == null || imageLocation2 == null || this.settingMainPhoto != 0) {
            return false;
        }
        ImageLocation imageLocation3 = this.prevImageLocation;
        if (imageLocation3 == null || imageLocation3.location.local_id != imageLocation.location.local_id) {
            if (!this.imagesLocations.isEmpty()) {
                this.prevImageLocation = imageLocation;
                if (z) {
                    MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.dialogId, 80, 0, true, this.parentClassGuid);
                }
                return true;
            } else if (z) {
                MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.dialogId, 80, 0, true, this.parentClassGuid);
            }
        }
        if (!this.imagesLocations.isEmpty()) {
            return false;
        }
        this.prevImageLocation = imageLocation;
        this.thumbsFileNames.add(null);
        this.videoFileNames.add(null);
        this.imagesLocations.add(imageLocation);
        this.thumbsLocations.add(imageLocation2);
        this.videoLocations.add(null);
        this.photos.add(null);
        this.imagesLocationsSizes.add(-1);
        this.imagesUploadProgress.add(null);
        getAdapter().notifyDataSetChanged();
        return true;
    }

    public void addUploadingImage(ImageLocation imageLocation, ImageLocation imageLocation2) {
        this.prevImageLocation = imageLocation;
        this.thumbsFileNames.add(0, null);
        this.videoFileNames.add(0, null);
        this.imagesLocations.add(0, imageLocation);
        this.thumbsLocations.add(0, imageLocation2);
        this.videoLocations.add(0, null);
        this.photos.add(0, null);
        this.imagesLocationsSizes.add(0, -1);
        this.imagesUploadProgress.add(0, Float.valueOf(0.0f));
        this.adapter.notifyDataSetChanged();
        resetCurrentItem();
        this.currentUploadingImageLocation = imageLocation;
        this.curreantUploadingThumbLocation = imageLocation2;
    }

    public void removeUploadingImage(ImageLocation imageLocation) {
        this.uploadingImageLocation = imageLocation;
        this.currentUploadingImageLocation = null;
        this.curreantUploadingThumbLocation = null;
    }

    public ImageLocation getImageLocation(int i) {
        if (i < 0 || i >= this.imagesLocations.size()) {
            return null;
        }
        ImageLocation imageLocation = this.videoLocations.get(i);
        return imageLocation != null ? imageLocation : this.imagesLocations.get(i);
    }

    public ImageLocation getRealImageLocation(int i) {
        if (i < 0 || i >= this.imagesLocations.size()) {
            return null;
        }
        return this.imagesLocations.get(i);
    }

    public boolean hasImages() {
        return !this.imagesLocations.isEmpty();
    }

    public BackupImageView getCurrentItemView() {
        ViewPagerAdapter viewPagerAdapter = this.adapter;
        if (viewPagerAdapter == null || viewPagerAdapter.objects.isEmpty()) {
            return null;
        }
        return ((Item) this.adapter.objects.get(getCurrentItem())).imageView;
    }

    public boolean isLoadingCurrentVideo() {
        BackupImageView currentItemView;
        if (this.videoLocations.get(this.hasActiveVideo ? getRealPosition() - 1 : getRealPosition()) == null || (currentItemView = getCurrentItemView()) == null) {
            return false;
        }
        AnimatedFileDrawable animation = currentItemView.getImageReceiver().getAnimation();
        return animation == null || !animation.hasBitmap();
    }

    public float getCurrentItemProgress() {
        AnimatedFileDrawable animation;
        BackupImageView currentItemView = getCurrentItemView();
        if (currentItemView == null || (animation = currentItemView.getImageReceiver().getAnimation()) == null) {
            return 0.0f;
        }
        return animation.getCurrentProgress();
    }

    public boolean isCurrentItemVideo() {
        int realPosition = getRealPosition();
        if (this.hasActiveVideo) {
            if (realPosition == 0) {
                return false;
            }
            realPosition--;
        }
        return this.videoLocations.get(realPosition) != null;
    }

    public ImageLocation getCurrentVideoLocation(ImageLocation imageLocation, ImageLocation imageLocation2) {
        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated;
        if (imageLocation == null) {
            return null;
        }
        int i = 0;
        while (i < 2) {
            ArrayList<ImageLocation> arrayList = i == 0 ? this.thumbsLocations : this.imagesLocations;
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ImageLocation imageLocation3 = arrayList.get(i2);
                if (imageLocation3 != null && (tLRPC$TL_fileLocationToBeDeprecated = imageLocation3.location) != null) {
                    int i3 = imageLocation3.dc_id;
                    if (i3 == imageLocation.dc_id) {
                        int i4 = tLRPC$TL_fileLocationToBeDeprecated.local_id;
                        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated2 = imageLocation.location;
                        if (i4 == tLRPC$TL_fileLocationToBeDeprecated2.local_id && tLRPC$TL_fileLocationToBeDeprecated.volume_id == tLRPC$TL_fileLocationToBeDeprecated2.volume_id) {
                            return this.videoLocations.get(i2);
                        }
                    }
                    if (i3 == imageLocation2.dc_id) {
                        int i5 = tLRPC$TL_fileLocationToBeDeprecated.local_id;
                        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated3 = imageLocation2.location;
                        if (i5 == tLRPC$TL_fileLocationToBeDeprecated3.local_id && tLRPC$TL_fileLocationToBeDeprecated.volume_id == tLRPC$TL_fileLocationToBeDeprecated3.volume_id) {
                            return this.videoLocations.get(i2);
                        }
                    } else {
                        continue;
                    }
                }
            }
            i++;
        }
        return null;
    }

    public void resetCurrentItem() {
        setCurrentItem(this.adapter.getExtraCount(), false);
    }

    public int getRealCount() {
        int size = this.photos.size();
        return this.hasActiveVideo ? size + 1 : size;
    }

    public int getRealPosition(int i) {
        return this.adapter.getRealPosition(i);
    }

    public int getRealPosition() {
        return this.adapter.getRealPosition(getCurrentItem());
    }

    public TLRPC$Photo getPhoto(int i) {
        if (i < 0 || i >= this.photos.size()) {
            return null;
        }
        return this.photos.get(i);
    }

    public void replaceFirstPhoto(TLRPC$Photo tLRPC$Photo, TLRPC$Photo tLRPC$Photo2) {
        int indexOf;
        if (!this.photos.isEmpty() && (indexOf = this.photos.indexOf(tLRPC$Photo)) >= 0) {
            this.photos.set(indexOf, tLRPC$Photo2);
        }
    }

    public void finishSettingMainPhoto() {
        this.settingMainPhoto--;
    }

    public void startMovePhotoToBegin(int i) {
        if (i <= 0 || i >= this.photos.size()) {
            return;
        }
        this.settingMainPhoto++;
        this.photos.remove(i);
        this.photos.add(0, this.photos.get(i));
        this.thumbsFileNames.remove(i);
        this.thumbsFileNames.add(0, this.thumbsFileNames.get(i));
        ArrayList<String> arrayList = this.videoFileNames;
        arrayList.add(0, arrayList.remove(i));
        this.videoLocations.remove(i);
        this.videoLocations.add(0, this.videoLocations.get(i));
        this.imagesLocations.remove(i);
        this.imagesLocations.add(0, this.imagesLocations.get(i));
        this.thumbsLocations.remove(i);
        this.thumbsLocations.add(0, this.thumbsLocations.get(i));
        this.imagesLocationsSizes.remove(i);
        this.imagesLocationsSizes.add(0, this.imagesLocationsSizes.get(i));
        this.imagesUploadProgress.remove(i);
        this.imagesUploadProgress.add(0, this.imagesUploadProgress.get(i));
        this.prevImageLocation = this.imagesLocations.get(0);
    }

    public void commitMoveToBegin() {
        this.adapter.notifyDataSetChanged();
        resetCurrentItem();
    }

    public boolean removePhotoAtIndex(int i) {
        if (i < 0 || i >= this.photos.size()) {
            return false;
        }
        this.photos.remove(i);
        this.thumbsFileNames.remove(i);
        this.videoFileNames.remove(i);
        this.videoLocations.remove(i);
        this.imagesLocations.remove(i);
        this.thumbsLocations.remove(i);
        this.imagesLocationsSizes.remove(i);
        this.radialProgresses.delete(i);
        this.imagesUploadProgress.remove(i);
        if (i == 0 && !this.imagesLocations.isEmpty()) {
            this.prevImageLocation = this.imagesLocations.get(0);
        }
        this.adapter.notifyDataSetChanged();
        return this.photos.isEmpty();
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.parentListView.getScrollState() != 0) {
            return false;
        }
        if (getParent() != null && getParent().getParent() != null) {
            getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    private void loadNeighboringThumbs() {
        int size = this.thumbsLocations.size();
        if (size > 1) {
            int i = 0;
            while (true) {
                int i2 = 2;
                if (size <= 2) {
                    i2 = 1;
                }
                if (i >= i2) {
                    return;
                }
                FileLoader.getInstance(this.currentAccount).loadFile(this.thumbsLocations.get(i == 0 ? 1 : size - 1), null, null, 0, 1);
                i++;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0187, code lost:
        if (r4 != false) goto L72;
     */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        RadialProgress2 radialProgress2;
        RadialProgress2 radialProgress22;
        ImageLocation imageLocation;
        ArrayList<TLRPC$PhotoSize> arrayList;
        Float f;
        boolean z;
        if (i == NotificationCenter.dialogPhotosLoaded) {
            int intValue = ((Integer) objArr[3]).intValue();
            long longValue = ((Long) objArr[0]).longValue();
            if (longValue != this.dialogId || this.parentClassGuid != intValue || this.adapter == null) {
                return;
            }
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            ArrayList arrayList2 = (ArrayList) objArr[4];
            this.thumbsFileNames.clear();
            this.videoFileNames.clear();
            this.imagesLocations.clear();
            this.videoLocations.clear();
            this.thumbsLocations.clear();
            this.photos.clear();
            this.imagesLocationsSizes.clear();
            this.imagesUploadProgress.clear();
            if (DialogObject.isChatDialog(longValue)) {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                imageLocation = ImageLocation.getForUserOrChat(chat, 0);
                if (imageLocation != null) {
                    this.imagesLocations.add(imageLocation);
                    this.thumbsLocations.add(ImageLocation.getForUserOrChat(chat, 1));
                    this.thumbsFileNames.add(null);
                    TLRPC$ChatFull tLRPC$ChatFull = this.chatInfo;
                    if (tLRPC$ChatFull != null && FileLoader.isSamePhoto((TLRPC$FileLocation) imageLocation.location, tLRPC$ChatFull.chat_photo)) {
                        this.photos.add(this.chatInfo.chat_photo);
                        if (!this.chatInfo.chat_photo.video_sizes.isEmpty()) {
                            TLRPC$VideoSize tLRPC$VideoSize = this.chatInfo.chat_photo.video_sizes.get(0);
                            this.videoLocations.add(ImageLocation.getForPhoto(tLRPC$VideoSize, this.chatInfo.chat_photo));
                            this.videoFileNames.add(FileLoader.getAttachFileName(tLRPC$VideoSize));
                        } else {
                            this.videoLocations.add(null);
                            this.videoFileNames.add(null);
                        }
                    } else {
                        this.photos.add(null);
                        this.videoFileNames.add(null);
                        this.videoLocations.add(null);
                    }
                    this.imagesLocationsSizes.add(-1);
                    this.imagesUploadProgress.add(null);
                }
            } else {
                imageLocation = null;
            }
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                TLRPC$Photo tLRPC$Photo = (TLRPC$Photo) arrayList2.get(i3);
                if (tLRPC$Photo != null && !(tLRPC$Photo instanceof TLRPC$TL_photoEmpty) && (arrayList = tLRPC$Photo.sizes) != null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 50);
                    int size = tLRPC$Photo.sizes.size();
                    int i4 = 0;
                    while (true) {
                        if (i4 >= size) {
                            break;
                        }
                        TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Photo.sizes.get(i4);
                        if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                            closestPhotoSizeWithSize = tLRPC$PhotoSize;
                            break;
                        }
                        i4++;
                    }
                    if (imageLocation != null) {
                        int size2 = tLRPC$Photo.sizes.size();
                        int i5 = 0;
                        while (true) {
                            if (i5 >= size2) {
                                z = false;
                                break;
                            }
                            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$Photo.sizes.get(i5).location;
                            if (tLRPC$FileLocation != null) {
                                int i6 = tLRPC$FileLocation.local_id;
                                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
                                if (i6 == tLRPC$TL_fileLocationToBeDeprecated.local_id && tLRPC$FileLocation.volume_id == tLRPC$TL_fileLocationToBeDeprecated.volume_id) {
                                    this.photos.set(0, tLRPC$Photo);
                                    if (!tLRPC$Photo.video_sizes.isEmpty()) {
                                        this.videoLocations.set(0, ImageLocation.getForPhoto(tLRPC$Photo.video_sizes.get(0), tLRPC$Photo));
                                    }
                                    z = true;
                                }
                            }
                            i5++;
                        }
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 640);
                    if (closestPhotoSizeWithSize2 != null) {
                        int i7 = tLRPC$Photo.dc_id;
                        if (i7 != 0) {
                            TLRPC$FileLocation tLRPC$FileLocation2 = closestPhotoSizeWithSize2.location;
                            tLRPC$FileLocation2.dc_id = i7;
                            tLRPC$FileLocation2.file_reference = tLRPC$Photo.file_reference;
                        }
                        ImageLocation forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tLRPC$Photo);
                        if (forPhoto != null) {
                            this.imagesLocations.add(forPhoto);
                            this.thumbsFileNames.add(FileLoader.getAttachFileName(closestPhotoSizeWithSize instanceof TLRPC$TL_photoStrippedSize ? closestPhotoSizeWithSize2 : closestPhotoSizeWithSize));
                            this.thumbsLocations.add(ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo));
                            if (!tLRPC$Photo.video_sizes.isEmpty()) {
                                TLRPC$VideoSize tLRPC$VideoSize2 = tLRPC$Photo.video_sizes.get(0);
                                this.videoLocations.add(ImageLocation.getForPhoto(tLRPC$VideoSize2, tLRPC$Photo));
                                this.videoFileNames.add(FileLoader.getAttachFileName(tLRPC$VideoSize2));
                                f = null;
                            } else {
                                f = null;
                                this.videoLocations.add(null);
                                this.videoFileNames.add(null);
                            }
                            this.photos.add(tLRPC$Photo);
                            this.imagesLocationsSizes.add(Integer.valueOf(closestPhotoSizeWithSize2.size));
                            this.imagesUploadProgress.add(f);
                        }
                    }
                }
            }
            loadNeighboringThumbs();
            getAdapter().notifyDataSetChanged();
            if (this.isProfileFragment) {
                if (!this.scrolledByUser || this.forceResetPosition) {
                    resetCurrentItem();
                }
            } else if (!this.scrolledByUser || this.forceResetPosition) {
                resetCurrentItem();
                getAdapter().notifyDataSetChanged();
            }
            this.forceResetPosition = false;
            if (booleanValue) {
                MessagesController.getInstance(this.currentAccount).loadDialogPhotos(longValue, 80, 0, false, this.parentClassGuid);
            }
            Callback callback = this.callback;
            if (callback != null) {
                callback.onPhotosLoaded();
            }
            ImageLocation imageLocation2 = this.currentUploadingImageLocation;
            if (imageLocation2 == null) {
                return;
            }
            addUploadingImage(imageLocation2, this.curreantUploadingThumbLocation);
        } else if (i == NotificationCenter.fileLoaded) {
            String str = (String) objArr[0];
            for (int i8 = 0; i8 < this.thumbsFileNames.size(); i8++) {
                String str2 = this.videoFileNames.get(i8);
                if (str2 == null) {
                    str2 = this.thumbsFileNames.get(i8);
                }
                if (str2 != null && TextUtils.equals(str, str2) && (radialProgress22 = this.radialProgresses.get(i8)) != null) {
                    radialProgress22.setProgress(1.0f, true);
                }
            }
        } else if (i == NotificationCenter.fileLoadProgressChanged) {
            String str3 = (String) objArr[0];
            for (int i9 = 0; i9 < this.thumbsFileNames.size(); i9++) {
                String str4 = this.videoFileNames.get(i9);
                if (str4 == null) {
                    str4 = this.thumbsFileNames.get(i9);
                }
                if (str4 != null && TextUtils.equals(str3, str4) && (radialProgress2 = this.radialProgresses.get(i9)) != null) {
                    radialProgress2.setProgress(Math.min(1.0f, ((float) ((Long) objArr[1]).longValue()) / ((float) ((Long) objArr[2]).longValue())), true);
                }
            }
        } else if (i == NotificationCenter.reloadDialogPhotos && this.settingMainPhoto == 0) {
            MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.dialogId, 80, 0, true, this.parentClassGuid);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewPagerAdapter extends CircularViewPager.Adapter {
        private final Context context;
        private BackupImageView parentAvatarImageView;
        private final Paint placeholderPaint;
        private final ArrayList<Item> objects = new ArrayList<>();
        private final ArrayList<BackupImageView> imageViews = new ArrayList<>();

        public ViewPagerAdapter(Context context, ProfileActivity.AvatarImageView avatarImageView, ActionBar actionBar) {
            ProfileGalleryView.this = r1;
            this.context = context;
            this.parentAvatarImageView = avatarImageView;
            Paint paint = new Paint(1);
            this.placeholderPaint = paint;
            paint.setColor(-16777216);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.objects.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            Item item = (Item) obj;
            return item.isActiveVideo ? view == item.textureViewStubView : view == item.imageView;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getItemPosition(Object obj) {
            int indexOf = this.objects.indexOf((Item) obj);
            if (indexOf == -1) {
                return -2;
            }
            return indexOf;
        }

        /* JADX WARN: Removed duplicated region for block: B:77:0x0292  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x0295  */
        @Override // androidx.viewpager.widget.PagerAdapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Item instantiateItem(ViewGroup viewGroup, int i) {
            boolean z;
            Item item = this.objects.get(i);
            int realPosition = getRealPosition(i);
            boolean z2 = true;
            if (ProfileGalleryView.this.hasActiveVideo && realPosition == 0) {
                item.isActiveVideo = true;
                if (item.textureViewStubView == null) {
                    item.textureViewStubView = new TextureStubView(ProfileGalleryView.this, this.context);
                }
                if (item.textureViewStubView.getParent() == null) {
                    viewGroup.addView(item.textureViewStubView);
                }
                return item;
            }
            item.isActiveVideo = false;
            if (item.textureViewStubView != null && item.textureViewStubView.getParent() != null) {
                viewGroup.removeView(item.textureViewStubView);
            }
            if (item.imageView == null) {
                item.imageView = new AvatarImageView(this.context, i, this.placeholderPaint);
                this.imageViews.set(i, item.imageView);
            }
            if (item.imageView.getParent() == null) {
                viewGroup.addView(item.imageView);
            }
            item.imageView.getImageReceiver().setAllowDecodeSingleFrame(true);
            if (ProfileGalleryView.this.hasActiveVideo) {
                realPosition--;
            }
            if (realPosition != 0) {
                ImageLocation imageLocation = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition);
                item.imageView.isVideo = imageLocation != null;
                item.imageView.setImageMedia(imageLocation, null, (ImageLocation) ProfileGalleryView.this.imagesLocations.get(realPosition), null, (ImageLocation) ProfileGalleryView.this.thumbsLocations.get(realPosition), ((ImageLocation) ProfileGalleryView.this.thumbsLocations.get(realPosition)).photoSize instanceof TLRPC$TL_photoStrippedSize ? "b" : null, null, ((Integer) ProfileGalleryView.this.imagesLocationsSizes.get(realPosition)).intValue(), 1, "avatar_" + ProfileGalleryView.this.dialogId);
            } else {
                BackupImageView backupImageView = this.parentAvatarImageView;
                Drawable drawable = backupImageView == null ? null : backupImageView.getImageReceiver().getDrawable();
                if (drawable instanceof AnimatedFileDrawable) {
                    AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                    if (animatedFileDrawable.hasBitmap()) {
                        item.imageView.setImageDrawable(drawable);
                        animatedFileDrawable.addSecondParentView(item.imageView);
                        animatedFileDrawable.setInvalidateParentViewWithSecond(true);
                        z = false;
                        if (ProfileGalleryView.this.imagesUploadProgress.get(realPosition) == null) {
                            z2 = z;
                        }
                        if (z2) {
                            item.imageView.radialProgress = (RadialProgress2) ProfileGalleryView.this.radialProgresses.get(realPosition);
                            if (item.imageView.radialProgress == null) {
                                item.imageView.radialProgress = new RadialProgress2(item.imageView);
                                item.imageView.radialProgress.setOverrideAlpha(0.0f);
                                item.imageView.radialProgress.setIcon(10, false, false);
                                item.imageView.radialProgress.setColors(1107296256, 1107296256, -1, -1);
                                ProfileGalleryView.this.radialProgresses.append(realPosition, item.imageView.radialProgress);
                            }
                            if (ProfileGalleryView.this.invalidateWithParent) {
                                ProfileGalleryView.this.invalidate();
                            } else {
                                ProfileGalleryView.this.postInvalidateOnAnimation();
                            }
                        }
                        item.imageView.getImageReceiver().setDelegate(new AnonymousClass1());
                        item.imageView.getImageReceiver().setCrossfadeAlpha((byte) 2);
                        item.imageView.setRoundRadius(ProfileGalleryView.this.roundTopRadius, ProfileGalleryView.this.roundTopRadius, ProfileGalleryView.this.roundBottomRadius, ProfileGalleryView.this.roundBottomRadius);
                        return item;
                    }
                }
                ImageLocation imageLocation2 = (ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition);
                item.imageView.isVideo = imageLocation2 != null;
                String str = (!ProfileGalleryView.this.isProfileFragment || imageLocation2 == null || imageLocation2.imageType != 2) ? null : "avatar";
                ImageLocation imageLocation3 = (ImageLocation) ProfileGalleryView.this.thumbsLocations.get(realPosition);
                Bitmap bitmap = (this.parentAvatarImageView == null || !ProfileGalleryView.this.createThumbFromParent) ? null : this.parentAvatarImageView.getImageReceiver().getBitmap();
                String str2 = "avatar_" + ProfileGalleryView.this.dialogId;
                if (bitmap != null) {
                    item.imageView.setImageMedia((ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition), str, (ImageLocation) ProfileGalleryView.this.imagesLocations.get(realPosition), null, bitmap, ((Integer) ProfileGalleryView.this.imagesLocationsSizes.get(realPosition)).intValue(), 1, str2);
                } else if (ProfileGalleryView.this.uploadingImageLocation != null) {
                    item.imageView.setImageMedia((ImageLocation) ProfileGalleryView.this.videoLocations.get(realPosition), str, (ImageLocation) ProfileGalleryView.this.imagesLocations.get(realPosition), null, ProfileGalleryView.this.uploadingImageLocation, null, null, ((Integer) ProfileGalleryView.this.imagesLocationsSizes.get(realPosition)).intValue(), 1, str2);
                } else {
                    item.imageView.setImageMedia(imageLocation2, null, (ImageLocation) ProfileGalleryView.this.imagesLocations.get(realPosition), null, (ImageLocation) ProfileGalleryView.this.thumbsLocations.get(realPosition), imageLocation3.photoSize instanceof TLRPC$TL_photoStrippedSize ? "b" : null, null, ((Integer) ProfileGalleryView.this.imagesLocationsSizes.get(realPosition)).intValue(), 1, str2);
                }
            }
            z = true;
            if (ProfileGalleryView.this.imagesUploadProgress.get(realPosition) == null) {
            }
            if (z2) {
            }
            item.imageView.getImageReceiver().setDelegate(new AnonymousClass1());
            item.imageView.getImageReceiver().setCrossfadeAlpha((byte) 2);
            item.imageView.setRoundRadius(ProfileGalleryView.this.roundTopRadius, ProfileGalleryView.this.roundTopRadius, ProfileGalleryView.this.roundBottomRadius, ProfileGalleryView.this.roundBottomRadius);
            return item;
        }

        /* renamed from: org.telegram.ui.Components.ProfileGalleryView$ViewPagerAdapter$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements ImageReceiver.ImageReceiverDelegate {
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
            }

            AnonymousClass1() {
                ViewPagerAdapter.this = r1;
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public void onAnimationReady(ImageReceiver imageReceiver) {
                ProfileGalleryView.this.callback.onVideoSet();
            }
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            Item item = (Item) obj;
            if (item.textureViewStubView != null) {
                viewGroup.removeView(item.textureViewStubView);
            }
            if (item.isActiveVideo) {
                return;
            }
            AvatarImageView avatarImageView = item.imageView;
            if (avatarImageView.getImageReceiver().hasStaticThumb()) {
                Drawable drawable = avatarImageView.getImageReceiver().getDrawable();
                if (drawable instanceof AnimatedFileDrawable) {
                    ((AnimatedFileDrawable) drawable).removeSecondParentView(avatarImageView);
                }
            }
            avatarImageView.setRoundRadius(0);
            viewGroup.removeView(avatarImageView);
            avatarImageView.getImageReceiver().cancelLoadImage();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public CharSequence getPageTitle(int i) {
            return (getRealPosition(i) + 1) + "/" + (getCount() - (getExtraCount() * 2));
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void notifyDataSetChanged() {
            for (int i = 0; i < this.imageViews.size(); i++) {
                if (this.imageViews.get(i) != null) {
                    this.imageViews.get(i).getImageReceiver().cancelLoadImage();
                }
            }
            this.objects.clear();
            this.imageViews.clear();
            int size = ProfileGalleryView.this.imagesLocations.size();
            if (ProfileGalleryView.this.hasActiveVideo) {
                size++;
            }
            int extraCount = size + (getExtraCount() * 2);
            for (int i2 = 0; i2 < extraCount; i2++) {
                this.objects.add(new Item(null));
                this.imageViews.add(null);
            }
            super.notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.CircularViewPager.Adapter
        public int getExtraCount() {
            int size = ProfileGalleryView.this.imagesLocations.size();
            if (ProfileGalleryView.this.hasActiveVideo) {
                size++;
            }
            if (size >= 2) {
                return ProfileGalleryView.this.getOffscreenPageLimit();
            }
            return 0;
        }
    }

    public void setData(long j) {
        setData(j, false);
    }

    public void setData(long j, boolean z) {
        if (this.dialogId == j && !z) {
            resetCurrentItem();
            return;
        }
        this.forceResetPosition = true;
        this.adapter.notifyDataSetChanged();
        reset();
        this.dialogId = j;
        if (j == 0) {
            return;
        }
        MessagesController.getInstance(this.currentAccount).loadDialogPhotos(j, 80, 0, true, this.parentClassGuid);
    }

    private void reset() {
        this.videoFileNames.clear();
        this.thumbsFileNames.clear();
        this.photos.clear();
        this.videoLocations.clear();
        this.imagesLocations.clear();
        this.thumbsLocations.clear();
        this.imagesLocationsSizes.clear();
        this.imagesUploadProgress.clear();
        this.adapter.notifyDataSetChanged();
        this.uploadingImageLocation = null;
    }

    public void setRoundRadius(int i, int i2) {
        this.roundTopRadius = i;
        this.roundBottomRadius = i2;
        if (this.adapter != null) {
            for (int i3 = 0; i3 < this.adapter.objects.size(); i3++) {
                if (((Item) this.adapter.objects.get(i3)).imageView != null) {
                    AvatarImageView avatarImageView = ((Item) this.adapter.objects.get(i3)).imageView;
                    int i4 = this.roundTopRadius;
                    int i5 = this.roundBottomRadius;
                    avatarImageView.setRoundRadius(i4, i4, i5, i5);
                }
            }
        }
    }

    public void setParentAvatarImage(BackupImageView backupImageView) {
        ViewPagerAdapter viewPagerAdapter = this.adapter;
        if (viewPagerAdapter != null) {
            viewPagerAdapter.parentAvatarImageView = backupImageView;
        }
    }

    public void setUploadProgress(ImageLocation imageLocation, float f) {
        if (imageLocation == null) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.imagesLocations.size()) {
                break;
            } else if (this.imagesLocations.get(i) == imageLocation) {
                this.imagesUploadProgress.set(i, Float.valueOf(f));
                if (this.radialProgresses.get(i) != null) {
                    this.radialProgresses.get(i).setProgress(f, true);
                }
            } else {
                i++;
            }
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            getChildAt(i2).invalidate();
        }
    }

    public void setCreateThumbFromParent(boolean z) {
        this.createThumbFromParent = z;
    }

    /* loaded from: classes3.dex */
    public class AvatarImageView extends BackupImageView {
        public boolean isVideo;
        private final Paint placeholderPaint;
        private final int position;
        private RadialProgress2 radialProgress;
        private ValueAnimator radialProgressHideAnimator;
        private float radialProgressHideAnimatorStartValue;
        private final int radialProgressSize = AndroidUtilities.dp(64.0f);
        private long firstDrawTime = -1;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AvatarImageView(Context context, int i, Paint paint) {
            super(context);
            ProfileGalleryView.this = r3;
            this.position = i;
            this.placeholderPaint = paint;
            setLayerNum(r3.imagesLayerNum);
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            if (this.radialProgress != null) {
                int currentActionBarHeight = (ProfileGalleryView.this.parentActionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight();
                int dp2 = AndroidUtilities.dp2(80.0f);
                RadialProgress2 radialProgress2 = this.radialProgress;
                int i5 = this.radialProgressSize;
                int i6 = (i2 - currentActionBarHeight) - dp2;
                radialProgress2.setProgressRect((i - i5) / 2, ((i6 - i5) / 2) + currentActionBarHeight, (i + i5) / 2, currentActionBarHeight + ((i6 + i5) / 2));
            }
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            ProfileGalleryView profileGalleryView;
            ProfileGalleryView profileGalleryView2;
            ProfileGalleryView profileGalleryView3;
            PinchToZoomHelper pinchToZoomHelper = ProfileGalleryView.this.pinchToZoomHelper;
            if (pinchToZoomHelper == null || !pinchToZoomHelper.isInOverlayMode()) {
                if (this.radialProgress != null) {
                    int realPosition = ProfileGalleryView.this.getRealPosition(this.position);
                    if (ProfileGalleryView.this.hasActiveVideo) {
                        realPosition--;
                    }
                    Drawable drawable = getImageReceiver().getDrawable();
                    long j = 0;
                    if (realPosition >= ProfileGalleryView.this.imagesUploadProgress.size() || ProfileGalleryView.this.imagesUploadProgress.get(realPosition) == null ? !(drawable == null || (this.isVideo && (!(drawable instanceof AnimatedFileDrawable) || ((AnimatedFileDrawable) drawable).getDurationMs() <= 0))) : ((Float) ProfileGalleryView.this.imagesUploadProgress.get(realPosition)).floatValue() >= 1.0f) {
                        if (this.radialProgressHideAnimator == null) {
                            if (this.radialProgress.getProgress() < 1.0f) {
                                this.radialProgress.setProgress(1.0f, true);
                                j = 100;
                            }
                            this.radialProgressHideAnimatorStartValue = this.radialProgress.getOverrideAlpha();
                            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                            this.radialProgressHideAnimator = ofFloat;
                            ofFloat.setStartDelay(j);
                            this.radialProgressHideAnimator.setDuration(this.radialProgressHideAnimatorStartValue * 250.0f);
                            this.radialProgressHideAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                            this.radialProgressHideAnimator.addUpdateListener(new ProfileGalleryView$AvatarImageView$$ExternalSyntheticLambda0(this));
                            this.radialProgressHideAnimator.addListener(new AnonymousClass1(realPosition));
                            this.radialProgressHideAnimator.start();
                        }
                    } else {
                        if (this.firstDrawTime < 0) {
                            this.firstDrawTime = System.currentTimeMillis();
                        } else {
                            long currentTimeMillis = System.currentTimeMillis() - this.firstDrawTime;
                            long j2 = this.isVideo ? 250L : 750L;
                            if (currentTimeMillis <= 250 + j2 && currentTimeMillis > j2) {
                                this.radialProgress.setOverrideAlpha(CubicBezierInterpolator.DEFAULT.getInterpolation(((float) (currentTimeMillis - j2)) / 250.0f));
                            }
                        }
                        if (ProfileGalleryView.this.invalidateWithParent) {
                            invalidate();
                        } else {
                            postInvalidateOnAnimation();
                        }
                        invalidate();
                    }
                    if (ProfileGalleryView.this.roundTopRadius != 0 || ProfileGalleryView.this.roundBottomRadius != 0) {
                        if (ProfileGalleryView.this.roundTopRadius == ProfileGalleryView.this.roundBottomRadius) {
                            ProfileGalleryView.this.rect.set(0.0f, 0.0f, getWidth(), getHeight());
                            canvas.drawRoundRect(ProfileGalleryView.this.rect, profileGalleryView3.roundTopRadius, ProfileGalleryView.this.roundTopRadius, this.placeholderPaint);
                        } else {
                            ProfileGalleryView.this.path.reset();
                            ProfileGalleryView.this.rect.set(0.0f, 0.0f, getWidth(), getHeight());
                            for (int i = 0; i < 4; i++) {
                                ProfileGalleryView.this.radii[i] = profileGalleryView.roundTopRadius;
                                ProfileGalleryView.this.radii[i + 4] = profileGalleryView2.roundBottomRadius;
                            }
                            ProfileGalleryView profileGalleryView4 = ProfileGalleryView.this;
                            profileGalleryView4.path.addRoundRect(profileGalleryView4.rect, profileGalleryView4.radii, Path.Direction.CW);
                            canvas.drawPath(ProfileGalleryView.this.path, this.placeholderPaint);
                        }
                    } else {
                        canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.placeholderPaint);
                    }
                }
                super.onDraw(canvas);
                RadialProgress2 radialProgress2 = this.radialProgress;
                if (radialProgress2 == null || radialProgress2.getOverrideAlpha() <= 0.0f) {
                    return;
                }
                this.radialProgress.draw(canvas);
            }
        }

        public /* synthetic */ void lambda$onDraw$0(ValueAnimator valueAnimator) {
            this.radialProgress.setOverrideAlpha(AndroidUtilities.lerp(this.radialProgressHideAnimatorStartValue, 0.0f, valueAnimator.getAnimatedFraction()));
        }

        /* renamed from: org.telegram.ui.Components.ProfileGalleryView$AvatarImageView$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$finalRealPosition;

            AnonymousClass1(int i) {
                AvatarImageView.this = r1;
                this.val$finalRealPosition = i;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AvatarImageView.this.radialProgress = null;
                ProfileGalleryView.this.radialProgresses.delete(this.val$finalRealPosition);
            }
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            if (ProfileGalleryView.this.invalidateWithParent) {
                ProfileGalleryView.this.invalidate();
            }
        }
    }

    public void setPinchToZoomHelper(PinchToZoomHelper pinchToZoomHelper) {
        this.pinchToZoomHelper = pinchToZoomHelper;
    }

    public void setInvalidateWithParent(boolean z) {
        this.invalidateWithParent = z;
    }

    /* loaded from: classes3.dex */
    public class TextureStubView extends View {
        public TextureStubView(ProfileGalleryView profileGalleryView, Context context) {
            super(context);
        }
    }
}
