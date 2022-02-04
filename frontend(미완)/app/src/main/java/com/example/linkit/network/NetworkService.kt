package com.example.linkit.network

import retrofit2.Response
import retrofit2.http.*


interface SimpleApi {
    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY")
    @POST("/link/insert")
    suspend fun insert(
        @Body insert: Insert,
    ): Response<ResInsert>

    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY",
            "Content-Type: application/json")
    @POST("/link/read")
    suspend fun get(
        @Body snode: Snode,
    ): Response<ResGet>

    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY",
        "Content-Type: application/json")
    @POST("/link/read")
    suspend fun get(
        @Body email:Email
    ):Response<ResGet>

    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY",
        "Content-Type: application/json")
    @POST("/link/delete")
    suspend fun delete(
        @Body snode: Snodes,
    ): Response<ResInsert>

    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY",
        "Content-Type: application/json")
    @POST("/user/login/google")
    suspend fun gLogin(
        @Body googleToken: SNSToken
    ): Response<BaseResponse<UserData>>

    @Headers("Authorization:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRhaWx5MTQyODU3QGdtYWlsLmNvbSIsInBhaWQiOmZhbHNlLCJleHAiOjE2Njg0MTY4MzB9.cEx1_fcIV-HtLqp47EzgsYW7O53DdwSPS_HxkrdiVOY",
        "Content-Type: application/json")
    @POST("/group/add")
    suspend fun add(
        @Body add:Add
    )
}