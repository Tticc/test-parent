package com.tester.testerrpc.gpt.api;

import com.tester.testerrpc.gpt.entity.billing.CreditGrantsResponse;
import com.tester.testerrpc.gpt.entity.billing.SubscriptionData;
import com.tester.testerrpc.gpt.entity.billing.UseageResponse;
import com.tester.testerrpc.gpt.entity.chat.ChatCompletion;
import com.tester.testerrpc.gpt.entity.chat.ChatCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 *
 */
public interface Api {

    String DEFAULT_API_HOST = "https://api.openai.com/";


    /**
     * chat
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);


    /**
     * 余额查询
     */
    @GET("dashboard/billing/credit_grants")
    Single<CreditGrantsResponse> creditGrants();

    /**
     * 余额查询
     */
    @GET("v1/dashboard/billing/subscription")
    Single<SubscriptionData> subscription();

    /**
     * 余额查询
     */
    @GET("v1/dashboard/billing/usage")
    Single<UseageResponse> usage(@Query("start_date") String startDate,
                                 @Query("end_date") String endDate);


}
