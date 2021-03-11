package com.work.onlineleave.data

import com.google.gson.GsonBuilder
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.govt_holiday.GovtHoliday
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.leavetype.LeaveType
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_approval.MyApproval
import com.work.onlineleave.data.my_approval.approve.CommonResponse
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.data.my_leave.delete.MyLeaveDelete
import com.work.onlineleave.data.new_leave.NewLeaveResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.data.new_leave_days.Leavedays
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


object ApiClient {

    private val API_BASE_URL = "http://dashboard.acigroup.info/emisaci/api/"

    private var servicesApiInterface: ServiceApiInterface? = null

    @Synchronized
    fun build(): ServiceApiInterface? {

        if (servicesApiInterface != null)
            return servicesApiInterface

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()
        servicesApiInterface = retrofit.create(
            ServiceApiInterface::class.java
        )

        return servicesApiInterface as ServiceApiInterface
    }


    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    interface ServiceApiInterface {
        @Headers("Content-Type: application/json")
        @GET("authenticate/login")
        fun login(
            @Query("empcode") empcode: String,
            @Query("password") password: String
        ): Call<LoginResponse>


        // new leave

        @Headers("Content-Type: application/json")
        @GET("newleave/applyto")
        fun applyto(
            @Query("empcode") empcode: String
        ): Call<ApplyTo>


        @Headers("Content-Type: application/json")
        @GET("newleave/edit")
        fun getNewLeaveEditInfo(
            @Query("leaverefno") leaverefno: String
        ): Call<NewLeaveEditInfo>


        @Headers("Content-Type: application/json")
        @GET("newleave/leavetype")
        fun leavetype(): Call<LeaveType>

        @Headers("Content-Type: application/json")
        @GET("newleave/leavedays")
        fun leavedays(
            @Query("leaveappfrom") leaveappfrom: String,
            @Query("leaveappto") leaveappto: String,
            @Query("leavefor") leavefor: String
        ): Call<Leavedays>


        @Headers("Content-Type: application/json")
        @GET("leave/create")
        fun newLeaveCreate(
            @Query("leaveappfrom") leaveappfrom: String,
            @Query("leaveappto") leaveappto: String,
            @Query("empcode") empcode: String,
            @Query("empname") empname: String,
            @Query("deptname") deptname: String,
            @Query("leaveappdate") leaveappdate: String,
            @Query("desgname") desgname: String,
            @Query("leaverefno") leaverefno: String,
            @Query("emailid") emailid: String,
            @Query("reason") reason: String,
            @Query("authcode") authcode: String,
            @Query("leavefor") leavefor: String,
            @Query("leaveappdays") leaveappdays: String,
            @Query("deptcode") deptcode: String,
            @Query("worklocation") worklocation: String,
            @Query("appworklocation") appworklocation: String,
            @Query("lfa") lfa: String
        ): Call<NewLeaveResponse>

        @Headers("Content-Type: application/json")
        @GET("leave/update")
        fun newLeaveUpdate(
            @Query("leaveappfrom") leaveappfrom: String,
            @Query("leaveappto") leaveappto: String,
            @Query("empcode") empcode: String,
            @Query("empname") empname: String,
            @Query("deptname") deptname: String,
            @Query("leaveappdate") leaveappdate: String,
            @Query("desgname") desgname: String,
            @Query("leaverefno") leaverefno: String,
            @Query("emailid") emailid: String,
            @Query("reason") reason: String,
            @Query("authcode") authcode: String,
            @Query("leavefor") leavefor: String,
            @Query("leaveappdays") leaveappdays: String,
            @Query("deptcode") deptcode: String,
            @Query("worklocation") worklocation: String,
            @Query("appworklocation") appworklocation: String,
            @Query("lfa") lfa: String
        ): Call<NewLeaveResponse>


        // end new leave

        // my leave start

        @Headers("Content-Type: application/json")
        @GET("myleave/read")
        fun getMyLeaveList(
            @Query("empcode") empcode: String,
            @Query("start") start: String,
            @Query("limit") limit: String

        ): Call<MyLeaveList>

        @Headers("Content-Type: application/json")
        @GET("myleave/delete")
        fun myLeaveDelete(
            @Query("leaverefno") leaverefno: String

        ): Call<MyLeaveDelete>


        // my leave end


        // dashboard start

        @Headers("Content-Type: application/json")
        @GET("leave/statusdata")
        fun leaveStatus(
            @Query("empcode") empcode: String
        ): Call<LeaveStatus>


        // dashboard end


        // govt holiday start

        @Headers("Content-Type: application/json")
        @GET("myleave/holidays")
        fun getGovtHolidayList(
            @Query("worklocation") worklocation: String
        ): Call<GovtHoliday>

        // govt holiday end


        // my approval start

        @Headers("Content-Type: application/json")
        @GET("myleave/approval")
        fun getMyApprovalList(
            @Query("empcode") empcode: String,
            @Query("start") start: String,
            @Query("limit") limit: String

        ): Call<MyApproval>





//        dashboard.acigroup.info/emisaci/api/leave/approve?leaveapprovefrom=2017-8-22&leaveapproveto=2017-8-22&empcode=05581&empname=Md. Hasibul Hasan Khan&deptname=M I S&leaveappdate=21/08/2017&desgname=Senior System Analyst&leaverefno=109985&emailid=hasib@aci-bd.com&reason=test&authname=Md. Hasibul Hasan Khan&leavedetails=PRIVILEGE LEAVE&leaveappfrom=22/08/2017&leaveappto=22/08/2017&leaveappdays=1&approvefrom=22/08/2017&approveto=22/08/2017&leavegrantdays=1&reasontorefuse=&authcode=05581

        @Headers("Content-Type: application/json")
        @GET("leave/approve")
        fun newApprovalApprove(
            @Query("leaveapprovefrom") leaveapprovefrom: String,
            @Query("leaveapproveto") leaveapproveto: String,
            @Query("empcode") empcode: String,
            @Query("empname") empname: String,
            @Query("deptname") deptname: String,
            @Query("leaveappdate") leaveappdate: String,
            @Query("desgname") desgname: String,
            @Query("leaverefno") leaverefno: String,
            @Query("emailid") emailid: String,
            @Query("reason") reason: String,
            @Query("authname") authname: String,
            @Query("leavedetails") leavedetails: String,
            @Query("leaveappfrom") leaveappfrom: String,
            @Query("leaveappto") leaveappto: String,
            @Query("leaveappdays") leaveappdays: String,
            @Query("approvefrom") approvefrom: String,
            @Query("approveto") approveto: String,
            @Query("leavegrantdays") leavegrantdays: String,
            @Query("reasontorefuse") reasontorefuse: String,
            @Query("authcode") authcode: String
        ): Call<CommonResponse>

        @Headers("Content-Type: application/json")
        @GET("leave/refuse")
        fun newApprovalRefuse(
            @Query("leaveapprovefrom") leaveapprovefrom: String,
            @Query("leaveapproveto") leaveapproveto: String,
            @Query("empcode") empcode: String,
            @Query("empname") empname: String,
            @Query("deptname") deptname: String,
            @Query("leaveappdate") leaveappdate: String,
            @Query("desgname") desgname: String,
            @Query("leaverefno") leaverefno: String,
            @Query("emailid") emailid: String,
            @Query("reason") reason: String,
            @Query("authname") authname: String,
            @Query("leavedetails") leavedetails: String,
            @Query("leaveappfrom") leaveappfrom: String,
            @Query("leaveappto") leaveappto: String,
            @Query("leaveappdays") leaveappdays: String,
            @Query("approvefrom") approvefrom: String,
            @Query("approveto") approveto: String,
            @Query("leavegrantdays") leavegrantdays: String,
            @Query("reasontorefuse") reasontorefuse: String,
            @Query("authcode") authcode: String
        ): Call<CommonResponse>


        @Headers("Content-Type: application/json")
        @GET("leave/cancel")
        fun getMyApprovalCancelRequest(
            @Query("leaverefno") leaverefno: String
        ): Call<CommonResponse>

        // my Approval end


    }
}