package aaextras.rizzo.gabriele.legacy

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import aaextras.rizzo.gabriele.R



@Suppress("ConstantConditionIf")
class Launcher : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pm = packageManager

        val listofsupported = arrayOf(
                "projekt.substratum",
                "projekt.substratum.debug",
                "projekt.substratum.lite",
                "projekt.themer",
                "projekt.samsung.theme.compiler"
        )


        for (p in  listofsupported) {
            if (isPackageInstalled(p, pm)) {
                val launchIntent = packageManager.getLaunchIntentForPackage(p)
                if (launchIntent != null) {
                    val p = packageManager
                    p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                    startActivity(launchIntent)//null pointer check in case package name was not found
                    finish()
                }
            }
        }
            setContentView(R.layout.activity_launcher)

        val tv:TextView = findViewById(R.id.textView)
        tv.setText(getString(R.string.hi_iew, getString(R.string.ThemeName)))
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
