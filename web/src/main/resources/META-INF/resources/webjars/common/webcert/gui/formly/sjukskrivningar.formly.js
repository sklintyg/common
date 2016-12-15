angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'sjukskrivningar',
        templateUrl: '/web/webjars/common/webcert/gui/formly/sjukskrivningar.formly.html',
        controller: ['$scope', 'common.ArendeListViewStateService', 'common.SjukskrivningarViewStateService',
            'common.UtkastValidationService',
            function($scope, ArendeListViewState, viewstate, UtkastValidationService) {

                $scope.$watch('formState.viewState.common.validation.messagesByField', function() {
                    $scope.validationsForPeriod = {};
                    $scope.overlapValidations = [];

                    if (!$scope.formState.viewState.common.validation.messagesByField) {
                        return;
                    }

                    angular.forEach($scope.to.fields, function(field) {

                        var key = $scope.options.key + '.period.' + field.toLowerCase();

                        var fromValidations = $scope.formState.viewState.common.validation.messagesByField[key + '.from'];
                        var tomValidations = $scope.formState.viewState.common.validation.messagesByField[key + '.tom'];

                        $scope.validationsForPeriod[field] = [];
                        if (fromValidations) {
                            $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(fromValidations);
                        }
                        if (tomValidations) {
                            $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(tomValidations);
                        }

                        // The validation message for PERIOD_OVERLAP should only be displayed once even if several periods overlaps
                        angular.forEach($scope.validationsForPeriod[field], function(validation) {
                            if (validation.type === 'PERIOD_OVERLAP' && $scope.overlapValidations.length === 0) {
                                $scope.overlapValidations.push(validation);
                            }
                        });

                        // The validation message for PERIOD_OVERLAP should not be displayed at each period
                        function noPeriodOverlaps(validation) {
                            return validation.type !== 'PERIOD_OVERLAP';
                        }

                        $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].filter(noPeriodOverlaps);
                    });
                });

                $scope.hasValidationError = function(field, type) {
                    var key = $scope.options.key + '.period.' + field + '.' + type;
                    return $scope.formState.viewState.common.validation.messagesByField &&
                        !!$scope.formState.viewState.common.validation.messagesByField[key.toLowerCase()];
                };

                $scope.hasKompletteringar = function() {
                    return ArendeListViewState.hasKompletteringar($scope.options.key);
                };
                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };
                $scope.viewstate = viewstate.reset();

                $scope.$on('intyg.loaded', function() {
                    viewstate.setModel($scope.model[$scope.options.key]);
                    viewstate.updatePeriods();
                });

                $scope.$watch('model.' + $scope.options.key, function(newValue, oldValue) {
                    viewstate.updatePeriods();
                }, true);

            }
        ]
    });

});
