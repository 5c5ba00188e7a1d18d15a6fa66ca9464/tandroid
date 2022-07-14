package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
@Descriptor(tags = {4})
/* loaded from: classes3.dex */
public class DecoderConfigDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(DecoderConfigDescriptor.class.getName());
    AudioSpecificConfig audioSpecificInfo;
    long avgBitRate;
    int bufferSizeDB;
    byte[] configDescriptorDeadBytes;
    DecoderSpecificInfo decoderSpecificInfo;
    long maxBitRate;
    int objectTypeIndication;
    List<ProfileLevelIndicationDescriptor> profileLevelIndicationDescriptors = new ArrayList();
    int streamType;
    int upStream;

    /* JADX WARN: Incorrect condition in loop: B:20:0x0091 */
    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void parseDetail(ByteBuffer bb) throws IOException {
        int size;
        this.objectTypeIndication = IsoTypeReader.readUInt8(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamType = data >>> 2;
        this.upStream = (data >> 1) & 1;
        this.bufferSizeDB = IsoTypeReader.readUInt24(bb);
        this.maxBitRate = IsoTypeReader.readUInt32(bb);
        this.avgBitRate = IsoTypeReader.readUInt32(bb);
        if (bb.remaining() > 2) {
            int begin = bb.position();
            BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            int read = bb.position() - begin;
            Logger logger = log;
            StringBuilder sb = new StringBuilder();
            sb.append(descriptor);
            sb.append(" - DecoderConfigDescr1 read: ");
            sb.append(read);
            sb.append(", size: ");
            sb.append(descriptor != null ? Integer.valueOf(descriptor.getSize()) : null);
            logger.finer(sb.toString());
            if (descriptor != null && read < (size = descriptor.getSize())) {
                byte[] bArr = new byte[size - read];
                this.configDescriptorDeadBytes = bArr;
                bb.get(bArr);
            }
            if (descriptor instanceof DecoderSpecificInfo) {
                this.decoderSpecificInfo = (DecoderSpecificInfo) descriptor;
            }
            if (descriptor instanceof AudioSpecificConfig) {
                this.audioSpecificInfo = (AudioSpecificConfig) descriptor;
            }
        }
        while (begin > 2) {
            long begin2 = bb.position();
            BaseDescriptor descriptor2 = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            Logger logger2 = log;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(descriptor2);
            sb2.append(" - DecoderConfigDescr2 read: ");
            sb2.append(bb.position() - begin2);
            sb2.append(", size: ");
            sb2.append(descriptor2 != null ? Integer.valueOf(descriptor2.getSize()) : null);
            logger2.finer(sb2.toString());
            if (descriptor2 instanceof ProfileLevelIndicationDescriptor) {
                this.profileLevelIndicationDescriptors.add((ProfileLevelIndicationDescriptor) descriptor2);
            }
        }
    }

    public int serializedSize() {
        AudioSpecificConfig audioSpecificConfig = this.audioSpecificInfo;
        return (audioSpecificConfig == null ? 0 : audioSpecificConfig.serializedSize()) + 15;
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(serializedSize());
        IsoTypeWriter.writeUInt8(out, 4);
        IsoTypeWriter.writeUInt8(out, serializedSize() - 2);
        IsoTypeWriter.writeUInt8(out, this.objectTypeIndication);
        int flags = (this.streamType << 2) | (this.upStream << 1) | 1;
        IsoTypeWriter.writeUInt8(out, flags);
        IsoTypeWriter.writeUInt24(out, this.bufferSizeDB);
        IsoTypeWriter.writeUInt32(out, this.maxBitRate);
        IsoTypeWriter.writeUInt32(out, this.avgBitRate);
        AudioSpecificConfig audioSpecificConfig = this.audioSpecificInfo;
        if (audioSpecificConfig != null) {
            out.put(audioSpecificConfig.serialize().array());
        }
        return out;
    }

    public DecoderSpecificInfo getDecoderSpecificInfo() {
        return this.decoderSpecificInfo;
    }

    public AudioSpecificConfig getAudioSpecificInfo() {
        return this.audioSpecificInfo;
    }

    public void setAudioSpecificInfo(AudioSpecificConfig audioSpecificInfo) {
        this.audioSpecificInfo = audioSpecificInfo;
    }

    public List<ProfileLevelIndicationDescriptor> getProfileLevelIndicationDescriptors() {
        return this.profileLevelIndicationDescriptors;
    }

    public int getObjectTypeIndication() {
        return this.objectTypeIndication;
    }

    public void setObjectTypeIndication(int objectTypeIndication) {
        this.objectTypeIndication = objectTypeIndication;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getUpStream() {
        return this.upStream;
    }

    public void setUpStream(int upStream) {
        this.upStream = upStream;
    }

    public int getBufferSizeDB() {
        return this.bufferSizeDB;
    }

    public void setBufferSizeDB(int bufferSizeDB) {
        this.bufferSizeDB = bufferSizeDB;
    }

    public long getMaxBitRate() {
        return this.maxBitRate;
    }

    public void setMaxBitRate(long maxBitRate) {
        this.maxBitRate = maxBitRate;
    }

    public long getAvgBitRate() {
        return this.avgBitRate;
    }

    public void setAvgBitRate(long avgBitRate) {
        this.avgBitRate = avgBitRate;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecoderConfigDescriptor");
        sb.append("{objectTypeIndication=");
        sb.append(this.objectTypeIndication);
        sb.append(", streamType=");
        sb.append(this.streamType);
        sb.append(", upStream=");
        sb.append(this.upStream);
        sb.append(", bufferSizeDB=");
        sb.append(this.bufferSizeDB);
        sb.append(", maxBitRate=");
        sb.append(this.maxBitRate);
        sb.append(", avgBitRate=");
        sb.append(this.avgBitRate);
        sb.append(", decoderSpecificInfo=");
        sb.append(this.decoderSpecificInfo);
        sb.append(", audioSpecificInfo=");
        sb.append(this.audioSpecificInfo);
        sb.append(", configDescriptorDeadBytes=");
        byte[] bArr = this.configDescriptorDeadBytes;
        if (bArr == null) {
            bArr = new byte[0];
        }
        sb.append(Hex.encodeHex(bArr));
        sb.append(", profileLevelIndicationDescriptors=");
        List<ProfileLevelIndicationDescriptor> list = this.profileLevelIndicationDescriptors;
        sb.append(list == null ? "null" : Arrays.asList(list).toString());
        sb.append('}');
        return sb.toString();
    }
}
