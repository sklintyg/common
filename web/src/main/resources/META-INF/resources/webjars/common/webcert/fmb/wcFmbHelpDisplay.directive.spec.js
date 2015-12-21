/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

describe('wcFmbHelpDisplay', function () {
    'use strict';

    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module('htmlTemplates'));

    var element, outerScope, innerScope;

    beforeEach(inject(function ($rootScope, $compile) {
        element = angular.element('<wc-fmb-help-display diagnosis-code="fmb.diagnosKod" diagnosis-description="fmb.diagnosBeskrivning" help-text-contents="fmb.formData.FORM"></wc-fmb-help-display>');
        outerScope = $rootScope;
        $compile(element)(outerScope);

        outerScope.$digest(); //digest the outerscope before the innerScope is called

        innerScope = element.isolateScope(); //This will get the isolate scope
    }));

    function setUpFMBData(data) {
        outerScope.$apply(function () {
            outerScope.fmb = data;
        });
    }

    describe('correct headlines', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: {},
                diagnosKod: 'J22'
            });
        });

        it('should have a diagnosis headline set', function() {
            expect(element.find('h2').text()).toEqual('J22');
        });
    });

    describe('text are set for forms', function () {
        beforeEach(function() {
            setUpFMBData({
                formData: {FORM: [
                    {heading: 'FALT2_SPB', text: 'Akut bronkit'},
                    {heading: 'FALT2_GENERAL', list: ['BulletText1', 'BulletText2', 'BulletText3']}
                ]},
                diagnosKod: 'J22'
            });
        });

        it('should create a section with text for formdata that is a text node', function() {
            expect(element.find('section').first().text()).toEqual('Akut bronkit');
        });


        it('should create a section with an unordered list for formdata that is a list node', function() {
            expect(element.find('#fmb_bullet_FALT2_GENERAL_0').first().text()).toEqual('BulletText1');
            expect(element.find('#fmb_bullet_FALT2_GENERAL_1').first().text()).toEqual('BulletText2');
            expect(element.find('#fmb_bullet_FALT2_GENERAL_2').first().text()).toEqual('BulletText3');
        });

    });

});

