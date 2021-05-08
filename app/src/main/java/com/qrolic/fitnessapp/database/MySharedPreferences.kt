package com.qrolic.fitnessapp.database

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class MySharedPreferences (context: Context) {

    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences(
                PREFERENCE_NAME,
                PRIVATE_MODE)
        editor = preferences.edit()
    }

    fun isFirstRun() = preferences.getBoolean(FIRST_TIME, true)

    fun setFirstRun() {
        editor.putBoolean(FIRST_TIME, false).commit()
        editor.commit()
    }

    fun setProfileData(id:String,height:String,weight:String,birthDate:String,type:String){
        editor.putBoolean("is_user_login", true)
        editor.putString("id",id)
        editor.putString("height",height)
        editor.putString("weight",weight)
        editor.putString("birthDate",birthDate)
        editor.putString("type",type)
        editor.commit()
    }

    fun setWeekData(monday:Boolean,tuesday:Boolean,wednesday:Boolean,thursday:Boolean,friday:Boolean,saturday:Boolean,sunday:Boolean){
        editor.putBoolean("monday", monday)
        editor.putBoolean("tuesday", tuesday)
        editor.putBoolean("wednesday", wednesday)
        editor.putBoolean("thursday", thursday)
        editor.putBoolean("friday", friday)
        editor.putBoolean("saturday", saturday)
        editor.putBoolean("sunday", sunday)
        editor.commit()
    }

    fun getMonday():Boolean{
        return preferences.getBoolean("monday",true)
    }

    fun getTuesday():Boolean{
        return preferences.getBoolean("tuesday",true)
    }

    fun getWednesday():Boolean{
        return preferences.getBoolean("wednesday",true)
    }

    fun getThursday():Boolean{
        return preferences.getBoolean("thursday",true)
    }

    fun getFriday():Boolean{
        return preferences.getBoolean("friday",true)
    }

    fun getSaturday():Boolean{
        return preferences.getBoolean("saturday",true)
    }

    fun getSunday():Boolean{
        return preferences.getBoolean("sunday",true)
    }

    fun getId():String?{
        return preferences.getString("id","")
    }

    fun getHeight():String?{
        return preferences.getString("height","")
    }

    fun getWeight():String?{
        return preferences.getString("weight","")
    }

    fun getBirthDate():String?{
        return preferences.getString("birthDate","")
    }

    fun getType():String?{
        return preferences.getString("type","")
    }

    fun setType(){
        editor.putString("type","").commit()
        editor.commit()
    }

    fun getRestSetTime():String?{
        return preferences.getString("restSet","30")
    }

    fun setRestSetTime(restTime :String){
        editor.putString("restSet", restTime).commit()
        editor.commit()
    }

    fun getCountDownTime():String?{
        return preferences.getString("countDown","15")
    }

    fun setCountDownTime(restTime :String){
        editor.putString("countDown", restTime).commit()
        editor.commit()
    }

    fun getMute():Boolean?{
        return preferences.getBoolean("mute",false)
    }

    fun setMute(mute :Boolean){
        editor.putBoolean("mute", mute).commit()
        editor.commit()
    }

    fun getScreenOn():Boolean?{
        return preferences.getBoolean("screen_on",true)
    }

    fun setScreenOn(screenOn :Boolean){
        editor.putBoolean("screen_on", screenOn).commit()
        editor.commit()
    }

    fun getReminderTime():String?{
        return preferences.getString("reminder_time","no reminder set")
    }

    fun setReminderTime(reminderTime :String){
        editor.putString("reminder_time",reminderTime ).commit()
        editor.commit()
    }

    fun getLocaleLanguage():String?{
        return preferences.getString("local_lang","UK")
    }

    fun setLocalLanguage(localLang :String){
        editor.putString("local_lang",localLang ).commit()
        editor.commit()
    }

    fun getReminderSwitchEnable():Boolean?{
        return preferences.getBoolean("reminderSwicth",false)
    }

    fun setReminderSwitchEnabled(reminderSwicth :Boolean){
        editor.putBoolean("reminderSwicth",reminderSwicth ).commit()
        editor.commit()
    }

    fun getPauseEnable():Boolean?{
        return preferences.getBoolean("pauseEnable",false)
    }

    fun setPauseEnable(pauseEnable :Boolean){
        editor.putBoolean("pauseEnable",pauseEnable ).commit()
        editor.commit()
    }

    fun getHour():String?{
        return preferences.getString("hour","")
    }

    fun setHour(hour :String){
        editor.putString("hour",hour ).commit()
        editor.commit()
    }

    fun getMinute():String?{
        return preferences.getString("minute","")
    }

    fun setMinute(minute :String){
        editor.putString("minute",minute ).commit()
        editor.commit()
    }

    fun getUserLogin(): Boolean {
        return preferences.getBoolean("is_user_login", false)
    }
    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREFERENCE_NAME = "fitness_app"
        private const val FIRST_TIME = "isFirstRun"
    }
}