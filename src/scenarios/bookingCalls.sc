# Сценарий вызова звонка 

theme: /

    state: bookingCall
        event!: bookingCall
        script:
            var clientId = $request.clientId;
            var data = $request.rawRequest.data;

            // TODO:  сохранить переданные параметры в session
            var param = {
              "phone": data.phone, 
              "name": data.name, 
              "clientId": 'test1'
              
            }

            // Дебаг
            /*
            var param = {
              "token": configTokenCall.test,
              "clientId": clientId,
              "phone":  "79994762401", 
              "text": ["привет", "купил слона"]
            }
            */
            
            $response.replies = $response.replies || [];

            // Первое добавление номера      
            var response = firstAddPhones(configTokenCall.test, param)      

            var responseReplies = {"type":"raw","body":{"status":"accepted"}};

            if (response.s === 400){
              // Последующее добавление номера
              var  test  = nextAddPhones(configTokenCall.test, param);
              $response.replies.push(responseReplies);
            } else {
               $response.replies.push(responseReplies);
            }


    state: Stop
        q!: стоп
        script: $jsapi.stopSession();
        a: ok


#=================================ТЕСТ======================================

    state: Cont
        a: Привет, тут мы будем тестировать сумму долга.
        a: Чтобы лучше разобраться в Вашей ситуации, расскажите, пожалуйста, какая общая сумма долга у Вас на данный момент?


        # AmountOfDebt_4/AmountOfDebt
        state: AmountOfDebt
            intent: /AmountOfDebt

            script: 

                var amountOfDebt = sumDucklingNumbers($parseTree);
                $temp.amountOfDebt = shiftHundredsToThousands(amountOfDebt)


            a: Я насчитала {{$temp.amountOfDebt}}
            a: Пробуем ещё раз, уточните сумму долга, только иначе



        # AmountOfDebt_4/CatchAll
        state: CatchAll
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr33 = $session.snr33 ?  $session.snr33 : 0
                $session.snr33 ++
            if: $session.snr33 === 1
                a: Не расслышала, какая сумма задолженности у вас?
            elseif:  $session.snr33 === 2
                a: Что-то очень плохая связь, уточните сумму задолжности 
            else:
                script: $session.user.resultContext = "AmountOfDebt_4/CatchAll"
                a: Пробуем ещё раз, уточните сумму долга, только иначе


        # AmountOfDebt_4/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr8 = $session.snr8 ?  $session.snr8 : 0
                $session.snr8 ++
            if: $session.snr8 === 1
                a: Вас не слышно, какая сумма долга у вас?
            elseif:  $session.snr8 === 2
                a: Что-то очень плохая связь, повторите ещё раз. Какая сумма долга?
            else:
                script: $session.user.resultContext = "AmountOfDebt_4/SpeechNotRecognized"
                a: Пробуем ещё раз, уточните сумму долга, только иначе


    state: Region
        a: Привет, тут мы будем тестировать определение города и села. 
        a: Подскажи, откуда обращаешься?

 
        # Region/City
        state: City
            q: * @mystem.geo * 
            q: * @pymorphy.geox * 
            q: * $City *
            script:

                $session.temp = {};

                if ( $parseTree["City"]){
                    var gNomn = $parseTree["_City"].name;
                } else if ($parseTree["_pymorphy.geox"]) {
                    $temp.g = $parseTree["_pymorphy.geox"];
                    var gNomn = $nlp.inflect($temp.g, "nomn"); // Указываем в нужный падеж
                } else {
                    $temp.g = $parseTree["_mystem.geo"];
                    var gNomn = $nlp.inflect($temp.g, "nomn"); // Указываем в нужный падеж
                }

                var response = findCitiesWithSubstring(gNomn);

                if (isNotEmptyArray(response)){
                    $reactions.answer("Это какой регион?");
                    $session.temp.locality = gNomn;
      
                } else if (_.isArray(response)){
                    $reactions.answer("Это какой регион?");
                    $session.temp.locality = gNomn;
                    $reactions.transition({value: "/GetRegion_Test", deferred: true});
                 } else {
    
                    $session.user.locality = response.locality;
                    $session.user.region = response.region;
  
                    $reactions.answer("Вот что мне удалось определить");
                    $reactions.answer(JSON.stringify($session.user.locality));
                    $reactions.answer(JSON.stringify($session.user.region));

                    $reactions.answer("Давай попробуем ещё раз. Откуда ты?");
                    $reactions.transition({value: "/Region", deferred: true});
                    
                }
            
        

        # Region/CatchAll
        state: CatchAll
            event: noMatch
            a: Это какой регион?
            script: 
                $session.temp = {};
                $session.temp.locality = $parseTree.text;
                $reactions.transition({value: "/GetRegion_Test", deferred: true});


        # Region/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr6 = $session.snr6 ?  $session.snr6 : 0
                $session.snr6 ++
            if: $session.snr6 === 1
                a: Не расслышала, в каком городе вы проживаете?
            elseif:  $session.snr6 === 2
                a: Что-то очень плохая связь, повторите ещё раз. В каком городе вы проживаете?
            else:
                script: $session.user.resultContext = "Location_3/SpeechNotRecognized"
                a: К сожалению, я вас не слышу. Перезвоню вам позже!
                go!: /Analytics

       
    # GetRegion_Test
    state: GetRegion_Test

        # GetRegion/WriteRegion
        state: WriteRegion
            q: * 
            script: 
                var param = $session.temp.locality + " , " + $parseTree.text;
                var response = getAddress(param)
                if (response){

                    $reactions.answer("Вот что мне удалось определить");
                    $reactions.answer(JSON.stringify(response[0].result));
                    $reactions.answer(JSON.stringify(response[0].region_with_type));

                    $reactions.answer("Давай попробуем ещё раз. Откуда ты?");
                    $reactions.transition({value: "/Region", deferred: true});
                }

 

    state: Test_Name_1
        
        audio: https://f91ce0dd-1d82-4419-9be0-62264b73469e.selstorage.ru/test1.wav


        # Name_1/isName
        state: isName || noContext = true
            q: * @mystem.persn *
            script:
                if ($parseTree["_mystem.persn"]){
                    $temp.n = $parseTree["_mystem.persn"];
                    $session.user.name = $temp.n;
                    $temp.url = getNameUrl($temp.n)
                }
            if: $temp.url
                audio: {{$temp.url}} 
                audio: https://f91ce0dd-1d82-4419-9be0-62264b73469e.selstorage.ru/test2.wav
            else: 
                a: Очень приятно


