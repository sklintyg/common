angular.module('common').controller('common.UtkastFooter',
    ['$scope', 'common.UtkastService', 'common.UtkastNotifyService',
        function($scope, UtkastService, UtkastNotifyService) {
            'use strict';

            var viewState = $scope.viewState;

            /**
             * Handle vidarebefordra dialog
             */
            $scope.vidarebefordraUtkast = function() {
                UtkastNotifyService.notifyUtkast(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            $scope.onVidarebefordradChange = function() {
                UtkastNotifyService.onNotifyChange(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                UtkastService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                    function(result) {
                        if (result.newVersion) {
                            viewState.draftModel.version = result.newVersion;
                        }
                    }
                );
            };

        }
    ]
);
