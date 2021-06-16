package com.example.plantquiz.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadingObject {
    //Parsing JSON
@Throws(IOException::class)
    //following fun returns string
fun downloadJSONDataFromLink(link: String): String{
        val stringBuilder: StringBuilder =StringBuilder()
        val url = URL(link)
        val urlConnection =url.openConnection() as HttpURLConnection //create connection
        try{

            val BufferedInputString: BufferedInputStream =
                  BufferedInputStream( urlConnection.inputStream)  //to get from url in the byte form
            val bufferedReader: BufferedReader=
                BufferedReader(InputStreamReader(BufferedInputString))
            //****inputstreamReader bridge from byte stream to character stream
            //to read data from input string we can also use fileReader
          //Temporary string to hold each line read from the Bufferedreader
            var inputLineString: String?
             inputLineString =bufferedReader.readLine()

            while(inputLineString!= null){
                stringBuilder.append((inputLineString))
                inputLineString =bufferedReader.readLine()
            }
        }
        finally{
            //regardless of success or failure of Try Block, we will disconnect from the URLConnection
              urlConnection.disconnect()
        }

        return stringBuilder.toString()
      }

fun downloadPlantPicture(pictureName: String?): Bitmap? {
      var bitmap: Bitmap? =null
      val pictureLink = PLANTPLACES_COM + "/photos/$pictureName"
      val pictureURL = URL(pictureLink)
        //to open the picture url and get the inputStream
        val inputStream = pictureURL.openConnection().getInputStream()
        if(inputStream !=null){
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
        return bitmap
    }
    //companion object can it has access to the private methods and properties
    //of our downloading object class
companion object {
        val PLANTPLACES_COM ="https://www.plantplaces.com"

    }


}