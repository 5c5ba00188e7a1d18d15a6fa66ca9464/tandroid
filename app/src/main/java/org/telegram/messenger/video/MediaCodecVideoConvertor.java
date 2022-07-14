package org.telegram.messenger.video;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import com.google.android.exoplayer2.util.MimeTypes;
import com.microsoft.appcenter.distribute.DistributeConstants;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
/* loaded from: classes4.dex */
public class MediaCodecVideoConvertor {
    private static final int MEDIACODEC_TIMEOUT_DEFAULT = 2500;
    private static final int MEDIACODEC_TIMEOUT_INCREASED = 22000;
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private MediaController.VideoConvertorListener callback;
    private long endPresentationTime;
    private MediaExtractor extractor;
    private MP4Builder mediaMuxer;

    public boolean convertVideo(String videoPath, File cacheFile, int rotationValue, boolean isSecret, int originalWidth, int originalHeight, int resultWidth, int resultHeight, int framerate, int bitrate, int originalBitrate, long startTime, long endTime, long avatarStartTime, boolean needCompress, long duration, MediaController.SavedFilterState savedFilterState, String paintPath, ArrayList<VideoEditedInfo.MediaEntity> mediaEntities, boolean isPhoto, MediaController.CropState cropState, boolean isRound, MediaController.VideoConvertorListener callback) {
        this.callback = callback;
        return convertVideoInternal(videoPath, cacheFile, rotationValue, isSecret, originalWidth, originalHeight, resultWidth, resultHeight, framerate, bitrate, originalBitrate, startTime, endTime, avatarStartTime, duration, needCompress, false, savedFilterState, paintPath, mediaEntities, isPhoto, cropState, isRound);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }

