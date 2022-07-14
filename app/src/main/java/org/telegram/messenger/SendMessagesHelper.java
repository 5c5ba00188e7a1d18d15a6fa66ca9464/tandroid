package org.telegram.messenger;

import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaCodecInfo;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.collection.LongSparseArray;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.actions.SearchIntents;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import org.telegram.ui.GroupCallActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.TwoStepVerificationActivity;
import org.telegram.ui.TwoStepVerificationSetupActivity;
/* loaded from: classes.dex */
public class SendMessagesHelper extends BaseController implements NotificationCenter.NotificationCenterDelegate {
    private static final int ERROR_TYPE_FILE_TOO_LARGE = 2;
    private static final int ERROR_TYPE_UNSUPPORTED = 1;
    private static volatile SendMessagesHelper[] Instance;
    private static DispatchQueue mediaSendQueue = new DispatchQueue("mediaSendQueue");
    private static ThreadPoolExecutor mediaSendThreadPool;
    private HashMap<String, ArrayList<DelayedMessage>> delayedMessages = new HashMap<>();
    private SparseArray<MessageObject> unsentMessages = new SparseArray<>();
    private SparseArray<TLRPC.Message> sendingMessages = new SparseArray<>();
    private SparseArray<TLRPC.Message> editingMessages = new SparseArray<>();
    private SparseArray<TLRPC.Message> uploadMessages = new SparseArray<>();
    private LongSparseArray<Integer> sendingMessagesIdDialogs = new LongSparseArray<>();
    private LongSparseArray<Integer> uploadingMessagesIdDialogs = new LongSparseArray<>();
    private HashMap<String, MessageObject> waitingForLocation = new HashMap<>();
    private HashMap<String, Boolean> waitingForCallback = new HashMap<>();
    private HashMap<String, byte[]> waitingForVote = new HashMap<>();
    private LongSparseArray<Long> voteSendTime = new LongSparseArray<>();
    private HashMap<String, ImportingHistory> importingHistoryFiles = new HashMap<>();
    private LongSparseArray<ImportingHistory> importingHistoryMap = new LongSparseArray<>();
    private HashMap<String, ImportingStickers> importingStickersFiles = new HashMap<>();
    private HashMap<String, ImportingStickers> importingStickersMap = new HashMap<>();
    private LocationProvider locationProvider = new LocationProvider(new LocationProvider.LocationProviderDelegate() { // from class: org.telegram.messenger.SendMessagesHelper.1
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onLocationAcquired(Location location) {
            SendMessagesHelper.this.sendLocation(location);
            SendMessagesHelper.this.waitingForLocation.clear();
        }

        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onUnableLocationAcquire() {
            HashMap<String, MessageObject> waitingForLocationCopy = new HashMap<>(SendMessagesHelper.this.waitingForLocation);
            SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.wasUnableToFindCurrentLocation, waitingForLocationCopy);
            SendMessagesHelper.this.waitingForLocation.clear();
        }
    });

    /* loaded from: classes4.dex */
    public static class SendingMediaInfo {
        public boolean canDeleteAfter;
        public String caption;
        public ArrayList<TLRPC.MessageEntity> entities;
        public boolean forceImage;
        public TLRPC.BotInlineResult inlineResult;
        public boolean isVideo;
        public ArrayList<TLRPC.InputDocument> masks;
        public String paintPath;
        public HashMap<String, String> params;
        public String path;
        public MediaController.SearchImage searchImage;
        public String thumbPath;
        public int ttl;
        public Uri uri;
        public VideoEditedInfo videoEditedInfo;
    }

    /* loaded from: classes4.dex */
    public class ImportingHistory {
        public long dialogId;
        public double estimatedUploadSpeed;
        public String historyPath;
        public long importId;
        private long lastUploadSize;
        private long lastUploadTime;
        public TLRPC.InputPeer peer;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public ArrayList<Uri> mediaPaths = new ArrayList<>();
        public HashSet<String> uploadSet = new HashSet<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<String> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = Integer.MAX_VALUE;

        public ImportingHistory() {
            SendMessagesHelper.this = this$0;
        }

        public void initImport(TLRPC.InputFile inputFile) {
            TLRPC.TL_messages_initHistoryImport req = new TLRPC.TL_messages_initHistoryImport();
            req.file = inputFile;
            req.media_count = this.mediaPaths.size();
            req.peer = this.peer;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(req, new AnonymousClass1(req), 2);
        }

        /* renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$1 */
        /* loaded from: classes4.dex */
        public class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_initHistoryImport val$req;

            AnonymousClass1(TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport) {
                ImportingHistory.this = this$1;
                this.val$req = tL_messages_initHistoryImport;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject response, final TLRPC.TL_error error) {
                final TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.AnonymousClass1.this.m1223x6fa5ea93(response, tL_messages_initHistoryImport, error);
                    }
                });
            }

            /* renamed from: lambda$run$0$org-telegram-messenger-SendMessagesHelper$ImportingHistory$1 */
            public /* synthetic */ void m1223x6fa5ea93(TLObject response, TLRPC.TL_messages_initHistoryImport req, TLRPC.TL_error error) {
                if (!(response instanceof TLRPC.TL_messages_historyImport)) {
                    SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), req, error);
                    return;
                }
                ImportingHistory.this.importId = ((TLRPC.TL_messages_historyImport) response).id;
                ImportingHistory.this.uploadSet.remove(ImportingHistory.this.historyPath);
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                if (ImportingHistory.this.uploadSet.isEmpty()) {
                    ImportingHistory.this.startImport();
                }
                ImportingHistory.this.lastUploadTime = SystemClock.elapsedRealtime();
                int N = ImportingHistory.this.uploadMedia.size();
                for (int a = 0; a < N; a++) {
                    SendMessagesHelper.this.getFileLoader().uploadFile(ImportingHistory.this.uploadMedia.get(a), false, true, ConnectionsManager.FileTypeFile);
                }
            }
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        public void onFileFailedToUpload(String path) {
            if (path.equals(this.historyPath)) {
                SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
                TLRPC.TL_error error = new TLRPC.TL_error();
                error.code = 400;
                error.text = "IMPORT_UPLOAD_FAILED";
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId), new TLRPC.TL_messages_initHistoryImport(), error);
                return;
            }
            this.uploadSet.remove(path);
        }

        public void addUploadProgress(String path, long sz, float progress) {
            this.uploadProgresses.put(path, Float.valueOf(progress));
            this.uploadSize.put(path, Long.valueOf(sz));
            this.uploadedSize = 0L;
            for (Map.Entry<String, Long> entry : this.uploadSize.entrySet()) {
                this.uploadedSize += entry.getValue().longValue();
            }
            long newTime = SystemClock.elapsedRealtime();
            if (!path.equals(this.historyPath)) {
                long j = this.uploadedSize;
                long j2 = this.lastUploadSize;
                if (j != j2) {
                    long j3 = this.lastUploadTime;
                    if (newTime != j3) {
                        double d = newTime - j3;
                        Double.isNaN(d);
                        double dt = d / 1000.0d;
                        double d2 = j - j2;
                        Double.isNaN(d2);
                        double uploadSpeed = d2 / dt;
                        double d3 = this.estimatedUploadSpeed;
                        if (d3 != FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE) {
                            this.estimatedUploadSpeed = (0.01d * uploadSpeed) + ((1.0d - 0.01d) * d3);
                        } else {
                            this.estimatedUploadSpeed = uploadSpeed;
                        }
                        double d4 = (this.totalSize - j) * 1000;
                        double d5 = this.estimatedUploadSpeed;
                        Double.isNaN(d4);
                        this.timeUntilFinish = (int) (d4 / d5);
                        this.lastUploadSize = j;
                        this.lastUploadTime = newTime;
                    }
                }
            }
            float pr = ((float) getUploadedCount()) / ((float) getTotalCount());
            int newProgress = (int) (100.0f * pr);
            if (this.uploadProgress != newProgress) {
                this.uploadProgress = newProgress;
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
            }
        }

        public void onMediaImport(String path, long size, TLRPC.InputFile inputFile) {
            addUploadProgress(path, size, 1.0f);
            TLRPC.TL_messages_uploadImportedMedia req = new TLRPC.TL_messages_uploadImportedMedia();
            req.peer = this.peer;
            req.import_id = this.importId;
            req.file_name = new File(path).getName();
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            String ext = "txt";
            int idx = req.file_name.lastIndexOf(46);
            if (idx != -1) {
                ext = req.file_name.substring(idx + 1).toLowerCase();
            }
            String mimeType = myMime.getMimeTypeFromExtension(ext);
            if (mimeType == null) {
                if ("opus".equals(ext)) {
                    mimeType = MimeTypes.AUDIO_OPUS;
                } else if ("webp".equals(ext)) {
                    mimeType = "image/webp";
                } else {
                    mimeType = ErrorAttachmentLog.CONTENT_TYPE_TEXT_PLAIN;
                }
            }
            if (mimeType.equals("image/jpg") || mimeType.equals("image/jpeg")) {
                TLRPC.TL_inputMediaUploadedPhoto inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                inputMediaUploadedPhoto.file = inputFile;
                req.media = inputMediaUploadedPhoto;
            } else {
                TLRPC.TL_inputMediaUploadedDocument inputMediaDocument = new TLRPC.TL_inputMediaUploadedDocument();
                inputMediaDocument.file = inputFile;
                inputMediaDocument.mime_type = mimeType;
                req.media = inputMediaDocument;
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(req, new AnonymousClass2(path), 2);
        }

        /* renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$2 */
        /* loaded from: classes4.dex */
        public class AnonymousClass2 implements RequestDelegate {
            final /* synthetic */ String val$path;

            AnonymousClass2(String str) {
                ImportingHistory.this = this$1;
                this.val$path = str;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject response, TLRPC.TL_error error) {
                final String str = this.val$path;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.AnonymousClass2.this.m1224x6fa5ea94(str);
                    }
                });
            }

            /* renamed from: lambda$run$0$org-telegram-messenger-SendMessagesHelper$ImportingHistory$2 */
            public /* synthetic */ void m1224x6fa5ea94(String path) {
                ImportingHistory.this.uploadSet.remove(path);
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                if (ImportingHistory.this.uploadSet.isEmpty()) {
                    ImportingHistory.this.startImport();
                }
            }
        }

        public void startImport() {
            TLRPC.TL_messages_startHistoryImport req = new TLRPC.TL_messages_startHistoryImport();
            req.peer = this.peer;
            req.import_id = this.importId;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(req, new AnonymousClass3(req));
        }

        /* renamed from: org.telegram.messenger.SendMessagesHelper$ImportingHistory$3 */
        /* loaded from: classes4.dex */
        public class AnonymousClass3 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_startHistoryImport val$req;

            AnonymousClass3(TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport) {
                ImportingHistory.this = this$1;
                this.val$req = tL_messages_startHistoryImport;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject response, final TLRPC.TL_error error) {
                final TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.AnonymousClass3.this.m1225x6fa5ea95(error, tL_messages_startHistoryImport);
                    }
                });
            }

            /* renamed from: lambda$run$0$org-telegram-messenger-SendMessagesHelper$ImportingHistory$3 */
            public /* synthetic */ void m1225x6fa5ea95(TLRPC.TL_error error, TLRPC.TL_messages_startHistoryImport req) {
                SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                if (error == null) {
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                } else {
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), req, error);
                }
            }
        }

        public void setImportProgress(int value) {
            if (value == 100) {
                SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
            }
            SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
        }
    }

    /* loaded from: classes4.dex */
    public static class ImportingSticker {
        public boolean animated;
        public String emoji;
        public TLRPC.TL_inputStickerSetItem item;
        public String mimeType;
        public String path;
        public boolean validated;

        public void uploadMedia(int account, TLRPC.InputFile inputFile, Runnable onFinish) {
            TLRPC.TL_messages_uploadMedia req = new TLRPC.TL_messages_uploadMedia();
            req.peer = new TLRPC.TL_inputPeerSelf();
            req.media = new TLRPC.TL_inputMediaUploadedDocument();
            req.media.file = inputFile;
            req.media.mime_type = this.mimeType;
            ConnectionsManager.getInstance(account).sendRequest(req, new AnonymousClass1(onFinish), 2);
        }

        /* renamed from: org.telegram.messenger.SendMessagesHelper$ImportingSticker$1 */
        /* loaded from: classes4.dex */
        public class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ Runnable val$onFinish;

            AnonymousClass1(Runnable runnable) {
                ImportingSticker.this = this$0;
                this.val$onFinish = runnable;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject response, TLRPC.TL_error error) {
                final Runnable runnable = this.val$onFinish;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingSticker$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingSticker.AnonymousClass1.this.m1226x170488fc(response, runnable);
                    }
                });
            }

            /* renamed from: lambda$run$0$org-telegram-messenger-SendMessagesHelper$ImportingSticker$1 */
            public /* synthetic */ void m1226x170488fc(TLObject response, Runnable onFinish) {
                if (response instanceof TLRPC.TL_messageMediaDocument) {
                    TLRPC.TL_messageMediaDocument mediaDocument = (TLRPC.TL_messageMediaDocument) response;
                    ImportingSticker.this.item = new TLRPC.TL_inputStickerSetItem();
                    ImportingSticker.this.item.document = new TLRPC.TL_inputDocument();
                    ImportingSticker.this.item.document.id = mediaDocument.document.id;
                    ImportingSticker.this.item.document.access_hash = mediaDocument.document.access_hash;
                    ImportingSticker.this.item.document.file_reference = mediaDocument.document.file_reference;
                    ImportingSticker.this.item.emoji = ImportingSticker.this.emoji != null ? ImportingSticker.this.emoji : "";
                    ImportingSticker.this.mimeType = mediaDocument.document.mime_type;
                } else if (ImportingSticker.this.animated) {
                    ImportingSticker.this.mimeType = "application/x-bad-tgsticker";
                }
                onFinish.run();
            }
        }
    }

    /* loaded from: classes4.dex */
    public class ImportingStickers {
        public double estimatedUploadSpeed;
        private long lastUploadSize;
        private long lastUploadTime;
        public String shortName;
        public String software;
        public String title;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public HashMap<String, ImportingSticker> uploadSet = new HashMap<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<ImportingSticker> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = Integer.MAX_VALUE;

        public ImportingStickers() {
            SendMessagesHelper.this = this$0;
        }

        public void initImport() {
            SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, this.shortName);
            this.lastUploadTime = SystemClock.elapsedRealtime();
            int N = this.uploadMedia.size();
            for (int a = 0; a < N; a++) {
                SendMessagesHelper.this.getFileLoader().uploadFile(this.uploadMedia.get(a).path, false, true, ConnectionsManager.FileTypeFile);
            }
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        public void onFileFailedToUpload(String path) {
            ImportingSticker file = this.uploadSet.remove(path);
            if (file != null) {
                this.uploadMedia.remove(file);
            }
        }

        public void addUploadProgress(String path, long sz, float progress) {
            this.uploadProgresses.put(path, Float.valueOf(progress));
            this.uploadSize.put(path, Long.valueOf(sz));
            this.uploadedSize = 0L;
            for (Map.Entry<String, Long> entry : this.uploadSize.entrySet()) {
                this.uploadedSize += entry.getValue().longValue();
            }
            long newTime = SystemClock.elapsedRealtime();
            long j = this.uploadedSize;
            long j2 = this.lastUploadSize;
            if (j != j2) {
                long j3 = this.lastUploadTime;
                if (newTime != j3) {
                    double d = newTime - j3;
                    Double.isNaN(d);
                    double dt = d / 1000.0d;
                    double d2 = j - j2;
                    Double.isNaN(d2);
                    double uploadSpeed = d2 / dt;
                    double d3 = this.estimatedUploadSpeed;
                    if (d3 != FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE) {
                        this.estimatedUploadSpeed = (0.01d * uploadSpeed) + ((1.0d - 0.01d) * d3);
                    } else {
                        this.estimatedUploadSpeed = uploadSpeed;
                    }
                    double d4 = (this.totalSize - j) * 1000;
                    double d5 = this.estimatedUploadSpeed;
                    Double.isNaN(d4);
                    this.timeUntilFinish = (int) (d4 / d5);
                    this.lastUploadSize = j;
                    this.lastUploadTime = newTime;
                }
            }
            float pr = ((float) getUploadedCount()) / ((float) getTotalCount());
            int newProgress = (int) (100.0f * pr);
            if (this.uploadProgress != newProgress) {
                this.uploadProgress = newProgress;
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, this.shortName);
            }
        }

        public void onMediaImport(final String path, long size, TLRPC.InputFile inputFile) {
            addUploadProgress(path, size, 1.0f);
            ImportingSticker file = this.uploadSet.get(path);
            if (file == null) {
                return;
            }
            file.uploadMedia(SendMessagesHelper.this.currentAccount, inputFile, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.ImportingStickers.this.m1227xb92aa0e3(path);
                }
            });
        }

        /* renamed from: lambda$onMediaImport$0$org-telegram-messenger-SendMessagesHelper$ImportingStickers */
        public /* synthetic */ void m1227xb92aa0e3(String path) {
            this.uploadSet.remove(path);
            SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, this.shortName);
            if (this.uploadSet.isEmpty()) {
                startImport();
            }
        }

        public void startImport() {
            TLRPC.TL_stickers_createStickerSet req = new TLRPC.TL_stickers_createStickerSet();
            req.user_id = new TLRPC.TL_inputUserSelf();
            req.title = this.title;
            req.short_name = this.shortName;
            req.animated = this.uploadMedia.get(0).animated;
            String str = this.software;
            if (str != null) {
                req.software = str;
                req.flags |= 8;
            }
            int N = this.uploadMedia.size();
            for (int a = 0; a < N; a++) {
                ImportingSticker file = this.uploadMedia.get(a);
                if (file.item != null) {
                    req.stickers.add(file.item);
                }
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(req, new AnonymousClass1(req));
        }

        /* renamed from: org.telegram.messenger.SendMessagesHelper$ImportingStickers$1 */
        /* loaded from: classes4.dex */
        public class AnonymousClass1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_stickers_createStickerSet val$req;

            AnonymousClass1(TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet) {
                ImportingStickers.this = this$1;
                this.val$req = tL_stickers_createStickerSet;
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject response, final TLRPC.TL_error error) {
                final TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingStickers.AnonymousClass1.this.m1228xc98dbdb1(error, tL_stickers_createStickerSet, response);
                    }
                });
            }

            /* renamed from: lambda$run$0$org-telegram-messenger-SendMessagesHelper$ImportingStickers$1 */
            public /* synthetic */ void m1228xc98dbdb1(TLRPC.TL_error error, TLRPC.TL_stickers_createStickerSet req, TLObject response) {
                SendMessagesHelper.this.importingStickersMap.remove(ImportingStickers.this.shortName);
                if (error == null) {
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName);
                } else {
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName, req, error);
                }
                if (response instanceof TLRPC.TL_messages_stickerSet) {
                    if (SendMessagesHelper.this.getNotificationCenter().hasObservers(NotificationCenter.stickersImportComplete)) {
                        SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportComplete, response);
                    } else {
                        SendMessagesHelper.this.getMediaDataController().toggleStickerSet(null, response, 2, null, false, false);
                    }
                }
            }
        }

        public void setImportProgress(int value) {
            if (value == 100) {
                SendMessagesHelper.this.importingStickersMap.remove(this.shortName);
            }
            SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.stickersImportProgressChanged, this.shortName);
        }
    }

    static {
        int cores;
        if (Build.VERSION.SDK_INT >= 17) {
            cores = Runtime.getRuntime().availableProcessors();
        } else {
            cores = 2;
        }
        mediaSendThreadPool = new ThreadPoolExecutor(cores, cores, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Instance = new SendMessagesHelper[4];
    }

    /* loaded from: classes4.dex */
    public static class MediaSendPrepareWorker {
        public volatile String parentObject;
        public volatile TLRPC.TL_photo photo;
        public CountDownLatch sync;

        private MediaSendPrepareWorker() {
        }
    }

    /* loaded from: classes4.dex */
    public static class LocationProvider {
        private LocationProviderDelegate delegate;
        private Location lastKnownLocation;
        private LocationManager locationManager;
        private Runnable locationQueryCancelRunnable;
        private GpsLocationListener gpsLocationListener = new GpsLocationListener();
        private GpsLocationListener networkLocationListener = new GpsLocationListener();

        /* loaded from: classes4.dex */
        public interface LocationProviderDelegate {
            void onLocationAcquired(Location location);

            void onUnableLocationAcquire();
        }

        /* loaded from: classes4.dex */
        public class GpsLocationListener implements LocationListener {
            private GpsLocationListener() {
                LocationProvider.this = r1;
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                if (location == null || LocationProvider.this.locationQueryCancelRunnable == null) {
                    return;
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("found location " + location);
                }
                LocationProvider.this.lastKnownLocation = location;
                if (location.getAccuracy() < 100.0f) {
                    if (LocationProvider.this.delegate != null) {
                        LocationProvider.this.delegate.onLocationAcquired(location);
                    }
                    if (LocationProvider.this.locationQueryCancelRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                    }
                    LocationProvider.this.cleanup();
                }
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String provider) {
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String provider) {
            }
        }

        public LocationProvider() {
        }

        public LocationProvider(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        public void setDelegate(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        public void cleanup() {
            this.locationManager.removeUpdates(this.gpsLocationListener);
            this.locationManager.removeUpdates(this.networkLocationListener);
            this.lastKnownLocation = null;
            this.locationQueryCancelRunnable = null;
        }

        public void start() {
            if (this.locationManager == null) {
                this.locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            }
            try {
                this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
            } catch (Exception e) {
                FileLog.e(e);
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            try {
                Location lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                this.lastKnownLocation = lastKnownLocation;
                if (lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$LocationProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.LocationProvider.this.m1229x9940a2de();
                }
            };
            this.locationQueryCancelRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
        }

        /* renamed from: lambda$start$0$org-telegram-messenger-SendMessagesHelper$LocationProvider */
        public /* synthetic */ void m1229x9940a2de() {
            LocationProviderDelegate locationProviderDelegate = this.delegate;
            if (locationProviderDelegate != null) {
                Location location = this.lastKnownLocation;
                if (location != null) {
                    locationProviderDelegate.onLocationAcquired(location);
                } else {
                    locationProviderDelegate.onUnableLocationAcquire();
                }
            }
            cleanup();
        }

        public void stop() {
            if (this.locationManager == null) {
                return;
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            cleanup();
        }
    }

    /* loaded from: classes4.dex */
    public class DelayedMessageSendAfterRequest {
        public DelayedMessage delayedMessage;
        public MessageObject msgObj;
        public ArrayList<MessageObject> msgObjs;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public TLObject request;
        public boolean scheduled;

        protected DelayedMessageSendAfterRequest() {
            SendMessagesHelper.this = this$0;
        }
    }

    /* loaded from: classes4.dex */
    public class DelayedMessage {
        public TLRPC.EncryptedChat encryptedChat;
        public HashMap<Object, Object> extraHashMap;
        public int finalGroupMessage;
        public long groupId;
        public String httpLocation;
        public ArrayList<String> httpLocations;
        public ArrayList<TLRPC.InputMedia> inputMedias;
        public TLRPC.InputMedia inputUploadMedia;
        public TLObject locationParent;
        public ArrayList<TLRPC.PhotoSize> locations;
        public ArrayList<MessageObject> messageObjects;
        public ArrayList<TLRPC.Message> messages;
        public MessageObject obj;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public long peer;
        public boolean performMediaUpload;
        public TLRPC.PhotoSize photoSize;
        ArrayList<DelayedMessageSendAfterRequest> requests;
        public boolean retriedToSend;
        public boolean scheduled;
        public TLObject sendEncryptedRequest;
        public TLObject sendRequest;
        public int topMessageId;
        public int type;
        public VideoEditedInfo videoEditedInfo;
        public ArrayList<VideoEditedInfo> videoEditedInfos;

        public DelayedMessage(long peer) {
            SendMessagesHelper.this = this$0;
            this.peer = peer;
        }

        public void initForGroup(long id) {
            this.type = 4;
            this.groupId = id;
            this.messageObjects = new ArrayList<>();
            this.messages = new ArrayList<>();
            this.inputMedias = new ArrayList<>();
            this.originalPaths = new ArrayList<>();
            this.parentObjects = new ArrayList<>();
            this.extraHashMap = new HashMap<>();
            this.locations = new ArrayList<>();
            this.httpLocations = new ArrayList<>();
            this.videoEditedInfos = new ArrayList<>();
        }

        public void addDelayedRequest(TLObject req, MessageObject msgObj, String originalPath, Object parentObject, DelayedMessage delayedMessage, boolean scheduled) {
            DelayedMessageSendAfterRequest request = new DelayedMessageSendAfterRequest();
            request.request = req;
            request.msgObj = msgObj;
            request.originalPath = originalPath;
            request.delayedMessage = delayedMessage;
            request.parentObject = parentObject;
            request.scheduled = scheduled;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(request);
        }

        public void addDelayedRequest(TLObject req, ArrayList<MessageObject> msgObjs, ArrayList<String> originalPaths, ArrayList<Object> parentObjects, DelayedMessage delayedMessage, boolean scheduled) {
            DelayedMessageSendAfterRequest request = new DelayedMessageSendAfterRequest();
            request.request = req;
            request.msgObjs = msgObjs;
            request.originalPaths = originalPaths;
            request.delayedMessage = delayedMessage;
            request.parentObjects = parentObjects;
            request.scheduled = scheduled;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(request);
        }

        public void sendDelayedRequests() {
            ArrayList<DelayedMessageSendAfterRequest> arrayList = this.requests;
            if (arrayList != null) {
                int i = this.type;
                if (i != 4 && i != 0) {
                    return;
                }
                int size = arrayList.size();
                for (int a = 0; a < size; a++) {
                    DelayedMessageSendAfterRequest request = this.requests.get(a);
                    if (request.request instanceof TLRPC.TL_messages_sendEncryptedMultiMedia) {
                        SendMessagesHelper.this.getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) request.request, this);
                    } else if (!(request.request instanceof TLRPC.TL_messages_sendMultiMedia)) {
                        SendMessagesHelper.this.performSendMessageRequest(request.request, request.msgObj, request.originalPath, request.delayedMessage, request.parentObject, null, request.scheduled);
                    } else {
                        SendMessagesHelper.this.performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia) request.request, request.msgObjs, request.originalPaths, request.parentObjects, request.delayedMessage, request.scheduled);
                    }
                }
                this.requests = null;
            }
        }

        public void markAsError() {
            if (this.type == 4) {
                for (int a = 0; a < this.messageObjects.size(); a++) {
                    MessageObject obj = this.messageObjects.get(a);
                    SendMessagesHelper.this.getMessagesStorage().markMessageAsSendError(obj.messageOwner, obj.scheduled);
                    obj.messageOwner.send_state = 2;
                    SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(obj.getId()));
                    SendMessagesHelper.this.processSentMessage(obj.getId());
                    SendMessagesHelper.this.removeFromUploadingMessages(obj.getId(), this.scheduled);
                }
                HashMap hashMap = SendMessagesHelper.this.delayedMessages;
                hashMap.remove("group_" + this.groupId);
            } else {
                SendMessagesHelper.this.getMessagesStorage().markMessageAsSendError(this.obj.messageOwner, this.obj.scheduled);
                this.obj.messageOwner.send_state = 2;
                SendMessagesHelper.this.getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(this.obj.getId()));
                SendMessagesHelper.this.processSentMessage(this.obj.getId());
                SendMessagesHelper.this.removeFromUploadingMessages(this.obj.getId(), this.scheduled);
            }
            sendDelayedRequests();
        }
    }

    public static SendMessagesHelper getInstance(int num) {
        SendMessagesHelper localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (SendMessagesHelper.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    SendMessagesHelper[] sendMessagesHelperArr = Instance;
                    SendMessagesHelper sendMessagesHelper = new SendMessagesHelper(num);
                    localInstance = sendMessagesHelper;
                    sendMessagesHelperArr[num] = sendMessagesHelper;
                }
            }
        }
        return localInstance;
    }

    public SendMessagesHelper(int instance) {
        super(instance);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1162lambda$new$0$orgtelegrammessengerSendMessagesHelper();
            }
        });
    }

    /* renamed from: lambda$new$0$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1162lambda$new$0$orgtelegrammessengerSendMessagesHelper() {
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadProgressChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingStarted);
        getNotificationCenter().addObserver(this, NotificationCenter.fileNewChunkAvailable);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidFailedLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
    }

    public void cleanup() {
        this.delayedMessages.clear();
        this.unsentMessages.clear();
        this.sendingMessages.clear();
        this.editingMessages.clear();
        this.sendingMessagesIdDialogs.clear();
        this.uploadMessages.clear();
        this.uploadingMessagesIdDialogs.clear();
        this.waitingForLocation.clear();
        this.waitingForCallback.clear();
        this.waitingForVote.clear();
        this.importingHistoryFiles.clear();
        this.importingHistoryMap.clear();
        this.importingStickersFiles.clear();
        this.importingStickersMap.clear();
        this.locationProvider.stop();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        String path;
        ArrayList<DelayedMessage> arr;
        final MessageObject messageObject;
        int fileType;
        ArrayList<DelayedMessage> arr2;
        long availableSize;
        String finalPath;
        TLRPC.InputMedia media;
        ImportingStickers importingStickers;
        ImportingHistory importingHistory;
        TLRPC.InputFile file;
        ArrayList<DelayedMessage> arr3;
        String location;
        TLRPC.InputEncryptedFile encryptedFile;
        int a;
        TLRPC.InputEncryptedFile encryptedFile2;
        int a2;
        ArrayList<DelayedMessage> arr4;
        if (id == NotificationCenter.fileUploadProgressChanged) {
            String fileName = (String) args[0];
            ImportingHistory importingHistory2 = this.importingHistoryFiles.get(fileName);
            if (importingHistory2 != null) {
                Long loadedSize = (Long) args[1];
                Long totalSize = (Long) args[2];
                importingHistory2.addUploadProgress(fileName, loadedSize.longValue(), ((float) loadedSize.longValue()) / ((float) totalSize.longValue()));
            }
            ImportingStickers importingStickers2 = this.importingStickersFiles.get(fileName);
            if (importingStickers2 != null) {
                Long loadedSize2 = (Long) args[1];
                Long totalSize2 = (Long) args[2];
                importingStickers2.addUploadProgress(fileName, loadedSize2.longValue(), ((float) loadedSize2.longValue()) / ((float) totalSize2.longValue()));
            }
        } else if (id == NotificationCenter.fileUploaded) {
            String location2 = (String) args[0];
            TLRPC.InputFile file2 = (TLRPC.InputFile) args[1];
            TLRPC.InputEncryptedFile encryptedFile3 = (TLRPC.InputEncryptedFile) args[2];
            ImportingHistory importingHistory3 = this.importingHistoryFiles.get(location2);
            if (importingHistory3 != null) {
                if (location2.equals(importingHistory3.historyPath)) {
                    importingHistory3.initImport(file2);
                } else {
                    importingHistory3.onMediaImport(location2, ((Long) args[5]).longValue(), file2);
                }
            }
            ImportingStickers importingStickers3 = this.importingStickersFiles.get(location2);
            if (importingStickers3 != null) {
                importingStickers3.onMediaImport(location2, ((Long) args[5]).longValue(), file2);
            }
            ArrayList<DelayedMessage> arr5 = this.delayedMessages.get(location2);
            if (arr5 != null) {
                int a3 = 0;
                while (a3 < arr5.size()) {
                    DelayedMessage message = arr5.get(a3);
                    if (message.sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                        media = ((TLRPC.TL_messages_sendMedia) message.sendRequest).media;
                    } else if (message.sendRequest instanceof TLRPC.TL_messages_editMessage) {
                        media = ((TLRPC.TL_messages_editMessage) message.sendRequest).media;
                    } else if (!(message.sendRequest instanceof TLRPC.TL_messages_sendMultiMedia)) {
                        media = null;
                    } else {
                        media = (TLRPC.InputMedia) message.extraHashMap.get(location2);
                    }
                    if (file2 == null || media == null) {
                        ArrayList<DelayedMessage> arr6 = arr5;
                        importingStickers = importingStickers3;
                        importingHistory = importingHistory3;
                        file = file2;
                        location = location2;
                        a = a3;
                        encryptedFile = encryptedFile3;
                        if (encryptedFile == null || message.sendEncryptedRequest == null) {
                            arr3 = arr6;
                        } else {
                            TLRPC.TL_decryptedMessage decryptedMessage = null;
                            if (message.type == 4) {
                                TLRPC.TL_messages_sendEncryptedMultiMedia req = (TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                                TLRPC.InputEncryptedFile inputEncryptedFile = (TLRPC.InputEncryptedFile) message.extraHashMap.get(location);
                                int index = req.files.indexOf(inputEncryptedFile);
                                if (index < 0) {
                                    arr3 = arr6;
                                } else {
                                    req.files.set(index, encryptedFile);
                                    arr3 = arr6;
                                    if (inputEncryptedFile.id == 1) {
                                        MessageObject messageObject2 = (MessageObject) message.extraHashMap.get(location + "_i");
                                        message.photoSize = (TLRPC.PhotoSize) message.extraHashMap.get(location + "_t");
                                        stopVideoService(message.messageObjects.get(index).messageOwner.attachPath);
                                    }
                                    TLRPC.TL_decryptedMessage decryptedMessage2 = req.messages.get(index);
                                    decryptedMessage = decryptedMessage2;
                                }
                            } else {
                                arr3 = arr6;
                                decryptedMessage = (TLRPC.TL_decryptedMessage) message.sendEncryptedRequest;
                            }
                            if (decryptedMessage != null) {
                                if ((decryptedMessage.media instanceof TLRPC.TL_decryptedMessageMediaVideo) || (decryptedMessage.media instanceof TLRPC.TL_decryptedMessageMediaPhoto) || (decryptedMessage.media instanceof TLRPC.TL_decryptedMessageMediaDocument)) {
                                    long size = ((Long) args[5]).longValue();
                                    decryptedMessage.media.size = (int) size;
                                }
                                decryptedMessage.media.key = (byte[]) args[3];
                                decryptedMessage.media.iv = (byte[]) args[4];
                                if (message.type == 4) {
                                    uploadMultiMedia(message, null, encryptedFile, location);
                                } else {
                                    getSecretChatHelper().performSendEncryptedRequest(decryptedMessage, message.obj.messageOwner, message.encryptedChat, encryptedFile, message.originalPath, message.obj);
                                }
                            }
                            arr3.remove(a);
                            a--;
                        }
                    } else {
                        if (message.type == 0) {
                            media.file = file2;
                            a2 = a3;
                            encryptedFile2 = encryptedFile3;
                            importingHistory = importingHistory3;
                            importingStickers = importingStickers3;
                            arr4 = arr5;
                            performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, message, true, null, message.parentObject, null, message.scheduled);
                            file = file2;
                            location = location2;
                        } else {
                            a2 = a3;
                            arr4 = arr5;
                            importingStickers = importingStickers3;
                            importingHistory = importingHistory3;
                            encryptedFile2 = encryptedFile3;
                            TLRPC.InputFile file3 = file2;
                            String location3 = location2;
                            TLRPC.InputMedia media2 = media;
                            if (message.type != 1) {
                                if (message.type == 2) {
                                    if (media2.file == null) {
                                        media2.file = file3;
                                        if (media2.thumb != null || message.photoSize == null || message.photoSize.location == null) {
                                            file = file3;
                                            performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, null, message.parentObject, null, message.scheduled);
                                            location = location3;
                                        } else {
                                            performSendDelayedMessage(message);
                                            file = file3;
                                            location = location3;
                                        }
                                    } else {
                                        file = file3;
                                        media2.thumb = file;
                                        media2.flags |= 4;
                                        performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, null, message.parentObject, null, message.scheduled);
                                        location = location3;
                                    }
                                } else {
                                    file = file3;
                                    if (message.type == 3) {
                                        media2.file = file;
                                        performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, null, message.parentObject, null, message.scheduled);
                                        location = location3;
                                    } else if (message.type != 4) {
                                        location = location3;
                                    } else if (media2 instanceof TLRPC.TL_inputMediaUploadedDocument) {
                                        if (media2.file == null) {
                                            media2.file = file;
                                            HashMap<Object, Object> hashMap = message.extraHashMap;
                                            StringBuilder sb = new StringBuilder();
                                            location = location3;
                                            sb.append(location);
                                            sb.append("_i");
                                            int index2 = message.messageObjects.indexOf((MessageObject) hashMap.get(sb.toString()));
                                            if (index2 >= 0) {
                                                stopVideoService(message.messageObjects.get(index2).messageOwner.attachPath);
                                            }
                                            message.photoSize = (TLRPC.PhotoSize) message.extraHashMap.get(location + "_t");
                                            if (media2.thumb != null || message.photoSize == null || message.photoSize.location == null) {
                                                uploadMultiMedia(message, media2, null, location);
                                            } else {
                                                message.performMediaUpload = true;
                                                performSendDelayedMessage(message, index2);
                                            }
                                        } else {
                                            location = location3;
                                            media2.thumb = file;
                                            media2.flags |= 4;
                                            uploadMultiMedia(message, media2, null, (String) message.extraHashMap.get(location + "_o"));
                                        }
                                    } else {
                                        location = location3;
                                        media2.file = file;
                                        uploadMultiMedia(message, media2, null, location);
                                    }
                                }
                            } else if (media2.file == null) {
                                media2.file = file3;
                                if (media2.thumb != null || message.photoSize == null || message.photoSize.location == null) {
                                    performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, null, message.parentObject, null, message.scheduled);
                                    file = file3;
                                    location = location3;
                                } else {
                                    performSendDelayedMessage(message);
                                    file = file3;
                                    location = location3;
                                }
                            } else {
                                media2.thumb = file3;
                                media2.flags |= 4;
                                performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, null, message.parentObject, null, message.scheduled);
                                file = file3;
                                location = location3;
                            }
                        }
                        int a4 = a2;
                        arr4.remove(a4);
                        a = a4 - 1;
                        arr3 = arr4;
                        encryptedFile = encryptedFile2;
                    }
                    encryptedFile3 = encryptedFile;
                    file2 = file;
                    importingHistory3 = importingHistory;
                    importingStickers3 = importingStickers;
                    a3 = a + 1;
                    ArrayList<DelayedMessage> arrayList = arr3;
                    location2 = location;
                    arr5 = arrayList;
                }
                String location4 = location2;
                if (arr5.isEmpty()) {
                    this.delayedMessages.remove(location4);
                }
            }
        } else if (id == NotificationCenter.fileUploadFailed) {
            String location5 = (String) args[0];
            boolean enc = ((Boolean) args[1]).booleanValue();
            ImportingHistory importingHistory4 = this.importingHistoryFiles.get(location5);
            if (importingHistory4 != null) {
                importingHistory4.onFileFailedToUpload(location5);
            }
            ImportingStickers importingStickers4 = this.importingStickersFiles.get(location5);
            if (importingStickers4 != null) {
                importingStickers4.onFileFailedToUpload(location5);
            }
            ArrayList<DelayedMessage> arr7 = this.delayedMessages.get(location5);
            if (arr7 != null) {
                int a5 = 0;
                while (a5 < arr7.size()) {
                    DelayedMessage obj = arr7.get(a5);
                    if ((enc && obj.sendEncryptedRequest != null) || (!enc && obj.sendRequest != null)) {
                        obj.markAsError();
                        arr7.remove(a5);
                        a5--;
                    }
                    a5++;
                }
                if (arr7.isEmpty()) {
                    this.delayedMessages.remove(location5);
                }
            }
        } else if (id == NotificationCenter.filePreparingStarted) {
            MessageObject messageObject3 = (MessageObject) args[0];
            if (messageObject3.getId() == 0) {
                return;
            }
            String str = (String) args[1];
            ArrayList<DelayedMessage> arr8 = this.delayedMessages.get(messageObject3.messageOwner.attachPath);
            if (arr8 != null) {
                int a6 = 0;
                while (true) {
                    if (a6 >= arr8.size()) {
                        break;
                    }
                    DelayedMessage message2 = arr8.get(a6);
                    if (message2.type == 4) {
                        int index3 = message2.messageObjects.indexOf(messageObject3);
                        message2.photoSize = (TLRPC.PhotoSize) message2.extraHashMap.get(messageObject3.messageOwner.attachPath + "_t");
                        message2.performMediaUpload = true;
                        performSendDelayedMessage(message2, index3);
                        arr8.remove(a6);
                        break;
                    } else if (message2.obj != messageObject3) {
                        a6++;
                    } else {
                        message2.videoEditedInfo = null;
                        performSendDelayedMessage(message2);
                        arr8.remove(a6);
                        break;
                    }
                }
                if (arr8.isEmpty()) {
                    this.delayedMessages.remove(messageObject3.messageOwner.attachPath);
                }
            }
        } else if (id == NotificationCenter.fileNewChunkAvailable) {
            MessageObject messageObject4 = (MessageObject) args[0];
            if (messageObject4.getId() == 0) {
                return;
            }
            String finalPath2 = (String) args[1];
            long availableSize2 = ((Long) args[2]).longValue();
            long finalSize = ((Long) args[3]).longValue();
            boolean isEncrypted = DialogObject.isEncryptedDialog(messageObject4.getDialogId());
            getFileLoader().checkUploadNewDataAvailable(finalPath2, isEncrypted, availableSize2, finalSize);
            if (finalSize != 0) {
                stopVideoService(messageObject4.messageOwner.attachPath);
                ArrayList<DelayedMessage> arr9 = this.delayedMessages.get(messageObject4.messageOwner.attachPath);
                if (arr9 != null) {
                    int a7 = 0;
                    while (a7 < arr9.size()) {
                        DelayedMessage message3 = arr9.get(a7);
                        if (message3.type == 4) {
                            int b = 0;
                            while (true) {
                                if (b >= message3.messageObjects.size()) {
                                    finalPath = finalPath2;
                                    availableSize = availableSize2;
                                    break;
                                }
                                MessageObject obj2 = message3.messageObjects.get(b);
                                if (obj2 != messageObject4) {
                                    b++;
                                } else {
                                    finalPath = finalPath2;
                                    message3.obj.shouldRemoveVideoEditedInfo = true;
                                    obj2.messageOwner.params.remove("ve");
                                    availableSize = availableSize2;
                                    obj2.messageOwner.media.document.size = (int) finalSize;
                                    ArrayList<TLRPC.Message> messages = new ArrayList<>();
                                    messages.add(obj2.messageOwner);
                                    getMessagesStorage().putMessages(messages, false, true, false, 0, obj2.scheduled);
                                    break;
                                }
                            }
                        } else {
                            finalPath = finalPath2;
                            availableSize = availableSize2;
                            if (message3.obj == messageObject4) {
                                message3.obj.shouldRemoveVideoEditedInfo = true;
                                message3.obj.messageOwner.params.remove("ve");
                                message3.obj.messageOwner.media.document.size = (int) finalSize;
                                ArrayList<TLRPC.Message> messages2 = new ArrayList<>();
                                messages2.add(message3.obj.messageOwner);
                                getMessagesStorage().putMessages(messages2, false, true, false, 0, message3.obj.scheduled);
                                return;
                            }
                        }
                        a7++;
                        finalPath2 = finalPath;
                        availableSize2 = availableSize;
                    }
                }
            }
        } else if (id == NotificationCenter.filePreparingFailed) {
            MessageObject messageObject5 = (MessageObject) args[0];
            if (messageObject5.getId() == 0) {
                return;
            }
            String finalPath3 = (String) args[1];
            stopVideoService(messageObject5.messageOwner.attachPath);
            ArrayList<DelayedMessage> arr10 = this.delayedMessages.get(finalPath3);
            if (arr10 != null) {
                int a8 = 0;
                while (a8 < arr10.size()) {
                    DelayedMessage message4 = arr10.get(a8);
                    if (message4.type == 4) {
                        int b2 = 0;
                        while (true) {
                            if (b2 < message4.messages.size()) {
                                if (message4.messageObjects.get(b2) != messageObject5) {
                                    b2++;
                                } else {
                                    message4.markAsError();
                                    arr10.remove(a8);
                                    a8--;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if (message4.obj == messageObject5) {
                        message4.markAsError();
                        arr10.remove(a8);
                        a8--;
                    }
                    a8++;
                }
                if (arr10.isEmpty()) {
                    this.delayedMessages.remove(finalPath3);
                }
            }
        } else if (id == NotificationCenter.httpFileDidLoad) {
            final String path2 = (String) args[0];
            ArrayList<DelayedMessage> arr11 = this.delayedMessages.get(path2);
            if (arr11 != null) {
                int a9 = 0;
                while (a9 < arr11.size()) {
                    final DelayedMessage message5 = arr11.get(a9);
                    if (message5.type == 0) {
                        fileType = 0;
                        messageObject = message5.obj;
                    } else if (message5.type == 2) {
                        fileType = 1;
                        messageObject = message5.obj;
                    } else if (message5.type != 4) {
                        fileType = -1;
                        messageObject = null;
                    } else {
                        MessageObject messageObject6 = (MessageObject) message5.extraHashMap.get(path2);
                        if (messageObject6.getDocument() != null) {
                            fileType = 1;
                            messageObject = messageObject6;
                        } else {
                            fileType = 0;
                            messageObject = messageObject6;
                        }
                    }
                    if (fileType == 0) {
                        String md5 = Utilities.MD5(path2) + "." + ImageLoader.getHttpUrlExtension(path2, "file");
                        final File cacheFile = new File(FileLoader.getDirectory(4), md5);
                        final MessageObject messageObject7 = messageObject;
                        arr2 = arr11;
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.m1157x5b683917(cacheFile, messageObject7, message5, path2);
                            }
                        });
                    } else {
                        arr2 = arr11;
                        if (fileType == 1) {
                            String md52 = Utilities.MD5(path2) + ".gif";
                            final File cacheFile2 = new File(FileLoader.getDirectory(4), md52);
                            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda32
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.m1159x25eb1699(message5, cacheFile2, messageObject);
                                }
                            });
                        }
                    }
                    a9++;
                    arr11 = arr2;
                }
                this.delayedMessages.remove(path2);
            }
        } else if (id == NotificationCenter.fileLoaded) {
            String path3 = (String) args[0];
            ArrayList<DelayedMessage> arr12 = this.delayedMessages.get(path3);
            if (arr12 != null) {
                for (int a10 = 0; a10 < arr12.size(); a10++) {
                    performSendDelayedMessage(arr12.get(a10));
                }
                this.delayedMessages.remove(path3);
            }
        } else if ((id == NotificationCenter.httpFileDidFailedLoad || id == NotificationCenter.fileLoadFailed) && (arr = this.delayedMessages.get((path = (String) args[0]))) != null) {
            for (int a11 = 0; a11 < arr.size(); a11++) {
                arr.get(a11).markAsError();
            }
            this.delayedMessages.remove(path);
        }
    }

    /* renamed from: lambda$didReceivedNotification$2$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1157x5b683917(final File cacheFile, final MessageObject messageObject, final DelayedMessage message, final String path) {
        final TLRPC.TL_photo photo = generatePhotoSizes(cacheFile.toString(), null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1156x7626ca56(photo, messageObject, cacheFile, message, path);
            }
        });
    }

    /* renamed from: lambda$didReceivedNotification$1$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1156x7626ca56(TLRPC.TL_photo photo, MessageObject messageObject, File cacheFile, DelayedMessage message, String path) {
        if (photo != null) {
            messageObject.messageOwner.media.photo = photo;
            messageObject.messageOwner.attachPath = cacheFile.toString();
            ArrayList<TLRPC.Message> messages = new ArrayList<>();
            messages.add(messageObject.messageOwner);
            getMessagesStorage().putMessages(messages, false, true, false, 0, messageObject.scheduled);
            getNotificationCenter().postNotificationName(NotificationCenter.updateMessageMedia, messageObject.messageOwner);
            message.photoSize = photo.sizes.get(photo.sizes.size() - 1);
            message.locationParent = photo;
            message.httpLocation = null;
            if (message.type == 4) {
                message.performMediaUpload = true;
                performSendDelayedMessage(message, message.messageObjects.indexOf(messageObject));
                return;
            }
            performSendDelayedMessage(message);
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("can't load image " + path + " to file " + cacheFile.toString());
        }
        message.markAsError();
    }

    /* renamed from: lambda$didReceivedNotification$4$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1159x25eb1699(final DelayedMessage message, final File cacheFile, final MessageObject messageObject) {
        final TLRPC.Document document = message.obj.getDocument();
        boolean z = false;
        if (document.thumbs.isEmpty() || (document.thumbs.get(0).location instanceof TLRPC.TL_fileLocationUnavailable)) {
            try {
                Bitmap bitmap = ImageLoader.loadBitmap(cacheFile.getAbsolutePath(), null, 90.0f, 90.0f, true);
                if (bitmap != null) {
                    document.thumbs.clear();
                    ArrayList<TLRPC.PhotoSize> arrayList = document.thumbs;
                    if (message.sendEncryptedRequest != null) {
                        z = true;
                    }
                    arrayList.add(ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, z));
                    bitmap.recycle();
                }
            } catch (Exception e) {
                document.thumbs.clear();
                FileLog.e(e);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1158x40a9a7d8(message, cacheFile, document, messageObject);
            }
        });
    }

    /* renamed from: lambda$didReceivedNotification$3$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1158x40a9a7d8(DelayedMessage message, File cacheFile, TLRPC.Document document, MessageObject messageObject) {
        message.httpLocation = null;
        message.obj.messageOwner.attachPath = cacheFile.toString();
        if (!document.thumbs.isEmpty()) {
            TLRPC.PhotoSize photoSize = document.thumbs.get(0);
            if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                message.photoSize = photoSize;
                message.locationParent = document;
            }
        }
        ArrayList<TLRPC.Message> messages = new ArrayList<>();
        messages.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(messages, false, true, false, 0, messageObject.scheduled);
        message.performMediaUpload = true;
        performSendDelayedMessage(message);
        getNotificationCenter().postNotificationName(NotificationCenter.updateMessageMedia, message.obj.messageOwner);
    }

    private void revertEditingMessageObject(MessageObject object) {
        object.cancelEditing = true;
        object.messageOwner.media = object.previousMedia;
        object.messageOwner.message = object.previousMessage;
        object.messageOwner.entities = object.previousMessageEntities;
        object.messageOwner.attachPath = object.previousAttachPath;
        object.messageOwner.send_state = 0;
        if (object.messageOwner.entities != null) {
            object.messageOwner.flags |= 128;
        } else {
            object.messageOwner.flags &= -129;
        }
        object.previousMedia = null;
        object.previousMessage = null;
        object.previousMessageEntities = null;
        object.previousAttachPath = null;
        object.videoEditedInfo = null;
        object.type = -1;
        object.setType();
        object.caption = null;
        if (object.type != 0) {
            object.generateCaption();
        } else {
            object.resetLayout();
            object.checkLayout();
        }
        ArrayList<TLRPC.Message> arr = new ArrayList<>();
        arr.add(object.messageOwner);
        getMessagesStorage().putMessages(arr, false, true, false, 0, object.scheduled);
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(object);
        getNotificationCenter().postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(object.getDialogId()), arrayList);
    }

    public void cancelSendingMessage(MessageObject object) {
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(object);
        cancelSendingMessage(arrayList);
    }

    public void cancelSendingMessage(ArrayList<MessageObject> objects) {
        TLRPC.Message sendingMessage;
        Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it;
        boolean enc;
        long dialogId;
        int b;
        MessageObject messageObject;
        ArrayList<String> keysToRemove;
        ArrayList<MessageObject> arrayList = objects;
        ArrayList<String> keysToRemove2 = new ArrayList<>();
        ArrayList<DelayedMessage> checkReadyToSendGroups = new ArrayList<>();
        ArrayList<Integer> messageIds = new ArrayList<>();
        int c = 0;
        boolean enc2 = false;
        boolean scheduled = false;
        long dialogId2 = 0;
        while (c < objects.size()) {
            MessageObject object = arrayList.get(c);
            if (object.scheduled) {
                scheduled = true;
            }
            dialogId2 = object.getDialogId();
            messageIds.add(Integer.valueOf(object.getId()));
            TLRPC.Message sendingMessage2 = removeFromSendingMessages(object.getId(), object.scheduled);
            if (sendingMessage2 != null) {
                getConnectionsManager().cancelRequest(sendingMessage2.reqId, true);
            }
            Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it2 = this.delayedMessages.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, ArrayList<DelayedMessage>> entry = it2.next();
                ArrayList<DelayedMessage> messages = entry.getValue();
                int a = 0;
                while (true) {
                    if (a >= messages.size()) {
                        sendingMessage = sendingMessage2;
                        it = it2;
                        enc = enc2;
                        dialogId = dialogId2;
                        break;
                    }
                    DelayedMessage message = messages.get(a);
                    sendingMessage = sendingMessage2;
                    it = it2;
                    if (message.type == 4) {
                        MessageObject messageObject2 = null;
                        int index = 0;
                        while (true) {
                            MessageObject messageObject3 = messageObject2;
                            if (index >= message.messageObjects.size()) {
                                enc = enc2;
                                b = -1;
                                messageObject = messageObject3;
                                break;
                            }
                            MessageObject messageObject4 = message.messageObjects.get(index);
                            enc = enc2;
                            if (messageObject4.getId() == object.getId()) {
                                removeFromUploadingMessages(object.getId(), object.scheduled);
                                b = index;
                                messageObject = messageObject4;
                                break;
                            }
                            index++;
                            messageObject2 = messageObject4;
                            enc2 = enc;
                        }
                        if (b >= 0) {
                            message.messageObjects.remove(b);
                            message.messages.remove(b);
                            message.originalPaths.remove(b);
                            if (!message.parentObjects.isEmpty()) {
                                message.parentObjects.remove(b);
                            }
                            if (message.sendRequest != null) {
                                dialogId = dialogId2;
                                ((TLRPC.TL_messages_sendMultiMedia) message.sendRequest).multi_media.remove(b);
                            } else {
                                dialogId = dialogId2;
                                TLRPC.TL_messages_sendEncryptedMultiMedia request = (TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                                request.messages.remove(b);
                                request.files.remove(b);
                            }
                            MediaController.getInstance().cancelVideoConvert(object);
                            String keyToRemove = (String) message.extraHashMap.get(messageObject);
                            if (keyToRemove != null) {
                                keysToRemove2.add(keyToRemove);
                            }
                            if (message.messageObjects.isEmpty()) {
                                message.sendDelayedRequests();
                                keysToRemove = keysToRemove2;
                            } else {
                                int i = message.finalGroupMessage;
                                int index2 = object.getId();
                                if (i != index2) {
                                    keysToRemove = keysToRemove2;
                                } else {
                                    MessageObject prevMessage = message.messageObjects.get(message.messageObjects.size() - 1);
                                    message.finalGroupMessage = prevMessage.getId();
                                    prevMessage.messageOwner.params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                                    TLRPC.TL_messages_messages messagesRes = new TLRPC.TL_messages_messages();
                                    messagesRes.messages.add(prevMessage.messageOwner);
                                    keysToRemove = keysToRemove2;
                                    getMessagesStorage().putMessages((TLRPC.messages_Messages) messagesRes, message.peer, -2, 0, false, scheduled);
                                }
                                if (!checkReadyToSendGroups.contains(message)) {
                                    checkReadyToSendGroups.add(message);
                                }
                            }
                            keysToRemove2 = keysToRemove;
                        } else {
                            dialogId = dialogId2;
                        }
                    } else {
                        ArrayList<String> keysToRemove3 = keysToRemove2;
                        enc = enc2;
                        dialogId = dialogId2;
                        if (message.obj.getId() == object.getId()) {
                            removeFromUploadingMessages(object.getId(), object.scheduled);
                            messages.remove(a);
                            message.sendDelayedRequests();
                            MediaController.getInstance().cancelVideoConvert(message.obj);
                            if (messages.size() != 0) {
                                keysToRemove2 = keysToRemove3;
                            } else {
                                keysToRemove2 = keysToRemove3;
                                keysToRemove2.add(entry.getKey());
                                if (message.sendEncryptedRequest != null) {
                                    enc2 = true;
                                }
                            }
                        } else {
                            keysToRemove2 = keysToRemove3;
                            a++;
                            sendingMessage2 = sendingMessage;
                            it2 = it;
                            enc2 = enc;
                            dialogId2 = dialogId;
                        }
                    }
                }
                enc2 = enc;
                sendingMessage2 = sendingMessage;
                it2 = it;
                dialogId2 = dialogId;
            }
            c++;
            arrayList = objects;
        }
        for (int a2 = 0; a2 < keysToRemove2.size(); a2++) {
            String key = keysToRemove2.get(a2);
            if (key.startsWith("http")) {
                ImageLoader.getInstance().cancelLoadHttpFile(key);
            } else {
                getFileLoader().cancelFileUpload(key, enc2);
            }
            stopVideoService(key);
            this.delayedMessages.remove(key);
        }
        int N = checkReadyToSendGroups.size();
        for (int a3 = 0; a3 < N; a3++) {
            sendReadyToSendGroup(checkReadyToSendGroups.get(a3), false, true);
        }
        int a4 = objects.size();
        if (a4 == 1 && objects.get(0).isEditing() && objects.get(0).previousMedia != null) {
            revertEditingMessageObject(objects.get(0));
            return;
        }
        getMessagesController().deleteMessages(messageIds, null, null, dialogId2, false, scheduled);
    }

    public boolean retrySendMessage(MessageObject messageObject, boolean unsent) {
        if (messageObject.getId() >= 0) {
            if (messageObject.isEditing()) {
                editMessage(messageObject, null, null, null, null, null, true, messageObject);
            }
            return false;
        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageEncryptedAction) {
            int enc_id = DialogObject.getEncryptedChatId(messageObject.getDialogId());
            TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(enc_id));
            if (encryptedChat == null) {
                getMessagesStorage().markMessageAsSendError(messageObject.messageOwner, messageObject.scheduled);
                messageObject.messageOwner.send_state = 2;
                getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
                processSentMessage(messageObject.getId());
                return false;
            }
            if (messageObject.messageOwner.random_id == 0) {
                messageObject.messageOwner.random_id = getNextRandomId();
            }
            if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                getSecretChatHelper().sendTTLMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
                getSecretChatHelper().sendMessagesDeleteMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
                getSecretChatHelper().sendClearHistoryMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                getSecretChatHelper().sendNotifyLayerMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
                getSecretChatHelper().sendMessagesReadMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                getSecretChatHelper().sendScreenshotMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (!(messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionTyping)) {
                if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionResend) {
                    getSecretChatHelper().sendResendMessage(encryptedChat, 0, 0, messageObject.messageOwner);
                } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                    getSecretChatHelper().sendCommitKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                    getSecretChatHelper().sendAbortKeyMessage(encryptedChat, messageObject.messageOwner, 0L);
                } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                    getSecretChatHelper().sendRequestKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                    getSecretChatHelper().sendAcceptKeyMessage(encryptedChat, messageObject.messageOwner);
                } else if (messageObject.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionNoop) {
                    getSecretChatHelper().sendNoopMessage(encryptedChat, messageObject.messageOwner);
                }
            }
            return true;
        } else {
            if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(messageObject.getDialogId()));
                sendScreenshotMessage(user, messageObject.getReplyMsgId(), messageObject.messageOwner);
            }
            if (unsent) {
                this.unsentMessages.put(messageObject.getId(), messageObject);
            }
            sendMessage(messageObject);
            return true;
        }
    }

    public void processSentMessage(int id) {
        int prevSize = this.unsentMessages.size();
        this.unsentMessages.remove(id);
        if (prevSize != 0 && this.unsentMessages.size() == 0) {
            checkUnsentMessages();
        }
    }

    public void processForwardFromMyName(MessageObject messageObject, long did) {
        ArrayList<TLRPC.MessageEntity> entities;
        HashMap<String, String> params;
        if (messageObject == null) {
            return;
        }
        if (messageObject.messageOwner.media != null && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaEmpty) && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGame) && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice)) {
            if (DialogObject.isEncryptedDialog(did) && messageObject.messageOwner.peer_id != null && ((messageObject.messageOwner.media.photo instanceof TLRPC.TL_photo) || (messageObject.messageOwner.media.document instanceof TLRPC.TL_document))) {
                HashMap<String, String> params2 = new HashMap<>();
                params2.put("parentObject", "sent_" + messageObject.messageOwner.peer_id.channel_id + "_" + messageObject.getId());
                params = params2;
            } else {
                params = null;
            }
            if (messageObject.messageOwner.media.photo instanceof TLRPC.TL_photo) {
                sendMessage((TLRPC.TL_photo) messageObject.messageOwner.media.photo, null, did, messageObject.replyMessageObject, null, messageObject.messageOwner.message, messageObject.messageOwner.entities, null, params, true, 0, messageObject.messageOwner.media.ttl_seconds, messageObject);
            } else if (messageObject.messageOwner.media.document instanceof TLRPC.TL_document) {
                sendMessage((TLRPC.TL_document) messageObject.messageOwner.media.document, null, messageObject.messageOwner.attachPath, did, messageObject.replyMessageObject, null, messageObject.messageOwner.message, messageObject.messageOwner.entities, null, params, true, 0, messageObject.messageOwner.media.ttl_seconds, messageObject, null);
            } else if ((messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaVenue) || (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo)) {
                sendMessage(messageObject.messageOwner.media, did, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
            } else if (messageObject.messageOwner.media.phone_number != null) {
                TLRPC.User user = new TLRPC.TL_userContact_old2();
                user.phone = messageObject.messageOwner.media.phone_number;
                user.first_name = messageObject.messageOwner.media.first_name;
                user.last_name = messageObject.messageOwner.media.last_name;
                user.id = messageObject.messageOwner.media.user_id;
                sendMessage(user, did, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
            } else if (!DialogObject.isEncryptedDialog(did)) {
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                arrayList.add(messageObject);
                sendMessage(arrayList, did, true, false, true, 0);
            }
        } else if (messageObject.messageOwner.message != null) {
            TLRPC.WebPage webPage = null;
            if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                webPage = messageObject.messageOwner.media.webpage;
            }
            if (messageObject.messageOwner.entities != null && !messageObject.messageOwner.entities.isEmpty()) {
                ArrayList<TLRPC.MessageEntity> entities2 = new ArrayList<>();
                for (int a = 0; a < messageObject.messageOwner.entities.size(); a++) {
                    TLRPC.MessageEntity entity = messageObject.messageOwner.entities.get(a);
                    if ((entity instanceof TLRPC.TL_messageEntityBold) || (entity instanceof TLRPC.TL_messageEntityItalic) || (entity instanceof TLRPC.TL_messageEntityPre) || (entity instanceof TLRPC.TL_messageEntityCode) || (entity instanceof TLRPC.TL_messageEntityTextUrl) || (entity instanceof TLRPC.TL_messageEntitySpoiler)) {
                        entities2.add(entity);
                    }
                }
                entities = entities2;
            } else {
                entities = null;
            }
            sendMessage(messageObject.messageOwner.message, did, messageObject.replyMessageObject, null, webPage, true, entities, null, null, true, 0, null);
        } else if (DialogObject.isEncryptedDialog(did)) {
            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
            arrayList2.add(messageObject);
            sendMessage(arrayList2, did, true, false, true, 0);
        }
    }

    public void sendScreenshotMessage(TLRPC.User user, int messageId, TLRPC.Message resendMessage) {
        TLRPC.Message message;
        if (user != null && messageId != 0) {
            if (user.id == getUserConfig().getClientUserId()) {
                return;
            }
            TLRPC.TL_messages_sendScreenshotNotification req = new TLRPC.TL_messages_sendScreenshotNotification();
            req.peer = new TLRPC.TL_inputPeerUser();
            req.peer.access_hash = user.access_hash;
            req.peer.user_id = user.id;
            if (resendMessage != null) {
                req.reply_to_msg_id = messageId;
                req.random_id = resendMessage.random_id;
                message = resendMessage;
            } else {
                TLRPC.Message message2 = new TLRPC.TL_messageService();
                message2.random_id = getNextRandomId();
                message2.dialog_id = user.id;
                message2.unread = true;
                message2.out = true;
                int newMessageId = getUserConfig().getNewMessageId();
                message2.id = newMessageId;
                message2.local_id = newMessageId;
                message2.from_id = new TLRPC.TL_peerUser();
                message2.from_id.user_id = getUserConfig().getClientUserId();
                message2.flags |= 256;
                message2.flags |= 8;
                message2.reply_to = new TLRPC.TL_messageReplyHeader();
                message2.reply_to.reply_to_msg_id = messageId;
                message2.peer_id = new TLRPC.TL_peerUser();
                message2.peer_id.user_id = user.id;
                message2.date = getConnectionsManager().getCurrentTime();
                message2.action = new TLRPC.TL_messageActionScreenshotTaken();
                getUserConfig().saveConfig(false);
                message = message2;
            }
            req.random_id = message.random_id;
            MessageObject newMsgObj = new MessageObject(this.currentAccount, message, false, true);
            newMsgObj.messageOwner.send_state = 1;
            newMsgObj.wasJustSent = true;
            ArrayList<MessageObject> objArr = new ArrayList<>();
            objArr.add(newMsgObj);
            getMessagesController().updateInterfaceWithMessages(message.dialog_id, objArr, false);
            getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            ArrayList<TLRPC.Message> arr = new ArrayList<>();
            arr.add(message);
            getMessagesStorage().putMessages(arr, false, true, false, 0, false);
            performSendMessageRequest(req, newMsgObj, null, null, null, null, false);
        }
    }

    public void sendSticker(TLRPC.Document document, String query, final long peer, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final Object parentObject, final MessageObject.SendAnimationData sendAnimationData, final boolean notify, final int scheduleDate) {
        TLRPC.Document document2;
        HashMap<String, String> params;
        if (document == null) {
            return;
        }
        if (!DialogObject.isEncryptedDialog(peer)) {
            document2 = document;
        } else {
            int encryptedId = DialogObject.getEncryptedChatId(peer);
            TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedId));
            if (encryptedChat == null) {
                return;
            }
            TLRPC.TL_document_layer82 newDocument = new TLRPC.TL_document_layer82();
            newDocument.id = document.id;
            newDocument.access_hash = document.access_hash;
            newDocument.date = document.date;
            newDocument.mime_type = document.mime_type;
            newDocument.file_reference = document.file_reference;
            if (newDocument.file_reference == null) {
                newDocument.file_reference = new byte[0];
            }
            newDocument.size = document.size;
            newDocument.dc_id = document.dc_id;
            newDocument.attributes = new ArrayList<>(document.attributes);
            if (newDocument.mime_type == null) {
                newDocument.mime_type = "";
            }
            TLRPC.PhotoSize thumb = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
            if ((thumb instanceof TLRPC.TL_photoSize) || (thumb instanceof TLRPC.TL_photoSizeProgressive)) {
                File file = FileLoader.getInstance(this.currentAccount).getPathToAttach(thumb, true);
                if (file.exists()) {
                    try {
                        int length = (int) file.length();
                        byte[] arr = new byte[(int) file.length()];
                        RandomAccessFile reader = new RandomAccessFile(file, "r");
                        reader.readFully(arr);
                        TLRPC.PhotoSize newThumb = new TLRPC.TL_photoCachedSize();
                        TLRPC.TL_fileLocation_layer82 fileLocation = new TLRPC.TL_fileLocation_layer82();
                        fileLocation.dc_id = thumb.location.dc_id;
                        fileLocation.volume_id = thumb.location.volume_id;
                        fileLocation.local_id = thumb.location.local_id;
                        fileLocation.secret = thumb.location.secret;
                        newThumb.location = fileLocation;
                        newThumb.size = thumb.size;
                        newThumb.w = thumb.w;
                        newThumb.h = thumb.h;
                        newThumb.type = thumb.type;
                        newThumb.bytes = arr;
                        newDocument.thumbs.add(newThumb);
                        newDocument.flags = 1 | newDocument.flags;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
            if (newDocument.thumbs.isEmpty()) {
                TLRPC.PhotoSize thumb2 = new TLRPC.TL_photoSizeEmpty();
                thumb2.type = "s";
                newDocument.thumbs.add(thumb2);
            }
            document2 = newDocument;
        }
        final TLRPC.Document finalDocument = document2;
        if (MessageObject.isGifDocument(document2)) {
            mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1216lambda$sendSticker$6$orgtelegrammessengerSendMessagesHelper(finalDocument, peer, replyToMsg, replyToTopMsg, notify, scheduleDate, parentObject, sendAnimationData);
                }
            });
            return;
        }
        if (!TextUtils.isEmpty(query)) {
            params = new HashMap<>();
            params.put(SearchIntents.EXTRA_QUERY, query);
        } else {
            params = null;
        }
        sendMessage((TLRPC.TL_document) finalDocument, null, null, peer, replyToMsg, replyToTopMsg, null, null, null, params, notify, scheduleDate, 0, parentObject, sendAnimationData);
    }

    /* renamed from: lambda$sendSticker$6$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1216lambda$sendSticker$6$orgtelegrammessengerSendMessagesHelper(final TLRPC.Document finalDocument, final long peer, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final boolean notify, final int scheduleDate, final Object parentObject, final MessageObject.SendAnimationData sendAnimationData) {
        String docExt;
        File docFile;
        final Bitmap[] bitmapFinal = new Bitmap[1];
        final String[] keyFinal = new String[1];
        String mediaLocationKey = ImageLocation.getForDocument(finalDocument).getKey(null, null, false);
        if (MimeTypes.VIDEO_MP4.equals(finalDocument.mime_type)) {
            docExt = ".mp4";
        } else if ("video/x-matroska".equals(finalDocument.mime_type)) {
            docExt = ".mkv";
        } else {
            docExt = "";
        }
        File directory = FileLoader.getDirectory(3);
        File docFile2 = new File(directory, mediaLocationKey + docExt);
        if (docFile2.exists()) {
            docFile = docFile2;
        } else {
            File directory2 = FileLoader.getDirectory(2);
            docFile = new File(directory2, mediaLocationKey + docExt);
        }
        ensureMediaThumbExists(getAccountInstance(), false, finalDocument, docFile.getAbsolutePath(), null, 0L);
        keyFinal[0] = getKeyForPhotoSize(getAccountInstance(), FileLoader.getClosestPhotoSizeWithSize(finalDocument.thumbs, GroupCallActivity.TABLET_LIST_SIZE), bitmapFinal, true, true);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1215lambda$sendSticker$5$orgtelegrammessengerSendMessagesHelper(bitmapFinal, keyFinal, finalDocument, peer, replyToMsg, replyToTopMsg, notify, scheduleDate, parentObject, sendAnimationData);
            }
        });
    }

    /* renamed from: lambda$sendSticker$5$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1215lambda$sendSticker$5$orgtelegrammessengerSendMessagesHelper(Bitmap[] bitmapFinal, String[] keyFinal, TLRPC.Document finalDocument, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, boolean notify, int scheduleDate, Object parentObject, MessageObject.SendAnimationData sendAnimationData) {
        if (bitmapFinal[0] != null && keyFinal[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapFinal[0]), keyFinal[0], false);
        }
        sendMessage((TLRPC.TL_document) finalDocument, null, null, peer, replyToMsg, replyToTopMsg, null, null, null, null, notify, scheduleDate, 0, parentObject, sendAnimationData);
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x08ea  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x08fa  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0927  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x096c  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x096e  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x09b3  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x09db  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x02dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int sendMessage(ArrayList<MessageObject> messages, final long peer, boolean forwardFromMyName, boolean hideCaption, boolean notify, final int scheduleDate) {
        boolean canSendPreview;
        boolean canSendPolls;
        boolean canSendMedia;
        boolean canSendStickers;
        boolean isSignature;
        boolean isSignature2;
        int sendResult;
        long linkedToGroup;
        TLRPC.Chat chat;
        String rank;
        TLRPC.Chat chat2;
        final TLRPC.Peer peer_id;
        LongSparseArray<Long> groupsMap;
        TLRPC.InputPeer inputPeer;
        long linkedToGroup2;
        String rank2;
        ArrayList<Long> randomIds;
        long myId;
        final boolean toMyself;
        ArrayList<MessageObject> arrayList;
        int a;
        ArrayList<MessageObject> objArr;
        ArrayList<Integer> ids;
        final LongSparseArray<TLRPC.Message> messagesByRandomIds;
        ArrayList<TLRPC.Message> arr;
        TLRPC.WebPage webPage;
        String rank3;
        TLRPC.Chat chat3;
        LongSparseArray<Long> groupsMap2;
        long linkedToGroup3;
        LongSparseArray<Long> groupsMap3;
        LongSparseArray<Long> groupsMap4;
        long j;
        String rank4;
        TLRPC.Chat chat4;
        boolean z;
        ArrayList<MessageObject> arrayList2;
        int a2;
        boolean z2;
        int a3;
        boolean z3;
        int N;
        int N2;
        boolean forwardFromSaved;
        TLRPC.User signUser;
        boolean isChannel;
        boolean isSignature3;
        boolean isSignature4;
        ArrayList<MessageObject> arrayList3 = messages;
        long j2 = peer;
        boolean z4 = forwardFromMyName;
        boolean z5 = hideCaption;
        if (arrayList3 != null && !messages.isEmpty()) {
            long myId2 = getUserConfig().getClientUserId();
            if (DialogObject.isEncryptedDialog(peer)) {
                for (int a4 = 0; a4 < messages.size(); a4++) {
                    processForwardFromMyName(arrayList3.get(a4), peer);
                }
                return 0;
            }
            TLRPC.Peer peer_id2 = getMessagesController().getPeer(j2);
            boolean isSignature5 = false;
            String rank5 = null;
            long linkedToGroup4 = 0;
            if (DialogObject.isUserDialog(peer)) {
                sendResult = 0;
                TLRPC.User sendToUser = getMessagesController().getUser(Long.valueOf(peer));
                if (sendToUser == null) {
                    return 0;
                }
                chat = null;
                isSignature2 = false;
                isSignature = false;
                canSendStickers = true;
                canSendMedia = true;
                canSendPolls = true;
                canSendPreview = true;
                rank = null;
                linkedToGroup = 0;
            } else {
                sendResult = 0;
                TLRPC.Chat chat5 = getMessagesController().getChat(Long.valueOf(-j2));
                if (!ChatObject.isChannel(chat5)) {
                    isChannel = false;
                } else {
                    boolean isSignature6 = chat5.signatures;
                    isChannel = !chat5.megagroup;
                    if (!isChannel || !chat5.has_link) {
                        isSignature4 = isSignature6;
                    } else {
                        isSignature4 = isSignature6;
                        TLRPC.ChatFull chatFull = getMessagesController().getChatFull(chat5.id);
                        if (chatFull != null) {
                            linkedToGroup4 = chatFull.linked_chat_id;
                            isSignature5 = isSignature4;
                        }
                    }
                    isSignature5 = isSignature4;
                }
                if (chat5 == null) {
                    isSignature3 = isSignature5;
                } else {
                    isSignature3 = isSignature5;
                    rank5 = getMessagesController().getAdminRank(chat5.id, myId2);
                }
                boolean canSendStickers2 = ChatObject.canSendStickers(chat5);
                boolean canSendMedia2 = ChatObject.canSendMedia(chat5);
                boolean canSendPreview2 = ChatObject.canSendEmbed(chat5);
                boolean canSendPolls2 = ChatObject.canSendPolls(chat5);
                chat = chat5;
                canSendStickers = canSendStickers2;
                canSendMedia = canSendMedia2;
                canSendPolls = canSendPolls2;
                canSendPreview = canSendPreview2;
                linkedToGroup = linkedToGroup4;
                isSignature = isSignature3;
                isSignature2 = isChannel;
                rank = rank5;
            }
            LongSparseArray<Long> groupsMap5 = new LongSparseArray<>();
            ArrayList<MessageObject> objArr2 = new ArrayList<>();
            ArrayList<TLRPC.Message> arr2 = new ArrayList<>();
            ArrayList<Long> randomIds2 = new ArrayList<>();
            ArrayList<Integer> ids2 = new ArrayList<>();
            LongSparseArray<TLRPC.Message> messagesByRandomIds2 = new LongSparseArray<>();
            long linkedToGroup5 = linkedToGroup;
            TLRPC.InputPeer inputPeer2 = getMessagesController().getInputPeer(j2);
            boolean toMyself2 = j2 == myId2;
            TLRPC.InputPeer inputPeer3 = inputPeer2;
            long linkedToGroup6 = linkedToGroup5;
            LongSparseArray<TLRPC.Message> messagesByRandomIds3 = messagesByRandomIds2;
            boolean toMyself3 = toMyself2;
            ArrayList<Long> randomIds3 = randomIds2;
            ArrayList<MessageObject> objArr3 = objArr2;
            int a5 = 0;
            while (true) {
                LongSparseArray<TLRPC.Message> messagesByRandomIds4 = messagesByRandomIds3;
                if (a5 >= messages.size()) {
                    return sendResult;
                }
                final MessageObject msgObj = arrayList3.get(a5);
                int a6 = a5;
                if (msgObj.getId() <= 0) {
                    groupsMap = groupsMap5;
                    rank2 = rank;
                    chat2 = chat;
                    peer_id = peer_id2;
                    linkedToGroup2 = linkedToGroup6;
                    myId = myId2;
                    objArr = objArr3;
                    ids = ids2;
                    messagesByRandomIds = messagesByRandomIds4;
                    inputPeer = inputPeer3;
                    a = a6;
                    webPage = null;
                    arr = arr2;
                    boolean z6 = toMyself3;
                    randomIds = randomIds3;
                    toMyself = z6;
                } else if (msgObj.needDrawBluredPreview()) {
                    groupsMap = groupsMap5;
                    rank2 = rank;
                    chat2 = chat;
                    peer_id = peer_id2;
                    linkedToGroup2 = linkedToGroup6;
                    myId = myId2;
                    objArr = objArr3;
                    ids = ids2;
                    messagesByRandomIds = messagesByRandomIds4;
                    inputPeer = inputPeer3;
                    a = a6;
                    webPage = null;
                    arr = arr2;
                    boolean z7 = toMyself3;
                    randomIds = randomIds3;
                    toMyself = z7;
                } else {
                    boolean mediaIsSticker = msgObj.isSticker() || msgObj.isAnimatedSticker() || msgObj.isGif() || msgObj.isGame();
                    if (!canSendStickers && mediaIsSticker) {
                        if (sendResult != 0) {
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            objArr = objArr3;
                            ids = ids2;
                            messagesByRandomIds = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            arr = arr2;
                            boolean z8 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z8;
                            arr2 = arr;
                            messagesByRandomIds3 = messagesByRandomIds;
                            ids2 = ids;
                            objArr3 = objArr;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList4 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList4;
                        } else {
                            sendResult = ChatObject.isActionBannedByDefault(chat, 8) ? 4 : 1;
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            messagesByRandomIds3 = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            boolean z9 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z9;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList42 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList42;
                        }
                    } else if (!canSendMedia && (((msgObj.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || (msgObj.messageOwner.media instanceof TLRPC.TL_messageMediaDocument)) && !mediaIsSticker)) {
                        if (sendResult != 0) {
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            objArr = objArr3;
                            ids = ids2;
                            messagesByRandomIds = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            arr = arr2;
                            boolean z10 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z10;
                            arr2 = arr;
                            messagesByRandomIds3 = messagesByRandomIds;
                            ids2 = ids;
                            objArr3 = objArr;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList422 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList422;
                        } else {
                            sendResult = ChatObject.isActionBannedByDefault(chat, 7) ? 5 : 2;
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            messagesByRandomIds3 = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            boolean z11 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z11;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList4222 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList4222;
                        }
                    } else if (!canSendPolls && (msgObj.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                        if (sendResult != 0) {
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            objArr = objArr3;
                            ids = ids2;
                            messagesByRandomIds = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            arr = arr2;
                            boolean z12 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z12;
                            arr2 = arr;
                            messagesByRandomIds3 = messagesByRandomIds;
                            ids2 = ids;
                            objArr3 = objArr;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList42222 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList42222;
                        } else {
                            sendResult = ChatObject.isActionBannedByDefault(chat, 10) ? 6 : 3;
                            groupsMap = groupsMap5;
                            rank2 = rank;
                            chat2 = chat;
                            peer_id = peer_id2;
                            linkedToGroup2 = linkedToGroup6;
                            myId = myId2;
                            arrayList = arrayList3;
                            messagesByRandomIds3 = messagesByRandomIds4;
                            inputPeer = inputPeer3;
                            a = a6;
                            boolean z13 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z13;
                            a5 = a + 1;
                            j2 = peer;
                            z4 = forwardFromMyName;
                            z5 = hideCaption;
                            arrayList3 = arrayList;
                            myId2 = myId;
                            rank = rank2;
                            linkedToGroup6 = linkedToGroup2;
                            inputPeer3 = inputPeer;
                            groupsMap5 = groupsMap;
                            peer_id2 = peer_id;
                            chat = chat2;
                            ArrayList<Long> arrayList422222 = randomIds;
                            toMyself3 = toMyself;
                            randomIds3 = arrayList422222;
                        }
                    } else {
                        TLRPC.Message newMsg = new TLRPC.TL_message();
                        if (z4) {
                            groupsMap2 = groupsMap5;
                            rank3 = rank;
                            chat3 = chat;
                        } else {
                            if (msgObj.getDialogId() != myId2 || !msgObj.isFromUser()) {
                                rank3 = rank;
                                chat3 = chat;
                            } else {
                                rank3 = rank;
                                chat3 = chat;
                                if (msgObj.messageOwner.from_id.user_id == myId2) {
                                    forwardFromSaved = true;
                                    if (!msgObj.isForwarded()) {
                                        newMsg.fwd_from = new TLRPC.TL_messageFwdHeader();
                                        if ((msgObj.messageOwner.fwd_from.flags & 1) != 0) {
                                            newMsg.fwd_from.flags |= 1;
                                            newMsg.fwd_from.from_id = msgObj.messageOwner.fwd_from.from_id;
                                        }
                                        if ((msgObj.messageOwner.fwd_from.flags & 32) != 0) {
                                            newMsg.fwd_from.flags |= 32;
                                            newMsg.fwd_from.from_name = msgObj.messageOwner.fwd_from.from_name;
                                        }
                                        if ((msgObj.messageOwner.fwd_from.flags & 4) != 0) {
                                            newMsg.fwd_from.flags |= 4;
                                            newMsg.fwd_from.channel_post = msgObj.messageOwner.fwd_from.channel_post;
                                        }
                                        if ((msgObj.messageOwner.fwd_from.flags & 8) != 0) {
                                            TLRPC.MessageFwdHeader messageFwdHeader = newMsg.fwd_from;
                                            messageFwdHeader.flags = 8 | messageFwdHeader.flags;
                                            newMsg.fwd_from.post_author = msgObj.messageOwner.fwd_from.post_author;
                                        }
                                        if ((j2 == myId2 || isSignature2) && (msgObj.messageOwner.fwd_from.flags & 16) != 0 && !UserObject.isReplyUser(msgObj.getDialogId())) {
                                            newMsg.fwd_from.flags |= 16;
                                            newMsg.fwd_from.saved_from_peer = msgObj.messageOwner.fwd_from.saved_from_peer;
                                            newMsg.fwd_from.saved_from_msg_id = msgObj.messageOwner.fwd_from.saved_from_msg_id;
                                        }
                                        newMsg.fwd_from.date = msgObj.messageOwner.fwd_from.date;
                                        newMsg.flags = 4;
                                        groupsMap2 = groupsMap5;
                                    } else if (forwardFromSaved) {
                                        groupsMap2 = groupsMap5;
                                    } else {
                                        long fromId = msgObj.getFromChatId();
                                        newMsg.fwd_from = new TLRPC.TL_messageFwdHeader();
                                        newMsg.fwd_from.channel_post = msgObj.getId();
                                        newMsg.fwd_from.flags |= 4;
                                        if (msgObj.isFromUser()) {
                                            newMsg.fwd_from.from_id = msgObj.messageOwner.from_id;
                                            newMsg.fwd_from.flags |= 1;
                                            groupsMap2 = groupsMap5;
                                        } else {
                                            newMsg.fwd_from.from_id = new TLRPC.TL_peerChannel();
                                            groupsMap2 = groupsMap5;
                                            newMsg.fwd_from.from_id.channel_id = msgObj.messageOwner.peer_id.channel_id;
                                            newMsg.fwd_from.flags |= 1;
                                            if (msgObj.messageOwner.post && fromId > 0) {
                                                newMsg.fwd_from.from_id = msgObj.messageOwner.from_id != null ? msgObj.messageOwner.from_id : msgObj.messageOwner.peer_id;
                                            }
                                        }
                                        if (msgObj.messageOwner.post_author == null && !msgObj.isOutOwner() && fromId > 0 && msgObj.messageOwner.post && (signUser = getMessagesController().getUser(Long.valueOf(fromId))) != null) {
                                            newMsg.fwd_from.post_author = ContactsController.formatName(signUser.first_name, signUser.last_name);
                                            newMsg.fwd_from.flags |= 8;
                                        }
                                        newMsg.date = msgObj.messageOwner.date;
                                        newMsg.flags = 4;
                                    }
                                    if (j2 == myId2 && newMsg.fwd_from != null) {
                                        newMsg.fwd_from.flags |= 16;
                                        newMsg.fwd_from.saved_from_msg_id = msgObj.getId();
                                        newMsg.fwd_from.saved_from_peer = msgObj.messageOwner.peer_id;
                                        if (newMsg.fwd_from.saved_from_peer.user_id == myId2) {
                                            newMsg.fwd_from.saved_from_peer.user_id = msgObj.getDialogId();
                                        }
                                    }
                                }
                            }
                            forwardFromSaved = false;
                            if (!msgObj.isForwarded()) {
                            }
                            if (j2 == myId2) {
                                newMsg.fwd_from.flags |= 16;
                                newMsg.fwd_from.saved_from_msg_id = msgObj.getId();
                                newMsg.fwd_from.saved_from_peer = msgObj.messageOwner.peer_id;
                                if (newMsg.fwd_from.saved_from_peer.user_id == myId2) {
                                }
                            }
                        }
                        newMsg.params = new HashMap<>();
                        newMsg.params.put("fwd_id", "" + msgObj.getId());
                        newMsg.params.put("fwd_peer", "" + msgObj.getDialogId());
                        if (!msgObj.messageOwner.restriction_reason.isEmpty()) {
                            newMsg.restriction_reason = msgObj.messageOwner.restriction_reason;
                            newMsg.flags |= 4194304;
                        }
                        if (!canSendPreview && (msgObj.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage)) {
                            newMsg.media = new TLRPC.TL_messageMediaEmpty();
                        } else {
                            newMsg.media = msgObj.messageOwner.media;
                        }
                        if (newMsg.media != null) {
                            newMsg.flags |= 512;
                        }
                        if (msgObj.messageOwner.via_bot_id != 0) {
                            newMsg.via_bot_id = msgObj.messageOwner.via_bot_id;
                            newMsg.flags |= 2048;
                        }
                        if (linkedToGroup6 != 0) {
                            newMsg.replies = new TLRPC.TL_messageReplies();
                            newMsg.replies.comments = true;
                            newMsg.replies.channel_id = linkedToGroup6;
                            newMsg.replies.flags |= 1;
                            newMsg.flags |= 8388608;
                        }
                        if (!z5 || newMsg.media == null) {
                            newMsg.message = msgObj.messageOwner.message;
                        }
                        if (newMsg.message == null) {
                            newMsg.message = "";
                        }
                        newMsg.fwd_msg_id = msgObj.getId();
                        newMsg.attachPath = msgObj.messageOwner.attachPath;
                        newMsg.entities = msgObj.messageOwner.entities;
                        if (!(msgObj.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                            linkedToGroup3 = linkedToGroup6;
                        } else {
                            newMsg.reply_markup = new TLRPC.TL_replyInlineMarkup();
                            boolean dropMarkup = false;
                            int b = 0;
                            int N3 = msgObj.messageOwner.reply_markup.rows.size();
                            while (true) {
                                if (b >= N3) {
                                    linkedToGroup3 = linkedToGroup6;
                                    break;
                                }
                                TLRPC.TL_keyboardButtonRow oldRow = msgObj.messageOwner.reply_markup.rows.get(b);
                                TLRPC.TL_keyboardButtonRow newRow = null;
                                boolean dropMarkup2 = dropMarkup;
                                int N22 = oldRow.buttons.size();
                                linkedToGroup3 = linkedToGroup6;
                                int c = 0;
                                while (true) {
                                    if (c >= N22) {
                                        N = N3;
                                        dropMarkup = dropMarkup2;
                                        break;
                                    }
                                    TLRPC.KeyboardButton button = oldRow.buttons.get(c);
                                    int N23 = N22;
                                    if ((button instanceof TLRPC.TL_keyboardButtonUrlAuth) || (button instanceof TLRPC.TL_keyboardButtonUrl) || (button instanceof TLRPC.TL_keyboardButtonSwitchInline) || (button instanceof TLRPC.TL_keyboardButtonBuy)) {
                                        boolean dropMarkup3 = button instanceof TLRPC.TL_keyboardButtonUrlAuth;
                                        if (!dropMarkup3) {
                                            N2 = N3;
                                        } else {
                                            TLRPC.TL_keyboardButtonUrlAuth auth = new TLRPC.TL_keyboardButtonUrlAuth();
                                            N2 = N3;
                                            int N4 = button.flags;
                                            auth.flags = N4;
                                            if (button.fwd_text != null) {
                                                String str = button.fwd_text;
                                                auth.fwd_text = str;
                                                auth.text = str;
                                            } else {
                                                auth.text = button.text;
                                            }
                                            auth.url = button.url;
                                            auth.button_id = button.button_id;
                                            button = auth;
                                        }
                                        if (newRow == null) {
                                            TLRPC.TL_keyboardButtonRow newRow2 = new TLRPC.TL_keyboardButtonRow();
                                            newMsg.reply_markup.rows.add(newRow2);
                                            newRow = newRow2;
                                        }
                                        newRow.buttons.add(button);
                                        c++;
                                        N22 = N23;
                                        N3 = N2;
                                    } else {
                                        dropMarkup = true;
                                        N = N3;
                                        break;
                                    }
                                }
                                if (dropMarkup) {
                                    break;
                                }
                                b++;
                                linkedToGroup6 = linkedToGroup3;
                                N3 = N;
                            }
                            if (!dropMarkup) {
                                newMsg.flags |= 64;
                            } else {
                                msgObj.messageOwner.reply_markup = null;
                                newMsg.flags &= -65;
                            }
                        }
                        if (!newMsg.entities.isEmpty()) {
                            newMsg.flags |= 128;
                        }
                        if (newMsg.attachPath == null) {
                            newMsg.attachPath = "";
                        }
                        int newMessageId = getUserConfig().getNewMessageId();
                        newMsg.id = newMessageId;
                        newMsg.local_id = newMessageId;
                        newMsg.out = true;
                        if (msgObj.messageOwner.grouped_id == 0) {
                            groupsMap3 = groupsMap2;
                        } else {
                            groupsMap3 = groupsMap2;
                            Long gId = groupsMap3.get(msgObj.messageOwner.grouped_id);
                            if (gId == null) {
                                gId = Long.valueOf(Utilities.random.nextLong());
                                groupsMap3.put(msgObj.messageOwner.grouped_id, gId);
                            }
                            newMsg.grouped_id = gId.longValue();
                            newMsg.flags |= 131072;
                        }
                        if (peer_id2.channel_id == 0 || !isSignature2) {
                            j = peer;
                            groupsMap4 = groupsMap3;
                            chat4 = chat3;
                            long fromPeerId = ChatObject.getSendAsPeerId(chat4, getMessagesController().getChatFull(-j), true);
                            if (fromPeerId == myId2) {
                                newMsg.from_id = new TLRPC.TL_peerUser();
                                newMsg.from_id.user_id = myId2;
                                newMsg.flags |= 256;
                                rank4 = rank3;
                            } else {
                                newMsg.from_id = getMessagesController().getPeer(fromPeerId);
                                if (rank3 == null) {
                                    rank4 = rank3;
                                } else {
                                    rank4 = rank3;
                                    newMsg.post_author = rank4;
                                    newMsg.flags |= 65536;
                                }
                            }
                        } else {
                            if (isSignature) {
                                newMsg.from_id = new TLRPC.TL_peerUser();
                                newMsg.from_id.user_id = myId2;
                            } else {
                                newMsg.from_id = peer_id2;
                            }
                            newMsg.post = true;
                            j = peer;
                            groupsMap4 = groupsMap3;
                            chat4 = chat3;
                            rank4 = rank3;
                        }
                        if (newMsg.random_id == 0) {
                            newMsg.random_id = getNextRandomId();
                        }
                        randomIds3.add(Long.valueOf(newMsg.random_id));
                        messagesByRandomIds4.put(newMsg.random_id, newMsg);
                        ArrayList<Integer> ids3 = ids2;
                        ids3.add(Integer.valueOf(newMsg.fwd_msg_id));
                        linkedToGroup2 = linkedToGroup3;
                        newMsg.date = scheduleDate != 0 ? scheduleDate : getConnectionsManager().getCurrentTime();
                        TLRPC.InputPeer inputPeer4 = inputPeer3;
                        if ((inputPeer4 instanceof TLRPC.TL_inputPeerChannel) && isSignature2) {
                            if (scheduleDate == 0) {
                                newMsg.views = 1;
                                newMsg.flags |= 1024;
                            }
                        } else {
                            if ((msgObj.messageOwner.flags & 1024) != 0 && scheduleDate == 0) {
                                newMsg.views = msgObj.messageOwner.views;
                                newMsg.flags |= 1024;
                            }
                            newMsg.unread = true;
                        }
                        newMsg.dialog_id = j;
                        newMsg.peer_id = peer_id2;
                        if (!MessageObject.isVoiceMessage(newMsg) && !MessageObject.isRoundVideoMessage(newMsg)) {
                            z = true;
                        } else if ((inputPeer4 instanceof TLRPC.TL_inputPeerChannel) && msgObj.getChannelId() != 0) {
                            newMsg.media_unread = msgObj.isContentUnread();
                            z = true;
                        } else {
                            z = true;
                            newMsg.media_unread = true;
                        }
                        TLRPC.Peer peer_id3 = peer_id2;
                        LongSparseArray<Long> groupsMap6 = groupsMap4;
                        messagesByRandomIds = messagesByRandomIds4;
                        TLRPC.Chat chat6 = chat4;
                        MessageObject newMsgObj = new MessageObject(this.currentAccount, newMsg, z, z);
                        newMsgObj.scheduled = scheduleDate != 0;
                        newMsgObj.messageOwner.send_state = 1;
                        newMsgObj.wasJustSent = true;
                        ArrayList<MessageObject> objArr4 = objArr3;
                        objArr4.add(newMsgObj);
                        final ArrayList<TLRPC.Message> arr3 = arr2;
                        arr3.add(newMsg);
                        long myId3 = myId2;
                        if (msgObj.replyMessageObject == null) {
                            arrayList2 = messages;
                            rank2 = rank4;
                        } else {
                            int i = 0;
                            while (true) {
                                if (i >= messages.size()) {
                                    arrayList2 = messages;
                                    rank2 = rank4;
                                    break;
                                }
                                arrayList2 = messages;
                                rank2 = rank4;
                                if (arrayList2.get(i).getId() != msgObj.replyMessageObject.getId()) {
                                    i++;
                                    rank4 = rank2;
                                } else {
                                    newMsgObj.messageOwner.replyMessage = msgObj.replyMessageObject.messageOwner;
                                    newMsgObj.replyMessageObject = msgObj.replyMessageObject;
                                    break;
                                }
                            }
                        }
                        putToSendingMessages(newMsg, scheduleDate != 0);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("forward message user_id = " + inputPeer4.user_id + " chat_id = " + inputPeer4.chat_id + " channel_id = " + inputPeer4.channel_id + " access_hash = " + inputPeer4.access_hash);
                        }
                        if (arr3.size() != 100) {
                            a2 = a6;
                            if (a2 != messages.size() - 1 && (a2 == messages.size() - 1 || arrayList2.get(a2 + 1).getDialogId() == msgObj.getDialogId())) {
                                ids = ids3;
                                objArr = objArr4;
                                arr = arr3;
                                inputPeer = inputPeer4;
                                arrayList = arrayList2;
                                a = a2;
                                myId = myId3;
                                peer_id = peer_id3;
                                chat2 = chat6;
                                groupsMap = groupsMap6;
                                boolean z14 = toMyself3;
                                randomIds = randomIds3;
                                toMyself = z14;
                                arr2 = arr;
                                messagesByRandomIds3 = messagesByRandomIds;
                                ids2 = ids;
                                objArr3 = objArr;
                                a5 = a + 1;
                                j2 = peer;
                                z4 = forwardFromMyName;
                                z5 = hideCaption;
                                arrayList3 = arrayList;
                                myId2 = myId;
                                rank = rank2;
                                linkedToGroup6 = linkedToGroup2;
                                inputPeer3 = inputPeer;
                                groupsMap5 = groupsMap;
                                peer_id2 = peer_id;
                                chat = chat2;
                                ArrayList<Long> arrayList4222222 = randomIds;
                                toMyself3 = toMyself;
                                randomIds3 = arrayList4222222;
                            }
                        } else {
                            a2 = a6;
                        }
                        getMessagesStorage().putMessages(new ArrayList<>(arr3), false, true, false, 0, scheduleDate != 0);
                        getMessagesController().updateInterfaceWithMessages(j, objArr4, scheduleDate != 0);
                        getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        getUserConfig().saveConfig(false);
                        final TLRPC.TL_messages_forwardMessages req = new TLRPC.TL_messages_forwardMessages();
                        req.to_peer = inputPeer4;
                        if (notify) {
                            if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + j, false)) {
                                z2 = false;
                                req.silent = z2;
                                if (scheduleDate != 0) {
                                    req.schedule_date = scheduleDate;
                                    req.flags |= 1024;
                                }
                                if (!(msgObj.messageOwner.peer_id instanceof TLRPC.TL_peerChannel)) {
                                    a3 = a2;
                                    TLRPC.Chat channel = getMessagesController().getChat(Long.valueOf(msgObj.messageOwner.peer_id.channel_id));
                                    req.from_peer = new TLRPC.TL_inputPeerChannel();
                                    req.from_peer.channel_id = msgObj.messageOwner.peer_id.channel_id;
                                    if (channel != null) {
                                        req.from_peer.access_hash = channel.access_hash;
                                    }
                                } else {
                                    a3 = a2;
                                    req.from_peer = new TLRPC.TL_inputPeerEmpty();
                                }
                                req.random_id = randomIds3;
                                req.id = ids3;
                                req.drop_author = forwardFromMyName;
                                req.drop_media_captions = hideCaption;
                                if (messages.size() == 1 && arrayList2.get(0).messageOwner.with_my_score) {
                                    z3 = true;
                                    req.with_my_score = z3;
                                    myId = myId3;
                                    final ArrayList<MessageObject> newMsgArr = new ArrayList<>(objArr4);
                                    objArr = objArr4;
                                    int a7 = a3;
                                    ids = ids3;
                                    inputPeer = inputPeer4;
                                    final boolean scheduledOnline = scheduleDate != 2147483646;
                                    groupsMap = groupsMap6;
                                    peer_id = peer_id3;
                                    chat2 = chat6;
                                    arr = arr3;
                                    boolean z15 = toMyself3;
                                    randomIds = randomIds3;
                                    toMyself = z15;
                                    getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            SendMessagesHelper.this.m1207lambda$sendMessage$14$orgtelegrammessengerSendMessagesHelper(peer, scheduleDate, scheduledOnline, toMyself, messagesByRandomIds, arr3, newMsgArr, msgObj, peer_id, req, tLObject, tL_error);
                                        }
                                    }, 68);
                                    a = a7;
                                    if (a != messages.size() - 1) {
                                        arrayList = messages;
                                        arr2 = arr;
                                        messagesByRandomIds3 = messagesByRandomIds;
                                        ids2 = ids;
                                        objArr3 = objArr;
                                        a5 = a + 1;
                                        j2 = peer;
                                        z4 = forwardFromMyName;
                                        z5 = hideCaption;
                                        arrayList3 = arrayList;
                                        myId2 = myId;
                                        rank = rank2;
                                        linkedToGroup6 = linkedToGroup2;
                                        inputPeer3 = inputPeer;
                                        groupsMap5 = groupsMap;
                                        peer_id2 = peer_id;
                                        chat = chat2;
                                        ArrayList<Long> arrayList42222222 = randomIds;
                                        toMyself3 = toMyself;
                                        randomIds3 = arrayList42222222;
                                    } else {
                                        ArrayList<MessageObject> objArr5 = new ArrayList<>();
                                        ArrayList<TLRPC.Message> arr4 = new ArrayList<>();
                                        ArrayList<Long> randomIds4 = new ArrayList<>();
                                        ArrayList<Integer> ids4 = new ArrayList<>();
                                        objArr3 = objArr5;
                                        arr2 = arr4;
                                        randomIds = randomIds4;
                                        ids2 = ids4;
                                        messagesByRandomIds3 = new LongSparseArray<>();
                                        arrayList = messages;
                                        a5 = a + 1;
                                        j2 = peer;
                                        z4 = forwardFromMyName;
                                        z5 = hideCaption;
                                        arrayList3 = arrayList;
                                        myId2 = myId;
                                        rank = rank2;
                                        linkedToGroup6 = linkedToGroup2;
                                        inputPeer3 = inputPeer;
                                        groupsMap5 = groupsMap;
                                        peer_id2 = peer_id;
                                        chat = chat2;
                                        ArrayList<Long> arrayList422222222 = randomIds;
                                        toMyself3 = toMyself;
                                        randomIds3 = arrayList422222222;
                                    }
                                }
                                z3 = false;
                                req.with_my_score = z3;
                                myId = myId3;
                                final ArrayList newMsgArr2 = new ArrayList<>(objArr4);
                                objArr = objArr4;
                                int a72 = a3;
                                ids = ids3;
                                inputPeer = inputPeer4;
                                final boolean scheduledOnline2 = scheduleDate != 2147483646;
                                groupsMap = groupsMap6;
                                peer_id = peer_id3;
                                chat2 = chat6;
                                arr = arr3;
                                boolean z152 = toMyself3;
                                randomIds = randomIds3;
                                toMyself = z152;
                                getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        SendMessagesHelper.this.m1207lambda$sendMessage$14$orgtelegrammessengerSendMessagesHelper(peer, scheduleDate, scheduledOnline2, toMyself, messagesByRandomIds, arr3, newMsgArr2, msgObj, peer_id, req, tLObject, tL_error);
                                    }
                                }, 68);
                                a = a72;
                                if (a != messages.size() - 1) {
                                }
                            }
                        }
                        z2 = true;
                        req.silent = z2;
                        if (scheduleDate != 0) {
                        }
                        if (!(msgObj.messageOwner.peer_id instanceof TLRPC.TL_peerChannel)) {
                        }
                        req.random_id = randomIds3;
                        req.id = ids3;
                        req.drop_author = forwardFromMyName;
                        req.drop_media_captions = hideCaption;
                        if (messages.size() == 1) {
                            z3 = true;
                            req.with_my_score = z3;
                            myId = myId3;
                            final ArrayList newMsgArr22 = new ArrayList<>(objArr4);
                            objArr = objArr4;
                            int a722 = a3;
                            ids = ids3;
                            inputPeer = inputPeer4;
                            final boolean scheduledOnline22 = scheduleDate != 2147483646;
                            groupsMap = groupsMap6;
                            peer_id = peer_id3;
                            chat2 = chat6;
                            arr = arr3;
                            boolean z1522 = toMyself3;
                            randomIds = randomIds3;
                            toMyself = z1522;
                            getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    SendMessagesHelper.this.m1207lambda$sendMessage$14$orgtelegrammessengerSendMessagesHelper(peer, scheduleDate, scheduledOnline22, toMyself, messagesByRandomIds, arr3, newMsgArr22, msgObj, peer_id, req, tLObject, tL_error);
                                }
                            }, 68);
                            a = a722;
                            if (a != messages.size() - 1) {
                            }
                        }
                        z3 = false;
                        req.with_my_score = z3;
                        myId = myId3;
                        final ArrayList newMsgArr222 = new ArrayList<>(objArr4);
                        objArr = objArr4;
                        int a7222 = a3;
                        ids = ids3;
                        inputPeer = inputPeer4;
                        final boolean scheduledOnline222 = scheduleDate != 2147483646;
                        groupsMap = groupsMap6;
                        peer_id = peer_id3;
                        chat2 = chat6;
                        arr = arr3;
                        boolean z15222 = toMyself3;
                        randomIds = randomIds3;
                        toMyself = z15222;
                        getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                SendMessagesHelper.this.m1207lambda$sendMessage$14$orgtelegrammessengerSendMessagesHelper(peer, scheduleDate, scheduledOnline222, toMyself, messagesByRandomIds, arr3, newMsgArr222, msgObj, peer_id, req, tLObject, tL_error);
                            }
                        }, 68);
                        a = a7222;
                        if (a != messages.size() - 1) {
                        }
                    }
                }
                if (msgObj.type != 0 || TextUtils.isEmpty(msgObj.messageText)) {
                    arrayList = messages;
                } else {
                    TLRPC.WebPage webPage2 = msgObj.messageOwner.media != null ? msgObj.messageOwner.media.webpage : webPage;
                    arrayList = messages;
                    sendMessage(msgObj.messageText.toString(), peer, null, null, webPage2, webPage2 != null, msgObj.messageOwner.entities, null, null, notify, scheduleDate, null);
                }
                arr2 = arr;
                messagesByRandomIds3 = messagesByRandomIds;
                ids2 = ids;
                objArr3 = objArr;
                a5 = a + 1;
                j2 = peer;
                z4 = forwardFromMyName;
                z5 = hideCaption;
                arrayList3 = arrayList;
                myId2 = myId;
                rank = rank2;
                linkedToGroup6 = linkedToGroup2;
                inputPeer3 = inputPeer;
                groupsMap5 = groupsMap;
                peer_id2 = peer_id;
                chat = chat2;
                ArrayList<Long> arrayList4222222222 = randomIds;
                toMyself3 = toMyself;
                randomIds3 = arrayList4222222222;
            }
        }
        return 0;
    }

    /* renamed from: lambda$sendMessage$14$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1207lambda$sendMessage$14$orgtelegrammessengerSendMessagesHelper(final long peer, final int scheduleDate, boolean scheduledOnline, boolean toMyself, LongSparseArray messagesByRandomIdsFinal, ArrayList newMsgObjArr, ArrayList newMsgArr, final MessageObject msgObj, final TLRPC.Peer peer_id, final TLRPC.TL_messages_forwardMessages req, TLObject response, final TLRPC.TL_error error) {
        SendMessagesHelper sendMessagesHelper;
        Integer value;
        SparseLongArray newMessagesByIds;
        TLRPC.Updates updates;
        int i;
        int sentCount;
        boolean currentSchedule;
        TLRPC.Message message;
        int sentCount2;
        int i2 = scheduleDate;
        ArrayList arrayList = newMsgObjArr;
        ArrayList arrayList2 = newMsgArr;
        if (error == null) {
            SparseLongArray newMessagesByIds2 = new SparseLongArray();
            TLRPC.Updates updates2 = (TLRPC.Updates) response;
            int a1 = 0;
            while (a1 < updates2.updates.size()) {
                TLRPC.Update update = updates2.updates.get(a1);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID updateMessageID = (TLRPC.TL_updateMessageID) update;
                    newMessagesByIds2.put(updateMessageID.id, updateMessageID.random_id);
                    updates2.updates.remove(a1);
                    a1--;
                }
                a1++;
            }
            Integer value2 = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(peer));
            if (value2 == null) {
                Integer value3 = Integer.valueOf(getMessagesStorage().getDialogReadMax(true, peer));
                getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(peer), value3);
                value = value3;
            } else {
                value = value2;
            }
            int a12 = 0;
            int sentCount3 = 0;
            while (a12 < updates2.updates.size()) {
                TLRPC.Update update2 = updates2.updates.get(a12);
                if ((update2 instanceof TLRPC.TL_updateNewMessage) || (update2 instanceof TLRPC.TL_updateNewChannelMessage) || (update2 instanceof TLRPC.TL_updateNewScheduledMessage)) {
                    boolean currentSchedule2 = i2 != 0;
                    updates2.updates.remove(a12);
                    int a13 = a12 - 1;
                    if (update2 instanceof TLRPC.TL_updateNewMessage) {
                        TLRPC.TL_updateNewMessage updateNewMessage = (TLRPC.TL_updateNewMessage) update2;
                        message = updateNewMessage.message;
                        MessagesController messagesController = getMessagesController();
                        currentSchedule = currentSchedule2;
                        int i3 = updateNewMessage.pts;
                        sentCount = sentCount3;
                        int sentCount4 = updateNewMessage.pts_count;
                        messagesController.processNewDifferenceParams(-1, i3, -1, sentCount4);
                    } else {
                        currentSchedule = currentSchedule2;
                        sentCount = sentCount3;
                        if (update2 instanceof TLRPC.TL_updateNewScheduledMessage) {
                            message = ((TLRPC.TL_updateNewScheduledMessage) update2).message;
                        } else {
                            TLRPC.TL_updateNewChannelMessage updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update2;
                            message = updateNewChannelMessage.message;
                            getMessagesController().processNewChannelDifferenceParams(updateNewChannelMessage.pts, updateNewChannelMessage.pts_count, message.peer_id.channel_id);
                        }
                    }
                    if (scheduledOnline && message.date != 2147483646) {
                        currentSchedule = false;
                    }
                    ImageLoader.saveMessageThumbs(message);
                    if (!currentSchedule) {
                        message.unread = value.intValue() < message.id;
                    }
                    if (toMyself) {
                        message.out = true;
                        message.unread = false;
                        message.media_unread = false;
                    }
                    long random_id = newMessagesByIds2.get(message.id);
                    if (random_id != 0) {
                        final TLRPC.Message newMsgObj1 = (TLRPC.Message) messagesByRandomIdsFinal.get(random_id);
                        if (newMsgObj1 == null) {
                            newMessagesByIds = newMessagesByIds2;
                            updates = updates2;
                            sentCount2 = sentCount;
                            i = 1;
                        } else {
                            int index = arrayList.indexOf(newMsgObj1);
                            if (index == -1) {
                                newMessagesByIds = newMessagesByIds2;
                                updates = updates2;
                                sentCount2 = sentCount;
                                i = 1;
                            } else {
                                MessageObject msgObj1 = (MessageObject) arrayList2.get(index);
                                arrayList.remove(index);
                                arrayList2.remove(index);
                                final int index2 = newMsgObj1.id;
                                final ArrayList<TLRPC.Message> sentMessages = new ArrayList<>();
                                sentMessages.add(message);
                                msgObj1.messageOwner.post_author = message.post_author;
                                if ((message.flags & ConnectionsManager.FileTypeVideo) != 0) {
                                    msgObj1.messageOwner.ttl_period = message.ttl_period;
                                    msgObj1.messageOwner.flags |= ConnectionsManager.FileTypeVideo;
                                }
                                newMessagesByIds = newMessagesByIds2;
                                updateMediaPaths(msgObj1, message, message.id, null, true);
                                final int existFlags = msgObj1.getMediaExistanceFlags();
                                newMsgObj1.id = message.id;
                                int sentCount5 = sentCount + 1;
                                if (i2 == 0 || currentSchedule) {
                                    updates = updates2;
                                    i = 1;
                                    final TLRPC.Message newMsgObj12 = message;
                                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda47
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.this.m1204lambda$sendMessage$11$orgtelegrammessengerSendMessagesHelper(newMsgObj1, peer_id, index2, scheduleDate, sentMessages, peer, newMsgObj12, existFlags);
                                        }
                                    });
                                } else {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda15
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.this.m1210lambda$sendMessage$9$orgtelegrammessengerSendMessagesHelper(index2, newMsgObj1, sentMessages, msgObj, scheduleDate);
                                        }
                                    });
                                    updates = updates2;
                                    i = 1;
                                }
                                a12 = a13;
                                sentCount3 = sentCount5;
                            }
                        }
                    } else {
                        newMessagesByIds = newMessagesByIds2;
                        updates = updates2;
                        sentCount2 = sentCount;
                        i = 1;
                    }
                    sentCount3 = sentCount2;
                    a12 = a13;
                } else {
                    newMessagesByIds = newMessagesByIds2;
                    updates = updates2;
                    i = 1;
                }
                a12 += i;
                arrayList = newMsgObjArr;
                arrayList2 = newMsgArr;
                updates2 = updates;
                newMessagesByIds2 = newMessagesByIds;
                i2 = scheduleDate;
            }
            TLRPC.Updates updates3 = updates2;
            int sentCount6 = sentCount3;
            if (!updates3.updates.isEmpty()) {
                getMessagesController().processUpdates(updates3, false);
            }
            getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, sentCount6);
            sendMessagesHelper = this;
        } else {
            sendMessagesHelper = this;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1205lambda$sendMessage$12$orgtelegrammessengerSendMessagesHelper(error, req);
                }
            });
        }
        for (int a14 = 0; a14 < newMsgObjArr.size(); a14++) {
            final TLRPC.Message newMsgObj13 = (TLRPC.Message) newMsgObjArr.get(a14);
            getMessagesStorage().markMessageAsSendError(newMsgObj13, scheduleDate != 0);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1206lambda$sendMessage$13$orgtelegrammessengerSendMessagesHelper(newMsgObj13, scheduleDate);
                }
            });
        }
    }

    /* renamed from: lambda$sendMessage$9$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1210lambda$sendMessage$9$orgtelegrammessengerSendMessagesHelper(final int oldId, final TLRPC.Message newMsgObj1, final ArrayList sentMessages, final MessageObject msgObj, final int scheduleDate) {
        ArrayList<Integer> messageIds = new ArrayList<>();
        messageIds.add(Integer.valueOf(oldId));
        getMessagesController().deleteMessages(messageIds, null, null, newMsgObj1.dialog_id, false, true);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1209lambda$sendMessage$8$orgtelegrammessengerSendMessagesHelper(sentMessages, msgObj, newMsgObj1, oldId, scheduleDate);
            }
        });
    }

    /* renamed from: lambda$sendMessage$8$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1209lambda$sendMessage$8$orgtelegrammessengerSendMessagesHelper(ArrayList sentMessages, final MessageObject msgObj, final TLRPC.Message newMsgObj1, final int oldId, final int scheduleDate) {
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) sentMessages, true, false, false, 0, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1208lambda$sendMessage$7$orgtelegrammessengerSendMessagesHelper(msgObj, newMsgObj1, oldId, scheduleDate);
            }
        });
    }

    /* renamed from: lambda$sendMessage$7$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1208lambda$sendMessage$7$orgtelegrammessengerSendMessagesHelper(MessageObject msgObj, TLRPC.Message newMsgObj1, int oldId, int scheduleDate) {
        ArrayList<MessageObject> messageObjects = new ArrayList<>();
        boolean z = true;
        messageObjects.add(new MessageObject(msgObj.currentAccount, msgObj.messageOwner, true, true));
        getMessagesController().updateInterfaceWithMessages(newMsgObj1.dialog_id, messageObjects, false);
        getMediaDataController().increasePeerRaiting(newMsgObj1.dialog_id);
        processSentMessage(oldId);
        if (scheduleDate == 0) {
            z = false;
        }
        removeFromSendingMessages(oldId, z);
    }

    /* renamed from: lambda$sendMessage$11$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1204lambda$sendMessage$11$orgtelegrammessengerSendMessagesHelper(final TLRPC.Message newMsgObj1, TLRPC.Peer peer_id, final int oldId, final int scheduleDate, ArrayList sentMessages, final long peer, final TLRPC.Message message, final int existFlags) {
        getMessagesStorage().updateMessageStateAndId(newMsgObj1.random_id, MessageObject.getPeerId(peer_id), Integer.valueOf(oldId), newMsgObj1.id, 0, false, scheduleDate != 0 ? 1 : 0);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) sentMessages, true, false, false, 0, scheduleDate != 0);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1203lambda$sendMessage$10$orgtelegrammessengerSendMessagesHelper(newMsgObj1, peer, oldId, message, existFlags, scheduleDate);
            }
        });
    }

    /* renamed from: lambda$sendMessage$10$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1203lambda$sendMessage$10$orgtelegrammessengerSendMessagesHelper(TLRPC.Message newMsgObj1, long peer, int oldId, TLRPC.Message message, int existFlags, int scheduleDate) {
        boolean z = false;
        newMsgObj1.send_state = 0;
        getMediaDataController().increasePeerRaiting(peer);
        NotificationCenter notificationCenter = getNotificationCenter();
        int i = NotificationCenter.messageReceivedByServer;
        Object[] objArr = new Object[7];
        objArr[0] = Integer.valueOf(oldId);
        objArr[1] = Integer.valueOf(message.id);
        objArr[2] = message;
        objArr[3] = Long.valueOf(peer);
        objArr[4] = 0L;
        objArr[5] = Integer.valueOf(existFlags);
        objArr[6] = Boolean.valueOf(scheduleDate != 0);
        notificationCenter.postNotificationName(i, objArr);
        processSentMessage(oldId);
        if (scheduleDate != 0) {
            z = true;
        }
        removeFromSendingMessages(oldId, z);
    }

    /* renamed from: lambda$sendMessage$12$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1205lambda$sendMessage$12$orgtelegrammessengerSendMessagesHelper(TLRPC.TL_error error, TLRPC.TL_messages_forwardMessages req) {
        AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
    }

    /* renamed from: lambda$sendMessage$13$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1206lambda$sendMessage$13$orgtelegrammessengerSendMessagesHelper(TLRPC.Message newMsgObj1, int scheduleDate) {
        newMsgObj1.send_state = 2;
        boolean z = true;
        getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj1.id));
        processSentMessage(newMsgObj1.id);
        int i = newMsgObj1.id;
        if (scheduleDate == 0) {
            z = false;
        }
        removeFromSendingMessages(i, z);
    }

    private void writePreviousMessageData(TLRPC.Message message, SerializedData data) {
        if (message.media == null) {
            TLRPC.TL_messageMediaEmpty media = new TLRPC.TL_messageMediaEmpty();
            media.serializeToStream(data);
        } else {
            message.media.serializeToStream(data);
        }
        String str = "";
        data.writeString(message.message != null ? message.message : str);
        if (message.attachPath != null) {
            str = message.attachPath;
        }
        data.writeString(str);
        int count = message.entities.size();
        data.writeInt32(count);
        for (int a = 0; a < count; a++) {
            message.entities.get(a).serializeToStream(data);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:(1:6)(1:7)|8|388|9|(17:(25:421|11|(1:16)(2:13|14)|17|22|(11:24|(9:31|(1:33)(2:34|(7:36|37|395|38|(2:42|44)|43|44)(1:47))|49|414|50|(1:56)(1:55)|428|57|58)|48|49|414|50|(0)|56|428|57|58)(9:63|(2:65|66)|67|(2:69|70)|71|394|(3:73|74|(1:81)(1:80))(1:(6:85|(4:89|(1:92)|93|94)|90|(0)|93|94)(1:95))|96|97)|404|98|(2:424|100)|103|104|105|(3:390|108|109)|412|113|(2:115|116)|117|(1:122)(2:120|121)|(11:397|124|(5:386|126|127|411|(2:129|(1:131)(2:132|(2:134|135)(1:136)))(3:139|(2:141|142)(4:143|408|144|(2:149|(1:151))(1:148))|152))(1:157)|392|158|159|(3:161|(1:166)|167)|168|169|419|170)(1:175)|176|(1:184)(2:180|181)|185|(1:376)(15:192|(1:194)(15:195|399|(7:197|(2:199|(4:201|(3:203|204|205)|438|206)(1:207))(1:208)|209|(1:211)(3:212|(1:214)|215)|216|(1:223)(1:222)|224)(3:229|403|(14:436|231|(3:233|234|(4:236|(3:238|239|240)|439|241)(1:242))|243|430|244|(3:(4:247|434|248|(1:250))(1:251)|252|(1:254))(1:257)|258|259|(1:261)(3:262|(1:264)|265)|266|(2:268|(1:270))|271|272)(2:277|(6:279|409|280|281|(3:283|284|285)(6:288|289|422|290|(1:292)|293)|(4:295|(2:297|(1:299))|300|301)(1:302))(1:305)))|406|307|(1:309)|312|(1:314)|315|(5:317|(1:319)(1:320)|321|(1:323)(2:324|(2:326|(1:328)))|329)(1:330)|(1:332)|333|401|(5:335|426|336|337|338)(4:343|417|(1:(2:346|347)(5:348|432|349|350|351))(2:354|(1:(1:357)(1:358))(2:359|(1:361)(2:364|(1:(1:367)(1:368))(2:369|(1:(1:372)(1:373))))))|416)|400)|306|406|307|(0)|312|(0)|315|(0)(0)|(0)|333|401|(0)(0)|400)|377|440)(1:20)|412|113|(0)|117|(0)|122|(0)(0)|176|(1:178)|184|185|(1:187)|189|376|377|440)|21|22|(0)(0)|404|98|(0)|103|104|105|(3:390|108|109)) */
    /* JADX WARN: Can't wrap try/catch for region: R(8:24|(4:(9:31|(1:33)(2:34|(7:36|37|395|38|(2:42|44)|43|44)(1:47))|49|414|50|(1:56)(1:55)|428|57|58)|428|57|58)|48|49|414|50|(0)|56) */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0229, code lost:
        if (r32.type != 2) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004c, code lost:
        if (org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r6.layer) >= 101) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x085e, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x085f, code lost:
        r28 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0109, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x010a, code lost:
        r28 = r5;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:115:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0069 A[Catch: Exception -> 0x0054, TRY_ENTER, TryCatch #19 {Exception -> 0x0054, blocks: (B:11:0x002e, B:13:0x0042, B:24:0x0069, B:26:0x0071, B:28:0x0077, B:31:0x0080, B:33:0x0088, B:34:0x0095, B:36:0x009d, B:65:0x012c, B:69:0x014c, B:76:0x017f, B:78:0x0185, B:80:0x018b), top: B:421:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0688 A[Catch: Exception -> 0x0691, TRY_ENTER, TRY_LEAVE, TryCatch #20 {Exception -> 0x0691, blocks: (B:290:0x0609, B:292:0x061f, B:295:0x062b, B:297:0x063f, B:299:0x064c, B:300:0x0650, B:309:0x0688, B:314:0x069b, B:317:0x06ad, B:321:0x06c2, B:323:0x06c8, B:324:0x06d6, B:326:0x06eb, B:328:0x06f1, B:329:0x06fb, B:332:0x0705), top: B:422:0x0609 }] */
    /* JADX WARN: Removed duplicated region for block: B:314:0x069b A[Catch: Exception -> 0x0691, TRY_ENTER, TRY_LEAVE, TryCatch #20 {Exception -> 0x0691, blocks: (B:290:0x0609, B:292:0x061f, B:295:0x062b, B:297:0x063f, B:299:0x064c, B:300:0x0650, B:309:0x0688, B:314:0x069b, B:317:0x06ad, B:321:0x06c2, B:323:0x06c8, B:324:0x06d6, B:326:0x06eb, B:328:0x06f1, B:329:0x06fb, B:332:0x0705), top: B:422:0x0609 }] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x06ad A[Catch: Exception -> 0x0691, TRY_ENTER, TryCatch #20 {Exception -> 0x0691, blocks: (B:290:0x0609, B:292:0x061f, B:295:0x062b, B:297:0x063f, B:299:0x064c, B:300:0x0650, B:309:0x0688, B:314:0x069b, B:317:0x06ad, B:321:0x06c2, B:323:0x06c8, B:324:0x06d6, B:326:0x06eb, B:328:0x06f1, B:329:0x06fb, B:332:0x0705), top: B:422:0x0609 }] */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0701  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0705 A[Catch: Exception -> 0x0691, TRY_LEAVE, TryCatch #20 {Exception -> 0x0691, blocks: (B:290:0x0609, B:292:0x061f, B:295:0x062b, B:297:0x063f, B:299:0x064c, B:300:0x0650, B:309:0x0688, B:314:0x069b, B:317:0x06ad, B:321:0x06c2, B:323:0x06c8, B:324:0x06d6, B:326:0x06eb, B:328:0x06f1, B:329:0x06fb, B:332:0x0705), top: B:422:0x0609 }] */
    /* JADX WARN: Removed duplicated region for block: B:335:0x070b  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x074f  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0250 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:424:0x020c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0118 A[Catch: Exception -> 0x0870, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x0870, blocks: (B:9:0x0022, B:63:0x0118, B:67:0x0132, B:71:0x0152, B:96:0x01f6), top: B:388:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01e7 A[Catch: Exception -> 0x01b5, TryCatch #4 {Exception -> 0x01b5, blocks: (B:73:0x0169, B:81:0x018f, B:85:0x01c6, B:92:0x01e7, B:93:0x01f0), top: B:394:0x0167 }] */
    /* JADX WARN: Type inference failed for: r1v74, types: [org.telegram.tgnet.TLRPC$TL_inputMediaPhoto] */
    /* JADX WARN: Type inference failed for: r20v12 */
    /* JADX WARN: Type inference failed for: r20v13 */
    /* JADX WARN: Type inference failed for: r20v16 */
    /* JADX WARN: Type inference failed for: r20v17 */
    /* JADX WARN: Type inference failed for: r20v18 */
    /* JADX WARN: Type inference failed for: r22v13 */
    /* JADX WARN: Type inference failed for: r22v14 */
    /* JADX WARN: Type inference failed for: r22v16 */
    /* JADX WARN: Type inference failed for: r22v17 */
    /* JADX WARN: Type inference failed for: r22v18 */
    /* JADX WARN: Type inference failed for: r22v19 */
    /* JADX WARN: Type inference failed for: r22v20 */
    /* JADX WARN: Type inference failed for: r22v3 */
    /* JADX WARN: Type inference failed for: r22v6 */
    /* JADX WARN: Type inference failed for: r22v8 */
    /* JADX WARN: Type inference failed for: r25v12 */
    /* JADX WARN: Type inference failed for: r25v13 */
    /* JADX WARN: Type inference failed for: r25v14 */
    /* JADX WARN: Type inference failed for: r25v2 */
    /* JADX WARN: Type inference failed for: r25v22 */
    /* JADX WARN: Type inference failed for: r25v23 */
    /* JADX WARN: Type inference failed for: r25v24 */
    /* JADX WARN: Type inference failed for: r25v27 */
    /* JADX WARN: Type inference failed for: r25v28 */
    /* JADX WARN: Type inference failed for: r25v5 */
    /* JADX WARN: Type inference failed for: r25v7 */
    /* JADX WARN: Type inference failed for: r2v34, types: [org.telegram.tgnet.TLRPC$TL_inputMediaDocument] */
    /* JADX WARN: Type inference failed for: r31v0, types: [org.telegram.messenger.SendMessagesHelper] */
    /* JADX WARN: Type inference failed for: r34v1 */
    /* JADX WARN: Type inference failed for: r34v2 */
    /* JADX WARN: Type inference failed for: r34v6 */
    /* JADX WARN: Type inference failed for: r34v7 */
    /* JADX WARN: Type inference failed for: r39v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v35, types: [org.telegram.tgnet.TLRPC$TL_inputMediaDocument] */
    /* JADX WARN: Type inference failed for: r8v14, types: [org.telegram.messenger.MediaDataController] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void editMessage(MessageObject messageObject, TLRPC.TL_photo photo, VideoEditedInfo videoEditedInfo, TLRPC.TL_document document, String path, HashMap<String, String> params, boolean retry, Object obj) {
        HashMap<String, String> params2;
        Exception e;
        long peer;
        int type;
        boolean supportsSendingNewEntities;
        DelayedMessage delayedMessage;
        TLRPC.TL_photo photo2;
        TLRPC.TL_document document2;
        String path2;
        String str;
        HashMap<String, String> params3;
        int type2;
        VideoEditedInfo videoEditedInfo2;
        TLRPC.TL_document document3;
        VideoEditedInfo videoEditedInfo3;
        TLRPC.TL_document document4;
        boolean z;
        ArrayList<MessageObject> arrayList;
        boolean supportsSendingNewEntities2;
        ?? r22;
        TLRPC.TL_inputMediaUploadedDocument photo3;
        String path3;
        String str2;
        HashMap<String, String> params4;
        String path4;
        HashMap<String, String> params5;
        boolean performMediaUpload;
        TLRPC.TL_photo photo4;
        TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto;
        DelayedMessage delayedMessage2;
        TLRPC.TL_document document5;
        boolean supportsSendingNewEntities3;
        ?? r20;
        HashMap<String, String> params6;
        TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument;
        ?? r25;
        VideoEditedInfo videoEditedInfo4;
        boolean performMediaUpload2;
        TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument2;
        boolean performMediaUpload3;
        TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument3;
        HashMap<String, String> params7;
        TLRPC.TL_photo tL_photo;
        int i;
        boolean performMediaUpload4;
        char c;
        TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto2;
        boolean performMediaUpload5;
        int type3;
        int type4;
        VideoEditedInfo videoEditedInfo5;
        int type5;
        TLRPC.TL_photo photo5 = photo;
        TLRPC.TL_document document6 = document;
        if (messageObject == null) {
            return;
        }
        HashMap<String, String> params8 = params == null ? new HashMap<>() : params;
        TLRPC.Message newMsg = messageObject.messageOwner;
        messageObject.cancelEditing = false;
        try {
            peer = messageObject.getDialogId();
        } catch (Exception e2) {
            e = e2;
            params2 = params8;
        }
        try {
            if (DialogObject.isEncryptedDialog(peer)) {
                try {
                    int encryptedId = DialogObject.getEncryptedChatId(peer);
                    type = 65535;
                    TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedId));
                    if (encryptedChat == null) {
                    }
                    supportsSendingNewEntities = false;
                    if (!retry) {
                        try {
                            if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && messageObject.messageOwner.media != null && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaEmpty)) {
                                if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                    photo5 = (TLRPC.TL_photo) messageObject.messageOwner.media.photo;
                                    type5 = 2;
                                    videoEditedInfo5 = videoEditedInfo;
                                } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                    document6 = (TLRPC.TL_document) messageObject.messageOwner.media.document;
                                    try {
                                        if (!MessageObject.isVideoDocument(document6) && videoEditedInfo == null) {
                                            type5 = 7;
                                            videoEditedInfo5 = messageObject.videoEditedInfo;
                                        }
                                        type5 = 3;
                                        videoEditedInfo5 = messageObject.videoEditedInfo;
                                    } catch (Exception e3) {
                                        e = e3;
                                        params2 = params8;
                                        FileLog.e(e);
                                        revertEditingMessageObject(messageObject);
                                        return;
                                    }
                                } else {
                                    videoEditedInfo5 = videoEditedInfo;
                                    type5 = type;
                                }
                                TLRPC.TL_photo photo6 = photo5;
                                params8 = newMsg.params;
                                String str3 = (obj == 0 || params8 == null || !params8.containsKey("parentObject")) ? obj : params8.get("parentObject");
                                messageObject.editingMessage = newMsg.message;
                                messageObject.editingMessageEntities = newMsg.entities;
                                params3 = params8;
                                delayedMessage = null;
                                str = str3;
                                document2 = document6;
                                videoEditedInfo2 = videoEditedInfo5;
                                photo2 = photo6;
                                int i2 = type5;
                                path2 = newMsg.attachPath;
                                type2 = i2;
                            }
                            messageObject.editingMessage = newMsg.message;
                            messageObject.editingMessageEntities = newMsg.entities;
                            params3 = params8;
                            delayedMessage = null;
                            str = str3;
                            document2 = document6;
                            videoEditedInfo2 = videoEditedInfo5;
                            photo2 = photo6;
                            int i22 = type5;
                            path2 = newMsg.attachPath;
                            type2 = i22;
                        } catch (Exception e4) {
                            e = e4;
                            params2 = params8;
                            FileLog.e(e);
                            revertEditingMessageObject(messageObject);
                            return;
                        }
                        type5 = 1;
                        videoEditedInfo5 = videoEditedInfo;
                        TLRPC.TL_photo photo62 = photo5;
                        params8 = newMsg.params;
                        if (obj == 0) {
                        }
                    } else {
                        messageObject.previousMedia = newMsg.media;
                        messageObject.previousMessage = newMsg.message;
                        messageObject.previousMessageEntities = newMsg.entities;
                        messageObject.previousAttachPath = newMsg.attachPath;
                        TLRPC.MessageMedia media = newMsg.media;
                        if (media == null) {
                            media = new TLRPC.TL_messageMediaEmpty();
                        }
                        SerializedData serializedDataCalc = new SerializedData(true);
                        writePreviousMessageData(newMsg, serializedDataCalc);
                        SerializedData prevMessageData = new SerializedData(serializedDataCalc.length());
                        writePreviousMessageData(newMsg, prevMessageData);
                        if (params8 == null) {
                            params8 = new HashMap<>();
                        }
                        delayedMessage = null;
                        params8.put("prevMedia", Base64.encodeToString(prevMessageData.toByteArray(), 0));
                        prevMessageData.cleanup();
                        try {
                            if (photo5 != null) {
                                newMsg.media = new TLRPC.TL_messageMediaPhoto();
                                newMsg.media.flags |= 3;
                                newMsg.media.photo = photo5;
                                type3 = 2;
                                if (path == null || path.length() <= 0 || !path.startsWith("http")) {
                                    TLRPC.FileLocation location1 = photo5.sizes.get(photo5.sizes.size() - 1).location;
                                    newMsg.attachPath = FileLoader.getInstance(this.currentAccount).getPathToAttach(location1, true).toString();
                                } else {
                                    newMsg.attachPath = path;
                                }
                            } else if (document6 != null) {
                                newMsg.media = new TLRPC.TL_messageMediaDocument();
                                newMsg.media.flags |= 3;
                                newMsg.media.document = document6;
                                if (!MessageObject.isVideoDocument(document) && videoEditedInfo == null) {
                                    type4 = 7;
                                    if (videoEditedInfo != null) {
                                        String ve = videoEditedInfo.getString();
                                        params8.put("ve", ve);
                                    }
                                    newMsg.attachPath = path;
                                    type3 = type4;
                                }
                                type4 = 3;
                                if (videoEditedInfo != null) {
                                }
                                newMsg.attachPath = path;
                                type3 = type4;
                            } else {
                                type3 = 1;
                            }
                            newMsg.params = params8;
                            newMsg.send_state = 3;
                            photo2 = photo;
                            videoEditedInfo2 = videoEditedInfo;
                            document2 = document6;
                            params3 = params8;
                            str = obj;
                            int i3 = type3;
                            path2 = path;
                            type2 = i3;
                        } catch (Exception e5) {
                            e = e5;
                            params2 = params8;
                            FileLog.e(e);
                            revertEditingMessageObject(messageObject);
                            return;
                        }
                    }
                    if (newMsg.attachPath == null) {
                        try {
                            newMsg.attachPath = "";
                        } catch (Exception e6) {
                            e = e6;
                            params2 = params3;
                            FileLog.e(e);
                            revertEditingMessageObject(messageObject);
                            return;
                        }
                    }
                    newMsg.local_id = 0;
                    document3 = document2;
                    if (messageObject.type != 3 && videoEditedInfo2 == null) {
                        try {
                        } catch (Exception e7) {
                            e = e7;
                            params2 = params3;
                            FileLog.e(e);
                            revertEditingMessageObject(messageObject);
                            return;
                        }
                    }
                    if (!TextUtils.isEmpty(newMsg.attachPath)) {
                        messageObject.attachPathExists = true;
                    }
                    VideoEditedInfo videoEditedInfo6 = (messageObject.videoEditedInfo == null && videoEditedInfo2 == null) ? messageObject.videoEditedInfo : videoEditedInfo2;
                    if (retry) {
                        try {
                            if (messageObject.editingMessage != null) {
                                try {
                                    String oldMessge = newMsg.message;
                                    newMsg.message = messageObject.editingMessage.toString();
                                    messageObject.caption = null;
                                    try {
                                        if (type2 != 1) {
                                            if (messageObject.editingMessageEntities != null) {
                                                newMsg.entities = messageObject.editingMessageEntities;
                                                newMsg.flags |= 128;
                                                videoEditedInfo3 = videoEditedInfo6;
                                            } else {
                                                videoEditedInfo3 = videoEditedInfo6;
                                                try {
                                                    CharSequence[] message = {messageObject.editingMessage};
                                                    ArrayList<TLRPC.MessageEntity> entities = getMediaDataController().getEntities(message, supportsSendingNewEntities);
                                                    if (entities != null && !entities.isEmpty()) {
                                                        newMsg.entities = entities;
                                                        newMsg.flags |= 128;
                                                    } else if (!TextUtils.equals(oldMessge, newMsg.message)) {
                                                        newMsg.flags &= -129;
                                                    }
                                                } catch (Exception e8) {
                                                    e = e8;
                                                    params2 = params3;
                                                    FileLog.e(e);
                                                    revertEditingMessageObject(messageObject);
                                                    return;
                                                }
                                            }
                                            messageObject.generateCaption();
                                        } else if (messageObject.editingMessageEntities != null) {
                                            newMsg.entities = messageObject.editingMessageEntities;
                                            newMsg.flags |= 128;
                                            videoEditedInfo3 = videoEditedInfo6;
                                        } else if (!TextUtils.equals(oldMessge, newMsg.message)) {
                                            newMsg.flags &= -129;
                                            videoEditedInfo3 = videoEditedInfo6;
                                        } else {
                                            videoEditedInfo3 = videoEditedInfo6;
                                        }
                                    } catch (Exception e9) {
                                        e = e9;
                                        params2 = params3;
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    params2 = params3;
                                }
                            } else {
                                videoEditedInfo3 = videoEditedInfo6;
                            }
                            try {
                                ArrayList<TLRPC.Message> arr = new ArrayList<>();
                                arr.add(newMsg);
                                MessagesStorage messagesStorage = getMessagesStorage();
                                document4 = 1;
                                i = 1;
                                i = 1;
                                document4 = 1;
                                document4 = 1;
                                r25 = 1;
                                z = messageObject.scheduled;
                                messagesStorage.putMessages(arr, false, true, false, 0, z);
                                messageObject.type = -1;
                                messageObject.setType();
                                if (type2 == 1) {
                                    if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument)) {
                                        messageObject.resetLayout();
                                        messageObject.checkLayout();
                                    }
                                    messageObject.generateCaption();
                                }
                                messageObject.createMessageSendInfo();
                                arrayList = new ArrayList<>();
                                arrayList.add(messageObject);
                                supportsSendingNewEntities2 = supportsSendingNewEntities;
                            } catch (Exception e11) {
                                e = e11;
                                params2 = params3;
                                FileLog.e(e);
                                revertEditingMessageObject(messageObject);
                                return;
                            }
                        } catch (Exception e12) {
                            e = e12;
                            params2 = params3;
                        }
                        try {
                            r22 = 0;
                            getNotificationCenter().postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(peer), arrayList);
                        } catch (Exception e13) {
                            e = e13;
                            params2 = params3;
                            FileLog.e(e);
                            revertEditingMessageObject(messageObject);
                            return;
                        }
                    } else {
                        videoEditedInfo3 = videoEditedInfo6;
                        supportsSendingNewEntities2 = supportsSendingNewEntities;
                    }
                    String originalPath = (params3 != null || !params3.containsKey("originalPath")) ? null : params3.get("originalPath");
                    if ((type2 >= 1 || type2 > 3) && (type2 < 5 || type2 > '\b')) {
                        params4 = params3;
                        path3 = path2;
                        photo3 = photo2;
                        document4 = document3;
                        str2 = str;
                    } else {
                        try {
                            try {
                                if (type2 == 1) {
                                    performMediaUpload = false;
                                    tL_inputMediaUploadedDocument = null;
                                    params6 = params3;
                                    photo4 = photo2;
                                    document5 = document3;
                                    path4 = path2;
                                } else {
                                    path3 = 0;
                                    path3 = 0;
                                    path3 = 0;
                                    path3 = 0;
                                    r22 = 0;
                                    try {
                                        try {
                                            if (type2 == 2) {
                                                TLRPC.TL_inputMediaUploadedPhoto uploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                                                if (params3 != null) {
                                                    String masks = params3.get("masks");
                                                    if (masks != null) {
                                                        performMediaUpload4 = false;
                                                        SerializedData serializedData = new SerializedData(Utilities.hexToBytes(masks));
                                                        int count = serializedData.readInt32(false);
                                                        tL_photo = null;
                                                        int a = 0;
                                                        while (a < count) {
                                                            int count2 = count;
                                                            HashMap<String, String> params9 = params3;
                                                            String masks2 = masks;
                                                            uploadedPhoto.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData, serializedData.readInt32(false), false));
                                                            a++;
                                                            count = count2;
                                                            masks = masks2;
                                                            params3 = params9;
                                                        }
                                                        params7 = params3;
                                                        i = count;
                                                        int a2 = uploadedPhoto.flags;
                                                        uploadedPhoto.flags = a2 | 1;
                                                        serializedData.cleanup();
                                                    } else {
                                                        performMediaUpload4 = false;
                                                        tL_photo = null;
                                                        params7 = params3;
                                                    }
                                                } else {
                                                    performMediaUpload4 = false;
                                                    tL_photo = null;
                                                    params7 = params3;
                                                }
                                                if (photo2.access_hash == 0) {
                                                    c = type2;
                                                    tL_inputMediaUploadedPhoto2 = uploadedPhoto;
                                                    performMediaUpload5 = true;
                                                } else {
                                                    ?? tL_inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                                                    tL_inputMediaPhoto.id = new TLRPC.TL_inputPhoto();
                                                    c = type2;
                                                    tL_inputMediaPhoto.id.id = photo2.id;
                                                    tL_inputMediaPhoto.id.access_hash = photo2.access_hash;
                                                    tL_inputMediaPhoto.id.file_reference = photo2.file_reference;
                                                    if (tL_inputMediaPhoto.id.file_reference == null) {
                                                        tL_inputMediaPhoto.id.file_reference = new byte[0];
                                                    }
                                                    tL_inputMediaUploadedPhoto2 = tL_inputMediaPhoto;
                                                    performMediaUpload5 = performMediaUpload4;
                                                }
                                                DelayedMessage delayedMessage3 = new DelayedMessage(peer);
                                                delayedMessage3.type = 0;
                                                delayedMessage3.obj = messageObject;
                                                delayedMessage3.originalPath = originalPath;
                                                delayedMessage3.parentObject = str;
                                                delayedMessage3.inputUploadMedia = uploadedPhoto;
                                                delayedMessage3.performMediaUpload = performMediaUpload5;
                                                if (path2 == null || path2.length() <= 0 || !path2.startsWith("http")) {
                                                    delayedMessage3.photoSize = photo2.sizes.get(photo2.sizes.size() - 1);
                                                    delayedMessage3.locationParent = photo2;
                                                } else {
                                                    delayedMessage3.httpLocation = path2;
                                                }
                                                performMediaUpload = performMediaUpload5;
                                                photo4 = photo2;
                                                document5 = document3;
                                                delayedMessage2 = delayedMessage3;
                                                path4 = path2;
                                                type2 = c;
                                                tL_inputMediaUploadedPhoto = tL_inputMediaUploadedPhoto2;
                                                document4 = i;
                                                photo3 = tL_photo;
                                                params5 = params7;
                                            } else {
                                                performMediaUpload = false;
                                                tL_inputMediaUploadedDocument = null;
                                                params5 = params3;
                                                try {
                                                    if (type2 == 3) {
                                                        try {
                                                            TLRPC.TL_inputMediaUploadedDocument uploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
                                                            HashMap<String, String> params10 = params5;
                                                            if (params5 != null) {
                                                                HashMap<String, String> params11 = params5;
                                                                String masks3 = params11.get("masks");
                                                                if (masks3 != null) {
                                                                    SerializedData serializedData2 = new SerializedData(Utilities.hexToBytes(masks3));
                                                                    int count3 = serializedData2.readInt32(false);
                                                                    int a3 = 0;
                                                                    while (a3 < count3) {
                                                                        String masks4 = masks3;
                                                                        HashMap<String, String> params12 = params11;
                                                                        uploadedDocument.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData2, serializedData2.readInt32(false), false));
                                                                        a3++;
                                                                        masks3 = masks4;
                                                                        params11 = params12;
                                                                    }
                                                                    r25 = masks3;
                                                                    params10 = params11;
                                                                    uploadedDocument.flags |= 1;
                                                                    serializedData2.cleanup();
                                                                } else {
                                                                    r25 = masks3;
                                                                    params10 = params11;
                                                                }
                                                            }
                                                            document5 = document3;
                                                            try {
                                                                uploadedDocument.mime_type = document5.mime_type;
                                                                uploadedDocument.attributes = document5.attributes;
                                                                if (!messageObject.isGif()) {
                                                                    if (videoEditedInfo3 != null) {
                                                                        videoEditedInfo4 = videoEditedInfo3;
                                                                        try {
                                                                            if (!videoEditedInfo4.muted) {
                                                                            }
                                                                        } catch (Exception e14) {
                                                                            e = e14;
                                                                            params2 = params10;
                                                                            FileLog.e(e);
                                                                            revertEditingMessageObject(messageObject);
                                                                            return;
                                                                        }
                                                                    } else {
                                                                        videoEditedInfo4 = videoEditedInfo3;
                                                                    }
                                                                    uploadedDocument.nosound_video = true;
                                                                    if (BuildVars.DEBUG_VERSION) {
                                                                        FileLog.d("nosound_video = true");
                                                                    }
                                                                } else {
                                                                    videoEditedInfo4 = videoEditedInfo3;
                                                                }
                                                                path4 = path2;
                                                                if (document5.access_hash == 0) {
                                                                    tL_inputMediaUploadedDocument2 = uploadedDocument;
                                                                    performMediaUpload2 = true;
                                                                } else {
                                                                    ?? tL_inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                                                                    tL_inputMediaDocument.id = new TLRPC.TL_inputDocument();
                                                                    tL_inputMediaDocument.id.id = document5.id;
                                                                    tL_inputMediaDocument.id.access_hash = document5.access_hash;
                                                                    tL_inputMediaDocument.id.file_reference = document5.file_reference;
                                                                    if (tL_inputMediaDocument.id.file_reference == null) {
                                                                        tL_inputMediaDocument.id.file_reference = new byte[0];
                                                                    }
                                                                    performMediaUpload2 = false;
                                                                    tL_inputMediaUploadedDocument2 = tL_inputMediaDocument;
                                                                }
                                                                DelayedMessage delayedMessage4 = new DelayedMessage(peer);
                                                                delayedMessage4.type = 1;
                                                                delayedMessage4.obj = messageObject;
                                                                delayedMessage4.originalPath = originalPath;
                                                                delayedMessage4.parentObject = str;
                                                                delayedMessage4.inputUploadMedia = uploadedDocument;
                                                                delayedMessage4.performMediaUpload = performMediaUpload2;
                                                                if (!document5.thumbs.isEmpty()) {
                                                                    TLRPC.PhotoSize photoSize = document5.thumbs.get(0);
                                                                    if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                                                                        delayedMessage4.photoSize = photoSize;
                                                                        delayedMessage4.locationParent = document5;
                                                                    }
                                                                }
                                                                delayedMessage4.videoEditedInfo = videoEditedInfo4;
                                                                performMediaUpload = performMediaUpload2;
                                                                photo4 = photo2;
                                                                tL_inputMediaUploadedPhoto = tL_inputMediaUploadedDocument2;
                                                                delayedMessage2 = delayedMessage4;
                                                                document4 = r25;
                                                                photo3 = tL_inputMediaUploadedDocument;
                                                                params5 = params10;
                                                            } catch (Exception e15) {
                                                                e = e15;
                                                                params2 = params10;
                                                            }
                                                        } catch (Exception e16) {
                                                            e = e16;
                                                            params2 = params5;
                                                        }
                                                    } else {
                                                        path4 = path2;
                                                        document5 = document3;
                                                        if (type2 == 7) {
                                                            try {
                                                                TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument4 = new TLRPC.TL_inputMediaUploadedDocument();
                                                                tL_inputMediaUploadedDocument4.mime_type = document5.mime_type;
                                                                tL_inputMediaUploadedDocument4.attributes = document5.attributes;
                                                                if (document5.access_hash == 0) {
                                                                    tL_inputMediaUploadedDocument3 = tL_inputMediaUploadedDocument4;
                                                                    performMediaUpload3 = tL_inputMediaUploadedDocument4 instanceof TLRPC.TL_inputMediaUploadedDocument;
                                                                    photo4 = photo2;
                                                                } else {
                                                                    ?? tL_inputMediaDocument2 = new TLRPC.TL_inputMediaDocument();
                                                                    tL_inputMediaDocument2.id = new TLRPC.TL_inputDocument();
                                                                    photo4 = photo2;
                                                                    try {
                                                                        tL_inputMediaDocument2.id.id = document5.id;
                                                                        tL_inputMediaDocument2.id.access_hash = document5.access_hash;
                                                                        tL_inputMediaDocument2.id.file_reference = document5.file_reference;
                                                                        if (tL_inputMediaDocument2.id.file_reference == null) {
                                                                            tL_inputMediaDocument2.id.file_reference = new byte[0];
                                                                        }
                                                                        performMediaUpload3 = false;
                                                                        tL_inputMediaUploadedDocument3 = tL_inputMediaDocument2;
                                                                    } catch (Exception e17) {
                                                                        e = e17;
                                                                        params2 = params5;
                                                                        FileLog.e(e);
                                                                        revertEditingMessageObject(messageObject);
                                                                        return;
                                                                    }
                                                                }
                                                                if (0 == 0) {
                                                                    DelayedMessage delayedMessage5 = new DelayedMessage(peer);
                                                                    delayedMessage5.originalPath = originalPath;
                                                                    delayedMessage5.type = 2;
                                                                    delayedMessage5.obj = messageObject;
                                                                    if (!document5.thumbs.isEmpty()) {
                                                                        TLRPC.PhotoSize photoSize2 = document5.thumbs.get(0);
                                                                        if (!(photoSize2 instanceof TLRPC.TL_photoStrippedSize)) {
                                                                            delayedMessage5.photoSize = photoSize2;
                                                                            delayedMessage5.locationParent = document5;
                                                                        }
                                                                    }
                                                                    delayedMessage5.parentObject = str;
                                                                    delayedMessage5.inputUploadMedia = tL_inputMediaUploadedDocument4;
                                                                    delayedMessage5.performMediaUpload = performMediaUpload3;
                                                                    tL_inputMediaUploadedPhoto = tL_inputMediaUploadedDocument3;
                                                                    performMediaUpload = performMediaUpload3;
                                                                    delayedMessage2 = delayedMessage5;
                                                                    photo3 = tL_inputMediaUploadedDocument;
                                                                    params5 = params5;
                                                                } else {
                                                                    tL_inputMediaUploadedPhoto = tL_inputMediaUploadedDocument3;
                                                                    performMediaUpload = performMediaUpload3;
                                                                    delayedMessage2 = delayedMessage;
                                                                    photo3 = tL_inputMediaUploadedDocument;
                                                                    params5 = params5;
                                                                }
                                                            } catch (Exception e18) {
                                                                e = e18;
                                                                params2 = params5;
                                                                FileLog.e(e);
                                                                revertEditingMessageObject(messageObject);
                                                                return;
                                                            }
                                                        } else {
                                                            photo4 = photo2;
                                                            params6 = params5;
                                                        }
                                                    }
                                                } catch (Exception e19) {
                                                    e = e19;
                                                    params2 = params5;
                                                }
                                            }
                                            TLRPC.TL_messages_editMessage request = new TLRPC.TL_messages_editMessage();
                                            request.id = messageObject.getId();
                                            request.peer = getMessagesController().getInputPeer(peer);
                                            if (tL_inputMediaUploadedPhoto != null) {
                                                request.flags |= 16384;
                                                request.media = tL_inputMediaUploadedPhoto;
                                            }
                                            if (messageObject.scheduled) {
                                                request.schedule_date = messageObject.messageOwner.date;
                                                request.flags |= 32768;
                                            }
                                            if (messageObject.editingMessage == null) {
                                                request.message = messageObject.editingMessage.toString();
                                                request.flags |= 2048;
                                                request.no_webpage = !messageObject.editingMessageSearchWebPage;
                                                if (messageObject.editingMessageEntities != null) {
                                                    request.entities = messageObject.editingMessageEntities;
                                                    request.flags |= 8;
                                                    supportsSendingNewEntities3 = supportsSendingNewEntities2;
                                                    r20 = supportsSendingNewEntities2;
                                                } else {
                                                    CharSequence[] message2 = {messageObject.editingMessage};
                                                    supportsSendingNewEntities3 = supportsSendingNewEntities2;
                                                    ArrayList<TLRPC.MessageEntity> entities2 = getMediaDataController().getEntities(message2, supportsSendingNewEntities3);
                                                    r20 = supportsSendingNewEntities2;
                                                    if (entities2 != null) {
                                                        r20 = supportsSendingNewEntities2;
                                                        if (!entities2.isEmpty()) {
                                                            request.entities = entities2;
                                                            r20 = 8;
                                                            request.flags |= 8;
                                                        }
                                                    }
                                                }
                                                messageObject.editingMessage = null;
                                                messageObject.editingMessageEntities = null;
                                                str2 = r20;
                                            } else {
                                                supportsSendingNewEntities3 = supportsSendingNewEntities2;
                                                str2 = supportsSendingNewEntities2;
                                            }
                                            if (delayedMessage2 != null) {
                                                delayedMessage2.sendRequest = request;
                                            }
                                            if (type2 != 1) {
                                                try {
                                                    TLRPC.TL_photo photo7 = photo4;
                                                    params4 = params5;
                                                    str2 = str;
                                                    path3 = path4;
                                                    document4 = document5;
                                                    performSendMessageRequest(request, messageObject, null, delayedMessage2, str2, params4, messageObject.scheduled);
                                                    photo3 = photo7;
                                                    videoEditedInfo = photo7;
                                                } catch (Exception e20) {
                                                    e = e20;
                                                    params2 = params5;
                                                    FileLog.e(e);
                                                    revertEditingMessageObject(messageObject);
                                                    return;
                                                }
                                            } else {
                                                path3 = path4;
                                                str2 = str;
                                                document4 = document5;
                                                TLRPC.TL_photo photo8 = photo4;
                                                params4 = params5;
                                                int type6 = type2;
                                                try {
                                                    if (type6 != 2) {
                                                        photo3 = photo8;
                                                        DelayedMessage delayedMessage6 = delayedMessage2;
                                                        if (type6 == 3) {
                                                            if (performMediaUpload) {
                                                                performSendDelayedMessage(delayedMessage6);
                                                                photo3 = photo3;
                                                            } else {
                                                                performSendMessageRequest(request, messageObject, originalPath, delayedMessage6, str2, params4, messageObject.scheduled);
                                                                photo3 = photo3;
                                                            }
                                                        } else if (type6 == 6) {
                                                            performSendMessageRequest(request, messageObject, originalPath, delayedMessage6, str2, params4, messageObject.scheduled);
                                                            photo3 = photo3;
                                                        } else if (type6 == 7) {
                                                            if (performMediaUpload) {
                                                                performSendDelayedMessage(delayedMessage6);
                                                                photo3 = photo3;
                                                            } else {
                                                                performSendMessageRequest(request, messageObject, originalPath, delayedMessage6, str2, params4, messageObject.scheduled);
                                                                photo3 = photo3;
                                                            }
                                                        } else if (type6 == 8) {
                                                            if (performMediaUpload) {
                                                                performSendDelayedMessage(delayedMessage6);
                                                                photo3 = photo3;
                                                            } else {
                                                                performSendMessageRequest(request, messageObject, originalPath, delayedMessage6, str2, params4, messageObject.scheduled);
                                                                photo3 = photo3;
                                                            }
                                                        }
                                                    } else if (performMediaUpload) {
                                                        performSendDelayedMessage(delayedMessage2);
                                                        photo3 = photo8;
                                                        videoEditedInfo = photo8;
                                                    } else {
                                                        try {
                                                            photo3 = photo8;
                                                            performSendMessageRequest(request, messageObject, originalPath, null, true, delayedMessage2, str2, params4, messageObject.scheduled);
                                                        } catch (Exception e21) {
                                                            e = e21;
                                                            params2 = params4;
                                                            FileLog.e(e);
                                                            revertEditingMessageObject(messageObject);
                                                            return;
                                                        }
                                                    }
                                                } catch (Exception e22) {
                                                    e = e22;
                                                    params2 = params4;
                                                }
                                            }
                                        } catch (Exception e23) {
                                            e = e23;
                                            params2 = z;
                                        }
                                    } catch (Exception e24) {
                                        e = e24;
                                        params2 = params3;
                                    }
                                }
                                if (type2 != 1) {
                                }
                            } catch (Exception e25) {
                                e = e25;
                                params2 = params4;
                            }
                            TLRPC.TL_messages_editMessage request2 = new TLRPC.TL_messages_editMessage();
                            request2.id = messageObject.getId();
                            request2.peer = getMessagesController().getInputPeer(peer);
                            if (tL_inputMediaUploadedPhoto != null) {
                            }
                            if (messageObject.scheduled) {
                            }
                            if (messageObject.editingMessage == null) {
                            }
                            if (delayedMessage2 != null) {
                            }
                        } catch (Exception e26) {
                            e = e26;
                            params2 = params5;
                        }
                        delayedMessage2 = delayedMessage;
                        tL_inputMediaUploadedPhoto = tL_inputMediaUploadedDocument;
                        path3 = r22;
                        photo3 = tL_inputMediaUploadedDocument;
                        params5 = params6;
                    }
                    return;
                } catch (Exception e27) {
                    e = e27;
                    params2 = params8;
                    FileLog.e(e);
                    revertEditingMessageObject(messageObject);
                    return;
                }
            }
            type = -1;
            if (!TextUtils.isEmpty(newMsg.attachPath)) {
            }
            if (messageObject.videoEditedInfo == null) {
            }
            if (retry) {
            }
            if (params3 != null) {
            }
            if (type2 >= 1) {
            }
            params4 = params3;
            path3 = path2;
            photo3 = photo2;
            document4 = document3;
            str2 = str;
            return;
        } catch (Exception e28) {
            e = e28;
            params2 = params3;
            FileLog.e(e);
            revertEditingMessageObject(messageObject);
            return;
        }
        supportsSendingNewEntities = true;
        if (!retry) {
        }
        if (newMsg.attachPath == null) {
        }
        newMsg.local_id = 0;
        document3 = document2;
        if (messageObject.type != 3) {
        }
    }

    public int editMessage(MessageObject messageObject, String message, boolean searchLinks, final BaseFragment fragment, ArrayList<TLRPC.MessageEntity> entities, int scheduleDate) {
        if (fragment == null || fragment.getParentActivity() == null) {
            return 0;
        }
        final TLRPC.TL_messages_editMessage req = new TLRPC.TL_messages_editMessage();
        req.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (message != null) {
            req.message = message;
            req.flags |= 2048;
            req.no_webpage = !searchLinks;
        }
        req.id = messageObject.getId();
        if (entities != null) {
            req.entities = entities;
            req.flags |= 8;
        }
        if (scheduleDate != 0) {
            req.schedule_date = scheduleDate;
            req.flags |= 32768;
        }
        return getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda86
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1161lambda$editMessage$16$orgtelegrammessengerSendMessagesHelper(fragment, req, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$editMessage$16$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1161lambda$editMessage$16$orgtelegrammessengerSendMessagesHelper(final BaseFragment fragment, final TLRPC.TL_messages_editMessage req, TLObject response, final TLRPC.TL_error error) {
        if (error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) response, false);
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1160lambda$editMessage$15$orgtelegrammessengerSendMessagesHelper(error, fragment, req);
                }
            });
        }
    }

    /* renamed from: lambda$editMessage$15$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1160lambda$editMessage$15$orgtelegrammessengerSendMessagesHelper(TLRPC.TL_error error, BaseFragment fragment, TLRPC.TL_messages_editMessage req) {
        AlertsCreator.processError(this.currentAccount, error, fragment, req, new Object[0]);
    }

    public void sendLocation(Location location) {
        TLRPC.TL_messageMediaGeo mediaGeo = new TLRPC.TL_messageMediaGeo();
        mediaGeo.geo = new TLRPC.TL_geoPoint();
        mediaGeo.geo.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
        mediaGeo.geo._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
        for (Map.Entry<String, MessageObject> entry : this.waitingForLocation.entrySet()) {
            MessageObject messageObject = entry.getValue();
            sendMessage((TLRPC.MessageMedia) mediaGeo, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
        }
    }

    public void sendCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton button) {
        if (messageObject == null || button == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(button.data));
        sb.append("_");
        sb.append(button instanceof TLRPC.TL_keyboardButtonGame ? IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE : "0");
        String key = sb.toString();
        this.waitingForLocation.put(key, messageObject);
        this.locationProvider.start();
    }

    public boolean isSendingCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton button) {
        if (messageObject == null || button == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(button.data));
        sb.append("_");
        sb.append(button instanceof TLRPC.TL_keyboardButtonGame ? IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE : "0");
        String key = sb.toString();
        return this.waitingForLocation.containsKey(key);
    }

    public void sendNotificationCallback(final long dialogId, final int msgId, final byte[] data) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1213xb2d0ec92(dialogId, msgId, data);
            }
        });
    }

    /* renamed from: lambda$sendNotificationCallback$19$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1213xb2d0ec92(long dialogId, int msgId, byte[] data) {
        TLRPC.Chat chat;
        TLRPC.User user;
        final String key = dialogId + "_" + msgId + "_" + Utilities.bytesToHex(data) + "_0";
        this.waitingForCallback.put(key, true);
        if (DialogObject.isUserDialog(dialogId)) {
            if (getMessagesController().getUser(Long.valueOf(dialogId)) == null && (user = getMessagesStorage().getUserSync(dialogId)) != null) {
                getMessagesController().putUser(user, true);
            }
        } else if (getMessagesController().getChat(Long.valueOf(-dialogId)) == null && (chat = getMessagesStorage().getChatSync(-dialogId)) != null) {
            getMessagesController().putChat(chat, true);
        }
        TLRPC.TL_messages_getBotCallbackAnswer req = new TLRPC.TL_messages_getBotCallbackAnswer();
        req.peer = getMessagesController().getInputPeer(dialogId);
        req.msg_id = msgId;
        req.game = false;
        if (data != null) {
            req.flags |= 1;
            req.data = data;
        }
        getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda79
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1212xcd8f7dd1(key, tLObject, tL_error);
            }
        }, 2);
        getMessagesController().markDialogAsRead(dialogId, msgId, msgId, 0, false, 0, 0, true, 0);
    }

    /* renamed from: lambda$sendNotificationCallback$17$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1211xe84e0f10(String key) {
        this.waitingForCallback.remove(key);
    }

    /* renamed from: lambda$sendNotificationCallback$18$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1212xcd8f7dd1(final String key, TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1211xe84e0f10(key);
            }
        });
    }

    public byte[] isSendingVote(MessageObject messageObject) {
        if (messageObject == null) {
            return null;
        }
        String key = "poll_" + messageObject.getPollId();
        return this.waitingForVote.get(key);
    }

    public int sendVote(final MessageObject messageObject, ArrayList<TLRPC.TL_pollAnswer> answers, final Runnable finishRunnable) {
        byte[] options;
        if (messageObject == null) {
            return 0;
        }
        final String key = "poll_" + messageObject.getPollId();
        if (this.waitingForCallback.containsKey(key)) {
            return 0;
        }
        TLRPC.TL_messages_sendVote req = new TLRPC.TL_messages_sendVote();
        req.msg_id = messageObject.getId();
        req.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (answers != null) {
            options = new byte[answers.size()];
            for (int a = 0; a < answers.size(); a++) {
                TLRPC.TL_pollAnswer answer = answers.get(a);
                req.options.add(answer.option);
                options[a] = answer.option[0];
            }
        } else {
            options = new byte[0];
        }
        this.waitingForVote.put(key, options);
        return getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda82
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1218lambda$sendVote$21$orgtelegrammessengerSendMessagesHelper(messageObject, key, finishRunnable, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$sendVote$21$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1218lambda$sendVote$21$orgtelegrammessengerSendMessagesHelper(MessageObject messageObject, final String key, final Runnable finishRunnable, TLObject response, TLRPC.TL_error error) {
        if (error == null) {
            this.voteSendTime.put(messageObject.getPollId(), 0L);
            getMessagesController().processUpdates((TLRPC.Updates) response, false);
            this.voteSendTime.put(messageObject.getPollId(), Long.valueOf(SystemClock.elapsedRealtime()));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1217lambda$sendVote$20$orgtelegrammessengerSendMessagesHelper(key, finishRunnable);
            }
        });
    }

    /* renamed from: lambda$sendVote$20$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1217lambda$sendVote$20$orgtelegrammessengerSendMessagesHelper(String key, Runnable finishRunnable) {
        this.waitingForVote.remove(key);
        if (finishRunnable != null) {
            finishRunnable.run();
        }
    }

    public long getVoteSendTime(long pollId) {
        return this.voteSendTime.get(pollId, 0L).longValue();
    }

    public void sendReaction(MessageObject messageObject, CharSequence reaction, boolean big, ChatActivity parentFragment, final Runnable callback) {
        if (messageObject == null || parentFragment == null) {
            return;
        }
        TLRPC.TL_messages_sendReaction req = new TLRPC.TL_messages_sendReaction();
        if (messageObject.messageOwner.isThreadMessage && messageObject.messageOwner.fwd_from != null) {
            req.peer = getMessagesController().getInputPeer(messageObject.getFromChatId());
            req.msg_id = messageObject.messageOwner.fwd_from.saved_from_msg_id;
        } else {
            req.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            req.msg_id = messageObject.getId();
        }
        if (reaction != null) {
            req.reaction = reaction.toString();
            req.flags |= 1;
        }
        if (big) {
            req.flags |= 2;
            req.big = true;
        }
        getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda78
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1214lambda$sendReaction$22$orgtelegrammessengerSendMessagesHelper(callback, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$sendReaction$22$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1214lambda$sendReaction$22$orgtelegrammessengerSendMessagesHelper(Runnable callback, TLObject response, TLRPC.TL_error error) {
        if (response != null) {
            getMessagesController().processUpdates((TLRPC.Updates) response, false);
            if (callback != null) {
                AndroidUtilities.runOnUIThread(callback);
            }
        }
    }

    public void requestUrlAuth(final String url, final ChatActivity parentFragment, final boolean ask) {
        final TLRPC.TL_messages_requestUrlAuth req = new TLRPC.TL_messages_requestUrlAuth();
        req.url = url;
        req.flags |= 4;
        getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.lambda$requestUrlAuth$23(ChatActivity.this, req, url, ask, tLObject, tL_error);
            }
        }, 2);
    }

    public static /* synthetic */ void lambda$requestUrlAuth$23(ChatActivity parentFragment, TLRPC.TL_messages_requestUrlAuth req, String url, boolean ask, TLObject response, TLRPC.TL_error error) {
        if (response != null) {
            if (response instanceof TLRPC.TL_urlAuthResultRequest) {
                TLRPC.TL_urlAuthResultRequest res = (TLRPC.TL_urlAuthResultRequest) response;
                parentFragment.showRequestUrlAlert(res, req, url, ask);
                return;
            } else if (response instanceof TLRPC.TL_urlAuthResultAccepted) {
                TLRPC.TL_urlAuthResultAccepted res2 = (TLRPC.TL_urlAuthResultAccepted) response;
                AlertsCreator.showOpenUrlAlert(parentFragment, res2.url, false, false);
                return;
            } else if (response instanceof TLRPC.TL_urlAuthResultDefault) {
                AlertsCreator.showOpenUrlAlert(parentFragment, url, false, ask);
                return;
            } else {
                return;
            }
        }
        AlertsCreator.showOpenUrlAlert(parentFragment, url, false, ask);
    }

    public void sendCallback(boolean cache, MessageObject messageObject, TLRPC.KeyboardButton button, ChatActivity parentFragment) {
        m1196lambda$sendCallback$24$orgtelegrammessengerSendMessagesHelper(cache, messageObject, button, null, null, parentFragment);
    }

    /* renamed from: sendCallback */
    public void m1196lambda$sendCallback$24$orgtelegrammessengerSendMessagesHelper(final boolean cache, final MessageObject messageObject, final TLRPC.KeyboardButton button, final TLRPC.InputCheckPasswordSRP srp, final TwoStepVerificationActivity passwordFragment, final ChatActivity parentFragment) {
        int type;
        boolean cacheFinal;
        if (messageObject == null || button == null || parentFragment == null) {
            return;
        }
        if (button instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            cacheFinal = false;
            type = 3;
        } else if (button instanceof TLRPC.TL_keyboardButtonGame) {
            cacheFinal = false;
            type = 1;
        } else if (button instanceof TLRPC.TL_keyboardButtonBuy) {
            cacheFinal = cache;
            type = 2;
        } else {
            cacheFinal = cache;
            type = 0;
        }
        final String key = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + type;
        this.waitingForCallback.put(key, true);
        final TLObject[] request = new TLObject[1];
        final boolean z = cacheFinal;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda80
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1201lambda$sendCallback$30$orgtelegrammessengerSendMessagesHelper(key, z, messageObject, button, parentFragment, passwordFragment, request, srp, cache, tLObject, tL_error);
            }
        };
        if (cacheFinal) {
            getMessagesStorage().getBotCache(key, requestDelegate);
        } else if (button instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            TLRPC.TL_messages_requestUrlAuth req = new TLRPC.TL_messages_requestUrlAuth();
            req.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            req.msg_id = messageObject.getId();
            req.button_id = button.button_id;
            req.flags |= 2;
            request[0] = req;
            getConnectionsManager().sendRequest(req, requestDelegate, 2);
        } else if (button instanceof TLRPC.TL_keyboardButtonBuy) {
            if ((messageObject.messageOwner.media.flags & 4) == 0) {
                TLRPC.TL_payments_getPaymentForm req2 = new TLRPC.TL_payments_getPaymentForm();
                TLRPC.TL_inputInvoiceMessage inputInvoice = new TLRPC.TL_inputInvoiceMessage();
                inputInvoice.msg_id = messageObject.getId();
                inputInvoice.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                req2.invoice = inputInvoice;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("bg_color", Theme.getColor(Theme.key_windowBackgroundWhite));
                    jsonObject.put("text_color", Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    jsonObject.put("hint_color", Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                    jsonObject.put("link_color", Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
                    jsonObject.put("button_color", Theme.getColor(Theme.key_featuredStickers_addButton));
                    jsonObject.put("button_text_color", Theme.getColor(Theme.key_featuredStickers_buttonText));
                    req2.theme_params = new TLRPC.TL_dataJSON();
                    req2.theme_params.data = jsonObject.toString();
                    req2.flags |= 1;
                } catch (Exception e) {
                    FileLog.e(e);
                }
                getConnectionsManager().sendRequest(req2, requestDelegate, 2);
                return;
            }
            TLRPC.TL_payments_getPaymentReceipt req3 = new TLRPC.TL_payments_getPaymentReceipt();
            req3.msg_id = messageObject.messageOwner.media.receipt_msg_id;
            req3.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
            getConnectionsManager().sendRequest(req3, requestDelegate, 2);
        } else {
            TLRPC.TL_messages_getBotCallbackAnswer req4 = new TLRPC.TL_messages_getBotCallbackAnswer();
            req4.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            req4.msg_id = messageObject.getId();
            req4.game = button instanceof TLRPC.TL_keyboardButtonGame;
            if (button.requires_password) {
                TLRPC.InputCheckPasswordSRP tL_inputCheckPasswordEmpty = srp != null ? srp : new TLRPC.TL_inputCheckPasswordEmpty();
                req4.password = tL_inputCheckPasswordEmpty;
                req4.password = tL_inputCheckPasswordEmpty;
                req4.flags |= 4;
            }
            if (button.data != null) {
                req4.flags |= 1;
                req4.data = button.data;
            }
            getConnectionsManager().sendRequest(req4, requestDelegate, 2);
        }
    }

    /* renamed from: lambda$sendCallback$30$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1201lambda$sendCallback$30$orgtelegrammessengerSendMessagesHelper(final String key, final boolean cacheFinal, final MessageObject messageObject, final TLRPC.KeyboardButton button, final ChatActivity parentFragment, final TwoStepVerificationActivity passwordFragment, final TLObject[] request, final TLRPC.InputCheckPasswordSRP srp, final boolean cache, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1200lambda$sendCallback$29$orgtelegrammessengerSendMessagesHelper(key, cacheFinal, response, messageObject, button, parentFragment, passwordFragment, request, error, srp, cache);
            }
        });
    }

    /* renamed from: lambda$sendCallback$29$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1200lambda$sendCallback$29$orgtelegrammessengerSendMessagesHelper(String key, boolean cacheFinal, TLObject response, final MessageObject messageObject, final TLRPC.KeyboardButton button, final ChatActivity parentFragment, final TwoStepVerificationActivity passwordFragment, TLObject[] request, TLRPC.TL_error error, TLRPC.InputCheckPasswordSRP srp, final boolean cache) {
        long uid;
        String name;
        boolean z;
        this.waitingForCallback.remove(key);
        if (cacheFinal && response == null) {
            sendCallback(false, messageObject, button, parentFragment);
        } else if (response != null) {
            if (passwordFragment != null) {
                passwordFragment.needHideProgress();
                passwordFragment.finishFragment();
            }
            long uid2 = messageObject.getFromChatId();
            if (messageObject.messageOwner.via_bot_id == 0) {
                uid = uid2;
            } else {
                long uid3 = messageObject.messageOwner.via_bot_id;
                uid = uid3;
            }
            String name2 = null;
            if (uid > 0) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(uid));
                if (user != null) {
                    name2 = ContactsController.formatName(user.first_name, user.last_name);
                }
            } else {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-uid));
                if (chat != null) {
                    name2 = chat.title;
                }
            }
            if (name2 != null) {
                name = name2;
            } else {
                name = "bot";
            }
            if (button instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                if (response instanceof TLRPC.TL_urlAuthResultRequest) {
                    parentFragment.showRequestUrlAlert((TLRPC.TL_urlAuthResultRequest) response, (TLRPC.TL_messages_requestUrlAuth) request[0], button.url, false);
                } else if (response instanceof TLRPC.TL_urlAuthResultAccepted) {
                    AlertsCreator.showOpenUrlAlert(parentFragment, ((TLRPC.TL_urlAuthResultAccepted) response).url, false, false);
                } else if (response instanceof TLRPC.TL_urlAuthResultDefault) {
                    TLRPC.TL_urlAuthResultDefault tL_urlAuthResultDefault = (TLRPC.TL_urlAuthResultDefault) response;
                    AlertsCreator.showOpenUrlAlert(parentFragment, button.url, false, true);
                }
            } else if (button instanceof TLRPC.TL_keyboardButtonBuy) {
                if (response instanceof TLRPC.TL_payments_paymentForm) {
                    TLRPC.TL_payments_paymentForm form = (TLRPC.TL_payments_paymentForm) response;
                    getMessagesController().putUsers(form.users, false);
                    parentFragment.presentFragment(new PaymentFormActivity(form, messageObject, parentFragment));
                } else if (response instanceof TLRPC.TL_payments_paymentReceipt) {
                    parentFragment.presentFragment(new PaymentFormActivity((TLRPC.TL_payments_paymentReceipt) response));
                }
            } else {
                TLRPC.TL_messages_botCallbackAnswer res = (TLRPC.TL_messages_botCallbackAnswer) response;
                if (!cacheFinal && res.cache_time != 0 && !button.requires_password) {
                    getMessagesStorage().saveBotCache(key, res);
                }
                if (res.message != null) {
                    if (res.alert) {
                        if (parentFragment.getParentActivity() == null) {
                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(parentFragment.getParentActivity());
                        builder.setTitle(name);
                        builder.setPositiveButton(LocaleController.getString("OK", org.telegram.messenger.beta.R.string.OK), null);
                        builder.setMessage(res.message);
                        parentFragment.showDialog(builder.create());
                        return;
                    }
                    parentFragment.showAlert(name, res.message);
                } else if (res.url == null || parentFragment.getParentActivity() == null) {
                } else {
                    TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(uid));
                    boolean verified = user2 != null && user2.verified;
                    if (button instanceof TLRPC.TL_keyboardButtonGame) {
                        TLRPC.TL_game game = messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGame ? messageObject.messageOwner.media.game : null;
                        if (game == null) {
                            return;
                        }
                        String str = res.url;
                        if (!verified) {
                            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                            if (notificationsSettings.getBoolean("askgame_" + uid, true)) {
                                z = true;
                                parentFragment.showOpenGameAlert(game, messageObject, str, z, uid);
                                return;
                            }
                        }
                        z = false;
                        parentFragment.showOpenGameAlert(game, messageObject, str, z, uid);
                        return;
                    }
                    AlertsCreator.showOpenUrlAlert(parentFragment, res.url, false, false);
                }
            }
        } else if (error == null || parentFragment.getParentActivity() == null) {
        } else {
            if ("PASSWORD_HASH_INVALID".equals(error.text)) {
                if (srp == null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(parentFragment.getParentActivity());
                    builder2.setTitle(LocaleController.getString("BotOwnershipTransfer", org.telegram.messenger.beta.R.string.BotOwnershipTransfer));
                    builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferReadyAlertText", org.telegram.messenger.beta.R.string.BotOwnershipTransferReadyAlertText, new Object[0])));
                    builder2.setPositiveButton(LocaleController.getString("BotOwnershipTransferChangeOwner", org.telegram.messenger.beta.R.string.BotOwnershipTransferChangeOwner), new DialogInterface.OnClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            SendMessagesHelper.this.m1197lambda$sendCallback$25$orgtelegrammessengerSendMessagesHelper(cache, messageObject, button, parentFragment, dialogInterface, i);
                        }
                    });
                    builder2.setNegativeButton(LocaleController.getString("Cancel", org.telegram.messenger.beta.R.string.Cancel), null);
                    parentFragment.showDialog(builder2.create());
                }
            } else if ("PASSWORD_MISSING".equals(error.text) || error.text.startsWith("PASSWORD_TOO_FRESH_") || error.text.startsWith("SESSION_TOO_FRESH_")) {
                if (passwordFragment != null) {
                    passwordFragment.needHideProgress();
                }
                AlertDialog.Builder builder3 = new AlertDialog.Builder(parentFragment.getParentActivity());
                builder3.setTitle(LocaleController.getString("EditAdminTransferAlertTitle", org.telegram.messenger.beta.R.string.EditAdminTransferAlertTitle));
                LinearLayout linearLayout = new LinearLayout(parentFragment.getParentActivity());
                linearLayout.setPadding(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(24.0f), 0);
                linearLayout.setOrientation(1);
                builder3.setView(linearLayout);
                TextView messageTextView = new TextView(parentFragment.getParentActivity());
                messageTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                messageTextView.setTextSize(1, 16.0f);
                messageTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferAlertText", org.telegram.messenger.beta.R.string.BotOwnershipTransferAlertText, new Object[0])));
                linearLayout.addView(messageTextView, LayoutHelper.createLinear(-1, -2));
                LinearLayout linearLayout2 = new LinearLayout(parentFragment.getParentActivity());
                linearLayout2.setOrientation(0);
                linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
                ImageView dotImageView = new ImageView(parentFragment.getParentActivity());
                dotImageView.setImageResource(org.telegram.messenger.beta.R.drawable.list_circle);
                dotImageView.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
                dotImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogTextBlack), PorterDuff.Mode.MULTIPLY));
                TextView messageTextView2 = new TextView(parentFragment.getParentActivity());
                messageTextView2.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                messageTextView2.setTextSize(1, 16.0f);
                messageTextView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                messageTextView2.setText(AndroidUtilities.replaceTags(LocaleController.getString("EditAdminTransferAlertText1", org.telegram.messenger.beta.R.string.EditAdminTransferAlertText1)));
                if (LocaleController.isRTL) {
                    linearLayout2.addView(messageTextView2, LayoutHelper.createLinear(-1, -2));
                    linearLayout2.addView(dotImageView, LayoutHelper.createLinear(-2, -2, 5));
                } else {
                    linearLayout2.addView(dotImageView, LayoutHelper.createLinear(-2, -2));
                    linearLayout2.addView(messageTextView2, LayoutHelper.createLinear(-1, -2));
                }
                LinearLayout linearLayout22 = new LinearLayout(parentFragment.getParentActivity());
                linearLayout22.setOrientation(0);
                linearLayout.addView(linearLayout22, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
                ImageView dotImageView2 = new ImageView(parentFragment.getParentActivity());
                dotImageView2.setImageResource(org.telegram.messenger.beta.R.drawable.list_circle);
                dotImageView2.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
                dotImageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogTextBlack), PorterDuff.Mode.MULTIPLY));
                TextView messageTextView3 = new TextView(parentFragment.getParentActivity());
                messageTextView3.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                messageTextView3.setTextSize(1, 16.0f);
                messageTextView3.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                messageTextView3.setText(AndroidUtilities.replaceTags(LocaleController.getString("EditAdminTransferAlertText2", org.telegram.messenger.beta.R.string.EditAdminTransferAlertText2)));
                if (LocaleController.isRTL) {
                    linearLayout22.addView(messageTextView3, LayoutHelper.createLinear(-1, -2));
                    linearLayout22.addView(dotImageView2, LayoutHelper.createLinear(-2, -2, 5));
                } else {
                    linearLayout22.addView(dotImageView2, LayoutHelper.createLinear(-2, -2));
                    linearLayout22.addView(messageTextView3, LayoutHelper.createLinear(-1, -2));
                }
                if ("PASSWORD_MISSING".equals(error.text)) {
                    builder3.setPositiveButton(LocaleController.getString("EditAdminTransferSetPassword", org.telegram.messenger.beta.R.string.EditAdminTransferSetPassword), new DialogInterface.OnClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda11
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            ChatActivity.this.presentFragment(new TwoStepVerificationSetupActivity(6, null));
                        }
                    });
                    builder3.setNegativeButton(LocaleController.getString("Cancel", org.telegram.messenger.beta.R.string.Cancel), null);
                } else {
                    TextView messageTextView4 = new TextView(parentFragment.getParentActivity());
                    messageTextView4.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                    messageTextView4.setTextSize(1, 16.0f);
                    messageTextView4.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    messageTextView4.setText(LocaleController.getString("EditAdminTransferAlertText3", org.telegram.messenger.beta.R.string.EditAdminTransferAlertText3));
                    linearLayout.addView(messageTextView4, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
                    builder3.setNegativeButton(LocaleController.getString("OK", org.telegram.messenger.beta.R.string.OK), null);
                }
                parentFragment.showDialog(builder3.create());
            } else if ("SRP_ID_INVALID".equals(error.text)) {
                TLRPC.TL_account_getPassword getPasswordReq = new TLRPC.TL_account_getPassword();
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(getPasswordReq, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda87
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        SendMessagesHelper.this.m1199lambda$sendCallback$28$orgtelegrammessengerSendMessagesHelper(passwordFragment, cache, messageObject, button, parentFragment, tLObject, tL_error);
                    }
                }, 8);
            } else if (passwordFragment != null) {
                passwordFragment.needHideProgress();
                passwordFragment.finishFragment();
            }
        }
    }

    /* renamed from: lambda$sendCallback$25$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1197lambda$sendCallback$25$orgtelegrammessengerSendMessagesHelper(final boolean cache, final MessageObject messageObject, final TLRPC.KeyboardButton button, final ChatActivity parentFragment, DialogInterface dialogInterface, int i) {
        final TwoStepVerificationActivity fragment = new TwoStepVerificationActivity();
        fragment.setDelegate(new TwoStepVerificationActivity.TwoStepVerificationActivityDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda90
            @Override // org.telegram.ui.TwoStepVerificationActivity.TwoStepVerificationActivityDelegate
            public final void didEnterPassword(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP) {
                SendMessagesHelper.this.m1196lambda$sendCallback$24$orgtelegrammessengerSendMessagesHelper(cache, messageObject, button, fragment, parentFragment, inputCheckPasswordSRP);
            }
        });
        parentFragment.presentFragment(fragment);
    }

    /* renamed from: lambda$sendCallback$28$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1199lambda$sendCallback$28$orgtelegrammessengerSendMessagesHelper(final TwoStepVerificationActivity passwordFragment, final boolean cache, final MessageObject messageObject, final TLRPC.KeyboardButton button, final ChatActivity parentFragment, final TLObject response2, final TLRPC.TL_error error2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1198lambda$sendCallback$27$orgtelegrammessengerSendMessagesHelper(error2, response2, passwordFragment, cache, messageObject, button, parentFragment);
            }
        });
    }

    /* renamed from: lambda$sendCallback$27$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1198lambda$sendCallback$27$orgtelegrammessengerSendMessagesHelper(TLRPC.TL_error error2, TLObject response2, TwoStepVerificationActivity passwordFragment, boolean cache, MessageObject messageObject, TLRPC.KeyboardButton button, ChatActivity parentFragment) {
        if (error2 == null) {
            TLRPC.TL_account_password currentPassword = (TLRPC.TL_account_password) response2;
            passwordFragment.setCurrentPasswordInfo(null, currentPassword);
            TwoStepVerificationActivity.initPasswordNewAlgo(currentPassword);
            m1196lambda$sendCallback$24$orgtelegrammessengerSendMessagesHelper(cache, messageObject, button, passwordFragment.getNewSrpPassword(), passwordFragment, parentFragment);
        }
    }

    public boolean isSendingCallback(MessageObject messageObject, TLRPC.KeyboardButton button) {
        int type;
        if (messageObject == null || button == null) {
            return false;
        }
        if (button instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            type = 3;
        } else if (button instanceof TLRPC.TL_keyboardButtonGame) {
            type = 1;
        } else if (button instanceof TLRPC.TL_keyboardButtonBuy) {
            type = 2;
        } else {
            type = 0;
        }
        String key = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + type;
        return this.waitingForCallback.containsKey(key);
    }

    public void sendGame(TLRPC.InputPeer peer, TLRPC.TL_inputMediaGame game, long random_id, long taskId) {
        final long newTaskId;
        if (peer == null || game == null) {
            return;
        }
        TLRPC.TL_messages_sendMedia request = new TLRPC.TL_messages_sendMedia();
        request.peer = peer;
        if (request.peer instanceof TLRPC.TL_inputPeerChannel) {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            request.silent = notificationsSettings.getBoolean("silent_" + (-peer.channel_id), false);
        } else if (request.peer instanceof TLRPC.TL_inputPeerChat) {
            SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
            request.silent = notificationsSettings2.getBoolean("silent_" + (-peer.chat_id), false);
        } else {
            SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(this.currentAccount);
            request.silent = notificationsSettings3.getBoolean("silent_" + peer.user_id, false);
        }
        request.random_id = random_id != 0 ? random_id : getNextRandomId();
        request.message = "";
        request.media = game;
        long fromId = ChatObject.getSendAsPeerId(getMessagesController().getChat(Long.valueOf(peer.chat_id)), getMessagesController().getChatFull(peer.chat_id));
        if (fromId != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            request.send_as = getMessagesController().getInputPeer(fromId);
        }
        if (taskId == 0) {
            NativeByteBuffer data = null;
            try {
                data = new NativeByteBuffer(peer.getObjectSize() + game.getObjectSize() + 4 + 8);
                data.writeInt32(3);
                data.writeInt64(random_id);
                peer.serializeToStream(data);
                game.serializeToStream(data);
            } catch (Exception e) {
                FileLog.e(e);
            }
            newTaskId = getMessagesStorage().createPendingTask(data);
        } else {
            newTaskId = taskId;
        }
        getConnectionsManager().sendRequest(request, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda75
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1202lambda$sendGame$31$orgtelegrammessengerSendMessagesHelper(newTaskId, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$sendGame$31$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1202lambda$sendGame$31$orgtelegrammessengerSendMessagesHelper(long newTaskId, TLObject response, TLRPC.TL_error error) {
        if (error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) response, false);
        }
        if (newTaskId != 0) {
            getMessagesStorage().removePendingTask(newTaskId);
        }
    }

    public void sendMessage(MessageObject retryMessageObject) {
        sendMessage(null, null, null, null, null, null, null, null, null, null, retryMessageObject.getDialogId(), retryMessageObject.messageOwner.attachPath, null, null, null, true, retryMessageObject, null, retryMessageObject.messageOwner.reply_markup, retryMessageObject.messageOwner.params, !retryMessageObject.messageOwner.silent, retryMessageObject.scheduled ? retryMessageObject.messageOwner.date : 0, 0, null, null);
    }

    public void sendMessage(TLRPC.User user, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate) {
        sendMessage(null, null, null, null, null, user, null, null, null, null, peer, null, replyToMsg, replyToTopMsg, null, true, null, null, replyMarkup, params, notify, scheduleDate, 0, null, null);
    }

    public void sendMessage(TLRPC.TL_messageMediaInvoice invoice, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate) {
        sendMessage(null, null, null, null, null, null, null, null, null, invoice, peer, null, replyToMsg, replyToTopMsg, null, true, null, null, replyMarkup, params, notify, scheduleDate, 0, null, null);
    }

    public void sendMessage(TLRPC.TL_document document, VideoEditedInfo videoEditedInfo, String path, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, String caption, ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate, int ttl, Object parentObject, MessageObject.SendAnimationData sendAnimationData) {
        sendMessage(null, caption, null, null, videoEditedInfo, null, document, null, null, null, peer, path, replyToMsg, replyToTopMsg, null, true, null, entities, replyMarkup, params, notify, scheduleDate, ttl, parentObject, sendAnimationData);
    }

    public void sendMessage(String message, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.WebPage webPage, boolean searchLinks, ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate, MessageObject.SendAnimationData sendAnimationData) {
        sendMessage(message, null, null, null, null, null, null, null, null, null, peer, null, replyToMsg, replyToTopMsg, webPage, searchLinks, null, entities, replyMarkup, params, notify, scheduleDate, 0, null, sendAnimationData);
    }

    public void sendMessage(TLRPC.MessageMedia location, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate) {
        sendMessage(null, null, location, null, null, null, null, null, null, null, peer, null, replyToMsg, replyToTopMsg, null, true, null, null, replyMarkup, params, notify, scheduleDate, 0, null, null);
    }

    public void sendMessage(TLRPC.TL_messageMediaPoll poll, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate) {
        sendMessage(null, null, null, null, null, null, null, null, poll, null, peer, null, replyToMsg, replyToTopMsg, null, true, null, null, replyMarkup, params, notify, scheduleDate, 0, null, null);
    }

    public void sendMessage(TLRPC.TL_game game, long peer, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate) {
        sendMessage(null, null, null, null, null, null, null, game, null, null, peer, null, null, null, null, true, null, null, replyMarkup, params, notify, scheduleDate, 0, null, null);
    }

    public void sendMessage(TLRPC.TL_photo photo, String path, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, String caption, ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate, int ttl, Object parentObject) {
        sendMessage(null, caption, null, photo, null, null, null, null, null, null, peer, path, replyToMsg, replyToTopMsg, null, true, null, entities, replyMarkup, params, notify, scheduleDate, ttl, parentObject, null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:694:0x1512
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:92)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:44)
        */
    private void sendMessage(java.lang.String r91, java.lang.String r92, org.telegram.tgnet.TLRPC.MessageMedia r93, org.telegram.tgnet.TLRPC.TL_photo r94, org.telegram.messenger.VideoEditedInfo r95, org.telegram.tgnet.TLRPC.User r96, org.telegram.tgnet.TLRPC.TL_document r97, org.telegram.tgnet.TLRPC.TL_game r98, org.telegram.tgnet.TLRPC.TL_messageMediaPoll r99, org.telegram.tgnet.TLRPC.TL_messageMediaInvoice r100, long r101, java.lang.String r103, org.telegram.messenger.MessageObject r104, org.telegram.messenger.MessageObject r105, org.telegram.tgnet.TLRPC.WebPage r106, boolean r107, org.telegram.messenger.MessageObject r108, java.util.ArrayList<org.telegram.tgnet.TLRPC.MessageEntity> r109, org.telegram.tgnet.TLRPC.ReplyMarkup r110, java.util.HashMap<java.lang.String, java.lang.String> r111, boolean r112, int r113, int r114, java.lang.Object r115, org.telegram.messenger.MessageObject.SendAnimationData r116) {
        /*
            Method dump skipped, instructions count: 17456
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.sendMessage(java.lang.String, java.lang.String, org.telegram.tgnet.TLRPC$MessageMedia, org.telegram.tgnet.TLRPC$TL_photo, org.telegram.messenger.VideoEditedInfo, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$TL_document, org.telegram.tgnet.TLRPC$TL_game, org.telegram.tgnet.TLRPC$TL_messageMediaPoll, org.telegram.tgnet.TLRPC$TL_messageMediaInvoice, long, java.lang.String, org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$WebPage, boolean, org.telegram.messenger.MessageObject, java.util.ArrayList, org.telegram.tgnet.TLRPC$ReplyMarkup, java.util.HashMap, boolean, int, int, java.lang.Object, org.telegram.messenger.MessageObject$SendAnimationData):void");
    }

    private void performSendDelayedMessage(DelayedMessage message) {
        performSendDelayedMessage(message, -1);
    }

    private TLRPC.PhotoSize getThumbForSecretChat(ArrayList<TLRPC.PhotoSize> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        int N = arrayList.size();
        for (int a = 0; a < N; a++) {
            TLRPC.PhotoSize size = arrayList.get(a);
            if (size != null && !(size instanceof TLRPC.TL_photoStrippedSize) && !(size instanceof TLRPC.TL_photoPathSize) && !(size instanceof TLRPC.TL_photoSizeEmpty) && size.location != null) {
                TLRPC.TL_photoSize photoSize = new TLRPC.TL_photoSize_layer127();
                photoSize.type = size.type;
                photoSize.w = size.w;
                photoSize.h = size.h;
                photoSize.size = size.size;
                photoSize.bytes = size.bytes;
                if (photoSize.bytes == null) {
                    photoSize.bytes = new byte[0];
                }
                photoSize.location = new TLRPC.TL_fileLocation_layer82();
                photoSize.location.dc_id = size.location.dc_id;
                photoSize.location.volume_id = size.location.volume_id;
                photoSize.location.local_id = size.location.local_id;
                photoSize.location.secret = size.location.secret;
                return photoSize;
            }
        }
        return null;
    }

    private void performSendDelayedMessage(final DelayedMessage message, int index) {
        int index2;
        TLObject inputMedia;
        MessageObject messageObject;
        TLRPC.InputMedia media;
        TLRPC.InputMedia media2;
        TLRPC.InputMedia media3;
        boolean z = false;
        boolean z2 = true;
        if (message.type == 0) {
            if (message.httpLocation != null) {
                putToDelayedMessages(message.httpLocation, message);
                ImageLoader.getInstance().loadHttpFile(message.httpLocation, "file", this.currentAccount);
            } else if (message.sendRequest != null) {
                String location = FileLoader.getInstance(this.currentAccount).getPathToAttach(message.photoSize).toString();
                putToDelayedMessages(location, message);
                getFileLoader().uploadFile(location, false, true, 16777216);
                putToUploadingMessages(message.obj);
            } else {
                String location2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(message.photoSize).toString();
                if (message.sendEncryptedRequest != null && message.photoSize.location.dc_id != 0) {
                    File file = new File(location2);
                    if (!file.exists()) {
                        location2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(message.photoSize, true).toString();
                        file = new File(location2);
                    }
                    if (!file.exists()) {
                        putToDelayedMessages(FileLoader.getAttachFileName(message.photoSize), message);
                        getFileLoader().loadFile(ImageLocation.getForObject(message.photoSize, message.locationParent), message.parentObject, "jpg", 2, 0);
                        return;
                    }
                }
                putToDelayedMessages(location2, message);
                getFileLoader().uploadFile(location2, true, true, 16777216);
                putToUploadingMessages(message.obj);
            }
        } else if (message.type == 1) {
            if (message.videoEditedInfo != null && message.videoEditedInfo.needConvert()) {
                String location3 = message.obj.messageOwner.attachPath;
                TLRPC.Document document = message.obj.getDocument();
                if (location3 == null) {
                    location3 = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                }
                putToDelayedMessages(location3, message);
                MediaController.getInstance().scheduleVideoConvert(message.obj);
                putToUploadingMessages(message.obj);
            } else {
                if (message.videoEditedInfo != null) {
                    if (message.videoEditedInfo.file != null) {
                        if (message.sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                            media3 = ((TLRPC.TL_messages_sendMedia) message.sendRequest).media;
                        } else {
                            media3 = ((TLRPC.TL_messages_editMessage) message.sendRequest).media;
                        }
                        media3.file = message.videoEditedInfo.file;
                        message.videoEditedInfo.file = null;
                    } else if (message.videoEditedInfo.encryptedFile != null) {
                        TLRPC.TL_decryptedMessage decryptedMessage = (TLRPC.TL_decryptedMessage) message.sendEncryptedRequest;
                        decryptedMessage.media.size = (int) message.videoEditedInfo.estimatedSize;
                        decryptedMessage.media.key = message.videoEditedInfo.key;
                        decryptedMessage.media.iv = message.videoEditedInfo.iv;
                        getSecretChatHelper().performSendEncryptedRequest(decryptedMessage, message.obj.messageOwner, message.encryptedChat, message.videoEditedInfo.encryptedFile, message.originalPath, message.obj);
                        message.videoEditedInfo.encryptedFile = null;
                        return;
                    }
                }
                if (message.sendRequest != null) {
                    if (message.sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                        media2 = ((TLRPC.TL_messages_sendMedia) message.sendRequest).media;
                    } else {
                        media2 = ((TLRPC.TL_messages_editMessage) message.sendRequest).media;
                    }
                    if (media2.file == null) {
                        String location4 = message.obj.messageOwner.attachPath;
                        TLRPC.Document document2 = message.obj.getDocument();
                        if (location4 == null) {
                            location4 = FileLoader.getDirectory(4) + "/" + document2.id + ".mp4";
                        }
                        putToDelayedMessages(location4, message);
                        if (message.obj.videoEditedInfo != null && message.obj.videoEditedInfo.needConvert()) {
                            getFileLoader().uploadFile(location4, false, false, document2.size, ConnectionsManager.FileTypeVideo, false);
                        } else {
                            getFileLoader().uploadFile(location4, false, false, ConnectionsManager.FileTypeVideo);
                        }
                        putToUploadingMessages(message.obj);
                    } else {
                        String location5 = FileLoader.getDirectory(4) + "/" + message.photoSize.location.volume_id + "_" + message.photoSize.location.local_id + ".jpg";
                        putToDelayedMessages(location5, message);
                        getFileLoader().uploadFile(location5, false, true, 16777216);
                        putToUploadingMessages(message.obj);
                    }
                } else {
                    String location6 = message.obj.messageOwner.attachPath;
                    TLRPC.Document document3 = message.obj.getDocument();
                    if (location6 == null) {
                        location6 = FileLoader.getDirectory(4) + "/" + document3.id + ".mp4";
                    }
                    if (message.sendEncryptedRequest != null && document3.dc_id != 0) {
                        File file2 = new File(location6);
                        if (!file2.exists()) {
                            putToDelayedMessages(FileLoader.getAttachFileName(document3), message);
                            getFileLoader().loadFile(document3, message.parentObject, 2, 0);
                            return;
                        }
                    }
                    putToDelayedMessages(location6, message);
                    if (message.obj.videoEditedInfo != null && message.obj.videoEditedInfo.needConvert()) {
                        getFileLoader().uploadFile(location6, true, false, document3.size, ConnectionsManager.FileTypeVideo, false);
                    } else {
                        getFileLoader().uploadFile(location6, true, false, ConnectionsManager.FileTypeVideo);
                    }
                    putToUploadingMessages(message.obj);
                }
            }
        } else if (message.type == 2) {
            if (message.httpLocation != null) {
                putToDelayedMessages(message.httpLocation, message);
                ImageLoader.getInstance().loadHttpFile(message.httpLocation, "gif", this.currentAccount);
            } else if (message.sendRequest != null) {
                if (message.sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                    media = ((TLRPC.TL_messages_sendMedia) message.sendRequest).media;
                } else {
                    media = ((TLRPC.TL_messages_editMessage) message.sendRequest).media;
                }
                if (media.file == null) {
                    String location7 = message.obj.messageOwner.attachPath;
                    putToDelayedMessages(location7, message);
                    FileLoader fileLoader = getFileLoader();
                    if (message.sendRequest != null) {
                        z2 = false;
                    }
                    fileLoader.uploadFile(location7, z2, false, ConnectionsManager.FileTypeFile);
                    putToUploadingMessages(message.obj);
                } else if (media.thumb == null && message.photoSize != null && !(message.photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                    String location8 = FileLoader.getDirectory(4) + "/" + message.photoSize.location.volume_id + "_" + message.photoSize.location.local_id + ".jpg";
                    putToDelayedMessages(location8, message);
                    getFileLoader().uploadFile(location8, false, true, 16777216);
                    putToUploadingMessages(message.obj);
                }
            } else {
                String location9 = message.obj.messageOwner.attachPath;
                TLRPC.Document document4 = message.obj.getDocument();
                if (message.sendEncryptedRequest != null && document4.dc_id != 0) {
                    File file3 = new File(location9);
                    if (!file3.exists()) {
                        putToDelayedMessages(FileLoader.getAttachFileName(document4), message);
                        getFileLoader().loadFile(document4, message.parentObject, 2, 0);
                        return;
                    }
                }
                putToDelayedMessages(location9, message);
                getFileLoader().uploadFile(location9, true, false, ConnectionsManager.FileTypeFile);
                putToUploadingMessages(message.obj);
            }
        } else if (message.type == 3) {
            String location10 = message.obj.messageOwner.attachPath;
            putToDelayedMessages(location10, message);
            FileLoader fileLoader2 = getFileLoader();
            if (message.sendRequest == null) {
                z = true;
            }
            fileLoader2.uploadFile(location10, z, true, ConnectionsManager.FileTypeAudio);
            putToUploadingMessages(message.obj);
        } else if (message.type == 4) {
            boolean add = index < 0;
            if (message.performMediaUpload) {
                if (index >= 0) {
                    index2 = index;
                } else {
                    index2 = message.messageObjects.size() - 1;
                }
                MessageObject messageObject2 = message.messageObjects.get(index2);
                if (messageObject2.getDocument() != null) {
                    if (message.videoEditedInfo != null) {
                        String location11 = messageObject2.messageOwner.attachPath;
                        TLRPC.Document document5 = messageObject2.getDocument();
                        if (location11 == null) {
                            location11 = FileLoader.getDirectory(4) + "/" + document5.id + ".mp4";
                        }
                        putToDelayedMessages(location11, message);
                        message.extraHashMap.put(messageObject2, location11);
                        message.extraHashMap.put(location11 + "_i", messageObject2);
                        if (message.photoSize != null && message.photoSize.location != null) {
                            message.extraHashMap.put(location11 + "_t", message.photoSize);
                        }
                        MediaController.getInstance().scheduleVideoConvert(messageObject2);
                        message.obj = messageObject2;
                        putToUploadingMessages(messageObject2);
                    } else {
                        TLRPC.Document document6 = messageObject2.getDocument();
                        String documentLocation = messageObject2.messageOwner.attachPath;
                        if (documentLocation != null) {
                            messageObject = messageObject2;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(FileLoader.getDirectory(4));
                            sb.append("/");
                            messageObject = messageObject2;
                            sb.append(document6.id);
                            sb.append(".mp4");
                            documentLocation = sb.toString();
                        }
                        if (message.sendRequest != null) {
                            TLRPC.TL_messages_sendMultiMedia request = (TLRPC.TL_messages_sendMultiMedia) message.sendRequest;
                            TLRPC.InputMedia media4 = request.multi_media.get(index2).media;
                            if (media4.file == null) {
                                putToDelayedMessages(documentLocation, message);
                                MessageObject messageObject3 = messageObject;
                                message.extraHashMap.put(messageObject3, documentLocation);
                                message.extraHashMap.put(documentLocation, media4);
                                message.extraHashMap.put(documentLocation + "_i", messageObject3);
                                if (message.photoSize != null && message.photoSize.location != null) {
                                    message.extraHashMap.put(documentLocation + "_t", message.photoSize);
                                }
                                if (messageObject3.videoEditedInfo != null && messageObject3.videoEditedInfo.needConvert()) {
                                    getFileLoader().uploadFile(documentLocation, false, false, document6.size, ConnectionsManager.FileTypeVideo, false);
                                } else {
                                    getFileLoader().uploadFile(documentLocation, false, false, ConnectionsManager.FileTypeVideo);
                                }
                                putToUploadingMessages(messageObject3);
                            } else {
                                MessageObject messageObject4 = messageObject;
                                if (message.photoSize != null) {
                                    String location12 = FileLoader.getDirectory(4) + "/" + message.photoSize.location.volume_id + "_" + message.photoSize.location.local_id + ".jpg";
                                    putToDelayedMessages(location12, message);
                                    message.extraHashMap.put(location12 + "_o", documentLocation);
                                    message.extraHashMap.put(messageObject4, location12);
                                    message.extraHashMap.put(location12, media4);
                                    getFileLoader().uploadFile(location12, false, true, 16777216);
                                    putToUploadingMessages(messageObject4);
                                }
                            }
                        } else {
                            MessageObject messageObject5 = messageObject;
                            TLRPC.TL_messages_sendEncryptedMultiMedia request2 = (TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                            putToDelayedMessages(documentLocation, message);
                            message.extraHashMap.put(messageObject5, documentLocation);
                            message.extraHashMap.put(documentLocation, request2.files.get(index2));
                            message.extraHashMap.put(documentLocation + "_i", messageObject5);
                            if (message.photoSize != null && message.photoSize.location != null) {
                                message.extraHashMap.put(documentLocation + "_t", message.photoSize);
                            }
                            if (messageObject5.videoEditedInfo != null && messageObject5.videoEditedInfo.needConvert()) {
                                getFileLoader().uploadFile(documentLocation, true, false, document6.size, ConnectionsManager.FileTypeVideo, false);
                            } else {
                                getFileLoader().uploadFile(documentLocation, true, false, ConnectionsManager.FileTypeVideo);
                            }
                            putToUploadingMessages(messageObject5);
                        }
                    }
                    message.videoEditedInfo = null;
                    message.photoSize = null;
                } else if (message.httpLocation != null) {
                    putToDelayedMessages(message.httpLocation, message);
                    message.extraHashMap.put(messageObject2, message.httpLocation);
                    message.extraHashMap.put(message.httpLocation, messageObject2);
                    ImageLoader.getInstance().loadHttpFile(message.httpLocation, "file", this.currentAccount);
                    message.httpLocation = null;
                } else {
                    if (message.sendRequest != null) {
                        TLRPC.TL_messages_sendMultiMedia request3 = (TLRPC.TL_messages_sendMultiMedia) message.sendRequest;
                        inputMedia = request3.multi_media.get(index2).media;
                    } else {
                        TLObject inputMedia2 = message.sendEncryptedRequest;
                        TLRPC.TL_messages_sendEncryptedMultiMedia request4 = (TLRPC.TL_messages_sendEncryptedMultiMedia) inputMedia2;
                        inputMedia = request4.files.get(index2);
                    }
                    String location13 = FileLoader.getInstance(this.currentAccount).getPathToAttach(message.photoSize).toString();
                    putToDelayedMessages(location13, message);
                    message.extraHashMap.put(location13, inputMedia);
                    message.extraHashMap.put(messageObject2, location13);
                    getFileLoader().uploadFile(location13, message.sendEncryptedRequest != null, true, 16777216);
                    putToUploadingMessages(messageObject2);
                    message.photoSize = null;
                }
                message.performMediaUpload = false;
            } else if (!message.messageObjects.isEmpty()) {
                putToSendingMessages(message.messageObjects.get(message.messageObjects.size() - 1).messageOwner, message.finalGroupMessage != 0);
            }
            sendReadyToSendGroup(message, add, true);
        } else if (message.type == 5) {
            final String key = "stickerset_" + message.obj.getId();
            TLRPC.TL_messages_getStickerSet req = new TLRPC.TL_messages_getStickerSet();
            req.stickerset = (TLRPC.InputStickerSet) message.parentObject;
            getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda83
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    SendMessagesHelper.this.m1164x4ece33ee(message, key, tLObject, tL_error);
                }
            });
            putToDelayedMessages(key, message);
        }
    }

    /* renamed from: lambda$performSendDelayedMessage$33$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1164x4ece33ee(final DelayedMessage message, final String key, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1163x698cc52d(response, message, key);
            }
        });
    }

    /* renamed from: lambda$performSendDelayedMessage$32$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1163x698cc52d(TLObject response, DelayedMessage message, String key) {
        boolean found = false;
        if (response != null) {
            TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet) response;
            getMediaDataController().storeTempStickerSet(set);
            TLRPC.TL_documentAttributeSticker_layer55 attributeSticker = (TLRPC.TL_documentAttributeSticker_layer55) message.locationParent;
            attributeSticker.stickerset = new TLRPC.TL_inputStickerSetShortName();
            attributeSticker.stickerset.short_name = set.set.short_name;
            found = true;
        }
        ArrayList<DelayedMessage> arrayList = this.delayedMessages.remove(key);
        if (arrayList != null && !arrayList.isEmpty()) {
            if (found) {
                getMessagesStorage().replaceMessageIfExists(arrayList.get(0).obj.messageOwner, null, null, false);
            }
            getSecretChatHelper().performSendEncryptedRequest((TLRPC.DecryptedMessage) message.sendEncryptedRequest, message.obj.messageOwner, message.encryptedChat, null, null, message.obj);
        }
    }

    private void uploadMultiMedia(final DelayedMessage message, final TLRPC.InputMedia inputMedia, TLRPC.InputEncryptedFile inputEncryptedFile, String key) {
        if (inputMedia != null) {
            TLRPC.TL_messages_sendMultiMedia multiMedia = (TLRPC.TL_messages_sendMultiMedia) message.sendRequest;
            int a = 0;
            while (true) {
                if (a >= multiMedia.multi_media.size()) {
                    break;
                } else if (multiMedia.multi_media.get(a).media != inputMedia) {
                    a++;
                } else {
                    putToSendingMessages(message.messages.get(a), message.scheduled);
                    getNotificationCenter().postNotificationName(NotificationCenter.fileUploadProgressChanged, key, -1L, -1L, false);
                    break;
                }
            }
            TLRPC.TL_messages_uploadMedia req = new TLRPC.TL_messages_uploadMedia();
            req.media = inputMedia;
            req.peer = ((TLRPC.TL_messages_sendMultiMedia) message.sendRequest).peer;
            getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda85
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    SendMessagesHelper.this.m1222xbfc806b8(inputMedia, message, tLObject, tL_error);
                }
            });
        } else if (inputEncryptedFile != null) {
            TLRPC.TL_messages_sendEncryptedMultiMedia multiMedia2 = (TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
            int a2 = 0;
            while (true) {
                if (a2 >= multiMedia2.files.size()) {
                    break;
                } else if (multiMedia2.files.get(a2) != inputEncryptedFile) {
                    a2++;
                } else {
                    putToSendingMessages(message.messages.get(a2), message.scheduled);
                    getNotificationCenter().postNotificationName(NotificationCenter.fileUploadProgressChanged, key, -1L, -1L, false);
                    break;
                }
            }
            sendReadyToSendGroup(message, false, true);
        }
    }

    /* renamed from: lambda$uploadMultiMedia$35$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1222xbfc806b8(final TLRPC.InputMedia inputMedia, final DelayedMessage message, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1221xda8697f7(response, inputMedia, message);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: lambda$uploadMultiMedia$34$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1221xda8697f7(TLObject response, TLRPC.InputMedia inputMedia, DelayedMessage message) {
        TLRPC.InputMedia newInputMedia = null;
        if (response != null) {
            TLRPC.MessageMedia messageMedia = (TLRPC.MessageMedia) response;
            if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) && (messageMedia instanceof TLRPC.TL_messageMediaPhoto)) {
                TLRPC.TL_inputMediaPhoto inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                inputMediaPhoto.id = new TLRPC.TL_inputPhoto();
                inputMediaPhoto.id.id = messageMedia.photo.id;
                inputMediaPhoto.id.access_hash = messageMedia.photo.access_hash;
                inputMediaPhoto.id.file_reference = messageMedia.photo.file_reference;
                newInputMedia = inputMediaPhoto;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("set uploaded photo");
                }
            } else if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) && (messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                TLRPC.TL_inputMediaDocument inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                inputMediaDocument.id = new TLRPC.TL_inputDocument();
                inputMediaDocument.id.id = messageMedia.document.id;
                inputMediaDocument.id.access_hash = messageMedia.document.access_hash;
                inputMediaDocument.id.file_reference = messageMedia.document.file_reference;
                newInputMedia = inputMediaDocument;
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("set uploaded document");
                }
            }
        }
        if (newInputMedia != null) {
            if (inputMedia.ttl_seconds != 0) {
                newInputMedia.ttl_seconds = inputMedia.ttl_seconds;
                newInputMedia.flags |= 1;
            }
            TLRPC.TL_messages_sendMultiMedia req1 = (TLRPC.TL_messages_sendMultiMedia) message.sendRequest;
            int a = 0;
            while (true) {
                if (a >= req1.multi_media.size()) {
                    break;
                } else if (req1.multi_media.get(a).media != inputMedia) {
                    a++;
                } else {
                    req1.multi_media.get(a).media = newInputMedia;
                    break;
                }
            }
            sendReadyToSendGroup(message, false, true);
            return;
        }
        message.markAsError();
    }

    private void sendReadyToSendGroup(DelayedMessage message, boolean add, boolean check) {
        DelayedMessage maxDelayedMessage;
        if (message.messageObjects.isEmpty()) {
            message.markAsError();
            return;
        }
        String key = "group_" + message.groupId;
        if (message.finalGroupMessage != message.messageObjects.get(message.messageObjects.size() - 1).getId()) {
            if (add) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("final message not added, add");
                }
                putToDelayedMessages(key, message);
                return;
            } else if (BuildVars.DEBUG_VERSION) {
                FileLog.d("final message not added");
                return;
            } else {
                return;
            }
        }
        if (add) {
            this.delayedMessages.remove(key);
            getMessagesStorage().putMessages(message.messages, false, true, false, 0, message.scheduled);
            getMessagesController().updateInterfaceWithMessages(message.peer, message.messageObjects, message.scheduled);
            if (!message.scheduled) {
                getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("add message");
            }
        }
        if (message.sendRequest instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia request = (TLRPC.TL_messages_sendMultiMedia) message.sendRequest;
            for (int a = 0; a < request.multi_media.size(); a++) {
                TLRPC.InputMedia inputMedia = request.multi_media.get(a).media;
                if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("multi media not ready");
                        return;
                    } else {
                        return;
                    }
                }
            }
            if (check && (maxDelayedMessage = findMaxDelayedMessageForMessageId(message.finalGroupMessage, message.peer)) != null) {
                maxDelayedMessage.addDelayedRequest(message.sendRequest, message.messageObjects, message.originalPaths, message.parentObjects, message, message.scheduled);
                if (message.requests != null) {
                    maxDelayedMessage.requests.addAll(message.requests);
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("has maxDelayedMessage, delay");
                    return;
                }
                return;
            }
        } else {
            TLRPC.TL_messages_sendEncryptedMultiMedia request2 = (TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
            for (int a2 = 0; a2 < request2.files.size(); a2++) {
                if (request2.files.get(a2) instanceof TLRPC.TL_inputEncryptedFile) {
                    return;
                }
            }
        }
        if (message.sendRequest instanceof TLRPC.TL_messages_sendMultiMedia) {
            performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia) message.sendRequest, message.messageObjects, message.originalPaths, message.parentObjects, message, message.scheduled);
        } else {
            getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest, message);
        }
        message.sendDelayedRequests();
    }

    /* renamed from: lambda$stopVideoService$36$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1219x610147a9(String path) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopEncodingService, path, Integer.valueOf(this.currentAccount));
    }

    /* renamed from: lambda$stopVideoService$37$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1220x4642b66a(final String path) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1219x610147a9(path);
            }
        });
    }

    public void stopVideoService(final String path) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1220x4642b66a(path);
            }
        });
    }

    public void putToSendingMessages(final TLRPC.Message message, final boolean scheduled) {
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1195x5747eb0b(message, scheduled);
                }
            });
        } else {
            putToSendingMessages(message, scheduled, true);
        }
    }

    /* renamed from: lambda$putToSendingMessages$38$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1195x5747eb0b(TLRPC.Message message, boolean scheduled) {
        putToSendingMessages(message, scheduled, true);
    }

    protected void putToSendingMessages(TLRPC.Message message, boolean scheduled, boolean notify) {
        if (message == null) {
            return;
        }
        if (message.id > 0) {
            this.editingMessages.put(message.id, message);
            return;
        }
        boolean contains = this.sendingMessages.indexOfKey(message.id) >= 0;
        removeFromUploadingMessages(message.id, scheduled);
        this.sendingMessages.put(message.id, message);
        if (!scheduled && !contains) {
            long did = MessageObject.getDialogId(message);
            LongSparseArray<Integer> longSparseArray = this.sendingMessagesIdDialogs;
            longSparseArray.put(did, Integer.valueOf(longSparseArray.get(did, 0).intValue() + 1));
            if (notify) {
                getNotificationCenter().postNotificationName(NotificationCenter.sendingMessagesChanged, new Object[0]);
            }
        }
    }

    public TLRPC.Message removeFromSendingMessages(int mid, boolean scheduled) {
        TLRPC.Message message;
        long did;
        Integer currentCount;
        if (mid > 0) {
            message = this.editingMessages.get(mid);
            if (message != null) {
                this.editingMessages.remove(mid);
            }
        } else {
            message = this.sendingMessages.get(mid);
            if (message != null) {
                this.sendingMessages.remove(mid);
                if (!scheduled && (currentCount = this.sendingMessagesIdDialogs.get((did = MessageObject.getDialogId(message)))) != null) {
                    int count = currentCount.intValue() - 1;
                    if (count <= 0) {
                        this.sendingMessagesIdDialogs.remove(did);
                    } else {
                        this.sendingMessagesIdDialogs.put(did, Integer.valueOf(count));
                    }
                    getNotificationCenter().postNotificationName(NotificationCenter.sendingMessagesChanged, new Object[0]);
                }
            }
        }
        return message;
    }

    public int getSendingMessageId(long did) {
        for (int a = 0; a < this.sendingMessages.size(); a++) {
            TLRPC.Message message = this.sendingMessages.valueAt(a);
            if (message.dialog_id == did) {
                return message.id;
            }
        }
        for (int a2 = 0; a2 < this.uploadMessages.size(); a2++) {
            TLRPC.Message message2 = this.uploadMessages.valueAt(a2);
            if (message2.dialog_id == did) {
                return message2.id;
            }
        }
        return 0;
    }

    protected void putToUploadingMessages(MessageObject obj) {
        if (obj == null || obj.getId() > 0 || obj.scheduled) {
            return;
        }
        TLRPC.Message message = obj.messageOwner;
        boolean contains = this.uploadMessages.indexOfKey(message.id) >= 0;
        this.uploadMessages.put(message.id, message);
        if (!contains) {
            long did = MessageObject.getDialogId(message);
            LongSparseArray<Integer> longSparseArray = this.uploadingMessagesIdDialogs;
            longSparseArray.put(did, Integer.valueOf(longSparseArray.get(did, 0).intValue() + 1));
            getNotificationCenter().postNotificationName(NotificationCenter.sendingMessagesChanged, new Object[0]);
        }
    }

    protected void removeFromUploadingMessages(int mid, boolean scheduled) {
        TLRPC.Message message;
        if (mid <= 0 && !scheduled && (message = this.uploadMessages.get(mid)) != null) {
            this.uploadMessages.remove(mid);
            long did = MessageObject.getDialogId(message);
            Integer currentCount = this.uploadingMessagesIdDialogs.get(did);
            if (currentCount != null) {
                int count = currentCount.intValue() - 1;
                if (count <= 0) {
                    this.uploadingMessagesIdDialogs.remove(did);
                } else {
                    this.uploadingMessagesIdDialogs.put(did, Integer.valueOf(count));
                }
                getNotificationCenter().postNotificationName(NotificationCenter.sendingMessagesChanged, new Object[0]);
            }
        }
    }

    public boolean isSendingMessage(int mid) {
        return this.sendingMessages.indexOfKey(mid) >= 0 || this.editingMessages.indexOfKey(mid) >= 0;
    }

    public boolean isSendingMessageIdDialog(long did) {
        return this.sendingMessagesIdDialogs.get(did, 0).intValue() > 0;
    }

    public boolean isUploadingMessageIdDialog(long did) {
        return this.uploadingMessagesIdDialogs.get(did, 0).intValue() > 0;
    }

    public void performSendMessageRequestMulti(final TLRPC.TL_messages_sendMultiMedia req, final ArrayList<MessageObject> msgObjs, final ArrayList<String> originalPaths, final ArrayList<Object> parentObjects, final DelayedMessage delayedMessage, final boolean scheduled) {
        int size = msgObjs.size();
        for (int a = 0; a < size; a++) {
            putToSendingMessages(msgObjs.get(a).messageOwner, scheduled);
        }
        getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda81
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1188xc62865dc(parentObjects, req, msgObjs, originalPaths, delayedMessage, scheduled, tLObject, tL_error);
            }
        }, (QuickAckDelegate) null, 68);
    }

    /* renamed from: lambda$performSendMessageRequestMulti$46$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1188xc62865dc(ArrayList parentObjects, final TLRPC.TL_messages_sendMultiMedia req, final ArrayList msgObjs, final ArrayList originalPaths, final DelayedMessage delayedMessage, final boolean scheduled, final TLObject response, final TLRPC.TL_error error) {
        if (error != null && FileRefController.isFileRefError(error.text)) {
            if (parentObjects != null) {
                ArrayList<Object> arrayList = new ArrayList<>(parentObjects);
                getFileRefController().requestReference(arrayList, req, msgObjs, originalPaths, arrayList, delayedMessage, Boolean.valueOf(scheduled));
                return;
            } else if (delayedMessage != null && !delayedMessage.retriedToSend) {
                delayedMessage.retriedToSend = true;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda57
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.m1181xb30048c0(req, delayedMessage, msgObjs, scheduled);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1187xe0e6f71b(error, response, msgObjs, originalPaths, scheduled, req);
            }
        });
    }

    /* renamed from: lambda$performSendMessageRequestMulti$39$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1181xb30048c0(TLRPC.TL_messages_sendMultiMedia req, DelayedMessage delayedMessage, ArrayList msgObjs, boolean scheduled) {
        boolean hasEmptyFile = false;
        int size = req.multi_media.size();
        for (int a = 0; a < size; a++) {
            if (delayedMessage.parentObjects.get(a) != null) {
                removeFromSendingMessages(((MessageObject) msgObjs.get(a)).getId(), scheduled);
                TLRPC.TL_inputSingleMedia request = req.multi_media.get(a);
                if (request.media instanceof TLRPC.TL_inputMediaPhoto) {
                    request.media = delayedMessage.inputMedias.get(a);
                } else if (request.media instanceof TLRPC.TL_inputMediaDocument) {
                    request.media = delayedMessage.inputMedias.get(a);
                }
                delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(a);
                delayedMessage.httpLocation = delayedMessage.httpLocations.get(a);
                delayedMessage.photoSize = delayedMessage.locations.get(a);
                delayedMessage.performMediaUpload = true;
                if (request.media.file == null || delayedMessage.photoSize != null) {
                    hasEmptyFile = true;
                }
                performSendDelayedMessage(delayedMessage, a);
            }
        }
        if (!hasEmptyFile) {
            for (int i = 0; i < msgObjs.size(); i++) {
                TLRPC.Message newMsgObj = ((MessageObject) msgObjs.get(i)).messageOwner;
                getMessagesStorage().markMessageAsSendError(newMsgObj, scheduled);
                newMsgObj.send_state = 2;
                getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj.id));
                processSentMessage(newMsgObj.id);
                removeFromSendingMessages(newMsgObj.id, scheduled);
            }
        }
    }

    /* renamed from: lambda$performSendMessageRequestMulti$45$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1187xe0e6f71b(TLRPC.TL_error error, TLObject response, ArrayList msgObjs, ArrayList originalPaths, final boolean scheduled, TLRPC.TL_messages_sendMultiMedia req) {
        SendMessagesHelper sendMessagesHelper;
        int i;
        boolean isSentError;
        final TLRPC.Updates updates;
        TLRPC.Message newMsgObj;
        LongSparseArray<Integer> newIds;
        SparseArray<TLRPC.Message> newMessages;
        boolean isSentError2;
        int i2;
        LongSparseArray<Integer> newIds2;
        SparseArray<TLRPC.Message> newMessages2;
        TLRPC.Updates updates2;
        SendMessagesHelper sendMessagesHelper2;
        TLRPC.MessageReplies messageReplies;
        SendMessagesHelper sendMessagesHelper3 = this;
        ArrayList arrayList = msgObjs;
        boolean isSentError3 = false;
        if (error == null) {
            SparseArray<TLRPC.Message> newMessages3 = new SparseArray<>();
            LongSparseArray<Integer> newIds3 = new LongSparseArray<>();
            TLRPC.Updates updates3 = (TLRPC.Updates) response;
            ArrayList<TLRPC.Update> updatesArr = ((TLRPC.Updates) response).updates;
            int a = 0;
            LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies = null;
            while (a < updatesArr.size()) {
                TLRPC.Update update = updatesArr.get(a);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID updateMessageID = (TLRPC.TL_updateMessageID) update;
                    newIds3.put(updateMessageID.random_id, Integer.valueOf(updateMessageID.id));
                    updatesArr.remove(a);
                    a--;
                    sendMessagesHelper2 = this;
                    updates2 = updates3;
                } else if (update instanceof TLRPC.TL_updateNewMessage) {
                    final TLRPC.TL_updateNewMessage newMessage = (TLRPC.TL_updateNewMessage) update;
                    newMessages3.put(newMessage.message.id, newMessage.message);
                    sendMessagesHelper2 = this;
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda62
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.m1182x669fcd56(newMessage);
                        }
                    });
                    updatesArr.remove(a);
                    a--;
                    updates2 = updates3;
                } else {
                    sendMessagesHelper2 = this;
                    if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                        final TLRPC.TL_updateNewChannelMessage newMessage2 = (TLRPC.TL_updateNewChannelMessage) update;
                        long channelId = MessagesController.getUpdateChannelId(newMessage2);
                        updates2 = updates3;
                        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(channelId));
                        if (chat == null || chat.megagroup) {
                            if (newMessage2.message.reply_to != null) {
                                if (newMessage2.message.reply_to.reply_to_top_id != 0 || newMessage2.message.reply_to.reply_to_msg_id != 0) {
                                    if (channelReplies == null) {
                                        channelReplies = new LongSparseArray<>();
                                    }
                                    long did = MessageObject.getDialogId(newMessage2.message);
                                    SparseArray<TLRPC.MessageReplies> replies = channelReplies.get(did);
                                    if (replies == null) {
                                        replies = new SparseArray<>();
                                        channelReplies.put(did, replies);
                                    }
                                    LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies2 = channelReplies;
                                    int id = newMessage2.message.reply_to.reply_to_top_id != 0 ? newMessage2.message.reply_to.reply_to_top_id : newMessage2.message.reply_to.reply_to_msg_id;
                                    TLRPC.MessageReplies messageReplies2 = replies.get(id);
                                    if (messageReplies2 == null) {
                                        messageReplies = new TLRPC.TL_messageReplies();
                                        replies.put(id, messageReplies);
                                    } else {
                                        messageReplies = messageReplies2;
                                    }
                                    if (newMessage2.message.from_id != null) {
                                        messageReplies.recent_repliers.add(0, newMessage2.message.from_id);
                                    }
                                    messageReplies.replies++;
                                    channelReplies = channelReplies2;
                                }
                            }
                            newMessages3.put(newMessage2.message.id, newMessage2.message);
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda60
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.m1183x4be13c17(newMessage2);
                                }
                            });
                            updatesArr.remove(a);
                            a--;
                        }
                        newMessages3.put(newMessage2.message.id, newMessage2.message);
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda60
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.m1183x4be13c17(newMessage2);
                            }
                        });
                        updatesArr.remove(a);
                        a--;
                    } else {
                        updates2 = updates3;
                        if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                            TLRPC.TL_updateNewScheduledMessage newMessage3 = (TLRPC.TL_updateNewScheduledMessage) update;
                            newMessages3.put(newMessage3.message.id, newMessage3.message);
                            updatesArr.remove(a);
                            a--;
                        }
                    }
                }
                a++;
                sendMessagesHelper3 = sendMessagesHelper2;
                updates3 = updates2;
            }
            TLRPC.Updates updates4 = updates3;
            sendMessagesHelper = sendMessagesHelper3;
            if (channelReplies != null) {
                getMessagesStorage().putChannelViews(null, null, channelReplies, true);
                getNotificationCenter().postNotificationName(NotificationCenter.didUpdateMessagesViews, null, null, channelReplies, true);
            }
            int i3 = 0;
            while (true) {
                if (i3 >= msgObjs.size()) {
                    boolean isSentError4 = isSentError3;
                    updates = updates4;
                    i = 1;
                    isSentError = isSentError4;
                    break;
                }
                MessageObject msgObj = (MessageObject) arrayList.get(i3);
                String originalPath = (String) originalPaths.get(i3);
                TLRPC.Message newMsgObj2 = msgObj.messageOwner;
                final int oldId = newMsgObj2.id;
                final ArrayList<TLRPC.Message> sentMessages = new ArrayList<>();
                String str = newMsgObj2.attachPath;
                Integer id2 = newIds3.get(newMsgObj2.random_id);
                if (id2 == null) {
                    updates = updates4;
                    i = 1;
                    isSentError = true;
                    break;
                }
                TLRPC.Message message = newMessages3.get(id2.intValue());
                if (message == null) {
                    updates = updates4;
                    i = 1;
                    isSentError = true;
                    break;
                }
                MessageObject.getDialogId(message);
                sentMessages.add(message);
                if ((message.flags & ConnectionsManager.FileTypeVideo) == 0) {
                    newMsgObj = newMsgObj2;
                } else {
                    newMsgObj = newMsgObj2;
                    msgObj.messageOwner.ttl_period = message.ttl_period;
                    msgObj.messageOwner.flags |= ConnectionsManager.FileTypeVideo;
                }
                final TLRPC.Message newMsgObj3 = newMsgObj;
                LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies3 = channelReplies;
                ArrayList<TLRPC.Update> updatesArr2 = updatesArr;
                TLRPC.Updates updates5 = updates4;
                updateMediaPaths(msgObj, message, message.id, originalPath, false);
                final int existFlags = msgObj.getMediaExistanceFlags();
                newMsgObj3.id = message.id;
                final long grouped_id = message.grouped_id;
                if (scheduled) {
                    newMessages = newMessages3;
                    newIds = newIds3;
                } else {
                    Integer value = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                    if (value != null) {
                        newMessages = newMessages3;
                        newIds = newIds3;
                    } else {
                        newMessages = newMessages3;
                        newIds = newIds3;
                        value = Integer.valueOf(getMessagesStorage().getDialogReadMax(message.out, message.dialog_id));
                        getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), value);
                    }
                    message.unread = value.intValue() < message.id;
                }
                if (!isSentError3) {
                    getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                    newMsgObj3.send_state = 0;
                    getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj3.id), newMsgObj3, Long.valueOf(newMsgObj3.dialog_id), Long.valueOf(grouped_id), Integer.valueOf(existFlags), Boolean.valueOf(scheduled));
                    newMessages2 = newMessages;
                    newIds2 = newIds;
                    i2 = i3;
                    isSentError2 = isSentError3;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda45
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.m1185x16641999(newMsgObj3, oldId, scheduled, sentMessages, grouped_id, existFlags);
                        }
                    });
                } else {
                    isSentError2 = isSentError3;
                    newMessages2 = newMessages;
                    newIds2 = newIds;
                    i2 = i3;
                }
                i3 = i2 + 1;
                updates4 = updates5;
                newMessages3 = newMessages2;
                updatesArr = updatesArr2;
                channelReplies = channelReplies3;
                newIds3 = newIds2;
                isSentError3 = isSentError2;
                arrayList = msgObjs;
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1186xfba5885a(updates);
                }
            });
        } else {
            sendMessagesHelper = sendMessagesHelper3;
            i = 1;
            AlertsCreator.processError(sendMessagesHelper.currentAccount, error, null, req, new Object[0]);
            isSentError = true;
        }
        if (isSentError) {
            for (int i4 = 0; i4 < msgObjs.size(); i4++) {
                TLRPC.Message newMsgObj4 = ((MessageObject) msgObjs.get(i4)).messageOwner;
                getMessagesStorage().markMessageAsSendError(newMsgObj4, scheduled);
                newMsgObj4.send_state = 2;
                NotificationCenter notificationCenter = getNotificationCenter();
                int i5 = NotificationCenter.messageSendError;
                Object[] objArr = new Object[i];
                objArr[0] = Integer.valueOf(newMsgObj4.id);
                notificationCenter.postNotificationName(i5, objArr);
                sendMessagesHelper.processSentMessage(newMsgObj4.id);
                sendMessagesHelper.removeFromSendingMessages(newMsgObj4.id, scheduled);
            }
        }
    }

    /* renamed from: lambda$performSendMessageRequestMulti$40$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1182x669fcd56(TLRPC.TL_updateNewMessage newMessage) {
        getMessagesController().processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
    }

    /* renamed from: lambda$performSendMessageRequestMulti$41$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1183x4be13c17(TLRPC.TL_updateNewChannelMessage newMessage) {
        getMessagesController().processNewChannelDifferenceParams(newMessage.pts, newMessage.pts_count, newMessage.message.peer_id.channel_id);
    }

    /* renamed from: lambda$performSendMessageRequestMulti$43$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1185x16641999(final TLRPC.Message newMsgObj, final int oldId, final boolean scheduled, ArrayList sentMessages, final long grouped_id, final int existFlags) {
        getMessagesStorage().updateMessageStateAndId(newMsgObj.random_id, MessageObject.getPeerId(newMsgObj.peer_id), Integer.valueOf(oldId), newMsgObj.id, 0, false, scheduled ? 1 : 0);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) sentMessages, true, false, false, 0, scheduled);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1184x3122aad8(newMsgObj, oldId, grouped_id, existFlags, scheduled);
            }
        });
    }

    /* renamed from: lambda$performSendMessageRequestMulti$42$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1184x3122aad8(TLRPC.Message newMsgObj, int oldId, long grouped_id, int existFlags, boolean scheduled) {
        getMediaDataController().increasePeerRaiting(newMsgObj.dialog_id);
        getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id), Long.valueOf(grouped_id), Integer.valueOf(existFlags), Boolean.valueOf(scheduled));
        processSentMessage(oldId);
        removeFromSendingMessages(oldId, scheduled);
    }

    /* renamed from: lambda$performSendMessageRequestMulti$44$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1186xfba5885a(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    public void performSendMessageRequest(TLObject req, MessageObject msgObj, String originalPath, DelayedMessage delayedMessage, Object parentObject, HashMap<String, String> params, boolean scheduled) {
        performSendMessageRequest(req, msgObj, originalPath, null, false, delayedMessage, parentObject, params, scheduled);
    }

    private DelayedMessage findMaxDelayedMessageForMessageId(int messageId, long dialogId) {
        DelayedMessage maxDelayedMessage = null;
        int maxDalyedMessageId = Integer.MIN_VALUE;
        for (Map.Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
            ArrayList<DelayedMessage> messages = entry.getValue();
            int size = messages.size();
            for (int a = 0; a < size; a++) {
                DelayedMessage delayedMessage = messages.get(a);
                if ((delayedMessage.type == 4 || delayedMessage.type == 0) && delayedMessage.peer == dialogId) {
                    int mid = 0;
                    if (delayedMessage.obj != null) {
                        mid = delayedMessage.obj.getId();
                    } else if (delayedMessage.messageObjects != null && !delayedMessage.messageObjects.isEmpty()) {
                        mid = delayedMessage.messageObjects.get(delayedMessage.messageObjects.size() - 1).getId();
                    }
                    if (mid != 0 && mid > messageId && maxDelayedMessage == null && maxDalyedMessageId < mid) {
                        maxDelayedMessage = delayedMessage;
                        maxDalyedMessageId = mid;
                    }
                }
            }
        }
        return maxDelayedMessage;
    }

    public void performSendMessageRequest(final TLObject req, final MessageObject msgObj, final String originalPath, final DelayedMessage parentMessage, final boolean check, final DelayedMessage delayedMessage, final Object parentObject, HashMap<String, String> params, final boolean scheduled) {
        DelayedMessage maxDelayedMessage;
        if (!(req instanceof TLRPC.TL_messages_editMessage) && check && (maxDelayedMessage = findMaxDelayedMessageForMessageId(msgObj.getId(), msgObj.getDialogId())) != null) {
            maxDelayedMessage.addDelayedRequest(req, msgObj, originalPath, parentObject, delayedMessage, parentMessage != null ? parentMessage.scheduled : false);
            if (parentMessage != null && parentMessage.requests != null) {
                maxDelayedMessage.requests.addAll(parentMessage.requests);
                return;
            }
            return;
        }
        final TLRPC.Message newMsgObj = msgObj.messageOwner;
        putToSendingMessages(newMsgObj, scheduled);
        newMsgObj.reqId = getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda84
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.m1178xc47ba465(req, parentObject, msgObj, originalPath, parentMessage, check, delayedMessage, scheduled, newMsgObj, tLObject, tL_error);
            }
        }, new QuickAckDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda74
            @Override // org.telegram.tgnet.QuickAckDelegate
            public final void run() {
                SendMessagesHelper.this.m1180x8efe81e7(newMsgObj);
            }
        }, (req instanceof TLRPC.TL_messages_sendMessage ? 128 : 0) | 68);
        if (parentMessage != null) {
            parentMessage.sendDelayedRequests();
        }
    }

    /* renamed from: lambda$performSendMessageRequest$60$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1178xc47ba465(final TLObject req, Object parentObject, final MessageObject msgObj, final String originalPath, DelayedMessage parentMessage, boolean check, final DelayedMessage delayedMessage, final boolean scheduled, final TLRPC.Message newMsgObj, final TLObject response, final TLRPC.TL_error error) {
        if (error != null && (((req instanceof TLRPC.TL_messages_sendMedia) || (req instanceof TLRPC.TL_messages_editMessage)) && FileRefController.isFileRefError(error.text))) {
            if (parentObject != null) {
                getFileRefController().requestReference(parentObject, req, msgObj, originalPath, parentMessage, Boolean.valueOf(check), delayedMessage, Boolean.valueOf(scheduled));
                return;
            } else if (delayedMessage != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda50
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.m1165x836cd8ee(newMsgObj, scheduled, req, delayedMessage);
                    }
                });
                return;
            }
        }
        if (req instanceof TLRPC.TL_messages_editMessage) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1168x18f3b06(error, newMsgObj, response, msgObj, originalPath, scheduled, req);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1177x10dc1fcf(scheduled, error, newMsgObj, response, msgObj, originalPath, req);
                }
            });
        }
    }

    /* renamed from: lambda$performSendMessageRequest$47$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1165x836cd8ee(TLRPC.Message newMsgObj, boolean scheduled, TLObject req, DelayedMessage delayedMessage) {
        removeFromSendingMessages(newMsgObj.id, scheduled);
        if (req instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia request = (TLRPC.TL_messages_sendMedia) req;
            if (request.media instanceof TLRPC.TL_inputMediaPhoto) {
                request.media = delayedMessage.inputUploadMedia;
            } else if (request.media instanceof TLRPC.TL_inputMediaDocument) {
                request.media = delayedMessage.inputUploadMedia;
            }
        } else if (req instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.TL_messages_editMessage request2 = (TLRPC.TL_messages_editMessage) req;
            if (request2.media instanceof TLRPC.TL_inputMediaPhoto) {
                request2.media = delayedMessage.inputUploadMedia;
            } else if (request2.media instanceof TLRPC.TL_inputMediaDocument) {
                request2.media = delayedMessage.inputUploadMedia;
            }
        }
        delayedMessage.performMediaUpload = true;
        performSendDelayedMessage(delayedMessage);
    }

    /* renamed from: lambda$performSendMessageRequest$50$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1168x18f3b06(TLRPC.TL_error error, final TLRPC.Message newMsgObj, TLObject response, MessageObject msgObj, String originalPath, final boolean scheduled, TLObject req) {
        TLRPC.Message message;
        if (error == null) {
            String attachPath = newMsgObj.attachPath;
            final TLRPC.Updates updates = (TLRPC.Updates) response;
            ArrayList<TLRPC.Update> updatesArr = ((TLRPC.Updates) response).updates;
            int a = 0;
            while (true) {
                if (a >= updatesArr.size()) {
                    message = null;
                    break;
                }
                TLRPC.Update update = updatesArr.get(a);
                if (update instanceof TLRPC.TL_updateEditMessage) {
                    TLRPC.TL_updateEditMessage newMessage = (TLRPC.TL_updateEditMessage) update;
                    TLRPC.Message message2 = newMessage.message;
                    message = message2;
                    break;
                } else if (update instanceof TLRPC.TL_updateEditChannelMessage) {
                    TLRPC.TL_updateEditChannelMessage newMessage2 = (TLRPC.TL_updateEditChannelMessage) update;
                    TLRPC.Message message3 = newMessage2.message;
                    message = message3;
                    break;
                } else if (!(update instanceof TLRPC.TL_updateNewScheduledMessage)) {
                    a++;
                } else {
                    TLRPC.TL_updateNewScheduledMessage newMessage3 = (TLRPC.TL_updateNewScheduledMessage) update;
                    TLRPC.Message message4 = newMessage3.message;
                    message = message4;
                    break;
                }
            }
            if (message != null) {
                ImageLoader.saveMessageThumbs(message);
                updateMediaPaths(msgObj, message, message.id, originalPath, false);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1167x4defb670(updates, newMsgObj, scheduled);
                }
            });
            if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
                stopVideoService(attachPath);
            }
            return;
        }
        AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
        if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
            stopVideoService(newMsgObj.attachPath);
        }
        removeFromSendingMessages(newMsgObj.id, scheduled);
        revertEditingMessageObject(msgObj);
    }

    /* renamed from: lambda$performSendMessageRequest$49$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1167x4defb670(TLRPC.Updates updates, final TLRPC.Message newMsgObj, final boolean scheduled) {
        getMessagesController().processUpdates(updates, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1166x68ae47af(newMsgObj, scheduled);
            }
        });
    }

    /* renamed from: lambda$performSendMessageRequest$48$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1166x68ae47af(TLRPC.Message newMsgObj, boolean scheduled) {
        processSentMessage(newMsgObj.id);
        removeFromSendingMessages(newMsgObj.id, scheduled);
    }

    /* renamed from: lambda$performSendMessageRequest$59$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1177x10dc1fcf(final boolean scheduled, TLRPC.TL_error error, final TLRPC.Message newMsgObj, TLObject response, final MessageObject msgObj, String originalPath, TLObject req) {
        boolean isSentError;
        boolean currentSchedule;
        String attachPath;
        int oldId;
        int existFlags;
        ArrayList<TLRPC.Message> sentMessages;
        LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies;
        TLRPC.Message message;
        ArrayList<TLRPC.Update> updatesArr;
        int existFlags2;
        SparseArray<TLRPC.MessageReplies> replies;
        TLRPC.MessageReplies messageReplies;
        boolean currentSchedule2 = scheduled;
        boolean isSentError2 = false;
        if (error != null) {
            AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
            isSentError = true;
        } else {
            int oldId2 = newMsgObj.id;
            ArrayList<TLRPC.Message> sentMessages2 = new ArrayList<>();
            String attachPath2 = newMsgObj.attachPath;
            boolean scheduledOnline = newMsgObj.date == 2147483646;
            if (response instanceof TLRPC.TL_updateShortSentMessage) {
                final TLRPC.TL_updateShortSentMessage res = (TLRPC.TL_updateShortSentMessage) response;
                attachPath = attachPath2;
                sentMessages = sentMessages2;
                oldId = oldId2;
                updateMediaPaths(msgObj, null, res.id, null, false);
                int existFlags3 = msgObj.getMediaExistanceFlags();
                int i = res.id;
                newMsgObj.id = i;
                newMsgObj.local_id = i;
                newMsgObj.date = res.date;
                newMsgObj.entities = res.entities;
                newMsgObj.out = res.out;
                if ((res.flags & ConnectionsManager.FileTypeVideo) != 0) {
                    newMsgObj.ttl_period = res.ttl_period;
                    newMsgObj.flags |= ConnectionsManager.FileTypeVideo;
                }
                if (res.media != null) {
                    newMsgObj.media = res.media;
                    newMsgObj.flags |= 512;
                    ImageLoader.saveMessageThumbs(newMsgObj);
                }
                if (((res.media instanceof TLRPC.TL_messageMediaGame) || (res.media instanceof TLRPC.TL_messageMediaInvoice)) && !TextUtils.isEmpty(res.message)) {
                    newMsgObj.message = res.message;
                }
                if (!newMsgObj.entities.isEmpty()) {
                    newMsgObj.flags |= 128;
                }
                if (0 == 0) {
                    Integer value = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(newMsgObj.dialog_id));
                    if (value == null) {
                        value = Integer.valueOf(getMessagesStorage().getDialogReadMax(newMsgObj.out, newMsgObj.dialog_id));
                        getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(newMsgObj.dialog_id), value);
                    }
                    newMsgObj.unread = value.intValue() < newMsgObj.id;
                }
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda63
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.m1169xe6d0a9c7(res);
                    }
                });
                sentMessages.add(newMsgObj);
                existFlags = existFlags3;
                currentSchedule = false;
                isSentError = false;
            } else {
                attachPath = attachPath2;
                sentMessages = sentMessages2;
                oldId = oldId2;
                if (response instanceof TLRPC.Updates) {
                    final TLRPC.Updates updates = (TLRPC.Updates) response;
                    ArrayList<TLRPC.Update> updatesArr2 = ((TLRPC.Updates) response).updates;
                    TLRPC.Message message2 = null;
                    LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies2 = null;
                    int a = 0;
                    while (true) {
                        if (a >= updatesArr2.size()) {
                            currentSchedule = currentSchedule2;
                            isSentError = isSentError2;
                            channelReplies = null;
                            message = message2;
                            break;
                        }
                        TLRPC.Update update = updatesArr2.get(a);
                        if (update instanceof TLRPC.TL_updateNewMessage) {
                            final TLRPC.TL_updateNewMessage newMessage = (TLRPC.TL_updateNewMessage) update;
                            TLRPC.Message message3 = newMessage.message;
                            sentMessages.add(message3);
                            currentSchedule = currentSchedule2;
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda61
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.m1170xcc121888(newMessage);
                                }
                            });
                            updatesArr2.remove(a);
                            isSentError = isSentError2;
                            message = message3;
                            channelReplies = null;
                            break;
                        }
                        TLRPC.Message message4 = message2;
                        currentSchedule = currentSchedule2;
                        if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                            final TLRPC.TL_updateNewChannelMessage newMessage2 = (TLRPC.TL_updateNewChannelMessage) update;
                            long channelId = MessagesController.getUpdateChannelId(newMessage2);
                            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(channelId));
                            if (chat == null || chat.megagroup) {
                                if (newMessage2.message.reply_to == null) {
                                    isSentError = isSentError2;
                                } else if (newMessage2.message.reply_to.reply_to_top_id != 0 || newMessage2.message.reply_to.reply_to_msg_id != 0) {
                                    if (0 == 0) {
                                        channelReplies2 = new LongSparseArray<>();
                                    }
                                    isSentError = isSentError2;
                                    long did = MessageObject.getDialogId(newMessage2.message);
                                    SparseArray<TLRPC.MessageReplies> replies2 = channelReplies2.get(did);
                                    if (replies2 == null) {
                                        replies = new SparseArray<>();
                                        channelReplies2.put(did, replies);
                                    } else {
                                        replies = replies2;
                                    }
                                    LongSparseArray<SparseArray<TLRPC.MessageReplies>> channelReplies3 = channelReplies2;
                                    int id = newMessage2.message.reply_to.reply_to_top_id != 0 ? newMessage2.message.reply_to.reply_to_top_id : newMessage2.message.reply_to.reply_to_msg_id;
                                    TLRPC.MessageReplies messageReplies2 = replies.get(id);
                                    if (messageReplies2 == null) {
                                        messageReplies = new TLRPC.TL_messageReplies();
                                        replies.put(id, messageReplies);
                                    } else {
                                        messageReplies = messageReplies2;
                                    }
                                    if (newMessage2.message.from_id != null) {
                                        messageReplies.recent_repliers.add(0, newMessage2.message.from_id);
                                    }
                                    messageReplies.replies++;
                                    channelReplies2 = channelReplies3;
                                }
                                TLRPC.Message message5 = newMessage2.message;
                                message = message5;
                                sentMessages.add(message5);
                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda59
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.this.m1171xb1538749(newMessage2);
                                    }
                                });
                                updatesArr2.remove(a);
                                channelReplies = channelReplies2;
                            }
                            isSentError = isSentError2;
                            TLRPC.Message message52 = newMessage2.message;
                            message = message52;
                            sentMessages.add(message52);
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda59
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.m1171xb1538749(newMessage2);
                                }
                            });
                            updatesArr2.remove(a);
                            channelReplies = channelReplies2;
                        } else {
                            isSentError = isSentError2;
                            if (!(update instanceof TLRPC.TL_updateNewScheduledMessage)) {
                                a++;
                                message2 = message4;
                                currentSchedule2 = currentSchedule;
                                isSentError2 = isSentError;
                            } else {
                                TLRPC.Message message6 = ((TLRPC.TL_updateNewScheduledMessage) update).message;
                                message = message6;
                                sentMessages.add(message6);
                                updatesArr2.remove(a);
                                channelReplies = null;
                                break;
                            }
                        }
                    }
                    if (channelReplies == null) {
                        updatesArr = updatesArr2;
                    } else {
                        getMessagesStorage().putChannelViews(null, null, channelReplies, true);
                        updatesArr = updatesArr2;
                        getNotificationCenter().postNotificationName(NotificationCenter.didUpdateMessagesViews, null, null, channelReplies, true);
                    }
                    if (message != null) {
                        MessageObject.getDialogId(message);
                        if (scheduledOnline && message.date != 2147483646) {
                            currentSchedule = false;
                        }
                        ImageLoader.saveMessageThumbs(message);
                        if (!currentSchedule) {
                            Integer value2 = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                            if (value2 == null) {
                                value2 = Integer.valueOf(getMessagesStorage().getDialogReadMax(message.out, message.dialog_id));
                                getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), value2);
                            }
                            message.unread = value2.intValue() < message.id;
                        }
                        msgObj.messageOwner.post_author = message.post_author;
                        if ((message.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            msgObj.messageOwner.ttl_period = message.ttl_period;
                            msgObj.messageOwner.flags |= ConnectionsManager.FileTypeVideo;
                        }
                        msgObj.messageOwner.entities = message.entities;
                        updateMediaPaths(msgObj, message, message.id, originalPath, false);
                        existFlags2 = msgObj.getMediaExistanceFlags();
                        newMsgObj.id = message.id;
                    } else {
                        isSentError = true;
                        existFlags2 = 0;
                    }
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda64
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.m1172x9694f60a(updates);
                        }
                    });
                    existFlags = existFlags2;
                } else {
                    currentSchedule = currentSchedule2;
                    isSentError = false;
                    existFlags = 0;
                }
            }
            if (MessageObject.isLiveLocationMessage(newMsgObj) && newMsgObj.via_bot_id == 0 && TextUtils.isEmpty(newMsgObj.via_bot_name)) {
                getLocationController().addSharingLocation(newMsgObj);
            }
            if (!isSentError) {
                getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                newMsgObj.send_state = 0;
                if (scheduled && !currentSchedule) {
                    ArrayList<Integer> messageIds = new ArrayList<>();
                    messageIds.add(Integer.valueOf(oldId));
                    getMessagesController().deleteMessages(messageIds, null, null, newMsgObj.dialog_id, false, true);
                    final ArrayList<TLRPC.Message> arrayList = sentMessages;
                    final int i2 = oldId;
                    final String str = attachPath;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.m1174x6117d38c(arrayList, msgObj, newMsgObj, i2, scheduled, str);
                        }
                    });
                } else {
                    getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id), 0L, Integer.valueOf(existFlags), Boolean.valueOf(scheduled));
                    final int i3 = oldId;
                    final ArrayList<TLRPC.Message> arrayList2 = sentMessages;
                    final int i4 = existFlags;
                    final String str2 = attachPath;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda43
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.m1176x2b9ab10e(newMsgObj, i3, scheduled, arrayList2, i4, str2);
                        }
                    });
                }
            }
        }
        if (isSentError) {
            getMessagesStorage().markMessageAsSendError(newMsgObj, scheduled);
            newMsgObj.send_state = 2;
            getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj.id));
            processSentMessage(newMsgObj.id);
            if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
                stopVideoService(newMsgObj.attachPath);
            }
            removeFromSendingMessages(newMsgObj.id, scheduled);
        }
    }

    /* renamed from: lambda$performSendMessageRequest$51$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1169xe6d0a9c7(TLRPC.TL_updateShortSentMessage res) {
        getMessagesController().processNewDifferenceParams(-1, res.pts, res.date, res.pts_count);
    }

    /* renamed from: lambda$performSendMessageRequest$52$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1170xcc121888(TLRPC.TL_updateNewMessage newMessage) {
        getMessagesController().processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
    }

    /* renamed from: lambda$performSendMessageRequest$53$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1171xb1538749(TLRPC.TL_updateNewChannelMessage newMessage) {
        getMessagesController().processNewChannelDifferenceParams(newMessage.pts, newMessage.pts_count, newMessage.message.peer_id.channel_id);
    }

    /* renamed from: lambda$performSendMessageRequest$54$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1172x9694f60a(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    /* renamed from: lambda$performSendMessageRequest$56$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1174x6117d38c(ArrayList sentMessages, final MessageObject msgObj, final TLRPC.Message newMsgObj, final int oldId, final boolean scheduled, String attachPath) {
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) sentMessages, true, false, false, 0, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1173x7bd664cb(msgObj, newMsgObj, oldId, scheduled);
            }
        });
        if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
            stopVideoService(attachPath);
        }
    }

    /* renamed from: lambda$performSendMessageRequest$55$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1173x7bd664cb(MessageObject msgObj, TLRPC.Message newMsgObj, int oldId, boolean scheduled) {
        ArrayList<MessageObject> messageObjects = new ArrayList<>();
        messageObjects.add(new MessageObject(msgObj.currentAccount, msgObj.messageOwner, true, true));
        getMessagesController().updateInterfaceWithMessages(newMsgObj.dialog_id, messageObjects, false);
        getMediaDataController().increasePeerRaiting(newMsgObj.dialog_id);
        processSentMessage(oldId);
        removeFromSendingMessages(oldId, scheduled);
    }

    /* renamed from: lambda$performSendMessageRequest$58$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1176x2b9ab10e(final TLRPC.Message newMsgObj, final int oldId, final boolean scheduled, ArrayList sentMessages, final int existFlags, String attachPath) {
        getMessagesStorage().updateMessageStateAndId(newMsgObj.random_id, MessageObject.getPeerId(newMsgObj.peer_id), Integer.valueOf(oldId), newMsgObj.id, 0, false, scheduled ? 1 : 0);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) sentMessages, true, false, false, 0, scheduled);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1175x4659424d(newMsgObj, oldId, existFlags, scheduled);
            }
        });
        if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
            stopVideoService(attachPath);
        }
    }

    /* renamed from: lambda$performSendMessageRequest$57$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1175x4659424d(TLRPC.Message newMsgObj, int oldId, int existFlags, boolean scheduled) {
        getMediaDataController().increasePeerRaiting(newMsgObj.dialog_id);
        getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id), 0L, Integer.valueOf(existFlags), Boolean.valueOf(scheduled));
        processSentMessage(oldId);
        removeFromSendingMessages(oldId, scheduled);
    }

    /* renamed from: lambda$performSendMessageRequest$62$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1180x8efe81e7(final TLRPC.Message newMsgObj) {
        final int msg_id = newMsgObj.id;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1179xa9bd1326(newMsgObj, msg_id);
            }
        });
    }

    /* renamed from: lambda$performSendMessageRequest$61$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1179xa9bd1326(TLRPC.Message newMsgObj, int msg_id) {
        newMsgObj.send_state = 0;
        getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByAck, Integer.valueOf(msg_id));
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x03ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMediaPaths(MessageObject newMsgObj, TLRPC.Message sentMessage, int newMsgId, String originalPath, boolean post) {
        TLRPC.PhotoSize strippedNew;
        MessageObject messageObject;
        byte[] oldWaveform;
        boolean save;
        int b;
        int b2;
        TLRPC.Message newMsg;
        TLRPC.PhotoSize strippedNew2;
        boolean found;
        File cacheFile2;
        TLRPC.Message newMsg2 = newMsgObj.messageOwner;
        TLRPC.PhotoSize strippedNew3 = null;
        if (newMsg2.media == null) {
            strippedNew = null;
        } else {
            TLRPC.PhotoSize strippedOld = null;
            TLObject photoObject = null;
            if (newMsgObj.isLiveLocation() && (sentMessage.media instanceof TLRPC.TL_messageMediaGeoLive)) {
                newMsg2.media.period = sentMessage.media.period;
            } else if (newMsgObj.isDice()) {
                TLRPC.TL_messageMediaDice mediaDice = (TLRPC.TL_messageMediaDice) newMsg2.media;
                TLRPC.TL_messageMediaDice mediaDiceNew = (TLRPC.TL_messageMediaDice) sentMessage.media;
                mediaDice.value = mediaDiceNew.value;
            } else if (newMsg2.media.photo != null) {
                strippedOld = FileLoader.getClosestPhotoSizeWithSize(newMsg2.media.photo.sizes, 40);
                if (sentMessage != null && sentMessage.media != null && sentMessage.media.photo != null) {
                    strippedNew3 = FileLoader.getClosestPhotoSizeWithSize(sentMessage.media.photo.sizes, 40);
                } else {
                    strippedNew3 = strippedOld;
                }
                photoObject = newMsg2.media.photo;
            } else if (newMsg2.media.document != null) {
                strippedOld = FileLoader.getClosestPhotoSizeWithSize(newMsg2.media.document.thumbs, 40);
                if (sentMessage != null && sentMessage.media != null && sentMessage.media.document != null) {
                    strippedNew3 = FileLoader.getClosestPhotoSizeWithSize(sentMessage.media.document.thumbs, 40);
                } else {
                    strippedNew3 = strippedOld;
                }
                photoObject = newMsg2.media.document;
            } else if (newMsg2.media.webpage != null) {
                if (newMsg2.media.webpage.photo != null) {
                    strippedOld = FileLoader.getClosestPhotoSizeWithSize(newMsg2.media.webpage.photo.sizes, 40);
                    if (sentMessage != null && sentMessage.media != null && sentMessage.media.webpage != null && sentMessage.media.webpage.photo != null) {
                        strippedNew3 = FileLoader.getClosestPhotoSizeWithSize(sentMessage.media.webpage.photo.sizes, 40);
                    } else {
                        strippedNew3 = strippedOld;
                    }
                    photoObject = newMsg2.media.webpage.photo;
                } else if (newMsg2.media.webpage.document != null) {
                    strippedOld = FileLoader.getClosestPhotoSizeWithSize(newMsg2.media.webpage.document.thumbs, 40);
                    if (sentMessage != null && sentMessage.media != null && sentMessage.media.webpage != null && sentMessage.media.webpage.document != null) {
                        strippedNew3 = FileLoader.getClosestPhotoSizeWithSize(sentMessage.media.webpage.document.thumbs, 40);
                    } else {
                        strippedNew3 = strippedOld;
                    }
                    photoObject = newMsg2.media.webpage.document;
                }
            }
            if ((strippedNew3 instanceof TLRPC.TL_photoStrippedSize) && (strippedOld instanceof TLRPC.TL_photoStrippedSize)) {
                String oldKey = "stripped" + FileRefController.getKeyForParentObject(newMsgObj);
                String newKey = sentMessage != null ? "stripped" + FileRefController.getKeyForParentObject(sentMessage) : "strippedmessage" + newMsgId + "_" + newMsgObj.getChannelId() + "_" + newMsgObj.scheduled;
                ImageLoader.getInstance().replaceImageInCache(oldKey, newKey, ImageLocation.getForObject(strippedNew3, photoObject), post);
            }
            strippedNew = strippedNew3;
        }
        if (sentMessage == null) {
            return;
        }
        if ((sentMessage.media instanceof TLRPC.TL_messageMediaPhoto) && sentMessage.media.photo != null && (newMsg2.media instanceof TLRPC.TL_messageMediaPhoto) && newMsg2.media.photo != null) {
            if (sentMessage.media.ttl_seconds == 0 && !newMsgObj.scheduled) {
                getMessagesStorage().putSentFile(originalPath, sentMessage.media.photo, 0, "sent_" + sentMessage.peer_id.channel_id + "_" + sentMessage.id);
            }
            if (newMsg2.media.photo.sizes.size() == 1 && (newMsg2.media.photo.sizes.get(0).location instanceof TLRPC.TL_fileLocationUnavailable)) {
                newMsg2.media.photo.sizes = sentMessage.media.photo.sizes;
            } else {
                int b3 = 0;
                while (b3 < newMsg2.media.photo.sizes.size()) {
                    TLRPC.PhotoSize size2 = newMsg2.media.photo.sizes.get(b3);
                    if (size2 == null || size2.location == null) {
                        b = b3;
                    } else if (size2.type == null) {
                        b = b3;
                    } else {
                        boolean found2 = false;
                        int a = 0;
                        while (a < sentMessage.media.photo.sizes.size()) {
                            TLRPC.PhotoSize size = sentMessage.media.photo.sizes.get(a);
                            if (size == null || size.location == null || (size instanceof TLRPC.TL_photoSizeEmpty)) {
                                b2 = b3;
                            } else if (size.type == null) {
                                b2 = b3;
                            } else {
                                b2 = b3;
                                if ((size2.location.volume_id == -2147483648L && size.type.equals(size2.type)) || (size.w == size2.w && size.h == size2.h)) {
                                    StringBuilder sb = new StringBuilder();
                                    newMsg = newMsg2;
                                    strippedNew2 = strippedNew;
                                    sb.append(size2.location.volume_id);
                                    sb.append("_");
                                    sb.append(size2.location.local_id);
                                    String fileName = sb.toString();
                                    String fileName2 = size.location.volume_id + "_" + size.location.local_id;
                                    if (!fileName.equals(fileName2)) {
                                        found = true;
                                        File cacheFile = new File(FileLoader.getDirectory(4), fileName + ".jpg");
                                        if (sentMessage.media.ttl_seconds == 0 && (sentMessage.media.photo.sizes.size() == 1 || size.w > 90 || size.h > 90)) {
                                            cacheFile2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(size);
                                        } else {
                                            cacheFile2 = new File(FileLoader.getDirectory(4), fileName2 + ".jpg");
                                        }
                                        cacheFile.renameTo(cacheFile2);
                                        ImageLoader.getInstance().replaceImageInCache(fileName, fileName2, ImageLocation.getForPhoto(size, sentMessage.media.photo), post);
                                        size2.location = size.location;
                                        size2.size = size.size;
                                    } else {
                                        found = true;
                                    }
                                    found2 = found;
                                    if (!found2) {
                                        newMsg2 = newMsg;
                                        b = b2;
                                        strippedNew = strippedNew2;
                                    } else {
                                        String fileName3 = size2.location.volume_id + "_" + size2.location.local_id;
                                        new File(FileLoader.getDirectory(4), fileName3 + ".jpg").delete();
                                        if (!"s".equals(size2.type) || strippedNew2 == null) {
                                            newMsg2 = newMsg;
                                            b = b2;
                                            strippedNew = strippedNew2;
                                        } else {
                                            newMsg2 = newMsg;
                                            b = b2;
                                            strippedNew = strippedNew2;
                                            newMsg2.media.photo.sizes.set(b, strippedNew);
                                            ImageLocation location = ImageLocation.getForPhoto(strippedNew, sentMessage.media.photo);
                                            ImageLoader.getInstance().replaceImageInCache(fileName3, location.getKey(sentMessage, null, false), location, post);
                                        }
                                    }
                                }
                            }
                            a++;
                            newMsg2 = newMsg2;
                            b3 = b2;
                            strippedNew = strippedNew;
                        }
                        b2 = b3;
                        newMsg = newMsg2;
                        strippedNew2 = strippedNew;
                        if (!found2) {
                        }
                    }
                    b3 = b + 1;
                }
            }
            newMsg2.message = sentMessage.message;
            sentMessage.attachPath = newMsg2.attachPath;
            newMsg2.media.photo.id = sentMessage.media.photo.id;
            newMsg2.media.photo.dc_id = sentMessage.media.photo.dc_id;
            newMsg2.media.photo.access_hash = sentMessage.media.photo.access_hash;
        } else if ((sentMessage.media instanceof TLRPC.TL_messageMediaDocument) && sentMessage.media.document != null && (newMsg2.media instanceof TLRPC.TL_messageMediaDocument) && newMsg2.media.document != null) {
            if (sentMessage.media.ttl_seconds != 0) {
                messageObject = newMsgObj;
            } else if (newMsgObj.videoEditedInfo == null || (newMsgObj.videoEditedInfo.mediaEntities == null && TextUtils.isEmpty(newMsgObj.videoEditedInfo.paintPath) && newMsgObj.videoEditedInfo.cropState == null)) {
                boolean isVideo = MessageObject.isVideoMessage(sentMessage);
                if ((isVideo || MessageObject.isGifMessage(sentMessage)) && MessageObject.isGifDocument(sentMessage.media.document) == MessageObject.isGifDocument(newMsg2.media.document)) {
                    if (!newMsgObj.scheduled) {
                        getMessagesStorage().putSentFile(originalPath, sentMessage.media.document, 2, "sent_" + sentMessage.peer_id.channel_id + "_" + sentMessage.id);
                    }
                    if (!isVideo) {
                        messageObject = newMsgObj;
                    } else {
                        sentMessage.attachPath = newMsg2.attachPath;
                        messageObject = newMsgObj;
                    }
                } else if (MessageObject.isVoiceMessage(sentMessage) || MessageObject.isRoundVideoMessage(sentMessage)) {
                    messageObject = newMsgObj;
                } else {
                    messageObject = newMsgObj;
                    if (!messageObject.scheduled) {
                        getMessagesStorage().putSentFile(originalPath, sentMessage.media.document, 1, "sent_" + sentMessage.peer_id.channel_id + "_" + sentMessage.id);
                    }
                }
            } else {
                messageObject = newMsgObj;
            }
            TLRPC.PhotoSize size22 = FileLoader.getClosestPhotoSizeWithSize(newMsg2.media.document.thumbs, GroupCallActivity.TABLET_LIST_SIZE);
            TLRPC.PhotoSize size3 = FileLoader.getClosestPhotoSizeWithSize(sentMessage.media.document.thumbs, GroupCallActivity.TABLET_LIST_SIZE);
            if (size22 != null && size22.location != null && size22.location.volume_id == -2147483648L && size3 != null && size3.location != null && !(size3 instanceof TLRPC.TL_photoSizeEmpty) && !(size22 instanceof TLRPC.TL_photoSizeEmpty)) {
                String fileName4 = size22.location.volume_id + "_" + size22.location.local_id;
                String fileName22 = size3.location.volume_id + "_" + size3.location.local_id;
                if (!fileName4.equals(fileName22)) {
                    new File(FileLoader.getDirectory(4), fileName4 + ".jpg").renameTo(new File(FileLoader.getDirectory(4), fileName22 + ".jpg"));
                    ImageLoader.getInstance().replaceImageInCache(fileName4, fileName22, ImageLocation.getForDocument(size3, sentMessage.media.document), post);
                    size22.location = size3.location;
                    size22.size = size3.size;
                }
            } else if (size3 != null && size22 != null && MessageObject.isStickerMessage(sentMessage) && size22.location != null) {
                size3.location = size22.location;
            } else if (size22 == null || ((size22 != null && (size22.location instanceof TLRPC.TL_fileLocationUnavailable)) || (size22 instanceof TLRPC.TL_photoSizeEmpty))) {
                newMsg2.media.document.thumbs = sentMessage.media.document.thumbs;
            }
            newMsg2.media.document.dc_id = sentMessage.media.document.dc_id;
            newMsg2.media.document.id = sentMessage.media.document.id;
            newMsg2.media.document.access_hash = sentMessage.media.document.access_hash;
            int a2 = 0;
            while (true) {
                if (a2 >= newMsg2.media.document.attributes.size()) {
                    oldWaveform = null;
                    break;
                }
                TLRPC.DocumentAttribute attribute = newMsg2.media.document.attributes.get(a2);
                if (!(attribute instanceof TLRPC.TL_documentAttributeAudio)) {
                    a2++;
                } else {
                    byte[] oldWaveform2 = attribute.waveform;
                    oldWaveform = oldWaveform2;
                    break;
                }
            }
            newMsg2.media.document.attributes = sentMessage.media.document.attributes;
            if (oldWaveform != null) {
                for (int a3 = 0; a3 < newMsg2.media.document.attributes.size(); a3++) {
                    TLRPC.DocumentAttribute attribute2 = newMsg2.media.document.attributes.get(a3);
                    if (attribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                        attribute2.waveform = oldWaveform;
                        attribute2.flags |= 4;
                    }
                }
            }
            newMsg2.media.document.size = sentMessage.media.document.size;
            newMsg2.media.document.mime_type = sentMessage.media.document.mime_type;
            if ((sentMessage.flags & 4) == 0 && MessageObject.isOut(sentMessage)) {
                if (MessageObject.isNewGifDocument(sentMessage.media.document)) {
                    if (MessageObject.isDocumentHasAttachedStickers(sentMessage.media.document)) {
                        save = getMessagesController().saveGifsWithStickers;
                    } else {
                        save = true;
                    }
                    if (save) {
                        getMediaDataController().addRecentGif(sentMessage.media.document, sentMessage.date, true);
                    }
                } else if (MessageObject.isStickerDocument(sentMessage.media.document) || MessageObject.isAnimatedStickerDocument(sentMessage.media.document, true)) {
                    getMediaDataController().addRecentSticker(0, sentMessage, sentMessage.media.document, sentMessage.date, false);
                }
            }
            if (newMsg2.attachPath != null && newMsg2.attachPath.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                File cacheFile3 = new File(newMsg2.attachPath);
                File cacheFile22 = FileLoader.getInstance(this.currentAccount).getPathToAttach(sentMessage.media.document, sentMessage.media.ttl_seconds != 0);
                if (!cacheFile3.renameTo(cacheFile22)) {
                    if (cacheFile3.exists()) {
                        sentMessage.attachPath = newMsg2.attachPath;
                    } else {
                        messageObject.attachPathExists = false;
                    }
                    messageObject.mediaExists = cacheFile22.exists();
                    sentMessage.message = newMsg2.message;
                    return;
                } else if (MessageObject.isVideoMessage(sentMessage)) {
                    messageObject.attachPathExists = true;
                    return;
                } else {
                    messageObject.mediaExists = messageObject.attachPathExists;
                    messageObject.attachPathExists = false;
                    newMsg2.attachPath = "";
                    if (originalPath != null && originalPath.startsWith("http")) {
                        getMessagesStorage().addRecentLocalFile(originalPath, cacheFile22.toString(), newMsg2.media.document);
                        return;
                    }
                    return;
                }
            }
            sentMessage.attachPath = newMsg2.attachPath;
            sentMessage.message = newMsg2.message;
        } else if ((sentMessage.media instanceof TLRPC.TL_messageMediaContact) && (newMsg2.media instanceof TLRPC.TL_messageMediaContact)) {
            newMsg2.media = sentMessage.media;
        } else if (sentMessage.media instanceof TLRPC.TL_messageMediaWebPage) {
            newMsg2.media = sentMessage.media;
        } else if (sentMessage.media instanceof TLRPC.TL_messageMediaGeo) {
            sentMessage.media.geo.lat = newMsg2.media.geo.lat;
            sentMessage.media.geo._long = newMsg2.media.geo._long;
        } else if ((sentMessage.media instanceof TLRPC.TL_messageMediaGame) || (sentMessage.media instanceof TLRPC.TL_messageMediaInvoice)) {
            newMsg2.media = sentMessage.media;
            if (!TextUtils.isEmpty(sentMessage.message)) {
                newMsg2.entities = sentMessage.entities;
                newMsg2.message = sentMessage.message;
            }
            if (sentMessage.reply_markup != null) {
                newMsg2.reply_markup = sentMessage.reply_markup;
                newMsg2.flags |= 64;
            }
        } else if (sentMessage.media instanceof TLRPC.TL_messageMediaPoll) {
            newMsg2.media = sentMessage.media;
        }
    }

    private void putToDelayedMessages(String location, DelayedMessage message) {
        ArrayList<DelayedMessage> arrayList = this.delayedMessages.get(location);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.delayedMessages.put(location, arrayList);
        }
        arrayList.add(message);
    }

    public ArrayList<DelayedMessage> getDelayedMessages(String location) {
        return this.delayedMessages.get(location);
    }

    public long getNextRandomId() {
        long val = 0;
        while (val == 0) {
            val = Utilities.random.nextLong();
        }
        return val;
    }

    public void checkUnsentMessages() {
        getMessagesStorage().getUnsentMessages(1000);
    }

    public void processUnsentMessages(final ArrayList<TLRPC.Message> messages, final ArrayList<TLRPC.Message> scheduledMessages, final ArrayList<TLRPC.User> users, final ArrayList<TLRPC.Chat> chats, final ArrayList<TLRPC.EncryptedChat> encryptedChats) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1194x625b480d(users, chats, encryptedChats, messages, scheduledMessages);
            }
        });
    }

    /* renamed from: lambda$processUnsentMessages$63$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1194x625b480d(ArrayList users, ArrayList chats, ArrayList encryptedChats, ArrayList messages, ArrayList scheduledMessages) {
        getMessagesController().putUsers(users, true);
        getMessagesController().putChats(chats, true);
        getMessagesController().putEncryptedChats(encryptedChats, true);
        int N = messages.size();
        for (int a = 0; a < N; a++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) messages.get(a), false, true);
            long groupId = messageObject.getGroupId();
            if (groupId != 0 && messageObject.messageOwner.params != null && !messageObject.messageOwner.params.containsKey("final") && (a == N - 1 || ((TLRPC.Message) messages.get(a + 1)).grouped_id != groupId)) {
                messageObject.messageOwner.params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
            }
            retrySendMessage(messageObject, true);
        }
        if (scheduledMessages != null) {
            for (int a2 = 0; a2 < scheduledMessages.size(); a2++) {
                MessageObject messageObject2 = new MessageObject(this.currentAccount, (TLRPC.Message) scheduledMessages.get(a2), false, true);
                messageObject2.scheduled = true;
                retrySendMessage(messageObject2, true);
            }
        }
    }

    public ImportingStickers getImportingStickers(String shortName) {
        return this.importingStickersMap.get(shortName);
    }

    public ImportingHistory getImportingHistory(long dialogId) {
        return this.importingHistoryMap.get(dialogId);
    }

    public boolean isImportingStickers() {
        return this.importingStickersMap.size() != 0;
    }

    public boolean isImportingHistory() {
        return this.importingHistoryMap.size() != 0;
    }

    public void prepareImportHistory(final long dialogId, final Uri uri, final ArrayList<Uri> mediaUris, final MessagesStorage.LongCallback onStartImport) {
        TLRPC.Chat chat;
        if (this.importingHistoryMap.get(dialogId) != null) {
            onStartImport.run(0L);
        } else if (DialogObject.isChatDialog(dialogId) && (chat = getMessagesController().getChat(Long.valueOf(-dialogId))) != null && !chat.megagroup) {
            getMessagesController().convertToMegaGroup(null, -dialogId, null, new MessagesStorage.LongCallback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda73
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    SendMessagesHelper.this.m1189x12cad7b8(uri, mediaUris, onStartImport, j);
                }
            });
        } else {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1191x8d12017d(mediaUris, dialogId, uri, onStartImport);
                }
            }).start();
        }
    }

    /* renamed from: lambda$prepareImportHistory$64$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1189x12cad7b8(Uri uri, ArrayList mediaUris, MessagesStorage.LongCallback onStartImport, long chatId) {
        if (chatId != 0) {
            prepareImportHistory(-chatId, uri, mediaUris, onStartImport);
        } else {
            onStartImport.run(0L);
        }
    }

    /* renamed from: lambda$prepareImportHistory$69$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1191x8d12017d(ArrayList mediaUris, final long dialogId, Uri uri, final MessagesStorage.LongCallback onStartImport) {
        Uri mediaUri;
        ArrayList arrayList = mediaUris != null ? mediaUris : new ArrayList();
        final ImportingHistory importingHistory = new ImportingHistory();
        importingHistory.mediaPaths = arrayList;
        importingHistory.dialogId = dialogId;
        importingHistory.peer = getMessagesController().getInputPeer(dialogId);
        final HashMap<String, ImportingHistory> files = new HashMap<>();
        int N = arrayList.size();
        for (int a = 0; a < N + 1; a++) {
            if (a == 0) {
                mediaUri = uri;
            } else {
                mediaUri = (Uri) arrayList.get(a - 1);
            }
            if (mediaUri == null || AndroidUtilities.isInternalUri(mediaUri)) {
                if (a == 0) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagesStorage.LongCallback.this.run(0L);
                        }
                    });
                    return;
                }
            } else {
                String path = MediaController.copyFileToCache(mediaUri, "txt");
                if (path == null) {
                    continue;
                } else {
                    File f = new File(path);
                    if (f.exists()) {
                        long size = f.length();
                        if (size != 0) {
                            importingHistory.totalSize += size;
                            if (a == 0) {
                                if (size > 33554432) {
                                    f.delete();
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda10
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.lambda$prepareImportHistory$67(MessagesStorage.LongCallback.this);
                                        }
                                    });
                                    return;
                                }
                                importingHistory.historyPath = path;
                            } else {
                                importingHistory.uploadMedia.add(path);
                            }
                            importingHistory.uploadSet.add(path);
                            files.put(path, importingHistory);
                        }
                    }
                    if (a == 0) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                MessagesStorage.LongCallback.this.run(0L);
                            }
                        });
                        return;
                    }
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1190xa7d092bc(files, dialogId, importingHistory, onStartImport);
            }
        });
    }

    public static /* synthetic */ void lambda$prepareImportHistory$67(MessagesStorage.LongCallback onStartImport) {
        Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("ImportFileTooLarge", org.telegram.messenger.beta.R.string.ImportFileTooLarge), 0).show();
        onStartImport.run(0L);
    }

    /* renamed from: lambda$prepareImportHistory$68$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1190xa7d092bc(HashMap files, long dialogId, ImportingHistory importingHistory, MessagesStorage.LongCallback onStartImport) {
        this.importingHistoryFiles.putAll(files);
        this.importingHistoryMap.put(dialogId, importingHistory);
        getFileLoader().uploadFile(importingHistory.historyPath, false, true, 0L, ConnectionsManager.FileTypeFile, true);
        getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(dialogId));
        onStartImport.run(dialogId);
        Intent intent = new Intent(ApplicationLoader.applicationContext, ImportingService.class);
        try {
            ApplicationLoader.applicationContext.startService(intent);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void prepareImportStickers(final String title, final String shortName, final String sofrware, final ArrayList<ImportingSticker> paths, final MessagesStorage.StringCallback onStartImport) {
        if (this.importingStickersMap.get(shortName) != null) {
            onStartImport.run(null);
        } else {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.m1193xfa297655(title, shortName, sofrware, paths, onStartImport);
                }
            }).start();
        }
    }

    /* renamed from: lambda$prepareImportStickers$72$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1193xfa297655(String title, final String shortName, String sofrware, ArrayList paths, final MessagesStorage.StringCallback onStartImport) {
        final ImportingStickers importingStickers = new ImportingStickers();
        importingStickers.title = title;
        importingStickers.shortName = shortName;
        importingStickers.software = sofrware;
        final HashMap<String, ImportingStickers> files = new HashMap<>();
        int N = paths.size();
        for (int a = 0; a < N; a++) {
            ImportingSticker sticker = (ImportingSticker) paths.get(a);
            File f = new File(sticker.path);
            if (f.exists()) {
                long size = f.length();
                if (size != 0) {
                    importingStickers.totalSize += size;
                    importingStickers.uploadMedia.add(sticker);
                    importingStickers.uploadSet.put(sticker.path, sticker);
                    files.put(sticker.path, importingStickers);
                }
            }
            if (a == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagesStorage.StringCallback.this.run(null);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.m1192x14e80794(importingStickers, files, shortName, onStartImport);
            }
        });
    }

    /* renamed from: lambda$prepareImportStickers$71$org-telegram-messenger-SendMessagesHelper */
    public /* synthetic */ void m1192x14e80794(ImportingStickers importingStickers, HashMap files, String shortName, MessagesStorage.StringCallback onStartImport) {
        if (importingStickers.uploadMedia.get(0).item != null) {
            importingStickers.startImport();
        } else {
            this.importingStickersFiles.putAll(files);
            this.importingStickersMap.put(shortName, importingStickers);
            importingStickers.initImport();
            getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, shortName);
            onStartImport.run(shortName);
        }
        Intent intent = new Intent(ApplicationLoader.applicationContext, ImportingService.class);
        try {
            ApplicationLoader.applicationContext.startService(intent);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public TLRPC.TL_photo generatePhotoSizes(String path, Uri imageUri) {
        return generatePhotoSizes(null, path, imageUri);
    }

    public TLRPC.TL_photo generatePhotoSizes(TLRPC.TL_photo photo, String path, Uri imageUri) {
        Bitmap bitmap = ImageLoader.loadBitmap(path, imageUri, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true);
        if (bitmap == null) {
            bitmap = ImageLoader.loadBitmap(path, imageUri, 800.0f, 800.0f, true);
        }
        ArrayList<TLRPC.PhotoSize> sizes = new ArrayList<>();
        TLRPC.PhotoSize size = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, true);
        if (size != null) {
            sizes.add(size);
        }
        TLRPC.PhotoSize size2 = ImageLoader.scaleAndSaveImage(bitmap, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true, 80, false, 101, 101);
        if (size2 != null) {
            sizes.add(size2);
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (sizes.isEmpty()) {
            return null;
        }
        getUserConfig().saveConfig(false);
        if (photo == null) {
            photo = new TLRPC.TL_photo();
        }
        photo.date = getConnectionsManager().getCurrentTime();
        photo.sizes = sizes;
        photo.file_reference = new byte[0];
        return photo;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(8:355|67|(7:69|359|70|71|353|72|73)(1:82)|(3:351|84|(6:86|87|(1:89)|333|95|98))|94|333|95|98) */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0196, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0197, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x027a  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0281  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02d2  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02df A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0580  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0586  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x05a5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x05ae  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x061a  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x061d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:339:0x01fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:349:0x01de A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int prepareSendingDocumentInternal(final AccountInstance accountInstance, String path, String originalPath, Uri uri, String mime, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, CharSequence caption, final ArrayList<TLRPC.MessageEntity> entities, final MessageObject editingMessageObject, long[] groupId, boolean isGroupFinal, boolean forceDocument, final boolean notify, final int scheduleDate, Integer[] docType) {
        String extension;
        String path2;
        String ext;
        String str;
        TLRPC.TL_documentAttributeAudio attributeAudio;
        String extL;
        long j;
        String permormer;
        String title;
        int duration;
        int duration2;
        TLRPC.TL_documentAttributeAudio attributeAudio2;
        String title2;
        TLRPC.TL_documentAttributeAudio attributeAudio3;
        boolean sendNew;
        String title3;
        String originalPath2;
        TLRPC.TL_documentAttributeAudio attributeAudio4;
        String str2;
        String parentObject;
        boolean isEncrypted;
        MimeTypeMap myMime;
        String path3;
        int duration3;
        Object obj;
        String name;
        String originalPath3;
        long j2;
        String path4;
        String permormer2;
        TLRPC.TL_document document;
        MimeTypeMap myMime2;
        String path5;
        boolean isEncrypted2;
        String str3;
        int i;
        TLRPC.TL_documentAttributeAudio attributeAudio5;
        boolean isEncrypted3;
        final String parentFinal;
        final HashMap<String, String> params;
        String originalPath4;
        Integer prevType;
        Integer prevType2;
        TLRPC.TL_documentAttributeAudio attributeAudio6;
        String extL2;
        Exception e;
        Exception e2;
        char c;
        TLRPC.TL_document document2;
        TLRPC.TL_document document3;
        String permormer3;
        String permormer4;
        Exception e3;
        Throwable th;
        String permormer5;
        if ((path == null || path.length() == 0) && uri == null) {
            return 1;
        }
        if (uri != null && AndroidUtilities.isInternalUri(uri)) {
            return 1;
        }
        if (path != null && AndroidUtilities.isInternalUri(Uri.fromFile(new File(path)))) {
            return 1;
        }
        MimeTypeMap myMime3 = MimeTypeMap.getSingleton();
        String extension2 = null;
        if (uri == null || path != null) {
            path2 = path;
            extension = null;
        } else if (checkFileSize(accountInstance, uri)) {
            return 2;
        } else {
            boolean hasExt = false;
            if (mime != null) {
                extension2 = myMime3.getExtensionFromMimeType(mime);
            }
            if (extension2 == null) {
                extension2 = "txt";
            } else {
                hasExt = true;
            }
            String path6 = MediaController.copyFileToCache(uri, extension2);
            if (path6 == null) {
                return 1;
            }
            if (!hasExt) {
                path2 = path6;
                extension = null;
            } else {
                path2 = path6;
                extension = extension2;
            }
        }
        File f = new File(path2);
        if (f.exists() && f.length() != 0) {
            if (!FileLoader.checkUploadFileSize(accountInstance.getCurrentAccount(), f.length())) {
                return 2;
            }
            boolean isEncrypted4 = DialogObject.isEncryptedDialog(dialogId);
            String name2 = f.getName();
            if (extension != null) {
                ext = extension;
            } else {
                int idx = path2.lastIndexOf(46);
                ext = idx != -1 ? path2.substring(idx + 1) : "";
            }
            String extL3 = ext.toLowerCase();
            String permormer6 = null;
            String title4 = null;
            boolean isVoice = false;
            int duration4 = 0;
            if (extL3.equals("mp3")) {
                attributeAudio = null;
                permormer3 = null;
            } else if (!extL3.equals("m4a")) {
                if (extL3.equals("opus") || extL3.equals("ogg") || extL3.equals("flac")) {
                    MediaMetadataRetriever mediaMetadataRetriever = null;
                    try {
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        try {
                            try {
                                mediaMetadataRetriever.setDataSource(f.getAbsolutePath());
                                String d = mediaMetadataRetriever.extractMetadata(9);
                                if (d != null) {
                                    attributeAudio = null;
                                    try {
                                        int duration5 = (int) Math.ceil(((float) Long.parseLong(d)) / 1000.0f);
                                        try {
                                            title4 = mediaMetadataRetriever.extractMetadata(7);
                                            permormer6 = mediaMetadataRetriever.extractMetadata(2);
                                            duration4 = duration5;
                                        } catch (Exception e4) {
                                            e3 = e4;
                                            duration4 = duration5;
                                            mediaMetadataRetriever = mediaMetadataRetriever;
                                            permormer6 = null;
                                            try {
                                                FileLog.e(e3);
                                                if (mediaMetadataRetriever != null) {
                                                }
                                                permormer = permormer6;
                                                extL = extL3;
                                                str = "flac";
                                                title = title4;
                                                duration = duration4;
                                                j = 0;
                                                if (duration != 0) {
                                                }
                                                if (originalPath == null) {
                                                }
                                                String parentObject2 = null;
                                                if (!sendNew) {
                                                }
                                                str2 = "";
                                                originalPath2 = title3;
                                                originalPath3 = "opus";
                                                name = name2;
                                                obj = "mp3";
                                                attributeAudio4 = attributeAudio3;
                                                isEncrypted = isEncrypted4;
                                                permormer2 = str;
                                                duration3 = -1;
                                                String str4 = path2;
                                                path4 = "m4a";
                                                long j3 = j;
                                                path3 = str4;
                                                myMime = myMime3;
                                                j2 = j3;
                                                parentObject = null;
                                                document = null;
                                                if (document == null) {
                                                }
                                                if (caption != null) {
                                                }
                                                isEncrypted3 = isEncrypted2;
                                                final TLRPC.TL_document documentFinal = document;
                                                final String pathFinal = path5;
                                                parentFinal = parentObject;
                                                params = new HashMap<>();
                                                if (originalPath2 != null) {
                                                }
                                                if (forceDocument) {
                                                }
                                                if (parentFinal != null) {
                                                }
                                                Integer prevType3 = Integer.valueOf(i);
                                                boolean isSticker = false;
                                                if (docType != null) {
                                                }
                                                if (!isEncrypted3) {
                                                }
                                                attributeAudio6 = attributeAudio5;
                                                prevType2 = prevType;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal, pathFinal, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                                                    }
                                                });
                                                return 0;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                if (mediaMetadataRetriever != null) {
                                                    try {
                                                        mediaMetadataRetriever.release();
                                                    } catch (Exception e5) {
                                                        FileLog.e(e5);
                                                    }
                                                }
                                                throw th;
                                            }
                                        } catch (Throwable th3) {
                                            mediaMetadataRetriever = mediaMetadataRetriever;
                                            th = th3;
                                            if (mediaMetadataRetriever != null) {
                                            }
                                            throw th;
                                        }
                                    } catch (Exception e6) {
                                        e3 = e6;
                                        mediaMetadataRetriever = mediaMetadataRetriever;
                                        permormer6 = null;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        mediaMetadataRetriever = mediaMetadataRetriever;
                                    }
                                } else {
                                    attributeAudio = null;
                                }
                                if (editingMessageObject == null) {
                                    try {
                                        if (extL3.equals("ogg")) {
                                            permormer5 = permormer6;
                                            if (MediaController.isOpusFile(f.getAbsolutePath()) == 1) {
                                                isVoice = true;
                                            }
                                            mediaMetadataRetriever.release();
                                            str = "flac";
                                            title = title4;
                                            permormer = permormer5;
                                            j = 0;
                                            extL = extL3;
                                            duration = duration4;
                                        }
                                    } catch (Exception e7) {
                                        e3 = e7;
                                        mediaMetadataRetriever = mediaMetadataRetriever;
                                        FileLog.e(e3);
                                        if (mediaMetadataRetriever != null) {
                                            try {
                                                mediaMetadataRetriever.release();
                                            } catch (Exception e8) {
                                                FileLog.e(e8);
                                            }
                                        }
                                        permormer = permormer6;
                                        extL = extL3;
                                        str = "flac";
                                        title = title4;
                                        duration = duration4;
                                        j = 0;
                                        if (duration != 0) {
                                        }
                                        if (originalPath == null) {
                                        }
                                        String parentObject22 = null;
                                        if (!sendNew) {
                                        }
                                        str2 = "";
                                        originalPath2 = title3;
                                        originalPath3 = "opus";
                                        name = name2;
                                        obj = "mp3";
                                        attributeAudio4 = attributeAudio3;
                                        isEncrypted = isEncrypted4;
                                        permormer2 = str;
                                        duration3 = -1;
                                        String str42 = path2;
                                        path4 = "m4a";
                                        long j32 = j;
                                        path3 = str42;
                                        myMime = myMime3;
                                        j2 = j32;
                                        parentObject = null;
                                        document = null;
                                        if (document == null) {
                                        }
                                        if (caption != null) {
                                        }
                                        isEncrypted3 = isEncrypted2;
                                        final TLRPC.TL_document documentFinal2 = document;
                                        final String pathFinal2 = path5;
                                        parentFinal = parentObject;
                                        params = new HashMap<>();
                                        if (originalPath2 != null) {
                                        }
                                        if (forceDocument) {
                                        }
                                        if (parentFinal != null) {
                                        }
                                        Integer prevType32 = Integer.valueOf(i);
                                        boolean isSticker2 = false;
                                        if (docType != null) {
                                        }
                                        if (!isEncrypted3) {
                                        }
                                        attributeAudio6 = attributeAudio5;
                                        prevType2 = prevType;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal2, pathFinal2, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                                            }
                                        });
                                        return 0;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        mediaMetadataRetriever = mediaMetadataRetriever;
                                        if (mediaMetadataRetriever != null) {
                                        }
                                        throw th;
                                    }
                                }
                                permormer5 = permormer6;
                                mediaMetadataRetriever.release();
                                str = "flac";
                                title = title4;
                                permormer = permormer5;
                                j = 0;
                                extL = extL3;
                                duration = duration4;
                            } catch (Exception e9) {
                                e3 = e9;
                                attributeAudio = null;
                                mediaMetadataRetriever = mediaMetadataRetriever;
                            } catch (Throwable th6) {
                                th = th6;
                                mediaMetadataRetriever = mediaMetadataRetriever;
                            }
                        } catch (Exception e10) {
                            e3 = e10;
                            attributeAudio = null;
                        } catch (Throwable th7) {
                            th = th7;
                        }
                    } catch (Exception e11) {
                        e3 = e11;
                        attributeAudio = null;
                    } catch (Throwable th8) {
                        th = th8;
                    }
                } else {
                    attributeAudio = null;
                    permormer = null;
                    extL = extL3;
                    str = "flac";
                    title = null;
                    duration = 0;
                    j = 0;
                }
                if (duration != 0) {
                    TLRPC.TL_documentAttributeAudio attributeAudio7 = new TLRPC.TL_documentAttributeAudio();
                    attributeAudio7.duration = duration;
                    attributeAudio7.title = title;
                    attributeAudio7.performer = permormer;
                    if (attributeAudio7.title == null) {
                        attributeAudio7.title = "";
                    }
                    attributeAudio7.flags |= 1;
                    if (attributeAudio7.performer == null) {
                        attributeAudio7.performer = "";
                    }
                    attributeAudio7.flags |= 2;
                    if (isVoice) {
                        duration2 = duration;
                        attributeAudio7.voice = true;
                    } else {
                        duration2 = duration;
                    }
                    attributeAudio2 = attributeAudio7;
                } else {
                    duration2 = duration;
                    attributeAudio2 = attributeAudio;
                }
                if (originalPath == null) {
                    sendNew = false;
                    attributeAudio3 = attributeAudio2;
                    title2 = title;
                    title3 = originalPath;
                } else if (originalPath.endsWith("attheme")) {
                    sendNew = true;
                    attributeAudio3 = attributeAudio2;
                    title2 = title;
                    title3 = originalPath;
                } else if (attributeAudio2 != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(originalPath);
                    sendNew = false;
                    sb.append("audio");
                    attributeAudio3 = attributeAudio2;
                    title2 = title;
                    sb.append(f.length());
                    title3 = sb.toString();
                } else {
                    sendNew = false;
                    attributeAudio3 = attributeAudio2;
                    title2 = title;
                    title3 = originalPath + "" + f.length();
                }
                String parentObject222 = null;
                if (!sendNew || isEncrypted4) {
                    str2 = "";
                    originalPath2 = title3;
                    originalPath3 = "opus";
                    name = name2;
                    obj = "mp3";
                    attributeAudio4 = attributeAudio3;
                    isEncrypted = isEncrypted4;
                    permormer2 = str;
                    duration3 = -1;
                    String str422 = path2;
                    path4 = "m4a";
                    long j322 = j;
                    path3 = str422;
                    myMime = myMime3;
                    j2 = j322;
                    parentObject = null;
                    document = null;
                } else {
                    Object[] sentData = accountInstance.getMessagesStorage().getSentFile(title3, !isEncrypted4 ? 1 : 4);
                    if (sentData != null) {
                        document3 = null;
                        if (sentData[0] instanceof TLRPC.TL_document) {
                            document2 = (TLRPC.TL_document) sentData[0];
                            parentObject222 = (String) sentData[1];
                            if (document2 == null || path2.equals(title3) || isEncrypted4) {
                                parentObject = parentObject222;
                                document = document2;
                            } else {
                                MessagesStorage messagesStorage = accountInstance.getMessagesStorage();
                                TLRPC.TL_document document4 = document2;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(path2);
                                parentObject = parentObject222;
                                sb2.append(f.length());
                                Object[] sentData2 = messagesStorage.getSentFile(sb2.toString(), !isEncrypted4 ? 1 : 4);
                                if (sentData2 != null && (sentData2[0] instanceof TLRPC.TL_document)) {
                                    document = (TLRPC.TL_document) sentData2[0];
                                    parentObject = (String) sentData2[1];
                                }
                                document = document4;
                            }
                            str2 = "";
                            attributeAudio4 = attributeAudio3;
                            duration3 = -1;
                            String str5 = path2;
                            name = name2;
                            obj = "mp3";
                            permormer2 = str;
                            isEncrypted = isEncrypted4;
                            originalPath2 = title3;
                            originalPath3 = "opus";
                            String str6 = path2;
                            path4 = "m4a";
                            long j4 = j;
                            path3 = str6;
                            myMime = myMime3;
                            j2 = j4;
                            ensureMediaThumbExists(accountInstance, isEncrypted4, document, str5, null, 0L);
                        }
                    } else {
                        document3 = null;
                    }
                    document2 = document3;
                    if (document2 == null) {
                    }
                    parentObject = parentObject222;
                    document = document2;
                    str2 = "";
                    attributeAudio4 = attributeAudio3;
                    duration3 = -1;
                    String str52 = path2;
                    name = name2;
                    obj = "mp3";
                    permormer2 = str;
                    isEncrypted = isEncrypted4;
                    originalPath2 = title3;
                    originalPath3 = "opus";
                    String str62 = path2;
                    path4 = "m4a";
                    long j42 = j;
                    path3 = str62;
                    myMime = myMime3;
                    j2 = j42;
                    ensureMediaThumbExists(accountInstance, isEncrypted4, document, str52, null, 0L);
                }
                if (document == null) {
                    TLRPC.TL_document document5 = new TLRPC.TL_document();
                    document5.id = j2;
                    document5.date = accountInstance.getConnectionsManager().getCurrentTime();
                    TLRPC.TL_documentAttributeFilename fileName = new TLRPC.TL_documentAttributeFilename();
                    fileName.file_name = name;
                    i = 0;
                    document5.file_reference = new byte[0];
                    document5.attributes.add(fileName);
                    document5.size = f.length();
                    document5.dc_id = 0;
                    attributeAudio5 = attributeAudio4;
                    if (attributeAudio5 != null) {
                        document5.attributes.add(attributeAudio5);
                    }
                    if (ext.length() != 0) {
                        switch (extL.hashCode()) {
                            case 106458:
                                extL2 = extL;
                                if (extL2.equals(path4)) {
                                    c = 3;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 108272:
                                extL2 = extL;
                                if (extL2.equals(obj)) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 109967:
                                extL2 = extL;
                                if (extL2.equals("ogg")) {
                                    c = 4;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3145576:
                                extL2 = extL;
                                if (extL2.equals(permormer2)) {
                                    c = 5;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3418175:
                                extL2 = extL;
                                if (extL2.equals(originalPath3)) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3645340:
                                extL2 = extL;
                                if (extL2.equals("webp")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                extL2 = extL;
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                document5.mime_type = "image/webp";
                                myMime2 = myMime;
                                break;
                            case 1:
                                document5.mime_type = MimeTypes.AUDIO_OPUS;
                                myMime2 = myMime;
                                break;
                            case 2:
                                document5.mime_type = MimeTypes.AUDIO_MPEG;
                                myMime2 = myMime;
                                break;
                            case 3:
                                document5.mime_type = "audio/m4a";
                                myMime2 = myMime;
                                break;
                            case 4:
                                document5.mime_type = "audio/ogg";
                                myMime2 = myMime;
                                break;
                            case 5:
                                document5.mime_type = MimeTypes.AUDIO_FLAC;
                                myMime2 = myMime;
                                break;
                            default:
                                myMime2 = myMime;
                                String mimeType = myMime2.getMimeTypeFromExtension(extL2);
                                if (mimeType == null) {
                                    document5.mime_type = "application/octet-stream";
                                    break;
                                } else {
                                    document5.mime_type = mimeType;
                                    break;
                                }
                        }
                    } else {
                        myMime2 = myMime;
                        extL2 = extL;
                        document5.mime_type = "application/octet-stream";
                    }
                    if (forceDocument || !document5.mime_type.equals("image/gif")) {
                        isEncrypted2 = isEncrypted;
                    } else if (editingMessageObject == null || editingMessageObject.getGroupIdForUse() == j2) {
                        try {
                            Bitmap bitmap = ImageLoader.loadBitmap(f.getAbsolutePath(), null, 90.0f, 90.0f, true);
                            if (bitmap != null) {
                                fileName.file_name = "animation.gif";
                                document5.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                isEncrypted2 = isEncrypted;
                                try {
                                    TLRPC.PhotoSize thumb = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, isEncrypted2);
                                    if (thumb != null) {
                                        document5.thumbs.add(thumb);
                                        document5.flags |= 1;
                                    }
                                    bitmap.recycle();
                                } catch (Exception e12) {
                                    e2 = e12;
                                    FileLog.e(e2);
                                    if (document5.mime_type.equals("image/webp")) {
                                    }
                                    extL = extL2;
                                    path5 = path3;
                                    str3 = str2;
                                    document = document5;
                                    if (caption != null) {
                                    }
                                    isEncrypted3 = isEncrypted2;
                                    final TLRPC.TL_document documentFinal22 = document;
                                    final String pathFinal22 = path5;
                                    parentFinal = parentObject;
                                    params = new HashMap<>();
                                    if (originalPath2 != null) {
                                    }
                                    if (forceDocument) {
                                    }
                                    if (parentFinal != null) {
                                    }
                                    Integer prevType322 = Integer.valueOf(i);
                                    boolean isSticker22 = false;
                                    if (docType != null) {
                                    }
                                    if (!isEncrypted3) {
                                    }
                                    attributeAudio6 = attributeAudio5;
                                    prevType2 = prevType;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal22, pathFinal22, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                                        }
                                    });
                                    return 0;
                                }
                            } else {
                                isEncrypted2 = isEncrypted;
                            }
                        } catch (Exception e13) {
                            e2 = e13;
                            isEncrypted2 = isEncrypted;
                        }
                    } else {
                        isEncrypted2 = isEncrypted;
                    }
                    if (document5.mime_type.equals("image/webp") || editingMessageObject != null) {
                        extL = extL2;
                        path5 = path3;
                        str3 = str2;
                    } else {
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        try {
                            bmOptions.inJustDecodeBounds = true;
                            path5 = path3;
                            try {
                                RandomAccessFile file = new RandomAccessFile(path5, "r");
                                extL = extL2;
                                try {
                                    ByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, path5.length());
                                    Utilities.loadWebpImage(null, buffer, buffer.limit(), bmOptions, true);
                                    file.close();
                                } catch (Exception e14) {
                                    e = e14;
                                    FileLog.e(e);
                                    if (bmOptions.outWidth != 0) {
                                    }
                                    str3 = str2;
                                    document = document5;
                                    if (caption != null) {
                                    }
                                    isEncrypted3 = isEncrypted2;
                                    final TLRPC.TL_document documentFinal222 = document;
                                    final String pathFinal222 = path5;
                                    parentFinal = parentObject;
                                    params = new HashMap<>();
                                    if (originalPath2 != null) {
                                    }
                                    if (forceDocument) {
                                    }
                                    if (parentFinal != null) {
                                    }
                                    Integer prevType3222 = Integer.valueOf(i);
                                    boolean isSticker222 = false;
                                    if (docType != null) {
                                    }
                                    if (!isEncrypted3) {
                                    }
                                    attributeAudio6 = attributeAudio5;
                                    prevType2 = prevType;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal222, pathFinal222, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                                        }
                                    });
                                    return 0;
                                }
                            } catch (Exception e15) {
                                e = e15;
                                extL = extL2;
                            }
                        } catch (Exception e16) {
                            e = e16;
                            extL = extL2;
                            path5 = path3;
                        }
                        if (bmOptions.outWidth != 0 || bmOptions.outHeight == 0 || bmOptions.outWidth > 800 || bmOptions.outHeight > 800) {
                            str3 = str2;
                        } else {
                            TLRPC.TL_documentAttributeSticker attributeSticker = new TLRPC.TL_documentAttributeSticker();
                            str3 = str2;
                            attributeSticker.alt = str3;
                            attributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                            document5.attributes.add(attributeSticker);
                            TLRPC.TL_documentAttributeImageSize attributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                            attributeImageSize.w = bmOptions.outWidth;
                            attributeImageSize.h = bmOptions.outHeight;
                            document5.attributes.add(attributeImageSize);
                        }
                    }
                    document = document5;
                } else {
                    path5 = path3;
                    myMime2 = myMime;
                    isEncrypted2 = isEncrypted;
                    str3 = str2;
                    attributeAudio5 = attributeAudio4;
                    i = 0;
                }
                final String captionFinal = caption != null ? caption.toString() : str3;
                isEncrypted3 = isEncrypted2;
                final TLRPC.TL_document documentFinal2222 = document;
                final String pathFinal2222 = path5;
                parentFinal = parentObject;
                params = new HashMap<>();
                if (originalPath2 != null) {
                    originalPath4 = originalPath2;
                    params.put("originalPath", originalPath4);
                } else {
                    originalPath4 = originalPath2;
                }
                if (forceDocument && attributeAudio5 == null) {
                    params.put("forceDocument", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                }
                if (parentFinal != null) {
                    params.put("parentObject", parentFinal);
                }
                Integer prevType32222 = Integer.valueOf(i);
                boolean isSticker2222 = false;
                if (docType != null) {
                    Integer prevType4 = docType[i];
                    if (document.mime_type == null || !document.mime_type.toLowerCase().startsWith("image/webp")) {
                        if ((document.mime_type != null && (document.mime_type.toLowerCase().startsWith("image/") || document.mime_type.toLowerCase().startsWith(MimeTypes.VIDEO_MP4))) || MessageObject.canPreviewDocument(document)) {
                            docType[0] = 1;
                        } else if (attributeAudio5 != null) {
                            docType[0] = 2;
                        } else {
                            docType[0] = 0;
                        }
                        prevType = prevType4;
                    } else {
                        docType[0] = Integer.valueOf(duration3);
                        isSticker2222 = true;
                        prevType = prevType4;
                    }
                } else {
                    prevType = prevType32222;
                }
                if (!isEncrypted3 || groupId == null) {
                    attributeAudio6 = attributeAudio5;
                    prevType2 = prevType;
                } else {
                    if (docType == null || prevType == null || prevType == docType[0]) {
                        attributeAudio6 = attributeAudio5;
                        prevType2 = prevType;
                    } else {
                        attributeAudio6 = attributeAudio5;
                        prevType2 = prevType;
                        finishGroup(accountInstance, groupId[0], scheduleDate);
                        groupId[0] = Utilities.random.nextLong();
                    }
                    if (!isSticker2222) {
                        params.put("groupId", str3 + groupId[0]);
                        if (isGroupFinal) {
                            params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                        }
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal2222, pathFinal2222, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                    }
                });
                return 0;
            } else {
                attributeAudio = null;
                permormer3 = null;
            }
            AudioInfo audioInfo = AudioInfo.getAudioInfo(f);
            if (audioInfo != null) {
                long d2 = audioInfo.getDuration();
                j = 0;
                if (d2 != 0) {
                    String permormer7 = audioInfo.getArtist();
                    title4 = audioInfo.getTitle();
                    extL = extL3;
                    str = "flac";
                    duration4 = (int) (d2 / 1000);
                    permormer4 = permormer7;
                    permormer = permormer4;
                    title = title4;
                    duration = duration4;
                    if (duration != 0) {
                    }
                    if (originalPath == null) {
                    }
                    String parentObject2222 = null;
                    if (!sendNew) {
                    }
                    str2 = "";
                    originalPath2 = title3;
                    originalPath3 = "opus";
                    name = name2;
                    obj = "mp3";
                    attributeAudio4 = attributeAudio3;
                    isEncrypted = isEncrypted4;
                    permormer2 = str;
                    duration3 = -1;
                    String str4222 = path2;
                    path4 = "m4a";
                    long j3222 = j;
                    path3 = str4222;
                    myMime = myMime3;
                    j2 = j3222;
                    parentObject = null;
                    document = null;
                    if (document == null) {
                    }
                    if (caption != null) {
                    }
                    isEncrypted3 = isEncrypted2;
                    final TLRPC.TL_document documentFinal22222 = document;
                    final String pathFinal22222 = path5;
                    parentFinal = parentObject;
                    params = new HashMap<>();
                    if (originalPath2 != null) {
                    }
                    if (forceDocument) {
                        params.put("forceDocument", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                    }
                    if (parentFinal != null) {
                    }
                    Integer prevType322222 = Integer.valueOf(i);
                    boolean isSticker22222 = false;
                    if (docType != null) {
                    }
                    if (!isEncrypted3) {
                    }
                    attributeAudio6 = attributeAudio5;
                    prevType2 = prevType;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal22222, pathFinal22222, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                        }
                    });
                    return 0;
                }
                extL = extL3;
                str = "flac";
            } else {
                extL = extL3;
                str = "flac";
                j = 0;
            }
            permormer4 = permormer3;
            permormer = permormer4;
            title = title4;
            duration = duration4;
            if (duration != 0) {
            }
            if (originalPath == null) {
            }
            String parentObject22222 = null;
            if (!sendNew) {
            }
            str2 = "";
            originalPath2 = title3;
            originalPath3 = "opus";
            name = name2;
            obj = "mp3";
            attributeAudio4 = attributeAudio3;
            isEncrypted = isEncrypted4;
            permormer2 = str;
            duration3 = -1;
            String str42222 = path2;
            path4 = "m4a";
            long j32222 = j;
            path3 = str42222;
            myMime = myMime3;
            j2 = j32222;
            parentObject = null;
            document = null;
            if (document == null) {
            }
            if (caption != null) {
            }
            isEncrypted3 = isEncrypted2;
            final TLRPC.TL_document documentFinal222222 = document;
            final String pathFinal222222 = path5;
            parentFinal = parentObject;
            params = new HashMap<>();
            if (originalPath2 != null) {
            }
            if (forceDocument) {
            }
            if (parentFinal != null) {
            }
            Integer prevType3222222 = Integer.valueOf(i);
            boolean isSticker222222 = false;
            if (docType != null) {
            }
            if (!isEncrypted3) {
            }
            attributeAudio6 = attributeAudio5;
            prevType2 = prevType;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingDocumentInternal$73(MessageObject.this, accountInstance, documentFinal222222, pathFinal222222, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate);
                }
            });
            return 0;
        }
        return 1;
    }

    public static /* synthetic */ void lambda$prepareSendingDocumentInternal$73(MessageObject editingMessageObject, AccountInstance accountInstance, TLRPC.TL_document documentFinal, String pathFinal, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, String captionFinal, ArrayList entities, boolean notify, int scheduleDate) {
        if (editingMessageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, null, null, documentFinal, pathFinal, params, false, parentFinal);
        } else {
            accountInstance.getSendMessagesHelper().sendMessage(documentFinal, null, pathFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, null, params, notify, scheduleDate, 0, parentFinal, null);
        }
    }

    private static boolean checkFileSize(AccountInstance accountInstance, Uri uri) {
        long len = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                AssetFileDescriptor assetFileDescriptor = ApplicationLoader.applicationContext.getContentResolver().openAssetFileDescriptor(uri, "r", null);
                if (assetFileDescriptor != null) {
                    len = assetFileDescriptor.getLength();
                }
                Cursor cursor = ApplicationLoader.applicationContext.getContentResolver().query(uri, new String[]{"_size"}, null, null, null);
                int sizeIndex = cursor.getColumnIndex("_size");
                cursor.moveToFirst();
                len = cursor.getLong(sizeIndex);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return !FileLoader.checkUploadFileSize(accountInstance.getCurrentAccount(), len);
    }

    public static void prepareSendingDocument(AccountInstance accountInstance, String path, String originalPath, Uri uri, String caption, String mine, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, InputContentInfoCompat inputContent, MessageObject editingMessageObject, boolean notify, int scheduleDate) {
        ArrayList<Uri> uris;
        if ((path == null || originalPath == null) && uri == null) {
            return;
        }
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<String> originalPaths = new ArrayList<>();
        if (uri == null) {
            uris = null;
        } else {
            ArrayList<Uri> uris2 = new ArrayList<>();
            uris2.add(uri);
            uris = uris2;
        }
        if (path != null) {
            paths.add(path);
            originalPaths.add(originalPath);
        }
        prepareSendingDocuments(accountInstance, paths, originalPaths, uris, caption, mine, dialogId, replyToMsg, replyToTopMsg, inputContent, editingMessageObject, notify, scheduleDate);
    }

    public static void prepareSendingAudioDocuments(final AccountInstance accountInstance, final ArrayList<MessageObject> messageObjects, final String caption, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final MessageObject editingMessageObject, final boolean notify, final int scheduleDate) {
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingAudioDocuments$75(messageObjects, dialogId, accountInstance, caption, editingMessageObject, replyToMsg, replyToTopMsg, notify, scheduleDate);
            }
        }).start();
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingAudioDocuments$75(ArrayList messageObjects, final long dialogId, final AccountInstance accountInstance, String caption, final MessageObject editingMessageObject, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final boolean notify, final int scheduleDate) {
        String originalPath;
        String parentObject;
        TLRPC.TL_document document;
        final String parentFinal;
        final HashMap<String, String> params;
        int count = messageObjects.size();
        long groupId = 0;
        int mediaCount = 0;
        int a = 0;
        while (a < count) {
            final MessageObject messageObject = (MessageObject) messageObjects.get(a);
            String originalPath2 = messageObject.messageOwner.attachPath;
            File f = new File(originalPath2);
            boolean isEncrypted = DialogObject.isEncryptedDialog(dialogId);
            if (!isEncrypted && count > 1 && mediaCount % 10 == 0) {
                groupId = Utilities.random.nextLong();
                mediaCount = 0;
            }
            if (originalPath2 == null) {
                originalPath = originalPath2;
            } else {
                originalPath = originalPath2 + "audio" + f.length();
            }
            TLRPC.TL_document document2 = null;
            if (!isEncrypted) {
                Object[] sentData = accountInstance.getMessagesStorage().getSentFile(originalPath, !isEncrypted ? 1 : 4);
                if (sentData != null && (sentData[0] instanceof TLRPC.TL_document)) {
                    document2 = (TLRPC.TL_document) sentData[0];
                    String parentObject2 = (String) sentData[1];
                    ensureMediaThumbExists(accountInstance, isEncrypted, document2, originalPath, null, 0L);
                    parentObject = parentObject2;
                    if (document2 == null) {
                        document = document2;
                    } else {
                        TLRPC.TL_document document3 = (TLRPC.TL_document) messageObject.messageOwner.media.document;
                        document = document3;
                    }
                    if (isEncrypted) {
                        int encryptedChatId = DialogObject.getEncryptedChatId(dialogId);
                        TLRPC.EncryptedChat encryptedChat = accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            return;
                        }
                    }
                    final TLRPC.TL_document documentFinal = document;
                    parentFinal = parentObject;
                    final String captionFinal = a != 0 ? caption : null;
                    params = new HashMap<>();
                    if (originalPath != null) {
                        params.put("originalPath", originalPath);
                    }
                    if (parentFinal != null) {
                        params.put("parentObject", parentFinal);
                    }
                    mediaCount++;
                    params.put("groupId", "" + groupId);
                    if (mediaCount != 10 || a == count - 1) {
                        params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.lambda$prepareSendingAudioDocuments$74(MessageObject.this, accountInstance, documentFinal, messageObject, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, notify, scheduleDate);
                        }
                    });
                    a++;
                }
            }
            parentObject = null;
            if (document2 == null) {
            }
            if (isEncrypted) {
            }
            final TLRPC.TL_document documentFinal2 = document;
            parentFinal = parentObject;
            if (a != 0) {
            }
            params = new HashMap<>();
            if (originalPath != null) {
            }
            if (parentFinal != null) {
            }
            mediaCount++;
            params.put("groupId", "" + groupId);
            if (mediaCount != 10) {
            }
            params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingAudioDocuments$74(MessageObject.this, accountInstance, documentFinal2, messageObject, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, notify, scheduleDate);
                }
            });
            a++;
        }
    }

    public static /* synthetic */ void lambda$prepareSendingAudioDocuments$74(MessageObject editingMessageObject, AccountInstance accountInstance, TLRPC.TL_document documentFinal, MessageObject messageObject, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, String captionFinal, boolean notify, int scheduleDate) {
        if (editingMessageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, null, null, documentFinal, messageObject.messageOwner.attachPath, params, false, parentFinal);
        } else {
            accountInstance.getSendMessagesHelper().sendMessage(documentFinal, null, messageObject.messageOwner.attachPath, dialogId, replyToMsg, replyToTopMsg, captionFinal, null, null, params, notify, scheduleDate, 0, parentFinal, null);
        }
    }

    private static void finishGroup(final AccountInstance accountInstance, final long groupId, final int scheduleDate) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$finishGroup$76(AccountInstance.this, groupId, scheduleDate);
            }
        });
    }

    public static /* synthetic */ void lambda$finishGroup$76(AccountInstance accountInstance, long groupId, int scheduleDate) {
        SendMessagesHelper instance = accountInstance.getSendMessagesHelper();
        HashMap<String, ArrayList<DelayedMessage>> hashMap = instance.delayedMessages;
        ArrayList<DelayedMessage> arrayList = hashMap.get("group_" + groupId);
        if (arrayList != null && !arrayList.isEmpty()) {
            DelayedMessage message = arrayList.get(0);
            MessageObject prevMessage = message.messageObjects.get(message.messageObjects.size() - 1);
            message.finalGroupMessage = prevMessage.getId();
            prevMessage.messageOwner.params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
            TLRPC.TL_messages_messages messagesRes = new TLRPC.TL_messages_messages();
            messagesRes.messages.add(prevMessage.messageOwner);
            accountInstance.getMessagesStorage().putMessages((TLRPC.messages_Messages) messagesRes, message.peer, -2, 0, false, scheduleDate != 0);
            instance.sendReadyToSendGroup(message, true, true);
        }
    }

    public static void prepareSendingDocuments(final AccountInstance accountInstance, final ArrayList<String> paths, final ArrayList<String> originalPaths, final ArrayList<Uri> uris, final String caption, final String mime, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final InputContentInfoCompat inputContent, final MessageObject editingMessageObject, final boolean notify, final int scheduleDate) {
        if (paths == null && originalPaths == null && uris == null) {
            return;
        }
        if (paths != null && originalPaths != null && paths.size() != originalPaths.size()) {
            return;
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingDocuments$77(dialogId, paths, caption, accountInstance, scheduleDate, originalPaths, mime, replyToMsg, replyToTopMsg, editingMessageObject, inputContent, notify, uris);
            }
        });
    }

    /* JADX WARN: Incorrect condition in loop: B:39:0x00e8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingDocuments$77(long dialogId, ArrayList paths, String caption, AccountInstance accountInstance, int scheduleDate, ArrayList originalPaths, String mime, MessageObject replyToMsg, MessageObject replyToTopMsg, MessageObject editingMessageObject, InputContentInfoCompat inputContent, boolean notify, ArrayList uris) {
        long[] groupId;
        Integer[] docType;
        AccountInstance accountInstance2 = accountInstance;
        int i = scheduleDate;
        int error = 0;
        int i2 = 1;
        long[] groupId2 = new long[1];
        int mediaCount = 0;
        Integer[] docType2 = new Integer[1];
        boolean isEncrypted = DialogObject.isEncryptedDialog(dialogId);
        int i3 = 10;
        if (paths == null) {
            docType = docType2;
            groupId = groupId2;
        } else {
            int count = paths.size();
            int error2 = 0;
            int a = 0;
            while (a < count) {
                String captionFinal = a == 0 ? caption : null;
                if (!isEncrypted && count > i2 && mediaCount % 10 == 0) {
                    if (groupId2[0] != 0) {
                        finishGroup(accountInstance2, groupId2[0], i);
                    }
                    groupId2[0] = Utilities.random.nextLong();
                    mediaCount = 0;
                }
                int mediaCount2 = mediaCount + 1;
                long prevGroupId = groupId2[0];
                int count2 = count;
                int a2 = a;
                Integer[] docType3 = docType2;
                long[] groupId3 = groupId2;
                error2 = prepareSendingDocumentInternal(accountInstance, (String) paths.get(a), (String) originalPaths.get(a), null, mime, dialogId, replyToMsg, replyToTopMsg, captionFinal, null, editingMessageObject, groupId3, mediaCount2 == i3 || a == count + (-1), inputContent == null, notify, scheduleDate, docType3);
                mediaCount = (prevGroupId != groupId3[0] || groupId3[0] == -1) ? 1 : mediaCount2;
                a = a2 + 1;
                accountInstance2 = accountInstance;
                i = scheduleDate;
                count = count2;
                docType2 = docType3;
                groupId2 = groupId3;
                i3 = 10;
                i2 = 1;
            }
            docType = docType2;
            groupId = groupId2;
            error = error2;
        }
        ArrayList arrayList = uris;
        if (arrayList != null) {
            groupId[0] = 0;
            int mediaCount3 = 0;
            int count3 = uris.size();
            int error3 = error;
            int a3 = 0;
            while (a3 < error) {
                String captionFinal2 = (a3 == 0 && (paths == null || paths.size() == 0)) ? caption : null;
                if (!isEncrypted) {
                    if (count3 > 1 && mediaCount3 % 10 == 0) {
                        if (groupId[0] != 0) {
                            finishGroup(accountInstance, groupId[0], scheduleDate);
                        }
                        groupId[0] = Utilities.random.nextLong();
                        mediaCount3 = 0;
                    }
                }
                int mediaCount4 = mediaCount3 + 1;
                long prevGroupId2 = groupId[0];
                int a4 = a3;
                int count4 = count3;
                error3 = prepareSendingDocumentInternal(accountInstance, null, null, (Uri) arrayList.get(a3), mime, dialogId, replyToMsg, replyToTopMsg, captionFinal2, null, editingMessageObject, groupId, mediaCount4 == 10 || a3 == count3 + (-1), inputContent == null, notify, scheduleDate, docType);
                mediaCount3 = (prevGroupId2 != groupId[0] || groupId[0] == -1) ? 1 : mediaCount4;
                a3 = a4 + 1;
                arrayList = uris;
                count3 = count4;
            }
            error = error3;
        }
        if (inputContent != null) {
            inputContent.releasePermission();
        }
        handleError(error, accountInstance);
    }

    private static void handleError(final int error, final AccountInstance accountInstance) {
        if (error != 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$handleError$78(error, accountInstance);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$handleError$78(int finalError, AccountInstance accountInstance) {
        try {
            if (finalError == 1) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 1, LocaleController.getString("UnsupportedAttachment", org.telegram.messenger.beta.R.string.UnsupportedAttachment));
            } else if (finalError == 2) {
                NotificationCenter.getInstance(accountInstance.getCurrentAccount()).postNotificationName(NotificationCenter.currentUserShowLimitReachedDialog, 6);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String imageFilePath, Uri imageUri, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, CharSequence caption, ArrayList<TLRPC.MessageEntity> entities, ArrayList<TLRPC.InputDocument> stickers, InputContentInfoCompat inputContent, int ttl, MessageObject editingMessageObject, boolean notify, int scheduleDate) {
        prepareSendingPhoto(accountInstance, imageFilePath, null, imageUri, dialogId, replyToMsg, replyToTopMsg, caption, entities, stickers, inputContent, ttl, editingMessageObject, null, notify, scheduleDate, false);
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String imageFilePath, String thumbFilePath, Uri imageUri, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, CharSequence caption, ArrayList<TLRPC.MessageEntity> entities, ArrayList<TLRPC.InputDocument> stickers, InputContentInfoCompat inputContent, int ttl, MessageObject editingMessageObject, VideoEditedInfo videoEditedInfo, boolean notify, int scheduleDate, boolean forceDocument) {
        SendingMediaInfo info = new SendingMediaInfo();
        info.path = imageFilePath;
        info.thumbPath = thumbFilePath;
        info.uri = imageUri;
        if (caption != null) {
            info.caption = caption.toString();
        }
        info.entities = entities;
        info.ttl = ttl;
        if (stickers != null) {
            info.masks = new ArrayList<>(stickers);
        }
        info.videoEditedInfo = videoEditedInfo;
        ArrayList<SendingMediaInfo> infos = new ArrayList<>();
        infos.add(info);
        prepareSendingMedia(accountInstance, infos, dialogId, replyToMsg, replyToTopMsg, inputContent, forceDocument, false, editingMessageObject, notify, scheduleDate);
    }

    public static void prepareSendingBotContextResult(final AccountInstance accountInstance, final TLRPC.BotInlineResult result, final HashMap<String, String> params, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final boolean notify, final int scheduleDate) {
        if (result == null) {
            return;
        }
        if (result.send_message instanceof TLRPC.TL_botInlineMessageMediaAuto) {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingBotContextResult$80(dialogId, result, accountInstance, params, replyToMsg, replyToTopMsg, notify, scheduleDate);
                }
            }).run();
        } else if (!(result.send_message instanceof TLRPC.TL_botInlineMessageText)) {
            if (!(result.send_message instanceof TLRPC.TL_botInlineMessageMediaVenue)) {
                if (result.send_message instanceof TLRPC.TL_botInlineMessageMediaGeo) {
                    if (result.send_message.period != 0 || result.send_message.proximity_notification_radius != 0) {
                        TLRPC.TL_messageMediaGeoLive location = new TLRPC.TL_messageMediaGeoLive();
                        location.period = result.send_message.period != 0 ? result.send_message.period : 900;
                        location.geo = result.send_message.geo;
                        location.heading = result.send_message.heading;
                        location.proximity_notification_radius = result.send_message.proximity_notification_radius;
                        accountInstance.getSendMessagesHelper().sendMessage(location, dialogId, replyToMsg, replyToTopMsg, result.send_message.reply_markup, params, notify, scheduleDate);
                        return;
                    }
                    TLRPC.TL_messageMediaGeo location2 = new TLRPC.TL_messageMediaGeo();
                    location2.geo = result.send_message.geo;
                    location2.heading = result.send_message.heading;
                    accountInstance.getSendMessagesHelper().sendMessage(location2, dialogId, replyToMsg, replyToTopMsg, result.send_message.reply_markup, params, notify, scheduleDate);
                    return;
                } else if (result.send_message instanceof TLRPC.TL_botInlineMessageMediaContact) {
                    TLRPC.User user = new TLRPC.TL_user();
                    user.phone = result.send_message.phone_number;
                    user.first_name = result.send_message.first_name;
                    user.last_name = result.send_message.last_name;
                    TLRPC.TL_restrictionReason reason = new TLRPC.TL_restrictionReason();
                    reason.text = result.send_message.vcard;
                    reason.platform = "";
                    reason.reason = "";
                    user.restriction_reason.add(reason);
                    accountInstance.getSendMessagesHelper().sendMessage(user, dialogId, replyToMsg, replyToTopMsg, result.send_message.reply_markup, params, notify, scheduleDate);
                    return;
                } else if (!(result.send_message instanceof TLRPC.TL_botInlineMessageMediaInvoice) || DialogObject.isEncryptedDialog(dialogId)) {
                    return;
                } else {
                    TLRPC.TL_botInlineMessageMediaInvoice invoice = (TLRPC.TL_botInlineMessageMediaInvoice) result.send_message;
                    TLRPC.TL_messageMediaInvoice messageMediaInvoice = new TLRPC.TL_messageMediaInvoice();
                    messageMediaInvoice.shipping_address_requested = invoice.shipping_address_requested;
                    messageMediaInvoice.test = invoice.test;
                    messageMediaInvoice.title = invoice.title;
                    messageMediaInvoice.description = invoice.description;
                    if (invoice.photo != null) {
                        messageMediaInvoice.photo = invoice.photo;
                        messageMediaInvoice.flags |= 1;
                    }
                    messageMediaInvoice.currency = invoice.currency;
                    messageMediaInvoice.total_amount = invoice.total_amount;
                    messageMediaInvoice.start_param = "";
                    accountInstance.getSendMessagesHelper().sendMessage(messageMediaInvoice, dialogId, replyToMsg, replyToTopMsg, result.send_message.reply_markup, params, notify, scheduleDate);
                    return;
                }
            }
            TLRPC.TL_messageMediaVenue venue = new TLRPC.TL_messageMediaVenue();
            venue.geo = result.send_message.geo;
            venue.address = result.send_message.address;
            venue.title = result.send_message.title;
            venue.provider = result.send_message.provider;
            venue.venue_id = result.send_message.venue_id;
            String str = result.send_message.venue_type;
            venue.venue_id = str;
            venue.venue_type = str;
            if (venue.venue_type == null) {
                venue.venue_type = "";
            }
            accountInstance.getSendMessagesHelper().sendMessage(venue, dialogId, replyToMsg, replyToTopMsg, result.send_message.reply_markup, params, notify, scheduleDate);
        } else {
            TLRPC.WebPage webPage = null;
            if (DialogObject.isEncryptedDialog(dialogId)) {
                int a = 0;
                while (true) {
                    if (a >= result.send_message.entities.size()) {
                        break;
                    }
                    TLRPC.MessageEntity entity = result.send_message.entities.get(a);
                    if (!(entity instanceof TLRPC.TL_messageEntityUrl)) {
                        a++;
                    } else {
                        webPage = new TLRPC.TL_webPagePending();
                        webPage.url = result.send_message.message.substring(entity.offset, entity.offset + entity.length);
                        break;
                    }
                }
            }
            accountInstance.getSendMessagesHelper().sendMessage(result.send_message.message, dialogId, replyToMsg, replyToTopMsg, webPage, !result.send_message.no_webpage, result.send_message.entities, result.send_message.reply_markup, params, notify, scheduleDate, null);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0492  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x04a4  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04f8  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x053b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingBotContextResult$80(final long dialogId, final TLRPC.BotInlineResult result, final AccountInstance accountInstance, final HashMap params, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final boolean notify, final int scheduleDate) {
        TLRPC.TL_photo photo;
        TLRPC.TL_game game;
        TLRPC.TL_document document;
        TLRPC.TL_document document2;
        String ext;
        String finalPath;
        char c;
        char c2;
        String ext2;
        Throwable e;
        Bitmap bitmap;
        boolean isEncrypted = DialogObject.isEncryptedDialog(dialogId);
        String finalPath2 = null;
        TLRPC.TL_photo photo2 = null;
        if ("game".equals(result.type)) {
            if (isEncrypted) {
                return;
            }
            TLRPC.TL_game game2 = new TLRPC.TL_game();
            game2.title = result.title;
            game2.description = result.description;
            game2.short_name = result.id;
            game2.photo = result.photo;
            if (game2.photo == null) {
                game2.photo = new TLRPC.TL_photoEmpty();
            }
            if (result.document instanceof TLRPC.TL_document) {
                game2.document = result.document;
                game2.flags |= 1;
            }
            document = null;
            photo = null;
            game = game2;
        } else if (result instanceof TLRPC.TL_botInlineMediaResult) {
            if (result.document != null) {
                if (!(result.document instanceof TLRPC.TL_document)) {
                    document2 = null;
                    photo = null;
                    game = null;
                    document = document2;
                } else {
                    document = (TLRPC.TL_document) result.document;
                    photo = null;
                    game = null;
                }
            } else {
                if (result.photo == null) {
                    document2 = null;
                    photo = null;
                    game = null;
                } else if (!(result.photo instanceof TLRPC.TL_photo)) {
                    document2 = null;
                    photo = null;
                    game = null;
                } else {
                    TLRPC.TL_photo photo3 = (TLRPC.TL_photo) result.photo;
                    document = null;
                    photo = photo3;
                    game = null;
                }
                document = document2;
            }
        } else if (result.content == null) {
            document2 = null;
            photo = null;
            game = null;
            document = document2;
        } else {
            String ext3 = ImageLoader.getHttpUrlExtension(result.content.url, null);
            if (TextUtils.isEmpty(ext3)) {
                ext = FileLoader.getExtensionByMimeType(result.content.mime_type);
            } else {
                ext = "." + ext3;
            }
            File f = new File(FileLoader.getDirectory(4), Utilities.MD5(result.content.url) + ext);
            if (f.exists()) {
                finalPath = f.getAbsolutePath();
            } else {
                finalPath = result.content.url;
            }
            String finalPath3 = result.type;
            switch (finalPath3.hashCode()) {
                case -1890252483:
                    if (finalPath3.equals("sticker")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 102340:
                    if (finalPath3.equals("gif")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 3143036:
                    if (finalPath3.equals("file")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 93166550:
                    if (finalPath3.equals("audio")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 106642994:
                    if (finalPath3.equals("photo")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 112202875:
                    if (finalPath3.equals("video")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 112386354:
                    if (finalPath3.equals("voice")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    TLRPC.TL_document document3 = new TLRPC.TL_document();
                    photo = null;
                    game = null;
                    String ext4 = ext;
                    document3.id = 0L;
                    document3.size = 0L;
                    document3.dc_id = 0;
                    document3.mime_type = result.content.mime_type;
                    document3.file_reference = new byte[0];
                    document3.date = accountInstance.getConnectionsManager().getCurrentTime();
                    TLRPC.TL_documentAttributeFilename fileName = new TLRPC.TL_documentAttributeFilename();
                    document3.attributes.add(fileName);
                    String str = result.type;
                    switch (str.hashCode()) {
                        case -1890252483:
                            if (str.equals("sticker")) {
                                c2 = 5;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 102340:
                            if (str.equals("gif")) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 3143036:
                            if (str.equals("file")) {
                                c2 = 3;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 93166550:
                            if (str.equals("audio")) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 112202875:
                            if (str.equals("video")) {
                                c2 = 4;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 112386354:
                            if (str.equals("voice")) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            fileName.file_name = "animation.gif";
                            if (finalPath.endsWith("mp4")) {
                                document3.mime_type = MimeTypes.VIDEO_MP4;
                                document3.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                            } else {
                                document3.mime_type = "image/gif";
                            }
                            int side = isEncrypted ? 90 : GroupCallActivity.TABLET_LIST_SIZE;
                            try {
                                if (finalPath.endsWith("mp4")) {
                                    bitmap = createVideoThumbnail(finalPath, 1);
                                    if (bitmap == null && (result.thumb instanceof TLRPC.TL_webDocument) && MimeTypes.VIDEO_MP4.equals(result.thumb.mime_type)) {
                                        String ext5 = ImageLoader.getHttpUrlExtension(result.thumb.url, null);
                                        try {
                                            if (TextUtils.isEmpty(ext5)) {
                                                ext2 = FileLoader.getExtensionByMimeType(result.thumb.mime_type);
                                            } else {
                                                ext2 = "." + ext5;
                                            }
                                            bitmap = createVideoThumbnail(new File(FileLoader.getDirectory(4), Utilities.MD5(result.thumb.url) + ext2).getAbsolutePath(), 1);
                                            ext4 = ext2;
                                        } catch (Throwable th) {
                                            e = th;
                                            FileLog.e(e);
                                            if (fileName.file_name == null) {
                                            }
                                            if (document3.mime_type == null) {
                                            }
                                            if (document3.thumbs.isEmpty()) {
                                            }
                                            document = document3;
                                            finalPath2 = finalPath;
                                            final String finalPathFinal = finalPath2;
                                            final TLRPC.TL_document finalDocument = document;
                                            final TLRPC.TL_photo finalPhoto = photo;
                                            final TLRPC.TL_game finalGame = game;
                                            if (params != null) {
                                                params.put("originalPath", result.content.url);
                                                break;
                                            }
                                            final Bitmap[] precahcedThumb = new Bitmap[1];
                                            final String[] precachedKey = new String[1];
                                            if (!MessageObject.isGifDocument(document)) {
                                            }
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda71
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingBotContextResult$79(TLRPC.TL_document.this, precahcedThumb, precachedKey, accountInstance, finalPathFinal, dialogId, replyToMsg, replyToTopMsg, result, params, notify, scheduleDate, finalPhoto, finalGame);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    bitmap = ImageLoader.loadBitmap(finalPath, null, side, side, true);
                                }
                                if (bitmap != null) {
                                    TLRPC.PhotoSize thumb = ImageLoader.scaleAndSaveImage(bitmap, side, side, side > 90 ? 80 : 55, false);
                                    if (thumb != null) {
                                        document3.thumbs.add(thumb);
                                        document3.flags |= 1;
                                    }
                                    bitmap.recycle();
                                }
                            } catch (Throwable th2) {
                                e = th2;
                                ext2 = ext4;
                            }
                            break;
                        case 1:
                            TLRPC.TL_documentAttributeAudio audio = new TLRPC.TL_documentAttributeAudio();
                            audio.duration = MessageObject.getInlineResultDuration(result);
                            audio.voice = true;
                            fileName.file_name = "audio.ogg";
                            document3.attributes.add(audio);
                            break;
                        case 2:
                            TLRPC.TL_documentAttributeAudio audio2 = new TLRPC.TL_documentAttributeAudio();
                            audio2.duration = MessageObject.getInlineResultDuration(result);
                            audio2.title = result.title;
                            audio2.flags |= 1;
                            if (result.description != null) {
                                audio2.performer = result.description;
                                audio2.flags |= 2;
                            }
                            fileName.file_name = "audio.mp3";
                            document3.attributes.add(audio2);
                            break;
                        case 3:
                            int idx = result.content.mime_type.lastIndexOf(47);
                            if (idx != -1) {
                                fileName.file_name = "file." + result.content.mime_type.substring(idx + 1);
                                break;
                            } else {
                                fileName.file_name = "file";
                                break;
                            }
                        case 4:
                            fileName.file_name = "video.mp4";
                            TLRPC.TL_documentAttributeVideo attributeVideo = new TLRPC.TL_documentAttributeVideo();
                            int[] wh = MessageObject.getInlineResultWidthAndHeight(result);
                            attributeVideo.w = wh[0];
                            attributeVideo.h = wh[1];
                            attributeVideo.duration = MessageObject.getInlineResultDuration(result);
                            attributeVideo.supports_streaming = true;
                            document3.attributes.add(attributeVideo);
                            try {
                                if (result.thumb != null) {
                                    String thumbPath = new File(FileLoader.getDirectory(4), Utilities.MD5(result.thumb.url) + "." + ImageLoader.getHttpUrlExtension(result.thumb.url, "jpg")).getAbsolutePath();
                                    Bitmap bitmap2 = ImageLoader.loadBitmap(thumbPath, null, 90.0f, 90.0f, true);
                                    if (bitmap2 != null) {
                                        TLRPC.PhotoSize thumb2 = ImageLoader.scaleAndSaveImage(bitmap2, 90.0f, 90.0f, 55, false);
                                        if (thumb2 != null) {
                                            document3.thumbs.add(thumb2);
                                            document3.flags |= 1;
                                        }
                                        bitmap2.recycle();
                                        break;
                                    }
                                }
                            } catch (Throwable e2) {
                                FileLog.e(e2);
                                break;
                            }
                            break;
                        case 5:
                            TLRPC.TL_documentAttributeSticker attributeSticker = new TLRPC.TL_documentAttributeSticker();
                            attributeSticker.alt = "";
                            attributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                            document3.attributes.add(attributeSticker);
                            TLRPC.TL_documentAttributeImageSize attributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                            int[] wh2 = MessageObject.getInlineResultWidthAndHeight(result);
                            attributeImageSize.w = wh2[0];
                            attributeImageSize.h = wh2[1];
                            document3.attributes.add(attributeImageSize);
                            fileName.file_name = "sticker.webp";
                            try {
                                if (result.thumb != null) {
                                    String thumbPath2 = new File(FileLoader.getDirectory(4), Utilities.MD5(result.thumb.url) + "." + ImageLoader.getHttpUrlExtension(result.thumb.url, "webp")).getAbsolutePath();
                                    Bitmap bitmap3 = ImageLoader.loadBitmap(thumbPath2, null, 90.0f, 90.0f, true);
                                    if (bitmap3 != null) {
                                        TLRPC.PhotoSize thumb3 = ImageLoader.scaleAndSaveImage(bitmap3, 90.0f, 90.0f, 55, false);
                                        if (thumb3 != null) {
                                            document3.thumbs.add(thumb3);
                                            document3.flags |= 1;
                                        }
                                        bitmap3.recycle();
                                        break;
                                    }
                                }
                            } catch (Throwable e3) {
                                FileLog.e(e3);
                                break;
                            }
                            break;
                    }
                    if (fileName.file_name == null) {
                        fileName.file_name = "file";
                    }
                    if (document3.mime_type == null) {
                        document3.mime_type = "application/octet-stream";
                    }
                    if (document3.thumbs.isEmpty()) {
                        TLRPC.PhotoSize thumb4 = new TLRPC.TL_photoSize();
                        int[] wh3 = MessageObject.getInlineResultWidthAndHeight(result);
                        thumb4.w = wh3[0];
                        thumb4.h = wh3[1];
                        thumb4.size = 0;
                        thumb4.location = new TLRPC.TL_fileLocationUnavailable();
                        thumb4.type = "x";
                        document3.thumbs.add(thumb4);
                        document3.flags |= 1;
                    }
                    document = document3;
                    finalPath2 = finalPath;
                    break;
                case 6:
                    if (f.exists()) {
                        photo2 = accountInstance.getSendMessagesHelper().generatePhotoSizes(finalPath, null);
                    }
                    if (photo2 != null) {
                        photo = photo2;
                        game = null;
                        finalPath2 = finalPath;
                        document = null;
                        break;
                    } else {
                        TLRPC.TL_photo photo4 = new TLRPC.TL_photo();
                        photo4.date = accountInstance.getConnectionsManager().getCurrentTime();
                        photo4.file_reference = new byte[0];
                        TLRPC.TL_photoSize photoSize = new TLRPC.TL_photoSize();
                        int[] wh4 = MessageObject.getInlineResultWidthAndHeight(result);
                        photoSize.w = wh4[0];
                        photoSize.h = wh4[1];
                        photoSize.size = 1;
                        photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                        photoSize.type = "x";
                        photo4.sizes.add(photoSize);
                        photo = photo4;
                        game = null;
                        finalPath2 = finalPath;
                        document = null;
                        break;
                    }
                default:
                    photo = null;
                    game = null;
                    finalPath2 = finalPath;
                    document = null;
                    break;
            }
        }
        final String finalPathFinal2 = finalPath2;
        final TLRPC.TL_document finalDocument2 = document;
        final TLRPC.TL_photo finalPhoto2 = photo;
        final TLRPC.TL_game finalGame2 = game;
        if (params != null && result.content != null) {
            params.put("originalPath", result.content.url);
        }
        final Bitmap[] precahcedThumb2 = new Bitmap[1];
        final String[] precachedKey2 = new String[1];
        if (!MessageObject.isGifDocument(document)) {
            TLRPC.PhotoSize photoSizeThumb = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, GroupCallActivity.TABLET_LIST_SIZE);
            File gifFile = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(document);
            if (!gifFile.exists()) {
                gifFile = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(document, true);
            }
            ensureMediaThumbExists(accountInstance, isEncrypted, document, gifFile.getAbsolutePath(), null, 0L);
            precachedKey2[0] = getKeyForPhotoSize(accountInstance, photoSizeThumb, precahcedThumb2, true, true);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingBotContextResult$79(TLRPC.TL_document.this, precahcedThumb2, precachedKey2, accountInstance, finalPathFinal2, dialogId, replyToMsg, replyToTopMsg, result, params, notify, scheduleDate, finalPhoto2, finalGame2);
            }
        });
    }

    public static /* synthetic */ void lambda$prepareSendingBotContextResult$79(TLRPC.TL_document finalDocument, Bitmap[] precahcedThumb, String[] precachedKey, AccountInstance accountInstance, String finalPathFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, TLRPC.BotInlineResult result, HashMap params, boolean notify, int scheduleDate, TLRPC.TL_photo finalPhoto, TLRPC.TL_game finalGame) {
        if (finalDocument != null) {
            if (precahcedThumb[0] != null && precachedKey[0] != null) {
                ImageLoader.getInstance().putImageToCache(new BitmapDrawable(precahcedThumb[0]), precachedKey[0], false);
            }
            accountInstance.getSendMessagesHelper().sendMessage(finalDocument, null, finalPathFinal, dialogId, replyToMsg, replyToTopMsg, result.send_message.message, result.send_message.entities, result.send_message.reply_markup, params, notify, scheduleDate, 0, result, null);
        } else if (finalPhoto != null) {
            accountInstance.getSendMessagesHelper().sendMessage(finalPhoto, result.content != null ? result.content.url : null, dialogId, replyToMsg, replyToTopMsg, result.send_message.message, result.send_message.entities, result.send_message.reply_markup, params, notify, scheduleDate, 0, result);
        } else if (finalGame != null) {
            accountInstance.getSendMessagesHelper().sendMessage(finalGame, dialogId, result.send_message.reply_markup, params, notify, scheduleDate);
        }
    }

    private static String getTrimmedString(String src) {
        String result = src.trim();
        if (result.length() == 0) {
            return result;
        }
        while (src.startsWith("\n")) {
            src = src.substring(1);
        }
        while (src.endsWith("\n")) {
            src = src.substring(0, src.length() - 1);
        }
        return src;
    }

    public static void prepareSendingText(final AccountInstance accountInstance, final String text, final long dialogId, final boolean notify, final int scheduleDate) {
        accountInstance.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda91
            @Override // java.lang.Runnable
            public final void run() {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda88
                    @Override // java.lang.Runnable
                    public final void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda77
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.lambda$prepareSendingText$81(r1, r2, r3, r5, r6);
                            }
                        });
                    }
                });
            }
        });
    }

    public static /* synthetic */ void lambda$prepareSendingText$81(String text, AccountInstance accountInstance, long dialogId, boolean notify, int scheduleDate) {
        String textFinal = getTrimmedString(text);
        if (textFinal.length() != 0) {
            int count = (int) Math.ceil(textFinal.length() / 4096.0f);
            for (int a = 0; a < count; a++) {
                String mess = textFinal.substring(a * 4096, Math.min((a + 1) * 4096, textFinal.length()));
                accountInstance.getSendMessagesHelper().sendMessage(mess, dialogId, null, null, null, true, null, null, null, notify, scheduleDate, null);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ad, code lost:
        r6.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void ensureMediaThumbExists(AccountInstance accountInstance, boolean isEncrypted, TLObject object, String path, Uri uri, long startTime) {
        Bitmap thumb;
        boolean smallExists;
        Bitmap bitmap;
        TLRPC.PhotoSize size;
        if (object instanceof TLRPC.TL_photo) {
            TLRPC.TL_photo photo = (TLRPC.TL_photo) object;
            TLRPC.PhotoSize smallSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 90);
            if ((smallSize instanceof TLRPC.TL_photoStrippedSize) || (smallSize instanceof TLRPC.TL_photoPathSize)) {
                smallExists = true;
            } else {
                File smallFile = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(smallSize, true);
                smallExists = smallFile.exists();
            }
            TLRPC.PhotoSize bigSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
            File bigFile = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(bigSize, false);
            boolean bigExists = bigFile.exists();
            if (!smallExists || !bigExists) {
                Bitmap bitmap2 = ImageLoader.loadBitmap(path, uri, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true);
                if (bitmap2 != null) {
                    bitmap = bitmap2;
                } else {
                    bitmap = ImageLoader.loadBitmap(path, uri, 800.0f, 800.0f, true);
                }
                if (!bigExists) {
                    TLRPC.PhotoSize size2 = ImageLoader.scaleAndSaveImage(bigSize, bitmap, Bitmap.CompressFormat.JPEG, true, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), 80, false, 101, 101, false);
                    if (size2 != bigSize) {
                        photo.sizes.add(0, size2);
                    }
                }
                if (!smallExists && (size = ImageLoader.scaleAndSaveImage(smallSize, bitmap, 90.0f, 90.0f, 55, true, false)) != smallSize) {
                    photo.sizes.add(0, size);
                }
            }
        } else if (object instanceof TLRPC.TL_document) {
            TLRPC.TL_document document = (TLRPC.TL_document) object;
            if ((MessageObject.isVideoDocument(document) || MessageObject.isNewGifDocument(document)) && MessageObject.isDocumentHasThumb(document)) {
                ArrayList<TLRPC.PhotoSize> arrayList = document.thumbs;
                int side = GroupCallActivity.TABLET_LIST_SIZE;
                TLRPC.PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, GroupCallActivity.TABLET_LIST_SIZE);
                if ((photoSize instanceof TLRPC.TL_photoStrippedSize) || (photoSize instanceof TLRPC.TL_photoPathSize)) {
                    return;
                }
                File smallFile2 = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(photoSize, true);
                if (!smallFile2.exists()) {
                    Bitmap thumb2 = createVideoThumbnailAtTime(path, startTime);
                    if (thumb2 != null) {
                        thumb = thumb2;
                    } else {
                        thumb = createVideoThumbnail(path, 1);
                    }
                    if (isEncrypted) {
                        side = 90;
                    }
                    document.thumbs.set(0, ImageLoader.scaleAndSaveImage(photoSize, thumb, side, side, side > 90 ? 80 : 55, false, true));
                }
            }
        }
    }

    public static String getKeyForPhotoSize(AccountInstance accountInstance, TLRPC.PhotoSize photoSize, Bitmap[] bitmap, boolean blur, boolean forceCache) {
        if (photoSize != null && photoSize.location != null) {
            Point point = ChatMessageCell.getMessageSize(photoSize.w, photoSize.h);
            if (bitmap != null) {
                try {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    try {
                        File file = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(photoSize, forceCache);
                        FileInputStream is = new FileInputStream(file);
                        BitmapFactory.decodeStream(is, null, opts);
                        is.close();
                        float photoW = opts.outWidth;
                        float photoH = opts.outHeight;
                        float scaleFactor = Math.max(photoW / point.x, photoH / point.y);
                        if (scaleFactor < 1.0f) {
                            scaleFactor = 1.0f;
                        }
                        opts.inJustDecodeBounds = false;
                        opts.inSampleSize = (int) scaleFactor;
                        opts.inPreferredConfig = Bitmap.Config.RGB_565;
                        if (Build.VERSION.SDK_INT >= 21) {
                            FileInputStream is2 = new FileInputStream(file);
                            bitmap[0] = BitmapFactory.decodeStream(is2, null, opts);
                            is2.close();
                        }
                    } catch (Throwable th) {
                    }
                } catch (Throwable th2) {
                }
            }
            return String.format(Locale.US, blur ? "%d_%d@%d_%d_b" : "%d_%d@%d_%d", Long.valueOf(photoSize.location.volume_id), Integer.valueOf(photoSize.location.local_id), Integer.valueOf((int) (point.x / AndroidUtilities.density)), Integer.valueOf((int) (point.y / AndroidUtilities.density)));
        }
        return null;
    }

    public static boolean shouldSendWebPAsSticker(String path, Uri uri) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        if (path != null) {
            try {
                RandomAccessFile file = new RandomAccessFile(path, "r");
                ByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, path.length());
                Utilities.loadWebpImage(null, buffer, buffer.limit(), bmOptions, true);
                file.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                InputStream inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream, null, bmOptions);
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
            }
        }
        return bmOptions.outWidth < 800 && bmOptions.outHeight < 800;
    }

    public static void prepareSendingMedia(final AccountInstance accountInstance, final ArrayList<SendingMediaInfo> media, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final InputContentInfoCompat inputContent, final boolean forceDocument, boolean groupMedia, final MessageObject editingMessageObject, final boolean notify, final int scheduleDate) {
        boolean groupMedia2;
        if (media.isEmpty()) {
            return;
        }
        int a = 0;
        int N = media.size();
        while (true) {
            if (a >= N) {
                groupMedia2 = groupMedia;
                break;
            } else if (media.get(a).ttl <= 0) {
                a++;
            } else {
                groupMedia2 = false;
                break;
            }
        }
        final boolean groupMediaFinal = groupMedia2;
        mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingMedia$89(media, dialogId, forceDocument, groupMediaFinal, accountInstance, editingMessageObject, replyToMsg, replyToTopMsg, notify, scheduleDate, inputContent);
            }
        });
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:389|(1:394)(1:393)|395|(4:397|(2:400|398)|704|401)|402|(1:404)|(1:406)|(4:676|408|409|(4:411|(3:425|(2:427|(1:429))(1:430)|431)(1:432)|433|702))(1:414)|674|415|(3:417|662|418)(1:420)|(0)(0)|433|702) */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0065, code lost:
        if (r7 != false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a7b, code lost:
        r0 = e;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:143:0x036e A[Catch: Exception -> 0x035d, TRY_ENTER, TRY_LEAVE, TryCatch #12 {Exception -> 0x035d, blocks: (B:135:0x0355, B:143:0x036e), top: B:672:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0379 A[Catch: Exception -> 0x03b2, TRY_ENTER, TRY_LEAVE, TryCatch #9 {Exception -> 0x03b2, blocks: (B:141:0x0364, B:145:0x0379), top: B:666:0x0364 }] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0387  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03f0  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0411  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x041e  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x07ab  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0832  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0837  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0841  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x084b  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x08a3  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0a84  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x0ab6  */
    /* JADX WARN: Removed duplicated region for block: B:506:0x0d63  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0e84  */
    /* JADX WARN: Removed duplicated region for block: B:566:0x0e95  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:570:0x0ea3  */
    /* JADX WARN: Removed duplicated region for block: B:575:0x0eb4  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x0ec0  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x0f10  */
    /* JADX WARN: Removed duplicated region for block: B:586:0x0f15  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0f1a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:598:0x0f5e  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x0f95 A[LOOP:4: B:603:0x0f8d->B:605:0x0f95, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:705:0x0eb2 A[EDGE_INSN: B:705:0x0eb2->B:574:0x0eb2 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01b4  */
    /* JADX WARN: Type inference failed for: r7v22 */
    /* JADX WARN: Type inference failed for: r7v23 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v58, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v59 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingMedia$89(ArrayList media, final long dialogId, boolean forceDocument, boolean groupMediaFinal, final AccountInstance accountInstance, final MessageObject editingMessageObject, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final boolean notify, final int scheduleDate, InputContentInfoCompat inputContent) {
        String str;
        String str2;
        HashMap<SendingMediaInfo, MediaSendPrepareWorker> workers;
        boolean isEncrypted;
        long lastGroupId;
        long lastGroupId2;
        AccountInstance accountInstance2;
        int count;
        long groupId;
        int mediaCount;
        int count2;
        int a;
        String str3;
        long groupId2;
        String str4;
        boolean isEncrypted2;
        String str5;
        HashMap<SendingMediaInfo, MediaSendPrepareWorker> workers2;
        int count3;
        int a2;
        long groupId3;
        String str6;
        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList;
        SendingMediaInfo info;
        VideoEditedInfo videoEditedInfo;
        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList2;
        SendingMediaInfo info2;
        boolean muted;
        long startTime;
        VideoEditedInfo videoEditedInfo2;
        long startTime2;
        String parentObject;
        String str7;
        String str8;
        TLRPC.TL_document document;
        SendingMediaInfo info3;
        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList3;
        String thumbKey;
        Bitmap thumb;
        boolean isEncrypted3;
        VideoEditedInfo videoEditedInfo3;
        Object obj;
        TLRPC.TL_document document2;
        TLRPC.TL_document videoFinal;
        final String parentFinal;
        String finalPath;
        final HashMap<String, String> params;
        String finalPath2;
        TLRPC.TL_document videoFinal2;
        int count4;
        int a3;
        long groupId4;
        int b;
        boolean found;
        int b2;
        int N;
        long startTime3;
        Bitmap thumb2;
        int i;
        TLRPC.TL_documentAttributeVideo attributeVideo;
        int w;
        int h;
        TLRPC.TL_document document3;
        String parentObject2;
        int i2;
        String tempPath;
        String tempPath2;
        boolean isDocument;
        String originalPath;
        String str9;
        String str10;
        String tempPath3;
        String originalPath2;
        String originalPath3;
        String tempPath4;
        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList4;
        String str11;
        String originalPath4;
        String str12;
        String parentObject3;
        TLRPC.TL_photo photo;
        int count5;
        long groupId5;
        int a4;
        String parentObject4;
        String parentObject5;
        TLRPC.TL_photo photo2;
        ArrayList<String> sendAsDocumentsCaptions;
        FileOutputStream stream;
        Throwable e;
        String tempPath5;
        TLRPC.TL_photo photo3;
        int count6;
        int a5;
        long groupId6;
        boolean isEncrypted4;
        int count7;
        long groupId7;
        int a6;
        TLRPC.TL_photo photo4;
        File cacheFile;
        int count8;
        File cacheFile2;
        boolean isEncrypted5;
        HashMap<String, String> hashMap;
        int side;
        Bitmap bitmap;
        int a7;
        String str13;
        String str14;
        String tempPath6;
        boolean isWebP;
        String originalPath5;
        TLRPC.TL_photo photo5;
        int i3;
        final SendingMediaInfo info4;
        TLRPC.TL_photo photo6;
        String parentObject6;
        ArrayList arrayList5 = media;
        long beginTime = System.currentTimeMillis();
        int count9 = media.size();
        final boolean isEncrypted6 = DialogObject.isEncryptedDialog(dialogId);
        String str15 = ".webp";
        String str16 = ".gif";
        String str17 = "_";
        Uri uri = null;
        ?? r8 = 1;
        if (forceDocument || !groupMediaFinal) {
            str2 = str16;
            str = str15;
            workers = null;
        } else {
            HashMap<SendingMediaInfo, MediaSendPrepareWorker> workers3 = new HashMap<>();
            int a8 = 0;
            while (a8 < count9) {
                SendingMediaInfo info5 = (SendingMediaInfo) arrayList5.get(a8);
                if (info5.searchImage != null || info5.isVideo || info5.videoEditedInfo != null) {
                    a7 = a8;
                    str14 = str16;
                    str13 = str15;
                } else {
                    String originalPath6 = info5.path;
                    String tempPath7 = info5.path;
                    if (tempPath7 == null && info5.uri != null) {
                        String tempPath8 = AndroidUtilities.getPath(info5.uri);
                        originalPath6 = info5.uri.toString();
                        tempPath6 = tempPath8;
                    } else {
                        tempPath6 = tempPath7;
                    }
                    boolean isWebP2 = false;
                    if (tempPath6 != null && info5.ttl <= 0) {
                        if (!tempPath6.endsWith(str16)) {
                            boolean endsWith = tempPath6.endsWith(str15);
                            isWebP2 = endsWith;
                        }
                        if (media.size() <= r8) {
                            if (!isWebP2) {
                                a7 = a8;
                                str14 = str16;
                                str13 = str15;
                            } else if (shouldSendWebPAsSticker(tempPath6, uri)) {
                                a7 = a8;
                                str14 = str16;
                                str13 = str15;
                            }
                        }
                        info5.forceImage = r8;
                        isWebP = isWebP2;
                        if (tempPath6 == null) {
                            File temp = new File(tempPath6);
                            StringBuilder sb = new StringBuilder();
                            sb.append(originalPath6);
                            str14 = str16;
                            sb.append(temp.length());
                            sb.append(str17);
                            sb.append(temp.lastModified());
                            originalPath5 = sb.toString();
                        } else {
                            str14 = str16;
                            originalPath5 = null;
                        }
                        photo5 = null;
                        String parentObject7 = null;
                        if (!isEncrypted6 || info5.ttl != 0) {
                            info4 = info5;
                            a7 = a8;
                            str13 = str15;
                            i3 = 1;
                        } else {
                            Object[] sentData = accountInstance.getMessagesStorage().getSentFile(originalPath5, !isEncrypted6 ? 0 : 3);
                            if (sentData != null && (sentData[0] instanceof TLRPC.TL_photo)) {
                                photo5 = (TLRPC.TL_photo) sentData[0];
                                parentObject7 = (String) sentData[1];
                            }
                            if (photo5 != null || info5.uri == null) {
                                photo6 = photo5;
                            } else {
                                photo6 = photo5;
                                sentData = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(info5.uri), !isEncrypted6 ? 0 : 3);
                                if (sentData != null && (sentData[0] instanceof TLRPC.TL_photo)) {
                                    String parentObject8 = (String) sentData[1];
                                    photo6 = (TLRPC.TL_photo) sentData[0];
                                    parentObject6 = parentObject8;
                                    info4 = info5;
                                    a7 = a8;
                                    str13 = str15;
                                    i3 = 1;
                                    ensureMediaThumbExists(accountInstance, isEncrypted6, photo6, info5.path, info5.uri, 0L);
                                    parentObject7 = parentObject6;
                                    photo5 = photo6;
                                }
                            }
                            parentObject6 = parentObject7;
                            info4 = info5;
                            a7 = a8;
                            str13 = str15;
                            i3 = 1;
                            ensureMediaThumbExists(accountInstance, isEncrypted6, photo6, info5.path, info5.uri, 0L);
                            parentObject7 = parentObject6;
                            photo5 = photo6;
                        }
                        final MediaSendPrepareWorker worker = new MediaSendPrepareWorker();
                        workers3.put(info4, worker);
                        if (photo5 == null) {
                            worker.parentObject = parentObject7;
                            worker.photo = photo5;
                        } else {
                            worker.sync = new CountDownLatch(i3);
                            mediaSendThreadPool.execute(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda13
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.lambda$prepareSendingMedia$84(SendMessagesHelper.MediaSendPrepareWorker.this, accountInstance, info4, isEncrypted6);
                                }
                            });
                        }
                    }
                    if (ImageLoader.shouldSendImageAsDocument(info5.path, info5.uri)) {
                        a7 = a8;
                        str14 = str16;
                        str13 = str15;
                    } else {
                        if (tempPath6 == null && info5.uri != null) {
                            if (!MediaController.isGif(info5.uri)) {
                                boolean isWebp = MediaController.isWebp(info5.uri);
                                isWebP2 = isWebp;
                                if (!isWebp) {
                                    isWebP = isWebP2;
                                }
                            }
                            if (media.size() <= r8) {
                                if (!isWebP2) {
                                    a7 = a8;
                                    str14 = str16;
                                    str13 = str15;
                                } else if (shouldSendWebPAsSticker(null, info5.uri)) {
                                    a7 = a8;
                                    str14 = str16;
                                    str13 = str15;
                                }
                            }
                            info5.forceImage = r8;
                            isWebP = isWebP2;
                        } else {
                            isWebP = isWebP2;
                        }
                        if (tempPath6 == null) {
                        }
                        photo5 = null;
                        String parentObject72 = null;
                        if (!isEncrypted6) {
                        }
                        info4 = info5;
                        a7 = a8;
                        str13 = str15;
                        i3 = 1;
                        final MediaSendPrepareWorker worker2 = new MediaSendPrepareWorker();
                        workers3.put(info4, worker2);
                        if (photo5 == null) {
                        }
                    }
                }
                a8 = a7 + 1;
                str16 = str14;
                str15 = str13;
                r8 = 1;
                uri = null;
            }
            str2 = str16;
            str = str15;
            workers = workers3;
        }
        String extension = null;
        ArrayList<ArrayList<TLRPC.MessageEntity>> sendAsDocumentsEntities = null;
        int mediaCount2 = 0;
        ArrayList<String> sendAsDocumentsOriginal = null;
        ArrayList<String> sendAsDocuments = null;
        int a9 = 0;
        long groupId8 = 0;
        ArrayList<Uri> sendAsDocumentsUri = null;
        ArrayList<String> sendAsDocumentsCaptions2 = null;
        long lastGroupId3 = 0;
        while (true) {
            isEncrypted = isEncrypted6;
            lastGroupId = lastGroupId3;
            if (a9 >= count9) {
                break;
            }
            final SendingMediaInfo info6 = (SendingMediaInfo) arrayList5.get(a9);
            if (groupMediaFinal && count9 > 1 && mediaCount2 % 10 == 0) {
                long groupId9 = Utilities.random.nextLong();
                lastGroupId = groupId9;
                groupId = groupId9;
                mediaCount = 0;
            } else {
                groupId = groupId8;
                mediaCount = mediaCount2;
            }
            ArrayList<String> sendAsDocumentsCaptions3 = sendAsDocumentsCaptions2;
            ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList6 = sendAsDocumentsEntities;
            HashMap<SendingMediaInfo, MediaSendPrepareWorker> workers4 = workers;
            ArrayList<String> arrayList7 = sendAsDocuments;
            ArrayList<Uri> sendAsDocumentsUri2 = sendAsDocumentsUri;
            ArrayList<String> sendAsDocumentsOriginal2 = sendAsDocumentsOriginal;
            if (info6.searchImage == null || info6.videoEditedInfo != null) {
                int a10 = a9;
                str3 = str17;
                int count10 = count9;
                String str18 = str2;
                String str19 = str;
                long groupId10 = groupId;
                if (info6.isVideo) {
                    info = info6;
                    str6 = str3;
                    workers2 = workers4;
                    str4 = str18;
                    str5 = str19;
                    arrayList = arrayList6;
                    groupId3 = groupId10;
                    a2 = a10;
                    count3 = count10;
                } else if (info6.videoEditedInfo != null) {
                    info = info6;
                    str6 = str3;
                    workers2 = workers4;
                    str4 = str18;
                    str5 = str19;
                    arrayList = arrayList6;
                    groupId3 = groupId10;
                    a2 = a10;
                    count3 = count10;
                } else {
                    String originalPath7 = info6.path;
                    String tempPath9 = info6.path;
                    if (tempPath9 == null && info6.uri != null) {
                        if (Build.VERSION.SDK_INT >= 30 && "content".equals(info6.uri.getScheme())) {
                            tempPath5 = null;
                        } else {
                            tempPath5 = AndroidUtilities.getPath(info6.uri);
                        }
                        tempPath = tempPath5;
                        tempPath2 = info6.uri.toString();
                    } else {
                        tempPath = tempPath9;
                        tempPath2 = originalPath7;
                    }
                    if (inputContent == null || info6.uri == null) {
                        originalPath = tempPath2;
                        isDocument = false;
                        str9 = str19;
                    } else {
                        ClipDescription description = inputContent.getDescription();
                        if (!description.hasMimeType("image/png")) {
                            originalPath = tempPath2;
                            isDocument = false;
                            str9 = str19;
                        } else {
                            InputStream inputStream = null;
                            try {
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(info6.uri);
                                Bitmap b3 = BitmapFactory.decodeStream(inputStream, null, bmOptions);
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("-2147483648_");
                                sb2.append(SharedConfig.getLastLocalId());
                                stream = null;
                                str9 = str19;
                                try {
                                    sb2.append(str9);
                                    String fileName = sb2.toString();
                                    File fileDir = FileLoader.getDirectory(4);
                                    originalPath = tempPath2;
                                    try {
                                        File cacheFile3 = new File(fileDir, fileName);
                                        stream = new FileOutputStream(cacheFile3);
                                        try {
                                            isDocument = false;
                                            try {
                                                b3.compress(Bitmap.CompressFormat.WEBP, 100, stream);
                                                SharedConfig.saveConfig();
                                                info6.uri = Uri.fromFile(cacheFile3);
                                                if (inputStream != null) {
                                                    try {
                                                        inputStream.close();
                                                    } catch (Exception e2) {
                                                    }
                                                }
                                                try {
                                                    stream.close();
                                                } catch (Exception e3) {
                                                }
                                            } catch (Throwable th) {
                                                e = th;
                                                stream = stream;
                                                try {
                                                    FileLog.e(e);
                                                    if (inputStream != null) {
                                                        try {
                                                            inputStream.close();
                                                        } catch (Exception e4) {
                                                        }
                                                    }
                                                    if (stream != null) {
                                                        try {
                                                            stream.close();
                                                        } catch (Exception e5) {
                                                        }
                                                    }
                                                    if (!forceDocument) {
                                                    }
                                                    extension = tempPath == null ? FileLoader.getFileExtension(new File(tempPath)) : "";
                                                    tempPath3 = tempPath;
                                                    isDocument = true;
                                                    originalPath2 = originalPath;
                                                    if (isDocument) {
                                                    }
                                                    a9 = a + 1;
                                                    arrayList5 = media;
                                                    workers = workers2;
                                                    str = str5;
                                                    isEncrypted6 = isEncrypted2;
                                                    str2 = str4;
                                                    groupId8 = groupId2;
                                                    str17 = str3;
                                                    count9 = count2;
                                                } catch (Throwable th2) {
                                                    if (inputStream != null) {
                                                        try {
                                                            inputStream.close();
                                                        } catch (Exception e6) {
                                                        }
                                                    }
                                                    if (stream != null) {
                                                        try {
                                                            stream.close();
                                                        } catch (Exception e7) {
                                                        }
                                                    }
                                                    throw th2;
                                                }
                                            }
                                        } catch (Throwable th3) {
                                            e = th3;
                                            isDocument = false;
                                        }
                                    } catch (Throwable th4) {
                                        e = th4;
                                        isDocument = false;
                                    }
                                } catch (Throwable th5) {
                                    e = th5;
                                    originalPath = tempPath2;
                                    isDocument = false;
                                }
                            } catch (Throwable th6) {
                                e = th6;
                                originalPath = tempPath2;
                                isDocument = false;
                                stream = null;
                                str9 = str19;
                            }
                        }
                    }
                    if (!forceDocument) {
                        str10 = str18;
                    } else if (ImageLoader.shouldSendImageAsDocument(info6.path, info6.uri)) {
                        str10 = str18;
                    } else {
                        if (info6.forceImage || tempPath == null) {
                            str10 = str18;
                        } else {
                            str10 = str18;
                            if ((tempPath.endsWith(str10) || tempPath.endsWith(str9)) && info6.ttl <= 0) {
                                if (tempPath.endsWith(str10)) {
                                    extension = "gif";
                                } else {
                                    extension = "webp";
                                }
                                tempPath3 = tempPath;
                                isDocument = true;
                                originalPath2 = originalPath;
                                if (isDocument) {
                                    if (arrayList6 != null) {
                                        sendAsDocumentsEntities = arrayList6;
                                        sendAsDocuments = arrayList7;
                                        sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                        sendAsDocumentsUri = sendAsDocumentsUri2;
                                        sendAsDocumentsCaptions = sendAsDocumentsCaptions3;
                                    } else {
                                        sendAsDocumentsEntities = new ArrayList<>();
                                        sendAsDocumentsOriginal = new ArrayList<>();
                                        sendAsDocumentsCaptions = new ArrayList<>();
                                        sendAsDocuments = new ArrayList<>();
                                        sendAsDocumentsUri = new ArrayList<>();
                                    }
                                    sendAsDocumentsEntities.add(tempPath3);
                                    sendAsDocumentsOriginal.add(originalPath2);
                                    sendAsDocumentsUri.add(info6.uri);
                                    sendAsDocumentsCaptions.add(info6.caption);
                                    sendAsDocuments.add(info6.entities);
                                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions;
                                    str5 = str9;
                                    str4 = str10;
                                    mediaCount2 = mediaCount;
                                    isEncrypted2 = isEncrypted;
                                    lastGroupId3 = lastGroupId;
                                    workers2 = workers4;
                                    groupId2 = groupId10;
                                    a = a10;
                                    count2 = count10;
                                } else {
                                    if (tempPath3 != null) {
                                        File temp2 = new File(tempPath3);
                                        originalPath3 = originalPath2 + temp2.length() + str3 + temp2.lastModified();
                                    } else {
                                        originalPath3 = null;
                                    }
                                    TLRPC.TL_photo photo7 = null;
                                    if (workers4 != null) {
                                        MediaSendPrepareWorker worker3 = workers4.get(info6);
                                        TLRPC.TL_photo photo8 = worker3.photo;
                                        String parentObject9 = worker3.parentObject;
                                        if (photo8 == null) {
                                            try {
                                                worker3.sync.await();
                                            } catch (Exception e8) {
                                                FileLog.e(e8);
                                            }
                                            photo8 = worker3.photo;
                                            parentObject9 = worker3.parentObject;
                                        }
                                        parentObject3 = parentObject9;
                                        originalPath4 = originalPath3;
                                        arrayList4 = arrayList6;
                                        str5 = str9;
                                        tempPath4 = tempPath3;
                                        str12 = str10;
                                        str11 = str3;
                                        workers2 = workers4;
                                        photo = photo8;
                                    } else {
                                        String parentObject10 = null;
                                        if (isEncrypted || info6.ttl != 0) {
                                            originalPath4 = originalPath3;
                                            arrayList4 = arrayList6;
                                            str5 = str9;
                                            tempPath4 = tempPath3;
                                            str12 = str10;
                                            str11 = str3;
                                            workers2 = workers4;
                                            parentObject4 = null;
                                        } else {
                                            Object[] sentData2 = accountInstance.getMessagesStorage().getSentFile(originalPath3, !isEncrypted ? 0 : 3);
                                            if (sentData2 != null && (sentData2[0] instanceof TLRPC.TL_photo)) {
                                                photo7 = (TLRPC.TL_photo) sentData2[0];
                                                parentObject10 = (String) sentData2[1];
                                            }
                                            if (photo7 != null) {
                                                photo2 = photo7;
                                            } else if (info6.uri == null) {
                                                photo2 = photo7;
                                            } else {
                                                photo2 = photo7;
                                                sentData2 = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(info6.uri), !isEncrypted ? 0 : 3);
                                                if (sentData2 != null && (sentData2[0] instanceof TLRPC.TL_photo)) {
                                                    photo7 = (TLRPC.TL_photo) sentData2[0];
                                                    String parentObject11 = (String) sentData2[1];
                                                    parentObject5 = parentObject11;
                                                    workers2 = workers4;
                                                    originalPath4 = originalPath3;
                                                    str11 = str3;
                                                    arrayList4 = arrayList6;
                                                    str5 = str9;
                                                    tempPath4 = tempPath3;
                                                    str12 = str10;
                                                    ensureMediaThumbExists(accountInstance, isEncrypted, photo7, info6.path, info6.uri, 0L);
                                                    parentObject4 = parentObject5;
                                                }
                                            }
                                            photo7 = photo2;
                                            parentObject5 = parentObject10;
                                            workers2 = workers4;
                                            originalPath4 = originalPath3;
                                            str11 = str3;
                                            arrayList4 = arrayList6;
                                            str5 = str9;
                                            tempPath4 = tempPath3;
                                            str12 = str10;
                                            ensureMediaThumbExists(accountInstance, isEncrypted, photo7, info6.path, info6.uri, 0L);
                                            parentObject4 = parentObject5;
                                        }
                                        if (photo7 != null) {
                                            photo = photo7;
                                            parentObject3 = parentObject4;
                                        } else {
                                            TLRPC.TL_photo photo9 = accountInstance.getSendMessagesHelper().generatePhotoSizes(info6.path, info6.uri);
                                            if (isEncrypted && info6.canDeleteAfter) {
                                                new File(info6.path).delete();
                                            }
                                            photo = photo9;
                                            parentObject3 = parentObject4;
                                        }
                                    }
                                    if (photo != null) {
                                        final TLRPC.TL_photo photoFinal = photo;
                                        final String parentFinal2 = parentObject3;
                                        final HashMap<String, String> params2 = new HashMap<>();
                                        final Bitmap[] bitmapFinal = new Bitmap[1];
                                        final String[] keyFinal = new String[1];
                                        boolean z = info6.masks != null && !info6.masks.isEmpty();
                                        photo.has_stickers = z;
                                        if (z) {
                                            SerializedData serializedData = new SerializedData((info6.masks.size() * 20) + 4);
                                            serializedData.writeInt32(info6.masks.size());
                                            for (int b4 = 0; b4 < info6.masks.size(); b4++) {
                                                info6.masks.get(b4).serializeToStream(serializedData);
                                            }
                                            params2.put("masks", Utilities.bytesToHex(serializedData.toByteArray()));
                                            serializedData.cleanup();
                                        }
                                        String originalPath8 = originalPath4;
                                        if (originalPath8 != null) {
                                            params2.put("originalPath", originalPath8);
                                        }
                                        if (parentFinal2 != null) {
                                            params2.put("parentObject", parentFinal2);
                                        }
                                        if (groupMediaFinal) {
                                            try {
                                            } catch (Exception e9) {
                                                e = e9;
                                                FileLog.e(e);
                                                if (!groupMediaFinal) {
                                                }
                                                long groupId11 = groupId5;
                                                str4 = str12;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$88(bitmapFinal, keyFinal, editingMessageObject, accountInstance, photoFinal, params2, parentFinal2, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                                    }
                                                });
                                                mediaCount2 = mediaCount;
                                                isEncrypted2 = isEncrypted;
                                                lastGroupId3 = lastGroupId;
                                                sendAsDocuments = arrayList7;
                                                sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                                sendAsDocumentsUri = sendAsDocumentsUri2;
                                                sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                                str3 = str11;
                                                sendAsDocumentsEntities = arrayList4;
                                                groupId2 = groupId11;
                                                a = a4;
                                                count2 = count5;
                                                a9 = a + 1;
                                                arrayList5 = media;
                                                workers = workers2;
                                                str = str5;
                                                isEncrypted6 = isEncrypted2;
                                                str2 = str4;
                                                groupId8 = groupId2;
                                                str17 = str3;
                                                count9 = count2;
                                            }
                                            if (media.size() != 1) {
                                                if (!groupMediaFinal) {
                                                    groupId5 = groupId10;
                                                    a4 = a10;
                                                    count5 = count10;
                                                } else {
                                                    int mediaCount3 = mediaCount + 1;
                                                    StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("");
                                                    groupId5 = groupId10;
                                                    sb3.append(groupId5);
                                                    params2.put("groupId", sb3.toString());
                                                    if (mediaCount3 != 10) {
                                                        count5 = count10;
                                                        a4 = a10;
                                                        if (a4 != count5 - 1) {
                                                            mediaCount = mediaCount3;
                                                        }
                                                    } else {
                                                        a4 = a10;
                                                        count5 = count10;
                                                    }
                                                    params2.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                                                    lastGroupId = 0;
                                                    mediaCount = mediaCount3;
                                                }
                                                long groupId112 = groupId5;
                                                str4 = str12;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$88(bitmapFinal, keyFinal, editingMessageObject, accountInstance, photoFinal, params2, parentFinal2, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                                    }
                                                });
                                                mediaCount2 = mediaCount;
                                                isEncrypted2 = isEncrypted;
                                                lastGroupId3 = lastGroupId;
                                                sendAsDocuments = arrayList7;
                                                sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                                sendAsDocumentsUri = sendAsDocumentsUri2;
                                                sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                                str3 = str11;
                                                sendAsDocumentsEntities = arrayList4;
                                                groupId2 = groupId112;
                                                a = a4;
                                                count2 = count5;
                                            }
                                        }
                                        TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoFinal.sizes, AndroidUtilities.getPhotoSize());
                                        if (currentPhotoObject != null) {
                                            try {
                                                keyFinal[0] = getKeyForPhotoSize(accountInstance, currentPhotoObject, bitmapFinal, false, false);
                                            } catch (Exception e10) {
                                                e = e10;
                                                FileLog.e(e);
                                                if (!groupMediaFinal) {
                                                }
                                                long groupId1122 = groupId5;
                                                str4 = str12;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$88(bitmapFinal, keyFinal, editingMessageObject, accountInstance, photoFinal, params2, parentFinal2, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                                    }
                                                });
                                                mediaCount2 = mediaCount;
                                                isEncrypted2 = isEncrypted;
                                                lastGroupId3 = lastGroupId;
                                                sendAsDocuments = arrayList7;
                                                sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                                sendAsDocumentsUri = sendAsDocumentsUri2;
                                                sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                                str3 = str11;
                                                sendAsDocumentsEntities = arrayList4;
                                                groupId2 = groupId1122;
                                                a = a4;
                                                count2 = count5;
                                                a9 = a + 1;
                                                arrayList5 = media;
                                                workers = workers2;
                                                str = str5;
                                                isEncrypted6 = isEncrypted2;
                                                str2 = str4;
                                                groupId8 = groupId2;
                                                str17 = str3;
                                                count9 = count2;
                                            }
                                        }
                                        if (!groupMediaFinal) {
                                        }
                                        long groupId11222 = groupId5;
                                        str4 = str12;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingMedia$88(bitmapFinal, keyFinal, editingMessageObject, accountInstance, photoFinal, params2, parentFinal2, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                            }
                                        });
                                        mediaCount2 = mediaCount;
                                        isEncrypted2 = isEncrypted;
                                        lastGroupId3 = lastGroupId;
                                        sendAsDocuments = arrayList7;
                                        sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                        sendAsDocumentsUri = sendAsDocumentsUri2;
                                        sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                        str3 = str11;
                                        sendAsDocumentsEntities = arrayList4;
                                        groupId2 = groupId11222;
                                        a = a4;
                                        count2 = count5;
                                    } else {
                                        str4 = str12;
                                        String originalPath9 = originalPath4;
                                        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList8 = arrayList4;
                                        if (arrayList8 != null) {
                                            sendAsDocumentsEntities = arrayList8;
                                            sendAsDocuments = arrayList7;
                                            sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                            sendAsDocumentsUri = sendAsDocumentsUri2;
                                            sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                        } else {
                                            sendAsDocumentsEntities = new ArrayList<>();
                                            sendAsDocumentsOriginal = new ArrayList<>();
                                            sendAsDocumentsCaptions2 = new ArrayList<>();
                                            sendAsDocuments = new ArrayList<>();
                                            sendAsDocumentsUri = new ArrayList<>();
                                        }
                                        sendAsDocumentsEntities.add(tempPath4);
                                        sendAsDocumentsOriginal.add(originalPath9);
                                        sendAsDocumentsUri.add(info6.uri);
                                        sendAsDocumentsCaptions2.add(info6.caption);
                                        sendAsDocuments.add(info6.entities);
                                        mediaCount2 = mediaCount;
                                        isEncrypted2 = isEncrypted;
                                        lastGroupId3 = lastGroupId;
                                        str3 = str11;
                                        groupId2 = groupId10;
                                        a = a10;
                                        count2 = count10;
                                    }
                                }
                            }
                        }
                        if (!info6.forceImage && tempPath == null && info6.uri != null) {
                            if (MediaController.isGif(info6.uri)) {
                                originalPath2 = info6.uri.toString();
                                extension = "gif";
                                tempPath3 = MediaController.copyFileToCache(info6.uri, "gif");
                                isDocument = true;
                            } else if (MediaController.isWebp(info6.uri)) {
                                originalPath2 = info6.uri.toString();
                                extension = "webp";
                                tempPath3 = MediaController.copyFileToCache(info6.uri, "webp");
                                isDocument = true;
                            }
                            if (isDocument) {
                            }
                        }
                        tempPath3 = tempPath;
                        originalPath2 = originalPath;
                        if (isDocument) {
                        }
                    }
                    extension = tempPath == null ? FileLoader.getFileExtension(new File(tempPath)) : "";
                    tempPath3 = tempPath;
                    isDocument = true;
                    originalPath2 = originalPath;
                    if (isDocument) {
                    }
                }
                String thumbKey2 = null;
                if (forceDocument) {
                    videoEditedInfo = null;
                } else {
                    videoEditedInfo = info.videoEditedInfo != null ? info.videoEditedInfo : createCompressionSettings(info.path);
                }
                if (forceDocument) {
                    info2 = info;
                    arrayList2 = arrayList;
                    isEncrypted2 = isEncrypted;
                    str3 = str6;
                    groupId2 = groupId3;
                    a = a2;
                    count2 = count3;
                } else if (videoEditedInfo != null || info.path.endsWith("mp4")) {
                    if (info.path == null && info.searchImage != null) {
                        if (info.searchImage.photo instanceof TLRPC.TL_photo) {
                            info.path = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(info.searchImage.photo, true).getAbsolutePath();
                        } else {
                            String md5 = Utilities.MD5(info.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(info.searchImage.imageUrl, "jpg");
                            info.path = new File(FileLoader.getDirectory(4), md5).getAbsolutePath();
                        }
                    }
                    String path = info.path;
                    String originalPath10 = info.path;
                    File temp3 = new File(originalPath10);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(originalPath10);
                    String path2 = path;
                    Bitmap thumb3 = null;
                    sb4.append(temp3.length());
                    String str20 = str6;
                    sb4.append(str20);
                    sb4.append(temp3.lastModified());
                    String originalPath11 = sb4.toString();
                    if (videoEditedInfo == null) {
                        muted = false;
                        startTime = 0;
                    } else {
                        boolean muted2 = videoEditedInfo.muted;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(originalPath11);
                        sb5.append(videoEditedInfo.estimatedDuration);
                        sb5.append(str20);
                        sb5.append(videoEditedInfo.startTime);
                        sb5.append(str20);
                        sb5.append(videoEditedInfo.endTime);
                        sb5.append(videoEditedInfo.muted ? "_m" : "");
                        originalPath11 = sb5.toString();
                        if (videoEditedInfo.resultWidth != videoEditedInfo.originalWidth) {
                            originalPath11 = originalPath11 + str20 + videoEditedInfo.resultWidth;
                        }
                        muted = muted2;
                        startTime = videoEditedInfo.startTime >= 0 ? videoEditedInfo.startTime : 0L;
                    }
                    if (!isEncrypted) {
                        str3 = str20;
                        if (info.ttl == 0) {
                            if (videoEditedInfo != null && (videoEditedInfo.filterState != null || videoEditedInfo.paintPath != null || videoEditedInfo.mediaEntities != null || videoEditedInfo.cropState != null)) {
                                document3 = null;
                                parentObject2 = null;
                                startTime2 = startTime;
                                videoEditedInfo2 = videoEditedInfo;
                                info3 = info;
                                str7 = "";
                                str8 = MimeTypes.VIDEO_MP4;
                            } else {
                                MessagesStorage messagesStorage = accountInstance.getMessagesStorage();
                                if (!isEncrypted) {
                                    document3 = null;
                                    i2 = 2;
                                } else {
                                    document3 = null;
                                    i2 = 5;
                                }
                                Object[] sentData3 = messagesStorage.getSentFile(originalPath11, i2);
                                if (sentData3 != null) {
                                    parentObject2 = null;
                                    if (sentData3[0] instanceof TLRPC.TL_document) {
                                        TLRPC.TL_document document4 = (TLRPC.TL_document) sentData3[0];
                                        String parentObject12 = (String) sentData3[1];
                                        String str21 = info.path;
                                        startTime2 = startTime;
                                        videoEditedInfo2 = videoEditedInfo;
                                        info3 = info;
                                        str7 = "";
                                        str8 = MimeTypes.VIDEO_MP4;
                                        ensureMediaThumbExists(accountInstance, isEncrypted, document4, str21, null, startTime2);
                                        document = document4;
                                        parentObject = parentObject12;
                                        if (document != null) {
                                            arrayList3 = arrayList;
                                            isEncrypted3 = isEncrypted;
                                            videoEditedInfo3 = videoEditedInfo2;
                                            obj = null;
                                            thumbKey = null;
                                            document2 = document;
                                            thumb = null;
                                        } else {
                                            if (info3.thumbPath != null) {
                                                thumb3 = BitmapFactory.decodeFile(info3.thumbPath);
                                            }
                                            if (thumb3 != null) {
                                                startTime3 = startTime2;
                                                thumb2 = thumb3;
                                            } else {
                                                startTime3 = startTime2;
                                                thumb2 = createVideoThumbnailAtTime(info3.path, startTime3);
                                                if (thumb2 == null) {
                                                    thumb2 = createVideoThumbnail(info3.path, 1);
                                                }
                                            }
                                            TLRPC.PhotoSize size = null;
                                            if (thumb2 != null) {
                                                int side2 = (isEncrypted || info3.ttl != 0) ? 90 : Math.max(thumb2.getWidth(), thumb2.getHeight());
                                                arrayList3 = arrayList;
                                                isEncrypted3 = isEncrypted;
                                                size = ImageLoader.scaleAndSaveImage(thumb2, side2, side2, side2 > 90 ? 80 : 55, isEncrypted3);
                                                startTime2 = startTime3;
                                                i = 0;
                                                thumbKey2 = getKeyForPhotoSize(accountInstance, size, null, true, false);
                                            } else {
                                                startTime2 = startTime3;
                                                arrayList3 = arrayList;
                                                isEncrypted3 = isEncrypted;
                                                i = 0;
                                            }
                                            TLRPC.TL_document document5 = new TLRPC.TL_document();
                                            document5.file_reference = new byte[i];
                                            if (size != null) {
                                                document5.thumbs.add(size);
                                                document5.flags |= 1;
                                            }
                                            document5.mime_type = str8;
                                            accountInstance.getUserConfig().saveConfig(i);
                                            if (isEncrypted3) {
                                                attributeVideo = new TLRPC.TL_documentAttributeVideo();
                                            } else {
                                                attributeVideo = new TLRPC.TL_documentAttributeVideo();
                                                attributeVideo.supports_streaming = true;
                                            }
                                            document5.attributes.add(attributeVideo);
                                            videoEditedInfo3 = videoEditedInfo2;
                                            if (videoEditedInfo3 == null) {
                                                thumb = thumb2;
                                            } else if (videoEditedInfo3.needConvert() || !info3.isVideo) {
                                                if (info3.isVideo && videoEditedInfo3.muted) {
                                                    fillVideoAttribute(info3.path, attributeVideo, videoEditedInfo3);
                                                    videoEditedInfo3.originalWidth = attributeVideo.w;
                                                    videoEditedInfo3.originalHeight = attributeVideo.h;
                                                } else {
                                                    attributeVideo.duration = (int) (videoEditedInfo3.estimatedDuration / 1000);
                                                }
                                                int rotation = videoEditedInfo3.rotationValue;
                                                if (videoEditedInfo3.cropState != null) {
                                                    w = videoEditedInfo3.cropState.transformWidth;
                                                    h = videoEditedInfo3.cropState.transformHeight;
                                                } else {
                                                    w = videoEditedInfo3.resultWidth;
                                                    h = videoEditedInfo3.resultHeight;
                                                }
                                                thumb = thumb2;
                                                if (rotation == 90 || rotation == 270) {
                                                    attributeVideo.w = h;
                                                    attributeVideo.h = w;
                                                } else {
                                                    attributeVideo.w = w;
                                                    attributeVideo.h = h;
                                                }
                                                document5.size = (int) videoEditedInfo3.estimatedSize;
                                                obj = null;
                                                document2 = document5;
                                                thumbKey = thumbKey2;
                                            } else {
                                                thumb = thumb2;
                                            }
                                            if (temp3.exists()) {
                                                document5.size = (int) temp3.length();
                                            }
                                            obj = null;
                                            fillVideoAttribute(info3.path, attributeVideo, null);
                                            document2 = document5;
                                            thumbKey = thumbKey2;
                                        }
                                        if (videoEditedInfo3 != null && videoEditedInfo3.muted) {
                                            found = false;
                                            b2 = 0;
                                            N = document2.attributes.size();
                                            while (true) {
                                                if (b2 < N) {
                                                    if (!(document2.attributes.get(b2) instanceof TLRPC.TL_documentAttributeAnimated)) {
                                                        b2++;
                                                    } else {
                                                        found = true;
                                                        break;
                                                    }
                                                } else {
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                document2.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                            }
                                        }
                                        if (videoEditedInfo3 != null && (videoEditedInfo3.needConvert() || !info3.isVideo)) {
                                            String fileName2 = "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4";
                                            File cacheFile4 = new File(FileLoader.getDirectory(4), fileName2);
                                            SharedConfig.saveConfig();
                                            path2 = cacheFile4.getAbsolutePath();
                                        }
                                        videoFinal = document2;
                                        parentFinal = parentObject;
                                        String str22 = str7;
                                        final VideoEditedInfo videoEditedInfo4 = videoEditedInfo3;
                                        finalPath = path2;
                                        params = new HashMap<>();
                                        final Bitmap thumbFinal = thumb;
                                        final String thumbKeyFinal = thumbKey;
                                        if (originalPath11 != null) {
                                            params.put("originalPath", originalPath11);
                                        }
                                        if (parentFinal != null) {
                                            params.put("parentObject", parentFinal);
                                        }
                                        if (!muted || !groupMediaFinal) {
                                            videoFinal2 = videoFinal;
                                            finalPath2 = finalPath;
                                            groupId4 = groupId3;
                                            a3 = a2;
                                            count4 = count3;
                                        } else {
                                            int mediaCount4 = mediaCount + 1;
                                            StringBuilder sb6 = new StringBuilder();
                                            sb6.append(str22);
                                            videoFinal2 = videoFinal;
                                            finalPath2 = finalPath;
                                            groupId4 = groupId3;
                                            sb6.append(groupId4);
                                            params.put("groupId", sb6.toString());
                                            if (mediaCount4 != 10) {
                                                count4 = count3;
                                                a3 = a2;
                                                if (a3 != count4 - 1) {
                                                    mediaCount = mediaCount4;
                                                }
                                            } else {
                                                a3 = a2;
                                                count4 = count3;
                                            }
                                            params.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                                            lastGroupId = 0;
                                            mediaCount = mediaCount4;
                                        }
                                        if (!isEncrypted3 && info3.masks != null && !info3.masks.isEmpty()) {
                                            document2.attributes.add(new TLRPC.TL_documentAttributeHasStickers());
                                            SerializedData serializedData2 = new SerializedData((info3.masks.size() * 20) + 4);
                                            serializedData2.writeInt32(info3.masks.size());
                                            for (b = 0; b < info3.masks.size(); b++) {
                                                info3.masks.get(b).serializeToStream(serializedData2);
                                            }
                                            params.put("masks", Utilities.bytesToHex(serializedData2.toByteArray()));
                                            serializedData2.cleanup();
                                        }
                                        a = a3;
                                        count2 = count4;
                                        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList9 = arrayList3;
                                        isEncrypted2 = isEncrypted3;
                                        final SendingMediaInfo sendingMediaInfo = info3;
                                        final String finalPath3 = finalPath2;
                                        long j = groupId4;
                                        final TLRPC.TL_document videoFinal3 = videoFinal2;
                                        groupId2 = j;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda66
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingMedia$87(thumbFinal, thumbKeyFinal, editingMessageObject, accountInstance, videoEditedInfo4, videoFinal3, finalPath3, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, sendingMediaInfo, notify, scheduleDate);
                                            }
                                        });
                                        sendAsDocuments = arrayList7;
                                        sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                        sendAsDocumentsUri = sendAsDocumentsUri2;
                                        sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                        sendAsDocumentsEntities = arrayList9;
                                        mediaCount2 = mediaCount;
                                        lastGroupId3 = lastGroupId;
                                    } else {
                                        startTime2 = startTime;
                                        videoEditedInfo2 = videoEditedInfo;
                                        info3 = info;
                                        str7 = "";
                                        str8 = MimeTypes.VIDEO_MP4;
                                    }
                                } else {
                                    parentObject2 = null;
                                    startTime2 = startTime;
                                    videoEditedInfo2 = videoEditedInfo;
                                    info3 = info;
                                    str7 = "";
                                    str8 = MimeTypes.VIDEO_MP4;
                                }
                            }
                            document = document3;
                            parentObject = parentObject2;
                            if (document != null) {
                            }
                            if (videoEditedInfo3 != null) {
                                found = false;
                                b2 = 0;
                                N = document2.attributes.size();
                                while (true) {
                                    if (b2 < N) {
                                    }
                                    b2++;
                                }
                                if (!found) {
                                }
                            }
                            if (videoEditedInfo3 != null) {
                                String fileName22 = "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4";
                                File cacheFile42 = new File(FileLoader.getDirectory(4), fileName22);
                                SharedConfig.saveConfig();
                                path2 = cacheFile42.getAbsolutePath();
                            }
                            videoFinal = document2;
                            parentFinal = parentObject;
                            String str222 = str7;
                            final VideoEditedInfo videoEditedInfo42 = videoEditedInfo3;
                            finalPath = path2;
                            params = new HashMap<>();
                            final Bitmap thumbFinal2 = thumb;
                            final String thumbKeyFinal2 = thumbKey;
                            if (originalPath11 != null) {
                            }
                            if (parentFinal != null) {
                            }
                            if (!muted) {
                            }
                            videoFinal2 = videoFinal;
                            finalPath2 = finalPath;
                            groupId4 = groupId3;
                            a3 = a2;
                            count4 = count3;
                            if (!isEncrypted3) {
                                document2.attributes.add(new TLRPC.TL_documentAttributeHasStickers());
                                SerializedData serializedData22 = new SerializedData((info3.masks.size() * 20) + 4);
                                serializedData22.writeInt32(info3.masks.size());
                                while (b < info3.masks.size()) {
                                }
                                params.put("masks", Utilities.bytesToHex(serializedData22.toByteArray()));
                                serializedData22.cleanup();
                            }
                            a = a3;
                            count2 = count4;
                            ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList92 = arrayList3;
                            isEncrypted2 = isEncrypted3;
                            final SendingMediaInfo sendingMediaInfo2 = info3;
                            final String finalPath32 = finalPath2;
                            long j2 = groupId4;
                            final TLRPC.TL_document videoFinal32 = videoFinal2;
                            groupId2 = j2;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda66
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.lambda$prepareSendingMedia$87(thumbFinal2, thumbKeyFinal2, editingMessageObject, accountInstance, videoEditedInfo42, videoFinal32, finalPath32, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, sendingMediaInfo2, notify, scheduleDate);
                                }
                            });
                            sendAsDocuments = arrayList7;
                            sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                            sendAsDocumentsUri = sendAsDocumentsUri2;
                            sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                            sendAsDocumentsEntities = arrayList92;
                            mediaCount2 = mediaCount;
                            lastGroupId3 = lastGroupId;
                        }
                    } else {
                        str3 = str20;
                    }
                    document3 = null;
                    parentObject2 = null;
                    startTime2 = startTime;
                    videoEditedInfo2 = videoEditedInfo;
                    info3 = info;
                    str7 = "";
                    str8 = MimeTypes.VIDEO_MP4;
                    document = document3;
                    parentObject = parentObject2;
                    if (document != null) {
                    }
                    if (videoEditedInfo3 != null) {
                    }
                    if (videoEditedInfo3 != null) {
                    }
                    videoFinal = document2;
                    parentFinal = parentObject;
                    String str2222 = str7;
                    final VideoEditedInfo videoEditedInfo422 = videoEditedInfo3;
                    finalPath = path2;
                    params = new HashMap<>();
                    final Bitmap thumbFinal22 = thumb;
                    final String thumbKeyFinal22 = thumbKey;
                    if (originalPath11 != null) {
                    }
                    if (parentFinal != null) {
                    }
                    if (!muted) {
                    }
                    videoFinal2 = videoFinal;
                    finalPath2 = finalPath;
                    groupId4 = groupId3;
                    a3 = a2;
                    count4 = count3;
                    if (!isEncrypted3) {
                    }
                    a = a3;
                    count2 = count4;
                    ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList922 = arrayList3;
                    isEncrypted2 = isEncrypted3;
                    final SendingMediaInfo sendingMediaInfo22 = info3;
                    final String finalPath322 = finalPath2;
                    long j22 = groupId4;
                    final TLRPC.TL_document videoFinal322 = videoFinal2;
                    groupId2 = j22;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda66
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.lambda$prepareSendingMedia$87(thumbFinal22, thumbKeyFinal22, editingMessageObject, accountInstance, videoEditedInfo422, videoFinal322, finalPath322, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, sendingMediaInfo22, notify, scheduleDate);
                        }
                    });
                    sendAsDocuments = arrayList7;
                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                    sendAsDocumentsUri = sendAsDocumentsUri2;
                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                    sendAsDocumentsEntities = arrayList922;
                    mediaCount2 = mediaCount;
                    lastGroupId3 = lastGroupId;
                } else {
                    info2 = info;
                    arrayList2 = arrayList;
                    isEncrypted2 = isEncrypted;
                    str3 = str6;
                    groupId2 = groupId3;
                    a = a2;
                    count2 = count3;
                }
                ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList10 = arrayList2;
                if (arrayList10 != null) {
                    sendAsDocumentsEntities = arrayList10;
                    sendAsDocuments = arrayList7;
                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                    sendAsDocumentsUri = sendAsDocumentsUri2;
                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                } else {
                    sendAsDocumentsEntities = new ArrayList<>();
                    sendAsDocumentsOriginal = new ArrayList<>();
                    sendAsDocumentsCaptions2 = new ArrayList<>();
                    sendAsDocuments = new ArrayList<>();
                    sendAsDocumentsUri = new ArrayList<>();
                }
                sendAsDocumentsEntities.add(info2.path);
                sendAsDocumentsOriginal.add(info2.path);
                sendAsDocumentsUri.add(info2.uri);
                sendAsDocumentsCaptions2.add(info2.caption);
                sendAsDocuments.add(info2.entities);
                mediaCount2 = mediaCount;
                lastGroupId3 = lastGroupId;
            } else {
                int a11 = a9;
                if (info6.searchImage.type == 1) {
                    final HashMap<String, String> params3 = new HashMap<>();
                    TLRPC.TL_document document6 = null;
                    if (info6.searchImage.document instanceof TLRPC.TL_document) {
                        document6 = (TLRPC.TL_document) info6.searchImage.document;
                        cacheFile = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(document6, true);
                    } else {
                        String md52 = Utilities.MD5(info6.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(info6.searchImage.imageUrl, "jpg");
                        cacheFile = new File(FileLoader.getDirectory(4), md52);
                    }
                    if (document6 != null) {
                        count8 = count9;
                        isEncrypted5 = isEncrypted;
                        hashMap = null;
                        cacheFile2 = cacheFile;
                    } else {
                        File thumbFile = null;
                        TLRPC.TL_document document7 = new TLRPC.TL_document();
                        count8 = count9;
                        document7.id = 0L;
                        document7.file_reference = new byte[0];
                        document7.date = accountInstance.getConnectionsManager().getCurrentTime();
                        TLRPC.TL_documentAttributeFilename fileName3 = new TLRPC.TL_documentAttributeFilename();
                        fileName3.file_name = "animation.gif";
                        document7.attributes.add(fileName3);
                        document7.size = info6.searchImage.size;
                        document7.dc_id = 0;
                        if (!forceDocument && cacheFile.toString().endsWith("mp4")) {
                            document7.mime_type = MimeTypes.VIDEO_MP4;
                            document7.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                        } else {
                            document7.mime_type = "image/gif";
                        }
                        if (cacheFile.exists()) {
                            thumbFile = cacheFile;
                        } else {
                            cacheFile = null;
                        }
                        if (thumbFile == null) {
                            String thumb4 = Utilities.MD5(info6.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(info6.searchImage.thumbUrl, "jpg");
                            thumbFile = new File(FileLoader.getDirectory(4), thumb4);
                            if (!thumbFile.exists()) {
                                thumbFile = null;
                            }
                        }
                        if (thumbFile == null) {
                            isEncrypted5 = isEncrypted;
                            hashMap = null;
                        } else {
                            try {
                                if (!isEncrypted) {
                                    try {
                                        if (info6.ttl == 0) {
                                            side = GroupCallActivity.TABLET_LIST_SIZE;
                                            if (!thumbFile.getAbsolutePath().endsWith("mp4")) {
                                                bitmap = createVideoThumbnail(thumbFile.getAbsolutePath(), 1);
                                                hashMap = null;
                                            } else {
                                                hashMap = null;
                                                try {
                                                    bitmap = ImageLoader.loadBitmap(thumbFile.getAbsolutePath(), null, side, side, true);
                                                } catch (Exception e11) {
                                                    e = e11;
                                                    isEncrypted5 = isEncrypted;
                                                    FileLog.e(e);
                                                    if (!document7.thumbs.isEmpty()) {
                                                    }
                                                    cacheFile2 = cacheFile;
                                                    document6 = document7;
                                                    final TLRPC.TL_document documentFinal = document6;
                                                    String str23 = info6.searchImage.imageUrl;
                                                    final String pathFinal = cacheFile2 != null ? info6.searchImage.imageUrl : cacheFile2.toString();
                                                    if (info6.searchImage.imageUrl != null) {
                                                    }
                                                    if (0 != 0) {
                                                    }
                                                    str3 = str17;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            SendMessagesHelper.lambda$prepareSendingMedia$85(MessageObject.this, accountInstance, documentFinal, pathFinal, params3, r6, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                                        }
                                                    });
                                                    isEncrypted2 = isEncrypted5;
                                                    mediaCount2 = mediaCount;
                                                    lastGroupId3 = lastGroupId;
                                                    groupId2 = groupId;
                                                    sendAsDocuments = arrayList7;
                                                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                                    sendAsDocumentsUri = sendAsDocumentsUri2;
                                                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                                    workers2 = workers4;
                                                    str4 = str2;
                                                    str5 = str;
                                                    a = a11;
                                                    count2 = count8;
                                                    sendAsDocumentsEntities = arrayList6;
                                                    a9 = a + 1;
                                                    arrayList5 = media;
                                                    workers = workers2;
                                                    str = str5;
                                                    isEncrypted6 = isEncrypted2;
                                                    str2 = str4;
                                                    groupId8 = groupId2;
                                                    str17 = str3;
                                                    count9 = count2;
                                                }
                                            }
                                            if (bitmap != null) {
                                                isEncrypted5 = isEncrypted;
                                            } else {
                                                isEncrypted5 = isEncrypted;
                                                try {
                                                    TLRPC.PhotoSize thumb5 = ImageLoader.scaleAndSaveImage(bitmap, side, side, side > 90 ? 80 : 55, isEncrypted5);
                                                    if (thumb5 != null) {
                                                        document7.thumbs.add(thumb5);
                                                        document7.flags |= 1;
                                                    }
                                                    bitmap.recycle();
                                                } catch (Exception e12) {
                                                    e = e12;
                                                    FileLog.e(e);
                                                    if (!document7.thumbs.isEmpty()) {
                                                    }
                                                    cacheFile2 = cacheFile;
                                                    document6 = document7;
                                                    final TLRPC.TL_document documentFinal2 = document6;
                                                    String str232 = info6.searchImage.imageUrl;
                                                    final String pathFinal2 = cacheFile2 != null ? info6.searchImage.imageUrl : cacheFile2.toString();
                                                    if (info6.searchImage.imageUrl != null) {
                                                    }
                                                    if (0 != 0) {
                                                    }
                                                    str3 = str17;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            SendMessagesHelper.lambda$prepareSendingMedia$85(MessageObject.this, accountInstance, documentFinal2, pathFinal2, params3, r6, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                                        }
                                                    });
                                                    isEncrypted2 = isEncrypted5;
                                                    mediaCount2 = mediaCount;
                                                    lastGroupId3 = lastGroupId;
                                                    groupId2 = groupId;
                                                    sendAsDocuments = arrayList7;
                                                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                                    sendAsDocumentsUri = sendAsDocumentsUri2;
                                                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                                    workers2 = workers4;
                                                    str4 = str2;
                                                    str5 = str;
                                                    a = a11;
                                                    count2 = count8;
                                                    sendAsDocumentsEntities = arrayList6;
                                                    a9 = a + 1;
                                                    arrayList5 = media;
                                                    workers = workers2;
                                                    str = str5;
                                                    isEncrypted6 = isEncrypted2;
                                                    str2 = str4;
                                                    groupId8 = groupId2;
                                                    str17 = str3;
                                                    count9 = count2;
                                                }
                                            }
                                        }
                                    } catch (Exception e13) {
                                        e = e13;
                                        isEncrypted5 = isEncrypted;
                                        hashMap = null;
                                        FileLog.e(e);
                                        if (!document7.thumbs.isEmpty()) {
                                        }
                                        cacheFile2 = cacheFile;
                                        document6 = document7;
                                        final TLRPC.TL_document documentFinal22 = document6;
                                        String str2322 = info6.searchImage.imageUrl;
                                        final String pathFinal22 = cacheFile2 != null ? info6.searchImage.imageUrl : cacheFile2.toString();
                                        if (info6.searchImage.imageUrl != null) {
                                        }
                                        if (0 != 0) {
                                        }
                                        str3 = str17;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingMedia$85(MessageObject.this, accountInstance, documentFinal22, pathFinal22, params3, r6, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                                            }
                                        });
                                        isEncrypted2 = isEncrypted5;
                                        mediaCount2 = mediaCount;
                                        lastGroupId3 = lastGroupId;
                                        groupId2 = groupId;
                                        sendAsDocuments = arrayList7;
                                        sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                                        sendAsDocumentsUri = sendAsDocumentsUri2;
                                        sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                                        workers2 = workers4;
                                        str4 = str2;
                                        str5 = str;
                                        a = a11;
                                        count2 = count8;
                                        sendAsDocumentsEntities = arrayList6;
                                        a9 = a + 1;
                                        arrayList5 = media;
                                        workers = workers2;
                                        str = str5;
                                        isEncrypted6 = isEncrypted2;
                                        str2 = str4;
                                        groupId8 = groupId2;
                                        str17 = str3;
                                        count9 = count2;
                                    }
                                }
                                if (!thumbFile.getAbsolutePath().endsWith("mp4")) {
                                }
                                if (bitmap != null) {
                                }
                            } catch (Exception e14) {
                                e = e14;
                                isEncrypted5 = isEncrypted;
                                hashMap = null;
                            }
                            side = 90;
                        }
                        if (!document7.thumbs.isEmpty()) {
                            TLRPC.TL_photoSize thumb6 = new TLRPC.TL_photoSize();
                            thumb6.w = info6.searchImage.width;
                            thumb6.h = info6.searchImage.height;
                            thumb6.size = 0;
                            thumb6.location = new TLRPC.TL_fileLocationUnavailable();
                            thumb6.type = "x";
                            document7.thumbs.add(thumb6);
                            document7.flags |= 1;
                        }
                        cacheFile2 = cacheFile;
                        document6 = document7;
                    }
                    final TLRPC.TL_document documentFinal222 = document6;
                    String str23222 = info6.searchImage.imageUrl;
                    final String pathFinal222 = cacheFile2 != null ? info6.searchImage.imageUrl : cacheFile2.toString();
                    if (info6.searchImage.imageUrl != null) {
                        params3.put("originalPath", info6.searchImage.imageUrl);
                    }
                    if (0 != 0) {
                        params3.put("parentObject", null);
                    }
                    str3 = str17;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.lambda$prepareSendingMedia$85(MessageObject.this, accountInstance, documentFinal222, pathFinal222, params3, r6, dialogId, replyToMsg, replyToTopMsg, info6, notify, scheduleDate);
                        }
                    });
                    isEncrypted2 = isEncrypted5;
                    mediaCount2 = mediaCount;
                    lastGroupId3 = lastGroupId;
                    groupId2 = groupId;
                    sendAsDocuments = arrayList7;
                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                    sendAsDocumentsUri = sendAsDocumentsUri2;
                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                    workers2 = workers4;
                    str4 = str2;
                    str5 = str;
                    a = a11;
                    count2 = count8;
                    sendAsDocumentsEntities = arrayList6;
                } else {
                    str3 = str17;
                    int count11 = count9;
                    String str24 = str2;
                    String str25 = str;
                    boolean needDownloadHttp = true;
                    TLRPC.TL_photo photo10 = null;
                    if (info6.searchImage.photo instanceof TLRPC.TL_photo) {
                        photo10 = (TLRPC.TL_photo) info6.searchImage.photo;
                    } else if (!isEncrypted) {
                        int i4 = info6.ttl;
                    }
                    if (photo10 != null) {
                        photo3 = photo10;
                    } else {
                        String md53 = Utilities.MD5(info6.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(info6.searchImage.imageUrl, "jpg");
                        File cacheFile5 = new File(FileLoader.getDirectory(4), md53);
                        if (cacheFile5.exists() && cacheFile5.length() != 0) {
                            TLRPC.TL_photo photo11 = accountInstance.getSendMessagesHelper().generatePhotoSizes(cacheFile5.toString(), null);
                            if (photo11 == null) {
                                photo4 = photo11;
                            } else {
                                needDownloadHttp = false;
                                photo4 = photo11;
                            }
                        } else {
                            photo4 = photo10;
                        }
                        if (photo4 == null) {
                            String md54 = Utilities.MD5(info6.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(info6.searchImage.thumbUrl, "jpg");
                            File cacheFile6 = new File(FileLoader.getDirectory(4), md54);
                            if (cacheFile6.exists()) {
                                photo4 = accountInstance.getSendMessagesHelper().generatePhotoSizes(cacheFile6.toString(), null);
                            }
                            if (photo4 == null) {
                                TLRPC.TL_photo photo12 = new TLRPC.TL_photo();
                                photo12.date = accountInstance.getConnectionsManager().getCurrentTime();
                                photo12.file_reference = new byte[0];
                                TLRPC.TL_photoSize photoSize = new TLRPC.TL_photoSize();
                                photoSize.w = info6.searchImage.width;
                                photoSize.h = info6.searchImage.height;
                                photoSize.size = 0;
                                photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                                photoSize.type = "x";
                                photo12.sizes.add(photoSize);
                                photo3 = photo12;
                            } else {
                                photo3 = photo4;
                            }
                        } else {
                            photo3 = photo4;
                        }
                    }
                    if (photo3 == null) {
                        isEncrypted4 = isEncrypted;
                        groupId6 = groupId;
                        a5 = a11;
                        count6 = count11;
                    } else {
                        final TLRPC.TL_photo photoFinal2 = photo3;
                        final boolean needDownloadHttpFinal = needDownloadHttp;
                        final HashMap<String, String> params4 = new HashMap<>();
                        if (info6.searchImage.imageUrl != null) {
                            params4.put("originalPath", info6.searchImage.imageUrl);
                        }
                        if (0 != 0) {
                            params4.put("parentObject", null);
                        }
                        if (!groupMediaFinal) {
                            groupId7 = groupId;
                            a6 = a11;
                            count7 = count11;
                        } else {
                            int mediaCount5 = mediaCount + 1;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("");
                            groupId7 = groupId;
                            sb7.append(groupId7);
                            params4.put("groupId", sb7.toString());
                            if (mediaCount5 != 10) {
                                count7 = count11;
                                a6 = a11;
                                if (a6 != count7 - 1) {
                                    mediaCount = mediaCount5;
                                }
                            } else {
                                a6 = a11;
                                count7 = count11;
                            }
                            params4.put("final", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
                            lastGroupId = 0;
                            mediaCount = mediaCount5;
                        }
                        groupId6 = groupId7;
                        a5 = a6;
                        count6 = count7;
                        isEncrypted4 = isEncrypted;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda7
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.lambda$prepareSendingMedia$86(MessageObject.this, accountInstance, photoFinal2, needDownloadHttpFinal, info6, params4, r7, dialogId, replyToMsg, replyToTopMsg, notify, scheduleDate);
                            }
                        });
                    }
                    mediaCount2 = mediaCount;
                    isEncrypted2 = isEncrypted4;
                    lastGroupId3 = lastGroupId;
                    sendAsDocuments = arrayList7;
                    sendAsDocumentsOriginal = sendAsDocumentsOriginal2;
                    sendAsDocumentsUri = sendAsDocumentsUri2;
                    sendAsDocumentsCaptions2 = sendAsDocumentsCaptions3;
                    workers2 = workers4;
                    str4 = str24;
                    str5 = str25;
                    sendAsDocumentsEntities = arrayList6;
                    groupId2 = groupId6;
                    a = a5;
                    count2 = count6;
                }
            }
            a9 = a + 1;
            arrayList5 = media;
            workers = workers2;
            str = str5;
            isEncrypted6 = isEncrypted2;
            str2 = str4;
            groupId8 = groupId2;
            str17 = str3;
            count9 = count2;
        }
        ArrayList<String> sendAsDocumentsCaptions4 = sendAsDocumentsCaptions2;
        ArrayList<Uri> sendAsDocumentsUri3 = sendAsDocumentsUri;
        ArrayList<String> sendAsDocumentsOriginal3 = sendAsDocumentsOriginal;
        ArrayList<String> arrayList11 = sendAsDocuments;
        ArrayList<ArrayList<TLRPC.MessageEntity>> arrayList12 = sendAsDocumentsEntities;
        int count12 = count9;
        if (lastGroupId == 0) {
            accountInstance2 = accountInstance;
            lastGroupId2 = lastGroupId;
        } else {
            accountInstance2 = accountInstance;
            lastGroupId2 = lastGroupId;
            finishGroup(accountInstance2, lastGroupId2, scheduleDate);
        }
        if (inputContent != null) {
            inputContent.releasePermission();
        }
        if (arrayList12 != null && !arrayList12.isEmpty()) {
            long[] groupId22 = new long[1];
            int documentsCount = arrayList12.size();
            int a12 = 0;
            while (a12 < documentsCount) {
                if (!forceDocument || isEncrypted) {
                    count = count12;
                } else {
                    count = count12;
                    if (count > 1 && mediaCount2 % 10 == 0) {
                        groupId22[0] = Utilities.random.nextLong();
                        mediaCount2 = 0;
                    }
                }
                mediaCount2++;
                ArrayList<String> sendAsDocumentsOriginal4 = sendAsDocumentsOriginal3;
                ArrayList<Uri> sendAsDocumentsUri4 = sendAsDocumentsUri3;
                ArrayList<String> sendAsDocumentsCaptions5 = sendAsDocumentsCaptions4;
                ArrayList<String> arrayList13 = arrayList11;
                long[] groupId23 = groupId22;
                int error = prepareSendingDocumentInternal(accountInstance, (String) arrayList12.get(a12), sendAsDocumentsOriginal4.get(a12), sendAsDocumentsUri4.get(a12), extension, dialogId, replyToMsg, replyToTopMsg, sendAsDocumentsCaptions5.get(a12), (ArrayList) arrayList13.get(a12), editingMessageObject, groupId23, mediaCount2 == 10 || a12 == documentsCount + (-1), forceDocument, notify, scheduleDate, null);
                handleError(error, accountInstance);
                a12++;
                accountInstance2 = accountInstance;
                arrayList11 = arrayList13;
                sendAsDocumentsCaptions4 = sendAsDocumentsCaptions5;
                sendAsDocumentsUri3 = sendAsDocumentsUri4;
                lastGroupId2 = lastGroupId2;
                sendAsDocumentsOriginal3 = sendAsDocumentsOriginal4;
                count12 = count;
                documentsCount = documentsCount;
                groupId22 = groupId23;
                arrayList12 = arrayList12;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("total send time = " + (System.currentTimeMillis() - beginTime));
        }
    }

    public static /* synthetic */ void lambda$prepareSendingMedia$84(MediaSendPrepareWorker worker, AccountInstance accountInstance, SendingMediaInfo info, boolean isEncrypted) {
        worker.photo = accountInstance.getSendMessagesHelper().generatePhotoSizes(info.path, info.uri);
        if (isEncrypted && info.canDeleteAfter) {
            new File(info.path).delete();
        }
        worker.sync.countDown();
    }

    public static /* synthetic */ void lambda$prepareSendingMedia$85(MessageObject editingMessageObject, AccountInstance accountInstance, TLRPC.TL_document documentFinal, String pathFinal, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, SendingMediaInfo info, boolean notify, int scheduleDate) {
        if (editingMessageObject == null) {
            accountInstance.getSendMessagesHelper().sendMessage(documentFinal, null, pathFinal, dialogId, replyToMsg, replyToTopMsg, info.caption, info.entities, null, params, notify, scheduleDate, 0, parentFinal, null);
        } else {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, null, null, documentFinal, pathFinal, params, false, parentFinal);
        }
    }

    public static /* synthetic */ void lambda$prepareSendingMedia$86(MessageObject editingMessageObject, AccountInstance accountInstance, TLRPC.TL_photo photoFinal, boolean needDownloadHttpFinal, SendingMediaInfo info, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, boolean notify, int scheduleDate) {
        String str = null;
        if (editingMessageObject != null) {
            SendMessagesHelper sendMessagesHelper = accountInstance.getSendMessagesHelper();
            if (needDownloadHttpFinal) {
                str = info.searchImage.imageUrl;
            }
            sendMessagesHelper.editMessage(editingMessageObject, photoFinal, null, null, str, params, false, parentFinal);
            return;
        }
        SendMessagesHelper sendMessagesHelper2 = accountInstance.getSendMessagesHelper();
        if (needDownloadHttpFinal) {
            str = info.searchImage.imageUrl;
        }
        sendMessagesHelper2.sendMessage(photoFinal, str, dialogId, replyToMsg, replyToTopMsg, info.caption, info.entities, null, params, notify, scheduleDate, info.ttl, parentFinal);
    }

    public static /* synthetic */ void lambda$prepareSendingMedia$87(Bitmap thumbFinal, String thumbKeyFinal, MessageObject editingMessageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document videoFinal, String finalPath, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, SendingMediaInfo info, boolean notify, int scheduleDate) {
        if (thumbFinal != null && thumbKeyFinal != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(thumbFinal), thumbKeyFinal, false);
        }
        if (editingMessageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, null, videoEditedInfo, videoFinal, finalPath, params, false, parentFinal);
        } else {
            accountInstance.getSendMessagesHelper().sendMessage(videoFinal, videoEditedInfo, finalPath, dialogId, replyToMsg, replyToTopMsg, info.caption, info.entities, null, params, notify, scheduleDate, info.ttl, parentFinal, null);
        }
    }

    public static /* synthetic */ void lambda$prepareSendingMedia$88(Bitmap[] bitmapFinal, String[] keyFinal, MessageObject editingMessageObject, AccountInstance accountInstance, TLRPC.TL_photo photoFinal, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, SendingMediaInfo info, boolean notify, int scheduleDate) {
        if (bitmapFinal[0] != null && keyFinal[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapFinal[0]), keyFinal[0], false);
        }
        if (editingMessageObject == null) {
            accountInstance.getSendMessagesHelper().sendMessage(photoFinal, null, dialogId, replyToMsg, replyToTopMsg, info.caption, info.entities, null, params, notify, scheduleDate, info.ttl, parentFinal);
        } else {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, photoFinal, null, null, null, params, false, parentFinal);
        }
    }

    private static void fillVideoAttribute(String videoPath, TLRPC.TL_documentAttributeVideo attributeVideo, VideoEditedInfo videoEditedInfo) {
        String rotation;
        boolean infoObtained = false;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            try {
                try {
                    MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
                    mediaMetadataRetriever2.setDataSource(videoPath);
                    String width = mediaMetadataRetriever2.extractMetadata(18);
                    if (width != null) {
                        attributeVideo.w = Integer.parseInt(width);
                    }
                    String height = mediaMetadataRetriever2.extractMetadata(19);
                    if (height != null) {
                        attributeVideo.h = Integer.parseInt(height);
                    }
                    String duration = mediaMetadataRetriever2.extractMetadata(9);
                    if (duration != null) {
                        attributeVideo.duration = (int) Math.ceil(((float) Long.parseLong(duration)) / 1000.0f);
                    }
                    if (Build.VERSION.SDK_INT >= 17 && (rotation = mediaMetadataRetriever2.extractMetadata(24)) != null) {
                        int val = Utilities.parseInt((CharSequence) rotation).intValue();
                        if (videoEditedInfo != null) {
                            videoEditedInfo.rotationValue = val;
                        } else if (val == 90 || val == 270) {
                            int temp = attributeVideo.w;
                            attributeVideo.w = attributeVideo.h;
                            attributeVideo.h = temp;
                        }
                    }
                    infoObtained = true;
                    mediaMetadataRetriever2.release();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } catch (Exception e2) {
                FileLog.e(e2);
                if (0 != 0) {
                    mediaMetadataRetriever.release();
                }
            }
            if (!infoObtained) {
                try {
                    MediaPlayer mp = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(videoPath)));
                    if (mp != null) {
                        attributeVideo.duration = (int) Math.ceil(mp.getDuration() / 1000.0f);
                        attributeVideo.w = mp.getVideoWidth();
                        attributeVideo.h = mp.getVideoHeight();
                        mp.release();
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    mediaMetadataRetriever.release();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            throw th;
        }
    }

    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        float size;
        if (kind == 2) {
            size = 1920.0f;
        } else if (kind == 3) {
            size = 96.0f;
        } else {
            size = 512.0f;
        }
        Bitmap bitmap = createVideoThumbnailAtTime(filePath, 0L);
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (w > size || h > size) {
                float scale = Math.max(w, h) / size;
                return Bitmap.createScaledBitmap(bitmap, (int) (w / scale), (int) (h / scale), true);
            }
            return bitmap;
        }
        return bitmap;
    }

    public static Bitmap createVideoThumbnailAtTime(String filePath, long time) {
        return createVideoThumbnailAtTime(filePath, time, null, false);
    }

    public static Bitmap createVideoThumbnailAtTime(String filePath, long time, int[] orientation, boolean precise) {
        Bitmap bitmap = null;
        if (precise) {
            AnimatedFileDrawable fileDrawable = new AnimatedFileDrawable(new File(filePath), true, 0L, null, null, null, 0L, 0, true);
            bitmap = fileDrawable.getFrameAtTime(time, precise);
            if (orientation != null) {
                orientation[0] = fileDrawable.getOrientation();
            }
            fileDrawable.recycle();
            if (bitmap == null) {
                return createVideoThumbnailAtTime(filePath, time, orientation, false);
            }
        } else {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                try {
                    retriever.setDataSource(filePath);
                    bitmap = retriever.getFrameAtTime(time, 1);
                    if (bitmap == null) {
                        bitmap = retriever.getFrameAtTime(time, 3);
                    }
                    retriever.release();
                } catch (RuntimeException e) {
                }
            } catch (Exception e2) {
                retriever.release();
            } catch (Throwable th) {
                try {
                    retriever.release();
                } catch (RuntimeException e3) {
                }
                throw th;
            }
        }
        return bitmap;
    }

    private static VideoEditedInfo createCompressionSettings(String videoPath) {
        int compressionsCount;
        float maxSize;
        int[] params = new int[11];
        AnimatedFileDrawable.getVideoInfo(videoPath, params);
        if (params[0] == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("video hasn't avc1 atom");
            }
            return null;
        }
        int originalBitrate = MediaController.getVideoBitrate(videoPath);
        if (originalBitrate == -1) {
            originalBitrate = params[3];
        }
        int bitrate = originalBitrate;
        float videoDuration = params[4];
        long j = params[6];
        long audioFramesSize = params[5];
        int videoFramerate = params[7];
        if (Build.VERSION.SDK_INT < 18) {
            try {
                MediaCodecInfo codecInfo = MediaController.selectCodec("video/avc");
                if (codecInfo == null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("no codec info for video/avc");
                    }
                    return null;
                }
                String name = codecInfo.getName();
                if (!name.equals("OMX.google.h264.encoder") && !name.equals("OMX.ST.VFM.H264Enc") && !name.equals("OMX.Exynos.avc.enc") && !name.equals("OMX.MARVELL.VIDEO.HW.CODA7542ENCODER") && !name.equals("OMX.MARVELL.VIDEO.H264ENCODER") && !name.equals("OMX.k3.video.encoder.avc") && !name.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                    if (MediaController.selectColorFormat(codecInfo, "video/avc") == 0) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("no color format for video/avc");
                        }
                        return null;
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("unsupported encoder = " + name);
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
        videoEditedInfo.startTime = -1L;
        videoEditedInfo.endTime = -1L;
        videoEditedInfo.bitrate = bitrate;
        videoEditedInfo.originalPath = videoPath;
        videoEditedInfo.framerate = videoFramerate;
        videoEditedInfo.estimatedDuration = (long) Math.ceil(videoDuration);
        int i = params[1];
        videoEditedInfo.originalWidth = i;
        videoEditedInfo.resultWidth = i;
        int i2 = params[2];
        videoEditedInfo.originalHeight = i2;
        videoEditedInfo.resultHeight = i2;
        videoEditedInfo.rotationValue = params[8];
        videoEditedInfo.originalDuration = videoDuration * 1000.0f;
        float maxSize2 = Math.max(videoEditedInfo.originalWidth, videoEditedInfo.originalHeight);
        if (maxSize2 > 1280.0f) {
            compressionsCount = 4;
        } else if (maxSize2 > 854.0f) {
            compressionsCount = 3;
        } else if (maxSize2 > 640.0f) {
            compressionsCount = 2;
        } else {
            compressionsCount = 1;
        }
        int selectedCompression = Math.round(DownloadController.getInstance(UserConfig.selectedAccount).getMaxVideoBitrate() / (100.0f / compressionsCount));
        if (selectedCompression > compressionsCount) {
            selectedCompression = compressionsCount;
        }
        boolean needCompress = false;
        if (selectedCompression != compressionsCount || Math.max(videoEditedInfo.originalWidth, videoEditedInfo.originalHeight) > 1280) {
            needCompress = true;
            switch (selectedCompression) {
                case 1:
                    maxSize = 432.0f;
                    break;
                case 2:
                    maxSize = 640.0f;
                    break;
                case 3:
                    maxSize = 848.0f;
                    break;
                default:
                    maxSize = 1280.0f;
                    break;
            }
            float scale = maxSize / (videoEditedInfo.originalWidth > videoEditedInfo.originalHeight ? videoEditedInfo.originalWidth : videoEditedInfo.originalHeight);
            videoEditedInfo.resultWidth = Math.round((videoEditedInfo.originalWidth * scale) / 2.0f) * 2;
            videoEditedInfo.resultHeight = Math.round((videoEditedInfo.originalHeight * scale) / 2.0f) * 2;
        }
        int i3 = videoEditedInfo.originalHeight;
        int i4 = videoEditedInfo.originalWidth;
        int i5 = videoEditedInfo.resultHeight;
        int bitrate2 = videoEditedInfo.resultWidth;
        int bitrate3 = MediaController.makeVideoBitrate(i3, i4, originalBitrate, i5, bitrate2);
        if (!needCompress) {
            videoEditedInfo.resultWidth = videoEditedInfo.originalWidth;
            videoEditedInfo.resultHeight = videoEditedInfo.originalHeight;
            videoEditedInfo.bitrate = bitrate3;
        } else {
            videoEditedInfo.bitrate = bitrate3;
        }
        videoEditedInfo.estimatedSize = (int) (((float) audioFramesSize) + (((videoDuration / 1000.0f) * bitrate3) / 8.0f));
        if (videoEditedInfo.estimatedSize == 0) {
            videoEditedInfo.estimatedSize = 1L;
        }
        return videoEditedInfo;
    }

    public static void prepareSendingVideo(final AccountInstance accountInstance, final String videoPath, final VideoEditedInfo info, final long dialogId, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final CharSequence caption, final ArrayList<TLRPC.MessageEntity> entities, final int ttl, final MessageObject editingMessageObject, final boolean notify, final int scheduleDate, final boolean forceDocument) {
        if (videoPath == null || videoPath.length() == 0) {
            return;
        }
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingVideo$91(VideoEditedInfo.this, videoPath, dialogId, ttl, accountInstance, caption, editingMessageObject, replyToMsg, replyToTopMsg, entities, notify, scheduleDate, forceDocument);
            }
        }).start();
    }

    /* JADX WARN: Removed duplicated region for block: B:133:0x036e  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x03cd  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0154  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingVideo$91(VideoEditedInfo info, String videoPath, final long dialogId, final int ttl, final AccountInstance accountInstance, CharSequence caption, final MessageObject editingMessageObject, final MessageObject replyToMsg, final MessageObject replyToTopMsg, final ArrayList entities, final boolean notify, final int scheduleDate, boolean forceDocument) {
        VideoEditedInfo videoEditedInfo;
        boolean isEncrypted;
        boolean isRound;
        boolean isRound2;
        String originalPath;
        long startTime;
        long startTime2;
        String parentObject;
        boolean isEncrypted2;
        TLRPC.TL_document document;
        String thumbKey;
        Bitmap thumb;
        String path;
        final String parentFinal;
        TLRPC.TL_documentAttributeVideo attributeVideo;
        Bitmap thumb2;
        int h;
        int w;
        VideoEditedInfo videoEditedInfo2 = info != null ? info : createCompressionSettings(videoPath);
        boolean isEncrypted3 = DialogObject.isEncryptedDialog(dialogId);
        boolean isRound3 = videoEditedInfo2 != null && videoEditedInfo2.roundVideo;
        String thumbKey2 = null;
        if (videoEditedInfo2 != null || videoPath.endsWith("mp4")) {
            isRound = isRound3;
            isEncrypted = isEncrypted3;
            videoEditedInfo = videoEditedInfo2;
        } else if (!isRound3) {
            prepareSendingDocumentInternal(accountInstance, videoPath, videoPath, null, null, dialogId, replyToMsg, replyToTopMsg, caption, entities, editingMessageObject, null, false, forceDocument, notify, scheduleDate, null);
            return;
        } else {
            isRound = isRound3;
            isEncrypted = isEncrypted3;
            videoEditedInfo = videoEditedInfo2;
        }
        File temp = new File(videoPath);
        String originalPath2 = videoPath + temp.length() + "_" + temp.lastModified();
        final VideoEditedInfo videoEditedInfo3 = videoEditedInfo;
        if (videoEditedInfo3 == null) {
            isRound2 = isRound;
            originalPath = originalPath2;
            startTime = 0;
        } else {
            isRound2 = isRound;
            if (!isRound2) {
                StringBuilder sb = new StringBuilder();
                sb.append(originalPath2);
                sb.append(videoEditedInfo3.estimatedDuration);
                sb.append("_");
                sb.append(videoEditedInfo3.startTime);
                sb.append("_");
                sb.append(videoEditedInfo3.endTime);
                sb.append(videoEditedInfo3.muted ? "_m" : "");
                originalPath2 = sb.toString();
                if (videoEditedInfo3.resultWidth != videoEditedInfo3.originalWidth) {
                    originalPath2 = originalPath2 + "_" + videoEditedInfo3.resultWidth;
                }
            }
            startTime = 0;
            if (videoEditedInfo3.startTime >= 0) {
                startTime = videoEditedInfo3.startTime;
            }
            originalPath = originalPath2;
        }
        TLRPC.TL_document document2 = null;
        boolean isEncrypted4 = isEncrypted;
        if (isEncrypted4 || ttl != 0) {
            isEncrypted2 = isEncrypted4;
            startTime2 = startTime;
        } else if (videoEditedInfo3 == null || (videoEditedInfo3.filterState == null && videoEditedInfo3.paintPath == null && videoEditedInfo3.mediaEntities == null && videoEditedInfo3.cropState == null)) {
            Object[] sentData = accountInstance.getMessagesStorage().getSentFile(originalPath, !isEncrypted4 ? 2 : 5);
            if (sentData == null || !(sentData[0] instanceof TLRPC.TL_document)) {
                isEncrypted2 = isEncrypted4;
                startTime2 = startTime;
            } else {
                TLRPC.TL_document document3 = (TLRPC.TL_document) sentData[0];
                parentObject = (String) sentData[1];
                isEncrypted2 = isEncrypted4;
                startTime2 = startTime;
                ensureMediaThumbExists(accountInstance, isEncrypted4, document3, videoPath, null, startTime);
                document2 = document3;
                if (document2 == null) {
                    document = document2;
                    thumbKey = null;
                    thumb = null;
                } else {
                    Bitmap thumb3 = createVideoThumbnailAtTime(videoPath, startTime2);
                    if (thumb3 == null) {
                        thumb3 = createVideoThumbnail(videoPath, 1);
                    }
                    int side = (isEncrypted2 || ttl != 0) ? 90 : GroupCallActivity.TABLET_LIST_SIZE;
                    TLRPC.PhotoSize size = ImageLoader.scaleAndSaveImage(thumb3, side, side, side > 90 ? 80 : 55, isEncrypted2);
                    if (thumb3 != null && size != null) {
                        if (!isRound2) {
                            thumb3 = null;
                        } else if (isEncrypted2) {
                            Bitmap thumb4 = Bitmap.createScaledBitmap(thumb3, 90, 90, true);
                            Utilities.blurBitmap(thumb4, 7, Build.VERSION.SDK_INT < 21 ? 0 : 1, thumb4.getWidth(), thumb4.getHeight(), thumb4.getRowBytes());
                            Utilities.blurBitmap(thumb4, 7, Build.VERSION.SDK_INT < 21 ? 0 : 1, thumb4.getWidth(), thumb4.getHeight(), thumb4.getRowBytes());
                            Utilities.blurBitmap(thumb4, 7, Build.VERSION.SDK_INT < 21 ? 0 : 1, thumb4.getWidth(), thumb4.getHeight(), thumb4.getRowBytes());
                            thumbKey2 = String.format(size.location.volume_id + "_" + size.location.local_id + "@%d_%d_b2", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                            thumb3 = thumb4;
                        } else {
                            Utilities.blurBitmap(thumb3, 3, Build.VERSION.SDK_INT < 21 ? 0 : 1, thumb3.getWidth(), thumb3.getHeight(), thumb3.getRowBytes());
                            thumbKey2 = String.format(size.location.volume_id + "_" + size.location.local_id + "@%d_%d_b", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                        }
                    }
                    TLRPC.TL_document document4 = new TLRPC.TL_document();
                    if (size != null) {
                        document4.thumbs.add(size);
                        document4.flags |= 1;
                    }
                    document4.file_reference = new byte[0];
                    document4.mime_type = MimeTypes.VIDEO_MP4;
                    accountInstance.getUserConfig().saveConfig(false);
                    if (isEncrypted2) {
                        int encryptedChatId = DialogObject.getEncryptedChatId(dialogId);
                        TLRPC.EncryptedChat encryptedChat = accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            return;
                        }
                        attributeVideo = new TLRPC.TL_documentAttributeVideo();
                    } else {
                        attributeVideo = new TLRPC.TL_documentAttributeVideo();
                        attributeVideo.supports_streaming = true;
                    }
                    attributeVideo.round_message = isRound2;
                    document4.attributes.add(attributeVideo);
                    if (videoEditedInfo3 == null || !videoEditedInfo3.needConvert()) {
                        thumb2 = thumb3;
                        if (temp.exists()) {
                            document4.size = (int) temp.length();
                        }
                        fillVideoAttribute(videoPath, attributeVideo, null);
                    } else {
                        if (videoEditedInfo3.muted) {
                            document4.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                            fillVideoAttribute(videoPath, attributeVideo, videoEditedInfo3);
                            videoEditedInfo3.originalWidth = attributeVideo.w;
                            videoEditedInfo3.originalHeight = attributeVideo.h;
                            thumb2 = thumb3;
                        } else {
                            thumb2 = thumb3;
                            attributeVideo.duration = (int) (videoEditedInfo3.estimatedDuration / 1000);
                        }
                        int rotation = videoEditedInfo3.rotationValue;
                        if (videoEditedInfo3.cropState != null) {
                            w = videoEditedInfo3.cropState.transformWidth;
                            h = videoEditedInfo3.cropState.transformHeight;
                            rotation += videoEditedInfo3.cropState.transformRotation;
                        } else {
                            w = videoEditedInfo3.resultWidth;
                            h = videoEditedInfo3.resultHeight;
                        }
                        if (rotation == 90 || rotation == 270) {
                            attributeVideo.w = h;
                            attributeVideo.h = w;
                        } else {
                            attributeVideo.w = w;
                            attributeVideo.h = h;
                        }
                        document4.size = (int) videoEditedInfo3.estimatedSize;
                    }
                    document = document4;
                    thumbKey = thumbKey2;
                    thumb = thumb2;
                }
                if (videoEditedInfo3 == null && videoEditedInfo3.needConvert()) {
                    String fileName = "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4";
                    File cacheFile = new File(FileLoader.getDirectory(4), fileName);
                    SharedConfig.saveConfig();
                    String path2 = cacheFile.getAbsolutePath();
                    path = path2;
                } else {
                    path = videoPath;
                }
                final TLRPC.TL_document videoFinal = document;
                parentFinal = parentObject;
                final String finalPath = path;
                final HashMap<String, String> params = new HashMap<>();
                final Bitmap thumbFinal = thumb;
                final String thumbKeyFinal = thumbKey;
                final String captionFinal = caption == null ? caption.toString() : "";
                if (originalPath != null) {
                    params.put("originalPath", originalPath);
                }
                if (parentFinal != null) {
                    params.put("parentObject", parentFinal);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda55
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.lambda$prepareSendingVideo$90(thumbFinal, thumbKeyFinal, editingMessageObject, accountInstance, videoEditedInfo3, videoFinal, finalPath, params, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate, ttl);
                    }
                });
            }
        } else {
            isEncrypted2 = isEncrypted4;
            startTime2 = startTime;
        }
        parentObject = null;
        if (document2 == null) {
        }
        if (videoEditedInfo3 == null) {
        }
        path = videoPath;
        final TLRPC.TL_document videoFinal2 = document;
        parentFinal = parentObject;
        final String finalPath2 = path;
        final HashMap params2 = new HashMap<>();
        final Bitmap thumbFinal2 = thumb;
        final String thumbKeyFinal2 = thumbKey;
        if (caption == null) {
        }
        if (originalPath != null) {
        }
        if (parentFinal != null) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingVideo$90(thumbFinal2, thumbKeyFinal2, editingMessageObject, accountInstance, videoEditedInfo3, videoFinal2, finalPath2, params2, parentFinal, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, notify, scheduleDate, ttl);
            }
        });
    }

    public static /* synthetic */ void lambda$prepareSendingVideo$90(Bitmap thumbFinal, String thumbKeyFinal, MessageObject editingMessageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document videoFinal, String finalPath, HashMap params, String parentFinal, long dialogId, MessageObject replyToMsg, MessageObject replyToTopMsg, String captionFinal, ArrayList entities, boolean notify, int scheduleDate, int ttl) {
        if (thumbFinal != null && thumbKeyFinal != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(thumbFinal), thumbKeyFinal, false);
        }
        if (editingMessageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(editingMessageObject, null, videoEditedInfo, videoFinal, finalPath, params, false, parentFinal);
        } else {
            accountInstance.getSendMessagesHelper().sendMessage(videoFinal, videoEditedInfo, finalPath, dialogId, replyToMsg, replyToTopMsg, captionFinal, entities, null, params, notify, scheduleDate, ttl, parentFinal, null);
        }
    }
}
