require: city/city.sc
    module = sys.zb-common

require: slotfilling/slotFilling.sc
    module = sys.zb-common

require: dateTime/moment.min.js
    module = sys.zb-common

require: dicts/response.yaml
    var = response
  
require: dicts/tokenCall.yaml
    var = configTokenCall

require: dicts/configGoogle.yaml
    var = configGoogle
    
require: dicts/locality.yaml
    var = geo

require: dicts/firstName.yaml
    var = userName
    

require: js/functions.js
require: scenarios/bookingCalls.sc
require: scenarios/1_start.sc
require: scenarios/2_name.sc
require: scenarios/3_citizenship.sc
require: scenarios/4_region.sc
require: scenarios/5_amountOfDebt.sc
require: scenarios/6_debtComposition.sc
require: scenarios/7_credit.sc
require: scenarios/8_property.sc
require: scenarios/10_businessDeal.sc
require: scenarios/9_work.sc
require: scenarios/11_bankruptcy.sc
require: scenarios/12_resettingTheCall.sc
require: scenarios/analytics.sc
require: scenarios/bookingCalls.sc





init: 
    $global.$ = {
        __noSuchProperty__: function(property) {
            return $jsapi.context()[property];
        }
    };



    bind("preProcess", function($context) {
        /* фиксация истории переходов, поскольку session.transitionsHistory не поддерживается
        в $session.intentsTransitions фиксируются только стейты с интентами,
        в которых есть Реплики для перехода на оператора или которые являются тематическими
        "служебные стейты" /SlotFilling и /Switch не отображаются в истории переходов */
        $context.session.history = $context.session.history || [];
        if (!$context.currentState.startsWith("/SlotFilling") && !$context.currentState.startsWith("/Switch")) {
            $context.session.request = $context.request.query;
        }
    });



    bind("preMatch", function($context) {
        if (!_.isEmpty($context.request.query)){
            fixMillionThousand($context)
        }

    });



    /* Обрботчик onScriptError */
    bind("onScriptError", function($context) {
        log('[+++] 🧠🧠🧠 onScriptError = ' + toPrettyString($context.exception.message));
        $dialer.hangUp();
    });





# TODO добавить статусы ошибок, когда мы не дозвонились
theme: /

    # Если положили трубку
    state: ClientHangUp
        event!: hangup
        script: 
        go!: /Analytics
