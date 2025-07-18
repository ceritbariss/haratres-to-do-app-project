# Spring Todo App Projesi

Bu proje, kullanıcıların kişisel görevlerini yönetebileceği JWT tabanlı kimlik doğrulama içeren bir Todo uygulamasıdır. 
Kullanıcılar görev ekleyebilir, silebilir, güncelleyebilir ve görevlerini filtreleyebilir. Ayrıca entegre edilen WeatherAPI, 
kullanıcılar istedikleri şehrin anlık hava durumu bilgilerine erişebilir ve planlamalarını buna göre şekillendirebilir.

## Kullanılan Teknolojiler

###  Backend
Java 21 / Spring Boot / Spring Data JPA / JWT

<hr style="border: 0.2px solid #888;" />

### Database
MySQL

<hr style="border: 0.2px solid #888;" />

### API Entegrasyonu
WeatherAPI 



## Projeyi Çalıştırma
### 1. Projeyi Klonla
```bash
git clone https://github.com/ceritbariss/haratres-to-do-app-project.git
cd haratres-to-do-app-project
```

### 2. application.properties Yapılandır
```bash
spring.jpa.hibernate.ddl-auto = update
spring.datasource.url=jdbc:postgresql://localhost:5432/todoapp
spring.datasource.username= #veritabani_kullanici_adi
spring.datasource.password= #veritabani_sifresi


spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username= #gmail_adresiniz
spring.mail.password= #gmail_uygulama_sifresi

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.default-encoding=UTF-8

weather.api.key= #weatherapi_keyiniz
```

### 3. Projeyi Başlatma
```bash
# Windows
mvnw spring-boot:run

# macOS / Linux
chmod +x mvnw
./mvnw spring-boot:run
```


## REST API Endpointleri

## [AUTH](https://github.com/ceritbariss/haratres-to-do-app-project/blob/main/src/main/java/com/todoapp/todoapp/controller/AuthController.java)
<details>

<summary> <b>POST</b> /auth/login </summary>

<b>Açıklama :</b> Kullanıcının kullanıcı adı ve şifresi ile giriş yapmasını sağlar. Başarılı girişte JWT token döner.

<b>URL :</b> http://localhost:8080/auth/login

<b>Request :</b>
```json
{
  "username": "baris1",
  "userPassword": "123456"
}
```

<b>Response :</b>
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYXJpczEiLCJpYXQiOjE3NTI3NjM4NDIsImV4cCI6MTc1Mjc2NzQ0Mn0.Axc_r6sAs-S7w_E94RXg3uPty-6MUTHRhWawCuAkEcc"
}
```
<b>Error :</b>
```text
  Kullanıcı adı veya şifre hatalı!
```
</details>

<details>

<summary> <b>POST</b> /auth/register </summary>

<b>Açıklama :</b> Yeni bir kullanıcı kaydı oluşturur.

<b>URL :</b> http://localhost:8080/auth/register

<b>Request :</b>
```json
{
  "userName": "baris2",
  "password": "123456",
  "firstName": "Barış",
  "lastName": "Cerit",
  "gender": "Male",
  "birthDate": "2003-01-11",
  "email": "cerit.bariss1@gmail.com",
  "phoneNumber": "05349758308"
}
```

<b>Response :</b>
```json
  Kayıt başarılı!
```
<b>Error :</b>
```text
  {
    "timestamp": "2025-07-17T18:01:40.8687937",
    "status": 400,
    "error": "Bad Request",
    "message": "Bu kullanıcı adı kullanılıyor!",
    "path": "/auth/register"
  }
```
</details>

<details>

<summary> <b>POST</b> /auth/forgot-password </summary>

<b>Açıklama :</b> Şifre sıfırlama için e-posta adresine OTP kodu gönderir.

<b>URL :</b> http://localhost:8080/auth/forgot-password

<b>Request :</b>
```json
{
  "email" : "cerit.bariss@gmail.com"
}
```

<b>Response :</b>
```json
  OTP e-mail adresinize gönderildi!
```
<b>Error :</b>
```text
  {
    "timestamp": "2025-07-17T18:06:39.6171454",
    "status": 400,
    "error": "Bad Request",
    "message": "Kullanıcı bulunamadı!",
    "path": "/auth/forgot-password"
  }
```
</details>

<details>

<summary> <b>POST</b> /auth/verify-otp </summary>

<b>Açıklama :</b> Kullanıcının e-posta adresine gönderilen OTP kodunun doğruluğunu kontrol eder.

<b>URL :</b> http://localhost:8080/auth/verify-otp

<b>Request :</b>
```json
{
  "email" : "cerit.bariss@gmail.com",
  "otp" : "494325"
}
```

<b>Response :</b>
```json
  OTP Doğrulandı!
```
<b>Error :</b>
```text
  {
    "timestamp": "2025-07-17T18:09:19.47802",
    "status": 400,
    "error": "Bad Request",
    "message": "Hatalı E-Mail Veya OTP Girişi Yaptınız!",
    "path": "/auth/verify-otp"
  }
```
</details>

<details>

<summary> <b>POST</b> /auth/reset-password </summary>

<b>Açıklama :</b> Girilen OTP ve yeni şifre ile kullanıcı şifresini günceller.

<b>URL :</b> http://localhost:8080/auth/reset-password

<b>Request :</b>
```json
{
  "email" : "cerit.bariss@gmail.com",
  "otp" : "494325",
  "newPassword" : "123456"
}
```

<b>Response :</b>
```json
  Şifreniz başarıyla güncellendi.
```
<b>Error :</b>
```json
  {
  "timestamp": "2025-07-17T18:14:03.005528",
  "status": 400,
  "error": "Bad Request",
  "message": "OTP bulunamadı.",
  "path": "/auth/reset-password"
  }
```
</details>

## [USER](https://github.com/ceritbariss/haratres-to-do-app-project/blob/main/src/main/java/com/todoapp/todoapp/controller/UserController.java)
<details>

<summary> <b>PATCH</b> /api/me </summary>

<b>Açıklama :</b> Giriş yapmış kullanıcının bilgilerini günceller, eğer kullanıcı adı güncellendiyse yeni JWT token döner.

<b>URL :</b> http://localhost:8080/api/me

<b>Request :</b>
```json
{
  "username": "bariscerit",
  "password": "123456",
  "firstName": "Barış",
  "lastName": "Cerit",
  "gender": "Erkek",
  "birthDate": "2000-05-12",
  "email": "cerit.bariss@hotmail.com",
  "phoneNumber": "05341234567"
}
```

<b>Response :</b>

Username güncellendiği için JWT döndü.
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYXJpc2Nlcml0IiwiaWF0IjoxNzUyODIwNjY2LCJleHAiOjE3NTI4MjQyNjZ9.2A-p0GVLV2-4i66UfuzFRmWjJtZJ0XTaIgat_TtBKcw"
}
```
Eğer username güncellenmezse.
```json
Kullanıcı bilgileri güncellendi
```

