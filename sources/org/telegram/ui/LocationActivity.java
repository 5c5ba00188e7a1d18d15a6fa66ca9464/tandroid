package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.IMapsProvider;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.BaseLocationAdapter;
import org.telegram.ui.Adapters.LocationActivityAdapter;
import org.telegram.ui.Adapters.LocationActivitySearchAdapter;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.LocationDirectionCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.LocationPoweredCell;
import org.telegram.ui.Cells.SendLocationCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.SharingLiveLocationCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MapPlaceholderDrawable;
import org.telegram.ui.Components.ProximitySheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.Stories.recorder.HintView2;

/* loaded from: classes4.dex */
public class LocationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private LocationActivityAdapter adapter;
    private AnimatorSet animatorSet;
    private int askWithRadius;
    private boolean canUndo;
    private TLRPC.TL_channelLocation chatLocation;
    private boolean currentMapStyleDark;
    private LocationActivityDelegate delegate;
    private long dialogId;
    private ImageView emptyImageView;
    private TextView emptySubtitleTextView;
    private TextView emptyTitleTextView;
    private LinearLayout emptyView;
    private boolean firstWas;
    private IMapsProvider.ICameraUpdate forceUpdate;
    public boolean fromStories;
    private boolean hasScreenshot;
    private HintView2 hintView;
    private TLRPC.TL_channelLocation initialLocation;
    private boolean initialMaxZoom;
    private IMapsProvider.IMarker lastPressedMarker;
    private FrameLayout lastPressedMarkerView;
    private VenueLocation lastPressedVenue;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ImageView locationButton;
    private int locationType;
    private IMapsProvider.IMap map;
    private ActionBarMenuItem mapTypeButton;
    private IMapsProvider.IMapView mapView;
    private FrameLayout mapViewClip;
    private boolean mapsInitialized;
    private Runnable markAsReadRunnable;
    private View markerImageView;
    private int markerTop;
    private MessageObject messageObject;
    private IMapsProvider.ICameraUpdate moveToBounds;
    private Location myLocation;
    private boolean onResumeCalled;
    private ActionBarMenuItem otherItem;
    private MapOverlayView overlayView;
    private ChatActivity parentFragment;
    private ActionBarPopupWindow popupWindow;
    private double previousRadius;
    private boolean proximityAnimationInProgress;
    private ImageView proximityButton;
    private IMapsProvider.ICircle proximityCircle;
    private ProximitySheet proximitySheet;
    private boolean scrolling;
    private LocationActivitySearchAdapter searchAdapter;
    private SearchButton searchAreaButton;
    private boolean searchInProgress;
    private ActionBarMenuItem searchItem;
    private RecyclerListView searchListView;
    private TL_stories.MediaArea searchStoriesArea;
    private boolean searchWas;
    private boolean searchedForCustomLocations;
    private boolean searching;
    private View shadow;
    private Drawable shadowDrawable;
    private GraySectionCell sharedMediaHeader;
    private SharedMediaLayout sharedMediaLayout;
    private Runnable updateRunnable;
    private Location userLocation;
    private boolean userLocationMoved;
    private float yOffset;
    private UndoView[] undoView = new UndoView[2];
    private boolean checkGpsEnabled = true;
    private boolean locationDenied = false;
    private boolean isFirstLocation = true;
    private boolean firstFocus = true;
    private ArrayList markers = new ArrayList();
    private LongSparseArray markersMap = new LongSparseArray();
    private long selectedMarkerId = -1;
    private ArrayList placeMarkers = new ArrayList();
    private boolean checkPermission = true;
    private boolean checkBackgroundPermission = true;
    private int overScrollHeight = (AndroidUtilities.displaySize.x - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(66.0f);
    private boolean isSharingAllowed = true;
    private Bitmap[] bitmapCache = new Bitmap[7];

    public static class LiveLocation {
        public TLRPC.Chat chat;
        public IMapsProvider.IMarker directionMarker;
        public boolean hasRotation;
        public long id;
        public IMapsProvider.IMarker marker;
        public TLRPC.Message object;
        public TLRPC.User user;
    }

    public interface LocationActivityDelegate {
        void didSelectLocation(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2);
    }

    public class MapOverlayView extends FrameLayout {
        private HashMap views;

        public MapOverlayView(Context context) {
            super(context);
            this.views = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addInfoView$0(VenueLocation venueLocation, boolean z, int i) {
            LocationActivity.this.delegate.didSelectLocation(venueLocation.venue, LocationActivity.this.locationType, z, i);
            LocationActivity.this.lambda$onBackPressed$323();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addInfoView$1(final VenueLocation venueLocation, View view) {
            if (LocationActivity.this.parentFragment != null && LocationActivity.this.parentFragment.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(LocationActivity.this.getParentActivity(), LocationActivity.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LocationActivity$MapOverlayView$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i) {
                        LocationActivity.MapOverlayView.this.lambda$addInfoView$0(venueLocation, z, i);
                    }
                });
            } else {
                LocationActivity.this.delegate.didSelectLocation(venueLocation.venue, LocationActivity.this.locationType, true, 0);
                LocationActivity.this.lambda$onBackPressed$323();
            }
        }

        public void addInfoView(IMapsProvider.IMarker iMarker) {
            final VenueLocation venueLocation = (VenueLocation) iMarker.getTag();
            if (venueLocation == null || LocationActivity.this.lastPressedVenue == venueLocation) {
                return;
            }
            LocationActivity.this.showSearchPlacesButton(false);
            if (LocationActivity.this.lastPressedMarker != null) {
                removeInfoView(LocationActivity.this.lastPressedMarker);
                LocationActivity.this.lastPressedMarker = null;
            }
            LocationActivity.this.lastPressedVenue = venueLocation;
            LocationActivity.this.lastPressedMarker = iMarker;
            Context context = getContext();
            FrameLayout frameLayout = new FrameLayout(context);
            addView(frameLayout, LayoutHelper.createFrame(-2, 114.0f));
            LocationActivity.this.lastPressedMarkerView = new FrameLayout(context);
            LocationActivity.this.lastPressedMarkerView.setBackgroundResource(R.drawable.venue_tooltip);
            LocationActivity.this.lastPressedMarkerView.getBackground().setColorFilter(new PorterDuffColorFilter(LocationActivity.this.getThemedColor(Theme.key_dialogBackground), PorterDuff.Mode.MULTIPLY));
            frameLayout.addView(LocationActivity.this.lastPressedMarkerView, LayoutHelper.createFrame(-2, 71.0f));
            LocationActivity.this.lastPressedMarkerView.setAlpha(0.0f);
            LocationActivity.this.lastPressedMarkerView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$MapOverlayView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LocationActivity.MapOverlayView.this.lambda$addInfoView$1(venueLocation, view);
                }
            });
            TextView textView = new TextView(context);
            textView.setTextSize(1, 16.0f);
            textView.setMaxLines(1);
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView.setEllipsize(truncateAt);
            textView.setSingleLine(true);
            textView.setTextColor(LocationActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            textView.setTypeface(AndroidUtilities.bold());
            textView.setGravity(LocaleController.isRTL ? 5 : 3);
            LocationActivity.this.lastPressedMarkerView.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 18.0f, 10.0f, 18.0f, 0.0f));
            TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 14.0f);
            textView2.setMaxLines(1);
            textView2.setEllipsize(truncateAt);
            textView2.setSingleLine(true);
            textView2.setTextColor(LocationActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText3));
            textView2.setGravity(LocaleController.isRTL ? 5 : 3);
            LocationActivity.this.lastPressedMarkerView.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 18.0f, 32.0f, 18.0f, 0.0f));
            textView.setText(venueLocation.venue.title);
            textView2.setText(LocaleController.getString(R.string.TapToSendLocation));
            final FrameLayout frameLayout2 = new FrameLayout(context);
            frameLayout2.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(36.0f), LocationCell.getColorForIndex(venueLocation.num)));
            frameLayout.addView(frameLayout2, LayoutHelper.createFrame(36, 36.0f, 81, 0.0f, 0.0f, 0.0f, 4.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setImage("https://ss3.4sqi.net/img/categories_v2/" + venueLocation.venue.venue_type + "_64.png", null, null);
            frameLayout2.addView(backupImageView, LayoutHelper.createFrame(30, 30, 17));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LocationActivity.MapOverlayView.1
                private final float[] animatorValues = {0.0f, 1.0f};
                private boolean startedInner;

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float lerp = AndroidUtilities.lerp(this.animatorValues, valueAnimator.getAnimatedFraction());
                    if (lerp >= 0.7f && !this.startedInner && LocationActivity.this.lastPressedMarkerView != null) {
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(ObjectAnimator.ofFloat(LocationActivity.this.lastPressedMarkerView, (Property<FrameLayout, Float>) View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(LocationActivity.this.lastPressedMarkerView, (Property<FrameLayout, Float>) View.SCALE_Y, 0.0f, 1.0f), ObjectAnimator.ofFloat(LocationActivity.this.lastPressedMarkerView, (Property<FrameLayout, Float>) View.ALPHA, 0.0f, 1.0f));
                        animatorSet.setInterpolator(new OvershootInterpolator(1.02f));
                        animatorSet.setDuration(250L);
                        animatorSet.start();
                        this.startedInner = true;
                    }
                    float interpolation = lerp <= 0.5f ? CubicBezierInterpolator.EASE_OUT.getInterpolation(lerp / 0.5f) * 1.1f : lerp <= 0.75f ? 1.1f - (CubicBezierInterpolator.EASE_OUT.getInterpolation((lerp - 0.5f) / 0.25f) * 0.2f) : (CubicBezierInterpolator.EASE_OUT.getInterpolation((lerp - 0.75f) / 0.25f) * 0.1f) + 0.9f;
                    frameLayout2.setScaleX(interpolation);
                    frameLayout2.setScaleY(interpolation);
                }
            });
            ofFloat.setDuration(360L);
            ofFloat.start();
            this.views.put(iMarker, frameLayout);
            LocationActivity.this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(iMarker.getPosition()), NotificationCenter.chatlistFolderUpdate, null);
        }

        public void removeInfoView(IMapsProvider.IMarker iMarker) {
            View view = (View) this.views.get(iMarker);
            if (view != null) {
                removeView(view);
                this.views.remove(iMarker);
            }
        }

        public void updatePositions() {
            if (LocationActivity.this.map == null) {
                return;
            }
            IMapsProvider.IProjection projection = LocationActivity.this.map.getProjection();
            for (Map.Entry entry : this.views.entrySet()) {
                IMapsProvider.IMarker iMarker = (IMapsProvider.IMarker) entry.getKey();
                View view = (View) entry.getValue();
                Point screenLocation = projection.toScreenLocation(iMarker.getPosition());
                view.setTranslationX(screenLocation.x - (view.getMeasuredWidth() / 2));
                view.setTranslationY((screenLocation.y - view.getMeasuredHeight()) + AndroidUtilities.dp(22.0f));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class NestedFrameLayout extends SizeNotifierFrameLayout implements NestedScrollingParent3 {
        private boolean first;
        private NestedScrollingParentHelper nestedScrollingParentHelper;

        public NestedFrameLayout(Context context) {
            super(context);
            this.first = true;
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNestedScroll$0() {
            try {
                RecyclerListView currentListView = LocationActivity.this.sharedMediaLayout.getCurrentListView();
                if (currentListView == null || currentListView.getAdapter() == null) {
                    return;
                }
                currentListView.getAdapter().notifyDataSetChanged();
            } catch (Throwable unused) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == ((BaseFragment) LocationActivity.this).actionBar && ((BaseFragment) LocationActivity.this).parentLayout != null) {
                ((BaseFragment) LocationActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) LocationActivity.this).actionBar.getMeasuredHeight());
            }
            return drawChild;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected void drawList(Canvas canvas, boolean z, ArrayList arrayList) {
            super.drawList(canvas, z, arrayList);
            if (LocationActivity.this.sharedMediaLayout != null) {
                canvas.save();
                canvas.translate(0.0f, LocationActivity.this.listView.getY());
                LocationActivity.this.sharedMediaLayout.drawListForBlur(canvas, arrayList);
                canvas.restore();
            }
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (!z) {
                LocationActivity.this.updateClipView(true);
            } else {
                LocationActivity.this.fixLayoutInternal(this.first);
                this.first = false;
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedPreFling(View view, float f, float f2) {
            return super.onNestedPreFling(view, f, f2);
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
            int i4;
            RecyclerListView currentListView;
            if (view == LocationActivity.this.listView && LocationActivity.this.sharedMediaLayout != null && LocationActivity.this.sharedMediaLayout.isAttachedToWindow()) {
                boolean isSearchFieldVisible = ((BaseFragment) LocationActivity.this).actionBar.isSearchFieldVisible();
                int top = LocationActivity.this.sharedMediaLayout.getTop();
                boolean z = false;
                if (i2 >= 0) {
                    if (isSearchFieldVisible) {
                        RecyclerListView currentListView2 = LocationActivity.this.sharedMediaLayout.getCurrentListView();
                        iArr[1] = i2;
                        if (top > 0) {
                            iArr[1] = i2 - i2;
                        }
                        if (currentListView2 == null || (i4 = iArr[1]) <= 0) {
                            return;
                        }
                        currentListView2.scrollBy(0, i4);
                        return;
                    }
                    return;
                }
                if (top <= 0 && (currentListView = LocationActivity.this.sharedMediaLayout.getCurrentListView()) != null) {
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) currentListView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition != -1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = currentListView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        int top2 = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : -1;
                        int paddingTop = currentListView.getPaddingTop();
                        if (top2 != paddingTop || findFirstVisibleItemPosition != 0) {
                            iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, top2 - paddingTop);
                            currentListView.scrollBy(0, i2);
                            z = true;
                        }
                    }
                }
                if (isSearchFieldVisible) {
                    if (z || top >= 0) {
                        iArr[1] = i2;
                    } else {
                        iArr[1] = i2 - Math.max(top, i2);
                    }
                }
            }
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        }

        @Override // androidx.core.view.NestedScrollingParent3
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
            try {
                if (view == LocationActivity.this.listView && LocationActivity.this.sharedMediaLayout != null && LocationActivity.this.sharedMediaLayout.isAttachedToWindow()) {
                    RecyclerListView currentListView = LocationActivity.this.sharedMediaLayout.getCurrentListView();
                    if (LocationActivity.this.sharedMediaLayout.getTop() == 0) {
                        iArr[1] = i4;
                        currentListView.scrollBy(0, i4);
                    }
                }
            } catch (Throwable th) {
                FileLog.e(th);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$NestedFrameLayout$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationActivity.NestedFrameLayout.this.lambda$onNestedScroll$0();
                    }
                });
            }
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
            return LocationActivity.this.sharedMediaLayout != null && i == 2;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
        }

        @Override // androidx.core.view.NestedScrollingParent2
        public void onStopNestedScroll(View view, int i) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
        }
    }

    private static class SearchButton extends TextView {
        private float additionanTranslationY;
        private float currentTranslationY;

        public SearchButton(Context context) {
            super(context);
        }

        private void updateTranslationY() {
            setTranslationY(this.currentTranslationY + this.additionanTranslationY);
        }

        @Override // android.view.View
        public float getTranslationX() {
            return this.additionanTranslationY;
        }

        public void setTranslation(float f) {
            this.currentTranslationY = f;
            updateTranslationY();
        }

        @Override // android.view.View
        public void setTranslationX(float f) {
            this.additionanTranslationY = f;
            updateTranslationY();
        }
    }

    public static class VenueLocation {
        public IMapsProvider.IMarker marker;
        public int num;
        public TLRPC.TL_messageMediaVenue venue;
    }

    public LocationActivity(int i) {
        this.locationType = i;
        AndroidUtilities.fixGoogleMapsBug();
    }

    static /* synthetic */ float access$3116(LocationActivity locationActivity, float f) {
        float f2 = locationActivity.yOffset + f;
        locationActivity.yOffset = f2;
        return f2;
    }

    private LiveLocation addUserMarker(TLRPC.Message message) {
        long dialogId;
        Location location;
        TLRPC.GeoPoint geoPoint = message.media.geo;
        IMapsProvider.LatLng latLng = new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long);
        LiveLocation liveLocation = (LiveLocation) this.markersMap.get(MessageObject.getFromChatId(message));
        if (liveLocation == null) {
            liveLocation = new LiveLocation();
            liveLocation.object = message;
            if (message.from_id instanceof TLRPC.TL_peerUser) {
                liveLocation.user = getMessagesController().getUser(Long.valueOf(liveLocation.object.from_id.user_id));
                dialogId = liveLocation.object.from_id.user_id;
            } else {
                dialogId = MessageObject.getDialogId(message);
                if (DialogObject.isUserDialog(dialogId)) {
                    liveLocation.user = getMessagesController().getUser(Long.valueOf(dialogId));
                } else {
                    liveLocation.chat = getMessagesController().getChat(Long.valueOf(-dialogId));
                }
            }
            liveLocation.id = dialogId;
            try {
                IMapsProvider.IMarkerOptions position = ApplicationLoader.getMapsProvider().onCreateMarkerOptions().position(latLng);
                Bitmap createUserBitmap = createUserBitmap(liveLocation);
                if (createUserBitmap != null) {
                    position.icon(createUserBitmap);
                    position.anchor(0.5f, 0.907f);
                    liveLocation.marker = this.map.addMarker(position);
                    if (!UserObject.isUserSelf(liveLocation.user)) {
                        IMapsProvider.IMarkerOptions flat = ApplicationLoader.getMapsProvider().onCreateMarkerOptions().position(latLng).flat(true);
                        flat.anchor(0.5f, 0.5f);
                        IMapsProvider.IMarker addMarker = this.map.addMarker(flat);
                        liveLocation.directionMarker = addMarker;
                        int i = message.media.heading;
                        if (i != 0) {
                            addMarker.setRotation(i);
                            liveLocation.directionMarker.setIcon(R.drawable.map_pin_cone2);
                            liveLocation.hasRotation = true;
                        } else {
                            addMarker.setRotation(0);
                            liveLocation.directionMarker.setIcon(R.drawable.map_pin_circle);
                            liveLocation.hasRotation = false;
                        }
                    }
                    this.markers.add(liveLocation);
                    this.markersMap.put(liveLocation.id, liveLocation);
                    LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId);
                    if (liveLocation.id == getUserConfig().getClientUserId() && sharingLocationInfo != null && liveLocation.object.id == sharingLocationInfo.mid && (location = this.myLocation) != null) {
                        liveLocation.marker.setPosition(new IMapsProvider.LatLng(location.getLatitude(), this.myLocation.getLongitude()));
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            liveLocation.object = message;
            liveLocation.marker.setPosition(latLng);
            if (this.selectedMarkerId == liveLocation.id) {
                this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(liveLocation.marker.getPosition()));
            }
        }
        ProximitySheet proximitySheet = this.proximitySheet;
        if (proximitySheet != null) {
            proximitySheet.updateText(true, true);
        }
        return liveLocation;
    }

    private LiveLocation addUserMarker(TLRPC.TL_channelLocation tL_channelLocation) {
        TLRPC.GeoPoint geoPoint = tL_channelLocation.geo_point;
        IMapsProvider.LatLng latLng = new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long);
        LiveLocation liveLocation = new LiveLocation();
        if (DialogObject.isUserDialog(this.dialogId)) {
            liveLocation.user = getMessagesController().getUser(Long.valueOf(this.dialogId));
        } else {
            liveLocation.chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        }
        liveLocation.id = this.dialogId;
        try {
            IMapsProvider.IMarkerOptions position = ApplicationLoader.getMapsProvider().onCreateMarkerOptions().position(latLng);
            Bitmap createUserBitmap = createUserBitmap(liveLocation);
            if (createUserBitmap != null) {
                position.icon(createUserBitmap);
                position.anchor(0.5f, 0.907f);
                liveLocation.marker = this.map.addMarker(position);
                if (!UserObject.isUserSelf(liveLocation.user)) {
                    IMapsProvider.IMarkerOptions flat = ApplicationLoader.getMapsProvider().onCreateMarkerOptions().position(latLng).flat(true);
                    flat.icon(R.drawable.map_pin_circle);
                    flat.anchor(0.5f, 0.5f);
                    liveLocation.directionMarker = this.map.addMarker(flat);
                }
                this.markers.add(liveLocation);
                this.markersMap.put(liveLocation.id, liveLocation);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return liveLocation;
    }

    private boolean checkGpsEnabled() {
        if (disablePermissionCheck()) {
            return false;
        }
        if (!getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
            return true;
        }
        try {
            if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTopAnimation(R.raw.permission_request_location, 72, false, getThemedColor(Theme.key_dialogTopBackground));
                builder.setMessage(LocaleController.getString(R.string.GpsDisabledAlertText));
                builder.setPositiveButton(LocaleController.getString(R.string.ConnectingToProxyEnable), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda22
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i) {
                        LocationActivity.this.lambda$checkGpsEnabled$38(alertDialog, i);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                showDialog(builder.create());
                return false;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return true;
    }

    private void createCircle(int i) {
        int i2;
        if (this.map == null) {
            return;
        }
        List<IMapsProvider.PatternItem> asList = Arrays.asList(new IMapsProvider.PatternItem.Gap(20), new IMapsProvider.PatternItem.Dash(20));
        IMapsProvider.ICircleOptions onCreateCircleOptions = ApplicationLoader.getMapsProvider().onCreateCircleOptions();
        onCreateCircleOptions.center(new IMapsProvider.LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
        onCreateCircleOptions.radius(i);
        if (isActiveThemeDark()) {
            onCreateCircleOptions.strokeColor(-1771658281);
            i2 = 476488663;
        } else {
            onCreateCircleOptions.strokeColor(-1774024971);
            i2 = 474121973;
        }
        onCreateCircleOptions.fillColor(i2);
        onCreateCircleOptions.strokePattern(asList);
        onCreateCircleOptions.strokeWidth(2);
        this.proximityCircle = this.map.addCircle(onCreateCircleOptions);
    }

    private Bitmap createPlaceBitmap(int i) {
        Bitmap bitmap = this.bitmapCache[i % 7];
        if (bitmap != null) {
            return bitmap;
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

    private Bitmap createUserBitmap(LiveLocation liveLocation) {
        TLRPC.FileLocation fileLocation;
        TLRPC.ChatPhoto chatPhoto;
        TLRPC.UserProfilePhoto userProfilePhoto;
        Bitmap bitmap = null;
        try {
            TLRPC.User user = liveLocation.user;
            if (user == null || (userProfilePhoto = user.photo) == null) {
                TLRPC.Chat chat = liveLocation.chat;
                fileLocation = (chat == null || (chatPhoto = chat.photo) == null) ? null : chatPhoto.photo_small;
            } else {
                fileLocation = userProfilePhoto.photo_small;
            }
            Bitmap createBitmap = Bitmap.createBitmap(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(85.0f), Bitmap.Config.ARGB_8888);
            try {
                createBitmap.eraseColor(0);
                Canvas canvas = new Canvas(createBitmap);
                Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.map_pin_photo);
                drawable.setBounds(0, 0, AndroidUtilities.dp(62.0f), AndroidUtilities.dp(85.0f));
                drawable.draw(canvas);
                Paint paint = new Paint(1);
                RectF rectF = new RectF();
                canvas.save();
                canvas.save();
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                TLRPC.User user2 = liveLocation.user;
                if (user2 != null) {
                    avatarDrawable.setInfo(this.currentAccount, user2);
                } else {
                    TLRPC.Chat chat2 = liveLocation.chat;
                    if (chat2 != null) {
                        avatarDrawable.setInfo(this.currentAccount, chat2);
                    }
                }
                canvas.translate(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
                avatarDrawable.setBounds(0, 0, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(50.0f));
                avatarDrawable.draw(canvas);
                canvas.restore();
                if (fileLocation != null) {
                    int i = this.currentAccount;
                    TLObject tLObject = liveLocation.user;
                    if (tLObject == null) {
                        tLObject = liveLocation.chat;
                    }
                    Bitmap decodeFile = BitmapFactory.decodeFile(ImageReceiver.getAvatarLocalFile(i, tLObject).toString());
                    if (decodeFile != null) {
                        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                        BitmapShader bitmapShader = new BitmapShader(decodeFile, tileMode, tileMode);
                        Matrix matrix = new Matrix();
                        float dp = AndroidUtilities.dp(50.0f) / decodeFile.getWidth();
                        matrix.postTranslate(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
                        matrix.postScale(dp, dp);
                        paint.setShader(bitmapShader);
                        bitmapShader.setLocalMatrix(matrix);
                        rectF.set(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        canvas.drawRoundRect(rectF, AndroidUtilities.dp(25.0f), AndroidUtilities.dp(25.0f), paint);
                    }
                }
                canvas.restore();
                try {
                    canvas.setBitmap(null);
                    return createBitmap;
                } catch (Exception unused) {
                    return createBitmap;
                }
            } catch (Throwable th) {
                th = th;
                bitmap = createBitmap;
                FileLog.e(th);
                return bitmap;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void fetchRecentLocations(ArrayList arrayList) {
        IMapsProvider.ILatLngBoundsBuilder onCreateLatLngBoundsBuilder = this.firstFocus ? ApplicationLoader.getMapsProvider().onCreateLatLngBoundsBuilder() : null;
        int currentTime = getConnectionsManager().getCurrentTime();
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.Message message = (TLRPC.Message) arrayList.get(i);
            int i2 = message.date;
            TLRPC.MessageMedia messageMedia = message.media;
            int i3 = messageMedia.period;
            if (i2 + i3 > currentTime || i3 == Integer.MAX_VALUE) {
                if (onCreateLatLngBoundsBuilder != null) {
                    TLRPC.GeoPoint geoPoint = messageMedia.geo;
                    onCreateLatLngBoundsBuilder.include(new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long));
                }
                addUserMarker(message);
                if (this.proximityButton.getVisibility() != 8 && MessageObject.getFromChatId(message) != getUserConfig().getClientUserId()) {
                    this.proximityButton.setVisibility(0);
                    this.proximityAnimationInProgress = true;
                    this.proximityButton.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(180L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LocationActivity.16
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            LocationActivity.this.proximityAnimationInProgress = false;
                            LocationActivity.this.maybeShowProximityHint();
                        }
                    }).start();
                }
            }
        }
        if (onCreateLatLngBoundsBuilder != null) {
            if (this.firstFocus) {
                this.listView.smoothScrollBy(0, AndroidUtilities.dp(99.0f));
            }
            this.firstFocus = false;
            this.adapter.setLiveLocations(this.markers);
            if (this.messageObject.isLiveLocation()) {
                try {
                    IMapsProvider.LatLng center = onCreateLatLngBoundsBuilder.build().getCenter();
                    IMapsProvider.LatLng move = move(center, 100.0d, 100.0d);
                    onCreateLatLngBoundsBuilder.include(move(center, -100.0d, -100.0d));
                    onCreateLatLngBoundsBuilder.include(move);
                    IMapsProvider.ILatLngBounds build = onCreateLatLngBoundsBuilder.build();
                    if (arrayList.size() > 1) {
                        try {
                            IMapsProvider.ICameraUpdate newCameraUpdateLatLngBounds = ApplicationLoader.getMapsProvider().newCameraUpdateLatLngBounds(build, AndroidUtilities.dp(113.0f));
                            this.moveToBounds = newCameraUpdateLatLngBounds;
                            this.map.moveCamera(newCameraUpdateLatLngBounds);
                            this.moveToBounds = null;
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixLayoutInternal(boolean z) {
        FrameLayout.LayoutParams layoutParams;
        if (this.listView != null) {
            int currentActionBarHeight = (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight();
            int measuredHeight = this.fragmentView.getMeasuredHeight();
            if (measuredHeight == 0) {
                return;
            }
            int i = this.locationType;
            this.overScrollHeight = (measuredHeight - AndroidUtilities.dp((i != 6 && i == 2) ? 73.0f : 66.0f)) - currentActionBarHeight;
            SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
            if (sharedMediaLayout != null && sharedMediaLayout.getStoriesCount(8) > 0) {
                this.overScrollHeight -= AndroidUtilities.dp(200.0f);
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
            layoutParams2.topMargin = currentActionBarHeight;
            this.listView.setLayoutParams(layoutParams2);
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mapViewClip.getLayoutParams();
            layoutParams3.topMargin = currentActionBarHeight;
            layoutParams3.height = this.overScrollHeight;
            this.mapViewClip.setLayoutParams(layoutParams3);
            RecyclerListView recyclerListView = this.searchListView;
            if (recyclerListView != null) {
                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) recyclerListView.getLayoutParams();
                layoutParams4.topMargin = currentActionBarHeight;
                this.searchListView.setLayoutParams(layoutParams4);
            }
            this.adapter.setOverScrollHeight(this.overScrollHeight);
            FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) this.mapView.getView().getLayoutParams();
            if (layoutParams5 != null) {
                layoutParams5.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
                IMapsProvider.IMap iMap = this.map;
                if (iMap != null) {
                    iMap.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
                }
                this.mapView.getView().setLayoutParams(layoutParams5);
            }
            MapOverlayView mapOverlayView = this.overlayView;
            if (mapOverlayView != null && (layoutParams = (FrameLayout.LayoutParams) mapOverlayView.getLayoutParams()) != null) {
                layoutParams.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
                this.overlayView.setLayoutParams(layoutParams);
            }
            this.adapter.notifyDataSetChanged();
            if (!z) {
                updateClipView(false);
                return;
            }
            int i2 = this.locationType;
            final int i3 = i2 == 3 ? 73 : (i2 == 1 || i2 == 2) ? 66 : 0;
            this.layoutManager.scrollToPositionWithOffset(0, -AndroidUtilities.dp(i3));
            updateClipView(false);
            this.listView.post(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$fixLayoutInternal$40(i3);
                }
            });
        }
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

    private int getMapThemeResId() {
        if (AndroidUtilities.computePerceivedBrightness(getThemedColor(Theme.key_windowBackgroundWhite)) < 0.721f) {
            return R.raw.mapstyle_night;
        }
        return 0;
    }

    private long getMessageId(TLRPC.Message message) {
        return message.from_id != null ? MessageObject.getFromChatId(message) : MessageObject.getDialogId(message);
    }

    private boolean getRecentLocations() {
        ArrayList arrayList = (ArrayList) getLocationController().locationsCache.get(this.messageObject.getDialogId());
        if (arrayList == null || !arrayList.isEmpty()) {
            arrayList = null;
        } else {
            fetchRecentLocations(arrayList);
        }
        if (DialogObject.isChatDialog(this.dialogId)) {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                return false;
            }
        }
        TLRPC.TL_messages_getRecentLocations tL_messages_getRecentLocations = new TLRPC.TL_messages_getRecentLocations();
        final long dialogId = this.messageObject.getDialogId();
        tL_messages_getRecentLocations.peer = getMessagesController().getInputPeer(dialogId);
        tL_messages_getRecentLocations.limit = 100;
        getConnectionsManager().sendRequest(tL_messages_getRecentLocations, new RequestDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda47
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LocationActivity.this.lambda$getRecentLocations$43(dialogId, tLObject, tL_error);
            }
        });
        return arrayList != null;
    }

    private UndoView getUndoView() {
        if (this.undoView[0].getVisibility() == 0) {
            UndoView[] undoViewArr = this.undoView;
            UndoView undoView = undoViewArr[0];
            undoViewArr[0] = undoViewArr[1];
            undoViewArr[1] = undoView;
            undoView.hide(true, 2);
            this.mapViewClip.removeView(this.undoView[0]);
            this.mapViewClip.addView(this.undoView[0]);
        }
        return this.undoView[0];
    }

    private boolean isActiveThemeDark() {
        return (getResourceProvider() == null && Theme.getActiveTheme().isDark()) || AndroidUtilities.computePerceivedBrightness(getThemedColor(Theme.key_windowBackgroundWhite)) < 0.721f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkGpsEnabled$38(AlertDialog alertDialog, int i) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        try {
            TLRPC.GeoPoint geoPoint = this.messageObject.messageOwner.media.geo;
            double d = geoPoint.lat;
            double d2 = geoPoint._long;
            getParentActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:" + d + "," + d2 + "?q=" + d + "," + d2)));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        showSearchPlacesButton(false);
        this.adapter.searchPlacesWithQuery(null, this.userLocation, true, true);
        this.searchedForCustomLocations = true;
        showResults();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(LiveLocation liveLocation, View view) {
        openDirections(liveLocation);
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$11(Context context, View view, int i) {
        if (this.locationType == 2) {
            Object item = this.adapter.getItem(i);
            if (item instanceof LiveLocation) {
                final LiveLocation liveLocation = (LiveLocation) item;
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context);
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) getParentActivity(), true, true, getResourceProvider());
                actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(200.0f));
                actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.GetDirections), R.drawable.filled_directions);
                actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda31
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        LocationActivity.this.lambda$createView$10(liveLocation, view2);
                    }
                });
                actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
                int i2 = -2;
                ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, i2, i2) { // from class: org.telegram.ui.LocationActivity.12
                    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        LocationActivity.this.popupWindow = null;
                    }
                };
                this.popupWindow = actionBarPopupWindow;
                actionBarPopupWindow.setOutsideTouchable(true);
                this.popupWindow.setClippingEnabled(true);
                this.popupWindow.setInputMethodMode(2);
                this.popupWindow.setSoftInputMode(0);
                int[] iArr = new int[2];
                view.getLocationInWindow(iArr);
                this.popupWindow.showAtLocation(view, 48, 0, iArr[1] - AndroidUtilities.dp(52.0f));
                this.popupWindow.dimBehind();
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(AlertDialog[] alertDialogArr, TLRPC.TL_messageMediaVenue tL_messageMediaVenue) {
        try {
            alertDialogArr[0].dismiss();
        } catch (Throwable unused) {
        }
        alertDialogArr[0] = null;
        this.delegate.didSelectLocation(tL_messageMediaVenue, 4, true, 0);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(final AlertDialog[] alertDialogArr, final TLRPC.TL_messageMediaVenue tL_messageMediaVenue, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$12(alertDialogArr, tL_messageMediaVenue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(int i, DialogInterface dialogInterface) {
        getConnectionsManager().cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(TLRPC.TL_messageMediaGeo tL_messageMediaGeo, boolean z, int i) {
        this.delegate.didSelectLocation(tL_messageMediaGeo, this.locationType, z, i);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(Object obj, boolean z, int i) {
        this.delegate.didSelectLocation((TLRPC.TL_messageMediaVenue) obj, this.locationType, z, i);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$createView$17(View view, int i) {
        Activity parentActivity;
        long dialogId;
        AlertsCreator.ScheduleDatePickerDelegate scheduleDatePickerDelegate;
        LocationActivityDelegate locationActivityDelegate;
        TLRPC.TL_messageMediaVenue tL_messageMediaVenue;
        MessageObject messageObject;
        IMapsProvider.IMap iMap;
        IMapsProvider mapsProvider;
        IMapsProvider.LatLng latLng;
        final TLRPC.TL_messageMediaVenue tL_messageMediaVenue2;
        this.selectedMarkerId = -1L;
        int i2 = this.locationType;
        if (i2 != 4) {
            if (i2 == 5) {
                iMap = this.map;
                if (iMap == null) {
                    return;
                }
                mapsProvider = ApplicationLoader.getMapsProvider();
                TLRPC.GeoPoint geoPoint = this.chatLocation.geo_point;
                latLng = new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long);
            } else if (i == 1 && (messageObject = this.messageObject) != null && (!messageObject.isLiveLocation() || this.locationType == 6)) {
                iMap = this.map;
                if (iMap == null) {
                    return;
                }
                mapsProvider = ApplicationLoader.getMapsProvider();
                TLRPC.GeoPoint geoPoint2 = this.messageObject.messageOwner.media.geo;
                latLng = new IMapsProvider.LatLng(geoPoint2.lat, geoPoint2._long);
            } else if (i != 1 || this.locationType == 2) {
                if (this.locationType != 2 || !getLocationController().isSharingLocation(this.dialogId) || this.adapter.getItemViewType(i) != 7) {
                    if (this.locationType == 2 && getLocationController().isSharingLocation(this.dialogId) && this.adapter.getItemViewType(i) == 6) {
                        openShareLiveLocation(getLocationController().getSharingLocationInfo(this.dialogId).period != Integer.MAX_VALUE, 0);
                        return;
                    }
                    if ((i != 2 || this.locationType != 1) && ((i != 1 || this.locationType != 2) && (i != 3 || this.locationType != 3))) {
                        final Object item = this.adapter.getItem(i);
                        if (!(item instanceof TLRPC.TL_messageMediaVenue)) {
                            if (item instanceof LiveLocation) {
                                LiveLocation liveLocation = (LiveLocation) item;
                                this.selectedMarkerId = liveLocation.id;
                                this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(liveLocation.marker.getPosition(), this.map.getMaxZoomLevel() - 4.0f));
                                return;
                            }
                            return;
                        }
                        ChatActivity chatActivity = this.parentFragment;
                        if (chatActivity != null && chatActivity.isInScheduleMode()) {
                            parentActivity = getParentActivity();
                            dialogId = this.parentFragment.getDialogId();
                            scheduleDatePickerDelegate = new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda27
                                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                                public final void didSelectDate(boolean z, int i3) {
                                    LocationActivity.this.lambda$createView$16(item, z, i3);
                                }
                            };
                            AlertsCreator.createScheduleDatePickerDialog(parentActivity, dialogId, scheduleDatePickerDelegate);
                            return;
                        }
                        tL_messageMediaVenue = (TLRPC.TL_messageMediaVenue) item;
                        locationActivityDelegate = this.delegate;
                        locationActivityDelegate.didSelectLocation(tL_messageMediaVenue, this.locationType, true, 0);
                    } else if (!getLocationController().isSharingLocation(this.dialogId)) {
                        openShareLiveLocation(false, 0);
                        return;
                    }
                }
                getLocationController().removeSharingLocation(this.dialogId);
                this.adapter.notifyDataSetChanged();
            } else {
                if (this.delegate == null || this.userLocation == null) {
                    return;
                }
                FrameLayout frameLayout = this.lastPressedMarkerView;
                if (frameLayout != null) {
                    frameLayout.callOnClick();
                    return;
                }
                final TLRPC.TL_messageMediaGeo tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
                TLRPC.TL_geoPoint tL_geoPoint = new TLRPC.TL_geoPoint();
                tL_messageMediaGeo.geo = tL_geoPoint;
                tL_geoPoint.lat = AndroidUtilities.fixLocationCoord(this.userLocation.getLatitude());
                tL_messageMediaGeo.geo._long = AndroidUtilities.fixLocationCoord(this.userLocation.getLongitude());
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 != null && chatActivity2.isInScheduleMode()) {
                    parentActivity = getParentActivity();
                    dialogId = this.parentFragment.getDialogId();
                    scheduleDatePickerDelegate = new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda26
                        @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                        public final void didSelectDate(boolean z, int i3) {
                            LocationActivity.this.lambda$createView$15(tL_messageMediaGeo, z, i3);
                        }
                    };
                    AlertsCreator.createScheduleDatePickerDialog(parentActivity, dialogId, scheduleDatePickerDelegate);
                    return;
                }
                tL_messageMediaVenue = tL_messageMediaGeo;
                locationActivityDelegate = this.delegate;
                locationActivityDelegate.didSelectLocation(tL_messageMediaVenue, this.locationType, true, 0);
            }
            iMap.animateCamera(mapsProvider.newCameraUpdateLatLngZoom(latLng, this.map.getMaxZoomLevel() - 4.0f));
            return;
        }
        if (i != 1 || (tL_messageMediaVenue2 = (TLRPC.TL_messageMediaVenue) this.adapter.getItem(i)) == null) {
            return;
        }
        if (this.dialogId != 0) {
            final AlertDialog[] alertDialogArr = {new AlertDialog(getParentActivity(), 3)};
            TLRPC.TL_channels_editLocation tL_channels_editLocation = new TLRPC.TL_channels_editLocation();
            tL_channels_editLocation.address = tL_messageMediaVenue2.address;
            tL_channels_editLocation.channel = getMessagesController().getInputChannel(-this.dialogId);
            TLRPC.TL_inputGeoPoint tL_inputGeoPoint = new TLRPC.TL_inputGeoPoint();
            tL_channels_editLocation.geo_point = tL_inputGeoPoint;
            TLRPC.GeoPoint geoPoint3 = tL_messageMediaVenue2.geo;
            tL_inputGeoPoint.lat = geoPoint3.lat;
            tL_inputGeoPoint._long = geoPoint3._long;
            final int sendRequest = getConnectionsManager().sendRequest(tL_channels_editLocation, new RequestDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LocationActivity.this.lambda$createView$13(alertDialogArr, tL_messageMediaVenue2, tLObject, tL_error);
                }
            });
            alertDialogArr[0].setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda25
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    LocationActivity.this.lambda$createView$14(sendRequest, dialogInterface);
                }
            });
            showDialog(alertDialogArr[0]);
            return;
        }
        this.delegate.didSelectLocation(tL_messageMediaVenue2, 4, true, 0);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$18(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
        MotionEvent motionEvent2;
        MotionEvent motionEvent3;
        if (this.yOffset != 0.0f) {
            motionEvent3 = MotionEvent.obtain(motionEvent);
            motionEvent3.offsetLocation(0.0f, (-this.yOffset) / 2.0f);
            motionEvent2 = motionEvent3;
        } else {
            motionEvent2 = motionEvent;
            motionEvent3 = null;
        }
        boolean booleanValue = ((Boolean) iCallableMethod.call(motionEvent2)).booleanValue();
        if (motionEvent3 != null) {
            motionEvent3.recycle();
        }
        return booleanValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$19(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
        Location location;
        if (this.messageObject == null && this.chatLocation == null) {
            if (motionEvent.getAction() == 0) {
                AnimatorSet animatorSet = this.animatorSet;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.animatorSet = animatorSet2;
                animatorSet2.setDuration(200L);
                this.animatorSet.playTogether(ObjectAnimator.ofFloat(this.markerImageView, (Property<View, Float>) View.TRANSLATION_Y, this.markerTop - AndroidUtilities.dp(10.0f)));
                this.animatorSet.start();
            } else if (motionEvent.getAction() == 1) {
                AnimatorSet animatorSet3 = this.animatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                }
                this.yOffset = 0.0f;
                AnimatorSet animatorSet4 = new AnimatorSet();
                this.animatorSet = animatorSet4;
                animatorSet4.setDuration(200L);
                this.animatorSet.playTogether(ObjectAnimator.ofFloat(this.markerImageView, (Property<View, Float>) View.TRANSLATION_Y, this.markerTop));
                this.animatorSet.start();
                this.adapter.fetchLocationAddress();
            }
            if (motionEvent.getAction() == 2) {
                if (!this.userLocationMoved) {
                    ImageView imageView = this.locationButton;
                    int i = Theme.key_location_actionIcon;
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i), PorterDuff.Mode.MULTIPLY));
                    this.locationButton.setTag(Integer.valueOf(i));
                    this.userLocationMoved = true;
                }
                IMapsProvider.IMap iMap = this.map;
                if (iMap != null && (location = this.userLocation) != null) {
                    location.setLatitude(iMap.getCameraPosition().target.latitude);
                    this.userLocation.setLongitude(this.map.getCameraPosition().target.longitude);
                }
                this.adapter.setCustomLocation(this.userLocation);
            }
        }
        return ((Boolean) iCallableMethod.call(motionEvent)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        this.mapTypeButton.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$20() {
        IMapsProvider.ICameraUpdate iCameraUpdate = this.moveToBounds;
        if (iCameraUpdate != null) {
            this.map.moveCamera(iCameraUpdate);
            this.moveToBounds = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$21() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$20();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$22(IMapsProvider.IMap iMap) {
        this.map = iMap;
        int mapThemeResId = getMapThemeResId();
        if (mapThemeResId != 0) {
            this.currentMapStyleDark = true;
            this.map.setMapStyle(ApplicationLoader.getMapsProvider().loadRawResourceStyle(ApplicationLoader.applicationContext, mapThemeResId));
        }
        this.map.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
        onMapInit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$23(IMapsProvider.IMapView iMapView) {
        if (this.mapView == null || getParentActivity() == null) {
            return;
        }
        try {
            iMapView.onCreate(null);
            ApplicationLoader.getMapsProvider().initializeMaps(ApplicationLoader.applicationContext);
            this.mapView.getMapAsync(new Consumer() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda41
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    LocationActivity.this.lambda$createView$22((IMapsProvider.IMap) obj);
                }
            });
            this.mapsInitialized = true;
            if (this.onResumeCalled) {
                this.mapView.onResume();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$24(final IMapsProvider.IMapView iMapView) {
        try {
            iMapView.onCreate(null);
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$23(iMapView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$25(ArrayList arrayList) {
        this.searchInProgress = false;
        updateEmptyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$26(TLRPC.TL_messageMediaVenue tL_messageMediaVenue, boolean z, int i) {
        this.delegate.didSelectLocation(tL_messageMediaVenue, this.locationType, z, i);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$27(ActionBarMenu actionBarMenu, View view, int i) {
        float maxZoomLevel;
        float f;
        final TLRPC.TL_messageMediaVenue item = this.searchAdapter.getItem(i);
        if (item == null || item.icon == null || this.locationType != 8 || this.map == null) {
            if (item == null || this.delegate == null) {
                return;
            }
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && chatActivity.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda30
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i2) {
                        LocationActivity.this.lambda$createView$26(item, z, i2);
                    }
                });
                return;
            } else {
                this.delegate.didSelectLocation(item, this.locationType, true, 0);
                lambda$onBackPressed$323();
                return;
            }
        }
        this.userLocationMoved = true;
        actionBarMenu.closeSearchField(true);
        if ("pin".equals(item.icon)) {
            maxZoomLevel = this.map.getMaxZoomLevel();
            f = 4.0f;
        } else {
            maxZoomLevel = this.map.getMaxZoomLevel();
            f = 9.0f;
        }
        float f2 = maxZoomLevel - f;
        IMapsProvider.IMap iMap = this.map;
        IMapsProvider mapsProvider = ApplicationLoader.getMapsProvider();
        TLRPC.GeoPoint geoPoint = item.geo;
        iMap.animateCamera(mapsProvider.newCameraUpdateLatLngZoom(new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long), f2));
        Location location = this.userLocation;
        if (location != null) {
            location.setLatitude(item.geo.lat);
            this.userLocation.setLongitude(item.geo._long);
        }
        this.adapter.setCustomLocation(this.userLocation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(int i) {
        int i2;
        IMapsProvider.IMap iMap = this.map;
        if (iMap == null) {
            return;
        }
        if (i == 2) {
            i2 = 0;
        } else {
            if (i != 3) {
                if (i == 4) {
                    iMap.setMapType(2);
                    return;
                }
                return;
            }
            i2 = 1;
        }
        iMap.setMapType(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        IMapsProvider.IMap iMap;
        Activity parentActivity;
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT >= 23 && (parentActivity = getParentActivity()) != null) {
            checkSelfPermission = parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
                showPermissionAlert(false);
                return;
            }
        }
        if (checkGpsEnabled() || this.locationType == 3) {
            if ((this.messageObject == null || this.locationType == 3) && this.chatLocation == null) {
                if (this.myLocation != null && this.map != null) {
                    ImageView imageView = this.locationButton;
                    int i = Theme.key_location_actionActiveIcon;
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i), PorterDuff.Mode.MULTIPLY));
                    this.locationButton.setTag(Integer.valueOf(i));
                    this.adapter.setCustomLocation(null);
                    this.userLocationMoved = false;
                    showSearchPlacesButton(false);
                    this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(new IMapsProvider.LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude())));
                    if (this.searchedForCustomLocations && this.locationType != 8) {
                        Location location = this.myLocation;
                        if (location != null) {
                            this.adapter.searchPlacesWithQuery(null, location, true, true);
                        }
                        this.searchedForCustomLocations = false;
                        showResults();
                    }
                }
            } else if (this.myLocation != null && (iMap = this.map) != null) {
                iMap.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(new IMapsProvider.LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude()), this.map.getMaxZoomLevel() - 4.0f));
            }
            removeInfoView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5() {
        getLocationController().setProximityLocation(this.dialogId, 0, true);
        this.canUndo = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(LocationController.SharingLocationInfo sharingLocationInfo) {
        this.proximityButton.setImageResource(R.drawable.msg_location_alert2);
        createCircle(sharingLocationInfo.proximityMeters);
        this.canUndo = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(View view) {
        if (getParentActivity() == null || this.myLocation == null || !checkGpsEnabled() || this.map == null) {
            return;
        }
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
        }
        MessagesController.getGlobalMainSettings().edit().putInt("proximityhint", 3).commit();
        final LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId);
        if (this.canUndo) {
            this.undoView[0].hide(true, 1);
        }
        if (sharingLocationInfo == null || sharingLocationInfo.proximityMeters <= 0) {
            openProximityAlert();
            return;
        }
        this.proximityButton.setImageResource(R.drawable.msg_location_alert);
        IMapsProvider.ICircle iCircle = this.proximityCircle;
        if (iCircle != null) {
            iCircle.remove();
            this.proximityCircle = null;
        }
        this.canUndo = true;
        getUndoView().showWithAction(0L, 25, (Object) 0, (Object) null, new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$5();
            }
        }, new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$6(sharingLocationInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$8(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9() {
        updateClipView(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fixLayoutInternal$40(int i) {
        this.layoutManager.scrollToPositionWithOffset(0, -AndroidUtilities.dp(i));
        updateClipView(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecentLocations$41() {
        Runnable runnable;
        getLocationController().markLiveLoactionsAsRead(this.dialogId);
        if (this.isPaused || (runnable = this.markAsReadRunnable) == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecentLocations$42(TLObject tLObject, long j) {
        if (this.map == null) {
            return;
        }
        TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
        int i = 0;
        while (i < messages_messages.messages.size()) {
            if (!(messages_messages.messages.get(i).media instanceof TLRPC.TL_messageMediaGeoLive)) {
                messages_messages.messages.remove(i);
                i--;
            }
            i++;
        }
        getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
        getMessagesController().putUsers(messages_messages.users, false);
        getMessagesController().putChats(messages_messages.chats, false);
        getLocationController().locationsCache.put(j, messages_messages.messages);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsCacheChanged, Long.valueOf(j));
        fetchRecentLocations(messages_messages.messages);
        getLocationController().markLiveLoactionsAsRead(this.dialogId);
        if (this.markAsReadRunnable == null) {
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$getRecentLocations$41();
                }
            };
            this.markAsReadRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecentLocations$43(final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$getRecentLocations$42(tLObject, j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$47() {
        IMapsProvider.ICircle iCircle;
        int i;
        this.mapTypeButton.setIconColor(getThemedColor(Theme.key_location_actionIcon));
        this.mapTypeButton.redrawPopup(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
        this.mapTypeButton.setPopupItemsColor(getThemedColor(Theme.key_actionBarDefaultSubmenuItemIcon), true);
        this.mapTypeButton.setPopupItemsColor(getThemedColor(Theme.key_actionBarDefaultSubmenuItem), false);
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogBackground), PorterDuff.Mode.MULTIPLY));
        this.shadow.invalidate();
        if (this.map != null) {
            int mapThemeResId = getMapThemeResId();
            if (mapThemeResId != 0) {
                if (this.currentMapStyleDark) {
                    return;
                }
                this.currentMapStyleDark = true;
                this.map.setMapStyle(ApplicationLoader.getMapsProvider().loadRawResourceStyle(ApplicationLoader.applicationContext, mapThemeResId));
                IMapsProvider.ICircle iCircle2 = this.proximityCircle;
                if (iCircle2 == null) {
                    return;
                }
                iCircle2.setStrokeColor(-1);
                iCircle = this.proximityCircle;
                i = 553648127;
            } else {
                if (!this.currentMapStyleDark) {
                    return;
                }
                this.currentMapStyleDark = false;
                this.map.setMapStyle(null);
                IMapsProvider.ICircle iCircle3 = this.proximityCircle;
                if (iCircle3 == null) {
                    return;
                }
                iCircle3.setStrokeColor(-16777216);
                iCircle = this.proximityCircle;
                i = 536870912;
            }
            iCircle.setFillColor(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckGlScreenshot$44(ViewGroup viewGroup, GLSurfaceView gLSurfaceView) {
        try {
            viewGroup.removeView(gLSurfaceView);
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.hasScreenshot = true;
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckGlScreenshot$45(Bitmap bitmap, final GLSurfaceView gLSurfaceView) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        final ViewGroup viewGroup = (ViewGroup) gLSurfaceView.getParent();
        try {
            viewGroup.addView(imageView, viewGroup.indexOfChild(gLSurfaceView));
        } catch (Exception e) {
            FileLog.e(e);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$onCheckGlScreenshot$44(viewGroup, gLSurfaceView);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckGlScreenshot$46(final GLSurfaceView gLSurfaceView) {
        if (gLSurfaceView.getWidth() == 0 || gLSurfaceView.getHeight() == 0) {
            return;
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(gLSurfaceView.getWidth() * gLSurfaceView.getHeight() * 4);
        GLES20.glReadPixels(0, 0, gLSurfaceView.getWidth(), gLSurfaceView.getHeight(), 6408, 5121, allocateDirect);
        Bitmap createBitmap = Bitmap.createBitmap(gLSurfaceView.getWidth(), gLSurfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(allocateDirect);
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        final Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, createBitmap.getWidth(), createBitmap.getHeight(), matrix, false);
        createBitmap.recycle();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$onCheckGlScreenshot$45(createBitmap2, gLSurfaceView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMapInit$34(int i) {
        View childAt;
        RecyclerView.ViewHolder findContainingViewHolder;
        if (i == 1) {
            showSearchPlacesButton(true);
            removeInfoView();
            this.selectedMarkerId = -1L;
            if (this.scrolling) {
                return;
            }
            int i2 = this.locationType;
            if ((i2 == 0 || i2 == 1) && this.listView.getChildCount() > 0 && (childAt = this.listView.getChildAt(0)) != null && (findContainingViewHolder = this.listView.findContainingViewHolder(childAt)) != null && findContainingViewHolder.getAdapterPosition() == 0) {
                int dp = this.locationType == 0 ? 0 : AndroidUtilities.dp(66.0f);
                int top = childAt.getTop();
                if (top < (-dp)) {
                    IMapsProvider.CameraPosition cameraPosition = this.map.getCameraPosition();
                    this.forceUpdate = ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(cameraPosition.target, cameraPosition.zoom);
                    this.listView.smoothScrollBy(0, top + dp);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMapInit$35(Location location) {
        positionMarker(location);
        getLocationController().setMapLocation(location, this.isFirstLocation);
        this.isFirstLocation = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onMapInit$36(float f, IMapsProvider.IMarker iMarker) {
        if (!(iMarker.getTag() instanceof VenueLocation)) {
            return true;
        }
        this.markerImageView.setVisibility(4);
        if (!this.userLocationMoved) {
            ImageView imageView = this.locationButton;
            int i = Theme.key_location_actionIcon;
            imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i), PorterDuff.Mode.MULTIPLY));
            this.locationButton.setTag(Integer.valueOf(i));
            this.userLocationMoved = true;
        }
        int i2 = 0;
        while (true) {
            if (i2 < this.markers.size()) {
                LiveLocation liveLocation = (LiveLocation) this.markers.get(i2);
                if (liveLocation != null && liveLocation.marker == iMarker) {
                    this.selectedMarkerId = liveLocation.id;
                    this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(liveLocation.marker.getPosition(), f));
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        this.overlayView.addInfoView(iMarker);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMapInit$37() {
        MapOverlayView mapOverlayView = this.overlayView;
        if (mapOverlayView != null) {
            mapOverlayView.updatePositions();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$openProximityAlert$28(boolean z, int i) {
        IMapsProvider.ICircle iCircle = this.proximityCircle;
        if (iCircle != null) {
            iCircle.setRadius(i);
            if (z) {
                moveToBounds(i, true, true);
            }
        }
        if (DialogObject.isChatDialog(this.dialogId)) {
            return true;
        }
        int size = this.markers.size();
        for (int i2 = 0; i2 < size; i2++) {
            LiveLocation liveLocation = (LiveLocation) this.markers.get(i2);
            if (liveLocation.object != null && !UserObject.isUserSelf(liveLocation.user)) {
                TLRPC.GeoPoint geoPoint = liveLocation.object.media.geo;
                Location location = new Location("network");
                location.setLatitude(geoPoint.lat);
                location.setLongitude(geoPoint._long);
                if (this.myLocation.distanceTo(location) > i) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openProximityAlert$29(TLRPC.User user, int i, AlertDialog alertDialog, int i2) {
        shareLiveLocation(user, 900, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$openProximityAlert$30(final TLRPC.User user, boolean z, final int i) {
        if (getLocationController().getSharingLocationInfo(this.dialogId) != null) {
            this.proximitySheet.setRadiusSet();
            this.proximityButton.setImageResource(R.drawable.msg_location_alert2);
            getUndoView().showWithAction(0L, 24, Integer.valueOf(i), user, (Runnable) null, (Runnable) null);
            getLocationController().setProximityLocation(this.dialogId, i, true);
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.ShareLocationAlertTitle));
        builder.setMessage(LocaleController.getString(R.string.ShareLocationAlertText));
        builder.setPositiveButton(LocaleController.getString(R.string.ShareLocationAlertButton), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda46
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                LocationActivity.this.lambda$openProximityAlert$29(user, i, alertDialog, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openProximityAlert$31() {
        IMapsProvider.IMap iMap = this.map;
        if (iMap != null) {
            iMap.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
        }
        if (!this.proximitySheet.getRadiusSet()) {
            double d = this.previousRadius;
            if (d > 0.0d) {
                this.proximityCircle.setRadius(d);
            } else {
                IMapsProvider.ICircle iCircle = this.proximityCircle;
                if (iCircle != null) {
                    iCircle.remove();
                    this.proximityCircle = null;
                }
            }
        }
        this.proximitySheet = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openShareLiveLocation$32(boolean z) {
        openShareLiveLocation(z, this.askWithRadius);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openShareLiveLocation$33(boolean z, TLRPC.User user, int i, int i2) {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        if (!z) {
            shareLiveLocation(user, i2, i);
            return;
        }
        LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo != null) {
            TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tL_messages_editMessage.peer = getMessagesController().getInputPeer(sharingLocationInfo.did);
            tL_messages_editMessage.id = sharingLocationInfo.mid;
            tL_messages_editMessage.flags |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
            TLRPC.TL_inputMediaGeoLive tL_inputMediaGeoLive = new TLRPC.TL_inputMediaGeoLive();
            tL_messages_editMessage.media = tL_inputMediaGeoLive;
            tL_inputMediaGeoLive.stopped = false;
            tL_inputMediaGeoLive.geo_point = new TLRPC.TL_inputGeoPoint();
            Location lastKnownLocation = LocationController.getInstance(this.currentAccount).getLastKnownLocation();
            tL_messages_editMessage.media.geo_point.lat = AndroidUtilities.fixLocationCoord(lastKnownLocation.getLatitude());
            tL_messages_editMessage.media.geo_point._long = AndroidUtilities.fixLocationCoord(lastKnownLocation.getLongitude());
            tL_messages_editMessage.media.geo_point.accuracy_radius = (int) lastKnownLocation.getAccuracy();
            TLRPC.InputMedia inputMedia = tL_messages_editMessage.media;
            TLRPC.InputGeoPoint inputGeoPoint = inputMedia.geo_point;
            if (inputGeoPoint.accuracy_radius != 0) {
                inputGeoPoint.flags |= 1;
            }
            int i3 = sharingLocationInfo.lastSentProximityMeters;
            int i4 = sharingLocationInfo.proximityMeters;
            if (i3 != i4) {
                inputMedia.proximity_notification_radius = i4;
                inputMedia.flags |= 8;
            }
            inputMedia.heading = LocationController.getHeading(lastKnownLocation);
            TLRPC.InputMedia inputMedia2 = tL_messages_editMessage.media;
            int i5 = inputMedia2.flags;
            inputMedia2.flags = i5 | 4;
            int i6 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int i7 = i2 == Integer.MAX_VALUE ? ConnectionsManager.DEFAULT_DATACENTER_ID : sharingLocationInfo.period + i2;
            sharingLocationInfo.period = i7;
            inputMedia2.period = i7;
            if (i2 != Integer.MAX_VALUE) {
                i6 = sharingLocationInfo.stopTime + i2;
            }
            sharingLocationInfo.stopTime = i6;
            inputMedia2.flags = i5 | 6;
            MessageObject messageObject = sharingLocationInfo.messageObject;
            if (messageObject != null && (message = messageObject.messageOwner) != null && (messageMedia = message.media) != null) {
                messageMedia.period = i7;
                getMessagesStorage().replaceMessageIfExists(sharingLocationInfo.messageObject.messageOwner, null, null, true);
            }
            getConnectionsManager().sendRequest(tL_messages_editMessage, null);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPermissionAlert$39(AlertDialog alertDialog, int i) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            getParentActivity().startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeShowProximityHint() {
        SharedPreferences globalMainSettings;
        int i;
        ImageView imageView = this.proximityButton;
        if (imageView == null || imageView.getVisibility() != 0 || this.proximityAnimationInProgress || (i = (globalMainSettings = MessagesController.getGlobalMainSettings()).getInt("proximityhint", 0)) >= 3) {
            return;
        }
        globalMainSettings.edit().putInt("proximityhint", i + 1).commit();
        if (DialogObject.isUserDialog(this.dialogId)) {
            this.hintView.setText(LocaleController.formatString("ProximityTooltioUser", R.string.ProximityTooltioUser, UserObject.getFirstName(getMessagesController().getUser(Long.valueOf(this.dialogId)))));
        } else {
            this.hintView.setText(LocaleController.getString(R.string.ProximityTooltioGroup));
        }
        this.hintView.show();
    }

    private static double meterToLatitude(double d) {
        return Math.toDegrees(d / 6366198.0d);
    }

    private static double meterToLongitude(double d, double d2) {
        return Math.toDegrees(d / (Math.cos(Math.toRadians(d2)) * 6366198.0d));
    }

    private static IMapsProvider.LatLng move(IMapsProvider.LatLng latLng, double d, double d2) {
        double meterToLongitude = meterToLongitude(d2, latLng.latitude);
        return new IMapsProvider.LatLng(latLng.latitude + meterToLatitude(d), latLng.longitude + meterToLongitude);
    }

    private void moveToBounds(int i, boolean z, boolean z2) {
        IMapsProvider.ILatLngBoundsBuilder onCreateLatLngBoundsBuilder = ApplicationLoader.getMapsProvider().onCreateLatLngBoundsBuilder();
        onCreateLatLngBoundsBuilder.include(new IMapsProvider.LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
        try {
            try {
                if (z) {
                    int max = Math.max(i, 250);
                    IMapsProvider.LatLng center = onCreateLatLngBoundsBuilder.build().getCenter();
                    double d = max;
                    IMapsProvider.LatLng move = move(center, d, d);
                    double d2 = -max;
                    onCreateLatLngBoundsBuilder.include(move(center, d2, d2));
                    onCreateLatLngBoundsBuilder.include(move);
                    IMapsProvider.ILatLngBounds build = onCreateLatLngBoundsBuilder.build();
                    this.map.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), (int) ((this.proximitySheet.getCustomView().getMeasuredHeight() - AndroidUtilities.dp(40.0f)) + this.mapViewClip.getTranslationY()));
                    if (z2) {
                        this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngBounds(build, 0), 500, null);
                    } else {
                        this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngBounds(build, 0));
                    }
                } else {
                    int currentTime = getConnectionsManager().getCurrentTime();
                    int size = this.markers.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        TLRPC.Message message = ((LiveLocation) this.markers.get(i2)).object;
                        int i3 = message.date;
                        TLRPC.MessageMedia messageMedia = message.media;
                        if (i3 + messageMedia.period > currentTime) {
                            TLRPC.GeoPoint geoPoint = messageMedia.geo;
                            onCreateLatLngBoundsBuilder.include(new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long));
                        }
                    }
                    IMapsProvider.LatLng center2 = onCreateLatLngBoundsBuilder.build().getCenter();
                    IMapsProvider.LatLng move2 = move(center2, 100.0d, 100.0d);
                    onCreateLatLngBoundsBuilder.include(move(center2, -100.0d, -100.0d));
                    onCreateLatLngBoundsBuilder.include(move2);
                    IMapsProvider.ILatLngBounds build2 = onCreateLatLngBoundsBuilder.build();
                    this.map.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), this.proximitySheet.getCustomView().getMeasuredHeight() - AndroidUtilities.dp(100.0f));
                    this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngBounds(build2, 0));
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } catch (Exception unused) {
        }
    }

    private boolean onCheckGlScreenshot() {
        IMapsProvider.IMapView iMapView = this.mapView;
        if (iMapView == null || iMapView.getGlSurfaceView() == null || this.hasScreenshot) {
            return false;
        }
        final GLSurfaceView glSurfaceView = this.mapView.getGlSurfaceView();
        glSurfaceView.queueEvent(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$onCheckGlScreenshot$46(glSurfaceView);
            }
        });
        return true;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:5|(1:7)(1:53)|8|(1:10)(8:36|(2:38|(1:40)(4:42|43|44|45))(2:49|(1:51)(1:52))|12|13|14|(1:18)|19|(1:29)(2:27|28))|11|12|13|14|(2:16|18)|19|(2:21|31)(1:32)) */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0118, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0119, code lost:
    
        org.telegram.messenger.FileLog.e((java.lang.Throwable) r1, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x006d, code lost:
    
        if (getRecentLocations() == false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onMapInit() {
        LiveLocation addUserMarker;
        ImageView imageView;
        LocationController.SharingLocationInfo sharingLocationInfo;
        int i;
        if (this.map == null) {
            return;
        }
        this.mapView.getView().animate().alpha(1.0f).setStartDelay(200L).setDuration(100L).start();
        final float minZoomLevel = this.initialMaxZoom ? this.map.getMinZoomLevel() + 4.0f : this.map.getMaxZoomLevel() - 4.0f;
        TLRPC.TL_channelLocation tL_channelLocation = this.chatLocation;
        if (tL_channelLocation == null) {
            MessageObject messageObject = this.messageObject;
            if (messageObject == null) {
                Location location = new Location("network");
                this.userLocation = location;
                TLRPC.TL_channelLocation tL_channelLocation2 = this.initialLocation;
                if (tL_channelLocation2 != null) {
                    TLRPC.GeoPoint geoPoint = tL_channelLocation2.geo_point;
                    this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long), minZoomLevel));
                    this.userLocation.setLatitude(this.initialLocation.geo_point.lat);
                    this.userLocation.setLongitude(this.initialLocation.geo_point._long);
                    this.userLocation.setAccuracy(this.initialLocation.geo_point.accuracy_radius);
                    this.adapter.setCustomLocation(this.userLocation);
                } else {
                    location.setLatitude(20.659322d);
                    this.userLocation.setLongitude(-11.40625d);
                }
            } else if (messageObject.isLiveLocation()) {
                addUserMarker = addUserMarker(this.messageObject.messageOwner);
            } else {
                IMapsProvider.LatLng latLng = new IMapsProvider.LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude());
                try {
                    this.map.addMarker(ApplicationLoader.getMapsProvider().onCreateMarkerOptions().position(latLng).icon(R.drawable.map_pin2));
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(latLng, minZoomLevel));
                this.firstFocus = false;
                getRecentLocations();
            }
            this.map.setMyLocationEnabled(true);
            this.map.getUiSettings().setMyLocationButtonEnabled(false);
            this.map.getUiSettings().setZoomControlsEnabled(false);
            this.map.getUiSettings().setCompassEnabled(false);
            this.map.setOnCameraMoveStartedListener(new IMapsProvider.OnCameraMoveStartedListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda42
                @Override // org.telegram.messenger.IMapsProvider.OnCameraMoveStartedListener
                public final void onCameraMoveStarted(int i2) {
                    LocationActivity.this.lambda$onMapInit$34(i2);
                }
            });
            this.map.setOnMyLocationChangeListener(new Consumer() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda43
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    LocationActivity.this.lambda$onMapInit$35((Location) obj);
                }
            });
            this.map.setOnMarkerClickListener(new IMapsProvider.OnMarkerClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda44
                @Override // org.telegram.messenger.IMapsProvider.OnMarkerClickListener
                public final boolean onClick(IMapsProvider.IMarker iMarker) {
                    boolean lambda$onMapInit$36;
                    lambda$onMapInit$36 = LocationActivity.this.lambda$onMapInit$36(minZoomLevel, iMarker);
                    return lambda$onMapInit$36;
                }
            });
            this.map.setOnCameraMoveListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$onMapInit$37();
                }
            });
            Location lastLocation = getLastLocation();
            this.myLocation = lastLocation;
            positionMarker(lastLocation);
            if (this.checkGpsEnabled && getParentActivity() != null) {
                this.checkGpsEnabled = false;
                checkGpsEnabled();
            }
            imageView = this.proximityButton;
            if (imageView != null || imageView.getVisibility() != 0 || (sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId)) == null || (i = sharingLocationInfo.proximityMeters) <= 0) {
                return;
            }
            createCircle(i);
            return;
        }
        addUserMarker = addUserMarker(tL_channelLocation);
        this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(addUserMarker.marker.getPosition(), minZoomLevel));
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(false);
        this.map.getUiSettings().setZoomControlsEnabled(false);
        this.map.getUiSettings().setCompassEnabled(false);
        this.map.setOnCameraMoveStartedListener(new IMapsProvider.OnCameraMoveStartedListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda42
            @Override // org.telegram.messenger.IMapsProvider.OnCameraMoveStartedListener
            public final void onCameraMoveStarted(int i2) {
                LocationActivity.this.lambda$onMapInit$34(i2);
            }
        });
        this.map.setOnMyLocationChangeListener(new Consumer() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda43
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                LocationActivity.this.lambda$onMapInit$35((Location) obj);
            }
        });
        this.map.setOnMarkerClickListener(new IMapsProvider.OnMarkerClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda44
            @Override // org.telegram.messenger.IMapsProvider.OnMarkerClickListener
            public final boolean onClick(IMapsProvider.IMarker iMarker) {
                boolean lambda$onMapInit$36;
                lambda$onMapInit$36 = LocationActivity.this.lambda$onMapInit$36(minZoomLevel, iMarker);
                return lambda$onMapInit$36;
            }
        });
        this.map.setOnCameraMoveListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$onMapInit$37();
            }
        });
        Location lastLocation2 = getLastLocation();
        this.myLocation = lastLocation2;
        positionMarker(lastLocation2);
        if (this.checkGpsEnabled) {
            this.checkGpsEnabled = false;
            checkGpsEnabled();
        }
        imageView = this.proximityButton;
        if (imageView != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002f A[Catch: Exception -> 0x007f, TRY_ENTER, TryCatch #0 {Exception -> 0x007f, blocks: (B:13:0x002f, B:14:0x007b, B:18:0x0081), top: B:11:0x002d }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0081 A[Catch: Exception -> 0x007f, TRY_LEAVE, TryCatch #0 {Exception -> 0x007f, blocks: (B:13:0x002f, B:14:0x007b, B:18:0x0081), top: B:11:0x002d }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void openDirections(LiveLocation liveLocation) {
        TLRPC.GeoPoint geoPoint;
        TLRPC.Message message;
        Intent intent;
        Activity parentActivity;
        try {
            if (liveLocation == null || (message = liveLocation.object) == null) {
                MessageObject messageObject = this.messageObject;
                if (messageObject == null) {
                    geoPoint = this.chatLocation.geo_point;
                    double d = geoPoint.lat;
                    double d2 = geoPoint._long;
                    String str = !BuildVars.isHuaweiStoreApp() ? "mapapp://navigation" : "http://maps.google.com/maps";
                    if (this.myLocation == null) {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, str + "?saddr=%f,%f&daddr=%f,%f", Double.valueOf(this.myLocation.getLatitude()), Double.valueOf(this.myLocation.getLongitude()), Double.valueOf(d), Double.valueOf(d2))));
                        parentActivity = getParentActivity();
                    } else {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, str + "?saddr=&daddr=%f,%f", Double.valueOf(d), Double.valueOf(d2))));
                        parentActivity = getParentActivity();
                    }
                    parentActivity.startActivity(intent);
                    return;
                }
                message = messageObject.messageOwner;
            }
            if (this.myLocation == null) {
            }
            parentActivity.startActivity(intent);
            return;
        } catch (Exception e) {
            FileLog.e(e);
            return;
        }
        geoPoint = message.media.geo;
        double d3 = geoPoint.lat;
        double d22 = geoPoint._long;
        if (!BuildVars.isHuaweiStoreApp()) {
        }
    }

    private void openProximityAlert() {
        IMapsProvider.ICircle iCircle = this.proximityCircle;
        if (iCircle == null) {
            createCircle(500);
        } else {
            this.previousRadius = iCircle.getRadius();
        }
        final TLRPC.User user = DialogObject.isUserDialog(this.dialogId) ? getMessagesController().getUser(Long.valueOf(this.dialogId)) : null;
        ProximitySheet proximitySheet = new ProximitySheet(getParentActivity(), user, new ProximitySheet.onRadiusPickerChange() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda37
            @Override // org.telegram.ui.Components.ProximitySheet.onRadiusPickerChange
            public final boolean run(boolean z, int i) {
                boolean lambda$openProximityAlert$28;
                lambda$openProximityAlert$28 = LocationActivity.this.lambda$openProximityAlert$28(z, i);
                return lambda$openProximityAlert$28;
            }
        }, new ProximitySheet.onRadiusPickerChange() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda38
            @Override // org.telegram.ui.Components.ProximitySheet.onRadiusPickerChange
            public final boolean run(boolean z, int i) {
                boolean lambda$openProximityAlert$30;
                lambda$openProximityAlert$30 = LocationActivity.this.lambda$openProximityAlert$30(user, z, i);
                return lambda$openProximityAlert$30;
            }
        }, new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$openProximityAlert$31();
            }
        });
        this.proximitySheet = proximitySheet;
        ((FrameLayout) this.fragmentView).addView(proximitySheet, LayoutHelper.createFrame(-1, -1.0f));
        this.proximitySheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openShareLiveLocation(final boolean z, final int i) {
        Activity parentActivity;
        int checkSelfPermission;
        if (this.delegate == null || disablePermissionCheck() || getParentActivity() == null || this.myLocation == null || !checkGpsEnabled()) {
            return;
        }
        if (this.checkBackgroundPermission && Build.VERSION.SDK_INT >= 29 && (parentActivity = getParentActivity()) != null) {
            this.askWithRadius = i;
            this.checkBackgroundPermission = false;
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (Math.abs((System.currentTimeMillis() / 1000) - globalMainSettings.getInt("backgroundloc", 0)) > 86400) {
                checkSelfPermission = parentActivity.checkSelfPermission("android.permission.ACCESS_BACKGROUND_LOCATION");
                if (checkSelfPermission != 0) {
                    globalMainSettings.edit().putInt("backgroundloc", (int) (System.currentTimeMillis() / 1000)).commit();
                    AlertsCreator.createBackgroundLocationPermissionDialog(parentActivity, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda18
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationActivity.this.lambda$openShareLiveLocation$32(z);
                        }
                    }, null).show();
                    return;
                }
            }
        }
        final TLRPC.User user = DialogObject.isUserDialog(this.dialogId) ? getMessagesController().getUser(Long.valueOf(this.dialogId)) : null;
        showDialog(AlertsCreator.createLocationUpdateDialog(getParentActivity(), z, user, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda19
            @Override // org.telegram.messenger.MessagesStorage.IntCallback
            public final void run(int i2) {
                LocationActivity.this.lambda$openShareLiveLocation$33(z, user, i, i2);
            }
        }, null));
    }

    private void positionMarker(Location location) {
        int i;
        if (location == null) {
            return;
        }
        this.myLocation = new Location(location);
        LiveLocation liveLocation = (LiveLocation) this.markersMap.get(getUserConfig().getClientUserId());
        LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId);
        if (liveLocation != null && sharingLocationInfo != null && liveLocation.object.id == sharingLocationInfo.mid) {
            IMapsProvider.LatLng latLng = new IMapsProvider.LatLng(location.getLatitude(), location.getLongitude());
            liveLocation.marker.setPosition(latLng);
            IMapsProvider.IMarker iMarker = liveLocation.directionMarker;
            if (iMarker != null) {
                iMarker.setPosition(latLng);
            }
            if (this.selectedMarkerId == liveLocation.id) {
                this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(liveLocation.marker.getPosition()));
            }
        }
        if (this.messageObject == null && this.chatLocation == null && this.map != null) {
            IMapsProvider.LatLng latLng2 = new IMapsProvider.LatLng(location.getLatitude(), location.getLongitude());
            LocationActivityAdapter locationActivityAdapter = this.adapter;
            if (locationActivityAdapter != null) {
                if (!this.searchedForCustomLocations && (i = this.locationType) != 4 && i != 8) {
                    locationActivityAdapter.searchPlacesWithQuery(null, this.myLocation, true);
                }
                this.adapter.setGpsLocation(this.myLocation);
            }
            if (!this.userLocationMoved) {
                this.userLocation = new Location(location);
                if (this.firstWas) {
                    this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(latLng2));
                } else {
                    this.firstWas = true;
                    this.map.moveCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLngZoom(latLng2, this.map.getMaxZoomLevel() - 4.0f));
                }
            }
        } else {
            this.adapter.setGpsLocation(this.myLocation);
        }
        ProximitySheet proximitySheet = this.proximitySheet;
        if (proximitySheet != null) {
            proximitySheet.updateText(true, true);
        }
        IMapsProvider.ICircle iCircle = this.proximityCircle;
        if (iCircle != null) {
            iCircle.setCenter(new IMapsProvider.LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
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

    private void shareLiveLocation(TLRPC.User user, int i, int i2) {
        TLRPC.TL_messageMediaGeoLive tL_messageMediaGeoLive = new TLRPC.TL_messageMediaGeoLive();
        TLRPC.TL_geoPoint tL_geoPoint = new TLRPC.TL_geoPoint();
        tL_messageMediaGeoLive.geo = tL_geoPoint;
        tL_geoPoint.lat = AndroidUtilities.fixLocationCoord(this.myLocation.getLatitude());
        tL_messageMediaGeoLive.geo._long = AndroidUtilities.fixLocationCoord(this.myLocation.getLongitude());
        tL_messageMediaGeoLive.heading = LocationController.getHeading(this.myLocation);
        int i3 = tL_messageMediaGeoLive.flags;
        tL_messageMediaGeoLive.period = i;
        tL_messageMediaGeoLive.proximity_notification_radius = i2;
        tL_messageMediaGeoLive.flags = i3 | 9;
        this.delegate.didSelectLocation(tL_messageMediaGeoLive, this.locationType, true, 0);
        if (i2 <= 0) {
            lambda$onBackPressed$323();
            return;
        }
        this.proximitySheet.setRadiusSet();
        this.proximityButton.setImageResource(R.drawable.msg_location_alert2);
        ProximitySheet proximitySheet = this.proximitySheet;
        if (proximitySheet != null) {
            proximitySheet.dismiss();
        }
        getUndoView().showWithAction(0L, 24, Integer.valueOf(i2), user, (Runnable) null, (Runnable) null);
    }

    private void showPermissionAlert(boolean z) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTopAnimation(R.raw.permission_request_location, 72, false, getThemedColor(Theme.key_dialogTopBackground));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(z ? R.string.PermissionNoLocationNavigation : R.string.PermissionNoLocationFriends)));
        builder.setNegativeButton(LocaleController.getString(R.string.PermissionOpenSettings), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda35
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                LocationActivity.this.lambda$showPermissionAlert$39(alertDialog, i);
            }
        });
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        showDialog(builder.create());
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

    /* JADX INFO: Access modifiers changed from: private */
    public void showSearchPlacesButton(boolean z) {
        SearchButton searchButton;
        Location location;
        Location location2;
        if (this.locationType == 3) {
            z = true;
        }
        if (z && (searchButton = this.searchAreaButton) != null && searchButton.getTag() == null && ((location = this.myLocation) == null || (location2 = this.userLocation) == null || location2.distanceTo(location) < 300.0f)) {
            z = false;
        }
        SearchButton searchButton2 = this.searchAreaButton;
        if (searchButton2 != null) {
            if (!z || searchButton2.getTag() == null) {
                if (z || this.searchAreaButton.getTag() != null) {
                    this.searchAreaButton.setTag(z ? 1 : null);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(this.searchAreaButton, (Property<SearchButton, Float>) View.TRANSLATION_X, z ? 0.0f : -AndroidUtilities.dp(80.0f)));
                    animatorSet.setDuration(180L);
                    animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                    animatorSet.start();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateClipView(boolean z) {
        int i;
        int i2;
        FrameLayout.LayoutParams layoutParams;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(0);
        if (findViewHolderForAdapterPosition != null) {
            i = (int) findViewHolderForAdapterPosition.itemView.getY();
            i2 = this.overScrollHeight + Math.min(i, 0);
        } else {
            i = -this.mapViewClip.getMeasuredHeight();
            i2 = 0;
        }
        if (((FrameLayout.LayoutParams) this.mapViewClip.getLayoutParams()) != null) {
            int visibility = this.mapView.getView().getVisibility();
            if (i2 <= 0) {
                if (visibility == 0) {
                    this.mapView.getView().setVisibility(4);
                    this.mapViewClip.setVisibility(4);
                    MapOverlayView mapOverlayView = this.overlayView;
                    if (mapOverlayView != null) {
                        mapOverlayView.setVisibility(4);
                    }
                }
            } else if (visibility == 4) {
                this.mapView.getView().setVisibility(0);
                this.mapViewClip.setVisibility(0);
                MapOverlayView mapOverlayView2 = this.overlayView;
                if (mapOverlayView2 != null) {
                    mapOverlayView2.setVisibility(0);
                }
            }
            this.mapViewClip.setTranslationY(Math.min(0, i));
            int i3 = -i;
            int i4 = i3 / 2;
            this.mapView.getView().setTranslationY(Math.max(0, i4));
            MapOverlayView mapOverlayView3 = this.overlayView;
            if (mapOverlayView3 != null) {
                mapOverlayView3.setTranslationY(Math.max(0, i4));
            }
            int measuredHeight = this.overScrollHeight - this.mapTypeButton.getMeasuredHeight();
            int i5 = this.locationType;
            float min = Math.min(measuredHeight - AndroidUtilities.dp(64 + ((i5 == 0 || i5 == 1) ? 30 : 10)), i3);
            this.mapTypeButton.setTranslationY(min);
            this.proximityButton.setTranslationY(min);
            HintView2 hintView2 = this.hintView;
            if (hintView2 != null) {
                hintView2.setTranslationY(min);
            }
            SearchButton searchButton = this.searchAreaButton;
            if (searchButton != null) {
                searchButton.setTranslation(min);
            }
            View view = this.markerImageView;
            if (view != null) {
                int dp = (i3 - AndroidUtilities.dp(view.getTag() == null ? 48.0f : 69.0f)) + (i2 / 2);
                this.markerTop = dp;
                view.setTranslationY(dp);
            }
            if (z) {
                return;
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mapView.getView().getLayoutParams();
            if (layoutParams2 != null && layoutParams2.height != this.overScrollHeight + AndroidUtilities.dp(10.0f)) {
                layoutParams2.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
                IMapsProvider.IMap iMap = this.map;
                if (iMap != null) {
                    iMap.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
                }
                this.mapView.getView().setLayoutParams(layoutParams2);
            }
            MapOverlayView mapOverlayView4 = this.overlayView;
            if (mapOverlayView4 == null || (layoutParams = (FrameLayout.LayoutParams) mapOverlayView4.getLayoutParams()) == null || layoutParams.height == this.overScrollHeight + AndroidUtilities.dp(10.0f)) {
                return;
            }
            layoutParams.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
            this.overlayView.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmptyView() {
        if (!this.searching) {
            this.emptyView.setVisibility(8);
        } else {
            if (!this.searchInProgress) {
                this.searchListView.setEmptyView(this.emptyView);
                return;
            }
            this.searchListView.setEmptyView(null);
            this.emptyView.setVisibility(8);
            this.searchListView.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlacesMarkers(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        int size = this.placeMarkers.size();
        for (int i = 0; i < size; i++) {
            ((VenueLocation) this.placeMarkers.get(i)).marker.remove();
        }
        this.placeMarkers.clear();
        int size2 = arrayList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            TLRPC.TL_messageMediaVenue tL_messageMediaVenue = (TLRPC.TL_messageMediaVenue) arrayList.get(i2);
            try {
                IMapsProvider.IMarkerOptions onCreateMarkerOptions = ApplicationLoader.getMapsProvider().onCreateMarkerOptions();
                TLRPC.GeoPoint geoPoint = tL_messageMediaVenue.geo;
                IMapsProvider.IMarkerOptions position = onCreateMarkerOptions.position(new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long));
                position.icon(createPlaceBitmap(i2));
                position.anchor(0.5f, 0.5f);
                position.title(tL_messageMediaVenue.title);
                position.snippet(tL_messageMediaVenue.address);
                VenueLocation venueLocation = new VenueLocation();
                venueLocation.num = i2;
                IMapsProvider.IMarker addMarker = this.map.addMarker(position);
                venueLocation.marker = addMarker;
                venueLocation.venue = tL_messageMediaVenue;
                addMarker.setTag(venueLocation);
                this.placeMarkers.add(venueLocation);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0af1  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0bf8  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0c05  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0c5a  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0c6f  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0bd7  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0be5  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0beb  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0a16  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0941  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0770  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x073a  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0733  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x069e  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x062b  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0514  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x050d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0215 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x050a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0511  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0628  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0673  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0730  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0737  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x075f  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0777  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x093e  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0988  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0a4a  */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v5 */
    /* JADX WARN: Type inference failed for: r14v6 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        Location location;
        TLRPC.GeoPoint geoPoint;
        int i;
        boolean z;
        ActionBarMenu createMenu;
        ActionBar actionBar;
        int i2;
        MessageObject messageObject;
        ActionBarMenu actionBarMenu;
        Property property;
        int i3;
        NestedFrameLayout nestedFrameLayout;
        Rect rect;
        Drawable drawable;
        Property property2;
        Drawable drawable2;
        Property property3;
        Drawable drawable3;
        MessageObject messageObject2;
        TLRPC.Chat chat;
        FrameLayout.LayoutParams layoutParams;
        NestedFrameLayout nestedFrameLayout2;
        ActionBarMenu actionBarMenu2;
        int i4;
        int i5;
        boolean z2;
        int i6;
        MessageObject messageObject3;
        MessageObject messageObject4;
        ?? r14;
        TLRPC.TL_channelLocation tL_channelLocation;
        MessageObject messageObject5;
        int i7;
        int i8;
        long j;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        Property property4;
        Drawable drawable4;
        int i9;
        int checkSelfPermission;
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
        if (this.chatLocation == null) {
            if (this.messageObject != null) {
                Location location2 = new Location("network");
                this.userLocation = location2;
                location2.setLatitude(this.messageObject.messageOwner.media.geo.lat);
                location = this.userLocation;
                geoPoint = this.messageObject.messageOwner.media.geo;
            }
            i = Build.VERSION.SDK_INT;
            if (i >= 23 && getParentActivity() != null) {
                checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
                if (checkSelfPermission != 0) {
                    z = true;
                    this.locationDenied = z;
                    ActionBar actionBar2 = this.actionBar;
                    int i10 = Theme.key_dialogBackground;
                    actionBar2.setBackgroundColor(getThemedColor(i10));
                    ActionBar actionBar3 = this.actionBar;
                    int i11 = Theme.key_dialogTextBlack;
                    actionBar3.setTitleColor(getThemedColor(i11));
                    this.actionBar.setItemsColor(getThemedColor(i11), false);
                    this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_dialogButtonSelector), false);
                    this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
                    this.actionBar.setAllowOverlayTitle(true);
                    if (AndroidUtilities.isTablet()) {
                        this.actionBar.setOccupyStatusBar(false);
                    }
                    this.actionBar.setAddToContainer(false);
                    this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LocationActivity.1
                        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                        public void onItemClick(int i12) {
                            if (i12 == -1) {
                                LocationActivity.this.lambda$onBackPressed$323();
                                return;
                            }
                            if (i12 != 1) {
                                if (i12 == 5) {
                                    LocationActivity.this.openShareLiveLocation(false, 0);
                                    return;
                                } else {
                                    if (i12 == 6) {
                                        LocationActivity.this.openDirections(null);
                                        return;
                                    }
                                    return;
                                }
                            }
                            try {
                                double d = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                                double d2 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                                LocationActivity.this.getParentActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:" + d + "," + d2 + "?q=" + d + "," + d2)));
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    });
                    createMenu = this.actionBar.createMenu();
                    if (this.chatLocation != null) {
                        this.actionBar.setTitle(LocaleController.getString(R.string.ChatLocation));
                    } else {
                        MessageObject messageObject6 = this.messageObject;
                        if (messageObject6 == null) {
                            this.actionBar.setTitle(LocaleController.getString(R.string.ShareLocation));
                            if (this.locationType != 4) {
                                this.overlayView = new MapOverlayView(context);
                                ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, R.drawable.ic_ab_search, getResourceProvider()).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.LocationActivity.2
                                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                                    public void onSearchCollapse() {
                                        LocationActivity.this.searching = false;
                                        LocationActivity.this.searchWas = false;
                                        LocationActivity.this.searchAdapter.searchDelayed(null, null);
                                        LocationActivity.this.updateEmptyView();
                                        if (LocationActivity.this.locationType == 8) {
                                            if (LocationActivity.this.otherItem != null) {
                                                LocationActivity.this.otherItem.setVisibility(0);
                                            }
                                            LocationActivity.this.listView.setVisibility(0);
                                            LocationActivity.this.mapViewClip.setVisibility(0);
                                            LocationActivity.this.searchListView.setAdapter(null);
                                            LocationActivity.this.searchListView.setVisibility(8);
                                        }
                                    }

                                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                                    public void onSearchExpand() {
                                        LocationActivity.this.searching = true;
                                    }

                                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                                    public void onTextChanged(EditText editText) {
                                        if (LocationActivity.this.searchAdapter == null) {
                                            return;
                                        }
                                        String obj = editText.getText().toString();
                                        if (obj.length() != 0) {
                                            LocationActivity.this.searchWas = true;
                                            LocationActivity.this.searchItem.setShowSearchProgress(true);
                                            if (LocationActivity.this.otherItem != null) {
                                                LocationActivity.this.otherItem.setVisibility(8);
                                            }
                                            LocationActivity.this.listView.setVisibility(8);
                                            LocationActivity.this.mapViewClip.setVisibility(8);
                                            if (LocationActivity.this.searchListView.getAdapter() != LocationActivity.this.searchAdapter) {
                                                LocationActivity.this.searchListView.setAdapter(LocationActivity.this.searchAdapter);
                                            }
                                            LocationActivity.this.searchListView.setVisibility(0);
                                            LocationActivity locationActivity = LocationActivity.this;
                                            locationActivity.searchInProgress = locationActivity.searchAdapter.getItemCount() == 0;
                                        } else {
                                            if (LocationActivity.this.otherItem != null) {
                                                LocationActivity.this.otherItem.setVisibility(0);
                                            }
                                            LocationActivity.this.listView.setVisibility(0);
                                            LocationActivity.this.mapViewClip.setVisibility(0);
                                            LocationActivity.this.searchListView.setAdapter(null);
                                            LocationActivity.this.searchListView.setVisibility(8);
                                        }
                                        LocationActivity.this.updateEmptyView();
                                        LocationActivity.this.searchAdapter.searchDelayed(obj, LocationActivity.this.userLocation);
                                    }
                                });
                                this.searchItem = actionBarMenuItemSearchListener;
                                int i12 = R.string.Search;
                                actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(i12));
                                this.searchItem.setContentDescription(LocaleController.getString(i12));
                                EditTextBoldCursor searchField = this.searchItem.getSearchField();
                                searchField.setTextColor(getThemedColor(i11));
                                searchField.setCursorColor(getThemedColor(i11));
                                searchField.setHintTextColor(getThemedColor(Theme.key_chat_messagePanelHint));
                            }
                        } else if (messageObject6.isLiveLocation()) {
                            this.actionBar.setTitle(LocaleController.getString(R.string.AttachLiveLocation));
                            ActionBarMenuItem addItem = createMenu.addItem(0, R.drawable.ic_ab_other, getResourceProvider());
                            this.otherItem = addItem;
                            addItem.addSubItem(6, R.drawable.filled_directions, LocaleController.getString(R.string.GetDirections));
                        } else {
                            String str = this.messageObject.messageOwner.media.title;
                            if (str == null || str.length() <= 0) {
                                actionBar = this.actionBar;
                                i2 = R.string.ChatLocation;
                            } else {
                                actionBar = this.actionBar;
                                i2 = R.string.SharedPlace;
                            }
                            actionBar.setTitle(LocaleController.getString(i2));
                            if (this.locationType != 3) {
                                ActionBarMenuItem addItem2 = createMenu.addItem(0, R.drawable.ic_ab_other, getResourceProvider());
                                this.otherItem = addItem2;
                                addItem2.addSubItem(1, R.drawable.msg_openin, LocaleController.getString(R.string.OpenInExternalApp));
                                if (!getLocationController().isSharingLocation(this.dialogId) && this.isSharingAllowed) {
                                    this.otherItem.addSubItem(5, R.drawable.msg_location, LocaleController.getString(R.string.SendLiveLocationMenu));
                                }
                                this.otherItem.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
                            }
                        }
                    }
                    NestedFrameLayout nestedFrameLayout3 = new NestedFrameLayout(context);
                    this.fragmentView = nestedFrameLayout3;
                    nestedFrameLayout3.setBackgroundColor(getThemedColor(i10));
                    Drawable mutate = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                    this.shadowDrawable = mutate;
                    int themedColor = getThemedColor(i10);
                    PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                    mutate.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
                    Rect rect2 = new Rect();
                    this.shadowDrawable.getPadding(rect2);
                    int i13 = this.locationType;
                    FrameLayout.LayoutParams layoutParams2 = (i13 != 0 || i13 == 1) ? new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(21.0f) + rect2.top) : new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(6.0f) + rect2.top);
                    layoutParams2.gravity = 83;
                    FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.LocationActivity.3
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i14, int i15) {
                            super.onMeasure(i14, i15);
                            if (LocationActivity.this.overlayView != null) {
                                LocationActivity.this.overlayView.updatePositions();
                            }
                        }
                    };
                    this.mapViewClip = frameLayout;
                    frameLayout.setBackgroundDrawable(new MapPlaceholderDrawable(isActiveThemeDark()));
                    messageObject = this.messageObject;
                    if ((messageObject == null || !((i9 = this.locationType) == 0 || i9 == 1)) && (messageObject == null || this.locationType != 3)) {
                        actionBarMenu = createMenu;
                    } else {
                        SearchButton searchButton = new SearchButton(context);
                        this.searchAreaButton = searchButton;
                        searchButton.setTranslationX(-AndroidUtilities.dp(80.0f));
                        Drawable createSimpleSelectorRoundRectDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(40.0f), getThemedColor(Theme.key_location_actionBackground), getThemedColor(Theme.key_location_actionPressedBackground));
                        if (i < 21) {
                            Drawable mutate2 = context.getResources().getDrawable(R.drawable.places_btn).mutate();
                            mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
                            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate2, createSimpleSelectorRoundRectDrawable, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
                            combinedDrawable.setFullsize(true);
                            drawable4 = combinedDrawable;
                            actionBarMenu = createMenu;
                        } else {
                            StateListAnimator stateListAnimator = new StateListAnimator();
                            int[] iArr = {android.R.attr.state_pressed};
                            SearchButton searchButton2 = this.searchAreaButton;
                            property4 = View.TRANSLATION_Z;
                            actionBarMenu = createMenu;
                            stateListAnimator.addState(iArr, ObjectAnimator.ofFloat(searchButton2, (Property<SearchButton, Float>) property4, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.searchAreaButton, (Property<SearchButton, Float>) property4, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                            this.searchAreaButton.setStateListAnimator(stateListAnimator);
                            this.searchAreaButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.LocationActivity.4
                                @Override // android.view.ViewOutlineProvider
                                public void getOutline(View view, Outline outline) {
                                    outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), view.getMeasuredHeight() / 2);
                                }
                            });
                            drawable4 = createSimpleSelectorRoundRectDrawable;
                        }
                        this.searchAreaButton.setBackgroundDrawable(drawable4);
                        this.searchAreaButton.setTextColor(getThemedColor(Theme.key_location_actionActiveIcon));
                        this.searchAreaButton.setTextSize(1, 14.0f);
                        this.searchAreaButton.setTypeface(AndroidUtilities.bold());
                        this.searchAreaButton.setGravity(17);
                        this.searchAreaButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
                        this.mapViewClip.addView(this.searchAreaButton, LayoutHelper.createFrame(-2, i >= 21 ? 40.0f : 44.0f, 49, 80.0f, 12.0f, 80.0f, 0.0f));
                        if (this.locationType == 3) {
                            this.searchAreaButton.setText(LocaleController.getString(R.string.OpenInMaps));
                            this.searchAreaButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda16
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    LocationActivity.this.lambda$createView$0(view);
                                }
                            });
                            this.searchAreaButton.setTranslationX(0.0f);
                        } else {
                            this.searchAreaButton.setText(LocaleController.getString(R.string.PlacesInThisArea));
                            this.searchAreaButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda17
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    LocationActivity.this.lambda$createView$1(view);
                                }
                            });
                        }
                    }
                    int i14 = Theme.key_location_actionIcon;
                    ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, getThemedColor(i14), getResourceProvider());
                    this.mapTypeButton = actionBarMenuItem;
                    actionBarMenuItem.setClickable(true);
                    this.mapTypeButton.setSubMenuOpenSide(2);
                    this.mapTypeButton.setAdditionalXOffset(AndroidUtilities.dp(10.0f));
                    this.mapTypeButton.setAdditionalYOffset(-AndroidUtilities.dp(10.0f));
                    this.mapTypeButton.addSubItem(2, R.drawable.msg_map, LocaleController.getString(R.string.Map), getResourceProvider());
                    this.mapTypeButton.addSubItem(3, R.drawable.msg_satellite, LocaleController.getString(R.string.Satellite), getResourceProvider());
                    this.mapTypeButton.addSubItem(4, R.drawable.msg_hybrid, LocaleController.getString(R.string.Hybrid), getResourceProvider());
                    this.mapTypeButton.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
                    int dp = AndroidUtilities.dp(40.0f);
                    int i15 = Theme.key_location_actionBackground;
                    int themedColor2 = getThemedColor(i15);
                    int i16 = Theme.key_location_actionPressedBackground;
                    Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, themedColor2, getThemedColor(i16));
                    if (i < 21) {
                        Drawable mutate3 = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                        mutate3.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
                        CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate3, createSimpleSelectorCircleDrawable, 0, 0);
                        combinedDrawable2.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                        drawable = combinedDrawable2;
                        i3 = i14;
                        rect = rect2;
                        nestedFrameLayout = nestedFrameLayout3;
                    } else {
                        StateListAnimator stateListAnimator2 = new StateListAnimator();
                        int[] iArr2 = {android.R.attr.state_pressed};
                        ActionBarMenuItem actionBarMenuItem2 = this.mapTypeButton;
                        property = View.TRANSLATION_Z;
                        i3 = i14;
                        nestedFrameLayout = nestedFrameLayout3;
                        stateListAnimator2.addState(iArr2, ObjectAnimator.ofFloat(actionBarMenuItem2, (Property<ActionBarMenuItem, Float>) property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        rect = rect2;
                        stateListAnimator2.addState(new int[0], ObjectAnimator.ofFloat(this.mapTypeButton, (Property<ActionBarMenuItem, Float>) property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.mapTypeButton.setStateListAnimator(stateListAnimator2);
                        this.mapTypeButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.LocationActivity.5
                            @Override // android.view.ViewOutlineProvider
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                            }
                        });
                        drawable = createSimpleSelectorCircleDrawable;
                    }
                    this.mapTypeButton.setBackgroundDrawable(drawable);
                    this.mapTypeButton.setIcon(R.drawable.msg_map_type);
                    this.mapViewClip.addView(this.mapTypeButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 12.0f, 12.0f, 0.0f));
                    this.mapTypeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LocationActivity.this.lambda$createView$2(view);
                        }
                    });
                    this.mapTypeButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda2
                        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
                        public final void onItemClick(int i17) {
                            LocationActivity.this.lambda$createView$3(i17);
                        }
                    });
                    this.locationButton = new ImageView(context);
                    Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i15), getThemedColor(i16));
                    if (i < 21) {
                        Drawable mutate4 = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                        mutate4.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
                        CombinedDrawable combinedDrawable3 = new CombinedDrawable(mutate4, createSimpleSelectorCircleDrawable2, 0, 0);
                        combinedDrawable3.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                        drawable2 = combinedDrawable3;
                    } else {
                        StateListAnimator stateListAnimator3 = new StateListAnimator();
                        int[] iArr3 = {android.R.attr.state_pressed};
                        ImageView imageView = this.locationButton;
                        property2 = View.TRANSLATION_Z;
                        stateListAnimator3.addState(iArr3, ObjectAnimator.ofFloat(imageView, (Property<ImageView, Float>) property2, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        stateListAnimator3.addState(new int[0], ObjectAnimator.ofFloat(this.locationButton, (Property<ImageView, Float>) property2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.locationButton.setStateListAnimator(stateListAnimator3);
                        this.locationButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.LocationActivity.6
                            @Override // android.view.ViewOutlineProvider
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                            }
                        });
                        drawable2 = createSimpleSelectorCircleDrawable2;
                    }
                    this.locationButton.setBackgroundDrawable(drawable2);
                    this.locationButton.setImageResource(R.drawable.msg_current_location);
                    ImageView imageView2 = this.locationButton;
                    ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
                    imageView2.setScaleType(scaleType);
                    ImageView imageView3 = this.locationButton;
                    int i17 = Theme.key_location_actionActiveIcon;
                    imageView3.setColorFilter(new PorterDuffColorFilter(getThemedColor(i17), mode));
                    this.locationButton.setTag(Integer.valueOf(i17));
                    this.locationButton.setContentDescription(LocaleController.getString(R.string.AccDescrMyLocation));
                    FrameLayout.LayoutParams createFrame = LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 85, 0.0f, 0.0f, 12.0f, 12.0f);
                    final Rect rect3 = rect;
                    createFrame.bottomMargin += layoutParams2.height - rect3.top;
                    this.mapViewClip.addView(this.locationButton, createFrame);
                    this.locationButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LocationActivity.this.lambda$createView$4(view);
                        }
                    });
                    this.proximityButton = new ImageView(context);
                    Drawable createSimpleSelectorCircleDrawable3 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i15), getThemedColor(i16));
                    if (i < 21) {
                        Drawable mutate5 = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                        mutate5.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
                        CombinedDrawable combinedDrawable4 = new CombinedDrawable(mutate5, createSimpleSelectorCircleDrawable3, 0, 0);
                        combinedDrawable4.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                        drawable3 = combinedDrawable4;
                    } else {
                        StateListAnimator stateListAnimator4 = new StateListAnimator();
                        int[] iArr4 = {android.R.attr.state_pressed};
                        ImageView imageView4 = this.proximityButton;
                        property3 = View.TRANSLATION_Z;
                        stateListAnimator4.addState(iArr4, ObjectAnimator.ofFloat(imageView4, (Property<ImageView, Float>) property3, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        stateListAnimator4.addState(new int[0], ObjectAnimator.ofFloat(this.proximityButton, (Property<ImageView, Float>) property3, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.proximityButton.setStateListAnimator(stateListAnimator4);
                        this.proximityButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.LocationActivity.7
                            @Override // android.view.ViewOutlineProvider
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                            }
                        });
                        drawable3 = createSimpleSelectorCircleDrawable3;
                    }
                    this.proximityButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), mode));
                    this.proximityButton.setBackgroundDrawable(drawable3);
                    this.proximityButton.setScaleType(scaleType);
                    this.proximityButton.setContentDescription(LocaleController.getString(R.string.AccDescrLocationNotify));
                    this.mapViewClip.addView(this.proximityButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 62.0f, 12.0f, 0.0f));
                    this.proximityButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LocationActivity.this.lambda$createView$7(view);
                        }
                    });
                    TLRPC.Chat chat2 = DialogObject.isChatDialog(this.dialogId) ? getMessagesController().getChat(Long.valueOf(-this.dialogId)) : null;
                    messageObject2 = this.messageObject;
                    if (messageObject2 != null || !messageObject2.isLiveLocation() || this.messageObject.isExpiredLiveLocation(getConnectionsManager().getCurrentTime()) || (ChatObject.isChannel(chat2) && !chat2.megagroup)) {
                        this.proximityButton.setVisibility(8);
                    } else {
                        LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(this.dialogId);
                        if (sharingLocationInfo != null && sharingLocationInfo.proximityMeters > 0) {
                            this.proximityButton.setImageResource(R.drawable.msg_location_alert2);
                            HintView2 hintView2 = new HintView2(context, 1);
                            this.hintView = hintView2;
                            hintView2.setLayerType(2, null);
                            this.hintView.setDuration(4000L);
                            this.hintView.setJoint(1.0f, -25.0f);
                            this.hintView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                            this.mapViewClip.addView(this.hintView, LayoutHelper.createFrame(-1, -2.0f, 51, 8.0f, 106.0f, 8.0f, 0.0f));
                            LinearLayout linearLayout = new LinearLayout(context);
                            this.emptyView = linearLayout;
                            linearLayout.setOrientation(1);
                            this.emptyView.setGravity(1);
                            this.emptyView.setPadding(0, AndroidUtilities.dp(160.0f), 0, 0);
                            this.emptyView.setVisibility(8);
                            NestedFrameLayout nestedFrameLayout4 = nestedFrameLayout;
                            nestedFrameLayout4.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
                            this.emptyView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda5
                                @Override // android.view.View.OnTouchListener
                                public final boolean onTouch(View view, MotionEvent motionEvent) {
                                    boolean lambda$createView$8;
                                    lambda$createView$8 = LocationActivity.lambda$createView$8(view, motionEvent);
                                    return lambda$createView$8;
                                }
                            });
                            ImageView imageView5 = new ImageView(context);
                            this.emptyImageView = imageView5;
                            imageView5.setImageResource(R.drawable.location_empty);
                            this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogEmptyImage), mode));
                            this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
                            TextView textView = new TextView(context);
                            this.emptyTitleTextView = textView;
                            int i18 = Theme.key_dialogEmptyText;
                            textView.setTextColor(getThemedColor(i18));
                            this.emptyTitleTextView.setGravity(17);
                            this.emptyTitleTextView.setTypeface(AndroidUtilities.bold());
                            this.emptyTitleTextView.setTextSize(1, 17.0f);
                            this.emptyTitleTextView.setText(LocaleController.getString(R.string.NoPlacesFound));
                            this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
                            TextView textView2 = new TextView(context);
                            this.emptySubtitleTextView = textView2;
                            textView2.setTextColor(getThemedColor(i18));
                            this.emptySubtitleTextView.setGravity(17);
                            this.emptySubtitleTextView.setTextSize(1, 15.0f);
                            this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
                            this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
                            RecyclerListView recyclerListView = new RecyclerListView(context);
                            this.listView = recyclerListView;
                            LocationActivityAdapter locationActivityAdapter2 = new LocationActivityAdapter(context, this.locationType, this.dialogId, false, getResourceProvider(), false, this.fromStories, this.locationType != 8) { // from class: org.telegram.ui.LocationActivity.8
                                private boolean firstSet = true;

                                @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                                protected void onDirectionClick() {
                                    LocationActivity.this.openDirections(null);
                                }

                                @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                                public void setLiveLocations(ArrayList arrayList) {
                                    int i19;
                                    if (LocationActivity.this.messageObject != null && LocationActivity.this.messageObject.isLiveLocation()) {
                                        if (arrayList != null) {
                                            i19 = 0;
                                            for (int i20 = 0; i20 < arrayList.size(); i20++) {
                                                LiveLocation liveLocation = (LiveLocation) arrayList.get(i20);
                                                if (liveLocation != null && !UserObject.isUserSelf(liveLocation.user)) {
                                                    i19++;
                                                }
                                            }
                                        } else {
                                            i19 = 0;
                                        }
                                        if (this.firstSet && i19 == 1) {
                                            LocationActivity.this.selectedMarkerId = ((LiveLocation) arrayList.get(0)).id;
                                        }
                                        this.firstSet = false;
                                        LocationActivity.this.otherItem.setVisibility(i19 != 1 ? 8 : 0);
                                    }
                                    super.setLiveLocations(arrayList);
                                }
                            };
                            this.adapter = locationActivityAdapter2;
                            recyclerListView.setAdapter(locationActivityAdapter2);
                            RecyclerListView recyclerListView2 = this.listView;
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
                            this.layoutManager = linearLayoutManager;
                            recyclerListView2.setLayoutManager(linearLayoutManager);
                            if (this.searchStoriesArea == null) {
                                this.sharedMediaHeader = new GraySectionCell(context, this.resourceProvider);
                                nestedFrameLayout2 = nestedFrameLayout4;
                                chat = chat2;
                                actionBarMenu2 = actionBarMenu;
                                i6 = 1;
                                layoutParams = layoutParams2;
                                SharedMediaLayout sharedMediaLayout = new SharedMediaLayout(context, 0L, new SharedMediaLayout.SharedMediaPreloader(this), 0, null, null, null, 8, this, new SharedMediaLayout.Delegate() { // from class: org.telegram.ui.LocationActivity.9
                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public boolean canSearchMembers() {
                                        return false;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public TLRPC.Chat getCurrentChat() {
                                        return null;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public RecyclerListView getListView() {
                                        return LocationActivity.this.listView;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public boolean isFragmentOpened() {
                                        return true;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public boolean onMemberClick(TLRPC.ChatParticipant chatParticipant, boolean z3, boolean z4, View view) {
                                        return false;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public void scrollToSharedMedia() {
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
                                    public void updateSelectedMediaTabText() {
                                        int storiesCount = LocationActivity.this.sharedMediaLayout == null ? 0 : LocationActivity.this.sharedMediaLayout.getStoriesCount(8);
                                        LocationActivity.this.sharedMediaHeader.setText(LocaleController.formatPluralString("LocationStories", storiesCount, new Object[0]));
                                        if (LocationActivity.this.adapter.setSharedMediaLayoutVisible(storiesCount > 0)) {
                                            LocationActivity.this.listView.smoothScrollBy(0, AndroidUtilities.dp(200.0f));
                                        }
                                    }
                                }, 0, getResourceProvider()) { // from class: org.telegram.ui.LocationActivity.10
                                    @Override // org.telegram.ui.Components.SharedMediaLayout
                                    protected boolean customTabs() {
                                        return true;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout
                                    public TL_stories.MediaArea getStoriesArea() {
                                        return LocationActivity.this.searchStoriesArea;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout
                                    public int mediaPageTopMargin() {
                                        return 32;
                                    }

                                    @Override // org.telegram.ui.Components.SharedMediaLayout
                                    public int overrideColumnsCount() {
                                        return 3;
                                    }
                                };
                                this.sharedMediaLayout = sharedMediaLayout;
                                sharedMediaLayout.setBackgroundColor(getThemedColor(i10));
                                i4 = -1;
                                this.sharedMediaLayout.addView(this.sharedMediaHeader, LayoutHelper.createFrame(-1, 32, 55));
                                this.adapter.setSharedMediaLayout(this.sharedMediaLayout);
                                i5 = 2;
                                this.listView.setOverScrollMode(2);
                                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
                                z2 = false;
                                defaultItemAnimator.setSupportsChangeAnimations(false);
                                defaultItemAnimator.setDelayAnimations(false);
                                defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                                defaultItemAnimator.setDurations(350L);
                                this.listView.setItemAnimator(defaultItemAnimator);
                            } else {
                                chat = chat2;
                                layoutParams = layoutParams2;
                                nestedFrameLayout2 = nestedFrameLayout4;
                                actionBarMenu2 = actionBarMenu;
                                i4 = -1;
                                i5 = 2;
                                z2 = false;
                                i6 = 1;
                            }
                            this.adapter.setMyLocationDenied(this.locationDenied, z2);
                            this.adapter.setUpdateRunnable(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LocationActivity.this.lambda$createView$9();
                                }
                            });
                            this.listView.setVerticalScrollBarEnabled(z2);
                            NestedFrameLayout nestedFrameLayout5 = nestedFrameLayout2;
                            nestedFrameLayout5.addView(this.listView, LayoutHelper.createFrame(i4, i4, 51));
                            messageObject3 = this.messageObject;
                            if (messageObject3 != null && (message = messageObject3.messageOwner) != null && (messageMedia = message.media) != null && !TextUtils.isEmpty(messageMedia.address)) {
                                this.adapter.setAddressNameOverride(this.messageObject.messageOwner.media.address);
                            }
                            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LocationActivity.11
                                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                                public void onScrollStateChanged(RecyclerView recyclerView, int i19) {
                                    LocationActivity.this.scrolling = i19 != 0;
                                    if (LocationActivity.this.scrolling || LocationActivity.this.forceUpdate == null) {
                                        return;
                                    }
                                    LocationActivity.this.forceUpdate = null;
                                }

                                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                                public void onScrolled(RecyclerView recyclerView, int i19, int i20) {
                                    LocationActivity.this.updateClipView(false);
                                    if (LocationActivity.this.forceUpdate != null) {
                                        LocationActivity.access$3116(LocationActivity.this, i20);
                                    }
                                }
                            });
                            ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(z2);
                            this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda7
                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                                public final boolean onItemClick(View view, int i19) {
                                    boolean lambda$createView$11;
                                    lambda$createView$11 = LocationActivity.this.lambda$createView$11(context, view, i19);
                                    return lambda$createView$11;
                                }
                            });
                            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda8
                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                                public final void onItemClick(View view, int i19) {
                                    LocationActivity.this.lambda$createView$17(view, i19);
                                }
                            });
                            this.adapter.setDelegate(this.dialogId, new BaseLocationAdapter.BaseLocationAdapterDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda9
                                @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
                                public final void didLoadSearchResult(ArrayList arrayList) {
                                    LocationActivity.this.updatePlacesMarkers(arrayList);
                                }
                            });
                            this.adapter.setOverScrollHeight(this.overScrollHeight);
                            nestedFrameLayout5.addView(this.mapViewClip, LayoutHelper.createFrame(i4, i4, 51));
                            IMapsProvider.IMapView onCreateMapView = ApplicationLoader.getMapsProvider().onCreateMapView(context);
                            this.mapView = onCreateMapView;
                            onCreateMapView.getView().setAlpha(0.0f);
                            this.mapView.setOnDispatchTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda10
                                @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                                public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                                    boolean lambda$createView$18;
                                    lambda$createView$18 = LocationActivity.this.lambda$createView$18(motionEvent, iCallableMethod);
                                    return lambda$createView$18;
                                }
                            });
                            this.mapView.setOnInterceptTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda11
                                @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                                public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                                    boolean lambda$createView$19;
                                    lambda$createView$19 = LocationActivity.this.lambda$createView$19(motionEvent, iCallableMethod);
                                    return lambda$createView$19;
                                }
                            });
                            this.mapView.setOnLayoutListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda12
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LocationActivity.this.lambda$createView$21();
                                }
                            });
                            final IMapsProvider.IMapView iMapView = this.mapView;
                            new Thread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda13
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LocationActivity.this.lambda$createView$24(iMapView);
                                }
                            }).start();
                            messageObject4 = this.messageObject;
                            if (messageObject4 == null || this.chatLocation != null) {
                                r14 = 1;
                                r14 = 1;
                                r14 = 1;
                                r14 = 1;
                                if ((messageObject4 != null && !messageObject4.isLiveLocation()) || this.chatLocation != null) {
                                    tL_channelLocation = this.chatLocation;
                                    if (tL_channelLocation != null) {
                                        this.adapter.setChatLocation(tL_channelLocation);
                                    } else {
                                        MessageObject messageObject7 = this.messageObject;
                                        if (messageObject7 != null) {
                                            this.adapter.setMessageObject(messageObject7);
                                        }
                                    }
                                }
                            } else {
                                TLRPC.Chat chat3 = chat;
                                if (chat3 == null || this.locationType != 4) {
                                    i8 = 49;
                                    j = 0;
                                } else {
                                    j = 0;
                                    if (this.dialogId != 0) {
                                        FrameLayout frameLayout2 = new FrameLayout(context);
                                        frameLayout2.setBackgroundResource(R.drawable.livepin);
                                        i8 = 49;
                                        this.mapViewClip.addView(frameLayout2, LayoutHelper.createFrame(62, 76, 49));
                                        BackupImageView backupImageView = new BackupImageView(context);
                                        backupImageView.setRoundRadius(AndroidUtilities.dp(26.0f));
                                        backupImageView.setForUserOrChat(chat3, new AvatarDrawable(chat3));
                                        frameLayout2.addView(backupImageView, LayoutHelper.createFrame(52, 52.0f, 51, 5.0f, 5.0f, 0.0f, 0.0f));
                                        this.markerImageView = frameLayout2;
                                        frameLayout2.setTag(Integer.valueOf(i6));
                                    } else {
                                        i8 = 49;
                                    }
                                }
                                if (this.markerImageView == null) {
                                    ImageView imageView6 = new ImageView(context);
                                    imageView6.setImageResource(R.drawable.map_pin2);
                                    this.mapViewClip.addView(imageView6, LayoutHelper.createFrame(28, 48, i8));
                                    this.markerImageView = imageView6;
                                }
                                RecyclerListView recyclerListView3 = new RecyclerListView(context);
                                this.searchListView = recyclerListView3;
                                recyclerListView3.setVisibility(8);
                                r14 = 1;
                                this.searchListView.setLayoutManager(new LinearLayoutManager(context, 1, z2));
                                LocationActivitySearchAdapter locationActivitySearchAdapter2 = new LocationActivitySearchAdapter(context, getResourceProvider(), false, this.locationType == 8) { // from class: org.telegram.ui.LocationActivity.13
                                    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                                    public void notifyDataSetChanged() {
                                        if (LocationActivity.this.searchItem != null) {
                                            LocationActivity.this.searchItem.setShowSearchProgress(LocationActivity.this.searchAdapter.isSearching());
                                        }
                                        if (LocationActivity.this.emptySubtitleTextView != null) {
                                            LocationActivity.this.emptySubtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("NoPlacesFoundInfo", R.string.NoPlacesFoundInfo, LocationActivity.this.searchAdapter.getLastSearchString())));
                                        }
                                        super.notifyDataSetChanged();
                                    }
                                };
                                this.searchAdapter = locationActivitySearchAdapter2;
                                locationActivitySearchAdapter2.setDelegate(j, new BaseLocationAdapter.BaseLocationAdapterDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda14
                                    @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
                                    public final void didLoadSearchResult(ArrayList arrayList) {
                                        LocationActivity.this.lambda$createView$25(arrayList);
                                    }
                                });
                                nestedFrameLayout5.addView(this.searchListView, LayoutHelper.createFrame(i4, i4, 51));
                                this.searchListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LocationActivity.14
                                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                                    public void onScrollStateChanged(RecyclerView recyclerView, int i19) {
                                        if (i19 == 1 && LocationActivity.this.searching && LocationActivity.this.searchWas) {
                                            AndroidUtilities.hideKeyboard(LocationActivity.this.getParentActivity().getCurrentFocus());
                                        }
                                    }
                                });
                                final ActionBarMenu actionBarMenu3 = actionBarMenu2;
                                this.searchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda15
                                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                                    public final void onItemClick(View view, int i19) {
                                        LocationActivity.this.lambda$createView$27(actionBarMenu3, view, i19);
                                    }
                                });
                            }
                            messageObject5 = this.messageObject;
                            if (messageObject5 != null && this.locationType == 6) {
                                this.adapter.setMessageObject(messageObject5);
                            }
                            for (i7 = 0; i7 < i5; i7 += r14) {
                                this.undoView[i7] = new UndoView(context);
                                this.undoView[i7].setAdditionalTranslationY(AndroidUtilities.dp(10.0f));
                                if (Build.VERSION.SDK_INT >= 21) {
                                    this.undoView[i7].setTranslationZ(AndroidUtilities.dp(5.0f));
                                }
                                this.mapViewClip.addView(this.undoView[i7], LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                            }
                            View view = new View(context) { // from class: org.telegram.ui.LocationActivity.15
                                private RectF rect = new RectF();

                                @Override // android.view.View
                                protected void onDraw(Canvas canvas) {
                                    LocationActivity.this.shadowDrawable.setBounds(-rect3.left, 0, getMeasuredWidth() + rect3.right, getMeasuredHeight());
                                    LocationActivity.this.shadowDrawable.draw(canvas);
                                    if (LocationActivity.this.locationType == 0 || LocationActivity.this.locationType == 1) {
                                        int dp2 = AndroidUtilities.dp(36.0f);
                                        this.rect.set((getMeasuredWidth() - dp2) / 2, rect3.top + AndroidUtilities.dp(10.0f), (getMeasuredWidth() + dp2) / 2, r1 + AndroidUtilities.dp(4.0f));
                                        int themedColor3 = LocationActivity.this.getThemedColor(Theme.key_sheet_scrollUp);
                                        Color.alpha(themedColor3);
                                        Theme.dialogs_onlineCirclePaint.setColor(themedColor3);
                                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                                    }
                                }
                            };
                            this.shadow = view;
                            if (Build.VERSION.SDK_INT >= 21) {
                                view.setTranslationZ(AndroidUtilities.dp(6.0f));
                            }
                            this.mapViewClip.addView(this.shadow, layoutParams);
                            if (this.messageObject == null && this.chatLocation == null && this.initialLocation != null) {
                                this.userLocationMoved = r14;
                                ImageView imageView7 = this.locationButton;
                                int i19 = Theme.key_location_actionIcon;
                                imageView7.setColorFilter(new PorterDuffColorFilter(getThemedColor(i19), PorterDuff.Mode.MULTIPLY));
                                this.locationButton.setTag(Integer.valueOf(i19));
                            }
                            nestedFrameLayout5.addView(this.actionBar);
                            updateEmptyView();
                            return this.fragmentView;
                        }
                        if (DialogObject.isUserDialog(this.dialogId) && this.messageObject.getFromChatId() == getUserConfig().getClientUserId()) {
                            this.proximityButton.setVisibility(4);
                            this.proximityButton.setAlpha(0.0f);
                            this.proximityButton.setScaleX(0.4f);
                            this.proximityButton.setScaleY(0.4f);
                        }
                    }
                    this.proximityButton.setImageResource(R.drawable.msg_location_alert);
                    HintView2 hintView22 = new HintView2(context, 1);
                    this.hintView = hintView22;
                    hintView22.setLayerType(2, null);
                    this.hintView.setDuration(4000L);
                    this.hintView.setJoint(1.0f, -25.0f);
                    this.hintView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                    this.mapViewClip.addView(this.hintView, LayoutHelper.createFrame(-1, -2.0f, 51, 8.0f, 106.0f, 8.0f, 0.0f));
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    this.emptyView = linearLayout2;
                    linearLayout2.setOrientation(1);
                    this.emptyView.setGravity(1);
                    this.emptyView.setPadding(0, AndroidUtilities.dp(160.0f), 0, 0);
                    this.emptyView.setVisibility(8);
                    NestedFrameLayout nestedFrameLayout42 = nestedFrameLayout;
                    nestedFrameLayout42.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
                    this.emptyView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnTouchListener
                        public final boolean onTouch(View view2, MotionEvent motionEvent) {
                            boolean lambda$createView$8;
                            lambda$createView$8 = LocationActivity.lambda$createView$8(view2, motionEvent);
                            return lambda$createView$8;
                        }
                    });
                    ImageView imageView52 = new ImageView(context);
                    this.emptyImageView = imageView52;
                    imageView52.setImageResource(R.drawable.location_empty);
                    this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogEmptyImage), mode));
                    this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
                    TextView textView3 = new TextView(context);
                    this.emptyTitleTextView = textView3;
                    int i182 = Theme.key_dialogEmptyText;
                    textView3.setTextColor(getThemedColor(i182));
                    this.emptyTitleTextView.setGravity(17);
                    this.emptyTitleTextView.setTypeface(AndroidUtilities.bold());
                    this.emptyTitleTextView.setTextSize(1, 17.0f);
                    this.emptyTitleTextView.setText(LocaleController.getString(R.string.NoPlacesFound));
                    this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
                    TextView textView22 = new TextView(context);
                    this.emptySubtitleTextView = textView22;
                    textView22.setTextColor(getThemedColor(i182));
                    this.emptySubtitleTextView.setGravity(17);
                    this.emptySubtitleTextView.setTextSize(1, 15.0f);
                    this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
                    this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
                    RecyclerListView recyclerListView4 = new RecyclerListView(context);
                    this.listView = recyclerListView4;
                    LocationActivityAdapter locationActivityAdapter22 = new LocationActivityAdapter(context, this.locationType, this.dialogId, false, getResourceProvider(), false, this.fromStories, this.locationType != 8) { // from class: org.telegram.ui.LocationActivity.8
                        private boolean firstSet = true;

                        @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                        protected void onDirectionClick() {
                            LocationActivity.this.openDirections(null);
                        }

                        @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                        public void setLiveLocations(ArrayList arrayList) {
                            int i192;
                            if (LocationActivity.this.messageObject != null && LocationActivity.this.messageObject.isLiveLocation()) {
                                if (arrayList != null) {
                                    i192 = 0;
                                    for (int i20 = 0; i20 < arrayList.size(); i20++) {
                                        LiveLocation liveLocation = (LiveLocation) arrayList.get(i20);
                                        if (liveLocation != null && !UserObject.isUserSelf(liveLocation.user)) {
                                            i192++;
                                        }
                                    }
                                } else {
                                    i192 = 0;
                                }
                                if (this.firstSet && i192 == 1) {
                                    LocationActivity.this.selectedMarkerId = ((LiveLocation) arrayList.get(0)).id;
                                }
                                this.firstSet = false;
                                LocationActivity.this.otherItem.setVisibility(i192 != 1 ? 8 : 0);
                            }
                            super.setLiveLocations(arrayList);
                        }
                    };
                    this.adapter = locationActivityAdapter22;
                    recyclerListView4.setAdapter(locationActivityAdapter22);
                    RecyclerListView recyclerListView22 = this.listView;
                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, 1, false);
                    this.layoutManager = linearLayoutManager2;
                    recyclerListView22.setLayoutManager(linearLayoutManager2);
                    if (this.searchStoriesArea == null) {
                    }
                    this.adapter.setMyLocationDenied(this.locationDenied, z2);
                    this.adapter.setUpdateRunnable(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationActivity.this.lambda$createView$9();
                        }
                    });
                    this.listView.setVerticalScrollBarEnabled(z2);
                    NestedFrameLayout nestedFrameLayout52 = nestedFrameLayout2;
                    nestedFrameLayout52.addView(this.listView, LayoutHelper.createFrame(i4, i4, 51));
                    messageObject3 = this.messageObject;
                    if (messageObject3 != null) {
                        this.adapter.setAddressNameOverride(this.messageObject.messageOwner.media.address);
                    }
                    this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LocationActivity.11
                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrollStateChanged(RecyclerView recyclerView, int i192) {
                            LocationActivity.this.scrolling = i192 != 0;
                            if (LocationActivity.this.scrolling || LocationActivity.this.forceUpdate == null) {
                                return;
                            }
                            LocationActivity.this.forceUpdate = null;
                        }

                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrolled(RecyclerView recyclerView, int i192, int i20) {
                            LocationActivity.this.updateClipView(false);
                            if (LocationActivity.this.forceUpdate != null) {
                                LocationActivity.access$3116(LocationActivity.this, i20);
                            }
                        }
                    });
                    ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(z2);
                    this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda7
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                        public final boolean onItemClick(View view2, int i192) {
                            boolean lambda$createView$11;
                            lambda$createView$11 = LocationActivity.this.lambda$createView$11(context, view2, i192);
                            return lambda$createView$11;
                        }
                    });
                    this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda8
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view2, int i192) {
                            LocationActivity.this.lambda$createView$17(view2, i192);
                        }
                    });
                    this.adapter.setDelegate(this.dialogId, new BaseLocationAdapter.BaseLocationAdapterDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda9
                        @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
                        public final void didLoadSearchResult(ArrayList arrayList) {
                            LocationActivity.this.updatePlacesMarkers(arrayList);
                        }
                    });
                    this.adapter.setOverScrollHeight(this.overScrollHeight);
                    nestedFrameLayout52.addView(this.mapViewClip, LayoutHelper.createFrame(i4, i4, 51));
                    IMapsProvider.IMapView onCreateMapView2 = ApplicationLoader.getMapsProvider().onCreateMapView(context);
                    this.mapView = onCreateMapView2;
                    onCreateMapView2.getView().setAlpha(0.0f);
                    this.mapView.setOnDispatchTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda10
                        @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                        public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                            boolean lambda$createView$18;
                            lambda$createView$18 = LocationActivity.this.lambda$createView$18(motionEvent, iCallableMethod);
                            return lambda$createView$18;
                        }
                    });
                    this.mapView.setOnInterceptTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda11
                        @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                        public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                            boolean lambda$createView$19;
                            lambda$createView$19 = LocationActivity.this.lambda$createView$19(motionEvent, iCallableMethod);
                            return lambda$createView$19;
                        }
                    });
                    this.mapView.setOnLayoutListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationActivity.this.lambda$createView$21();
                        }
                    });
                    final IMapsProvider.IMapView iMapView2 = this.mapView;
                    new Thread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationActivity.this.lambda$createView$24(iMapView2);
                        }
                    }).start();
                    messageObject4 = this.messageObject;
                    if (messageObject4 == null) {
                    }
                    r14 = 1;
                    r14 = 1;
                    r14 = 1;
                    r14 = 1;
                    if (messageObject4 != null) {
                        tL_channelLocation = this.chatLocation;
                        if (tL_channelLocation != null) {
                        }
                        messageObject5 = this.messageObject;
                        if (messageObject5 != null) {
                            this.adapter.setMessageObject(messageObject5);
                        }
                        while (i7 < i5) {
                        }
                        View view2 = new View(context) { // from class: org.telegram.ui.LocationActivity.15
                            private RectF rect = new RectF();

                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                LocationActivity.this.shadowDrawable.setBounds(-rect3.left, 0, getMeasuredWidth() + rect3.right, getMeasuredHeight());
                                LocationActivity.this.shadowDrawable.draw(canvas);
                                if (LocationActivity.this.locationType == 0 || LocationActivity.this.locationType == 1) {
                                    int dp2 = AndroidUtilities.dp(36.0f);
                                    this.rect.set((getMeasuredWidth() - dp2) / 2, rect3.top + AndroidUtilities.dp(10.0f), (getMeasuredWidth() + dp2) / 2, r1 + AndroidUtilities.dp(4.0f));
                                    int themedColor3 = LocationActivity.this.getThemedColor(Theme.key_sheet_scrollUp);
                                    Color.alpha(themedColor3);
                                    Theme.dialogs_onlineCirclePaint.setColor(themedColor3);
                                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                                }
                            }
                        };
                        this.shadow = view2;
                        if (Build.VERSION.SDK_INT >= 21) {
                        }
                        this.mapViewClip.addView(this.shadow, layoutParams);
                        if (this.messageObject == null) {
                            this.userLocationMoved = r14;
                            ImageView imageView72 = this.locationButton;
                            int i192 = Theme.key_location_actionIcon;
                            imageView72.setColorFilter(new PorterDuffColorFilter(getThemedColor(i192), PorterDuff.Mode.MULTIPLY));
                            this.locationButton.setTag(Integer.valueOf(i192));
                        }
                        nestedFrameLayout52.addView(this.actionBar);
                        updateEmptyView();
                        return this.fragmentView;
                    }
                    tL_channelLocation = this.chatLocation;
                    if (tL_channelLocation != null) {
                    }
                    messageObject5 = this.messageObject;
                    if (messageObject5 != null) {
                    }
                    while (i7 < i5) {
                    }
                    View view22 = new View(context) { // from class: org.telegram.ui.LocationActivity.15
                        private RectF rect = new RectF();

                        @Override // android.view.View
                        protected void onDraw(Canvas canvas) {
                            LocationActivity.this.shadowDrawable.setBounds(-rect3.left, 0, getMeasuredWidth() + rect3.right, getMeasuredHeight());
                            LocationActivity.this.shadowDrawable.draw(canvas);
                            if (LocationActivity.this.locationType == 0 || LocationActivity.this.locationType == 1) {
                                int dp2 = AndroidUtilities.dp(36.0f);
                                this.rect.set((getMeasuredWidth() - dp2) / 2, rect3.top + AndroidUtilities.dp(10.0f), (getMeasuredWidth() + dp2) / 2, r1 + AndroidUtilities.dp(4.0f));
                                int themedColor3 = LocationActivity.this.getThemedColor(Theme.key_sheet_scrollUp);
                                Color.alpha(themedColor3);
                                Theme.dialogs_onlineCirclePaint.setColor(themedColor3);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                            }
                        }
                    };
                    this.shadow = view22;
                    if (Build.VERSION.SDK_INT >= 21) {
                    }
                    this.mapViewClip.addView(this.shadow, layoutParams);
                    if (this.messageObject == null) {
                    }
                    nestedFrameLayout52.addView(this.actionBar);
                    updateEmptyView();
                    return this.fragmentView;
                }
            }
            z = false;
            this.locationDenied = z;
            ActionBar actionBar22 = this.actionBar;
            int i102 = Theme.key_dialogBackground;
            actionBar22.setBackgroundColor(getThemedColor(i102));
            ActionBar actionBar32 = this.actionBar;
            int i112 = Theme.key_dialogTextBlack;
            actionBar32.setTitleColor(getThemedColor(i112));
            this.actionBar.setItemsColor(getThemedColor(i112), false);
            this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_dialogButtonSelector), false);
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            this.actionBar.setAllowOverlayTitle(true);
            if (AndroidUtilities.isTablet()) {
            }
            this.actionBar.setAddToContainer(false);
            this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LocationActivity.1
                @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                public void onItemClick(int i122) {
                    if (i122 == -1) {
                        LocationActivity.this.lambda$onBackPressed$323();
                        return;
                    }
                    if (i122 != 1) {
                        if (i122 == 5) {
                            LocationActivity.this.openShareLiveLocation(false, 0);
                            return;
                        } else {
                            if (i122 == 6) {
                                LocationActivity.this.openDirections(null);
                                return;
                            }
                            return;
                        }
                    }
                    try {
                        double d = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                        double d2 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                        LocationActivity.this.getParentActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:" + d + "," + d2 + "?q=" + d + "," + d2)));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            });
            createMenu = this.actionBar.createMenu();
            if (this.chatLocation != null) {
            }
            NestedFrameLayout nestedFrameLayout32 = new NestedFrameLayout(context);
            this.fragmentView = nestedFrameLayout32;
            nestedFrameLayout32.setBackgroundColor(getThemedColor(i102));
            Drawable mutate6 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            this.shadowDrawable = mutate6;
            int themedColor3 = getThemedColor(i102);
            PorterDuff.Mode mode2 = PorterDuff.Mode.MULTIPLY;
            mutate6.setColorFilter(new PorterDuffColorFilter(themedColor3, mode2));
            Rect rect22 = new Rect();
            this.shadowDrawable.getPadding(rect22);
            int i132 = this.locationType;
            FrameLayout.LayoutParams layoutParams22 = (i132 != 0 || i132 == 1) ? new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(21.0f) + rect22.top) : new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(6.0f) + rect22.top);
            layoutParams22.gravity = 83;
            FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.LocationActivity.3
                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i142, int i152) {
                    super.onMeasure(i142, i152);
                    if (LocationActivity.this.overlayView != null) {
                        LocationActivity.this.overlayView.updatePositions();
                    }
                }
            };
            this.mapViewClip = frameLayout3;
            frameLayout3.setBackgroundDrawable(new MapPlaceholderDrawable(isActiveThemeDark()));
            messageObject = this.messageObject;
            if (messageObject == null) {
            }
            actionBarMenu = createMenu;
            int i142 = Theme.key_location_actionIcon;
            ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, getThemedColor(i142), getResourceProvider());
            this.mapTypeButton = actionBarMenuItem3;
            actionBarMenuItem3.setClickable(true);
            this.mapTypeButton.setSubMenuOpenSide(2);
            this.mapTypeButton.setAdditionalXOffset(AndroidUtilities.dp(10.0f));
            this.mapTypeButton.setAdditionalYOffset(-AndroidUtilities.dp(10.0f));
            this.mapTypeButton.addSubItem(2, R.drawable.msg_map, LocaleController.getString(R.string.Map), getResourceProvider());
            this.mapTypeButton.addSubItem(3, R.drawable.msg_satellite, LocaleController.getString(R.string.Satellite), getResourceProvider());
            this.mapTypeButton.addSubItem(4, R.drawable.msg_hybrid, LocaleController.getString(R.string.Hybrid), getResourceProvider());
            this.mapTypeButton.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
            int dp2 = AndroidUtilities.dp(40.0f);
            int i152 = Theme.key_location_actionBackground;
            int themedColor22 = getThemedColor(i152);
            int i162 = Theme.key_location_actionPressedBackground;
            Drawable createSimpleSelectorCircleDrawable4 = Theme.createSimpleSelectorCircleDrawable(dp2, themedColor22, getThemedColor(i162));
            if (i < 21) {
            }
            this.mapTypeButton.setBackgroundDrawable(drawable);
            this.mapTypeButton.setIcon(R.drawable.msg_map_type);
            this.mapViewClip.addView(this.mapTypeButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 12.0f, 12.0f, 0.0f));
            this.mapTypeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    LocationActivity.this.lambda$createView$2(view3);
                }
            });
            this.mapTypeButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
                public final void onItemClick(int i172) {
                    LocationActivity.this.lambda$createView$3(i172);
                }
            });
            this.locationButton = new ImageView(context);
            Drawable createSimpleSelectorCircleDrawable22 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i152), getThemedColor(i162));
            if (i < 21) {
            }
            this.locationButton.setBackgroundDrawable(drawable2);
            this.locationButton.setImageResource(R.drawable.msg_current_location);
            ImageView imageView22 = this.locationButton;
            ImageView.ScaleType scaleType2 = ImageView.ScaleType.CENTER;
            imageView22.setScaleType(scaleType2);
            ImageView imageView32 = this.locationButton;
            int i172 = Theme.key_location_actionActiveIcon;
            imageView32.setColorFilter(new PorterDuffColorFilter(getThemedColor(i172), mode2));
            this.locationButton.setTag(Integer.valueOf(i172));
            this.locationButton.setContentDescription(LocaleController.getString(R.string.AccDescrMyLocation));
            FrameLayout.LayoutParams createFrame2 = LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 85, 0.0f, 0.0f, 12.0f, 12.0f);
            final Rect rect32 = rect;
            createFrame2.bottomMargin += layoutParams22.height - rect32.top;
            this.mapViewClip.addView(this.locationButton, createFrame2);
            this.locationButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    LocationActivity.this.lambda$createView$4(view3);
                }
            });
            this.proximityButton = new ImageView(context);
            Drawable createSimpleSelectorCircleDrawable32 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i152), getThemedColor(i162));
            if (i < 21) {
            }
            this.proximityButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), mode2));
            this.proximityButton.setBackgroundDrawable(drawable3);
            this.proximityButton.setScaleType(scaleType2);
            this.proximityButton.setContentDescription(LocaleController.getString(R.string.AccDescrLocationNotify));
            this.mapViewClip.addView(this.proximityButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 62.0f, 12.0f, 0.0f));
            this.proximityButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    LocationActivity.this.lambda$createView$7(view3);
                }
            });
            if (DialogObject.isChatDialog(this.dialogId)) {
            }
            messageObject2 = this.messageObject;
            if (messageObject2 != null) {
            }
            this.proximityButton.setVisibility(8);
            this.proximityButton.setImageResource(R.drawable.msg_location_alert);
            HintView2 hintView222 = new HintView2(context, 1);
            this.hintView = hintView222;
            hintView222.setLayerType(2, null);
            this.hintView.setDuration(4000L);
            this.hintView.setJoint(1.0f, -25.0f);
            this.hintView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
            this.mapViewClip.addView(this.hintView, LayoutHelper.createFrame(-1, -2.0f, 51, 8.0f, 106.0f, 8.0f, 0.0f));
            LinearLayout linearLayout22 = new LinearLayout(context);
            this.emptyView = linearLayout22;
            linearLayout22.setOrientation(1);
            this.emptyView.setGravity(1);
            this.emptyView.setPadding(0, AndroidUtilities.dp(160.0f), 0, 0);
            this.emptyView.setVisibility(8);
            NestedFrameLayout nestedFrameLayout422 = nestedFrameLayout;
            nestedFrameLayout422.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
            this.emptyView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view23, MotionEvent motionEvent) {
                    boolean lambda$createView$8;
                    lambda$createView$8 = LocationActivity.lambda$createView$8(view23, motionEvent);
                    return lambda$createView$8;
                }
            });
            ImageView imageView522 = new ImageView(context);
            this.emptyImageView = imageView522;
            imageView522.setImageResource(R.drawable.location_empty);
            this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogEmptyImage), mode2));
            this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
            TextView textView32 = new TextView(context);
            this.emptyTitleTextView = textView32;
            int i1822 = Theme.key_dialogEmptyText;
            textView32.setTextColor(getThemedColor(i1822));
            this.emptyTitleTextView.setGravity(17);
            this.emptyTitleTextView.setTypeface(AndroidUtilities.bold());
            this.emptyTitleTextView.setTextSize(1, 17.0f);
            this.emptyTitleTextView.setText(LocaleController.getString(R.string.NoPlacesFound));
            this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
            TextView textView222 = new TextView(context);
            this.emptySubtitleTextView = textView222;
            textView222.setTextColor(getThemedColor(i1822));
            this.emptySubtitleTextView.setGravity(17);
            this.emptySubtitleTextView.setTextSize(1, 15.0f);
            this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
            this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
            RecyclerListView recyclerListView42 = new RecyclerListView(context);
            this.listView = recyclerListView42;
            LocationActivityAdapter locationActivityAdapter222 = new LocationActivityAdapter(context, this.locationType, this.dialogId, false, getResourceProvider(), false, this.fromStories, this.locationType != 8) { // from class: org.telegram.ui.LocationActivity.8
                private boolean firstSet = true;

                @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                protected void onDirectionClick() {
                    LocationActivity.this.openDirections(null);
                }

                @Override // org.telegram.ui.Adapters.LocationActivityAdapter
                public void setLiveLocations(ArrayList arrayList) {
                    int i1922;
                    if (LocationActivity.this.messageObject != null && LocationActivity.this.messageObject.isLiveLocation()) {
                        if (arrayList != null) {
                            i1922 = 0;
                            for (int i20 = 0; i20 < arrayList.size(); i20++) {
                                LiveLocation liveLocation = (LiveLocation) arrayList.get(i20);
                                if (liveLocation != null && !UserObject.isUserSelf(liveLocation.user)) {
                                    i1922++;
                                }
                            }
                        } else {
                            i1922 = 0;
                        }
                        if (this.firstSet && i1922 == 1) {
                            LocationActivity.this.selectedMarkerId = ((LiveLocation) arrayList.get(0)).id;
                        }
                        this.firstSet = false;
                        LocationActivity.this.otherItem.setVisibility(i1922 != 1 ? 8 : 0);
                    }
                    super.setLiveLocations(arrayList);
                }
            };
            this.adapter = locationActivityAdapter222;
            recyclerListView42.setAdapter(locationActivityAdapter222);
            RecyclerListView recyclerListView222 = this.listView;
            LinearLayoutManager linearLayoutManager22 = new LinearLayoutManager(context, 1, false);
            this.layoutManager = linearLayoutManager22;
            recyclerListView222.setLayoutManager(linearLayoutManager22);
            if (this.searchStoriesArea == null) {
            }
            this.adapter.setMyLocationDenied(this.locationDenied, z2);
            this.adapter.setUpdateRunnable(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$createView$9();
                }
            });
            this.listView.setVerticalScrollBarEnabled(z2);
            NestedFrameLayout nestedFrameLayout522 = nestedFrameLayout2;
            nestedFrameLayout522.addView(this.listView, LayoutHelper.createFrame(i4, i4, 51));
            messageObject3 = this.messageObject;
            if (messageObject3 != null) {
            }
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LocationActivity.11
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i1922) {
                    LocationActivity.this.scrolling = i1922 != 0;
                    if (LocationActivity.this.scrolling || LocationActivity.this.forceUpdate == null) {
                        return;
                    }
                    LocationActivity.this.forceUpdate = null;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i1922, int i20) {
                    LocationActivity.this.updateClipView(false);
                    if (LocationActivity.this.forceUpdate != null) {
                        LocationActivity.access$3116(LocationActivity.this, i20);
                    }
                }
            });
            ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(z2);
            this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda7
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                public final boolean onItemClick(View view23, int i1922) {
                    boolean lambda$createView$11;
                    lambda$createView$11 = LocationActivity.this.lambda$createView$11(context, view23, i1922);
                    return lambda$createView$11;
                }
            });
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda8
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view23, int i1922) {
                    LocationActivity.this.lambda$createView$17(view23, i1922);
                }
            });
            this.adapter.setDelegate(this.dialogId, new BaseLocationAdapter.BaseLocationAdapterDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda9
                @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
                public final void didLoadSearchResult(ArrayList arrayList) {
                    LocationActivity.this.updatePlacesMarkers(arrayList);
                }
            });
            this.adapter.setOverScrollHeight(this.overScrollHeight);
            nestedFrameLayout522.addView(this.mapViewClip, LayoutHelper.createFrame(i4, i4, 51));
            IMapsProvider.IMapView onCreateMapView22 = ApplicationLoader.getMapsProvider().onCreateMapView(context);
            this.mapView = onCreateMapView22;
            onCreateMapView22.getView().setAlpha(0.0f);
            this.mapView.setOnDispatchTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda10
                @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                    boolean lambda$createView$18;
                    lambda$createView$18 = LocationActivity.this.lambda$createView$18(motionEvent, iCallableMethod);
                    return lambda$createView$18;
                }
            });
            this.mapView.setOnInterceptTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda11
                @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
                public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                    boolean lambda$createView$19;
                    lambda$createView$19 = LocationActivity.this.lambda$createView$19(motionEvent, iCallableMethod);
                    return lambda$createView$19;
                }
            });
            this.mapView.setOnLayoutListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$createView$21();
                }
            });
            final IMapsProvider.IMapView iMapView22 = this.mapView;
            new Thread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LocationActivity.this.lambda$createView$24(iMapView22);
                }
            }).start();
            messageObject4 = this.messageObject;
            if (messageObject4 == null) {
            }
            r14 = 1;
            r14 = 1;
            r14 = 1;
            r14 = 1;
            if (messageObject4 != null) {
            }
            tL_channelLocation = this.chatLocation;
            if (tL_channelLocation != null) {
            }
            messageObject5 = this.messageObject;
            if (messageObject5 != null) {
            }
            while (i7 < i5) {
            }
            View view222 = new View(context) { // from class: org.telegram.ui.LocationActivity.15
                private RectF rect = new RectF();

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    LocationActivity.this.shadowDrawable.setBounds(-rect32.left, 0, getMeasuredWidth() + rect32.right, getMeasuredHeight());
                    LocationActivity.this.shadowDrawable.draw(canvas);
                    if (LocationActivity.this.locationType == 0 || LocationActivity.this.locationType == 1) {
                        int dp22 = AndroidUtilities.dp(36.0f);
                        this.rect.set((getMeasuredWidth() - dp22) / 2, rect32.top + AndroidUtilities.dp(10.0f), (getMeasuredWidth() + dp22) / 2, r1 + AndroidUtilities.dp(4.0f));
                        int themedColor32 = LocationActivity.this.getThemedColor(Theme.key_sheet_scrollUp);
                        Color.alpha(themedColor32);
                        Theme.dialogs_onlineCirclePaint.setColor(themedColor32);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                    }
                }
            };
            this.shadow = view222;
            if (Build.VERSION.SDK_INT >= 21) {
            }
            this.mapViewClip.addView(this.shadow, layoutParams);
            if (this.messageObject == null) {
            }
            nestedFrameLayout522.addView(this.actionBar);
            updateEmptyView();
            return this.fragmentView;
        }
        Location location3 = new Location("network");
        this.userLocation = location3;
        location3.setLatitude(this.chatLocation.geo_point.lat);
        location = this.userLocation;
        geoPoint = this.chatLocation.geo_point;
        location.setLongitude(geoPoint._long);
        i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
            }
        }
        z = false;
        this.locationDenied = z;
        ActionBar actionBar222 = this.actionBar;
        int i1022 = Theme.key_dialogBackground;
        actionBar222.setBackgroundColor(getThemedColor(i1022));
        ActionBar actionBar322 = this.actionBar;
        int i1122 = Theme.key_dialogTextBlack;
        actionBar322.setTitleColor(getThemedColor(i1122));
        this.actionBar.setItemsColor(getThemedColor(i1122), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_dialogButtonSelector), false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (AndroidUtilities.isTablet()) {
        }
        this.actionBar.setAddToContainer(false);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LocationActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i122) {
                if (i122 == -1) {
                    LocationActivity.this.lambda$onBackPressed$323();
                    return;
                }
                if (i122 != 1) {
                    if (i122 == 5) {
                        LocationActivity.this.openShareLiveLocation(false, 0);
                        return;
                    } else {
                        if (i122 == 6) {
                            LocationActivity.this.openDirections(null);
                            return;
                        }
                        return;
                    }
                }
                try {
                    double d = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                    double d2 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                    LocationActivity.this.getParentActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:" + d + "," + d2 + "?q=" + d + "," + d2)));
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        });
        createMenu = this.actionBar.createMenu();
        if (this.chatLocation != null) {
        }
        NestedFrameLayout nestedFrameLayout322 = new NestedFrameLayout(context);
        this.fragmentView = nestedFrameLayout322;
        nestedFrameLayout322.setBackgroundColor(getThemedColor(i1022));
        Drawable mutate62 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.shadowDrawable = mutate62;
        int themedColor32 = getThemedColor(i1022);
        PorterDuff.Mode mode22 = PorterDuff.Mode.MULTIPLY;
        mutate62.setColorFilter(new PorterDuffColorFilter(themedColor32, mode22));
        Rect rect222 = new Rect();
        this.shadowDrawable.getPadding(rect222);
        int i1322 = this.locationType;
        FrameLayout.LayoutParams layoutParams222 = (i1322 != 0 || i1322 == 1) ? new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(21.0f) + rect222.top) : new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(6.0f) + rect222.top);
        layoutParams222.gravity = 83;
        FrameLayout frameLayout32 = new FrameLayout(context) { // from class: org.telegram.ui.LocationActivity.3
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i1422, int i1522) {
                super.onMeasure(i1422, i1522);
                if (LocationActivity.this.overlayView != null) {
                    LocationActivity.this.overlayView.updatePositions();
                }
            }
        };
        this.mapViewClip = frameLayout32;
        frameLayout32.setBackgroundDrawable(new MapPlaceholderDrawable(isActiveThemeDark()));
        messageObject = this.messageObject;
        if (messageObject == null) {
        }
        actionBarMenu = createMenu;
        int i1422 = Theme.key_location_actionIcon;
        ActionBarMenuItem actionBarMenuItem32 = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, getThemedColor(i1422), getResourceProvider());
        this.mapTypeButton = actionBarMenuItem32;
        actionBarMenuItem32.setClickable(true);
        this.mapTypeButton.setSubMenuOpenSide(2);
        this.mapTypeButton.setAdditionalXOffset(AndroidUtilities.dp(10.0f));
        this.mapTypeButton.setAdditionalYOffset(-AndroidUtilities.dp(10.0f));
        this.mapTypeButton.addSubItem(2, R.drawable.msg_map, LocaleController.getString(R.string.Map), getResourceProvider());
        this.mapTypeButton.addSubItem(3, R.drawable.msg_satellite, LocaleController.getString(R.string.Satellite), getResourceProvider());
        this.mapTypeButton.addSubItem(4, R.drawable.msg_hybrid, LocaleController.getString(R.string.Hybrid), getResourceProvider());
        this.mapTypeButton.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
        int dp22 = AndroidUtilities.dp(40.0f);
        int i1522 = Theme.key_location_actionBackground;
        int themedColor222 = getThemedColor(i1522);
        int i1622 = Theme.key_location_actionPressedBackground;
        Drawable createSimpleSelectorCircleDrawable42 = Theme.createSimpleSelectorCircleDrawable(dp22, themedColor222, getThemedColor(i1622));
        if (i < 21) {
        }
        this.mapTypeButton.setBackgroundDrawable(drawable);
        this.mapTypeButton.setIcon(R.drawable.msg_map_type);
        this.mapViewClip.addView(this.mapTypeButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 12.0f, 12.0f, 0.0f));
        this.mapTypeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                LocationActivity.this.lambda$createView$2(view3);
            }
        });
        this.mapTypeButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i1722) {
                LocationActivity.this.lambda$createView$3(i1722);
            }
        });
        this.locationButton = new ImageView(context);
        Drawable createSimpleSelectorCircleDrawable222 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i1522), getThemedColor(i1622));
        if (i < 21) {
        }
        this.locationButton.setBackgroundDrawable(drawable2);
        this.locationButton.setImageResource(R.drawable.msg_current_location);
        ImageView imageView222 = this.locationButton;
        ImageView.ScaleType scaleType22 = ImageView.ScaleType.CENTER;
        imageView222.setScaleType(scaleType22);
        ImageView imageView322 = this.locationButton;
        int i1722 = Theme.key_location_actionActiveIcon;
        imageView322.setColorFilter(new PorterDuffColorFilter(getThemedColor(i1722), mode22));
        this.locationButton.setTag(Integer.valueOf(i1722));
        this.locationButton.setContentDescription(LocaleController.getString(R.string.AccDescrMyLocation));
        FrameLayout.LayoutParams createFrame22 = LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 85, 0.0f, 0.0f, 12.0f, 12.0f);
        final Rect rect322 = rect;
        createFrame22.bottomMargin += layoutParams222.height - rect322.top;
        this.mapViewClip.addView(this.locationButton, createFrame22);
        this.locationButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                LocationActivity.this.lambda$createView$4(view3);
            }
        });
        this.proximityButton = new ImageView(context);
        Drawable createSimpleSelectorCircleDrawable322 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(40.0f), getThemedColor(i1522), getThemedColor(i1622));
        if (i < 21) {
        }
        this.proximityButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), mode22));
        this.proximityButton.setBackgroundDrawable(drawable3);
        this.proximityButton.setScaleType(scaleType22);
        this.proximityButton.setContentDescription(LocaleController.getString(R.string.AccDescrLocationNotify));
        this.mapViewClip.addView(this.proximityButton, LayoutHelper.createFrame(i >= 21 ? 40 : 44, i >= 21 ? 40.0f : 44.0f, 53, 0.0f, 62.0f, 12.0f, 0.0f));
        this.proximityButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                LocationActivity.this.lambda$createView$7(view3);
            }
        });
        if (DialogObject.isChatDialog(this.dialogId)) {
        }
        messageObject2 = this.messageObject;
        if (messageObject2 != null) {
        }
        this.proximityButton.setVisibility(8);
        this.proximityButton.setImageResource(R.drawable.msg_location_alert);
        HintView2 hintView2222 = new HintView2(context, 1);
        this.hintView = hintView2222;
        hintView2222.setLayerType(2, null);
        this.hintView.setDuration(4000L);
        this.hintView.setJoint(1.0f, -25.0f);
        this.hintView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
        this.mapViewClip.addView(this.hintView, LayoutHelper.createFrame(-1, -2.0f, 51, 8.0f, 106.0f, 8.0f, 0.0f));
        LinearLayout linearLayout222 = new LinearLayout(context);
        this.emptyView = linearLayout222;
        linearLayout222.setOrientation(1);
        this.emptyView.setGravity(1);
        this.emptyView.setPadding(0, AndroidUtilities.dp(160.0f), 0, 0);
        this.emptyView.setVisibility(8);
        NestedFrameLayout nestedFrameLayout4222 = nestedFrameLayout;
        nestedFrameLayout4222.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view23, MotionEvent motionEvent) {
                boolean lambda$createView$8;
                lambda$createView$8 = LocationActivity.lambda$createView$8(view23, motionEvent);
                return lambda$createView$8;
            }
        });
        ImageView imageView5222 = new ImageView(context);
        this.emptyImageView = imageView5222;
        imageView5222.setImageResource(R.drawable.location_empty);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogEmptyImage), mode22));
        this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView322 = new TextView(context);
        this.emptyTitleTextView = textView322;
        int i18222 = Theme.key_dialogEmptyText;
        textView322.setTextColor(getThemedColor(i18222));
        this.emptyTitleTextView.setGravity(17);
        this.emptyTitleTextView.setTypeface(AndroidUtilities.bold());
        this.emptyTitleTextView.setTextSize(1, 17.0f);
        this.emptyTitleTextView.setText(LocaleController.getString(R.string.NoPlacesFound));
        this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
        TextView textView2222 = new TextView(context);
        this.emptySubtitleTextView = textView2222;
        textView2222.setTextColor(getThemedColor(i18222));
        this.emptySubtitleTextView.setGravity(17);
        this.emptySubtitleTextView.setTextSize(1, 15.0f);
        this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
        RecyclerListView recyclerListView422 = new RecyclerListView(context);
        this.listView = recyclerListView422;
        LocationActivityAdapter locationActivityAdapter2222 = new LocationActivityAdapter(context, this.locationType, this.dialogId, false, getResourceProvider(), false, this.fromStories, this.locationType != 8) { // from class: org.telegram.ui.LocationActivity.8
            private boolean firstSet = true;

            @Override // org.telegram.ui.Adapters.LocationActivityAdapter
            protected void onDirectionClick() {
                LocationActivity.this.openDirections(null);
            }

            @Override // org.telegram.ui.Adapters.LocationActivityAdapter
            public void setLiveLocations(ArrayList arrayList) {
                int i1922;
                if (LocationActivity.this.messageObject != null && LocationActivity.this.messageObject.isLiveLocation()) {
                    if (arrayList != null) {
                        i1922 = 0;
                        for (int i20 = 0; i20 < arrayList.size(); i20++) {
                            LiveLocation liveLocation = (LiveLocation) arrayList.get(i20);
                            if (liveLocation != null && !UserObject.isUserSelf(liveLocation.user)) {
                                i1922++;
                            }
                        }
                    } else {
                        i1922 = 0;
                    }
                    if (this.firstSet && i1922 == 1) {
                        LocationActivity.this.selectedMarkerId = ((LiveLocation) arrayList.get(0)).id;
                    }
                    this.firstSet = false;
                    LocationActivity.this.otherItem.setVisibility(i1922 != 1 ? 8 : 0);
                }
                super.setLiveLocations(arrayList);
            }
        };
        this.adapter = locationActivityAdapter2222;
        recyclerListView422.setAdapter(locationActivityAdapter2222);
        RecyclerListView recyclerListView2222 = this.listView;
        LinearLayoutManager linearLayoutManager222 = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager222;
        recyclerListView2222.setLayoutManager(linearLayoutManager222);
        if (this.searchStoriesArea == null) {
        }
        this.adapter.setMyLocationDenied(this.locationDenied, z2);
        this.adapter.setUpdateRunnable(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$9();
            }
        });
        this.listView.setVerticalScrollBarEnabled(z2);
        NestedFrameLayout nestedFrameLayout5222 = nestedFrameLayout2;
        nestedFrameLayout5222.addView(this.listView, LayoutHelper.createFrame(i4, i4, 51));
        messageObject3 = this.messageObject;
        if (messageObject3 != null) {
        }
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LocationActivity.11
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i1922) {
                LocationActivity.this.scrolling = i1922 != 0;
                if (LocationActivity.this.scrolling || LocationActivity.this.forceUpdate == null) {
                    return;
                }
                LocationActivity.this.forceUpdate = null;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i1922, int i20) {
                LocationActivity.this.updateClipView(false);
                if (LocationActivity.this.forceUpdate != null) {
                    LocationActivity.access$3116(LocationActivity.this, i20);
                }
            }
        });
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(z2);
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view23, int i1922) {
                boolean lambda$createView$11;
                lambda$createView$11 = LocationActivity.this.lambda$createView$11(context, view23, i1922);
                return lambda$createView$11;
            }
        });
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view23, int i1922) {
                LocationActivity.this.lambda$createView$17(view23, i1922);
            }
        });
        this.adapter.setDelegate(this.dialogId, new BaseLocationAdapter.BaseLocationAdapterDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
            public final void didLoadSearchResult(ArrayList arrayList) {
                LocationActivity.this.updatePlacesMarkers(arrayList);
            }
        });
        this.adapter.setOverScrollHeight(this.overScrollHeight);
        nestedFrameLayout5222.addView(this.mapViewClip, LayoutHelper.createFrame(i4, i4, 51));
        IMapsProvider.IMapView onCreateMapView222 = ApplicationLoader.getMapsProvider().onCreateMapView(context);
        this.mapView = onCreateMapView222;
        onCreateMapView222.getView().setAlpha(0.0f);
        this.mapView.setOnDispatchTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
            public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                boolean lambda$createView$18;
                lambda$createView$18 = LocationActivity.this.lambda$createView$18(motionEvent, iCallableMethod);
                return lambda$createView$18;
            }
        });
        this.mapView.setOnInterceptTouchEventInterceptor(new IMapsProvider.ITouchInterceptor() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.IMapsProvider.ITouchInterceptor
            public final boolean onInterceptTouchEvent(MotionEvent motionEvent, IMapsProvider.ICallableMethod iCallableMethod) {
                boolean lambda$createView$19;
                lambda$createView$19 = LocationActivity.this.lambda$createView$19(motionEvent, iCallableMethod);
                return lambda$createView$19;
            }
        });
        this.mapView.setOnLayoutListener(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$21();
            }
        });
        final IMapsProvider.IMapView iMapView222 = this.mapView;
        new Thread(new Runnable() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                LocationActivity.this.lambda$createView$24(iMapView222);
            }
        }).start();
        messageObject4 = this.messageObject;
        if (messageObject4 == null) {
        }
        r14 = 1;
        r14 = 1;
        r14 = 1;
        r14 = 1;
        if (messageObject4 != null) {
        }
        tL_channelLocation = this.chatLocation;
        if (tL_channelLocation != null) {
        }
        messageObject5 = this.messageObject;
        if (messageObject5 != null) {
        }
        while (i7 < i5) {
        }
        View view2222 = new View(context) { // from class: org.telegram.ui.LocationActivity.15
            private RectF rect = new RectF();

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                LocationActivity.this.shadowDrawable.setBounds(-rect322.left, 0, getMeasuredWidth() + rect322.right, getMeasuredHeight());
                LocationActivity.this.shadowDrawable.draw(canvas);
                if (LocationActivity.this.locationType == 0 || LocationActivity.this.locationType == 1) {
                    int dp222 = AndroidUtilities.dp(36.0f);
                    this.rect.set((getMeasuredWidth() - dp222) / 2, rect322.top + AndroidUtilities.dp(10.0f), (getMeasuredWidth() + dp222) / 2, r1 + AndroidUtilities.dp(4.0f));
                    int themedColor322 = LocationActivity.this.getThemedColor(Theme.key_sheet_scrollUp);
                    Color.alpha(themedColor322);
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor322);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                }
            }
        };
        this.shadow = view2222;
        if (Build.VERSION.SDK_INT >= 21) {
        }
        this.mapViewClip.addView(this.shadow, layoutParams);
        if (this.messageObject == null) {
        }
        nestedFrameLayout5222.addView(this.actionBar);
        updateEmptyView();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        LocationActivityAdapter locationActivityAdapter;
        LiveLocation liveLocation;
        LocationActivityAdapter locationActivityAdapter2;
        if (i == NotificationCenter.closeChats) {
            removeSelfFromStack(true);
            return;
        }
        if (i == NotificationCenter.locationPermissionGranted) {
            this.locationDenied = false;
            LocationActivityAdapter locationActivityAdapter3 = this.adapter;
            if (locationActivityAdapter3 != null) {
                locationActivityAdapter3.setMyLocationDenied(false, false);
            }
            IMapsProvider.IMap iMap = this.map;
            if (iMap != null) {
                try {
                    iMap.setMyLocationEnabled(true);
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
            return;
        }
        if (i == NotificationCenter.locationPermissionDenied) {
            this.locationDenied = true;
            LocationActivityAdapter locationActivityAdapter4 = this.adapter;
            if (locationActivityAdapter4 != null) {
                locationActivityAdapter4.setMyLocationDenied(true, false);
                return;
            }
            return;
        }
        if (i == NotificationCenter.liveLocationsChanged) {
            LocationActivityAdapter locationActivityAdapter5 = this.adapter;
            if (locationActivityAdapter5 != null) {
                locationActivityAdapter5.notifyDataSetChanged();
                return;
            }
            return;
        }
        if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue() || ((Long) objArr[0]).longValue() != this.dialogId || this.messageObject == null) {
                return;
            }
            ArrayList arrayList = (ArrayList) objArr[1];
            boolean z = false;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i3);
                if (messageObject.isLiveLocation()) {
                    addUserMarker(messageObject.messageOwner);
                    z = true;
                } else if ((messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGeoProximityReached) && DialogObject.isUserDialog(messageObject.getDialogId())) {
                    this.proximityButton.setImageResource(R.drawable.msg_location_alert);
                    IMapsProvider.ICircle iCircle = this.proximityCircle;
                    if (iCircle != null) {
                        iCircle.remove();
                        this.proximityCircle = null;
                    }
                }
            }
            if (!z || (locationActivityAdapter2 = this.adapter) == null) {
                return;
            }
            locationActivityAdapter2.setLiveLocations(this.markers);
            return;
        }
        if (i == NotificationCenter.replaceMessagesObjects) {
            long longValue = ((Long) objArr[0]).longValue();
            if (longValue != this.dialogId || this.messageObject == null) {
                return;
            }
            ArrayList arrayList2 = (ArrayList) objArr[1];
            boolean z2 = false;
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                MessageObject messageObject2 = (MessageObject) arrayList2.get(i4);
                if (messageObject2.isLiveLocation() && (liveLocation = (LiveLocation) this.markersMap.get(getMessageId(messageObject2.messageOwner))) != null) {
                    LocationController.SharingLocationInfo sharingLocationInfo = getLocationController().getSharingLocationInfo(longValue);
                    if (sharingLocationInfo == null || sharingLocationInfo.mid != messageObject2.getId()) {
                        TLRPC.Message message = messageObject2.messageOwner;
                        liveLocation.object = message;
                        TLRPC.GeoPoint geoPoint = message.media.geo;
                        IMapsProvider.LatLng latLng = new IMapsProvider.LatLng(geoPoint.lat, geoPoint._long);
                        liveLocation.marker.setPosition(latLng);
                        if (this.selectedMarkerId == liveLocation.id) {
                            this.map.animateCamera(ApplicationLoader.getMapsProvider().newCameraUpdateLatLng(liveLocation.marker.getPosition()));
                        }
                        IMapsProvider.IMarker iMarker = liveLocation.directionMarker;
                        if (iMarker != null) {
                            iMarker.getPosition();
                            liveLocation.directionMarker.setPosition(latLng);
                            int i5 = messageObject2.messageOwner.media.heading;
                            if (i5 != 0) {
                                liveLocation.directionMarker.setRotation(i5);
                                if (!liveLocation.hasRotation) {
                                    liveLocation.directionMarker.setIcon(R.drawable.map_pin_cone2);
                                    liveLocation.hasRotation = true;
                                }
                            } else if (liveLocation.hasRotation) {
                                liveLocation.directionMarker.setRotation(0);
                                liveLocation.directionMarker.setIcon(R.drawable.map_pin_circle);
                                liveLocation.hasRotation = false;
                            }
                        }
                    }
                    z2 = true;
                }
            }
            if (!z2 || (locationActivityAdapter = this.adapter) == null) {
                return;
            }
            locationActivityAdapter.notifyDataSetChanged();
            ProximitySheet proximitySheet = this.proximitySheet;
            if (proximitySheet != null) {
                proximitySheet.updateText(true, true);
            }
        }
    }

    protected boolean disablePermissionCheck() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean finishFragment(boolean z) {
        if (onCheckGlScreenshot()) {
            return false;
        }
        return super.finishFragment(z);
    }

    public String getAddressName() {
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            return locationActivityAdapter.getAddressName();
        }
        return null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.LocationActivity$$ExternalSyntheticLambda20
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                LocationActivity.this.lambda$getThemeDescriptions$47();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        for (int i = 0; i < this.undoView.length; i++) {
            UndoView undoView = this.undoView[i];
            int i2 = ThemeDescription.FLAG_BACKGROUNDFILTER;
            int i3 = Theme.key_undo_background;
            arrayList.add(new ThemeDescription(undoView, i2, null, null, null, null, i3));
            int i4 = Theme.key_undo_cancelColor;
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
            int i5 = Theme.key_undo_infoColor;
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"subinfoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "BODY", i3));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Wibe Big", i3));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Wibe Big 3", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Wibe Small", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Body Main.**", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Body Top.**", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Line.**", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Curve Big.**", i5));
            arrayList.add(new ThemeDescription(this.undoView[i], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Curve Small.**", i5));
        }
        View view = this.fragmentView;
        int i6 = ThemeDescription.FLAG_BACKGROUND;
        int i7 = Theme.key_dialogBackground;
        arrayList.add(new ThemeDescription(view, i6, null, null, null, themeDescriptionDelegate, i7));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i7));
        ActionBar actionBar = this.actionBar;
        int i8 = ThemeDescription.FLAG_AB_ITEMSCOLOR;
        int i9 = Theme.key_dialogTextBlack;
        arrayList.add(new ThemeDescription(actionBar, i8, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_dialogButtonSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_chat_messagePanelHint));
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        arrayList.add(new ThemeDescription(actionBarMenuItem != null ? actionBarMenuItem.getSearchField() : null, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        ImageView imageView = this.emptyImageView;
        int i10 = ThemeDescription.FLAG_IMAGECOLOR;
        int i11 = Theme.key_dialogEmptyImage;
        arrayList.add(new ThemeDescription(imageView, i10, null, null, null, null, i11));
        TextView textView = this.emptyTitleTextView;
        int i12 = ThemeDescription.FLAG_TEXTCOLOR;
        int i13 = Theme.key_dialogEmptyText;
        arrayList.add(new ThemeDescription(textView, i12, null, null, null, null, i13));
        arrayList.add(new ThemeDescription(this.emptySubtitleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i13));
        arrayList.add(new ThemeDescription(this.shadow, 0, null, null, null, null, Theme.key_sheet_scrollUp));
        ImageView imageView2 = this.locationButton;
        int i14 = ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_CHECKTAG;
        int i15 = Theme.key_location_actionIcon;
        arrayList.add(new ThemeDescription(imageView2, i14, null, null, null, null, i15));
        ImageView imageView3 = this.locationButton;
        int i16 = ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_CHECKTAG;
        int i17 = Theme.key_location_actionActiveIcon;
        arrayList.add(new ThemeDescription(imageView3, i16, null, null, null, null, i17));
        ImageView imageView4 = this.locationButton;
        int i18 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        int i19 = Theme.key_location_actionBackground;
        arrayList.add(new ThemeDescription(imageView4, i18, null, null, null, null, i19));
        ImageView imageView5 = this.locationButton;
        int i20 = ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE;
        int i21 = Theme.key_location_actionPressedBackground;
        arrayList.add(new ThemeDescription(imageView5, i20, null, null, null, null, i21));
        arrayList.add(new ThemeDescription(this.mapTypeButton, 0, null, null, null, themeDescriptionDelegate, i15));
        arrayList.add(new ThemeDescription(this.mapTypeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i19));
        arrayList.add(new ThemeDescription(this.mapTypeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, i21));
        arrayList.add(new ThemeDescription(this.proximityButton, 0, null, null, null, themeDescriptionDelegate, i15));
        arrayList.add(new ThemeDescription(this.proximityButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i19));
        arrayList.add(new ThemeDescription(this.proximityButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, i21));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i17));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i19));
        arrayList.add(new ThemeDescription(this.searchAreaButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, i21));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, themeDescriptionDelegate, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_location_liveLocationProgress));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_location_placeLocationBackground));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialog_liveLocationProgress));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLocationIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLiveLocationIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLocationBackground));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLiveLocationBackground));
        int i22 = Theme.key_windowBackgroundWhiteGrayText3;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SendLocationCell.class}, new String[]{"accurateTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLiveLocationText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_location_sendLocationText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationDirectionCell.class}, new String[]{"buttonTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_buttonText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{LocationDirectionCell.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addButton));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{LocationDirectionCell.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addButtonPressed));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_dialogTextBlue2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        int i23 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i23));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.searchListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i23));
        arrayList.add(new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SharingLiveLocationCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i23));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SharingLiveLocationCell.class}, new String[]{"distanceTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i22));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{LocationPoweredCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i11));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(getThemedColor(Theme.key_windowBackgroundWhite)) > 0.699999988079071d;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        ProximitySheet proximitySheet = this.proximitySheet;
        if (proximitySheet != null) {
            proximitySheet.dismiss();
            return false;
        }
        if (onCheckGlScreenshot()) {
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        UndoView undoView = this.undoView[0];
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getNotificationCenter().addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionDenied);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || !messageObject.isLiveLocation()) {
            return true;
        }
        getNotificationCenter().addObserver(this, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().addObserver(this, NotificationCenter.replaceMessagesObjects);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionDenied);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
        getNotificationCenter().removeObserver(this, NotificationCenter.closeChats);
        getNotificationCenter().removeObserver(this, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().removeObserver(this, NotificationCenter.replaceMessagesObjects);
        try {
            IMapsProvider.IMap iMap = this.map;
            if (iMap != null) {
                iMap.setMyLocationEnabled(false);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            IMapsProvider.IMapView iMapView = this.mapView;
            if (iMapView != null) {
                iMapView.onDestroy();
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        UndoView undoView = this.undoView[0];
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.destroy();
        }
        LocationActivitySearchAdapter locationActivitySearchAdapter = this.searchAdapter;
        if (locationActivitySearchAdapter != null) {
            locationActivitySearchAdapter.destroy();
        }
        Runnable runnable = this.updateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.updateRunnable = null;
        }
        Runnable runnable2 = this.markAsReadRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.markAsReadRunnable = null;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onLowMemory() {
        super.onLowMemory();
        IMapsProvider.IMapView iMapView = this.mapView;
        if (iMapView == null || !this.mapsInitialized) {
            return;
        }
        iMapView.onLowMemory();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        IMapsProvider.IMapView iMapView = this.mapView;
        if (iMapView != null && this.mapsInitialized) {
            try {
                iMapView.onPause();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        UndoView undoView = this.undoView[0];
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        this.onResumeCalled = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i == 30) {
            openShareLiveLocation(false, this.askWithRadius);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        Activity parentActivity;
        int checkSelfPermission;
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        IMapsProvider.IMapView iMapView = this.mapView;
        if (iMapView != null && this.mapsInitialized) {
            try {
                iMapView.onResume();
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        this.onResumeCalled = true;
        IMapsProvider.IMap iMap = this.map;
        if (iMap != null) {
            try {
                iMap.setMyLocationEnabled(true);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        fixLayoutInternal(true);
        if (disablePermissionCheck()) {
            this.checkPermission = false;
        } else if (this.checkPermission && Build.VERSION.SDK_INT >= 23 && (parentActivity = getParentActivity()) != null) {
            this.checkPermission = false;
            checkSelfPermission = parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
                parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            }
        }
        Runnable runnable = this.markAsReadRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(this.markAsReadRunnable, 5000L);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (!z || z2) {
            return;
        }
        try {
            if (this.mapView.getView().getParent() instanceof ViewGroup) {
                ((ViewGroup) this.mapView.getView().getParent()).removeView(this.mapView.getView());
            }
        } catch (Exception unused) {
        }
        FrameLayout frameLayout = this.mapViewClip;
        if (frameLayout == null) {
            View view = this.fragmentView;
            if (view != null) {
                ((FrameLayout) view).addView(this.mapView.getView(), 0, LayoutHelper.createFrame(-1, -1, 51));
                return;
            }
            return;
        }
        frameLayout.addView(this.mapView.getView(), 0, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0f), 51));
        MapOverlayView mapOverlayView = this.overlayView;
        if (mapOverlayView != null) {
            try {
                if (mapOverlayView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) this.overlayView.getParent()).removeView(this.overlayView);
                }
            } catch (Exception unused2) {
            }
            this.mapViewClip.addView(this.overlayView, 1, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0f), 51));
        }
        updateClipView(false);
        maybeShowProximityHint();
    }

    public LocationActivity searchStories(TL_stories.MediaArea mediaArea) {
        this.searchStoriesArea = mediaArea;
        return this;
    }

    public void setChatLocation(long j, TLRPC.TL_channelLocation tL_channelLocation) {
        this.dialogId = -j;
        this.chatLocation = tL_channelLocation;
    }

    public void setDelegate(LocationActivityDelegate locationActivityDelegate) {
        this.delegate = locationActivityDelegate;
    }

    public void setDialogId(long j) {
        this.dialogId = j;
    }

    public void setInitialLocation(TLRPC.TL_channelLocation tL_channelLocation) {
        this.initialLocation = tL_channelLocation;
    }

    public void setInitialMaxZoom(boolean z) {
        this.initialMaxZoom = z;
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
        this.dialogId = messageObject.getDialogId();
    }

    public void setSharingAllowed(boolean z) {
        this.isSharingAllowed = z;
    }
}
