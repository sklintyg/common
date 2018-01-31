/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

fdescribe('wcReadMoreTextBlock Directive', function() {
    'use strict';

    var $initialScope;
    var directiveScope;
    var compile;
    var element;

    function runDirective() {
        element = compile('<wc-read-more-text-block text="text" max-chars="{{max}}" text-key="{{textkey}}"></wc-read-more-text-block>')
                ($initialScope);
        $initialScope.$digest();

        directiveScope = element.isolateScope() || element();
    }

    beforeEach(module('htmlTemplates'));
    beforeEach(module('common', function($provide) {
        $provide.value('common.messageService', {
            addResources: function() {
            },
            getProperty: function(prop) {
                return prop;
            }
        });
    }));

    beforeEach(inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        compile = $compile;
        $initialScope = $rootScope.$new();
    } ]));

    describe('verify truncate behaviour', function() {

        it('should not truncate text if length is less than max-chars', function() {
            //Arrange
            $initialScope.text = 'Hej och hå vad det går';
            $initialScope.max = 100;

            //Act
            runDirective();

            //Assert
            expect(directiveScope.vm.text).toBe('Hej och hå vad det går');
            expect($(element).find('.btn-link').length).toBe(0);

        });

        it('should truncate text if length is more than max-chars', function() {
            //Arrange
            $initialScope.text = 'Hej och hå vad det går';
            $initialScope.max = 3;

            //Act
            runDirective();

            //Assert
            expect(directiveScope.vm.text).toEqual('Hej...');
            expect($(element).find('.btn-link').length).toBe(1);

            //Act again.. expand text
            $(element).find('.btn-link').click();
            directiveScope.$digest();

            //Assert again..
            expect(directiveScope.vm.text).toEqual('Hej och hå vad det går');
            expect($(element).find('.btn-link').length).toBe(1);

        });

    });

    describe('verify message source behaviour', function() {

        it('should handle message key resources', function() {
            //Arrange
            $initialScope.textkey = 'testkey';
            $initialScope.max = 100;

            //Act
            runDirective();

            //Assert
            expect(directiveScope.vm.text).toBe('testkey');
            expect($(element).find('.btn-link').length).toBe(0);

        });

        it('should truncate messagekey text if length is more than max-chars', function() {
            //Arrange
            $initialScope.textkey = 'alooongerkey';
            $initialScope.max = 3;

            //Act
            runDirective();

            //Assert
            expect(directiveScope.vm.text).toEqual('alo...');
            expect($(element).find('.btn-link').length).toBe(1);

            //Act again.. expand text
            $(element).find('.btn-link').click();
            directiveScope.$digest();

            //Assert again..
            expect(directiveScope.vm.text).toEqual('alooongerkey');
            expect($(element).find('.btn-link').length).toBe(1);

        });

    });

});
