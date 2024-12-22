(function () {
    angular.module('app').controller('app.views.tags.createModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.tag',
        function ($scope, $uibModalInstance, tagService) {
            var vm = this;          

            vm.save = function () {
                console.log(vm.tag);
                tagService.create(vm.tag)
                    .then(function () {
                        abp.notify.info(App.localize('SavedSuccessfully'));
                        $uibModalInstance.close();
                    });
            };

            vm.cancel = function () {
                $uibModalInstance.dismiss({});
            };
        }
    ]);
})();