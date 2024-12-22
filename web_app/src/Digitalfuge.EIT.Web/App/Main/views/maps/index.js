(function () {
    angular.module('app').controller('app.views.maps.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.map',
        function ($scope, $timeout, $uibModal, mapService) {
            var vm = this;
            vm.maps = [];

            function getMaps() {
                mapService.getAll({}).then(function (result) {
                    vm.maps = result.data.items;
                });
            }
            vm.openMapCreationModal = function () {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/maps/createModal.cshtml',
                    controller: 'app.views.maps.createModal as vm',
                    backdrop: 'static',
                    size: 'lg',
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getMaps();
                });
            };

            vm.openMapEditModal = function (tag) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/maps/editModal.cshtml',
                    controller: 'app.views.maps.editModal as vm',
                    backdrop: 'static',
                    size: 'lg', 
                    resolve: {
                        id: function () {
                            return tag.id;
                        }
                    }
                });

                modalInstance.rendered.then(function () {
                    $timeout(function () {
                        $.AdminBSB.input.activate();
                    }, 0);
                });

                modalInstance.result.then(function () {
                    getMaps();
                });
            };

            vm.delete = function (map) {
                abp.message.confirm(
                    "Delete map '" + map.name + "'?",
                    "Are you sure?",
                    function (result) {
                        if (result) {
                            mapService.delete({ id: map.id })
                                .then(function () {
                                    abp.notify.info("Deleted map: " + map.name);
                                    getMaps();
                                });
                        }
                    });
            }

            vm.refresh = function () {
                getMaps();
            };

            getMaps();
        }
    ]);
})();