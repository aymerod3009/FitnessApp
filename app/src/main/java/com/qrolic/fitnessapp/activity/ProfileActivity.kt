package com.qrolic.fitnessapp.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityProfileBinding
import com.qrolic.fitnessapp.model.ProfileTable
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity(), OnDateSetListener {

    private var today: Long = 0
    private var type: String? = "save"
    lateinit var simpleDateFormat: SimpleDateFormat
    var selectedDate: Date? = null
    lateinit var etDialogWeight: EditText
    lateinit var etDialogHeightFeet: EditText
    lateinit var etDialogHeightInch: EditText
    lateinit var etHeightCM: EditText
    lateinit var tvLB: TextView
    lateinit var tvKG: TextView
    lateinit var tvCM: TextView
    lateinit var tvIN: TextView
    lateinit var tvCancel: TextView
    lateinit var tvSave: TextView
    lateinit var tvDialogWeightUnit: TextView
    lateinit var llHeight: LinearLayout
    lateinit var llHeightCM: LinearLayout
    lateinit var db: FitnessDatabase
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var activityProfileBinding:ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        initAll()
    }

    private fun initAll() {
        supportActionBar!!.hide()
        db = FitnessDatabase.invoke(this)
        mySharedPreferences = MySharedPreferences(this)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR,-10)
        today = calendar.timeInMillis
        Log.d("ProfileActivity", "initAll: "+today)
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

        type = intent.getStringExtra("type")
        if (type.equals("update")) {
           activityProfileBinding.tvYearOfBirth.text = mySharedPreferences.getBirthDate()
            if (mySharedPreferences.getType().equals("lbft")) {
                activityProfileBinding.tvWeight.text = mySharedPreferences.getWeight()
                activityProfileBinding.tvHieght.text = mySharedPreferences.getHeight()
            } else {
                activityProfileBinding.tvWeight.text = DecimalFormat("#.#").format(
                    (mySharedPreferences.getWeight().toString().toDouble() * 2.205).toDouble()
                ).toString()

                var height = mySharedPreferences.getHeight().toString()
                    .substring(0, mySharedPreferences.getHeight().toString().indexOf(" CM"))
                var cm = (DecimalFormat("#.#").format((height).toDouble() / 30.48)).toString()


                if(cm.contains(".")) {
                    activityProfileBinding.tvHieght.text = cm.substring(0, cm.indexOf(".")).plus(" FT ")
                        .plus(
                            cm
                                .substring(cm.indexOf(".") + 1, cm.length)
                                .plus(" IN")
                        )
                }else{
                    activityProfileBinding.tvHieght.text = cm.substring(0, cm.length).plus(" FT ")
                        .plus("0")
                                .plus(" IN")

                }
            }

            activityProfileBinding.tvNext.text = "UPDATE"

        } else {
            activityProfileBinding.tvYearOfBirth.text = simpleDateFormat.format(today)

        }

        activityProfileBinding.tvYearOfBirth.setOnClickListener { onDateClicked() }
        activityProfileBinding.tvWeight.setOnClickListener { OnWeightClick() }
        activityProfileBinding.tvHieght.setOnClickListener { onHieghtClick() }
        activityProfileBinding.tvKGCM.setOnClickListener { tvKGCMClick() }
        activityProfileBinding.tvLBFT.setOnClickListener { tvLBFTClick() }
        activityProfileBinding.tvNext.setOnClickListener { onNextClick() }
        activityProfileBinding.ivBack.setOnClickListener { onBackButtonClick() }
    }

    fun onDateClicked() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR,-10)
        val dialog = DatePickerDialog(
            this@ProfileActivity,
            R.style.TimePickerTheme,
            this,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        dialog.getDatePicker().setMaxDate(calendar.timeInMillis);
        dialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val stringDate: String = p3.toString() + "-" + (p2 + 1) + "-" + p1
        try {
            selectedDate = simpleDateFormat.parse(stringDate)
            activityProfileBinding.tvYearOfBirth.text = (simpleDateFormat.format(selectedDate))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

     fun OnWeightClick() {
        dialogWeightHeight()
    }

    fun onHieghtClick() {
        dialogWeightHeight()
    }

    @SuppressWarnings("kotlin:S3776")
    //dialogWeightHeight() have cognitive complexity
    private fun dialogWeightHeight() {
        val dialogHeihtWeight = Dialog(this@ProfileActivity, android.R.style.Theme_Dialog)
        dialogHeihtWeight.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogHeihtWeight.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogHeihtWeight.setContentView(R.layout.dialog_height_weight)

        etDialogWeight = dialogHeihtWeight.findViewById<View>(R.id.etWeight) as EditText
        etDialogHeightFeet = dialogHeihtWeight.findViewById<View>(R.id.etHeightFeet) as EditText
        etDialogHeightInch = dialogHeihtWeight.findViewById<View>(R.id.etHeightInch) as EditText
        etHeightCM = dialogHeihtWeight.findViewById<View>(R.id.etHeightCM) as EditText
        tvLB = dialogHeihtWeight.findViewById<View>(R.id.tvLB) as TextView
        tvKG = dialogHeihtWeight.findViewById<View>(R.id.tvKG) as TextView
        tvCM = dialogHeihtWeight.findViewById<View>(R.id.tvCM) as TextView
        tvKG = dialogHeihtWeight.findViewById<View>(R.id.tvKG) as TextView
        tvIN = dialogHeihtWeight.findViewById<View>(R.id.tvIN) as TextView
        tvSave = dialogHeihtWeight.findViewById<View>(R.id.tvSave) as TextView
        tvCancel = dialogHeihtWeight.findViewById<View>(R.id.tvCancel) as TextView
        tvDialogWeightUnit =
            dialogHeihtWeight.findViewById<View>(R.id.tvDialogWeightUnit) as TextView
        llHeight = dialogHeihtWeight.findViewById<View>(R.id.llHeight) as LinearLayout
        llHeightCM = dialogHeihtWeight.findViewById<View>(R.id.llHeightCM) as LinearLayout

        dialogHeihtWeight.show()
        dialogHeihtWeight.setCancelable(true)

        /*
        set values to edit text
        */

        if (activityProfileBinding.tvWeightUnit.text.equals(" KG") && !tvLB.background.equals(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.gray_border_button,
                    null
                )
            ) && !tvIN.background.equals(
                ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
            )
        ) {
            etDialogWeight.text = Editable.Factory.getInstance().newEditable(
                activityProfileBinding.tvWeight.text.toString().substring(0, activityProfileBinding.tvWeight.text.toString().length).trim()
            )
            tvDialogWeightUnit.text = " KG"

            tvKG.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
            tvKG.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            tvLB.background =
                ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
            tvLB.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))

            tvCM.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
            tvCM.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            tvIN.background =
                ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
            tvIN.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))

            llHeightCM.visibility = View.VISIBLE
            llHeight.visibility = View.GONE

            etHeightCM.text = Editable.Factory.getInstance().newEditable(
                activityProfileBinding.tvHieght.text.toString().substring(0, activityProfileBinding.tvHieght.text.toString().indexOf(" CM"))
            )


        } else {
            etDialogWeight.text = Editable.Factory.getInstance().newEditable(
                activityProfileBinding.tvWeight.text.toString().substring(0, activityProfileBinding.tvWeight.text.toString().length).trim()
            )

            etDialogHeightFeet.text = Editable.Factory.getInstance().newEditable(
                activityProfileBinding.tvHieght.text.toString().substring(0, activityProfileBinding.tvHieght.text.toString().indexOf("F")).trim()
            )

            etDialogHeightInch.text = Editable.Factory.getInstance().newEditable(
                activityProfileBinding.tvHieght.text.toString().substring(
                    activityProfileBinding.tvHieght.text.toString().indexOf("T") + 1,
                    activityProfileBinding.tvHieght.text.toString().indexOf("IN")
                ).trim()
            )

        }


        tvLB.setOnClickListener(View.OnClickListener { view ->
            lbAndInConvert()
        })

        tvKG.setOnClickListener(View.OnClickListener { view ->
            kgcmConvert()
        })

        tvCM.setOnClickListener(View.OnClickListener { view ->
            kgcmConvert()
        })

        tvIN.setOnClickListener(View.OnClickListener { view ->
            lbAndInConvert()
        })

        tvCancel.setOnClickListener(View.OnClickListener { view ->
            dialogHeihtWeight.dismiss()
        })

        tvSave.setOnClickListener(View.OnClickListener { view ->

            if (etDialogWeight.text.toString().equals("") || etDialogWeight.text.toString()
                    .equals(".")
            ) {
                Toast.makeText(
                    applicationContext,
                    "please enter correct weight value",
                    Toast.LENGTH_LONG
                )
                    .show()
            }else if(tvDialogWeightUnit.text.toString().equals(" KG") &&(etHeightCM.text.toString().equals("") || etHeightCM.text.toString()
                    .equals(".") || etHeightCM.text.toString().startsWith(".") || etHeightCM.text.toString().endsWith("."))){
                Toast.makeText(applicationContext, "Please enter correct height value", Toast.LENGTH_LONG)
                    .show()
            }else if(etDialogHeightFeet.text.toString().equals("")){
                Toast.makeText(applicationContext, "please enter height value", Toast.LENGTH_LONG)
                    .show()
            }  else if(etDialogHeightFeet.text.toString().equals("") && etDialogHeightInch.text.toString().equals("")){
                Toast.makeText(applicationContext, "please enter  height value", Toast.LENGTH_LONG)
                    .show()
            }  else {
                dialogHeihtWeight.dismiss()

                if (tvDialogWeightUnit.text.toString().equals(" LB") && activityProfileBinding.tvWeightUnit.text.toString()
                        .equals(" LB")
                ) {
                    activityProfileBinding.tvHieght.text = etDialogHeightFeet.text.toString().plus(" FT ")
                        .plus(etDialogHeightInch.text.toString().plus(" IN"))
                    activityProfileBinding.tvWeight.text = etDialogWeight.text.toString()

                } else if (tvDialogWeightUnit.text.toString()
                        .equals(" KG") && activityProfileBinding.tvWeightUnit.text.toString().equals(" LB")
                ) {
                    etDialogWeight.text = Editable.Factory.getInstance()
                        .newEditable(
                            (DecimalFormat("#.#").format(
                                etDialogWeight.text.toString().toDouble() * 2.205
                            )).toString()
                        )
                    activityProfileBinding.tvWeight.text =
                        Editable.Factory.getInstance().newEditable(etDialogWeight.text.toString())
                    activityProfileBinding.tvWeightUnit.text = " LB"

                    var cm = Editable.Factory.getInstance().newEditable(
                        (Editable.Factory.getInstance()
                            .newEditable(DecimalFormat("#.#").format((etHeightCM.text.toString()).toDouble() / 30.48))
                            .toString())
                    )

                    activityProfileBinding.tvHieght.text =
                        cm.toString().substring(0, cm.toString().indexOf(".")).plus(" FT ")
                            .plus(
                                cm.toString()
                                    .substring(cm.toString().indexOf(".") + 1, cm.toString().length)
                                    .plus(" IN")
                            )

                } else if (tvDialogWeightUnit.text.toString()
                        .equals(" LB") && activityProfileBinding.tvWeightUnit.text.toString().equals(" KG")
                ) {
                    etDialogWeight.text = Editable.Factory.getInstance()
                        .newEditable(
                            DecimalFormat("#.#").format(
                                (etDialogWeight.text.toString().toDouble() / 2.205)
                            ).toString()
                        )
                    activityProfileBinding.tvWeight.text =
                        Editable.Factory.getInstance().newEditable(etDialogWeight.text.toString())
                    activityProfileBinding.tvWeightUnit.text = " KG"

                    activityProfileBinding.tvHieght.text = Editable.Factory.getInstance()
                        .newEditable(etHeightCM.text.toString().plus(" CM"))

                } else {
                    activityProfileBinding.tvHieght.text = etHeightCM.text.toString().plus(" CM")
                    activityProfileBinding.tvWeight.text = etDialogWeight.text.toString()

                }
            }
        })


    }

    private fun kgcmConvert() {


        if (etDialogWeight.text.toString().equals("") || etDialogWeight.text.toString()
                .equals(".")
        ) {
            Toast.makeText(applicationContext, "please enter correct values", Toast.LENGTH_LONG)
                .show()
        }else if(etDialogHeightFeet.text.toString().equals("")){
                Toast.makeText(applicationContext, "please enter correct height value", Toast.LENGTH_LONG)
                    .show()
        }  else if(etDialogHeightFeet.text.toString().equals("") && etDialogHeightInch.text.toString().equals("")){
                Toast.makeText(applicationContext, "please enter height value", Toast.LENGTH_LONG)
                    .show()
        } else {
            /*
            Convert lb to kg
            devide lb / 2.205
            */
            if (!tvLB.background.equals(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.gray_border_button,
                        null
                    )
                )
            ) {

                etDialogWeight.text =
                    Editable.Factory.getInstance()
                        .newEditable(
                            (Editable.Factory.getInstance().newEditable(
                                DecimalFormat("#.#").format(
                                    (etDialogWeight.text.toString())
                                        .toDouble() / 2.205
                                )
                            ).toString())
                        )
                tvDialogWeightUnit.text = " KG"
                tvKG.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
                tvKG.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                tvLB.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
                tvLB.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))
            }


            /*
            Convert IN to CM
            multiply IN * 30.48
            */
            if (!tvIN.background.equals(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.gray_border_button,
                        null
                    )
                )
            ) {

                llHeightCM.visibility = View.VISIBLE
                llHeight.visibility = View.GONE

                var value: String = Editable.Factory.getInstance()
                    .newEditable(etDialogHeightFeet.text.toString().trim()).toString()
                    .plus(".")
                    .plus(
                        Editable.Factory.getInstance()
                            .newEditable(etDialogHeightInch.text.toString()).trim()
                    ).toString()

                etHeightCM.text = Editable.Factory.getInstance()
                    .newEditable(
                        DecimalFormat("#.#").format((value.toDouble() * 30.48)).toString()
                    )

                tvCM.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
                tvCM.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                tvIN.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
                tvIN.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))
            }
        }
    }

    private fun lbAndInConvert() {
        if (etDialogWeight.text.toString().equals("") || etDialogWeight.text.toString()
                        .equals(".")
                ) {
                    Toast.makeText(applicationContext, "please enter correct weight value", Toast.LENGTH_LONG)
                        .show()
        }else if(etHeightCM.text.toString().equals("")
            || etHeightCM.text.toString().equals(".")
            || etHeightCM.text.toString().startsWith(".")
            || etHeightCM.text.toString().endsWith(".")){
            Toast.makeText(applicationContext, "please enter correct height value", Toast.LENGTH_LONG)
                .show()
        } else {
            /*
               Convert CM to IN
               multiply CM / 30.48
               */
            if (!tvCM.background.equals(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.gray_border_button,
                        null
                    )
                )
            ) {

                llHeightCM.visibility = View.GONE
                llHeight.visibility = View.VISIBLE

                var cm = DecimalFormat("#.#").format(etHeightCM.text.toString().toDouble() / 30.48)
                Log.d("ProfileActivity", "lbAndInConvert: "+cm)

                if (cm.toString().contains(".")) {
                    etDialogHeightFeet.text = Editable.Factory.getInstance()
                        .newEditable(cm.toString().substring(0, cm.toString().indexOf(".")))

                    etDialogHeightInch.text = Editable.Factory.getInstance()
                        .newEditable(
                            (cm.toString()
                                .substring(cm.toString().indexOf(".") + 1, cm.toString().length))
                        )
                }else{
                    etDialogHeightFeet.text = Editable.Factory.getInstance()
                        .newEditable(cm.toString().substring(0, cm.toString().length))

                    etDialogHeightInch.text = Editable.Factory.getInstance()
                        .newEditable(
                            ("0")
                        )
                }

                tvIN.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
                tvIN.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                tvCM.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
                tvCM.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))
            }

            /*
               Convert kg to lb
               multiply kg * 2.205
               */
            if (!tvKG.background.equals(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.gray_border_button,
                        null
                    )
                )
            ) {

                etDialogWeight.text =
                    Editable.Factory.getInstance()
                        .newEditable(
                            (Editable.Factory.getInstance().newEditable(
                                DecimalFormat("#.#").format(
                                    (etDialogWeight.text.toString())
                                        .toDouble() * 2.205
                                )
                            ).toString())
                        )
                tvDialogWeightUnit.text = " LB"

                tvLB.background = ResourcesCompat.getDrawable(resources, R.color.dark_pink, null)
                tvLB.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                tvKG.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.gray_border_button, null)
                tvKG.setTextColor(ResourcesCompat.getColor(resources, R.color.gray, null))
            }
        }
    }

    fun tvKGCMClick() {
        if (!activityProfileBinding.tvWeightUnit.text.toString()
                .equals(" KG") && !activityProfileBinding.tvLBFT.background.equals(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.pink_button_bg,
                    null
                )
            )
        ) {

            activityProfileBinding.tvWeight.text = (Math.round(
                (activityProfileBinding.tvWeight.text.toString().toDouble() / 2.205) * 100.0
            ) / 100.0).toString()
            activityProfileBinding.tvWeightUnit.text = " KG"

            var value: String

            if (activityProfileBinding.tvHieght.text.toString().contains("FT")) {
                value =
                    activityProfileBinding.tvHieght.text.toString().substring(0, activityProfileBinding.tvHieght.text.toString().indexOf(" FT "))
                        .trim().plus(".")
                        .plus(
                            activityProfileBinding.tvHieght.text.toString().substring(
                                activityProfileBinding.tvHieght.text.toString().indexOf(" FT ") + 4,
                                activityProfileBinding.tvHieght.text.toString().indexOf("IN")
                            )
                        ).trim()

                activityProfileBinding.tvHieght.text =
                    DecimalFormat("#.#").format(value.toDouble() * 30.48).toString().plus(" CM")

            } else {
                value =
                    (activityProfileBinding.tvHieght.text.toString().substring(0, activityProfileBinding.tvHieght.text.toString().indexOf(" CM"))
                        .toDouble() / 30.48).toString()

                activityProfileBinding.tvHieght.text = value.substring(0, value.indexOf(".")).plus(" FT ")
                    .plus(value.substring(value.indexOf(".") + 1, value.length).plus(" IN"))
            }



            activityProfileBinding.tvKGCM.background =
                ResourcesCompat.getDrawable(resources, R.drawable.pink_button_bg, null)
            activityProfileBinding.tvKGCM.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            activityProfileBinding.tvLBFT.background =
                ResourcesCompat.getDrawable(resources, R.drawable.pink_border_button, null)
            activityProfileBinding.tvLBFT.setTextColor(ResourcesCompat.getColor(resources, R.color.light_pink, null))
        }
    }

    fun tvLBFTClick() {
        if (!activityProfileBinding.tvWeightUnit.text.toString()
                .equals(" LB") && !activityProfileBinding.tvKGCM.background.equals(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.pink_button_bg,
                    null
                )
            )
        ) {

            activityProfileBinding.tvWeight.text = (Math.round(
                (activityProfileBinding.tvWeight.text.toString().toDouble() * 2.205) * 100.0
            ) / 100.0).toString()
            activityProfileBinding.tvWeightUnit.text = " LB"

            var value =
                DecimalFormat("#.#").format(
                    (activityProfileBinding.tvHieght.text.toString().substring(0, activityProfileBinding.tvHieght.text.toString().indexOf(" CM"))
                        .toDouble() / 30.48)
                ).toString()

            if (value.contains(".")){
                activityProfileBinding.tvHieght.text = value.substring(0, value.indexOf(".")).plus(" FT ")
                .plus(value.substring(value.indexOf(".") + 1, value.length)).plus(" IN")

            }else{
                activityProfileBinding.tvHieght.text = value.substring(0, value.length).plus(" FT ")
                .plus("0").plus(" IN")

            }

            activityProfileBinding.tvLBFT.background =
                ResourcesCompat.getDrawable(resources, R.drawable.pink_button_bg, null)
            activityProfileBinding.tvLBFT.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            activityProfileBinding.tvKGCM.background =
                ResourcesCompat.getDrawable(resources, R.drawable.pink_border_button, null)
            activityProfileBinding.tvKGCM.setTextColor(ResourcesCompat.getColor(resources, R.color.light_pink, null))
        }
    }

    fun onNextClick() {

        var height = activityProfileBinding.tvHieght.text.toString()
        var weight = activityProfileBinding.tvWeight.text.toString()
        var birthDate = activityProfileBinding.tvYearOfBirth.text.toString()
        var heightType: String
        if (height.contains("FT")) {
            heightType = "lbft"
        } else {
            heightType = "kgcm"
        }
        if (type.equals("update")) {
            mySharedPreferences.setProfileData("0", height, weight, birthDate, heightType)
            Toast.makeText(applicationContext, "Update Successfully", Toast.LENGTH_SHORT).show()
            finish()

        } else {
            insertProfileData(height, weight, "", heightType)
        }
    }

    private fun insertProfileData(height: String, weight: String, birthDate: String, type: String) {
        lifecycleScope.launch {
            val data = ProfileTable(0, height, weight, birthDate, type)
            this.coroutineContext.let {
                db.profileDao().insertAll(data)
                mySharedPreferences.setProfileData("0", height, weight, birthDate, type)
                Toast.makeText(applicationContext, "Insert Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ProfileActivity, BottomNavigationActivity::class.java)
                startActivity(intent)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    }



    fun onBackButtonClick() {
        finish()
    }

}