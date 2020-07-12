package com.akash.smsreader

import me.everything.providers.android.telephony.Sms

class SMS(private var sms: Sms, val type:Boolean) {

    var amount:Double? = null
    var accName:String? = null
    var tags = ArrayList<String> ()
    var time:Long? = null

    fun analyseMsg(){
        amount = findAmount(sms.body)
        accName = findAcc(sms.body)
        time = sms.receivedDate
        if(type)
            setTags("debit")
        else
            setTags("credit")
    }

    private fun findAmount(data: String):Double{
        val regex = Regex("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)")
        val amount = regex.find(data)?.value
        return if (amount!=null){
            "\\d+(\\.\\d+)?".toRegex().find(amount)!!.value.toDouble()
        } else
            0.0
    }

    private fun findAcc(data: String): String?{
        val regex = Regex("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
        return regex.find(data)?.value
    }

    fun setTags(vararg strings: String){
        for (string in strings)
            tags.add(string)
    }
}