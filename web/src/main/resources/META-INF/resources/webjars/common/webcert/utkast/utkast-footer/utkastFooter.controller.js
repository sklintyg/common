angular.module('common').controller('common.UtkastFooter',
    ['$scope', 'common.ManageCertView',
        function($scope, ManageCertView) {
            'use strict';

            var viewState = $scope.viewState;

            /**
             * Handle vidarebefordra dialog
             */
            $scope.vidarebefordraUtkast = function() {
                ManageCertView.notifyUtkast(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            $scope.onVidarebefordradChange = function() {
                ManageCertView.onNotifyChange(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
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
