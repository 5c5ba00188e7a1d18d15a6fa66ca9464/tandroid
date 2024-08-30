package org.telegram.messenger.audioinfo.mp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class AttachedPicture {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class CommentOrUnsynchronizedLyrics {
        final String description;
        final String language;
        final String text;

        public CommentOrUnsynchronizedLyrics(String str, String str2, String str3) {
            this.language = str;
            this.description = str2;
            this.text = str3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x005d, code lost:
        r0 = org.telegram.messenger.audioinfo.mp3.ID3v2Info.LOGGER;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0063, code lost:
        if (r0.isLoggable(r13) == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0065, code lost:
        r0.log(r13, "ID3 frame claims to extend frames area");
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ID3v2Info(InputStream inputStream, Level level) {
        Logger logger;
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
                } catch (ID3v2Exception e) {
                    logger = LOGGER;
                    if (logger.isLoggable(level)) {
                    }
                }
                if (tagBody.getRemainingLength() <= 10) {
                    break;
                }
                ID3v2FrameHeader iD3v2FrameHeader = new ID3v2FrameHeader(tagBody);
                if (iD3v2FrameHeader.isPadding()) {
                    break;
                }
                if (iD3v2FrameHeader.getBodySize() > tagBody.getRemainingLength()) {
                    break;
                }
                if (!iD3v2FrameHeader.isValid() || iD3v2FrameHeader.isEncryption()) {
                    ID3v2DataInput data = tagBody.getData();
                    long bodySize = iD3v2FrameHeader.getBodySize();
                    iD3v2DataInput = data;
                    j = bodySize;
                } else {
                    ID3v2FrameBody frameBody = tagBody.frameBody(iD3v2FrameHeader);
                    try {
                        parseFrame(frameBody);
                        iD3v2DataInput = frameBody.getData();
                    } catch (ID3v2Exception e2) {
                        if (LOGGER.isLoggable(level)) {
                            LOGGER.log(level, String.format("ID3 exception occured in frame %s: %s", iD3v2FrameHeader.getFrameId(), e2.getMessage()));
                        }
                        iD3v2DataInput = frameBody.getData();
                    }
                    j = frameBody.getRemainingLength();
                }
                iD3v2DataInput.skipFully(j);
                logger = LOGGER;
                if (logger.isLoggable(level)) {
                    logger.log(level, "ID3 exception occured: " + e.getMessage());
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

    /* JADX WARN: Removed duplicated region for block: B:253:0x0488 A[Catch: all -> 0x046a, TryCatch #6 {all -> 0x046a, blocks: (B:241:0x0450, B:243:0x0465, B:251:0x047d, B:253:0x0488, B:255:0x049f, B:257:0x04b8, B:259:0x04bc, B:256:0x04b6, B:248:0x046c, B:250:0x0474), top: B:284:0x0450 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void parseFrame(ID3v2FrameBody iD3v2FrameBody) {
        String str;
        byte[] bArr;
        BitmapFactory.Options options;
        int i;
        Bitmap decodeByteArray;
        Bitmap bitmap;
        byte b;
        ID3v1Genre genre;
        int i2;
        String parseTextFrame;
        Logger logger;
        Level level;
        StringBuilder sb;
        String str2;
        Logger logger2 = LOGGER;
        if (logger2.isLoggable(this.debugLevel)) {
            logger2.log(this.debugLevel, "Parsing frame: " + iD3v2FrameBody.getFrameHeader().getFrameId());
        }
        String frameId = iD3v2FrameBody.getFrameHeader().getFrameId();
        frameId.hashCode();
        char c = 65535;
        switch (frameId.hashCode()) {
            case 66913:
                if (frameId.equals("COM")) {
                    c = 0;
                    break;
                }
                break;
            case 79210:
                if (frameId.equals("PIC")) {
                    c = 1;
                    break;
                }
                break;
            case 82815:
                if (frameId.equals("TAL")) {
                    c = 2;
                    break;
                }
                break;
            case 82878:
                if (frameId.equals("TCM")) {
                    c = 3;
                    break;
                }
                break;
            case 82880:
                if (frameId.equals("TCO")) {
                    c = 4;
                    break;
                }
                break;
            case 82881:
                if (frameId.equals("TCP")) {
                    c = 5;
                    break;
                }
                break;
            case 82883:
                if (frameId.equals("TCR")) {
                    c = 6;
                    break;
                }
                break;
            case 83149:
                if (frameId.equals("TLE")) {
                    c = 7;
                    break;
                }
                break;
            case 83253:
                if (frameId.equals("TP1")) {
                    c = '\b';
                    break;
                }
                break;
            case 83254:
                if (frameId.equals("TP2")) {
                    c = '\t';
                    break;
                }
                break;
            case 83269:
                if (frameId.equals("TPA")) {
                    c = '\n';
                    break;
                }
                break;
            case 83341:
                if (frameId.equals("TRK")) {
                    c = 11;
                    break;
                }
                break;
            case 83377:
                if (frameId.equals("TT1")) {
                    c = '\f';
                    break;
                }
                break;
            case 83378:
                if (frameId.equals("TT2")) {
                    c = '\r';
                    break;
                }
                break;
            case 83552:
                if (frameId.equals("TYE")) {
                    c = 14;
                    break;
                }
                break;
            case 84125:
                if (frameId.equals("ULT")) {
                    c = 15;
                    break;
                }
                break;
            case 2015625:
                if (frameId.equals("APIC")) {
                    c = 16;
                    break;
                }
                break;
            case 2074380:
                if (frameId.equals("COMM")) {
                    c = 17;
                    break;
                }
                break;
            case 2567331:
                if (frameId.equals("TALB")) {
                    c = 18;
                    break;
                }
                break;
            case 2569298:
                if (frameId.equals("TCMP")) {
                    c = 19;
                    break;
                }
                break;
            case 2569357:
                if (frameId.equals("TCOM")) {
                    c = 20;
                    break;
                }
                break;
            case 2569358:
                if (frameId.equals("TCON")) {
                    c = 21;
                    break;
                }
                break;
            case 2569360:
                if (frameId.equals("TCOP")) {
                    c = 22;
                    break;
                }
                break;
            case 2570401:
                if (frameId.equals("TDRC")) {
                    c = 23;
                    break;
                }
                break;
            case 2575250:
                if (frameId.equals("TIT1")) {
                    c = 24;
                    break;
                }
                break;
            case 2575251:
                if (frameId.equals("TIT2")) {
                    c = 25;
                    break;
                }
                break;
            case 2577697:
                if (frameId.equals("TLEN")) {
                    c = 26;
                    break;
                }
                break;
            case 2581512:
                if (frameId.equals("TPE1")) {
                    c = 27;
                    break;
                }
                break;
            case 2581513:
                if (frameId.equals("TPE2")) {
                    c = 28;
                    break;
                }
                break;
            case 2581856:
                if (frameId.equals("TPOS")) {
                    c = 29;
                    break;
                }
                break;
            case 2583398:
                if (frameId.equals("TRCK")) {
                    c = 30;
                    break;
                }
                break;
            case 2590194:
                if (frameId.equals("TYER")) {
                    c = 31;
                    break;
                }
                break;
            case 2614438:
                if (frameId.equals("USLT")) {
                    c = ' ';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 17:
                CommentOrUnsynchronizedLyrics parseCommentOrUnsynchronizedLyricsFrame = parseCommentOrUnsynchronizedLyricsFrame(iD3v2FrameBody);
                if (this.comment == null || (str = parseCommentOrUnsynchronizedLyricsFrame.description) == null || "".equals(str)) {
                    this.comment = parseCommentOrUnsynchronizedLyricsFrame.text;
                    return;
                }
                return;
            case 1:
            case 16:
                if (this.cover == null || this.coverPictureType != 3) {
                    AttachedPicture parseAttachedPictureFrame = parseAttachedPictureFrame(iD3v2FrameBody);
                    if (this.cover == null || (b = parseAttachedPictureFrame.type) == 3 || b == 0) {
                        try {
                            bArr = parseAttachedPictureFrame.imageData;
                            options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            options.inSampleSize = 1;
                            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                        if (options.outWidth <= 800) {
                            if (options.outHeight > 800) {
                            }
                            options.inJustDecodeBounds = false;
                            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                            this.cover = decodeByteArray;
                            if (decodeByteArray != null) {
                                float max = Math.max(decodeByteArray.getWidth(), this.cover.getHeight()) / 120.0f;
                                Bitmap createScaledBitmap = max > 0.0f ? Bitmap.createScaledBitmap(this.cover, (int) (bitmap.getWidth() / max), (int) (this.cover.getHeight() / max), true) : this.cover;
                                this.smallCover = createScaledBitmap;
                                if (createScaledBitmap == null) {
                                    this.smallCover = this.cover;
                                }
                            }
                            this.coverPictureType = parseAttachedPictureFrame.type;
                            return;
                        }
                        for (int max2 = Math.max(i, options.outHeight); max2 > 800; max2 /= 2) {
                            options.inSampleSize *= 2;
                        }
                        options.inJustDecodeBounds = false;
                        decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                        this.cover = decodeByteArray;
                        if (decodeByteArray != null) {
                        }
                        this.coverPictureType = parseAttachedPictureFrame.type;
                        return;
                    }
                    return;
                }
                return;
            case 2:
            case 18:
                this.album = parseTextFrame(iD3v2FrameBody);
                return;
            case 3:
            case 20:
                this.composer = parseTextFrame(iD3v2FrameBody);
                return;
            case 4:
            case 21:
                String parseTextFrame2 = parseTextFrame(iD3v2FrameBody);
                if (parseTextFrame2.length() > 0) {
                    this.genre = parseTextFrame2;
                    try {
                        if (parseTextFrame2.charAt(0) == '(') {
                            int indexOf = parseTextFrame2.indexOf(41);
                            if (indexOf > 1) {
                                genre = ID3v1Genre.getGenre(Integer.parseInt(parseTextFrame2.substring(1, indexOf)));
                                if (genre == null && parseTextFrame2.length() > (i2 = indexOf + 1)) {
                                    this.genre = parseTextFrame2.substring(i2);
                                }
                            } else {
                                genre = null;
                            }
                        } else {
                            genre = ID3v1Genre.getGenre(Integer.parseInt(parseTextFrame2));
                        }
                        if (genre != null) {
                            this.genre = genre.getDescription();
                            return;
                        }
                        return;
                    } catch (NumberFormatException unused) {
                        return;
                    }
                }
                return;
            case 5:
            case 19:
                this.compilation = "1".equals(parseTextFrame(iD3v2FrameBody));
                return;
            case 6:
            case 22:
                this.copyright = parseTextFrame(iD3v2FrameBody);
                return;
            case 7:
            case 26:
                parseTextFrame = parseTextFrame(iD3v2FrameBody);
                try {
                    this.duration = Long.valueOf(parseTextFrame).longValue();
                    return;
                } catch (NumberFormatException unused2) {
                    logger = LOGGER;
                    if (logger.isLoggable(this.debugLevel)) {
                        level = this.debugLevel;
                        sb = new StringBuilder();
                        str2 = "Could not parse track duration: ";
                        break;
                    } else {
                        return;
                    }
                }
            case '\b':
            case 27:
                this.artist = parseTextFrame(iD3v2FrameBody);
                return;
            case '\t':
            case 28:
                this.albumArtist = parseTextFrame(iD3v2FrameBody);
                return;
            case '\n':
            case 29:
                parseTextFrame = parseTextFrame(iD3v2FrameBody);
                if (parseTextFrame.length() > 0) {
                    int indexOf2 = parseTextFrame.indexOf(47);
                    str2 = "Could not parse disc number: ";
                    if (indexOf2 < 0) {
                        try {
                            this.disc = Short.valueOf(parseTextFrame).shortValue();
                            return;
                        } catch (NumberFormatException unused3) {
                            logger = LOGGER;
                            if (logger.isLoggable(this.debugLevel)) {
                                level = this.debugLevel;
                                sb = new StringBuilder();
                                break;
                            } else {
                                return;
                            }
                        }
                    } else {
                        try {
                            this.disc = Short.valueOf(parseTextFrame.substring(0, indexOf2)).shortValue();
                        } catch (NumberFormatException unused4) {
                            Logger logger3 = LOGGER;
                            if (logger3.isLoggable(this.debugLevel)) {
                                logger3.log(this.debugLevel, "Could not parse disc number: " + parseTextFrame);
                            }
                        }
                        try {
                            this.discs = Short.valueOf(parseTextFrame.substring(indexOf2 + 1)).shortValue();
                            return;
                        } catch (NumberFormatException unused5) {
                            logger = LOGGER;
                            if (logger.isLoggable(this.debugLevel)) {
                                level = this.debugLevel;
                                sb = new StringBuilder();
                                str2 = "Could not parse number of discs: ";
                                break;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            case 11:
            case 30:
                parseTextFrame = parseTextFrame(iD3v2FrameBody);
                if (parseTextFrame.length() > 0) {
                    int indexOf3 = parseTextFrame.indexOf(47);
                    str2 = "Could not parse track number: ";
                    if (indexOf3 < 0) {
                        try {
                            this.track = Short.valueOf(parseTextFrame).shortValue();
                            return;
                        } catch (NumberFormatException unused6) {
                            logger = LOGGER;
                            if (logger.isLoggable(this.debugLevel)) {
                                level = this.debugLevel;
                                sb = new StringBuilder();
                                break;
                            } else {
                                return;
                            }
                        }
                    } else {
                        try {
                            this.track = Short.valueOf(parseTextFrame.substring(0, indexOf3)).shortValue();
                        } catch (NumberFormatException unused7) {
                            Logger logger4 = LOGGER;
                            if (logger4.isLoggable(this.debugLevel)) {
                                logger4.log(this.debugLevel, "Could not parse track number: " + parseTextFrame);
                            }
                        }
                        try {
                            this.tracks = Short.valueOf(parseTextFrame.substring(indexOf3 + 1)).shortValue();
                            return;
                        } catch (NumberFormatException unused8) {
                            logger = LOGGER;
                            if (logger.isLoggable(this.debugLevel)) {
                                level = this.debugLevel;
                                sb = new StringBuilder();
                                str2 = "Could not parse number of tracks: ";
                                break;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            case '\f':
            case 24:
                this.grouping = parseTextFrame(iD3v2FrameBody);
                return;
            case '\r':
            case 25:
                this.title = parseTextFrame(iD3v2FrameBody);
                return;
            case 14:
            case 31:
                parseTextFrame = parseTextFrame(iD3v2FrameBody);
                if (parseTextFrame.length() > 0) {
                    try {
                        this.year = Short.valueOf(parseTextFrame).shortValue();
                        return;
                    } catch (NumberFormatException unused9) {
                        logger = LOGGER;
                        if (logger.isLoggable(this.debugLevel)) {
                            level = this.debugLevel;
                            sb = new StringBuilder();
                            str2 = "Could not parse year: ";
                            break;
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            case 15:
            case ' ':
                if (this.lyrics == null) {
                    this.lyrics = parseCommentOrUnsynchronizedLyricsFrame(iD3v2FrameBody).text;
                    return;
                }
                return;
            case 23:
                parseTextFrame = parseTextFrame(iD3v2FrameBody);
                if (parseTextFrame.length() >= 4) {
                    try {
                        this.year = Short.valueOf(parseTextFrame.substring(0, 4)).shortValue();
                        return;
                    } catch (NumberFormatException unused10) {
                        logger = LOGGER;
                        if (logger.isLoggable(this.debugLevel)) {
                            level = this.debugLevel;
                            sb = new StringBuilder();
                            str2 = "Could not parse year from: ";
                            break;
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            default:
                return;
        }
        sb.append(str2);
        sb.append(parseTextFrame);
        logger.log(level, sb.toString());
    }

    String parseTextFrame(ID3v2FrameBody iD3v2FrameBody) {
        return iD3v2FrameBody.readFixedLengthString((int) iD3v2FrameBody.getRemainingLength(), iD3v2FrameBody.readEncoding());
    }
}
