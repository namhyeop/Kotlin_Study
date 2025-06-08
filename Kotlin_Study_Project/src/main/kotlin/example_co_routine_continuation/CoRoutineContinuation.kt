package com.example.example_co_routine_continuation

import kotlinx.coroutines.delay

/**
 * 이 예제는 Kotlin 컴파일러가 suspend 함수를 어떻게 상태 머신으로 변환하는지 보여줍니다.
 * 실제로는 컴파일러가 자동으로 이런 변환을 수행하므로 개발자가 직접 작성할 필요는 없습니다.
 */

/*
실행 흐름 요약:
1. main()에서 findUser(1L, null) 호출
2. findUser() 내에서 새로운 FindUserContinuation 객체 생성
3. label=0이므로 프로필 조회 시작
4. userProfileRepository.findProfile() 호출 후 함수 중단
5. 프로필 조회 완료 후 continuation.resumeWith() 호출
6. resumeWith()에서 profile 저장하고 label=1로 변경 후 findUser() 재호출
7. label=1이므로 이미지 조회 시작
8. userImageRepository.findImage() 호출 후 함수 중단
9. 이미지 조회 완료 후 continuation.resumeWith() 호출
10. resumeWith()에서 image 저장하고 label=2로 변경 후 findUser() 재호출
11. 모든 데이터가 준비되었으므로 UserDto 반환
*/

suspend fun main(){
    val service = UserService()
    // 사용자 정보 조회를 시작 - 첫 번째 호출에서는 continuation이 null
    println(service.findUser(1L, null))
}

/**
 * 코루틴의 중단과 재개를 관리하는 인터페이스
 * 실제 Kotlin 코루틴에서는 더 복잡하지만, 여기서는 개념 이해를 위해 단순화
 */
interface Continuation {
    suspend fun resumeWith(data: Any?)
}

class UserService {

    private val userProfileRepository = UserProfileRepository();
    private val userImageRepository = UserImageRepository()

    /**
     * 코루틴의 상태를 관리하는 상태 머신(State Machine) 클래스
     * - label: 현재 실행 단계를 나타내는 상태값 (0: 프로필 조회, 1: 이미지 조회, 2: 완료)
     * - profile, image: 각 단계에서 얻은 데이터를 저장하는 변수들
     */
    private abstract class FindUserContinuation() : Continuation {
        var label = 0                    // 현재 실행 중인 단계 (상태)
        var profile: Profile? = null     // 1단계에서 조회된 프로필 정보
        var image: Image? = null         // 2단계에서 조회된 이미지 정보
    }

    /**
     * 사용자 정보를 조회하는 suspend 함수
     * @param userId 조회할 사용자 ID
     * @param continuation 코루틴의 상태를 관리하는 객체 (첫 호출시에는 null)
     * @return 사용자 정보 DTO
     */
    suspend fun findUser(userId: Long, continuation: Continuation?): UserDto {
        // 상태 머신 객체 생성 또는 기존 것 재사용
        val sm = continuation as? FindUserContinuation ?: object : FindUserContinuation() {

            /**
             * 비동기 작업이 완료되었을 때 호출되는 콜백 메서드
             * 각 단계별로 받은 데이터를 저장하고 다음 단계로 넘어감
             */
            override suspend fun resumeWith(data: Any?) {
                when (label) {
                    0 -> {
                        // 첫 번째 단계: 프로필 조회 완료
                        profile = data as Profile
                        label = 1  // 다음 단계(이미지 조회)로 상태 변경
                    }

                    1 -> {
                        // 두 번째 단계: 이미지 조회 완료
                        image = data as Image
                        label = 2  // 완료 상태로 변경
                    }
                }
                // 상태가 변경된 후 다시 findUser 함수를 호출하여 다음 단계 실행
                findUser(userId, this)
            }
        }

        // 현재 상태(label)에 따라 실행할 작업 결정
        when (sm.label) {
            0 -> {
                // 첫 번째 단계: 사용자 프로필 조회
                println("프로필 조회")
                userProfileRepository.findProfile(userId, sm)
                // 여기서 함수가 중단됨 (suspend)
                // 프로필 조회가 완료되면 sm.resumeWith()이 호출됨
            }

            1 -> {
                // 두 번째 단계: 사용자 이미지 조회 (프로필 조회 완료 후)
                println("이미지 조회")
                userImageRepository.findImage(sm.profile!!, sm)
                // 여기서 함수가 다시 중단됨 (suspend)
                // 이미지 조회가 완료되면 sm.resumeWith()이 호출됨
            }
        }
        // 모든 단계가 완료되면 최종 결과 반환
        return UserDto(sm.profile!!, sm.image!!)
    }
}

/**
 * 사용자 프로필 정보를 조회하는 Repository 클래스
 */
class UserProfileRepository {
    /**
     * 사용자 프로필을 비동기로 조회
     * @param userId 사용자 ID
     * @param continuation 조회 완료 후 호출할 콜백
     * @return 조회된 프로필 정보
     */
    suspend fun findProfile(userId: Long, continuation: Continuation): Profile {
        delay(100L)  // 네트워크 호출이나 DB 조회 등의 비동기 작업을 시뮬레이션

        // 조회 완료 후 continuation을 통해 결과 전달
        continuation.resumeWith(Profile())
        return Profile()
    }
}

class Profile  // 사용자 프로필 정보를 담는 데이터 클래스

/**
 * 사용자 이미지 정보를 조회하는 Repository 클래스
 */
class UserImageRepository {
    /**
     * 사용자 이미지를 비동기로 조회
     * @param profile 이미지 조회에 필요한 프로필 정보
     * @param continuation 조회 완료 후 호출할 콜백
     * @return 조회된 이미지 정보
     */
    suspend fun findImage(profile: Profile, continuation: Continuation): Image {
        delay(100L)  // 이미지 다운로드 등의 비동기 작업을 시뮬레이션

        // 조회 완료 후 continuation을 통해 결과 전달
        continuation.resumeWith(Image())
        return Image()
    }
}

class Image  // 사용자 이미지 정보를 담는 데이터 클래스

/**
 * 사용자 정보를 담는 DTO 클래스
 * @param profile 사용자 프로필 정보
 * @param image 사용자 이미지 정보
 */
class UserDto(
    val profile: Profile,
    val image: Image
)