    /* JADX WARN: Code restructure failed: missing block: B:553:0x0eeb, code lost:
        r8 = r99;
        r13 = r101;
        r24 = r102;
        r26 = r1;
        r35 = r2;
        r1 = r1;
        r66 = r10;
        r37 = r14;
        r18 = r21;
        r6 = r62;
        r25 = r78;
        r7 = r100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x025d, code lost:
        r18 = r6;
        r42 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:952:0x182c, code lost:
        r13 = r101;
        r55 = r5;
        r4 = r78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:953:0x184e, code lost:
        throw new java.lang.RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + r3);
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 62, insn: 0x1b50: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r62 I:??[OBJECT, ARRAY]), block:B:1024:0x1b43 */
    /* JADX WARN: Removed duplicated region for block: B:1029:0x1b69 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1041:0x1baf A[Catch: all -> 0x1be9, TryCatch #128 {all -> 0x1be9, blocks: (B:1039:0x1ba6, B:1041:0x1baf, B:1053:0x1be5, B:1057:0x1bf5, B:1059:0x1bfa, B:1061:0x1c02, B:1062:0x1c05), top: B:1246:0x1ba6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1053:0x1be5 A[Catch: all -> 0x1be9, TryCatch #128 {all -> 0x1be9, blocks: (B:1039:0x1ba6, B:1041:0x1baf, B:1053:0x1be5, B:1057:0x1bf5, B:1059:0x1bfa, B:1061:0x1c02, B:1062:0x1c05), top: B:1246:0x1ba6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1057:0x1bf5 A[Catch: all -> 0x1be9, TryCatch #128 {all -> 0x1be9, blocks: (B:1039:0x1ba6, B:1041:0x1baf, B:1053:0x1be5, B:1057:0x1bf5, B:1059:0x1bfa, B:1061:0x1c02, B:1062:0x1c05), top: B:1246:0x1ba6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1059:0x1bfa A[Catch: all -> 0x1be9, TryCatch #128 {all -> 0x1be9, blocks: (B:1039:0x1ba6, B:1041:0x1baf, B:1053:0x1be5, B:1057:0x1bf5, B:1059:0x1bfa, B:1061:0x1c02, B:1062:0x1c05), top: B:1246:0x1ba6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1061:0x1c02 A[Catch: all -> 0x1be9, TryCatch #128 {all -> 0x1be9, blocks: (B:1039:0x1ba6, B:1041:0x1baf, B:1053:0x1be5, B:1057:0x1bf5, B:1059:0x1bfa, B:1061:0x1c02, B:1062:0x1c05), top: B:1246:0x1ba6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1066:0x1c0e  */
    /* JADX WARN: Removed duplicated region for block: B:1084:0x1c82  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x1ca5  */
    /* JADX WARN: Removed duplicated region for block: B:1094:0x1cdc  */
    /* JADX WARN: Removed duplicated region for block: B:1109:0x1c15 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1200:0x1c89 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1233:0x0d03 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1234:0x0c89 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1306:0x0cb0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1350:0x1571 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1352:0x1553 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0594  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x07fa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0836 A[Catch: all -> 0x0856, TryCatch #42 {all -> 0x0856, blocks: (B:288:0x0814, B:290:0x0836, B:292:0x083b, B:294:0x0840, B:295:0x0846), top: B:1155:0x0814 }] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x083b A[Catch: all -> 0x0856, TryCatch #42 {all -> 0x0856, blocks: (B:288:0x0814, B:290:0x0836, B:292:0x083b, B:294:0x0840, B:295:0x0846), top: B:1155:0x0814 }] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0840 A[Catch: all -> 0x0856, TryCatch #42 {all -> 0x0856, blocks: (B:288:0x0814, B:290:0x0836, B:292:0x083b, B:294:0x0840, B:295:0x0846), top: B:1155:0x0814 }] */
    /* JADX WARN: Removed duplicated region for block: B:390:0x0a64  */
    /* JADX WARN: Removed duplicated region for block: B:401:0x0aa2  */
    /* JADX WARN: Removed duplicated region for block: B:404:0x0ab1  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x0ae0  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x0b05 A[Catch: all -> 0x0a83, Exception -> 0x0ad0, TRY_ENTER, TRY_LEAVE, TryCatch #64 {Exception -> 0x0ad0, blocks: (B:405:0x0ab3, B:412:0x0b05), top: B:1188:0x0ab3 }] */
    /* JADX WARN: Removed duplicated region for block: B:436:0x0bb3  */
    /* JADX WARN: Removed duplicated region for block: B:457:0x0c68  */
    /* JADX WARN: Removed duplicated region for block: B:481:0x0cfe  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x0d00  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x0e7a  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0ebf  */
    /* JADX WARN: Removed duplicated region for block: B:545:0x0ed5  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x0ed7  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0ee6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:556:0x0f0b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:565:0x0f47  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x1006  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x102b A[Catch: all -> 0x0f16, Exception -> 0x1010, TRY_ENTER, TRY_LEAVE, TryCatch #24 {Exception -> 0x1010, blocks: (B:601:0x1007, B:606:0x102b, B:613:0x1048), top: B:1131:0x1007 }] */
    /* JADX WARN: Removed duplicated region for block: B:607:0x1039  */
    /* JADX WARN: Removed duplicated region for block: B:611:0x1042  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x10f3  */
    /* JADX WARN: Removed duplicated region for block: B:666:0x11cc  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x11ed A[ADDED_TO_REGION, EDGE_INSN: B:669:0x11ed->B:1351:0x11f0 ?: BREAK  ] */
    /* JADX WARN: Removed duplicated region for block: B:673:0x120f  */
    /* JADX WARN: Removed duplicated region for block: B:674:0x1217  */
    /* JADX WARN: Removed duplicated region for block: B:679:0x1227  */
    /* JADX WARN: Removed duplicated region for block: B:680:0x1241  */
    /* JADX WARN: Removed duplicated region for block: B:813:0x1541  */
    /* JADX WARN: Removed duplicated region for block: B:814:0x1543  */
    /* JADX WARN: Removed duplicated region for block: B:892:0x1713 A[Catch: Exception -> 0x1723, all -> 0x184f, TRY_ENTER, TryCatch #35 {all -> 0x184f, blocks: (B:870:0x167c, B:892:0x1713, B:894:0x171b, B:909:0x1753, B:911:0x1758, B:913:0x177c, B:915:0x1789, B:923:0x17a1, B:924:0x17a7, B:926:0x17ac, B:929:0x17b6, B:933:0x17c3, B:936:0x17ca, B:938:0x17cf, B:940:0x17df, B:943:0x17f9, B:945:0x17ff, B:947:0x1804, B:948:0x1809, B:952:0x182c, B:953:0x184e), top: B:1144:0x167c }] */
    /* JADX WARN: Removed duplicated region for block: B:901:0x173c  */
    /* JADX WARN: Removed duplicated region for block: B:902:0x173f  */
    /* JADX WARN: Removed duplicated region for block: B:905:0x1749  */
    /* JADX WARN: Removed duplicated region for block: B:918:0x1790  */
    /* JADX WARN: Removed duplicated region for block: B:919:0x1795  */
    /* JADX WARN: Removed duplicated region for block: B:926:0x17ac A[Catch: Exception -> 0x1817, all -> 0x184f, TRY_LEAVE, TryCatch #30 {Exception -> 0x1817, blocks: (B:924:0x17a7, B:926:0x17ac, B:936:0x17ca, B:938:0x17cf), top: B:1140:0x17a7 }] */
    /* JADX WARN: Removed duplicated region for block: B:942:0x17ee  */
    /* JADX WARN: Removed duplicated region for block: B:945:0x17ff A[Catch: all -> 0x184f, Exception -> 0x185d, TryCatch #35 {all -> 0x184f, blocks: (B:870:0x167c, B:892:0x1713, B:894:0x171b, B:909:0x1753, B:911:0x1758, B:913:0x177c, B:915:0x1789, B:923:0x17a1, B:924:0x17a7, B:926:0x17ac, B:929:0x17b6, B:933:0x17c3, B:936:0x17ca, B:938:0x17cf, B:940:0x17df, B:943:0x17f9, B:945:0x17ff, B:947:0x1804, B:948:0x1809, B:952:0x182c, B:953:0x184e), top: B:1144:0x167c }] */
    /* JADX WARN: Removed duplicated region for block: B:949:0x1811  */
    /* JADX WARN: Type inference failed for: r10v46 */
    /* JADX WARN: Type inference failed for: r10v50 */
    /* JADX WARN: Type inference failed for: r10v51 */
    /* JADX WARN: Type inference failed for: r10v52 */
    /* JADX WARN: Type inference failed for: r10v53 */
    /* JADX WARN: Type inference failed for: r10v63 */
    /* JADX WARN: Type inference failed for: r10v64 */
    /* JADX WARN: Type inference failed for: r10v67 */
    /* JADX WARN: Type inference failed for: r10v69 */
    /* JADX WARN: Type inference failed for: r10v70 */
    /* JADX WARN: Type inference failed for: r13v194 */
    /* JADX WARN: Type inference failed for: r25v34 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean convertVideoInternal(String videoPath, File cacheFile, int rotationValue, boolean isSecret, int originalWidth, int originalHeight, int resultWidth, int resultHeight, int framerate, int bitrate, int originalBitrate, long startTime, long endTime, long avatarStartTime, long duration, boolean needCompress, boolean increaseTimeout, MediaController.SavedFilterState savedFilterState, String paintPath, ArrayList<VideoEditedInfo.MediaEntity> mediaEntities, boolean isPhoto, MediaController.CropState cropState, boolean isRound) {
        long endTime2;
        long avatarStartTime2;
        boolean repeatWithIncreasedTimeout;
        boolean error;
        int resultWidth2;
        int resultWidth3;
        int bitrate2;
        int resultWidth4;
        int resultHeight2;
        String str;
        int videoTrackIndex;
        Throwable e;
        MediaExtractor mediaExtractor;
        MP4Builder mP4Builder;
        int i;
        int i2;
        Throwable th;
        int bitrate3;
        int resultWidth5;
        int bitrate4;
        int videoTrackIndex2;
        MediaExtractor mediaExtractor2;
        MP4Builder mP4Builder2;
        int findTrack;
        boolean needConvertVideo;
        String str2;
        int audioIndex;
        String str3;
        long currentPts;
        MediaCodec.BufferInfo info;
        int videoIndex;
        int i3;
        int i4;
        int videoIndex2;
        int i5;
        Exception e2;
        int bitrate5;
        Throwable th2;
        long frameDeltaFroSkipFrames;
        long avatarStartTime3;
        long avatarStartTime4;
        int w;
        int w2;
        MediaCodec decoder;
        MediaCodec encoder;
        InputSurface inputSurface;
        InputSurface inputSurface2;
        MediaCodec encoder2;
        MediaCodec.BufferInfo info2;
        OutputSurface outputSurface;
        int i6;
        int i7;
        int i8;
        ByteBuffer[] decoderInputBuffers;
        int audioIndex2;
        ByteBuffer[] encoderOutputBuffers;
        boolean z;
        int audioIndex3;
        long j;
        long j2;
        int audioIndex4;
        int maxBufferSize;
        AudioRecoder audioRecoder;
        int audioIndex5;
        Exception e3;
        boolean audioEncoderDone;
        ByteBuffer[] encoderOutputBuffers2;
        int videoTrackIndex3;
        int maxBufferSize2;
        long endTime3;
        long j3;
        int i9;
        Throwable th3;
        boolean audioEncoderDone2;
        AudioRecoder audioRecoder2;
        boolean audioEncoderDone3;
        long endTime4;
        int audioIndex6;
        int audioTrackIndex;
        ByteBuffer[] decoderInputBuffers2;
        MediaCodec.BufferInfo info3;
        long endTime5;
        boolean decoderStatus;
        ByteBuffer[] encoderOutputBuffers3;
        long minPresentationTime;
        int i10;
        Throwable th4;
        boolean encoderOutputAvailable;
        boolean decoderOutputAvailable;
        long j4;
        MediaCodec encoder3;
        int encoderStatus;
        OutputSurface outputSurface2;
        String str4;
        long minPresentationTime2;
        ByteBuffer[] encoderOutputBuffers4;
        String str5;
        String str6;
        int h;
        int w3;
        String str7;
        int encoderStatus2;
        long minPresentationTime3;
        int videoTrackIndex4;
        InputSurface inputSurface3;
        long minPresentationTime4;
        int i11;
        Throwable th5;
        Exception e4;
        long j5;
        boolean doRender;
        boolean flushed;
        boolean flushed2;
        boolean doRender2;
        long trueStartTime;
        long minPresentationTime5;
        int i12;
        int encoderStatus3;
        int i13;
        MediaFormat newFormat;
        boolean firstEncode;
        int h2;
        String str8;
        String str9;
        String str10;
        ByteBuffer audioBuffer;
        long currentPts2;
        String str11;
        int bitrate6;
        Exception e5;
        String str12;
        int i14;
        int resultHeight3;
        Exception e6;
        Object obj;
        boolean decoderOutputAvailable2;
        int framesCount;
        String str13;
        String str14;
        long currentPts3;
        String str15;
        ByteBuffer[] encoderInputBuffers;
        int encoderStatus4;
        boolean firstEncode2;
        ByteBuffer[] encoderOutputBuffers5;
        boolean encoderOutputAvailable2;
        InputSurface inputSurface4;
        int encoderStatus5;
        boolean firstEncode3;
        int encoderStatus6;
        boolean encoderOutputAvailable3;
        ByteBuffer sps;
        ByteBuffer sps2;
        boolean firstEncode4;
        byte[] temp;
        String str16;
        MediaCodecVideoConvertor mediaCodecVideoConvertor = this;
        int resultHeight4 = resultHeight;
        int bitrate7 = bitrate;
        long time = System.currentTimeMillis();
        boolean error2 = false;
        boolean repeatWithIncreasedTimeout2 = false;
        int videoTrackIndex5 = -5;
        try {
            MediaCodec.BufferInfo info4 = new MediaCodec.BufferInfo();
            Mp4Movie movie = new Mp4Movie();
            try {
                movie.setCacheFile(cacheFile);
                movie.setRotation(0);
                movie.setSize(resultWidth, resultHeight4);
                mediaCodecVideoConvertor.mediaMuxer = new MP4Builder().createMovie(movie, isSecret);
                float durationS = ((float) duration) / 1000.0f;
                MediaCodec encoder4 = null;
                InputSurface inputSurface5 = null;
                OutputSurface outputSurface3 = null;
                int prependHeaderSize = 0;
                mediaCodecVideoConvertor.endPresentationTime = duration * 1000;
                checkConversionCanceled();
                if (isPhoto) {
                    boolean outputDone = false;
                    boolean decoderDone = false;
                    int framesCount2 = 0;
                    if (avatarStartTime >= 0) {
                        bitrate7 = durationS <= 2000.0f ? 2600000 : durationS <= 5000.0f ? 2200000 : 1560000;
                    } else if (bitrate7 <= 0) {
                        bitrate7 = 921600;
                    }
                    try {
                        if (resultWidth % 16 != 0) {
                            try {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("changing width from " + resultWidth + " to " + (Math.round(resultWidth / 16.0f) * 16));
                                }
                                resultWidth4 = Math.round(resultWidth / 16.0f) * 16;
                            } catch (Exception e7) {
                                mediaCodecVideoConvertor = this;
                                e5 = e7;
                                bitrate6 = bitrate7;
                                resultWidth4 = resultWidth;
                                str11 = "bitrate: ";
                                try {
                                    if (e5 instanceof IllegalStateException) {
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    str12 = str11;
                                    try {
                                        sb.append(str12);
                                        bitrate7 = bitrate6;
                                        try {
                                            sb.append(bitrate7);
                                            sb.append(" framerate: ");
                                            i14 = framerate;
                                            try {
                                                sb.append(i14);
                                                sb.append(" size: ");
                                                sb.append(resultHeight4);
                                                sb.append("x");
                                                sb.append(resultWidth4);
                                                FileLog.e(sb.toString());
                                                FileLog.e(e5);
                                                error2 = true;
                                                if (outputSurface3 != null) {
                                                }
                                                if (inputSurface5 != null) {
                                                }
                                                if (encoder4 != null) {
                                                }
                                                checkConversionCanceled();
                                                endTime2 = endTime;
                                                avatarStartTime2 = avatarStartTime;
                                                bitrate3 = bitrate7;
                                                bitrate4 = resultHeight4;
                                                resultWidth5 = resultWidth4;
                                                videoTrackIndex2 = videoTrackIndex5;
                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                resultWidth2 = bitrate4;
                                                resultWidth3 = resultWidth5;
                                                error = error2;
                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                bitrate2 = bitrate3;
                                            } catch (Throwable th6) {
                                                endTime2 = endTime;
                                                avatarStartTime2 = avatarStartTime;
                                                e = th6;
                                                resultHeight2 = i14;
                                                str = str12;
                                                videoTrackIndex = videoTrackIndex5;
                                                try {
                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                    FileLog.e(e);
                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    bitrate2 = bitrate7;
                                                    resultWidth3 = resultWidth4;
                                                    error = true;
                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                    resultWidth2 = resultHeight4;
                                                    if (repeatWithIncreasedTimeout) {
                                                    }
                                                } catch (Throwable th7) {
                                                    MediaExtractor mediaExtractor3 = this.extractor;
                                                    if (mediaExtractor3 != null) {
                                                        mediaExtractor3.release();
                                                    }
                                                    MP4Builder mP4Builder3 = this.mediaMuxer;
                                                    if (mP4Builder3 != null) {
                                                        try {
                                                            mP4Builder3.finishMovie();
                                                            this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(videoTrackIndex);
                                                        } catch (Throwable e8) {
                                                            FileLog.e(e8);
                                                        }
                                                    }
                                                    throw th7;
                                                }
                                            }
                                        } catch (Throwable th8) {
                                            resultHeight2 = framerate;
                                            endTime2 = endTime;
                                            avatarStartTime2 = avatarStartTime;
                                            e = th8;
                                            str = str12;
                                            videoTrackIndex = videoTrackIndex5;
                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                            FileLog.e(e);
                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            bitrate2 = bitrate7;
                                            resultWidth3 = resultWidth4;
                                            error = true;
                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                            resultWidth2 = resultHeight4;
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        }
                                    } catch (Throwable th9) {
                                        bitrate7 = bitrate6;
                                        resultHeight2 = framerate;
                                        endTime2 = endTime;
                                        avatarStartTime2 = avatarStartTime;
                                        e = th9;
                                        str = str12;
                                        videoTrackIndex = videoTrackIndex5;
                                    }
                                } catch (Throwable th10) {
                                    bitrate7 = bitrate6;
                                    resultHeight2 = framerate;
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    e = th10;
                                    videoTrackIndex = videoTrackIndex5;
                                    str = str11;
                                }
                                if (repeatWithIncreasedTimeout) {
                                }
                            } catch (Throwable th11) {
                                mediaCodecVideoConvertor = this;
                                endTime2 = endTime;
                                avatarStartTime2 = avatarStartTime;
                                e = th11;
                                resultHeight2 = framerate;
                                resultWidth4 = resultWidth;
                                videoTrackIndex = -5;
                                str = "bitrate: ";
                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                FileLog.e(e);
                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                bitrate2 = bitrate7;
                                resultWidth3 = resultWidth4;
                                error = true;
                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                resultWidth2 = resultHeight4;
                                if (repeatWithIncreasedTimeout) {
                                }
                            }
                        } else {
                            resultWidth4 = resultWidth;
                        }
                        try {
                            if (resultHeight4 % 16 != 0) {
                                try {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("changing height from " + resultHeight4 + " to " + (Math.round(resultHeight4 / 16.0f) * 16));
                                    }
                                    resultHeight3 = Math.round(resultHeight4 / 16.0f) * 16;
                                } catch (Exception e9) {
                                    mediaCodecVideoConvertor = this;
                                    e5 = e9;
                                    bitrate6 = bitrate7;
                                    str11 = "bitrate: ";
                                    if (e5 instanceof IllegalStateException) {
                                    }
                                    StringBuilder sb2 = new StringBuilder();
                                    str12 = str11;
                                    sb2.append(str12);
                                    bitrate7 = bitrate6;
                                    sb2.append(bitrate7);
                                    sb2.append(" framerate: ");
                                    i14 = framerate;
                                    sb2.append(i14);
                                    sb2.append(" size: ");
                                    sb2.append(resultHeight4);
                                    sb2.append("x");
                                    sb2.append(resultWidth4);
                                    FileLog.e(sb2.toString());
                                    FileLog.e(e5);
                                    error2 = true;
                                    if (outputSurface3 != null) {
                                    }
                                    if (inputSurface5 != null) {
                                    }
                                    if (encoder4 != null) {
                                    }
                                    checkConversionCanceled();
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    bitrate3 = bitrate7;
                                    bitrate4 = resultHeight4;
                                    resultWidth5 = resultWidth4;
                                    videoTrackIndex2 = videoTrackIndex5;
                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                    if (mediaExtractor2 != null) {
                                    }
                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                    if (mP4Builder2 != null) {
                                    }
                                    resultWidth2 = bitrate4;
                                    resultWidth3 = resultWidth5;
                                    error = error2;
                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                    bitrate2 = bitrate3;
                                    if (repeatWithIncreasedTimeout) {
                                    }
                                } catch (Throwable th12) {
                                    mediaCodecVideoConvertor = this;
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    e = th12;
                                    resultHeight2 = framerate;
                                    videoTrackIndex = -5;
                                    str = "bitrate: ";
                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                    FileLog.e(e);
                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                    if (mediaExtractor != null) {
                                    }
                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                    if (mP4Builder != null) {
                                    }
                                    bitrate2 = bitrate7;
                                    resultWidth3 = resultWidth4;
                                    error = true;
                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                    resultWidth2 = resultHeight4;
                                    if (repeatWithIncreasedTimeout) {
                                    }
                                }
                            } else {
                                resultHeight3 = resultHeight4;
                            }
                            try {
                                try {
                                    if (BuildVars.LOGS_ENABLED) {
                                        try {
                                            FileLog.d("create photo encoder " + resultWidth4 + " " + resultHeight3 + " duration = " + duration);
                                        } catch (Exception e10) {
                                            mediaCodecVideoConvertor = this;
                                            e5 = e10;
                                            bitrate6 = bitrate7;
                                            resultHeight4 = resultHeight3;
                                            str11 = "bitrate: ";
                                            if (e5 instanceof IllegalStateException) {
                                            }
                                            StringBuilder sb22 = new StringBuilder();
                                            str12 = str11;
                                            sb22.append(str12);
                                            bitrate7 = bitrate6;
                                            sb22.append(bitrate7);
                                            sb22.append(" framerate: ");
                                            i14 = framerate;
                                            sb22.append(i14);
                                            sb22.append(" size: ");
                                            sb22.append(resultHeight4);
                                            sb22.append("x");
                                            sb22.append(resultWidth4);
                                            FileLog.e(sb22.toString());
                                            FileLog.e(e5);
                                            error2 = true;
                                            if (outputSurface3 != null) {
                                            }
                                            if (inputSurface5 != null) {
                                            }
                                            if (encoder4 != null) {
                                            }
                                            checkConversionCanceled();
                                            endTime2 = endTime;
                                            avatarStartTime2 = avatarStartTime;
                                            bitrate3 = bitrate7;
                                            bitrate4 = resultHeight4;
                                            resultWidth5 = resultWidth4;
                                            videoTrackIndex2 = videoTrackIndex5;
                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                            if (mediaExtractor2 != null) {
                                            }
                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                            if (mP4Builder2 != null) {
                                            }
                                            resultWidth2 = bitrate4;
                                            resultWidth3 = resultWidth5;
                                            error = error2;
                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                            bitrate2 = bitrate3;
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        } catch (Throwable th13) {
                                            mediaCodecVideoConvertor = this;
                                            endTime2 = endTime;
                                            avatarStartTime2 = avatarStartTime;
                                            e = th13;
                                            resultHeight4 = resultHeight3;
                                            videoTrackIndex = -5;
                                            str = "bitrate: ";
                                            resultHeight2 = framerate;
                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                            FileLog.e(e);
                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            bitrate2 = bitrate7;
                                            resultWidth3 = resultWidth4;
                                            error = true;
                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                            resultWidth2 = resultHeight4;
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        }
                                    }
                                    MediaFormat outputFormat = MediaFormat.createVideoFormat("video/avc", resultWidth4, resultHeight3);
                                    outputFormat.setInteger("color-format", 2130708361);
                                    outputFormat.setInteger("bitrate", bitrate7);
                                    outputFormat.setInteger("frame-rate", 30);
                                    outputFormat.setInteger("i-frame-interval", 1);
                                    MediaCodec encoder5 = MediaCodec.createEncoderByType("video/avc");
                                    try {
                                        encoder5.configure(outputFormat, (Surface) null, (MediaCrypto) null, 1);
                                        InputSurface inputSurface6 = new InputSurface(encoder5.createInputSurface());
                                        try {
                                            inputSurface6.makeCurrent();
                                            encoder5.start();
                                            String str17 = "csd-0";
                                            String str18 = "video/avc";
                                            InputSurface inputSurface7 = inputSurface6;
                                            String str19 = "csd-1";
                                            bitrate6 = bitrate7;
                                            MediaCodec encoder6 = encoder5;
                                            int resultHeight5 = resultHeight3;
                                            str11 = "bitrate: ";
                                            long currentPts4 = 0;
                                            String str20 = "prepend-sps-pps-to-idr-frames";
                                            int i15 = 21;
                                            try {
                                                outputSurface3 = new OutputSurface(savedFilterState, videoPath, paintPath, mediaEntities, null, resultWidth4, resultHeight3, originalWidth, originalHeight, rotationValue, framerate, true);
                                                ByteBuffer[] encoderOutputBuffers6 = null;
                                                Object obj2 = null;
                                                if (Build.VERSION.SDK_INT < 21) {
                                                    try {
                                                        encoderOutputBuffers6 = encoder6.getOutputBuffers();
                                                    } catch (Exception e11) {
                                                        mediaCodecVideoConvertor = this;
                                                        encoder4 = encoder6;
                                                        e5 = e11;
                                                        inputSurface5 = inputSurface7;
                                                        resultHeight4 = resultHeight5;
                                                        if (e5 instanceof IllegalStateException) {
                                                        }
                                                        StringBuilder sb222 = new StringBuilder();
                                                        str12 = str11;
                                                        sb222.append(str12);
                                                        bitrate7 = bitrate6;
                                                        sb222.append(bitrate7);
                                                        sb222.append(" framerate: ");
                                                        i14 = framerate;
                                                        sb222.append(i14);
                                                        sb222.append(" size: ");
                                                        sb222.append(resultHeight4);
                                                        sb222.append("x");
                                                        sb222.append(resultWidth4);
                                                        FileLog.e(sb222.toString());
                                                        FileLog.e(e5);
                                                        error2 = true;
                                                        if (outputSurface3 != null) {
                                                        }
                                                        if (inputSurface5 != null) {
                                                        }
                                                        if (encoder4 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        endTime2 = endTime;
                                                        avatarStartTime2 = avatarStartTime;
                                                        bitrate3 = bitrate7;
                                                        bitrate4 = resultHeight4;
                                                        resultWidth5 = resultWidth4;
                                                        videoTrackIndex2 = videoTrackIndex5;
                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        resultWidth2 = bitrate4;
                                                        resultWidth3 = resultWidth5;
                                                        error = error2;
                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                        bitrate2 = bitrate3;
                                                        if (repeatWithIncreasedTimeout) {
                                                        }
                                                    } catch (Throwable th14) {
                                                        mediaCodecVideoConvertor = this;
                                                        resultHeight2 = framerate;
                                                        endTime2 = endTime;
                                                        avatarStartTime2 = avatarStartTime;
                                                        e = th14;
                                                        videoTrackIndex = -5;
                                                        bitrate7 = bitrate6;
                                                        resultHeight4 = resultHeight5;
                                                        str = str11;
                                                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                        FileLog.e(e);
                                                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                        if (mediaExtractor != null) {
                                                        }
                                                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                        if (mP4Builder != null) {
                                                        }
                                                        bitrate2 = bitrate7;
                                                        resultWidth3 = resultWidth4;
                                                        error = true;
                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                        resultWidth2 = resultHeight4;
                                                        if (repeatWithIncreasedTimeout) {
                                                        }
                                                    }
                                                }
                                                boolean firstEncode5 = true;
                                                checkConversionCanceled();
                                                while (!outputDone) {
                                                    checkConversionCanceled();
                                                    boolean decoderOutputAvailable3 = !decoderDone;
                                                    boolean encoderOutputAvailable4 = true;
                                                    int videoTrackIndex6 = videoTrackIndex5;
                                                    int framesCount3 = framesCount2;
                                                    while (true) {
                                                        if (decoderOutputAvailable3 || encoderOutputAvailable4) {
                                                            try {
                                                                try {
                                                                    checkConversionCanceled();
                                                                    MediaCodec encoder7 = encoder6;
                                                                    MediaCodec.BufferInfo info5 = info4;
                                                                    try {
                                                                        int encoderStatus7 = encoder7.dequeueOutputBuffer(info5, increaseTimeout ? 22000L : 2500L);
                                                                        if (encoderStatus7 == -1) {
                                                                            encoderOutputAvailable4 = false;
                                                                            mediaCodecVideoConvertor = this;
                                                                            obj = obj2;
                                                                            decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                            framesCount = framesCount3;
                                                                            str13 = str17;
                                                                            str15 = str18;
                                                                            str14 = str19;
                                                                            encoderInputBuffers = encoderOutputBuffers6;
                                                                            encoderStatus4 = encoderStatus7;
                                                                            currentPts3 = currentPts4;
                                                                        } else if (encoderStatus7 == -3) {
                                                                            try {
                                                                                if (Build.VERSION.SDK_INT < i15) {
                                                                                    ByteBuffer[] encoderOutputBuffers7 = encoder7.getOutputBuffers();
                                                                                    mediaCodecVideoConvertor = this;
                                                                                    obj = obj2;
                                                                                    decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                                    framesCount = framesCount3;
                                                                                    str13 = str17;
                                                                                    str15 = str18;
                                                                                    str14 = str19;
                                                                                    encoderInputBuffers = encoderOutputBuffers7;
                                                                                    encoderStatus4 = encoderStatus7;
                                                                                    currentPts3 = currentPts4;
                                                                                } else {
                                                                                    mediaCodecVideoConvertor = this;
                                                                                    obj = obj2;
                                                                                    decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                                    framesCount = framesCount3;
                                                                                    str13 = str17;
                                                                                    str15 = str18;
                                                                                    str14 = str19;
                                                                                    encoderInputBuffers = encoderOutputBuffers6;
                                                                                    encoderStatus4 = encoderStatus7;
                                                                                    currentPts3 = currentPts4;
                                                                                }
                                                                            } catch (Exception e12) {
                                                                                mediaCodecVideoConvertor = this;
                                                                                e5 = e12;
                                                                                videoTrackIndex5 = videoTrackIndex6;
                                                                                encoder4 = encoder7;
                                                                                inputSurface5 = inputSurface7;
                                                                                resultHeight4 = resultHeight5;
                                                                                if (e5 instanceof IllegalStateException) {
                                                                                }
                                                                                StringBuilder sb2222 = new StringBuilder();
                                                                                str12 = str11;
                                                                                sb2222.append(str12);
                                                                                bitrate7 = bitrate6;
                                                                                sb2222.append(bitrate7);
                                                                                sb2222.append(" framerate: ");
                                                                                i14 = framerate;
                                                                                sb2222.append(i14);
                                                                                sb2222.append(" size: ");
                                                                                sb2222.append(resultHeight4);
                                                                                sb2222.append("x");
                                                                                sb2222.append(resultWidth4);
                                                                                FileLog.e(sb2222.toString());
                                                                                FileLog.e(e5);
                                                                                error2 = true;
                                                                                if (outputSurface3 != null) {
                                                                                }
                                                                                if (inputSurface5 != null) {
                                                                                }
                                                                                if (encoder4 != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                endTime2 = endTime;
                                                                                avatarStartTime2 = avatarStartTime;
                                                                                bitrate3 = bitrate7;
                                                                                bitrate4 = resultHeight4;
                                                                                resultWidth5 = resultWidth4;
                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                if (mediaExtractor2 != null) {
                                                                                }
                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                if (mP4Builder2 != null) {
                                                                                }
                                                                                resultWidth2 = bitrate4;
                                                                                resultWidth3 = resultWidth5;
                                                                                error = error2;
                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                bitrate2 = bitrate3;
                                                                                if (repeatWithIncreasedTimeout) {
                                                                                }
                                                                            } catch (Throwable th15) {
                                                                                mediaCodecVideoConvertor = this;
                                                                                resultHeight2 = framerate;
                                                                                endTime2 = endTime;
                                                                                avatarStartTime2 = avatarStartTime;
                                                                                e = th15;
                                                                                videoTrackIndex = videoTrackIndex6;
                                                                                bitrate7 = bitrate6;
                                                                                resultHeight4 = resultHeight5;
                                                                                str = str11;
                                                                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                FileLog.e(e);
                                                                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                if (mediaExtractor != null) {
                                                                                }
                                                                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                if (mP4Builder != null) {
                                                                                }
                                                                                bitrate2 = bitrate7;
                                                                                resultWidth3 = resultWidth4;
                                                                                error = true;
                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                resultWidth2 = resultHeight4;
                                                                                if (repeatWithIncreasedTimeout) {
                                                                                }
                                                                            }
                                                                        } else if (encoderStatus7 == -2) {
                                                                            MediaFormat newFormat2 = encoder7.getOutputFormat();
                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                FileLog.d("photo encoder new format " + newFormat2);
                                                                            }
                                                                            if (videoTrackIndex6 != -5 || newFormat2 == null) {
                                                                                mediaCodecVideoConvertor = this;
                                                                                str13 = str17;
                                                                                str14 = str19;
                                                                            } else {
                                                                                mediaCodecVideoConvertor = this;
                                                                                try {
                                                                                    try {
                                                                                        videoTrackIndex5 = mediaCodecVideoConvertor.mediaMuxer.addTrack(newFormat2, false);
                                                                                        str16 = str20;
                                                                                    } catch (Throwable th16) {
                                                                                        resultHeight2 = framerate;
                                                                                        endTime2 = endTime;
                                                                                        avatarStartTime2 = avatarStartTime;
                                                                                        e = th16;
                                                                                        videoTrackIndex = videoTrackIndex6;
                                                                                        bitrate7 = bitrate6;
                                                                                        resultHeight4 = resultHeight5;
                                                                                        str = str11;
                                                                                    }
                                                                                } catch (Exception e13) {
                                                                                    e5 = e13;
                                                                                    videoTrackIndex5 = videoTrackIndex6;
                                                                                    encoder4 = encoder7;
                                                                                    inputSurface5 = inputSurface7;
                                                                                    resultHeight4 = resultHeight5;
                                                                                }
                                                                                try {
                                                                                    if (!newFormat2.containsKey(str16) || newFormat2.getInteger(str16) != 1) {
                                                                                        str20 = str16;
                                                                                        str13 = str17;
                                                                                        str14 = str19;
                                                                                        videoTrackIndex6 = videoTrackIndex5;
                                                                                    } else {
                                                                                        str13 = str17;
                                                                                        ByteBuffer spsBuff = newFormat2.getByteBuffer(str13);
                                                                                        str20 = str16;
                                                                                        str14 = str19;
                                                                                        ByteBuffer ppsBuff = newFormat2.getByteBuffer(str14);
                                                                                        prependHeaderSize = spsBuff.limit() + ppsBuff.limit();
                                                                                        videoTrackIndex6 = videoTrackIndex5;
                                                                                    }
                                                                                } catch (Exception e14) {
                                                                                    e5 = e14;
                                                                                    encoder4 = encoder7;
                                                                                    inputSurface5 = inputSurface7;
                                                                                    resultHeight4 = resultHeight5;
                                                                                    if (e5 instanceof IllegalStateException) {
                                                                                    }
                                                                                    StringBuilder sb22222 = new StringBuilder();
                                                                                    str12 = str11;
                                                                                    sb22222.append(str12);
                                                                                    bitrate7 = bitrate6;
                                                                                    sb22222.append(bitrate7);
                                                                                    sb22222.append(" framerate: ");
                                                                                    i14 = framerate;
                                                                                    sb22222.append(i14);
                                                                                    sb22222.append(" size: ");
                                                                                    sb22222.append(resultHeight4);
                                                                                    sb22222.append("x");
                                                                                    sb22222.append(resultWidth4);
                                                                                    FileLog.e(sb22222.toString());
                                                                                    FileLog.e(e5);
                                                                                    error2 = true;
                                                                                    if (outputSurface3 != null) {
                                                                                    }
                                                                                    if (inputSurface5 != null) {
                                                                                    }
                                                                                    if (encoder4 != null) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    endTime2 = endTime;
                                                                                    avatarStartTime2 = avatarStartTime;
                                                                                    bitrate3 = bitrate7;
                                                                                    bitrate4 = resultHeight4;
                                                                                    resultWidth5 = resultWidth4;
                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                    if (mediaExtractor2 != null) {
                                                                                    }
                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                    if (mP4Builder2 != null) {
                                                                                    }
                                                                                    resultWidth2 = bitrate4;
                                                                                    resultWidth3 = resultWidth5;
                                                                                    error = error2;
                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                    bitrate2 = bitrate3;
                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                    }
                                                                                } catch (Throwable th17) {
                                                                                    resultHeight2 = framerate;
                                                                                    endTime2 = endTime;
                                                                                    avatarStartTime2 = avatarStartTime;
                                                                                    e = th17;
                                                                                    videoTrackIndex = videoTrackIndex5;
                                                                                    bitrate7 = bitrate6;
                                                                                    resultHeight4 = resultHeight5;
                                                                                    str = str11;
                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                    FileLog.e(e);
                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                    if (mediaExtractor != null) {
                                                                                    }
                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                    if (mP4Builder != null) {
                                                                                    }
                                                                                    bitrate2 = bitrate7;
                                                                                    resultWidth3 = resultWidth4;
                                                                                    error = true;
                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                    resultWidth2 = resultHeight4;
                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                    }
                                                                                }
                                                                            }
                                                                            obj = obj2;
                                                                            decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                            framesCount = framesCount3;
                                                                            str15 = str18;
                                                                            encoderInputBuffers = encoderOutputBuffers6;
                                                                            encoderStatus4 = encoderStatus7;
                                                                            currentPts3 = currentPts4;
                                                                        } else {
                                                                            mediaCodecVideoConvertor = this;
                                                                            str13 = str17;
                                                                            str14 = str19;
                                                                            if (encoderStatus7 < 0) {
                                                                                throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus7);
                                                                            }
                                                                            try {
                                                                                try {
                                                                                    obj = obj2;
                                                                                    ByteBuffer encodedData = Build.VERSION.SDK_INT < 21 ? encoderOutputBuffers6[encoderStatus7] : encoder7.getOutputBuffer(encoderStatus7);
                                                                                    if (encodedData == null) {
                                                                                        throw new RuntimeException("encoderOutputBuffer " + encoderStatus7 + " was null");
                                                                                    }
                                                                                    ByteBuffer[] encoderOutputBuffers8 = encoderOutputBuffers6;
                                                                                    try {
                                                                                        if (info5.size > 1) {
                                                                                            try {
                                                                                                if ((info5.flags & 2) == 0) {
                                                                                                    if (prependHeaderSize != 0 && (info5.flags & 1) != 0) {
                                                                                                        info5.offset += prependHeaderSize;
                                                                                                        info5.size -= prependHeaderSize;
                                                                                                    }
                                                                                                    if (!firstEncode5 || (info5.flags & 1) == 0) {
                                                                                                        firstEncode4 = firstEncode5;
                                                                                                    } else {
                                                                                                        if (info5.size > 100) {
                                                                                                            encodedData.position(info5.offset);
                                                                                                            byte[] temp2 = new byte[100];
                                                                                                            encodedData.get(temp2);
                                                                                                            int nalCount = 0;
                                                                                                            int a = 0;
                                                                                                            while (true) {
                                                                                                                boolean firstEncode6 = firstEncode5;
                                                                                                                if (a >= temp2.length - 4) {
                                                                                                                    break;
                                                                                                                }
                                                                                                                if (temp2[a] == 0 && temp2[a + 1] == 0 && temp2[a + 2] == 0) {
                                                                                                                    temp = temp2;
                                                                                                                    if (temp2[a + 3] != 1) {
                                                                                                                        continue;
                                                                                                                    } else {
                                                                                                                        int nalCount2 = nalCount + 1;
                                                                                                                        if (nalCount2 > 1) {
                                                                                                                            info5.offset += a;
                                                                                                                            info5.size -= a;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        nalCount = nalCount2;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    temp = temp2;
                                                                                                                }
                                                                                                                a++;
                                                                                                                firstEncode5 = firstEncode6;
                                                                                                                temp2 = temp;
                                                                                                            }
                                                                                                        }
                                                                                                        firstEncode4 = false;
                                                                                                    }
                                                                                                    long availableSize = mediaCodecVideoConvertor.mediaMuxer.writeSampleData(videoTrackIndex6, encodedData, info5, true);
                                                                                                    boolean firstEncode7 = firstEncode4;
                                                                                                    boolean decoderOutputAvailable4 = decoderOutputAvailable3;
                                                                                                    int framesCount4 = framesCount3;
                                                                                                    encoderStatus6 = encoderStatus7;
                                                                                                    if (availableSize != 0) {
                                                                                                        MediaController.VideoConvertorListener videoConvertorListener = mediaCodecVideoConvertor.callback;
                                                                                                        if (videoConvertorListener != null) {
                                                                                                            decoderOutputAvailable2 = decoderOutputAvailable4;
                                                                                                            framesCount = framesCount4;
                                                                                                            currentPts3 = currentPts4;
                                                                                                            try {
                                                                                                                videoConvertorListener.didWriteData(availableSize, (((float) currentPts3) / 1000.0f) / durationS);
                                                                                                            } catch (Exception e15) {
                                                                                                                e5 = e15;
                                                                                                                videoTrackIndex5 = videoTrackIndex6;
                                                                                                                encoder4 = encoder7;
                                                                                                                inputSurface5 = inputSurface7;
                                                                                                                resultHeight4 = resultHeight5;
                                                                                                                if (e5 instanceof IllegalStateException) {
                                                                                                                }
                                                                                                                StringBuilder sb222222 = new StringBuilder();
                                                                                                                str12 = str11;
                                                                                                                sb222222.append(str12);
                                                                                                                bitrate7 = bitrate6;
                                                                                                                sb222222.append(bitrate7);
                                                                                                                sb222222.append(" framerate: ");
                                                                                                                i14 = framerate;
                                                                                                                sb222222.append(i14);
                                                                                                                sb222222.append(" size: ");
                                                                                                                sb222222.append(resultHeight4);
                                                                                                                sb222222.append("x");
                                                                                                                sb222222.append(resultWidth4);
                                                                                                                FileLog.e(sb222222.toString());
                                                                                                                FileLog.e(e5);
                                                                                                                error2 = true;
                                                                                                                if (outputSurface3 != null) {
                                                                                                                }
                                                                                                                if (inputSurface5 != null) {
                                                                                                                }
                                                                                                                if (encoder4 != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                endTime2 = endTime;
                                                                                                                avatarStartTime2 = avatarStartTime;
                                                                                                                bitrate3 = bitrate7;
                                                                                                                bitrate4 = resultHeight4;
                                                                                                                resultWidth5 = resultWidth4;
                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                resultWidth2 = bitrate4;
                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                error = error2;
                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                bitrate2 = bitrate3;
                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                }
                                                                                                            }
                                                                                                        } else {
                                                                                                            decoderOutputAvailable2 = decoderOutputAvailable4;
                                                                                                            framesCount = framesCount4;
                                                                                                            currentPts3 = currentPts4;
                                                                                                        }
                                                                                                    } else {
                                                                                                        decoderOutputAvailable2 = decoderOutputAvailable4;
                                                                                                        framesCount = framesCount4;
                                                                                                        currentPts3 = currentPts4;
                                                                                                    }
                                                                                                    firstEncode5 = firstEncode7;
                                                                                                    encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                                    str15 = str18;
                                                                                                } else {
                                                                                                    firstEncode3 = firstEncode5;
                                                                                                    decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                                                    framesCount = framesCount3;
                                                                                                    encoderStatus6 = encoderStatus7;
                                                                                                    currentPts3 = currentPts4;
                                                                                                    if (videoTrackIndex6 == -5) {
                                                                                                        byte[] csd = new byte[info5.size];
                                                                                                        encodedData.limit(info5.offset + info5.size);
                                                                                                        encodedData.position(info5.offset);
                                                                                                        encodedData.get(csd);
                                                                                                        ByteBuffer sps3 = null;
                                                                                                        ByteBuffer pps = null;
                                                                                                        byte b = 1;
                                                                                                        int a2 = info5.size - 1;
                                                                                                        while (true) {
                                                                                                            if (a2 < 0) {
                                                                                                                sps = sps3;
                                                                                                                encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                                                break;
                                                                                                            }
                                                                                                            sps = sps3;
                                                                                                            if (a2 <= 3) {
                                                                                                                encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                                                break;
                                                                                                            } else if (csd[a2] == b && csd[a2 - 1] == 0 && csd[a2 - 2] == 0 && csd[a2 - 3] == 0) {
                                                                                                                sps2 = ByteBuffer.allocate(a2 - 3);
                                                                                                                pps = ByteBuffer.allocate(info5.size - (a2 - 3));
                                                                                                                encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                                                sps2.put(csd, 0, a2 - 3).position(0);
                                                                                                                pps.put(csd, a2 - 3, info5.size - (a2 - 3)).position(0);
                                                                                                                break;
                                                                                                            } else {
                                                                                                                a2--;
                                                                                                                sps3 = sps;
                                                                                                                encoderOutputAvailable4 = encoderOutputAvailable4;
                                                                                                                b = 1;
                                                                                                            }
                                                                                                        }
                                                                                                        sps2 = sps;
                                                                                                        str15 = str18;
                                                                                                        int resultHeight6 = resultHeight5;
                                                                                                        try {
                                                                                                            MediaFormat newFormat3 = MediaFormat.createVideoFormat(str15, resultWidth4, resultHeight6);
                                                                                                            if (sps2 != null && pps != null) {
                                                                                                                try {
                                                                                                                    newFormat3.setByteBuffer(str13, sps2);
                                                                                                                    newFormat3.setByteBuffer(str14, pps);
                                                                                                                } catch (Exception e16) {
                                                                                                                    e5 = e16;
                                                                                                                    videoTrackIndex5 = videoTrackIndex6;
                                                                                                                    encoder4 = encoder7;
                                                                                                                    inputSurface5 = inputSurface7;
                                                                                                                    resultHeight4 = resultHeight6;
                                                                                                                    if (e5 instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb2222222 = new StringBuilder();
                                                                                                                    str12 = str11;
                                                                                                                    sb2222222.append(str12);
                                                                                                                    bitrate7 = bitrate6;
                                                                                                                    sb2222222.append(bitrate7);
                                                                                                                    sb2222222.append(" framerate: ");
                                                                                                                    i14 = framerate;
                                                                                                                    sb2222222.append(i14);
                                                                                                                    sb2222222.append(" size: ");
                                                                                                                    sb2222222.append(resultHeight4);
                                                                                                                    sb2222222.append("x");
                                                                                                                    sb2222222.append(resultWidth4);
                                                                                                                    FileLog.e(sb2222222.toString());
                                                                                                                    FileLog.e(e5);
                                                                                                                    error2 = true;
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface5 != null) {
                                                                                                                    }
                                                                                                                    if (encoder4 != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    endTime2 = endTime;
                                                                                                                    avatarStartTime2 = avatarStartTime;
                                                                                                                    bitrate3 = bitrate7;
                                                                                                                    bitrate4 = resultHeight4;
                                                                                                                    resultWidth5 = resultWidth4;
                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                    error = error2;
                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                    bitrate2 = bitrate3;
                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                    }
                                                                                                                } catch (Throwable th18) {
                                                                                                                    resultHeight2 = framerate;
                                                                                                                    endTime2 = endTime;
                                                                                                                    avatarStartTime2 = avatarStartTime;
                                                                                                                    e = th18;
                                                                                                                    resultHeight4 = resultHeight6;
                                                                                                                    videoTrackIndex = videoTrackIndex6;
                                                                                                                    bitrate7 = bitrate6;
                                                                                                                    str = str11;
                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                    FileLog.e(e);
                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    bitrate2 = bitrate7;
                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                    error = true;
                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            resultHeight5 = resultHeight6;
                                                                                                            firstEncode5 = firstEncode3;
                                                                                                            videoTrackIndex6 = mediaCodecVideoConvertor.mediaMuxer.addTrack(newFormat3, false);
                                                                                                        } catch (Exception e17) {
                                                                                                            e5 = e17;
                                                                                                            videoTrackIndex5 = videoTrackIndex6;
                                                                                                            encoder4 = encoder7;
                                                                                                            inputSurface5 = inputSurface7;
                                                                                                            resultHeight4 = resultHeight6;
                                                                                                        } catch (Throwable th19) {
                                                                                                            resultHeight2 = framerate;
                                                                                                            endTime2 = endTime;
                                                                                                            avatarStartTime2 = avatarStartTime;
                                                                                                            e = th19;
                                                                                                            videoTrackIndex = videoTrackIndex6;
                                                                                                            bitrate7 = bitrate6;
                                                                                                            resultHeight4 = resultHeight6;
                                                                                                            str = str11;
                                                                                                        }
                                                                                                    } else {
                                                                                                        encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                                        str15 = str18;
                                                                                                    }
                                                                                                }
                                                                                                outputDone = (info5.flags & 4) == 0;
                                                                                                encoderStatus4 = encoderStatus6;
                                                                                                encoder7.releaseOutputBuffer(encoderStatus4, false);
                                                                                                encoderInputBuffers = encoderOutputBuffers8;
                                                                                                encoderOutputAvailable4 = encoderOutputAvailable3;
                                                                                            } catch (Exception e18) {
                                                                                                e5 = e18;
                                                                                                videoTrackIndex5 = videoTrackIndex6;
                                                                                                encoder4 = encoder7;
                                                                                                inputSurface5 = inputSurface7;
                                                                                                resultHeight4 = resultHeight5;
                                                                                            }
                                                                                        } else {
                                                                                            firstEncode3 = firstEncode5;
                                                                                            decoderOutputAvailable2 = decoderOutputAvailable3;
                                                                                            encoderOutputAvailable3 = encoderOutputAvailable4;
                                                                                            framesCount = framesCount3;
                                                                                            encoderStatus6 = encoderStatus7;
                                                                                            str15 = str18;
                                                                                            currentPts3 = currentPts4;
                                                                                        }
                                                                                        outputDone = (info5.flags & 4) == 0;
                                                                                        encoderStatus4 = encoderStatus6;
                                                                                        encoder7.releaseOutputBuffer(encoderStatus4, false);
                                                                                        encoderInputBuffers = encoderOutputBuffers8;
                                                                                        encoderOutputAvailable4 = encoderOutputAvailable3;
                                                                                    } catch (Exception e19) {
                                                                                        e5 = e19;
                                                                                        inputSurface5 = inputSurface7;
                                                                                        videoTrackIndex5 = videoTrackIndex6;
                                                                                        encoder4 = encoder7;
                                                                                        resultHeight4 = resultHeight5;
                                                                                        if ((e5 instanceof IllegalStateException) && !increaseTimeout) {
                                                                                            repeatWithIncreasedTimeout2 = true;
                                                                                        }
                                                                                        StringBuilder sb22222222 = new StringBuilder();
                                                                                        str12 = str11;
                                                                                        sb22222222.append(str12);
                                                                                        bitrate7 = bitrate6;
                                                                                        sb22222222.append(bitrate7);
                                                                                        sb22222222.append(" framerate: ");
                                                                                        i14 = framerate;
                                                                                        sb22222222.append(i14);
                                                                                        sb22222222.append(" size: ");
                                                                                        sb22222222.append(resultHeight4);
                                                                                        sb22222222.append("x");
                                                                                        sb22222222.append(resultWidth4);
                                                                                        FileLog.e(sb22222222.toString());
                                                                                        FileLog.e(e5);
                                                                                        error2 = true;
                                                                                        if (outputSurface3 != null) {
                                                                                        }
                                                                                        if (inputSurface5 != null) {
                                                                                        }
                                                                                        if (encoder4 != null) {
                                                                                        }
                                                                                        checkConversionCanceled();
                                                                                        endTime2 = endTime;
                                                                                        avatarStartTime2 = avatarStartTime;
                                                                                        bitrate3 = bitrate7;
                                                                                        bitrate4 = resultHeight4;
                                                                                        resultWidth5 = resultWidth4;
                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                        if (mediaExtractor2 != null) {
                                                                                        }
                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                        if (mP4Builder2 != null) {
                                                                                        }
                                                                                        resultWidth2 = bitrate4;
                                                                                        resultWidth3 = resultWidth5;
                                                                                        error = error2;
                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                        bitrate2 = bitrate3;
                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                        }
                                                                                    }
                                                                                    firstEncode5 = firstEncode3;
                                                                                } catch (Throwable th20) {
                                                                                    resultHeight2 = framerate;
                                                                                    endTime2 = endTime;
                                                                                    avatarStartTime2 = avatarStartTime;
                                                                                    e = th20;
                                                                                    videoTrackIndex = videoTrackIndex6;
                                                                                    bitrate7 = bitrate6;
                                                                                    resultHeight4 = resultHeight5;
                                                                                    str = str11;
                                                                                }
                                                                            } catch (Exception e20) {
                                                                                e6 = e20;
                                                                                e5 = e6;
                                                                                inputSurface5 = inputSurface7;
                                                                                videoTrackIndex5 = videoTrackIndex6;
                                                                                encoder4 = encoder7;
                                                                                resultHeight4 = resultHeight5;
                                                                                if (e5 instanceof IllegalStateException) {
                                                                                }
                                                                                StringBuilder sb222222222 = new StringBuilder();
                                                                                str12 = str11;
                                                                                sb222222222.append(str12);
                                                                                bitrate7 = bitrate6;
                                                                                sb222222222.append(bitrate7);
                                                                                sb222222222.append(" framerate: ");
                                                                                i14 = framerate;
                                                                                sb222222222.append(i14);
                                                                                sb222222222.append(" size: ");
                                                                                sb222222222.append(resultHeight4);
                                                                                sb222222222.append("x");
                                                                                sb222222222.append(resultWidth4);
                                                                                FileLog.e(sb222222222.toString());
                                                                                FileLog.e(e5);
                                                                                error2 = true;
                                                                                if (outputSurface3 != null) {
                                                                                }
                                                                                if (inputSurface5 != null) {
                                                                                }
                                                                                if (encoder4 != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                endTime2 = endTime;
                                                                                avatarStartTime2 = avatarStartTime;
                                                                                bitrate3 = bitrate7;
                                                                                bitrate4 = resultHeight4;
                                                                                resultWidth5 = resultWidth4;
                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                if (mediaExtractor2 != null) {
                                                                                }
                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                if (mP4Builder2 != null) {
                                                                                }
                                                                                resultWidth2 = bitrate4;
                                                                                resultWidth3 = resultWidth5;
                                                                                error = error2;
                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                bitrate2 = bitrate3;
                                                                                if (repeatWithIncreasedTimeout) {
                                                                                }
                                                                            }
                                                                        }
                                                                        if (encoderStatus4 != -1) {
                                                                            encoderOutputBuffers6 = encoderInputBuffers;
                                                                            str18 = str15;
                                                                            currentPts4 = currentPts3;
                                                                            str19 = str14;
                                                                            str17 = str13;
                                                                            framesCount3 = framesCount;
                                                                            decoderOutputAvailable3 = decoderOutputAvailable2;
                                                                            i15 = 21;
                                                                            obj2 = obj;
                                                                            encoder6 = encoder7;
                                                                            info4 = info5;
                                                                        } else {
                                                                            if (!decoderDone) {
                                                                                outputSurface3.drawImage();
                                                                                encoderOutputBuffers5 = encoderInputBuffers;
                                                                                int framesCount5 = framesCount;
                                                                                firstEncode2 = firstEncode5;
                                                                                long presentationTime = (framesCount5 / 30.0f) * 1000.0f * 1000.0f * 1000.0f;
                                                                                encoderOutputAvailable2 = encoderOutputAvailable4;
                                                                                inputSurface4 = inputSurface7;
                                                                                try {
                                                                                    inputSurface4.setPresentationTime(presentationTime);
                                                                                    inputSurface4.swapBuffers();
                                                                                    int framesCount6 = framesCount5 + 1;
                                                                                    str19 = str14;
                                                                                    str17 = str13;
                                                                                    if (framesCount6 >= (((float) duration) / 1000.0f) * 30.0f) {
                                                                                        decoderDone = true;
                                                                                        try {
                                                                                            encoder7.signalEndOfInputStream();
                                                                                            encoderStatus5 = framesCount6;
                                                                                            decoderOutputAvailable3 = false;
                                                                                        } catch (Exception e21) {
                                                                                            e5 = e21;
                                                                                            inputSurface5 = inputSurface4;
                                                                                            videoTrackIndex5 = videoTrackIndex6;
                                                                                            encoder4 = encoder7;
                                                                                            resultHeight4 = resultHeight5;
                                                                                            if (e5 instanceof IllegalStateException) {
                                                                                                repeatWithIncreasedTimeout2 = true;
                                                                                            }
                                                                                            StringBuilder sb2222222222 = new StringBuilder();
                                                                                            str12 = str11;
                                                                                            sb2222222222.append(str12);
                                                                                            bitrate7 = bitrate6;
                                                                                            sb2222222222.append(bitrate7);
                                                                                            sb2222222222.append(" framerate: ");
                                                                                            i14 = framerate;
                                                                                            sb2222222222.append(i14);
                                                                                            sb2222222222.append(" size: ");
                                                                                            sb2222222222.append(resultHeight4);
                                                                                            sb2222222222.append("x");
                                                                                            sb2222222222.append(resultWidth4);
                                                                                            FileLog.e(sb2222222222.toString());
                                                                                            FileLog.e(e5);
                                                                                            error2 = true;
                                                                                            if (outputSurface3 != null) {
                                                                                            }
                                                                                            if (inputSurface5 != null) {
                                                                                            }
                                                                                            if (encoder4 != null) {
                                                                                            }
                                                                                            checkConversionCanceled();
                                                                                            endTime2 = endTime;
                                                                                            avatarStartTime2 = avatarStartTime;
                                                                                            bitrate3 = bitrate7;
                                                                                            bitrate4 = resultHeight4;
                                                                                            resultWidth5 = resultWidth4;
                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                            if (mediaExtractor2 != null) {
                                                                                            }
                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                            if (mP4Builder2 != null) {
                                                                                            }
                                                                                            resultWidth2 = bitrate4;
                                                                                            resultWidth3 = resultWidth5;
                                                                                            error = error2;
                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                            bitrate2 = bitrate3;
                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        encoderStatus5 = framesCount6;
                                                                                        decoderOutputAvailable3 = decoderOutputAvailable2;
                                                                                    }
                                                                                } catch (Exception e22) {
                                                                                    e5 = e22;
                                                                                    inputSurface5 = inputSurface4;
                                                                                    videoTrackIndex5 = videoTrackIndex6;
                                                                                    encoder4 = encoder7;
                                                                                    resultHeight4 = resultHeight5;
                                                                                }
                                                                            } else {
                                                                                encoderOutputBuffers5 = encoderInputBuffers;
                                                                                firstEncode2 = firstEncode5;
                                                                                encoderOutputAvailable2 = encoderOutputAvailable4;
                                                                                str19 = str14;
                                                                                str17 = str13;
                                                                                inputSurface4 = inputSurface7;
                                                                                encoderStatus5 = framesCount;
                                                                                decoderOutputAvailable3 = decoderOutputAvailable2;
                                                                            }
                                                                            obj2 = obj;
                                                                            firstEncode5 = firstEncode2;
                                                                            inputSurface7 = inputSurface4;
                                                                            str18 = str15;
                                                                            currentPts4 = currentPts3;
                                                                            encoder6 = encoder7;
                                                                            info4 = info5;
                                                                            encoderOutputAvailable4 = encoderOutputAvailable2;
                                                                            i15 = 21;
                                                                            framesCount3 = encoderStatus5;
                                                                            encoderOutputBuffers6 = encoderOutputBuffers5;
                                                                        }
                                                                    } catch (Exception e23) {
                                                                        e6 = e23;
                                                                        mediaCodecVideoConvertor = this;
                                                                    }
                                                                } catch (Exception e24) {
                                                                    mediaCodecVideoConvertor = this;
                                                                    e5 = e24;
                                                                    inputSurface5 = inputSurface7;
                                                                    videoTrackIndex5 = videoTrackIndex6;
                                                                    encoder4 = encoder6;
                                                                    resultHeight4 = resultHeight5;
                                                                }
                                                            } catch (Throwable th21) {
                                                                mediaCodecVideoConvertor = this;
                                                                resultHeight2 = framerate;
                                                                endTime2 = endTime;
                                                                avatarStartTime2 = avatarStartTime;
                                                                e = th21;
                                                                videoTrackIndex = videoTrackIndex6;
                                                                bitrate7 = bitrate6;
                                                                resultHeight4 = resultHeight5;
                                                                str = str11;
                                                            }
                                                        }
                                                    }
                                                }
                                                mediaCodecVideoConvertor = this;
                                                i14 = framerate;
                                                inputSurface5 = inputSurface7;
                                                encoder4 = encoder6;
                                                bitrate7 = bitrate6;
                                                resultHeight4 = resultHeight5;
                                                str12 = str11;
                                            } catch (Exception e25) {
                                                mediaCodecVideoConvertor = this;
                                                e5 = e25;
                                                inputSurface5 = inputSurface7;
                                                encoder4 = encoder6;
                                                resultHeight4 = resultHeight5;
                                            } catch (Throwable th22) {
                                                mediaCodecVideoConvertor = this;
                                                resultHeight2 = framerate;
                                                endTime2 = endTime;
                                                avatarStartTime2 = avatarStartTime;
                                                e = th22;
                                                videoTrackIndex = videoTrackIndex5;
                                                bitrate7 = bitrate6;
                                                resultHeight4 = resultHeight5;
                                                str = str11;
                                            }
                                        } catch (Exception e26) {
                                            mediaCodecVideoConvertor = this;
                                            bitrate6 = bitrate7;
                                            str11 = "bitrate: ";
                                            e5 = e26;
                                            inputSurface5 = inputSurface6;
                                            encoder4 = encoder5;
                                            resultHeight4 = resultHeight3;
                                        }
                                    } catch (Exception e27) {
                                        mediaCodecVideoConvertor = this;
                                        bitrate6 = bitrate7;
                                        str11 = "bitrate: ";
                                        e5 = e27;
                                        encoder4 = encoder5;
                                        resultHeight4 = resultHeight3;
                                    }
                                } catch (Exception e28) {
                                    mediaCodecVideoConvertor = this;
                                    bitrate6 = bitrate7;
                                    str11 = "bitrate: ";
                                    e5 = e28;
                                    resultHeight4 = resultHeight3;
                                }
                            } catch (Throwable th23) {
                                mediaCodecVideoConvertor = this;
                                int i16 = resultHeight3;
                                resultHeight2 = framerate;
                                endTime2 = endTime;
                                avatarStartTime2 = avatarStartTime;
                                e = th23;
                                videoTrackIndex = -5;
                                str = "bitrate: ";
                                resultHeight4 = i16;
                            }
                        } catch (Exception e29) {
                            mediaCodecVideoConvertor = this;
                            bitrate6 = bitrate7;
                            str11 = "bitrate: ";
                            e5 = e29;
                        } catch (Throwable th24) {
                            mediaCodecVideoConvertor = this;
                            resultHeight2 = framerate;
                            endTime2 = endTime;
                            avatarStartTime2 = avatarStartTime;
                            e = th24;
                            videoTrackIndex = -5;
                            str = "bitrate: ";
                        }
                    } catch (Exception e30) {
                        mediaCodecVideoConvertor = this;
                        bitrate6 = bitrate7;
                        str11 = "bitrate: ";
                        resultWidth4 = resultWidth;
                        e5 = e30;
                    } catch (Throwable th25) {
                        mediaCodecVideoConvertor = this;
                        resultWidth4 = resultWidth;
                        resultHeight2 = framerate;
                        endTime2 = endTime;
                        avatarStartTime2 = avatarStartTime;
                        e = th25;
                        videoTrackIndex = -5;
                        str = "bitrate: ";
                    }
                    if (outputSurface3 != null) {
                        outputSurface3.release();
                    }
                    if (inputSurface5 != null) {
                        inputSurface5.release();
                    }
                    if (encoder4 != null) {
                        encoder4.stop();
                        encoder4.release();
                    }
                    checkConversionCanceled();
                    endTime2 = endTime;
                    avatarStartTime2 = avatarStartTime;
                    bitrate3 = bitrate7;
                    bitrate4 = resultHeight4;
                    resultWidth5 = resultWidth4;
                    videoTrackIndex2 = videoTrackIndex5;
                } else {
                    MediaCodec.BufferInfo info6 = info4;
                    mediaCodecVideoConvertor = this;
                    try {
                        MediaExtractor mediaExtractor4 = new MediaExtractor();
                        mediaCodecVideoConvertor.extractor = mediaExtractor4;
                        mediaExtractor4.setDataSource(videoPath);
                        int videoIndex3 = MediaController.findTrack(mediaCodecVideoConvertor.extractor, false);
                        if (bitrate7 != -1) {
                            try {
                                findTrack = MediaController.findTrack(mediaCodecVideoConvertor.extractor, true);
                            } catch (Throwable th26) {
                                endTime2 = endTime;
                                avatarStartTime2 = avatarStartTime;
                                e = th26;
                                resultHeight2 = framerate;
                                bitrate7 = bitrate7;
                                resultWidth4 = resultWidth;
                                str = "bitrate: ";
                                videoTrackIndex = -5;
                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                FileLog.e(e);
                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                bitrate2 = bitrate7;
                                resultWidth3 = resultWidth4;
                                error = true;
                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                resultWidth2 = resultHeight4;
                                if (repeatWithIncreasedTimeout) {
                                }
                            }
                        } else {
                            findTrack = -1;
                        }
                        int audioIndex7 = findTrack;
                        if (videoIndex3 >= 0) {
                            needConvertVideo = false;
                            if (!mediaCodecVideoConvertor.extractor.getTrackFormat(videoIndex3).getString("mime").equals("video/avc")) {
                                needConvertVideo = true;
                            }
                        } else {
                            needConvertVideo = false;
                        }
                        try {
                            if (needCompress) {
                                videoIndex = videoIndex3;
                                audioIndex = audioIndex7;
                                str3 = "bitrate: ";
                                currentPts = 0;
                                info = info6;
                                i3 = framerate;
                            } else if (needConvertVideo) {
                                videoIndex = videoIndex3;
                                audioIndex = audioIndex7;
                                str3 = "bitrate: ";
                                currentPts = 0;
                                info = info6;
                                i3 = framerate;
                            } else {
                                try {
                                    readAndWriteTracks(mediaCodecVideoConvertor.extractor, mediaCodecVideoConvertor.mediaMuxer, info6, startTime, endTime, duration, cacheFile, bitrate7 != -1);
                                    resultWidth5 = resultWidth;
                                    bitrate4 = resultHeight;
                                    bitrate3 = bitrate;
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    videoTrackIndex2 = -5;
                                } catch (Throwable th27) {
                                    resultWidth4 = resultWidth;
                                    resultHeight4 = resultHeight;
                                    bitrate7 = bitrate;
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    e = th27;
                                    resultHeight2 = framerate;
                                    str = "bitrate: ";
                                    videoTrackIndex = -5;
                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                    FileLog.e(e);
                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                    if (mediaExtractor != null) {
                                    }
                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                    if (mP4Builder != null) {
                                    }
                                    bitrate2 = bitrate7;
                                    resultWidth3 = resultWidth4;
                                    error = true;
                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                    resultWidth2 = resultHeight4;
                                    if (repeatWithIncreasedTimeout) {
                                    }
                                }
                            }
                            AudioRecoder audioRecoder3 = null;
                            ByteBuffer audioBuffer2 = null;
                            boolean copyAudioBuffer = true;
                            long lastFramePts = -1;
                            if (videoIndex >= 0) {
                                MediaCodec decoder2 = null;
                                long videoTime = -1;
                                boolean outputDone2 = false;
                                boolean inputDone = false;
                                boolean decoderDone2 = false;
                                long additionalPresentationTime = 0;
                                long minPresentationTime6 = -2147483648L;
                                try {
                                    long frameDelta = (1000 / i3) * 1000;
                                    if (i3 < 30) {
                                        try {
                                            frameDeltaFroSkipFrames = (1000 / (i3 + 5)) * 1000;
                                        } catch (Exception e31) {
                                            bitrate5 = bitrate;
                                            endTime2 = endTime;
                                            avatarStartTime2 = avatarStartTime;
                                            e2 = e31;
                                            videoIndex2 = videoIndex;
                                            i5 = i3;
                                            try {
                                                if (e2 instanceof IllegalStateException) {
                                                }
                                                StringBuilder sb3 = new StringBuilder();
                                                str = str3;
                                                try {
                                                    sb3.append(str);
                                                    sb3.append(bitrate5);
                                                    sb3.append(" framerate: ");
                                                    sb3.append(i5);
                                                    sb3.append(" size: ");
                                                    bitrate4 = resultHeight;
                                                    try {
                                                        sb3.append(bitrate4);
                                                        sb3.append("x");
                                                        resultWidth5 = resultWidth;
                                                        try {
                                                            sb3.append(resultWidth5);
                                                            FileLog.e(sb3.toString());
                                                            FileLog.e(e2);
                                                            bitrate3 = bitrate5;
                                                            error2 = true;
                                                            i4 = i5;
                                                        } catch (Throwable th28) {
                                                            th2 = th28;
                                                            e = th2;
                                                            resultHeight4 = bitrate4;
                                                            resultWidth4 = resultWidth5;
                                                            videoTrackIndex = videoTrackIndex5;
                                                            bitrate7 = bitrate5;
                                                            resultHeight2 = i5;
                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                            FileLog.e(e);
                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            bitrate2 = bitrate7;
                                                            resultWidth3 = resultWidth4;
                                                            error = true;
                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                            resultWidth2 = resultHeight4;
                                                            if (repeatWithIncreasedTimeout) {
                                                            }
                                                        }
                                                    } catch (Throwable th29) {
                                                        th2 = th29;
                                                        resultWidth5 = resultWidth;
                                                    }
                                                } catch (Throwable th30) {
                                                    th2 = th30;
                                                    resultWidth5 = resultWidth;
                                                    bitrate4 = resultHeight;
                                                }
                                            } catch (Throwable th31) {
                                                th2 = th31;
                                                resultWidth5 = resultWidth;
                                                bitrate4 = resultHeight;
                                                str = str3;
                                            }
                                            try {
                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                i4 = i4;
                                                if (decoder2 != null) {
                                                }
                                                if (outputSurface3 != null) {
                                                }
                                                if (inputSurface5 != null) {
                                                }
                                                if (encoder4 != null) {
                                                }
                                                if (audioRecoder3 != null) {
                                                }
                                                checkConversionCanceled();
                                                videoTrackIndex2 = videoTrackIndex5;
                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                resultWidth2 = bitrate4;
                                                resultWidth3 = resultWidth5;
                                                error = error2;
                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                bitrate2 = bitrate3;
                                            } catch (Throwable th32) {
                                                e = th32;
                                                resultHeight4 = bitrate4;
                                                resultWidth4 = resultWidth5;
                                                videoTrackIndex = videoTrackIndex5;
                                                bitrate7 = bitrate3;
                                                resultHeight2 = i4;
                                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                FileLog.e(e);
                                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                bitrate2 = bitrate7;
                                                resultWidth3 = resultWidth4;
                                                error = true;
                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                resultWidth2 = resultHeight4;
                                                if (repeatWithIncreasedTimeout) {
                                                }
                                            }
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        }
                                    } else {
                                        frameDeltaFroSkipFrames = (1000 / (i3 + 1)) * 1000;
                                    }
                                    mediaCodecVideoConvertor.extractor.selectTrack(videoIndex);
                                    MediaFormat videoFormat = mediaCodecVideoConvertor.extractor.getTrackFormat(videoIndex);
                                    if (avatarStartTime >= 0) {
                                        bitrate5 = durationS <= 2000.0f ? 2600000 : durationS <= 5000.0f ? 2200000 : 1560000;
                                        avatarStartTime3 = 0;
                                    } else if (bitrate <= 0) {
                                        bitrate5 = 921600;
                                        avatarStartTime3 = avatarStartTime;
                                    } else {
                                        bitrate5 = bitrate;
                                        avatarStartTime3 = avatarStartTime;
                                    }
                                    if (originalBitrate > 0) {
                                        try {
                                            bitrate5 = Math.min(originalBitrate, bitrate5);
                                        } catch (Exception e32) {
                                            endTime2 = endTime;
                                            e2 = e32;
                                            avatarStartTime2 = avatarStartTime3;
                                            videoIndex2 = videoIndex;
                                            i5 = i3;
                                            if (e2 instanceof IllegalStateException) {
                                            }
                                            StringBuilder sb32 = new StringBuilder();
                                            str = str3;
                                            sb32.append(str);
                                            sb32.append(bitrate5);
                                            sb32.append(" framerate: ");
                                            sb32.append(i5);
                                            sb32.append(" size: ");
                                            bitrate4 = resultHeight;
                                            sb32.append(bitrate4);
                                            sb32.append("x");
                                            resultWidth5 = resultWidth;
                                            sb32.append(resultWidth5);
                                            FileLog.e(sb32.toString());
                                            FileLog.e(e2);
                                            bitrate3 = bitrate5;
                                            error2 = true;
                                            i4 = i5;
                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                            i4 = i4;
                                            if (decoder2 != null) {
                                            }
                                            if (outputSurface3 != null) {
                                            }
                                            if (inputSurface5 != null) {
                                            }
                                            if (encoder4 != null) {
                                            }
                                            if (audioRecoder3 != null) {
                                            }
                                            checkConversionCanceled();
                                            videoTrackIndex2 = videoTrackIndex5;
                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                            if (mediaExtractor2 != null) {
                                            }
                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                            if (mP4Builder2 != null) {
                                            }
                                            resultWidth2 = bitrate4;
                                            resultWidth3 = resultWidth5;
                                            error = error2;
                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                            bitrate2 = bitrate3;
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        } catch (Throwable th33) {
                                            resultWidth4 = resultWidth;
                                            resultHeight4 = resultHeight;
                                            endTime2 = endTime;
                                            e = th33;
                                            bitrate7 = bitrate5;
                                            avatarStartTime2 = avatarStartTime3;
                                            videoTrackIndex = -5;
                                            str = str3;
                                            resultHeight2 = i3;
                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                            FileLog.e(e);
                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            bitrate2 = bitrate7;
                                            resultWidth3 = resultWidth4;
                                            error = true;
                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                            resultWidth2 = resultHeight4;
                                            if (repeatWithIncreasedTimeout) {
                                            }
                                        }
                                    }
                                    long avatarStartTime5 = avatarStartTime3 >= 0 ? -1L : avatarStartTime3;
                                    try {
                                        try {
                                            try {
                                                try {
                                                    try {
                                                        try {
                                                            try {
                                                                try {
                                                                    try {
                                                                        try {
                                                                            try {
                                                                                try {
                                                                                    try {
                                                                                        if (avatarStartTime5 >= 0) {
                                                                                            mediaCodecVideoConvertor.extractor.seekTo(avatarStartTime5, 0);
                                                                                            avatarStartTime4 = avatarStartTime5;
                                                                                            endTime2 = frameDelta;
                                                                                            avatarStartTime5 = 0;
                                                                                        } else {
                                                                                            endTime2 = frameDelta;
                                                                                            if (startTime > 0) {
                                                                                                mediaCodecVideoConvertor.extractor.seekTo(startTime, 0);
                                                                                                avatarStartTime4 = avatarStartTime5;
                                                                                                avatarStartTime5 = 0;
                                                                                            } else {
                                                                                                try {
                                                                                                    avatarStartTime4 = avatarStartTime5;
                                                                                                    try {
                                                                                                        try {
                                                                                                            mediaCodecVideoConvertor.extractor.seekTo(0L, 0);
                                                                                                            if (cropState == null) {
                                                                                                                try {
                                                                                                                    if (rotationValue == 90 || rotationValue == 270) {
                                                                                                                        w = cropState.transformHeight;
                                                                                                                        w2 = cropState.transformWidth;
                                                                                                                    } else {
                                                                                                                        w = cropState.transformWidth;
                                                                                                                        w2 = cropState.transformHeight;
                                                                                                                    }
                                                                                                                } catch (Exception e33) {
                                                                                                                    endTime2 = endTime;
                                                                                                                    avatarStartTime2 = avatarStartTime4;
                                                                                                                    e2 = e33;
                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                    i5 = i3;
                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb322 = new StringBuilder();
                                                                                                                    str = str3;
                                                                                                                    sb322.append(str);
                                                                                                                    sb322.append(bitrate5);
                                                                                                                    sb322.append(" framerate: ");
                                                                                                                    sb322.append(i5);
                                                                                                                    sb322.append(" size: ");
                                                                                                                    bitrate4 = resultHeight;
                                                                                                                    sb322.append(bitrate4);
                                                                                                                    sb322.append("x");
                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                    sb322.append(resultWidth5);
                                                                                                                    FileLog.e(sb322.toString());
                                                                                                                    FileLog.e(e2);
                                                                                                                    bitrate3 = bitrate5;
                                                                                                                    error2 = true;
                                                                                                                    i4 = i5;
                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                    i4 = i4;
                                                                                                                    if (decoder2 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface5 != null) {
                                                                                                                    }
                                                                                                                    if (encoder4 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                    error = error2;
                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                    bitrate2 = bitrate3;
                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                w = resultWidth;
                                                                                                                w2 = resultHeight;
                                                                                                            }
                                                                                                            if (!BuildVars.LOGS_ENABLED) {
                                                                                                                decoder = null;
                                                                                                                try {
                                                                                                                    FileLog.d("create encoder with w = " + w + " h = " + w2);
                                                                                                                } catch (Exception e34) {
                                                                                                                    endTime2 = endTime;
                                                                                                                    avatarStartTime2 = avatarStartTime4;
                                                                                                                    e2 = e34;
                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                    decoder2 = decoder;
                                                                                                                    i5 = i3;
                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb3222 = new StringBuilder();
                                                                                                                    str = str3;
                                                                                                                    sb3222.append(str);
                                                                                                                    sb3222.append(bitrate5);
                                                                                                                    sb3222.append(" framerate: ");
                                                                                                                    sb3222.append(i5);
                                                                                                                    sb3222.append(" size: ");
                                                                                                                    bitrate4 = resultHeight;
                                                                                                                    sb3222.append(bitrate4);
                                                                                                                    sb3222.append("x");
                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                    sb3222.append(resultWidth5);
                                                                                                                    FileLog.e(sb3222.toString());
                                                                                                                    FileLog.e(e2);
                                                                                                                    bitrate3 = bitrate5;
                                                                                                                    error2 = true;
                                                                                                                    i4 = i5;
                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                    i4 = i4;
                                                                                                                    if (decoder2 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface5 != null) {
                                                                                                                    }
                                                                                                                    if (encoder4 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                    error = error2;
                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                    bitrate2 = bitrate3;
                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                decoder = null;
                                                                                                            }
                                                                                                            MediaFormat outputFormat2 = MediaFormat.createVideoFormat("video/avc", w, w2);
                                                                                                            outputFormat2.setInteger("color-format", 2130708361);
                                                                                                            outputFormat2.setInteger("bitrate", bitrate5);
                                                                                                            outputFormat2.setInteger("frame-rate", i3);
                                                                                                            outputFormat2.setInteger("i-frame-interval", 2);
                                                                                                            if (Build.VERSION.SDK_INT < 23) {
                                                                                                                if (Math.min(w2, w) <= 480) {
                                                                                                                    int bitrate8 = bitrate5 > 921600 ? 921600 : bitrate5;
                                                                                                                    try {
                                                                                                                        outputFormat2.setInteger("bitrate", bitrate8);
                                                                                                                        bitrate3 = bitrate8;
                                                                                                                        encoder = MediaCodec.createEncoderByType("video/avc");
                                                                                                                        encoder.configure(outputFormat2, (Surface) null, (MediaCrypto) null, 1);
                                                                                                                        inputSurface = new InputSurface(encoder.createInputSurface());
                                                                                                                        inputSurface.makeCurrent();
                                                                                                                        encoder.start();
                                                                                                                        decoder2 = MediaCodec.createDecoderByType(videoFormat.getString("mime"));
                                                                                                                        inputSurface2 = inputSurface;
                                                                                                                        avatarStartTime2 = avatarStartTime4;
                                                                                                                        int h3 = w2;
                                                                                                                        encoder2 = encoder;
                                                                                                                        String str21 = "video/avc";
                                                                                                                        int w4 = w;
                                                                                                                        String str22 = "prepend-sps-pps-to-idr-frames";
                                                                                                                        long frameDelta2 = endTime2;
                                                                                                                        String str23 = "csd-1";
                                                                                                                        String str24 = "csd-0";
                                                                                                                        info2 = info;
                                                                                                                        outputSurface = new OutputSurface(savedFilterState, null, paintPath, mediaEntities, cropState, resultWidth, resultHeight, originalWidth, originalHeight, rotationValue, i3, false);
                                                                                                                        if (!isRound) {
                                                                                                                            i7 = resultHeight;
                                                                                                                            try {
                                                                                                                                try {
                                                                                                                                    if (Math.max(i7, i7) / Math.max(originalHeight, originalWidth) < 0.9f) {
                                                                                                                                        i6 = resultWidth;
                                                                                                                                        try {
                                                                                                                                            try {
                                                                                                                                                i8 = 0;
                                                                                                                                                outputSurface.changeFragmentShader(createFragmentShader(originalWidth, originalHeight, i6, i7, true), createFragmentShader(originalWidth, originalHeight, i6, i7, false));
                                                                                                                                                decoder2.configure(videoFormat, outputSurface.getSurface(), (MediaCrypto) null, i8);
                                                                                                                                                decoder2.start();
                                                                                                                                                decoderInputBuffers = null;
                                                                                                                                                ByteBuffer[] encoderOutputBuffers9 = null;
                                                                                                                                                if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                                    try {
                                                                                                                                                        decoderInputBuffers = decoder2.getInputBuffers();
                                                                                                                                                        encoderOutputBuffers9 = encoder2.getOutputBuffers();
                                                                                                                                                    } catch (Exception e35) {
                                                                                                                                                        i5 = framerate;
                                                                                                                                                        encoder4 = encoder2;
                                                                                                                                                        endTime2 = endTime;
                                                                                                                                                        e2 = e35;
                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                        videoIndex2 = videoIndex;
                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                        }
                                                                                                                                                        StringBuilder sb32222 = new StringBuilder();
                                                                                                                                                        str = str3;
                                                                                                                                                        sb32222.append(str);
                                                                                                                                                        sb32222.append(bitrate5);
                                                                                                                                                        sb32222.append(" framerate: ");
                                                                                                                                                        sb32222.append(i5);
                                                                                                                                                        sb32222.append(" size: ");
                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                        sb32222.append(bitrate4);
                                                                                                                                                        sb32222.append("x");
                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                        sb32222.append(resultWidth5);
                                                                                                                                                        FileLog.e(sb32222.toString());
                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                        error2 = true;
                                                                                                                                                        i4 = i5;
                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                        i4 = i4;
                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                        }
                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                        }
                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                        }
                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                        }
                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                        }
                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                        }
                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                        }
                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                        error = error2;
                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                int maxBufferSize3 = 0;
                                                                                                                                                audioIndex2 = audioIndex;
                                                                                                                                                if (audioIndex2 < 0) {
                                                                                                                                                    try {
                                                                                                                                                        MediaFormat audioFormat = mediaCodecVideoConvertor.extractor.getTrackFormat(audioIndex2);
                                                                                                                                                        encoderOutputBuffers = encoderOutputBuffers9;
                                                                                                                                                        if (!audioFormat.getString("mime").equals("audio/mp4a-latm")) {
                                                                                                                                                            try {
                                                                                                                                                                if (!audioFormat.getString("mime").equals(MimeTypes.AUDIO_MPEG)) {
                                                                                                                                                                    z = false;
                                                                                                                                                                    copyAudioBuffer = z;
                                                                                                                                                                    audioIndex3 = !audioFormat.getString("mime").equals("audio/unknown") ? -1 : audioIndex2;
                                                                                                                                                                    if (audioIndex3 < 0) {
                                                                                                                                                                        try {
                                                                                                                                                                            if (copyAudioBuffer) {
                                                                                                                                                                                try {
                                                                                                                                                                                    int audioTrackIndex2 = mediaCodecVideoConvertor.mediaMuxer.addTrack(audioFormat, true);
                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.selectTrack(audioIndex3);
                                                                                                                                                                                    try {
                                                                                                                                                                                        maxBufferSize3 = audioFormat.getInteger("max-input-size");
                                                                                                                                                                                    } catch (Exception e36) {
                                                                                                                                                                                        FileLog.e(e36);
                                                                                                                                                                                    }
                                                                                                                                                                                    if (maxBufferSize3 <= 0) {
                                                                                                                                                                                        maxBufferSize3 = 65536;
                                                                                                                                                                                    }
                                                                                                                                                                                    audioBuffer2 = ByteBuffer.allocateDirect(maxBufferSize3);
                                                                                                                                                                                    j = startTime;
                                                                                                                                                                                    int maxBufferSize4 = maxBufferSize3;
                                                                                                                                                                                    if (j > 0) {
                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.seekTo(j, 0);
                                                                                                                                                                                    } else {
                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.seekTo(0L, 0);
                                                                                                                                                                                    }
                                                                                                                                                                                    j2 = endTime;
                                                                                                                                                                                    audioIndex4 = audioIndex3;
                                                                                                                                                                                    maxBufferSize = maxBufferSize4;
                                                                                                                                                                                    audioRecoder = null;
                                                                                                                                                                                    audioIndex5 = audioTrackIndex2;
                                                                                                                                                                                } catch (Exception e37) {
                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                    endTime2 = endTime;
                                                                                                                                                                                    e2 = e37;
                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                    }
                                                                                                                                                                                    StringBuilder sb322222 = new StringBuilder();
                                                                                                                                                                                    str = str3;
                                                                                                                                                                                    sb322222.append(str);
                                                                                                                                                                                    sb322222.append(bitrate5);
                                                                                                                                                                                    sb322222.append(" framerate: ");
                                                                                                                                                                                    sb322222.append(i5);
                                                                                                                                                                                    sb322222.append(" size: ");
                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                    sb322222.append(bitrate4);
                                                                                                                                                                                    sb322222.append("x");
                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                    sb322222.append(resultWidth5);
                                                                                                                                                                                    FileLog.e(sb322222.toString());
                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                    error = error2;
                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                    }
                                                                                                                                                                                } catch (Throwable th34) {
                                                                                                                                                                                    resultHeight2 = framerate;
                                                                                                                                                                                    endTime2 = endTime;
                                                                                                                                                                                    e = th34;
                                                                                                                                                                                    resultHeight4 = i7;
                                                                                                                                                                                    resultWidth4 = i6;
                                                                                                                                                                                    videoTrackIndex = -5;
                                                                                                                                                                                    str = str3;
                                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                    FileLog.e(e);
                                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                                    }
                                                                                                                                                                                    bitrate2 = bitrate7;
                                                                                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                                                                                    error = true;
                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                j = startTime;
                                                                                                                                                                                try {
                                                                                                                                                                                    MediaExtractor audioExtractor = new MediaExtractor();
                                                                                                                                                                                    try {
                                                                                                                                                                                        try {
                                                                                                                                                                                            audioExtractor.setDataSource(videoPath);
                                                                                                                                                                                            audioExtractor.selectTrack(audioIndex3);
                                                                                                                                                                                            if (j > 0) {
                                                                                                                                                                                                audioExtractor.seekTo(j, 0);
                                                                                                                                                                                            } else {
                                                                                                                                                                                                audioExtractor.seekTo(0L, 0);
                                                                                                                                                                                            }
                                                                                                                                                                                            AudioRecoder audioRecoder4 = new AudioRecoder(audioFormat, audioExtractor, audioIndex3);
                                                                                                                                                                                            try {
                                                                                                                                                                                                audioRecoder4.startTime = j;
                                                                                                                                                                                                j2 = endTime;
                                                                                                                                                                                            } catch (Exception e38) {
                                                                                                                                                                                                e3 = e38;
                                                                                                                                                                                                j2 = endTime;
                                                                                                                                                                                            }
                                                                                                                                                                                            try {
                                                                                                                                                                                                try {
                                                                                                                                                                                                    audioRecoder4.endTime = j2;
                                                                                                                                                                                                    maxBufferSize = 0;
                                                                                                                                                                                                } catch (Exception e39) {
                                                                                                                                                                                                    e3 = e39;
                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                                    e2 = e3;
                                                                                                                                                                                                    endTime2 = j2;
                                                                                                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                                                                                                    audioRecoder3 = audioRecoder4;
                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    StringBuilder sb3222222 = new StringBuilder();
                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                    sb3222222.append(str);
                                                                                                                                                                                                    sb3222222.append(bitrate5);
                                                                                                                                                                                                    sb3222222.append(" framerate: ");
                                                                                                                                                                                                    sb3222222.append(i5);
                                                                                                                                                                                                    sb3222222.append(" size: ");
                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                    sb3222222.append(bitrate4);
                                                                                                                                                                                                    sb3222222.append("x");
                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                    sb3222222.append(resultWidth5);
                                                                                                                                                                                                    FileLog.e(sb3222222.toString());
                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                                try {
                                                                                                                                                                                                    int audioTrackIndex3 = mediaCodecVideoConvertor.mediaMuxer.addTrack(audioRecoder4.format, true);
                                                                                                                                                                                                    audioIndex4 = audioIndex3;
                                                                                                                                                                                                    audioRecoder = audioRecoder4;
                                                                                                                                                                                                    audioIndex5 = audioTrackIndex3;
                                                                                                                                                                                                } catch (Exception e40) {
                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                                    e2 = e40;
                                                                                                                                                                                                    endTime2 = j2;
                                                                                                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                                                                                                    audioRecoder3 = audioRecoder4;
                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    StringBuilder sb32222222 = new StringBuilder();
                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                    sb32222222.append(str);
                                                                                                                                                                                                    sb32222222.append(bitrate5);
                                                                                                                                                                                                    sb32222222.append(" framerate: ");
                                                                                                                                                                                                    sb32222222.append(i5);
                                                                                                                                                                                                    sb32222222.append(" size: ");
                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                    sb32222222.append(bitrate4);
                                                                                                                                                                                                    sb32222222.append("x");
                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                    sb32222222.append(resultWidth5);
                                                                                                                                                                                                    FileLog.e(sb32222222.toString());
                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            } catch (Throwable th35) {
                                                                                                                                                                                                resultWidth4 = resultWidth;
                                                                                                                                                                                                resultHeight2 = framerate;
                                                                                                                                                                                                e = th35;
                                                                                                                                                                                                endTime2 = j2;
                                                                                                                                                                                                videoTrackIndex = -5;
                                                                                                                                                                                                str = str3;
                                                                                                                                                                                                bitrate7 = bitrate3;
                                                                                                                                                                                                resultHeight4 = resultHeight;
                                                                                                                                                                                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                FileLog.e(e);
                                                                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                                                                }
                                                                                                                                                                                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                                                                }
                                                                                                                                                                                                bitrate2 = bitrate7;
                                                                                                                                                                                                resultWidth3 = resultWidth4;
                                                                                                                                                                                                error = true;
                                                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                resultWidth2 = resultHeight4;
                                                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        } catch (Exception e41) {
                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                                            e2 = e41;
                                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                            endTime2 = endTime;
                                                                                                                                                                                            videoIndex2 = videoIndex;
                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                        }
                                                                                                                                                                                    } catch (Throwable th36) {
                                                                                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                                                                                        resultHeight2 = framerate;
                                                                                                                                                                                        e = th36;
                                                                                                                                                                                        endTime2 = endTime;
                                                                                                                                                                                        videoTrackIndex = -5;
                                                                                                                                                                                        str = str3;
                                                                                                                                                                                        bitrate7 = bitrate3;
                                                                                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                                                                                    }
                                                                                                                                                                                } catch (Exception e42) {
                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                    e2 = e42;
                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                    endTime2 = endTime;
                                                                                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                } catch (Throwable th37) {
                                                                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                                                                    resultHeight2 = framerate;
                                                                                                                                                                                    e = th37;
                                                                                                                                                                                    endTime2 = endTime;
                                                                                                                                                                                    videoTrackIndex = -5;
                                                                                                                                                                                    str = str3;
                                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                                    resultHeight4 = resultHeight;
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        } catch (Exception e43) {
                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                            endTime2 = endTime;
                                                                                                                                                                            e2 = e43;
                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                            videoIndex2 = videoIndex;
                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                        }
                                                                                                                                                                    } else {
                                                                                                                                                                        j = startTime;
                                                                                                                                                                        maxBufferSize = 0;
                                                                                                                                                                        j2 = endTime;
                                                                                                                                                                        audioIndex4 = audioIndex3;
                                                                                                                                                                        audioRecoder = null;
                                                                                                                                                                        audioIndex5 = -5;
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            } catch (Exception e44) {
                                                                                                                                                                i5 = framerate;
                                                                                                                                                                encoder4 = encoder2;
                                                                                                                                                                endTime2 = endTime;
                                                                                                                                                                e2 = e44;
                                                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                videoIndex2 = videoIndex;
                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                if (e2 instanceof IllegalStateException) {
                                                                                                                                                                }
                                                                                                                                                                StringBuilder sb322222222 = new StringBuilder();
                                                                                                                                                                str = str3;
                                                                                                                                                                sb322222222.append(str);
                                                                                                                                                                sb322222222.append(bitrate5);
                                                                                                                                                                sb322222222.append(" framerate: ");
                                                                                                                                                                sb322222222.append(i5);
                                                                                                                                                                sb322222222.append(" size: ");
                                                                                                                                                                bitrate4 = resultHeight;
                                                                                                                                                                sb322222222.append(bitrate4);
                                                                                                                                                                sb322222222.append("x");
                                                                                                                                                                resultWidth5 = resultWidth;
                                                                                                                                                                sb322222222.append(resultWidth5);
                                                                                                                                                                FileLog.e(sb322222222.toString());
                                                                                                                                                                FileLog.e(e2);
                                                                                                                                                                bitrate3 = bitrate5;
                                                                                                                                                                error2 = true;
                                                                                                                                                                i4 = i5;
                                                                                                                                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                i4 = i4;
                                                                                                                                                                if (decoder2 != null) {
                                                                                                                                                                }
                                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                                }
                                                                                                                                                                if (inputSurface5 != null) {
                                                                                                                                                                }
                                                                                                                                                                if (encoder4 != null) {
                                                                                                                                                                }
                                                                                                                                                                if (audioRecoder3 != null) {
                                                                                                                                                                }
                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                }
                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                }
                                                                                                                                                                resultWidth2 = bitrate4;
                                                                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                                                                error = error2;
                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                bitrate2 = bitrate3;
                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                        z = true;
                                                                                                                                                        copyAudioBuffer = z;
                                                                                                                                                        if (!audioFormat.getString("mime").equals("audio/unknown")) {
                                                                                                                                                        }
                                                                                                                                                        if (audioIndex3 < 0) {
                                                                                                                                                        }
                                                                                                                                                    } catch (Exception e45) {
                                                                                                                                                        i5 = framerate;
                                                                                                                                                        encoder4 = encoder2;
                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                        endTime2 = endTime;
                                                                                                                                                        videoIndex2 = videoIndex;
                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                        e2 = e45;
                                                                                                                                                    } catch (Throwable th38) {
                                                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                                                        resultHeight2 = framerate;
                                                                                                                                                        e = th38;
                                                                                                                                                        endTime2 = endTime;
                                                                                                                                                        videoTrackIndex = -5;
                                                                                                                                                        str = str3;
                                                                                                                                                        bitrate7 = bitrate3;
                                                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    encoderOutputBuffers = encoderOutputBuffers9;
                                                                                                                                                    maxBufferSize = 0;
                                                                                                                                                    j = startTime;
                                                                                                                                                    j2 = endTime;
                                                                                                                                                    audioIndex4 = audioIndex2;
                                                                                                                                                    audioRecoder = null;
                                                                                                                                                    audioIndex5 = -5;
                                                                                                                                                }
                                                                                                                                                audioEncoderDone = audioIndex4 >= 0;
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                encoderOutputBuffers2 = encoderOutputBuffers;
                                                                                                                                                videoTrackIndex3 = -5;
                                                                                                                                                boolean firstEncode8 = true;
                                                                                                                                                maxBufferSize2 = maxBufferSize;
                                                                                                                                                endTime3 = j2;
                                                                                                                                                loop4: while (true) {
                                                                                                                                                    if (outputDone2 || (!copyAudioBuffer && !audioEncoderDone)) {
                                                                                                                                                        try {
                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                            if (!copyAudioBuffer || audioRecoder == null) {
                                                                                                                                                                audioEncoderDone2 = audioEncoderDone;
                                                                                                                                                            } else {
                                                                                                                                                                try {
                                                                                                                                                                    try {
                                                                                                                                                                        audioEncoderDone2 = audioRecoder.step(mediaCodecVideoConvertor.mediaMuxer, audioIndex5);
                                                                                                                                                                    } catch (Exception e46) {
                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                        encoder4 = encoder2;
                                                                                                                                                                        e2 = e46;
                                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                                        audioRecoder3 = audioRecoder;
                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                        endTime2 = endTime3;
                                                                                                                                                                        videoIndex2 = videoIndex;
                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                                        }
                                                                                                                                                                        StringBuilder sb3222222222 = new StringBuilder();
                                                                                                                                                                        str = str3;
                                                                                                                                                                        sb3222222222.append(str);
                                                                                                                                                                        sb3222222222.append(bitrate5);
                                                                                                                                                                        sb3222222222.append(" framerate: ");
                                                                                                                                                                        sb3222222222.append(i5);
                                                                                                                                                                        sb3222222222.append(" size: ");
                                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                                        sb3222222222.append(bitrate4);
                                                                                                                                                                        sb3222222222.append("x");
                                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                                        sb3222222222.append(resultWidth5);
                                                                                                                                                                        FileLog.e(sb3222222222.toString());
                                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                                        error2 = true;
                                                                                                                                                                        i4 = i5;
                                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                        i4 = i4;
                                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                        }
                                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                                        error = error2;
                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                } catch (Throwable th39) {
                                                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                                                    resultHeight2 = framerate;
                                                                                                                                                                    e = th39;
                                                                                                                                                                    endTime2 = endTime3;
                                                                                                                                                                    videoTrackIndex = videoTrackIndex3;
                                                                                                                                                                    str = str3;
                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                    resultHeight4 = resultHeight;
                                                                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                    FileLog.e(e);
                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                    }
                                                                                                                                                                    bitrate2 = bitrate7;
                                                                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                                                                    error = true;
                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            if (inputDone) {
                                                                                                                                                                boolean eof = false;
                                                                                                                                                                audioRecoder2 = audioRecoder;
                                                                                                                                                                try {
                                                                                                                                                                    try {
                                                                                                                                                                        int index = mediaCodecVideoConvertor.extractor.getSampleTrackIndex();
                                                                                                                                                                        if (index == videoIndex) {
                                                                                                                                                                            videoIndex2 = videoIndex;
                                                                                                                                                                            try {
                                                                                                                                                                                int inputBufIndex = decoder2.dequeueInputBuffer(2500L);
                                                                                                                                                                                if (inputBufIndex >= 0) {
                                                                                                                                                                                    audioEncoderDone3 = audioEncoderDone2;
                                                                                                                                                                                    ByteBuffer inputBuf = Build.VERSION.SDK_INT < 21 ? decoderInputBuffers[inputBufIndex] : decoder2.getInputBuffer(inputBufIndex);
                                                                                                                                                                                    decoderInputBuffers2 = decoderInputBuffers;
                                                                                                                                                                                    int chunkSize = mediaCodecVideoConvertor.extractor.readSampleData(inputBuf, 0);
                                                                                                                                                                                    if (chunkSize < 0) {
                                                                                                                                                                                        decoder2.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
                                                                                                                                                                                        inputDone = true;
                                                                                                                                                                                    } else {
                                                                                                                                                                                        decoder2.queueInputBuffer(inputBufIndex, 0, chunkSize, mediaCodecVideoConvertor.extractor.getSampleTime(), 0);
                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.advance();
                                                                                                                                                                                    }
                                                                                                                                                                                } else {
                                                                                                                                                                                    decoderInputBuffers2 = decoderInputBuffers;
                                                                                                                                                                                    audioEncoderDone3 = audioEncoderDone2;
                                                                                                                                                                                }
                                                                                                                                                                                audioTrackIndex = audioIndex5;
                                                                                                                                                                                audioIndex6 = audioIndex4;
                                                                                                                                                                                endTime4 = endTime3;
                                                                                                                                                                                info3 = info2;
                                                                                                                                                                            } catch (Exception e47) {
                                                                                                                                                                                i5 = framerate;
                                                                                                                                                                                encoder4 = encoder2;
                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                e2 = e47;
                                                                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                endTime2 = endTime3;
                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                                if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                }
                                                                                                                                                                                StringBuilder sb32222222222 = new StringBuilder();
                                                                                                                                                                                str = str3;
                                                                                                                                                                                sb32222222222.append(str);
                                                                                                                                                                                sb32222222222.append(bitrate5);
                                                                                                                                                                                sb32222222222.append(" framerate: ");
                                                                                                                                                                                sb32222222222.append(i5);
                                                                                                                                                                                sb32222222222.append(" size: ");
                                                                                                                                                                                bitrate4 = resultHeight;
                                                                                                                                                                                sb32222222222.append(bitrate4);
                                                                                                                                                                                sb32222222222.append("x");
                                                                                                                                                                                resultWidth5 = resultWidth;
                                                                                                                                                                                sb32222222222.append(resultWidth5);
                                                                                                                                                                                FileLog.e(sb32222222222.toString());
                                                                                                                                                                                FileLog.e(e2);
                                                                                                                                                                                bitrate3 = bitrate5;
                                                                                                                                                                                error2 = true;
                                                                                                                                                                                i4 = i5;
                                                                                                                                                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                i4 = i4;
                                                                                                                                                                                if (decoder2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (inputSurface5 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (encoder4 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (audioRecoder3 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                resultWidth2 = bitrate4;
                                                                                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                                                                                error = error2;
                                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                bitrate2 = bitrate3;
                                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        } else {
                                                                                                                                                                            decoderInputBuffers2 = decoderInputBuffers;
                                                                                                                                                                            audioEncoderDone3 = audioEncoderDone2;
                                                                                                                                                                            videoIndex2 = videoIndex;
                                                                                                                                                                            if (!copyAudioBuffer || audioIndex4 == -1 || index != audioIndex4) {
                                                                                                                                                                                audioTrackIndex = audioIndex5;
                                                                                                                                                                                audioIndex6 = audioIndex4;
                                                                                                                                                                                endTime4 = endTime3;
                                                                                                                                                                                info3 = info2;
                                                                                                                                                                                if (index == -1) {
                                                                                                                                                                                    eof = true;
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                try {
                                                                                                                                                                                    try {
                                                                                                                                                                                        try {
                                                                                                                                                                                            if (Build.VERSION.SDK_INT >= 28) {
                                                                                                                                                                                                long size = mediaCodecVideoConvertor.extractor.getSampleSize();
                                                                                                                                                                                                if (size > maxBufferSize2) {
                                                                                                                                                                                                    maxBufferSize2 = (int) (DistributeConstants.KIBIBYTE_IN_BYTES + size);
                                                                                                                                                                                                    audioBuffer = ByteBuffer.allocateDirect(maxBufferSize2);
                                                                                                                                                                                                    info3 = info2;
                                                                                                                                                                                                    info3.size = mediaCodecVideoConvertor.extractor.readSampleData(audioBuffer, 0);
                                                                                                                                                                                                    if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                                                                                        try {
                                                                                                                                                                                                            audioBuffer.position(0);
                                                                                                                                                                                                            audioBuffer.limit(info3.size);
                                                                                                                                                                                                        } catch (Exception e48) {
                                                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                            e2 = e48;
                                                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                            endTime2 = endTime3;
                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            StringBuilder sb322222222222 = new StringBuilder();
                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                            sb322222222222.append(str);
                                                                                                                                                                                                            sb322222222222.append(bitrate5);
                                                                                                                                                                                                            sb322222222222.append(" framerate: ");
                                                                                                                                                                                                            sb322222222222.append(i5);
                                                                                                                                                                                                            sb322222222222.append(" size: ");
                                                                                                                                                                                                            bitrate4 = resultHeight;
                                                                                                                                                                                                            sb322222222222.append(bitrate4);
                                                                                                                                                                                                            sb322222222222.append("x");
                                                                                                                                                                                                            resultWidth5 = resultWidth;
                                                                                                                                                                                                            sb322222222222.append(resultWidth5);
                                                                                                                                                                                                            FileLog.e(sb322222222222.toString());
                                                                                                                                                                                                            FileLog.e(e2);
                                                                                                                                                                                                            bitrate3 = bitrate5;
                                                                                                                                                                                                            error2 = true;
                                                                                                                                                                                                            i4 = i5;
                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                            i4 = i4;
                                                                                                                                                                                                            if (decoder2 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (inputSurface5 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (encoder4 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (audioRecoder3 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            resultWidth2 = bitrate4;
                                                                                                                                                                                                            resultWidth3 = resultWidth5;
                                                                                                                                                                                                            error = error2;
                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                            bitrate2 = bitrate3;
                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (info3.size < 0) {
                                                                                                                                                                                                        info3.presentationTimeUs = mediaCodecVideoConvertor.extractor.getSampleTime();
                                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.advance();
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        info3.size = 0;
                                                                                                                                                                                                        inputDone = true;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (info3.size > 0) {
                                                                                                                                                                                                        audioTrackIndex = audioIndex5;
                                                                                                                                                                                                        audioIndex6 = audioIndex4;
                                                                                                                                                                                                        endTime4 = endTime3;
                                                                                                                                                                                                    } else if (endTime3 < 0 || info3.presentationTimeUs < endTime3) {
                                                                                                                                                                                                        info3.offset = 0;
                                                                                                                                                                                                        info3.flags = mediaCodecVideoConvertor.extractor.getSampleFlags();
                                                                                                                                                                                                        long availableSize2 = mediaCodecVideoConvertor.mediaMuxer.writeSampleData(audioIndex5, audioBuffer, info3, false);
                                                                                                                                                                                                        if (availableSize2 != 0) {
                                                                                                                                                                                                            audioTrackIndex = audioIndex5;
                                                                                                                                                                                                            if (mediaCodecVideoConvertor.callback != null) {
                                                                                                                                                                                                                audioIndex6 = audioIndex4;
                                                                                                                                                                                                                try {
                                                                                                                                                                                                                    if (info3.presentationTimeUs - j > currentPts) {
                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                            currentPts2 = info3.presentationTimeUs - j;
                                                                                                                                                                                                                        } catch (Exception e49) {
                                                                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                            e2 = e49;
                                                                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                                            endTime2 = endTime3;
                                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            StringBuilder sb3222222222222 = new StringBuilder();
                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                            sb3222222222222.append(str);
                                                                                                                                                                                                                            sb3222222222222.append(bitrate5);
                                                                                                                                                                                                                            sb3222222222222.append(" framerate: ");
                                                                                                                                                                                                                            sb3222222222222.append(i5);
                                                                                                                                                                                                                            sb3222222222222.append(" size: ");
                                                                                                                                                                                                                            bitrate4 = resultHeight;
                                                                                                                                                                                                                            sb3222222222222.append(bitrate4);
                                                                                                                                                                                                                            sb3222222222222.append("x");
                                                                                                                                                                                                                            resultWidth5 = resultWidth;
                                                                                                                                                                                                                            sb3222222222222.append(resultWidth5);
                                                                                                                                                                                                                            FileLog.e(sb3222222222222.toString());
                                                                                                                                                                                                                            FileLog.e(e2);
                                                                                                                                                                                                                            bitrate3 = bitrate5;
                                                                                                                                                                                                                            error2 = true;
                                                                                                                                                                                                                            i4 = i5;
                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                            i4 = i4;
                                                                                                                                                                                                                            if (decoder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (inputSurface5 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (encoder4 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (audioRecoder3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            resultWidth2 = bitrate4;
                                                                                                                                                                                                                            resultWidth3 = resultWidth5;
                                                                                                                                                                                                                            error = error2;
                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                            bitrate2 = bitrate3;
                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                        currentPts2 = currentPts;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    endTime4 = endTime3;
                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                            mediaCodecVideoConvertor.callback.didWriteData(availableSize2, (((float) currentPts2) / 1000.0f) / durationS);
                                                                                                                                                                                                                            currentPts = currentPts2;
                                                                                                                                                                                                                        } catch (Exception e50) {
                                                                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                                                            endTime2 = endTime4;
                                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                                                            e2 = e50;
                                                                                                                                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            StringBuilder sb32222222222222 = new StringBuilder();
                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                            sb32222222222222.append(str);
                                                                                                                                                                                                                            sb32222222222222.append(bitrate5);
                                                                                                                                                                                                                            sb32222222222222.append(" framerate: ");
                                                                                                                                                                                                                            sb32222222222222.append(i5);
                                                                                                                                                                                                                            sb32222222222222.append(" size: ");
                                                                                                                                                                                                                            bitrate4 = resultHeight;
                                                                                                                                                                                                                            sb32222222222222.append(bitrate4);
                                                                                                                                                                                                                            sb32222222222222.append("x");
                                                                                                                                                                                                                            resultWidth5 = resultWidth;
                                                                                                                                                                                                                            sb32222222222222.append(resultWidth5);
                                                                                                                                                                                                                            FileLog.e(sb32222222222222.toString());
                                                                                                                                                                                                                            FileLog.e(e2);
                                                                                                                                                                                                                            bitrate3 = bitrate5;
                                                                                                                                                                                                                            error2 = true;
                                                                                                                                                                                                                            i4 = i5;
                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                            i4 = i4;
                                                                                                                                                                                                                            if (decoder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (inputSurface5 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (encoder4 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (audioRecoder3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            resultWidth2 = bitrate4;
                                                                                                                                                                                                                            resultWidth3 = resultWidth5;
                                                                                                                                                                                                                            error = error2;
                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                            bitrate2 = bitrate3;
                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    } catch (Throwable th40) {
                                                                                                                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                                                                                                                        resultHeight2 = framerate;
                                                                                                                                                                                                                        e = th40;
                                                                                                                                                                                                                        videoTrackIndex = videoTrackIndex3;
                                                                                                                                                                                                                        endTime2 = endTime4;
                                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                                        bitrate7 = bitrate3;
                                                                                                                                                                                                                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                        FileLog.e(e);
                                                                                                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        bitrate2 = bitrate7;
                                                                                                                                                                                                                        resultWidth3 = resultWidth4;
                                                                                                                                                                                                                        error = true;
                                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                        resultWidth2 = resultHeight4;
                                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                } catch (Exception e51) {
                                                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                    e2 = e51;
                                                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                                    videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                                                    endTime2 = endTime3;
                                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                                }
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                audioIndex6 = audioIndex4;
                                                                                                                                                                                                                endTime4 = endTime3;
                                                                                                                                                                                                            }
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            audioTrackIndex = audioIndex5;
                                                                                                                                                                                                            audioIndex6 = audioIndex4;
                                                                                                                                                                                                            endTime4 = endTime3;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        audioBuffer2 = audioBuffer;
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        audioTrackIndex = audioIndex5;
                                                                                                                                                                                                        audioIndex6 = audioIndex4;
                                                                                                                                                                                                        endTime4 = endTime3;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    audioBuffer2 = audioBuffer;
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                            info3.size = mediaCodecVideoConvertor.extractor.readSampleData(audioBuffer, 0);
                                                                                                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                                                                            }
                                                                                                                                                                                            if (info3.size < 0) {
                                                                                                                                                                                            }
                                                                                                                                                                                            if (info3.size > 0) {
                                                                                                                                                                                            }
                                                                                                                                                                                            audioBuffer2 = audioBuffer;
                                                                                                                                                                                        } catch (Exception e52) {
                                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                            e2 = e52;
                                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                            endTime2 = endTime3;
                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                        }
                                                                                                                                                                                        info3 = info2;
                                                                                                                                                                                    } catch (Exception e53) {
                                                                                                                                                                                        encoder4 = encoder2;
                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                        e2 = e53;
                                                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                        endTime2 = endTime3;
                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                                    }
                                                                                                                                                                                    audioBuffer = audioBuffer2;
                                                                                                                                                                                } catch (Exception e54) {
                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                    encoder4 = encoder2;
                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                    e2 = e54;
                                                                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                    videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                    endTime2 = endTime3;
                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                        if (eof) {
                                                                                                                                                                            try {
                                                                                                                                                                                int inputBufIndex2 = decoder2.dequeueInputBuffer(2500L);
                                                                                                                                                                                if (inputBufIndex2 >= 0) {
                                                                                                                                                                                    decoder2.queueInputBuffer(inputBufIndex2, 0, 0, 0L, 4);
                                                                                                                                                                                    inputDone = true;
                                                                                                                                                                                }
                                                                                                                                                                            } catch (Exception e55) {
                                                                                                                                                                                i5 = framerate;
                                                                                                                                                                                encoder4 = encoder2;
                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                e2 = e55;
                                                                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                                endTime2 = endTime4;
                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                                if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                }
                                                                                                                                                                                StringBuilder sb322222222222222 = new StringBuilder();
                                                                                                                                                                                str = str3;
                                                                                                                                                                                sb322222222222222.append(str);
                                                                                                                                                                                sb322222222222222.append(bitrate5);
                                                                                                                                                                                sb322222222222222.append(" framerate: ");
                                                                                                                                                                                sb322222222222222.append(i5);
                                                                                                                                                                                sb322222222222222.append(" size: ");
                                                                                                                                                                                bitrate4 = resultHeight;
                                                                                                                                                                                sb322222222222222.append(bitrate4);
                                                                                                                                                                                sb322222222222222.append("x");
                                                                                                                                                                                resultWidth5 = resultWidth;
                                                                                                                                                                                sb322222222222222.append(resultWidth5);
                                                                                                                                                                                FileLog.e(sb322222222222222.toString());
                                                                                                                                                                                FileLog.e(e2);
                                                                                                                                                                                bitrate3 = bitrate5;
                                                                                                                                                                                error2 = true;
                                                                                                                                                                                i4 = i5;
                                                                                                                                                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                i4 = i4;
                                                                                                                                                                                if (decoder2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (inputSurface5 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (encoder4 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                if (audioRecoder3 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                                }
                                                                                                                                                                                resultWidth2 = bitrate4;
                                                                                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                                                                                error = error2;
                                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                bitrate2 = bitrate3;
                                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                    } catch (Exception e56) {
                                                                                                                                                                        videoIndex2 = videoIndex;
                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                        encoder4 = encoder2;
                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                        e2 = e56;
                                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                                        endTime2 = endTime3;
                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                    }
                                                                                                                                                                } catch (Throwable th41) {
                                                                                                                                                                    th3 = th41;
                                                                                                                                                                    j3 = endTime3;
                                                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                                                    resultHeight4 = resultHeight;
                                                                                                                                                                    i9 = framerate;
                                                                                                                                                                    e = th3;
                                                                                                                                                                    videoTrackIndex = videoTrackIndex3;
                                                                                                                                                                    endTime2 = j3;
                                                                                                                                                                    str = str3;
                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                    resultHeight2 = i9;
                                                                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                    FileLog.e(e);
                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                    }
                                                                                                                                                                    bitrate2 = bitrate7;
                                                                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                                                                    error = true;
                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            } else {
                                                                                                                                                                audioRecoder2 = audioRecoder;
                                                                                                                                                                audioTrackIndex = audioIndex5;
                                                                                                                                                                audioIndex6 = audioIndex4;
                                                                                                                                                                decoderInputBuffers2 = decoderInputBuffers;
                                                                                                                                                                audioEncoderDone3 = audioEncoderDone2;
                                                                                                                                                                endTime4 = endTime3;
                                                                                                                                                                videoIndex2 = videoIndex;
                                                                                                                                                                info3 = info2;
                                                                                                                                                            }
                                                                                                                                                            boolean decoderOutputAvailable5 = !decoderDone2;
                                                                                                                                                            boolean inputSurface8 = true;
                                                                                                                                                            videoTrackIndex = videoTrackIndex3;
                                                                                                                                                            endTime5 = endTime4;
                                                                                                                                                            decoderStatus = decoderOutputAvailable5;
                                                                                                                                                            encoderOutputBuffers3 = encoderOutputBuffers2;
                                                                                                                                                            int maxBufferSize5 = maxBufferSize2;
                                                                                                                                                            minPresentationTime = minPresentationTime6;
                                                                                                                                                            while (true) {
                                                                                                                                                                if (!decoderStatus || inputSurface8) {
                                                                                                                                                                    try {
                                                                                                                                                                        try {
                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                            if (!increaseTimeout) {
                                                                                                                                                                                decoderOutputAvailable = decoderStatus;
                                                                                                                                                                                encoderOutputAvailable = inputSurface8;
                                                                                                                                                                                j4 = 22000;
                                                                                                                                                                            } else {
                                                                                                                                                                                decoderOutputAvailable = decoderStatus;
                                                                                                                                                                                encoderOutputAvailable = inputSurface8;
                                                                                                                                                                                j4 = 2500;
                                                                                                                                                                            }
                                                                                                                                                                            boolean decoderOutputAvailable6 = decoderOutputAvailable;
                                                                                                                                                                            encoder3 = encoder2;
                                                                                                                                                                            try {
                                                                                                                                                                                encoderStatus = encoder3.dequeueOutputBuffer(info3, j4);
                                                                                                                                                                                if (encoderStatus != -1) {
                                                                                                                                                                                    outputSurface2 = outputSurface;
                                                                                                                                                                                    encoderOutputBuffers4 = encoderOutputBuffers3;
                                                                                                                                                                                    encoderStatus2 = encoderStatus;
                                                                                                                                                                                    encoderOutputAvailable = false;
                                                                                                                                                                                    endTime2 = endTime5;
                                                                                                                                                                                    minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                    h = h3;
                                                                                                                                                                                    str6 = str21;
                                                                                                                                                                                    w3 = w4;
                                                                                                                                                                                    str4 = str22;
                                                                                                                                                                                    str7 = str23;
                                                                                                                                                                                    str5 = str24;
                                                                                                                                                                                } else if (encoderStatus == -3) {
                                                                                                                                                                                    try {
                                                                                                                                                                                        try {
                                                                                                                                                                                            outputSurface2 = outputSurface;
                                                                                                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                                                                                try {
                                                                                                                                                                                                    ByteBuffer[] encoderOutputBuffers10 = encoder3.getOutputBuffers();
                                                                                                                                                                                                    encoderOutputBuffers4 = encoderOutputBuffers10;
                                                                                                                                                                                                    encoderStatus2 = encoderStatus;
                                                                                                                                                                                                    endTime2 = endTime5;
                                                                                                                                                                                                    minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                                    endTime5 = h3;
                                                                                                                                                                                                    str6 = str21;
                                                                                                                                                                                                    w3 = w4;
                                                                                                                                                                                                    str4 = str22;
                                                                                                                                                                                                    str7 = str23;
                                                                                                                                                                                                    str5 = str24;
                                                                                                                                                                                                    h = endTime5;
                                                                                                                                                                                                } catch (Exception e57) {
                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                    e2 = e57;
                                                                                                                                                                                                    videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                    encoder4 = encoder3;
                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                    endTime2 = endTime5;
                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    StringBuilder sb3222222222222222 = new StringBuilder();
                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                    sb3222222222222222.append(str);
                                                                                                                                                                                                    sb3222222222222222.append(bitrate5);
                                                                                                                                                                                                    sb3222222222222222.append(" framerate: ");
                                                                                                                                                                                                    sb3222222222222222.append(i5);
                                                                                                                                                                                                    sb3222222222222222.append(" size: ");
                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                    sb3222222222222222.append(bitrate4);
                                                                                                                                                                                                    sb3222222222222222.append("x");
                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                    sb3222222222222222.append(resultWidth5);
                                                                                                                                                                                                    FileLog.e(sb3222222222222222.toString());
                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            } else {
                                                                                                                                                                                                encoderOutputBuffers4 = encoderOutputBuffers3;
                                                                                                                                                                                                encoderStatus2 = encoderStatus;
                                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                                minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                                h = h3;
                                                                                                                                                                                                str6 = str21;
                                                                                                                                                                                                w3 = w4;
                                                                                                                                                                                                str4 = str22;
                                                                                                                                                                                                str7 = str23;
                                                                                                                                                                                                str5 = str24;
                                                                                                                                                                                            }
                                                                                                                                                                                        } catch (Throwable th42) {
                                                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                                                            resultHeight2 = framerate;
                                                                                                                                                                                            e = th42;
                                                                                                                                                                                            endTime2 = endTime5;
                                                                                                                                                                                            str = str3;
                                                                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                            FileLog.e(e);
                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                                                                            error = true;
                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    } catch (Exception e58) {
                                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                                        outputSurface3 = outputSurface;
                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                        e2 = e58;
                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                        encoder4 = encoder3;
                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                    }
                                                                                                                                                                                } else {
                                                                                                                                                                                    outputSurface2 = outputSurface;
                                                                                                                                                                                    if (encoderStatus == -2) {
                                                                                                                                                                                        MediaFormat newFormat4 = encoder3.getOutputFormat();
                                                                                                                                                                                        if (videoTrackIndex != -5 || newFormat4 == null) {
                                                                                                                                                                                            minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                            str9 = str22;
                                                                                                                                                                                            str10 = str23;
                                                                                                                                                                                            str8 = str24;
                                                                                                                                                                                        } else {
                                                                                                                                                                                            minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                            int videoTrackIndex7 = mediaCodecVideoConvertor.mediaMuxer.addTrack(newFormat4, false);
                                                                                                                                                                                            str9 = str22;
                                                                                                                                                                                            try {
                                                                                                                                                                                                if (!newFormat4.containsKey(str9) || newFormat4.getInteger(str9) != 1) {
                                                                                                                                                                                                    str10 = str23;
                                                                                                                                                                                                    str8 = str24;
                                                                                                                                                                                                    videoTrackIndex = videoTrackIndex7;
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    String str25 = str24;
                                                                                                                                                                                                    ByteBuffer spsBuff2 = newFormat4.getByteBuffer(str25);
                                                                                                                                                                                                    String str26 = str23;
                                                                                                                                                                                                    try {
                                                                                                                                                                                                        ByteBuffer ppsBuff2 = newFormat4.getByteBuffer(str26);
                                                                                                                                                                                                        prependHeaderSize = spsBuff2.limit() + ppsBuff2.limit();
                                                                                                                                                                                                        videoTrackIndex = videoTrackIndex7;
                                                                                                                                                                                                        str10 = str26;
                                                                                                                                                                                                        str8 = str25;
                                                                                                                                                                                                    } catch (Exception e59) {
                                                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                        e2 = e59;
                                                                                                                                                                                                        encoder4 = encoder3;
                                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex7;
                                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        StringBuilder sb32222222222222222 = new StringBuilder();
                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                        sb32222222222222222.append(str);
                                                                                                                                                                                                        sb32222222222222222.append(bitrate5);
                                                                                                                                                                                                        sb32222222222222222.append(" framerate: ");
                                                                                                                                                                                                        sb32222222222222222.append(i5);
                                                                                                                                                                                                        sb32222222222222222.append(" size: ");
                                                                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                                                                        sb32222222222222222.append(bitrate4);
                                                                                                                                                                                                        sb32222222222222222.append("x");
                                                                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                                                                        sb32222222222222222.append(resultWidth5);
                                                                                                                                                                                                        FileLog.e(sb32222222222222222.toString());
                                                                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                                                                        error2 = true;
                                                                                                                                                                                                        i4 = i5;
                                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                        i4 = i4;
                                                                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                                                                        error = error2;
                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } catch (Throwable th43) {
                                                                                                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                                                                                                        resultHeight2 = framerate;
                                                                                                                                                                                                        e = th43;
                                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                                        videoTrackIndex = videoTrackIndex7;
                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                        bitrate7 = bitrate3;
                                                                                                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                                                                                                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                        FileLog.e(e);
                                                                                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        bitrate2 = bitrate7;
                                                                                                                                                                                                        resultWidth3 = resultWidth4;
                                                                                                                                                                                                        error = true;
                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                        resultWidth2 = resultHeight4;
                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            } catch (Exception e60) {
                                                                                                                                                                                                i5 = framerate;
                                                                                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                e2 = e60;
                                                                                                                                                                                                encoder4 = encoder3;
                                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex7;
                                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                                            } catch (Throwable th44) {
                                                                                                                                                                                                resultWidth4 = resultWidth;
                                                                                                                                                                                                resultHeight2 = framerate;
                                                                                                                                                                                                e = th44;
                                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                                videoTrackIndex = videoTrackIndex7;
                                                                                                                                                                                                str = str3;
                                                                                                                                                                                                bitrate7 = bitrate3;
                                                                                                                                                                                                resultHeight4 = resultHeight;
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                        encoderOutputBuffers4 = encoderOutputBuffers3;
                                                                                                                                                                                        encoderStatus2 = encoderStatus;
                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                        str4 = str9;
                                                                                                                                                                                        h = h3;
                                                                                                                                                                                        str6 = str21;
                                                                                                                                                                                        w3 = w4;
                                                                                                                                                                                        str7 = str10;
                                                                                                                                                                                        str5 = str8;
                                                                                                                                                                                    } else {
                                                                                                                                                                                        minPresentationTime2 = minPresentationTime;
                                                                                                                                                                                        String str27 = str22;
                                                                                                                                                                                        String str28 = str23;
                                                                                                                                                                                        String str29 = str24;
                                                                                                                                                                                        try {
                                                                                                                                                                                            if (encoderStatus < 0) {
                                                                                                                                                                                                throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                                                                                                                                                                                            }
                                                                                                                                                                                            try {
                                                                                                                                                                                                str4 = str27;
                                                                                                                                                                                                ByteBuffer encodedData2 = Build.VERSION.SDK_INT < 21 ? encoderOutputBuffers3[encoderStatus] : encoder3.getOutputBuffer(encoderStatus);
                                                                                                                                                                                                if (encodedData2 == null) {
                                                                                                                                                                                                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                                                                                                                                                                                                }
                                                                                                                                                                                                encoderOutputBuffers4 = encoderOutputBuffers3;
                                                                                                                                                                                                try {
                                                                                                                                                                                                    if (info3.size > 1) {
                                                                                                                                                                                                        try {
                                                                                                                                                                                                            if ((info3.flags & 2) == 0) {
                                                                                                                                                                                                                if (prependHeaderSize != 0 && (info3.flags & 1) != 0) {
                                                                                                                                                                                                                    info3.offset += prependHeaderSize;
                                                                                                                                                                                                                    info3.size -= prependHeaderSize;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (!firstEncode8 || (info3.flags & 1) == 0) {
                                                                                                                                                                                                                    endTime2 = endTime5;
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    if (info3.size > 100) {
                                                                                                                                                                                                                        encodedData2.position(info3.offset);
                                                                                                                                                                                                                        byte[] temp3 = new byte[100];
                                                                                                                                                                                                                        encodedData2.get(temp3);
                                                                                                                                                                                                                        int nalCount3 = 0;
                                                                                                                                                                                                                        int a3 = 0;
                                                                                                                                                                                                                        long endTime6 = endTime5;
                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                            endTime2 = endTime6;
                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                    if (a3 >= temp3.length - 4) {
                                                                                                                                                                                                                                        break;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (temp3[a3] == 0 && temp3[a3 + 1] == 0 && temp3[a3 + 2] == 0 && temp3[a3 + 3] == 1) {
                                                                                                                                                                                                                                        int nalCount4 = nalCount3 + 1;
                                                                                                                                                                                                                                        if (nalCount4 > 1) {
                                                                                                                                                                                                                                            info3.offset += a3;
                                                                                                                                                                                                                                            info3.size -= a3;
                                                                                                                                                                                                                                            break;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        nalCount3 = nalCount4;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    a3++;
                                                                                                                                                                                                                                    endTime6 = endTime2;
                                                                                                                                                                                                                                } catch (Exception e61) {
                                                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                    e2 = e61;
                                                                                                                                                                                                                                    videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                                                    encoder4 = encoder3;
                                                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                                                    if ((e2 instanceof IllegalStateException) && !increaseTimeout) {
                                                                                                                                                                                                                                        repeatWithIncreasedTimeout2 = true;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    StringBuilder sb322222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                                                    sb322222222222222222.append(str);
                                                                                                                                                                                                                                    sb322222222222222222.append(bitrate5);
                                                                                                                                                                                                                                    sb322222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                    sb322222222222222222.append(i5);
                                                                                                                                                                                                                                    sb322222222222222222.append(" size: ");
                                                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                                                    sb322222222222222222.append(bitrate4);
                                                                                                                                                                                                                                    sb322222222222222222.append("x");
                                                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                                                    sb322222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                    FileLog.e(sb322222222222222222.toString());
                                                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                                                        decoder2.stop();
                                                                                                                                                                                                                                        decoder2.release();
                                                                                                                                                                                                                                        i4 = i4;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            } catch (Throwable th45) {
                                                                                                                                                                                                                                resultWidth4 = resultWidth;
                                                                                                                                                                                                                                resultHeight4 = resultHeight;
                                                                                                                                                                                                                                resultHeight2 = framerate;
                                                                                                                                                                                                                                e = th45;
                                                                                                                                                                                                                                str = str3;
                                                                                                                                                                                                                                bitrate7 = bitrate3;
                                                                                                                                                                                                                                FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                                FileLog.e(e);
                                                                                                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                bitrate2 = bitrate7;
                                                                                                                                                                                                                                resultWidth3 = resultWidth4;
                                                                                                                                                                                                                                error = true;
                                                                                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                resultWidth2 = resultHeight4;
                                                                                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    firstEncode8 = false;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                long availableSize3 = mediaCodecVideoConvertor.mediaMuxer.writeSampleData(videoTrackIndex, encodedData2, info3, true);
                                                                                                                                                                                                                if (availableSize3 == 0) {
                                                                                                                                                                                                                    encoderStatus3 = encoderStatus;
                                                                                                                                                                                                                } else if (mediaCodecVideoConvertor.callback != null) {
                                                                                                                                                                                                                    long currentPts5 = info3.presentationTimeUs - j > currentPts ? info3.presentationTimeUs - j : currentPts;
                                                                                                                                                                                                                    encoderStatus3 = encoderStatus;
                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                        mediaCodecVideoConvertor.callback.didWriteData(availableSize3, (((float) currentPts5) / 1000.0f) / durationS);
                                                                                                                                                                                                                        currentPts = currentPts5;
                                                                                                                                                                                                                    } catch (Exception e62) {
                                                                                                                                                                                                                        i5 = framerate;
                                                                                                                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                                        encoder4 = encoder3;
                                                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                                                        e2 = e62;
                                                                                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        StringBuilder sb3222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                                        sb3222222222222222222.append(str);
                                                                                                                                                                                                                        sb3222222222222222222.append(bitrate5);
                                                                                                                                                                                                                        sb3222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                        sb3222222222222222222.append(i5);
                                                                                                                                                                                                                        sb3222222222222222222.append(" size: ");
                                                                                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                                                                                        sb3222222222222222222.append(bitrate4);
                                                                                                                                                                                                                        sb3222222222222222222.append("x");
                                                                                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                                                                                        sb3222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                        FileLog.e(sb3222222222222222222.toString());
                                                                                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                                                                                        error2 = true;
                                                                                                                                                                                                                        i4 = i5;
                                                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                        i4 = i4;
                                                                                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                                                                                        error = error2;
                                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    encoderStatus3 = encoderStatus;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                firstEncode = firstEncode8;
                                                                                                                                                                                                                h2 = h3;
                                                                                                                                                                                                                str6 = str21;
                                                                                                                                                                                                                w3 = w4;
                                                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                encoderStatus3 = encoderStatus;
                                                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                                                if (videoTrackIndex == -5) {
                                                                                                                                                                                                                    byte[] csd2 = new byte[info3.size];
                                                                                                                                                                                                                    encodedData2.limit(info3.offset + info3.size);
                                                                                                                                                                                                                    encodedData2.position(info3.offset);
                                                                                                                                                                                                                    encodedData2.get(csd2);
                                                                                                                                                                                                                    ByteBuffer sps4 = null;
                                                                                                                                                                                                                    ByteBuffer pps2 = null;
                                                                                                                                                                                                                    byte b2 = 1;
                                                                                                                                                                                                                    int a4 = info3.size - 1;
                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                        if (a4 < 0 || a4 <= 3) {
                                                                                                                                                                                                                            break;
                                                                                                                                                                                                                        } else if (csd2[a4] == b2 && csd2[a4 - 1] == 0 && csd2[a4 - 2] == 0 && csd2[a4 - 3] == 0) {
                                                                                                                                                                                                                            sps4 = ByteBuffer.allocate(a4 - 3);
                                                                                                                                                                                                                            pps2 = ByteBuffer.allocate(info3.size - (a4 - 3));
                                                                                                                                                                                                                            sps4.put(csd2, 0, a4 - 3).position(0);
                                                                                                                                                                                                                            pps2.put(csd2, a4 - 3, info3.size - (a4 - 3)).position(0);
                                                                                                                                                                                                                            break;
                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                            a4--;
                                                                                                                                                                                                                            b2 = 1;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    int h4 = h3;
                                                                                                                                                                                                                    str6 = str21;
                                                                                                                                                                                                                    w3 = w4;
                                                                                                                                                                                                                    MediaFormat newFormat5 = MediaFormat.createVideoFormat(str6, w3, h4);
                                                                                                                                                                                                                    if (sps4 == null || pps2 == null) {
                                                                                                                                                                                                                        newFormat = newFormat5;
                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                        newFormat = newFormat5;
                                                                                                                                                                                                                        newFormat.setByteBuffer(str29, sps4);
                                                                                                                                                                                                                        newFormat.setByteBuffer(str28, pps2);
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    boolean z2 = firstEncode8;
                                                                                                                                                                                                                    videoTrackIndex5 = mediaCodecVideoConvertor.mediaMuxer.addTrack(newFormat, false);
                                                                                                                                                                                                                    firstEncode = z2;
                                                                                                                                                                                                                    h2 = h4;
                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                    i13 = h3;
                                                                                                                                                                                                                    str6 = str21;
                                                                                                                                                                                                                    w3 = w4;
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                            outputDone2 = (info3.flags & 4) == 0;
                                                                                                                                                                                                            encoderStatus2 = encoderStatus3;
                                                                                                                                                                                                            encoder3.releaseOutputBuffer(encoderStatus2, false);
                                                                                                                                                                                                            videoTrackIndex = videoTrackIndex5;
                                                                                                                                                                                                            firstEncode8 = firstEncode;
                                                                                                                                                                                                            str7 = str28;
                                                                                                                                                                                                            h = h2;
                                                                                                                                                                                                            str5 = str29;
                                                                                                                                                                                                        } catch (Exception e63) {
                                                                                                                                                                                                            endTime2 = endTime5;
                                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                            e2 = e63;
                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                            encoder4 = encoder3;
                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                        } catch (Throwable th46) {
                                                                                                                                                                                                            th4 = th46;
                                                                                                                                                                                                            endTime2 = endTime5;
                                                                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                                                                            i10 = framerate;
                                                                                                                                                                                                            e = th4;
                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                                                                                            resultHeight2 = i10;
                                                                                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                            FileLog.e(e);
                                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                                                                                            error = true;
                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        encoderStatus3 = encoderStatus;
                                                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                                                        i13 = h3;
                                                                                                                                                                                                        str6 = str21;
                                                                                                                                                                                                        w3 = w4;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    outputDone2 = (info3.flags & 4) == 0;
                                                                                                                                                                                                    encoderStatus2 = encoderStatus3;
                                                                                                                                                                                                    encoder3.releaseOutputBuffer(encoderStatus2, false);
                                                                                                                                                                                                    videoTrackIndex = videoTrackIndex5;
                                                                                                                                                                                                    firstEncode8 = firstEncode;
                                                                                                                                                                                                    str7 = str28;
                                                                                                                                                                                                    h = h2;
                                                                                                                                                                                                    str5 = str29;
                                                                                                                                                                                                } catch (Exception e64) {
                                                                                                                                                                                                    i5 = framerate;
                                                                                                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                    e2 = e64;
                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                    encoder4 = encoder3;
                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    StringBuilder sb32222222222222222222 = new StringBuilder();
                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                    sb32222222222222222222.append(str);
                                                                                                                                                                                                    sb32222222222222222222.append(bitrate5);
                                                                                                                                                                                                    sb32222222222222222222.append(" framerate: ");
                                                                                                                                                                                                    sb32222222222222222222.append(i5);
                                                                                                                                                                                                    sb32222222222222222222.append(" size: ");
                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                    sb32222222222222222222.append(bitrate4);
                                                                                                                                                                                                    sb32222222222222222222.append("x");
                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                    sb32222222222222222222.append(resultWidth5);
                                                                                                                                                                                                    FileLog.e(sb32222222222222222222.toString());
                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                } catch (Throwable th47) {
                                                                                                                                                                                                    resultHeight2 = framerate;
                                                                                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                                                                                    resultHeight4 = resultHeight;
                                                                                                                                                                                                    e = th47;
                                                                                                                                                                                                    videoTrackIndex = videoTrackIndex5;
                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                    FileLog.e(e);
                                                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    bitrate2 = bitrate7;
                                                                                                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                                                                                                    error = true;
                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                                firstEncode = firstEncode8;
                                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                h2 = i13;
                                                                                                                                                                                            } catch (Exception e65) {
                                                                                                                                                                                                i5 = framerate;
                                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                e2 = e65;
                                                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                encoder4 = encoder3;
                                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                                            }
                                                                                                                                                                                        } catch (Exception e66) {
                                                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                            e2 = e66;
                                                                                                                                                                                            inputSurface5 = str28;
                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                            encoder4 = encoder3;
                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                            i5 = str29;
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                                if (encoderStatus2 == -1) {
                                                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                                                    str23 = str7;
                                                                                                                                                                                    encoder2 = encoder3;
                                                                                                                                                                                    w4 = w3;
                                                                                                                                                                                    h3 = h;
                                                                                                                                                                                    str21 = str6;
                                                                                                                                                                                    str24 = str5;
                                                                                                                                                                                    inputSurface8 = encoderOutputAvailable;
                                                                                                                                                                                    decoderStatus = decoderOutputAvailable6;
                                                                                                                                                                                    encoderOutputBuffers3 = encoderOutputBuffers4;
                                                                                                                                                                                    minPresentationTime = minPresentationTime2;
                                                                                                                                                                                    str22 = str4;
                                                                                                                                                                                    endTime5 = endTime2;
                                                                                                                                                                                    j = startTime;
                                                                                                                                                                                } else {
                                                                                                                                                                                    if (!decoderDone2) {
                                                                                                                                                                                        try {
                                                                                                                                                                                            try {
                                                                                                                                                                                                int decoderStatus2 = decoder2.dequeueOutputBuffer(info3, 2500L);
                                                                                                                                                                                                if (decoderStatus2 != -1) {
                                                                                                                                                                                                    if (decoderStatus2 != -3) {
                                                                                                                                                                                                        if (decoderStatus2 != -2) {
                                                                                                                                                                                                            if (decoderStatus2 < 0) {
                                                                                                                                                                                                                break loop4;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            boolean doRender3 = info3.size != 0;
                                                                                                                                                                                                            w4 = w3;
                                                                                                                                                                                                            long originalPresentationTime = info3.presentationTimeUs;
                                                                                                                                                                                                            if (endTime2 > 0 && originalPresentationTime >= endTime2) {
                                                                                                                                                                                                                inputDone = true;
                                                                                                                                                                                                                decoderDone2 = true;
                                                                                                                                                                                                                doRender3 = false;
                                                                                                                                                                                                                info3.flags |= 4;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (avatarStartTime2 >= 0) {
                                                                                                                                                                                                                try {
                                                                                                                                                                                                                    if ((info3.flags & 4) != 0) {
                                                                                                                                                                                                                        h3 = h;
                                                                                                                                                                                                                        j5 = startTime;
                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                            doRender = doRender3;
                                                                                                                                                                                                                            str24 = str5;
                                                                                                                                                                                                                            i11 = framerate;
                                                                                                                                                                                                                        } catch (Exception e67) {
                                                                                                                                                                                                                            e4 = e67;
                                                                                                                                                                                                                            i11 = framerate;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                            flushed = false;
                                                                                                                                                                                                                            if (Math.abs(avatarStartTime2 - j5) > MediaController.VIDEO_BITRATE_480 / i11) {
                                                                                                                                                                                                                                if (j5 > 0) {
                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.seekTo(j5, 0);
                                                                                                                                                                                                                                            str23 = str7;
                                                                                                                                                                                                                                            videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                                                        } catch (Throwable th48) {
                                                                                                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                                                                                                            e = th48;
                                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                                                                                                                            resultHeight2 = i11;
                                                                                                                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                                            FileLog.e(e);
                                                                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                                                                                                                            error = true;
                                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    } catch (Exception e68) {
                                                                                                                                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                        e2 = e68;
                                                                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                                                        encoder4 = encoder3;
                                                                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                                                                        i5 = i11;
                                                                                                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        StringBuilder sb322222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                                                        sb322222222222222222222.append(str);
                                                                                                                                                                                                                                        sb322222222222222222222.append(bitrate5);
                                                                                                                                                                                                                                        sb322222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                        sb322222222222222222222.append(i5);
                                                                                                                                                                                                                                        sb322222222222222222222.append(" size: ");
                                                                                                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                                                                                                        sb322222222222222222222.append(bitrate4);
                                                                                                                                                                                                                                        sb322222222222222222222.append("x");
                                                                                                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                                                                                                        sb322222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                        FileLog.e(sb322222222222222222222.toString());
                                                                                                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                                                                                                        error2 = true;
                                                                                                                                                                                                                                        i4 = i5;
                                                                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                        i4 = i4;
                                                                                                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                        error = error2;
                                                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                } else {
                                                                                                                                                                                                                                    str23 = str7;
                                                                                                                                                                                                                                    videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.seekTo(0L, 0);
                                                                                                                                                                                                                                        } catch (Throwable th49) {
                                                                                                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                                                                                                            e = th49;
                                                                                                                                                                                                                                            videoTrackIndex = videoTrackIndex4;
                                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                                                                                                                            resultHeight2 = i11;
                                                                                                                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                                            FileLog.e(e);
                                                                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                                                                                                                            error = true;
                                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    } catch (Exception e69) {
                                                                                                                                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                        audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                        e2 = e69;
                                                                                                                                                                                                                                        encoder4 = encoder3;
                                                                                                                                                                                                                                        decoder2 = decoder2;
                                                                                                                                                                                                                                        videoTrackIndex5 = videoTrackIndex4;
                                                                                                                                                                                                                                        bitrate5 = bitrate3;
                                                                                                                                                                                                                                        inputSurface5 = inputSurface2;
                                                                                                                                                                                                                                        i5 = i11;
                                                                                                                                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        StringBuilder sb3222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                        str = str3;
                                                                                                                                                                                                                                        sb3222222222222222222222.append(str);
                                                                                                                                                                                                                                        sb3222222222222222222222.append(bitrate5);
                                                                                                                                                                                                                                        sb3222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                        sb3222222222222222222222.append(i5);
                                                                                                                                                                                                                                        sb3222222222222222222222.append(" size: ");
                                                                                                                                                                                                                                        bitrate4 = resultHeight;
                                                                                                                                                                                                                                        sb3222222222222222222222.append(bitrate4);
                                                                                                                                                                                                                                        sb3222222222222222222222.append("x");
                                                                                                                                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                                                                                                                                        sb3222222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                        FileLog.e(sb3222222222222222222222.toString());
                                                                                                                                                                                                                                        FileLog.e(e2);
                                                                                                                                                                                                                                        bitrate3 = bitrate5;
                                                                                                                                                                                                                                        error2 = true;
                                                                                                                                                                                                                                        i4 = i5;
                                                                                                                                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                        i4 = i4;
                                                                                                                                                                                                                                        if (decoder2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (inputSurface5 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (encoder4 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                        error = error2;
                                                                                                                                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                        bitrate2 = bitrate3;
                                                                                                                                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                additionalPresentationTime = minPresentationTime2 + frameDelta2;
                                                                                                                                                                                                                                long endTime7 = avatarStartTime2;
                                                                                                                                                                                                                                avatarStartTime2 = -1;
                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                    info3.flags &= -5;
                                                                                                                                                                                                                                    decoder2.flush();
                                                                                                                                                                                                                                    flushed2 = true;
                                                                                                                                                                                                                                    endTime2 = endTime7;
                                                                                                                                                                                                                                    inputDone = false;
                                                                                                                                                                                                                                    doRender2 = false;
                                                                                                                                                                                                                                    decoderDone2 = false;
                                                                                                                                                                                                                                    i11 = i11;
                                                                                                                                                                                                                                    if (lastFramePts > 0 && info3.presentationTimeUs - lastFramePts < frameDeltaFroSkipFrames && (info3.flags & 4) == 0) {
                                                                                                                                                                                                                                        doRender2 = false;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    trueStartTime = avatarStartTime2 >= 0 ? avatarStartTime2 : j5;
                                                                                                                                                                                                                                    if (trueStartTime <= 0 && videoTime == -1) {
                                                                                                                                                                                                                                        if (originalPresentationTime < trueStartTime) {
                                                                                                                                                                                                                                            doRender2 = false;
                                                                                                                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                                                                                FileLog.d("drop frame startTime = " + trueStartTime + " present time = " + info3.presentationTimeUs);
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                            videoTime = info3.presentationTimeUs;
                                                                                                                                                                                                                                            if (minPresentationTime2 != -2147483648L) {
                                                                                                                                                                                                                                                additionalPresentationTime -= videoTime;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (flushed2) {
                                                                                                                                                                                                                                        videoTime = -1;
                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                        if (avatarStartTime2 == -1 && additionalPresentationTime != 0) {
                                                                                                                                                                                                                                            info3.presentationTimeUs += additionalPresentationTime;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                            decoder2.releaseOutputBuffer(decoderStatus2, doRender2);
                                                                                                                                                                                                                                        } catch (Exception e70) {
                                                                                                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                            e2 = e70;
                                                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                                                            encoder4 = encoder3;
                                                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex4;
                                                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                                                            i5 = i11;
                                                                                                                                                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            StringBuilder sb32222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                                            sb32222222222222222222222.append(str);
                                                                                                                                                                                                                                            sb32222222222222222222222.append(bitrate5);
                                                                                                                                                                                                                                            sb32222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                            sb32222222222222222222222.append(i5);
                                                                                                                                                                                                                                            sb32222222222222222222222.append(" size: ");
                                                                                                                                                                                                                                            bitrate4 = resultHeight;
                                                                                                                                                                                                                                            sb32222222222222222222222.append(bitrate4);
                                                                                                                                                                                                                                            sb32222222222222222222222.append("x");
                                                                                                                                                                                                                                            resultWidth5 = resultWidth;
                                                                                                                                                                                                                                            sb32222222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                            FileLog.e(sb32222222222222222222222.toString());
                                                                                                                                                                                                                                            FileLog.e(e2);
                                                                                                                                                                                                                                            bitrate3 = bitrate5;
                                                                                                                                                                                                                                            error2 = true;
                                                                                                                                                                                                                                            i4 = i5;
                                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                            i4 = i4;
                                                                                                                                                                                                                                            if (decoder2 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (inputSurface5 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (encoder4 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            if (audioRecoder3 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            resultWidth2 = bitrate4;
                                                                                                                                                                                                                                            resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                            error = error2;
                                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                            bitrate2 = bitrate3;
                                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (doRender2) {
                                                                                                                                                                                                                                        lastFramePts = info3.presentationTimeUs;
                                                                                                                                                                                                                                        if (avatarStartTime2 >= 0) {
                                                                                                                                                                                                                                            minPresentationTime5 = Math.max(minPresentationTime2, info3.presentationTimeUs);
                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                            minPresentationTime5 = minPresentationTime2;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        boolean errorWait = false;
                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                            outputSurface2.awaitNewImage();
                                                                                                                                                                                                                                        } catch (Exception e71) {
                                                                                                                                                                                                                                            errorWait = true;
                                                                                                                                                                                                                                            FileLog.e(e71);
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        if (!errorWait) {
                                                                                                                                                                                                                                            outputSurface2.drawImage();
                                                                                                                                                                                                                                            minPresentationTime3 = minPresentationTime5;
                                                                                                                                                                                                                                            inputSurface3 = inputSurface2;
                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                inputSurface3.setPresentationTime(info3.presentationTimeUs * 1000);
                                                                                                                                                                                                                                                inputSurface3.swapBuffers();
                                                                                                                                                                                                                                            } catch (Exception e72) {
                                                                                                                                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                                e2 = e72;
                                                                                                                                                                                                                                                inputSurface5 = inputSurface3;
                                                                                                                                                                                                                                                encoder4 = encoder3;
                                                                                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex4;
                                                                                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                                                                                                i5 = i11;
                                                                                                                                                                                                                                                if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                StringBuilder sb322222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                                str = str3;
                                                                                                                                                                                                                                                sb322222222222222222222222.append(str);
                                                                                                                                                                                                                                                sb322222222222222222222222.append(bitrate5);
                                                                                                                                                                                                                                                sb322222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                                sb322222222222222222222222.append(i5);
                                                                                                                                                                                                                                                sb322222222222222222222222.append(" size: ");
                                                                                                                                                                                                                                                bitrate4 = resultHeight;
                                                                                                                                                                                                                                                sb322222222222222222222222.append(bitrate4);
                                                                                                                                                                                                                                                sb322222222222222222222222.append("x");
                                                                                                                                                                                                                                                resultWidth5 = resultWidth;
                                                                                                                                                                                                                                                sb322222222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                                FileLog.e(sb322222222222222222222222.toString());
                                                                                                                                                                                                                                                FileLog.e(e2);
                                                                                                                                                                                                                                                bitrate3 = bitrate5;
                                                                                                                                                                                                                                                error2 = true;
                                                                                                                                                                                                                                                i4 = i5;
                                                                                                                                                                                                                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                                i4 = i4;
                                                                                                                                                                                                                                                if (decoder2 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (inputSurface5 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (encoder4 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                if (audioRecoder3 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                resultWidth2 = bitrate4;
                                                                                                                                                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                                error = error2;
                                                                                                                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                                bitrate2 = bitrate3;
                                                                                                                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        } else {
                                                                                                                                                                                                                                            minPresentationTime3 = minPresentationTime5;
                                                                                                                                                                                                                                            inputSurface3 = inputSurface2;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                        inputSurface3 = inputSurface2;
                                                                                                                                                                                                                                        minPresentationTime3 = minPresentationTime2;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if ((info3.flags & 4) != 0) {
                                                                                                                                                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                                                                            FileLog.d("decoder stream end");
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        encoder3.signalEndOfInputStream();
                                                                                                                                                                                                                                        decoderStatus = false;
                                                                                                                                                                                                                                        endTime5 = endTime2;
                                                                                                                                                                                                                                    } else {
                                                                                                                                                                                                                                        decoderStatus = decoderOutputAvailable6;
                                                                                                                                                                                                                                        endTime5 = endTime2;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                } catch (Exception e73) {
                                                                                                                                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                                                                                                                                    audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                                    e2 = e73;
                                                                                                                                                                                                                                    endTime2 = endTime7;
                                                                                                                                                                                                                                    encoder4 = encoder3;
                                                                                                                                                                                                                                    decoder2 = decoder2;
                                                                                                                                                                                                                                    videoTrackIndex5 = videoTrackIndex4;
                                                                                                                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                                                                                                                    i5 = i11;
                                                                                                                                                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    StringBuilder sb3222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(str);
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(bitrate5);
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(i5);
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(" size: ");
                                                                                                                                                                                                                                    bitrate4 = resultHeight;
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(bitrate4);
                                                                                                                                                                                                                                    sb3222222222222222222222222.append("x");
                                                                                                                                                                                                                                    resultWidth5 = resultWidth;
                                                                                                                                                                                                                                    sb3222222222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                                    FileLog.e(sb3222222222222222222222222.toString());
                                                                                                                                                                                                                                    FileLog.e(e2);
                                                                                                                                                                                                                                    bitrate3 = bitrate5;
                                                                                                                                                                                                                                    error2 = true;
                                                                                                                                                                                                                                    i4 = i5;
                                                                                                                                                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                                    i4 = i4;
                                                                                                                                                                                                                                    if (decoder2 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (inputSurface5 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (encoder4 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    resultWidth2 = bitrate4;
                                                                                                                                                                                                                                    resultWidth3 = resultWidth5;
                                                                                                                                                                                                                                    error = error2;
                                                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                    bitrate2 = bitrate3;
                                                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                } catch (Throwable th50) {
                                                                                                                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                                                                                                                    resultHeight4 = resultHeight;
                                                                                                                                                                                                                                    e = th50;
                                                                                                                                                                                                                                    endTime2 = endTime7;
                                                                                                                                                                                                                                    videoTrackIndex = videoTrackIndex4;
                                                                                                                                                                                                                                    str = str3;
                                                                                                                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                                                                                                                    resultHeight2 = i11;
                                                                                                                                                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                                    FileLog.e(e);
                                                                                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    bitrate2 = bitrate7;
                                                                                                                                                                                                                                    resultWidth3 = resultWidth4;
                                                                                                                                                                                                                                    error = true;
                                                                                                                                                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                                    resultWidth2 = resultHeight4;
                                                                                                                                                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                                str23 = str7;
                                                                                                                                                                                                                                videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                                                i12 = i11;
                                                                                                                                                                                                                                flushed2 = flushed;
                                                                                                                                                                                                                                doRender2 = doRender;
                                                                                                                                                                                                                                i11 = i12;
                                                                                                                                                                                                                                if (lastFramePts > 0) {
                                                                                                                                                                                                                                    doRender2 = false;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                trueStartTime = avatarStartTime2 >= 0 ? avatarStartTime2 : j5;
                                                                                                                                                                                                                                if (trueStartTime <= 0) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if (flushed2) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if (doRender2) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                if ((info3.flags & 4) != 0) {
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        } catch (Exception e74) {
                                                                                                                                                                                                                            e4 = e74;
                                                                                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                                                            e2 = e4;
                                                                                                                                                                                                                            encoder4 = encoder3;
                                                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                                                            i5 = i11;
                                                                                                                                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            StringBuilder sb32222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                            sb32222222222222222222222222.append(str);
                                                                                                                                                                                                                            sb32222222222222222222222222.append(bitrate5);
                                                                                                                                                                                                                            sb32222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                            sb32222222222222222222222222.append(i5);
                                                                                                                                                                                                                            sb32222222222222222222222222.append(" size: ");
                                                                                                                                                                                                                            bitrate4 = resultHeight;
                                                                                                                                                                                                                            sb32222222222222222222222222.append(bitrate4);
                                                                                                                                                                                                                            sb32222222222222222222222222.append("x");
                                                                                                                                                                                                                            resultWidth5 = resultWidth;
                                                                                                                                                                                                                            sb32222222222222222222222222.append(resultWidth5);
                                                                                                                                                                                                                            FileLog.e(sb32222222222222222222222222.toString());
                                                                                                                                                                                                                            FileLog.e(e2);
                                                                                                                                                                                                                            bitrate3 = bitrate5;
                                                                                                                                                                                                                            error2 = true;
                                                                                                                                                                                                                            i4 = i5;
                                                                                                                                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                                                                                            i4 = i4;
                                                                                                                                                                                                                            if (decoder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (inputSurface5 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (encoder4 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            if (audioRecoder3 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            resultWidth2 = bitrate4;
                                                                                                                                                                                                                            resultWidth3 = resultWidth5;
                                                                                                                                                                                                                            error = error2;
                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                            bitrate2 = bitrate3;
                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        } catch (Throwable th51) {
                                                                                                                                                                                                                            th5 = th51;
                                                                                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                                                                                            e = th5;
                                                                                                                                                                                                                            str = str3;
                                                                                                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                                                                                                            resultHeight2 = i11;
                                                                                                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                                                                                                            FileLog.e(e);
                                                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                                                                                                            error = true;
                                                                                                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                } catch (Exception e75) {
                                                                                                                                                                                                                    e4 = e75;
                                                                                                                                                                                                                    i11 = framerate;
                                                                                                                                                                                                                } catch (Throwable th52) {
                                                                                                                                                                                                                    th5 = th52;
                                                                                                                                                                                                                    i11 = framerate;
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                            doRender = doRender3;
                                                                                                                                                                                                            flushed = false;
                                                                                                                                                                                                            str23 = str7;
                                                                                                                                                                                                            videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                            h3 = h;
                                                                                                                                                                                                            str24 = str5;
                                                                                                                                                                                                            i12 = framerate;
                                                                                                                                                                                                            j5 = startTime;
                                                                                                                                                                                                            flushed2 = flushed;
                                                                                                                                                                                                            doRender2 = doRender;
                                                                                                                                                                                                            i11 = i12;
                                                                                                                                                                                                            if (lastFramePts > 0) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            trueStartTime = avatarStartTime2 >= 0 ? avatarStartTime2 : j5;
                                                                                                                                                                                                            if (trueStartTime <= 0) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (flushed2) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (doRender2) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if ((info3.flags & 4) != 0) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            MediaFormat newFormat6 = decoder2.getOutputFormat();
                                                                                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                                                FileLog.d("newFormat = " + newFormat6);
                                                                                                                                                                                                            }
                                                                                                                                                                                                            str23 = str7;
                                                                                                                                                                                                            videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                            w4 = w3;
                                                                                                                                                                                                            h3 = h;
                                                                                                                                                                                                            str24 = str5;
                                                                                                                                                                                                            minPresentationTime4 = minPresentationTime2;
                                                                                                                                                                                                            inputSurface3 = inputSurface2;
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        str23 = str7;
                                                                                                                                                                                                        videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                        w4 = w3;
                                                                                                                                                                                                        h3 = h;
                                                                                                                                                                                                        str24 = str5;
                                                                                                                                                                                                        minPresentationTime4 = minPresentationTime2;
                                                                                                                                                                                                        inputSurface3 = inputSurface2;
                                                                                                                                                                                                    }
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    str23 = str7;
                                                                                                                                                                                                    videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                                    w4 = w3;
                                                                                                                                                                                                    h3 = h;
                                                                                                                                                                                                    decoderStatus = false;
                                                                                                                                                                                                    str24 = str5;
                                                                                                                                                                                                    minPresentationTime3 = minPresentationTime2;
                                                                                                                                                                                                    endTime5 = endTime2;
                                                                                                                                                                                                    inputSurface3 = inputSurface2;
                                                                                                                                                                                                }
                                                                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                                                                j = startTime;
                                                                                                                                                                                                inputSurface2 = inputSurface3;
                                                                                                                                                                                                encoder2 = encoder3;
                                                                                                                                                                                                str21 = str6;
                                                                                                                                                                                                inputSurface8 = encoderOutputAvailable;
                                                                                                                                                                                                encoderOutputBuffers3 = encoderOutputBuffers4;
                                                                                                                                                                                                videoTrackIndex = videoTrackIndex4;
                                                                                                                                                                                                str22 = str4;
                                                                                                                                                                                                minPresentationTime = minPresentationTime3;
                                                                                                                                                                                            } catch (Throwable th53) {
                                                                                                                                                                                                th5 = th53;
                                                                                                                                                                                                i11 = framerate;
                                                                                                                                                                                            }
                                                                                                                                                                                        } catch (Exception e76) {
                                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                                            e2 = e76;
                                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                                            encoder4 = encoder3;
                                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                                        }
                                                                                                                                                                                    } else {
                                                                                                                                                                                        str23 = str7;
                                                                                                                                                                                        videoTrackIndex4 = videoTrackIndex;
                                                                                                                                                                                        w4 = w3;
                                                                                                                                                                                        h3 = h;
                                                                                                                                                                                        str24 = str5;
                                                                                                                                                                                        minPresentationTime4 = minPresentationTime2;
                                                                                                                                                                                        inputSurface3 = inputSurface2;
                                                                                                                                                                                    }
                                                                                                                                                                                    minPresentationTime3 = minPresentationTime4;
                                                                                                                                                                                    decoderStatus = decoderOutputAvailable6;
                                                                                                                                                                                    endTime5 = endTime2;
                                                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                                                    j = startTime;
                                                                                                                                                                                    inputSurface2 = inputSurface3;
                                                                                                                                                                                    encoder2 = encoder3;
                                                                                                                                                                                    str21 = str6;
                                                                                                                                                                                    inputSurface8 = encoderOutputAvailable;
                                                                                                                                                                                    encoderOutputBuffers3 = encoderOutputBuffers4;
                                                                                                                                                                                    videoTrackIndex = videoTrackIndex4;
                                                                                                                                                                                    str22 = str4;
                                                                                                                                                                                    minPresentationTime = minPresentationTime3;
                                                                                                                                                                                }
                                                                                                                                                                            } catch (Exception e77) {
                                                                                                                                                                                i5 = framerate;
                                                                                                                                                                                endTime2 = endTime5;
                                                                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                                                                audioRecoder3 = audioRecoder2;
                                                                                                                                                                                e2 = e77;
                                                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                                                videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                                encoder4 = encoder3;
                                                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                                            }
                                                                                                                                                                        } catch (Exception e78) {
                                                                                                                                                                            i5 = framerate;
                                                                                                                                                                            endTime2 = endTime5;
                                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                                            audioRecoder3 = audioRecoder2;
                                                                                                                                                                            e2 = e78;
                                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                                            videoTrackIndex5 = videoTrackIndex;
                                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                                        }
                                                                                                                                                                    } catch (Throwable th54) {
                                                                                                                                                                        th4 = th54;
                                                                                                                                                                        i10 = framerate;
                                                                                                                                                                        endTime2 = endTime5;
                                                                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        } catch (Exception e79) {
                                                                                                                                                            i5 = framerate;
                                                                                                                                                            videoIndex2 = videoIndex;
                                                                                                                                                            outputSurface3 = outputSurface;
                                                                                                                                                            audioRecoder3 = audioRecoder;
                                                                                                                                                            e2 = e79;
                                                                                                                                                            inputSurface5 = inputSurface2;
                                                                                                                                                            encoder4 = encoder2;
                                                                                                                                                            decoder2 = decoder2;
                                                                                                                                                            videoTrackIndex5 = videoTrackIndex3;
                                                                                                                                                            endTime2 = endTime3;
                                                                                                                                                            bitrate5 = bitrate3;
                                                                                                                                                        } catch (Throwable th55) {
                                                                                                                                                            th3 = th55;
                                                                                                                                                            i9 = framerate;
                                                                                                                                                            j3 = endTime3;
                                                                                                                                                            resultWidth4 = resultWidth;
                                                                                                                                                            resultHeight4 = resultHeight;
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    audioEncoderDone = audioEncoderDone3;
                                                                                                                                                    videoTrackIndex3 = videoTrackIndex;
                                                                                                                                                    minPresentationTime6 = minPresentationTime;
                                                                                                                                                    info2 = info3;
                                                                                                                                                    decoderInputBuffers = decoderInputBuffers2;
                                                                                                                                                    audioIndex5 = audioTrackIndex;
                                                                                                                                                    videoIndex = videoIndex2;
                                                                                                                                                    audioIndex4 = audioIndex6;
                                                                                                                                                    maxBufferSize2 = maxBufferSize5;
                                                                                                                                                    encoderOutputBuffers2 = encoderOutputBuffers3;
                                                                                                                                                    audioRecoder = audioRecoder2;
                                                                                                                                                    endTime3 = endTime5;
                                                                                                                                                }
                                                                                                                                            } catch (Exception e80) {
                                                                                                                                                i5 = framerate;
                                                                                                                                                encoder4 = encoder2;
                                                                                                                                                endTime2 = endTime;
                                                                                                                                                e2 = e80;
                                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                                videoIndex2 = videoIndex;
                                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                                decoder2 = decoder2;
                                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                                                if (e2 instanceof IllegalStateException) {
                                                                                                                                                    repeatWithIncreasedTimeout2 = true;
                                                                                                                                                }
                                                                                                                                                StringBuilder sb322222222222222222222222222 = new StringBuilder();
                                                                                                                                                str = str3;
                                                                                                                                                sb322222222222222222222222222.append(str);
                                                                                                                                                sb322222222222222222222222222.append(bitrate5);
                                                                                                                                                sb322222222222222222222222222.append(" framerate: ");
                                                                                                                                                sb322222222222222222222222222.append(i5);
                                                                                                                                                sb322222222222222222222222222.append(" size: ");
                                                                                                                                                bitrate4 = resultHeight;
                                                                                                                                                sb322222222222222222222222222.append(bitrate4);
                                                                                                                                                sb322222222222222222222222222.append("x");
                                                                                                                                                resultWidth5 = resultWidth;
                                                                                                                                                sb322222222222222222222222222.append(resultWidth5);
                                                                                                                                                FileLog.e(sb322222222222222222222222222.toString());
                                                                                                                                                FileLog.e(e2);
                                                                                                                                                bitrate3 = bitrate5;
                                                                                                                                                error2 = true;
                                                                                                                                                i4 = i5;
                                                                                                                                                mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                                                i4 = i4;
                                                                                                                                                if (decoder2 != null) {
                                                                                                                                                }
                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface5 != null) {
                                                                                                                                                }
                                                                                                                                                if (encoder4 != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder3 != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                videoTrackIndex2 = videoTrackIndex5;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                resultWidth2 = bitrate4;
                                                                                                                                                resultWidth3 = resultWidth5;
                                                                                                                                                error = error2;
                                                                                                                                                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                                bitrate2 = bitrate3;
                                                                                                                                                if (repeatWithIncreasedTimeout) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        } catch (Throwable th56) {
                                                                                                                                            resultHeight2 = framerate;
                                                                                                                                            endTime2 = endTime;
                                                                                                                                            e = th56;
                                                                                                                                            resultHeight4 = i7;
                                                                                                                                            resultWidth4 = i6;
                                                                                                                                            videoTrackIndex = -5;
                                                                                                                                            str = str3;
                                                                                                                                            bitrate7 = bitrate3;
                                                                                                                                            FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                                            FileLog.e(e);
                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                            }
                                                                                                                                            mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                            }
                                                                                                                                            bitrate2 = bitrate7;
                                                                                                                                            resultWidth3 = resultWidth4;
                                                                                                                                            error = true;
                                                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                                            resultWidth2 = resultHeight4;
                                                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        i6 = resultWidth;
                                                                                                                                    }
                                                                                                                                } catch (Exception e81) {
                                                                                                                                    i5 = framerate;
                                                                                                                                    encoder4 = encoder2;
                                                                                                                                    endTime2 = endTime;
                                                                                                                                    e2 = e81;
                                                                                                                                    outputSurface3 = outputSurface;
                                                                                                                                    videoIndex2 = videoIndex;
                                                                                                                                    bitrate5 = bitrate3;
                                                                                                                                    decoder2 = decoder2;
                                                                                                                                    inputSurface5 = inputSurface2;
                                                                                                                                } catch (Throwable th57) {
                                                                                                                                    resultHeight2 = framerate;
                                                                                                                                    endTime2 = endTime;
                                                                                                                                    e = th57;
                                                                                                                                    resultHeight4 = i7;
                                                                                                                                    resultWidth4 = resultWidth;
                                                                                                                                    videoTrackIndex = -5;
                                                                                                                                    str = str3;
                                                                                                                                    bitrate7 = bitrate3;
                                                                                                                                }
                                                                                                                            } catch (Exception e82) {
                                                                                                                                i5 = framerate;
                                                                                                                                encoder4 = encoder2;
                                                                                                                                endTime2 = endTime;
                                                                                                                                e2 = e82;
                                                                                                                                outputSurface3 = outputSurface;
                                                                                                                                videoIndex2 = videoIndex;
                                                                                                                                bitrate5 = bitrate3;
                                                                                                                                decoder2 = decoder2;
                                                                                                                                inputSurface5 = inputSurface2;
                                                                                                                            } catch (Throwable th58) {
                                                                                                                                resultHeight2 = framerate;
                                                                                                                                endTime2 = endTime;
                                                                                                                                e = th58;
                                                                                                                                resultHeight4 = i7;
                                                                                                                                resultWidth4 = resultWidth;
                                                                                                                                videoTrackIndex = -5;
                                                                                                                                str = str3;
                                                                                                                                bitrate7 = bitrate3;
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            i6 = resultWidth;
                                                                                                                            i7 = resultHeight;
                                                                                                                        }
                                                                                                                        i8 = 0;
                                                                                                                        decoder2.configure(videoFormat, outputSurface.getSurface(), (MediaCrypto) null, i8);
                                                                                                                        decoder2.start();
                                                                                                                        decoderInputBuffers = null;
                                                                                                                        ByteBuffer[] encoderOutputBuffers92 = null;
                                                                                                                        if (Build.VERSION.SDK_INT < 21) {
                                                                                                                        }
                                                                                                                        int maxBufferSize32 = 0;
                                                                                                                        audioIndex2 = audioIndex;
                                                                                                                        if (audioIndex2 < 0) {
                                                                                                                        }
                                                                                                                        if (audioIndex4 >= 0) {
                                                                                                                        }
                                                                                                                        checkConversionCanceled();
                                                                                                                        encoderOutputBuffers2 = encoderOutputBuffers;
                                                                                                                        videoTrackIndex3 = -5;
                                                                                                                        boolean firstEncode82 = true;
                                                                                                                        maxBufferSize2 = maxBufferSize;
                                                                                                                        endTime3 = j2;
                                                                                                                        loop4: while (true) {
                                                                                                                            if (outputDone2) {
                                                                                                                            }
                                                                                                                            checkConversionCanceled();
                                                                                                                            if (!copyAudioBuffer) {
                                                                                                                            }
                                                                                                                            audioEncoderDone2 = audioEncoderDone;
                                                                                                                            if (inputDone) {
                                                                                                                            }
                                                                                                                            boolean decoderOutputAvailable52 = !decoderDone2;
                                                                                                                            boolean inputSurface82 = true;
                                                                                                                            videoTrackIndex = videoTrackIndex3;
                                                                                                                            endTime5 = endTime4;
                                                                                                                            decoderStatus = decoderOutputAvailable52;
                                                                                                                            encoderOutputBuffers3 = encoderOutputBuffers2;
                                                                                                                            int maxBufferSize52 = maxBufferSize2;
                                                                                                                            minPresentationTime = minPresentationTime6;
                                                                                                                            while (true) {
                                                                                                                                if (!decoderStatus) {
                                                                                                                                }
                                                                                                                                checkConversionCanceled();
                                                                                                                                if (!increaseTimeout) {
                                                                                                                                }
                                                                                                                                boolean decoderOutputAvailable62 = decoderOutputAvailable;
                                                                                                                                encoder3 = encoder2;
                                                                                                                                encoderStatus = encoder3.dequeueOutputBuffer(info3, j4);
                                                                                                                                if (encoderStatus != -1) {
                                                                                                                                }
                                                                                                                                if (encoderStatus2 == -1) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                            audioEncoderDone = audioEncoderDone3;
                                                                                                                            videoTrackIndex3 = videoTrackIndex;
                                                                                                                            minPresentationTime6 = minPresentationTime;
                                                                                                                            info2 = info3;
                                                                                                                            decoderInputBuffers = decoderInputBuffers2;
                                                                                                                            audioIndex5 = audioTrackIndex;
                                                                                                                            videoIndex = videoIndex2;
                                                                                                                            audioIndex4 = audioIndex6;
                                                                                                                            maxBufferSize2 = maxBufferSize52;
                                                                                                                            encoderOutputBuffers2 = encoderOutputBuffers3;
                                                                                                                            audioRecoder = audioRecoder2;
                                                                                                                            endTime3 = endTime5;
                                                                                                                        }
                                                                                                                    } catch (Exception e83) {
                                                                                                                        endTime2 = endTime;
                                                                                                                        avatarStartTime2 = avatarStartTime4;
                                                                                                                        e2 = e83;
                                                                                                                        bitrate5 = bitrate8;
                                                                                                                        videoIndex2 = videoIndex;
                                                                                                                        decoder2 = decoder;
                                                                                                                        i5 = i3;
                                                                                                                        if (e2 instanceof IllegalStateException) {
                                                                                                                        }
                                                                                                                        StringBuilder sb3222222222222222222222222222 = new StringBuilder();
                                                                                                                        str = str3;
                                                                                                                        sb3222222222222222222222222222.append(str);
                                                                                                                        sb3222222222222222222222222222.append(bitrate5);
                                                                                                                        sb3222222222222222222222222222.append(" framerate: ");
                                                                                                                        sb3222222222222222222222222222.append(i5);
                                                                                                                        sb3222222222222222222222222222.append(" size: ");
                                                                                                                        bitrate4 = resultHeight;
                                                                                                                        sb3222222222222222222222222222.append(bitrate4);
                                                                                                                        sb3222222222222222222222222222.append("x");
                                                                                                                        resultWidth5 = resultWidth;
                                                                                                                        sb3222222222222222222222222222.append(resultWidth5);
                                                                                                                        FileLog.e(sb3222222222222222222222222222.toString());
                                                                                                                        FileLog.e(e2);
                                                                                                                        bitrate3 = bitrate5;
                                                                                                                        error2 = true;
                                                                                                                        i4 = i5;
                                                                                                                        mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                                        i4 = i4;
                                                                                                                        if (decoder2 != null) {
                                                                                                                        }
                                                                                                                        if (outputSurface3 != null) {
                                                                                                                        }
                                                                                                                        if (inputSurface5 != null) {
                                                                                                                        }
                                                                                                                        if (encoder4 != null) {
                                                                                                                        }
                                                                                                                        if (audioRecoder3 != null) {
                                                                                                                        }
                                                                                                                        checkConversionCanceled();
                                                                                                                        videoTrackIndex2 = videoTrackIndex5;
                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                        }
                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                        }
                                                                                                                        resultWidth2 = bitrate4;
                                                                                                                        resultWidth3 = resultWidth5;
                                                                                                                        error = error2;
                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                        bitrate2 = bitrate3;
                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                        }
                                                                                                                    } catch (Throwable th59) {
                                                                                                                        resultWidth4 = resultWidth;
                                                                                                                        resultHeight4 = resultHeight;
                                                                                                                        endTime2 = endTime;
                                                                                                                        avatarStartTime2 = avatarStartTime4;
                                                                                                                        bitrate7 = bitrate8;
                                                                                                                        videoTrackIndex = -5;
                                                                                                                        str = str3;
                                                                                                                        e = th59;
                                                                                                                        resultHeight2 = i3;
                                                                                                                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                                        FileLog.e(e);
                                                                                                                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                                        if (mediaExtractor != null) {
                                                                                                                        }
                                                                                                                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                                        if (mP4Builder != null) {
                                                                                                                        }
                                                                                                                        bitrate2 = bitrate7;
                                                                                                                        resultWidth3 = resultWidth4;
                                                                                                                        error = true;
                                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                                        resultWidth2 = resultHeight4;
                                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            bitrate3 = bitrate5;
                                                                                                            encoder = MediaCodec.createEncoderByType("video/avc");
                                                                                                            encoder.configure(outputFormat2, (Surface) null, (MediaCrypto) null, 1);
                                                                                                            inputSurface = new InputSurface(encoder.createInputSurface());
                                                                                                            inputSurface.makeCurrent();
                                                                                                            encoder.start();
                                                                                                            decoder2 = MediaCodec.createDecoderByType(videoFormat.getString("mime"));
                                                                                                            inputSurface2 = inputSurface;
                                                                                                            avatarStartTime2 = avatarStartTime4;
                                                                                                            int h32 = w2;
                                                                                                            encoder2 = encoder;
                                                                                                            String str212 = "video/avc";
                                                                                                            int w42 = w;
                                                                                                            String str222 = "prepend-sps-pps-to-idr-frames";
                                                                                                            long frameDelta22 = endTime2;
                                                                                                            String str232 = "csd-1";
                                                                                                            String str242 = "csd-0";
                                                                                                            info2 = info;
                                                                                                            outputSurface = new OutputSurface(savedFilterState, null, paintPath, mediaEntities, cropState, resultWidth, resultHeight, originalWidth, originalHeight, rotationValue, i3, false);
                                                                                                            if (!isRound) {
                                                                                                            }
                                                                                                            i8 = 0;
                                                                                                            decoder2.configure(videoFormat, outputSurface.getSurface(), (MediaCrypto) null, i8);
                                                                                                            decoder2.start();
                                                                                                            decoderInputBuffers = null;
                                                                                                            ByteBuffer[] encoderOutputBuffers922 = null;
                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                            }
                                                                                                            int maxBufferSize322 = 0;
                                                                                                            audioIndex2 = audioIndex;
                                                                                                            if (audioIndex2 < 0) {
                                                                                                            }
                                                                                                            if (audioIndex4 >= 0) {
                                                                                                            }
                                                                                                            checkConversionCanceled();
                                                                                                            encoderOutputBuffers2 = encoderOutputBuffers;
                                                                                                            videoTrackIndex3 = -5;
                                                                                                            boolean firstEncode822 = true;
                                                                                                            maxBufferSize2 = maxBufferSize;
                                                                                                            endTime3 = j2;
                                                                                                            loop4: while (true) {
                                                                                                                if (outputDone2) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                if (!copyAudioBuffer) {
                                                                                                                }
                                                                                                                audioEncoderDone2 = audioEncoderDone;
                                                                                                                if (inputDone) {
                                                                                                                }
                                                                                                                boolean decoderOutputAvailable522 = !decoderDone2;
                                                                                                                boolean inputSurface822 = true;
                                                                                                                videoTrackIndex = videoTrackIndex3;
                                                                                                                endTime5 = endTime4;
                                                                                                                decoderStatus = decoderOutputAvailable522;
                                                                                                                encoderOutputBuffers3 = encoderOutputBuffers2;
                                                                                                                int maxBufferSize522 = maxBufferSize2;
                                                                                                                minPresentationTime = minPresentationTime6;
                                                                                                                while (true) {
                                                                                                                    if (!decoderStatus) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    if (!increaseTimeout) {
                                                                                                                    }
                                                                                                                    boolean decoderOutputAvailable622 = decoderOutputAvailable;
                                                                                                                    encoder3 = encoder2;
                                                                                                                    encoderStatus = encoder3.dequeueOutputBuffer(info3, j4);
                                                                                                                    if (encoderStatus != -1) {
                                                                                                                    }
                                                                                                                    if (encoderStatus2 == -1) {
                                                                                                                    }
                                                                                                                }
                                                                                                                audioEncoderDone = audioEncoderDone3;
                                                                                                                videoTrackIndex3 = videoTrackIndex;
                                                                                                                minPresentationTime6 = minPresentationTime;
                                                                                                                info2 = info3;
                                                                                                                decoderInputBuffers = decoderInputBuffers2;
                                                                                                                audioIndex5 = audioTrackIndex;
                                                                                                                videoIndex = videoIndex2;
                                                                                                                audioIndex4 = audioIndex6;
                                                                                                                maxBufferSize2 = maxBufferSize522;
                                                                                                                encoderOutputBuffers2 = encoderOutputBuffers3;
                                                                                                                audioRecoder = audioRecoder2;
                                                                                                                endTime3 = endTime5;
                                                                                                            }
                                                                                                        } catch (Exception e84) {
                                                                                                            avatarStartTime2 = avatarStartTime4;
                                                                                                            videoIndex2 = videoIndex;
                                                                                                            endTime2 = endTime;
                                                                                                            e2 = e84;
                                                                                                            i5 = i3;
                                                                                                            if (e2 instanceof IllegalStateException) {
                                                                                                            }
                                                                                                            StringBuilder sb32222222222222222222222222222 = new StringBuilder();
                                                                                                            str = str3;
                                                                                                            sb32222222222222222222222222222.append(str);
                                                                                                            sb32222222222222222222222222222.append(bitrate5);
                                                                                                            sb32222222222222222222222222222.append(" framerate: ");
                                                                                                            sb32222222222222222222222222222.append(i5);
                                                                                                            sb32222222222222222222222222222.append(" size: ");
                                                                                                            bitrate4 = resultHeight;
                                                                                                            sb32222222222222222222222222222.append(bitrate4);
                                                                                                            sb32222222222222222222222222222.append("x");
                                                                                                            resultWidth5 = resultWidth;
                                                                                                            sb32222222222222222222222222222.append(resultWidth5);
                                                                                                            FileLog.e(sb32222222222222222222222222222.toString());
                                                                                                            FileLog.e(e2);
                                                                                                            bitrate3 = bitrate5;
                                                                                                            error2 = true;
                                                                                                            i4 = i5;
                                                                                                            mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                            i4 = i4;
                                                                                                            if (decoder2 != null) {
                                                                                                            }
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface5 != null) {
                                                                                                            }
                                                                                                            if (encoder4 != null) {
                                                                                                            }
                                                                                                            if (audioRecoder3 != null) {
                                                                                                            }
                                                                                                            checkConversionCanceled();
                                                                                                            videoTrackIndex2 = videoTrackIndex5;
                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                            if (mediaExtractor2 != null) {
                                                                                                            }
                                                                                                            mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                            if (mP4Builder2 != null) {
                                                                                                            }
                                                                                                            resultWidth2 = bitrate4;
                                                                                                            resultWidth3 = resultWidth5;
                                                                                                            error = error2;
                                                                                                            repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                            bitrate2 = bitrate3;
                                                                                                            if (repeatWithIncreasedTimeout) {
                                                                                                            }
                                                                                                        }
                                                                                                    } catch (Throwable th60) {
                                                                                                        avatarStartTime2 = avatarStartTime4;
                                                                                                        resultWidth4 = resultWidth;
                                                                                                        resultHeight4 = resultHeight;
                                                                                                        endTime2 = endTime;
                                                                                                        e = th60;
                                                                                                        bitrate7 = bitrate5;
                                                                                                        videoTrackIndex = -5;
                                                                                                        str = str3;
                                                                                                        resultHeight2 = i3;
                                                                                                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                        FileLog.e(e);
                                                                                                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                        if (mediaExtractor != null) {
                                                                                                        }
                                                                                                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                        if (mP4Builder != null) {
                                                                                                        }
                                                                                                        bitrate2 = bitrate7;
                                                                                                        resultWidth3 = resultWidth4;
                                                                                                        error = true;
                                                                                                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                        resultWidth2 = resultHeight4;
                                                                                                        if (repeatWithIncreasedTimeout) {
                                                                                                        }
                                                                                                    }
                                                                                                } catch (Exception e85) {
                                                                                                    avatarStartTime2 = avatarStartTime5;
                                                                                                    videoIndex2 = videoIndex;
                                                                                                    endTime2 = endTime;
                                                                                                    e2 = e85;
                                                                                                    i5 = i3;
                                                                                                    if (e2 instanceof IllegalStateException) {
                                                                                                    }
                                                                                                    StringBuilder sb322222222222222222222222222222 = new StringBuilder();
                                                                                                    str = str3;
                                                                                                    sb322222222222222222222222222222.append(str);
                                                                                                    sb322222222222222222222222222222.append(bitrate5);
                                                                                                    sb322222222222222222222222222222.append(" framerate: ");
                                                                                                    sb322222222222222222222222222222.append(i5);
                                                                                                    sb322222222222222222222222222222.append(" size: ");
                                                                                                    bitrate4 = resultHeight;
                                                                                                    sb322222222222222222222222222222.append(bitrate4);
                                                                                                    sb322222222222222222222222222222.append("x");
                                                                                                    resultWidth5 = resultWidth;
                                                                                                    sb322222222222222222222222222222.append(resultWidth5);
                                                                                                    FileLog.e(sb322222222222222222222222222222.toString());
                                                                                                    FileLog.e(e2);
                                                                                                    bitrate3 = bitrate5;
                                                                                                    error2 = true;
                                                                                                    i4 = i5;
                                                                                                    mediaCodecVideoConvertor.extractor.unselectTrack(videoIndex2);
                                                                                                    i4 = i4;
                                                                                                    if (decoder2 != null) {
                                                                                                    }
                                                                                                    if (outputSurface3 != null) {
                                                                                                    }
                                                                                                    if (inputSurface5 != null) {
                                                                                                    }
                                                                                                    if (encoder4 != null) {
                                                                                                    }
                                                                                                    if (audioRecoder3 != null) {
                                                                                                    }
                                                                                                    checkConversionCanceled();
                                                                                                    videoTrackIndex2 = videoTrackIndex5;
                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                                                                                                    if (mediaExtractor2 != null) {
                                                                                                    }
                                                                                                    mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                    if (mP4Builder2 != null) {
                                                                                                    }
                                                                                                    resultWidth2 = bitrate4;
                                                                                                    resultWidth3 = resultWidth5;
                                                                                                    error = error2;
                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                    bitrate2 = bitrate3;
                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                    }
                                                                                                } catch (Throwable th61) {
                                                                                                    avatarStartTime2 = avatarStartTime5;
                                                                                                    resultWidth4 = resultWidth;
                                                                                                    resultHeight4 = resultHeight;
                                                                                                    endTime2 = endTime;
                                                                                                    e = th61;
                                                                                                    bitrate7 = bitrate5;
                                                                                                    videoTrackIndex = -5;
                                                                                                    str = str3;
                                                                                                    resultHeight2 = i3;
                                                                                                    FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                                                                                                    FileLog.e(e);
                                                                                                    mediaExtractor = mediaCodecVideoConvertor.extractor;
                                                                                                    if (mediaExtractor != null) {
                                                                                                    }
                                                                                                    mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                                                                                                    if (mP4Builder != null) {
                                                                                                    }
                                                                                                    bitrate2 = bitrate7;
                                                                                                    resultWidth3 = resultWidth4;
                                                                                                    error = true;
                                                                                                    repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                                                                                                    resultWidth2 = resultHeight4;
                                                                                                    if (repeatWithIncreasedTimeout) {
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        checkConversionCanceled();
                                                                                        encoderOutputBuffers2 = encoderOutputBuffers;
                                                                                        videoTrackIndex3 = -5;
                                                                                        boolean firstEncode8222 = true;
                                                                                        maxBufferSize2 = maxBufferSize;
                                                                                        endTime3 = j2;
                                                                                        loop4: while (true) {
                                                                                            if (outputDone2) {
                                                                                            }
                                                                                            checkConversionCanceled();
                                                                                            if (!copyAudioBuffer) {
                                                                                            }
                                                                                            audioEncoderDone2 = audioEncoderDone;
                                                                                            if (inputDone) {
                                                                                            }
                                                                                            boolean decoderOutputAvailable5222 = !decoderDone2;
                                                                                            boolean inputSurface8222 = true;
                                                                                            videoTrackIndex = videoTrackIndex3;
                                                                                            endTime5 = endTime4;
                                                                                            decoderStatus = decoderOutputAvailable5222;
                                                                                            encoderOutputBuffers3 = encoderOutputBuffers2;
                                                                                            int maxBufferSize5222 = maxBufferSize2;
                                                                                            minPresentationTime = minPresentationTime6;
                                                                                            while (true) {
                                                                                                if (!decoderStatus) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                if (!increaseTimeout) {
                                                                                                }
                                                                                                boolean decoderOutputAvailable6222 = decoderOutputAvailable;
                                                                                                encoder3 = encoder2;
                                                                                                encoderStatus = encoder3.dequeueOutputBuffer(info3, j4);
                                                                                                if (encoderStatus != -1) {
                                                                                                }
                                                                                                if (encoderStatus2 == -1) {
                                                                                                }
                                                                                            }
                                                                                            audioEncoderDone = audioEncoderDone3;
                                                                                            videoTrackIndex3 = videoTrackIndex;
                                                                                            minPresentationTime6 = minPresentationTime;
                                                                                            info2 = info3;
                                                                                            decoderInputBuffers = decoderInputBuffers2;
                                                                                            audioIndex5 = audioTrackIndex;
                                                                                            videoIndex = videoIndex2;
                                                                                            audioIndex4 = audioIndex6;
                                                                                            maxBufferSize2 = maxBufferSize5222;
                                                                                            encoderOutputBuffers2 = encoderOutputBuffers3;
                                                                                            audioRecoder = audioRecoder2;
                                                                                            endTime3 = endTime5;
                                                                                        }
                                                                                    } catch (Exception e86) {
                                                                                        i5 = framerate;
                                                                                        videoIndex2 = videoIndex;
                                                                                        outputSurface3 = outputSurface;
                                                                                        endTime2 = endTime;
                                                                                        audioRecoder3 = audioRecoder;
                                                                                        e2 = e86;
                                                                                        inputSurface5 = inputSurface2;
                                                                                        encoder4 = encoder2;
                                                                                        decoder2 = decoder2;
                                                                                        bitrate5 = bitrate3;
                                                                                    }
                                                                                    decoder2.configure(videoFormat, outputSurface.getSurface(), (MediaCrypto) null, i8);
                                                                                    decoder2.start();
                                                                                    decoderInputBuffers = null;
                                                                                    ByteBuffer[] encoderOutputBuffers9222 = null;
                                                                                    if (Build.VERSION.SDK_INT < 21) {
                                                                                    }
                                                                                    int maxBufferSize3222 = 0;
                                                                                    audioIndex2 = audioIndex;
                                                                                    if (audioIndex2 < 0) {
                                                                                    }
                                                                                    if (audioIndex4 >= 0) {
                                                                                    }
                                                                                } catch (Exception e87) {
                                                                                    i5 = framerate;
                                                                                    videoIndex2 = videoIndex;
                                                                                    outputSurface3 = outputSurface;
                                                                                    endTime2 = endTime;
                                                                                    inputSurface5 = inputSurface2;
                                                                                    encoder4 = encoder2;
                                                                                    decoder2 = decoder2;
                                                                                    bitrate5 = bitrate3;
                                                                                    e2 = e87;
                                                                                }
                                                                            } catch (Exception e88) {
                                                                                i5 = framerate;
                                                                                videoIndex2 = videoIndex;
                                                                                outputSurface3 = outputSurface;
                                                                                endTime2 = endTime;
                                                                                inputSurface5 = inputSurface2;
                                                                                encoder4 = encoder2;
                                                                                decoder2 = decoder2;
                                                                                bitrate5 = bitrate3;
                                                                                e2 = e88;
                                                                            }
                                                                            outputSurface = new OutputSurface(savedFilterState, null, paintPath, mediaEntities, cropState, resultWidth, resultHeight, originalWidth, originalHeight, rotationValue, i3, false);
                                                                            if (!isRound) {
                                                                            }
                                                                            i8 = 0;
                                                                        } catch (Throwable th62) {
                                                                            resultHeight2 = framerate;
                                                                            resultWidth4 = resultWidth;
                                                                            resultHeight4 = resultHeight;
                                                                            endTime2 = endTime;
                                                                            e = th62;
                                                                            videoTrackIndex = -5;
                                                                            str = str3;
                                                                            bitrate7 = bitrate3;
                                                                        }
                                                                    } catch (Exception e89) {
                                                                        i5 = framerate;
                                                                        videoIndex2 = videoIndex;
                                                                        endTime2 = endTime;
                                                                        inputSurface5 = inputSurface2;
                                                                        encoder4 = encoder2;
                                                                        decoder2 = decoder2;
                                                                        bitrate5 = bitrate3;
                                                                        e2 = e89;
                                                                    }
                                                                    inputSurface2 = inputSurface;
                                                                    avatarStartTime2 = avatarStartTime4;
                                                                    int h322 = w2;
                                                                    encoder2 = encoder;
                                                                    String str2122 = "video/avc";
                                                                    int w422 = w;
                                                                    String str2222 = "prepend-sps-pps-to-idr-frames";
                                                                    long frameDelta222 = endTime2;
                                                                    String str2322 = "csd-1";
                                                                    String str2422 = "csd-0";
                                                                    info2 = info;
                                                                } catch (Exception e90) {
                                                                    avatarStartTime2 = avatarStartTime4;
                                                                    videoIndex2 = videoIndex;
                                                                    endTime2 = endTime;
                                                                    inputSurface5 = inputSurface;
                                                                    encoder4 = encoder;
                                                                    bitrate5 = bitrate3;
                                                                    e2 = e90;
                                                                    i5 = i3;
                                                                }
                                                                inputSurface.makeCurrent();
                                                                encoder.start();
                                                                decoder2 = MediaCodec.createDecoderByType(videoFormat.getString("mime"));
                                                            } catch (Exception e91) {
                                                                avatarStartTime2 = avatarStartTime4;
                                                                videoIndex2 = videoIndex;
                                                                endTime2 = endTime;
                                                                inputSurface5 = inputSurface;
                                                                encoder4 = encoder;
                                                                decoder2 = decoder;
                                                                bitrate5 = bitrate3;
                                                                e2 = e91;
                                                                i5 = i3;
                                                            }
                                                            encoder.configure(outputFormat2, (Surface) null, (MediaCrypto) null, 1);
                                                            inputSurface = new InputSurface(encoder.createInputSurface());
                                                        } catch (Exception e92) {
                                                            avatarStartTime2 = avatarStartTime4;
                                                            videoIndex2 = videoIndex;
                                                            endTime2 = endTime;
                                                            encoder4 = encoder;
                                                            decoder2 = decoder;
                                                            bitrate5 = bitrate3;
                                                            e2 = e92;
                                                            i5 = i3;
                                                        }
                                                        encoder = MediaCodec.createEncoderByType("video/avc");
                                                    } catch (Exception e93) {
                                                        avatarStartTime2 = avatarStartTime4;
                                                        videoIndex2 = videoIndex;
                                                        endTime2 = endTime;
                                                        decoder2 = decoder;
                                                        bitrate5 = bitrate3;
                                                        e2 = e93;
                                                        i5 = i3;
                                                    }
                                                } catch (Throwable th63) {
                                                    avatarStartTime2 = avatarStartTime4;
                                                    resultWidth4 = resultWidth;
                                                    resultHeight4 = resultHeight;
                                                    endTime2 = endTime;
                                                    e = th63;
                                                    videoTrackIndex = -5;
                                                    str = str3;
                                                    bitrate7 = bitrate3;
                                                    resultHeight2 = i3;
                                                }
                                                MediaFormat outputFormat22 = MediaFormat.createVideoFormat("video/avc", w, w2);
                                                outputFormat22.setInteger("color-format", 2130708361);
                                                outputFormat22.setInteger("bitrate", bitrate5);
                                                outputFormat22.setInteger("frame-rate", i3);
                                                outputFormat22.setInteger("i-frame-interval", 2);
                                                if (Build.VERSION.SDK_INT < 23) {
                                                }
                                                bitrate3 = bitrate5;
                                            } catch (Exception e94) {
                                                avatarStartTime2 = avatarStartTime4;
                                                videoIndex2 = videoIndex;
                                                endTime2 = endTime;
                                                decoder2 = decoder;
                                                e2 = e94;
                                                i5 = i3;
                                            }
                                            if (cropState == null) {
                                            }
                                            if (!BuildVars.LOGS_ENABLED) {
                                            }
                                        } catch (Throwable th64) {
                                            resultWidth4 = resultWidth;
                                            resultHeight4 = resultHeight;
                                            endTime2 = endTime;
                                            avatarStartTime2 = avatarStartTime4;
                                            e = th64;
                                            bitrate7 = bitrate5;
                                            videoTrackIndex = -5;
                                            str = str3;
                                            resultHeight2 = i3;
                                        }
                                    } catch (Exception e95) {
                                        endTime2 = endTime;
                                        e2 = e95;
                                        avatarStartTime2 = avatarStartTime5;
                                        videoIndex2 = videoIndex;
                                        i5 = i3;
                                    } catch (Throwable th65) {
                                        resultWidth4 = resultWidth;
                                        resultHeight4 = resultHeight;
                                        endTime2 = endTime;
                                        e = th65;
                                        bitrate7 = bitrate5;
                                        avatarStartTime2 = avatarStartTime5;
                                        videoTrackIndex = -5;
                                        str = str3;
                                        resultHeight2 = i3;
                                    }
                                } catch (Exception e96) {
                                    videoIndex2 = videoIndex;
                                    bitrate5 = bitrate;
                                    endTime2 = endTime;
                                    avatarStartTime2 = avatarStartTime;
                                    e2 = e96;
                                    i5 = i3;
                                }
                            } else {
                                resultWidth5 = resultWidth;
                                bitrate4 = resultHeight;
                                str = str3;
                                bitrate3 = bitrate;
                                endTime2 = endTime;
                                avatarStartTime2 = avatarStartTime;
                                i4 = i3;
                            }
                            if (outputSurface3 != null) {
                                outputSurface3.release();
                            }
                            if (inputSurface5 != null) {
                                inputSurface5.release();
                            }
                            if (encoder4 != null) {
                                encoder4.stop();
                                encoder4.release();
                            }
                            if (audioRecoder3 != null) {
                                audioRecoder3.release();
                            }
                            checkConversionCanceled();
                            videoTrackIndex2 = videoTrackIndex5;
                        } catch (Throwable th66) {
                            resultWidth4 = resultWidth;
                            resultHeight4 = resultHeight;
                            bitrate7 = bitrate;
                            endTime2 = endTime;
                            avatarStartTime2 = avatarStartTime;
                            e = th66;
                            videoTrackIndex = -5;
                            str = str2;
                            resultHeight2 = info6;
                        }
                    } catch (Throwable th67) {
                        th = th67;
                        i2 = resultWidth;
                        i = framerate;
                        str = "bitrate: ";
                        endTime2 = endTime;
                        avatarStartTime2 = avatarStartTime;
                        e = th;
                        resultWidth4 = i2;
                        videoTrackIndex = -5;
                        bitrate7 = bitrate;
                        resultHeight2 = i;
                        FileLog.e(str + bitrate7 + " framerate: " + resultHeight2 + " size: " + resultHeight4 + "x" + resultWidth4);
                        FileLog.e(e);
                        mediaExtractor = mediaCodecVideoConvertor.extractor;
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        mP4Builder = mediaCodecVideoConvertor.mediaMuxer;
                        if (mP4Builder != null) {
                            try {
                                mP4Builder.finishMovie();
                                mediaCodecVideoConvertor.endPresentationTime = mediaCodecVideoConvertor.mediaMuxer.getLastFrameTimestamp(videoTrackIndex);
                            } catch (Throwable e97) {
                                FileLog.e(e97);
                            }
                        }
                        bitrate2 = bitrate7;
                        resultWidth3 = resultWidth4;
                        error = true;
                        repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                        resultWidth2 = resultHeight4;
                        if (repeatWithIncreasedTimeout) {
                        }
                    }
                }
                mediaExtractor2 = mediaCodecVideoConvertor.extractor;
                if (mediaExtractor2 != null) {
                    mediaExtractor2.release();
                }
                mP4Builder2 = mediaCodecVideoConvertor.mediaMuxer;
                if (mP4Builder2 != null) {
                    try {
                        mP4Builder2.finishMovie();
                        mediaCodecVideoConvertor.endPresentationTime = mediaCodecVideoConvertor.mediaMuxer.getLastFrameTimestamp(videoTrackIndex2);
                    } catch (Throwable e98) {
                        FileLog.e(e98);
                    }
                }
                resultWidth2 = bitrate4;
                resultWidth3 = resultWidth5;
                error = error2;
                repeatWithIncreasedTimeout = repeatWithIncreasedTimeout2;
                bitrate2 = bitrate3;
            } catch (Throwable th68) {
                th = th68;
                i = framerate;
                i2 = resultWidth;
                str = "bitrate: ";
            }
        } catch (Throwable th69) {
            th = th69;
            str = "bitrate: ";
            i = framerate;
            i2 = resultWidth;
        }
        if (repeatWithIncreasedTimeout) {
            return convertVideoInternal(videoPath, cacheFile, rotationValue, isSecret, originalWidth, originalHeight, resultWidth3, resultWidth2, framerate, bitrate2, originalBitrate, startTime, endTime2, avatarStartTime2, duration, needCompress, true, savedFilterState, paintPath, mediaEntities, isPhoto, cropState, isRound);
        }
        int bitrate9 = bitrate2;
        int resultWidth6 = resultWidth3;
        int resultHeight7 = resultWidth2;
        long timeLeft = System.currentTimeMillis() - time;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("compression completed time=" + timeLeft + " needCompress=" + needCompress + " w=" + resultWidth6 + " h=" + resultHeight7 + " bitrate=" + bitrate9);
        }
        return error;
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x013b, code lost:
        if (r14[r10 + 3] != 1) goto L73;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0241 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long readAndWriteTracks(MediaExtractor extractor, MP4Builder mediaMuxer, MediaCodec.BufferInfo info, long start, long end, long duration, File file, boolean needAudio) throws Exception {
        long currentPts;
        int muxerVideoTrackIndex;
        int maxBufferSize;
        ByteBuffer buffer;
        int index;
        int muxerTrackIndex;
        int audioTrackIndex;
        int maxBufferSize2;
        int writeStart;
        ByteBuffer buffer2;
        MediaCodecVideoConvertor mediaCodecVideoConvertor;
        boolean eof;
        boolean eof2;
        long currentPts2;
        int audioTrackIndex2;
        int offset;
        ByteBuffer buffer3;
        MP4Builder mP4Builder = mediaMuxer;
        int videoTrackIndex = MediaController.findTrack(extractor, false);
        int audioTrackIndex3 = needAudio ? MediaController.findTrack(extractor, true) : -1;
        float durationS = ((float) duration) / 1000.0f;
        int maxBufferSize3 = 0;
        int muxerAudioTrackIndex = -1;
        boolean inputDone = false;
        if (videoTrackIndex < 0) {
            currentPts = 0;
            muxerVideoTrackIndex = -1;
            maxBufferSize = 0;
        } else {
            extractor.selectTrack(videoTrackIndex);
            MediaFormat trackFormat = extractor.getTrackFormat(videoTrackIndex);
            muxerVideoTrackIndex = mP4Builder.addTrack(trackFormat, false);
            try {
                int muxerVideoTrackIndex2 = trackFormat.getInteger("max-input-size");
                maxBufferSize3 = muxerVideoTrackIndex2;
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (start <= 0) {
                currentPts = 0;
                extractor.seekTo(0L, 0);
            } else {
                extractor.seekTo(start, 0);
                currentPts = 0;
            }
            maxBufferSize = maxBufferSize3;
        }
        if (audioTrackIndex3 >= 0) {
            extractor.selectTrack(audioTrackIndex3);
            MediaFormat trackFormat2 = extractor.getTrackFormat(audioTrackIndex3);
            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                audioTrackIndex3 = -1;
            } else {
                muxerAudioTrackIndex = mP4Builder.addTrack(trackFormat2, true);
                try {
                    maxBufferSize = Math.max(trackFormat2.getInteger("max-input-size"), maxBufferSize);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (start <= 0) {
                    extractor.seekTo(0L, 0);
                } else {
                    extractor.seekTo(start, 0);
                }
            }
        }
        if (maxBufferSize <= 0) {
            maxBufferSize = 65536;
        }
        ByteBuffer buffer4 = ByteBuffer.allocateDirect(maxBufferSize);
        if (audioTrackIndex3 < 0 && videoTrackIndex < 0) {
            return -1L;
        }
        long startTime = -1;
        checkConversionCanceled();
        while (!inputDone) {
            checkConversionCanceled();
            if (Build.VERSION.SDK_INT < 28) {
                buffer3 = buffer4;
            } else {
                long size = extractor.getSampleSize();
                buffer3 = buffer4;
                if (size > maxBufferSize) {
                    int maxBufferSize4 = (int) (DistributeConstants.KIBIBYTE_IN_BYTES + size);
                    buffer = ByteBuffer.allocateDirect(maxBufferSize4);
                    maxBufferSize = maxBufferSize4;
                    info.size = extractor.readSampleData(buffer, 0);
                    index = extractor.getSampleTrackIndex();
                    if (index != videoTrackIndex) {
                        muxerTrackIndex = muxerVideoTrackIndex;
                    } else if (index == audioTrackIndex3) {
                        muxerTrackIndex = muxerAudioTrackIndex;
                    } else {
                        muxerTrackIndex = -1;
                    }
                    if (muxerTrackIndex == -1) {
                        if (Build.VERSION.SDK_INT < 21) {
                            buffer.position(0);
                            buffer.limit(info.size);
                        }
                        if (index == audioTrackIndex3) {
                            eof2 = false;
                            writeStart = muxerAudioTrackIndex;
                            audioTrackIndex = audioTrackIndex3;
                            maxBufferSize2 = maxBufferSize;
                        } else {
                            byte[] array = buffer.array();
                            if (array == null) {
                                eof2 = false;
                                writeStart = muxerAudioTrackIndex;
                                audioTrackIndex = audioTrackIndex3;
                                maxBufferSize2 = maxBufferSize;
                            } else {
                                int offset2 = buffer.arrayOffset();
                                int len = offset2 + buffer.limit();
                                eof2 = false;
                                int writeStart2 = -1;
                                writeStart = muxerAudioTrackIndex;
                                int muxerAudioTrackIndex2 = offset2;
                                while (true) {
                                    maxBufferSize2 = maxBufferSize;
                                    int maxBufferSize5 = len - 4;
                                    if (muxerAudioTrackIndex2 > maxBufferSize5) {
                                        break;
                                    }
                                    if (array[muxerAudioTrackIndex2] == 0 && array[muxerAudioTrackIndex2 + 1] == 0 && array[muxerAudioTrackIndex2 + 2] == 0) {
                                        offset = offset2;
                                    } else {
                                        offset = offset2;
                                    }
                                    if (muxerAudioTrackIndex2 != len - 4) {
                                        audioTrackIndex2 = audioTrackIndex3;
                                        muxerAudioTrackIndex2++;
                                        maxBufferSize = maxBufferSize2;
                                        offset2 = offset;
                                        audioTrackIndex3 = audioTrackIndex2;
                                    }
                                    if (writeStart2 != -1) {
                                        int l = (muxerAudioTrackIndex2 - writeStart2) - (muxerAudioTrackIndex2 != len + (-4) ? 4 : 0);
                                        array[writeStart2] = (byte) (l >> 24);
                                        audioTrackIndex2 = audioTrackIndex3;
                                        array[writeStart2 + 1] = (byte) (l >> 16);
                                        array[writeStart2 + 2] = (byte) (l >> 8);
                                        array[writeStart2 + 3] = (byte) l;
                                        writeStart2 = muxerAudioTrackIndex2;
                                    } else {
                                        audioTrackIndex2 = audioTrackIndex3;
                                        writeStart2 = muxerAudioTrackIndex2;
                                    }
                                    muxerAudioTrackIndex2++;
                                    maxBufferSize = maxBufferSize2;
                                    offset2 = offset;
                                    audioTrackIndex3 = audioTrackIndex2;
                                }
                                audioTrackIndex = audioTrackIndex3;
                            }
                        }
                        if (info.size >= 0) {
                            info.presentationTimeUs = extractor.getSampleTime();
                            eof = eof2;
                        } else {
                            info.size = 0;
                            eof = true;
                        }
                        if (info.size <= 0 || eof) {
                            mediaCodecVideoConvertor = this;
                            buffer2 = buffer;
                        } else {
                            if (index == videoTrackIndex && start > 0) {
                                if (startTime == -1) {
                                    startTime = info.presentationTimeUs;
                                }
                            }
                            if (end < 0 || info.presentationTimeUs < end) {
                                info.offset = 0;
                                info.flags = extractor.getSampleFlags();
                                long availableSize = mP4Builder.writeSampleData(muxerTrackIndex, buffer, info, false);
                                if (availableSize == 0) {
                                    mediaCodecVideoConvertor = this;
                                    buffer2 = buffer;
                                } else if (this.callback == null) {
                                    buffer2 = buffer;
                                    mediaCodecVideoConvertor = this;
                                } else {
                                    if (info.presentationTimeUs - startTime <= currentPts) {
                                        currentPts2 = currentPts;
                                    } else {
                                        long currentPts3 = info.presentationTimeUs - startTime;
                                        currentPts2 = currentPts3;
                                    }
                                    mediaCodecVideoConvertor = this;
                                    buffer2 = buffer;
                                    mediaCodecVideoConvertor.callback.didWriteData(availableSize, (((float) currentPts2) / 1000.0f) / durationS);
                                    currentPts = currentPts2;
                                }
                            } else {
                                eof = true;
                                mediaCodecVideoConvertor = this;
                                buffer2 = buffer;
                            }
                        }
                        if (!eof) {
                            extractor.advance();
                        }
                    } else {
                        buffer2 = buffer;
                        writeStart = muxerAudioTrackIndex;
                        audioTrackIndex = audioTrackIndex3;
                        maxBufferSize2 = maxBufferSize;
                        mediaCodecVideoConvertor = this;
                        if (index == -1) {
                            eof = true;
                        } else {
                            extractor.advance();
                            eof = false;
                        }
                    }
                    if (!eof) {
                        inputDone = true;
                    }
                    mP4Builder = mediaMuxer;
                    buffer4 = buffer2;
                    muxerAudioTrackIndex = writeStart;
                    maxBufferSize = maxBufferSize2;
                    audioTrackIndex3 = audioTrackIndex;
                }
            }
            buffer = buffer3;
            info.size = extractor.readSampleData(buffer, 0);
            index = extractor.getSampleTrackIndex();
            if (index != videoTrackIndex) {
            }
            if (muxerTrackIndex == -1) {
            }
            if (!eof) {
            }
            mP4Builder = mediaMuxer;
            buffer4 = buffer2;
            muxerAudioTrackIndex = writeStart;
            maxBufferSize = maxBufferSize2;
            audioTrackIndex3 = audioTrackIndex;
        }
        int audioTrackIndex4 = audioTrackIndex3;
        if (videoTrackIndex >= 0) {
            extractor.unselectTrack(videoTrackIndex);
        }
        if (audioTrackIndex4 >= 0) {
            extractor.unselectTrack(audioTrackIndex4);
        }
        return startTime;
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener != null && videoConvertorListener.checkConversionCanceled()) {
            throw new ConversionCanceledException();
        }
    }

    private static String createFragmentShader(int srcWidth, int srcHeight, int dstWidth, int dstHeight, boolean external) {
        float kernelSize = Utilities.clamp((Math.max(srcWidth, srcHeight) / Math.max(dstHeight, dstWidth)) * 0.8f, 2.0f, 1.0f);
        int kernelRadius = (int) kernelSize;
        FileLog.d("source size " + srcWidth + "x" + srcHeight + "    dest size " + dstWidth + dstHeight + "   kernelRadius " + kernelRadius);
        if (external) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + kernelRadius + ".0;\nconst float pixelSizeX = 1.0 / " + srcWidth + ".0;\nconst float pixelSizeY = 1.0 / " + srcHeight + ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + kernelRadius + ".0;\nconst float pixelSizeX = 1.0 / " + srcHeight + ".0;\nconst float pixelSizeY = 1.0 / " + srcWidth + ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
    }

    /* loaded from: classes4.dex */
    public class ConversionCanceledException extends RuntimeException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ConversionCanceledException() {
            super("canceled conversion");
            MediaCodecVideoConvertor.this = this$0;
        }
    }
}
