package org.telegram.messenger.audioinfo.m4a;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.audioinfo.mp3.ID3v1Genre;
/* loaded from: classes3.dex */
public class M4AInfo extends AudioInfo {
    static final Logger LOGGER = Logger.getLogger(M4AInfo.class.getName());
    private final Level debugLevel;
    private byte rating;
    private BigDecimal speed;
    private short tempo;
    private BigDecimal volume;

    public M4AInfo(InputStream inputStream) {
        this(inputStream, Level.FINEST);
    }

    public M4AInfo(InputStream inputStream, Level level) {
        this.debugLevel = level;
        MP4Input mP4Input = new MP4Input(inputStream);
        Logger logger = LOGGER;
        if (logger.isLoggable(level)) {
            logger.log(level, mP4Input.toString());
        }
        ftyp(mP4Input.nextChild("ftyp"));
        moov(mP4Input.nextChildUpTo("moov"));
    }

    /* JADX WARN: Removed duplicated region for block: B:144:0x0273 A[Catch: Exception -> 0x0255, TryCatch #1 {Exception -> 0x0255, blocks: (B:132:0x0239, B:134:0x0250, B:142:0x0268, B:144:0x0273, B:146:0x028a, B:148:0x02a3, B:150:0x02a7, B:147:0x02a1, B:139:0x0257, B:141:0x025f), top: B:158:0x0239 }] */
    /* JADX WARN: Removed duplicated region for block: B:181:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void data(MP4Atom mP4Atom) {
        int i;
        Bitmap decodeByteArray;
        Bitmap bitmap;
        String description;
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        mP4Atom.skip(4);
        mP4Atom.skip(4);
        String type = mP4Atom.getParent().getType();
        type.hashCode();
        char c = 65535;
        switch (type.hashCode()) {
            case 2954818:
                if (type.equals("aART")) {
                    c = 0;
                    break;
                }
                break;
            case 3059752:
                if (type.equals("covr")) {
                    c = 1;
                    break;
                }
                break;
            case 3060304:
                if (type.equals("cpil")) {
                    c = 2;
                    break;
                }
                break;
            case 3060591:
                if (type.equals("cprt")) {
                    c = 3;
                    break;
                }
                break;
            case 3083677:
                if (type.equals("disk")) {
                    c = 4;
                    break;
                }
                break;
            case 3177818:
                if (type.equals("gnre")) {
                    c = 5;
                    break;
                }
                break;
            case 3511163:
                if (type.equals("rtng")) {
                    c = 6;
                    break;
                }
                break;
            case 3564088:
                if (type.equals("tmpo")) {
                    c = 7;
                    break;
                }
                break;
            case 3568737:
                if (type.equals("trkn")) {
                    c = '\b';
                    break;
                }
                break;
            case 5099770:
                if (type.equals("©ART")) {
                    c = '\t';
                    break;
                }
                break;
            case 5131342:
                if (type.equals("©alb")) {
                    c = '\n';
                    break;
                }
                break;
            case 5133313:
                if (type.equals("©cmt")) {
                    c = 11;
                    break;
                }
                break;
            case 5133368:
                if (type.equals("©com")) {
                    c = '\f';
                    break;
                }
                break;
            case 5133411:
                if (type.equals("©cpy")) {
                    c = '\r';
                    break;
                }
                break;
            case 5133907:
                if (type.equals("©day")) {
                    c = 14;
                    break;
                }
                break;
            case 5136903:
                if (type.equals("©gen")) {
                    c = 15;
                    break;
                }
                break;
            case 5137308:
                if (type.equals("©grp")) {
                    c = 16;
                    break;
                }
                break;
            case 5142332:
                if (type.equals("©lyr")) {
                    c = 17;
                    break;
                }
                break;
            case 5143505:
                if (type.equals("©nam")) {
                    c = 18;
                    break;
                }
                break;
            case 5152688:
                if (type.equals("©wrt")) {
                    c = 19;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.albumArtist = mP4Atom.readString("UTF-8");
                return;
            case 1:
                try {
                    byte[] readBytes = mP4Atom.readBytes();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inSampleSize = 1;
                    BitmapFactory.decodeByteArray(readBytes, 0, readBytes.length, options);
                    if (options.outWidth <= 800) {
                        if (options.outHeight > 800) {
                        }
                        options.inJustDecodeBounds = false;
                        decodeByteArray = BitmapFactory.decodeByteArray(readBytes, 0, readBytes.length, options);
                        this.cover = decodeByteArray;
                        if (decodeByteArray == null) {
                            float max = Math.max(decodeByteArray.getWidth(), this.cover.getHeight()) / 120.0f;
                            Bitmap createScaledBitmap = max > 0.0f ? Bitmap.createScaledBitmap(this.cover, (int) (bitmap.getWidth() / max), (int) (this.cover.getHeight() / max), true) : this.cover;
                            this.smallCover = createScaledBitmap;
                            if (createScaledBitmap == null) {
                                this.smallCover = this.cover;
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    for (int max2 = Math.max(i, options.outHeight); max2 > 800; max2 /= 2) {
                        options.inSampleSize *= 2;
                    }
                    options.inJustDecodeBounds = false;
                    decodeByteArray = BitmapFactory.decodeByteArray(readBytes, 0, readBytes.length, options);
                    this.cover = decodeByteArray;
                    if (decodeByteArray == null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case 2:
                this.compilation = mP4Atom.readBoolean();
                return;
            case 3:
            case '\r':
                String str = this.copyright;
                if (str == null || str.trim().length() == 0) {
                    this.copyright = mP4Atom.readString("UTF-8");
                    return;
                }
                return;
            case 4:
                mP4Atom.skip(2);
                this.disc = mP4Atom.readShort();
                this.discs = mP4Atom.readShort();
                return;
            case 5:
                String str2 = this.genre;
                if (str2 == null || str2.trim().length() == 0) {
                    if (mP4Atom.getRemaining() == 2) {
                        ID3v1Genre genre = ID3v1Genre.getGenre(mP4Atom.readShort() - 1);
                        if (genre != null) {
                            description = genre.getDescription();
                            break;
                        } else {
                            return;
                        }
                    }
                    description = mP4Atom.readString("UTF-8");
                    break;
                } else {
                    return;
                }
            case 6:
                this.rating = mP4Atom.readByte();
                return;
            case 7:
                this.tempo = mP4Atom.readShort();
                return;
            case '\b':
                mP4Atom.skip(2);
                this.track = mP4Atom.readShort();
                this.tracks = mP4Atom.readShort();
                return;
            case '\t':
                this.artist = mP4Atom.readString("UTF-8");
                return;
            case '\n':
                this.album = mP4Atom.readString("UTF-8");
                return;
            case 11:
                this.comment = mP4Atom.readString("UTF-8");
                return;
            case '\f':
            case 19:
                String str3 = this.composer;
                if (str3 == null || str3.trim().length() == 0) {
                    this.composer = mP4Atom.readString("UTF-8");
                    return;
                }
                return;
            case 14:
                String trim = mP4Atom.readString("UTF-8").trim();
                if (trim.length() >= 4) {
                    try {
                        this.year = Short.valueOf(trim.substring(0, 4)).shortValue();
                        return;
                    } catch (NumberFormatException unused) {
                        return;
                    }
                }
                return;
            case 15:
                String str4 = this.genre;
                if (str4 != null && str4.trim().length() != 0) {
                    return;
                }
                description = mP4Atom.readString("UTF-8");
                break;
            case 16:
                this.grouping = mP4Atom.readString("UTF-8");
                return;
            case 17:
                this.lyrics = mP4Atom.readString("UTF-8");
                return;
            case 18:
                this.title = mP4Atom.readString("UTF-8");
                return;
            default:
                return;
        }
        this.genre = description;
    }

    void ftyp(MP4Atom mP4Atom) {
        StringBuilder sb;
        String str;
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        String trim = mP4Atom.readString(4, "ISO8859_1").trim();
        this.brand = trim;
        if (!trim.matches("M4V|MP4|mp42|isom")) {
            if (!this.brand.matches("M4A|M4P")) {
                sb = new StringBuilder();
                sb.append(mP4Atom.getPath());
                sb.append(": brand=");
                sb.append(this.brand);
                str = " (expected M4A or M4P)";
            }
            this.version = String.valueOf(mP4Atom.readInt());
        }
        sb = new StringBuilder();
        sb.append(mP4Atom.getPath());
        sb.append(": brand=");
        sb.append(this.brand);
        str = " (experimental)";
        sb.append(str);
        logger.warning(sb.toString());
        this.version = String.valueOf(mP4Atom.readInt());
    }

    void ilst(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        while (mP4Atom.hasMoreChildren()) {
            MP4Atom nextChild = mP4Atom.nextChild();
            Logger logger2 = LOGGER;
            if (logger2.isLoggable(this.debugLevel)) {
                logger2.log(this.debugLevel, nextChild.toString());
            }
            if (nextChild.getRemaining() != 0) {
                data(nextChild.nextChildUpTo("data"));
            } else if (logger2.isLoggable(this.debugLevel)) {
                Level level = this.debugLevel;
                logger2.log(level, nextChild.getPath() + ": contains no value");
            }
        }
    }

    void mdhd(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        byte readByte = mP4Atom.readByte();
        mP4Atom.skip(3);
        mP4Atom.skip(readByte == 1 ? 16 : 8);
        int readInt = mP4Atom.readInt();
        long readLong = readByte == 1 ? mP4Atom.readLong() : mP4Atom.readInt();
        if (this.duration == 0) {
            this.duration = (readLong * 1000) / readInt;
        } else if (logger.isLoggable(this.debugLevel)) {
            long j = (readLong * 1000) / readInt;
            if (Math.abs(this.duration - j) > 2) {
                Level level = this.debugLevel;
                logger.log(level, "mdhd: duration " + this.duration + " -> " + j);
            }
        }
    }

    void mdia(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        mdhd(mP4Atom.nextChild("mdhd"));
    }

    void meta(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        mP4Atom.skip(4);
        while (mP4Atom.hasMoreChildren()) {
            MP4Atom nextChild = mP4Atom.nextChild();
            if ("ilst".equals(nextChild.getType())) {
                ilst(nextChild);
                return;
            }
        }
    }

    void moov(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        while (mP4Atom.hasMoreChildren()) {
            MP4Atom nextChild = mP4Atom.nextChild();
            String type = nextChild.getType();
            type.hashCode();
            char c = 65535;
            switch (type.hashCode()) {
                case 3363941:
                    if (type.equals("mvhd")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3568424:
                    if (type.equals("trak")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3585340:
                    if (type.equals("udta")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    mvhd(nextChild);
                    break;
                case 1:
                    trak(nextChild);
                    break;
                case 2:
                    udta(nextChild);
                    break;
            }
        }
    }

    void mvhd(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        byte readByte = mP4Atom.readByte();
        mP4Atom.skip(3);
        mP4Atom.skip(readByte == 1 ? 16 : 8);
        int readInt = mP4Atom.readInt();
        long readLong = readByte == 1 ? mP4Atom.readLong() : mP4Atom.readInt();
        if (this.duration == 0) {
            this.duration = (readLong * 1000) / readInt;
        } else if (logger.isLoggable(this.debugLevel)) {
            long j = (readLong * 1000) / readInt;
            if (Math.abs(this.duration - j) > 2) {
                Level level = this.debugLevel;
                logger.log(level, "mvhd: duration " + this.duration + " -> " + j);
            }
        }
        this.speed = mP4Atom.readIntegerFixedPoint();
        this.volume = mP4Atom.readShortFixedPoint();
    }

    void trak(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        mdia(mP4Atom.nextChildUpTo("mdia"));
    }

    void udta(MP4Atom mP4Atom) {
        Logger logger = LOGGER;
        if (logger.isLoggable(this.debugLevel)) {
            logger.log(this.debugLevel, mP4Atom.toString());
        }
        while (mP4Atom.hasMoreChildren()) {
            MP4Atom nextChild = mP4Atom.nextChild();
            if ("meta".equals(nextChild.getType())) {
                meta(nextChild);
                return;
            }
        }
    }
}
