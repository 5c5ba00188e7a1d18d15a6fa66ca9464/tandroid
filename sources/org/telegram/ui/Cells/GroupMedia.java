package org.telegram.ui.Cells;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.Stars.StarsIntroActivity;
/* loaded from: classes4.dex */
public class GroupMedia {
    private final AnimatedFloat animatedHidden;
    public boolean attached;
    private Bitmap blurBitmap;
    private int blurBitmapHeight;
    private int blurBitmapMessageId;
    private Paint blurBitmapPaint;
    private int blurBitmapState;
    private int blurBitmapWidth;
    private final ButtonBounce bounce;
    private Text buttonText;
    private long buttonTextPrice;
    public final ChatMessageCell cell;
    public int height;
    public boolean hidden;
    private GroupedMessages layout;
    private LoadingDrawable loadingDrawable;
    public int maxWidth;
    private int overrideWidth;
    private boolean pressButton;
    private MediaHolder pressHolder;
    private Text priceText;
    private long priceTextPrice;
    SpoilerEffect2 spoilerEffect;
    public int width;
    public int x;
    public int y;
    public final ArrayList holders = new ArrayList();
    private Path clipPath = new Path();
    private Path clipPath2 = new Path();
    private RectF clipRect = new RectF();

    /* loaded from: classes4.dex */
    public static class GroupedMessages {
        public boolean hasSibling;
        float height;
        int maxX;
        int maxY;
        int width;
        public ArrayList medias = new ArrayList();
        public ArrayList posArray = new ArrayList();
        public HashMap positions = new HashMap();
        public int maxSizeWidth = 800;
        public float maxSizeHeight = 814.0f;
        public final TransitionParams transitionParams = new TransitionParams();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class MessageGroupedLayoutAttempt {
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

        /* loaded from: classes4.dex */
        public static class TransitionParams {
            public float captionEnterProgress = 1.0f;
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
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v17 */
        /* JADX WARN: Type inference failed for: r3v18 */
        /* JADX WARN: Type inference failed for: r3v8, types: [int] */
        public void calculate() {
            int i;
            float f;
            float f2;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            MessageObject.GroupedMessagePosition groupedMessagePosition;
            int i7;
            float f3;
            float f4;
            TLRPC.Document document;
            ArrayList<TLRPC.PhotoSize> arrayList;
            TLRPC.PhotoSize photoSize;
            boolean z = true;
            ?? r3 = 1;
            this.posArray.clear();
            this.positions.clear();
            this.maxX = 0;
            int size = this.medias.size();
            if (size == 0) {
                this.width = 0;
                this.height = 0.0f;
                this.maxY = 0;
                return;
            }
            this.maxSizeWidth = 800;
            StringBuilder sb = new StringBuilder();
            this.hasSibling = false;
            int i8 = 0;
            float f5 = 1.0f;
            boolean z2 = false;
            while (i8 < size) {
                TLRPC.MessageExtendedMedia messageExtendedMedia = (TLRPC.MessageExtendedMedia) this.medias.get(i8);
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = new MessageObject.GroupedMessagePosition();
                groupedMessagePosition2.last = i8 == size + (-1);
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                    TLRPC.TL_messageExtendedMediaPreview tL_messageExtendedMediaPreview = (TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia;
                    groupedMessagePosition2.photoWidth = tL_messageExtendedMediaPreview.w;
                    groupedMessagePosition2.photoHeight = tL_messageExtendedMediaPreview.h;
                } else {
                    int i9 = 100;
                    if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                        TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                            TLRPC.Photo photo = ((TLRPC.TL_messageMediaPhoto) messageMedia).photo;
                            if (photo != null) {
                                arrayList = photo.sizes;
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize());
                            }
                            photoSize = null;
                        } else {
                            if ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && (document = ((TLRPC.TL_messageMediaDocument) messageMedia).document) != null) {
                                arrayList = document.thumbs;
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize());
                            }
                            photoSize = null;
                        }
                        groupedMessagePosition2.photoWidth = photoSize == null ? 100 : photoSize.w;
                        if (photoSize != null) {
                            i9 = photoSize.h;
                        }
                    } else {
                        groupedMessagePosition2.photoWidth = 100;
                    }
                    groupedMessagePosition2.photoHeight = i9;
                }
                if (groupedMessagePosition2.photoWidth <= 0 || groupedMessagePosition2.photoHeight <= 0) {
                    groupedMessagePosition2.photoWidth = 50;
                    groupedMessagePosition2.photoHeight = 50;
                }
                float f6 = groupedMessagePosition2.photoWidth / groupedMessagePosition2.photoHeight;
                groupedMessagePosition2.aspectRatio = f6;
                sb.append(f6 > 1.2f ? "w" : f6 < 0.8f ? "n" : "q");
                float f7 = groupedMessagePosition2.aspectRatio;
                f5 += f7;
                if (f7 > 2.0f) {
                    z2 = true;
                }
                this.positions.put(messageExtendedMedia, groupedMessagePosition2);
                this.posArray.add(groupedMessagePosition2);
                i8++;
            }
            int dp = AndroidUtilities.dp(120.0f);
            Point point = AndroidUtilities.displaySize;
            int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
            Point point2 = AndroidUtilities.displaySize;
            float f8 = this.maxSizeWidth;
            int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / f8));
            float f9 = f8 / this.maxSizeHeight;
            float f10 = f5 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / this.maxSizeHeight;
            if (size == 1) {
                MessageObject.GroupedMessagePosition groupedMessagePosition3 = (MessageObject.GroupedMessagePosition) this.posArray.get(0);
                float f11 = groupedMessagePosition3.aspectRatio;
                if (f11 >= 1.0f) {
                    f3 = this.maxSizeWidth;
                    f4 = ((f3 / f11) / f3) * this.maxSizeHeight;
                } else {
                    float f12 = this.maxSizeHeight;
                    f3 = ((f11 * f12) / f12) * this.maxSizeWidth;
                    f4 = f12;
                }
                groupedMessagePosition3.set(0, 0, 0, 0, (int) f3, f4 / this.maxSizeHeight, 15);
            } else if (z2 || !(size == 2 || size == 3 || size == 4)) {
                int size2 = this.posArray.size();
                float[] fArr = new float[size2];
                for (int i10 = 0; i10 < size; i10++) {
                    if (f10 > 1.1f) {
                        fArr[i10] = Math.max(1.0f, ((MessageObject.GroupedMessagePosition) this.posArray.get(i10)).aspectRatio);
                    } else {
                        fArr[i10] = Math.min(1.0f, ((MessageObject.GroupedMessagePosition) this.posArray.get(i10)).aspectRatio);
                    }
                    fArr[i10] = Math.max(0.66667f, Math.min(1.7f, fArr[i10]));
                }
                ArrayList arrayList2 = new ArrayList();
                for (int i11 = 1; i11 < size2; i11++) {
                    int i12 = size2 - i11;
                    if (i11 <= 3 && i12 <= 3) {
                        arrayList2.add(new MessageGroupedLayoutAttempt(i11, i12, multiHeight(fArr, 0, i11), multiHeight(fArr, i11, size2)));
                    }
                }
                for (int i13 = 1; i13 < size2 - 1; i13++) {
                    int i14 = 1;
                    while (true) {
                        int i15 = size2 - i13;
                        if (i14 < i15) {
                            int i16 = i15 - i14;
                            if (i13 <= 3) {
                                if (i14 <= (f10 < 0.85f ? 4 : 3) && i16 <= 3) {
                                    int i17 = i13 + i14;
                                    arrayList2.add(new MessageGroupedLayoutAttempt(i13, i14, i16, multiHeight(fArr, 0, i13), multiHeight(fArr, i13, i17), multiHeight(fArr, i17, size2)));
                                }
                            }
                            i14++;
                        }
                    }
                }
                for (int i18 = 1; i18 < size2 - 2; i18++) {
                    int i19 = 1;
                    while (true) {
                        int i20 = size2 - i18;
                        if (i19 < i20) {
                            int i21 = 1;
                            while (true) {
                                int i22 = i20 - i19;
                                if (i21 < i22) {
                                    int i23 = i22 - i21;
                                    if (i18 <= 3 && i19 <= 3 && i21 <= 3 && i23 <= 3) {
                                        int i24 = i18 + i19;
                                        int i25 = i24 + i21;
                                        arrayList2.add(new MessageGroupedLayoutAttempt(i18, i19, i21, i23, multiHeight(fArr, 0, i18), multiHeight(fArr, i18, i24), multiHeight(fArr, i24, i25), multiHeight(fArr, i25, size2)));
                                    }
                                    i21++;
                                }
                            }
                            i19++;
                        }
                    }
                }
                float f13 = (this.maxSizeWidth / 3) * 4;
                int i26 = 0;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                float f14 = 0.0f;
                while (i26 < arrayList2.size()) {
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList2.get(i26);
                    int i27 = 0;
                    float f15 = Float.MAX_VALUE;
                    float f16 = 0.0f;
                    while (true) {
                        float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                        if (i27 >= fArr2.length) {
                            break;
                        }
                        float f17 = fArr2[i27];
                        f16 += f17;
                        if (f17 < f15) {
                            f15 = f17;
                        }
                        i27++;
                    }
                    float abs = Math.abs(f16 - f13);
                    int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                    float f18 = f13;
                    if (iArr.length > 1) {
                        int i28 = iArr[0];
                        int i29 = iArr[1];
                        if (i28 <= i29) {
                            if (iArr.length > 2 && i29 > iArr[2]) {
                                f = 1.2f;
                                abs *= f;
                            } else if (iArr.length <= 3 || iArr[2] <= iArr[3]) {
                            }
                        }
                        f = 1.2f;
                        abs *= f;
                    }
                    if (f15 < dp2) {
                        abs *= 1.5f;
                    }
                    if (messageGroupedLayoutAttempt == null || abs < f14) {
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                        f14 = abs;
                    }
                    i26++;
                    f13 = f18;
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int i30 = 0;
                int i31 = 0;
                while (true) {
                    int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                    if (i30 >= iArr2.length) {
                        break;
                    }
                    int i32 = iArr2[i30];
                    float f19 = messageGroupedLayoutAttempt.heights[i30];
                    int i33 = this.maxSizeWidth;
                    int i34 = i32 - 1;
                    this.maxX = Math.max(this.maxX, i34);
                    int i35 = i33;
                    MessageObject.GroupedMessagePosition groupedMessagePosition4 = null;
                    for (int i36 = 0; i36 < i32; i36++) {
                        int i37 = (int) (fArr[i31] * f19);
                        i35 -= i37;
                        MessageObject.GroupedMessagePosition groupedMessagePosition5 = (MessageObject.GroupedMessagePosition) this.posArray.get(i31);
                        int i38 = i30 == 0 ? 4 : 0;
                        if (i30 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            i38 |= 8;
                        }
                        if (i36 == 0) {
                            i38 |= 1;
                        }
                        if (i36 == i34) {
                            i = i38 | 2;
                            groupedMessagePosition4 = groupedMessagePosition5;
                        } else {
                            i = i38;
                        }
                        groupedMessagePosition5.set(i36, i36, i30, i30, i37, Math.max(dp4, f19 / this.maxSizeHeight), i);
                        i31++;
                    }
                    groupedMessagePosition4.pw += i35;
                    groupedMessagePosition4.spanSize += i35;
                    i30++;
                }
            } else {
                if (size == 2) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition6 = (MessageObject.GroupedMessagePosition) this.posArray.get(0);
                    MessageObject.GroupedMessagePosition groupedMessagePosition7 = (MessageObject.GroupedMessagePosition) this.posArray.get(1);
                    String sb2 = sb.toString();
                    if (sb2.equals("ww")) {
                        double d = f9;
                        Double.isNaN(d);
                        if (f10 > d * 1.4d) {
                            float f20 = groupedMessagePosition6.aspectRatio;
                            float f21 = groupedMessagePosition7.aspectRatio;
                            if (f20 - f21 < 0.2d) {
                                float f22 = this.maxSizeWidth;
                                float round = Math.round(Math.min(f22 / f20, Math.min(f22 / f21, this.maxSizeHeight / 2.0f))) / this.maxSizeHeight;
                                groupedMessagePosition6.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                                groupedMessagePosition7.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                            }
                        }
                    }
                    if (sb2.equals("ww") || sb2.equals("qq")) {
                        int i39 = this.maxSizeWidth / 2;
                        float f23 = i39;
                        i2 = 0;
                        i6 = 0;
                        i7 = i39;
                        f2 = Math.round(Math.min(f23 / groupedMessagePosition6.aspectRatio, Math.min(f23 / groupedMessagePosition7.aspectRatio, this.maxSizeHeight))) / this.maxSizeHeight;
                        groupedMessagePosition6.set(0, 0, 0, 0, i7, f2, 13);
                        i3 = 14;
                        i4 = 1;
                        i5 = 1;
                        groupedMessagePosition = groupedMessagePosition7;
                    } else {
                        float f24 = this.maxSizeWidth;
                        float f25 = groupedMessagePosition6.aspectRatio;
                        int max = (int) Math.max(0.4f * f24, Math.round((f24 / f25) / ((1.0f / f25) + (1.0f / groupedMessagePosition7.aspectRatio))));
                        int i40 = this.maxSizeWidth - max;
                        if (i40 < dp2) {
                            max -= dp2 - i40;
                        } else {
                            dp2 = i40;
                        }
                        i2 = 0;
                        i6 = 0;
                        f2 = Math.min(this.maxSizeHeight, Math.round(Math.min(dp2 / groupedMessagePosition6.aspectRatio, max / groupedMessagePosition7.aspectRatio))) / this.maxSizeHeight;
                        groupedMessagePosition6.set(0, 0, 0, 0, dp2, f2, 13);
                        i3 = 14;
                        i4 = 1;
                        i5 = 1;
                        groupedMessagePosition = groupedMessagePosition7;
                        i7 = max;
                    }
                    groupedMessagePosition.set(i4, i5, i6, i2, i7, f2, i3);
                } else {
                    if (size == 3) {
                        MessageObject.GroupedMessagePosition groupedMessagePosition8 = (MessageObject.GroupedMessagePosition) this.posArray.get(0);
                        MessageObject.GroupedMessagePosition groupedMessagePosition9 = (MessageObject.GroupedMessagePosition) this.posArray.get(1);
                        MessageObject.GroupedMessagePosition groupedMessagePosition10 = (MessageObject.GroupedMessagePosition) this.posArray.get(2);
                        if (sb.charAt(0) == 'n') {
                            float f26 = groupedMessagePosition9.aspectRatio;
                            float min = Math.min(this.maxSizeHeight * 0.5f, Math.round((this.maxSizeWidth * f26) / (groupedMessagePosition10.aspectRatio + f26)));
                            float f27 = this.maxSizeHeight - min;
                            int max2 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min, groupedMessagePosition9.aspectRatio * f27))));
                            int round2 = Math.round(Math.min((this.maxSizeHeight * groupedMessagePosition8.aspectRatio) + dp3, this.maxSizeWidth - max2));
                            groupedMessagePosition8.set(0, 0, 0, 1, round2, 1.0f, 13);
                            groupedMessagePosition9.set(1, 1, 0, 0, max2, f27 / this.maxSizeHeight, 6);
                            groupedMessagePosition10.set(1, 1, 1, 1, max2, min / this.maxSizeHeight, 10);
                            int i41 = this.maxSizeWidth;
                            groupedMessagePosition10.spanSize = i41;
                            float f28 = this.maxSizeHeight;
                            groupedMessagePosition8.siblingHeights = new float[]{min / f28, f27 / f28};
                            groupedMessagePosition9.spanSize = i41 - round2;
                            groupedMessagePosition10.leftSpanOffset = round2;
                        } else {
                            float round3 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition8.aspectRatio, this.maxSizeHeight * 0.66f)) / this.maxSizeHeight;
                            groupedMessagePosition8.set(0, 1, 0, 0, this.maxSizeWidth, round3, 7);
                            int i42 = this.maxSizeWidth / 2;
                            float f29 = this.maxSizeHeight - round3;
                            float f30 = i42;
                            float min2 = Math.min(f29, Math.round(Math.min(f30 / groupedMessagePosition9.aspectRatio, f30 / groupedMessagePosition10.aspectRatio))) / this.maxSizeHeight;
                            f2 = min2 < dp4 ? dp4 : min2;
                            groupedMessagePosition9.set(0, 0, 1, 1, i42, f2, 9);
                            i2 = 1;
                            i3 = 10;
                            i4 = 1;
                            i5 = 1;
                            i6 = 1;
                            groupedMessagePosition = groupedMessagePosition10;
                            i7 = i42;
                            groupedMessagePosition.set(i4, i5, i6, i2, i7, f2, i3);
                        }
                    } else {
                        MessageObject.GroupedMessagePosition groupedMessagePosition11 = (MessageObject.GroupedMessagePosition) this.posArray.get(0);
                        MessageObject.GroupedMessagePosition groupedMessagePosition12 = (MessageObject.GroupedMessagePosition) this.posArray.get(1);
                        MessageObject.GroupedMessagePosition groupedMessagePosition13 = (MessageObject.GroupedMessagePosition) this.posArray.get(2);
                        MessageObject.GroupedMessagePosition groupedMessagePosition14 = (MessageObject.GroupedMessagePosition) this.posArray.get(3);
                        if (sb.charAt(0) == 'w') {
                            float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition11.aspectRatio, this.maxSizeHeight * 0.66f)) / this.maxSizeHeight;
                            groupedMessagePosition11.set(0, 2, 0, 0, this.maxSizeWidth, round4, 7);
                            float round5 = Math.round(this.maxSizeWidth / ((groupedMessagePosition12.aspectRatio + groupedMessagePosition13.aspectRatio) + groupedMessagePosition14.aspectRatio));
                            float f31 = dp2;
                            int max3 = (int) Math.max(f31, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition12.aspectRatio * round5));
                            int max4 = (int) Math.max(Math.max(f31, this.maxSizeWidth * 0.33f), groupedMessagePosition14.aspectRatio * round5);
                            int i43 = (this.maxSizeWidth - max3) - max4;
                            if (i43 < AndroidUtilities.dp(58.0f)) {
                                int dp5 = AndroidUtilities.dp(58.0f) - i43;
                                i43 = AndroidUtilities.dp(58.0f);
                                int i44 = dp5 / 2;
                                max3 -= i44;
                                max4 -= dp5 - i44;
                            }
                            int i45 = max3;
                            float min3 = Math.min(this.maxSizeHeight - round4, round5) / this.maxSizeHeight;
                            if (min3 < dp4) {
                                min3 = dp4;
                            }
                            float f32 = min3;
                            groupedMessagePosition12.set(0, 0, 1, 1, i45, f32, 9);
                            groupedMessagePosition13.set(1, 1, 1, 1, i43, f32, 8);
                            groupedMessagePosition14.set(2, 2, 1, 1, max4, f32, 10);
                            this.maxX = 2;
                        } else {
                            int max5 = Math.max(dp2, Math.round(this.maxSizeHeight / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                            float f33 = dp;
                            float f34 = max5;
                            float min4 = Math.min(0.33f, Math.max(f33, f34 / groupedMessagePosition12.aspectRatio) / this.maxSizeHeight);
                            float min5 = Math.min(0.33f, Math.max(f33, f34 / groupedMessagePosition13.aspectRatio) / this.maxSizeHeight);
                            float f35 = (1.0f - min4) - min5;
                            int round6 = Math.round(Math.min((this.maxSizeHeight * groupedMessagePosition11.aspectRatio) + dp3, this.maxSizeWidth - max5));
                            groupedMessagePosition11.set(0, 0, 0, 2, round6, min4 + min5 + f35, 13);
                            groupedMessagePosition12.set(1, 1, 0, 0, max5, min4, 6);
                            groupedMessagePosition13.set(1, 1, 1, 1, max5, min5, 2);
                            groupedMessagePosition13.spanSize = this.maxSizeWidth;
                            groupedMessagePosition14.set(1, 1, 2, 2, max5, f35, 10);
                            int i46 = this.maxSizeWidth;
                            groupedMessagePosition14.spanSize = i46;
                            groupedMessagePosition12.spanSize = i46 - round6;
                            groupedMessagePosition13.leftSpanOffset = round6;
                            groupedMessagePosition14.leftSpanOffset = round6;
                            z = true;
                            groupedMessagePosition11.siblingHeights = new float[]{min4, min5, f35};
                        }
                    }
                    this.hasSibling = z;
                    r3 = z;
                }
                this.maxX = r3;
            }
            for (int i47 = 0; i47 < size; i47++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition15 = (MessageObject.GroupedMessagePosition) this.posArray.get(i47);
                if (groupedMessagePosition15.maxX == this.maxX || (groupedMessagePosition15.flags & 2) != 0) {
                    groupedMessagePosition15.spanSize += NotificationCenter.storyQualityUpdate;
                }
                if ((groupedMessagePosition15.flags & 1) != 0) {
                    groupedMessagePosition15.edge = true;
                }
                TLRPC.MessageExtendedMedia messageExtendedMedia2 = (TLRPC.MessageExtendedMedia) this.medias.get(i47);
                if (groupedMessagePosition15.edge) {
                    int i48 = groupedMessagePosition15.spanSize;
                    if (i48 != 1000) {
                        groupedMessagePosition15.spanSize = i48 + 108;
                    }
                    groupedMessagePosition15.pw += 108;
                } else if ((groupedMessagePosition15.flags & 2) != 0) {
                    int i49 = groupedMessagePosition15.spanSize;
                    if (i49 != 1000) {
                        groupedMessagePosition15.spanSize = i49 - 108;
                    } else {
                        int i50 = groupedMessagePosition15.leftSpanOffset;
                        if (i50 != 0) {
                            groupedMessagePosition15.leftSpanOffset = i50 + 108;
                        }
                    }
                }
            }
            for (int i51 = 0; i51 < size; i51++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition16 = (MessageObject.GroupedMessagePosition) this.posArray.get(i51);
                if (groupedMessagePosition16.minX == 0) {
                    groupedMessagePosition16.spanSize += NotificationCenter.storyQualityUpdate;
                }
                if ((groupedMessagePosition16.flags & 2) != 0) {
                    groupedMessagePosition16.edge = true;
                }
                this.maxX = Math.max(this.maxX, (int) groupedMessagePosition16.maxX);
                this.maxY = Math.max(this.maxY, (int) groupedMessagePosition16.maxY);
                groupedMessagePosition16.left = getLeft(groupedMessagePosition16, groupedMessagePosition16.minY, groupedMessagePosition16.maxY, groupedMessagePosition16.minX);
            }
            for (int i52 = 0; i52 < size; i52++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition17 = (MessageObject.GroupedMessagePosition) this.posArray.get(i52);
                groupedMessagePosition17.top = getTop(groupedMessagePosition17, groupedMessagePosition17.minY);
            }
            this.width = getWidth();
            this.height = getHeight();
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

        public MessageObject.GroupedMessagePosition getPosition(TLRPC.MessageExtendedMedia messageExtendedMedia) {
            if (messageExtendedMedia == null) {
                return null;
            }
            return (MessageObject.GroupedMessagePosition) this.positions.get(messageExtendedMedia);
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

    /* loaded from: classes4.dex */
    public static class MediaHolder implements DownloadController.FileDownloadProgressListener {
        private final int TAG;
        public boolean album;
        public String attachPath;
        public boolean attached;
        public boolean autoplay;
        public int b;
        public final ChatMessageCell cell;
        private int duration;
        private Text durationText;
        private int durationValue;
        public String filename;
        private final int h;
        public boolean hidden;
        public int icon;
        public final ImageReceiver imageReceiver;
        public int l;
        public TLRPC.MessageExtendedMedia media;
        public int r;
        public final RadialProgress2 radialProgress;
        public int t;
        public boolean video;
        private final int w;
        public final float[] radii = new float[8];
        public final RectF clipRect = new RectF();
        public final Path clipPath = new Path();

        /* JADX WARN: Removed duplicated region for block: B:21:0x006a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public MediaHolder(ChatMessageCell chatMessageCell, MessageObject messageObject, TLRPC.MessageExtendedMedia messageExtendedMedia, boolean z, int i, int i2) {
            int i3;
            this.icon = 4;
            this.duration = 0;
            this.durationValue = 0;
            this.cell = chatMessageCell;
            this.album = z;
            this.video = false;
            if (!(messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia)) {
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                    TLRPC.TL_messageExtendedMediaPreview tL_messageExtendedMediaPreview = (TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia;
                    this.video = (4 & tL_messageExtendedMediaPreview.flags) != 0;
                    i3 = tL_messageExtendedMediaPreview.video_duration;
                }
                if (this.video) {
                    int i4 = this.duration;
                    this.durationValue = i4;
                    this.durationText = new Text(AndroidUtilities.formatLongDuration(i4), 12.0f);
                }
                ImageReceiver imageReceiver = new ImageReceiver(chatMessageCell);
                this.imageReceiver = imageReceiver;
                imageReceiver.setColorFilter(null);
                this.w = i;
                this.h = i2;
                this.TAG = DownloadController.getInstance(chatMessageCell.currentAccount).generateObserverTag();
                updateMedia(messageExtendedMedia, messageObject);
                RadialProgress2 radialProgress2 = new RadialProgress2(chatMessageCell, chatMessageCell.getResourcesProvider());
                this.radialProgress = radialProgress2;
                int defaultIcon = getDefaultIcon();
                this.icon = defaultIcon;
                radialProgress2.setIcon(defaultIcon, false, false);
            }
            TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
            this.video = ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && MessageObject.isVideoDocument(messageMedia.document)) ? false : false;
            i3 = (int) Math.max(1L, Math.round(MessageObject.getDocumentDuration(messageMedia.document)));
            this.duration = i3;
            if (this.video) {
            }
            ImageReceiver imageReceiver2 = new ImageReceiver(chatMessageCell);
            this.imageReceiver = imageReceiver2;
            imageReceiver2.setColorFilter(null);
            this.w = i;
            this.h = i2;
            this.TAG = DownloadController.getInstance(chatMessageCell.currentAccount).generateObserverTag();
            updateMedia(messageExtendedMedia, messageObject);
            RadialProgress2 radialProgress22 = new RadialProgress2(chatMessageCell, chatMessageCell.getResourcesProvider());
            this.radialProgress = radialProgress22;
            int defaultIcon2 = getDefaultIcon();
            this.icon = defaultIcon2;
            radialProgress22.setIcon(defaultIcon2, false, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDefaultIcon() {
            return (!this.video || this.autoplay) ? 4 : 0;
        }

        public void attach() {
            if (this.attached) {
                return;
            }
            this.attached = true;
            this.imageReceiver.onAttachedToWindow();
        }

        public void detach() {
            if (this.attached) {
                this.attached = false;
                this.imageReceiver.onDetachedFromWindow();
            }
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public int getObserverTag() {
            return this.TAG;
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onFailedDownload(String str, boolean z) {
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressDownload(String str, long j, long j2) {
            float min = j2 == 0 ? 0.0f : Math.min(1.0f, ((float) j) / ((float) j2));
            RadialProgress2 radialProgress2 = this.radialProgress;
            this.media.downloadProgress = min;
            radialProgress2.setProgress(min, true);
            setIcon(min < 1.0f ? 3 : getDefaultIcon());
            this.cell.invalidate();
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressUpload(String str, long j, long j2, boolean z) {
            float min = j2 == 0 ? 0.0f : Math.min(1.0f, ((float) j) / ((float) j2));
            RadialProgress2 radialProgress2 = this.radialProgress;
            this.media.uploadProgress = min;
            radialProgress2.setProgress(min, true);
            setIcon(min < 1.0f ? 3 : this.album ? 6 : getDefaultIcon());
            this.cell.invalidate();
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onSuccessDownload(String str) {
        }

        public void setIcon(int i) {
            if (i != this.icon) {
                RadialProgress2 radialProgress2 = this.radialProgress;
                this.icon = i;
                radialProgress2.setIcon(i, true, true);
            }
        }

        public void setTime(int i) {
            int max;
            if (this.video || this.durationValue == (max = Math.max(0, this.duration - i))) {
                return;
            }
            this.durationValue = max;
            this.durationText = new Text(AndroidUtilities.formatLongDuration(max), 12.0f);
        }

        public void updateMedia(TLRPC.MessageExtendedMedia messageExtendedMedia, MessageObject messageObject) {
            ImageLocation forDocument;
            ImageLocation forDocument2;
            TLRPC.Document document;
            if (this.media == messageExtendedMedia) {
                return;
            }
            this.media = messageExtendedMedia;
            this.autoplay = false;
            String str = this.w + "_" + this.h;
            if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                this.hidden = true;
                this.filename = null;
                this.imageReceiver.setImage(ImageLocation.getForObject(((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia).thumb, messageObject.messageOwner), str + "_b2", null, null, messageObject, 0);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(1.4f);
                AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, -0.1f);
                this.imageReceiver.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            } else if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                boolean z = messageObject.isRepostPreview;
                this.hidden = z;
                if (z) {
                    str = str + "_b3";
                }
                String str2 = str;
                this.imageReceiver.setColorFilter(null);
                TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                this.filename = MessageObject.getFileName(messageMedia);
                if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                    TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = (TLRPC.TL_messageMediaPhoto) messageMedia;
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_messageMediaPhoto.photo.sizes, AndroidUtilities.getPhotoSize(), true, null, true);
                    TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tL_messageMediaPhoto.photo.sizes, Math.min(this.w, this.h) / 100, false, closestPhotoSizeWithSize, false);
                    forDocument = ImageLocation.getForPhoto(closestPhotoSizeWithSize, tL_messageMediaPhoto.photo);
                    forDocument2 = ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tL_messageMediaPhoto.photo);
                } else if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                    return;
                } else {
                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = (TLRPC.TL_messageMediaDocument) messageMedia;
                    this.autoplay = !this.hidden && !this.album && this.video && SharedConfig.isAutoplayVideo();
                    if (!this.album && this.video && (document = tL_messageMediaDocument.document) != null) {
                        TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, AndroidUtilities.getPhotoSize(), true, null, true);
                        TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(tL_messageMediaDocument.document.thumbs, Math.min(this.w, this.h), false, closestPhotoSizeWithSize3, false);
                        ImageLocation forDocument3 = ImageLocation.getForDocument(tL_messageMediaDocument.document);
                        ImageLocation forDocument4 = ImageLocation.getForDocument(closestPhotoSizeWithSize3, tL_messageMediaDocument.document);
                        ImageLocation forDocument5 = ImageLocation.getForDocument(closestPhotoSizeWithSize4, tL_messageMediaDocument.document);
                        ImageReceiver imageReceiver = this.imageReceiver;
                        ImageLocation imageLocation = this.autoplay ? forDocument3 : null;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(this.autoplay ? "_g" : "");
                        imageReceiver.setImage(imageLocation, sb.toString(), forDocument4, str2, forDocument5, str2, null, 0L, null, messageObject, 0);
                        return;
                    }
                    TLRPC.Document document2 = tL_messageMediaDocument.document;
                    if (document2 == null) {
                        return;
                    }
                    TLRPC.PhotoSize closestPhotoSizeWithSize5 = FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, AndroidUtilities.getPhotoSize(), true, null, true);
                    TLRPC.PhotoSize closestPhotoSizeWithSize6 = FileLoader.getClosestPhotoSizeWithSize(tL_messageMediaDocument.document.thumbs, Math.min(this.w, this.h), false, closestPhotoSizeWithSize5, false);
                    forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize5, tL_messageMediaDocument.document);
                    forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize6, tL_messageMediaDocument.document);
                }
                this.imageReceiver.setImage(forDocument, str2, forDocument2, str2, 0L, null, messageObject, 0);
            }
        }
    }

    public GroupMedia(ChatMessageCell chatMessageCell) {
        this.cell = chatMessageCell;
        this.spoilerEffect = SpoilerEffect2.getInstance(chatMessageCell);
        this.animatedHidden = new AnimatedFloat(chatMessageCell, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.bounce = new ButtonBounce(chatMessageCell);
    }

    public boolean allVisible() {
        Iterator it = this.holders.iterator();
        while (it.hasNext()) {
            if (!((MediaHolder) it.next()).imageReceiver.getVisible()) {
                return false;
            }
        }
        return true;
    }

    public void checkBlurBitmap() {
        int id = this.cell.getMessageObject() != null ? this.cell.getMessageObject().getId() : 0;
        int i = this.width;
        int i2 = this.height;
        int max = (int) Math.max(1.0f, i > i2 ? 100.0f : (i / i2) * 100.0f);
        int i3 = this.height;
        int i4 = this.width;
        int max2 = (int) Math.max(1.0f, i3 <= i4 ? 100.0f * (i3 / i4) : 100.0f);
        int i5 = 0;
        for (int i6 = 0; i6 < this.holders.size(); i6++) {
            MediaHolder mediaHolder = (MediaHolder) this.holders.get(i6);
            if (mediaHolder.imageReceiver.hasImageSet() && mediaHolder.imageReceiver.getBitmap() != null) {
                i5 |= 1 << i6;
            }
        }
        Bitmap bitmap = this.blurBitmap;
        if (bitmap != null && this.blurBitmapMessageId == id && this.blurBitmapState == i5 && this.blurBitmapWidth == max && this.blurBitmapHeight == max2) {
            return;
        }
        this.blurBitmapState = i5;
        this.blurBitmapMessageId = id;
        this.blurBitmapWidth = max;
        this.blurBitmapHeight = max2;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.blurBitmap = Bitmap.createBitmap(max, max2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.blurBitmap);
        float f = max / this.width;
        canvas.scale(f, f);
        for (int i7 = 0; i7 < this.holders.size(); i7++) {
            MediaHolder mediaHolder2 = (MediaHolder) this.holders.get(i7);
            ImageReceiver imageReceiver = mediaHolder2.imageReceiver;
            int i8 = mediaHolder2.l;
            int i9 = mediaHolder2.t;
            imageReceiver.setImageCoords(i8, i9, mediaHolder2.r - i8, mediaHolder2.b - i9);
            mediaHolder2.imageReceiver.draw(canvas);
        }
        Utilities.stackBlurBitmap(this.blurBitmap, 12);
        if (this.blurBitmapPaint == null) {
            this.blurBitmapPaint = new Paint(3);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(1.5f);
            this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    public void draw(Canvas canvas) {
        if (this.layout == null) {
            return;
        }
        float f = this.animatedHidden.set(this.hidden);
        drawImages(canvas, true);
        if (this.buttonText != null && f > 0.0f) {
            float scale = this.bounce.getScale(0.05f);
            float dp = AndroidUtilities.dp(28.0f) + this.buttonText.getCurrentWidth();
            float dp2 = AndroidUtilities.dp(32.0f);
            RectF rectF = this.clipRect;
            float f2 = this.x;
            float f3 = this.width;
            float f4 = this.y;
            float f5 = this.height;
            rectF.set(((f3 - dp) / 2.0f) + f2, ((f5 - dp2) / 2.0f) + f4, f2 + ((f3 + dp) / 2.0f), f4 + ((f5 + dp2) / 2.0f));
            this.clipPath.rewind();
            float f6 = dp2 / 2.0f;
            this.clipPath.addRoundRect(this.clipRect, f6, f6, Path.Direction.CW);
            canvas.save();
            canvas.scale(scale, scale, this.x + (this.width / 2.0f), this.y + (this.height / 2.0f));
            canvas.save();
            canvas.clipPath(this.clipPath);
            drawBlurred(canvas, f);
            canvas.drawColor(Theme.multAlpha(1342177280, f));
            this.buttonText.draw(canvas, ((this.x + (this.width / 2.0f)) - (dp / 2.0f)) + AndroidUtilities.dp(14.0f), (this.height / 2.0f) + this.y, -1, f);
            canvas.restore();
            if (isLoading()) {
                LoadingDrawable loadingDrawable = this.loadingDrawable;
                if (loadingDrawable == null) {
                    LoadingDrawable loadingDrawable2 = new LoadingDrawable();
                    this.loadingDrawable = loadingDrawable2;
                    loadingDrawable2.setCallback(this.cell);
                    this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.1f), Theme.multAlpha(-1, 0.3f), Theme.multAlpha(-1, 0.35f), Theme.multAlpha(-1, 0.8f));
                    this.loadingDrawable.setAppearByGradient(true);
                    this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dpf2(1.25f));
                } else if (loadingDrawable.isDisappeared() || this.loadingDrawable.isDisappearing()) {
                    this.loadingDrawable.reset();
                    this.loadingDrawable.resetDisappear();
                }
            } else {
                LoadingDrawable loadingDrawable3 = this.loadingDrawable;
                if (loadingDrawable3 != null && !loadingDrawable3.isDisappearing() && !this.loadingDrawable.isDisappeared()) {
                    this.loadingDrawable.disappear();
                }
            }
            LoadingDrawable loadingDrawable4 = this.loadingDrawable;
            if (loadingDrawable4 != null) {
                loadingDrawable4.setBounds(this.clipRect);
                this.loadingDrawable.setRadiiDp(f6);
                this.loadingDrawable.setAlpha((int) (255.0f * f));
                this.loadingDrawable.draw(canvas);
            }
            canvas.restore();
        }
        if (this.priceText == null || f >= 1.0f || !allVisible()) {
            return;
        }
        float timeAlpha = (1.0f - f) * this.cell.getTimeAlpha();
        float dp3 = AndroidUtilities.dp(11.32f) + this.priceText.getCurrentWidth();
        float dp4 = AndroidUtilities.dp(17.0f);
        float dp5 = AndroidUtilities.dp(5.0f);
        RectF rectF2 = this.clipRect;
        float f7 = this.x + this.width;
        float f8 = this.y + dp5;
        rectF2.set((f7 - dp3) - dp5, f8, f7 - dp5, f8 + dp4);
        this.clipPath.rewind();
        float f9 = dp4 / 2.0f;
        this.clipPath.addRoundRect(this.clipRect, f9, f9, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(this.clipPath);
        canvas.drawColor(Theme.multAlpha(1073741824, timeAlpha));
        this.priceText.draw(canvas, (((this.x + this.width) - dp3) - dp5) + AndroidUtilities.dp(5.66f), this.y + dp5 + f9, -1, timeAlpha);
        canvas.restore();
    }

    public void drawBlurRect(Canvas canvas, RectF rectF, float f, float f2) {
        canvas.save();
        this.clipPath.rewind();
        this.clipPath.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.clipPath);
        canvas.drawColor(1073741824);
        canvas.restore();
    }

    public void drawBlurred(Canvas canvas, float f) {
        if (this.layout == null) {
            return;
        }
        checkBlurBitmap();
        if (this.blurBitmap != null) {
            canvas.save();
            canvas.translate(this.x, this.y);
            canvas.scale(this.width / this.blurBitmap.getWidth(), this.width / this.blurBitmap.getWidth());
            this.blurBitmapPaint.setAlpha((int) (f * 255.0f));
            canvas.drawBitmap(this.blurBitmap, 0.0f, 0.0f, this.blurBitmapPaint);
            canvas.restore();
        }
    }

    public void drawImages(Canvas canvas, boolean z) {
        int i;
        float f = this.animatedHidden.set(this.hidden);
        MessageObject messageObject = this.cell.getMessageObject();
        this.clipPath2.rewind();
        float f2 = Float.MAX_VALUE;
        float f3 = Float.MAX_VALUE;
        float f4 = Float.MIN_VALUE;
        float f5 = Float.MIN_VALUE;
        int i2 = 0;
        while (i2 < this.holders.size()) {
            MediaHolder mediaHolder = (MediaHolder) this.holders.get(i2);
            ImageReceiver imageReceiver = mediaHolder.imageReceiver;
            int i3 = this.x;
            int i4 = mediaHolder.l;
            int i5 = this.y;
            int i6 = mediaHolder.t;
            imageReceiver.setImageCoords(i3 + i4, i5 + i6, mediaHolder.r - i4, mediaHolder.b - i6);
            mediaHolder.imageReceiver.draw(canvas);
            if (mediaHolder.imageReceiver.getAnimation() != null) {
                mediaHolder.setTime(Math.round(((float) mediaHolder.imageReceiver.getAnimation().currentTime) / 1000.0f));
            }
            if (f > 0.0f) {
                f3 = Math.min(this.x + mediaHolder.l, f3);
                f2 = Math.min(this.y + mediaHolder.t, f2);
                f5 = Math.max(this.x + mediaHolder.r, f5);
                f4 = Math.max(this.y + mediaHolder.b, f4);
                RectF rectF = AndroidUtilities.rectTmp;
                int i7 = this.x;
                int i8 = this.y;
                rectF.set(mediaHolder.l + i7, mediaHolder.t + i8, i7 + mediaHolder.r, i8 + mediaHolder.b);
                this.clipPath2.addRoundRect(rectF, mediaHolder.radii, Path.Direction.CW);
            }
            mediaHolder.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
            float f6 = f2;
            mediaHolder.radialProgress.setProgressRect(mediaHolder.imageReceiver.getImageX() + ((mediaHolder.imageReceiver.getImageWidth() / 2.0f) - mediaHolder.radialProgress.getRadius()), mediaHolder.imageReceiver.getImageY() + ((mediaHolder.imageReceiver.getImageHeight() / 2.0f) - mediaHolder.radialProgress.getRadius()), mediaHolder.imageReceiver.getImageX() + (mediaHolder.imageReceiver.getImageWidth() / 2.0f) + mediaHolder.radialProgress.getRadius(), mediaHolder.imageReceiver.getImageY() + (mediaHolder.imageReceiver.getImageHeight() / 2.0f) + mediaHolder.radialProgress.getRadius());
            if (messageObject.isSending()) {
                SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(messageObject.currentAccount);
                long[] fileProgressSizes = ImageLoader.getInstance().getFileProgressSizes(mediaHolder.attachPath);
                boolean isSendingPaidMessage = sendMessagesHelper.isSendingPaidMessage(messageObject.getId(), i2);
                if (fileProgressSizes == null && isSendingPaidMessage) {
                    mediaHolder.radialProgress.setProgress(1.0f, true);
                    if (mediaHolder.album) {
                        i = 6;
                        mediaHolder.setIcon(i);
                    }
                    i = mediaHolder.getDefaultIcon();
                    mediaHolder.setIcon(i);
                }
                canvas.saveLayerAlpha(mediaHolder.radialProgress.getProgressRect(), (int) ((1.0f - f) * 255.0f), 31);
                mediaHolder.radialProgress.draw(canvas);
                canvas.restore();
                i2++;
                f2 = f6;
            } else {
                if (FileLoader.getInstance(messageObject.currentAccount).isLoadingFile(mediaHolder.filename)) {
                    i = 3;
                    mediaHolder.setIcon(i);
                    canvas.saveLayerAlpha(mediaHolder.radialProgress.getProgressRect(), (int) ((1.0f - f) * 255.0f), 31);
                    mediaHolder.radialProgress.draw(canvas);
                    canvas.restore();
                    i2++;
                    f2 = f6;
                }
                i = mediaHolder.getDefaultIcon();
                mediaHolder.setIcon(i);
                canvas.saveLayerAlpha(mediaHolder.radialProgress.getProgressRect(), (int) ((1.0f - f) * 255.0f), 31);
                mediaHolder.radialProgress.draw(canvas);
                canvas.restore();
                i2++;
                f2 = f6;
            }
        }
        if (f > 0.0f && z) {
            canvas.save();
            canvas.clipPath(this.clipPath2);
            canvas.translate(f3, f2);
            int i9 = (int) (f5 - f3);
            int i10 = (int) (f4 - f2);
            canvas.saveLayerAlpha(0.0f, 0.0f, i9, i10, (int) (255.0f * f), 31);
            SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
            ChatMessageCell chatMessageCell = this.cell;
            spoilerEffect2.draw(canvas, chatMessageCell, i9, i10, 1.0f, chatMessageCell.drawingToBitmap);
            canvas.restore();
            canvas.restore();
            this.cell.invalidate();
        }
        for (int i11 = 0; i11 < this.holders.size(); i11++) {
            MediaHolder mediaHolder2 = (MediaHolder) this.holders.get(i11);
            if (mediaHolder2.durationText != null) {
                float dp = AndroidUtilities.dp(11.4f) + mediaHolder2.durationText.getCurrentWidth();
                float dp2 = AndroidUtilities.dp(17.0f);
                float dp3 = AndroidUtilities.dp(5.0f);
                float f7 = this.x + mediaHolder2.l + dp3;
                float f8 = this.y + mediaHolder2.t + dp3;
                this.clipRect.set(f7, f8, dp + f7, f8 + dp2);
                if (this.priceText == null || this.clipRect.right <= ((this.x + this.width) - (AndroidUtilities.dp(11.32f) + this.priceText.getCurrentWidth())) - dp3 || this.clipRect.top > this.y + dp3) {
                    this.clipPath.rewind();
                    float f9 = dp2 / 2.0f;
                    this.clipPath.addRoundRect(this.clipRect, f9, f9, Path.Direction.CW);
                    canvas.save();
                    canvas.clipPath(this.clipPath);
                    drawBlurred(canvas, f);
                    canvas.drawColor(Theme.multAlpha(1073741824, 1.0f));
                    mediaHolder2.durationText.draw(canvas, this.x + mediaHolder2.l + dp3 + AndroidUtilities.dp(5.66f), this.y + mediaHolder2.t + dp3 + f9, -1, 1.0f);
                    canvas.restore();
                }
            }
        }
    }

    public MediaHolder getHolderAt(float f, float f2) {
        for (int i = 0; i < this.holders.size(); i++) {
            if (((MediaHolder) this.holders.get(i)).imageReceiver.isInsideImage(f, f2)) {
                return (MediaHolder) this.holders.get(i);
            }
        }
        return null;
    }

    public ImageReceiver getPhotoImage(int i) {
        GroupedMessages groupedMessages = this.layout;
        if (groupedMessages != null && i >= 0 && i < groupedMessages.medias.size()) {
            TLRPC.MessageExtendedMedia messageExtendedMedia = (TLRPC.MessageExtendedMedia) this.layout.medias.get(i);
            for (int i2 = 0; i2 < this.holders.size(); i2++) {
                if (((MediaHolder) this.holders.get(i2)).media == messageExtendedMedia) {
                    return ((MediaHolder) this.holders.get(i2)).imageReceiver;
                }
            }
        }
        return null;
    }

    public boolean isLoading() {
        return this.cell.getDelegate() != null && this.cell.getDelegate().isProgressLoading(this.cell, 5);
    }

    public void onAttachedToWindow() {
        if (this.attached) {
            return;
        }
        this.attached = true;
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this.cell);
        }
        for (int i = 0; i < this.holders.size(); i++) {
            ((MediaHolder) this.holders.get(i)).attach();
        }
    }

    public void onDetachedFromWindow() {
        if (this.attached) {
            this.attached = false;
            SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
            if (spoilerEffect2 != null) {
                spoilerEffect2.attach(this.cell);
            }
            for (int i = 0; i < this.holders.size(); i++) {
                ((MediaHolder) this.holders.get(i)).detach();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (motionEvent.getAction() == 0) {
            MediaHolder holderAt = getHolderAt(x, y);
            this.pressHolder = holderAt;
            this.pressButton = (holderAt == null || holderAt.radialProgress.getIcon() == 4 || !this.pressHolder.radialProgress.getProgressRect().contains(x, y)) ? false : true;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            MediaHolder holderAt2 = getHolderAt(x, y);
            boolean z = (holderAt2 == null || holderAt2.radialProgress.getIcon() == 4 || !holderAt2.radialProgress.getProgressRect().contains(x, y)) ? false : true;
            MediaHolder mediaHolder = this.pressHolder;
            if (mediaHolder != null && mediaHolder == holderAt2 && this.cell.getDelegate() != null && motionEvent.getAction() == 1) {
                MessageObject messageObject = this.cell.getMessageObject();
                if (!this.pressButton || !z || holderAt2.radialProgress.getIcon() != 3 || messageObject == null) {
                    ChatMessageCell.ChatMessageCellDelegate delegate = this.cell.getDelegate();
                    ChatMessageCell chatMessageCell = this.cell;
                    MediaHolder mediaHolder2 = this.pressHolder;
                    delegate.didPressGroupImage(chatMessageCell, mediaHolder2.imageReceiver, mediaHolder2.media, motionEvent.getX(), motionEvent.getY());
                } else if (messageObject.isSending()) {
                    SendMessagesHelper.getInstance(messageObject.currentAccount).cancelSendingMessage(messageObject);
                }
            }
            this.pressButton = false;
            this.pressHolder = null;
        }
        this.bounce.setPressed(this.pressHolder != null);
        return this.pressHolder != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0124 A[EDGE_INSN: B:81:0x0124->B:60:0x0124 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z, boolean z2) {
        TLRPC.Message message;
        int min;
        float f;
        int i;
        int size;
        MessageObject.GroupedMessagePosition position;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC.MessageMedia messageMedia = message.media;
        if (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) {
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia;
            if (this.layout == null) {
                this.layout = new GroupedMessages();
            }
            this.layout.medias.clear();
            this.layout.medias.addAll(tL_messageMediaPaidMedia.extended_media);
            this.layout.calculate();
            int i2 = this.overrideWidth;
            if (i2 <= 0) {
                if (AndroidUtilities.isTablet()) {
                    min = AndroidUtilities.getMinTabletSide();
                    f = 122.0f;
                } else {
                    min = Math.min(this.cell.getParentWidth(), AndroidUtilities.displaySize.y);
                    f = (this.cell.checkNeedDrawShareButton(messageObject) ? 10 : 0) + 64;
                }
                this.maxWidth = min - AndroidUtilities.dp(f);
                if (this.cell.needDrawAvatar()) {
                    i2 = this.maxWidth - AndroidUtilities.dp(52.0f);
                }
                i = 0;
                while (true) {
                    String str = null;
                    if (i < tL_messageMediaPaidMedia.extended_media.size()) {
                        break;
                    }
                    TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i);
                    MediaHolder mediaHolder = i >= this.holders.size() ? null : (MediaHolder) this.holders.get(i);
                    if (mediaHolder == null) {
                        MediaHolder mediaHolder2 = new MediaHolder(this.cell, messageObject, messageExtendedMedia, tL_messageMediaPaidMedia.extended_media.size() != 1, (int) ((position.pw / 1000.0f) * this.maxWidth), (int) (this.layout.getPosition(messageExtendedMedia).ph * this.layout.maxSizeHeight));
                        String str2 = messageExtendedMedia.attachPath;
                        if (str2 != null) {
                            str = str2;
                        } else {
                            if (tL_messageMediaPaidMedia.extended_media.size() == 1) {
                                TLRPC.Message message2 = messageObject.messageOwner;
                                if (message2 != null) {
                                    str = message2.attachPath;
                                }
                            }
                            if (!TextUtils.isEmpty(mediaHolder2.attachPath)) {
                                DownloadController.getInstance(this.cell.currentAccount).addLoadingFileObserver(mediaHolder2.attachPath, messageObject, mediaHolder2);
                                if (messageObject.isSending()) {
                                    mediaHolder2.radialProgress.setProgress(messageExtendedMedia.uploadProgress, false);
                                }
                            }
                            if (this.cell.isCellAttachedToWindow()) {
                                mediaHolder2.attach();
                            }
                            this.holders.add(mediaHolder2);
                        }
                        mediaHolder2.attachPath = str;
                        if (!TextUtils.isEmpty(mediaHolder2.attachPath)) {
                        }
                        if (this.cell.isCellAttachedToWindow()) {
                        }
                        this.holders.add(mediaHolder2);
                    } else {
                        mediaHolder.updateMedia(messageExtendedMedia, messageObject);
                    }
                    i++;
                }
                size = tL_messageMediaPaidMedia.extended_media.size();
                while (size < this.holders.size()) {
                    MediaHolder mediaHolder3 = size >= this.holders.size() ? null : (MediaHolder) this.holders.get(size);
                    if (mediaHolder3 != null) {
                        mediaHolder3.detach();
                        this.holders.remove(size);
                        size--;
                    }
                    size++;
                }
                updateHolders(messageObject);
                GroupedMessages groupedMessages = this.layout;
                this.width = (int) ((groupedMessages.width / 1000.0f) * this.maxWidth);
                this.height = (int) (groupedMessages.height * groupedMessages.maxSizeHeight);
                if (this.hidden) {
                    long j = tL_messageMediaPaidMedia.stars_amount;
                    this.buttonTextPrice = j;
                    Text text = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) j), 0.7f), 14.0f, AndroidUtilities.bold());
                    this.buttonText = text;
                    if (text.getCurrentWidth() > this.width - AndroidUtilities.dp(30.0f)) {
                        long j2 = tL_messageMediaPaidMedia.stars_amount;
                        this.buttonTextPrice = j2;
                        this.buttonText = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContentShort", (int) j2), 0.7f), 14.0f, AndroidUtilities.bold());
                    }
                }
                if (this.priceText == null && this.priceTextPrice == tL_messageMediaPaidMedia.stars_amount) {
                    return;
                }
                long j3 = tL_messageMediaPaidMedia.stars_amount;
                this.priceTextPrice = j3;
                this.priceText = new Text(StarsIntroActivity.replaceStars(LocaleController.formatPluralStringComma("PaidMediaPrice", (int) j3), 0.9f), 12.0f, AndroidUtilities.bold());
            }
            this.maxWidth = i2;
            i = 0;
            while (true) {
                String str3 = null;
                if (i < tL_messageMediaPaidMedia.extended_media.size()) {
                }
                i++;
            }
            size = tL_messageMediaPaidMedia.extended_media.size();
            while (size < this.holders.size()) {
            }
            updateHolders(messageObject);
            GroupedMessages groupedMessages2 = this.layout;
            this.width = (int) ((groupedMessages2.width / 1000.0f) * this.maxWidth);
            this.height = (int) (groupedMessages2.height * groupedMessages2.maxSizeHeight);
            if (this.hidden) {
            }
            if (this.priceText == null) {
            }
            long j32 = tL_messageMediaPaidMedia.stars_amount;
            this.priceTextPrice = j32;
            this.priceText = new Text(StarsIntroActivity.replaceStars(LocaleController.formatPluralStringComma("PaidMediaPrice", (int) j32), 0.9f), 12.0f, AndroidUtilities.bold());
        }
    }

    public void setOverrideWidth(int i) {
        this.overrideWidth = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:125:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateHolders(MessageObject messageObject) {
        boolean z;
        int i;
        int min;
        float f;
        float f2;
        int i2;
        int i3;
        int i4;
        ChatMessageCell chatMessageCell = this.cell;
        boolean z2 = chatMessageCell.namesOffset > 0 || (chatMessageCell.captionAbove && !TextUtils.isEmpty(messageObject.caption));
        if (this.cell.captionAbove || TextUtils.isEmpty(messageObject.caption)) {
            ChatMessageCell chatMessageCell2 = this.cell;
            if (chatMessageCell2.reactionsLayoutInBubble.isEmpty && !chatMessageCell2.hasCommentLayout()) {
                z = false;
                i = this.overrideWidth;
                float f3 = 1000.0f;
                if (i <= 0) {
                    f2 = 1000.0f / this.layout.width;
                    this.maxWidth = i;
                } else {
                    if (AndroidUtilities.isTablet()) {
                        min = AndroidUtilities.getMinTabletSide();
                        f = 122.0f;
                    } else {
                        min = Math.min(this.cell.getParentWidth(), AndroidUtilities.displaySize.y);
                        f = (this.cell.checkNeedDrawShareButton(messageObject) ? 10 : 0) + 64;
                    }
                    this.maxWidth = min - AndroidUtilities.dp(f);
                    if (this.cell.needDrawAvatar()) {
                        this.maxWidth -= AndroidUtilities.dp(52.0f);
                    }
                    f2 = 1.0f;
                }
                GroupedMessages groupedMessages = this.layout;
                this.width = (int) ((groupedMessages.width / 1000.0f) * f2 * this.maxWidth);
                this.height = (int) (groupedMessages.height * groupedMessages.maxSizeHeight);
                this.hidden = false;
                int dp = AndroidUtilities.dp(1.0f);
                int dp2 = AndroidUtilities.dp(4.0f);
                int dp3 = AndroidUtilities.dp(i2 - (SharedConfig.bubbleRadius <= 2 ? 2 : 0));
                int min2 = Math.min(AndroidUtilities.dp(3.0f), dp3);
                i3 = 0;
                while (i3 < this.holders.size()) {
                    MediaHolder mediaHolder = (MediaHolder) this.holders.get(i3);
                    MessageObject.GroupedMessagePosition position = this.layout.getPosition(mediaHolder.media);
                    if (position == null) {
                        i4 = dp2;
                    } else {
                        float f4 = this.maxWidth;
                        int i5 = (int) ((position.left / f3) * f2 * f4);
                        float f5 = position.top;
                        float f6 = this.layout.maxSizeHeight;
                        int i6 = (int) (f5 * f6);
                        i4 = dp2;
                        int i7 = (int) ((position.pw / 1000.0f) * f2 * f4);
                        int i8 = (int) (position.ph * f6);
                        int i9 = position.flags;
                        if ((i9 & 1) == 0) {
                            i5 += dp;
                            i7 -= dp;
                        }
                        if ((i9 & 4) == 0) {
                            i6 += dp;
                            i8 -= dp;
                        }
                        if ((i9 & 2) == 0) {
                            i7 -= dp;
                        }
                        if ((i9 & 8) == 0) {
                            i8 -= dp;
                        }
                        mediaHolder.l = i5;
                        mediaHolder.t = i6;
                        mediaHolder.r = i5 + i7;
                        mediaHolder.b = i6 + i8;
                        mediaHolder.imageReceiver.setImageCoords(i5, i6, i7, i8);
                        int i10 = position.flags;
                        int i11 = i10 & 4;
                        int i12 = (i11 == 0 || (i10 & 1) == 0 || z2) ? i4 : dp3;
                        int i13 = (i11 == 0 || (i10 & 2) == 0 || z2) ? i4 : dp3;
                        int i14 = i10 & 8;
                        int i15 = (i14 == 0 || (i10 & 1) == 0 || z) ? i4 : dp3;
                        int i16 = (i14 == 0 || (i10 & 2) == 0 || z) ? i4 : dp3;
                        if (!z) {
                            if (messageObject.isOutOwner()) {
                                i16 = i4;
                            } else {
                                i15 = i4;
                            }
                        }
                        if (!z2 && this.cell.pinnedTop) {
                            if (messageObject.isOutOwner()) {
                                i13 = min2;
                            } else {
                                i12 = min2;
                            }
                        }
                        mediaHolder.imageReceiver.setRoundRadius(i12, i13, i16, i15);
                        float[] fArr = mediaHolder.radii;
                        float f7 = i12;
                        fArr[1] = f7;
                        fArr[0] = f7;
                        float f8 = i13;
                        fArr[3] = f8;
                        fArr[2] = f8;
                        float f9 = i16;
                        fArr[5] = f9;
                        fArr[4] = f9;
                        float f10 = i15;
                        fArr[7] = f10;
                        fArr[6] = f10;
                        if (messageObject != null && messageObject.isSending()) {
                            mediaHolder.setIcon(3);
                        }
                        this.hidden = this.hidden || mediaHolder.hidden;
                    }
                    i3++;
                    dp2 = i4;
                    f3 = 1000.0f;
                }
                if (this.hidden) {
                    return;
                }
                TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = messageObject == null ? null : (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
                if (tL_messageMediaPaidMedia != null) {
                    long j = tL_messageMediaPaidMedia.stars_amount;
                    this.buttonTextPrice = j;
                    Text text = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) j), 0.7f), 14.0f, AndroidUtilities.bold());
                    this.buttonText = text;
                    if (text.getCurrentWidth() > this.width - AndroidUtilities.dp(30.0f)) {
                        long j2 = tL_messageMediaPaidMedia.stars_amount;
                        this.buttonTextPrice = j2;
                        this.buttonText = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContentShort", (int) j2), 0.7f), 14.0f, AndroidUtilities.bold());
                        return;
                    }
                    return;
                }
                return;
            }
        }
        z = true;
        i = this.overrideWidth;
        float f32 = 1000.0f;
        if (i <= 0) {
        }
        GroupedMessages groupedMessages2 = this.layout;
        this.width = (int) ((groupedMessages2.width / 1000.0f) * f2 * this.maxWidth);
        this.height = (int) (groupedMessages2.height * groupedMessages2.maxSizeHeight);
        this.hidden = false;
        int dp4 = AndroidUtilities.dp(1.0f);
        int dp22 = AndroidUtilities.dp(4.0f);
        int dp32 = AndroidUtilities.dp(i2 - (SharedConfig.bubbleRadius <= 2 ? 2 : 0));
        int min22 = Math.min(AndroidUtilities.dp(3.0f), dp32);
        i3 = 0;
        while (i3 < this.holders.size()) {
        }
        if (this.hidden) {
        }
    }
}
