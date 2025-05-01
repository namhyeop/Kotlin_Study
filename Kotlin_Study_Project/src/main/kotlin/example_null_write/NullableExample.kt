package com.example.example_null_write

/**
 * Kotlin의 Null 안전성 기능을 보여주는 예제 파일입니다.
 * Nullable 타입과 다양한 null 처리 연산자를 소개합니다.
 */
fun main() {
    // 일반 변수는 null을 가질 수 없음
    // var normalString: String = null  // 오류 발생

    // Nullable 타입 (타입 뒤에 ? 추가)
    var nullableString: String? = "Hello"
    println("nullableString 초기값: $nullableString")

    nullableString = null  // 정상 작동: nullable 타입은 null 할당 가능
    println("nullableString이 null인가요? ${nullableString == null}")

    // null 안전 호출 연산자 (?.)
    println("nullableString의 길이: ${nullableString?.length}")  // null이면 null 반환

    // Elvis 연산자 (?:)
    val length = nullableString?.length ?: 0  // null이면 0 반환
    println("nullableString의 길이(기본값 적용): $length")

    // 널 아님 단언 연산자(!!)
    var nonNullString: String? = "Kotlin"
    val forcedLength = nonNullString!!.length  // null이면 NullPointerException 발생
    println("nonNullString의 길이(강제): $forcedLength")

    // 안전한 형변환 (as?)
    val obj: Any = "String value"
    val str: String? = obj as? String  // 안전한 형변환, 실패하면 null
    println("안전한 형변환 결과: $str")

    // 스마트 캐스트
    val nullableValue: String? = "Not null"
    if (nullableValue != null) {
        // 이 블록 내에서는 nullableValue가 자동으로 String 타입으로 캐스팅됨
        println("nullableValue의 길이: ${nullableValue.length}")  // ?. 없이도 안전하게 접근 가능
    }

    // let 함수를 이용한 null 처리
    nullableString = "Let's use let"
    nullableString?.let {
        println("nullableString은 null이 아니며 값은: $it")
    } ?: println("nullableString은 null입니다")
}
