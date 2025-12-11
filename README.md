# The Last Garage 🛡️🚛

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)
![LibGDX](https://img.shields.io/badge/LibGDX-1.12.1-red.svg)
![Status](https://img.shields.io/badge/Status-Completed-green.svg)

> **"Protect the last haven of humanity, one gear at a time."**

**The Last Garage** is a strategic Tower Defense game developed as part of the Programming Laboratory II course at Kocaeli University. The players must defend their garage—the last bastion of hope—against waves of relentless mechanical enemies using innovative defensive structures.

---

## 👥 Developers

| Name | Role | GitHub |
|---|---|---|
| **Emir Bera Soğuk** | Lead Developer | [@emirberasoguk](https://github.com/emirberasoguk) |
| **Emre Acar** | Lead Developer | [@emreacar](https://github.com/emreacar) |

---

## 🇬🇧 English Documentation

### 🎮 Game Overview

In a post-apocalyptic world where resources are scarce, your garage is the only thing standing between survival and destruction. You must utilize scrap metal collected from fallen enemies to build and upgrade your defenses. Defend against Scouts, Armored Trucks, and Motorized Raiders!

### ✨ Key Features

* **Dynamic Wave System:** Survive increasingly difficult waves of enemies with varied attributes.
* **Strategic Towers:**
  * **Nail Launcher (Çivi Ağ Atar):** High single-target damage, slows down enemies.
  * **Wrench Thrower (Anahtar Makinesi):** Balanced attack speed and range.
  * **Oil Spiller (Yağ Sızdırıcı):** Area of Effect (AoE) damage and crowd control.
* **Unique Enemy Types:**
  * **Scout Drone:** Fast but fragile flying unit.
  * **Motorized Raider:** Balanced ground unit.
  * **Armored Truck:** Heavy tank unit with high durability.
* **Economy System:** Manage your "Scrap" resources wisely to build the ultimate defense.
* **Combat Log:** Real-time logging of battle events for strategic analysis.

### 🛠️ Installation & How to Run

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/emirberasoguk/The-Last-Garage.git
    cd The-Last-Garage
    ```

2. **Run the Game:**
    * **Windows:** `gradlew desktop:run`
    * **Linux/macOS:** `./gradlew desktop:run`

---

## 🇹🇷 Türkçe Dokümantasyon

### 🎮 Oyun Hakkında

**The Last Garage**, kaynakların tükendiği post-apokaliptik bir dünyada geçen, strateji odaklı bir Kule Savunma oyunudur. Oyuncuların amacı, insanlığın son sığınağı olan garajı, gelen mekanik düşman dalgalarına karşı korumaktır. Düşmanlardan düşen hurdaları (Scrap) toplayarak savunma hattınızı güçlendirin ve garajı ne pahasına olursa olsun koruyun.

### ✨ Temel Özellikler

* **Dinamik Dalga Sistemi:** Her dalgada zorlaşan ve strateji değiştirmenizi gerektiren düşman akınları.
* **Stratejik Savunma Kuleleri:**
  * **Çivi Ağ Atar:** Yüksek hasar verir ve düşmanları yavaşlatır.
  * **Anahtar Makinesi:** Dengeli atış hızı ve menzili ile güvenilir bir savunma birimidir.
  * **Yağ Sızdırıcı:** Alan etkili (AoE) hasar vererek toplu düşman gruplarını eritir.
* **Çeşitli Düşman Birimleri:**
  * **Gözcü Uçağı:** Hızlı ve uçan birim (Hava savunması gerektirir).
  * **Motorlu Çapulcu:** Dengeli hıza sahip kara birimi.
  * **Zırhlı Kamyon:** Yüksek zırhı ve canı ile durdurulması zor bir tank birimi.
* **Kaynak Yönetimi:** Kısıtlı hurda kaynağını verimli kullanarak en iyi savunma hattını kurun.
* **Savaş Günlüğü:** Savaş sırasında gerçekleşen tüm hasar ve olayların detaylı kaydını tutan log sistemi.

### 🛠️ Kurulum ve Çalıştırma

1. **Projeyi Klonlayın:**

    ```bash
    git clone https://github.com/emirberasoguk/The-Last-Garage.git
    cd The-Last-Garage
    ```

2. **Oyunu Başlatın:**
    * **Windows:**

        ```cmd
        gradlew desktop:run
        ```

    * **Linux / macOS:**

        ```bash
        ./gradlew desktop:run
        ```

### 📂 Proje Yapısı

Proje, **LibGDX** modüler yapısını takip eder:

* `core/`: Oyunun mantığı ve tüm kaynak kodları.
* `desktop/`: Masaüstü (PC) platformu başlatıcıları.
* `assets/`: Oyun içi görseller, sesler ve fontlar.

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Developed for **Kocaeli University - Programming Laboratory II**.
