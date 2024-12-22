var App = App || {};
(function () {

    var appLocalizationSource = abp.localization.getSource('EIT');
    App.localize = function () {
        return appLocalizationSource.apply(this, arguments);
    };

    App.downloadTempFile = function (file) {
        location.href = abp.appPath + 'File/DownloadTempFile?fileType=' + file.fileType + '&fileToken=' + file.fileToken + '&fileName=' + file.fileName;
    };
    App.createDateRangePickerOptions = function () {
        var format = abp.setting.get("App.General.DateFormat");
        if (!format) {
            //format = 'YYYY-MM-DD';
            format = 'DD-MM-YYYY';
        }
        var todayAsString = moment().format(format);
        var options = {
            locale: {
                format: format,
                applyLabel: App.localize('Apply'),
                cancelLabel: App.localize('Cancel'),
                customRangeLabel: App.localize('CustomRange')
            },
            //min: '2015-05-01',
            //minDate: '2015-05-01',
            max: todayAsString,
            maxDate: todayAsString,
            ranges: {}
        };

        options.ranges[App.localize('Today')] = [moment().startOf('day'), moment().endOf('day')];
        options.ranges[App.localize('Yesterday')] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
        options.ranges[App.localize('Last7Days')] = [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')];
        options.ranges[App.localize('Last30Days')] = [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')];
        options.ranges[App.localize('ThisMonth')] = [moment().startOf('month'), moment().endOf('month')];
        options.ranges[App.localize('LastMonth')] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')];

        return options;
    };

    App.getUserProfilePicturePath = function (profilePictureId) {
        return profilePictureId ?
            (abp.appPath + 'Profile/GetProfilePictureById?id=' + profilePictureId) :
            (abp.appPath + 'Common/Images/default-profile-picture.png');
    }

    App.getUserProfilePicturePath = function () {
        return abp.appPath + 'Profile/GetProfilePicture?v=' + new Date().valueOf();
    }

    App.getShownLinkedUserName = function (linkedUser) {
        if (!abp.multiTenancy.isEnabled) {
            return linkedUser.userName;
        } else {
            if (linkedUser.tenancyName) {
                return linkedUser.tenancyName + '\\' + linkedUser.username;
            } else {
                return '.\\' + linkedUser.username;
            }
        }
    }

    App.notification = App.notification || {};

    App.notification.getUiIconBySeverity = function (severity) {
        switch (severity) {
            case abp.notifications.severity.SUCCESS:
                return 'fa fa-check';
            case abp.notifications.severity.WARN:
                return 'fa fa-warning';
            case abp.notifications.severity.ERROR:
                return 'fa fa-bolt';
            case abp.notifications.severity.FATAL:
                return 'fa fa-bomb';
            case abp.notifications.severity.INFO:
            default:
                return 'fa fa-info';
        }
    };

    App.changeNotifyPosition = function (positionClass) {
        if (!toastr) {
            return;
        }

        toastr.clear();
        toastr.options.positionClass = positionClass;
    };

    App.waitUntilElementIsReady = function (selector, callback, checkPeriod) {
        if (!$) {
            return;
        }

        var elementCount = selector.split(',').length;

        if (!checkPeriod) {
            checkPeriod = 100;
        }

        var checkExist = setInterval(function () {
            if ($(selector).length >= elementCount) {
                clearInterval(checkExist);
                callback();
            }
        }, checkPeriod);
    };

    App.calculateTimeDifference = function (fromTime, toTime, period) {
        if (!moment) {
            return null;
        }

        var from = moment(fromTime);
        var to = moment(toTime);
        return to.diff(from, period);
    }

})(App);