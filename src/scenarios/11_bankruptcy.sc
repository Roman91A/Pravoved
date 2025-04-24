theme: /
 
    # Bankruptcy_10
    state: Bankruptcy_10
        q!: 33
        audio: {{response.Bankruptcy10.audio}}


        # Bankruptcy_10/NotBankruptcy
        state: NotBankruptcy
            intent: /StartSecondNoNo
            intent: /No
            script: $session.user.bankruptcy = true;
            audio: {{response.NotBankruptcy.audio}}
            go!: /Analytics




        # Bankruptcy_10/HasBankruptcy
        state: HasBankruptcy
            intent: /StartSecondNoYes
            intent: /Yes
            script: 
                $session.user.bankruptcy = false;
                $session.user.qualified = false;
            audio: {{response.HasBankruptcy.audio}}
            go!: /Analytics

        # Bankruptcy_10/CatchAll
        state: CatchAll
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr171 = $session.snr171 ?  $session.snr171 : 0
                $session.snr171 ++
            if: $session.snr171 === 1
                audio: {{response.Bankruptcy10CatchAll1.audio}}
            elseif:  $session.snr171 === 2
                audio: {{response.Bankruptcy10CatchAll2.audio}}
            else:
                script: 
                    $session.user.bankruptcy = [$context.request.query];
                    $session.user.resultContext = "Bankruptcy_10/CatchAll"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}

                go!: /Analytics


        # Bankruptcy_10/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr181 = $session.snr181 ?  $session.snr181 : 0
                $session.snr181 ++
            if: $session.snr181 === 1
                audio: {{response.Bankruptcy10CatchAll1.audio}}
            elseif:  $session.snr181 === 2
                audio: {{response.Bankruptcy10CatchAll2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Bankruptcy_10/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics