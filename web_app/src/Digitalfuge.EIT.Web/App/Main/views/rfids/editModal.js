(function () {
    angular.module('app').controller('app.views.rfids.editModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.rfid', 'abp.services.app.room', 'id',
        function ($scope, $uibModalInstance, rfidService,roomService, id) {
            var vm = this;         
            vm.rooms = [];

            var init = function () {
                roomService.getAll({}).then(function (result) {
                    vm.rooms = result.data.items;
                    if (vm.rooms.length == 0) {
                        abp.notify.warn("No rooms kindly add some.")
                    }
                });
                rfidService.get({ id: id })
                    .then(function (result) {
                        vm.rfid = result.data;                       
                    });
            }

            vm.save = function () {           
                rfidService.update(vm.rfid)
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