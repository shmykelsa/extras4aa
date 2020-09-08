@file:Suppress("ConstantConditionIf")

package aaextras.rizzo.gabriele.legacy

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import aaextras.rizzo.gabriele.R
import aaextras.rizzo.gabriele.BuildConfig



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

        if (intent.action == "projekt.substratum.THEME") {
            setResult(getSelfSignature(applicationContext), returnIntent)
        } else if (intent.action == "projekt.substratum.GET_KEYS") {
            returnIntent.action = "projekt.substratum.RECEIVE_KEYS"
            sendBroadcast(returnIntent)
        }

        //setContentView(R.layout.content_snow)
        showWelcomeDialog()
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

        val anteprime: Button = view.findViewById(R.id.quarto_bottone) as Button
        anteprime.startAnimation(anifadeinfast)
        anteprime.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_previews))))
        }

        val donate = view.findViewById(R.id.button_donate) as Button
        donate.startAnimation(anifadeinfast)
        donate.setOnClickListener {
            openDonationOptions()
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


    private fun openDonationOptions() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(true)

        val view = View.inflate(this, R.layout.donation_dialog, null);

        val paypalButton:ImageView = view.findViewById(R.id.paypal_button)
        paypalButton.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_donate))))
        }

        val bitcoinButton:ImageView = view.findViewById(R.id.bitcoin_button)
        bitcoinButton.setOnClickListener{
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label","39bdKem8taTZvm2WeyH8wwDhYKzZ2PzhGn")
                clipboard.primaryClip = clip
            val text = R.string.copied_bitcoin
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }

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


        val myCheckBox = view.findViewById(R.id.myCheckBox) as CheckBox
        myCheckBox.visibility = CheckBox.GONE

        bottoneNegativo.setOnClickListener {
            val mSharedPreferences = getSharedPreferences("counter", MODE_PRIVATE)
            val mEditor = mSharedPreferences.edit()
            mEditor.putInt("counter_", -1)
            mEditor.apply()
            finish()
        }

        bottoneContinua.setOnClickListener {
            val mSharedPreferences = getSharedPreferences("counter", MODE_PRIVATE)
            val mEditor = mSharedPreferences.edit()
            mEditor.putInt("counter_", -1)
            mEditor.apply()
            openDonationOptions()
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
        val view= View.inflate(this, R.layout.contact_view, null)

        val icon1:ImageView = view.findViewById(R.id.telegram_icon)
        val icon2:ImageView = view.findViewById(R.id.mail_icon)
        val icon3:ImageView = view.findViewById(R.id.xda_icon)


        val takemetotelegram = view.findViewById(R.id.primaicona) as ConstraintLayout
        val mailme = view.findViewById(R.id.secondaicona) as ConstraintLayout
        val takmetoxda = view.findViewById(R.id.terzaicona) as ConstraintLayout

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


        val myCheckBox = view.findViewById(R.id.myCheckBox) as CheckBox
        myCheckBox.visibility = CheckBox.GONE

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
        val mSharedPreferences = getSharedPreferences("counter", MODE_PRIVATE)
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
        val mSharedPreferences = getSharedPreferences("counter", MODE_PRIVATE)
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


