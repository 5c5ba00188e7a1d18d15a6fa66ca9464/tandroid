package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
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
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SharedConfig;
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
import org.telegram.ui.Components.Premium.PremiumLockIconView;
import org.telegram.ui.SelectAnimatedEmojiDialog;
/* loaded from: classes3.dex */
public class AnimatedEmojiDrawable extends Drawable {
    private static boolean LOG_MEMORY_LEAK = false;
    public static int attachedCount;
    public static ArrayList<AnimatedEmojiDrawable> attachedDrawable;
    private static HashMap<Long, Integer> dominantColors;
    private static HashMap<Integer, EmojiDocumentFetcher> fetchers;
    private static SparseArray<LongSparseArray<AnimatedEmojiDrawable>> globalEmojiCache;
    private String absolutePath;
    private boolean attached;
    private int cacheType;
    private ColorFilter colorFilterToSet;
    private int currentAccount;
    private TLRPC$Document document;
    private long documentId;
    private ArrayList<AnimatedEmojiSpan.InvalidateHolder> holders;
    private ImageReceiver imageReceiver;
    public int rawDrawIndex;
    public int sizedp;
    private ArrayList<View> views;
    private float alpha = 1.0f;
    private Boolean canOverrideColorCached = null;
    private Boolean isDefaultStatusEmojiCached = null;

    /* loaded from: classes3.dex */
    public interface ReceivedDocument {
        void run(TLRPC$Document tLRPC$Document);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public static AnimatedEmojiDrawable make(int i, int i2, long j) {
        return make(i, i2, j, null);
    }

