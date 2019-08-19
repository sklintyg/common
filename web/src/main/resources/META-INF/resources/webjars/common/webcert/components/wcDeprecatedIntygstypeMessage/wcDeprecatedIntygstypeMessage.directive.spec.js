/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('wcDeprecatedIntygstypeMessage Directive', function() {
    'use strict';

    var $initialScope;
    var directiveScope;
    var compile;
    var element;
    var moduleService;

    function runDirective(intygsType) {
        element = compile(
                '<wc-deprecated-intygstype-message message-suffix="utkastheader" intygs-type="' + intygsType +
                        '"></wc-deprecated-intygstype-message>')($initialScope);
        $initialScope.$digest();
        directiveScope = element.isolateScope() || element();
    }

    beforeEach(module('htmlTemplates'));
    beforeEach(module('common'));

    beforeEach(inject([ '$compile', '$rootScope', 'common.moduleService', function($compile, $rootScope, _moduleService_) {
        compile = $compile;
        $initialScope = $rootScope.$new();
        moduleService = _moduleService_;

        var modules = [ {
            id: 'lisjp',
            deprecated: false
        }, {
            id: 'fk7263',
            deprecated: true,
            displayDeprecated: true
        } ];

        moduleService.setModules(modules);

    } ]));

    describe('Verify display of deprecated alert-message', function() {

        it('Should NOT show message if NOT deprecated', function() {

            //Act
            runDirective('lisjp');

            //Assert
            expect($(element).find('span[key="lisjp.deprecated-message.utkastheader"]').length).toBe(0);
        });

        it('Should show message if deprecated', function() {

            //Act
            runDirective('fk7263');

            //Assert
            expect($(element).find('span[key="fk7263.deprecated-message.utkastheader"]').length).toBe(1);
        });

    });

});
