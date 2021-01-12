package com.example.gaugeapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gaugeapp.R
import com.example.gaugeapp.data.entities.Cashflow
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.kola.kola_entities_features.entities.DateForSort
import com.kola.kola_entities_features.entities.NumberForOperator
import com.kola.kola_entities_features.entities.SavedStoreInfo
import com.example.gaugeapp.KolaWhalletApplication.Companion.userPref
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.data.enums.ENUMCATEGORYEXPENSE
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.extentions.getLimiteDateByPreference
import com.example.gaugeapp.utils.extentions.removeZeroAtEnd
import com.whiteelephant.monthpicker.MonthPickerDialog
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.date_picker_dialog_view.view.*
import kotlinx.android.synthetic.main.date_time_picker_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


const val TAG = "GeneralUtils"
const val ORANGE_OPERATOR_NAME = "Orange"
const val ORANGE_MONEY_NAME = "OrangeMoney"
const val MTN_OPERATOR_NAME = "MTN"
const val MTN_Money_NAME = "MobileMoney"
const val BOTH_OPERATOR_NAME = "BOTH"
const val BALANCE_ALL = "ALL"
const val BALANCE_PERCENT = "BALANCE_PERCENT"
const val BALANCE_FIX = "BALANCE_FIX"
const val WHATSAP_TEST_LINK = "https://chat.whatsapp.com/C42q8u2TDtsC7Ewyh6WOnE"
const val CHANNEL_ID = "personal finance"


fun displayNotification(
    context: Context,
    textTitle: String,
    textContent: String,
    longText: String
) {
    var builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ionic_ios_notifications_outline)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(longText))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
}


/**
 * to get a month name from a ont provided by the date class
 * @param month the number of the month
 * @return String the name of the month in english
 */
fun getMontNameFromInt(month: Int): String {
    val formatter = SimpleDateFormat("MMMM", Locale.getDefault())
    val calendar = GregorianCalendar()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.MONTH, month)
    return formatter.format(calendar.getTime()).capitalize()
    //return DateFormatSymbols().months[month]
}

fun displayBalanceWithSettings(view: TextView, double: Double, operatorName: String) {
    when (userPref.balanceTheme) {
        BALANCE_FIX -> view.setText(
            if (operatorName.equals(ORANGE_OPERATOR_NAME)) {
                userPref.balanceFixValueOrange?.toDouble()?.formatNumberWithSpaceBetweenThousand()
            } else {
                userPref.balanceFixValueMTN?.toDouble()?.formatNumberWithSpaceBetweenThousand()
            }
        )
        BALANCE_PERCENT -> view.setText(
            ((userPref.balancePercentage?.toDouble()
                ?.times(double))?.div(100))?.formatNumberWithSpaceBetweenThousand()
        )
        else -> {
            if (double >= 0) {
                view.setText(double.formatNumberWithSpaceBetweenThousand())
            } else {
                view.setText("")
            }
        }
    }
}

fun isOrangeMoneyTransfertInitiated(messageFromUSSD: String): Boolean {
    val regexForOrangeMoneyTransferInitiated =
        """transfert initie|transfert est initie|transfer initiated|transfer is initiated|successfully transferred|effectué avec succes|rechargement effectue|top up done""".toRegex()
    val lowerCaseMessage = messageFromUSSD.toLowerCase()
    return lowerCaseMessage.contains(regexForOrangeMoneyTransferInitiated)
}

fun isMTNMoneyTransfertInitiated(messageFromUSSD: String): Boolean {
    val regexForMTNMoneyTransferInitiated =
        """transfert effectue avec succes|successful transfer of|successful transfer|transaction is pending|transaction est en cours|transaction en cours|transfert reussi""".toRegex()
    val lowerCaseMessage = messageFromUSSD.toLowerCase()
    return lowerCaseMessage.contains(regexForMTNMoneyTransferInitiated)
}

