@file:Suppress("ConstantConditionIf")

package aawallpapers.rizzo.gabriele

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.*



class SubstratumLauncher : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val returnIntent = if (intent.action == "projekt.substratum.GET_KEYS") {
            Intent("projekt.substratum.RECEIVE_KEYS")
        } else {
            Intent()
        }
        val themeName = getString(R.string.ThemeName)
        val themeAuthor = getString(R.string.ThemeAuthor)
        val themePid = packageName
        returnIntent.putExtra("theme_name", themeName)
        returnIntent.putExtra("theme_author", themeAuthor)
        returnIntent.putExtra("theme_pid", themePid)
        returnIntent.putExtra("theme_debug", BuildConfig.DEBUG)

        if (intent.action == "projekt.substratum.THEME") {
            setResult(getSelfSignature(applicationContext), returnIntent)
        } else if (intent.action == "projekt.substratum.GET_KEYS") {
            returnIntent.action = "projekt.substratum.RECEIVE_KEYS"
            sendBroadcast(returnIntent)
        }

        //setContentView(R.layout.content_snow)
        //showWelcomeDialog()
        finish()
    }

    @SuppressLint("InflateParams")
    private fun showWelcomeDialog() {

        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(false)
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val anifadein = AnimationUtils.loadAnimation(this, R.anim.fade)
        val anislideup = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val anifadeinfast = AnimationUtils.loadAnimation(this, R.anim.fadefast)


        val dialogContent = view.findViewById(R.id.dialog_content) as TextView
        dialogContent.startAnimation(anifadein)

        val dialogTitle = view.findViewById(R.id.title) as TextView
        dialogTitle.startAnimation(anislideup)

        val previewsIcon: ImageButton = view.findViewById(R.id.previews_icon) as ImageButton
        previewsIcon.startAnimation(anifadeinfast)
        previewsIcon.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_photos))))
        }
        val preview = view.findViewById(R.id.area_preview) as RelativeLayout
        preview.startAnimation(anifadeinfast)
        preview.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_photos))))
        }

        val creditsicon: ImageButton = view.findViewById(R.id.image_credits) as ImageButton
        creditsicon.startAnimation(anifadeinfast)
        creditsicon.setOnClickListener {
            openCreditsDialog()
        }
        val areacrediti = view.findViewById(R.id.area_credits) as RelativeLayout
        areacrediti.startAnimation(anifadeinfast)
        areacrediti.setOnClickListener {
            openCreditsDialog()
        }

        val areavarianti = view.findViewById(R.id.area_altritemi) as RelativeLayout
        areavarianti.startAnimation(anifadeinfast)
        areavarianti.setOnClickListener {
            openotherthemesdialog()
        }

        val cont = view.findViewById(R.id.button_continue) as Button
        cont.startAnimation(anifadeinfast)
        cont.setOnClickListener {
            if (getCounterOpened() % 3 == 0) {
                openDonationDialog()
            } else if (!getStoreRatingStatus() && getCounterOpened() > 1) {
                ratingDialog()
            } else {
                finish()
            }
        }

        val maybelater: Button = view.findViewById(R.id.terzo_bottone) as Button
        maybelater.startAnimation(anifadeinfast)
        maybelater.setText(R.string.helpButton)
        maybelater.setOnClickListener{
            openHelpDialog()
        }

        val donate = view.findViewById(R.id.button_donate) as Button
        donate.startAnimation(anifadeinfast)
        donate.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_donate))))
        }
        /*Checkbox*/
        val myCheckBox = view.findViewById(R.id.myCheckBox) as CheckBox
        myCheckBox.startAnimation(anifadeinfast)
        myCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                storeDialogStatus(true)
            }
            if (!isChecked) {
                storeDialogStatus(false)
            }
        }


        alertDialog.setView(view)

        if (getDialogStatus()) {
            finish()
        } else {
            alertDialog.show()
        }

    }

    private fun openotherthemesdialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.other_themes_view, null)

        val bottomtext: TextView = view.findViewById(R.id.bottomtextivewothers)
        bottomtext.setOnClickListener{
            openContactDialog()
        }

        val primobottone: RelativeLayout = view.findViewById(R.id.primaiconaaltre)
        primobottone.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_sport))))
        }

        val secondobottone: RelativeLayout = view.findViewById(R.id.secondaiconaaltre)
        secondobottone.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_vehicules))))
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    @SuppressLint("InflateParams")
    private fun openCreditsDialog() {

        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.sample_credits_view, null)

        val text:TextView = view.findViewById(R.id.credits_content)
        val creditors = getString(R.string.creditstring, getString(R.string.creditors))
        text.text = creditors

        val bottomtext:TextView = view.findViewById(R.id.bottomtextivew)
        bottomtext.visibility = TextView.GONE

        alertDialog.setView(view)
        alertDialog.show()


    }


    private fun openDonationDialog() {

        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val sezionesuperiore:TextView = view.findViewById(R.id.title)
        sezionesuperiore.visibility = RelativeLayout.GONE

        val textViewcentrale:TextView = view.findViewById(R.id.dialog_content)
        textViewcentrale.text = getString(R.string.donation_message)
        textViewcentrale.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F)

        val bottoneContinua: Button = view.findViewById(R.id.button_continue)
        bottoneContinua.text = getString(R.string.gotorating)

        val bottoneNegativo: Button = view.findViewById(R.id.button_donate)
        bottoneNegativo.text = getString(R.string.nevershowagain)

        val previewsIcon: ImageButton = view.findViewById(R.id.previews_icon) as ImageButton
        previewsIcon.visibility = ImageButton.GONE

        val preview = view.findViewById(R.id.area_preview) as RelativeLayout
        preview.visibility = RelativeLayout.GONE

        val creditsicon: ImageButton = view.findViewById(R.id.image_credits) as ImageButton
        creditsicon.visibility = ImageButton.GONE

        val areacrediti = view.findViewById(R.id.area_credits) as RelativeLayout
        areacrediti.visibility = RelativeLayout.GONE

        val areaaktru = view.findViewById(R.id.area_altritemi) as RelativeLayout
        areaaktru.visibility = RelativeLayout.GONE

        val myCheckBox = view.findViewById(R.id.myCheckBox) as CheckBox
        myCheckBox.visibility = CheckBox.GONE

        bottoneNegativo.setOnClickListener {
            val mSharedPreferences = getSharedPreferences("counter", Activity.MODE_PRIVATE)
            val mEditor = mSharedPreferences.edit()
            mEditor.putInt("counter_", -1)
            mEditor.apply()
            finish()
        }

        bottoneContinua.setOnClickListener {
            val mSharedPreferences = getSharedPreferences("counter", Activity.MODE_PRIVATE)
            val mEditor = mSharedPreferences.edit()
            mEditor.putInt("counter_", -1)
            mEditor.apply()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_donate))))
        }

        val maybelater: Button = view.findViewById(R.id.terzo_bottone) as Button
        maybelater.setOnClickListener { finish() }

        alertDialog.setView(view)
        alertDialog.show()

    }

    @SuppressLint("InflateParams")
    private fun openHelpDialog() {


        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.sample_credits_view, null)

        val text:TextView = view.findViewById(R.id.credits_content)
        if (whatSubAreYouUsing()=="projekt.substratum.lite") {
            text.setText(R.string.helpme_lite)
        } else {
            text.setText(R.string.helpme)
        }

        val takmetoxda = view.findViewById(R.id.takemetocontacts) as RelativeLayout
        takmetoxda.setOnClickListener {
            openContactDialog()
        }
        alertDialog.setView(view)
        alertDialog.show()

    }

    @SuppressLint("InflateParams")
    private fun openContactDialog() {

        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.contact_view, null)

        val icon1:ImageView = view.findViewById(R.id.telegram_icon)
        val icon2:ImageView = view.findViewById(R.id.mail_icon)
        val icon3:ImageView = view.findViewById(R.id.xda_icon)


        val takemetotelegram = view.findViewById(R.id.primaicona) as RelativeLayout
        val mailme = view.findViewById(R.id.secondaicona) as RelativeLayout
        val takmetoxda = view.findViewById(R.id.terzaicona) as RelativeLayout

        icon1.setImageResource(R.drawable.ic_icons8_telegramma_app)
        icon2.setImageResource(R.drawable.ic_envelope)
        icon3.setImageResource(R.drawable.ic_xda)

        takemetotelegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_telegram))))
        }

        mailme.setOnClickListener {

            val androidversion = Build.VERSION.SDK_INT
            val modelname = Build.MODEL

            val nomePacchetto = whatSubAreYouUsing()
            val pinfo: PackageInfo?
            pinfo = packageManager.getPackageInfo(nomePacchetto, 0)
            val version = pinfo.versionName

            val text = getString(R.string.link_mail, BuildConfig.VERSION_NAME, androidversion.toString(), modelname, nomePacchetto, version)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(text)))
        }

        takmetoxda.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_support))))
        }

        alertDialog.setView(view)
        alertDialog.show()

    }

    private fun ratingDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val sezionesuperiore:TextView = view.findViewById(R.id.title)
        sezionesuperiore.visibility = RelativeLayout.GONE

        val textViewcentrale:TextView = view.findViewById(R.id.dialog_content)
        textViewcentrale.text = getString(R.string.rating_message)
        textViewcentrale.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22F)

        val bottoneContinua:Button = view.findViewById(R.id.button_continue)
        bottoneContinua.text = getString(R.string.gotorating)

        val bottoneNegativo:Button = view.findViewById(R.id.button_donate)
        bottoneNegativo.text = getString(R.string.nevershowagain)

        val previewsIcon:ImageButton = view.findViewById(R.id.previews_icon) as ImageButton
        previewsIcon.visibility = ImageButton.GONE

        val preview = view.findViewById(R.id.area_preview) as RelativeLayout
        preview.visibility = RelativeLayout.GONE

        val creditsicon:ImageButton = view.findViewById(R.id.image_credits) as ImageButton
        creditsicon.visibility = ImageButton.GONE

        val areacrediti = view.findViewById(R.id.area_credits) as RelativeLayout
        areacrediti.visibility = RelativeLayout.GONE

        val myCheckBox = view.findViewById(R.id.myCheckBox) as CheckBox
        myCheckBox.visibility = CheckBox.GONE

        val areaaktru = view.findViewById(R.id.area_altritemi) as RelativeLayout
        areaaktru.visibility = RelativeLayout.GONE

        bottoneNegativo.setOnClickListener {
            storeRatingStatus(isChecked = true)
            finish()
        }

        bottoneContinua.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_playstore))))
            storeRatingStatus(isChecked = true)
        }

        val maybelater:Button = view.findViewById(R.id.terzo_bottone) as Button
        maybelater.setOnClickListener { finish() }

        if (!getStoreRatingStatus()) {
            alertDialog.setView(view)
            alertDialog.show()
        }
    }


    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    fun getSelfSignature(context: Context): Int {
        try {
            val sigs = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
            ).signatures
            return sigs[0].hashCode()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun whatSubAreYouUsing() : String {
        val pm = packageManager
        val listofsupported = arrayOf(
                "projekt.substratum",
                "projekt.substratum.debug",
                "projekt.substratum.lite",
                "projekt.themer",
                "projekt.samsung.theme.compiler"
        )

        for (p in  listofsupported) {
            if (isPackageInstalled(p, pm) && p == "projekt.substratum.lite") {
                return p
            }
        }
        return "vaffanculo"
    }

    private fun storeCounterOpened() {
        val mSharedPreferences = getSharedPreferences("counter", Activity.MODE_PRIVATE)
        val mEditor = mSharedPreferences.edit()
        var counter = mSharedPreferences.getInt("counter_", 0)
        if (counter == -1) {
            return
        }
        counter += 1
        mEditor.putInt("counter_", counter)
        mEditor.apply()
    }

    private fun getCounterOpened():Int {
        val mSharedPreferences = getSharedPreferences("counter", Activity.MODE_PRIVATE)
        return mSharedPreferences.getInt("counter_", 0)
    }

    private fun storeDialogStatus(isChecked: Boolean) {
        val mSharedPreferences = getSharedPreferences("dialog", Context.MODE_PRIVATE)
        val mEditor = mSharedPreferences.edit()
        mEditor.putBoolean("show_dialog_" + BuildConfig.VERSION_CODE, isChecked)
        mEditor.apply()
    }

    private fun getDialogStatus(): Boolean {
        storeCounterOpened()
        val mSharedPreferences = getSharedPreferences("dialog", Context.MODE_PRIVATE)
        return mSharedPreferences.getBoolean("show_dialog_" + BuildConfig.VERSION_CODE, false)
    }

    private fun storeRatingStatus(isChecked: Boolean) {
        val mSharedPreferences = getSharedPreferences("rated", Context.MODE_PRIVATE)
        val mEditor = mSharedPreferences.edit()
        mEditor.putBoolean("show_rating_dialog_", isChecked)
        mEditor.apply()
    }

    private fun getStoreRatingStatus(): Boolean {
        val mSharedPreferences = getSharedPreferences("rated", Context.MODE_PRIVATE)
        return mSharedPreferences.getBoolean("show_rating_dialog_", false)
    }


    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}


