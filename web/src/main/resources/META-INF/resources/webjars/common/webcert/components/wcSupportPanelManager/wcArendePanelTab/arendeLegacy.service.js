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
angular.module('common').factory('common.ArendeLegacyService',
    function() {
      'use strict';

      function _convertSvarFragestallare(frageStallare) {
        if (frageStallare.toLowerCase() === 'fk') {
          return 'wc';
        } else if (frageStallare.toLowerCase() === 'wc') {
          return 'fk';
        }
      }

      var amneConvertMap = {
        'KOMPLETTERING_AV_LAKARINTYG': 'KOMPLT',
        'MAKULERING_AV_LAKARINTYG': 'MAKULERING_AV_LAKARINTYG',
        'AVSTAMNINGSMOTE': 'AVSTMN',
        'KONTAKT': 'KONTKT',
        'ARBETSTIDSFORLAGGNING': 'ARBETSTIDSFORLAGGNING',
        'PAMINNELSE': 'PAMINN',
        'OVRIGT': 'OVRIGT'
      };

      function _convertAmneArendeToFragasvar(amne) {
        for (var key in amneConvertMap) {
          if (amneConvertMap[key] === amne) {
            return key;
          }
        }
        return null;
      }

      function _convertAmneFragasvarToArende(amne) {
        return amneConvertMap[amne];
      }

      function _convertFragasvarListToArendeList(list) {
        var converted = [];
        angular.forEach(list, function(fs) {
          var arende = _convertFragasvarToArende(fs);
          converted.push(arende);
        });
        return converted;
      }

      function _convertFragasvarViewListToArendeList(list) {
        var converted = [];
        angular.forEach(list, function(fs) {
          var arende = _convertFragasvarViewToArende(fs);
          converted.push(arende);
        });
        return converted;
      }

      function _convertFragasvarViewToArende(fs) {
        var fragaSvar = fs.fragaSvar;

        var arende = null;

        if (fragaSvar) {
          arende = {
            fraga: {
              internReferens: fragaSvar.internReferens,
              frageStallare: fragaSvar.frageStallare,
              amne: _convertAmneFragasvarToArende(fragaSvar.amne),
              meddelande: fragaSvar.frageText,
              meddelandeRubrik: fragaSvar.meddelandeRubrik,
              externaKontakter: fragaSvar.externaKontakter,
              intygId: fragaSvar.intygsReferens.intygsId,
              kompletteringar: fragaSvar.kompletteringar,
              status: fragaSvar.status,
              vidarebefordrad: fragaSvar.vidarebefordrad,
              vardaktorNamn: fragaSvar.vardAktorNamn,
              timestamp: fragaSvar.frageSkickadDatum
            },
            svar: {
              internReferens: fragaSvar.internReferens,
              frageStallare: _convertSvarFragestallare(fragaSvar.frageStallare),
              amne: _convertAmneFragasvarToArende(fragaSvar.amne),
              meddelande: fragaSvar.svarsText,
              meddelandeRubrik: fragaSvar.meddelandeRubrik,
              intygId: fragaSvar.intygsReferens.intygsId,
              kompletteringar: fragaSvar.kompletteringar,
              status: fragaSvar.status,
              vidarebefordrad: fragaSvar.vidarebefordrad,
              vardaktorNamn: fragaSvar.vardAktorNamn,
              svarSkickadDatum: fragaSvar.svarSkickadDatum
            },
            answeredWithIntyg: fs.answeredWithIntyg,
            senasteHandelseDatum: fragaSvar.senasteHandelseDatum,
            paminnelser: [],
            draftText: fs.answerDraft
          };
        }
        return arende;
      }

      function _convertFragasvarToArende(fs) {

        var arende = null;

        if (fs) {
          arende = {
            fraga: {
              internReferens: fs.internReferens,
              frageStallare: fs.frageStallare,
              amne: _convertAmneFragasvarToArende(fs.amne),
              meddelande: fs.frageText,
              meddelandeRubrik: fs.meddelandeRubrik,
              externaKontakter: fs.externaKontakter,
              intygId: fs.intygsReferens.intygsId,
              kompletteringar: fs.kompletteringar,
              status: fs.status,
              vidarebefordrad: fs.vidarebefordrad,
              vardaktorNamn: fs.vardAktorNamn,
              timestamp: fs.frageSkickadDatum
            },
            svar: {
              internReferens: fs.internReferens,
              frageStallare: _convertSvarFragestallare(fs.frageStallare),
              amne: _convertAmneFragasvarToArende(fs.amne),
              meddelande: fs.svarsText,
              meddelandeRubrik: fs.meddelandeRubrik,
              intygId: fs.intygsReferens.intygsId,
              kompletteringar: fs.kompletteringar,
              status: fs.status,
              vidarebefordrad: fs.vidarebefordrad,
              vardaktorNamn: fs.vardAktorNamn,
              svarSkickadDatum: fs.svarSkickadDatum
            },
            answeredWithIntyg: fs.answeredWithIntyg,
            senasteHandelseDatum: fs.senasteHandelseDatum,
            paminnelser: []
          };
        }
        return arende;
      }

      return {
        convertAmneArendeToFragasvar: _convertAmneArendeToFragasvar,
        convertFragasvarListToArendeList: _convertFragasvarListToArendeList,
        convertFragasvarViewListToArendeList: _convertFragasvarViewListToArendeList,
        convertFragasvarToArende: _convertFragasvarToArende,
        convertFragasvarViewToArende: _convertFragasvarViewToArende
      };

    });
