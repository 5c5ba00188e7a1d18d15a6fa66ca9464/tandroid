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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes3.dex */
class ShortcutsInfoSerialization {
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_COMPONENT = "component";
    private static final String ATTR_DISABLED_MSG = "disabled_message";
    private static final String ATTR_ICON_BMP_PATH = "icon_bitmap_path";
    private static final String ATTR_ICON_RES_NAME = "icon_resource_name";
    private static final String ATTR_ID = "id";
    private static final String ATTR_LONG_LABEL = "long_label";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_RANK = "rank";
    private static final String ATTR_SHORT_LABEL = "short_label";
    private static final String ATTR_TARGET_CLASS = "targetClass";
    private static final String ATTR_TARGET_PACKAGE = "targetPackage";
    private static final String TAG = "ShortcutInfoCompatSaver";
    private static final String TAG_CATEGORY = "categories";
    private static final String TAG_INTENT = "intent";
    private static final String TAG_ROOT = "share_targets";
    private static final String TAG_TARGET = "target";

    private ShortcutsInfoSerialization() {
    }

    /* loaded from: classes3.dex */
    public static class ShortcutContainer {
        final String mBitmapPath;
        final String mResourceName;
        final ShortcutInfoCompat mShortcutInfo;

        public ShortcutContainer(ShortcutInfoCompat shortcut, String resourceName, String bitmapPath) {
            this.mShortcutInfo = shortcut;
            this.mResourceName = resourceName;
            this.mBitmapPath = bitmapPath;
        }
    }

    public static void saveAsXml(List<ShortcutContainer> shortcutsList, File output) {
        AtomicFile atomicFile = new AtomicFile(output);
        FileOutputStream fileStream = null;
        try {
            fileStream = atomicFile.startWrite();
            BufferedOutputStream stream = new BufferedOutputStream(fileStream);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(stream, "UTF_8");
            serializer.startDocument(null, true);
            serializer.startTag(null, TAG_ROOT);
            for (ShortcutContainer shortcut : shortcutsList) {
                serializeShortcutContainer(serializer, shortcut);
            }
            serializer.endTag(null, TAG_ROOT);
            serializer.endDocument();
            stream.flush();
            fileStream.flush();
            atomicFile.finishWrite(fileStream);
        } catch (Exception e) {
            Log.e(TAG, "Failed to write to file " + atomicFile.getBaseFile(), e);
            atomicFile.failWrite(fileStream);
            throw new RuntimeException("Failed to write to file " + atomicFile.getBaseFile(), e);
        }
    }

