package com.otaliastudios.cameraview;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Location;
import android.media.MediaActionSound;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Control;
import com.otaliastudios.cameraview.controls.ControlParser;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Grid;
import com.otaliastudios.cameraview.controls.Hdr;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.controls.VideoCodec;
import com.otaliastudios.cameraview.controls.WhiteBalance;
import com.otaliastudios.cameraview.engine.Camera1Engine;
import com.otaliastudios.cameraview.engine.Camera2Engine;
import com.otaliastudios.cameraview.engine.CameraEngine;
import com.otaliastudios.cameraview.engine.offset.Reference;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.FilterParser;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.filter.NoFilter;
import com.otaliastudios.cameraview.filter.OneParameterFilter;
import com.otaliastudios.cameraview.filter.TwoParameterFilter;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.otaliastudios.cameraview.gesture.GestureFinder;
import com.otaliastudios.cameraview.gesture.GestureParser;
import com.otaliastudios.cameraview.gesture.PinchGestureFinder;
import com.otaliastudios.cameraview.gesture.ScrollGestureFinder;
import com.otaliastudios.cameraview.gesture.SwipeGesture;
import com.otaliastudios.cameraview.gesture.TapGestureFinder;
import com.otaliastudios.cameraview.internal.GridLinesLayout;
import com.otaliastudios.cameraview.internal.utils.CropHelper;
import com.otaliastudios.cameraview.internal.utils.OrientationHelper;
import com.otaliastudios.cameraview.internal.utils.WorkerHandler;
import com.otaliastudios.cameraview.markers.AutoFocusMarker;
import com.otaliastudios.cameraview.markers.AutoFocusTrigger;
import com.otaliastudios.cameraview.markers.MarkerLayout;
import com.otaliastudios.cameraview.markers.MarkerParser;
import com.otaliastudios.cameraview.overlay.OverlayLayout;
import com.otaliastudios.cameraview.preview.CameraPreview;
import com.otaliastudios.cameraview.preview.FilterCameraPreview;
import com.otaliastudios.cameraview.preview.GlCameraPreview;
import com.otaliastudios.cameraview.preview.SurfaceCameraPreview;
import com.otaliastudios.cameraview.preview.TextureCameraPreview;
import com.otaliastudios.cameraview.size.AspectRatio;
import com.otaliastudios.cameraview.size.Size;
import com.otaliastudios.cameraview.size.SizeSelector;
import com.otaliastudios.cameraview.size.SizeSelectorParser;
import com.otaliastudios.cameraview.size.SizeSelectors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Entry point for the whole library.
 * Please read documentation for usage and full set of features.
 */
public class CameraView extends FrameLayout implements LifecycleObserver {

    public final static int PERMISSION_REQUEST_CODE = 16;
    final static long DEFAULT_AUTOFOCUS_RESET_DELAY_MILLIS = 3000;
    final static boolean DEFAULT_PLAY_SOUNDS = true;
    final static boolean DEFAULT_USE_DEVICE_ORIENTATION = true;
    final static boolean DEFAULT_PICTURE_METERING = true;
    final static boolean DEFAULT_PICTURE_SNAPSHOT_METERING = false;
    private final static String TAG = CameraView.class.getSimpleName();
    private static final CameraLogger LOG = CameraLogger.create(TAG);
    private static final int SELECT_IMAGE = 200;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    // Components
    @VisibleForTesting
    CameraCallbacks mCameraCallbacks;
    @VisibleForTesting
    List<CameraListener> mListeners = new CopyOnWriteArrayList<>();
    @VisibleForTesting
    List<FrameProcessor> mFrameProcessors = new CopyOnWriteArrayList<>();
    // Gestures
    @VisibleForTesting
    PinchGestureFinder mPinchGestureFinder;
    @VisibleForTesting
    TapGestureFinder mTapGestureFinder;
    @VisibleForTesting
    ScrollGestureFinder mScrollGestureFinder;
    @VisibleForTesting
    SwipeGesture swipeGesture;
    // Views
    @VisibleForTesting
    GridLinesLayout mGridLinesLayout;
    @VisibleForTesting
    MarkerLayout mMarkerLayout;
    // Overlays
    @VisibleForTesting
    OverlayLayout mOverlayLayout;
    // Self managed parameters
    private boolean mPlaySounds;
    private boolean mUseDeviceOrientation;
    private HashMap<Gesture, GestureAction> mGestureMap = new HashMap<>(4);
    private Preview mPreview;
    private Engine mEngine;
    private Filter mPendingFilter;
    private CameraPreview mCameraPreview;
    private OrientationHelper mOrientationHelper;
    private CameraEngine mCameraEngine;
    //DetectSwipeGestureListener detectSwipeGestureListener;
    private MediaActionSound mSound;
    private AutoFocusMarker mAutoFocusMarker;
    private Lifecycle mLifecycle;
    private boolean mKeepScreenOn;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean mExperimental;
    private boolean mInEditor;
    // Threading
    private Handler mUiHandler;
    private WorkerHandler mFrameProcessorsHandler;

    public CameraView(@NonNull Context context) {
        super(context, null);
        initialize(context, null);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    //region Init

    @SuppressWarnings("WrongConstant")
    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        mInEditor = isInEditMode();
        if (mInEditor) return;

        setWillNotDraw(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CameraView, 0, 0);
        ControlParser controls = new ControlParser(context, a);

        // Self managed
        boolean playSounds = a.getBoolean(R.styleable.CameraView_cameraPlaySounds, DEFAULT_PLAY_SOUNDS);
        boolean useDeviceOrientation = a.getBoolean(R.styleable.CameraView_cameraUseDeviceOrientation, DEFAULT_USE_DEVICE_ORIENTATION);
        mExperimental = a.getBoolean(R.styleable.CameraView_cameraExperimental, false);
        mPreview = controls.getPreview();
        mEngine = controls.getEngine();

        // Camera engine params
        int gridColor = a.getColor(R.styleable.CameraView_cameraGridColor, GridLinesLayout.DEFAULT_COLOR);
        long videoMaxSize = (long) a.getFloat(R.styleable.CameraView_cameraVideoMaxSize, 0);
        int videoMaxDuration = a.getInteger(R.styleable.CameraView_cameraVideoMaxDuration, 0);
        int videoBitRate = a.getInteger(R.styleable.CameraView_cameraVideoBitRate, 0);
        int audioBitRate = a.getInteger(R.styleable.CameraView_cameraAudioBitRate, 0);
        long autoFocusResetDelay = a.getInteger(R.styleable.CameraView_cameraAutoFocusResetDelay, (int) DEFAULT_AUTOFOCUS_RESET_DELAY_MILLIS);
        boolean pictureMetering = a.getBoolean(R.styleable.CameraView_cameraPictureMetering, DEFAULT_PICTURE_METERING);
        boolean pictureSnapshotMetering = a.getBoolean(R.styleable.CameraView_cameraPictureSnapshotMetering, DEFAULT_PICTURE_SNAPSHOT_METERING);

        // Size selectors and gestures
        SizeSelectorParser sizeSelectors = new SizeSelectorParser(a);
        GestureParser gestures = new GestureParser(a);
        MarkerParser markers = new MarkerParser(a);
        FilterParser filters = new FilterParser(a);

        a.recycle();

        // Components
        mCameraCallbacks = new CameraCallbacks();
        mUiHandler = new Handler(Looper.getMainLooper());
        mFrameProcessorsHandler = WorkerHandler.get("FrameProcessorsWorker");

        // Gestures
        mPinchGestureFinder = new PinchGestureFinder(mCameraCallbacks);
        mTapGestureFinder = new TapGestureFinder(mCameraCallbacks);
        mScrollGestureFinder = new ScrollGestureFinder(mCameraCallbacks);
        swipeGesture = new SwipeGesture(mCameraCallbacks);

        // Views
        mGridLinesLayout = new GridLinesLayout(context);
        mOverlayLayout = new OverlayLayout(context);
        mMarkerLayout = new MarkerLayout(context);
        addView(mGridLinesLayout);
        addView(mMarkerLayout);
        addView(mOverlayLayout);

        // Create the engine
        doInstantiateEngine();

        // Apply self managed
        setPlaySounds(playSounds);
        setUseDeviceOrientation(useDeviceOrientation);
        setGrid(controls.getGrid());
        setGridColor(gridColor);

        // Apply camera engine params
        // Adding new ones? See setEngine().
        setFacing(controls.getFacing());
        setFlash(controls.getFlash());
        setMode(controls.getMode());
        setWhiteBalance(controls.getWhiteBalance());
        setHdr(controls.getHdr());
        setAudio(controls.getAudio());
        setAudioBitRate(audioBitRate);
        setPictureSize(sizeSelectors.getPictureSizeSelector());
        setPictureMetering(pictureMetering);
        setPictureSnapshotMetering(pictureSnapshotMetering);
        setVideoSize(sizeSelectors.getVideoSizeSelector());
        setVideoCodec(controls.getVideoCodec());
        setVideoMaxSize(videoMaxSize);
        setVideoMaxDuration(videoMaxDuration);
        setVideoBitRate(videoBitRate);
        setAutoFocusResetDelay(autoFocusResetDelay);

        // Apply gestures
        mapGesture(Gesture.TAP, gestures.getTapAction());
        mapGesture(Gesture.LONG_TAP, gestures.getLongTapAction());
        mapGesture(Gesture.PINCH, gestures.getPinchAction());
        mapGesture(Gesture.SCROLL_HORIZONTAL, gestures.getHorizontalScrollAction());
        mapGesture(Gesture.SCROLL_VERTICAL, gestures.getVerticalScrollAction());
        mapGesture(Gesture.SWIPE_DOWN, gestures.getswipedownAction());
        mapGesture(Gesture.SWIPE_UP, gestures.getswipeupAction());

        // Apply markers
        setAutoFocusMarker(markers.getAutoFocusMarker());

        // Apply filters
        setFilter(filters.getFilter());

        // Create the orientation helper
        mOrientationHelper = new OrientationHelper(context, mCameraCallbacks);
    }

