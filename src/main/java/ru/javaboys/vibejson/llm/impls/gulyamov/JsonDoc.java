package ru.javaboys.vibejson.llm.impls.gulyamov;

import java.util.HashMap;
import java.util.Map;

public abstract class JsonDoc {

    public static Map<String, String> ACTIVITY_DESCRIPTION_LIST = buildActivities();
    public static Map<String, String> ACTIVITY_EXAMPLE_LIST = buildActivityExample();
    public static Map<String, String> ACTIVITY_DOCUMENTATION_LIST = buildActivityDoc();

    private static Map<String, String> buildActivities() {
        ACTIVITY_DESCRIPTION_LIST = new HashMap<>();

        ACTIVITY_DESCRIPTION_LIST.put("await_for_message", "await for message чаще всего используется для асинхронного взаимодействия, когда после вызова REST ожидается callback от внешней системы");
        ACTIVITY_DESCRIPTION_LIST.put("rest_call", "HTTP‑вызов к внешнему REST‑сервису");
        ACTIVITY_DESCRIPTION_LIST.put("db_call", "Вызов функции или выборка из базы данных");
        ACTIVITY_DESCRIPTION_LIST.put("send_to_rabbitmq", "Отправка сообщения в очередь RabbitMQ");
        ACTIVITY_DESCRIPTION_LIST.put("send_to_kafka", "Отправка сообщения в тему Apache Kafka");
        ACTIVITY_DESCRIPTION_LIST.put("send_to_s3", "Запись данных в хранилище S3");
        ACTIVITY_DESCRIPTION_LIST.put("send_to_sap", "Отправка IDoc в SAP");
        ACTIVITY_DESCRIPTION_LIST.put("xslt_transform", "XSLT‑преобразование XML");
        ACTIVITY_DESCRIPTION_LIST.put("transform", "Трансформация данных (json↔xml и т.п.)");
        ACTIVITY_DESCRIPTION_LIST.put("inject", "Вставка/инициализация константных данных");
        ACTIVITY_DESCRIPTION_LIST.put("switch", "Условный переход (if/else‑логика)");
        ACTIVITY_DESCRIPTION_LIST.put("parallel", "Параллельное выполнение нескольких веток");
        ACTIVITY_DESCRIPTION_LIST.put("timer", "Пауза на заданное время (таймер)");

        return ACTIVITY_DESCRIPTION_LIST;
    }


