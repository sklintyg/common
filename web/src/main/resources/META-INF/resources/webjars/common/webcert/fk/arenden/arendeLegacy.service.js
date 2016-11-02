angular.module('common').factory('common.ArendeLegacyService',
    function() {
        'use strict';

        function _convertSvarFragestallare(frageStallare) {
            if (frageStallare.toLowerCase() === 'fk') {
                return 'wc';
            }
            else if (frageStallare.toLowerCase() === 'wc') {
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
            for(var key in amneConvertMap) {
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
                console.log(fs);
                var arende = _convertFragasvarToArende(fs);
                console.log(arende);
                converted.push(arende);
            });
            return converted;
        }

        function _convertFragasvarToArende(fs) {
            var arende = {
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
                senasteHandelseDatum: fs.senasteHandelseDatum,
                paminnelser: []
            };
            return arende;
        }

        return {
            convertAmneArendeToFragasvar: _convertAmneArendeToFragasvar,
            convertFragasvarListToArendeList: _convertFragasvarListToArendeList,
            convertFragasvarToArende: _convertFragasvarToArende
        };

    });