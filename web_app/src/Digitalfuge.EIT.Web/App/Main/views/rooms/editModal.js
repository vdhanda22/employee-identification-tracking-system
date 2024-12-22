(function () {
    angular.module('app').controller('app.views.rooms.editModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.map', 'abp.services.app.room', 'id',
        function ($scope, $uibModalInstance,mapService, roomService, id) {
            var vm = this;
            vm.room = {};
            vm.maps = [];

            var init = function () {
                mapService.getAll({}).then(function (result) {
                    vm.maps = result.data.items;
                    if (vm.maps.length == 0) {
                        abp.notify.warn("No maps kindly add some.")
                    }
                });
                roomService.get({ id: id })
                    .then(function (result) {
                        vm.room = result.data;                       
                    });
            }

            vm.save = function () {           
                roomService.update(vm.room)
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