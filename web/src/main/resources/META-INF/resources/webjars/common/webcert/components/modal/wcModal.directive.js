/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcModal',
    ['$timeout','$window','$uibModal','$templateCache', '$http', '$q', '$log', function($timeout, $window, $uibModal, $templateCache, $http, $q, $log) {
        'use strict';

        return {
            restrict: 'E',
            replace: true,
            scope :{
                options:'='
            },
            controller: function($scope/*, $element*/){

                if($scope.options === undefined){
                    return;
                }

                var contentTemplate = '/web/webjars/common/webcert/components/modal/wcModal.content.html',
                    windowTemplate = '/web/webjars/common/webcert/components/modal/wcModal.window.html';

                $scope.modal = {
                    titleId : $scope.options.titleId,
                    extraDlgClass : $scope.options.extraDlgClass,
                    width : $scope.options.width,
                    height : $scope.options.height,
                    maxWidth : $scope.options.maxWidth,
                    maxHeight : $scope.options.maxHeight,
                    minWidth : $scope.options.minWidth,
                    minHeight : $scope.options.minHeight,
                    contentHeight: $scope.options.contentHeight,
                    contentOverflowY : $scope.options.contentOverflowY,
                    contentMinHeight : $scope.options.contentMinHeight,
                    bodyOverflowY: $scope.options.bodyOverflowY,
                    templateUrl: $scope.options.templateUrl === undefined ? contentTemplate : $scope.options.templateUrl,
                    windowTemplateUrl: $scope.options.windowTemplateUrl === undefined ? windowTemplate : $scope.options.windowTemplateUrl,
                    showClose: $scope.options.showClose
                };

                if($scope.modal.bodyOverflowY !== undefined){
                    $scope.modal.bodyOuterStyle = 'height: 76%;' + 'overflow-y: ' + $scope.options.bodyOverflowY + ';';
                }

                if($scope.options.buttons !== undefined && $scope.options.buttons.length > 0){
                    $scope.modal.buttons = [];
                    for(var i=0; i< $scope.options.buttons.length; i++){
                        var button = $scope.options.buttons[i];
                        var className = button.className === undefined ? 'btn-info' : button.className;
                        var mb = {
                            text:button.text,
                            id:button.id,
                            className:className,
                            clickFnName : 'modal.'+ button.name + '()',
                            clickFn : button.clickFn
                        };
                        $scope.modal[button.name] = button.clickFn;
                        $scope.modal.buttons.push(mb);
                    }
                }

                $scope.modal.buttonAction = function(index){
                    $scope.modal.buttons[index].clickFn();
                };

                $scope.open = function ()
                {
                    $scope.modalInstance = $uibModal.open(
                        {
                            backdrop: 'static',
                            keyboard: false,
                            modalFade: true,

                            templateUrl: $scope.modal.templateUrl,
                            template: $scope.modal.template,
                            windowTemplateUrl: $scope.modal.windowTemplateUrl,
                            scope: $scope,
                            //size: size,   - overwritten by the extraDlgClass below (use 'modal-lg' or 'modal-sm' if desired)

                            extraDlgClass: $scope.modal.extraDlgClass,

                            width: $scope.modal.width,
                            height: $scope.modal.height,
                            maxWidth: $scope.modal.maxWidth,
                            maxHeight: $scope.modal.maxHeight,
                            minWidth: $scope.modal.minWidth,
                            minHeight: $scope.modal.minHeight
                        });
                    $scope.options.modalInstance = $scope.modalInstance;

                    $scope.modalInstance.result.then(function ()
                        {
                            $log.info('Modal closed at: ' + new Date());
                        },
                        function ()
                        {
                            $log.info('Modal dismissed at: ' + new Date());
                        });
                };

                $scope.close = function($event){
                    if ($event) {
                        $event.preventDefault();
                    }
                    $scope.modalInstance.dismiss('cancel');
                };

                $scope.cancel = function ($event)
                {
                    if ($event) {
                        $event.preventDefault();
                    }
                    $scope.modalInstance.dismiss('cancel');
                };

                function getTemplatePromise(options) {
                    return options.template ? $q.when(options.template) :
                        $http.get(angular.isFunction(options.templateUrl) ? (options.templateUrl)() : options.templateUrl,
                            {cache: $templateCache}).then(function (result) {
                                return result.data;
                            });
                }

                var def = $q.defer();
                $scope.templatePromise = def.promise;
                getTemplatePromise({templateUrl:$scope.modal.templateUrl}).then(function(modalTemplate){
                    getTemplatePromise({templateUrl:$scope.options.modalBodyTemplateUrl}).then(function(modalBody){
                        // the compiling is done in ui bootstrap
                        // so lets just replace the placeholder in the modal templates html with that
                        // of the modal body
                        var res = modalTemplate.replace('<!-- modalBody -->', modalBody);
                        $scope.modal.template = res;
                        $scope.modal.templateUrl = undefined;
                        def.resolve();
                    });
                });
            },
            link: function(scope/*, element, attrs, ctrl*/) {

                function modalBodyHeight(){
                    $timeout(function(){
                        var header = angular.element('.modal-header').outerHeight();
                        var footer = angular.element('.modal-footer').outerHeight();
                        var modalcontent = angular.element('.modal-content').height();
                        var modal = angular.element(document).height();

                        var modalBody = modal - header - footer- 110;
                        $log.info('header:' + header + ',footer:' + footer +
                            ',modal:' + modal + ',modalcontent:' + modalcontent + ',modalBody:' + modalBody);

                        angular.element('.modal-body').height(modalBody);
                        angular.element('.modal-body').css('overflow-y', scope.modal.bodyOverflowY);
                    });
                }


                scope.templatePromise.then(function(){
                    $log.info('bodyOuterStyle: ' + scope.modal.bodyOuterStyle);
                    scope.open();
                    if(scope.modal !== undefined && scope.modal.bodyOverflowY !== undefined){

                        modalBodyHeight();
                    }
                });

                if(scope.modal !== undefined && scope.modal.bodyOverflowY !== undefined) {
                    var w = angular.element($window);
                    scope.getWindowDimensions = function() {
                        return {
                            'h': w.height(),
                            'w': w.width()
                        };
                    };
                    scope.$watch(scope.getWindowDimensions, function(/*newValue, oldValue*/) {
                        modalBodyHeight();
                    }, true);

                    w.bind('resize', function() {
                        scope.$apply();
                    });
                }
            }
        };
    }]);