    public static AnimatedEmojiDrawable make(int i, int i2, long j, String str) {
        if (globalEmojiCache == null) {
            globalEmojiCache = new SparseArray<>();
        }
        int hash = Objects.hash(Integer.valueOf(i), Integer.valueOf(i2));
        LongSparseArray<AnimatedEmojiDrawable> longSparseArray = globalEmojiCache.get(hash);
        if (longSparseArray == null) {
            SparseArray<LongSparseArray<AnimatedEmojiDrawable>> sparseArray = globalEmojiCache;
            LongSparseArray<AnimatedEmojiDrawable> longSparseArray2 = new LongSparseArray<>();
            sparseArray.put(hash, longSparseArray2);
            longSparseArray = longSparseArray2;
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = longSparseArray.get(j);
        if (animatedEmojiDrawable == null) {
            AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(i2, i, j, str);
            longSparseArray.put(j, animatedEmojiDrawable2);
            return animatedEmojiDrawable2;
        }
        return animatedEmojiDrawable;
    }

    public static AnimatedEmojiDrawable make(int i, int i2, TLRPC$Document tLRPC$Document) {
        if (globalEmojiCache == null) {
            globalEmojiCache = new SparseArray<>();
        }
        int hash = Objects.hash(Integer.valueOf(i), Integer.valueOf(i2));
        LongSparseArray<AnimatedEmojiDrawable> longSparseArray = globalEmojiCache.get(hash);
        if (longSparseArray == null) {
            SparseArray<LongSparseArray<AnimatedEmojiDrawable>> sparseArray = globalEmojiCache;
            LongSparseArray<AnimatedEmojiDrawable> longSparseArray2 = new LongSparseArray<>();
            sparseArray.put(hash, longSparseArray2);
            longSparseArray = longSparseArray2;
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = longSparseArray.get(tLRPC$Document.id);
        if (animatedEmojiDrawable == null) {
            long j = tLRPC$Document.id;
            AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(i2, i, tLRPC$Document);
            longSparseArray.put(j, animatedEmojiDrawable2);
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
            if (this.imageReceiver.getAnimation() != null) {
                this.imageReceiver.getAnimation().updateCurrentFrame(j, true);
            }
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
            synchronized (this) {
                HashMap<Long, TLRPC$Document> hashMap = this.emojiDocumentsCache;
                if (hashMap != null && (tLRPC$Document = hashMap.get(Long.valueOf(j))) != null) {
                    if (receivedDocument != null) {
                        receivedDocument.run(tLRPC$Document);
                    }
                } else if (checkThread()) {
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
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fetchDocument$0() {
            ArrayList<Long> arrayList = new ArrayList<>(this.toFetchDocuments);
            this.toFetchDocuments.clear();
            loadFromDatabase(arrayList);
            this.fetchRunnable = null;
        }

        private boolean checkThread() {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.e("EmojiDocumentFetcher", new IllegalStateException("Wrong thread"));
                    return false;
                }
                return false;
            }
            return true;
        }

        private void loadFromDatabase(final ArrayList<Long> arrayList) {
            final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromDatabase$2(messagesStorage, arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromDatabase$2(MessagesStorage messagesStorage, ArrayList arrayList) {
            SQLiteDatabase database = messagesStorage.getDatabase();
            if (database == null) {
                return;
            }
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromDatabase$1(arrayList2, hashSet);
                    }
                });
                queryFinalized.dispose();
            } catch (SQLiteException e2) {
                messagesStorage.checkSQLException(e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadFromDatabase$1(ArrayList arrayList, HashSet hashSet) {
            processDocuments(arrayList);
            if (hashSet.isEmpty()) {
                return;
            }
            loadFromServer(new ArrayList<>(hashSet));
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda3
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
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$putToStorage$5(arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:18:0x004d A[Catch: SQLiteException -> 0x0057, TryCatch #2 {SQLiteException -> 0x0057, blocks: (B:3:0x000a, B:4:0x0011, B:6:0x0017, B:8:0x001f, B:18:0x004d, B:16:0x0047, B:19:0x0050, B:20:0x0053), top: B:29:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0050 A[SYNTHETIC] */
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
            if (checkThread()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i) instanceof TLRPC$Document) {
                        TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i);
                        putDocument(tLRPC$Document);
                        HashMap<Long, ArrayList<ReceivedDocument>> hashMap = this.loadingDocuments;
                        if (hashMap != null && (remove = hashMap.remove(Long.valueOf(tLRPC$Document.id))) != null) {
                            for (int i2 = 0; i2 < remove.size(); i2++) {
                                ReceivedDocument receivedDocument = remove.get(i2);
                                if (receivedDocument != null) {
                                    receivedDocument.run(tLRPC$Document);
                                }
                            }
                            remove.clear();
                        }
                    }
                }
            }
        }

        public void putDocument(TLRPC$Document tLRPC$Document) {
            if (tLRPC$Document == null) {
                return;
            }
            synchronized (this) {
                if (this.emojiDocumentsCache == null) {
                    this.emojiDocumentsCache = new HashMap<>();
                }
                this.emojiDocumentsCache.put(Long.valueOf(tLRPC$Document.id), tLRPC$Document);
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
        this.currentAccount = i2;
        this.cacheType = i;
        updateSize();
        this.documentId = j;
        getDocumentFetcher(i2).fetchDocument(j, new ReceivedDocument() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                AnimatedEmojiDrawable.this.lambda$new$0(tLRPC$Document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLRPC$Document tLRPC$Document) {
        this.document = tLRPC$Document;
        initDocument(false);
    }

    public AnimatedEmojiDrawable(int i, int i2, long j, String str) {
        this.currentAccount = i2;
        this.cacheType = i;
        updateSize();
        this.documentId = j;
        this.absolutePath = str;
        getDocumentFetcher(i2).fetchDocument(j, new ReceivedDocument() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                AnimatedEmojiDrawable.this.lambda$new$1(tLRPC$Document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(TLRPC$Document tLRPC$Document) {
        this.document = tLRPC$Document;
        initDocument(false);
    }

    public AnimatedEmojiDrawable(int i, int i2, TLRPC$Document tLRPC$Document) {
        this.cacheType = i;
        this.currentAccount = i2;
        this.document = tLRPC$Document;
        updateSize();
        initDocument(false);
    }

    private void updateSize() {
        int i = this.cacheType;
        if (i == 0) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaint.ascent()) + Math.abs(Theme.chat_msgTextPaint.descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 1 || i == 4) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaintEmoji[2].ascent()) + Math.abs(Theme.chat_msgTextPaintEmoji[2].descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 8) {
            this.sizedp = (int) (((Math.abs(Theme.chat_msgTextPaintEmoji[0].ascent()) + Math.abs(Theme.chat_msgTextPaintEmoji[0].descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 14 || i == 15) {
            this.sizedp = 100;
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v17, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v18, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v19, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v21, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v23, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r23v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r23v1, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r25v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r25v1, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r25v2, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r2v2, types: [org.telegram.messenger.SvgHelper$SvgDrawable] */
    /* JADX WARN: Type inference failed for: r2v3, types: [org.telegram.messenger.SvgHelper$SvgDrawable] */
    private void initDocument(boolean z) {
        int i;
        String str;
        Object obj;
        if (this.document != null) {
            ImageReceiver imageReceiver = this.imageReceiver;
            if (imageReceiver == null || z) {
                if (imageReceiver == null) {
                    this.imageReceiver = new ImageReceiver() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable.1
                        @Override // org.telegram.messenger.ImageReceiver
                        public void invalidate() {
                            AnimatedEmojiDrawable.this.invalidate();
                            super.invalidate();
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.messenger.ImageReceiver
                        public boolean setImageBitmapByKey(Drawable drawable, String str2, int i2, boolean z2, int i3) {
                            AnimatedEmojiDrawable.this.invalidate();
                            return super.setImageBitmapByKey(drawable, str2, i2, z2, i3);
                        }
                    };
                }
                if (this.cacheType == 12) {
                    this.imageReceiver.ignoreNotifications = true;
                }
                if (this.colorFilterToSet != null && canOverrideColor()) {
                    this.imageReceiver.setColorFilter(this.colorFilterToSet);
                }
                int i2 = this.cacheType;
                if (i2 != 0) {
                    if (i2 == 12) {
                        i2 = 2;
                    }
                    this.imageReceiver.setUniqKeyPrefix(i2 + "_");
                }
                this.imageReceiver.setVideoThumbIsSame(true);
                boolean z2 = (SharedConfig.getDevicePerformanceClass() == 0 && this.cacheType == 5) || (((i = this.cacheType) == 2 || i == 3) && !LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD));
                if (this.cacheType == 13) {
                    z2 = true;
                }
                String str2 = this.sizedp + "_" + this.sizedp;
                if (this.cacheType == 12) {
                    str2 = str2 + "_d_nostream";
                }
                int i3 = this.cacheType;
                if (i3 != 15 && i3 != 14 && i3 != 8 && ((i3 != 1 || SharedConfig.getDevicePerformanceClass() < 2) && this.cacheType != 12)) {
                    str2 = str2 + "_pcache";
                }
                int i4 = this.cacheType;
                if (i4 != 0 && i4 != 1 && i4 != 14 && i4 != 15) {
                    str2 = str2 + "_compress";
                }
                if (this.cacheType == 8) {
                    str2 = str2 + "firstframe";
                }
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.document.thumbs, 90);
                Object obj2 = null;
                if ("video/webm".equals(this.document.mime_type)) {
                    obj2 = ImageLocation.getForDocument(this.document);
                    str2 = str2 + "_" + ImageLoader.AUTOPLAY_FILTER;
                    obj = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
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
                    if (SharedConfig.getDevicePerformanceClass() != 0 || this.cacheType == 2 || !ImageLoader.getInstance().hasLottieMemCache(sb2)) {
                        ?? svgThumb = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
                        if (svgThumb != 0 && MessageObject.isAnimatedStickerDocument(this.document, true)) {
                            svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                        }
                        obj2 = svgThumb;
                    }
                    Object obj3 = obj2;
                    obj2 = ImageLocation.getForDocument(this.document);
                    obj = obj3;
                } else {
                    ?? svgThumb2 = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
                    obj = svgThumb2;
                    if (svgThumb2 != 0) {
                        obj = svgThumb2;
                        if (MessageObject.isAnimatedStickerDocument(this.document, true)) {
                            svgThumb2.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                            obj = svgThumb2;
                        }
                    }
                }
                if (this.absolutePath != null) {
                    this.imageReceiver.setImageBitmap(new AnimatedFileDrawable(new File(this.absolutePath), true, 0L, 0, null, null, null, 0L, this.currentAccount, true, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, null));
                } else if (this.cacheType == 8) {
                    ?? r1 = this.imageReceiver;
                    TLRPC$Document tLRPC$Document = this.document;
                    r1.setImage(null, null, obj2, str2, null, null, obj, tLRPC$Document.size, null, tLRPC$Document, 1);
                } else if (z2 || (!LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD) && this.cacheType != 14)) {
                    if ("video/webm".equals(this.document.mime_type)) {
                        TLRPC$Document tLRPC$Document2 = this.document;
                        this.imageReceiver.setImage(null, null, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.document), this.sizedp + "_" + this.sizedp, null, null, obj, tLRPC$Document2.size, null, tLRPC$Document2, 1);
                    } else if (MessageObject.isAnimatedStickerDocument(this.document, true)) {
                        TLRPC$Document tLRPC$Document3 = this.document;
                        this.imageReceiver.setImage(obj2, str2 + "_firstframe", null, null, obj, tLRPC$Document3.size, null, tLRPC$Document3, 1);
                    } else {
                        TLRPC$Document tLRPC$Document4 = this.document;
                        this.imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize, this.document), this.sizedp + "_" + this.sizedp, null, null, obj, tLRPC$Document4.size, null, tLRPC$Document4, 1);
                    }
                } else {
                    TLRPC$Document tLRPC$Document5 = this.document;
                    this.imageReceiver.setImage(obj2, str2, ImageLocation.getForDocument(closestPhotoSizeWithSize, this.document), this.sizedp + "_" + this.sizedp, null, null, obj, tLRPC$Document5.size, null, tLRPC$Document5, 1);
                }
                updateAutoRepeat(this.imageReceiver);
                int i5 = this.cacheType;
                if (i5 == 13 || i5 == 3 || i5 == 5 || i5 == 4) {
                    this.imageReceiver.setLayerNum(7);
                }
                if (this.cacheType == 9) {
                    this.imageReceiver.setLayerNum(6656);
                }
                this.imageReceiver.setAspectFit(true);
                int i6 = this.cacheType;
                if (i6 == 12 || i6 == 8 || i6 == 6 || i6 == 5) {
                    this.imageReceiver.setAllowStartAnimation(false);
                    this.imageReceiver.setAllowStartLottieAnimation(false);
                    this.imageReceiver.setAutoRepeat(0);
                } else {
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setAllowStartAnimation(true);
                    this.imageReceiver.setAutoRepeat(1);
                }
                this.imageReceiver.setAllowDecodeSingleFrame(true);
                int i7 = this.cacheType;
                this.imageReceiver.setRoundRadius((i7 == 5 || i7 == 6) ? AndroidUtilities.dp(6.0f) : 0);
                updateAttachState();
                invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAutoRepeat(ImageReceiver imageReceiver) {
        int i = this.cacheType;
        if (i == 7 || i == 9 || i == 10) {
            imageReceiver.setAutoRepeatCount(2);
        } else if (i == 11 || i == 14 || i == 6 || i == 5) {
            imageReceiver.setAutoRepeatCount(1);
        }
    }

    void invalidate() {
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
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver == null) {
            return;
        }
        imageReceiver.setImageCoords(getBounds());
        this.imageReceiver.setAlpha(this.alpha);
        this.imageReceiver.draw(canvas);
    }

    public void drawRaw(Canvas canvas, boolean z, int i) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver == null) {
            return;
        }
        if (imageReceiver.getLottieAnimation() != null) {
            RLottieDrawable lottieAnimation = this.imageReceiver.getLottieAnimation();
            if (z) {
                lottieAnimation.currentFrame = (lottieAnimation.currentFrame + Math.round((lottieAnimation.getFramesCount() / (((float) lottieAnimation.getDuration()) / 1000.0f)) / 30.0f)) % lottieAnimation.getFramesCount();
            }
            lottieAnimation.setBounds(getBounds());
            lottieAnimation.drawFrame(canvas, lottieAnimation.currentFrame);
        } else if (this.imageReceiver.getAnimation() != null) {
            this.imageReceiver.getAnimation().drawFrame(canvas, z ? i / 30 : 0);
        } else {
            this.imageReceiver.setImageCoords(getBounds());
            this.imageReceiver.setAlpha(this.alpha);
            this.imageReceiver.draw(canvas);
        }
    }

    public void draw(Canvas canvas, android.graphics.Rect rect, float f) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver == null) {
            return;
        }
        imageReceiver.setImageCoords(rect);
        this.imageReceiver.setAlpha(f);
        this.imageReceiver.draw(canvas);
    }

    public void draw(Canvas canvas, ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder, boolean z) {
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver == null) {
            return;
        }
        imageReceiver.setAlpha(this.alpha);
        this.imageReceiver.draw(canvas, backgroundThreadDrawHolder);
    }

    public void addView(View view) {
        if (view instanceof SelectAnimatedEmojiDialog.EmojiListView) {
            throw new RuntimeException();
        }
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
        if (z != this.attached) {
            this.attached = z;
            if (z) {
                this.imageReceiver.onAttachedToWindow();
            } else {
                this.imageReceiver.onDetachedFromWindow();
            }
            if (LOG_MEMORY_LEAK) {
                if (attachedDrawable == null) {
                    attachedDrawable = new ArrayList<>();
                }
                if (this.attached) {
                    attachedCount++;
                    attachedDrawable.add(this);
                } else {
                    attachedCount--;
                    attachedDrawable.remove(this);
                }
                Log.d("animatedDrawable", "attached count " + attachedCount);
            }
        }
    }

    public boolean canOverrideColor() {
        Boolean bool = this.canOverrideColorCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        boolean z = false;
        if (this.document != null) {
            Boolean valueOf = Boolean.valueOf((isDefaultStatusEmoji() || MessageObject.isTextColorEmoji(this.document)) ? true : true);
            this.canOverrideColorCached = valueOf;
            return valueOf.booleanValue();
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
        if (r2 != 2964141614563343L) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isDefaultStatusEmoji() {
        Boolean bool = this.isDefaultStatusEmojiCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        TLRPC$Document tLRPC$Document = this.document;
        boolean z = false;
        if (tLRPC$Document != null) {
            TLRPC$InputStickerSet inputStickerSet = MessageObject.getInputStickerSet(tLRPC$Document);
            if (!(inputStickerSet instanceof TLRPC$TL_inputStickerSetEmojiDefaultStatuses)) {
                if (inputStickerSet instanceof TLRPC$TL_inputStickerSetID) {
                    long j = inputStickerSet.id;
                    if (j != 773947703670341676L) {
                    }
                }
                Boolean valueOf = Boolean.valueOf(z);
                this.isDefaultStatusEmojiCached = valueOf;
                return valueOf.booleanValue();
            }
            z = true;
            Boolean valueOf2 = Boolean.valueOf(z);
            this.isDefaultStatusEmojiCached = valueOf2;
            return valueOf2.booleanValue();
        }
        return false;
    }

    public static boolean isDefaultStatusEmoji(Drawable drawable) {
        if (drawable instanceof AnimatedEmojiDrawable) {
            return isDefaultStatusEmoji((AnimatedEmojiDrawable) drawable);
        }
        return false;
    }

    public static boolean isDefaultStatusEmoji(AnimatedEmojiDrawable animatedEmojiDrawable) {
        return animatedEmojiDrawable != null && animatedEmojiDrawable.isDefaultStatusEmoji();
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
        if (this.imageReceiver == null || this.document == null) {
            this.colorFilterToSet = colorFilter;
        } else if (canOverrideColor()) {
            this.imageReceiver.setColorFilter(colorFilter);
        }
    }

    public ImageReceiver getImageReceiver() {
        return this.imageReceiver;
    }

    public static int getDominantColor(AnimatedEmojiDrawable animatedEmojiDrawable) {
        if (animatedEmojiDrawable == null) {
            return 0;
        }
        long documentId = animatedEmojiDrawable.getDocumentId();
        if (documentId == 0) {
            return 0;
        }
        if (dominantColors == null) {
            dominantColors = new HashMap<>();
        }
        Integer num = dominantColors.get(Long.valueOf(documentId));
        if (num == null && animatedEmojiDrawable.getImageReceiver() != null && animatedEmojiDrawable.getImageReceiver().getBitmap() != null) {
            HashMap<Long, Integer> hashMap = dominantColors;
            Long valueOf = Long.valueOf(documentId);
            Integer valueOf2 = Integer.valueOf(PremiumLockIconView.getDominantColor(animatedEmojiDrawable.getImageReceiver().getBitmap()));
            hashMap.put(valueOf, valueOf2);
            num = valueOf2;
        }
        if (num == null) {
            return 0;
        }
        return num.intValue();
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
    public static class SwapAnimatedEmojiDrawable extends Drawable implements AnimatedEmojiSpan.InvalidateHolder {
        private int alpha;
        boolean attached;
        private int cacheType;
        public boolean center;
        private AnimatedFloat changeProgress;
        private ColorFilter colorFilter;
        private Drawable[] drawables;
        private boolean invalidateParent;
        private Integer lastColor;
        private OvershootInterpolator overshootInterpolator;
        private View parentView;
        private View secondParent;
        private int size;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public SwapAnimatedEmojiDrawable(View view, int i) {
            this(view, false, i, 7);
        }

        public SwapAnimatedEmojiDrawable(View view, boolean z, int i) {
            this(view, z, i, 7);
        }

        public SwapAnimatedEmojiDrawable(View view, int i, int i2) {
            this(view, false, i, i2);
        }

        public SwapAnimatedEmojiDrawable(View view, boolean z, int i, int i2) {
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
            this.invalidateParent = z;
        }

        public void setParentView(View view) {
            this.changeProgress.setParent(view);
            this.parentView = view;
        }

        public void play() {
            AnimatedEmojiDrawable animatedEmojiDrawable;
            ImageReceiver imageReceiver;
            if (!(getDrawable() instanceof AnimatedEmojiDrawable) || (imageReceiver = (animatedEmojiDrawable = (AnimatedEmojiDrawable) getDrawable()).getImageReceiver()) == null) {
                return;
            }
            animatedEmojiDrawable.updateAutoRepeat(imageReceiver);
            imageReceiver.startAnimation();
        }

        public void setColor(Integer num) {
            Integer num2 = this.lastColor;
            if (num2 == null && num == null) {
                return;
            }
            if (num2 == null || !num2.equals(num)) {
                this.lastColor = num;
                this.colorFilter = num != null ? new PorterDuffColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN) : null;
            }
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
            if ((drawableArr[0] instanceof AnimatedEmojiDrawable) && ((AnimatedEmojiDrawable) drawableArr[0]).getDocumentId() == j) {
                return;
            }
            if (z) {
                this.changeProgress.set(0.0f, true);
                Drawable[] drawableArr2 = this.drawables;
                if (drawableArr2[1] != null) {
                    if (this.attached && (drawableArr2[1] instanceof AnimatedEmojiDrawable)) {
                        ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this);
                    }
                    this.drawables[1] = null;
                }
                Drawable[] drawableArr3 = this.drawables;
                drawableArr3[1] = drawableArr3[0];
                drawableArr3[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, j);
                if (this.attached) {
                    ((AnimatedEmojiDrawable) this.drawables[0]).addView(this);
                }
            } else {
                this.changeProgress.set(1.0f, true);
                boolean z2 = this.attached;
                if (z2) {
                    detach();
                }
                this.drawables[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, j);
                if (z2) {
                    attach();
                }
            }
            this.lastColor = -1;
            this.colorFilter = null;
            play();
            invalidate();
        }

        public void set(TLRPC$Document tLRPC$Document, boolean z) {
            set(tLRPC$Document, this.cacheType, z);
        }

        public void set(TLRPC$Document tLRPC$Document, int i, boolean z) {
            Drawable[] drawableArr = this.drawables;
            if ((drawableArr[0] instanceof AnimatedEmojiDrawable) && tLRPC$Document != null && ((AnimatedEmojiDrawable) drawableArr[0]).getDocumentId() == tLRPC$Document.id) {
                return;
            }
            if (z) {
                this.changeProgress.set(0.0f, true);
                Drawable[] drawableArr2 = this.drawables;
                if (drawableArr2[1] != null) {
                    if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                        ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this);
                    }
                    this.drawables[1] = null;
                }
                Drawable[] drawableArr3 = this.drawables;
                drawableArr3[1] = drawableArr3[0];
                if (tLRPC$Document != null) {
                    drawableArr3[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, tLRPC$Document);
                    if (this.attached) {
                        ((AnimatedEmojiDrawable) this.drawables[0]).addView(this);
                    }
                } else {
                    drawableArr3[0] = null;
                }
            } else {
                this.changeProgress.set(1.0f, true);
                boolean z2 = this.attached;
                if (z2) {
                    detach();
                }
                if (tLRPC$Document != null) {
                    this.drawables[0] = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, i, tLRPC$Document);
                } else {
                    this.drawables[0] = null;
                }
                if (z2) {
                    attach();
                }
            }
            this.lastColor = -1;
            this.colorFilter = null;
            play();
            invalidate();
        }

        public void set(Drawable drawable, boolean z) {
            if (this.drawables[0] == drawable) {
                return;
            }
            if (z) {
                this.changeProgress.set(0.0f, true);
                Drawable[] drawableArr = this.drawables;
                if (drawableArr[1] != null) {
                    if (this.attached && (drawableArr[1] instanceof AnimatedEmojiDrawable)) {
                        ((AnimatedEmojiDrawable) drawableArr[1]).removeView(this);
                    }
                    this.drawables[1] = null;
                }
                Drawable[] drawableArr2 = this.drawables;
                drawableArr2[1] = drawableArr2[0];
                drawableArr2[0] = drawable;
            } else {
                this.changeProgress.set(1.0f, true);
                boolean z2 = this.attached;
                if (z2) {
                    detach();
                }
                this.drawables[0] = drawable;
                if (z2) {
                    attach();
                }
            }
            this.lastColor = -1;
            this.colorFilter = null;
            play();
            invalidate();
        }

        public void detach() {
            if (this.attached) {
                this.attached = false;
                Drawable[] drawableArr = this.drawables;
                if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawableArr[0]).removeView(this);
                }
                Drawable[] drawableArr2 = this.drawables;
                if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawableArr2[1]).removeView(this);
                }
            }
        }

