
theme: /

  # Citizenship_2 
    state: Citizenship_2
        audio: {{response.Citizenship.audio}}


        # Citizenship_2/Citizen
        state: Citizen
            intent: /Yes
            intent: /Citizen
            intent: /StartSecondNoYes
            script: 
                $session.user.qualified = true;
                $session.user.citizenship = true;
            audio: {{response.Good.audio}}
            go!: /Location_3
        
        # Citizenship_2/NotACitizen
        state: NotACitizen
            intent: /StartSecondNoNo
            intent: /NotACitizen
            script: 
                $session.user.qualified = false;
                $session.user.citizenship = false;
            audio: {{response.Citizenship2NotACitizen.audio}}
            audio: {{response.End.audio}}
            go!: /Analytics

        
        # Citizenship_2/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr5 = $session.snr5 ?  $session.snr5 : 0
                $session.snr5 ++
            if: $session.snr5 === 1
                audio: {{response.CitizenshipCatchAll1.audio}}
            elseif:  $session.snr5 === 2
                audio: {{response.CitizenshipCatchAll2.audio}}
            else:
                script: 
                    $session.user.resultContext = "Citizenship_2/CatchAll"
                    $session.user.qualified = false;
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
