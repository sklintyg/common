/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.ArendeListItemModel',
    ['$log', 'common.UserModel', 'common.ObjectHelper',
        function($log, UserModel, ObjectHelper) {
        'use strict';

        /**
         * Constructor
         */
        function ArendeListItemModel(arendeModel) {
            this.answerDisabled = false;
            this.answerDisabledReason = '';
            this.svaraMedNyttIntygDisabled = false;
            this.svaraMedNyttIntygDisabledReason = '';
            this.atgardMessageId = '';
            this.arende = arendeModel; // ArendeModel from backend
            this.kompletteringar = []; // this is created in updateArendeListItem since dynamic text ids needs to be created from arende.fraga.kompletteringar
            this.updateArendeListItem();
        }

        ArendeListItemModel.build = function(arendeModel) {
            return new ArendeListItemModel(arendeModel);
        };

        ArendeListItemModel.prototype.updateArendeListItem = function () {
            this._updateListItemState();
            this._updateAtgardMessage();
            this._updateKompletteringar();
        };

        ArendeListItemModel.prototype._updateListItemState = function() {
            if (this.arende.fraga.amne === 'PAMINNELSE') {
                // RE-020 Påminnelser is never
                // answerable
                this.answerDisabled = true;
                this.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
            } else if (this.arende.fraga.status !== 'CLOSED' &&
                (this.arende.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' || this.arende.fraga.amne === 'KOMPLT') &&
                !UserModel.hasPrivilege(UserModel.privileges.BESVARA_KOMPLETTERINGSFRAGA)) {
                // RE-005, RE-006
                this.answerDisabled = true;
                this.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
            } else {
                this.answerDisabled = false;
                this.answerDisabledReason = undefined;
            }

            if ((this.arende.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' || this.arende.fraga.amne === 'KOMPLT') &&
                UserModel.hasRequestOrigin(UserModel.requestOrigins.UTHOPP)) {
                this.svaraMedNyttIntygDisabled = true;
                this.svaraMedNyttIntygDisabledReason = 'Gå tillbaka till journalsystemet för att svara på kompletteringsbegäran med nytt intyg.';
            } else {
                this.svaraMedNyttIntygDisabled = false;
            }
        };

        ArendeListItemModel.prototype._updateAtgardMessage = function() {
            if (this.arende.fraga.status === 'CLOSED') {
                this.atgardMessageId = 'handled';
            } else if (this._isUnhandledForDecoration()) {
                this.atgardMessageId = 'markhandled';
            } else if (this.arende.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' || this.arende.fraga.amne === 'KOMPLT') {
                this.atgardMessageId = 'komplettering';
            } else {
                if (this.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                    this.atgardMessageId = 'svarfranvarden';
                } else if (this.arende.fraga.status === 'PENDING_EXTERNAL_ACTION') {
                    this.atgardMessageId = 'svarfranfk';
                } else {
                    this.atgardMessageId = '';
                    $log.debug('warning: undefined status');
                }
            }
        };

        function convertFragaToKatLUSE(frageId) {
            switch(Number(frageId)) {
            case 1: case 2:
                return 1;
            case 3: case 4:
                return 2;
            case 5:
                return 3;
            case 6: case 7:
                return 4;
            case 8: case 10: case 11: case 12: case 13: case 14:
                return 5;
                //case 15:
                //case 16:
            case 17:
                return 6;
            case 18: case 19: case 20: case 21:
                return 7;
            case 22: case 23:
                return 8;
                //case 24:
            case 25:
                return 9;
            case 26:
                return 10;
            default:
                return 9999;
            }
        }

        ArendeListItemModel.prototype._updateKompletteringar = function() {
            if (ObjectHelper.isDefined(this.arende.fraga.kompletteringar)) {
                /*
                 frageId:"1"
                 instans:1
                 jsonPropertyHandle:"undersokningAvPatienten"
                 position:0
                 text:"Fixa."
                 */

                this.kompletteringar = [];
                angular.forEach(this.arende.fraga.kompletteringar, function(komplettering){

                    var newKompletteringListItem = {
                        katId: 'KAT_' + convertFragaToKatLUSE(komplettering.frageId) + '.RBK',
                        frgId: 'FRG_' + komplettering.frageId + '.RBK',
                        text: komplettering.text
                    };
                    this.push(newKompletteringListItem);
                }, this.kompletteringar);
            }
        };

        ArendeListItemModel.prototype._isUnhandledForDecoration = function(){
            return this.arende.fraga.status === 'ANSWERED' || this.arende.fraga.amne === 'MAKULERING' || this.arende.fraga.amne === 'PAMINNELSE';
        };

        return ArendeListItemModel;
    }
]);
