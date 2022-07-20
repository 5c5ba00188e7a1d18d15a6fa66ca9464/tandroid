package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$GeoPoint;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_geoPoint;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.LocationActivityAdapter;
import org.telegram.ui.Adapters.LocationActivitySearchAdapter;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.LocationDirectionCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.LocationPoweredCell;
import org.telegram.ui.Cells.SendLocationCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.SharingLiveLocationCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ChatAttachAlertLocationLayout extends ChatAttachAlert.AttachAlertLayout implements NotificationCenter.NotificationCenterDelegate {
    private LocationActivityAdapter adapter;
    private AnimatorSet animatorSet;
    private int clipSize;
    private boolean currentMapStyleDark;
    private LocationActivityDelegate delegate;
    private long dialogId;
    private ImageView emptyImageView;
    private TextView emptySubtitleTextView;
    private TextView emptyTitleTextView;
    private LinearLayout emptyView;
    private boolean firstWas;
    private CameraUpdate forceUpdate;
    private GoogleMap googleMap;
    private boolean ignoreLayout;
    private Marker lastPressedMarker;
    private FrameLayout lastPressedMarkerView;
    private VenueLocation lastPressedVenue;
    private FillLastLinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private View loadingMapView;
    private ImageView locationButton;
    private boolean locationDenied;
    private int locationType;
    private int mapHeight;
    private ActionBarMenuItem mapTypeButton;
    private MapView mapView;
    private FrameLayout mapViewClip;
    private boolean mapsInitialized;
    private ImageView markerImageView;
    private int markerTop;
    private Location myLocation;
    private int nonClipSize;
    private boolean onResumeCalled;
    private ActionBarMenuItem otherItem;
    private int overScrollHeight;
    private MapOverlayView overlayView;
    private boolean scrolling;
    private LocationActivitySearchAdapter searchAdapter;
    private SearchButton searchAreaButton;
    private boolean searchInProgress;
    private ActionBarMenuItem searchItem;
    private RecyclerListView searchListView;
    private boolean searchWas;
    private boolean searchedForCustomLocations;
    private boolean searching;
    private Location userLocation;
    private boolean userLocationMoved;
    private float yOffset;
    private boolean checkGpsEnabled = true;
    private boolean isFirstLocation = true;
    private Paint backgroundPaint = new Paint();
    private ArrayList<VenueLocation> placeMarkers = new ArrayList<>();
    private boolean checkPermission = true;
    private boolean checkBackgroundPermission = true;
    private boolean first = true;
    private Bitmap[] bitmapCache = new Bitmap[7];

    /* loaded from: classes3.dex */
    public static class LiveLocation {
        public Marker marker;
    }

    /* loaded from: classes3.dex */
    public interface LocationActivityDelegate {
        void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2);
    }

    /* loaded from: classes3.dex */
    public static class VenueLocation {
        public Marker marker;
        public int num;
        public TLRPC$TL_messageMediaVenue venue;
    }

    public static /* synthetic */ boolean lambda$new$4(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int needsActionBar() {
        return 1;
    }

    static /* synthetic */ float access$2816(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout, float f) {
        float f2 = chatAttachAlertLocationLayout.yOffset + f;
        chatAttachAlertLocationLayout.yOffset = f2;
        return f2;
    }

    /* loaded from: classes3.dex */
    public static class SearchButton extends TextView {
        private float additionanTranslationY;
        private float currentTranslationY;

        public SearchButton(Context context) {
            super(context);
        }

        @Override // android.view.View
        public float getTranslationX() {
            return this.additionanTranslationY;
        }

        @Override // android.view.View
        public void setTranslationX(float f) {
            this.additionanTranslationY = f;
            updateTranslationY();
        }

        public void setTranslation(float f) {
            this.currentTranslationY = f;
            updateTranslationY();
        }

        private void updateTranslationY() {
            setTranslationY(this.currentTranslationY + this.additionanTranslationY);
        }
    }

    /* loaded from: classes3.dex */
    public class MapOverlayView extends FrameLayout {
        private HashMap<Marker, View> views = new HashMap<>();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MapOverlayView(Context context) {
            super(context);
            ChatAttachAlertLocationLayout.this = r1;
        }

        public void addInfoView(Marker marker) {
            VenueLocation venueLocation = (VenueLocation) marker.getTag();
            if (ChatAttachAlertLocationLayout.this.lastPressedVenue == venueLocation) {
                return;
            }
            ChatAttachAlertLocationLayout.this.showSearchPlacesButton(false);
            if (ChatAttachAlertLocationLayout.this.lastPressedMarker != null) {
                removeInfoView(ChatAttachAlertLocationLayout.this.lastPressedMarker);
                ChatAttachAlertLocationLayout.this.lastPressedMarker = null;
            }
            ChatAttachAlertLocationLayout.this.lastPressedVenue = venueLocation;
            ChatAttachAlertLocationLayout.this.lastPressedMarker = marker;
            Context context = getContext();
            FrameLayout frameLayout = new FrameLayout(context);
            addView(frameLayout, LayoutHelper.createFrame(-2, 114.0f));
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView = new FrameLayout(context);
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView.setBackgroundResource(2131166189);
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView.getBackground().setColorFilter(new PorterDuffColorFilter(ChatAttachAlertLocationLayout.this.getThemedColor("dialogBackground"), PorterDuff.Mode.MULTIPLY));
            frameLayout.addView(ChatAttachAlertLocationLayout.this.lastPressedMarkerView, LayoutHelper.createFrame(-2, 71.0f));
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView.setAlpha(0.0f);
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView.setOnClickListener(new ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda0(this, venueLocation));
            TextView textView = new TextView(context);
            textView.setTextSize(1, 16.0f);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            textView.setTextColor(ChatAttachAlertLocationLayout.this.getThemedColor("windowBackgroundWhiteBlackText"));
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            int i = 5;
            textView.setGravity(LocaleController.isRTL ? 5 : 3);
            ChatAttachAlertLocationLayout.this.lastPressedMarkerView.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 18.0f, 10.0f, 18.0f, 0.0f));
            TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 14.0f);
            textView2.setMaxLines(1);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setSingleLine(true);
            textView2.setTextColor(ChatAttachAlertLocationLayout.this.getThemedColor("windowBackgroundWhiteGrayText3"));
            textView2.setGravity(LocaleController.isRTL ? 5 : 3);
            FrameLayout frameLayout2 = ChatAttachAlertLocationLayout.this.lastPressedMarkerView;
            if (!LocaleController.isRTL) {
                i = 3;
            }
            frameLayout2.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, i | 48, 18.0f, 32.0f, 18.0f, 0.0f));
            textView.setText(venueLocation.venue.title);
            textView2.setText(LocaleController.getString("TapToSendLocation", 2131628550));
            FrameLayout frameLayout3 = new FrameLayout(context);
            frameLayout3.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(36.0f), LocationCell.getColorForIndex(venueLocation.num)));
            frameLayout.addView(frameLayout3, LayoutHelper.createFrame(36, 36.0f, 81, 0.0f, 0.0f, 0.0f, 4.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setImage("https://ss3.4sqi.net/img/categories_v2/" + venueLocation.venue.venue_type + "_64.png", null, null);
            frameLayout3.addView(backupImageView, LayoutHelper.createFrame(30, 30, 17));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new AnonymousClass1(frameLayout3));
            ofFloat.setDuration(360L);
            ofFloat.start();
            this.views.put(marker, frameLayout);
            ChatAttachAlertLocationLayout.this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 300, null);
        }

        public /* synthetic */ void lambda$addInfoView$1(VenueLocation venueLocation, View view) {
            ChatActivity chatActivity = (ChatActivity) ChatAttachAlertLocationLayout.this.parentAlert.baseFragment;
            if (chatActivity.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(ChatAttachAlertLocationLayout.this.getParentActivity(), chatActivity.getDialogId(), new ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda1(this, venueLocation), ChatAttachAlertLocationLayout.this.resourcesProvider);
                return;
            }
            ChatAttachAlertLocationLayout.this.delegate.didSelectLocation(venueLocation.venue, ChatAttachAlertLocationLayout.this.locationType, true, 0);
            ChatAttachAlertLocationLayout.this.parentAlert.dismiss(true);
        }

        public /* synthetic */ void lambda$addInfoView$0(VenueLocation venueLocation, boolean z, int i) {
            ChatAttachAlertLocationLayout.this.delegate.didSelectLocation(venueLocation.venue, ChatAttachAlertLocationLayout.this.locationType, z, i);
            ChatAttachAlertLocationLayout.this.parentAlert.dismiss(true);
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$MapOverlayView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements ValueAnimator.AnimatorUpdateListener {
            private final float[] animatorValues = {0.0f, 1.0f};
            private boolean startedInner;
            final /* synthetic */ FrameLayout val$iconLayout;

            AnonymousClass1(FrameLayout frameLayout) {
                MapOverlayView.this = r1;
                this.val$iconLayout = frameLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                float lerp = AndroidUtilities.lerp(this.animatorValues, valueAnimator.getAnimatedFraction());
                if (lerp >= 0.7f && !this.startedInner && ChatAttachAlertLocationLayout.this.lastPressedMarkerView != null) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(ChatAttachAlertLocationLayout.this.lastPressedMarkerView, View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(ChatAttachAlertLocationLayout.this.lastPressedMarkerView, View.SCALE_Y, 0.0f, 1.0f), ObjectAnimator.ofFloat(ChatAttachAlertLocationLayout.this.lastPressedMarkerView, View.ALPHA, 0.0f, 1.0f));
                    animatorSet.setInterpolator(new OvershootInterpolator(1.02f));
                    animatorSet.setDuration(250L);
                    animatorSet.start();
                    this.startedInner = true;
                }
                if (lerp <= 0.5f) {
                    f = CubicBezierInterpolator.EASE_OUT.getInterpolation(lerp / 0.5f) * 1.1f;
                } else if (lerp <= 0.75f) {
                    f = 1.1f - (CubicBezierInterpolator.EASE_OUT.getInterpolation((lerp - 0.5f) / 0.25f) * 0.2f);
                } else {
                    f = (CubicBezierInterpolator.EASE_OUT.getInterpolation((lerp - 0.75f) / 0.25f) * 0.1f) + 0.9f;
                }
                this.val$iconLayout.setScaleX(f);
                this.val$iconLayout.setScaleY(f);
            }
        }

        public void removeInfoView(Marker marker) {
            View view = this.views.get(marker);
            if (view != null) {
                removeView(view);
                this.views.remove(marker);
            }
        }

        public void updatePositions() {
            if (ChatAttachAlertLocationLayout.this.googleMap == null) {
                return;
            }
            Projection projection = ChatAttachAlertLocationLayout.this.googleMap.getProjection();
            for (Map.Entry<Marker, View> entry : this.views.entrySet()) {
                View value = entry.getValue();
                android.graphics.Point screenLocation = projection.toScreenLocation(entry.getKey().getPosition());
                value.setTranslationX(screenLocation.x - (value.getMeasuredWidth() / 2));
                value.setTranslationY((screenLocation.y - value.getMeasuredHeight()) + AndroidUtilities.dp(22.0f));
            }
        }
    }

    public ChatAttachAlertLocationLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        ChatActivity chatActivity;
        this.locationDenied = false;
        int currentActionBarHeight = (AndroidUtilities.displaySize.x - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(66.0f);
        this.overScrollHeight = currentActionBarHeight;
        this.mapHeight = currentActionBarHeight;
        AndroidUtilities.fixGoogleMapsBug();
        ChatActivity chatActivity2 = (ChatActivity) this.parentAlert.baseFragment;
        this.dialogId = chatActivity2.getDialogId();
        if (chatActivity2.getCurrentEncryptedChat() == null && !chatActivity2.isInScheduleMode() && !UserObject.isUserSelf(chatActivity2.getCurrentUser())) {
            this.locationType = 1;
        } else {
            this.locationType = 0;
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionDenied);
        this.searchWas = false;
        this.searching = false;
        this.searchInProgress = false;
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.destroy();
        }
        LocationActivitySearchAdapter locationActivitySearchAdapter = this.searchAdapter;
        if (locationActivitySearchAdapter != null) {
            locationActivitySearchAdapter.destroy();
        }
        int i = Build.VERSION.SDK_INT;
        this.locationDenied = (i < 23 || getParentActivity() == null || getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) ? false : true;
        ActionBarMenu createMenu = this.parentAlert.actionBar.createMenu();
        this.overlayView = new MapOverlayView(context);
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass1());
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setVisibility(this.locationDenied ? 8 : 0);
        this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131628092));
        this.searchItem.setContentDescription(LocaleController.getString("Search", 2131628092));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setTextColor(getThemedColor("dialogTextBlack"));
        searchField.setCursorColor(getThemedColor("dialogTextBlack"));
        searchField.setHintTextColor(getThemedColor("chat_messagePanelHint"));
        new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(21.0f)).gravity = 83;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.mapViewClip = anonymousClass2;
        anonymousClass2.setWillNotDraw(false);
        View view = new View(context);
        this.loadingMapView = view;
        view.setBackgroundDrawable(new MapPlaceholderDrawable());
        SearchButton searchButton = new SearchButton(context);
        this.searchAreaButton = searchButton;
        searchButton.setTranslationX(-AndroidUtilities.dp(80.0f));
        this.searchAreaButton.setVisibility(4);
        Drawable createSimpleSelectorRoundRectDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(40.0f), getThemedColor("location_actionBackground"), getThemedColor("location_actionPressedBackground"));
        if (i < 21) {
            Drawable mutate = context.getResources().getDrawable(2131166062).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorRoundRectDrawable, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
            combinedDrawable.setFullsize(true);
            createSimpleSelectorRoundRectDrawable = combinedDrawable;
        } else {
            StateListAnimator stateListAnimator = new StateListAnimator();
            SearchButton searchButton2 = this.searchAreaButton;
            Property property = View.TRANSLATION_Z;
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(searchButton2, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.searchAreaButton, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.searchAreaButton.setStateListAnimator(stateListAnimator);
            this.searchAreaButton.setOutlineProvider(new AnonymousClass3(this));
        }
        this.searchAreaButton.setBackgroundDrawable(createSimpleSelectorRoundRectDrawable);
        this.searchAreaButton.setTextColor(getThemedColor("location_actionActiveIcon"));
        this.searchAreaButton.setTextSize(1, 14.0f);
        this.searchAreaButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.searchAreaButton.setText(LocaleController.getString("PlacesInThisArea", 2131627558));
        this.searchAreaButton.setGravity(17);
        this.searchAreaButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.mapViewClip.addView(this.searchAreaButton, LayoutHelper.createFrame(-2, i >= 21 ? 40.0f : 44.0f, 49, 80.0f, 12.0f, 80.0f, 0.0f));
        this.searchAreaButton.setOnClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda1(this));
        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, getThemedColor("location_actionIcon"), resourcesProvider);
        this.mapTypeButton = actionBarMenuItem;
        actionBarMenuItem.setClickable(true);
        this.mapTypeButton.setSubMenuOpenSide(2);
        this.mapTypeButton.setAdditionalXOffset(AndroidUtilities.dp(10.0f));
        this.mapTypeButton.setAdditionalYOffset(-AndroidUtilities.dp(10.0f));
        this.mapTypeButton.addSubItem(2, 2131165792, LocaleController.getString("Map", 2131626533), resourcesProvider);
        this.mapTypeButton.addSubItem(3, 2131165914, LocaleController.getString("Satellite", 2131628058), resourcesProvider);
        this.mapTypeButton.addSubItem(4, 2131165762, LocaleController.getString("Hybrid", 2131626168), resourcesProvider);
        this.mapTypeButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor("location_actionBackground"), getThemedColor("location_actionPressedBackground"));
        if (i < 21) {
            Drawable mutate2 = context.getResources().getDrawable(2131165415).mutate();
            mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate2, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable2.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable2;
            chatActivity = chatActivity2;
        } else {
            StateListAnimator stateListAnimator2 = new StateListAnimator();
            ActionBarMenuItem actionBarMenuItem2 = this.mapTypeButton;
            Property property2 = View.TRANSLATION_Z;
            chatActivity = chatActivity2;
            stateListAnimator2.addState(new int[]{16842919}, ObjectAnimator.ofFloat(actionBarMenuItem2, property2, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator2.addState(new int[0], ObjectAnimator.ofFloat(this.mapTypeButton, property2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.mapTypeButton.setStateListAnimator(stateListAnimator2);
            this.mapTypeButton.setOutlineProvider(new AnonymousClass4(this));
        }
        this.mapTypeButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        this.mapTypeButton.setIcon(2131165793);
        this.mapViewClip.addView(this.mapTypeButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 12.0f, 12.0f, 0.0f));
        this.mapTypeButton.setOnClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda2(this));
        this.mapTypeButton.setDelegate(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda19(this));
        this.locationButton = new ImageView(context);
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor("location_actionBackground"), getThemedColor("location_actionPressedBackground"));
        if (i < 21) {
            Drawable mutate3 = context.getResources().getDrawable(2131165415).mutate();
            mutate3.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable3 = new CombinedDrawable(mutate3, createSimpleSelectorCircleDrawable2, 0, 0);
            combinedDrawable3.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            createSimpleSelectorCircleDrawable2 = combinedDrawable3;
        } else {
            StateListAnimator stateListAnimator3 = new StateListAnimator();
            ImageView imageView = this.locationButton;
            Property property3 = View.TRANSLATION_Z;
            stateListAnimator3.addState(new int[]{16842919}, ObjectAnimator.ofFloat(imageView, property3, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator3.addState(new int[0], ObjectAnimator.ofFloat(this.locationButton, property3, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.locationButton.setStateListAnimator(stateListAnimator3);
            this.locationButton.setOutlineProvider(new AnonymousClass5(this));
        }
        this.locationButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
        this.locationButton.setImageResource(2131165699);
        this.locationButton.setScaleType(ImageView.ScaleType.CENTER);
        this.locationButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("location_actionActiveIcon"), PorterDuff.Mode.MULTIPLY));
        this.locationButton.setTag("location_actionActiveIcon");
        this.locationButton.setContentDescription(LocaleController.getString("AccDescrMyLocation", 2131624012));
        this.mapViewClip.addView(this.locationButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 85, 0.0f, 0.0f, 12.0f, 12.0f));
        this.locationButton.setOnClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda3(this));
        LinearLayout linearLayout = new LinearLayout(context);
        this.emptyView = linearLayout;
        linearLayout.setOrientation(1);
        this.emptyView.setGravity(1);
        this.emptyView.setPadding(0, AndroidUtilities.dp(160.0f), 0, 0);
        this.emptyView.setVisibility(8);
        addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.setOnTouchListener(ChatAttachAlertLocationLayout$$ExternalSyntheticLambda4.INSTANCE);
        ImageView imageView2 = new ImageView(context);
        this.emptyImageView = imageView2;
        imageView2.setImageResource(2131165586);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogEmptyImage"), PorterDuff.Mode.MULTIPLY));
        this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView = new TextView(context);
        this.emptyTitleTextView = textView;
        textView.setTextColor(getThemedColor("dialogEmptyText"));
        this.emptyTitleTextView.setGravity(17);
        this.emptyTitleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTitleTextView.setTextSize(1, 17.0f);
        this.emptyTitleTextView.setText(LocaleController.getString("NoPlacesFound", 2131626848));
        this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
        TextView textView2 = new TextView(context);
        this.emptySubtitleTextView = textView2;
        textView2.setTextColor(getThemedColor("dialogEmptyText"));
        this.emptySubtitleTextView.setGravity(17);
        this.emptySubtitleTextView.setTextSize(1, 15.0f);
        this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(context, resourcesProvider);
        this.listView = anonymousClass6;
        anonymousClass6.setClipToPadding(false);
        RecyclerListView recyclerListView = this.listView;
        LocationActivityAdapter locationActivityAdapter2 = new LocationActivityAdapter(context, this.locationType, this.dialogId, true, resourcesProvider);
        this.adapter = locationActivityAdapter2;
        recyclerListView.setAdapter(locationActivityAdapter2);
        this.adapter.setUpdateRunnable(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda15(this));
        this.adapter.setMyLocationDenied(this.locationDenied);
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView2 = this.listView;
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(context, 1, false, 0, recyclerListView2);
        this.layoutManager = anonymousClass7;
        recyclerListView2.setLayoutManager(anonymousClass7);
        addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setOnScrollListener(new AnonymousClass8());
        this.listView.setOnItemClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda26(this, chatActivity, resourcesProvider));
        this.adapter.setDelegate(this.dialogId, new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda22(this));
        this.adapter.setOverScrollHeight(this.overScrollHeight);
        addView(this.mapViewClip, LayoutHelper.createFrame(-1, -1, 51));
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(context);
        this.mapView = anonymousClass9;
        new Thread(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda17(this, anonymousClass9)).start();
        ImageView imageView3 = new ImageView(context);
        this.markerImageView = imageView3;
        imageView3.setImageResource(2131165595);
        this.mapViewClip.addView(this.markerImageView, LayoutHelper.createFrame(28, 48, 49));
        RecyclerListView recyclerListView3 = new RecyclerListView(context, resourcesProvider);
        this.searchListView = recyclerListView3;
        recyclerListView3.setVisibility(8);
        this.searchListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        AnonymousClass10 anonymousClass10 = new AnonymousClass10(context);
        this.searchAdapter = anonymousClass10;
        anonymousClass10.setDelegate(0L, new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda21(this));
        this.searchListView.setItemAnimator(null);
        addView(this.searchListView, LayoutHelper.createFrame(-1, -1, 51));
        this.searchListView.setOnScrollListener(new AnonymousClass11());
        this.searchListView.setOnItemClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda27(this, chatActivity, resourcesProvider));
        updateEmptyView();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass1() {
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            ChatAttachAlertLocationLayout.this.searching = true;
            ChatAttachAlertLocationLayout chatAttachAlertLocationLayout = ChatAttachAlertLocationLayout.this;
            chatAttachAlertLocationLayout.parentAlert.makeFocusable(chatAttachAlertLocationLayout.searchItem.getSearchField(), true);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            ChatAttachAlertLocationLayout.this.searching = false;
            ChatAttachAlertLocationLayout.this.searchWas = false;
            ChatAttachAlertLocationLayout.this.searchAdapter.searchDelayed(null, null);
            ChatAttachAlertLocationLayout.this.updateEmptyView();
            if (ChatAttachAlertLocationLayout.this.otherItem != null) {
                ChatAttachAlertLocationLayout.this.otherItem.setVisibility(0);
            }
            ChatAttachAlertLocationLayout.this.listView.setVisibility(0);
            ChatAttachAlertLocationLayout.this.mapViewClip.setVisibility(0);
            ChatAttachAlertLocationLayout.this.searchListView.setVisibility(8);
            ChatAttachAlertLocationLayout.this.emptyView.setVisibility(8);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            if (ChatAttachAlertLocationLayout.this.searchAdapter == null) {
                return;
            }
            String obj = editText.getText().toString();
            if (obj.length() != 0) {
                ChatAttachAlertLocationLayout.this.searchWas = true;
                ChatAttachAlertLocationLayout.this.searchItem.setShowSearchProgress(true);
                if (ChatAttachAlertLocationLayout.this.otherItem != null) {
                    ChatAttachAlertLocationLayout.this.otherItem.setVisibility(8);
                }
                ChatAttachAlertLocationLayout.this.listView.setVisibility(8);
                ChatAttachAlertLocationLayout.this.mapViewClip.setVisibility(8);
                if (ChatAttachAlertLocationLayout.this.searchListView.getAdapter() != ChatAttachAlertLocationLayout.this.searchAdapter) {
                    ChatAttachAlertLocationLayout.this.searchListView.setAdapter(ChatAttachAlertLocationLayout.this.searchAdapter);
                }
                ChatAttachAlertLocationLayout.this.searchListView.setVisibility(0);
                ChatAttachAlertLocationLayout chatAttachAlertLocationLayout = ChatAttachAlertLocationLayout.this;
                chatAttachAlertLocationLayout.searchInProgress = chatAttachAlertLocationLayout.searchAdapter.isEmpty();
                ChatAttachAlertLocationLayout.this.updateEmptyView();
            } else {
                if (ChatAttachAlertLocationLayout.this.otherItem != null) {
                    ChatAttachAlertLocationLayout.this.otherItem.setVisibility(0);
                }
                ChatAttachAlertLocationLayout.this.listView.setVisibility(0);
                ChatAttachAlertLocationLayout.this.mapViewClip.setVisibility(0);
                ChatAttachAlertLocationLayout.this.searchListView.setAdapter(null);
                ChatAttachAlertLocationLayout.this.searchListView.setVisibility(8);
                ChatAttachAlertLocationLayout.this.emptyView.setVisibility(8);
            }
            ChatAttachAlertLocationLayout.this.searchAdapter.searchDelayed(obj, ChatAttachAlertLocationLayout.this.userLocation);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (ChatAttachAlertLocationLayout.this.overlayView != null) {
                ChatAttachAlertLocationLayout.this.overlayView.updatePositions();
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatAttachAlertLocationLayout.this.clipSize);
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            ChatAttachAlertLocationLayout.this.backgroundPaint.setColor(ChatAttachAlertLocationLayout.this.getThemedColor("dialogBackground"));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() - ChatAttachAlertLocationLayout.this.clipSize, ChatAttachAlertLocationLayout.this.backgroundPaint);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getY() > getMeasuredHeight() - ChatAttachAlertLocationLayout.this.clipSize) {
                return false;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getY() > getMeasuredHeight() - ChatAttachAlertLocationLayout.this.clipSize) {
                return false;
            }
            return super.dispatchTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ViewOutlineProvider {
        AnonymousClass3(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), view.getMeasuredHeight() / 2);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        showSearchPlacesButton(false);
        this.adapter.searchPlacesWithQuery(null, this.userLocation, true, true);
        this.searchedForCustomLocations = true;
        showResults();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ViewOutlineProvider {
        AnonymousClass4(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        this.mapTypeButton.toggleSubMenu();
    }

    public /* synthetic */ void lambda$new$2(int i) {
        GoogleMap googleMap = this.googleMap;
        if (googleMap == null) {
            return;
        }
        if (i == 2) {
            googleMap.setMapType(1);
        } else if (i == 3) {
            googleMap.setMapType(2);
        } else if (i != 4) {
        } else {
            googleMap.setMapType(4);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends ViewOutlineProvider {
        AnonymousClass5(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
        }
    }

    public /* synthetic */ void lambda$new$3(View view) {
        Activity parentActivity;
        if (Build.VERSION.SDK_INT >= 23 && (parentActivity = getParentActivity()) != null && parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            AlertsCreator.createLocationRequiredDialog(getParentActivity(), true).show();
            return;
        }
        if (this.myLocation != null && this.googleMap != null) {
            this.locationButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("location_actionActiveIcon"), PorterDuff.Mode.MULTIPLY));
            this.locationButton.setTag("location_actionActiveIcon");
            this.adapter.setCustomLocation(null);
            this.userLocationMoved = false;
            showSearchPlacesButton(false);
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude())));
            if (this.searchedForCustomLocations) {
                Location location = this.myLocation;
                if (location != null) {
                    this.adapter.searchPlacesWithQuery(null, location, true, true);
                }
                this.searchedForCustomLocations = false;
                showResults();
            }
        }
        removeInfoView();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ChatAttachAlertLocationLayout.this.updateClipView();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends FillLastLinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context, int i, boolean z, int i2, RecyclerView recyclerView) {
            super(context, i, z, i2, recyclerView);
            ChatAttachAlertLocationLayout.this = r7;
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$7$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends LinearSmoothScroller {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                AnonymousClass7.this = r1;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateDyToMakeVisible(View view, int i) {
                return super.calculateDyToMakeVisible(view, i) - (ChatAttachAlertLocationLayout.this.listView.getPaddingTop() - (ChatAttachAlertLocationLayout.this.mapHeight - ChatAttachAlertLocationLayout.this.overScrollHeight));
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateTimeForDeceleration(int i) {
                return super.calculateTimeForDeceleration(i) * 4;
            }
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(recyclerView.getContext());
            anonymousClass1.setTargetPosition(i);
            startSmoothScroll(anonymousClass1);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends RecyclerView.OnScrollListener {
        AnonymousClass8() {
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            RecyclerListView.Holder holder;
            ChatAttachAlertLocationLayout.this.scrolling = i != 0;
            if (!ChatAttachAlertLocationLayout.this.scrolling && ChatAttachAlertLocationLayout.this.forceUpdate != null) {
                ChatAttachAlertLocationLayout.this.forceUpdate = null;
            }
            if (i == 0) {
                int dp = AndroidUtilities.dp(13.0f);
                int backgroundPaddingTop = ChatAttachAlertLocationLayout.this.parentAlert.getBackgroundPaddingTop();
                if (((ChatAttachAlertLocationLayout.this.parentAlert.scrollOffsetY[0] - backgroundPaddingTop) - dp) + backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || (holder = (RecyclerListView.Holder) ChatAttachAlertLocationLayout.this.listView.findViewHolderForAdapterPosition(0)) == null || holder.itemView.getTop() <= ChatAttachAlertLocationLayout.this.mapHeight - ChatAttachAlertLocationLayout.this.overScrollHeight) {
                    return;
                }
                ChatAttachAlertLocationLayout.this.listView.smoothScrollBy(0, holder.itemView.getTop() - (ChatAttachAlertLocationLayout.this.mapHeight - ChatAttachAlertLocationLayout.this.overScrollHeight));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            ChatAttachAlertLocationLayout.this.updateClipView();
            if (ChatAttachAlertLocationLayout.this.forceUpdate != null) {
                ChatAttachAlertLocationLayout.access$2816(ChatAttachAlertLocationLayout.this, i2);
            }
            ChatAttachAlertLocationLayout chatAttachAlertLocationLayout = ChatAttachAlertLocationLayout.this;
            chatAttachAlertLocationLayout.parentAlert.updateLayout(chatAttachAlertLocationLayout, true, i2);
        }
    }

    public /* synthetic */ void lambda$new$7(ChatActivity chatActivity, Theme.ResourcesProvider resourcesProvider, View view, int i) {
        if (i == 1) {
            if (this.delegate != null && this.userLocation != null) {
                FrameLayout frameLayout = this.lastPressedMarkerView;
                if (frameLayout != null) {
                    frameLayout.callOnClick();
                    return;
                }
                TLRPC$TL_messageMediaGeo tLRPC$TL_messageMediaGeo = new TLRPC$TL_messageMediaGeo();
                TLRPC$TL_geoPoint tLRPC$TL_geoPoint = new TLRPC$TL_geoPoint();
                tLRPC$TL_messageMediaGeo.geo = tLRPC$TL_geoPoint;
                tLRPC$TL_geoPoint.lat = AndroidUtilities.fixLocationCoord(this.userLocation.getLatitude());
                tLRPC$TL_messageMediaGeo.geo._long = AndroidUtilities.fixLocationCoord(this.userLocation.getLongitude());
                if (chatActivity.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), chatActivity.getDialogId(), new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda24(this, tLRPC$TL_messageMediaGeo), resourcesProvider);
                    return;
                }
                this.delegate.didSelectLocation(tLRPC$TL_messageMediaGeo, this.locationType, true, 0);
                this.parentAlert.dismiss(true);
            } else if (!this.locationDenied) {
            } else {
                AlertsCreator.createLocationRequiredDialog(getParentActivity(), true).show();
            }
        } else if (i == 2 && this.locationType == 1) {
            if (getLocationController().isSharingLocation(this.dialogId)) {
                getLocationController().removeSharingLocation(this.dialogId);
                this.parentAlert.dismiss(true);
            } else if (this.myLocation == null && this.locationDenied) {
                AlertsCreator.createLocationRequiredDialog(getParentActivity(), true).show();
            } else {
                openShareLiveLocation();
            }
        } else {
            Object item = this.adapter.getItem(i);
            if (item instanceof TLRPC$TL_messageMediaVenue) {
                if (chatActivity.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), chatActivity.getDialogId(), new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda23(this, item), resourcesProvider);
                    return;
                }
                this.delegate.didSelectLocation((TLRPC$TL_messageMediaVenue) item, this.locationType, true, 0);
                this.parentAlert.dismiss(true);
            } else if (!(item instanceof LiveLocation)) {
            } else {
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(((LiveLocation) item).marker.getPosition(), this.googleMap.getMaxZoomLevel() - 4.0f));
            }
        }
    }

    public /* synthetic */ void lambda$new$5(TLRPC$TL_messageMediaGeo tLRPC$TL_messageMediaGeo, boolean z, int i) {
        this.delegate.didSelectLocation(tLRPC$TL_messageMediaGeo, this.locationType, z, i);
        this.parentAlert.dismiss(true);
    }

    public /* synthetic */ void lambda$new$6(Object obj, boolean z, int i) {
        this.delegate.didSelectLocation((TLRPC$TL_messageMediaVenue) obj, this.locationType, z, i);
        this.parentAlert.dismiss(true);
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends MapView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context) {
            super(context);
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            MotionEvent motionEvent2;
            if (ChatAttachAlertLocationLayout.this.yOffset != 0.0f) {
                motionEvent = MotionEvent.obtain(motionEvent);
                motionEvent.offsetLocation(0.0f, (-ChatAttachAlertLocationLayout.this.yOffset) / 2.0f);
                motionEvent2 = motionEvent;
            } else {
                motionEvent2 = null;
            }
            boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            if (motionEvent2 != null) {
                motionEvent2.recycle();
            }
            return dispatchTouchEvent;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (ChatAttachAlertLocationLayout.this.animatorSet != null) {
                    ChatAttachAlertLocationLayout.this.animatorSet.cancel();
                }
                ChatAttachAlertLocationLayout.this.animatorSet = new AnimatorSet();
                ChatAttachAlertLocationLayout.this.animatorSet.setDuration(200L);
                ChatAttachAlertLocationLayout.this.animatorSet.playTogether(ObjectAnimator.ofFloat(ChatAttachAlertLocationLayout.this.markerImageView, View.TRANSLATION_Y, ChatAttachAlertLocationLayout.this.markerTop - AndroidUtilities.dp(10.0f)));
                ChatAttachAlertLocationLayout.this.animatorSet.start();
            } else if (motionEvent.getAction() == 1) {
                if (ChatAttachAlertLocationLayout.this.animatorSet != null) {
                    ChatAttachAlertLocationLayout.this.animatorSet.cancel();
                }
                ChatAttachAlertLocationLayout.this.yOffset = 0.0f;
                ChatAttachAlertLocationLayout.this.animatorSet = new AnimatorSet();
                ChatAttachAlertLocationLayout.this.animatorSet.setDuration(200L);
                ChatAttachAlertLocationLayout.this.animatorSet.playTogether(ObjectAnimator.ofFloat(ChatAttachAlertLocationLayout.this.markerImageView, View.TRANSLATION_Y, ChatAttachAlertLocationLayout.this.markerTop));
                ChatAttachAlertLocationLayout.this.animatorSet.start();
                ChatAttachAlertLocationLayout.this.adapter.fetchLocationAddress();
            }
            if (motionEvent.getAction() == 2) {
                if (!ChatAttachAlertLocationLayout.this.userLocationMoved) {
                    ChatAttachAlertLocationLayout.this.locationButton.setColorFilter(new PorterDuffColorFilter(ChatAttachAlertLocationLayout.this.getThemedColor("location_actionIcon"), PorterDuff.Mode.MULTIPLY));
                    ChatAttachAlertLocationLayout.this.locationButton.setTag("location_actionIcon");
                    ChatAttachAlertLocationLayout.this.userLocationMoved = true;
                }
                if (ChatAttachAlertLocationLayout.this.googleMap != null && ChatAttachAlertLocationLayout.this.userLocation != null) {
                    ChatAttachAlertLocationLayout.this.userLocation.setLatitude(ChatAttachAlertLocationLayout.this.googleMap.getCameraPosition().target.latitude);
                    ChatAttachAlertLocationLayout.this.userLocation.setLongitude(ChatAttachAlertLocationLayout.this.googleMap.getCameraPosition().target.longitude);
                }
                ChatAttachAlertLocationLayout.this.adapter.setCustomLocation(ChatAttachAlertLocationLayout.this.userLocation);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    public /* synthetic */ void lambda$new$12(MapView mapView) {
        try {
            mapView.onCreate(null);
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda16(this, mapView));
    }

    public /* synthetic */ void lambda$new$11(MapView mapView) {
        if (this.mapView == null || getParentActivity() == null) {
            return;
        }
        try {
            mapView.onCreate(null);
            MapsInitializer.initialize(ApplicationLoader.applicationContext);
            this.mapView.getMapAsync(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda10(this));
            this.mapsInitialized = true;
            if (!this.onResumeCalled) {
                return;
            }
            this.mapView.onResume();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$new$10(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLoadedCallback(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda7(this));
        if (isActiveThemeDark()) {
            this.currentMapStyleDark = true;
            this.googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ApplicationLoader.applicationContext, 2131558483));
        }
        onMapInit();
    }

    public /* synthetic */ void lambda$new$9() {
        AndroidUtilities.runOnUIThread(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda14(this));
    }

    public /* synthetic */ void lambda$new$8() {
        this.loadingMapView.setTag(1);
        this.loadingMapView.animate().alpha(0.0f).setDuration(180L).start();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends LocationActivitySearchAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context) {
            super(context);
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            if (ChatAttachAlertLocationLayout.this.searchItem != null) {
                ChatAttachAlertLocationLayout.this.searchItem.setShowSearchProgress(ChatAttachAlertLocationLayout.this.searchAdapter.isSearching());
            }
            if (ChatAttachAlertLocationLayout.this.emptySubtitleTextView != null) {
                ChatAttachAlertLocationLayout.this.emptySubtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("NoPlacesFoundInfo", 2131626849, ChatAttachAlertLocationLayout.this.searchAdapter.getLastSearchString())));
            }
            super.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$new$13(ArrayList arrayList) {
        this.searchInProgress = false;
        updateEmptyView();
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertLocationLayout$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends RecyclerView.OnScrollListener {
        AnonymousClass11() {
            ChatAttachAlertLocationLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i != 1 || !ChatAttachAlertLocationLayout.this.searching || !ChatAttachAlertLocationLayout.this.searchWas) {
                return;
            }
            AndroidUtilities.hideKeyboard(ChatAttachAlertLocationLayout.this.parentAlert.getCurrentFocus());
        }
    }

    public /* synthetic */ void lambda$new$15(ChatActivity chatActivity, Theme.ResourcesProvider resourcesProvider, View view, int i) {
        TLRPC$TL_messageMediaVenue item = this.searchAdapter.getItem(i);
        if (item == null || this.delegate == null) {
            return;
        }
        if (chatActivity.isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), chatActivity.getDialogId(), new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda25(this, item), resourcesProvider);
            return;
        }
        this.delegate.didSelectLocation(item, this.locationType, true, 0);
        this.parentAlert.dismiss(true);
    }

    public /* synthetic */ void lambda$new$14(TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue, boolean z, int i) {
        this.delegate.didSelectLocation(tLRPC$TL_messageMediaVenue, this.locationType, z, i);
        this.parentAlert.dismiss(true);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    boolean shouldHideBottomButtons() {
        return !this.locationDenied;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onPause() {
        MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onPause();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        this.onResumeCalled = false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionDenied);
        try {
            GoogleMap googleMap = this.googleMap;
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(false);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        MapView mapView = this.mapView;
        if (mapView != null) {
            mapView.setTranslationY((-AndroidUtilities.displaySize.y) * 3);
        }
        try {
            MapView mapView2 = this.mapView;
            if (mapView2 != null) {
                mapView2.onPause();
            }
        } catch (Exception unused) {
        }
        try {
            MapView mapView3 = this.mapView;
            if (mapView3 != null) {
                mapView3.onDestroy();
                this.mapView = null;
            }
        } catch (Exception unused2) {
        }
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.destroy();
        }
        LocationActivitySearchAdapter locationActivitySearchAdapter = this.searchAdapter;
        if (locationActivitySearchAdapter != null) {
            locationActivitySearchAdapter.destroy();
        }
        this.parentAlert.actionBar.closeSearchField();
        this.parentAlert.actionBar.createMenu().removeView(this.searchItem);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHide() {
        this.searchItem.setVisibility(8);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onDismiss() {
        onDestroy();
        return false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            return Integer.MAX_VALUE;
        }
        int i = 0;
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findViewHolderForAdapterPosition(0);
        if (holder != null) {
            i = Math.max(((int) holder.itemView.getY()) - this.nonClipSize, 0);
        }
        return i + AndroidUtilities.dp(56.0f);
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
        updateClipView();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003e  */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void onPreMeasure(int i, int i2) {
        int i3;
        int i4;
        if (this.parentAlert.actionBar.isSearchFieldVisible() || this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            i3 = this.mapHeight - this.overScrollHeight;
            this.parentAlert.setAllowNestedScroll(false);
        } else {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i4 = (int) (i2 / 3.5f);
                    i3 = i4 - AndroidUtilities.dp(52.0f);
                    if (i3 < 0) {
                        i3 = 0;
                    }
                    this.parentAlert.setAllowNestedScroll(true);
                }
            }
            i4 = (i2 / 5) * 2;
            i3 = i4 - AndroidUtilities.dp(52.0f);
            if (i3 < 0) {
            }
            this.parentAlert.setAllowNestedScroll(true);
        }
        if (this.listView.getPaddingTop() != i3) {
            this.ignoreLayout = true;
            this.listView.setPadding(0, i3, 0, 0);
            this.ignoreLayout = false;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            fixLayoutInternal(this.first);
            this.first = false;
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getButtonsHideOffset() {
        return AndroidUtilities.dp(56.0f);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void scrollToTop() {
        this.listView.smoothScrollToPosition(0);
    }

    private boolean isActiveThemeDark() {
        return Theme.getActiveTheme().isDark() || AndroidUtilities.computePerceivedBrightness(getThemedColor("windowBackgroundWhite")) < 0.721f;
    }

    public void updateEmptyView() {
        if (this.searching) {
            if (this.searchInProgress) {
                this.searchListView.setEmptyView(null);
                this.emptyView.setVisibility(8);
                return;
            }
            this.searchListView.setEmptyView(this.emptyView);
            return;
        }
        this.emptyView.setVisibility(8);
    }

    public void showSearchPlacesButton(boolean z) {
        SearchButton searchButton;
        Location location;
        Location location2;
        if (z && (searchButton = this.searchAreaButton) != null && searchButton.getTag() == null && ((location = this.myLocation) == null || (location2 = this.userLocation) == null || location2.distanceTo(location) < 300.0f)) {
            z = false;
        }
        SearchButton searchButton2 = this.searchAreaButton;
        if (searchButton2 != null) {
            if (z && searchButton2.getTag() != null) {
                return;
            }
            if (!z && this.searchAreaButton.getTag() == null) {
                return;
            }
            this.searchAreaButton.setVisibility(z ? 0 : 4);
            this.searchAreaButton.setTag(z ? 1 : null);
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[1];
            SearchButton searchButton3 = this.searchAreaButton;
            Property property = View.TRANSLATION_X;
            float[] fArr = new float[1];
            fArr[0] = z ? 0.0f : -AndroidUtilities.dp(80.0f);
            animatorArr[0] = ObjectAnimator.ofFloat(searchButton3, property, fArr);
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(180L);
            animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            animatorSet.start();
        }
    }

    public void openShareLiveLocation() {
        Activity parentActivity;
        if (this.delegate == null || getParentActivity() == null || this.myLocation == null) {
            return;
        }
        if (this.checkBackgroundPermission && Build.VERSION.SDK_INT >= 29 && (parentActivity = getParentActivity()) != null) {
            this.checkBackgroundPermission = false;
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (Math.abs((System.currentTimeMillis() / 1000) - globalMainSettings.getInt("backgroundloc", 0)) > 86400 && parentActivity.checkSelfPermission("android.permission.ACCESS_BACKGROUND_LOCATION") != 0) {
                globalMainSettings.edit().putInt("backgroundloc", (int) (System.currentTimeMillis() / 1000)).commit();
                AlertsCreator.createBackgroundLocationPermissionDialog(parentActivity, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda11(this), this.resourcesProvider).show();
                return;
            }
        }
        TLRPC$User tLRPC$User = null;
        if (DialogObject.isUserDialog(this.dialogId)) {
            tLRPC$User = this.parentAlert.baseFragment.getMessagesController().getUser(Long.valueOf(this.dialogId));
        }
        AlertsCreator.createLocationUpdateDialog(getParentActivity(), tLRPC$User, new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda18(this), this.resourcesProvider).show();
    }

    public /* synthetic */ void lambda$openShareLiveLocation$16(int i) {
        TLRPC$TL_messageMediaGeoLive tLRPC$TL_messageMediaGeoLive = new TLRPC$TL_messageMediaGeoLive();
        TLRPC$TL_geoPoint tLRPC$TL_geoPoint = new TLRPC$TL_geoPoint();
        tLRPC$TL_messageMediaGeoLive.geo = tLRPC$TL_geoPoint;
        tLRPC$TL_geoPoint.lat = AndroidUtilities.fixLocationCoord(this.myLocation.getLatitude());
        tLRPC$TL_messageMediaGeoLive.geo._long = AndroidUtilities.fixLocationCoord(this.myLocation.getLongitude());
        tLRPC$TL_messageMediaGeoLive.period = i;
        this.delegate.didSelectLocation(tLRPC$TL_messageMediaGeoLive, this.locationType, true, 0);
        this.parentAlert.dismiss(true);
    }

    private Bitmap createPlaceBitmap(int i) {
        Bitmap[] bitmapArr = this.bitmapCache;
        int i2 = i % 7;
        if (bitmapArr[i2] != null) {
            return bitmapArr[i2];
        }
        try {
            Paint paint = new Paint(1);
            paint.setColor(-1);
            Bitmap createBitmap = Bitmap.createBitmap(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawCircle(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), paint);
            paint.setColor(LocationCell.getColorForIndex(i));
            canvas.drawCircle(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(5.0f), paint);
            canvas.setBitmap(null);
            this.bitmapCache[i % 7] = createBitmap;
            return createBitmap;
        } catch (Throwable th) {
            FileLog.e(th);
            return null;
        }
    }

    public void updatePlacesMarkers(ArrayList<TLRPC$TL_messageMediaVenue> arrayList) {
        if (arrayList == null) {
            return;
        }
        int size = this.placeMarkers.size();
        for (int i = 0; i < size; i++) {
            this.placeMarkers.get(i).marker.remove();
        }
        this.placeMarkers.clear();
        int size2 = arrayList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = arrayList.get(i2);
            try {
                MarkerOptions markerOptions = new MarkerOptions();
                TLRPC$GeoPoint tLRPC$GeoPoint = tLRPC$TL_messageMediaVenue.geo;
                MarkerOptions position = markerOptions.position(new LatLng(tLRPC$GeoPoint.lat, tLRPC$GeoPoint._long));
                position.icon(BitmapDescriptorFactory.fromBitmap(createPlaceBitmap(i2)));
                position.anchor(0.5f, 0.5f);
                position.title(tLRPC$TL_messageMediaVenue.title);
                position.snippet(tLRPC$TL_messageMediaVenue.address);
                VenueLocation venueLocation = new VenueLocation();
                venueLocation.num = i2;
                Marker addMarker = this.googleMap.addMarker(position);
                venueLocation.marker = addMarker;
                venueLocation.venue = tLRPC$TL_messageMediaVenue;
                addMarker.setTag(venueLocation);
                this.placeMarkers.add(venueLocation);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private MessagesController getMessagesController() {
        return this.parentAlert.baseFragment.getMessagesController();
    }

    private LocationController getLocationController() {
        return this.parentAlert.baseFragment.getLocationController();
    }

    private UserConfig getUserConfig() {
        return this.parentAlert.baseFragment.getUserConfig();
    }

    public Activity getParentActivity() {
        BaseFragment baseFragment;
        ChatAttachAlert chatAttachAlert = this.parentAlert;
        if (chatAttachAlert == null || (baseFragment = chatAttachAlert.baseFragment) == null) {
            return null;
        }
        return baseFragment.getParentActivity();
    }

    private void onMapInit() {
        if (this.googleMap == null) {
            return;
        }
        Location location = new Location("network");
        this.userLocation = location;
        location.setLatitude(20.659322d);
        this.userLocation.setLongitude(-11.40625d);
        try {
            this.googleMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setCompassEnabled(false);
        this.googleMap.setOnCameraMoveStartedListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda6(this));
        this.googleMap.setOnMyLocationChangeListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda9(this));
        this.googleMap.setOnMarkerClickListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda8(this));
        this.googleMap.setOnCameraMoveListener(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda5(this));
        AndroidUtilities.runOnUIThread(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda13(this), 200L);
        Location lastLocation = getLastLocation();
        this.myLocation = lastLocation;
        positionMarker(lastLocation);
        if (this.checkGpsEnabled && getParentActivity() != null) {
            this.checkGpsEnabled = false;
            if (!getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                return;
            }
            try {
                if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity(), this.resourcesProvider);
                    builder.setTopAnimation(2131558496, 72, false, Theme.getColor("dialogTopBackground"));
                    builder.setMessage(LocaleController.getString("GpsDisabledAlertText", 2131626083));
                    builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", 2131625228), new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda0(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                    builder.show();
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        updateClipView();
    }

    public /* synthetic */ void lambda$onMapInit$17(int i) {
        View childAt;
        RecyclerView.ViewHolder findContainingViewHolder;
        if (i == 1) {
            showSearchPlacesButton(true);
            removeInfoView();
            if (this.scrolling || this.listView.getChildCount() <= 0 || (childAt = this.listView.getChildAt(0)) == null || (findContainingViewHolder = this.listView.findContainingViewHolder(childAt)) == null || findContainingViewHolder.getAdapterPosition() != 0) {
                return;
            }
            int dp = this.locationType == 0 ? 0 : AndroidUtilities.dp(66.0f);
            int top = childAt.getTop();
            if (top >= (-dp)) {
                return;
            }
            CameraPosition cameraPosition = this.googleMap.getCameraPosition();
            this.forceUpdate = CameraUpdateFactory.newLatLngZoom(cameraPosition.target, cameraPosition.zoom);
            this.listView.smoothScrollBy(0, top + dp);
        }
    }

    public /* synthetic */ void lambda$onMapInit$18(Location location) {
        ChatAttachAlert chatAttachAlert = this.parentAlert;
        if (chatAttachAlert == null || chatAttachAlert.baseFragment == null) {
            return;
        }
        positionMarker(location);
        getLocationController().setGoogleMapLocation(location, this.isFirstLocation);
        this.isFirstLocation = false;
    }

    public /* synthetic */ boolean lambda$onMapInit$19(Marker marker) {
        if (!(marker.getTag() instanceof VenueLocation)) {
            return true;
        }
        this.markerImageView.setVisibility(4);
        if (!this.userLocationMoved) {
            this.locationButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("location_actionIcon"), PorterDuff.Mode.MULTIPLY));
            this.locationButton.setTag("location_actionIcon");
            this.userLocationMoved = true;
        }
        this.overlayView.addInfoView(marker);
        return true;
    }

    public /* synthetic */ void lambda$onMapInit$20() {
        MapOverlayView mapOverlayView = this.overlayView;
        if (mapOverlayView != null) {
            mapOverlayView.updatePositions();
        }
    }

    public /* synthetic */ void lambda$onMapInit$21() {
        if (this.loadingMapView.getTag() == null) {
            this.loadingMapView.animate().alpha(0.0f).setDuration(180L).start();
        }
    }

    public /* synthetic */ void lambda$onMapInit$22(DialogInterface dialogInterface, int i) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        } catch (Exception unused) {
        }
    }

    private void removeInfoView() {
        if (this.lastPressedMarker != null) {
            this.markerImageView.setVisibility(0);
            this.overlayView.removeInfoView(this.lastPressedMarker);
            this.lastPressedMarker = null;
            this.lastPressedVenue = null;
            this.lastPressedMarkerView = null;
        }
    }

    private void showResults() {
        if (this.adapter.getItemCount() != 0 && this.layoutManager.findFirstVisibleItemPosition() == 0) {
            int dp = AndroidUtilities.dp(258.0f) + this.listView.getChildAt(0).getTop();
            if (dp < 0 || dp > AndroidUtilities.dp(258.0f)) {
                return;
            }
            this.listView.smoothScrollBy(0, dp);
        }
    }

    public void updateClipView() {
        int i;
        int i2;
        LatLng latLng;
        if (this.mapView == null || this.mapViewClip == null) {
            return;
        }
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(0);
        if (findViewHolderForAdapterPosition != null) {
            i2 = (int) findViewHolderForAdapterPosition.itemView.getY();
            i = this.overScrollHeight + Math.min(i2, 0);
        } else {
            i2 = -this.mapViewClip.getMeasuredHeight();
            i = 0;
        }
        if (((FrameLayout.LayoutParams) this.mapViewClip.getLayoutParams()) == null) {
            return;
        }
        if (i <= 0) {
            if (this.mapView.getVisibility() == 0) {
                this.mapView.setVisibility(4);
                this.mapViewClip.setVisibility(4);
                MapOverlayView mapOverlayView = this.overlayView;
                if (mapOverlayView != null) {
                    mapOverlayView.setVisibility(4);
                }
            }
            this.mapView.setTranslationY(i2);
            return;
        }
        if (this.mapView.getVisibility() == 4) {
            this.mapView.setVisibility(0);
            this.mapViewClip.setVisibility(0);
            MapOverlayView mapOverlayView2 = this.overlayView;
            if (mapOverlayView2 != null) {
                mapOverlayView2.setVisibility(0);
            }
        }
        int max = Math.max(0, (-((i2 - this.mapHeight) + this.overScrollHeight)) / 2);
        int i3 = this.mapHeight - this.overScrollHeight;
        float max2 = 1.0f - Math.max(0.0f, Math.min(1.0f, (this.listView.getPaddingTop() - i2) / (this.listView.getPaddingTop() - i3)));
        int i4 = this.clipSize;
        if (this.locationDenied && isTypeSend()) {
            i3 += Math.min(i2, this.listView.getPaddingTop());
        }
        this.clipSize = (int) (i3 * max2);
        float f = max;
        this.mapView.setTranslationY(f);
        this.nonClipSize = i3 - this.clipSize;
        this.mapViewClip.invalidate();
        this.mapViewClip.setTranslationY(i2 - this.nonClipSize);
        GoogleMap googleMap = this.googleMap;
        if (googleMap != null) {
            googleMap.setPadding(0, AndroidUtilities.dp(6.0f), 0, this.clipSize + AndroidUtilities.dp(6.0f));
        }
        MapOverlayView mapOverlayView3 = this.overlayView;
        if (mapOverlayView3 != null) {
            mapOverlayView3.setTranslationY(f);
        }
        float min = Math.min(Math.max(this.nonClipSize - i2, 0), (this.mapHeight - this.mapTypeButton.getMeasuredHeight()) - AndroidUtilities.dp(80.0f));
        this.mapTypeButton.setTranslationY(min);
        this.searchAreaButton.setTranslation(min);
        this.locationButton.setTranslationY(-this.clipSize);
        ImageView imageView = this.markerImageView;
        int dp = (((this.mapHeight - this.clipSize) / 2) - AndroidUtilities.dp(48.0f)) + max;
        this.markerTop = dp;
        imageView.setTranslationY(dp);
        if (i4 != this.clipSize) {
            Marker marker = this.lastPressedMarker;
            if (marker != null) {
                latLng = marker.getPosition();
            } else if (this.userLocationMoved) {
                latLng = new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude());
            } else {
                latLng = this.myLocation != null ? new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude()) : null;
            }
            if (latLng != null) {
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
        if (!this.locationDenied || !isTypeSend()) {
            return;
        }
        int itemCount = this.adapter.getItemCount();
        for (int i5 = 1; i5 < itemCount; i5++) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(i5);
            if (findViewHolderForAdapterPosition2 != null) {
                findViewHolderForAdapterPosition2.itemView.setTranslationY(this.listView.getPaddingTop() - i2);
            }
        }
    }

    private boolean isTypeSend() {
        int i = this.locationType;
        return i == 0 || i == 1;
    }

    private int buttonsHeight() {
        int dp = AndroidUtilities.dp(66.0f);
        return this.locationType == 1 ? dp + AndroidUtilities.dp(66.0f) : dp;
    }

    private void fixLayoutInternal(boolean z) {
        FrameLayout.LayoutParams layoutParams;
        if (getMeasuredHeight() == 0 || this.mapView == null) {
            return;
        }
        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
        int buttonsHeight = ((AndroidUtilities.displaySize.y - currentActionBarHeight) - buttonsHeight()) - AndroidUtilities.dp(90.0f);
        int dp = AndroidUtilities.dp(189.0f);
        this.overScrollHeight = dp;
        if (!this.locationDenied || !isTypeSend()) {
            buttonsHeight = Math.min(AndroidUtilities.dp(310.0f), buttonsHeight);
        }
        this.mapHeight = Math.max(dp, buttonsHeight);
        if (this.locationDenied && isTypeSend()) {
            this.overScrollHeight = this.mapHeight;
        }
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
        layoutParams2.topMargin = currentActionBarHeight;
        this.listView.setLayoutParams(layoutParams2);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mapViewClip.getLayoutParams();
        layoutParams3.topMargin = currentActionBarHeight;
        layoutParams3.height = this.mapHeight;
        this.mapViewClip.setLayoutParams(layoutParams3);
        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) this.searchListView.getLayoutParams();
        layoutParams4.topMargin = currentActionBarHeight;
        this.searchListView.setLayoutParams(layoutParams4);
        this.adapter.setOverScrollHeight((!this.locationDenied || !isTypeSend()) ? this.overScrollHeight : this.overScrollHeight - this.listView.getPaddingTop());
        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) this.mapView.getLayoutParams();
        if (layoutParams5 != null) {
            layoutParams5.height = this.mapHeight + AndroidUtilities.dp(10.0f);
            this.mapView.setLayoutParams(layoutParams5);
        }
        MapOverlayView mapOverlayView = this.overlayView;
        if (mapOverlayView != null && (layoutParams = (FrameLayout.LayoutParams) mapOverlayView.getLayoutParams()) != null) {
            layoutParams.height = this.mapHeight + AndroidUtilities.dp(10.0f);
            this.overlayView.setLayoutParams(layoutParams);
        }
        this.adapter.notifyDataSetChanged();
        updateClipView();
    }

    private Location getLastLocation() {
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (int size = providers.size() - 1; size >= 0; size--) {
            location = locationManager.getLastKnownLocation(providers.get(size));
            if (location != null) {
                break;
            }
        }
        return location;
    }

    private void positionMarker(Location location) {
        if (location == null) {
            return;
        }
        Location location2 = new Location(location);
        this.myLocation = location2;
        if (this.googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LocationActivityAdapter locationActivityAdapter = this.adapter;
            if (locationActivityAdapter != null) {
                if (!this.searchedForCustomLocations) {
                    locationActivityAdapter.searchPlacesWithQuery(null, this.myLocation, true);
                }
                this.adapter.setGpsLocation(this.myLocation);
            }
            if (this.userLocationMoved) {
                return;
            }
            this.userLocation = new Location(location);
            if (this.firstWas) {
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                return;
            }
            this.firstWas = true;
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, this.googleMap.getMaxZoomLevel() - 4.0f));
            return;
        }
        this.adapter.setGpsLocation(location2);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3 = 0;
        if (i == NotificationCenter.locationPermissionGranted) {
            this.locationDenied = false;
            LocationActivityAdapter locationActivityAdapter = this.adapter;
            if (locationActivityAdapter != null) {
                locationActivityAdapter.setMyLocationDenied(false);
            }
            GoogleMap googleMap = this.googleMap;
            if (googleMap != null) {
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } else if (i == NotificationCenter.locationPermissionDenied) {
            this.locationDenied = true;
            LocationActivityAdapter locationActivityAdapter2 = this.adapter;
            if (locationActivityAdapter2 != null) {
                locationActivityAdapter2.setMyLocationDenied(true);
            }
        }
        fixLayoutInternal(true);
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (this.locationDenied) {
            i3 = 8;
        }
        actionBarMenuItem.setVisibility(i3);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onResume() {
        MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onResume();
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        this.onResumeCalled = true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.parentAlert.actionBar.setTitle(LocaleController.getString("ShareLocation", 2131628276));
        if (this.mapView.getParent() == null) {
            this.mapViewClip.addView(this.mapView, 0, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0f), 51));
            this.mapViewClip.addView(this.overlayView, 1, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0f), 51));
            this.mapViewClip.addView(this.loadingMapView, 2, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.searchItem.setVisibility(0);
        MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onResume();
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        this.onResumeCalled = true;
        GoogleMap googleMap = this.googleMap;
        if (googleMap != null) {
            try {
                googleMap.setMyLocationEnabled(true);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        fixLayoutInternal(true);
        AndroidUtilities.runOnUIThread(new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda12(this), this.parentAlert.delegate.needEnterComment() ? 200L : 0L);
        this.layoutManager.scrollToPositionWithOffset(0, 0);
        updateClipView();
    }

    public /* synthetic */ void lambda$onShow$23() {
        Activity parentActivity;
        if (!this.checkPermission || Build.VERSION.SDK_INT < 23 || (parentActivity = getParentActivity()) == null) {
            return;
        }
        this.checkPermission = false;
        if (parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
            return;
        }
        parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
    }

    public void setDelegate(LocationActivityDelegate locationActivityDelegate) {
        this.delegate = locationActivityDelegate;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChatAttachAlertLocationLayout$$ExternalSyntheticLambda20 chatAttachAlertLocationLayout$$ExternalSyntheticLambda20 = new ChatAttachAlertLocationLayout$$ExternalSyntheticLambda20(this);
        arrayList.add(new ThemeDescription(this.mapViewClip, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "dialogScrollGlow"));
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        arrayList.add(new ThemeDescription(actionBarMenuItem != null ? actionBarMenuItem.getSearchField() : null, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "dialogEmptyImage"));
        arrayList.add(new ThemeDescription(this.emptyTitleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogEmptyText"));
        arrayList.add(new ThemeDescription(this.emptySubtitleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogEmptyText"));
        arrayList.add(new ThemeDescription(this.locationButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "location_actionIcon"));
        arrayList.add(new ThemeDescription(this.locationButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "location_actionActiveIcon"));
        arrayList.add(new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "location_actionBackground"));
        arrayList.add(new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "location_actionPressedBackground"));
        arrayList.add(new ThemeDescription(this.mapTypeButton, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "location_actionIcon"));
        arrayList.add(new ThemeDescription(this.mapTypeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "location_actionBackground"));
        arrayList.add(new ThemeDescription(this.mapTypeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "location_actionPressedBackground"));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "location_actionActiveIcon"));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "location_actionBackground"));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "location_actionPressedBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatAttachAlertLocationLayout$$ExternalSyntheticLambda20, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "location_liveLocationProgress"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "location_placeLocationBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialog_liveLocationProgress"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLocationIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLiveLocationIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLocationBackground"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLiveLocationBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SendLocationCell.class}, new String[]{"accurateTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLiveLocationText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "location_sendLocationText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationDirectionCell.class}, new String[]{"buttonTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{LocationDirectionCell.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{LocationDirectionCell.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButtonPressed"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogTextBlue2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.searchListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SharingLiveLocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SharingLiveLocationCell.class}, new String[]{"distanceTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{LocationPoweredCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogEmptyImage"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogEmptyText"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$24() {
        this.mapTypeButton.setIconColor(getThemedColor("location_actionIcon"));
        this.mapTypeButton.redrawPopup(getThemedColor("actionBarDefaultSubmenuBackground"));
        this.mapTypeButton.setPopupItemsColor(getThemedColor("actionBarDefaultSubmenuItemIcon"), true);
        this.mapTypeButton.setPopupItemsColor(getThemedColor("actionBarDefaultSubmenuItem"), false);
        if (this.googleMap != null) {
            if (isActiveThemeDark()) {
                if (this.currentMapStyleDark) {
                    return;
                }
                this.currentMapStyleDark = true;
                this.googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ApplicationLoader.applicationContext, 2131558483));
            } else if (!this.currentMapStyleDark) {
            } else {
                this.currentMapStyleDark = false;
                this.googleMap.setMapStyle(null);
            }
        }
    }
}
