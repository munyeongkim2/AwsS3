

# Image AWS S3 API

이 프로젝트는 AWS S3에 이미지를 업로드하고 관리하는 REST API를 제공합니다. 주요 기능으로는 이미지 조회, 업로드, 삭제가 있습니다.

---

## 목차


1. [API 개요](#API-개요)
2. [API 사용법](#API-사용법)
    - [모든 이미지 조회](#1-모든-이미지-조회)
    - [이미지 업로드](#2-이미지-업로드)
    - [이미지 삭제](#3-이미지-삭제)
3. [참고 사항](#참고-사항)
4. [프로젝트 빌드 방법](#프로젝트-빌드-방법)
5. [AWS IAM 계정 정보 및 권한 설정](#AWS-IAM-계정-정보-및-권한-설정)
    - [IAM 계정 정보](#IAM-계정-정보)
    - [IAM 계정 권한 정보](#IAM-계정-권한-정보)
6. [프로젝트 구조](#프로젝트-구조)
---

## API 개요

| 기능          | 메서드 | 엔드포인트            | 설명                      |
|---------------|--------|-----------------------|---------------------------|
| 모든 이미지 조회 | `GET`  | `/api/v1/s3/images`   | 모든 이미지를 조회합니다. |
| 이미지 업로드   | `POST` | `/api/v1/s3/images`   | 이미지를 업로드합니다.    |
| 이미지 삭제     | `DELETE` | `/api/v1/s3/images` | 이미지를 삭제합니다.      |

---

## API 사용법

### 1. 모든 이미지 조회

- **URL**: `GET` `http://localhost:8080/api/v1/s3/images`
- **Response**:
    - `200 OK`: 이미지 목록을 성공적으로 반환합니다.
    - **Response Body**:
      ```json
      {
          "body": [
              {
              "url": "https://crowdworks-image.s3.ap-southeast-2.amazonaws.com/mun/KakaoTalk_20240823_221556583.jpg",
              "name": "KakaoTalk_20240823_221556583.jpg",
              "size": 246573,
              "createdAt": "2024-11-02T17:47:26"
              }
          ],
          "header": {
          "isSuccessful": true,
          "resultMessage": "SUCCESS"
          }
      }
      ```
     

### 2. 이미지 업로드

- **URL**: `POST` `http://localhost:8080/api/v1/s3/images`
- **Parameters**:
    - `file` (MultipartFile): 업로드할 이미지 파일 (필수)
    - `path` (String): 이미지를 저장할 경로 (선택 사항, 기본값은 루트 경로 `""`)


- **Request 예시**:
  ```http
  POST /api/v1/s3/images
  Content-Type: multipart/form-data

  file: [업로드할 이미지 파일]
  path: "path/to/directory" (선택 사항)
  ```

- **Response**:
    - `200 OK`: 이미지를 성공적으로 업로드했습니다.
    - **Response Body**:
      ```json
        {
              "body": null,
              "header": {
              "isSuccessful": true,
              "resultMessage": "SUCCESS"
              }
        }
      ```
- 업로드 정보 : 콘솔 로그 출력(`filename`, `s3 url`, `time`)

### 3. 이미지 삭제

- **URL**: `DELETE` `http://localhost:8080/api/v1/s3/images?file={}&path={}`
- **Parameters**:
    - `file` (String): 삭제할 이미지 파일 이름 (필수)
    - `path` (String): 이미지 파일이 저장된 경로 (선택 사항, 기본값은 루트 경로 `""`)

- **Response**:
    - `200 OK`: 이미지를 성공적으로 삭제했습니다.
    - **Response Body**:

        ```json
          {
                "body": null,
                "header": {
                "isSuccessful": true,
                "resultMessage": "SUCCESS"
                }
          }
        ```


## 참고 사항

- `path`를 생략하면 기본 경로(`/default`)에 파일을 업로드하거나 삭제할 수 있습니다.

---





# AWS IAM 계정 정보 및 권한 설정

이 문서는 AWS S3에 접근하기 위한 IAM 계정 정보와 권한 설정을 안내합니다.



## IAM 계정 정보
`appliaction.yml`에 기술해 주세요.

- **Access Key**: `<your-access-key>`
- **Secret Key**: `<your-secret-key>`
- **region**: static: `<your-region>`
- **bucketName**: `<your-bucketName>`
- **path**:
  - 현재 버킷 기본 파일 경로 이름 : `default/`

## IAM 계정 권한 정보

- **권한 정책**: 커스텀으로 부여
  - **s3:ListBucket**: 버킷의 객체 목록 조회 권한
  - **s3:PutObject**: 객체 업로드 권한
  - **s3:GetObject**: 객체 다운로드 권한
  - **s3:DeleteObject**: 객체 삭제 권한
  ```json
    {
        "Version": "2012-10-17",
        "Statement": [
            {
            "Effect": "Allow",
            "Action": [
                        "s3:ListBucket"
                        ],
            "Resource": "arn:aws:s3:::crowdworks-image"
            },
            {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "s3:DeleteObject"
            ],
              "Resource": "arn:aws:s3:::crowdworks-image/*"
            }
        ]
    }
  ```
## 버킷 정책
1. 퍼블릭 액세스 차단(버킷 설정)
   - 모든 퍼블릭 액세스 차단 : `비활성`
2. 버킷 정책
      ```json
        
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Principal": "*",
              "Action": "s3:GetObject",
              "Resource": "arn:aws:s3:::crowdworks-image/*"
            }
          ]
        }
        
      ```
---


# 프로젝트 구조

- 스프링부트 버전 : 3.2.11
- 자바 17, openJdk 17
- SDK for Java : 2.XX
```plaintext
src
├── main
│   ├── java
│   │   └── crowdworks
│   │       └── image
│   │           ├── api
│   │           │   ├── controller
│   │           │   │   ├── ImageController.java            # 이미지 작업 인터페이스
│   │           │   │   └── impl
│   │           │   │       └── ImageControllerImpl.java    # 이미지 작업 구현체
│   │           │   ├── dto
│   │           │   │   └── GetImageResponse.java           # 이미지 응답 데이터 전송 객체
│   │           │   └── service
│   │           │       ├── ImageService.java               # 이미지 작업 서비스 인터페이스
│   │           │       ├── impl
│   │           │       │   └── ImageServiceImpl.java       # 이미지 작업 서비스 구현체
│   │           │       └── ImageExtension.java              # 이미지 파일 확장자 열거형
│   │           ├── common
│   │           │   ├── Response
│   │           │   │   ├── ApiResponse.java                # 표준화된 API 응답 클래스
│   │           │   │   └── ErrorResponse.java              # 오류 응답 처리 클래스
│   │           │   │   └── ErrorCode.java                  # 오류 코드 열거형
│   │           │   ├── exception
│   │           │   │   └── FileServiceException.java        # 파일 서비스 오류를 위한 사용자 정의 예외
│   │           │   └── handler
│   │           │       ├── GlobalResponseHandler.java       # API 응답을 위한 글로벌 핸들러
│   │           │       └── GlobalExceptionHandler.java      # API 오류 처리를 위한 글로벌 예외 핸들러
│   │           │   
│   │           ├── config
│   │           │   └── AwsS3Config.java                     # AWS S3 설정 클래스
│   └── resources
│       └── application.yml                                  # 애플리케이션 설정
│                                  
└── test

```
---
# 프로젝트 빌드 방법

이 문서에서는 Java 17과 Apache Maven 3.9.6을 사용하여 프로젝트를 빌드하는 방법을 설명합니다.

## 요구 사항

- Java 17
- Apache Maven 3.9.6

## 빌드 단계

프로젝트를 빌드하기 위해 다음 단계를 수행하세요.

각 단계는 커맨드 라인(터미널)에서 아래와 같이 입력하여 실행할 수 있습니다.

1. **클린**: 이전 빌드의 산출물을 삭제합니다.
   ```bash
   mvn clean
   ```

2. **컴파일**: 소스 코드를 컴파일합니다.
   ```bash
   mvn compile
   ```

3. **패키지**: 컴파일된 코드를 JAR 파일로 패키징합니다.
   ```bash
   mvn package
   ```

## 빌드 실행



```bash
 java -jar target/image-0.0.1-SNAPSHOT.jar
```

---