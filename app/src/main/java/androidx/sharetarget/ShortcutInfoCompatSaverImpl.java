package androidx.sharetarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutInfoCompatSaver;
import androidx.core.graphics.drawable.IconCompat;
import androidx.sharetarget.ShortcutsInfoSerialization;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class ShortcutInfoCompatSaverImpl extends ShortcutInfoCompatSaver<ListenableFuture<Void>> {
    private static final Object GET_INSTANCE_LOCK = new Object();
    private static volatile ShortcutInfoCompatSaverImpl sInstance;
    final File mBitmapsDir;
    final ExecutorService mCacheUpdateService;
    final Context mContext;
    private final ExecutorService mDiskIoService;
    final File mTargetsXmlFile;
    final Map<String, ShortcutsInfoSerialization.ShortcutContainer> mShortcutsMap = new ArrayMap();
    final Map<String, ListenableFuture<?>> mScheduledBitmapTasks = new ArrayMap();

    public static ShortcutInfoCompatSaverImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GET_INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new ShortcutInfoCompatSaverImpl(context, createExecutorService(), createExecutorService());
                }
            }
        }
        return sInstance;
    }

    static ExecutorService createExecutorService() {
        return new ThreadPoolExecutor(0, 1, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    ShortcutInfoCompatSaverImpl(Context context, ExecutorService cacheUpdateService, ExecutorService diskIoService) {
        this.mContext = context.getApplicationContext();
        this.mCacheUpdateService = cacheUpdateService;
        this.mDiskIoService = diskIoService;
        File file = new File(context.getFilesDir(), "ShortcutInfoCompatSaver_share_targets");
        this.mBitmapsDir = new File(file, "ShortcutInfoCompatSaver_share_targets_bitmaps");
        this.mTargetsXmlFile = new File(file, "targets.xml");
        cacheUpdateService.submit(new AnonymousClass1(file));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ File val$workingDirectory;

        AnonymousClass1(final File val$workingDirectory) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$workingDirectory = val$workingDirectory;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ShortcutInfoCompatSaverImpl.ensureDir(this.val$workingDirectory);
                ShortcutInfoCompatSaverImpl.ensureDir(ShortcutInfoCompatSaverImpl.this.mBitmapsDir);
                ShortcutInfoCompatSaverImpl shortcutInfoCompatSaverImpl = ShortcutInfoCompatSaverImpl.this;
                shortcutInfoCompatSaverImpl.mShortcutsMap.putAll(ShortcutsInfoSerialization.loadFromXml(shortcutInfoCompatSaverImpl.mTargetsXmlFile, shortcutInfoCompatSaverImpl.mContext));
                ShortcutInfoCompatSaverImpl.this.deleteDanglingBitmaps(new ArrayList(ShortcutInfoCompatSaverImpl.this.mShortcutsMap.values()));
            } catch (Exception e) {
                Log.w("ShortcutInfoCompatSaver", "ShortcutInfoCompatSaver started with an exceptions ", e);
            }
        }
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture<Void> removeShortcuts(List<String> shortcutIds) {
        ArrayList arrayList = new ArrayList(shortcutIds);
        ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new AnonymousClass2(arrayList, create));
        return create;
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ List val$idList;
        final /* synthetic */ ResolvableFuture val$result;

        AnonymousClass2(final List val$idList, final ResolvableFuture val$result) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$idList = val$idList;
            this.val$result = val$result;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (String str : this.val$idList) {
                ShortcutInfoCompatSaverImpl.this.mShortcutsMap.remove(str);
                ListenableFuture<?> remove = ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.remove(str);
                if (remove != null) {
                    remove.cancel(false);
                }
            }
            ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(this.val$result);
        }
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$3 */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ ResolvableFuture val$result;

        AnonymousClass3(final ResolvableFuture val$result) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$result = val$result;
        }

        @Override // java.lang.Runnable
        public void run() {
            ShortcutInfoCompatSaverImpl.this.mShortcutsMap.clear();
            for (ListenableFuture<?> listenableFuture : ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.values()) {
                listenableFuture.cancel(false);
            }
            ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.clear();
            ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(this.val$result);
        }
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture<Void> removeAllShortcuts() {
        ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new AnonymousClass3(create));
        return create;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$4 */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements Callable<ArrayList<ShortcutInfoCompat>> {
        AnonymousClass4() {
            ShortcutInfoCompatSaverImpl.this = this$0;
        }

        @Override // java.util.concurrent.Callable
        public ArrayList<ShortcutInfoCompat> call() {
            ArrayList<ShortcutInfoCompat> arrayList = new ArrayList<>();
            for (ShortcutsInfoSerialization.ShortcutContainer shortcutContainer : ShortcutInfoCompatSaverImpl.this.mShortcutsMap.values()) {
                arrayList.add(new ShortcutInfoCompat.Builder(shortcutContainer.mShortcutInfo).build());
            }
            return arrayList;
        }
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return (List) this.mCacheUpdateService.submit(new AnonymousClass4()).get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements Callable<ShortcutsInfoSerialization.ShortcutContainer> {
        final /* synthetic */ String val$shortcutId;

        AnonymousClass5(final String val$shortcutId) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$shortcutId = val$shortcutId;
        }

        @Override // java.util.concurrent.Callable
        public ShortcutsInfoSerialization.ShortcutContainer call() {
            return ShortcutInfoCompatSaverImpl.this.mShortcutsMap.get(this.val$shortcutId);
        }
    }

    public IconCompat getShortcutIcon(final String shortcutId) throws Exception {
        Bitmap bitmap;
        ShortcutsInfoSerialization.ShortcutContainer shortcutContainer = (ShortcutsInfoSerialization.ShortcutContainer) this.mCacheUpdateService.submit(new AnonymousClass5(shortcutId)).get();
        if (shortcutContainer == null) {
            return null;
        }
        if (!TextUtils.isEmpty(shortcutContainer.mResourceName)) {
            int i = 0;
            try {
                i = this.mContext.getResources().getIdentifier(shortcutContainer.mResourceName, null, null);
            } catch (Exception unused) {
            }
            if (i != 0) {
                return IconCompat.createWithResource(this.mContext, i);
            }
        }
        if (!TextUtils.isEmpty(shortcutContainer.mBitmapPath) && (bitmap = (Bitmap) this.mDiskIoService.submit(new AnonymousClass6(this, shortcutContainer)).get()) != null) {
            return IconCompat.createWithBitmap(bitmap);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$6 */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements Callable<Bitmap> {
        final /* synthetic */ ShortcutsInfoSerialization.ShortcutContainer val$container;

        AnonymousClass6(final ShortcutInfoCompatSaverImpl this$0, final ShortcutsInfoSerialization.ShortcutContainer val$container) {
            this.val$container = val$container;
        }

        @Override // java.util.concurrent.Callable
        public Bitmap call() {
            return BitmapFactory.decodeFile(this.val$container.mBitmapPath);
        }
    }

    void deleteDanglingBitmaps(List<ShortcutsInfoSerialization.ShortcutContainer> shortcutsList) {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        for (ShortcutsInfoSerialization.ShortcutContainer shortcutContainer : shortcutsList) {
            if (!TextUtils.isEmpty(shortcutContainer.mBitmapPath)) {
                arrayList.add(shortcutContainer.mBitmapPath);
            }
        }
        for (File file : this.mBitmapsDir.listFiles()) {
            if (!arrayList.contains(file.getAbsolutePath())) {
                file.delete();
            }
        }
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture<Void> addShortcuts(List<ShortcutInfoCompat> shortcuts) {
        ArrayList arrayList = new ArrayList(shortcuts.size());
        for (ShortcutInfoCompat shortcutInfoCompat : shortcuts) {
            arrayList.add(new ShortcutInfoCompat.Builder(shortcutInfoCompat).build());
        }
        ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new AnonymousClass7(arrayList, create));
        return create;
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$7 */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements Runnable {
        final /* synthetic */ List val$copy;
        final /* synthetic */ ResolvableFuture val$result;

        AnonymousClass7(final List val$copy, final ResolvableFuture val$result) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$copy = val$copy;
            this.val$result = val$result;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (ShortcutInfoCompat shortcutInfoCompat : this.val$copy) {
                Set<String> categories = shortcutInfoCompat.getCategories();
                if (categories != null && !categories.isEmpty()) {
                    ShortcutsInfoSerialization.ShortcutContainer containerFrom = ShortcutInfoCompatSaverImpl.this.containerFrom(shortcutInfoCompat);
                    Bitmap bitmap = containerFrom.mBitmapPath != null ? shortcutInfoCompat.getIcon().getBitmap() : null;
                    String id = shortcutInfoCompat.getId();
                    ShortcutInfoCompatSaverImpl.this.mShortcutsMap.put(id, containerFrom);
                    if (bitmap != null) {
                        ListenableFuture<Void> scheduleBitmapSaving = ShortcutInfoCompatSaverImpl.this.scheduleBitmapSaving(bitmap, containerFrom.mBitmapPath);
                        ListenableFuture<?> put = ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.put(id, scheduleBitmapSaving);
                        if (put != null) {
                            put.cancel(false);
                        }
                        scheduleBitmapSaving.addListener(new AnonymousClass1(id, scheduleBitmapSaving), ShortcutInfoCompatSaverImpl.this.mCacheUpdateService);
                    }
                }
            }
            ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(this.val$result);
        }

        /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$7$1 */
        /* loaded from: classes.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ ListenableFuture val$future;
            final /* synthetic */ String val$id;

            AnonymousClass1(final String val$id, final ListenableFuture val$future) {
                AnonymousClass7.this = this$1;
                this.val$id = val$id;
                this.val$future = val$future;
            }

            @Override // java.lang.Runnable
            public void run() {
                ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.remove(this.val$id);
                if (this.val$future.isCancelled()) {
                    return;
                }
                try {
                    this.val$future.get();
                } catch (Exception e) {
                    AnonymousClass7.this.val$result.setException(e);
                }
            }
        }
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$8 */
    /* loaded from: classes.dex */
    public class AnonymousClass8 implements Runnable {
        final /* synthetic */ Bitmap val$bitmap;
        final /* synthetic */ String val$path;

        AnonymousClass8(final Bitmap val$bitmap, final String val$path) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$bitmap = val$bitmap;
            this.val$path = val$path;
        }

        @Override // java.lang.Runnable
        public void run() {
            ShortcutInfoCompatSaverImpl.this.saveBitmap(this.val$bitmap, this.val$path);
        }
    }

    ListenableFuture<Void> scheduleBitmapSaving(final Bitmap bitmap, final String path) {
        return submitDiskOperation(new AnonymousClass8(bitmap, path));
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$9 */
    /* loaded from: classes.dex */
    public class AnonymousClass9 implements Runnable {
        final /* synthetic */ ResolvableFuture val$result;
        final /* synthetic */ Runnable val$runnable;

        AnonymousClass9(final ShortcutInfoCompatSaverImpl this$0, final ResolvableFuture val$result, final Runnable val$runnable) {
            this.val$result = val$result;
            this.val$runnable = val$runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.val$result.isCancelled()) {
                return;
            }
            try {
                this.val$runnable.run();
                this.val$result.set(null);
            } catch (Exception e) {
                this.val$result.setException(e);
            }
        }
    }

    private ListenableFuture<Void> submitDiskOperation(final Runnable runnable) {
        ResolvableFuture create = ResolvableFuture.create();
        this.mDiskIoService.submit(new AnonymousClass9(this, create, runnable));
        return create;
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$10 */
    /* loaded from: classes.dex */
    public class AnonymousClass10 implements Runnable {
        final /* synthetic */ List val$containers;

        AnonymousClass10(final List val$containers) {
            ShortcutInfoCompatSaverImpl.this = this$0;
            this.val$containers = val$containers;
        }

        @Override // java.lang.Runnable
        public void run() {
            ShortcutInfoCompatSaverImpl.this.deleteDanglingBitmaps(this.val$containers);
            ShortcutsInfoSerialization.saveAsXml(this.val$containers, ShortcutInfoCompatSaverImpl.this.mTargetsXmlFile);
        }
    }

    void scheduleSyncCurrentState(final ResolvableFuture<Void> output) {
        ListenableFuture<Void> submitDiskOperation = submitDiskOperation(new AnonymousClass10(new ArrayList(this.mShortcutsMap.values())));
        submitDiskOperation.addListener(new AnonymousClass11(this, submitDiskOperation, output), this.mCacheUpdateService);
    }

    /* renamed from: androidx.sharetarget.ShortcutInfoCompatSaverImpl$11 */
    /* loaded from: classes.dex */
    public class AnonymousClass11 implements Runnable {
        final /* synthetic */ ListenableFuture val$future;
        final /* synthetic */ ResolvableFuture val$output;

        AnonymousClass11(final ShortcutInfoCompatSaverImpl this$0, final ListenableFuture val$future, final ResolvableFuture val$output) {
            this.val$future = val$future;
            this.val$output = val$output;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.val$future.get();
                this.val$output.set(null);
            } catch (Exception e) {
                this.val$output.setException(e);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0012, code lost:
        if (r2 != 5) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    ShortcutsInfoSerialization.ShortcutContainer containerFrom(ShortcutInfoCompat shortcut) {
        String str;
        String str2;
        IconCompat icon = shortcut.getIcon();
        if (icon != null) {
            int type = icon.getType();
            if (type != 1) {
                if (type == 2) {
                    str2 = this.mContext.getResources().getResourceName(icon.getResId());
                    str = null;
                    return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcut).setIcon(null).build(), str2, str);
                }
            }
            str = new File(this.mBitmapsDir, UUID.randomUUID().toString()).getAbsolutePath();
            str2 = null;
            return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcut).setIcon(null).build(), str2, str);
        }
        str2 = null;
        str = null;
        return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcut).setIcon(null).build(), str2, str);
    }

    void saveBitmap(Bitmap bitmap, String path) {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap is null");
        }
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path is empty");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                Log.wtf("ShortcutInfoCompatSaver", "Unable to compress bitmap");
                throw new RuntimeException("Unable to compress bitmap for saving " + path);
            }
            fileOutputStream.close();
        } catch (IOException | OutOfMemoryError | RuntimeException e) {
            Log.wtf("ShortcutInfoCompatSaver", "Unable to write bitmap to file", e);
            throw new RuntimeException("Unable to write bitmap to file " + path, e);
        }
    }

    static boolean ensureDir(File directory) {
        if (!directory.exists() || directory.isDirectory() || directory.delete()) {
            if (directory.exists()) {
                return true;
            }
            return directory.mkdirs();
        }
        return false;
    }
}
