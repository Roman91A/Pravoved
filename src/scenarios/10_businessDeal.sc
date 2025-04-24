theme: /

    # BusinessDeal_9
    state: BusinessDeal_9
        q!: 1233
        audio: {{response.BusinessDeal9.audio}}

        # BusinessDeal_9/NotDeal
        state: NotDeal
            intent: /StartSecondNoNo
            intent: /No
            script: 
                $session.user.businessDeal = true;
            go!: /Bankruptcy_10

        # BusinessDeal_9/HasDeal
        state: HasDeal
            intent: /StartSecondNoYes
            intent: /Yes
            script: 
                $session.user.businessDeal = false;
                $session.user.qualified = false;
            go!: /Bankruptcy_10

        # BusinessDeal_9/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr17 = $session.snr17 ?  $session.snr17 : 0
                $session.snr17 ++
            if: $session.snr17 === 1
                audio: {{response.BusinessDeal9CatchAll1.audio}}
            elseif:  $session.snr17 === 2
                audio: {{response.BusinessDeal9CatchAll2.audio}}
            else:
                script: $session.user.businessDeal = [$context.request.query];
                go!: /Bankruptcy_10

        # BusinessDeal_9/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr18 = $session.snr18 ?  $session.snr18 : 0
                $session.snr18 ++
            if: $session.snr18 === 1
                audio: {{response.BusinessDeal9CatchAll1.audio}}
            elseif:  $session.snr18 === 2
                audio: {{response.BusinessDeal9CatchAll2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "BusinessDeal_9/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics