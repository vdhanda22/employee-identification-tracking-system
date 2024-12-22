(function () {
    angular.module('app').controller('app.views.cameras.createModal', [
        '$scope', '$uibModalInstance','abp.services.app.camera', 'abp.services.app.room',
        function ($scope, $uibModalInstance,cameraService, roomService) {
            var vm = this;
            vm.camera = {};
            vm.rooms = [];
            function init() {
                roomService.getAll({}).then(function (result) {
                    vm.rooms = result.data.items;
                    if (vm.rooms.length == 0) {
                        abp.notify.warn("No rooms kindly add some.")
                    }
                });

            }
            vm.save = function () {
                if (!vm.camera.roomId) {
                    abp.notify.error("No map selected");
                    return;
                }
                cameraService.create(vm.camera)
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