fun getGMTDate(): String {
    val dateFormatGmt = SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault())
    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormatGmt.format(Date())
}

/**
 * to format double 2 number after the coma
 * @param number the double you want to format
 * @author Yvana
 */
fun Double.formatDouble(): Double {
    try {
        val df = DecimalFormat("#.##")
        val result = df.format(this)
        val res = result.replace(",", ".")
        return res.toDouble()
    } catch (e: NumberFormatException) {
        return this
    }
}

/**
 * this function is for format the number with space between thousands to be more understanding
 * @author yvana
 * @return String
 */
fun Double.formatNumberWithSpaceBetweenThousand(): String {
    val string = DecimalFormat("###,###.##", DecimalFormatSymbols(Locale.ENGLISH)).format(this)
    return string.replace(",", " ")
}


/**
 * this function is for format the number with space between thousands to be more understanding
 * @author yvana
 * @return String
 */
fun Double.formatNumberWithSpaceBetweenThousandAndTakeIntegerPart(): String {
    val value = if (this >= 1000000.0) {
        this.toInt().toDouble()
    } else {
        this
    }
    return value.formatNumberWithSpaceBetweenThousand()
}

/**
 * the function to format phone number with space between 3 characters
 * @author yvana
 */

fun formatPhoneNumberWithSpace(phoneNumber: String): String {
    var numberSpaced = ""
    if (phoneNumber.length == 9) {
        numberSpaced =
            phoneNumber.substring(0, 1) + " " + phoneNumber.substring(
                1,
                3
            ) + " " + phoneNumber.substring(
                3,
                5
            ) + " " + phoneNumber.substring(
                5,
                7
            ) + " " + phoneNumber.substring(7)
    } else if (phoneNumber.length == 8) {
        numberSpaced =
            phoneNumber.substring(
                0,
                2
            ) + " " + phoneNumber.substring(
                2,
                4
            ) + " " + phoneNumber.substring(
                4,
                6
            ) + " " + phoneNumber.substring(6)
    } else if (phoneNumber.length == 13 && phoneNumber.startsWith("+237")) {
        numberSpaced =
            phoneNumber.substring(0, 4) + " " + phoneNumber.substring(
                4,
                5
            ) + " " + phoneNumber.substring(
                5,
                7
            ) + " " + phoneNumber.substring(
                7,
                9
            ) + " " + phoneNumber.substring(
                9,
                11
            ) + " " + phoneNumber.substring(11)
    } else if (phoneNumber.length == 12 && phoneNumber.startsWith("+237")) {
        numberSpaced =
            phoneNumber.substring(0, 4) + " " + phoneNumber.substring(
                4,
                6
            ) + " " + phoneNumber.substring(
                6,
                8
            ) + " " + phoneNumber.substring(
                8,
                10
            ) + " " + phoneNumber.substring(10)
    } else {
        numberSpaced = phoneNumber
    }
    return numberSpaced
}

fun getVersionNameOfApp(context: Context): String {
    val pinfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return pinfo.versionName
}

fun getVersionCodeOfApp(context: Context): Long {
    val pinfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pinfo.longVersionCode
    } else {
        pinfo.versionCode.toLong()
    }
}

/**
 * Cette fonction a pour rôle de retourner
 * l'EMEIl du telephone de l'utilisateur
 * @param context: le context actuel qui execute le service
 * @return phone EMEI or empty string if
 * a permission is not granded by user
 */
/**
 * this function retuns the EMEII of the user's phone.
 * @param context the context that execute the service
 *
 */
@SuppressLint("HardwareIds")
fun getDeviceId(
    context: Context
): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun getDeviceModel(): String {
    return android.os.Build.MODEL
}

fun getDevice(): String {
    return android.os.Build.DEVICE
}


