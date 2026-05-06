## 1. Даалгаврын зорилго
`csa311_hw1.pdf`-д заасан шаардлагын дагуу интерактив командын мөрийн flashcard системийг хэрэгжүүлж, карт эрэмбэлэгч стратегиуд, achievement систем болон чанарын суурь тохиргоонуудыг бүрдүүлэв.

## 2. Хэрэгжүүлсэн шийдэл
### 2.1 Командын мөрийн интерфейс (Даалгавар 1)
Хэрэгжүүлсэн формат:

```bash
flashcard <cards-file> [options]
```
Дэмжсэн сонголтууд:
- `--help`
- `--order <order>` (`random`, `worst-first`, `recent-mistakes-first`)
- `--repetitions <num>`
- `--invertCards`

Хэрэгжилтийн гол хэсгүүд:
- CLI парсинг: `src/main/java/flashcard/cli/FlashcardCliParser.java`
- CLI тохиргооны бүтэц: `src/main/java/flashcard/cli/FlashcardConfiguration.java`
- Программын entrypoint ба алдааны урсгал: `src/main/java/flashcard/FlashcardApplication.java`

Шаардлагад нийцэж буй нотолгоо:
- `--help` нь бусад option-той хамт ирсэн ч зөвхөн тусламж хэвлээд дуусна.
- Буруу оролт бүрт ойлгомжтой алдаа буцааж usage хэвлэнэ.
- `recent-mistakes-first` утгыг CLI-аас зөв танина.
- Картын файлын формат нь текст файл (`question|answer`) бөгөөд коммент/хоосон мөрийг зөв алгасна.

Тест:
- `src/test/java/flashcard/cli/FlashcardCliParserTest.java`

### 2.2 CardOrganizer интерфейс ба RecentMistakesFirstSorter (Даалгавар 2)
Хэрэгжүүлэлт:
- Интерфейс: `src/main/java/flashcard/organizer/CardOrganizer.java`
- Шинэ ангилагч: `src/main/java/flashcard/organizer/RecentMistakesFirstSorter.java`
- Төрөл сонголт ба factory холболт:
  - `src/main/java/flashcard/organizer/OrderType.java`
  - `src/main/java/flashcard/organizer/CardOrganizerFactory.java`

Ажиллах зарчим:
- Өмнөх тойрогт буруу хариулсан картуудыг эхэнд гаргана.
- Буруу хариулсан картуудын доторх дараалал хадгалагдана.
- Мөн зөв хариулсан картуудын доторх дараалал мөн хадгалагдана.

Тест:
- `src/test/java/flashcard/organizer/RecentMistakesFirstSorterTest.java`

### 2.3 Achievement нэмэлтүүд (Даалгавар 3)
Achievement enum-д дараахуудыг нэмсэн:
- `CORRECT`: Сүүлийн тойрог бүх зөв
- `REPEAT`: Нэг картад 5-аас олон удаа хариулсан
- `CONFIDENT`: Нэг картад 3+ удаа зөв хариулсан

Мөн өмнөх `SPEED` achievement хадгалсан.

Хэрэгжилтийн гол хэсгүүд:
- `src/main/java/flashcard/achievement/Achievement.java`
- `src/main/java/flashcard/achievement/AchievementEvaluator.java`
- Session нэгтгэл ба achievement хэвлэлт: `src/main/java/flashcard/session/StudySession.java`

Тест:
- `src/test/java/flashcard/achievement/AchievementEvaluatorTest.java`

### 2.4 Картын файлын формат ба IO
Текст формат:
- Мөр бүр `question|answer`
- `#`-оор эхэлсэн мөр comment
- `\|` escape дэмждэг

Хэрэгжилт:
- `src/main/java/flashcard/io/CardDeckLoader.java`
- Загвар класс: `src/main/java/flashcard/model/Card.java`

Тест:
- `src/test/java/flashcard/io/CardDeckLoaderTest.java`

## 3. Инфраструктур ба чанар
Maven тохиргоо:
- Java 17 компайл
- JUnit 5 тест
- `maven-site-plugin`, `maven-project-info-reports-plugin`, `maven-javadoc-plugin`

Файл:
- `pom.xml`

Локал баталгаажуулалт:
- `mvn test` амжилттай
- `mvn site` амжилттай

## 4. Ажиллуулах заавар
Build:

```bash
mvn package
```

Жишээ ажиллуулах:

```bash
./flashcard my-cards.txt --order recent-mistakes-first --repetitions 2
```

```bash
./flashcard --help
```
