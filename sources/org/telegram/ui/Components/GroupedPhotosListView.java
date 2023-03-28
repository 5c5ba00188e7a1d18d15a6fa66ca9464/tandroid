package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$PhotoSize;
/* loaded from: classes3.dex */
public class GroupedPhotosListView extends View implements GestureDetector.OnGestureListener {
    private boolean animateAllLine;
    private boolean animateBackground;
    private int animateToDX;
    private int animateToDXStart;
    private int animateToItem;
    private boolean animateToItemFast;
    private boolean animationsEnabled;
    private Paint backgroundPaint;
    private long currentGroupId;
    private int currentImage;
    private float currentItemProgress;
    private ArrayList<Object> currentObjects;
    public ArrayList<ImageLocation> currentPhotos;
    private GroupedPhotosListViewDelegate delegate;
    private float drawAlpha;
    private int drawDx;
    private GestureDetector gestureDetector;
    private boolean hasPhotos;
    private ValueAnimator hideAnimator;
    private boolean ignoreChanges;
    private ArrayList<ImageReceiver> imagesToDraw;
    private int itemHeight;
    private int itemSpacing;
    private int itemWidth;
    private int itemY;
    private long lastUpdateTime;
    private float moveLineProgress;
    private boolean moving;
    private int nextImage;
    private float nextItemProgress;
    private int nextPhotoScrolling;
    private android.widget.Scroller scroll;
    private boolean scrolling;
    private ValueAnimator showAnimator;
    private boolean stopedScrolling;
    private ArrayList<ImageReceiver> unusedReceivers;

    /* loaded from: classes3.dex */
    public interface GroupedPhotosListViewDelegate {
        long getAvatarsDialogId();

        int getCurrentAccount();

        int getCurrentIndex();

        ArrayList<MessageObject> getImagesArr();

        ArrayList<ImageLocation> getImagesArrLocations();

        List<TLRPC$PageBlock> getPageBlockArr();

        Object getParentObject();

        int getSlideshowMessageId();

        void onShowAnimationStart();

        void onStopScrolling();

        void setCurrentIndex(int i);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    public GroupedPhotosListView(Context context, int i) {
        super(context);
        this.backgroundPaint = new Paint();
        this.unusedReceivers = new ArrayList<>();
        this.imagesToDraw = new ArrayList<>();
        this.currentPhotos = new ArrayList<>();
        this.currentObjects = new ArrayList<>();
        this.currentItemProgress = 1.0f;
        this.nextItemProgress = 0.0f;
        this.animateToItem = -1;
        this.animationsEnabled = true;
        this.nextPhotoScrolling = -1;
        this.animateBackground = true;
        this.gestureDetector = new GestureDetector(context, this);
        this.scroll = new android.widget.Scroller(context);
        this.itemWidth = AndroidUtilities.dp(42.0f);
        this.itemHeight = AndroidUtilities.dp(56.0f);
        this.itemSpacing = AndroidUtilities.dp(1.0f);
        this.itemY = i;
        this.backgroundPaint.setColor(2130706432);
    }

