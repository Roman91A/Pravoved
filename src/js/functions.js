// Проверяет, что arr - массив, причем не пустой
function isNotEmptyArray(arr) {
    return _.isArray(arr) && !_.isEmpty(arr);
}



function getGPT(prompt, assistants) {
    var res = $http.post("http://150.241.68.42:5000/gpt-query", {
        headers: { "accept": "application/json" },
        body: {
            prompt: prompt,
            assistants: assistants,
        }
    })
    if (res && res.isOk) return res.data.result;
    return null;
}




function returnData(link, data) {
    log('[+++] 🧠🧠🧠 data = ' + toPrettyString(data));

    $http.post(link, {
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    })

}



function findCitiesWithSubstring(substring) {
    var results = [];
    var lowerSubstring = String(substring).toLowerCase();

    for (var city in geo) {
        if (geo.hasOwnProperty(city)) {
            // сравнение «город == подстрока» без учёта регистра
            if (city.toLowerCase() === lowerSubstring) {
                results.push({
                    locality: city,
                    region: geo[city]
                });
            }
        }
    }

    // если найден ровно один город — вернуть сам объект
    return results.length === 1 ? results[0] : results;
}


function fixMillionThousand($context) {
    var text = $context.request.query;

    // Сразу чистим Ё/ё:
    text = text.replace(/ё/g, "е").replace(/Ё/g, "Е");

    // === (1) ДОБАВЛЕННЫЙ ШАГ. Нормализация "четырехсот" -> "четыреста" и т.п. ===
    text = text.replace(
        /\b(двух|трех|трёх|четырех|четырёх|пяти|шести|семи|восьми|девяти)сот\b/gi,
        function (_, prefix) {
            switch (prefix.toLowerCase()) {
                case "двух": return "двести";
                case "трех":
                case "трёх": return "триста";
                case "четырех":
                case "четырёх": return "четыреста";
                case "пяти": return "пятьсот";
                case "шести": return "шестьсот";
                case "семи": return "семьсот";
                case "восьми": return "восемьсот";
                case "девяти": return "девятьсот";
            }
        }
    );

    // === (2) "тысяч сто" -> "сто тысяч" и т.п.
    text = text.replace(
        /\bтысяч\s+(сто|двести|триста|четыреста|пятьсот|шестьсот|семьсот|восемьсот|девятьсот)\b/gi,
        "$1 тысяч"
    );

    // === (3) Ваши pattern1, pattern2, numberWords, parseMultiWordNumber, replacer1, replacer2 ===

    var pattern1 = /(?:^|[^А-Яа-яЁё])(миллион[а-яё]*|тысяч[а-яё]*)\s+(?:и\s+)?((?:\d+|[А-Яа-яЁё]+)(?:\s+(?:\d+|[А-Яа-яЁё]+)*))(?=$|[^А-Яа-яЁё])/gi;
    var pattern2 = /(?:^|[^А-Яа-яЁё])((?:\d+|[А-Яа-яЁё]+)(?:\s+(?:\d+|[А-Яа-яЁё]+)*))\s+(?:и\s+)?(миллион[а-яё]*|тысяч[а-яё]*)?(?=$|[^А-Яа-яЁё])/gi;

    var numberWords = { /* ваш словарь чисел */ };
    function getNumericValue(word) { /* реализация, как у вас */ }
    function parseMultiWordNumber(phrase) { /* реализация, как у вас */ }

    function replacer1(match, group1, group2) {
        var isMillion = group1.toLowerCase().startsWith("миллион");
        var multiplier = isMillion ? 1000000 : 1000;
        var num = parseMultiWordNumber(group2);
        if (!num) return match;
        // "миллион триста" => "1 300 000"
        if (isMillion && num < 1000) {
            return String(1000000 + num * 1000);
        }
        return String(num * multiplier);
    }

    function replacer2(match, group1, group2) {
        var isMillion = group2 && group2.toLowerCase().startsWith("миллион");
        var multiplier = isMillion ? 1000000 : 1000;
        var num = parseMultiWordNumber(group1);
        if (!num) return match;
        return String(num * multiplier);
    }

    text = text.replace(pattern1, replacer1);
    text = text.replace(pattern2, replacer2);

    // === (4) Доп. проход: склеиваем большие числа (>=100) между которыми только пробел или "и " ===
    var unifyPattern = /(\d{3,})\s+(?:и\s+)?(\d{3,})/g;
    var replaced;
    do {
        replaced = false;
        text = text.replace(unifyPattern, function (_match, g1, g2) {
            replaced = true;
            return String(parseInt(g1, 10) + parseInt(g2, 10));
        });
    } while (replaced);

    // Сохраняем результат
    $context.request.query = text;
}




