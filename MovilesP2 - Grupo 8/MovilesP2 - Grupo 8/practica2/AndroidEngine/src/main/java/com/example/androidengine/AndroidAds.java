package com.example.androidengine;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class AndroidAds {

    private Activity activity;
    private AdView adView;
    private RewardedAd rewardedAd;

    // Inicializa el sistema de anuncios
    public AndroidAds(Activity activity) {
        this.activity = activity;
        MobileAds.initialize(activity, initializationStatus -> {
        });
    }

    // Carga el anuncio de banner según un id
    public void loadBannerAd(int adViewId) {
        adView = activity.findViewById(adViewId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void setBannerVisible(boolean visible) {
        if (adView == null) return;

        activity.runOnUiThread(() -> {
            adView.setVisibility(visible ? View.VISIBLE : View.GONE);
        });
    }

    // Carga el anuncio de recompensas según un id
    public void loadRewardedAd(String adId) {
        RewardedAd.load(activity, adId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd ad) {
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedAd = null;
            }
        });
    }

    // Enseña el anuncio de recompensas
    public void showRewardedAd(Runnable onReward) {
        if (rewardedAd != null) {
            activity.runOnUiThread(() -> rewardedAd.show(activity, rewardItem -> {
                if (onReward != null) {
                    onReward.run();
                }
                loadRewardedAd(rewardedAd.getAdUnitId());
            }));
        } else {
        }
    }

}
