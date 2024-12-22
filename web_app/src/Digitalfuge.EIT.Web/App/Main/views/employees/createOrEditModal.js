(function () {
    angular.module('app').controller('app.views.employees.createOrEditModal', [
        '$scope', 'employeeId', '$uibModalInstance', 'FileUploader','abp.services.app.tag', 'abp.services.app.employee',
        function ($scope, employeeId, $uibModalInstance, fileUploader,tagService, employeeService) {
            var vm = this;
            vm.saving = false;
            vm.isEditMod = false;
            vm.tags = [];

            vm.uploader = new fileUploader({
                url: abp.appPath + 'Profile/UploadProfilePicture',
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
            vm.save = function () {
                employeeService.createOrUpdateEmployee({
                    employee: vm.employee
                }).then(function () {
                    abp.notify.info(App.localize('SavedSuccessfully'));
                    $uibModalInstance.close();
                });
            };

            vm.cancel = function () {
                $uibModalInstance.dismiss({});
            };

            vm.uploader.onSuccessItem = function (fileItem, response, status, headers) {
                if (response.success) {
                    var $profilePictureResize = $('#profileFrame');

                    var profileFilePath = abp.appPath + 'Temp/Downloads/' + response.result.fileName + '?v=' + new Date().valueOf();
                    vm.employee.image = response.result.fileName;


                    $profilePictureResize.attr('src', profileFilePath);

                } else {
                    abp.message.error(response.error.message);
                }
            };
            function getTags() {
                tagService.getUnusedTags().then(function (result) {
                    //if (result.data.length == 0) {
                    //    abp.message.warn('Please Create Some Tags', 'No More Tags');
                    //}
                    vm.tags = result.data;
                   
                });
            }
            function init() {
                getTags();
                employeeService.getEmployeeForEdit({
                    id: employeeId
                }).then(function (result) {
                    vm.employee = result.data;
                    vm.employee.dateOfBirth = new Date(result.data.dateOfBirth);
                    if (vm.employee.id > 0) {
                        vm.isEditMod = true;
                        if (vm.employee.tagId) {
                            tagService.get({ id: vm.employee.tagId })
                                .then(function (result) {                                 
                                    vm.tags.push(result.data);                                    
                                });
                        }
                        

                    } else {
                        vm.isEditMod = false;
                    }
                }).finally(function () {
                    //vm.loading = false;
                });
            }
            init();
        }
    ]);
})();