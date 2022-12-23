package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AccompanistLibraryAccessors laccForAccompanistLibraryAccessors = new AccompanistLibraryAccessors(owner);
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final ArrowfxLibraryAccessors laccForArrowfxLibraryAccessors = new ArrowfxLibraryAccessors(owner);
    private final CoroutinesLibraryAccessors laccForCoroutinesLibraryAccessors = new CoroutinesLibraryAccessors(owner);
    private final DaggerLibraryAccessors laccForDaggerLibraryAccessors = new DaggerLibraryAccessors(owner);
    private final EspressoLibraryAccessors laccForEspressoLibraryAccessors = new EspressoLibraryAccessors(owner);
    private final ExoPlayerLibraryAccessors laccForExoPlayerLibraryAccessors = new ExoPlayerLibraryAccessors(owner);
    private final FacebookLibraryAccessors laccForFacebookLibraryAccessors = new FacebookLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final FlipperLibraryAccessors laccForFlipperLibraryAccessors = new FlipperLibraryAccessors(owner);
    private final GoogleLibraryAccessors laccForGoogleLibraryAccessors = new GoogleLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KspLibraryAccessors laccForKspLibraryAccessors = new KspLibraryAccessors(owner);
    private final KtlintLibraryAccessors laccForKtlintLibraryAccessors = new KtlintLibraryAccessors(owner);
    private final MoshiLibraryAccessors laccForMoshiLibraryAccessors = new MoshiLibraryAccessors(owner);
    private final Okhttp3LibraryAccessors laccForOkhttp3LibraryAccessors = new Okhttp3LibraryAccessors(owner);
    private final Retrofit2LibraryAccessors laccForRetrofit2LibraryAccessors = new Retrofit2LibraryAccessors(owner);
    private final RoomLibraryAccessors laccForRoomLibraryAccessors = new RoomLibraryAccessors(owner);
    private final SendbirdLibraryAccessors laccForSendbirdLibraryAccessors = new SendbirdLibraryAccessors(owner);
    private final SsoLibraryAccessors laccForSsoLibraryAccessors = new SsoLibraryAccessors(owner);
    private final TripletLibraryAccessors laccForTripletLibraryAccessors = new TripletLibraryAccessors(owner);
    private final WorkManagerLibraryAccessors laccForWorkManagerLibraryAccessors = new WorkManagerLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects) {
        super(config, providers, objects);
    }

        /**
         * Creates a dependency provider for androidTestOrchestrator (androidx.test:orchestrator)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroidTestOrchestrator() { return create("androidTestOrchestrator"); }

        /**
         * Creates a dependency provider for appsflyer (com.appsflyer:af-android-sdk)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAppsflyer() { return create("appsflyer"); }

        /**
         * Creates a dependency provider for arrow (io.arrow-kt:arrow-core)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getArrow() { return create("arrow"); }

        /**
         * Creates a dependency provider for assertk (com.willowtreeapps.assertk:assertk-jvm)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAssertk() { return create("assertk"); }

        /**
         * Creates a dependency provider for betterLinkMovement (me.saket:better-link-movement-method)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBetterLinkMovement() { return create("betterLinkMovement"); }

        /**
         * Creates a dependency provider for billing (com.android.billingclient:billing-ktx)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBilling() { return create("billing"); }

        /**
         * Creates a dependency provider for bolts (com.parse.bolts:bolts-android)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBolts() { return create("bolts"); }

        /**
         * Creates a dependency provider for braze (com.appboy:android-sdk-ui)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBraze() { return create("braze"); }

        /**
         * Creates a dependency provider for cameraCore (androidx.camera:camera-core)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCameraCore() { return create("cameraCore"); }

        /**
         * Creates a dependency provider for cameraLifecycle (androidx.camera:camera-lifecycle)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCameraLifecycle() { return create("cameraLifecycle"); }

        /**
         * Creates a dependency provider for cameraTwo (androidx.camera:camera-camera2)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCameraTwo() { return create("cameraTwo"); }

        /**
         * Creates a dependency provider for cameraVideo (androidx.camera:camera-video)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCameraVideo() { return create("cameraVideo"); }

        /**
         * Creates a dependency provider for cameraView (androidx.camera:camera-view)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCameraView() { return create("cameraView"); }

        /**
         * Creates a dependency provider for coil (io.coil-kt:coil)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCoil() { return create("coil"); }

        /**
         * Creates a dependency provider for compose (androidx.compose.foundation:foundation)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() { return create("compose"); }

        /**
         * Creates a dependency provider for composeActivity (androidx.activity:activity-compose)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeActivity() { return create("composeActivity"); }

        /**
         * Creates a dependency provider for composeCoil (io.coil-kt:coil-compose)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeCoil() { return create("composeCoil"); }

        /**
         * Creates a dependency provider for composeConstraintLayout (androidx.constraintlayout:constraintlayout-compose)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeConstraintLayout() { return create("composeConstraintLayout"); }

        /**
         * Creates a dependency provider for composeCustomView (androidx.customview:customview)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeCustomView() { return create("composeCustomView"); }

        /**
         * Creates a dependency provider for composeCustomViewPoolingContainer (androidx.customview:customview-poolingcontainer)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeCustomViewPoolingContainer() { return create("composeCustomViewPoolingContainer"); }

        /**
         * Creates a dependency provider for composeMaterial (androidx.compose.material3:material3)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeMaterial() { return create("composeMaterial"); }

        /**
         * Creates a dependency provider for composeMaterial1 (androidx.compose.material:material)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeMaterial1() { return create("composeMaterial1"); }

        /**
         * Creates a dependency provider for composeTooling (androidx.compose.ui:ui-tooling)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeTooling() { return create("composeTooling"); }

        /**
         * Creates a dependency provider for composeToolingPreview (androidx.compose.ui:ui-tooling-preview)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getComposeToolingPreview() { return create("composeToolingPreview"); }

        /**
         * Creates a dependency provider for desugar (com.android.tools:desugar_jdk_libs)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDesugar() { return create("desugar"); }

        /**
         * Creates a dependency provider for faker (io.github.serpro69:kotlin-faker)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFaker() { return create("faker"); }

        /**
         * Creates a dependency provider for flexbox (com.google.android.flexbox:flexbox)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFlexbox() { return create("flexbox"); }

        /**
         * Creates a dependency provider for googleAuth (com.google.android.gms:play-services-auth)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGoogleAuth() { return create("googleAuth"); }

        /**
         * Creates a dependency provider for googleAuthPhone (com.google.android.gms:play-services-auth-api-phone)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGoogleAuthPhone() { return create("googleAuthPhone"); }

        /**
         * Creates a dependency provider for googleLocation (com.google.android.gms:play-services-location)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGoogleLocation() { return create("googleLocation"); }

        /**
         * Creates a dependency provider for googleMaps (com.google.android.gms:play-services-maps)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGoogleMaps() { return create("googleMaps"); }

        /**
         * Creates a dependency provider for installreferrer (com.android.installreferrer:installreferrer)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getInstallreferrer() { return create("installreferrer"); }

        /**
         * Creates a dependency provider for junit (junit:junit)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() { return create("junit"); }

        /**
         * Creates a dependency provider for leakcanary (com.squareup.leakcanary:leakcanary-android)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLeakcanary() { return create("leakcanary"); }

        /**
         * Creates a dependency provider for lottie (com.airbnb.android:lottie)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLottie() { return create("lottie"); }

        /**
         * Creates a dependency provider for materialDesign (com.google.android.material:material)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterialDesign() { return create("materialDesign"); }

        /**
         * Creates a dependency provider for mockk (io.mockk:mockk)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMockk() { return create("mockk"); }

        /**
         * Creates a dependency provider for mp4Composer (com.github.MasayukiSuda:Mp4Composer-android)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMp4Composer() { return create("mp4Composer"); }

        /**
         * Creates a dependency provider for okio (com.squareup.okio:okio)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOkio() { return create("okio"); }

        /**
         * Creates a dependency provider for playCore (com.google.android.play:core)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlayCore() { return create("playCore"); }

        /**
         * Creates a dependency provider for playCoreKtx (com.google.android.play:core-ktx)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlayCoreKtx() { return create("playCoreKtx"); }

        /**
         * Creates a dependency provider for robolectric (org.robolectric:robolectric)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRobolectric() { return create("robolectric"); }

        /**
         * Creates a dependency provider for roomktx (androidx.room:room-ktx)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRoomktx() { return create("roomktx"); }

        /**
         * Creates a dependency provider for split (io.split.client:android-client)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSplit() { return create("split"); }

        /**
         * Creates a dependency provider for timber (com.jakewharton.timber:timber)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTimber() { return create("timber"); }

        /**
         * Creates a dependency provider for turbine (app.cash.turbine:turbine)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTurbine() { return create("turbine"); }

        /**
         * Creates a dependency provider for uiAutomator (androidx.test.uiautomator:uiautomator)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getUiAutomator() { return create("uiAutomator"); }

        /**
         * Creates a dependency provider for workManagerTest (androidx.work:work-testing)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWorkManagerTest() { return create("workManagerTest"); }

    /**
     * Returns the group of libraries at accompanist
     */
    public AccompanistLibraryAccessors getAccompanist() { return laccForAccompanistLibraryAccessors; }

    /**
     * Returns the group of libraries at android
     */
    public AndroidLibraryAccessors getAndroid() { return laccForAndroidLibraryAccessors; }

    /**
     * Returns the group of libraries at androidx
     */
    public AndroidxLibraryAccessors getAndroidx() { return laccForAndroidxLibraryAccessors; }

    /**
     * Returns the group of libraries at arrowfx
     */
    public ArrowfxLibraryAccessors getArrowfx() { return laccForArrowfxLibraryAccessors; }

    /**
     * Returns the group of libraries at coroutines
     */
    public CoroutinesLibraryAccessors getCoroutines() { return laccForCoroutinesLibraryAccessors; }

    /**
     * Returns the group of libraries at dagger
     */
    public DaggerLibraryAccessors getDagger() { return laccForDaggerLibraryAccessors; }

    /**
     * Returns the group of libraries at espresso
     */
    public EspressoLibraryAccessors getEspresso() { return laccForEspressoLibraryAccessors; }

    /**
     * Returns the group of libraries at exoPlayer
     */
    public ExoPlayerLibraryAccessors getExoPlayer() { return laccForExoPlayerLibraryAccessors; }

    /**
     * Returns the group of libraries at facebook
     */
    public FacebookLibraryAccessors getFacebook() { return laccForFacebookLibraryAccessors; }

    /**
     * Returns the group of libraries at firebase
     */
    public FirebaseLibraryAccessors getFirebase() { return laccForFirebaseLibraryAccessors; }

    /**
     * Returns the group of libraries at flipper
     */
    public FlipperLibraryAccessors getFlipper() { return laccForFlipperLibraryAccessors; }

    /**
     * Returns the group of libraries at google
     */
    public GoogleLibraryAccessors getGoogle() { return laccForGoogleLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() { return laccForKotlinLibraryAccessors; }

    /**
     * Returns the group of libraries at ksp
     */
    public KspLibraryAccessors getKsp() { return laccForKspLibraryAccessors; }

    /**
     * Returns the group of libraries at ktlint
     */
    public KtlintLibraryAccessors getKtlint() { return laccForKtlintLibraryAccessors; }

    /**
     * Returns the group of libraries at moshi
     */
    public MoshiLibraryAccessors getMoshi() { return laccForMoshiLibraryAccessors; }

    /**
     * Returns the group of libraries at okhttp3
     */
    public Okhttp3LibraryAccessors getOkhttp3() { return laccForOkhttp3LibraryAccessors; }

    /**
     * Returns the group of libraries at retrofit2
     */
    public Retrofit2LibraryAccessors getRetrofit2() { return laccForRetrofit2LibraryAccessors; }

    /**
     * Returns the group of libraries at room
     */
    public RoomLibraryAccessors getRoom() { return laccForRoomLibraryAccessors; }

    /**
     * Returns the group of libraries at sendbird
     */
    public SendbirdLibraryAccessors getSendbird() { return laccForSendbirdLibraryAccessors; }

    /**
     * Returns the group of libraries at sso
     */
    public SsoLibraryAccessors getSso() { return laccForSsoLibraryAccessors; }

    /**
     * Returns the group of libraries at triplet
     */
    public TripletLibraryAccessors getTriplet() { return laccForTripletLibraryAccessors; }

    /**
     * Returns the group of libraries at workManager
     */
    public WorkManagerLibraryAccessors getWorkManager() { return laccForWorkManagerLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AccompanistLibraryAccessors extends SubDependencyFactory {
        private final AccompanistPagerLibraryAccessors laccForAccompanistPagerLibraryAccessors = new AccompanistPagerLibraryAccessors(owner);

        public AccompanistLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at accompanist.pager
         */
        public AccompanistPagerLibraryAccessors getPager() { return laccForAccompanistPagerLibraryAccessors; }

    }

    public static class AccompanistPagerLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AccompanistPagerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for pager (com.google.accompanist:accompanist-pager)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("accompanist.pager"); }

            /**
             * Creates a dependency provider for indicator (com.google.accompanist:accompanist-pager-indicators)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getIndicator() { return create("accompanist.pager.indicator"); }

    }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {
        private final AndroidGradleLibraryAccessors laccForAndroidGradleLibraryAccessors = new AndroidGradleLibraryAccessors(owner);

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (com.android.tools.build:gradle)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("android.gradlePlugin"); }

            /**
             * Creates a dependency provider for gradlePluginApi (com.android.tools.build:gradle)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePluginApi() { return create("android.gradlePluginApi"); }

        /**
         * Returns the group of libraries at android.gradle
         */
        public AndroidGradleLibraryAccessors getGradle() { return laccForAndroidGradleLibraryAccessors; }

    }

    public static class AndroidGradleLibraryAccessors extends SubDependencyFactory {
        private final AndroidGradleBuildLibraryAccessors laccForAndroidGradleBuildLibraryAccessors = new AndroidGradleBuildLibraryAccessors(owner);

        public AndroidGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at android.gradle.build
         */
        public AndroidGradleBuildLibraryAccessors getBuild() { return laccForAndroidGradleBuildLibraryAccessors; }

    }

    public static class AndroidGradleBuildLibraryAccessors extends SubDependencyFactory {
        private final AndroidGradleBuildCacheLibraryAccessors laccForAndroidGradleBuildCacheLibraryAccessors = new AndroidGradleBuildCacheLibraryAccessors(owner);

        public AndroidGradleBuildLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at android.gradle.build.cache
         */
        public AndroidGradleBuildCacheLibraryAccessors getCache() { return laccForAndroidGradleBuildCacheLibraryAccessors; }

    }

    public static class AndroidGradleBuildCacheLibraryAccessors extends SubDependencyFactory {
        private final AndroidGradleBuildCacheFixLibraryAccessors laccForAndroidGradleBuildCacheFixLibraryAccessors = new AndroidGradleBuildCacheFixLibraryAccessors(owner);

        public AndroidGradleBuildCacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at android.gradle.build.cache.fix
         */
        public AndroidGradleBuildCacheFixLibraryAccessors getFix() { return laccForAndroidGradleBuildCacheFixLibraryAccessors; }

    }

    public static class AndroidGradleBuildCacheFixLibraryAccessors extends SubDependencyFactory {

        public AndroidGradleBuildCacheFixLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (org.gradle.android.cache-fix:org.gradle.android.cache-fix.gradle.plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("android.gradle.build.cache.fix.gradlePlugin"); }

    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxArchLibraryAccessors laccForAndroidxArchLibraryAccessors = new AndroidxArchLibraryAccessors(owner);
        private final AndroidxConcurrentLibraryAccessors laccForAndroidxConcurrentLibraryAccessors = new AndroidxConcurrentLibraryAccessors(owner);
        private final AndroidxFragmentLibraryAccessors laccForAndroidxFragmentLibraryAccessors = new AndroidxFragmentLibraryAccessors(owner);
        private final AndroidxHiltLibraryAccessors laccForAndroidxHiltLibraryAccessors = new AndroidxHiltLibraryAccessors(owner);
        private final AndroidxNavigationLibraryAccessors laccForAndroidxNavigationLibraryAccessors = new AndroidxNavigationLibraryAccessors(owner);
        private final AndroidxTestLibraryAccessors laccForAndroidxTestLibraryAccessors = new AndroidxTestLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for activity (androidx.activity:activity-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getActivity() { return create("androidx.activity"); }

            /**
             * Creates a dependency provider for appcompat (androidx.appcompat:appcompat)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAppcompat() { return create("androidx.appcompat"); }

            /**
             * Creates a dependency provider for browser (androidx.browser:browser)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBrowser() { return create("androidx.browser"); }

            /**
             * Creates a dependency provider for constraintLayout (androidx.constraintlayout:constraintlayout)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getConstraintLayout() { return create("androidx.constraintLayout"); }

            /**
             * Creates a dependency provider for core (androidx.core:core-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.core"); }

            /**
             * Creates a dependency provider for lifecycle (androidx.lifecycle:lifecycle-runtime-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLifecycle() { return create("androidx.lifecycle"); }

            /**
             * Creates a dependency provider for lifecycleCommonJava8 (androidx.lifecycle:lifecycle-common-java8)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLifecycleCommonJava8() { return create("androidx.lifecycleCommonJava8"); }

            /**
             * Creates a dependency provider for lifecycleLivedataKtx (androidx.lifecycle:lifecycle-livedata-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLifecycleLivedataKtx() { return create("androidx.lifecycleLivedataKtx"); }

            /**
             * Creates a dependency provider for lifecycleProcess (androidx.lifecycle:lifecycle-process)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLifecycleProcess() { return create("androidx.lifecycleProcess"); }

            /**
             * Creates a dependency provider for lifecycleViewModelCompose (androidx.lifecycle:lifecycle-viewmodel-compose)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLifecycleViewModelCompose() { return create("androidx.lifecycleViewModelCompose"); }

            /**
             * Creates a dependency provider for recyclerview (androidx.recyclerview:recyclerview)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRecyclerview() { return create("androidx.recyclerview"); }

            /**
             * Creates a dependency provider for savedState (androidx.savedstate:savedstate)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSavedState() { return create("androidx.savedState"); }

            /**
             * Creates a dependency provider for sharetarget (androidx.sharetarget:sharetarget)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSharetarget() { return create("androidx.sharetarget"); }

            /**
             * Creates a dependency provider for startup (androidx.startup:startup-runtime)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getStartup() { return create("androidx.startup"); }

            /**
             * Creates a dependency provider for transition (androidx.transition:transition-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTransition() { return create("androidx.transition"); }

            /**
             * Creates a dependency provider for viewmodel (androidx.lifecycle:lifecycle-viewmodel-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getViewmodel() { return create("androidx.viewmodel"); }

        /**
         * Returns the group of libraries at androidx.arch
         */
        public AndroidxArchLibraryAccessors getArch() { return laccForAndroidxArchLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.concurrent
         */
        public AndroidxConcurrentLibraryAccessors getConcurrent() { return laccForAndroidxConcurrentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.fragment
         */
        public AndroidxFragmentLibraryAccessors getFragment() { return laccForAndroidxFragmentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.hilt
         */
        public AndroidxHiltLibraryAccessors getHilt() { return laccForAndroidxHiltLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.navigation
         */
        public AndroidxNavigationLibraryAccessors getNavigation() { return laccForAndroidxNavigationLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.test
         */
        public AndroidxTestLibraryAccessors getTest() { return laccForAndroidxTestLibraryAccessors; }

    }

    public static class AndroidxArchLibraryAccessors extends SubDependencyFactory {

        public AndroidxArchLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for coreTesting (androidx.arch.core:core-testing)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCoreTesting() { return create("androidx.arch.coreTesting"); }

    }

    public static class AndroidxConcurrentLibraryAccessors extends SubDependencyFactory {

        public AndroidxConcurrentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for futures (androidx.concurrent:concurrent-futures-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFutures() { return create("androidx.concurrent.futures"); }

    }

    public static class AndroidxFragmentLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxFragmentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for fragment (androidx.fragment:fragment-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.fragment"); }

            /**
             * Creates a dependency provider for testing (androidx.fragment:fragment-testing)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("androidx.fragment.testing"); }

    }

    public static class AndroidxHiltLibraryAccessors extends SubDependencyFactory {

        public AndroidxHiltLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (androidx.hilt:hilt-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("androidx.hilt.compiler"); }

            /**
             * Creates a dependency provider for navigation (androidx.hilt:hilt-navigation-fragment)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getNavigation() { return create("androidx.hilt.navigation"); }

            /**
             * Creates a dependency provider for work (androidx.hilt:hilt-work)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWork() { return create("androidx.hilt.work"); }

    }

    public static class AndroidxNavigationLibraryAccessors extends SubDependencyFactory {
        private final AndroidxNavigationFragmentLibraryAccessors laccForAndroidxNavigationFragmentLibraryAccessors = new AndroidxNavigationFragmentLibraryAccessors(owner);
        private final AndroidxNavigationUiLibraryAccessors laccForAndroidxNavigationUiLibraryAccessors = new AndroidxNavigationUiLibraryAccessors(owner);

        public AndroidxNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at androidx.navigation.fragment
         */
        public AndroidxNavigationFragmentLibraryAccessors getFragment() { return laccForAndroidxNavigationFragmentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.navigation.ui
         */
        public AndroidxNavigationUiLibraryAccessors getUi() { return laccForAndroidxNavigationUiLibraryAccessors; }

    }

    public static class AndroidxNavigationFragmentLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationFragmentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for extensions (androidx.navigation:navigation-fragment-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getExtensions() { return create("androidx.navigation.fragment.extensions"); }

    }

    public static class AndroidxNavigationUiLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for extensions (androidx.navigation:navigation-ui-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getExtensions() { return create("androidx.navigation.ui.extensions"); }

    }

    public static class AndroidxTestLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final AndroidxTestExtLibraryAccessors laccForAndroidxTestExtLibraryAccessors = new AndroidxTestExtLibraryAccessors(owner);

        public AndroidxTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for test (androidx.arch.core:core-testing)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.test"); }

            /**
             * Creates a dependency provider for core (androidx.test:core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.test.core"); }

            /**
             * Creates a dependency provider for rules (androidx.test:rules)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRules() { return create("androidx.test.rules"); }

            /**
             * Creates a dependency provider for runner (androidx.test:runner)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRunner() { return create("androidx.test.runner"); }

        /**
         * Returns the group of libraries at androidx.test.ext
         */
        public AndroidxTestExtLibraryAccessors getExt() { return laccForAndroidxTestExtLibraryAccessors; }

    }

    public static class AndroidxTestExtLibraryAccessors extends SubDependencyFactory {

        public AndroidxTestExtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (androidx.test.ext:junit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJunit() { return create("androidx.test.ext.junit"); }

    }

    public static class ArrowfxLibraryAccessors extends SubDependencyFactory {

        public ArrowfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for coroutines (io.arrow-kt:arrow-fx-coroutines)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCoroutines() { return create("arrowfx.coroutines"); }

    }

    public static class CoroutinesLibraryAccessors extends SubDependencyFactory {

        public CoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.jetbrains.kotlinx:kotlinx-coroutines-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("coroutines.android"); }

            /**
             * Creates a dependency provider for core (org.jetbrains.kotlinx:kotlinx-coroutines-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("coroutines.core"); }

            /**
             * Creates a dependency provider for test (org.jetbrains.kotlinx:kotlinx-coroutines-test)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("coroutines.test"); }

    }

    public static class DaggerLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final DaggerHiltLibraryAccessors laccForDaggerHiltLibraryAccessors = new DaggerHiltLibraryAccessors(owner);

        public DaggerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for dagger (com.google.dagger:dagger)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("dagger"); }

            /**
             * Creates a dependency provider for compiler (com.google.dagger:dagger-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("dagger.compiler"); }

        /**
         * Returns the group of libraries at dagger.hilt
         */
        public DaggerHiltLibraryAccessors getHilt() { return laccForDaggerHiltLibraryAccessors; }

    }

    public static class DaggerHiltLibraryAccessors extends SubDependencyFactory {
        private final DaggerHiltAndroidLibraryAccessors laccForDaggerHiltAndroidLibraryAccessors = new DaggerHiltAndroidLibraryAccessors(owner);

        public DaggerHiltLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (com.google.dagger:hilt-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("dagger.hilt.compiler"); }

        /**
         * Returns the group of libraries at dagger.hilt.android
         */
        public DaggerHiltAndroidLibraryAccessors getAndroid() { return laccForDaggerHiltAndroidLibraryAccessors; }

    }

    public static class DaggerHiltAndroidLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public DaggerHiltAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.google.dagger:hilt-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("dagger.hilt.android"); }

            /**
             * Creates a dependency provider for compiler (com.google.dagger:hilt-android-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("dagger.hilt.android.compiler"); }

            /**
             * Creates a dependency provider for gradlePlugin (com.google.dagger:hilt-android-gradle-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("dagger.hilt.android.gradlePlugin"); }

            /**
             * Creates a dependency provider for testing (com.google.dagger:hilt-android-testing)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("dagger.hilt.android.testing"); }

    }

    public static class EspressoLibraryAccessors extends SubDependencyFactory {

        public EspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test.espresso:espresso-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("espresso.core"); }

    }

    public static class ExoPlayerLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public ExoPlayerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for exoPlayer (com.google.android.exoplayer:exoplayer)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("exoPlayer"); }

            /**
             * Creates a dependency provider for okhttp (com.google.android.exoplayer:extension-okhttp)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getOkhttp() { return create("exoPlayer.okhttp"); }

    }

    public static class FacebookLibraryAccessors extends SubDependencyFactory {

        public FacebookLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for login (com.facebook.android:facebook-login)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLogin() { return create("facebook.login"); }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {
        private final FirebaseCrashlyticsLibraryAccessors laccForFirebaseCrashlyticsLibraryAccessors = new FirebaseCrashlyticsLibraryAccessors(owner);
        private final FirebasePrefLibraryAccessors laccForFirebasePrefLibraryAccessors = new FirebasePrefLibraryAccessors(owner);

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for analytics (com.google.firebase:firebase-analytics)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAnalytics() { return create("firebase.analytics"); }

            /**
             * Creates a dependency provider for auth (com.google.firebase:firebase-auth-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAuth() { return create("firebase.auth"); }

            /**
             * Creates a dependency provider for messaging (com.google.firebase:firebase-messaging)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMessaging() { return create("firebase.messaging"); }

        /**
         * Returns the group of libraries at firebase.crashlytics
         */
        public FirebaseCrashlyticsLibraryAccessors getCrashlytics() { return laccForFirebaseCrashlyticsLibraryAccessors; }

        /**
         * Returns the group of libraries at firebase.pref
         */
        public FirebasePrefLibraryAccessors getPref() { return laccForFirebasePrefLibraryAccessors; }

    }

    public static class FirebaseCrashlyticsLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public FirebaseCrashlyticsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for crashlytics (com.google.firebase:firebase-crashlytics)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("firebase.crashlytics"); }

            /**
             * Creates a dependency provider for gradlePlugin (com.google.firebase:firebase-crashlytics-gradle)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("firebase.crashlytics.gradlePlugin"); }

            /**
             * Creates a dependency provider for ndk (com.google.firebase:firebase-crashlytics-ndk)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getNdk() { return create("firebase.crashlytics.ndk"); }

    }

    public static class FirebasePrefLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public FirebasePrefLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for pref (com.google.firebase:firebase-perf)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("firebase.pref"); }

            /**
             * Creates a dependency provider for gradlePlugin (com.google.firebase:perf-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("firebase.pref.gradlePlugin"); }

    }

    public static class FlipperLibraryAccessors extends SubDependencyFactory {
        private final FlipperLeakcanaryLibraryAccessors laccForFlipperLeakcanaryLibraryAccessors = new FlipperLeakcanaryLibraryAccessors(owner);
        private final FlipperNetworkLibraryAccessors laccForFlipperNetworkLibraryAccessors = new FlipperNetworkLibraryAccessors(owner);

        public FlipperLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.facebook.flipper:flipper)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("flipper.core"); }

        /**
         * Returns the group of libraries at flipper.leakcanary
         */
        public FlipperLeakcanaryLibraryAccessors getLeakcanary() { return laccForFlipperLeakcanaryLibraryAccessors; }

        /**
         * Returns the group of libraries at flipper.network
         */
        public FlipperNetworkLibraryAccessors getNetwork() { return laccForFlipperNetworkLibraryAccessors; }

    }

    public static class FlipperLeakcanaryLibraryAccessors extends SubDependencyFactory {

        public FlipperLeakcanaryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (com.facebook.flipper:flipper-leakcanary-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("flipper.leakcanary.plugin"); }

    }

    public static class FlipperNetworkLibraryAccessors extends SubDependencyFactory {

        public FlipperNetworkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (com.facebook.flipper:flipper-network-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("flipper.network.plugin"); }

    }

    public static class GoogleLibraryAccessors extends SubDependencyFactory {
        private final GoogleSmsLibraryAccessors laccForGoogleSmsLibraryAccessors = new GoogleSmsLibraryAccessors(owner);

        public GoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at google.sms
         */
        public GoogleSmsLibraryAccessors getSms() { return laccForGoogleSmsLibraryAccessors; }

    }

    public static class GoogleSmsLibraryAccessors extends SubDependencyFactory {
        private final GoogleSmsServicesLibraryAccessors laccForGoogleSmsServicesLibraryAccessors = new GoogleSmsServicesLibraryAccessors(owner);

        public GoogleSmsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at google.sms.services
         */
        public GoogleSmsServicesLibraryAccessors getServices() { return laccForGoogleSmsServicesLibraryAccessors; }

    }

    public static class GoogleSmsServicesLibraryAccessors extends SubDependencyFactory {

        public GoogleSmsServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (com.google.gms:google-services)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("google.sms.services.gradlePlugin"); }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (org.jetbrains.kotlin:kotlin-gradle-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("kotlin.gradlePlugin"); }

            /**
             * Creates a dependency provider for reflect (org.jetbrains.kotlin:kotlin-reflect)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getReflect() { return create("kotlin.reflect"); }

            /**
             * Creates a dependency provider for stdlib (org.jetbrains.kotlin:kotlin-stdlib)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getStdlib() { return create("kotlin.stdlib"); }

            /**
             * Creates a dependency provider for test (org.jetbrains.kotlin:kotlin-test)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("kotlin.test"); }

    }

    public static class KspLibraryAccessors extends SubDependencyFactory {

        public KspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("ksp.gradlePlugin"); }

    }

    public static class KtlintLibraryAccessors extends SubDependencyFactory {

        public KtlintLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.pinterest.ktlint:ktlint-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("ktlint.core"); }

            /**
             * Creates a dependency provider for test (com.pinterest.ktlint:ktlint-test)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("ktlint.test"); }

    }

    public static class MoshiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public MoshiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for moshi (com.squareup.moshi:moshi)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("moshi"); }

            /**
             * Creates a dependency provider for codegen (com.squareup.moshi:moshi)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCodegen() { return create("moshi.codegen"); }

    }

    public static class Okhttp3LibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public Okhttp3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for okhttp3 (com.squareup.okhttp3:okhttp)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("okhttp3"); }

            /**
             * Creates a dependency provider for mockwebserver (com.squareup.okhttp3:mockwebserver)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMockwebserver() { return create("okhttp3.mockwebserver"); }

    }

    public static class Retrofit2LibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final Retrofit2ConverterLibraryAccessors laccForRetrofit2ConverterLibraryAccessors = new Retrofit2ConverterLibraryAccessors(owner);
        private final Retrofit2RetrofitLibraryAccessors laccForRetrofit2RetrofitLibraryAccessors = new Retrofit2RetrofitLibraryAccessors(owner);

        public Retrofit2LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for retrofit2 (com.squareup.retrofit2:retrofit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("retrofit2"); }

        /**
         * Returns the group of libraries at retrofit2.converter
         */
        public Retrofit2ConverterLibraryAccessors getConverter() { return laccForRetrofit2ConverterLibraryAccessors; }

        /**
         * Returns the group of libraries at retrofit2.retrofit
         */
        public Retrofit2RetrofitLibraryAccessors getRetrofit() { return laccForRetrofit2RetrofitLibraryAccessors; }

    }

    public static class Retrofit2ConverterLibraryAccessors extends SubDependencyFactory {

        public Retrofit2ConverterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for moshi (com.squareup.retrofit2:converter-moshi)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMoshi() { return create("retrofit2.converter.moshi"); }

    }

    public static class Retrofit2RetrofitLibraryAccessors extends SubDependencyFactory {

        public Retrofit2RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for mock (com.squareup.retrofit2:retrofit-mock)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMock() { return create("retrofit2.retrofit.mock"); }

    }

    public static class RoomLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public RoomLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for room (androidx.room:room-common)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("room"); }

            /**
             * Creates a dependency provider for compiler (androidx.room:room-compiler)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("room.compiler"); }

            /**
             * Creates a dependency provider for testing (androidx.room:room-testing)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("room.testing"); }

    }

    public static class SendbirdLibraryAccessors extends SubDependencyFactory {
        private final SendbirdAndroidLibraryAccessors laccForSendbirdAndroidLibraryAccessors = new SendbirdAndroidLibraryAccessors(owner);

        public SendbirdLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at sendbird.android
         */
        public SendbirdAndroidLibraryAccessors getAndroid() { return laccForSendbirdAndroidLibraryAccessors; }

    }

    public static class SendbirdAndroidLibraryAccessors extends SubDependencyFactory {

        public SendbirdAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for calls (com.sendbird.sdk:sendbird-calls)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCalls() { return create("sendbird.android.calls"); }

            /**
             * Creates a dependency provider for sdk (com.sendbird.sdk:sendbird-android-sdk)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSdk() { return create("sendbird.android.sdk"); }

    }

    public static class SsoLibraryAccessors extends SubDependencyFactory {

        public SsoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for loader (com.facebook.soloader:soloader)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLoader() { return create("sso.loader"); }

    }

    public static class TripletLibraryAccessors extends SubDependencyFactory {
        private final TripletGradleLibraryAccessors laccForTripletGradleLibraryAccessors = new TripletGradleLibraryAccessors(owner);

        public TripletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at triplet.gradle
         */
        public TripletGradleLibraryAccessors getGradle() { return laccForTripletGradleLibraryAccessors; }

    }

    public static class TripletGradleLibraryAccessors extends SubDependencyFactory {
        private final TripletGradlePlayLibraryAccessors laccForTripletGradlePlayLibraryAccessors = new TripletGradlePlayLibraryAccessors(owner);

        public TripletGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at triplet.gradle.play
         */
        public TripletGradlePlayLibraryAccessors getPlay() { return laccForTripletGradlePlayLibraryAccessors; }

    }

    public static class TripletGradlePlayLibraryAccessors extends SubDependencyFactory {
        private final TripletGradlePlayPublisherLibraryAccessors laccForTripletGradlePlayPublisherLibraryAccessors = new TripletGradlePlayPublisherLibraryAccessors(owner);

        public TripletGradlePlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at triplet.gradle.play.publisher
         */
        public TripletGradlePlayPublisherLibraryAccessors getPublisher() { return laccForTripletGradlePlayPublisherLibraryAccessors; }

    }

    public static class TripletGradlePlayPublisherLibraryAccessors extends SubDependencyFactory {

        public TripletGradlePlayPublisherLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (com.github.triplet.gradle:play-publisher)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("triplet.gradle.play.publisher.gradlePlugin"); }

    }

    public static class WorkManagerLibraryAccessors extends SubDependencyFactory {
        private final WorkManagerRuntimeLibraryAccessors laccForWorkManagerRuntimeLibraryAccessors = new WorkManagerRuntimeLibraryAccessors(owner);

        public WorkManagerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at workManager.runtime
         */
        public WorkManagerRuntimeLibraryAccessors getRuntime() { return laccForWorkManagerRuntimeLibraryAccessors; }

    }

    public static class WorkManagerRuntimeLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public WorkManagerRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for runtime (androidx.work:work-runtime)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("workManager.runtime"); }

            /**
             * Creates a dependency provider for extensions (androidx.work:work-runtime-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getExtensions() { return create("workManager.runtime.extensions"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: accompanist (0.27.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAccompanist() { return getVersion("accompanist"); }

            /**
             * Returns the version associated to this alias: activity (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getActivity() { return getVersion("activity"); }

            /**
             * Returns the version associated to this alias: androidArchCoreTesting (2.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidArchCoreTesting() { return getVersion("androidArchCoreTesting"); }

            /**
             * Returns the version associated to this alias: androidBuildToolsVersion (33.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidBuildToolsVersion() { return getVersion("androidBuildToolsVersion"); }

            /**
             * Returns the version associated to this alias: androidCompileSdkVersion (33)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidCompileSdkVersion() { return getVersion("androidCompileSdkVersion"); }

            /**
             * Returns the version associated to this alias: androidCore (1.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidCore() { return getVersion("androidCore"); }

            /**
             * Returns the version associated to this alias: androidGradlePlugin (7.4.0-rc01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidGradlePlugin() { return getVersion("androidGradlePlugin"); }

            /**
             * Returns the version associated to this alias: androidMinSdkVersion (27)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidMinSdkVersion() { return getVersion("androidMinSdkVersion"); }

            /**
             * Returns the version associated to this alias: androidTargetSdkVersion (33)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidTargetSdkVersion() { return getVersion("androidTargetSdkVersion"); }

            /**
             * Returns the version associated to this alias: androidXExtJunit (1.1.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidXExtJunit() { return getVersion("androidXExtJunit"); }

            /**
             * Returns the version associated to this alias: androidXTestCore (1.5.0-beta01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidXTestCore() { return getVersion("androidXTestCore"); }

            /**
             * Returns the version associated to this alias: androidXTestRules (1.4.1-beta01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidXTestRules() { return getVersion("androidXTestRules"); }

            /**
             * Returns the version associated to this alias: androidXTestRunner (1.5.0-beta01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroidXTestRunner() { return getVersion("androidXTestRunner"); }

            /**
             * Returns the version associated to this alias: appCompat (1.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAppCompat() { return getVersion("appCompat"); }

            /**
             * Returns the version associated to this alias: appsflyer (6.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAppsflyer() { return getVersion("appsflyer"); }

            /**
             * Returns the version associated to this alias: arrow (1.1.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getArrow() { return getVersion("arrow"); }

            /**
             * Returns the version associated to this alias: betterLinkMovement (2.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBetterLinkMovement() { return getVersion("betterLinkMovement"); }

            /**
             * Returns the version associated to this alias: billing (5.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBilling() { return getVersion("billing"); }

            /**
             * Returns the version associated to this alias: bolts (1.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBolts() { return getVersion("bolts"); }

            /**
             * Returns the version associated to this alias: braze (16.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBraze() { return getVersion("braze"); }

            /**
             * Returns the version associated to this alias: browser (1.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBrowser() { return getVersion("browser"); }

            /**
             * Returns the version associated to this alias: camerax (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCamerax() { return getVersion("camerax"); }

            /**
             * Returns the version associated to this alias: cardview (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCardview() { return getVersion("cardview"); }

            /**
             * Returns the version associated to this alias: coil (2.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCoil() { return getVersion("coil"); }

            /**
             * Returns the version associated to this alias: compose (1.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCompose() { return getVersion("compose"); }

            /**
             * Returns the version associated to this alias: composeActivityVersion (1.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeActivityVersion() { return getVersion("composeActivityVersion"); }

            /**
             * Returns the version associated to this alias: composeConstraint (1.1.0-alpha01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeConstraint() { return getVersion("composeConstraint"); }

            /**
             * Returns the version associated to this alias: composeCustomView (1.2.0-alpha02)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeCustomView() { return getVersion("composeCustomView"); }

            /**
             * Returns the version associated to this alias: composeCustomViewPoolingContainer (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeCustomViewPoolingContainer() { return getVersion("composeCustomViewPoolingContainer"); }

            /**
             * Returns the version associated to this alias: composeKotlinCompiler (1.3.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeKotlinCompiler() { return getVersion("composeKotlinCompiler"); }

            /**
             * Returns the version associated to this alias: composeMaterial1 (1.3.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getComposeMaterial1() { return getVersion("composeMaterial1"); }

            /**
             * Returns the version associated to this alias: concurrentFutures (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getConcurrentFutures() { return getVersion("concurrentFutures"); }

            /**
             * Returns the version associated to this alias: constraintLayout (2.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getConstraintLayout() { return getVersion("constraintLayout"); }

            /**
             * Returns the version associated to this alias: coroutines (1.6.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCoroutines() { return getVersion("coroutines"); }

            /**
             * Returns the version associated to this alias: dagger (2.42)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDagger() { return getVersion("dagger"); }

            /**
             * Returns the version associated to this alias: desugar (1.1.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDesugar() { return getVersion("desugar"); }

            /**
             * Returns the version associated to this alias: espresso (3.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getEspresso() { return getVersion("espresso"); }

            /**
             * Returns the version associated to this alias: exoPlayer (2.18.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getExoPlayer() { return getVersion("exoPlayer"); }

            /**
             * Returns the version associated to this alias: facebook (15.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFacebook() { return getVersion("facebook"); }

            /**
             * Returns the version associated to this alias: faker (1.11.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFaker() { return getVersion("faker"); }

            /**
             * Returns the version associated to this alias: firebaseAnalytics (21.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseAnalytics() { return getVersion("firebaseAnalytics"); }

            /**
             * Returns the version associated to this alias: firebaseAuth (19.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseAuth() { return getVersion("firebaseAuth"); }

            /**
             * Returns the version associated to this alias: firebaseCrashlytics (18.3.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseCrashlytics() { return getVersion("firebaseCrashlytics"); }

            /**
             * Returns the version associated to this alias: firebaseMessaging (22.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebaseMessaging() { return getVersion("firebaseMessaging"); }

            /**
             * Returns the version associated to this alias: firebasePerformance (20.0.6)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFirebasePerformance() { return getVersion("firebasePerformance"); }

            /**
             * Returns the version associated to this alias: flexbox (3.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFlexbox() { return getVersion("flexbox"); }

            /**
             * Returns the version associated to this alias: flipper (0.149.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFlipper() { return getVersion("flipper"); }

            /**
             * Returns the version associated to this alias: fragment (1.5.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFragment() { return getVersion("fragment"); }

            /**
             * Returns the version associated to this alias: googleAuth (19.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGoogleAuth() { return getVersion("googleAuth"); }

            /**
             * Returns the version associated to this alias: googleAuthPhone (18.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGoogleAuthPhone() { return getVersion("googleAuthPhone"); }

            /**
             * Returns the version associated to this alias: googleLocation (18.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGoogleLocation() { return getVersion("googleLocation"); }

            /**
             * Returns the version associated to this alias: googleMaps (18.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGoogleMaps() { return getVersion("googleMaps"); }

            /**
             * Returns the version associated to this alias: googlePlayCore (1.10.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGooglePlayCore() { return getVersion("googlePlayCore"); }

            /**
             * Returns the version associated to this alias: hilt (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHilt() { return getVersion("hilt"); }

            /**
             * Returns the version associated to this alias: installreferrer (2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getInstallreferrer() { return getVersion("installreferrer"); }

            /**
             * Returns the version associated to this alias: junit (4.13.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJunit() { return getVersion("junit"); }

            /**
             * Returns the version associated to this alias: kotlin (1.7.20)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: ktlintCore (0.45.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKtlintCore() { return getVersion("ktlintCore"); }

            /**
             * Returns the version associated to this alias: leakCanary (2.9.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLeakCanary() { return getVersion("leakCanary"); }

            /**
             * Returns the version associated to this alias: lifecycle (2.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLifecycle() { return getVersion("lifecycle"); }

            /**
             * Returns the version associated to this alias: lottie (5.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLottie() { return getVersion("lottie"); }

            /**
             * Returns the version associated to this alias: material2Design (1.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMaterial2Design() { return getVersion("material2Design"); }

            /**
             * Returns the version associated to this alias: material3Design (1.0.0-beta02)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMaterial3Design() { return getVersion("material3Design"); }

            /**
             * Returns the version associated to this alias: mockk (1.13.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMockk() { return getVersion("mockk"); }

            /**
             * Returns the version associated to this alias: moshi (1.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMoshi() { return getVersion("moshi"); }

            /**
             * Returns the version associated to this alias: mp4Composer (v0.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMp4Composer() { return getVersion("mp4Composer"); }

            /**
             * Returns the version associated to this alias: navigation (2.5.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getNavigation() { return getVersion("navigation"); }

            /**
             * Returns the version associated to this alias: okhttp (4.9.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOkhttp() { return getVersion("okhttp"); }

            /**
             * Returns the version associated to this alias: okio (2.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOkio() { return getVersion("okio"); }

            /**
             * Returns the version associated to this alias: recyclerview (1.2.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRecyclerview() { return getVersion("recyclerview"); }

            /**
             * Returns the version associated to this alias: retrofit (2.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRetrofit() { return getVersion("retrofit"); }

            /**
             * Returns the version associated to this alias: room (2.4.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRoom() { return getVersion("room"); }

            /**
             * Returns the version associated to this alias: savedState (1.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSavedState() { return getVersion("savedState"); }

            /**
             * Returns the version associated to this alias: sendbird (3.1.23)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSendbird() { return getVersion("sendbird"); }

            /**
             * Returns the version associated to this alias: sendbirdVideo (1.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSendbirdVideo() { return getVersion("sendbirdVideo"); }

            /**
             * Returns the version associated to this alias: sharetarget (1.2.0-rc01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSharetarget() { return getVersion("sharetarget"); }

            /**
             * Returns the version associated to this alias: showkase (1.0.0-beta14)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getShowkase() { return getVersion("showkase"); }

            /**
             * Returns the version associated to this alias: split (2.13.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSplit() { return getVersion("split"); }

            /**
             * Returns the version associated to this alias: ssoloader (0.10.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSsoloader() { return getVersion("ssoloader"); }

            /**
             * Returns the version associated to this alias: startup (1.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getStartup() { return getVersion("startup"); }

            /**
             * Returns the version associated to this alias: testOrchestrator (1.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTestOrchestrator() { return getVersion("testOrchestrator"); }

            /**
             * Returns the version associated to this alias: timber (5.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTimber() { return getVersion("timber"); }

            /**
             * Returns the version associated to this alias: transition (1.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTransition() { return getVersion("transition"); }

            /**
             * Returns the version associated to this alias: turbine (0.11.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTurbine() { return getVersion("turbine"); }

            /**
             * Returns the version associated to this alias: uiAutomator (2.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getUiAutomator() { return getVersion("uiAutomator"); }

            /**
             * Returns the version associated to this alias: workManager (2.7.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getWorkManager() { return getVersion("workManager"); }

    }

    public static class BundleAccessors extends BundleFactory {
        private final AndroidxBundleAccessors baccForAndroidxBundleAccessors = new AndroidxBundleAccessors(objects, providers, config);
        private final FlipperBundleAccessors baccForFlipperBundleAccessors = new FlipperBundleAccessors(objects, providers, config);
        private final PlayBundleAccessors baccForPlayBundleAccessors = new PlayBundleAccessors(objects, providers, config);

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

            /**
             * Creates a dependency bundle provider for arrow which is an aggregate for the following dependencies:
             * <ul>
             *    <li>io.arrow-kt:arrow-core</li>
             *    <li>io.arrow-kt:arrow-fx-coroutines</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getArrow() { return createBundle("arrow"); }

            /**
             * Creates a dependency bundle provider for camera which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.camera:camera-core</li>
             *    <li>androidx.camera:camera-camera2</li>
             *    <li>androidx.camera:camera-lifecycle</li>
             *    <li>androidx.camera:camera-video</li>
             *    <li>androidx.camera:camera-view</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCamera() { return createBundle("camera"); }

            /**
             * Creates a dependency bundle provider for coroutines which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-core</li>
             *    <li>org.jetbrains.kotlinx:kotlinx-coroutines-android</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCoroutines() { return createBundle("coroutines"); }

            /**
             * Creates a dependency bundle provider for exoPlayer which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.android.exoplayer:exoplayer</li>
             *    <li>com.google.android.exoplayer:extension-okhttp</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getExoPlayer() { return createBundle("exoPlayer"); }

            /**
             * Creates a dependency bundle provider for facebook which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.facebook.android:facebook-login</li>
             *    <li>com.parse.bolts:bolts-android</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getFacebook() { return createBundle("facebook"); }

            /**
             * Creates a dependency bundle provider for mockk which is an aggregate for the following dependencies:
             * <ul>
             *    <li>junit:junit</li>
             *    <li>io.mockk:mockk</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getMockk() { return createBundle("mockk"); }

            /**
             * Creates a dependency bundle provider for navigation which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.navigation:navigation-ui-ktx</li>
             *    <li>androidx.navigation:navigation-fragment-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getNavigation() { return createBundle("navigation"); }

            /**
             * Creates a dependency bundle provider for retrofit which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.squareup.retrofit2:retrofit</li>
             *    <li>com.squareup.retrofit2:converter-moshi</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getRetrofit() { return createBundle("retrofit"); }

            /**
             * Creates a dependency bundle provider for sendbird which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.sendbird.sdk:sendbird-android-sdk</li>
             *    <li>com.sendbird.sdk:sendbird-calls</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getSendbird() { return createBundle("sendbird"); }

            /**
             * Creates a dependency bundle provider for uiTesting which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.test.espresso:espresso-core</li>
             *    <li>androidx.test:core</li>
             *    <li>androidx.test:runner</li>
             *    <li>androidx.test:rules</li>
             *    <li>androidx.test.ext:junit</li>
             *    <li>androidx.test.uiautomator:uiautomator</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getUiTesting() { return createBundle("uiTesting"); }

            /**
             * Creates a dependency bundle provider for workManager which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.work:work-runtime</li>
             *    <li>androidx.work:work-runtime-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getWorkManager() { return createBundle("workManager"); }

        /**
         * Returns the group of bundles at bundles.androidx
         */
        public AndroidxBundleAccessors getAndroidx() { return baccForAndroidxBundleAccessors; }

        /**
         * Returns the group of bundles at bundles.flipper
         */
        public FlipperBundleAccessors getFlipper() { return baccForFlipperBundleAccessors; }

        /**
         * Returns the group of bundles at bundles.play
         */
        public PlayBundleAccessors getPlay() { return baccForPlayBundleAccessors; }

    }

    public static class AndroidxBundleAccessors extends BundleFactory {

        public AndroidxBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

            /**
             * Creates a dependency bundle provider for androidx.compose which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.compose.foundation:foundation</li>
             *    <li>androidx.compose.ui:ui-tooling-preview</li>
             *    <li>androidx.compose.material3:material3</li>
             *    <li>androidx.constraintlayout:constraintlayout-compose</li>
             *    <li>androidx.activity:activity-compose</li>
             *    <li>androidx.lifecycle:lifecycle-viewmodel-compose</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCompose() { return createBundle("androidx.compose"); }

            /**
             * Creates a dependency bundle provider for androidx.core which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.appcompat:appcompat</li>
             *    <li>androidx.core:core-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCore() { return createBundle("androidx.core"); }

            /**
             * Creates a dependency bundle provider for androidx.ui which is an aggregate for the following dependencies:
             * <ul>
             *    <li>androidx.activity:activity-ktx</li>
             *    <li>androidx.constraintlayout:constraintlayout</li>
             *    <li>androidx.fragment:fragment-ktx</li>
             *    <li>androidx.lifecycle:lifecycle-runtime-ktx</li>
             *    <li>androidx.lifecycle:lifecycle-common-java8</li>
             *    <li>androidx.lifecycle:lifecycle-livedata-ktx</li>
             *    <li>androidx.recyclerview:recyclerview</li>
             *    <li>androidx.savedstate:savedstate</li>
             *    <li>androidx.sharetarget:sharetarget</li>
             *    <li>androidx.transition:transition-ktx</li>
             *    <li>androidx.lifecycle:lifecycle-viewmodel-ktx</li>
             *    <li>com.google.android.material:material</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getUi() { return createBundle("androidx.ui"); }

    }

    public static class FlipperBundleAccessors extends BundleFactory  implements BundleNotationSupplier{
        private final FlipperWithoutBundleAccessors baccForFlipperWithoutBundleAccessors = new FlipperWithoutBundleAccessors(objects, providers, config);

        public FlipperBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

            /**
             * Creates a dependency bundle provider for flipper which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.facebook.flipper:flipper</li>
             *    <li>com.facebook.flipper:flipper-network-plugin</li>
             *    <li>com.facebook.flipper:flipper-leakcanary-plugin</li>
             *    <li>com.facebook.soloader:soloader</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> asProvider() { return createBundle("flipper"); }

        /**
         * Returns the group of bundles at bundles.flipper.without
         */
        public FlipperWithoutBundleAccessors getWithout() { return baccForFlipperWithoutBundleAccessors; }

    }

    public static class FlipperWithoutBundleAccessors extends BundleFactory {

        public FlipperWithoutBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

            /**
             * Creates a dependency bundle provider for flipper.without.leakcanary which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.facebook.flipper:flipper</li>
             *    <li>com.facebook.flipper:flipper-network-plugin</li>
             *    <li>com.facebook.soloader:soloader</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getLeakcanary() { return createBundle("flipper.without.leakcanary"); }

    }

    public static class PlayBundleAccessors extends BundleFactory {

        public PlayBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

            /**
             * Creates a dependency bundle provider for play.core which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.android.play:core</li>
             *    <li>com.google.android.play:core-ktx</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCore() { return createBundle("play.core"); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final TomlPluginAccessors paccForTomlPluginAccessors = new TomlPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for dependencyAnalysis to the plugin id 'com.autonomousapps.dependency-analysis'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getDependencyAnalysis() { return createPlugin("dependencyAnalysis"); }

        /**
         * Returns the group of plugins at plugins.toml
         */
        public TomlPluginAccessors getToml() { return paccForTomlPluginAccessors; }

    }

    public static class TomlPluginAccessors extends PluginFactory {

        public TomlPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for toml.checker to the plugin id 'com.github.ben-manes.versions'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getChecker() { return createPlugin("toml.checker"); }

            /**
             * Creates a plugin provider for toml.updater to the plugin id 'nl.littlerobots.version-catalog-update'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getUpdater() { return createPlugin("toml.updater"); }

    }

}
