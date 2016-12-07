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

describe('moduleService', function() {
    'use strict';

    var moduleService;

    beforeEach(angular.mock.module('common', function($provide) {}));

    beforeEach(angular.mock.inject(['common.moduleService', function(_moduleService_) {
        var modules = [
            { id : 'luse', label: 'Läkarutlåtande för sjukersättning'},
            { id : 'lisjp', label: 'Läkarintyg för sjukpenning'},
            { id : 'fk7263', label: 'Läkarintyg FK 7263'}
        ];

        moduleService = _moduleService_;
        moduleService.setModules(modules);
    }]));

    it('should return a module object when module was found', function() {
        var module = moduleService.getModule('lisjp');
        expect(module.id === 'lisjp').toBeTruthy();
        expect(module.label === 'Läkarintyg för sjukpenning').toBeTruthy();
    });

    it('should return null when module wasn\'t found', function() {
        var module = moduleService.getModule('xxxxx');
        expect(module === null).toBeTruthy();
    });

    it('should return null when underlying modules array is empty', function() {
        moduleService.setModules([]);
        var module = moduleService.getModule('lisjp');
        expect(module === null).toBeTruthy();
    });

    it('should return null when underlying modules array is null', function() {
        moduleService.setModules(null);
        var module = moduleService.getModule('lisjp');
        expect(module === null).toBeTruthy();
    });

    it('should return the module\'s name when module was found', function() {
        var name = moduleService.getModuleName('luse');
        expect(name === 'Läkarutlåtande för sjukersättning').toBeTruthy();
    });

    it('should return empty string when module wasn\'t found', function() {
        var name = moduleService.getModuleName('xxxxx');
        expect(name === '').toBeTruthy();
    });

});