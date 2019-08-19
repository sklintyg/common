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

describe('common.domain.AtticService', function() {
    'use strict';

    var attic;

    beforeEach(angular.mock.module('common'));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.AtticService',
        function( _attic_) {
            attic = _attic_;
        }]));

    describe('#attic service', function() {

        beforeEach(function(){

        });

        it('can add to attic and get', function(){
            var model = {name:'testModel1',pop1:'prop1', prop2:'prop2'};
            attic.addNewAtticModel(model);
            var atticModel = attic.getAtticModel(model.name);
            expect(atticModel).not.toBe(null);
            expect(atticModel.atticModel.name).toBe(model.name);

            var model2 = {name:'testModel2',pop1:'prop1', prop2:'prop2', prop:'prop3'};
            attic.addNewAtticModel(model2);
            var atticModel2 = attic.getAtticModel(model2.name);
            expect(atticModel2).not.toBe(null);
            expect(atticModel2.atticModel.name).toBe(model2.name);
        });

        it('can update and restore from attic', function(){
            var model = {name:'testModel1',prop1:'original prop1', prop2:'original prop2', properties : ['prop1', 'prop2'] };
            attic.addNewAtticModel(model);
            var atticModel = attic.getAtticModel(model.name);
            expect(atticModel).not.toBe(null);
            expect(atticModel.atticModel.prop1).toBe('original prop1');

            // restore
            model.prop1 = 'new value';

            atticModel.restore(model, ['prop1']);

            expect(model.prop1).toBe('original prop1');

            // update
            model.prop1 = 'new value';

            atticModel.update(model, ['prop1']);

            expect(model.prop1).toBe('new value');

            // restore, should restore the last updated value
            atticModel.restore(model, ['prop1']);

            expect(model.prop1).toBe('new value');

            // restore
            model.prop1 = 'another new value';

            atticModel.restore(model, ['prop1']);

            expect(model.prop1).toBe('new value');

            expect(model.prop2).toBe('original prop2');


        });

        it('can update and restore from attic with all properties', function(){
            var model = {name:'testModel1',prop1:'original prop1', prop2:'original prop2', properties : ['prop1', 'prop2'] };
            attic.addNewAtticModel(model);
            var atticModel = attic.getAtticModel(model.name);
            expect(atticModel).not.toBe(null);
            expect(atticModel.atticModel.prop1).toBe('original prop1');

            // test without properties, this should just take all the properties
            // restore
            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value';

            atticModel.restore(model);

            expect(model.prop1).toBe('original prop1');

            expect(model.prop2).toBe('original prop2');

            // update
            model.prop1 = 'new value';

            atticModel.update(model);

            expect(model.prop1).toBe('new value');
        });

        it('can update and restore from attic with all properties as function', function(){
            var model = {name:'testModel1',prop1:'original prop1', prop2:'original prop2', properties : function(){ return ['prop1', 'prop2']; } };
            attic.addNewAtticModel(model);
            var atticModel = attic.getAtticModel(model.name);
            expect(atticModel).not.toBe(null);
            expect(atticModel.atticModel.prop1).toBe('original prop1');

            // test without properties, this should just take all the properties
            // restore
            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value';

            atticModel.restore(model);

            expect(model.prop1).toBe('original prop1');

            expect(model.prop2).toBe('original prop2');

            // update
            model.prop1 = 'new value';

            atticModel.update(model);

            expect(model.prop1).toBe('new value');
        });

        it('can update and restore from attic with two different models', function(){
            var model = {name:'testModel1',prop1:'original prop1', prop2:'original prop2', properties : ['prop1', 'prop2'] };
            var model2 = {name:'testModel2',prop1:'2 original prop1', prop2:'2 original prop2', properties : ['prop1', 'prop2'] };
            attic.addNewAtticModel(model);
            attic.addNewAtticModel(model2);
            var atticModel = attic.getAtticModel(model.name);

            // model2
            // restore
            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value';

            atticModel.restore(model);

            expect(model.prop1).toBe('original prop1');

            expect(model.prop2).toBe('original prop2');

            // update
            model.prop1 = 'new value';

            atticModel.update(model);

            expect(model.prop1).toBe('new value');

            // model2
            // restore
            var atticModel2 = attic.getAtticModel(model2.name);

            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value';

            atticModel2.restore(model);

            expect(model.prop1).toBe('2 original prop1');

            expect(model.prop2).toBe('2 original prop2');

            // update
            model.prop1 = '2 new value';

            atticModel2.update(model);

            expect(model.prop1).toBe('2 new value');


        });


    });

});
