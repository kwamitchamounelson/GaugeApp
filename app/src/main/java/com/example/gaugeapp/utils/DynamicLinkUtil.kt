package com.example.gaugeapp.utils

import android.content.Context
import android.net.Uri
import com.example.gaugeapp.R
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.example.gaugeapp.utils.remoteConfig.AppReleaseRemoteConfigUtils
import org.jetbrains.anko.indeterminateProgressDialog

object DynamicLinkUtil {


    const val DYNIMIKLINK_SPLITER = "#"
    const val APPLICATION_ID = "com.kola.kolaapplication"

    /**
     * Cette fonctiona apour but de generer un lien dynamique
     * @param context: le context de l'application, du fragment ou de l'acticité
     * @param chaine: une chaine de caractère à cacher dans le lien
     * @return string: le lien complet qui a été generer
     * */
    fun generateContentLink(
        context: Context,
        chaine: String,
        onSucess: (String) -> Unit,
        onError: () -> Unit
    ) {
        getReleaseInfos { urlApk, illustrationUtl, description ->
            val baseUrl =
                Uri.parse("${context.getString(R.string.dynamik_link_url)}/$DYNIMIKLINK_SPLITER$chaine")
            val domain = context.getString(R.string.dynamik_link_url)

            val progressDialog =
                context.indeterminateProgressDialog(context.getString(R.string.loading_in_progress))
            FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setSocialMetaTagParameters(
                    DynamicLink.SocialMetaTagParameters.Builder()
                        .setTitle(context.getString(R.string.app_name))
                        .setDescription(description ?: "")
                        .setImageUrl(Uri.parse(illustrationUtl))
                        .build()
                )
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setIosParameters(DynamicLink.IosParameters.Builder(APPLICATION_ID).build())
                .setAndroidParameters(
                    DynamicLink.AndroidParameters.Builder(APPLICATION_ID)
                        .setFallbackUrl(Uri.parse(urlApk))
                        .build()
                )
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnSuccessListener { result ->
                    val shortLink = result.shortLink
                    //val flowchartLink = result.previewLink
                    onSucess(shortLink.toString())
                    progressDialog.dismiss()
                }.addOnFailureListener {
                    onError()
                    progressDialog.dismiss()
                }
        }
    }


    /**
     * Cette fonction a pour but de recupérer les informations relatives à la generation du lient dynamique de remoteConfig
     * @return urlApk: l'url de téléchargement de l'application dans le store
     * @return urlImgllustration: l'image permettant d'illustrer l'application dans le lien dynamique générer
     * @return description: la description qui sera ac=ffiché avec le lien dans le partage de l'application
     * */

    fun getReleaseInfos(onComplete: (urlApk: String?, urlImgllustration: String?, description: String?) -> Unit) {

        val release = AppReleaseRemoteConfigUtils.getCurrentRelease()

        onComplete(
            release.apkUrl,
            release.illustrationUrl,
            release.shareDescription
        )


    }

}