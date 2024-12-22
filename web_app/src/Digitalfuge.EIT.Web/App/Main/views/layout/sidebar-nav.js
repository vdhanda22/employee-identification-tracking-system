(function () {
    var controllerId = 'app.views.layout.sidebarNav';
    angular.module('app').controller(controllerId, [
        '$rootScope', '$state', 'appSession',
        function ($rootScope, $state, appSession) {
            var vm = this;

            vm.menuItems = [
                createMenuItem(App.localize("HomePage"), "", "home", "home"),

                createMenuItem(App.localize("Trackings"), "Pages.Trackings", "explore", "trackings"),
                createMenuItem(App.localize("Locations"), "Pages.Locations", "room", "locations"),
                createMenuItem(App.localize("Movements"), "Pages.Movements","map","movements"),
                createMenuItem(App.localize("Tenants"), "Pages.Tenants", "business", "tenants"),
                createMenuItem(App.localize("Users"), "Pages.Users", "people", "users"),
                createMenuItem(App.localize("Roles"), "Pages.Roles", "local_offer", "roles"),
                createMenuItem(App.localize("Employees"), "Pages.Employees", "people", "employees"),
                createMenuItem(App.localize("Tags"), "Pages.Tags", "local_offer", "tags"),
                createMenuItem(App.localize("Maps"),"Pages.Maps","location_on","maps"),
                createMenuItem(App.localize("Rooms"), "Pages.Rooms", "hotel", "rooms"),
                createMenuItem(App.localize("Cameras"), "Pages.Cameras", "videocam", "cameras"),
                createMenuItem(App.localize("Rfids"), "Pages.Rfids", "radio", "rfids"),
                createMenuItem(App.localize("About"), "", "info", "about")             
            ];

            vm.showMenuItem = function (menuItem) {
                if (menuItem.permissionName) {
                    return abp.auth.isGranted(menuItem.permissionName);
                }

                return true;
            }

            function createMenuItem(name, permissionName, icon, route, childItems) {
                return {
                    name: name,
                    permissionName: permissionName,
                    icon: icon,
                    route: route,
                    items: childItems
                };
            }
        }
    ]);
})();