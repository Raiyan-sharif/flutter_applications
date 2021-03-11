package com.acibd.serviceautonomous.networking.all_data

data class Data(
    val call_types: ArrayList<CallType>,
    val perticipants: ArrayList<Perticipant>,
    val products: ArrayList<Product>,
    val districts: ArrayList<District>,
    val brands: ArrayList<Brand>,
    val brand_models: ArrayList<Model>,
    val service_types: ArrayList<ServiceType>,
    val user_info: UserInfo
)