package com.erhodes.fallout.database

import android.arch.persistence.room.TypeConverter
import android.util.Log
import com.erhodes.fallout.jsontesting.RuntimeTypeAdapterFactory
import com.erhodes.fallout.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Converters {
    // a helper function
    fun getGsonBuilderWithAdapter() : Gson {
        val typeAdapterFactory = RuntimeTypeAdapterFactory.of(Attribute::class.java)
                .registerSubtype(Attribute::class.java)
                .registerSubtype(DerivedAttribute::class.java)
                .registerSubtype(CapacityAttribute::class.java)
                .registerSubtype(Skill::class.java)
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory)
        return gsonBuilder.create()
    }

    // attributes
    @TypeConverter
    fun attributesFromString(json:String) : HashMap<String, Attribute> {
        Log.d("Eric","loading " + json)
        val collectionType = object : TypeToken<HashMap<String, Attribute>>() {}.type
        return getGsonBuilderWithAdapter().fromJson(json, collectionType)
    }
    @TypeConverter
    fun toAttributeString(attributes : HashMap<String, Attribute>) : String {
        val gson = getGsonBuilderWithAdapter()
        Log.d("Eric"," saving as " + gson.toJson(attributes));
        return gson.toJson(attributes)
    }
    @TypeConverter
    fun regularAttributesFromString(json: String) : ArrayList<Attribute> {
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<Attribute>>() {}.type
        return gson.fromJson(json, collectionType)
    }
    @TypeConverter
    fun toRegularAttributesString(attributes : ArrayList<Attribute>) : String {
        val gson = Gson()
        return gson.toJson(attributes)
    }
    @TypeConverter
    fun derivedAttributesFromString(json: String) : ArrayList<DerivedAttribute> {
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<DerivedAttribute>>() {}.type
        return gson.fromJson(json, collectionType)
    }
    @TypeConverter
    fun toDerivedAttributesString(attributes : ArrayList<DerivedAttribute>) : String {
        val gson = Gson()
        return gson.toJson(attributes)
    }
    @TypeConverter
    fun capacityAttributesFromString(json: String) : ArrayList<CapacityAttribute> {
        Log.d("Eric", "loading " + json);
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<CapacityAttribute>>() {}.type
        return gson.fromJson(json, collectionType)
    }
    @TypeConverter
    fun toCapacityAttributesString(attributes : ArrayList<CapacityAttribute>) : String {
        val builder = GsonBuilder()
//        builder.registerTypeAdapter(CapacityAttribute::class.java, AttributeTypeAdapter())
        val gson = builder.create()
        Log.d("Eric","savving " + gson.toJson(attributes));
        return gson.toJson(attributes)
    }

    // perks
    @TypeConverter
    fun perksFromString(json : String) : HashSet<String> {
        val gson = Gson()
        val collectionType = object : TypeToken<HashSet<String>>() {}.type
        return gson.fromJson(json, collectionType)
    }
    @TypeConverter
    fun toPerkString(perks : HashSet<String>) : String {
        val gson = Gson()
        return gson.toJson(perks)
    }
    // quick items
    @TypeConverter
    fun fromQuickItemString(json : String) : ArrayList<Item> {
//        val gson = Gson()
//        val collectionType = object : TypeToken<ArrayList<Item>>() {}.type
//        return gson.fromJson(json, collectionType)
        return ArrayList()
    }
    @TypeConverter
    fun toQuickItemsString(items : ArrayList<Item>) : String {
//        val gson = Gson()
//        return gson.toJson(items)
        return "?"
    }
    // inventory
    @TypeConverter
    fun fromInventoryString(json : String) : HashMap<String, Item> {
//        val gson = Gson()
//        val collectionType = object : TypeToken<HashMap<String, Item>>() {}.type
//        return gson.fromJson(json, collectionType)
        return HashMap()
    }
    @TypeConverter
    fun toInventoryString(items : HashMap<String, Item>) : String {
//        val gson = Gson()
//        return gson.toJson(items)
        return "?"
    }
    // actions
    @TypeConverter
    fun fromActionsString(json : String) : ArrayList<Action> {
//        val gson = Gson()
//        val collectionType = object : TypeToken<ArrayList<Action>>() {}.type
//        return gson.fromJson(json, collectionType)
        return ArrayList()
    }
    @TypeConverter
    fun toActionsString(actions : ArrayList<Action>) : String {
//        val gson = Gson()
//        return gson.toJson(actions)
        return ">"
    }
    //Effects
    @TypeConverter
    fun fromEffectString(json : String) : ArrayList<Effect> {
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<Effect>>() {}.type
        return gson.fromJson(json, collectionType)
    }
    @TypeConverter
    fun toEffectString(effects : ArrayList<Effect>) : String {
        val gson = Gson()
        return gson.toJson(effects)
    }
    //Weapon
    @TypeConverter
    fun fromWeaponString(json : String) : Weapon {
//        val gson = Gson()
//        return gson.fromJson(json, Weapon::class.java)
        return Weapon("fake", "fake", "fake", 1, 1)
    }
    @TypeConverter
    fun toWeaponString(weapon : Weapon) : String {
//        val gson = Gson()
//        return gson.toJson(weapon)
        return ">"
    }
    //Armour
    @TypeConverter
    fun fromArmourString(json : String) : Item {
//        val gson = Gson()
//        return gson.fromJson(json, Weapon::class.java)
        return Item()
    }
    @TypeConverter
    fun toArmourString(armour : Item) : String {
//        val gson = Gson()
//        return gson.toJson(armour)
        return ">"
    }
}