<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T09:49:02.3009879",
  "status": 400,
  "error": "Bad Request",
  "message": "Girdiğiniz kullanıcı adı başkası tarafından kullanılıyor yada şuanda kullandığınız kullanıcı adına eşit!",
  "path": "/api/me"
}
```
</details>

<details>

<summary> <b>DELETE</b> /api/me </summary>

<b>Açıklama :</b> Giriş yapan kullanıcının hesabını siler.

<b>URL :</b> http://localhost:8080/api/me


</details>

## [TODO](https://github.com/ceritbariss/haratres-to-do-app-project/blob/main/src/main/java/com/todoapp/todoapp/controller/TodoController.java)
<details>

<summary> <b>GET</b> /api/todos </summary>

<b>Açıklama :</b> Giriş yapan kullanıcının todolarını sayfalı olarak getirir.

<b>URL :</b> http://localhost:8080/api/todos?page=0&size=1

<b>Request :</b>
```json
page = 0
size = 1
```

<b>Response :</b>
```json
{
  "content": [
    {
      "id": 66,
      "title": "Frontend bağlantısını test et",
      "description": "React uygulaması ile API endpointleri arasındaki bağlantı kontrol edilecek",
      "createdDate": "2025-07-17 14:41",
      "updateDate": "2025-07-17 14:41",
      "dueDate": "2026-07-10 14:15",
      "status": "CREATED"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 9,
  "totalPages": 9,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "empty": false
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T09:56:51.9068532",
  "status": 400,
  "error": "Bad Request",
  "message": "Page index must not be less than zero",
  "path": "/api/todos"
}
```
</details>

<details>

<summary> <b>GET</b> /api/todos/status </summary>

<b>Açıklama :</b> Belirtilen duruma göre todoları sayfalı olarak listeler.

<b>URL :</b> http://localhost:8080/api/todos/status?status=IN_PROGRESS&page=0&size=1

<b>Request :</b>

Status değerleri = IN_PROGRESS / CREATED / COMPLETE
```json
status = IN_PROGRESS
page = 0
size = 1
```

<b>Response :</b>
```json
{
  "content": [
    {
      "id": 41,
      "title": "Hata mesajlarını özelleştir",
      "description": "ValidationException ve RuntimeException mesajları kullanıcı dostu hale getirilecek",
      "createdDate": "2025-07-14 17:49",
      "updateDate": "2025-07-14 17:56",
      "dueDate": "2026-07-18 09:30",
      "status": "IN_PROGRESS"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 2,
  "totalPages": 2,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "empty": false
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:03:43.5334226",
  "status": 400,
  "error": "Bad Request",
  "message": "Geçersiz status değeri: 'IN_PROGR'. Beklenen tip: Status.",
  "path": "/api/todos/status"
}
```
</details>

<details>

<summary> <b>GET</b> /api/todos/search </summary>

<b>Açıklama :</b> Girilen anahtar kelimeyi içeren başlıklara sahip görevleri sayfalı olarak listeler.

<b>URL :</b> http://localhost:8080/api/todos/search?keyword=front&page=0&size=1

<b>Request :</b>

```json
keyword = front
page = 0
size = 1
```

<b>Response :</b>
```json
{
  "content": [
    {
      "id": 66,
      "title": "Frontend bağlantısını test et",
      "description": "React uygulaması ile API endpointleri arasındaki bağlantı kontrol edilecek",
      "createdDate": "2025-07-17 14:41",
      "updateDate": "2025-07-17 14:41",
      "dueDate": "2026-07-10 14:15",
      "status": "CREATED"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 2,
  "totalPages": 2,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "empty": false
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:08:04.5518902",
  "status": 400,
  "error": "Bad Request",
  "message": "searchTodosByTitle.keyword: Anahtar kelime boş olamaz.",
  "path": "/api/todos/search"
}
```
</details>

<details>

<summary> <b>GET</b> /api/todos/sorted </summary>

<b>Açıklama :</b> Girilen sıralama kriterine ve yönüne göre görevleri sayfalı olarak sıralı şekilde listeler.

<b>URL :</b> http://localhost:8080/api/todos/sorted?sortBy=dueDate&direction=desc&page=0&size=1

<b>Request :</b>

sortBy değerleri = createdDate / updateDate / dueDate
```json
sortBy = dueDate
direction = desc
page = 0
size = 1
```

<b>Response :</b>
```json
{
  "content": [
    {
      "id": 42,
      "title": "Yeni görev oluşturma modalı ekle",
      "description": "Kullanıcı yeni todo eklerken modal pencere ile işlem yapılacak",
      "createdDate": "2025-07-14 17:49",
      "updateDate": "2025-07-14 17:49",
      "dueDate": "2026-07-19 14:00",
      "status": "CREATED"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 9,
  "totalPages": 9,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "empty": false
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:12:28.3917741",
  "status": 400,
  "error": "Bad Request",
  "message": "No property 'dueDa' found for type 'Todo'; Did you mean 'dueDate'",
  "path": "/api/todos/sorted"
}
```
</details>

<details>

<summary> <b>POST</b> /api/todos/create </summary>

<b>Açıklama :</b> Yeni bir görev oluşturarak veritabanına kaydeder ve oluşturulan görevi döner.

<b>URL :</b> http://localhost:8080/api/todos/create

<b>Request :</b>

```json
{
  "title": "Frontend bağlantısını test et",
  "description": "React uygulaması ile API endpointleri arasındaki bağlantı kontrol edilecek",
  "dueDate": "2026-07-10T14:15:00"
}
```

<b>Response :</b>
```json
{
  "id": 67,
  "title": "Frontend bağlantısını test et",
  "description": "React uygulaması ile API endpointleri arasındaki bağlantı kontrol edilecek",
  "createdDate": "2025-07-18 10:16",
  "updateDate": "2025-07-18 10:16",
  "dueDate": "2026-07-10 14:15",
  "status": "CREATED"
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:16:35.6556481",
  "status": 400,
  "error": "Bad Request",
  "message": "Title alanı boş olamaz.",
  "path": "/api/todos/create"
}
```
</details>

<details>

<summary> <b>DELETE</b> /api/todos/{id} </summary>

<b>Açıklama :</b> Belirtilen ID'ye sahip görevi siler. Başarılı işlemde içerik döndürülmez (204 No Content).

<b>URL :</b> http://localhost:8080/api/todos/{id}

<b>Request :</b>

```json
id = 67
```

<b>Response :</b>
```json
204
No Content
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:20:00.1599167",
  "status": 400,
  "error": "Bad Request",
  "message": "Bu todo size ait değil veya böyle bir todo yok.",
  "path": "/api/todos/45"
}
```
</details>

<details>

<summary> <b>PATCH</b> /api/todos/{id} </summary>

<b>Açıklama :</b> Belirtilen ID'ye sahip görevin bilgilerini günceller ve güncellenmiş görevi döner.

<b>URL :</b> http://localhost:8080/api/todos/{id}

<b>Request :</b>

```json
{
  "title": "Alışveriş Yap",
  "description": "Market alışverişi yapılacak. Sebzeler ve temel ihtiyaçlar alınacak.",
  "dueDate": "2025-07-20T15:30:00",
  "status" : "IN_PROGRESS"
}
```

<b>Response :</b>
```json
{
  "id": 38,
  "title": "Alışveriş Yap",
  "description": "Market alışverişi yapılacak. Sebzeler ve temel ihtiyaçlar alınacak.",
  "createdDate": "2025-07-14 17:48",
  "updateDate": "2025-07-18 10:24",
  "dueDate": "2025-07-20 15:30",
  "status": "IN_PROGRESS"
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:24:06.7490038",
  "status": 400,
  "error": "Bad Request",
  "message": "Bu todo size ait değil veya böyle bir todo yok.",
  "path": "/api/todos/45"
}
```
</details>


## [WEATHER](https://github.com/ceritbariss/haratres-to-do-app-project/blob/main/src/main/java/com/todoapp/todoapp/controller/WeatherController.java)
<details>

<summary> <b>GET</b> /api/weather </summary>

<b>Açıklama :</b> Girilen şehir, ilçe ve gün sayısına göre hava durumu tahminini döner.

<b>URL :</b> http://localhost:8080/api/weather?city=İstanbul&district=Maltepe&days=1

<b>Request :</b>

```json
city = İstanbul
district = Maltepe
days = 1
```

<b>Response :</b>
```json
{
  "location": {
    "name": "Maltepe",
    "region": "Istanbul"
  },
  "forecast": {
    "forecastday": [
      {
        "date": "2025-07-18",
        "day": {
          "condition": {
            "text": "Güneşli"
          },
          "maxtemp_c": 27.4,
          "mintemp_c": 22.4,
          "maxwind_kph": 24.5,
          "avghumidity": 67
        }
      }
    ]
  }
}
```
<b>Error :</b>
```json
{
  "timestamp": "2025-07-18T10:28:55.3488626",
  "status": 400,
  "error": "Bad Request",
  "message": "400 Bad Request on GET request for \"https://api.weatherapi.com/v1/forecast.json\": \"{\"error\":{\"code\":1006,\"message\":\"No matching location found.\"}}\"",
  "path": "/api/weather"
}
```
</details>