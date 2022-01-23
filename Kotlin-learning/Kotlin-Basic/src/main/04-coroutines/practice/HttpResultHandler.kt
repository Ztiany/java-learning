package practice

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private val gson = Gson()

private interface AccountApi {

    @POST("user/v1/client/login")
    suspend fun login(@Body loginRequest: LoginRequest): HttpResult<LoginResponse>

    @POST("user/v1/client/login")
    fun loginRx(@Body loginRequest: LoginRequest): Observable<HttpResult<LoginResponse>>

    @POST("user/v1/client/login")
    suspend fun loginNullable(@Body loginRequest: LoginRequest): HttpResult<LoginResponse?>

}

suspend fun main() {
    //testAPICall()
    //testNullableAPICall()
    testRxJavaCall()
}

fun testRxJavaCall() {
    serverAPI.loginRx(request).subscribe({
        println("testRxJavaCall-success: $it")
    }, {
        println("testRxJavaCall-error: $it")
    })
}

private suspend fun testAPICall() {
    try {
        val loginResponse = callAPI(buildRealCall())
        println("testAPICall-success: $loginResponse")
    } catch (e: Exception) {
        println("testAPICall-error: $e")
    }
}

private suspend fun testNullableAPICall() {
    try {
        val loginResponse = callAPINullable(buildRealCallNullable())
        println("testNullableAPICall-success: $loginResponse")
    } catch (e: Exception) {
        println("testNullableAPICall-error: $e")
    }
}

///////////////////////////////////////////////////////////////////////////
// 网络调用封装
///////////////////////////////////////////////////////////////////////////
private suspend fun <T> callAPINullable(
    call: suspend () -> Result<T>
): T? {

    val result: Result<T>

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        throw transformHttpException(throwable)
    }

    println("callAPI result = $result")
    return if (isErrorDataStub(result)) {//服务器数据格式错误
        throw ServerErrorException(ServerErrorException.SERVER_DATA_ERROR)
    } else if (!result.isSuccess) { //检测响应码是否正确
        onApiError(result)
        throw createApiException(result)
    } else {
        result.data
    }
}

private suspend fun <T : Any> callAPI(
    call: suspend () -> Result<T>
): T {

    val result: Result<T>

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        throw transformHttpException(throwable)
    }

    println("callAPI result = $result")
    return if (isErrorDataStub(result)) {//服务器数据格式错误
        throw ServerErrorException(ServerErrorException.SERVER_DATA_ERROR)
    } else if (!result.isSuccess) { //检测响应码是否正确
        onApiError(result)
        throw createApiException(result)
    } else {
        result.data ?: throw ServerErrorException(ServerErrorException.NO_DATA_ERROR)
    }
}

private fun <T> createApiException(
    result: Result<T>
): Throwable {
    return APIErrorException(result.code, result.message)
}

private fun <T> onApiError(result: Result<T>) {
    println("onApiError")
}

private fun transformHttpException(throwable: Throwable): Throwable {
    println("transformHttpException $throwable")
    return if (throwable is HttpException && !(throwable.code() >= 500)) {
        val errorBody = throwable.response()?.errorBody()
        if (errorBody == null) {
            newNetworkErrorException()
        } else {
            parseErrorBody(errorBody.string()) ?: newNetworkErrorException()
        }
    } else {
        newNetworkErrorException()
    }
}

private fun parseErrorBody(string: String): APIErrorException? {
    println("parseErrorBody: $string")
    return try {
        val errorResult = gson.fromJson(string, ErrorResult::class.java)
        APIErrorException(errorResult.status, errorResult.msg)
    } catch (e: Exception) {
        null
    }
}

private fun newNetworkErrorException() = if (isConnected()) {
    //有连接无数据，服务器错误
    ServerErrorException(ServerErrorException.UNKNOW_ERROR)
} else {
    //无连接网络错误
    NetworkErrorException()
}

private fun isConnected(): Boolean {
    return true
}

private class APIErrorException(
    private val code: Int, message: String
) : Exception(message) {

    override fun toString(): String {
        return "APIErrorException(code=$code, message=${message})"
    }

}

private class ServerErrorException(private val serverDataError: Int) : Exception("服务器错误") {
    companion object {
        const val SERVER_DATA_ERROR = 1
        const val UNKNOW_ERROR = 2
        const val NO_DATA_ERROR = 3
    }

    override fun toString(): String {
        return "ServerErrorException(code=$serverDataError, message=${message})"
    }

}

private class NetworkErrorException : Exception() {
    override fun toString(): String {
        return "NetworkErrorException)"
    }
}

private fun <T> isErrorDataStub(result: Result<T>): Boolean {
    return false
}

