package com.ankitgupta.kchatapp.application

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.Locale


class HiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // timber for logging
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }
        instance = this
        appContext = applicationContext
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

//        val defaultLanguage = Locale.getDefault().language
//
//        // Set the app's language based on the device's default language
//        if (defaultLanguage == "ar") {
//            // If the device's default language is Arabic, set the app's language to Arabic
//            changeLanguage("ar", applicationContext)
//        } else {
//            // If the device's default language is not Arabic, set the app's language to English
//            changeLanguage("en", applicationContext)
//        }
    }


    companion object {
        private lateinit var dialog: ProgressDialog
        var SHARED_PREF_NAME = "KChatAppPref"
        var AuthToken = "AuthToken"
        var UserId = "UserId"
        var isLogin = "Login"
        var email = "Email"
        var userName = "USERNAME"
        var isFirstLogin = "FIRSTLOGIN"
        var password = "Password"
        var isSupplier = "isSupplier"
        var DEVICEID = "deviceId"
        var supplierId = "supplierId"
        var supplierPhoneNumber = "supplierNumber"
        var userSelectCatagory = "catagory"
        var userSelectSubCatagory = "subcatagory"
        private val MIN_TIME_BETWEEN_UPDATES: Long =
            200 // Minimum time interval between location updates in milliseconds
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f
        var locationManager: LocationManager? = null

        lateinit var appContext: Context
            private set

        @get:Synchronized
        lateinit var instance: HiltApplication
            private set

        lateinit var progressDialog: AlertDialog
    }

    fun isDeviceLanguageArabic(): Boolean {
        val language = resources.configuration.locale.language
        //    Timber.tag("lan").e(language)
        return language == "ar"
    }

    fun returnLangString(s: String): String {
        val splitString = s.split("/")
        if (isDeviceLanguageArabic()) {
            return splitString[1]
        }
        return splitString[0]
    }


    fun spinnerStart(context: Context) {
        val pleaseWait = context.getString(R.string.please_wait)
        dialog = ProgressDialog.show(context, "", pleaseWait, true)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    fun spinnerStop() {
        try {
            if (dialog.isShowing)
                dialog.dismiss()
        } catch (e: Exception) {

        }

    }

    fun showSnackBar(view: View, string: String) {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show()
    }

    fun getSharedPrefString(preffConstant: String): String {
        var stringValue: String? = ""
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        return try {
            stringValue = sp.getString(preffConstant, "")
            stringValue ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    fun setSharedPrefString(
        preffConstant: String,
        stringValue: String
    ) {
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putString(preffConstant, stringValue)
        editor.apply()
    }

    fun setUserDataInSharedPref(userProfileData: ProfileData, key: String) {


    }

    fun getSharedPrefInteger(preffConstant: String): Int {
        var intValue = 0
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        intValue = sp.getInt(preffConstant, 0)
        return intValue
    }

    fun setSharedPrefInteger(preffConstant: String, value: Int) {
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putInt(preffConstant, value)
        editor.apply()
    }

    fun setSharedPrefBoolean(preffConstant: String, value: Boolean) {
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putBoolean(preffConstant, value)
        editor.apply()
    }


    fun getSharedPrefBoolean(preffConstant: String): Boolean {
        var stringValue = false
        val sp = appContext.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        stringValue = sp.getBoolean(preffConstant, false)
        return stringValue
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    fun showLoader(context: Context) {
//        val builder = AlertDialog.Builder(context)
//        builder.setView(R.layout.loader)
//        progressDialog = builder.create()
//        progressDialog.setCanceledOnTouchOutside(false)
//        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        progressDialog.show()
//    }

//    fun stopLoader() {
//        try {
//            if (progressDialog.isShowing) {
//                progressDialog.cancel()
//            }
//            Timber.tag("ACTIVITY_LIFECYCLE").e("progressDialogIsShowing: ${progressDialog.isShowing}")
//        } catch (e: Exception) {
//            Timber.tag("ACTIVITY_LIFECYCLE").e("progressDialog: $e")
//        }
//
//    }
//
//    fun showVisibility(binding: ActivityLoginBinding) {
//        binding.apply {
//            if (enterdPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
//                // Password is currently hidden, so show it
//                enterdPassword.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//                showText.text = getString(R.string.hide)
//            } else {
//                // Password is currently shown, so hide it
//                enterdPassword.transformationMethod = PasswordTransformationMethod.getInstance()
//                showText.text = getString(R.string.show_)
//            }
//        }
//    }
//
//
//    fun showVisibility(binding: ActivitySignUpBinding) {
//        binding.apply {
//            if (enterdPassword12.transformationMethod == PasswordTransformationMethod.getInstance()) {
//                // Password is currently hidden, so show it
//                enterdPassword12.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//                showText12.text = getString(R.string.hide)
//            } else {
//                // Password is currently shown, so hide it
//                enterdPassword12.transformationMethod = PasswordTransformationMethod.getInstance()
//                showText12.text = getString(R.string.show_)
//            }
//        }
//    }

//    fun getCurrentLanguage(): String? {
//        val sharedPreferences: SharedPreferences = getSharedPreferences("language", 0)
//        return sharedPreferences.getString("language", "ar")
//    }

    fun setCurrentLanguage(value: String?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("language", 0)
        val editor = sharedPreferences.edit()
        editor.putString("language", value)
        editor.apply()
    }

    private fun changeLanguage(language: String?, context: Context) {
        val locale = Locale(language.toString())
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }


//    fun changeLocale() {
//        changeLanguage(getCurrentLanguage(), baseContext)
//    }


    fun handleError(context: Context, exception: Throwable) {
        showToast(context = context, "$exception")
        // Handle error cases if needed
    }


//    fun showForgotPassWordDialog(context: Context, callback: (String?) -> Unit) {
//        val popupView: View =
//            LayoutInflater.from(context).inflate(R.layout.forgot_password_layout, null)
//        val dialogBuilder: AlertDialog.Builder =
//            AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
//        dialogBuilder.setView(popupView)
//        val dialog1 = dialogBuilder.create()
//
//        val submit: TextView = popupView.findViewById(R.id.submit)
//        val cancel: TextView = popupView.findViewById(R.id.cancel)
//        val enterEmail: EditText = popupView.findViewById(R.id.enterEmail)
//
//        submit.setOnClickListener {
//            val email = enterEmail.text.toString()
//            if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                // Uncomment the following lines and replace them with your API call logic
//                // viewModel._isloading.value = true
//                // viewModel.forgotPassword(email) { success ->
//                //     callback(if (success) email else null)
//                // }
//
//                // For demonstration, assuming the API call is successful
//                callback(email)
//            } else {
//                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
//                callback("")
//            }
//            dialog1.dismiss()
//        }
//
//        cancel.setOnClickListener {
//            dialog1.cancel()
//            callback("")
//        }
//
//        dialog1.show()
//    }

    fun clearSharedPreferences() {
        // Replace "your_preference_name" with the actual name of your shared preferences file
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

//    fun showLogoutDialog(activity: AppCompatActivity, function: () -> Unit) {
//        val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
//        builder.setTitle(getString(R.string.Logout))
//        builder.setMessage(getString(R.string.logout_show_message))
//        builder.setPositiveButton(getString(R.string.Logout)) { dialog, which ->
//            // Perform logout action here
//            // For example, you can clear session, navigate to login screen, etc.
//            // Example: logoutUser()
//            function()
//        }
//        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
//            dialog.dismiss()
//        }
//
//        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
//        dialog.show()
//    }

    fun isPermissionGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun isGpsOn(locationManager: LocationManager): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun turnOnGps(context: Context, gpsCode: Int): androidx.appcompat.app.AlertDialog? {
        return MaterialAlertDialogBuilder(context)
            .setTitle("Location Provider")
            .setBackgroundInsetEnd(20)
            .setBackgroundInsetBottom(20)
            .setBackgroundInsetStart(20)
            .setMessage("Location is necessary")
            .setPositiveButton(
                "SETTINGS"
            ) { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                (context as? Activity)?.startActivityForResult(settingsIntent, gpsCode)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                // Handle the case where the user cancels the dialog
                dialog.cancel()
            }
            .show()
    }

    fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

//    fun sendLocationUpdate(context: Context, listener: LocationUpdateListener) {
//        try {
//            // Request location updates from the LocationManager
//            locationManager?.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                MIN_TIME_BETWEEN_UPDATES,
//                MIN_DISTANCE_CHANGE_FOR_UPDATES,
//                object : LocationListener {
//                    override fun onLocationChanged(location: Location) {
//                        // Remove location updates after obtaining the latest location
//                        locationManager!!.removeUpdates(this)
//                        // Invoke the callback with the obtained location
//                        listener.onLocationUpdated(location)
//                    }
//
//                    @Deprecated(
//                        "Deprecated in Java", ReplaceWith(
//                            "Timber.tag(\"eee\").e(\"\$status + \$provider\")",
//                            "timber.log.Timber"
//                        )
//                    )
//                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                        Timber.tag("eee").e("$status + $provider")
//                    }
//
//                }
//            )
//        } catch (exception: SecurityException) {
//            Timber.e(exception, "Location permission not granted")
//            listener.errorInLocationListener("Location permission not granted")
//        } catch (e: Exception) {
//            Timber.e(e, "Error occurred while obtaining location")
//            listener.errorInLocationListener("Error occurred while obtaining location")
//        }
//    }

    fun removeLocationUpdates(): Location? {
        var location: Location? = null
        if (locationManager != null) {
            locationManager!!.removeUpdates { location12 ->
                location = location12
            }
        }

        return location
    }

    fun showEditDeleteDialog(
        context: Context,
        onEditClicked: () -> Unit,
        onDeleteClicked: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Options")
        val options = arrayOf("Edit", "Delete")
        builder.setItems(options) { a, which ->
            when (which) {
                0 -> {
                    onEditClicked.invoke() // Edit clicked
                    a.dismiss()
                }

                1 -> {
                    onDeleteClicked.invoke() // Delete clicked
                    a.dismiss()
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    @SuppressLint("MissingInflatedId")
    fun showPopupMenu(
        context: Context, view: View, onProfileClicked: () -> Unit,
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate the custom layout
        val customView = inflater.inflate(R.layout.popup_menu_style, null)

        // Initialize the PopupWindow
        val popupWindow = PopupWindow(
            customView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val profile: TextView = customView.findViewById(R.id.profile__)

        profile.setOnClickListener {
            onProfileClicked.invoke()
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }

        }

//        delete.setOnClickListener {
//            onDeleteClicked.invoke()
//            if (popupWindow.isShowing) {
//                popupWindow.dismiss()
//            }
//
//        }

        popupWindow.showAsDropDown(view)
    }


//    fun getLocationPermission(
//        context: Context,
//        onPermissionDenied: () -> Unit,
//        onPermissionGranted: () -> Unit
//    ) {
//        Dexter.withContext(context)
//            .withPermissions(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ).withListener(/* p0 = */ object :
//                MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                    if (report.areAllPermissionsGranted()) {
//                        onPermissionGranted()
//                    } else {
//                        onPermissionDenied()
//                        // Handle case where permissions are not granted
//                        // Maybe show a message or take appropriate action
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: List<PermissionRequest?>?,
//                    token: PermissionToken?
//                ) {
//                    // Handle rationale for permissions
//                    // This might involve showing a dialog explaining why permissions are needed
//                }
//            }).check()
//    }

    fun showLocationPermissionDialog(context: Context) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Location Permission")
            .setMessage("Location permission is necessary.")
            .setPositiveButton("Settings") { dialog: DialogInterface, _: Int ->
                // Open settings to enable location permission
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
                dialog.dismiss()
            }

            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun checkCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

//    fun requestCallPermission(activity: Activity) {
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(Manifest.permission.CALL_PHONE),
//            PERMISSION_REQUEST_CALL_PHONE
//        )
//    }
}