# Tower Defense - Prolab 2 Projesi

[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)
[![LibGDX](https://img.shields.io/badge/LibGDX-1.12.1-red.svg)](https://libgdx.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Bu proje, Kocaeli Üniversitesi Bilgisayar Mühendisliği **Programlama Laboratuvarı II** dersi kapsamında geliştirilmiş bir **Kule Savunma (Tower Defense)** oyunudur. Oyuncular, stratejik kuleler yerleştirerek gelen düşman dalgalarını durdurmalı ve ana üsleri olan garajı korumalıdır.

## 🎮 Oyun Özellikleri

*   **Dinamik Dalga Sistemi:** Giderek zorlaşan düşman dalgaları.
*   **Çeşitli Düşmanlar:** Her biri farklı hız ve dayanıklılığa sahip düşman birimleri.
    *   **Gözcü Uçağı:** Hızlı ama düşük zırhlı.
    *   **Motorlu Çapulcu:** Dengeli hız ve zırh.
    *   **Zırhlı Kamyon:** Yavaş ama yüksek dayanıklılık.
*   **Stratejik Kuleler:** Düşmanları durdurmak için farklı yeteneklere sahip savunma kuleleri.
    *   **Çivi Ağ Atar:** Yüksek hasar potansiyeli.
    *   **Anahtar Makinesi:** Dengeli saldırı hızı ve menzil.
    *   **Yağ Sızdırıcı:** Düşmanları yavaşlatma veya alan hasarı verme potansiyeli.
*   **Kaynak Yönetimi:** Kule inşa etmek için düşmanları yok ederek **Hurda (Scrap)** toplayın.
*   **Savaş Günlüğü:** Savaş sırasında gerçekleşen olayları (hasar, ölüm vb.) takip edebileceğiniz log sistemi.
*   **Kullanıcı Dostu Arayüz:** Kolay anlaşılır menüler ve oyun içi kontroller.

## 🛠️ Kurulum ve Çalıştırma

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin:

### Gereksinimler

*   [Java Development Kit (JDK) 11](https://adoptium.net/) veya üzeri.
*   Git (isteğe bağlı, projeyi klonlamak için).

### Adımlar

1.  **Projeyi Klonlayın:**
    ```bash
    git clone https://github.com/kullaniciadi/prolab2-tower-defense.git
    cd prolab2-tower-defense
    ```

2.  **Oyunu Çalıştırın:**
    *   **Linux / macOS:**
        ```bash
        ./gradlew desktop:run
        ```
    *   **Windows:**
        ```cmd
        gradlew desktop:run
        ```

## 📂 Proje Yapısı

Proje, standart LibGDX modül yapısını takip eder:

*   `core/`: Oyunun tüm mantığını ve kaynak kodlarını içerir. (Platform bağımsız)
*   `desktop/`: Masaüstü (PC) platformu için başlatıcı kodları içerir.
*   `assets/`: Oyun görselleri, fontlar ve diğer medya dosyaları burada bulunur.

## 🤝 Katkıda Bulunma

Katkılarınızı bekliyoruz! Lütfen bir **Pull Request** göndermeden önce mevcut sorunları (issues) kontrol edin veya yeni bir özellik önerisi için tartışma başlatın.

## 📝 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakınız.

---
**Geliştirici:** Emir
**Ders:** Kocaeli Üniversitesi - Programlama Laboratuvarı II