///////////////////////////////////////////////////////////////////////////
// 网络
///////////////////////////////////////////////////////////////////////////
private val request = gson.fromJson(
    "{\"client\":\"1\",\"diskName\":\"Mi 10\",\"imei\":\"2371FC1256A3C83EC681D85AA831B75FA053C49E\",\"ipAddr\":\"10.5.12.162\",\"mac\":\"\",\"password\":\"Kl2NKoLUjKLn04irlJfdQQ==\",\"phone\":\"19999999999\",\"uuid\":\"2371FC1256A3C83EC681D85AA831B75FA053C49E\"}",
    LoginRequest::class.java
)

private fun buildRealCall(): suspend () -> Result<LoginResponse> {
    val networkCall: suspend () -> Result<LoginResponse> = {
        serverAPI.login(request)
    }
    return networkCall
}

private fun buildRealCallNullable(): suspend () -> Result<LoginResponse?> {
    val networkCall: suspend () -> Result<LoginResponse?> = {
        serverAPI.loginNullable(request)
    }
    return networkCall
}

//假数据
//private const val FAKE_BODY = "{\"status\":0,\"msg\":\"消息\"}"
//private const val FAKE_BODY = "{\"status\":0,\"msg\":\"消息\",\"data\":null}"
private const val FAKE_BODY = "{\"status\":0,\"msg\":\"消息\",\"data\":[]}"

private val serverAPI by lazy {

    val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            println(message)
        }
    })
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val retrofit = retrofit2.Retrofit.Builder().client(
        OkHttpClient.Builder()
            //日志
            .addInterceptor(httpLoggingInterceptor)
            //协议
            .addInterceptor(Interceptor {
                val request = it.request()
                val newBuilder = request.newBuilder()
                with(newBuilder) {
                    header("os", "android")
                    header("versionName", "2.0.1")
                    header("versionCode", "201")
                    header("devicesId", "2371FC1256A3C83EC681D85AA831B75FA053C49E")
                    header("brand", "XiaoMi")
                    //header("Authorization", "")
                }
                it.proceed(newBuilder.build())
            })
            //模拟
            .addInterceptor {
                val response = it.proceed(it.request())
                response.newBuilder().code(200).message("OK").body(FAKE_BODY.toResponseBody()).build()
            }.build()
    ).baseUrl("http://demo.ysj.vclusters.com/api/")
        //json
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ErrorJsonLenientConverterFactory(GsonConverterFactory.create(gson)))
        //rx call
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()

    retrofit.create(
        AccountApi::class.java
    )
}

private data class LoginRequest(
    val phone: String,
    val password: String,
    val diskName: String,
    val imei: String,
    val uuid: String,
    val mac: String,
    val ipAddr: String,
    val client: String = "1",
)

data class LoginResponse(
    val id: Long = -1,
    val username: String = "",
    val token: String = "",
    val nextCloudIP: String = "",
    val nextLocalNetworkIP: String = "",
)

///////////////////////////////////////////////////////////////////////////
// 结果接收
///////////////////////////////////////////////////////////////////////////

private interface Result<T> {
    val data: T
    val code: Int
    val message: String
    val isSuccess: Boolean
}

/**
 * 1. 没有提供默认构造器，则 GSON 使用 Unsafe 创建对象。
 * 2. T 表示隐式的 Any?，所以生成的 Java 代码在 getData 时不会执行  null 检测，而是在创建具体的 HttpResult
 *      对象时根据实际类型参数来约束参数的是否可 null，然而通过反射却可以逃避构造 HttpResult 时的 null 约束检测。
 */
data class HttpResult<T>(
    @SerializedName("data") override val data: T,
    @SerializedName("status") override val code: Int,
    @SerializedName("msg") override val message: String
) : Result<T> {

    init {
        println("HttpResult Constructor Called.")
    }

    override val isSuccess: Boolean
        get() = code == 0
}

data class ErrorResult(
    val status: Int = 0,
    val msg: String = "",
)

///////////////////////////////////////////////////////////////////////////
// 测试泛型声明
///////////////////////////////////////////////////////////////////////////

private fun testHttpResult(httpResult: HttpResult<String>) {
    val data: String = "null"
    processHttpResult(HttpResult(data, 1, ""))
}

private fun processHttpResult(httpResult: HttpResult<String>) {
    val data = httpResult.data
}

///////////////////////////////////////////////////////////////////////////
// 反射与构造器调用
///////////////////////////////////////////////////////////////////////////

/**提供了默认构造器，则 GSON 用默认构造器来创建对象。*/
private data class HttpResult2<T>(
    @SerializedName("data") var data: T? = null,
    @SerializedName("status") var code: Int = 1,
    @SerializedName("msg") var message: String = ""
) {
    init {
        println("HttpResult2 Constructor Called.")
    }
}

private fun testReflecting() {
    gson.fromJson(FAKE_BODY, HttpResult2::class.java)
}

