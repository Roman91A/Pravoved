theme: /

    # Credit_6
    state: Credit_6
        q!: 3
        audio: {{response.Credit6.audio}}

        # Credit_6/HasDeposit
        state: HasDeposit
            intent: /HasDeposit
            intent: /StartSecondNoYes
            intent: /Yes
            script: 
                $session.user.deposit = false; 
                $session.user.qualified = false;
            audio: {{response.HasDeposit.audio}}

            
            
            
            # Credit_6/HasDeposit/ReadyForBankruptcy
            state: ReadyForBankruptcy
                intent: /Yes
                intent: /ReadyForBankruptcy
                script:
                    $session.user.qualified = true;
                    $session.user.deposit = true;
                audio: {{response.Understood.audio}} 
                go!: /Property_7


            # Credit_6/HasDeposit/NotReadyForBankruptcy
            state: NotReadyForBankruptcy
                intent: /No
                intent: /NotReadyForBankruptcy
                script: $session.user.qualified = false;
                audio: {{response.Understood.audio}} 
                audio: {{response.ThankYouForYourTime.audio}} 
                go!: /Analytics


        
            # Credit_6/HasDeposit/CatchAll
            state: CatchAll || noContext = true
                event: noMatch
                script:
                    $dialer.setNoInputTimeout(2500);
                    $session.snr30 = $session.snr30 ?  $session.snr30 : 0
                    $session.snr30 ++
                if: $session.snr30 === 1
                    audio: {{response.TheSurvey1.audio}}
                elseif:  $session.snr30 === 2
                    audio: {{response.TheSurvey1.audio}}
                else:
                    script: 
                        $session.user.resultContext = "Credit_6/HasDeposit/CatchAll"
                        $session.user.qualified = false;
                    audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                    go!: /Analytics


            # Credit_6/HasDeposit/SpeechNotRecognized
            state: SpeechNotRecognized || noContext = true
                event: speechNotRecognized
                script:
                    $dialer.setNoInputTimeout(2500);
                    $session.snr19 = $session.snr19 ?  $session.snr19 : 0
                    $session.snr19 ++
                if: $session.snr19 === 1
                    audio: {{response.TheSurvey1.audio}}
                elseif:  $session.snr19 === 2
                    audio: {{response.TheSurvey2.audio}}
                else:
                    script: $session.user.resultContext = "Credit_6/HasDeposit/SpeechNotRecognized"
                    audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                    go!: /Analytics




        # Credit_6/NoDeposit
        state: NoDeposit
            intent: /NoDeposit
            intent: /StartSecondNoNo
            intent: /No
            script: $session.user.deposit = true;
            go!: /Property_7

        # Credit_6/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr11 = $session.snr11 ?  $session.snr11 : 0
                $session.snr11 ++
            if: $session.snr11 === 1
                audio: {{response.Credit6CatchAll1.audio}}
            elseif:  $session.snr11 === 2
                audio: {{response.Credit6CatchAll2.audio}}
            else:
                go!: /Property_7

        # Credit_6/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr12 = $session.snr12 ?  $session.snr12 : 0
                $session.snr12 ++
            if: $session.snr12 === 1
                audio: {{response.Credit6CatchAll1.audio}}
            elseif:  $session.snr12 === 2
                audio: {{response.Credit6CatchAll2.audio}}
            else:
                script: $session.user.resultContext = "Credit_6/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
