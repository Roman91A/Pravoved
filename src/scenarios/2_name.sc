theme: /
    
    # Name_1
    state: Name_1

        audio: {{response.Name1.audio}}

        # Name_1/isName
        state: isName
            q: * @mystem.persn *
            script:
                if ($parseTree["_mystem.persn"]){
                    $temp.n = $parseTree["_mystem.persn"];
                    $session.user.name = $temp.n;
                    $temp.url = getNameUrl($temp.n);
                }
            if: $temp.url
                audio: {{$temp.url}} 
            else: 
                audio: {{response.IsName.audio}}
            go!: /Citizenship_2

    
        # Name_1/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr4 = $session.snr4 ?  $session.snr4 : 0
                $session.snr4 ++
            if: $session.snr4 === 1
                audio: {{response.Name1SpeechNotRecognazed2.audio}}
            elseif:  $session.snr4 === 2
                audio: {{response.StartFirstSpeechNotRecognized1.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Name_1/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
            
            
        # Name_1/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script: $session.user.name = $context.request.query;
            audio: {{response.IsName.audio}}
            go!: /Citizenship_2