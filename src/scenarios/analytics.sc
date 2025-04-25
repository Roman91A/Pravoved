theme: /

    state: Analytics
        script: 
            $dialer.hangUp();

            try {
                if (!$session.analytics){
                    $session.analytics = true;
                    var link = $session.pushback;
                    var data = $session.user;
                
                    if (link && data ) {
                        returnData(link, data);
                    }

                    var name = $session.user.name;
                    var phone = $session.user.phone;
                    // получаем id города 
                    var arrayCityId =  getCityId($session.user.locality);
                    var city_id = isNotEmptyArray(arrayCityId) ?  arrayCityId[0].id : null;
                    var urlAudio = $dialer.getCallRecordingFullUrl();  // ссылка на запись звонка
                    // Если статус == квал, то передаём лида 
                    if ($session.user 
                        && $session.user.qualified 
                        && $session.user.leftRequest
                        && $session.user.citizenship
                        && $session.user.amountOfDebt
                        && isNotEmptyArray($session.user.debtComposition.details)
                        && $session.user.deposit
                        && $session.user.property
                        && $session.user.businessDeal 
                        && $session.user.bankruptcy 
                        && $session.user.work 
                        ){
                            var leftRequest = $session.user.leftRequest ? "Да" : "Нет"; // Bool
                            var citizenship = $session.user.citizenship ? "Да" : "Нет"; // Bool
                            var amountOfDebt = _.isArray($session.user.amountOfDebt) ? $session.user.amountOfDebt[0] : $session.user.amountOfDebt; // число или строка '300000+' или массив
                            var debtComposition = $session.user.debtComposition.details ? $session.user.debtComposition.details.join(',') : "Не известно";  // массив
                            var deposit = $session.user.deposit ? "Нет" : "Да";; // Bool
                            var property = _.isArray($session.user.property)  ?  $session.user.property[0] : 'Нет';  // Bool или массив                  
                            var businessDeal = _.isArray($session.user.businessDeal)  ?  $session.user.businessDeal[0] : 'Нет';  // Bool или массив 
                            var bankruptcy = _.isArray($session.user.bankruptcy)  ?  $session.user.bankruptcy[0] : 'Нет';  // Bool или массив 
                            var work = _.isArray($session.user.work)  ?  $session.user.work[0] : 'Нет';  // Bool или массив 
                            
 
                            var question = "Вы интересовались процедурой ?: " + leftRequest+
                                        "\nВы являетесь гражданином РФ ?: " + citizenship+
                                        "\nОбщая сумма долга?: " + amountOfDebt +
                                        "\nСостав суммы долга?: " + debtComposition+
                                        "\nЕсть залоговые кредиты?: " + deposit+
                                        "\nЕсть имущество кроме единственного жилья?: " + property+
                                        "\nЕсть официальный доход?: " + work+
                                        "\nЕсть сделки за последние три года?: " + businessDeal+
                                        "\nПризнавались банкротом за последние 5 лет?: " + bankruptcy

                            sendPartnerLead(name, phone, question, city_id, urlAudio);
                    }
                }
            } catch (e) {
                log('[+++] 🧠🧠🧠 stack = ' + toPrettyString(e.stack));
            } 




    state: OnCallNotConnectedTechnical
        event!: onCallNotConnectedTechnical
        event!: onCallNotConnected 
        script: 
            $temp.dialer = $dialer.getPayload()
            $session.pushback = $temp.dialer.pushback;
            try {
                $session.user = {};
                $session.user.status = $request.rawRequest.hangupData;
            } catch (e) {
                log('[+++] 🧠🧠🧠 stack = ' + toPrettyString(e.stack));
            } 
        go!: /Analytics