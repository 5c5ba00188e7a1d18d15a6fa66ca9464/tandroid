package org.telegram.messenger.audioinfo.mp3;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.audioinfo.AudioInfo;

/* loaded from: classes3.dex */
public class ID3v2Info extends AudioInfo {
    static final Logger LOGGER = Logger.getLogger(ID3v2Info.class.getName());
    private byte coverPictureType;
    private final Level debugLevel;

    static class AttachedPicture {
        final String description;
        final byte[] imageData;
        final String imageType;
        final byte type;

        public AttachedPicture(byte b, String str, String str2, byte[] bArr) {
            this.type = b;
            this.description = str;
            this.imageType = str2;
            this.imageData = bArr;
        }
    }

    static class CommentOrUnsynchronizedLyrics {
        final String description;
        final String language;
        final String text;

        public CommentOrUnsynchronizedLyrics(String str, String str2, String str3) {
            this.language = str;
            this.description = str2;
            this.text = str3;
        }
    }

    public ID3v2Info(InputStream inputStream, Level level) {
        ID3v2DataInput iD3v2DataInput;
        long j;
        this.debugLevel = level;
        if (isID3v2StartPosition(inputStream)) {
            ID3v2TagHeader iD3v2TagHeader = new ID3v2TagHeader(inputStream);
            this.brand = "ID3";
            this.version = String.format("2.%d.%d", Integer.valueOf(iD3v2TagHeader.getVersion()), Integer.valueOf(iD3v2TagHeader.getRevision()));
            ID3v2TagBody tagBody = iD3v2TagHeader.tagBody(inputStream);
            while (true) {
                try {
                    if (tagBody.getRemainingLength() <= 10) {
                        break;
                    }
                    ID3v2FrameHeader iD3v2FrameHeader = new ID3v2FrameHeader(tagBody);
                    if (iD3v2FrameHeader.isPadding()) {
                        break;
                    }
                    if (iD3v2FrameHeader.getBodySize() > tagBody.getRemainingLength()) {
                        Logger logger = LOGGER;
                        if (logger.isLoggable(level)) {
                            logger.log(level, "ID3 frame claims to extend frames area");
                        }
                    } else {
                        if (!iD3v2FrameHeader.isValid() || iD3v2FrameHeader.isEncryption()) {
                            ID3v2DataInput data = tagBody.getData();
                            long bodySize = iD3v2FrameHeader.getBodySize();
                            iD3v2DataInput = data;
                            j = bodySize;
                        } else {
                            ID3v2FrameBody frameBody = tagBody.frameBody(iD3v2FrameHeader);
                            try {
                                try {
                                    parseFrame(frameBody);
                                    iD3v2DataInput = frameBody.getData();
                                } catch (ID3v2Exception e) {
                                    if (LOGGER.isLoggable(level)) {
                                        LOGGER.log(level, String.format("ID3 exception occured in frame %s: %s", iD3v2FrameHeader.getFrameId(), e.getMessage()));
                                    }
                                    iD3v2DataInput = frameBody.getData();
                                }
                                j = frameBody.getRemainingLength();
                            } catch (Throwable th) {
                                frameBody.getData().skipFully(frameBody.getRemainingLength());
                                throw th;
                            }
                        }
                        iD3v2DataInput.skipFully(j);
                    }
                } catch (ID3v2Exception e2) {
                    Logger logger2 = LOGGER;
                    if (logger2.isLoggable(level)) {
                        logger2.log(level, "ID3 exception occured: " + e2.getMessage());
                    }
                }
            }
            tagBody.getData().skipFully(tagBody.getRemainingLength());
            if (iD3v2TagHeader.getFooterSize() > 0) {
                inputStream.skip(iD3v2TagHeader.getFooterSize());
            }
        }
    }

    public static boolean isID3v2StartPosition(InputStream inputStream) {
        boolean z;
        inputStream.mark(3);
        try {
            if (inputStream.read() == 73 && inputStream.read() == 68) {
                if (inputStream.read() == 51) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            inputStream.reset();
        }
    }

    AttachedPicture parseAttachedPictureFrame(ID3v2FrameBody iD3v2FrameBody) {
        String readZeroTerminatedString;
        ID3v2Encoding readEncoding = iD3v2FrameBody.readEncoding();
        if (iD3v2FrameBody.getTagHeader().getVersion() == 2) {
            String upperCase = iD3v2FrameBody.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1).toUpperCase();
            upperCase.hashCode();
            readZeroTerminatedString = !upperCase.equals("JPG") ? !upperCase.equals("PNG") ? "image/unknown" : "image/png" : "image/jpeg";
        } else {
            readZeroTerminatedString = iD3v2FrameBody.readZeroTerminatedString(20, ID3v2Encoding.ISO_8859_1);
        }
        return new AttachedPicture(iD3v2FrameBody.getData().readByte(), iD3v2FrameBody.readZeroTerminatedString(NotificationCenter.storyQualityUpdate, readEncoding), readZeroTerminatedString, iD3v2FrameBody.getData().readFully((int) iD3v2FrameBody.getRemainingLength()));
    }

    CommentOrUnsynchronizedLyrics parseCommentOrUnsynchronizedLyricsFrame(ID3v2FrameBody iD3v2FrameBody) {
        ID3v2Encoding readEncoding = iD3v2FrameBody.readEncoding();
        return new CommentOrUnsynchronizedLyrics(iD3v2FrameBody.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1), iD3v2FrameBody.readZeroTerminatedString(NotificationCenter.storyQualityUpdate, readEncoding), iD3v2FrameBody.readFixedLengthString((int) iD3v2FrameBody.getRemainingLength(), readEncoding));
    }

    /*  JADX ERROR: NullPointerException in pass: LoopRegionVisitor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.SSAVar.use(jadx.core.dex.instructions.args.RegisterArg)" because "ssaVar" is null
        	at jadx.core.dex.nodes.InsnNode.rebindArgs(InsnNode.java:493)
        	at jadx.core.dex.nodes.InsnNode.rebindArgs(InsnNode.java:496)
        */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0488 A[Catch: all -> 0x046a, TryCatch #6 {all -> 0x046a, blocks: (B:253:0x0450, B:255:0x0465, B:258:0x047d, B:260:0x0488, B:262:0x049f, B:263:0x04b8, B:265:0x04bc, B:267:0x04b6, B:270:0x046c, B:272:0x0474), top: B:252:0x0450 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void parseFrame(org.telegram.messenger.audioinfo.mp3.ID3v2FrameBody r9) {
        /*
            Method dump skipped, instructions count: 1454
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.audioinfo.mp3.ID3v2Info.parseFrame(org.telegram.messenger.audioinfo.mp3.ID3v2FrameBody):void");
    }

    String parseTextFrame(ID3v2FrameBody iD3v2FrameBody) {
        return iD3v2FrameBody.readFixedLengthString((int) iD3v2FrameBody.getRemainingLength(), iD3v2FrameBody.readEncoding());
    }
}
