/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
    ['$log', 'common.UserModel', 'common.ObjectHelper', 'common.messageService',
      function($log, UserModel, ObjectHelper, messageService) {
        'use strict';

        /**
         * Constructor
         */
        function ArendeListItemModel(arendeModel) {
          this.atgardMessageId = '';
          this.arende = arendeModel; // ArendeModel from backend
          this.kompletteringar = []; // this is created in updateArendeListItem since dynamic text ids needs to be created from arende.fraga.kompletteringar

          this.updateArendeListItem();
        }

        function _isPaminnelse(amne) {
          return amne === 'PAMINNELSE' || amne === 'PAMINN';
        }

        function _isKomplettering(amne) {
          return amne === 'KOMPLETTERING_AV_LAKARINTYG' || amne === 'KOMPLT';
        }

        ArendeListItemModel.build = function(arendeModel) {
          return new ArendeListItemModel(arendeModel);
        };

        ArendeListItemModel.prototype.updateArendeListItem = function() {

          if (!ObjectHelper.isDefined(this.arende.svar)) {
            this.arende.svar = {
              meddelande: ''
            };
          }

          this._updateAtgardMessage();
          this._updateKompletteringar();
        };

        ArendeListItemModel.prototype._updateAtgardMessage = function() {
          if (this.arende.fraga.status === 'CLOSED') {
            this.atgardMessageId = 'handled';
          } else if (this._isUnhandledForDecoration()) {
            this.atgardMessageId = 'markhandled';
          } else if (_isKomplettering(this.arende.fraga.amne)) {
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

        ArendeListItemModel.prototype._updateKompletteringar = function() {
          if (ObjectHelper.isDefined(this.arende.fraga.kompletteringar)) {
            this.kompletteringar = [];
            angular.forEach(this.arende.fraga.kompletteringar, function(komplettering) {

              // Support fraga/svar legacy komplettering (fk7263)
              var newKompletteringListItem = komplettering;

              if (komplettering.frageId) {
                newKompletteringListItem = {
                  frgId: 'FRG_' + komplettering.frageId + '.RBK',
                  text: komplettering.text,
                  modelName: komplettering.jsonPropertyHandle,
                  id: komplettering.frageId
                };
              }

              if (komplettering.jsonPropertyHandle === 'tillaggsfragor') {
                newKompletteringListItem.frgId = 'DFR_' + komplettering.frageId + '.1.RBK';
              }

              this.push(newKompletteringListItem);
            }, this.kompletteringar);
          }
        };

        ArendeListItemModel.prototype._isUnhandledForDecoration = function() {
          return this.arende.fraga.status === 'ANSWERED' || this.arende.fraga.amne === 'MAKULERING' || _isPaminnelse(
              this.arende.fraga.amne);
        };

        ArendeListItemModel.prototype.isOpen = function() {
          return this.arende.fraga.status !== 'CLOSED';
        };

        ArendeListItemModel.prototype.isUnhandled = function() {
          return (this.arende.fraga.status === 'PENDING_INTERNAL_ACTION' && _isPaminnelse(this.arende.fraga.amne)) ||
              this.arende.fraga.status === 'ANSWERED';
        };

        ArendeListItemModel.prototype.isKomplettering = function() {
          return _isKomplettering(this.arende.fraga.amne);
        };

        ArendeListItemModel.prototype.isPaminnelse = function() {
          return _isPaminnelse(this.arende.fraga.amne);
        };

        ArendeListItemModel.prototype.fromFk = function() {
          if (this.arende.fraga.frageStallare === 'FK') {
            return true;
          }
          return false;
        };

        return ArendeListItemModel;
      }
    ]);
