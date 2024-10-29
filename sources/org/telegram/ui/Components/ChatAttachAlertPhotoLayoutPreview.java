package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
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
import androidx.core.math.MathUtils;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
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
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stars.StarsIntroActivity;

/* loaded from: classes3.dex */
public class ChatAttachAlertPhotoLayoutPreview extends ChatAttachAlert.AttachAlertLayout {
    private static HashMap photoRotate = new HashMap();
    private ValueAnimator draggingAnimator;
    private PreviewGroupsView.PreviewGroupCell.MediaCell draggingCell;
    private float draggingCellFromHeight;
    private float draggingCellFromWidth;
    private float draggingCellGroupY;
    private boolean draggingCellHiding;
    private float draggingCellLeft;
    private float draggingCellTop;
    private float draggingCellTouchX;
    private float draggingCellTouchY;
    private final long durationMultiplier;
    private PreviewGroupsView groupsView;
    public TextView header;
    private ViewPropertyAnimator headerAnimator;
    private boolean ignoreLayout;
    private boolean isPortrait;
    private LinearLayoutManager layoutManager;
    public RecyclerListView listView;
    private int paddingTop;
    private ChatAttachAlertPhotoLayout photoLayout;
    private boolean shown;
    private Theme.ResourcesProvider themeDelegate;
    private UndoView undoView;
    private Drawable videoPlayImage;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class GroupCalculator {
        float height;
        int maxX;
        int maxY;
        ArrayList photos;
        int width;
        public ArrayList posArray = new ArrayList();
        public HashMap positions = new HashMap();
        private final int maxSizeWidth = 1000;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        public GroupCalculator(ArrayList arrayList) {
            this.photos = arrayList;
            calculate();
        }

        private float getLeft(MessageObject.GroupedMessagePosition groupedMessagePosition, int i, int i2, int i3) {
            int i4 = (i2 - i) + 1;
            float[] fArr = new float[i4];
            float f = 0.0f;
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i5 = 0; i5 < size; i5++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = (MessageObject.GroupedMessagePosition) this.posArray.get(i5);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxX < i3) {
                    int min = Math.min((int) groupedMessagePosition2.maxY, i2) - i;
                    for (int max = Math.max(groupedMessagePosition2.minY - i, 0); max <= min; max++) {
                        fArr[max] = fArr[max] + groupedMessagePosition2.pw;
                    }
                }
            }
            for (int i6 = 0; i6 < i4; i6++) {
                float f2 = fArr[i6];
                if (f < f2) {
                    f = f2;
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
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = (MessageObject.GroupedMessagePosition) this.posArray.get(i3);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxY < i) {
                    for (int i4 = groupedMessagePosition2.minX; i4 <= groupedMessagePosition2.maxX; i4++) {
                        fArr[i4] = fArr[i4] + groupedMessagePosition2.ph;
                    }
                }
            }
            for (int i5 = 0; i5 < i2; i5++) {
                float f2 = fArr[i5];
                if (f < f2) {
                    f = f2;
                }
            }
            return f;
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return 1000.0f / f;
        }

        /* JADX WARN: Code restructure failed: missing block: B:223:0x0740, code lost:
        
            if (r4[2] > r4[3]) goto L187;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x00ae, code lost:
        
            if (r1 != r13) goto L32;
         */
        /* JADX WARN: Removed duplicated region for block: B:64:0x07dd  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x081c A[LOOP:2: B:75:0x081a->B:76:0x081c, LOOP_END] */
        /* JADX WARN: Type inference failed for: r14v17 */
        /* JADX WARN: Type inference failed for: r14v19, types: [int, boolean] */
        /* JADX WARN: Type inference failed for: r14v2 */
        /* JADX WARN: Type inference failed for: r14v20 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            ?? r14;
            int i2;
            int i3;
            int i4;
            int i5;
            float[] fArr;
            int i6;
            int i7;
            float f;
            int i8;
            int i9;
            float f2;
            ArrayList arrayList;
            int i10;
            int i11;
            int i12;
            float f3;
            int i13;
            int i14;
            int i15;
            MessageObject.GroupedMessagePosition groupedMessagePosition;
            int i16;
            int i17;
            int i18;
            int i19;
            float round;
            int i20;
            int i21;
            int i22;
            MessageObject.GroupedMessagePosition groupedMessagePosition2;
            int i23;
            int i24;
            int i25;
            boolean z;
            GroupCalculator groupCalculator = this;
            int i26 = 8;
            int size = groupCalculator.photos.size();
            groupCalculator.posArray.clear();
            groupCalculator.positions.clear();
            int i27 = 0;
            if (size == 0) {
                groupCalculator.width = 0;
                groupCalculator.height = 0.0f;
                groupCalculator.maxX = 0;
                groupCalculator.maxY = 0;
                return;
            }
            groupCalculator.posArray.ensureCapacity(size);
            char[] cArr = new char[size];
            int i28 = 0;
            float f4 = 1.0f;
            boolean z2 = false;
            while (i28 < size) {
                MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) groupCalculator.photos.get(i28);
                MessageObject.GroupedMessagePosition groupedMessagePosition3 = new MessageObject.GroupedMessagePosition();
                groupedMessagePosition3.last = i28 == size + (-1);
                MediaController.CropState cropState = photoEntry.cropState;
                int i29 = cropState != null ? cropState.width : photoEntry.width;
                int i30 = cropState != null ? cropState.height : photoEntry.height;
                if (ChatAttachAlertPhotoLayoutPreview.photoRotate.containsKey(photoEntry)) {
                    z = ((Boolean) ChatAttachAlertPhotoLayoutPreview.photoRotate.get(photoEntry)).booleanValue();
                } else {
                    if (photoEntry.isVideo) {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(photoEntry.path);
                        String extractMetadata = mediaMetadataRetriever.extractMetadata(24);
                        if (extractMetadata != null) {
                            if (!extractMetadata.equals("90")) {
                                if (extractMetadata.equals("270")) {
                                }
                            }
                        }
                        z = false;
                        ChatAttachAlertPhotoLayoutPreview.photoRotate.put(photoEntry, Boolean.valueOf(z));
                    } else {
                        int attributeInt = new ExifInterface(photoEntry.path).getAttributeInt("Orientation", 1);
                        if (attributeInt != 6) {
                        }
                    }
                    z = true;
                    ChatAttachAlertPhotoLayoutPreview.photoRotate.put(photoEntry, Boolean.valueOf(z));
                }
                if (z) {
                    int i31 = i29;
                    i29 = i30;
                    i30 = i31;
                }
                float f5 = i29 / i30;
                groupedMessagePosition3.aspectRatio = f5;
                cArr[i28] = f5 > 1.2f ? 'w' : f5 < 0.8f ? 'n' : 'q';
                f4 += f5;
                if (f5 > 2.0f) {
                    z2 = true;
                }
                groupCalculator.positions.put(photoEntry, groupedMessagePosition3);
                groupCalculator.posArray.add(groupedMessagePosition3);
                i28++;
                i26 = 8;
            }
            String str = new String(cArr);
            int dp = AndroidUtilities.dp(120.0f);
            float dp2 = AndroidUtilities.dp(120.0f);
            android.graphics.Point point = AndroidUtilities.displaySize;
            int min = (int) (dp2 / (Math.min(point.x, point.y) / 1000.0f));
            float dp3 = AndroidUtilities.dp(40.0f);
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            int min2 = (int) (dp3 / (Math.min(point2.x, point2.y) / 1000.0f));
            float f6 = f4 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
            if (size != 1) {
                if (z2 || !(size == 2 || size == 3 || size == 4)) {
                    int size2 = groupCalculator.posArray.size();
                    float[] fArr2 = new float[size2];
                    for (int i32 = 0; i32 < size; i32++) {
                        if (f6 > 1.1f) {
                            fArr2[i32] = Math.max(1.0f, ((MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(i32)).aspectRatio);
                        } else {
                            fArr2[i32] = Math.min(1.0f, ((MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(i32)).aspectRatio);
                        }
                        fArr2[i32] = Math.max(0.66667f, Math.min(1.7f, fArr2[i32]));
                    }
                    ArrayList arrayList2 = new ArrayList();
                    for (int i33 = 1; i33 < size2; i33 = i10 + 1) {
                        int i34 = size2 - i33;
                        if (i33 > 3) {
                            i10 = i33;
                        } else if (i34 > 3) {
                            i10 = i33;
                        } else {
                            i10 = i33;
                            arrayList2.add(new MessageGroupedLayoutAttempt(i33, i34, groupCalculator.multiHeight(fArr2, 0, i33), groupCalculator.multiHeight(fArr2, i33, size2)));
                        }
                    }
                    int i35 = 1;
                    while (i35 < size2 - 1) {
                        int i36 = 1;
                        while (true) {
                            int i37 = size2 - i35;
                            if (i36 < i37) {
                                int i38 = i37 - i36;
                                if (i35 <= 3) {
                                    if (i36 <= (f6 < 0.85f ? 4 : 3)) {
                                        if (i38 > 3) {
                                            i8 = i36;
                                            i9 = size;
                                            f2 = f6;
                                            arrayList = arrayList2;
                                            i36 = i8 + 1;
                                            arrayList2 = arrayList;
                                            f6 = f2;
                                            size = i9;
                                            i27 = 0;
                                        } else {
                                            int i39 = i35 + i36;
                                            i9 = size;
                                            i8 = i36;
                                            f2 = f6;
                                            arrayList = arrayList2;
                                            arrayList.add(new MessageGroupedLayoutAttempt(i35, i36, i38, groupCalculator.multiHeight(fArr2, i27, i35), groupCalculator.multiHeight(fArr2, i35, i39), groupCalculator.multiHeight(fArr2, i39, size2)));
                                            i36 = i8 + 1;
                                            arrayList2 = arrayList;
                                            f6 = f2;
                                            size = i9;
                                            i27 = 0;
                                        }
                                    }
                                }
                                i8 = i36;
                                i9 = size;
                                f2 = f6;
                                arrayList = arrayList2;
                                i36 = i8 + 1;
                                arrayList2 = arrayList;
                                f6 = f2;
                                size = i9;
                                i27 = 0;
                            }
                        }
                        i35++;
                        f6 = f6;
                        i27 = 0;
                    }
                    ArrayList arrayList3 = arrayList2;
                    int i40 = size;
                    int i41 = 1;
                    while (i41 < size2 - 2) {
                        int i42 = 1;
                        while (true) {
                            int i43 = size2 - i41;
                            if (i42 < i43) {
                                int i44 = 1;
                                while (true) {
                                    int i45 = i43 - i42;
                                    if (i44 < i45) {
                                        int i46 = i45 - i44;
                                        if (i41 > 3 || i42 > 3 || i44 > 3 || i46 > 3) {
                                            i3 = i44;
                                            i4 = i43;
                                            i5 = i42;
                                            fArr = fArr2;
                                            i6 = size2;
                                            i7 = i40;
                                            f = dp4;
                                        } else {
                                            int i47 = i41 + i42;
                                            int i48 = i47 + i44;
                                            i6 = size2;
                                            i3 = i44;
                                            i4 = i43;
                                            i5 = i42;
                                            i7 = i40;
                                            fArr = fArr2;
                                            f = dp4;
                                            arrayList3.add(new MessageGroupedLayoutAttempt(i41, i42, i44, i46, groupCalculator.multiHeight(fArr2, 0, i41), groupCalculator.multiHeight(fArr2, i41, i47), groupCalculator.multiHeight(fArr2, i47, i48), groupCalculator.multiHeight(fArr2, i48, size2)));
                                        }
                                        i44 = i3 + 1;
                                        fArr2 = fArr;
                                        i43 = i4;
                                        i42 = i5;
                                        dp4 = f;
                                        size2 = i6;
                                        i40 = i7;
                                    }
                                }
                                i42++;
                                dp4 = dp4;
                                i40 = i40;
                            }
                        }
                        i41++;
                        dp4 = dp4;
                        i40 = i40;
                    }
                    float[] fArr3 = fArr2;
                    i = i40;
                    float f7 = dp4;
                    r14 = 1;
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                    float f8 = 0.0f;
                    for (int i49 = 0; i49 < arrayList3.size(); i49++) {
                        MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList3.get(i49);
                        float f9 = Float.MAX_VALUE;
                        int i50 = 0;
                        float f10 = 0.0f;
                        while (true) {
                            float[] fArr4 = messageGroupedLayoutAttempt2.heights;
                            if (i50 >= fArr4.length) {
                                break;
                            }
                            float f11 = fArr4[i50];
                            f10 += f11;
                            if (f11 < f9) {
                                f9 = f11;
                            }
                            i50++;
                        }
                        float abs = Math.abs(f10 - 1332.0f);
                        int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                        if (iArr.length > 1) {
                            int i51 = iArr[0];
                            int i52 = iArr[1];
                            if (i51 <= i52 && (iArr.length <= 2 || i52 <= iArr[2])) {
                                if (iArr.length > 3) {
                                }
                            }
                            abs *= 1.2f;
                        }
                        if (f9 < min) {
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
                    int i53 = 0;
                    int i54 = 0;
                    while (true) {
                        int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                        if (i54 >= iArr2.length) {
                            break;
                        }
                        int i55 = iArr2[i54];
                        float f12 = messageGroupedLayoutAttempt.heights[i54];
                        int i56 = 1000;
                        MessageObject.GroupedMessagePosition groupedMessagePosition4 = null;
                        for (int i57 = 0; i57 < i55; i57++) {
                            int i58 = (int) (fArr3[i53] * f12);
                            i56 -= i58;
                            MessageObject.GroupedMessagePosition groupedMessagePosition5 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(i53);
                            int i59 = i54 == 0 ? 4 : 0;
                            if (i54 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                i59 |= 8;
                            }
                            if (i57 == 0) {
                                i59 |= 1;
                                groupedMessagePosition4 = groupedMessagePosition5;
                            }
                            if (i57 == i55 - 1) {
                                i2 = i59 | 2;
                                groupedMessagePosition4 = groupedMessagePosition5;
                            } else {
                                i2 = i59;
                            }
                            groupedMessagePosition5.set(i57, i57, i54, i54, i58, Math.max(f7, f12 / 814.0f), i2);
                            i53++;
                        }
                        float f13 = f7;
                        if (groupedMessagePosition4 != null) {
                            groupedMessagePosition4.pw += i56;
                            groupedMessagePosition4.spanSize += i56;
                        }
                        i54++;
                        f7 = f13;
                    }
                } else if (size == 2) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition6 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(0);
                    MessageObject.GroupedMessagePosition groupedMessagePosition7 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(1);
                    if (str.equals("ww")) {
                        double d = f6;
                        double d2 = 1.2285012f;
                        Double.isNaN(d2);
                        if (d > d2 * 1.4d) {
                            float f14 = groupedMessagePosition6.aspectRatio;
                            float f15 = groupedMessagePosition7.aspectRatio;
                            if (f14 - f15 < 0.2d) {
                                i17 = 1000;
                                i21 = 0;
                                i22 = 0;
                                round = Math.round(Math.min(1000.0f / f14, Math.min(1000.0f / f15, 407.0f))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, 1000, round, 7);
                                i20 = 11;
                                i18 = 1;
                                i19 = 1;
                                groupedMessagePosition2 = groupedMessagePosition7;
                                groupedMessagePosition2.set(i21, i22, i18, i19, i17, round, i20);
                                r14 = 1;
                                groupCalculator = this;
                                i = size;
                            }
                        }
                    }
                    if (str.equals("ww") || str.equals("qq")) {
                        float f16 = 500;
                        i17 = 500;
                        i18 = 0;
                        i19 = 0;
                        round = Math.round(Math.min(f16 / groupedMessagePosition6.aspectRatio, Math.min(f16 / groupedMessagePosition7.aspectRatio, 814.0f))) / 814.0f;
                        groupedMessagePosition6.set(0, 0, 0, 0, 500, round, 13);
                        i20 = 14;
                        i21 = 1;
                        i22 = 1;
                        groupedMessagePosition2 = groupedMessagePosition7;
                        groupedMessagePosition2.set(i21, i22, i18, i19, i17, round, i20);
                        r14 = 1;
                        groupCalculator = this;
                        i = size;
                    } else {
                        float f17 = groupedMessagePosition6.aspectRatio;
                        int max = (int) Math.max(400.0f, Math.round((1000.0f / f17) / ((1.0f / f17) + (1.0f / groupedMessagePosition7.aspectRatio))));
                        int i60 = 1000 - max;
                        if (i60 < min) {
                            max -= min - i60;
                        } else {
                            min = i60;
                        }
                        i19 = 0;
                        i18 = 0;
                        round = Math.min(814.0f, Math.round(Math.min(min / groupedMessagePosition6.aspectRatio, max / groupedMessagePosition7.aspectRatio))) / 814.0f;
                        groupedMessagePosition6.set(0, 0, 0, 0, min, round, 13);
                        i20 = 14;
                        i21 = 1;
                        i22 = 1;
                        groupedMessagePosition2 = groupedMessagePosition7;
                        i17 = max;
                        groupedMessagePosition2.set(i21, i22, i18, i19, i17, round, i20);
                        r14 = 1;
                        groupCalculator = this;
                        i = size;
                    }
                } else {
                    groupCalculator = this;
                    if (size == 3) {
                        MessageObject.GroupedMessagePosition groupedMessagePosition8 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(0);
                        MessageObject.GroupedMessagePosition groupedMessagePosition9 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(1);
                        MessageObject.GroupedMessagePosition groupedMessagePosition10 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(2);
                        if (str.charAt(0) == 'n') {
                            float f18 = groupedMessagePosition9.aspectRatio;
                            float min3 = Math.min(407.0f, Math.round((1000.0f * f18) / (groupedMessagePosition10.aspectRatio + f18)));
                            int max2 = (int) Math.max(min, Math.min(500.0f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min3, groupedMessagePosition9.aspectRatio * r13))));
                            int i61 = 1000 - max2;
                            groupedMessagePosition8.set(0, 0, 0, 1, Math.round(Math.min((groupedMessagePosition8.aspectRatio * 814.0f) + min2, i61)), 1.0f, 13);
                            float f19 = (814.0f - min3) / 814.0f;
                            groupedMessagePosition9.set(1, 1, 0, 0, max2, f19, 6);
                            float f20 = min3 / 814.0f;
                            groupedMessagePosition10.set(1, 1, 1, 1, max2, f20, 10);
                            groupedMessagePosition10.spanSize = 1000;
                            groupedMessagePosition8.siblingHeights = new float[]{f20, f19};
                            groupedMessagePosition8.spanSize = i61;
                        } else {
                            float round2 = Math.round(Math.min(1000.0f / groupedMessagePosition8.aspectRatio, 537.24005f)) / 814.0f;
                            groupedMessagePosition8.set(0, 1, 0, 0, 1000, round2, 7);
                            float f21 = 500;
                            float min4 = Math.min(814.0f - round2, Math.round(Math.min(f21 / groupedMessagePosition9.aspectRatio, f21 / groupedMessagePosition10.aspectRatio))) / 814.0f;
                            if (min4 < dp4) {
                                min4 = dp4;
                            }
                            i11 = 1;
                            i12 = 1;
                            i16 = 500;
                            f3 = min4;
                            groupedMessagePosition9.set(0, 0, 1, 1, 500, f3, 9);
                            i13 = 10;
                            i14 = 1;
                            i15 = 1;
                            groupedMessagePosition = groupedMessagePosition10;
                            groupedMessagePosition.set(i14, i15, i12, i11, i16, f3, i13);
                        }
                    } else {
                        MessageObject.GroupedMessagePosition groupedMessagePosition11 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(0);
                        MessageObject.GroupedMessagePosition groupedMessagePosition12 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(1);
                        MessageObject.GroupedMessagePosition groupedMessagePosition13 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(2);
                        MessageObject.GroupedMessagePosition groupedMessagePosition14 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(3);
                        if (str.charAt(0) == 'w') {
                            float round3 = Math.round(Math.min(1000.0f / groupedMessagePosition11.aspectRatio, 537.24005f)) / 814.0f;
                            groupedMessagePosition11.set(0, 2, 0, 0, 1000, round3, 7);
                            float round4 = Math.round(1000.0f / ((groupedMessagePosition12.aspectRatio + groupedMessagePosition13.aspectRatio) + groupedMessagePosition14.aspectRatio));
                            float f22 = min;
                            int max3 = (int) Math.max(f22, Math.min(400.0f, groupedMessagePosition12.aspectRatio * round4));
                            int max4 = (int) Math.max(Math.max(f22, 330.0f), groupedMessagePosition14.aspectRatio * round4);
                            int i62 = (1000 - max3) - max4;
                            if (i62 < AndroidUtilities.dp(58.0f)) {
                                int dp5 = AndroidUtilities.dp(58.0f) - i62;
                                i62 = AndroidUtilities.dp(58.0f);
                                int i63 = dp5 / 2;
                                max3 -= i63;
                                max4 -= dp5 - i63;
                            }
                            int i64 = max3;
                            float min5 = Math.min(814.0f - round3, round4) / 814.0f;
                            if (min5 < dp4) {
                                min5 = dp4;
                            }
                            i11 = 1;
                            i12 = 1;
                            f3 = min5;
                            groupedMessagePosition12.set(0, 0, 1, 1, i64, f3, 9);
                            groupedMessagePosition13.set(1, 1, 1, 1, i62, f3, 8);
                            i13 = 10;
                            i14 = 2;
                            i15 = 2;
                            groupedMessagePosition = groupedMessagePosition14;
                            i16 = max4;
                            groupedMessagePosition.set(i14, i15, i12, i11, i16, f3, i13);
                        } else {
                            int max5 = Math.max(min, Math.round(814.0f / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                            float f23 = dp;
                            float f24 = max5;
                            float min6 = Math.min(0.33f, Math.max(f23, f24 / groupedMessagePosition12.aspectRatio) / 814.0f);
                            float min7 = Math.min(0.33f, Math.max(f23, f24 / groupedMessagePosition13.aspectRatio) / 814.0f);
                            float f25 = (1.0f - min6) - min7;
                            int i65 = 1000 - max5;
                            groupedMessagePosition11.set(0, 0, 0, 2, Math.round(Math.min((groupedMessagePosition11.aspectRatio * 814.0f) + min2, i65)), min6 + min7 + f25, 13);
                            groupedMessagePosition12.set(1, 1, 0, 0, max5, min6, 6);
                            groupedMessagePosition13.set(1, 1, 1, 1, max5, min7, 2);
                            groupedMessagePosition13.spanSize = 1000;
                            groupedMessagePosition14.set(1, 1, 2, 2, max5, f25, 10);
                            groupedMessagePosition14.spanSize = 1000;
                            groupedMessagePosition11.spanSize = i65;
                            groupedMessagePosition11.siblingHeights = new float[]{min6, min7, f25};
                        }
                    }
                }
                i23 = i;
                for (i24 = 0; i24 < i23; i24 += r14) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition15 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(i24);
                    if (groupedMessagePosition15.minX == 0) {
                        groupedMessagePosition15.spanSize += NotificationCenter.storyQualityUpdate;
                    }
                    if ((groupedMessagePosition15.flags & 2) != 0) {
                        groupedMessagePosition15.edge = r14;
                    }
                    groupCalculator.maxX = Math.max(groupCalculator.maxX, (int) groupedMessagePosition15.maxX);
                    groupCalculator.maxY = Math.max(groupCalculator.maxY, (int) groupedMessagePosition15.maxY);
                    groupedMessagePosition15.left = groupCalculator.getLeft(groupedMessagePosition15, groupedMessagePosition15.minY, groupedMessagePosition15.maxY, groupedMessagePosition15.minX);
                }
                for (i25 = 0; i25 < i23; i25 += r14) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition16 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(i25);
                    groupedMessagePosition16.top = groupCalculator.getTop(groupedMessagePosition16, groupedMessagePosition16.minY);
                }
                groupCalculator.width = getWidth();
                groupCalculator.height = getHeight();
            }
            MessageObject.GroupedMessagePosition groupedMessagePosition17 = (MessageObject.GroupedMessagePosition) groupCalculator.posArray.get(0);
            int backgroundPaddingLeft = AndroidUtilities.displaySize.x - (ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getBackgroundPaddingLeft() * 2);
            android.graphics.Point point3 = AndroidUtilities.displaySize;
            groupedMessagePosition17.set(0, 0, 0, 0, 800, ((backgroundPaddingLeft * 0.8f) / groupedMessagePosition17.aspectRatio) / (Math.max(point3.x, point3.y) * 0.5f), 15);
            i = size;
            r14 = 1;
            i23 = i;
            while (i24 < i23) {
            }
            while (i25 < i23) {
            }
            groupCalculator.width = getWidth();
            groupCalculator.height = getHeight();
        }

        public float getHeight() {
            float[] fArr = new float[10];
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = (MessageObject.GroupedMessagePosition) this.posArray.get(i);
                float f = groupedMessagePosition.ph;
                for (int i2 = groupedMessagePosition.minX; i2 <= groupedMessagePosition.maxX; i2++) {
                    fArr[i2] = fArr[i2] + f;
                }
            }
            float f2 = fArr[0];
            for (int i3 = 1; i3 < 10; i3++) {
                float f3 = fArr[i3];
                if (f2 < f3) {
                    f2 = f3;
                }
            }
            return f2;
        }

        public int getWidth() {
            int[] iArr = new int[10];
            Arrays.fill(iArr, 0);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = (MessageObject.GroupedMessagePosition) this.posArray.get(i);
                int i2 = groupedMessagePosition.pw;
                for (int i3 = groupedMessagePosition.minY; i3 <= groupedMessagePosition.maxY; i3++) {
                    iArr[i3] = iArr[i3] + i2;
                }
            }
            int i4 = iArr[0];
            for (int i5 = 1; i5 < 10; i5++) {
                int i6 = iArr[i5];
                if (i4 < i6) {
                    i4 = i6;
                }
            }
            return i4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PreviewGroupsView extends ViewGroup {
        private HashMap deletedPhotos;
        private float draggingT;
        private ArrayList groupCells;
        private ChatActionCell hintView;
        private HashMap images;
        boolean[] lastGroupSeen;
        private int lastMeasuredHeight;
        private int paddingBottom;
        private int paddingTop;
        GroupingPhotoViewerProvider photoViewerProvider;
        HashMap photosMap;
        List photosMapKeys;
        ArrayList photosOrder;
        private float savedDragFromX;
        private float savedDragFromY;
        private float savedDraggingT;
        private final Runnable scroller;
        private boolean scrollerStarted;
        HashMap selectedPhotos;
        PreviewGroupCell tapGroupCell;
        PreviewGroupCell.MediaCell tapMediaCell;
        long tapTime;
        private final Point tmpPoint;
        private int undoViewId;
        float viewBottom;
        float viewTop;

        /* loaded from: classes3.dex */
        class GroupingPhotoViewerProvider extends PhotoViewer.EmptyPhotoViewerProvider {
            private ArrayList photos = new ArrayList();

            GroupingPhotoViewerProvider() {
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean cancelButtonPressed() {
                return false;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int getPhotoIndex(int i) {
                MediaController.PhotoEntry photoEntry;
                if (i < 0 || i >= this.photos.size() || (photoEntry = (MediaController.PhotoEntry) this.photos.get(i)) == null) {
                    return -1;
                }
                return PreviewGroupsView.this.photosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC.FileLocation fileLocation, int i, boolean z) {
                MediaController.PhotoEntry photoEntry;
                ArrayList arrayList;
                PhotoViewer.PlaceProviderObject placeProviderObject = null;
                if (i >= 0 && i < this.photos.size() && isPhotoChecked(i) && (photoEntry = (MediaController.PhotoEntry) this.photos.get(i)) != null) {
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
                                PreviewGroupCell.MediaCell mediaCell2 = (PreviewGroupCell.MediaCell) previewGroupCell.media.get(i3);
                                if (mediaCell2 != null && mediaCell2.photoEntry == photoEntry && mediaCell2.scale > 0.5d) {
                                    mediaCell = (PreviewGroupCell.MediaCell) previewGroupCell.media.get(i3);
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
            public int getSelectedCount() {
                return PreviewGroupsView.this.photosOrder.size();
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public HashMap getSelectedPhotos() {
                return PreviewGroupsView.this.photosMap;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public ArrayList getSelectedPhotosOrder() {
                return PreviewGroupsView.this.photosOrder;
            }

            public void init(ArrayList arrayList) {
                this.photos = arrayList;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean isPhotoChecked(int i) {
                if (i < 0 || i >= this.photos.size()) {
                    return false;
                }
                return PreviewGroupsView.this.photosOrder.contains(Integer.valueOf(((MediaController.PhotoEntry) this.photos.get(i)).imageId));
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void onClose() {
                PreviewGroupsView.this.fromPhotoArrays();
                PreviewGroupsView previewGroupsView = PreviewGroupsView.this;
                previewGroupsView.toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int setPhotoChecked(int i, VideoEditedInfo videoEditedInfo) {
                if (i < 0 || i >= this.photos.size()) {
                    return -1;
                }
                Integer valueOf = Integer.valueOf(((MediaController.PhotoEntry) this.photos.get(i)).imageId);
                int indexOf = PreviewGroupsView.this.photosOrder.indexOf(valueOf);
                if (indexOf < 0) {
                    PreviewGroupsView.this.photosOrder.add(valueOf);
                    PreviewGroupsView.this.fromPhotoArrays();
                    return PreviewGroupsView.this.photosOrder.size() - 1;
                }
                if (PreviewGroupsView.this.photosOrder.size() <= 1) {
                    return -1;
                }
                PreviewGroupsView.this.photosOrder.remove(indexOf);
                PreviewGroupsView.this.fromPhotoArrays();
                return indexOf;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public int setPhotoUnchecked(Object obj) {
                int indexOf;
                Integer valueOf = Integer.valueOf(((MediaController.PhotoEntry) obj).imageId);
                if (PreviewGroupsView.this.photosOrder.size() <= 1 || (indexOf = PreviewGroupsView.this.photosOrder.indexOf(valueOf)) < 0) {
                    return -1;
                }
                PreviewGroupsView.this.photosOrder.remove(indexOf);
                PreviewGroupsView.this.fromPhotoArrays();
                return indexOf;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void updatePhotoAtIndex(int i) {
                MediaController.PhotoEntry photoEntry;
                boolean z;
                if (i < 0 || i >= this.photos.size() || (photoEntry = (MediaController.PhotoEntry) this.photos.get(i)) == null) {
                    return;
                }
                int i2 = photoEntry.imageId;
                PreviewGroupsView.this.invalidate();
                for (int i3 = 0; i3 < PreviewGroupsView.this.groupCells.size(); i3++) {
                    PreviewGroupCell previewGroupCell = (PreviewGroupCell) PreviewGroupsView.this.groupCells.get(i3);
                    if (previewGroupCell != null && previewGroupCell.media != null) {
                        for (int i4 = 0; i4 < previewGroupCell.media.size(); i4++) {
                            PreviewGroupCell.MediaCell mediaCell = (PreviewGroupCell.MediaCell) previewGroupCell.media.get(i4);
                            if (mediaCell != null && mediaCell.photoEntry.imageId == i2) {
                                mediaCell.setImage(photoEntry);
                            }
                        }
                        if (previewGroupCell.group == null || previewGroupCell.group.photos == null) {
                            z = false;
                        } else {
                            z = false;
                            for (int i5 = 0; i5 < previewGroupCell.group.photos.size(); i5++) {
                                if (((MediaController.PhotoEntry) previewGroupCell.group.photos.get(i5)).imageId == i2) {
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

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class PreviewGroupCell {
            private Theme.MessageDrawable.PathDrawParams backgroundCacheParams;
            private float bottom;
            private Text buttonText;
            private Paint buttonTextBgPaint;
            private long buttonTextPrice;
            private RectF buttonTextRect;
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
            public ArrayList media;
            private Theme.MessageDrawable messageBackground;
            final int padding;
            private float previousGroupHeight;
            private float previousGroupWidth;
            private float right;
            public long stars;
            private float top;
            private final long updateDuration;
            private float width;
            public float y;

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: classes3.dex */
            public class MediaCell {
                private Paint bitmapPaint;
                public ImageReceiver blurredImage;
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
                private Path path;
                public MediaController.PhotoEntry photoEntry;
                private int positionFlags;
                private float[] radii;
                public RectF rect;
                public RectF roundRadiuses;
                public float scale;
                private Bitmap spoilerCrossfadeBitmap;
                private Paint spoilerCrossfadePaint;
                private float spoilerCrossfadeProgress;
                private SpoilerEffect2 spoilerEffect;
                private float spoilerMaxRadius;
                private float spoilerRevealProgress;
                private float spoilerRevealX;
                private float spoilerRevealY;
                private Paint strokePaint;
                private RectF tempRect;
                private TextPaint textPaint;
                private final long updateDuration;
                private Bitmap videoDurationBitmap;
                private String videoDurationBitmapText;
                private String videoDurationText;
                private TextPaint videoDurationTextPaint;
                private float visibleT;
                public boolean wasSpoiler;

                private MediaCell() {
                    this.groupCell = PreviewGroupCell.this;
                    this.fromRect = null;
                    this.rect = new RectF();
                    this.lastUpdate = 0L;
                    this.updateDuration = 200L;
                    this.positionFlags = 0;
                    this.fromScale = 1.0f;
                    this.scale = 0.0f;
                    this.fromRoundRadiuses = null;
                    this.roundRadiuses = new RectF();
                    this.videoDurationText = null;
                    this.path = new Path();
                    this.radii = new float[8];
                    this.spoilerCrossfadeProgress = 1.0f;
                    this.spoilerCrossfadePaint = new Paint(1);
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

                private void drawDuration(Canvas canvas, float f, float f2, String str, float f3, float f4) {
                    String str2;
                    if (str != null) {
                        if (this.videoDurationBitmap == null || (str2 = this.videoDurationBitmapText) == null || !str2.equals(str)) {
                            if (this.videoDurationTextPaint == null) {
                                TextPaint textPaint = new TextPaint(1);
                                this.videoDurationTextPaint = textPaint;
                                textPaint.setTypeface(AndroidUtilities.bold());
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
                            textPaint.setTypeface(AndroidUtilities.bold());
                        }
                        TextPaint textPaint2 = this.textPaint;
                        ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = ChatAttachAlertPhotoLayoutPreview.this;
                        int i3 = Theme.key_chat_attachCheckBoxCheck;
                        textPaint2.setColor(chatAttachAlertPhotoLayoutPreview.getThemedColor(i3));
                        int length = str.length();
                        float f5 = (length == 0 || length == 1 || length == 2) ? 14.0f : length != 3 ? 8.0f : 10.0f;
                        this.textPaint.setTextSize(AndroidUtilities.dp(f5));
                        float f6 = i / 2.0f;
                        this.paint.setColor(ChatAttachAlertPhotoLayoutPreview.this.getThemedColor(Theme.key_chat_attachCheckBoxBackground));
                        float f7 = (int) f6;
                        float f8 = dp;
                        canvas2.drawCircle(f7, f7, f8, this.paint);
                        this.strokePaint.setColor(AndroidUtilities.getOffsetColor(-1, ChatAttachAlertPhotoLayoutPreview.this.getThemedColor(i3), 1.0f, 1.0f));
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

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$setImage$0(MediaController.PhotoEntry photoEntry, ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                    if (z && !z2 && photoEntry != null && photoEntry.hasSpoiler && this.blurredImage.getBitmap() == null) {
                        if (this.blurredImage.getBitmap() != null && !this.blurredImage.getBitmap().isRecycled()) {
                            this.blurredImage.getBitmap().recycle();
                            this.blurredImage.setImageBitmap((Bitmap) null);
                        }
                        this.blurredImage.setImageBitmap(Utilities.stackBlurBitmapMax(imageReceiver.getBitmap()));
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$startRevealMedia$1(ValueAnimator valueAnimator) {
                    this.spoilerRevealProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    PreviewGroupsView.this.invalidate();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void layout(GroupCalculator groupCalculator, MessageObject.GroupedMessagePosition groupedMessagePosition, boolean z) {
                    if (groupCalculator == null || groupedMessagePosition == null) {
                        if (!z) {
                            this.fromScale = 0.0f;
                            this.scale = 0.0f;
                            return;
                        }
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
                    float f2 = groupCalculator.width;
                    float f3 = f / f2;
                    float f4 = groupedMessagePosition.top;
                    float f5 = groupCalculator.height;
                    float f6 = f4 / f5;
                    float f7 = groupedMessagePosition.pw / f2;
                    float f8 = groupedMessagePosition.ph / f5;
                    this.scale = 1.0f;
                    this.rect.set(f3, f6, f7 + f3, f8 + f6);
                    float dp = AndroidUtilities.dp(2.0f);
                    float dp2 = AndroidUtilities.dp(SharedConfig.bubbleRadius - 1);
                    RectF rectF4 = this.roundRadiuses;
                    int i = this.positionFlags;
                    float f9 = (i & 5) == 5 ? dp2 : dp;
                    float f10 = (i & 6) == 6 ? dp2 : dp;
                    float f11 = (i & 10) == 10 ? dp2 : dp;
                    if ((i & 9) == 9) {
                        dp = dp2;
                    }
                    rectF4.set(f9, f10, f11, dp);
                    if (this.fromRect == null) {
                        RectF rectF5 = new RectF();
                        this.fromRect = rectF5;
                        rectF5.set(this.rect);
                    }
                    if (this.fromRoundRadiuses == null) {
                        RectF rectF6 = new RectF();
                        this.fromRoundRadiuses = rectF6;
                        rectF6.set(this.roundRadiuses);
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void layoutFrom(MediaCell mediaCell) {
                    RectF rectF;
                    float centerX;
                    float centerY;
                    float centerX2;
                    float centerY2;
                    RectF rectF2;
                    this.fromScale = AndroidUtilities.lerp(mediaCell.fromScale, mediaCell.scale, mediaCell.getT());
                    if (this.fromRect == null) {
                        this.fromRect = new RectF();
                    }
                    RectF rectF3 = new RectF();
                    RectF rectF4 = this.fromRect;
                    if (rectF4 == null) {
                        rectF3.set(this.rect);
                    } else {
                        AndroidUtilities.lerp(rectF4, this.rect, getT(), rectF3);
                    }
                    RectF rectF5 = mediaCell.fromRect;
                    if (rectF5 != null) {
                        AndroidUtilities.lerp(rectF5, mediaCell.rect, mediaCell.getT(), this.fromRect);
                        rectF = this.fromRect;
                        centerX = rectF3.centerX() - (((this.fromRect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width);
                        centerY = rectF3.centerY() - (((this.fromRect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height);
                        centerX2 = rectF3.centerX() + (((this.fromRect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width);
                        centerY2 = rectF3.centerY();
                        rectF2 = this.fromRect;
                    } else {
                        rectF = this.fromRect;
                        centerX = rectF3.centerX() - (((mediaCell.rect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width);
                        centerY = rectF3.centerY() - (((mediaCell.rect.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height);
                        centerX2 = rectF3.centerX() + (((mediaCell.rect.width() / 2.0f) * mediaCell.groupCell.width) / PreviewGroupCell.this.width);
                        centerY2 = rectF3.centerY();
                        rectF2 = mediaCell.rect;
                    }
                    rectF.set(centerX, centerY, centerX2, centerY2 + (((rectF2.height() / 2.0f) * mediaCell.groupCell.height) / PreviewGroupCell.this.height));
                    this.fromScale = AndroidUtilities.lerp(this.fromScale, this.scale, getT());
                    this.lastUpdate = SystemClock.elapsedRealtime();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void setImage(final MediaController.PhotoEntry photoEntry) {
                    this.photoEntry = photoEntry;
                    this.videoDurationText = (photoEntry == null || !photoEntry.isVideo) ? null : AndroidUtilities.formatShortDuration(photoEntry.duration);
                    if (this.image == null) {
                        this.image = new ImageReceiver(PreviewGroupsView.this);
                        this.blurredImage = new ImageReceiver(PreviewGroupsView.this);
                        this.image.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$PreviewGroupCell$MediaCell$$ExternalSyntheticLambda0
                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                                ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.PreviewGroupCell.MediaCell.this.lambda$setImage$0(photoEntry, imageReceiver, z, z2, z3);
                            }

                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public /* synthetic */ void didSetImageBitmap(int i, String str, Drawable drawable) {
                                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i, str, drawable);
                            }

                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
                            }
                        });
                    }
                    if (photoEntry != null) {
                        String str = photoEntry.thumbPath;
                        if (str != null) {
                            this.image.setImage(ImageLocation.getForPath(str), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                            return;
                        }
                        if (photoEntry.path == null) {
                            this.image.setImageBitmap(Theme.chat_attachEmptyDrawable);
                            return;
                        }
                        if (photoEntry.isVideo) {
                            this.image.setImage(ImageLocation.getForPath("vthumb://" + photoEntry.imageId + ":" + photoEntry.path), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                            this.image.setAllowStartAnimation(true);
                            return;
                        }
                        this.image.setOrientation(photoEntry.orientation, true);
                        this.image.setImage(ImageLocation.getForPath("thumb://" + photoEntry.imageId + ":" + photoEntry.path), null, null, null, Theme.chat_attachEmptyDrawable, 0L, null, null, 0);
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void startRevealMedia(float f, float f2) {
                    this.spoilerRevealX = f;
                    this.spoilerRevealY = f2;
                    RectF drawingRect = drawingRect();
                    this.spoilerMaxRadius = (float) Math.sqrt(Math.pow(drawingRect.width(), 2.0d) + Math.pow(drawingRect.height(), 2.0d));
                    ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(MathUtils.clamp(this.spoilerMaxRadius * 0.3f, 250.0f, 550.0f));
                    duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                    duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$PreviewGroupCell$MediaCell$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.PreviewGroupCell.MediaCell.this.lambda$startRevealMedia$1(valueAnimator);
                        }
                    });
                    duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.PreviewGroupCell.MediaCell.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            MediaCell mediaCell = MediaCell.this;
                            mediaCell.photoEntry.isChatPreviewSpoilerRevealed = true;
                            PreviewGroupsView.this.invalidate();
                        }
                    });
                    duration.start();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public MediaCell clone() {
                    MediaCell mediaCell = new MediaCell();
                    mediaCell.rect.set(this.rect);
                    mediaCell.image = this.image;
                    mediaCell.photoEntry = this.photoEntry;
                    return mediaCell;
                }

                public void detach() {
                    SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
                    if (spoilerEffect2 != null) {
                        spoilerEffect2.detach(PreviewGroupsView.this);
                        this.spoilerEffect = null;
                    }
                }

                public boolean draw(Canvas canvas) {
                    return draw(canvas, false);
                }

                /* JADX WARN: Removed duplicated region for block: B:36:0x0139  */
                /* JADX WARN: Removed duplicated region for block: B:39:0x017f  */
                /* JADX WARN: Removed duplicated region for block: B:49:0x024e  */
                /* JADX WARN: Removed duplicated region for block: B:52:0x026d  */
                /* JADX WARN: Removed duplicated region for block: B:55:0x027f  */
                /* JADX WARN: Removed duplicated region for block: B:57:0x0284  */
                /* JADX WARN: Removed duplicated region for block: B:63:0x02f4  */
                /* JADX WARN: Removed duplicated region for block: B:69:0x0281  */
                /* JADX WARN: Removed duplicated region for block: B:70:0x0270  */
                /* JADX WARN: Removed duplicated region for block: B:71:0x0263  */
                /* JADX WARN: Removed duplicated region for block: B:77:0x0157  */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public boolean draw(Canvas canvas, float f, boolean z) {
                    float f2;
                    Bitmap bitmap;
                    int indexOf;
                    String str;
                    boolean z2;
                    char c;
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
                    int i = (int) lerp2;
                    int i2 = (int) lerp3;
                    int i3 = (int) lerp4;
                    int i4 = (int) lerp5;
                    this.image.setRoundRadius(i, i2, i3, i4);
                    this.image.setImageCoords(drawingRect.left, drawingRect.top, drawingRect.width(), drawingRect.height());
                    this.image.setAlpha(lerp);
                    this.image.draw(canvas);
                    MediaController.PhotoEntry photoEntry = this.photoEntry;
                    if (photoEntry == null || !photoEntry.hasSpoiler || photoEntry.isChatPreviewSpoilerRevealed) {
                        f2 = lerp5;
                    } else {
                        if (!this.wasSpoiler && this.blurredImage.getBitmap() == null && this.image.getBitmap() != null) {
                            this.wasSpoiler = true;
                            this.blurredImage.setImageBitmap(Utilities.stackBlurBitmapMax(this.image.getBitmap()));
                        } else if (!this.wasSpoiler && this.blurredImage.getBitmap() != null) {
                            c = 1;
                            this.wasSpoiler = true;
                            float[] fArr = this.radii;
                            fArr[c] = lerp2;
                            fArr[0] = lerp2;
                            fArr[3] = lerp3;
                            fArr[2] = lerp3;
                            fArr[5] = lerp4;
                            fArr[4] = lerp4;
                            fArr[7] = lerp5;
                            fArr[6] = lerp5;
                            canvas.save();
                            this.path.rewind();
                            Path path = this.path;
                            float[] fArr2 = this.radii;
                            Path.Direction direction = Path.Direction.CW;
                            path.addRoundRect(drawingRect, fArr2, direction);
                            canvas.clipPath(this.path);
                            if (this.spoilerRevealProgress == 0.0f) {
                                this.path.rewind();
                                f2 = lerp5;
                                this.path.addCircle(this.spoilerRevealX, this.spoilerRevealY, this.spoilerMaxRadius * this.spoilerRevealProgress, direction);
                                canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                            } else {
                                f2 = lerp5;
                            }
                            this.blurredImage.setRoundRadius(i, i2, i3, i4);
                            this.blurredImage.setImageCoords(drawingRect.left, drawingRect.top, drawingRect.width(), drawingRect.height());
                            this.blurredImage.setAlpha(lerp);
                            this.blurredImage.draw(canvas);
                            if (this.spoilerEffect == null) {
                                this.spoilerEffect = SpoilerEffect2.getInstance(PreviewGroupsView.this);
                            }
                            SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
                            PreviewGroupsView previewGroupsView = PreviewGroupsView.this;
                            spoilerEffect2.draw(canvas, previewGroupsView, previewGroupsView.getWidth(), PreviewGroupsView.this.getHeight());
                            canvas.restore();
                            PreviewGroupsView.this.invalidate();
                            PreviewGroupsView.this.invalidate();
                        }
                        c = 1;
                        float[] fArr3 = this.radii;
                        fArr3[c] = lerp2;
                        fArr3[0] = lerp2;
                        fArr3[3] = lerp3;
                        fArr3[2] = lerp3;
                        fArr3[5] = lerp4;
                        fArr3[4] = lerp4;
                        fArr3[7] = lerp5;
                        fArr3[6] = lerp5;
                        canvas.save();
                        this.path.rewind();
                        Path path2 = this.path;
                        float[] fArr22 = this.radii;
                        Path.Direction direction2 = Path.Direction.CW;
                        path2.addRoundRect(drawingRect, fArr22, direction2);
                        canvas.clipPath(this.path);
                        if (this.spoilerRevealProgress == 0.0f) {
                        }
                        this.blurredImage.setRoundRadius(i, i2, i3, i4);
                        this.blurredImage.setImageCoords(drawingRect.left, drawingRect.top, drawingRect.width(), drawingRect.height());
                        this.blurredImage.setAlpha(lerp);
                        this.blurredImage.draw(canvas);
                        if (this.spoilerEffect == null) {
                        }
                        SpoilerEffect2 spoilerEffect22 = this.spoilerEffect;
                        PreviewGroupsView previewGroupsView2 = PreviewGroupsView.this;
                        spoilerEffect22.draw(canvas, previewGroupsView2, previewGroupsView2.getWidth(), PreviewGroupsView.this.getHeight());
                        canvas.restore();
                        PreviewGroupsView.this.invalidate();
                        PreviewGroupsView.this.invalidate();
                    }
                    float f8 = this.spoilerCrossfadeProgress;
                    if (f8 == 1.0f || this.spoilerCrossfadeBitmap == null) {
                        if (f8 == 1.0f && (bitmap = this.spoilerCrossfadeBitmap) != null) {
                            bitmap.recycle();
                            this.spoilerCrossfadeBitmap = null;
                        }
                        PreviewGroupCell previewGroupCell = PreviewGroupCell.this;
                        indexOf = previewGroupCell.indexStart + previewGroupCell.group.photos.indexOf(this.photoEntry);
                        if (indexOf < 0) {
                            str = (indexOf + 1) + "";
                        } else {
                            str = null;
                        }
                        float f9 = !this.image.getVisible() ? 1.0f : 0.0f;
                        z2 = Math.abs(this.visibleT - f9) <= 0.01f;
                        if (z2) {
                            long min = Math.min(17L, SystemClock.elapsedRealtime() - this.lastVisibleTUpdate);
                            this.lastVisibleTUpdate = SystemClock.elapsedRealtime();
                            float f10 = ((float) min) / 100.0f;
                            float f11 = this.visibleT;
                            if (f9 < f11) {
                                this.visibleT = Math.max(0.0f, f11 - f10);
                            } else {
                                this.visibleT = Math.min(1.0f, f11 + f10);
                            }
                        }
                        drawPhotoIndex(canvas, AndroidUtilities.dp(10.0f) + drawingRect.top, drawingRect.right - AndroidUtilities.dp(10.0f), str, lerp, lerp * this.visibleT);
                        drawDuration(canvas, AndroidUtilities.dp(4.0f) + drawingRect.left, drawingRect.bottom - AndroidUtilities.dp(4.0f), this.videoDurationText, lerp, lerp * this.visibleT);
                        if (z) {
                            canvas.restore();
                        }
                        return f >= 1.0f || z2;
                    }
                    float[] fArr4 = this.radii;
                    fArr4[1] = lerp2;
                    fArr4[0] = lerp2;
                    fArr4[3] = lerp3;
                    fArr4[2] = lerp3;
                    fArr4[5] = lerp4;
                    fArr4[4] = lerp4;
                    fArr4[7] = f2;
                    fArr4[6] = f2;
                    canvas.save();
                    this.path.rewind();
                    this.path.addRoundRect(drawingRect, this.radii, Path.Direction.CW);
                    canvas.clipPath(this.path);
                    float min2 = Math.min(1.0f, this.spoilerCrossfadeProgress + (((float) Math.min(16L, SystemClock.elapsedRealtime() - this.lastUpdate)) / 250.0f));
                    this.spoilerCrossfadeProgress = min2;
                    this.spoilerCrossfadePaint.setAlpha((int) ((1.0f - min2) * 255.0f));
                    canvas.drawBitmap(this.spoilerCrossfadeBitmap, drawingRect.left, drawingRect.top, this.spoilerCrossfadePaint);
                    canvas.restore();
                    PreviewGroupsView.this.invalidate();
                    PreviewGroupCell previewGroupCell2 = PreviewGroupCell.this;
                    indexOf = previewGroupCell2.indexStart + previewGroupCell2.group.photos.indexOf(this.photoEntry);
                    if (indexOf < 0) {
                    }
                    if (!this.image.getVisible()) {
                    }
                    if (Math.abs(this.visibleT - f9) <= 0.01f) {
                    }
                    if (z2) {
                    }
                    drawPhotoIndex(canvas, AndroidUtilities.dp(10.0f) + drawingRect.top, drawingRect.right - AndroidUtilities.dp(10.0f), str, lerp, lerp * this.visibleT);
                    drawDuration(canvas, AndroidUtilities.dp(4.0f) + drawingRect.left, drawingRect.bottom - AndroidUtilities.dp(4.0f), this.videoDurationText, lerp, lerp * this.visibleT);
                    if (z) {
                    }
                    if (f >= 1.0f) {
                    }
                }

                public boolean draw(Canvas canvas, boolean z) {
                    return draw(canvas, getT(), z);
                }

                public RectF drawingRect() {
                    float f = 0.0f;
                    if (this.rect == null || this.image == null) {
                        this.tempRect.set(0.0f, 0.0f, 0.0f, 0.0f);
                        return this.tempRect;
                    }
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

                public float getT() {
                    return PreviewGroupCell.this.interpolator.getInterpolation(Math.min(1.0f, ((float) (SystemClock.elapsedRealtime() - this.lastUpdate)) / 200.0f));
                }

                public RectF rect() {
                    return rect(getT());
                }

                public RectF rect(float f) {
                    if (this.rect == null || this.image == null) {
                        this.tempRect.set(0.0f, 0.0f, 0.0f, 0.0f);
                    } else {
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
                            float f4 = PreviewGroupCell.this.halfGap;
                            f3 += f4;
                            height -= f4;
                        }
                        if ((i & 8) == 0) {
                            height -= PreviewGroupCell.this.halfGap;
                        }
                        if ((i & 1) == 0) {
                            float f5 = PreviewGroupCell.this.halfGap;
                            f2 += f5;
                            width -= f5;
                        }
                        if ((i & 2) == 0) {
                            width -= PreviewGroupCell.this.halfGap;
                        }
                        this.tempRect.set(f2, f3, width + f2, height + f3);
                    }
                    return this.tempRect;
                }

                public void startCrossfade() {
                    RectF drawingRect = drawingRect();
                    Bitmap createBitmap = Bitmap.createBitmap(Math.max(1, Math.round(drawingRect.width())), Math.max(1, Math.round(drawingRect.height())), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.save();
                    canvas.translate(-drawingRect.left, -drawingRect.top);
                    draw(canvas);
                    canvas.restore();
                    Bitmap bitmap = this.spoilerCrossfadeBitmap;
                    if (bitmap != null && !bitmap.isRecycled()) {
                        this.spoilerCrossfadeBitmap.recycle();
                    }
                    this.spoilerCrossfadeBitmap = createBitmap;
                    this.spoilerCrossfadeProgress = 0.0f;
                    PreviewGroupsView.this.invalidate();
                }
            }

            private PreviewGroupCell() {
                this.y = 0.0f;
                this.indexStart = 0;
                this.updateDuration = 200L;
                this.lastMediaUpdate = 0L;
                this.groupWidth = 0.0f;
                this.groupHeight = 0.0f;
                this.previousGroupWidth = 0.0f;
                this.previousGroupHeight = 0.0f;
                this.media = new ArrayList();
                this.interpolator = CubicBezierInterpolator.EASE_BOTH;
                this.padding = AndroidUtilities.dp(4.0f);
                int dp = AndroidUtilities.dp(2.0f);
                this.gap = dp;
                this.halfGap = dp / 2;
                this.buttonTextRect = new RectF();
                this.buttonTextBgPaint = new Paint(1);
                this.messageBackground = (Theme.MessageDrawable) ChatAttachAlertPhotoLayoutPreview.this.getThemedDrawable("drawableMsgOutMedia");
                this.backgroundCacheParams = new Theme.MessageDrawable.PathDrawParams();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setGroup(GroupCalculator groupCalculator, boolean z) {
                float f;
                MediaCell mediaCell;
                this.group = groupCalculator;
                if (groupCalculator == null) {
                    return;
                }
                groupCalculator.calculate();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = elapsedRealtime - this.lastMediaUpdate;
                if (j < 200) {
                    float f2 = ((float) j) / 200.0f;
                    this.previousGroupHeight = AndroidUtilities.lerp(this.previousGroupHeight, this.groupHeight, f2);
                    f = AndroidUtilities.lerp(this.previousGroupWidth, this.groupWidth, f2);
                } else {
                    this.previousGroupHeight = this.groupHeight;
                    f = this.groupWidth;
                }
                this.previousGroupWidth = f;
                this.groupWidth = groupCalculator.width / 1000.0f;
                this.groupHeight = groupCalculator.height;
                this.lastMediaUpdate = z ? elapsedRealtime : 0L;
                this.stars = 0L;
                ArrayList arrayList = new ArrayList(groupCalculator.positions.keySet());
                int size = arrayList.size();
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) arrayList.get(i2);
                    MessageObject.GroupedMessagePosition groupedMessagePosition = (MessageObject.GroupedMessagePosition) groupCalculator.positions.get(photoEntry);
                    this.stars = Math.max(this.stars, photoEntry.starsAmount);
                    int size2 = this.media.size();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= size2) {
                            mediaCell = null;
                            break;
                        }
                        mediaCell = (MediaCell) this.media.get(i3);
                        if (mediaCell.photoEntry == photoEntry) {
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (mediaCell == null) {
                        MediaCell mediaCell2 = new MediaCell();
                        mediaCell2.setImage(photoEntry);
                        mediaCell2.layout(groupCalculator, groupedMessagePosition, z);
                        this.media.add(mediaCell2);
                    } else {
                        mediaCell.layout(groupCalculator, groupedMessagePosition, z);
                    }
                    i2++;
                }
                int size3 = this.media.size();
                while (i < size3) {
                    MediaCell mediaCell3 = (MediaCell) this.media.get(i);
                    if (!groupCalculator.positions.containsKey(mediaCell3.photoEntry)) {
                        if (mediaCell3.scale <= 0.0f && mediaCell3.lastUpdate + 200 <= elapsedRealtime) {
                            mediaCell3.detach();
                            this.media.remove(i);
                            i--;
                            size3--;
                        }
                        mediaCell3.layout(null, null, z);
                    }
                    i++;
                }
                PreviewGroupsView.this.invalidate();
            }

            public void detach() {
                for (int i = 0; i < this.media.size(); i++) {
                    ((MediaCell) this.media.get(i)).detach();
                }
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
                    float f3 = this.padding;
                    this.top = f2 + f3;
                    this.left += f3;
                    this.bottom -= f3;
                    this.right -= f3;
                }
                this.width = this.right - this.left;
                this.height = this.bottom - this.top;
                int size = this.media.size();
                for (int i = 0; i < size; i++) {
                    MediaCell mediaCell = (MediaCell) this.media.get(i);
                    if (mediaCell != null && ((ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null || ChatAttachAlertPhotoLayoutPreview.this.draggingCell.photoEntry != mediaCell.photoEntry) && mediaCell.draw(canvas))) {
                        z = true;
                    }
                }
                drawStarsButton(canvas);
                return z;
            }

            public void drawStarsButton(Canvas canvas) {
                long j = this.stars;
                if (j <= 0) {
                    return;
                }
                if (this.buttonText == null || this.buttonTextPrice != j) {
                    this.buttonTextPrice = j;
                    this.buttonText = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) j), 0.7f), 14.0f, AndroidUtilities.bold());
                }
                float dp = AndroidUtilities.dp(28.0f) + this.buttonText.getCurrentWidth();
                float dp2 = AndroidUtilities.dp(32.0f);
                RectF rectF = this.buttonTextRect;
                float f = this.left;
                float f2 = this.width;
                float f3 = this.top;
                float f4 = this.height;
                rectF.set(((f2 - dp) / 2.0f) + f, ((f4 - dp2) / 2.0f) + f3, f + ((f2 + dp) / 2.0f), f3 + ((f4 + dp2) / 2.0f));
                this.buttonTextBgPaint.setColor(1610612736);
                float f5 = dp2 / 2.0f;
                canvas.drawRoundRect(this.buttonTextRect, f5, f5, this.buttonTextBgPaint);
                this.buttonText.draw(canvas, ((this.left + (this.width / 2.0f)) - (dp / 2.0f)) + AndroidUtilities.dp(14.0f), this.top + (this.height / 2.0f), -1, 1.0f);
            }

            public float getT() {
                return this.interpolator.getInterpolation(Math.min(1.0f, ((float) (SystemClock.elapsedRealtime() - this.lastMediaUpdate)) / 200.0f));
            }

            public float measure() {
                android.graphics.Point point = AndroidUtilities.displaySize;
                return AndroidUtilities.lerp(this.previousGroupHeight, this.groupHeight, getT()) * Math.max(point.x, point.y) * 0.5f * ChatAttachAlertPhotoLayoutPreview.this.getPreviewScale();
            }
        }

        public PreviewGroupsView(Context context) {
            super(context);
            this.groupCells = new ArrayList();
            this.deletedPhotos = new HashMap();
            this.paddingTop = AndroidUtilities.dp(16.0f);
            this.paddingBottom = AndroidUtilities.dp(64.0f);
            this.lastMeasuredHeight = 0;
            this.lastGroupSeen = null;
            this.tapTime = 0L;
            this.tapGroupCell = null;
            this.tapMediaCell = null;
            this.draggingT = 0.0f;
            this.tmpPoint = new Point();
            this.scrollerStarted = false;
            this.scroller = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.2
                @Override // java.lang.Runnable
                public void run() {
                    if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null || ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                        return;
                    }
                    int computeVerticalScrollOffset = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollOffset();
                    boolean z = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollExtent() + computeVerticalScrollOffset >= (PreviewGroupsView.this.measurePureHeight() - PreviewGroupsView.this.paddingBottom) + PreviewGroupsView.this.paddingTop;
                    float max = Math.max(0.0f, (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - Math.max(0, computeVerticalScrollOffset - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding())) - AndroidUtilities.dp(52.0f));
                    float max2 = Math.max(0.0f, ((ChatAttachAlertPhotoLayoutPreview.this.listView.getMeasuredHeight() - (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - computeVerticalScrollOffset)) - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) - AndroidUtilities.dp(84.0f));
                    float dp = AndroidUtilities.dp(32.0f);
                    float dp2 = (max >= dp || computeVerticalScrollOffset <= ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) ? max2 < dp ? AndroidUtilities.dp(6.0f) * (1.0f - (max2 / dp)) : 0.0f : (-(1.0f - (max / dp))) * AndroidUtilities.dp(6.0f);
                    int i = (int) dp2;
                    if (Math.abs(i) > 0 && ChatAttachAlertPhotoLayoutPreview.this.listView.canScrollVertically(i) && (dp2 <= 0.0f || !z)) {
                        ChatAttachAlertPhotoLayoutPreview.access$1416(ChatAttachAlertPhotoLayoutPreview.this, dp2);
                        ChatAttachAlertPhotoLayoutPreview.this.listView.scrollBy(0, i);
                        PreviewGroupsView.this.invalidate();
                    }
                    PreviewGroupsView.this.scrollerStarted = true;
                    PreviewGroupsView.this.postDelayed(this, 15L);
                }
            };
            this.photoViewerProvider = new GroupingPhotoViewerProvider();
            this.undoViewId = 0;
            this.images = new HashMap();
            setWillNotDraw(false);
            ChatActionCell chatActionCell = new ChatActionCell(context, true, ChatAttachAlertPhotoLayoutPreview.this.themeDelegate);
            this.hintView = chatActionCell;
            chatActionCell.setCustomText(LocaleController.getString(R.string.AttachMediaDragHint));
            addView(this.hintView);
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
                float measure = ((PreviewGroupCell) this.groupCells.get(i)).measure() + f;
                zArr[i] = isSeen(f, measure);
                i++;
                f = measure;
            }
            return zArr;
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
            if (i == this.undoViewId && ChatAttachAlertPhotoLayoutPreview.this.undoView.isShown()) {
                ChatAttachAlertPhotoLayoutPreview.this.undoView.hide(true, 1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startDragging$1(ValueAnimator valueAnimator) {
            this.draggingT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$stopDragging$0(ValueAnimator valueAnimator) {
            this.draggingT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        private int measureHeight() {
            return Math.max(measurePureHeight(), (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(45.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int measurePureHeight() {
            int i = this.paddingTop + this.paddingBottom;
            int size = this.groupCells.size();
            for (int i2 = 0; i2 < size; i2++) {
                i = (int) (i + ((PreviewGroupCell) this.groupCells.get(i2)).measure());
            }
            if (this.hintView.getMeasuredHeight() <= 0) {
                this.hintView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), View.MeasureSpec.makeMeasureSpec(9999, Integer.MIN_VALUE));
            }
            return i + this.hintView.getMeasuredHeight();
        }

        private void pushToGroup(PreviewGroupCell previewGroupCell, MediaController.PhotoEntry photoEntry, int i) {
            previewGroupCell.group.photos.add(Math.min(previewGroupCell.group.photos.size(), i), photoEntry);
            if (previewGroupCell.group.photos.size() == 11) {
                MediaController.PhotoEntry photoEntry2 = (MediaController.PhotoEntry) previewGroupCell.group.photos.get(10);
                previewGroupCell.group.photos.remove(10);
                int indexOf = this.groupCells.indexOf(previewGroupCell);
                if (indexOf >= 0) {
                    int i2 = indexOf + 1;
                    PreviewGroupCell previewGroupCell2 = i2 == this.groupCells.size() ? null : (PreviewGroupCell) this.groupCells.get(i2);
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
                PreviewGroupCell previewGroupCell = (PreviewGroupCell) this.groupCells.get(i);
                if (previewGroupCell.group.photos.size() < 10 && i < this.groupCells.size() - 1) {
                    int size2 = 10 - previewGroupCell.group.photos.size();
                    PreviewGroupCell previewGroupCell2 = (PreviewGroupCell) this.groupCells.get(i + 1);
                    ArrayList arrayList = new ArrayList();
                    int min = Math.min(size2, previewGroupCell2.group.photos.size());
                    for (int i2 = 0; i2 < min; i2++) {
                        arrayList.add((MediaController.PhotoEntry) previewGroupCell2.group.photos.remove(0));
                    }
                    previewGroupCell.group.photos.addAll(arrayList);
                    previewGroupCell.setGroup(previewGroupCell.group, true);
                    previewGroupCell2.setGroup(previewGroupCell2.group, true);
                }
            }
        }

        public void calcPhotoArrays() {
            Map.Entry entry;
            Object value;
            String str;
            this.photosMap = ChatAttachAlertPhotoLayoutPreview.this.photoLayout.getSelectedPhotos();
            this.photosMapKeys = new ArrayList(this.photosMap.entrySet());
            this.selectedPhotos = new HashMap();
            this.photosOrder = new ArrayList();
            int size = this.groupCells.size();
            for (int i = 0; i < size; i++) {
                GroupCalculator groupCalculator = ((PreviewGroupCell) this.groupCells.get(i)).group;
                if (groupCalculator.photos.size() != 0) {
                    int size2 = groupCalculator.photos.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) groupCalculator.photos.get(i2);
                        if (!this.deletedPhotos.containsKey(photoEntry)) {
                            int i3 = 0;
                            while (true) {
                                if (i3 < this.photosMapKeys.size()) {
                                    entry = (Map.Entry) this.photosMapKeys.get(i3);
                                    value = entry.getValue();
                                    if (value == photoEntry) {
                                        break;
                                    } else {
                                        i3++;
                                    }
                                } else {
                                    for (int i4 = 0; i4 < this.photosMapKeys.size(); i4++) {
                                        entry = (Map.Entry) this.photosMapKeys.get(i4);
                                        value = entry.getValue();
                                        if (!(value instanceof MediaController.PhotoEntry) || (str = ((MediaController.PhotoEntry) value).path) == null || photoEntry == null || !str.equals(photoEntry.path)) {
                                        }
                                    }
                                }
                            }
                            Object key = entry.getKey();
                            this.selectedPhotos.put(key, value);
                            this.photosOrder.add(key);
                            break;
                        }
                        Object obj = this.deletedPhotos.get(photoEntry);
                        this.selectedPhotos.put(obj, photoEntry);
                        this.photosOrder.add(obj);
                    }
                }
            }
        }

        Point dragTranslate() {
            Point point;
            float height;
            float f;
            float f2;
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell == null) {
                Point point2 = this.tmpPoint;
                point2.x = 0.0f;
                point2.y = 0.0f;
                return point2;
            }
            if (ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                RectF rect = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                RectF rect2 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect(1.0f);
                this.tmpPoint.x = AndroidUtilities.lerp(rect2.left + (rect.width() / 2.0f), this.savedDragFromX, this.draggingT / this.savedDraggingT);
                point = this.tmpPoint;
                height = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.y + rect2.top + (rect.height() / 2.0f);
                f = this.savedDragFromY;
                f2 = this.draggingT / this.savedDraggingT;
            } else {
                RectF rect3 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect();
                RectF rect4 = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.rect(1.0f);
                this.tmpPoint.x = AndroidUtilities.lerp(rect4.left + (rect3.width() / 2.0f), ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchX - ((ChatAttachAlertPhotoLayoutPreview.this.draggingCellLeft - 0.5f) * ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromWidth), this.draggingT);
                point = this.tmpPoint;
                height = ChatAttachAlertPhotoLayoutPreview.this.draggingCell.groupCell.y + rect4.top + (rect3.height() / 2.0f);
                f = (ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY - ((ChatAttachAlertPhotoLayoutPreview.this.draggingCellTop - 0.5f) * ChatAttachAlertPhotoLayoutPreview.this.draggingCellFromHeight)) + ChatAttachAlertPhotoLayoutPreview.this.draggingCellGroupY;
                f2 = this.draggingT;
            }
            point.y = AndroidUtilities.lerp(height, f, f2);
            return this.tmpPoint;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return false;
        }

        public void fromPhotoArrays() {
            for (int i = 0; i < this.groupCells.size(); i++) {
                ((PreviewGroupCell) this.groupCells.get(i)).detach();
            }
            this.groupCells.clear();
            ArrayList arrayList = new ArrayList();
            int size = this.photosOrder.size();
            int i2 = size - 1;
            for (int i3 = 0; i3 < size; i3++) {
                Integer num = (Integer) this.photosOrder.get(i3);
                num.intValue();
                arrayList.add((MediaController.PhotoEntry) this.photosMap.get(num));
                if (i3 % 10 == 9 || i3 == i2) {
                    PreviewGroupCell previewGroupCell = new PreviewGroupCell();
                    previewGroupCell.setGroup(new GroupCalculator(arrayList), false);
                    this.groupCells.add(previewGroupCell);
                    arrayList = new ArrayList();
                }
            }
        }

        public void fromPhotoLayout(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout) {
            this.photosOrder = chatAttachAlertPhotoLayout.getSelectedPhotosOrder();
            this.photosMap = chatAttachAlertPhotoLayout.getSelectedPhotos();
            fromPhotoArrays();
        }

        public ArrayList getPhotos() {
            ArrayList arrayList = new ArrayList();
            int size = this.groupCells.size();
            for (int i = 0; i < size; i++) {
                PreviewGroupCell previewGroupCell = (PreviewGroupCell) this.groupCells.get(i);
                if (previewGroupCell != null && previewGroupCell.group != null && previewGroupCell.group.photos != null) {
                    arrayList.addAll(previewGroupCell.group.photos);
                }
            }
            return arrayList;
        }

        public int getPhotosCount() {
            int size = this.groupCells.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                PreviewGroupCell previewGroupCell = (PreviewGroupCell) this.groupCells.get(i2);
                if (previewGroupCell != null && previewGroupCell.group != null && previewGroupCell.group.photos != null) {
                    i += previewGroupCell.group.photos.size();
                }
            }
            return i;
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

        public boolean isSeen(float f, float f2) {
            float f3 = this.viewTop;
            return (f >= f3 && f <= this.viewBottom) || (f2 >= f3 && f2 <= this.viewBottom) || (f <= f3 && f2 >= this.viewBottom);
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0067, code lost:
        
            if (r8 <= r10.viewBottom) goto L16;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onDraw(Canvas canvas) {
            float f = this.paddingTop;
            int computeVerticalScrollOffset = ChatAttachAlertPhotoLayoutPreview.this.listView.computeVerticalScrollOffset();
            this.viewTop = Math.max(0, computeVerticalScrollOffset - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding());
            this.viewBottom = (ChatAttachAlertPhotoLayoutPreview.this.listView.getMeasuredHeight() - ChatAttachAlertPhotoLayoutPreview.this.getListTopPadding()) + computeVerticalScrollOffset;
            canvas.save();
            canvas.translate(0.0f, this.paddingTop);
            int size = this.groupCells.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                PreviewGroupCell previewGroupCell = (PreviewGroupCell) this.groupCells.get(i2);
                float measure = previewGroupCell.measure();
                previewGroupCell.y = f;
                previewGroupCell.indexStart = i;
                float f2 = this.viewTop;
                if (f < f2 || f > this.viewBottom) {
                    float f3 = f + measure;
                    if (f3 >= f2) {
                    }
                    if (f <= f2) {
                        if (f3 < this.viewBottom) {
                        }
                    }
                    canvas.translate(0.0f, measure);
                    f += measure;
                    i += previewGroupCell.group.photos.size();
                }
                if (previewGroupCell.draw(canvas)) {
                    invalidate();
                }
                canvas.translate(0.0f, measure);
                f += measure;
                i += previewGroupCell.group.photos.size();
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

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            ChatActionCell chatActionCell = this.hintView;
            chatActionCell.layout(0, 0, chatActionCell.getMeasuredWidth(), this.hintView.getMeasuredHeight());
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            this.hintView.measure(i, View.MeasureSpec.makeMeasureSpec(9999, Integer.MIN_VALUE));
            if (this.lastMeasuredHeight <= 0) {
                this.lastMeasuredHeight = measureHeight();
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(Math.max(View.MeasureSpec.getSize(i2), this.lastMeasuredHeight), 1073741824));
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x002e  */
        /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onScroll() {
            boolean z = true;
            boolean z2 = this.lastGroupSeen == null;
            if (!z2) {
                boolean[] groupSeen = groupSeen();
                if (groupSeen.length == this.lastGroupSeen.length) {
                    for (int i = 0; i < groupSeen.length; i++) {
                        if (groupSeen[i] != this.lastGroupSeen[i]) {
                            break;
                        }
                    }
                }
                if (z) {
                    return;
                }
                invalidate();
                return;
            }
            this.lastGroupSeen = groupSeen();
            z = z2;
            if (z) {
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:185:0x044c  */
        /* JADX WARN: Removed duplicated region for block: B:188:0x045c  */
        /* JADX WARN: Removed duplicated region for block: B:191:0x04bb  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x04e3  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x04f2  */
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
            BaseFragment baseFragment;
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
                previewGroupCell = (PreviewGroupCell) this.groupCells.get(i3);
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
                    mediaCell = (PreviewGroupCell.MediaCell) previewGroupCell.media.get(i4);
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
                    PreviewGroupCell previewGroupCell3 = (PreviewGroupCell) this.groupCells.get(i6);
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
                        PreviewGroupCell.MediaCell mediaCell5 = (PreviewGroupCell.MediaCell) previewGroupCell2.media.get(i7);
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
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda0
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
            if (action == 2 && ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null && !ChatAttachAlertPhotoLayoutPreview.this.draggingCellHiding) {
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchX = x;
                ChatAttachAlertPhotoLayoutPreview.this.draggingCellTouchY = y;
                if (!this.scrollerStarted) {
                    this.scrollerStarted = true;
                    postDelayed(this.scroller, 16L);
                }
                invalidate();
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
                                    mediaCell4 = (PreviewGroupCell.MediaCell) previewGroupCell.media.get(i9);
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
                i8 = 1;
            }
            if (action != i8 || ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null || (mediaCell3 = this.tapMediaCell) == null || this.tapGroupCell == null) {
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
            if (mediaCell3.wasSpoiler && mediaCell3.spoilerRevealProgress == 0.0f) {
                this.tapMediaCell.startRevealMedia(motionEvent.getX(), motionEvent.getY());
            } else {
                RectF drawingRect2 = this.tapMediaCell.drawingRect();
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(drawingRect2.right - AndroidUtilities.dp(36.4f), this.tapGroupCell.top + drawingRect2.top, drawingRect2.right, this.tapGroupCell.top + drawingRect2.top + AndroidUtilities.dp(36.4f));
                if (!rectF2.contains(x, y - this.tapMediaCell.groupCell.y)) {
                    calcPhotoArrays();
                    ArrayList photos = getPhotos();
                    int indexOf3 = photos.indexOf(this.tapMediaCell.photoEntry);
                    ChatAttachAlert chatAttachAlert = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                    if (chatAttachAlert.avatarPicker != 0) {
                        i2 = 1;
                    } else {
                        BaseFragment baseFragment2 = chatAttachAlert.baseFragment;
                        if (baseFragment2 instanceof ChatActivity) {
                            chatActivity = (ChatActivity) baseFragment2;
                            i2 = 0;
                            baseFragment = chatAttachAlert.baseFragment;
                            if (baseFragment == null) {
                                baseFragment = LaunchActivity.getLastFragment();
                            }
                            if (!ChatAttachAlertPhotoLayoutPreview.this.parentAlert.delegate.needEnterComment()) {
                                AndroidUtilities.hideKeyboard(baseFragment.getFragmentView().findFocus());
                                AndroidUtilities.hideKeyboard(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getContainer().findFocus());
                            }
                            PhotoViewer.getInstance().setParentActivity(baseFragment, ChatAttachAlertPhotoLayoutPreview.this.resourcesProvider);
                            PhotoViewer.getInstance().setParentAlert(ChatAttachAlertPhotoLayoutPreview.this.parentAlert);
                            PhotoViewer photoViewer = PhotoViewer.getInstance();
                            ChatAttachAlert chatAttachAlert2 = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                            photoViewer.setMaxSelectedPhotos(chatAttachAlert2.maxSelectedPhotos, chatAttachAlert2.allowOrder);
                            this.photoViewerProvider.init(photos);
                            PhotoViewer.getInstance().openPhotoForSelect(new ArrayList(photos), indexOf3, i2, false, this.photoViewerProvider, chatActivity);
                            if (ChatAttachAlertPhotoLayoutPreview.this.photoLayout.captionForAllMedia()) {
                                PhotoViewer.getInstance().setCaption(ChatAttachAlertPhotoLayoutPreview.this.parentAlert.getCommentTextView().getText());
                            }
                        } else {
                            i2 = 4;
                        }
                    }
                    chatActivity = null;
                    baseFragment = chatAttachAlert.baseFragment;
                    if (baseFragment == null) {
                    }
                    if (!ChatAttachAlertPhotoLayoutPreview.this.parentAlert.delegate.needEnterComment()) {
                    }
                    PhotoViewer.getInstance().setParentActivity(baseFragment, ChatAttachAlertPhotoLayoutPreview.this.resourcesProvider);
                    PhotoViewer.getInstance().setParentAlert(ChatAttachAlertPhotoLayoutPreview.this.parentAlert);
                    PhotoViewer photoViewer2 = PhotoViewer.getInstance();
                    ChatAttachAlert chatAttachAlert22 = ChatAttachAlertPhotoLayoutPreview.this.parentAlert;
                    photoViewer2.setMaxSelectedPhotos(chatAttachAlert22.maxSelectedPhotos, chatAttachAlert22.allowOrder);
                    this.photoViewerProvider.init(photos);
                    PhotoViewer.getInstance().openPhotoForSelect(new ArrayList(photos), indexOf3, i2, false, this.photoViewerProvider, chatActivity);
                    if (ChatAttachAlertPhotoLayoutPreview.this.photoLayout.captionForAllMedia()) {
                    }
                } else if (ChatAttachAlertPhotoLayoutPreview.this.getSelectedItemsCount() > 1) {
                    final MediaController.PhotoEntry photoEntry = this.tapMediaCell.photoEntry;
                    final int indexOf4 = this.tapGroupCell.group.photos.indexOf(photoEntry);
                    if (indexOf4 >= 0) {
                        saveDeletedImageId(photoEntry);
                        final PreviewGroupCell previewGroupCell4 = this.tapGroupCell;
                        previewGroupCell4.group.photos.remove(indexOf4);
                        previewGroupCell4.setGroup(previewGroupCell4.group, true);
                        updateGroups();
                        toPhotoLayout(ChatAttachAlertPhotoLayoutPreview.this.photoLayout, false);
                        final int i10 = this.undoViewId + 1;
                        this.undoViewId = i10;
                        ChatAttachAlertPhotoLayoutPreview.this.undoView.showWithAction(0L, 82, photoEntry, null, new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$onTouchEvent$3(previewGroupCell4, photoEntry, indexOf4);
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
                this.tapMediaCell = null;
                this.tapTime = 0L;
                ChatAttachAlertPhotoLayoutPreview.this.draggingCell = null;
                this.draggingT = 0.0f;
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

        public void remeasure() {
            float f = this.paddingTop;
            int size = this.groupCells.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                PreviewGroupCell previewGroupCell = (PreviewGroupCell) this.groupCells.get(i2);
                float measure = previewGroupCell.measure();
                previewGroupCell.y = f;
                previewGroupCell.indexStart = i;
                f += measure;
                i += previewGroupCell.group.photos.size();
            }
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
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView.this.lambda$startDragging$1(valueAnimator);
                }
            });
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.setDuration(200L);
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.start();
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
            ChatAttachAlertPhotoLayoutPreview.this.draggingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda4
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

        public void toPhotoLayout(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout, boolean z) {
            int size = chatAttachAlertPhotoLayout.getSelectedPhotosOrder().size();
            calcPhotoArrays();
            chatAttachAlertPhotoLayout.updateSelected(this.selectedPhotos, this.photosOrder, z);
            if (size != this.photosOrder.size()) {
                ChatAttachAlertPhotoLayoutPreview.this.parentAlert.updateCountButton(1);
            }
        }
    }

    public ChatAttachAlertPhotoLayoutPreview(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.durationMultiplier = 1L;
        this.draggingCellTouchX = 0.0f;
        this.draggingCellTouchY = 0.0f;
        this.draggingCellTop = 0.0f;
        this.draggingCellLeft = 0.0f;
        this.draggingCellFromWidth = 0.0f;
        this.draggingCellFromHeight = 0.0f;
        this.draggingCell = null;
        this.draggingCellHiding = false;
        this.draggingCellGroupY = 0.0f;
        this.shown = false;
        this.ignoreLayout = false;
        android.graphics.Point point = AndroidUtilities.displaySize;
        this.isPortrait = point.y > point.x;
        this.themeDelegate = resourcesProvider;
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
        this.header.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
        this.header.setText(LocaleController.getString(R.string.AttachMediaPreview));
        this.header.setTypeface(AndroidUtilities.bold());
        this.header.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
        this.header.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
        this.header.setAlpha(0.0f);
        actionBarMenuItem.addView(this.header, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview.2
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (ChatAttachAlertPhotoLayoutPreview.this.draggingCell != null) {
                    return false;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

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

    static /* synthetic */ float access$1416(ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview, float f) {
        float f2 = chatAttachAlertPhotoLayoutPreview.draggingCellTouchY + f;
        chatAttachAlertPhotoLayoutPreview.draggingCellTouchY = f2;
        return f2;
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
        if (!this.shown || this.parentAlert.getPhotoLayout() == null) {
            return;
        }
        this.parentAlert.getPhotoLayout().previewItem.setIcon(R.drawable.ic_ab_back);
        this.parentAlert.getPhotoLayout().previewItem.setText(LocaleController.getString(R.string.Back));
        this.parentAlert.getPhotoLayout().previewItem.setRightIcon(0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void applyCaption(CharSequence charSequence) {
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout != null) {
            chatAttachAlertPhotoLayout.applyCaption(charSequence);
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

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            recyclerListView.setTopGlowOffset(recyclerListView.getPaddingTop());
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
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

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    public float getPreviewScale() {
        android.graphics.Point point = AndroidUtilities.displaySize;
        return point.y > point.x ? 0.8f : 0.45f;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getSelectedItemsCount() {
        return this.groupsView.getPhotosCount();
    }

    public Drawable getThemedDrawable(String str) {
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        Drawable drawable = resourcesProvider != null ? resourcesProvider.getDrawable(str) : null;
        return drawable != null ? drawable : Theme.getThemeDrawable(str);
    }

    public void invalidateGroupsView() {
        this.groupsView.invalidate();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int needsActionBar() {
        return 1;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        this.parentAlert.updatePhotoPreview(false);
        return true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        MediaController.PhotoEntry photoEntry;
        this.draggingCell = null;
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(false, 0);
        }
        Iterator it = this.groupsView.groupCells.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((PreviewGroupsView.PreviewGroupCell) it.next()).media.iterator();
            while (it2.hasNext()) {
                PreviewGroupsView.PreviewGroupCell.MediaCell mediaCell = (PreviewGroupsView.PreviewGroupCell.MediaCell) it2.next();
                if (mediaCell.wasSpoiler && (photoEntry = mediaCell.photoEntry) != null) {
                    photoEntry.isChatPreviewSpoilerRevealed = false;
                }
            }
        }
    }

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
        if (getSelectedItemsCount() > 1 && this.parentAlert.getPhotoLayout() != null) {
            this.parentAlert.getPhotoLayout().previewItem.setIcon(R.drawable.msg_view_file);
            this.parentAlert.getPhotoLayout().previewItem.setText(LocaleController.getString(R.string.AttachMediaPreviewButton));
            this.parentAlert.getPhotoLayout().previewItem.setRightIcon(R.drawable.msg_arrowright);
        }
        this.groupsView.toPhotoLayout(this.photoLayout, true);
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
    public void onMenuItemClick(int i) {
        try {
            this.parentAlert.getPhotoLayout().onMenuItemClick(i);
        } catch (Exception unused) {
        }
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
    public void onSelectedItemsCountChanged(int i) {
        if (i > 1) {
            this.parentAlert.selectedMenuItem.showSubItem(0);
        } else {
            this.parentAlert.selectedMenuItem.hideSubItem(0);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShow(final ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.shown = true;
        if (attachAlertLayout instanceof ChatAttachAlertPhotoLayout) {
            this.photoLayout = (ChatAttachAlertPhotoLayout) attachAlertLayout;
            this.groupsView.deletedPhotos.clear();
            this.groupsView.fromPhotoLayout(this.photoLayout);
            this.groupsView.requestLayout();
            this.layoutManager.scrollToPositionWithOffset(0, 0);
            this.listView.post(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertPhotoLayoutPreview.this.lambda$onShow$0(attachAlertLayout);
                }
            });
            postDelayed(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda1
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

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        this.listView.smoothScrollToPosition(0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean shouldHideBottomButtons() {
        return true;
    }

    public void startMediaCrossfade() {
        Iterator it = this.groupsView.groupCells.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((PreviewGroupsView.PreviewGroupCell) it.next()).media.iterator();
            while (it2.hasNext()) {
                ((PreviewGroupsView.PreviewGroupCell.MediaCell) it2.next()).startCrossfade();
            }
        }
    }
}
