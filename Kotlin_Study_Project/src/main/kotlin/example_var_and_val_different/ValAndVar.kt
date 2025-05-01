package com.example.example_var_and_val_different

/**
 * val과 var의 차이점을 보여주는 예제입니다.
 * val: 불변(immutable) 변수 - 한 번 할당하면 값을 변경할 수 없음
 * var: 가변(mutable) 변수 - 값을 변경할 수 있음
 */
fun main() {
    // val: 불변(immutable) 변수 - 한 번 할당하면 값을 변경할 수 없음
    val number = 10L
    // number = 20L  // 오류 발생: Val cannot be reassigned

    // var: 가변(mutable) 변수 - 값을 변경할 수 있음
    var varNumber = 11
    println("varNumber 초기값: $varNumber")
    varNumber = 22  // 정상 작동: var는 재할당 가능
    println("varNumber 변경 후: $varNumber")

    // 타입 명시적 선언도 가능
    val typedNumber: Long = 30L
    var typedVarNumber: Int = 33
    println("typedNumber: $typedNumber, typedVarNumber: $typedVarNumber")

    // val은 참조가 변경되지 않지만, 참조하는 객체의 내용은 변경될 수 있음
    val list = mutableListOf(1, 2, 3)
    // list = mutableListOf(4, 5, 6)  // 오류: val은 재할당 불가
    list.add(4)  // 가능: list 내용은 변경 가능
    println("list에 요소 추가 후: $list")
}
