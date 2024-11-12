# Vergi Hesaplama

Bu proje, Spring Boot ile geliştirilmiş, Spring Security, Docker, Docker Compose ve Spring Test kullanılan bir uygulamadır. Uygulama, MongoDB ile entegre edilmiştir ve Docker konteynerleri ile çalıştırılabilir.

## Özellikler

- RESTful API ile CRUD işlemleri yapıldı.
- Spring Security kullanarak kullanıcının kayıt olması ve token ile endpointlere istek atması sağlandı.
- Aynı zamanda Spring Security kullanarak product kayıt eden kullanıcının bilgileri token üzerinde alındı.
- MongoDB ile veriler nosql'de saklandı.
- Docker ile containerlar ayaklandırıldı ve volume ile Mongodb içinde bulunan veriler kalıcı olarak saklanmaya çalışıldı.
- Spring Test ile birim ve entegrasyon testleri yapıldı.

## Teknolojiler

- Java 17
- Mongodb
- Spring Boot 3.0
- Restful API
- Maven
- Mockito
- Docker
- Docker Compose
- Postman


## Endpoints

| Method       | URL             | Açıklama               |
|--------------|-----------------|------------------------|
| `POST`       | `/api/register` | Yeni kullanıcı kaydı   |
| `POST`       | `/api/login`    | Kullanıcı kimlik doğrulama |
| `GET`        | `/tax/get`      | Vergileri listeler       |
| `GET`       | `/tax/get/{name}`   | Belirli bir vergi bilgilerini getir |
| `POST`        | `/tax/create`     | Yeni vergi oluşturulur |
| `PUT`     | `/tax/update/{name}` | Belirli bir vergi güncellenir |
| `DELETE`  | `/tax/delete/{name}` | Belirli bir vergi silinir |
| `GET`     | `/product/get` | Kullanıcı kendi ürünlerini listeler |
| `POST`     | `/product/create` | Kullancı ürün kayıt eder |
| `PUT`     | `/product/update/{name}` | Kullanıcı kendi ürünü günceller|
| `DELETE`  | `/product/delete/{name}` | Kullanıcı kendi ürünü siler|


## Postman
- Kullanıcı Kayıt Olma
```sh
{   
    "username": "username_admin",
    "password": "password_admin", 
    "firstName": "firstname_admin",
    "lastName": "lastname_admin",
    "role": "USER"
}
```

- Kullanıcı Giriş Yapma
```sh
{   
    "username": "username_admin",
    "password": "password_admin", 
}
```

- Vergi Oluşturma ve Güncelleme
```sh
{
    "name": "tax_name",
    "rate": 1
}
```

- Ürün Oluşturma ve Güncelleme
```sh
{
  "name": "Product_name",
  "basePrice": 100,
  "quantity": 10,
  "taxName": "tax_name"
}
```

## Docker
Uygulama Docker engine tarafından oluşturulabilir ve çalıştırılabilir. 
```sh
docker-compose up --build
```

### Kurulum
```sh
git clone https://github.com/eyupakdniz/tax-calculator.git
cd tax-calculator
```
