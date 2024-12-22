(function () {
    angular.module('app').controller('app.views.movements.index', [
        '$scope', '$timeout', 'uiGridConstants', '$uibModal', 'abp.services.app.location',  'abp.services.app.employee',
        function ($scope, $timeout, uiGridConstants, $uibModal, locationService, employeeService) {
            var vm = this;
            vm.loading = false;
            vm.employees = [];
            vm.dateFormat = 'DD-MM-YYYY';
            vm.createDateRangePickerOptions = function () {
                var format = 'DD-MM-YYYY';
                if (!format) {
                    //format = 'YYYY-MM-DD';
                    format = 'DD-MM-YYYY';
                }
                var todayAsString = moment().format(format);
                var options = {
                    locale: {
                        format: format,
                        applyLabel: "Apply",
                        cancelLabel: "Cancel",
                        customRangeLabel: "Custom Range"
                    },
                    //min: '2015-05-01',
                    //minDate: '2015-05-01',
                    max: todayAsString,
                    maxDate: todayAsString,
                    ranges: {}
                };

                options.ranges["Today"] = [moment().startOf('day'), moment().endOf('day')];
                options.ranges["Yesterday"] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
                options.ranges["Last7Days"] = [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')];
                options.ranges["Last30Days"] = [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')];
                options.ranges["ThisMonth"] = [moment().startOf('month'), moment().endOf('month')];
                options.ranges["LastMonth"] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')];

                return options;
            };
            vm.dateRangeOptions = vm.createDateRangePickerOptions();
            vm.dateRangeOptions.max = moment().add(3, 'year').format('DD-MM-YYYY');
            vm.dateRangeOptions.maxDate = moment().add(3, 'year').format('DD-MM-YYYY');

            vm.filters = {
                enableDateRange: true,               
                movementType: null,
                employeeId: null,
                fromDate: null,               
                toDate: null,
                amount: null,
                skipCount: 0,
                maxResultCount: 10,
            };
            vm.getlocalDateTime = function (date) {
                if (date != null) {
                    var localTime = moment.utc(date).toDate();
                    return moment(localTime).format(vm.dateFormat);
                }
                else {
                    return "";
                }
            };
            vm.dateRangeCheck = function () {

                if (vm.filters.enableDateRange) {
                    vm.filters.fromDate = vm.dateRangeModel.startDate != null ? vm.dateRangeModel.startDate : moment(moment().format('DD-MM-YYYY')).utc();
                    vm.filters.toDate = vm.dateRangeModel.endDate != null ? vm.dateRangeModel.endDate : moment(moment().format('DD-MM-YYYY')).add(1, 'days').add(-1, 'milliseconds').utc();
                }
                if (!vm.filters.enableDateRange) {
                    vm.filters.fromDate = null;
                    vm.filters.toDate = null;
                }
            };
          
            function getEmployees() {
                employeeService.getEmployees({}).then(function (result) {
                    vm.employees = result.data.items;
                });
            }
            vm.dateRangeModelChange = function () {
                vm.filters.fromDate = vm.dateRangeModel.startDate != null ? vm.dateRangeModel.startDate : moment().add(-29, 'days').startOf('day');
                vm.filters.toDate = vm.dateRangeModel.endDate != null ? vm.dateRangeModel.endDate : moment().endOf('day');
                //vm.getLocations();    
            };
            vm.enterKeyPress = function (eventOnKeyPress) {
                if (eventOnKeyPress.which === 13) {
                    //vm.getInvoices();
                    eventOnKeyPress.preventDefault();
                }
            }
            vm.gridOptions = {
                enableHorizontalScrollbar: uiGridConstants.scrollbars.WHEN_NEEDED,
                enableVerticalScrollbar: uiGridConstants.scrollbars.WHEN_NEEDED,
                paginationPageSizes: [25, 50, 75],
                paginationPageSize: 25,
                enablePaginationControls: true,
                useExternalPagination: true,
                appScopeProvider: vm,
                columnDefs: [                  
                    {
                        name: App.localize('EmployeeName'),
                        field: 'employee.name',
                        width: 220,
                        cellTemplate:
                            '<div class=\"ui-grid-cell-contents\">' +
                            ' <span >' + '{{row.entity.employee.name}}' + '&nbsp' + '</span>' +
                            '</div>'
                    },                   
                    {
                        name: App.localize('Room'),
                        field: 'movementType',
                        cellTemplate:
                            '<div class=\"ui-grid-cell\">' +
                            '<div class=\"ui-grid-cell-contents\"><span>{{row.entity.movementType}}</span></div>' +
                            '</div>'
                    },
                     {
                        name: App.localize('Date'),
                        field: 'reportedOn',
                        cellTemplate:
                            '<div class=\"ui-grid-cell\">' +
                            '<div class=\"ui-grid-cell-contents\">{{grid.appScope.getlocalDateTime(row.entity.reportedOn)}}</div>' +
                            '</div>'
                    }
                    //,
                    //{
                    //    name: App.localize('EmployeeName'),
                    //    field: 'name',
                    //    width: 150,
                    //    cellTemplate:
                    //        '<div class=\"ui-grid-cell pull-right\">' +
                    //        '<div class=\"ui-grid-cell-contents\"> {{grid.appScope.CurrencyFormate(grid.appScope.rounding(row.entity.amount))}}</div>' +
                    //        '</div>'
                    //},
                    //{
                    //    name: App.localize('DueAmount'),
                    //    field: 'dueAmount',
                    //    width: 80,
                    //    cellTemplate:
                    //        '<div class=\"ui-grid-cell pull-right\">' +
                    //        '<div class=\"ui-grid-cell-contents\">{{grid.appScope.CurrencyFormate(row.entity.dueAmount)}}</div>' +
                    //        '</div>'
                    //},
                    //{
                    //    name: App.localize('Status'),
                    //    field: 'status',
                    //    width: 80,
                    //    cellTemplate:
                    //        '<div class=\"ui-grid-cell\">' +
                    //        '<div class=\"ui-grid-cell-contents\">{{grid.appScope.getStatus(row.entity.status)}}</div>' +
                    //        '</div>'
                    //}
                ],

                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (!sortColumns.length || !sortColumns[0].field) {
                            vm.filters.sorting = null;
                        } else {
                            vm.filters.sorting = sortColumns[0].field + ' ' + sortColumns[0].sort.direction;
                        }

                        vm.getLocations();
                    });

                    gridApi.pagination.on.paginationChanged($scope, function (pageNumber, pageSize) {
                        vm.filters.skipCount = (pageNumber - 1) * pageSize;
                        vm.filters.maxResultCount = pageSize;

                        vm.getLocations();
                    });
                },
                data: []
            };
            vm.exportToExcel = function () {
                vm.loading = true;              
                locationService.exportToExcelMovements(vm.filters)
                    .then(function (result) {                      
                        App.downloadTempFile(result.data);
                    }).finally(function () {
                        vm.loading = false;
                    });
            };
            vm.getLocations = function () {
                vm.loading = true;
                if (vm.filters.enableDateRange) {
                    vm.filters.fromDate = vm.dateRangeModel.startDate != null ? vm.dateRangeModel.startDate : moment(moment().format('YYYY-MM-DD')).utc();
                    vm.filters.toDate = vm.dateRangeModel.endDate != null ? vm.dateRangeModel.endDate : moment(moment().format('YYYY-MM-DD')).add(1, 'days').add(-1, 'milliseconds').utc();
                }
                if (!vm.filters.enableDateRange) {
                    vm.filters.fromDate = null;
                    vm.filters.toDate = null;
                }
                locationService.getMovements(vm.filters).then(function (result) {
                    if (vm.dateRangeModel.startDate == null && vm.dateRangeModel.endDate == null)
                        initDate();

                    vm.gridOptions.totalItems = result.totalCount;
                    vm.gridOptions.data = result.data.items;
                    console.log(result.data.items);
                    //vm.gridOptions.data.forEach(function (value) {
                    //    value.printObj = {
                    //        id: value.id,
                    //        printHeader: vm.filters.printingWithoutLogo
                    //    };
                    //});

                    //vm.printAll(null);
                }).finally(function () {
                    vm.loading = false;
                });
            }
           
            function initDate() {
                vm.dateRangeModel = {
                    startDate: moment().add(-29, 'days').startOf('day'),
                    endDate: moment().endOf('day')
                };
            };
            function init() {
                initDate();
                vm.getLocations();
                getEmployees();               
            }
            init();
        }
    ]);
})();