package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.LongSparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.WallpaperCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.WallpaperUpdater;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes4.dex */
public class WallpapersListActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ArrayList actionModeViews;
    private ColorWallpaper addedColorWallpaper;
    private FileWallpaper addedFileWallpaper;
    private ArrayList allWallPapers;
    private HashMap allWallPapersDict;
    private ColorWallpaper catsWallpaper;
    private Paint colorFramePaint;
    private Paint colorPaint;
    private int columnsCount;
    private int currentType;
    private final long dialogId;
    private int galleryHintRow;
    private int galleryRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private HashMap localDict;
    private ArrayList localWallPapers;
    private ArrayList patterns;
    private HashMap patternsDict;
    private AlertDialog progressDialog;
    private int resetInfoRow;
    private int resetRow;
    private int resetSectionRow;
    private int rowCount;
    private boolean scrolling;
    private SearchAdapter searchAdapter;
    private EmptyTextProgressView searchEmptyView;
    private ActionBarMenuItem searchItem;
    private int sectionRow;
    private boolean selectedBackgroundBlurred;
    private boolean selectedBackgroundMotion;
    private String selectedBackgroundSlug;
    private int selectedColor;
    private int selectedGradientColor1;
    private int selectedGradientColor2;
    private int selectedGradientColor3;
    private int selectedGradientRotation;
    private float selectedIntensity;
    private NumberTextView selectedMessagesCountTextView;
    private final LongSparseArray selectedWallPapers;
    private int setColorRow;
    private FileWallpaper themeWallpaper;
    private int totalWallpaperRows;
    private WallpaperUpdater updater;
    private int uploadImageRow;
    private int wallPaperStartRow;
    private ArrayList wallPapers;
    private static final int[][] defaultColorsLight = {new int[]{-2368069, -9722489, -2762611, -7817084}, new int[]{-7487253, -4599318, -3755537, -1320977}, new int[]{-6832405, -5117462, -3755537, -1067044}, new int[]{-7676942, -7827988, -1859606, -9986835}, new int[]{-5190165, -6311702, -4461867, -5053475}, new int[]{-2430264, -6114049, -1258497, -4594945}, new int[]{-2298990, -7347754, -9985038, -8006011}, new int[]{-1399954, -990074, -876865, -1523602}, new int[]{-15438, -1916673, -6222, -471346}, new int[]{-2891798}, new int[]{-5913125}, new int[]{-9463352}, new int[]{-2956375}, new int[]{-5974898}, new int[]{-8537234}, new int[]{-1647186}, new int[]{-2769263}, new int[]{-3431303}, new int[]{-1326919}, new int[]{-2054243}, new int[]{-3573648}, new int[]{-1328696}, new int[]{-2056777}, new int[]{-2984557}, new int[]{-2440467}, new int[]{-2906649}, new int[]{-4880430}, new int[]{-4013331}, new int[]{-5921305}, new int[]{-8421424}, new int[]{-4005139}, new int[]{-5908761}, new int[]{-8406320}, new int[]{-2702663}, new int[]{-6518654}, new int[]{-16777216}};
    private static final int[][] defaultColorsDark = {new int[]{-14797481, -15394250, -14924974, -14006975}, new int[]{-14867905, -14870478, -14997181, -15460815}, new int[]{-14666695, -15720408, -14861254, -15260107}, new int[]{-14932175, -15066075, -14208965, -15000799}, new int[]{-12968902, -14411460, -13029826, -15067598}, new int[]{-13885157, -12307670, -14542561, -12899018}, new int[]{-14797481, -15196106, -14924974, -15325638}, new int[]{-15658442, -15449521, -16047308, -12897955}, new int[]{-13809610, -15258855, -13221071, -15715791}, new int[]{-14865092}, new int[]{-15656154}, new int[]{-16051170}, new int[]{-14731745}, new int[]{-15524075}, new int[]{-15853808}, new int[]{-13685209}, new int[]{-14014945}, new int[]{-15132649}, new int[]{-12374480}, new int[]{-13755362}, new int[]{-14740716}, new int[]{-12374468}, new int[]{-13755352}, new int[]{-14740709}, new int[]{-12833213}, new int[]{-14083026}, new int[]{-14872031}, new int[]{-13554109}, new int[]{-14803922}, new int[]{-15461855}, new int[]{-13680833}, new int[]{-14602960}, new int[]{-15458784}, new int[]{-14211804}, new int[]{-15132906}, new int[]{-16777216}};
    private static final int[] searchColors = {-16746753, -65536, -30208, -13824, -16718798, -14702165, -9240406, -409915, -9224159, -16777216, -10725281, -1};
    private static final String[] searchColorsNames = {"Blue", "Red", "Orange", "Yellow", "Green", "Teal", "Purple", "Pink", "Brown", "Black", "Gray", "White"};
    private static final int[] searchColorsNamesR = {R.string.Blue, R.string.Red, R.string.Orange, R.string.Yellow, R.string.Green, R.string.Teal, R.string.Purple, R.string.Pink, R.string.Brown, R.string.Black, R.string.Gray, R.string.White};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 1 implements WallpaperUpdater.WallpaperUpdaterDelegate {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectWallpaper$0(TLRPC.WallPaper wallPaper) {
            WallpapersListActivity.this.removeSelfFromStack();
        }

        @Override // org.telegram.ui.Components.WallpaperUpdater.WallpaperUpdaterDelegate
        public void didSelectWallpaper(File file, Bitmap bitmap, boolean z) {
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(new FileWallpaper("", file, file), bitmap);
            themePreviewActivity.setDialogId(WallpapersListActivity.this.dialogId);
            if (WallpapersListActivity.this.dialogId != 0) {
                themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.WallpapersListActivity$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                    public final void didSetNewBackground(TLRPC.WallPaper wallPaper) {
                        WallpapersListActivity.1.this.lambda$didSelectWallpaper$0(wallPaper);
                    }
                });
            }
            WallpapersListActivity.this.presentFragment(themePreviewActivity, z);
        }

        @Override // org.telegram.ui.Components.WallpaperUpdater.WallpaperUpdaterDelegate
        public void needOpenColorPicker() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 extends ActionBar.ActionBarMenuOnItemClick {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(int[] iArr) {
            int i = iArr[0] - 1;
            iArr[0] = i;
            if (i == 0) {
                WallpapersListActivity.this.loadWallpapers(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1(final int[] iArr, TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    WallpapersListActivity.2.this.lambda$onItemClick$0(iArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$onItemClick$2(DialogInterface dialogInterface, int i) {
            TLRPC.TL_inputWallPaper tL_inputWallPaper;
            WallpapersListActivity.this.progressDialog = new AlertDialog(WallpapersListActivity.this.getParentActivity(), 3);
            WallpapersListActivity.this.progressDialog.setCanCancel(false);
            WallpapersListActivity.this.progressDialog.show();
            new ArrayList();
            final int[] iArr = {0};
            for (int i2 = 0; i2 < WallpapersListActivity.this.selectedWallPapers.size(); i2++) {
                Object valueAt = WallpapersListActivity.this.selectedWallPapers.valueAt(i2);
                if (valueAt instanceof ColorWallpaper) {
                    ColorWallpaper colorWallpaper = (ColorWallpaper) valueAt;
                    TLRPC.WallPaper wallPaper = colorWallpaper.parentWallpaper;
                    if (wallPaper == null || wallPaper.id >= 0) {
                        valueAt = wallPaper;
                    } else {
                        WallpapersListActivity.this.getMessagesStorage().deleteWallpaper(colorWallpaper.parentWallpaper.id);
                        WallpapersListActivity.this.localWallPapers.remove(colorWallpaper);
                        WallpapersListActivity.this.localDict.remove(colorWallpaper.getHash());
                    }
                }
                if (valueAt instanceof TLRPC.WallPaper) {
                    iArr[0] = iArr[0] + 1;
                    TLRPC.WallPaper wallPaper2 = (TLRPC.WallPaper) valueAt;
                    TLRPC.TL_account_saveWallPaper tL_account_saveWallPaper = new TLRPC.TL_account_saveWallPaper();
                    tL_account_saveWallPaper.settings = new TLRPC.TL_wallPaperSettings();
                    tL_account_saveWallPaper.unsave = true;
                    if (valueAt instanceof TLRPC.TL_wallPaperNoFile) {
                        TLRPC.TL_inputWallPaperNoFile tL_inputWallPaperNoFile = new TLRPC.TL_inputWallPaperNoFile();
                        tL_inputWallPaperNoFile.id = wallPaper2.id;
                        tL_inputWallPaper = tL_inputWallPaperNoFile;
                    } else {
                        TLRPC.TL_inputWallPaper tL_inputWallPaper2 = new TLRPC.TL_inputWallPaper();
                        tL_inputWallPaper2.id = wallPaper2.id;
                        tL_inputWallPaper2.access_hash = wallPaper2.access_hash;
                        tL_inputWallPaper = tL_inputWallPaper2;
                    }
                    tL_account_saveWallPaper.wallpaper = tL_inputWallPaper;
                    String str = wallPaper2.slug;
                    if (str != null && str.equals(WallpapersListActivity.this.selectedBackgroundSlug)) {
                        WallpapersListActivity.this.selectedBackgroundSlug = Theme.hasWallpaperFromTheme() ? "t" : "d";
                        Theme.getActiveTheme().setOverrideWallpaper(null);
                        Theme.reloadWallpaper(true);
                    }
                    ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendRequest(tL_account_saveWallPaper, new RequestDelegate() { // from class: org.telegram.ui.WallpapersListActivity$2$$ExternalSyntheticLambda2
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            WallpapersListActivity.2.this.lambda$onItemClick$1(iArr, tLObject, tL_error);
                        }
                    });
                }
            }
            if (iArr[0] == 0) {
                WallpapersListActivity.this.loadWallpapers(true);
            }
            WallpapersListActivity.this.selectedWallPapers.clear();
            ((BaseFragment) WallpapersListActivity.this).actionBar.hideActionMode();
            ((BaseFragment) WallpapersListActivity.this).actionBar.closeSearchField();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onItemClick$3(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
            long j;
            String url;
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < WallpapersListActivity.this.selectedWallPapers.size(); i2++) {
                Object valueAt = WallpapersListActivity.this.selectedWallPapers.valueAt(i2);
                if (valueAt instanceof TLRPC.TL_wallPaper) {
                    url = AndroidUtilities.getWallPaperUrl(valueAt);
                } else if (valueAt instanceof ColorWallpaper) {
                    url = ((ColorWallpaper) valueAt).getUrl();
                }
                if (!TextUtils.isEmpty(url)) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(url);
                }
            }
            WallpapersListActivity.this.selectedWallPapers.clear();
            ((BaseFragment) WallpapersListActivity.this).actionBar.hideActionMode();
            ((BaseFragment) WallpapersListActivity.this).actionBar.closeSearchField();
            if (arrayList.size() > 1 || ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId == UserConfig.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).getClientUserId() || charSequence != null) {
                WallpapersListActivity.this.updateRowsSelection();
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    long j2 = ((MessagesStorage.TopicKey) arrayList.get(i3)).dialogId;
                    if (charSequence != null) {
                        j = j2;
                        SendMessagesHelper.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(charSequence.toString(), j2, null, null, null, true, null, null, null, true, 0, null, false));
                    } else {
                        j = j2;
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        SendMessagesHelper.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(sb.toString(), j, null, null, null, true, null, null, null, true, 0, null, false));
                    }
                }
                dialogsActivity.finishFragment();
            } else {
                long j3 = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (DialogObject.isEncryptedDialog(j3)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j3));
                } else {
                    if (DialogObject.isUserDialog(j3)) {
                        bundle.putLong("user_id", j3);
                    } else if (DialogObject.isChatDialog(j3)) {
                        bundle.putLong("chat_id", -j3);
                    }
                    if (!MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
                        return true;
                    }
                }
                NotificationCenter.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                WallpapersListActivity.this.presentFragment(new ChatActivity(bundle), true);
                SendMessagesHelper.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(sb.toString(), j3, null, null, null, true, null, null, null, true, 0, null, false));
            }
            return true;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!((BaseFragment) WallpapersListActivity.this).actionBar.isActionModeShowed()) {
                    WallpapersListActivity.this.finishFragment();
                    return;
                }
                WallpapersListActivity.this.selectedWallPapers.clear();
                ((BaseFragment) WallpapersListActivity.this).actionBar.hideActionMode();
                WallpapersListActivity.this.updateRowsSelection();
            } else if (i != 4) {
                if (i == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 3);
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.WallpapersListActivity$2$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
                            boolean lambda$onItemClick$3;
                            lambda$onItemClick$3 = WallpapersListActivity.2.this.lambda$onItemClick$3(dialogsActivity2, arrayList, charSequence, z, z2, i2, topicsFragment);
                            return lambda$onItemClick$3;
                        }
                    });
                    WallpapersListActivity.this.presentFragment(dialogsActivity);
                }
            } else if (WallpapersListActivity.this.getParentActivity() == null) {
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WallpapersListActivity.this.getParentActivity());
                builder.setTitle(LocaleController.formatPluralString("DeleteBackground", WallpapersListActivity.this.selectedWallPapers.size(), new Object[0]));
                builder.setMessage(LocaleController.formatString("DeleteChatBackgroundsAlert", R.string.DeleteChatBackgroundsAlert, new Object[0]));
                builder.setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.WallpapersListActivity$2$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        WallpapersListActivity.2.this.lambda$onItemClick$2(dialogInterface, i2);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                AlertDialog create = builder.create();
                WallpapersListActivity.this.showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    private class ColorCell extends View {
        private int color;

        public ColorCell(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            WallpapersListActivity.this.colorPaint.setColor(this.color);
            canvas.drawCircle(AndroidUtilities.dp(25.0f), AndroidUtilities.dp(31.0f), AndroidUtilities.dp(18.0f), WallpapersListActivity.this.colorPaint);
            if (this.color == Theme.getColor(Theme.key_windowBackgroundWhite)) {
                canvas.drawCircle(AndroidUtilities.dp(25.0f), AndroidUtilities.dp(31.0f), AndroidUtilities.dp(18.0f), WallpapersListActivity.this.colorFramePaint);
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(AndroidUtilities.dp(50.0f), AndroidUtilities.dp(62.0f));
        }

        public void setColor(int i) {
            this.color = i;
        }
    }

    /* loaded from: classes4.dex */
    public static class ColorWallpaper {
        public int color;
        public Bitmap defaultCache;
        public int gradientColor1;
        public int gradientColor2;
        public int gradientColor3;
        public int gradientRotation;
        public float intensity;
        public boolean isGradient;
        public boolean motion;
        public TLRPC.WallPaper parentWallpaper;
        public File path;
        public TLRPC.TL_wallPaper pattern;
        public long patternId;
        public String slug;

        public ColorWallpaper(String str, int i, int i2, int i3) {
            this.slug = str;
            this.color = i | (-16777216);
            int i4 = i2 == 0 ? 0 : (-16777216) | i2;
            this.gradientColor1 = i4;
            this.gradientRotation = i4 == 0 ? 0 : i3;
            this.intensity = 1.0f;
        }

        public ColorWallpaper(String str, int i, int i2, int i3, int i4) {
            this.slug = str;
            this.color = i | (-16777216);
            this.gradientColor1 = i2 == 0 ? 0 : i2 | (-16777216);
            this.gradientColor2 = i3 == 0 ? 0 : i3 | (-16777216);
            this.gradientColor3 = i4 != 0 ? i4 | (-16777216) : 0;
            this.intensity = 1.0f;
            this.isGradient = true;
        }

        public ColorWallpaper(String str, int i, int i2, int i3, int i4, int i5, float f, boolean z, File file) {
            this.slug = str;
            this.color = i | (-16777216);
            int i6 = i2 == 0 ? 0 : i2 | (-16777216);
            this.gradientColor1 = i6;
            this.gradientColor2 = i3 == 0 ? 0 : i3 | (-16777216);
            this.gradientColor3 = i4 != 0 ? i4 | (-16777216) : 0;
            this.gradientRotation = i6 == 0 ? 45 : i5;
            this.intensity = f;
            this.path = file;
            this.motion = z;
        }

        public String getHash() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(this.color));
            sb.append(this.gradientColor1);
            sb.append(this.gradientColor2);
            sb.append(this.gradientColor3);
            sb.append(this.gradientRotation);
            sb.append(this.intensity);
            String str = this.slug;
            if (str == null) {
                str = "";
            }
            sb.append(str);
            return Utilities.MD5(sb.toString());
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x014b  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0199  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String getUrl() {
            StringBuilder sb;
            String str;
            int i = this.gradientColor1;
            String lowerCase = i != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i >> 16)) & 255), Integer.valueOf(((byte) (this.gradientColor1 >> 8)) & 255), Byte.valueOf((byte) (this.gradientColor1 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            String lowerCase2 = String.format("%02x%02x%02x", Integer.valueOf(((byte) (this.color >> 16)) & 255), Integer.valueOf(((byte) (this.color >> 8)) & 255), Byte.valueOf((byte) (this.color & NotificationCenter.closeSearchByActiveAction))).toLowerCase();
            int i2 = this.gradientColor2;
            String lowerCase3 = i2 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i2 >> 16)) & 255), Integer.valueOf(((byte) (this.gradientColor2 >> 8)) & 255), Byte.valueOf((byte) (this.gradientColor2 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            int i3 = this.gradientColor3;
            String lowerCase4 = i3 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i3 >> 16)) & 255), Integer.valueOf(((byte) (this.gradientColor3 >> 8)) & 255), Byte.valueOf((byte) (this.gradientColor3 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            if (lowerCase == null || lowerCase3 == null) {
                if (lowerCase != null) {
                    String str2 = lowerCase2 + "-" + lowerCase;
                    if (this.pattern != null) {
                        sb = new StringBuilder();
                        sb.append(str2);
                        str = "&rotation=";
                    } else {
                        sb = new StringBuilder();
                        sb.append(str2);
                        str = "?rotation=";
                    }
                    sb.append(str);
                    sb.append(AndroidUtilities.getWallpaperRotation(this.gradientRotation, true));
                }
                if (this.pattern != null) {
                    return "https://" + MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix + "/bg/" + lowerCase2;
                }
                String str3 = "https://" + MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix + "/bg/" + this.pattern.slug + "?intensity=" + ((int) (this.intensity * 100.0f)) + "&bg_color=" + lowerCase2;
                if (this.motion) {
                    return str3 + "&mode=motion";
                }
                return str3;
            } else if (lowerCase4 != null) {
                sb = new StringBuilder();
                sb.append(lowerCase2);
                sb.append("~");
                sb.append(lowerCase);
                sb.append("~");
                sb.append(lowerCase3);
                sb.append("~");
                sb.append(lowerCase4);
            } else {
                sb = new StringBuilder();
                sb.append(lowerCase2);
                sb.append("~");
                sb.append(lowerCase);
                sb.append("~");
                sb.append(lowerCase3);
            }
            lowerCase2 = sb.toString();
            if (this.pattern != null) {
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class FileWallpaper {
        public File originalPath;
        public File path;
        public int resId;
        public String slug;
        public int thumbResId;

        public FileWallpaper(String str, int i, int i2) {
            this.slug = str;
            this.resId = i;
            this.thumbResId = i2;
        }

        public FileWallpaper(String str, File file, File file2) {
            this.slug = str;
            this.path = file;
            this.originalPath = file2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return WallpapersListActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == WallpapersListActivity.this.uploadImageRow || i == WallpapersListActivity.this.galleryRow || i == WallpapersListActivity.this.setColorRow || i == WallpapersListActivity.this.resetRow) {
                return 0;
            }
            if (i == WallpapersListActivity.this.sectionRow || i == WallpapersListActivity.this.resetSectionRow) {
                return 1;
            }
            return (i == WallpapersListActivity.this.resetInfoRow || i == WallpapersListActivity.this.galleryHintRow) ? 3 : 2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        /* JADX WARN: Code restructure failed: missing block: B:56:0x0139, code lost:
            if (r4 > 0) goto L57;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x01e6, code lost:
            if (r3 != null) goto L57;
         */
        /* JADX WARN: Removed duplicated region for block: B:104:0x021d  */
        /* JADX WARN: Removed duplicated region for block: B:109:0x0237  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            int i2;
            long j;
            TLRPC.WallPaper wallPaper;
            String str;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                if (i == WallpapersListActivity.this.uploadImageRow) {
                    string = LocaleController.getString(R.string.SelectFromGallery);
                    i2 = R.drawable.msg_photos;
                } else if (i != WallpapersListActivity.this.setColorRow) {
                    if (i == WallpapersListActivity.this.resetRow) {
                        textCell.setText(LocaleController.getString(R.string.ResetChatBackgrounds), false);
                        return;
                    } else if (i == WallpapersListActivity.this.galleryRow) {
                        textCell.setTextAndIcon((CharSequence) "Choose from gallery", R.drawable.msg_background, false);
                        textCell.setLockLevel(false, 10);
                        return;
                    } else {
                        return;
                    }
                } else {
                    string = LocaleController.getString(R.string.SetColor);
                    i2 = R.drawable.msg_palette;
                }
                textCell.setTextAndIcon((CharSequence) string, i2, true);
            } else if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == WallpapersListActivity.this.resetInfoRow) {
                    str = LocaleController.getString(R.string.ResetChatBackgroundsInfo);
                } else if (i != WallpapersListActivity.this.galleryHintRow) {
                    return;
                } else {
                    str = "Upload your own background for the channel.";
                }
                textInfoPrivacyCell.setText(str);
            } else {
                WallpaperCell wallpaperCell = (WallpaperCell) viewHolder.itemView;
                int i3 = (i - WallpapersListActivity.this.wallPaperStartRow) * WallpapersListActivity.this.columnsCount;
                wallpaperCell.setParams(WallpapersListActivity.this.columnsCount, i3 == 0, i3 / WallpapersListActivity.this.columnsCount == WallpapersListActivity.this.totalWallpaperRows - 1);
                for (int i4 = 0; i4 < WallpapersListActivity.this.columnsCount; i4++) {
                    int i5 = i3 + i4;
                    FileWallpaper fileWallpaper = null;
                    TLRPC.WallPaper wallPaper2 = i5 < WallpapersListActivity.this.wallPapers.size() ? WallpapersListActivity.this.wallPapers.get(i5) : null;
                    if (wallPaper2 instanceof TLRPC.TL_wallPaper) {
                        TLRPC.WallPaper wallPaper3 = (TLRPC.TL_wallPaper) wallPaper2;
                        Theme.OverrideWallpaperInfo overrideWallpaperInfo = Theme.getActiveTheme().overrideWallpaper;
                        boolean equals = WallpapersListActivity.this.selectedBackgroundSlug.equals(wallPaper3.slug);
                        wallPaper = wallPaper3;
                        if (equals) {
                            if (WallpapersListActivity.this.selectedBackgroundSlug.equals(wallPaper3.slug) && wallPaper3.settings != null) {
                                int i6 = WallpapersListActivity.this.selectedColor;
                                int wallpaperColor = Theme.getWallpaperColor(wallPaper3.settings.background_color);
                                wallPaper = wallPaper3;
                                if (i6 == wallpaperColor) {
                                    int i7 = WallpapersListActivity.this.selectedGradientColor1;
                                    int wallpaperColor2 = Theme.getWallpaperColor(wallPaper3.settings.second_background_color);
                                    wallPaper = wallPaper3;
                                    if (i7 == wallpaperColor2) {
                                        int i8 = WallpapersListActivity.this.selectedGradientColor2;
                                        int wallpaperColor3 = Theme.getWallpaperColor(wallPaper3.settings.third_background_color);
                                        wallPaper = wallPaper3;
                                        if (i8 == wallpaperColor3) {
                                            int i9 = WallpapersListActivity.this.selectedGradientColor3;
                                            int wallpaperColor4 = Theme.getWallpaperColor(wallPaper3.settings.fourth_background_color);
                                            wallPaper = wallPaper3;
                                            if (i9 == wallpaperColor4) {
                                                if (WallpapersListActivity.this.selectedGradientColor1 != 0 && WallpapersListActivity.this.selectedGradientColor2 == 0 && WallpapersListActivity.this.selectedGradientRotation != AndroidUtilities.getWallpaperRotation(wallPaper3.settings.rotation, false) && wallPaper3.pattern) {
                                                    int i10 = (Math.abs(Theme.getThemeIntensity(wallPaper3.settings.intensity / 100.0f) - WallpapersListActivity.this.selectedIntensity) > 0.001f ? 1 : (Math.abs(Theme.getThemeIntensity(wallPaper3.settings.intensity / 100.0f) - WallpapersListActivity.this.selectedIntensity) == 0.001f ? 0 : -1));
                                                    wallPaper = wallPaper3;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            fileWallpaper = wallPaper3;
                            wallPaper = wallPaper3;
                        }
                    } else {
                        j = 0;
                        if (wallPaper2 instanceof ColorWallpaper) {
                            ColorWallpaper colorWallpaper = (ColorWallpaper) wallPaper2;
                            if (("d".equals(colorWallpaper.slug) && WallpapersListActivity.this.selectedBackgroundSlug != null && WallpapersListActivity.this.selectedBackgroundSlug.equals(colorWallpaper.slug)) || (colorWallpaper.color == WallpapersListActivity.this.selectedColor && colorWallpaper.gradientColor1 == WallpapersListActivity.this.selectedGradientColor1 && colorWallpaper.gradientColor2 == WallpapersListActivity.this.selectedGradientColor2 && colorWallpaper.gradientColor3 == WallpapersListActivity.this.selectedGradientColor3 && ((WallpapersListActivity.this.selectedGradientColor1 == 0 || colorWallpaper.gradientRotation == WallpapersListActivity.this.selectedGradientRotation) && ((!"c".equals(WallpapersListActivity.this.selectedBackgroundSlug) || colorWallpaper.slug == null) && ("c".equals(WallpapersListActivity.this.selectedBackgroundSlug) || (TextUtils.equals(WallpapersListActivity.this.selectedBackgroundSlug, colorWallpaper.slug) && ((int) (colorWallpaper.intensity * 100.0f)) == ((int) (WallpapersListActivity.this.selectedIntensity * 100.0f)))))))) {
                                fileWallpaper = wallPaper2;
                            }
                            TLRPC.WallPaper wallPaper4 = colorWallpaper.parentWallpaper;
                            wallPaper = wallPaper4;
                        } else if ((wallPaper2 instanceof FileWallpaper) && WallpapersListActivity.this.selectedBackgroundSlug.equals(wallPaper2.slug)) {
                            fileWallpaper = wallPaper2;
                        }
                        long j2 = j;
                        wallpaperCell.setWallpaper(WallpapersListActivity.this.currentType, i4, wallPaper2, fileWallpaper, null, false);
                        if (((BaseFragment) WallpapersListActivity.this).actionBar.isActionModeShowed()) {
                            wallpaperCell.setChecked(i4, false, !WallpapersListActivity.this.scrolling);
                        } else {
                            wallpaperCell.setChecked(i4, WallpapersListActivity.this.selectedWallPapers.indexOfKey(j2) >= 0, !WallpapersListActivity.this.scrolling);
                        }
                    }
                    j = wallPaper.id;
                    long j22 = j;
                    wallpaperCell.setWallpaper(WallpapersListActivity.this.currentType, i4, wallPaper2, fileWallpaper, null, false);
                    if (((BaseFragment) WallpapersListActivity.this).actionBar.isActionModeShowed()) {
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textCell;
            View shadowSectionCell;
            CombinedDrawable combinedDrawable;
            if (i != 0) {
                if (i == 1) {
                    shadowSectionCell = new ShadowSectionCell(this.mContext);
                    combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, WallpapersListActivity.this.wallPaperStartRow == -1 ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                } else if (i != 3) {
                    textCell = new WallpaperCell(this.mContext) { // from class: org.telegram.ui.WallpapersListActivity.ListAdapter.1
                        @Override // org.telegram.ui.Cells.WallpaperCell
                        protected void onWallpaperClick(Object obj, int i2) {
                            WallpapersListActivity.this.onItemClick(this, obj, i2);
                        }

                        @Override // org.telegram.ui.Cells.WallpaperCell
                        protected boolean onWallpaperLongClick(Object obj, int i2) {
                            return WallpapersListActivity.this.onItemLongClick(this, obj, i2);
                        }
                    };
                } else {
                    shadowSectionCell = new TextInfoPrivacyCell(this.mContext);
                    combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                }
                combinedDrawable.setFullsize(true);
                shadowSectionCell.setBackgroundDrawable(combinedDrawable);
                textCell = shadowSectionCell;
            } else {
                textCell = new TextCell(this.mContext);
            }
            return new RecyclerListView.Holder(textCell);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private int imageReqId;
        private RecyclerListView innerListView;
        private String lastSearchImageString;
        private String lastSearchString;
        private int lastSearchToken;
        private Context mContext;
        private String nextImagesSearchOffset;
        private Runnable searchRunnable;
        private boolean searchingUser;
        private String selectedColor;
        private ArrayList searchResult = new ArrayList();
        private HashMap searchResultKeys = new HashMap();
        private boolean bingSearchEndReached = true;

        /* loaded from: classes4.dex */
        private class CategoryAdapterRecycler extends RecyclerListView.SelectionAdapter {
            private CategoryAdapterRecycler() {
            }

            /* synthetic */ CategoryAdapterRecycler(SearchAdapter searchAdapter, 1 r2) {
                this();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return WallpapersListActivity.searchColors.length;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((ColorCell) viewHolder.itemView).setColor(WallpapersListActivity.searchColors[i]);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                SearchAdapter searchAdapter = SearchAdapter.this;
                return new RecyclerListView.Holder(new ColorCell(searchAdapter.mContext));
            }
        }

        public SearchAdapter(Context context) {
            this.mContext = context;
        }

        private void doSearch(String str) {
            this.searchResult.clear();
            this.searchResultKeys.clear();
            this.bingSearchEndReached = true;
            searchImages(str, "", true);
            this.lastSearchString = str;
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$5(View view, int i) {
            String string = LocaleController.getString(R.string.BackgroundSearchColor);
            SpannableString spannableString = new SpannableString(string + " " + LocaleController.getString(WallpapersListActivity.searchColorsNames[i], WallpapersListActivity.searchColorsNamesR[i]));
            spannableString.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_actionBarDefaultSubtitle)), string.length(), spannableString.length(), 33);
            WallpapersListActivity.this.searchItem.setSearchFieldCaption(spannableString);
            WallpapersListActivity.this.searchItem.setSearchFieldHint(null);
            WallpapersListActivity.this.searchItem.setSearchFieldText("", true);
            this.selectedColor = WallpapersListActivity.searchColorsNames[i];
            processSearch("", true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$0(String str) {
            doSearch(str);
            this.searchRunnable = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchBotUser$1(TLObject tLObject) {
            TLRPC.TL_contacts_resolvedPeer tL_contacts_resolvedPeer = (TLRPC.TL_contacts_resolvedPeer) tLObject;
            MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).putUsers(tL_contacts_resolvedPeer.users, false);
            MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).putChats(tL_contacts_resolvedPeer.chats, false);
            WallpapersListActivity.this.getMessagesStorage().putUsersAndChats(tL_contacts_resolvedPeer.users, tL_contacts_resolvedPeer.chats, true, true);
            String str = this.lastSearchImageString;
            this.lastSearchImageString = null;
            searchImages(str, "", false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchBotUser$2(final TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        WallpapersListActivity.SearchAdapter.this.lambda$searchBotUser$1(tLObject);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchImages$3(int i, TLObject tLObject) {
            if (i != this.lastSearchToken) {
                return;
            }
            boolean z = false;
            this.imageReqId = 0;
            int size = this.searchResult.size();
            if (tLObject != null) {
                TLRPC.messages_BotResults messages_botresults = (TLRPC.messages_BotResults) tLObject;
                this.nextImagesSearchOffset = messages_botresults.next_offset;
                int size2 = messages_botresults.results.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC.BotInlineResult botInlineResult = messages_botresults.results.get(i2);
                    if ("photo".equals(botInlineResult.type) && !this.searchResultKeys.containsKey(botInlineResult.id)) {
                        MediaController.SearchImage searchImage = new MediaController.SearchImage();
                        TLRPC.Photo photo = botInlineResult.photo;
                        if (photo != null) {
                            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
                            TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(botInlineResult.photo.sizes, 320);
                            if (closestPhotoSizeWithSize != null) {
                                searchImage.width = closestPhotoSizeWithSize.w;
                                searchImage.height = closestPhotoSizeWithSize.h;
                                searchImage.photoSize = closestPhotoSizeWithSize;
                                searchImage.photo = botInlineResult.photo;
                                searchImage.size = closestPhotoSizeWithSize.size;
                                searchImage.thumbPhotoSize = closestPhotoSizeWithSize2;
                                searchImage.id = botInlineResult.id;
                                searchImage.type = 0;
                                this.searchResult.add(searchImage);
                                this.searchResultKeys.put(searchImage.id, searchImage);
                            }
                        } else if (botInlineResult.content != null) {
                            int i3 = 0;
                            while (true) {
                                if (i3 >= botInlineResult.content.attributes.size()) {
                                    break;
                                }
                                TLRPC.DocumentAttribute documentAttribute = botInlineResult.content.attributes.get(i3);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                                    searchImage.width = documentAttribute.w;
                                    searchImage.height = documentAttribute.h;
                                    break;
                                }
                                i3++;
                            }
                            TLRPC.WebDocument webDocument = botInlineResult.thumb;
                            searchImage.thumbUrl = webDocument != null ? webDocument.url : null;
                            TLRPC.WebDocument webDocument2 = botInlineResult.content;
                            searchImage.imageUrl = webDocument2.url;
                            searchImage.size = webDocument2.size;
                            searchImage.id = botInlineResult.id;
                            searchImage.type = 0;
                            this.searchResult.add(searchImage);
                            this.searchResultKeys.put(searchImage.id, searchImage);
                        }
                    }
                }
                this.bingSearchEndReached = (size == this.searchResult.size() || this.nextImagesSearchOffset == null) ? true : true;
            }
            if (size != this.searchResult.size()) {
                int i4 = size % WallpapersListActivity.this.columnsCount;
                float f = size;
                int ceil = (int) Math.ceil(f / WallpapersListActivity.this.columnsCount);
                if (i4 != 0) {
                    notifyItemChanged(((int) Math.ceil(f / WallpapersListActivity.this.columnsCount)) - 1);
                }
                WallpapersListActivity.this.searchAdapter.notifyItemRangeInserted(ceil, ((int) Math.ceil(this.searchResult.size() / WallpapersListActivity.this.columnsCount)) - ceil);
            }
            WallpapersListActivity.this.searchEmptyView.showTextView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchImages$4(final int i, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    WallpapersListActivity.SearchAdapter.this.lambda$searchImages$3(i, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processSearch(final String str, boolean z) {
            if (str != null && this.selectedColor != null) {
                str = "#color" + this.selectedColor + " " + str;
            }
            Runnable runnable = this.searchRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty(str)) {
                this.searchResult.clear();
                this.searchResultKeys.clear();
                this.bingSearchEndReached = true;
                this.lastSearchString = null;
                if (this.imageReqId != 0) {
                    ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).cancelRequest(this.imageReqId, true);
                    this.imageReqId = 0;
                }
                WallpapersListActivity.this.searchEmptyView.showTextView();
            } else {
                WallpapersListActivity.this.searchEmptyView.showProgress();
                if (z) {
                    doSearch(str);
                } else {
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            WallpapersListActivity.SearchAdapter.this.lambda$processSearch$0(str);
                        }
                    };
                    this.searchRunnable = runnable2;
                    AndroidUtilities.runOnUIThread(runnable2, 500L);
                }
            }
            notifyDataSetChanged();
        }

        private void searchBotUser() {
            if (this.searchingUser) {
                return;
            }
            this.searchingUser = true;
            TLRPC.TL_contacts_resolveUsername tL_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
            tL_contacts_resolveUsername.username = MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).imageSearchBot;
            ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendRequest(tL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    WallpapersListActivity.SearchAdapter.this.lambda$searchBotUser$2(tLObject, tL_error);
                }
            });
        }

        private void searchImages(String str, String str2, boolean z) {
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
            this.lastSearchImageString = str;
            TLObject userOrChat = MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).getUserOrChat(MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).imageSearchBot);
            if (!(userOrChat instanceof TLRPC.User)) {
                if (z) {
                    searchBotUser();
                    return;
                }
                return;
            }
            TLRPC.TL_messages_getInlineBotResults tL_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
            tL_messages_getInlineBotResults.query = "#wallpaper " + str;
            tL_messages_getInlineBotResults.bot = MessagesController.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).getInputUser((TLRPC.User) userOrChat);
            tL_messages_getInlineBotResults.offset = str2;
            tL_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
            final int i = this.lastSearchToken + 1;
            this.lastSearchToken = i;
            this.imageReqId = ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).sendRequest(tL_messages_getInlineBotResults, new RequestDelegate() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    WallpapersListActivity.SearchAdapter.this.lambda$searchImages$4(i, tLObject, tL_error);
                }
            });
            ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).bindRequestToGuid(this.imageReqId, ((BaseFragment) WallpapersListActivity.this).classGuid);
        }

        public void clearColor() {
            this.selectedColor = null;
            processSearch(null, true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (TextUtils.isEmpty(this.lastSearchString)) {
                return 2;
            }
            return (int) Math.ceil(this.searchResult.size() / WallpapersListActivity.this.columnsCount);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (TextUtils.isEmpty(this.lastSearchString)) {
                return i == 0 ? 2 : 1;
            }
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 2;
        }

        public void loadMoreResults() {
            if (this.bingSearchEndReached || this.imageReqId != 0) {
                return;
            }
            searchImages(this.lastSearchString, this.nextImagesSearchOffset, true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 2) {
                    return;
                }
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString(R.string.SearchByColor));
                return;
            }
            WallpaperCell wallpaperCell = (WallpaperCell) viewHolder.itemView;
            int i2 = i * WallpapersListActivity.this.columnsCount;
            wallpaperCell.setParams(WallpapersListActivity.this.columnsCount, i2 == 0, i2 / WallpapersListActivity.this.columnsCount == ((int) Math.ceil((double) (((float) this.searchResult.size()) / ((float) WallpapersListActivity.this.columnsCount)))) - 1);
            for (int i3 = 0; i3 < WallpapersListActivity.this.columnsCount; i3++) {
                int i4 = i2 + i3;
                wallpaperCell.setWallpaper(WallpapersListActivity.this.currentType, i3, i4 < this.searchResult.size() ? this.searchResult.get(i4) : null, "", null, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i != 0) {
                view = null;
                if (i == 1) {
                    RecyclerListView recyclerListView = new RecyclerListView(this.mContext) { // from class: org.telegram.ui.WallpapersListActivity.SearchAdapter.2
                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            if (getParent() != null && getParent().getParent() != null) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                    };
                    recyclerListView.setItemAnimator(null);
                    recyclerListView.setLayoutAnimation(null);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext) { // from class: org.telegram.ui.WallpapersListActivity.SearchAdapter.3
                        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                        public boolean supportsPredictiveItemAnimations() {
                            return false;
                        }
                    };
                    recyclerListView.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
                    recyclerListView.setClipToPadding(false);
                    linearLayoutManager.setOrientation(0);
                    recyclerListView.setLayoutManager(linearLayoutManager);
                    recyclerListView.setAdapter(new CategoryAdapterRecycler(this, null));
                    recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view2, int i2) {
                            WallpapersListActivity.SearchAdapter.this.lambda$onCreateViewHolder$5(view2, i2);
                        }
                    });
                    this.innerListView = recyclerListView;
                    view = recyclerListView;
                } else if (i == 2) {
                    view = new GraySectionCell(this.mContext);
                }
            } else {
                view = new WallpaperCell(this.mContext) { // from class: org.telegram.ui.WallpapersListActivity.SearchAdapter.1
                    @Override // org.telegram.ui.Cells.WallpaperCell
                    protected void onWallpaperClick(Object obj, int i2) {
                        WallpapersListActivity.this.presentFragment(new ThemePreviewActivity(obj, null, true, false));
                    }
                };
            }
            view.setLayoutParams(i == 1 ? new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(60.0f)) : new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        public void onDestroy() {
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(((BaseFragment) WallpapersListActivity.this).currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
        }
    }

    public WallpapersListActivity(int i) {
        this(i, 0L);
    }

    public WallpapersListActivity(int i, long j) {
        this.actionModeViews = new ArrayList();
        this.columnsCount = 3;
        this.selectedBackgroundSlug = "";
        this.allWallPapers = new ArrayList();
        this.allWallPapersDict = new HashMap();
        this.localDict = new HashMap();
        this.wallPapers = new ArrayList();
        this.localWallPapers = new ArrayList();
        this.patterns = new ArrayList();
        this.patternsDict = new HashMap();
        this.selectedWallPapers = new LongSparseArray();
        this.currentType = i;
        this.dialogId = j;
    }

    public static void fillDefaultColors(ArrayList arrayList, boolean z) {
        int[][] iArr;
        for (int[] iArr2 : z ? defaultColorsDark : defaultColorsLight) {
            arrayList.add(iArr2.length == 1 ? new ColorWallpaper("c", iArr2[0], 0, 45) : new ColorWallpaper("c", iArr2[0], iArr2[1], iArr2[2], iArr2[3]));
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(26:7|(1:9)|10|(1:12)|13|(1:15)(1:183)|16|(1:18)|19|(2:20|(2:22|(7:24|(1:26)|27|(2:29|(2:33|34))|35|(2:37|(2:39|(2:41|(1:(1:145)(2:146|46))(1:147))(1:148))(1:149))(1:150)|34)(3:151|(2:153|(1:172))(1:180)|34))(2:181|182))|47|(2:49|(16:139|69|70|71|72|73|(1:135)(3:77|(1:79)|80)|81|(6:111|(2:118|(2:120|(2:125|102)(2:124|98))(2:126|(3:130|(1:132)(1:134)|133)))(2:115|(2:117|100))|103|(1:110)(1:107)|108|109)(6:(2:91|(2:101|102)(2:97|98))|103|(1:105)|110|108|109)|99|100|103|(0)|110|108|109)(1:67))(15:140|(1:142)|144|70|71|72|73|(1:75)|135|81|(1:83)|111|(1:113)|118|(0)(0))|68|69|70|71|72|73|(0)|135|81|(0)|111|(0)|118|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01c7, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01c8, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01a1, code lost:
        if (r0 != 0) goto L68;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x028f  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x02f6  */
    /* JADX WARN: Type inference failed for: r0v11, types: [org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v10, types: [org.telegram.ui.WallpapersListActivity$ColorWallpaper] */
    /* JADX WARN: Type inference failed for: r4v9, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void fillWallpapersWithCustom() {
        int i;
        ?? r4;
        String str;
        final String str2;
        Object obj;
        TLRPC.TL_wallPaper tL_wallPaper;
        final long j;
        int i2;
        ColorWallpaper colorWallpaper;
        int i3;
        ColorWallpaper colorWallpaper2;
        int i4;
        ColorWallpaper colorWallpaper3;
        ArrayList arrayList;
        int i5;
        int i6;
        Object obj2;
        String str3;
        TLRPC.TL_wallPaper tL_wallPaper2;
        TLRPC.TL_wallPaper tL_wallPaper3;
        TLRPC.TL_wallPaper tL_wallPaper4;
        int i7 = this.currentType;
        if (i7 != 0 && i7 != 2) {
            return;
        }
        MessagesController.getGlobalMainSettings();
        ColorWallpaper colorWallpaper4 = this.addedColorWallpaper;
        if (colorWallpaper4 != null) {
            this.wallPapers.remove(colorWallpaper4);
            this.addedColorWallpaper = null;
        }
        FileWallpaper fileWallpaper = this.addedFileWallpaper;
        if (fileWallpaper != null) {
            this.wallPapers.remove(fileWallpaper);
            this.addedFileWallpaper = null;
        }
        ColorWallpaper colorWallpaper5 = this.catsWallpaper;
        if (colorWallpaper5 == null) {
            ColorWallpaper colorWallpaper6 = new ColorWallpaper("d", -2368069, -9722489, -2762611, -7817084);
            this.catsWallpaper = colorWallpaper6;
            colorWallpaper6.intensity = 0.34f;
        } else {
            this.wallPapers.remove(colorWallpaper5);
        }
        FileWallpaper fileWallpaper2 = this.themeWallpaper;
        if (fileWallpaper2 != null) {
            this.wallPapers.remove(fileWallpaper2);
        }
        int size = this.wallPapers.size();
        while (true) {
            if (i >= size) {
                r4 = 0;
                break;
            }
            Object obj3 = this.wallPapers.get(i);
            if (obj3 instanceof ColorWallpaper) {
                r4 = (ColorWallpaper) obj3;
                String str4 = r4.slug;
                if (str4 != null) {
                    r4.pattern = (TLRPC.TL_wallPaper) this.allWallPapersDict.get(str4);
                }
                if (!"c".equals(r4.slug)) {
                    String str5 = r4.slug;
                    i = (str5 == null || TextUtils.equals(this.selectedBackgroundSlug, str5)) ? 0 : i + 1;
                }
                if (this.selectedColor == r4.color) {
                    int i8 = this.selectedGradientColor1;
                    if (i8 != r4.gradientColor1) {
                        continue;
                    } else if (this.selectedGradientColor2 != r4.gradientColor2) {
                        continue;
                    } else if (this.selectedGradientColor3 != r4.gradientColor3) {
                        continue;
                    } else if (i8 == 0 || this.selectedGradientRotation == r4.gradientRotation) {
                        break;
                    }
                } else {
                    continue;
                }
            } else {
                if (obj3 instanceof TLRPC.TL_wallPaper) {
                    r4 = (TLRPC.TL_wallPaper) obj3;
                    if (r4.settings != null && TextUtils.equals(this.selectedBackgroundSlug, r4.slug) && this.selectedColor == Theme.getWallpaperColor(r4.settings.background_color) && this.selectedGradientColor1 == Theme.getWallpaperColor(r4.settings.second_background_color) && this.selectedGradientColor2 == Theme.getWallpaperColor(r4.settings.third_background_color) && this.selectedGradientColor3 == Theme.getWallpaperColor(r4.settings.fourth_background_color) && ((this.selectedGradientColor1 == 0 || this.selectedGradientRotation == AndroidUtilities.getWallpaperRotation(r4.settings.rotation, false)) && Math.abs(Theme.getThemeIntensity(r4.settings.intensity / 100.0f) - this.selectedIntensity) <= 0.001f)) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        if (r4 instanceof TLRPC.WallPaper) {
            TLRPC.TL_wallPaper tL_wallPaper5 = r4;
            Theme.OverrideWallpaperInfo overrideWallpaperInfo = Theme.getActiveTheme().overrideWallpaper;
            TLRPC.WallPaperSettings wallPaperSettings = tL_wallPaper5.settings;
            if (wallPaperSettings == null || this.selectedColor != Theme.getWallpaperColor(wallPaperSettings.background_color) || this.selectedGradientColor1 != Theme.getWallpaperColor(tL_wallPaper5.settings.second_background_color) || this.selectedGradientColor2 != Theme.getWallpaperColor(tL_wallPaper5.settings.third_background_color) || this.selectedGradientColor3 != Theme.getWallpaperColor(tL_wallPaper5.settings.fourth_background_color) || (this.selectedGradientColor1 != 0 && this.selectedGradientColor2 == 0 && this.selectedGradientRotation != AndroidUtilities.getWallpaperRotation(tL_wallPaper5.settings.rotation, false) && Math.abs(Theme.getThemeIntensity(tL_wallPaper5.settings.intensity / 100.0f) - this.selectedIntensity) > 0.001f)) {
                str3 = "";
                obj2 = null;
                tL_wallPaper2 = tL_wallPaper5;
                tL_wallPaper3 = tL_wallPaper5;
                tL_wallPaper = tL_wallPaper2;
                obj = obj2;
                j = tL_wallPaper3.id;
                str2 = str3;
                final boolean isDark = Theme.getCurrentTheme().isDark();
                Collections.sort(this.wallPapers, new Comparator() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda3
                    @Override // java.util.Comparator
                    public final int compare(Object obj4, Object obj5) {
                        int lambda$fillWallpapersWithCustom$9;
                        lambda$fillWallpapersWithCustom$9 = WallpapersListActivity.this.lambda$fillWallpapersWithCustom$9(j, str2, isDark, obj4, obj5);
                        return lambda$fillWallpapersWithCustom$9;
                    }
                });
                if (Theme.hasWallpaperFromTheme() || Theme.isThemeWallpaperPublic()) {
                    this.themeWallpaper = null;
                } else {
                    if (this.themeWallpaper == null) {
                        this.themeWallpaper = new FileWallpaper("t", -2, -2);
                    }
                    this.wallPapers.add(0, this.themeWallpaper);
                }
                Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
                if (TextUtils.isEmpty(this.selectedBackgroundSlug) && ("d".equals(this.selectedBackgroundSlug) || obj != null)) {
                    if (obj == null && this.selectedColor != 0 && "c".equals(this.selectedBackgroundSlug)) {
                        int i9 = this.selectedGradientColor1;
                        if (i9 == 0 || (i5 = this.selectedGradientColor2) == 0 || (i6 = this.selectedGradientColor3) == 0) {
                            colorWallpaper = new ColorWallpaper(this.selectedBackgroundSlug, this.selectedColor, i9, this.selectedGradientRotation);
                            this.addedColorWallpaper = colorWallpaper;
                        } else {
                            colorWallpaper2 = new ColorWallpaper(this.selectedBackgroundSlug, this.selectedColor, i9, i5, i6);
                            this.addedColorWallpaper = colorWallpaper2;
                            colorWallpaper2.gradientRotation = this.selectedGradientRotation;
                        }
                    }
                    if ("d".equals(this.selectedBackgroundSlug)) {
                    }
                    this.wallPapers.add(0, this.catsWallpaper);
                    updateRows();
                    return;
                }
                if (!"c".equals(this.selectedBackgroundSlug) || (i4 = this.selectedColor) == 0) {
                    i2 = this.selectedColor;
                    if (i2 != 0) {
                        int i10 = this.selectedGradientColor1;
                        if (i10 == 0 || (i3 = this.selectedGradientColor2) == 0) {
                            colorWallpaper = new ColorWallpaper(this.selectedBackgroundSlug, i2, i10, this.selectedGradientRotation);
                            this.addedColorWallpaper = colorWallpaper;
                        } else {
                            colorWallpaper2 = new ColorWallpaper(this.selectedBackgroundSlug, i2, i10, i3, this.selectedGradientColor3);
                            this.addedColorWallpaper = colorWallpaper2;
                            colorWallpaper2.gradientRotation = this.selectedGradientRotation;
                        }
                    } else if (activeTheme.overrideWallpaper != null && !this.allWallPapersDict.containsKey(this.selectedBackgroundSlug)) {
                        FileWallpaper fileWallpaper3 = new FileWallpaper(this.selectedBackgroundSlug, new File(ApplicationLoader.getFilesDirFixed(), activeTheme.overrideWallpaper.fileName), new File(ApplicationLoader.getFilesDirFixed(), activeTheme.overrideWallpaper.originalFileName));
                        this.addedFileWallpaper = fileWallpaper3;
                        this.wallPapers.add(this.themeWallpaper != null ? 1 : 0, fileWallpaper3);
                    }
                } else if (activeTheme.overrideWallpaper != null) {
                    colorWallpaper3 = new ColorWallpaper(this.selectedBackgroundSlug, i4, this.selectedGradientColor1, this.selectedGradientColor2, this.selectedGradientColor3, this.selectedGradientRotation, this.selectedIntensity, this.selectedBackgroundMotion, new File(ApplicationLoader.getFilesDirFixed(), activeTheme.overrideWallpaper.fileName));
                    this.addedColorWallpaper = colorWallpaper3;
                    colorWallpaper3.pattern = tL_wallPaper;
                    arrayList = this.wallPapers;
                    arrayList.add(0, colorWallpaper3);
                }
                if (!"d".equals(this.selectedBackgroundSlug) || this.wallPapers.isEmpty()) {
                    this.wallPapers.add(0, this.catsWallpaper);
                } else {
                    this.wallPapers.add(1, this.catsWallpaper);
                }
                updateRows();
                return;
                arrayList = this.wallPapers;
                colorWallpaper3 = this.addedColorWallpaper;
                arrayList.add(0, colorWallpaper3);
                if ("d".equals(this.selectedBackgroundSlug)) {
                }
                this.wallPapers.add(0, this.catsWallpaper);
                updateRows();
                return;
            }
            str = this.selectedBackgroundSlug;
            tL_wallPaper4 = tL_wallPaper5;
        } else {
            str = this.selectedBackgroundSlug;
            if (r4 instanceof ColorWallpaper) {
                ?? r0 = r4.parentWallpaper;
                tL_wallPaper4 = r0;
            }
            str2 = str;
            obj = r4;
            tL_wallPaper = null;
            j = 0;
            final boolean isDark2 = Theme.getCurrentTheme().isDark();
            Collections.sort(this.wallPapers, new Comparator() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda3
                @Override // java.util.Comparator
                public final int compare(Object obj4, Object obj5) {
                    int lambda$fillWallpapersWithCustom$9;
                    lambda$fillWallpapersWithCustom$9 = WallpapersListActivity.this.lambda$fillWallpapersWithCustom$9(j, str2, isDark2, obj4, obj5);
                    return lambda$fillWallpapersWithCustom$9;
                }
            });
            if (Theme.hasWallpaperFromTheme()) {
            }
            this.themeWallpaper = null;
            Theme.ThemeInfo activeTheme2 = Theme.getActiveTheme();
            if (TextUtils.isEmpty(this.selectedBackgroundSlug)) {
            }
            if ("c".equals(this.selectedBackgroundSlug)) {
            }
            i2 = this.selectedColor;
            if (i2 != 0) {
            }
        }
        str3 = str;
        tL_wallPaper2 = null;
        tL_wallPaper3 = tL_wallPaper4;
        obj2 = r4;
        tL_wallPaper = tL_wallPaper2;
        obj = obj2;
        j = tL_wallPaper3.id;
        str2 = str3;
        final boolean isDark22 = Theme.getCurrentTheme().isDark();
        Collections.sort(this.wallPapers, new Comparator() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(Object obj4, Object obj5) {
                int lambda$fillWallpapersWithCustom$9;
                lambda$fillWallpapersWithCustom$9 = WallpapersListActivity.this.lambda$fillWallpapersWithCustom$9(j, str2, isDark22, obj4, obj5);
                return lambda$fillWallpapersWithCustom$9;
            }
        });
        if (Theme.hasWallpaperFromTheme()) {
        }
        this.themeWallpaper = null;
        Theme.ThemeInfo activeTheme22 = Theme.getActiveTheme();
        if (TextUtils.isEmpty(this.selectedBackgroundSlug)) {
        }
        if ("c".equals(this.selectedBackgroundSlug)) {
        }
        i2 = this.selectedColor;
        if (i2 != 0) {
        }
    }

    private void fixLayout() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.WallpapersListActivity.8
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    WallpapersListActivity.this.fixLayoutInternal();
                    if (WallpapersListActivity.this.listView != null) {
                        WallpapersListActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixLayoutInternal() {
        if (getParentActivity() == null) {
            return;
        }
        int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if (!AndroidUtilities.isTablet() && (rotation == 3 || rotation == 1)) {
            this.columnsCount = 5;
        } else {
            this.columnsCount = 3;
        }
        updateRows();
    }

    private String getWallPaperSlug(Object obj) {
        if (obj instanceof TLRPC.TL_wallPaper) {
            return ((TLRPC.TL_wallPaper) obj).slug;
        }
        if (obj instanceof ColorWallpaper) {
            return ((ColorWallpaper) obj).slug;
        }
        if (obj instanceof FileWallpaper) {
            return ((FileWallpaper) obj).slug;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1() {
        loadWallpapers(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                WallpapersListActivity.this.lambda$createView$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(DialogInterface dialogInterface, int i) {
        if (this.actionBar.isActionModeShowed()) {
            this.selectedWallPapers.clear();
            this.actionBar.hideActionMode();
            updateRowsSelection();
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        this.progressDialog.show();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_resetWallPapers(), new RequestDelegate() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                WallpapersListActivity.this.lambda$createView$2(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view, int i) {
        if (getParentActivity() == null || this.listView.getAdapter() == this.searchAdapter) {
            return;
        }
        if (i == this.uploadImageRow) {
            this.updater.openGallery();
        } else if (i == this.setColorRow) {
            WallpapersListActivity wallpapersListActivity = new WallpapersListActivity(1);
            wallpapersListActivity.patterns = this.patterns;
            presentFragment(wallpapersListActivity);
        } else if (i == this.resetRow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.ResetChatBackgroundsAlertTitle));
            builder.setMessage(LocaleController.getString(R.string.ResetChatBackgroundsAlert));
            builder.setPositiveButton(LocaleController.getString(R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    WallpapersListActivity.this.lambda$createView$3(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$fillWallpapersWithCustom$9(long j, String str, boolean z, Object obj, Object obj2) {
        if (obj instanceof ColorWallpaper) {
            obj = ((ColorWallpaper) obj).parentWallpaper;
        }
        if (obj2 instanceof ColorWallpaper) {
            obj2 = ((ColorWallpaper) obj2).parentWallpaper;
        }
        if ((obj instanceof TLRPC.WallPaper) && (obj2 instanceof TLRPC.WallPaper)) {
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) obj;
            TLRPC.WallPaper wallPaper2 = (TLRPC.WallPaper) obj2;
            if (j != 0) {
                if (wallPaper.id == j) {
                    return -1;
                }
                if (wallPaper2.id == j) {
                    return 1;
                }
            } else if (str.equals(wallPaper.slug)) {
                return -1;
            } else {
                if (str.equals(wallPaper2.slug)) {
                    return 1;
                }
            }
            if (!z) {
                if ("qeZWES8rGVIEAAAARfWlK1lnfiI".equals(wallPaper.slug)) {
                    return -1;
                }
                if ("qeZWES8rGVIEAAAARfWlK1lnfiI".equals(wallPaper2.slug)) {
                    return 1;
                }
            }
            int indexOf = this.allWallPapers.indexOf(wallPaper);
            int indexOf2 = this.allWallPapers.indexOf(wallPaper2);
            boolean z2 = wallPaper.dark;
            if (!(z2 && wallPaper2.dark) && (z2 || wallPaper2.dark)) {
                return (!z2 || wallPaper2.dark) ? z ? 1 : -1 : z ? -1 : 1;
            } else if (indexOf > indexOf2) {
                return 1;
            } else {
                return indexOf < indexOf2 ? -1 : 0;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadWallpapers$7(TLObject tLObject, boolean z) {
        int i;
        TLRPC.WallPaperSettings wallPaperSettings;
        boolean z2;
        TLRPC.WallPaperSettings wallPaperSettings2;
        TLRPC.WallPaperSettings wallPaperSettings3;
        TLRPC.Document document;
        if (tLObject instanceof TLRPC.TL_account_wallPapers) {
            TLRPC.TL_account_wallPapers tL_account_wallPapers = (TLRPC.TL_account_wallPapers) tLObject;
            this.patterns.clear();
            this.patternsDict.clear();
            int i2 = this.currentType;
            if (i2 != 1 && i2 != 2) {
                this.wallPapers.clear();
                this.allWallPapersDict.clear();
                this.allWallPapers.clear();
                this.allWallPapers.addAll(tL_account_wallPapers.wallpapers);
                this.wallPapers.addAll(this.localWallPapers);
            }
            int size = tL_account_wallPapers.wallpapers.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC.WallPaper wallPaper = tL_account_wallPapers.wallpapers.get(i3);
                if (!"fqv01SQemVIBAAAApND8LDRUhRU".equals(wallPaper.slug)) {
                    if ((wallPaper instanceof TLRPC.TL_wallPaper) && !(wallPaper.document instanceof TLRPC.TL_documentEmpty)) {
                        this.allWallPapersDict.put(wallPaper.slug, wallPaper);
                        if (wallPaper.pattern && (document = wallPaper.document) != null && !this.patternsDict.containsKey(Long.valueOf(document.id))) {
                            this.patterns.add(wallPaper);
                            this.patternsDict.put(Long.valueOf(wallPaper.document.id), wallPaper);
                        }
                        int i4 = this.currentType;
                        if (i4 != 1 && ((!(z2 = wallPaper.pattern) || ((wallPaperSettings3 = wallPaper.settings) != null && wallPaperSettings3.background_color != 0)) && ((i4 != 2 || z2) && (Theme.isCurrentThemeDark() || (wallPaperSettings2 = wallPaper.settings) == null || wallPaperSettings2.intensity >= 0)))) {
                            this.wallPapers.add(wallPaper);
                        }
                    } else if (wallPaper.settings.background_color != 0 && (Theme.isCurrentThemeDark() || (wallPaperSettings = wallPaper.settings) == null || wallPaperSettings.intensity >= 0)) {
                        TLRPC.WallPaperSettings wallPaperSettings4 = wallPaper.settings;
                        int i5 = wallPaperSettings4.second_background_color;
                        ColorWallpaper colorWallpaper = (i5 == 0 || (i = wallPaperSettings4.third_background_color) == 0) ? new ColorWallpaper(null, wallPaperSettings4.background_color, i5, wallPaperSettings4.rotation) : new ColorWallpaper(null, wallPaperSettings4.background_color, i5, i, wallPaperSettings4.fourth_background_color);
                        colorWallpaper.slug = wallPaper.slug;
                        TLRPC.WallPaperSettings wallPaperSettings5 = wallPaper.settings;
                        colorWallpaper.intensity = wallPaperSettings5.intensity / 100.0f;
                        colorWallpaper.gradientRotation = AndroidUtilities.getWallpaperRotation(wallPaperSettings5.rotation, false);
                        colorWallpaper.parentWallpaper = wallPaper;
                        this.wallPapers.add(colorWallpaper);
                    }
                }
            }
            fillWallpapersWithCustom();
            getMessagesStorage().putWallpapers(tL_account_wallPapers.wallpapers, 1);
        }
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            if (z) {
                return;
            }
            this.listView.smoothScrollToPosition(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadWallpapers$8(final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                WallpapersListActivity.this.lambda$loadWallpapers$7(tLObject, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemClick$5(TLRPC.WallPaper wallPaper) {
        removeSelfFromStack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showAsSheet$6() {
        PhotoViewer.getInstance().closePhoto(false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadWallpapers(final boolean z) {
        long j = 0;
        if (!z) {
            int size = this.allWallPapers.size();
            long j2 = 0;
            for (int i = 0; i < size; i++) {
                Object obj = this.allWallPapers.get(i);
                if (obj instanceof TLRPC.WallPaper) {
                    long j3 = ((TLRPC.WallPaper) obj).id;
                    if (j3 >= 0) {
                        j2 = MediaDataController.calcHash(j2, j3);
                    }
                }
            }
            j = j2;
        }
        TLRPC.TL_account_getWallPapers tL_account_getWallPapers = new TLRPC.TL_account_getWallPapers();
        tL_account_getWallPapers.hash = j;
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getWallPapers, new RequestDelegate() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                WallpapersListActivity.this.lambda$loadWallpapers$8(z, tLObject, tL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onItemClick(WallpaperCell wallpaperCell, Object obj, int i) {
        WallpaperCell wallpaperCell2;
        int i2;
        Object obj2 = obj;
        boolean z = false;
        if (this.actionBar.isActionModeShowed()) {
            Object obj3 = obj2 instanceof ColorWallpaper ? ((ColorWallpaper) obj2).parentWallpaper : obj2;
            if (obj3 instanceof TLRPC.WallPaper) {
                TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) obj3;
                if (this.selectedWallPapers.indexOfKey(wallPaper.id) >= 0) {
                    this.selectedWallPapers.remove(wallPaper.id);
                } else {
                    this.selectedWallPapers.put(wallPaper.id, obj2);
                }
                if (this.selectedWallPapers.size() == 0) {
                    this.actionBar.hideActionMode();
                } else {
                    this.selectedMessagesCountTextView.setNumber(this.selectedWallPapers.size(), true);
                }
                this.scrolling = false;
                if (this.selectedWallPapers.indexOfKey(wallPaper.id) >= 0) {
                    wallpaperCell2 = wallpaperCell;
                    i2 = i;
                    z = true;
                } else {
                    wallpaperCell2 = wallpaperCell;
                    i2 = i;
                }
                wallpaperCell2.setChecked(i2, z, true);
                return;
            }
            return;
        }
        String wallPaperSlug = getWallPaperSlug(obj2);
        boolean z2 = obj2 instanceof TLRPC.TL_wallPaper;
        ColorWallpaper colorWallpaper = obj2;
        if (z2) {
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj2;
            colorWallpaper = obj2;
            if (tL_wallPaper.pattern) {
                String str = tL_wallPaper.slug;
                TLRPC.WallPaperSettings wallPaperSettings = tL_wallPaper.settings;
                int i3 = wallPaperSettings.background_color;
                int i4 = wallPaperSettings.second_background_color;
                int i5 = wallPaperSettings.third_background_color;
                int i6 = wallPaperSettings.fourth_background_color;
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(wallPaperSettings.rotation, false);
                TLRPC.WallPaperSettings wallPaperSettings2 = tL_wallPaper.settings;
                ColorWallpaper colorWallpaper2 = new ColorWallpaper(str, i3, i4, i5, i6, wallpaperRotation, wallPaperSettings2.intensity / 100.0f, wallPaperSettings2.motion, null);
                colorWallpaper2.pattern = tL_wallPaper;
                colorWallpaper2.parentWallpaper = tL_wallPaper;
                colorWallpaper = colorWallpaper2;
            }
        }
        ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(colorWallpaper, null, true, false) { // from class: org.telegram.ui.WallpapersListActivity.7
            @Override // org.telegram.ui.ThemePreviewActivity
            public boolean insideBottomSheet() {
                return true;
            }
        };
        if (this.currentType == 1 || this.dialogId != 0) {
            themePreviewActivity.setDelegate(new ThemePreviewActivity.WallpaperActivityDelegate() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda7
                @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
                public final void didSetNewBackground(TLRPC.WallPaper wallPaper2) {
                    WallpapersListActivity.this.lambda$onItemClick$5(wallPaper2);
                }
            });
        }
        if (this.selectedBackgroundSlug.equals(wallPaperSlug)) {
            themePreviewActivity.setInitialModes(this.selectedBackgroundBlurred, this.selectedBackgroundMotion, this.selectedIntensity);
        }
        themePreviewActivity.setPatterns(this.patterns);
        themePreviewActivity.setDialogId(this.dialogId);
        showAsSheet(themePreviewActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onItemLongClick(WallpaperCell wallpaperCell, Object obj, int i) {
        int i2 = this.currentType;
        if (i2 != 2 && i2 != 3) {
            Object obj2 = obj instanceof ColorWallpaper ? ((ColorWallpaper) obj).parentWallpaper : obj;
            if (!this.actionBar.isActionModeShowed() && getParentActivity() != null && (obj2 instanceof TLRPC.WallPaper)) {
                AndroidUtilities.hideKeyboard(getParentActivity().getCurrentFocus());
                this.selectedWallPapers.put(((TLRPC.WallPaper) obj2).id, obj);
                this.selectedMessagesCountTextView.setNumber(1, false);
                AnimatorSet animatorSet = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < this.actionModeViews.size(); i3++) {
                    View view = (View) this.actionModeViews.get(i3);
                    AndroidUtilities.clearDrawableAnimation(view);
                    arrayList.add(ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f, 1.0f));
                }
                animatorSet.playTogether(arrayList);
                animatorSet.setDuration(250L);
                animatorSet.start();
                this.scrolling = false;
                this.actionBar.showActionMode();
                wallpaperCell.setChecked(i, true, true);
                return true;
            }
        }
        return false;
    }

    private void showAsSheet(ThemePreviewActivity themePreviewActivity) {
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        themePreviewActivity.setResourceProvider(this.resourceProvider);
        bottomSheetParams.onOpenAnimationFinished = new Runnable() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                WallpapersListActivity.lambda$showAsSheet$6();
            }
        };
        bottomSheetParams.occupyNavigationBar = true;
        showAsSheet(themePreviewActivity, bottomSheetParams);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateRows() {
        ListAdapter listAdapter;
        this.rowCount = 0;
        int i = this.currentType;
        if (i == 0) {
            this.uploadImageRow = 0;
            this.setColorRow = 1;
            this.rowCount = 3;
            this.sectionRow = 2;
        } else {
            this.uploadImageRow = -1;
            this.setColorRow = -1;
            this.sectionRow = -1;
            if (i == 2) {
                this.galleryRow = 0;
                this.rowCount = 2;
                this.galleryHintRow = 1;
                if (this.wallPapers.isEmpty()) {
                    int ceil = (int) Math.ceil(this.wallPapers.size() / this.columnsCount);
                    this.totalWallpaperRows = ceil;
                    int i2 = this.rowCount;
                    this.wallPaperStartRow = i2;
                    this.rowCount = i2 + ceil;
                } else {
                    this.wallPaperStartRow = -1;
                }
                if (this.currentType != 0) {
                    int i3 = this.rowCount;
                    this.resetSectionRow = i3;
                    this.resetRow = i3 + 1;
                    this.rowCount = i3 + 3;
                    this.resetInfoRow = i3 + 2;
                } else {
                    this.resetSectionRow = -1;
                    this.resetRow = -1;
                    this.resetInfoRow = -1;
                }
                listAdapter = this.listAdapter;
                if (listAdapter == null) {
                    this.scrolling = true;
                    listAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            }
        }
        this.galleryRow = -1;
        this.galleryHintRow = -1;
        if (this.wallPapers.isEmpty()) {
        }
        if (this.currentType != 0) {
        }
        listAdapter = this.listAdapter;
        if (listAdapter == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRowsSelection() {
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof WallpaperCell) {
                WallpaperCell wallpaperCell = (WallpaperCell) childAt;
                for (int i2 = 0; i2 < 5; i2++) {
                    wallpaperCell.setChecked(i2, false, true);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0074  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        ActionBar actionBar;
        int i;
        String str;
        this.colorPaint = new Paint(1);
        Paint paint = new Paint(1);
        this.colorFramePaint = paint;
        paint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        this.colorFramePaint.setStyle(Paint.Style.STROKE);
        this.colorFramePaint.setColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
        this.updater = new WallpaperUpdater(getParentActivity(), this, new 1());
        this.hasOwnBackground = true;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.currentType;
        if (i2 != 0) {
            if (i2 != 2) {
                if (i2 == 1) {
                    actionBar = this.actionBar;
                    i = R.string.SelectColorTitle;
                }
                this.actionBar.setActionBarMenuOnItemClick(new 2());
                if (this.currentType == 0) {
                }
                FrameLayout frameLayout = new FrameLayout(context);
                this.fragmentView = frameLayout;
                RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.WallpapersListActivity.4
                    private Paint paint = new Paint();

                    @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                    public boolean hasOverlappingRendering() {
                        return false;
                    }

                    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
                        if (r0.itemView.getBottom() >= r1) goto L16;
                     */
                    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public void onDraw(Canvas canvas) {
                        int i3;
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = (getAdapter() != WallpapersListActivity.this.listAdapter || WallpapersListActivity.this.resetInfoRow == -1) ? null : findViewHolderForAdapterPosition(WallpapersListActivity.this.resetInfoRow);
                        int measuredHeight = getMeasuredHeight();
                        if (findViewHolderForAdapterPosition != null) {
                            i3 = findViewHolderForAdapterPosition.itemView.getBottom();
                        }
                        i3 = measuredHeight;
                        this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        float f = i3;
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), f, this.paint);
                        if (i3 != measuredHeight) {
                            this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
                            canvas.drawRect(0.0f, f, getMeasuredWidth(), measuredHeight, this.paint);
                        }
                    }
                };
                this.listView = recyclerListView;
                recyclerListView.setClipToPadding(false);
                this.listView.setHorizontalScrollBarEnabled(false);
                this.listView.setVerticalScrollBarEnabled(false);
                this.listView.setItemAnimator(null);
                this.listView.setLayoutAnimation(null);
                RecyclerListView recyclerListView2 = this.listView;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.WallpapersListActivity.5
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                this.layoutManager = linearLayoutManager;
                recyclerListView2.setLayoutManager(linearLayoutManager);
                frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
                RecyclerListView recyclerListView3 = this.listView;
                ListAdapter listAdapter = new ListAdapter(context);
                this.listAdapter = listAdapter;
                recyclerListView3.setAdapter(listAdapter);
                this.searchAdapter = new SearchAdapter(context);
                this.listView.setGlowColor(Theme.getColor(Theme.key_avatar_backgroundActionBarBlue));
                this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i3) {
                        WallpapersListActivity.this.lambda$createView$4(view, i3);
                    }
                });
                this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.WallpapersListActivity.6
                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                    public void onScrollStateChanged(RecyclerView recyclerView, int i3) {
                        if (i3 == 1) {
                            AndroidUtilities.hideKeyboard(WallpapersListActivity.this.getParentActivity().getCurrentFocus());
                        }
                        WallpapersListActivity.this.scrolling = i3 != 0;
                    }

                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                    public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                        if (WallpapersListActivity.this.listView.getAdapter() == WallpapersListActivity.this.searchAdapter) {
                            int findFirstVisibleItemPosition = WallpapersListActivity.this.layoutManager.findFirstVisibleItemPosition();
                            int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(WallpapersListActivity.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                            if (abs > 0) {
                                int itemCount = WallpapersListActivity.this.layoutManager.getItemCount();
                                if (abs == 0 || findFirstVisibleItemPosition + abs <= itemCount - 2) {
                                    return;
                                }
                                WallpapersListActivity.this.searchAdapter.loadMoreResults();
                            }
                        }
                    }
                });
                EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
                this.searchEmptyView = emptyTextProgressView;
                emptyTextProgressView.setVisibility(8);
                this.searchEmptyView.setShowAtCenter(true);
                this.searchEmptyView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                this.searchEmptyView.setText(LocaleController.getString(R.string.NoResult));
                this.listView.setEmptyView(this.searchEmptyView);
                frameLayout.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f));
                updateRows();
                return this.fragmentView;
            }
            actionBar = this.actionBar;
            str = "Channel Wallpaper";
            actionBar.setTitle(str);
            this.actionBar.setActionBarMenuOnItemClick(new 2());
            if (this.currentType == 0) {
                ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.WallpapersListActivity.3
                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onCaptionCleared() {
                        WallpapersListActivity.this.searchAdapter.clearColor();
                        WallpapersListActivity.this.searchItem.setSearchFieldHint(LocaleController.getString(R.string.SearchBackgrounds));
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onSearchCollapse() {
                        WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.listAdapter);
                        WallpapersListActivity.this.listView.invalidate();
                        WallpapersListActivity.this.searchAdapter.processSearch(null, true);
                        WallpapersListActivity.this.searchItem.setSearchFieldCaption(null);
                        onCaptionCleared();
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onSearchExpand() {
                        WallpapersListActivity.this.listView.setAdapter(WallpapersListActivity.this.searchAdapter);
                        WallpapersListActivity.this.listView.invalidate();
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onTextChanged(EditText editText) {
                        WallpapersListActivity.this.searchAdapter.processSearch(editText.getText().toString(), false);
                    }
                });
                this.searchItem = actionBarMenuItemSearchListener;
                actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(R.string.SearchBackgrounds));
                ActionBarMenu createActionMode = this.actionBar.createActionMode(false, null);
                createActionMode.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
                ActionBar actionBar2 = this.actionBar;
                int i3 = Theme.key_actionBarDefaultIcon;
                actionBar2.setItemsColor(Theme.getColor(i3), true);
                this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSelector), true);
                NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
                this.selectedMessagesCountTextView = numberTextView;
                numberTextView.setTextSize(18);
                this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.bold());
                this.selectedMessagesCountTextView.setTextColor(Theme.getColor(i3));
                this.selectedMessagesCountTextView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$createView$0;
                        lambda$createView$0 = WallpapersListActivity.lambda$createView$0(view, motionEvent);
                        return lambda$createView$0;
                    }
                });
                createActionMode.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
                this.actionModeViews.add(createActionMode.addItemWithWidth(3, R.drawable.msg_forward, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.Forward)));
                this.actionModeViews.add(createActionMode.addItemWithWidth(4, R.drawable.msg_delete, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.Delete)));
                this.selectedWallPapers.clear();
            }
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.fragmentView = frameLayout2;
            RecyclerListView recyclerListView4 = new RecyclerListView(context) { // from class: org.telegram.ui.WallpapersListActivity.4
                private Paint paint = new Paint();

                @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                public boolean hasOverlappingRendering() {
                    return false;
                }

                /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
                    if (r0.itemView.getBottom() >= r1) goto L16;
                 */
                @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onDraw(Canvas canvas) {
                    int i32;
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = (getAdapter() != WallpapersListActivity.this.listAdapter || WallpapersListActivity.this.resetInfoRow == -1) ? null : findViewHolderForAdapterPosition(WallpapersListActivity.this.resetInfoRow);
                    int measuredHeight = getMeasuredHeight();
                    if (findViewHolderForAdapterPosition != null) {
                        i32 = findViewHolderForAdapterPosition.itemView.getBottom();
                    }
                    i32 = measuredHeight;
                    this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    float f = i32;
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), f, this.paint);
                    if (i32 != measuredHeight) {
                        this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
                        canvas.drawRect(0.0f, f, getMeasuredWidth(), measuredHeight, this.paint);
                    }
                }
            };
            this.listView = recyclerListView4;
            recyclerListView4.setClipToPadding(false);
            this.listView.setHorizontalScrollBarEnabled(false);
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            RecyclerListView recyclerListView22 = this.listView;
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.WallpapersListActivity.5
                @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            this.layoutManager = linearLayoutManager2;
            recyclerListView22.setLayoutManager(linearLayoutManager2);
            frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
            RecyclerListView recyclerListView32 = this.listView;
            ListAdapter listAdapter2 = new ListAdapter(context);
            this.listAdapter = listAdapter2;
            recyclerListView32.setAdapter(listAdapter2);
            this.searchAdapter = new SearchAdapter(context);
            this.listView.setGlowColor(Theme.getColor(Theme.key_avatar_backgroundActionBarBlue));
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i32) {
                    WallpapersListActivity.this.lambda$createView$4(view, i32);
                }
            });
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.WallpapersListActivity.6
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i32) {
                    if (i32 == 1) {
                        AndroidUtilities.hideKeyboard(WallpapersListActivity.this.getParentActivity().getCurrentFocus());
                    }
                    WallpapersListActivity.this.scrolling = i32 != 0;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i32, int i4) {
                    if (WallpapersListActivity.this.listView.getAdapter() == WallpapersListActivity.this.searchAdapter) {
                        int findFirstVisibleItemPosition = WallpapersListActivity.this.layoutManager.findFirstVisibleItemPosition();
                        int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(WallpapersListActivity.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                        if (abs > 0) {
                            int itemCount = WallpapersListActivity.this.layoutManager.getItemCount();
                            if (abs == 0 || findFirstVisibleItemPosition + abs <= itemCount - 2) {
                                return;
                            }
                            WallpapersListActivity.this.searchAdapter.loadMoreResults();
                        }
                    }
                }
            });
            EmptyTextProgressView emptyTextProgressView2 = new EmptyTextProgressView(context);
            this.searchEmptyView = emptyTextProgressView2;
            emptyTextProgressView2.setVisibility(8);
            this.searchEmptyView.setShowAtCenter(true);
            this.searchEmptyView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            this.searchEmptyView.setText(LocaleController.getString(R.string.NoResult));
            this.listView.setEmptyView(this.searchEmptyView);
            frameLayout2.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f));
            updateRows();
            return this.fragmentView;
        }
        actionBar = this.actionBar;
        i = R.string.ChatBackground;
        str = LocaleController.getString(i);
        actionBar.setTitle(str);
        this.actionBar.setActionBarMenuOnItemClick(new 2());
        if (this.currentType == 0) {
        }
        FrameLayout frameLayout22 = new FrameLayout(context);
        this.fragmentView = frameLayout22;
        RecyclerListView recyclerListView42 = new RecyclerListView(context) { // from class: org.telegram.ui.WallpapersListActivity.4
            private Paint paint = new Paint();

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public boolean hasOverlappingRendering() {
                return false;
            }

            /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
                if (r0.itemView.getBottom() >= r1) goto L16;
             */
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onDraw(Canvas canvas) {
                int i32;
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = (getAdapter() != WallpapersListActivity.this.listAdapter || WallpapersListActivity.this.resetInfoRow == -1) ? null : findViewHolderForAdapterPosition(WallpapersListActivity.this.resetInfoRow);
                int measuredHeight = getMeasuredHeight();
                if (findViewHolderForAdapterPosition != null) {
                    i32 = findViewHolderForAdapterPosition.itemView.getBottom();
                }
                i32 = measuredHeight;
                this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                float f = i32;
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), f, this.paint);
                if (i32 != measuredHeight) {
                    this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
                    canvas.drawRect(0.0f, f, getMeasuredWidth(), measuredHeight, this.paint);
                }
            }
        };
        this.listView = recyclerListView42;
        recyclerListView42.setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView222 = this.listView;
        LinearLayoutManager linearLayoutManager22 = new LinearLayoutManager(context, 1, false) { // from class: org.telegram.ui.WallpapersListActivity.5
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = linearLayoutManager22;
        recyclerListView222.setLayoutManager(linearLayoutManager22);
        frameLayout22.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView322 = this.listView;
        ListAdapter listAdapter22 = new ListAdapter(context);
        this.listAdapter = listAdapter22;
        recyclerListView322.setAdapter(listAdapter22);
        this.searchAdapter = new SearchAdapter(context);
        this.listView.setGlowColor(Theme.getColor(Theme.key_avatar_backgroundActionBarBlue));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.WallpapersListActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i32) {
                WallpapersListActivity.this.lambda$createView$4(view, i32);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.WallpapersListActivity.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i32) {
                if (i32 == 1) {
                    AndroidUtilities.hideKeyboard(WallpapersListActivity.this.getParentActivity().getCurrentFocus());
                }
                WallpapersListActivity.this.scrolling = i32 != 0;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i32, int i4) {
                if (WallpapersListActivity.this.listView.getAdapter() == WallpapersListActivity.this.searchAdapter) {
                    int findFirstVisibleItemPosition = WallpapersListActivity.this.layoutManager.findFirstVisibleItemPosition();
                    int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(WallpapersListActivity.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                    if (abs > 0) {
                        int itemCount = WallpapersListActivity.this.layoutManager.getItemCount();
                        if (abs == 0 || findFirstVisibleItemPosition + abs <= itemCount - 2) {
                            return;
                        }
                        WallpapersListActivity.this.searchAdapter.loadMoreResults();
                    }
                }
            }
        });
        EmptyTextProgressView emptyTextProgressView22 = new EmptyTextProgressView(context);
        this.searchEmptyView = emptyTextProgressView22;
        emptyTextProgressView22.setVisibility(8);
        this.searchEmptyView.setShowAtCenter(true);
        this.searchEmptyView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.searchEmptyView.setText(LocaleController.getString(R.string.NoResult));
        this.listView.setEmptyView(this.searchEmptyView);
        frameLayout22.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f));
        updateRows();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC.WallPaperSettings wallPaperSettings;
        int i3;
        boolean z;
        TLRPC.WallPaperSettings wallPaperSettings2;
        TLRPC.WallPaperSettings wallPaperSettings3;
        if (i != NotificationCenter.wallpapersDidLoad) {
            if (i != NotificationCenter.didSetNewWallpapper) {
                if (i == NotificationCenter.wallpapersNeedReload) {
                    getMessagesStorage().getWallpapers();
                    return;
                }
                return;
            }
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView != null) {
                recyclerListView.invalidateViews();
            }
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
                return;
            }
            return;
        }
        ArrayList arrayList = (ArrayList) objArr[0];
        this.patterns.clear();
        this.patternsDict.clear();
        int i4 = this.currentType;
        if (i4 != 1 && i4 != 2) {
            this.wallPapers.clear();
            this.localWallPapers.clear();
            this.localDict.clear();
            this.allWallPapers.clear();
            this.allWallPapersDict.clear();
            this.allWallPapers.addAll(arrayList);
        }
        int size = arrayList.size();
        ArrayList arrayList2 = null;
        for (int i5 = 0; i5 < size; i5++) {
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) arrayList.get(i5);
            if (!"fqv01SQemVIBAAAApND8LDRUhRU".equals(wallPaper.slug)) {
                if (wallPaper instanceof TLRPC.TL_wallPaper) {
                    TLRPC.Document document = wallPaper.document;
                    if (!(document instanceof TLRPC.TL_documentEmpty)) {
                        if (wallPaper.pattern && document != null && !this.patternsDict.containsKey(Long.valueOf(document.id))) {
                            this.patterns.add(wallPaper);
                            this.patternsDict.put(Long.valueOf(wallPaper.document.id), wallPaper);
                        }
                        this.allWallPapersDict.put(wallPaper.slug, wallPaper);
                        int i6 = this.currentType;
                        if (i6 != 1 && ((!(z = wallPaper.pattern) || ((wallPaperSettings3 = wallPaper.settings) != null && wallPaperSettings3.background_color != 0)) && ((i6 != 2 || z) && (Theme.isCurrentThemeDark() || (wallPaperSettings2 = wallPaper.settings) == null || wallPaperSettings2.intensity >= 0)))) {
                            this.wallPapers.add(wallPaper);
                        }
                    }
                }
                TLRPC.WallPaperSettings wallPaperSettings4 = wallPaper.settings;
                int i7 = wallPaperSettings4.background_color;
                if (i7 != 0) {
                    int i8 = wallPaperSettings4.second_background_color;
                    ColorWallpaper colorWallpaper = (i8 == 0 || (i3 = wallPaperSettings4.third_background_color) == 0) ? new ColorWallpaper(null, i7, i8, wallPaperSettings4.rotation) : new ColorWallpaper(null, i7, i8, i3, wallPaperSettings4.fourth_background_color);
                    colorWallpaper.slug = wallPaper.slug;
                    TLRPC.WallPaperSettings wallPaperSettings5 = wallPaper.settings;
                    colorWallpaper.intensity = wallPaperSettings5.intensity / 100.0f;
                    colorWallpaper.gradientRotation = AndroidUtilities.getWallpaperRotation(wallPaperSettings5.rotation, false);
                    colorWallpaper.parentWallpaper = wallPaper;
                    if (wallPaper.id < 0) {
                        String hash = colorWallpaper.getHash();
                        if (this.localDict.containsKey(hash)) {
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            arrayList2.add(wallPaper);
                        } else {
                            this.localWallPapers.add(colorWallpaper);
                            this.localDict.put(hash, colorWallpaper);
                        }
                    }
                    if (Theme.isCurrentThemeDark() || (wallPaperSettings = wallPaper.settings) == null || wallPaperSettings.intensity >= 0) {
                        this.wallPapers.add(colorWallpaper);
                    }
                }
            }
        }
        if (arrayList2 != null) {
            int size2 = arrayList2.size();
            for (int i9 = 0; i9 < size2; i9++) {
                getMessagesStorage().deleteWallpaper(((TLRPC.WallPaper) arrayList2.get(i9)).id);
            }
        }
        if (this.dialogId == 0) {
            this.selectedBackgroundSlug = Theme.getSelectedBackgroundSlug();
        }
        fillWallpapersWithCustom();
        loadWallpapers(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        View view = this.fragmentView;
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(view, 0, null, null, null, null, i));
        View view2 = this.fragmentView;
        int i2 = Theme.key_windowBackgroundGray;
        arrayList.add(new ThemeDescription(view2, 0, null, null, null, null, i2));
        ActionBar actionBar = this.actionBar;
        int i3 = ThemeDescription.FLAG_BACKGROUND;
        int i4 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i3, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        int i5 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
        arrayList.add(new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        this.updater.onActivityResult(i, i2, intent);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fixLayout();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter globalInstance;
        int i;
        int i2 = this.currentType;
        if (i2 != 0 && i2 != 2) {
            fillDefaultColors(this.wallPapers, Theme.isCurrentThemeDark());
            if (this.currentType == 1 && this.patterns.isEmpty()) {
                globalInstance = NotificationCenter.getGlobalInstance();
                i = NotificationCenter.wallpapersDidLoad;
            }
            return super.onFragmentCreate();
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        globalInstance = NotificationCenter.getGlobalInstance();
        i = NotificationCenter.wallpapersNeedReload;
        globalInstance.addObserver(this, i);
        getMessagesStorage().getWallpapers();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter globalInstance;
        int i;
        int i2 = this.currentType;
        if (i2 != 0 && i2 != 2) {
            if (i2 == 1) {
                globalInstance = NotificationCenter.getGlobalInstance();
                i = NotificationCenter.wallpapersDidLoad;
            }
            this.updater.cleanup();
            super.onFragmentDestroy();
        }
        this.searchAdapter.onDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        globalInstance = NotificationCenter.getGlobalInstance();
        i = NotificationCenter.wallpapersNeedReload;
        globalInstance.removeObserver(this, i);
        this.updater.cleanup();
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        boolean z;
        TLRPC.WallPaper wallPaper;
        super.onResume();
        MessagesController.getGlobalMainSettings();
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        if (this.dialogId != 0) {
            TLRPC.UserFull userFull = getMessagesController().getUserFull(this.dialogId);
            if (userFull != null && (wallPaper = userFull.wallpaper) != null) {
                String str = wallPaper.slug;
                this.selectedBackgroundSlug = str;
                if (str == null) {
                    this.selectedBackgroundSlug = "";
                }
                TLRPC.WallPaperSettings wallPaperSettings = wallPaper.settings;
                if (wallPaperSettings != null) {
                    this.selectedColor = wallPaperSettings.background_color;
                    this.selectedGradientColor1 = wallPaperSettings.second_background_color;
                    this.selectedGradientColor2 = wallPaperSettings.third_background_color;
                    this.selectedGradientColor3 = wallPaperSettings.fourth_background_color;
                    this.selectedGradientRotation = wallPaperSettings.rotation;
                    this.selectedIntensity = wallPaperSettings.intensity;
                    this.selectedBackgroundMotion = wallPaperSettings.motion;
                    z = wallPaperSettings.blur;
                }
            }
            fillWallpapersWithCustom();
            fixLayout();
        }
        Theme.OverrideWallpaperInfo overrideWallpaperInfo = activeTheme.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            String str2 = overrideWallpaperInfo.slug;
            this.selectedBackgroundSlug = str2;
            if (str2 == null) {
                this.selectedBackgroundSlug = "";
            }
            this.selectedColor = overrideWallpaperInfo.color;
            this.selectedGradientColor1 = overrideWallpaperInfo.gradientColor1;
            this.selectedGradientColor2 = overrideWallpaperInfo.gradientColor2;
            this.selectedGradientColor3 = overrideWallpaperInfo.gradientColor3;
            this.selectedGradientRotation = overrideWallpaperInfo.rotation;
            this.selectedIntensity = overrideWallpaperInfo.intensity;
            this.selectedBackgroundMotion = overrideWallpaperInfo.isMotion;
            z = overrideWallpaperInfo.isBlurred;
        } else {
            this.selectedBackgroundSlug = Theme.hasWallpaperFromTheme() ? "t" : "d";
            z = false;
            this.selectedColor = 0;
            this.selectedGradientColor1 = 0;
            this.selectedGradientColor2 = 0;
            this.selectedGradientColor3 = 0;
            this.selectedGradientRotation = 45;
            this.selectedIntensity = 1.0f;
            this.selectedBackgroundMotion = false;
        }
        this.selectedBackgroundBlurred = z;
        fillWallpapersWithCustom();
        fixLayout();
    }

    public void restoreSelfArgs(Bundle bundle) {
        this.updater.setCurrentPicturePath(bundle.getString("path"));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String currentPicturePath = this.updater.getCurrentPicturePath();
        if (currentPicturePath != null) {
            bundle.putString("path", currentPicturePath);
        }
    }
}
