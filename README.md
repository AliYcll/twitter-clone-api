# Twitter Clone API

Bu proje, Spring Boot kullanarak geliştirilmiş bir Twitter benzeri sosyal medya platformunun arka uç (backend) API'sidir. Amacı, Spring Boot ekosisteminde edinilen bilgi ve becerileri pratik etmek, modern bir backend uygulamasının tasarım ve implementasyon süreçlerini deneyimlemektir. Proje, bir Twitter uygulamasının temel fonksiyonlarını sağlamayı hedefler.

## Özellikler

- **Kullanıcı Yönetimi:** Kullanıcı kaydı, girişi ve JWT tabanlı kimlik doğrulama/yetkilendirme.
- **Tweet Yönetimi:** Tweet oluşturma, görüntüleme, güncelleme ve silme.
- **Yorum Yönetimi:** Tweetlere yorum yapma, yorumları güncelleme ve silme.
- **Beğeni Yönetimi:** Tweetleri beğenme ve beğeniyi geri çekme (beğenmeme).
- **Retweet Yönetimi:** Tweetleri retweet etme ve retweetleri silme.

## Teknolojiler

- **Backend Framework:** Spring Boot
- **Veritabanı:** PostgreSQL (Entity katmanı ile uyumlu)
- **Güvenlik:** Spring Security (JWT - JSON Web Token ile)
- **Bağımlılık Yönetimi:** Maven
- **Dil:** Java

## Mimari

Proje, katmanlı mimari prensiplerine uygun olarak tasarlanmıştır:

-   **Controller Katmanı:** Gelen HTTP isteklerini karşılar ve iş mantığına yönlendirir.
-   **Service Katmanı:** İş mantığını ve veri akışını yönetir.
-   **Repository Katmanı:** Veritabanı ile etkileşimi sağlar.
-   **Entity Katmanı:** Veritabanı tablolarını temsil eden POJO sınıflarını içerir.
-   **Global Exception Handling:** Uygulama genelinde merkezi hata yönetimi.
-   **Veri Doğrulama:** Giriş verileri için sunucu tarafı doğrulama.

## Başlangıç

Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

1.  **Gereksinimler:**
    *   Java Development Kit (JDK) 17 veya üzeri
    *   Apache Maven
    *   PostgreSQL veritabanı
    *   Tercihen bir IDE (IntelliJ IDEA, VS Code vb.)
2.  **Veritabanı Kurulumu:**
    *   Bir PostgreSQL sunucusu çalıştırın.
    *   `twitter_clone_db` adında yeni bir veritabanı oluşturun.
    *   `src/main/resources/application.properties` dosyasındaki veritabanı bağlantı bilgilerini kendi ortamınıza göre güncelleyin.
3.  **Projeyi Çalıştırma:**
    *   Projeyi klonlayın: `git clone [proje_reposu_url]`
    *   Proje dizinine gidin: `cd twitter-clone-api/twitter-api`
    *   Maven bağımlılıklarını yükleyin: `mvn clean install`
    *   Uygulamayı çalıştırın: `mvn spring-boot:run`
    *   API varsayılan olarak `http://localhost:8080` adresinde çalışacaktır.

## Katkıda Bulunma

Geliştirmelere katkıda bulunmak isterseniz, lütfen bir pull request göndermekten çekinmeyin.

---