angular.module('common').directive('wcDetectAnimation',
    function($window, $rootScope, $log) {
        'use strict';
        return {
            restrict: 'EA',

            link: function(scope, element, attr) {
                var group = attr.group;
                if(group === undefined){
                    group = 'main';
                }

                if(!$window.animations){
                    $window.animations = {};
                }
                if(!$window.animations[group]){
                    $window.animations[group] = {
                        count:0,
                        startCount:0,
                        endCount:0,
                        rendered:false,
                        ids:{},
                        resetGroup : function(){
                            this.startCount = 0;
                            this.endCount = 0;
                        },
                        resetElements : function(){
                            for (var eid in this.ids) {
                                if (this.ids.hasOwnProperty(eid)) {
                                    this.ids[eid].rendered = false;
                                }
                            }
                        }
                    };
                }

                var id = element.attr('id');

                if($window.animations[group].ids[id] === undefined ){
                    $window.animations[group].count ++;
                    $window.animations[group].ids[id] = {rendered:false};
                }

                var init = function(){
                    element.on('$animate:before', function() {
                        if($window.animations[group].startCount > $window.animations[group].count){
                            $window.animations[group].startCount = 0;
                        }

                        $window.animations[group].startCount ++;
                        $window.animations[group].endCount ++;

                        $window.rendered = false;
                        $window.animations[group].rendered = false;
                        $window.animations[group].ids[id].rendered = false;
                        $rootScope.$broadcast('wcAnimationStart');
                    });
                    element.on('$animate:close', function() {
                        $window.animations[group].endCount --;

                        if($window.animations[group].endCount < 0 ){
                            $window.animations[group].endCount = 0;
                        }
                        if($window.animations[group].endCount === 0) {
                            $window.animations[group].resetGroup();
                            $window.animations[group].rendered = true;
                            $window.rendered = true;
                            $rootScope.$broadcast('wcAllAnimationsEnd');
                        }

                        $window.animations[group].ids[id].rendered = true;
                    });
                };

                init();
            }
        };
    });
