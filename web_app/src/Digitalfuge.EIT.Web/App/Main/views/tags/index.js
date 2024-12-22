(function () {
    angular.module('app').controller('app.views.tags.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.tag',
        function ($scope, $timeout, $uibModal, tagService) {
            var vm = this;

            vm.tags = [];

            function getTags() {
                tagService.getAll({}).then(function (result) {
                    vm.tags = result.data.items;
                });
            }

            vm.openTagCreationModal = function () {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/tags/createModal.cshtml',
                    controller: 'app.views.tags.createModal as vm',
                    backdrop: 'static'
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getTags();
                });
            };

            vm.openTagEditModal = function (tag) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/tags/editModal.cshtml',
                    controller: 'app.views.tags.editModal as vm',
                    backdrop: 'static',
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
                    getTags();
                });
            };

            vm.delete = function (tag) {
                abp.message.confirm(
                    "Delete tag '" + tag.name + "'?",
                    "Are you sure?",
                    function (result) {
                        if (result) {
                            tagService.delete({ id: tag.id })
                                .then(function () {
                                    abp.notify.info("Deleted user: " + tag.name);
                                    getTags();
                                });
                        }
                    });
            }

            vm.refresh = function () {
                getTags();
            };

            getTags();
        }
    ]);
})();