describe('common.domain.BaseAtticModel', function() {
    'use strict';

    var BaseAtticModel;
    var attic;
    var ModelAttr;

    beforeEach(angular.mock.module('common', function($provide) {

        // the below are now included in common
        //$provide.value('fk7263.domain.DraftModel', __draftModel__);
        //$provide.value('fk7263.domain.IntygModel', {});
        //$provide.value('fk7263.domain.PatientModel', {});
        //$provide.value('fk7263.domain.SkapadAvModel', {});
        //$provide.value('fk7263.domain.VardenhetModel', {});

    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.BaseAtticModel', 'common.domain.AtticService', 'common.domain.ModelAttr',
        function( _BaseAtticModel_, _atticService_, _modelAttr_) {
            BaseAtticModel = _BaseAtticModel_;
            attic = _atticService_;
            ModelAttr = _modelAttr_;
        }]));

    describe('enums', function(){
        it('can handle an array of objects ', function(){
            
            var modelDef = {
                korkortstyp: new ModelAttr('korkortstyp',
                    {defaultValue:
                        [
                            {'type': 'C1', 'selected': false},
                            {'type': 'C1E', 'selected': false},
                            {'type': 'C', 'selected': false}
                        ]
                    })
            };

            var model = new BaseAtticModel('model1', modelDef);
            console.log('------------------------- enums');
            console.log('--- model def');

            console.log(JSON.stringify(model));

            var content = {korkortstyp:[
                {'type': 'C1', 'selected': 'stooges'},
                {'type': 'C1E', 'selected': 'mc5'},
                {'type': 'C', 'selected': 'deviants'}
            ]};

            console.log('--- after update');
            model.update(content);
            console.log(JSON.stringify(model));
            expect(model.korkortstyp[0].selected).toBe('stooges');
            expect(model.korkortstyp[1].selected).toBe('mc5');
            expect(model.korkortstyp[2].selected).toBe('deviants');

            console.log('--- after clear');
            model.clear();
            console.log(JSON.stringify(model));
            expect(model.korkortstyp[0].selected).toBe(false);
            expect(model.korkortstyp[1].selected).toBe(false);
            expect(model.korkortstyp[2].selected).toBe(false);

            console.log('--- after restore');
            model.restoreFromAttic();
            console.log(JSON.stringify(model));
            expect(model.korkortstyp[0].selected).toBe('stooges');
            expect(model.korkortstyp[1].selected).toBe('mc5');
            expect(model.korkortstyp[2].selected).toBe('deviants');

        });
    });

    xdescribe('base attic model', function(){
        describe('nested attic model', function() {

            var model;

            beforeEach(function() {





            });

            it('can update and restore from attic', function() {
                var modelDef = {
                    a: {
                        aa: {aaa: false, aab: 'hi'},
                        ab: false,
                        ac : new ModelAttr('ac', {defaultValue:undefined,trans:true}),
                        ad : 'fred'
                    }
                };

                model = new BaseAtticModel('NestedModel', modelDef);

                var content = {
                    a:{
                        aa: {aaa: 'update aaa', aab: 'update aab'},
                        ab: true,
                        ac : 'I am not undefined!',
                        ad : 'barney'
                    }
                };

                //console.log('---------------------------- update model');
                model.update(content); // will do an update to attic

                //console.log('attic model :' + JSON.stringify(model.atticModel));

                //console.log('---------------------------- clear model');
                model.clear();

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.ab).toBe(false);
                expect(model.a.ac).toBe(undefined);
                expect(model.a.ad).toBe('fred');


                //console.log('---------------------------- restore from attic');
                model.restoreFromAttic();

                expect(model.a.aa.aaa).toBe('update aaa');
                expect(model.a.ab).toBe(true);
                expect(model.a.ac).toBe('I am not undefined!');
                expect(model.a.ad).toBe('barney');
            });

            it('can update and restore specific object in the model from attic', function() {
                var modelDef = {
                    a: {
                        aa: {aaa: false, aab: 'hi'},
                        ab: {aba:false, abb:'yeah', abc: 'no way'},
                        ac : {aca:false}
                    }
                };

                model = new BaseAtticModel('NestedModel', modelDef);

                var content = {
                    a:{
                        aa: {aaa: 'update aaa', aab: 'update aab'},
                        ab: {aba:true, abb:'abbb', abc: 'yes way'},
                        ac : {aca:true}
                    }
                };

                //console.log('---------------------------- update model');
                model.update(content); // will do an update to attic

                //console.log('attic model :' + JSON.stringify(model.atticModel));

                //console.log('---------------------------- clear model');
                model.clear('a.aa');

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab.aba).toBe(true);


                //console.log('---------------------------- restore from attic');
                model.restoreFromAttic('a.aa');

                expect(model.a.aa.aaa).toBe('update aaa');
                expect(model.a.aa.aab).toBe('update aab');
                expect(model.a.ab.aba).toBe(true);

                model.update({aba:'aba', abb:'abb', abc:'abc'}, 'a.ab');
                expect(model.a.ab.aba).toBe('aba');
                expect(model.a.ab.abb).toBe('abb');
                expect(model.a.ab.abc).toBe('abc');
                model.updateToAttic('a.ab');
                model.clear('a.ab');
                expect(model.a.ab.aba).toBe(false);
                expect(model.a.ab.abb).toBe('yeah');
                expect(model.a.ab.abc).toBe('no way');
                model.restoreFromAttic('a.ab');
                expect(model.a.ab.aba).toBe('aba');
                expect(model.a.ab.abb).toBe('abb');
                expect(model.a.ab.abc).toBe('abc');

            });
        });

        describe('array props attic model', function() {

        var model;

        beforeEach(function(){
            var content = {prop1:'original prop1', prop2:'original prop2' };
            model = new BaseAtticModel('model1', ['prop1', 'prop2'] );
            model.update(content);
            model.updateToAttic();
        });

        it('can update and restore from attic', function(){


            var atticModel = attic.getAtticModel(model.name);
            expect(atticModel).not.toBe(null);
            expect(atticModel.atticModel.prop1).toBe('original prop1');

            // restore
            model.prop1 = 'new value';

            model.restoreFromAttic(['prop1']);

            expect(model.prop1).toBe('original prop1');

            // update
            model.prop1 = 'new value';

            model.updateToAttic(['prop1']);

            expect(model.prop1).toBe('new value');

            // restore, should restore the last updated value
            model.restoreFromAttic(['prop1']);

            expect(model.prop1).toBe('new value');

            // restore
            model.prop1 = 'another new value';

            model.restoreFromAttic(['prop1']);

            expect(model.prop1).toBe('new value');

            expect(model.prop2).toBe('original prop2');


        });

        it('can update and restore from attic with all properties', function(){

            // test without properties, this should just take all the properties
            // restore
            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value'

            model.restoreFromAttic();

            expect(model.prop1).toBe('original prop1');

            expect(model.prop2).toBe('original prop2');

            // update
            model.prop1 = 'new value';

            model.updateToAttic();

            expect(model.prop1).toBe('new value');


        });


        it('can update and restore from attic with two different models', function(){
            var content2 = {prop1:'2 original prop1', prop2:'2 original prop2'};
            var model2 = new BaseAtticModel('model2', ['prop1', 'prop2'] );
            model2.update(content2);
            model2.updateToAttic();

            // model2
            // restore
            model.prop1 = 'prop1 new value';
            model.prop2 = 'prop2 new value'

            model.restoreFromAttic();

            expect(model.prop1).toBe('original prop1');

            expect(model.prop2).toBe('original prop2');

            // update
            model.prop1 = 'new value';

            model.updateToAttic();

            expect(model.prop1).toBe('new value');

            // model2
            // restore
            model2.prop1 = 'prop1 new value';
            model2.prop2 = 'prop2 new value'

            model2.restoreFromAttic();

            expect(model2.prop1).toBe('2 original prop1');

            expect(model2.prop2).toBe('2 original prop2');

            // update
            model2.prop1 = '2 new value';

            model2.updateToAttic();

            expect(model2.prop1).toBe('2 new value');


        });

        describe('#base attic model', function() {

            var model;

            beforeEach(function() {
                var nedsattFromTransform = function(nedsatt) {
                    if (!nedsatt || (!nedsatt.from && !nedsatt.tom)) {
                        return undefined;
                    } else {
                        return nedsatt;
                    }
                };

                var grundData = {somedata:'some grunddata'};

                var IntygModel = BaseAtticModel._extend({
                    init: function init() {
                        init._super.call(this, 'IntygModel', {

                            form1: [new ModelAttr('avstangningSmittskydd', {defaultValue: false})],

                            form2: ['diagnosBeskrivning', 'diagnosBeskrivning1',
                                'diagnosBeskrivning2', 'diagnosBeskrivning3', 'diagnosKod', 'diagnosKod2',
                                'diagnosKod3', 'diagnosKodsystem1', 'diagnosKodsystem2', 'diagnosKodsystem3',
                                new ModelAttr('samsjuklighet', {defaultValue: false})],
                            form3: ['sjukdomsforlopp'],

                            form4: ['funktionsnedsattning'],

                            form4b: ['annanReferens', 'annanReferensBeskrivning', 'journaluppgifter',
                                'telefonkontaktMedPatienten', 'undersokningAvPatienten'],

                            form5: ['aktivitetsbegransning'],

                            form6a: ['rekommendationKontaktArbetsformedlingen', 'rekommendationKontaktForetagshalsovarden',
                                'rekommendationOvrigt', 'rekommendationOvrigtCheck'],

                            form6b: ['atgardInomSjukvarden', 'annanAtgard'],

                            form7: ['rehab'],

                            form8a: ['arbetsloshet', 'foraldrarledighet', 'nuvarandeArbete', 'nuvarandeArbetsuppgifter'],

                            form8b: ['tjanstgoringstid',
                                new ModelAttr('nedsattMed100', {fromTransform: nedsattFromTransform}),
                                new ModelAttr('nedsattMed25', {fromTransform: nedsattFromTransform}),
                                'nedsattMed25Beskrivning',
                                new ModelAttr('nedsattMed50', {fromTransform: nedsattFromTransform}),
                                'nedsattMed50Beskrivning',
                                new ModelAttr('nedsattMed75', {fromTransform: nedsattFromTransform}),
                                'nedsattMed75Beskrivning'],

                            form9: ['arbetsformagaPrognos'],

                            form10: ['arbetsformagaPrognosGarInteAttBedomaBeskrivning', 'prognosBedomning'],

                            form11: ['ressattTillArbeteAktuellt', 'ressattTillArbeteEjAktuellt'],

                            form12: ['kontaktMedFk'],

                            form13: ['kommentar'],

                            misc: ['forskrivarkodOchArbetsplatskod', 'namnfortydligandeOchAdress', 'rehabilitering', 'id',
                                new ModelAttr('grundData', {defaultValue: grundData})]



                        });
                    },

                    update: function update(content) {
                        //if (parent) {
                        //    parent.content = this;
                        //}
                        update._super.call(this, content);
                    }
                });

                model = new IntygModel();
                //console.log('model new : ' + JSON.stringify(model));

            });


            it('can update and restore from intygs model light', function() {


                var content = {diagnosBeskrivning: 'diagnosBeskrivning', diagnosBeskrivning1: 'diagnosBeskrivning1'};
                model.update(content);
                //console.log('model after update : ' + JSON.stringify(model));

                model.updateToAttic();

                expect(model.isInAttic(model.properties.form2)).toBe(true);

                var props = ['diagnosBeskrivning'];

                // restore
                model.diagnosBeskrivning = 'new value';

                model.restoreFromAttic(props);

                expect(model.diagnosBeskrivning).toBe('diagnosBeskrivning');

                // update
                model.diagnosBeskrivning = 'new value';

                model.updateToAttic(props);

                expect(model.diagnosBeskrivning).toBe('new value');

                // ------------- without props

                // reset back to diagnosBeskrivning
                model.diagnosBeskrivning = 'diagnosBeskrivning';

                model.updateToAttic();

                // restore
                model.diagnosBeskrivning = 'new value';

                model.restoreFromAttic();

                expect(model.diagnosBeskrivning).toBe('diagnosBeskrivning');

                // update
                model.diagnosBeskrivning = 'new value';

                model.updateToAttic();

                expect(model.diagnosBeskrivning).toBe('new value');

                expect(model.diagnosBeskrivning1).toBe('diagnosBeskrivning1');

                // --- check toSendModel

                var send = model.toSendModel();

                //console.log('model : ' + JSON.stringify(model));
                //console.log('send : ' + JSON.stringify(send));

                // check that send includes default values
                expect(send.avstangningSmittskydd).toBe(false);

                // check values that have been set
                expect(send.diagnosBeskrivning).toBe('new value');
                expect(send.diagnosBeskrivning1).toBe('diagnosBeskrivning1');


            });

            it('form2', function() {

                var content = {
                    diagnosBeskrivning: 'diagnosBeskrivning',
                    diagnosBeskrivning1: 'diagnosBeskrivning1',
                    diagnosBeskrivning2: 'diagnosBeskrivning2',
                    diagnosBeskrivning3: 'diagnosBeskrivning3',
                    diagnosKod: 'diagnosKod',
                    diagnosKod2: 'diagnosKod2',
                    diagnosKod3: 'diagnosKod3',
                    diagnosKodsystem1: 'diagnosKodsystem1',
                    diagnosKodsystem2: 'diagnosKodsystem2',
                    diagnosKodsystem3: 'diagnosKodsystem3',
                    samsjuklighet: true
                };

                model.update(content);

                var send = model.toSendModel();

                // check that send includes default values
                expect(send.samsjuklighet).toBe(true);

                // check values that have been set
                expect(send.diagnosBeskrivning).toBe('diagnosBeskrivning');
                expect(send.diagnosBeskrivning1).toBe('diagnosBeskrivning1');
                expect(send.diagnosBeskrivning2).toBe('diagnosBeskrivning2');
                expect(send.diagnosBeskrivning3).toBe('diagnosBeskrivning3');
                expect(send.diagnosKod).toBe('diagnosKod');
                expect(send.diagnosKod2).toBe('diagnosKod2');
                expect(send.diagnosKod3).toBe('diagnosKod3');
                expect(send.diagnosKodsystem1).toBe('diagnosKodsystem1');
                expect(send.diagnosKodsystem2).toBe('diagnosKodsystem2');
                expect(send.diagnosKodsystem3).toBe('diagnosKodsystem3');

                model.updateToAttic(model.properties.form2);
                model.clear(model.properties.form2);

                send = model.toSendModel();

                expect(send.samsjuklighet).toBe(false);

                // check values that have been set
                expect(send.diagnosBeskrivning).toBe(undefined);
                expect(send.diagnosBeskrivning1).toBe(undefined);
                expect(send.diagnosBeskrivning2).toBe(undefined);
                expect(send.diagnosBeskrivning3).toBe(undefined);
                expect(send.diagnosKod).toBe(undefined);
                expect(send.diagnosKod2).toBe(undefined);
                expect(send.diagnosKod3).toBe(undefined);
                expect(send.diagnosKodsystem1).toBe(undefined);
                expect(send.diagnosKodsystem2).toBe(undefined);
                expect(send.diagnosKodsystem3).toBe(undefined);

            });

            it('form3', function() {

                var content = {
                    sjukdomsforlopp: 'sjukdomsforlopp'
                };

                model.update(content);

                var send = model.toSendModel();

                // check values that have been set
                expect(send.sjukdomsforlopp).toBe('sjukdomsforlopp');

            });

            it('form4', function() {

                var content = {
                    funktionsnedsattning: 'funktionsnedsattning'
                };

                model.update(content);

                var send = model.toSendModel();

                // check values that have been set
                expect(send.funktionsnedsattning).toBe('funktionsnedsattning');

            });

            it('form4b', function() {

                var content = {
                    annanReferens:'annanReferens',
                    annanReferensBeskrivning:'annanReferensBeskrivning',
                    journaluppgifter : 'journaluppgifter',
                    telefonkontaktMedPatienten : 'telefonkontaktMedPatienten',
                    undersokningAvPatienten : 'undersokningAvPatienten',
                    funktionsnedsattning: 'funktionsnedsattning'
                };

                model.update(content);

                var send = model.toSendModel();

                // check values that have been set
                expect(send.annanReferens).toBe('annanReferens');
                expect(send.annanReferensBeskrivning).toBe('annanReferensBeskrivning');
                expect(send.journaluppgifter).toBe('journaluppgifter');
                expect(send.telefonkontaktMedPatienten).toBe('telefonkontaktMedPatienten');
                expect(send.undersokningAvPatienten).toBe('undersokningAvPatienten');

                model.clear(model.properties.form4b);
                send = model.toSendModel();

                expect(send.annanReferens).toBe(undefined);
                expect(send.annanReferensBeskrivning).toBe(undefined);
                expect(send.journaluppgifter).toBe(undefined);
                expect(send.telefonkontaktMedPatienten).toBe(undefined);
                expect(send.undersokningAvPatienten).toBe(undefined);

                expect(send.funktionsnedsattning).toBe('funktionsnedsattning');
                expect(send.samsjuklighet).toBe(false);

            });

            it('form5', function() {

                var content = {
                    aktivitetsbegransning: 'aktivitetsbegransning'
                };

                model.update(content);

                var send = model.toSendModel();

                // check values that have been set
                expect(send.aktivitetsbegransning).toBe('aktivitetsbegransning');

            });

        });


    });
    });

});