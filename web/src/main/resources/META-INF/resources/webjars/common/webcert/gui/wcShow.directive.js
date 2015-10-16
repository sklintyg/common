angular.module('common').directive('wcShow',
    function($window, $animate, $log) {
        'use strict';

        return {
            restrict: 'A',
            multiElement: true,
            link: function(scope, element, attr) {
                scope.$watch(attr.wcShow, function ngShowWatchAction(value){

                    $animate[value ? 'removeClass' : 'addClass'](element, 'ng-hide');

                    //var id = element.attr('id');
                    element.on('$animate:before', function() {
                        //$log.debug('------------ animate:before ' + id + ', saving:' + $window.saving + 'hasRegistered : ' + $window.hasRegistered);
                        $window.animations++;
                        $window.rendered = false;
                        //$log.debug('--- '+id+' show animation:before animations:'+ $window.animations + ', rendered:' + $window.rendered);
                    });
                    element.on('$animate:close', function() {
                        //$log.debug('------------- animate:close ' + id  + ', saving:' + $window.saving + 'hasRegistered : ' + $window.hasRegistered);
                        $window.animations--;
                        if ($window.animations === 0) {
                            $window.rendered = true;
                        }
                        //$log.debug('--- '+id+' show animation:close animations:'+ $window.animations + ', rendered:' + $window.rendered);
                    });
                });
            }
        };
    });
