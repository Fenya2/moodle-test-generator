# Moodle Test Generator

**Moodle Test Generator** — это программа для автоматизированного создания тестов, совместимых с платформой [Moodle](https://moodle.org/). С его помощью преподаватели могут быстро генерировать файлы вопросов в формате, совместимом с Moodle.


## Возможности

Программа позволяет:

- Создавать шаблонные вопросы с переменными (параметрами)
- Автоматически генерировать множество вариантов одного вопроса с разными значениями параметров
- Экспортировать готовые вопросы в формате [GIFT](https://docs.moodle.org/501/en/GIFT_format)

## Сборка программы

Чтобы собрать программу, нужно предварительно установить:

- [JDK 25](https://jdk.java.net/25/)
- [Apache Maven](https://maven.apache.org/download.cgi)

```bash
git clone https://github.com/fenya2/MoodleTestGenerator.git
cd MoodleTestGenerator
mvn clean install -Pbuild-release-artifact
```

В итоге требуемый исполняемый *.jar* файл программы будет находиться в директории `target`.

## Требования к запуску программы

Для запуска программы требуется установить:

- [JDK 25](https://jdk.java.net/25/)
- [JavaFX 25](https://jdk.java.net/javafx25/)

## Запуск программы

Программа представляет из себя исполняемый *.jar*-файл. Файл программы можно скачать на странице [релизов проекта](https://github.com/Fenya2/moodle-test-generator/releases) или же [собрать из исходных кодов](#сборка-программы).


Для запуска программы выполните команду:

```bash
java --module-path <PATH_TO_JAVAFX_LIBS> --add-modules javafx.controls,javafx.fxml,javafx.web -jar <PATH_TO_PROGRAM>
```

где:

- *PATH_TO_JAVAFX_LIBS* - путь к директории `lib` в [установленной ранее](#требования-к-запуску-программы) платформе JavaFX (подробности можно посмотреть в [официальной документации к запуску приложений на базе JavaFX](https://openjfx.io/openjfx-docs/#install-javafx))
- *PATH_TO_PROGRAM* - путь к программе

> Подразумевается, что путь к *java* указан в переменной окружения *PATH*

Пример:

```bash
java --module-path ~/opt/openjfx-25.0.1_linux-x64_bin-sdk/javafx-sdk-25.0.1/lib/ --add-modules javafx.controls,javafx.fxml,javafx.web -jar ./target/MoodleTestGenerator-1.0.jar
```


## Контакты

- *Email:* [fenya74.09@gmail.com](mailto:fenya74.09l@gmail.com)
- *Telegram:* [@fenya00](https://t.me/fenya00)