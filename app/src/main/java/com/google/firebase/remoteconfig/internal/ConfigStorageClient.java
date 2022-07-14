package com.google.firebase.remoteconfig.internal;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ConfigStorageClient {
    private static final String JSON_STRING_ENCODING = "UTF-8";
    private static final Map<String, ConfigStorageClient> clientInstances = new HashMap();
    private final Context context;
    private final String fileName;

    private ConfigStorageClient(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public synchronized Void write(ConfigContainer container) throws IOException {
        FileOutputStream outputStream = this.context.openFileOutput(this.fileName, 0);
        outputStream.write(container.toString().getBytes("UTF-8"));
        outputStream.close();
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003a A[Catch: all -> 0x003e, TRY_LEAVE, TryCatch #3 {, blocks: (B:6:0x0028, B:11:0x0030, B:12:0x0033, B:18:0x003a, B:4:0x0002), top: B:23:0x0002, inners: #3 }] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized ConfigContainer read() throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = this.context.openFileInput(this.fileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes, 0, bytes.length);
            String containerJsonString = new String(bytes, "UTF-8");
            JSONObject containerJson = new JSONObject(containerJsonString);
            ConfigContainer copyOf = ConfigContainer.copyOf(containerJson);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return copyOf;
        } catch (FileNotFoundException e) {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return null;
        } catch (JSONException e2) {
            if (fileInputStream != null) {
            }
            return null;
        } catch (Throwable th) {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    public synchronized Void clear() {
        this.context.deleteFile(this.fileName);
        return null;
    }

    public static synchronized ConfigStorageClient getInstance(Context context, String fileName) {
        ConfigStorageClient configStorageClient;
        synchronized (ConfigStorageClient.class) {
            Map<String, ConfigStorageClient> map = clientInstances;
            if (!map.containsKey(fileName)) {
                map.put(fileName, new ConfigStorageClient(context, fileName));
            }
            configStorageClient = map.get(fileName);
        }
        return configStorageClient;
    }

    public static synchronized void clearInstancesForTest() {
        synchronized (ConfigStorageClient.class) {
            clientInstances.clear();
        }
    }

    public String getFileName() {
        return this.fileName;
    }
}
