# Twitter Clone API

Bu proje, Spring Boot ile gelistirilmis bir Twitter benzeri sosyal medya platformunun backend API'sidir. Amaç; Spring Boot ekosistemini pratik etmek, modern bir backend mimarisini deneyimlemek ve temel sosyal medya akýþlarýný saglam bir API ile sunmaktýr.

## Ozellikler

- Kullanici yonetimi: Kayit, giris ve JWT tabanli kimlik dogrulama
- Tweet yonetimi: Tweet olusturma, listeleme, guncelleme, silme
- Yorum yonetimi: Tweetlere yorum ekleme, guncelleme, silme
- Begenme yonetimi: Like/Unlike
- Retweet yonetimi: Retweet ekleme/silme ve kullanici retweet listesini goruntuleme

## Teknolojiler

- Backend Framework: Spring Boot
- Veritabani: PostgreSQL
- Guvenlik: Spring Security + JWT
- Bagimlilik Yoneticisi: Maven
- Dil: Java (JDK 17+)

## Mimari

- Controller Katmani: HTTP isteklerini karsilar ve servislere yonlendirir
- Service Katmani: Is mantigi
- Repository Katmani: Veritabani erisimi
- Entity Katmani: Veri modeli
- Global Exception Handling: Merkezi hata yonetimi
- Veri Dogrulama: Sunucu tarafi validation

## API Temel Yolu

Tum endpoint'ler su temel yol altindadir:

`/api/v1`

## Baslangic

### 1) Gereksinimler

- Java 17 veya uzeri
- Apache Maven
- PostgreSQL
- Tercihen bir IDE (IntelliJ IDEA, VS Code vb.)

### 2) Veritabani Kurulumu (PostgreSQL)

- PostgreSQL calistirin.
- Ornek olarak `my_db` adinda bir veritabani olusturun.

### 3) Konfigurasyon Secenekleri

Bu proje, `application.properties` icinde gizli veri tutmaz. Yapilandirma icin iki secenek vardir:

#### Secenek A: Ortam Degiskenleri (Onerilen)

Asagidaki ortam degiskenlerini tanimlayin:

- `DB_URL` (ornek: `jdbc:postgresql://localhost:5432/my_db`)
- `DB_USER` (ornek: `my_user`)
- `DB_PASS` (ornek: `my_pass`)
- `JWT_SECRET` (uzun ve tahmin edilmesi zor bir deger)

Windows (PowerShell) ornek:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/my_db"
$env:DB_USER="my_user"
$env:DB_PASS="my_pass"
$env:JWT_SECRET="change_me_to_a_long_random_secret"
```

Mac/Linux (bash/zsh) ornek:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/my_db"
export DB_USER="my_user"
export DB_PASS="my_pass"
export JWT_SECRET="change_me_to_a_long_random_secret"
```

#### Secenek B: Local Profil (Gelisme Ortami)

`application-local.properties` dosyasi kullanabilirsiniz. Bu dosya `.gitignore` icinde oldugu icin Git'e eklenmez.

Ornek icerik:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/my_db
spring.datasource.username=my_user
spring.datasource.password=my_pass
jwt.secret=change_me_to_a_long_random_secret
```

Local profili aktif etmek icin:

Windows (PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
```

Mac/Linux (bash/zsh):

```bash
export SPRING_PROFILES_ACTIVE="local"
```

### 4) Projeyi Calistirma

```bash
cd twitter-clone-api/twitter-api
mvn clean install
mvn spring-boot:run
```

Uygulama varsayilan olarak `http://localhost:8080` adresinde calisir.

## Testler

Testler icin H2 in-memory veritabani kullanilir (ayarlari `src/test/resources/application.properties` altindadir).

Testleri calistirmak icin:

```bash
mvn test
```

## Guvenlik Notlari

- Gercek veritabani bilgilerini ve JWT secret degerlerini repo'ya eklemeyin.
- `application-local.properties` dosyasi zaten `.gitignore` ile disarida tutulur.

## Katkida Bulunma

Katkida bulunmak isterseniz pull request gonderebilirsiniz.