/**
 * This function permit to get a date from an alert dialog
 * If the user aught to enter a date and they may use this date for treatment is better to take it from a datePicker
 * This function privide a date picker in a alertDialog
 * @author Yvana
 * @param layoutInflater to inflate the view that have the date picker in the alert view
 * @param context to create the alertDialog and have the text from strings for the buttons
 * @param onListen the function that return the date get by the date picker
 * @return Unit the function doesn't return anything
 */
fun getDateWithDialog(
    layoutInflater: LayoutInflater,
    context: Context,
    onListen: (Date) -> Unit,
    minDate: Date? = null,
    maxDate: Date? = null
) {
    /**
     * I define the view which will be used in the alertDialog
     */

    val dateView =
        layoutInflater.inflate(R.layout.date_picker_dialog_view, null, false)
    /**
     * I declare the AlertDialog
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        dateView.id_date_picker.setOnDateChangedListener { datePicker, i, i2, i3 ->

        }
    }
    if (maxDate != null) {
        dateView.id_date_picker.maxDate = maxDate.time
    }

    if (minDate != null) {
        dateView.id_date_picker.minDate = minDate.time
    }

    android.app.AlertDialog.Builder(context)
        .setView(dateView)
        .setNegativeButton(context.getString(R.string.text_cancel), null)
        .setPositiveButton(context.getString(R.string.text_ok)) { dialogInterface, i ->
            /**
             * we fetch the Date
             */
            val calendar = Calendar.getInstance()
            calendar.set(
                dateView.id_date_picker.year,
                dateView.id_date_picker.month,
                dateView.id_date_picker.dayOfMonth
            )
            onListen(calendar.time)
        }
        .create()
        .show()
}

fun getDateTimeWithDialog(
    context: Context,
    onListen: (Date) -> Unit,
    minDate: Date? = null,
    maxDate: Date? = null
) {
    /**
     * I define the view which will be used in the alertDialog
     */

    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val dateTimeView =
        layoutInflater.inflate(R.layout.date_time_picker_dialog, null, false)
    /**
     * I declare the AlertDialog
     */

//    dateTimeView.date_time_date_picker.setOnDateChangeListener { calendarView, i, i2, i3 ->
//
//    }
//
    if (maxDate != null) {
        dateTimeView.date_time_date_picker.maxDate = maxDate.time
    }

    if (minDate != null) {
        dateTimeView.date_time_date_picker.minDate = minDate.time
    }

    dateTimeView.btn_previous.setOnClickListener {
        dateTimeView.date_time_date_picker.visibility = View.VISIBLE
        dateTimeView.btn_next.visibility = View.VISIBLE
        dateTimeView.btn_previous.visibility = View.GONE
        dateTimeView.btn_ok.visibility = View.GONE
        dateTimeView.date_time_time_picker.visibility = View.GONE
    }

    dateTimeView.btn_next.setOnClickListener {
        dateTimeView.date_time_date_picker.visibility = View.GONE
        dateTimeView.btn_next.visibility = View.GONE
        dateTimeView.btn_previous.visibility = View.VISIBLE
        dateTimeView.btn_ok.visibility = View.VISIBLE
        dateTimeView.date_time_time_picker.visibility = View.VISIBLE
    }

    val alert = AlertDialog.Builder(context)
        .setView(dateTimeView)
        .create()

    dateTimeView.btn_cancel.setOnClickListener {
        alert.dismiss()
    }

    dateTimeView.btn_ok.setOnClickListener {
        val calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendar.set(
                dateTimeView.date_time_date_picker.year,
                dateTimeView.date_time_date_picker.month,
                dateTimeView.date_time_date_picker.dayOfMonth,
                dateTimeView.date_time_time_picker.hour,
                dateTimeView.date_time_time_picker.minute
            )
        } else {
            calendar.set(
                dateTimeView.date_time_date_picker.year,
                dateTimeView.date_time_date_picker.month,
                dateTimeView.date_time_date_picker.dayOfMonth,
                dateTimeView.date_time_time_picker.currentHour,
                dateTimeView.date_time_time_picker.currentMinute
            )
        }
        onListen(calendar.time)
        alert.dismiss()
    }

    alert.show()
}

