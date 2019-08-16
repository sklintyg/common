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
 * Created by BESA on 2015-05-09.
 */

/**
 * arendePanelFraga directive. Handles fraga part of a arendePanel.
 */
angular.module('common').directive('arendePanelFraga',
    ['common.anchorScrollService',
      function(anchorScroll) {
        'use strict';

        return {
          restrict: 'E',
          templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/panel/fraga/arendePanelFraga.directive.html',
          scope: {
            panelId: '@',
            arendeListItem: '='
          },
          link: function(scope) {
            scope.scrollToFraga = function(komplettering) {
              //Both uv-wc-fraga (intyg) and wc-fraga-formly wrapper (utkast) sets this anchor id format
              var target = 'komplettering_' + komplettering.id;

              //Since the actual anchor for komplettering target reside inside a "kategori/fraga" we need a small offset so that
              // the name of the field is visible after scroll.
              anchorScroll.scrollIntygContainerTo(target, parseInt($('#certificate-content-container').offset().top + 60, 10));
            };
          }
        };
      }]);
