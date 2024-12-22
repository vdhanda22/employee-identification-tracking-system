//(function () {
//    //var dateTimeController = function ($scope, $rootScope) {
//    //    $scope.vm = {
//    //        message: "Bootstrap DateTimePicker Directive",
//    //        dateTime: {}
//    //    };
//    //    
//    //    $scope.$watch('change', function(){
//    //        console.log($scope.vm.dateTime);
//    //    });


//    //    /*
//    //       $scope.$on('emit:dateTimePicker', function (e, value) {
//    //       $scope.vm.dateTime = value.dateTime;
//    //       console.log(value);
//    //       })
//    //    */
//    //};
//    var dateTimePicker = function($rootScope) {

//        return {
//            require: '?ngModel',
//            restrict: 'AE',
//            scope: {
//                pick12HourFormat: '@',
//                language: '@',
//                useCurrent: '@',
//                location: '@'
//            },
//            link: function(scope, elem, attrs) {
//                elem.datetimepicker({
//                    pick12HourFormat: scope.pick12HourFormat,
//                    language: scope.language,
//                    useCurrent: scope.useCurrent
//                });

//                //Local event change
//                elem.on('blur', function() {

//                    console.info('this', this);
//                    console.info('scope', scope);
//                    console.info('attrs', attrs);


//                    // returns moments.js format object
//                    scope.dateTime = new Date(elem.data("DateTimePicker").getDate().format());
//                    // Global change propagation

//                    $rootScope.$broadcast("emit:dateTimePicker", {
//                        location: scope.location,
//                        action: 'changed',
//                        dateTime: scope.dateTime,
//                        example: scope.useCurrent
//                    });
//                    scope.$apply();
//                });
//            }
//        };
//    };

//    angular.module("app", [])//.run(['$rootScope', function ($rootScope) { }])
//        //.controller('dateTimeController', ['$scope', '$http', dateTimeController])
//        .directive('dateTimePicker', dateTimePicker);
//    appModule.directive('dDatepicker', function ($timeout, $parse) {
//        return {
//            link: function ($scope, element, $attrs) {
//                return $timeout(function () {
//                    var ngModelGetter = $parse($attrs['ngModel']);

//                    return $(element).datetimepicker(
//                        {
//                            minDate: moment().add(1, 'd').toDate(),
//                            sideBySide: true,
//                            allowInputToggle: true,
//                            locale: "tr",
//                            useCurrent: false,
//                            defaultDate: moment().add(1, 'd').add(1, 'h'),
//                            icons: {
//                                time: 'icon-back-in-time',
//                                date: 'icon-calendar-outlilne',
//                                up: 'icon-up-open-big',
//                                down: 'icon-down-open-big',
//                                previous: 'icon-left-open-big',
//                                next: 'icon-right-open-big',
//                                today: 'icon-bullseye',
//                                clear: 'icon-cancel'
//                            }
//                        }
//                    ).on('dp.change', function (event) {
//                        $scope.$apply(function () {
//                            return ngModelGetter.assign($scope, event.target.value);
//                        });
//                    });
//                });
//            }
//        };
//    });

//})();

(function () {
    'use strict';

    var module = angular.module('ae-datetimepicker', []);
    //var module = angular.module('app', []);
    module.directive('datetimepicker', [
        '$timeout',
        function ($timeout) {
            return {
                restrict: 'EA',
                require: 'ngModel',
                scope: {
                    options: '=?',
                    onChange: '&?',
                    onClick: '&?'
                },
                link: function ($scope, $element, $attrs, ngModel) {
                    
                    $scope.$watch('options', function (newValue) {
                        var dtp = $element.data('DateTimePicker');
                        $.map(newValue, function (value, key) {
                            dtp[key](value);
                        });
                    });

                    ngModel.$render = function () {
                        if (ngModel.$viewValue !== undefined) {
                            //moment(ngModel.$viewValue)
                            moment.utc(ngModel.$viewValue).local();
                            $element.data('DateTimePicker').date(moment.utc(ngModel.$viewValue).local());
                        }   
                    };

                    $element.on('dp.change', function (e) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                ngModel.$setViewValue(e.date);
                            });
                            if (typeof $scope.onChange === "function") {
                                $scope.onChange();
                            }
                        });
                    });

                    $element.on('click', function () {
                        $timeout(function () {
                            if (typeof $scope.onClick === "function") {
                                $scope.onClick();
                            }
                        });
                    });

                    $element.datetimepicker($scope.options);
                    $timeout(function () {
                        if (ngModel.$viewValue !== undefined && ngModel.$viewValue !== null && $scope.date !== undefined) {
                            if (!(ngModel.$viewValue instanceof moment)) {
                                ngModel.$setViewValue(moment($scope.date));
                            }
                            $element.data('DateTimePicker').date(moment(ngModel.$viewValue));
                        }
                    });
                }
            };
        }
    ]);
   
})();