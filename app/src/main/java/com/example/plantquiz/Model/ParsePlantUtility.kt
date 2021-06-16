package com.example.plantquiz.Model

import org.json.JSONArray
import org.json.JSONObject

class ParsePlantUtility {
    fun parsePlantObjectsFromJSONData(): List<Plant>? {
        var allPlantObjects: ArrayList<Plant> = ArrayList() //as this fun returns List
        var downloadingObject = DownloadingObject() //make a object of class DownloadingObject
        var topLevelPlantJSONData =downloadingObject.
               downloadJSONDataFromLink("https://plantplaces.com/perl/mobile/flashcard.pl") //call this fun which is in the class
        var topLevelJSONObject: JSONObject =JSONObject(topLevelPlantJSONData)
        var plantObjectsArray: JSONArray = topLevelJSONObject.getJSONArray("values") //to call Jsonarray which is in topleveljson object
        var index:Int =0
        while(index<plantObjectsArray.length()) {

            var plantObject: Plant =Plant()
            var jsonObject =plantObjectsArray.getJSONObject(index)
           /*
           (genys: String,species: String, cultivar: String, common: String,
           pictureName: String, description: String, difficulty: Int,id: Int= 0
            */
            with(jsonObject){

                plantObject.genus =getString("genus")
                plantObject.species=getString("species")
                plantObject.cultivar=getString("cultivar")
                plantObject.common=getString("common")
                plantObject.pictureName=getString("picture_name")
                plantObject.description=getString("description")
                plantObject.difficulty=getInt("difficulty")
                plantObject.id=getInt("id")
            }
            allPlantObjects.add(plantObject)
            index++


        }
        return allPlantObjects
    }
}