fun Fragment.log(tag: String, message: String) {
    Log.e(tag, message)
}

fun getAndroidSDKVersion(): Int {
    return android.os.Build.VERSION.SDK_INT
}

fun Context.openMonthlyPiker(onComplete: (month: Int, year: Int?) -> Unit) {
    var year: Int? = null
    val builder = MonthPickerDialog.Builder(
        this,
        MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
            onComplete(selectedMonth, year)
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH)
    )

    builder.apply {
        setOnYearChangedListener {
            year = it
        }

        //showMonthOnly()
        setTitle(this@openMonthlyPiker.getString(R.string.select_month))
        build().show()
    }
}

/**
 * This function takes a vector resource id and return a bitmap from it
 * This function just create a bitmap from a vector that is in the resource of the application
 * @param context to get the drawable
 * @param vectorResId the id of the drawable
 * @return Bitmap the bitmap drawn with the vector
 * @author Yvana
 */
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * this function just resize a bitmap
 * @param bm the old bitmap you want to resize
 * @param newWidth the new width you want
 * @param newHeight the new height you want
 * @return Bitmap the new bitmap resized
 * @param Yvana
 */
fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    try {
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    } catch (e: java.lang.Exception) {
        return bm
    }
}

/**
 * Cette fonction vérifie si un operateur donnée est présent dans
 * la liste mobileMoneyNumber d'un utilisateur
 * @param mobileMoneyNumber: Liste des numéro mbileMoney d'un utilisateur
 * @param operatorName: le nom de l'opérateur rechercher
 * @return le couple numéro-poerateur rechercher s'il existe dans la liste
 * @return null si l'operateur n'existe pas dans la liste
 * */
fun getOperatorFromList(
    mobileMoneyNumber: ArrayList<NumberForOperator>,
    operatorName: String
): NumberForOperator? {

    //on vérifie si l'operateur rechercher est présent dans la liste et on le retourne
    val curOpName = mobileMoneyNumber.filter { it.operatorName == operatorName }
    return if (curOpName.isNotEmpty()) {
        curOpName[0]
    } else {
        null
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun getOperatorFromPhoneNumber(phoneNumber: String): ENUMOPERATEUR {

    return if (PhoneNumberUtils.isOrangeOperator(phoneNumber)) ENUMOPERATEUR.ORANGE_MONEY else ENUMOPERATEUR.MTN_MONEY
}

/**Cette fonction a pour but de prendre une image et de la compresser
 * @param Uri: l'uri de l'image à compresser
 * @param context: le  contexte de l'application ou du fragment
 * @return compressedImag: l'Uri de l'image compressée
 * */
fun compressImageFIle(fileUri: Uri, context: Context, onFinish: (compressedImag: Uri) -> Unit) {

    val job = Job()
    val coroutineContext = job + Dispatchers.IO

    CoroutineScope(coroutineContext).launch {
        Dispatchers.IO

        var actualImageFile: File? = null
        var compressedImageFile: File? = null
        try {
            actualImageFile = File(getRealPathFromUri(context, fileUri)!!)
            compressedImageFile = Compressor.compress(context, actualImageFile)
        } catch (e: Exception) {
            e.printStackTrace()
            onFinish(fileUri)
            //return@launch
        }
        onFinish(Uri.fromFile(compressedImageFile))
    }

}

suspend fun compressImageFIle(fileUri: Uri, context: Context): Uri {

    var actualImageFile: File? = null
    var compressedImageFile: File? = null
    try {
        actualImageFile = File(getRealPathFromUri(context, fileUri)!!)
        compressedImageFile = Compressor.compress(context, actualImageFile)
        return Uri.fromFile(compressedImageFile)
    } catch (e: Exception) {
        e.printStackTrace()
        return fileUri
    }

}

fun getRealPathFromUri(
    context: Context,
    contentUri: Uri?
): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}


fun isEmailValid(textEmail: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()
}

fun getCasflowTotal(list: List<Cashflow>): HashMap<String, Double> {
    var result = HashMap<String, Double>()
    var totalIncomes = 0.0
    var TotalOutcomes = 0.0
    list.forEach {
        if (it.category?.equals(ENUMCATEGORYEXPENSE.INCOME) ?: false) {
            totalIncomes += it.amount
        } else {
            TotalOutcomes += it.amount
        }
    }
    result.put("income", totalIncomes)
    result.put("outcome", TotalOutcomes)
    result.put("total", totalIncomes + TotalOutcomes)
    return result
}

fun convertStoreToString(strore: SavedStoreInfo): String {
    return Gson().toJson(strore)

}

fun getStoreFromString(storeString: String): SavedStoreInfo? {
    return try {
        Gson().fromJson(storeString, SavedStoreInfo::class.java)
    } catch (ex: JsonSyntaxException) {
        ex.printStackTrace()
        return null
    }
}

fun getImageContentUri(
    context: Context,
    imageFile: File
): Uri? {
    val filePath = imageFile.absolutePath
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ",
        arrayOf(filePath),
        null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(
            cursor
                .getColumnIndex(MediaStore.MediaColumns._ID)
        )
        val baseUri =
            Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            null
        }
    }
}

