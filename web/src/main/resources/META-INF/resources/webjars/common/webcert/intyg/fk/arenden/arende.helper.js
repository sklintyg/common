angular.module('common').service('common.ArendeHelper',
    ['$log', '$timeout', 'common.ArendeProxy', '$window', 'common.statService',
        function( $log, $timeout, ArendeProxy, $window, statService) {
        'use strict';

            this.updateArendeViewState = function(arende) {
                if (arende.amne === 'PAMINNELSE') {
                    // RE-020 Påminnelser is never
                    // answerable
                    arende.answerDisabled = true;
                    arende.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                } else if ((arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arende.amne === 'KOMPLT') && !UserModel.hasPrivilege(UserModel.privileges.BESVARA_KOMPLETTERINGSFRAGA)) {
                    // RE-005, RE-006
                    arende.answerDisabled = true;
                    arende.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
                } else {
                    arende.answerDisabled = false;
                    arende.answerDisabledReason = undefined;
                }

                if ((arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arende.amne === 'KOMPLT') && UserModel.hasRequestOrigin(UserModel.requestOrigins.UTHOPP)) {
                    arende.svaraMedNyttIntygDisabled = true;
                    arende.svaraMedNyttIntygDisabledReason = 'Gå tillbaka till journalsystemet för att svara på kompletteringsbegäran med nytt intyg.';
                } else {
                    arende.svaraMedNyttIntygDisabled = false;
                }

                _updateAtgardMessage(arende);
            };

            function _updateAtgardMessage(arende) {
                if (arende.status === 'CLOSED') {
                    arende.atgardMessageId = 'handled';
                } else if (_isUnhandledForDecoration(arende)) {
                    arende.atgardMessageId = 'markhandled';
                } else if (arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arende.amne === 'KOMPLT') {
                    arende.atgardMessageId = 'komplettering';
                } else {
                    if (arende.status === 'PENDING_INTERNAL_ACTION') {
                        arende.atgardMessageId = 'svarfranvarden';
                    } else if (arende.status === 'PENDING_EXTERNAL_ACTION') {
                        arende.atgardMessageId = 'svarfranfk';
                    } else {
                        arende.atgardMessageId = '';
                        $log.debug('warning: undefined status');
                    }
                }
            }

        this.updateAnsweredAsHandled = function(deferred, unhandledarendes, fromHandledDialog){
            if(unhandledarendes === undefined || unhandledarendes.length === 0 ){
                return;
            }
            fragaSvarService.closeAllAsHandled(unhandledarendes,
                function(arendes){
                    if(arendes) {
                        angular.forEach(arendes, function(arende) { //unused parameter , key
                            fragaSvarCommonService.decorateSingleItem(arende);
                            if(fromHandledDialog) {
                                arende.proxyMessage = 'common.fk.arenden.marked.as.hanterad';
                            } else {
                                addListMessage(arendes, arende, 'common.fk.arenden.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                            }
                        });
                        statService.refreshStat();
                    }
                    $window.doneLoading = true;
                    if(deferred) {
                        deferred.resolve();
                    }
                },function() { // unused parameter: errorData
                    // show error view
                    $window.doneLoading = true;
                    if(deferred) {
                        deferred.resolve();
                    }
                });
        };


    }]);