package com.easyapps.zkplayer.utils.ads

import android.content.Context
import android.widget.LinearLayout
import com.google.android.gms.ads.*
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import org.json.JSONArray

class BannerAds(private val context: Context, private val view:LinearLayout, banners: JSONArray) {
    private var copy = JSONArray()
    
    init {
        copy = JSONArray(banners.toString())
        waterFall()
    }

    private fun waterFall(removeFirst:Boolean =false){
        if (copy.length() <= 0) return
        if (removeFirst) copy.remove(0)
        view.removeAllViews()

        val adConfig = copy.optJSONObject(0) ?: return
        when (adConfig.optString("service")) {
            "g" -> loadGoogleADS()
            "y" -> loadYandexADS()
        }
    }

    private fun loadGoogleADS(){

        val adView = AdView(context)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = copy.optJSONObject(0).optString("id")
        view.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.adListener = object: AdListener() {  override fun onAdFailedToLoad(adError : LoadAdError) = waterFall(true) }
        adView.loadAd(adRequest)

    }

    private fun loadYandexADS(){
        val mBannerAdView = BannerAdView(context)
        mBannerAdView.setAdSize(BannerAdSize.inlineSize(view.context,320,100))
        mBannerAdView.setAdUnitId(copy.optJSONObject(0).optString("id"))
        view.addView(mBannerAdView)

        val madRequest = com.yandex.mobile.ads.common.AdRequest.Builder().build()
        mBannerAdView.setBannerAdEventListener(object : BannerAdEventListener {
            override fun onAdFailedToLoad(error: AdRequestError) = waterFall(true)
            override fun onAdLoaded() {}
            override fun onAdClicked() {}
            override fun onLeftApplication() {}
            override fun onReturnedToApplication() {}
            override fun onImpression(impressionData: ImpressionData?) {}
        })
        mBannerAdView.loadAd(madRequest)

    }

}