    public void clear() {
        this.currentPhotos.clear();
        this.currentObjects.clear();
        this.imagesToDraw.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:122:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:196:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v7, types: [org.telegram.messenger.MessageObject] */
    /* JADX WARN: Type inference failed for: r11v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillList() {
        ArrayList<ImageLocation> arrayList;
        Object obj;
        boolean z;
        int i;
        Object obj2;
        Object obj3;
        boolean z2;
        if (this.ignoreChanges) {
            this.ignoreChanges = false;
            return;
        }
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        List<TLRPC$PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        int slideshowMessageId = this.delegate.getSlideshowMessageId();
        this.delegate.getCurrentAccount();
        this.hasPhotos = false;
        if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
            if (currentIndex >= imagesArrLocations.size()) {
                currentIndex = imagesArrLocations.size() - 1;
            }
            int size = imagesArrLocations.size();
            this.hasPhotos = true;
            arrayList = imagesArrLocations;
            i = size;
            z = false;
            obj3 = (ImageLocation) imagesArrLocations.get(currentIndex);
        } else if (imagesArr != null && !imagesArr.isEmpty()) {
            if (currentIndex >= imagesArr.size()) {
                currentIndex = imagesArr.size() - 1;
            }
            MessageObject messageObject = imagesArr.get(currentIndex);
            long groupIdForUse = messageObject.getGroupIdForUse();
            if (groupIdForUse != this.currentGroupId) {
                this.currentGroupId = groupIdForUse;
                z = true;
            } else {
                z = false;
            }
            if (this.currentGroupId != 0) {
                this.hasPhotos = true;
                int min = Math.min(currentIndex + 10, imagesArr.size());
                int i2 = currentIndex;
                i = 0;
                while (true) {
                    if (i2 >= min) {
                        obj2 = messageObject;
                        break;
                    }
                    MessageObject messageObject2 = imagesArr.get(i2);
                    if (slideshowMessageId == 0) {
                        obj2 = messageObject;
                        if (messageObject2.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                    } else {
                        obj2 = messageObject;
                    }
                    i++;
                    i2++;
                    messageObject = obj2;
                }
                int max = Math.max(currentIndex - 10, 0);
                int i3 = currentIndex - 1;
                while (true) {
                    if (i3 < max) {
                        arrayList = imagesArrLocations;
                        break;
                    }
                    MessageObject messageObject3 = imagesArr.get(i3);
                    if (slideshowMessageId == 0) {
                        arrayList = imagesArrLocations;
                        if (messageObject3.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                    } else {
                        arrayList = imagesArrLocations;
                    }
                    i++;
                    i3--;
                    imagesArrLocations = arrayList;
                }
            } else {
                arrayList = imagesArrLocations;
                obj2 = messageObject;
                i = 0;
            }
            obj3 = obj2;
        } else {
            arrayList = imagesArrLocations;
            if (pageBlockArr == null || pageBlockArr.isEmpty()) {
                obj = null;
                z = false;
            } else {
                TLRPC$PageBlock tLRPC$PageBlock = pageBlockArr.get(currentIndex);
                int i4 = tLRPC$PageBlock.groupId;
                if (i4 != this.currentGroupId) {
                    this.currentGroupId = i4;
                    z = true;
                } else {
                    z = false;
                }
                obj = tLRPC$PageBlock;
                if (this.currentGroupId != 0) {
                    this.hasPhotos = true;
                    int size2 = pageBlockArr.size();
                    int i5 = 0;
                    for (int i6 = currentIndex; i6 < size2 && pageBlockArr.get(i6).groupId == this.currentGroupId; i6++) {
                        i5++;
                    }
                    for (int i7 = currentIndex - 1; i7 >= 0 && pageBlockArr.get(i7).groupId == this.currentGroupId; i7--) {
                        i5++;
                    }
                    i = i5;
                    obj3 = tLRPC$PageBlock;
                }
            }
            i = 0;
            obj3 = obj;
        }
        if (obj3 == null) {
            return;
        }
        if (this.animationsEnabled) {
            if (!this.hasPhotos) {
                ValueAnimator valueAnimator = this.showAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.showAnimator = null;
                }
                if (this.drawAlpha > 0.0f && this.currentPhotos.size() > 1) {
                    if (this.hideAnimator == null) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.drawAlpha, 0.0f);
                        this.hideAnimator = ofFloat;
                        ofFloat.setDuration(this.drawAlpha * 200.0f);
                        this.hideAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupedPhotosListView.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (GroupedPhotosListView.this.hideAnimator == animator) {
                                    GroupedPhotosListView.this.hideAnimator = null;
                                    GroupedPhotosListView.this.fillList();
                                }
                            }
                        });
                        this.hideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupedPhotosListView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                GroupedPhotosListView.this.lambda$fillList$0(valueAnimator2);
                            }
                        });
                        this.hideAnimator.start();
                        return;
                    }
                    return;
                }
            } else {
                ValueAnimator valueAnimator2 = this.hideAnimator;
                if (valueAnimator2 != null) {
                    this.hideAnimator = null;
                    valueAnimator2.cancel();
                }
                float f = this.drawAlpha;
                if (f < 1.0f && this.showAnimator == null) {
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(f, 1.0f);
                    this.showAnimator = ofFloat2;
                    ofFloat2.setDuration((1.0f - this.drawAlpha) * 200.0f);
                    this.showAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupedPhotosListView.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                            if (GroupedPhotosListView.this.delegate != null) {
                                GroupedPhotosListView.this.delegate.onShowAnimationStart();
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (GroupedPhotosListView.this.showAnimator == animator) {
                                GroupedPhotosListView.this.showAnimator = null;
                            }
                        }
                    });
                    this.showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupedPhotosListView$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                            GroupedPhotosListView.this.lambda$fillList$1(valueAnimator3);
                        }
                    });
                }
            }
        }
        if (!z) {
            if (i == this.currentPhotos.size() && this.currentObjects.contains(obj3)) {
                int indexOf = this.currentObjects.indexOf(obj3);
                int i8 = this.currentImage;
                if (i8 != indexOf && indexOf != -1) {
                    boolean z3 = this.animateAllLine;
                    if (!z3 && !this.moving && (indexOf == i8 - 1 || indexOf == i8 + 1)) {
                        this.animateToItemFast = true;
                        z3 = true;
                    }
                    if (z3) {
                        this.animateToItem = indexOf;
                        this.nextImage = indexOf;
                        this.animateToDX = (i8 - indexOf) * (this.itemWidth + this.itemSpacing);
                        this.moving = true;
                        this.animateAllLine = false;
                        this.lastUpdateTime = System.currentTimeMillis();
                        invalidate();
                        z2 = false;
                    } else {
                        fillImages(true, (i8 - indexOf) * (this.itemWidth + this.itemSpacing));
                        this.currentImage = indexOf;
                        z2 = false;
                        this.moving = false;
                    }
                    this.drawDx = z2 ? 1 : 0;
                }
            } else {
                z2 = false;
                z = true;
            }
            if (z) {
                return;
            }
            int size3 = this.currentPhotos.size();
            this.animateAllLine = z2;
            this.currentPhotos.clear();
            this.currentObjects.clear();
            if (arrayList != null && !arrayList.isEmpty()) {
                ArrayList<ImageLocation> arrayList2 = arrayList;
                this.currentObjects.addAll(arrayList2);
                this.currentPhotos.addAll(arrayList2);
                this.currentImage = currentIndex;
                this.animateToItem = -1;
                this.animateToItemFast = false;
            } else if (imagesArr != null && !imagesArr.isEmpty()) {
                if (this.currentGroupId != 0 || slideshowMessageId != 0) {
                    int min2 = Math.min(currentIndex + 10, imagesArr.size());
                    for (int i9 = currentIndex; i9 < min2; i9++) {
                        MessageObject messageObject4 = imagesArr.get(i9);
                        if (slideshowMessageId == 0 && messageObject4.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(messageObject4);
                        this.currentPhotos.add(ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject4.photoThumbs, 56, true), messageObject4.photoThumbsObject));
                    }
                    this.currentImage = 0;
                    this.animateToItem = -1;
                    this.animateToItemFast = false;
                    int max2 = Math.max(currentIndex - 10, 0);
                    for (int i10 = currentIndex - 1; i10 >= max2; i10--) {
                        MessageObject messageObject5 = imagesArr.get(i10);
                        if (slideshowMessageId == 0 && messageObject5.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(0, messageObject5);
                        this.currentPhotos.add(0, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject5.photoThumbs, 56, true), messageObject5.photoThumbsObject));
                        this.currentImage++;
                    }
                }
            } else if (pageBlockArr != null && !pageBlockArr.isEmpty() && this.currentGroupId != 0) {
                int size4 = pageBlockArr.size();
                for (int i11 = currentIndex; i11 < size4; i11++) {
                    TLRPC$PageBlock tLRPC$PageBlock2 = pageBlockArr.get(i11);
                    if (tLRPC$PageBlock2.groupId != this.currentGroupId) {
                        break;
                    }
                    this.currentObjects.add(tLRPC$PageBlock2);
                    this.currentPhotos.add(ImageLocation.getForObject(tLRPC$PageBlock2.thumb, tLRPC$PageBlock2.thumbObject));
                }
                this.currentImage = 0;
                this.animateToItem = -1;
                this.animateToItemFast = false;
                for (int i12 = currentIndex - 1; i12 >= 0; i12--) {
                    TLRPC$PageBlock tLRPC$PageBlock3 = pageBlockArr.get(i12);
                    if (tLRPC$PageBlock3.groupId != this.currentGroupId) {
                        break;
                    }
                    this.currentObjects.add(0, tLRPC$PageBlock3);
                    this.currentPhotos.add(0, ImageLocation.getForObject(tLRPC$PageBlock3.thumb, tLRPC$PageBlock3.thumbObject));
                    this.currentImage++;
                }
            }
            if (this.currentPhotos.size() == 1) {
                this.currentPhotos.clear();
                this.currentObjects.clear();
            }
            if (this.currentPhotos.size() != size3) {
                requestLayout();
            }
            fillImages(false, 0);
            return;
        }
        z2 = false;
        if (z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillList$0(ValueAnimator valueAnimator) {
        this.drawAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillList$1(ValueAnimator valueAnimator) {
        this.drawAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void setMoveProgress(float f) {
        if (this.scrolling || this.animateToItem >= 0) {
            return;
        }
        if (f > 0.0f) {
            this.nextImage = this.currentImage - 1;
        } else {
            this.nextImage = this.currentImage + 1;
        }
        int i = this.nextImage;
        if (i >= 0 && i < this.currentPhotos.size()) {
            this.currentItemProgress = 1.0f - Math.abs(f);
        } else {
            this.currentItemProgress = 1.0f;
        }
        this.nextItemProgress = 1.0f - this.currentItemProgress;
        this.moving = f != 0.0f;
        invalidate();
        if (this.currentPhotos.isEmpty()) {
            return;
        }
        if (f >= 0.0f || this.currentImage != this.currentPhotos.size() - 1) {
            if (f <= 0.0f || this.currentImage != 0) {
                int i2 = (int) (f * (this.itemWidth + this.itemSpacing));
                this.drawDx = i2;
                fillImages(true, i2);
            }
        }
    }

    private ImageReceiver getFreeReceiver() {
        ImageReceiver imageReceiver;
        if (this.unusedReceivers.isEmpty()) {
            imageReceiver = new ImageReceiver(this);
        } else {
            imageReceiver = this.unusedReceivers.get(0);
            this.unusedReceivers.remove(0);
        }
        this.imagesToDraw.add(imageReceiver);
        imageReceiver.setCurrentAccount(this.delegate.getCurrentAccount());
        return imageReceiver;
    }

    private void fillImages(boolean z, int i) {
        int i2;
        int i3;
        Object obj;
        Object obj2;
        if (!z && !this.imagesToDraw.isEmpty()) {
            this.unusedReceivers.addAll(this.imagesToDraw);
            this.imagesToDraw.clear();
            this.moving = false;
            this.moveLineProgress = 1.0f;
            this.currentItemProgress = 1.0f;
            this.nextItemProgress = 0.0f;
        }
        invalidate();
        if (getMeasuredWidth() == 0 || this.currentPhotos.isEmpty()) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredWidth2 = (getMeasuredWidth() / 2) - (this.itemWidth / 2);
        if (z) {
            int size = this.imagesToDraw.size();
            int i4 = 0;
            i2 = Integer.MIN_VALUE;
            i3 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            while (i4 < size) {
                ImageReceiver imageReceiver = this.imagesToDraw.get(i4);
                int param = imageReceiver.getParam();
                int i5 = this.itemWidth;
                int i6 = ((param - this.currentImage) * (this.itemSpacing + i5)) + measuredWidth2 + i;
                if (i6 > measuredWidth || i6 + i5 < 0) {
                    this.unusedReceivers.add(imageReceiver);
                    this.imagesToDraw.remove(i4);
                    size--;
                    i4--;
                }
                i3 = Math.min(i3, param - 1);
                i2 = Math.max(i2, param + 1);
                i4++;
            }
        } else {
            i2 = this.currentImage;
            i3 = i2 - 1;
        }
        if (i2 != Integer.MIN_VALUE) {
            int size2 = this.currentPhotos.size();
            while (i2 < size2) {
                int i7 = ((i2 - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth2 + i;
                if (i7 >= measuredWidth) {
                    break;
                }
                ImageLocation imageLocation = this.currentPhotos.get(i2);
                ImageReceiver freeReceiver = getFreeReceiver();
                freeReceiver.setImageCoords(i7, this.itemY, this.itemWidth, this.itemHeight);
                if (this.currentObjects.get(0) instanceof MessageObject) {
                    obj2 = this.currentObjects.get(i2);
                } else if (this.currentObjects.get(0) instanceof TLRPC$PageBlock) {
                    obj2 = this.delegate.getParentObject();
                } else {
                    obj2 = "avatar_" + this.delegate.getAvatarsDialogId();
                }
                freeReceiver.setImage(null, null, imageLocation, "80_80", 0L, null, obj2, 1);
                freeReceiver.setParam(i2);
                i2++;
            }
        }
        if (i3 != Integer.MAX_VALUE) {
            while (i3 >= 0) {
                int i8 = this.itemWidth;
                int i9 = ((i3 - this.currentImage) * (this.itemSpacing + i8)) + measuredWidth2 + i + i8;
                if (i9 <= 0) {
                    break;
                }
                ImageLocation imageLocation2 = this.currentPhotos.get(i3);
                ImageReceiver freeReceiver2 = getFreeReceiver();
                freeReceiver2.setImageCoords(i9, this.itemY, this.itemWidth, this.itemHeight);
                if (this.currentObjects.get(0) instanceof MessageObject) {
                    obj = this.currentObjects.get(i3);
                } else if (this.currentObjects.get(0) instanceof TLRPC$PageBlock) {
                    obj = this.delegate.getParentObject();
                } else {
                    obj = "avatar_" + this.delegate.getAvatarsDialogId();
                }
                freeReceiver2.setImage(null, null, imageLocation2, "80_80", 0L, null, obj, 1);
                freeReceiver2.setParam(i3);
                i3--;
            }
        }
        ValueAnimator valueAnimator = this.showAnimator;
        if (valueAnimator == null || valueAnimator.isStarted()) {
            return;
        }
        this.showAnimator.start();
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        this.animateToItem = -1;
        this.animateToItemFast = false;
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        List<TLRPC$PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        stopScrolling();
        int size = this.imagesToDraw.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            ImageReceiver imageReceiver = this.imagesToDraw.get(i);
            if (imageReceiver.isInsideImage(motionEvent.getX(), motionEvent.getY())) {
                int param = imageReceiver.getParam();
                if (param < 0 || param >= this.currentObjects.size()) {
                    return true;
                }
                if (imagesArr != null && !imagesArr.isEmpty()) {
                    int indexOf = imagesArr.indexOf((MessageObject) this.currentObjects.get(param));
                    if (currentIndex == indexOf) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(indexOf);
                } else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                    int indexOf2 = pageBlockArr.indexOf((TLRPC$PageBlock) this.currentObjects.get(param));
                    if (currentIndex == indexOf2) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(indexOf2);
                } else if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
                    int indexOf3 = imagesArrLocations.indexOf((ImageLocation) this.currentObjects.get(param));
                    if (currentIndex == indexOf3) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(indexOf3);
                }
            } else {
                i++;
            }
        }
        return false;
    }

    private void updateAfterScroll() {
        int i;
        int i2;
        int i3;
        int i4 = this.drawDx;
        int abs = Math.abs(i4);
        int i5 = this.itemWidth;
        int i6 = this.itemSpacing;
        int i7 = -1;
        if (abs > (i5 / 2) + i6) {
            if (i4 > 0) {
                i2 = i4 - ((i5 / 2) + i6);
                i3 = 1;
            } else {
                i2 = i4 + (i5 / 2) + i6;
                i3 = -1;
            }
            i = i3 + (i2 / (i5 + (i6 * 2)));
        } else {
            i = 0;
        }
        this.nextPhotoScrolling = this.currentImage - i;
        int currentIndex = this.delegate.getCurrentIndex();
        ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        List<TLRPC$PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        int i8 = this.nextPhotoScrolling;
        if (currentIndex != i8 && i8 >= 0 && i8 < this.currentPhotos.size()) {
            Object obj = this.currentObjects.get(this.nextPhotoScrolling);
            if (imagesArr != null && !imagesArr.isEmpty()) {
                i7 = imagesArr.indexOf((MessageObject) obj);
            } else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                i7 = pageBlockArr.indexOf((TLRPC$PageBlock) obj);
            } else if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
                i7 = imagesArrLocations.indexOf((ImageLocation) obj);
            }
            if (i7 >= 0) {
                this.ignoreChanges = true;
                this.delegate.setCurrentIndex(i7);
            }
        }
        if (!this.scrolling) {
            this.scrolling = true;
            this.stopedScrolling = false;
        }
        fillImages(true, this.drawDx);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.drawDx = (int) (this.drawDx - f);
        int minScrollX = getMinScrollX();
        int maxScrollX = getMaxScrollX();
        int i = this.drawDx;
        if (i < minScrollX) {
            this.drawDx = minScrollX;
        } else if (i > maxScrollX) {
            this.drawDx = maxScrollX;
        }
        updateAfterScroll();
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.scroll.abortAnimation();
        if (this.currentPhotos.size() >= 10) {
            this.scroll.fling(this.drawDx, 0, Math.round(f), 0, getMinScrollX(), getMaxScrollX(), 0, 0);
            return false;
        }
        return false;
    }

    private void stopScrolling() {
        this.scrolling = false;
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        int i = this.nextPhotoScrolling;
        if (i >= 0 && i < this.currentObjects.size()) {
            this.stopedScrolling = true;
            this.animateToItemFast = false;
            int i2 = this.nextPhotoScrolling;
            this.animateToItem = i2;
            this.nextImage = i2;
            this.animateToDX = (this.currentImage - i2) * (this.itemWidth + this.itemSpacing);
            this.animateToDXStart = this.drawDx;
            this.moveLineProgress = 1.0f;
            this.nextPhotoScrolling = -1;
            GroupedPhotosListViewDelegate groupedPhotosListViewDelegate = this.delegate;
            if (groupedPhotosListViewDelegate != null) {
                groupedPhotosListViewDelegate.onStopScrolling();
            }
        }
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (!this.currentPhotos.isEmpty() && getAlpha() == 1.0f) {
            z = (this.gestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent)) ? true : true;
            if (this.scrolling && motionEvent.getAction() == 1 && this.scroll.isFinished()) {
                stopScrolling();
            }
        }
        return z;
    }

    private int getMinScrollX() {
        return (-((this.currentPhotos.size() - this.currentImage) - 1)) * (this.itemWidth + (this.itemSpacing * 2));
    }

    private int getMaxScrollX() {
        return this.currentImage * (this.itemWidth + (this.itemSpacing * 2));
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        fillImages(false, 0);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        float interpolation;
        int i5;
        int i6;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        TLRPC$PhotoSize tLRPC$PhotoSize2;
        if (this.hasPhotos || !this.imagesToDraw.isEmpty()) {
            float f = this.drawAlpha;
            if (!this.animateBackground) {
                f = this.hasPhotos ? 1.0f : 0.0f;
            }
            this.backgroundPaint.setAlpha((int) (f * 127.0f));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
            if (this.imagesToDraw.isEmpty()) {
                return;
            }
            int size = this.imagesToDraw.size();
            int i7 = this.drawDx;
            int i8 = (int) (this.itemWidth * 2.0f);
            int dp = AndroidUtilities.dp(8.0f);
            ImageLocation imageLocation = this.currentPhotos.get(this.currentImage);
            if (imageLocation != null && (tLRPC$PhotoSize2 = imageLocation.photoSize) != null) {
                i = Math.max(this.itemWidth, (int) (tLRPC$PhotoSize2.w * (this.itemHeight / tLRPC$PhotoSize2.h)));
            } else {
                i = this.itemHeight;
            }
            int min = Math.min(i8, i);
            float f2 = dp * 2;
            float f3 = this.currentItemProgress;
            int i9 = (int) (f2 * f3);
            int i10 = this.itemWidth + ((int) ((min - i2) * f3)) + i9;
            int i11 = this.nextImage;
            if (i11 >= 0 && i11 < this.currentPhotos.size()) {
                ImageLocation imageLocation2 = this.currentPhotos.get(this.nextImage);
                if (imageLocation2 != null && (tLRPC$PhotoSize = imageLocation2.photoSize) != null) {
                    i3 = Math.max(this.itemWidth, (int) (tLRPC$PhotoSize.w * (this.itemHeight / tLRPC$PhotoSize.h)));
                } else {
                    i3 = this.itemHeight;
                }
            } else {
                i3 = this.itemWidth;
            }
            int min2 = Math.min(i8, i3);
            float f4 = this.nextItemProgress;
            int i12 = (int) (f2 * f4);
            float f5 = i7;
            int i13 = (int) (f5 + ((((min2 + i12) - i4) / 2) * f4 * (this.nextImage > this.currentImage ? -1 : 1)));
            int i14 = this.itemWidth + ((int) ((min2 - i4) * f4)) + i12;
            int measuredWidth = (getMeasuredWidth() - i10) / 2;
            for (int i15 = 0; i15 < size; i15++) {
                ImageReceiver imageReceiver = this.imagesToDraw.get(i15);
                int param = imageReceiver.getParam();
                int i16 = this.currentImage;
                if (param == i16) {
                    imageReceiver.setImageX(measuredWidth + i13 + (i9 / 2));
                    imageReceiver.setImageWidth(i10 - i9);
                } else {
                    int i17 = this.nextImage;
                    if (i17 < i16) {
                        if (param >= i16) {
                            imageReceiver.setImageX(measuredWidth + i10 + this.itemSpacing + (((imageReceiver.getParam() - this.currentImage) - 1) * (this.itemWidth + this.itemSpacing)) + i13);
                        } else if (param <= i17) {
                            int i18 = this.itemWidth;
                            int i19 = this.itemSpacing;
                            imageReceiver.setImageX((((((imageReceiver.getParam() - this.currentImage) + 1) * (i18 + i19)) + measuredWidth) - (i19 + i14)) + i13);
                        } else {
                            imageReceiver.setImageX(((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth + i13);
                        }
                    } else if (param < i16) {
                        imageReceiver.setImageX(((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + measuredWidth + i13);
                    } else if (param <= i17) {
                        imageReceiver.setImageX(measuredWidth + i10 + this.itemSpacing + (((imageReceiver.getParam() - this.currentImage) - 1) * (this.itemWidth + this.itemSpacing)) + i13);
                    } else {
                        int i20 = this.itemWidth;
                        int i21 = this.itemSpacing;
                        imageReceiver.setImageX(measuredWidth + i10 + this.itemSpacing + (((imageReceiver.getParam() - this.currentImage) - 2) * (i20 + i21)) + i21 + i14 + i13);
                    }
                    if (param == this.nextImage) {
                        imageReceiver.setImageWidth(i14 - i12);
                        imageReceiver.setImageX((int) (imageReceiver.getImageX() + (i12 / 2)));
                    } else {
                        imageReceiver.setImageWidth(this.itemWidth);
                    }
                }
                imageReceiver.setAlpha(this.drawAlpha);
                imageReceiver.setRoundRadius(AndroidUtilities.dp(2.0f));
                imageReceiver.draw(canvas);
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastUpdateTime;
            if (j > 17) {
                j = 17;
            }
            this.lastUpdateTime = currentTimeMillis;
            int i22 = this.animateToItem;
            if (i22 >= 0) {
                float f6 = this.moveLineProgress;
                if (f6 > 0.0f) {
                    float f7 = (float) j;
                    float f8 = f6 - (f7 / (this.animateToItemFast ? 100.0f : 200.0f));
                    this.moveLineProgress = f8;
                    if (i22 == this.currentImage) {
                        float f9 = this.currentItemProgress;
                        if (f9 < 1.0f) {
                            float f10 = f9 + (f7 / 200.0f);
                            this.currentItemProgress = f10;
                            if (f10 > 1.0f) {
                                this.currentItemProgress = 1.0f;
                            }
                        }
                        this.drawDx = this.animateToDXStart + ((int) Math.ceil(this.currentItemProgress * (this.animateToDX - i6)));
                    } else {
                        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT;
                        this.nextItemProgress = cubicBezierInterpolator.getInterpolation(1.0f - f8);
                        if (this.stopedScrolling) {
                            float f11 = this.currentItemProgress;
                            if (f11 > 0.0f) {
                                float f12 = f11 - (f7 / 200.0f);
                                this.currentItemProgress = f12;
                                if (f12 < 0.0f) {
                                    this.currentItemProgress = 0.0f;
                                }
                            }
                            this.drawDx = this.animateToDXStart + ((int) Math.ceil(interpolation * (this.animateToDX - i5)));
                        } else {
                            this.currentItemProgress = cubicBezierInterpolator.getInterpolation(this.moveLineProgress);
                            this.drawDx = (int) Math.ceil(this.nextItemProgress * this.animateToDX);
                        }
                    }
                    if (this.moveLineProgress <= 0.0f) {
                        this.currentImage = this.animateToItem;
                        this.moveLineProgress = 1.0f;
                        this.currentItemProgress = 1.0f;
                        this.nextItemProgress = 0.0f;
                        this.moving = false;
                        this.stopedScrolling = false;
                        this.drawDx = 0;
                        this.animateToItem = -1;
                        this.animateToItemFast = false;
                    }
                }
                fillImages(true, this.drawDx);
                invalidate();
            }
            if (this.scrolling) {
                float f13 = this.currentItemProgress;
                if (f13 > 0.0f) {
                    float f14 = f13 - (((float) j) / 200.0f);
                    this.currentItemProgress = f14;
                    if (f14 < 0.0f) {
                        this.currentItemProgress = 0.0f;
                    }
                    invalidate();
                }
            }
            if (this.scroll.isFinished()) {
                return;
            }
            if (this.scroll.computeScrollOffset()) {
                this.drawDx = this.scroll.getCurrX();
                updateAfterScroll();
                invalidate();
            }
            if (this.scroll.isFinished()) {
                stopScrolling();
            }
        }
    }

    public void setDelegate(GroupedPhotosListViewDelegate groupedPhotosListViewDelegate) {
        this.delegate = groupedPhotosListViewDelegate;
    }

    public boolean hasPhotos() {
        ValueAnimator valueAnimator;
        return this.hasPhotos && this.hideAnimator == null && (this.drawAlpha > 0.0f || !this.animateBackground || ((valueAnimator = this.showAnimator) != null && valueAnimator.isStarted()));
    }

    public void setAnimationsEnabled(boolean z) {
        if (this.animationsEnabled != z) {
            this.animationsEnabled = z;
            if (z) {
                return;
            }
            ValueAnimator valueAnimator = this.showAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.showAnimator = null;
            }
            ValueAnimator valueAnimator2 = this.hideAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.cancel();
                this.hideAnimator = null;
            }
            this.drawAlpha = 0.0f;
            invalidate();
        }
    }

    public void setAnimateBackground(boolean z) {
        this.animateBackground = z;
    }

    public void reset() {
        this.hasPhotos = false;
        if (this.animationsEnabled) {
            this.drawAlpha = 0.0f;
        }
    }
}