    private static Map<String, String> buildActivityDoc() {
        ACTIVITY_DOCUMENTATION_LIST = new HashMap<>();

        ACTIVITY_DOCUMENTATION_LIST.put("await_for_message", """
                Документация по await_for_message:
                • Назначение:
                  – Ожидание callback‑сообщения от внешней системы после REST‑вызова.
                • Callback‑формат:
                  {
                    "businessKey": "<ид процесса>",
                    "messageName": "<ожидаемое имя>",
                    "variables": { … } 
                  }
                • Сохраняется автоматически в переменную `wf.consumedMessages`.
                • Структура Activity:
                  – `id` (String255) — уникальный ID шага, обязателен.
                  – `transition` (String255) — ID следующего шага или `null`, обязателен.
                  – `type` = `"workflow_call"`.
                  – `workflowCall.workflowDef.type` = `"await_for_message"`.
                  – `workflowCall.workflowDef.details.awaitForMessageConfig.messageName` — имя ожидаемого сообщения, обязателен.
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("rest_call", """
                Примитив rest_call

                Составной Activity, выполняющий REST-вызов внешнего сервиса.

                Важно! В рамках одного БП не должно быть activity с одинаковым ИД.

                Параметры:
                - id (String255)  
                  ИД activity. Обязательное.
                - description (String255)  
                  Описание шага. Необязательное.
                - transition (String255)  
                  ИД следующего activity. Если БП или ветка заканчивается на данном activity, то значение будет "null". Обязательное.
                - type (String255)  
                  Всегда должно быть "workflow_call". Обязательное.
                - workflowCall (Составной WorkflowCall)  
                  Описание activity с типом workflow_call. Обязательное.
                - outputFilter (String255)  
                  Описывает трансформацию исходящих данных после завершения activity: используется для переименования пришедших параметров и объявления переменных (эти переменные нужно явно указывать в outputValidateSchema). Если outputFilter не указан, движок всё равно добавит выходные переменные в общий скоуп и передаст дальше. Необязательное.
                  
                  Составной WorkflowCall
                  
                      Параметры:
                      - args (JSONObject) \s
                        Аргументы на вход activity. Необязательное.
                      - retryConfig (Составной retryConfig) \s
                        Политика повторных попыток при неудаче. Необязательное.
                      - workflowDef (SimpleWorkflowDefinition) \s
                        Описание вызываемого подпроцесса. Обязательное, если нет workflowRef.
                        
                     Составной retryConfig
                 
                     Параметры:
                     - initialInterval (String256, ISO 8601 duration) \s
                       Интервал до первого повтора (по умолчанию 1 с). \s
                       Примеры: \s
                       • "PT20.345S" — 20.345 сек \s
                       • "PT15M" — 15 мин \s
                       • "PT10H" — 10 ч \s
                       • "P2D" — 2 дн \s
                       • "P2DT3H4M" — 2 дн 3 ч 4 мин \s
                       Необязательное.
                     - maxInterval (String256, ISO 8601 duration) \s
                       Максимальный интервал между попытками (должен быть ≥ initialInterval), иначе не ограничен. \s
                       Примеры те же, что и для initialInterval. \s
                       Необязательное.
                     - maxAttempts (Int) \s
                       Максимальное число повторов. Необязательное.
                     - backoffCoefficient (float) \s
                       Коэффициент увеличения интервала после каждого повтора (минимум 1.0, по умолчанию2.0). Может быть дробным. Необязательное.
                     Составной SimpleWorkflowDefinition
                     
                         Параметры:
                         - type (String255) \s
                           Тип activity. Должно быть "rest_call". Обязательное.
                         - details (Составной details) \s
                           Описание деталей вызова. Обязательное.
                           
                   Составной details
                   
                       Параметры:
                       - inputValidateSchema (JSONObject) \s
                         Стандартная JSON Schema (https://www.jsonschemavalidator.net/) для проверки входящих переменных. Необязательное.
                       - outputValidateSchema (JSONObject) \s
                         Стандартная JSON Schema для проверки исходящих переменных. Необязательное.
                       - restCallConfig (Составной restCallConfig) \s
                         Описание параметров REST-вызова. Обязательное.
                         
                   Составной restCallConfig
                   
                       Параметры:
                       - resultHandlers (List<Predicate>) \s
                         Условия успешного вызова. Несколько predicate объединяются через ИЛИ. \s
                         Пример:
                         [
                           {
                             "predicate": { "respCode": 200 },
                             "respValueAnyOf": [
                               { "jsonPath": "jp{body.status.statusId}", "values": ["executor-reject","rejected","approvement-reject","approvement-deadline","revoke"] }
                             ]
                           },
                           {
                             "predicate": { "respCode": 205 },
                             "respValueAnyOf": [
                               { "jsonPath": "jp{body.status.status.statusId}", "values": ["executor-reject","rejected","approvement-reject","approvement-deadline","revoke"] }
                             ]
                           }
                         ]
                         Необязательное.
                       - restCallTemplateRef (RestCallTemplateRef) \s
                         Ссылка на шаблон REST-запроса из справочника. Обязательное, если нет restCallTemplateDef.
                       - restCallTemplateDef (RestCallTemplateDef) \s
                         Параметры вызова REST-запроса. Обязательное, если нет restCallTemplateRef
                         
                 Составной predicate
                 
                     Параметры:
                     - respCode (Int) \s
                       Код ответа. Обязательный, если не задано ни respCodeInterval, ни respCodes.
                     - respCodes (List<Int>) \s
                       Список кодов ответов. Обязательный, если не задано ни respCodeInterval, ни respCode.
                     - respCodeInterval (respCodeInterval) \s
                       Интервал ответов. Обязательный, если не задан respCode.
                     - respValueAnyOf (List<pathValueValidation>) \s
                       Описание условий по значениям полей ответа. Несколько экземпляров pathValueValidation объединяются через ИЛИ. \s
                       Пример:
                       [
                         {
                           "jsonPath": "jp{body.prop1}",
                           "values": ["val"],
                           "and": {
                             "jsonPath": "jp{body.prop2}",
                             "values": ["val2"]
                           }
                         }
                       ]
                       
                      Составной respCodeInterval
                  
                      Параметры:
                      - from (Int) \s
                        Код ответа, с которого начинается интервал. Необязательное.
                      - to (Int) \s
                        Код ответа, на котором заканчивается интервал. Необязательное.
                     Составной pathValueValidation
                 
                     Параметры:
                     - jsonPath (String255) \s
                       Путь к переменной. Обязательное.
                     - values (List<Object>) \s
                       Значения переменной. Типы элементов: String255, Boolean, Int или Double. Обязательное.
                     - and (pathValueValidation) \s
                       Дополнительный блок И для ещё одной переменной. Необязательное.
                      Составной restCallTemplateRef
                  
                      Параметры:
                      - id (String‑UUID) \s
                        ID шаблона REST‑запроса из справочника. Обязательное, если нет name.
                      - name (String255) \s
                        Наименование шаблона. Обязательное, если нет id.
                      - version (Int) \s
                        Версия шаблона. Необязательное.
                      - tenantId (String255) \s
                        ID системы, использующей шаблон. По умолчанию "default". Необязательное.
                Составной restCallTemplateDef
                                
                    Параметры:
                    - method (String255) \s
                      Тип метода подключения (POST, PUT и т.д.). Обязательный, если не указан curl.
                    - url (String255) \s
                      URL подключения. Обязательный, если не указан curl.
                    - bodyTemplate (String255) \s
                      Тело запроса. Необязательное.
                    - headers (Составной headers) \s
                      Заголовки запроса. Обязательное, если не указан curl.
                    - curl (String255) \s
                      Экранированный curl-запрос. Обязательный, если не указаны method, url и headers. \s
                      Важно! Параметры выгружаемого curl должны быть: \s
                      • Use long form options – F \s
                      • Line continuation character – \\\\ \s
                      • Quote Type-SINGLE \s
                      • Set request timeout–0 \s
                      • Follow redirects–T \s
                      • Trim request body fields–F \s
                      • Use Silent Mode–F
                    - authDef (Составной authDef) \s
                      Параметры авторизации. Обязательное.   
                      
                    оставной headers
                    
                        Параметры:
                        - <headers> (String255) \s
                          Одна запись заголовка запроса вида "Name: Value". \s
                          Обязательное, если не указан curl.
                          
                    Составной authDef
                    
                        Параметры:
                        - type (String255) \s
                          Тип авторизации, один из "basic", "oauth2". Обязательное.
                        - basic (Составной basic) \s
                          Параметры для basic-авторизации. Обязательное, если type == "basic".
                        - oauth2 (Составной oauth2) \s
                          Параметры для oauth2-авторизации. Обязательное, если type == "oauth2". 
                    Составной basic
                    
                        Параметры:
                        - login (String255) \s
                          Логин. Обязательный, если type == "basic".
                        - password (String255) \s
                          Пароль. Обязательный, если type == "basic".                  
                        Составной oauth2
                    
                        Параметры:
                        - issuerLocation (String255) \s
                          URL для получения токена. Обязательный, если type == "oauth2".
                        - clientId (String255) \s
                          ID клиента. Обязательный, если type == "oauth2".
                        - clientSecret (String255) \s
                          Секрет клиента. Обязательный, если type == "oauth2".
                        - grantType (String255) \s
                          Тип выдачи (на данный момент только "client_credentials"). Обязательный, если type == "oauth2".
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("workflow_call", """
                Составной WorkflowCall

                Параметры:
                - args (JSONObject)  
                  Аргументы на вход activity.  
                  Важно! При вызове процедур или функций в селекте используются переменные из схемы – их обращения описываются здесь, в args.  
                  Необязательное.
                - retryConfig (Составной retryConfig)  
                  Политика повторных попыток при неудаче. Необязательное.
                - workflowDef (SimpleWorkflowDefinition)  
                  Описание вызываемого подпроцесса.  
                  Обязательное, если не указано workflowRef.
                  
                Составной SimpleWorkflowDefinition
                                
                    Параметры:
                    - type (String255) \s
                      Тип activity. Должно быть "db_call". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное.
                Составной details
                               
                    Параметры:
                    - databaseCallConfig (databaseCallConfig) \s
                      Описание деталей вызова. Обязательное.
                Составной databaseCallConfig
                                
                    Параметры:
                    - databaseCallRef (databaseCallRef) \s
                      Параметры шаблона подключения к БД из справочника. \s
                      Обязательный, если нет databaseCallDef.
                    - databaseCallDef (databaseCallDef) \s
                      Параметры прямого подключения к БД. \s
                      Обязательный, если нет databaseCallRef.
                    - dataSourceId (String255) \s
                      ID шаблона dataSource из справочника. \s
                      Обязательный, если нет dataSourceDef.
                    - dataSourceDef (dataSourceDef) \s
                      Параметры для авторизации на стороне dataSource. \s
                      Обязательный, если нет dataSourceId.
                   Составной databaseCallRef
                               
                   Параметры:
                   - id (String‑UUID) \s
                     ID шаблона параметров подключения к БД из справочника. \s
                     Обязательный, если нет name.
                   - name (String255) \s
                     Наименование шаблона. \s
                     Обязательный, если нет id.
                   - version (Int) \s
                     Версия шаблона. Необязательное.
                   - tenantId (String255) \s
                     ID системы, использующей шаблон (по умолчанию "default"). Необязательное.
                    Составной databaseCallDef
                                
                    Параметры:
                    - type (String255) \s
                      Тип подключения. Возможные значения: "function", "select", "procedure". \s
                      Обязательный.
                    - sql (JSONString) \s
                      Описание запроса в формате SELECT. \s
                      Важно! Для PostgreSQL запрос должен заканчиваться точкой с запятой `";"`, для остальных СУБД — без. \s
                      Обязательный, если type == "select".
                    - schema (String255) \s
                      Схема подключения. Обязательный, если type == "function".
                    - catalog (String255) \s
                      Каталог подключения. Необязательный.
                    - functionName (String255) \s
                      Имя функции. Обязательный, если type == "function".
                    - inParameters (JSONString) \s
                      Входные параметры для функций и процедур, например \s
                      `{ "_doc_num": "VARCHAR", "_sta_con": "VARCHAR", "_sta_txt": "VARCHAR" }`. \s
                      Необязательный, используется при type == "function" или "procedure".
                    - outParameters (JSONString) \s
                      Выходные параметры для функций, например `{ "res": "INTEGER" }`. \s
                      Необязательный, используется при type == "function" или "procedure".
                    Составной dataSourceDef
                                
                    Параметры:
                    - url (String255) \s
                      JDBC‑URL подключения. Обязательный.
                    - className (String255) \s
                      Полное имя JDBC‑драйвера, например: \s
                        • `oracle.jdbc.OracleDriver` — Oracle \s
                        • `org.postgresql.Driver` — PostgreSQL \s
                        • `com.microsoft.sqlserver.jdbc.SQLServerDriver` — MsSQL \s
                      Обязательный.
                    - userName (String255) \s
                      Имя пользователя БД. Обязательный.
                    - userPass (String255) \s
                      Пароль пользователя БД. Обязательный.
                """);
        ACTIVITY_DOCUMENTATION_LIST.put("send_to_rabbitmq", """
                Параметры:
                    - id (String255) \s
                      ИД activity. \s
                      Важно! В рамках одного БП не должно быть двух activities с одинаковым id. \s
                      Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. \s
                      Если БП или ветка завершается здесь — значение будет "null". \s
                      Обязательное.
                    - type (String255) \s
                      Всегда "workflow_call". Обязательное.
                    - workflowCall (WorkflowCall) \s
                      Описание запуска подпроцесса отправки сообщения в RabbitMQ. Обязательное.
                    - outputFilter (String255) \s
                      Трансформация данных после выполнения activity. Необязательное.
                Составной WorkflowCall
                    Параметры:
                    - args (JSONObject) \s
                      Аргументы на вход подпроцесса. \s
                      Важно! При вызове процедур или функций в селекте используются переменные из схемы – их обращения описываются здесь, в args. \s
                      Необязательное.
                    - retryConfig (retryConfig) \s
                      Политика повторных попыток. Необязательное.
                    - workflowDef (SimpleWorkflowDefinition) \s
                      Описание вызываемого подпроцесса (в нашем случае – отправка в RabbitMQ). \s
                      Обязательное, если нет workflowRef.
                    Составной SimpleWorkflowDefinition
                    Параметры:
                    - type (String255) \s
                      Тип activity. Должно быть "send_to_rabbitmq". Обязательное.
                    - details (Составной details) \s
                      Описание деталей отправки. Обязательное.
                    Составной details
                                
                    Параметры:
                    - sendToRabbitmqConfig (Составной sendToRabbitmqConfig) \s
                      Конфигурация отправки в RabbitMQ. Обязательное.
                    Составной sendToRabbitmqConfig
                                
                    Параметры:
                    - connectionRef (Составной connectionRef) \s
                      Ссылка на шаблон параметров подключения. \s
                      Обязательный, если нет connectionDef.
                    - connectionDef (Составной connectionDef) \s
                      Прямые параметры подключения к RabbitMQ. \s
                      Обязательный, если нет connectionRef.
                    - exchange (String255) \s
                      Обменник. Обязательное.
                    - routingKey (String255) \s
                      Ключ маршрутизации. Обязательное.
                    - message (String255) \s
                      Тело сообщения. Обязательное.
                    - messageProperties (Составной messageProperties) \s
                      Параметры заголовков сообщения (contentType, priority и т.п.). Обязательное.
                    Составной connectionRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона подключения из справочника. Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, использующей шаблон. Необязательное, по умолчанию "default".
                    Составной connectionDef
                                
                    Параметры:
                    - userName (String255) \s
                      Имя пользователя. Обязательное.
                    - userPass (String255) \s
                      Пароль пользователя. Обязательное.
                    - addresses (List<String>) \s
                      Массив адресов вида "host:port". Обязательное.
                    - virtualHost (String255) \s
                      Виртуальный хост. Обязательное.
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("send_to_kafka", """
                               
                   Параметры:
                   - id (String255) \s
                     ИД activity. В рамках одного БП должен быть уникальным. Обязательное.
                   - description (String255) \s
                     Описание шага. Необязательное.
                   - transition (String255) \s
                     ИД следующего activity. Если ветка заканчивается здесь – "null". Обязательное.
                   - type (String255) \s
                     Всегда "workflow_call". Обязательное.
                   - workflowCall (WorkflowCall) \s
                     Описание запуска подпроцесса отправки в Kafka. Обязательное.
                   - outputFilter (String255) \s
                     Трансформация выходных данных после выполнения. Необязательное.
                Составной SimpleWorkflowDefinition
                               
                    Параметры:
                    - type (String255) \s
                      Должно быть "send_to_kafka". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное. 
                    Составной details
                                
                    Параметры:
                    - sendToKafkaConfig (sendToKafkaConfig) \s
                      Конфигурация отправки в Kafka. Обязательное.
                    Составной sendToKafkaConfig
                                
                    Параметры:
                    - connectionRef (connectionRef) \s
                      Ссылка на шаблон параметров подключения. \s
                      Обязательный, если нет connectionDef.
                    - connectionDef (connectionDef) \s
                      Прямые параметры подключения к Kafka. \s
                      Обязательный, если нет connectionRef.
                    - topic (String255) \s
                      Топик Kafka для отправки. Обязательное.
                    - key (String255) \s
                      Ключ сообщения. Необязательное.
                    - message (String255) \s
                      Тело сообщения. Обязательное.
                    // При необходимости можно добавить дополнительные свойства (partition, timestamp и т.п.).
                    Составной connectionRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона подключения из справочника. Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, использующей шаблон. По умолчанию "default". Необязательное.
                    Составной message
                                
                    Параметры:
                    - payload (String255) \s
                      Тело сообщения. Обязательное.
                    - connectionDef (connectionDef) \s
                      Параметры подключения (bootstrapServers + authDef). Обязательное.
                    Составной connectionDef
                                
                    Параметры:
                    - bootstrapServers (String255) \s
                      Адрес(а) брокера(ов) Kafka в формате "host:port". Обязательное.
                    - authDef (authDef) \s
                      Параметры авторизации (SASL или TLS). Необязательное.
                    Составной authDef
                                
                    Параметры:
                    - type (String255) \s
                      Тип авторизации: "SASL" или "TLS". Обязательное, если используется защита.
                    - sasl (sasl) \s
                      Конфигурация SASL. Обязательное, если type == "SASL".
                    - tls (tls) \s
                      Конфигурация mTLS. Обязательное, если type == "TLS".
                    Составной sasl
                                
                    Параметры:
                    - protocol (String255) \s
                      Протокол подключения: "SASL_SSL" или "SASL_PLAINTEXT". Обязательное.
                    - mechanism (String255) \s
                      Механизм SASL: \s
                        • "OAUTHBEARER" – только для SASL_PLAINTEXT \s
                        • "SCRAM-SHA-512" – для SASL_SSL и SASL_PLAINTEXT \s
                      Обязательное.
                    - username (String255) \s
                      Логин. Обязательное.
                    - password (String255) \s
                      Пароль. Обязательное.
                    - sslDef (Составной sslDef) \s
                      Сертификаты для TLS при SCRAM‑SHA‑512. Обязательный для SCRAM‑SHA‑512.
                    - tokenUrl (String255) \s
                      URL для получения токена при OAUTHBEARER. Обязательный для OAUTHBEARER.
                    Составной sslDef
                                
                    Параметры:
                    - trustStoreType (String255) \s
                      Тип хранилища сертификатов (например, PEM). Необязательное.
                    - trustStoreCertificates (String) \s
                      Тело доверенных сертификатов. \s
                      Важно! Начинается с "-----BEGIN CERTIFICATE-----\\\\r\\\\n" и заканчивается "\\\\r\\\\n-----END CERTIFICATE-----\\\\r\\\\n". \s
                      Необязательное.
                    Составной tls
                                
                    Параметры:
                    - keyStoreCertificates (String) \s
                      Публичный ключ клиента (PEM или base64). Обязательное для TLS.
                    - keyStoreKey (String) \s
                      Приватный ключ клиента (PEM или base64). Обязательное для TLS.
                    - trustStoreCertificates (String) \s
                      Корневой (CA) сертификат (PEM или base64). Обязательное для TLS.
                    - trustStoreType (String255) \s
                      Тип хранилища сертификатов (например, PEM). Обязательное для TLS.     
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("send_to_s3", """
                    Параметры:
                    - id (String255) \s
                      ИД activity. Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. Если ветка завершается здесь – "null". Обязательное.
                    - type (String255) \s
                      Всегда "workflow_call". Обязательное.
                    - workflowCall (WorkflowCall) \s
                      Описание activity с типом workflow_call. Обязательное.
                    - outputFilter (String255) \s
                      Трансформация исходящих данных после выполнения activity. Необязательное.
                    Составной WorkflowCall
                                
                    Параметры:
                    - args (JSONObject) \s
                      Аргументы на вход activity. Необязательное.
                    - workflowDef (SimpleWorkflowDefinition) \s
                      Описание вызываемого подпроцесса. Обязательное, если нет workflowRef.
                    Составной SimpleWorkflowDefinition
                                
                    Параметры:
                    - type (String255) \s
                      Должно быть "send_to_s3". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное.
                    Составной details
                                
                    Параметры:
                    - sendToS3Config (sendToS3Config) \s
                      Описание деталей вызова. Обязательное.
                Составной sendToS3Config
                                
                    Параметры:
                    - connectionRef (connectionRef) \s
                      Ссылка на шаблон параметров подключения. \s
                      Обязательный, если нет connectionDef.
                    - connectionDef (connectionDef) \s
                      Прямые параметры подключения к S3. \s
                      Обязательный, если нет connectionRef.
                    - bucket (String255) \s
                      Имя бакета. Обязательное.
                    - region (String255) \s
                      Регион. Обязательное.
                    - s3File (s3File) \s
                      Параметры файла. Обязательное      
                    Составной connectionRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона подключения из справочника. \s
                      Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. \s
                      Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, использующей шаблон (по умолчанию "default"). Необязательное.
                    Составной connectionDef
                                
                    Параметры:
                    - endpoint (String255) \s
                      Адрес подключения. Обязательное.
                    - authDef (authDef) \s
                      Параметры авторизации. Обязательное.
                Составной authDef
                                
                    Параметры:
                    - type (String255) \s
                      Тип авторизации. Обязательное.
                    - accessKey (String255) \s
                      Ключ доступа. Обязательное.
                    - secretKey (String255) \s
                      Секрет. Обязательное  
                    Составной s3File
                                
                    Параметры:
                    - filePath (String255) \s
                      Название файла с расширением. Обязательное.
                    - content (String255) \s
                      Переменная, из которой будут взяты данные для тела файла. Обязательное.
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("send_to_sap", """
                    Примитив send_to_sap
                                
                    Составной Activity
                                
                    Параметры:
                    - id (String255) \s
                      ИД activity. \s
                      Важно! В рамках одного БП не должно быть activity с одинаковым id. \s
                      Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. \s
                      Если БП или ветка завершается здесь — значение будет "null". \s
                      Обязательное.
                    - type (String255) \s
                      Всегда "workflow_call". Обязательное.
                    - workflowCall (WorkflowCall) \s
                      Описание activity с типом workflow_call. Обязательное.
                    - outputFilter (String255) \s
                      Трансформация выходных данных после выполнения activity. Необязательное.
                    Составной WorkflowCall
                                
                    Параметры:
                    - args (JSONObject) \s
                      Аргументы на вход activity. Обязательное.
                    - workflowDef (SimpleWorkflowDefinition) \s
                      Описание вызываемого подпроцесса. Обязательное, если нет workflowRef.
                    Составной SimpleWorkflowDefinition
                                
                    Параметры:
                    - type (String255) \s
                      Должно быть "send_to_sap". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное.
                    Составной details
                                
                    Параметры:
                    - sendToSapConfig (sendToSapConfig) \s
                      Описание деталей вызова. Обязательное.
                    Составной sendToSapConfig
                                
                    Параметры:
                    - connectionRef (connectionRef) \s
                      Ссылка на шаблон с параметрами подключения. \s
                      Обязательный, если нет connectionDef.
                    - connectionDef (connectionDef) \s
                      Прямые параметры подключения. \s
                      Обязательный, если нет connectionRef.
                    - idoc (String255) \s
                      Параметры отправляемого документа. Обязательный.
                    Составной connectionRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона подключения к SAP из справочника. Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, использующей шаблон (по умолчанию "default"). Необязательное.
                    Составной connectionDef
                                
                    Параметры:
                    - props (props) \s
                      Параметры подключения к SAP. Все поля обязательны.
                    Составной props
                                
                    Параметры:
                    // Здесь перечислите все поля подключения к SAP: хост, порт, учетные данные, пути и т.п. \s
                    Все параметры обязательны.
                    Составной props
                                
                    Параметры:
                    - jco.client.lang (String255) \s
                      Язык клиента. Обязательное.
                    - jco.client.passwd (String255) \s
                      Пароль. Обязательное.
                    - jco.client.user (String255) \s
                      Логин. Обязательное.
                    - jco.client.sysnr (Int) \s
                      Номер SAP‑системы. Обязательное.
                    - jco.destination.pool_capacity (Int) \s
                      Максимальное число подключений в пуле для destination. Обязательное.
                    - jco.destination.peak_limit (Int) \s
                      Максимальное число одновременных подключений для destination. Обязательное.
                    - jco.client.client (Int) \s
                      Номер клиента в SAP‑системе. Обязательное.
                    - jco.client.ashost (String255) \s
                      Хост. Обязательное.
                    Составной idoc
                                
                    Параметры:
                    - xml (String255) \s
                      Тело документа в формате XML. \s
                      Можно указать переменную, где хранится документ в БП, например: \s
                      "xml": "jp{sap_xml}" \s
                      Обязательное.
                                
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("xslt_transform", """
                               
                   Параметры:
                   - id (String255) \s
                     ИД activity. Обязательное.
                   - description (String255) \s
                     Описание шага. Необязательное.
                   - transition (String255) \s
                     ИД следующего activity. Если ветка завершается здесь — "null". Обязательное.
                   - type (String255) \s
                     Всегда "workflow_call". Обязательное.
                   - workflowCall (WorkflowCall) \s
                     Описание activity с типом workflow_call. Обязательное.
                   - outputFilter (String255) \s
                     Трансформация выходных данных после выполнения activity. Необязательное.
                    Составной WorkflowCall
                                
                    Параметры:
                    - args (JSONObject) \s
                      Аргументы на вход activity. Обязательное.
                    - workflowDef (workflowDef) \s
                      Описание вызываемого подпроцесса. Обязательное, если нет workflowRef.
                    Составной workflowDef
                                
                    Параметры:
                    - type (String255) \s
                      Должно быть "xslt_transform". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное.
                    Составной details
                                
                    Параметры:
                    - xsltTransformConfig (xsltTransformConfig) \s
                      Описание деталей вызова. Обязательное.
                    Составной xsltTransformConfig
                                
                    Параметры:
                    // Здесь перечислите поля конфигурации трансформации (путь к шаблону XSL, входные/выходные переменные и т.п.).
                    Составной xsltTemplateRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона параметров трансформации из справочника. \s
                      Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. \s
                      Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, которая использует шаблон (по умолчанию "default"). Необязательное.
                    Составной xsltTransformTargetRef
                                
                    Параметры:
                    - id (String‑UUID) \s
                      ID шаблона целевой трансформации. \s
                      Обязательный, если нет name.
                    - name (String255) \s
                      Наименование шаблона. \s
                      Обязательный, если нет id.
                    - version (Int) \s
                      Версия шаблона. Необязательное.
                    - tenantId (String255) \s
                      ID системы, которая использует шаблон (по умолчанию "default"). Необязательное.
                                
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("transform", """
                    Параметры:
                    - id (String255) \s
                      ИД activity. Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. Если ветка завершается здесь — "null". Обязательное.
                    - type (String255) \s
                      Всегда "workflow_call". Обязательное.
                    - workflowCall (WorkflowCall) \s
                      Описание activity с типом workflow_call. Обязательное.
                    - outputFilter (String255) \s
                      Трансформация выходных данных после выполнения activity. Необязательное.
                    Составной WorkflowCall
                                
                    Параметры:
                    - args (JSONObject) \s
                      Аргументы на вход activity. Обязательное.
                    - workflowDef (workflowDef) \s
                      Описание вызываемого подпроцесса. Обязательное, если нет workflowRef.
                    Составной workflowDef
                                
                    Параметры:
                    - type (String255) \s
                      Должно быть "transform". Обязательное.
                    - details (details) \s
                      Описание деталей вызова. Обязательное.
                    Составной details
                                
                    Параметры:
                    - transformConfig (transformConfig) \s
                      Описание деталей трансформации. Обязательное.
                    Составной transformConfig
                                
                    Параметры:
                    - type (String255) \s
                      Тип трансформации. Доступно два варианта: \s
                        • "xml_to_json" \s
                        • "json_to_xml" \s
                      Обязательное.
                    - target (target) \s
                      Цель трансформации. Например: \s
                        "target": { "idoc_json": "jp{xslTransformResult}" } \s
                      В этом примере переменная idoc_json сохранится под именем xslTransformResult. \s
                      Обязательное.
                                
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("inject", """
                Параметры:
                    - id (String255) \s
                      ИД activity. \s
                      Важно! В рамках одного БП не должно быть activity с одинаковым id. \s
                      Обязательное.
                    - description (String255) \s
                      Описание шага. \s
                      Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. Если БП или ветка заканчивается здесь – "null". \s
                      Обязательное.
                    - type (String255) \s
                      Всегда "inject". Обязательное.
                    - injectData (injectData) \s
                      Описание атрибутов для вставки. Например: \s
                        "injectData": { "try_count": 0 } \s
                      Можно задавать константы, использовать переменные из данных схемы и lua‑преобразования. \s
                      Обязательное.
                    Составной injectData
                                
                    Параметры:
                    // Здесь перечислите ключи и значения, которые нужно вставить. \s
                    // Ключ — имя переменной, значение — константа или выражение. \s
                    // Пример:
                    //   "try_count": 0
                      
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("switch", """
                    Параметры:
                    - id (String255) \s
                      ИД activity. Важно! В рамках одного БП не должно быть activity с одинаковым id. Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - type (String255) \s
                      Всегда "switch". Обязательное.
                    - dataConditions (List<dataConditions>) \s
                      Описание проверяемых условий и действий при их выполнении. \s
                     \s
                      Важно! Порядок условий имеет значение: при выполнении первого true переход идет по его transition, даже если подходят и другие. \s
                      Обязательное, если type == "switch".
                    - defaultTransition (DefaultDataTransition) \s
                      Описание поведения, если все conditions = false. Обязательное, если type == "switch".
                    Составной dataConditions
                                
                    Параметры:
                    - condition (String400) \s
                      Скрипт условия в формате Lua. \s
                      Например: lua{return next(wf.vars.email200.ok) ~= nil}lua \s
                      Обязательное.
                    - conditionDescription (String400) \s
                      Описание условия. Необязательное.
                    - transition (String400) \s
                      ИД activity для перехода, если condition = true. \s
                      Если пусто, ветка завершится при условии = true. Необязательное.
                    Составной DefaultDataTransition
                                
                    Параметры:
                    - transition (String255) \s
                      ИД activity для перехода, если все conditions = false. \s
                      Если пусто, ветка завершится при all false. Обязательное.
                    - conditionDescription (String400) \s
                      Описание перехода. Необязательное.
                                
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("parallel", """
                    Параметры:
                    - id (String255) \s
                      ИД activity. \s
                      Важно! В рамках одного БП не должно быть двух activities с одинаковым id. \s
                      Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. \s
                      Если ветка завершается здесь — значение "null". \s
                      Обязательное.
                    - type (String255) \s
                      Всегда "parallel". Обязательное.
                    - branches (List<String>) \s
                      Список id activity данного WF, которые будут выполняться параллельно. \s
                      Пример:
                        [
                          "AR-1-ApiStatusCREATING",
                          "AR-2-APIFirewallStatusCREATING"
                        ] \s
                      Обязательное, если type == "parallel".
                    - completionType (String255) \s
                      Тип завершения parallel-activity: \s
                        • anyOf — завершение после окончании хотя бы одного из указанных действий \s
                        • allOf — завершение только после окончания всех указанных действий \s
                      Обязательное, если type == "parallel".
                                
                """);

        ACTIVITY_DOCUMENTATION_LIST.put("", """
                    Параметры:
                    - id (String255) \s
                      ИД activity. Обязательное.
                    - description (String255) \s
                      Описание шага. Необязательное.
                    - transition (String255) \s
                      ИД следующего activity. Если ветка завершается здесь — "null". Обязательное.
                    - type (String255) \s
                      Всегда "timer". Обязательное.
                    - timerDuration (String256, ISO 8601 duration) \s
                      Таймер до перехода к следующему activity. \s
                      Примеры: \s
                      • "PT20.345S" — 20.345 секунд \s
                      • "PT15M" — 15 минут \s
                      • "PT10H" — 10 часов \s
                      • "P2D" — 2 дня \s
                      • "P2DT3H4M" — 2 дн, 3 ч, 4 мин \s
                      Обязательное.
                                
                """);

        return ACTIVITY_DOCUMENTATION_LIST;
    }

    private static Map<String, String> buildActivityExample() {
        ACTIVITY_EXAMPLE_LIST = new HashMap<>();

        ACTIVITY_EXAMPLE_LIST.put("rest_call", """
                [
                  {
                    "id": "activity-2",
                    "type": "workflow_call",
                    "description": "Получение статуса",
                    "workflowCall": {
                      "workflowDef": {
                        "type": "rest_call",
                        "details": {
                          "restCallConfig": {
                            "restCallTemplateDef": {
                              "curl": "curl -L 'https://workflow.test.ru/api/v1/wf/search' -H 'accept: */*' -H 'Content-Type: application/json' -d '{\"name\":\"MaxActivitiShema\",\"offset\":0,\"limit\":25}'",
                              "authDef": {
                                "type": "oauth2",
                                "oauth2": {
                                  "issuerLocation": "https://isso.mts.ru/auth/realms/mts",
                                  "clientId": "clientId",
                                  "clientSecret": "clientSecret",
                                  "grantType": "client_credentials"
                                }
                              }
                            }
                          },
                          "outputValidateSchema": {
                            "type": "array",
                            "items": [
                              {
                                "type": "object",
                                "properties": {
                                  "id": { "type": "string" },
                                  "type": { "type": "string" },
                                  "name": { "type": "string" },
                                  "description": { "type": "string" },
                                  "tenantId": { "type": "string" },
                                  "createTime": { "type": "string" },
                                  "changeTime": { "type": "string" },
                                  "version": { "type": "integer" },
                                  "status": { "type": "string" },
                                  "ownerLogin": { "type": "string" }
                                },
                                "required": [
                                  "id", "type", "name", "description", "tenantId", "status"
                                ]
                              }
                            ]
                          }
                        }
                      },
                      "retryConfig": {}
                    },
                    "outputFilter": {
                      "sd_body": "jp{$.body}",
                      "sd_status": "jp{$.body[0].status}",
                      "wf_id":   "jp{$.body[0].id}"
                    },
                    "transition": "activity-16"
                  },
                  {
                    "id": "SD-2-SDAPIpublicationStatus",
                    "description": "Запрос статуса заявки Service Desk",
                    "type": "workflow_call",
                    "workflowCall": {
                      "retryConfig": {
                        "initialInterval":    "PT1S",
                        "maxInterval":        "PT5S",
                        "maxAttempts":        10,
                        "backoffCoefficient": 1.2
                      },
                      "failActivityResult": {
                        "retryStates": [
                          "RETRY_STATE_MAXIMUM_ATTEMPTS_REACHED"
                        ],
                        "variables": {
                          "sd_status": "ERROR",
                          "sd_body":   "ERROR"
                        }
                      },
                      "workflowDef": {
                        "type": "rest_call",
                        "details": {
                          "inputValidateSchema": {},
                          "outputValidateSchema": {
                            "type": "object",
                            "required": ["status"],
                            "properties": {
                              "status": { "$ref": "#/definitions/root_status" }
                            },
                            "definitions": {
                              "root_status": {
                                "type": "object",
                                "required": ["status"],
                                "properties": {
                                  "status": { "$ref": "#/definitions/status_status" }
                                }
                              },
                              "status_status": {
                                "type": "object",
                                "required": ["statusId"],
                                "properties": {
                                  "statusId": { "type": "string" }
                                }
                              }
                            }
                          },
                          "restCallConfig": {
                            "resultHandlers": [
                              {
                                "predicate": {
                                  "respCode": 200,
                                  "respValueAnyOf": [
                                    {
                                      "jsonPath": "jp{body.status.status.statusId}",
                                      "values": [
                                        "executor-reject",
                                        "rejected",
                                        "approvement-reject",
                                        "approvement-deadline",
                                        "revoke"
                                      ]
                                    }
                                  ]
                                }
                              }
                            ],
                            "restCallTemplateDef": {
                              "curl": "curl --location --request GET 'http://wiremock:8080/api/herald/v1/application/RA0-solved' \\r\\n--header 'accept: application/json'",
                              "authDef": {
                                "type": "oauth2",
                                "oauth2": {
                                  "issuerLocation": "https://isso.mts.ru/auth/realms/mts",
                                  "clientId":       "clientId",
                                  "clientSecret":   "clientSecret",
                                  "grantType":      "client_credentials"
                                }
                              }
                            }
                          }
                        }
                      }
                    },
                    "outputFilter": {
                      "sd_status": "jp{body.status.status.statusId}",
                      "sd_body":   "jp{body}"
                    },
                    "transition": "Flow"
                  }
                ]
                """);

        ACTIVITY_EXAMPLE_LIST.put("db_call", """
                db_call_oracle_select:
                {
                  "id": "activity-1",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "db_call",
                      "details": {
                        "databaseCallConfig": {
                          "databaseCallDef": {
                            "type": "select",
                            "sql": "select * from table(d.pkg_1c.d001(to_date('29.01.2025 0:37:48','dd.mm.yyyy hh24:mi:ss')))"
                          },
                          "dataSourceDef": {
                            "className": "oracle.jdbc.OracleDriver",
                            "url": "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=11.111.0.111)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=TS)))",
                            "userName": "",
                            "userPass": ""
                          }
                        }
                      }
                    }
                  },
                  "transition": null
                }
                db_call_postgres_procedure:
                {
                  "id": "activity-1",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "db_call",
                      "details": {
                        "databaseCallConfig": {
                          "databaseCallDef": {
                            "type": "procedure",
                            "schema": "bdtest",
                            "inParameters": {
                              "in": "INTEGER"
                            }
                          },
                          "dataSourceDef": {
                            "className": "org.postgresql.Driver",
                            "url": "jdbc:postgresql://111.11.111.111:5432/postgres",
                            "userName": "",
                            "userPass": ""
                          }
                        }
                      }
                    },
                    "args": {
                      "in": "text"
                    },
                    "failActivityResult": {
                      "retryStates": [
                        "RETRY_STATE_MAXIMUM_ATTEMPTS_REACHED"
                      ],
                      "variables": {
                        "status": "ERROR"
                      }
                    }
                  },
                  "transition": null
                }
                db_call_postgres_function:
                {
                  "id": "activity-1",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "db_call",
                      "details": {
                        "databaseCallConfig": {
                          "databaseCallDef": {
                            "type": "function",
                            "schema": "bdtest",
                            "catalog": "functions",
                            "functionName": "functionName",
                            "outParameters": {
                              "result": "INTEGER"
                            },
                            "inParameters": {
                              "in": "INTEGER"
                            }
                          },
                          "dataSourceDef": {
                            "className": "org.postgresql.Driver",
                            "url": "jdbc:postgresql://111.11.111.111:5432/postgres",
                            "userName": "",
                            "userPass": ""
                          }
                        }
                      }
                    },
                    "args": {
                      "in": "text"
                    },
                    "failActivityResult": {
                      "retryStates": [
                        "RETRY_STATE_MAXIMUM_ATTEMPTS_REACHED"
                      ],
                      "variables": {
                        "status": "ERROR"
                      }
                    }
                  },
                  "transition": null
                }
                """);

        ACTIVITY_EXAMPLE_LIST.put("send_to_rabbitmq", """
                {
                  "id": "send_to_rabbit",
                  "type": "workflow_call",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "send_to_rabbitmq",
                      "details": {
                        "sendToRabbitmqConfig": {
                          "connectionDef": {
                            "userName": "userName",
                            "userPass": "userPass",
                            "virtualHost": "/",
                            "addresses": [
                              "10.2.3.1:5672"
                            ]
                          },
                          "exchange": "amq.direct",
                          "routingKey": "rk-to-testq",
                          "message": "jp{xsltTransformResult}",
                          "messageProperties": {
                            "contentType": "application/xml"
                          }
                        }
                      }
                    }
                  }
                }
                """);

        ACTIVITY_EXAMPLE_LIST.put("send_to_kafka", """
                {
                  "id": "activity-13",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "send_to_kafka",
                      "details": {
                        "sendToKafkaConfig": {
                          "topic": "testing",
                          "key": "jp{sd_status}",
                          "message": {
                            "payload": {
                              "messageType": "RequestCreated",
                              "createdAt": "lua{return math.floor(os.time()+0.5)}lua",
                              "version": "1.0.0",
                              "transactionID": "jp{$.transactionID}",
                              "data": {
                                "applicationID": "",
                                "serviceID": "jp{$.data.serviceDeskID}",
                                "eventType": "jp{$.data.eventType}",
                                "error": {
                                  "code": "jp{$.respCode}",
                                  "description": "Неизвестная ошибка"
                                }
                              }
                            }
                          },
                          "connectionDef": {
                            "bootstrapServers": "bootstrap-kafka.ru:443",
                            "authDef": {
                              "type": "SASL",
                              "sasl": {
                                "protocol": "SASL_SSL",
                                "mechanism": "SCRAM-SHA-512",
                                "username": "username",
                                "password": "password",
                                "sslDef": {
                                  "trustStoreType": "PEM",
                                  "trustStoreCertificates": "-----BEGIN CERTIFICATE-----\\nMIIGVjCCBD6gAwIAt\\n-----END CERTIFICATE-----\\n-----BEGIN CERTIFICATE-----\\nMIIGVjCCBD6gAwIAt\\n-----END CERTIFICATE-----"
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "transition": null
                }
                """);

        // Пример для отправки в S3
        ACTIVITY_EXAMPLE_LIST.put("send_to_s3", """
                {
                  "id": "activity-14",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "send_to_s3",
                      "details": {
                        "sendToS3Config": {
                          "bucket": "bucket1",
                          "region": "msk-1",
                          "s3File": {
                            "filePath": "loadTestFile.txt",
                            "content": "jp{sd_body}"
                          },
                          "connectionDef": {
                            "endpoint": "https://s3.ru",
                            "authDef": {
                              "type": "accessKey",
                              "accessKeyAuth": {
                                "accessKey": "accessKey",
                                "secretKey": "secretKey"
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "transition": "activity-15"
                }
                """);

        // Пример для отправки в SAP
        ACTIVITY_EXAMPLE_LIST.put("send_to_sap", """
                {
                  "id": "send_to_sap",
                  "type": "workflow_call",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "send_to_sap",
                      "details": {
                        "sendToSapConfig": {
                          "connectionDef": {
                            "props": {
                              "jco.client.lang":          "EN",
                              "jco.client.passwd":        "passwd",
                              "jco.client.sysnr":         10,
                              "jco.destination.pool_capacity": 3,
                              "jco.destination.peak_limit":     10,
                              "jco.client.client":        400,
                              "jco.client.user":          "user",
                              "jco.client.ashost":        "t.t.ru"
                            }
                          },
                          "idoc": {
                            "xml": "jp{sap_xml}"
                          }
                        }
                      }
                    }
                  }
                }
                """);

        // Пример для xslt_transform
        ACTIVITY_EXAMPLE_LIST.put("xslt_transform", """
                {
                  "id": "xslt_transform",
                  "type": "workflow_call",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "xslt_transform",
                      "details": {
                        "xsltTransformConfig": {
                          "xsltTemplate": "<xsl:stylesheet version=\\"1.0\\" xmlns:xsl=\\"http://www.w3.org/1999/XSL/Transform\\" xmlns:foo=\\"http://www.foo.org/\\" xmlns:bar=\\"http://www.bar.org\\">\\r\\n  <xsl:template match=\\"node()|@*\\">\\r\\n    <xsl:copy>\\r\\n      <xsl:apply-templates select=\\"node()|@*\\"/>\\r\\n    </xsl:copy>\\r\\n  </xsl:template> \\r\\n  <xsl:template match=\\"SENDER_1C/text()\\">RS52.TVR-\\"текст склейки\\"</xsl:template>\\r\\n</xsl:stylesheet>",
                          "xsltTransformTarget": "jp{idoc}"
                        }
                      }
                    }
                  }
                }
                """);

        // Пример для transform с Lua-скриптом
        ACTIVITY_EXAMPLE_LIST.put("transform", """
                {
                  "id": "activity-15",
                  "type": "workflow_call",
                  "description": "",
                  "workflowCall": {
                    "workflowDef": {
                      "type": "transform",
                      "details": {
                        "transformConfig": {
                          "type": "xml_to_json",
                          "target": {
                            "sap_json": "jp{sap_xml}",
                            "mis_json": "jp{mis_xml}"
                          }
                        }
                      }
                    }
                  },
                  "transition": "activity-8",
                  "outputFilter": {
                    "sap_json_modified": "lua{\r\nlocal sap = wf.vars.sap_json; \r\nlocal mis = wf.vars.mis_json;\r\n\r\nfunction findPerson(persons, uid)\r\n for k, v in pairs(persons) do\r\n\tif(v.guidPerson == uid) then\r\n\t return v;\r\n\tend\r\n end\r\nend \r\nif(mis ~=nil) then\r\n local newTranz = mis['ТранзакцияНовая'];\r\n if(newTranz ~=nill) then\r\n for k, v in pairs(mis['ТранзакцияНовая']) do\r\n if(v ~=nill) then\r\n local fiz = v['ФизлицоУИД'];\r\n\t if(fiz ~=nill) the\n\r\n local uid = fiz[''];\r\n local misSnils = v['СНИЛС'][''];\r\n local person = findPerson(sap.Person, uid);\r\n if(person ~=nill) then \r\n\tperson.SNILS = misSnils;\r\n end\t\r\n\t end\r\n\t end\r\n end \r\n end\r\nend\r\nreturn sap;\r\n}lua"
                  }
                }
                """);

        // Пример для inject init
        ACTIVITY_EXAMPLE_LIST.put("inject", """
                {
                  "id": "init",
                  "type": "inject",
                  "description": "Status=INIT",
                  "injectData": {
                    "Status": "200",
                    "id": "jp{json.IDOC.EDI_DC40.DOCNUM}",
                    "email200": "lua{\\r\\nfunction sort_by_last_status(data)\\r\\n    local sorted = {} \\r\\n    local result = {ok={}}\\r\\n    for _, status in ipairs(data.status) do \\r\\n        for _, item in pairs(status.items) do \\r\\n            local destination = item.destination\\r\\n            local status = item.status \\r\\n            if not sorted[destination] or status > sorted[destination] then\\r\\n                sorted[destination] = status\\r\\n            end\\r\\n        end\\r\\n    end \\r\\n    for e, s in pairs(sorted) do \\r\\n        if s > 199 and s < 300 then\\r\\n            table.insert(result.ok, {email=e})\\r\\n        end\\r\\n    end \\r\\n    return result\\r\\nend \\r\\n\\r\\nif wf.consumedMessages == nil then\\r\\n    return {ok={}}\\r\\nend\\r\\nreturn sort_by_last_status(wf.consumedMessages)\\r\\n}lua"
                  },
                  "transition": "SendingStatuses"
                }
                """);

        // Пример для switch
        ACTIVITY_EXAMPLE_LIST.put("switch", """
                {
                  "id": "activity-1",
                  "type": "switch",
                  "description": "",
                  "dataConditions": [
                    {
                      "transition": "activity-3",
                      "condition": "lua{return wf.vars.data.eventType == 'create' and (wf.vars.data.requestType == 'service' or wf.vars.data.requestType == 'access')}lua",
                      "conditionDescription": ""
                    },
                    {
                      "transition": "activity-12",
                      "condition": "lua{return wf.vars.data.eventType == 'create' and wf.vars.data.requestType == 'incident'}lua",
                      "conditionDescription": ""
                    },
                    {
                      "transition": "activity-13",
                      "condition": "lua{return wf.vars.data.eventType == 'create' and wf.vars.data.requestType == 'inquiry'}lua",
                      "conditionDescription": ""
                    }
                  ],
                  "defaultCondition": {
                    "transition": "activity-2",
                    "conditionDescription": "Unrecognized payload"
                  }
                }
                """);

        // Пример для parallel и веток AR-1…AR-4
        ACTIVITY_EXAMPLE_LIST.put("parallel", """
                [
                  {
                    "id": "SendingStatuses",
                    "type": "parallel",
                    "branches": [
                      "AR-1",
                      "AR-2"
                    ],
                    "completionType": "allOf",
                    "transition": "AR-4"
                  },
                  {
                    "id": "AR-1",
                    // …здесь остальные поля для AR-1
                    "transition": null
                  },
                  {
                    "id": "AR-2",
                    // …здесь остальные поля для AR-2
                    "transition": "AR-3"
                  },
                  {
                    "id": "AR-3",
                    // …здесь остальные поля для AR-3
                    "transition": null
                  },
                  {
                    "id": "AR-4"
                    // …здесь остальные поля для AR-4
                  }
                ]
                """);

        // Пример для timer
        ACTIVITY_EXAMPLE_LIST.put("timer", """
                {
                  "id": "timer0",
                  "type": "timer",
                  "description": "timer 1 секунда",
                  "timerDuration": "PT1S",
                  "transition": "SD-2-SDAPIpublicationStatus"
                }
                """);

        return ACTIVITY_EXAMPLE_LIST;
    }
}
