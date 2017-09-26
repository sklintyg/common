angular.module('db').factory('db.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel) {
            'use strict';

            var DbModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'dbModel', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        'identitetStyrkt': undefined,
                        'dodsdatumSakert': undefined,
                        'dodsdatum': undefined,
                        'antraffatDodDatum': undefined,
                        'dodsplatsKommun': undefined,
                        'dodsplatsBoende': undefined,
                        'barn': undefined,
                        'explosivImplantat': undefined,
                        'explosivAvlagsnat': undefined,
                        'undersokningYttre': undefined,
                        'undersokningDatum': undefined,
                        'polisanmalan': undefined
                    });
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }

            }, {
                build : function(){
                    return new DraftModel(new DbModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return DbModel;

        }]);
