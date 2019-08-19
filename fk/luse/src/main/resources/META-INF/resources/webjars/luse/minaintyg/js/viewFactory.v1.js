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
angular.module('luse').factory('luse.viewFactory.v1', [
  '$stateParams',
  function($stateParams) {
    'use strict';

    var intygsTyp = 'luse';
    var selectRecipientKey = 'modules.page-header.info.select-recipients-and-send';

    var _sendUrl = function() {
      return '/send/' + intygsTyp + '/' + $stateParams.intygTypeVersion + '/' + $stateParams.certificateId + '/FKASSA';
    };

    return {
      intygsTyp: intygsTyp,
      selectRecipientKey: selectRecipientKey,
      getSendUrl: _sendUrl
    };
  }]);
