package com.example.plantquiz.Controller

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.plantquiz.Model.DownloadingObject
import com.example.plantquiz.Model.ParsePlantUtility
import com.example.plantquiz.Model.Plant
import com.example.plantquiz.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {    //backward compatibility for older version

   //view components
    private var cameraButton: Button? =null
    private var photoGalleryButton: Button? =null
    private var imageTaken: ImageView? = null
//   private var button1: Button? =null
//    private var button2: Button? =null
//    private var button3: Button? =null
//    private var button4: Button? =null
    val OPEN_CAMERA_BUTTON_REQUEST_ID = 1000 //every button must have unique request code
    val OPEN_GALLERY_BUTTON_REQUEST_ID = 2000

    //Instance Variables
    var correctAnswerIndex : Int =0
    var correctPlant: Plant? =null
    var NumberOfTimesUserAnswerCorrectly :Int =0
    var NumberOfTimesUserAnswerIncorrectly: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //to display activity R is generated file
        //it requires to show activity_main.xml

        setSupportActionBar(toolbar)

        //progressBar.setVisibility(View.GONE)
        setProgressBar(false)
        displayUIWidgets(false)

        YoYo.with(Techniques.Pulse)
            .duration(700)
            .repeat(5)
            .playOn(btnNextPlant)


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


            cameraButton = findViewById<Button>(R.id.btnOpenCamera)
            photoGalleryButton = findViewById(R.id.btnOpenGallery)
            imageTaken = findViewById<ImageView>(R.id.imgTaken)
            //as already declare type so no need of again assigning <Button>

            //below is kotlin lambda expression it help to make anonymous function
            //without specifying the specification

        cameraButton?.setOnClickListener(View.OnClickListener {

            Toast.makeText(this,"the Camera Button is clicked",
                Toast.LENGTH_SHORT).show()
         //implicit intent-> what we want to do ,we don't care how it is done
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, OPEN_CAMERA_BUTTON_REQUEST_ID) //2nd param is requestCode
        })

        photoGalleryButton?.setOnClickListener(View.OnClickListener {
           Toast.makeText(this,"the Gallery Button is clicked",
               Toast.LENGTH_SHORT).show()
           val galleryIntent =Intent(Intent.ACTION_PICK,
               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
           startActivityForResult(galleryIntent,OPEN_GALLERY_BUTTON_REQUEST_ID)
       })
        //See The Next Plant
        btnNextPlant.setOnClickListener(View.OnClickListener {
            if(checkForInternetConnection()) {
                //to put progress bar until the image is dowloaded in onPostExec of imgTask
                setProgressBar(true)
              try {
                  val innerClassObject = DownloadingPlantTask()
                  innerClassObject.execute()
              } catch(e: Exception){
                  e.printStackTrace()
              }
//                button1.setBackgroundColor((Color.LTGRAY))
//                button2.setBackgroundColor((Color.LTGRAY))
//                button3.setBackgroundColor((Color.LTGRAY))
//                button4.setBackgroundColor((Color.LTGRAY))
                //starting color and ending color as same as in Border_Button.xml
                //in order to make Gradient Colors (Mix Colors)

                var gradientColors: IntArray = IntArray(2)
                gradientColors.set(0 , Color.parseColor("#FFFF66"))
                gradientColors.set(1 , Color.parseColor("#FF0008"))

                var gradientDrawable: GradientDrawable=
                    GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        gradientColors)
                var convertDipValue=dipToFloat(this@MainActivity,50f)
                gradientDrawable.setCornerRadius(convertDipValue)
                gradientDrawable.setStroke(5,Color.parseColor("#FBF8F8"))
                    button1.setBackground(gradientDrawable)
                    button2.setBackground(gradientDrawable)
                    button3.setBackground(gradientDrawable)
                    button4.setBackground(gradientDrawable)

            }
        })

    }

    fun dipToFloat(context: Context, dipValue: Float): Float {
        val metrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    //when we want something back from intent result we us onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
       if(requestCode==OPEN_CAMERA_BUTTON_REQUEST_ID){

           if(resultCode== Activity.RESULT_OK){

               //to get the picture ??????? Bitmap are used to represent image
               val imageData =data?.getExtras()?.get("data") as Bitmap
               imageTaken?.setImageBitmap(imageData)
           }
       }
        if(requestCode==OPEN_GALLERY_BUTTON_REQUEST_ID){
            if(resultCode==Activity.RESULT_OK){
                val contentURI =data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,
                contentURI)
                imageTaken?.setImageBitmap(bitmap)

            }
        }


    }

    fun Button1IsClicked(buttonView: View)
    {
//        Toast.makeText(this,"the Button1 is clicked",
//            Toast.LENGTH_SHORT).show()
//        var myNumber =20 //Implied Data Type
//        var myName: String ="Raj"
//        var numberOfLetters =myName.length
//        var animalName: String? = null
//        //var numberofCharacter =animalName!!.length ,this will give error
//        //as we give assurance by putting !! that animanlName will not be an invalid value
//        //hence app will crash in place of this we put animalName?.length
//        var NumberOfCharactersOFAnimalName =animalName?.length ?: 100  //elvis operator
//        //if animalName value is null then it will print 100
        specifyTheRightAndWrongAnswer(0)

    }

    fun Button2IsClicked(buttonView: View)
    {
//        Toast.makeText(this,"the Button2 is clicked",
//            Toast.LENGTH_SHORT).show()
//
        specifyTheRightAndWrongAnswer(1)
    }
    fun Button3IsClicked(buttonView: View)
    {
//        Toast.makeText(this,"the Button2 is clicked",
//            Toast.LENGTH_SHORT).show()
//
        specifyTheRightAndWrongAnswer(2)
    }
    fun Button4IsClicked(buttonView: View)
    {
//        Toast.makeText(this,"the Button2 is clicked",
//            Toast.LENGTH_SHORT).show()
//
        specifyTheRightAndWrongAnswer(3)
    }


    fun ImageViewIsClicked(view: View){
    val randomNumber: Int =(Math.random() * 6).toInt() + 1
        Log.i("TAG","THE RANDOM NUMBER IS:$randomNumber")

        when(randomNumber){
            1->btnOpenCamera.setBackgroundColor(Color.YELLOW)
            2->button4.setBackgroundColor(Color.BLUE)
            3->btnOpenGallery.setBackgroundColor(Color.RED)
            4->button1.setBackgroundColor(Color.CYAN)
            5->button2.setBackgroundColor(Color.MAGENTA)
            6-> button3.setBackgroundColor(Color.GREEN)
        }
//        if(randomNumber == 1){
//            btnOpenCamera.setBackgroundColor(Color.YELLOW)
//        }
//        else if(randomNumber == 2){
//            button4.setBackgroundColor(Color.BLUE)
//        }
//        else if(randomNumber == 3){
//            //as we directly refer to id
//            btnOpenGallery.setBackgroundColor(Color.RED)
//        }
//        else if(randomNumber == 4){
//            //we have to use ? because they are instance
//            button1.setBackgroundColor(Color.CYAN)
//        }
//        else if(randomNumber == 5){
//            button2.setBackgroundColor(Color.MAGENTA)
//        }
//        else if(randomNumber == 6){
//            button3.setBackgroundColor(Color.GREEN)
//        }

    }
     //AsyncTask class is an abstract class --doing the heavy processes in background


    inner class DownloadingPlantTask: AsyncTask<String, Int, List<Plant> >(){

         override fun doInBackground(vararg params: String?): List<Plant>? {
            //vararg tells that it is kind of an array
           // doInBackground can access background thread not user interface thread
//             val downloadingObject : DownloadingObject =
//                 DownloadingObject()
//             var jsonData= downloadingObject.downloadJSONDataFromLink("https://cat-fact.herokuapp.com/facts")
//             Log.i("JSON",jsonData)
             val parsePlant =ParsePlantUtility()
             //by calling this method we can return list of plant objects
             return parsePlant.parsePlantObjectsFromJSONData()


         }
         //when doInBackground executes completely the returned value will pass
         //to onPostExecute method automatically

         override fun onPostExecute(result: List<Plant>?) {
             super.onPostExecute(result)
             //onPostExecute can access user interface thread
             var numberOfPlants =result?.size ?: 0
            //to generate random plant object for four buttos
             if(numberOfPlants > 0 ) {
                 //result.size contains the number of json object or plant object
                 //that we get from the internet
                 var randomPlantIndexForButton1: Int =(Math.random() * result!!.size).toInt()
                 var randomPlantIndexForButton2: Int =(Math.random() * result!!.size).toInt()
                 var randomPlantIndexForButton3: Int =(Math.random() * result!!.size).toInt()
                 var randomPlantIndexForButton4: Int =(Math.random() * result!!.size).toInt()

                 var allRadomPlants = ArrayList<Plant>()
                 //we are adding object to this arraylist
                 //result.get(index) will get the object at this index
                 allRadomPlants.add(result.get(randomPlantIndexForButton1))
                 allRadomPlants.add(result.get(randomPlantIndexForButton2))
                 allRadomPlants.add(result.get(randomPlantIndexForButton3))
                 allRadomPlants.add(result.get(randomPlantIndexForButton4))

                 button1.text =result.get(randomPlantIndexForButton1).toString()
                 button2.text =result.get(randomPlantIndexForButton1).toString()
                 button3.text =result.get(randomPlantIndexForButton1).toString()
                 button4.text =result.get(randomPlantIndexForButton1).toString()

                 //correctAnswerIndex hold index of correct answer
                 correctAnswerIndex =(Math.random()*allRadomPlants.size).toInt()
                 correctPlant = allRadomPlants.get(correctAnswerIndex)

                 val downloadingImageTask =DownloadingImageTask()
                 //to initialize the doInBackGround and pass the argument of pictureName of correctIndex
                 downloadingImageTask.execute(allRadomPlants.get(correctAnswerIndex).pictureName)
                                        
             }

         }

    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"the onsTart method is called",
            Toast.LENGTH_SHORT).show()
        //checkForInternetConnection()
    }

    //check for internet connection
    @Suppress("DEPRECATION")
    private fun checkForInternetConnection(): Boolean {
        //this refers to the main activity
        var result = false
        val cm: ConnectivityManager? = this.
                       getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {

                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        if(result) {
           return true
        }
        else {
            createAlert()
            return false
        }
    }
    private  fun createAlert(){
        val alertDialog: AlertDialog =
                    AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("Network Error")

        alertDialog.setMessage("Please check for internet connection")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{
                dialog: DialogInterface?, which: Int ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        })

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel"
        ) { dialog: DialogInterface?, which: Int ->
           Toast.makeText(this@MainActivity,"You Must be connected to the intenet",
               Toast.LENGTH_SHORT).show()
            finish()
        }
        alertDialog.show()
    }

    //Specify the right or wrong answer
    private fun specifyTheRightAndWrongAnswer(userGuess: Int) {
        when(correctAnswerIndex) {
            0 ->button1.setBackgroundColor(Color.CYAN)
            1 ->button2.setBackgroundColor(Color.CYAN)
            2 ->button3.setBackgroundColor(Color.CYAN)
            3 ->button4.setBackgroundColor(Color.CYAN)
        }
        if( userGuess == correctAnswerIndex) {
            txtState.setText("Right")
            NumberOfTimesUserAnswerCorrectly++
            txtRightAnswer.setText("$NumberOfTimesUserAnswerCorrectly")
        }
        else {
            var correctPlantName = correctPlant.toString()
            txtState.setText("Wrong. Choose : $correctPlantName")
            NumberOfTimesUserAnswerIncorrectly++
            txtWrongAnswer.setText("$NumberOfTimesUserAnswerIncorrectly")
        }
    }

    // Downloading Image Process
    inner class DownloadingImageTask : AsyncTask<String ,Int , Bitmap?>() {
        override fun doInBackground(vararg pictureName: String?): Bitmap? {
           //if there is any internet connection problem
            try {
                val downloadingObject = DownloadingObject()
                val plantBitmap: Bitmap? = downloadingObject.downloadPlantPicture(pictureName[0])
                return plantBitmap

            } catch (e: Exception) {
                e.printStackTrace()
            }
            //if try block not run and catch run
            return null
        }
        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            setProgressBar(false)
            displayUIWidgets(true)
            playAnimationOnView(imgTaken,Techniques.Tada)
            playAnimationOnView(button1,Techniques.RollIn)
            playAnimationOnView(button2,Techniques.RollIn)
            playAnimationOnView(button3,Techniques.RollIn)
            playAnimationOnView(button4,Techniques.RollIn)
            playAnimationOnView(txtState,Techniques.Swing)
            playAnimationOnView(txtWrongAnswer,Techniques.FlipInX)
            playAnimationOnView(txtRightAnswer,Techniques.Landing)

            imgTaken.setImageBitmap(result)
        }
    }

    //ProgressBar Visibilty
    private fun setProgressBar(show: Boolean){
        if(show){
            linearLayoutProgress.setVisibility(View.VISIBLE) //refers to gray screen
            progressBar.setVisibility(View.VISIBLE)  //to show ProgressBar
            //to prevent the user to interect with user interface component
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else if (!show){
            linearLayoutProgress.setVisibility(View.GONE)
            progressBar.setVisibility(View.GONE) //to hide progressBar
            //to allow user to interect with user interface component
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }
    //Set Visibility Of UI Widgets Part 34 Beautify The app
    private fun displayUIWidgets(display: Boolean){
        if(display){
            imgTaken.setVisibility(View.VISIBLE)
            button1.setVisibility(View.VISIBLE)
            button2.setVisibility(View.VISIBLE)
            button3.setVisibility(View.VISIBLE)
            button4.setVisibility(View.VISIBLE)
            txtState.setVisibility(View.VISIBLE)
            txtRightAnswer.setVisibility(View.VISIBLE)
            txtWrongAnswer.setVisibility(View.VISIBLE)

        }
        else if(!display){
            imgTaken.setVisibility(View.INVISIBLE)
            button1.setVisibility(View.INVISIBLE)
            button2.setVisibility(View.INVISIBLE)
            button3.setVisibility(View.INVISIBLE)
            button4.setVisibility(View.INVISIBLE)
            txtState.setVisibility(View.INVISIBLE)
            txtRightAnswer.setVisibility(View.INVISIBLE)
            txtWrongAnswer.setVisibility(View.INVISIBLE)

        }

    }
    //Playing Animations
    private fun playAnimationOnView(view: View?,technique: Techniques){
        YoYo.with(technique)
            .duration(700)
            .repeat(0)
            .playOn(view)
    }

}
