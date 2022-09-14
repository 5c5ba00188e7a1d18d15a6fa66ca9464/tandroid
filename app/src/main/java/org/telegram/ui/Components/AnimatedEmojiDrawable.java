package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLiteException;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmojiDefaultStatuses;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_getCustomEmojiDocuments;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
/* loaded from: classes3.dex */
public class AnimatedEmojiDrawable extends Drawable {
    private static HashMap<Integer, EmojiDocumentFetcher> fetchers;
    private static HashMap<Integer, HashMap<Long, AnimatedEmojiDrawable>> globalEmojiCache;
    private boolean attached;
    private int cacheType;
    private ColorFilter colorFilterToSet;
    private TLRPC$Document document;
    private long documentId;
    private ArrayList<AnimatedEmojiSpan.InvalidateHolder> holders;
    private ImageReceiver imageReceiver;
    public int sizedp;
    private ArrayList<View> views;
    private float alpha = 1.0f;
    private Boolean canOverrideColorCached = null;

    /* loaded from: classes3.dex */
    public interface ReceivedDocument {
        void run(TLRPC$Document tLRPC$Document);
    }

    private void drawPlaceholder(Canvas canvas, float f, float f2, float f3) {
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public static AnimatedEmojiDrawable make(int i, int i2, long j) {
        if (globalEmojiCache == null) {
            globalEmojiCache = new HashMap<>();
        }
        int hashCode = Arrays.hashCode(new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        HashMap<Long, AnimatedEmojiDrawable> hashMap = globalEmojiCache.get(Integer.valueOf(hashCode));
        if (hashMap == null) {
            HashMap<Integer, HashMap<Long, AnimatedEmojiDrawable>> hashMap2 = globalEmojiCache;
            Integer valueOf = Integer.valueOf(hashCode);
            HashMap<Long, AnimatedEmojiDrawable> hashMap3 = new HashMap<>();
            hashMap2.put(valueOf, hashMap3);
            hashMap = hashMap3;
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = hashMap.get(Long.valueOf(j));
        if (animatedEmojiDrawable == null) {
            Long valueOf2 = Long.valueOf(j);
            AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(i2, i, j);
            hashMap.put(valueOf2, animatedEmojiDrawable2);
            return animatedEmojiDrawable2;
        }
        return animatedEmojiDrawable;
    }

    public static AnimatedEmojiDrawable make(int i, int i2, TLRPC$Document tLRPC$Document) {
        if (globalEmojiCache == null) {
            globalEmojiCache = new HashMap<>();
        }
        int hashCode = Arrays.hashCode(new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        HashMap<Long, AnimatedEmojiDrawable> hashMap = globalEmojiCache.get(Integer.valueOf(hashCode));
        if (hashMap == null) {
            HashMap<Integer, HashMap<Long, AnimatedEmojiDrawable>> hashMap2 = globalEmojiCache;
            Integer valueOf = Integer.valueOf(hashCode);
            HashMap<Long, AnimatedEmojiDrawable> hashMap3 = new HashMap<>();
            hashMap2.put(valueOf, hashMap3);
            hashMap = hashMap3;
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = hashMap.get(Long.valueOf(tLRPC$Document.id));
        if (animatedEmojiDrawable == null) {
            Long valueOf2 = Long.valueOf(tLRPC$Document.id);
            AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(i2, i, tLRPC$Document);
            hashMap.put(valueOf2, animatedEmojiDrawable2);
            return animatedEmojiDrawable2;
        }
        return animatedEmojiDrawable;
    }

    public static int getCacheTypeForEnterView() {
        return SharedConfig.getDevicePerformanceClass() == 0 ? 0 : 2;
    }

    public void setTime(long j) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            if (this.cacheType == 8) {
                j = 0;
            }
            imageReceiver.setCurrentTime(j);
        }
    }

    public void update(long j) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            if (this.cacheType == 8) {
                j = 0;
            }
            if (imageReceiver.getLottieAnimation() != null) {
                this.imageReceiver.getLottieAnimation().updateCurrentFrame(j, true);
            }
            if (this.imageReceiver.getAnimation() == null) {
                return;
            }
            this.imageReceiver.getAnimation().updateCurrentFrame(j, true);
        }
    }

    public static EmojiDocumentFetcher getDocumentFetcher(int i) {
        if (fetchers == null) {
            fetchers = new HashMap<>();
        }
        EmojiDocumentFetcher emojiDocumentFetcher = fetchers.get(Integer.valueOf(i));
        if (emojiDocumentFetcher == null) {
            HashMap<Integer, EmojiDocumentFetcher> hashMap = fetchers;
            Integer valueOf = Integer.valueOf(i);
            EmojiDocumentFetcher emojiDocumentFetcher2 = new EmojiDocumentFetcher(i);
            hashMap.put(valueOf, emojiDocumentFetcher2);
            return emojiDocumentFetcher2;
        }
        return emojiDocumentFetcher;
    }

    /* loaded from: classes3.dex */
    public static class EmojiDocumentFetcher {
        private final int currentAccount;
        private HashMap<Long, TLRPC$Document> emojiDocumentsCache;
        private Runnable fetchRunnable;
        private HashMap<Long, ArrayList<ReceivedDocument>> loadingDocuments;
        private HashSet<Long> toFetchDocuments;

        public EmojiDocumentFetcher(int i) {
            this.currentAccount = i;
        }

        public void fetchDocument(long j, ReceivedDocument receivedDocument) {
            TLRPC$Document tLRPC$Document;
            checkThread();
            HashMap<Long, TLRPC$Document> hashMap = this.emojiDocumentsCache;
            if (hashMap != null && (tLRPC$Document = hashMap.get(Long.valueOf(j))) != null) {
                receivedDocument.run(tLRPC$Document);
                return;
            }
            if (receivedDocument != null) {
                if (this.loadingDocuments == null) {
                    this.loadingDocuments = new HashMap<>();
                }
                ArrayList<ReceivedDocument> arrayList = this.loadingDocuments.get(Long.valueOf(j));
                if (arrayList != null) {
                    arrayList.add(receivedDocument);
                    return;
                }
                ArrayList<ReceivedDocument> arrayList2 = new ArrayList<>(1);
                arrayList2.add(receivedDocument);
                this.loadingDocuments.put(Long.valueOf(j), arrayList2);
            }
            if (this.toFetchDocuments == null) {
                this.toFetchDocuments = new HashSet<>();
            }
            this.toFetchDocuments.add(Long.valueOf(j));
            if (this.fetchRunnable != null) {
                return;
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$fetchDocument$0();
                }
            };
            this.fetchRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fetchDocument$0() {
            ArrayList<Long> arrayList = new ArrayList<>(this.toFetchDocuments);
            this.toFetchDocuments.clear();
            loadFromDatabase(arrayList);
            this.fetchRunnable = null;
        }

        private void checkThread() {
            if (!BuildVars.DEBUG_VERSION || Thread.currentThread() == Looper.getMainLooper().getThread()) {
                return;
            }
            throw new IllegalStateException("Wrong thread");
        }

        private void loadFromDatabase(final ArrayList<Long> arrayList) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromDatabase$2(arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromDatabase$2(ArrayList arrayList) {
            SQLiteDatabase database = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            try {
                SQLiteCursor queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT data FROM animated_emoji WHERE document_id IN (%s)", TextUtils.join(",", arrayList)), new Object[0]);
                final ArrayList arrayList2 = new ArrayList();
                final HashSet hashSet = new HashSet(arrayList);
                while (queryFinalized.next()) {
                    NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                    try {
                        TLRPC$Document TLdeserialize = TLRPC$Document.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(true), true);
                        if (TLdeserialize != null && TLdeserialize.id != 0) {
                            arrayList2.add(TLdeserialize);
                            hashSet.remove(Long.valueOf(TLdeserialize.id));
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (byteBufferValue != null) {
                        byteBufferValue.reuse();
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromDatabase$1(arrayList2, hashSet);
                    }
                });
                queryFinalized.dispose();
            } catch (SQLiteException e2) {
                FileLog.e(e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromDatabase$1(ArrayList arrayList, HashSet hashSet) {
            processDocuments(arrayList);
            if (!hashSet.isEmpty()) {
                loadFromServer(new ArrayList<>(hashSet));
            }
        }

        private void loadFromServer(final ArrayList<Long> arrayList) {
            TLRPC$TL_messages_getCustomEmojiDocuments tLRPC$TL_messages_getCustomEmojiDocuments = new TLRPC$TL_messages_getCustomEmojiDocuments();
            tLRPC$TL_messages_getCustomEmojiDocuments.document_id = arrayList;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getCustomEmojiDocuments, new RequestDelegate() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda5
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromServer$4(arrayList, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromServer$4(final ArrayList arrayList, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromServer$3(arrayList, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromServer$3(ArrayList arrayList, TLObject tLObject) {
            HashSet hashSet = new HashSet(arrayList);
            if (tLObject instanceof TLRPC$Vector) {
                ArrayList<Object> arrayList2 = ((TLRPC$Vector) tLObject).objects;
                putToStorage(arrayList2);
                processDocuments(arrayList2);
                for (int i = 0; i < arrayList2.size(); i++) {
                    if (arrayList2.get(i) instanceof TLRPC$Document) {
                        hashSet.remove(Long.valueOf(((TLRPC$Document) arrayList2.get(i)).id));
                    }
                }
                if (hashSet.isEmpty()) {
                    return;
                }
                loadFromServer(new ArrayList<>(hashSet));
            }
        }

        private void putToStorage(final ArrayList<Object> arrayList) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$putToStorage$5(arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:16:0x004d A[Catch: SQLiteException -> 0x0057, TryCatch #2 {SQLiteException -> 0x0057, blocks: (B:3:0x000a, B:4:0x0011, B:6:0x0017, B:8:0x001f, B:16:0x004d, B:23:0x0047, B:18:0x0050, B:27:0x0053), top: B:2:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0050 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$putToStorage$5(ArrayList arrayList) {
            NativeByteBuffer nativeByteBuffer;
            try {
                SQLitePreparedStatement executeFast = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO animated_emoji VALUES(?, ?)");
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i) instanceof TLRPC$Document) {
                        TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i);
                        NativeByteBuffer nativeByteBuffer2 = null;
                        try {
                            nativeByteBuffer = new NativeByteBuffer(tLRPC$Document.getObjectSize());
                        } catch (Exception e) {
                            e = e;
                        }
                        try {
                            tLRPC$Document.serializeToStream(nativeByteBuffer);
                            executeFast.requery();
                            executeFast.bindLong(1, tLRPC$Document.id);
                            executeFast.bindByteBuffer(2, nativeByteBuffer);
                            executeFast.step();
                        } catch (Exception e2) {
                            e = e2;
                            nativeByteBuffer2 = nativeByteBuffer;
                            e.printStackTrace();
                            nativeByteBuffer = nativeByteBuffer2;
                            if (nativeByteBuffer == null) {
                            }
                        }
                        if (nativeByteBuffer == null) {
                            nativeByteBuffer.reuse();
                        }
                    }
                }
                executeFast.dispose();
            } catch (SQLiteException e3) {
                FileLog.e(e3);
            }
        }

        public void processDocuments(ArrayList<?> arrayList) {
            ArrayList<ReceivedDocument> remove;
            checkThread();
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) instanceof TLRPC$Document) {
                    TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i);
                    if (this.emojiDocumentsCache == null) {
                        this.emojiDocumentsCache = new HashMap<>();
                    }
                    this.emojiDocumentsCache.put(Long.valueOf(tLRPC$Document.id), tLRPC$Document);
                    HashMap<Long, ArrayList<ReceivedDocument>> hashMap = this.loadingDocuments;
                    if (hashMap != null && (remove = hashMap.remove(Long.valueOf(tLRPC$Document.id))) != null) {
                        for (int i2 = 0; i2 < remove.size(); i2++) {
                            remove.get(i2).run(tLRPC$Document);
                        }
                        remove.clear();
                    }
                }
            }
        }
    }

    public static TLRPC$Document findDocument(int i, long j) {
        EmojiDocumentFetcher documentFetcher = getDocumentFetcher(i);
        if (documentFetcher == null || documentFetcher.emojiDocumentsCache == null) {
            return null;
        }
        return (TLRPC$Document) documentFetcher.emojiDocumentsCache.get(Long.valueOf(j));
    }

    public AnimatedEmojiDrawable(int i, int i2, long j) {
        new AnimatedFloat(1.0f, new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedEmojiDrawable.this.invalidate();
            }
        }, 0L, 150L, new LinearInterpolator());
        this.cacheType = i;
        updateSize();
        this.documentId = j;
        getDocumentFetcher(i2).fetchDocument(j, new ReceivedDocument() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                AnimatedEmojiDrawable.this.lambda$new$0(tLRPC$Document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLRPC$Document tLRPC$Document) {
        this.document = tLRPC$Document;
        initDocument();
    }

    public AnimatedEmojiDrawable(int i, int i2, TLRPC$Document tLRPC$Document) {
        new AnimatedFloat(1.0f, new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedEmojiDrawable.this.invalidate();
            }
        }, 0L, 150L, new LinearInterpolator());
        this.cacheType = i;
        this.document = tLRPC$Document;
        updateSize();
        initDocument();
    }

