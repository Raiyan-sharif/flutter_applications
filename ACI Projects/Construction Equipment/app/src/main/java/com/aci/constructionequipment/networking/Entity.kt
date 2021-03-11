package com.aci.constructionequipment.networking


data class LoginPostBody(val mobile: String, val password: String)
data class LoginTechPostBody(val username: String, val password: String)

data class LoginResponse(val token: String)


data class ApkVersion(
    val id: Int,
    val name: String,
    val version: String,
    val version_code: Int,
    val link: String,
    val download_page: String
)

data class CustomerRequestCreateBody(
    val brand_id: Int,
    val brand_model_id: Int,
    val customer_remarks: String,
    val district_id: Int,
    val customer_mobile_no: String,
    val customer_name: String
)

data class CustomerRequestResponse(val data: CustomerRequestResponseData)
data class CustomerRequestResponseData(val id: Int)


data class UserInfo(val data: UserInfoData)
data class UserInfoData(
    val id: Int,
    val name: String,
    val role: UserInfoDataRole
)

data class UserInfoDataRole(
    val id: Int,
    val name: String,
    val code: String,
)

data class EnginnerUpdate(
    val id: Int,
    val customer_name: String,
    val customer_mobile_no: String,
    val customer_id: Int,
    val brand_id: Int,
    val brand_model_id: Int,
    val district_id: Int,
    val customer_remarks: String?,
    val technitian_id: Int,
    val engineer_remarks: String?
)

//{
//    "id":2,
//    "customer_name":"Test CUstomer",
//    "customer_mobile_no":"01704114542",
//    "brand_id":1,
//    "brand_model_id":1,
//    "district_id":1,
//    "customer_id":3,
//    "customer_remarks":"Customer Remarks",
//    "chesis_no" :"Chesis No",
//    "service_type_id" :1,
//    "technitian_remarks" :"Technician Remark",
//    "customer_feedback" : 5
//}

data class TechnicianUpdate(
    val id: Int,
    val customer_name: String,
    val customer_mobile_no: String,
    val customer_id: Int,
    val brand_id: Int,
    val brand_model_id: Int,
    val district_id: Int,
    val customer_remarks: String?,
    val chesis_no: String,
    val technitian_remarks: String?,
    val customer_feedback: Int,
    val service_type_id: Int
)

data class DeviceToken(val firebase_token: String)

data class CommonResponse(val message: String)