        public void attach() {
            if (this.attached) {
                return;
            }
            this.attached = true;
            Drawable[] drawableArr = this.drawables;
            if (drawableArr[0] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr[0]).addView(this);
            }
            Drawable[] drawableArr2 = this.drawables;
            if (drawableArr2[1] instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawableArr2[1]).addView(this);
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

        @Override // org.telegram.ui.Components.AnimatedEmojiSpan.InvalidateHolder
        public void invalidate() {
            View view = this.parentView;
            if (view != null) {
                if (this.invalidateParent && (view.getParent() instanceof View)) {
                    ((View) this.parentView.getParent()).invalidate();
                } else {
                    this.parentView.invalidate();
                }
            }
            View view2 = this.secondParent;
            if (view2 != null) {
                view2.invalidate();
            }
            invalidateSelf();
        }

        public void setSecondParent(View view) {
            this.secondParent = view;
        }
    }

    public static void updateAll() {
        if (globalEmojiCache == null) {
            return;
        }
        for (int i = 0; i < globalEmojiCache.size(); i++) {
            LongSparseArray<AnimatedEmojiDrawable> valueAt = globalEmojiCache.valueAt(i);
            for (int i2 = 0; i2 < valueAt.size(); i2++) {
                long keyAt = valueAt.keyAt(i2);
                AnimatedEmojiDrawable animatedEmojiDrawable = valueAt.get(keyAt);
                if (animatedEmojiDrawable != null && animatedEmojiDrawable.attached) {
                    animatedEmojiDrawable.initDocument(true);
                } else {
                    valueAt.remove(keyAt);
                }
            }
        }
    }
}
