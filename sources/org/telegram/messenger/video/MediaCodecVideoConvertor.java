package org.telegram.messenger.video;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Stories.recorder.StoryEntry;
/* loaded from: classes.dex */
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
    private String outputMimeType;

    public boolean convertVideo(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, boolean z2, long j4, MediaController.SavedFilterState savedFilterState, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, boolean z3, MediaController.CropState cropState, boolean z4, MediaController.VideoConvertorListener videoConvertorListener, Integer num, Integer num2, boolean z5, boolean z6, StoryEntry.HDRInfo hDRInfo, ArrayList<StoryEntry.Part> arrayList2) {
        this.callback = videoConvertorListener;
        return convertVideoInternal(str, file, i, z, i2, i3, i4, i5, i6, i7, i8, j, j2, j3, j4, z2, false, savedFilterState, str2, arrayList, z3, cropState, z4, num, num2, z5, z6, hDRInfo, arrayList2, 0);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:381:0x0857
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @android.annotation.TargetApi(18)
    private boolean convertVideoInternal(java.lang.String r77, java.io.File r78, int r79, boolean r80, int r81, int r82, int r83, int r84, int r85, int r86, int r87, long r88, long r90, long r92, long r94, boolean r96, boolean r97, org.telegram.messenger.MediaController.SavedFilterState r98, java.lang.String r99, java.util.ArrayList<org.telegram.messenger.VideoEditedInfo.MediaEntity> r100, boolean r101, org.telegram.messenger.MediaController.CropState r102, boolean r103, java.lang.Integer r104, java.lang.Integer r105, boolean r106, boolean r107, org.telegram.ui.Stories.recorder.StoryEntry.HDRInfo r108, java.util.ArrayList<org.telegram.ui.Stories.recorder.StoryEntry.Part> r109, int r110) {
        /*
            Method dump skipped, instructions count: 7411
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.video.MediaCodecVideoConvertor.convertVideoInternal(java.lang.String, java.io.File, int, boolean, int, int, int, int, int, int, int, long, long, long, long, boolean, boolean, org.telegram.messenger.MediaController$SavedFilterState, java.lang.String, java.util.ArrayList, boolean, org.telegram.messenger.MediaController$CropState, boolean, java.lang.Integer, java.lang.Integer, boolean, boolean, org.telegram.ui.Stories.recorder.StoryEntry$HDRInfo, java.util.ArrayList, int):boolean");
    }

    private MediaCodec createEncoderForMimeType() throws IOException {
        MediaCodec createEncoderByType;
        if (this.outputMimeType.equals("video/hevc") && Build.VERSION.SDK_INT >= 29) {
            String findGoodHevcEncoder = SharedConfig.findGoodHevcEncoder();
            createEncoderByType = findGoodHevcEncoder != null ? MediaCodec.createByCodecName(findGoodHevcEncoder) : null;
        } else {
            this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            createEncoderByType = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
        }
        if (createEncoderByType == null && this.outputMimeType.equals("video/hevc")) {
            this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            return MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
        }
        return createEncoderByType;
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

    private boolean isMediatekAvcEncoder(MediaCodec mediaCodec) {
        return mediaCodec.getName().equals("c2.mtk.avc.encoder");
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x0123, code lost:
        if (r9[r6 + 3] != 1) goto L62;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long readAndWriteTracks(MediaExtractor mediaExtractor, MP4Builder mP4Builder, MediaCodec.BufferInfo bufferInfo, long j, long j2, long j3, File file, boolean z) throws Exception {
        long j4;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean z2;
        int i10;
        int i11;
        boolean z3;
        long j5;
        byte[] array;
        int i12;
        int i13;
        int i14;
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
            i3 = mP4Builder.addTrack(trackFormat, false);
            try {
                i14 = trackFormat.getInteger("max-input-size");
            } catch (Exception e) {
                FileLog.e(e);
                i14 = 0;
            }
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
            } else {
                mediaExtractor.seekTo(0L, 0);
            }
            i2 = i14;
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
                i4 = mP4Builder.addTrack(trackFormat2, true);
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
                int i15 = Build.VERSION.SDK_INT;
                if (i15 >= 28) {
                    long sampleSize = mediaExtractor.getSampleSize();
                    i5 = i;
                    if (sampleSize > i2) {
                        int i16 = (int) (sampleSize + 1024);
                        i2 = i16;
                        allocateDirect = ByteBuffer.allocateDirect(i16);
                    }
                } else {
                    i5 = i;
                }
                bufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
                int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                if (sampleTrackIndex == findTrack) {
                    i6 = i5;
                    i8 = i3;
                } else {
                    i6 = i5;
                    if (sampleTrackIndex == i6) {
                        i8 = i4;
                    } else {
                        i7 = -1;
                        i8 = -1;
                        if (i8 == i7) {
                            if (i15 < 21) {
                                allocateDirect.position(0);
                                allocateDirect.limit(bufferInfo.size);
                            }
                            if (sampleTrackIndex != i6 && (array = allocateDirect.array()) != null) {
                                int arrayOffset = allocateDirect.arrayOffset();
                                int limit = arrayOffset + allocateDirect.limit();
                                i9 = i4;
                                int i17 = arrayOffset;
                                int i18 = -1;
                                while (true) {
                                    z2 = z4;
                                    int i19 = limit - 4;
                                    if (i17 > i19) {
                                        break;
                                    }
                                    if (array[i17] == 0 && array[i17 + 1] == 0 && array[i17 + 2] == 0) {
                                        i12 = i2;
                                        i13 = i6;
                                    } else {
                                        i12 = i2;
                                        i13 = i6;
                                    }
                                    if (i17 != i19) {
                                        i17++;
                                        z4 = z2;
                                        i6 = i13;
                                        i2 = i12;
                                    }
                                    if (i18 != -1) {
                                        int i20 = (i17 - i18) - (i17 == i19 ? 0 : 4);
                                        array[i18] = (byte) (i20 >> 24);
                                        array[i18 + 1] = (byte) (i20 >> 16);
                                        array[i18 + 2] = (byte) (i20 >> 8);
                                        array[i18 + 3] = (byte) i20;
                                    }
                                    i18 = i17;
                                    i17++;
                                    z4 = z2;
                                    i6 = i13;
                                    i2 = i12;
                                }
                            } else {
                                i9 = i4;
                                z2 = z4;
                            }
                            i10 = i2;
                            i11 = i6;
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
                                    long writeSampleData = mP4Builder.writeSampleData(i8, allocateDirect, bufferInfo, false);
                                    if (writeSampleData != 0) {
                                        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
                                        if (videoConvertorListener != null) {
                                            long j8 = bufferInfo.presentationTimeUs;
                                            long j9 = j8 - j7 > j6 ? j8 - j7 : j6;
                                            videoConvertorListener.didWriteData(writeSampleData, (((float) j9) / 1000.0f) / f);
                                            j6 = j9;
                                            if (!z3) {
                                                mediaExtractor.advance();
                                            }
                                        }
                                        if (!z3) {
                                        }
                                    }
                                } else {
                                    z3 = true;
                                }
                            }
                            if (!z3) {
                            }
                        } else {
                            i9 = i4;
                            z2 = z4;
                            i10 = i2;
                            i11 = i6;
                            if (sampleTrackIndex == -1) {
                                z3 = true;
                            } else {
                                mediaExtractor.advance();
                                z3 = false;
                            }
                        }
                        z4 = !z3 ? true : z2;
                        i4 = i9;
                        i = i11;
                        i2 = i10;
                    }
                }
                i7 = -1;
                if (i8 == i7) {
                }
                if (!z3) {
                }
                i4 = i9;
                i = i11;
                i2 = i10;
            }
            int i21 = i;
            if (findTrack >= 0) {
                mediaExtractor.unselectTrack(findTrack);
            }
            if (i21 >= 0) {
                mediaExtractor.unselectTrack(i21);
            }
            return j7;
        }
        return -1L;
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener != null && videoConvertorListener.checkConversionCanceled()) {
            throw new ConversionCanceledException();
        }
    }

    private static String hdrFragmentShader(int i, int i2, int i3, int i4, boolean z, StoryEntry.HDRInfo hDRInfo) {
        String readRes;
        if (z) {
            if (hDRInfo.getHDRType() == 1) {
                readRes = RLottieDrawable.readRes(null, R.raw.yuv_hlg2rgb);
            } else {
                readRes = RLottieDrawable.readRes(null, R.raw.yuv_pq2rgb);
            }
            String replace = readRes.replace("$dstWidth", i3 + ".0");
            String replace2 = replace.replace("$dstHeight", i4 + ".0");
            return replace2 + "\nin vec2 vTextureCoord;\nout vec4 fragColor;\nvoid main() {\n    fragColor = TEX(vTextureCoord);\n}";
        }
        return "#version 320 es\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nout vec4 fragColor;\nvoid main() {\nfragColor = texture(sTexture, vTextureCoord);\n}\n";
    }

    private static String createFragmentShader(int i, int i2, int i3, int i4, boolean z, int i5) {
        int clamp = (int) Utilities.clamp((Math.max(i, i2) / Math.max(i4, i3)) * 0.8f, 2.0f, 1.0f);
        if (clamp > 1 && SharedConfig.deviceIsAverage()) {
            clamp = 1;
        }
        int min = Math.min(i5, clamp);
        FileLog.d("source size " + i + "x" + i2 + "    dest size " + i3 + i4 + "   kernelRadius " + min);
        if (z) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + min + ".0;\nconst float pixelSizeX = 1.0 / " + i + ".0;\nconst float pixelSizeY = 1.0 / " + i2 + ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + min + ".0;\nconst float pixelSizeX = 1.0 / " + i2 + ".0;\nconst float pixelSizeY = 1.0 / " + i + ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
    }

    /* loaded from: classes.dex */
    public class ConversionCanceledException extends RuntimeException {
        public ConversionCanceledException() {
            super("canceled conversion");
        }
    }

    private MediaCodec getDecoderByFormat(MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            throw new RuntimeException("getDecoderByFormat: format is null");
        }
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
}
