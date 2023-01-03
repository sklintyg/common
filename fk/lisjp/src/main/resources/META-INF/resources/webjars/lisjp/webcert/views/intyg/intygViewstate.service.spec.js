/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
describe('lisjp.IntygViewStateService', function() {
    'use strict';

    var IntygViewState;

    beforeEach(angular.mock.module('common', function($provide) {
    }));

    beforeEach(angular.mock.inject(['lisjp.IntygController.ViewStateService',
        function(_IntygViewState_) {
            IntygViewState = _IntygViewState_;
        }]));

    describe('send', function() {

        it ('should show observandum in the right conditions (LISJP)', function() {

            /**
             * Visa observandum om:
             * Perioden intyget avser är kortare eller lika med 7 dagar
             * Alternativet Arbetssökande (LISJP) eller Arbetslöshet (FK7263) är EJ valt.
             * Alternativen Studerande och Nuvarande arbete är EJ valda samtidigt (LISJP)
             */

            IntygViewState.intygModel = {
                typ: 'lisjp',
                sjukskrivningar: [
                    {
                        period: {
                            from: '2018-01-01',
                            tom: '2018-01-02'
                        }
                    },
                    {
                        period: {
                            from: '2018-01-03',
                            tom: '2018-01-04'
                        }
                    },
                    {
                        period: {
                            from: '2018-01-06',
                            tom: '2018-01-07'
                        }
                    }
                ],
                sysselsattning: [
                    { typ: 'STUDIER' }
                ]
            };

            // Model starts correct = 7 days duration
            expect(IntygViewState.calculateSjukskrivningDuration()).toBe(7);

            // Model starts correct = 7 days duration
            expect(IntygViewState.getObservandumId()).not.toBe(null);


            // Longer period should result in null message id

            IntygViewState.intygModel.sjukskrivningar = [
                {
                    period: {
                        from: '2018-01-01',
                        tom: '2018-01-02'
                    }
                },
                {
                    period: {
                        from: '2018-01-03',
                        tom: '2018-01-04'
                    }
                },
                {
                    period: {
                        from: '2018-01-06',
                        tom: '2018-01-08' // <--
                    }
                }
            ];

            expect(IntygViewState.calculateSjukskrivningDuration()).toBe(8);
            expect(IntygViewState.getObservandumId()).toBe(null);
            expect(IntygViewState.shouldSysselsattningSpawnObservandum()).toBeTruthy();

            IntygViewState.intygModel.sysselsattning = [
                { typ: 'ARBETSSOKANDE' }
            ];
            expect(IntygViewState.shouldSysselsattningSpawnObservandum()).toBeFalsy();

            IntygViewState.intygModel.sysselsattning = [
                { typ: 'NUVARANDE_ARBETE' },
                { typ: 'STUDIER' }
            ];
            expect(IntygViewState.shouldSysselsattningSpawnObservandum()).toBeFalsy();
        });

    });

});
