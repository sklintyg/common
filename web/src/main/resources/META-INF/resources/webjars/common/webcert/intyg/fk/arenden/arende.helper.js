angular.module('common').service('common.ArendeHelper',
    ['$log', '$timeout', 'common.ArendeProxy', '$window', 'common.statService',
        function ($log, $timeout, ArendeProxy, $window, statService) {
            'use strict';

            this.updateArendeListItem = function (arendeListItem) {
                _updateListItemState(arendeListItem);
                _updateAtgardMessage(arendeListItem);
            };

            function _updateListItemState(arendeListItem) {
                if (arendeListItem.arende.amne === 'PAMINNELSE') {
                    // RE-020 Påminnelser is never
                    // answerable
                    arendeListItem.answerDisabled = true;
                    arendeListItem.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                } else if ((arendeListItem.arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.arende.amne === 'KOMPLT') && !UserModel.hasPrivilege(UserModel.privileges.BESVARA_KOMPLETTERINGSFRAGA)) {
                    // RE-005, RE-006
                    arendeListItem.answerDisabled = true;
                    arendeListItem.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
                } else {
                    arendeListItem.answerDisabled = false;
                    arendeListItem.answerDisabledReason = undefined;
                }

                if ((arendeListItem.arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.arende.amne === 'KOMPLT') && UserModel.hasRequestOrigin(UserModel.requestOrigins.UTHOPP)) {
                    arendeListItem.svaraMedNyttIntygDisabled = true;
                    arendeListItem.svaraMedNyttIntygDisabledReason = 'Gå tillbaka till journalsystemet för att svara på kompletteringsbegäran med nytt intyg.';
                } else {
                    arendeListItem.svaraMedNyttIntygDisabled = false;
                }
            }

            function _updateAtgardMessage(arendeListItem) {
                if (arendeListItem.arende.status === 'CLOSED') {
                    arendeListItem.atgardMessageId = 'handled';
                } else if (_isUnhandledForDecoration(arendeListItem)) {
                    arendeListItem.atgardMessageId = 'markhandled';
                } else if (arendeListItem.arende.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.arende.amne === 'KOMPLT') {
                    arendeListItem.atgardMessageId = 'komplettering';
                } else {
                    if (arendeListItem.arende.status === 'PENDING_INTERNAL_ACTION') {
                        arendeListItem.atgardMessageId = 'svarfranvarden';
                    } else if (arendeListItem.status === 'PENDING_EXTERNAL_ACTION') {
                        arendeListItem.atgardMessageId = 'svarfranfk';
                    } else {
                        arendeListItem.atgardMessageId = '';
                        $log.debug('warning: undefined status');
                    }
                }
            }

            function _isUnhandledForDecoration(arendeListItem){
                if(!arendeListItem){
                    return false;
                }
                return arendeListItem.arende.status === 'ANSWERED' || arendeListItem.arende.amne === 'MAKULERING' || arendeListItem.arende.amne === 'PAMINNELSE';
            }

        }]);
