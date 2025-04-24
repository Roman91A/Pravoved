theme: /

    # DebtComposition_5
    state: DebtComposition_5
        audio: {{response.DebtComposition5.audio}}

        # DebtComposition_5/PositiveDebt
        state: PositiveDebt
            intent: /PositiveDebt
            script: 
                if ($parseTree["_consumer_credit"]) $session.user.debtComposition.details.push("Потребительский кредит")
                if ($parseTree["_credit"]) $session.user.debtComposition.details.push("Кредит")
                if ($parseTree["_credit_card"]) $session.user.debtComposition.details.push("Кредитная карта")
                if ($parseTree["_medical_bills"]) $session.user.debtComposition.details.push("Медицинские счета")
                if ($parseTree["_mfo"]) $session.user.debtComposition.details.push("МФО")
                if ($parseTree["_microcredit"]) $session.user.debtComposition.details.push("Микрокредит")
                if ($parseTree["_microloan"]) $session.user.debtComposition.details.push("Микрозайм")
                if ($parseTree["_private_person"]) $session.user.debtComposition.details.push("Долг физическому лицу")
                if ($parseTree["_receiptр"]) $session.user.debtComposition.details.push("По расписписке физическому лицу")
                if ($parseTree["_tax"]) $session.user.debtComposition.details.push("Налоги")
                if ($parseTree["_traffic_police"]) $session.user.debtComposition.details.push("Штрафы ГИБДД")
                if ($parseTree["_utilities_sector"]) $session.user.debtComposition.details.push("ЖКУ")
                $session.user.debtComposition.type = true;
            go!: /Credit_6

        # DebtComposition_5/NegativeDebt
        state: NegativeDebt
            intent: /NegativeDebt
            script: 
                $session.user.debtComposition.type = false;
                $session.user.qualified = false;
            audio: {{response.NegativeDebt.audio}}
            go!: /Analytics

        # DebtComposition_5/CatchAll
        state: CatchAll || noContext = true
            event: noMatch
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr9 = $session.snr9 ?  $session.snr9 : 0
                $session.snr9 ++
            if: $session.snr9 === 1
                audio: {{response.DebtComposition5CatchAll1.audio}}
            elseif:  $session.snr9 === 2
                audio: {{response.DebtComposition5CatchAll2.audio}}
            else:
                script: $session.user.debtComposition.details.push($context.request.query);
                go!: /Credit_6

        # DebtComposition_5/SpeechNotRecognized
        state: SpeechNotRecognized || noContext = true
            event: speechNotRecognized
            script:
                $dialer.setNoInputTimeout(2500);
                $session.snr10 = $session.snr10 ?  $session.snr10 : 0
                $session.snr10 ++
            if: $session.snr10 === 1
                audio: {{response.DebtComposition5CatchAll1.audio}}
            elseif:  $session.snr10 === 2
                audio: {{response.DebtComposition5CatchAll2.audio}}
            else:
                script:
                    $session.user.qualified = false;
                    $session.user.resultContext = "DebtComposition_5/SpeechNotRecognized"
                audio: {{response.StartFirstSpeechNotRecognized3.audio}}
                go!: /Analytics
