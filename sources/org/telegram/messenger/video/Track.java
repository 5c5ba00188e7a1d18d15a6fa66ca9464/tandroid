package org.telegram.messenger.video;

import android.media.MediaCodec;
import android.media.MediaFormat;
import com.coremedia.iso.boxes.AbstractMediaHeaderBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.video.Track;
/* loaded from: classes3.dex */
public class Track {
    private static Map<Integer, Integer> samplingFrequencyIndexMap;
    private String handler;
    private AbstractMediaHeaderBox headerBox;
    private int height;
    private boolean isAudio;
    private int[] sampleCompositions;
    private SampleDescriptionBox sampleDescriptionBox;
    private long[] sampleDurations;
    private LinkedList<Integer> syncSamples;
    private int timeScale;
    private long trackId;
    private float volume;
    private int width;
    private ArrayList<Sample> samples = new ArrayList<>();
    private long duration = 0;
    private Date creationTime = new Date();
    private ArrayList<SamplePresentationTime> samplePresentationTimes = new ArrayList<>();
    private boolean first = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class SamplePresentationTime {
        private long dt;
        private int index;
        private long presentationTime;

        public SamplePresentationTime(int i, long j) {
            this.index = i;
            this.presentationTime = j;
        }
    }

    static {
        HashMap hashMap = new HashMap();
        samplingFrequencyIndexMap = hashMap;
        hashMap.put(96000, 0);
        samplingFrequencyIndexMap.put(88200, 1);
        samplingFrequencyIndexMap.put(64000, 2);
        samplingFrequencyIndexMap.put(48000, 3);
        samplingFrequencyIndexMap.put(44100, 4);
        samplingFrequencyIndexMap.put(32000, 5);
        samplingFrequencyIndexMap.put(24000, 6);
        samplingFrequencyIndexMap.put(22050, 7);
        samplingFrequencyIndexMap.put(16000, 8);
        samplingFrequencyIndexMap.put(12000, 9);
        samplingFrequencyIndexMap.put(11025, 10);
        samplingFrequencyIndexMap.put(8000, 11);
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Track(int i, MediaFormat mediaFormat, boolean z) {
        int i2;
        int i3;
        this.syncSamples = null;
        this.volume = 0.0f;
        this.trackId = i;
        this.isAudio = z;
        if (z) {
            this.volume = 1.0f;
            this.timeScale = mediaFormat.getInteger("sample-rate");
            this.handler = "soun";
            this.headerBox = new SoundMediaHeaderBox();
            this.sampleDescriptionBox = new SampleDescriptionBox();
            AudioSampleEntry audioSampleEntry = new AudioSampleEntry("mp4a");
            audioSampleEntry.setChannelCount(mediaFormat.getInteger("channel-count"));
            audioSampleEntry.setSampleRate(mediaFormat.getInteger("sample-rate"));
            audioSampleEntry.setDataReferenceIndex(1);
            audioSampleEntry.setSampleSize(16);
            ESDescriptorBox eSDescriptorBox = new ESDescriptorBox();
            ESDescriptor eSDescriptor = new ESDescriptor();
            eSDescriptor.setEsId(0);
            SLConfigDescriptor sLConfigDescriptor = new SLConfigDescriptor();
            sLConfigDescriptor.setPredefined(2);
            eSDescriptor.setSlConfigDescriptor(sLConfigDescriptor);
            String string = mediaFormat.containsKey("mime") ? mediaFormat.getString("mime") : "audio/mp4-latm";
            DecoderConfigDescriptor decoderConfigDescriptor = new DecoderConfigDescriptor();
            if ("audio/mpeg".equals(string)) {
                decoderConfigDescriptor.setObjectTypeIndication(105);
            } else {
                decoderConfigDescriptor.setObjectTypeIndication(64);
            }
            decoderConfigDescriptor.setStreamType(5);
            decoderConfigDescriptor.setBufferSizeDB(1536);
            decoderConfigDescriptor.setMaxBitRate(mediaFormat.containsKey("max-bitrate") ? mediaFormat.getInteger("max-bitrate") : 96000L);
            decoderConfigDescriptor.setAvgBitRate(this.timeScale);
            AudioSpecificConfig audioSpecificConfig = new AudioSpecificConfig();
            audioSpecificConfig.setAudioObjectType(2);
            audioSpecificConfig.setSamplingFrequencyIndex(samplingFrequencyIndexMap.get(Integer.valueOf((int) audioSampleEntry.getSampleRate())).intValue());
            audioSpecificConfig.setChannelConfiguration(audioSampleEntry.getChannelCount());
            decoderConfigDescriptor.setAudioSpecificInfo(audioSpecificConfig);
            eSDescriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);
            eSDescriptorBox.setData(eSDescriptor.serialize());
            audioSampleEntry.addBox(eSDescriptorBox);
            this.sampleDescriptionBox.addBox(audioSampleEntry);
            return;
        }
        this.width = mediaFormat.getInteger("width");
        this.height = mediaFormat.getInteger("height");
        this.timeScale = 90000;
        this.syncSamples = new LinkedList<>();
        this.handler = "vide";
        this.headerBox = new VideoMediaHeaderBox();
        this.sampleDescriptionBox = new SampleDescriptionBox();
        String string2 = mediaFormat.getString("mime");
        if (!string2.equals(MediaController.VIDEO_MIME_TYPE)) {
            if (string2.equals("video/mp4v")) {
                VisualSampleEntry visualSampleEntry = new VisualSampleEntry("mp4v");
                visualSampleEntry.setDataReferenceIndex(1);
                visualSampleEntry.setDepth(24);
                visualSampleEntry.setFrameCount(1);
                visualSampleEntry.setHorizresolution(72.0d);
                visualSampleEntry.setVertresolution(72.0d);
                visualSampleEntry.setWidth(this.width);
                visualSampleEntry.setHeight(this.height);
                this.sampleDescriptionBox.addBox(visualSampleEntry);
                return;
            } else if (!string2.equals("video/hevc") || mediaFormat.getByteBuffer("csd-0") == null) {
                return;
            } else {
                byte[] array = mediaFormat.getByteBuffer("csd-0").array();
                int i4 = -1;
                int i5 = -1;
                int i6 = 0;
                int i7 = -1;
                for (int i8 = 0; i8 < array.length; i8++) {
                    if (i6 == 3 && array[i8] == 1) {
                        if (i7 == -1) {
                            i7 = i8 - 3;
                        } else if (i5 == -1) {
                            i5 = i8 - 3;
                        } else if (i4 == -1) {
                            i4 = i8 - 3;
                        }
                    }
                    i6 = array[i8] == 0 ? i6 + 1 : 0;
                }
                byte[] bArr = new byte[i5 - 4];
                byte[] bArr2 = new byte[(i4 - i5) - 4];
                byte[] bArr3 = new byte[(array.length - i4) - 4];
                for (int i9 = 0; i9 < array.length; i9++) {
                    if (i9 < i5) {
                        int i10 = i9 - 4;
                        if (i10 >= 0) {
                            bArr[i10] = array[i9];
                        }
                    } else if (i9 < i4) {
                        int i11 = (i9 - i5) - 4;
                        if (i11 >= 0) {
                            bArr2[i11] = array[i9];
                        }
                    } else {
                        int i12 = (i9 - i4) - 4;
                        if (i12 >= 0) {
                            bArr3[i12] = array[i9];
                        }
                    }
                }
                try {
                    VisualSampleEntry parseFromCsd = HevcDecoderConfigurationRecord.parseFromCsd(Arrays.asList(ByteBuffer.wrap(bArr), ByteBuffer.wrap(bArr3), ByteBuffer.wrap(bArr2)));
                    parseFromCsd.setWidth(this.width);
                    parseFromCsd.setHeight(this.height);
                    this.sampleDescriptionBox.addBox(parseFromCsd);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        VisualSampleEntry visualSampleEntry2 = new VisualSampleEntry("avc1");
        visualSampleEntry2.setDataReferenceIndex(1);
        visualSampleEntry2.setDepth(24);
        visualSampleEntry2.setFrameCount(1);
        visualSampleEntry2.setHorizresolution(72.0d);
        visualSampleEntry2.setVertresolution(72.0d);
        visualSampleEntry2.setWidth(this.width);
        visualSampleEntry2.setHeight(this.height);
        AvcConfigurationBox avcConfigurationBox = new AvcConfigurationBox();
        if (mediaFormat.getByteBuffer("csd-0") != null) {
            ArrayList arrayList = new ArrayList();
            ByteBuffer byteBuffer = mediaFormat.getByteBuffer("csd-0");
            byteBuffer.position(4);
            byte[] bArr4 = new byte[byteBuffer.remaining()];
            byteBuffer.get(bArr4);
            arrayList.add(bArr4);
            ArrayList arrayList2 = new ArrayList();
            ByteBuffer byteBuffer2 = mediaFormat.getByteBuffer("csd-1");
            byteBuffer2.position(4);
            byte[] bArr5 = new byte[byteBuffer2.remaining()];
            byteBuffer2.get(bArr5);
            arrayList2.add(bArr5);
            avcConfigurationBox.setSequenceParameterSets(arrayList);
            avcConfigurationBox.setPictureParameterSets(arrayList2);
        }
        if (mediaFormat.containsKey("level")) {
            int integer = mediaFormat.getInteger("level");
            if (integer == 1) {
                i2 = 1;
            } else if (integer == 32) {
                i2 = 2;
            } else if (integer == 4) {
                i2 = 11;
            } else if (integer == 8) {
                i2 = 12;
            } else if (integer != 16) {
                if (integer == 64) {
                    i2 = 21;
                } else if (integer == 128) {
                    i2 = 22;
                } else if (integer == 256) {
                    i2 = 3;
                } else if (integer == 512) {
                    i2 = 31;
                } else if (integer == 1024) {
                    i2 = 32;
                } else if (integer == 2048) {
                    i2 = 4;
                } else if (integer == 4096) {
                    i2 = 41;
                } else if (integer == 8192) {
                    i2 = 42;
                } else if (integer == 16384) {
                    i2 = 5;
                } else if (integer == 32768) {
                    i2 = 51;
                } else if (integer != 65536) {
                    if (integer == 2) {
                        i2 = 27;
                    }
                    if (mediaFormat.containsKey("profile")) {
                        int integer2 = mediaFormat.getInteger("profile");
                        if (integer2 == 1) {
                            i3 = 66;
                        } else if (integer2 == 2) {
                            i3 = 77;
                        } else if (integer2 == 4) {
                            i3 = 88;
                        } else if (integer2 != 8) {
                            if (integer2 == 16) {
                                i3 = 110;
                            } else if (integer2 != 32) {
                                if (integer2 == 64) {
                                    i3 = NotificationCenter.suggestedLangpack;
                                }
                                avcConfigurationBox.setBitDepthLumaMinus8(-1);
                                avcConfigurationBox.setBitDepthChromaMinus8(-1);
                                avcConfigurationBox.setChromaFormat(-1);
                                avcConfigurationBox.setConfigurationVersion(1);
                                avcConfigurationBox.setLengthSizeMinusOne(3);
                                avcConfigurationBox.setProfileCompatibility(0);
                                visualSampleEntry2.addBox(avcConfigurationBox);
                                this.sampleDescriptionBox.addBox(visualSampleEntry2);
                            } else {
                                i3 = 122;
                            }
                        }
                        avcConfigurationBox.setAvcProfileIndication(i3);
                        avcConfigurationBox.setBitDepthLumaMinus8(-1);
                        avcConfigurationBox.setBitDepthChromaMinus8(-1);
                        avcConfigurationBox.setChromaFormat(-1);
                        avcConfigurationBox.setConfigurationVersion(1);
                        avcConfigurationBox.setLengthSizeMinusOne(3);
                        avcConfigurationBox.setProfileCompatibility(0);
                        visualSampleEntry2.addBox(avcConfigurationBox);
                        this.sampleDescriptionBox.addBox(visualSampleEntry2);
                    }
                    i3 = 100;
                    avcConfigurationBox.setAvcProfileIndication(i3);
                    avcConfigurationBox.setBitDepthLumaMinus8(-1);
                    avcConfigurationBox.setBitDepthChromaMinus8(-1);
                    avcConfigurationBox.setChromaFormat(-1);
                    avcConfigurationBox.setConfigurationVersion(1);
                    avcConfigurationBox.setLengthSizeMinusOne(3);
                    avcConfigurationBox.setProfileCompatibility(0);
                    visualSampleEntry2.addBox(avcConfigurationBox);
                    this.sampleDescriptionBox.addBox(visualSampleEntry2);
                } else {
                    i2 = 52;
                }
            }
            avcConfigurationBox.setAvcLevelIndication(i2);
            if (mediaFormat.containsKey("profile")) {
            }
            i3 = 100;
            avcConfigurationBox.setAvcProfileIndication(i3);
            avcConfigurationBox.setBitDepthLumaMinus8(-1);
            avcConfigurationBox.setBitDepthChromaMinus8(-1);
            avcConfigurationBox.setChromaFormat(-1);
            avcConfigurationBox.setConfigurationVersion(1);
            avcConfigurationBox.setLengthSizeMinusOne(3);
            avcConfigurationBox.setProfileCompatibility(0);
            visualSampleEntry2.addBox(avcConfigurationBox);
            this.sampleDescriptionBox.addBox(visualSampleEntry2);
        }
        i2 = 13;
        avcConfigurationBox.setAvcLevelIndication(i2);
        if (mediaFormat.containsKey("profile")) {
        }
        i3 = 100;
        avcConfigurationBox.setAvcProfileIndication(i3);
        avcConfigurationBox.setBitDepthLumaMinus8(-1);
        avcConfigurationBox.setBitDepthChromaMinus8(-1);
        avcConfigurationBox.setChromaFormat(-1);
        avcConfigurationBox.setConfigurationVersion(1);
        avcConfigurationBox.setLengthSizeMinusOne(3);
        avcConfigurationBox.setProfileCompatibility(0);
        visualSampleEntry2.addBox(avcConfigurationBox);
        this.sampleDescriptionBox.addBox(visualSampleEntry2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$prepare$0(SamplePresentationTime samplePresentationTime, SamplePresentationTime samplePresentationTime2) {
        if (samplePresentationTime.presentationTime > samplePresentationTime2.presentationTime) {
            return 1;
        }
        return samplePresentationTime.presentationTime < samplePresentationTime2.presentationTime ? -1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0008, code lost:
        if ((r8.flags & 1) != 0) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void addSample(long j, MediaCodec.BufferInfo bufferInfo) {
        boolean z = this.isAudio ? false : true;
        this.samples.add(new Sample(j, bufferInfo.size));
        LinkedList<Integer> linkedList = this.syncSamples;
        if (linkedList != null && z) {
            linkedList.add(Integer.valueOf(this.samples.size()));
        }
        ArrayList<SamplePresentationTime> arrayList = this.samplePresentationTimes;
        arrayList.add(new SamplePresentationTime(arrayList.size(), ((bufferInfo.presentationTimeUs * this.timeScale) + 500000) / 1000000));
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getHandler() {
        return this.handler;
    }

    public int getHeight() {
        return this.height;
    }

    public long getLastFrameTimestamp() {
        long j = this.duration;
        long[] jArr = this.sampleDurations;
        return (((j - jArr[jArr.length - 1]) * 1000000) - 500000) / this.timeScale;
    }

    public AbstractMediaHeaderBox getMediaHeaderBox() {
        return this.headerBox;
    }

    public int[] getSampleCompositions() {
        return this.sampleCompositions;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        return this.sampleDurations;
    }

    public ArrayList<Sample> getSamples() {
        return this.samples;
    }

    public long[] getSyncSamples() {
        LinkedList<Integer> linkedList = this.syncSamples;
        if (linkedList == null || linkedList.isEmpty()) {
            return null;
        }
        long[] jArr = new long[this.syncSamples.size()];
        for (int i = 0; i < this.syncSamples.size(); i++) {
            jArr[i] = this.syncSamples.get(i).intValue();
        }
        return jArr;
    }

    public int getTimeScale() {
        return this.timeScale;
    }

    public long getTrackId() {
        return this.trackId;
    }

    public float getVolume() {
        return this.volume;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean isAudio() {
        return this.isAudio;
    }

    public void prepare() {
        int i;
        this.duration = 0L;
        ArrayList arrayList = new ArrayList(this.samplePresentationTimes);
        Collections.sort(this.samplePresentationTimes, new Comparator() { // from class: org.telegram.messenger.video.Track$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$prepare$0;
                lambda$prepare$0 = Track.lambda$prepare$0((Track.SamplePresentationTime) obj, (Track.SamplePresentationTime) obj2);
                return lambda$prepare$0;
            }
        });
        this.sampleDurations = new long[this.samplePresentationTimes.size()];
        long j = Long.MAX_VALUE;
        long j2 = 0;
        int i2 = 0;
        boolean z = false;
        while (true) {
            if (i2 >= this.samplePresentationTimes.size()) {
                break;
            }
            SamplePresentationTime samplePresentationTime = this.samplePresentationTimes.get(i2);
            long j3 = samplePresentationTime.presentationTime - j2;
            j2 = samplePresentationTime.presentationTime;
            this.sampleDurations[samplePresentationTime.index] = j3;
            int i3 = i2;
            if (samplePresentationTime.index != 0) {
                this.duration += j3;
            }
            if (j3 > 0 && j3 < 2147483647L) {
                j = Math.min(j, j3);
            }
            if (samplePresentationTime.index != i3) {
                z = true;
            }
            i2 = i3 + 1;
        }
        long[] jArr = this.sampleDurations;
        if (jArr.length > 0) {
            jArr[0] = j;
            this.duration += j;
        }
        for (i = 1; i < arrayList.size(); i++) {
            ((SamplePresentationTime) arrayList.get(i)).dt = this.sampleDurations[i] + ((SamplePresentationTime) arrayList.get(i - 1)).dt;
        }
        if (z) {
            this.sampleCompositions = new int[this.samplePresentationTimes.size()];
            for (int i4 = 0; i4 < this.samplePresentationTimes.size(); i4++) {
                SamplePresentationTime samplePresentationTime2 = this.samplePresentationTimes.get(i4);
                this.sampleCompositions[samplePresentationTime2.index] = (int) (samplePresentationTime2.presentationTime - samplePresentationTime2.dt);
            }
        }
    }
}
