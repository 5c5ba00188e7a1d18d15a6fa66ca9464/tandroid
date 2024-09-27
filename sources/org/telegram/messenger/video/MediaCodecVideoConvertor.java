package org.telegram.messenger.video;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.video.audio_input.AudioInput;
import org.telegram.messenger.video.audio_input.GeneralAudioInput;
import org.telegram.ui.Stories.recorder.StoryEntry;
/* loaded from: classes3.dex */
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
    private Muxer muxer;
    private String outputMimeType;

    /* loaded from: classes3.dex */
    public class ConversionCanceledException extends RuntimeException {
        public ConversionCanceledException() {
            super("canceled conversion");
        }
    }

    /* loaded from: classes3.dex */
    public static class ConvertVideoParams {
        int account;
        long avatarStartTime;
        String backgroundPath;
        int bitrate;
        String blurPath;
        File cacheFile;
        MediaController.VideoConvertorListener callback;
        MediaController.CropState cropState;
        long duration;
        long endTime;
        int framerate;
        Integer gradientBottomColor;
        Integer gradientTopColor;
        StoryEntry.HDRInfo hdrInfo;
        boolean isDark;
        boolean isPhoto;
        boolean isRound;
        boolean isSecret;
        boolean isSticker;
        boolean isStory;
        ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
        String messagePath;
        String messageVideoMaskPath;
        boolean muted;
        boolean needCompress;
        int originalBitrate;
        int originalHeight;
        int originalWidth;
        String paintPath;
        int resultHeight;
        int resultWidth;
        int rotationValue;
        MediaController.SavedFilterState savedFilterState;
        public ArrayList<MixedSoundInfo> soundInfos = new ArrayList<>();
        long startTime;
        String videoPath;
        float volume;
        long wallpaperPeerId;

        private ConvertVideoParams() {
        }

        public static ConvertVideoParams of(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, boolean z2, long j4, MediaController.VideoConvertorListener videoConvertorListener, VideoEditedInfo videoEditedInfo) {
            ConvertVideoParams convertVideoParams = new ConvertVideoParams();
            convertVideoParams.videoPath = str;
            convertVideoParams.cacheFile = file;
            convertVideoParams.rotationValue = i;
            convertVideoParams.isSecret = z;
            convertVideoParams.originalWidth = i2;
            convertVideoParams.originalHeight = i3;
            convertVideoParams.resultWidth = i4;
            convertVideoParams.resultHeight = i5;
            convertVideoParams.framerate = i6;
            convertVideoParams.bitrate = i7;
            convertVideoParams.originalBitrate = i8;
            convertVideoParams.startTime = j;
            convertVideoParams.endTime = j2;
            convertVideoParams.avatarStartTime = j3;
            convertVideoParams.needCompress = z2;
            convertVideoParams.duration = j4;
            convertVideoParams.savedFilterState = videoEditedInfo.filterState;
            convertVideoParams.paintPath = videoEditedInfo.paintPath;
            convertVideoParams.blurPath = videoEditedInfo.blurPath;
            convertVideoParams.mediaEntities = videoEditedInfo.mediaEntities;
            convertVideoParams.isPhoto = videoEditedInfo.isPhoto;
            convertVideoParams.cropState = videoEditedInfo.cropState;
            convertVideoParams.isRound = videoEditedInfo.roundVideo;
            convertVideoParams.callback = videoConvertorListener;
            convertVideoParams.gradientTopColor = videoEditedInfo.gradientTopColor;
            convertVideoParams.gradientBottomColor = videoEditedInfo.gradientBottomColor;
            convertVideoParams.muted = videoEditedInfo.muted;
            convertVideoParams.volume = videoEditedInfo.volume;
            convertVideoParams.isStory = videoEditedInfo.isStory;
            convertVideoParams.hdrInfo = videoEditedInfo.hdrInfo;
            convertVideoParams.isDark = videoEditedInfo.isDark;
            convertVideoParams.wallpaperPeerId = videoEditedInfo.wallpaperPeerId;
            convertVideoParams.account = videoEditedInfo.account;
            convertVideoParams.messagePath = videoEditedInfo.messagePath;
            convertVideoParams.messageVideoMaskPath = videoEditedInfo.messageVideoMaskPath;
            convertVideoParams.backgroundPath = videoEditedInfo.backgroundPath;
            convertVideoParams.isSticker = videoEditedInfo.isSticker;
            return convertVideoParams;
        }
    }

    /* loaded from: classes3.dex */
    public static class MixedSoundInfo {
        final String audioFile;
        public long audioOffset;
        public long duration;
        public long startTime;
        public float volume = 1.0f;

        public MixedSoundInfo(String str) {
            this.audioFile = str;
        }
    }

    /* loaded from: classes3.dex */
    public static class Muxer {
        public final MediaMuxer mediaMuxer;
        public final MP4Builder mp4Builder;
        private boolean started;

        public Muxer(MediaMuxer mediaMuxer) {
            this.started = false;
            this.mp4Builder = null;
            this.mediaMuxer = mediaMuxer;
        }

        public Muxer(MP4Builder mP4Builder) {
            this.started = false;
            this.mp4Builder = mP4Builder;
            this.mediaMuxer = null;
        }

        public int addTrack(MediaFormat mediaFormat, boolean z) {
            MediaMuxer mediaMuxer = this.mediaMuxer;
            if (mediaMuxer != null) {
                return mediaMuxer.addTrack(mediaFormat);
            }
            MP4Builder mP4Builder = this.mp4Builder;
            if (mP4Builder != null) {
                return mP4Builder.addTrack(mediaFormat, z);
            }
            return 0;
        }

        public void finishMovie() {
            MediaMuxer mediaMuxer = this.mediaMuxer;
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                this.mediaMuxer.release();
                return;
            }
            MP4Builder mP4Builder = this.mp4Builder;
            if (mP4Builder != null) {
                mP4Builder.finishMovie();
            }
        }

        public long getLastFrameTimestamp(int i, MediaCodec.BufferInfo bufferInfo) {
            if (this.mediaMuxer != null) {
                return bufferInfo.presentationTimeUs;
            }
            MP4Builder mP4Builder = this.mp4Builder;
            if (mP4Builder != null) {
                return mP4Builder.getLastFrameTimestamp(i);
            }
            return 0L;
        }

        public void start() {
            MediaMuxer mediaMuxer = this.mediaMuxer;
            if (mediaMuxer != null) {
                mediaMuxer.start();
            }
        }

        public long writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, boolean z) {
            MediaMuxer mediaMuxer = this.mediaMuxer;
            if (mediaMuxer == null) {
                MP4Builder mP4Builder = this.mp4Builder;
                if (mP4Builder != null) {
                    return mP4Builder.writeSampleData(i, byteBuffer, bufferInfo, z);
                }
                return 0L;
            }
            if (!this.started) {
                mediaMuxer.start();
                this.started = true;
            }
            this.mediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
            return 0L;
        }
    }

    private static void applyAudioInputs(ArrayList<MixedSoundInfo> arrayList, ArrayList<AudioInput> arrayList2) {
        if (arrayList == null) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            MixedSoundInfo mixedSoundInfo = arrayList.get(i);
            GeneralAudioInput generalAudioInput = new GeneralAudioInput(mixedSoundInfo.audioFile);
            generalAudioInput.setVolume(mixedSoundInfo.volume);
            long j = mixedSoundInfo.startTime;
            if (j > 0) {
                generalAudioInput.setStartOffsetUs(j);
            }
            long j2 = mixedSoundInfo.audioOffset;
            if (j2 > 0) {
                generalAudioInput.setStartTimeUs(j2);
            } else {
                j2 = 0;
            }
            long j3 = mixedSoundInfo.duration;
            if (j3 > 0) {
                generalAudioInput.setEndTimeUs(j2 + j3);
            }
            arrayList2.add(generalAudioInput);
        }
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener != null && videoConvertorListener.checkConversionCanceled()) {
            throw new ConversionCanceledException();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:525:0x0bdf
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private boolean convertVideoInternal(org.telegram.messenger.video.MediaCodecVideoConvertor.ConvertVideoParams r94, boolean r95, int r96) {
        /*
            Method dump skipped, instructions count: 7962
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.video.MediaCodecVideoConvertor.convertVideoInternal(org.telegram.messenger.video.MediaCodecVideoConvertor$ConvertVideoParams, boolean, int):boolean");
    }

    private MediaCodec createEncoderForMimeType() {
        MediaCodec createEncoderByType;
        if (!this.outputMimeType.equals("video/hevc") || Build.VERSION.SDK_INT < 29) {
            if (this.outputMimeType.equals("video/hevc")) {
                this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            }
            createEncoderByType = MediaCodec.createEncoderByType(this.outputMimeType);
        } else {
            String findGoodHevcEncoder = SharedConfig.findGoodHevcEncoder();
            createEncoderByType = findGoodHevcEncoder != null ? MediaCodec.createByCodecName(findGoodHevcEncoder) : null;
        }
        if (createEncoderByType == null && this.outputMimeType.equals("video/hevc")) {
            this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            return MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
        }
        return createEncoderByType;
    }

    private static String createFragmentShader(int i, int i2, int i3, int i4, boolean z, int i5) {
        StringBuilder sb;
        String str;
        int clamp = (int) Utilities.clamp((Math.max(i, i2) / Math.max(i4, i3)) * 0.8f, 2.0f, 1.0f);
        if (clamp > 1 && SharedConfig.deviceIsAverage()) {
            clamp = 1;
        }
        int min = Math.min(i5, clamp);
        FileLog.d("source size " + i + "x" + i2 + "    dest size " + i3 + i4 + "   kernelRadius " + min);
        if (z) {
            sb = new StringBuilder();
            sb.append("#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = ");
            sb.append(min);
            sb.append(".0;\nconst float pixelSizeX = 1.0 / ");
            sb.append(i);
            sb.append(".0;\nconst float pixelSizeY = 1.0 / ");
            sb.append(i2);
            str = ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        } else {
            sb = new StringBuilder();
            sb.append("precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = ");
            sb.append(min);
            sb.append(".0;\nconst float pixelSizeX = 1.0 / ");
            sb.append(i2);
            sb.append(".0;\nconst float pixelSizeY = 1.0 / ");
            sb.append(i);
            str = ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        sb.append(str);
        return sb.toString();
    }

    public static void cutOfNalData(String str, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        int i = str.equals("video/hevc") ? 3 : 1;
        if (bufferInfo.size > 100) {
            byteBuffer.position(bufferInfo.offset);
            byte[] bArr = new byte[100];
            byteBuffer.get(bArr);
            int i2 = 0;
            for (int i3 = 0; i3 < 96; i3++) {
                if (bArr[i3] == 0 && bArr[i3 + 1] == 0 && bArr[i3 + 2] == 0 && bArr[i3 + 3] == 1 && (i2 = i2 + 1) > i) {
                    bufferInfo.offset += i3;
                    bufferInfo.size -= i3;
                    return;
                }
            }
        }
    }

    private MediaCodec getDecoderByFormat(MediaFormat mediaFormat) {
        if (mediaFormat != null) {
            ArrayList arrayList = new ArrayList();
            String string = mediaFormat.getString("mime");
            arrayList.add(string);
            if ("video/dolby-vision".equals(string)) {
                arrayList.add("video/hevc");
                arrayList.add(MediaController.VIDEO_MIME_TYPE);
            }
            Exception exc = null;
            while (!arrayList.isEmpty()) {
                try {
                    String str = (String) arrayList.remove(0);
                    mediaFormat.setString("mime", str);
                    return MediaCodec.createDecoderByType(str);
                } catch (Exception e) {
                    if (exc == null) {
                        exc = e;
                    }
                }
            }
            throw new RuntimeException(exc);
        }
        throw new RuntimeException("getDecoderByFormat: format is null");
    }

    private static String hdrFragmentShader(int i, int i2, int i3, int i4, boolean z, StoryEntry.HDRInfo hDRInfo) {
        if (z) {
            String readRes = AndroidUtilities.readRes(hDRInfo.getHDRType() == 1 ? R.raw.hdr2sdr_hlg : R.raw.hdr2sdr_pq);
            String replace = readRes.replace("$dstWidth", i3 + ".0");
            String replace2 = replace.replace("$dstHeight", i4 + ".0");
            return replace2 + "\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_FragColor = TEX(vTextureCoord);\n}";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    }

    private boolean isMediatekAvcEncoder(MediaCodec mediaCodec) {
        return mediaCodec.getName().equals("c2.mtk.avc.encoder");
    }

    /* JADX WARN: Code restructure failed: missing block: B:121:0x01ce, code lost:
        if (r8 == false) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0120, code lost:
        if (r13[r15 + 3] != 1) goto L63;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long readAndWriteTracks(MediaExtractor mediaExtractor, Muxer muxer, MediaCodec.BufferInfo bufferInfo, long j, long j2, long j3, File file, boolean z) {
        long j4;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        boolean z2;
        int i7;
        int i8;
        boolean z3;
        long j5;
        byte[] array;
        int i9;
        int i10;
        long sampleSize;
        int i11;
        int findTrack = MediaController.findTrack(mediaExtractor, false);
        if (z) {
            j4 = j3;
            i = MediaController.findTrack(mediaExtractor, true);
        } else {
            j4 = j3;
            i = -1;
        }
        float f = ((float) j4) / 1000.0f;
        if (findTrack >= 0) {
            mediaExtractor.selectTrack(findTrack);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(findTrack);
            i3 = muxer.addTrack(trackFormat, false);
            try {
                i11 = trackFormat.getInteger("max-input-size");
            } catch (Exception e) {
                FileLog.e(e);
                i11 = 0;
            }
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
            } else {
                mediaExtractor.seekTo(0L, 0);
            }
            i2 = i11;
        } else {
            i2 = 0;
            i3 = -1;
        }
        if (i >= 0) {
            mediaExtractor.selectTrack(i);
            MediaFormat trackFormat2 = mediaExtractor.getTrackFormat(i);
            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                i4 = -1;
                i = -1;
            } else {
                i4 = muxer.addTrack(trackFormat2, true);
                try {
                    i2 = Math.max(trackFormat2.getInteger("max-input-size"), i2);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (j > 0) {
                    mediaExtractor.seekTo(j, 0);
                } else {
                    mediaExtractor.seekTo(0L, 0);
                }
            }
        } else {
            i4 = -1;
        }
        if (i2 <= 0) {
            i2 = 65536;
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
        if (i >= 0 || findTrack >= 0) {
            checkConversionCanceled();
            long j6 = 0;
            long j7 = -1;
            boolean z4 = false;
            while (!z4) {
                checkConversionCanceled();
                int i12 = Build.VERSION.SDK_INT;
                if (i12 >= 28) {
                    sampleSize = mediaExtractor.getSampleSize();
                    i5 = i;
                    if (sampleSize > i2) {
                        int i13 = (int) (sampleSize + 1024);
                        i2 = i13;
                        allocateDirect = ByteBuffer.allocateDirect(i13);
                    }
                } else {
                    i5 = i;
                }
                bufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
                int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                int i14 = i5;
                int i15 = sampleTrackIndex == findTrack ? i3 : sampleTrackIndex == i14 ? i4 : -1;
                if (i15 != -1) {
                    if (i12 < 21) {
                        allocateDirect.position(0);
                        allocateDirect.limit(bufferInfo.size);
                    }
                    if (sampleTrackIndex != i14 && (array = allocateDirect.array()) != null) {
                        int arrayOffset = allocateDirect.arrayOffset();
                        int limit = arrayOffset + allocateDirect.limit();
                        i6 = i4;
                        int i16 = -1;
                        while (true) {
                            z2 = z4;
                            int i17 = limit - 4;
                            if (arrayOffset > i17) {
                                break;
                            }
                            if (array[arrayOffset] == 0 && array[arrayOffset + 1] == 0 && array[arrayOffset + 2] == 0) {
                                i9 = i2;
                                i10 = i14;
                            } else {
                                i9 = i2;
                                i10 = i14;
                            }
                            if (arrayOffset != i17) {
                                arrayOffset++;
                                z4 = z2;
                                i14 = i10;
                                i2 = i9;
                            }
                            if (i16 != -1) {
                                int i18 = (arrayOffset - i16) - (arrayOffset == i17 ? 0 : 4);
                                array[i16] = (byte) (i18 >> 24);
                                array[i16 + 1] = (byte) (i18 >> 16);
                                array[i16 + 2] = (byte) (i18 >> 8);
                                array[i16 + 3] = (byte) i18;
                            }
                            i16 = arrayOffset;
                            arrayOffset++;
                            z4 = z2;
                            i14 = i10;
                            i2 = i9;
                        }
                    } else {
                        i6 = i4;
                        z2 = z4;
                    }
                    i7 = i2;
                    i8 = i14;
                    if (bufferInfo.size >= 0) {
                        bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                        z3 = false;
                    } else {
                        bufferInfo.size = 0;
                        z3 = true;
                    }
                    if (bufferInfo.size > 0 && !z3) {
                        if (sampleTrackIndex == findTrack) {
                            j5 = 0;
                            if (j > 0 && j7 == -1) {
                                j7 = bufferInfo.presentationTimeUs;
                            }
                        } else {
                            j5 = 0;
                        }
                        if (j2 < j5 || bufferInfo.presentationTimeUs < j2) {
                            bufferInfo.offset = 0;
                            bufferInfo.flags = mediaExtractor.getSampleFlags();
                            long writeSampleData = muxer.writeSampleData(i15, allocateDirect, bufferInfo, false);
                            if (writeSampleData != 0) {
                                MediaController.VideoConvertorListener videoConvertorListener = this.callback;
                                if (videoConvertorListener != null) {
                                    long j8 = bufferInfo.presentationTimeUs - j7;
                                    if (j8 <= j6) {
                                        j8 = j6;
                                    }
                                    videoConvertorListener.didWriteData(writeSampleData, (((float) j8) / 1000.0f) / f);
                                    j6 = j8;
                                }
                            }
                        } else {
                            z3 = true;
                        }
                    }
                } else {
                    i6 = i4;
                    z2 = z4;
                    i7 = i2;
                    i8 = i14;
                    if (sampleTrackIndex == -1) {
                        z3 = true;
                    } else {
                        z3 = false;
                        mediaExtractor.advance();
                    }
                }
                i4 = i6;
                if (z3) {
                    i = i8;
                    i2 = i7;
                    z4 = true;
                } else {
                    z4 = z2;
                    i = i8;
                    i2 = i7;
                }
            }
            int i19 = i;
            if (findTrack >= 0) {
                mediaExtractor.unselectTrack(findTrack);
            }
            if (i19 >= 0) {
                mediaExtractor.unselectTrack(i19);
            }
            return j7;
        }
        return -1L;
    }

    public boolean convertVideo(ConvertVideoParams convertVideoParams) {
        if (convertVideoParams.isSticker) {
            return WebmEncoder.convert(convertVideoParams, 0);
        }
        this.callback = convertVideoParams.callback;
        return convertVideoInternal(convertVideoParams, false, 0);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }
}
