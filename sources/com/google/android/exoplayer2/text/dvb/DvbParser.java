package com.google.android.exoplayer2.text.dvb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.SparseArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class DvbParser {
    private static final byte[] defaultMap2To4 = {0, 7, 8, 15};
    private static final byte[] defaultMap2To8 = {0, 119, -120, -1};
    private static final byte[] defaultMap4To8 = {0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1};
    private Bitmap bitmap;
    private final Canvas canvas;
    private final ClutDefinition defaultClutDefinition;
    private final DisplayDefinition defaultDisplayDefinition;
    private final Paint defaultPaint;
    private final Paint fillRegionPaint;
    private final SubtitleService subtitleService;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ClutDefinition {
        public final int[] clutEntries2Bit;
        public final int[] clutEntries4Bit;
        public final int[] clutEntries8Bit;
        public final int id;

        public ClutDefinition(int i, int[] iArr, int[] iArr2, int[] iArr3) {
            this.id = i;
            this.clutEntries2Bit = iArr;
            this.clutEntries4Bit = iArr2;
            this.clutEntries8Bit = iArr3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class DisplayDefinition {
        public final int height;
        public final int horizontalPositionMaximum;
        public final int horizontalPositionMinimum;
        public final int verticalPositionMaximum;
        public final int verticalPositionMinimum;
        public final int width;

        public DisplayDefinition(int i, int i2, int i3, int i4, int i5, int i6) {
            this.width = i;
            this.height = i2;
            this.horizontalPositionMinimum = i3;
            this.horizontalPositionMaximum = i4;
            this.verticalPositionMinimum = i5;
            this.verticalPositionMaximum = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ObjectData {
        public final byte[] bottomFieldData;
        public final int id;
        public final boolean nonModifyingColorFlag;
        public final byte[] topFieldData;

        public ObjectData(int i, boolean z, byte[] bArr, byte[] bArr2) {
            this.id = i;
            this.nonModifyingColorFlag = z;
            this.topFieldData = bArr;
            this.bottomFieldData = bArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PageComposition {
        public final SparseArray regions;
        public final int state;
        public final int timeOutSecs;
        public final int version;

        public PageComposition(int i, int i2, int i3, SparseArray sparseArray) {
            this.timeOutSecs = i;
            this.version = i2;
            this.state = i3;
            this.regions = sparseArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PageRegion {
        public final int horizontalAddress;
        public final int verticalAddress;

        public PageRegion(int i, int i2) {
            this.horizontalAddress = i;
            this.verticalAddress = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RegionComposition {
        public final int clutId;
        public final int depth;
        public final boolean fillFlag;
        public final int height;
        public final int id;
        public final int levelOfCompatibility;
        public final int pixelCode2Bit;
        public final int pixelCode4Bit;
        public final int pixelCode8Bit;
        public final SparseArray regionObjects;
        public final int width;

        public RegionComposition(int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, SparseArray sparseArray) {
            this.id = i;
            this.fillFlag = z;
            this.width = i2;
            this.height = i3;
            this.levelOfCompatibility = i4;
            this.depth = i5;
            this.clutId = i6;
            this.pixelCode8Bit = i7;
            this.pixelCode4Bit = i8;
            this.pixelCode2Bit = i9;
            this.regionObjects = sparseArray;
        }

        public void mergeFrom(RegionComposition regionComposition) {
            SparseArray sparseArray = regionComposition.regionObjects;
            for (int i = 0; i < sparseArray.size(); i++) {
                this.regionObjects.put(sparseArray.keyAt(i), (RegionObject) sparseArray.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RegionObject {
        public final int backgroundPixelCode;
        public final int foregroundPixelCode;
        public final int horizontalPosition;
        public final int provider;
        public final int type;
        public final int verticalPosition;

        public RegionObject(int i, int i2, int i3, int i4, int i5, int i6) {
            this.type = i;
            this.provider = i2;
            this.horizontalPosition = i3;
            this.verticalPosition = i4;
            this.foregroundPixelCode = i5;
            this.backgroundPixelCode = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SubtitleService {
        public final int ancillaryPageId;
        public DisplayDefinition displayDefinition;
        public PageComposition pageComposition;
        public final int subtitlePageId;
        public final SparseArray regions = new SparseArray();
        public final SparseArray cluts = new SparseArray();
        public final SparseArray objects = new SparseArray();
        public final SparseArray ancillaryCluts = new SparseArray();
        public final SparseArray ancillaryObjects = new SparseArray();

        public SubtitleService(int i, int i2) {
            this.subtitlePageId = i;
            this.ancillaryPageId = i2;
        }

        public void reset() {
            this.regions.clear();
            this.cluts.clear();
            this.objects.clear();
            this.ancillaryCluts.clear();
            this.ancillaryObjects.clear();
            this.displayDefinition = null;
            this.pageComposition = null;
        }
    }

    public DvbParser(int i, int i2) {
        Paint paint = new Paint();
        this.defaultPaint = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setPathEffect(null);
        Paint paint2 = new Paint();
        this.fillRegionPaint = paint2;
        paint2.setStyle(Paint.Style.FILL);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        paint2.setPathEffect(null);
        this.canvas = new Canvas();
        this.defaultDisplayDefinition = new DisplayDefinition(719, 575, 0, 719, 0, 575);
        this.defaultClutDefinition = new ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
        this.subtitleService = new SubtitleService(i, i2);
    }

    private static byte[] buildClutMapTable(int i, int i2, ParsableBitArray parsableBitArray) {
        byte[] bArr = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr[i3] = (byte) parsableBitArray.readBits(i2);
        }
        return bArr;
    }

    private static int[] generateDefault2BitClutEntries() {
        return new int[]{0, -1, -16777216, -8421505};
    }

    private static int[] generateDefault4BitClutEntries() {
        int[] iArr = new int[16];
        iArr[0] = 0;
        for (int i = 1; i < 16; i++) {
            if (i < 8) {
                iArr[i] = getColor(NotificationCenter.notificationsCountUpdated, (i & 1) != 0 ? NotificationCenter.notificationsCountUpdated : 0, (i & 2) != 0 ? NotificationCenter.notificationsCountUpdated : 0, (i & 4) != 0 ? NotificationCenter.notificationsCountUpdated : 0);
            } else {
                int i2 = i & 1;
                int i3 = NotificationCenter.dialogTranslate;
                int i4 = i2 != 0 ? NotificationCenter.dialogTranslate : 0;
                int i5 = (i & 2) != 0 ? NotificationCenter.dialogTranslate : 0;
                if ((i & 4) == 0) {
                    i3 = 0;
                }
                iArr[i] = getColor(NotificationCenter.notificationsCountUpdated, i4, i5, i3);
            }
        }
        return iArr;
    }

    private static int[] generateDefault8BitClutEntries() {
        int i;
        int[] iArr = new int[256];
        iArr[0] = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            int i3 = NotificationCenter.notificationsCountUpdated;
            if (i2 < 8) {
                int i4 = (i2 & 1) != 0 ? NotificationCenter.notificationsCountUpdated : 0;
                int i5 = (i2 & 2) != 0 ? NotificationCenter.notificationsCountUpdated : 0;
                if ((i2 & 4) == 0) {
                    i3 = 0;
                }
                iArr[i2] = getColor(63, i4, i5, i3);
            } else {
                int i6 = i2 & NotificationCenter.fileUploadProgressChanged;
                int i7 = NotificationCenter.groupCallVisibilityChanged;
                if (i6 == 0) {
                    int i8 = ((i2 & 1) != 0 ? 85 : 0) + ((i2 & 16) != 0 ? NotificationCenter.groupCallVisibilityChanged : 0);
                    int i9 = ((i2 & 2) != 0 ? 85 : 0) + ((i2 & 32) != 0 ? NotificationCenter.groupCallVisibilityChanged : 0);
                    i = (i2 & 4) == 0 ? 0 : 85;
                    if ((i2 & 64) == 0) {
                        i7 = 0;
                    }
                    iArr[i2] = getColor(NotificationCenter.notificationsCountUpdated, i8, i9, i + i7);
                } else if (i6 == 8) {
                    int i10 = ((i2 & 1) != 0 ? 85 : 0) + ((i2 & 16) != 0 ? NotificationCenter.groupCallVisibilityChanged : 0);
                    int i11 = ((i2 & 2) != 0 ? 85 : 0) + ((i2 & 32) != 0 ? NotificationCenter.groupCallVisibilityChanged : 0);
                    i = (i2 & 4) == 0 ? 0 : 85;
                    if ((i2 & 64) == 0) {
                        i7 = 0;
                    }
                    iArr[i2] = getColor(NotificationCenter.dialogTranslate, i10, i11, i + i7);
                } else if (i6 == 128) {
                    iArr[i2] = getColor(NotificationCenter.notificationsCountUpdated, ((i2 & 1) != 0 ? 43 : 0) + NotificationCenter.dialogTranslate + ((i2 & 16) != 0 ? 85 : 0), ((i2 & 2) != 0 ? 43 : 0) + NotificationCenter.dialogTranslate + ((i2 & 32) != 0 ? 85 : 0), ((i2 & 4) == 0 ? 0 : 43) + NotificationCenter.dialogTranslate + ((i2 & 64) == 0 ? 0 : 85));
                } else if (i6 == 136) {
                    iArr[i2] = getColor(NotificationCenter.notificationsCountUpdated, ((i2 & 1) != 0 ? 43 : 0) + ((i2 & 16) != 0 ? 85 : 0), ((i2 & 2) != 0 ? 43 : 0) + ((i2 & 32) != 0 ? 85 : 0), ((i2 & 4) == 0 ? 0 : 43) + ((i2 & 64) == 0 ? 0 : 85));
                }
            }
        }
        return iArr;
    }

    private static int getColor(int i, int i2, int i3, int i4) {
        return (i << 24) | (i2 << 16) | (i3 << 8) | i4;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x007b A[LOOP:0: B:2:0x0009->B:13:0x007b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x007a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x005b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int paint2BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        boolean z;
        int i3;
        int readBits;
        int i4 = i;
        boolean z2 = false;
        while (true) {
            int readBits2 = parsableBitArray.readBits(2);
            if (readBits2 != 0) {
                z = z2;
            } else {
                if (parsableBitArray.readBit()) {
                    readBits = parsableBitArray.readBits(3) + 3;
                } else if (parsableBitArray.readBit()) {
                    z = z2;
                    readBits2 = 0;
                } else {
                    int readBits3 = parsableBitArray.readBits(2);
                    if (readBits3 == 0) {
                        readBits2 = 0;
                        z = true;
                    } else if (readBits3 == 1) {
                        z = z2;
                        readBits2 = 0;
                        i3 = 2;
                        if (i3 != 0 && paint != null) {
                            if (bArr != null) {
                                readBits2 = bArr[readBits2];
                            }
                            paint.setColor(iArr[readBits2]);
                            canvas.drawRect(i4, i2, i4 + i3, i2 + 1, paint);
                        }
                        i4 += i3;
                        if (!z) {
                            return i4;
                        }
                        z2 = z;
                    } else if (readBits3 == 2) {
                        readBits = parsableBitArray.readBits(4) + 12;
                    } else if (readBits3 != 3) {
                        z = z2;
                        readBits2 = 0;
                    } else {
                        readBits = parsableBitArray.readBits(8) + 29;
                    }
                    i3 = 0;
                    if (i3 != 0) {
                        if (bArr != null) {
                        }
                        paint.setColor(iArr[readBits2]);
                        canvas.drawRect(i4, i2, i4 + i3, i2 + 1, paint);
                    }
                    i4 += i3;
                    if (!z) {
                    }
                }
                z = z2;
                i3 = readBits;
                readBits2 = parsableBitArray.readBits(2);
                if (i3 != 0) {
                }
                i4 += i3;
                if (!z) {
                }
            }
            i3 = 1;
            if (i3 != 0) {
            }
            i4 += i3;
            if (!z) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0087 A[LOOP:0: B:2:0x0009->B:13:0x0087, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0086 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int paint4BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        boolean z;
        int i3;
        int readBits;
        int i4 = i;
        boolean z2 = false;
        while (true) {
            int readBits2 = parsableBitArray.readBits(4);
            if (readBits2 != 0) {
                z = z2;
            } else {
                if (parsableBitArray.readBit()) {
                    if (parsableBitArray.readBit()) {
                        int readBits3 = parsableBitArray.readBits(2);
                        if (readBits3 == 0) {
                            z = z2;
                            readBits2 = 0;
                        } else if (readBits3 == 1) {
                            z = z2;
                            readBits2 = 0;
                            i3 = 2;
                        } else if (readBits3 == 2) {
                            readBits = parsableBitArray.readBits(4) + 9;
                        } else if (readBits3 != 3) {
                            z = z2;
                            readBits2 = 0;
                            i3 = 0;
                        } else {
                            readBits = parsableBitArray.readBits(8) + 25;
                        }
                    } else {
                        readBits = parsableBitArray.readBits(2) + 4;
                    }
                    z = z2;
                    i3 = readBits;
                    readBits2 = parsableBitArray.readBits(4);
                } else {
                    int readBits4 = parsableBitArray.readBits(3);
                    if (readBits4 != 0) {
                        z = z2;
                        i3 = readBits4 + 2;
                        readBits2 = 0;
                    } else {
                        readBits2 = 0;
                        z = true;
                        i3 = 0;
                    }
                }
                if (i3 != 0 && paint != null) {
                    if (bArr != null) {
                        readBits2 = bArr[readBits2];
                    }
                    paint.setColor(iArr[readBits2]);
                    canvas.drawRect(i4, i2, i4 + i3, i2 + 1, paint);
                }
                i4 += i3;
                if (!z) {
                    return i4;
                }
                z2 = z;
            }
            i3 = 1;
            if (i3 != 0) {
                if (bArr != null) {
                }
                paint.setColor(iArr[readBits2]);
                canvas.drawRect(i4, i2, i4 + i3, i2 + 1, paint);
            }
            i4 += i3;
            if (!z) {
            }
        }
    }

    private static int paint8BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        boolean z;
        int readBits;
        int i3 = i;
        boolean z2 = false;
        while (true) {
            int readBits2 = parsableBitArray.readBits(8);
            if (readBits2 != 0) {
                z = z2;
                readBits = 1;
            } else if (parsableBitArray.readBit()) {
                z = z2;
                readBits = parsableBitArray.readBits(7);
                readBits2 = parsableBitArray.readBits(8);
            } else {
                int readBits3 = parsableBitArray.readBits(7);
                if (readBits3 != 0) {
                    z = z2;
                    readBits = readBits3;
                    readBits2 = 0;
                } else {
                    readBits2 = 0;
                    z = true;
                    readBits = 0;
                }
            }
            if (readBits != 0 && paint != null) {
                if (bArr != null) {
                    readBits2 = bArr[readBits2];
                }
                paint.setColor(iArr[readBits2]);
                canvas.drawRect(i3, i2, i3 + readBits, i2 + 1, paint);
            }
            i3 += readBits;
            if (z) {
                return i3;
            }
            z2 = z;
        }
    }

    private static void paintPixelDataSubBlock(byte[] bArr, int[] iArr, int i, int i2, int i3, Paint paint, Canvas canvas) {
        byte[] bArr2;
        byte[] bArr3;
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr);
        int i4 = i2;
        int i5 = i3;
        byte[] bArr4 = null;
        byte[] bArr5 = null;
        byte[] bArr6 = null;
        while (parsableBitArray.bitsLeft() != 0) {
            int readBits = parsableBitArray.readBits(8);
            if (readBits != 240) {
                switch (readBits) {
                    case 16:
                        if (i != 3) {
                            if (i != 2) {
                                bArr2 = null;
                            } else if (bArr6 == null) {
                                bArr3 = defaultMap2To4;
                                bArr2 = bArr3;
                            } else {
                                bArr2 = bArr6;
                            }
                            i4 = paint2BitPixelCodeString(parsableBitArray, iArr, bArr2, i4, i5, paint, canvas);
                        } else if (bArr4 == null) {
                            bArr3 = defaultMap2To8;
                            bArr2 = bArr3;
                            i4 = paint2BitPixelCodeString(parsableBitArray, iArr, bArr2, i4, i5, paint, canvas);
                            break;
                        } else {
                            bArr2 = bArr4;
                            i4 = paint2BitPixelCodeString(parsableBitArray, iArr, bArr2, i4, i5, paint, canvas);
                        }
                    case 17:
                        i4 = paint4BitPixelCodeString(parsableBitArray, iArr, i == 3 ? bArr5 == null ? defaultMap4To8 : bArr5 : null, i4, i5, paint, canvas);
                        break;
                    case 18:
                        i4 = paint8BitPixelCodeString(parsableBitArray, iArr, null, i4, i5, paint, canvas);
                        continue;
                    default:
                        switch (readBits) {
                            case 32:
                                bArr6 = buildClutMapTable(4, 4, parsableBitArray);
                                break;
                            case 33:
                                bArr4 = buildClutMapTable(4, 8, parsableBitArray);
                                break;
                            case 34:
                                bArr5 = buildClutMapTable(16, 8, parsableBitArray);
                                break;
                            default:
                                continue;
                        }
                }
                parsableBitArray.byteAlign();
            } else {
                i5 += 2;
                i4 = i2;
            }
        }
    }

    private static void paintPixelDataSubBlocks(ObjectData objectData, ClutDefinition clutDefinition, int i, int i2, int i3, Paint paint, Canvas canvas) {
        int[] iArr = i == 3 ? clutDefinition.clutEntries8Bit : i == 2 ? clutDefinition.clutEntries4Bit : clutDefinition.clutEntries2Bit;
        paintPixelDataSubBlock(objectData.topFieldData, iArr, i, i2, i3, paint, canvas);
        paintPixelDataSubBlock(objectData.bottomFieldData, iArr, i, i2, i3 + 1, paint, canvas);
    }

    private static ClutDefinition parseClutDefinition(ParsableBitArray parsableBitArray, int i) {
        int readBits;
        int i2;
        int readBits2;
        int i3;
        int i4;
        int i5 = 8;
        int readBits3 = parsableBitArray.readBits(8);
        parsableBitArray.skipBits(8);
        int i6 = 2;
        int i7 = i - 2;
        int[] generateDefault2BitClutEntries = generateDefault2BitClutEntries();
        int[] generateDefault4BitClutEntries = generateDefault4BitClutEntries();
        int[] generateDefault8BitClutEntries = generateDefault8BitClutEntries();
        while (i7 > 0) {
            int readBits4 = parsableBitArray.readBits(i5);
            int readBits5 = parsableBitArray.readBits(i5);
            int[] iArr = (readBits5 & 128) != 0 ? generateDefault2BitClutEntries : (readBits5 & 64) != 0 ? generateDefault4BitClutEntries : generateDefault8BitClutEntries;
            if ((readBits5 & 1) != 0) {
                i3 = parsableBitArray.readBits(i5);
                i4 = parsableBitArray.readBits(i5);
                readBits = parsableBitArray.readBits(i5);
                readBits2 = parsableBitArray.readBits(i5);
                i2 = i7 - 6;
            } else {
                int readBits6 = parsableBitArray.readBits(6) << i6;
                int readBits7 = parsableBitArray.readBits(4) << 4;
                readBits = parsableBitArray.readBits(4) << 4;
                i2 = i7 - 4;
                readBits2 = parsableBitArray.readBits(i6) << 6;
                i3 = readBits6;
                i4 = readBits7;
            }
            if (i3 == 0) {
                i4 = 0;
                readBits = 0;
                readBits2 = NotificationCenter.notificationsCountUpdated;
            }
            byte b = (byte) (255 - (readBits2 & NotificationCenter.notificationsCountUpdated));
            double d = i3;
            double d2 = i4 - 128;
            Double.isNaN(d2);
            Double.isNaN(d);
            double d3 = readBits - 128;
            Double.isNaN(d3);
            Double.isNaN(d);
            Double.isNaN(d2);
            Double.isNaN(d3);
            Double.isNaN(d);
            iArr[readBits4] = getColor(b, Util.constrainValue((int) (d + (1.402d * d2)), 0, NotificationCenter.notificationsCountUpdated), Util.constrainValue((int) ((d - (0.34414d * d3)) - (d2 * 0.71414d)), 0, NotificationCenter.notificationsCountUpdated), Util.constrainValue((int) (d + (d3 * 1.772d)), 0, NotificationCenter.notificationsCountUpdated));
            i7 = i2;
            readBits3 = readBits3;
            i5 = 8;
            i6 = 2;
        }
        return new ClutDefinition(readBits3, generateDefault2BitClutEntries, generateDefault4BitClutEntries, generateDefault8BitClutEntries);
    }

    private static DisplayDefinition parseDisplayDefinition(ParsableBitArray parsableBitArray) {
        int i;
        int i2;
        int i3;
        int i4;
        parsableBitArray.skipBits(4);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray.skipBits(3);
        int readBits = parsableBitArray.readBits(16);
        int readBits2 = parsableBitArray.readBits(16);
        if (readBit) {
            int readBits3 = parsableBitArray.readBits(16);
            int readBits4 = parsableBitArray.readBits(16);
            int readBits5 = parsableBitArray.readBits(16);
            i2 = parsableBitArray.readBits(16);
            i = readBits4;
            i4 = readBits5;
            i3 = readBits3;
        } else {
            i = readBits;
            i2 = readBits2;
            i3 = 0;
            i4 = 0;
        }
        return new DisplayDefinition(readBits, readBits2, i3, i, i4, i2);
    }

    private static ObjectData parseObjectData(ParsableBitArray parsableBitArray) {
        byte[] bArr;
        int readBits = parsableBitArray.readBits(16);
        parsableBitArray.skipBits(4);
        int readBits2 = parsableBitArray.readBits(2);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray.skipBits(1);
        byte[] bArr2 = Util.EMPTY_BYTE_ARRAY;
        if (readBits2 == 1) {
            parsableBitArray.skipBits(parsableBitArray.readBits(8) * 16);
        } else if (readBits2 == 0) {
            int readBits3 = parsableBitArray.readBits(16);
            int readBits4 = parsableBitArray.readBits(16);
            if (readBits3 > 0) {
                bArr2 = new byte[readBits3];
                parsableBitArray.readBytes(bArr2, 0, readBits3);
            }
            if (readBits4 > 0) {
                bArr = new byte[readBits4];
                parsableBitArray.readBytes(bArr, 0, readBits4);
                return new ObjectData(readBits, readBit, bArr2, bArr);
            }
        }
        bArr = bArr2;
        return new ObjectData(readBits, readBit, bArr2, bArr);
    }

    private static PageComposition parsePageComposition(ParsableBitArray parsableBitArray, int i) {
        int readBits = parsableBitArray.readBits(8);
        int readBits2 = parsableBitArray.readBits(4);
        int readBits3 = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(2);
        int i2 = i - 2;
        SparseArray sparseArray = new SparseArray();
        while (i2 > 0) {
            int readBits4 = parsableBitArray.readBits(8);
            parsableBitArray.skipBits(8);
            i2 -= 6;
            sparseArray.put(readBits4, new PageRegion(parsableBitArray.readBits(16), parsableBitArray.readBits(16)));
        }
        return new PageComposition(readBits, readBits2, readBits3, sparseArray);
    }

    private static RegionComposition parseRegionComposition(ParsableBitArray parsableBitArray, int i) {
        int i2;
        int i3;
        int readBits = parsableBitArray.readBits(8);
        parsableBitArray.skipBits(4);
        boolean readBit = parsableBitArray.readBit();
        parsableBitArray.skipBits(3);
        int i4 = 16;
        int readBits2 = parsableBitArray.readBits(16);
        int readBits3 = parsableBitArray.readBits(16);
        int readBits4 = parsableBitArray.readBits(3);
        int readBits5 = parsableBitArray.readBits(3);
        int i5 = 2;
        parsableBitArray.skipBits(2);
        int readBits6 = parsableBitArray.readBits(8);
        int readBits7 = parsableBitArray.readBits(8);
        int readBits8 = parsableBitArray.readBits(4);
        int readBits9 = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(2);
        int i6 = i - 10;
        SparseArray sparseArray = new SparseArray();
        while (i6 > 0) {
            int readBits10 = parsableBitArray.readBits(i4);
            int readBits11 = parsableBitArray.readBits(i5);
            int readBits12 = parsableBitArray.readBits(i5);
            int readBits13 = parsableBitArray.readBits(12);
            int i7 = readBits9;
            parsableBitArray.skipBits(4);
            int readBits14 = parsableBitArray.readBits(12);
            int i8 = i6 - 6;
            if (readBits11 != 1 && readBits11 != 2) {
                i6 = i8;
                i3 = 0;
                i2 = 0;
                sparseArray.put(readBits10, new RegionObject(readBits11, readBits12, readBits13, readBits14, i3, i2));
                readBits9 = i7;
                i5 = 2;
                i4 = 16;
            }
            i6 -= 8;
            i3 = parsableBitArray.readBits(8);
            i2 = parsableBitArray.readBits(8);
            sparseArray.put(readBits10, new RegionObject(readBits11, readBits12, readBits13, readBits14, i3, i2));
            readBits9 = i7;
            i5 = 2;
            i4 = 16;
        }
        return new RegionComposition(readBits, readBit, readBits2, readBits3, readBits4, readBits5, readBits6, readBits7, readBits8, readBits9, sparseArray);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void parseSubtitlingSegment(ParsableBitArray parsableBitArray, SubtitleService subtitleService) {
        RegionComposition regionComposition;
        SparseArray sparseArray;
        ClutDefinition clutDefinition;
        int i;
        ClutDefinition clutDefinition2;
        ObjectData objectData;
        int readBits = parsableBitArray.readBits(8);
        int readBits2 = parsableBitArray.readBits(16);
        int readBits3 = parsableBitArray.readBits(16);
        int bytePosition = parsableBitArray.getBytePosition() + readBits3;
        if (readBits3 * 8 > parsableBitArray.bitsLeft()) {
            Log.w("DvbParser", "Data field length exceeds limit");
            parsableBitArray.skipBits(parsableBitArray.bitsLeft());
            return;
        }
        switch (readBits) {
            case 16:
                if (readBits2 == subtitleService.subtitlePageId) {
                    PageComposition pageComposition = subtitleService.pageComposition;
                    PageComposition parsePageComposition = parsePageComposition(parsableBitArray, readBits3);
                    if (parsePageComposition.state == 0) {
                        if (pageComposition != null && pageComposition.version != parsePageComposition.version) {
                            subtitleService.pageComposition = parsePageComposition;
                            break;
                        }
                    } else {
                        subtitleService.pageComposition = parsePageComposition;
                        subtitleService.regions.clear();
                        subtitleService.cluts.clear();
                        subtitleService.objects.clear();
                        break;
                    }
                }
                break;
            case 17:
                PageComposition pageComposition2 = subtitleService.pageComposition;
                if (readBits2 == subtitleService.subtitlePageId && pageComposition2 != null) {
                    RegionComposition parseRegionComposition = parseRegionComposition(parsableBitArray, readBits3);
                    if (pageComposition2.state == 0 && (regionComposition = (RegionComposition) subtitleService.regions.get(parseRegionComposition.id)) != null) {
                        parseRegionComposition.mergeFrom(regionComposition);
                    }
                    subtitleService.regions.put(parseRegionComposition.id, parseRegionComposition);
                    break;
                }
                break;
            case 18:
                if (readBits2 == subtitleService.subtitlePageId) {
                    ClutDefinition parseClutDefinition = parseClutDefinition(parsableBitArray, readBits3);
                    sparseArray = subtitleService.cluts;
                    clutDefinition = parseClutDefinition;
                } else if (readBits2 == subtitleService.ancillaryPageId) {
                    ClutDefinition parseClutDefinition2 = parseClutDefinition(parsableBitArray, readBits3);
                    sparseArray = subtitleService.ancillaryCluts;
                    clutDefinition = parseClutDefinition2;
                }
                i = clutDefinition.id;
                clutDefinition2 = clutDefinition;
                sparseArray.put(i, clutDefinition2);
                break;
            case 19:
                if (readBits2 == subtitleService.subtitlePageId) {
                    ObjectData parseObjectData = parseObjectData(parsableBitArray);
                    sparseArray = subtitleService.objects;
                    objectData = parseObjectData;
                } else if (readBits2 == subtitleService.ancillaryPageId) {
                    ObjectData parseObjectData2 = parseObjectData(parsableBitArray);
                    sparseArray = subtitleService.ancillaryObjects;
                    objectData = parseObjectData2;
                }
                i = objectData.id;
                clutDefinition2 = objectData;
                sparseArray.put(i, clutDefinition2);
                break;
            case 20:
                if (readBits2 == subtitleService.subtitlePageId) {
                    subtitleService.displayDefinition = parseDisplayDefinition(parsableBitArray);
                    break;
                }
                break;
        }
        parsableBitArray.skipBytes(bytePosition - parsableBitArray.getBytePosition());
    }

    public List decode(byte[] bArr, int i) {
        int i2;
        SparseArray sparseArray;
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr, i);
        while (parsableBitArray.bitsLeft() >= 48 && parsableBitArray.readBits(8) == 15) {
            parseSubtitlingSegment(parsableBitArray, this.subtitleService);
        }
        SubtitleService subtitleService = this.subtitleService;
        PageComposition pageComposition = subtitleService.pageComposition;
        if (pageComposition == null) {
            return Collections.emptyList();
        }
        DisplayDefinition displayDefinition = subtitleService.displayDefinition;
        if (displayDefinition == null) {
            displayDefinition = this.defaultDisplayDefinition;
        }
        Bitmap bitmap = this.bitmap;
        if (bitmap == null || displayDefinition.width + 1 != bitmap.getWidth() || displayDefinition.height + 1 != this.bitmap.getHeight()) {
            Bitmap createBitmap = Bitmap.createBitmap(displayDefinition.width + 1, displayDefinition.height + 1, Bitmap.Config.ARGB_8888);
            this.bitmap = createBitmap;
            this.canvas.setBitmap(createBitmap);
        }
        ArrayList arrayList = new ArrayList();
        SparseArray sparseArray2 = pageComposition.regions;
        for (int i3 = 0; i3 < sparseArray2.size(); i3++) {
            this.canvas.save();
            PageRegion pageRegion = (PageRegion) sparseArray2.valueAt(i3);
            RegionComposition regionComposition = (RegionComposition) this.subtitleService.regions.get(sparseArray2.keyAt(i3));
            int i4 = pageRegion.horizontalAddress + displayDefinition.horizontalPositionMinimum;
            int i5 = pageRegion.verticalAddress + displayDefinition.verticalPositionMinimum;
            this.canvas.clipRect(i4, i5, Math.min(regionComposition.width + i4, displayDefinition.horizontalPositionMaximum), Math.min(regionComposition.height + i5, displayDefinition.verticalPositionMaximum));
            ClutDefinition clutDefinition = (ClutDefinition) this.subtitleService.cluts.get(regionComposition.clutId);
            if (clutDefinition == null && (clutDefinition = (ClutDefinition) this.subtitleService.ancillaryCluts.get(regionComposition.clutId)) == null) {
                clutDefinition = this.defaultClutDefinition;
            }
            SparseArray sparseArray3 = regionComposition.regionObjects;
            int i6 = 0;
            while (i6 < sparseArray3.size()) {
                int keyAt = sparseArray3.keyAt(i6);
                RegionObject regionObject = (RegionObject) sparseArray3.valueAt(i6);
                ObjectData objectData = (ObjectData) this.subtitleService.objects.get(keyAt);
                ObjectData objectData2 = objectData == null ? (ObjectData) this.subtitleService.ancillaryObjects.get(keyAt) : objectData;
                if (objectData2 != null) {
                    i2 = i6;
                    sparseArray = sparseArray3;
                    paintPixelDataSubBlocks(objectData2, clutDefinition, regionComposition.depth, regionObject.horizontalPosition + i4, i5 + regionObject.verticalPosition, objectData2.nonModifyingColorFlag ? null : this.defaultPaint, this.canvas);
                } else {
                    i2 = i6;
                    sparseArray = sparseArray3;
                }
                i6 = i2 + 1;
                sparseArray3 = sparseArray;
            }
            if (regionComposition.fillFlag) {
                int i7 = regionComposition.depth;
                this.fillRegionPaint.setColor(i7 == 3 ? clutDefinition.clutEntries8Bit[regionComposition.pixelCode8Bit] : i7 == 2 ? clutDefinition.clutEntries4Bit[regionComposition.pixelCode4Bit] : clutDefinition.clutEntries2Bit[regionComposition.pixelCode2Bit]);
                this.canvas.drawRect(i4, i5, regionComposition.width + i4, regionComposition.height + i5, this.fillRegionPaint);
            }
            arrayList.add(new Cue.Builder().setBitmap(Bitmap.createBitmap(this.bitmap, i4, i5, regionComposition.width, regionComposition.height)).setPosition(i4 / displayDefinition.width).setPositionAnchor(0).setLine(i5 / displayDefinition.height, 0).setLineAnchor(0).setSize(regionComposition.width / displayDefinition.width).setBitmapHeight(regionComposition.height / displayDefinition.height).build());
            this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            this.canvas.restore();
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void reset() {
        this.subtitleService.reset();
    }
}
