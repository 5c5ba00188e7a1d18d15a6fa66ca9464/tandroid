package com.google.android.exoplayer2.text.cea;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public final class Cea608Decoder extends CeaDecoder {
    private int captionMode;
    private int captionRowCount;
    private List cues;
    private boolean isCaptionValid;
    private boolean isInCaptionService;
    private long lastCueUpdateUs;
    private List lastCues;
    private final int packetLength;
    private byte repeatableControlCc1;
    private byte repeatableControlCc2;
    private boolean repeatableControlSet;
    private final int selectedChannel;
    private final int selectedField;
    private final long validDataChannelTimeoutUs;
    private static final int[] ROW_INDICES = {11, 1, 3, 12, 14, 5, 7, 9};
    private static final int[] COLUMN_INDICES = {0, 4, 8, 12, 16, 20, 24, 28};
    private static final int[] STYLE_COLORS = {-1, -16711936, -16776961, -16711681, -65536, -256, -65281};
    private static final int[] BASIC_CHARACTER_SET = {32, 33, 34, 35, 36, 37, 38, 39, 40, 41, NotificationCenter.botDownloadsUpdate, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, NotificationCenter.closeOtherAppActivities, 93, NotificationCenter.themeListUpdated, NotificationCenter.goingToPreviewTheme, NotificationCenter.proxyCheckDone, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, NotificationCenter.emojiLoaded, NotificationCenter.suggestedLangpack, NotificationCenter.starOptionsLoaded, NotificationCenter.needShareTheme, 9632};
    private static final int[] SPECIAL_CHARACTER_SET = {NotificationCenter.newEmojiSuggestionsAvailable, NotificationCenter.themeUploadError, NotificationCenter.storiesLimitUpdate, NotificationCenter.unconfirmedAuthUpdate, 8482, NotificationCenter.groupCallUpdated, NotificationCenter.groupCallSpeakingUsersUpdated, 9834, NotificationCenter.updateStories, 32, NotificationCenter.invalidateMotionBackground, NotificationCenter.pushMessagesUpdated, NotificationCenter.cameraInitied, NotificationCenter.didApplyNewTheme, NotificationCenter.locationPermissionGranted, NotificationCenter.proxyChangedByRotation};
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = {193, NotificationCenter.openBoostForUsersDialog, NotificationCenter.starGiveawayOptionsLoaded, NotificationCenter.channelStarsUpdated, NotificationCenter.updateAllMessages, NotificationCenter.liveLocationsChanged, 8216, NotificationCenter.didStartedCall, 42, 39, 8212, NotificationCenter.closeInCallActivity, 8480, 8226, 8220, 8221, NotificationCenter.dialogPhotosUpdate, NotificationCenter.savedMessagesDialogsUpdate, NotificationCenter.smsJobStatusUpdate, NotificationCenter.storyQualityUpdate, NotificationCenter.groupRestrictionsUnlockedByBoosts, 203, NotificationCenter.didReplacedPhotoInMemCache, NotificationCenter.customStickerCreated, NotificationCenter.premiumFloodWaitReceived, NotificationCenter.themeAccentListUpdated, NotificationCenter.starBalanceUpdated, NotificationCenter.botStarsTransactionsLoaded, NotificationCenter.proxySettingsChanged, NotificationCenter.webViewResolved, NotificationCenter.appDidLogout, NotificationCenter.storiesEnabledUpdate};
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = {NotificationCenter.savedReactionTagsUpdate, NotificationCenter.wallpapersDidLoad, NotificationCenter.timezonesUpdated, NotificationCenter.groupPackUpdated, NotificationCenter.didSetNewTheme, NotificationCenter.starGiftOptionsLoaded, NotificationCenter.needSetDayNightTheme, NotificationCenter.starTransactionsLoaded, NotificationCenter.locationPermissionDenied, 123, 125, 92, 94, 95, 124, 126, NotificationCenter.userIsPremiumBlockedUpadted, NotificationCenter.wallpapersNeedReload, NotificationCenter.starSubscriptionsLoaded, NotificationCenter.reloadInterface, NotificationCenter.starGiftSoldOut, NotificationCenter.activeGroupCallsUpdated, NotificationCenter.groupCallScreencastStateChanged, 9474, NotificationCenter.savedMessagesForwarded, NotificationCenter.didReceiveSmsCode, NotificationCenter.botStarsUpdated, NotificationCenter.didSetNewWallpapper, 9484, 9488, 9492, 9496};
    private static final boolean[] ODD_PARITY_BYTE_TABLE = {false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
    private final ParsableByteArray ccData = new ParsableByteArray();
    private final ArrayList cueBuilders = new ArrayList();
    private CueBuilder currentCueBuilder = new CueBuilder(0, 4);
    private int currentChannel = 0;

    private static final class CueBuilder {
        private int captionMode;
        private int captionRowCount;
        private int indent;
        private int row;
        private int tabOffset;
        private final List cueStyles = new ArrayList();
        private final List rolledUpCaptions = new ArrayList();
        private final StringBuilder captionStringBuilder = new StringBuilder();

        private static class CueStyle {
            public int start;
            public final int style;
            public final boolean underline;

            public CueStyle(int i, boolean z, int i2) {
                this.style = i;
                this.underline = z;
                this.start = i2;
            }
        }

        public CueBuilder(int i, int i2) {
            reset(i);
            this.captionRowCount = i2;
        }

        private SpannableString buildCurrentLine() {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.captionStringBuilder);
            int length = spannableStringBuilder.length();
            int i = 0;
            int i2 = -1;
            int i3 = -1;
            int i4 = 0;
            int i5 = -1;
            boolean z = false;
            int i6 = -1;
            while (i < this.cueStyles.size()) {
                CueStyle cueStyle = (CueStyle) this.cueStyles.get(i);
                boolean z2 = cueStyle.underline;
                int i7 = cueStyle.style;
                if (i7 != 8) {
                    boolean z3 = i7 == 7;
                    if (i7 != 7) {
                        i6 = Cea608Decoder.STYLE_COLORS[i7];
                    }
                    z = z3;
                }
                int i8 = cueStyle.start;
                i++;
                if (i8 != (i < this.cueStyles.size() ? ((CueStyle) this.cueStyles.get(i)).start : length)) {
                    if (i2 != -1 && !z2) {
                        setUnderlineSpan(spannableStringBuilder, i2, i8);
                        i2 = -1;
                    } else if (i2 == -1 && z2) {
                        i2 = i8;
                    }
                    if (i3 != -1 && !z) {
                        setItalicSpan(spannableStringBuilder, i3, i8);
                        i3 = -1;
                    } else if (i3 == -1 && z) {
                        i3 = i8;
                    }
                    if (i6 != i5) {
                        setColorSpan(spannableStringBuilder, i4, i8, i5);
                        i5 = i6;
                        i4 = i8;
                    }
                }
            }
            if (i2 != -1 && i2 != length) {
                setUnderlineSpan(spannableStringBuilder, i2, length);
            }
            if (i3 != -1 && i3 != length) {
                setItalicSpan(spannableStringBuilder, i3, length);
            }
            if (i4 != length) {
                setColorSpan(spannableStringBuilder, i4, length, i5);
            }
            return new SpannableString(spannableStringBuilder);
        }

        private static void setColorSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2, int i3) {
            if (i3 == -1) {
                return;
            }
            spannableStringBuilder.setSpan(new ForegroundColorSpan(i3), i, i2, 33);
        }

        private static void setItalicSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
            spannableStringBuilder.setSpan(new StyleSpan(2), i, i2, 33);
        }

        private static void setUnderlineSpan(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
            spannableStringBuilder.setSpan(new UnderlineSpan(), i, i2, 33);
        }

        public void append(char c) {
            if (this.captionStringBuilder.length() < 32) {
                this.captionStringBuilder.append(c);
            }
        }

        public void backspace() {
            int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
                for (int size = this.cueStyles.size() - 1; size >= 0; size--) {
                    CueStyle cueStyle = (CueStyle) this.cueStyles.get(size);
                    int i = cueStyle.start;
                    if (i != length) {
                        return;
                    }
                    cueStyle.start = i - 1;
                }
            }
        }

        public Cue build(int i) {
            float f;
            int i2 = this.indent + this.tabOffset;
            int i3 = 32 - i2;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i4 = 0; i4 < this.rolledUpCaptions.size(); i4++) {
                spannableStringBuilder.append(Util.truncateAscii((CharSequence) this.rolledUpCaptions.get(i4), i3));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append(Util.truncateAscii(buildCurrentLine(), i3));
            if (spannableStringBuilder.length() == 0) {
                return null;
            }
            int length = i3 - spannableStringBuilder.length();
            int i5 = i2 - length;
            if (i == Integer.MIN_VALUE) {
                i = (this.captionMode != 2 || (Math.abs(i5) >= 3 && length >= 0)) ? (this.captionMode != 2 || i5 <= 0) ? 0 : 2 : 1;
            }
            if (i != 1) {
                if (i == 2) {
                    i2 = 32 - length;
                }
                f = ((i2 / 32.0f) * 0.8f) + 0.1f;
            } else {
                f = 0.5f;
            }
            int i6 = this.row;
            if (i6 > 7) {
                i6 -= 17;
            } else if (this.captionMode == 1) {
                i6 -= this.captionRowCount - 1;
            }
            return new Cue.Builder().setText(spannableStringBuilder).setTextAlignment(Layout.Alignment.ALIGN_NORMAL).setLine(i6, 1).setPosition(f).setPositionAnchor(i).build();
        }

        public boolean isEmpty() {
            return this.cueStyles.isEmpty() && this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0;
        }

        public void reset(int i) {
            this.captionMode = i;
            this.cueStyles.clear();
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.setLength(0);
            this.row = 15;
            this.indent = 0;
            this.tabOffset = 0;
        }

        public void rollUp() {
            this.rolledUpCaptions.add(buildCurrentLine());
            this.captionStringBuilder.setLength(0);
            this.cueStyles.clear();
            int min = Math.min(this.captionRowCount, this.row);
            while (this.rolledUpCaptions.size() >= min) {
                this.rolledUpCaptions.remove(0);
            }
        }

        public void setCaptionMode(int i) {
            this.captionMode = i;
        }

        public void setCaptionRowCount(int i) {
            this.captionRowCount = i;
        }

        public void setStyle(int i, boolean z) {
            this.cueStyles.add(new CueStyle(i, z, this.captionStringBuilder.length()));
        }
    }

    public Cea608Decoder(String str, int i, long j) {
        this.validDataChannelTimeoutUs = j > 0 ? j * 1000 : -9223372036854775807L;
        this.packetLength = "application/x-mp4-cea-608".equals(str) ? 2 : 3;
        if (i != 1) {
            if (i == 2) {
                this.selectedChannel = 1;
                this.selectedField = 0;
                setCaptionMode(0);
                resetCueBuilders();
                this.isInCaptionService = true;
                this.lastCueUpdateUs = -9223372036854775807L;
            }
            if (i == 3) {
                this.selectedChannel = 0;
            } else if (i != 4) {
                Log.w("Cea608Decoder", "Invalid channel. Defaulting to CC1.");
            } else {
                this.selectedChannel = 1;
            }
            this.selectedField = 1;
            setCaptionMode(0);
            resetCueBuilders();
            this.isInCaptionService = true;
            this.lastCueUpdateUs = -9223372036854775807L;
        }
        this.selectedChannel = 0;
        this.selectedField = 0;
        setCaptionMode(0);
        resetCueBuilders();
        this.isInCaptionService = true;
        this.lastCueUpdateUs = -9223372036854775807L;
    }

    private static char getBasicChar(byte b) {
        return (char) BASIC_CHARACTER_SET[(b & Byte.MAX_VALUE) - 32];
    }

    private static int getChannel(byte b) {
        return (b >> 3) & 1;
    }

    private List getDisplayCues() {
        int size = this.cueBuilders.size();
        ArrayList arrayList = new ArrayList(size);
        int i = 2;
        for (int i2 = 0; i2 < size; i2++) {
            Cue build = ((CueBuilder) this.cueBuilders.get(i2)).build(Integer.MIN_VALUE);
            arrayList.add(build);
            if (build != null) {
                i = Math.min(i, build.positionAnchor);
            }
        }
        ArrayList arrayList2 = new ArrayList(size);
        for (int i3 = 0; i3 < size; i3++) {
            Cue cue = (Cue) arrayList.get(i3);
            if (cue != null) {
                if (cue.positionAnchor != i) {
                    cue = (Cue) Assertions.checkNotNull(((CueBuilder) this.cueBuilders.get(i3)).build(i));
                }
                arrayList2.add(cue);
            }
        }
        return arrayList2;
    }

    private static char getExtendedEsFrChar(byte b) {
        return (char) SPECIAL_ES_FR_CHARACTER_SET[b & 31];
    }

    private static char getExtendedPtDeChar(byte b) {
        return (char) SPECIAL_PT_DE_CHARACTER_SET[b & 31];
    }

    private static char getExtendedWestEuropeanChar(byte b, byte b2) {
        return (b & 1) == 0 ? getExtendedEsFrChar(b2) : getExtendedPtDeChar(b2);
    }

    private static char getSpecialNorthAmericanChar(byte b) {
        return (char) SPECIAL_CHARACTER_SET[b & 15];
    }

    private void handleMidrowCtrl(byte b) {
        this.currentCueBuilder.append(' ');
        this.currentCueBuilder.setStyle((b >> 1) & 7, (b & 1) == 1);
    }

    private void handleMiscCode(byte b) {
        if (b == 32) {
            setCaptionMode(2);
            return;
        }
        if (b == 41) {
            setCaptionMode(3);
            return;
        }
        switch (b) {
            case 37:
                setCaptionMode(1);
                setCaptionRowCount(2);
                break;
            case 38:
                setCaptionMode(1);
                setCaptionRowCount(3);
                break;
            case 39:
                setCaptionMode(1);
                setCaptionRowCount(4);
                break;
            default:
                int i = this.captionMode;
                if (i != 0) {
                    if (b == 33) {
                        this.currentCueBuilder.backspace();
                        break;
                    } else {
                        switch (b) {
                            case 44:
                                this.cues = Collections.emptyList();
                                int i2 = this.captionMode;
                                if (i2 != 1 && i2 != 3) {
                                }
                                break;
                            case 45:
                                if (i == 1 && !this.currentCueBuilder.isEmpty()) {
                                    this.currentCueBuilder.rollUp();
                                    break;
                                }
                                break;
                            case 47:
                                this.cues = getDisplayCues();
                                break;
                        }
                        resetCueBuilders();
                        break;
                    }
                }
                break;
        }
    }

    private void handlePreambleAddressCode(byte b, byte b2) {
        int i = ROW_INDICES[b & 7];
        if ((b2 & 32) != 0) {
            i++;
        }
        if (i != this.currentCueBuilder.row) {
            if (this.captionMode != 1 && !this.currentCueBuilder.isEmpty()) {
                CueBuilder cueBuilder = new CueBuilder(this.captionMode, this.captionRowCount);
                this.currentCueBuilder = cueBuilder;
                this.cueBuilders.add(cueBuilder);
            }
            this.currentCueBuilder.row = i;
        }
        boolean z = (b2 & 16) == 16;
        boolean z2 = (b2 & 1) == 1;
        int i2 = (b2 >> 1) & 7;
        this.currentCueBuilder.setStyle(z ? 8 : i2, z2);
        if (z) {
            this.currentCueBuilder.indent = COLUMN_INDICES[i2];
        }
    }

    private static boolean isCtrlCode(byte b) {
        return (b & 224) == 0;
    }

    private static boolean isExtendedWestEuropeanChar(byte b, byte b2) {
        return (b & 246) == 18 && (b2 & 224) == 32;
    }

    private static boolean isMidrowCtrlCode(byte b, byte b2) {
        return (b & 247) == 17 && (b2 & 240) == 32;
    }

    private static boolean isMiscCode(byte b, byte b2) {
        return (b & 246) == 20 && (b2 & 240) == 32;
    }

    private static boolean isPreambleAddressCode(byte b, byte b2) {
        return (b & 240) == 16 && (b2 & 192) == 64;
    }

    private static boolean isRepeatable(byte b) {
        return (b & 240) == 16;
    }

    private boolean isRepeatedCommand(boolean z, byte b, byte b2) {
        if (!z || !isRepeatable(b)) {
            this.repeatableControlSet = false;
        } else {
            if (this.repeatableControlSet && this.repeatableControlCc1 == b && this.repeatableControlCc2 == b2) {
                this.repeatableControlSet = false;
                return true;
            }
            this.repeatableControlSet = true;
            this.repeatableControlCc1 = b;
            this.repeatableControlCc2 = b2;
        }
        return false;
    }

    private static boolean isServiceSwitchCommand(byte b) {
        return (b & 246) == 20;
    }

    private static boolean isSpecialNorthAmericanChar(byte b, byte b2) {
        return (b & 247) == 17 && (b2 & 240) == 48;
    }

    private static boolean isTabCtrlCode(byte b, byte b2) {
        return (b & 247) == 23 && b2 >= 33 && b2 <= 35;
    }

    private static boolean isXdsControlCode(byte b) {
        return 1 <= b && b <= 15;
    }

    private void maybeUpdateIsInCaptionService(byte b, byte b2) {
        boolean z;
        if (!isXdsControlCode(b)) {
            if (isServiceSwitchCommand(b)) {
                if (b2 != 32 && b2 != 47) {
                    switch (b2) {
                        default:
                            switch (b2) {
                            }
                            this.isInCaptionService = z;
                            break;
                        case 37:
                        case 38:
                        case 39:
                            z = true;
                            this.isInCaptionService = z;
                            break;
                    }
                    return;
                }
                z = true;
                this.isInCaptionService = z;
            }
            return;
        }
        z = false;
        this.isInCaptionService = z;
    }

    private void resetCueBuilders() {
        this.currentCueBuilder.reset(this.captionMode);
        this.cueBuilders.clear();
        this.cueBuilders.add(this.currentCueBuilder);
    }

    private void setCaptionMode(int i) {
        int i2 = this.captionMode;
        if (i2 == i) {
            return;
        }
        this.captionMode = i;
        if (i == 3) {
            for (int i3 = 0; i3 < this.cueBuilders.size(); i3++) {
                ((CueBuilder) this.cueBuilders.get(i3)).setCaptionMode(i);
            }
            return;
        }
        resetCueBuilders();
        if (i2 == 3 || i == 1 || i == 0) {
            this.cues = Collections.emptyList();
        }
    }

    private void setCaptionRowCount(int i) {
        this.captionRowCount = i;
        this.currentCueBuilder.setCaptionRowCount(i);
    }

    private boolean shouldClearStuckCaptions() {
        return (this.validDataChannelTimeoutUs == -9223372036854775807L || this.lastCueUpdateUs == -9223372036854775807L || getPositionUs() - this.lastCueUpdateUs < this.validDataChannelTimeoutUs) ? false : true;
    }

    private boolean updateAndVerifyCurrentChannel(byte b) {
        if (isCtrlCode(b)) {
            this.currentChannel = getChannel(b);
        }
        return this.currentChannel == this.selectedChannel;
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected Subtitle createSubtitle() {
        List list = this.cues;
        this.lastCues = list;
        return new CeaSubtitle((List) Assertions.checkNotNull(list));
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x006d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0017 A[SYNTHETIC] */
    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void decode(SubtitleInputBuffer subtitleInputBuffer) {
        boolean z;
        CueBuilder cueBuilder;
        char basicChar;
        ByteBuffer byteBuffer = (ByteBuffer) Assertions.checkNotNull(subtitleInputBuffer.data);
        this.ccData.reset(byteBuffer.array(), byteBuffer.limit());
        boolean z2 = false;
        while (true) {
            int bytesLeft = this.ccData.bytesLeft();
            int i = this.packetLength;
            if (bytesLeft < i) {
                break;
            }
            int readUnsignedByte = i == 2 ? -4 : this.ccData.readUnsignedByte();
            int readUnsignedByte2 = this.ccData.readUnsignedByte();
            int readUnsignedByte3 = this.ccData.readUnsignedByte();
            if ((readUnsignedByte & 2) == 0 && (readUnsignedByte & 1) == this.selectedField) {
                byte b = (byte) (readUnsignedByte2 & NotificationCenter.dialogTranslate);
                byte b2 = (byte) (readUnsignedByte3 & NotificationCenter.dialogTranslate);
                if (b != 0 || b2 != 0) {
                    boolean z3 = this.isCaptionValid;
                    if ((readUnsignedByte & 4) == 4) {
                        boolean[] zArr = ODD_PARITY_BYTE_TABLE;
                        if (zArr[readUnsignedByte2] && zArr[readUnsignedByte3]) {
                            z = true;
                            this.isCaptionValid = z;
                            if (!isRepeatedCommand(z, b, b2)) {
                                if (this.isCaptionValid) {
                                    maybeUpdateIsInCaptionService(b, b2);
                                    if (this.isInCaptionService && updateAndVerifyCurrentChannel(b)) {
                                        if (isCtrlCode(b)) {
                                            if (isSpecialNorthAmericanChar(b, b2)) {
                                                cueBuilder = this.currentCueBuilder;
                                                basicChar = getSpecialNorthAmericanChar(b2);
                                            } else if (isExtendedWestEuropeanChar(b, b2)) {
                                                this.currentCueBuilder.backspace();
                                                cueBuilder = this.currentCueBuilder;
                                                basicChar = getExtendedWestEuropeanChar(b, b2);
                                            } else {
                                                if (isMidrowCtrlCode(b, b2)) {
                                                    handleMidrowCtrl(b2);
                                                } else if (isPreambleAddressCode(b, b2)) {
                                                    handlePreambleAddressCode(b, b2);
                                                } else if (isTabCtrlCode(b, b2)) {
                                                    this.currentCueBuilder.tabOffset = b2 - 32;
                                                } else if (isMiscCode(b, b2)) {
                                                    handleMiscCode(b2);
                                                }
                                                z2 = true;
                                            }
                                            cueBuilder.append(basicChar);
                                            z2 = true;
                                        } else {
                                            this.currentCueBuilder.append(getBasicChar(b));
                                            if ((b2 & 224) != 0) {
                                                cueBuilder = this.currentCueBuilder;
                                                basicChar = getBasicChar(b2);
                                                cueBuilder.append(basicChar);
                                            }
                                            z2 = true;
                                        }
                                    }
                                } else if (z3) {
                                    resetCueBuilders();
                                    z2 = true;
                                }
                            }
                        }
                    }
                    z = false;
                    this.isCaptionValid = z;
                    if (!isRepeatedCommand(z, b, b2)) {
                    }
                }
            }
        }
        if (z2) {
            int i2 = this.captionMode;
            if (i2 == 1 || i2 == 3) {
                this.cues = getDisplayCues();
                this.lastCueUpdateUs = getPositionUs();
            }
        }
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() {
        return super.dequeueInputBuffer();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public SubtitleOutputBuffer dequeueOutputBuffer() {
        SubtitleOutputBuffer availableOutputBuffer;
        SubtitleOutputBuffer dequeueOutputBuffer = super.dequeueOutputBuffer();
        if (dequeueOutputBuffer != null) {
            return dequeueOutputBuffer;
        }
        if (!shouldClearStuckCaptions() || (availableOutputBuffer = getAvailableOutputBuffer()) == null) {
            return null;
        }
        this.cues = Collections.emptyList();
        this.lastCueUpdateUs = -9223372036854775807L;
        availableOutputBuffer.setContent(getPositionUs(), createSubtitle(), Long.MAX_VALUE);
        return availableOutputBuffer;
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        setCaptionMode(0);
        setCaptionRowCount(4);
        resetCueBuilders();
        this.isCaptionValid = false;
        this.repeatableControlSet = false;
        this.repeatableControlCc1 = (byte) 0;
        this.repeatableControlCc2 = (byte) 0;
        this.currentChannel = 0;
        this.isInCaptionService = true;
        this.lastCueUpdateUs = -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public String getName() {
        return "Cea608Decoder";
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
    public void release() {
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.text.SubtitleDecoder
    public /* bridge */ /* synthetic */ void setPositionUs(long j) {
        super.setPositionUs(j);
    }
}
