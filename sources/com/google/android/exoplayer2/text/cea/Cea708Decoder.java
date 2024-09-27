package com.google.android.exoplayer2.text.cea;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.text.cea.Cea708Decoder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public final class Cea708Decoder extends CeaDecoder {
    private final CueInfoBuilder[] cueInfoBuilders;
    private List cues;
    private CueInfoBuilder currentCueInfoBuilder;
    private DtvCcPacket currentDtvCcPacket;
    private int currentWindow;
    private final boolean isWideAspectRatio;
    private List lastCues;
    private final int selectedServiceNumber;
    private final ParsableByteArray ccData = new ParsableByteArray();
    private final ParsableBitArray captionChannelPacketData = new ParsableBitArray();
    private int previousSequenceNumber = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Cea708CueInfo {
        private static final Comparator LEAST_IMPORTANT_FIRST = new Comparator() { // from class: com.google.android.exoplayer2.text.cea.Cea708Decoder$Cea708CueInfo$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$static$0;
                lambda$static$0 = Cea708Decoder.Cea708CueInfo.lambda$static$0((Cea708Decoder.Cea708CueInfo) obj, (Cea708Decoder.Cea708CueInfo) obj2);
                return lambda$static$0;
            }
        };
        public final Cue cue;
        public final int priority;

        public Cea708CueInfo(CharSequence charSequence, Layout.Alignment alignment, float f, int i, int i2, float f2, int i3, float f3, boolean z, int i4, int i5) {
            Cue.Builder size = new Cue.Builder().setText(charSequence).setTextAlignment(alignment).setLine(f, i).setLineAnchor(i2).setPosition(f2).setPositionAnchor(i3).setSize(f3);
            if (z) {
                size.setWindowColor(i4);
            }
            this.cue = size.build();
            this.priority = i5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$static$0(Cea708CueInfo cea708CueInfo, Cea708CueInfo cea708CueInfo2) {
            return Integer.compare(cea708CueInfo2.priority, cea708CueInfo.priority);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CueInfoBuilder {
        public static final int COLOR_SOLID_BLACK;
        public static final int COLOR_SOLID_WHITE = getArgbColorFromCeaColor(2, 2, 2, 0);
        public static final int COLOR_TRANSPARENT;
        private static final int[] PEN_STYLE_BACKGROUND;
        private static final int[] PEN_STYLE_EDGE_TYPE;
        private static final int[] PEN_STYLE_FONT_STYLE;
        private static final int[] WINDOW_STYLE_FILL;
        private static final int[] WINDOW_STYLE_JUSTIFICATION;
        private static final int[] WINDOW_STYLE_PRINT_DIRECTION;
        private static final int[] WINDOW_STYLE_SCROLL_DIRECTION;
        private static final boolean[] WINDOW_STYLE_WORD_WRAP;
        private int anchorId;
        private int backgroundColor;
        private int backgroundColorStartPosition;
        private boolean defined;
        private int foregroundColor;
        private int foregroundColorStartPosition;
        private int horizontalAnchor;
        private int italicsStartPosition;
        private int justification;
        private int penStyleId;
        private int priority;
        private boolean relativePositioning;
        private int row;
        private int rowCount;
        private boolean rowLock;
        private int underlineStartPosition;
        private int verticalAnchor;
        private boolean visible;
        private int windowFillColor;
        private int windowStyleId;
        private final List rolledUpCaptions = new ArrayList();
        private final SpannableStringBuilder captionStringBuilder = new SpannableStringBuilder();

        static {
            int argbColorFromCeaColor = getArgbColorFromCeaColor(0, 0, 0, 0);
            COLOR_SOLID_BLACK = argbColorFromCeaColor;
            int argbColorFromCeaColor2 = getArgbColorFromCeaColor(0, 0, 0, 3);
            COLOR_TRANSPARENT = argbColorFromCeaColor2;
            WINDOW_STYLE_JUSTIFICATION = new int[]{0, 0, 0, 0, 0, 2, 0};
            WINDOW_STYLE_PRINT_DIRECTION = new int[]{0, 0, 0, 0, 0, 0, 2};
            WINDOW_STYLE_SCROLL_DIRECTION = new int[]{3, 3, 3, 3, 3, 3, 1};
            WINDOW_STYLE_WORD_WRAP = new boolean[]{false, false, false, true, true, true, false};
            WINDOW_STYLE_FILL = new int[]{argbColorFromCeaColor, argbColorFromCeaColor2, argbColorFromCeaColor, argbColorFromCeaColor, argbColorFromCeaColor2, argbColorFromCeaColor, argbColorFromCeaColor};
            PEN_STYLE_FONT_STYLE = new int[]{0, 1, 2, 3, 4, 3, 4};
            PEN_STYLE_EDGE_TYPE = new int[]{0, 0, 0, 0, 0, 3, 3};
            PEN_STYLE_BACKGROUND = new int[]{argbColorFromCeaColor, argbColorFromCeaColor, argbColorFromCeaColor, argbColorFromCeaColor, argbColorFromCeaColor, argbColorFromCeaColor2, argbColorFromCeaColor2};
        }

        public CueInfoBuilder() {
            reset();
        }

        public static int getArgbColorFromCeaColor(int i, int i2, int i3) {
            return getArgbColorFromCeaColor(i, i2, i3, 0);
        }

        public static int getArgbColorFromCeaColor(int i, int i2, int i3, int i4) {
            Assertions.checkIndex(i, 0, 4);
            Assertions.checkIndex(i2, 0, 4);
            Assertions.checkIndex(i3, 0, 4);
            Assertions.checkIndex(i4, 0, 4);
            return Color.argb(i4 != 2 ? i4 != 3 ? NotificationCenter.messagePlayingSpeedChanged : 0 : NotificationCenter.dialogTranslate, i > 1 ? NotificationCenter.messagePlayingSpeedChanged : 0, i2 > 1 ? NotificationCenter.messagePlayingSpeedChanged : 0, i3 > 1 ? NotificationCenter.messagePlayingSpeedChanged : 0);
        }

        public void append(char c) {
            if (c != '\n') {
                this.captionStringBuilder.append(c);
                return;
            }
            this.rolledUpCaptions.add(buildSpannableString());
            this.captionStringBuilder.clear();
            if (this.italicsStartPosition != -1) {
                this.italicsStartPosition = 0;
            }
            if (this.underlineStartPosition != -1) {
                this.underlineStartPosition = 0;
            }
            if (this.foregroundColorStartPosition != -1) {
                this.foregroundColorStartPosition = 0;
            }
            if (this.backgroundColorStartPosition != -1) {
                this.backgroundColorStartPosition = 0;
            }
            while (true) {
                if ((!this.rowLock || this.rolledUpCaptions.size() < this.rowCount) && this.rolledUpCaptions.size() < 15) {
                    return;
                }
                this.rolledUpCaptions.remove(0);
            }
        }

        public void backspace() {
            int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0071  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0092  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0094  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00a9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Cea708CueInfo build() {
            Layout.Alignment alignment;
            float f;
            float f2;
            if (isEmpty()) {
                return null;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i = 0; i < this.rolledUpCaptions.size(); i++) {
                spannableStringBuilder.append((CharSequence) this.rolledUpCaptions.get(i));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append((CharSequence) buildSpannableString());
            int i2 = this.justification;
            if (i2 != 0) {
                if (i2 == 1) {
                    alignment = Layout.Alignment.ALIGN_OPPOSITE;
                } else if (i2 == 2) {
                    alignment = Layout.Alignment.ALIGN_CENTER;
                } else if (i2 != 3) {
                    throw new IllegalArgumentException("Unexpected justification value: " + this.justification);
                }
                Layout.Alignment alignment2 = alignment;
                if (this.relativePositioning) {
                    f = this.horizontalAnchor / 209.0f;
                    f2 = this.verticalAnchor / 74.0f;
                } else {
                    f = this.horizontalAnchor / 99.0f;
                    f2 = this.verticalAnchor / 99.0f;
                }
                float f3 = (f * 0.9f) + 0.05f;
                float f4 = (f2 * 0.9f) + 0.05f;
                int i3 = this.anchorId;
                int i4 = i3 / 3;
                int i5 = i3 % 3;
                return new Cea708CueInfo(spannableStringBuilder, alignment2, f4, 0, i4 != 0 ? 0 : i4 == 1 ? 1 : 2, f3, i5 != 0 ? 0 : i5 == 1 ? 1 : 2, -3.4028235E38f, this.windowFillColor != COLOR_SOLID_BLACK, this.windowFillColor, this.priority);
            }
            alignment = Layout.Alignment.ALIGN_NORMAL;
            Layout.Alignment alignment22 = alignment;
            if (this.relativePositioning) {
            }
            float f32 = (f * 0.9f) + 0.05f;
            float f42 = (f2 * 0.9f) + 0.05f;
            int i32 = this.anchorId;
            int i42 = i32 / 3;
            int i52 = i32 % 3;
            return new Cea708CueInfo(spannableStringBuilder, alignment22, f42, 0, i42 != 0 ? 0 : i42 == 1 ? 1 : 2, f32, i52 != 0 ? 0 : i52 == 1 ? 1 : 2, -3.4028235E38f, this.windowFillColor != COLOR_SOLID_BLACK, this.windowFillColor, this.priority);
        }

        public SpannableString buildSpannableString() {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.captionStringBuilder);
            int length = spannableStringBuilder.length();
            if (length > 0) {
                if (this.italicsStartPosition != -1) {
                    spannableStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, length, 33);
                }
                if (this.underlineStartPosition != -1) {
                    spannableStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, length, 33);
                }
                if (this.foregroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, length, 33);
                }
                if (this.backgroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, length, 33);
                }
            }
            return new SpannableString(spannableStringBuilder);
        }

        public void clear() {
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.clear();
            this.italicsStartPosition = -1;
            this.underlineStartPosition = -1;
            this.foregroundColorStartPosition = -1;
            this.backgroundColorStartPosition = -1;
            this.row = 0;
        }

        public void defineWindow(boolean z, boolean z2, boolean z3, int i, boolean z4, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            this.defined = true;
            this.visible = z;
            this.rowLock = z2;
            this.priority = i;
            this.relativePositioning = z4;
            this.verticalAnchor = i2;
            this.horizontalAnchor = i3;
            this.anchorId = i6;
            int i9 = i4 + 1;
            if (this.rowCount != i9) {
                this.rowCount = i9;
                while (true) {
                    if ((!z2 || this.rolledUpCaptions.size() < this.rowCount) && this.rolledUpCaptions.size() < 15) {
                        break;
                    }
                    this.rolledUpCaptions.remove(0);
                }
            }
            if (i7 != 0 && this.windowStyleId != i7) {
                this.windowStyleId = i7;
                int i10 = i7 - 1;
                setWindowAttributes(WINDOW_STYLE_FILL[i10], COLOR_TRANSPARENT, WINDOW_STYLE_WORD_WRAP[i10], 0, WINDOW_STYLE_PRINT_DIRECTION[i10], WINDOW_STYLE_SCROLL_DIRECTION[i10], WINDOW_STYLE_JUSTIFICATION[i10]);
            }
            if (i8 == 0 || this.penStyleId == i8) {
                return;
            }
            this.penStyleId = i8;
            int i11 = i8 - 1;
            setPenAttributes(0, 1, 1, false, false, PEN_STYLE_EDGE_TYPE[i11], PEN_STYLE_FONT_STYLE[i11]);
            setPenColor(COLOR_SOLID_WHITE, PEN_STYLE_BACKGROUND[i11], COLOR_SOLID_BLACK);
        }

        public boolean isDefined() {
            return this.defined;
        }

        public boolean isEmpty() {
            return !isDefined() || (this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0);
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void reset() {
            clear();
            this.defined = false;
            this.visible = false;
            this.priority = 4;
            this.relativePositioning = false;
            this.verticalAnchor = 0;
            this.horizontalAnchor = 0;
            this.anchorId = 0;
            this.rowCount = 15;
            this.rowLock = true;
            this.justification = 0;
            this.windowStyleId = 0;
            this.penStyleId = 0;
            int i = COLOR_SOLID_BLACK;
            this.windowFillColor = i;
            this.foregroundColor = COLOR_SOLID_WHITE;
            this.backgroundColor = i;
        }

        public void setPenAttributes(int i, int i2, int i3, boolean z, boolean z2, int i4, int i5) {
            if (this.italicsStartPosition != -1) {
                if (!z) {
                    this.captionStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, this.captionStringBuilder.length(), 33);
                    this.italicsStartPosition = -1;
                }
            } else if (z) {
                this.italicsStartPosition = this.captionStringBuilder.length();
            }
            if (this.underlineStartPosition == -1) {
                if (z2) {
                    this.underlineStartPosition = this.captionStringBuilder.length();
                }
            } else if (z2) {
            } else {
                this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
                this.underlineStartPosition = -1;
            }
        }

        public void setPenColor(int i, int i2, int i3) {
            if (this.foregroundColorStartPosition != -1 && this.foregroundColor != i) {
                this.captionStringBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
            if (i != COLOR_SOLID_WHITE) {
                this.foregroundColorStartPosition = this.captionStringBuilder.length();
                this.foregroundColor = i;
            }
            if (this.backgroundColorStartPosition != -1 && this.backgroundColor != i2) {
                this.captionStringBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
            if (i2 != COLOR_SOLID_BLACK) {
                this.backgroundColorStartPosition = this.captionStringBuilder.length();
                this.backgroundColor = i2;
            }
        }

        public void setPenLocation(int i, int i2) {
            if (this.row != i) {
                append('\n');
            }
            this.row = i;
        }

        public void setVisibility(boolean z) {
            this.visible = z;
        }

        public void setWindowAttributes(int i, int i2, boolean z, int i3, int i4, int i5, int i6) {
            this.windowFillColor = i;
            this.justification = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class DtvCcPacket {
        int currentIndex = 0;
        public final byte[] packetData;
        public final int packetSize;
        public final int sequenceNumber;

        public DtvCcPacket(int i, int i2) {
            this.sequenceNumber = i;
            this.packetSize = i2;
            this.packetData = new byte[(i2 * 2) - 1];
        }
    }

    public Cea708Decoder(int i, List list) {
        boolean z = true;
        this.selectedServiceNumber = i == -1 ? 1 : i;
        this.isWideAspectRatio = (list == null || !CodecSpecificDataUtil.parseCea708InitializationData(list)) ? false : false;
        this.cueInfoBuilders = new CueInfoBuilder[8];
        for (int i2 = 0; i2 < 8; i2++) {
            this.cueInfoBuilders[i2] = new CueInfoBuilder();
        }
        this.currentCueInfoBuilder = this.cueInfoBuilders[0];
    }

    private void finalizeCurrentPacket() {
        if (this.currentDtvCcPacket == null) {
            return;
        }
        processCurrentPacket();
        this.currentDtvCcPacket = null;
    }

    private List getDisplayCues() {
        Cea708CueInfo build;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 8; i++) {
            if (!this.cueInfoBuilders[i].isEmpty() && this.cueInfoBuilders[i].isVisible() && (build = this.cueInfoBuilders[i].build()) != null) {
                arrayList.add(build);
            }
        }
        Collections.sort(arrayList, Cea708CueInfo.LEAST_IMPORTANT_FIRST);
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(((Cea708CueInfo) arrayList.get(i2)).cue);
        }
        return Collections.unmodifiableList(arrayList2);
    }

    private void handleC0Command(int i) {
        ParsableBitArray parsableBitArray;
        if (i != 0) {
            if (i == 3) {
                this.cues = getDisplayCues();
                return;
            }
            int i2 = 8;
            if (i == 8) {
                this.currentCueInfoBuilder.backspace();
                return;
            }
            switch (i) {
                case 12:
                    resetCueBuilders();
                    return;
                case 13:
                    this.currentCueInfoBuilder.append('\n');
                    return;
                case 14:
                    return;
                default:
                    if (i >= 17 && i <= 23) {
                        Log.w("Cea708Decoder", "Currently unsupported COMMAND_EXT1 Command: " + i);
                        parsableBitArray = this.captionChannelPacketData;
                    } else if (i < 24 || i > 31) {
                        Log.w("Cea708Decoder", "Invalid C0 command: " + i);
                        return;
                    } else {
                        Log.w("Cea708Decoder", "Currently unsupported COMMAND_P16 Command: " + i);
                        parsableBitArray = this.captionChannelPacketData;
                        i2 = 16;
                    }
                    parsableBitArray.skipBits(i2);
                    return;
            }
        }
    }

    private void handleC1Command(int i) {
        CueInfoBuilder cueInfoBuilder;
        CueInfoBuilder cueInfoBuilder2;
        ParsableBitArray parsableBitArray;
        int i2 = 8;
        int i3 = 1;
        switch (i) {
            case 128:
            case NotificationCenter.walletPendingTransactionsChanged /* 129 */:
            case NotificationCenter.walletSyncProgressChanged /* 130 */:
            case NotificationCenter.httpFileDidLoad /* 131 */:
            case NotificationCenter.httpFileDidFailedLoad /* 132 */:
            case NotificationCenter.didUpdateConnectionState /* 133 */:
            case NotificationCenter.fileUploaded /* 134 */:
            case NotificationCenter.fileUploadFailed /* 135 */:
                int i4 = i - 128;
                if (this.currentWindow != i4) {
                    this.currentWindow = i4;
                    cueInfoBuilder = this.cueInfoBuilders[i4];
                    this.currentCueInfoBuilder = cueInfoBuilder;
                    return;
                }
                return;
            case NotificationCenter.fileUploadProgressChanged /* 136 */:
                while (i3 <= 8) {
                    if (this.captionChannelPacketData.readBit()) {
                        this.cueInfoBuilders[8 - i3].clear();
                    }
                    i3++;
                }
                return;
            case NotificationCenter.fileLoadProgressChanged /* 137 */:
                for (int i5 = 1; i5 <= 8; i5++) {
                    if (this.captionChannelPacketData.readBit()) {
                        this.cueInfoBuilders[8 - i5].setVisibility(true);
                    }
                }
                return;
            case NotificationCenter.fileLoaded /* 138 */:
                while (i3 <= 8) {
                    if (this.captionChannelPacketData.readBit()) {
                        this.cueInfoBuilders[8 - i3].setVisibility(false);
                    }
                    i3++;
                }
                return;
            case NotificationCenter.fileLoadFailed /* 139 */:
                for (int i6 = 1; i6 <= 8; i6++) {
                    if (this.captionChannelPacketData.readBit()) {
                        this.cueInfoBuilders[8 - i6].setVisibility(!cueInfoBuilder2.isVisible());
                    }
                }
                return;
            case NotificationCenter.filePreparingStarted /* 140 */:
                while (i3 <= 8) {
                    if (this.captionChannelPacketData.readBit()) {
                        this.cueInfoBuilders[8 - i3].reset();
                    }
                    i3++;
                }
                return;
            case NotificationCenter.fileNewChunkAvailable /* 141 */:
                parsableBitArray = this.captionChannelPacketData;
                parsableBitArray.skipBits(i2);
                return;
            case NotificationCenter.filePreparingFailed /* 142 */:
                return;
            case NotificationCenter.dialogsUnreadCounterChanged /* 143 */:
                resetCueBuilders();
                return;
            case NotificationCenter.messagePlayingProgressDidChanged /* 144 */:
                if (this.currentCueInfoBuilder.isDefined()) {
                    handleSetPenAttributes();
                    return;
                }
                parsableBitArray = this.captionChannelPacketData;
                i2 = 16;
                parsableBitArray.skipBits(i2);
                return;
            case NotificationCenter.messagePlayingDidReset /* 145 */:
                if (this.currentCueInfoBuilder.isDefined()) {
                    handleSetPenColor();
                    return;
                }
                parsableBitArray = this.captionChannelPacketData;
                i2 = 24;
                parsableBitArray.skipBits(i2);
                return;
            case NotificationCenter.messagePlayingPlayStateChanged /* 146 */:
                if (this.currentCueInfoBuilder.isDefined()) {
                    handleSetPenLocation();
                    return;
                }
                parsableBitArray = this.captionChannelPacketData;
                i2 = 16;
                parsableBitArray.skipBits(i2);
                return;
            case NotificationCenter.messagePlayingDidStart /* 147 */:
            case NotificationCenter.messagePlayingDidSeek /* 148 */:
            case NotificationCenter.messagePlayingGoingToStop /* 149 */:
            case 150:
            default:
                Log.w("Cea708Decoder", "Invalid C1 command: " + i);
                return;
            case NotificationCenter.recordStarted /* 151 */:
                if (this.currentCueInfoBuilder.isDefined()) {
                    handleSetWindowAttributes();
                    return;
                }
                parsableBitArray = this.captionChannelPacketData;
                i2 = 32;
                parsableBitArray.skipBits(i2);
                return;
            case NotificationCenter.recordStartError /* 152 */:
            case NotificationCenter.recordStopped /* 153 */:
            case NotificationCenter.recordPaused /* 154 */:
            case NotificationCenter.recordResumed /* 155 */:
            case NotificationCenter.screenshotTook /* 156 */:
            case NotificationCenter.albumsDidLoad /* 157 */:
            case 158:
            case NotificationCenter.audioRecordTooShort /* 159 */:
                int i7 = i - 152;
                handleDefineWindow(i7);
                if (this.currentWindow != i7) {
                    this.currentWindow = i7;
                    cueInfoBuilder = this.cueInfoBuilders[i7];
                    this.currentCueInfoBuilder = cueInfoBuilder;
                    return;
                }
                return;
        }
    }

    private void handleC2Command(int i) {
        ParsableBitArray parsableBitArray;
        int i2;
        if (i <= 7) {
            return;
        }
        if (i <= 15) {
            parsableBitArray = this.captionChannelPacketData;
            i2 = 8;
        } else if (i <= 23) {
            parsableBitArray = this.captionChannelPacketData;
            i2 = 16;
        } else if (i > 31) {
            return;
        } else {
            parsableBitArray = this.captionChannelPacketData;
            i2 = 24;
        }
        parsableBitArray.skipBits(i2);
    }

    private void handleC3Command(int i) {
        ParsableBitArray parsableBitArray;
        int i2;
        if (i <= 135) {
            parsableBitArray = this.captionChannelPacketData;
            i2 = 32;
        } else if (i > 143) {
            if (i <= 159) {
                this.captionChannelPacketData.skipBits(2);
                this.captionChannelPacketData.skipBits(this.captionChannelPacketData.readBits(6) * 8);
                return;
            }
            return;
        } else {
            parsableBitArray = this.captionChannelPacketData;
            i2 = 40;
        }
        parsableBitArray.skipBits(i2);
    }

    private void handleDefineWindow(int i) {
        CueInfoBuilder cueInfoBuilder = this.cueInfoBuilders[i];
        this.captionChannelPacketData.skipBits(2);
        boolean readBit = this.captionChannelPacketData.readBit();
        boolean readBit2 = this.captionChannelPacketData.readBit();
        boolean readBit3 = this.captionChannelPacketData.readBit();
        int readBits = this.captionChannelPacketData.readBits(3);
        boolean readBit4 = this.captionChannelPacketData.readBit();
        int readBits2 = this.captionChannelPacketData.readBits(7);
        int readBits3 = this.captionChannelPacketData.readBits(8);
        int readBits4 = this.captionChannelPacketData.readBits(4);
        int readBits5 = this.captionChannelPacketData.readBits(4);
        this.captionChannelPacketData.skipBits(2);
        int readBits6 = this.captionChannelPacketData.readBits(6);
        this.captionChannelPacketData.skipBits(2);
        cueInfoBuilder.defineWindow(readBit, readBit2, readBit3, readBits, readBit4, readBits2, readBits3, readBits5, readBits6, readBits4, this.captionChannelPacketData.readBits(3), this.captionChannelPacketData.readBits(3));
    }

    private void handleG0Character(int i) {
        if (i == 127) {
            this.currentCueInfoBuilder.append((char) 9835);
        } else {
            this.currentCueInfoBuilder.append((char) (i & NotificationCenter.messagePlayingSpeedChanged));
        }
    }

    private void handleG1Character(int i) {
        this.currentCueInfoBuilder.append((char) (i & NotificationCenter.messagePlayingSpeedChanged));
    }

    private void handleG2Character(int i) {
        CueInfoBuilder cueInfoBuilder;
        char c = ' ';
        if (i == 32) {
            cueInfoBuilder = this.currentCueInfoBuilder;
        } else if (i == 33) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 160;
        } else if (i == 37) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 8230;
        } else if (i == 42) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 352;
        } else if (i == 44) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 338;
        } else if (i == 63) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 376;
        } else if (i == 57) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 8482;
        } else if (i == 58) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 353;
        } else if (i == 60) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 339;
        } else if (i != 61) {
            switch (i) {
                case 48:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 9608;
                    break;
                case 49:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 8216;
                    break;
                case 50:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 8217;
                    break;
                case 51:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 8220;
                    break;
                case 52:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 8221;
                    break;
                case 53:
                    cueInfoBuilder = this.currentCueInfoBuilder;
                    c = 8226;
                    break;
                default:
                    switch (i) {
                        case 118:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 8539;
                            break;
                        case 119:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 8540;
                            break;
                        case 120:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 8541;
                            break;
                        case 121:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 8542;
                            break;
                        case 122:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9474;
                            break;
                        case 123:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9488;
                            break;
                        case 124:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9492;
                            break;
                        case 125:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9472;
                            break;
                        case 126:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9496;
                            break;
                        case NotificationCenter.dialogTranslate /* 127 */:
                            cueInfoBuilder = this.currentCueInfoBuilder;
                            c = 9484;
                            break;
                        default:
                            Log.w("Cea708Decoder", "Invalid G2 character: " + i);
                            return;
                    }
            }
        } else {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 8480;
        }
        cueInfoBuilder.append(c);
    }

    private void handleG3Character(int i) {
        CueInfoBuilder cueInfoBuilder;
        char c;
        if (i == 160) {
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = 13252;
        } else {
            Log.w("Cea708Decoder", "Invalid G3 character: " + i);
            cueInfoBuilder = this.currentCueInfoBuilder;
            c = '_';
        }
        cueInfoBuilder.append(c);
    }

    private void handleSetPenAttributes() {
        this.currentCueInfoBuilder.setPenAttributes(this.captionChannelPacketData.readBits(4), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBit(), this.captionChannelPacketData.readBit(), this.captionChannelPacketData.readBits(3), this.captionChannelPacketData.readBits(3));
    }

    private void handleSetPenColor() {
        int argbColorFromCeaColor = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        int argbColorFromCeaColor2 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        this.captionChannelPacketData.skipBits(2);
        this.currentCueInfoBuilder.setPenColor(argbColorFromCeaColor, argbColorFromCeaColor2, CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2)));
    }

    private void handleSetPenLocation() {
        this.captionChannelPacketData.skipBits(4);
        int readBits = this.captionChannelPacketData.readBits(4);
        this.captionChannelPacketData.skipBits(2);
        this.currentCueInfoBuilder.setPenLocation(readBits, this.captionChannelPacketData.readBits(6));
    }

    private void handleSetWindowAttributes() {
        int argbColorFromCeaColor = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        int readBits = this.captionChannelPacketData.readBits(2);
        int argbColorFromCeaColor2 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        if (this.captionChannelPacketData.readBit()) {
            readBits |= 4;
        }
        boolean readBit = this.captionChannelPacketData.readBit();
        int readBits2 = this.captionChannelPacketData.readBits(2);
        int readBits3 = this.captionChannelPacketData.readBits(2);
        int readBits4 = this.captionChannelPacketData.readBits(2);
        this.captionChannelPacketData.skipBits(8);
        this.currentCueInfoBuilder.setWindowAttributes(argbColorFromCeaColor, argbColorFromCeaColor2, readBit, readBits, readBits2, readBits3, readBits4);
    }

    private void processCurrentPacket() {
        StringBuilder sb;
        String str;
        DtvCcPacket dtvCcPacket = this.currentDtvCcPacket;
        if (dtvCcPacket.currentIndex != (dtvCcPacket.packetSize * 2) - 1) {
            Log.d("Cea708Decoder", "DtvCcPacket ended prematurely; size is " + ((this.currentDtvCcPacket.packetSize * 2) - 1) + ", but current index is " + this.currentDtvCcPacket.currentIndex + " (sequence number " + this.currentDtvCcPacket.sequenceNumber + ");");
        }
        ParsableBitArray parsableBitArray = this.captionChannelPacketData;
        DtvCcPacket dtvCcPacket2 = this.currentDtvCcPacket;
        parsableBitArray.reset(dtvCcPacket2.packetData, dtvCcPacket2.currentIndex);
        boolean z = false;
        while (true) {
            if (this.captionChannelPacketData.bitsLeft() <= 0) {
                break;
            }
            int readBits = this.captionChannelPacketData.readBits(3);
            int readBits2 = this.captionChannelPacketData.readBits(5);
            if (readBits == 7) {
                this.captionChannelPacketData.skipBits(2);
                readBits = this.captionChannelPacketData.readBits(6);
                if (readBits < 7) {
                    Log.w("Cea708Decoder", "Invalid extended service number: " + readBits);
                }
            }
            if (readBits2 == 0) {
                if (readBits != 0) {
                    Log.w("Cea708Decoder", "serviceNumber is non-zero (" + readBits + ") when blockSize is 0");
                }
            } else if (readBits != this.selectedServiceNumber) {
                this.captionChannelPacketData.skipBytes(readBits2);
            } else {
                int position = this.captionChannelPacketData.getPosition() + (readBits2 * 8);
                while (this.captionChannelPacketData.getPosition() < position) {
                    int readBits3 = this.captionChannelPacketData.readBits(8);
                    if (readBits3 == 16) {
                        readBits3 = this.captionChannelPacketData.readBits(8);
                        if (readBits3 <= 31) {
                            handleC2Command(readBits3);
                        } else {
                            if (readBits3 <= 127) {
                                handleG2Character(readBits3);
                            } else if (readBits3 <= 159) {
                                handleC3Command(readBits3);
                            } else if (readBits3 <= 255) {
                                handleG3Character(readBits3);
                            } else {
                                sb = new StringBuilder();
                                str = "Invalid extended command: ";
                                sb.append(str);
                                sb.append(readBits3);
                                Log.w("Cea708Decoder", sb.toString());
                            }
                            z = true;
                        }
                    } else if (readBits3 <= 31) {
                        handleC0Command(readBits3);
                    } else {
                        if (readBits3 <= 127) {
                            handleG0Character(readBits3);
                        } else if (readBits3 <= 159) {
                            handleC1Command(readBits3);
                        } else if (readBits3 <= 255) {
                            handleG1Character(readBits3);
                        } else {
                            sb = new StringBuilder();
                            str = "Invalid base command: ";
                            sb.append(str);
                            sb.append(readBits3);
                            Log.w("Cea708Decoder", sb.toString());
                        }
                        z = true;
                    }
                }
            }
        }
        if (z) {
            this.cues = getDisplayCues();
        }
    }

    private void resetCueBuilders() {
        for (int i = 0; i < 8; i++) {
            this.cueInfoBuilders[i].reset();
        }
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected Subtitle createSubtitle() {
        List list = this.cues;
        this.lastCues = list;
        return new CeaSubtitle((List) Assertions.checkNotNull(list));
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected void decode(SubtitleInputBuffer subtitleInputBuffer) {
        ByteBuffer byteBuffer = (ByteBuffer) Assertions.checkNotNull(subtitleInputBuffer.data);
        this.ccData.reset(byteBuffer.array(), byteBuffer.limit());
        while (this.ccData.bytesLeft() >= 3) {
            int readUnsignedByte = this.ccData.readUnsignedByte();
            int i = readUnsignedByte & 3;
            boolean z = (readUnsignedByte & 4) == 4;
            byte readUnsignedByte2 = (byte) this.ccData.readUnsignedByte();
            byte readUnsignedByte3 = (byte) this.ccData.readUnsignedByte();
            if (i == 2 || i == 3) {
                if (z) {
                    if (i == 3) {
                        finalizeCurrentPacket();
                        int i2 = (readUnsignedByte2 & 192) >> 6;
                        int i3 = this.previousSequenceNumber;
                        if (i3 != -1 && i2 != (i3 + 1) % 4) {
                            resetCueBuilders();
                            Log.w("Cea708Decoder", "Sequence number discontinuity. previous=" + this.previousSequenceNumber + " current=" + i2);
                        }
                        this.previousSequenceNumber = i2;
                        int i4 = readUnsignedByte2 & 63;
                        if (i4 == 0) {
                            i4 = 64;
                        }
                        DtvCcPacket dtvCcPacket = new DtvCcPacket(i2, i4);
                        this.currentDtvCcPacket = dtvCcPacket;
                        byte[] bArr = dtvCcPacket.packetData;
                        int i5 = dtvCcPacket.currentIndex;
                        dtvCcPacket.currentIndex = i5 + 1;
                        bArr[i5] = readUnsignedByte3;
                    } else {
                        Assertions.checkArgument(i == 2);
                        DtvCcPacket dtvCcPacket2 = this.currentDtvCcPacket;
                        if (dtvCcPacket2 == null) {
                            Log.e("Cea708Decoder", "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
                        } else {
                            byte[] bArr2 = dtvCcPacket2.packetData;
                            int i6 = dtvCcPacket2.currentIndex;
                            bArr2[i6] = readUnsignedByte2;
                            dtvCcPacket2.currentIndex = i6 + 2;
                            bArr2[i6 + 1] = readUnsignedByte3;
                        }
                    }
                    DtvCcPacket dtvCcPacket3 = this.currentDtvCcPacket;
                    if (dtvCcPacket3.currentIndex == (dtvCcPacket3.packetSize * 2) - 1) {
                        finalizeCurrentPacket();
                    }
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() {
        return super.dequeueInputBuffer();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() {
        return super.dequeueOutputBuffer();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        this.currentWindow = 0;
        this.currentCueInfoBuilder = this.cueInfoBuilders[0];
        resetCueBuilders();
        this.currentDtvCcPacket = null;
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public String getName() {
        return "Cea708Decoder";
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer subtitleInputBuffer) {
        super.queueInputBuffer(subtitleInputBuffer);
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.text.SubtitleDecoder
    public /* bridge */ /* synthetic */ void setPositionUs(long j) {
        super.setPositionUs(j);
    }
}