fun getDaysFromPeriod(period: String): Int {
    when (period) {
        "weekly" -> return 6
        "Monthly" -> return 30
        "trimester" -> return 90
        "semester" -> return 180
        "annual" -> return 365
        else -> return 6
    }
}

fun getUrlOfImageMessage(messageContain: String): String {
    return messageContain.split(commonFireStoreRefKeyWord.SPLITOR)[0]
}

fun getCommentOfImageMessage(messageContain: String): String {
    return try {
        messageContain.split(commonFireStoreRefKeyWord.SPLITOR)[1]
    } catch (e: Exception) {
        ""
    }
}

fun shareInvitation(title: String, subjet: String, context: Context) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, "$title $subjet")
    intent.type = "text/plain"
    context.startActivity(intent)
}

/**
 * this function is for design and display the action bar in the fragment with a title, and a background
 * To add a menu in the action bar we must add the function setHasOptionMenu(true) in the fragment
 * @author Yvana
 * @param activity the current activity
 * @param title the title of your fragment
 * @param background the drawable that you want to display in the actionBar
 * @param hasHomeMenu state if the fragment has or not the home button. the default is true.
 * @return ActionBar the function return the action bar created if we want specifics adding...
 */
fun defineActionbar(
    activity: AppCompatActivity,
    title: String,
    background: Drawable,
    hasHomeMenu: Boolean = true,
    elevationValue: Float = 0F
): ActionBar {
    return activity.supportActionBar!!.apply {
        setTitle(title)
        elevation = elevationValue
        setBackgroundDrawable(background)
        setDisplayHomeAsUpEnabled(hasHomeMenu)
        //setDisplayShowHomeEnabled(hasHomeMenu)
        show()
    }
}

/**
 * Sometimes we want to display a number but the number is too long and we can display it
 * with k at the end if it is hundred number and m if it is a million number.
 * This function divide the number to know if the number is a hundred or a million number
 * @author Yvana
 * @param number the number to format
 * @return String
 */
fun formatLongNumber(number: Double): String {
    var numberString = ""
    if (Math.abs(number / 1000000000) > 1) {
        numberString = (number / 1000000000).formatDouble().removeZeroAtEnd() + "b"
    } else if (Math.abs(number / 1000000) > 1) {
        numberString = (number / 1000000).formatDouble().removeZeroAtEnd() + "m"

    } else if (Math.abs(number / 1000) > 1) {
        numberString = (number / 1000).formatDouble().removeZeroAtEnd() + "k"

    } else {
        numberString = number.formatDouble().removeZeroAtEnd()

    }
    return numberString
}

