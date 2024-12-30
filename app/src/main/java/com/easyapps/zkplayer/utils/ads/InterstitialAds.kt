package com.easyapps.zkplayer.utils.ads

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import org.json.JSONArray


class InterstitialAds(private val activity: Activity, banners: JSONArray = JSONArray("[]")) {
    private var copy = JSONArray()
    init {
        MobileAds.initialize(activity)
        copy = JSONArray(banners.toString())
        waterFall()
    }


    private fun waterFall(removeFirst: Boolean =false) {
        if (copy.length() <= 0) return
        if (removeFirst)  copy.remove(0)

        val adConfig = copy.optJSONObject(0) ?: return
        when (adConfig.optString("service")) {
            "g" -> loadGoogleADS()
            "y" -> loadYandexADS()
        }

    }
    private fun loadGoogleADS(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity,copy.optJSONObject(0).optString("id"), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) = waterFall(true)
            override fun onAdLoaded(interstitialAd: InterstitialAd) = interstitialAd.show(activity)

        })
    }
    private fun loadYandexADS(){
        val interstitialAdLoader = InterstitialAdLoader(activity)

        interstitialAdLoader.setAdLoadListener(object: InterstitialAdLoadListener {
            override fun onAdLoaded(interstitialAd: com.yandex.mobile.ads.interstitial.InterstitialAd) = interstitialAd.show(activity)
            override fun onAdFailedToLoad(error: AdRequestError) = waterFall(true)

        })

        val adRequestConfiguration = AdRequestConfiguration.Builder(copy.optJSONObject(0).optString("id")).build()
        interstitialAdLoader.loadAd(adRequestConfiguration)

    }



}


