(function () {
    angular.module('app').controller('app.views.maps.createModal', [
        '$scope', '$uibModalInstance', 'FileUploader', 'abp.services.app.map',
        function ($scope, $uibModalInstance, fileUploader, mapService) {
            var vm = this;          
            vm.uploader = new fileUploader({
                url: abp.appPath + 'Profile/UploadMapPicture',
                queueLimit: 1,
                autoUpload: true,
                removeAfterUpload: true,
                filters: [{
                    name: 'imageFilter',
                    fn: function (item, options) {
                        //File type check
                        var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                        if ('|jpg|jpeg|png|'.indexOf(type) === -1) {
                            abp.message.warn("Invalid File Format!!");
                            return false;
                        }

                        //File size check
                        if (item.size > 5242880) //1MB
                        {
                            abp.message.warn("Size Limit Exceded");
                            return false;
                        }

                        return true;
                    }
                }]
            });
            vm.imageClicked = function () {
                $('#ProfileImageFile').click();
            }
            vm.uploader.onSuccessItem = function (fileItem, response, status, headers) {
                if (response.success) {
                    var $profilePictureResize = $('#profileFrame');

                    var profileFilePath = abp.appPath + 'Temp/Maps/' + response.result.fileName + '?v=' + new Date().valueOf();
                    if (!vm.map) {
                        vm.map = {};
                    }
                    vm.map.fileName = response.result.fileName;


                    $profilePictureResize.attr('src', profileFilePath);

                } else {
                    abp.message.error(response.error.message);
                }
            };
            vm.save = function () {
                mapService.create(vm.map)
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