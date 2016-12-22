angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        'extends': 'validation-on-blur',
        name: 'multi-text',
        defaultOptions: {
            className: 'fold-animation'
        },
        templateUrl: '/web/webjars/common/webcert/gui/formly/multiText.formly.html',
        controller: ['$scope', 'common.ObjectHelper', 'common.AtticHelper', function($scope, ObjectHelper, AtticHelper) {

            // Auto-resize textareas
            $('.edit-form').on( 'keydown', 'textarea', function (e) {
                $(this).css('height', 'auto' );
                $(this).height( this.scrollHeight );
            });
            $('.edit-form').find('textarea').keydown();

            // atticService doesn't support array syntax with []
            // this is used by tillaggsfragor
            // Since tillaggsfragor are currently not shown/hidden it is safe to skip these
            if ($scope.options.key.substring(0, 14) !== 'tillaggsfragor') {
                // Restore data model value form attic if exists
                AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
            }
        }]
    });
});
