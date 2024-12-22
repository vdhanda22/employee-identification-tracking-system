(function () {
    angular.module('app').controller('app.views.tags.editModal', [
        '$scope', '$uibModalInstance', 'abp.services.app.tag', 'id',
        function ($scope, $uibModalInstance, tagService, id) {
            var vm = this;
           
            

            var init = function () {
                tagService.get({ id: id })
                    .then(function (result) {
                        vm.tag = result.data;                       
                    });
            }

            vm.save = function () {           
                tagService.update(vm.tag)
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