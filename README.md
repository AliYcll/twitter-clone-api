# Twitter Clone API

Bu proje, Spring Boot ile geliştirilmiş bir Twitter benzeri sosyal medya platformunun backend API’sidir. Amaç; Spring Boot ekosistemini pratik etmek, modern bir backend mimarisini deneyimlemek ve temel sosyal medya akışlarını sağlam bir API ile sunmaktır.

## Özellikler

- Kullanıcı yönetimi: Kayıt, giriş ve JWT tabanlı kimlik doğrulama
- Tweet yönetimi: Tweet oluşturma, listeleme, güncelleme, silme
- Yorum yönetimi: Tweetlere yorum ekleme, güncelleme, silme
- Beğenme yönetimi: Like / Unlike
- Retweet yönetimi: Retweet ekleme / silme ve kullanıcı retweet listesini görüntüleme

## Teknolojiler

- Backend Framework: Spring Boot
- Veritabanı: PostgreSQL
- Güvenlik: Spring Security + JWT
- Bağımlılık Yöneticisi: Maven
- Dil: Java (JDK 17+)

## Mimari

- Controller Katmanı: HTTP isteklerini karşılar ve servislere yönlendirir
- Service Katmanı: İş mantığı
- Repository Katmanı: Veritabanı erişimi
- Entity Katmanı: Veri modeli
- Global Exception Handling: Merkezi hata yönetimi
- Veri Doğrulama: Sunucu tarafı validation

## API Temel Yolu

Tüm endpoint’ler şu temel yol altındadır:

`/api/v1`

## Başlangıç

### 1) Gereksinimler

- Java 17 veya üzeri
- Apache Maven
- PostgreSQL
- Tercihen bir IDE (IntelliJ IDEA, VS Code vb.)

### 2) Veritabanı Kurulumu (PostgreSQL)

- PostgreSQL’i çalıştırın.
- Örnek olarak `my_db` adında bir veritabanı oluşturun.

### 3) Konfigürasyon Seçenekleri

Bu proje, `application.properties` içinde gizli veri tutmaz. Yapılandırma için iki seçenek vardır:

#### Seçenek A: Ortam Değişkenleri (Önerilen)

Aşağıdaki ortam değişkenlerini tanımlayın:

- `DB_URL` (örnek: `jdbc:postgresql://localhost:5432/my_db`)
- `DB_USER` (örnek: `my_user`)
- `DB_PASS` (örnek: `my_pass`)
- `JWT_SECRET` (base64 formatında, en az 32 byte karşılığı)

Windows (PowerShell) örnek:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/my_db"
$env:DB_USER="my_user"
$env:DB_PASS="my_pass"
$env:JWT_SECRET="bXktYmFzZTY0LXNlY3JldC1leGFtcGxlLWZvci1kb2NzLW9ubHk="
```

Mac / Linux (bash / zsh) örnek:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/my_db"
export DB_USER="my_user"
export DB_PASS="my_pass"
export JWT_SECRET="bXktYmFzZTY0LXNlY3JldC1leGFtcGxlLWZvci1kb2NzLW9ubHk="
```

#### Seçenek B: Local Profil (Geliştirme Ortamı)

`application-local.properties` dosyası kullanabilirsiniz. Bu dosya `.gitignore` içinde olduğu için Git’e eklenmez.

Örnek içerik:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/my_db
spring.datasource.username=my_user
spring.datasource.password=my_pass
jwt.secret=bXktYmFzZTY0LXNlY3JldC1leGFtcGxlLWZvci1kb2NzLW9ubHk=
```

Local profili aktif etmek için:

Windows (PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
```

Mac / Linux (bash / zsh):

```bash
export SPRING_PROFILES_ACTIVE="local"
```

### 4) Projeyi Çalıştırma

```bash
cd twitter-clone-api/twitter-api
mvn clean install
mvn spring-boot:run
```

Uygulama varsayılan olarak `http://localhost:8080` adresinde çalışır.

## Testler

Testler için H2 in-memory veritabanı kullanılır (ayarları `src/test/resources/application.properties` altındadır).

Testleri çalıştırmak için:

```bash
mvn test
```

## Güvenlik Notları

- Gerçek veritabanı bilgilerini ve JWT secret değerlerini repo’ya eklemeyin.
- `application-local.properties` dosyası zaten `.gitignore` ile dışarıda tutulur.

## Katkıda Bulunma

Katkıda bulunmak isterseniz pull request gönderebilirsiniz.

## Frontend Entegrasyonu

Frontend repo:

- https://github.com/AliYcll/twitter-clone-frontend

Varsayılan çalışma düzeni:

- Backend: http://localhost:8080
- Frontend: http://localhost:3200
- API base path: /api/v1

Frontend uygulaması bu API yapısı ile uyumludur. Backend ayağa kalkmadan frontend tek başına çalışmaz.