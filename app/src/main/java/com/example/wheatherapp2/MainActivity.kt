package com.example.wheatherapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.wheatherapp2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchwheatherdata("Nawalgarh")
        searchcity()


    }

    private fun searchcity() {
        val SearchView =binding.searchview
        SearchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchwheatherdata(query)

                }
                return  true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchwheatherdata(cityname:String) {

        val  retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(myinterface::class.java)

        val response = retrofit.getwheatherdata("Nawalgarh","e094544642b1beb59bb9cb37318a1051","metric")
        response.enqueue(object:Callback<wheatherapp>{
            override fun onResponse(call: Call<wheatherapp>, response: Response<wheatherapp>) {
                  val responsebody = response.body()
//

                if (response.isSuccessful&&responsebody !=null) {

                    val temperture = responsebody.main.temp.toString()
//
                 //   Log.d("TAG", "onresponse:$temperture")

                    binding.temp.text ="$temperture °C"

                    val humidity = responsebody.main.humidity
                   val windspeed = responsebody.wind.speed
                   val sunrise  = responsebody.sys.sunrise.toLong()
                 val sunset = responsebody.sys.sunset.toLong()
                 val sealevel = responsebody.main.pressure
                   val condition  = responsebody.weather.firstOrNull()?.main?:"unknown"

                   val maxtemp = responsebody.main.temp_max
                   val mintemp = responsebody.main.temp_min

                    binding.textView5.text ="Min Temp:$maxtemp °C"
                    binding.textView6.text ="Max Temp:$mintemp °C"
                    binding.sea.text = "$sealevel hpa"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.condition.text = condition
                    binding.wheather.text = condition
                    binding.textView7.text = dayname(System.currentTimeMillis())
                    binding.textView9.text =date()
                    binding.sunrise.text = "${ time(sunrise)}"
                    binding.sunset.text = "${ time(sunset)}"
                    binding.textView.text = "$cityname"
                    changeimagesaccordingtoweatherdata(condition)
                }
                }

                      override fun onFailure(call: Call<wheatherapp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

}

  private fun changeimagesaccordingtoweatherdata(condtions:String) {
    when(condtions){
       "clearsky","sunny","clear"->{ binding.root.setBackgroundResource(R.drawable.sunny_background)
        binding.lottieAnimationView.setAnimation(R.raw.sun)

  }
      "partlycloud","cloud","overcast","mist","foggy"->{ binding.root.setBackgroundResource(R.drawable.colud_background)
          binding.lottieAnimationView.setAnimation(R.raw.cloud)
       }
       "light rain","drizzel","moderate rain","showers","heavyrain"->{
          binding.root.setBackgroundResource(R.drawable.rain_background)
        binding.lottieAnimationView.setAnimation(R.raw.rain)
       }
       "lightsnow","moderate snow","heavy snow","blizzard"->{
           binding.root.setBackgroundResource(R.drawable.snow_background)
           binding.lottieAnimationView.setAnimation(R.raw.snow)
       }

        else->{
  binding.root.setBackgroundResource(R.drawable.sunny_background)
  binding.lottieAnimationView.setAnimation(R.raw.sun) }
    }
        binding.lottieAnimationView.playAnimation()
   }




    fun dayname (timestamp:Long):String{
          val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
          return sdf.format(Date())
  }


    fun date ():String{
        val sdf = SimpleDateFormat("dd /MMMM/ yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    fun time(timestamp: Long):String{
      val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()
     )
      return sdf.format(Date(timestamp*1000))
   }

}