    /**
     * Engine is instantiated on creation and anytime
     * {@link #setEngine(Engine)} is called.
     */
    private void doInstantiateEngine() {
        LOG.w("doInstantiateEngine:", "instantiating. engine:", mEngine);
        mCameraEngine = instantiateCameraEngine(mEngine, mCameraCallbacks);
        LOG.w("doInstantiateEngine:", "instantiated. engine:", mCameraEngine.getClass().getSimpleName());
        mCameraEngine.setOverlay(mOverlayLayout);
    }

    /**
     * Preview is instantiated {@link #onAttachedToWindow()}, because
     * we want to know if we're hardware accelerated or not.
     * However, in tests, we might want to create the preview right after constructor.
     */
    @VisibleForTesting
    void doInstantiatePreview() {
        LOG.w("doInstantiateEngine:", "instantiating. preview:", mPreview);
        mCameraPreview = instantiatePreview(mPreview, getContext(), this);
        LOG.w("doInstantiateEngine:", "instantiated. preview:", mCameraPreview.getClass().getSimpleName());
        mCameraEngine.setPreview(mCameraPreview);
        if (mPendingFilter != null) {
            setFilter(mPendingFilter);
            mPendingFilter = null;
        }
    }

    /**
     * Instantiates the camera engine.
     *
     * @param engine   the engine preference
     * @param callback the engine callback
     * @return the engine
     */
    @NonNull
    public CameraEngine instantiateCameraEngine(@NonNull Engine engine, @NonNull CameraEngine.Callback callback) {
        if (mExperimental && engine == Engine.CAMERA2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Camera2Engine(callback);
        } else {
            mEngine = Engine.CAMERA1;
            return new Camera1Engine(callback);
        }
    }

    public void getfaces() {

    }

    /**
     * Instantiates the camera preview.
     *
     * @param preview   current preview value
     * @param context   a context
     * @param container the container
     * @return the preview
     */
    @NonNull
    protected CameraPreview instantiatePreview(@NonNull Preview preview, @NonNull Context context, @NonNull ViewGroup container) {
        switch (preview) {
            case SURFACE:
                return new SurfaceCameraPreview(context, container);
            case TEXTURE: {
                if (isHardwareAccelerated()) {
                    // TextureView is not supported without hardware acceleration.
                    return new TextureCameraPreview(context, container);
                }
            }
            case GL_SURFACE:
            default: {
                mPreview = Preview.GL_SURFACE;
                return new GlCameraPreview(context, container);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mInEditor) return;
        if (mCameraPreview == null) {
            // isHardwareAccelerated will return the real value only after we are
            // attached. That's why we instantiate the preview here.
            doInstantiatePreview();
        }
        mOrientationHelper.enable(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!mInEditor) mOrientationHelper.disable();
        super.onDetachedFromWindow();
    }

    //endregion

    //region Measuring behavior

    private String ms(int mode) {
        switch (mode) {
            case AT_MOST:
                return "AT_MOST";
            case EXACTLY:
                return "EXACTLY";
            case UNSPECIFIED:
                return "UNSPECIFIED";
        }
        return null;
    }

    /**
     * Measuring is basically controlled by layout params width and height.
     * The basic semantics are:
     * <p>
     * - MATCH_PARENT: CameraView should completely fill this dimension, even if this might mean
     * not respecting the preview aspect ratio.
     * - WRAP_CONTENT: CameraView should try to adapt this dimension to respect the preview
     * aspect ratio.
     * <p>
     * When both dimensions are MATCH_PARENT, CameraView will fill its
     * parent no matter the preview. Thanks to what happens in {@link CameraPreview}, this acts like
     * a CENTER CROP scale type.
     * <p>
     * When both dimensions are WRAP_CONTENT, CameraView will take the biggest dimensions that
     * fit the preview aspect ratio. This acts like a CENTER INSIDE scale type.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mInEditor) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, EXACTLY));
            return;
        }

        Size previewSize = mCameraEngine.getPreviewStreamSize(Reference.VIEW);
        if (previewSize == null) {
            LOG.w("onMeasure:", "surface is not ready. Calling default behavior.");
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        // Let's which dimensions need to be adapted.
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthValue = MeasureSpec.getSize(widthMeasureSpec);
        final int heightValue = MeasureSpec.getSize(heightMeasureSpec);
        final float previewWidth = previewSize.getWidth();
        final float previewHeight = previewSize.getHeight();

        // Pre-process specs
        final ViewGroup.LayoutParams lp = getLayoutParams();
        if (!mCameraPreview.supportsCropping()) {
            // We can't allow EXACTLY constraints in this case.
            if (widthMode == EXACTLY) widthMode = AT_MOST;
            if (heightMode == EXACTLY) heightMode = AT_MOST;
        } else {
            // If MATCH_PARENT is interpreted as AT_MOST, transform to EXACTLY
            // to be consistent with our semantics (and our docs).
            if (widthMode == AT_MOST && lp.width == MATCH_PARENT) widthMode = EXACTLY;
            if (heightMode == AT_MOST && lp.height == MATCH_PARENT) heightMode = EXACTLY;
        }

        LOG.i("onMeasure:", "requested dimensions are", "(" + widthValue + "[" + ms(widthMode) + "]x" +
                heightValue + "[" + ms(heightMode) + "])");
        LOG.i("onMeasure:", "previewSize is", "(" + previewWidth + "x" + previewHeight + ")");

        // (1) If we have fixed dimensions (either 300dp or MATCH_PARENT), there's nothing we should do,
        // other than respect it. The preview will eventually be cropped at the sides (by PreviewImpl scaling)
        // except the case in which these fixed dimensions manage to fit exactly the preview aspect ratio.
        if (widthMode == EXACTLY && heightMode == EXACTLY) {
            LOG.i("onMeasure:", "both are MATCH_PARENT or fixed value. We adapt.",
                    "This means CROP_CENTER.", "(" + widthValue + "x" + heightValue + ")");
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        // (2) If both dimensions are free, with no limits, then our size will be exactly the
        // preview size. This can happen rarely, for example in 2d scrollable containers.
        if (widthMode == UNSPECIFIED && heightMode == UNSPECIFIED) {
            LOG.i("onMeasure:", "both are completely free.",
                    "We respect that and extend to the whole preview size.",
                    "(" + previewWidth + "x" + previewHeight + ")");
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec((int) previewWidth, EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) previewHeight, EXACTLY));
            return;
        }

        // It's sure now that at least one dimension can be determined (either because EXACTLY or AT_MOST).
        // This starts to seem a pleasant situation.

        // (3) If one of the dimension is completely free (e.g. in a scrollable container),
        // take the other and fit the ratio.
        // One of the two might be AT_MOST, but we use the value anyway.
        float ratio = previewHeight / previewWidth;
        if (widthMode == UNSPECIFIED || heightMode == UNSPECIFIED) {
            boolean freeWidth = widthMode == UNSPECIFIED;
            int height, width;
            if (freeWidth) {
                height = heightValue;
                width = Math.round(height / ratio);
            } else {
                width = widthValue;
                height = Math.round(width * ratio);
            }
            LOG.i("onMeasure:", "one dimension was free, we adapted it to fit the aspect ratio.",
                    "(" + width + "x" + height + ")");
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, EXACTLY));
            return;
        }

        // (4) At this point both dimensions are either AT_MOST-AT_MOST, EXACTLY-AT_MOST or AT_MOST-EXACTLY.
        // Let's manage this sanely. If only one is EXACTLY, we can TRY to fit the aspect ratio,
        // but it is not guaranteed to succeed. It depends on the AT_MOST value of the other dimensions.
        if (widthMode == EXACTLY || heightMode == EXACTLY) {
            boolean freeWidth = widthMode == AT_MOST;
            int height, width;
            if (freeWidth) {
                height = heightValue;
                width = Math.min(Math.round(height / ratio), widthValue);
            } else {
                width = widthValue;
                height = Math.min(Math.round(width * ratio), heightValue);
            }
            LOG.i("onMeasure:", "one dimension was EXACTLY, another AT_MOST.",
                    "We have TRIED to fit the aspect ratio, but it's not guaranteed.",
                    "(" + width + "x" + height + ")");
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, EXACTLY));
            return;
        }

        // (5) Last case, AT_MOST and AT_MOST. Here we can SURELY fit the aspect ratio by
        // filling one dimension and adapting the other.
        int height, width;
        float atMostRatio = (float) heightValue / (float) widthValue;
        if (atMostRatio >= ratio) {
            // We must reduce height.
            width = widthValue;
            height = Math.round(width * ratio);
        } else {
            height = heightValue;
            width = Math.round(height / ratio);
        }
        LOG.i("onMeasure:", "both dimension were AT_MOST.",
                "We fit the preview aspect ratio.",
                "(" + width + "x" + height + ")");
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, EXACTLY),
                MeasureSpec.makeMeasureSpec(height, EXACTLY));
    }

    //endregion

    //region Gesture APIs

    /**
     * Maps a {@link Gesture} to a certain gesture action.
     * For example, you can assign zoom control to the pinch gesture by just calling:
     * <code>
     * cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
     * </code>
     * <p>
     * Not all actions can be assigned to a certain gesture. For example, zoom control can't be
     * assigned to the Gesture.TAP gesture. Look at {@link Gesture} to know more.
     * This method returns false if they are not assignable.
     *
     * @param gesture which gesture to map
     * @param action  which action should be assigned
     * @return true if this action could be assigned to this gesture
     */
    public boolean mapGesture(@NonNull Gesture gesture, @NonNull GestureAction action) {
        GestureAction none = GestureAction.NONE;
        if (gesture.isAssignableTo(action)) {
            mGestureMap.put(gesture, action);
            switch (gesture) {
                case PINCH:
                    mPinchGestureFinder.setActive(mGestureMap.get(Gesture.PINCH) != none);
                    break;
                case TAP:
                    // case DOUBLE_TAP:
                case LONG_TAP:
                    mTapGestureFinder.setActive(
                            mGestureMap.get(Gesture.TAP) != none ||
                                    // mGestureMap.get(Gesture.DOUBLE_TAP) != none ||
                                    mGestureMap.get(Gesture.LONG_TAP) != none);
                    break;
                /*case SCROLL_HORIZONTAL:
                case SCROLL_VERTICAL:
                    mScrollGestureFinder.setActive(
                            mGestureMap.get(Gesture.SCROLL_HORIZONTAL) != none ||
                                    mGestureMap.get(Gesture.SCROLL_VERTICAL) != none);
                    break;*/
                case SWIPE_DOWN:
                    swipeGesture.setActive(
                            mGestureMap.get(Gesture.SWIPE_DOWN) != none ||
                                    mGestureMap.get(Gesture.SWIPE_UP) != none);


                case DOUBLE_TAP:
                    swipeGesture.setActive(mGestureMap.get(Gesture.DOUBLE_TAP) != none) ;
            }
            return true;
        }
        mapGesture(gesture, none);
        return false;
    }

    /**
     * Clears any action mapped to the given gesture.
     *
     * @param gesture which gesture to clear
     */
    public void clearGesture(@NonNull Gesture gesture) {
        mapGesture(gesture, GestureAction.NONE);
    }

    /**
     * Returns the action currently mapped to the given gesture.
     *
     * @param gesture which gesture to inspect
     * @return mapped action
     */
    @NonNull
    public GestureAction getGestureAction(@NonNull Gesture gesture) {
        //noinspection ConstantConditions
        return mGestureMap.get(gesture);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true; // Steal our own events.
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isOpened()) return true;

        // Pass to our own GestureLayouts
        CameraOptions options = mCameraEngine.getCameraOptions(); // Non null
        if (options == null) throw new IllegalStateException("Options should not be null here.");
        if (mPinchGestureFinder.onTouchEvent(event)) {
            LOG.i("onTouchEvent", "pinch!");
            onGesture(mPinchGestureFinder, options);
        } /*else if (mScrollGestureFinder.onTouchEvent(event)) {
            LOG.i("onTouchEvent", "scroll!");
            onGesture(mScrollGestureFinder, options);
        }*/ else if (mTapGestureFinder.onTouchEvent(event)) {
            LOG.i("onTouchEvent", "tap!");
            onGesture(mTapGestureFinder, options);
        } else if (swipeGesture.onTouchEvent(event)) {
            LOG.i("onTouchEvent", "tap!");
            onGesture(swipeGesture, options);
        }

        return true;
    }

