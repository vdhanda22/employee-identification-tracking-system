(function () {
    'use strict';

    var app = angular.module('app', [
        'ngAnimate',
        'ngSanitize',
        'ui.grid',
        'ui.grid.pagination',
        'daterangepicker',
        'angularMoment',
        'ui.router',
        'ui.bootstrap',
        'ui.jq',
        'abp',
        'angularFileUpload',
        'zingchart-angularjs' 
    ]);

    //Configuration for Angular UI routing.
    app.config([
        '$stateProvider', '$urlRouterProvider', '$locationProvider', '$qProvider',
        function ($stateProvider, $urlRouterProvider, $locationProvider, $qProvider) {
            $locationProvider.hashPrefix('');
            $urlRouterProvider.otherwise('/');
            $qProvider.errorOnUnhandledRejections(false);

            if (abp.auth.hasPermission('Pages.Users')) {
                $stateProvider
                    .state('users', {
                        url: '/users',
                        templateUrl: '/App/Main/views/users/index.cshtml',
                        menu: 'Users' //Matches to name of 'Users' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/users');
            }
            if (abp.auth.hasPermission('Pages.Trackings')) {
                $stateProvider
                    .state('trackings', {
                        url: '/trackings',
                        templateUrl: '/App/Main/views/trackings/index.cshtml',
                        menu: 'Trackings' //Matches to name of 'Users' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/trackings');
            }
            if (abp.auth.hasPermission('Pages.Locations')) {
                $stateProvider
                    .state('locations', {
                        url: '/locations',
                        templateUrl: '/App/Main/views/locations/index.cshtml',
                        menu: 'Locations' //Matches to name of 'Users' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/locations');
            }
            if (abp.auth.hasPermission('Pages.Movements')) {
                $stateProvider
                    .state('movements', {
                        url: '/movements',
                        templateUrl: '/App/Main/views/movements/index.cshtml',
                        menu: 'Locations' //Matches to name of 'Users' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/movements');
            }
            if (abp.auth.hasPermission('Pages.Roles')) {
                $stateProvider
                    .state('roles', {
                        url: '/roles',
                        templateUrl: '/App/Main/views/roles/index.cshtml',
                        menu: 'Roles' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/roles');
            }

            if (abp.auth.hasPermission('Pages.Employees')) {
                $stateProvider
                    .state('employees', {
                        url: '/employees',
                        templateUrl: '/App/Main/views/employees/index.cshtml',
                        menu: 'Employees' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/employees');
            }

            if (abp.auth.hasPermission('Pages.Tags')) {
                $stateProvider
                    .state('tags', {
                        url: '/tags',
                        templateUrl: '/App/Main/views/tags/index.cshtml',
                        menu: 'Tags' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/tags');
            }
            if (abp.auth.hasPermission('Pages.Maps')) {
                $stateProvider
                    .state('maps', {
                        url: '/maps',
                        templateUrl: '/App/Main/views/maps/index.cshtml',
                        menu: 'Maps' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/maps');
            }

            if (abp.auth.hasPermission('Pages.Rooms')) {
                $stateProvider
                    .state('rooms', {
                        url: '/rooms',
                        templateUrl: '/App/Main/views/rooms/index.cshtml',
                        menu: 'Rooms' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/rooms');
            }

            if (abp.auth.hasPermission('Pages.Cameras')) {
                $stateProvider
                    .state('cameras', {
                        url: '/cameras',
                        templateUrl: '/App/Main/views/cameras/index.cshtml',
                        menu: 'Cameras' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/cameras');
            }

            if (abp.auth.hasPermission('Pages.Rfids')) {
                $stateProvider
                    .state('rfids', {
                        url: '/rfids',
                        templateUrl: '/App/Main/views/rfids/index.cshtml',
                        menu: 'Rfids' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/rfids');
            }

            if (abp.auth.hasPermission('Pages.Tenants')) {
                $stateProvider
                    .state('tenants', {
                        url: '/tenants',
                        templateUrl: '/App/Main/views/tenants/index.cshtml',
                        menu: 'Tenants' //Matches to name of 'Tenants' menu in EITNavigationProvider
                    });
                $urlRouterProvider.otherwise('/tenants');
            }

            $stateProvider
                .state('home', {
                    url: '/',
                    templateUrl: '/App/Main/views/home/home.cshtml',
                    menu: 'Home' //Matches to name of 'Home' menu in EITNavigationProvider
                })
                .state('about', {
                    url: '/about',
                    templateUrl: '/App/Main/views/about/about.cshtml',
                    menu: 'About' //Matches to name of 'About' menu in EITNavigationProvider
                });
        }
    ]);

})();