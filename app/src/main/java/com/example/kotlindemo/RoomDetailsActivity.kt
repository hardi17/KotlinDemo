package com.example.kotlindemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlindemo.databinding.ActivityRoomDetailsBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RoomDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomDetailsBinding

    val client = OkHttpClient()
    var key: String = String()

    var featurelist: ArrayList<Model> = ArrayList()
    var serviceList: ArrayList<Model> = ArrayList()
    var facilititesList: ArrayList<Model> = ArrayList()
    var eaquipmentList: ArrayList<Model> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_details)

        getIntentData()

        run("https://ssgmobile.github.io/api/room/detail/" + key + ".json")

    }

    private fun getIntentData() {
        key = intent.getStringExtra("key").toString()
    }

    private fun run(url: String) {
        binding.pgbRoomDetail.visibility = View.VISIBLE
        binding.pgbEuipment.visibility = View.VISIBLE
        binding.pgbFeature.visibility = View.VISIBLE
        binding.pgbFacilities.visibility = View.VISIBLE
        binding.pgbService.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.run {

            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    binding.pgbRoomDetail.visibility = View.GONE
                }

                override fun onResponse(call: Call, response: Response) {
                    var str_response = response.body()!!.string()
                    //creating json object
                    // val json_contact: JSONObject = JSONObject(str_response)
                    //creating json array
                    //  var jsonarray_info = JSONArray(str_response)
                    //    val jsonObject: JSONObject = jsonarray_info.getJSONObject(0)
                    val jsonObject: JSONObject = JSONObject(str_response)
                    val name = jsonObject.getString("name")
                    val capacity = jsonObject.getString("capacity")
                    val heroImageUrl = jsonObject.getString("heroImageUrl")

                    //getlocation
                    val jsonLocObj: JSONObject = jsonObject.getJSONObject("location")
                    val location = " " +jsonLocObj.getJSONObject("region").getString("name") +
                            ", " + jsonLocObj.getJSONObject("site").getString("name") +
                            ", " + jsonLocObj.getJSONObject("building").getString("name") +
                            ", " + jsonLocObj.getJSONObject("floor").getString("name") + "."

                    var i: Int = 0

                    //services
                    var jsonarray_info_service = jsonObject.getJSONArray("services")
                    var size_service: Int = jsonarray_info_service.length()
                    for (i in 0..size_service - 1) {
                        var json_objectdetail: JSONObject = jsonarray_info_service.getJSONObject(i)
                        val model = Model()
                        model.name = json_objectdetail.getString("name")
                        model.iconUrl = json_objectdetail.getString("iconUrl")
                        serviceList.add(model)
                    }


                    //features
                    var jsonarray_info = jsonObject.getJSONArray("features")
                    var size: Int = jsonarray_info.length()
                    for (i in 0..size - 1) {
                        var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                        val model = Model()
                        model.name = json_objectdetail.getString("name")
                        featurelist.add(model)
                    }

                    //facilities
                    var jsonarray_info1 = jsonObject.getJSONArray("facilities")
                    var size_facilities: Int = jsonarray_info1.length()
                    for (i in 0..size_facilities - 1) {
                        var json_objectdetail: JSONObject = jsonarray_info1.getJSONObject(i)
                        val model = Model()
                        model.name = json_objectdetail.getString("name")
                        model.iconUrl = json_objectdetail.getString("iconUrl")
                        facilititesList.add(model)
                    }

                    //facilities
                    var jsonarray_info2 = jsonObject.getJSONArray("equipment")
                    var size_equipment: Int = jsonarray_info2.length()
                    for (i in 0..size_equipment - 1) {
                        var json_objectdetail: JSONObject = jsonarray_info2.getJSONObject(i)
                        val model = Model()
                        model.name = json_objectdetail.getString("name")
                        model.iconUrl = json_objectdetail.getString("iconUrl")
                        eaquipmentList.add(model)
                    }

                    runOnUiThread {
                        binding.pgbRoomDetail.visibility = View.GONE
                        binding.tvNameMain.text = name.toString()
                        binding.tvCapacity.text = capacity.toString()
                        Picasso.get().load(heroImageUrl.toString()).into(binding.ivHeroImage)
                        binding.tvLocation.text = location.toString()

                        var obj_adapter: RoomListAdapter
                        if(!featurelist.isNullOrEmpty()) {
                            val layoutManager = LinearLayoutManager(this@RoomDetailsActivity)
                            obj_adapter =
                                RoomListAdapter(this@RoomDetailsActivity, featurelist, false, true)
                            binding.featuresRcv.layoutManager = layoutManager
                            binding.featuresRcv.adapter = obj_adapter
                            binding.pgbFeature.visibility = View.GONE
                            binding.tvFeatureNoData.visibility = View.GONE
                        }else{
                            binding.pgbFeature.visibility = View.GONE
                            binding.featuresRcv.visibility = View.GONE
                            binding.tvFeatureNoData.visibility = View.VISIBLE
                        }

                        if(!serviceList.isNullOrEmpty()) {
                            val layoutManagers = LinearLayoutManager(this@RoomDetailsActivity)
                            obj_adapter =
                                RoomListAdapter(this@RoomDetailsActivity, serviceList, true, true)
                            binding.serviceRcv.layoutManager = layoutManagers
                            binding.serviceRcv.adapter = obj_adapter
                            binding.pgbService.visibility = View.GONE
                            binding.tvServiceNoData.visibility = View.GONE
                        }else{
                            binding.pgbService.visibility = View.GONE
                            binding.serviceRcv.visibility = View.GONE
                            binding.tvServiceNoData.visibility = View.VISIBLE
                        }

                        if(!facilititesList.isNullOrEmpty()) {
                            val layoutManagerf = LinearLayoutManager(this@RoomDetailsActivity)
                            obj_adapter = RoomListAdapter(
                                this@RoomDetailsActivity,
                                facilititesList,
                                true,
                                true
                            )
                            binding.facilitiesRcv.layoutManager = layoutManagerf
                            binding.facilitiesRcv.adapter = obj_adapter
                            binding.pgbFacilities.visibility = View.GONE
                            binding.tvFacilitiesNoData.visibility = View.GONE
                        }else{
                            binding.facilitiesRcv.visibility = View.GONE
                            binding.pgbFacilities.visibility = View.GONE
                            binding.tvFacilitiesNoData.visibility = View.VISIBLE
                        }

                        if(!eaquipmentList.isNullOrEmpty()) {
                            val layoutManagere = LinearLayoutManager(this@RoomDetailsActivity)
                            obj_adapter = RoomListAdapter(
                                this@RoomDetailsActivity,
                                eaquipmentList,
                                true,
                                true
                            )
                            binding.euipmentRcv.layoutManager = layoutManagere
                            binding.euipmentRcv.adapter = obj_adapter
                            binding.pgbEuipment.visibility = View.GONE
                        }else{
                            binding.euipmentRcv.visibility = View.GONE
                            binding.pgbEuipment.visibility = View.GONE
                            binding.tvEquiNoData.visibility = View.VISIBLE
                        }
                    }

                }
            })
        }
    }
}