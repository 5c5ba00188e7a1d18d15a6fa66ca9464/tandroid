package org.telegram.ui.Components;

import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
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
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getCustomEmojiDocuments;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
/* loaded from: classes3.dex */
public class AnimatedEmojiDrawable extends Drawable {
    static int count = 0;
    private static HashMap<Integer, EmojiDocumentFetcher> fetchers = null;
    private static HashMap<Integer, HashMap<Long, AnimatedEmojiDrawable>> globalEmojiCache = null;
    public static int sizedp = 30;
    private float alpha = 1.0f;
    private boolean attached;
    private int cacheType;
    private TLRPC$Document document;
    private long documentId;
    private ArrayList<AnimatedEmojiSpan.AnimatedEmojiHolder> holders;
    private EmojiImageReceiver imageReceiver;
    private ArrayList<View> views;

    /* loaded from: classes3.dex */
    public interface ReceivedDocument {
        void run(TLRPC$Document tLRPC$Document);
    }

    private void drawDebugCacheType(Canvas canvas) {
    }

    private void drawPlaceholder(Canvas canvas, float f, float f2, float f3) {
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
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
        return SharedConfig.getDevicePerformanceClass() == 0 ? 0 : 1;
    }

    public void setTime(long j) {
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            emojiImageReceiver.setCurrentTime(j);
        }
    }

    public void update(long j) {
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            if (emojiImageReceiver.getLottieAnimation() != null) {
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

        public /* synthetic */ void lambda$loadFromServer$4(final ArrayList arrayList, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiDocumentFetcher.this.lambda$loadFromServer$3(arrayList, tLObject);
                }
            });
        }

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

        /* JADX WARN: Removed duplicated region for block: B:17:0x004d A[Catch: SQLiteException -> 0x0057, TryCatch #2 {SQLiteException -> 0x0057, blocks: (B:3:0x000a, B:4:0x0011, B:6:0x0017, B:8:0x001f, B:15:0x0047, B:17:0x004d, B:18:0x0050, B:19:0x0053), top: B:27:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0050 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$putToStorage$5(ArrayList arrayList) {
            NativeByteBuffer nativeByteBuffer;
            Exception e;
            try {
                SQLitePreparedStatement executeFast = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO animated_emoji VALUES(?, ?)");
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i) instanceof TLRPC$Document) {
                        TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i);
                        NativeByteBuffer nativeByteBuffer2 = null;
                        try {
                            nativeByteBuffer = new NativeByteBuffer(tLRPC$Document.getObjectSize());
                        } catch (Exception e2) {
                            e = e2;
                        }
                        try {
                            tLRPC$Document.serializeToStream(nativeByteBuffer);
                            executeFast.requery();
                            executeFast.bindLong(1, tLRPC$Document.id);
                            executeFast.bindByteBuffer(2, nativeByteBuffer);
                            executeFast.step();
                        } catch (Exception e3) {
                            e = e3;
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
            } catch (SQLiteException e4) {
                FileLog.e(e4);
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
        new AnimatedFloat(1.0f, new AnimatedEmojiDrawable$$ExternalSyntheticLambda0(this), 0L, 150L, new LinearInterpolator());
        this.cacheType = i;
        if (i == 0) {
            TextPaint textPaint = Theme.chat_msgTextPaint;
            sizedp = (int) (((Math.abs(textPaint.ascent()) + Math.abs(textPaint.descent())) * 1.15f) / AndroidUtilities.density);
        } else if (i == 1 || i == 3 || i == 2) {
            sizedp = 34;
        }
        this.documentId = j;
        getDocumentFetcher(i2).fetchDocument(j, new ReceivedDocument() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                AnimatedEmojiDrawable.this.lambda$new$0(tLRPC$Document);
            }
        });
    }

    public /* synthetic */ void lambda$new$0(TLRPC$Document tLRPC$Document) {
        this.document = tLRPC$Document;
        initDocument();
    }

    public AnimatedEmojiDrawable(int i, int i2, TLRPC$Document tLRPC$Document) {
        new AnimatedFloat(1.0f, new AnimatedEmojiDrawable$$ExternalSyntheticLambda0(this), 0L, 150L, new LinearInterpolator());
        this.cacheType = i;
        this.document = tLRPC$Document;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedEmojiDrawable.this.initDocument();
            }
        });
    }

    public long getDocumentId() {
        TLRPC$Document tLRPC$Document = this.document;
        return tLRPC$Document != null ? tLRPC$Document.id : this.documentId;
    }

    public TLRPC$Document getDocument() {
        return this.document;
    }

    public void initDocument() {
        SvgHelper.SvgDrawable svgDrawable;
        String str;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        ImageLocation imageLocation;
        String str2;
        SvgHelper.SvgDrawable svgDrawable2;
        int i;
        if (this.document == null || this.imageReceiver != null) {
            return;
        }
        EmojiImageReceiver emojiImageReceiver = new EmojiImageReceiver(new AnimatedEmojiDrawable$$ExternalSyntheticLambda0(this));
        this.imageReceiver = emojiImageReceiver;
        if (this.cacheType != 0) {
            emojiImageReceiver.setUniqKeyPrefix(this.cacheType + "_");
        }
        String str3 = sizedp + "_" + sizedp + "_pcache";
        if (this.cacheType != 0) {
            str3 = str3 + "_compress";
        }
        boolean z = SharedConfig.getDevicePerformanceClass() == 0 && ((i = this.cacheType) == 1 || i == 2);
        if ("video/webm".equals(this.document.mime_type)) {
            FileLoader.getClosestPhotoSizeWithSize(this.document.thumbs, 90);
            this.imageReceiver.setParentView(new View(ApplicationLoader.applicationContext) { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable.1
                @Override // android.view.View
                public void invalidate() {
                    AnimatedEmojiDrawable.this.invalidate();
                }
            });
            imageLocation = ImageLocation.getForDocument(this.document);
            str = str3 + "_" + ImageLoader.AUTOPLAY_FILTER;
            tLRPC$PhotoSize = null;
            svgDrawable = null;
        } else {
            StringBuilder sb = new StringBuilder();
            if (this.cacheType != 0) {
                str2 = this.cacheType + "_";
            } else {
                str2 = "";
            }
            sb.append(str2);
            sb.append(this.documentId);
            sb.append("@");
            sb.append(sizedp);
            sb.append("_");
            sb.append(sizedp);
            sb.append("_pcache");
            String sb2 = sb.toString();
            if (this.cacheType == 1 || !ImageLoader.getInstance().hasLottieMemCache(sb2)) {
                svgDrawable2 = DocumentObject.getSvgThumb(this.document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
                if (svgDrawable2 != null) {
                    svgDrawable2.overrideWidthAndHeight(512, 512);
                }
            } else {
                svgDrawable2 = null;
            }
            tLRPC$PhotoSize = FileLoader.getClosestPhotoSizeWithSize(this.document.thumbs, 90);
            svgDrawable = svgDrawable2;
            str = sizedp + "_" + sizedp + "_pcache";
            imageLocation = ImageLocation.getForDocument(this.document);
        }
        ImageLocation imageLocation2 = z ? null : imageLocation;
        TLRPC$Document tLRPC$Document = this.document;
        this.imageReceiver.setImage(imageLocation2, str, ImageLocation.getForDocument(tLRPC$PhotoSize, this.document), sizedp + "_" + sizedp, null, null, svgDrawable, tLRPC$Document.size, null, tLRPC$Document, 1);
        if (this.cacheType == 2) {
            this.imageReceiver.setLayerNum(7);
        }
        this.imageReceiver.setAspectFit(true);
        this.imageReceiver.setAllowStartLottieAnimation(true);
        this.imageReceiver.setAllowStartAnimation(true);
        this.imageReceiver.setAutoRepeat(1);
        this.imageReceiver.setAllowDecodeSingleFrame(true);
        updateAttachState();
    }

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
                AnimatedEmojiSpan.AnimatedEmojiHolder animatedEmojiHolder = this.holders.get(i2);
                if (animatedEmojiHolder != null) {
                    animatedEmojiHolder.invalidate();
                }
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        drawDebugCacheType(canvas);
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            emojiImageReceiver.setImageCoords(getBounds());
            this.imageReceiver.setAlpha(this.alpha);
            this.imageReceiver.draw(canvas);
        }
        drawPlaceholder(canvas, getBounds().centerX(), getBounds().centerY(), getBounds().width() / 2.0f);
    }

    public void draw(Canvas canvas, android.graphics.Rect rect, float f) {
        drawDebugCacheType(canvas);
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            emojiImageReceiver.setImageCoords(rect);
            this.imageReceiver.setAlpha(f);
            this.imageReceiver.draw(canvas);
        }
        if (rect != null) {
            drawPlaceholder(canvas, rect.centerX(), rect.centerY(), rect.width() / 2.0f);
        }
    }

    public void draw(Canvas canvas, ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
        drawDebugCacheType(canvas);
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            emojiImageReceiver.setAlpha(this.alpha);
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

    public void addView(AnimatedEmojiSpan.AnimatedEmojiHolder animatedEmojiHolder) {
        if (this.holders == null) {
            this.holders = new ArrayList<>(10);
        }
        if (!this.holders.contains(animatedEmojiHolder)) {
            this.holders.add(animatedEmojiHolder);
        }
        updateAttachState();
    }

    public void removeView(AnimatedEmojiSpan.AnimatedEmojiHolder animatedEmojiHolder) {
        ArrayList<AnimatedEmojiSpan.AnimatedEmojiHolder> arrayList = this.holders;
        if (arrayList != null) {
            arrayList.remove(animatedEmojiHolder);
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
        ArrayList<AnimatedEmojiSpan.AnimatedEmojiHolder> arrayList;
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
            count++;
            this.imageReceiver.onAttachedToWindow();
            return;
        }
        count--;
        this.imageReceiver.onDetachedFromWindow();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        float f = i / 255.0f;
        this.alpha = f;
        EmojiImageReceiver emojiImageReceiver = this.imageReceiver;
        if (emojiImageReceiver != null) {
            emojiImageReceiver.setAlpha(f);
        }
    }

    public EmojiImageReceiver getImageReceiver() {
        return this.imageReceiver;
    }

    /* loaded from: classes3.dex */
    public static class EmojiImageReceiver extends ImageReceiver {
        private AnimatedFloat crossfadeAlpha;
        private AnimatedFloat crossfadeAlphaForced;
        private boolean forcedThumb = false;
        private Runnable invalidate;
        private AnimatedFloat usedCrossfade;

        public EmojiImageReceiver(Runnable runnable) {
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiImageReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiImageReceiver.this.invalidate();
                }
            };
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            this.crossfadeAlpha = new AnimatedFloat(runnable2, 200L, cubicBezierInterpolator);
            this.crossfadeAlphaForced = new AnimatedFloat(new Runnable() { // from class: org.telegram.ui.Components.AnimatedEmojiDrawable$EmojiImageReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedEmojiDrawable.EmojiImageReceiver.this.invalidate();
                }
            }, 200L, cubicBezierInterpolator);
            this.usedCrossfade = this.crossfadeAlpha;
            this.invalidate = runnable;
        }

        @Override // org.telegram.messenger.ImageReceiver
        public void invalidate() {
            super.invalidate();
            Runnable runnable = this.invalidate;
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // org.telegram.messenger.ImageReceiver
        public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
            Runnable runnable = this.invalidate;
            if (runnable != null) {
                runnable.run();
            }
            return super.setImageBitmapByKey(drawable, str, i, z, i2);
        }

        public void forceThumb(boolean z) {
            AnimatedFloat animatedFloat = this.crossfadeAlphaForced;
            this.usedCrossfade = animatedFloat;
            this.forcedThumb = z;
            animatedFloat.set(z ? 0.0f : 1.0f, z);
        }

        public void switchBackAnimator() {
            this.usedCrossfade = this.crossfadeAlpha;
        }

        @Override // org.telegram.messenger.ImageReceiver
        protected boolean customDraw(Canvas canvas, AnimatedFileDrawable animatedFileDrawable, RLottieDrawable rLottieDrawable, Drawable drawable, Shader shader, Drawable drawable2, Shader shader2, Drawable drawable3, Shader shader3, boolean z, boolean z2, Drawable drawable4, BitmapShader bitmapShader, Drawable drawable5, float f, float f2, float f3, int[] iArr, ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
            boolean z3 = rLottieDrawable == null || !rLottieDrawable.hasBitmap() || (this.forcedThumb && this.usedCrossfade == this.crossfadeAlphaForced);
            float f4 = this.usedCrossfade.set(z3 ? 0.0f : 1.0f, z3);
            if (drawable5 != null && f4 < 1.0f) {
                drawDrawable(canvas, drawable5, (int) ((1.0f - f4) * 255.0f * f3), null, this.imageOrientation, 0, backgroundThreadDrawHolder);
            }
            if (rLottieDrawable != null && rLottieDrawable.hasBitmap() && f4 > 0.0f) {
                drawDrawable(canvas, rLottieDrawable, (int) (f4 * 255.0f * f3), null, this.imageOrientation, 0, backgroundThreadDrawHolder);
            }
            return true;
        }
    }
}
