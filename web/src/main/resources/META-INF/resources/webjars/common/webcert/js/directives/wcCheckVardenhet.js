/**
 * Det här direktivet används för att verifiera att användaren är inloggad på en specifik vårdenhet. Om användaren inte
 * är inloggad på den vårdenheten så döljs elementet.
 */
angular.module('common').directive('wcCheckVardenhet',
    ['common.User', function(User) {
        'use strict';

        return {
            restrict: 'A',
            scope: {
                vardenhet: '@'
            },
            link: function(scope, element, attrs) {
                attrs.$observe('vardenhet', function() {
                    var found = false;

                    // If vardenhet is set but set to a blank id, ignore the check.
                    if (scope.vardenhet === '') {
                        found = true;
                    }
                    var vardenheter = User.getVardenhetFilterList();
                    for (var i = 0; i < vardenheter.length && !found; i++) {
                        if (vardenheter[i].id === scope.vardenhet) {
                            found = true;
                        }
                    }
                    element.css('display', found ? '' : 'none');
                });
            }
        };
    }]);