    public static Map<String, ShortcutContainer> loadFromXml(File input, Context context) {
        FileInputStream stream;
        ShortcutContainer shortcut;
        Map<String, ShortcutContainer> shortcutsList = new ArrayMap<>();
        try {
            stream = new FileInputStream(input);
        } catch (Exception e) {
            input.delete();
            Log.e(TAG, "Failed to load saved values from file " + input.getAbsolutePath() + ". Old state removed, new added", e);
        }
        if (input.exists()) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, "UTF_8");
            while (true) {
                int type = parser.next();
                if (type == 1) {
                    break;
                } else if (type == 2 && parser.getName().equals(TAG_TARGET) && (shortcut = parseShortcutContainer(parser, context)) != null && shortcut.mShortcutInfo != null) {
                    shortcutsList.put(shortcut.mShortcutInfo.getId(), shortcut);
                }
            }
            stream.close();
            return shortcutsList;
        }
        stream.close();
        return shortcutsList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0086, code lost:
        if (r12.equals(androidx.sharetarget.ShortcutsInfoSerialization.TAG_INTENT) != false) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ShortcutContainer parseShortcutContainer(XmlPullParser parser, Context context) throws Exception {
        if (!parser.getName().equals(TAG_TARGET)) {
            return null;
        }
        String id = getAttributeValue(parser, "id");
        CharSequence label = getAttributeValue(parser, ATTR_SHORT_LABEL);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(label)) {
            int rank = Integer.parseInt(getAttributeValue(parser, ATTR_RANK));
            CharSequence longLabel = getAttributeValue(parser, ATTR_LONG_LABEL);
            CharSequence disabledMessage = getAttributeValue(parser, ATTR_DISABLED_MSG);
            ComponentName activity = parseComponentName(parser);
            String iconResourceName = getAttributeValue(parser, ATTR_ICON_RES_NAME);
            String iconBitmapPath = getAttributeValue(parser, ATTR_ICON_BMP_PATH);
            ArrayList<Intent> intents = new ArrayList<>();
            Set<String> categories = new HashSet<>();
            while (true) {
                int type = parser.next();
                char c = 0;
                if (type != 1) {
                    if (type == 2) {
                        String name = parser.getName();
                        switch (name.hashCode()) {
                            case -1183762788:
                                break;
                            case 1296516636:
                                if (name.equals(TAG_CATEGORY)) {
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
                                Intent intent = parseIntent(parser);
                                if (intent != null) {
                                    intents.add(intent);
                                    break;
                                } else {
                                    continue;
                                }
                            case 1:
                                String category = getAttributeValue(parser, "name");
                                if (!TextUtils.isEmpty(category)) {
                                    categories.add(category);
                                    break;
                                } else {
                                    continue;
                                }
                        }
                    } else if (type == 3 && parser.getName().equals(TAG_TARGET)) {
                    }
                }
            }
            ShortcutInfoCompat.Builder builder = new ShortcutInfoCompat.Builder(context, id).setShortLabel(label).setRank(rank);
            if (!TextUtils.isEmpty(longLabel)) {
                builder.setLongLabel(longLabel);
            }
            if (!TextUtils.isEmpty(disabledMessage)) {
                builder.setDisabledMessage(disabledMessage);
            }
            if (activity != null) {
                builder.setActivity(activity);
            }
            if (!intents.isEmpty()) {
                builder.setIntents((Intent[]) intents.toArray(new Intent[0]));
            }
            if (!categories.isEmpty()) {
                builder.setCategories(categories);
            }
            return new ShortcutContainer(builder.build(), iconResourceName, iconBitmapPath);
        }
        return null;
    }

    private static ComponentName parseComponentName(XmlPullParser parser) {
        String value = getAttributeValue(parser, ATTR_COMPONENT);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return ComponentName.unflattenFromString(value);
    }

    private static Intent parseIntent(XmlPullParser parser) {
        String action = getAttributeValue(parser, ATTR_ACTION);
        String targetPackage = getAttributeValue(parser, ATTR_TARGET_PACKAGE);
        String targetClass = getAttributeValue(parser, ATTR_TARGET_CLASS);
        if (action == null) {
            return null;
        }
        Intent intent = new Intent(action);
        if (!TextUtils.isEmpty(targetPackage) && !TextUtils.isEmpty(targetClass)) {
            intent.setClassName(targetPackage, targetClass);
        }
        return intent;
    }

    private static String getAttributeValue(XmlPullParser parser, String attribute) {
        String value = parser.getAttributeValue("http://schemas.android.com/apk/res/android", attribute);
        if (value == null) {
            return parser.getAttributeValue(null, attribute);
        }
        return value;
    }

    private static void serializeShortcutContainer(XmlSerializer serializer, ShortcutContainer container) throws IOException {
        Intent[] intents;
        serializer.startTag(null, TAG_TARGET);
        ShortcutInfoCompat shortcut = container.mShortcutInfo;
        serializeAttribute(serializer, "id", shortcut.getId());
        serializeAttribute(serializer, ATTR_SHORT_LABEL, shortcut.getShortLabel().toString());
        serializeAttribute(serializer, ATTR_RANK, Integer.toString(shortcut.getRank()));
        if (!TextUtils.isEmpty(shortcut.getLongLabel())) {
            serializeAttribute(serializer, ATTR_LONG_LABEL, shortcut.getLongLabel().toString());
        }
        if (!TextUtils.isEmpty(shortcut.getDisabledMessage())) {
            serializeAttribute(serializer, ATTR_DISABLED_MSG, shortcut.getDisabledMessage().toString());
        }
        if (shortcut.getActivity() != null) {
            serializeAttribute(serializer, ATTR_COMPONENT, shortcut.getActivity().flattenToString());
        }
        if (!TextUtils.isEmpty(container.mResourceName)) {
            serializeAttribute(serializer, ATTR_ICON_RES_NAME, container.mResourceName);
        }
        if (!TextUtils.isEmpty(container.mBitmapPath)) {
            serializeAttribute(serializer, ATTR_ICON_BMP_PATH, container.mBitmapPath);
        }
        for (Intent intent : shortcut.getIntents()) {
            serializeIntent(serializer, intent);
        }
        for (String category : shortcut.getCategories()) {
            serializeCategory(serializer, category);
        }
        serializer.endTag(null, TAG_TARGET);
    }

    private static void serializeIntent(XmlSerializer serializer, Intent intent) throws IOException {
        serializer.startTag(null, TAG_INTENT);
        serializeAttribute(serializer, ATTR_ACTION, intent.getAction());
        if (intent.getComponent() != null) {
            serializeAttribute(serializer, ATTR_TARGET_PACKAGE, intent.getComponent().getPackageName());
            serializeAttribute(serializer, ATTR_TARGET_CLASS, intent.getComponent().getClassName());
        }
        serializer.endTag(null, TAG_INTENT);
    }

    private static void serializeCategory(XmlSerializer serializer, String category) throws IOException {
        if (TextUtils.isEmpty(category)) {
            return;
        }
        serializer.startTag(null, TAG_CATEGORY);
        serializeAttribute(serializer, "name", category);
        serializer.endTag(null, TAG_CATEGORY);
    }

    private static void serializeAttribute(XmlSerializer serializer, String attribute, String value) throws IOException {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        serializer.attribute(null, attribute, value);
    }
}
