(function () {
    angular.module('app').controller('app.views.cameras.editModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.camera', 'abp.services.app.room', 'id',
        function ($scope, $uibModalInstance,cameraService, roomService, id) {
            var vm = this;
            vm.camera = {};
            vm.rooms = [];

            var init = function () {
                roomService.getAll({}).then(function (result) {
                    vm.rooms = result.data.items;
                    if (vm.rooms.length == 0) {
                        abp.notify.warn("No rooms kindly add some.")
                    }
                });
                cameraService.get({ id: id })
                    .then(function (result) {
                        vm.camera = result.data;                       
                    });
            }

            vm.save = function () {           
                cameraService.update(vm.camera)
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