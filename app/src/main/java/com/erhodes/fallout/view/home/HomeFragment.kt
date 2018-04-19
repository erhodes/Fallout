package com.erhodes.fallout.view.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.erhodes.fallout.R
import com.erhodes.fallout.jsontesting.*
import com.erhodes.fallout.model.Attribute
import com.erhodes.fallout.model.Character
import com.erhodes.fallout.view.attributes.AttributesViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    var listView: ListView? = null
    var adapter : ArrayAdapter<Character>? = null
    var viewModel : HomeViewModel? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel?.getCharacters()?.observe(this, Observer { characters:List<Character>? ->
                update(characters)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = inflater?.inflate(R.layout.fragment_home, null)

        adapter = ArrayAdapter(activity, R.layout.list_character)
        listView = view?.findViewById<ListView>(R.id.characterList)
        listView?.adapter = adapter

        val button = view?.findViewById<Button>(R.id.reloadButton)
        button?.setOnClickListener({
            viewModel?.resetDatabase()
        })

        val loadButton = view?.findViewById<Button>(R.id.loadButton)
        loadButton?.setOnClickListener({
            test();
        })
        return view;
    }

    fun test() {
        val attributes = HashMap<String, Animal>();
        attributes.put("animal", Animal("default"))
        attributes.put("cat", Cat("cat", "calico"))
        attributes.put("dog", Dog("dog", "huskie"))

        val collectionType = object : TypeToken<HashMap<String, Animal>>() {}.type
        val gsonBuilder = GsonBuilder()
        val adapter = RuntimeTypeAdapterFactory.of(Animal::class.java, "type")
        adapter.registerSubtype(Animal::class.java)
        adapter.registerSubtype(Cat::class.java)
        adapter.registerSubtype(Dog::class.java)
//        gsonBuilder.registerTypeAdapterFactory(adapter)
        gsonBuilder.registerTypeAdapter(collectionType, AnimalDeserializer())

        val gson = gsonBuilder.create()

        val serialized = gson.toJson(attributes)
        Log.d("Eric",serialized)


        var deserialized = HashMap<String, Animal>()
        deserialized = gson.fromJson(serialized, collectionType)

        for (animal: Animal in deserialized.values) {
            Log.d("Eric",animal.name + " " + animal::class)
        }
        Log.d("Eric",gson.toJson(deserialized))
    }

    fun update(characters : List<Character>? ) {
        adapter?.clear()
        adapter?.addAll(characters)
        adapter?.notifyDataSetChanged()
        listView?.invalidate()
    }
}