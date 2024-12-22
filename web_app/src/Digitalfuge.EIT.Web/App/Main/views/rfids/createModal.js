(function () {
    angular.module('app').controller('app.views.rfids.createModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.rfid', 'abp.services.app.room',
        function ($scope, $uibModalInstance, rfidService, roomService) {
            var vm = this;          
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
                rfidService.create(vm.rfid)
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