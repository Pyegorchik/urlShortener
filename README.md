# Описание системы
## Функциональность сервиса

Сервис предоставляет следующие возможности:

- Создание коротких ссылок для пользователей.
- Управление ссылками: редактирование и удаление.
- Валидация ссылок: проверка их актуальности (не истекли, не превышено количество кликов).
- Переход по коротким ссылкам с учетом ограничений.
- Хранение ссылок, привязанных к пользователям, с уникальными UUID.

## Структура проекта

- Main: Точка входа в приложение, предоставляет текстовое меню для взаимодействия.
- LinkService: Управляет созданием, валидацией и хранением ссылок.
- UserService: Управляет пользователями и их UUID.
- ConfigLoader: Загружает конфигурационные параметры из файла.
- Link: Модель ссылки с полями для хранения данных.
- User: Модель пользователя с UUID и списком ссылок.

## Инструкция по использованию
Запуск приложения
Запустите Main для старта. При первом создании ссылки программа создаст UUID для пользователя, если он отсутствует.

## Команды меню
Пользователь может выбрать один из следующих пунктов:
1. Создать короткую ссылку: Введите оригинальный URL, количество кликов и срок действия ссылки.
2. Перейти по короткой ссылке: Введите короткий URL для перенаправления.
3. Изменить UUID: Введите новый UUID для пользователя.
4. Просмотреть ссылки пользователя: Выводит все ссылки, привязанные к текущему UUID.
5. Редактировать ссылку: Изменить данные существующей ссылки.
6. Удалить ссылку: Удалить короткую ссылку.
7. Выход: Завершить работу приложения.

  Особенности использования
        Если ссылка устарела или недействительна, она удаляется автоматически при попытке перейти по ней.
