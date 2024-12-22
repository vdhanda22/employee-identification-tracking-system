(function () {
    angular.module('app').controller('app.views.rooms.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.room',
        function ($scope, $timeout, $uibModal, roomService) {
            var vm = this;

            vm.rooms = [];

            function getRoom() {
                roomService.getAll({}).then(function (result) {
                    vm.rooms = result.data.items;
                  
                });
            }

            vm.openRoomCreationModal = function () {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/rooms/createModal.cshtml',
                    controller: 'app.views.rooms.createModal as vm',
                    backdrop: 'static'
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getRoom();
                });
            };

            vm.openRoomEditModal = function (room) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/rooms/editModal.cshtml',
                    controller: 'app.views.rooms.editModal as vm',
                    backdrop: 'static',
                    resolve: {
                        id: function () {
                            return room.id;
                        }
                    }
                });

                modalInstance.rendered.then(function () {
                    $timeout(function () {
                        $.AdminBSB.input.activate();
                    }, 0);
                });

                modalInstance.result.then(function () {
                    getRoom();
                });
            };

            vm.delete = function (room) {
                abp.message.confirm(
                    "Delete room '" + room.name + "'?",
                    "Are you sure?",
                    function (result) {
                        if (result) {
                            roomService.delete({ id: room.id })
                                .then(function () {
                                    abp.notify.info("Deleted user: " + room.name);
                                    getRoom();
                                });
                        }
                    });
            }

            vm.refresh = function () {
                getRoom();
            };

            getRoom();
        }
    ]);
})();