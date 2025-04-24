
theme: /
  
    # Location_3
    state: Location_3
        audio: {{response.Location3.audio}}


        # Location_3/City
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
                // TODO: Сделать рефакторинг
                var response = findCitiesWithSubstring(gNomn);
                if (isNotEmptyArray(response)){
                    $session.temp.locality = gNomn;
                    $reactions.transition({value: "/GetRegion", deferred: false});
                } else if (_.isArray(response)){
                    $session.temp.locality = gNomn;
                    $reactions.transition({value: "/GetRegion", deferred: false});
                } else {
                    $session.user.locality = response.locality;
                    $session.user.region = response.region;
                    $reactions.transition("/AmountOfDebt_4");
                }

            
        # Location_3/CatchAll
        state: CatchAll
            event: noMatch
            audio: {{response.Location3City.audio}}
            script: 
                $session.temp = {};
                $session.temp.locality = $parseTree.text;
                $reactions.transition({value: "/GetRegion", deferred: true});


        # Location_3/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr61 = $session.snr61 ?  $session.snr61 : 0
                $session.snr61 ++
            if: $session.snr61 === 1
                audio: {{response.Location3SpeechNotRecognized1.audio}}
            elseif:  $session.snr61 === 2
                audio: {{response.Location3SpeechNotRecognized2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Location_3/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
  
  # GetRegion
    state: GetRegion
        audio: {{response.Location3City.audio}}

        # GetRegion/WriteRegion
        state: WriteRegion
            q: * 
            script: 
                var param = $session.temp.locality + " , " + $parseTree.text;
                var response = getAddress(param);
                if (response){
                    $session.user.locality = response[0].result;
                    $session.user.region = response[0].region_with_type;
                }
                $reactions.transition("/AmountOfDebt_4");


        # GetRegion/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr6 = $session.snr6 ?  $session.snr6 : 0
                $session.snr6 ++
            if: $session.snr6 === 1
                audio: {{response.Location3SpeechNotRecognized1.audio}}
            elseif:  $session.snr6 === 2
                audio: {{response.Location3SpeechNotRecognized2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Location_3/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics