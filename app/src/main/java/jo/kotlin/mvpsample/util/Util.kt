package jo.kotlin.mvpsample.util

/**
 * Created by Jo on 2018. 9. 14.
 */
fun Int.ApiException():Exception {

    val errorMsg = when(this){
        1 -> "1번 오류"
        2 -> "2번 오류"
        3 -> "검색어 누락"
        else -> {
            "알수없는 에러"
        }
    }
    return Exception(errorMsg)
}