    // Some gesture layout detected a gesture. It's not known at this moment:
    // (1) if it was mapped to some action (we check here)
    // (2) if it's supported by the camera (CameraEngine checks)
    private void onGesture(@NonNull GestureFinder source, @NonNull CameraOptions options) {
        Gesture gesture = source.getGesture();
        GestureAction action = mGestureMap.get(gesture);
        PointF[] points = source.getPoints();
        float oldValue, newValue;
        //noinspection ConstantConditions
        switch (action) {

            case TAKE_PICTURE:
                takePicture();
                break;

            case AUTO_FOCUS:
                //  mCameraEngine.startAutoFocus(gesture, points[0]);

                if (mCameraEngine.getFacing() != Facing.BACK) {
                    mCameraEngine.setFacing(Facing.BACK);
                    Log.d("mn13helloworld", "hell");
                } else {
                    mCameraEngine.setFacing(Facing.FRONT);
                    Log.d("mn13helloworld1", "hell");
                }

                break;

            case ZOOM:
                oldValue = mCameraEngine.getZoomValue();
                newValue = source.computeValue(oldValue, 0, 1);
                if (newValue != oldValue) {
                    mCameraEngine.setZoom(newValue, points, true);
                }
                break;

            case EXPOSURE_CORRECTION:
                oldValue = mCameraEngine.getExposureCorrectionValue();
                float minValue = options.getExposureCorrectionMinValue();
                float maxValue = options.getExposureCorrectionMaxValue();
                newValue = source.computeValue(oldValue, minValue, maxValue);
                if (newValue != oldValue) {
                    float[] bounds = new float[]{minValue, maxValue};
                    mCameraEngine.setExposureCorrection(newValue, bounds, points, true);

                }
                break;

            case FILTER_CONTROL_1:
                if (!mExperimental) break;
                if (getFilter() instanceof OneParameterFilter) {
                    OneParameterFilter filter = (OneParameterFilter) getFilter();
                    oldValue = filter.getParameter1();
                    newValue = source.computeValue(oldValue, 0, 1);
                    if (newValue != oldValue) {
                        filter.setParameter1(newValue);
                    }
                }
                break;

            case FILTER_CONTROL_2:
                if (!mExperimental) break;
                if (getFilter() instanceof TwoParameterFilter) {
                    TwoParameterFilter filter = (TwoParameterFilter) getFilter();
                    oldValue = filter.getParameter2();
                    newValue = source.computeValue(oldValue, 0, 1);
                    if (newValue != oldValue) {
                        filter.setParameter2(newValue);
                    }
                }
                break;

            case NONE:
                if (gesture.name().equals("SWIPE_DOWN")) {

//                    try {
//                        Intent myIntent = new Intent(getContext(), Class.forName("com.TBI.Client.Bluff.BottombarActivity"));
//                        myIntent.putExtra("type", "map");
//                        getContext().startActivity(myIntent);
//                        ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }

                } else if (gesture.name().equalsIgnoreCase("SWIPE_UP")) {

//                    try {
//                        Intent myIntent = new Intent(getContext(), Class.forName("com.TBI.Client.Bluff.Activity.Profile.ProfilePage"));
//                        myIntent.putExtra("type","top");
//                        getContext().startActivity(myIntent);
//                        ((Activity)getContext()).overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/* video/*");
                    ((Activity) getContext()).startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }

                break;

            case SWIPEDOWN:

                break;

        }
    }
    //endregion

    //region Lifecycle APIs

    /**
     * Returns whether the camera has started showing its preview.
     *
     * @return whether the camera has started
     */
    public boolean isOpened() {
        return mCameraEngine.getEngineState() >= CameraEngine.STATE_STARTED;
    }

    private boolean isClosed() {
        return mCameraEngine.getEngineState() == CameraEngine.STATE_STOPPED;
    }

    /**
     * Sets the lifecycle owner for this view. This means you don't need
     * to call {@link #open()}, {@link #close()} or {@link #destroy()} at all.
     *
     * @param owner the owner activity or fragment
     */
    public void setLifecycleOwner(@NonNull LifecycleOwner owner) {
        if (mLifecycle != null) mLifecycle.removeObserver(this);
        mLifecycle = owner.getLifecycle();
        mLifecycle.addObserver(this);
    }

