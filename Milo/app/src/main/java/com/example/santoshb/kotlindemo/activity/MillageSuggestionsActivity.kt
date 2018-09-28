package com.example.santoshb.kotlindemo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.adapter.SectionRecyclerViewAdapter
import com.example.santoshb.kotlindemo.model.SectionModel
import kotlinx.android.synthetic.main.activity_millage_suggesstions.*

class MileageSuggestionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_millage_suggesstions)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sectionedRecyclerView.layoutManager = LinearLayoutManager(this)
        sectionedRecyclerView.setHasFixedSize(true)
        populateRecyclerView()
    }

    //populate recycler view
    private fun populateRecyclerView() {
        val sectionModelArrayList = ArrayList<SectionModel>()
        /*//for loop for sections
        for (i in 1..5) {
            val itemArrayList = ArrayList<String>()
            //for loop for items
            for (j in 1..10) {
                itemArrayList.add("Item $j")
            }

            //add the section and items to array list
            sectionModelArrayList.add(SectionModel("Section $i", itemArrayList))
        }*/

        sectionModelArrayList.add(SectionModel("Bike", getListOfBikeSuggestions()))
        sectionModelArrayList.add(SectionModel("Car", getListOfCarDieselSuggestions()))
        val adapter = SectionRecyclerViewAdapter(this, sectionModelArrayList)
        sectionedRecyclerView.adapter = adapter
    }

    private fun getListOfBikeSuggestions(): ArrayList<String> {
        val listOfStrings = ArrayList<String>()
        listOfStrings.add("Getting your bike serviced on time brings the chances of loss to the machine at the minimum. A healthy engine would return a better mileage than that with non-timely servicing. Also prefer using the same grade oil which is preferred by the company at the time of service.")
        listOfStrings.add("Even after timely servicing, carburetor settings can be adjusted for a better mileage. Re-tuning the carburetor helps a lot when you are getting bad mileage.")
        listOfStrings.add("Tyre pressure plays an important role when it comes to mileage. Lesser air pressure means more friction, thereby returning less mileage, so make sure to keep the tyre pressure as recommended by the company.")
        listOfStrings.add("Filling good quality fuel will help the bike return a better figure as compared to the contaminated fuel. It even helps in keeping the engine healthy. Good quality petrol will always keep the engine in good condition thereby increasing the fuel economy of the bike.")
        listOfStrings.add("Sudden acceleration and hard braking also wastes a lot of fuel. Keep a light hand on the throttle and avoid accelerating the bike at high rpm level in short gear. Riding the bike in proper gear at low rpm level helps in returning better mileage figures.")
        listOfStrings.add("Avoid roads with high traffic as driving at constant slow speeds in short gear also return bad mileage figures.")
        listOfStrings.add("While on a traffic light, always switch off the engine as idling is one of the basic factors where the odometer stands constant and fuel gets wasted. In case you get stuck in a traffic jam and the waiting time is above 40 seconds, then don’t forget to switch off the engine.")
        listOfStrings.add("Parking your motorcycle in sunlight will also let a small amount of fuel evaporate. Although the amount is small, parking it for 9 hours daily and 30 days a month can have an impact.")
        listOfStrings.add("Even between the service intervals, the chain of the bike needs cleaning and lubrication. If you live in area with more dirt and sand, doing it often can help you in getting better mileage as the engine would need less power to rotate the chain.")
        listOfStrings.add("Altering the original components of the bike results in lower fuel mileage. Avoiding custom exhausts, air filters and extra wide tyres can help you get a better mileage.")
        return listOfStrings
    }

    private fun getListOfCarDieselSuggestions(): ArrayList<String> {
        val listOfStrings = ArrayList<String>()
        listOfStrings.add("Maintain the correct tyre pressure.")
        listOfStrings.add("Reduce weight in the car, by keeping unnecessary junk out of it")
        listOfStrings.add("Get the car regularly serviced, keeping air filter clean")
        listOfStrings.add("Gently accelerate, gently slow down")
        listOfStrings.add("Shift gears at the correct rpm")
        listOfStrings.add("Drive at the optimum speed, not exceeding about 60-70 kmph depending on the car")
        listOfStrings.add("Switch off the engine if you are going to be idle for more than a minute")
        listOfStrings.add("Reduce AC use if possible")
        listOfStrings.add("Don't drive your car with dirty or clogged filters, such as the air filter or the oil filter if you don't want to shell out at the pump. Get them cleaned or replace them")
        listOfStrings.add("Keep the boot empty and clean and watch the fuel gauge become more stable. The extra bucks will make up for the trouble.")
        listOfStrings.add("Shift to the highest possible gear without the engine knocking.")
        listOfStrings.add("Make sure the tyres are inflated at the manufacturer recommended levels and you'll be fine. And don't forget to check them once a week, always in the morning.")

        return listOfStrings
    }
}