package com.example.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlindemo.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var list: ArrayList<Model> = ArrayList()

    val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        run("https://ssgmobile.github.io/api/room/rooms.json")

    }

    fun run(url: String) {
        binding.pgb.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.run {

            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    binding.pgb.visibility = View.GONE
                }

                override fun onResponse(call: Call, response: Response) {
                    var str_response = response.body()!!.string()
                    //creating json object
                    // val json_contact: JSONObject = JSONObject(str_response)
                    //creating json array
                    var jsonarray_info = JSONArray(str_response)
                    var i: Int = 0
                    var size: Int = jsonarray_info.length()
                    list = ArrayList();
                    for (i in 0..size - 1) {
                        var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                        var model: Model = Model();
                        model.key = json_objectdetail.getString("key")
                        model.name = json_objectdetail.getString("name")
                        model.thumbnailUrl = json_objectdetail.getString("thumbnailUrl")
                        model.capacity = json_objectdetail.getString("capacity")
                        list.add(model)
                    }

                    runOnUiThread {
                        //stuff that updates ui
                        val obj_adapter: RoomListAdapter
                        obj_adapter = RoomListAdapter(this@MainActivity, list, true, false)
                        val layoutManager: LinearLayoutManager =
                            LinearLayoutManager(this@MainActivity)
                        binding.rcvRoomDetails.layoutManager = layoutManager
                        binding.rcvRoomDetails.adapter = obj_adapter
                        binding.pgb.visibility = View.GONE
                    }

                }
            })
        }
    }

}