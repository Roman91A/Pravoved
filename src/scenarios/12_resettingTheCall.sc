theme: /

    # AnsweringMachine
    state: AnsweringMachine
        intent!:  /AnsweringMachine
        script: $session.user.resultContext = "/AnsweringMachine"
        go!: /Analytics


    # SmartAnsweringMachine
    state: SmartAnsweringMachine
        intent!:  /SmartAnsweringMachine
        script: $session.user.resultContext = "/SmartAnsweringMachine"
        go!: /Analytics


    # FoulLanguage
    state: FoulLanguage
        q!: * @mystem.obsc *
        q!: $regexp_i<[\s\S]*(\bбля.{0,5}\b|\b(вы|про|е)бля.{0,5}\b|\bсу[кч][аи].{0,3}\b|\b[ао]хуе.{0,4}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bг[ао]ндон.?\b|\b.{0,2}хуй.{0,2}\b|\bна[её]б.{0,7}\b|\b.{0,4}ебал.{0,6}\b|\bпид[ао]р.{0,4}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bеб[ао]н.{0,5}\b|\bебуч.{0,3}\b|\bпоху.{0,5}\b|\b(иди.+|иди|пош.+)\sв\sочко\b|\bжоп[уае]\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bд[ие]бил.{0,4}\b|\bурод.{0,4}\b|\b.{0,3}пизд.{0,5}\b|\b[нп][оа]хер.?\b|\bхер.{0,2}\b|\bхеров.{0,3}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bн[аеи]ху[ийя]\b|\bху[еи]та\b|\bхул[ьи]\b|\bвз[ъь]еб.{0,4}\b|\bвыпердеть\b|\b.{0,3}сра[лтн][ыиье].{0,4}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\b.{0,3}с[сц]ать.{0,2}\b|\bг[оа]в[ёе][нш].{0,4}\b|\b(на|под|за|из)г[оа]вн.{0,7}\b|\bг[оа]вн.{0,7}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bдерьмо.{0,3}\b|\bд[оа]лб[оа]еб.?\b|\bдрист.{0,5}\b|\b.{0,2}дроч.{0,6}\b|\b[ёе]бну.{0,4}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\b[иеё]ба[лнрт].{0,3}\b|\b[еия]блив[аы][йея]\b|\b[яеи]буч[иа][яйе]\b|\bелда(к|чить)\b|\bзаебыв.{0,5}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bзалупа\b|\bзас[еи]?рать\b|\bзасрун.?\b|\bзлоебуч.{0,5}\b|\bкурв[аы]\b|\bконч[еиаы](нн?)[аы][йея]\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bминет.?\b|\bмокрощелк[иа]\b|\bмудак.{0,3}\b|\bмуде[нт]ь\b|\bмуди(ла|сты)\b|\bмуд(ня|о[ёе]б|озвон).?\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bобсира.{0,3}\b|\bотсос.{0,3}\b|\bоху[еи][лвт].{0,5}\b|\b(о|за)хуя[чк].{0,4}\b|\bперд.{0,5}\b|\bперн[уеи]т.{0,2}\b|\bп[ие]д[оа]р.{0,5}\b|\bп[ие]др.{0,3}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bпод[ъь]ебну.{0,3}\b|\bраз[ъь]еба.{0,3}\b|\bсрак[ау]\b|\bссак[иа]\b|\bс[сц]ать\b|\bсцых[иа]\b|\bссыкун.{0,3}\b|\bус[сц]аться\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bхитрожопы[йе]\b|\b[шх]люх[аи]\b|\bху[её]в.{0,4}\b|\bху[еи]сос.{0,5}\b|\bхуищ[аие]\b|\bху[яёе]к.{0,3}\b|\bцелк[иеа]\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bлох.?\b|\blo[hx].?\b|\bf[au]ck\b|\bу[её]б(ище|ища|ищу|ок|ки|ка|ку|кам|ан|ану|аны)|\b[еи]бл[ао]н.{0,3}\b)[\s\S]*>
        q!: $regexp_i<[\s\S]*(\bмраз.{0,5}\b|\b(по|за|вы|у)[ёе]бушк.{0,3}\b)[\s\S]*>
        script: 
            $session.user.resultContext = "/FoulLanguage";
            $session.user.qualified = false;
        go!: /Analytics

        
    state: Busy
        intent!: /Busy
        script: 
            $session.user.resultContext = "/Busy";
            $session.user.qualified = false;
        audio: {{response.StartSecondNoNo.audio}}
        go!: /Analytics
