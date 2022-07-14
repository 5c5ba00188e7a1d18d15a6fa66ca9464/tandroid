package androidx.sharetarget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import androidx.sharetarget.ShareTargetCompat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
class ShareTargetXmlParser {
    private static final String ATTR_HOST = "host";
    private static final String ATTR_MIME_TYPE = "mimeType";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String ATTR_PATH_PATTERN = "pathPattern";
    private static final String ATTR_PATH_PREFIX = "pathPrefix";
    private static final String ATTR_PORT = "port";
    private static final String ATTR_SCHEME = "scheme";
    private static final String ATTR_TARGET_CLASS = "targetClass";
    private static final Object GET_INSTANCE_LOCK = new Object();
    private static final String META_DATA_APP_SHORTCUTS = "android.app.shortcuts";
    static final String TAG = "ShareTargetXmlParser";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_DATA = "data";
    private static final String TAG_SHARE_TARGET = "share-target";
    private static volatile ArrayList<ShareTargetCompat> sShareTargets;

    public static ArrayList<ShareTargetCompat> getShareTargets(Context context) {
        if (sShareTargets == null) {
            synchronized (GET_INSTANCE_LOCK) {
                if (sShareTargets == null) {
                    sShareTargets = parseShareTargets(context);
                }
            }
        }
        return sShareTargets;
    }

    private ShareTargetXmlParser() {
    }

    private static ArrayList<ShareTargetCompat> parseShareTargets(Context context) {
        ArrayList<ShareTargetCompat> targets = new ArrayList<>();
        Intent mainIntent = new Intent("android.intent.action.MAIN");
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        mainIntent.setPackage(context.getPackageName());
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(mainIntent, 128);
        if (resolveInfos == null) {
            return targets;
        }
        for (ResolveInfo info : resolveInfos) {
            ActivityInfo activityInfo = info.activityInfo;
            Bundle metaData = activityInfo.metaData;
            if (metaData != null && metaData.containsKey(META_DATA_APP_SHORTCUTS)) {
                List<ShareTargetCompat> shareTargets = parseShareTargets(context, activityInfo);
                targets.addAll(shareTargets);
            }
        }
        return targets;
    }

    private static ArrayList<ShareTargetCompat> parseShareTargets(Context context, ActivityInfo activityInfo) {
        ShareTargetCompat target;
        ArrayList<ShareTargetCompat> targets = new ArrayList<>();
        XmlResourceParser parser = getXmlResourceParser(context, activityInfo);
        while (true) {
            try {
                int type = parser.next();
                if (type == 1) {
                    break;
                } else if (type == 2 && parser.getName().equals(TAG_SHARE_TARGET) && (target = parseShareTarget(parser)) != null) {
                    targets.add(target);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to parse the Xml resource: ", e);
            }
        }
        parser.close();
        return targets;
    }

    private static XmlResourceParser getXmlResourceParser(Context context, ActivityInfo info) {
        XmlResourceParser parser = info.loadXmlMetaData(context.getPackageManager(), META_DATA_APP_SHORTCUTS);
        if (parser == null) {
            throw new IllegalArgumentException("Failed to open android.app.shortcuts meta-data resource of " + info.name);
        }
        return parser;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x002e, code lost:
        if (r3.equals(androidx.sharetarget.ShareTargetXmlParser.TAG_CATEGORY) != false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ShareTargetCompat parseShareTarget(XmlResourceParser parser) throws Exception {
        String targetClass = getAttributeValue(parser, ATTR_TARGET_CLASS);
        ArrayList<ShareTargetCompat.TargetData> targetData = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        while (true) {
            int type = parser.next();
            char c = 1;
            if (type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    switch (name.hashCode()) {
                        case 3076010:
                            if (name.equals("data")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 50511102:
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            targetData.add(parseTargetData(parser));
                            continue;
                        case 1:
                            categories.add(getAttributeValue(parser, "name"));
                            continue;
                    }
                } else if (type == 3 && parser.getName().equals(TAG_SHARE_TARGET)) {
                }
            }
        }
        if (targetData.isEmpty() || targetClass == null || categories.isEmpty()) {
            return null;
        }
        return new ShareTargetCompat((ShareTargetCompat.TargetData[]) targetData.toArray(new ShareTargetCompat.TargetData[targetData.size()]), targetClass, (String[]) categories.toArray(new String[categories.size()]));
    }

    private static ShareTargetCompat.TargetData parseTargetData(XmlResourceParser parser) {
        String scheme = getAttributeValue(parser, ATTR_SCHEME);
        String host = getAttributeValue(parser, ATTR_HOST);
        String port = getAttributeValue(parser, ATTR_PORT);
        String path = getAttributeValue(parser, ATTR_PATH);
        String pathPattern = getAttributeValue(parser, ATTR_PATH_PATTERN);
        String pathPrefix = getAttributeValue(parser, ATTR_PATH_PREFIX);
        String mimeType = getAttributeValue(parser, ATTR_MIME_TYPE);
        return new ShareTargetCompat.TargetData(scheme, host, port, path, pathPattern, pathPrefix, mimeType);
    }

    private static String getAttributeValue(XmlResourceParser parser, String attribute) {
        String value = parser.getAttributeValue("http://schemas.android.com/apk/res/android", attribute);
        if (value == null) {
            return parser.getAttributeValue(null, attribute);
        }
        return value;
    }
}
