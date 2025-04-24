theme: /

    # Property_7
    state: Property_7
        audio: {{response.Property7.audio}}

        # Property_7/NoAdditionalProperty
        state: NoAdditionalProperty
            intent: /StartSecondNoNo
            intent: /No
            script: $session.user.property = true;
            go!: /Work_8

        # Property_7/HasAdditionalProperty
        state: HasAdditionalProperty
            intent: /HasAdditionalProperty
            intent: /StartSecondNoYes
            intent: /Yes
            script: 
                $session.user.property = false;
                $session.user.qualified = false;
            audio: {{response.Property7HasAdditionalProperty.audio}}
            audio: {{response.End.audio}}
            go!: /Analytics

        # Property_7/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr13 = $session.snr13 ?  $session.snr13 : 0
                $session.snr13 ++
            if: $session.snr13 === 1
                audio: {{response.Property7CatchAll1.audio}}
            elseif:  $session.snr13 === 2
                audio: {{response.Property7CatchAll2.audio}}
            else: 
                script: 
                    $session.user.property  = [$context.request.query];
                go!: /Work_8

        # Property_7/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr14 = $session.snr14 ?  $session.snr14 : 0
                $session.snr14 ++
            if: $session.snr14 === 1
                audio: {{response.Property7CatchAll1.audio}}
            elseif:  $session.snr14 === 2
                audio: {{response.Property7SpeechNotRecognized2.audio}}
            else:     
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Property_7/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
