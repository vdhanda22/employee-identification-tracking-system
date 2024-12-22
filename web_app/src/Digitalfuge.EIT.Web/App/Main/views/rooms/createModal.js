(function () {
    angular.module('app').controller('app.views.rooms.createModal', [
        '$scope', '$uibModalInstance','abp.services.app.map', 'abp.services.app.room',
        function ($scope, $uibModalInstance,mapService, roomService) {
            var vm = this;
            vm.room = {};
            vm.maps = [];
            function init() {
                mapService.getAll({}).then(function (result) {
                    vm.maps = result.data.items;
                    if (vm.maps.length == 0) {
                        abp.notify.warn("No maps kindly add some.")
                    }
                });

            }
            vm.save = function () {
                if (!vm.room.mapId) {
                    abp.notify.error("No map selected");
                    return;
                }
                roomService.create(vm.room)
                    .then(function () {
                        abp.notify.info(App.localize('SavedSuccessfully'));
                        $uibModalInstance.close();
                    });
            };

            vm.cancel = function () {
                $uibModalInstance.dismiss({});
            };
            init();
            
        }
    ]);
})();