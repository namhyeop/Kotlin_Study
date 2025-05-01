/**
 * Kotlin의 객체 선언과 사용에 관한 예제 파일입니다.
 * 클래스, 데이터 클래스, 싱글톤 객체 등의 사용법을 보여줍니다.
 */

// 기본 클래스 정의
class Person(val name: String, var age: Int) {
    // 프로퍼티
    var email: String? = null

    // 초기화 블록
    init {
        println("새로운 Person 객체 생성: $name, ${age}세")
    }

    // 메서드
    fun introduce() {
        println("안녕하세요! 저는 ${name}이고, ${age}살입니다.")
        println("이메일: ${email ?: "없음"}")
    }

    // 보조 생성자
    constructor(name: String, age: Int, email: String) : this(name, age) {
        this.email = email
    }
}

// 데이터 클래스 (equals, hashCode, toString, copy 등 자동 구현)
data class Product(val id: Int, val name: String, val price: Double) {
    fun applyDiscount(discountPercent: Double): Product {
        val discountedPrice = price * (1 - discountPercent / 100)
        return copy(price = discountedPrice)
    }
}

// 객체 싱글톤
object DatabaseConfig {
    val url = "jdbc:mysql://localhost:3306/mydb"
    val username = "user"
    val password = "password"

    fun connect() {
        println("DB에 연결 중... $url")
    }
}

// 클래스 내부의 동반 객체
class UserService {
    companion object {
        const val MAX_USERS = 100

        fun createUser(name: String): String {
            return "사용자 $name 생성됨 (최대 허용: $MAX_USERS)"
        }
    }

    fun getUserCount(): Int {
        return 42  // 예시 값
    }
}

// 실행 예시
fun main() {
    // 기본 클래스 사용
    println("\n==== 일반 클래스 사용 ====")
    val person1 = Person("김코틀린", 25)
    person1.introduce()

    val person2 = Person("박자바", 30, "java@example.com")
    person2.introduce()

    // 데이터 클래스 사용
    println("\n==== 데이터 클래스 사용 ====")
    val product = Product(1, "노트북", 1500000.0)
    println("상품 정보: $product")

    // 복사하면서 일부 속성만 변경
    val discountedProduct = product.copy(price = 1350000.0)
    println("할인된 상품(copy 사용): $discountedProduct")

    // 메서드를 사용한 할인
    val saleProduct = product.applyDiscount(10.0)
    println("할인된 상품(10% 할인): $saleProduct")

    // 데이터 클래스 구조 분해
    val (id, name, price) = product
    println("구조 분해: id=$id, name=$name, price=$price")

    // 싱글톤 객체 사용
    println("\n==== 싱글톤 객체 사용 ====")
    DatabaseConfig.connect()
    println("DB 접속 정보: ${DatabaseConfig.username}@${DatabaseConfig.url}")

    // 동반 객체 사용
    println("\n==== 동반 객체 사용 ====")
    println(UserService.createUser("홍길동"))
    println("최대 사용자 수: ${UserService.MAX_USERS}")

    // 일반 메서드는 인스턴스 필요
    val service = UserService()
    println("현재 사용자 수: ${service.getUserCount()}")
}
