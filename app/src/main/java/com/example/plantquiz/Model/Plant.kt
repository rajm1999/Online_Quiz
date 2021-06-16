package com.example.plantquiz.Model

import android.util.Log

class Plant(var genus: String,var species: String,var cultivar: String,var common: String,
var pictureName: String,var description: String,var difficulty: Int,var id: Int= 0){

constructor() : this("","","","","",
    "",0,0)

    private var _plantName: String? =null  //convention is put _ before getter

    var plantName: String?

         get() = _plantName
         set(value){

            _plantName = value

                }
    //tostring actually gives the string representation of specific object
    //if we do not change this we get adress of object at button's text
    override fun toString(): String {
       Log.i("PLANT","$common - $species")
        //as by default it returns the address of string value as shown in cmnt below
       // return super.toString()
        return "$common $species"
    }

}