    private void updateSize() {
        int i = this.cacheType;
        if (i == 0) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaint.ascent()) + Math.abs(Theme.chat_msgTextPaint.descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 1 || i == 4) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaintEmoji[2].ascent()) + Math.abs(Theme.chat_msgTextPaintEmoji[2].descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 8) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaintEmoji[0].ascent()) + Math.abs(Theme.chat_msgTextPaintEmoji[0].descent())) * 1.15f) / AndroidUtilities.density);
        } else {
            this.sizedp = 34;
        }
    }

    public long getDocumentId() {
        TLRPC$Document tLRPC$Document = this.document;
        return tLRPC$Document != null ? tLRPC$Document.id : this.documentId;
    }

    public TLRPC$Document getDocument() {
        return this.document;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0183  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0215  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0186  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void initDocument() {
        SvgHelper.SvgDrawable svgDrawable;
        ImageLocation imageLocation;
        String str;
        SvgHelper.SvgDrawable svgDrawable2;
        int i;
        int i2;
        int i3;
        if (this.document == null || this.imageReceiver != null) {
            return;
        }
        this.imageReceiver = new ImageReceiver() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable.1
            @Override // org.telegram.messenger.ImageReceiver
            public void invalidate() {
                AnimatedEmojiDrawable.this.invalidate();
                super.invalidate();
            }

            @Override // org.telegram.messenger.ImageReceiver
            protected boolean setImageBitmapByKey(Drawable drawable, String str2, int i4, boolean z, int i5) {
                AnimatedEmojiDrawable.this.invalidate();
                return super.setImageBitmapByKey(drawable, str2, i4, z, i5);
            }
        };
        if (this.colorFilterToSet != null && canOverrideColor()) {
            this.imageReceiver.setColorFilter(this.colorFilterToSet);
        }
        if (this.cacheType != 0) {
            this.imageReceiver.setUniqKeyPrefix(this.cacheType + "_");
        }
        this.imageReceiver.setVideoThumbIsSame(true);
        boolean z = SharedConfig.getDevicePerformanceClass() == 0 && ((i3 = this.cacheType) == 2 || i3 == 3 || i3 == 5);
        String str2 = this.sizedp + "_" + this.sizedp;
        int i4 = this.cacheType;
        if (i4 != 8 && (i4 != 1 || SharedConfig.getDevicePerformanceClass() < 2)) {
            str2 = str2 + "_pcache";
        }
        int i5 = this.cacheType;
        if (i5 != 0 && i5 != 1) {
            str2 = str2 + "_compress";
        }
        if (this.cacheType == 8) {
            str2 = str2 + "firstframe";
        }
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.document.thumbs, 90);
        if ("video/webm".equals(this.document.mime_type)) {
            imageLocation = ImageLocation.getForDocument(this.document);
            str2 = str2 + "_" + ImageLoader.AUTOPLAY_FILTER;
            svgDrawable2 = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
        } else if ("application/x-tgsticker".equals(this.document.mime_type)) {
            StringBuilder sb = new StringBuilder();
            if (this.cacheType != 0) {
                str = this.cacheType + "_";
            } else {
                str = "";
            }
            sb.append(str);
            sb.append(this.documentId);
            sb.append("@");
            sb.append(str2);
            String sb2 = sb.toString();
            if (this.cacheType == 2 || !ImageLoader.getInstance().hasLottieMemCache(sb2)) {
                SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
                if (svgThumb != null && MessageObject.isAnimatedStickerDocument(this.document, true)) {
                    svgThumb.overrideWidthAndHeight(512, 512);
                }
                svgDrawable2 = svgThumb;
            } else {
                svgDrawable2 = null;
            }
            imageLocation = ImageLocation.getForDocument(this.document);
        } else {
            SvgHelper.SvgDrawable svgThumb2 = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
            if (svgThumb2 != null && MessageObject.isAnimatedStickerDocument(this.document, true)) {
                svgThumb2.overrideWidthAndHeight(512, 512);
            }
            svgDrawable = svgThumb2;
            imageLocation = null;
            ImageLocation imageLocation2 = !z ? null : imageLocation;
            if (this.cacheType != 8) {
                ImageReceiver imageReceiver = this.imageReceiver;
                TLRPC$Document tLRPC$Document = this.document;
                imageReceiver.setImage(null, null, imageLocation2, str2, null, null, svgDrawable, tLRPC$Document.size, null, tLRPC$Document, 1);
            } else {
                TLRPC$Document tLRPC$Document2 = this.document;
                this.imageReceiver.setImage(imageLocation2, str2, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.document), this.sizedp + "_" + this.sizedp, null, null, svgDrawable, tLRPC$Document2.size, null, tLRPC$Document2, 1);
            }
            i = this.cacheType;
            if (i != 7 || i == 9) {
                this.imageReceiver.setAutoRepeatCount(2);
            }
            i2 = this.cacheType;
            if (i2 != 3 || i2 == 5 || i2 == 4) {
                this.imageReceiver.setLayerNum(7);
            }
            if (this.cacheType == 9) {
                this.imageReceiver.setLayerNum(6656);
            }
            this.imageReceiver.setAspectFit(true);
            if (this.cacheType == 8) {
                this.imageReceiver.setAllowStartLottieAnimation(true);
                this.imageReceiver.setAllowStartAnimation(true);
                this.imageReceiver.setAutoRepeat(1);
            } else {
                this.imageReceiver.setAllowStartAnimation(false);
                this.imageReceiver.setAllowStartLottieAnimation(false);
                this.imageReceiver.setAutoRepeat(0);
            }
            this.imageReceiver.setAllowDecodeSingleFrame(true);
            int i6 = this.cacheType;
            this.imageReceiver.setRoundRadius((i6 != 5 || i6 == 6) ? AndroidUtilities.dp(6.0f) : 0);
            updateAttachState();
            invalidate();
        }
        svgDrawable = svgDrawable2;
        if (!z) {
        }
        if (this.cacheType != 8) {
        }
        i = this.cacheType;
        if (i != 7) {
        }
        this.imageReceiver.setAutoRepeatCount(2);
        i2 = this.cacheType;
        if (i2 != 3) {
        }
        this.imageReceiver.setLayerNum(7);
        if (this.cacheType == 9) {
        }
        this.imageReceiver.setAspectFit(true);
        if (this.cacheType == 8) {
        }
        this.imageReceiver.setAllowDecodeSingleFrame(true);
        int i62 = this.cacheType;
        this.imageReceiver.setRoundRadius((i62 != 5 || i62 == 6) ? AndroidUtilities.dp(6.0f) : 0);
        updateAttachState();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidate() {
        if (this.views != null) {
            for (int i = 0; i < this.views.size(); i++) {
                View view = this.views.get(i);
                if (view != null) {
                    view.invalidate();
                }
            }
        }
        if (this.holders != null) {
            for (int i2 = 0; i2 < this.holders.size(); i2++) {
                AnimatedEmojiSpan.InvalidateHolder invalidateHolder = this.holders.get(i2);
                if (invalidateHolder != null) {
                    invalidateHolder.invalidate();
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnimatedEmojiDrawable{");
        TLRPC$Document tLRPC$Document = this.document;
        sb.append(tLRPC$Document == null ? "null" : MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document, null));
        sb.append("}");
        return sb.toString();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        draw(canvas, true);
    }

    public void draw(Canvas canvas, boolean z) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.setImageCoords(getBounds());
            this.imageReceiver.setAlpha(this.alpha);
            this.imageReceiver.draw(canvas);
        }
        drawPlaceholder(canvas, getBounds().centerX(), getBounds().centerY(), getBounds().width() / 2.0f);
    }

    public void draw(Canvas canvas, android.graphics.Rect rect, float f) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.setImageCoords(rect);
            this.imageReceiver.setAlpha(f);
            this.imageReceiver.draw(canvas);
        }
        if (rect != null) {
            drawPlaceholder(canvas, rect.centerX(), rect.centerY(), rect.width() / 2.0f);
        }
    }

    public void draw(Canvas canvas, ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder, boolean z) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.setAlpha(this.alpha);
            this.imageReceiver.draw(canvas, backgroundThreadDrawHolder);
        }
        if (backgroundThreadDrawHolder != null) {
            float f = backgroundThreadDrawHolder.imageX;
            float f2 = backgroundThreadDrawHolder.imageW;
            drawPlaceholder(canvas, f + (f2 / 2.0f), backgroundThreadDrawHolder.imageY + (backgroundThreadDrawHolder.imageH / 2.0f), f2 / 2.0f);
        }
    }

    public void addView(View view) {
        if (this.views == null) {
            this.views = new ArrayList<>(10);
        }
        if (!this.views.contains(view)) {
            this.views.add(view);
        }
        updateAttachState();
    }

    public void addView(AnimatedEmojiSpan.InvalidateHolder invalidateHolder) {
        if (this.holders == null) {
            this.holders = new ArrayList<>(10);
        }
        if (!this.holders.contains(invalidateHolder)) {
            this.holders.add(invalidateHolder);
        }
        updateAttachState();
    }

    public void removeView(AnimatedEmojiSpan.InvalidateHolder invalidateHolder) {
        ArrayList<AnimatedEmojiSpan.InvalidateHolder> arrayList = this.holders;
        if (arrayList != null) {
            arrayList.remove(invalidateHolder);
        }
        updateAttachState();
    }

    public void removeView(View view) {
        ArrayList<View> arrayList = this.views;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        updateAttachState();
    }

    private void updateAttachState() {
        ArrayList<AnimatedEmojiSpan.InvalidateHolder> arrayList;
        if (this.imageReceiver == null) {
            return;
        }
        ArrayList<View> arrayList2 = this.views;
        boolean z = (arrayList2 != null && arrayList2.size() > 0) || ((arrayList = this.holders) != null && arrayList.size() > 0);
        if (z == this.attached) {
            return;
        }
        this.attached = z;
        if (z) {
            this.imageReceiver.onAttachedToWindow();
        } else {
            this.imageReceiver.onDetachedFromWindow();
        }
    }

    public boolean canOverrideColor() {
        Boolean bool = this.canOverrideColorCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        TLRPC$Document tLRPC$Document = this.document;
        boolean z = false;
        if (tLRPC$Document == null) {
            return false;
        }
        TLRPC$InputStickerSet inputStickerSet = MessageObject.getInputStickerSet(tLRPC$Document);
        if ((inputStickerSet instanceof TLRPC$TL_inputStickerSetEmojiDefaultStatuses) || ((inputStickerSet instanceof TLRPC$TL_inputStickerSetID) && inputStickerSet.id == 773947703670341676L)) {
            z = true;
        }
        Boolean valueOf = Boolean.valueOf(z);
        this.canOverrideColorCached = valueOf;
        return valueOf.booleanValue();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return (int) (this.alpha * 255.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        float f = i / 255.0f;
        this.alpha = f;
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.setAlpha(f);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.imageReceiver == null) {
            this.colorFilterToSet = colorFilter;
        } else if (!canOverrideColor()) {
        } else {
            this.imageReceiver.setColorFilter(colorFilter);
        }
    }

    public ImageReceiver getImageReceiver() {
        return this.imageReceiver;
    }

    /* loaded from: classes3.dex */
    public static class WrapSizeDrawable extends Drawable {
        private int alpha = 255;
        private Drawable drawable;
        int height;
        int width;

        public WrapSizeDrawable(Drawable drawable, int i, int i2) {
            this.drawable = drawable;
            this.width = i;
            this.height = i2;
        }

        public Drawable getDrawable() {
            return this.drawable;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Drawable drawable = this.drawable;
            if (drawable != null) {
                drawable.setBounds(getBounds());
                this.drawable.setAlpha(this.alpha);
                this.drawable.draw(canvas);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.width;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.height;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.alpha = i;
            Drawable drawable = this.drawable;
            if (drawable != null) {
                drawable.setAlpha(i);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            Drawable drawable = this.drawable;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            Drawable drawable = this.drawable;
            if (drawable != null) {
                return drawable.getOpacity();
            }
            return -2;
        }
    }

    /* loaded from: classes3.dex */
    public static class SwapAnimatedEmojiDrawable extends Drawable {
        private int alpha;
        private int cacheType;
        public boolean center;
        private AnimatedFloat changeProgress;
        private ColorFilter colorFilter;
        private Drawable[] drawables;
        private Integer lastColor;
        private OvershootInterpolator overshootInterpolator;
        private View parentView;
        private int size;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public SwapAnimatedEmojiDrawable(View view, int i) {
            this(view, i, 7);
        }

        public SwapAnimatedEmojiDrawable(View view, int i, int i2) {
            this.center = false;
            this.overshootInterpolator = new OvershootInterpolator(2.0f);
            AnimatedFloat animatedFloat = new AnimatedFloat((View) null, 300L, CubicBezierInterpolator.EASE_OUT);
            this.changeProgress = animatedFloat;
            this.drawables = new Drawable[2];
            this.alpha = 255;
            this.parentView = view;
            animatedFloat.setParent(view);
            this.size = i;
            this.cacheType = i2;
        }

        public void setParentView(View view) {
            removeParentView(this.parentView);
            this.parentView = view;
            addParentView(view);
            this.changeProgress.setParent(view);
            this.parentView = view;
        }

        public void addParentView(View view) {
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr[0]).addView(view);
            }
            Drawable[] drawableArr2 = this.drawables;
            if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr2[1]).addView(view);
            }
        }

        public void removeParentView(View view) {
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr[0]).removeView(view);
            }
            Drawable[] drawableArr2 = this.drawables;
            if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(view);
            }
        }

        public void play() {
            AnimatedEmojiDrawable animatedEmojiDrawable;
            ImageReceiver imageReceiver;
            if (!(getDrawable() instanceof AnimatedEmojiDrawable) || (imageReceiver = (animatedEmojiDrawable = (AnimatedEmojiDrawable) getDrawable()).getImageReceiver()) == null) {
                return;
            }
            if (animatedEmojiDrawable.cacheType == 7 || animatedEmojiDrawable.cacheType == 9) {
                imageReceiver.setAutoRepeatCount(2);
            }
            imageReceiver.startAnimation();
        }

        public void setColor(Integer num) {
            Integer num2 = this.lastColor;
            if (num2 == null && num == null) {
                return;
            }
            if (num2 != null && num2.equals(num)) {
                return;
            }
            this.lastColor = num;
            this.colorFilter = num != null ? new PorterDuffColorFilter(num.intValue(), PorterDuff.Mode.MULTIPLY) : null;
        }

        public Integer getColor() {
            return this.lastColor;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            float f = this.changeProgress.set(1.0f);
            android.graphics.Rect bounds = getBounds();
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[1] != null && f < 1.0f) {
                drawableArr[1].setAlpha((int) (this.alpha * (1.0f - f)));
                Drawable[] drawableArr2 = this.drawables;
                if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                    drawableArr2[1].setBounds(bounds);
                } else if (this.center) {
                    drawableArr2[1].setBounds(bounds.centerX() - (this.drawables[1].getIntrinsicWidth() / 2), bounds.centerY() - (this.drawables[1].getIntrinsicHeight() / 2), bounds.centerX() + (this.drawables[1].getIntrinsicWidth() / 2), bounds.centerY() + (this.drawables[1].getIntrinsicHeight() / 2));
                } else {
                    drawableArr2[1].setBounds(bounds.left, bounds.centerY() - (this.drawables[1].getIntrinsicHeight() / 2), bounds.left + this.drawables[1].getIntrinsicWidth(), bounds.centerY() + (this.drawables[1].getIntrinsicHeight() / 2));
                }
                this.drawables[1].setColorFilter(this.colorFilter);
                this.drawables[1].draw(canvas);
                this.drawables[1].setColorFilter(null);
            }
            if (this.drawables[0] != null) {
                canvas.save();
                Drawable[] drawableArr3 = this.drawables;
                if (drawableArr3[0] instanceof AnimatedEmojiDrawable) {
                    if (((AnimatedEmojiDrawable) drawableArr3[0]).imageReceiver != null) {
                        ((AnimatedEmojiDrawable) this.drawables[0]).imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
                    }
                    if (f < 1.0f) {
                        float interpolation = this.overshootInterpolator.getInterpolation(f);
                        canvas.scale(interpolation, interpolation, bounds.centerX(), bounds.centerY());
                    }
                    this.drawables[0].setBounds(bounds);
                } else if (this.center) {
                    if (f < 1.0f) {
                        float interpolation2 = this.overshootInterpolator.getInterpolation(f);
                        canvas.scale(interpolation2, interpolation2, bounds.centerX(), bounds.centerY());
                    }
                    this.drawables[0].setBounds(bounds.centerX() - (this.drawables[0].getIntrinsicWidth() / 2), bounds.centerY() - (this.drawables[0].getIntrinsicHeight() / 2), bounds.centerX() + (this.drawables[0].getIntrinsicWidth() / 2), bounds.centerY() + (this.drawables[0].getIntrinsicHeight() / 2));
                } else {
                    if (f < 1.0f) {
                        float interpolation3 = this.overshootInterpolator.getInterpolation(f);
                        canvas.scale(interpolation3, interpolation3, bounds.left + (this.drawables[0].getIntrinsicWidth() / 2.0f), bounds.centerY());
                    }
                    this.drawables[0].setBounds(bounds.left, bounds.centerY() - (this.drawables[0].getIntrinsicHeight() / 2), bounds.left + this.drawables[0].getIntrinsicWidth(), bounds.centerY() + (this.drawables[0].getIntrinsicHeight() / 2));
                }
                this.drawables[0].setAlpha(this.alpha);
                this.drawables[0].setColorFilter(this.colorFilter);
                this.drawables[0].draw(canvas);
                this.drawables[0].setColorFilter(null);
                canvas.restore();
            }
        }

        public Drawable getDrawable() {
            return this.drawables[0];
        }

        public void set(long j, boolean z) {
            set(j, this.cacheType, z);
        }

        public void set(long j, int i, boolean z) {
            Drawable[] drawableArr = this.drawables;
            if (!(drawableArr[0] instanceof AnimatedEmojiDrawable) || ((AnimatedEmojiDrawable) drawableArr[0]).getDocumentId() != j) {
                if (z) {
                    this.changeProgress.set(0.0f, true);
                    Drawable[] drawableArr2 = this.drawables;
                    if (drawableArr2[1] != null) {
                        if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                            ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this.parentView);
                        }
                        this.drawables[1] = null;
                    }
                    Drawable[] drawableArr3 = this.drawables;
                    drawableArr3[1] = drawableArr3[0];
                    drawableArr3[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, j);
                    ((AnimatedEmojiDrawable) this.drawables[0]).addView(this.parentView);
                } else {
                    this.changeProgress.set(1.0f, true);
                    detach();
                    this.drawables[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, j);
                    ((AnimatedEmojiDrawable) this.drawables[0]).addView(this.parentView);
                }
                this.lastColor = -1;
                this.colorFilter = null;
                play();
                View view = this.parentView;
                if (view == null) {
                    return;
                }
                view.invalidate();
            }
        }

        public void set(TLRPC$Document tLRPC$Document, boolean z) {
            set(tLRPC$Document, this.cacheType, z);
        }

        public void set(TLRPC$Document tLRPC$Document, int i, boolean z) {
            Drawable[] drawableArr = this.drawables;
            if (!(drawableArr[0] instanceof AnimatedEmojiDrawable) || tLRPC$Document == null || ((AnimatedEmojiDrawable) drawableArr[0]).getDocumentId() != tLRPC$Document.id) {
                if (z) {
                    this.changeProgress.set(0.0f, true);
                    Drawable[] drawableArr2 = this.drawables;
                    if (drawableArr2[1] != null) {
                        if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                            ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this.parentView);
                        }
                        this.drawables[1] = null;
                    }
                    Drawable[] drawableArr3 = this.drawables;
                    drawableArr3[1] = drawableArr3[0];
                    if (tLRPC$Document != null) {
                        drawableArr3[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, tLRPC$Document);
                        ((AnimatedEmojiDrawable) this.drawables[0]).addView(this.parentView);
                    } else {
                        drawableArr3[0] = null;
                    }
                } else {
                    this.changeProgress.set(1.0f, true);
                    detach();
                    if (tLRPC$Document != null) {
                        this.drawables[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, tLRPC$Document);
                        ((AnimatedEmojiDrawable) this.drawables[0]).addView(this.parentView);
                    } else {
                        this.drawables[0] = null;
                    }
                }
                this.lastColor = -1;
                this.colorFilter = null;
                play();
                View view = this.parentView;
                if (view == null) {
                    return;
                }
                view.invalidate();
            }
        }

        public void set(Drawable drawable, boolean z) {
            if (this.drawables[0] == drawable) {
                return;
            }
            if (z) {
                this.changeProgress.set(0.0f, true);
                Drawable[] drawableArr = this.drawables;
                if (drawableArr[1] != null) {
                    if (drawableArr[1] instanceof AnimatedEmojiDrawable) {
                        ((AnimatedEmojiDrawable) drawableArr[1]).removeView(this.parentView);
                    }
                    this.drawables[1] = null;
                }
                Drawable[] drawableArr2 = this.drawables;
                drawableArr2[1] = drawableArr2[0];
                drawableArr2[0] = drawable;
            } else {
                this.changeProgress.set(1.0f, true);
                detach();
                this.drawables[0] = drawable;
            }
            this.lastColor = -1;
            this.colorFilter = null;
            play();
            View view = this.parentView;
            if (view == null) {
                return;
            }
            view.invalidate();
        }

        public void detach() {
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr[0]).removeView(this.parentView);
            }
            Drawable[] drawableArr2 = this.drawables;
            if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this.parentView);
            }
        }

        public void attach() {
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr[0]).addView(this.parentView);
            }
            Drawable[] drawableArr2 = this.drawables;
            if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr2[1]).addView(this.parentView);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.size;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.size;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.alpha = i;
        }
    }
}