    /**
     * Starts the camera preview, if not started already.
     * This should be called onResume(), or when you are ready with permissions.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void open() {
        if (mInEditor) return;
        if (mCameraPreview != null) mCameraPreview.onResume();
        if (checkPermissions(getAudio())) {
            // Update display orientation for current CameraEngine
            mOrientationHelper.enable(getContext());
            mCameraEngine.getAngles().setDisplayOffset(mOrientationHelper.getDisplayOffset());
            mCameraEngine.start();
        }
    }

    /**
     * Checks that we have appropriate permissions.
     * This means checking that we have audio permissions if audio = Audio.ON.
     *
     * @param audio the audio setting to be checked
     * @return true if we can go on, false otherwise.
     */
    @SuppressWarnings("ConstantConditions")
    @SuppressLint("NewApi")
    protected boolean checkPermissions(@NonNull Audio audio) {
        checkPermissionsManifestOrThrow(audio);
        // Manifest is OK at this point. Let's check runtime permissions.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        Context c = getContext();
        boolean needsCamera = true;
        boolean needsAudio = audio == Audio.ON || audio == Audio.MONO || audio == Audio.STEREO;

        needsCamera = needsCamera && c.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        needsAudio = needsAudio && c.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;

        if (needsCamera || needsAudio) {
            requestPermissions(needsCamera, needsAudio);
            return false;
        }
        return true;
    }

