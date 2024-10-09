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
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
abstract class ShareTargetXmlParser {
    private static final Object GET_INSTANCE_LOCK = new Object();
    private static volatile ArrayList sShareTargets;

    private static String getAttributeValue(XmlResourceParser xmlResourceParser, String str) {
        String attributeValue = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", str);
        return attributeValue == null ? xmlResourceParser.getAttributeValue(null, str) : attributeValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList getShareTargets(Context context) {
        if (sShareTargets == null) {
            synchronized (GET_INSTANCE_LOCK) {
                try {
                    if (sShareTargets == null) {
                        sShareTargets = parseShareTargets(context);
                    }
                } finally {
                }
            }
        }
        return sShareTargets;
    }

    private static XmlResourceParser getXmlResourceParser(Context context, ActivityInfo activityInfo) {
        XmlResourceParser loadXmlMetaData = activityInfo.loadXmlMetaData(context.getPackageManager(), "android.app.shortcuts");
        if (loadXmlMetaData != null) {
            return loadXmlMetaData;
        }
        throw new IllegalArgumentException("Failed to open android.app.shortcuts meta-data resource of " + activityInfo.name);
    }

    private static ShareTargetCompat parseShareTarget(XmlResourceParser xmlResourceParser) {
        String attributeValue = getAttributeValue(xmlResourceParser, "targetClass");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (true) {
            int next = xmlResourceParser.next();
            if (next != 1) {
                if (next != 2) {
                    if (next == 3 && xmlResourceParser.getName().equals("share-target")) {
                        break;
                    }
                } else {
                    String name = xmlResourceParser.getName();
                    name.hashCode();
                    if (name.equals("data")) {
                        arrayList.add(parseTargetData(xmlResourceParser));
                    } else if (name.equals("category")) {
                        arrayList2.add(getAttributeValue(xmlResourceParser, "name"));
                    }
                }
            } else {
                break;
            }
        }
        if (arrayList.isEmpty() || attributeValue == null || arrayList2.isEmpty()) {
            return null;
        }
        return new ShareTargetCompat((ShareTargetCompat.TargetData[]) arrayList.toArray(new ShareTargetCompat.TargetData[arrayList.size()]), attributeValue, (String[]) arrayList2.toArray(new String[arrayList2.size()]));
    }

    private static ArrayList parseShareTargets(Context context) {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(context.getPackageName());
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 128);
        if (queryIntentActivities == null) {
            return arrayList;
        }
        Iterator<ResolveInfo> it = queryIntentActivities.iterator();
        while (it.hasNext()) {
            ActivityInfo activityInfo = it.next().activityInfo;
            Bundle bundle = activityInfo.metaData;
            if (bundle != null && bundle.containsKey("android.app.shortcuts")) {
                arrayList.addAll(parseShareTargets(context, activityInfo));
            }
        }
        return arrayList;
    }

    private static ArrayList parseShareTargets(Context context, ActivityInfo activityInfo) {
        ShareTargetCompat parseShareTarget;
        ArrayList arrayList = new ArrayList();
        XmlResourceParser xmlResourceParser = getXmlResourceParser(context, activityInfo);
        while (true) {
            try {
                int next = xmlResourceParser.next();
                if (next == 1) {
                    break;
                }
                if (next == 2 && xmlResourceParser.getName().equals("share-target") && (parseShareTarget = parseShareTarget(xmlResourceParser)) != null) {
                    arrayList.add(parseShareTarget);
                }
            } catch (Exception e) {
                Log.e("ShareTargetXmlParser", "Failed to parse the Xml resource: ", e);
            }
        }
        xmlResourceParser.close();
        return arrayList;
    }

    private static ShareTargetCompat.TargetData parseTargetData(XmlResourceParser xmlResourceParser) {
        return new ShareTargetCompat.TargetData(getAttributeValue(xmlResourceParser, "scheme"), getAttributeValue(xmlResourceParser, "host"), getAttributeValue(xmlResourceParser, "port"), getAttributeValue(xmlResourceParser, "path"), getAttributeValue(xmlResourceParser, "pathPattern"), getAttributeValue(xmlResourceParser, "pathPrefix"), getAttributeValue(xmlResourceParser, "mimeType"));
    }
}