function shiftHundredsToThousands(num) {
    // 1) Сколько миллионов
    var millions = Math.floor(num / 1000000);
    // 2) Остаток после миллионов
    var remainder = num % 1000000;

    // 3) Сколько тысяч в остатке
    var thousands = Math.floor(remainder / 1000);
    // 4) Осталось сотен + десятков + единиц
    var hundredsAndBelow = remainder % 1000;

    // 5) Если тысячи == 0, переносим ВСЮ часть hundredsAndBelow к тысячам
    if (thousands === 0 && hundredsAndBelow > 0) {
        thousands = hundredsAndBelow;
        hundredsAndBelow = 0;
    }

    // 6) Собираем число обратно
    var result = millions * 1000000
        + thousands * 1000
        + hundredsAndBelow;

    return result;
}



function sumDucklingNumbers(obj) {
    if (!obj || !obj["duckling.number"] || !Array.isArray(obj["duckling.number"])) {
        return 0; // Если данных нет, возвращаем 0
    }

    // Суммируем все значения value внутри duckling.number
    return obj["duckling.number"].reduce(function (sum, item) {
        if (!item || typeof item.value !== "number") {
            return sum; // Пропускаем некорректные значения
        }

        var value = item.value < 1000 ? item.value * 1000 : item.value;
        return sum + value;
    }, 0);
}


/**
 * Отправляет запрос на DaData для очистки/стандартизации адреса.
 * https://dadata.ru/api/geocode/
 * @param {String} address - Адрес
 * @returns {Array|null} - Результат сервиса  или null в случае ошибки.
 */
function getAddress(address) {

    var url = "https://cleaner.dadata.ru/api/v1/clean/address";
    var apiKey = $.injector.api.dadata.apiKey;
    var secretKey = $.injector.api.dadata.secretKey;
    var res = $http.post(url, {
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": "Token " + apiKey,
            "X-Secret": secretKey
        },
        body: [address]
    });
    if (res && res.isOk) return res.data;
    return null;
}


/**
 * Возвращает ссылку на запись, в которой лежит аудио реплики 
 * 
 * @param {String} name - имя которое озвучил пользователь
 * @returns {String|null} - Ссылка или null, если записи нет
 */
function getNameUrl(inputName) {

    var nameYaml = userName.name;
    var url = userName.url; // + akhmed.wav
    var inputName = inputName.toLowerCase().replace("ё", "е");

    for (var n in nameYaml) {

        var lowercaseArray = _.map(nameYaml[n], function (name) {
            return name.toLowerCase().replace("ё", "е")
        });

        if (_.contains(lowercaseArray, inputName)) {
            return url + n + '.wav'
        }
    }
    return null;
}


/**
 * Отправка лида
 */
function sendPartnerLead(name, phone, question, city_id, audioLink) {

    var name = name;
    var phone = phone;
    var question = question;
    var city_id = city_id;

    var headers = { "Content-Type": "application/x-www-form-urlencoded" }

    var form = {
        "edata[phone]": phone,
        "edata[question]": question,
        "edata[name]": name,
        "edata[cd-referral]": "94f62be214ac507199e27eba420eae24",
        "edata[secret]": "ab40a992710370521bb24a1dc19e3921",
        "edata[city_id]": city_id,
        "edata[putm_medium]": audioLink,
        "edata[putm_content]": "JustAI"
    }
    
    // "https://leads-reception.feedot.com/api/v1/partner-leads"
    // https://admin.feedot.com/index/exportlog
    var res = $http.post("https://leads-reception.feedot.com/api/v1/partner-leads", {
        headers: headers,
        form: form
    })
    log('[+++] 🧠🧠🧠 headers = ' + toPrettyString(headers));
    log('[+++] 🧠🧠🧠 form = ' + toPrettyString(form));
    log('[+++] 🧠🧠🧠 sendPartnerLead = ' + toPrettyString(res));

    if (res && res.isOk) return true;
    return null;
}

/**
 * Получаем id города
 */

function getCityId(city) { 

    var url = "https://api.my.feedot.com/rest/v1/cities?prefix=" + city

    var res = $http.get(url, {
        headers: { "Content-Type": "application/json" },
    })

    if (res && res.isOk) return res.data.data.cities;
    return null;


}