fun formatLongLongNumber(number: Double): String {
    if (number >= 100000) {
        var numberString = ""
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000).formatDouble().removeZeroAtEnd() + "m"

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000).formatDouble().removeZeroAtEnd() + "k"

        } else {
            numberString = number.formatNumberWithSpaceBetweenThousand()

        }
        return numberString
    } else {
        return number.formatNumberWithSpaceBetweenThousand()
    }

}

fun formatDate(sendingDate: Date, context: Context): String {
    val todayDate = convertDateToSpecificStringFormat(Calendar.getInstance().time, "dd MMM yyy")
    val sendDate = convertDateToSpecificStringFormat(sendingDate, "dd MMM yyy")
    if (todayDate == sendDate) {
        return "${context.getString(R.string.today)}: ${
            convertDateToSpecificStringFormat(
                sendingDate,
                "kk:mm"
            )
        }"
    }
    return convertDateToSpecificStringFormat(sendingDate)
}



/**
 * to format date to a specific format like have only the 3 first letters of a month to have custom date
 * @param date the date to format
 * @param format the format of date that who want
 * @return the date formatted
 */
fun convertDateToSpecificStringFormat(date: Date, format: String = "dd MMM yyyy kk:mm"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}

fun Uri.getName(context: Context): String {
    val returnCursor = context.contentResolver.query(this, null, null, null, null)
    var nameIndex: Int = 0
    try {
        nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    } catch (e: Exception) {
        val randomName = UUID.randomUUID().toString()
        return randomName.substring(randomName.length - 15, randomName.length) + ".pdf"
    }
    returnCursor.moveToFirst()
    val fileName = returnCursor.getString(nameIndex)
    returnCursor.close()
    return fileName
}

fun tutorialForView(
    activity: Activity,
    view: View,
    title: String,
    description: String,
    onListen: (View) -> Unit,
    titleSize: Int = 20,
    descriptionSize: Int = 16,
    textColor: Int = R.color.white,
    outerCircleColor: Int = R.color.tutorial_background,
    outerCircleAlpha: Float = 0.85f,
    targetCircleColor: Int = R.color.colorAccent,
    isTransparentTarget: Boolean = true,
    isCancelable: Boolean = false,
    targetRaduis: Int = 50
) {

    TapTargetView.showFor(activity,  // `this` is an Activity
        TapTarget.forView(
            view,
            title,
            description
        ) // All options below are optional
            .outerCircleColor(outerCircleColor) // Specify a color for the outer circle
            .outerCircleAlpha(outerCircleAlpha) // Specify the alpha amount for the outer circle
            .targetCircleColor(targetCircleColor) // Specify a color for the target circle
            .titleTextSize(titleSize) // Specify the size (in sp) of the title text
            .titleTextColor(textColor) // Specify the color of the title text
            .descriptionTextSize(descriptionSize) // Specify the size (in sp) of the description text
            .descriptionTextColor(textColor) // Specify the color of the description text
            .textColor(textColor) // Specify a color for both the title and description text
            .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
            .descriptionTextAlpha(1.0f)
            .dimColor(R.color.colorPrimary) // If set, will dim behind the view with 30% opacity of the given color
            .drawShadow(true) // Whether to draw a drop shadow or not
            .cancelable(isCancelable) // Whether tapping outside the outer circle dismisses the view
            .tintTarget(true) // Whether to tint the target view's color
            .transparentTarget(isTransparentTarget) // Specify whether the target is transparent (displays the content underneath)
//                .icon(resources.getDrawable(R.drawable.ic_settings_black_24dp)) // Specify a custom drawable to draw as the target
            .targetRadius(targetRaduis),  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view) // This call is optional
                onListen(view)
            }
        })
}

