(function () {
    angular.module('app').controller('app.views.employees.index', [
        '$scope', '$timeout', '$uibModal', 'abp.services.app.employee',
        function ($scope, $timeout, $uibModal, employeeService) {
            var vm = this;

            vm.employees = [];

            function getEmployees() {
                employeeService.getEmployees({}).then(function (result) {                    
                    vm.employees = result.data.items;
                });
            }
            function openCreateOrEditEmployeeModal(employeeId) {
                var modalInstance = $uibModal.open({
                    templateUrl: '/App/Main/views/employees/createOrEditModal.cshtml',
                    controller: 'app.views.employees.createOrEditModal as vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        employeeId: function () {
                            return employeeId;
                        }
                    }
                });

                modalInstance.rendered.then(function () {
                    $.AdminBSB.input.activate();
                });

                modalInstance.result.then(function () {
                    getEmployees();
                });
            }
            vm.retrainEmployee = function (image, id) {
                let imagePath = "C:\\Data\\6.3.0\\src\\Digitalfuge.EIT.Web\\Temp\\Downloads\\" + image;
                employeeService.trainImage(imagePath,id).then(function (result) {
                    abp.notify.info(App.localize('SavedSuccessfully'));
                    getEmployees();
                });
            };
            vm.createEmployee = function () {
                openCreateOrEditEmployeeModal(null);                
            };
            vm.editEmployee = function (id) {
                openCreateOrEditEmployeeModal(id);
            }
            vm.deleteEmployee = function (id) {
                employeeService.deleteEmployee(id).then(function (result) {
                    abp.notify.info(App.localize('SavedSuccessfully'));
                    getEmployees();
                });
            }
            vm.refresh = function () {
                getEmployees();
            };

            getEmployees();
        }
    ]);
})();