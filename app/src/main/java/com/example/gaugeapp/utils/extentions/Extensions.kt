package com.example.gaugeapp.utils.extentions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.withTranslation
import androidx.fragment.app.FragmentActivity
import com.beeline09.daterangepicker.date.DateRangePickerFragment
import com.example.gaugeapp.KolaWhalletApplication.Companion.userPref
import com.example.gaugeapp.R
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.DETECTED_NAMES
import com.example.gaugeapp.data.entities.Cashflow
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.kola.kola_entities_features.entities.PhoneDims
import com.kola.kola_entities_features.enums.ENUMEXPENSECATEGORYTYPE
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt


private const val TAG = "Extentions"

/**
 * to format date to a specific format like have only the 3 first letters of a month to have custom date
 * @param date the date to format
 * @param format the format of date that who want
 * @return the date formatted
 */
fun Date.convertDateToSpecificStringFormat(format: String = "dd MMM yyyy kk:mm"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}

/**
 * to convert date to specific format
 * this function put Today instead of the date with the hh:mm if is the date of today and Yesterday if it yesterday
 * In addition this function is able to add the day of week in the date
 * @param context the context to get the string in the resources
 * @param format the format of the date if it is not today or yesterday
 * @param isDayOfWeek to tell if you want the day of week or not. the default value is false
 */
fun Date.convertDateToASpecificFormatWithTodayAndYesterdayInCount(
    context: Context,
    format: String = "dd MMM yyyy kk:mm",
    isDayOfWeek: Boolean = false,
    withHour: Boolean = true
): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.time = this

    val hour = if (withHour) {
        this.convertDateToSpecificStringFormat(
            "kk:mm"
        )
    } else {
        ""
    }

    when {
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR) -> {
            return context.getString(R.string.today_str) + " " + hour
        }
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR) == 1 -> {
            return context.getString(R.string.yesterday_str) + " " + hour
        }
        isDayOfWeek -> {
            return if (this.getDayOfWeekFromDate().length > 3) {
                this.getDayOfWeekFromDate().substring(
                    0,
                    3
                ) + ", " + this.convertDateToSpecificStringFormat(format)
            } else {
                this.getDayOfWeekFromDate() + " " + this.convertDateToSpecificStringFormat(format)
            }
        }
        else -> {
            return this.convertDateToSpecificStringFormat(format)
        }
    }
}

/**
 * to get a year form a date because date property year is deprecated
 * @param date the date to have the year
 * @return the number of the year
 */
fun Date.getYearFromDate(): Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

/**
 * to get the month from a date because the property month of the class date is deprecated
 * @param date the date to have the month
 * @return the number of the month
 */
fun Date.getMonthFromDate(): Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    return calendar.get(Calendar.MONTH)
}

/**
 * to get the day of the month from a date
 */
fun Date.getDayOfMonthFromDate(): Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.getDayOfWeekFromDate(): String {
    val calendar = GregorianCalendar()
    calendar.time = this
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
}

fun Date.getLastDayOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, 1)
    calendar[Calendar.DAY_OF_MONTH] = 1
    calendar.add(Calendar.DATE, -1)
    val lastDayOfMonth = calendar.time
    return lastDayOfMonth
}

/**
 * Permet de formater la date et renvoie avec le mot
 * toDay(ayjourdhui si la date correspond à celle
 * du jours courant
 * **/
fun Date.formatDateWithToDay(context: Context, fromat: String = "dd MMM yyy"): String {
    val todayDate = this.convertDateToSpecificStringFormat("dd MMM yyy")
    val sendDate = this.convertDateToSpecificStringFormat("dd MMM yyy")
    if (todayDate == sendDate) {
        return "${context.getString(R.string.today)}: ${
            this.convertDateToSpecificStringFormat(
                "kk:mm"
            )
        }"
    }
    return this.convertDateToSpecificStringFormat(fromat)
}

fun Uri.getRealPathFromUri(
    context: Context
): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(this, proj, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}

fun Activity.getPhoneDims(): PhoneDims {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels

    Log.d(TAG, "Height: $height")
    Log.d(TAG, "width: $width")
    return PhoneDims(height.toFloat(), width.toFloat())
}

fun View.showSnackBar(text: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, text, duration).show()
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun Date.addDtates(number: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_MONTH, number)
    return cal.time
}

fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}

fun Date.addMonthOnDate(month: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MONTH, month)
    return cal.time
}

fun Date.addWeekToDate(nomberOfWeek: Int): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.WEEK_OF_MONTH, nomberOfWeek)
    return c.time
}

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("WrongConstant")

fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE,
    justificationMode: Int = Layout.JUSTIFICATION_MODE_NONE
) {

    val staticLayout = StaticLayout.Builder
        .obtain(text, start, end, textPaint, width)
        .setAlignment(alignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setBreakStrategy(1)
//        .setJustificationMode(justificationMode)
        .build()

    staticLayout.draw(this, x, y)
}

fun Activity.hideBottomNavifation() {
    try {
        val navBar = findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.apply {
            visibility = View.GONE
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.showBottomNavifation() {
    try {
        val navBar = findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.apply {
            visibility = View.VISIBLE
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) {
        multiplier *= 10
    }
    return round(this * multiplier) / multiplier
}


fun Double.removeZeroAtEnd(): String {
    val valueStr = "" + (this)
    val amountTab = (valueStr).split(".")
    return try {
        if (amountTab.size == 2 && amountTab.last().equals("0", true)) {
            amountTab[0]
        } else {
            valueStr
        }
    } catch (e: Exception) {
        valueStr
    }
}


/**
 * this function extends the SectionedRecyclerViewAdapter class.
 * It for update the adapter without recreate it completely.
 * It is like the update function of groupie library.
 * The function just give another list to the adapter to display.
 * @param list the new list of Section
 * @return Unit
 */
fun SectionedRecyclerViewAdapter.updateAllSection(list: List<Section>) {
    this.removeAllSections()
    list.forEach {
        this.addSection(it)
    }
    this.notifyDataSetChanged()
}


fun Date.getDelaitByValue(hour: Int): Long {
    val hourInMilis: Long = (hour * 60 * 60 * 1000).toLong()
    return (this.time + hourInMilis)
}


fun Long.getMinuteLeftOfTime(): String? {
    return try {
        val leftTime: Long = this - Calendar.getInstance().time.time
        val cal = Calendar.getInstance().apply {
            timeInMillis = leftTime
        }
        cal.get(Calendar.MINUTE).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}


//verifier si un compte est om ou momo et retourner licone correspondante
fun String.getIconOfNumber(
    onComplete: (Int?) -> Unit
) {
    try {
        val colOperator = when {
            PhoneNumberUtils.isOrangeOperator(this) -> {
                ENUMOPERATEUR.ORANGE_MONEY.name
            }
            PhoneNumberUtils.isMTNOoperator(this) -> {
                ENUMOPERATEUR.MTN_MONEY.name
            }
            else -> {
                "UNSUPORTED_OPERATOR"
            }
        }

        val validAcountList = userPref.getAllValideAcount()
        if (validAcountList.contains(PhoneNumberUtils.formatPhoneNumber(this))) {
            onComplete(this.getResourceIconeOperator(true))
        } else {
            val userPhoneNumber = PhoneNumberUtils.remove237ToPhoneNumber(this)

            FireStoreCollDocRef.firestoreInstance
                .collection(commonFireStoreRefKeyWord.PHONENUMBERS_AND_DETECTED_NAMES)
                .document(colOperator)
                .collection(DETECTED_NAMES)
                .document(userPhoneNumber)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userPhoneNumber.saveValideAccoutInCash()
                    }
                    onComplete(this.getResourceIconeOperator((document != null && document.exists())))
                }
                .addOnFailureListener { exception ->
                    onComplete(null)
                }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}

fun String.getResourceIconeOperator(isGoodAcount: Boolean = true): Int? {
    val operatorName = when {
        PhoneNumberUtils.isOrangeOperator(this) -> {
            ENUMOPERATEUR.ORANGE_MONEY.name
        }
        PhoneNumberUtils.isMTNOoperator(this) -> {
            ENUMOPERATEUR.MTN_MONEY.name
        }
        else -> {
            "UNSUPORTED_OPERATOR"
        }
    }
    return when (operatorName) {
        ENUMOPERATEUR.ORANGE_MONEY.name -> {
            if (isGoodAcount) {
                R.drawable.orange_money_new
            } else {
                R.drawable.orangelogo
            }
        }
        ENUMOPERATEUR.MTN_MONEY.name -> {
            if (isGoodAcount) {
                R.drawable.mtn_momo_new
            } else {
                R.drawable.mtnlogo
            }
        }
        else -> {
            null
        }
    }
}

fun String.removeSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String.getIconeValidAcountInCash(): Int? {
    val validAcountList = userPref.getAllValideAcount()
    return this.getResourceIconeOperator(
        validAcountList.contains(
            PhoneNumberUtils.formatPhoneNumber(
                this
            )
        )
    )
}

fun String.isSamePhoneNumber(other: String): Boolean {
    return PhoneNumberUtils.remove237ToPhoneNumber(this) == PhoneNumberUtils.remove237ToPhoneNumber(
        other
    )
}

@SuppressLint("ResourceType")
fun TextView.displayAsBookkeeping(currentCashflow: Cashflow) {
    this.setTextColor(
        if (currentCashflow.category?.category?.type == ENUMEXPENSECATEGORYTYPE.EARNING) {
            context.resources.getInteger(R.color.green_incomes)
        } else if (currentCashflow.category?.category?.type == ENUMEXPENSECATEGORYTYPE.SPENDING) {
            context.resources.getInteger(R.color.red_outcomes)
        } else {
            context.resources.getInteger(R.color.red_outcomes)
        }
    )
}

fun String.saveValideAccoutInCash() {
    userPref.saveValideAccount(this)
}

fun String.cleanPhoneNumber(): String {
    return this.removeSpaces().replace("(", "").replace(")", "").replace("+", "")
        .removePrefix("237")
}


// Afin d'afficher uniquement la premier et la dernière chaine de caractère du nom
fun String?.splitFirstAndSecondName(): String {
    val arrayName = this?.split(" ")
    var nameToShow = this ?: ""
    if (arrayName?.size ?: 0 > 1) {
        nameToShow = arrayName?.first() + " " + (arrayName?.get(1) ?: "")
    }
    return nameToShow
}

fun Int.getLimiteDateByPreference(): Long {
    val timeNow = Calendar.getInstance().timeInMillis
    val oneDayMillis: Long = 24 * 60 * 60 * 1000
    return when (this) {
        1 -> {
            //weekly
            timeNow - (oneDayMillis * 7)
        }
        2 -> {
            //monthly
            timeNow - (oneDayMillis * 30)
        }
        3 -> {
            //trimester
            timeNow - (oneDayMillis * 30 * 3)
        }
        4 -> {
            //semester
            timeNow - (oneDayMillis * 30 * 6)
        }
        5 -> {
            //annual
            timeNow - (oneDayMillis * 30 * 12)
        }
        else -> {
            //all
            0
        }
    }
}

//getBalance of User according to fake preference
fun Double.getBalnaceWithFakePreference(operatorName: String): Double {
    return when (userPref.balanceTheme) {
        BALANCE_FIX -> {
            if (operatorName.equals(ORANGE_OPERATOR_NAME)) {
                userPref.balanceFixValueOrange?.toDouble()
                    ?: 0.0
            } else {
                if (operatorName.equals(MTN_OPERATOR_NAME)) {
                    userPref.balanceFixValueMTN?.toDouble()
                        ?: 0.0
                } else {
                    userPref.balanceFixValueMTN?.toDouble()!! + userPref.balanceFixValueOrange?.toDouble()!!
                }
            }
        }
        BALANCE_PERCENT -> {
            ((userPref.balancePercentage?.toDouble()
                ?.times(this))?.div(100)) ?: 0.0
        }
        else -> {
            this
        }
    }
}


/**
 * detected name
 */
//to get detected name of user in cash
fun String.getDetectedNameInCash(defaultValue: String? = null): String {
    val nameUserInCash = userPref.getUserName(PhoneNumberUtils.remove237ToPhoneNumber(this)) ?: ""
    return defaultValue ?: nameUserInCash
}

//to save detectedNameInCash
fun String.saveDetectedNameInCash(userName: String) {
    if (userName.isNotBlank() && this.isNotBlank()) {
        userPref.saveUserName(
            PhoneNumberUtils.remove237ToPhoneNumber(this),
            userName
        )
    }
}

fun String.isNumericalValue(): Boolean {
    return try {
        this.toLong()
        true
    } catch (e: NumberFormatException) {
        false
    }
}


fun String.checkAndSaveDetectedName(onComplete: (String) -> Unit) {
    try {
        //recuperation du nom detecte dans les cash
        val nameUserInCash = this.getDetectedNameInCash()
        if (nameUserInCash.isBlank() && this.isNotBlank()) {
            FireStoreAuthUtil.getDetectedNameOfUser(
                this,
                onSucess = { name ->
                    this.saveDetectedNameInCash(name)
                    onComplete(this.getDetectedNameInCash())
                },
                onError = {
                    onComplete("")
                }
            )
        } else {
            onComplete(nameUserInCash)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


//to fetch detected name according to the delai
fun String.fetchDetectedName(onComplete: (String) -> Unit) {
    val actualTimeMilliss = getCurrentTimeInMiliss()
    if (actualTimeMilliss >= userPref.checDetectedNameDelai && this.isNumericalValue()) {
        this.checkAndSaveDetectedName {
            onComplete(it)
        }
    } else {
        onComplete(this)
    }
}
/**
 * detected name
 */


/**
 * user Image profile
 */
//to get image of user in cash
fun String.getUserImageProfilInCash(defaultValue: String? = null): String {
    val userImageInCash = userPref.getUserImageUrl(PhoneNumberUtils.formatPhoneNumber(this)) ?: ""
    return defaultValue ?: userImageInCash
}

//to save user image profile In Cash
fun String.saveUserImageProfileInCash(userImageUrl: String) {
    if (userImageUrl.isNotBlank() && this.isNotBlank()) {
        userPref.saveUserImage(
            PhoneNumberUtils.formatPhoneNumber(this),
            userImageUrl
        )
    }
}


fun String.checkAndSaveUserImageProfile(
    onComplete: (String) -> Unit,
    replaceValueInCash: Boolean = false
) {
    try {
        //recuperation du l'image dans les cash
        val userImageProfileInCash = if (replaceValueInCash) {
            ""
        } else {
            this.getUserImageProfilInCash()
        }

        if (userImageProfileInCash.isBlank() && this.isNotBlank()) {
            FireStoreAuthUtil.getUserFromFiresToreByAnyPhoneNumber(
                this,
                onSucess = { user ->
                    this.saveUserImageProfileInCash(user.imageUrL)
                    onComplete(this.getUserImageProfilInCash())
                },
                onError = {
                    try {
                        onComplete(this.getUserImageProfilInCash())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        } else {
            onComplete(userImageProfileInCash)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


//to fetch user image url according to the delai
fun String.fetchUserImageProfile(onComplete: (String) -> Unit) {
    val actualTimeMilliss = getCurrentTimeInMiliss()
    if (actualTimeMilliss >= userPref.checDetectedNameDelai && this.isNumericalValue()) {
        this.checkAndSaveUserImageProfile(
            onComplete = {
                onComplete(it)
            },
            replaceValueInCash = true
        )
    } else {
        onComplete(this.getUserImageProfilInCash())
    }
}

/**
 * user Image profile
 */


fun String.getMiniNameOfPeriode(): String {
    if (this.isNotBlank() && this.length > 3) {
        when {
            (this[0].toString().isNumericalValue() && this[1].toString().isNumericalValue()) -> {
                return this.removeSpaces().substring(0, 3)
            }
            (this[0].toString().isNumericalValue() && !(this[1].toString().isNumericalValue())) -> {
                return this.removeSpaces().substring(0, 2)
            }
            (!this[0].toString().isNumericalValue()) -> {
                return this.first().toString().toUpperCase(Locale.ROOT)
            }
        }
    } else {
        return this
    }
    return this
}

fun FragmentActivity.openRangePicker(
    onComplete: (yearStart: Int, monthStart: Int, dayStart: Int, yearEnd: Int, monthEnd: Int, dayEnd: Int) -> Unit,
    onCancelled: () -> Unit
) {

    val dateRangePickerFragment = DateRangePickerFragment.newInstance(
        object : DateRangePickerFragment.OnDateRangeSetListener {

            override fun onDateRangeSet(
                view: DateRangePickerFragment,
                yearStart: Int,
                monthStart: Int,
                dayStart: Int,
                yearEnd: Int,
                monthEnd: Int,
                dayEnd: Int
            ) {
                onComplete(yearStart, monthStart, dayStart, yearEnd, monthEnd, dayEnd)
            }


        }

    )

    dateRangePickerFragment.setOnCancelListener(object : DialogInterface.OnCancelListener {
        override fun onCancel(dialog: DialogInterface?) {
            onCancelled()
        }

    })
    dateRangePickerFragment.show(this.supportFragmentManager, "dateRangePicker")
}

fun MenuItem.applyIconeOnFilterAmount(desc: Boolean, context: Context) {
    if (desc) {
        this.icon = context.getDrawable(R.drawable.ic_baseline_arrow_downward)
    } else {
        this.icon = context.getDrawable(R.drawable.ic_baseline_arrow_upward)
    }
}

fun Context.showTexteForStartAndEndDate(startDate: Date, endDate: Date): String {
    //val to = this.getString(R.string.to_au)
    return "${startDate.convertDateToSpecificStringFormat("dd MMM yyyy")}  -  ${
        endDate.convertDateToSpecificStringFormat(
            "dd MMM yyyy"
        )
    }".capitalize()
}

fun BottomSheetBehavior<*>.showBottomSheet() {
    try {
        this.state = BottomSheetBehavior.STATE_EXPANDED
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun BottomSheetBehavior<*>.hideBottomSheet() {
    try {
        this.state = BottomSheetBehavior.STATE_COLLAPSED
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun String.formatPhoneNumberWithSpace(): String {
    return formatPhoneNumberWithSpace(
        PhoneNumberUtils.remove237ToPhoneNumber(
            this
        )
    )
}

fun String.fecthDetectedNameAndImageUrlInCash(onComplete: (userDetectedName: String, userImageUrl: String) -> Unit) {
    val userImageUrl = this.getUserImageProfilInCash()
    val userName = this.getDetectedNameInCash()
    if (userImageUrl.isNotBlank() && userName.isNotBlank()) {
        onComplete(userName, userImageUrl)
    } else if (userImageUrl.isBlank() && userName.isNotBlank()) {
        this.checkAndSaveUserImageProfile(onComplete = {
            onComplete(userName, it)
        })
    } else if (userImageUrl.isNotBlank() && userName.isBlank()) {
        this.checkAndSaveDetectedName {
            onComplete(it, userImageUrl)
        }
    }
}


fun Bitmap.convertToImage(context: Context, directory: String, fileName: String): Uri? {

    val root = Environment.getExternalStorageDirectory().toString()
    val myDir = File("$root/${context.resources.getString(R.string.app_name)}/${directory}")
    myDir.mkdirs()

    val file = File(myDir, fileName)
    if (file.exists()) file.delete();
    try {
        val out = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();

    } catch (e: Exception) {
        e.printStackTrace();
    }
    //return Uri.fromFile(file)
    return getImageContentUri(context, file)
}

/**
 * Open key board
 *
 * to open the keyboard with a view in focus
 * @author Yvana
 * @param activity
 */
fun View.openKeyBoard(activity: Activity) {
    this.requestFocus()
    val imm: InputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(
        this,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}


// Extension function to resize bitmap using new width value by keeping aspect ratio
fun Bitmap.resizeByWidth(width: Int): Bitmap {
    val ratio: Float = this.width.toFloat() / this.height.toFloat()
    val height: Int = (width / ratio).roundToInt()

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}


// Extension function to resize bitmap using new height value by keeping aspect ratio
fun Bitmap.resizeByHeight(height: Int): Bitmap {
    val ratio: Float = this.height.toFloat() / this.width.toFloat()
    val width: Int = (height / ratio).roundToInt()

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}


fun Bitmap.resize(width: Int, height: Int): Bitmap {
    val ratio: Float = this.height.toFloat() / this.width.toFloat()
    val w: Int = (height / ratio).roundToInt()
    val h: Int = (width / ratio).roundToInt()

    return Bitmap.createScaledBitmap(this, width, height, false)
}

