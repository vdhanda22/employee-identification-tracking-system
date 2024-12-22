(function () {
    angular.module('app').controller('app.views.rfids.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.rfid',
        function ($scope, $timeout, $uibModal, rfidService) {
            var vm = this;

            vm.rfids = [];

            function getRfids() {
                rfidService.getAll({}).then(function (result) {
                    vm.rfids = result.data.items;
                });
            }

            vm.openRfidCreationModal = function () {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/rfids/createModal.cshtml',
                    controller: 'app.views.rfids.createModal as vm',
                    backdrop: 'static'
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getRfids();
                });
            };

            vm.openRfidEditModal = function (rfid) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/rfids/editModal.cshtml',
                    controller: 'app.views.rfids.editModal as vm',
                    backdrop: 'static',
                    resolve: {
                        id: function () {
                            return rfid.id;
                        }
                    }
                });

                modalInstance.rendered.then(function () {
                    $timeout(function () {
                        $.AdminBSB.input.activate();
                    }, 0);
                });

                modalInstance.result.then(function () {
                    getRfids();
                });
            };

            vm.delete = function (rfid) {
                abp.message.confirm(
                    "Delete tag '" + rfid.name + "'?",
                    "Are you sure?",
                    function (result) {
                        if (result) {
                            rfidService.delete({ id: rfid.id })
                                .then(function () {
                                    abp.notify.info("Deleted user: " + rfid.name);
                                    getRfids();
                                });
                        }
                    });
            }

            vm.refresh = function () {
                getRfids();
            };

            getRfids();
        }
    ]);
})();