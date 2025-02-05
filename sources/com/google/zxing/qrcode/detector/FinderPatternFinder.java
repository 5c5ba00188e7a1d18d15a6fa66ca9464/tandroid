package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class FinderPatternFinder {
    private static final EstimatedModuleComparator moduleComparator = new EstimatedModuleComparator();
    private boolean hasSkipped;
    private final BitMatrix image;
    private final List possibleCenters = new ArrayList();
    private final int[] crossCheckStateCount = new int[5];

    private static final class EstimatedModuleComparator implements Comparator, Serializable {
        private EstimatedModuleComparator() {
        }

        @Override // java.util.Comparator
        public int compare(FinderPattern finderPattern, FinderPattern finderPattern2) {
            return Float.compare(finderPattern.getEstimatedModuleSize(), finderPattern2.getEstimatedModuleSize());
        }
    }

    public FinderPatternFinder(BitMatrix bitMatrix, ResultPointCallback resultPointCallback) {
        this.image = bitMatrix;
    }

    private static float centerFromEnd(int[] iArr, int i) {
        return ((i - iArr[4]) - iArr[3]) - (iArr[2] / 2.0f);
    }

    private boolean crossCheckDiagonal(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int[] crossCheckStateCount = getCrossCheckStateCount();
        int i6 = 0;
        while (i >= i6 && i2 >= i6 && this.image.get(i2 - i6, i - i6)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i6++;
        }
        if (crossCheckStateCount[2] == 0) {
            return false;
        }
        while (i >= i6 && i2 >= i6 && !this.image.get(i2 - i6, i - i6)) {
            crossCheckStateCount[1] = crossCheckStateCount[1] + 1;
            i6++;
        }
        if (crossCheckStateCount[1] == 0) {
            return false;
        }
        while (i >= i6 && i2 >= i6 && this.image.get(i2 - i6, i - i6)) {
            crossCheckStateCount[0] = crossCheckStateCount[0] + 1;
            i6++;
        }
        if (crossCheckStateCount[0] == 0) {
            return false;
        }
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i7 = 1;
        while (true) {
            int i8 = i + i7;
            if (i8 >= height || (i5 = i2 + i7) >= width || !this.image.get(i5, i8)) {
                break;
            }
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i7++;
        }
        while (true) {
            int i9 = i + i7;
            if (i9 >= height || (i4 = i2 + i7) >= width || this.image.get(i4, i9)) {
                break;
            }
            crossCheckStateCount[3] = crossCheckStateCount[3] + 1;
            i7++;
        }
        if (crossCheckStateCount[3] == 0) {
            return false;
        }
        while (true) {
            int i10 = i + i7;
            if (i10 >= height || (i3 = i2 + i7) >= width || !this.image.get(i3, i10)) {
                break;
            }
            crossCheckStateCount[4] = crossCheckStateCount[4] + 1;
            i7++;
        }
        if (crossCheckStateCount[4] == 0) {
            return false;
        }
        return foundPatternDiagonal(crossCheckStateCount);
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0082, code lost:
    
        if (r2[3] < r13) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0086, code lost:
    
        if (r11 >= r1) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008c, code lost:
    
        if (r0.get(r11, r12) == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x008e, code lost:
    
        r9 = r2[4];
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0090, code lost:
    
        if (r9 >= r13) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0092, code lost:
    
        r2[4] = r9 + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0099, code lost:
    
        r12 = r2[4];
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009b, code lost:
    
        if (r12 < r13) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x009d, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b1, code lost:
    
        if ((java.lang.Math.abs(((((r2[0] + r2[1]) + r2[2]) + r2[3]) + r12) - r14) * 5) < r14) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b3, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00b8, code lost:
    
        if (foundPatternCross(r2) == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00be, code lost:
    
        return centerFromEnd(r2, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
    
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float crossCheckHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        BitMatrix bitMatrix = this.image;
        int width = bitMatrix.getWidth();
        int[] crossCheckStateCount = getCrossCheckStateCount();
        int i7 = i;
        while (i7 >= 0 && bitMatrix.get(i7, i2)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i7--;
        }
        if (i7 < 0) {
            return Float.NaN;
        }
        while (i7 >= 0 && !bitMatrix.get(i7, i2)) {
            int i8 = crossCheckStateCount[1];
            if (i8 > i3) {
                break;
            }
            crossCheckStateCount[1] = i8 + 1;
            i7--;
        }
        if (i7 < 0 || crossCheckStateCount[1] > i3) {
            return Float.NaN;
        }
        while (i7 >= 0 && bitMatrix.get(i7, i2) && (i6 = crossCheckStateCount[0]) <= i3) {
            crossCheckStateCount[0] = i6 + 1;
            i7--;
        }
        if (crossCheckStateCount[0] > i3) {
            return Float.NaN;
        }
        int i9 = i + 1;
        while (i9 < width && bitMatrix.get(i9, i2)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i9++;
        }
        if (i9 == width) {
            return Float.NaN;
        }
        while (i9 < width && !bitMatrix.get(i9, i2) && (i5 = crossCheckStateCount[3]) < i3) {
            crossCheckStateCount[3] = i5 + 1;
            i9++;
        }
        return Float.NaN;
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0082, code lost:
    
        if (r2[3] < r13) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0086, code lost:
    
        if (r11 >= r1) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008c, code lost:
    
        if (r0.get(r12, r11) == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x008e, code lost:
    
        r9 = r2[4];
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0090, code lost:
    
        if (r9 >= r13) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0092, code lost:
    
        r2[4] = r9 + 1;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0099, code lost:
    
        r12 = r2[4];
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009b, code lost:
    
        if (r12 < r13) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x009d, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b3, code lost:
    
        if ((java.lang.Math.abs(((((r2[0] + r2[1]) + r2[2]) + r2[3]) + r12) - r14) * 5) < (r14 * 2)) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b5, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00ba, code lost:
    
        if (foundPatternCross(r2) == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c0, code lost:
    
        return centerFromEnd(r2, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
    
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float crossCheckVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        BitMatrix bitMatrix = this.image;
        int height = bitMatrix.getHeight();
        int[] crossCheckStateCount = getCrossCheckStateCount();
        int i7 = i;
        while (i7 >= 0 && bitMatrix.get(i2, i7)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i7--;
        }
        if (i7 < 0) {
            return Float.NaN;
        }
        while (i7 >= 0 && !bitMatrix.get(i2, i7)) {
            int i8 = crossCheckStateCount[1];
            if (i8 > i3) {
                break;
            }
            crossCheckStateCount[1] = i8 + 1;
            i7--;
        }
        if (i7 < 0 || crossCheckStateCount[1] > i3) {
            return Float.NaN;
        }
        while (i7 >= 0 && bitMatrix.get(i2, i7) && (i6 = crossCheckStateCount[0]) <= i3) {
            crossCheckStateCount[0] = i6 + 1;
            i7--;
        }
        if (crossCheckStateCount[0] > i3) {
            return Float.NaN;
        }
        int i9 = i + 1;
        while (i9 < height && bitMatrix.get(i2, i9)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i9++;
        }
        if (i9 == height) {
            return Float.NaN;
        }
        while (i9 < height && !bitMatrix.get(i2, i9) && (i5 = crossCheckStateCount[3]) < i3) {
            crossCheckStateCount[3] = i5 + 1;
            i9++;
        }
        return Float.NaN;
    }

    private int findRowSkip() {
        if (this.possibleCenters.size() <= 1) {
            return 0;
        }
        FinderPattern finderPattern = null;
        for (FinderPattern finderPattern2 : this.possibleCenters) {
            if (finderPattern2.getCount() >= 2) {
                if (finderPattern != null) {
                    this.hasSkipped = true;
                    return ((int) (Math.abs(finderPattern.getX() - finderPattern2.getX()) - Math.abs(finderPattern.getY() - finderPattern2.getY()))) / 2;
                }
                finderPattern = finderPattern2;
            }
        }
        return 0;
    }

    protected static boolean foundPatternCross(int[] iArr) {
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            int i3 = iArr[i2];
            if (i3 == 0) {
                return false;
            }
            i += i3;
        }
        if (i < 7) {
            return false;
        }
        float f = i / 7.0f;
        float f2 = f / 2.0f;
        return Math.abs(f - ((float) iArr[0])) < f2 && Math.abs(f - ((float) iArr[1])) < f2 && Math.abs((f * 3.0f) - ((float) iArr[2])) < 3.0f * f2 && Math.abs(f - ((float) iArr[3])) < f2 && Math.abs(f - ((float) iArr[4])) < f2;
    }

    protected static boolean foundPatternDiagonal(int[] iArr) {
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            int i3 = iArr[i2];
            if (i3 == 0) {
                return false;
            }
            i += i3;
        }
        if (i < 7) {
            return false;
        }
        float f = i / 7.0f;
        float f2 = f / 1.333f;
        return Math.abs(f - ((float) iArr[0])) < f2 && Math.abs(f - ((float) iArr[1])) < f2 && Math.abs((f * 3.0f) - ((float) iArr[2])) < 3.0f * f2 && Math.abs(f - ((float) iArr[3])) < f2 && Math.abs(f - ((float) iArr[4])) < f2;
    }

    private int[] getCrossCheckStateCount() {
        clearCounts(this.crossCheckStateCount);
        return this.crossCheckStateCount;
    }

    private boolean haveMultiplyConfirmedCenters() {
        int size = this.possibleCenters.size();
        float f = 0.0f;
        int i = 0;
        float f2 = 0.0f;
        for (FinderPattern finderPattern : this.possibleCenters) {
            if (finderPattern.getCount() >= 2) {
                i++;
                f2 += finderPattern.getEstimatedModuleSize();
            }
        }
        if (i < 3) {
            return false;
        }
        float f3 = f2 / size;
        Iterator it = this.possibleCenters.iterator();
        while (it.hasNext()) {
            f += Math.abs(((FinderPattern) it.next()).getEstimatedModuleSize() - f3);
        }
        return f <= f2 * 0.05f;
    }

    private FinderPattern[] selectBestPatterns() {
        int i = 2;
        if (this.possibleCenters.size() < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        Collections.sort(this.possibleCenters, moduleComparator);
        FinderPattern[] finderPatternArr = new FinderPattern[3];
        int i2 = 0;
        double d = Double.MAX_VALUE;
        while (i2 < this.possibleCenters.size() - i) {
            FinderPattern finderPattern = (FinderPattern) this.possibleCenters.get(i2);
            float estimatedModuleSize = finderPattern.getEstimatedModuleSize();
            i2++;
            int i3 = i2;
            while (i3 < this.possibleCenters.size() - 1) {
                FinderPattern finderPattern2 = (FinderPattern) this.possibleCenters.get(i3);
                double squaredDistance = squaredDistance(finderPattern, finderPattern2);
                i3++;
                int i4 = i3;
                while (i4 < this.possibleCenters.size()) {
                    FinderPattern finderPattern3 = (FinderPattern) this.possibleCenters.get(i4);
                    if (finderPattern3.getEstimatedModuleSize() <= 1.4f * estimatedModuleSize) {
                        double[] dArr = {squaredDistance, squaredDistance(finderPattern2, finderPattern3), squaredDistance(finderPattern, finderPattern3)};
                        Arrays.sort(dArr);
                        double abs = Math.abs(dArr[2] - (dArr[1] * 2.0d)) + Math.abs(dArr[2] - (dArr[0] * 2.0d));
                        if (abs < d) {
                            finderPatternArr[0] = finderPattern;
                            finderPatternArr[1] = finderPattern2;
                            finderPatternArr[2] = finderPattern3;
                            d = abs;
                        }
                    }
                    i4++;
                    i = 2;
                }
            }
        }
        if (d != Double.MAX_VALUE) {
            return finderPatternArr;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static double squaredDistance(FinderPattern finderPattern, FinderPattern finderPattern2) {
        double x = finderPattern.getX() - finderPattern2.getX();
        double y = finderPattern.getY() - finderPattern2.getY();
        Double.isNaN(x);
        Double.isNaN(x);
        Double.isNaN(y);
        Double.isNaN(y);
        return (x * x) + (y * y);
    }

    protected final void clearCounts(int[] iArr) {
        Arrays.fill(iArr, 0);
    }

    final FinderPatternInfo find(Map map) {
        boolean z = map != null && map.containsKey(DecodeHintType.TRY_HARDER);
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = (height * 3) / 388;
        if (i < 3 || z) {
            i = 3;
        }
        int[] iArr = new int[5];
        int i2 = i - 1;
        boolean z2 = false;
        while (i2 < height && !z2) {
            clearCounts(iArr);
            int i3 = 0;
            int i4 = 0;
            while (i3 < width) {
                if (this.image.get(i3, i2)) {
                    if ((i4 & 1) == 1) {
                        i4++;
                    }
                    iArr[i4] = iArr[i4] + 1;
                } else if ((i4 & 1) != 0) {
                    iArr[i4] = iArr[i4] + 1;
                } else if (i4 != 4) {
                    i4++;
                    iArr[i4] = iArr[i4] + 1;
                } else if (foundPatternCross(iArr) && handlePossibleCenter(iArr, i2, i3)) {
                    if (this.hasSkipped) {
                        z2 = haveMultiplyConfirmedCenters();
                    } else {
                        int findRowSkip = findRowSkip();
                        int i5 = iArr[2];
                        if (findRowSkip > i5) {
                            i2 += (findRowSkip - i5) - 2;
                            i3 = width - 1;
                        }
                    }
                    clearCounts(iArr);
                    i = 2;
                    i4 = 0;
                } else {
                    shiftCounts2(iArr);
                    i4 = 3;
                }
                i3++;
            }
            if (foundPatternCross(iArr) && handlePossibleCenter(iArr, i2, width)) {
                i = iArr[0];
                if (this.hasSkipped) {
                    z2 = haveMultiplyConfirmedCenters();
                }
            }
            i2 += i;
        }
        FinderPattern[] selectBestPatterns = selectBestPatterns();
        ResultPoint.orderBestPatterns(selectBestPatterns);
        return new FinderPatternInfo(selectBestPatterns);
    }

    protected final boolean handlePossibleCenter(int[] iArr, int i, int i2) {
        int i3 = 0;
        int i4 = iArr[0] + iArr[1] + iArr[2] + iArr[3] + iArr[4];
        int centerFromEnd = (int) centerFromEnd(iArr, i2);
        float crossCheckVertical = crossCheckVertical(i, centerFromEnd, iArr[2], i4);
        if (!Float.isNaN(crossCheckVertical)) {
            int i5 = (int) crossCheckVertical;
            float crossCheckHorizontal = crossCheckHorizontal(centerFromEnd, i5, iArr[2], i4);
            if (!Float.isNaN(crossCheckHorizontal) && crossCheckDiagonal(i5, (int) crossCheckHorizontal)) {
                float f = i4 / 7.0f;
                while (true) {
                    if (i3 >= this.possibleCenters.size()) {
                        this.possibleCenters.add(new FinderPattern(crossCheckHorizontal, crossCheckVertical, f));
                        break;
                    }
                    FinderPattern finderPattern = (FinderPattern) this.possibleCenters.get(i3);
                    if (finderPattern.aboutEquals(f, crossCheckVertical, crossCheckHorizontal)) {
                        this.possibleCenters.set(i3, finderPattern.combineEstimate(crossCheckVertical, crossCheckHorizontal, f));
                        break;
                    }
                    i3++;
                }
                return true;
            }
        }
        return false;
    }

    protected final void shiftCounts2(int[] iArr) {
        iArr[0] = iArr[2];
        iArr[1] = iArr[3];
        iArr[2] = iArr[4];
        iArr[3] = 1;
        iArr[4] = 0;
    }
}
