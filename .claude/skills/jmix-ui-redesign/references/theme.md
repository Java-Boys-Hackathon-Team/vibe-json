# Тема и дизайн-токены

## Устройство темы

- Тема лежит в `src/main/frontend/themes/<имя>/`, подключается аннотацией
  `@Theme("<имя>")` на классе приложения и наследует `jmix-lumo` через
  `theme.json`.
- `styles.css` только импортирует файлы:

  ```css
  @import url('<имя>.css');          /* токены и общие элементы */
  @import url('view/main-view.css');
  @import url('view/login-view.css');
  @import url('view/<экран>.css');
  ```

- Старые шаблонные файлы (спиннеры, неиспользуемые темы в корневом `frontend/`)
  лучше удалить, чтобы не путаться, какая тема рабочая.

## Токены

Стартовый файл: `assets/theme-tokens.css`. Скопируй его в `<имя>.css` и поменяй
префикс и палитру под бренд.

- Свои переменные (`--app-bg`, `--app-surface`, `--app-text`, `--app-border`,
  `--app-shadow-*`, `--app-radius-*`, `--app-gradient`) задаются на `html`.
- Переменные Lumo переопределяются там же: `--lumo-primary-color`,
  `--lumo-primary-color-50pct`, `--lumo-primary-color-10pct`,
  `--lumo-primary-text-color`, `--lumo-base-color`, `--lumo-body-text-color`,
  `--lumo-font-family`, `--lumo-border-radius-*`. Тогда кнопки, поля, таблицы и
  диалоги Vaadin и Jmix сами становятся «фирменными».
- Тёмная схема - те же переменные под `html[theme~="dark"]`. В стилях экранов
  цвета пишутся только через переменные.
- Для полупрозрачных вариантов цвета удобен `color-mix(in srgb, var(--c) 15%, transparent)`.

## Переключение темы

- `ThemeUtils.applyLightTheme()`, `applyDarkTheme()`, `applySystemTheme()` из
  Jmix меняют атрибут `theme` на `<html>` и запоминают выбор.
- Текущую тему можно узнать из браузера и переключить одной кнопкой:

  ```java
  UI.getCurrent().getPage()
          .executeJs("return document.documentElement.getAttribute('theme') || ''")
          .then(String.class, theme -> {
              if (theme.contains("dark")) ThemeUtils.applyLightTheme();
              else ThemeUtils.applyDarkTheme();
          });
  ```

## Внутренности компонентов

Компоненты Vaadin стилизуются снаружи через `::part(...)`:

| Что | Селектор |
|---|---|
| верхняя панель и меню | `vaadin-app-layout::part(navbar)`, `::part(drawer)` |
| карточка формы входа | `vaadin-login-form-wrapper::part(form)`, `::part(form-title)` |
| поле ввода | `vaadin-text-field::part(input-field)` |
| заголовок таблицы | `vaadin-grid::part(header-cell)` |
| оверлей диалога | `vaadin-dialog-overlay::part(overlay)` |
| разделитель панелей | `vaadin-split-layout::part(splitter)`, `::part(handle)` |

Ширина бокового меню - переменная `--vaadin-app-layout-drawer-width`.

## Шрифты, фавикон, метатеги

Подключаются в `AppShellConfigurator.configurePage(AppShellSettings settings)`:

```java
settings.addFavIcon("icon", "icons/logo.svg", "any");
settings.addMetaTag("theme-color", "#6366f1");
settings.addLink("preconnect", "https://fonts.gstatic.com");
settings.addLink("stylesheet", "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap");
```

В `--lumo-font-family` всегда оставляй запасной системный шрифт: CDN может быть
недоступен (закрытый контур, прокси).

Статика (логотип, иллюстрации) кладётся в
`src/main/resources/META-INF/resources/icons/` и доступна по пути `icons/...`.
Логотип удобно сделать SVG: он же фавикон, аватар бота и элемент меню.

## Мелочи, которые делают интерфейс приятным

- градиент на основной кнопке (`vaadin-button[theme~="primary"]`) со сдвигом
  `background-position` при наведении и цветной тенью;
- мягкие многослойные тени и скругления 10-18 px;
- тонкие полосы прокрутки (`scrollbar-width: thin` и `::-webkit-scrollbar`);
- анимации появления 0,3-0,5 с (`opacity` и `translateY`), с задержкой по порядку;
- точечная сетка (`radial-gradient` 1 px, шаг 18-22 px) на фоне пустых областей
  и холстов;
- полупрозрачная верхняя панель с `backdrop-filter: blur(...)`;
- аватары пользователей в фирменном градиенте вместо случайных цветов.