fun getTapTargetForView(
    view: View,
    title: String,
    description: String,
    titleSize: Int = 20,
    descriptionSize: Int = 16,
    textColor: Int = R.color.white,
    outerCircleColor: Int = R.color.tutorial_background,
    outerCircleAlpha: Float = 0.85f,
    targetCircleColor: Int = R.color.colorAccent,
    isTransparentTarget: Boolean = true,
    isCancelable: Boolean = false,
    targetRaduis: Int = 50
): TapTarget {
    return TapTarget.forView(
        view,
        title,
        description
    ) // All options below are optional
        .outerCircleColor(outerCircleColor) // Specify a color for the outer circle
        .outerCircleAlpha(outerCircleAlpha) // Specify the alpha amount for the outer circle
        .targetCircleColor(targetCircleColor) // Specify a color for the target circle
        .titleTextSize(titleSize) // Specify the size (in sp) of the title text
        .titleTextColor(textColor) // Specify the color of the title text
        .descriptionTextSize(descriptionSize) // Specify the size (in sp) of the description text
        .descriptionTextColor(textColor) // Specify the color of the description text
        .textColor(textColor) // Specify a color for both the title and description text
        .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
        .descriptionTextAlpha(1.0f)
        .dimColor(R.color.colorPrimary) // If set, will dim behind the view with 30% opacity of the given color
        .drawShadow(true) // Whether to draw a drop shadow or not
        .cancelable(isCancelable) // Whether tapping outside the outer circle dismisses the view
        .tintTarget(true) // Whether to tint the target view's color
        .transparentTarget(isTransparentTarget) // Specify whether the target is transparent (displays the content underneath)
//                .icon(resources.getDrawable(R.drawable.ic_settings_black_24dp)) // Specify a custom drawable to draw as the target
        .targetRadius(targetRaduis)
}

fun getTapTargetForBounds(
    left: Int,
    right: Int,
    top: Int,
    bottom: Int,
    title: String,
    description: String,
    titleSize: Int = 20,
    descriptionSize: Int = 16,
    textColor: Int = R.color.white,
    outerCircleColor: Int = R.color.tutorial_background,
    outerCircleAlpha: Float = 0.85f,
    targetCircleColor: Int = R.color.colorAccent,
    isTransparentTarget: Boolean = true,
    isCancelable: Boolean = false,
    targetRaduis: Int = 50
): TapTarget {
    return TapTarget.forBounds(
        Rect(left, top, right, bottom),
        title,
        description
    )
        .dimColor(outerCircleColor)
        .outerCircleColor(outerCircleColor)
        .titleTextSize(titleSize)
        .textColor(textColor)
        .descriptionTextAlpha(1.0f)
        .descriptionTextSize(descriptionSize)
        .outerCircleAlpha(outerCircleAlpha)
        .transparentTarget(isTransparentTarget)
        .targetCircleColor(targetCircleColor)
        .targetRadius(targetRaduis)
        .cancelable(isCancelable)
}

//pour recupere la transche de date de recuperation des transaction de lutilisateur en fonction de ses prefrences
fun getRangeDatePreferenceOfUser(): Long {
    return userPref.rangeToShowTransactions.getLimiteDateByPreference()
}


fun getCurrentTimeInMiliss() = Calendar.getInstance().time.time

//augmentation de 7 jours sur le delai de syncronisation des detected names
fun checkDetectedNameDelaiPerOneWeek() {
    val actualTimeMilliss = getCurrentTimeInMiliss()
    if (actualTimeMilliss >= userPref.checDetectedNameDelai) {
        //cas ou il fautmettre a jour les cachs en augmentant 7 jours
        userPref.checDetectedNameDelai = actualTimeMilliss + (getOneDayInMillis() * 7)
    }
}


//f0nction retournant une duree d'un jour
fun getOneDayInMillis(): Long {
    return 24 * 60 * 60 * 1000
}

