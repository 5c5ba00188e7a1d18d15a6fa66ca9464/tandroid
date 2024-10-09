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
import java.util.Iterator;
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
public class ShortcutInfoCompatSaverImpl extends ShortcutInfoCompatSaver {
    private static final Object GET_INSTANCE_LOCK = new Object();
    private static volatile ShortcutInfoCompatSaverImpl sInstance;
    final File mBitmapsDir;
    final ExecutorService mCacheUpdateService;
    final Context mContext;
    private final ExecutorService mDiskIoService;
    final File mTargetsXmlFile;
    final Map mShortcutsMap = new ArrayMap();
    final Map mScheduledBitmapTasks = new ArrayMap();

    ShortcutInfoCompatSaverImpl(Context context, ExecutorService executorService, ExecutorService executorService2) {
        this.mContext = context.getApplicationContext();
        this.mCacheUpdateService = executorService;
        this.mDiskIoService = executorService2;
        final File file = new File(context.getFilesDir(), "ShortcutInfoCompatSaver_share_targets");
        this.mBitmapsDir = new File(file, "ShortcutInfoCompatSaver_share_targets_bitmaps");
        this.mTargetsXmlFile = new File(file, "targets.xml");
        executorService.submit(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ShortcutInfoCompatSaverImpl.ensureDir(file);
                    ShortcutInfoCompatSaverImpl.ensureDir(ShortcutInfoCompatSaverImpl.this.mBitmapsDir);
                    ShortcutInfoCompatSaverImpl shortcutInfoCompatSaverImpl = ShortcutInfoCompatSaverImpl.this;
                    shortcutInfoCompatSaverImpl.mShortcutsMap.putAll(ShortcutsInfoSerialization.loadFromXml(shortcutInfoCompatSaverImpl.mTargetsXmlFile, shortcutInfoCompatSaverImpl.mContext));
                    ShortcutInfoCompatSaverImpl.this.deleteDanglingBitmaps(new ArrayList(ShortcutInfoCompatSaverImpl.this.mShortcutsMap.values()));
                } catch (Exception e) {
                    Log.w("ShortcutInfoCompatSaver", "ShortcutInfoCompatSaver started with an exceptions ", e);
                }
            }
        });
    }

    static ExecutorService createExecutorService() {
        return new ThreadPoolExecutor(0, 1, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    static boolean ensureDir(File file) {
        if (file.exists() && !file.isDirectory() && !file.delete()) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    public static ShortcutInfoCompatSaverImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GET_INSTANCE_LOCK) {
                try {
                    if (sInstance == null) {
                        sInstance = new ShortcutInfoCompatSaverImpl(context, createExecutorService(), createExecutorService());
                    }
                } finally {
                }
            }
        }
        return sInstance;
    }

    private ListenableFuture submitDiskOperation(final Runnable runnable) {
        final ResolvableFuture create = ResolvableFuture.create();
        this.mDiskIoService.submit(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.9
            @Override // java.lang.Runnable
            public void run() {
                if (create.isCancelled()) {
                    return;
                }
                try {
                    runnable.run();
                    create.set(null);
                } catch (Exception e) {
                    create.setException(e);
                }
            }
        });
        return create;
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture addShortcuts(List list) {
        final ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(new ShortcutInfoCompat.Builder((ShortcutInfoCompat) it.next()).build());
        }
        final ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.7
            @Override // java.lang.Runnable
            public void run() {
                for (ShortcutInfoCompat shortcutInfoCompat : arrayList) {
                    Set categories = shortcutInfoCompat.getCategories();
                    if (categories != null && !categories.isEmpty()) {
                        ShortcutsInfoSerialization.ShortcutContainer containerFrom = ShortcutInfoCompatSaverImpl.this.containerFrom(shortcutInfoCompat);
                        Bitmap bitmap = containerFrom.mBitmapPath != null ? shortcutInfoCompat.getIcon().getBitmap() : null;
                        final String id = shortcutInfoCompat.getId();
                        ShortcutInfoCompatSaverImpl.this.mShortcutsMap.put(id, containerFrom);
                        if (bitmap != null) {
                            final ListenableFuture scheduleBitmapSaving = ShortcutInfoCompatSaverImpl.this.scheduleBitmapSaving(bitmap, containerFrom.mBitmapPath);
                            ListenableFuture listenableFuture = (ListenableFuture) ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.put(id, scheduleBitmapSaving);
                            if (listenableFuture != null) {
                                listenableFuture.cancel(false);
                            }
                            scheduleBitmapSaving.addListener(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.7.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.remove(id);
                                    if (scheduleBitmapSaving.isCancelled()) {
                                        return;
                                    }
                                    try {
                                        scheduleBitmapSaving.get();
                                    } catch (Exception e) {
                                        create.setException(e);
                                    }
                                }
                            }, ShortcutInfoCompatSaverImpl.this.mCacheUpdateService);
                        }
                    }
                }
                ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(create);
            }
        });
        return create;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0012, code lost:
    
        if (r2 != 5) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    ShortcutsInfoSerialization.ShortcutContainer containerFrom(ShortcutInfoCompat shortcutInfoCompat) {
        String str;
        String str2;
        IconCompat icon = shortcutInfoCompat.getIcon();
        if (icon != null) {
            int type = icon.getType();
            if (type != 1) {
                if (type == 2) {
                    str = this.mContext.getResources().getResourceName(icon.getResId());
                    str2 = null;
                    return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcutInfoCompat).setIcon(null).build(), str, str2);
                }
            }
            str2 = new File(this.mBitmapsDir, UUID.randomUUID().toString()).getAbsolutePath();
            str = null;
            return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcutInfoCompat).setIcon(null).build(), str, str2);
        }
        str = null;
        str2 = null;
        return new ShortcutsInfoSerialization.ShortcutContainer(new ShortcutInfoCompat.Builder(shortcutInfoCompat).setIcon(null).build(), str, str2);
    }

    void deleteDanglingBitmaps(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ShortcutsInfoSerialization.ShortcutContainer shortcutContainer = (ShortcutsInfoSerialization.ShortcutContainer) it.next();
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

    public IconCompat getShortcutIcon(final String str) {
        int i;
        Bitmap bitmap;
        final ShortcutsInfoSerialization.ShortcutContainer shortcutContainer = (ShortcutsInfoSerialization.ShortcutContainer) this.mCacheUpdateService.submit(new Callable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.5
            @Override // java.util.concurrent.Callable
            public ShortcutsInfoSerialization.ShortcutContainer call() {
                return (ShortcutsInfoSerialization.ShortcutContainer) ShortcutInfoCompatSaverImpl.this.mShortcutsMap.get(str);
            }
        }).get();
        if (shortcutContainer == null) {
            return null;
        }
        if (!TextUtils.isEmpty(shortcutContainer.mResourceName)) {
            try {
                i = this.mContext.getResources().getIdentifier(shortcutContainer.mResourceName, null, null);
            } catch (Exception unused) {
                i = 0;
            }
            if (i != 0) {
                return IconCompat.createWithResource(this.mContext, i);
            }
        }
        if (TextUtils.isEmpty(shortcutContainer.mBitmapPath) || (bitmap = (Bitmap) this.mDiskIoService.submit(new Callable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.6
            @Override // java.util.concurrent.Callable
            public Bitmap call() {
                return BitmapFactory.decodeFile(shortcutContainer.mBitmapPath);
            }
        }).get()) == null) {
            return null;
        }
        return IconCompat.createWithBitmap(bitmap);
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public List getShortcuts() {
        return (List) this.mCacheUpdateService.submit(new Callable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.4
            @Override // java.util.concurrent.Callable
            public ArrayList call() {
                ArrayList arrayList = new ArrayList();
                Iterator it = ShortcutInfoCompatSaverImpl.this.mShortcutsMap.values().iterator();
                while (it.hasNext()) {
                    arrayList.add(new ShortcutInfoCompat.Builder(((ShortcutsInfoSerialization.ShortcutContainer) it.next()).mShortcutInfo).build());
                }
                return arrayList;
            }
        }).get();
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture removeAllShortcuts() {
        final ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.3
            @Override // java.lang.Runnable
            public void run() {
                ShortcutInfoCompatSaverImpl.this.mShortcutsMap.clear();
                Iterator it = ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.values().iterator();
                while (it.hasNext()) {
                    ((ListenableFuture) it.next()).cancel(false);
                }
                ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.clear();
                ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(create);
            }
        });
        return create;
    }

    @Override // androidx.core.content.pm.ShortcutInfoCompatSaver
    public ListenableFuture removeShortcuts(List list) {
        final ArrayList arrayList = new ArrayList(list);
        final ResolvableFuture create = ResolvableFuture.create();
        this.mCacheUpdateService.submit(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.2
            @Override // java.lang.Runnable
            public void run() {
                for (String str : arrayList) {
                    ShortcutInfoCompatSaverImpl.this.mShortcutsMap.remove(str);
                    ListenableFuture listenableFuture = (ListenableFuture) ShortcutInfoCompatSaverImpl.this.mScheduledBitmapTasks.remove(str);
                    if (listenableFuture != null) {
                        listenableFuture.cancel(false);
                    }
                }
                ShortcutInfoCompatSaverImpl.this.scheduleSyncCurrentState(create);
            }
        });
        return create;
    }

    void saveBitmap(Bitmap bitmap, String str) {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap is null");
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("path is empty");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            try {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.close();
                    return;
                }
                Log.wtf("ShortcutInfoCompatSaver", "Unable to compress bitmap");
                throw new RuntimeException("Unable to compress bitmap for saving " + str);
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException | OutOfMemoryError | RuntimeException e) {
            Log.wtf("ShortcutInfoCompatSaver", "Unable to write bitmap to file", e);
            throw new RuntimeException("Unable to write bitmap to file " + str, e);
        }
    }

    ListenableFuture scheduleBitmapSaving(final Bitmap bitmap, final String str) {
        return submitDiskOperation(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.8
            @Override // java.lang.Runnable
            public void run() {
                ShortcutInfoCompatSaverImpl.this.saveBitmap(bitmap, str);
            }
        });
    }

    void scheduleSyncCurrentState(final ResolvableFuture resolvableFuture) {
        final ArrayList arrayList = new ArrayList(this.mShortcutsMap.values());
        final ListenableFuture submitDiskOperation = submitDiskOperation(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.10
            @Override // java.lang.Runnable
            public void run() {
                ShortcutInfoCompatSaverImpl.this.deleteDanglingBitmaps(arrayList);
                ShortcutsInfoSerialization.saveAsXml(arrayList, ShortcutInfoCompatSaverImpl.this.mTargetsXmlFile);
            }
        });
        submitDiskOperation.addListener(new Runnable() { // from class: androidx.sharetarget.ShortcutInfoCompatSaverImpl.11
            @Override // java.lang.Runnable
            public void run() {
                try {
                    submitDiskOperation.get();
                    resolvableFuture.set(null);
                } catch (Exception e) {
                    resolvableFuture.setException(e);
                }
            }
        }, this.mCacheUpdateService);
    }
}
