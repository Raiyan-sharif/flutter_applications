package com.aci.constructionequipment.networking.service_request_list

data class Data(
    val brand_id: String,
    val brand_model_id: String,
    val chesis_no: String,
    val customer_feedback: String,
    val customer_id: String,
    val customer_mobile_no: String,
    val customer_name: String,
    val customer_remarks: String,
    val district_id: String,
    val engineer_id: String,
    val engineer_remarks: String,
    val id: Int,
    val is_six_hour: String,
    val is_solved: String,
    val service_date: String,
    val service_time: String,
    val service_type_id: String,
    val solved_at: String,
    val technitian_id: String,
    val technitian_remarks: String
)