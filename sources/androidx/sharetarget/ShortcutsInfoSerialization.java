package androidx.sharetarget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import androidx.collection.ArrayMap;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.util.AtomicFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes.dex */
abstract class ShortcutsInfoSerialization {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ShortcutContainer {
        final String mBitmapPath;
        final String mResourceName;
        final ShortcutInfoCompat mShortcutInfo;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ShortcutContainer(ShortcutInfoCompat shortcutInfoCompat, String str, String str2) {
            this.mShortcutInfo = shortcutInfoCompat;
            this.mResourceName = str;
            this.mBitmapPath = str2;
        }
    }

    private static String getAttributeValue(XmlPullParser xmlPullParser, String str) {
        String attributeValue = xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", str);
        return attributeValue == null ? xmlPullParser.getAttributeValue(null, str) : attributeValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map loadFromXml(File file, Context context) {
        FileInputStream fileInputStream;
        ShortcutContainer parseShortcutContainer;
        ShortcutInfoCompat shortcutInfoCompat;
        ArrayMap arrayMap = new ArrayMap();
        try {
            fileInputStream = new FileInputStream(file);
            try {
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            file.delete();
            Log.e("ShortcutInfoCompatSaver", "Failed to load saved values from file " + file.getAbsolutePath() + ". Old state removed, new added", e);
        }
        if (!file.exists()) {
            fileInputStream.close();
            return arrayMap;
        }
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(fileInputStream, "UTF_8");
        while (true) {
            int next = newPullParser.next();
            if (next == 1) {
                break;
            }
            if (next == 2 && newPullParser.getName().equals("target") && (parseShortcutContainer = parseShortcutContainer(newPullParser, context)) != null && (shortcutInfoCompat = parseShortcutContainer.mShortcutInfo) != null) {
                arrayMap.put(shortcutInfoCompat.getId(), parseShortcutContainer);
            }
        }
        fileInputStream.close();
        return arrayMap;
    }

    private static ComponentName parseComponentName(XmlPullParser xmlPullParser) {
        String attributeValue = getAttributeValue(xmlPullParser, "component");
        if (TextUtils.isEmpty(attributeValue)) {
            return null;
        }
        return ComponentName.unflattenFromString(attributeValue);
    }

    private static Intent parseIntent(XmlPullParser xmlPullParser) {
        String attributeValue = getAttributeValue(xmlPullParser, "action");
        String attributeValue2 = getAttributeValue(xmlPullParser, "targetPackage");
        String attributeValue3 = getAttributeValue(xmlPullParser, "targetClass");
        if (attributeValue == null) {
            return null;
        }
        Intent intent = new Intent(attributeValue);
        if (!TextUtils.isEmpty(attributeValue2) && !TextUtils.isEmpty(attributeValue3)) {
            intent.setClassName(attributeValue2, attributeValue3);
        }
        return intent;
    }

    private static ShortcutContainer parseShortcutContainer(XmlPullParser xmlPullParser, Context context) {
        if (!xmlPullParser.getName().equals("target")) {
            return null;
        }
        String attributeValue = getAttributeValue(xmlPullParser, "id");
        String attributeValue2 = getAttributeValue(xmlPullParser, "short_label");
        if (TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(attributeValue2)) {
            return null;
        }
        int parseInt = Integer.parseInt(getAttributeValue(xmlPullParser, "rank"));
        String attributeValue3 = getAttributeValue(xmlPullParser, "long_label");
        String attributeValue4 = getAttributeValue(xmlPullParser, "disabled_message");
        ComponentName parseComponentName = parseComponentName(xmlPullParser);
        String attributeValue5 = getAttributeValue(xmlPullParser, "icon_resource_name");
        String attributeValue6 = getAttributeValue(xmlPullParser, "icon_bitmap_path");
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        while (true) {
            int next = xmlPullParser.next();
            if (next != 1) {
                if (next != 2) {
                    if (next == 3 && xmlPullParser.getName().equals("target")) {
                        break;
                    }
                } else {
                    String name = xmlPullParser.getName();
                    name.hashCode();
                    if (name.equals("intent")) {
                        Intent parseIntent = parseIntent(xmlPullParser);
                        if (parseIntent != null) {
                            arrayList.add(parseIntent);
                        }
                    } else if (name.equals("categories")) {
                        String attributeValue7 = getAttributeValue(xmlPullParser, "name");
                        if (!TextUtils.isEmpty(attributeValue7)) {
                            hashSet.add(attributeValue7);
                        }
                    }
                }
            } else {
                break;
            }
        }
        ShortcutInfoCompat.Builder rank = new ShortcutInfoCompat.Builder(context, attributeValue).setShortLabel(attributeValue2).setRank(parseInt);
        if (!TextUtils.isEmpty(attributeValue3)) {
            rank.setLongLabel(attributeValue3);
        }
        if (!TextUtils.isEmpty(attributeValue4)) {
            rank.setDisabledMessage(attributeValue4);
        }
        if (parseComponentName != null) {
            rank.setActivity(parseComponentName);
        }
        if (!arrayList.isEmpty()) {
            rank.setIntents((Intent[]) arrayList.toArray(new Intent[0]));
        }
        if (!hashSet.isEmpty()) {
            rank.setCategories(hashSet);
        }
        return new ShortcutContainer(rank.build(), attributeValue5, attributeValue6);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void saveAsXml(List list, File file) {
        FileOutputStream startWrite;
        AtomicFile atomicFile = new AtomicFile(file);
        FileOutputStream fileOutputStream = null;
        try {
            startWrite = atomicFile.startWrite();
        } catch (Exception e) {
            e = e;
        }
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(startWrite);
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(bufferedOutputStream, "UTF_8");
            newSerializer.startDocument(null, Boolean.TRUE);
            newSerializer.startTag(null, "share_targets");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                serializeShortcutContainer(newSerializer, (ShortcutContainer) it.next());
            }
            newSerializer.endTag(null, "share_targets");
            newSerializer.endDocument();
            bufferedOutputStream.flush();
            startWrite.flush();
            atomicFile.finishWrite(startWrite);
        } catch (Exception e2) {
            e = e2;
            fileOutputStream = startWrite;
            Log.e("ShortcutInfoCompatSaver", "Failed to write to file " + atomicFile.getBaseFile(), e);
            atomicFile.failWrite(fileOutputStream);
            throw new RuntimeException("Failed to write to file " + atomicFile.getBaseFile(), e);
        }
    }

    private static void serializeAttribute(XmlSerializer xmlSerializer, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        xmlSerializer.attribute(null, str, str2);
    }

    private static void serializeCategory(XmlSerializer xmlSerializer, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        xmlSerializer.startTag(null, "categories");
        serializeAttribute(xmlSerializer, "name", str);
        xmlSerializer.endTag(null, "categories");
    }

    private static void serializeIntent(XmlSerializer xmlSerializer, Intent intent) {
        xmlSerializer.startTag(null, "intent");
        serializeAttribute(xmlSerializer, "action", intent.getAction());
        if (intent.getComponent() != null) {
            serializeAttribute(xmlSerializer, "targetPackage", intent.getComponent().getPackageName());
            serializeAttribute(xmlSerializer, "targetClass", intent.getComponent().getClassName());
        }
        xmlSerializer.endTag(null, "intent");
    }

    private static void serializeShortcutContainer(XmlSerializer xmlSerializer, ShortcutContainer shortcutContainer) {
        xmlSerializer.startTag(null, "target");
        ShortcutInfoCompat shortcutInfoCompat = shortcutContainer.mShortcutInfo;
        serializeAttribute(xmlSerializer, "id", shortcutInfoCompat.getId());
        serializeAttribute(xmlSerializer, "short_label", shortcutInfoCompat.getShortLabel().toString());
        serializeAttribute(xmlSerializer, "rank", Integer.toString(shortcutInfoCompat.getRank()));
        if (!TextUtils.isEmpty(shortcutInfoCompat.getLongLabel())) {
            serializeAttribute(xmlSerializer, "long_label", shortcutInfoCompat.getLongLabel().toString());
        }
        if (!TextUtils.isEmpty(shortcutInfoCompat.getDisabledMessage())) {
            serializeAttribute(xmlSerializer, "disabled_message", shortcutInfoCompat.getDisabledMessage().toString());
        }
        if (shortcutInfoCompat.getActivity() != null) {
            serializeAttribute(xmlSerializer, "component", shortcutInfoCompat.getActivity().flattenToString());
        }
        if (!TextUtils.isEmpty(shortcutContainer.mResourceName)) {
            serializeAttribute(xmlSerializer, "icon_resource_name", shortcutContainer.mResourceName);
        }
        if (!TextUtils.isEmpty(shortcutContainer.mBitmapPath)) {
            serializeAttribute(xmlSerializer, "icon_bitmap_path", shortcutContainer.mBitmapPath);
        }
        for (Intent intent : shortcutInfoCompat.getIntents()) {
            serializeIntent(xmlSerializer, intent);
        }
        Iterator it = shortcutInfoCompat.getCategories().iterator();
        while (it.hasNext()) {
            serializeCategory(xmlSerializer, (String) it.next());
        }
        xmlSerializer.endTag(null, "target");
    }
}
