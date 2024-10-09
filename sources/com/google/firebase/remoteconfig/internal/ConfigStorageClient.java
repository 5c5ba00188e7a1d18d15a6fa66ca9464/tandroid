package com.google.firebase.remoteconfig.internal;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ConfigStorageClient {
    private static final Map clientInstances = new HashMap();
    private final Context context;
    private final String fileName;

    private ConfigStorageClient(Context context, String str) {
        this.context = context;
        this.fileName = str;
    }

    public static synchronized ConfigStorageClient getInstance(Context context, String str) {
        ConfigStorageClient configStorageClient;
        synchronized (ConfigStorageClient.class) {
            try {
                Map map = clientInstances;
                if (!map.containsKey(str)) {
                    map.put(str, new ConfigStorageClient(context, str));
                }
                configStorageClient = (ConfigStorageClient) map.get(str);
            } catch (Throwable th) {
                throw th;
            }
        }
        return configStorageClient;
    }

    public synchronized Void clear() {
        this.context.deleteFile(this.fileName);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getFileName() {
        return this.fileName;
    }

    public synchronized ConfigContainer read() {
        FileInputStream fileInputStream;
        Throwable th;
        try {
            fileInputStream = this.context.openFileInput(this.fileName);
        } catch (FileNotFoundException | JSONException unused) {
            fileInputStream = null;
        } catch (Throwable th2) {
            fileInputStream = null;
            th = th2;
        }
        try {
            int available = fileInputStream.available();
            byte[] bArr = new byte[available];
            fileInputStream.read(bArr, 0, available);
            ConfigContainer copyOf = ConfigContainer.copyOf(new JSONObject(new String(bArr, "UTF-8")));
            fileInputStream.close();
            return copyOf;
        } catch (FileNotFoundException | JSONException unused2) {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    public synchronized Void write(ConfigContainer configContainer) {
        FileOutputStream openFileOutput = this.context.openFileOutput(this.fileName, 0);
        try {
            openFileOutput.write(configContainer.toString().getBytes("UTF-8"));
        } finally {
            openFileOutput.close();
        }
        return null;
    }
}