    /**
     * If audio is on we will ask for RECORD_AUDIO permission.
     * If the developer did not add this to its manifest, throw and fire warnings.
     */
    private void checkPermissionsManifestOrThrow(@NonNull Audio audio) {
        if (audio == Audio.ON || audio == Audio.MONO || audio == Audio.STEREO) {
            try {
                PackageManager manager = getContext().getPackageManager();
                PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), PackageManager.GET_PERMISSIONS);
                for (String requestedPermission : info.requestedPermissions) {
                    if (requestedPermission.equals(Manifest.permission.RECORD_AUDIO)) {
                        return;
                    }
                }
                String message = LOG.e("Permission error:", "When audio is enabled (Audio.ON),",
                        "the RECORD_AUDIO permission should be added to the app manifest file.");
                throw new IllegalStateException(message);
            } catch (PackageManager.NameNotFoundException e) {
                // Not possible.
            }
        }
    }

    /**
     * Stops the current preview, if any was started.
     * This should be called onPause().
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void close() {
        if (mInEditor) return;
        mCameraEngine.stop();
        if (mCameraPreview != null) mCameraPreview.onPause();
    }

    /**
     * Destroys this instance, releasing immediately
     * the camera resource.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        if (mInEditor) return;
        clearCameraListeners();
        clearFrameProcessors();
        mCameraEngine.destroy();
        if (mCameraPreview != null) mCameraPreview.onDestroy();
    }

    //endregion

    //region Public APIs for controls

    /**
     * Sets the experimental flag which occasionally can enable
     * new, unstable beta features.
     *
     * @param experimental true to enable new features
     */
    public void setExperimental(boolean experimental) {
        mExperimental = experimental;
    }

    /**
     * Shorthand for the appropriate set* method.
     * For example, if control is a {@link Grid}, this calls {@link #setGrid(Grid)}.
     *
     * @param control desired value
     */
    public void set(@NonNull Control control) {
        if (control instanceof Audio) {
            setAudio((Audio) control);
        } else if (control instanceof Facing) {
            setFacing((Facing) control);
        } else if (control instanceof Flash) {
            setFlash((Flash) control);
        } else if (control instanceof Grid) {
            setGrid((Grid) control);
        } else if (control instanceof Hdr) {
            setHdr((Hdr) control);
        } else if (control instanceof Mode) {
            setMode((Mode) control);
        } else if (control instanceof WhiteBalance) {
            setWhiteBalance((WhiteBalance) control);
        } else if (control instanceof VideoCodec) {
            setVideoCodec((VideoCodec) control);
        } else if (control instanceof Preview) {
            setPreview((Preview) control);
        } else if (control instanceof Engine) {
            setEngine((Engine) control);
        }
    }

    /**
     * Shorthand for the appropriate get* method.
     * For example, if control class is a {@link Grid}, this calls {@link #getGrid()}.
     *
     * @param controlClass desired value class
     * @param <T>          the class type
     * @return the control
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public <T extends Control> T get(@NonNull Class<T> controlClass) {
        if (controlClass == Audio.class) {
            return (T) getAudio();
        } else if (controlClass == Facing.class) {
            return (T) getFacing();
        } else if (controlClass == Flash.class) {
            return (T) getFlash();
        } else if (controlClass == Grid.class) {
            return (T) getGrid();
        } else if (controlClass == Hdr.class) {
            return (T) getHdr();
        } else if (controlClass == Mode.class) {
            return (T) getMode();
        } else if (controlClass == WhiteBalance.class) {
            return (T) getWhiteBalance();
        } else if (controlClass == VideoCodec.class) {
            return (T) getVideoCodec();
        } else if (controlClass == Preview.class) {
            return (T) getPreview();
        } else if (controlClass == Engine.class) {
            return (T) getEngine();
        } else {
            throw new IllegalArgumentException("Unknown control class: " + controlClass);
        }
    }

    /**
     * Returns the current preview control.
     *
     * @return the current preview control
     * @see #setPreview(Preview)
     */
    @NonNull
    public Preview getPreview() {
        return mPreview;
    }

    /**
     * Controls the preview engine. Should only be called
     * if this CameraView was never added to any window
     * (like if you created it programmatically).
     * Otherwise, it has no effect.
     *
     * @param preview desired preview engine
     * @see Preview#SURFACE
     * @see Preview#TEXTURE
     * @see Preview#GL_SURFACE
     */
    public void setPreview(@NonNull Preview preview) {
        boolean isNew = preview != mPreview;
        if (isNew) {
            mPreview = preview;
            boolean isAttachedToWindow = getWindowToken() != null;
            if (!isAttachedToWindow && mCameraPreview != null) {
                // Null the preview: will create another when re-attaching.
                mCameraPreview.onDestroy();
                mCameraPreview = null;
            }
        }
    }

    /**
     * Returns the current engine control.
     *
     * @return the current engine control
     * @see #setEngine(Engine)
     */
    @NonNull
    public Engine getEngine() {
        return mEngine;
    }

    /**
     * Controls the core engine. Should only be called
     * if this CameraView is closed (open() was never called).
     * Otherwise, it has no effect.
     *
     * @param engine desired engine
     * @see Engine#CAMERA1
     * @see Engine#CAMERA2
     */
    public void setEngine(@NonNull Engine engine) {
        if (!isClosed()) return;
        mEngine = engine;
        CameraEngine oldEngine = mCameraEngine;
        doInstantiateEngine();
        if (mCameraPreview != null) mCameraEngine.setPreview(mCameraPreview);

        // Set again all parameters
        setFacing(oldEngine.getFacing());
        setFlash(oldEngine.getFlash());
        setMode(oldEngine.getMode());
        setWhiteBalance(oldEngine.getWhiteBalance());
        setHdr(oldEngine.getHdr());
        setAudio(oldEngine.getAudio());
        setAudioBitRate(oldEngine.getAudioBitRate());
        setPictureSize(oldEngine.getPictureSizeSelector());
        setVideoSize(oldEngine.getVideoSizeSelector());
        setVideoCodec(oldEngine.getVideoCodec());
        setVideoMaxSize(oldEngine.getVideoMaxSize());
        setVideoMaxDuration(oldEngine.getVideoMaxDuration());
        setVideoBitRate(oldEngine.getVideoBitRate());
        setAutoFocusResetDelay(oldEngine.getAutoFocusResetDelay());
    }

    /**
     * Returns a {@link CameraOptions} instance holding supported options for this camera
     * session. This might change over time. It's better to hold a reference from
     * {@link CameraListener#onCameraOpened(CameraOptions)}.
     *
     * @return an options map, or null if camera was not opened
     */
    @Nullable
    public CameraOptions getCameraOptions() {
        return mCameraEngine.getCameraOptions();
    }

    /**
     * Returns the current exposure correction value, typically 0
     * at start-up.
     *
     * @return the current exposure correction value
     */
    public float getExposureCorrection() {
        return mCameraEngine.getExposureCorrectionValue();
    }

    /**
     * Sets exposure adjustment, in EV stops. A positive value will mean brighter picture.
     * <p>
     * If camera is not opened, this will have no effect.
     * If {@link CameraOptions#isExposureCorrectionSupported()} is false, this will have no effect.
     * The provided value should be between the bounds returned by {@link CameraOptions}, or it will
     * be capped.
     *
     * @param EVvalue exposure correction value.
     * @see CameraOptions#getExposureCorrectionMinValue()
     * @see CameraOptions#getExposureCorrectionMaxValue()
     */
    public void setExposureCorrection(float EVvalue) {
        CameraOptions options = getCameraOptions();
        if (options != null) {
            float min = options.getExposureCorrectionMinValue();
            float max = options.getExposureCorrectionMaxValue();
            if (EVvalue < min) EVvalue = min;
            if (EVvalue > max) EVvalue = max;
            float[] bounds = new float[]{min, max};
            mCameraEngine.setExposureCorrection(EVvalue, bounds, null, false);
        }
    }

    /**
     * Returns the current zoom value, something between 0 and 1.
     *
     * @return the current zoom value
     */
    public float getZoom() {
        return mCameraEngine.getZoomValue();
    }

    /**
     * Sets a zoom value. This is not guaranteed to be supported by the current device,
     * but you can take a look at {@link CameraOptions#isZoomSupported()}.
     * This will have no effect if called before the camera is opened.
     * <p>
     * Zoom value should be between 0 and 1, where 1 will be the maximum available zoom.
     * If it's not, it will be capped.
     *
     * @param zoom value in [0,1]
     */
    public void setZoom(float zoom) {
        if (zoom < 0) zoom = 0;
        if (zoom > 1) zoom = 1;
        mCameraEngine.setZoom(zoom, null, false);
    }

    /**
     * Gets the current grid mode.
     *
     * @return the current grid mode
     */
    @NonNull
    public Grid getGrid() {
        return mGridLinesLayout.getGridMode();
    }

    /**
     * Controls the grids to be drawn over the current layout.
     *
     * @param gridMode desired grid mode
     * @see Grid#OFF
     * @see Grid#DRAW_3X3
     * @see Grid#DRAW_4X4
     * @see Grid#DRAW_PHI
     */
    public void setGrid(@NonNull Grid gridMode) {
        mGridLinesLayout.setGridMode(gridMode);
    }

    /**
     * Returns the current grid color.
     *
     * @return the current grid color
     */
    public int getGridColor() {
        return mGridLinesLayout.getGridColor();
    }

    /**
     * Controls the color of the grid lines that will be drawn
     * over the current layout.
     *
     * @param color a resolved color
     */
    public void setGridColor(@ColorInt int color) {
        mGridLinesLayout.setGridColor(color);
    }

    /**
     * Gets the current hdr value.
     *
     * @return the current hdr value
     */
    @NonNull
    public Hdr getHdr() {
        return mCameraEngine.getHdr();
    }

    /**
     * Controls the grids to be drawn over the current layout.
     *
     * @param hdr desired hdr value
     * @see Hdr#OFF
     * @see Hdr#ON
     */
    public void setHdr(@NonNull Hdr hdr) {
        mCameraEngine.setHdr(hdr);
    }

    /**
     * Set location coordinates to be found later in the EXIF header
     *
     * @param latitude  current latitude
     * @param longitude current longitude
     */
    public void setLocation(double latitude, double longitude) {
        Location location = new Location("Unknown");
        location.setTime(System.currentTimeMillis());
        location.setAltitude(0);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        mCameraEngine.setLocation(location);
    }

    /**
     * Retrieves the location previously applied with setLocation().
     *
     * @return the current location, if any.
     */
    @Nullable
    public Location getLocation() {
        return mCameraEngine.getLocation();
    }

    /**
     * Set location values to be found later in the EXIF header
     *
     * @param location current location
     */
    public void setLocation(@Nullable Location location) {
        mCameraEngine.setLocation(location);
    }

    /**
     * Returns the current white balance behavior.
     *
     * @return white balance value.
     */
    @NonNull
    public WhiteBalance getWhiteBalance() {
        return mCameraEngine.getWhiteBalance();
    }

    /**
     * Sets desired white balance to current camera session.
     *
     * @param whiteBalance desired white balance behavior.
     * @see WhiteBalance#AUTO
     * @see WhiteBalance#INCANDESCENT
     * @see WhiteBalance#FLUORESCENT
     * @see WhiteBalance#DAYLIGHT
     * @see WhiteBalance#CLOUDY
     */
    public void setWhiteBalance(@NonNull WhiteBalance whiteBalance) {
        mCameraEngine.setWhiteBalance(whiteBalance);
    }

    /**
     * Gets the facing camera currently being used.
     *
     * @return a facing value.
     */
    @NonNull
    public Facing getFacing() {
        return mCameraEngine.getFacing();
    }

    /**
     * Sets which camera sensor should be used.
     *
     * @param facing a facing value.
     * @see Facing#FRONT
     * @see Facing#BACK
     */
    public void setFacing(@NonNull Facing facing) {
        mCameraEngine.setFacing(facing);
    }

    /**
     * Toggles the facing value between {@link Facing#BACK}
     * and {@link Facing#FRONT}.
     *
     * @return the new facing value
     */
    public Facing toggleFacing() {
        Facing facing = mCameraEngine.getFacing();
        switch (facing) {
            case BACK:
                setFacing(Facing.FRONT);
                break;

            case FRONT:
                setFacing(Facing.BACK);
                break;
        }

        return mCameraEngine.getFacing();
    }

    /**
     * Gets the current flash mode.
     *
     * @return a flash mode
     */
    @NonNull
    public Flash getFlash() {
        return mCameraEngine.getFlash();
    }

    /**
     * Sets the flash mode.
     *
     * @param flash desired flash mode.
     * @see Flash#OFF
     * @see Flash#ON
     * @see Flash#AUTO
     * @see Flash#TORCH
     */
    public void setFlash(@NonNull Flash flash) {
        mCameraEngine.setFlash(flash);
    }

    /**
     * Gets the current audio value.
     *
     * @return the current audio value
     */
    @NonNull
    public Audio getAudio() {
        return mCameraEngine.getAudio();
    }

    /**
     * Controls the audio mode.
     *
     * @param audio desired audio value
     * @see Audio#OFF
     * @see Audio#ON
     * @see Audio#MONO
     * @see Audio#STEREO
     */
    public void setAudio(@NonNull Audio audio) {

        if (audio == getAudio() || isClosed()) {
            // Check did took place, or will happen on start().
            mCameraEngine.setAudio(audio);

        } else if (checkPermissions(audio)) {
            // Camera is running. Pass.
            mCameraEngine.setAudio(audio);

        } else {
            // This means that the audio permission is being asked.
            // Stop the camera so it can be restarted by the developer onPermissionResult.
            // Developer must also set the audio value again...
            // Not ideal but good for now.
            close();
        }
    }

    /**
     * Sets an {@link AutoFocusMarker} to be notified of metering start, end and fail events
     * so that it can draw elements on screen.
     *
     * @param autoFocusMarker the marker, or null
     */
    public void setAutoFocusMarker(@Nullable AutoFocusMarker autoFocusMarker) {
        mAutoFocusMarker = autoFocusMarker;
        mMarkerLayout.onMarker(MarkerLayout.TYPE_AUTOFOCUS, autoFocusMarker);
    }

    /**
     * Returns the current delay in milliseconds to reset the focus after a metering event.
     *
     * @return the current reset delay in milliseconds
     */
    @SuppressWarnings("unused")
    public long getAutoFocusResetDelay() {
        return mCameraEngine.getAutoFocusResetDelay();
    }

    /**
     * Sets the current delay in milliseconds to reset the focus after a metering event.
     *
     * @param delayMillis desired delay (in milliseconds). If the delay
     *                    is less than or equal to 0 or equal to Long.MAX_VALUE,
     *                    the values will not be reset.
     */
    public void setAutoFocusResetDelay(long delayMillis) {
        mCameraEngine.setAutoFocusResetDelay(delayMillis);
    }

    /**
     * Starts a 3A touch metering process at the given coordinates, with respect
     * to the view width and height.
     *
     * @param x should be between 0 and getWidth()
     * @param y should be between 0 and getHeight()
     */
    public void startAutoFocus(float x, float y) {
        if (x < 0 || x > getWidth())
            throw new IllegalArgumentException("x should be >= 0 and <= getWidth()");
        if (y < 0 || y > getHeight())
            throw new IllegalArgumentException("y should be >= 0 and <= getHeight()");
        mCameraEngine.startAutoFocus(null, new PointF(x, y));
    }

    /**
     * <strong>ADVANCED FEATURE</strong> - sets a size selector for the preview stream.
     * The {@link SizeSelector} will be invoked with the list of available sizes, and the first
     * acceptable size will be accepted and passed to the internal engine and surface.
     * <p>
     * This is typically NOT NEEDED. The default size selector is already smart enough to respect
     * the picture/video output aspect ratio, and be bigger than the surface so that there is no
     * upscaling. If all you want is set an aspect ratio, use {@link #setPictureSize(SizeSelector)}
     * and {@link #setVideoSize(SizeSelector)}.
     * <p>
     * When stream size changes, the {@link CameraView} is remeasured so any WRAP_CONTENT dimension
     * is recomputed accordingly.
     * <p>
     * See the {@link SizeSelectors} class for handy utilities for creating selectors.
     *
     * @param selector a size selector
     */
    public void setPreviewStreamSize(@NonNull SizeSelector selector) {
        mCameraEngine.setPreviewStreamSizeSelector(selector);
    }

    /**
     * Gets the current mode.
     *
     * @return the current mode
     */
    @NonNull
    public Mode getMode() {
        return mCameraEngine.getMode();
    }

    /**
     * Set the current session type to either picture or video.
     *
     * @param mode desired session type.
     * @see Mode#PICTURE
     * @see Mode#VIDEO
     */
    public void setMode(@NonNull Mode mode) {
        mCameraEngine.setMode(mode);
    }

    /**
     * Whether the engine should perform a metering sequence before taking pictures requested
     * with {@link #takePicture()}. See {@link #setPictureMetering(boolean)}.
     *
     * @return true if picture metering is enabled
     * @see #setPictureMetering(boolean)
     */
    public boolean getPictureMetering() {
        return mCameraEngine.getPictureMetering();
    }

    /**
     * Whether the engine should perform a metering sequence before taking pictures requested
     * with {@link #takePicture()}. A metering sequence includes adjusting focus, exposure
     * and white balance to ensure a good quality of the result.
     * <p>
     * When this parameter is true, the quality of the picture increases, but the latency
     * increases as well. Defaults to true.
     * <p>
     * This is a CAMERA2 only API. On CAMERA1, picture metering is always enabled.
     *
     * @param enable true to enable
     * @see #setPictureSnapshotMetering(boolean)
     */
    public void setPictureMetering(boolean enable) {
        mCameraEngine.setPictureMetering(enable);
    }

    /**
     * Whether the engine should perform a metering sequence before taking pictures requested
     * with {@link #takePictureSnapshot()}. See {@link #setPictureSnapshotMetering(boolean)}.
     *
     * @return true if picture metering is enabled
     * @see #setPictureSnapshotMetering(boolean)
     */
    public boolean getPictureSnapshotMetering() {
        return mCameraEngine.getPictureSnapshotMetering();
    }

    /**
     * Whether the engine should perform a metering sequence before taking pictures requested
     * with {@link #takePictureSnapshot()}. A metering sequence includes adjusting focus,
     * exposure and white balance to ensure a good quality of the result.
     * <p>
     * When this parameter is true, the quality of the picture increases, but the latency
     * increases as well. To keep snapshots fast, this defaults to false.
     * <p>
     * This is a CAMERA2 only API. On CAMERA1, picture snapshot metering is always disabled.
     *
     * @param enable true to enable
     * @see #setPictureMetering(boolean)
     */
    public void setPictureSnapshotMetering(boolean enable) {
        mCameraEngine.setPictureSnapshotMetering(enable);
    }

    /**
     * Returns the current video bit rate.
     *
     * @return current bit rate
     */
    @SuppressWarnings("unused")
    public int getVideoBitRate() {
        return mCameraEngine.getVideoBitRate();
    }

    /**
     * Sets the bit rate in bits per second for video capturing.
     * Will be used by both {@link #takeVideo(File)} and {@link #takeVideoSnapshot(File)}.
     *
     * @param bitRate desired bit rate
     */
    public void setVideoBitRate(int bitRate) {
        mCameraEngine.setVideoBitRate(bitRate);
    }

    /**
     * Returns the current audio bit rate.
     *
     * @return current bit rate
     */
    @SuppressWarnings("unused")
    public int getAudioBitRate() {
        return mCameraEngine.getAudioBitRate();
    }

    /**
     * Sets the bit rate in bits per second for audio capturing.
     * Will be used by both {@link #takeVideo(File)} and {@link #takeVideoSnapshot(File)}.
     *
     * @param bitRate desired bit rate
     */
    public void setAudioBitRate(int bitRate) {
        mCameraEngine.setAudioBitRate(bitRate);
    }

    /**
     * Adds a {@link CameraListener} instance to be notified of all
     * interesting events that happen during the camera lifecycle.
     *
     * @param cameraListener a listener for events.
     */
    public void addCameraListener(@NonNull CameraListener cameraListener) {
        mListeners.add(cameraListener);
    }

    /**
     * Remove a {@link CameraListener} that was previously registered.
     *
     * @param cameraListener a listener for events.
     */
    public void removeCameraListener(@NonNull CameraListener cameraListener) {
        mListeners.remove(cameraListener);
    }

    /**
     * Clears the list of {@link CameraListener} that are registered
     * to camera events.
     */
    public void clearCameraListeners() {
        mListeners.clear();
    }

    /**
     * Adds a {@link FrameProcessor} instance to be notified of
     * new frames in the preview stream.
     *
     * @param processor a frame processor.
     */
    public void addFrameProcessor(@Nullable FrameProcessor processor) {
        if (processor != null) {
            mFrameProcessors.add(processor);
            if (mFrameProcessors.size() == 1) {
                mCameraEngine.setHasFrameProcessors(true);
            }
        }
    }

    /**
     * Remove a {@link FrameProcessor} that was previously registered.
     *
     * @param processor a frame processor
     */
    public void removeFrameProcessor(@Nullable FrameProcessor processor) {
        if (processor != null) {
            mFrameProcessors.remove(processor);
            if (mFrameProcessors.size() == 0) {
                mCameraEngine.setHasFrameProcessors(false);
            }
        }
    }

    /**
     * Clears the list of {@link FrameProcessor} that have been registered
     * to preview frames.
     */
    public void clearFrameProcessors() {
        boolean had = mFrameProcessors.size() > 0;
        mFrameProcessors.clear();
        if (had) {
            mCameraEngine.setHasFrameProcessors(false);
        }
    }

    /**
     * Asks the camera to capture an image of the current scene.
     * This will trigger {@link CameraListener#onPictureTaken(PictureResult)} if a listener
     * was registered.
     * <p>
     * Note that if sessionType is {@link Mode#VIDEO}, this
     * might fall back to {@link #takePictureSnapshot()} (that is, we might capture a preview frame).
     *
     * @see #takePictureSnapshot()
     */
    public void takePicture() {
        PictureResult.Stub stub = new PictureResult.Stub();
        mCameraEngine.takePicture(stub);
    }

    public void getcamerapreview() {
        PictureResult.Stub stub = new PictureResult.Stub();
        // mCameraEngine.get(stub);
    }

    /**
     * Asks the camera to capture a snapshot of the current preview.
     * This eventually triggers {@link CameraListener#onPictureTaken(PictureResult)} if a listener
     * was registered.
     * <p>
     * The difference with {@link #takePicture()} is that this capture is faster, so it might be
     * better on slower cameras, though the result can be generally blurry or low quality.
     *
     * @see #takePicture()
     */
    public void takePictureSnapshot() {
        PictureResult.Stub stub = new PictureResult.Stub();
        mCameraEngine.takePictureSnapshot(stub);
    }

    /**
     * Starts recording a video. Video will be written to the given file,
     * so callers should ensure they have appropriate permissions to write to the file.
     *
     * @param file a file where the video will be saved
     */
    public void takeVideo(@NonNull File file) {
        VideoResult.Stub stub = new VideoResult.Stub();
        mCameraEngine.takeVideo(stub, file);
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mKeepScreenOn = getKeepScreenOn();
                if (!mKeepScreenOn) setKeepScreenOn(true);
            }
        });
    }

    /**
     * Starts recording a fast, low quality video snapshot. Video will be written to the given file,
     * so callers should ensure they have appropriate permissions to write to the file.
     * <p>
     * Throws an exception if API level is below 18, or if the preview being used is not
     * {@link Preview#GL_SURFACE}.
     *
     * @param file a file where the video will be saved
     */
    public void takeVideoSnapshot(@NonNull File file) {
        VideoResult.Stub stub = new VideoResult.Stub();
        mCameraEngine.takeVideoSnapshot(stub, file);
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mKeepScreenOn = getKeepScreenOn();
                if (!mKeepScreenOn) setKeepScreenOn(true);
            }
        });
    }

    /**
     * Starts recording a video. Video will be written to the given file,
     * so callers should ensure they have appropriate permissions to write to the file.
     * Recording will be automatically stopped after the given duration, overriding
     * temporarily any duration limit set by {@link #setVideoMaxDuration(int)}.
     *
     * @param file           a file where the video will be saved
     * @param durationMillis recording max duration
     */
    public void takeVideo(@NonNull File file, int durationMillis) {
        final int old = getVideoMaxDuration();
        addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                setVideoMaxDuration(old);
                removeCameraListener(this);
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
                if (exception.getReason() == CameraException.REASON_VIDEO_FAILED) {
                    setVideoMaxDuration(old);
                    removeCameraListener(this);
                }
            }
        });
        setVideoMaxDuration(durationMillis);
        takeVideo(file);
    }

    /**
     * Starts recording a fast, low quality video snapshot. Video will be written to the given file,
     * so callers should ensure they have appropriate permissions to write to the file.
     * Recording will be automatically stopped after the given duration, overriding
     * temporarily any duration limit set by {@link #setVideoMaxDuration(int)}.
     * <p>
     * Throws an exception if API level is below 18, or if the preview being used is not
     * {@link Preview#GL_SURFACE}.
     *
     * @param file           a file where the video will be saved
     * @param durationMillis recording max duration
     */
    public void takeVideoSnapshot(@NonNull File file, int durationMillis) {
        final int old = getVideoMaxDuration();
        addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                setVideoMaxDuration(old);
                removeCameraListener(this);
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
                if (exception.getReason() == CameraException.REASON_VIDEO_FAILED) {
                    setVideoMaxDuration(old);
                    removeCameraListener(this);
                }
            }
        });
        setVideoMaxDuration(durationMillis);
        takeVideoSnapshot(file);
    }

    /**
     * Stops capturing video or video snapshots being recorded, if there was any.
     * This will fire {@link CameraListener#onVideoTaken(VideoResult)}.
     */
    public void stopVideo() {
        mCameraEngine.stopVideo();
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getKeepScreenOn() != mKeepScreenOn) setKeepScreenOn(mKeepScreenOn);
            }
        });
    }

    /**
     * Sets the max width for snapshots taken with {@link #takePictureSnapshot()} or
     * {@link #takeVideoSnapshot(File)}. If the snapshot width exceeds this value, the snapshot
     * will be scaled down to match this constraint.
     *
     * @param maxWidth max width for snapshots
     */
    public void setSnapshotMaxWidth(int maxWidth) {
        mCameraEngine.setSnapshotMaxWidth(maxWidth);
    }

    // TODO: pauseVideo and resumeVideo? There is mediarecorder.pause(), but API 24...

    /**
     * Sets the max height for snapshots taken with {@link #takePictureSnapshot()} or
     * {@link #takeVideoSnapshot(File)}. If the snapshot height exceeds this value, the snapshot
     * will be scaled down to match this constraint.
     *
     * @param maxHeight max height for snapshots
     */
    public void setSnapshotMaxHeight(int maxHeight) {
        mCameraEngine.setSnapshotMaxHeight(maxHeight);
    }

    /**
     * Returns the size used for snapshots, or null if it hasn't been computed
     * (for example if the surface is not ready). This is the preview size, rotated to match
     * the output orientation, and cropped to the visible part.
     * <p>
     * This also includes the {@link #setSnapshotMaxWidth(int)} and
     * {@link #setSnapshotMaxHeight(int)} constraints.
     * <p>
     * This does NOT include any constraints specific to video encoding, which are
     * device specific and depend on the capabilities of the device codec.
     *
     * @return the size of snapshots
     */
    @Nullable
    public Size getSnapshotSize() {
        if (getWidth() == 0 || getHeight() == 0) return null;

        // Get the preview size and crop according to the current view size.
        // It's better to do calculations in the REF_VIEW reference, and then flip if needed.
        Size preview = mCameraEngine.getUncroppedSnapshotSize(Reference.VIEW);
        if (preview == null) return null; // Should never happen.
        AspectRatio viewRatio = AspectRatio.of(getWidth(), getHeight());
        Rect crop = CropHelper.computeCrop(preview, viewRatio);
        Size cropSize = new Size(crop.width(), crop.height());
        if (mCameraEngine.getAngles().flip(Reference.VIEW, Reference.OUTPUT)) {
            return cropSize.flip();
        } else {
            return cropSize;
        }
    }

    /**
     * Returns the size used for pictures taken with {@link #takePicture()},
     * or null if it hasn't been computed (for example if the surface is not ready),
     * or null if we are in video mode.
     * <p>
     * The size is rotated to match the output orientation.
     *
     * @return the size of pictures
     */
    @Nullable
    public Size getPictureSize() {
        return mCameraEngine.getPictureSize(Reference.OUTPUT);
    }

    /**
     * Sets a capture size selector for picture mode.
     * The {@link SizeSelector} will be invoked with the list of available sizes, and the first
     * acceptable size will be accepted and passed to the internal engine.
     * See the {@link SizeSelectors} class for handy utilities for creating selectors.
     *
     * @param selector a size selector
     */
    public void setPictureSize(@NonNull SizeSelector selector) {
        mCameraEngine.setPictureSizeSelector(selector);
    }

    /**
     * Returns the size used for videos taken with {@link #takeVideo(File)},
     * or null if it hasn't been computed (for example if the surface is not ready),
     * or null if we are in picture mode.
     * <p>
     * The size is rotated to match the output orientation.
     *
     * @return the size of videos
     */
    @Nullable
    public Size getVideoSize() {
        return mCameraEngine.getVideoSize(Reference.OUTPUT);
    }

    /**
     * Sets a capture size selector for video mode.
     * The {@link SizeSelector} will be invoked with the list of available sizes, and the first
     * acceptable size will be accepted and passed to the internal engine.
     * See the {@link SizeSelectors} class for handy utilities for creating selectors.
     *
     * @param selector a size selector
     */
    public void setVideoSize(@NonNull SizeSelector selector) {
        mCameraEngine.setVideoSizeSelector(selector);
    }

    // If we end up here, we're in M.
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions(boolean requestCamera, boolean requestAudio) {
        Activity activity = null;
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        List<String> permissions = new ArrayList<>();
        if (requestCamera) permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (requestAudio) permissions.add(Manifest.permission.RECORD_AUDIO);
        if (activity != null) {
            activity.requestPermissions(permissions.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("NewApi")
    private void playSound(int soundType) {
        if (mPlaySounds) {
            if (mSound == null) mSound = new MediaActionSound();
            mSound.play(soundType);
        }
    }

    /**
     * Gets the current sound effect behavior.
     *
     * @return whether sound effects are supported
     * @see #setPlaySounds(boolean)
     */
    public boolean getPlaySounds() {
        return mPlaySounds;
    }

    /**
     * Controls whether CameraView should play sound effects on certain
     * events (picture taken, focus complete). Note that:
     * - On API level {@literal <} 16, this flag is always false
     * - Camera1 will always play the shutter sound when taking pictures
     *
     * @param playSounds whether to play sound effects
     */
    public void setPlaySounds(boolean playSounds) {
        mPlaySounds = playSounds && Build.VERSION.SDK_INT >= 16;
        mCameraEngine.setPlaySounds(playSounds);
    }

    /**
     * Gets the current behavior for considering the device orientation when returning picture
     * or video outputs.
     *
     * @return whether we are using the device orientation for outputs
     * @see #setUseDeviceOrientation(boolean)
     */
    public boolean getUseDeviceOrientation() {
        return mUseDeviceOrientation;
    }

    /**
     * Controls whether picture and video output should consider the current device orientation.
     * For example, when true, if the user rotates the device before taking a picture, the picture
     * will be rotated as well.
     *
     * @param useDeviceOrientation true to consider device orientation for outputs
     */
    public void setUseDeviceOrientation(boolean useDeviceOrientation) {
        mUseDeviceOrientation = useDeviceOrientation;
    }

    /**
     * Gets the current encoder for video recordings.
     *
     * @return the current video codec
     */
    @NonNull
    public VideoCodec getVideoCodec() {
        return mCameraEngine.getVideoCodec();
    }

    /**
     * Sets the encoder for video recordings.
     * Defaults to {@link VideoCodec#DEVICE_DEFAULT}.
     *
     * @param codec requested video codec
     * @see VideoCodec#DEVICE_DEFAULT
     * @see VideoCodec#H_263
     * @see VideoCodec#H_264
     */
    public void setVideoCodec(@NonNull VideoCodec codec) {
        mCameraEngine.setVideoCodec(codec);
    }

    /**
     * Returns the maximum size in bytes for recorded video files, or 0
     * if no size was set.
     *
     * @return the maximum size in bytes
     * @see #setVideoMaxSize(long)
     */
    public long getVideoMaxSize() {
        return mCameraEngine.getVideoMaxSize();
    }

    /**
     * Sets the maximum size in bytes for recorded video files.
     * Once this size is reached, the recording will automatically stop.
     * Defaults to unlimited size. Use 0 or negatives to disable.
     *
     * @param videoMaxSizeInBytes The maximum video size in bytes
     */
    public void setVideoMaxSize(long videoMaxSizeInBytes) {
        mCameraEngine.setVideoMaxSize(videoMaxSizeInBytes);
    }

    /**
     * Returns the maximum duration in milliseconds for video recordings, or 0
     * if no limit was set.
     *
     * @return the maximum duration in milliseconds
     * @see #setVideoMaxDuration(int)
     */
    public int getVideoMaxDuration() {
        return mCameraEngine.getVideoMaxDuration();
    }

    /**
     * Sets the maximum duration in milliseconds for video recordings.
     * Once this duration is reached, the recording will automatically stop.
     * Defaults to unlimited duration. Use 0 or negatives to disable.
     *
     * @param videoMaxDurationMillis The maximum video duration in milliseconds
     */
    public void setVideoMaxDuration(int videoMaxDurationMillis) {
        mCameraEngine.setVideoMaxDuration(videoMaxDurationMillis);
    }

    /**
     * Returns true if the camera is currently recording a video
     *
     * @return boolean indicating if the camera is recording a video
     */
    public boolean isTakingVideo() {
        return mCameraEngine.isTakingVideo();
    }

    /**
     * Returns true if the camera is currently capturing a picture
     *
     * @return boolean indicating if the camera is capturing a picture
     */
    public boolean isTakingPicture() {
        return mCameraEngine.isTakingPicture();
    }

    //endregion

    //region Callbacks and dispatching

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        if (!mInEditor && mOverlayLayout.isOverlay(attributeSet)) {
            return mOverlayLayout.generateLayoutParams(attributeSet);
        }
        return super.generateLayoutParams(attributeSet);
    }

    //endregion

    //region Overlays

    // We don't support removeView on overlays for now.
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!mInEditor && mOverlayLayout.isOverlay(params)) {
            mOverlayLayout.addView(child, params);
        } else {
            super.addView(child, index, params);
        }
    }

    /**
     * Returns the current real-time filter applied to the camera preview.
     * <p>
     * This method will throw an exception if the current preview does not support real-time filters.
     * Make sure you use {@link Preview#GL_SURFACE} (the default).
     *
     * @return the current filter
     * @see #setFilter(Filter)
     */
    @NonNull
    public Filter getFilter() {
        if (!mExperimental) {
            throw new RuntimeException("Filters are an experimental features and need the experimental flag set.");
        } else if (mCameraPreview == null) {
            return mPendingFilter;
        } else if (mCameraPreview instanceof FilterCameraPreview) {
            return ((FilterCameraPreview) mCameraPreview).getCurrentFilter();
        } else {
            throw new RuntimeException("Filters are only supported by the GL_SURFACE preview. Current:" + mPreview);
        }

    }

    //endregion

    //region Filters

    /**
     * Applies a real-time filter to the camera preview, if it supports it.
     * The only preview type that does so is currently {@link Preview#GL_SURFACE}.
     * <p>
     * The filter will be applied to any picture snapshot taken with
     * {@link #takePictureSnapshot()} and any video snapshot taken with
     * {@link #takeVideoSnapshot(File)}.
     * <p>
     * Use {@link NoFilter} to clear the existing filter,
     * and take a look at the {@link Filters} class for commonly used filters.
     * <p>
     * This method will throw an exception if the current preview does not support real-time filters.
     * Make sure you use {@link Preview#GL_SURFACE} (the default).
     *
     * @param filter a new filter
     * @see Filters
     */
    public void setFilter(@NonNull Filter filter) {
        if (mCameraPreview == null) {
            mPendingFilter = filter;
        } else if (!(filter instanceof NoFilter) && !mExperimental) {
            throw new RuntimeException("Filters are an experimental features and need the experimental flag set.");
        } else if (mCameraPreview instanceof FilterCameraPreview) {
            ((FilterCameraPreview) mCameraPreview).setFilter(filter);
        } else {
            throw new RuntimeException("Filters are only supported by the GL_SURFACE preview. Current:" + mPreview);
        }
    }

    @VisibleForTesting
    class CameraCallbacks implements
            CameraEngine.Callback,
            OrientationHelper.Callback,
            GestureFinder.Controller {

        private CameraLogger mLogger = CameraLogger.create(CameraCallbacks.class.getSimpleName());

        @NonNull
        @Override
        public Context getContext() {
            return CameraView.this.getContext();
        }

        @Override
        public int getWidth() {
            return CameraView.this.getWidth();
        }

        @Override
        public int getHeight() {
            return CameraView.this.getHeight();
        }

        @Override
        public void dispatchOnCameraOpened(final CameraOptions options) {
            mLogger.i("dispatchOnCameraOpened", options);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onCameraOpened(options);
                    }
                }
            });
        }

        @Override
        public void dispatchOnCameraClosed() {
            mLogger.i("dispatchOnCameraClosed");
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onCameraClosed();
                    }
                }
            });
        }

        @Override
        public void onCameraPreviewStreamSizeChanged() {
            mLogger.i("onCameraPreviewStreamSizeChanged");
            // Camera preview size has changed.
            // Request a layout pass for onMeasure() to do its stuff.
            // Potentially this will change CameraView size, which changes Surface size,
            // which triggers a new Preview size. But hopefully it will converge.
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }

        @Override
        public void onShutter(boolean shouldPlaySound) {
            if (shouldPlaySound && mPlaySounds) {
                playSound(MediaActionSound.SHUTTER_CLICK);
            }
        }

        @Override
        public void dispatchOnPictureTaken(final PictureResult.Stub stub) {
            mLogger.i("dispatchOnPictureTaken", stub);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    PictureResult result = new PictureResult(stub);
                    for (CameraListener listener : mListeners) {
                        listener.onPictureTaken(result);
                    }
                }
            });
        }

        @Override
        public void dispatchOnVideoTaken(final VideoResult.Stub stub) {
            mLogger.i("dispatchOnVideoTaken", stub);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    VideoResult result = new VideoResult(stub);
                    for (CameraListener listener : mListeners) {
                        listener.onVideoTaken(result);
                    }
                }
            });
        }

        @Override
        public void dispatchOnFocusStart(@Nullable final Gesture gesture, @NonNull final PointF point) {
            mLogger.i("dispatchOnFocusStart", gesture, point);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMarkerLayout.onEvent(MarkerLayout.TYPE_AUTOFOCUS, new PointF[]{point});
                    if (mAutoFocusMarker != null) {
                        AutoFocusTrigger trigger = gesture != null ?
                                AutoFocusTrigger.GESTURE : AutoFocusTrigger.METHOD;
                        mAutoFocusMarker.onAutoFocusStart(trigger, point);
                    }

                    for (CameraListener listener : mListeners) {
                        listener.onAutoFocusStart(point);
                    }
                }
            });
        }

        @Override
        public void dispatchOnFocusEnd(@Nullable final Gesture gesture, final boolean success,
                                       @NonNull final PointF point) {
            mLogger.i("dispatchOnFocusEnd", gesture, success, point);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (success && mPlaySounds) {
                        playSound(MediaActionSound.FOCUS_COMPLETE);
                    }

                    if (mAutoFocusMarker != null) {
                        AutoFocusTrigger trigger = gesture != null ?
                                AutoFocusTrigger.GESTURE : AutoFocusTrigger.METHOD;
                        mAutoFocusMarker.onAutoFocusEnd(trigger, success, point);
                    }

                    for (CameraListener listener : mListeners) {
                        listener.onAutoFocusEnd(success, point);
                    }
                }
            });
        }

        @Override
        public void onDeviceOrientationChanged(int deviceOrientation) {
            mLogger.i("onDeviceOrientationChanged", deviceOrientation);
            int displayOffset = mOrientationHelper.getDisplayOffset();
            if (!mUseDeviceOrientation) {
                // To fool the engine to return outputs in the VIEW reference system,
                // The device orientation should be set to -displayOffset.
                int fakeDeviceOrientation = (360 - displayOffset) % 360;
                mCameraEngine.getAngles().setDeviceOrientation(fakeDeviceOrientation);
            } else {
                mCameraEngine.getAngles().setDeviceOrientation(deviceOrientation);
            }
            final int value = (deviceOrientation + displayOffset) % 360;
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onOrientationChanged(value);
                    }
                }
            });
        }

        @Override
        public void dispatchOnZoomChanged(final float newValue, @Nullable final PointF[] fingers) {
            mLogger.i("dispatchOnZoomChanged", newValue);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onZoomChanged(newValue, new float[]{0, 1}, fingers);
                    }
                }
            });
        }

        @Override
        public void dispatchOnExposureCorrectionChanged(final float newValue,
                                                        @NonNull final float[] bounds,
                                                        @Nullable final PointF[] fingers) {
            mLogger.i("dispatchOnExposureCorrectionChanged", newValue);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onExposureCorrectionChanged(newValue, bounds, fingers);
                    }
                }
            });
        }

        @Override
        public void dispatchFrame(@NonNull final Frame frame) {
            // The getTime() below might crash if developers incorrectly release frames asynchronously.
            mLogger.v("dispatchFrame:", frame.getTime(), "processors:", mFrameProcessors.size());
            if (mFrameProcessors.isEmpty()) {
                // Mark as released. This instance will be reused.
                frame.release();
            } else {
                // Dispatch this frame to frame processors.
                mFrameProcessorsHandler.run(new Runnable() {
                    @Override
                    public void run() {
                        mLogger.v("dispatchFrame: dispatching", frame.getTime(), "to processors.");
                        for (FrameProcessor processor : mFrameProcessors) {
                            try {
                                processor.process(frame);
                            } catch (Exception e) {
                                // Don't let a single processor crash the processor thread.
                                mLogger.w("Frame processor crashed:", e);
                            }
                        }
                        frame.release();
                    }
                });
            }
        }

        @Override
        public void dispatchError(final CameraException exception) {
            mLogger.i("dispatchError", exception);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onCameraError(exception);
                    }
                }
            });
        }

        @Override
        public void dispatchOnVideoRecordingStart() {
            mLogger.i("dispatchOnVideoRecordingStart");
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onVideoRecordingStart();
                    }
                }
            });
        }

        @Override
        public void dispatchOnVideoRecordingEnd() {
            mLogger.i("dispatchOnVideoRecordingEnd");
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (CameraListener listener : mListeners) {
                        listener.onVideoRecordingEnd();
                    }
                }
            });
        }
    }

    //endregion
}
