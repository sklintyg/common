angular.module('common').service('common.ArendeHelper',
    ['$log', '$timeout', '$window', 'common.statService', 'common.ArendeListItemModel',
        function ($log, $timeout, $window, statService, ArendeListItemModel) {
            'use strict';

            this.createListItemsFromArenden = function(arendeModelList) {
                var arendeListItemList = [];

                angular.forEach(arendeModelList, function (arendeModel) {
                    arendeListItemList.push(this.createArendeListItem(arendeModel));
                }, this);

                return arendeListItemList;
            };

            this.createArendeListItem = function(arendeModel) {
                var arendeListItem = ArendeListItemModel.build(arendeModel);
                return arendeListItem;
            };

        }]);
