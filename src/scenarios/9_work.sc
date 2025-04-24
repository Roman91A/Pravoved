theme: /

    # Work_8
    state: Work_8
        audio: {{response.Work8.audio}}
        
        # Work_8/NotWorking
        state: NotWorking
            intent: /StartSecondNoNo
            intent: /No
            intent: /NoWork
            script: $session.user.work = true;
            go!: /BusinessDeal_9

        # Work_8/Working
        state: Working
            intent: /hasWork
            intent: /StartSecondNoYes
            intent: /Yes
            script: 
                $session.user.work = false;
                $session.user.qualified = false;
            audio: {{response.Work8Working.audio}}
            audio: {{response.End.audio}}
            go!: /Analytics

        # Work_8/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr15 = $session.snr15 ?  $session.snr15 : 0
                $session.snr15 ++
            if: $session.snr15 === 1
                audio: {{response.Work8CatchAll1.audio}}
            elseif:  $session.snr15 === 2
                audio: {{response.Work8CatchAll2.audio}}
            else: 
                script: $session.user.work = [$context.request.query];
                go!: /BusinessDeal_9

        # Work_8/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr16 = $session.snr16 ?  $session.snr16 : 0
                $session.snr16 ++
            if: $session.snr16 === 1
                audio: {{response.Work8CatchAll1.audio}}
            elseif:  $session.snr16 === 2
                audio: {{response.Work8CatchAll2.audio}}
            else:
                script: 
                    $session.user.qualified = false;
                    $session.user.resultContext = "Work_8/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
