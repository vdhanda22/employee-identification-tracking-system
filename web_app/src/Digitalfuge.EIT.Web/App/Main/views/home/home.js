(function () {
    var controllerId = 'app.views.home';
    angular.module('app').controller(controllerId, [
        '$scope', 'abp.services.app.user', 'abp.services.app.rfid', 'abp.services.app.room', 'abp.services.app.camera','abp.services.app.analytics',
        function ($scope, userService, rfidService, roomService, cameraService, analyticsService) {
            var vm = this;                    
            vm.dates = [];
            vm.attendance = [];
            vm.showTable = false;
            vm.dateFormat = 'DD-MM-YYYY';
            vm.dateRangeOptions = App.createDateRangePickerOptions();
            vm.dateRangeOptions.max = moment().add(3, 'year').format('DD-MM-YYYY');
            vm.dateRangeOptions.maxDate = moment().add(3, 'year').format('DD-MM-YYYY');
            vm.filters = {
                enableDateRange: false,
                fromDate: null,
                toDate: null
            };
            vm.dateRangeModelChange = function () {
                vm.filters.fromDate = vm.dateRangeModel.startDate != null ? vm.dateRangeModel.startDate : moment().add(-29, 'days').startOf('day');
                vm.filters.toDate = vm.dateRangeModel.endDate != null ? vm.dateRangeModel.endDate : moment().endOf('day');
                vm.showTable = false;
                analyticsService.getAttendanceGraph(vm.filters).then(function (result) {
                    vm.dates = result.data.date;
                    vm.attendance = result.data.number;
                    $scope.myJson = {
                        gui: {
                            contextMenu: {
                                button: {
                                    visible: 0
                                }
                            }
                        },
                        backgroundColor: "#fff",
                        globals: {
                            shadow: false,
                            fontFamily: "Helvetica"
                        },
                        type: "area",

                        legend: {
                            layout: "x4",
                            backgroundColor: "transparent",
                            borderColor: "transparent",
                            marker: {
                                borderRadius: "50px",
                                borderColor: "black"
                            },
                            item: {
                                fontColor: "black"
                            }

                        },
                        scaleX: {
                            //transform: {
                            //    type: 'date',
                            //    "all": "%d.%m.%Y",
                            //},
                            zooming: true,
                            values: vm.dates,
                            lineColor: "black",
                            lineWidth: "1px",
                            tick: {
                                lineColor: "black",
                                lineWidth: "1px"
                            },
                            item: {
                                fontColor: "black"
                            },
                            guide: {
                                visible: false
                            }
                        },
                        scaleY: {
                            lineColor: "black",
                            lineWidth: "1px",
                            tick: {
                                lineColor: "black",
                                lineWidth: "1px"
                            },
                            guide: {
                                lineStyle: "solid",
                                lineColor: "#626262"
                            },
                            item: {
                                fontColor: "black"
                            },
                        },
                        tooltip: {
                            visible: false
                        },
                        crosshairX: {
                            scaleLabel: {
                                backgroundColor: "white",
                                fontColor: "black"
                            },
                            plotLabel: {
                                backgroundColor: "#434343",
                                fontColor: "#FFF",
                                _text: "Number of hits : %v"
                            }
                        },
                        plot: {
                            lineWidth: "2px",
                            aspect: "spline",
                            marker: {
                                visible: false
                            }
                        },
                        series: [{
                            text: "Present Employees",
                            values: vm.attendance,
                            backgroundColor1: "#77d9f8",
                            backgroundColor2: "#272822",
                            lineColor: "#40beeb"
                        }]
                    };
                    vm.showTable = true;
                });
            };
            function getData() {
                userService.getAll({}).then(function (result) {
                    vm.users = result.data.totalCount;
                });
                rfidService.getAll({}).then(function (result) {
                    vm.rfids = result.data.totalCount;
                });
                roomService.getAll({}).then(function (result) {
                    vm.rooms = result.data.totalCount;
                });
                cameraService.getAll({}).then(function (result) {
                    vm.cameras = result.data.totalCount;
                });
                analyticsService.getAttendanceGraph(vm.filters).then(function (result) {
                    vm.dates = result.data.date;
                    vm.attendance = result.data.number;
                    $scope.myJson = {
                        gui: {
                            contextMenu: {
                                button: {
                                    visible: 0
                                }
                            }
                        },
                        backgroundColor: "#fff",
                        globals: {
                            shadow: false,
                            fontFamily: "Helvetica"
                        },
                        type: "area",

                        legend: {
                            layout: "x4",
                            backgroundColor: "transparent",
                            borderColor: "transparent",
                            marker: {
                                borderRadius: "50px",
                                borderColor: "black"
                            },
                            item: {
                                fontColor: "black"
                            }

                        },
                        scaleX: {
                            //transform: {
                            //    type: 'date',
                            //    "all": "%d.%m.%Y",
                            //},
                            zooming: true,
                            values: vm.dates,
                            lineColor: "black",
                            lineWidth: "1px",
                            tick: {
                                lineColor: "black",
                                lineWidth: "1px"
                            },
                            item: {
                                fontColor: "black"
                            },
                            guide: {
                                visible: false
                            }
                        },
                        scaleY: {
                            lineColor: "black",
                            lineWidth: "1px",
                            tick: {
                                lineColor: "black",
                                lineWidth: "1px"
                            },
                            guide: {
                                lineStyle: "solid",
                                lineColor: "#626262"
                            },
                            item: {
                                fontColor: "black"
                            },
                        },
                        tooltip: {
                            visible: false
                        },
                        crosshairX: {
                            scaleLabel: {
                                backgroundColor: "white",
                                fontColor: "black"
                            },
                            plotLabel: {
                                backgroundColor: "#434343",
                                fontColor: "#FFF",
                                _text: "Number of hits : %v"
                            }
                        },
                        plot: {
                            lineWidth: "2px",
                            aspect: "spline",
                            marker: {
                                visible: false
                            }
                        },
                        series: [{
                            text: "Present Employees",
                            values: vm.attendance,
                            backgroundColor1: "#77d9f8",
                            backgroundColor2: "#272822",
                            lineColor: "#40beeb"
                        }]
                    };
                    vm.showTable = true;
                });
            }
            getData();

     
        }
    ]);
})();