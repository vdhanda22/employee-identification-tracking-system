(function () {
    angular.module('app').controller('app.views.cameras.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.camera',
        function ($scope, $timeout, $uibModal, cameraService) {
            var vm = this;

            vm.cameras = [];

            function getCameras() {
                cameraService.getAll({}).then(function (result) {
                    vm.cameras = result.data.items;
                });
            }

            vm.openCameraCreationModal = function () {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/cameras/createModal.cshtml',
                    controller: 'app.views.cameras.createModal as vm',
                    backdrop: 'static'
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getCameras();
                });
            };

            vm.openCameraEditModal = function (camera) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/cameras/editModal.cshtml',
                    controller: 'app.views.cameras.editModal as vm',
                    backdrop: 'static',
                    resolve: {
                        id: function () {
                            return camera.id;
                        }
                    }
                });

                modalInstance.rendered.then(function () {
                    $timeout(function () {
                        $.AdminBSB.input.activate();
                    }, 0);
                });

                modalInstance.result.then(function () {
                    getCameras();
                });
            };

            vm.delete = function (camera) {
                abp.message.confirm(
                    "Delete camera '" + camera.name + "'?",
                    "Are you sure?",
                    function (result) {
                        if (result) {
                            cameraService.delete({ id: camera.id })
                                .then(function () {
                                    abp.notify.info("Deleted camera: " + camera.name);
                                    getCameras();
                                });
                        }
                    });
            }

            vm.refresh = function () {
                getCameras();
            };

            getCameras();
        }
    ]);
})();