package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class ChatAttachAlertPhotoLayoutPreview extends ChatAttachAlert.AttachAlertLayout {
    private static HashMap<MediaController.PhotoEntry, Boolean> photoRotate = new HashMap<>();
    private ValueAnimator draggingAnimator;
    private PreviewGroupsView groupsView;
    public TextView header;
    private ViewPropertyAnimator headerAnimator;
    private boolean isPortrait;
    private LinearLayoutManager layoutManager;
    public RecyclerListView listView;
    private int paddingTop;
    private ChatAttachAlertPhotoLayout photoLayout;
    private ChatActivity.ThemeDelegate themeDelegate;
    private UndoView undoView;
    private Drawable videoPlayImage;
    private float draggingCellTouchX = 0.0f;
    private float draggingCellTouchY = 0.0f;
    private float draggingCellTop = 0.0f;
    private float draggingCellLeft = 0.0f;
    private float draggingCellFromWidth = 0.0f;
    private float draggingCellFromHeight = 0.0f;
    private PreviewGroupsView.PreviewGroupCell.MediaCell draggingCell = null;
    private boolean draggingCellHiding = false;
    private float draggingCellGroupY = 0.0f;
    private boolean shown = false;
    private boolean ignoreLayout = false;

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int needsActionBar() {
        return 1;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    boolean shouldHideBottomButtons() {
        return true;
    }

    static /* synthetic */ float access$1416(ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview, float f) {
        float f2 = chatAttachAlertPhotoLayoutPreview.draggingCellTouchY + f;
        chatAttachAlertPhotoLayoutPreview.draggingCellTouchY = f2;
        return f2;
    }

    public float getPreviewScale() {
        android.graphics.Point point = AndroidUtilities.displaySize;
        return point.y > point.x ? 0.8f : 0.45f;
    }

    public ChatAttachAlertPhotoLayoutPreview(ChatAttachAlert chatAttachAlert, Context context, ChatActivity.ThemeDelegate themeDelegate) {
        super(chatAttachAlert, context, themeDelegate);
        android.graphics.Point point = AndroidUtilities.displaySize;
        this.isPortrait = point.y > point.x;
        this.themeDelegate = themeDelegate;
        setWillNotDraw(false);
        ActionBarMenu createMenu = this.parentAlert.actionBar.createMenu();
        this.header = new TextView(context);
        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, createMenu, 0, 0, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.1
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem, android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setText(ChatAttachAlertPhotoLayoutPreview.this.header.getText());
            }
        };
        this.parentAlert.actionBar.addView(actionBarMenuItem, 0, LayoutHelper.createFrame(-2, -1.0f, 51, AndroidUtilities.isTablet() ? 64.0f : 56.0f, 0.0f, 40.0f, 0.0f));
        this.header.setImportantForAccessibility(2);
        this.header.setGravity(3);
        this.header.setSingleLine(true);
        this.header.setLines(1);
        this.header.setMaxLines(1);
        this.header.setEllipsize(TextUtils.TruncateAt.END);
        this.header.setTextColor(getThemedColor("dialogTextBlack"));
        this.header.setText(LocaleController.getString("AttachMediaPreview", R.string.AttachMediaPreview));
        this.header.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.header.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
        this.header.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
        this.header.setAlpha(0.0f);
        actionBarMenuItem.addView(this.header, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.2
            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i, int i2) {
                ChatAttachAlertPhotoLayoutPreview.this.invalidate();
                ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = ChatAttachAlertPhotoLayoutPreview.this;
                chatAttachAlertPhotoLayoutPreview.parentAlert.updateLayout(chatAttachAlertPhotoLayoutPreview, true, i2);
                ChatAttachAlertPhotoLayoutPreview.this.groupsView.onScroll();
                super.onScrolled(i, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                    return false;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setAdapter(new RecyclerView.Adapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.3
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return 1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new RecyclerListView.Holder(ChatAttachAlertPhotoLayoutPreview.this.groupsView);
            }
        });
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.listView.setClipChildren(false);
        this.listView.setClipToPadding(false);
        this.listView.setOverScrollMode(2);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(46.0f));
        PreviewGroupsView previewGroupsView = new PreviewGroupsView(context);
        this.groupsView = previewGroupsView;
        previewGroupsView.setClipToPadding(true);
        this.groupsView.setClipChildren(true);
        addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.photoLayout = this.parentAlert.getPhotoLayout();
        this.groupsView.deletedPhotos.clear();
        this.groupsView.fromPhotoLayout(this.photoLayout);
        UndoView undoView = new UndoView(context, null, false, this.parentAlert.parentThemeDelegate);
        this.undoView = undoView;
        undoView.setEnterOffsetMargin(AndroidUtilities.dp(32.0f));
        addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 52.0f));
        this.videoPlayImage = context.getResources().getDrawable(R.drawable.play_mini_video);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShow(final ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.shown = true;
        if (attachAlertLayout instanceof ChatAttachAlertPhotoLayout) {
            this.photoLayout = (ChatAttachAlertPhotoLayout) attachAlertLayout;
            this.groupsView.deletedPhotos.clear();
            this.groupsView.fromPhotoLayout(this.photoLayout);
            this.groupsView.requestLayout();
            this.layoutManager.scrollToPositionWithOffset(0, 0);
            this.listView.post(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertPhotoLayoutPreview.this.lambda$onShow$0(attachAlertLayout);
                }
            });
            postDelayed(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertPhotoLayoutPreview.this.lambda$onShow$1();
                }
            }, 250L);
            this.groupsView.toPhotoLayout(this.photoLayout, false);
        } else {
            scrollToTop();
        }
        ViewPropertyAnimator viewPropertyAnimator = this.headerAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        ViewPropertyAnimator interpolator = this.header.animate().alpha(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.headerAnimator = interpolator;
        interpolator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$0(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        int currentItemTop = attachAlertLayout.getCurrentItemTop();
        int listTopPadding = attachAlertLayout.getListTopPadding();
        RecyclerListView recyclerListView = this.listView;
        if (currentItemTop > AndroidUtilities.dp(7.0f)) {
            listTopPadding -= currentItemTop;
        }
        recyclerListView.scrollBy(0, listTopPadding);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$1() {
        if (this.shown) {
            this.parentAlert.selectedMenuItem.hideSubItem(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHide() {
        this.shown = false;
        ViewPropertyAnimator viewPropertyAnimator = this.headerAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        ViewPropertyAnimator interpolator = this.header.animate().alpha(0.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.EASE_BOTH);
        this.headerAnimator = interpolator;
        interpolator.start();
        if (getSelectedItemsCount() > 1) {
            this.parentAlert.selectedMenuItem.showSubItem(3);
        }
        this.groupsView.toPhotoLayout(this.photoLayout, true);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getSelectedItemsCount() {
        return this.groupsView.getPhotosCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        this.draggingCell = null;
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(false, 0);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void applyCaption(CharSequence charSequence) {
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout != null) {
            chatAttachAlertPhotoLayout.applyCaption(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class GroupCalculator {
        float height;
        int maxX;
        int maxY;
        ArrayList<MediaController.PhotoEntry> photos;
        public ArrayList<MessageObject.GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<MediaController.PhotoEntry, MessageObject.GroupedMessagePosition> positions = new HashMap<>();
        int width;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(GroupCalculator groupCalculator, int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(GroupCalculator groupCalculator, int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(GroupCalculator groupCalculator, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return 1000.0f / f;
        }

        public GroupCalculator(ArrayList<MediaController.PhotoEntry> arrayList) {
            this.photos = arrayList;
            calculate();
        }

        /* JADX WARN: Code restructure failed: missing block: B:55:0x00b1, code lost:
            if (r1 != 8) goto L50;
         */
        /* JADX WARN: Removed duplicated region for block: B:68:0x079a  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x07dd A[LOOP:2: B:80:0x07db->B:81:0x07dd, LOOP_END] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            int i2;
            float f;
            int i3;
            int i4;
            int i5;
            float f2;
            int i6;
            int i7;
            int i8;
            int i9;
            float[] fArr;
            int i10;
            ArrayList arrayList;
            float f3;
            int i11;
            int i12;
            int i13;
            boolean z;
            boolean z2;
            int size = this.photos.size();
            this.posArray.clear();
            this.positions.clear();
            if (size == 0) {
                this.width = 0;
                this.height = 0.0f;
                this.maxX = 0;
                this.maxY = 0;
                return;
            }
            this.posArray.ensureCapacity(size);
            char[] cArr = new char[size];
            int i14 = 0;
            float f4 = 1.0f;
            boolean z3 = false;
            while (i14 < size) {
                MediaController.PhotoEntry photoEntry = this.photos.get(i14);
                MessageObject.GroupedMessagePosition groupedMessagePosition = new MessageObject.GroupedMessagePosition();
                groupedMessagePosition.last = i14 == size + (-1);
                MediaController.CropState cropState = photoEntry.cropState;
                int i15 = cropState != null ? cropState.width : photoEntry.width;
                int i16 = cropState != null ? cropState.height : photoEntry.height;
                if (ChatAttachAlertPhotoLayoutPreview.photoRotate.containsKey(photoEntry)) {
                    z = ((Boolean) ChatAttachAlertPhotoLayoutPreview.photoRotate.get(photoEntry)).booleanValue();
                } else {
                    try {
                        if (photoEntry.isVideo) {
                            if (Build.VERSION.SDK_INT >= 17) {
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(photoEntry.path);
                                String extractMetadata = mediaMetadataRetriever.extractMetadata(24);
                                if (extractMetadata != null) {
                                    if (!extractMetadata.equals("90")) {
                                        if (extractMetadata.equals("270")) {
                                        }
                                    }
                                    z2 = true;
                                }
                            }
                            z2 = false;
                        } else {
                            int attributeInt = new ExifInterface(photoEntry.path).getAttributeInt("Orientation", 1);
                            if (attributeInt != 6) {
                            }
                            z2 = true;
                        }
                        z = z2;
                    } catch (Exception unused) {
                        z = false;
                    }
                    ChatAttachAlertPhotoLayoutPreview.photoRotate.put(photoEntry, Boolean.valueOf(z));
                }
                if (z) {
                    int i17 = i16;
                    i16 = i15;
                    i15 = i17;
                }
                float f5 = i15 / i16;
                groupedMessagePosition.aspectRatio = f5;
                cArr[i14] = f5 > 1.2f ? 'w' : f5 < 0.8f ? 'n' : 'q';
                f4 += f5;
                if (f5 > 2.0f) {
                    z3 = true;
                }
                this.positions.put(photoEntry, groupedMessagePosition);
                this.posArray.add(groupedMessagePosition);
                i14++;
            }
            String str = new String(cArr);
            int dp = AndroidUtilities.dp(120.0f);
            android.graphics.Point point = AndroidUtilities.displaySize;
            int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / 1000.0f));
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / 1000.0f));
            float f6 = f4 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
            if (size == 1) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(0);
                int backgroundPaddingLeft = AndroidUtilities.displaySize.x - (ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getBackgroundPaddingLeft() * 2);
                android.graphics.Point point3 = AndroidUtilities.displaySize;
                groupedMessagePosition2.set(0, 0, 0, 0, 800, ((backgroundPaddingLeft * 0.8f) / groupedMessagePosition2.aspectRatio) / (Math.max(point3.x, point3.y) * 0.5f), 15);
            } else if (z3 || !(size == 2 || size == 3 || size == 4)) {
                int size2 = this.posArray.size();
                float[] fArr2 = new float[size2];
                for (int i18 = 0; i18 < size; i18++) {
                    if (f6 > 1.1f) {
                        fArr2[i18] = Math.max(1.0f, this.posArray.get(i18).aspectRatio);
                    } else {
                        fArr2[i18] = Math.min(1.0f, this.posArray.get(i18).aspectRatio);
                    }
                    fArr2[i18] = Math.max(0.66667f, Math.min(1.7f, fArr2[i18]));
                }
                ArrayList arrayList2 = new ArrayList();
                int i19 = 1;
                while (i19 < size2) {
                    int i20 = size2 - i19;
                    if (i19 > 3 || i20 > 3) {
                        i10 = i19;
                        arrayList = arrayList2;
                    } else {
                        i10 = i19;
                        arrayList = arrayList2;
                        arrayList.add(new MessageGroupedLayoutAttempt(this, i19, i20, multiHeight(fArr2, 0, i19), multiHeight(fArr2, i19, size2)));
                    }
                    i19 = i10 + 1;
                    arrayList2 = arrayList;
                }
                ArrayList arrayList3 = arrayList2;
                int i21 = 1;
                while (i21 < size2 - 1) {
                    int i22 = 1;
                    while (true) {
                        int i23 = size2 - i21;
                        if (i22 < i23) {
                            int i24 = i23 - i22;
                            if (i21 <= 3) {
                                if (i22 <= (f6 < 0.85f ? 4 : 3) && i24 <= 3) {
                                    int i25 = i21 + i22;
                                    i7 = i22;
                                    i9 = size;
                                    fArr = fArr2;
                                    i8 = i21;
                                    arrayList3.add(new MessageGroupedLayoutAttempt(this, i21, i22, i24, multiHeight(fArr2, 0, i21), multiHeight(fArr2, i21, i25), multiHeight(fArr2, i25, size2)));
                                    i22 = i7 + 1;
                                    fArr2 = fArr;
                                    size = i9;
                                    i21 = i8;
                                }
                            }
                            i7 = i22;
                            i8 = i21;
                            i9 = size;
                            fArr = fArr2;
                            i22 = i7 + 1;
                            fArr2 = fArr;
                            size = i9;
                            i21 = i8;
                        }
                    }
                    i21++;
                    size = size;
                }
                i = size;
                float[] fArr3 = fArr2;
                for (int i26 = 1; i26 < size2 - 2; i26++) {
                    int i27 = 1;
                    while (true) {
                        int i28 = size2 - i26;
                        if (i27 < i28) {
                            int i29 = 1;
                            while (true) {
                                int i30 = i28 - i27;
                                if (i29 < i30) {
                                    int i31 = i30 - i29;
                                    if (i26 > 3 || i27 > 3 || i29 > 3 || i31 > 3) {
                                        i3 = i29;
                                        i4 = i28;
                                        i5 = i27;
                                        f2 = dp4;
                                        i6 = size2;
                                    } else {
                                        int i32 = i26 + i27;
                                        int i33 = i32 + i29;
                                        i6 = size2;
                                        i3 = i29;
                                        i4 = i28;
                                        i5 = i27;
                                        f2 = dp4;
                                        arrayList3.add(new MessageGroupedLayoutAttempt(this, i26, i27, i29, i31, multiHeight(fArr3, 0, i26), multiHeight(fArr3, i26, i32), multiHeight(fArr3, i32, i33), multiHeight(fArr3, i33, size2)));
                                    }
                                    i29 = i3 + 1;
                                    i28 = i4;
                                    i27 = i5;
                                    size2 = i6;
                                    dp4 = f2;
                                }
                            }
                            i27++;
                        }
                    }
                }
                float f7 = dp4;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                float f8 = 0.0f;
                for (int i34 = 0; i34 < arrayList3.size(); i34++) {
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList3.get(i34);
                    int i35 = 0;
                    float f9 = Float.MAX_VALUE;
                    float f10 = 0.0f;
                    while (true) {
                        float[] fArr4 = messageGroupedLayoutAttempt2.heights;
                        if (i35 >= fArr4.length) {
                            break;
                        }
                        f10 += fArr4[i35];
                        if (fArr4[i35] < f9) {
                            f9 = fArr4[i35];
                        }
                        i35++;
                    }
                    float abs = Math.abs(f10 - 1332.0f);
                    int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                    if (iArr.length > 1) {
                        if (iArr[0] <= iArr[1]) {
                            if (iArr.length > 2 && iArr[1] > iArr[2]) {
                                f = 1.2f;
                                abs *= f;
                            } else if (iArr.length <= 3 || iArr[2] <= iArr[3]) {
                            }
                        }
                        f = 1.2f;
                        abs *= f;
                    }
                    if (f9 < dp2) {
                        abs *= 1.5f;
                    }
                    if (messageGroupedLayoutAttempt == null || abs < f8) {
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                        f8 = abs;
                    }
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int i36 = 0;
                int i37 = 0;
                while (true) {
                    int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                    if (i36 >= iArr2.length) {
                        break;
                    }
                    int i38 = iArr2[i36];
                    float f11 = messageGroupedLayoutAttempt.heights[i36];
                    MessageObject.GroupedMessagePosition groupedMessagePosition3 = null;
                    int i39 = 1000;
                    int i40 = i37;
                    for (int i41 = 0; i41 < i38; i41++) {
                        int i42 = (int) (fArr3[i40] * f11);
                        i39 -= i42;
                        MessageObject.GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(i40);
                        int i43 = i36 == 0 ? 4 : 0;
                        if (i36 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            i43 |= 8;
                        }
                        if (i41 == 0) {
                            i43 |= 1;
                            groupedMessagePosition3 = groupedMessagePosition4;
                        }
                        if (i41 == i38 - 1) {
                            i2 = i43 | 2;
                            groupedMessagePosition3 = groupedMessagePosition4;
                        } else {
                            i2 = i43;
                        }
                        groupedMessagePosition4.set(i41, i41, i36, i36, i42, Math.max(f7, f11 / 814.0f), i2);
                        i40++;
                    }
                    float f12 = f7;
                    if (groupedMessagePosition3 != null) {
                        groupedMessagePosition3.pw += i39;
                        groupedMessagePosition3.spanSize += i39;
                    }
                    i36++;
                    i37 = i40;
                    f7 = f12;
                }
                i11 = i;
                for (i12 = 0; i12 < i11; i12++) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(i12);
                    if (groupedMessagePosition5.minX == 0) {
                        groupedMessagePosition5.spanSize += 200;
                    }
                    if ((groupedMessagePosition5.flags & 2) != 0) {
                        groupedMessagePosition5.edge = true;
                    }
                    this.maxX = Math.max(this.maxX, (int) groupedMessagePosition5.maxX);
                    this.maxY = Math.max(this.maxY, (int) groupedMessagePosition5.maxY);
                    groupedMessagePosition5.left = getLeft(groupedMessagePosition5, groupedMessagePosition5.minY, groupedMessagePosition5.maxY, groupedMessagePosition5.minX);
                }
                for (i13 = 0; i13 < i11; i13++) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(i13);
                    groupedMessagePosition6.top = getTop(groupedMessagePosition6, groupedMessagePosition6.minY);
                }
                this.width = getWidth();
                this.height = getHeight();
            } else if (size == 2) {
                MessageObject.GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                if (str.equals("ww")) {
                    double d = 1.2285012f;
                    Double.isNaN(d);
                    if (f6 > d * 1.4d) {
                        float f13 = groupedMessagePosition7.aspectRatio;
                        float f14 = groupedMessagePosition8.aspectRatio;
                        if (f13 - f14 < 0.2d) {
                            float round = Math.round(Math.min(1000.0f / f13, Math.min(1000.0f / f14, 407.0f))) / 814.0f;
                            groupedMessagePosition7.set(0, 0, 0, 0, 1000, round, 7);
                            groupedMessagePosition8.set(0, 0, 1, 1, 1000, round, 11);
                        }
                    }
                }
                if (str.equals("ww") || str.equals("qq")) {
                    float f15 = 500;
                    float round2 = Math.round(Math.min(f15 / groupedMessagePosition7.aspectRatio, Math.min(f15 / groupedMessagePosition8.aspectRatio, 814.0f))) / 814.0f;
                    groupedMessagePosition7.set(0, 0, 0, 0, 500, round2, 13);
                    groupedMessagePosition8.set(1, 1, 0, 0, 500, round2, 14);
                } else {
                    float f16 = groupedMessagePosition7.aspectRatio;
                    int max = (int) Math.max(400.0f, Math.round((1000.0f / f16) / ((1.0f / f16) + (1.0f / groupedMessagePosition8.aspectRatio))));
                    int i44 = 1000 - max;
                    if (i44 < dp2) {
                        max -= dp2 - i44;
                        i44 = dp2;
                    }
                    float min = Math.min(814.0f, Math.round(Math.min(i44 / groupedMessagePosition7.aspectRatio, max / groupedMessagePosition8.aspectRatio))) / 814.0f;
                    groupedMessagePosition7.set(0, 0, 0, 0, i44, min, 13);
                    groupedMessagePosition8.set(1, 1, 0, 0, max, min, 14);
                }
            } else if (size == 3) {
                MessageObject.GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(1);
                MessageObject.GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(2);
                if (str.charAt(0) == 'n') {
                    float f17 = groupedMessagePosition10.aspectRatio;
                    float min2 = Math.min(407.0f, Math.round((1000.0f * f17) / (groupedMessagePosition11.aspectRatio + f17)));
                    int max2 = (int) Math.max(dp2, Math.min(500.0f, Math.round(Math.min(groupedMessagePosition11.aspectRatio * min2, groupedMessagePosition10.aspectRatio * f3))));
                    float f18 = (groupedMessagePosition9.aspectRatio * 814.0f) + dp3;
                    int i45 = 1000 - max2;
                    groupedMessagePosition9.set(0, 0, 0, 1, Math.round(Math.min(f18, i45)), 1.0f, 13);
                    float f19 = (814.0f - min2) / 814.0f;
                    groupedMessagePosition10.set(1, 1, 0, 0, max2, f19, 6);
                    float f20 = min2 / 814.0f;
                    groupedMessagePosition11.set(1, 1, 1, 1, max2, f20, 10);
                    groupedMessagePosition11.spanSize = 1000;
                    groupedMessagePosition9.siblingHeights = new float[]{f20, f19};
                    groupedMessagePosition9.spanSize = i45;
                } else {
                    float round3 = Math.round(Math.min(1000.0f / groupedMessagePosition9.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition9.set(0, 1, 0, 0, 1000, round3, 7);
                    float f21 = 500;
                    float min3 = Math.min(814.0f - round3, Math.round(Math.min(f21 / groupedMessagePosition10.aspectRatio, f21 / groupedMessagePosition11.aspectRatio))) / 814.0f;
                    if (min3 < dp4) {
                        min3 = dp4;
                    }
                    float f22 = min3;
                    groupedMessagePosition10.set(0, 0, 1, 1, 500, f22, 9);
                    groupedMessagePosition11.set(1, 1, 1, 1, 500, f22, 10);
                }
            } else {
                MessageObject.GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(1);
                MessageObject.GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(2);
                MessageObject.GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(3);
                if (str.charAt(0) == 'w') {
                    float round4 = Math.round(Math.min(1000.0f / groupedMessagePosition12.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition12.set(0, 2, 0, 0, 1000, round4, 7);
                    float round5 = Math.round(1000.0f / ((groupedMessagePosition13.aspectRatio + groupedMessagePosition14.aspectRatio) + groupedMessagePosition15.aspectRatio));
                    float f23 = dp2;
                    int max3 = (int) Math.max(f23, Math.min(400.0f, groupedMessagePosition13.aspectRatio * round5));
                    int max4 = (int) Math.max(Math.max(f23, 330.0f), groupedMessagePosition15.aspectRatio * round5);
                    int i46 = (1000 - max3) - max4;
                    if (i46 < AndroidUtilities.dp(58.0f)) {
                        int dp5 = AndroidUtilities.dp(58.0f) - i46;
                        i46 = AndroidUtilities.dp(58.0f);
                        int i47 = dp5 / 2;
                        max3 -= i47;
                        max4 -= dp5 - i47;
                    }
                    int i48 = max3;
                    float min4 = Math.min(814.0f - round4, round5) / 814.0f;
                    if (min4 < dp4) {
                        min4 = dp4;
                    }
                    float f24 = min4;
                    groupedMessagePosition13.set(0, 0, 1, 1, i48, f24, 9);
                    groupedMessagePosition14.set(1, 1, 1, 1, i46, f24, 8);
                    groupedMessagePosition15.set(2, 2, 1, 1, max4, f24, 10);
                } else {
                    int max5 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition13.aspectRatio) + (1.0f / groupedMessagePosition14.aspectRatio)) + (1.0f / groupedMessagePosition15.aspectRatio))));
                    float f25 = dp;
                    float f26 = max5;
                    float min5 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition13.aspectRatio) / 814.0f);
                    float min6 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition14.aspectRatio) / 814.0f);
                    float f27 = (1.0f - min5) - min6;
                    float f28 = (groupedMessagePosition12.aspectRatio * 814.0f) + dp3;
                    int i49 = 1000 - max5;
                    groupedMessagePosition12.set(0, 0, 0, 2, Math.round(Math.min(f28, i49)), min5 + min6 + f27, 13);
                    groupedMessagePosition13.set(1, 1, 0, 0, max5, min5, 6);
                    groupedMessagePosition14.set(1, 1, 1, 1, max5, min6, 2);
                    groupedMessagePosition14.spanSize = 1000;
                    groupedMessagePosition15.set(1, 1, 2, 2, max5, f27, 10);
                    groupedMessagePosition15.spanSize = 1000;
                    groupedMessagePosition12.spanSize = i49;
                    groupedMessagePosition12.siblingHeights = new float[]{min5, min6, f27};
                }
            }
            i = size;
            i11 = i;
            while (i12 < i11) {
            }
            while (i13 < i11) {
            }
            this.width = getWidth();
            this.height = getHeight();
        }

        public int getWidth() {
            int[] iArr = new int[10];
            Arrays.fill(iArr, 0);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = this.posArray.get(i);
                int i2 = groupedMessagePosition.pw;
                for (int i3 = groupedMessagePosition.minY; i3 <= groupedMessagePosition.maxY; i3++) {
                    iArr[i3] = iArr[i3] + i2;
                }
            }
            int i4 = iArr[0];
            for (int i5 = 1; i5 < 10; i5++) {
                if (i4 < iArr[i5]) {
                    i4 = iArr[i5];
                }
            }
            return i4;
        }

        public float getHeight() {
            float[] fArr = new float[10];
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = this.posArray.get(i);
                float f = groupedMessagePosition.ph;
                for (int i2 = groupedMessagePosition.minX; i2 <= groupedMessagePosition.maxX; i2++) {
                    fArr[i2] = fArr[i2] + f;
                }
            }
            float f2 = fArr[0];
            for (int i3 = 1; i3 < 10; i3++) {
                if (f2 < fArr[i3]) {
                    f2 = fArr[i3];
                }
            }
            return f2;
        }

        private float getLeft(MessageObject.GroupedMessagePosition groupedMessagePosition, int i, int i2, int i3) {
            int i4 = (i2 - i) + 1;
            float[] fArr = new float[i4];
            float f = 0.0f;
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i5 = 0; i5 < size; i5++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i5);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxX < i3) {
                    int min = Math.min((int) groupedMessagePosition2.maxY, i2) - i;
                    for (int max = Math.max(groupedMessagePosition2.minY - i, 0); max <= min; max++) {
                        fArr[max] = fArr[max] + groupedMessagePosition2.pw;
                    }
                }
            }
            for (int i6 = 0; i6 < i4; i6++) {
                if (f < fArr[i6]) {
                    f = fArr[i6];
                }
            }
            return f;
        }

        private float getTop(MessageObject.GroupedMessagePosition groupedMessagePosition, int i) {
            int i2 = this.maxX + 1;
            float[] fArr = new float[i2];
            float f = 0.0f;
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i3);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxY < i) {
                    for (int i4 = groupedMessagePosition2.minX; i4 <= groupedMessagePosition2.maxX; i4++) {
                        fArr[i4] = fArr[i4] + groupedMessagePosition2.ph;
                    }
                }
            }
            for (int i5 = 0; i5 < i2; i5++) {
                if (f < fArr[i5]) {
                    f = fArr[i5];
                }
            }
            return f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            recyclerListView.setTopGlowOffset(recyclerListView.getPaddingTop());
            return Integer.MAX_VALUE;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(8.0f);
        if (top < AndroidUtilities.dp(8.0f) || holder == null || holder.getAdapterPosition() != 0) {
            top = dp;
        }
        this.listView.setTopGlowOffset(top);
        return top;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0039  */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPreMeasure(int i, int i2) {
        int dp;
        float f;
        this.ignoreLayout = true;
        ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
        if (!AndroidUtilities.isTablet()) {
            android.graphics.Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                this.paddingTop = (int) (i2 / 3.5f);
                dp = this.paddingTop - AndroidUtilities.dp(52.0f);
                this.paddingTop = dp;
                if (dp < 0) {
                    this.paddingTop = 0;
                }
                if (this.listView.getPaddingTop() != this.paddingTop) {
                    RecyclerListView recyclerListView = this.listView;
                    recyclerListView.setPadding(recyclerListView.getPaddingLeft(), this.paddingTop, this.listView.getPaddingRight(), this.listView.getPaddingBottom());
                    invalidate();
                }
                TextView textView = this.header;
                if (!AndroidUtilities.isTablet()) {
                    android.graphics.Point point2 = AndroidUtilities.displaySize;
                    if (point2.x > point2.y) {
                        f = 18.0f;
                        textView.setTextSize(f);
                        this.ignoreLayout = false;
                    }
                }
                f = 20.0f;
                textView.setTextSize(f);
                this.ignoreLayout = false;
            }
        }
        this.paddingTop = (i2 / 5) * 2;
        dp = this.paddingTop - AndroidUtilities.dp(52.0f);
        this.paddingTop = dp;
        if (dp < 0) {
        }
        if (this.listView.getPaddingTop() != this.paddingTop) {
        }
        TextView textView2 = this.header;
        if (!AndroidUtilities.isTablet()) {
        }
        f = 20.0f;
        textView2.setTextSize(f);
        this.ignoreLayout = false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void scrollToTop() {
        this.listView.smoothScrollToPosition(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        this.parentAlert.updatePhotoPreview(false);
        return true;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onMenuItemClick(int i) {
        try {
            this.parentAlert.getPhotoLayout().onMenuItemClick(i);
        } catch (Exception unused) {
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Drawable wallpaperDrawable;
        int i;
        ChatActivity.ThemeDelegate themeDelegate = this.parentAlert.parentThemeDelegate;
        boolean z = false;
        if (themeDelegate != null && (wallpaperDrawable = themeDelegate.getWallpaperDrawable()) != null) {
            int currentItemTop = getCurrentItemTop();
            if (AndroidUtilities.isTablet()) {
                i = 16;
            } else {
                android.graphics.Point point = AndroidUtilities.displaySize;
                i = point.x > point.y ? 6 : 12;
            }
            if (currentItemTop < ActionBar.getCurrentActionBarHeight()) {
                currentItemTop -= AndroidUtilities.dp((1.0f - (currentItemTop / ActionBar.getCurrentActionBarHeight())) * i);
            }
            int max = Math.max(0, currentItemTop);
            canvas.save();
            canvas.clipRect(0, max, getWidth(), getHeight());
            wallpaperDrawable.setBounds(0, max, getWidth(), AndroidUtilities.displaySize.y + max);
            wallpaperDrawable.draw(canvas);
            z = true;
        }
        super.dispatchDraw(canvas);
        if (z) {
            canvas.restore();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        android.graphics.Point point = AndroidUtilities.displaySize;
        boolean z2 = point.y > point.x;
        if (this.isPortrait != z2) {
            this.isPortrait = z2;
            int size = this.groupsView.groupCells.size();
            for (int i5 = 0; i5 < size; i5++) {
                PreviewGroupsView.PreviewGroupCell previewGroupCell = (PreviewGroupsView.PreviewGroupCell) this.groupsView.groupCells.get(i5);
                if (previewGroupCell.group.photos.size() == 1) {
                    previewGroupCell.setGroup(previewGroupCell.group, true);
                }
            }
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onSelectedItemsCountChanged(int i) {
        if (i > 1) {
            this.parentAlert.selectedMenuItem.showSubItem(0);
        } else {
            this.parentAlert.selectedMenuItem.hideSubItem(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PreviewGroupsView extends ViewGroup {
        private ChatActionCell hintView;
        HashMap<Object, Object> photosMap;
        List<Map.Entry<Object, Object>> photosMapKeys;
        ArrayList<Object> photosOrder;
        private float savedDragFromX;
        private float savedDragFromY;
        private float savedDraggingT;
        HashMap<Object, Object> selectedPhotos;
        float viewBottom;
        float viewTop;
        private ArrayList<PreviewGroupCell> groupCells = new ArrayList<>();
        private HashMap<Object, Object> deletedPhotos = new HashMap<>();
        private int paddingTop = AndroidUtilities.dp(16.0f);
        private int paddingBottom = AndroidUtilities.dp(64.0f);
        private int lastMeasuredHeight = 0;
        boolean[] lastGroupSeen = null;
        long tapTime = 0;
        PreviewGroupCell tapGroupCell = null;
        PreviewGroupCell.MediaCell tapMediaCell = null;
        private float draggingT = 0.0f;
        private final Point tmpPoint = new Point();
        private boolean scrollerStarted = false;
        private final Runnable scroller = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.2
            @Override // java.lang.Runnable
            public void run() {
                float dp;
                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null || ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                    return;
                }
                int computeVerticalScrollOffset = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollOffset();
                boolean z = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollExtent() + computeVerticalScrollOffset >= (PreviewGroupsView.this.measurePureHeight() - PreviewGroupsView.this.paddingBottom) + PreviewGroupsView.this.paddingTop;
                float max = Math.max(0.0f, (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - Math.max(0, computeVerticalScrollOffset - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding())) - AndroidUtilities.dp(52.0f));
                float max2 = Math.max(0.0f, ((ChatAttachAlertPhotoLayoutPreview.this.listView.getMeasuredHeight() - (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - computeVerticalScrollOffset)) - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) - AndroidUtilities.dp(84.0f));
                float dp2 = AndroidUtilities.dp(32.0f);
                if (max < dp2 && computeVerticalScrollOffset > ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) {
                    dp = (-(1.0f - (max / dp2))) * AndroidUtilities.dp(6.0f);
                } else {
                    dp = max2 < dp2 ? AndroidUtilities.dp(6.0f) * (1.0f - (max2 / dp2)) : 0.0f;
                }
                int i = (int) dp;
                if (Math.abs(i) > 0 && ChatAttachAlertPhotoLayoutPreview.this.listView.canScrollVertically(i) && (dp <= 0.0f || !z)) {
                    ChatAttachAlertPhotoLayoutPreview.access$1416(ChatAttachAlertPhotoLayoutPreview.this, dp);
                    ChatAttachAlertPhotoLayoutPreview.this.listView.scrollBy(0, i);
                    PreviewGroupsView.this.invalidate();
                }
                PreviewGroupsView.this.scrollerStarted = true;
                PreviewGroupsView.this.postDelayed(this, 15L);
            }
        };
        GroupingPhotoViewerProvider photoViewerProvider = new GroupingPhotoViewerProvider();
        private int undoViewId = 0;

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return false;
        }

        public PreviewGroupsView(Context context) {
            super(context);
            new HashMap();
            setWillNotDraw(false);
            ChatActionCell chatActionCell = new ChatActionCell(context, true, ChatAttachAlertPhotoLayoutPreview.this.themeDelegate);
            this.hintView = chatActionCell;
            chatActionCell.setCustomText(LocaleController.getString("AttachMediaDragHint", R.string.AttachMediaDragHint));
            addView(this.hintView);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            ChatActionCell chatActionCell = this.hintView;
            chatActionCell.layout(0, 0, chatActionCell.getMeasuredWidth(), this.hintView.getMeasuredHeight());
        }

        public void saveDeletedImageId(MediaController.PhotoEntry photoEntry) {
            if (ChatAttachAlertPhotoLayoutPreview.this.photoLayout == null) {
                return;
            }
            ArrayList arrayList = new ArrayList(ChatAttachAlertPhotoLayoutPreview.this.photoLayout.getSelectedPhotos().entrySet());
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (((Map.Entry) arrayList.get(i)).getValue() == photoEntry) {
                    this.deletedPhotos.put(photoEntry, ((Map.Entry) arrayList.get(i)).getKey());
                    return;
                }
            }
        }

        public void fromPhotoLayout(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout) {
            this.photosOrder = chatAttachAlertPhotoLayout.getSelectedPhotosOrder();
            this.photosMap = chatAttachAlertPhotoLayout.getSelectedPhotos();
            fromPhotoArrays();
        }

        public void fromPhotoArrays() {
            this.groupCells.clear();
            ArrayList arrayList = new ArrayList();
            int size = this.photosOrder.size();
            int i = size - 1;
            for (int i2 = 0; i2 < size; i2++) {
                arrayList.add((MediaController.PhotoEntry) this.photosMap.get(Integer.valueOf(((Integer) this.photosOrder.get(i2)).intValue())));
                if (i2 % 10 == 9 || i2 == i) {
                    PreviewGroupCell previewGroupCell = new PreviewGroupCell();
                    previewGroupCell.setGroup(new GroupCalculator(arrayList), false);
                    this.groupCells.add(previewGroupCell);
                    arrayList = new ArrayList();
                }
            }
        }

        public void calcPhotoArrays() {
            boolean z;
            String str;
            this.photosMap = ChatAttachAlertPhotoLayoutPreview.this.photoLayout.getSelectedPhotos();
            this.photosMapKeys = new ArrayList(this.photosMap.entrySet());
            this.selectedPhotos = new HashMap<>();
            this.photosOrder = new ArrayList<>();
            int size = this.groupCells.size();
            for (int i = 0; i < size; i++) {
                GroupCalculator groupCalculator = this.groupCells.get(i).group;
                if (groupCalculator.photos.size() != 0) {
                    int size2 = groupCalculator.photos.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        MediaController.PhotoEntry photoEntry = groupCalculator.photos.get(i2);
                        if (this.deletedPhotos.containsKey(photoEntry)) {
                            Object obj = this.deletedPhotos.get(photoEntry);
                            this.selectedPhotos.put(obj, photoEntry);
                            this.photosOrder.add(obj);
                        } else {
                            int i3 = 0;
                            while (true) {
                                if (i3 >= this.photosMapKeys.size()) {
                                    z = false;
                                    break;
                                }
                                Map.Entry<Object, Object> entry = this.photosMapKeys.get(i3);
                                Object value = entry.getValue();
                                if (value == photoEntry) {
                                    Object key = entry.getKey();
                                    this.selectedPhotos.put(key, value);
                                    this.photosOrder.add(key);
                                    z = true;
                                    break;
                                }
                                i3++;
                            }
                            if (!z) {
                                int i4 = 0;
                                while (true) {
                                    if (i4 < this.photosMapKeys.size()) {
                                        Map.Entry<Object, Object> entry2 = this.photosMapKeys.get(i4);
                                        Object value2 = entry2.getValue();
                                        if ((value2 instanceof MediaController.PhotoEntry) && (str = ((MediaController.PhotoEntry) value2).path) != null && photoEntry != null && str.equals(photoEntry.path)) {
                                            Object key2 = entry2.getKey();
                                            this.selectedPhotos.put(key2, value2);
                                            this.photosOrder.add(key2);
                                            break;
                                        }
                                        i4++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void toPhotoLayout(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout, boolean z) {
            int size = chatAttachAlertPhotoLayout.getSelectedPhotosOrder().size();
            calcPhotoArrays();
            chatAttachAlertPhotoLayout.updateSelected(this.selectedPhotos, this.photosOrder, z);
            if (size != this.photosOrder.size()) {
                ChatAttachAlertPhotoLayoutPreview.this.parentAlert.updateCountButton(1);
            }
        }

        public int getPhotosCount() {
            int size = this.groupCells.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                PreviewGroupCell previewGroupCell = this.groupCells.get(i2);
                if (previewGroupCell != null && previewGroupCell.group != null && previewGroupCell.group.photos != null) {
                    i += previewGroupCell.group.photos.size();
                }
            }
            return i;
        }

        public ArrayList<MediaController.PhotoEntry> getPhotos() {
            ArrayList<MediaController.PhotoEntry> arrayList = new ArrayList<>();
            int size = this.groupCells.size();
            for (int i = 0; i < size; i++) {
                PreviewGroupCell previewGroupCell = this.groupCells.get(i);
                if (previewGroupCell != null && previewGroupCell.group != null && previewGroupCell.group.photos != null) {
                    arrayList.addAll(previewGroupCell.group.photos);
                }
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int measurePureHeight() {
            int i = this.paddingTop + this.paddingBottom;
            int size = this.groupCells.size();
            for (int i2 = 0; i2 < size; i2++) {
                i = (int) (i + this.groupCells.get(i2).measure());
            }
            if (this.hintView.getMeasuredHeight() <= 0) {
                this.hintView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), View.MeasureSpec.makeMeasureSpec(9999, Integer.MIN_VALUE));
            }
            return i + this.hintView.getMeasuredHeight();
        }

        private int measureHeight() {
            return Math.max(measurePureHeight(), (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(45.0f));
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            this.hintView.measure(i, View.MeasureSpec.makeMeasureSpec(9999, Integer.MIN_VALUE));
            if (this.lastMeasuredHeight <= 0) {
                this.lastMeasuredHeight = measureHeight();
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(Math.max(View.MeasureSpec.getSize(i2), this.lastMeasuredHeight), 1073741824));
        }

        @Override // android.view.View
        public void invalidate() {
            int measureHeight = measureHeight();
            if (this.lastMeasuredHeight != measureHeight) {
                this.lastMeasuredHeight = measureHeight;
                requestLayout();
            }
            super.invalidate();
        }

        private boolean[] groupSeen() {
            boolean[] zArr = new boolean[this.groupCells.size()];
            float f = this.paddingTop;
            int computeVerticalScrollOffset = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollOffset();
            int i = 0;
            this.viewTop = Math.max(0, computeVerticalScrollOffset - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding());
            this.viewBottom = (ChatAttachAlertPhotoLayoutPreview.this.listView.getMeasuredHeight() - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) + computeVerticalScrollOffset;
            int size = this.groupCells.size();
            while (i < size) {
                float measure = this.groupCells.get(i).measure() + f;
                zArr[i] = isSeen(f, measure);
                i++;
                f = measure;
            }
            return zArr;
        }

        public boolean isSeen(float f, float f2) {
            float f3 = this.viewTop;
            return (f >= f3 && f <= this.viewBottom) || (f2 >= f3 && f2 <= this.viewBottom) || (f <= f3 && f2 >= this.viewBottom);
        }

        public void onScroll() {
            int i = 0;
            boolean z = true;
            boolean z2 = this.lastGroupSeen == null;
            if (!z2) {
                boolean[] groupSeen = groupSeen();
                if (groupSeen.length == this.lastGroupSeen.length) {
                    while (true) {
                        if (i >= groupSeen.length) {
                            z = z2;
                            break;
                        } else if (groupSeen[i] != this.lastGroupSeen[i]) {
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                z2 = z;
            } else {
                this.lastGroupSeen = groupSeen();
            }
            if (z2) {
                invalidate();
            }
        }

        public void remeasure() {
            float f = this.paddingTop;
            int size = this.groupCells.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                PreviewGroupCell previewGroupCell = this.groupCells.get(i2);
                float measure = previewGroupCell.measure();
                previewGroupCell.y = f;
                previewGroupCell.indexStart = i;
                f += measure;
                i += previewGroupCell.group.photos.size();
            }
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            float f = this.paddingTop;
            int computeVerticalScrollOffset = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollOffset();
            this.viewTop = Math.max(0, computeVerticalScrollOffset - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding());
            this.viewBottom = (ChatAttachAlertPhotoLayoutPreview.this.listView.getMeasuredHeight() - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) + computeVerticalScrollOffset;
            canvas.save();
            canvas.translate(0.0f, this.paddingTop);
            int size = this.groupCells.size();
            int i = 0;
            int i2 = 0;
            while (true) {
                boolean z = true;
                if (i >= size) {
                    break;
                }
                PreviewGroupCell previewGroupCell = this.groupCells.get(i);
                float measure = previewGroupCell.measure();
                previewGroupCell.y = f;
                previewGroupCell.indexStart = i2;
                float f2 = this.viewTop;
                if (f < f2 || f > this.viewBottom) {
                    float f3 = f + measure;
                    if ((f3 < f2 || f3 > this.viewBottom) && (f > f2 || f3 < this.viewBottom)) {
                        z = false;
                    }
                }
                if (z && previewGroupCell.draw(canvas)) {
                    invalidate();
                }
                canvas.translate(0.0f, measure);
                f += measure;
                i2 += previewGroupCell.group.photos.size();
                i++;
            }
            ChatActionCell chatActionCell = this.hintView;
            chatActionCell.setVisiblePart(f, chatActionCell.getMeasuredHeight());
            if (this.hintView.hasGradientService()) {
                this.hintView.drawBackground(canvas, true);
            }
            this.hintView.draw(canvas);
            canvas.restore();
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                canvas.save();
                Point dragTranslate = dragTranslate();
                canvas.translate(dragTranslate.x, dragTranslate.y);
                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell.draw(canvas, true)) {
                    invalidate();
                }
                canvas.restore();
            }
            super.onDraw(canvas);
        }

        Point dragTranslate() {
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                if (!ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                    RectF rect = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                    RectF rect2 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect(1.0f);
                    this.tmpPoint.x = AndroidUtilities.lerp(rect2.left + (rect.width() / 2.0f), ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchX - ((ChatAttachAlertPhotoLayoutPreview.this.draggingCellLeft - 0.5f) * ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromWidth), this.draggingT);
                    this.tmpPoint.y = AndroidUtilities.lerp(ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.y + rect2.top + (rect.height() / 2.0f), (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - ((ChatAttachAlertPhotoLayoutPreview.this.draggingCellTop - 0.5f) * ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromHeight)) + ChatAttachAlertPhotoLayoutPreview.this.draggingCellGroupY, this.draggingT);
                } else {
                    RectF rect3 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                    RectF rect4 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect(1.0f);
                    this.tmpPoint.x = AndroidUtilities.lerp(rect4.left + (rect3.width() / 2.0f), this.savedDragFromX, this.draggingT / this.savedDraggingT);
                    this.tmpPoint.y = AndroidUtilities.lerp(ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.y + rect4.top + (rect3.height() / 2.0f), this.savedDragFromY, this.draggingT / this.savedDraggingT);
                }
                return this.tmpPoint;
            }
            Point point = this.tmpPoint;
            point.x = 0.0f;
            point.y = 0.0f;
            return point;
        }

        void stopDragging() {
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator != null) {
                ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.cancel();
            }
            Point dragTranslate = dragTranslate();
            this.savedDraggingT = this.draggingT;
            this.savedDragFromX = dragTranslate.x;
            this.savedDragFromY = dragTranslate.y;
            ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding = true;
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator = ValueAnimator.ofFloat(this.savedDraggingT, 0.0f);
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$stopDragging$0(valueAnimator);
                }
            });
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCell = null;
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding = false;
                    PreviewGroupsView.this.invalidate();
                }
            });
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.setDuration(200L);
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.start();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$stopDragging$0(ValueAnimator valueAnimator) {
            this.draggingT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        void startDragging(PreviewGroupCell.MediaCell mediaCell) {
            ChatAttachAlertPhotoLayoutPreview.this.draggingCell = mediaCell;
            ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = ChatAttachAlertPhotoLayoutPreview.this;
            chatAttachAlertPhotoLayoutPreview.draggingCellGroupY = chatAttachAlertPhotoLayoutPreview.draggingCell.groupCell.y;
            ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding = false;
            this.draggingT = 0.0f;
            invalidate();
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator != null) {
                ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.cancel();
            }
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$startDragging$1(valueAnimator);
                }
            });
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.setDuration(200L);
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startDragging$1(ValueAnimator valueAnimator) {
            this.draggingT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* loaded from: classes3.dex */
        class GroupingPhotoViewerProvider extends PhotoViewer.EmptyPhotoViewerProvider {
            private ArrayList<MediaController.PhotoEntry> photos = new ArrayList<>();

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean cancelButtonPressed() {
                return false;
            }

            GroupingPhotoViewerProvider() {
            }

            public void init(ArrayList<MediaController.PhotoEntry> arrayList) {
                this.photos = arrayList;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void onClose() {
                PreviewGroupsView.this.fromPhotoArrays();
                PreviewGroupsView previewGroupsView = PreviewGroupsView.this;
                previewGroupsView.toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean isPhotoChecked(int i) {
                if (i < 0 || i >= this.photos.size()) {
                    return false;
                }
                return PreviewGroupsView.this.photosOrder.contains(Integer.valueOf(this.photos.get(i).imageId));
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int setPhotoChecked(int i, VideoEditedInfo videoEditedInfo) {
                if (i < 0 || i >= this.photos.size()) {
                    return -1;
                }
                Integer valueOf = Integer.valueOf(this.photos.get(i).imageId);
                int indexOf = PreviewGroupsView.this.photosOrder.indexOf(valueOf);
                if (indexOf >= 0) {
                    if (PreviewGroupsView.this.photosOrder.size() <= 1) {
                        return -1;
                    }
                    PreviewGroupsView.this.photosOrder.remove(indexOf);
                    PreviewGroupsView.this.fromPhotoArrays();
                    return indexOf;
                }
                PreviewGroupsView.this.photosOrder.add(valueOf);
                PreviewGroupsView.this.fromPhotoArrays();
                return PreviewGroupsView.this.photosOrder.size() - 1;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int setPhotoUnchecked(Object obj) {
                int indexOf;
                Integer valueOf = Integer.valueOf(((MediaController.PhotoEntry) obj).imageId);
                if (PreviewGroupsView.this.photosOrder.size() > 1 && (indexOf = PreviewGroupsView.this.photosOrder.indexOf(valueOf)) >= 0) {
                    PreviewGroupsView.this.photosOrder.remove(indexOf);
                    PreviewGroupsView.this.fromPhotoArrays();
                    return indexOf;
                }
                return -1;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int getSelectedCount() {
                return PreviewGroupsView.this.photosOrder.size();
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public ArrayList<Object> getSelectedPhotosOrder() {
                return PreviewGroupsView.this.photosOrder;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public HashMap<Object, Object> getSelectedPhotos() {
                return PreviewGroupsView.this.photosMap;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int getPhotoIndex(int i) {
                MediaController.PhotoEntry photoEntry;
                if (i < 0 || i >= this.photos.size() || (photoEntry = this.photos.get(i)) == null) {
                    return -1;
                }
                return PreviewGroupsView.this.photosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
                MediaController.PhotoEntry photoEntry;
                ArrayList<PreviewGroupCell.MediaCell> arrayList;
                PhotoViewer.PlaceProviderObject placeProviderObject = null;
                if (i >= 0 && i < this.photos.size() && isPhotoChecked(i) && (photoEntry = this.photos.get(i)) != null) {
                    int size = PreviewGroupsView.this.groupCells.size();
                    PreviewGroupCell previewGroupCell = null;
                    PreviewGroupCell.MediaCell mediaCell = null;
                    for (int i2 = 0; i2 < size; i2++) {
                        previewGroupCell = (PreviewGroupCell) PreviewGroupsView.this.groupCells.get(i2);
                        if (previewGroupCell != null && (arrayList = previewGroupCell.media) != null) {
                            int size2 = arrayList.size();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= size2) {
                                    break;
                                }
                                PreviewGroupCell.MediaCell mediaCell2 = previewGroupCell.media.get(i3);
                                if (mediaCell2 != null && mediaCell2.photoEntry == photoEntry && mediaCell2.scale > 0.5d) {
                                    mediaCell = previewGroupCell.media.get(i3);
                                    break;
                                }
                                i3++;
                            }
                            if (mediaCell != null) {
                                break;
                            }
                        }
                    }
                    if (previewGroupCell != null && mediaCell != null) {
                        placeProviderObject = new PhotoViewer.PlaceProviderObject();
                        int[] iArr = new int[2];
                        PreviewGroupsView.this.getLocationInWindow(iArr);
                        if (Build.VERSION.SDK_INT < 26) {
                            iArr[0] = iArr[0] - ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getLeftInset();
                        }
                        placeProviderObject.viewX = iArr[0];
                        placeProviderObject.viewY = iArr[1] + ((int) previewGroupCell.y);
                        placeProviderObject.scale = 1.0f;
                        placeProviderObject.parentView = PreviewGroupsView.this;
                        ImageReceiver imageReceiver = mediaCell.image;
                        placeProviderObject.imageReceiver = imageReceiver;
                        placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                        placeProviderObject.radius = r13;
                        RectF rectF = mediaCell.roundRadiuses;
                        int[] iArr2 = {(int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom};
                        placeProviderObject.clipTopAddition = (int) (-PreviewGroupsView.this.getY());
                        placeProviderObject.clipBottomAddition = PreviewGroupsView.this.getHeight() - ((int) (((-PreviewGroupsView.this.getY()) + ChatAttachAlertPhotoLayoutPreview.this.listView.getHeight()) - ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getClipLayoutBottom()));
                    }
                }
                return placeProviderObject;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void updatePhotoAtIndex(int i) {
                MediaController.PhotoEntry photoEntry;
                boolean z;
                if (i < 0 || i >= this.photos.size() || (photoEntry = this.photos.get(i)) == null) {
                    return;
                }
                int i2 = photoEntry.imageId;
                PreviewGroupsView.this.invalidate();
                for (int i3 = 0; i3 < PreviewGroupsView.this.groupCells.size(); i3++) {
                    PreviewGroupCell previewGroupCell = (PreviewGroupCell) PreviewGroupsView.this.groupCells.get(i3);
                    if (previewGroupCell != null && previewGroupCell.media != null) {
                        for (int i4 = 0; i4 < previewGroupCell.media.size(); i4++) {
                            PreviewGroupCell.MediaCell mediaCell = previewGroupCell.media.get(i4);
                            if (mediaCell != null && mediaCell.photoEntry.imageId == i2) {
                                mediaCell.setImage(photoEntry);
                            }
                        }
                        if (previewGroupCell.group == null || previewGroupCell.group.photos == null) {
                            z = false;
                        } else {
                            z = false;
                            for (int i5 = 0; i5 < previewGroupCell.group.photos.size(); i5++) {
                                if (previewGroupCell.group.photos.get(i5).imageId == i2) {
                                    previewGroupCell.group.photos.set(i5, photoEntry);
                                    z = true;
                                }
                            }
                        }
                        if (z) {
                            previewGroupCell.setGroup(previewGroupCell.group, true);
                        }
                    }
                }
                PreviewGroupsView.this.remeasure();
                PreviewGroupsView.this.invalidate();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:180:0x0433  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x04a2  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x04ca  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x04d9  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            PreviewGroupCell previewGroupCell;
            PreviewGroupCell.MediaCell mediaCell;
            PreviewGroupCell.MediaCell mediaCell2;
            PreviewGroupCell previewGroupCell2;
            int i;
            boolean z;
            PreviewGroupCell.MediaCell mediaCell3;
            int i2;
            ChatActivity chatActivity;
            PreviewGroupCell.MediaCell mediaCell4;
            float f;
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int size = this.groupCells.size();
            int i3 = 0;
            float f2 = 0.0f;
            while (true) {
                if (i3 >= size) {
                    previewGroupCell = null;
                    break;
                }
                previewGroupCell = this.groupCells.get(i3);
                float measure = previewGroupCell.measure();
                if (y >= f2 && y <= f2 + measure) {
                    break;
                }
                f2 += measure;
                i3++;
            }
            if (previewGroupCell != null) {
                int size2 = previewGroupCell.media.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    mediaCell = previewGroupCell.media.get(i4);
                    if (mediaCell != null && mediaCell.drawingRect().contains(x, y - f2)) {
                        break;
                    }
                }
            }
            mediaCell = null;
            int i5 = 4;
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                RectF rect = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                Point dragTranslate = dragTranslate();
                RectF rectF = new RectF();
                float f3 = dragTranslate.x;
                float f4 = dragTranslate.y;
                rectF.set(f3 - (rect.width() / 2.0f), f4 - (rect.height() / 2.0f), f3 + (rect.width() / 2.0f), f4 + (rect.height() / 2.0f));
                int i6 = 0;
                previewGroupCell2 = null;
                float f5 = 0.0f;
                float f6 = 0.0f;
                while (i6 < size) {
                    PreviewGroupCell previewGroupCell3 = this.groupCells.get(i6);
                    float measure2 = f5 + previewGroupCell3.measure();
                    if (measure2 >= rectF.top) {
                        float f7 = rectF.bottom;
                        if (f7 >= f5) {
                            float min = Math.min(measure2, f7) - Math.max(f5, rectF.top);
                            if (min > f6) {
                                f6 = min;
                                previewGroupCell2 = previewGroupCell3;
                            }
                        }
                    }
                    i6++;
                    f5 = measure2;
                }
                if (previewGroupCell2 != null) {
                    int size3 = previewGroupCell2.media.size();
                    int i7 = 0;
                    mediaCell2 = null;
                    float f8 = 0.0f;
                    while (i7 < size3) {
                        PreviewGroupCell.MediaCell mediaCell5 = previewGroupCell2.media.get(i7);
                        if (mediaCell5 != null && mediaCell5 != ChatAttachAlertPhotoLayoutPreview.this.draggingCell && previewGroupCell2.group.photos.contains(mediaCell5.photoEntry)) {
                            RectF drawingRect = mediaCell5.drawingRect();
                            if ((mediaCell5.positionFlags & i5) > 0) {
                                f = 0.0f;
                                drawingRect.top = 0.0f;
                            } else {
                                f = 0.0f;
                            }
                            if ((mediaCell5.positionFlags & 1) > 0) {
                                drawingRect.left = f;
                            }
                            if ((mediaCell5.positionFlags & 2) > 0) {
                                drawingRect.right = getWidth();
                            }
                            if ((mediaCell5.positionFlags & 8) > 0) {
                                drawingRect.bottom = previewGroupCell2.height;
                            }
                            if (RectF.intersects(rectF, drawingRect)) {
                                float min2 = ((Math.min(drawingRect.right, rectF.right) - Math.max(drawingRect.left, rectF.left)) * (Math.min(drawingRect.bottom, rectF.bottom) - Math.max(drawingRect.top, rectF.top))) / (rectF.width() * rectF.height());
                                if (min2 > 0.15f && min2 > f8) {
                                    mediaCell2 = mediaCell5;
                                    f8 = min2;
                                }
                            }
                        }
                        i7++;
                        i5 = 4;
                    }
                } else {
                    mediaCell2 = null;
                }
            } else {
                mediaCell2 = null;
                previewGroupCell2 = null;
            }
            int action = motionEvent.getAction();
            if (action == 0 && ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null) {
                ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = ChatAttachAlertPhotoLayoutPreview.this;
                if (!chatAttachAlertPhotoLayoutPreview.listView.scrollingByUser && ((chatAttachAlertPhotoLayoutPreview.draggingAnimator == null || !ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.isRunning()) && previewGroupCell != null && mediaCell != null && previewGroupCell.group != null && previewGroupCell.group.photos.contains(mediaCell.photoEntry))) {
                    this.tapGroupCell = previewGroupCell;
                    this.tapMediaCell = mediaCell;
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchX = x;
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY = y;
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCell = null;
                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                    this.tapTime = elapsedRealtime;
                    final PreviewGroupCell.MediaCell mediaCell6 = this.tapMediaCell;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$onTouchEvent$2(elapsedRealtime, mediaCell6);
                        }
                    }, ViewConfiguration.getLongPressTimeout());
                    invalidate();
                    i = 1;
                    z = true;
                    if (action != i || action == 3) {
                        this.tapTime = 0L;
                        removeCallbacks(this.scroller);
                        this.scrollerStarted = false;
                        if (!z) {
                            stopDragging();
                            return true;
                        }
                    }
                    return z;
                }
            }
            if (action != 2 || ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null || ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                int i8 = 1;
                if (action == 1) {
                    if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                        if (previewGroupCell == null || mediaCell == null || mediaCell == ChatAttachAlertPhotoLayoutPreview.this.draggingCell) {
                            if (previewGroupCell2 == null || mediaCell2 == null || mediaCell2 == ChatAttachAlertPhotoLayoutPreview.this.draggingCell || mediaCell2.photoEntry == ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry) {
                                previewGroupCell = null;
                                mediaCell = null;
                            } else {
                                mediaCell = mediaCell2;
                                previewGroupCell = previewGroupCell2;
                            }
                        }
                        if (previewGroupCell != null && mediaCell != null && mediaCell != ChatAttachAlertPhotoLayoutPreview.this.draggingCell) {
                            int indexOf = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.group.photos.indexOf(ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry);
                            int indexOf2 = previewGroupCell.group.photos.indexOf(mediaCell.photoEntry);
                            if (indexOf >= 0) {
                                ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.group.photos.remove(indexOf);
                                ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.setGroup(ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.group, true);
                            }
                            if (indexOf2 >= 0) {
                                if (this.groupCells.indexOf(previewGroupCell) > this.groupCells.indexOf(ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell)) {
                                    indexOf2++;
                                }
                                pushToGroup(previewGroupCell, ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry, indexOf2);
                                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell != previewGroupCell) {
                                    int size4 = previewGroupCell.media.size();
                                    int i9 = 0;
                                    while (true) {
                                        if (i9 >= size4) {
                                            mediaCell4 = null;
                                            break;
                                        }
                                        mediaCell4 = previewGroupCell.media.get(i9);
                                        if (mediaCell4.photoEntry == ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry) {
                                            break;
                                        }
                                        i9++;
                                    }
                                    if (mediaCell4 != null) {
                                        remeasure();
                                        mediaCell4.layoutFrom(ChatAttachAlertPhotoLayoutPreview.this.draggingCell);
                                        ChatAttachAlertPhotoLayoutPreview.this.draggingCell = mediaCell4;
                                        mediaCell4.groupCell = previewGroupCell;
                                        ChatAttachAlertPhotoLayoutPreview.this.draggingCell.fromScale = 1.0f;
                                        mediaCell4.scale = 1.0f;
                                        remeasure();
                                    }
                                }
                            }
                            try {
                                ChatAttachAlertPhotoLayoutPreview.this.performHapticFeedback(7, 2);
                            } catch (Exception unused) {
                            }
                            updateGroups();
                            toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
                        }
                        stopDragging();
                    } else {
                        i8 = 1;
                    }
                }
                if (action == i8 && ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null && (mediaCell3 = this.tapMediaCell) != null && this.tapGroupCell != null) {
                    RectF drawingRect2 = mediaCell3.drawingRect();
                    RectF rectF2 = AndroidUtilities.rectTmp;
                    rectF2.set(drawingRect2.right - AndroidUtilities.dp(36.4f), this.tapGroupCell.top + drawingRect2.top, drawingRect2.right, this.tapGroupCell.top + drawingRect2.top + AndroidUtilities.dp(36.4f));
                    if (rectF2.contains(x, y - this.tapMediaCell.groupCell.y)) {
                        if (ChatAttachAlertPhotoLayoutPreview.this.getSelectedItemsCount() > 1) {
                            final MediaController.PhotoEntry photoEntry = this.tapMediaCell.photoEntry;
                            final int indexOf3 = this.tapGroupCell.group.photos.indexOf(photoEntry);
                            if (indexOf3 >= 0) {
                                saveDeletedImageId(photoEntry);
                                final PreviewGroupCell previewGroupCell4 = this.tapGroupCell;
                                previewGroupCell4.group.photos.remove(indexOf3);
                                previewGroupCell4.setGroup(previewGroupCell4.group, true);
                                updateGroups();
                                toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
                                final int i10 = this.undoViewId + 1;
                                this.undoViewId = i10;
                                ChatAttachAlertPhotoLayoutPreview.this.undoView.showWithAction(0L, 82, photoEntry, null, new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$onTouchEvent$3(previewGroupCell4, photoEntry, indexOf3);
                                    }
                                });
                                postDelayed(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda2
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$onTouchEvent$4(i10);
                                    }
                                }, 4000L);
                            }
                            if (ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator != null) {
                                ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.cancel();
                            }
                        }
                    } else {
                        calcPhotoArrays();
                        ArrayList<MediaController.PhotoEntry> photos = getPhotos();
                        int indexOf4 = photos.indexOf(this.tapMediaCell.photoEntry);
                        ChatAttachAlert chatAttachAlert = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                        if (chatAttachAlert.avatarPicker != 0) {
                            i2 = 1;
                        } else {
                            BaseFragment baseFragment = chatAttachAlert.baseFragment;
                            if (baseFragment instanceof ChatActivity) {
                                chatActivity = (ChatActivity) baseFragment;
                                i2 = 0;
                                if (!chatAttachAlert.delegate.needEnterComment()) {
                                    AndroidUtilities.hideKeyboard(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.baseFragment.getFragmentView().findFocus());
                                    AndroidUtilities.hideKeyboard(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getContainer().findFocus());
                                }
                                PhotoViewer.getInstance().setParentActivity(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.baseFragment.getParentActivity(), ChatAttachAlertPhotoLayoutPreview.this.resourcesProvider);
                                PhotoViewer.getInstance().setParentAlert(ChatAttachAlertPhotoLayoutPreview.this.parentAlert);
                                PhotoViewer photoViewer = PhotoViewer.getInstance();
                                ChatAttachAlert chatAttachAlert2 = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                                photoViewer.setMaxSelectedPhotos(chatAttachAlert2.maxSelectedPhotos, chatAttachAlert2.allowOrder);
                                this.photoViewerProvider.init(photos);
                                PhotoViewer.getInstance().openPhotoForSelect(new ArrayList<>(photos), indexOf4, i2, false, this.photoViewerProvider, chatActivity);
                                if (ChatAttachAlertPhotoLayoutPreview.this.photoLayout.captionForAllMedia()) {
                                    PhotoViewer.getInstance().setCaption(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getCommentTextView().getText());
                                }
                            } else {
                                i2 = 4;
                            }
                        }
                        chatActivity = null;
                        if (!chatAttachAlert.delegate.needEnterComment()) {
                        }
                        PhotoViewer.getInstance().setParentActivity(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.baseFragment.getParentActivity(), ChatAttachAlertPhotoLayoutPreview.this.resourcesProvider);
                        PhotoViewer.getInstance().setParentAlert(ChatAttachAlertPhotoLayoutPreview.this.parentAlert);
                        PhotoViewer photoViewer2 = PhotoViewer.getInstance();
                        ChatAttachAlert chatAttachAlert22 = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                        photoViewer2.setMaxSelectedPhotos(chatAttachAlert22.maxSelectedPhotos, chatAttachAlert22.allowOrder);
                        this.photoViewerProvider.init(photos);
                        PhotoViewer.getInstance().openPhotoForSelect(new ArrayList<>(photos), indexOf4, i2, false, this.photoViewerProvider, chatActivity);
                        if (ChatAttachAlertPhotoLayoutPreview.this.photoLayout.captionForAllMedia()) {
                        }
                    }
                    this.tapMediaCell = null;
                    this.tapTime = 0L;
                    ChatAttachAlertPhotoLayoutPreview.this.draggingCell = null;
                    this.draggingT = 0.0f;
                } else {
                    i = 1;
                    z = false;
                    if (action != i) {
                    }
                    this.tapTime = 0L;
                    removeCallbacks(this.scroller);
                    this.scrollerStarted = false;
                    if (!z) {
                    }
                    return z;
                }
            } else {
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchX = x;
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY = y;
                if (!this.scrollerStarted) {
                    this.scrollerStarted = true;
                    postDelayed(this.scroller, 16L);
                }
                invalidate();
            }
            i = 1;
            z = true;
            if (action != i) {
            }
            this.tapTime = 0L;
            removeCallbacks(this.scroller);
            this.scrollerStarted = false;
            if (!z) {
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$2(long j, PreviewGroupCell.MediaCell mediaCell) {
            PreviewGroupCell.MediaCell mediaCell2;
            if (!ChatAttachAlertPhotoLayoutPreview.this.listView.scrollingByUser && this.tapTime == j && (mediaCell2 = this.tapMediaCell) == mediaCell) {
                startDragging(mediaCell2);
                RectF rect = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                RectF drawingRect = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.drawingRect();
                ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = ChatAttachAlertPhotoLayoutPreview.this;
                chatAttachAlertPhotoLayoutPreview.draggingCellLeft = (((chatAttachAlertPhotoLayoutPreview.draggingCellTouchX - rect.left) / rect.width()) + 0.5f) / 2.0f;
                ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview2 = ChatAttachAlertPhotoLayoutPreview.this;
                chatAttachAlertPhotoLayoutPreview2.draggingCellTop = (chatAttachAlertPhotoLayoutPreview2.draggingCellTouchY - rect.top) / rect.height();
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromWidth = drawingRect.width();
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromHeight = drawingRect.height();
                try {
                    ChatAttachAlertPhotoLayoutPreview.this.performHapticFeedback(0, 2);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$3(PreviewGroupCell previewGroupCell, MediaController.PhotoEntry photoEntry, int i) {
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator != null) {
                ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.cancel();
            }
            ChatAttachAlertPhotoLayoutPreview.this.draggingCell = null;
            this.draggingT = 0.0f;
            pushToGroup(previewGroupCell, photoEntry, i);
            updateGroups();
            toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$4(int i) {
            if (i != this.undoViewId || !ChatAttachAlertPhotoLayoutPreview.this.undoView.isShown()) {
                return;
            }
            ChatAttachAlertPhotoLayoutPreview.this.undoView.hide(true, 1);
        }

        private void pushToGroup(PreviewGroupCell previewGroupCell, MediaController.PhotoEntry photoEntry, int i) {
            previewGroupCell.group.photos.add(Math.min(previewGroupCell.group.photos.size(), i), photoEntry);
            if (previewGroupCell.group.photos.size() == 11) {
                MediaController.PhotoEntry photoEntry2 = previewGroupCell.group.photos.get(10);
                previewGroupCell.group.photos.remove(10);
                int indexOf = this.groupCells.indexOf(previewGroupCell);
                if (indexOf >= 0) {
                    int i2 = indexOf + 1;
                    PreviewGroupCell previewGroupCell2 = i2 == this.groupCells.size() ? null : this.groupCells.get(i2);
                    if (previewGroupCell2 == null) {
                        PreviewGroupCell previewGroupCell3 = new PreviewGroupCell();
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(photoEntry2);
                        previewGroupCell3.setGroup(new GroupCalculator(arrayList), true);
                        invalidate();
                    } else {
                        pushToGroup(previewGroupCell2, photoEntry2, 0);
                    }
                }
            }
            previewGroupCell.setGroup(previewGroupCell.group, true);
        }

        private void updateGroups() {
            int size = this.groupCells.size();
            for (int i = 0; i < size; i++) {
                PreviewGroupCell previewGroupCell = this.groupCells.get(i);
                if (previewGroupCell.group.photos.size() < 10 && i < this.groupCells.size() - 1) {
                    int size2 = 10 - previewGroupCell.group.photos.size();
                    PreviewGroupCell previewGroupCell2 = this.groupCells.get(i + 1);
                    ArrayList arrayList = new ArrayList();
                    int min = Math.min(size2, previewGroupCell2.group.photos.size());
                    for (int i2 = 0; i2 < min; i2++) {
                        arrayList.add(previewGroupCell2.group.photos.remove(0));
                    }
                    previewGroupCell.group.photos.addAll(arrayList);
                    previewGroupCell.setGroup(previewGroupCell.group, true);
                    previewGroupCell2.setGroup(previewGroupCell2.group, true);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class PreviewGroupCell {
            private Theme.MessageDrawable.PathDrawParams backgroundCacheParams;
            private float bottom;
            final int gap;
            private GroupCalculator group;
            private float groupHeight;
            private float groupWidth;
            final int halfGap;
            private float height;
            public int indexStart;
            private Interpolator interpolator;
            private long lastMediaUpdate;
            private float left;
            public ArrayList<MediaCell> media;
            private Theme.MessageDrawable messageBackground;
            final int padding;
            private float previousGroupHeight;
            private float previousGroupWidth;
            private float right;
            private float top;
            private float width;
            public float y;

            private PreviewGroupCell() {
                this.y = 0.0f;
                this.indexStart = 0;
                this.lastMediaUpdate = 0L;
                this.groupWidth = 0.0f;
                this.groupHeight = 0.0f;
                this.previousGroupWidth = 0.0f;
                this.previousGroupHeight = 0.0f;
                this.media = new ArrayList<>();
                this.interpolator = CubicBezierInterpolator.EASE_BOTH;
                this.padding = AndroidUtilities.dp(4.0f);
                int dp = AndroidUtilities.dp(2.0f);
                this.gap = dp;
                this.halfGap = dp / 2;
                this.messageBackground = (Theme.MessageDrawable) ChatAttachAlertPhotoLayoutPreview.this.getThemedDrawable("drawableMsgOutMedia");
                this.backgroundCacheParams = new Theme.MessageDrawable.PathDrawParams();
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: classes3.dex */
            public class MediaCell {
                private Paint bitmapPaint;
                private android.graphics.Rect durationIn;
                private android.graphics.Rect durationOut;
                private RectF fromRect;
                public RectF fromRoundRadiuses;
                public float fromScale;
                public PreviewGroupCell groupCell;
                public ImageReceiver image;
                private Bitmap indexBitmap;
                private String indexBitmapText;
                private android.graphics.Rect indexIn;
                private android.graphics.Rect indexOut;
                private long lastUpdate;
                private long lastVisibleTUpdate;
                private Paint paint;
                public MediaController.PhotoEntry photoEntry;
                private int positionFlags;
                public RectF rect;
                public RectF roundRadiuses;
                public float scale;
                private Paint strokePaint;
                private RectF tempRect;
                private TextPaint textPaint;
                private Bitmap videoDurationBitmap;
                private String videoDurationBitmapText;
                private String videoDurationText;
                private TextPaint videoDurationTextPaint;
                private float visibleT;

                private MediaCell() {
                    this.groupCell = PreviewGroupCell.this;
                    this.fromRect = null;
                    this.rect = new RectF();
                    this.lastUpdate = 0L;
                    this.positionFlags = 0;
                    this.fromScale = 1.0f;
                    this.scale = 0.0f;
                    this.fromRoundRadiuses = null;
                    this.roundRadiuses = new RectF();
                    this.videoDurationText = null;
                    this.tempRect = new RectF();
                    this.paint = new Paint(1);
                    this.strokePaint = new Paint(1);
                    this.bitmapPaint = new Paint(1);
                    this.indexBitmap = null;
                    this.indexBitmapText = null;
                    this.videoDurationBitmap = null;
                    this.videoDurationBitmapText = null;
                    this.indexIn = new android.graphics.Rect();
                    this.indexOut = new android.graphics.Rect();
                    this.durationIn = new android.graphics.Rect();
                    this.durationOut = new android.graphics.Rect();
                    this.visibleT = 1.0f;
                    this.lastVisibleTUpdate = 0L;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void setImage(MediaController.PhotoEntry photoEntry) {
                    this.photoEntry = photoEntry;
                    if (photoEntry != null && photoEntry.isVideo) {
                        this.videoDurationText = AndroidUtilities.formatShortDuration(photoEntry.duration);
                    } else {
                        this.videoDurationText = null;
                    }
                    if (this.image == null) {
                        this.image = new ImageReceiver(PreviewGroupsView.this);
                    }
                    if (photoEntry != null) {
                        String str = photoEntry.thumbPath;
                        if (str != null) {
                            this.image.setImage(ImageLocation.getForPath(str), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                        } else if (photoEntry.path != null) {
                            if (photoEntry.isVideo) {
                                ImageReceiver imageReceiver = this.image;
                                imageReceiver.setImage(ImageLocation.getForPath("vthumb://" + photoEntry.imageId + ":" + photoEntry.path), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                                this.image.setAllowStartAnimation(true);
                                return;
                            }
                            this.image.setOrientation(photoEntry.orientation, true);
                            ImageReceiver imageReceiver2 = this.image;
                            imageReceiver2.setImage(ImageLocation.getForPath("thumb://" + photoEntry.imageId + ":" + photoEntry.path), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                        } else {
                            this.image.setImageBitmap(Theme.chat_attachEmptyDrawable);
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void layoutFrom(MediaCell mediaCell) {
                    this.fromScale = AndroidUtilities.lerp(mediaCell.fromScale, mediaCell.scale, mediaCell.getT());
                    if (this.fromRect == null) {
                        this.fromRect = new RectF();
                    }
                    RectF rectF = new RectF();
                    RectF rectF2 = this.fromRect;
                    if (rectF2 == null) {
                        rectF.set(this.rect);
                    } else {
                        AndroidUtilities.lerp(rectF2, this.rect, getT(), rectF);
                    }
                    RectF rectF3 = mediaCell.fromRect;
                    if (rectF3 != null) {
                        AndroidUtilities.lerp(rectF3, mediaCell.rect, mediaCell.getT(), this.fromRect);
                        this.fromRect.set(rectF.centerX() - (((this.fromRect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width), rectF.centerY() - (((this.fromRect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height), rectF.centerX() + (((this.fromRect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width), rectF.centerY() + (((this.fromRect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height));
                    } else {
                        this.fromRect.set(rectF.centerX() - (((mediaCell.rect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width), rectF.centerY() - (((mediaCell.rect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height), rectF.centerX() + (((mediaCell.rect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width), rectF.centerY() + (((mediaCell.rect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height));
                    }
                    this.fromScale = AndroidUtilities.lerp(this.fromScale, this.scale, getT());
                    this.lastUpdate = SystemClock.elapsedRealtime();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void layout(GroupCalculator groupCalculator, MessageObject.GroupedMessagePosition groupedMessagePosition, boolean z) {
                    if (groupCalculator == null || groupedMessagePosition == null) {
                        if (z) {
                            long elapsedRealtime = SystemClock.elapsedRealtime();
                            this.fromScale = AndroidUtilities.lerp(this.fromScale, this.scale, getT());
                            RectF rectF = this.fromRect;
                            if (rectF != null) {
                                AndroidUtilities.lerp(rectF, this.rect, getT(), this.fromRect);
                            }
                            this.scale = 0.0f;
                            this.lastUpdate = elapsedRealtime;
                            return;
                        }
                        this.fromScale = 0.0f;
                        this.scale = 0.0f;
                        return;
                    }
                    this.positionFlags = groupedMessagePosition.flags;
                    if (z) {
                        float t = getT();
                        RectF rectF2 = this.fromRect;
                        if (rectF2 != null) {
                            AndroidUtilities.lerp(rectF2, this.rect, t, rectF2);
                        }
                        RectF rectF3 = this.fromRoundRadiuses;
                        if (rectF3 != null) {
                            AndroidUtilities.lerp(rectF3, this.roundRadiuses, t, rectF3);
                        }
                        this.fromScale = AndroidUtilities.lerp(this.fromScale, this.scale, t);
                        this.lastUpdate = SystemClock.elapsedRealtime();
                    }
                    float f = groupedMessagePosition.left;
                    int i = groupCalculator.width;
                    float f2 = f / i;
                    float f3 = groupedMessagePosition.top;
                    float f4 = groupCalculator.height;
                    float f5 = f3 / f4;
                    float f6 = groupedMessagePosition.ph / f4;
                    this.scale = 1.0f;
                    this.rect.set(f2, f5, (groupedMessagePosition.pw / i) + f2, f6 + f5);
                    float dp = AndroidUtilities.dp(2.0f);
                    float dp2 = AndroidUtilities.dp(SharedConfig.bubbleRadius - 1);
                    RectF rectF4 = this.roundRadiuses;
                    int i2 = this.positionFlags;
                    float f7 = (i2 & 5) == 5 ? dp2 : dp;
                    float f8 = (i2 & 6) == 6 ? dp2 : dp;
                    float f9 = (i2 & 10) == 10 ? dp2 : dp;
                    if ((i2 & 9) == 9) {
                        dp = dp2;
                    }
                    rectF4.set(f7, f8, f9, dp);
                    if (this.fromRect == null) {
                        RectF rectF5 = new RectF();
                        this.fromRect = rectF5;
                        rectF5.set(this.rect);
                    }
                    if (this.fromRoundRadiuses != null) {
                        return;
                    }
                    RectF rectF6 = new RectF();
                    this.fromRoundRadiuses = rectF6;
                    rectF6.set(this.roundRadiuses);
                }

                public float getT() {
                    return PreviewGroupCell.this.interpolator.getInterpolation(Math.min(1.0f, ((float) (SystemClock.elapsedRealtime() - this.lastUpdate)) / 200.0f));
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public MediaCell clone() {
                    MediaCell mediaCell = new MediaCell();
                    mediaCell.rect.set(this.rect);
                    mediaCell.image = this.image;
                    mediaCell.photoEntry = this.photoEntry;
                    return mediaCell;
                }

                public RectF rect() {
                    return rect(getT());
                }

                public RectF rect(float f) {
                    if (this.rect != null && this.image != null) {
                        float f2 = PreviewGroupCell.this.left + (this.rect.left * PreviewGroupCell.this.width);
                        float f3 = PreviewGroupCell.this.top + (this.rect.top * PreviewGroupCell.this.height);
                        float width = this.rect.width() * PreviewGroupCell.this.width;
                        float height = this.rect.height() * PreviewGroupCell.this.height;
                        if (f < 1.0f && this.fromRect != null) {
                            f2 = AndroidUtilities.lerp(PreviewGroupCell.this.left + (this.fromRect.left * PreviewGroupCell.this.width), f2, f);
                            f3 = AndroidUtilities.lerp(PreviewGroupCell.this.top + (this.fromRect.top * PreviewGroupCell.this.height), f3, f);
                            width = AndroidUtilities.lerp(this.fromRect.width() * PreviewGroupCell.this.width, width, f);
                            height = AndroidUtilities.lerp(this.fromRect.height() * PreviewGroupCell.this.height, height, f);
                        }
                        int i = this.positionFlags;
                        if ((i & 4) == 0) {
                            int i2 = PreviewGroupCell.this.halfGap;
                            f3 += i2;
                            height -= i2;
                        }
                        if ((i & 8) == 0) {
                            height -= PreviewGroupCell.this.halfGap;
                        }
                        if ((i & 1) == 0) {
                            int i3 = PreviewGroupCell.this.halfGap;
                            f2 += i3;
                            width -= i3;
                        }
                        if ((i & 2) == 0) {
                            width -= PreviewGroupCell.this.halfGap;
                        }
                        this.tempRect.set(f2, f3, width + f2, height + f3);
                        return this.tempRect;
                    }
                    this.tempRect.set(0.0f, 0.0f, 0.0f, 0.0f);
                    return this.tempRect;
                }

                public RectF drawingRect() {
                    float f = 0.0f;
                    if (this.rect != null && this.image != null) {
                        if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null && ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry == this.photoEntry) {
                            f = PreviewGroupsView.this.draggingT;
                        }
                        float lerp = AndroidUtilities.lerp(this.fromScale, this.scale, getT()) * (((1.0f - f) * 0.2f) + 0.8f);
                        RectF rect = rect();
                        float f2 = 1.0f - lerp;
                        float f3 = lerp + 1.0f;
                        rect.set(rect.left + ((rect.width() * f2) / 2.0f), rect.top + ((rect.height() * f2) / 2.0f), rect.left + ((rect.width() * f3) / 2.0f), rect.top + ((rect.height() * f3) / 2.0f));
                        return rect;
                    }
                    this.tempRect.set(0.0f, 0.0f, 0.0f, 0.0f);
                    return this.tempRect;
                }

                private void drawPhotoIndex(Canvas canvas, float f, float f2, String str, float f3, float f4) {
                    String str2;
                    int dp = AndroidUtilities.dp(12.0f);
                    int dp2 = AndroidUtilities.dp(1.2f);
                    int i = (dp + dp2) * 2;
                    int i2 = dp2 * 4;
                    if (str != null && (this.indexBitmap == null || (str2 = this.indexBitmapText) == null || !str2.equals(str))) {
                        if (this.indexBitmap == null) {
                            this.indexBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
                        }
                        Canvas canvas2 = new Canvas(this.indexBitmap);
                        canvas2.drawColor(0);
                        if (this.textPaint == null) {
                            TextPaint textPaint = new TextPaint(1);
                            this.textPaint = textPaint;
                            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        }
                        this.textPaint.setColor(ChatAttachAlertPhotoLayoutPreview.this.getThemedColor("chat_attachCheckBoxCheck"));
                        int length = str.length();
                        float f5 = (length == 0 || length == 1 || length == 2) ? 14.0f : length != 3 ? 8.0f : 10.0f;
                        this.textPaint.setTextSize(AndroidUtilities.dp(f5));
                        float f6 = i / 2.0f;
                        this.paint.setColor(ChatAttachAlertPhotoLayoutPreview.this.getThemedColor("chat_attachCheckBoxBackground"));
                        float f7 = (int) f6;
                        float f8 = dp;
                        canvas2.drawCircle(f7, f7, f8, this.paint);
                        this.strokePaint.setColor(AndroidUtilities.getOffsetColor(-1, ChatAttachAlertPhotoLayoutPreview.this.getThemedColor("chat_attachCheckBoxCheck"), 1.0f, 1.0f));
                        this.strokePaint.setStyle(Paint.Style.STROKE);
                        this.strokePaint.setStrokeWidth(dp2);
                        canvas2.drawCircle(f7, f7, f8, this.strokePaint);
                        canvas2.drawText(str, f6 - (this.textPaint.measureText(str) / 2.0f), f6 + AndroidUtilities.dp(1.0f) + AndroidUtilities.dp(f5 / 4.0f), this.textPaint);
                        this.indexIn.set(0, 0, i, i);
                        this.indexBitmapText = str;
                    }
                    if (this.indexBitmap != null) {
                        float f9 = i * f3;
                        float f10 = i2;
                        float f11 = f - f10;
                        this.indexOut.set((int) ((f2 - f9) + f10), (int) f11, (int) (f2 + f10), (int) (f11 + f9));
                        this.bitmapPaint.setAlpha((int) (255.0f * f4));
                        canvas.drawBitmap(this.indexBitmap, this.indexIn, this.indexOut, this.bitmapPaint);
                    }
                }

                private void drawDuration(Canvas canvas, float f, float f2, String str, float f3, float f4) {
                    String str2;
                    if (str != null) {
                        if (this.videoDurationBitmap == null || (str2 = this.videoDurationBitmapText) == null || !str2.equals(str)) {
                            if (this.videoDurationTextPaint == null) {
                                TextPaint textPaint = new TextPaint(1);
                                this.videoDurationTextPaint = textPaint;
                                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                                this.videoDurationTextPaint.setColor(-1);
                            }
                            float dp = AndroidUtilities.dp(12.0f);
                            this.videoDurationTextPaint.setTextSize(dp);
                            float intrinsicWidth = ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.getIntrinsicWidth() + this.videoDurationTextPaint.measureText(str) + AndroidUtilities.dp(15.0f);
                            float max = Math.max(dp, ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.getIntrinsicHeight() + AndroidUtilities.dp(4.0f));
                            int ceil = (int) Math.ceil(intrinsicWidth);
                            int ceil2 = (int) Math.ceil(max);
                            Bitmap bitmap = this.videoDurationBitmap;
                            if (bitmap == null || bitmap.getWidth() != ceil || this.videoDurationBitmap.getHeight() != ceil2) {
                                Bitmap bitmap2 = this.videoDurationBitmap;
                                if (bitmap2 != null) {
                                    bitmap2.recycle();
                                }
                                this.videoDurationBitmap = Bitmap.createBitmap(ceil, ceil2, Bitmap.Config.ARGB_8888);
                            }
                            Canvas canvas2 = new Canvas(this.videoDurationBitmap);
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set(0.0f, 0.0f, intrinsicWidth, max);
                            canvas2.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                            int dp2 = AndroidUtilities.dp(5.0f);
                            int intrinsicHeight = (int) ((max - ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.getIntrinsicHeight()) / 2.0f);
                            ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.setBounds(dp2, intrinsicHeight, ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.getIntrinsicWidth() + dp2, ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.getIntrinsicHeight() + intrinsicHeight);
                            ChatAttachAlertPhotoLayoutPreview.this.videoPlayImage.draw(canvas2);
                            canvas2.drawText(str, AndroidUtilities.dp(18.0f), dp + AndroidUtilities.dp(-0.7f), this.videoDurationTextPaint);
                            this.durationIn.set(0, 0, ceil, ceil2);
                            this.videoDurationBitmapText = str;
                        }
                        this.durationOut.set((int) f, (int) (f2 - (this.videoDurationBitmap.getHeight() * f3)), (int) (f + (this.videoDurationBitmap.getWidth() * f3)), (int) f2);
                        this.bitmapPaint.setAlpha((int) (f4 * 255.0f));
                        canvas.drawBitmap(this.videoDurationBitmap, this.durationIn, this.durationOut, this.bitmapPaint);
                    }
                }

                public boolean draw(Canvas canvas) {
                    return draw(canvas, false);
                }

                public boolean draw(Canvas canvas, boolean z) {
                    return draw(canvas, getT(), z);
                }

                public boolean draw(Canvas canvas, float f, boolean z) {
                    int indexOf;
                    String str;
                    float f2;
                    RectF rectF;
                    if (this.rect == null || this.image == null) {
                        return false;
                    }
                    float f3 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell == this ? PreviewGroupsView.this.draggingT : 0.0f;
                    float lerp = AndroidUtilities.lerp(this.fromScale, this.scale, f);
                    if (lerp <= 0.0f) {
                        return false;
                    }
                    RectF drawingRect = drawingRect();
                    float dp = AndroidUtilities.dp(SharedConfig.bubbleRadius - 1);
                    RectF rectF2 = this.roundRadiuses;
                    float f4 = rectF2.left;
                    float f5 = rectF2.top;
                    float f6 = rectF2.right;
                    float f7 = rectF2.bottom;
                    if (f < 1.0f && (rectF = this.fromRoundRadiuses) != null) {
                        f4 = AndroidUtilities.lerp(rectF.left, f4, f);
                        f5 = AndroidUtilities.lerp(this.fromRoundRadiuses.top, f5, f);
                        f6 = AndroidUtilities.lerp(this.fromRoundRadiuses.right, f6, f);
                        f7 = AndroidUtilities.lerp(this.fromRoundRadiuses.bottom, f7, f);
                    }
                    float lerp2 = AndroidUtilities.lerp(f4, dp, f3);
                    float lerp3 = AndroidUtilities.lerp(f5, dp, f3);
                    float lerp4 = AndroidUtilities.lerp(f6, dp, f3);
                    float lerp5 = AndroidUtilities.lerp(f7, dp, f3);
                    if (z) {
                        canvas.save();
                        canvas.translate(-drawingRect.centerX(), -drawingRect.centerY());
                    }
                    this.image.setRoundRadius((int) lerp2, (int) lerp3, (int) lerp4, (int) lerp5);
                    this.image.setImageCoords(drawingRect.left, drawingRect.top, drawingRect.width(), drawingRect.height());
                    this.image.setAlpha(lerp);
                    this.image.draw(canvas);
                    PreviewGroupCell previewGroupCell = PreviewGroupCell.this;
                    if (previewGroupCell.indexStart + previewGroupCell.group.photos.indexOf(this.photoEntry) >= 0) {
                        str = (indexOf + 1) + "";
                    } else {
                        str = null;
                    }
                    String str2 = str;
                    float f8 = this.image.getVisible() ? 1.0f : 0.0f;
                    boolean z2 = Math.abs(this.visibleT - f8) > 0.01f;
                    if (z2) {
                        f2 = lerp;
                        long min = Math.min(17L, SystemClock.elapsedRealtime() - this.lastVisibleTUpdate);
                        this.lastVisibleTUpdate = SystemClock.elapsedRealtime();
                        float f9 = ((float) min) / 100.0f;
                        float f10 = this.visibleT;
                        if (f8 < f10) {
                            this.visibleT = Math.max(0.0f, f10 - f9);
                        } else {
                            this.visibleT = Math.min(1.0f, f10 + f9);
                        }
                    } else {
                        f2 = lerp;
                    }
                    float f11 = f2;
                    drawPhotoIndex(canvas, AndroidUtilities.dp(10.0f) + drawingRect.top, drawingRect.right - AndroidUtilities.dp(10.0f), str2, f11, f2 * this.visibleT);
                    drawDuration(canvas, AndroidUtilities.dp(4.0f) + drawingRect.left, drawingRect.bottom - AndroidUtilities.dp(4.0f), this.videoDurationText, f11, f2 * this.visibleT);
                    if (z) {
                        canvas.restore();
                    }
                    return f < 1.0f || z2;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setGroup(GroupCalculator groupCalculator, boolean z) {
                MediaCell mediaCell;
                this.group = groupCalculator;
                if (groupCalculator == null) {
                    return;
                }
                groupCalculator.calculate();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = this.lastMediaUpdate;
                if (elapsedRealtime - j < 200) {
                    float f = ((float) (elapsedRealtime - j)) / 200.0f;
                    this.previousGroupHeight = AndroidUtilities.lerp(this.previousGroupHeight, this.groupHeight, f);
                    this.previousGroupWidth = AndroidUtilities.lerp(this.previousGroupWidth, this.groupWidth, f);
                } else {
                    this.previousGroupHeight = this.groupHeight;
                    this.previousGroupWidth = this.groupWidth;
                }
                this.groupWidth = groupCalculator.width / 1000.0f;
                this.groupHeight = groupCalculator.height;
                this.lastMediaUpdate = z ? elapsedRealtime : 0L;
                ArrayList arrayList = new ArrayList(groupCalculator.positions.keySet());
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) arrayList.get(i);
                    MessageObject.GroupedMessagePosition groupedMessagePosition = groupCalculator.positions.get(photoEntry);
                    int size2 = this.media.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= size2) {
                            mediaCell = null;
                            break;
                        }
                        mediaCell = this.media.get(i2);
                        if (mediaCell.photoEntry == photoEntry) {
                            break;
                        }
                        i2++;
                    }
                    if (mediaCell != null) {
                        mediaCell.layout(groupCalculator, groupedMessagePosition, z);
                    } else {
                        MediaCell mediaCell2 = new MediaCell();
                        mediaCell2.setImage(photoEntry);
                        mediaCell2.layout(groupCalculator, groupedMessagePosition, z);
                        this.media.add(mediaCell2);
                    }
                }
                int size3 = this.media.size();
                int i3 = 0;
                while (i3 < size3) {
                    MediaCell mediaCell3 = this.media.get(i3);
                    if (!groupCalculator.positions.containsKey(mediaCell3.photoEntry)) {
                        if (mediaCell3.scale <= 0.0f && mediaCell3.lastUpdate + 200 <= elapsedRealtime) {
                            this.media.remove(i3);
                            i3--;
                            size3--;
                        }
                        mediaCell3.layout(null, null, z);
                    }
                    i3++;
                }
                PreviewGroupsView.this.invalidate();
            }

            public float getT() {
                return this.interpolator.getInterpolation(Math.min(1.0f, ((float) (SystemClock.elapsedRealtime() - this.lastMediaUpdate)) / 200.0f));
            }

            public float measure() {
                android.graphics.Point point = AndroidUtilities.displaySize;
                return AndroidUtilities.lerp(this.previousGroupHeight, this.groupHeight, getT()) * Math.max(point.x, point.y) * 0.5f * ChatAttachAlertPhotoLayoutPreview.this.getPreviewScale();
            }

            public boolean draw(Canvas canvas) {
                float f = 1.0f;
                float interpolation = this.interpolator.getInterpolation(Math.min(1.0f, ((float) (SystemClock.elapsedRealtime() - this.lastMediaUpdate)) / 200.0f));
                boolean z = interpolation < 1.0f;
                android.graphics.Point point = AndroidUtilities.displaySize;
                float lerp = AndroidUtilities.lerp(this.previousGroupWidth, this.groupWidth, interpolation) * PreviewGroupsView.this.getWidth() * ChatAttachAlertPhotoLayoutPreview.this.getPreviewScale();
                float lerp2 = AndroidUtilities.lerp(this.previousGroupHeight, this.groupHeight, interpolation) * Math.max(point.x, point.y) * 0.5f * ChatAttachAlertPhotoLayoutPreview.this.getPreviewScale();
                if (this.messageBackground != null) {
                    this.top = 0.0f;
                    this.left = (PreviewGroupsView.this.getWidth() - Math.max(this.padding, lerp)) / 2.0f;
                    this.right = (PreviewGroupsView.this.getWidth() + Math.max(this.padding, lerp)) / 2.0f;
                    this.bottom = Math.max(this.padding * 2, lerp2);
                    this.messageBackground.setTop(0, (int) lerp, (int) lerp2, 0, 0, 0, false, false);
                    this.messageBackground.setBounds((int) this.left, (int) this.top, (int) this.right, (int) this.bottom);
                    if (this.groupWidth <= 0.0f) {
                        f = 1.0f - interpolation;
                    } else if (this.previousGroupWidth <= 0.0f) {
                        f = interpolation;
                    }
                    this.messageBackground.setAlpha((int) (f * 255.0f));
                    this.messageBackground.drawCached(canvas, this.backgroundCacheParams);
                    float f2 = this.top;
                    int i = this.padding;
                    this.top = f2 + i;
                    this.left += i;
                    this.bottom -= i;
                    this.right -= i;
                }
                this.width = this.right - this.left;
                this.height = this.bottom - this.top;
                int size = this.media.size();
                for (int i2 = 0; i2 < size; i2++) {
                    MediaCell mediaCell = this.media.get(i2);
                    if (mediaCell != null && ((ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null || ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry != mediaCell.photoEntry) && mediaCell.draw(canvas))) {
                        z = true;
                    }
                }
                return z;
            }
        }
    }

    public Drawable getThemedDrawable(String str) {
        Drawable drawable = this.themeDelegate.getDrawable(str);
        return drawable != null ? drawable : Theme.getThemeDrawable(str);
    }
}
