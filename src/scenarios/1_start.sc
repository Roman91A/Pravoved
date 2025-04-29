theme: /

    # /GlobalNoMatch
    state: GlobalNoMatch
        event!: noMatch
        script: 
            $session.snr20 = $session.snr20?  $session.snr20: 0
            $session.snr20 ++
        if: $session.snr20 === 1
            audio: {{response.StartFirstSpeechNotRecognized1.audio}}
        elseif:  $session.snr20 === 2
            audio: {{response.StartFirstSpeechNotRecognized2.audio}}
        else:
            script: $session.user.resultContext = "GlobalNoMatch"
            audio: {{response.StartFirstSpeechNotRecognized3.audio}}
            go!: /Analytics


    # /StartFirst
    state: StartFirst
        q!: $regex</start>
        script:  
            $dialer.setNoInputTimeout(2500);
            $temp.dialer = $dialer.getPayload()
            $session.pushback = $temp.dialer.pushback;
            $session.event = $temp.dialer.event;
            $session.user = {   
                "phone":  $temp.dialer.phone,
                "resultContext": null, // String текущий контекст
                "qualified": null, // Boolean квалификация --- question
                "name" : null, // String имя
                "audio" : null, // String audio url
                "leftRequest" : null, // Boolean оставлял заявку --- question
                "citizenship" : null, //  Boolean гражданство
                "locality": null, // String Город
                "region": null, // String Регион
                "amountOfDebt": null, // Nuber общая сумма долга
                "debtComposition": {
                    "type": null, // Boolean тип структуры долга 
                    "details": [], // Array продукты в типе долга 
                },
                "deposit": null, // Boolean есть залог или  нет
                "property": null, // Boolean имущество
                "work": null, // Boolean работа
                "businessDeal": null, // Boolean сделки с недвижимостью
                "bankruptcy": null // Boolean Банкротство
            }
        audio: {{response.StartFirst.audio}}

        # StartFirst/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr1 = $session.snr1 ?  $session.snr1 : 0
                $session.snr1 ++
            if: $session.snr1 === 1
                audio: {{response.StartFirstSpeechNotRecognized1.audio}}
            elseif:  $session.snr1 === 2
                audio: {{response.StartFirstSpeechNotRecognized2.audio}}
            else:
                script: $session.user.resultContext = "StartFirst/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics

        # TODO: Не забыть отредактировать
        # StartFirst/Start
        state: Start
            intent: /Start
            intent: /Yes
            if: $session.event === "count"
                go!: /Cont
            elseif: $session.event === "region"
                go!: /Region
            elseif: $session.event === "name"
                go!: /Test_Name_1
            else:
                go!: /StartSecond


    # StartSecond
    state: StartSecond
        script: 
            $dialer.setNoInputTimeout(3000);
        audio: {{response.StartSecond.audio}}

        # StartSecond/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: noMatch
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr3 = $session.snr3 ?  $session.snr3 : 0
                $session.snr3 ++
            if: $session.snr3 === 1
                audio: {{response.StartFirstSpeechNotRecognized1.audio}}
            elseif:  $session.snr3 === 2
                audio: {{response.StartFirstSpeechNotRecognized2.audio}}
            else:
                script: $session.user.resultContext = "StartSecond/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics


        # StartSecond/Yes
        state: Yes
            intent: /Yes
            intent: /StartSecondNoYes
            script: $session.user.leftRequest = true;
            go!: /Name_1


        # StartSecond/No
        state: No
            intent: /No
            intent: /StartSecondNoNo
            script: 
                $dialer.setNoInputTimeout(2500)
                $session.user.resultContext = "StartSecond/No"
                $session.user.leftRequest = false

            audio: {{response.StartSecondNo.audio}}

            # StartSecond/No/No
            state: No
                intent: /StartSecondNoNo
                intent: /No
                script: 
                    $session.user.resultContext = "StartSecond/No/No"
                    $session.user.leftRequest = false
                audio: {{response.StartSecondNoNo.audio}}
                go!: /Analytics

            # StartSecond/No/Yes
            state: Yes
                intent: /StartSecondNoYes
                intent: /Yes
                script: 
                    $session.user.resultContext = "StartSecond/No/Yes"
                    $session.user.leftRequest = true
                audio: {{response.Understood.audio}}
                go!: /Name_1


            # StartSecond/No/SpeechNotRecognized
            state: SpeechNotRecognized || noContext = true
                event: noMatch
                event: speechNotRecognized
                script:
                    $dialer.setNoInputTimeout(2500);
                    $session.snr2 = $session.snr2 ?  $session.snr2 : 0
                    $session.snr2 ++
                if: $session.snr2 === 1
                    audio: {{response.StartFirstSpeechNotRecognized1.audio}}
                elseif:  $session.snr2 === 2
                    audio: {{response.StartFirstSpeechNotRecognized2.audio}}
                else:
                    script: $session.user.resultContext = "StartSecond/No/SpeechNotRecognized"
                    audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                    script: $session.user.qualified = false;
                    go!: /Analytics

        # StartSecond/ContactSourceInquiry
        state: ContactSourceInquiry || noContext = true
            intent: /ContactSourceInquiry
            audio: {{response.ContactSourceInquiry3.audio}}