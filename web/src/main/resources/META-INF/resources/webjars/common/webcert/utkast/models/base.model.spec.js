/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('common.domain.BaseModel', function() {
    'use strict';

    var BaseModel;
    var BaseAtticModel;
    var ModelAttr;
    var ModelTransform;
    var GrundData;

    beforeEach(angular.mock.module('common', function($provide) {
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.BaseModel', 'common.domain.BaseAtticModel', 'common.domain.ModelAttr', 'common.domain.ModelTransformService',
        'common.Domain.GrundDataModel',
        function( _BaseModel_, _BaseAtticModel_,  _modelAttr_, _ModelTransform_, _GrundData_) {
            BaseModel = _BaseModel_;
            BaseAtticModel = _BaseAtticModel_;
            ModelAttr = _modelAttr_;
            ModelTransform = _ModelTransform_;
            GrundData = _GrundData_;
        }]));

    describe('#base model', function() {

        var model;
        beforeEach(function(){

        });

        var print = function(model){
            var properties = model.properties;

            delete model.properties;

            model.properties = properties;
        };

        describe('linked properties', function(){
            it('link a number of properties to a single property', function(){

                var modelDef = {
                    strawberry : false,
                    vanilla : false,
                    chocolate : false,
                    shake : new ModelAttr('shake', {
                        linkedProperty : {
                            props:['strawberry','vanilla','chocolate'],
                            update:function(model, props){
                                if(props.strawberry){
                                    return 'strawberry';
                                } else if(props.vanilla){
                                    return 'vanilla';
                                } else if(props.chocolate){
                                    return 'chocolate';
                                }
                            },
                            set : function(value){
                                this.strawberry = value === 'strawberry';
                                this.vanilla = value === 'vanilla';
                                this.chocolate = value === 'chocolate';
                            }
                        }
                    })

                };

                model = new BaseModel('model1', modelDef);

                var content = {
                    strawberry : false,
                    vanilla : true,
                    chocolate : false};

                model.update(content);

                expect(model.vanilla).toBeTruthy();
                expect(model.shake).toBe('vanilla');

                model.setshake('chocolate');

                expect(model.vanilla).toBeFalsy();
                expect(model.chocolate).toBeTruthy();

                model.strawberry = true;
                expect(model.shake).toBe('strawberry');

            });
        });

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

                model = new BaseModel('model1', modelDef);

                var content = {korkortstyp:[
                    {'type': 'C1', 'selected': 'stooges'},
                    {'type': 'C1E', 'selected': 'mc5'},
                    {'type': 'C', 'selected': 'deviants'}
                ]};

                model.update(content);
                expect(model.korkortstyp[0].selected).toBe('stooges');
                expect(model.korkortstyp[1].selected).toBe('mc5');
                expect(model.korkortstyp[2].selected).toBe('deviants');

                model.clear();
                expect(model.korkortstyp[0].selected).toBe(false);
                expect(model.korkortstyp[1].selected).toBe(false);
                expect(model.korkortstyp[2].selected).toBe(false);

            });
        });

        describe('clear', function(){

            it('can clear single property', function() {

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    },
                    b: undefined
                };
                model = new BaseModel('model1', modelDef );

                print(model);

                // set model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';
                model.b = 'i\'m alone here';

                model.clear('b');

                expect(model.a).toEqual({
                    aa: { aaa: 'hiya aaa', aab: 'hiya aab'},
                    ab : 'hiya cp1'
                });
                expect(model.b).toBe(undefined);

            });

            it('can clear PatientModel property', function() {

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    },
                    grundData: GrundData.build()
                };
                model = new BaseModel('model1', modelDef );

                print(model);

                model.clear('a.aa.aab');

                // set model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';
                model.grundData.patient.postadress = '2nd street';

                model.clear('grundData.patient.postadress');

                expect(model.a).toEqual({
                    aa: { aaa: 'hiya aaa', aab: 'hiya aab'},
                    ab : 'hiya cp1'
                });
                expect(model.grundData.patient.postadress).toBe(undefined);

            });

            it('can clear object ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    },
                    b: undefined
                };
                model = new BaseModel('model1', modelDef );

                print(model);

                // set model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';
                model.b = 'i\'m alone here';

                model.clear('a');

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);

                // reset model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';

                model.clear('a.aa');

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe('hiya cp1');

                model.clear('a.ab');
                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);

            });

            it('can clear all ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    }
                };
                model = new BaseModel('model1', modelDef );

                print(model);

                // set model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';

                model.clear();

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);


            });

            it('can clear all bit more complex ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false,
                        ac : [new ModelAttr('aca', {defaultValue:'oh yeah'}), 'acb']
                    },
                    b :['b1',new ModelAttr('b2',{defaultValue:false})]
                };
                model = new BaseModel('model1', modelDef );

                print(model);

                // set model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';
                model.a.aca = 'aca';
                model.a.acb = 'acb';

                model.b1 = 'hi b1';
                model.b2 = 'hi b2';

                model.clear();

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);
                expect(model.a.aca).toBe('oh yeah');
                expect(model.a.acb).toBe(undefined);

                expect(model.b1).toBe(undefined);
                expect(model.b2).toBe(false);

            });
        });

        describe('update', function(){
            it('can update specific object ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    }
                };
                model = new BaseModel('model1', modelDef );

                var contentApAa = {aaa:'update aaa', aab:'update aab'};

                model.update(contentApAa, 'a.aa');

                expect(model.a.aa.aaa).toBe('update aaa');
                expect(model.a.aa.aab).toBe('update aab');
                expect(model.a.ab).toBe(false);

            });

            it('can update all ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    }
                };
                model = new BaseModel('model1', modelDef );

                var contentA = { a: {
                    aa: { aaa: 'u aaa', aab: 'u aab'},
                    ab : 'u ab'
                } };

                model.update(contentA);

                expect(model.a.aa.aaa).toBe('u aaa');
                expect(model.a.aa.aab).toBe('u aab');
                expect(model.a.ab).toBe('u ab');

            });

            it('can update a single prop ', function(){

                var modelDef = {
                    a: {
                        aa: { aaa: false, aab: 'hi'},
                        ab : false
                    }
                };
                model = new BaseModel('model1', modelDef );

                model.update('fred', 'a.aa.aaa');

                expect(model.a.aa.aaa).toBe('fred');
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);

                model.update('its a monster!', 'a.ab');
                expect(model.a.ab).toBe('its a monster!');

            });

            it('can update a bag of props ', function(){

                var modelDef = {
                    a : [new ModelAttr('aa',{defaultValue:'best'}), 'ab', 'ac'],
                    b : ['ba', new ModelAttr('bb',{defaultValue:'fred'}), 'bc'],
                    c : ['ca', 'cb', 'cc']
                };

                var IntygModel = BaseAtticModel._extend({
                    init: function init(){
                        init._super.call(this, 'model1',modelDef);
                    },
                    update: function update(content, parent) {
                        if (parent) {
                            parent.content = this;
                        }
                        update._super.call(this, content);
                    }
                });
                model = new IntygModel();


                var content = {aa:'uaa', ab:'uab'};

                model.update(content);

                expect(model.aa).toBe('uaa');
                expect(model.ab).toBe('uab');
                expect(model.ac).toBe(undefined);
            });

            it('can update and send using transform to and from functions', function(){

                var fromBackend = {
                    a:[
                        {val:1},
                        {val:3},
                        {val:5}
                    ]
                };

                var modelDef = {
                    'a': new ModelAttr('a',{
                            defaultValue:[],
                            toTransform: ModelTransform.toTypeTransform,
                            fromTransform: ModelTransform.fromTypeTransform
                        })
                };

                var IntygModel = BaseAtticModel._extend({
                    init: function init(){
                        init._super.call(this, 'model1',modelDef);
                    },
                    update: function update(content, parent) {
                        if (parent) {
                            parent.content = this;
                        }
                        update._super.call(this, content);
                    }
                });
                model = new IntygModel();

                model.update(fromBackend);

                expect(model.a[1]).toBeTruthy();
                expect(model.a[2]).toBeFalsy();
                expect(model.a[3]).toBeTruthy();
                expect(model.a[4]).toBeFalsy();
                expect(model.a[5]).toBeTruthy();

                var send = model.toSendModel();
                expect(send.a[0].val).toBe(1);
                expect(send.a[1].val).toBe(3);
                expect(send.a[2].val).toBe(5);
            });

        });

        describe('to send', function() {
            it('can send object ', function() {

                var modelDef = {
                    a: {
                        aa: {aaa: false, aab: 'hi'},
                        ab: false,
                        ac : new ModelAttr('ac', {trans:true}),
                        ad : 'fred'
                    }
                };
                model = new BaseModel('model1', modelDef);

                var contentApAa = {aaa: 'update aaa', aab: 'update aab'};


                model.update(contentApAa, 'a.aa');

                var send = model.toSendModel();

                expect(send.a.aa.aaa).toBe('update aaa');
                expect(send.a.aa.aab).toBe('update aab');
                expect(send.a.ab).toBe(false);
                expect(send.a.ac).toBe(undefined);
                expect(send.a.ad).toBe('fred');

            });

            it('can send specific object ', function() {

                var modelDef = {
                    a: {
                        aa: {aaa: false, aab: 'hi'},
                        ab: false,
                        ac : new ModelAttr('ac', {trans:true}),
                        ad : 'fred'
                    }
                };
                model = new BaseModel('model1', modelDef);

                var contentApAa = {aaa: 'update aaa', aab: 'update aab'};
                model.update(contentApAa, 'a.aa');

                var send = model.toSendModel( 'a.aa');

                expect(send.aaa).toBe('update aaa');
                expect(send.aab).toBe('update aab');

            });

            it('can send array props ', function() {

                var modelDef = {
                    a: false,
                    b : ['ba', 'bb']
                };

                model = new BaseModel('model1', modelDef);

                var content = {a: 'ua', ba: 'uba', bb: 'ubb'};


                model.update(content);

                var send = model.toSendModel();

                expect(send.a).toBe('ua');
                expect(send.ba).toBe('uba');
                expect(send.bb).toBe('ubb');

            });

            it('can send array props, clear and send ', function() {

                var modelDef = {
                    a: false,
                    b : ['ba', 'bb', new ModelAttr('bc',{defaultValue:false})]
                };

                model = new BaseModel('model1', modelDef);

                var content = {a: 'ua', ba: 'uba', bb: 'ubb'};


                model.update(content);

                var send = model.toSendModel();

                expect(send.a).toBe('ua');
                expect(send.ba).toBe('uba');
                expect(send.bb).toBe('ubb');

                // clear b array
                model.clear('b');
                send = model.toSendModel();
                expect(send.a).toBe('ua');
                expect(send.ba).toBe(undefined);
                expect(send.bb).toBe(undefined);
                expect(send.bc).toBe(false);

            });
        });
    });

});
