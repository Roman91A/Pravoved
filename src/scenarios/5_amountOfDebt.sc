theme: /

        
    # AmountOfDebt_4
    state: AmountOfDebt_4
        q!: 2
        audio: {{response.AmountOfDebt.audio}}

        # AmountOfDebt_4/AmountOfDebt
        state: AmountOfDebt
            intent: /AmountOfDebt
            script: 
                var amountOfDebt = sumDucklingNumbers($parseTree);
                $temp.amountOfDebt = shiftHundredsToThousands(amountOfDebt)
                $session.user.amountOfDebt = $temp.amountOfDebt;    
            if: $temp.amountOfDebt < 300000
                audio: {{response.AmountOfDebt4AmountOfDebt.audio}} 
            else:
                go!: /DebtComposition_5


            # AmountOfDebt_4/СorrectionYes
            state: СorrectionYes
                intent: /Yes
                intent: /CorrectionYes                
                script: $session.user.qualified = false;
                audio: {{response.Understood.audio}} 
                audio: {{response.StartSecondNoNo.audio}} 
                go!: /Analytics
               
                

            # AmountOfDebt_4/CorrectionNo
            state: CorrectionNo
                intent: /No
                intent: /CorrectionNo
                intent: /AmountOfDebt
                script: 
                    var amountOfDebt = sumDucklingNumbers($parseTree);
                    $temp.amountOfDebt = shiftHundredsToThousands(amountOfDebt)
                    if ($temp.amountOfDebt == 0) $temp.amountOfDebt = '300000+';
                    $session.user.amountOfDebt = $temp.amountOfDebt;
                if: $temp.amountOfDebt < 300000 || $temp.amountOfDebt != '300000+'
                    script: $session.user.qualified = false;
                    audio: {{response.Understood.audio}} 
                    audio: {{response.StartSecondNoNo.audio}} 
                else:
                    go!: /DebtComposition_5




            # AmountOfDebt_4/AmountOfDebt/CatchAll
            state: CatchAll || noContext = true
                event: noMatch
                script:
                    $dialer.setNoInputTimeout(2500);
                    $session.snr32 = $session.snr32 ?  $session.snr32 : 0
                    $session.snr32 ++
                if: $session.snr32 === 1
                    audio: {{response.AmountOfDebt4CatchAll1.audio}}
                elseif:  $session.snr32 === 2
                    audio: {{response.AmountOfDebt4CatchAll2.audio}}
                else:
                    script:
                        $session.user.amountOfDebt = [$context.request.query];
                        $session.user.resultContext = "AmountOfDebt_4/AmountOfDebt/CatchAll"
                    go!: /DebtComposition_5

            # AmountOfDebt_4/AmountOfDebt/SpeechNotRecognized
            state: SpeechNotRecognized || noContext = true
                event: speechNotRecognized
                script:
                    $dialer.setNoInputTimeout(2500);
                    $session.snr31 = $session.snr31 ?  $session.snr31 : 0
                    $session.snr31 ++
                if: $session.snr31 === 1
                    audio: {{response.AmountOfDebt4CatchAll1.audio}}
                elseif:  $session.snr31 === 2
                    audio: {{response.AmountOfDebt4CatchAll2.audio}}
                else:
                    script: 
                        $session.user.qualified = false;
                        $session.user.resultContext = "AmountOfDebt_4/AmountOfDebt/SpeechNotRecognized"
                    audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                    go!: /Analytics



        # AmountOfDebt_4/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr7 = $session.snr7 ?  $session.snr7 : 0
                $session.snr7 ++
            if: $session.snr7 === 1
                audio: {{response.AmountOfDebt4CatchAll1.audio}}
            elseif:  $session.snr7 === 2
                audio: {{response.AmountOfDebt4CatchAll2.audio}}
            else:
                script: 
                    $session.user.resultContext = "AmountOfDebt_4/CatchAll";
                    $session.user.amountOfDebt = [$context.request.query];
                go!: /DebtComposition_5

        # AmountOfDebt_4/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr8 = $session.snr8 ?  $session.snr8 : 0
                $session.snr8 ++
            if: $session.snr8 === 1
                audio: {{response.AmountOfDebt4CatchAll1.audio}}
            elseif:  $session.snr8 === 2
                audio: {{response.AmountOfDebt4CatchAll2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "AmountOfDebt_4/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics