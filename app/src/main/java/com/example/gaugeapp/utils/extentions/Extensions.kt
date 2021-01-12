package com.example.gaugeapp.utils.extentions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
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
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.withTranslation
import androidx.fragment.app.FragmentActivity
import com.beeline09.daterangepicker.date.DateRangePickerFragment
import com.example.gaugeapp.R
import com.example.gaugeapp.data.entities.Cashflow
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.kola.kola_entities_features.entities.PhoneDims
import com.kola.kola_entities_features.enums.ENUMEXPENSECATEGORYTYPE
import com.example.gaugeapp.KolaWhalletApplication.Companion.userPref
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.DETECTED_NAMES
import com.example.gaugeapp.commonRepositories.roomRep.roomEntities.SmsTable
import com.example.gaugeapp.data.enums.ENUMFEESTRANSACTION
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.*

import com.kola.smsmodule.entities.CustumSMS
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


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

fun Double.getTransactionFees(serviceType: ENUM_SERVICE_ANALYSIS): Double {
    var fees = 0.0
    ENUMFEESTRANSACTION.values().filter { enumFree ->
        enumFree.feesTransaction.operateur == serviceType
    }.forEach {
        if (this >= it.feesTransaction.min && this <= it.feesTransaction.max) {
            return ((this * it.feesTransaction.percentage) + it.feesTransaction.feesValues).round(2)
        }
    }
    return fees
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

fun Activity.generatePdfOfTransactionList(list: ArrayList<CustumSMS>?, title: String? = null) {
    if (ActivityCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1
        )
    } else {
        buidPdf(list, title)
        /*val job = Job()
        val coroutineContext = job + Dispatchers.IO
        CoroutineScope(coroutineContext).launch {
            Dispatchers.IO
            buidPdf(list, title)
        }*/
    }
}

private fun Context.buidPdf(list: ArrayList<CustumSMS>?, title: String? = null) {
    list?.let { newList ->
        val transactionList = arrayListOf<CustumSMS>()

        transactionList.apply {
            try {
                addAll(newList.subList(0, 20))
            } catch (e: Exception) {
                e.printStackTrace()
                addAll(newList)
            }
        }

        Log.d("PDF_GENERATE", transactionList.size.toString())


        val context = this
        val document = PdfDocument()
        // crate a page description
        val pageInfo: PdfDocument.PageInfo =
            PdfDocument.PageInfo.Builder(2100, 2970, 1).create()
        // start a page
        val page: PdfDocument.Page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        val margin = 200.0
        val marginTop = 500.0
        val marginRight = 1900.0
        val y = marginTop + 200.0

        createPdfCore(
            paint,
            canvas,
            margin,
            marginTop,
            marginRight,
            y,
            context,
            transactionList,
            document,
            page,
            pageInfo
        )

        // close the document
        document.close()
    }
}


private fun createPdfCore(
    paint: Paint,
    canvas: Canvas,
    margin: Double,
    marginTop: Double,
    marginRight: Double,
    y: Double,
    context: Context,
    transactionList: ArrayList<CustumSMS>,
    document: PdfDocument,
    page: PdfDocument.Page,
    pageInfo: PdfDocument.PageInfo
) {

    //display the name of the application

    val xName = 600.0
    val yName = 450.0

    paint.setColor(Color.BLACK)
    val nameSize = 80.0
    paint.textSize = nameSize.toFloat()

    canvas.drawText(
        context.getString(R.string.transaction_history),
        xName.toFloat(),
        yName.toFloat(),
        paint
    )

    var canvas1 = canvas
    var y1 = y
    var page1 = page
    paint.setColor(Color.GRAY)

    val xdate = 200.0
    val xOperator = 450.0
    val xCategory = 1000.0
    val xAmount = 1200.0
    val xSender = 1450.0
    val xReceiver = 1700.0

    //display the title of the table
    canvas1.drawRect(
        (xdate - 10.0).toFloat(),
        (marginTop + 100.0).toFloat(),
        marginRight.toFloat(),
        (y1 + 20).toFloat(),
        paint
    )

    val titlesize = 40.0
    paint.setColor(Color.WHITE)
    paint.textSize = titlesize.toFloat()


    //definition des colones
    canvas1.drawText(
        context.getString(R.string.date),
        xdate.toFloat(),
        y1.toFloat(),
        paint
    )
    canvas1.drawText(
        context.getString(R.string.operator),
        xOperator.toFloat(),
        y1.toFloat(),
        paint
    )

    canvas1.drawText(
        context.getString(R.string.category),
        xCategory.toFloat(),
        y1.toFloat(),
        paint
    )

    canvas1.drawText(
        context.getString(R.string.amount),
        xAmount.toFloat(),
        y1.toFloat(),
        paint
    )

    canvas1.drawText(
        context.getString(R.string.sender),
        xSender.toFloat(),
        y1.toFloat(),
        paint
    )
    canvas1.drawText(
        context.getString(R.string.receiver),
        xReceiver.toFloat(),
        y1.toFloat(),
        paint
    )

    val textsize = 30.0
    paint.setColor(Color.BLACK)
    paint.textSize = textsize.toFloat()

    y1 += 100.0

    //display all the informations
    if (transactionList.isNotEmpty()) {
        transactionList.forEach {

            //date
            canvas1.drawText(
                convertDateToSpecificStringFormat(
                    it.transaction?.date!!,
                    "dd MMM yyyy"
                ),
                xdate.toFloat(),
                y1.toFloat(),
                paint
            )

            //operator
            canvas1.drawText(
                it.transaction?.transactionService?.curentService?.serviceOperateur?.curentServiceOperator!!.aireTimeService,
                xOperator.toFloat(),
                y1.toFloat(),
                paint
            )

            //category
            canvas1.drawText(
                it.transaction?.transactionService?.curentService!!.name,
                xCategory.toFloat(),
                y1.toFloat(),
                paint
            )

            //amount
            canvas1.drawText(
                it.transaction?.montantTransaction!!.removeZeroAtEnd(),
                xAmount.toFloat(),
                y1.toFloat(),
                paint
            )

            //sender
            canvas1.drawText(
                it.transaction?.senderName!!,
                xSender.toFloat(),
                y1.toFloat(),
                paint
            )

            //receiver
            canvas1.drawText(
                it.transaction?.recipientName!!,
                xReceiver.toFloat(),
                y1.toFloat(),
                paint
            )

            y1 += 20.0
            canvas1.drawLine(
                (xdate - 10.0).toFloat(),
                y1.toFloat(),
                (marginRight - 10.0).toFloat(),
                y1.toFloat(),
                paint
            )
            y1 += 50.0

            // if the data are too much for just one page we have to add pages dynamically

            if (y1 >= 2900) {
                document.finishPage(page1)

                page1 = document.startPage(pageInfo)
                canvas1 = page1.canvas

                y1 = 200.0
            }
        }
    }

    y1 += 100.0

    val xPied = 200.0

    paint.setColor(Color.BLACK)
    val piedSize = 30.0
    paint.textSize = piedSize.toFloat()

    canvas.drawText(context.getString(R.string.made_by), xPied.toFloat(), y1.toFloat(), paint)

    y1 += 45.0
    canvas.drawText(
        "${context.getString(R.string.at)} ${Date()}",
        xPied.toFloat(),
        y1.toFloat(),
        paint
    )

    // finish the page
    document.finishPage(page1)

    // write the document content
    val directory_path =
        Environment.getExternalStorageDirectory()
            .getPath() + "/${commonFireStoreRefKeyWord.KOLA_WALLET}/pdf/"
    val file = File(directory_path)
    if (!file.exists()) {
        file.mkdirs()
    }
    val targetPdf = directory_path + "Transactions_${Date()}.pdf"
    val filePath = File(targetPdf)
    try {
        document.writeTo(FileOutputStream(filePath))
        Log.d("TAG", "done")
        context.toast(context.getString(R.string.pdf_created))

        //open the file when is success made
        val uriPath = Uri.fromFile(filePath)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)

        val apkURI = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            filePath
        )
        intent.setDataAndType(apkURI, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    } catch (e: IOException) {
        context.toast(context.getString(R.string.error_message_gen) + e.message)
        e.printStackTrace()
        Log.e("main", "error $e")
    } catch (e: ActivityNotFoundException) {
        context.toast(context.getString(R.string.error_message_gen) + e.message)
        e.printStackTrace()
        Log.e("main", "error file $e")
    } catch (e: java.lang.Exception) {
        context.toast(context.getString(R.string.error_message_gen))
        e.printStackTrace()
        Log.e("main", "error $e")
    }
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

fun ArrayList<CustumSMS>.verifyIfItIsTheSameUser(onComplete: (phoneNumber: String?, detectedName: String, imageUrl: String) -> Unit) {
    if (this.isEmpty()) {
        onComplete(null, "", "")
    } else {
        var firstPhoneNumber =
            PhoneNumberUtils.remove237ToPhoneNumber(this[0].transaction!!.recipientPhoneNumber)
        var mylist = this.filter { custumSMS ->
            PhoneNumberUtils.remove237ToPhoneNumber(custumSMS.transaction!!.recipientPhoneNumber) == firstPhoneNumber
                    ||
                    PhoneNumberUtils.remove237ToPhoneNumber(custumSMS.transaction!!.senderPhoneNumber) == firstPhoneNumber
        }
        if (firstPhoneNumber.isNotBlank() && mylist.size == this.size) {
            firstPhoneNumber.fecthDetectedNameAndImageUrlInCash { userDetectedName, userImageUrl ->
                onComplete(firstPhoneNumber, userDetectedName, userImageUrl)
            }
        } else {
            firstPhoneNumber =
                PhoneNumberUtils.remove237ToPhoneNumber(this[0].transaction!!.senderPhoneNumber)
            mylist = this.filter { custumSMS ->
                PhoneNumberUtils.remove237ToPhoneNumber(custumSMS.transaction!!.recipientPhoneNumber) == firstPhoneNumber
                        ||
                        PhoneNumberUtils.remove237ToPhoneNumber(custumSMS.transaction!!.senderPhoneNumber) == firstPhoneNumber
            }
            if (firstPhoneNumber.isNotBlank() && mylist.size == this.size) {
                firstPhoneNumber.fecthDetectedNameAndImageUrlInCash { userDetectedName, userImageUrl ->
                    onComplete(firstPhoneNumber, userDetectedName, userImageUrl)
                }
            } else {
                onComplete(null, "", "")
            }
        }
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


fun String?.getOperatorOfPhoneNumber(): ENUM_SERVICE_OPERATEUR? {
    return if (this != null) {
        when {
            PhoneNumberUtils.isOrangeOperator(this) -> {
                ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY
            }
            PhoneNumberUtils.isMTNOoperator(this) -> {
                ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY
            }
            else -> {
                null
            }
        }
    } else {
        null
    }
}

//to convert custumSms to SmsTable
fun CustumSMS.convertToSmsTable(): SmsTable {
    val smsString = TransactionGsonConverter.convertCustumSmsToGson(this)
    return SmsTable(
        this.custumSMSObjet?.messageId!!,
        (this.custumSMSObjet?.dateReceive)?.time ?: Calendar.getInstance().timeInMillis,
        this.serviceOperateur!!.name,
        this.transaction?.transactionService?.name!!,
        false,
        smsString
    )
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

