package com.akash.smsreader

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SmsListAdapter(private val context: Context, val resourceId: Int, private val smsList: ArrayList<SMS>): BaseAdapter() {

    private val inflater: LayoutInflater
    var totalCredit = 0.0
    var totalDebit = 0.0
    init {
        for (sms in smsList){
            if(sms.type)
                totalDebit += sms.amount!!
            else
                totalCredit += sms.amount!!
        }
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(resourceId, parent, false)
        val smsObj = smsList[position]
        if (smsList!=null){
            val accName = view.findViewById<TextView>(R.id.accName)
            val amount = view.findViewById<TextView>(R.id.amount)
            val time = view.findViewById<TextView>(R.id.time)
            val tags = view.findViewById<TextView>(R.id.tags)
            accName.text = "Account: " + smsObj?.accName
            amount.text = "Amount: " + smsObj?.amount.toString()
            time.text = "Time: " + smsObj?.time.toString()
            tags.text = "Tags: "
            for(tag in smsObj!!.tags)
                tags.append(" #$tag ")
        }
        return view
    }

    override fun getItem(position: Int): SMS? {
        return smsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return smsList.size
    }
}