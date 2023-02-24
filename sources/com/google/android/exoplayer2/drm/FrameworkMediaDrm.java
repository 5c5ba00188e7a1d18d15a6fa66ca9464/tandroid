package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.media.DeniedByServerException;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.MediaDrmException;
import android.media.NotProvisionedException;
import android.media.UnsupportedSchemeException;
import android.media.metrics.LogSessionId;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Charsets;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public final class FrameworkMediaDrm implements ExoMediaDrm {
    public static final ExoMediaDrm.Provider DEFAULT_PROVIDER = FrameworkMediaDrm$$ExternalSyntheticLambda1.INSTANCE;
    private final MediaDrm mediaDrm;
    private int referenceCount;
    private final UUID uuid;

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public int getCryptoType() {
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ExoMediaDrm lambda$static$0(UUID uuid) {
        try {
            return newInstance(uuid);
        } catch (UnsupportedDrmException unused) {
            Log.e("FrameworkMediaDrm", "Failed to instantiate a FrameworkMediaDrm for uuid: " + uuid + ".");
            return new DummyExoMediaDrm();
        }
    }

    public static FrameworkMediaDrm newInstance(UUID uuid) throws UnsupportedDrmException {
        try {
            return new FrameworkMediaDrm(uuid);
        } catch (UnsupportedSchemeException e) {
            throw new UnsupportedDrmException(1, e);
        } catch (Exception e2) {
            throw new UnsupportedDrmException(2, e2);
        }
    }

    private FrameworkMediaDrm(UUID uuid) throws UnsupportedSchemeException {
        Assertions.checkNotNull(uuid);
        Assertions.checkArgument(!C.COMMON_PSSH_UUID.equals(uuid), "Use C.CLEARKEY_UUID instead");
        this.uuid = uuid;
        MediaDrm mediaDrm = new MediaDrm(adjustUuid(uuid));
        this.mediaDrm = mediaDrm;
        this.referenceCount = 1;
        if (C.WIDEVINE_UUID.equals(uuid) && needsForceWidevineL3Workaround()) {
            forceWidevineL3(mediaDrm);
        }
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void setOnEventListener(final ExoMediaDrm.OnEventListener onEventListener) {
        this.mediaDrm.setOnEventListener(onEventListener == null ? null : new MediaDrm.OnEventListener() { // from class: com.google.android.exoplayer2.drm.FrameworkMediaDrm$$ExternalSyntheticLambda0
            @Override // android.media.MediaDrm.OnEventListener
            public final void onEvent(MediaDrm mediaDrm, byte[] bArr, int i, int i2, byte[] bArr2) {
                FrameworkMediaDrm.this.lambda$setOnEventListener$1(onEventListener, mediaDrm, bArr, i, i2, bArr2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnEventListener$1(ExoMediaDrm.OnEventListener onEventListener, MediaDrm mediaDrm, byte[] bArr, int i, int i2, byte[] bArr2) {
        onEventListener.onEvent(this, bArr, i, i2, bArr2);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public byte[] openSession() throws MediaDrmException {
        return this.mediaDrm.openSession();
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void closeSession(byte[] bArr) {
        this.mediaDrm.closeSession(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void setPlayerIdForSession(byte[] bArr, PlayerId playerId) {
        if (Util.SDK_INT >= 31) {
            try {
                Api31.setLogSessionIdOnMediaDrmSession(this.mediaDrm, bArr, playerId);
            } catch (UnsupportedOperationException unused) {
                Log.w("FrameworkMediaDrm", "setLogSessionId failed.");
            }
        }
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    @SuppressLint({"WrongConstant"})
    public ExoMediaDrm.KeyRequest getKeyRequest(byte[] bArr, List<DrmInitData.SchemeData> list, int i, HashMap<String, String> hashMap) throws NotProvisionedException {
        byte[] bArr2;
        String str;
        DrmInitData.SchemeData schemeData = null;
        if (list != null) {
            schemeData = getSchemeData(this.uuid, list);
            bArr2 = adjustRequestInitData(this.uuid, (byte[]) Assertions.checkNotNull(schemeData.data));
            str = adjustRequestMimeType(this.uuid, schemeData.mimeType);
        } else {
            bArr2 = null;
            str = null;
        }
        MediaDrm.KeyRequest keyRequest = this.mediaDrm.getKeyRequest(bArr, bArr2, str, i, hashMap);
        byte[] adjustRequestData = adjustRequestData(this.uuid, keyRequest.getData());
        String adjustLicenseServerUrl = adjustLicenseServerUrl(keyRequest.getDefaultUrl());
        if (TextUtils.isEmpty(adjustLicenseServerUrl) && schemeData != null && !TextUtils.isEmpty(schemeData.licenseServerUrl)) {
            adjustLicenseServerUrl = schemeData.licenseServerUrl;
        }
        return new ExoMediaDrm.KeyRequest(adjustRequestData, adjustLicenseServerUrl, Util.SDK_INT >= 23 ? keyRequest.getRequestType() : Integer.MIN_VALUE);
    }

    private static String adjustLicenseServerUrl(String str) {
        return "<LA_URL>https://x</LA_URL>".equals(str) ? "" : (Util.SDK_INT == 33 && "https://default.url".equals(str)) ? "" : str;
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public byte[] provideKeyResponse(byte[] bArr, byte[] bArr2) throws NotProvisionedException, DeniedByServerException {
        if (C.CLEARKEY_UUID.equals(this.uuid)) {
            bArr2 = ClearKeyUtil.adjustResponseData(bArr2);
        }
        return this.mediaDrm.provideKeyResponse(bArr, bArr2);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public ExoMediaDrm.ProvisionRequest getProvisionRequest() {
        MediaDrm.ProvisionRequest provisionRequest = this.mediaDrm.getProvisionRequest();
        return new ExoMediaDrm.ProvisionRequest(provisionRequest.getData(), provisionRequest.getDefaultUrl());
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void provideProvisionResponse(byte[] bArr) throws DeniedByServerException {
        this.mediaDrm.provideProvisionResponse(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public Map<String, String> queryKeyStatus(byte[] bArr) {
        return this.mediaDrm.queryKeyStatus(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public boolean requiresSecureDecoder(byte[] bArr, String str) {
        if (Util.SDK_INT >= 31) {
            return Api31.requiresSecureDecoder(this.mediaDrm, str);
        }
        try {
            MediaCrypto mediaCrypto = new MediaCrypto(this.uuid, bArr);
            try {
                return mediaCrypto.requiresSecureDecoderComponent(str);
            } finally {
                mediaCrypto.release();
            }
        } catch (MediaCryptoException unused) {
            return true;
        }
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public synchronized void release() {
        int i = this.referenceCount - 1;
        this.referenceCount = i;
        if (i == 0) {
            this.mediaDrm.release();
        }
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void restoreKeys(byte[] bArr, byte[] bArr2) {
        this.mediaDrm.restoreKeys(bArr, bArr2);
    }

    public String getPropertyString(String str) {
        return this.mediaDrm.getPropertyString(str);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public FrameworkCryptoConfig createCryptoConfig(byte[] bArr) throws MediaCryptoException {
        return new FrameworkCryptoConfig(adjustUuid(this.uuid), bArr, Util.SDK_INT < 21 && C.WIDEVINE_UUID.equals(this.uuid) && "L3".equals(getPropertyString("securityLevel")));
    }

    private static DrmInitData.SchemeData getSchemeData(UUID uuid, List<DrmInitData.SchemeData> list) {
        boolean z;
        if (!C.WIDEVINE_UUID.equals(uuid)) {
            return list.get(0);
        }
        if (Util.SDK_INT >= 28 && list.size() > 1) {
            DrmInitData.SchemeData schemeData = list.get(0);
            int i = 0;
            for (int i2 = 0; i2 < list.size(); i2++) {
                DrmInitData.SchemeData schemeData2 = list.get(i2);
                byte[] bArr = (byte[]) Assertions.checkNotNull(schemeData2.data);
                if (!Util.areEqual(schemeData2.mimeType, schemeData.mimeType) || !Util.areEqual(schemeData2.licenseServerUrl, schemeData.licenseServerUrl) || !PsshAtomUtil.isPsshAtom(bArr)) {
                    z = false;
                    break;
                }
                i += bArr.length;
            }
            z = true;
            if (z) {
                byte[] bArr2 = new byte[i];
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    byte[] bArr3 = (byte[]) Assertions.checkNotNull(list.get(i4).data);
                    int length = bArr3.length;
                    System.arraycopy(bArr3, 0, bArr2, i3, length);
                    i3 += length;
                }
                return schemeData.copyWithData(bArr2);
            }
        }
        for (int i5 = 0; i5 < list.size(); i5++) {
            DrmInitData.SchemeData schemeData3 = list.get(i5);
            int parseVersion = PsshAtomUtil.parseVersion((byte[]) Assertions.checkNotNull(schemeData3.data));
            int i6 = Util.SDK_INT;
            if (i6 < 23 && parseVersion == 0) {
                return schemeData3;
            }
            if (i6 >= 23 && parseVersion == 1) {
                return schemeData3;
            }
        }
        return list.get(0);
    }

    private static UUID adjustUuid(UUID uuid) {
        return (Util.SDK_INT >= 27 || !C.CLEARKEY_UUID.equals(uuid)) ? uuid : C.COMMON_PSSH_UUID;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0056, code lost:
        if ("AFTT".equals(r0) == false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static byte[] adjustRequestInitData(UUID uuid, byte[] bArr) {
        UUID uuid2 = C.PLAYREADY_UUID;
        if (uuid2.equals(uuid)) {
            byte[] parseSchemeSpecificData = PsshAtomUtil.parseSchemeSpecificData(bArr, uuid);
            if (parseSchemeSpecificData != null) {
                bArr = parseSchemeSpecificData;
            }
            bArr = PsshAtomUtil.buildPsshAtom(uuid2, addLaUrlAttributeIfMissing(bArr));
        }
        if (Util.SDK_INT >= 23 || !C.WIDEVINE_UUID.equals(uuid)) {
            if (uuid2.equals(uuid) && "Amazon".equals(Util.MANUFACTURER)) {
                String str = Util.MODEL;
                if (!"AFTB".equals(str)) {
                    if (!"AFTS".equals(str)) {
                        if (!"AFTM".equals(str)) {
                        }
                    }
                }
            }
        }
        byte[] parseSchemeSpecificData2 = PsshAtomUtil.parseSchemeSpecificData(bArr, uuid);
        return parseSchemeSpecificData2 != null ? parseSchemeSpecificData2 : bArr;
    }

    private static String adjustRequestMimeType(UUID uuid, String str) {
        return (Util.SDK_INT < 26 && C.CLEARKEY_UUID.equals(uuid) && ("video/mp4".equals(str) || "audio/mp4".equals(str))) ? "cenc" : str;
    }

    private static byte[] adjustRequestData(UUID uuid, byte[] bArr) {
        return C.CLEARKEY_UUID.equals(uuid) ? ClearKeyUtil.adjustRequestData(bArr) : bArr;
    }

    private static void forceWidevineL3(MediaDrm mediaDrm) {
        mediaDrm.setPropertyString("securityLevel", "L3");
    }

    private static boolean needsForceWidevineL3Workaround() {
        return "ASUS_Z00AD".equals(Util.MODEL);
    }

    private static byte[] addLaUrlAttributeIfMissing(byte[] bArr) {
        int indexOf;
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr);
        int readLittleEndianInt = parsableByteArray.readLittleEndianInt();
        short readLittleEndianShort = parsableByteArray.readLittleEndianShort();
        short readLittleEndianShort2 = parsableByteArray.readLittleEndianShort();
        if (readLittleEndianShort != 1 || readLittleEndianShort2 != 1) {
            Log.i("FrameworkMediaDrm", "Unexpected record count or type. Skipping LA_URL workaround.");
            return bArr;
        }
        short readLittleEndianShort3 = parsableByteArray.readLittleEndianShort();
        Charset charset = Charsets.UTF_16LE;
        String readString = parsableByteArray.readString(readLittleEndianShort3, charset);
        if (readString.contains("<LA_URL>")) {
            return bArr;
        }
        if (readString.indexOf("</DATA>") == -1) {
            Log.w("FrameworkMediaDrm", "Could not find the </DATA> tag. Skipping LA_URL workaround.");
        }
        String str = readString.substring(0, indexOf) + "<LA_URL>https://x</LA_URL>" + readString.substring(indexOf);
        int i = readLittleEndianInt + 52;
        ByteBuffer allocate = ByteBuffer.allocate(i);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.putInt(i);
        allocate.putShort(readLittleEndianShort);
        allocate.putShort(readLittleEndianShort2);
        allocate.putShort((short) (str.length() * 2));
        allocate.put(str.getBytes(charset));
        return allocate.array();
    }

    /* loaded from: classes.dex */
    private static class Api31 {
        public static boolean requiresSecureDecoder(MediaDrm mediaDrm, String str) {
            return mediaDrm.requiresSecureDecoder(str);
        }

        public static void setLogSessionIdOnMediaDrmSession(MediaDrm mediaDrm, byte[] bArr, PlayerId playerId) {
            LogSessionId logSessionId = playerId.getLogSessionId();
            if (logSessionId.equals(LogSessionId.LOG_SESSION_ID_NONE)) {
                return;
            }
            ((MediaDrm.PlaybackComponent) Assertions.checkNotNull(mediaDrm.getPlaybackComponent(bArr))).setLogSessionId(logSessionId);
        }
    }
}
