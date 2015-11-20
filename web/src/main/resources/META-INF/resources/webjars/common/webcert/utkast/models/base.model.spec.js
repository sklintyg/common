describe('common.domain.BaseModel', function() {
    'use strict';

    var BaseModel;
    var BaseAtticModel;
    var ModelAttr;

    beforeEach(angular.mock.module('common', function($provide) {
    }));

    // Get references to the object we want to test from the context.

    beforeEach(angular.mock.inject([
        'common.domain.BaseModel', 'common.domain.BaseAtticModel', 'common.domain.ModelAttr',
        function( _BaseModel_, _BaseAtticModel_,  _modelAttr_) {
            BaseModel = _BaseModel_;
            BaseAtticModel = _BaseAtticModel_;
            ModelAttr = _modelAttr_;
        }]));

    describe('#base model', function() {

        var model;
        beforeEach(function(){

        });
/*
        var isArray = function(val){
            return val !== undefined && val instanceof Array;
        };

        var isString = function(val){
            return val !== undefined && typeof val === 'string';
        };

        var isObject = function(val){
            return val !== undefined && typeof val === 'object';
        };
*/
        var print = function(model){
            var properties = model.properties;
            ////console.log('-------- properties :');
            ////console.log(JSON.stringify(model.properties));
            ////console.log('--------');

            ////console.log('-------- model :');
            delete model.properties;
            ////console.log(JSON.stringify(model));
            ////console.log('--------');
            model.properties = properties;
        };
/*
        var checkProps = function checkProps(modelDef, model){

            angular.forEach(modelDef, function(val, key){
                if(isString(key) && !isArray(val)){
                    // this is an object property
                    // we need to check the property name
                    expect(model.hasOwnProperty(key)).toBe(true);
                    if(isObject(val)){
                        // send back the object value and the models object value
                        checkProps(val, model[key]);
                    } else {
                        checkProps(key, model);
                    }
                } else if(isArray(val)){
                    checkProps(val, model);
                } else if(typeof modelDef[val] === 'object'){
                    checkProps(modelDef[val], model[val]);
                } else {
                    expect(model.hasOwnProperty(val)).toBe(true);
                }
            });
        };
*/
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
                //console.log('------------------------- enums');
                //console.log('--- model def');

                //console.log(JSON.stringify(model));

                var content = {
                    strawberry : false,
                    vanilla : true,
                    chocolate : false};

                //console.log('--- after update');
                model.update(content);

                //console.log(JSON.stringify(model));
                expect(model.vanilla).toBeTruthy();
                expect(model.shake).toBe('vanilla');

                model.setshake('chocolate');
                
                expect(model.vanilla).toBeFalsy();
                expect(model.chocolate).toBeTruthy();

                model.strawberry = true;
                expect(model.shake).toBe('strawberry');

            });
        });

        xdescribe('enums', function(){
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
                //console.log('------------------------- enums');
                //console.log('--- model def');

                //console.log(JSON.stringify(model));

                var content = {korkortstyp:[
                    {'type': 'C1', 'selected': 'stooges'},
                    {'type': 'C1E', 'selected': 'mc5'},
                    {'type': 'C', 'selected': 'deviants'}
                ]};

                //console.log('--- after update');
                model.update(content);
                //console.log(JSON.stringify(model));
                expect(model.korkortstyp[0].selected).toBe('stooges');
                expect(model.korkortstyp[1].selected).toBe('mc5');
                expect(model.korkortstyp[2].selected).toBe('deviants');

                //console.log('--- after clear');
                model.clear();
                //console.log(JSON.stringify(model));
                expect(model.korkortstyp[0].selected).toBe(false);
                expect(model.korkortstyp[1].selected).toBe(false);
                expect(model.korkortstyp[2].selected).toBe(false);

            });
        });

        describe('clear', function(){
            it('can clear object ', function(){

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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear a');
                ////console.log('+++++++++++++++++++++++++++++++++++');
                model.clear('a');

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe(false);

                // reset model props
                model.a.aa.aaa ='hiya aaa';
                model.a.aa.aab ='hiya aab';
                model.a.ab ='hiya cp1';

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear a.aa');
                ////console.log('+++++++++++++++++++++++++++++++++++');
                model.clear('a.aa');

                expect(model.a.aa.aaa).toBe(false);
                expect(model.a.aa.aab).toBe('hi');
                expect(model.a.ab).toBe('hiya cp1');

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear a.ab');
                ////console.log('+++++++++++++++++++++++++++++++++++');
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear all');
                ////console.log('+++++++++++++++++++++++++++++++++++');
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear all');
                ////console.log('+++++++++++++++++++++++++++++++++++');
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ update a.aa');
                ////console.log('+++++++++++++++++++++++++++++++++++');
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ update all');
                ////console.log('+++++++++++++++++++++++++++++++++++');

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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ update a.aa.aaa');
                ////console.log('+++++++++++++++++++++++++++++++++++');
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
                        ////console.log('update im');
                        if (parent) {
                            parent.content = this;
                        }
                        update._super.call(this, content);
                    }
                });
                model = new IntygModel();

                ////console.log('model :' + JSON.stringify(model));

                var content = {aa:'uaa', ab:'uab'};

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ update a bag');
                ////console.log('+++++++++++++++++++++++++++++++++++');
                model.update(content);

                ////console.log('model after update :' + JSON.stringify(model));

                expect(model.aa).toBe('uaa');
                expect(model.ab).toBe('uab');
                expect(model.ac).toBe(undefined);

                //model.clear('a');
                //
                //expect(model.aa).toBe('best');
                //expect(model.ab).toBe(undefined);
                //expect(model.ac).toBe(undefined);
                //
                //content = {ba:'uba', bb:'ubb'};
                //
                //////console.log('+++++++++++++++++++++++++++++++++++');
                //////console.log('++ update a bb bag');
                //////console.log('+++++++++++++++++++++++++++++++++++');
                //model.update(content, 'b');
                //
                //////console.log('model after update :' + JSON.stringify(model));
                //
                //expect(model.aa).toBe('best');
                //expect(model.ab).toBe(undefined);
                //expect(model.ac).toBe(undefined);
                //
                //expect(model.ba).toBe('uba');
                //expect(model.bb).toBe('ubb');
                //expect(model.bc).toBe(undefined);
                //
                //expect(model.ca).toBe(undefined);
                //expect(model.cb).toBe(undefined);
                //expect(model.cc).toBe(undefined);
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ send all');
                ////console.log('+++++++++++++++++++++++++++++++++++');

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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ send a.aa');
                ////console.log('+++++++++++++++++++++++++++++++++++');

                var send = model.toSendModel( 'a.aa');

                ////console.log('send tm: ' + JSON.stringify(send));
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ send all');
                ////console.log('+++++++++++++++++++++++++++++++++++');

                var send = model.toSendModel();

                ////console.log('send tm: ' + JSON.stringify(send));
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

                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ send all');
                ////console.log('+++++++++++++++++++++++++++++++++++');

                var send = model.toSendModel();

                ////console.log('send tm: ' + JSON.stringify(send));
                expect(send.a).toBe('ua');
                expect(send.ba).toBe('uba');
                expect(send.bb).toBe('ubb');

                // clear b array
                ////console.log('+++++++++++++++++++++++++++++++++++');
                ////console.log('++ clear b');
                ////console.log('+++++++++++++++++++++++++++++++++++');
                model.clear('b');
                send = model.toSendModel();
                ////console.log('send tm: ' + JSON.stringify(send));
                expect(send.a).toBe('ua');
                expect(send.ba).toBe(undefined);
                expect(send.bb).toBe(undefined);
                expect(send.bc).toBe(false);

            });
        });
    });

});