fun Context.getRangeNameByPreference(): String {
    return when (userPref.rangeToShowTransactions) {
        1 -> {
            this.getString(R.string.weekly)
        }
        2 -> {
            this.getString(R.string.monthly)
        }
        3 -> {
            this.getString(R.string.trimester)
        }
        4 -> {
            this.getString(R.string.semester)
        }
        5 -> {
            this.getString(R.string.annual)
        }
        else -> {
            this.getString(R.string.all)
        }
    }
}

fun inAppReview(context: Context) {
    Log.d("Review", "usageRateOfTheApplication: ${userPref.usageRateOfTheApplication}")
    if (userPref.usageRateOfTheApplication != 0 && userPref.usageRateOfTheApplication % 15 == 0) {
        val manager =
            ReviewManagerFactory.create(context) //FakeReviewManager(requireContext())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(context as Activity, reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            } else {
                // There was some problem, continue regardless of the result.
            }
        }
    }
}

/**
 * Keyboard management
 *
 * to manage the key board in a fragment or an activity
 *
 * @param activity
 * @param isOpenFun the things to do when the key board is open
 * @param isCloseFun the things to do when the key board ic close
 */
fun keyboardManagement(activity: Activity, isOpenFun: () -> Unit, isCloseFun: () -> Unit) {
    KeyboardVisibilityEvent.setEventListener(
        activity,
        object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                try {
                    if (isOpen) {
                        isOpenFun()
                    } else {
                        isCloseFun()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        })
}

fun getColorStateList(context: Context, color: Int): ColorStateList {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ColorStateList.valueOf(context.getColor(color))
    } else {
        ColorStateList.valueOf(context.resources.getColor(color))
    }
}

fun <T> sortMapDescending(map: Map<DateForSort, T>): Map<DateForSort, T> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val group = map.toSortedMap(compareBy<DateForSort> {
            it.year
        }.thenBy {
            it.monthOfYear
        }.thenBy {
            it.dayOfMont
        }.reversed())

        group
    } else {
        val group = map.toSortedMap(compareBy<DateForSort> {
            it.year
        }.thenBy {
            it.monthOfYear
        }.thenBy {
            it.dayOfMont
        })
        val mutableMap = mutableMapOf<DateForSort, T>()

        (1..group.size).forEach {
            mutableMap.put(
                group.entries.elementAt(group.size - it).key,
                group.entries.elementAt(group.size - it).value
            )
        }
        mutableMap
    }
}

fun initBarChart(
    barChart: BarChart,
    values: ArrayList<BarEntry>,
    action: () -> Unit,
    chartColor: Int,
    valuesTextColor: Int = R.color.white
) {
    val set = BarDataSet(values, "")
    set.setDrawValues(false)
    set.color = chartColor
    set.valueTextColor = valuesTextColor

    val dataSets = arrayListOf<IBarDataSet>()
    dataSets.add(set)
    val chartData = BarData(dataSets)

    barChart.setDrawValueAboveBar(true)
    barChart.setFitBars(true)
    barChart.setDrawBarShadow(false)
    val description = Description()
    description.text = ""

    description.setPosition(30f, 50f)
    description.textSize = 11f
    description.textAlign = Paint.Align.LEFT
    barChart.description = description

    barChart.xAxis.setDrawAxisLine(false)
    barChart.xAxis.setDrawGridLines(false)
    barChart.xAxis.setCenterAxisLabels(false)
    barChart.xAxis.setDrawLabels(true)
    barChart.xAxis.textColor = valuesTextColor
    barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    barChart.axisLeft.setDrawAxisLine(false)
    barChart.axisLeft.setDrawLabels(false)
    barChart.axisLeft.textColor = valuesTextColor
    barChart.axisRight.textColor = valuesTextColor
    barChart.legend.isEnabled = false

    barChart.setOnClickListener {
        action()
    }
    barChart.data = chartData
    barChart.setScaleEnabled(false)
    barChart.invalidate()
}


